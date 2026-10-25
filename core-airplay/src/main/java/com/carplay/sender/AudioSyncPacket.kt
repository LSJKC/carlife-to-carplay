package com.carplay.sender

/**
 * AirPlay private RTCP packet that anchors audio RTP timestamps to NTP time.
 */
internal object AudioSyncPacket {
    const val SIZE = 20
    private const val PAYLOAD_TYPE = 0xD4
    private const val FIRST_FLAG = 0x10

    fun build(
        sequence: Int,
        currentTimestamp: Long,
        nextTimestamp: Long,
        ntp: ByteArray,
        first: Boolean,
    ): ByteArray {
        require(ntp.size == 8) { "NTP timestamp must be 8 bytes" }
        val packet = ByteArray(SIZE)
        packet[0] = (0x80 or if (first) FIRST_FLAG else 0).toByte()
        packet[1] = PAYLOAD_TYPE.toByte()
        packet[2] = (sequence ushr 8).toByte()
        packet[3] = sequence.toByte()
        writeU32(packet, 4, currentTimestamp)
        ntp.copyInto(packet, 8)
        writeU32(packet, 16, nextTimestamp)
        return packet
    }

    private fun writeU32(target: ByteArray, offset: Int, value: Long) {
        val v = value.toInt()
        target[offset] = (v ushr 24).toByte()
        target[offset + 1] = (v ushr 16).toByte()
        target[offset + 2] = (v ushr 8).toByte()
        target[offset + 3] = v.toByte()
    }
}
