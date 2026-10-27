package com.carplay.airplay;

import android.util.Log;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.UByte;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: BufferedAudioStream.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\u008a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010$\n\u0002\u0010\u0000\n\u0002\b\u0016\b\u0000\u0018\u0000 U2\u00020\u0001:\u0002TUBO\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0014\b\u0002\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\t\u0012\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0010¢\u0006\u0004\b\u0011\u0010\u0012J\u0006\u00109\u001a\u00020\u000bJ\b\u0010:\u001a\u00020\u000bH\u0002J\n\u0010;\u001a\u0004\u0018\u00010#H\u0002J\b\u0010<\u001a\u00020\u000bH\u0002J\b\u0010=\u001a\u00020\u000eH\u0002J5\u0010>\u001a\u0012\u0012\u0004\u0012\u00020\n\u0012\u0006\u0012\u0004\u0018\u00010@\u0018\u00010?2\b\u0010A\u001a\u0004\u0018\u00010\u000e2\u0006\u0010B\u001a\u00020!2\u0006\u0010C\u001a\u000201¢\u0006\u0002\u0010DJ\u0016\u0010E\u001a\u0012\u0012\u0004\u0012\u00020\n\u0012\u0006\u0012\u0004\u0018\u00010@\u0018\u00010?J\u0015\u0010F\u001a\u00020\u000b2\b\u0010G\u001a\u0004\u0018\u00010\u000e¢\u0006\u0002\u0010HJ&\u0010I\u001a\u0010\u0012\u0004\u0012\u00020\n\u0012\u0006\u0012\u0004\u0018\u00010@0?2\u0006\u0010C\u001a\u0002012\b\u0010J\u001a\u0004\u0018\u00010@J\u000f\u0010K\u001a\u0004\u0018\u00010\u000eH\u0002¢\u0006\u0002\u0010LJ\u0018\u0010M\u001a\u0012\u0012\u0004\u0012\u00020\n\u0012\u0006\u0012\u0004\u0018\u00010@\u0018\u00010?H\u0002J\u0010\u0010N\u001a\u00020\u000b2\u0006\u0010O\u001a\u00020\u000eH\u0002J\b\u0010P\u001a\u00020\u000bH\u0002J\b\u0010Q\u001a\u00020\u000bH\u0002J\b\u0010R\u001a\u00020\u000bH\u0002J\b\u0010S\u001a\u00020\u000bH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u001b\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0019@BX\u0080\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u0004\u0018\u00010#X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020%X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010&\u001a\b\u0012\u0004\u0012\u00020(0'X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010)\u001a\b\u0012\u0004\u0012\u00020(0'X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020!X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020!X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010-\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0004\n\u0002\u0010.R\u0012\u0010/\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0004\n\u0002\u0010.R\u0010\u00100\u001a\u0004\u0018\u000101X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u00102\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0004\n\u0002\u0010.R\u000e\u00103\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00104\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00105\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u00106\u001a\u00020!8F¢\u0006\u0006\u001a\u0004\b7\u00108¨\u0006V"}, d2 = {"Lcom/carplay/airplay/BufferedAudioStream;", "Ljava/io/Closeable;", "key", "", "format", "Lcom/carplay/airplay/AudioFormat;", "sink", "Lcom/carplay/airplay/MediaSink;", "log", "Lkotlin/Function1;", "", "", "nanoTime", "Lkotlin/Function0;", "", "expectedPeer", "Ljava/net/InetAddress;", "<init>", "([BLcom/carplay/airplay/AudioFormat;Lcom/carplay/airplay/MediaSink;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Ljava/net/InetAddress;)V", "id", "Lcom/carplay/airplay/AudioStreamId;", "closed", "Ljava/util/concurrent/atomic/AtomicBoolean;", "started", "cleanupStarted", "", "value", "outputCleanupComplete", "getOutputCleanupComplete$core_airplay", "()Z", "server", "Ljava/net/ServerSocket;", "boundPort", "", "client", "Ljava/net/Socket;", "lock", "Ljava/lang/Object;", "queue", "Ljava/util/ArrayDeque;", "Lcom/carplay/airplay/BufferedAudioStream$Frame;", "fed", "queuedBytes", "maxBufferedFrames", "rate", "flushUntil", "Ljava/lang/Long;", "anchorRtp", "anchorNtp", "Ljava/math/BigInteger;", "feedStartTimestamp", "feedStartNs", "fedSamples", "outputGeneration", "port", "getPort", "()I", "start", "receive", "acceptPeer", "feed", "leadMillisLocked", "setRate", "", "", "rtpTime", "newRate", "nowNtp", "(Ljava/lang/Long;ILjava/math/BigInteger;)Ljava/util/Map;", "anchor", "flush", "untilTimestamp", "(Ljava/lang/Long;)V", "feedback", "connectionId", "positionLocked", "()Ljava/lang/Long;", "anchorLocked", "dropBeforeLocked", "timestamp", "retireFedLocked", "restoreFedLocked", "restartOutputLocked", "close", "Frame", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class BufferedAudioStream implements Closeable {
    public static final int AUDIO_BUFFER_BYTES = 8388608;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final long FEED_POLL_MILLIS = 20;
    public static final long LEAD_MILLIS = 1000;
    private static final int LENGTH_BYTES = 2;
    private static final long MAX_BUFFER_MILLIS = 120000;
    private static final int NONCE_BYTES = 8;
    private static final long NTP_UNIX_OFFSET = 2208988800L;
    private static final int RTP_HEADER = 12;
    private static final long SAMPLES_PER_FRAME = 1024;
    public static final long START_LATENCY_MILLIS = 400;
    private static final long STATS_WINDOW_NS = 10000000000L;
    public static final int STREAM_TYPE = 103;
    private static final String TAG = "xcertplay-usb";
    private static final int TAG_BYTES = 16;
    private static final long U32 = 4294967295L;
    private BigInteger anchorNtp;
    private Long anchorRtp;
    private final int boundPort;
    private boolean cleanupStarted;
    private volatile Socket client;
    private final AtomicBoolean closed;
    private final InetAddress expectedPeer;
    private final ArrayDeque<Frame> fed;
    private long fedSamples;
    private long feedStartNs;
    private Long feedStartTimestamp;
    private Long flushUntil;
    private final AudioFormat format;
    private final AudioStreamId id;
    private final byte[] key;
    private final Object lock;
    private final Function1<String, Unit> log;
    private final int maxBufferedFrames;
    private final Function0<Long> nanoTime;
    private volatile boolean outputCleanupComplete;
    private long outputGeneration;
    private final ArrayDeque<Frame> queue;
    private long queuedBytes;
    private int rate;
    private final ServerSocket server;
    private final MediaSink sink;
    private final AtomicBoolean started;

    /* JADX WARN: Multi-variable type inference failed */
    public BufferedAudioStream(byte[] key, AudioFormat format, MediaSink sink, Function1<? super String, Unit> log, Function0<Long> nanoTime, InetAddress expectedPeer) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(format, "format");
        Intrinsics.checkNotNullParameter(sink, "sink");
        Intrinsics.checkNotNullParameter(log, "log");
        Intrinsics.checkNotNullParameter(nanoTime, "nanoTime");
        Intrinsics.checkNotNullParameter(expectedPeer, "expectedPeer");
        this.key = key;
        this.format = format;
        this.sink = sink;
        this.log = log;
        this.nanoTime = nanoTime;
        this.expectedPeer = expectedPeer;
        this.id = new AudioStreamId(this.format.getPayloadType(), this.format.getAudioType());
        this.closed = new AtomicBoolean(false);
        this.started = new AtomicBoolean(false);
        this.server = new ServerSocket(0);
        this.boundPort = this.server.getLocalPort();
        this.lock = new Object();
        this.queue = new ArrayDeque<>();
        this.fed = new ArrayDeque<>();
        this.maxBufferedFrames = (int) ((((long) this.format.getSampleRate()) * MAX_BUFFER_MILLIS) / 1024000);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ BufferedAudioStream(byte[] bArr, AudioFormat audioFormat, MediaSink mediaSink, Function1 function1, Function0 function0, InetAddress inetAddress, int i, DefaultConstructorMarker defaultConstructorMarker) {
        InetAddress inetAddress2;
        Function1 function2 = (i & 8) != 0 ? new Function1() { // from class: com.carplay.airplay.BufferedAudioStream$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BufferedAudioStream._init_$lambda$0((String) obj);
            }
        } : function1;
        Function0 function3 = (i & 16) != 0 ? AnonymousClass2.INSTANCE : function0;
        if ((i & 32) != 0) {
            InetAddress loopbackAddress = InetAddress.getLoopbackAddress();
            Intrinsics.checkNotNullExpressionValue(loopbackAddress, "getLoopbackAddress(...)");
            inetAddress2 = loopbackAddress;
        } else {
            inetAddress2 = inetAddress;
        }
        this(bArr, audioFormat, mediaSink, function2, function3, inetAddress2);
    }

    /* JADX INFO: renamed from: com.carplay.airplay.BufferedAudioStream$2, reason: invalid class name */
    /* JADX INFO: compiled from: BufferedAudioStream.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass2 extends FunctionReferenceImpl implements Function0<Long> {
        public static final AnonymousClass2 INSTANCE = new AnonymousClass2();

        AnonymousClass2() {
            super(0, System.class, "nanoTime", "nanoTime()J", 0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        public final Long invoke() {
            return Long.valueOf(System.nanoTime());
        }
    }

    static final Unit _init_$lambda$0(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    /* JADX INFO: compiled from: BufferedAudioStream.kt */
    @Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0012\n\u0002\b\t\b\u0000\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0004\b\b\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u0010"}, d2 = {"Lcom/carplay/airplay/BufferedAudioStream$Frame;", "", "sequence", "", "timestamp", "", "rtp", "", "<init>", "(IJ[B)V", "getSequence", "()I", "getTimestamp", "()J", "getRtp", "()[B", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Frame {
        private final byte[] rtp;
        private final int sequence;
        private final long timestamp;

        public Frame(int sequence, long timestamp, byte[] rtp) {
            Intrinsics.checkNotNullParameter(rtp, "rtp");
            this.sequence = sequence;
            this.timestamp = timestamp;
            this.rtp = rtp;
        }

        public final byte[] getRtp() {
            return this.rtp;
        }

        public final int getSequence() {
            return this.sequence;
        }

        public final long getTimestamp() {
            return this.timestamp;
        }
    }

    /* JADX INFO: renamed from: getOutputCleanupComplete$core_airplay, reason: from getter */
    public final boolean getOutputCleanupComplete() {
        return this.outputCleanupComplete;
    }

    /* JADX INFO: renamed from: getPort, reason: from getter */
    public final int getBoundPort() {
        return this.boundPort;
    }

    public final void start() {
        if (this.closed.get() || !this.started.compareAndSet(false, true)) {
            return;
        }
        Thread $this$start_u24lambda_u242 = new Thread(new Runnable() { // from class: com.carplay.airplay.BufferedAudioStream$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() throws IOException {
                this.f$0.receive();
            }
        }, "airplay-buffered-rx");
        $this$start_u24lambda_u242.setDaemon(true);
        $this$start_u24lambda_u242.start();
        Thread $this$start_u24lambda_u244 = new Thread(new Runnable() { // from class: com.carplay.airplay.BufferedAudioStream$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.feed();
            }
        }, "airplay-buffered-feed");
        $this$start_u24lambda_u244.setDaemon(true);
        $this$start_u24lambda_u244.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:115:0x0259  */
    public final void receive() throws IOException {
        Object obj;
        Unit unit;
        Unit unit2;
        long receivedBytes;
        long windowStart;
        boolean z;
        boolean z2;
        Socket socket = null;
        try {
            try {
                Socket socketAcceptPeer = acceptPeer();
                if (socketAcceptPeer == null) {
                    try {
                        Result.Companion companion = Result.INSTANCE;
                        BufferedAudioStream bufferedAudioStream = this;
                        Result.m159constructorimpl(null);
                    } catch (Throwable th) {
                        Result.Companion companion2 = Result.INSTANCE;
                        Result.m159constructorimpl(ResultKt.createFailure(th));
                    }
                    synchronized (this.lock) {
                        if (this.client == null) {
                            this.client = null;
                        }
                        Unit unit3 = Unit.INSTANCE;
                    }
                    return;
                }
                Socket socket2 = socketAcceptPeer;
                this.server.close();
                this.log.invoke("Buffered audio: connected");
                InputStream inputStream = socket2.getInputStream();
                Intrinsics.checkNotNullExpressionValue(inputStream, "getInputStream(...)");
                DataInputStream input = new DataInputStream(inputStream instanceof BufferedInputStream ? (BufferedInputStream) inputStream : new BufferedInputStream(inputStream, 8192));
                long windowStart2 = this.nanoTime.invoke().longValue();
                long receivedBytes2 = 0;
                int failures = 0;
                while (!this.closed.get()) {
                    int length = input.readUnsignedShort();
                    if (length < 38) {
                        this.log.invoke("Buffered audio: invalid frame length " + length + "; closed");
                        close();
                        try {
                            Result.Companion companion3 = Result.INSTANCE;
                            BufferedAudioStream bufferedAudioStream2 = this;
                            socket2.close();
                            Result.m159constructorimpl(Unit.INSTANCE);
                        } catch (Throwable th2) {
                            Result.Companion companion4 = Result.INSTANCE;
                            Result.m159constructorimpl(ResultKt.createFailure(th2));
                        }
                        synchronized (this.lock) {
                            if (this.client == socket2) {
                                this.client = socket;
                            }
                            Unit unit4 = Unit.INSTANCE;
                        }
                        return;
                    }
                    byte[] body = new byte[length - 2];
                    input.readFully(body);
                    Frame frame = INSTANCE.open$core_airplay(this.key, body);
                    if (frame == null) {
                        int failures2 = failures + 1;
                        if (failures < 3) {
                            this.log.invoke("Buffered audio: frame did not open (" + body.length + " bytes)");
                        }
                        failures = failures2;
                        socket = null;
                    } else {
                        long receivedBytes3 = receivedBytes2 + ((long) length);
                        long now = this.nanoTime.invoke().longValue();
                        if (now - windowStart2 >= STATS_WINDOW_NS) {
                            synchronized (this.lock) {
                                try {
                                    try {
                                        long queuedMillis = ((((long) this.queue.size()) * SAMPLES_PER_FRAME) * 1000) / ((long) this.format.getSampleRate());
                                        this.log.invoke("Buffered audio: queued " + (queuedMillis / 1000) + " s, received " + ((((8 * receivedBytes3) * 1000000) / ((now - windowStart2) / 1000)) / 1000) + " kbit/s");
                                        receivedBytes3 = 0;
                                        windowStart2 = now;
                                    } catch (Throwable th3) {
                                        th = th3;
                                        throw th;
                                    }
                                } catch (Throwable th4) {
                                    th = th4;
                                }
                            }
                        }
                        if (frame.getRtp().length > 12) {
                            synchronized (this.lock) {
                                while (true) {
                                    try {
                                        if (!this.closed.get()) {
                                            Long l = this.flushUntil;
                                            if (l != null) {
                                                long it = l.longValue();
                                                receivedBytes = receivedBytes3;
                                                try {
                                                    windowStart = windowStart2;
                                                    try {
                                                        z2 = INSTANCE.before$core_airplay(frame.getTimestamp(), it);
                                                        if (!!z2 || (this.queuedBytes + ((long) frame.getRtp().length) <= 8388608 && this.queue.size() + this.fed.size() < this.maxBufferedFrames)) {
                                                            break;
                                                        }
                                                        this.lock.wait();
                                                        receivedBytes3 = receivedBytes;
                                                        windowStart2 = windowStart;
                                                    } catch (Throwable th5) {
                                                        th = th5;
                                                        throw th;
                                                    }
                                                } catch (Throwable th6) {
                                                    th = th6;
                                                }
                                            } else {
                                                receivedBytes = receivedBytes3;
                                                windowStart = windowStart2;
                                            }
                                            if (!z2) {
                                                break;
                                            }
                                            break;
                                            break;
                                        }
                                        receivedBytes = receivedBytes3;
                                        windowStart = windowStart2;
                                        break;
                                    } catch (Throwable th7) {
                                        th = th7;
                                    }
                                }
                                if (!this.closed.get()) {
                                    Long l2 = this.flushUntil;
                                    if (l2 != null) {
                                        long it2 = l2.longValue();
                                        if (INSTANCE.before$core_airplay(frame.getTimestamp(), it2)) {
                                            z = true;
                                        } else {
                                            z = false;
                                        }
                                    } else {
                                        z = false;
                                    }
                                    if (!z) {
                                        this.queue.addLast(frame);
                                        this.queuedBytes += (long) frame.getRtp().length;
                                        this.lock.notifyAll();
                                    }
                                    Unit unit5 = Unit.INSTANCE;
                                }
                            }
                            Result.Companion companion5 = Result.INSTANCE;
                            socket2.close();
                            Result.m159constructorimpl(Unit.INSTANCE);
                            synchronized (this.lock) {
                                if (this.client == socket2) {
                                    this.client = null;
                                }
                                Unit unit6 = Unit.INSTANCE;
                            }
                            return;
                        }
                        receivedBytes = receivedBytes3;
                        windowStart = windowStart2;
                        failures = failures;
                        socket2 = socket2;
                        input = input;
                        receivedBytes2 = receivedBytes;
                        windowStart2 = windowStart;
                        socket = null;
                    }
                }
                try {
                    Result.Companion companion6 = Result.INSTANCE;
                    BufferedAudioStream bufferedAudioStream3 = this;
                    socket2.close();
                    Result.m159constructorimpl(Unit.INSTANCE);
                } catch (Throwable th8) {
                    Result.Companion companion7 = Result.INSTANCE;
                    Result.m159constructorimpl(ResultKt.createFailure(th8));
                }
                obj = this.lock;
                synchronized (obj) {
                    if (this.client == socket2) {
                        this.client = null;
                    }
                    Unit unit7 = Unit.INSTANCE;
                }
            } catch (Throwable th9) {
                try {
                    Result.Companion companion8 = Result.INSTANCE;
                    Socket socket3 = (Socket) null;
                    if (socket3 != null) {
                        socket3.close();
                        unit2 = Unit.INSTANCE;
                    } else {
                        unit2 = null;
                    }
                    Result.m159constructorimpl(unit2);
                } catch (Throwable th10) {
                    Result.Companion companion9 = Result.INSTANCE;
                    Result.m159constructorimpl(ResultKt.createFailure(th10));
                }
                synchronized (this.lock) {
                    if (this.client == null) {
                        this.client = null;
                    }
                    Unit unit8 = Unit.INSTANCE;
                    throw th9;
                }
            }
        } catch (Exception error) {
            if (!this.closed.get()) {
                this.log.invoke("Buffered audio: connection ended (" + error.getClass().getSimpleName() + ")");
            }
            try {
                Result.Companion companion10 = Result.INSTANCE;
                Socket socket4 = (Socket) null;
                if (socket4 != null) {
                    socket4.close();
                    unit = Unit.INSTANCE;
                } else {
                    unit = null;
                }
                Result.m159constructorimpl(unit);
            } catch (Throwable th11) {
                Result.Companion companion11 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th11));
            }
            obj = this.lock;
            synchronized (obj) {
                if (this.client == null) {
                    this.client = null;
                }
                Unit unit9 = Unit.INSTANCE;
            }
        }
    }

    private final Socket acceptPeer() throws IOException {
        boolean accepted;
        while (!this.closed.get()) {
            Socket socket = this.server.accept();
            synchronized (this.lock) {
                if (this.closed.get()) {
                    try {
                        Result.Companion companion = Result.INSTANCE;
                        BufferedAudioStream bufferedAudioStream = this;
                        socket.close();
                        Result.m159constructorimpl(Unit.INSTANCE);
                    } catch (Throwable th) {
                        Result.Companion companion2 = Result.INSTANCE;
                        Result.m159constructorimpl(ResultKt.createFailure(th));
                    }
                    return null;
                }
                if (!Intrinsics.areEqual(socket.getInetAddress(), this.expectedPeer)) {
                    try {
                        Result.Companion companion3 = Result.INSTANCE;
                        BufferedAudioStream bufferedAudioStream2 = this;
                        socket.close();
                        Result.m159constructorimpl(Unit.INSTANCE);
                    } catch (Throwable th2) {
                        Result.Companion companion4 = Result.INSTANCE;
                        Result.m159constructorimpl(ResultKt.createFailure(th2));
                    }
                    this.log.invoke("Buffered audio: rejected sender outside the control session");
                    accepted = false;
                } else {
                    this.client = socket;
                    accepted = true;
                }
                if (accepted) {
                    return socket;
                }
                throw th;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void feed() {
        while (!this.closed.get()) {
            try {
                synchronized (this.lock) {
                    if (this.closed.get()) {
                        return;
                    }
                    retireFedLocked();
                    if (this.rate == 0 || this.queue.isEmpty() || leadMillisLocked() >= 1000) {
                        this.lock.wait(FEED_POLL_MILLIS);
                    } else {
                        Frame frame = this.queue.pollFirst();
                        if (frame != null) {
                            this.fed.addLast(frame);
                            long generation = this.outputGeneration;
                            if (this.feedStartTimestamp == null) {
                                this.feedStartTimestamp = Long.valueOf(frame.getTimestamp());
                                this.feedStartNs = this.nanoTime.invoke().longValue();
                                this.fedSamples = 0L;
                                this.sink.onAudioStarted(this.id, this.format, (int) frame.getTimestamp());
                            }
                            if (!this.closed.get() && this.rate != 0 && generation == this.outputGeneration) {
                                this.fedSamples += SAMPLES_PER_FRAME;
                                this.sink.onAudioRtp(this.id, this.format, frame.getRtp(), (int) frame.getTimestamp());
                            }
                        }
                    }
                    Unit unit = Unit.INSTANCE;
                }
            } catch (InterruptedException e) {
                return;
            } catch (Exception error) {
                if (!this.closed.get()) {
                    Log.w("xcertplay-usb", "buffered audio feed failed", error);
                }
                close();
                return;
            }
        }
    }

    private final long leadMillisLocked() {
        if (this.feedStartTimestamp == null) {
            return 0L;
        }
        long playedMillis = (this.nanoTime.invoke().longValue() - this.feedStartNs) / 1000000;
        return ((this.fedSamples * 1000) / ((long) this.format.getSampleRate())) - playedMillis;
    }

    public final Map<String, Object> setRate(Long rtpTime, int newRate, BigInteger nowNtp) {
        Map<String, Object> mapAnchorLocked;
        Intrinsics.checkNotNullParameter(nowNtp, "nowNtp");
        synchronized (this.lock) {
            Long lValueOf = null;
            mapAnchorLocked = null;
            if (!this.closed.get()) {
                if (newRate > 0) {
                    if (rtpTime == null) {
                        Long lPositionLocked = positionLocked();
                        if (lPositionLocked == null) {
                            Frame framePeekFirst = this.queue.peekFirst();
                            if (framePeekFirst != null) {
                                lValueOf = Long.valueOf(framePeekFirst.getTimestamp());
                            }
                        } else {
                            lValueOf = lPositionLocked;
                        }
                    } else {
                        lValueOf = rtpTime;
                    }
                    restoreFedLocked();
                    if (lValueOf != null) {
                        dropBeforeLocked(lValueOf.longValue());
                    }
                    restartOutputLocked();
                    this.anchorRtp = lValueOf;
                    this.anchorNtp = nowNtp.add(INSTANCE.millisToNtp$core_airplay(400L));
                } else {
                    Long lPositionLocked2 = positionLocked();
                    restoreFedLocked();
                    if (lPositionLocked2 != null) {
                        long jLongValue = lPositionLocked2.longValue();
                        this.anchorRtp = Long.valueOf(jLongValue);
                        dropBeforeLocked(jLongValue);
                    }
                    this.anchorNtp = nowNtp;
                    restartOutputLocked();
                }
                this.rate = RangesKt.coerceIn(newRate, 0, 1);
                this.lock.notifyAll();
                mapAnchorLocked = anchorLocked();
            }
        }
        return mapAnchorLocked;
    }

    public final Map<String, Object> anchor() {
        Map<String, Object> mapAnchorLocked;
        synchronized (this.lock) {
            mapAnchorLocked = anchorLocked();
        }
        return mapAnchorLocked;
    }

    public final void flush(Long untilTimestamp) {
        synchronized (this.lock) {
            if (!this.closed.get()) {
                restoreFedLocked();
                if (untilTimestamp != null) {
                    dropBeforeLocked(untilTimestamp.longValue());
                }
                restartOutputLocked();
                this.lock.notifyAll();
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public final Map<String, Object> feedback(BigInteger nowNtp, Object connectionId) {
        LinkedHashMap $this$feedback_u24lambda_u2421_u24lambda_u2420;
        Intrinsics.checkNotNullParameter(nowNtp, "nowNtp");
        synchronized (this.lock) {
            $this$feedback_u24lambda_u2421_u24lambda_u2420 = MapsKt.linkedMapOf(TuplesKt.to("type", Integer.valueOf(this.format.getPayloadType())), TuplesKt.to("sampleRate", Integer.valueOf(this.format.getSampleRate())));
            Long lPositionLocked = positionLocked();
            if (lPositionLocked != null) {
                long position = lPositionLocked.longValue();
                $this$feedback_u24lambda_u2421_u24lambda_u2420.put("streamConnectionID", CarPlayMediaEngineKt.unsignedPlistInteger(connectionId == null ? 0L : connectionId));
                $this$feedback_u24lambda_u2421_u24lambda_u2420.put("timestamp", nowNtp);
                $this$feedback_u24lambda_u2421_u24lambda_u2420.put("sampleTime", Long.valueOf(position));
            }
        }
        return $this$feedback_u24lambda_u2421_u24lambda_u2420;
    }

    private final Long positionLocked() {
        Long start = this.feedStartTimestamp;
        if (start != null && this.rate > 0) {
            long playedMillis = Math.max(0L, ((this.nanoTime.invoke().longValue() - this.feedStartNs) / 1000000) - 400);
            return Long.valueOf((start.longValue() + ((((long) this.format.getSampleRate()) * playedMillis) / 1000)) & 4294967295L);
        }
        return this.anchorRtp;
    }

    private final Map<String, Object> anchorLocked() {
        Long l = this.anchorRtp;
        if (l == null) {
            return null;
        }
        long rtp = l.longValue();
        BigInteger at = this.anchorNtp;
        if (at == null) {
            return null;
        }
        return INSTANCE.anchorPlist$core_airplay(rtp, at, this.rate);
    }

    private final void dropBeforeLocked(long timestamp) {
        this.flushUntil = Long.valueOf(timestamp);
        while (true) {
            Frame it = this.queue.peekFirst();
            boolean z = false;
            if (it != null && INSTANCE.before$core_airplay(it.getTimestamp(), timestamp)) {
                z = true;
            }
            if (z) {
                long j = this.queuedBytes;
                Frame framePollFirst = this.queue.pollFirst();
                Intrinsics.checkNotNull(framePollFirst);
                this.queuedBytes = j - ((long) framePollFirst.getRtp().length);
            } else {
                this.lock.notifyAll();
                return;
            }
        }
    }

    private final void retireFedLocked() {
        Long lPositionLocked = positionLocked();
        if (lPositionLocked == null) {
            return;
        }
        long position = lPositionLocked.longValue();
        while (true) {
            Frame frame = this.fed.peekFirst();
            boolean z = false;
            if (frame != null) {
                long end = (frame.getTimestamp() + SAMPLES_PER_FRAME) & 4294967295L;
                if (end == position || INSTANCE.before$core_airplay(end, position)) {
                    z = true;
                }
            }
            if (z) {
                this.queuedBytes -= (long) this.fed.removeFirst().getRtp().length;
            } else {
                this.lock.notifyAll();
                return;
            }
        }
    }

    private final void restoreFedLocked() {
        while (!this.fed.isEmpty()) {
            this.queue.addFirst(this.fed.removeLast());
        }
    }

    private final void restartOutputLocked() {
        Object objM159constructorimpl;
        this.outputGeneration++;
        if (this.feedStartTimestamp != null) {
            try {
                Result.Companion companion = Result.INSTANCE;
                BufferedAudioStream $this$restartOutputLocked_u24lambda_u2424 = this;
                $this$restartOutputLocked_u24lambda_u2424.sink.onAudioStopped($this$restartOutputLocked_u24lambda_u2424.id);
                objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            Throwable it = Result.m162exceptionOrNullimpl(objM159constructorimpl);
            if (it != null) {
                this.log.invoke("Buffered audio: output cleanup failed (" + it.getClass().getSimpleName() + ")");
            }
        }
        this.feedStartTimestamp = null;
        this.fedSamples = 0L;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        Unit unit;
        this.closed.set(true);
        try {
            Result.Companion companion = Result.INSTANCE;
            BufferedAudioStream $this$close_u24lambda_u2426 = this;
            $this$close_u24lambda_u2426.server.close();
            Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        synchronized (this.lock) {
            if (this.cleanupStarted) {
                return;
            }
            this.cleanupStarted = true;
            try {
                try {
                    Result.Companion companion3 = Result.INSTANCE;
                    BufferedAudioStream $this$close_u24lambda_u2428_u24lambda_u2427 = this;
                    Socket socket = $this$close_u24lambda_u2428_u24lambda_u2427.client;
                    if (socket != null) {
                        socket.close();
                        unit = Unit.INSTANCE;
                    } else {
                        unit = null;
                    }
                    Result.m159constructorimpl(unit);
                } catch (Throwable th2) {
                    Result.Companion companion4 = Result.INSTANCE;
                    Result.m159constructorimpl(ResultKt.createFailure(th2));
                }
                this.client = null;
                this.queue.clear();
                this.fed.clear();
                this.queuedBytes = 0L;
                this.rate = 0;
                restartOutputLocked();
                this.outputCleanupComplete = true;
                this.lock.notifyAll();
                Unit unit2 = Unit.INSTANCE;
            } catch (Throwable th3) {
                this.outputCleanupComplete = true;
                this.lock.notifyAll();
                throw th3;
            }
        }
    }

    /* JADX INFO: compiled from: BufferedAudioStream.kt */
    @Metadata(d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u001f\u0010\u0016\u001a\u0004\u0018\u00010\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0019H\u0000¢\u0006\u0002\b\u001bJ3\u0010\u001c\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u001d2\u0006\u0010\u001e\u001a\u00020\n2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u0007H\u0000¢\u0006\u0002\b\"J\u0015\u0010#\u001a\u00020 2\u0006\u0010$\u001a\u00020\nH\u0000¢\u0006\u0002\b%J\u001d\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020\n2\u0006\u0010)\u001a\u00020\nH\u0000¢\u0006\u0002\b*J\u0018\u0010+\u001a\u00020\n2\u0006\u0010,\u001a\u00020\u00192\u0006\u0010-\u001a\u00020\u0007H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0080T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\nX\u0080T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\nX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\nX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\nX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\nX\u0082T¢\u0006\u0002\n\u0000¨\u0006."}, d2 = {"Lcom/carplay/airplay/BufferedAudioStream$Companion;", "", "<init>", "()V", "TAG", "", "STREAM_TYPE", "", "AUDIO_BUFFER_BYTES", "MAX_BUFFER_MILLIS", "", "LEAD_MILLIS", "START_LATENCY_MILLIS", "FEED_POLL_MILLIS", "STATS_WINDOW_NS", "SAMPLES_PER_FRAME", "LENGTH_BYTES", "RTP_HEADER", "NONCE_BYTES", "TAG_BYTES", "U32", "NTP_UNIX_OFFSET", "open", "Lcom/carplay/airplay/BufferedAudioStream$Frame;", "key", "", "body", "open$core_airplay", "anchorPlist", "", "rtp", "ntp", "Ljava/math/BigInteger;", "rate", "anchorPlist$core_airplay", "millisToNtp", "millis", "millisToNtp$core_airplay", "before", "", "a", "b", "before$core_airplay", "u32", "bytes", "offset", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Frame open$core_airplay(byte[] key, byte[] body) {
            Object objM159constructorimpl;
            Intrinsics.checkNotNullParameter(key, "key");
            Intrinsics.checkNotNullParameter(body, "body");
            if (body.length < 36 || (body[0] & UByte.MAX_VALUE) != 128) {
                return null;
            }
            int sealedEnd = body.length - 8;
            byte[] nonce = new byte[12];
            ArraysKt.copyInto(body, nonce, 4, sealedEnd, body.length);
            try {
                Result.Companion companion = Result.INSTANCE;
                Companion companion2 = this;
                objM159constructorimpl = Result.m159constructorimpl(AirPlayCrypto.INSTANCE.chachaOpen(key, nonce, ArraysKt.copyOfRange(body, 12, sealedEnd), ArraysKt.copyOfRange(body, 4, 12)));
            } catch (Throwable th) {
                Result.Companion companion3 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            if (Result.m165isFailureimpl(objM159constructorimpl)) {
                objM159constructorimpl = null;
            }
            byte[] payload = (byte[]) objM159constructorimpl;
            if (payload == null) {
                return null;
            }
            int sequence = ((body[2] & UByte.MAX_VALUE) << 8) | (body[3] & UByte.MAX_VALUE);
            long jU32 = u32(body, 4);
            byte[] bArrCopyOf = Arrays.copyOf(body, 12);
            Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
            return new Frame(sequence, jU32, ArraysKt.plus(bArrCopyOf, payload));
        }

        public final Map<String, Object> anchorPlist$core_airplay(long rtp, BigInteger ntp, int rate) {
            Intrinsics.checkNotNullParameter(ntp, "ntp");
            return MapsKt.linkedMapOf(TuplesKt.to("rtpTime", Long.valueOf(rtp & 4294967295L)), TuplesKt.to("networkTimeSecs", Long.valueOf(ntp.shiftRight(32).longValue() - BufferedAudioStream.NTP_UNIX_OFFSET)), TuplesKt.to("networkTimeFrac", ntp.and(BigInteger.valueOf(4294967295L)).shiftLeft(32)), TuplesKt.to("rate", Integer.valueOf(rate)));
        }

        public final BigInteger millisToNtp$core_airplay(long millis) {
            BigInteger bigIntegerDivide = BigInteger.valueOf(millis).shiftLeft(32).divide(BigInteger.valueOf(1000L));
            Intrinsics.checkNotNullExpressionValue(bigIntegerDivide, "divide(...)");
            return bigIntegerDivide;
        }

        public final boolean before$core_airplay(long a, long b) {
            return ((a - b) & 4294967295L) >= 2147483648L;
        }

        private final long u32(byte[] bytes, int offset) {
            return ((((long) bytes[offset]) & 255) << 24) | ((((long) bytes[offset + 1]) & 255) << 16) | ((((long) bytes[offset + 2]) & 255) << 8) | (255 & ((long) bytes[offset + 3]));
        }
    }
}
