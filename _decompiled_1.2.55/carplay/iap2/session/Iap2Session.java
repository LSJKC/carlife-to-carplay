package com.carplay.iap2.session;

import com.carplay.iap2.body.Iap2BodyBuilder;
import com.carplay.iap2.body.Iap2BodyReader;
import com.carplay.iap2.catalog.Iap2Endpoint;
import com.carplay.iap2.message.Iap2Messages;
import com.carplay.iap2.trace.Iap2FrameFormatter;
import com.carplay.iap2.trace.Iap2TraceDirection;
import com.carplay.iap2.transport.BlockingDuplexByteStream;
import com.carplay.iap2.transport.Iap2ArtworkTransfer;
import com.carplay.iap2.transport.Iap2CsmChannel;
import com.carplay.iap2.transport.Iap2HandshakeStatus;
import com.carplay.iap2.transport.PhoneBluetoothHandshakeDeadline;
import com.carplay.iap2.transport.PhoneBluetoothProfile;
import com.carplay.iap2.wire.Iap2Frame;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: Iap2Session.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000\u0082\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 32\u00060\u0001j\u0002`\u0002:\u00013B-\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\t0\b¢\u0006\u0004\b\n\u0010\u000bJ\u0006\u0010\u000f\u001a\u00020\u0010J/\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u00132\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\r0\u0017¢\u0006\u0002\u0010\u0018J\u000e\u0010\u0019\u001a\u00020\r2\u0006\u0010\u001a\u001a\u00020\u0015J\u000e\u0010\u001b\u001a\u00020\r2\u0006\u0010\u001a\u001a\u00020\u0015J\u0018\u0010\u001c\u001a\u00020\t2\u0006\u0010\u001d\u001a\u00020\u001e2\b\b\u0002\u0010\u001a\u001a\u00020\u0015J1\u0010\u001c\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020 2\b\b\u0002\u0010\u001a\u001a\u00020\u00152\u0017\u0010!\u001a\u0013\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\t0\b¢\u0006\u0002\b#J1\u0010$\u001a\u00020\t2\u0006\u0010%\u001a\u00020&2\b\b\u0002\u0010\u001a\u001a\u00020\u00152\u0017\u0010!\u001a\u0013\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\t0\b¢\u0006\u0002\b#J\u0018\u0010'\u001a\u00020\r2\u0006\u0010(\u001a\u00020)2\b\b\u0002\u0010\u001a\u001a\u00020\u0015J\u0010\u0010*\u001a\u0004\u0018\u00010\u001e2\u0006\u0010\u001a\u001a\u00020\u0015J\u000e\u0010+\u001a\u00020,2\u0006\u0010\u001d\u001a\u00020\u001eJ\b\u0010-\u001a\u00020\tH\u0016J\u0018\u0010.\u001a\u00020\t2\u0006\u0010/\u001a\u0002002\u0006\u0010\u001d\u001a\u00020\u001eH\u0002J\u0010\u00101\u001a\u00020\t2\u0006\u00102\u001a\u00020\u0006H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\f\u001a\u00020\r8F¢\u0006\u0006\u001a\u0004\b\f\u0010\u000e¨\u00064"}, d2 = {"Lcom/carplay/iap2/session/Iap2Session;", "Ljava/lang/AutoCloseable;", "Lkotlin/AutoCloseable;", "channel", "Lcom/carplay/iap2/transport/Iap2CsmChannel;", "traceContext", "", "onTrace", "Lkotlin/Function1;", "", "<init>", "(Lcom/carplay/iap2/transport/Iap2CsmChannel;Ljava/lang/String;Lkotlin/jvm/functions/Function1;)V", "isClosed", "", "()Z", "handshakeStatus", "Lcom/carplay/iap2/transport/Iap2HandshakeStatus;", "awaitPhoneBluetoothReady", "profile", "Lcom/carplay/iap2/transport/PhoneBluetoothProfile;", "maximumWaitMillis", "", "cancelled", "Lkotlin/Function0;", "(Lcom/carplay/iap2/transport/PhoneBluetoothProfile;Ljava/lang/Long;Lkotlin/jvm/functions/Function0;)Z", "awaitReady", "timeoutMillis", "awaitOutboundAcknowledged", "send", "frame", "Lcom/carplay/iap2/wire/Iap2Frame;", "endpoint", "Lcom/carplay/iap2/catalog/Iap2Endpoint;", "block", "Lcom/carplay/iap2/body/Iap2BodyBuilder;", "Lkotlin/ExtensionFunctionType;", "sendRaw", "messageId", "", "sendFileTransfer", "bytes", "", "recv", "reader", "Lcom/carplay/iap2/body/Iap2BodyReader;", "close", "emitFrameTrace", "direction", "Lcom/carplay/iap2/trace/Iap2TraceDirection;", "emitTrace", "message", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2Session implements AutoCloseable {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final long DEFAULT_SEND_TIMEOUT_MILLIS = 5000;
    private final Iap2CsmChannel channel;
    private final Function1<String, Unit> onTrace;
    private final String traceContext;

    public /* synthetic */ Iap2Session(Iap2CsmChannel iap2CsmChannel, String str, Function1 function1, DefaultConstructorMarker defaultConstructorMarker) {
        this(iap2CsmChannel, str, function1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Iap2Session(Iap2CsmChannel channel, String traceContext, Function1<? super String, Unit> function1) {
        this.channel = channel;
        this.traceContext = traceContext;
        this.onTrace = function1;
    }

    public final boolean isClosed() {
        return this.channel.isClosed();
    }

    public final Iap2HandshakeStatus handshakeStatus() {
        return this.channel.handshakeStatus();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ boolean awaitPhoneBluetoothReady$default(Iap2Session iap2Session, PhoneBluetoothProfile phoneBluetoothProfile, Long l, Function0 function0, int i, Object obj) {
        if ((i & 2) != 0) {
            l = null;
        }
        if ((i & 4) != 0) {
            function0 = new Function0() { // from class: com.carplay.iap2.session.Iap2Session$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return Boolean.valueOf(Iap2Session.awaitPhoneBluetoothReady$lambda$0());
                }
            };
        }
        return iap2Session.awaitPhoneBluetoothReady(phoneBluetoothProfile, l, function0);
    }

    static final boolean awaitPhoneBluetoothReady$lambda$0() {
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final boolean awaitPhoneBluetoothReady(PhoneBluetoothProfile profile, Long maximumWaitMillis, Function0<Boolean> cancelled) {
        Long lValueOf;
        long j;
        long jCoerceAtLeast;
        Intrinsics.checkNotNullParameter(profile, "profile");
        Intrinsics.checkNotNullParameter(cancelled, "cancelled");
        if (!(maximumWaitMillis == null || maximumWaitMillis.longValue() >= 0)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        long j2 = 1000000;
        Function0 function0 = null;
        Object[] objArr = 0;
        if (maximumWaitMillis != null) {
            lValueOf = Long.valueOf(System.nanoTime() + (Math.min(maximumWaitMillis.longValue(), profile.getReadyTimeoutMillis()) * 1000000));
        } else {
            lValueOf = null;
        }
        PhoneBluetoothHandshakeDeadline phoneBluetoothHandshakeDeadline = new PhoneBluetoothHandshakeDeadline(profile, function0, 2, objArr == true ? 1 : 0);
        boolean z = false;
        while (!isClosed() && !cancelled.invoke().booleanValue()) {
            long jRemainingMillis = phoneBluetoothHandshakeDeadline.remainingMillis(handshakeStatus());
            if (lValueOf != null) {
                j = j2;
                jCoerceAtLeast = RangesKt.coerceAtLeast((lValueOf.longValue() - System.nanoTime()) / j, 0L);
            } else {
                j = j2;
                jCoerceAtLeast = Long.MAX_VALUE;
            }
            long jMin = Math.min(jRemainingMillis, jCoerceAtLeast);
            if (jMin == 0) {
                break;
            }
            if (this.channel.awaitReady(Math.min(jMin, 250L))) {
                z = true;
                break;
            }
            j2 = j;
        }
        emitTrace("IAP2 READY [" + this.traceContext + "] ready=" + z);
        return z;
    }

    public final boolean awaitReady(long timeoutMillis) {
        boolean ready = this.channel.awaitReady(timeoutMillis);
        emitTrace("IAP2 READY [" + this.traceContext + "] ready=" + ready);
        return ready;
    }

    public final boolean awaitOutboundAcknowledged(long timeoutMillis) {
        boolean acknowledged = this.channel.awaitOutboundAcknowledged(timeoutMillis);
        emitTrace("IAP2 DRAIN [" + this.traceContext + "] acknowledged=" + acknowledged);
        return acknowledged;
    }

    public static /* synthetic */ void send$default(Iap2Session iap2Session, Iap2Frame iap2Frame, long j, int i, Object obj) {
        if ((i & 2) != 0) {
            j = 5000;
        }
        iap2Session.send(iap2Frame, j);
    }

    public final void send(Iap2Frame frame, long timeoutMillis) {
        Intrinsics.checkNotNullParameter(frame, "frame");
        try {
            this.channel.send(frame, timeoutMillis);
            emitFrameTrace(Iap2TraceDirection.TX, frame);
        } catch (Throwable failure) {
            emitTrace(Iap2FrameFormatter.INSTANCE.formatFailure(Iap2TraceDirection.TX, this.traceContext, Integer.valueOf(frame.getMessageId()), failure));
            throw failure;
        }
    }

    public static /* synthetic */ void send$default(Iap2Session iap2Session, Iap2Endpoint iap2Endpoint, long j, Function1 function1, int i, Object obj) {
        if ((i & 2) != 0) {
            j = 5000;
        }
        iap2Session.send(iap2Endpoint, j, function1);
    }

    public final void send(Iap2Endpoint endpoint, long timeoutMillis, Function1<? super Iap2BodyBuilder, Unit> block) {
        Intrinsics.checkNotNullParameter(endpoint, "endpoint");
        Intrinsics.checkNotNullParameter(block, "block");
        Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
        Iap2BodyBuilder iap2BodyBuilderOf = Iap2BodyBuilder.INSTANCE.of(endpoint);
        block.invoke(iap2BodyBuilderOf);
        send(iap2BodyBuilderOf.build(), timeoutMillis);
    }

    public static /* synthetic */ void sendRaw$default(Iap2Session iap2Session, int i, long j, Function1 function1, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            j = 5000;
        }
        iap2Session.sendRaw(i, j, function1);
    }

    public final void sendRaw(int messageId, long timeoutMillis, Function1<? super Iap2BodyBuilder, Unit> block) {
        Intrinsics.checkNotNullParameter(block, "block");
        Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
        Iap2BodyBuilder iap2BodyBuilderOpaque = Iap2BodyBuilder.INSTANCE.opaque(messageId);
        block.invoke(iap2BodyBuilderOpaque);
        send(iap2BodyBuilderOpaque.build(), timeoutMillis);
    }

    public static /* synthetic */ boolean sendFileTransfer$default(Iap2Session iap2Session, byte[] bArr, long j, int i, Object obj) {
        if ((i & 2) != 0) {
            j = 5000;
        }
        return iap2Session.sendFileTransfer(bArr, j);
    }

    public final boolean sendFileTransfer(byte[] bytes, long timeoutMillis) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return this.channel.sendFileTransfer(bytes, timeoutMillis);
    }

    public final Iap2Frame recv(long timeoutMillis) {
        try {
            Iap2Frame it = this.channel.recv(timeoutMillis);
            if (it != null) {
                emitFrameTrace(Iap2TraceDirection.RX, it);
                return it;
            }
            return null;
        } catch (Throwable failure) {
            emitTrace(Iap2FrameFormatter.INSTANCE.formatFailure(Iap2TraceDirection.RX, this.traceContext, null, failure));
            throw failure;
        }
    }

    public final Iap2BodyReader reader(Iap2Frame frame) {
        Intrinsics.checkNotNullParameter(frame, "frame");
        return Iap2Messages.INSTANCE.reader(frame);
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        try {
            this.channel.close();
        } finally {
            emitTrace("IAP2 CLOSE [" + this.traceContext + "]");
        }
    }

    private final void emitFrameTrace(Iap2TraceDirection direction, Iap2Frame frame) {
        try {
            emitTrace(Iap2FrameFormatter.INSTANCE.format(direction, this.traceContext, frame));
        } catch (Exception failure) {
            emitTrace(Iap2FrameFormatter.INSTANCE.formatFailure(direction, this.traceContext, Integer.valueOf(frame.getMessageId()), failure));
        }
    }

    private final void emitTrace(String message) {
        try {
            this.onTrace.invoke(message);
        } catch (Exception e) {
        }
    }

    /* JADX INFO: compiled from: Iap2Session.kt */
    @Metadata(d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J,\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000bJ>\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\r0\u000bJ$\u0010\u0010\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000bJ$\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000bJ:\u0010\u0012\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b2\u0014\b\u0002\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\r0\u000bJD\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\u0016\u001a\u00020\f2\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b2\u0014\b\u0002\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\r0\u000bJD\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\u0016\u001a\u00020\f2\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b2\u0014\b\u0002\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\r0\u000bJD\u0010\u001a\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\u0016\u001a\u00020\f2\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b2\u0014\b\u0002\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\r0\u000bJ.\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u001c\u001a\u00020\u001d2\b\b\u0002\u0010\u0016\u001a\u00020\f2\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000bR\u000e\u0010\u0013\u001a\u00020\u0014X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u001e"}, d2 = {"Lcom/carplay/iap2/session/Iap2Session$Companion;", "", "<init>", "()V", "phoneBluetooth", "Lcom/carplay/iap2/session/Iap2Session;", "underlying", "Lcom/carplay/iap2/transport/BlockingDuplexByteStream;", "profile", "Lcom/carplay/iap2/transport/PhoneBluetoothProfile;", "onTrace", "Lkotlin/Function1;", "", "", "onFileTransferDatagram", "", "controller", "controllerTunnel", "commandTunnel", "DEFAULT_SEND_TIMEOUT_MILLIS", "", "open", "traceContext", "onArtwork", "Lcom/carplay/iap2/transport/Iap2ArtworkTransfer;", "openWireless", "openTunnel", "wrap", "channel", "Lcom/carplay/iap2/transport/Iap2CsmChannel;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2Session phoneBluetooth$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, PhoneBluetoothProfile phoneBluetoothProfile, Function1 function1, int i, Object obj) {
            if ((i & 4) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.session.Iap2Session$Companion$$ExternalSyntheticLambda9
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2Session.Companion.phoneBluetooth$lambda$0((String) obj2);
                    }
                };
            }
            return companion.phoneBluetooth(blockingDuplexByteStream, phoneBluetoothProfile, function1);
        }

        static final Unit phoneBluetooth$lambda$0(String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2Session phoneBluetooth(BlockingDuplexByteStream underlying, PhoneBluetoothProfile profile, Function1<? super String, Unit> onTrace) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(profile, "profile");
            Intrinsics.checkNotNullParameter(onTrace, "onTrace");
            return phoneBluetooth(underlying, profile, onTrace, new Function1() { // from class: com.carplay.iap2.session.Iap2Session$Companion$$ExternalSyntheticLambda14
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2Session.Companion.phoneBluetooth$lambda$1((byte[]) obj);
                }
            });
        }

        static final Unit phoneBluetooth$lambda$1(byte[] it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2Session phoneBluetooth(BlockingDuplexByteStream underlying, final PhoneBluetoothProfile profile, final Function1<? super String, Unit> onTrace, Function1<? super byte[], Unit> onFileTransferDatagram) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(profile, "profile");
            Intrinsics.checkNotNullParameter(onTrace, "onTrace");
            Intrinsics.checkNotNullParameter(onFileTransferDatagram, "onFileTransferDatagram");
            return new Iap2Session(Iap2CsmChannel.INSTANCE.openPhoneBluetooth(underlying, profile, new Function1() { // from class: com.carplay.iap2.session.Iap2Session$Companion$$ExternalSyntheticLambda3
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2Session.Companion.phoneBluetooth$lambda$2(onTrace, profile, (String) obj);
                }
            }, onFileTransferDatagram), "phone/" + profile.getLabel(), onTrace, null);
        }

        static final Unit phoneBluetooth$lambda$2(Function1 $onTrace, PhoneBluetoothProfile $profile, String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            $onTrace.invoke("IAP2 LINK [phone/" + $profile.getLabel() + "] " + it);
            return Unit.INSTANCE;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2Session controller$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, Function1 function1, int i, Object obj) {
            if ((i & 2) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.session.Iap2Session$Companion$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2Session.Companion.controller$lambda$3((String) obj2);
                    }
                };
            }
            return companion.controller(blockingDuplexByteStream, function1);
        }

        static final Unit controller$lambda$3(String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2Session controller(BlockingDuplexByteStream underlying, Function1<? super String, Unit> onTrace) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(onTrace, "onTrace");
            return new Iap2Session(Iap2CsmChannel.INSTANCE.openController(underlying), "phone", onTrace, null);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2Session controllerTunnel$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, Function1 function1, int i, Object obj) {
            if ((i & 2) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.session.Iap2Session$Companion$$ExternalSyntheticLambda8
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2Session.Companion.controllerTunnel$lambda$4((String) obj2);
                    }
                };
            }
            return companion.controllerTunnel(blockingDuplexByteStream, function1);
        }

        static final Unit controllerTunnel$lambda$4(String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        static final Unit controllerTunnel$lambda$5(Function1 $onTrace, String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            $onTrace.invoke("IAP2 LINK [phone-wifi] " + it);
            return Unit.INSTANCE;
        }

        public final Iap2Session controllerTunnel(BlockingDuplexByteStream underlying, final Function1<? super String, Unit> onTrace) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(onTrace, "onTrace");
            return new Iap2Session(Iap2CsmChannel.INSTANCE.openControllerTunnel(underlying, new Function1() { // from class: com.carplay.iap2.session.Iap2Session$Companion$$ExternalSyntheticLambda5
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2Session.Companion.controllerTunnel$lambda$5(onTrace, (String) obj);
                }
            }), "phone-wifi", onTrace, null);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2Session commandTunnel$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, Function1 function1, Function1 function2, int i, Object obj) {
            if ((i & 2) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.session.Iap2Session$Companion$$ExternalSyntheticLambda6
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2Session.Companion.commandTunnel$lambda$6((String) obj2);
                    }
                };
            }
            if ((i & 4) != 0) {
                function2 = new Function1() { // from class: com.carplay.iap2.session.Iap2Session$Companion$$ExternalSyntheticLambda7
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2Session.Companion.commandTunnel$lambda$7((byte[]) obj2);
                    }
                };
            }
            return companion.commandTunnel(blockingDuplexByteStream, function1, function2);
        }

        static final Unit commandTunnel$lambda$6(String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        static final Unit commandTunnel$lambda$7(byte[] it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2Session commandTunnel(BlockingDuplexByteStream underlying, final Function1<? super String, Unit> onTrace, Function1<? super byte[], Unit> onFileTransferDatagram) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(onTrace, "onTrace");
            Intrinsics.checkNotNullParameter(onFileTransferDatagram, "onFileTransferDatagram");
            return new Iap2Session(Iap2CsmChannel.INSTANCE.openCommandTunnel(underlying, new Function1() { // from class: com.carplay.iap2.session.Iap2Session$Companion$$ExternalSyntheticLambda15
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2Session.Companion.commandTunnel$lambda$8(onTrace, (String) obj);
                }
            }, onFileTransferDatagram), "phone-command", onTrace, null);
        }

        static final Unit commandTunnel$lambda$8(Function1 $onTrace, String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            $onTrace.invoke("IAP2 LINK [phone-command] " + it);
            return Unit.INSTANCE;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2Session open$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, String str, Function1 function1, Function1 function2, int i, Object obj) {
            if ((i & 2) != 0) {
                str = "wired";
            }
            if ((i & 4) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.session.Iap2Session$Companion$$ExternalSyntheticLambda1
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2Session.Companion.open$lambda$9((String) obj2);
                    }
                };
            }
            if ((i & 8) != 0) {
                function2 = new Function1() { // from class: com.carplay.iap2.session.Iap2Session$Companion$$ExternalSyntheticLambda2
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2Session.Companion.open$lambda$10((Iap2ArtworkTransfer) obj2);
                    }
                };
            }
            return companion.open(blockingDuplexByteStream, str, function1, function2);
        }

        static final Unit open$lambda$9(String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        static final Unit open$lambda$10(Iap2ArtworkTransfer it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2Session open(BlockingDuplexByteStream underlying, String traceContext, Function1<? super String, Unit> onTrace, Function1<? super Iap2ArtworkTransfer, Unit> onArtwork) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(traceContext, "traceContext");
            Intrinsics.checkNotNullParameter(onTrace, "onTrace");
            Intrinsics.checkNotNullParameter(onArtwork, "onArtwork");
            return new Iap2Session(Iap2CsmChannel.INSTANCE.open(underlying, onArtwork), traceContext, onTrace, null);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2Session openWireless$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, String str, Function1 function1, Function1 function2, int i, Object obj) {
            if ((i & 2) != 0) {
                str = "wireless";
            }
            if ((i & 4) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.session.Iap2Session$Companion$$ExternalSyntheticLambda10
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2Session.Companion.openWireless$lambda$11((String) obj2);
                    }
                };
            }
            if ((i & 8) != 0) {
                function2 = new Function1() { // from class: com.carplay.iap2.session.Iap2Session$Companion$$ExternalSyntheticLambda11
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2Session.Companion.openWireless$lambda$12((Iap2ArtworkTransfer) obj2);
                    }
                };
            }
            return companion.openWireless(blockingDuplexByteStream, str, function1, function2);
        }

        static final Unit openWireless$lambda$11(String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        static final Unit openWireless$lambda$12(Iap2ArtworkTransfer it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2Session openWireless(BlockingDuplexByteStream underlying, String traceContext, Function1<? super String, Unit> onTrace, Function1<? super Iap2ArtworkTransfer, Unit> onArtwork) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(traceContext, "traceContext");
            Intrinsics.checkNotNullParameter(onTrace, "onTrace");
            Intrinsics.checkNotNullParameter(onArtwork, "onArtwork");
            return new Iap2Session(Iap2CsmChannel.INSTANCE.openWireless(underlying, onArtwork), traceContext, onTrace, null);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2Session openTunnel$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, String str, Function1 function1, Function1 function2, int i, Object obj) {
            if ((i & 2) != 0) {
                str = "wireless-tunnel";
            }
            if ((i & 4) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.session.Iap2Session$Companion$$ExternalSyntheticLambda12
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2Session.Companion.openTunnel$lambda$13((String) obj2);
                    }
                };
            }
            if ((i & 8) != 0) {
                function2 = new Function1() { // from class: com.carplay.iap2.session.Iap2Session$Companion$$ExternalSyntheticLambda13
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2Session.Companion.openTunnel$lambda$14((Iap2ArtworkTransfer) obj2);
                    }
                };
            }
            return companion.openTunnel(blockingDuplexByteStream, str, function1, function2);
        }

        static final Unit openTunnel$lambda$13(String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        static final Unit openTunnel$lambda$14(Iap2ArtworkTransfer it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2Session openTunnel(BlockingDuplexByteStream underlying, String traceContext, Function1<? super String, Unit> onTrace, Function1<? super Iap2ArtworkTransfer, Unit> onArtwork) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(traceContext, "traceContext");
            Intrinsics.checkNotNullParameter(onTrace, "onTrace");
            Intrinsics.checkNotNullParameter(onArtwork, "onArtwork");
            return new Iap2Session(Iap2CsmChannel.INSTANCE.openTunnel(underlying, onArtwork), traceContext, onTrace, null);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2Session wrap$default(Companion companion, Iap2CsmChannel iap2CsmChannel, String str, Function1 function1, int i, Object obj) {
            if ((i & 2) != 0) {
                str = "iap2";
            }
            if ((i & 4) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.session.Iap2Session$Companion$$ExternalSyntheticLambda4
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2Session.Companion.wrap$lambda$15((String) obj2);
                    }
                };
            }
            return companion.wrap(iap2CsmChannel, str, function1);
        }

        static final Unit wrap$lambda$15(String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2Session wrap(Iap2CsmChannel channel, String traceContext, Function1<? super String, Unit> onTrace) {
            Intrinsics.checkNotNullParameter(channel, "channel");
            Intrinsics.checkNotNullParameter(traceContext, "traceContext");
            Intrinsics.checkNotNullParameter(onTrace, "onTrace");
            return new Iap2Session(channel, traceContext, onTrace, null);
        }
    }
}
