package com.carplay.adapter.carlife

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.InputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Returns true only when an access unit contains an H.264 IDR slice.
 *
 * SPS (7) and PPS (8) describe the decoder but are not independently
 * decodable pictures. They can arrive in their own CarLife packet, so treating
 * SPS as a key frame lets the sender release a parameter-set-only packet after
 * a decoder reset and leaves the head unit waiting on an unusable picture.
 */
internal fun isCarLifeKeyFrame(data: ByteArray): Boolean {
    var i = 0
    while (i < data.size) {
        val startCodeSize = when {
            i + 4 < data.size &&
                data[i] == 0.toByte() &&
                data[i + 1] == 0.toByte() &&
                data[i + 2] == 0.toByte() &&
                data[i + 3] == 1.toByte() -> 4
            i + 3 < data.size &&
                data[i] == 0.toByte() &&
                data[i + 1] == 0.toByte() &&
                data[i + 2] == 1.toByte() -> 3
            else -> 0
        }
        if (startCodeSize != 0) {
            val header = i + startCodeSize
            if (header < data.size && (data[header].toInt() and 0x1F) == 5) return true
            i = header
        } else {
            i++
        }
    }
    return false
}

/**
 * Drives the CarLife head-unit side of the session so the phone renders its own
 * car-mode UI and hands back the encoded H.264.
 *
 * The phone is the server: it listens on seven ports and only declares the
 * session connected once six of them (everything but Data) have been accepted.
 * It will not build its encoder before that, so opening the command port alone
 * never produces video.
 *
 * Once connected, the head unit has to ask for video with VIDEO_ENCODER_INIT.
 * This build never volunteers it, and never sends FEATURE_CONFIG_REQUEST or
 * AUTHEN_RESULT, so waiting for either of those stalls forever.
 */
internal class CarLifeSession(
    private val host: String,
    private val width: Int,
    private val height: Int,
    private val fps: Int,
    private val log: (String) -> Unit,
    private val hostId: String = "carlink-cast-host",
    /**
     * The `vehicle_cn` reported in HU_STATISTIC_INFO.
     *
     * CarLife keys its resolution decisions off this value, and so does Honor
     * Auto: it keeps one DeviceCastConfig per carModelId in
     * `carconnect.db:device_cast_config` and answers `carlife_encode_format`
     * from that row. Reporting 20011100 (which has such a row: 2202x1080@20)
     * made Honor override whatever size VIDEO_ENCODER_INIT asked for. The
     * default here is a channel with no row, so the encoder keeps the size this
     * app requested.
     */
    private val vehicleChannel: String = CarLifeProto.DEFAULT_VEHICLE_CHANNEL,
    /**
     * Panel and safe area to hand CarLife, or null to request the bare
     * rectangle this app has always asked for.
     *
     * CarLife applies these through VideoMsgHandler.initMediaCodec and forwards
     * them to its vendor encoder. The host supplies the safe area as a hint but
     * keeps a full-panel UI for the opaque H.264 source.
     */
    private val panel: CarLifePanel? = null,
    private val onEncoderReady: (Int, Int, Int) -> Unit,
    private val onVideoFrame: (ByteArray, Long, Boolean) -> Unit,
    private val onAudioInit: (sampleRate: Int, channels: Int, format: Int) -> Unit = { _, _, _ -> },
    private val onAudioData: (ByteArray) -> Unit = {},
    private val onAudioEnded: () -> Unit = {},
    private val onTtsInit: (sampleRate: Int, channels: Int, format: Int) -> Unit = { _, _, _ -> },
    private val onTtsData: (ByteArray) -> Unit = {},
    private val onFailure: (Throwable) -> Unit = {},
    /**
     * Reference back to the adapter for accessing safeAreaPolicy.
     * This allows CarLifeSession to determine encoder source dimensions.
     *
     * IMPORTANT: We capture the policy value at construction time to avoid
     * concurrent modification issues. The policy should not change after
     * the session starts, so this snapshot is stable and safe to use.
     */
    private val container: CarLifeProjectionAdapter? = null,
) : AutoCloseable {

    private val running = AtomicBoolean(false)
    private val retired = AtomicBoolean(false)
    private val lifecycleLock = Any()
    private val failureReported = AtomicBoolean(false)

    // Snapshot of safeAreaPolicy captured at construction time to avoid
    // concurrent modification issues while the session runs
    private val snapshotedSafeAreaPolicy = container?.safeAreaPolicy
    private var cmdSocket: Socket? = null
    private var cmdOut: BufferedOutputStream? = null
    private var touchSocket: Socket? = null
    private var touchPacketsSent = 0L
    private var touchUnavailableLogged = false
    private val auxSockets = ArrayList<Socket>()
    private val threads = ArrayList<Thread>()
    /** Keeps the first encoder request separate from later restore rebuilds. */
    private val encoderLifecycle = CarLifeEncoderLifecycle()

    /** Set once the phone reports its encoder is up. */
    @Volatile var streaming = false
        private set

    /** Invalidates delayed rate requests when a newer encoder is initialized. */
    @Volatile private var encoderRateGeneration = 0L

    /**
     * True while the head unit has asked CarLife to push media metadata.
     *
     * CarLife only sends MEDIA_INFO / MEDIA_PROGRESS_BAR after the car's
     * data-subscription handshake names module 2. Recorded so a session log
     * shows whether "cluster has no media info" was a subscription problem or
     * a metadata problem.
     */
    @Volatile var mediaInfoSubscribed = false
        private set

    /** Set once the data-subscription handshake has been sent for this session. */
    private var mediaSubscribed = false

    @Volatile var failure: Throwable? = null
        private set

    /** Connects the command port and runs the handshake as far as streaming. */
    fun connect(timeoutMs: Int = 15000) {
        synchronized(lifecycleLock) {
            check(!retired.get()) { "CarLife 会话已退役" }
            check(running.compareAndSet(false, true)) { "CarLife 会话已启动" }
        }
        check(!retired.get()) { "CarLife 会话已退役" }
        val cmd = connectWithRetry(CarLifeProtocol.PORT_CMD, timeoutMs)
        check(!retired.get()) { runCatching { cmd.close() }; "CarLife 会话已退役" }
        cmdSocket = cmd
        cmdOut = BufferedOutputStream(cmd.getOutputStream())

        // Six sockets are required before the phone will encode, so bring the
        // rest up before the handshake reaches the video request.
        connectAuxPorts()
        check(!retired.get()) { "CarLife 会话已退役" }

        startReader("carlife-cmd", cmd.getInputStream()) { splitter, buffer, read ->
            for (frame in splitter.feed(buffer, 0, read)) {
                // A message this build does not understand must not end the
                // session; only the frame it belongs to is affected.
                runCatching { handleCommand(frame.serviceType, frame.payload) }
                    .onFailure { log("处理消息 0x${frame.serviceType.toString(16)} 出错：${it.message}") }
            }
        }
        sendCommand(
            CarLifeProtocol.HU_PROTOCOL_VERSION,
            CarLifeProto.Writer().int32(1, CarLifeProtocol.PROTOCOL_MAJOR).int32(2, CarLifeProtocol.PROTOCOL_MINOR).toByteArray()
        )
    }

    /**
     * Connects one CarLife port, retrying while the phone is still starting up.
     *
     * CarLife only opens these listeners once its process is alive, and the
     * process takes a few seconds to come up after it is launched. A single
     * connect attempt therefore lands on ECONNREFUSED and the whole session is
     * lost, even though the app is about to be ready.
     */
    private fun connectWithRetry(
        port: Int,
        timeoutMs: Int,
        windowMs: Long = CONNECT_RETRY_WINDOW_MS,
    ): Socket {
        val deadline = System.nanoTime() + windowMs * 1_000_000
        while (true) {
            try {
                return Socket().apply {
                    tcpNoDelay = true
                    keepAlive = true
                    connect(InetSocketAddress(host, port), timeoutMs)
                }
            } catch (error: Exception) {
                if (System.nanoTime() >= deadline) throw error
                Thread.sleep(CONNECT_RETRY_INTERVAL_MS)
            }
        }
    }

    private fun connectAuxPorts() {
        val missingRequired = ArrayList<Int>()
        for (port in CarLifeProtocol.AUX_PORTS) {
            try {
                // A shorter window here: the command port above already proved
                // the process is up, so a port that is still refused is a
                // failed session unless it is the optional Data side channel.
                val socket = connectWithRetry(port, 3000, AUX_CONNECT_WINDOW_MS)
                auxSockets += socket
                when (port) {
                    // Video and media carry bare stream packets, not channel frames.
                    CarLifeProtocol.PORT_VIDEO -> startVideoReader(socket.getInputStream())
                    CarLifeProtocol.PORT_MEDIA -> startMediaReader(socket.getInputStream())
                    // The touch port is write-only for us; the phone reads it.
                    CarLifeProtocol.PORT_TOUCH -> touchSocket = socket
                    CarLifeProtocol.PORT_TTS -> startTtsReader(socket.getInputStream())
                    // VR/Data are opened for session accounting. VR microphone
                    // delivery is intentionally not claimed until its framing
                    // and peer/authentication path is verified.
                    else -> Unit
                }
                log("CarLife 端口 $port 已连接")
            } catch (error: Exception) {
                log("CarLife 端口 $port 连接失败：${error.message}")
                if (port in CarLifeProtocol.REQUIRED_AUX_PORTS) missingRequired += port
            }
        }
        if (missingRequired.isNotEmpty()) {
            throw java.io.IOException("CarLife 必需端口连接失败：${missingRequired.joinToString(",")}")
        }
        log("CarLife 端口 ${auxSockets.size + 1}/${CarLifeProtocol.AUX_PORTS.size + 1} 已连接")
    }

    private fun startReader(name: String, input: InputStream, onFrame: (CarLifeCommandSplitter, ByteArray, Int) -> Unit) {
        val thread = Thread({
            val splitter = CarLifeCommandSplitter()
            val buffer = ByteArray(32 * 1024)
            try {
                val stream = BufferedInputStream(input)
                while (running.get()) {
                    val read = stream.read(buffer)
                    if (read < 0) {
                        reportFailure(java.io.EOFException("CarLife 命令端口已关闭"))
                        break
                    }
                    if (read == 0) continue
                    onFrame(splitter, buffer, read)
                }
            } catch (error: Throwable) {
                reportFailure(error)
            }
        }, name).apply { isDaemon = true }
        threads += thread
        thread.start()
    }

    private fun startVideoReader(input: InputStream) {
        val thread = Thread({
            val splitter = CarLifeStreamSplitter()
            val buffer = ByteArray(64 * 1024)
            try {
                val stream = BufferedInputStream(input)
                while (running.get()) {
                    val read = stream.read(buffer)
                    if (read < 0) {
                        reportFailure(java.io.EOFException("CarLife 视频端口已关闭"))
                        break
                    }
                    if (read == 0) continue
                    for (packet in splitter.feed(buffer, 0, read)) {
                        if (packet.serviceType != CarLifeProtocol.STREAM_VIDEO_DATA) continue
                        onVideoFrame(packet.data, packet.timestampSeconds, isCarLifeKeyFrame(packet.data))
                    }
                }
            } catch (error: Throwable) {
                reportFailure(error)
            }
        }, "carlife-video").apply { isDaemon = true }
        threads += thread
        thread.start()
    }

    private fun startTtsReader(input: InputStream) {
        val thread = Thread({
            val splitter = CarLifeStreamSplitter()
            val buffer = ByteArray(64 * 1024)
            try {
                val stream = BufferedInputStream(input)
                while (running.get()) {
                    val read = stream.read(buffer)
                    if (read < 0) {
                        log("CarLife TTS 辅助端口已关闭，继续保持视频会话")
                        break
                    }
                    if (read == 0) continue
                    for (packet in splitter.feed(buffer, 0, read)) {
                        when (packet.serviceType) {
                            CarLifeProtocol.STREAM_TTS_INIT -> {
                                val (rate, channels, format) = CarLifeProto.decodeAudioInit(packet.data)
                                log("CarLife TTS 音频格式：${rate}Hz / ${channels} 声道 / 格式 $format")
                                onTtsInit(rate, channels, format)
                            }
                            CarLifeProtocol.STREAM_TTS_DATA -> onTtsData(packet.data)
                            CarLifeProtocol.STREAM_TTS_END -> log("CarLife TTS 结束")
                        }
                    }
                }
            } catch (error: Exception) {
                if (running.get()) log("CarLife TTS 辅助流结束：${error.message}")
            }
        }, "carlife-tts").apply { isDaemon = true }
        threads += thread
        thread.start()
    }

    /**
     * The media port carries the car-mode audio: MEDIA_INIT announces the PCM
     * format and MEDIA_DATA carries raw little-endian PCM. It is decoded here
     * so the source can encode it to AAC for AirPlay.
     */
    private fun startMediaReader(input: InputStream) {
        val thread = Thread({
            val splitter = CarLifeStreamSplitter()
            val buffer = ByteArray(64 * 1024)
            try {
                val stream = BufferedInputStream(input)
                while (running.get()) {
                    val read = stream.read(buffer)
                    if (read < 0) {
                        // Honor closes the media socket when the vehicle changes
                        // audio mode.  The video/control sockets remain valid;
                        // treating this EOF as a session failure was the direct
                        // cause of "音频没了，画面也一起断开".
                        log("CarLife 音频端口已关闭，停止音频读取但保留视频会话")
                        runCatching { onAudioEnded() }
                        break
                    }
                    if (read == 0) continue
                    for (packet in splitter.feed(buffer, 0, read)) {
                        when (packet.serviceType) {
                            CarLifeProtocol.STREAM_MEDIA_INIT -> {
                                val (sampleRate, channels, format) = CarLifeProto.decodeAudioInit(packet.data)
                                log("CarLife 音频格式：${sampleRate}Hz / ${channels} 声道 / 格式 $format")
                                onAudioInit(sampleRate, channels, format)
                            }
                            CarLifeProtocol.STREAM_MEDIA_DATA -> onAudioData(packet.data)
                            CarLifeProtocol.STREAM_MEDIA_STOP,
                            CarLifeProtocol.STREAM_MEDIA_PAUSE -> log("CarLife 音频暂停")
                            CarLifeProtocol.STREAM_MEDIA_RESUME -> log("CarLife 音频恢复")
                        }
                    }
                }
            } catch (error: Throwable) {
                if (running.get()) {
                    log("CarLife 音频流结束，视频会话继续：${error.message ?: error.javaClass.simpleName}")
                    runCatching { onAudioEnded() }
                }
            }
        }, "carlife-media").apply { isDaemon = true }
        threads += thread
        thread.start()
    }

    private fun handleCommand(serviceType: Int, payload: ByteArray) {
        // The phone sets 0x8000 on the low half of the id; the head unit does not.
        val id = 0x00010000 or (serviceType and 0x7FFF)
        when (id) {
            CarLifeProtocol.PROTOCOL_VERSION_MATCH_STATUS -> {
                log("协议版本匹配 status=${CarLifeProto.decodeMatchStatus(payload)}")
                sendDeviceInfo()
                // CarLife's StatisticVehicleUtil starts its 120-second
                // authorization timer as soon as the protocol matches and only
                // stops it on this report; without it the phone shows
                // "车机版本未经CarLife授权，即将断开连接" and tears the session down.
                sendCommand(
                    CarLifeProtocol.HU_STATISTIC_INFO,
                    CarLifeProto.statisticInfo(hostId, vehicleChannel),
                )
                log(
                    "已上报车辆渠道信息 $vehicleChannel" +
                        if (vehicleChannel == CarLifeProto.DEFAULT_VEHICLE_CHANNEL) {
                            "（该渠道在荣耀车联里没有自定义分辨率配置，编码尺寸由本应用决定）"
                        } else "（注意：该渠道若在荣耀车联里有配置，会覆盖编码尺寸）",
                )
            }

            CarLifeProtocol.MD_INFO -> {
                log("手机信息：${CarLifeProto.describeDeviceInfo(payload)}")
                sendCommand(CarLifeProtocol.HU_AUTHEN_REQUEST, CarLifeProto.authenRequest(CarLifeProtocol.AUTHEN_RANDOM_VALUE))
            }

            CarLifeProtocol.MD_AUTHEN_RESPONSE -> {
                sendCommand(CarLifeProtocol.HU_AUTHEN_RESULT, CarLifeProto.authenResult(true))

                // This build never sends FEATURE_CONFIG_REQUEST, so the host has
                // to volunteer the answer. FOCUS_UI is the one that matters: it
                // tells CarLife to render its car-mode UI into the encoder
                // instead of leaving the connect page on screen (which yields
                // one frame and then a black 10 fps trickle).
                sendCommand(
                    CarLifeProtocol.HU_FEATURE_CONFIG_RESPONSE,
                    CarLifeProto.featureConfig(
                        FEATURE_FOCUS_UI to 1,
                        FEATURE_DISABLE_RESOLUTION_OPTION to 1,
                        // CarLife reads this as setSceneWakeUpEnable(value != 1),
                        // so 0 keeps scene wake-up on and 1 would switch it off.
                        FEATURE_DISABLE_VOICE_COMMAND to 0,
                        FEATURE_CONTENT_ENCRYPTION to 0,
                        // CarLife outputs 44.1 kHz PCM unless the head unit asks
                        // for 48 kHz, and this car only accepts AAC-LC at 48 kHz.
                        // Leaving this out produced a stream the car silently
                        // dropped, which is what "完全没声音" was.
                        FEATURE_MEDIA_SAMPLE_RATE to 1,
                        // Record from the phone's own microphone. Without this
                        // CarLife waits for microphone audio from the head unit,
                        // which a Wi-Fi sender never sends, so the car's voice
                        // button did nothing.
                        FEATURE_VOICE_MIC to 1
                    )
                )
                log("已主动下发车机功能配置：FOCUS_UI=1，MEDIA_SAMPLE_RATE=48K，VOICE_MIC=手机")
                // Nothing else asks the phone to start encoding: this build sends
                // neither FEATURE_CONFIG_REQUEST nor AUTHEN_RESULT. Requesting the
                // encoder here is what starts the video stream.
                if (encoderLifecycle.requestInitial()) {
                    log("认证通过，请求手机编码器 ${width}x$height@$fps")
                    sendCommand(CarLifeProtocol.VIDEO_ENCODER_INIT, encoderInitPayload())
                } else {
                    log("认证重复：首次 VIDEO_ENCODER_INIT 已在处理中，忽略重复请求")
                }
            }

            CarLifeProtocol.VIDEO_ENCODER_INIT_DONE -> {
                val (w, h, rate) = CarLifeProto.decodeVideoEncoderInfo(payload)
                if (!encoderLifecycle.onEncoderInitDone()) {
                    log("忽略过期 VIDEO_ENCODER_INIT_DONE：当前编码器状态不匹配")
                    return
                }
                val rateGeneration = ++encoderRateGeneration
                log("手机编码器就绪 ${w}x$h@$rate")
                // This callback is the source-side generation boundary.  The
                // AirPlay sender may receive a fresh IDR immediately after
                // INIT_DONE, so the generation must be advanced before START
                // and before any video access unit is delivered.
                onEncoderReady(w, h, rate)
                // The encoder dimensions alone cannot prove that the vendor
                // honored the panel/background flags. Log the response without
                // promoting a matching width/height into a false fill success.
                panel?.let { area ->
                    log(
                        "编码器几何回报：请求面板 ${area.panelWidth}x${area.panelHeight}" +
                            "（安全区 ${area.safeWidth}x${area.safeHeight}@(${area.safeX},${area.safeY})），" +
                            "CarLife 回 ${w}x$h；仅凭回报尺寸不能确认背景外扩或居中生效",
                    )
                }
                sendCommand(CarLifeProtocol.VIDEO_ENCODER_START, ByteArray(0))
                encoderLifecycle.onEncoderStarted()
                streaming = true
                // The PHONE's reported rate is only an acknowledgement. Real
                // sessions have reported 30 here while the emitted stream
                // remained at 20, so ask for the car's target after every
                // completed encoder init, including the initial generation.
                requestFrameRate(fps, attempt = 0, generation = rateGeneration)
                /*
                 * Subscribe to the phone's media data, once CarLife is fully up.
                 *
                 * `CarlifeConnectHsgHandler` registers handlers for
                 * MSG_CMD_CARLIFE_DATA_SUBSCRIBE / _START / _STOP and waits for
                 * the head unit to ask. `CarlifeDataSubscribe.setMobileCarLife
                 * State` then flips `MusicInfoSender.setSubscibe` from moduleID
                 * 2, and only a subscribed sender emits MEDIA_INFO (0x10035) and
                 * MEDIA_PROGRESS_BAR (0x10036) — the title, artist, lyrics and
                 * position the cluster shows.
                 *
                 * Sending this during authentication was too early: CarLife
                 * answered nothing at all. By INIT_DONE it has the whole command
                 * path running, which is when a real head unit asks.
                 * moduleID 0 is turn-by-turn, 2 is media.
                 */
                if (!mediaSubscribed) {
                    mediaSubscribed = true
                    sendCommand(
                        CarLifeProtocol.HU_DATA_SUBSCRIBE,
                        CarLifeProto.dataSubscribeDone(listOf(0, 2)),
                    )
                    sendCommand(
                        CarLifeProtocol.HU_DATA_SUBSCRIBE_START,
                        CarLifeProto.dataSubscribeDone(listOf(0, 2)),
                    )
                    log("已向 CarLife 订阅数据：模块=0(导航),2(媒体)")
                }
            }

            CarLifeProtocol.MD_FEATURE_CONFIG_REQUEST -> {
                // DISABLE_RESOLUTION_OPTION makes CarLife encode at exactly the
                // size it was asked for. Without it, it quantises the request to
                // its own preset buckets, and the resulting SPS then disagrees
                // with the size the car already configured its decoder for.
                sendCommand(
                    CarLifeProtocol.HU_FEATURE_CONFIG_RESPONSE,
                    CarLifeProto.featureConfig(
                        FEATURE_DISABLE_RESOLUTION_OPTION to 1,
                        // CarLife reads this as setSceneWakeUpEnable(value != 1),
                        // so 0 keeps scene wake-up on and 1 would switch it off.
                        FEATURE_DISABLE_VOICE_COMMAND to 0,
                        FEATURE_CONTENT_ENCRYPTION to 0,
                        FEATURE_MEDIA_SAMPLE_RATE to 1,
                        // Repeated here on purpose: CarLife asks for the config
                        // a second time after authentication, and this answer
                        // replaces the earlier one. Omitting the key would let
                        // the phone-mic decision fall back to "head unit
                        // records", which is what silenced the voice button.
                        FEATURE_VOICE_MIC to 1
                    )
                )
                log("已回复车机功能配置：按请求分辨率编码，48K 音频，VOICE_MIC=手机")
            }

            CarLifeProtocol.VIDEO_ENCODER_FRAME_RATE_CHANGE_DONE -> {
                val rate = runCatching { CarLifeProto.Reader(payload).let {
                    var value = 0
                    while (true) {
                        when (val field = it.nextField()) {
                            0 -> return@let value
                            1 -> value = it.readInt32()
                            else -> it.skip()
                        }
                    }
                    @Suppress("UNREACHABLE_CODE") value
                } }.getOrNull()
                log("CarLife 帧率已变更：${rate ?: "未知"}fps（目标 ${fps}fps）")
            }

            CarLifeProtocol.MD_EXIT -> log("手机要求结束会话")

            /*
             * Mobile data subscription — the switch behind the cluster and
             * centre-console media display.
             *
             * CarLife waits for the car to ask before it pushes anything.
             * `CarlifeDataSubscribe.setMobileCarLifeState` flips
             * `MusicInfoSender.setSubscibe` from moduleID 2, and only a
             * subscribed sender emits MEDIA_INFO / MEDIA_PROGRESS_BAR. The
             * cluster sat on "正在加载媒体…" in every field session because
             * this handshake was never sent.
             */
            CarLifeProtocol.HU_DATA_SUBSCRIBE -> {
                val modules = CarLifeProto.decodeDataSubscribe(payload)
                sendCommand(
                    CarLifeProtocol.DATA_SUBSCRIBE_DONE,
                    CarLifeProto.dataSubscribeDone(modules),
                )
                log("已应答数据订阅：模块=${modules.joinToString(",")}（0=导航 2=媒体）")
            }
            CarLifeProtocol.HU_DATA_SUBSCRIBE_START -> {
                val modules = CarLifeProto.decodeDataSubscribe(payload)
                log(
                    "车机开始订阅手机数据：模块=${modules.joinToString(",")}" +
                        if (2 in modules) "，媒体信息已开启" else "",
                )
                mediaInfoSubscribed = 2 in modules || modules.isEmpty()
            }
            CarLifeProtocol.HU_DATA_SUBSCRIBE_STOP -> {
                val modules = CarLifeProto.decodeDataSubscribe(payload)
                log("车机停止订阅手机数据：模块=${modules.joinToString(",")}")
                if (modules.isEmpty() || 2 in modules) mediaInfoSubscribed = false
            }

            CarLifeProtocol.MEDIA_INFO -> log("媒体信息已推送：${payload.size}B")
            CarLifeProtocol.MEDIA_PROGRESS_BAR -> Unit

            else -> Unit
        }
    }

    private fun sendDeviceInfo() {
        val payload = CarLifeProto.Writer()
            .string(1, "Android").string(2, "carplay").string(4, "carplay")
            .string(13, "carplay").string(14, "CarPlay Sender").string(15, "carplay")
            .string(19, "11").int32(21, 30)
            .toByteArray()
        sendCommand(CarLifeProtocol.HU_INFO, payload)
    }

    @Synchronized
    private fun sendCommand(serviceType: Int, payload: ByteArray) {
        val out = cmdOut ?: return
        try {
            out.write(CarLifeProtocol.header(CarLifeProtocol.CHANNEL_CMD, serviceType, payload.size, 0))
            if (payload.isNotEmpty()) out.write(payload)
            out.flush()
        } catch (error: Exception) {
            reportFailure(error)
        }
    }

    /**
     * Preserve the first terminal error for diagnostics and notify the owner
     * exactly once. Reader EOF is terminal too: leaving AirPlay alive after
     * CarLife has closed its socket leaks the phone's CarLife audio route.
     */
    private fun reportFailure(error: Throwable) {
        if (!running.get()) return
        if (failure == null) failure = error
        if (failureReported.compareAndSet(false, true)) {
            runCatching { onFailure(error) }
                .onFailure { log("CarLife 失败回调异常：${it.message}") }
        }
    }

    /** Sends one touch event. Action is 0 down, 1 up, anything else move. */
    fun touch(action: Int, x: Int, y: Int) {
        val id = when (action) {
            0 -> CarLifeProtocol.MSG_TOUCH_ACTION_DOWN
            1 -> CarLifeProtocol.MSG_TOUCH_ACTION_UP
            else -> CarLifeProtocol.MSG_TOUCH_ACTION_MOVE
        }
        sendTouch(id, CarLifeProto.touchPoint(x, y))
    }

    /** Sends a head-unit key code, such as a steering-wheel button. */
    fun key(keyCode: Int) = sendTouch(CarLifeProtocol.MSG_TOUCH_CAR_HARD_KEY_CODE, CarLifeProto.hardKey(keyCode))

    /**
     * Forces CarLife to hand out a fresh independently decodable frame.
     *
     * CarLife configures its encoder with `i-frame-interval: 0`, so it only
     * emits an IDR at startup; there is no protocol message that asks for one.
     * The pair the real head unit sends when the driver comes back to CarPlay
     * is PAUSE then START: CarLife marks the stream paused, and on START it sets
     * `setNeedDropFrame(true)` and drops output until the encoder produces a new
     * I-frame. Without this the car restarted its decoder mid-GOP and showed the
     * background only.
     */
    /**
     * Forces CarLife to hand out a fresh SPS/PPS + IDR.
     *
     * PAUSE then START does **not** work here and must never be used to request
     * a key frame. On START CarLife runs `Recorder.resumeFromHeadUnit()`, which
     * calls `setNeedDropFrame(true)`, and `VideoOutputThread` only stops
     * dropping once it sees an I frame. It only asks the encoder for one on
     * OPPO ROMs, and this encoder is configured with `i-frame-interval: 0`
     * (GOP=0), so on any other phone no I frame ever arrives and the picture
     * stays black for good. That is exactly the "go back to the car's screen
     * and CarPlay comes back empty" symptom.
     *
     * Rebuilding the encoder is the one protocol path that reliably produces a
     * new IDR: VIDEO_ENCODER_INIT re-creates MediaCodec, whose first output is
     * SPS/PPS followed by an IDR. The source must therefore re-read the
     * parameter sets from the stream afterwards.
     */
    fun requestKeyFrame(): Boolean {
        if (!streaming) {
            log("CarLife 编码器尚未进入 STREAMING，忽略恢复请求，避免启动阶段重复 INIT")
            return false
        }
        if (!encoderLifecycle.requestRebuild()) {
            log("CarLife 恢复请求已合并：当前编码器重建仍未完成")
            return false
        }
        sendCommand(CarLifeProtocol.VIDEO_ENCODER_INIT, encoderInitPayload())
        log("已请求 CarLife 重建编码器以获得关键帧")
        return true
    }

    /**
     * Builds VIDEO_ENCODER_INIT for the CarLife source surface.
     *
     * The encoder source size is determined by the safe-area policy:
     * - If safe area is enabled, encode at safe-area dimensions
     * - Otherwise, encode at full panel dimensions
     *
     * The safe-area metadata is always transmitted via areaWidth/areaHeight/areaX/areaY.
     */
    private fun encoderInitPayload(): ByteArray {
        val area = panel ?: return CarLifeProto.videoEncoderInfo(width, height, fps)

        // Determine source encoding dimensions based on safe-area policy
        // We use the snapshotted policy from construction time to avoid concurrent
        // modification issues. The policy is stored as Any? to avoid cross-module dependency.
        // We check if it has an 'enabled' property via reflection.
        var useSafeAreaSource = false
        val policy = snapshotedSafeAreaPolicy
        if (policy != null) {
            try {
                // Kotlin data-class `val`s compile to getter methods, not
                // fields, so getField("enabled") always threw and the safe-area
                // policy was silently ignored on every session.
                val enabledField = policy.javaClass.methods.firstOrNull {
                    it.name == "getEnabled" && it.parameterCount == 0
                }
                val modeField = policy.javaClass.methods.firstOrNull {
                    it.name == "getMode" && it.parameterCount == 0
                }
                val enabled = enabledField?.invoke(policy) as? Boolean ?: false
                val mode = modeField?.invoke(policy)?.toString() ?: "OFF"
                useSafeAreaSource = enabled && mode != "OFF"
                log("安全区策略已应用：enabled=$enabled, mode=$mode, useSafeAreaSource=$useSafeAreaSource")
            } catch (e: Exception) {
                // Policy doesn't have the expected structure, use safe area
                log("解析 safeAreaPolicy 失败：${e.message}，回退到全屏编码")
            }
        } else {
            log("safeAreaPolicy 未设置，使用全屏编码")
        }

        /*
         * The coded frame is always the canvas the head unit announced.
         *
         * The safe rectangle is layout metadata, passed below as
         * areaWidth/areaHeight/areaX/areaY so CarLife keeps its UI inside it.
         * It must not shrink the coded frame: the car checks the size the
         * encoder reports against the canvas it declared, and answers SETUP
         * with 400 when they differ. Measured on the Buick unit — canvas
         * 1972x520, encoder reported 1408x476, SETUP refused.
         *
         * The safe-area policy still decides how the compositor places the
         * picture (full canvas or inside the safe rectangle); it does not
         * decide how large the source is encoded.
         */
        /*
         * With a safe-area policy on, CarLife is asked to encode the safe
         * rectangle itself.
         *
         * That is the only way to get a picture that is simultaneously the
         * safe area's shape, unscaled and free of cropping: the compositor then
         * places it 1:1 at the safe origin and paints wallpaper around it. Any
         * other route distorts (stretching 3.79:1 into 2.96:1), crops (fitting
         * the safe shape cuts the side rail) or blurs (enlarging a fitted
         * frame).
         *
         * An earlier note here claimed the car rejects an encoder whose size
         * differs from the canvas it declared. The capture it was based on
         * actually shows the 400 landing on the audio fallback SETUP: the
         * screen SETUP and RECORD had both returned 200 with a 1408x476
         * encoder, so the size was never the objection. With the audio path
         * fixed, this is the shape to use.
         */
        /*
         * Encode at the SAFE AREA's size and shape.
         *
         * The driver asked for CarLife to fill the safe rectangle exactly, not
         * to be drawn large and shrunk into it. Telling CarLife to encode at
         * 1408x476 makes its UI lay itself out for that shape, so the picture
         * fills the rectangle with no letterboxing and its controls are sized
         * for the space it actually occupies.
         *
         * The safe-area metadata below still goes along (areaX/areaY/wh/draw
         * outside) so CarLife knows where the car's edge overlays sit relative
         * to its canvas.
         */
        val sourceWidth = if (useSafeAreaSource) area.safeWidth else area.panelWidth
        val sourceHeight = if (useSafeAreaSource) area.safeHeight else area.panelHeight

        log(
            "编码器几何：AirPlay 全画布 ${area.panelWidth}x${area.panelHeight}，" +
                "CarLife 源=${sourceWidth}x$sourceHeight (全画布比例)，" +
                "安全区元数据=${area.safeWidth}x${area.safeHeight}@(${area.safeX},${area.safeY})",
        )
        return CarLifeProto.videoEncoderInfo(
            sourceWidth, sourceHeight, fps,
            areaWidth = area.safeWidth,
            areaHeight = area.safeHeight,
            areaX = area.safeX,
            areaY = area.safeY,
            drawOutsideSafeArea = area.drawUiOutsideSafeArea,
            needAdapterScreen = area.needAdapterScreen,
        )
    }

    /**
     * Asks CarLife to change its encoder frame rate.
     *
     * CarLife derives 20 fps for this size on its own, so a session that never
     * asks is stuck at 20 no matter what the car declared. The ceiling is not
     * this function's business: the car states it in `/info` as `maxFPS`, and
     * that value is what arrives here as [fps]. This head unit says 30, which is
     * also what a real iPhone gets, so 60 is not reachable on this firmware —
     * but hard-coding 30 would make the app silently wrong on a car that does
     * advertise more.
     */
    fun changeFrameRate(rate: Int) {
        // A sanity floor only. Whether the phone accepts the value is its own
        // decision and is reported back in FRAME_RATE_CHANGE_DONE.
        if (rate < 3) return
        sendCommand(
            CarLifeProtocol.VIDEO_ENCODER_FRAME_RATE_CHANGE,
            CarLifeProto.Writer().int32(1, rate).toByteArray(),
        )
        log("已请求 CarLife 帧率 ${rate}fps（车机声明上限 ${fps}fps）")
    }

    /**
     * Re-asks for the car's rate a bounded number of times.
     *
     * A rebuild can land at 20 fps and then ignore the first request — the log
     * shows `帧率已变更：30fps` from the phone while the encoder kept producing
     * 20. Retry even after that acknowledgement; stop at the attempt limit or
     * when a newer encoder generation replaces this one.
     */
    private fun requestFrameRate(rate: Int, attempt: Int, generation: Long) {
        changeFrameRate(rate)
        if (attempt + 1 >= FRAME_RATE_ATTEMPTS) return
        Thread({
            Thread.sleep(FRAME_RATE_RETRY_DELAY_MS)
            if (running.get() && encoderRateGeneration == generation) {
                requestFrameRate(rate, attempt + 1, generation)
            }
        }, "carlife-frame-rate").apply { isDaemon = true }.start()
    }

    @Synchronized
    private fun sendTouch(serviceType: Int, payload: ByteArray) {
        val socket = touchSocket
        val out = socket?.getOutputStream()
        if (out == null) {
            if (!touchUnavailableLogged) {
                touchUnavailableLogged = true
                log("触控发送跳过：CarLife touch socket 尚未连接")
            }
            return
        }
        try {
            out.write(CarLifeProtocol.header(CarLifeProtocol.CHANNEL_TOUCH, serviceType, payload.size, 0))
            out.write(payload)
            out.flush()
            val sent = ++touchPacketsSent
            if (sent == 1L || sent % 50L == 0L) {
                log(
                    "触控已写出 #$sent：service=0x${serviceType.toString(16)} " +
                        "payload=${payload.size}B connected=${socket.isConnected} closed=${socket.isClosed}",
                )
            }
        } catch (error: Exception) {
            log("触控发送失败：${error.message}")
        }
    }

    private companion object {
        /** CarLife's own config keys, as read by FeatureConfigManager. */
        const val FEATURE_FOCUS_UI = "FOCUS_UI"
        const val FEATURE_DISABLE_RESOLUTION_OPTION = "DISABLE_RESOLUTION_OPTION"
        const val FEATURE_DISABLE_VOICE_COMMAND = "DISABLE_VOICE_COMMAND"
        const val FEATURE_CONTENT_ENCRYPTION = "CONTENT_ENCRYPTION"
        /** 1 asks CarLife for 48 kHz PCM; the default 0 is 44.1 kHz. */
        const val FEATURE_MEDIA_SAMPLE_RATE = "MEDIA_SAMPLE_RATE"
        /**
         * 1 makes CarLife record from the phone's own microphone.
         *
         * Without it CarLife's `VrManager.canVehicleRecord()` returns true and
         * it sends MSG_CMD_MIC_RECORD_RECOG_START to the head unit, then waits
         * for microphone audio that a Wi-Fi sender never provides — so the
         * car's voice button produced no response at all. Setting module 6 to 1
         * makes `isUsePhoneMic()` true, which flips canVehicleRecord() to false
         * and lets CarLife run recognition locally against the phone mic.
         */
        const val FEATURE_VOICE_MIC = "VOICE_MIC"

        /** How long to keep retrying a port while CarLife finishes starting. */
        const val CONNECT_RETRY_WINDOW_MS = 25_000L
        const val AUX_CONNECT_WINDOW_MS = 8_000L
        const val CONNECT_RETRY_INTERVAL_MS = 400L

        /**
         * How many times a rebuild may re-ask for the car's frame rate.
         *
         * CarLife sometimes accepts the request and keeps encoding at 20, so a
         * single request is not reliable. Three tries covers the observed
         * behaviour without becoming a permanent poll.
         */
        const val FRAME_RATE_ATTEMPTS = 3
        const val FRAME_RATE_RETRY_DELAY_MS = 1_200L
    }

    override fun close() {
        synchronized(lifecycleLock) {
            if (retired.getAndSet(true)) return
            running.set(false)
        }
        encoderLifecycle.reset()
        runCatching { cmdSocket?.close() }
        auxSockets.forEach { runCatching { it.close() } }
        auxSockets.clear()
        threads.forEach { it.interrupt() }
        threads.clear()
        failure?.let { log("CarLife 会话结束：${it.message}") }
    }
}
