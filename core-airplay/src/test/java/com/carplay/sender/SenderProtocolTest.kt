package com.carplay.sender

import com.carplay.airplay.*
import org.junit.Assert.*
import org.junit.Test
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class SenderProtocolTest {
    @Test fun controllerPairsAndVerifiesAgainstOriginalAccessoryResponder() {
        val accessory = AirPlayIdentity.generate(); val store = PairingStore()
        val receiverSetup = PairSetup(accessory,store)
        val receiverVerify = PairVerify(accessory,store)
        val keyPair = AirPlayCrypto.ed25519Generate()
        val identity = ControllerIdentity(keyPair.privateKey,keyPair.publicKey,UUID.randomUUID().toString())
        var saved: PairedAccessory? = null
        val phone = PhonePairing(identity,{ path,body -> if (path == "/pair-setup") receiverSetup.handle(body) else receiverVerify.handle(body) },{ saved = it })
        val peer = phone.setup("3939",accessory.publicKey)
        assertEquals(peer,saved); assertEquals(accessory.pairingId,peer.id)
        val shared = phone.verify(peer)
        assertArrayEquals(shared,receiverVerify.shared)
        assertTrue(receiverVerify.isVerified)
        assertEquals(identity.id,receiverVerify.verifiedControllerId)
    }
    @Test fun controllerRejectsChangedAdvertisedPairingKey() {
        val accessory = AirPlayIdentity.generate(); val receiver = PairSetup(accessory,PairingStore())
        val key = AirPlayCrypto.ed25519Generate()
        val phone = PhonePairing(ControllerIdentity(key.privateKey,key.publicKey,"controller"),{ _,body -> receiver.handle(body) },{ fail("Unverified pair must not persist") })
        assertThrows(IllegalStateException::class.java) { phone.setup("3939",ByteArray(32)) }
    }
    @Test fun videoPacketDecryptsUsingOriginalReceiverAndPreservesTimestamp() {
        val key = ByteArray(32) { it.toByte() }; val payload = byteArrayOf(0,0,0,2,0x65,0x42)
        val packet = VideoPackets.frame(payload,key,11,0x123456789L)
        val header = packet.copyOfRange(0,128)
        assertEquals(payload.size+16,ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN).int)
        assertArrayEquals(payload,ScreenCodec.decryptFrame(key,11,header,packet.copyOfRange(128,packet.size)))
        assertEquals(0x123456789L,ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN).getLong(8))
        assertThrows(Exception::class.java) { ScreenCodec.decryptFrame(key,12,header,packet.copyOfRange(128,packet.size)) }
    }
    @Test fun screenConfigCarriesCodecOptionAndTheFollowingFramesTimestamp() {
        val packet = VideoPackets.config(byteArrayOf(1,2,3), 1408, 476, 0x1122334455667788L)
        val header = ByteBuffer.wrap(packet, 0, 128).order(ByteOrder.LITTLE_ENDIAN)
        assertEquals(3, header.getInt(0))
        // 0x01 0x00 = unencrypted SPS/PPS; 0x16 0x01 is the option pair a real
        // h264 sender writes (UxPlay raop_rtp_mirror.c).
        assertEquals(1, header.get(4).toInt())
        assertEquals(0, header.get(5).toInt())
        assertEquals(0x16, header.get(6).toInt() and 0xff)
        assertEquals(0x01, header.get(7).toInt())
        // The receiver pairs the config with the IDR that follows it by this
        // timestamp and drops the SPS/PPS when the two differ.
        assertEquals(0x1122334455667788L, header.getLong(8))
        assertEquals(1408f, header.getFloat(16), 0f)
        assertEquals(476f, header.getFloat(20), 0f)
        assertEquals(1408f, header.getFloat(40), 0f)
        assertEquals(476f, header.getFloat(44), 0f)
        // 48/52 are unused by every reference receiver; a sender must not put
        // panel offsets here.
        assertEquals(0f, header.getFloat(48), 0f)
        assertEquals(0f, header.getFloat(52), 0f)
        assertEquals(1408f, header.getFloat(56), 0f)
        assertEquals(476f, header.getFloat(60), 0f)
    }
    @Test fun videoPacketMarksKeyFramesOnTheWire() {
        val key = ByteArray(32); val payload = byteArrayOf(0,0,0,2,0x65,0x42)
        val idr = VideoPackets.frame(payload, key, 1, 7L, keyFrame = true)
        val p = VideoPackets.frame(payload, key, 2, 8L, keyFrame = false)
        assertEquals(0x10, idr[5].toInt() and 0xff)
        assertEquals(0x00, p[5].toInt() and 0xff)
    }
    @Test fun videoKeepAliveWithoutStatsUsesOpcodeTwoAndNoBody() {
        val packet = VideoPackets.keepAlive(withStats = false)
        assertEquals(128, packet.size)
        val header = ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN)
        assertEquals(0, header.getInt(0))
        assertEquals(2, packet[4].toInt())
    }
    @Test fun videoKeepAliveWithStatsUsesOpcodeFiveAndCarriesBodyLengthInParams14() {
        val packet = VideoPackets.keepAlive(
            withStats = true,
            framesPerSecond = 30,
            sentFrames = 29,
            bytesPerSecond = 1234.5,
        )
        val header = ByteBuffer.wrap(packet, 0, 128).order(ByteOrder.LITTLE_ENDIAN)
        val bodyLength = header.getInt(0)
        assertEquals(5, packet[4].toInt())
        assertTrue(bodyLength > 0)
        assertEquals(128 + bodyLength, packet.size)
        assertEquals(0f, header.getFloat(120), 0f)
        assertEquals(bodyLength.toFloat(), header.getFloat(124), 0f)
        val body = BplistCodec.decode(packet.copyOfRange(128, packet.size)) as Map<*, *>
        assertEquals(30L, (body["encoderCurrentFPS"] as Number).toLong())
        assertEquals(29L, (body["sentFramesAvg"] as Number).toLong())
    }
    @Test fun queueRequiresRecoveryKeyframeAfterOverflow() {
        val queue = VideoQueue(2,20)
        assertNotEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(1),0,false)))
        assertEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(2),1,true)))
        assertEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(3),2,false)))
        assertNotEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(4),3,false)))
        assertEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(5),4,true)))
        assertEquals(4,queue.dropped); assertEquals(4,queue.take()!!.ptsUs)
        queue.close(); assertNull(queue.take()); assertNotEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(1),5,true)))
    }
    @Test fun queueResetDropsOldEpochAndWaitsForNewKeyframe() {
        val queue = VideoQueue(4, 20)
        assertEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(1), 10, true)))
        assertEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(2), 11, false)))

        queue.resetForKeyFrame()

        assertEquals(2, queue.dropped)
        assertNotEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(3), 12, false)))
        assertEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(4), 13, true)))
        assertEquals(13, queue.take()!!.ptsUs)
        queue.close()
    }
    @Test fun queueDoesNotReleaseRecoveryForAFrameThatWouldOverflowIt() {
        val queue = VideoQueue(2, 20)
        assertEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(1), 10, true)))
        assertEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(2), 11, false)))

        queue.resetForKeyFrame()
        assertNotEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(3), 12, false)))
        assertNotEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(4), 13, false)))
        assertEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(5), 14, true)))
        assertEquals(14, queue.take()!!.ptsUs)
        queue.close()
    }
    @Test fun queueRejectsAFrameDequeuedBeforeDecoderEpochReset() {
        val queue = VideoQueue(4, 20)
        assertEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(1), 10, true)))
        val borrowed = checkNotNull(queue.take())

        queue.resetForKeyFrame()

        assertFalse(queue.isCurrent(borrowed))
        assertNotEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(2), 11, false)))
        assertEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(3), 12, true)))
        val fresh = checkNotNull(queue.take())
        assertTrue(queue.isCurrent(fresh))
        assertEquals(12, fresh.ptsUs)
        queue.close()
    }
    @Test fun queueResetWaitsForAnInFlightCurrentWrite() {
        val queue = VideoQueue(4, 20)
        assertEquals(VideoQueue.OfferResult.ACCEPTED,queue.offer(EncodedVideo(byteArrayOf(1), 10, true)))
        val borrowed = checkNotNull(queue.take())
        val entered = CountDownLatch(1)
        val release = CountDownLatch(1)
        val resetFinished = CountDownLatch(1)
        val writer = Thread {
            queue.withCurrent(borrowed) {
                entered.countDown()
                release.await(5, TimeUnit.SECONDS)
            }
        }
        writer.start()
        assertTrue(entered.await(1, TimeUnit.SECONDS))
        val resetter = Thread {
            queue.resetForKeyFrame()
            resetFinished.countDown()
        }
        resetter.start()
        assertFalse(resetFinished.await(100, TimeUnit.MILLISECONDS))
        release.countDown()
        assertTrue(resetFinished.await(1, TimeUnit.SECONDS))
        writer.join(1000)
        resetter.join(1000)
        assertFalse(queue.isCurrent(borrowed))
        queue.close()
    }
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
