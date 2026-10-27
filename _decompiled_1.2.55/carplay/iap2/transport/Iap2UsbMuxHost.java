package com.carplay.iap2.transport;

import android.util.Log;
import java.io.Closeable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.UByte;
import kotlin.UInt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.UStringsKt;

/* JADX INFO: compiled from: Iap2UsbMuxHost.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0012\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u0000 92\u00020\u0001:\u00019B-\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007¢\u0006\u0004\b\n\u0010\u000bJ\u001a\u0010\u001e\u001a\u00020\u00122\b\b\u0002\u0010\u001f\u001a\u00020\u00112\b\b\u0002\u0010 \u001a\u00020\u0005J\b\u0010!\u001a\u00020\tH\u0016J=\u0010\"\u001a\u00020\t2\u0006\u0010#\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u00112\u0006\u0010$\u001a\u00020\u00112\u0006\u0010%\u001a\u00020\u00112\u0006\u0010&\u001a\u00020\u00112\u0006\u0010'\u001a\u00020(H\u0000¢\u0006\u0002\b)J\u0015\u0010*\u001a\u00020\t2\u0006\u0010+\u001a\u00020\u0012H\u0000¢\u0006\u0002\b,J\b\u0010-\u001a\u00020\tH\u0002J\u0012\u0010.\u001a\u0004\u0018\u00010/2\u0006\u0010 \u001a\u00020\u0005H\u0002J\u0018\u00100\u001a\u00020\t2\u0006\u00101\u001a\u00020\u00112\u0006\u0010'\u001a\u00020(H\u0002J\b\u00102\u001a\u00020\tH\u0002J\u0010\u00103\u001a\u00020\t2\u0006\u00104\u001a\u00020(H\u0002J\u0010\u00105\u001a\u00020\t2\u0006\u00106\u001a\u00020\u0016H\u0002J\b\u00107\u001a\u00020\u0011H\u0002J\b\u00108\u001a\u00020\tH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00120\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006:"}, d2 = {"Lcom/carplay/iap2/transport/Iap2UsbMuxHost;", "Ljava/io/Closeable;", "pipe", "Lcom/carplay/iap2/transport/Iap2UsbSession;", "readTimeoutMillis", "", "onDiagnostic", "Lkotlin/Function1;", "", "", "<init>", "(Lcom/carplay/iap2/transport/Iap2UsbSession;JLkotlin/jvm/functions/Function1;)V", "stateLock", "", "writeLock", "connections", "", "", "Lcom/carplay/iap2/transport/Iap2UsbMuxTcpConnection;", "closed", "", "failure", "Lcom/carplay/iap2/transport/IphoneUsbException;", "nextMuxSequence", "nextMuxAcknowledgement", "nextSourcePort", "readerThread", "Ljava/lang/Thread;", "receiveFrames", "Lcom/carplay/iap2/transport/UsbMuxFrameBuffer;", "connect", "destinationPort", "timeoutMillis", "close", "sendTcp", "sourcePort", "sequence", "acknowledgement", "flags", "payload", "", "sendTcp$core_iap2", "removeConnection", "connection", "removeConnection$core_iap2", "begin", "takeFrame", "Lcom/carplay/iap2/transport/UsbMuxFrame;", "sendFrame", "protocol", "readerLoop", "dispatchTcp", "frame", "fail", "error", "allocateSourcePortLocked", "checkOpenLocked", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2UsbMuxHost implements Closeable {
    private static final long CLOSE_JOIN_MARGIN_MILLIS = 100;
    private static final long CONNECT_TIMEOUT_MILLIS = 5000;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final int FIRST_SOURCE_PORT = 1;
    private static final long HANDSHAKE_TIMEOUT_MILLIS = 60000;
    public static final int LOCKDOWN_PORT = 62078;
    private static final int MAX_STALE_HANDSHAKE_FRAMES = 32;
    private static final int MUX_HEADER_BYTES = 16;
    private static final int MUX_MAGIC = -17958194;
    private static final long NANOS_PER_MILLISECOND = 1000000;
    private static final int PROTOCOL_SETUP = 2;
    private static final int PROTOCOL_TCP = 6;
    private static final int PROTOCOL_VERSION = 0;
    private static final int SETUP_VALUE = 7;
    private static final int TCP_HEADER_BYTES = 20;
    private static final int TCP_WINDOW_FIELD = 512;
    private static final int USBMUX_VERSION = 2;
    private static final int VERSION_MESSAGE_BYTES = 20;
    private static final int WRITE_TIMEOUT_MILLIS = 2000;
    private boolean closed;
    private final Map<Integer, Iap2UsbMuxTcpConnection> connections;
    private IphoneUsbException failure;
    private int nextMuxAcknowledgement;
    private int nextMuxSequence;
    private int nextSourcePort;
    private final Function1<String, Unit> onDiagnostic;
    private final Iap2UsbSession pipe;
    private final long readTimeoutMillis;
    private Thread readerThread;
    private final UsbMuxFrameBuffer receiveFrames;
    private final Object stateLock;
    private final Object writeLock;

    public /* synthetic */ Iap2UsbMuxHost(Iap2UsbSession iap2UsbSession, long j, Function1 function1, DefaultConstructorMarker defaultConstructorMarker) {
        this(iap2UsbSession, j, function1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Iap2UsbMuxHost(Iap2UsbSession pipe, long readTimeoutMillis, Function1<? super String, Unit> function1) {
        this.pipe = pipe;
        this.readTimeoutMillis = readTimeoutMillis;
        this.onDiagnostic = function1;
        this.stateLock = new Object();
        this.writeLock = new Object();
        this.connections = new LinkedHashMap();
        this.nextSourcePort = 1;
        this.receiveFrames = new UsbMuxFrameBuffer(new Function1() { // from class: com.carplay.iap2.transport.Iap2UsbMuxHost$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Iap2UsbMuxHost.receiveFrames$lambda$1(this.f$0, (String) obj);
            }
        });
    }

    static final Unit receiveFrames$lambda$1(Iap2UsbMuxHost this$0, String line) {
        Intrinsics.checkNotNullParameter(line, "line");
        Log.w("xcertplay-usb", line);
        try {
            Result.Companion companion = Result.INSTANCE;
            this$0.onDiagnostic.invoke(line);
            Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        return Unit.INSTANCE;
    }

    public static /* synthetic */ Iap2UsbMuxTcpConnection connect$default(Iap2UsbMuxHost iap2UsbMuxHost, int i, long j, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = LOCKDOWN_PORT;
        }
        if ((i2 & 2) != 0) {
            j = 5000;
        }
        return iap2UsbMuxHost.connect(i, j);
    }

    public final Iap2UsbMuxTcpConnection connect(int destinationPort, long timeoutMillis) throws Throwable {
        Iap2UsbMuxTcpConnection connection;
        if (!(1 <= destinationPort && destinationPort < 65536)) {
            throw new IllegalArgumentException("destinationPort must be a valid TCP port".toString());
        }
        if (!(timeoutMillis > 0)) {
            throw new IllegalArgumentException("timeoutMillis must be positive".toString());
        }
        synchronized (this.stateLock) {
            checkOpenLocked();
            int sourcePort = allocateSourcePortLocked();
            connection = new Iap2UsbMuxTcpConnection(this, sourcePort, destinationPort);
            this.connections.put(Integer.valueOf(sourcePort), connection);
        }
        try {
            connection.beginConnect$core_iap2();
            if (!connection.awaitConnected$core_iap2(timeoutMillis)) {
                connection.abort$core_iap2();
                throw new IphoneUsbException.TimedOut("USBMUX TCP connection to port " + destinationPort + " timed out", null, 2, null);
            }
            return connection;
        } catch (IphoneUsbException error) {
            removeConnection$core_iap2(connection);
            throw error;
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        Thread thread;
        synchronized (this.stateLock) {
            if (this.closed) {
                return;
            }
            this.closed = true;
            Iterable activeConnections = CollectionsKt.toList(this.connections.values());
            this.connections.clear();
            Iterable $this$forEach$iv = activeConnections;
            Iterator it = $this$forEach$iv.iterator();
            while (true) {
                thread = null;
                if (!it.hasNext()) {
                    break;
                }
                Object element$iv = it.next();
                Iap2UsbMuxTcpConnection p0 = (Iap2UsbMuxTcpConnection) element$iv;
                Iap2UsbMuxTcpConnection.closeFromHost$core_iap2$default(p0, null, 1, null);
            }
            this.pipe.close();
            if (this.readerThread != null) {
                Thread threadCurrentThread = Thread.currentThread();
                Thread thread2 = this.readerThread;
                if (thread2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("readerThread");
                    thread2 = null;
                }
                if (threadCurrentThread != thread2) {
                    try {
                        Thread thread3 = this.readerThread;
                        if (thread3 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("readerThread");
                        } else {
                            thread = thread3;
                        }
                        thread.join(this.readTimeoutMillis + CLOSE_JOIN_MARGIN_MILLIS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    public final void sendTcp$core_iap2(int sourcePort, int destinationPort, int sequence, int acknowledgement, int flags, byte[] payload) throws Throwable {
        Intrinsics.checkNotNullParameter(payload, "payload");
        if (payload.length > 512) {
            Log.i("xcertplay-usb", "usbmux TX source=" + sourcePort + " destination=" + destinationPort + " bytes=" + payload.length + " seq=" + sequence + " ack=" + acknowledgement);
        }
        byte[] tcp = new byte[payload.length + 20];
        INSTANCE.putU16(tcp, 0, sourcePort);
        INSTANCE.putU16(tcp, 2, destinationPort);
        INSTANCE.putU32(tcp, 4, sequence);
        INSTANCE.putU32(tcp, 8, acknowledgement);
        tcp[12] = 80;
        tcp[13] = (byte) flags;
        INSTANCE.putU16(tcp, 14, 512);
        ArraysKt.copyInto$default(payload, tcp, 20, 0, 0, 12, (Object) null);
        sendFrame(6, tcp);
    }

    public final void removeConnection$core_iap2(Iap2UsbMuxTcpConnection connection) {
        Intrinsics.checkNotNullParameter(connection, "connection");
        synchronized (this.stateLock) {
            if (this.connections.get(Integer.valueOf(connection.getSourcePort())) == connection) {
                this.connections.remove(Integer.valueOf(connection.getSourcePort()));
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void begin() throws Throwable {
        byte[] version = new byte[20];
        INSTANCE.putU32(version, 0, 0);
        INSTANCE.putU32(version, 4, 20);
        INSTANCE.putU32(version, 8, 2);
        this.pipe.write(version, 60000);
        long deadline = System.nanoTime() + 60000000000L;
        int staleFrames = 0;
        while (true) {
            long remainingNanos = deadline - System.nanoTime();
            if (remainingNanos <= 0) {
                throw new IphoneUsbException.TimedOut("Timed out waiting for the USBMUX version reply", null, 2, null);
            }
            long remainingMillis = ((remainingNanos + 1000000) - 1) / 1000000;
            UsbMuxFrame reply = takeFrame(remainingMillis);
            if (reply == null) {
                throw new IphoneUsbException.TimedOut("Timed out waiting for the USBMUX version reply", null, 2, null);
            }
            if (reply.getProtocol() == 0 && reply.getLength() == 20 && reply.getWord8() == 2) {
                Log.i("xcertplay-usb", "usbmux version accepted: " + reply.getWord8());
                sendFrame(2, new byte[]{7});
                Thread $this$begin_u24lambda_u249 = new Thread(new Runnable() { // from class: com.carplay.iap2.transport.Iap2UsbMuxHost$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() throws Throwable {
                        this.f$0.readerLoop();
                    }
                }, "iap2-usbmux-reader");
                $this$begin_u24lambda_u249.setDaemon(true);
                $this$begin_u24lambda_u249.start();
                this.readerThread = $this$begin_u24lambda_u249;
                return;
            }
            if (reply.getProtocol() != 6 || (staleFrames = staleFrames + 1) > 32) {
                throw new IphoneUsbException.Protocol("Invalid USBMUX version reply: proto=" + reply.getProtocol() + " length=" + reply.getLength() + " version=" + reply.getWord8());
            }
            Log.i("xcertplay-usb", "discarding stale usbmux TCP frame before version reply");
        }
    }

    private final UsbMuxFrame takeFrame(long timeoutMillis) throws IphoneUsbException {
        long deadline = System.nanoTime() + (timeoutMillis * 1000000);
        while (true) {
            synchronized (this.stateLock) {
                if (this.closed) {
                    throw new IphoneUsbException.DeviceUnavailable("USBMUX host is closed", null, 2, null);
                }
                UsbMuxFrame frame = this.receiveFrames.takeFrame();
                if (frame != null) {
                    Log.i("xcertplay-usb", "usbmux rx proto=" + frame.getProtocol() + " length=" + frame.getLength() + " word8=0x" + UStringsKt.m1486toStringV7xB4Y4(UInt.m254constructorimpl(frame.getWord8()), 16));
                    this.nextMuxAcknowledgement = frame.getSequence();
                    return frame;
                }
                long remainingNanos = deadline - System.nanoTime();
                if (remainingNanos <= 0) {
                    return null;
                }
                long remainingMillis = ((remainingNanos + 1000000) - 1) / 1000000;
                byte[] bytes = this.pipe.read(remainingMillis);
                if (bytes != null) {
                    synchronized (this.stateLock) {
                        this.receiveFrames.append(bytes);
                        Unit unit = Unit.INSTANCE;
                    }
                }
            }
        }
    }

    private final void sendFrame(int protocol, byte[] payload) throws Throwable {
        Throwable th;
        Pair sequenceAndAcknowledgement;
        synchronized (this.writeLock) {
            try {
                synchronized (this.stateLock) {
                    try {
                        checkOpenLocked();
                        sequenceAndAcknowledgement = TuplesKt.to(Integer.valueOf(this.nextMuxSequence), Integer.valueOf(this.nextMuxAcknowledgement));
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
                byte[] frame = new byte[payload.length + 16];
                INSTANCE.putU32(frame, 0, protocol);
                INSTANCE.putU32(frame, 4, frame.length);
                INSTANCE.putU32(frame, 8, MUX_MAGIC);
                INSTANCE.putU16(frame, 12, ((Number) sequenceAndAcknowledgement.getFirst()).intValue());
                INSTANCE.putU16(frame, 14, ((Number) sequenceAndAcknowledgement.getSecond()).intValue());
                ArraysKt.copyInto$default(payload, frame, 16, 0, 0, 12, (Object) null);
                try {
                    this.pipe.write(frame, 2000);
                    synchronized (this.stateLock) {
                        this.nextMuxSequence = (this.nextMuxSequence + 1) & 65535;
                        Unit unit = Unit.INSTANCE;
                    }
                    Unit unit2 = Unit.INSTANCE;
                } catch (IphoneUsbException error) {
                    fail(error);
                    throw error;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void readerLoop() throws Throwable {
        while (true) {
            try {
                synchronized (this.stateLock) {
                    if (this.closed) {
                        return;
                    } else {
                        Unit unit = Unit.INSTANCE;
                    }
                }
                UsbMuxFrame frame = takeFrame(this.readTimeoutMillis);
                if (frame != null && frame.getProtocol() == 6) {
                    dispatchTcp(frame.getPayload());
                }
            } catch (IphoneUsbException error) {
                fail(error);
                return;
            } catch (RuntimeException error2) {
                fail(new IphoneUsbException.DeviceUnavailable("USBMUX reader failed", error2));
                return;
            }
        }
    }

    private final void dispatchTcp(byte[] frame) throws Throwable {
        Iap2UsbMuxTcpConnection connection;
        int length = frame.length;
        if (length < 20) {
            throw new IphoneUsbException.Protocol("USBMUX TCP frame is shorter than its header");
        }
        int tcpHeaderBytes = ((frame[0 + 12] >>> 4) & 15) * 4;
        if (tcpHeaderBytes < 20 || tcpHeaderBytes > length) {
            throw new IphoneUsbException.Protocol("Invalid USBMUX TCP header length");
        }
        int destinationPort = INSTANCE.readU16(frame, 0 + 2);
        if (length == tcpHeaderBytes) {
            Log.i("xcertplay-usb", "usbmux TCP control destination=" + destinationPort + " flags=" + (frame[13] & UByte.MAX_VALUE) + " ack=" + INSTANCE.readU32(frame, 8) + " window=" + INSTANCE.readU16(frame, 14));
        }
        synchronized (this.stateLock) {
            connection = this.connections.get(Integer.valueOf(destinationPort));
        }
        if (connection == null) {
            return;
        }
        connection.onPacket$core_iap2(frame[0 + 13] & UByte.MAX_VALUE, INSTANCE.readU32(frame, 0 + 4), ArraysKt.copyOfRange(frame, 0 + tcpHeaderBytes, 0 + length));
    }

    private final void fail(IphoneUsbException error) {
        synchronized (this.stateLock) {
            if (this.closed) {
                return;
            }
            IphoneUsbException iphoneUsbException = this.failure;
            if (iphoneUsbException == null) {
                iphoneUsbException = error;
            }
            this.failure = iphoneUsbException;
            this.closed = true;
            Iterable activeConnections = CollectionsKt.toList(this.connections.values());
            this.connections.clear();
            Iterable $this$forEach$iv = activeConnections;
            for (Object element$iv : $this$forEach$iv) {
                Iap2UsbMuxTcpConnection it = (Iap2UsbMuxTcpConnection) element$iv;
                it.closeFromHost$core_iap2(error);
            }
            this.pipe.close();
        }
    }

    private final int allocateSourcePortLocked() throws IphoneUsbException.DeviceUnavailable {
        for (int i = 0; i < 65535; i++) {
            int candidate = this.nextSourcePort;
            this.nextSourcePort = candidate == 65535 ? 1 : candidate + 1;
            if (!this.connections.containsKey(Integer.valueOf(candidate))) {
                return candidate;
            }
        }
        throw new IphoneUsbException.DeviceUnavailable("USBMUX has no free TCP source ports", null, 2, null);
    }

    private final void checkOpenLocked() throws IphoneUsbException {
        IphoneUsbException it = this.failure;
        if (it != null) {
            throw it;
        }
        if (this.closed) {
            throw new IphoneUsbException.DeviceUnavailable("USBMUX host is closed", null, 2, null);
        }
    }

    /* JADX INFO: compiled from: Iap2UsbMuxHost.kt */
    @Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J.\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\b\b\u0002\u0010\u001c\u001a\u00020\u000e2\u0014\b\u0002\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020 0\u001eJ \u0010!\u001a\u00020 2\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020\u00052\u0006\u0010%\u001a\u00020\u0005H\u0002J \u0010&\u001a\u00020 2\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020\u00052\u0006\u0010%\u001a\u00020\u0005H\u0002J\u0018\u0010'\u001a\u00020\u00052\u0006\u0010(\u001a\u00020#2\u0006\u0010$\u001a\u00020\u0005H\u0002J\u0018\u0010)\u001a\u00020\u00052\u0006\u0010(\u001a\u00020#2\u0006\u0010$\u001a\u00020\u0005H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000eX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000eX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u000eX\u0082T¢\u0006\u0002\n\u0000¨\u0006*"}, d2 = {"Lcom/carplay/iap2/transport/Iap2UsbMuxHost$Companion;", "", "<init>", "()V", "LOCKDOWN_PORT", "", "PROTOCOL_VERSION", "PROTOCOL_SETUP", "PROTOCOL_TCP", "USBMUX_VERSION", "SETUP_VALUE", "MUX_MAGIC", "VERSION_MESSAGE_BYTES", "NANOS_PER_MILLISECOND", "", "MUX_HEADER_BYTES", "TCP_HEADER_BYTES", "TCP_WINDOW_FIELD", "FIRST_SOURCE_PORT", "HANDSHAKE_TIMEOUT_MILLIS", "MAX_STALE_HANDSHAKE_FRAMES", "CONNECT_TIMEOUT_MILLIS", "WRITE_TIMEOUT_MILLIS", "CLOSE_JOIN_MARGIN_MILLIS", "open", "Lcom/carplay/iap2/transport/Iap2UsbMuxHost;", "pipe", "Lcom/carplay/iap2/transport/Iap2UsbSession;", "readTimeoutMillis", "onDiagnostic", "Lkotlin/Function1;", "", "", "putU16", "target", "", "offset", "value", "putU32", "readU16", "source", "readU32", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2UsbMuxHost open$default(Companion companion, Iap2UsbSession iap2UsbSession, long j, Function1 function1, int i, Object obj) {
            if ((i & 2) != 0) {
                j = 1000;
            }
            if ((i & 4) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.transport.Iap2UsbMuxHost$Companion$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2UsbMuxHost.Companion.open$lambda$0((String) obj2);
                    }
                };
            }
            return companion.open(iap2UsbSession, j, function1);
        }

        static final Unit open$lambda$0(String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2UsbMuxHost open(Iap2UsbSession pipe, long readTimeoutMillis, Function1<? super String, Unit> onDiagnostic) {
            Intrinsics.checkNotNullParameter(pipe, "pipe");
            Intrinsics.checkNotNullParameter(onDiagnostic, "onDiagnostic");
            if (!(readTimeoutMillis > 0)) {
                throw new IllegalArgumentException("readTimeoutMillis must be positive".toString());
            }
            Iap2UsbMuxHost it = new Iap2UsbMuxHost(pipe, readTimeoutMillis, onDiagnostic, null);
            try {
                it.begin();
                return it;
            } catch (Throwable error) {
                it.close();
                throw error;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void putU16(byte[] target, int offset, int value) {
            target[offset] = (byte) (value >>> 8);
            target[offset + 1] = (byte) value;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void putU32(byte[] target, int offset, int value) {
            target[offset] = (byte) (value >>> 24);
            target[offset + 1] = (byte) (value >>> 16);
            target[offset + 2] = (byte) (value >>> 8);
            target[offset + 3] = (byte) value;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int readU16(byte[] source, int offset) {
            return ((source[offset] & UByte.MAX_VALUE) << 8) | (source[offset + 1] & UByte.MAX_VALUE);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int readU32(byte[] source, int offset) {
            return ((source[offset] & UByte.MAX_VALUE) << 24) | ((source[offset + 1] & UByte.MAX_VALUE) << 16) | ((source[offset + 2] & UByte.MAX_VALUE) << 8) | (source[offset + 3] & UByte.MAX_VALUE);
        }
    }
}
