package com.carplay.adapter.carlife;

import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.carlink.cast.CastSettings;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.jmdns.impl.constants.DNSRecordClass;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Triple;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;

/* JADX INFO: compiled from: CarLifeSession.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000¤\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\u0010\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u001b\b\u0000\u0018\u0000 g2\u00060\u0001j\u0002`\u0002:\u0001gB\u008d\u0003\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0006\u0012\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u000b0\n\u0012\b\b\u0002\u0010\f\u001a\u00020\u0004\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e\u0012\u001e\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u000b0\u0010\u0012\u001e\u0010\u0011\u001a\u001a\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u000b0\u0010\u0012M\b\u0002\u0010\u0015\u001aG\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0019\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\u000b0\u0010\u0012\u0014\b\u0002\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u000b0\n\u0012\u000e\b\u0002\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u000b0\u001d\u0012M\b\u0002\u0010\u001e\u001aG\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0019\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\u000b0\u0010\u0012\u0014\b\u0002\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u000b0\n\u0012\u0014\b\u0002\u0010 \u001a\u000e\u0012\u0004\u0012\u00020!\u0012\u0004\u0012\u00020\u000b0\n\u0012\n\b\u0002\u0010\"\u001a\u0004\u0018\u00010#¢\u0006\u0004\b$\u0010%J\u0010\u0010C\u001a\u00020\u000b2\b\b\u0002\u0010D\u001a\u00020\u0006J\"\u0010E\u001a\u00020.2\u0006\u0010F\u001a\u00020\u00062\u0006\u0010D\u001a\u00020\u00062\b\b\u0002\u0010G\u001a\u00020\u0013H\u0002J\b\u0010H\u001a\u00020\u000bH\u0002J8\u0010I\u001a\u00020\u000b2\u0006\u0010\u0017\u001a\u00020\u00042\u0006\u0010J\u001a\u00020K2\u001e\u0010L\u001a\u001a\u0012\u0004\u0012\u00020M\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u000b0\u0010H\u0002J\u0010\u0010N\u001a\u00020\u000b2\u0006\u0010J\u001a\u00020KH\u0002J\u0010\u0010O\u001a\u00020\u000b2\u0006\u0010J\u001a\u00020KH\u0002J\u0010\u0010P\u001a\u00020\u000b2\u0006\u0010J\u001a\u00020KH\u0002J\u0018\u0010Q\u001a\u00020\u000b2\u0006\u0010R\u001a\u00020\u00062\u0006\u0010S\u001a\u00020\u0012H\u0002J\b\u0010T\u001a\u00020\u000bH\u0002J\u0018\u0010U\u001a\u00020\u000b2\u0006\u0010R\u001a\u00020\u00062\u0006\u0010S\u001a\u00020\u0012H\u0002J\u0010\u0010V\u001a\u00020\u000b2\u0006\u0010W\u001a\u00020!H\u0002J\u001e\u0010X\u001a\u00020\u000b2\u0006\u0010Y\u001a\u00020\u00062\u0006\u0010Z\u001a\u00020\u00062\u0006\u0010[\u001a\u00020\u0006J\u000e\u0010\\\u001a\u00020\u000b2\u0006\u0010]\u001a\u00020\u0006J\u0006\u0010^\u001a\u00020\u0014J\b\u0010_\u001a\u00020\u0012H\u0002J\u000e\u0010`\u001a\u00020\u000b2\u0006\u0010a\u001a\u00020\u0006J \u0010b\u001a\u00020\u000b2\u0006\u0010a\u001a\u00020\u00062\u0006\u0010c\u001a\u00020\u00062\u0006\u0010d\u001a\u00020\u0013H\u0002J\u0018\u0010e\u001a\u00020\u000b2\u0006\u0010R\u001a\u00020\u00062\u0006\u0010S\u001a\u00020\u0012H\u0002J\b\u0010f\u001a\u00020\u000bH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R&\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u000b0\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R&\u0010\u0011\u001a\u001a\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u000b0\u0010X\u0082\u0004¢\u0006\u0002\n\u0000RS\u0010\u0015\u001aG\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0019\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\u000b0\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u000b0\u001dX\u0082\u0004¢\u0006\u0002\n\u0000RS\u0010\u001e\u001aG\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u0019\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0016\u0012\b\b\u0017\u0012\u0004\b\b(\u001a\u0012\u0004\u0012\u00020\u000b0\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010 \u001a\u000e\u0012\u0004\u0012\u00020!\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u0004\u0018\u00010#X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020'X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020'X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020*X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020'X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010,\u001a\u0004\u0018\u00010*X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010-\u001a\u0004\u0018\u00010.X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010/\u001a\u0004\u0018\u000100X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u00101\u001a\u0004\u0018\u00010.X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00103\u001a\u00020\u0014X\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u00104\u001a\u0012\u0012\u0004\u0012\u00020.05j\b\u0012\u0004\u0012\u00020.`6X\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u00107\u001a\u0012\u0012\u0004\u0012\u00020805j\b\u0012\u0004\u0012\u000208`6X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020:X\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010<\u001a\u00020\u00142\u0006\u0010;\u001a\u00020\u0014@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b=\u0010>R\u000e\u0010?\u001a\u00020\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\"\u0010@\u001a\u0004\u0018\u00010!2\b\u0010;\u001a\u0004\u0018\u00010!@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\bA\u0010B¨\u0006h"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeSession;", "Ljava/lang/AutoCloseable;", "Lkotlin/AutoCloseable;", "host", "", "width", "", "height", "fps", "log", "Lkotlin/Function1;", "", "hostId", "panel", "Lcom/carplay/adapter/carlife/CarLifePanel;", "onEncoderReady", "Lkotlin/Function3;", "onVideoFrame", "", "", "", "onAudioInit", "Lkotlin/ParameterName;", "name", "sampleRate", "channels", "format", "onAudioData", "onAudioEnded", "Lkotlin/Function0;", "onTtsInit", "onTtsData", "onFailure", "", "container", "Lcom/carplay/adapter/carlife/CarLifeProjectionAdapter;", "<init>", "(Ljava/lang/String;IIILkotlin/jvm/functions/Function1;Ljava/lang/String;Lcom/carplay/adapter/carlife/CarLifePanel;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lcom/carplay/adapter/carlife/CarLifeProjectionAdapter;)V", "running", "Ljava/util/concurrent/atomic/AtomicBoolean;", "retired", "lifecycleLock", "", "failureReported", "snapshotedSafeAreaPolicy", "cmdSocket", "Ljava/net/Socket;", "cmdOut", "Ljava/io/BufferedOutputStream;", "touchSocket", "touchPacketsSent", "touchUnavailableLogged", "auxSockets", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "threads", "Ljava/lang/Thread;", "encoderLifecycle", "Lcom/carplay/adapter/carlife/CarLifeEncoderLifecycle;", "value", "streaming", "getStreaming", "()Z", "encoderRateGeneration", "failure", "getFailure", "()Ljava/lang/Throwable;", "connect", "timeoutMs", "connectWithRetry", "port", "windowMs", "connectAuxPorts", "startReader", "input", "Ljava/io/InputStream;", "onFrame", "Lcom/carplay/adapter/carlife/CarLifeCommandSplitter;", "startVideoReader", "startTtsReader", "startMediaReader", "handleCommand", "serviceType", "payload", "sendDeviceInfo", "sendCommand", "reportFailure", "error", CastSettings.KEY_TOUCH, "action", "x", "y", "key", "keyCode", "requestKeyFrame", "encoderInitPayload", "changeFrameRate", "rate", "requestFrameRate", "attempt", "generation", "sendTouch", "close", "Companion", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarLifeSession implements AutoCloseable {

    @Deprecated
    public static final long AUX_CONNECT_WINDOW_MS = 8000;

    @Deprecated
    public static final long CONNECT_RETRY_INTERVAL_MS = 400;

    @Deprecated
    public static final long CONNECT_RETRY_WINDOW_MS = 25000;
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final String FEATURE_CONTENT_ENCRYPTION = "CONTENT_ENCRYPTION";

    @Deprecated
    public static final String FEATURE_DISABLE_RESOLUTION_OPTION = "DISABLE_RESOLUTION_OPTION";

    @Deprecated
    public static final String FEATURE_DISABLE_VOICE_COMMAND = "DISABLE_VOICE_COMMAND";

    @Deprecated
    public static final String FEATURE_FOCUS_UI = "FOCUS_UI";

    @Deprecated
    public static final String FEATURE_MEDIA_SAMPLE_RATE = "MEDIA_SAMPLE_RATE";

    @Deprecated
    public static final String FEATURE_VOICE_MIC = "VOICE_MIC";

    @Deprecated
    public static final int FRAME_RATE_ATTEMPTS = 3;

    @Deprecated
    public static final long FRAME_RATE_RETRY_DELAY_MS = 1200;
    private final ArrayList<Socket> auxSockets;
    private BufferedOutputStream cmdOut;
    private Socket cmdSocket;
    private final CarLifeProjectionAdapter container;
    private final CarLifeEncoderLifecycle encoderLifecycle;
    private volatile long encoderRateGeneration;
    private volatile Throwable failure;
    private final AtomicBoolean failureReported;
    private final int fps;
    private final int height;
    private final String host;
    private final String hostId;
    private final Object lifecycleLock;
    private final Function1<String, Unit> log;
    private final Function1<byte[], Unit> onAudioData;
    private final Function0<Unit> onAudioEnded;
    private final Function3<Integer, Integer, Integer, Unit> onAudioInit;
    private final Function3<Integer, Integer, Integer, Unit> onEncoderReady;
    private final Function1<Throwable, Unit> onFailure;
    private final Function1<byte[], Unit> onTtsData;
    private final Function3<Integer, Integer, Integer, Unit> onTtsInit;
    private final Function3<byte[], Long, Boolean, Unit> onVideoFrame;
    private final CarLifePanel panel;
    private final AtomicBoolean retired;
    private final AtomicBoolean running;
    private final Object snapshotedSafeAreaPolicy;
    private volatile boolean streaming;
    private final ArrayList<Thread> threads;
    private long touchPacketsSent;
    private Socket touchSocket;
    private boolean touchUnavailableLogged;
    private final int width;

    /* JADX WARN: Multi-variable type inference failed */
    public CarLifeSession(String host, int width, int height, int fps, Function1<? super String, Unit> log, String hostId, CarLifePanel panel, Function3<? super Integer, ? super Integer, ? super Integer, Unit> onEncoderReady, Function3<? super byte[], ? super Long, ? super Boolean, Unit> onVideoFrame, Function3<? super Integer, ? super Integer, ? super Integer, Unit> onAudioInit, Function1<? super byte[], Unit> onAudioData, Function0<Unit> onAudioEnded, Function3<? super Integer, ? super Integer, ? super Integer, Unit> onTtsInit, Function1<? super byte[], Unit> onTtsData, Function1<? super Throwable, Unit> onFailure, CarLifeProjectionAdapter container) {
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(log, "log");
        Intrinsics.checkNotNullParameter(hostId, "hostId");
        Intrinsics.checkNotNullParameter(onEncoderReady, "onEncoderReady");
        Intrinsics.checkNotNullParameter(onVideoFrame, "onVideoFrame");
        Intrinsics.checkNotNullParameter(onAudioInit, "onAudioInit");
        Intrinsics.checkNotNullParameter(onAudioData, "onAudioData");
        Intrinsics.checkNotNullParameter(onAudioEnded, "onAudioEnded");
        Intrinsics.checkNotNullParameter(onTtsInit, "onTtsInit");
        Intrinsics.checkNotNullParameter(onTtsData, "onTtsData");
        Intrinsics.checkNotNullParameter(onFailure, "onFailure");
        this.host = host;
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.log = log;
        this.hostId = hostId;
        this.panel = panel;
        this.onEncoderReady = onEncoderReady;
        this.onVideoFrame = onVideoFrame;
        this.onAudioInit = onAudioInit;
        this.onAudioData = onAudioData;
        this.onAudioEnded = onAudioEnded;
        this.onTtsInit = onTtsInit;
        this.onTtsData = onTtsData;
        this.onFailure = onFailure;
        this.container = container;
        this.running = new AtomicBoolean(false);
        this.retired = new AtomicBoolean(false);
        this.lifecycleLock = new Object();
        this.failureReported = new AtomicBoolean(false);
        CarLifeProjectionAdapter carLifeProjectionAdapter = this.container;
        this.snapshotedSafeAreaPolicy = carLifeProjectionAdapter != null ? carLifeProjectionAdapter.getSafeAreaPolicy() : null;
        this.auxSockets = new ArrayList<>();
        this.threads = new ArrayList<>();
        this.encoderLifecycle = new CarLifeEncoderLifecycle();
    }

    public /* synthetic */ CarLifeSession(String str, int i, int i2, int i3, Function1 function1, String str2, CarLifePanel carLifePanel, Function3 function3, Function3 function4, Function3 function5, Function1 function2, Function0 function0, Function3 function6, Function1 function7, Function1 function8, CarLifeProjectionAdapter carLifeProjectionAdapter, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, i, i2, i3, function1, (i4 & 32) != 0 ? "carlink-cast-host" : str2, (i4 & 64) != 0 ? null : carLifePanel, function3, function4, (i4 & 512) != 0 ? new Function3() { // from class: com.carplay.adapter.carlife.CarLifeSession$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function3
            public final Object invoke(Object obj, Object obj2, Object obj3) {
                ((Integer) obj).intValue();
                ((Integer) obj2).intValue();
                ((Integer) obj3).intValue();
                return Unit.INSTANCE;
            }
        } : function5, (i4 & 1024) != 0 ? new Function1() { // from class: com.carplay.adapter.carlife.CarLifeSession$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return CarLifeSession._init_$lambda$1((byte[]) obj);
            }
        } : function2, (i4 & 2048) != 0 ? new Function0() { // from class: com.carplay.adapter.carlife.CarLifeSession$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return Unit.INSTANCE;
            }
        } : function0, (i4 & 4096) != 0 ? new Function3() { // from class: com.carplay.adapter.carlife.CarLifeSession$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function3
            public final Object invoke(Object obj, Object obj2, Object obj3) {
                ((Integer) obj).intValue();
                ((Integer) obj2).intValue();
                ((Integer) obj3).intValue();
                return Unit.INSTANCE;
            }
        } : function6, (i4 & 8192) != 0 ? new Function1() { // from class: com.carplay.adapter.carlife.CarLifeSession$$ExternalSyntheticLambda9
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return CarLifeSession._init_$lambda$4((byte[]) obj);
            }
        } : function7, (i4 & 16384) != 0 ? new Function1() { // from class: com.carplay.adapter.carlife.CarLifeSession$$ExternalSyntheticLambda10
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return CarLifeSession._init_$lambda$5((Throwable) obj);
            }
        } : function8, (i4 & 32768) != 0 ? null : carLifeProjectionAdapter);
    }

    static final Unit _init_$lambda$1(byte[] it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    static final Unit _init_$lambda$4(byte[] it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    static final Unit _init_$lambda$5(Throwable it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    public final boolean getStreaming() {
        return this.streaming;
    }

    public final Throwable getFailure() {
        return this.failure;
    }

    public static /* synthetic */ void connect$default(CarLifeSession carLifeSession, int i, int i2, Object obj) throws Throwable {
        if ((i2 & 1) != 0) {
            i = 15000;
        }
        carLifeSession.connect(i);
    }

    public final void connect(int timeoutMs) throws Throwable {
        Throwable th;
        synchronized (this.lifecycleLock) {
            try {
                try {
                    if (this.retired.get()) {
                        throw new IllegalStateException("CarLife 会话已退役".toString());
                    }
                    if (!this.running.compareAndSet(false, true)) {
                        throw new IllegalStateException("CarLife 会话已启动".toString());
                    }
                    Unit unit = Unit.INSTANCE;
                    if (this.retired.get()) {
                        throw new IllegalStateException("CarLife 会话已退役".toString());
                    }
                    Socket cmd = connectWithRetry$default(this, CarLifeProtocol.PORT_CMD, timeoutMs, 0L, 4, null);
                    if (this.retired.get()) {
                        try {
                            Result.Companion companion = Result.INSTANCE;
                            CarLifeSession carLifeSession = this;
                            cmd.close();
                            Result.m159constructorimpl(Unit.INSTANCE);
                        } catch (Throwable th2) {
                            Result.Companion companion2 = Result.INSTANCE;
                            Result.m159constructorimpl(ResultKt.createFailure(th2));
                        }
                        throw new IllegalStateException("CarLife 会话已退役".toString());
                    }
                    this.cmdSocket = cmd;
                    this.cmdOut = new BufferedOutputStream(cmd.getOutputStream());
                    connectAuxPorts();
                    if (this.retired.get()) {
                        throw new IllegalStateException("CarLife 会话已退役".toString());
                    }
                    InputStream inputStream = cmd.getInputStream();
                    Intrinsics.checkNotNullExpressionValue(inputStream, "getInputStream(...)");
                    startReader("carlife-cmd", inputStream, new Function3() { // from class: com.carplay.adapter.carlife.CarLifeSession$$ExternalSyntheticLambda3
                        @Override // kotlin.jvm.functions.Function3
                        public final Object invoke(Object obj, Object obj2, Object obj3) {
                            return CarLifeSession.connect$lambda$15(this.f$0, (CarLifeCommandSplitter) obj, (byte[]) obj2, ((Integer) obj3).intValue());
                        }
                    });
                    sendCommand(CarLifeProtocol.HU_PROTOCOL_VERSION, new CarLifeProto.Writer().int32(1, 1).int32(2, 0).toByteArray());
                    return;
                } catch (Throwable th3) {
                    th = th3;
                }
            } catch (Throwable th4) {
                th = th4;
            }
            throw th;
        }
    }

    static final Unit connect$lambda$15(CarLifeSession this$0, CarLifeCommandSplitter splitter, byte[] buffer, int read) {
        Object objM159constructorimpl;
        Intrinsics.checkNotNullParameter(splitter, "splitter");
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        for (CarLifeFrame frame : splitter.feed(buffer, 0, read)) {
            try {
                Result.Companion companion = Result.INSTANCE;
                this$0.handleCommand(frame.getServiceType(), frame.getPayload());
                objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            Throwable it = Result.m162exceptionOrNullimpl(objM159constructorimpl);
            if (it != null) {
                Function1<String, Unit> function1 = this$0.log;
                String string = Integer.toString(frame.getServiceType(), CharsKt.checkRadix(16));
                Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                function1.invoke("处理消息 0x" + string + " 出错：" + it.getMessage());
            }
        }
        return Unit.INSTANCE;
    }

    static /* synthetic */ Socket connectWithRetry$default(CarLifeSession carLifeSession, int i, int i2, long j, int i3, Object obj) {
        if ((i3 & 4) != 0) {
            j = CONNECT_RETRY_WINDOW_MS;
        }
        return carLifeSession.connectWithRetry(i, i2, j);
    }

    private final Socket connectWithRetry(int port, int timeoutMs, long windowMs) throws Exception {
        long deadline = System.nanoTime() + (1000000 * windowMs);
        while (true) {
            try {
                Socket $this$connectWithRetry_u24lambda_u2416 = new Socket();
                $this$connectWithRetry_u24lambda_u2416.setTcpNoDelay(true);
                $this$connectWithRetry_u24lambda_u2416.setKeepAlive(true);
                $this$connectWithRetry_u24lambda_u2416.connect(new InetSocketAddress(this.host, port), timeoutMs);
                return $this$connectWithRetry_u24lambda_u2416;
            } catch (Exception error) {
                if (System.nanoTime() >= deadline) {
                    throw error;
                }
                Thread.sleep(400L);
            }
        }
    }

    private final void connectAuxPorts() throws IOException {
        ArrayList missingRequired = new ArrayList();
        for (int port : CarLifeProtocol.INSTANCE.getAUX_PORTS()) {
            try {
                Socket socket = connectWithRetry(port, PathInterpolatorCompat.MAX_NUM_POINTS, AUX_CONNECT_WINDOW_MS);
                this.auxSockets.add(socket);
                switch (port) {
                    case CarLifeProtocol.PORT_VIDEO /* 8240 */:
                        InputStream inputStream = socket.getInputStream();
                        Intrinsics.checkNotNullExpressionValue(inputStream, "getInputStream(...)");
                        startVideoReader(inputStream);
                        break;
                    case CarLifeProtocol.PORT_MEDIA /* 9240 */:
                        InputStream inputStream2 = socket.getInputStream();
                        Intrinsics.checkNotNullExpressionValue(inputStream2, "getInputStream(...)");
                        startMediaReader(inputStream2);
                        break;
                    case CarLifeProtocol.PORT_TTS /* 9241 */:
                        InputStream inputStream3 = socket.getInputStream();
                        Intrinsics.checkNotNullExpressionValue(inputStream3, "getInputStream(...)");
                        startTtsReader(inputStream3);
                        break;
                    case CarLifeProtocol.PORT_TOUCH /* 9340 */:
                        this.touchSocket = socket;
                        break;
                }
                this.log.invoke("CarLife 端口 " + port + " 已连接");
            } catch (Exception error) {
                this.log.invoke("CarLife 端口 " + port + " 连接失败：" + error.getMessage());
                if (CarLifeProtocol.INSTANCE.getREQUIRED_AUX_PORTS().contains(Integer.valueOf(port))) {
                    missingRequired.add(Integer.valueOf(port));
                }
            }
        }
        if (!missingRequired.isEmpty()) {
            throw new IOException("CarLife 必需端口连接失败：" + CollectionsKt.joinToString$default(missingRequired, ",", null, null, 0, null, null, 62, null));
        }
        this.log.invoke("CarLife 端口 " + (this.auxSockets.size() + 1) + "/" + (CarLifeProtocol.INSTANCE.getAUX_PORTS().length + 1) + " 已连接");
    }

    private final void startReader(String name, final InputStream input, final Function3<? super CarLifeCommandSplitter, ? super byte[], ? super Integer, Unit> onFrame) {
        Thread thread = new Thread(new Runnable() { // from class: com.carplay.adapter.carlife.CarLifeSession$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                CarLifeSession.startReader$lambda$17(input, this, onFrame);
            }
        }, name);
        thread.setDaemon(true);
        this.threads.add(thread);
        thread.start();
    }

    static final void startReader$lambda$17(InputStream $input, CarLifeSession this$0, Function3 $onFrame) {
        CarLifeCommandSplitter splitter = new CarLifeCommandSplitter();
        byte[] buffer = new byte[32768];
        try {
            BufferedInputStream stream = new BufferedInputStream($input);
            while (this$0.running.get()) {
                int read = stream.read(buffer);
                if (read < 0) {
                    this$0.reportFailure(new EOFException("CarLife 命令端口已关闭"));
                    return;
                } else if (read != 0) {
                    $onFrame.invoke(splitter, buffer, Integer.valueOf(read));
                }
            }
        } catch (Throwable error) {
            this$0.reportFailure(error);
        }
    }

    private final void startVideoReader(final InputStream input) {
        Thread thread = new Thread(new Runnable() { // from class: com.carplay.adapter.carlife.CarLifeSession$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                CarLifeSession.startVideoReader$lambda$19(input, this);
            }
        }, "carlife-video");
        thread.setDaemon(true);
        this.threads.add(thread);
        thread.start();
    }

    static final void startVideoReader$lambda$19(InputStream $input, CarLifeSession this$0) {
        CarLifeStreamSplitter splitter = new CarLifeStreamSplitter();
        byte[] buffer = new byte[65536];
        try {
            BufferedInputStream stream = new BufferedInputStream($input);
            while (this$0.running.get()) {
                int read = stream.read(buffer);
                if (read < 0) {
                    this$0.reportFailure(new EOFException("CarLife 视频端口已关闭"));
                    return;
                } else if (read != 0) {
                    for (CarLifeStreamPacket packet : splitter.feed(buffer, 0, read)) {
                        if (packet.getServiceType() == 131073) {
                            this$0.onVideoFrame.invoke(packet.getData(), Long.valueOf(packet.getTimestampSeconds()), Boolean.valueOf(CarLifeSessionKt.isCarLifeKeyFrame(packet.getData())));
                        }
                    }
                }
            }
        } catch (Throwable error) {
            this$0.reportFailure(error);
        }
    }

    private final void startTtsReader(final InputStream input) {
        Thread thread = new Thread(new Runnable() { // from class: com.carplay.adapter.carlife.CarLifeSession$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CarLifeSession.startTtsReader$lambda$21(input, this);
            }
        }, "carlife-tts");
        thread.setDaemon(true);
        this.threads.add(thread);
        thread.start();
    }

    static final void startTtsReader$lambda$21(InputStream $input, CarLifeSession this$0) {
        CarLifeStreamSplitter splitter = new CarLifeStreamSplitter();
        byte[] buffer = new byte[65536];
        try {
            BufferedInputStream stream = new BufferedInputStream($input);
            while (this$0.running.get()) {
                int read = stream.read(buffer);
                if (read < 0) {
                    this$0.log.invoke("CarLife TTS 辅助端口已关闭，继续保持视频会话");
                    return;
                }
                if (read != 0) {
                    for (CarLifeStreamPacket packet : splitter.feed(buffer, 0, read)) {
                        switch (packet.getServiceType()) {
                            case CarLifeProtocol.STREAM_TTS_INIT /* 262145 */:
                                Triple<Integer, Integer, Integer> tripleDecodeAudioInit = CarLifeProto.INSTANCE.decodeAudioInit(packet.getData());
                                int rate = tripleDecodeAudioInit.component1().intValue();
                                int channels = tripleDecodeAudioInit.component2().intValue();
                                int format = tripleDecodeAudioInit.component3().intValue();
                                this$0.log.invoke("CarLife TTS 音频格式：" + rate + "Hz / " + channels + " 声道 / 格式 " + format);
                                this$0.onTtsInit.invoke(Integer.valueOf(rate), Integer.valueOf(channels), Integer.valueOf(format));
                                break;
                            case CarLifeProtocol.STREAM_TTS_END /* 262146 */:
                                this$0.log.invoke("CarLife TTS 结束");
                                break;
                            case CarLifeProtocol.STREAM_TTS_DATA /* 262147 */:
                                this$0.onTtsData.invoke(packet.getData());
                                break;
                        }
                    }
                }
            }
        } catch (Exception error) {
            if (this$0.running.get()) {
                this$0.log.invoke("CarLife TTS 辅助流结束：" + error.getMessage());
            }
        }
    }

    private final void startMediaReader(final InputStream input) {
        Thread thread = new Thread(new Runnable() { // from class: com.carplay.adapter.carlife.CarLifeSession$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                CarLifeSession.startMediaReader$lambda$25(input, this);
            }
        }, "carlife-media");
        thread.setDaemon(true);
        this.threads.add(thread);
        thread.start();
    }

    static final void startMediaReader$lambda$25(InputStream $input, CarLifeSession this$0) {
        CarLifeStreamSplitter splitter = new CarLifeStreamSplitter();
        byte[] buffer = new byte[65536];
        try {
            BufferedInputStream stream = new BufferedInputStream($input);
            while (this$0.running.get()) {
                int read = stream.read(buffer);
                if (read < 0) {
                    this$0.log.invoke("CarLife 音频端口已关闭，停止音频读取但保留视频会话");
                    try {
                        Result.Companion companion = Result.INSTANCE;
                        this$0.onAudioEnded.invoke();
                        Result.m159constructorimpl(Unit.INSTANCE);
                    } catch (Throwable th) {
                        Result.Companion companion2 = Result.INSTANCE;
                        Result.m159constructorimpl(ResultKt.createFailure(th));
                    }
                    return;
                }
                if (read != 0) {
                    for (CarLifeStreamPacket packet : splitter.feed(buffer, 0, read)) {
                        switch (packet.getServiceType()) {
                            case CarLifeProtocol.STREAM_MEDIA_INIT /* 196609 */:
                                Triple<Integer, Integer, Integer> tripleDecodeAudioInit = CarLifeProto.INSTANCE.decodeAudioInit(packet.getData());
                                int sampleRate = tripleDecodeAudioInit.component1().intValue();
                                int channels = tripleDecodeAudioInit.component2().intValue();
                                int format = tripleDecodeAudioInit.component3().intValue();
                                this$0.log.invoke("CarLife 音频格式：" + sampleRate + "Hz / " + channels + " 声道 / 格式 " + format);
                                this$0.onAudioInit.invoke(Integer.valueOf(sampleRate), Integer.valueOf(channels), Integer.valueOf(format));
                                break;
                            case CarLifeProtocol.STREAM_MEDIA_STOP /* 196610 */:
                            case CarLifeProtocol.STREAM_MEDIA_PAUSE /* 196611 */:
                                this$0.log.invoke("CarLife 音频暂停");
                                break;
                            case CarLifeProtocol.STREAM_MEDIA_RESUME /* 196612 */:
                                this$0.log.invoke("CarLife 音频恢复");
                                break;
                            case CarLifeProtocol.STREAM_MEDIA_DATA /* 196614 */:
                                this$0.onAudioData.invoke(packet.getData());
                                break;
                        }
                    }
                }
            }
        } catch (Throwable error) {
            if (this$0.running.get()) {
                Function1<String, Unit> function1 = this$0.log;
                String message = error.getMessage();
                if (message == null) {
                    message = error.getClass().getSimpleName();
                }
                function1.invoke("CarLife 音频流结束，视频会话继续：" + message);
                try {
                    Result.Companion companion3 = Result.INSTANCE;
                    this$0.onAudioEnded.invoke();
                    Result.m159constructorimpl(Unit.INSTANCE);
                } catch (Throwable th2) {
                    Result.Companion companion4 = Result.INSTANCE;
                    Result.m159constructorimpl(ResultKt.createFailure(th2));
                }
            }
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:15:0x0139. Please report as an issue. */
    private final void handleCommand(int serviceType, byte[] payload) {
        Object objM159constructorimpl;
        int id = (serviceType & DNSRecordClass.CLASS_MASK) | 65536;
        switch (id) {
            case CarLifeProtocol.PROTOCOL_VERSION_MATCH_STATUS /* 65538 */:
                this.log.invoke("协议版本匹配 status=" + CarLifeProto.INSTANCE.decodeMatchStatus(payload));
                sendDeviceInfo();
                sendCommand(CarLifeProtocol.HU_STATISTIC_INFO, CarLifeProto.INSTANCE.statisticInfo(this.hostId));
                this.log.invoke("已上报车辆渠道信息 20011100");
                break;
            case 65540:
                this.log.invoke("手机信息：" + CarLifeProto.INSTANCE.describeDeviceInfo(payload));
                sendCommand(CarLifeProtocol.HU_AUTHEN_REQUEST, CarLifeProto.INSTANCE.authenRequest(CarLifeProtocol.AUTHEN_RANDOM_VALUE));
                break;
            case CarLifeProtocol.VIDEO_ENCODER_INIT_DONE /* 65544 */:
                Triple<Integer, Integer, Integer> tripleDecodeVideoEncoderInfo = CarLifeProto.INSTANCE.decodeVideoEncoderInfo(payload);
                int w = tripleDecodeVideoEncoderInfo.component1().intValue();
                int h = tripleDecodeVideoEncoderInfo.component2().intValue();
                int rate = tripleDecodeVideoEncoderInfo.component3().intValue();
                if (!this.encoderLifecycle.onEncoderInitDone()) {
                    this.log.invoke("忽略过期 VIDEO_ENCODER_INIT_DONE：当前编码器状态不匹配");
                } else {
                    this.encoderRateGeneration++;
                    long rateGeneration = this.encoderRateGeneration;
                    this.log.invoke("手机编码器就绪 " + w + "x" + h + "@" + rate);
                    this.onEncoderReady.invoke(Integer.valueOf(w), Integer.valueOf(h), Integer.valueOf(rate));
                    CarLifePanel area = this.panel;
                    if (area != null) {
                        this.log.invoke("编码器几何回报：请求面板 " + area.getPanelWidth() + "x" + area.getPanelHeight() + "（安全区 " + area.getSafeWidth() + "x" + area.getSafeHeight() + "@(" + area.getSafeX() + "," + area.getSafeY() + ")），CarLife 回 " + w + "x" + h + "；仅凭回报尺寸不能确认背景外扩或居中生效");
                    }
                    sendCommand(CarLifeProtocol.VIDEO_ENCODER_START, new byte[0]);
                    this.encoderLifecycle.onEncoderStarted();
                    this.streaming = true;
                    requestFrameRate(this.fps, 0, rateGeneration);
                }
                break;
            case CarLifeProtocol.VIDEO_ENCODER_FRAME_RATE_CHANGE_DONE /* 65549 */:
                try {
                    Result.Companion companion = Result.INSTANCE;
                    CarLifeSession carLifeSession = this;
                    try {
                        CarLifeProto.Reader it = new CarLifeProto.Reader(payload, 0, 0, 6, null);
                        int value = 0;
                        while (true) {
                            int field = it.nextField();
                            switch (field) {
                                case 0:
                                    break;
                                case 1:
                                    value = it.readInt32();
                                    break;
                                default:
                                    it.skip();
                                    break;
                            }
                            objM159constructorimpl = Result.m159constructorimpl(Integer.valueOf(value));
                            if (Result.m165isFailureimpl(objM159constructorimpl)) {
                                objM159constructorimpl = null;
                            }
                            Integer rate2 = (Integer) objM159constructorimpl;
                            this.log.invoke("CarLife 帧率已变更：" + (rate2 == null ? "未知" : rate2) + "fps（目标 " + this.fps + "fps）");
                        }
                    } catch (Throwable th) {
                        th = th;
                        Result.Companion companion2 = Result.INSTANCE;
                        objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
                break;
            case CarLifeProtocol.MD_AUTHEN_RESPONSE /* 65609 */:
                sendCommand(CarLifeProtocol.HU_AUTHEN_RESULT, CarLifeProto.INSTANCE.authenResult(true));
                sendCommand(CarLifeProtocol.HU_FEATURE_CONFIG_RESPONSE, CarLifeProto.INSTANCE.featureConfig(TuplesKt.to(FEATURE_FOCUS_UI, 1), TuplesKt.to(FEATURE_DISABLE_RESOLUTION_OPTION, 1), TuplesKt.to(FEATURE_DISABLE_VOICE_COMMAND, 0), TuplesKt.to(FEATURE_CONTENT_ENCRYPTION, 0), TuplesKt.to(FEATURE_MEDIA_SAMPLE_RATE, 1), TuplesKt.to(FEATURE_VOICE_MIC, 1)));
                this.log.invoke("已主动下发车机功能配置：FOCUS_UI=1，MEDIA_SAMPLE_RATE=48K，VOICE_MIC=手机");
                boolean zRequestInitial = this.encoderLifecycle.requestInitial();
                Function1<String, Unit> function1 = this.log;
                if (!zRequestInitial) {
                    function1.invoke("认证重复：首次 VIDEO_ENCODER_INIT 已在处理中，忽略重复请求");
                } else {
                    function1.invoke("认证通过，请求手机编码器 " + this.width + "x" + this.height + "@" + this.fps);
                    sendCommand(CarLifeProtocol.VIDEO_ENCODER_INIT, encoderInitPayload());
                }
                break;
            case CarLifeProtocol.MD_FEATURE_CONFIG_REQUEST /* 65617 */:
                sendCommand(CarLifeProtocol.HU_FEATURE_CONFIG_RESPONSE, CarLifeProto.INSTANCE.featureConfig(TuplesKt.to(FEATURE_DISABLE_RESOLUTION_OPTION, 1), TuplesKt.to(FEATURE_DISABLE_VOICE_COMMAND, 0), TuplesKt.to(FEATURE_CONTENT_ENCRYPTION, 0), TuplesKt.to(FEATURE_MEDIA_SAMPLE_RATE, 1), TuplesKt.to(FEATURE_VOICE_MIC, 1)));
                this.log.invoke("已回复车机功能配置：按请求分辨率编码，48K 音频，VOICE_MIC=手机");
                break;
            case CarLifeProtocol.MD_EXIT /* 65625 */:
                this.log.invoke("手机要求结束会话");
                break;
            default:
                break;
        }
    }

    private final void sendDeviceInfo() {
        byte[] payload = new CarLifeProto.Writer().string(1, "Android").string(2, "carplay").string(4, "carplay").string(13, "carplay").string(14, "CarPlay Sender").string(15, "carplay").string(19, "11").int32(21, 30).toByteArray();
        sendCommand(CarLifeProtocol.HU_INFO, payload);
    }

    private final synchronized void sendCommand(int serviceType, byte[] payload) {
        BufferedOutputStream out = this.cmdOut;
        if (out == null) {
            return;
        }
        try {
            out.write(CarLifeProtocol.INSTANCE.header(1, serviceType, payload.length, 0));
            if (!(payload.length == 0)) {
                out.write(payload);
            }
            out.flush();
        } catch (Exception error) {
            reportFailure(error);
        }
    }

    private final void reportFailure(Throwable error) {
        Object objM159constructorimpl;
        if (this.running.get()) {
            if (this.failure == null) {
                this.failure = error;
            }
            if (this.failureReported.compareAndSet(false, true)) {
                try {
                    Result.Companion companion = Result.INSTANCE;
                    CarLifeSession $this$reportFailure_u24lambda_u2430 = this;
                    $this$reportFailure_u24lambda_u2430.onFailure.invoke(error);
                    objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
                } catch (Throwable th) {
                    Result.Companion companion2 = Result.INSTANCE;
                    objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
                }
                Throwable it = Result.m162exceptionOrNullimpl(objM159constructorimpl);
                if (it != null) {
                    this.log.invoke("CarLife 失败回调异常：" + it.getMessage());
                }
            }
        }
    }

    public final void touch(int action, int x, int y) {
        int id;
        switch (action) {
            case 0:
                id = CarLifeProtocol.MSG_TOUCH_ACTION_DOWN;
                break;
            case 1:
                id = CarLifeProtocol.MSG_TOUCH_ACTION_UP;
                break;
            default:
                id = CarLifeProtocol.MSG_TOUCH_ACTION_MOVE;
                break;
        }
        sendTouch(id, CarLifeProto.INSTANCE.touchPoint(x, y));
    }

    public final void key(int keyCode) {
        sendTouch(CarLifeProtocol.MSG_TOUCH_CAR_HARD_KEY_CODE, CarLifeProto.INSTANCE.hardKey(keyCode));
    }

    public final boolean requestKeyFrame() {
        if (!this.streaming) {
            this.log.invoke("CarLife 编码器尚未进入 STREAMING，忽略恢复请求，避免启动阶段重复 INIT");
            return false;
        }
        if (!this.encoderLifecycle.requestRebuild()) {
            this.log.invoke("CarLife 恢复请求已合并：当前编码器重建仍未完成");
            return false;
        }
        sendCommand(CarLifeProtocol.VIDEO_ENCODER_INIT, encoderInitPayload());
        this.log.invoke("已请求 CarLife 重建编码器以获得关键帧");
        return true;
    }

    private final byte[] encoderInitPayload() {
        String mode;
        CarLifePanel area = this.panel;
        if (area == null) {
            return CarLifeProto.videoEncoderInfo$default(CarLifeProto.INSTANCE, this.width, this.height, this.fps, 0, 0, 0, 0, false, false, 504, null);
        }
        boolean useSafeAreaSource = false;
        Object policy = this.snapshotedSafeAreaPolicy;
        if (policy != null) {
            try {
                Field enabledField = policy.getClass().getField("enabled");
                Field modeField = policy.getClass().getField("mode");
                Object obj = enabledField.get(policy);
                Boolean bool = obj instanceof Boolean ? (Boolean) obj : null;
                boolean z = false;
                boolean enabled = bool != null ? bool.booleanValue() : false;
                Object obj2 = modeField.get(policy);
                if (obj2 == null || (mode = obj2.toString()) == null) {
                    mode = "OFF";
                }
                if (enabled && !Intrinsics.areEqual(mode, "OFF")) {
                    z = true;
                }
                useSafeAreaSource = z;
                this.log.invoke("安全区策略已应用：enabled=" + enabled + ", mode=" + mode + ", useSafeAreaSource=" + useSafeAreaSource);
            } catch (Exception e) {
                this.log.invoke("解析 safeAreaPolicy 失败：" + e.getMessage() + "，回退到全屏编码");
            }
        } else {
            this.log.invoke("safeAreaPolicy 未设置，使用全屏编码");
        }
        int sourceWidth = useSafeAreaSource ? area.getSafeWidth() : area.getPanelWidth();
        int sourceHeight = useSafeAreaSource ? area.getSafeHeight() : area.getPanelHeight();
        Function1<String, Unit> function1 = this.log;
        int panelWidth = area.getPanelWidth();
        int panelHeight = area.getPanelHeight();
        String str = useSafeAreaSource ? "安全区" : "全屏";
        function1.invoke("编码器几何：AirPlay 全画布 " + panelWidth + "x" + panelHeight + "，CarLife 源=" + sourceWidth + "x" + sourceHeight + " (" + str + ")，元数据=" + area.getSafeWidth() + "x" + area.getSafeHeight() + "@(" + area.getSafeX() + "," + area.getSafeY() + ")");
        return CarLifeProto.INSTANCE.videoEncoderInfo(sourceWidth, sourceHeight, this.fps, area.getSafeWidth(), area.getSafeHeight(), area.getSafeX(), area.getSafeY(), area.getDrawUiOutsideSafeArea(), area.getNeedAdapterScreen());
    }

    public final void changeFrameRate(int rate) {
        if (rate < 3) {
            return;
        }
        sendCommand(CarLifeProtocol.VIDEO_ENCODER_FRAME_RATE_CHANGE, new CarLifeProto.Writer().int32(1, rate).toByteArray());
        this.log.invoke("已请求 CarLife 帧率 " + rate + "fps（车机声明上限 " + this.fps + "fps）");
    }

    private final void requestFrameRate(final int rate, final int attempt, final long generation) {
        changeFrameRate(rate);
        if (attempt + 1 >= 3) {
            return;
        }
        Thread $this$requestFrameRate_u24lambda_u2433 = new Thread(new Runnable() { // from class: com.carplay.adapter.carlife.CarLifeSession$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() throws InterruptedException {
                CarLifeSession.requestFrameRate$lambda$32(this.f$0, generation, rate, attempt);
            }
        }, "carlife-frame-rate");
        $this$requestFrameRate_u24lambda_u2433.setDaemon(true);
        $this$requestFrameRate_u24lambda_u2433.start();
    }

    static final void requestFrameRate$lambda$32(CarLifeSession this$0, long $generation, int $rate, int $attempt) throws InterruptedException {
        Thread.sleep(FRAME_RATE_RETRY_DELAY_MS);
        if (this$0.running.get() && this$0.encoderRateGeneration == $generation) {
            this$0.requestFrameRate($rate, $attempt + 1, $generation);
        }
    }

    private final synchronized void sendTouch(int serviceType, byte[] payload) {
        Socket socket = this.touchSocket;
        OutputStream out = socket != null ? socket.getOutputStream() : null;
        if (out == null) {
            if (!this.touchUnavailableLogged) {
                this.touchUnavailableLogged = true;
                this.log.invoke("触控发送跳过：CarLife touch socket 尚未连接");
            }
            return;
        }
        try {
            out.write(CarLifeProtocol.INSTANCE.header(6, serviceType, payload.length, 0));
            out.write(payload);
            out.flush();
            this.touchPacketsSent++;
            long sent = this.touchPacketsSent;
            if (sent == 1 || sent % 50 == 0) {
                Function1<String, Unit> function1 = this.log;
                String string = Integer.toString(serviceType, CharsKt.checkRadix(16));
                Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                function1.invoke("触控已写出 #" + sent + "：service=0x" + string + " payload=" + payload.length + "B connected=" + socket.isConnected() + " closed=" + socket.isClosed());
            }
        } catch (Exception error) {
            this.log.invoke("触控发送失败：" + error.getMessage());
        }
    }

    /* JADX INFO: compiled from: CarLifeSession.kt */
    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\fX\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeSession$Companion;", "", "<init>", "()V", "FEATURE_FOCUS_UI", "", "FEATURE_DISABLE_RESOLUTION_OPTION", "FEATURE_DISABLE_VOICE_COMMAND", "FEATURE_CONTENT_ENCRYPTION", "FEATURE_MEDIA_SAMPLE_RATE", "FEATURE_VOICE_MIC", "CONNECT_RETRY_WINDOW_MS", "", "AUX_CONNECT_WINDOW_MS", "CONNECT_RETRY_INTERVAL_MS", "FRAME_RATE_ATTEMPTS", "", "FRAME_RATE_RETRY_DELAY_MS", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        Unit unit;
        synchronized (this.lifecycleLock) {
            if (this.retired.getAndSet(true)) {
                return;
            }
            this.running.set(false);
            Unit unit2 = Unit.INSTANCE;
            this.encoderLifecycle.reset();
            try {
                Result.Companion companion = Result.INSTANCE;
                CarLifeSession $this$close_u24lambda_u2435 = this;
                Socket socket = $this$close_u24lambda_u2435.cmdSocket;
                if (socket != null) {
                    socket.close();
                    unit = Unit.INSTANCE;
                } else {
                    unit = null;
                }
                Result.m159constructorimpl(unit);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            Iterable $this$forEach$iv = this.auxSockets;
            for (Object element$iv : $this$forEach$iv) {
                Socket it = (Socket) element$iv;
                try {
                    Result.Companion companion3 = Result.INSTANCE;
                    CarLifeSession carLifeSession = this;
                    it.close();
                    Result.m159constructorimpl(Unit.INSTANCE);
                } catch (Throwable th2) {
                    Result.Companion companion4 = Result.INSTANCE;
                    Result.m159constructorimpl(ResultKt.createFailure(th2));
                }
            }
            this.auxSockets.clear();
            Iterable $this$forEach$iv2 = this.threads;
            for (Object element$iv2 : $this$forEach$iv2) {
                ((Thread) element$iv2).interrupt();
            }
            this.threads.clear();
            Throwable it2 = this.failure;
            if (it2 != null) {
                this.log.invoke("CarLife 会话结束：" + it2.getMessage());
            }
        }
    }
}
