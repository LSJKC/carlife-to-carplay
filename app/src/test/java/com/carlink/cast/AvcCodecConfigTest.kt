package com.carlink.cast

import com.carplay.sender.VideoPackets
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class AvcCodecConfigTest {
    private val sps = byteArrayOf(0x67, 0x42, 0x00, 0x1e, 0x12)
    private val pps = byteArrayOf(0x68, 0x56)
    private val startCode = byteArrayOf(0, 0, 0, 1)

    @Test
    fun `parses raw codec specific parameter sets`() {
        val config = AvcCodecConfig.fromParameterSets(sps, pps)

        assertNotNull(config)
        assertArrayEquals(expectedConfig(), config)
    }

    @Test
    fun `parses start code prefixed parameter sets`() {
        val config = AvcCodecConfig.fromParameterSets(startCode + sps, startCode + pps)

        assertNotNull(config)
        assertArrayEquals(expectedConfig(), config)
    }

    @Test
    fun `parses parameter sets together in one buffer`() {
        val config = AvcCodecConfig.fromParameterSets(startCode + sps + startCode + pps)

        assertNotNull(config)
        assertArrayEquals(expectedConfig(), config)
    }

    private fun expectedConfig(): ByteArray =
        VideoPackets.avcC(startCode + sps, startCode + pps)
}
