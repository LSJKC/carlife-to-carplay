package com.carplay.sender

import com.carplay.airplay.AirPlayCrypto
import com.carplay.airplay.BplistCodec
import java.nio.ByteBuffer
import java.nio.ByteOrder

object VideoPackets {
    fun nals(bytes: ByteArray): List<ByteArray> {
        require(bytes.size in 1..4_194_304)
        fun start(i: Int): Int = when {
            i + 4 <= bytes.size && bytes[i] == 0.toByte() && bytes[i+1] == 0.toByte() && bytes[i+2] == 0.toByte() && bytes[i+3] == 1.toByte() -> 4
            i + 3 <= bytes.size && bytes[i] == 0.toByte() && bytes[i+1] == 0.toByte() && bytes[i+2] == 1.toByte() -> 3
            else -> 0
        }
        val result = mutableListOf<ByteArray>()
        if (start(0) == 0) {
            var offset = 0
            while (offset < bytes.size) {
                require(bytes.size - offset >= 4)
                val size = ByteBuffer.wrap(bytes, offset, 4).int; offset += 4
                require(size in 1..bytes.size - offset)
                result += bytes.copyOfRange(offset, offset + size); offset += size
            }
        } else {
            var begin = start(0); var cursor = begin
            while (cursor < bytes.size) {
                val prefix = start(cursor)
                if (prefix > 0) { if (cursor > begin) result += bytes.copyOfRange(begin, cursor); begin = cursor + prefix; cursor = begin }
                else cursor++
            }
            if (begin < bytes.size) result += bytes.copyOfRange(begin, bytes.size)
        }
        require(result.isNotEmpty())
        return result
    }
    fun lengthPrefixed(bytes: ByteArray): ByteArray {
        val nals = nals(bytes)
        return ByteBuffer.allocate(nals.sumOf { it.size + 4 }).apply { nals.forEach { putInt(it.size); put(it) } }.array()
    }
    fun avcC(spsBytes: ByteArray, ppsBytes: ByteArray): ByteArray {
        val sps = nals(spsBytes).first { it[0].toInt() and 31 == 7 }
        val pps = nals(ppsBytes).first { it[0].toInt() and 31 == 8 }
        require(sps.size in 4..65535 && pps.size in 1..65535)
        return ByteBuffer.allocate(11 + sps.size + pps.size).apply {
            put(1); put(sps[1]); put(sps[2]); put(sps[3]); put(0xff.toByte()); put(0xe1.toByte())
            putShort(sps.size.toShort()); put(sps); put(1); putShort(pps.size.toShort()); put(pps)
        }.array()
    }
    /**
     * Screen CONFIG with explicit content and display rectangles.
     *
     * AADisplay's working sender fills both rectangles. Leaving the display
     * rectangle at zero lets a receiver decide its own placement, which is not
     * a stable way to centre an irregular panel.
     */
    fun config(
        body: ByteArray,
        width: Int,
        height: Int,
        displayX: Float = 0f,
        displayY: Float = 0f,
        displayWidth: Int = width,
        displayHeight: Int = height,
    ): ByteArray {
        require(width > 0 && height > 0 && displayWidth > 0 && displayHeight > 0)
        require(displayX >= 0f && displayY >= 0f)
        require(body.size <= 65535)
        val header = ByteBuffer.allocate(128).order(ByteOrder.LITTLE_ENDIAN)
        header.putInt(0, body.size); header.put(4, 1); header.put(6, 4)
        header.putFloat(16, width.toFloat()); header.putFloat(20, height.toFloat())
        header.putFloat(32, 0f); header.putFloat(36, 0f)
        header.putFloat(40, width.toFloat()); header.putFloat(44, height.toFloat())
        header.putFloat(48, displayX); header.putFloat(52, displayY)
        header.putFloat(56, displayWidth.toFloat()); header.putFloat(60, displayHeight.toFloat())
        return header.array() + body
    }
    fun frame(body: ByteArray, key: ByteArray, counter: Long, ntp: Long): ByteArray {
        require(body.size in 1..4_194_304 && key.size == 32 && counter >= 0)
        val header = ByteBuffer.allocate(128).order(ByteOrder.LITTLE_ENDIAN).apply { putInt(0, body.size + 16); putLong(8, ntp) }.array()
        return header + AirPlayCrypto.chachaSeal(key, AirPlayCrypto.nonce64(counter), body, header)
    }

    /**
     * Screen-stream keep-alive. It is deliberately plaintext and does not
     * advance the encrypted video nonce counter. Some receivers require the
     * statistics body (opcode 5), while others use the empty opcode 2 form.
     */
    fun keepAlive(
        withStats: Boolean,
        framesPerSecond: Int = 0,
        sentFrames: Int = 0,
        bytesPerSecond: Double = 0.0,
    ): ByteArray {
        val body = if (withStats) {
            BplistCodec.encode(
                linkedMapOf(
                    "txUsageAvg" to bytesPerSecond,
                    "encoderCurrentFPS" to framesPerSecond.toLong(),
                    "sentFramesAvg" to sentFrames.toLong(),
                    "queuedFramesAvg" to 0L,
                    "lossAvg" to 0.0,
                ),
            )
        } else {
            ByteArray(0)
        }
        val header = ByteArray(128).also {
            val little = ByteBuffer.wrap(it).order(ByteOrder.LITTLE_ENDIAN)
            little.putInt(0, body.size)
            it[4] = (if (body.isEmpty()) 2 else 5).toByte()
            if (body.isNotEmpty()) {
                little.putFloat(120, 0f)
                little.putFloat(124, body.size.toFloat())
            }
        }
        return header + body
    }
}
