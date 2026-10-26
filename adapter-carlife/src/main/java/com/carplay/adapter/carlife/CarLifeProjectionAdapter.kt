package com.carplay.adapter.carlife

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.carplay.adapter.PhoneProjectionAdapter
import com.carplay.adapter.ProjectionCapabilities
import com.carplay.adapter.ProjectionGrant
import com.carplay.adapter.ProjectionObserver
import com.carplay.sender.VideoPackets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.ArrayDeque
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.ConcurrentHashMap

/**
 * Streams the phone's own CarLife car-mode UI to the car.
 *
 * The phone runs CarLife as the head unit would see it: this adapter plays the
 * head-unit side of the CarLife protocol, so CarLife renders its real car
 * interface and encodes it to H.264 itself. Those frames are already in the
 * format AirPlay wants, so nothing is re-encoded here — unlike the screen-capture
 * adapters, which have to run a MediaCodec encoder of their own.
 */
class CarLifeProjectionAdapter : PhoneProjectionAdapter {

    override val name: String = "CarLife 车机模式"

    /** CarLife encodes its own car-mode UI, so no screen capture consent is involved. */
    override val requiresScreenGrant: Boolean = false

    override val capabilities: ProjectionCapabilities
        get() = ProjectionCapabilities(
            video = true,
            audio = true,
            input = true,
            microphone = false,
            detail = "由手机自带 CarLife 渲染车机界面并直接输出 H.264，无需录屏授权"
        )

    private var observer: ProjectionObserver? = null
    @Volatile private var session: CarLifeSession? = null
    @Volatile private var mediaPublisher: CarLifeMediaPublisher? = null
    /**
     * App-side media fallback. MediaSessionManager can expose playback state
     * while notification access still hides title/artist/artwork.
     */
    @Volatile
    var mediaSnapshotProvider: (() -> CarLifeMediaSnapshot?)? = null
    private val started = AtomicBoolean(false)

    /**
     * The context CarLife was started with, kept so it can be shut down again.
     *
     * [stopProjection] only receives no arguments, and releasing CarLife needs
     * the exact component this app started and its package manager. Holding the
     * application context (never an activity) for the lifetime of one session is
     * what makes that possible; it is cleared once released so a second session
     * cannot act on a stale one.
     */
    @Volatile private var hostContext: Context? = null
    @Volatile private var lifecycleContext: Context? = null
    private val lifecycleLock = Any()
    private var lifecycleGeneration = 0L
    @Volatile private var hostRetryThread: Thread? = null
    private var hostRetryGeneration = 0L
    private var lastHostStartAt = 0L

    /** True once this session has taken the CarLife binding back from Honor. */
    private var hostDetached = false
    private var lastRetiredAt = 0L

    /**
     * Panel and safe area to hand CarLife, or null for the bare rectangle.
     *
     * Set before [prepare]. CarLink composes the full-panel wallpaper; CarLife
     * keeps its controls inside the supplied safe area.
     */
    @Volatile
    private var _panel: CarLifePanel? = null
    var panel: CarLifePanel?
        get() = _panel
        set(value) {
            val old = _panel
            _panel = value
            if (old != value && value != null) {
                observer?.diagnostic(
                    "安全区初始化：panel=${value.panelWidth}x${value.panelHeight}, " +
                        "safe=@(${value.safeX},${value.safeY})/${value.safeWidth}x${value.safeHeight}, " +
                        "drawUiOutside=${value.drawUiOutsideSafeArea}, needAdapterScreen=${value.needAdapterScreen}",
                )
            }
        }

    /**
     * Safe area policy from settings (STRICT/RELAXED/CUSTOM/OFF).
     * Controls whether CarLife source encodes at full panel or safe-area size.
     */
    @Volatile
    var safeAreaPolicy: Any? = null

    @Volatile
    var videoProcessor: CarLifeVideoProcessor? = null

    @Volatile private var frameSink: ((ByteArray, Long, Boolean) -> Unit)? = null
    @Volatile private var ptsBaseSeconds = -1L
    @Volatile private var ptsCounter = 0L
    private val keyFrameGate = KeyFrameRebuildGate()
    private val videoAssembler = CarLifeVideoAssembler()
    /** Serializes encoder reset with assembly and delivery of one video batch. */
    private val videoStateLock = Any()
    /**
     * Identifies the source and encoder generation that owns the current video
     * callback. A late frame from a previous CarLife session must not be sent
     * to the new AirPlay stream or release the new generation's key-frame gate.
     */
    private var videoEpoch = 0L
    /** Source-side rebuild round; paired with AirPlaySender's recovery round in diagnostics. */
    private var recoveryRound = 0L
    private var encoderGeneration = 0L
    /** Last presentation time handed to AirPlay; keeps the timeline monotonic across encoder rebuilds. */
    @Volatile private var lastPts = 0L
    @Volatile private var streamFps = 30
    @Volatile private var sourceWidth = 0
    @Volatile private var sourceHeight = 0
    private val activeTouchPoints = ConcurrentHashMap<Int, Pair<Float, Float>>()
    private var touchMappingLogged = 0
    private var bridgeTouchLogged = 0
    /** True once the direct CarLife media socket has supplied audio. */
    private val mediaDataSeen = AtomicBoolean(false)

    /**
     * CarLife is present if its package is installed.
     *
     * It cannot be probed with getLaunchIntentForPackage(): this build ships no
     * standard LAUNCHER entry point, only a private fake category, so that call
     * returns null even when the app is installed and runnable.
     */
    override fun isSupported(context: Context): Boolean = runCatching {
        context.packageManager.getPackageInfo(CARLIFE_PACKAGE, 0)
        true
    }.getOrDefault(false)

    /**
     * Starts CarLife before the Bluetooth handshake begins.
     *
     * CarLife opens its seven listening ports from `CarLifeApplication.onCreate`
     * (`initConnectModule` → `ConnectClient.startTcpListener`), so merely getting
     * its process to exist is enough — this is what the probe did, and it
     * captured a full H.264 stream without ever showing CarLife's activity.
     *
     * Match the known-good 1.2.7 behavior: start CarLife's connect service with
     * the host fields attached, but do not bind to that service from this app.
     * CarLife owns the vendor-host binding; an extra client binding changes its
     * service lifetime and did not improve prewarming on the Honor build.
     */
    override fun warmUp(context: Context) {
        lifecycleContext = context.applicationContext
        logCarLifeInstallState(context, "预热")
        retireCarLifeProcess(context, "预热前清理旧进程")
        startCarLifeHost(context, "预热")
    }

    private fun connectIntent(context: Context) = Intent().apply {
        setClassName(CARLIFE_PACKAGE, CARLIFE_CONNECT_SERVICE)
        putExtra("bindPackageName", context.packageName)
        putExtra("bindClassName", CarLifeHostService::class.java.name)
        putExtra("bindVersion", "1.0")
        putExtra("usb_type", 0)
        putExtra("manufacturer", "honor")
        putExtra("is_box", false)
    }

    /**
     * Starts CarLife's process without asking it to bind this app as host.
     *
     * CarLife's CarlifeConnectService only runs its bind path when the intent
     * carries `bindPackageName`:
     *
     *     if (!intent.hasExtra("bindPackageName") || !intent.hasExtra("usb_type")) { log only }
     *     else { if (getMixCastManager() == null) bindManufacturerService(intent); ... }
     *
     * Without those extras the process still comes up and raises its own
     * "组件正在运行" notification, but it never takes the host binding away from
     * Honor's launcher — which is the side that draws the car desktop. That is
     * exactly the silent start the driver remembers: the status bar flickers
     * once and the session connects by itself.
     */
    private fun wakeIntent() = Intent().apply {
        setClassName(CARLIFE_PACKAGE, CARLIFE_CONNECT_SERVICE)
        putExtra("usb_type", 0)
    }

    /**
     * Starts CarLife using the 1.2.7 startService-only lifecycle.
     *
     * This deliberately verifies the vendor component before calling
     * startService.  A stopped/disabled or partially installed CarLife build
     * otherwise fails as a harmless-looking "no component returned", leaving
     * the TCP side to time out with no explanation.
     */
    private fun startCarLifeHost(context: Context, reason: String): Boolean {
        /*
         * If CarLife is already running, leave it alone.
         *
         * On this phone Honor's launcher (com.hihonor.auto) is what draws the
         * car desktop: it becomes CarLife's host, renders its UI into the
         * surface CarLife offers, and CarLife encodes the result. When the
         * launcher is already driving that, starting CarLife ourselves only
         * competes for the same binding — and whoever owns the binding owns the
         * surface, so taking it means the picture goes black because this app
         * has no car UI to draw into it.
         *
         * A running vendor process is therefore left untouched and the session
         * takes CarLife's H.264 stream from its ports instead.
         */
        if (isCarLifeRunning()) {
            observer?.diagnostic("$reason：CarLife 已在运行，直接取流，不再干预其宿主绑定")
            return true
        }
        synchronized(lifecycleLock) {
            val now = android.os.SystemClock.uptimeMillis()
            if (now - lastHostStartAt < HOST_START_DEBOUNCE_MS) {
                observer?.diagnostic("$reason：跳过重复启动请求，距离上次启动 ${now - lastHostStartAt}ms")
                return true
            }
            lastHostStartAt = now
        }
        val intent = wakeIntent()
        val resolved = runCatching {
            context.packageManager.resolveService(
                intent,
                PackageManager.MATCH_ALL,
            )
        }.getOrNull()
        if (resolved == null) {
            observer?.diagnostic(
                "$reason：CarLife 服务不可解析：${CARLIFE_PACKAGE}/$CARLIFE_CONNECT_SERVICE；" +
                    "请确认 Honor CarLife+ 组件完整安装且未被系统停用",
            )
            return false
        }
        /*
         * Bring CarLife's process up WITHOUT asking it to bind this app.
         *
         * CarLife binds to a host exactly once:
         *
         *     if (MixPhoneStrategy.getMixCastManager() == null) bindManufacturerService(intent);
         *
         * and com.hihonor.auto is the host that matters: it renders the car
         * desktop into the Surface CarLife offers, and CarLife only encodes the
         * result. That division is what the driver wants; handing CarLife a
         * `bindPackageName` of this app makes it bind THIS app instead, and a
         * host with no UI to draw produces exactly the black frames seen in
         * 1.2.66 and 1.2.71 (`prepareCast` arrives, then 480-byte frames).
         *
         * Killing or retiring the vendor process to win the binding back is not
         * an option either: com.hihonor.nearby marks the package stopped the
         * moment it dies, and Android then refuses every start this app issues.
         *
         * So the start below carries no bind extras. The process comes up, its
         * seven TCP ports open, and whichever host already has a UI (Honor's
         * launcher) keeps drawing the desktop. This app only reads the stream.
         */
        /*
         * CarLife 8.x is a vendor service and normally accepts a plain
         * startService even while this app is in the foreground. Android 11+
         * can reject that call when the request arrives from a background
         * Bluetooth receiver. Retry once through the foreground-service API so
         * the failure is visible and the vendor process gets a chance to come
         * up.
         */
        val result = runCatching { context.startService(intent) }
            .recoverCatching { failure ->
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O &&
                    failure is IllegalStateException
                ) {
                    observer?.diagnostic("$reason：普通启动被系统后台限制，改用前台服务启动 CarLife")
                    context.startForegroundService(intent)
                } else {
                    throw failure
                }
            }
            .onFailure {
                observer?.diagnostic(
                    "$reason：启动 CarLife 失败：${it.javaClass.simpleName}: ${it.message}; " +
                        "component=${resolved.serviceInfo?.packageName}/${resolved.serviceInfo?.name}",
                )
            }
        if (result.isSuccess) {
            val component = result.getOrNull()
            if (component != null) {
                observer?.diagnostic(
                    "$reason：已请求启动 CarLife 服务（${component.flattenToShortString()}）；" +
                        "未携带绑定参数，桌面继续由荣耀车联绘制",
                )
            } else {
                /*
                 * A null component means the package is in the "stopped" state,
                 * and this app deliberately does not fight it.
                 *
                 * com.hihonor.nearby marks CarLife stopped whenever its own car
                 * session ends, and Android then refuses every start a
                 * third-party app issues. Launching CarLife's activity does
                 * clear that state — but it also makes CarLife bind to THIS app
                 * as its host, and the surface CarLife then offers is one the
                 * host is expected to draw into. This app has no car UI, so the
                 * picture goes black. Every session that showed the real car
                 * desktop had Honor's launcher as the host; every black one had
                 * this app as the host.
                 *
                 * So the launcher stays the only thing that starts CarLife.
                 * This session waits for it and only takes the H.264 stream.
                 */
                observer?.diagnostic(
                    "$reason：CarLife 未运行且被系统停用，尝试不带绑定参数静默启动",
                )
                val woken = runCatching { context.startService(wakeIntent()) }
                if (woken.isSuccess) {
                    observer?.diagnostic(
                        "$reason：已静默启动 CarLife（不请求宿主绑定，桌面仍由荣耀车联绘制）",
                    )
                    return true
                }
                observer?.diagnostic(
                    "$reason：静默启动被拒绝；请在荣耀车联里完成一次连接",
                )
                return false
            }
        }
        return result.isSuccess
    }

    /**
     * Starts CarLife's activity once, clearing the package's stopped state.
     *
     * Deliberately a single launch per start attempt: repeating it would fight
     * Honor's launcher, which is what stopped the package in the first place.
     */
    private fun wakeCarLifeActivity(context: Context) {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            setClassName(CARLIFE_PACKAGE, CARLIFE_ACTIVITY)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        if (context.packageManager.resolveActivity(intent, PackageManager.MATCH_ALL) == null) {
            observer?.diagnostic("CarLife 主界面不可解析：${CARLIFE_PACKAGE}/$CARLIFE_ACTIVITY")
            return
        }
        runCatching { context.startActivity(intent) }
            .onSuccess { observer?.diagnostic("已启动 CarLife 主界面，等待其自行绑定宿主") }
            .onFailure {
                observer?.diagnostic("启动 CarLife 主界面失败：${it.javaClass.simpleName}: ${it.message}")
            }
    }

    override fun setObserver(observer: ProjectionObserver) { this.observer = observer }

    override fun setGrant(grant: ProjectionGrant) = Unit

    /** Records the media format the head unit accepted. */
    override fun configureAudio(sampleRate: Int, channels: Int, pcm: Boolean) {
        if (sampleRate in listOf(44_100, 48_000)) negotiatedSampleRate = sampleRate
        negotiatedChannels = if (channels == 1) 1 else 2
        // When the car took LPCM there is no encoder at all: CarLife's own PCM
        // is handed over as-is, which is also what removed the resampling step
        // that used to run before AAC encoding.
        negotiatedPcm = pcm
        // The media reader must never race a codec release.  The audio worker
        // notices this generation change and resets its codec on its own thread.
        synchronized(audioQueueLock) {
            audioConfigGeneration++
            audioQueueLock.notifyAll()
        }
    }

    /**
     * Coalesces duplicate requests until CarLife emits the rebuilt encoder's IDR.
     *
     * A fresh request after that IDR is a new recovery round and must not be
     * mistaken for a delayed duplicate of the completed round.
     */
    override fun requestKeyFrame() {
        requestCarLifeKeyFrame(retry = false)
    }

    /** A requestUI is a new visible attempt; retry even if the last rebuild is stuck. */
    override fun requestScreenRestoreKeyFrame() {
        requestCarLifeKeyFrame(retry = true)
    }

    /**
     * Requests one distinct decoder handover, or a retry after the car has
     * explicitly asked for the UI again.
     */
    private fun requestCarLifeKeyFrame(retry: Boolean) {
        /*
         * Fast path: make the compositor's own encoder emit an IDR.
         *
         * The receiver asks for a key frame after every decoder rebuild and
         * every screen handover. Rebuilding CarLife's encoder to satisfy that
         * took 3.3 seconds on the measured session — the car had already
         * dropped the picture and, in one capture, reset the connection before
         * the new IDR existed. With a compositor in the path there is nothing
         * to rebuild: the source stream is unchanged, only the output needs a
         * sync frame, and that arrives within a frame or two.
         */
        val processor = videoProcessor
        if (processor != null && processor.requestOutputKeyFrame()) {
            synchronized(videoStateLock) { recoveryRound++ }
            observer?.diagnostic(
                "已请求合成器输出关键帧（不重建 CarLife 编码器），恢复轮次 #$recoveryRound",
            )
            return
        }
        // Serialize the gate transition, assembler reset, and frame delivery.
        // Otherwise an old callback can observe the new in-flight gate and
        // release it before the rebuilt encoder's IDR arrives.
        synchronized(videoStateLock) {
            val accepted = if (retry) keyFrameGate.beginRetry() else keyFrameGate.request()
            if (!accepted) {
                observer?.diagnostic("CarLife 关键帧请求已合并（上一轮仍在等待 IDR）")
                return
            }
            recoveryRound++
            observer?.diagnostic(
                "CarLife 恢复轮次 #$recoveryRound：${if (retry) "屏幕恢复重试" else "关键帧请求"}，" +
                    "重置视频组装器并等待 VIDEO_ENCODER_INIT_DONE",
            )
            // Rebuilding the encoder is the only path that produces a new IDR
            // here, and a rebuilt encoder emits its own SPS/PPS. The next
            // access unit must therefore be re-inspected for the parameter
            // sets, or the car would keep decoding against stale SPS.
            videoAssembler.reset(preserveConfig = true)
        }
        val live = session
        if (live == null) {
            synchronized(videoStateLock) { keyFrameGate.reset() }
            observer?.diagnostic("关键帧请求被忽略：CarLife 会话尚未启动")
            return
        }
        runCatching {
            if (live.requestKeyFrame()) {
                observer?.diagnostic("已发送 CarLife VIDEO_ENCODER_INIT，等待新的 SPS/PPS 和 IDR")
            } else {
                synchronized(videoStateLock) { keyFrameGate.reset() }
                observer?.diagnostic("CarLife 尚未就绪或恢复请求已合并；本轮不再重建编码器")
            }
        }
            .onFailure {
                synchronized(videoStateLock) { keyFrameGate.reset() }
                observer?.diagnostic("请求 CarLife 重建编码器失败：${it.message}")
            }
    }

    override fun deliverMicrophone(pcm: ByteArray, sampleRate: Int, channels: Int): Boolean = false

    override suspend fun prepare(context: Context, width: Int, height: Int, fps: Int): Boolean {
        require(width > 0 && height > 0 && fps > 0)
        preparedWidth = width
        preparedHeight = height
        preparedFps = fps
        sourceWidth = 0
        sourceHeight = 0
        streamFps = fps
        bridgeTouchLogged = 0
        mediaDataSeen.set(false)
        // Kept so [stopProjection] can shut CarLife down again. An application
        // context is held, never the caller's activity, so nothing leaks.
        hostContext = context.applicationContext
        lifecycleContext = context.applicationContext
        retireCarLifeProcess(context, "准备新会话")

        // Set the bridge sink before asking CarLife to bind, so the bind
        // confirmation and anything CarLife reports shows up in the log. The
        // negotiated size goes in now because CarLife asks for it the moment it
        // starts building its encoder, which happens before startProjection.
        CarLifeBridge.clear()
        CarLifeBridge.log = { observer?.diagnostic(it) }
        CarLifeBridge.width = width
        CarLifeBridge.height = height
        CarLifeBridge.fps = fps
        // Honor CarLife's MixAuthorization asks the host for a device id six
        // seconds in; an empty answer makes the phone abort the session with
        // "车机未授权，即将退出". Derive a stable id for this installation.
        val hostId = stableHostId(context)
        preparedHostId = hostId
        hostDetached = false
        CarLifeBridge.deviceId = hostId
        CarLifeBridge.deviceVCode = android.provider.Settings.Secure.getString(
            context.contentResolver, android.provider.Settings.Secure.ANDROID_ID
        ).orEmpty()

        // No activity is started here. CarLife renders its car UI off-screen in
        // its own virtual display, so nothing needs to be in front; launching
        // CarlifeActivity would instead trigger its onConnectChange(1) finish()
        // path and disturb the renderer. notifyHost below starts CarLife's
        // process through its connect service.

        // Hand CarLife this host's identity. Its connect service registers the
        // message handlers that answer the handshake, and it only starts
        // listening for a session once it has been told which host to bind.
        //
        // This must wait for CarLife to come up. A fresh launch takes several
        // seconds, and a bind request delivered before its process exists is
        // simply dropped, leaving CarLife silent for the whole handshake.
        notifyHost(context)

        preparedAddress = localAddress(context)
        observer?.diagnostic(
            "CarLife 车机模式：连接 $preparedAddress:${CarLifeProtocol.PORT_CMD}，" +
                "AirPlay 画布 ${width}x$height@$fps；CarLife 编码尺寸由手机决定",
        )
        return true
    }

    override suspend fun startProjection(onFrameEncoded: (ByteArray, Long, Boolean) -> Unit) {
        val generation = synchronized(lifecycleLock) {
            if (!started.compareAndSet(false, true)) {
                observer?.diagnostic("CarLife 投屏重复启动请求，沿用当前会话")
                return
            }
            lifecycleGeneration++
            lifecycleGeneration
        }
        val observer = observer
        val address = preparedAddress ?: error("CarLife 尚未准备就绪")
        streamFps = preparedFps
        synchronized(videoStateLock) {
            videoEpoch++
            recoveryRound = 0L
            encoderGeneration = 0L
            frameSink = onFrameEncoded
            videoAssembler.reset()
            keyFrameGate.reset()
        }
        videoProcessor?.let { processor ->
            val failure = runCatching {
                processor.start(
                    context = checkNotNull(hostContext),
                    outputWidth = preparedWidth,
                    outputHeight = preparedHeight,
                    outputFps = preparedFps,
                    onConfig = { observer?.videoConfig(it) },
                    onFrame = { bytes, ptsUs, keyFrame -> frameSink?.invoke(bytes, ptsUs, keyFrame) },
                    onDiagnostic = { observer?.diagnostic(it) },
                    onFailure = { error ->
                        Thread({ observer?.failed(error) }, "carlink-composer-failure").apply {
                            isDaemon = true
                            start()
                        }
                    },
                    onNeedKeyFrame = { requestKeyFrame() },
                )
            }.exceptionOrNull()
            failure?.let { error ->
                val text = error.message.orEmpty()
                /*
                 * A duplicate start is a lifecycle race, not an EGL/codec
                 * failure.  The compositor is already consuming frames in
                 * that case; tearing it down and falling back to raw CarLife
                 * is exactly what made the old field builds lose the
                 * wallpaper and revert to the opaque full-panel stream.
                 */
                if (text.contains("已经启动") || text.contains("重复启动") ||
                    text.contains("already started", ignoreCase = true)
                ) {
                    observer?.diagnostic("全画布合成器已在运行，忽略重复启动，不回退原始 CarLife 视频")
                } else {
                    observer?.diagnostic(
                        "全画布合成器启动失败：${error.javaClass.simpleName}: " +
                            (error.message ?: "(无消息)"),
                    )
                    /*
                     * EGL/MediaCodec allocation can still fail on a phone.
                     * Keep the native CarLife path alive in that case so the
                     * user gets a diagnostic session instead of a hard link
                     * failure.
                     */
                    observer?.diagnostic(
                        "全画布合成器不可用，回退到 CarLife 原始视频：${error.javaClass.simpleName}: " +
                            (error.message ?: "(无消息)"),
                    )
                    runCatching { processor.stop() }
                    videoProcessor = null
                }
            }
        }
        if (!isLifecycleActive(generation)) {
            runCatching { videoProcessor?.stop() }
            return
        }
        activeTouchPoints.clear()
        touchMappingLogged = 0
        val sourceEpoch = videoEpoch

        val live = CarLifeSession(
            host = address,
            // The head unit's display negotiation is the source of truth.
            // Keeping a probe-era fallback here is useful only when CarLife is
            // started outside an AirPlay session; during a real session the
            // negotiated panel must flow into the first encoder and every
            // VIDEO_ENCODER_INIT rebuild.
            width = preparedWidth.takeIf { it > 0 } ?: CARLIFE_ENCODER_WIDTH,
            height = preparedHeight.takeIf { it > 0 } ?: CARLIFE_ENCODER_HEIGHT,
            fps = preparedFps.takeIf { it > 0 } ?: CARLIFE_ENCODER_FPS,
            log = { observer?.diagnostic(it) },
            hostId = preparedHostId,
            panel = panel,
            onEncoderReady = { width, height, fps ->
                streamFps = fps
                synchronized(videoStateLock) {
                    encoderGeneration++
                }
                val generation = synchronized(videoStateLock) { encoderGeneration }
                synchronized(videoStateLock) {
                    if (sourceEpoch == videoEpoch) {
                        sourceWidth = width
                        sourceHeight = height
                    }
                }
                observer?.diagnostic(
                    "CarLife 编码器 generation=$generation 就绪：${width}x$height@$fps；" +
                        "AirPlay 合成画布 ${preparedWidth}x${preparedHeight}" +
                        if (recoveryRound > 0) "（恢复轮次 #$recoveryRound）" else "",
                )
                // No activity launch here on purpose: CarLife renders its car UI
                // off-screen in its own virtual display and encodes it without
                // any activity being in front (the probe captured a full H.264
                // stream that way). Starting CarlifeActivity actually makes it
                // finish itself from onConnectChange(1) and disturbs rendering.
            },
            onVideoFrame = { data, timestampSeconds, keyFrame ->
                synchronized(videoStateLock) {
                    if (sourceEpoch != videoEpoch) return@synchronized
                    val sink = frameSink ?: return@synchronized
                    val batch = videoAssembler.offer(data, timestampSeconds, keyFrame)
                        ?: return@synchronized
                    batch.avcC?.let {
                        observer?.diagnostic(
                            "CarLife ${if (batch.reusedAvcC) "复用上一轮 SPS/PPS" else "新 SPS/PPS"} 已组装：${it.size}B，" +
                                "encoder generation=$encoderGeneration，恢复轮次 #$recoveryRound",
                        )
                        val processor = videoProcessor
                        if (processor == null) {
                            observer?.videoConfig(it)
                        } else {
                            processor.configureInput(
                                sourceWidth.takeIf { width -> width > 0 } ?: CARLIFE_ENCODER_WIDTH,
                                sourceHeight.takeIf { height -> height > 0 } ?: CARLIFE_ENCODER_HEIGHT,
                                streamFps,
                                it,
                            )
                        }
                    }
                    batch.frames.forEach { frame ->
                        if (frame.keyFrame) {
                            observer?.diagnostic(
                                "CarLife 新 IDR 已到达：${frame.data.size}B，" +
                                    "encoder generation=$encoderGeneration，恢复轮次 #$recoveryRound",
                            )
                        }
                        val ptsUs = presentationTime(frame.timestampSeconds)
                        val processor = videoProcessor
                        if (processor == null) {
                            sink(VideoPackets.lengthPrefixed(frame.data), ptsUs, frame.keyFrame)
                        } else {
                            processor.offerFrame(frame.data, ptsUs, frame.keyFrame)
                        }
                        if (frame.keyFrame) keyFrameGate.observeFrame(true)
                    }
                }
            },
            onAudioInit = { sampleRate, channels, _ ->
                synchronized(audioQueueLock) {
                    audioSampleRate = sampleRate.takeIf { it in 8_000..96_000 } ?: 48_000
                    audioChannels = if (channels == 1) 1 else 2
                    audioConfigGeneration++
                    audioQueue.clear()
                    audioQueueLock.notifyAll()
                }
                // Say which path the samples actually take. The old wording
                // always claimed AAC, so a journal full of "转为 AAC" sat next
                // to packets that were really raw PCM — the one line that could
                // have told the two apart was wrong.
                if (negotiatedPcm) {
                    observer?.diagnostic(
                        "CarLife 音频：${audioSampleRate}Hz / ${audioChannels} 声道，LPCM 直通" +
                            if (audioSampleRate == negotiatedSampleRate) ""
                            else "（重采样到 ${negotiatedSampleRate}Hz）",
                    )
                } else {
                    observer?.diagnostic(
                        "CarLife 音频：${audioSampleRate}Hz / ${audioChannels} 声道，转为 AAC " +
                            "${negotiatedSampleRate}Hz",
                    )
                }
            },
            onAudioData = {
                mediaDataSeen.set(true)
                enqueueAudio(it, observer)
            },
            onAudioEnded = {
                synchronized(audioQueueLock) {
                    audioQueue.clear()
                    audioConfigGeneration++
                    audioQueueLock.notifyAll()
                }
                observer?.diagnostic("CarLife 音频来源已结束：清空音频队列，保留视频/触控会话")
            },
            onTtsInit = { sampleRate, channels, format ->
                observer?.diagnostic("CarLife TTS 输入：${sampleRate}Hz/${channels}ch 格式=$format（暂不混入媒体）")
            },
            onTtsData = { data ->
                observer?.diagnostic("CarLife TTS 数据：${data.size}B（独立提示音来源，媒体流不受影响）")
            },
            onFailure = { error ->
                observer?.failed(error)
            },
            container = this,
        )
        val publisher = CarLifeMediaPublisher(
            checkNotNull(hostContext),
            log = { message -> observer?.diagnostic(message) },
            fallback = mediaSnapshotProvider,
        )
        synchronized(lifecycleLock) {
            if (!started.get() || lifecycleGeneration != generation) {
                abortStart(generation, live, publisher)
                return
            }
            session = live
            mediaPublisher = publisher
            CarLifeBridge.stop = {
                observer?.diagnostic("CarLife 请求停止投屏；开始释放 AirPlay/音频会话")
                observer?.failed(IllegalStateException("CarLife 请求停止投屏"))
            }

            // CarLife pushes touch and key events at the host service rather than
            // through the socket, so route them onto the session here.
            CarLifeBridge.touch = { action, x, y ->
                handleBridgeTouch(live, action, x, y, observer)
            }
            CarLifeBridge.key = lambda@{ carLifeCode, down ->
                if (!down) {
                    return@lambda  // Only handle key-down
                }
                // Convert CarLife code back to Android KeyEvent code and route to MediaSession
                val androidCode = CarLifeHardKey.toAndroid(carLifeCode)
                if (androidCode != null) {
                    // Emit diagnostic with Android KeyEvent code, routing will be handled
                    // by the Dispatch layer in the app module
                    observer?.diagnostic(
                        "车机硬键已转换：CarLife code=$carLifeCode → Android KeyCode=$androidCode"
                    )
                }
            }
            CarLifeBridge.audio = { pcm, sampleRate ->
                handleBridgeAudio(pcm, sampleRate, observer)
            }
            publisher.start()
        }
        if (!isLifecycleActive(generation)) {
            abortStart(generation, live, publisher)
            return
        }
        startAudioWorker(observer)
        if (!isLifecycleActive(generation)) {
            abortStart(generation, live, publisher)
            return
        }
        try {
            live.connect()
        } catch (failure: Throwable) {
            abortStart(generation, live, publisher)
            throw failure
        }
    }

    private fun abortStart(
        generation: Long,
        live: CarLifeSession,
        publisher: CarLifeMediaPublisher?,
    ) {
        synchronized(lifecycleLock) {
            if (lifecycleGeneration == generation) {
                lifecycleGeneration++
                started.set(false)
                if (session === live) session = null
                if (publisher != null && mediaPublisher === publisher) mediaPublisher = null
                CarLifeBridge.clear()
            }
        }
        runCatching { audioWorker?.interrupt() }
        runCatching { publisher?.close() }
        runCatching { live.close() }
    }

    private fun isLifecycleActive(generation: Long): Boolean =
        synchronized(lifecycleLock) {
            started.get() && lifecycleGeneration == generation
        }

    private fun handleBridgeTouch(
        live: CarLifeSession,
        action: Int,
        x: Float,
        y: Float,
        observer: ProjectionObserver?,
    ) {
        val targetWidth = sourceWidth.takeIf { it > 0 } ?: preparedWidth
        val targetHeight = sourceHeight.takeIf { it > 0 } ?: preparedHeight
        val virtualWidth = CarLifeBridge.castWidth.takeIf { it > 0 } ?: targetWidth
        val virtualHeight = CarLifeBridge.castHeight.takeIf { it > 0 } ?: targetHeight
        val geometry = panel

        // Step 1: Virtual coordinate → Panel coordinate (using safe area mapping if available)
        val panelPoint = geometry?.mapVirtualTouchToPanel(x, y, virtualWidth, virtualHeight)
        val panelX = panelPoint?.first ?: x * targetWidth / virtualWidth
        val panelY = panelPoint?.second ?: y * targetHeight / virtualHeight
        val usedSafeAreaMapping = panelPoint != null

        // Step 2: Panel coordinate → Source coordinate (using video processor or panel geometry)
        val sourcePoint = if (videoProcessor != null) {
            videoProcessor?.mapTouchToSource(panelX, panelY)
        } else {
            geometry?.mapPanelTouchToSource(panelX, panelY, targetWidth, targetHeight)
        }

        // Step 3: Validate against safe area constraints
        if (geometry != null && sourcePoint == null) {
            observer?.diagnostic(
                "CarLife 触控安全区验证：panel=(${"%.1f".format(panelX)}," +
                    "${"%.1f".format(panelY)}) 落在安全区外 " +
                    "[safe=${geometry.safeX},${geometry.safeY},${geometry.safeWidth}x${geometry.safeHeight}]，已忽略",
            )
            return
        }

        // Step 4: Final clamping to source bounds
        val mappedX = sourcePoint?.first ?: panelX.coerceIn(
            0f,
            (targetWidth - 1).coerceAtLeast(0).toFloat(),
        )
        val mappedY = sourcePoint?.second ?: panelY.coerceIn(
            0f,
            (targetHeight - 1).coerceAtLeast(0).toFloat(),
        )

        // Comprehensive diagnostic logging
        if (bridgeTouchLogged < 16) {
            val panelInfo = if (geometry != null) {
                "panel=${geometry.panelWidth}x${geometry.panelHeight}@(${geometry.safeX},${geometry.safeY}/${geometry.safeWidth}x${geometry.safeHeight}), "
            } else {
                ""
            }
            val processorInfo = if (videoProcessor != null) {
                "通过视频处理器"
            } else if (usedSafeAreaMapping) {
                "通过安全区映射"
            } else {
                "简单缩放"
            }
            observer?.diagnostic(
                "触控映射 #${bridgeTouchLogged++}: [virtual(${"%.0f".format(x)},${"%.0f".format(y)})@${virtualWidth}x$virtualHeight]" +
                    " → [panel(${"%.1f".format(panelX)},${"%.1f".format(panelY)})] " +
                    "→ [source(${"%.0f".format(mappedX)},${"%.0f".format(mappedY)})@${targetWidth}x$targetHeight] " +
                    "${panelInfo}$processorInfo",
            )
        }
        live.touch(action, mappedX.toInt(), mappedY.toInt())
    }

    private fun handleBridgeAudio(
        pcm: ByteArray,
        sampleRate: Int,
        observer: ProjectionObserver?,
    ) {
        if (!mediaDataSeen.get() && pcm.isNotEmpty()) {
            if (sampleRate in 8_000..96_000) {
                synchronized(audioQueueLock) {
                    if (audioSampleRate != sampleRate) {
                        audioSampleRate = sampleRate
                        audioConfigGeneration++
                        audioQueue.clear()
                    }
                    audioQueueLock.notifyAll()
                }
            }
            enqueueAudio(pcm, observer)
        }
    }

    /**
     * CarLife's car-mode audio arrives as raw PCM. It is encoded only when the
     * negotiated AirPlay stream is AAC; GM's advertised type-100 PCM path is
     * handed through without a codec.
     *
     * Output must be **raw AAC access units with no ADTS header**: the type-102
     * stream carries one AU per RTP packet, and an ADTS header in front of it
     * makes the car's decoder read garbage.
     */
    private var audioEncoder: android.media.MediaCodec? = null
    /** True when the car took LPCM, so CarLife's PCM goes out without encoding. */
    @Volatile private var negotiatedPcm = false
    /** The rate the car accepted; [audioSampleRate] is CarLife's own output rate. */
    @Volatile private var negotiatedSampleRate = 48_000
    @Volatile private var negotiatedChannels = 2
    @Volatile private var audioSampleRate = 48_000
    @Volatile private var audioChannels = 2
    private var audioPtsUs = 0L
    private var audioBytesPerFrame = 0
    private var resampler: com.carplay.media.Pcm16Resampler? = null
    private var resamplerInputRate = 0
    private var resamplerOutputRate = 0
    private var resamplerChannels = 0
    private var aacOutputDiagnosticLogged = false
    private val audioQueueLock = Object()
    private val audioQueue = ArrayDeque<ByteArray>()
    private var audioWorker: Thread? = null
    private var audioQueueDrops = 0L
    private var audioConfigGeneration = 0L
    private var audioWorkerGeneration = -1L
    private val audioQueueLimit = 24

    /**
     * CarLife's media reader is a protocol reader.  Keeping MediaCodec work
     * out of that thread is essential: a full input queue or a slow AAC
     * dequeue otherwise stops the reader, which also starves video/control
     * traffic and makes the session look like a display failure.
     */
    private fun enqueueAudio(pcm: ByteArray, observer: ProjectionObserver?) {
        if (pcm.isEmpty() || !started.get()) return
        synchronized(audioQueueLock) {
            if (!started.get()) return
            if (audioQueue.size >= audioQueueLimit) {
                audioQueue.removeFirst()
                audioQueueDrops++
                if (audioQueueDrops == 1L || audioQueueDrops % 100L == 0L) {
                    observer?.diagnostic("CarLife 音频队列丢弃过期块：$audioQueueDrops")
                }
            }
            audioQueue.addLast(pcm.copyOf())
            audioQueueLock.notifyAll()
        }
    }

    private fun startAudioWorker(observer: ProjectionObserver?) {
        check(audioWorker == null) { "CarLife 音频线程已启动" }
        audioWorker = Thread({
            try {
                while (started.get()) {
                    val pcm = synchronized(audioQueueLock) {
                        while (audioQueue.isEmpty() && started.get()) {
                            try {
                                audioQueueLock.wait(250)
                            } catch (_: InterruptedException) {
                                if (!started.get()) return@Thread
                            }
                        }
                        if (!started.get()) null else audioQueue.pollFirst()
                    } ?: continue

                    val generation = synchronized(audioQueueLock) { audioConfigGeneration }
                    if (generation != audioWorkerGeneration) {
                        releaseAudioEncoder()
                        audioPtsUs = 0L
                        audioWorkerGeneration = generation
                    }
                    encodeAudio(pcm, observer)
                }
            } finally {
                synchronized(audioQueueLock) {
                    audioQueue.clear()
                    audioQueueLock.notifyAll()
                }
                releaseAudioEncoder()
                audioWorker = null
            }
        }, "carlife-audio-encode").apply { isDaemon = true }
        audioWorker!!.start()
    }

    private fun encodeAudio(pcm: ByteArray, observer: ProjectionObserver?) {
        try {
            val rate = negotiatedSampleRate
            val sourceRate = audioSampleRate
            if (negotiatedPcm) {
                // Only reached when the car accepted an LPCM stream; on this
                // head unit that is the fallback path, not the normal one.
                val converted = if (sourceRate == rate) pcm else {
                    val active = resampler?.takeIf {
                        resamplerInputRate == sourceRate &&
                            resamplerOutputRate == rate &&
                            resamplerChannels == audioChannels
                    }
                        ?: com.carplay.media.Pcm16Resampler(sourceRate, rate, audioChannels).also {
                            resampler = it
                            resamplerInputRate = sourceRate
                            resamplerOutputRate = rate
                            resamplerChannels = audioChannels
                        }
                    active.convert(pcm)
                }
                observer?.audioFrame(converted, audioPtsUs, pcm = true)
                audioPtsUs += converted.size / (audioChannels * 2) * 1_000_000L / rate
                return
            }
            val converted = if (sourceRate == rate) pcm else {
                val active = resampler?.takeIf {
                    resamplerInputRate == sourceRate &&
                        resamplerOutputRate == rate &&
                        resamplerChannels == audioChannels
                }
                    ?: com.carplay.media.Pcm16Resampler(sourceRate, rate, audioChannels).also {
                        resampler = it
                        resamplerInputRate = sourceRate
                        resamplerOutputRate = rate
                        resamplerChannels = audioChannels
                    }
                active.convert(pcm)
            }
            var encoder = audioEncoder
            if (encoder == null) {
                val format = android.media.MediaFormat.createAudioFormat(
                    android.media.MediaFormat.MIMETYPE_AUDIO_AAC, rate, audioChannels
                ).apply {
                    setInteger(android.media.MediaFormat.KEY_AAC_PROFILE,
                        android.media.MediaCodecInfo.CodecProfileLevel.AACObjectLC)
                    setInteger(android.media.MediaFormat.KEY_BIT_RATE, 128_000)
                    setInteger(android.media.MediaFormat.KEY_MAX_INPUT_SIZE, 64 * 1024)
                }
                encoder = android.media.MediaCodec.createEncoderByType(android.media.MediaFormat.MIMETYPE_AUDIO_AAC)
                encoder.configure(format, null, null, android.media.MediaCodec.CONFIGURE_FLAG_ENCODE)
                encoder.start()
                audioEncoder = encoder
                audioBytesPerFrame = 1024 * audioChannels * 2
            }
            val info = android.media.MediaCodec.BufferInfo()
            var offset = 0
            while (offset < converted.size) {
                // Keep shutdown bounded: stopProjection interrupts the worker
                // and then joins it before releasing the codec.
                val index = encoder.dequeueInputBuffer(1_000)
                if (index >= 0) {
                    val input = encoder.getInputBuffer(index) ?: break
                    input.clear()
                    val chunk = minOf(input.capacity(), converted.size - offset)
                    input.put(converted, offset, chunk)
                    encoder.queueInputBuffer(index, 0, chunk, audioPtsUs, 0)
                    // Timestamps follow the rate the encoder actually receives.
                    audioPtsUs += chunk / (audioChannels * 2) * 1_000_000L / rate
                    offset += chunk
                }
                var out = encoder.dequeueOutputBuffer(info, 0)
                while (out >= 0) {
                    if (info.size > 0 && info.flags and android.media.MediaCodec.BUFFER_FLAG_CODEC_CONFIG == 0) {
                        val bytes = ByteArray(info.size)
                        encoder.getOutputBuffer(out)?.apply {
                            position(info.offset); limit(info.offset + info.size); get(bytes)
                        }
                        val rawBytes = bytes
                        val payload = stripAdtsHeader(rawBytes)
                        if (!aacOutputDiagnosticLogged) {
                            aacOutputDiagnosticLogged = true
                            observer?.diagnostic(
                                "CarLife AAC 首帧：${rawBytes.size}B，" +
                                    "ADTS=${if (payload.size != rawBytes.size) "已剥离" else "无"}，" +
                                    "输出=${payload.size}B",
                            )
                        }
                        observer?.audioFrame(payload, info.presentationTimeUs)
                    }
                    encoder.releaseOutputBuffer(out, false)
                    out = encoder.dequeueOutputBuffer(info, 0)
                }
            }
        } catch (error: Exception) {
            observer?.diagnostic("CarLife 音频编码失败：${error.message}")
        }
    }

    private fun releaseAudioEncoder() {
        runCatching { audioEncoder?.stop() }
        runCatching { audioEncoder?.release() }
        audioEncoder = null
        audioPtsUs = 0
        aacOutputDiagnosticLogged = false
        resampler = null
        resamplerInputRate = 0
        resamplerOutputRate = 0
        resamplerChannels = 0
    }

    /**
     * MediaCodec normally returns a raw AAC access unit, but a few vendor
     * encoders prepend an ADTS header even when configured for streaming. The
     * AirPlay MainHighAudio path expects the raw access unit; forwarding ADTS
     * makes the car report audio packets while producing silence.
     */
    private fun stripAdtsHeader(bytes: ByteArray): ByteArray {
        if (bytes.size < 7) return bytes
        val sync = ((bytes[0].toInt() and 0xFF) shl 4) or
            ((bytes[1].toInt() and 0xF0) ushr 4)
        if (sync != 0xFFF) return bytes
        val protectionAbsent = bytes[1].toInt() and 0x01
        val headerSize = if (protectionAbsent == 1) 7 else 9
        val frameLength = ((bytes[3].toInt() and 0x03) shl 11) or
            ((bytes[4].toInt() and 0xFF) shl 3) or
            ((bytes[5].toInt() and 0xE0) ushr 5)
        if (headerSize >= bytes.size || frameLength < headerSize || frameLength > bytes.size) {
            return bytes
        }
        return bytes.copyOfRange(headerSize, frameLength)
    }

    @Volatile private var preparedAddress: String? = null
    @Volatile private var preparedHostId: String = "carlink-cast-host"
    @Volatile private var preparedWidth = 0
    @Volatile private var preparedHeight = 0
    @Volatile private var preparedFps = 30

    /**
     * Converts the phone's whole-second timestamp into the microsecond timeline
     * AirPlay wants, counting frames within each second.
     */
    private fun presentationTime(timestampSeconds: Long): Long {
        if (timestampSeconds != ptsBaseSeconds) {
            ptsBaseSeconds = timestampSeconds
            ptsCounter = 0
        }
        val interval = 1_000_000L / streamFps.coerceAtLeast(1)
        val micros = ptsCounter * interval
        ptsCounter++
        var pts = timestampSeconds * 1_000_000L + micros
        // Requesting a key frame rebuilds CarLife's encoder, whose packet
        // timestamps then restart from zero. AirPlay needs a monotonic
        // timeline — the sender refuses a frame that goes backwards and would
        // end the session — so a restart is re-based onto the last value
        // instead of being passed through.
        if (pts <= lastPts) pts = lastPts + interval
        lastPts = pts
        return pts
    }

    override fun onTouchEvent(action: Int, x: Float, y: Float, pointerId: Int) {
        val live = session ?: return
        val processor = videoProcessor
        val point = processor?.mapTouchToSource(x, y) ?: if (processor == null) x to y else null
        when (action) {
            0 -> if (point != null) {
                activeTouchPoints[pointerId] = point
                val index = touchMappingLogged++
                if (index < 8 || index % 40 == 0) {
                    observer?.diagnostic(
                        "CarLife 触控映射 #$index panel=(${"%.1f".format(x)},${"%.1f".format(y)}) " +
                            "→ source=(${point.first.toInt()},${point.second.toInt()}) " +
                            "尺寸=${sourceWidth}x$sourceHeight，使用与视频相同的 viewport 逆变换",
                    )
                }
                live.touch(action, point.first.toInt(), point.second.toInt())
            }
            2 -> if (activeTouchPoints.containsKey(pointerId) && point != null) {
                activeTouchPoints[pointerId] = point
                live.touch(action, point.first.toInt(), point.second.toInt())
            }
            1, 3 -> {
                val previous = activeTouchPoints.remove(pointerId)
                val releaseAt = point ?: previous
                if (releaseAt != null) live.touch(1, releaseAt.first.toInt(), releaseAt.second.toInt())
            }
        }
    }

    override fun onKeyEvent(keyCode: Int, isDown: Boolean) {
        // CarLife takes a key code once, not as a down/up pair.
        if (!isDown) return
        val carLife = CarLifeHardKey.fromAndroid(keyCode) ?: run {
            observer?.diagnostic("车机按键 ${CarLifeHardKey.describe(keyCode)} 在 CarLife 里没有对应码，已忽略")
            return
        }
        session?.key(carLife)
    }

    override suspend fun stopProjection() {
        try {
            synchronized(lifecycleLock) {
                lifecycleGeneration++
                started.set(false)
                // Invalidate Binder callbacks before any slow close/join so a
                // racing start cannot republish them into the old session.
                CarLifeBridge.clear()
            }
            cancelHostRetry()
            synchronized(audioQueueLock) {
                audioQueue.clear()
                audioQueueLock.notifyAll()
            }
            audioWorker?.interrupt()
            runCatching { audioWorker?.join(1_500) }
                .onFailure { observer?.diagnostic("等待 CarLife 音频线程结束失败：${it.message}") }
            audioWorker = null
            runCatching { session?.close() }
                .onFailure { observer?.diagnostic("关闭 CarLife 会话失败：${it.message}") }
            session = null
            runCatching { mediaPublisher?.close() }
                .onFailure { observer?.diagnostic("关闭 CarLife 媒体发布器失败：${it.message}") }
            mediaPublisher = null
            runCatching { videoProcessor?.stop() }
                .onFailure { observer?.diagnostic("关闭全画布合成器失败：${it.message}") }
            activeTouchPoints.clear()
            mediaSnapshotProvider = null
            synchronized(videoStateLock) {
                videoEpoch++
                recoveryRound = 0L
                encoderGeneration = 0L
                frameSink = null
                videoAssembler.reset()
                keyFrameGate.reset()
            }
            ptsBaseSeconds = -1L
            ptsCounter = 0L
            lastPts = 0L
            synchronized(audioQueueLock) {
                audioConfigGeneration++
                audioWorkerGeneration = -1L
            }
            runCatching { releaseAudioEncoder() }
                .onFailure { observer?.diagnostic("释放 CarLife 音频编码器失败：${it.message}") }
        } finally {
            // This must run even when a codec, Binder callback, or worker join
            // throws.  Otherwise Honor CarLife remains alive holding the
            // phone's audio route and the next session inherits its Binder.
            releaseCarLife()
        }
    }

    /**
     * Lets go of CarLife once the car is gone.
     *
     * CarLife is a separate process that this app deliberately starts
     * (`CarlifeConnectService`) and hands a vendor host to. Closing our sockets
     * does not close it: it stays alive holding the audio route it took when it
     * began rendering, so every other app on the phone goes silent. That is the
     * "手机没声了，只能把软件后台删掉" report — the phone was not broken, a
     * head-unit renderer was still running with nobody listening.
     *
     * Both steps are best effort and logged, because either can legitimately
     * fail on a vendor ROM. Stopping our own connect request is the tidy path;
     * the background-process reclaim is the fallback that actually takes the
     * audio route back when CarLife ignores it.
     */
    /**
     * Ends whatever host CarLife is currently bound to, so the startService
     * that follows runs against a fresh process that binds to this app.
     *
     * Stopping the connect service alone is not enough: CarLife clears its host
     * binding only when the bound service dies, not when its own service is
     * stopped. Killing the vendor process is what resets it, and the process
     * has to be out of the foreground first for killBackgroundProcesses to act,
     * hence the stopService before it.
     */
    /**
     * Whether CarLife already has its command port open.
     *
     * Reading another app's process table is denied on Android 10+, but a port
     * that accepts a connection proves the vendor process is alive. The probe
     * connection is closed immediately.
     */
    private fun isCarLifeRunning(): Boolean = runCatching {
        java.net.Socket().use {
            it.connect(
                java.net.InetSocketAddress("127.0.0.1", CarLifeProtocol.PORT_CMD),
                CARLIFE_PROBE_TIMEOUT_MS,
            )
            it.setSoLinger(true, 0)
        }
        true
    }.getOrElse { false }

    private fun detachForeignHost(context: Context, reason: String) {
        if (!isCarLifeRunning()) return
        observer?.diagnostic("$reason：CarLife 已被其他宿主绑定，重置其进程以重新绑定")
        runCatching { context.stopService(connectIntent(context)) }
        runCatching {
            context.getSystemService(android.app.ActivityManager::class.java)
                ?.killBackgroundProcesses(CARLIFE_PACKAGE)
        }
        // Give the vendor process a moment to actually go away; startService
        // on a dying process is what makes the v2 bind silently fail.
        Thread.sleep(HOST_DETACH_SETTLE_MS)
    }

    private fun releaseCarLife() {
        val context = hostContext ?: lifecycleContext ?: return
        cancelHostRetry()
        runCatching {
            val stopped = context.stopService(connectIntent(context))
            observer?.diagnostic("停止 CarLife 连接服务：返回=$stopped")
        }.onFailure { observer?.diagnostic("停止 CarLife 连接服务失败：${it.message}") }
        runCatching {
            val stopped = context.stopService(Intent(context, CarLifeHostService::class.java))
            observer?.diagnostic("停止 CarLink 宿主服务：返回=$stopped")
        }.onFailure { observer?.diagnostic("停止 CarLink 宿主服务失败：${it.message}") }
        runCatching {
            val manager = context.getSystemService(android.app.ActivityManager::class.java)
            manager?.killBackgroundProcesses(CARLIFE_PACKAGE)
            observer?.diagnostic("已释放 CarLife，交还手机音频通道")
        }.onFailure { observer?.diagnostic("释放 CarLife 失败：${it.message}") }
        CarLifeBridge.resetForNewSession()
        /*
         * Also let go of Honor's launcher.
         *
         * CarLife is only a transport: Honor's launcher (com.hihonor.auto)
         * holds the car session and keeps the phone-side connection alive
         * after this app has stopped. Left running, it keeps drawing the car
         * UI, keeps its notification up, and the next session starts against a
         * launcher that still believes it is connected — which is what makes
         * the following attempt fail until the driver disconnects by hand.
         * Stopping its connect service is the closest this app can get to
         * pressing 断开 in the car UI; the system restart policy decides
         * whether the launcher keeps a foreground process.
         */
        runCatching {
            val stopped = context.stopService(
                Intent().setClassName(
                    "com.hihonor.auto",
                    "com.hihonor.auto.connect.ConnectService",
                ),
            )
            observer?.diagnostic("停止荣耀车联连接服务：返回=$stopped")
        }.onFailure { observer?.diagnostic("停止荣耀车联连接服务失败：${it.message}") }
        hostContext = null
        lifecycleContext = null
    }

    private fun logCarLifeInstallState(context: Context, reason: String) {
        runCatching {
            val info = context.packageManager.getApplicationInfo(CARLIFE_PACKAGE, 0)
            val service = context.packageManager.resolveService(
                connectIntent(context),
                PackageManager.MATCH_ALL,
            )
            observer?.diagnostic(
                "$reason：CarLife 安装状态 enabled=${info.enabled} " +
                    "stopped=${(info.flags and android.content.pm.ApplicationInfo.FLAG_STOPPED) != 0} " +
                    "hostService=${service?.serviceInfo?.name ?: "不可解析"}",
            )
        }.onFailure {
            observer?.diagnostic("$reason：CarLife 安装/服务检查失败：${it.javaClass.simpleName}: ${it.message}")
        }
    }

    /**
     * A new AirPlay/CarLife attempt must not inherit the previous process'
     * sockets, Binder callback or audio route.  Honor's connect service is
     * separate from CarLink, so stopping our sockets alone is insufficient.
     */
    private fun retireCarLifeProcess(context: Context, reason: String) {
        cancelHostRetry()
        val now = android.os.SystemClock.uptimeMillis()
        synchronized(lifecycleLock) {
            if (now - lastRetiredAt < HOST_RETIRE_COOLDOWN_MS) {
                observer?.diagnostic("$reason：复用刚完成的 CarLife 进程清理")
                return
            }
            lastRetiredAt = now
        }
        runCatching { context.stopService(connectIntent(context)) }
            .onFailure { observer?.diagnostic("$reason：停止 CarLife 服务失败：${it.message}") }
        runCatching {
            context.getSystemService(android.app.ActivityManager::class.java)
                ?.killBackgroundProcesses(CARLIFE_PACKAGE)
        }.onFailure { observer?.diagnostic("$reason：释放 CarLife 进程失败：${it.message}") }
        CarLifeBridge.resetForNewSession()
        // Give the vendor listener a short window to close all seven ports
        // before the next startService/CarLifeSession.connect() round.
        try {
            Thread.sleep(HOST_RETIRE_SETTLE_MS)
        } catch (_: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    /**
     * Tells CarLife to bind this host, retrying until its process is up.
     *
     * CarLife only registers the handlers that answer the handshake once its
     * connect service has been started, and a request sent before its process
     * exists is dropped without notice. The phone app takes a few seconds to
     * launch, so the request is repeated until the service actually binds.
     */
    private suspend fun notifyHost(context: Context) = withContext(Dispatchers.IO) {
        val intent = connectIntent(context)
        startCarLifeHost(context, "准备会话")
        launchRetry(context, intent)
    }

    /**
     * Keeps asking CarLife to bind in the background.
     *
     * CarLife ignores a connect request that arrives before its process exists
     * and debounces repeats within three seconds, so the retries are spaced
     * beyond that. Only the touch and key callbacks depend on the binding; the
     * video stream comes from the TCP session, which retries on its own.
     */
    private fun launchRetry(context: Context, intent: android.content.Intent) {
        cancelHostRetry()
        val generation = synchronized(lifecycleLock) {
            hostRetryGeneration++
            hostRetryGeneration
        }
        hostRetryThread = Thread({
            try {
                var attempt = 0
                while (attempt < HOST_NOTIFY_ATTEMPTS) {
                    synchronized(lifecycleLock) {
                        if (generation != hostRetryGeneration) return@Thread
                    }
                    if (CarLifeBridge.callbackReady) {
                        observer?.diagnostic("CarLife 宿主绑定与 CastCallback 已就绪")
                        return@Thread
                    }
                    if (attempt > 0) {
                        /*
                         * A bound Binder is not enough: the vendor registers
                         * CastCallback asynchronously. Keep the retry alive
                         * through that window, but retain the 3.2s spacing so
                         * this never becomes a connection storm.
                         */
                        startCarLifeHost(context, "宿主绑定重试 #$attempt")
                    }
                    attempt++
                    if (attempt < HOST_NOTIFY_ATTEMPTS) Thread.sleep(HOST_NOTIFY_INTERVAL_MS)
                }
                if (!CarLifeBridge.callbackReady) {
                    observer?.diagnostic(
                        if (CarLifeBridge.bound) {
                            "CarLife Binder 已绑定，但 CastCallback 未就绪；" +
                                "已完成 ${HOST_NOTIFY_ATTEMPTS} 次会话内退避重试，等待 Binder death 或下一轮会话"
                        } else {
                            "CarLife 宿主 Binder 尚未绑定；" +
                                "已完成 ${HOST_NOTIFY_ATTEMPTS} 次会话内退避重试，TCP 端口仍会继续等待手机端连接"
                        },
                    )
                }
            } catch (_: InterruptedException) {
                // A new session or stopProjection owns the next lifecycle.
            } finally {
                synchronized(lifecycleLock) {
                    if (generation == hostRetryGeneration) hostRetryThread = null
                }
            }
        }, "carlife-host-bind").apply { isDaemon = true; start() }
    }

    private fun cancelHostRetry() {
        synchronized(lifecycleLock) {
            hostRetryGeneration++
            hostRetryThread?.interrupt()
            hostRetryThread = null
        }
    }

    /**
     * A stable, non-empty head-unit id for CarLife's authorization query.
     *
     * CarLife only checks that the id exists and matches what it cached; the
     * activation service call that follows is ignored when it fails. Android id
     * is already unique per installation, so it doubles as the device id here.
     */
    private fun stableHostId(context: Context): String =
        android.provider.Settings.Secure.getString(
            context.contentResolver, android.provider.Settings.Secure.ANDROID_ID
        ).orEmpty().ifBlank { "carlink-cast-host" }

    /** Picks the phone's own routable address; CarLife ignores loopback peers. */
    private suspend fun localAddress(context: Context): String = withContext(Dispatchers.IO) {
        NetworkInterface.getNetworkInterfaces()?.toList().orEmpty()
            .firstOrNull { it.isUp && !it.isLoopback && it.name.startsWith("wlan") }
            ?.inetAddresses?.toList().orEmpty()
            .firstOrNull { it is Inet4Address && !it.isLoopbackAddress }
            ?.hostAddress
            ?: error("未找到可用的 WLAN 地址，CarLife 会忽略回环连接")
    }

    companion object {
        /**
         * Fallback geometry used only if CarLife is started without a prior
         * AirPlay display negotiation. Normal sessions use [prepare]'s panel
         * dimensions so the source, encoder, and AirPlay CONFIG agree.
         */
        const val CARLIFE_ENCODER_WIDTH = 1024
        const val CARLIFE_ENCODER_HEIGHT = 600
        const val CARLIFE_ENCODER_FPS = 30

        /** Honor's CarLife build. */
        const val CARLIFE_PACKAGE = "com.baidu.carlife.honor"

        /** Its single activity, which is not reachable through a launch intent. */
        const val CARLIFE_ACTIVITY = "com.baidu.carlife.CarlifeActivity"

        /** The entry point that starts a session and binds the vendor host. */
        const val CARLIFE_CONNECT_SERVICE = "com.baidu.carlife.service.CarlifeConnectService"

        /**
         * How long to keep asking CarLife to bind while it finishes launching.
         * CarLife drops requests that arrive within three seconds of the last
         * one, so the interval stays above that.
         */
        // 30 attempts at 3.2s covers roughly 96s, long enough for Honor
        // CarLife to finish a cold process start without issuing a tight loop.
        const val HOST_NOTIFY_ATTEMPTS = 30
        const val HOST_NOTIFY_INTERVAL_MS = 3_200L
        const val HOST_START_DEBOUNCE_MS = 2_500L
        const val HOST_RETIRE_SETTLE_MS = 350L

        /** How long to let the vendor process die before starting it again. */
        private const val HOST_DETACH_SETTLE_MS = 900L

        /** How long a loopback probe waits before deciding CarLife is not up. */
        private const val CARLIFE_PROBE_TIMEOUT_MS = 400
        const val HOST_RETIRE_COOLDOWN_MS = 15_000L

    }
}
