package com.carlink.cast

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import com.carplay.adapter.PhoneProjectionAdapter
import com.carplay.adapter.ProjectionGrant
import com.carplay.adapter.carlife.CarLifeMediaSnapshot
import com.carplay.adapter.carlife.CarLifeProjectionAdapter
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Owns the session so the connection survives the activity.
 *
 * The source is chosen before the session starts and is fixed for its lifetime:
 * CarLife when it is installed (no screen-capture consent, the phone renders and
 * encodes its own car UI), otherwise authorized screen mirroring.
 */
class CastService : Service() {

    private val worker = Executors.newSingleThreadExecutor { task ->
        Thread(task, "carlink-session").apply { isDaemon = true }
    }
    private val cleanup = Executors.newSingleThreadExecutor { task ->
        Thread(task, "carlink-cleanup").apply { isDaemon = true }
    }
    @Volatile private var engine: CastEngine? = null
    private val stopping = AtomicBoolean()

    /**
     * True once onDestroy has run.
     *
     * The executors below are shut down there, and a session that ends by the
     * service being destroyed still reaches finishSession() from the worker
     * thread. Submitting to a terminated pool throws
     * RejectedExecutionException and kills the app — observed in a real crash
     * log (`... rejected from ThreadPoolExecutor[Terminated]`) right after a
     * disconnect.
     */
    @Volatile private var destroyed = false
    /** Set once this session has recorded a real success for this car. */
    @Volatile private var connectedRecorded = false

    /**
     * Locks that keep the session alive while the phone is in a pocket.
     *
     * Without these the screen turning off lets the CPU suspend and the Wi-Fi
     * radio enter power save; the car then sees the video and iAP2 link stall
     * and drops the session a few minutes in, which is what looked like
     * "自动断开". They are released the moment the session ends so they cannot
     * keep draining the battery afterwards.
     */
    private var wakeLock: android.os.PowerManager.WakeLock? = null
    private var wifiLock: android.net.wifi.WifiManager.WifiLock? = null

    override fun onCreate() {
        super.onCreate()
        CastLog.initialize(this)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> {
                stopSession("用户停止投屏")
                return START_NOT_STICKY
            }
            ACTION_START -> Unit
            else -> return START_NOT_STICKY
        }
        if (engine != null) return START_NOT_STICKY
        stopping.set(false)
        val settings = CastSettings(this)
        val source = try {
            createSource(intent, settings)
        } catch (failure: Exception) {
            CastLog.begin("准备")
            CastLog.stop(failure.message ?: "无法准备投屏来源")
            notifyFailure(failure)
            stopSelf(startId)
            return START_NOT_STICKY
        }
        startForegroundNotification(source.name, source is MirrorSource)
        acquireSessionLocks()
        CastLog.begin(source.name)
        val session = CastEngine(applicationContext, settings, source) { message ->
            CastLog.event(com.carlink.cast.LogChannel.SESSION, "INFO", message)
        }
        engine = session
        runCatching {
            worker.execute {
                session.run { failure ->
                    if (!stopping.get()) {
                        CastLog.stop(failure.message ?: failure.javaClass.simpleName)
                        notifyFailure(failure)
                    }
                }
                // run() returns once the AirPlay worker is started; the session's
                // real lifetime is tracked by the engine.
                awaitSessionEnd(session)
                finishSession(startId)
            }
        }.onFailure { failure ->
            CastLog.event("ERROR", "投屏工作线程已关闭，会话无法启动：${failure.message}")
            runCatching { session.close() }
            if (engine === session) engine = null
            runCatching { releaseSessionLocks() }
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf(startId)
        }
        return START_NOT_STICKY
    }

    private fun createSource(intent: Intent?, settings: CastSettings): PhoneProjectionAdapter {
        val mode = settings.sourceMode
        val carLife = CarLifeProjectionAdapter()
        val useCarLife = mode != SourceMode.MIRROR && carLife.isSupported(this)
        if (useCarLife) {
            // Fallback until AirPlay /info provides this session's full display
            // and optional safeArea; AirPlayLink replaces the geometry before prepare().
            val panel = com.carplay.adapter.carlife.CarLifePanel(
                panelWidth = CarPanelGeometry.PANEL_WIDTH,
                panelHeight = CarPanelGeometry.PANEL_HEIGHT,
                safeX = CarPanelGeometry.SAFE_X,
                safeY = CarPanelGeometry.SAFE_Y,
                safeWidth = CarPanelGeometry.SAFE_WIDTH,
                safeHeight = CarPanelGeometry.SAFE_HEIGHT,
                drawUiOutsideSafeArea = true,
                needAdapterScreen = true,
            )
            carLife.panel = panel
            carLife.mediaSnapshotProvider = {
                MediaSessionSnapshotStore.current?.let { snapshot ->
                    CarLifeMediaSnapshot(
                        title = snapshot.title,
                        artist = snapshot.artist,
                        album = snapshot.album,
                        lyrics = snapshot.lyrics,
                        durationMs = snapshot.durationMs,
                        positionMs = snapshot.positionMs,
                        playing = snapshot.state == 1,
                        packageName = snapshot.packageName,
                        artworkBytes = snapshot.artwork,
                    )
                }
            }
            // CarLife emits a surface-sized H.264 stream that is not the size the
            // head unit negotiated, so it must be decoded, composited onto the
            // panel and re-encoded. Without this processor the raw CarLife frames
            // go straight to the car, which decodes nothing and asks for a key
            // frame forever while the screen stays black.
            //
            // Passthrough mode deliberately skips all of that. CarLife is asked
            // for the panel size instead of the safe rectangle (see
            // CarLifeSession's `passthrough`), so its stream already carries the
            // canvas the car declared and can be forwarded untouched — the shape
            // a-sender uses on this same Honor CarLife build. The trade is the
            // wallpaper the compositor paints outside the safe area.
            val passthrough = settings.videoPassthrough
            carLife.passthroughVideo = passthrough
            if (passthrough) {
                CastLog.event("INFO", "视频直通模式：不合成、不重编码，CarLife 编码尺寸=全画布")
            } else {
                carLife.videoProcessor = CarLifePanelCompositor(panel)
            }
            return carLife
        }
        if (mode == SourceMode.CARLIFE) error("未找到手机自带的 CarLife 应用")
        val data = if (Build.VERSION.SDK_INT >= 33) {
            intent?.getParcelableExtra(EXTRA_CAPTURE, Intent::class.java)
        } else {
            @Suppress("DEPRECATION") intent?.getParcelableExtra(EXTRA_CAPTURE)
        }
        val resultCode = intent?.getIntExtra(EXTRA_CAPTURE_RESULT, Activity.RESULT_CANCELED)
            ?: Activity.RESULT_CANCELED
        if (data == null || resultCode != Activity.RESULT_OK) error("缺少屏幕投射授权，请重新开始")
        val mirror = MirrorSource(this)
        mirror.setGrant(ProjectionGrant(resultCode, data))
        mirror.enableAudio(settings.audio)
        return mirror
    }

    /**
     * Takes the CPU and Wi-Fi locks for the duration of one session.
     *
     * `PARTIAL_WAKE_LOCK` keeps the CPU running with the screen off, and
     * `WIFI_MODE_FULL_HIGH_PERF` stops the radio from entering power save. The
     * car treats either stall as a dead link. Locks are reference counted and
     * released in [releaseSessionLocks], so a failed start cannot leak one.
     */
    private fun acquireSessionLocks() {
        runCatching {
            val power = getSystemService(PowerManager::class.java)
            wakeLock = power?.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "carlink:session")?.apply {
                setReferenceCounted(false)
                acquire()
            }
        }.onFailure { CastLog.event("WARN", "获取 CPU 锁失败：${it.message}") }
        runCatching {
            val wifi = applicationContext.getSystemService(android.net.wifi.WifiManager::class.java)
            // LOW_LATENCY also disables power save but does not tear the radio
            // down between packets, which HIGH_PERF may do. minSdk is 29, so it
            // is always available here.
            wifiLock = wifi?.createWifiLock(
                android.net.wifi.WifiManager.WIFI_MODE_FULL_LOW_LATENCY, "carlink:session",
            )?.apply {
                setReferenceCounted(false)
                acquire()
            }
        }.onFailure { CastLog.event("WARN", "获取 Wi-Fi 锁失败：${it.message}") }
    }

    private fun releaseSessionLocks() {
        runCatching { wakeLock?.takeIf { it.isHeld }?.release() }
        wakeLock = null
        runCatching { wifiLock?.takeIf { it.isHeld }?.release() }
        wifiLock = null
    }

    /**
     * Waits for the AirPlay worker to exit, then lets the log settle.
     *
     * While waiting it records the one state the next connection depends on:
     * that this car has actually shown a picture. Only a real success is
     * written, so a failed attempt can never turn into "already connected" and
     * send the driver straight past Honor's first-connection flow into a
     * session that cannot work.
     */
    private fun awaitSessionEnd(session: CastEngine) {
        while (!stopping.get() && !session.hasFinished()) {
            Thread.sleep(300)
            if (!connectedRecorded && CastLog.state.value.frames > 0) {
                connectedRecorded = true
                val peer = CastSettings(this).peerAddress
                if (peer != null) {
                    CastSettings(this).markConnected(peer)
                    CastLog.event("INFO", "已记录该车机连接成功，下次可直接连接")
                }
            }
        }
        if (!stopping.get()) {
            val state = CastLog.state.value
            if (state.stage != CastStage.FAILED) CastLog.stop()
        }
    }

    private fun finishSession(startId: Int) {
        if (destroyed) {
            runCatching { engine?.close() }
            engine = null
            runCatching { releaseSessionLocks() }
            return
        }
        runCatching {
            cleanup.execute {
                engine?.close()
                engine = null
                releaseSessionLocks()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf(startId)
            }
        }.onFailure { failure ->
            // The pool was shut down between the flag check and the submit.
            // Finish synchronously: leaving the engine open here would keep the
            // CarLife host and the CPU lock held with no session behind them.
            CastLog.event("WARN", "会话清理线程已关闭，改为同步收尾：${failure.message}")
            runCatching { engine?.close() }
            engine = null
            runCatching { releaseSessionLocks() }
        }
    }

    private fun startForegroundNotification(sourceName: String, screenCapture: Boolean) {
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL, getString(R.string.notif_channel), NotificationManager.IMPORTANCE_LOW)
        )
        val open = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val stop = PendingIntent.getService(this, 1,
            Intent(this, CastService::class.java).setAction(ACTION_STOP), PendingIntent.FLAG_IMMUTABLE)
        val notification: Notification = Notification.Builder(this, CHANNEL)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(R.string.notif_title))
            .setContentText("$sourceName · ${getString(R.string.notif_text)}")
            .setContentIntent(open)
            .setOngoing(true)
            .addAction(Notification.Action.Builder(null, getString(R.string.notif_stop), stop).build())
            .build()
        // The mediaProjection type is only claimed when a screen grant backs it;
        // CarLife sessions are connectedDevice only.
        if (Build.VERSION.SDK_INT >= 29) {
            startForeground(FOREGROUND_ID, notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE or
                    if (screenCapture) ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION else 0)
        } else {
            startForeground(FOREGROUND_ID, notification)
        }
    }

    private fun notifyFailure(failure: Throwable) {
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL, getString(R.string.notif_channel), NotificationManager.IMPORTANCE_LOW)
        )
        val open = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        manager.notify(FAILURE_ID, Notification.Builder(this, CHANNEL)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(R.string.notif_title))
            .setContentText(failure.message ?: failure.javaClass.simpleName)
            .setContentIntent(open)
            .setAutoCancel(true)
            .build())
    }

    private fun stopSession(reason: String) {
        if (!stopping.compareAndSet(false, true)) return
        CastLog.stop(reason)
        runCatching {
            cleanup.execute {
                engine?.close()
                engine = null
                releaseSessionLocks()
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }.onFailure {
            runCatching { engine?.close() }
            engine = null
            runCatching { releaseSessionLocks() }
        }
    }

    override fun onDestroy() {
        destroyed = true
        runCatching { engine?.close() }
        engine = null
        // Every way a session can end goes through here or finishSession(); a
        // single missed path would leave the CPU lock held and drain the phone
        // long after the car is parked.
        releaseSessionLocks()
        worker.shutdownNow()
        cleanup.shutdown()
        super.onDestroy()
    }

    companion object {
        const val ACTION_START = "com.carlink.cast.START"
        const val ACTION_STOP = "com.carlink.cast.STOP"
        const val EXTRA_CAPTURE = "capture"
        const val EXTRA_CAPTURE_RESULT = "captureResult"
        private const val CHANNEL = "cast"
        private const val FOREGROUND_ID = 11
        private const val FAILURE_ID = 12
    }
}
