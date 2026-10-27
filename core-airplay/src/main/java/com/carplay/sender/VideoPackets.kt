package com.carplay.sender

import com.carplay.airplay.AirPlayCrypto
import com.carplay.airplay.BplistCodec
import java.nio.ByteBuffer
import java.nio.ByteOrder

object VideoPackets {

    /**
     * `header[5]` value that marks an encrypted packet as carrying an IDR.
     *
     * UxPlay's `raop_rtp_mirror.c` lists the two observed pairs: `0x00 0x00`
     * for a non-IDR type-1 VCL NAL and `0x00 0x10` for an IDR type-5 VCL NAL.
     */
    const val KEY_FRAME_FLAG: Byte = 0x10

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
     * Screen CONFIG: the unencrypted SPS/PPS packet.
     *
     * Byte layout taken from UxPlay's `raop_rtp_mirror.c`, which is the
     * reference every other open sender was written against:
     *
     *     0x01 0x00  unencrypted packet carrying a type-7 SPS and a type-8 PPS
     *     0x16 0x01  the "option" pair real senders put on that packet
     *
     * Three things this used to get wrong, all verifiable against UxPlay:
     *
     *  - **bytes 8-15 were left at zero.** A receiver ties the codec config to
     *    the IDR that follows it by comparing that timestamp; UxPlay discards
     *    the SPS/PPS outright when the two differ ("prepended sps_pps timestamp
     *    does not match timestamp of video payload ... discarding"). Passing
     *    the IDR's timestamp in is what makes the pair match.
     *  - **bytes 6-7 were `0x04 0x00`**, a value that appears in none of the
     *    option pairs UxPlay lists; `0x16 0x01` is what a real h264 sender
     *    writes.
     *  - **bytes 48-51 carried the panel's x/y origin.** UxPlay reads those as
     *    an unknown pair of floats it only logs, and doubletake does not write
     *    them at all; putting (278, 44) there sent a malformed config.
     *
     * Bytes 16/20 and 40/44 are the same source size twice — UxPlay calls the
     * second pair "duplication of width_0" — and 56/60 repeat it as well.
     */
    fun config(
        body: ByteArray,
        width: Int,
        height: Int,
        ntp: Long,
        displayWidth: Int = width,
        displayHeight: Int = height,
    ): ByteArray {
        require(width > 0 && height > 0 && displayWidth > 0 && displayHeight > 0)
        require(body.size <= 65535)
        val header = ByteBuffer.allocate(128).order(ByteOrder.LITTLE_ENDIAN)
        header.putInt(0, body.size)
        header.put(4, 1); header.put(6, 0x16.toByte()); header.put(7, 0x01.toByte())
        header.putLong(8, ntp)
        header.putFloat(16, width.toFloat()); header.putFloat(20, height.toFloat())
        header.putFloat(40, width.toFloat()); header.putFloat(44, height.toFloat())
        header.putFloat(56, displayWidth.toFloat()); header.putFloat(60, displayHeight.toFloat())
        return header.array() + body
    }

    /**
     * One encrypted video frame.
     *
     * `header[5]` is the key-frame flag. UxPlay lists the pairs it observes:
     *
     *     0x00 0x00  encrypted packet containing a non-IDR type-1 VCL NAL
     *     0x00 0x10  encrypted packet containing an IDR type-5 VCL NAL
     *
     * This sender used to leave it at zero for every frame, so an IDR looked
     * exactly like a P frame on the wire.
     */
    fun frame(body: ByteArray, key: ByteArray, counter: Long, ntp: Long, keyFrame: Boolean = false): ByteArray {
        require(body.size in 1..4_194_304 && key.size == 32 && counter >= 0)
        val header = ByteBuffer.allocate(128).order(ByteOrder.LITTLE_ENDIAN).apply {
            putInt(0, body.size + 16)
            put(5, if (keyFrame) KEY_FRAME_FLAG else 0)
            putLong(8, ntp)
        }.array()
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
