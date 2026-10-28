package com.carplay.sender

import com.carplay.airplay.AudioCodecKind
import java.net.ProtocolException

enum class SenderControlKind { WIFI_DATASTREAM, WIFI_COMMAND, AUTHENTICATED_BLUETOOTH }
data class SenderControlTransport(val kind: SenderControlKind, val port: Int? = null)
data class SenderAudioFormat(
    val streamType: Int,
    val sampleRate: Int,
    val channels: Int,
    val formatBits: Long,
    val codec: AudioCodecKind = AudioCodecKind.AAC_LC,
    /** Samples carried by one RTP packet. LPCM uses the E5-tested 352-sample shape. */
    val samplesPerPacket: Int = 1024,
) {
    val isPcm: Boolean get() = codec == AudioCodecKind.LPCM
    /** Stable key used by the receiver-specific profile memory. */
    val key: String get() = "$streamType|${if (isPcm) "PCM" else "AAC"}|$sampleRate|$formatBits|$channels|$samplesPerPacket"
    /** Bytes of one LPCM packet, before the RTP header. */
    val pcmPacketBytes: Int get() = samplesPerPacket * channels * 2

    fun rtpTimestamp(ptsUs: Long): Int {
        require(ptsUs >= 0)
        val seconds = (ptsUs / 1_000_000) % 0x1_0000_0000L
        val fractional = ((ptsUs % 1_000_000) * sampleRate + 500_000) / 1_000_000
        return (seconds * sampleRate + fractional).toInt()
    }
}

/**
 * Keeps an AAC RTP clock on packet sample count after the first PTS anchor.
 *
 * CarLife can rebuild its AAC encoder and restart MediaCodec PTS while the
 * AirPlay stream remains alive. The Buick-E5 sender advances by SPF instead
 * of trusting those later PTS values.
 */
internal class MonotonicAudioTimestamp(
    private val format: SenderAudioFormat,
) {
    private var nextTimestamp: Long? = null

    fun next(ptsUs: Long): Int {
        val current = nextTimestamp ?: (format.rtpTimestamp(ptsUs).toLong() and RTP_MASK)
        nextTimestamp = (current + format.samplesPerPacket) and RTP_MASK
        return current.toInt()
    }

    fun reset() {
        nextTimestamp = null
    }

    private companion object {
        const val RTP_MASK = 0xffff_ffffL
    }
}

/** Select only what the peer actually accepted; Bluetooth availability is checked by the owner. */
object SenderNegotiation {
    fun control(reply: Map<*, *>): SenderControlTransport {
        val raw = reply["streams"] ?: return SenderControlTransport(SenderControlKind.AUTHENTICATED_BLUETOOTH)
        val streams = raw as? List<*> ?: throw ProtocolException("type 130 回复的 streams 类型无效")
        val accepted = streams.map { it as? Map<*, *> ?: throw ProtocolException("type 130 回复的流结构无效") }
            .filter { (it["type"] as? Number)?.toLong() == 130L }
        if (accepted.isEmpty()) return SenderControlTransport(SenderControlKind.AUTHENTICATED_BLUETOOTH)
        if (accepted.size != 1) throw ProtocolException("车机返回多个 iAP 控制流")
        val stream = accepted.single()
        val rawPort = stream["dataPort"] ?: return SenderControlTransport(SenderControlKind.WIFI_COMMAND)
        val port = rawPort as? Number ?: throw ProtocolException("iAP dataPort 类型无效")
        val value = port.toLong()
        if (value == 0L) return SenderControlTransport(SenderControlKind.WIFI_COMMAND)
        if (value !in 1..65535 || port.toDouble() != value.toDouble()) throw ProtocolException("iAP dataPort 超出范围")
        return SenderControlTransport(SenderControlKind.WIFI_DATASTREAM,value.toInt())
    }

    /**
     * Picks the media stream from the formats the head unit actually advertises.
     *
     * Buick E2LB advertises both `type=100/media` PCM and
     * `type=102/media` AAC-LC. The 对照实现 sender — the one known to hold a
     * session open on this car class — drives **PCM** as its main path
     * (`C0142f1.E()` selects `type == 100` and falls back to a 48 kHz baseline
     * when `/info` declares nothing usable), and its own diagnostic strings say
     * "✓ 音频流协商成功（type=100". This sender therefore prefers PCM by
     * default and keeps AAC as the fallback, which is the reverse of the
     * previous order.
     *
     * `preferAacMedia` still forces AAC first, for head units that reject PCM.
     */
    fun audio(info: Map<*, *>, preferAacMedia: Boolean = false): SenderAudioFormat {
        return audioCandidates(info, preferAacMedia).firstOrNull()
            ?: throw ProtocolException("车机未声明可发送的媒体格式（type 100/102、44.1/48 kHz）；请关闭音频重试")
    }

    /**
     * Returns every media profile the receiver advertises, in the same order
     * used by [audio].  Keeping the candidates visible lets the app remember
     * which profile a particular head unit accepted instead of retrying a
     * known-bad profile on every connection.
     */
    fun audioCandidates(info: Map<*, *>, preferAacMedia: Boolean = false): List<SenderAudioFormat> {
        val entries = (info["audioFormats"] as? List<*>)?.mapNotNull { it as? Map<*, *> }.orEmpty()
        val media = entries.filter {
            (it["type"] as? Number)?.toInt() in listOf(100, 101, 102) &&
                (it["audioType"] == null || it["audioType"] in listOf("media", "default"))
        }
        fun bits(entry: Map<*, *>): Long = when (val raw = entry["audioOutputFormats"]) {
            is Number -> raw.toLong()
            is String -> raw.toLongOrNull(16) ?: 0L
            else -> 0L
        }

        fun aacCandidates(): List<SenderAudioFormat> = media.flatMap { entry ->
            val type = (entry["type"] as? Number)?.toInt() ?: return@flatMap emptyList()
            listOf(48_000 to 0x800000L, 44_100 to 0x400000L).mapNotNull { (rate, flag) ->
                if (bits(entry) and flag != 0L) SenderAudioFormat(type, rate, 2, flag) else null
            }
        }.sortedWith(compareByDescending<SenderAudioFormat> { it.sampleRate }.thenBy { it.streamType })

        if (preferAacMedia) {
            val aac = aacCandidates()
            if (aac.isNotEmpty()) return aac
        }

        // PCM first, matching the 对照实现 sender's main path. Some HUs
        // declare type 100 with a zero output mask; those entries are skipped
        // rather than trusted, because an input-only description produces an
        // accepted SETUP with no audible output.
        val pcm = media.filter { (it["type"] as? Number)?.toInt() == 100 }.flatMap { entry ->
            // Prefer 48 kHz; fallback to 44.1 kHz if 48 fails
            listOf(48_000 to 0x8000L, 44_100 to 0x800L).mapNotNull { (rate, flag) ->
                // A zero output mask means this entry cannot carry media. Do not
                // authorize a PCM path from an input-only/microphone description;
                // that produced accepted SETUPs with no audible output.
                val bits_ = bits(entry)
                if (bits_ and flag == 0L) return@mapNotNull null
                SenderAudioFormat(
                    100, rate, 2, flag, AudioCodecKind.LPCM, PCM_SAMPLES_PER_PACKET,
                )
            }
        }
        val orderedPcm = pcm.sortedByDescending { it.sampleRate }
        val aac = aacCandidates()
        // PCM is preferred; AAC remains the fallback for receivers that refuse it.
        return if (orderedPcm.isNotEmpty()) orderedPcm + aac else aac
    }

    /**
     * Samples per LPCM RTP packet.
     *
     * 352 = 7.33 ms at 48 kHz. This is the value the 对照实现 sender
     * negotiates on this car class and the one its audio pipeline is built
     * around (`D3.java`: "，每包 352 帧"). An earlier build used 240 (5 ms)
     * from a different reference sender; both are accepted by the receiver,
     * but 352 is what a sender known to stay connected uses, so it is the
     * default here.
     */
    const val PCM_SAMPLES_PER_PACKET = 352
}

/** Binary data from a verified iAP command or that command's response; never infer proof from ACKs. */
object IapCommandData {
    fun payload(value: Any?, response: Boolean = false): ByteArray? {
        val raw = when (value) {
            is ByteArray -> if (response) value else null
            is Map<*, *> -> {
                if (value["type"] != null && value["type"] != "iAPSendMessage") return null
                val params = value["params"]
                if (params != null && params !is Map<*, *>) throw ProtocolException("iAP params 类型无效")
                (params as? Map<*, *>)?.get("data") ?: if (response) value["data"] else null
            }
            else -> null
        } ?: return null
        val data = raw as? ByteArray ?: throw ProtocolException("iAP 控制数据不是二进制字段")
        if (data.isEmpty()) return null
        if (data.size > 1_048_576) throw ProtocolException("iAP 控制数据超出接收上限")
        return data
    }
}
