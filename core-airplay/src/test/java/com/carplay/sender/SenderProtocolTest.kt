package com.carplay.sender

import com.carplay.airplay.*
import org.junit.Assert.*
import org.junit.Test
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.UUID
import java.util.concurrent.CountDownLatch

class SenderProtocolTest {
    @Test fun controlFramingRetainsFragmentedAndCoalescedResponses() {
        val response = "RTSP/1.0 200 OK\r\nCSeq: 4\r\nContent-Length: 3\r\n\r\nabc".toByteArray()
        val first = ControlFraming.parse(response.copyOfRange(0,response.size-1))
        assertTrue(first.messages.isEmpty())
        val complete = ControlFraming.parse(first.rest + response.last() + response)
        assertEquals(2,complete.messages.size); assertEquals(200,complete.messages.first().status)
        assertEquals("4",complete.messages.first().headers["cseq"]); assertTrue(complete.rest.isEmpty())
    }
    @Test fun invalidControlLengthsAndDuplicateHeadersAreRejected() {
        for (headers in listOf("Content-Length: -1", "Content-Length: 3000000", "Content-Length: 1\r\nContent-Length: 1")) {
            assertThrows(IllegalArgumentException::class.java) { ControlFraming.parse("RTSP/1.0 200 OK\r\n$headers\r\n\r\n".toByteArray()) }
        }
    }
    @Test fun resourceTransfersMatchReferenceAndRejectedBatchIsAtomic() {
        val state = SessionModes()
        fun change(type: Int) = state.change(mapOf("type" to "changeModes", "params" to mapOf("resources" to listOf(mapOf("resourceID" to 1, "transferType" to type)))))
        /*
         * A session opens with both resources on the car — that is what a real
         * iPhone reports on this car at 07:58:22.535, before it claims anything.
         * The transfers below therefore start from the car's side.
         */
        assertFalse(state.ownsScreen()); assertFalse(state.ownsAudio())
        change(3); assertFalse(state.ownsScreen())
        change(4); assertFalse(state.ownsScreen())   // permanent is still the car
        state.claimScreen(); assertTrue(state.ownsScreen())
        change(3); assertFalse(state.ownsScreen()); change(4); assertTrue(state.ownsScreen())
        change(1); change(3); change(4); assertFalse(state.ownsScreen()); change(2); assertTrue(state.ownsScreen())
        assertThrows(IllegalArgumentException::class.java) { state.change(mapOf("type" to "changeModes", "params" to mapOf("resources" to listOf(mapOf("resourceID" to 1,"transferType" to 1),mapOf("resourceID" to 999,"transferType" to 1))))) }
        assertTrue(state.ownsScreen())
    }
    @Test fun resourceRestoreIdentifiesScreenAndAudioSeparately() {
        val state = SessionModes()
        state.claimScreen()
        val screen = state.change(mapOf("type" to "changeModes", "params" to mapOf("resources" to listOf(mapOf("resourceID" to 1, "transferType" to 3)))) )
        assertFalse(screen.screenRestored)
        assertFalse(screen.audioRestored)
        val screenBack = state.change(mapOf("type" to "changeModes", "params" to mapOf("resources" to listOf(mapOf("resourceID" to 1, "transferType" to 4)))) )
        assertTrue(screenBack.screenRestored)
        assertFalse(screenBack.audioRestored)
        val audio = state.change(mapOf("type" to "changeModes", "params" to mapOf("resources" to listOf(mapOf("resourceID" to 2, "transferType" to 1)))) )
        assertFalse(audio.screenRestored)
        assertFalse(audio.audioRestored)
        val audioBack = state.change(mapOf("type" to "changeModes", "params" to mapOf("resources" to listOf(mapOf("resourceID" to 2, "transferType" to 2)))) )
        assertFalse(audioBack.screenRestored)
        assertTrue(audioBack.audioRestored)
    }

    @Test fun requestUiReclaimAlsoChangesPermanentScreenOwner() {
        val state = SessionModes()
        fun change(type: Int) = state.change(
            mapOf(
                "type" to "changeModes",
                "params" to mapOf(
                    "resources" to listOf(mapOf("resourceID" to 1, "transferType" to type)),
                ),
            ),
        )

        // The car takes the screen permanently, then temporarily. requestUI
        // must make the phone the permanent owner before the next type=4.
        change(1)
        change(3)
        assertTrue(state.claimScreen())
        val resource = ((state.snapshot()["params"] as Map<*, *>)["resources"] as List<*>)
            .map { it as Map<*, *> }
            .first { (it["resourceID"] as Number).toLong() == 1L }
        assertEquals(1L, (resource["entity"] as Number).toLong())
        assertEquals(1L, (resource["permanentEntity"] as Number).toLong())
        change(4)
        assertTrue(state.ownsScreen())
    }

    @Test fun audioGrantIsReflectedInModesSnapshot() {
        val state = SessionModes()

        // Before SETUP, the head unit owns the audio resource by default.
        val initialResources = (state.snapshot()["params"] as Map<*, *>)["resources"] as List<*>
        val initialAudio = initialResources
            .map { it as Map<*, *> }
            .first { (it["resourceID"] as Number).toLong() == 2L }
        assertEquals(2L, (initialAudio["entity"] as Number).toLong())
        assertEquals(2L, (initialAudio["permanentEntity"] as Number).toLong())

        // AirPlaySender calls this before its first modesChanged notification
        // when audio is enabled; the declaration must match the packets sent.
        state.audio(true)
        val grantedResources = (state.snapshot()["params"] as Map<*, *>)["resources"] as List<*>
        val grantedAudio = grantedResources
            .map { it as Map<*, *> }
            .first { (it["resourceID"] as Number).toLong() == 2L }
        assertEquals(1L, (grantedAudio["entity"] as Number).toLong())
        /*
         * `permanentEntity` stays on the car.
         *
         * A real iPhone on this car keeps `mainAudio (permanent: Car)` for the
         * whole session while `owner` moves to iOS — every `Modes changed` line
         * from 07:58:22.545 to 07:58:53.149 in the 2026-10-31 sysdiagnose says
         * so. Moving both together claimed audio permanently, which does not
         * match what the car observes from a genuine phone.
         */
        assertEquals(2L, (grantedAudio["permanentEntity"] as Number).toLong())
        assertTrue(state.ownsAudio())
    }

    @Test fun missingTrustRootsCannotAuthenticate() {
        assertThrows(IllegalStateException::class.java) { AccessoryTrust(emptyList()).validate(byteArrayOf(1)) }
    }
}
