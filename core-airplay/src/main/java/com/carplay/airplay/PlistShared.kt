package com.carplay.airplay

import java.math.BigInteger

/**
 * Small shared types that survived the 1.3.45 cleanup of the receiver stack.
 *
 * Both used to live inside files that belonged to the AirPlay *receiver*
 * (`AudioStream.kt` declared the codec enum, `CarPlayMediaEngine.kt` declared
 * the unsigned-plist helpers). This application is a *sender* and contains no
 * receiver, so those files were removed — but the sender's own negotiation code
 * (`SenderNegotiation`, `AirPlaySender`) had grown to depend on these two
 * declarations, and a plist integer is genuinely unsigned on the wire.
 *
 * Keeping them here, apart from anything that parses a stream, is what makes
 * the rest of the receiver stack deletable without touching the sender.
 */

/** Media codec a negotiated audio stream carries. */
enum class AudioCodecKind { AAC_LC, OPUS, LPCM }

/** A plist integer as unsigned decimal text; null when it cannot be one. */
internal fun unsignedPlistDecimal(value: Any?): String? = when (value) {
    is Long -> java.lang.Long.toUnsignedString(value)
    is Int -> Integer.toUnsignedString(value)
    is Short -> (value.toInt() and 0xffff).toString()
    is Byte -> (value.toInt() and 0xff).toString()
    is BigInteger -> if (value.signum() >= 0) value.toString() else null
    else -> (value as? Number)?.toLong()?.let(java.lang.Long::toUnsignedString)
}

/** A plist integer widened so a negative value keeps its unsigned meaning. */
internal fun unsignedPlistInteger(value: Any?): Any = when (value) {
    is Long -> if (value < 0) BigInteger(java.lang.Long.toUnsignedString(value)) else value
    is Int -> if (value < 0) BigInteger(Integer.toUnsignedString(value)) else value
    else -> value ?: 0L
}
