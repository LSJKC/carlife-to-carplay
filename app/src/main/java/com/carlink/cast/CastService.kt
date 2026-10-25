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
    private val destroyed = AtomicBoolean()
    private val lifecycleLock = Any()
    private var sessionQueued = false
    private var pendingStart: PendingStart? = null
    private var sessionGeneration = 0L
    @Volatile private var activeAutomaticSession = false

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
        destroyed.set(false)
        instance = this
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
        requestStart(intent ?: Intent(this, CastService::class.java), startId)
        return START_NOT_STICKY
    }

    private fun requestStart(intent: Intent, startId: Int) {
        val automatic = intent.getBooleanExtra(EXTRA_AUTO_START, false)
        var retired: CastEngine? = null
        val generation: Long
        synchronized(lifecycleLock) {
            if (sessionQueued || engine != null) {
                if (automatic) {
                    // Android can emit several ACL_CONNECTED broadcasts while
                    // the same Bluetooth handoff is settling. An automatic
                    // broadcast must never retire a live session and start a
                    // second CarLife process on top of it.
                    CastLog.event("INFO", "忽略重复自动连接请求：当前会话仍在排队或运行")
                    return
                }
                // One replacement is enough. Repeated ACL broadcasts must not
                // build a queue of generations that all race the same car.
                if (pendingStart == null) pendingStart = PendingStart(Intent(intent), startId)
                stopping.set(true)
                sessionGeneration++
                retired = engine
                CastLog.event("WARN", "收到新的连接请求：先退役旧 CarLife/AirPlay 会话，再开始新会话")
                generation = sessionGeneration
            } else {
                sessionQueued = true
                stopping.set(false)
                sessionGeneration++
                generation = sessionGeneration
            }
        }
        retired?.close()
        if (retired != null) return
        runCatching { worker.execute { runSession(intent, startId, generation) } }
            .onFailure {
                if (automatic) AutoConnectCoordinator.recordFailure(settingsPeer())
                CastLog.event("ERROR", "排队投屏会话失败：${it.message}")
            }
    }

    /** Runs exactly one generation; a replacement is queued behind cleanup. */
    private fun runSession(intent: Intent?, startId: Int, generation: Long) {
        if (!isCurrent(generation)) {
            finishSession(startId, null, generation)
            return
        }
        val automatic = intent?.getBooleanExtra(EXTRA_AUTO_START, false) == true
        val settings = CastSettings(this)
        val peer = settings.peerAddress
        val source = try {
            createSource(intent, settings)
        } catch (failure: Exception) {
            CastLog.begin("准备")
            CastLog.stop(failure.message ?: "无法准备投屏来源")
            notifyFailure(failure)
            if (automatic) peer?.let { AutoConnectCoordinator.recordFailure(it) }
            finishSession(startId, null, generation)
            return
        }
        // A replacement request can arrive while createSource() is preparing
        // the CarLife adapter. Do not publish a foreground session for a
        // generation that has already been retired.
        if (!isCurrent(generation)) {
            finishSession(startId, null, generation)
            return
        }
        startForegroundNotification(source.name, source is MirrorSource)
        acquireSessionLocks()
        val sessionId = CastLog.begin(source.name)
        val session = CastEngine(applicationContext, settings, source) { message ->
            if (CastLog.state.value.sessionId == sessionId) CastLog.event("INFO", message)
        }
        val cancelledBeforePublish = synchronized(lifecycleLock) {
            if (stopping.get() || generation != sessionGeneration) {
                true
            } else {
                engine = session
                activeAutomaticSession = automatic
                false
            }
        }
        if (cancelledBeforePublish) {
            session.close()
            releaseSessionLocks()
            finishSession(startId, null, generation)
            return
        }
        runCatching { worker.execute {
            // requestStart() may have retired this generation after it was
            // published but before the queued engine task got CPU time.
            if (!isCurrent(generation) || engine !== session) {
                session.close()
                finishSession(startId, session, generation)
                return@execute
            }
            val failureRecorded = AtomicBoolean(false)
            fun recordAutomaticFailure() {
                if (automatic && failureRecorded.compareAndSet(false, true)) {
                    peer?.let { AutoConnectCoordinator.recordFailure(it) }
                }
            }
            session.run { failure ->
                if (isCurrent(generation) && CastLog.state.value.sessionId == sessionId) {
                    CastLog.stop(failure.message ?: failure.javaClass.simpleName)
                    notifyFailure(failure)
                }
                recordAutomaticFailure()
                // Failure callbacks can arrive from an AirPlay worker while
                // awaitSessionEnd is still waiting. Close immediately so the
                // CarLife host/audio route is released before the next ACL.
                session.close()
            }
            // run() returns once the AirPlay worker is started; the session's
            // real lifetime is tracked by the engine.
            awaitSessionEnd(session, sessionId, generation, automatic)
            // A worker can finish without invoking the callback when it is
            // closed by the remote side during teardown. Only the terminal
            // FAILED state counts as an automatic-attempt failure; a normal
            // startup return must never enter backoff.
            if (CastLog.state.value.sessionId == sessionId &&
                CastLog.state.value.stage == CastStage.FAILED
            ) {
                recordAutomaticFailure()
            }
            finishSession(startId, session, generation)
        } }.onFailure {
            session.close()
            if (automatic) peer?.let { AutoConnectCoordinator.recordFailure(it) }
            CastLog.event("ERROR", "启动投屏工作线程失败：${it.message}")
        }
    }

    private fun isCurrent(generation: Long): Boolean =
        synchronized(lifecycleLock) {
            generation == sessionGeneration && !stopping.get()
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
            carLife.videoProcessor = CarLifePanelCompositor(panel)
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
    private fun awaitSessionEnd(
        session: CastEngine,
        sessionId: String,
        generation: Long,
        automatic: Boolean,
    ) {
        val peer = CastSettings(this).peerAddress
        var stableSince = 0L
        var lastProgress = 0L
        var lastFrames = -1L
        var recorded = false
        while (!stopping.get() && !session.hasFinished()) {
            Thread.sleep(300)
            val state = CastLog.state.value
            if (state.sessionId != sessionId || generation != synchronized(lifecycleLock) { sessionGeneration }) {
                continue
            }
            val now = android.os.SystemClock.elapsedRealtime()
            val stable = state.stage == CastStage.STREAMING &&
                state.frames > 0 &&
                session.isHealthy()
            if (!stable) {
                stableSince = 0L
                lastProgress = 0L
                lastFrames = state.frames
                continue
            }
            if (state.frames > lastFrames) {
                if (stableSince == 0L) stableSince = now
                lastProgress = now
                lastFrames = state.frames
            }
            if (!recorded &&
                stableSince > 0L &&
                now - stableSince >= STABLE_SUCCESS_WINDOW_MS &&
                now - lastProgress <= FRAME_STALL_GRACE_MS
            ) {
                recorded = true
                if (peer != null) {
                    CastSettings(this).markConnected(peer)
                    AutoConnectCoordinator.recordSuccess(peer)
                    CastLog.event(
                        "INFO",
                        "画面稳定 ${STABLE_SUCCESS_WINDOW_MS / 1000}s，已记录该车机连接成功；" +
                            if (automatic) "自动重连已解除冷却" else "手动会话",
                    )
                }
            }
        }
        if (!stopping.get() && CastLog.state.value.sessionId == sessionId) {
            val state = CastLog.state.value
            if (state.stage != CastStage.FAILED) CastLog.stop()
        }
    }

    private fun finishSession(startId: Int, finishedSession: CastEngine?, generation: Long) {
        if (destroyed.get()) {
            runCatching { finishedSession?.close() }
            return
        }
        runCatching { cleanup.execute {
            val shouldRelease = synchronized(lifecycleLock) {
                val wasPublishedEngine = finishedSession != null && engine === finishedSession
                if (wasPublishedEngine) engine = null
                wasPublishedEngine || (finishedSession == null && generation == sessionGeneration)
            }
            finishedSession?.close()
            if (shouldRelease) {
                activeAutomaticSession = false
                AutoConnectCoordinator.release(CastSettings(this).peerAddress)
            }
            if (shouldRelease) releaseSessionLocks()
            val next = synchronized(lifecycleLock) {
                val value = pendingStart
                pendingStart = null
                if (value != null && engine == null) {
                    // A duplicate Bluetooth broadcast can retire a generation
                    // before its engine is published. Start the one coalesced
                    // replacement even though the finishing generation is
                    // already stale.
                    sessionQueued = true
                    stopping.set(false)
                } else if (value == null && generation == sessionGeneration) {
                    sessionQueued = false
                }
                value
            }
            if (next != null) {
                val nextGeneration = synchronized(lifecycleLock) { sessionGeneration }
                runCatching { worker.execute { runSession(next.intent, next.startId, nextGeneration) } }
                    .onFailure { CastLog.event("ERROR", "排队下一次投屏失败：${it.message}") }
            } else if (generation == synchronized(lifecycleLock) { sessionGeneration }) {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf(startId)
            }
        } }.onFailure {
            runCatching { finishedSession?.close() }
            CastLog.event("ERROR", "投屏清理线程不可用：${it.message}")
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
        var retired: CastEngine? = null
        synchronized(lifecycleLock) {
            pendingStart = null
            stopping.set(true)
            sessionGeneration++
            retired = engine
        }
        CastLog.stop(reason)
        retired?.close()
        if (destroyed.get()) {
            runCatching { retired?.close() }
            return
        }
        runCatching { cleanup.execute {
            synchronized(lifecycleLock) {
                if (engine === retired) engine = null
                sessionQueued = false
            }
            releaseSessionLocks()
            AutoConnectCoordinator.release(CastSettings(this).peerAddress)
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        } }.onFailure {
            runCatching { retired?.close() }
            CastLog.event("ERROR", "停止投屏清理失败：${it.message}")
        }
    }

    private fun onBluetoothDisconnected(address: String) {
        val settings = CastSettings(this)
        if (!address.equals(settings.peerAddress, ignoreCase = true)) return
        val current = synchronized(lifecycleLock) { engine }
        if (current == null) return
        if (current.isWirelessHandoffComplete()) {
            CastLog.event("INFO", "车机蓝牙已断开，但 Wi-Fi 控制已完成交接，保留 CarLife 会话")
            return
        }
        if (activeAutomaticSession) {
            AutoConnectCoordinator.recordFailure(address)
        }
        CastLog.event("WARN", "车机蓝牙已断开且尚未完成 Wi-Fi 交接，自动释放 CarLife 会话")
        stopSession("车机蓝牙已断开")
    }

    override fun onDestroy() {
        destroyed.set(true)
        runCatching { engine?.close() }
        engine = null
        activeAutomaticSession = false
        // Every way a session can end goes through here or finishSession(); a
        // single missed path would leave the CPU lock held and drain the phone
        // long after the car is parked.
        releaseSessionLocks()
        AutoConnectCoordinator.release(CastSettings(this).peerAddress)
        worker.shutdownNow()
        cleanup.shutdown()
        if (instance === this) instance = null
        super.onDestroy()
    }

    private fun settingsPeer(): String =
        CastSettings(this).peerAddress.orEmpty().ifBlank { "UNKNOWN" }

    companion object {
        private data class PendingStart(val intent: Intent, val startId: Int)
        const val ACTION_START = "com.carlink.cast.START"
        const val ACTION_STOP = "com.carlink.cast.STOP"
        const val EXTRA_AUTO_START = "autoStart"
        const val EXTRA_CAPTURE = "capture"
        const val EXTRA_CAPTURE_RESULT = "captureResult"
        private const val CHANNEL = "cast"
        private const val FOREGROUND_ID = 11
        private const val FAILURE_ID = 12
        private const val STABLE_SUCCESS_WINDOW_MS = 4_000L
        private const val FRAME_STALL_GRACE_MS = 1_200L
        @Volatile private var instance: CastService? = null

        fun isBusy(): Boolean {
            val service = instance ?: return false
            synchronized(service.lifecycleLock) {
                return service.sessionQueued || service.engine != null || service.pendingStart != null
            }
        }

        fun onBluetoothDisconnected(context: android.content.Context, address: String) {
            val service = instance ?: return
            service.onBluetoothDisconnected(address)
        }
    }
}
