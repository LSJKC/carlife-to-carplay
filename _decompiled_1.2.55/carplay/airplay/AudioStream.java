package com.carplay.airplay;

import android.util.Log;
import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.UByte;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AudioStream.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\u0018\u0000 (2\u00020\u0001:\u0002'(B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\u0014\b\u0002\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007¢\u0006\u0004\b\n\u0010\u000bJ\u001a\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u001b2\u0006\u0010\u001c\u001a\u00020\u001dJ\b\u0010\u001e\u001a\u00020\tH\u0016J\u0018\u0010\u001f\u001a\u00020\t2\u0006\u0010 \u001a\u00020\u00132\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\u0010\u0010!\u001a\u00020\t2\u0006\u0010 \u001a\u00020\u0013H\u0002J\b\u0010\"\u001a\u00020\u0013H\u0002J\u0018\u0010#\u001a\u00020\u00052\u0006\u0010$\u001a\u00020\u00032\u0006\u0010%\u001a\u00020\u0005H\u0002J\f\u0010&\u001a\u00020\b*\u00020\u0003H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006)"}, d2 = {"Lcom/carplay/airplay/AudioStream;", "Ljava/io/Closeable;", "key", "", "streamType", "", "onDiagnostic", "Lkotlin/Function1;", "", "", "<init>", "([BILkotlin/jvm/functions/Function1;)V", "closed", "Ljava/util/concurrent/atomic/AtomicBoolean;", "receivedPackets", "Ljava/util/concurrent/atomic/AtomicInteger;", "decryptedPackets", "authenticationFailures", "dataSocket", "Ljava/net/DatagramSocket;", "controlSocket", "dataThread", "Ljava/lang/Thread;", "controlThread", "started", "", "listen", "Lkotlin/Pair;", "listener", "Lcom/carplay/airplay/AudioStream$Listener;", "close", "runData", "socket", "runControl", "bindAnyPort", "readU32Be", "source", "offset", "toHexString", "Listener", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AudioStream implements Closeable {

    @Deprecated
    public static final int AUDIO_RECEIVE_BUFFER_BYTES = 524288;
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int DATAGRAM_BYTES = 4096;

    @Deprecated
    public static final int FIRST_PACKET_LOG_COUNT = 3;

    @Deprecated
    public static final int NONCE_LEN = 8;

    @Deprecated
    public static final int PACKET_LOG_INTERVAL = 100;

    @Deprecated
    public static final int RTP_HEADER_LEN = 12;

    @Deprecated
    public static final String TAG = "xcertplay-usb";

    @Deprecated
    public static final int TAG_LEN = 16;

    @Deprecated
    public static final int TAIL_LEN = 24;
    private final AtomicInteger authenticationFailures;
    private final AtomicBoolean closed;
    private DatagramSocket controlSocket;
    private Thread controlThread;
    private DatagramSocket dataSocket;
    private Thread dataThread;
    private final AtomicInteger decryptedPackets;
    private final byte[] key;
    private final Function1<String, Unit> onDiagnostic;
    private final AtomicInteger receivedPackets;
    private boolean started;
    private final int streamType;

    /* JADX WARN: Multi-variable type inference failed */
    public AudioStream(byte[] key, int streamType, Function1<? super String, Unit> onDiagnostic) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(onDiagnostic, "onDiagnostic");
        this.key = key;
        this.streamType = streamType;
        this.onDiagnostic = onDiagnostic;
        this.closed = new AtomicBoolean(false);
        this.receivedPackets = new AtomicInteger();
        this.decryptedPackets = new AtomicInteger();
        this.authenticationFailures = new AtomicInteger();
    }

    public /* synthetic */ AudioStream(byte[] bArr, int i, Function1 function1, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(bArr, (i2 & 2) != 0 ? -1 : i, (i2 & 4) != 0 ? new Function1() { // from class: com.carplay.airplay.AudioStream$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AudioStream._init_$lambda$0((String) obj);
            }
        } : function1);
    }

    static final Unit _init_$lambda$0(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    /* JADX INFO: compiled from: AudioStream.kt */
    @Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0018\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0005H\u0016J3\u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\u00052\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0016¢\u0006\u0002\u0010\u000e¨\u0006\u000fÀ\u0006\u0003"}, d2 = {"Lcom/carplay/airplay/AudioStream$Listener;", "", "onStarted", "", "firstSample", "", "onRtp", "rtp", "", "sample", "onPacket", "wire", "error", "", "([B[BLjava/lang/Integer;Ljava/lang/Throwable;)V", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public interface Listener {

        /* JADX INFO: compiled from: AudioStream.kt */
        @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
        public static final class DefaultImpls {
            @Deprecated
            public static void onStarted(Listener $this, int firstSample) {
                Listener.super.onStarted(firstSample);
            }

            @Deprecated
            public static void onRtp(Listener $this, byte[] rtp, int sample) {
                Intrinsics.checkNotNullParameter(rtp, "rtp");
                Listener.super.onRtp(rtp, sample);
            }

            @Deprecated
            public static void onPacket(Listener $this, byte[] wire, byte[] rtp, Integer sample, Throwable error) {
                Intrinsics.checkNotNullParameter(wire, "wire");
                Listener.super.onPacket(wire, rtp, sample, error);
            }
        }

        default void onStarted(int firstSample) {
        }

        default void onRtp(byte[] rtp, int sample) {
            Intrinsics.checkNotNullParameter(rtp, "rtp");
        }

        default void onPacket(byte[] wire, byte[] rtp, Integer sample, Throwable error) {
            Intrinsics.checkNotNullParameter(wire, "wire");
        }
    }

    public final Pair<Integer, Integer> listen(final Listener listener) {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        Intrinsics.checkNotNullParameter(listener, "listener");
        final DatagramSocket data = bindAnyPort();
        try {
            Result.Companion companion = Result.INSTANCE;
            AudioStream audioStream = this;
            objM159constructorimpl = Result.m159constructorimpl(Integer.valueOf(data.getReceiveBufferSize()));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = 0;
        }
        int originalBufferBytes = ((Number) objM159constructorimpl).intValue();
        if (originalBufferBytes < 524288) {
            try {
                Result.Companion companion3 = Result.INSTANCE;
                AudioStream audioStream2 = this;
                data.setReceiveBufferSize(524288);
                Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th2) {
                Result.Companion companion4 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th2));
            }
        }
        Function1<String, Unit> function1 = this.onDiagnostic;
        int i = this.streamType;
        try {
            Result.Companion companion5 = Result.INSTANCE;
            AudioStream audioStream3 = this;
            objM159constructorimpl2 = Result.m159constructorimpl(Integer.valueOf(data.getReceiveBufferSize()));
        } catch (Throwable th3) {
            Result.Companion companion6 = Result.INSTANCE;
            objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th3));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl2)) {
            objM159constructorimpl2 = 0;
        }
        function1.invoke("Audio UDP receive buffer type=" + i + " original=" + originalBufferBytes + " requested=524288 actual=" + objM159constructorimpl2);
        final DatagramSocket control = bindAnyPort();
        this.dataSocket = data;
        this.controlSocket = control;
        Thread $this$listen_u24lambda_u245 = new Thread(new Runnable() { // from class: com.carplay.airplay.AudioStream$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() throws Exception {
                this.f$0.runData(data, listener);
            }
        }, "airplay-audio-rx");
        $this$listen_u24lambda_u245.setDaemon(true);
        $this$listen_u24lambda_u245.start();
        this.dataThread = $this$listen_u24lambda_u245;
        Thread $this$listen_u24lambda_u247 = new Thread(new Runnable() { // from class: com.carplay.airplay.AudioStream$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.runControl(control);
            }
        }, "airplay-rtcp-rx");
        $this$listen_u24lambda_u247.setDaemon(true);
        $this$listen_u24lambda_u247.start();
        this.controlThread = $this$listen_u24lambda_u247;
        return TuplesKt.to(Integer.valueOf(data.getLocalPort()), Integer.valueOf(control.getLocalPort()));
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.closed.compareAndSet(false, true)) {
            DatagramSocket datagramSocket = this.dataSocket;
            if (datagramSocket != null) {
                datagramSocket.close();
            }
            DatagramSocket datagramSocket2 = this.controlSocket;
            if (datagramSocket2 != null) {
                datagramSocket2.close();
            }
            Thread thread = this.dataThread;
            if (thread != null) {
                thread.interrupt();
            }
            Thread thread2 = this.controlThread;
            if (thread2 != null) {
                thread2.interrupt();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void runData(DatagramSocket socket, Listener listener) throws Exception {
        boolean z;
        Integer numValueOf;
        byte[] buffer;
        StreamReceiveStats stats = new StreamReceiveStats("audio type=" + this.streamType, this.onDiagnostic, null, 4, null);
        byte[] rtp = new byte[4096];
        while (!this.closed.get()) {
            try {
                try {
                    DatagramPacket packet = new DatagramPacket(rtp, rtp.length);
                    try {
                        stats.reading();
                        socket.receive(packet);
                        int length = packet.getLength();
                        if (packet.getLength() >= 12) {
                            try {
                                numValueOf = Integer.valueOf(((rtp[2] & UByte.MAX_VALUE) << 8) | (rtp[3] & UByte.MAX_VALUE));
                            } catch (Throwable th) {
                                th = th;
                                z = true;
                                stats.flush(z);
                                throw th;
                            }
                        } else {
                            numValueOf = null;
                        }
                        stats.received(length, numValueOf, packet.getLength() >= 12 ? Integer.valueOf(readU32Be(rtp, 4)) : null);
                        byte[] data = packet.getData();
                        Intrinsics.checkNotNullExpressionValue(data, "getData(...)");
                        byte[] wire = Arrays.copyOf(data, packet.getLength());
                        Intrinsics.checkNotNullExpressionValue(wire, "copyOf(...)");
                        int packetNumber = this.receivedPackets.incrementAndGet();
                        if (wire.length < 36) {
                            if (packetNumber == 1) {
                                Log.w("xcertplay-usb", "audio stream type=" + this.streamType + " short packet bytes=" + wire.length);
                            }
                            listener.onPacket(wire, null, null, new IOException("audio packet shorter than RTP header plus tail"));
                            stats.processed();
                        } else {
                            byte[] aad = ArraysKt.copyOfRange(wire, 4, 12);
                            int sealedEnd = wire.length - 8;
                            byte[] sealed = ArraysKt.copyOfRange(wire, 12, sealedEnd);
                            byte[] shortNonce = ArraysKt.copyOfRange(wire, sealedEnd, wire.length);
                            byte[] nonce = new byte[12];
                            ArraysKt.copyInto$default(shortNonce, nonce, 4, 0, 0, 12, (Object) null);
                            int sample = readU32Be(wire, 4);
                            try {
                                AirPlayCrypto airPlayCrypto = AirPlayCrypto.INSTANCE;
                                buffer = rtp;
                                try {
                                    try {
                                        byte[] buffer2 = this.key;
                                        try {
                                            byte[] payload = airPlayCrypto.chachaOpen(buffer2, nonce, sealed, aad);
                                            byte[] bArrCopyOf = Arrays.copyOf(wire, 12);
                                            Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
                                            byte[] rtp2 = ArraysKt.plus(bArrCopyOf, payload);
                                            int decryptedNumber = this.decryptedPackets.incrementAndGet();
                                            if (decryptedNumber <= 3) {
                                                int i = this.streamType;
                                                int length2 = wire.length;
                                                int length3 = payload.length;
                                                int packetNumber2 = payload.length;
                                                byte[] bArrCopyOf2 = Arrays.copyOf(payload, Math.min(packetNumber2, 16));
                                                Intrinsics.checkNotNullExpressionValue(bArrCopyOf2, "copyOf(...)");
                                                Log.i("xcertplay-usb", "audio stream type=" + i + " packet=" + decryptedNumber + " sample=" + sample + " wireBytes=" + length2 + " payloadBytes=" + length3 + " payloadHead=" + toHexString(bArrCopyOf2));
                                            } else if (decryptedNumber % 100 == 0) {
                                                Log.i("xcertplay-usb", "audio stream type=" + this.streamType + " decrypted=" + decryptedNumber + " authFailures=" + this.authenticationFailures.get());
                                            }
                                            listener.onPacket(wire, rtp2, Integer.valueOf(sample), null);
                                            if (!this.started) {
                                                this.started = true;
                                                listener.onStarted(sample);
                                            }
                                            listener.onRtp(rtp2, sample);
                                            stats.processed();
                                            rtp = buffer;
                                        } catch (Exception e) {
                                            error = e;
                                            int failureNumber = this.authenticationFailures.incrementAndGet();
                                            if (failureNumber == 1) {
                                                Log.w("xcertplay-usb", "audio stream type=" + this.streamType + " first decrypt failure wire=" + toHexString(wire), error);
                                            }
                                            listener.onPacket(wire, null, Integer.valueOf(sample), error);
                                            stats.processed();
                                            rtp = buffer;
                                        }
                                    } catch (Throwable th2) {
                                        th = th2;
                                        z = true;
                                        stats.flush(z);
                                        throw th;
                                    }
                                } catch (Exception e2) {
                                    error = e2;
                                }
                            } catch (Exception e3) {
                                error = e3;
                                buffer = rtp;
                            }
                        }
                    } catch (Exception e4) {
                        byte[] buffer3 = rtp;
                        if (this.closed.get()) {
                            stats.flush(true);
                            return;
                        }
                        rtp = buffer3;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    z = true;
                }
            } catch (Throwable th4) {
                th = th4;
                z = true;
            }
        }
        stats.flush(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void runControl(DatagramSocket socket) {
        byte[] buffer = new byte[4096];
        while (!this.closed.get()) {
            try {
                socket.receive(new DatagramPacket(buffer, buffer.length));
            } catch (Exception e) {
                if (this.closed.get()) {
                    return;
                }
            }
        }
    }

    private final DatagramSocket bindAnyPort() throws SocketException {
        DatagramSocket socket = new DatagramSocket((SocketAddress) null);
        socket.setReuseAddress(true);
        socket.bind(new InetSocketAddress(InetAddress.getByName("::"), 0));
        return socket;
    }

    private final int readU32Be(byte[] source, int offset) {
        return ((source[offset] & UByte.MAX_VALUE) << 24) | ((source[offset + 1] & UByte.MAX_VALUE) << 16) | ((source[offset + 2] & UByte.MAX_VALUE) << 8) | (source[offset + 3] & UByte.MAX_VALUE);
    }

    private final String toHexString(byte[] $this$toHexString) {
        return ArraysKt.joinToString$default($this$toHexString, (CharSequence) "", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, new Function1() { // from class: com.carplay.airplay.AudioStream$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AudioStream.toHexString$lambda$9(((Byte) obj).byteValue());
            }
        }, 30, (Object) null);
    }

    static final CharSequence toHexString$lambda$9(byte b) {
        String str = String.format("%02x", Arrays.copyOf(new Object[]{Integer.valueOf(b & UByte.MAX_VALUE)}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    /* JADX INFO: compiled from: AudioStream.kt */
    @Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\b\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lcom/carplay/airplay/AudioStream$Companion;", "", "<init>", "()V", "TAG", "", "DATAGRAM_BYTES", "", "AUDIO_RECEIVE_BUFFER_BYTES", "RTP_HEADER_LEN", "TAG_LEN", "NONCE_LEN", "TAIL_LEN", "FIRST_PACKET_LOG_COUNT", "PACKET_LOG_INTERVAL", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
