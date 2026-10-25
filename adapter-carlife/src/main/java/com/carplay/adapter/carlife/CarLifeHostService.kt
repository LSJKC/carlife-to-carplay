package com.carplay.adapter.carlife

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.Surface
import com.baidu.carlife.mixing.CastConfig
import com.baidu.carlife.mixing.MusicInfo
import com.baidu.carlife.mixing.aidl.ICarLifeCastCallback
import com.baidu.carlife.mixing.aidl.ICarLifeCastManager
import com.baidu.carlife.mixing.aidl.IRemoteAudioListener
import com.baidu.carlife.mixing.aidl.IRemoteKeyListener
import com.baidu.carlife.mixing.aidl.IRemoteTouchListener
import com.baidu.carlife.mixing.aidl.ISystemAudioCallback

private const val CALLBACK_RETIRE_DELAY_MS = 750L

/**
 * The service CarLife binds to once it has been told to connect.
 *
 * CarLife adopts a vendor host this way: it is handed this component's package
 * and class name in its connect intent, binds here, and then drives the cast
 * through ICarLifeCastManager. Binding is also what proves to CarLife that a
 * host is present, so it is a required step rather than an optional one.
 *
 * The Surface CarLife offers in prepareCast is the output side of its own
 * SurfaceTexture: the host must render CarLife's car UI into it through a
 * virtual display and report that display's id back. CarLife then hosts its car
 * UI on that display and its encoder reads the texture and pushes the frames
 * out the phone's video port.
 */
class CarLifeHostService : Service() {

    private var castCallback: ICarLifeCastCallback? = null
    private var castCallbackBinder: IBinder? = null
    private var callbackGeneration = 0L
    /**
     * Binding and callback lifetimes are independent. CarLife can unbind the
     * old process after the next process has already entered onBind(), but
     * before that process has registered its CastCallback. Keep a generation
     * for the service bind itself so that delayed cleanup from the old bind
     * cannot clear the new bind's state.
     */
    private var bindGeneration = 0L
    private var pendingCastPrepared = false
    private var callbackDeathRecipient: IBinder.DeathRecipient? = null

    private val audioListener = object : IRemoteAudioListener.Stub() {
        override fun onReceiveAudioStream(data: ByteArray?, offset: Int, length: Int, sampleRate: Int) {
            if (data == null || offset < 0 || length < 0 || offset > data.size - length) return
            CarLifeBridge.audio?.invoke(data.copyOfRange(offset, offset + length), sampleRate)
        }
    }

    private val binder = object : ICarLifeCastManager.Stub() {

        override fun prepareCast(config: CastConfig?) {
            val surface: Surface? = config?.surface
            CarLifeBridge.castWidth = config?.width ?: 0
            CarLifeBridge.castHeight = config?.height ?: 0
            CarLifeBridge.log(
                "prepareCast: ${config?.width}x${config?.height} dpi=${config?.dpi} " +
                    "surface=${if (surface == null) "无" else "有效=${surface.isValid}"}；" +
                    "这是 CarLife 手机端虚拟画布，不是车机 H.264 画布；" +
                    "车机整屏/安全区由 VIDEO_ENCODER_INIT 单独协商"
            )
            // Answer 0, exactly like the working probe host. CarLife then keeps
            // rendering its car UI in its own off-screen display and encodes it
            // itself; a real virtual display here sent it down the
            // ScreenHonorImpl/HonorDisplayProxy path, where it rendered nothing
            // into the encoder (10 fps of keep-alives, black car screen).
            val displayId = 0
            // CarLife may call prepareCast before setCastCallback. Keep the
            // request and complete it as soon as the callback arrives; dropping
            // it here leaves the phone waiting forever and produces transparent
            // or black video with no useful error.
            synchronized(this@CarLifeHostService) {
                pendingCastPrepared = true
            }
            dispatchCastPrepared(displayId)
        }

        override fun stopCast() {
            CarLifeBridge.log("stopCast")
            /*
             * This callback is the vendor-side equivalent of the head unit's
             * stop-cast request.  Previously we only logged it, so CarLife
             * could release its renderer while CarLink kept the AirPlay,
             * audio and Bluetooth session alive.  Notify the adapter off the
             * Binder thread so the vendor transaction cannot deadlock while
             * the AirPlay side tears down.
             */
            val stop = CarLifeBridge.stop
            if (stop != null) {
                Thread({
                    runCatching { stop.invoke() }
                        .onFailure { CarLifeBridge.log("CarLife 停止回调失败：${it.message}") }
                    // The vendor has explicitly released the cast.  Keeping
                    // this host service alive leaves a stale Binder target
                    // that the next CarLife process can bind to accidentally.
                    stopSelf()
                }, "carlife-stop-notify").apply {
                    isDaemon = true
                    start()
                }
            }
        }

        override fun setSystemAudioCallback(callback: ISystemAudioCallback?) {
            if (callback == null) return
            // Reply off the transaction thread: CarLife is blocked on this call.
            Thread({
                runCatching { callback.onAudioStateChange(0, 0) }
            }, "carlife-audio").start()
        }

        override fun setCastCallback(callback: ICarLifeCastCallback?) {
            /*
             * Honor may deliver a late null callback while the previous
             * process is tearing down. Do not synchronously clear a callback
             * that a newer bind is about to replace; doing so loses media and
             * hard-key routing after reconnect.
             */
            if (callback == null) {
                val (oldBinder, generation) = synchronized(this@CarLifeHostService) {
                    castCallbackBinder to callbackGeneration
                }
                if (oldBinder == null) {
                    CarLifeBridge.markCallbackUnready()
                    return
                }
                CarLifeBridge.log("CarLife 收到空 CastCallback，延迟确认旧会话退出")
                Thread({
                    try {
                        Thread.sleep(CALLBACK_RETIRE_DELAY_MS)
                    } catch (_: InterruptedException) {
                        return@Thread
                    }
                    val shouldRetire = synchronized(this@CarLifeHostService) {
                        callbackGeneration == generation && castCallbackBinder === oldBinder
                    }
                    if (shouldRetire) {
                        synchronized(this@CarLifeHostService) {
                            castCallback = null
                            castCallbackBinder = null
                            pendingCastPrepared = false
                            callbackGeneration++
                            callbackDeathRecipient = null
                        }
                        CarLifeBridge.musicInfo = null
                        CarLifeBridge.musicProgress = null
                        CarLifeBridge.markCallbackUnready()
                        CarLifeBridge.log("CarLife 旧 CastCallback 已确认退出")
                    } else {
                        CarLifeBridge.log("忽略旧 CarLife 空 CastCallback：新会话已接管")
                    }
                }, "carlife-callback-retire").apply { isDaemon = true }.start()
                return
            }
            val (oldBinder, generation) = synchronized(this@CarLifeHostService) {
                val old = castCallbackBinder
                castCallback = callback
                castCallbackBinder = callback?.asBinder()
                callbackGeneration++
                old to callbackGeneration
            }
            callbackDeathRecipient?.let { oldRecipient ->
                if (oldBinder != null) runCatching { oldBinder.unlinkToDeath(oldRecipient, 0) }
            }
            val target = callback
            val binder = target.asBinder()
            val recipient = IBinder.DeathRecipient {
                val current = synchronized(this@CarLifeHostService) {
                    if (callbackGeneration != generation || castCallbackBinder !== binder) {
                        false
                    } else {
                        castCallback = null
                        castCallbackBinder = null
                        pendingCastPrepared = false
                        true
                    }
                }
                if (current) {
                    CarLifeBridge.markCallbackUnready()
                    CarLifeBridge.log("CarLife CastCallback Binder 已死亡")
                }
            }
            callbackDeathRecipient = recipient
            runCatching { binder.linkToDeath(recipient, 0) }
                .onFailure { CarLifeBridge.log("CarLife CastCallback linkToDeath 失败：${it.message}") }
            CarLifeBridge.musicInfo = { info ->
                runCatching { currentCallback()?.sendMusicInfo(info) }
            }
            CarLifeBridge.musicProgress = { position ->
                runCatching { currentCallback()?.notifyMusicProgress(position) }
            }
            CarLifeBridge.markCallbackReady()
            // Never call back into CarLife from inside this transaction; it is
            // still waiting for the reply and a nested call would deadlock.
            Thread({
                runCatching { target.onServiceReady() }
                runCatching { target.addTouchListener(touchListener) }
                runCatching { target.addKeyListener(keyListener) }
                runCatching { target.addAudioListener(audioListener) }
            }, "carlife-cast-ready").start()
            dispatchCastPrepared(0)
        }

        override fun invokeMethod(intent: Intent?): Bundle? {
            val action = intent?.action ?: return null
            CarLifeBridge.log("invokeMethod: $action")
            // The format callback returns only safe-area vendor keys. It keeps
            // CarLife's negotiated coded size while constraining UI to that area.
            return CarLifeBridge.invoke(action, intent.extras)
        }
    }

    private val touchListener = object : IRemoteTouchListener.Stub() {
        override fun onTouchEvent(event: android.view.MotionEvent?): Boolean {
            event ?: return false
            CarLifeBridge.touch?.invoke(event.actionMasked, event.x, event.y)
            return true
        }
    }

    private val keyListener = object : IRemoteKeyListener.Stub() {
        override fun onKeyEvent(event: android.view.KeyEvent?): Boolean {
            event ?: return false
            CarLifeBridge.key?.invoke(event.keyCode, event.action == android.view.KeyEvent.ACTION_DOWN)
            return true
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        val (generation, oldBinder, oldDeathRecipient) = synchronized(this) {
            bindGeneration++
            /*
             * A reconnect can call onBind before the old client's delayed
             * onUnbind cleanup runs. Retire that callback at the new bind
             * boundary; otherwise media metadata and hard-key events can still
             * be delivered to the dead CarLife process.
             */
            val oldBinder = castCallbackBinder
            val oldDeathRecipient = callbackDeathRecipient
            castCallback = null
            castCallbackBinder = null
            callbackDeathRecipient = null
            pendingCastPrepared = false
            callbackGeneration++
            Triple(bindGeneration, oldBinder, oldDeathRecipient)
        }
        if (oldBinder != null && oldDeathRecipient != null) {
            runCatching { oldBinder.unlinkToDeath(oldDeathRecipient, 0) }
        }
        CarLifeBridge.musicInfo = null
        CarLifeBridge.musicProgress = null
        CarLifeBridge.markCallbackUnready()
        CarLifeBridge.log(
            "CarLife 宿主服务收到绑定：uid=${android.os.Binder.getCallingUid()}，bindGeneration=$generation",
        )
        CarLifeBridge.markBound()
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        val generation = synchronized(this) { bindGeneration }
        CarLifeBridge.log("CarLife 宿主服务收到解绑：等待代际确认 bindGeneration=$generation")
        Thread({
            try {
                Thread.sleep(CALLBACK_RETIRE_DELAY_MS)
            } catch (_: InterruptedException) {
                return@Thread
            }
            val shouldClear = synchronized(this@CarLifeHostService) {
                // A newer onBind() or a newly registered callback owns the
                // service now. The old unbind must not retire it.
                bindGeneration == generation && castCallback == null
            }
            if (!shouldClear) {
                CarLifeBridge.log("忽略旧 CarLife onUnbind：新绑定或 CastCallback 已接管")
                return@Thread
            }
            CarLifeBridge.musicInfo = null
            CarLifeBridge.musicProgress = null
            CarLifeBridge.markCallbackUnready()
            CarLifeBridge.markUnbound()
            CarLifeBridge.log("CarLife 宿主解绑已确认：bindGeneration=$generation")
        }, "carlife-bind-retire").apply { isDaemon = true }.start()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        synchronized(this) {
            bindGeneration++
            castCallback = null
            castCallbackBinder = null
            pendingCastPrepared = false
            callbackDeathRecipient = null
        }
        CarLifeBridge.musicInfo = null
        CarLifeBridge.musicProgress = null
        CarLifeBridge.markCallbackUnready()
        CarLifeBridge.markUnbound()
        CarLifeBridge.log("CarLife 宿主服务已销毁")
        super.onDestroy()
    }

    private fun currentCallback(): ICarLifeCastCallback? = synchronized(this) { castCallback }

    private fun dispatchCastPrepared(displayId: Int) {
        val callback = synchronized(this) {
            if (!pendingCastPrepared) return
            val current = castCallback ?: return
            pendingCastPrepared = false
            current
        }
        Thread({
            runCatching { callback.onCastPrepared(displayId) }
                .onFailure { CarLifeBridge.log("onCastPrepared 回调失败：${it.message}") }
        }, "carlife-cast-prepared").start()
    }
}

/**
 * Connects the running adapter to [CarLifeHostService].
 *
 * CarLife binds the host directly, so the callbacks have to be handed across a
 * process boundary that the adapter itself cannot see; this holds the current
 * session's hooks for the service to reach.
 */
internal object CarLifeBridge {
    /** Diagnostics sink, set while a session is live. */
    @Volatile var log: (String) -> Unit = {}

    /** Car touch events, in CarLife's own action numbering. */
    @Volatile var touch: ((Int, Float, Float) -> Unit)? = null

    /** Car key events. */
    @Volatile var key: ((Int, Boolean) -> Unit)? = null

    /** Audio delivered through the host callback, if this CarLife build uses it. */
    @Volatile var audio: ((ByteArray, Int) -> Unit)? = null

    /** Called when CarLife itself releases the cast through stopCast(). */
    @Volatile var stop: (() -> Unit)? = null

    @Volatile var musicInfo: ((MusicInfo) -> Unit)? = null
    @Volatile var musicProgress: ((Long) -> Unit)? = null

    fun publishMusic(info: MusicInfo) {
        runCatching { musicInfo?.invoke(info) }
    }

    fun publishMusicProgress(positionMs: Long, lyrics: String = "") {
        runCatching { musicProgress?.invoke(positionMs) }
    }

    /** Set once CarLife has bound the host, which is what makes the handshake work. */
    @Volatile var bound: Boolean = false
        private set

    /** True only after CarLife has registered the callback used for media/keys. */
    @Volatile var callbackReady: Boolean = false
        private set

    /** The size the car negotiated, kept for diagnostics only. */
    @Volatile var width: Int = 0
    @Volatile var height: Int = 0
    @Volatile var fps: Int = 30

    /** Host identity reported to CarLife's authorization query. */
    @Volatile var deviceId: String = ""
    @Volatile var deviceVCode: String = ""

    /** The pixel size CarLife renders its car UI at (prepareCast's CastConfig). */
    @Volatile var castWidth: Int = 0
    @Volatile var castHeight: Int = 0

    fun markBound() {
        if (bound) return
        bound = true
        log("CarLife 已绑定宿主服务")
    }

    fun markCallbackReady() {
        if (callbackReady) return
        callbackReady = true
        log("CarLife CastCallback 已注册")
    }

    fun markCallbackUnready() {
        if (!callbackReady) return
        callbackReady = false
        log("CarLife CastCallback 已失效")
    }

    /** CarLife dropped the host; the next session has to bind again. */
    fun markUnbound() {
        if (!bound) return
        bound = false
        callbackReady = false
        log("CarLife 已断开宿主服务")
    }

    /**
     * Answers the queries CarLife sends the host.
     *
     * The encoder-format response only forwards the validated panel safe-area
     * vendor parameters. It intentionally leaves the negotiated codec width,
     * height, and frame rate untouched.
     */
    fun invoke(action: String, extras: Bundle?): Bundle? = when (action) {
        "carlife_connect_audio_type" -> {
            val audioType = extras?.getInt("audioType", 1) ?: 1
            log("CarLife 音频通道通知：audioType=$audioType，保持 CarPlay 媒体音频由车机协商")
            Bundle().apply { putInt("audioType", audioType) }
        }
        "carlife_music_sample_rate" -> {
            // The vehicle session advertises 48 kHz media.  Returning the same
            // rate prevents CarLife from resampling its media port to 44.1 kHz
            // before the AirPlay AAC/PCM negotiation is complete.
            log("CarLife 音乐采样率查询：返回 48000Hz")
            Bundle().apply { putInt("music_sample_rate", 48_000) }
        }
        "carlife_vr_use_phone_mic" -> {
            log("CarLife 麦克风查询：返回手机麦克风")
            Bundle().apply { putBoolean("is_use_phone_mic", true) }
        }
        // Six seconds into the session Honor CarLife's MixAuthorization asks the
        // head unit who it is. An empty answer fails checkId() and the phone
        // reports "车机未授权，即将退出". A real host supplies the device id and
        // its Android id; the activation query that follows is then ignored on
        // failure (mIgnoreActiviteFail), so the session survives offline.
        CARLIFE_REQUEST_DEVICEID -> Bundle().apply {
            putString("device_id", deviceId)
            putString("device_vcode", deviceVCode)
            putString("app_version", "1.0.0")
            putString("rom_version", android.os.Build.DISPLAY.orEmpty())
        }
        CARLIFE_ENCODER_FORMAT -> {
            val params = extras?.getString(CARLIFE_PARAMS)
            val format = CarLifeEncoderFormat.fromJson(params) ?: run {
                log("忽略无效的 CarLife 编码安全区参数")
                return null
            }
            // The flag comes from the negotiated CarLife format. Do not infer
            // it from a Buick-sized canvas: dimensions describe a display,
            // not the vehicle brand, and another head unit may use the same
            // full-panel contract at a different size.
            val overrides = format.mediaFormatOverrides()
            log(
                "CarLife 编码安全区回调：面板 ${format.panelWidth}x${format.panelHeight}，" +
                    "安全区 ${format.safeWidth}x${format.safeHeight}@(${format.safeX},${format.safeY})；" +
                    "保持 CarLife 编码宽高/FPS不变",
            )
            Bundle().apply {
                overrides.forEach { (key, value) ->
                    when (value) {
                        is Int -> putInt(key, value)
                        is Boolean -> putBoolean(key, value)
                        is Long -> putLong(key, value)
                        is Float -> putFloat(key, value)
                        else -> putString(key, value.toString())
                    }
                }
            }  // ← 关键修复：返回这个 Bundle，使得 overrides 被真正应用
        }
        else -> null
    }

    private const val CARLIFE_ENCODER_FORMAT = "carlife_encode_format"
    private const val CARLIFE_PARAMS = "params"
    private const val CARLIFE_REQUEST_DEVICEID = "carlife_request_device_id"
    private const val CARLIFE_GET_BT_ADDRESS = "get_bt_address"

    /**
     * Drops this session's callbacks.
     *
     * [bound] is deliberately kept: CarLife holds the Binder connection across
     * a session restart, and forgetting it here made the next prepare() think
     * the host had never bound, so the retry loop gave up and touch stayed dead.
     */
    fun clear() {
        log = {}
        touch = null
        key = null
        audio = null
        stop = null
        musicInfo = null
        musicProgress = null
        callbackReady = false
    }

    /** Clears Binder/process state when CarLink deliberately retires a session. */
    fun resetForNewSession() {
        clear()
        bound = false
        callbackReady = false
        castWidth = 0
        castHeight = 0
    }
}
