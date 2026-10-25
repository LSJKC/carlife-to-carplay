package com.carlink.cast

import com.carplay.sender.VideoPackets

internal object AvcCodecConfig {
    private val startCode = byteArrayOf(0, 0, 0, 1)

    fun fromParameterSets(vararg buffers: ByteArray?): ByteArray? {
        val nals = buffers.filterNotNull().flatMap(::readNals)
        val sps = nals.firstOrNull { nalType(it) == 7 } ?: return null
        val pps = nals.firstOrNull { nalType(it) == 8 } ?: return null
        return runCatching {
            VideoPackets.avcC(startCode + sps, startCode + pps)
        }.getOrNull()
    }

    private fun readNals(bytes: ByteArray): List<ByteArray> {
        if (bytes.isEmpty()) return emptyList()
        val framed = runCatching { VideoPackets.nals(bytes) }.getOrNull()
        if (framed != null) return framed
        return if (nalType(bytes) in 1..23) listOf(bytes) else emptyList()
    }

    private fun nalType(nal: ByteArray): Int =
        nal.firstOrNull()?.toInt()?.and(0x1f) ?: 0
}
