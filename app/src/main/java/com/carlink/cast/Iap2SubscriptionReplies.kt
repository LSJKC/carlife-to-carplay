package com.carlink.cast

import com.carplay.iap2.message.Iap2Messages
import com.carplay.iap2.wire.Iap2Frame

/**
 * Idle subscription replies observed in Buick-E5's post-authentication path.
 *
 * These are deliberately kept as exact CSM bodies where the reference project
 * has a byte-level fixture. A typed approximation can change optional fields
 * and make the HU wait or retry the subscription.
 */
internal object Iap2SubscriptionReplies {
    fun forRequest(messageId: Int): Iap2Frame? = when (messageId) {
        0x4157 -> frame(
            0x4158,
            "404000644158000500000100050001060005000200000e0004e697a053494de58da1" +
                "00000500050100050006010005000700000500080000050009000005000a000005" +
                "000b000005000c010005000d000005000e000005000f0000050010000005001100",
        )
        0x4154 -> Iap2Messages.buildRaw(0x4155) {
            u8(2, 0)
        }
        0x4170 -> Iap2Messages.buildRaw(0x4171) {
            u8(0, 0)
            u16(2, 0)
            u8(5, 0)
            u16(7, 0)
        }
        else -> null
    }

    private fun frame(messageId: Int, fullFrameHex: String): Iap2Frame {
        val bytes = fullFrameHex.hexBytes()
        require(bytes.size >= 6) { "CSM frame too short" }
        val declared = ((bytes[2].toInt() and 0xff) shl 8) or (bytes[3].toInt() and 0xff)
        require(declared == bytes.size) { "CSM frame length mismatch" }
        return Iap2Frame(messageId, bytes.copyOfRange(6, bytes.size))
    }

    private fun String.hexBytes(): ByteArray {
        require(length % 2 == 0) { "hex must contain whole bytes" }
        return ByteArray(length / 2) { index ->
            substring(index * 2, index * 2 + 2).toInt(16).toByte()
        }
    }
}
