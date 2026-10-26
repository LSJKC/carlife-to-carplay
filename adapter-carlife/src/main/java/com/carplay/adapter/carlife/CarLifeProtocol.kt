package com.carplay.adapter.carlife

import com.carplay.sender.VideoPackets
import java.io.ByteArrayOutputStream

/**
 * Wire constants for the CarLife phone-side listener, as observed on the Honor build.
 *
 * The phone listens on all seven ports and counts six of them (everything but
 * Data) toward the threshold at which it declares the session connected. The
 * head unit connects to all of them.
 */
internal object CarLifeProtocol {
    const val PORT_CMD = 7240
    const val PORT_VIDEO = 8240
    const val PORT_MEDIA = 9240
    const val PORT_TTS = 9241
    const val PORT_VR = 9242
    const val PORT_TOUCH = 9340
    const val PORT_DATA = 9440

    val AUX_PORTS = intArrayOf(PORT_VIDEO, PORT_MEDIA, PORT_TTS, PORT_VR, PORT_TOUCH, PORT_DATA)
    /**
     * Honor CarLife counts every socket except Data toward its connected
     * threshold. Data is an optional side channel; the other five auxiliary
     * ports are required even when this adapter does not consume their payload.
     */
    val REQUIRED_AUX_PORTS = AUX_PORTS.filter { it != PORT_DATA }.toSet()

    /** Channel ids carried in the frame header. */
    const val CHANNEL_CMD = 1
    const val CHANNEL_VIDEO = 2
    const val CHANNEL_MEDIA = 3
    const val CHANNEL_TTS = 4
    const val CHANNEL_VR = 5
    const val CHANNEL_TOUCH = 6

    /** Cmd and Touch use an 8-byte header; every other channel uses 12. */
    const val HEADER_CMD = 8
    const val HEADER_STREAM = 12

    const val MAX_PAYLOAD = 64 * 1024 * 1024

    // Head-unit -> phone message ids (the phone answers on the same id with 0x8000 set).
    const val HU_PROTOCOL_VERSION = 0x00018001
    const val HU_INFO = 0x00018003
    const val HU_AUTHEN_REQUEST = 0x00018048
    const val HU_AUTHEN_RESULT = 0x0001804A
    const val HU_FEATURE_CONFIG_RESPONSE = 0x00018052
    const val VIDEO_ENCODER_INIT = 0x00018007
    const val VIDEO_ENCODER_START = 0x00018009
    const val VIDEO_ENCODER_PAUSE = 0x0001800A
    const val VIDEO_ENCODER_RESET = 0x0001800B
    const val VIDEO_ENCODER_FRAME_RATE_CHANGE = 0x0001800C

    /**
     * The vehicle channel report. CarLife starts a 120-second authorization
     * timer once the protocol matches; without this frame the timer expires
     * and it shows "车机版本未经CarLife授权，即将断开连接".
     */
    const val HU_STATISTIC_INFO = 0x00018027

    /**
     * Mobile data subscription: the head unit tells CarLife which of its own
     * data it wants pushed.
     *
     * This is the switch behind the cluster/centre-console media display.
     * CarLife registers all three ids unconditionally
     * (`CarlifeConnectHsgHandler.addMsg`) and waits for the car to ask:
     *
     *     0x18043 CARLIFE_DATA_SUBSCRIBE        -> answered with 0x10044 DONE
     *     0x18045 CARLIFE_DATA_SUBSCRIBE_START  -> moduleID 2 = media info ON
     *     0x18046 CARLIFE_DATA_SUBSCRIBE_STOP   -> moduleID 2 = media info OFF
     *
     * `CarlifeDataSubscribe.setMobileCarLifeState` sets
     * `MusicInfoSender.setSubscibe(z)` from moduleID 2, and only then does the
     * phone push `MSG_CMD_MEDIA_INFO` (0x10035) and
     * `MSG_CMD_MEDIA_PROGRESS_BAR` (0x10036). Without this handshake the car
     * shows "正在加载媒体…" forever — which is exactly what this project saw.
     */
    const val HU_DATA_SUBSCRIBE = 0x00018043
    const val HU_DATA_SUBSCRIBE_START = 0x00018045
    const val HU_DATA_SUBSCRIBE_STOP = 0x00018046

    // Phone -> head unit.
    const val PROTOCOL_VERSION_MATCH_STATUS = 0x00010002
    const val MD_INFO = 0x00010004
    const val VIDEO_ENCODER_INIT_DONE = 0x00010008

    /** Answer to HU_DATA_SUBSCRIBE; carries the same module list back. */
    const val DATA_SUBSCRIBE_DONE = 0x00010044

    /** Media metadata and playback position pushed by the phone. */
    const val MEDIA_INFO = 0x00010035
    const val MEDIA_PROGRESS_BAR = 0x00010036

    /** Reply to VIDEO_ENCODER_FRAME_RATE_CHANGE; the phone confirms the new rate. */
    const val VIDEO_ENCODER_FRAME_RATE_CHANGE_DONE = 0x0001000D
    const val MD_AUTHEN_RESPONSE = 0x00010049
    const val MD_AUTHEN_RESULT = 0x0001004B
    const val MD_FEATURE_CONFIG_REQUEST = 0x00010051
    const val MD_EXIT = 0x00010059

    // Touch channel ids, sent as the service type on the touch socket.
    const val MSG_TOUCH_ACTION = 425985
    const val MSG_TOUCH_ACTION_DOWN = 425986
    const val MSG_TOUCH_ACTION_UP = 425987
    const val MSG_TOUCH_ACTION_MOVE = 425988
    const val MSG_TOUCH_CAR_HARD_KEY_CODE = 425992

    // Stream-channel service ids carried in header bytes 8..11.
    const val STREAM_VIDEO_DATA = 0x00020001
    const val STREAM_VIDEO_HEARTBEAT = 0x00020002
    const val STREAM_MEDIA_INIT = 0x00030001
    const val STREAM_MEDIA_STOP = 0x00030002
    const val STREAM_MEDIA_PAUSE = 0x00030003
    const val STREAM_MEDIA_RESUME = 0x00030004
    const val STREAM_MEDIA_SEEK = 0x00030005
    const val STREAM_MEDIA_DATA = 0x00030006
    const val STREAM_TTS_INIT = 0x00040001
    const val STREAM_TTS_END = 0x00040002
    const val STREAM_TTS_DATA = 0x00040003

    /** Every service id a stream-channel header may legally carry. */
    val STREAM_IDS = setOf(
        STREAM_VIDEO_DATA, STREAM_VIDEO_HEARTBEAT,
        STREAM_MEDIA_INIT, STREAM_MEDIA_STOP, STREAM_MEDIA_PAUSE, STREAM_MEDIA_RESUME,
        STREAM_MEDIA_SEEK, STREAM_MEDIA_DATA,
        STREAM_TTS_INIT, STREAM_TTS_END, STREAM_TTS_DATA,
    )

    /**
     * The challenge string the head unit sends in HU_AUTHEN_REQUEST.
     *
     * The phone splits on '_' and compares the part after it with its own version,
     * rewriting the value to "_00.00.00" when the head unit claims to be older —
     * which the phone then reports as unauthorized. Claiming the phone's own
     * version keeps the session valid.
     */
    const val AUTHEN_RANDOM_VALUE = "8.8.4_8.8.4"

    const val PROTOCOL_MAJOR = 1
    const val PROTOCOL_MINOR = 0

    /**
     * Frame header for the TCP transport.
     *
     * Cmd and Touch use `[u16 len][u16 reserved][u32 serviceType]`; the remaining
     * channels use `[u32 len][u32 timestampSeconds][u16 reserved][u16 serviceType]`.
     */
    fun header(channel: Int, serviceType: Int, payloadLength: Int, timestampSeconds: Int): ByteArray {
        val out = ByteArray(if (channel == CHANNEL_CMD || channel == CHANNEL_TOUCH) HEADER_CMD else HEADER_STREAM)
        if (out.size == HEADER_CMD) {
            out[0] = (payloadLength ushr 8).toByte(); out[1] = payloadLength.toByte()
            out[2] = 0; out[3] = 0
            out[4] = (serviceType ushr 24).toByte(); out[5] = (serviceType ushr 16).toByte()
            out[6] = (serviceType ushr 8).toByte(); out[7] = serviceType.toByte()
        } else {
            out[0] = (payloadLength ushr 24).toByte(); out[1] = (payloadLength ushr 16).toByte()
            out[2] = (payloadLength ushr 8).toByte(); out[3] = payloadLength.toByte()
            out[4] = (timestampSeconds ushr 24).toByte(); out[5] = (timestampSeconds ushr 16).toByte()
            out[6] = (timestampSeconds ushr 8).toByte(); out[7] = timestampSeconds.toByte()
            // Bytes 8..11 carry the stream service id; for video that is
            // 0x00020001 (data) or 0x00020002 (keep-alive).
            out[8] = (serviceType ushr 24).toByte(); out[9] = (serviceType ushr 16).toByte()
            out[10] = (serviceType ushr 8).toByte(); out[11] = serviceType.toByte()
        }
        return out
    }
}

/** One decoded command-channel frame. */
internal class CarLifeFrame(val serviceType: Int, val payload: ByteArray)

/**
 * Incremental splitter for the command channel.
 *
 * The header is `[u16 length][u16 reserved][u32 serviceType]`, and `length`
 * counts only the payload. Confirmed on a captured frame carrying MD_INFO:
 * `015e 0000 00010004` decodes to length 350 and id 0x00010004.
 */
internal class CarLifeCommandSplitter {
    private var buffer = ByteArray(0)

    fun feed(data: ByteArray, offset: Int, length: Int): List<CarLifeFrame> {
        val joined = ByteArray(buffer.size + length)
        System.arraycopy(buffer, 0, joined, 0, buffer.size)
        System.arraycopy(data, offset, joined, buffer.size, length)
        buffer = joined

        val frames = ArrayList<CarLifeFrame>()
        var at = 0
        while (buffer.size - at >= CarLifeProtocol.HEADER_CMD) {
            val payloadLength = ((buffer[at].toInt() and 0xFF) shl 8) or (buffer[at + 1].toInt() and 0xFF)
            val serviceType = ((buffer[at + 4].toInt() and 0xFF) shl 24) or ((buffer[at + 5].toInt() and 0xFF) shl 16) or
                ((buffer[at + 6].toInt() and 0xFF) shl 8) or (buffer[at + 7].toInt() and 0xFF)
            require(payloadLength in 0..CarLifeProtocol.MAX_PAYLOAD) { "CarLife 帧长度非法：$payloadLength" }
            if (buffer.size - at - CarLifeProtocol.HEADER_CMD < payloadLength) break

            frames += CarLifeFrame(
                serviceType,
                buffer.copyOfRange(at + CarLifeProtocol.HEADER_CMD, at + CarLifeProtocol.HEADER_CMD + payloadLength)
            )
            at += CarLifeProtocol.HEADER_CMD + payloadLength
        }
        buffer = if (at == 0) buffer else buffer.copyOfRange(at, buffer.size)
        return frames
    }
}

/**
 * Splits the video port, which carries a bare run of video packets rather than
 * channel-framed messages.
 *
 * Each packet is `[u32 payloadLength][u32 timestampSeconds][00 02 00 01]`
 * followed by one Annex-B access unit; the length excludes the 12-byte header.
 * The `00 02 00 01` marker is written verbatim by the phone's sendVideoData().
 *
 * The encoder also emits keep-alive packets with a zero length while it has no
 * new frame (`sendEmptyPacketForeground`/`Background`, marker `00 02 00 01` or
 * `00 02 00 02`). Those must be skipped by a whole header: treating a zero
 * length as a desync and advancing one byte at a time shifts every following
 * packet boundary, and the car then receives garbage instead of H.264 (the
 * symptom was one second of video and then a permanently black screen).
 */
internal class CarLifeStreamSplitter {
    private var buffer = ByteArray(0)

    fun feed(data: ByteArray, offset: Int, length: Int): List<CarLifeStreamPacket> {
        val joined = ByteArray(buffer.size + length)
        System.arraycopy(buffer, 0, joined, 0, buffer.size)
        System.arraycopy(data, offset, joined, buffer.size, length)
        buffer = joined

        val packets = ArrayList<CarLifeStreamPacket>()
        var at = 0
        while (buffer.size - at >= CarLifeProtocol.HEADER_STREAM) {
            // The last four header bytes carry the stream service id: the video
            // port uses 00 02 00 01 for a frame and 00 02 00 02 for a keep-alive,
            // the media port 00 03 00 01/00 03 00 06, and so on. A header whose
            // id is not in the known set means the stream is out of sync.
            val serviceType = ((buffer[at + 8].toInt() and 0xFF) shl 24) or ((buffer[at + 9].toInt() and 0xFF) shl 16) or
                ((buffer[at + 10].toInt() and 0xFF) shl 8) or (buffer[at + 11].toInt() and 0xFF)
            if (serviceType !in CarLifeProtocol.STREAM_IDS) { at++; continue }
            val payloadLength = ((buffer[at].toInt() and 0xFF) shl 24) or ((buffer[at + 1].toInt() and 0xFF) shl 16) or
                ((buffer[at + 2].toInt() and 0xFF) shl 8) or (buffer[at + 3].toInt() and 0xFF)
            if (payloadLength < 0 || payloadLength > CarLifeProtocol.MAX_PAYLOAD) { at++; continue }
            if (buffer.size - at - CarLifeProtocol.HEADER_STREAM < payloadLength) break
            if (payloadLength == 0) {
                // Keep-alive: consume the header only, never hand it on as media.
                at += CarLifeProtocol.HEADER_STREAM
                continue
            }

            val timestamp = ((buffer[at + 4].toInt() and 0xFF) shl 24) or ((buffer[at + 5].toInt() and 0xFF) shl 16) or
                ((buffer[at + 6].toInt() and 0xFF) shl 8) or (buffer[at + 7].toInt() and 0xFF)
            packets += CarLifeStreamPacket(
                serviceType,
                buffer.copyOfRange(at + CarLifeProtocol.HEADER_STREAM, at + CarLifeProtocol.HEADER_STREAM + payloadLength),
                timestamp.toLong() and 0xFFFFFFFFL
            )
            at += CarLifeProtocol.HEADER_STREAM + payloadLength
        }
        buffer = if (at == 0) buffer else buffer.copyOfRange(at, buffer.size)
        return packets
    }
}

/** One stream-channel packet, with the phone's second timestamp. */
internal class CarLifeStreamPacket(val serviceType: Int, val data: ByteArray, val timestampSeconds: Long)

/**
 * Just enough protobuf for the messages CarLife exchanges.
 *
 * Only varint and length-delimited fields are used by the messages this adapter
 * builds or reads.
 */
internal object CarLifeProto {
    /**
     * Removes SPS/PPS NAL units, keeping all coded pictures.
     *
     * CarLife can send parameter sets in the video stream. They belong in
     * avcC; a packet containing only parameter sets must not be queued as a
     * frame, while a mixed SPS/PPS + IDR packet must retain its IDR.
     */
    fun pictureAccessUnit(accessUnit: ByteArray): ByteArray? {
        val out = java.io.ByteArrayOutputStream(accessUnit.size)
        var foundPicture = false
        var includeBytes = false
        var i = 0
        while (i < accessUnit.size) {
            val startCodeSize = when {
                i + 3 < accessUnit.size &&
                    accessUnit[i] == 0.toByte() &&
                    accessUnit[i + 1] == 0.toByte() &&
                    accessUnit[i + 2] == 0.toByte() &&
                    accessUnit[i + 3] == 1.toByte() -> 4
                i + 2 < accessUnit.size &&
                    accessUnit[i] == 0.toByte() &&
                    accessUnit[i + 1] == 0.toByte() &&
                    accessUnit[i + 2] == 1.toByte() -> 3
                else -> 0
            }
            if (startCodeSize == 0) {
                if (includeBytes) out.write(accessUnit, i, 1)
                i++
                continue
            }
            val header = i + startCodeSize
            if (header >= accessUnit.size) break
            val nalType = accessUnit[header].toInt() and 0x1F
            if (nalType in 1..5) {
                foundPicture = true
                includeBytes = true
                out.write(accessUnit, i, minOf(startCodeSize, accessUnit.size - i))
            } else {
                includeBytes = false
            }
            i = header
        }
        return if (foundPicture) out.toByteArray() else null
    }


    class Writer {
        private val out = ByteArrayOutputStream(64)

        fun int32(field: Int, value: Int) = apply { tag(field, 0); varint(value.toLong() and 0xFFFFFFFFL) }
        fun bool(field: Int, value: Boolean) = apply { tag(field, 0); varint(if (value) 1 else 0) }
        fun string(field: Int, value: String) = apply {
            val bytes = value.toByteArray(Charsets.UTF_8)
            tag(field, 2); varint(bytes.size.toLong()); out.write(bytes, 0, bytes.size)
        }

        fun message(field: Int, body: ByteArray) = apply {
            tag(field, 2); varint(body.size.toLong()); out.write(body, 0, body.size)
        }

        private fun tag(field: Int, wireType: Int) = varint(((field.toLong()) shl 3) or wireType.toLong())

        private fun varint(value: Long) {
            var v = value
            while (true) {
                val b = (v and 0x7F).toInt()
                v = v ushr 7
                if (v == 0L) { out.write(b); return }
                out.write(b or 0x80)
            }
        }

        fun toByteArray(): ByteArray = out.toByteArray()
    }

    /**
     * A bounded protobuf reader.
     *
     * Every read is range-checked so a malformed or unexpected field reports a
     * clear error instead of an index exception, and the caller can stop without
     * losing the fields already decoded.
     */
    class Reader(private val buffer: ByteArray, private var pos: Int = 0, private val end: Int = buffer.size) {
        private var lastWireType = 0

        fun nextField(): Int {
            if (pos >= end) return 0
            val key = varint()
            lastWireType = (key and 7L).toInt()
            return (key ushr 3).toInt()
        }

        fun readInt32(): Int = varint().toInt()

        fun readString(): String {
            val length = varint().toInt()
            require(length >= 0 && pos + length <= end) { "CarLife protobuf 字符串长度越界：$length" }
            val value = String(buffer, pos, length, Charsets.UTF_8)
            pos += length
            return value
        }

        fun readBytes(): ByteArray {
            val length = varint().toInt()
            require(length >= 0 && pos + length <= end) { "CarLife protobuf 字段长度越界：$length" }
            val value = buffer.copyOfRange(pos, pos + length)
            pos += length
            return value
        }

        fun skip() {
            when (lastWireType) {
                0 -> varint()
                1 -> advance(8)
                2 -> advance(varint().toInt())
                5 -> advance(4)
                else -> error("CarLife protobuf 不支持的字段类型 $lastWireType")
            }
        }

        private fun advance(count: Int) {
            require(count >= 0 && pos + count <= end) { "CarLife protobuf 字段长度越界：$count" }
            pos += count
        }

        private fun varint(): Long {
            var result = 0L
            var shift = 0
            while (true) {
                require(pos < end) { "CarLife protobuf 数据提前结束" }
                val b = buffer[pos++].toInt()
                result = result or ((b and 0x7F).toLong() shl shift)
                if (b and 0x80 == 0) return result
                shift += 7
                require(shift < 64) { "CarLife protobuf varint 过长" }
            }
        }
    }

    /**
     * HU_STATISTIC_INFO payload.
     *
     * StatisticVehicleUtil checks the channel against `2\d{7}` and rejects the
     * two reserved values 20000000/20000001; any other `2xxxxxxx` passes.
     *
     * The channel is not decoration. CarLife stores it as `vehicle_cn` and
     * every resolution decision keys off it:
     *
     *     CarlifeUtil.setVehicleChannel(from.getChannel())
     *       -> CommonParams.vehicleChannel
     *       -> VideoResolution.getCustomResolution()  (only when
     *          SP_CUSTOM_CN == vehicle_cn)
     *       -> BaseScreenImpl.buildResolution()
     *
     * It is also the key the host uses. Honor Auto keeps one
     * `DeviceCastConfig` per `carModelId` in
     * `/data/data/com.hihonor.auto/databases/carconnect.db`
     * (`device_cast_config`), and answers CarLife's `carlife_encode_format`
     * query from that row:
     *
     *     {"bitRate":0,"carModelId":"20011100","displayHeight":1080,
     *      "displayWidth":2202,"frameRate":20,"standbyMode":1}
     *
     * CarLife then writes those numbers over the MediaFormat it built from our
     * VIDEO_ENCODER_INIT (`MediaFormatUtils.setVendorEncoderParams` ->
     * `saveCustomResolution(w,h)`). That single row is why the coded size came
     * out 2202x1080@20 no matter what was negotiated.
     *
     * `20011100` is the one channel that already has such a row, so it is the
     * worst possible choice here. A channel with no row makes Honor fall back
     * to `CastConstant.DEFAULT_*`, `carlife_encode_format` returns an empty
     * Bundle, `saveCustomResolution` is never called, and the encoder keeps the
     * size VIDEO_ENCODER_INIT asked for. [DEFAULT_VEHICLE_CHANNEL] is that
     * value; it is overridable for a car that does want Honor's profile.
     */
    fun statisticInfo(cuid: String, vehicleChannel: String = DEFAULT_VEHICLE_CHANNEL): ByteArray = Writer()
        .string(1, cuid)
        .string(2, "8.8.4")
        .int32(3, 884)
        .string(4, vehicleChannel)
        .int32(5, 1)
        .toByteArray()

    /**
     * The channel reported when the caller has no preference.
     *
     * Deliberately NOT 20011100: see [statisticInfo]. It still matches
     * CarLife's `2\d{7}` check and is not one of the two reserved values.
     */
    const val DEFAULT_VEHICLE_CHANNEL = "20190101"

    /** MEDIA_INIT / NAVI_TTS_INIT: sampleRate, channelConfig, sampleFormat. */
    fun decodeAudioInit(payload: ByteArray): Triple<Int, Int, Int> {
        var sampleRate = 0; var channels = 0; var format = 0
        val reader = Reader(payload)
        while (true) {
            when (val field = reader.nextField()) {
                0 -> return Triple(sampleRate, channels, format)
                1 -> sampleRate = reader.readInt32()
                2 -> channels = reader.readInt32()
                3 -> format = reader.readInt32()
                else -> reader.skip()
            }
        }
    }

    /**
     * VIDEO_ENCODER_INIT.
     *
     * Fields 1-3 are the encoder size and rate. Fields 4-9 describe the panel
     * around that encoder: this car's physical panel is wider than the area
     * CarLife may draw in, and CarLife positions its UI from these rather than
     * centring it. They used to be omitted and defaulted to zero, which is what
     * put the picture at the far left of the irregular panel.
     */
    fun videoEncoderInfo(
        width: Int,
        height: Int,
        frameRate: Int,
        areaWidth: Int = 0,
        areaHeight: Int = 0,
        areaX: Int = 0,
        areaY: Int = 0,
        drawOutsideSafeArea: Boolean = false,
        needAdapterScreen: Boolean = false,
    ): ByteArray {
        val writer = Writer().int32(1, width).int32(2, height).int32(3, frameRate)
        // Protobuf3 omits default values, and so does this builder: a head unit
        // that has no panel insets to report must get exactly the three-field
        // message the phone has always understood, not a longer one padded with
        // zeroes.
        if (areaWidth != 0) writer.int32(4, areaWidth)
        if (areaHeight != 0) writer.int32(5, areaHeight)
        if (areaX != 0) writer.int32(6, areaX)
        if (areaY != 0) writer.int32(7, areaY)
        if (drawOutsideSafeArea) writer.bool(8, true)
        if (needAdapterScreen) writer.bool(9, true)
        return writer.toByteArray()
    }

    /** Read the width/height/frameRate the phone reports back in VIDEO_ENCODER_INIT_DONE. */
    fun decodeVideoEncoderInfo(payload: ByteArray): Triple<Int, Int, Int> {
        var width = 0; var height = 0; var frameRate = 0
        val reader = Reader(payload)
        while (true) {
            when (val field = reader.nextField()) {
                0 -> return Triple(width, height, frameRate)
                1 -> width = reader.readInt32()
                2 -> height = reader.readInt32()
                3 -> frameRate = reader.readInt32()
                else -> reader.skip()
            }
        }
    }

    fun authenRequest(randomValue: String): ByteArray = Writer().string(1, randomValue).toByteArray()
    fun authenResult(ok: Boolean): ByteArray = Writer().bool(1, ok).toByteArray()

    /**
     * HU_FEATURE_CONFIG_RESPONSE: `[cnt][{key,value}...]`.
     *
     * CarLife reads these before it builds its encoder. DISABLE_RESOLUTION_OPTION
     * makes it encode at exactly the size it was asked for instead of quantising
     * to one of its preset buckets — which matters here because the car sets up
     * its decoder from the size negotiated over AirPlay and drops a stream whose
     * SPS disagrees with it.
     */
    fun featureConfig(vararg entries: Pair<String, Int>): ByteArray {
        val writer = Writer().int32(1, entries.size)
        for ((key, value) in entries) {
            writer.message(2, Writer().string(1, key).int32(2, value).toByteArray())
        }
        return writer.toByteArray()
    }

    fun decodeAuthenResponse(payload: ByteArray): String = runCatching {
        val reader = Reader(payload)
        while (true) {
            when (val field = reader.nextField()) {
                0 -> return@runCatching ""
                1 -> return@runCatching reader.readString()
                else -> reader.skip()
            }
        }
        @Suppress("UNREACHABLE_CODE") ""
    }.getOrDefault("")

    /**
     * DATA_SUBSCRIBE_DONE: echoes the module list the car asked about.
     *
     * `CarlifeDataSubscribe.handleMobileCarLifeInfoList` reads `cnt` and one
     * entry per module, answering with the same shape. moduleID 0 (turn-by-turn
     * HUD) and 2 (media info) are the two it will report as supported; anything
     * else is answered `supportFlag=false`.
     */
    fun dataSubscribeDone(moduleIds: List<Int>): ByteArray {
        val writer = Writer().int32(1, moduleIds.size)
        for (id in moduleIds) {
            val supported = id == 0 || id == 2
            writer.message(2, Writer().int32(1, id).bool(2, supported).toByteArray())
        }
        return writer.toByteArray()
    }

    /** Reads the module ids out of a HU_DATA_SUBSCRIBE / START / STOP body. */
    fun decodeDataSubscribe(payload: ByteArray): List<Int> = runCatching {
        val ids = mutableListOf<Int>()
        val reader = Reader(payload)
        while (true) {
            when (val field = reader.nextField()) {
                0 -> return@runCatching ids
                2 -> {
                    // Nested CarlifeSubscribeMobileCarLifeInfo: moduleID = field 1.
                    val nested = Reader(reader.readBytes())
                    while (true) {
                        when (val inner = nested.nextField()) {
                            0 -> break
                            1 -> ids += nested.readInt32()
                            else -> nested.skip()
                        }
                    }
                }
                else -> reader.skip()
            }
        }
        @Suppress("UNREACHABLE_CODE") ids
    }.getOrDefault(emptyList())

    fun decodeMatchStatus(payload: ByteArray): Int = runCatching {
        val reader = Reader(payload)
        while (true) {
            when (val field = reader.nextField()) {
                0 -> return@runCatching 0
                1 -> return@runCatching reader.readInt32()
                else -> reader.skip()
            }
        }
        @Suppress("UNREACHABLE_CODE") 0
    }.getOrDefault(0)

    /**
     * The phone's device info, of which only the fields the log shows are read.
     *
     * This is diagnostics only: CarLife's own field set drifts between builds, so
     * a field that cannot be read ends the walk and the fields decoded so far are
     * still reported. Throwing here would take the whole session down with it.
     */
    fun describeDeviceInfo(payload: ByteArray): String {
        val parts = StringBuilder()
        return runCatching {
            val reader = Reader(payload)
            while (true) {
                when (val field = reader.nextField()) {
                    0 -> return@runCatching parts.toString().trim()
                    21, 25 -> parts.append("f$field=${reader.readInt32()} ")
                    in 1..20 -> reader.readString().takeIf { it.isNotEmpty() }?.let { parts.append("f$field=$it ") }
                    22, 23, 24, 26 -> reader.readString().takeIf { it.isNotEmpty() }?.let { parts.append("f$field=$it ") }
                    else -> reader.skip()
                }
            }
            @Suppress("UNREACHABLE_CODE") ""
        }.getOrElse { parts.append("(解析在 f 处停止：${it.message})").toString().trim() }
    }

    /** Extracts Annex-B SPS/PPS NAL units, preserving their start codes. */
    fun avcParameterSets(accessUnit: ByteArray): Pair<ByteArray?, ByteArray?> {
        var sps: ByteArray? = null
        var pps: ByteArray? = null
        var i = 0
        while (i < accessUnit.size) {
            val startCodeSize = when {
                i + 3 < accessUnit.size &&
                    accessUnit[i] == 0.toByte() &&
                    accessUnit[i + 1] == 0.toByte() &&
                    accessUnit[i + 2] == 0.toByte() &&
                    accessUnit[i + 3] == 1.toByte() -> 4
                i + 2 < accessUnit.size &&
                    accessUnit[i] == 0.toByte() &&
                    accessUnit[i + 1] == 0.toByte() &&
                    accessUnit[i + 2] == 1.toByte() -> 3
                else -> 0
            }
            if (startCodeSize == 0) {
                i++
                continue
            }

            val header = i + startCodeSize
            var end = accessUnit.size
            var next = header + 1
            while (next < accessUnit.size) {
                val nextStartCodeSize = when {
                    next + 3 < accessUnit.size &&
                        accessUnit[next] == 0.toByte() &&
                        accessUnit[next + 1] == 0.toByte() &&
                        accessUnit[next + 2] == 0.toByte() &&
                        accessUnit[next + 3] == 1.toByte() -> 4
                    next + 2 < accessUnit.size &&
                        accessUnit[next] == 0.toByte() &&
                        accessUnit[next + 1] == 0.toByte() &&
                        accessUnit[next + 2] == 1.toByte() -> 3
                    else -> 0
                }
                if (nextStartCodeSize != 0) {
                    end = next
                    break
                }
                next++
            }
            if (header < end) {
                when (accessUnit[header].toInt() and 0x1F) {
                    7 -> sps = accessUnit.copyOfRange(i, end)
                    8 -> pps = accessUnit.copyOfRange(i, end)
                }
            }
            i = end
        }
        return sps to pps
    }

    /** Builds an avcC record when this access unit carries both parameter sets. */
    fun avcConfig(accessUnit: ByteArray): ByteArray? {
        val (sps, pps) = avcParameterSets(accessUnit)
        return avcConfig(sps, pps)
    }

    /** Builds an avcC record from parameter sets collected across access units. */
    fun avcConfig(sps: ByteArray?, pps: ByteArray?): ByteArray? {
        val s = sps ?: return null
        val p = pps ?: return null
        return VideoPackets.avcC(s, p)
    }

    /** Single-point touch: x, y. */
    fun touchPoint(x: Int, y: Int): ByteArray = Writer().int32(1, x).int32(2, y).toByteArray()

    /** Touch action: action, x, y. */
    fun touchAction(action: Int, x: Int, y: Int): ByteArray =
        Writer().int32(1, action).int32(2, x).int32(3, y).toByteArray()

    fun hardKey(keyCode: Int): ByteArray = Writer().int32(1, keyCode).toByteArray()
}
