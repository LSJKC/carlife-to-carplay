package com.carplay.iap2.transport;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: Iap2LinkChannel.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u0098\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 ]2\u00060\u0001j\u0002`\u0002:\u0002\\]Bk\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f0\n\u0012\u0014\b\u0002\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\f0\n\u0012\u0014\b\u0002\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\f0\n\u0012\b\b\u0002\u0010\u0011\u001a\u00020\b¢\u0006\u0004\b\u0012\u0010\u0013J\u000e\u00102\u001a\u00020\b2\u0006\u00103\u001a\u00020*J\r\u0010\"\u001a\u0004\u0018\u00010\u001a¢\u0006\u0002\u00104J\u0006\u0010,\u001a\u00020-J\u000e\u00105\u001a\u00020\b2\u0006\u00103\u001a\u00020*J\u000e\u00106\u001a\u00020\b2\u0006\u00107\u001a\u00020\u000eJ\u0016\u00108\u001a\u00020\b2\u0006\u00109\u001a\u00020\u001a2\u0006\u00107\u001a\u00020\u000eJ\u001e\u0010:\u001a\u00020\b2\u0006\u00109\u001a\u00020\u001a2\u0006\u00107\u001a\u00020\u000e2\u0006\u00103\u001a\u00020*J\u001d\u0010;\u001a\u00020\b2\u0006\u00107\u001a\u00020\u000e2\u0006\u00103\u001a\u00020*H\u0000¢\u0006\u0002\b<J\u0010\u0010=\u001a\u0004\u0018\u00010\u000e2\u0006\u00103\u001a\u00020*J\b\u0010>\u001a\u00020\fH\u0016J\b\u0010?\u001a\u00020\fH\u0002J\u0010\u0010@\u001a\u00020\f2\u0006\u0010A\u001a\u00020BH\u0002J\u0010\u0010C\u001a\u00020\f2\u0006\u0010A\u001a\u00020BH\u0002J \u0010D\u001a\u00020\f2\u0006\u0010A\u001a\u00020B2\u0006\u0010E\u001a\u00020\u00102\u0006\u00107\u001a\u00020\u001aH\u0002J\u0010\u0010F\u001a\u00020\f2\u0006\u0010G\u001a\u00020\u0010H\u0002J\u0016\u0010H\u001a\u00020\u00102\f\u0010I\u001a\b\u0012\u0004\u0012\u00020K0JH\u0002J\u0010\u0010L\u001a\u00020\b2\u0006\u0010A\u001a\u00020BH\u0002J\u0012\u0010M\u001a\u00020\f2\b\u0010N\u001a\u0004\u0018\u00010'H\u0002J\u0012\u0010O\u001a\u00020\f2\b\u0010N\u001a\u0004\u0018\u00010'H\u0002J\u0012\u0010P\u001a\u00020\f2\b\u0010N\u001a\u0004\u0018\u00010'H\u0002J\b\u0010Q\u001a\u00020\fH\u0002J\b\u0010R\u001a\u00020\bH\u0002J\n\u0010S\u001a\u0004\u0018\u00010'H\u0002J\u0010\u0010T\u001a\u00020\b2\u0006\u00107\u001a\u00020\u001aH\u0002J\u0018\u0010U\u001a\u00020\b2\u0006\u00109\u001a\u00020\u001a2\u0006\u00107\u001a\u00020\u000eH\u0002J\u001e\u0010V\u001a\u00020\b2\u0006\u00103\u001a\u00020*2\f\u0010W\u001a\b\u0012\u0004\u0012\u00020\b0XH\u0002J\u001e\u0010Y\u001a\u0004\u0018\u00010'2\b\u0010Z\u001a\u0004\u0018\u00010'2\b\u0010[\u001a\u0004\u0018\u00010'H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f0\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\f0\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\f0\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0017X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001aX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u001aX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\"\u001a\u0004\u0018\u00010\u001aX\u0082\u000e¢\u0006\u0004\n\u0002\u0010#R\u000e\u0010$\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010&\u001a\u0004\u0018\u00010'X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020*X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020*X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020-X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\u001aX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u00020\u001aX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00100\u001a\u000201X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006^"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkChannel;", "Ljava/lang/AutoCloseable;", "Lkotlin/AutoCloseable;", "underlying", "Lcom/carplay/iap2/transport/BlockingDuplexByteStream;", "linkConfig", "Lcom/carplay/iap2/transport/Iap2LinkConfig;", "initiateNegotiation", "", "onArtwork", "Lkotlin/Function1;", "Lcom/carplay/iap2/transport/Iap2ArtworkTransfer;", "", "onFileTransferDatagram", "", "onDiagnostic", "", "packetDiagnostics", "<init>", "(Lcom/carplay/iap2/transport/BlockingDuplexByteStream;Lcom/carplay/iap2/transport/Iap2LinkConfig;ZLkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Z)V", "lock", "Ljava/lang/Object;", "commands", "Ljava/util/ArrayDeque;", "Lcom/carplay/iap2/transport/Iap2LinkChannel$Command;", "commandBytes", "", "controls", "controlBytes", "fileTransfers", "Lcom/carplay/iap2/transport/Iap2FileTransferReceiver;", "fileTransferSessionId", "ready", "outboundAcknowledged", "peerMaxControlPayloadBytes", "Ljava/lang/Integer;", "closing", "terminated", "terminalFailure", "", "streamCloseStarted", "sentBytes", "", "receivedBytes", "handshakeStatus", "Lcom/carplay/iap2/transport/Iap2HandshakeStatus;", "metadataRecords", "statusRecords", "worker", "Ljava/lang/Thread;", "awaitReady", "timeoutMillis", "()Ljava/lang/Integer;", "awaitOutboundAcknowledged", "sendControl", "bytes", "sendSession", "sessionId", "sendSessionAwaitCapacity", "sendControlAwaitCapacity", "sendControlAwaitCapacity$core_iap2", "recvControl", "close", "runPump", "drainCommands", "engine", "Lcom/carplay/iap2/transport/Iap2LinkEngine;", "flush", "publishHandshakeStatus", "activity", "reportMetadata", "message", "describeSessions", "sessions", "", "Lcom/carplay/iap2/transport/Iap2LinkEngine$SessionDescriptor;", "drainEvents", "finish", "failure", "finishLocked", "terminalLocked", "throwTerminalFailureLocked", "isClosing", "closeUnderlyingOnce", "hasCommandCapacityLocked", "enqueueCommandLocked", "waitFor", "readyPredicate", "Lkotlin/Function0;", "combineFailures", "first", "second", "Command", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2LinkChannel implements AutoCloseable {
    private static final long CLOSE_JOIN_MILLIS = 1000;
    private static final int CONTROL_COMMAND = -1;
    private static final int MAX_PENDING_COMMANDS = 64;
    private static final int MAX_PENDING_COMMAND_BYTES = 1048576;
    private static final int MAX_PENDING_CONTROLS = 64;
    private static final int MAX_PENDING_CONTROL_BYTES = 1048576;
    private static final long MAX_WAIT_MILLIS = 9223372036854L;
    private static final long MINIMUM_RECEIVE_POLL_MILLIS = 1;
    private static final long NANOS_PER_MILLISECOND = 1000000;
    private static final int RECEIVE_CHUNK_BYTES = 8192;
    private static final long RECEIVE_POLL_MILLIS = 100;
    private boolean closing;
    private int commandBytes;
    private final ArrayDeque<Command> commands;
    private int controlBytes;
    private final ArrayDeque<byte[]> controls;
    private final int fileTransferSessionId;
    private final Iap2FileTransferReceiver fileTransfers;
    private Iap2HandshakeStatus handshakeStatus;
    private final boolean initiateNegotiation;
    private final Iap2LinkConfig linkConfig;
    private final Object lock;
    private int metadataRecords;
    private final Function1<Iap2ArtworkTransfer, Unit> onArtwork;
    private final Function1<String, Unit> onDiagnostic;
    private final Function1<byte[], Unit> onFileTransferDatagram;
    private boolean outboundAcknowledged;
    private final boolean packetDiagnostics;
    private Integer peerMaxControlPayloadBytes;
    private boolean ready;
    private long receivedBytes;
    private long sentBytes;
    private int statusRecords;
    private boolean streamCloseStarted;
    private Throwable terminalFailure;
    private boolean terminated;
    private final BlockingDuplexByteStream underlying;
    private final Thread worker;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final Iap2LinkConfig WIRED_LINK_CONFIG = new Iap2LinkConfig(4, 0, 0, true, 2, 0, 0, 0, 0, null, null, false, false, null, 16358, null);
    private static final Iap2LinkConfig WIRELESS_LINK_CONFIG = new Iap2LinkConfig(4, 0, 0, false, 2, 0, 0, 0, 0, null, null, false, false, null, 16366, null);

    public /* synthetic */ Iap2LinkChannel(BlockingDuplexByteStream blockingDuplexByteStream, Iap2LinkConfig iap2LinkConfig, boolean z, Function1 function1, Function1 function2, Function1 function3, boolean z2, DefaultConstructorMarker defaultConstructorMarker) {
        this(blockingDuplexByteStream, iap2LinkConfig, z, function1, function2, function3, z2);
    }

    /* JADX WARN: Code duplicated, block: B:22:0x0089  */
    /* JADX WARN: Multi-variable type inference failed */
    private Iap2LinkChannel(BlockingDuplexByteStream underlying, Iap2LinkConfig linkConfig, boolean initiateNegotiation, Function1<? super Iap2ArtworkTransfer, Unit> function1, Function1<? super byte[], Unit> function2, Function1<? super String, Unit> function3, boolean packetDiagnostics) {
        int id;
        Iterable sessions;
        Object obj;
        this.underlying = underlying;
        this.linkConfig = linkConfig;
        this.initiateNegotiation = initiateNegotiation;
        this.onArtwork = function1;
        this.onFileTransferDatagram = function2;
        this.onDiagnostic = function3;
        this.packetDiagnostics = packetDiagnostics;
        this.lock = new Object();
        this.commands = new ArrayDeque<>();
        this.controls = new ArrayDeque<>();
        this.fileTransfers = new Iap2FileTransferReceiver(0, 1, null);
        Iap2LinkEngine.SynchronizationPayload synchronizationOverride = this.linkConfig.getSynchronizationOverride();
        if (synchronizationOverride == null || (sessions = synchronizationOverride.getSessions()) == null) {
            id = 12;
        } else {
            Iterable $this$firstOrNull$iv = sessions;
            Iterator it = $this$firstOrNull$iv.iterator();
            while (true) {
                if (!it.hasNext()) {
                    obj = null;
                    break;
                }
                Object element$iv = it.next();
                Iap2LinkEngine.SessionDescriptor it2 = (Iap2LinkEngine.SessionDescriptor) element$iv;
                if (it2.getKind() == 1 && it2.getVersion() == 2) {
                    obj = element$iv;
                    break;
                }
            }
            Iap2LinkEngine.SessionDescriptor sessionDescriptor = (Iap2LinkEngine.SessionDescriptor) obj;
            if (sessionDescriptor != null) {
                id = sessionDescriptor.getId();
            } else {
                id = 12;
            }
        }
        this.fileTransferSessionId = id;
        this.handshakeStatus = new Iap2HandshakeStatus(null, 0L, 0L, null, null, 31, null);
        Thread $this$worker_u24lambda_u243 = new Thread(new Runnable() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.runPump();
            }
        }, "xcertplay-iap2-link");
        $this$worker_u24lambda_u243.setDaemon(true);
        this.worker = $this$worker_u24lambda_u243;
    }

    /* synthetic */ Iap2LinkChannel(BlockingDuplexByteStream blockingDuplexByteStream, Iap2LinkConfig iap2LinkConfig, boolean z, Function1 function1, Function1 function2, Function1 function3, boolean z2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(blockingDuplexByteStream, iap2LinkConfig, z, function1, (i & 16) != 0 ? new Function1() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Iap2LinkChannel._init_$lambda$0((byte[]) obj);
            }
        } : function2, (i & 32) != 0 ? new Function1() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Iap2LinkChannel._init_$lambda$1((String) obj);
            }
        } : function3, (i & 64) != 0 ? false : z2);
    }

    static final Unit _init_$lambda$0(byte[] it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    static final Unit _init_$lambda$1(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    /* JADX INFO: compiled from: Iap2LinkChannel.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0015"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkChannel$Command;", "", "sessionId", "", "payload", "", "<init>", "(I[B)V", "getSessionId", "()I", "getPayload", "()[B", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class Command {
        private final byte[] payload;
        private final int sessionId;

        public static /* synthetic */ Command copy$default(Command command, int i, byte[] bArr, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                i = command.sessionId;
            }
            if ((i2 & 2) != 0) {
                bArr = command.payload;
            }
            return command.copy(i, bArr);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final int getSessionId() {
            return this.sessionId;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final byte[] getPayload() {
            return this.payload;
        }

        public final Command copy(int sessionId, byte[] payload) {
            Intrinsics.checkNotNullParameter(payload, "payload");
            return new Command(sessionId, payload);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Command)) {
                return false;
            }
            Command command = (Command) other;
            return this.sessionId == command.sessionId && Intrinsics.areEqual(this.payload, command.payload);
        }

        public int hashCode() {
            return (Integer.hashCode(this.sessionId) * 31) + Arrays.hashCode(this.payload);
        }

        public String toString() {
            return "Command(sessionId=" + this.sessionId + ", payload=" + Arrays.toString(this.payload) + ")";
        }

        public Command(int sessionId, byte[] payload) {
            Intrinsics.checkNotNullParameter(payload, "payload");
            this.sessionId = sessionId;
            this.payload = payload;
        }

        public final byte[] getPayload() {
            return this.payload;
        }

        public final int getSessionId() {
            return this.sessionId;
        }
    }

    public final boolean awaitReady(long timeoutMillis) {
        boolean z;
        if (!(timeoutMillis >= 0)) {
            throw new IllegalArgumentException("timeoutMillis must not be negative".toString());
        }
        synchronized (this.lock) {
            waitFor(timeoutMillis, new Function0() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$$ExternalSyntheticLambda9
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return Boolean.valueOf(Iap2LinkChannel.awaitReady$lambda$6$lambda$5(this.f$0));
                }
            });
            throwTerminalFailureLocked();
            z = this.ready && !this.terminated;
        }
        return z;
    }

    static final boolean awaitReady$lambda$6$lambda$5(Iap2LinkChannel this$0) {
        return (this$0.ready && !this$0.terminated) || this$0.terminated;
    }

    public final Integer peerMaxControlPayloadBytes() {
        Integer num;
        synchronized (this.lock) {
            num = this.peerMaxControlPayloadBytes;
        }
        return num;
    }

    public final Iap2HandshakeStatus handshakeStatus() {
        Iap2HandshakeStatus iap2HandshakeStatus;
        synchronized (this.lock) {
            iap2HandshakeStatus = this.handshakeStatus;
        }
        return iap2HandshakeStatus;
    }

    public final boolean awaitOutboundAcknowledged(long timeoutMillis) {
        boolean z;
        if (!(timeoutMillis >= 0)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        synchronized (this.lock) {
            waitFor(timeoutMillis, new Function0() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$$ExternalSyntheticLambda8
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return Boolean.valueOf(Iap2LinkChannel.awaitOutboundAcknowledged$lambda$10$lambda$9(this.f$0));
                }
            });
            throwTerminalFailureLocked();
            z = (this.closing || this.terminated || !this.outboundAcknowledged) ? false : true;
        }
        return z;
    }

    static final boolean awaitOutboundAcknowledged$lambda$10$lambda$9(Iap2LinkChannel this$0) {
        return this$0.closing || this$0.terminated || this$0.outboundAcknowledged;
    }

    public final boolean sendControl(byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        if (!(bytes.length <= 65525)) {
            throw new IllegalArgumentException("iAP2 control payload exceeds 65525 bytes".toString());
        }
        byte[] copy = Arrays.copyOf(bytes, bytes.length);
        Intrinsics.checkNotNullExpressionValue(copy, "copyOf(...)");
        synchronized (this.lock) {
            if (!this.terminated && !this.closing) {
                return enqueueCommandLocked(-1, copy);
            }
            return false;
        }
    }

    public final boolean sendSession(int sessionId, byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        if (!(sessionId >= 0 && sessionId < 256)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (!(bytes.length <= 65525)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        byte[] copy = Arrays.copyOf(bytes, bytes.length);
        Intrinsics.checkNotNullExpressionValue(copy, "copyOf(...)");
        synchronized (this.lock) {
            if (!this.terminated && !this.closing) {
                return enqueueCommandLocked(sessionId, copy);
            }
            return false;
        }
    }

    public final boolean sendSessionAwaitCapacity(int sessionId, byte[] bytes, long timeoutMillis) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        if (!(sessionId >= 0 && sessionId < 256)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (!(bytes.length <= 65525)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (!(timeoutMillis >= 0)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        final byte[] copy = Arrays.copyOf(bytes, bytes.length);
        Intrinsics.checkNotNullExpressionValue(copy, "copyOf(...)");
        synchronized (this.lock) {
            waitFor(timeoutMillis, new Function0() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return Boolean.valueOf(Iap2LinkChannel.sendSessionAwaitCapacity$lambda$15$lambda$14(this.f$0, copy));
                }
            });
            throwTerminalFailureLocked();
            if (!this.terminated && !this.closing) {
                return enqueueCommandLocked(sessionId, copy);
            }
            return false;
        }
    }

    static final boolean sendSessionAwaitCapacity$lambda$15$lambda$14(Iap2LinkChannel this$0, byte[] $copy) {
        return this$0.terminated || this$0.closing || this$0.hasCommandCapacityLocked($copy.length);
    }

    public final boolean sendControlAwaitCapacity$core_iap2(byte[] bytes, long timeoutMillis) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        if (!(bytes.length <= 65525)) {
            throw new IllegalArgumentException("iAP2 control payload exceeds 65525 bytes".toString());
        }
        if (!(timeoutMillis >= 0)) {
            throw new IllegalArgumentException("timeoutMillis must not be negative".toString());
        }
        final byte[] copy = Arrays.copyOf(bytes, bytes.length);
        Intrinsics.checkNotNullExpressionValue(copy, "copyOf(...)");
        synchronized (this.lock) {
            waitFor(timeoutMillis, new Function0() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$$ExternalSyntheticLambda2
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return Boolean.valueOf(Iap2LinkChannel.sendControlAwaitCapacity$lambda$19$lambda$18(this.f$0, copy));
                }
            });
            throwTerminalFailureLocked();
            if (!this.terminated && !this.closing) {
                return enqueueCommandLocked(-1, copy);
            }
            return false;
        }
    }

    static final boolean sendControlAwaitCapacity$lambda$19$lambda$18(Iap2LinkChannel this$0, byte[] $copy) {
        return this$0.terminated || this$0.closing || this$0.hasCommandCapacityLocked($copy.length);
    }

    public final byte[] recvControl(long timeoutMillis) {
        if (!(timeoutMillis >= 0)) {
            throw new IllegalArgumentException("timeoutMillis must not be negative".toString());
        }
        synchronized (this.lock) {
            waitFor(timeoutMillis, new Function0() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return Boolean.valueOf(Iap2LinkChannel.recvControl$lambda$22$lambda$21(this.f$0));
                }
            });
            throwTerminalFailureLocked();
            if (this.controls.isEmpty()) {
                return null;
            }
            byte[] bytes = this.controls.removeFirst();
            this.controlBytes -= bytes.length;
            return bytes;
        }
    }

    static final boolean recvControl$lambda$22$lambda$21(Iap2LinkChannel this$0) {
        return !this$0.controls.isEmpty() || this$0.terminated;
    }

    @Override // java.lang.AutoCloseable
    public void close() throws Throwable {
        Throwable thCombineFailures;
        synchronized (this.lock) {
            if (!this.closing) {
                this.closing = true;
                terminalLocked(null);
            }
            Unit unit = Unit.INSTANCE;
        }
        Throwable thCloseUnderlyingOnce = closeUnderlyingOnce();
        if (thCloseUnderlyingOnce != null) {
            synchronized (this.lock) {
                this.terminalFailure = combineFailures(this.terminalFailure, thCloseUnderlyingOnce);
                this.lock.notifyAll();
                Unit unit2 = Unit.INSTANCE;
            }
        }
        try {
            this.worker.join(1000L);
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            thCloseUnderlyingOnce = combineFailures(thCloseUnderlyingOnce, interrupted);
        }
        if (this.worker.isAlive()) {
            thCloseUnderlyingOnce = combineFailures(thCloseUnderlyingOnce, new IOException("iAP2 link worker did not stop after close"));
        }
        synchronized (this.lock) {
            thCombineFailures = combineFailures(thCloseUnderlyingOnce, this.terminalFailure);
            Unit unit3 = Unit.INSTANCE;
        }
        if (thCombineFailures != null) {
            throw thCombineFailures;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void runPump() {
        long jCoerceAtLeast;
        Iap2LinkEngine engine = new Iap2LinkEngine(this.linkConfig, this.packetDiagnostics ? new Function1() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Iap2LinkChannel.runPump$lambda$26(this.f$0, (Iap2PacketMetadata) obj);
            }
        } : null);
        try {
            engine.start(this.initiateNegotiation, INSTANCE.nowMillis());
            Iap2LinkEngine.SynchronizationPayload local = this.linkConfig.getSynchronizationOverride();
            if (local != null) {
                reportMetadata("LOCAL_SYN sessions=" + describeSessions(local.getSessions()) + " window=" + local.getMaxOutgoing() + " maxLength=" + local.getMaxLength() + " rto=" + local.getRetransmissionTimeoutMillis() + " ack=" + local.getAcknowledgementTimeoutMillis());
            }
            publishHandshakeStatus(engine, "START", 0);
            while (!isClosing()) {
                drainCommands(engine);
                engine.advanceTime(INSTANCE.nowMillis());
                flush(engine);
                if (!drainEvents(engine) || isClosing()) {
                    return;
                }
                Long next = engine.nextDeadlineMillis();
                if (next == null) {
                    jCoerceAtLeast = 100;
                } else {
                    long it = next.longValue();
                    jCoerceAtLeast = RangesKt.coerceAtLeast(it - INSTANCE.nowMillis(), 0L);
                }
                long waitMillis = RangesKt.coerceAtLeast(Math.min(RECEIVE_POLL_MILLIS, jCoerceAtLeast), MINIMUM_RECEIVE_POLL_MILLIS);
                byte[] received = this.underlying.recv(8192, waitMillis);
                if (isClosing()) {
                    return;
                }
                if (received != null) {
                    if (received.length == 0) {
                        engine.feedEof();
                    } else {
                        this.receivedBytes += (long) received.length;
                        try {
                            engine.feed(received, INSTANCE.nowMillis());
                            publishHandshakeStatus(engine, "RX", received.length);
                        } catch (Throwable th) {
                            publishHandshakeStatus(engine, "RX", received.length);
                            throw th;
                        }
                    }
                }
            }
            finish(null);
        } catch (Throwable failure) {
            if (failure instanceof Error) {
                finish(failure);
                throw failure;
            }
            if (isClosing()) {
                finish(null);
            } else {
                finish(failure);
            }
        }
    }

    static final Unit runPump$lambda$26(Iap2LinkChannel this$0, Iap2PacketMetadata metadata) {
        Intrinsics.checkNotNullParameter(metadata, "metadata");
        this$0.reportMetadata("PACKET " + metadata.diagnostic());
        return Unit.INSTANCE;
    }

    private final void drainCommands(Iap2LinkEngine engine) {
        Command command;
        while (engine.getWritable()) {
            synchronized (this.lock) {
                if (this.commands.isEmpty()) {
                    command = null;
                } else {
                    Command commandRemoveFirst = this.commands.removeFirst();
                    Command it = commandRemoveFirst;
                    this.commandBytes -= it.getPayload().length;
                    this.lock.notifyAll();
                    command = commandRemoveFirst;
                }
            }
            if (command == null) {
                return;
            }
            if (command.getSessionId() == -1) {
                engine.sendControl(command.getPayload(), INSTANCE.nowMillis());
            } else {
                engine.sendSession(command.getSessionId(), command.getPayload(), INSTANCE.nowMillis());
            }
            if (isClosing()) {
                return;
            }
        }
    }

    private final void flush(Iap2LinkEngine engine) {
        byte[] bytes = engine.takeOutput();
        if (!(bytes.length == 0)) {
            this.underlying.send(bytes);
            this.sentBytes += (long) bytes.length;
            publishHandshakeStatus(engine, "TX", bytes.length);
        }
    }

    private final void publishHandshakeStatus(Iap2LinkEngine engine, String activity, int bytes) {
        Iap2HandshakeStatus previous;
        String str;
        Iap2HandshakeStatus status = new Iap2HandshakeStatus(engine.getState(), this.sentBytes, this.receivedBytes, engine.validatedPeerSynchronization(), engine.negotiatedControlSessionId());
        synchronized (this.lock) {
            previous = this.handshakeStatus;
            this.handshakeStatus = status;
            this.outboundAcknowledged = this.commands.isEmpty() && engine.outboundAcknowledged();
            this.lock.notifyAll();
        }
        if (status.getState() == Iap2LinkEngine.State.NORMAL && previous.getState() == status.getState() && (!this.packetDiagnostics || this.statusRecords >= 64)) {
            return;
        }
        this.statusRecords++;
        Iap2LinkEngine.SynchronizationPayload peer = status.getPeerSynchronization();
        if (peer == null) {
            str = "peerSyn=false";
        } else {
            str = "peerSyn=true control=" + status.getControlSessionId() + " peerWindow=" + peer.getMaxOutgoing() + " peerMaxLength=" + peer.getMaxLength() + " peerRto=" + peer.getRetransmissionTimeoutMillis() + " peerAck=" + peer.getAcknowledgementTimeoutMillis() + " peerSessions=" + describeSessions(peer.getSessions());
        }
        String detail = str;
        try {
            this.onDiagnostic.invoke(activity + " bytes=" + bytes + " txComplete=" + status.getSentBytes() + " rx=" + status.getReceivedBytes() + " state=" + status.getState() + " " + detail);
        } catch (Exception e) {
        }
    }

    private final void reportMetadata(String message) {
        if (!this.packetDiagnostics || this.metadataRecords >= 120) {
            return;
        }
        this.metadataRecords++;
        try {
            this.onDiagnostic.invoke(message);
        } catch (Exception e) {
        }
    }

    private final String describeSessions(List<Iap2LinkEngine.SessionDescriptor> sessions) {
        String str;
        String strJoinToString$default = CollectionsKt.joinToString$default(CollectionsKt.take(sessions, 8), ",", null, null, 0, null, new Function1() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Iap2LinkChannel.describeSessions$lambda$33((Iap2LinkEngine.SessionDescriptor) obj);
            }
        }, 30, null);
        if (sessions.size() > 8) {
            str = ",… total=" + sessions.size();
        } else {
            str = "";
        }
        return strJoinToString$default + str;
    }

    static final CharSequence describeSessions$lambda$33(Iap2LinkEngine.SessionDescriptor it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getId() + "/" + it.getKind() + "/" + it.getVersion();
    }

    private final boolean drainEvents(Iap2LinkEngine engine) {
        while (true) {
            Iap2LinkEngine.Event event = engine.pollEvent();
            boolean overflow = true;
            if (event == null) {
                return true;
            }
            if (event instanceof Iap2LinkEngine.Event.Writable) {
                synchronized (this.lock) {
                    if (((Iap2LinkEngine.Event.Writable) event).getValue()) {
                        this.peerMaxControlPayloadBytes = Integer.valueOf(engine.getPeerSynchronization().getMaxLength() - 10);
                    }
                    if (!((Iap2LinkEngine.Event.Writable) event).getValue() || this.terminated) {
                        overflow = false;
                    }
                    this.ready = overflow;
                    this.lock.notifyAll();
                    Unit unit = Unit.INSTANCE;
                }
            } else if (event instanceof Iap2LinkEngine.Event.Control) {
                synchronized (this.lock) {
                    if (this.controls.size() < 64 && ((Iap2LinkEngine.Event.Control) event).getBytes().length <= 1048576 - this.controlBytes) {
                        this.controls.add(((Iap2LinkEngine.Event.Control) event).getBytes());
                        this.controlBytes += ((Iap2LinkEngine.Event.Control) event).getBytes().length;
                        this.lock.notifyAll();
                        overflow = false;
                    }
                }
                if (overflow) {
                    finish(new IOException("iAP2 received-control queue limit exceeded"));
                    return false;
                }
            } else if (event instanceof Iap2LinkEngine.Event.Session) {
                reportMetadata("ROUTING session=" + ((Iap2LinkEngine.Event.Session) event).getSessionId() + " payloadBytes=" + ((Iap2LinkEngine.Event.Session) event).getBytes().length + " result=NON_CONTROL");
                if (((Iap2LinkEngine.Event.Session) event).getSessionId() == this.fileTransferSessionId) {
                    try {
                        Result.Companion companion = Result.INSTANCE;
                        Iap2LinkChannel $this$drainEvents_u24lambda_u2436 = this;
                        Function1<byte[], Unit> function1 = $this$drainEvents_u24lambda_u2436.onFileTransferDatagram;
                        byte[] bytes = ((Iap2LinkEngine.Event.Session) event).getBytes();
                        byte[] bArrCopyOf = Arrays.copyOf(bytes, bytes.length);
                        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
                        function1.invoke(bArrCopyOf);
                        Result.m159constructorimpl(Unit.INSTANCE);
                    } catch (Throwable th) {
                        Result.Companion companion2 = Result.INSTANCE;
                        Result.m159constructorimpl(ResultKt.createFailure(th));
                    }
                    Iap2FileTransferReceiver.Outcome outcome = this.fileTransfers.accept(((Iap2LinkEngine.Event.Session) event).getBytes());
                    Iterable $this$forEach$iv = outcome.getReplies();
                    for (Object element$iv : $this$forEach$iv) {
                        byte[] reply = (byte[]) element$iv;
                        engine.sendSession(this.fileTransferSessionId, reply, INSTANCE.nowMillis());
                    }
                    Iap2ArtworkTransfer completed = outcome.getCompleted();
                    if (completed != null) {
                        try {
                            this.onArtwork.invoke(completed);
                        } catch (Exception e) {
                        }
                    }
                }
            } else {
                if (!(event instanceof Iap2LinkEngine.Event.Dead)) {
                    throw new NoWhenBranchMatchedException();
                }
                String reason = ((Iap2LinkEngine.Event.Dead) event).getReason();
                if (reason == null) {
                    reason = "iAP2 link ended";
                }
                byte[] it = ((Iap2LinkEngine.Event.Dead) event).getRestartInput();
                finish(it != null ? new Iap2PeerDetectionRestart(reason, it) : new IOException(reason));
                return false;
            }
        }
    }

    private final void finish(Throwable failure) {
        synchronized (this.lock) {
            finishLocked(failure);
            Unit unit = Unit.INSTANCE;
        }
        Throwable closeFailure = closeUnderlyingOnce();
        if (closeFailure != null) {
            synchronized (this.lock) {
                this.terminalFailure = combineFailures(this.terminalFailure, closeFailure);
                this.lock.notifyAll();
                Unit unit2 = Unit.INSTANCE;
            }
        }
    }

    private final void finishLocked(Throwable failure) {
        if (!this.terminated) {
            terminalLocked(failure);
        } else if (failure != null) {
            this.terminalFailure = combineFailures(this.terminalFailure, failure);
            this.lock.notifyAll();
        }
    }

    private final void terminalLocked(Throwable failure) {
        this.terminated = true;
        this.ready = false;
        this.commands.clear();
        this.commandBytes = 0;
        this.controls.clear();
        this.controlBytes = 0;
        this.fileTransfers.clear();
        this.terminalFailure = combineFailures(this.terminalFailure, failure);
        this.lock.notifyAll();
    }

    private final void throwTerminalFailureLocked() throws Throwable {
        Throwable it = this.terminalFailure;
        if (it != null) {
            throw it;
        }
    }

    private final boolean isClosing() {
        boolean z;
        synchronized (this.lock) {
            z = this.closing || this.terminated;
        }
        return z;
    }

    private final Throwable closeUnderlyingOnce() {
        synchronized (this.lock) {
            if (this.streamCloseStarted) {
                return null;
            }
            this.streamCloseStarted = true;
            Unit unit = Unit.INSTANCE;
            try {
                this.underlying.close();
                return null;
            } catch (Throwable failure) {
                return failure;
            }
        }
    }

    private final boolean hasCommandCapacityLocked(int bytes) {
        return this.commands.size() < 64 && bytes <= 1048576 - this.commandBytes;
    }

    private final boolean enqueueCommandLocked(int sessionId, byte[] bytes) {
        if (!hasCommandCapacityLocked(bytes.length)) {
            return false;
        }
        this.outboundAcknowledged = false;
        this.commands.add(new Command(sessionId, bytes));
        this.commandBytes += bytes.length;
        this.lock.notifyAll();
        return true;
    }

    private final boolean waitFor(long timeoutMillis, Function0<Boolean> readyPredicate) {
        boolean z;
        boolean z2 = true;
        if (readyPredicate.invoke().booleanValue()) {
            return true;
        }
        boolean z3 = false;
        if (timeoutMillis == 0) {
            return false;
        }
        long j = 1000000;
        long budgetNanos = RangesKt.coerceAtMost(timeoutMillis, MAX_WAIT_MILLIS) * 1000000;
        long startedNanos = System.nanoTime();
        while (!readyPredicate.invoke().booleanValue()) {
            long elapsedNanos = System.nanoTime() - startedNanos;
            if (elapsedNanos >= budgetNanos) {
                return z3;
            }
            long remainingNanos = budgetNanos - elapsedNanos;
            try {
                boolean z4 = z2;
                z = z3;
                try {
                    long j2 = j;
                    this.lock.wait(remainingNanos / j, (int) (remainingNanos % j2));
                    z2 = z4;
                    z3 = z;
                    j = j2;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return z;
                }
            } catch (InterruptedException e2) {
                z = z3;
            }
        }
        return z2;
    }

    private final Throwable combineFailures(Throwable first, Throwable second) {
        if (first == null) {
            return second;
        }
        if (second != null && second != first) {
            ExceptionsKt.addSuppressed(first, second);
        }
        return first;
    }

    /* JADX INFO: compiled from: Iap2LinkChannel.kt */
    @Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J:\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0014\b\u0002\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u001b0\u00192\u0014\b\u0002\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001b0\u0019J:\u0010\u001e\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0014\b\u0002\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u001b0\u00192\u0014\b\u0002\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001b0\u0019J\u000e\u0010\u001f\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017J,\u0010 \u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010!\u001a\u00020\"2\u0014\b\u0002\u0010#\u001a\u000e\u0012\u0004\u0012\u00020$\u0012\u0004\u0012\u00020\u001b0\u0019J>\u0010 \u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010!\u001a\u00020\"2\u0012\u0010#\u001a\u000e\u0012\u0004\u0012\u00020$\u0012\u0004\u0012\u00020\u001b0\u00192\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001b0\u0019J$\u0010%\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0014\b\u0002\u0010#\u001a\u000e\u0012\u0004\u0012\u00020$\u0012\u0004\u0012\u00020\u001b0\u0019J:\u0010&\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0014\b\u0002\u0010#\u001a\u000e\u0012\u0004\u0012\u00020$\u0012\u0004\u0012\u00020\u001b0\u00192\u0014\b\u0002\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001b0\u0019J$\u0010'\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0014\b\u0002\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u001b0\u0019J\b\u0010(\u001a\u00020\bH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0012X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006)"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkChannel$Companion;", "", "<init>", "()V", "CONTROL_COMMAND", "", "RECEIVE_CHUNK_BYTES", "RECEIVE_POLL_MILLIS", "", "MINIMUM_RECEIVE_POLL_MILLIS", "CLOSE_JOIN_MILLIS", "NANOS_PER_MILLISECOND", "MAX_WAIT_MILLIS", "MAX_PENDING_COMMANDS", "MAX_PENDING_COMMAND_BYTES", "MAX_PENDING_CONTROLS", "MAX_PENDING_CONTROL_BYTES", "WIRED_LINK_CONFIG", "Lcom/carplay/iap2/transport/Iap2LinkConfig;", "WIRELESS_LINK_CONFIG", "open", "Lcom/carplay/iap2/transport/Iap2LinkChannel;", "underlying", "Lcom/carplay/iap2/transport/BlockingDuplexByteStream;", "onArtwork", "Lkotlin/Function1;", "Lcom/carplay/iap2/transport/Iap2ArtworkTransfer;", "", "onFileTransferDatagram", "", "openWireless", "openController", "openPhoneBluetooth", "profile", "Lcom/carplay/iap2/transport/PhoneBluetoothProfile;", "onDiagnostic", "", "openControllerTunnel", "openCommandTunnel", "openTunnel", "nowMillis", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2LinkChannel open$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, Function1 function1, Function1 function2, int i, Object obj) {
            if ((i & 2) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$Companion$$ExternalSyntheticLambda4
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2LinkChannel.Companion.open$lambda$0((Iap2ArtworkTransfer) obj2);
                    }
                };
            }
            if ((i & 4) != 0) {
                function2 = new Function1() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$Companion$$ExternalSyntheticLambda5
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2LinkChannel.Companion.open$lambda$1((byte[]) obj2);
                    }
                };
            }
            return companion.open(blockingDuplexByteStream, function1, function2);
        }

        static final Unit open$lambda$0(Iap2ArtworkTransfer it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        static final Unit open$lambda$1(byte[] it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2LinkChannel open(BlockingDuplexByteStream underlying, Function1<? super Iap2ArtworkTransfer, Unit> onArtwork, Function1<? super byte[], Unit> onFileTransferDatagram) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(onArtwork, "onArtwork");
            Intrinsics.checkNotNullParameter(onFileTransferDatagram, "onFileTransferDatagram");
            Iap2LinkChannel it = new Iap2LinkChannel(underlying, Iap2LinkChannel.WIRED_LINK_CONFIG, true, onArtwork, onFileTransferDatagram, null, false, 96, null);
            it.worker.start();
            return it;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2LinkChannel openWireless$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, Function1 function1, Function1 function2, int i, Object obj) {
            if ((i & 2) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$Companion$$ExternalSyntheticLambda2
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2LinkChannel.Companion.openWireless$lambda$3((Iap2ArtworkTransfer) obj2);
                    }
                };
            }
            if ((i & 4) != 0) {
                function2 = new Function1() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$Companion$$ExternalSyntheticLambda3
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2LinkChannel.Companion.openWireless$lambda$4((byte[]) obj2);
                    }
                };
            }
            return companion.openWireless(blockingDuplexByteStream, function1, function2);
        }

        static final Unit openWireless$lambda$3(Iap2ArtworkTransfer it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        static final Unit openWireless$lambda$4(byte[] it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2LinkChannel openWireless(BlockingDuplexByteStream underlying, Function1<? super Iap2ArtworkTransfer, Unit> onArtwork, Function1<? super byte[], Unit> onFileTransferDatagram) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(onArtwork, "onArtwork");
            Intrinsics.checkNotNullParameter(onFileTransferDatagram, "onFileTransferDatagram");
            Iap2LinkChannel it = new Iap2LinkChannel(underlying, Iap2LinkChannel.WIRELESS_LINK_CONFIG, false, onArtwork, onFileTransferDatagram, null, false, 96, null);
            it.worker.start();
            return it;
        }

        static final Unit openController$lambda$6(Iap2ArtworkTransfer it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2LinkChannel openController(BlockingDuplexByteStream underlying) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Iap2LinkChannel it = new Iap2LinkChannel(underlying, Iap2LinkChannel.WIRELESS_LINK_CONFIG, true, new Function1() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$Companion$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2LinkChannel.Companion.openController$lambda$6((Iap2ArtworkTransfer) obj);
                }
            }, null, null, false, 112, null);
            it.worker.start();
            return it;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2LinkChannel openPhoneBluetooth$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, PhoneBluetoothProfile phoneBluetoothProfile, Function1 function1, int i, Object obj) {
            if ((i & 4) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$Companion$$ExternalSyntheticLambda8
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2LinkChannel.Companion.openPhoneBluetooth$lambda$8((String) obj2);
                    }
                };
            }
            return companion.openPhoneBluetooth(blockingDuplexByteStream, phoneBluetoothProfile, function1);
        }

        static final Unit openPhoneBluetooth$lambda$8(String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2LinkChannel openPhoneBluetooth(BlockingDuplexByteStream underlying, PhoneBluetoothProfile profile, Function1<? super String, Unit> onDiagnostic) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(profile, "profile");
            Intrinsics.checkNotNullParameter(onDiagnostic, "onDiagnostic");
            return openPhoneBluetooth(underlying, profile, onDiagnostic, new Function1() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$Companion$$ExternalSyntheticLambda12
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2LinkChannel.Companion.openPhoneBluetooth$lambda$9((byte[]) obj);
                }
            });
        }

        static final Unit openPhoneBluetooth$lambda$9(byte[] it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2LinkChannel openPhoneBluetooth(BlockingDuplexByteStream underlying, PhoneBluetoothProfile profile, Function1<? super String, Unit> onDiagnostic, Function1<? super byte[], Unit> onFileTransferDatagram) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(profile, "profile");
            Intrinsics.checkNotNullParameter(onDiagnostic, "onDiagnostic");
            Intrinsics.checkNotNullParameter(onFileTransferDatagram, "onFileTransferDatagram");
            Iap2LinkChannel it = new Iap2LinkChannel(underlying, profile.linkConfig$core_iap2(), profile == PhoneBluetoothProfile.LEGACY_CONTROLLER, new Function1() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$Companion$$ExternalSyntheticLambda11
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2LinkChannel.Companion.openPhoneBluetooth$lambda$10((Iap2ArtworkTransfer) obj);
                }
            }, onFileTransferDatagram, onDiagnostic, true, null);
            it.worker.start();
            return it;
        }

        static final Unit openPhoneBluetooth$lambda$10(Iap2ArtworkTransfer it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2LinkChannel openControllerTunnel$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, Function1 function1, int i, Object obj) {
            if ((i & 2) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$Companion$$ExternalSyntheticLambda6
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2LinkChannel.Companion.openControllerTunnel$lambda$12((String) obj2);
                    }
                };
            }
            return companion.openControllerTunnel(blockingDuplexByteStream, function1);
        }

        static final Unit openControllerTunnel$lambda$12(String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2LinkChannel openControllerTunnel(BlockingDuplexByteStream underlying, Function1<? super String, Unit> onDiagnostic) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(onDiagnostic, "onDiagnostic");
            Iap2LinkChannel it = new Iap2LinkChannel(underlying, Iap2LinkChannel.WIRED_LINK_CONFIG, false, new Function1() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$Companion$$ExternalSyntheticLambda13
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2LinkChannel.Companion.openControllerTunnel$lambda$13((Iap2ArtworkTransfer) obj);
                }
            }, null, onDiagnostic, true, 16, null);
            it.worker.start();
            return it;
        }

        static final Unit openControllerTunnel$lambda$13(Iap2ArtworkTransfer it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2LinkChannel openCommandTunnel$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, Function1 function1, Function1 function2, int i, Object obj) {
            if ((i & 2) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$Companion$$ExternalSyntheticLambda9
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2LinkChannel.Companion.openCommandTunnel$lambda$15((String) obj2);
                    }
                };
            }
            if ((i & 4) != 0) {
                function2 = new Function1() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$Companion$$ExternalSyntheticLambda10
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2LinkChannel.Companion.openCommandTunnel$lambda$16((byte[]) obj2);
                    }
                };
            }
            return companion.openCommandTunnel(blockingDuplexByteStream, function1, function2);
        }

        static final Unit openCommandTunnel$lambda$15(String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        static final Unit openCommandTunnel$lambda$16(byte[] it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2LinkChannel openCommandTunnel(BlockingDuplexByteStream underlying, Function1<? super String, Unit> onDiagnostic, Function1<? super byte[], Unit> onFileTransferDatagram) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(onDiagnostic, "onDiagnostic");
            Intrinsics.checkNotNullParameter(onFileTransferDatagram, "onFileTransferDatagram");
            Iap2LinkChannel it = new Iap2LinkChannel(underlying, PhoneBluetoothProfile.COMMAND_TUNNEL.linkConfig$core_iap2(), false, new Function1() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$Companion$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2LinkChannel.Companion.openCommandTunnel$lambda$17((Iap2ArtworkTransfer) obj);
                }
            }, onFileTransferDatagram, onDiagnostic, true, null);
            it.worker.start();
            return it;
        }

        static final Unit openCommandTunnel$lambda$17(Iap2ArtworkTransfer it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Iap2LinkChannel openTunnel$default(Companion companion, BlockingDuplexByteStream blockingDuplexByteStream, Function1 function1, int i, Object obj) {
            if ((i & 2) != 0) {
                function1 = new Function1() { // from class: com.carplay.iap2.transport.Iap2LinkChannel$Companion$$ExternalSyntheticLambda7
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return Iap2LinkChannel.Companion.openTunnel$lambda$19((Iap2ArtworkTransfer) obj2);
                    }
                };
            }
            return companion.openTunnel(blockingDuplexByteStream, function1);
        }

        static final Unit openTunnel$lambda$19(Iap2ArtworkTransfer it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        public final Iap2LinkChannel openTunnel(BlockingDuplexByteStream underlying, Function1<? super Iap2ArtworkTransfer, Unit> onArtwork) {
            Intrinsics.checkNotNullParameter(underlying, "underlying");
            Intrinsics.checkNotNullParameter(onArtwork, "onArtwork");
            Iap2LinkChannel it = new Iap2LinkChannel(underlying, Iap2LinkChannel.WIRED_LINK_CONFIG, true, onArtwork, null, null, false, 112, null);
            it.worker.start();
            return it;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final long nowMillis() {
            return System.nanoTime() / 1000000;
        }
    }
}
