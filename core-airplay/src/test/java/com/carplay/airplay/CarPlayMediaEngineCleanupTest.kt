package com.carplay.airplay

import java.net.Socket
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CarPlayMediaEngineCleanupTest {
    @Test
    fun audioKeysForSessionKeepsOtherSessionKeys() {
        val listener = object : AirPlaySessionListener {}
        val media = object : AirPlayMediaHandler {}
        val config = AirPlayConfig(
            deviceName = "test",
            deviceId = "AA:BB:CC:DD:EE:FF",
            btMac = "AA:BB:CC:DD:EE:FF",
            sourceVersion = "test",
            main = AirPlayDisplayConfig(widthPixels = 1, heightPixels = 1),
        )
        val identity = AirPlayIdentity.generate()
        val first = AirPlaySession(
            Socket(), config, identity, PairingStore(), null, listener, media,
        )
        val second = AirPlaySession(
            Socket(), config, identity, PairingStore(), null, listener, media,
        )
        val firstKey = CarPlayMediaEngine.StreamKey(first, 100, "media")
        val secondKey = CarPlayMediaEngine.StreamKey(second, 100, "media")

        val selected = audioKeysForSession(
            first,
            listOf(firstKey, secondKey),
            listOf(secondKey),
            emptyList(),
        )

        assertEquals(setOf(firstKey), selected)
        assertTrue(secondKey !in selected)
    }
}
