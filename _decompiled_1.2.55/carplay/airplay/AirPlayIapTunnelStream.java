package com.carplay.airplay;

import androidx.core.os.EnvironmentCompat;
import com.carplay.iap2.transport.BlockingDuplexByteStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: AirPlayIapTunnelStream.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0002\u0010\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u0000 )2\u00020\u0001:\u0001)B3\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u001a\b\u0002\u0010\u0006\u001a\u0014\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\u0007¢\u0006\u0004\b\u000b\u0010\fJ\u0006\u0010\u0018\u001a\u00020\u0012J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\bH\u0016J\u001a\u0010\u001c\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001d\u001a\u00020\u00122\u0006\u0010\u001e\u001a\u00020\tH\u0016J\b\u0010\u001f\u001a\u00020\u001aH\u0016J\u0010\u0010 \u001a\u00020\u001a2\u0006\u0010!\u001a\u00020\bH\u0002J\u0012\u0010\"\u001a\u00020\n2\b\u0010#\u001a\u0004\u0018\u00010\u0015H\u0002J\b\u0010$\u001a\u00020\u001aH\u0002J\b\u0010%\u001a\u00020&H\u0002J\b\u0010'\u001a\u00020\u0017H\u0002J\u0012\u0010(\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001d\u001a\u00020\u0012H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R \u0010\u0006\u001a\u0014\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\b0\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006*"}, d2 = {"Lcom/carplay/airplay/AirPlayIapTunnelStream;", "Lcom/carplay/iap2/transport/BlockingDuplexByteStream;", "session", "Lcom/carplay/airplay/AirPlaySession;", "tunnel", "Lcom/carplay/airplay/IapTunnel;", "sendCommand", "Lkotlin/Function2;", "", "", "", "<init>", "(Lcom/carplay/airplay/AirPlaySession;Lcom/carplay/airplay/IapTunnel;Lkotlin/jvm/functions/Function2;)V", "lock", "Ljava/lang/Object;", "pending", "Ljava/util/ArrayDeque;", "pendingBytes", "", "closed", "terminalFailure", "", "closeLocation", "", "listen", "send", "", "data", "recv", "maxBytes", "timeoutMillis", "close", "offer", "bytes", "markFailed", "cause", "throwTerminalFailureLocked", "closedFailureLocked", "Ljava/io/IOException;", "closeCaller", "takePendingLocked", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AirPlayIapTunnelStream implements BlockingDuplexByteStream {
    private static final Companion Companion = new Companion(null);
    private static final byte[] EMPTY = new byte[0];

    @Deprecated
    public static final long EVENT_READY_TIMEOUT_MILLIS = 10000;

    @Deprecated
    public static final int MAX_PENDING_BYTES = 1048576;

    @Deprecated
    public static final long NANOS_PER_MILLISECOND = 1000000;

    @Deprecated
    public static final long PEER_CONNECT_TIMEOUT_MILLIS = 15000;
    private String closeLocation;
    private boolean closed;
    private final Object lock;
    private final ArrayDeque<byte[]> pending;
    private int pendingBytes;
    private final Function2<byte[], Long, Boolean> sendCommand;
    private final AirPlaySession session;
    private Throwable terminalFailure;
    private final IapTunnel tunnel;

    /* JADX WARN: Multi-variable type inference failed */
    public AirPlayIapTunnelStream(AirPlaySession session, IapTunnel tunnel, Function2<? super byte[], ? super Long, Boolean> sendCommand) {
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(tunnel, "tunnel");
        Intrinsics.checkNotNullParameter(sendCommand, "sendCommand");
        this.session = session;
        this.tunnel = tunnel;
        this.sendCommand = sendCommand;
        this.lock = new Object();
        this.pending = new ArrayDeque<>();
    }

    public /* synthetic */ AirPlayIapTunnelStream(final AirPlaySession airPlaySession, IapTunnel iapTunnel, Function2 function2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(airPlaySession, iapTunnel, (i & 4) != 0 ? new Function2() { // from class: com.carplay.airplay.AirPlayIapTunnelStream$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return Boolean.valueOf(AirPlayIapTunnelStream._init_$lambda$0(airPlaySession, (byte[]) obj, ((Long) obj2).longValue()));
            }
        } : function2);
    }

    static final boolean _init_$lambda$0(AirPlaySession $session, byte[] data, long timeoutMillis) {
        Intrinsics.checkNotNullParameter(data, "data");
        return $session.sendIapMessage(data, timeoutMillis);
    }

    public final int listen() {
        return this.tunnel.listen(new IapTunnel.Listener() { // from class: com.carplay.airplay.AirPlayIapTunnelStream.listen.1
            @Override // com.carplay.airplay.IapTunnel.Listener
            public void onOpen(String remoteAddress) {
                AirPlayIapTunnelStream.this.session.logTrace$core_airplay("AirPlay iAP tunnel connected remote=" + (remoteAddress == null ? EnvironmentCompat.MEDIA_UNKNOWN : remoteAddress));
            }

            @Override // com.carplay.airplay.IapTunnel.Listener
            public void onIap(byte[] bytes) {
                Intrinsics.checkNotNullParameter(bytes, "bytes");
                AirPlayIapTunnelStream.this.offer(bytes);
            }

            @Override // com.carplay.airplay.IapTunnel.Listener
            public void onDebug(String message) {
                Intrinsics.checkNotNullParameter(message, "message");
                AirPlayIapTunnelStream.this.session.logTrace$core_airplay(message);
            }

            @Override // com.carplay.airplay.IapTunnel.Listener
            public void onClosed(Throwable cause) {
                String simpleName;
                String message;
                Class<?> cls;
                AirPlaySession airPlaySession = AirPlayIapTunnelStream.this.session;
                if (cause == null || (cls = cause.getClass()) == null || (simpleName = cls.getSimpleName()) == null) {
                    simpleName = "peer EOF";
                }
                if (cause == null || (message = cause.getMessage()) == null) {
                    message = "no detail";
                }
                airPlaySession.logTrace$core_airplay("AirPlay iAP tunnel closed cause=" + simpleName + ": " + message);
                boolean shouldCloseSession = AirPlayIapTunnelStream.this.markFailed(cause);
                if (shouldCloseSession) {
                    AirPlayIapTunnelStream.this.session.close();
                }
            }
        });
    }

    @Override // com.carplay.iap2.transport.BlockingDuplexByteStream
    public void send(byte[] data) throws IOException {
        Intrinsics.checkNotNullParameter(data, "data");
        synchronized (this.lock) {
            throwTerminalFailureLocked();
            if (this.closed) {
                throw closedFailureLocked();
            }
            Unit unit = Unit.INSTANCE;
        }
        if (!this.tunnel.awaitPeerConnection(15000L)) {
            throw new IOException("CarPlay iAP tunnel peer did not connect");
        }
        synchronized (this.lock) {
            throwTerminalFailureLocked();
            if (this.closed) {
                throw closedFailureLocked();
            }
            Unit unit2 = Unit.INSTANCE;
        }
        if (!this.sendCommand.invoke(data, Long.valueOf(EVENT_READY_TIMEOUT_MILLIS)).booleanValue()) {
            throw new IOException("AirPlay event channel rejected an iAP message");
        }
    }

    @Override // com.carplay.iap2.transport.BlockingDuplexByteStream
    public byte[] recv(int maxBytes, long timeoutMillis) {
        if (!(maxBytes > 0)) {
            throw new IllegalArgumentException("maxBytes must be positive".toString());
        }
        long j = 0;
        if (!(timeoutMillis >= 0)) {
            throw new IllegalArgumentException("timeoutMillis must not be negative".toString());
        }
        long deadlineNanos = System.nanoTime() + (timeoutMillis * 1000000);
        synchronized (this.lock) {
            while (true) {
                byte[] bArrTakePendingLocked = takePendingLocked(maxBytes);
                if (bArrTakePendingLocked != null) {
                    return bArrTakePendingLocked;
                }
                throwTerminalFailureLocked();
                if (this.closed) {
                    return EMPTY;
                }
                long remainingNanos = deadlineNanos - System.nanoTime();
                if (remainingNanos <= j) {
                    return null;
                }
                try {
                    this.lock.wait(remainingNanos / 1000000, (int) (remainingNanos % 1000000));
                    j = 0;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
        }
    }

    @Override // com.carplay.iap2.transport.BlockingDuplexByteStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        synchronized (this.lock) {
            if (this.closed) {
                return;
            }
            this.closed = true;
            this.closeLocation = closeCaller();
            this.lock.notifyAll();
            Unit unit = Unit.INSTANCE;
            this.tunnel.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void offer(byte[] bytes) {
        if (bytes.length == 0) {
            return;
        }
        synchronized (this.lock) {
            if (!this.closed && this.terminalFailure == null) {
                if (this.pendingBytes + bytes.length > 1048576) {
                    this.terminalFailure = new IOException("AirPlay iAP tunnel pending-data limit exceeded");
                    this.lock.notifyAll();
                    this.tunnel.close();
                    return;
                }
                ArrayDeque<byte[]> arrayDeque = this.pending;
                byte[] bArrCopyOf = Arrays.copyOf(bytes, bytes.length);
                Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
                arrayDeque.addLast(bArrCopyOf);
                this.pendingBytes += bytes.length;
                this.lock.notifyAll();
                Unit unit = Unit.INSTANCE;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean markFailed(Throwable cause) {
        synchronized (this.lock) {
            if (!this.closed && this.terminalFailure == null) {
                this.terminalFailure = cause == null ? new IOException("AirPlay iAP tunnel terminated") : cause;
                this.lock.notifyAll();
                return true;
            }
            return false;
        }
    }

    private final void throwTerminalFailureLocked() throws IOException {
        Throwable failure = this.terminalFailure;
        if (failure == null) {
            return;
        }
        String simpleName = failure.getClass().getSimpleName();
        String message = failure.getMessage();
        if (message == null) {
            message = "unknown error";
        }
        throw new IOException("AirPlay iAP tunnel failed: " + simpleName + ": " + message, failure);
    }

    private final IOException closedFailureLocked() {
        StringBuilder $this$closedFailureLocked_u24lambda_u2411 = new StringBuilder();
        $this$closedFailureLocked_u24lambda_u2411.append("AirPlay iAP tunnel is closed");
        String it = this.closeLocation;
        if (it != null) {
            $this$closedFailureLocked_u24lambda_u2411.append("; requested by " + it);
        }
        return new IOException($this$closedFailureLocked_u24lambda_u2411.toString());
    }

    private final String closeCaller() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        Intrinsics.checkNotNullExpressionValue(stackTrace, "getStackTrace(...)");
        return CollectionsKt.joinToString$default(CollectionsKt.take(ArraysKt.drop(stackTrace, 2), 8), " <- ", null, null, 0, null, new Function1() { // from class: com.carplay.airplay.AirPlayIapTunnelStream$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AirPlayIapTunnelStream.closeCaller$lambda$12((StackTraceElement) obj);
            }
        }, 30, null);
    }

    static final CharSequence closeCaller$lambda$12(StackTraceElement frame) {
        String className = frame.getClassName();
        Intrinsics.checkNotNullExpressionValue(className, "getClassName(...)");
        return StringsKt.substringAfterLast$default(className, '.', (String) null, 2, (Object) null) + "." + frame.getMethodName() + ":" + frame.getLineNumber();
    }

    private final byte[] takePendingLocked(int maxBytes) {
        byte[] chunk = this.pending.pollFirst();
        if (chunk == null) {
            return null;
        }
        this.pendingBytes -= chunk.length;
        if (chunk.length <= maxBytes) {
            return chunk;
        }
        byte[] head = Arrays.copyOf(chunk, maxBytes);
        Intrinsics.checkNotNullExpressionValue(head, "copyOf(...)");
        byte[] tail = ArraysKt.copyOfRange(chunk, maxBytes, chunk.length);
        this.pending.addFirst(tail);
        this.pendingBytes += tail.length;
        return head;
    }

    /* JADX INFO: compiled from: AirPlayIapTunnelStream.kt */
    @Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0003\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u000e"}, d2 = {"Lcom/carplay/airplay/AirPlayIapTunnelStream$Companion;", "", "<init>", "()V", "MAX_PENDING_BYTES", "", "PEER_CONNECT_TIMEOUT_MILLIS", "", "EVENT_READY_TIMEOUT_MILLIS", "NANOS_PER_MILLISECOND", "EMPTY", "", "getEMPTY", "()[B", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final byte[] getEMPTY() {
            return AirPlayIapTunnelStream.EMPTY;
        }
    }
}
