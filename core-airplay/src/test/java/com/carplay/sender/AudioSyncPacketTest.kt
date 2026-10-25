package com.carplay.sender

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AudioSyncPacketTest {
    @Test
    fun buildsTheTwentyByteRtpToNtpAnchor() {
        val ntp = byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        val packet = AudioSyncPacket.build(
            sequence = 0x1234,
            currentTimestamp = 0x10203040,
            nextTimestamp = 0x50607080,
            ntp = ntp,
            first = true,
        )
        assertEquals(AudioSyncPacket.SIZE, packet.size)
        assertEquals(0x90.toByte(), packet[0])
        assertEquals(0xd4.toByte(), packet[1])
        assertArrayEquals(byteArrayOf(0x12, 0x34), packet.copyOfRange(2, 4))
        assertArrayEquals(byteArrayOf(0x10, 0x20, 0x30, 0x40), packet.copyOfRange(4, 8))
        assertArrayEquals(ntp, packet.copyOfRange(8, 16))
        assertArrayEquals(byteArrayOf(0x50, 0x60, 0x70, 0x80.toByte()), packet.copyOfRange(16, 20))
        assertTrue(packet[0].toInt() and 0x10 != 0)
    }

    @Test
    fun subsequentAnchorClearsTheFirstPacketFlag() {
        val packet = AudioSyncPacket.build(
            sequence = 7,
            currentTimestamp = 1000,
            nextTimestamp = 2024,
            ntp = ByteArray(8),
            first = false,
        )
        assertEquals(0x80.toByte(), packet[0])
        assertEquals(7, packet[3].toInt())
    }
}
