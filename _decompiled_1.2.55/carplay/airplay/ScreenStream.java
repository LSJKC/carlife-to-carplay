package com.carplay.airplay;

import android.util.Log;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.UByte;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: ScreenStream.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 *2\u00020\u0001:\u0002)*B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0014\b\u0002\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005¢\u0006\u0004\b\b\u0010\tJ\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0015\u001a\u00020\u0016J\b\u0010\u0019\u001a\u00020\u0007H\u0016J\u0010\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u0010H\u0002J\u0010\u0010\u001c\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u0012H\u0002J(\u0010\u001e\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020\u00032\u0006\u0010 \u001a\u00020\u00032\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$H\u0002J\u001a\u0010%\u001a\u0004\u0018\u00010\u00032\u0006\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020\u0018H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006+"}, d2 = {"Lcom/carplay/airplay/ScreenStream;", "Ljava/io/Closeable;", "key", "", "onDiagnostic", "Lkotlin/Function1;", "", "", "<init>", "([BLkotlin/jvm/functions/Function1;)V", "closed", "Ljava/util/concurrent/atomic/AtomicBoolean;", "frameCounter", "Ljava/util/concurrent/atomic/AtomicLong;", "firstFrameLogged", "server", "Ljava/net/ServerSocket;", "socket", "Ljava/net/Socket;", "thread", "Ljava/lang/Thread;", "listener", "Lcom/carplay/airplay/ScreenStream$Listener;", "listen", "", "close", "accept", "bound", "run", "sock", "onMessage", "header", "body", "stats", "Lcom/carplay/airplay/StreamReceiveStats;", "arrivalNanos", "", "readFully", "input", "Ljava/io/InputStream;", "length", "Listener", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ScreenStream implements Closeable {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int HEADER_LEN = 128;

    @Deprecated
    public static final int MAX_BODY = 8388608;

    @Deprecated
    public static final int OPCODE_OFFSET = 4;

    @Deprecated
    public static final int OP_VIDEO_CONFIG = 1;

    @Deprecated
    public static final int OP_VIDEO_FRAME = 0;

    @Deprecated
    public static final String TAG = "xcertplay-usb";
    private final AtomicBoolean closed;
    private final AtomicBoolean firstFrameLogged;
    private final AtomicLong frameCounter;
    private final byte[] key;
    private volatile Listener listener;
    private final Function1<String, Unit> onDiagnostic;
    private ServerSocket server;
    private Socket socket;
    private Thread thread;

    /* JADX INFO: compiled from: ScreenStream.kt */
    @Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\bH\u0016J \u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0016J\u0012\u0010\u000e\u001a\u00020\u00032\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0016¨\u0006\u0011À\u0006\u0003"}, d2 = {"Lcom/carplay/airplay/ScreenStream$Listener;", "", "onCodec", "", "codec", "Lcom/carplay/airplay/VideoCodec;", "onConfig", "codecData", "", "onFrame", "naluBytes", "senderNanos", "", "arrivalNanos", "onClosed", "cause", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public interface Listener {

        /* JADX INFO: compiled from: ScreenStream.kt */
        @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
        public static final class DefaultImpls {
            @Deprecated
            public static void onCodec(Listener $this, VideoCodec codec) {
                Intrinsics.checkNotNullParameter(codec, "codec");
                Listener.super.onCodec(codec);
            }

            @Deprecated
            public static void onConfig(Listener $this, byte[] codecData) {
                Intrinsics.checkNotNullParameter(codecData, "codecData");
                Listener.super.onConfig(codecData);
            }

            @Deprecated
            public static void onFrame(Listener $this, byte[] naluBytes) {
                Intrinsics.checkNotNullParameter(naluBytes, "naluBytes");
                Listener.super.onFrame(naluBytes);
            }

            @Deprecated
            public static void onFrame(Listener $this, byte[] naluBytes, long senderNanos, long arrivalNanos) {
                Intrinsics.checkNotNullParameter(naluBytes, "naluBytes");
                Listener.super.onFrame(naluBytes, senderNanos, arrivalNanos);
            }

            @Deprecated
            public static void onClosed(Listener $this, Throwable cause) {
                Listener.super.onClosed(cause);
            }
        }

        default void onCodec(VideoCodec codec) {
            Intrinsics.checkNotNullParameter(codec, "codec");
        }

        default void onConfig(byte[] codecData) {
            Intrinsics.checkNotNullParameter(codecData, "codecData");
        }

        default void onFrame(byte[] naluBytes) {
            Intrinsics.checkNotNullParameter(naluBytes, "naluBytes");
        }

        default void onFrame(byte[] naluBytes, long senderNanos, long arrivalNanos) {
            Intrinsics.checkNotNullParameter(naluBytes, "naluBytes");
            onFrame(naluBytes);
        }

        default void onClosed(Throwable cause) {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ScreenStream(byte[] key, Function1<? super String, Unit> onDiagnostic) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(onDiagnostic, "onDiagnostic");
        this.key = key;
        this.onDiagnostic = onDiagnostic;
        this.closed = new AtomicBoolean(false);
        this.frameCounter = new AtomicLong(0L);
        this.firstFrameLogged = new AtomicBoolean(false);
        this.listener = new Listener() { // from class: com.carplay.airplay.ScreenStream$listener$1
            @Override // com.carplay.airplay.ScreenStream.Listener
            public void onClosed(Throwable cause) {
                super.onClosed(cause);
            }

            @Override // com.carplay.airplay.ScreenStream.Listener
            public void onCodec(VideoCodec codec) {
                super.onCodec(codec);
            }

            @Override // com.carplay.airplay.ScreenStream.Listener
            public void onConfig(byte[] codecData) {
                super.onConfig(codecData);
            }

            @Override // com.carplay.airplay.ScreenStream.Listener
            public void onFrame(byte[] naluBytes) {
                super.onFrame(naluBytes);
            }

            @Override // com.carplay.airplay.ScreenStream.Listener
            public void onFrame(byte[] naluBytes, long senderNanos, long arrivalNanos) {
                super.onFrame(naluBytes, senderNanos, arrivalNanos);
            }
        };
    }

    public /* synthetic */ ScreenStream(byte[] bArr, Function1 function1, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(bArr, (i & 2) != 0 ? new Function1() { // from class: com.carplay.airplay.ScreenStream$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ScreenStream._init_$lambda$0((String) obj);
            }
        } : function1);
    }

    static final Unit _init_$lambda$0(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    public final int listen(Listener listener) throws IOException {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.listener = listener;
        final ServerSocket bound = new ServerSocket();
        bound.setReuseAddress(true);
        bound.bind(new InetSocketAddress(InetAddress.getByName("::"), 0));
        this.server = bound;
        Thread $this$listen_u24lambda_u242 = new Thread(new Runnable() { // from class: com.carplay.airplay.ScreenStream$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() throws Throwable {
                this.f$0.accept(bound);
            }
        }, "airplay-screen");
        $this$listen_u24lambda_u242.setDaemon(true);
        $this$listen_u24lambda_u242.start();
        this.thread = $this$listen_u24lambda_u242;
        return bound.getLocalPort();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.closed.compareAndSet(false, true)) {
            AirPlaySessionKt.safeClose(this.socket);
            AirPlaySessionKt.safeClose(this.server);
            Thread thread = this.thread;
            if (thread != null) {
                thread.interrupt();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void accept(ServerSocket bound) throws Throwable {
        try {
            Socket accepted = bound.accept();
            this.socket = accepted;
            Intrinsics.checkNotNull(accepted);
            run(accepted);
        } catch (Exception error) {
            if (!this.closed.get()) {
                this.listener.onClosed(error);
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:50:0x00b6  */
    /* JADX WARN: Code duplicated, block: B:53:0x00c6  */
    private final void run(Socket sock) throws Throwable {
        boolean z;
        int bodySize;
        byte[] body;
        Throwable failure = null;
        StreamReceiveStats stats = new StreamReceiveStats("video", this.onDiagnostic, null, 4, null);
        boolean z2 = true;
        try {
            InputStream input = sock.getInputStream();
            while (!this.closed.get()) {
                stats.reading();
                Intrinsics.checkNotNull(input);
                byte[] header = readFully(input, 128);
                if (header == null || (bodySize = ScreenStreamKt.readU32Le(header, 0)) > 8388608 || (body = readFully(input, bodySize)) == null) {
                    break;
                    break;
                    break;
                }
                long arrivalNanos = System.nanoTime();
                try {
                    StreamReceiveStats.received$default(stats, bodySize + 128, null, null, 6, null);
                    z = z2;
                    try {
                        try {
                            onMessage(header, body, stats, arrivalNanos);
                            stats.processed();
                            z2 = z;
                        } catch (Exception e) {
                            error = e;
                            failure = error;
                            stats.flush(z);
                            if (this.socket == sock) {
                                this.socket = null;
                            }
                            AirPlaySessionKt.safeClose(sock);
                            if (this.closed.get()) {
                                return;
                            }
                            this.listener.onClosed(failure);
                        }
                    } catch (Throwable th) {
                        th = th;
                        stats.flush(z);
                        if (this.socket == sock) {
                            this.socket = null;
                        }
                        AirPlaySessionKt.safeClose(sock);
                        if (!this.closed.get()) {
                            this.listener.onClosed(null);
                        }
                        throw th;
                    }
                } catch (Exception e2) {
                    error = e2;
                    z = z2;
                    stats = stats;
                } catch (Throwable th2) {
                    th = th2;
                    z = z2;
                    stats = stats;
                    stats.flush(z);
                    if (this.socket == sock) {
                        this.socket = null;
                    }
                    AirPlaySessionKt.safeClose(sock);
                    if (!this.closed.get()) {
                        this.listener.onClosed(null);
                    }
                    throw th;
                }
            }
            stats.flush(z2);
            if (this.socket == sock) {
                this.socket = null;
            }
            AirPlaySessionKt.safeClose(sock);
            if (this.closed.get()) {
                return;
            }
        } catch (Exception e3) {
            error = e3;
            z = z2;
        } catch (Throwable th3) {
            th = th3;
            z = z2;
        }
        this.listener.onClosed(failure);
    }

    private final void onMessage(byte[] header, byte[] body, StreamReceiveStats stats, long arrivalNanos) {
        byte[] bArr;
        byte[] payload;
        switch (header[4] & UByte.MAX_VALUE) {
            case 0:
                if (body.length >= 16) {
                    long start = System.nanoTime();
                    bArr = header;
                    payload = ScreenCodec.INSTANCE.decryptFrame(this.key, this.frameCounter.get(), bArr, body);
                    stats.decrypted(System.nanoTime() - start, body.length);
                    this.frameCounter.incrementAndGet();
                } else {
                    bArr = header;
                    payload = body;
                }
                if (this.firstFrameLogged.compareAndSet(false, true)) {
                    Log.i("xcertplay-usb", "video first decrypted frame sealed=" + body.length + " plain=" + payload.length + " head=" + ScreenStreamKt.hexPrefix(payload, 16) + " chacha=" + AirPlayCrypto.INSTANCE.getChachaImplementation());
                }
                this.listener.onFrame(ScreenCodec.INSTANCE.lengthPrefixedToAnnexB(payload), ScreenCodec.INSTANCE.senderNanos(bArr), arrivalNanos);
                break;
            case 1:
                Pair<VideoCodec, byte[]> pairDetectConfig = ScreenCodec.INSTANCE.detectConfig(body);
                VideoCodec codec = pairDetectConfig.component1();
                byte[] codecData = pairDetectConfig.component2();
                Log.i("xcertplay-usb", "video codec config codec=" + codec + " body=" + body.length + " data=" + codecData.length);
                this.listener.onCodec(codec);
                this.listener.onConfig(codecData);
                break;
            default:
                break;
        }
    }

    private final byte[] readFully(InputStream input, int length) throws IOException {
        if (length < 0) {
            return null;
        }
        byte[] output = new byte[length];
        int offset = 0;
        while (offset < length) {
            int read = input.read(output, offset, length - offset);
            if (read < 0) {
                return null;
            }
            offset += read;
        }
        return output;
    }

    /* JADX INFO: compiled from: ScreenStream.kt */
    @Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lcom/carplay/airplay/ScreenStream$Companion;", "", "<init>", "()V", "TAG", "", "HEADER_LEN", "", "OPCODE_OFFSET", "OP_VIDEO_FRAME", "OP_VIDEO_CONFIG", "MAX_BODY", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
