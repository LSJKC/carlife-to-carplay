package com.carlink.cast;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import com.carplay.adapter.PhoneProjectionAdapter;
import com.carplay.adapter.ProjectionGrant;
import com.carplay.adapter.carlife.CarLifeMediaSnapshot;
import com.carplay.adapter.carlife.CarLifePanel;
import com.carplay.adapter.carlife.CarLifeProjectionAdapter;
import com.carplay.iap2.transport.Iap2IdentificationClient;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: CastService.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000\u0088\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u0003\n\u0002\b\b\u0018\u0000 C2\u00020\u0001:\u0001CB\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u001c\u001a\u00020\u001dH\u0016J\u0014\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\"\u0010\"\u001a\u00020#2\b\u0010 \u001a\u0004\u0018\u00010!2\u0006\u0010$\u001a\u00020#2\u0006\u0010%\u001a\u00020#H\u0016J\u0018\u0010&\u001a\u00020\u001d2\u0006\u0010 \u001a\u00020!2\u0006\u0010%\u001a\u00020#H\u0002J\"\u0010'\u001a\u00020\u001d2\b\u0010 \u001a\u0004\u0018\u00010!2\u0006\u0010%\u001a\u00020#2\u0006\u0010(\u001a\u00020\u0014H\u0002J\u0010\u0010)\u001a\u00020\u00102\u0006\u0010(\u001a\u00020\u0014H\u0002J\u001a\u0010*\u001a\u00020+2\b\u0010 \u001a\u0004\u0018\u00010!2\u0006\u0010,\u001a\u00020-H\u0002J\b\u0010.\u001a\u00020\u001dH\u0002J\b\u0010/\u001a\u00020\u001dH\u0002J(\u00100\u001a\u00020\u001d2\u0006\u00101\u001a\u00020\t2\u0006\u00102\u001a\u0002032\u0006\u0010(\u001a\u00020\u00142\u0006\u00104\u001a\u00020\u0010H\u0002J\"\u00105\u001a\u00020\u001d2\u0006\u0010%\u001a\u00020#2\b\u00106\u001a\u0004\u0018\u00010\t2\u0006\u0010(\u001a\u00020\u0014H\u0002J\u0018\u00107\u001a\u00020\u001d2\u0006\u00108\u001a\u0002032\u0006\u00109\u001a\u00020\u0010H\u0002J\u0010\u0010:\u001a\u00020\u001d2\u0006\u0010;\u001a\u00020<H\u0002J\u0010\u0010=\u001a\u00020\u001d2\u0006\u0010>\u001a\u000203H\u0002J\u0010\u0010?\u001a\u00020\u001d2\u0006\u0010@\u001a\u000203H\u0002J\b\u0010A\u001a\u00020\u001dH\u0016J\b\u0010B\u001a\u000203H\u0002R\u0016\u0010\u0004\u001a\n \u0006*\u0004\u0018\u00010\u00050\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \u0006*\u0004\u0018\u00010\u00050\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\b\u0018\u00010\u0017R\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0019\u001a\b\u0018\u00010\u001aR\u00020\u001bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006D"}, d2 = {"Lcom/carlink/cast/CastService;", "Landroid/app/Service;", "<init>", "()V", "worker", "Ljava/util/concurrent/ExecutorService;", "kotlin.jvm.PlatformType", "cleanup", "engine", "Lcom/carlink/cast/CastEngine;", "stopping", "Ljava/util/concurrent/atomic/AtomicBoolean;", "destroyed", "lifecycleLock", "", "sessionQueued", "", "pendingStart", "Lcom/carlink/cast/CastService$Companion$PendingStart;", "sessionGeneration", "", "activeAutomaticSession", "wakeLock", "Landroid/os/PowerManager$WakeLock;", "Landroid/os/PowerManager;", "wifiLock", "Landroid/net/wifi/WifiManager$WifiLock;", "Landroid/net/wifi/WifiManager;", "onCreate", "", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "onStartCommand", "", "flags", "startId", "requestStart", "runSession", "generation", "isCurrent", "createSource", "Lcom/carplay/adapter/PhoneProjectionAdapter;", "settings", "Lcom/carlink/cast/CastSettings;", "acquireSessionLocks", "releaseSessionLocks", "awaitSessionEnd", "session", "sessionId", "", "automatic", "finishSession", "finishedSession", "startForegroundNotification", "sourceName", "screenCapture", "notifyFailure", "failure", "", "stopSession", "reason", "onBluetoothDisconnected", "address", "onDestroy", "settingsPeer", "Companion", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CastService extends Service {
    public static final String ACTION_START = "com.carlink.cast.START";
    public static final String ACTION_STOP = "com.carlink.cast.STOP";
    private static final String CHANNEL = "cast";

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final String EXTRA_AUTO_START = "autoStart";
    public static final String EXTRA_CAPTURE = "capture";
    public static final String EXTRA_CAPTURE_RESULT = "captureResult";
    private static final int FAILURE_ID = 12;
    private static final int FOREGROUND_ID = 11;
    private static final long FRAME_STALL_GRACE_MS = 1200;
    private static final long STABLE_SUCCESS_WINDOW_MS = 4000;
    private static volatile CastService instance;
    private volatile boolean activeAutomaticSession;
    private volatile CastEngine engine;
    private Companion.PendingStart pendingStart;
    private long sessionGeneration;
    private boolean sessionQueued;
    private PowerManager.WakeLock wakeLock;
    private WifiManager.WifiLock wifiLock;
    private final ExecutorService worker = Executors.newSingleThreadExecutor(new ThreadFactory() { // from class: com.carlink.cast.CastService$$ExternalSyntheticLambda7
        @Override // java.util.concurrent.ThreadFactory
        public final Thread newThread(Runnable runnable) {
            return CastService.worker$lambda$1(runnable);
        }
    });
    private final ExecutorService cleanup = Executors.newSingleThreadExecutor(new ThreadFactory() { // from class: com.carlink.cast.CastService$$ExternalSyntheticLambda8
        @Override // java.util.concurrent.ThreadFactory
        public final Thread newThread(Runnable runnable) {
            return CastService.cleanup$lambda$3(runnable);
        }
    });
    private final AtomicBoolean stopping = new AtomicBoolean();
    private final AtomicBoolean destroyed = new AtomicBoolean();
    private final Object lifecycleLock = new Object();

    static final Thread worker$lambda$1(Runnable task) {
        Thread thread = new Thread(task, "carlink-session");
        thread.setDaemon(true);
        return thread;
    }

    static final Thread cleanup$lambda$3(Runnable task) {
        Thread thread = new Thread(task, "carlink-cleanup");
        thread.setDaemon(true);
        return thread;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.destroyed.set(false);
        instance = this;
        CastLog.INSTANCE.initialize(this);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) throws Throwable {
        String action = intent != null ? intent.getAction() : null;
        if (action != null) {
            switch (action.hashCode()) {
                case -1425792508:
                    if (action.equals(ACTION_STOP)) {
                        stopSession("用户停止投屏");
                        return 2;
                    }
                    break;
                case -1249908096:
                    if (action.equals(ACTION_START)) {
                        requestStart(intent == null ? new Intent(this, (Class<?>) CastService.class) : intent, startId);
                        return 2;
                    }
                    break;
            }
        }
        return 2;
    }

    private final void requestStart(final Intent intent, final int startId) throws Throwable {
        Object objM159constructorimpl;
        boolean automatic = intent.getBooleanExtra(EXTRA_AUTO_START, false);
        CastEngine castEngine = null;
        final Ref.LongRef generation = new Ref.LongRef();
        synchronized (this.lifecycleLock) {
            if (this.sessionQueued || this.engine != null) {
                if (automatic) {
                    CastLog.INSTANCE.event("INFO", "忽略重复自动连接请求：当前会话仍在排队或运行");
                    return;
                }
                if (this.pendingStart == null) {
                    this.pendingStart = new Companion.PendingStart(new Intent(intent), startId);
                }
                this.stopping.set(true);
                this.sessionGeneration++;
                castEngine = this.engine;
                CastLog.INSTANCE.event("WARN", "收到新的连接请求：先退役旧 CarLife/AirPlay 会话，再开始新会话");
                generation.element = this.sessionGeneration;
            } else {
                this.sessionQueued = true;
                this.stopping.set(false);
                this.sessionGeneration++;
                generation.element = this.sessionGeneration;
            }
            Unit unit = Unit.INSTANCE;
            if (castEngine != null) {
                castEngine.close();
            }
            if (castEngine != null) {
                return;
            }
            try {
                Result.Companion companion = Result.INSTANCE;
                final CastService castService = this;
                castService.worker.execute(new Runnable() { // from class: com.carlink.cast.CastService$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() throws Throwable {
                        this.f$0.runSession(intent, startId, generation.element);
                    }
                });
                objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl);
            if (thM162exceptionOrNullimpl != null) {
                if (automatic) {
                    AutoConnectCoordinator.recordFailure$default(AutoConnectCoordinator.INSTANCE, settingsPeer(), 0L, 2, null);
                }
                CastLog.INSTANCE.event("ERROR", "排队投屏会话失败：" + thM162exceptionOrNullimpl.getMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void runSession(Intent intent, final int startId, final long generation) throws Throwable {
        boolean z;
        Object objM159constructorimpl;
        if (!isCurrent(generation)) {
            finishSession(startId, null, generation);
            return;
        }
        final boolean automatic = intent != null && intent.getBooleanExtra(EXTRA_AUTO_START, false);
        CastSettings settings = new CastSettings(this);
        final String peer = settings.getPeerAddress();
        try {
            PhoneProjectionAdapter source = createSource(intent, settings);
            if (isCurrent(generation)) {
                startForegroundNotification(source.getName(), source instanceof MirrorSource);
                acquireSessionLocks();
                final String sessionId = CastLog.INSTANCE.begin(source.getName());
                Context applicationContext = getApplicationContext();
                Intrinsics.checkNotNullExpressionValue(applicationContext, "getApplicationContext(...)");
                final CastEngine session = new CastEngine(applicationContext, settings, source, new Function1() { // from class: com.carlink.cast.CastService$$ExternalSyntheticLambda1
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return CastService.runSession$lambda$9(sessionId, (String) obj);
                    }
                });
                synchronized (this.lifecycleLock) {
                    if (this.stopping.get() || generation != this.sessionGeneration) {
                        z = true;
                    } else {
                        this.engine = session;
                        this.activeAutomaticSession = automatic;
                        z = false;
                    }
                }
                boolean cancelledBeforePublish = z;
                if (cancelledBeforePublish) {
                    session.close();
                    releaseSessionLocks();
                    finishSession(startId, null, generation);
                    return;
                }
                try {
                    Result.Companion companion = Result.INSTANCE;
                    final CastService castService = this;
                    castService.worker.execute(new Runnable() { // from class: com.carlink.cast.CastService$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() throws Throwable {
                            CastService.runSession$lambda$14$lambda$13(this.f$0, generation, session, startId, sessionId, automatic, peer);
                        }
                    });
                    objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
                } catch (Throwable th) {
                    Result.Companion companion2 = Result.INSTANCE;
                    objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
                }
                Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl);
                if (thM162exceptionOrNullimpl != null) {
                    session.close();
                    if (automatic && peer != null) {
                        AutoConnectCoordinator.recordFailure$default(AutoConnectCoordinator.INSTANCE, peer, 0L, 2, null);
                    }
                    CastLog.INSTANCE.event("ERROR", "启动投屏工作线程失败：" + thM162exceptionOrNullimpl.getMessage());
                    return;
                }
                return;
            }
            finishSession(startId, null, generation);
        } catch (Exception failure) {
            CastLog.INSTANCE.begin("准备");
            CastLog castLog = CastLog.INSTANCE;
            String message = failure.getMessage();
            if (message == null) {
                message = "无法准备投屏来源";
            }
            castLog.stop(message);
            notifyFailure(failure);
            if (automatic && peer != null) {
                AutoConnectCoordinator.recordFailure$default(AutoConnectCoordinator.INSTANCE, peer, 0L, 2, null);
            }
            finishSession(startId, null, generation);
        }
    }

    static final Unit runSession$lambda$9(String $sessionId, String message) {
        Intrinsics.checkNotNullParameter(message, "message");
        if (Intrinsics.areEqual(CastLog.INSTANCE.getState().getValue().getSessionId(), $sessionId)) {
            CastLog.INSTANCE.event("INFO", message);
        }
        return Unit.INSTANCE;
    }

    static final void runSession$lambda$14$lambda$13(final CastService $this_runCatching, final long $generation, final CastEngine $session, int $startId, final String $sessionId, final boolean $automatic, final String $peer) throws Throwable {
        if (!$this_runCatching.isCurrent($generation) || $this_runCatching.engine != $session) {
            $session.close();
            $this_runCatching.finishSession($startId, $session, $generation);
            return;
        }
        final AtomicBoolean failureRecorded = new AtomicBoolean(false);
        $session.run(new Function1() { // from class: com.carlink.cast.CastService$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return CastService.runSession$lambda$14$lambda$13$lambda$12(this.f$0, $generation, $sessionId, $session, $automatic, failureRecorded, $peer, (Throwable) obj);
            }
        });
        $this_runCatching.awaitSessionEnd($session, $sessionId, $generation, $automatic);
        if (Intrinsics.areEqual(CastLog.INSTANCE.getState().getValue().getSessionId(), $sessionId) && CastLog.INSTANCE.getState().getValue().getStage() == CastStage.FAILED) {
            runSession$lambda$14$lambda$13$recordAutomaticFailure($automatic, failureRecorded, $peer);
        }
        $this_runCatching.finishSession($startId, $session, $generation);
    }

    private static final void runSession$lambda$14$lambda$13$recordAutomaticFailure(boolean automatic, AtomicBoolean failureRecorded, String peer) throws Throwable {
        if (automatic && failureRecorded.compareAndSet(false, true) && peer != null) {
            AutoConnectCoordinator.recordFailure$default(AutoConnectCoordinator.INSTANCE, peer, 0L, 2, null);
        }
    }

    static final Unit runSession$lambda$14$lambda$13$lambda$12(CastService $this_runCatching, long $generation, String $sessionId, CastEngine $session, boolean $automatic, AtomicBoolean $failureRecorded, String $peer, Throwable failure) throws Throwable {
        Intrinsics.checkNotNullParameter(failure, "failure");
        if ($this_runCatching.isCurrent($generation) && Intrinsics.areEqual(CastLog.INSTANCE.getState().getValue().getSessionId(), $sessionId)) {
            CastLog castLog = CastLog.INSTANCE;
            String message = failure.getMessage();
            if (message == null) {
                message = failure.getClass().getSimpleName();
            }
            castLog.stop(message);
            $this_runCatching.notifyFailure(failure);
        }
        runSession$lambda$14$lambda$13$recordAutomaticFailure($automatic, $failureRecorded, $peer);
        $session.close();
        return Unit.INSTANCE;
    }

    private final boolean isCurrent(long generation) {
        boolean z;
        synchronized (this.lifecycleLock) {
            z = generation == this.sessionGeneration && !this.stopping.get();
        }
        return z;
    }

    private final PhoneProjectionAdapter createSource(Intent intent, CastSettings settings) {
        SourceMode mode = settings.getSourceMode();
        CarLifeProjectionAdapter carLife = new CarLifeProjectionAdapter();
        boolean useCarLife = mode != SourceMode.MIRROR && carLife.isSupported(this);
        if (useCarLife) {
            CarLifePanel panel = new CarLifePanel(CarPanelGeometry.PANEL_WIDTH, CarPanelGeometry.PANEL_HEIGHT, CarPanelGeometry.SAFE_X, 44, CarPanelGeometry.SAFE_WIDTH, CarPanelGeometry.SAFE_HEIGHT, true, true);
            carLife.setPanel(panel);
            carLife.setMediaSnapshotProvider(new Function0() { // from class: com.carlink.cast.CastService$$ExternalSyntheticLambda4
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return CastService.createSource$lambda$19();
                }
            });
            carLife.setVideoProcessor(new CarLifePanelCompositor(panel));
            return carLife;
        }
        if (mode == SourceMode.CARLIFE) {
            throw new IllegalStateException("未找到手机自带的 CarLife 应用".toString());
        }
        Intent data = null;
        if (Build.VERSION.SDK_INT >= 33) {
            if (intent != null) {
                data = (Intent) intent.getParcelableExtra(EXTRA_CAPTURE, Intent.class);
            }
        } else if (intent != null) {
            data = (Intent) intent.getParcelableExtra(EXTRA_CAPTURE);
        }
        int resultCode = intent != null ? intent.getIntExtra(EXTRA_CAPTURE_RESULT, 0) : 0;
        if (data == null || resultCode != -1) {
            throw new IllegalStateException("缺少屏幕投射授权，请重新开始".toString());
        }
        MirrorSource mirror = new MirrorSource(this);
        mirror.setGrant(new ProjectionGrant(resultCode, data));
        mirror.enableAudio(settings.getAudio());
        return mirror;
    }

    static final CarLifeMediaSnapshot createSource$lambda$19() {
        MediaSessionSnapshotStore.Snapshot current = MediaSessionSnapshotStore.INSTANCE.getCurrent();
        if (current != null) {
            return new CarLifeMediaSnapshot(current.getTitle(), current.getArtist(), current.getAlbum(), current.getLyrics(), current.getDurationMs(), current.getPositionMs(), current.getState() == 1, current.getPackageName(), null, current.getArtwork(), 0, null, 0, Iap2IdentificationClient.START_IDENTIFICATION, null);
        }
        return null;
    }

    private final void acquireSessionLocks() {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        WifiManager.WifiLock wifiLockCreateWifiLock;
        PowerManager.WakeLock wakeLockNewWakeLock;
        WifiManager.WifiLock wifiLock = null;
        try {
            Result.Companion companion = Result.INSTANCE;
            CastService castService = this;
            PowerManager powerManager = (PowerManager) castService.getSystemService(PowerManager.class);
            if (powerManager == null || (wakeLockNewWakeLock = powerManager.newWakeLock(1, "carlink:session")) == null) {
                wakeLockNewWakeLock = null;
            } else {
                wakeLockNewWakeLock.setReferenceCounted(false);
                wakeLockNewWakeLock.acquire();
            }
            castService.wakeLock = wakeLockNewWakeLock;
            objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (thM162exceptionOrNullimpl != null) {
            CastLog.INSTANCE.event("WARN", "获取 CPU 锁失败：" + thM162exceptionOrNullimpl.getMessage());
        }
        try {
            Result.Companion companion3 = Result.INSTANCE;
            CastService castService2 = this;
            WifiManager wifiManager = (WifiManager) castService2.getApplicationContext().getSystemService(WifiManager.class);
            if (wifiManager != null && (wifiLockCreateWifiLock = wifiManager.createWifiLock(4, "carlink:session")) != null) {
                wifiLockCreateWifiLock.setReferenceCounted(false);
                wifiLockCreateWifiLock.acquire();
                wifiLock = wifiLockCreateWifiLock;
            }
            castService2.wifiLock = wifiLock;
            objM159constructorimpl2 = Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        Throwable thM162exceptionOrNullimpl2 = Result.m162exceptionOrNullimpl(objM159constructorimpl2);
        if (thM162exceptionOrNullimpl2 != null) {
            CastLog.INSTANCE.event("WARN", "获取 Wi-Fi 锁失败：" + thM162exceptionOrNullimpl2.getMessage());
        }
    }

    /* JADX WARN: Code duplicated, block: B:11:0x001d  */
    /* JADX WARN: Code duplicated, block: B:25:0x004a  */
    private final void releaseSessionLocks() {
        Unit unit;
        Unit unit2;
        try {
            Result.Companion companion = Result.INSTANCE;
            PowerManager.WakeLock wakeLock = this.wakeLock;
            if (wakeLock == null) {
                unit2 = null;
            } else {
                if (!wakeLock.isHeld()) {
                    wakeLock = null;
                }
                if (wakeLock != null) {
                    wakeLock.release();
                    unit2 = Unit.INSTANCE;
                } else {
                    unit2 = null;
                }
            }
            Result.m159constructorimpl(unit2);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        this.wakeLock = null;
        try {
            Result.Companion companion3 = Result.INSTANCE;
            WifiManager.WifiLock wifiLock = this.wifiLock;
            if (wifiLock == null) {
                unit = null;
            } else {
                if (!wifiLock.isHeld()) {
                    wifiLock = null;
                }
                if (wifiLock != null) {
                    wifiLock.release();
                    unit = Unit.INSTANCE;
                } else {
                    unit = null;
                }
            }
            Result.m159constructorimpl(unit);
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        this.wifiLock = null;
    }

    private final void awaitSessionEnd(CastEngine session, String sessionId, long generation, boolean automatic) throws InterruptedException {
        long j;
        String peer = new CastSettings(this).getPeerAddress();
        long lastFrames = -1;
        long lastProgress = 0;
        long stableSince = 0;
        boolean recorded = false;
        while (true) {
            if (this.stopping.get() || session.hasFinished()) {
                break;
            }
            Thread.sleep(300L);
            CastState state = CastLog.INSTANCE.getState().getValue();
            if (Intrinsics.areEqual(state.getSessionId(), sessionId)) {
                synchronized (this.lifecycleLock) {
                    try {
                        j = this.sessionGeneration;
                    } catch (Throwable th) {
                        throw th;
                    }
                }
                if (generation == j) {
                    long now = SystemClock.elapsedRealtime();
                    boolean stable = state.getStage() == CastStage.STREAMING && state.getFrames() > 0 && session.isHealthy();
                    if (stable) {
                        if (state.getFrames() > lastFrames) {
                            if (stableSince == 0) {
                                stableSince = now;
                            }
                            lastProgress = now;
                            lastFrames = state.getFrames();
                        }
                        if (recorded || stableSince <= 0 || now - stableSince < STABLE_SUCCESS_WINDOW_MS || now - lastProgress > 1200) {
                            peer = peer;
                            stableSince = stableSince;
                        } else {
                            if (peer != null) {
                                new CastSettings(this).markConnected(peer);
                                AutoConnectCoordinator.INSTANCE.recordSuccess(peer);
                                CastLog castLog = CastLog.INSTANCE;
                                String peer2 = automatic ? "自动重连已解除冷却" : "手动会话";
                                castLog.event("INFO", "画面稳定 4s，已记录该车机连接成功；" + peer2);
                            }
                            recorded = true;
                            peer = peer;
                            stableSince = stableSince;
                        }
                    } else {
                        stableSince = 0;
                        lastProgress = 0;
                        lastFrames = state.getFrames();
                    }
                }
            }
            peer = peer;
        }
        if (this.stopping.get() || !Intrinsics.areEqual(CastLog.INSTANCE.getState().getValue().getSessionId(), sessionId) || CastLog.INSTANCE.getState().getValue().getStage() == CastStage.FAILED) {
            return;
        }
        CastLog.stop$default(CastLog.INSTANCE, null, 1, null);
    }

    private final void finishSession(final int startId, CastEngine finishedSession, final long generation) {
        final CastEngine finishedSession2;
        Throwable th;
        Object objM159constructorimpl;
        Unit unit = null;
        if (this.destroyed.get()) {
            try {
                Result.Companion companion = Result.INSTANCE;
                CastService castService = this;
                if (finishedSession != null) {
                    finishedSession.close();
                    unit = Unit.INSTANCE;
                }
                Result.m159constructorimpl(unit);
                return;
            } catch (Throwable th2) {
                Result.Companion companion2 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th2));
                return;
            }
        }
        try {
            Result.Companion companion3 = Result.INSTANCE;
            final CastService castService2 = this;
            finishedSession2 = finishedSession;
            try {
                castService2.cleanup.execute(new Runnable() { // from class: com.carlink.cast.CastService$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        CastService.finishSession$lambda$40$lambda$39(this.f$0, finishedSession2, generation, startId);
                    }
                });
                objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th3) {
                th = th3;
                Result.Companion companion4 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
        } catch (Throwable th4) {
            finishedSession2 = finishedSession;
            th = th4;
        }
        Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (thM162exceptionOrNullimpl != null) {
            try {
                Result.Companion companion5 = Result.INSTANCE;
                CastService castService3 = this;
                if (finishedSession2 != null) {
                    finishedSession2.close();
                    unit = Unit.INSTANCE;
                }
                Result.m159constructorimpl(unit);
            } catch (Throwable th5) {
                Result.Companion companion6 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th5));
            }
            CastLog.INSTANCE.event("ERROR", "投屏清理线程不可用：" + thM162exceptionOrNullimpl.getMessage());
        }
    }

    /* JADX WARN: Code duplicated, block: B:11:0x0010  */
    static final void finishSession$lambda$40$lambda$39(final CastService $this_runCatching, CastEngine $finishedSession, long $generation, int $startId) {
        boolean z;
        int i;
        final Companion.PendingStart pendingStart;
        long j;
        final long nextGeneration;
        Object objM159constructorimpl;
        synchronized ($this_runCatching.lifecycleLock) {
            if ($finishedSession != null) {
                try {
                    if ($this_runCatching.engine == $finishedSession) {
                        z = true;
                    } else {
                        z = false;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            } else {
                z = false;
            }
            if (z) {
                $this_runCatching.engine = null;
            }
            i = (z || ($finishedSession == null && $generation == $this_runCatching.sessionGeneration)) ? 1 : 0;
        }
        if ($finishedSession != null) {
            $finishedSession.close();
        }
        if (i != 0) {
            $this_runCatching.activeAutomaticSession = false;
            AutoConnectCoordinator.INSTANCE.release(new CastSettings($this_runCatching).getPeerAddress());
        }
        if (i != 0) {
            $this_runCatching.releaseSessionLocks();
        }
        synchronized ($this_runCatching.lifecycleLock) {
            pendingStart = $this_runCatching.pendingStart;
            $this_runCatching.pendingStart = null;
            if (pendingStart != null && $this_runCatching.engine == null) {
                $this_runCatching.sessionQueued = true;
                $this_runCatching.stopping.set(false);
            } else if (pendingStart == null && $generation == $this_runCatching.sessionGeneration) {
                $this_runCatching.sessionQueued = false;
            }
        }
        if (pendingStart == null) {
            synchronized ($this_runCatching.lifecycleLock) {
                j = $this_runCatching.sessionGeneration;
            }
            if ($generation == j) {
                $this_runCatching.stopForeground(1);
                $this_runCatching.stopSelf($startId);
                return;
            }
            return;
        }
        synchronized ($this_runCatching.lifecycleLock) {
            nextGeneration = $this_runCatching.sessionGeneration;
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            $this_runCatching.worker.execute(new Runnable() { // from class: com.carlink.cast.CastService$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() throws Throwable {
                    CastService castService = this.f$0;
                    CastService.Companion.PendingStart pendingStart2 = pendingStart;
                    castService.runSession(pendingStart2.getIntent(), pendingStart2.getStartId(), nextGeneration);
                }
            });
            objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th2) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (thM162exceptionOrNullimpl != null) {
            CastLog.INSTANCE.event("ERROR", "排队下一次投屏失败：" + thM162exceptionOrNullimpl.getMessage());
        }
    }

    private final void startForegroundNotification(String sourceName, boolean screenCapture) {
        NotificationManager manager = (NotificationManager) getSystemService(NotificationManager.class);
        manager.createNotificationChannel(new NotificationChannel(CHANNEL, getString(R.string.notif_channel), 2));
        PendingIntent open = PendingIntent.getActivity(this, 0, new Intent(this, (Class<?>) MainActivity.class), 201326592);
        PendingIntent stop = PendingIntent.getService(this, 1, new Intent(this, (Class<?>) CastService.class).setAction(ACTION_STOP), 67108864);
        Notification notification = new Notification.Builder(this, CHANNEL).setSmallIcon(R.drawable.ic_notification).setContentTitle(getString(R.string.notif_title)).setContentText(sourceName + " · " + getString(R.string.notif_text)).setContentIntent(open).setOngoing(true).addAction(new Notification.Action.Builder((Icon) null, getString(R.string.notif_stop), stop).build()).build();
        Intrinsics.checkNotNullExpressionValue(notification, "build(...)");
        startForeground(11, notification, (screenCapture ? 32 : 0) | 16);
    }

    private final void notifyFailure(Throwable failure) {
        NotificationManager manager = (NotificationManager) getSystemService(NotificationManager.class);
        manager.createNotificationChannel(new NotificationChannel(CHANNEL, getString(R.string.notif_channel), 2));
        PendingIntent open = PendingIntent.getActivity(this, 0, new Intent(this, (Class<?>) MainActivity.class), 201326592);
        Notification.Builder contentTitle = new Notification.Builder(this, CHANNEL).setSmallIcon(R.drawable.ic_notification).setContentTitle(getString(R.string.notif_title));
        String message = failure.getMessage();
        if (message == null) {
            message = failure.getClass().getSimpleName();
        }
        manager.notify(12, contentTitle.setContentText(message).setContentIntent(open).setAutoCancel(true).build());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v3, types: [T, com.carlink.cast.CastEngine] */
    private final void stopSession(String reason) {
        Unit unit;
        Object objM159constructorimpl;
        final Ref.ObjectRef retired = new Ref.ObjectRef();
        synchronized (this.lifecycleLock) {
            unit = null;
            this.pendingStart = null;
            this.stopping.set(true);
            this.sessionGeneration++;
            retired.element = this.engine;
            Unit unit2 = Unit.INSTANCE;
        }
        CastLog.INSTANCE.stop(reason);
        CastEngine castEngine = (CastEngine) retired.element;
        if (castEngine != null) {
            castEngine.close();
        }
        if (this.destroyed.get()) {
            try {
                Result.Companion companion = Result.INSTANCE;
                CastService castService = this;
                CastEngine castEngine2 = (CastEngine) retired.element;
                if (castEngine2 != null) {
                    castEngine2.close();
                    unit = Unit.INSTANCE;
                }
                Result.m159constructorimpl(unit);
                return;
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th));
                return;
            }
        }
        try {
            Result.Companion companion3 = Result.INSTANCE;
            final CastService castService2 = this;
            castService2.cleanup.execute(new Runnable() { // from class: com.carlink.cast.CastService$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    CastService.stopSession$lambda$47$lambda$46(this.f$0, retired);
                }
            });
            objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (thM162exceptionOrNullimpl != null) {
            try {
                Result.Companion companion5 = Result.INSTANCE;
                CastService castService3 = this;
                CastEngine castEngine3 = (CastEngine) retired.element;
                if (castEngine3 != null) {
                    castEngine3.close();
                    unit = Unit.INSTANCE;
                }
                Result.m159constructorimpl(unit);
            } catch (Throwable th3) {
                Result.Companion companion6 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th3));
            }
            CastLog.INSTANCE.event("ERROR", "停止投屏清理失败：" + thM162exceptionOrNullimpl.getMessage());
        }
    }

    static final void stopSession$lambda$47$lambda$46(CastService $this_runCatching, Ref.ObjectRef $retired) {
        synchronized ($this_runCatching.lifecycleLock) {
            if ($this_runCatching.engine == $retired.element) {
                $this_runCatching.engine = null;
            }
            $this_runCatching.sessionQueued = false;
            Unit unit = Unit.INSTANCE;
        }
        $this_runCatching.releaseSessionLocks();
        AutoConnectCoordinator.INSTANCE.release(new CastSettings($this_runCatching).getPeerAddress());
        $this_runCatching.stopForeground(1);
        $this_runCatching.stopSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onBluetoothDisconnected(String address) throws Throwable {
        CastEngine current;
        CastSettings settings = new CastSettings(this);
        if (StringsKt.equals(address, settings.getPeerAddress(), true)) {
            synchronized (this.lifecycleLock) {
                current = this.engine;
            }
            if (current == null) {
                return;
            }
            if (current.isWirelessHandoffComplete()) {
                CastLog.INSTANCE.event("INFO", "车机蓝牙已断开，但 Wi-Fi 控制已完成交接，保留 CarLife 会话");
                return;
            }
            if (this.activeAutomaticSession) {
                AutoConnectCoordinator.recordFailure$default(AutoConnectCoordinator.INSTANCE, address, 0L, 2, null);
            }
            CastLog.INSTANCE.event("WARN", "车机蓝牙已断开且尚未完成 Wi-Fi 交接，自动释放 CarLife 会话");
            stopSession("车机蓝牙已断开");
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        Unit unit;
        this.destroyed.set(true);
        try {
            Result.Companion companion = Result.INSTANCE;
            CastEngine castEngine = this.engine;
            if (castEngine != null) {
                castEngine.close();
                unit = Unit.INSTANCE;
            } else {
                unit = null;
            }
            Result.m159constructorimpl(unit);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        this.engine = null;
        this.activeAutomaticSession = false;
        releaseSessionLocks();
        AutoConnectCoordinator.INSTANCE.release(new CastSettings(this).getPeerAddress());
        this.worker.shutdownNow();
        this.cleanup.shutdown();
        if (instance == this) {
            instance = null;
        }
        super.onDestroy();
    }

    private final String settingsPeer() {
        String peerAddress = new CastSettings(this).getPeerAddress();
        if (peerAddress == null) {
            peerAddress = "";
        }
        String str = peerAddress;
        if (StringsKt.isBlank(str)) {
            str = "UNKNOWN";
        }
        return str;
    }

    /* JADX INFO: compiled from: CastService.kt */
    @Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001:\u0001\u001aB\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0006\u0010\u0013\u001a\u00020\u0014J\u0016\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0005R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082T¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001b"}, d2 = {"Lcom/carlink/cast/CastService$Companion;", "", "<init>", "()V", "ACTION_START", "", "ACTION_STOP", "EXTRA_AUTO_START", "EXTRA_CAPTURE", "EXTRA_CAPTURE_RESULT", "CHANNEL", "FOREGROUND_ID", "", "FAILURE_ID", "STABLE_SUCCESS_WINDOW_MS", "", "FRAME_STALL_GRACE_MS", "instance", "Lcom/carlink/cast/CastService;", "isBusy", "", "onBluetoothDisconnected", "", "context", "Landroid/content/Context;", "address", "PendingStart", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: compiled from: CastService.kt */
        @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0005HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0015"}, d2 = {"Lcom/carlink/cast/CastService$Companion$PendingStart;", "", "intent", "Landroid/content/Intent;", "startId", "", "<init>", "(Landroid/content/Intent;I)V", "getIntent", "()Landroid/content/Intent;", "getStartId", "()I", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
        static final /* data */ class PendingStart {
            private final Intent intent;
            private final int startId;

            public static /* synthetic */ PendingStart copy$default(PendingStart pendingStart, Intent intent, int i, int i2, Object obj) {
                if ((i2 & 1) != 0) {
                    intent = pendingStart.intent;
                }
                if ((i2 & 2) != 0) {
                    i = pendingStart.startId;
                }
                return pendingStart.copy(intent, i);
            }

            /* JADX INFO: renamed from: component1, reason: from getter */
            public final Intent getIntent() {
                return this.intent;
            }

            /* JADX INFO: renamed from: component2, reason: from getter */
            public final int getStartId() {
                return this.startId;
            }

            public final PendingStart copy(Intent intent, int startId) {
                Intrinsics.checkNotNullParameter(intent, "intent");
                return new PendingStart(intent, startId);
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                if (!(other instanceof PendingStart)) {
                    return false;
                }
                PendingStart pendingStart = (PendingStart) other;
                return Intrinsics.areEqual(this.intent, pendingStart.intent) && this.startId == pendingStart.startId;
            }

            public int hashCode() {
                return (this.intent.hashCode() * 31) + Integer.hashCode(this.startId);
            }

            public String toString() {
                return "PendingStart(intent=" + this.intent + ", startId=" + this.startId + ")";
            }

            public PendingStart(Intent intent, int startId) {
                Intrinsics.checkNotNullParameter(intent, "intent");
                this.intent = intent;
                this.startId = startId;
            }

            public final Intent getIntent() {
                return this.intent;
            }

            public final int getStartId() {
                return this.startId;
            }
        }

        private Companion() {
        }

        public final boolean isBusy() {
            boolean z;
            CastService service = CastService.instance;
            if (service == null) {
                return false;
            }
            synchronized (service.lifecycleLock) {
                z = (!service.sessionQueued && service.engine == null && service.pendingStart == null) ? false : true;
            }
            return z;
        }

        public final void onBluetoothDisconnected(Context context, String address) throws Throwable {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(address, "address");
            CastService service = CastService.instance;
            if (service == null) {
                return;
            }
            service.onBluetoothDisconnected(address);
        }
    }
}
