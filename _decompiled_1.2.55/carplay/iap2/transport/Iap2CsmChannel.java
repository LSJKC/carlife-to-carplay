package com.carplay.iap2.transport;

import com.carplay.iap2.wire.Iap2CsmFramer;
import com.carplay.iap2.wire.Iap2Frame;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;
import kotlin.ExceptionsKt;
import kotlin.KotlinNothingValueException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2CsmChannel.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0007\n\u0002\u0010\u0001\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 52\u00060\u0001j\u0002`\u0002:\u00015B\u0011\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0004\b\u0005\u0010\u0006J\u0006\u0010\u0018\u001a\u00020\u0019J\u000e\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u001cJ\u0018\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u000f2\b\b\u0002\u0010\u001b\u001a\u00020\u001cJ\u0018\u0010 \u001a\u00020\u00132\u0006\u0010!\u001a\u00020\"2\b\b\u0002\u0010\u001b\u001a\u00020\u001cJ\u000e\u0010#\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u001cJ\u0010\u0010$\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u001b\u001a\u00020\u001cJ\b\u0010%\u001a\u00020\u001eH\u0016J\u0010\u0010&\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u000fH\u0002J\n\u0010'\u001a\u0004\u0018\u00010\u000fH\u0002J\b\u0010(\u001a\u00020\u001eH\u0002J\u0010\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020\u0015H\u0002J\u0014\u0010,\u001a\u00020*2\n\u0010-\u001a\u00060.j\u0002`/H\u0002J\u0010\u00100\u001a\u00020\u00152\u0006\u0010+\u001a\u00020\u0015H\u0002J\u0010\u00101\u001a\u00020\u001c2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0010\u00102\u001a\u00020\u001c2\u0006\u00103\u001a\u00020\u001cH\u0002J\u0010\u00104\u001a\u00020\u001e2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0016\u001a\u00020\u00138F¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017¨\u00066"}, d2 = {"Lcom/carplay/iap2/transport/Iap2CsmChannel;", "Ljava/lang/AutoCloseable;", "Lkotlin/AutoCloseable;", "link", "Lcom/carplay/iap2/transport/Iap2LinkChannel;", "<init>", "(Lcom/carplay/iap2/transport/Iap2LinkChannel;)V", "stateLock", "Ljava/lang/Object;", "sendLock", "receiveLock", "framer", "Lcom/carplay/iap2/wire/Iap2CsmFramer;", "receivedFrames", "Ljava/util/ArrayDeque;", "Lcom/carplay/iap2/wire/Iap2Frame;", "receivedBytes", "", "closed", "", "terminalFailure", "", "isClosed", "()Z", "handshakeStatus", "Lcom/carplay/iap2/transport/Iap2HandshakeStatus;", "awaitReady", "timeoutMillis", "", "send", "", "frame", "sendFileTransfer", "bytes", "", "awaitOutboundAcknowledged", "recv", "close", "enqueue", "takeReceived", "checkOpen", "failClosed", "", "failure", "fatal", "error", "Ljava/lang/Error;", "Lkotlin/Error;", "terminalize", "deadlineAfter", "remainingMillis", "deadlineNanos", "requireTimeout", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2CsmChannel implements AutoCloseable {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final long DEFAULT_SEND_TIMEOUT_MILLIS = 5000;
    private static final int MAX_PENDING_FRAMES = 64;
    private static final int MAX_PENDING_FRAME_BYTES = 1048576;
    private static final long MAX_TIMEOUT_MILLIS = 300000;
    private static final long NANOS_PER_MILLISECOND = 1000000;
    private boolean closed;
    private final Iap2CsmFramer framer;
    private final Iap2LinkChannel link;
    private final Object receiveLock;
    private int receivedBytes;
    private final ArrayDeque<Iap2Frame> receivedFrames;
    private final Object sendLock;
    private final Object stateLock;
    private Throwable terminalFailure;

    public /* synthetic */ Iap2CsmChannel(Iap2LinkChannel iap2LinkChannel, DefaultConstructorMarker defaultConstructorMarker) {
        this(iap2LinkChannel);
    }

    private Iap2CsmChannel(Iap2LinkChannel link) {
        this.link = link;
        this.stateLock = new Object();
        this.sendLock = new Object();
        this.receiveLock = new Object();
        this.framer = new Iap2CsmFramer();
        this.receivedFrames = new ArrayDeque<>();
    }

    public final boolean isClosed() {
        boolean z;
        synchronized (this.stateLock) {
            z = this.closed || this.terminalFailure != null;
        }
        return z;
    }

    public final Iap2HandshakeStatus handshakeStatus() {
        return this.link.handshakeStatus();
    }

    public final boolean awaitReady(long timeoutMillis) {
        requireTimeout(timeoutMillis);
        try {
            checkOpen();
            try {
                boolean ready = this.link.awaitReady(timeoutMillis);
                if (ready && this.link.peerMaxControlPayloadBytes() == null) {
                    throw new IOException("iAP2 link became writable without a peer control payload limit");
                }
                return ready;
            } catch (Throwable failure) {
                if (failure instanceof Error) {
                    fatal((Error) failure);
                    throw new KotlinNothingValueException();
                }
                failClosed(failure);
                throw new KotlinNothingValueException();
            }
        } catch (Error failure2) {
            fatal(failure2);
            throw new KotlinNothingValueException();
        }
    }

    public static /* synthetic */ void send$default(Iap2CsmChannel iap2CsmChannel, Iap2Frame iap2Frame, long j, int i, Object obj) {
        if ((i & 2) != 0) {
            j = 5000;
        }
        iap2CsmChannel.send(iap2Frame, j);
    }

    public final void send(Iap2Frame frame, long timeoutMillis) {
        Intrinsics.checkNotNullParameter(frame, "frame");
        requireTimeout(timeoutMillis);
        synchronized (this.sendLock) {
            try {
                try {
                    checkOpen();
                    Integer numPeerMaxControlPayloadBytes = this.link.peerMaxControlPayloadBytes();
                    if (numPeerMaxControlPayloadBytes == null) {
                        throw new IllegalStateException("CSM send requires a successful iAP2 awaitReady");
                    }
                    int peerLimit = numPeerMaxControlPayloadBytes.intValue();
                    List<byte[]> listSplitForLink = Iap2CsmFramer.INSTANCE.splitForLink(frame, peerLimit);
                    long deadlineNanos = deadlineAfter(timeoutMillis);
                    for (byte[] chunk : listSplitForLink) {
                        try {
                            boolean queued = this.link.sendControlAwaitCapacity$core_iap2(chunk, remainingMillis(deadlineNanos));
                            if (!queued) {
                                failClosed(new IOException("iAP2 could not queue the complete CSM frame before its timeout"));
                                throw new KotlinNothingValueException();
                            }
                        } catch (Throwable failure) {
                            if (failure instanceof Error) {
                                fatal((Error) failure);
                                throw new KotlinNothingValueException();
                            }
                            failClosed(failure);
                            throw new KotlinNothingValueException();
                        }
                    }
                    Unit unit = Unit.INSTANCE;
                } catch (Error failure2) {
                    fatal(failure2);
                    throw new KotlinNothingValueException();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static /* synthetic */ boolean sendFileTransfer$default(Iap2CsmChannel iap2CsmChannel, byte[] bArr, long j, int i, Object obj) {
        if ((i & 2) != 0) {
            j = 5000;
        }
        return iap2CsmChannel.sendFileTransfer(bArr, j);
    }

    public final boolean sendFileTransfer(byte[] bytes, long timeoutMillis) {
        boolean zSendSessionAwaitCapacity;
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        requireTimeout(timeoutMillis);
        synchronized (this.sendLock) {
            checkOpen();
            zSendSessionAwaitCapacity = this.link.sendSessionAwaitCapacity(12, bytes, timeoutMillis);
        }
        return zSendSessionAwaitCapacity;
    }

    public final boolean awaitOutboundAcknowledged(long timeoutMillis) {
        requireTimeout(timeoutMillis);
        try {
            return this.link.awaitOutboundAcknowledged(timeoutMillis);
        } catch (Throwable failure) {
            if (failure instanceof Error) {
                fatal((Error) failure);
                throw new KotlinNothingValueException();
            }
            failClosed(failure);
            throw new KotlinNothingValueException();
        }
    }

    public final Iap2Frame recv(long timeoutMillis) {
        requireTimeout(timeoutMillis);
        synchronized (this.receiveLock) {
            Iap2Frame iap2FrameTakeReceived = takeReceived();
            if (iap2FrameTakeReceived != null) {
                return iap2FrameTakeReceived;
            }
            long deadlineNanos = deadlineAfter(timeoutMillis);
            do {
                try {
                    checkOpen();
                    try {
                        byte[] control = this.link.recvControl(remainingMillis(deadlineNanos));
                        if (control == null) {
                            return null;
                        }
                        try {
                            for (Iap2Frame frame : this.framer.offer(control)) {
                                enqueue(frame);
                            }
                            Iap2Frame iap2FrameTakeReceived2 = takeReceived();
                            if (iap2FrameTakeReceived2 != null) {
                                return iap2FrameTakeReceived2;
                            }
                        } catch (Throwable failure) {
                            if (failure instanceof Error) {
                                fatal((Error) failure);
                                throw new KotlinNothingValueException();
                            }
                            failClosed(new IOException("Invalid CSM control payload", failure));
                            throw new KotlinNothingValueException();
                        }
                    } catch (Throwable failure2) {
                        if (failure2 instanceof Error) {
                            fatal((Error) failure2);
                            throw new KotlinNothingValueException();
                        }
                        failClosed(failure2);
                        throw new KotlinNothingValueException();
                    }
                } catch (Error failure3) {
                    fatal(failure3);
                    throw new KotlinNothingValueException();
                }
            } while (remainingMillis(deadlineNanos) != 0);
            return null;
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() throws Throwable {
        boolean close;
        synchronized (this.stateLock) {
            if (this.closed) {
                close = false;
            } else {
                close = true;
                this.closed = true;
            }
        }
        if (close) {
            try {
                this.link.close();
            } catch (Throwable failure) {
                if (failure instanceof Error) {
                    terminalize(failure);
                    throw failure;
                }
                throw terminalize(failure);
            }
        }
    }

    private final void enqueue(Iap2Frame frame) throws Throwable {
        int encodedBytes = frame.encodedFrame().length;
        if (this.receivedFrames.size() >= 64 || encodedBytes > 1048576 - this.receivedBytes) {
            failClosed(new IOException("CSM received-frame queue limit exceeded"));
            throw new KotlinNothingValueException();
        }
        this.receivedFrames.add(frame);
        this.receivedBytes += encodedBytes;
    }

    private final Iap2Frame takeReceived() {
        Iap2Frame frame = this.receivedFrames.pollFirst();
        if (frame == null) {
            return null;
        }
        this.receivedBytes -= frame.encodedFrame().length;
        return frame;
    }

    private final void checkOpen() {
        synchronized (this.stateLock) {
            Throwable it = this.terminalFailure;
            if (it != null) {
                throw it;
            }
            if (this.closed) {
                throw new IOException("CSM channel is closed");
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    private final Void failClosed(Throwable failure) throws Throwable {
        Throwable terminal = terminalize(failure);
        try {
            close();
            throw terminal;
        } catch (Throwable closeFailure) {
            if (closeFailure instanceof Error) {
                throw closeFailure;
            }
            if (closeFailure != terminal) {
                ExceptionsKt.addSuppressed(terminal, closeFailure);
                throw terminal;
            }
            throw terminal;
        }
    }

    private final Void fatal(Error error) {
        terminalize(error);
        try {
            close();
            throw error;
        } catch (Throwable th) {
            throw error;
        }
    }

    private final Throwable terminalize(Throwable failure) {
        synchronized (this.stateLock) {
            Throwable prior = this.terminalFailure;
            if (prior == null) {
                this.terminalFailure = failure;
                return failure;
            }
            if (prior != failure) {
                ExceptionsKt.addSuppressed(prior, failure);
            }
            return prior;
        }
    }

    private final long deadlineAfter(long timeoutMillis) {
        return System.nanoTime() + (1000000 * timeoutMillis);
    }

    private final long remainingMillis(long deadlineNanos) {
        long remaining = deadlineNanos - System.nanoTime();
        if (remaining <= 0) {
            return 0L;
        }
        return Math.min(300000L, ((remaining + 1000000) - 1) / 1000000);
    }

    private final void requireTimeout(long timeoutMillis) {
        boolean z = false;
        if (0 <= timeoutMillis && timeoutMillis < 300001) {
            z = true;
        }
        if (!z) {
            throw new IllegalArgumentException("timeoutMillis must be in 0..300000".toString());
        }
    }

    /* JADX INFO: compiled from: Iap2CsmChannel.kt */
    @Metadata(d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J,\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000bJ>\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\r0\u000bJ\u000e\u0010\u0010\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007J$\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000bJ:\u0010\u0012\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b2\u0014\b\u0002\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\r0\u000bJ$\u0010\u001a\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0014\b\u0002\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\r0\u000bJ$\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0014\b\u0002\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\r0\u000bJ$\u0010\u001e\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0014\b\u0002\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\r0\u000bR\u000e\u0010\u0013\u001a\u00020\u0014X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0014X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0017X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0017X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lcom/carplay/iap2/transport/Iap2CsmChannel$Companion;", "", "<init>", "()V", "openPhoneBluetooth", "Lcom/carplay/iap2/transport/Iap2CsmChannel;", "underlying", "Lcom/carplay/iap2/transport/BlockingDuplexByteStream;", "profile", "Lcom/carplay/iap2/transport/PhoneBluetoothProfile;", "onDiagnostic", "Lkotlin/Function1;", "", "", "onFileTransferDatagram", "", "openController", "openControllerTunnel", "openCommandTunnel", "MAX_PENDING_FRAMES", "", "MAX_PENDING_FRAME_BYTES", "NANOS_PER_MILLISECOND", "", "MAX_TIMEOUT_MILLIS", "DEFAULT_SEND_TIMEOUT_MILLIS", "open", "onArtwork", "Lcom/carplay/iap2/transport/Iap2ArtworkTransfer;", "openWireless", "openTunnel", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2CsmChannel openPhoneBluetooth$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, PhoneBluetoothProfile phoneBluetoothProfile, Function1 function1, int i, Object obj) {
            if ((i & 4) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.transport.Iap2CsmChannel$Companion$$ExternalSyntheticLambda7
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2CsmChannel.Companion.openPhoneBluetooth$lambda$0((String) obj2);
                    }
                };
            }
            return companion.openPhoneBluetooth(blockingDuplexByteStream, phoneBluetoothProfile, function1);
        }

        static final Unit openPhoneBluetooth$lambda$0(String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2CsmChannel openPhoneBluetooth(BlockingDuplexByteStream underlying, PhoneBluetoothProfile profile, Function1<? super String, Unit> onDiagnostic) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(profile, "profile");
            Intrinsics.checkNotNullParameter(onDiagnostic, "onDiagnostic");
            return openPhoneBluetooth(underlying, profile, onDiagnostic, new Function1() { // from class: com.carplay.iap2.transport.Iap2CsmChannel$Companion$$ExternalSyntheticLambda4
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2CsmChannel.Companion.openPhoneBluetooth$lambda$1((byte[]) obj);
                }
            });
        }

        static final Unit openPhoneBluetooth$lambda$1(byte[] it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2CsmChannel openPhoneBluetooth(BlockingDuplexByteStream underlying, PhoneBluetoothProfile profile, Function1<? super String, Unit> onDiagnostic, Function1<? super byte[], Unit> onFileTransferDatagram) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(profile, "profile");
            Intrinsics.checkNotNullParameter(onDiagnostic, "onDiagnostic");
            Intrinsics.checkNotNullParameter(onFileTransferDatagram, "onFileTransferDatagram");
            return new Iap2CsmChannel(Iap2LinkChannel.INSTANCE.openPhoneBluetooth(underlying, profile, onDiagnostic, onFileTransferDatagram), null);
        }

        public final Iap2CsmChannel openController(BlockingDuplexByteStream underlying) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            return new Iap2CsmChannel(Iap2LinkChannel.INSTANCE.openController(underlying), null);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2CsmChannel openControllerTunnel$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, Function1 function1, int i, Object obj) {
            if ((i & 2) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.transport.Iap2CsmChannel$Companion$$ExternalSyntheticLambda6
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2CsmChannel.Companion.openControllerTunnel$lambda$2((String) obj2);
                    }
                };
            }
            return companion.openControllerTunnel(blockingDuplexByteStream, function1);
        }

        static final Unit openControllerTunnel$lambda$2(String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2CsmChannel openControllerTunnel(BlockingDuplexByteStream underlying, Function1<? super String, Unit> onDiagnostic) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(onDiagnostic, "onDiagnostic");
            return new Iap2CsmChannel(Iap2LinkChannel.INSTANCE.openControllerTunnel(underlying, onDiagnostic), null);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2CsmChannel openCommandTunnel$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, Function1 function1, Function1 function2, int i, Object obj) {
            if ((i & 2) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.transport.Iap2CsmChannel$Companion$$ExternalSyntheticLambda2
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2CsmChannel.Companion.openCommandTunnel$lambda$3((String) obj2);
                    }
                };
            }
            if ((i & 4) != 0) {
                function2 = new Function1() { // from class: com.carplay.iap2.transport.Iap2CsmChannel$Companion$$ExternalSyntheticLambda3
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2CsmChannel.Companion.openCommandTunnel$lambda$4((byte[]) obj2);
                    }
                };
            }
            return companion.openCommandTunnel(blockingDuplexByteStream, function1, function2);
        }

        static final Unit openCommandTunnel$lambda$3(String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        static final Unit openCommandTunnel$lambda$4(byte[] it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2CsmChannel openCommandTunnel(BlockingDuplexByteStream underlying, Function1<? super String, Unit> onDiagnostic, Function1<? super byte[], Unit> onFileTransferDatagram) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(onDiagnostic, "onDiagnostic");
            Intrinsics.checkNotNullParameter(onFileTransferDatagram, "onFileTransferDatagram");
            return new Iap2CsmChannel(Iap2LinkChannel.INSTANCE.openCommandTunnel(underlying, onDiagnostic, onFileTransferDatagram), null);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2CsmChannel open$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, Function1 function1, int i, Object obj) {
            if ((i & 2) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.transport.Iap2CsmChannel$Companion$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2CsmChannel.Companion.open$lambda$5((Iap2ArtworkTransfer) obj2);
                    }
                };
            }
            return companion.open(blockingDuplexByteStream, function1);
        }

        static final Unit open$lambda$5(Iap2ArtworkTransfer it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2CsmChannel open(BlockingDuplexByteStream underlying, Function1<? super Iap2ArtworkTransfer, Unit> onArtwork) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(onArtwork, "onArtwork");
            return new Iap2CsmChannel(Iap2LinkChannel.Companion.open$default(Iap2LinkChannel.INSTANCE, underlying, onArtwork, null, 4, null), null);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2CsmChannel openWireless$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, Function1 function1, int i, Object obj) {
            if ((i & 2) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.transport.Iap2CsmChannel$Companion$$ExternalSyntheticLambda5
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2CsmChannel.Companion.openWireless$lambda$6((Iap2ArtworkTransfer) obj2);
                    }
                };
            }
            return companion.openWireless(blockingDuplexByteStream, function1);
        }

        static final Unit openWireless$lambda$6(Iap2ArtworkTransfer it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2CsmChannel openWireless(BlockingDuplexByteStream underlying, Function1<? super Iap2ArtworkTransfer, Unit> onArtwork) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(onArtwork, "onArtwork");
            return new Iap2CsmChannel(Iap2LinkChannel.Companion.openWireless$default(Iap2LinkChannel.INSTANCE, underlying, onArtwork, null, 4, null), null);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2CsmChannel openTunnel$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, Function1 function1, int i, Object obj) {
            if ((i & 2) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.transport.Iap2CsmChannel$Companion$$ExternalSyntheticLambda1
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2CsmChannel.Companion.openTunnel$lambda$7((Iap2ArtworkTransfer) obj2);
                    }
                };
            }
            return companion.openTunnel(blockingDuplexByteStream, function1);
        }

        static final Unit openTunnel$lambda$7(Iap2ArtworkTransfer it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2CsmChannel openTunnel(BlockingDuplexByteStream underlying, Function1<? super Iap2ArtworkTransfer, Unit> onArtwork) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(onArtwork, "onArtwork");
            return new Iap2CsmChannel(Iap2LinkChannel.INSTANCE.openTunnel(underlying, onArtwork), null);
        }
    }
}
