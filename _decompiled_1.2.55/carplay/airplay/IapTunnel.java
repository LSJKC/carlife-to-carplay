package com.carplay.airplay;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.bouncycastle.crypto.InvalidCipherTextException;

/* JADX INFO: compiled from: IapTunnel.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u0000 .2\u00020\u0001:\u0002-.B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u000e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0016\u001a\u00020\u0017J\b\u0010\u001a\u001a\u00020\u000fH\u0002J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\u000e\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 J\u0010\u0010!\u001a\u00020\u001c2\u0006\u0010\"\u001a\u00020\u000fH\u0002J\u0010\u0010#\u001a\u00020\u001c2\u0006\u0010$\u001a\u00020\u0011H\u0002J\u001c\u0010%\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030&2\u0006\u0010'\u001a\u00020\u0003H\u0002J\u0010\u0010(\u001a\u00020\u00032\u0006\u0010'\u001a\u00020\u0003H\u0002J\u0018\u0010)\u001a\u00020\u00192\u0006\u0010*\u001a\u00020\u00032\u0006\u0010+\u001a\u00020\u0019H\u0002J\u0018\u0010,\u001a\u00020\u00192\u0006\u0010*\u001a\u00020\u00032\u0006\u0010+\u001a\u00020\u0019H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0004\u001a\n \f*\u0004\u0018\u00010\u00050\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006/"}, d2 = {"Lcom/carplay/airplay/IapTunnel;", "Ljava/io/Closeable;", "readKey", "", "bindAddress", "Ljava/net/InetAddress;", "<init>", "([BLjava/net/InetAddress;)V", "closed", "Ljava/util/concurrent/atomic/AtomicBoolean;", "readCounter", "Ljava/util/concurrent/atomic/AtomicLong;", "kotlin.jvm.PlatformType", "servers", "", "Ljava/net/ServerSocket;", "socket", "Ljava/net/Socket;", "threads", "Ljava/lang/Thread;", "peerConnected", "Ljava/util/concurrent/CountDownLatch;", "listener", "Lcom/carplay/airplay/IapTunnel$Listener;", "listen", "", "bindAny", "close", "", "awaitPeerConnection", "", "timeoutMillis", "", "accept", "bound", "run", "sock", "decryptFrames", "Lkotlin/Pair;", "buffer", "parsePackages", "readU16Le", "source", "offset", "readU32Be", "Listener", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class IapTunnel implements Closeable {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int FRAME_HEADER_LEN = 2;

    @Deprecated
    public static final int MAX_PACKAGE = 4194304;

    @Deprecated
    public static final int MESSAGE_TYPE_OFFSET = 16;

    @Deprecated
    public static final int MSG_TYPE_COMM = 1668246893;

    @Deprecated
    public static final int PACKAGE_HEADER_LEN = 32;

    @Deprecated
    public static final int READ_CHUNK_BYTES = 16384;

    @Deprecated
    public static final int TAG_SIZE = 16;
    private final InetAddress bindAddress;
    private final AtomicBoolean closed;
    private volatile Listener listener;
    private final CountDownLatch peerConnected;
    private final AtomicLong readCounter;
    private final byte[] readKey;
    private final List<ServerSocket> servers;
    private Socket socket;
    private final List<Thread> threads;

    public IapTunnel(byte[] readKey, InetAddress bindAddress) {
        Intrinsics.checkNotNullParameter(readKey, "readKey");
        Intrinsics.checkNotNullParameter(bindAddress, "bindAddress");
        this.readKey = readKey;
        this.closed = new AtomicBoolean(false);
        this.readCounter = new AtomicLong(0L);
        this.bindAddress = bindAddress instanceof Inet4Address ? InetAddress.getByName("0.0.0.0") : bindAddress;
        this.servers = new ArrayList();
        this.threads = new ArrayList();
        this.peerConnected = new CountDownLatch(1);
        this.listener = new Listener() { // from class: com.carplay.airplay.IapTunnel$listener$1
            @Override // com.carplay.airplay.IapTunnel.Listener
            public void onClosed(Throwable cause) {
                super.onClosed(cause);
            }

            @Override // com.carplay.airplay.IapTunnel.Listener
            public void onDebug(String message) {
                super.onDebug(message);
            }

            @Override // com.carplay.airplay.IapTunnel.Listener
            public void onIap(byte[] bytes) {
                super.onIap(bytes);
            }

            @Override // com.carplay.airplay.IapTunnel.Listener
            public void onOpen(String remoteAddress) {
                super.onOpen(remoteAddress);
            }
        };
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ IapTunnel(byte[] bArr, InetAddress inetAddress, int i, DefaultConstructorMarker defaultConstructorMarker) throws UnknownHostException {
        if ((i & 2) != 0) {
            inetAddress = InetAddress.getByName("0.0.0.0");
            Intrinsics.checkNotNullExpressionValue(inetAddress, "getByName(...)");
        }
        this(bArr, inetAddress);
    }

    /* JADX INFO: compiled from: IapTunnel.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0000\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u0005H\u0016J\u0012\u0010\u000b\u001a\u00020\u00032\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0016¨\u0006\u000eÀ\u0006\u0003"}, d2 = {"Lcom/carplay/airplay/IapTunnel$Listener;", "", "onOpen", "", "remoteAddress", "", "onIap", "bytes", "", "onDebug", "message", "onClosed", "cause", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public interface Listener {

        /* JADX INFO: compiled from: IapTunnel.kt */
        @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
        public static final class DefaultImpls {
            @Deprecated
            public static void onOpen(Listener $this, String remoteAddress) {
                Listener.super.onOpen(remoteAddress);
            }

            @Deprecated
            public static void onIap(Listener $this, byte[] bytes) {
                Intrinsics.checkNotNullParameter(bytes, "bytes");
                Listener.super.onIap(bytes);
            }

            @Deprecated
            public static void onDebug(Listener $this, String message) {
                Intrinsics.checkNotNullParameter(message, "message");
                Listener.super.onDebug(message);
            }

            @Deprecated
            public static void onClosed(Listener $this, Throwable cause) {
                Listener.super.onClosed(cause);
            }
        }

        default void onOpen(String remoteAddress) {
        }

        default void onIap(byte[] bytes) {
            Intrinsics.checkNotNullParameter(bytes, "bytes");
        }

        default void onDebug(String message) {
            Intrinsics.checkNotNullParameter(message, "message");
        }

        default void onClosed(Throwable cause) {
        }
    }

    public final int listen(Listener listener) throws IOException {
        InetAddress secondaryAddress;
        Object objM159constructorimpl;
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.listener = listener;
        ServerSocket bound = bindAny();
        this.servers.add(bound);
        listener.onDebug("AirPlay iAP tunnel listener bound=" + bound.getLocalSocketAddress());
        if (this.bindAddress instanceof Inet6Address) {
            secondaryAddress = InetAddress.getByName("0.0.0.0");
        } else {
            secondaryAddress = InetAddress.getByName("::");
        }
        ServerSocket secondary = new ServerSocket();
        try {
            Result.Companion companion = Result.INSTANCE;
            IapTunnel iapTunnel = this;
            secondary.setReuseAddress(true);
            secondary.bind(new InetSocketAddress(secondaryAddress, bound.getLocalPort()));
            objM159constructorimpl = Result.m159constructorimpl(secondary);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m166isSuccessimpl(objM159constructorimpl)) {
            ServerSocket secondary2 = (ServerSocket) objM159constructorimpl;
            this.servers.add(secondary2);
            listener.onDebug("AirPlay iAP tunnel secondary listener bound=" + secondary2.getLocalSocketAddress());
        }
        Throwable error = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (error != null) {
            AirPlaySessionKt.safeClose(secondary);
            listener.onDebug("AirPlay iAP tunnel secondary listener failed address=" + secondaryAddress + " port=" + bound.getLocalPort() + ": " + error.getMessage());
        }
        Iterable $this$forEach$iv = this.servers;
        for (Object element$iv : $this$forEach$iv) {
            final ServerSocket server = (ServerSocket) element$iv;
            List<Thread> list = this.threads;
            Thread $this$listen_u24lambda_u246_u24lambda_u245 = new Thread(new Runnable() { // from class: com.carplay.airplay.IapTunnel$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() throws SocketException {
                    this.f$0.accept(server);
                }
            }, "airplay-iap-tunnel");
            $this$listen_u24lambda_u246_u24lambda_u245.setDaemon(true);
            $this$listen_u24lambda_u246_u24lambda_u245.start();
            list.add($this$listen_u24lambda_u246_u24lambda_u245);
        }
        return bound.getLocalPort();
    }

    private final ServerSocket bindAny() throws IOException {
        ServerSocket $this$bindAny_u24lambda_u247 = new ServerSocket();
        $this$bindAny_u24lambda_u247.setReuseAddress(true);
        $this$bindAny_u24lambda_u247.bind(new InetSocketAddress(this.bindAddress, 0));
        return $this$bindAny_u24lambda_u247;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.closed.compareAndSet(false, true)) {
            AirPlaySessionKt.safeClose(this.socket);
            Iterable $this$forEach$iv = CollectionsKt.toList(this.servers);
            for (Object element$iv : $this$forEach$iv) {
                Closeable p0 = (Closeable) element$iv;
                AirPlaySessionKt.safeClose(p0);
            }
            this.servers.clear();
            Iterable $this$forEach$iv2 = CollectionsKt.toList(this.threads);
            for (Object element$iv2 : $this$forEach$iv2) {
                Thread p1 = (Thread) element$iv2;
                p1.interrupt();
            }
            this.threads.clear();
            this.peerConnected.countDown();
        }
    }

    public final boolean awaitPeerConnection(long timeoutMillis) {
        if (!(timeoutMillis >= 0)) {
            throw new IllegalArgumentException("timeoutMillis must not be negative".toString());
        }
        try {
            return this.peerConnected.await(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void accept(ServerSocket bound) throws SocketException {
        this.listener.onDebug("AirPlay iAP tunnel accepting local=" + bound.getLocalSocketAddress());
        while (!this.closed.get()) {
            try {
                Socket accepted = bound.accept();
                if (this.closed.get()) {
                    AirPlaySessionKt.safeClose(accepted);
                    return;
                }
                accepted.setSoLinger(true, 0);
                this.socket = accepted;
                this.readCounter.set(0L);
                this.peerConnected.countDown();
                Listener listener = this.listener;
                SocketAddress remoteSocketAddress = accepted.getRemoteSocketAddress();
                listener.onOpen(remoteSocketAddress != null ? remoteSocketAddress.toString() : null);
                Intrinsics.checkNotNull(accepted);
                run(accepted);
            } catch (Exception error) {
                if (!this.closed.get()) {
                    this.listener.onClosed(error);
                    return;
                }
                return;
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:31:0x0093  */
    private final void run(Socket sock) {
        byte[] ciphertext = new byte[0];
        byte[] plaintext = new byte[0];
        boolean announcedData = false;
        try {
            try {
                InputStream input = sock.getInputStream();
                byte[] buffer = new byte[16384];
                while (!this.closed.get()) {
                    int read = input.read(buffer);
                    if (read < 0) {
                        this.listener.onDebug("AirPlay iAP tunnel peer EOF");
                        break;
                    }
                    if (!announcedData) {
                        announcedData = true;
                        this.listener.onDebug("AirPlay iAP tunnel received data");
                    }
                    byte[] bArrCopyOf = Arrays.copyOf(buffer, read);
                    Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
                    Pair<byte[], byte[]> pairDecryptFrames = decryptFrames(ArraysKt.plus(ciphertext, bArrCopyOf));
                    byte[] plaintext2 = ArraysKt.plus(plaintext, pairDecryptFrames.getFirst());
                    ciphertext = pairDecryptFrames.getSecond();
                    plaintext = parsePackages(plaintext2);
                    if (this.socket == sock) {
                        this.socket = null;
                    }
                    AirPlaySessionKt.safeClose(sock);
                    this.closed.get();
                    throw th;
                }
                if (this.socket == sock) {
                    this.socket = null;
                }
                AirPlaySessionKt.safeClose(sock);
                this.closed.get();
            } catch (Exception error) {
                Throwable failure = error;
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
            if (this.socket == sock) {
                this.socket = null;
            }
            AirPlaySessionKt.safeClose(sock);
            this.closed.get();
            throw th;
        }
    }

    private final Pair<byte[], byte[]> decryptFrames(byte[] buffer) throws InvalidCipherTextException {
        ArrayList output = new ArrayList();
        int offset = 0;
        while (buffer.length - offset >= 2) {
            int length = readU16Le(buffer, offset);
            int frameLength = length + 2 + 16;
            if (buffer.length - offset < frameLength) {
                break;
            }
            byte[] aad = ArraysKt.copyOfRange(buffer, offset, offset + 2);
            byte[] sealed = ArraysKt.copyOfRange(buffer, offset + 2, offset + frameLength);
            byte[] plain = AirPlayCrypto.INSTANCE.chachaOpen(this.readKey, AirPlayCrypto.INSTANCE.nonce64(this.readCounter.get()), sealed, aad);
            this.readCounter.incrementAndGet();
            output.add(plain);
            offset += frameLength;
        }
        ArrayList $this$toTypedArray$iv = output;
        byte[][] bArr = (byte[][]) $this$toTypedArray$iv.toArray(new byte[0][]);
        return TuplesKt.to(BytesKt.concatBytes((byte[][]) Arrays.copyOf(bArr, bArr.length)), ArraysKt.copyOfRange(buffer, offset, buffer.length));
    }

    private final byte[] parsePackages(byte[] buffer) {
        int size;
        int offset = 0;
        while (buffer.length - offset >= 32 && (size = readU32Be(buffer, offset)) >= 32 && size <= 4194304 && buffer.length - offset >= size) {
            int messageType = readU32Be(buffer, offset + 16);
            if (messageType == 1668246893) {
                this.listener.onDebug("AirPlay iAP tunnel package type=comm body=" + (size - 32));
                this.listener.onIap(ArraysKt.copyOfRange(buffer, offset + 32, offset + size));
            }
            offset += size;
        }
        return ArraysKt.copyOfRange(buffer, offset, buffer.length);
    }

    private final int readU16Le(byte[] source, int offset) {
        return (source[offset] & UByte.MAX_VALUE) | ((source[offset + 1] & UByte.MAX_VALUE) << 8);
    }

    private final int readU32Be(byte[] source, int offset) {
        return ((source[offset] & UByte.MAX_VALUE) << 24) | ((source[offset + 1] & UByte.MAX_VALUE) << 16) | ((source[offset + 2] & UByte.MAX_VALUE) << 8) | (source[offset + 3] & UByte.MAX_VALUE);
    }

    /* JADX INFO: compiled from: IapTunnel.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lcom/carplay/airplay/IapTunnel$Companion;", "", "<init>", "()V", "FRAME_HEADER_LEN", "", "TAG_SIZE", "PACKAGE_HEADER_LEN", "MESSAGE_TYPE_OFFSET", "MSG_TYPE_COMM", "MAX_PACKAGE", "READ_CHUNK_BYTES", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
