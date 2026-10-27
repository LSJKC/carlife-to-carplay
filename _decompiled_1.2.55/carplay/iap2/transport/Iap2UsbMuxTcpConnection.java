package com.carplay.iap2.transport;

import java.util.ArrayDeque;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2UsbMuxHost.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0013\u0018\u0000 12\u00020\u0001:\u00011B!\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005¢\u0006\u0004\b\u0007\u0010\bJ\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0011H\u0016J\u001a\u0010\u001c\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\b\u0010 \u001a\u00020\u001aH\u0016J\r\u0010!\u001a\u00020\u001aH\u0000¢\u0006\u0002\b\"J\u0015\u0010#\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u001fH\u0000¢\u0006\u0002\b$J\r\u0010%\u001a\u00020\u001aH\u0000¢\u0006\u0002\b&J\u0019\u0010'\u001a\u00020\u001a2\n\b\u0002\u0010(\u001a\u0004\u0018\u00010\u0018H\u0000¢\u0006\u0002\b)J%\u0010*\u001a\u00020\u001a2\u0006\u0010+\u001a\u00020\u00052\u0006\u0010,\u001a\u00020\u00052\u0006\u0010-\u001a\u00020\u0011H\u0000¢\u0006\u0002\b.J\u0010\u0010/\u001a\u00020\u001a2\u0006\u0010+\u001a\u00020\u0005H\u0002J\b\u00100\u001a\u00020\u001aH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\u00020\u0005X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0082\u000e¢\u0006\u0002\n\u0000¨\u00062"}, d2 = {"Lcom/carplay/iap2/transport/Iap2UsbMuxTcpConnection;", "Lcom/carplay/iap2/transport/BlockingDuplexByteStream;", "host", "Lcom/carplay/iap2/transport/Iap2UsbMuxHost;", "sourcePort", "", "destinationPort", "<init>", "(Lcom/carplay/iap2/transport/Iap2UsbMuxHost;II)V", "getSourcePort$core_iap2", "()I", "stateLock", "Ljava/lang/Object;", "writeLock", "", "received", "Ljava/util/ArrayDeque;", "", "nextSequence", "nextAcknowledgement", "connected", "", "closed", "failure", "Lcom/carplay/iap2/transport/IphoneUsbException;", "send", "", "data", "recv", "maxBytes", "timeoutMillis", "", "close", "beginConnect", "beginConnect$core_iap2", "awaitConnected", "awaitConnected$core_iap2", "abort", "abort$core_iap2", "closeFromHost", "error", "closeFromHost$core_iap2", "onPacket", "flags", "sequence", "payload", "onPacket$core_iap2", "sendControl", "checkConnectedLocked", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2UsbMuxTcpConnection implements BlockingDuplexByteStream {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int MAX_SEND_PAYLOAD_BYTES = 16384;

    @Deprecated
    public static final long NANOS_PER_MILLISECOND = 1000000;

    @Deprecated
    public static final int TCP_ACK = 16;

    @Deprecated
    public static final int TCP_FIN = 1;

    @Deprecated
    public static final int TCP_RST = 4;

    @Deprecated
    public static final int TCP_SYN = 2;
    private boolean closed;
    private boolean connected;
    private final int destinationPort;
    private IphoneUsbException failure;
    private final Iap2UsbMuxHost host;
    private int nextAcknowledgement;
    private int nextSequence;
    private final ArrayDeque<byte[]> received;
    private final int sourcePort;
    private final Object stateLock;
    private final Object writeLock;

    public Iap2UsbMuxTcpConnection(Iap2UsbMuxHost host, int sourcePort, int destinationPort) {
        Intrinsics.checkNotNullParameter(host, "host");
        this.host = host;
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.stateLock = new Object();
        this.writeLock = new Object();
        this.received = new ArrayDeque<>();
    }

    /* JADX INFO: renamed from: getSourcePort$core_iap2, reason: from getter */
    public final int getSourcePort() {
        return this.sourcePort;
    }

    @Override // com.carplay.iap2.transport.BlockingDuplexByteStream
    public void send(byte[] data) {
        Pair sequenceAndAck;
        Intrinsics.checkNotNullParameter(data, "data");
        synchronized (this.stateLock) {
            checkConnectedLocked();
            Unit unit = Unit.INSTANCE;
        }
        int offset = 0;
        while (offset < data.length) {
            int count = Math.min(16384, data.length - offset);
            byte[] chunk = ArraysKt.copyOfRange(data, offset, offset + count);
            synchronized (this.writeLock) {
                synchronized (this.stateLock) {
                    checkConnectedLocked();
                    sequenceAndAck = TuplesKt.to(Integer.valueOf(this.nextSequence), Integer.valueOf(this.nextAcknowledgement));
                }
                this.host.sendTcp$core_iap2(this.sourcePort, this.destinationPort, ((Number) sequenceAndAck.getFirst()).intValue(), ((Number) sequenceAndAck.getSecond()).intValue(), 16, chunk);
                synchronized (this.stateLock) {
                    this.nextSequence += count;
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit3 = Unit.INSTANCE;
            }
            offset += count;
        }
    }

    @Override // com.carplay.iap2.transport.BlockingDuplexByteStream
    public byte[] recv(int maxBytes, long timeoutMillis) {
        if (!(maxBytes > 0)) {
            throw new IllegalArgumentException("maxBytes must be positive".toString());
        }
        long j = 0;
        if (!(timeoutMillis > 0)) {
            throw new IllegalArgumentException("timeoutMillis must be positive".toString());
        }
        long j2 = 1000000;
        long deadline = System.nanoTime() + (timeoutMillis * 1000000);
        synchronized (this.stateLock) {
            while (this.received.isEmpty() && !this.closed) {
                long remainingNanos = deadline - System.nanoTime();
                if (remainingNanos <= j) {
                    return null;
                }
                try {
                    long j3 = j2;
                    this.stateLock.wait(remainingNanos / j2, (int) (remainingNanos % j3));
                    j2 = j3;
                    j = 0;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IphoneUsbException.DeviceUnavailable("Interrupted while waiting for USBMUX TCP data", null, 2, null);
                }
            }
            IphoneUsbException it = this.failure;
            if (it != null) {
                throw it;
            }
            if (this.received.isEmpty()) {
                return new byte[0];
            }
            byte[] packet = this.received.removeFirst();
            if (packet.length > maxBytes) {
                ArrayDeque<byte[]> arrayDeque = this.received;
                Intrinsics.checkNotNull(packet);
                arrayDeque.addFirst(ArraysKt.copyOfRange(packet, maxBytes, packet.length));
                byte[] bArrCopyOf = Arrays.copyOf(packet, maxBytes);
                Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
                packet = bArrCopyOf;
            }
            return packet;
        }
    }

    @Override // com.carplay.iap2.transport.BlockingDuplexByteStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        Pair sequenceAndAck;
        synchronized (this.stateLock) {
            if (this.closed) {
                return;
            }
            this.closed = true;
            this.stateLock.notifyAll();
            boolean shouldSendFin = this.connected;
            if (shouldSendFin) {
                try {
                    synchronized (this.writeLock) {
                        synchronized (this.stateLock) {
                            sequenceAndAck = TuplesKt.to(Integer.valueOf(this.nextSequence), Integer.valueOf(this.nextAcknowledgement));
                        }
                        this.host.sendTcp$core_iap2(this.sourcePort, this.destinationPort, ((Number) sequenceAndAck.getFirst()).intValue(), ((Number) sequenceAndAck.getSecond()).intValue(), 17, new byte[0]);
                        Unit unit = Unit.INSTANCE;
                    }
                } catch (IphoneUsbException e) {
                }
            }
            this.host.removeConnection$core_iap2(this);
        }
    }

    public final void beginConnect$core_iap2() throws Throwable {
        sendControl(2);
    }

    public final boolean awaitConnected$core_iap2(long timeoutMillis) {
        boolean z;
        long deadline = (timeoutMillis * 1000000) + System.nanoTime();
        synchronized (this.stateLock) {
            while (true) {
                z = false;
                if (this.connected || this.closed) {
                    break;
                }
                long remainingNanos = deadline - System.nanoTime();
                if (remainingNanos <= 0) {
                    return false;
                }
                try {
                    this.stateLock.wait(remainingNanos / 1000000, (int) (remainingNanos % 1000000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IphoneUsbException.DeviceUnavailable("Interrupted while connecting USBMUX TCP", null, 2, null);
                }
            }
            IphoneUsbException it = this.failure;
            if (it != null) {
                throw it;
            }
            if (this.connected && !this.closed) {
                z = true;
            }
            return z;
        }
    }

    public final void abort$core_iap2() throws Throwable {
        synchronized (this.stateLock) {
            if (this.closed) {
                return;
            }
            this.closed = true;
            this.stateLock.notifyAll();
            if (1 != 0) {
                try {
                    sendControl(20);
                } catch (IphoneUsbException e) {
                }
            }
            this.host.removeConnection$core_iap2(this);
        }
    }

    public static /* synthetic */ void closeFromHost$core_iap2$default(Iap2UsbMuxTcpConnection iap2UsbMuxTcpConnection, IphoneUsbException iphoneUsbException, int i, Object obj) {
        if ((i & 1) != 0) {
            iphoneUsbException = null;
        }
        iap2UsbMuxTcpConnection.closeFromHost$core_iap2(iphoneUsbException);
    }

    public final void closeFromHost$core_iap2(IphoneUsbException error) {
        synchronized (this.stateLock) {
            if (!this.closed) {
                this.failure = error == null ? this.failure : error;
                this.closed = true;
                this.stateLock.notifyAll();
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public final void onPacket$core_iap2(int flags, int sequence, byte[] payload) throws Throwable {
        Intrinsics.checkNotNullParameter(payload, "payload");
        if ((flags & 4) != 0) {
            synchronized (this.stateLock) {
                this.failure = new IphoneUsbException.DeviceUnavailable("USBMUX TCP connection was reset by the peer", null, 2, null);
                this.closed = true;
                this.stateLock.notifyAll();
                Unit unit = Unit.INSTANCE;
            }
            this.host.removeConnection$core_iap2(this);
            return;
        }
        if ((flags & 2) != 0 && (flags & 16) != 0) {
            synchronized (this.stateLock) {
                if (this.closed) {
                    return;
                }
                this.nextSequence++;
                this.nextAcknowledgement = sequence + 1;
                Unit unit2 = Unit.INSTANCE;
                sendControl(16);
                synchronized (this.stateLock) {
                    if (!this.closed) {
                        this.connected = true;
                        this.stateLock.notifyAll();
                    }
                    Unit unit3 = Unit.INSTANCE;
                }
                return;
            }
        }
        if (!(payload.length == 0)) {
            synchronized (this.stateLock) {
                if (this.closed) {
                    return;
                }
                this.nextAcknowledgement += payload.length;
                this.received.addLast(payload);
                this.stateLock.notifyAll();
                Unit unit4 = Unit.INSTANCE;
                sendControl(16);
            }
        }
        if ((flags & 1) != 0) {
            synchronized (this.stateLock) {
                if (this.closed) {
                    return;
                }
                this.nextAcknowledgement++;
                Unit unit5 = Unit.INSTANCE;
                sendControl(16);
                synchronized (this.stateLock) {
                    this.closed = true;
                    this.stateLock.notifyAll();
                    Unit unit6 = Unit.INSTANCE;
                }
                this.host.removeConnection$core_iap2(this);
            }
        }
    }

    private final void sendControl(int flags) throws Throwable {
        Throwable th;
        Pair sequenceAndAck;
        synchronized (this.writeLock) {
            try {
                synchronized (this.stateLock) {
                    try {
                        sequenceAndAck = TuplesKt.to(Integer.valueOf(this.nextSequence), Integer.valueOf(this.nextAcknowledgement));
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
                this.host.sendTcp$core_iap2(this.sourcePort, this.destinationPort, ((Number) sequenceAndAck.getFirst()).intValue(), ((Number) sequenceAndAck.getSecond()).intValue(), flags, new byte[0]);
                Unit unit = Unit.INSTANCE;
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    private final void checkConnectedLocked() throws IphoneUsbException {
        IphoneUsbException it = this.failure;
        if (it != null) {
            throw it;
        }
        if (!this.connected || this.closed) {
            throw new IphoneUsbException.DeviceUnavailable("USBMUX TCP connection is not open", null, 2, null);
        }
    }

    /* JADX INFO: compiled from: Iap2UsbMuxHost.kt */
    @Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0086T¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lcom/carplay/iap2/transport/Iap2UsbMuxTcpConnection$Companion;", "", "<init>", "()V", "TCP_FIN", "", "TCP_SYN", "TCP_RST", "TCP_ACK", "MAX_SEND_PAYLOAD_BYTES", "NANOS_PER_MILLISECOND", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
