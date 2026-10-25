package com.carplay.adapter.carlife

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CarLifeVideoAssemblerTest {
    private fun hex(value: String): ByteArray =
        value.chunked(2).map { it.toInt(16).toByte() }.toByteArray()

    @Test
    fun `holds IDR until separate SPS and PPS make config available`() {
        val assembler = CarLifeVideoAssembler()
        val gate = KeyFrameRebuildGate()
        val sps = hex("000000016742801fda01")
        val pps = hex("0000000168ce06f2")
        val idr = hex("0000000165b8427f")

        assertTrue(gate.request())
        assertNull(assembler.offer(idr, timestampSeconds = 10, keyFrame = true))
        assertTrue(gate.isInFlight())
        assertNull(assembler.offer(sps, timestampSeconds = 10, keyFrame = false))

        val batch = checkNotNull(assembler.offer(pps, timestampSeconds = 10, keyFrame = false))
        assertArrayEquals(CarLifeProto.avcConfig(sps, pps), batch.avcC)
        assertEquals(1, batch.frames.size)
        assertArrayEquals(idr, batch.frames[0].data)
        assertEquals(10L, batch.frames[0].timestampSeconds)
        assertTrue(batch.frames[0].keyFrame)

        batch.frames.forEach { gate.observeFrame(it.keyFrame) }
        assertFalse(gate.isInFlight())
        assertTrue(gate.request())
    }

    @Test
    fun `keeps dependent access units between IDR and delayed parameter sets`() {
        val assembler = CarLifeVideoAssembler()
        val sps = hex("000000016742801fda01")
        val pps = hex("0000000168ce06f2")
        val idr = hex("0000000165b8427f")
        val predicted = hex("0000000141aabbcc")

        assertNull(assembler.offer(idr, timestampSeconds = 20, keyFrame = true))
        assertNull(assembler.offer(predicted, timestampSeconds = 20, keyFrame = false))
        assertNull(assembler.offer(sps, timestampSeconds = 20, keyFrame = false))

        val batch = checkNotNull(assembler.offer(pps, timestampSeconds = 20, keyFrame = false))
        assertArrayEquals(CarLifeProto.avcConfig(sps, pps), batch.avcC)
        assertEquals(listOf(idr, predicted).map { it.toList() },
            batch.frames.map { it.data.toList() })
    }

    @Test
    fun `drops an overlong incomplete GOP and waits for a fresh IDR`() {
        val assembler = CarLifeVideoAssembler()
        val sps = hex("000000016742801fda01")
        val pps = hex("0000000168ce06f2")
        val idr = hex("0000000165b8427f")
        val predicted = hex("0000000141aabbcc")

        assertNull(assembler.offer(idr, timestampSeconds = 30, keyFrame = true))
        repeat(31) {
            assertNull(assembler.offer(predicted, timestampSeconds = 30, keyFrame = false))
        }
        assertNull(assembler.offer(predicted, timestampSeconds = 30, keyFrame = false))
        assertNull(assembler.offer(sps, timestampSeconds = 30, keyFrame = false))

        val config = checkNotNull(assembler.offer(pps, timestampSeconds = 30, keyFrame = false))
        assertNotNull(config.avcC)
        assertTrue(config.frames.isEmpty())
        assertNull(assembler.offer(predicted, timestampSeconds = 31, keyFrame = false))

        val recovery = checkNotNull(assembler.offer(idr, timestampSeconds = 31, keyFrame = true))
        assertEquals(listOf(idr.toList()), recovery.frames.map { it.data.toList() })
    }

    @Test
    fun `reset discards pending IDR and requires a new key frame`() {
        val assembler = CarLifeVideoAssembler()
        val idr = hex("0000000165b8427f")
        val sps = hex("000000016742801fda01")
        val pps = hex("0000000168ce06f2")
        val nextFrame = hex("0000000141b8427f")

        assertNull(assembler.offer(idr, timestampSeconds = 10, keyFrame = true))
        assembler.reset()
        val batch = checkNotNull(assembler.offer(sps + pps, timestampSeconds = 11, keyFrame = false))
        assertNotNull(batch.avcC)
        assertTrue(batch.frames.isEmpty())
        assertNull(assembler.offer(nextFrame, timestampSeconds = 11, keyFrame = false))

        val recovery = checkNotNull(assembler.offer(idr, timestampSeconds = 11, keyFrame = true))
        assertEquals(1, recovery.frames.size)
        assertArrayEquals(idr, recovery.frames.single().data)
        assertTrue(recovery.frames.single().keyFrame)
    }

    @Test
    fun `reset re-emits previous config when rebuilt encoder only sends IDR`() {
        val assembler = CarLifeVideoAssembler()
        val sps = hex("000000016742801fda01")
        val pps = hex("0000000168ce06f2")
        val idr = hex("0000000165b8427f")

        val initial = checkNotNull(assembler.offer(sps + pps + idr, 10, keyFrame = true))
        val config = checkNotNull(initial.avcC)
        assembler.reset(preserveConfig = true)

        val recovery = checkNotNull(assembler.offer(idr, 11, keyFrame = true))
        assertArrayEquals(config, recovery.avcC)
        assertTrue(recovery.reusedAvcC)
        assertEquals(1, recovery.frames.size)
        assertArrayEquals(idr, recovery.frames.single().data)
    }

    @Test
    fun `parameter sets are never emitted as video frames after config`() {
        val assembler = CarLifeVideoAssembler()
        val sps = hex("000000016742801fda01")
        val pps = hex("0000000168ce06f2")
        val idr = hex("0000000165b8427f")

        val first = checkNotNull(assembler.offer(sps + pps + idr, 10, keyFrame = true))
        assertEquals(listOf(idr.toList()), first.frames.map { it.data.toList() })
        assertNull(assembler.offer(sps, 11, keyFrame = false))
        assertNull(assembler.offer(pps, 11, keyFrame = false))
    }
}
