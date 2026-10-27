package com.carplay.adapter.carlife;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.SystemClock;
import com.baidu.carlife.mixing.MusicInfo;
import com.carlink.cast.NowPlayingPublisher;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: CarLifeMediaPublisher.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\u008a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0000\u0018\u0000 C2\u00060\u0001j\u0002`\u0002:\u0001CB7\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006\u0012\u0012\b\u0002\u0010\t\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u000b\u0018\u00010\n¢\u0006\u0004\b\f\u0010\rJ\u0006\u0010)\u001a\u00020\bJ\b\u0010*\u001a\u00020\bH\u0002J\u0014\u0010+\u001a\u0004\u0018\u00010 2\b\u0010,\u001a\u0004\u0018\u00010-H\u0002J\u0014\u0010.\u001a\u0004\u0018\u00010 2\b\u0010,\u001a\u0004\u0018\u00010-H\u0002J\u0010\u0010/\u001a\u00020\u00072\u0006\u00100\u001a\u00020 H\u0002J+\u00101\u001a\u00020\u00072\b\u0010,\u001a\u0004\u0018\u00010-2\u0012\u00102\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u000703\"\u00020\u0007H\u0002¢\u0006\u0002\u00104J\u0018\u00105\u001a\u0004\u0018\u0001062\f\u00107\u001a\b\u0012\u0004\u0012\u00020608H\u0002J\u0012\u00109\u001a\u00020\u00162\b\u0010,\u001a\u0004\u0018\u00010-H\u0002J\u0014\u0010:\u001a\u0004\u0018\u00010 2\b\u0010;\u001a\u0004\u0018\u00010\u000bH\u0002J\u001a\u0010<\u001a\u00020=2\u0006\u0010>\u001a\u00020\u00072\b\u0010?\u001a\u0004\u0018\u000106H\u0002J\u0010\u0010@\u001a\u00020\u001d2\u0006\u0010>\u001a\u00020\u0007H\u0002J\u0010\u0010A\u001a\u00020\u00072\u0006\u0010>\u001a\u00020\u0007H\u0002J\b\u0010B\u001a\u00020\bH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0018\u0010\t\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u000b\u0018\u00010\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\n \u000f*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0010\u001a\n \u000f*\u0004\u0018\u00010\u00110\u0011X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0013X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001aX\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u001c\u001a\u0004\u0018\u00010\u001dX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u001eR\u0010\u0010\u001f\u001a\u0004\u0018\u00010 X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u0004\u0018\u00010 X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u001aX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010(\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006D"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeMediaPublisher;", "Ljava/lang/AutoCloseable;", "Lkotlin/AutoCloseable;", "context", "Landroid/content/Context;", "log", "Lkotlin/Function1;", "", "", "fallback", "Lkotlin/Function0;", "Lcom/carplay/adapter/carlife/CarLifeMediaSnapshot;", "<init>", "(Landroid/content/Context;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;)V", "app", "kotlin.jvm.PlatformType", "sessions", "Landroid/media/session/MediaSessionManager;", "executor", "Ljava/util/concurrent/ScheduledExecutorService;", "artworkExecutor", "closed", "", "started", "lastKey", "lastPosition", "", "lastInfoSentAt", "fallbackArtworkHash", "", "Ljava/lang/Integer;", "fallbackArtworkBitmap", "Landroid/graphics/Bitmap;", "cachedArtworkUri", "cachedArtworkBitmap", "artworkDecodePending", "emptySessionLogged", "nowPlayingEmptyLogged", "Ljava/util/concurrent/atomic/AtomicBoolean;", "pollCount", "lastLoggedEndpoint", "start", "publish", "artwork", "metadata", "Landroid/media/MediaMetadata;", "artworkFromUri", "artworkKey", "bitmap", "firstString", "keys", "", "(Landroid/media/MediaMetadata;[Ljava/lang/String;)Ljava/lang/String;", "chooseController", "Landroid/media/session/MediaController;", "active", "", "hasUsableMetadata", "fallbackArtwork", "snapshot", "extractApplicationContext", "Lcom/carplay/adapter/carlife/MediaApplicationContext;", "packageName", "controller", "getApplicationPid", "getApplicationName", "close", "Companion", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarLifeMediaPublisher implements AutoCloseable {

    @Deprecated
    public static final String ACTION_MUSIC_INFO = "carlife_vehicle_music_info";

    @Deprecated
    public static final String ACTION_MUSIC_PROGRESS = "carlife_vehicle_music_progress";

    @Deprecated
    public static final String CARLIFE_PACKAGE = "com.baidu.carlife.honor";
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final long INFO_RETRY_INTERVAL_MS = 5000;
    private final Context app;
    private volatile boolean artworkDecodePending;
    private final ScheduledExecutorService artworkExecutor;
    private volatile Bitmap cachedArtworkBitmap;
    private volatile String cachedArtworkUri;
    private volatile boolean closed;
    private final Context context;
    private boolean emptySessionLogged;
    private final ScheduledExecutorService executor;
    private final Function0<CarLifeMediaSnapshot> fallback;
    private Bitmap fallbackArtworkBitmap;
    private Integer fallbackArtworkHash;
    private long lastInfoSentAt;
    private String lastKey;
    private String lastLoggedEndpoint;
    private long lastPosition;
    private final Function1<String, Unit> log;
    private final AtomicBoolean nowPlayingEmptyLogged;
    private long pollCount;
    private final MediaSessionManager sessions;
    private volatile boolean started;

    /* JADX WARN: Multi-variable type inference failed */
    public CarLifeMediaPublisher(Context context, Function1<? super String, Unit> log, Function0<CarLifeMediaSnapshot> function0) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(log, "log");
        this.context = context;
        this.log = log;
        this.fallback = function0;
        this.app = this.context.getApplicationContext();
        this.sessions = (MediaSessionManager) this.app.getSystemService(MediaSessionManager.class);
        ScheduledExecutorService scheduledExecutorServiceNewSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() { // from class: com.carplay.adapter.carlife.CarLifeMediaPublisher$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.ThreadFactory
            public final Thread newThread(Runnable runnable) {
                return CarLifeMediaPublisher.executor$lambda$1(runnable);
            }
        });
        Intrinsics.checkNotNullExpressionValue(scheduledExecutorServiceNewSingleThreadScheduledExecutor, "newSingleThreadScheduledExecutor(...)");
        this.executor = scheduledExecutorServiceNewSingleThreadScheduledExecutor;
        ScheduledExecutorService scheduledExecutorServiceNewSingleThreadScheduledExecutor2 = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() { // from class: com.carplay.adapter.carlife.CarLifeMediaPublisher$$ExternalSyntheticLambda1
            @Override // java.util.concurrent.ThreadFactory
            public final Thread newThread(Runnable runnable) {
                return CarLifeMediaPublisher.artworkExecutor$lambda$3(runnable);
            }
        });
        Intrinsics.checkNotNullExpressionValue(scheduledExecutorServiceNewSingleThreadScheduledExecutor2, "newSingleThreadScheduledExecutor(...)");
        this.artworkExecutor = scheduledExecutorServiceNewSingleThreadScheduledExecutor2;
        this.lastPosition = Long.MIN_VALUE;
        this.nowPlayingEmptyLogged = new AtomicBoolean();
    }

    public /* synthetic */ CarLifeMediaPublisher(Context context, Function1 function1, Function0 function0, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, function1, (i & 4) != 0 ? null : function0);
    }

    static final Thread executor$lambda$1(Runnable it) {
        Thread $this$executor_u24lambda_u241_u24lambda_u240 = new Thread(it, "carlife-media-publisher");
        $this$executor_u24lambda_u241_u24lambda_u240.setDaemon(true);
        return $this$executor_u24lambda_u241_u24lambda_u240;
    }

    static final Thread artworkExecutor$lambda$3(Runnable it) {
        Thread $this$artworkExecutor_u24lambda_u243_u24lambda_u242 = new Thread(it, "carlife-artwork-cache");
        $this$artworkExecutor_u24lambda_u243_u24lambda_u242.setDaemon(true);
        return $this$artworkExecutor_u24lambda_u243_u24lambda_u242;
    }

    public final void start() {
        Object objM159constructorimpl;
        if (this.closed || this.started) {
            return;
        }
        synchronized (this) {
            if (!this.closed && !this.started) {
                this.started = true;
                this.pollCount = 0L;
                this.log.invoke("[媒体] CarLife 媒体发布器启动: poll_interval=1s");
                try {
                    Result.Companion companion = Result.INSTANCE;
                    final CarLifeMediaPublisher $this$start_u24lambda_u246_u24lambda_u244 = this;
                    objM159constructorimpl = Result.m159constructorimpl($this$start_u24lambda_u246_u24lambda_u244.executor.scheduleWithFixedDelay(new Runnable() { // from class: com.carplay.adapter.carlife.CarLifeMediaPublisher$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.publish();
                        }
                    }, 0L, 1L, TimeUnit.SECONDS));
                } catch (Throwable th) {
                    Result.Companion companion2 = Result.INSTANCE;
                    objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
                }
                Throwable it = Result.m162exceptionOrNullimpl(objM159constructorimpl);
                if (it != null) {
                    this.started = false;
                    if (!this.closed) {
                        this.log.invoke("[媒体] CarLife 媒体发布器启动失败：" + it.getMessage());
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:106:0x01d5  */
    /* JADX WARN: Code duplicated, block: B:108:0x01d8  */
    /* JADX WARN: Code duplicated, block: B:109:0x01da  */
    /* JADX WARN: Code duplicated, block: B:113:0x01e4  */
    /* JADX WARN: Code duplicated, block: B:117:0x01ea  */
    /* JADX WARN: Code duplicated, block: B:120:0x01ee  */
    /* JADX WARN: Code duplicated, block: B:121:0x01f9  */
    /* JADX WARN: Code duplicated, block: B:123:0x01fd  */
    /* JADX WARN: Code duplicated, block: B:124:0x0206  */
    /* JADX WARN: Code duplicated, block: B:129:0x0230  */
    /* JADX WARN: Code duplicated, block: B:132:0x0239  */
    /* JADX WARN: Code duplicated, block: B:133:0x0240  */
    /* JADX WARN: Code duplicated, block: B:138:0x024e A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:139:0x0250  */
    /* JADX WARN: Code duplicated, block: B:140:0x0255  */
    /* JADX WARN: Code duplicated, block: B:142:0x0258  */
    /* JADX WARN: Code duplicated, block: B:145:0x0260  */
    /* JADX WARN: Code duplicated, block: B:159:0x0295  */
    /* JADX WARN: Code duplicated, block: B:162:0x029d  */
    /* JADX WARN: Code duplicated, block: B:164:0x02a7  */
    /* JADX WARN: Code duplicated, block: B:166:0x02af  */
    /* JADX WARN: Code duplicated, block: B:168:0x02b7  */
    /* JADX WARN: Code duplicated, block: B:169:0x02c1  */
    /* JADX WARN: Code duplicated, block: B:171:0x02c5  */
    /* JADX WARN: Code duplicated, block: B:174:0x0328  */
    /* JADX WARN: Code duplicated, block: B:176:0x033c  */
    /* JADX WARN: Code duplicated, block: B:177:0x0382  */
    /* JADX WARN: Code duplicated, block: B:178:0x0389  */
    /* JADX WARN: Code duplicated, block: B:185:0x03bb  */
    /* JADX WARN: Code duplicated, block: B:187:0x0432  */
    /* JADX WARN: Code duplicated, block: B:194:0x0460  */
    /* JADX WARN: Code duplicated, block: B:197:0x048f  */
    /* JADX WARN: Code duplicated, block: B:200:0x049a  */
    /* JADX WARN: Code duplicated, block: B:203:0x04a6  */
    /* JADX WARN: Code duplicated, block: B:204:0x04aa  */
    /* JADX WARN: Code duplicated, block: B:207:0x04fa  */
    /* JADX WARN: Code duplicated, block: B:208:0x04fd  */
    /* JADX WARN: Code duplicated, block: B:222:0x05a0  */
    /* JADX WARN: Code duplicated, block: B:230:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:232:? A[RETURN, SYNTHETIC] */
    public final void publish() {
        Object objM159constructorimpl;
        MediaController controller;
        long jCoerceAtLeast;
        long duration;
        boolean z;
        boolean z2;
        boolean playing;
        long duration2;
        long basePosition;
        long position;
        long duration3;
        long duration4;
        long boundedPosition;
        String packageName;
        String source;
        Bitmap bitmapArtwork;
        Bitmap cover;
        boolean z3;
        boolean hasMetadata;
        AtomicBoolean atomicBoolean;
        String strArtworkKey;
        String key;
        long duration5;
        long now;
        boolean infoChanged;
        String coverKey;
        String lyrics;
        long now2;
        Intent intent;
        Object objM159constructorimpl2;
        Throwable it;
        String str;
        String str2;
        String str3;
        String str4;
        boolean z4;
        long boundedPosition2;
        Object objM159constructorimpl3;
        Throwable it2;
        MediaApplicationContext appContext;
        String str5;
        String coverKey2;
        boolean z5;
        if (this.closed) {
            return;
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLifeMediaPublisher $this$publish_u24lambda_u248 = this;
            ComponentName component = new ComponentName($this$publish_u24lambda_u248.app, "com.carlink.cast.MediaListenerService");
            MediaSessionManager mediaSessionManager = $this$publish_u24lambda_u248.sessions;
            List<MediaController> activeSessions = mediaSessionManager != null ? mediaSessionManager.getActiveSessions(component) : null;
            if (activeSessions == null) {
                activeSessions = CollectionsKt.emptyList();
            }
            List<MediaController> list = activeSessions;
            if (list.isEmpty()) {
                MediaSessionManager mediaSessionManager2 = $this$publish_u24lambda_u248.sessions;
                List<MediaController> activeSessions2 = mediaSessionManager2 != null ? mediaSessionManager2.getActiveSessions(null) : null;
                if (activeSessions2 == null) {
                    activeSessions2 = CollectionsKt.emptyList();
                }
                list = activeSessions2;
            }
            objM159constructorimpl = Result.m159constructorimpl(list);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        Throwable it3 = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (it3 != null) {
            Function1<String, Unit> function1 = this.log;
            String message = it3.getMessage();
            if (message == null) {
                message = it3.getClass().getSimpleName();
            }
            function1.invoke("CarLife 媒体读取失败：" + message);
            objM159constructorimpl = CollectionsKt.emptyList();
        }
        List<MediaController> list2 = (List) objM159constructorimpl;
        if (!list2.isEmpty()) {
            this.emptySessionLogged = false;
        } else if (!this.emptySessionLogged) {
            this.emptySessionLogged = true;
            this.log.invoke("CarLife 媒体没有可读 MediaSession；请开启通知使用权并开始播放");
        }
        MediaController controller2 = chooseController(list2);
        MediaMetadata metadata = controller2 != null ? controller2.getMetadata() : null;
        PlaybackState playback = controller2 != null ? controller2.getPlaybackState() : null;
        Function0<CarLifeMediaSnapshot> function0 = this.fallback;
        CarLifeMediaSnapshot fallbackSnapshot = function0 != null ? function0.invoke() : null;
        String strFirstString = firstString(metadata, "android.media.metadata.TITLE", "android.media.metadata.DISPLAY_TITLE");
        if (StringsKt.isBlank(strFirstString)) {
            String title = fallbackSnapshot != null ? fallbackSnapshot.getTitle() : null;
            if (title == null) {
                title = "";
            }
            strFirstString = title;
        }
        String title2 = strFirstString;
        String strFirstString2 = firstString(metadata, "android.media.metadata.ARTIST", "android.media.metadata.ALBUM_ARTIST", "android.media.metadata.AUTHOR");
        if (StringsKt.isBlank(strFirstString2)) {
            String artist = fallbackSnapshot != null ? fallbackSnapshot.getArtist() : null;
            if (artist == null) {
                artist = "";
            }
            strFirstString2 = artist;
        }
        String artist2 = strFirstString2;
        String strFirstString3 = firstString(metadata, "android.media.metadata.ALBUM", "android.media.metadata.DISPLAY_SUBTITLE");
        if (StringsKt.isBlank(strFirstString3)) {
            String album = fallbackSnapshot != null ? fallbackSnapshot.getAlbum() : null;
            if (album == null) {
                album = "";
            }
            strFirstString3 = album;
        }
        String album2 = strFirstString3;
        String strFirstString4 = firstString(metadata, NowPlayingPublisher.METADATA_KEY_LYRICS, "android.media.metadata.LYRIC", "ucar.media.metadata.LYRICS_WHOLE", "com.tencent.qqmusic.metadata.LYRICS");
        if (StringsKt.isBlank(strFirstString4)) {
            String lyrics2 = fallbackSnapshot != null ? fallbackSnapshot.getLyrics() : null;
            if (lyrics2 == null) {
                lyrics2 = "";
            }
            strFirstString4 = lyrics2;
        }
        String lyrics3 = strFirstString4;
        long j = 0;
        if (metadata != null) {
            controller = controller2;
            Long lValueOf = Long.valueOf(RangesKt.coerceAtLeast(metadata.getLong("android.media.metadata.DURATION"), 0L));
            if (!(lValueOf.longValue() > 0)) {
                lValueOf = null;
            }
            if (lValueOf != null) {
                jCoerceAtLeast = lValueOf.longValue();
            }
            duration = jCoerceAtLeast;
            if (playback == null && playback.getState() == 3) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                if (playback == null) {
                    if (fallbackSnapshot == null && fallbackSnapshot.getPlaying()) {
                        z5 = true;
                    } else {
                        z5 = false;
                    }
                    z2 = z5;
                }
            }
            playing = z2;
            if (playback != null) {
                duration2 = duration;
                basePosition = RangesKt.coerceAtLeast(playback.getPosition(), 0L);
            } else {
                duration2 = duration;
                if (fallbackSnapshot != null) {
                    basePosition = RangesKt.coerceAtLeast(fallbackSnapshot.getPositionMs(), 0L);
                } else {
                    basePosition = 0;
                }
            }
            if (playing || playback == null) {
                position = basePosition;
            } else {
                long basePosition2 = basePosition;
                long basePosition3 = SystemClock.elapsedRealtime() - playback.getLastPositionUpdateTime();
                j = 0;
                position = RangesKt.coerceAtLeast(basePosition2 + ((long) (RangesKt.coerceAtLeast(basePosition3, 0L) * ((double) playback.getPlaybackSpeed()))), 0L);
            }
            if (duration2 > j) {
                duration3 = duration2;
                duration4 = RangesKt.coerceAtMost(position, duration3);
            } else {
                duration3 = duration2;
                duration4 = position;
            }
            boundedPosition = duration4;
            if (controller != null || (packageName = controller.getPackageName()) == null) {
                if (fallbackSnapshot != null) {
                    packageName = fallbackSnapshot.getPackageName();
                } else {
                    packageName = null;
                }
                if (packageName == null) {
                    packageName = "";
                }
            }
            source = packageName;
            bitmapArtwork = artwork(metadata);
            if (bitmapArtwork == null) {
                bitmapArtwork = fallbackArtwork(fallbackSnapshot);
            }
            cover = bitmapArtwork;
            if (!StringsKt.isBlank(title2) && StringsKt.isBlank(artist2) && StringsKt.isBlank(album2) && StringsKt.isBlank(lyrics3) && duration3 <= 0 && cover == null) {
                z3 = false;
            } else {
                z3 = true;
            }
            hasMetadata = z3;
            atomicBoolean = this.nowPlayingEmptyLogged;
            if (!hasMetadata) {
                if (atomicBoolean.compareAndSet(false, true)) {
                    this.log.invoke("CarLife 暂无可发布媒体元数据，跳过空快照，避免覆盖车机已有媒体卡");
                    return;
                }
                return;
            }
            atomicBoolean.set(false);
            if (cover != null) {
                strArtworkKey = artworkKey(cover);
            } else {
                strArtworkKey = null;
            }
            if (strArtworkKey == null) {
                strArtworkKey = "";
            }
            String coverKey3 = strArtworkKey;
            key = source + "\u0000" + title2 + "\u0000" + artist2 + "\u0000" + album2 + "\u0000" + duration3 + "\u0000" + lyrics3 + "\u0000" + coverKey3;
            duration5 = duration3;
            now = SystemClock.elapsedRealtime();
            infoChanged = !Intrinsics.areEqual(key, this.lastKey);
            this.pollCount++;
            if (infoChanged) {
                appContext = extractApplicationContext(source, controller);
                str5 = this.lastLoggedEndpoint;
                coverKey2 = appContext.toString();
                if (!Intrinsics.areEqual(str5, coverKey2)) {
                    this.lastLoggedEndpoint = appContext.toString();
                    this.log.invoke("[媒体] 轮询周期 #" + this.pollCount + " - 媒体变化");
                    this.log.invoke("  [Endpoint] " + appContext);
                }
            }
            coverKey = "putExtra(...)";
            if (!infoChanged || now - this.lastInfoSentAt >= 5000) {
                this.lastKey = key;
                this.lastInfoSentAt = now;
                lyrics = lyrics3;
                MusicInfo info = new MusicInfo(title2, artist2, source, duration5, 0, 0);
                now2 = duration5;
                CarLifeBridge.INSTANCE.publishMusic(info);
                intent = new Intent(ACTION_MUSIC_INFO).setPackage("com.baidu.carlife.honor").putExtra("song", title2).putExtra("artist", artist2).putExtra("album", album2).putExtra("duration", (int) RangesKt.coerceAtMost(now2, 2147483647L)).putExtra("index", 0).putExtra("listnum", 0).putExtra("mode", 0).putExtra("mediaUrl", source).putExtra("mediaLyric", lyrics).putExtra("coverUrl", "");
                coverKey = "putExtra(...)";
                Intrinsics.checkNotNullExpressionValue(intent, coverKey);
                if (cover != null) {
                    intent.putExtra("cover", cover);
                }
                try {
                    Result.Companion companion3 = Result.INSTANCE;
                    CarLifeMediaPublisher $this$publish_u24lambda_u2416 = this;
                    $this$publish_u24lambda_u2416.app.sendBroadcast(intent);
                    objM159constructorimpl2 = Result.m159constructorimpl(Unit.INSTANCE);
                } catch (Throwable th2) {
                    Result.Companion companion4 = Result.INSTANCE;
                    objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th2));
                }
                it = Result.m162exceptionOrNullimpl(objM159constructorimpl2);
                if (it != null) {
                    this.log.invoke("CarLife 媒体信息广播失败：" + it.getMessage());
                }
                Function1<String, Unit> function2 = this.log;
                str = title2;
                str2 = "<空>";
                if (StringsKt.isBlank(str)) {
                    str = "<空>";
                }
                str3 = artist2;
                if (StringsKt.isBlank(str3)) {
                    str3 = "<空>";
                }
                str4 = album2;
                if (!StringsKt.isBlank(str4)) {
                    str2 = str4;
                }
                function2.invoke("  [Metadata] title=" + ((Object) str) + " artist=" + ((Object) str3) + " album=" + ((Object) str2) + " duration=" + now2 + "ms");
                Function1<String, Unit> function3 = this.log;
                boolean z6 = !StringsKt.isBlank(lyrics);
                if (cover != null) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                boundedPosition2 = boundedPosition;
                function3.invoke("  [Playback] playing=" + playing + " position=" + boundedPosition2 + " ms lyrics=" + z6 + " cover=" + z4);
                this.log.invoke("  [Status] 发送 MusicInfo via AIDL + Intent via 广播");
            } else {
                lyrics = lyrics3;
                boundedPosition2 = boundedPosition;
                now2 = duration5;
            }
            if (boundedPosition2 == this.lastPosition || infoChanged) {
                this.lastPosition = boundedPosition2;
                CarLifeBridge.INSTANCE.publishMusicProgress(boundedPosition2, lyrics);
                Intent progress = new Intent(ACTION_MUSIC_PROGRESS).setPackage("com.baidu.carlife.honor").putExtra("duration", (int) RangesKt.coerceAtMost(now2, 2147483647L)).putExtra("curTime", (int) RangesKt.coerceAtMost(boundedPosition2, 2147483647L)).putExtra("mediaLyric", lyrics);
                Intrinsics.checkNotNullExpressionValue(progress, coverKey);
                try {
                    Result.Companion companion5 = Result.INSTANCE;
                    CarLifeMediaPublisher $this$publish_u24lambda_u2421 = this;
                    $this$publish_u24lambda_u2421.app.sendBroadcast(progress);
                    objM159constructorimpl3 = Result.m159constructorimpl(Unit.INSTANCE);
                } catch (Throwable th3) {
                    Result.Companion companion6 = Result.INSTANCE;
                    objM159constructorimpl3 = Result.m159constructorimpl(ResultKt.createFailure(th3));
                }
                it2 = Result.m162exceptionOrNullimpl(objM159constructorimpl3);
                if (it2 != null) {
                    this.log.invoke("CarLife 媒体进度广播失败：" + it2.getMessage());
                }
            }
            return;
        }
        controller = controller2;
        jCoerceAtLeast = fallbackSnapshot != null ? RangesKt.coerceAtLeast(fallbackSnapshot.getDurationMs(), 0L) : 0L;
        duration = jCoerceAtLeast;
        if (playback == null) {
            z = false;
        } else {
            z = false;
        }
        if (z) {
            if (playback == null) {
                if (fallbackSnapshot == null) {
                    z5 = false;
                } else {
                    z5 = false;
                }
                if (z5) {
                }
            }
        }
        playing = z2;
        if (playback != null) {
            duration2 = duration;
            basePosition = RangesKt.coerceAtLeast(playback.getPosition(), 0L);
        } else {
            duration2 = duration;
            if (fallbackSnapshot != null) {
                basePosition = RangesKt.coerceAtLeast(fallbackSnapshot.getPositionMs(), 0L);
            } else {
                basePosition = 0;
            }
        }
        if (playing) {
            position = basePosition;
        } else {
            position = basePosition;
        }
        if (duration2 > j) {
            duration3 = duration2;
            duration4 = RangesKt.coerceAtMost(position, duration3);
        } else {
            duration3 = duration2;
            duration4 = position;
        }
        boundedPosition = duration4;
        if (controller != null) {
            if (fallbackSnapshot != null) {
                packageName = fallbackSnapshot.getPackageName();
            } else {
                packageName = null;
            }
            if (packageName == null) {
                packageName = "";
            }
        } else {
            if (fallbackSnapshot != null) {
                packageName = fallbackSnapshot.getPackageName();
            } else {
                packageName = null;
            }
            if (packageName == null) {
                packageName = "";
            }
        }
        source = packageName;
        bitmapArtwork = artwork(metadata);
        if (bitmapArtwork == null) {
            bitmapArtwork = fallbackArtwork(fallbackSnapshot);
        }
        cover = bitmapArtwork;
        if (!StringsKt.isBlank(title2)) {
            z3 = true;
        } else {
            z3 = true;
        }
        hasMetadata = z3;
        atomicBoolean = this.nowPlayingEmptyLogged;
        if (!hasMetadata) {
            if (atomicBoolean.compareAndSet(false, true)) {
                this.log.invoke("CarLife 暂无可发布媒体元数据，跳过空快照，避免覆盖车机已有媒体卡");
                return;
            }
            return;
        }
        atomicBoolean.set(false);
        if (cover != null) {
            strArtworkKey = artworkKey(cover);
        } else {
            strArtworkKey = null;
        }
        if (strArtworkKey == null) {
            strArtworkKey = "";
        }
        String coverKey4 = strArtworkKey;
        key = source + "\u0000" + title2 + "\u0000" + artist2 + "\u0000" + album2 + "\u0000" + duration3 + "\u0000" + lyrics3 + "\u0000" + coverKey4;
        duration5 = duration3;
        now = SystemClock.elapsedRealtime();
        infoChanged = !Intrinsics.areEqual(key, this.lastKey);
        this.pollCount++;
        if (infoChanged) {
            appContext = extractApplicationContext(source, controller);
            str5 = this.lastLoggedEndpoint;
            coverKey2 = appContext.toString();
            if (!Intrinsics.areEqual(str5, coverKey2)) {
                this.lastLoggedEndpoint = appContext.toString();
                this.log.invoke("[媒体] 轮询周期 #" + this.pollCount + " - 媒体变化");
                this.log.invoke("  [Endpoint] " + appContext);
            }
        }
        coverKey = "putExtra(...)";
        if (infoChanged) {
            this.lastKey = key;
            this.lastInfoSentAt = now;
            lyrics = lyrics3;
            MusicInfo info2 = new MusicInfo(title2, artist2, source, duration5, 0, 0);
            now2 = duration5;
            CarLifeBridge.INSTANCE.publishMusic(info2);
            intent = new Intent(ACTION_MUSIC_INFO).setPackage("com.baidu.carlife.honor").putExtra("song", title2).putExtra("artist", artist2).putExtra("album", album2).putExtra("duration", (int) RangesKt.coerceAtMost(now2, 2147483647L)).putExtra("index", 0).putExtra("listnum", 0).putExtra("mode", 0).putExtra("mediaUrl", source).putExtra("mediaLyric", lyrics).putExtra("coverUrl", "");
            coverKey = "putExtra(...)";
            Intrinsics.checkNotNullExpressionValue(intent, coverKey);
            if (cover != null) {
                intent.putExtra("cover", cover);
            }
            Result.Companion companion7 = Result.INSTANCE;
            CarLifeMediaPublisher $this$publish_u24lambda_u2417 = this;
            $this$publish_u24lambda_u2417.app.sendBroadcast(intent);
            objM159constructorimpl2 = Result.m159constructorimpl(Unit.INSTANCE);
            it = Result.m162exceptionOrNullimpl(objM159constructorimpl2);
            if (it != null) {
                this.log.invoke("CarLife 媒体信息广播失败：" + it.getMessage());
            }
            Function1<String, Unit> function4 = this.log;
            str = title2;
            str2 = "<空>";
            if (StringsKt.isBlank(str)) {
                str = "<空>";
            }
            str3 = artist2;
            if (StringsKt.isBlank(str3)) {
                str3 = "<空>";
            }
            str4 = album2;
            if (!StringsKt.isBlank(str4)) {
                str2 = str4;
            }
            function4.invoke("  [Metadata] title=" + ((Object) str) + " artist=" + ((Object) str3) + " album=" + ((Object) str2) + " duration=" + now2 + "ms");
            Function1<String, Unit> function5 = this.log;
            boolean z7 = !StringsKt.isBlank(lyrics);
            if (cover != null) {
                z4 = true;
            } else {
                z4 = false;
            }
            boundedPosition2 = boundedPosition;
            function5.invoke("  [Playback] playing=" + playing + " position=" + boundedPosition2 + " ms lyrics=" + z7 + " cover=" + z4);
            this.log.invoke("  [Status] 发送 MusicInfo via AIDL + Intent via 广播");
        } else {
            this.lastKey = key;
            this.lastInfoSentAt = now;
            lyrics = lyrics3;
            MusicInfo info3 = new MusicInfo(title2, artist2, source, duration5, 0, 0);
            now2 = duration5;
            CarLifeBridge.INSTANCE.publishMusic(info3);
            intent = new Intent(ACTION_MUSIC_INFO).setPackage("com.baidu.carlife.honor").putExtra("song", title2).putExtra("artist", artist2).putExtra("album", album2).putExtra("duration", (int) RangesKt.coerceAtMost(now2, 2147483647L)).putExtra("index", 0).putExtra("listnum", 0).putExtra("mode", 0).putExtra("mediaUrl", source).putExtra("mediaLyric", lyrics).putExtra("coverUrl", "");
            coverKey = "putExtra(...)";
            Intrinsics.checkNotNullExpressionValue(intent, coverKey);
            if (cover != null) {
                intent.putExtra("cover", cover);
            }
            Result.Companion companion8 = Result.INSTANCE;
            CarLifeMediaPublisher $this$publish_u24lambda_u2418 = this;
            $this$publish_u24lambda_u2418.app.sendBroadcast(intent);
            objM159constructorimpl2 = Result.m159constructorimpl(Unit.INSTANCE);
            it = Result.m162exceptionOrNullimpl(objM159constructorimpl2);
            if (it != null) {
                this.log.invoke("CarLife 媒体信息广播失败：" + it.getMessage());
            }
            Function1<String, Unit> function6 = this.log;
            str = title2;
            str2 = "<空>";
            if (StringsKt.isBlank(str)) {
                str = "<空>";
            }
            str3 = artist2;
            if (StringsKt.isBlank(str3)) {
                str3 = "<空>";
            }
            str4 = album2;
            if (!StringsKt.isBlank(str4)) {
                str2 = str4;
            }
            function6.invoke("  [Metadata] title=" + ((Object) str) + " artist=" + ((Object) str3) + " album=" + ((Object) str2) + " duration=" + now2 + "ms");
            Function1<String, Unit> function7 = this.log;
            boolean z8 = !StringsKt.isBlank(lyrics);
            if (cover != null) {
                z4 = true;
            } else {
                z4 = false;
            }
            boundedPosition2 = boundedPosition;
            function7.invoke("  [Playback] playing=" + playing + " position=" + boundedPosition2 + " ms lyrics=" + z8 + " cover=" + z4);
            this.log.invoke("  [Status] 发送 MusicInfo via AIDL + Intent via 广播");
        }
        if (boundedPosition2 == this.lastPosition) {
        }
        this.lastPosition = boundedPosition2;
        CarLifeBridge.INSTANCE.publishMusicProgress(boundedPosition2, lyrics);
        Intent progress2 = new Intent(ACTION_MUSIC_PROGRESS).setPackage("com.baidu.carlife.honor").putExtra("duration", (int) RangesKt.coerceAtMost(now2, 2147483647L)).putExtra("curTime", (int) RangesKt.coerceAtMost(boundedPosition2, 2147483647L)).putExtra("mediaLyric", lyrics);
        Intrinsics.checkNotNullExpressionValue(progress2, coverKey);
        Result.Companion companion9 = Result.INSTANCE;
        CarLifeMediaPublisher $this$publish_u24lambda_u2422 = this;
        $this$publish_u24lambda_u2422.app.sendBroadcast(progress2);
        objM159constructorimpl3 = Result.m159constructorimpl(Unit.INSTANCE);
        it2 = Result.m162exceptionOrNullimpl(objM159constructorimpl3);
        if (it2 != null) {
            this.log.invoke("CarLife 媒体进度广播失败：" + it2.getMessage());
        }
    }

    private final Bitmap artwork(MediaMetadata metadata) {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        Bitmap bitmap;
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLifeMediaPublisher $this$artwork_u24lambda_u2423 = this;
            if (metadata == null || (bitmap = metadata.getBitmap("android.media.metadata.ART")) == null) {
                bitmap = metadata != null ? metadata.getBitmap("android.media.metadata.ALBUM_ART") : null;
                if (bitmap == null) {
                    bitmap = $this$artwork_u24lambda_u2423.artworkFromUri(metadata);
                }
            }
            objM159constructorimpl = Result.m159constructorimpl(bitmap);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = null;
        }
        Bitmap source = (Bitmap) objM159constructorimpl;
        if (source == null) {
            return null;
        }
        if (source.getWidth() <= 200 && source.getHeight() <= 200) {
            return source;
        }
        try {
            Result.Companion companion3 = Result.INSTANCE;
            CarLifeMediaPublisher carLifeMediaPublisher = this;
            objM159constructorimpl2 = Result.m159constructorimpl(Bitmap.createScaledBitmap(source, 200, 200, true));
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        return (Bitmap) (Result.m165isFailureimpl(objM159constructorimpl2) ? null : objM159constructorimpl2);
    }

    private final Bitmap artworkFromUri(MediaMetadata metadata) {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        String string;
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLifeMediaPublisher carLifeMediaPublisher = this;
            if (metadata == null || (string = metadata.getString("android.media.metadata.ALBUM_ART_URI")) == null) {
                string = metadata != null ? metadata.getString("android.media.metadata.ART_URI") : null;
            }
            objM159constructorimpl = Result.m159constructorimpl(string);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = null;
        }
        final String value = (String) objM159constructorimpl;
        if (value == null) {
            value = "";
        }
        if (StringsKt.isBlank(value)) {
            return null;
        }
        Bitmap it = this.cachedArtworkBitmap;
        if (it != null && Intrinsics.areEqual(this.cachedArtworkUri, value) && !it.isRecycled()) {
            return it;
        }
        if (!Intrinsics.areEqual(this.cachedArtworkUri, value)) {
            this.cachedArtworkUri = value;
            this.cachedArtworkBitmap = null;
        }
        if (!this.artworkDecodePending) {
            this.artworkDecodePending = true;
            try {
                Result.Companion companion3 = Result.INSTANCE;
                final CarLifeMediaPublisher $this$artworkFromUri_u24lambda_u2433 = this;
                $this$artworkFromUri_u24lambda_u2433.artworkExecutor.execute(new Runnable() { // from class: com.carplay.adapter.carlife.CarLifeMediaPublisher$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        CarLifeMediaPublisher.artworkFromUri$lambda$33$lambda$32(this.f$0, value);
                    }
                });
                objM159constructorimpl2 = Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th2) {
                Result.Companion companion4 = Result.INSTANCE;
                objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th2));
            }
            Throwable it2 = Result.m162exceptionOrNullimpl(objM159constructorimpl2);
            if (it2 != null) {
                this.artworkDecodePending = false;
                Function1<String, Unit> function1 = this.log;
                String message = it2.getMessage();
                if (message == null) {
                    message = it2.getClass().getSimpleName();
                }
                function1.invoke("CarLife 封面 URI 异步读取失败：" + message);
            }
        }
        return null;
    }

    static final void artworkFromUri$lambda$33$lambda$32(CarLifeMediaPublisher $this_runCatching, String $value) {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        Bitmap bitmapDecodeStream;
        try {
            Result.Companion companion = Result.INSTANCE;
            InputStream inputStreamOpenInputStream = $this_runCatching.app.getContentResolver().openInputStream(Uri.parse($value));
            if (inputStreamOpenInputStream != null) {
                InputStream inputStream = inputStreamOpenInputStream;
                try {
                    InputStream p0 = inputStream;
                    bitmapDecodeStream = BitmapFactory.decodeStream(p0);
                    CloseableKt.closeFinally(inputStream, null);
                } catch (Throwable th) {
                    try {
                        throw th;
                    } catch (Throwable th2) {
                        CloseableKt.closeFinally(inputStream, th);
                        throw th2;
                    }
                }
            } else {
                bitmapDecodeStream = null;
            }
            objM159constructorimpl = Result.m159constructorimpl(bitmapDecodeStream);
        } catch (Throwable th3) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th3));
        }
        Bitmap decoded = (Bitmap) (Result.m165isFailureimpl(objM159constructorimpl) ? null : objM159constructorimpl);
        Bitmap old = $this_runCatching.cachedArtworkBitmap;
        if (Intrinsics.areEqual($this_runCatching.cachedArtworkUri, $value)) {
            $this_runCatching.cachedArtworkBitmap = decoded;
            if (old != null && old != decoded) {
                try {
                    Result.Companion companion3 = Result.INSTANCE;
                    old.recycle();
                    Result.m159constructorimpl(Unit.INSTANCE);
                } catch (Throwable th4) {
                    Result.Companion companion4 = Result.INSTANCE;
                    Result.m159constructorimpl(ResultKt.createFailure(th4));
                }
            }
        } else if (decoded != null) {
            try {
                Result.Companion companion5 = Result.INSTANCE;
                decoded.recycle();
                objM159constructorimpl2 = Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th5) {
                Result.Companion companion6 = Result.INSTANCE;
                objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th5));
            }
            Result.m158boximpl(objM159constructorimpl2);
        }
        $this_runCatching.artworkDecodePending = false;
    }

    private final String artworkKey(Bitmap bitmap) {
        Bitmap bitmap2;
        Throwable th;
        Object objM159constructorimpl;
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLifeMediaPublisher carLifeMediaPublisher = this;
            int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
            bitmap2 = bitmap;
            try {
                bitmap2.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
                int hash = 1;
                for (int element$iv : pixels) {
                    hash = (hash * 31) + element$iv;
                }
                objM159constructorimpl = Result.m159constructorimpl(bitmap2.getWidth() + "x" + bitmap2.getHeight() + ":" + hash);
            } catch (Throwable th2) {
                th = th2;
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
        } catch (Throwable th3) {
            bitmap2 = bitmap;
            th = th3;
        }
        String str = bitmap2.getWidth() + "x" + bitmap2.getHeight();
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = str;
        }
        return (String) objM159constructorimpl;
    }

    private final String firstString(final MediaMetadata metadata, String... keys) {
        Object element$iv;
        String it;
        Sequence $this$firstOrNull$iv = SequencesKt.mapNotNull(ArraysKt.asSequence(keys), new Function1() { // from class: com.carplay.adapter.carlife.CarLifeMediaPublisher$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return CarLifeMediaPublisher.firstString$lambda$38(this.f$0, metadata, (String) obj);
            }
        });
        Iterator it2 = $this$firstOrNull$iv.iterator();
        do {
            if (!it2.hasNext()) {
                element$iv = null;
                break;
            }
            element$iv = it2.next();
            it = (String) element$iv;
        } while (StringsKt.isBlank(it));
        String str = (String) element$iv;
        return str == null ? "" : str;
    }

    static final String firstString$lambda$38(CarLifeMediaPublisher this$0, MediaMetadata $metadata, String key) {
        Object objM159constructorimpl;
        Intrinsics.checkNotNullParameter(key, "key");
        try {
            Result.Companion companion = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl($metadata != null ? $metadata.getString(key) : null);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        return (String) (Result.m165isFailureimpl(objM159constructorimpl) ? null : objM159constructorimpl);
    }

    private final MediaController chooseController(List<MediaController> active) {
        Object obj;
        Object element$iv;
        MediaController it;
        List<MediaController> $this$filter$iv = active;
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv : $this$filter$iv) {
            MediaController it2 = (MediaController) element$iv$iv;
            PlaybackState playbackState = it2.getPlaybackState();
            boolean z = false;
            if (playbackState != null && playbackState.getState() == 3) {
                z = true;
            }
            if (z) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        List playing = (List) destination$iv$iv;
        List $this$firstOrNull$iv = playing;
        Iterator it3 = $this$firstOrNull$iv.iterator();
        do {
            obj = null;
            if (!it3.hasNext()) {
                element$iv = null;
                break;
            }
            element$iv = it3.next();
            it = (MediaController) element$iv;
        } while (!hasUsableMetadata(it.getMetadata()));
        MediaController mediaController = (MediaController) element$iv;
        if (mediaController != null) {
            return mediaController;
        }
        List<MediaController> $this$firstOrNull$iv2 = active;
        for (Object element$iv2 : $this$firstOrNull$iv2) {
            MediaController it4 = (MediaController) element$iv2;
            if (hasUsableMetadata(it4.getMetadata())) {
                obj = element$iv2;
                break;
            }
        }
        MediaController mediaController2 = (MediaController) obj;
        if (mediaController2 != null) {
            return mediaController2;
        }
        MediaController mediaController3 = (MediaController) CollectionsKt.firstOrNull(playing);
        return mediaController3 == null ? (MediaController) CollectionsKt.firstOrNull((List) active) : mediaController3;
    }

    private final boolean hasUsableMetadata(MediaMetadata metadata) {
        boolean text;
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        if (metadata == null) {
            return false;
        }
        Iterable $this$any$iv = CollectionsKt.listOf((Object[]) new String[]{"android.media.metadata.TITLE", "android.media.metadata.DISPLAY_TITLE", "android.media.metadata.ARTIST", "android.media.metadata.ALBUM", "android.media.metadata.DISPLAY_SUBTITLE"});
        if (!($this$any$iv instanceof Collection) || !((Collection) $this$any$iv).isEmpty()) {
            Iterator it = $this$any$iv.iterator();
            while (true) {
                if (!it.hasNext()) {
                    text = false;
                    break;
                }
                Object element$iv = it.next();
                String key = (String) element$iv;
                try {
                    Result.Companion companion = Result.INSTANCE;
                    CarLifeMediaPublisher carLifeMediaPublisher = this;
                    String string = metadata.getString(key);
                    if (string == null) {
                        string = "";
                    }
                    objM159constructorimpl = Result.m159constructorimpl(Boolean.valueOf(!StringsKt.isBlank(string)));
                } catch (Throwable th) {
                    Result.Companion companion2 = Result.INSTANCE;
                    objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
                }
                if (Result.m165isFailureimpl(objM159constructorimpl)) {
                    objM159constructorimpl = false;
                }
                if (((Boolean) objM159constructorimpl).booleanValue()) {
                    text = true;
                    break;
                }
            }
        } else {
            text = false;
        }
        try {
            Result.Companion companion3 = Result.INSTANCE;
            CarLifeMediaPublisher $this$hasUsableMetadata_u24lambda_u2445 = this;
            Bitmap bitmap = metadata.getBitmap("android.media.metadata.ART");
            if (bitmap == null && (bitmap = metadata.getBitmap("android.media.metadata.ALBUM_ART")) == null) {
                bitmap = $this$hasUsableMetadata_u24lambda_u2445.artworkFromUri(metadata);
            }
            objM159constructorimpl2 = Result.m159constructorimpl(bitmap);
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl2)) {
            objM159constructorimpl2 = null;
        }
        Bitmap artwork = (Bitmap) objM159constructorimpl2;
        return text || artwork != null;
    }

    private final Bitmap fallbackArtwork(CarLifeMediaSnapshot snapshot) {
        byte[] bytes;
        Object objM159constructorimpl;
        Unit unit;
        Bitmap it;
        if (snapshot != null && (it = snapshot.getArtwork()) != null) {
            return it;
        }
        if (snapshot == null || (bytes = snapshot.getArtworkBytes()) == null) {
            return null;
        }
        int hash = Arrays.hashCode(bytes);
        Integer num = this.fallbackArtworkHash;
        if (num != null && hash == num.intValue()) {
            Bitmap bitmap = this.fallbackArtworkBitmap;
            if ((bitmap == null || bitmap.isRecycled()) ? false : true) {
                return this.fallbackArtworkBitmap;
            }
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLifeMediaPublisher $this$fallbackArtwork_u24lambda_u2447 = this;
            Bitmap bitmap2 = $this$fallbackArtwork_u24lambda_u2447.fallbackArtworkBitmap;
            if (bitmap2 != null) {
                bitmap2.recycle();
                unit = Unit.INSTANCE;
            } else {
                unit = null;
            }
            Result.m159constructorimpl(unit);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        this.fallbackArtworkHash = Integer.valueOf(hash);
        try {
            Result.Companion companion3 = Result.INSTANCE;
            CarLifeMediaPublisher carLifeMediaPublisher = this;
            Bitmap source = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (source == null) {
                source = null;
            } else if (source.getWidth() > 200 || source.getHeight() > 200) {
                Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(source, 200, 200, true);
                Intrinsics.checkNotNullExpressionValue(bitmapCreateScaledBitmap, "createScaledBitmap(...)");
                source = bitmapCreateScaledBitmap;
            }
            objM159constructorimpl = Result.m159constructorimpl(source);
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        this.fallbackArtworkBitmap = (Bitmap) (Result.m165isFailureimpl(objM159constructorimpl) ? null : objM159constructorimpl);
        return this.fallbackArtworkBitmap;
    }

    private final MediaApplicationContext extractApplicationContext(String packageName, MediaController controller) {
        int pid = getApplicationPid(packageName);
        String appName = getApplicationName(packageName);
        return new MediaApplicationContext(packageName, pid, appName, "playback");
    }

    private final int getApplicationPid(String packageName) {
        Object objM159constructorimpl;
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses;
        Object next;
        int i;
        ActivityManager.RunningAppProcessInfo it;
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLifeMediaPublisher $this$getApplicationPid_u24lambda_u2451 = this;
            ActivityManager am = (ActivityManager) $this$getApplicationPid_u24lambda_u2451.app.getSystemService(ActivityManager.class);
            if (am == null || (runningAppProcesses = am.getRunningAppProcesses()) == null) {
                i = 0;
            } else {
                Iterator<T> it2 = runningAppProcesses.iterator();
                do {
                    if (!it2.hasNext()) {
                        next = null;
                        break;
                    }
                    next = it2.next();
                    it = (ActivityManager.RunningAppProcessInfo) next;
                } while (!Intrinsics.areEqual(it.processName, packageName));
                ActivityManager.RunningAppProcessInfo runningAppProcessInfo = (ActivityManager.RunningAppProcessInfo) next;
                i = runningAppProcessInfo != null ? runningAppProcessInfo.pid : 0;
            }
            objM159constructorimpl = Result.m159constructorimpl(Integer.valueOf(i));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = 0;
        }
        return ((Number) objM159constructorimpl).intValue();
    }

    private final String getApplicationName(String packageName) {
        Object objM159constructorimpl;
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLifeMediaPublisher $this$getApplicationName_u24lambda_u2452 = this;
            PackageManager pm = $this$getApplicationName_u24lambda_u2452.app.getPackageManager();
            objM159constructorimpl = Result.m159constructorimpl(pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0)).toString());
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = packageName;
        }
        return (String) objM159constructorimpl;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        Unit unit;
        Unit unit2;
        synchronized (this) {
            if (this.closed) {
                return;
            }
            this.closed = true;
            Unit unit3 = Unit.INSTANCE;
            this.executor.shutdownNow();
            this.artworkExecutor.shutdownNow();
            try {
                Result.Companion companion = Result.INSTANCE;
                CarLifeMediaPublisher $this$close_u24lambda_u2454 = this;
                Bitmap bitmap = $this$close_u24lambda_u2454.cachedArtworkBitmap;
                if (bitmap != null) {
                    bitmap.recycle();
                    unit2 = Unit.INSTANCE;
                } else {
                    unit2 = null;
                }
                Result.m159constructorimpl(unit2);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            this.cachedArtworkBitmap = null;
            this.cachedArtworkUri = null;
            try {
                Result.Companion companion3 = Result.INSTANCE;
                CarLifeMediaPublisher $this$close_u24lambda_u2455 = this;
                Bitmap bitmap2 = $this$close_u24lambda_u2455.fallbackArtworkBitmap;
                if (bitmap2 != null) {
                    bitmap2.recycle();
                    unit = Unit.INSTANCE;
                } else {
                    unit = null;
                }
                Result.m159constructorimpl(unit);
            } catch (Throwable th2) {
                Result.Companion companion4 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th2));
            }
            this.fallbackArtworkBitmap = null;
            this.fallbackArtworkHash = null;
        }
    }

    /* JADX INFO: compiled from: CarLifeMediaPublisher.kt */
    @Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0086T¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeMediaPublisher$Companion;", "", "<init>", "()V", "CARLIFE_PACKAGE", "", "ACTION_MUSIC_INFO", "ACTION_MUSIC_PROGRESS", "INFO_RETRY_INTERVAL_MS", "", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
