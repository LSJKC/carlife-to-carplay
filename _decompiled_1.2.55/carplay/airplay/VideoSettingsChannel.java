package com.carplay.airplay;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.UByte;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import org.bouncycastle.crypto.InvalidCipherTextException;

/* JADX INFO: compiled from: VideoSettingsChannel.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u0000 (2\u00020\u0001:\u0001(B+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006¢\u0006\u0004\b\t\u0010\nJ\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017J\u0010\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\u000eH\u0002J\u0010\u0010\u001a\u001a\u00020\b2\u0006\u0010\u001b\u001a\u00020\u0010H\u0002J\u0018\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u0018\u0010 \u001a\u00020\b2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010!\u001a\u00020\u0003H\u0002J\b\u0010\"\u001a\u00020\bH\u0016J\u0018\u0010#\u001a\u00020\u00152\u0006\u0010$\u001a\u00020\u00032\u0006\u0010%\u001a\u00020\u0015H\u0002J \u0010&\u001a\u00020\u00072\u0006\u0010$\u001a\u00020\u00032\u0006\u0010%\u001a\u00020\u00152\u0006\u0010'\u001a\u00020\u0015H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0012X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006)"}, d2 = {"Lcom/carplay/airplay/VideoSettingsChannel;", "Ljava/io/Closeable;", "readKey", "", "writeKey", "log", "Lkotlin/Function1;", "", "", "<init>", "([B[BLkotlin/jvm/functions/Function1;)V", "closed", "Ljava/util/concurrent/atomic/AtomicBoolean;", "server", "Ljava/net/ServerSocket;", "socket", "Ljava/net/Socket;", "readCounter", "", "writeCounter", "listen", "", "bindAddress", "Ljava/net/InetAddress;", "accept", "bound", "run", "sock", "handlePackages", "buffer", "output", "Ljava/io/OutputStream;", "reply", "seq", "close", "u32", "b", "o", "ascii", "n", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class VideoSettingsChannel implements Closeable {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int HEADER = 32;

    @Deprecated
    public static final int MAX_PACKAGE = 8388608;

    @Deprecated
    public static final int TAG = 16;
    private final AtomicBoolean closed;
    private final Function1<String, Unit> log;
    private long readCounter;
    private final byte[] readKey;
    private ServerSocket server;
    private Socket socket;
    private long writeCounter;
    private final byte[] writeKey;

    /* JADX WARN: Multi-variable type inference failed */
    public VideoSettingsChannel(byte[] readKey, byte[] writeKey, Function1<? super String, Unit> log) {
        Intrinsics.checkNotNullParameter(readKey, "readKey");
        Intrinsics.checkNotNullParameter(writeKey, "writeKey");
        Intrinsics.checkNotNullParameter(log, "log");
        this.readKey = readKey;
        this.writeKey = writeKey;
        this.log = log;
        this.closed = new AtomicBoolean(false);
    }

    public final int listen(InetAddress bindAddress) throws IOException {
        Intrinsics.checkNotNullParameter(bindAddress, "bindAddress");
        final ServerSocket bound = new ServerSocket();
        bound.setReuseAddress(true);
        bound.bind(new InetSocketAddress(bindAddress, 0));
        this.server = bound;
        Thread $this$listen_u24lambda_u242 = new Thread(new Runnable() { // from class: com.carplay.airplay.VideoSettingsChannel$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.accept(bound);
            }
        }, "display-client");
        $this$listen_u24lambda_u242.setDaemon(true);
        $this$listen_u24lambda_u242.start();
        return bound.getLocalPort();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void accept(ServerSocket bound) {
        try {
            try {
                Socket accepted = bound.accept();
                this.socket = accepted;
                this.log.invoke("video settings channel connected");
                Intrinsics.checkNotNull(accepted);
                run(accepted);
            } catch (Exception error) {
                if (!this.closed.get()) {
                    this.log.invoke("video settings channel ended: " + error.getMessage());
                }
            }
        } finally {
            close();
        }
    }

    private final void run(Socket sock) throws InvalidCipherTextException, IOException {
        int read;
        InputStream input = sock.getInputStream();
        OutputStream output = sock.getOutputStream();
        byte[] buffer = new byte[16384];
        byte[] ciphertext = new byte[0];
        byte[] plaintext = new byte[0];
        while (!this.closed.get() && (read = input.read(buffer)) >= 0) {
            byte[] bArrCopyOf = Arrays.copyOf(buffer, read);
            Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
            byte[] ciphertext2 = ArraysKt.plus(ciphertext, bArrCopyOf);
            int offset = 0;
            while (ciphertext2.length - offset >= 2) {
                int length = (ciphertext2[offset] & UByte.MAX_VALUE) | ((ciphertext2[offset + 1] & UByte.MAX_VALUE) << 8);
                int frame = length + 2 + 16;
                if (ciphertext2.length - offset < frame) {
                    break;
                }
                byte[] aad = ArraysKt.copyOfRange(ciphertext2, offset, offset + 2);
                AirPlayCrypto airPlayCrypto = AirPlayCrypto.INSTANCE;
                byte[] bArr = this.readKey;
                AirPlayCrypto airPlayCrypto2 = AirPlayCrypto.INSTANCE;
                long j = this.readCounter;
                this.readCounter = j + 1;
                plaintext = ArraysKt.plus(plaintext, airPlayCrypto.chachaOpen(bArr, airPlayCrypto2.nonce64(j), ArraysKt.copyOfRange(ciphertext2, offset + 2, offset + frame), aad));
                offset += frame;
            }
            int length2 = ciphertext2.length;
            ciphertext = ArraysKt.copyOfRange(ciphertext2, offset, length2);
            Intrinsics.checkNotNull(output);
            plaintext = handlePackages(plaintext, output);
        }
    }

    private final byte[] handlePackages(byte[] buffer, OutputStream output) throws InvalidCipherTextException, IOException {
        int offset = 0;
        while (buffer.length - offset >= 32) {
            int size = u32(buffer, offset);
            if (size < 32 || size > 8388608) {
                throw new IllegalStateException("bad package size " + size);
            }
            if (buffer.length - offset < size) {
                break;
            }
            if (Intrinsics.areEqual(ascii(buffer, offset + 4, 12), "sync")) {
                reply(output, ArraysKt.copyOfRange(buffer, offset + 20, offset + 28));
            }
            offset += size;
        }
        return ArraysKt.copyOfRange(buffer, offset, buffer.length);
    }

    private final void reply(OutputStream output, byte[] seq) throws InvalidCipherTextException, IOException {
        byte[] header = new byte[32];
        header[3] = 32;
        byte[] bytes = "rply".getBytes(Charsets.US_ASCII);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        ArraysKt.copyInto$default(bytes, header, 4, 0, 0, 12, (Object) null);
        ArraysKt.copyInto$default(seq, header, 20, 0, 0, 12, (Object) null);
        byte[] aad = {(byte) (header.length & 255), (byte) (header.length >> 8)};
        AirPlayCrypto airPlayCrypto = AirPlayCrypto.INSTANCE;
        byte[] bArr = this.writeKey;
        AirPlayCrypto airPlayCrypto2 = AirPlayCrypto.INSTANCE;
        long j = this.writeCounter;
        this.writeCounter = 1 + j;
        byte[] sealed = airPlayCrypto.chachaSeal(bArr, airPlayCrypto2.nonce64(j), header, aad);
        output.write(ArraysKt.plus(aad, sealed));
        output.flush();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        Unit unit;
        if (this.closed.compareAndSet(false, true)) {
            Unit unit2 = null;
            try {
                Result.Companion companion = Result.INSTANCE;
                VideoSettingsChannel $this$close_u24lambda_u243 = this;
                Socket socket = $this$close_u24lambda_u243.socket;
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
            try {
                Result.Companion companion3 = Result.INSTANCE;
                VideoSettingsChannel $this$close_u24lambda_u244 = this;
                ServerSocket serverSocket = $this$close_u24lambda_u244.server;
                if (serverSocket != null) {
                    serverSocket.close();
                    unit2 = Unit.INSTANCE;
                }
                Result.m159constructorimpl(unit2);
            } catch (Throwable th2) {
                Result.Companion companion4 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th2));
            }
        }
    }

    private final int u32(byte[] b, int o) {
        return ((b[o] & UByte.MAX_VALUE) << 24) | ((b[o + 1] & UByte.MAX_VALUE) << 16) | ((b[o + 2] & UByte.MAX_VALUE) << 8) | (b[o + 3] & UByte.MAX_VALUE);
    }

    private final String ascii(byte[] b, int o, int n) {
        return StringsKt.trimEnd(new String(ArraysKt.copyOfRange(b, o, o + n), Charsets.US_ASCII), 0);
    }

    /* JADX INFO: compiled from: VideoSettingsChannel.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lcom/carplay/airplay/VideoSettingsChannel$Companion;", "", "<init>", "()V", "HEADER", "", "TAG", "MAX_PACKAGE", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
