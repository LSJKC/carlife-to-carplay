package com.carplay.iap2.transport;

import androidx.core.app.NotificationCompat;
import java.io.ByteArrayOutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.comparisons.ComparisonsKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: Iap2LinkEngine.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u008a\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\u0012\n\u0002\b$\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0015\u0018\u0000 u2\u00020\u0001:\bnopqrstuB)\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0016\b\u0002\u0010\u0004\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0005¢\u0006\u0004\b\b\u0010\tJ\u0006\u0010\n\u001a\u00020\u000bJ\u0006\u0010\u001b\u001a\u00020\u000fJ\u0006\u00101\u001a\u00020\u000fJ\u0006\u0010\f\u001a\u00020\rJ\b\u00102\u001a\u0004\u0018\u00010\rJ\r\u00103\u001a\u0004\u0018\u00010\u0012¢\u0006\u0002\u00104J\u0006\u00105\u001a\u000206J\b\u00107\u001a\u0004\u0018\u00010$J\r\u00108\u001a\u0004\u0018\u00010,¢\u0006\u0002\u00109J\u0016\u0010:\u001a\u00020\u00072\u0006\u0010;\u001a\u00020\u000f2\u0006\u0010<\u001a\u00020,J\u0016\u0010=\u001a\u00020\u00072\u0006\u0010>\u001a\u0002062\u0006\u0010<\u001a\u00020,J\u0006\u0010?\u001a\u00020\u0007J\u000e\u0010@\u001a\u00020\u00072\u0006\u0010<\u001a\u00020,J\u0016\u0010A\u001a\u00020\u00072\u0006\u0010>\u001a\u0002062\u0006\u0010<\u001a\u00020,J\u001e\u0010B\u001a\u00020\u00072\u0006\u0010C\u001a\u00020\u00122\u0006\u0010>\u001a\u0002062\u0006\u0010<\u001a\u00020,J\u0010\u0010D\u001a\u00020\u000f2\u0006\u0010<\u001a\u00020,H\u0002J\u0010\u0010E\u001a\u00020\u000f2\u0006\u0010<\u001a\u00020,H\u0002J\u001a\u0010F\u001a\u00020\u00072\u0006\u0010<\u001a\u00020,2\b\b\u0002\u0010G\u001a\u00020\u000fH\u0002J\"\u0010H\u001a\u00020\u00072\u0006\u0010I\u001a\u00020*2\b\u0010J\u001a\u0004\u0018\u0001062\u0006\u0010<\u001a\u00020,H\u0002J(\u0010K\u001a\u00020\u00072\u0006\u0010L\u001a\u00020\r2\u0006\u0010M\u001a\u00020\u00122\u0006\u0010<\u001a\u00020,2\u0006\u0010N\u001a\u00020\u000fH\u0002J \u0010O\u001a\u00020\u00072\u0006\u0010P\u001a\u00020\u00122\u0006\u0010<\u001a\u00020,2\u0006\u0010N\u001a\u00020\u000fH\u0002J\u0010\u0010Q\u001a\u00020\u00072\u0006\u0010R\u001a\u000206H\u0002J\u0018\u0010S\u001a\u00020\u00072\u0006\u0010T\u001a\u00020\u001e2\u0006\u0010<\u001a\u00020,H\u0002J\u0018\u0010U\u001a\u00020\u00072\u0006\u0010T\u001a\u00020\u001e2\u0006\u0010<\u001a\u00020,H\u0002J\b\u0010V\u001a\u00020\u0012H\u0002J\u0010\u0010W\u001a\u00020\u00072\u0006\u0010<\u001a\u00020,H\u0002J\b\u0010X\u001a\u00020\u0007H\u0002J\b\u0010Y\u001a\u00020\u0007H\u0002J\u0016\u0010Z\u001a\u00020\u00072\f\u0010R\u001a\b\u0012\u0004\u0012\u00020\u00120[H\u0002J \u0010\\\u001a\u00020\u00072\u0006\u0010M\u001a\u00020\u00122\u0006\u0010C\u001a\u00020\u00122\u0006\u0010J\u001a\u000206H\u0002J*\u0010]\u001a\u00020\u00072\b\u0010J\u001a\u0004\u0018\u0001062\u0006\u0010M\u001a\u00020\u00122\u0006\u0010^\u001a\u00020\u00122\u0006\u0010C\u001a\u00020\u0012H\u0002J\"\u0010_\u001a\u00020\u00072\u0006\u0010`\u001a\u00020a2\u0006\u0010I\u001a\u00020*2\b\b\u0002\u0010b\u001a\u00020\u000fH\u0002J\u0010\u0010c\u001a\u00020\u00072\u0006\u0010d\u001a\u00020\u000fH\u0002J\u001e\u0010e\u001a\u00020\u00072\b\u0010f\u001a\u0004\u0018\u00010a2\n\b\u0002\u0010g\u001a\u0004\u0018\u000106H\u0002J\u0010\u0010h\u001a\u00020\u000f2\u0006\u0010>\u001a\u000206H\u0002J\u0010\u0010i\u001a\u00020\u00072\u0006\u0010j\u001a\u00020$H\u0002J\b\u0010k\u001a\u00020\u000fH\u0002J\u0010\u0010l\u001a\u00020\u000f2\u0006\u0010J\u001a\u000206H\u0002J\b\u0010m\u001a\u00020\rH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0004\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0013R\u0012\u0010\u0014\u001a\u0004\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0013R\u000e\u0010\u0015\u001a\u00020\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0013R\u000e\u0010\u0018\u001a\u00020\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0019\u001a\u0004\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0013R\u000e\u0010\u001a\u001a\u00020\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u001c\u001a\u0012\u0012\u0004\u0012\u00020\u001e0\u001dj\b\u0012\u0004\u0012\u00020\u001e`\u001fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001e0!X\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\"\u001a\u0012\u0012\u0004\u0012\u00020\u001e0\u001dj\b\u0012\u0004\u0012\u00020\u001e`\u001fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010#\u001a\b\u0012\u0004\u0012\u00020$0!X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020(X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010)\u001a\u0004\u0018\u00010*X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010+\u001a\u0004\u0018\u00010,X\u0082\u000e¢\u0006\u0004\n\u0002\u0010-R\u0012\u0010.\u001a\u0004\u0018\u00010,X\u0082\u000e¢\u0006\u0004\n\u0002\u0010-R\u0012\u0010/\u001a\u0004\u0018\u00010,X\u0082\u000e¢\u0006\u0004\n\u0002\u0010-R\u0012\u00100\u001a\u0004\u0018\u00010,X\u0082\u000e¢\u0006\u0004\n\u0002\u0010-¨\u0006v"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkEngine;", "", "config", "Lcom/carplay/iap2/transport/Iap2LinkConfig;", "onPacketMetadata", "Lkotlin/Function1;", "Lcom/carplay/iap2/transport/Iap2PacketMetadata;", "", "<init>", "(Lcom/carplay/iap2/transport/Iap2LinkConfig;Lkotlin/jvm/functions/Function1;)V", "state", "Lcom/carplay/iap2/transport/Iap2LinkEngine$State;", "peerSynchronization", "Lcom/carplay/iap2/transport/Iap2LinkEngine$SynchronizationPayload;", "peerSynchronizationReceived", "", "respondingToPeerSynchronization", "localSynchronizationSequence", "", "Ljava/lang/Integer;", "peerSynchronizationSequence", "controlSessionId", "sentSequence", "lastSentAcknowledged", "lastReceivedInOrder", "lastAcknowledgedReceived", "accumulatedAcknowledgements", "writable", "unacknowledged", "Ljava/util/ArrayList;", "Lcom/carplay/iap2/transport/Iap2LinkEngine$Packet;", "Lkotlin/collections/ArrayList;", "queued", "Ljava/util/ArrayDeque;", "outOfOrder", "events", "Lcom/carplay/iap2/transport/Iap2LinkEngine$Event;", "output", "Ljava/io/ByteArrayOutputStream;", "receive", "Lcom/carplay/iap2/transport/Iap2LinkEngine$ReceiveBuffer;", "awaitingPayload", "Lcom/carplay/iap2/transport/Iap2LinkEngine$Header;", "markerDeadlineMillis", "", "Ljava/lang/Long;", "synchronizationDeadlineMillis", "sendAcknowledgementDeadlineMillis", "receiveAcknowledgementDeadlineMillis", "outboundAcknowledged", "validatedPeerSynchronization", "negotiatedControlSessionId", "()Ljava/lang/Integer;", "takeOutput", "", "pollEvent", "nextDeadlineMillis", "()Ljava/lang/Long;", "start", "wiredInitiator", "nowMillis", "feed", "bytes", "feedEof", "advanceTime", "sendControl", "sendSession", "sessionId", "parseAvailable", "parsePhoneDetection", "enterNegotiating", "sendImmediately", "processFrame", "header", "payload", "handleSynchronization", "sync", "sequence", "confirmsLocalSynchronization", "handleAcknowledgement", "acknowledgement", "handleExtendedAcknowledgement", "missing", "handleData", "packet", "sendPacket", "outgoingWindowLimit", "retransmitDuePacket", "sendSynchronization", "sendAcknowledgement", "sendExtendedAcknowledgement", "", "sendData", "writePacket", "control", "reportPacket", "action", "", "payloadChecksumValid", "setWritable", "value", "die", "reason", "restartInput", "appendOutput", "enqueueEvent", NotificationCompat.CATEGORY_EVENT, "peerLimitsAreUsable", "peerPayloadIsAcceptable", "localSynchronization", "State", "Event", "SessionDescriptor", "SynchronizationPayload", "Header", "Packet", "ReceiveBuffer", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2LinkEngine {
    private static final int ACK_SLACK = 10;
    private static final int CHECKSUM_BYTES = 1;
    private static final int CONTROL_ACKNOWLEDGEMENT = 64;
    private static final int CONTROL_EXTENDED_ACKNOWLEDGEMENT = 32;
    private static final int CONTROL_RESET = 16;
    public static final int CONTROL_SESSION_ID = 10;
    private static final int CONTROL_SYNCHRONIZE = 128;
    private static final int DEFAULT_MAX_ACKNOWLEDGEMENTS = 3;
    private static final int DEFAULT_MAX_RETRANSMISSIONS = 4;
    private static final int DEFAULT_RETRANSMISSION_TIMEOUT_MILLIS = 4000;
    public static final int EA_SESSION_ID = 11;
    public static final int FILE_TRANSFER_SESSION_ID = 12;
    private static final int HEADER_BYTES = 9;
    private static final int INITIAL_SEQUENCE = 99;
    private static final byte LINK_START_HIGH = -1;
    private static final byte LINK_START_LOW = 90;
    private static final long MARKER_RESEND_MILLIS = 1000;
    public static final int MAX_PAYLOAD_BYTES = 65525;
    public static final int MAX_WIRE_FRAME_BYTES = 65535;
    private static final int SESSION_DESCRIPTOR_BYTES = 3;
    private static final int SYNCHRONIZATION_FIXED_BYTES = 10;
    private static final long SYNCHRONIZATION_RESEND_MILLIS = 500;
    private static final int SYNCHRONIZATION_VERSION = 1;
    private int accumulatedAcknowledgements;
    private Header awaitingPayload;
    private final Iap2LinkConfig config;
    private int controlSessionId;
    private final ArrayDeque<Event> events;
    private Integer lastAcknowledgedReceived;
    private int lastReceivedInOrder;
    private Integer lastSentAcknowledged;
    private Integer localSynchronizationSequence;
    private Long markerDeadlineMillis;
    private final Function1<Iap2PacketMetadata, Unit> onPacketMetadata;
    private final ArrayList<Packet> outOfOrder;
    private final ByteArrayOutputStream output;
    private SynchronizationPayload peerSynchronization;
    private boolean peerSynchronizationReceived;
    private Integer peerSynchronizationSequence;
    private final ArrayDeque<Packet> queued;
    private final ReceiveBuffer receive;
    private Long receiveAcknowledgementDeadlineMillis;
    private boolean respondingToPeerSynchronization;
    private Long sendAcknowledgementDeadlineMillis;
    private int sentSequence;
    private State state;
    private Long synchronizationDeadlineMillis;
    private final ArrayList<Packet> unacknowledged;
    private boolean writable;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final byte[] IAP2_MARKER = {-1, 85, 2, 0, -18, 16};

    /* JADX INFO: compiled from: Iap2LinkEngine.kt */
    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\b\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\b¨\u0006\t"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkEngine$State;", "", "<init>", "(Ljava/lang/String;I)V", "IDLE", "DETECTING", "NEGOTIATING", "NORMAL", "DEAD", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public enum State {
        IDLE,
        DETECTING,
        NEGOTIATING,
        NORMAL,
        DEAD;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

        public static EnumEntries<State> getEntries() {
            return $ENTRIES;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Iap2LinkEngine() {
        this(null, 0 == true ? 1 : 0, 3, 0 == true ? 1 : 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Iap2LinkEngine(Iap2LinkConfig config, Function1<? super Iap2PacketMetadata, Unit> function1) {
        Intrinsics.checkNotNullParameter(config, "config");
        this.config = config;
        this.onPacketMetadata = function1;
        this.state = State.IDLE;
        this.peerSynchronization = localSynchronization();
        this.controlSessionId = 10;
        this.sentSequence = INITIAL_SEQUENCE;
        this.unacknowledged = new ArrayList<>();
        this.queued = new ArrayDeque<>();
        this.outOfOrder = new ArrayList<>();
        this.events = new ArrayDeque<>();
        this.output = new ByteArrayOutputStream();
        this.receive = new ReceiveBuffer();
    }

    public /* synthetic */ Iap2LinkEngine(Iap2LinkConfig iap2LinkConfig, Function1 function1, int i, DefaultConstructorMarker defaultConstructorMarker) {
        Iap2LinkConfig iap2LinkConfig2;
        Function1 function2;
        if ((i & 1) == 0) {
            iap2LinkConfig2 = iap2LinkConfig;
        } else {
            iap2LinkConfig2 = new Iap2LinkConfig(0, 0, 0, false, 0, 0, 0, 0, 0, null, null, false, false, null, 16383, null);
        }
        if ((i & 2) == 0) {
            function2 = function1;
        } else {
            function2 = null;
        }
        this(iap2LinkConfig2, function2);
    }

    /* JADX INFO: compiled from: Iap2LinkEngine.kt */
    @Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0004\u0004\u0005\u0006\u0007B\t\b\u0004¢\u0006\u0004\b\u0002\u0010\u0003\u0082\u0001\u0004\b\t\n\u000b¨\u0006\f"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkEngine$Event;", "", "<init>", "()V", "Control", "Session", "Writable", "Dead", "Lcom/carplay/iap2/transport/Iap2LinkEngine$Event$Control;", "Lcom/carplay/iap2/transport/Iap2LinkEngine$Event$Dead;", "Lcom/carplay/iap2/transport/Iap2LinkEngine$Event$Session;", "Lcom/carplay/iap2/transport/Iap2LinkEngine$Event$Writable;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static abstract class Event {
        public /* synthetic */ Event(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: compiled from: Iap2LinkEngine.kt */
        @Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\t\u0010\b\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rHÖ\u0003J\t\u0010\u000e\u001a\u00020\u000fHÖ\u0001J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0012"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkEngine$Event$Control;", "Lcom/carplay/iap2/transport/Iap2LinkEngine$Event;", "bytes", "", "<init>", "([B)V", "getBytes", "()[B", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
        public static final /* data */ class Control extends Event {
            private final byte[] bytes;

            public static /* synthetic */ Control copy$default(Control control, byte[] bArr, int i, Object obj) {
                if ((i & 1) != 0) {
                    bArr = control.bytes;
                }
                return control.copy(bArr);
            }

            /* JADX INFO: renamed from: component1, reason: from getter */
            public final byte[] getBytes() {
                return this.bytes;
            }

            public final Control copy(byte[] bytes) {
                Intrinsics.checkNotNullParameter(bytes, "bytes");
                return new Control(bytes);
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                return (other instanceof Control) && Intrinsics.areEqual(this.bytes, ((Control) other).bytes);
            }

            public int hashCode() {
                return Arrays.hashCode(this.bytes);
            }

            public String toString() {
                return "Control(bytes=" + Arrays.toString(this.bytes) + ")";
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public Control(byte[] bytes) {
                super(null);
                Intrinsics.checkNotNullParameter(bytes, "bytes");
                this.bytes = bytes;
            }

            public final byte[] getBytes() {
                return this.bytes;
            }
        }

        private Event() {
        }

        /* JADX INFO: compiled from: Iap2LinkEngine.kt */
        @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0014\u001a\u00020\u0015HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0016"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkEngine$Event$Session;", "Lcom/carplay/iap2/transport/Iap2LinkEngine$Event;", "sessionId", "", "bytes", "", "<init>", "(I[B)V", "getSessionId", "()I", "getBytes", "()[B", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
        public static final /* data */ class Session extends Event {
            private final byte[] bytes;
            private final int sessionId;

            public static /* synthetic */ Session copy$default(Session session, int i, byte[] bArr, int i2, Object obj) {
                if ((i2 & 1) != 0) {
                    i = session.sessionId;
                }
                if ((i2 & 2) != 0) {
                    bArr = session.bytes;
                }
                return session.copy(i, bArr);
            }

            /* JADX INFO: renamed from: component1, reason: from getter */
            public final int getSessionId() {
                return this.sessionId;
            }

            /* JADX INFO: renamed from: component2, reason: from getter */
            public final byte[] getBytes() {
                return this.bytes;
            }

            public final Session copy(int sessionId, byte[] bytes) {
                Intrinsics.checkNotNullParameter(bytes, "bytes");
                return new Session(sessionId, bytes);
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                if (!(other instanceof Session)) {
                    return false;
                }
                Session session = (Session) other;
                return this.sessionId == session.sessionId && Intrinsics.areEqual(this.bytes, session.bytes);
            }

            public int hashCode() {
                return (Integer.hashCode(this.sessionId) * 31) + Arrays.hashCode(this.bytes);
            }

            public String toString() {
                return "Session(sessionId=" + this.sessionId + ", bytes=" + Arrays.toString(this.bytes) + ")";
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public Session(int sessionId, byte[] bytes) {
                super(null);
                Intrinsics.checkNotNullParameter(bytes, "bytes");
                this.sessionId = sessionId;
                this.bytes = bytes;
            }

            public final byte[] getBytes() {
                return this.bytes;
            }

            public final int getSessionId() {
                return this.sessionId;
            }
        }

        /* JADX INFO: compiled from: Iap2LinkEngine.kt */
        @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\t\u0010\b\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\n\u001a\u00020\u00032\b\u0010\u000b\u001a\u0004\u0018\u00010\fHÖ\u0003J\t\u0010\r\u001a\u00020\u000eHÖ\u0001J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0011"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkEngine$Event$Writable;", "Lcom/carplay/iap2/transport/Iap2LinkEngine$Event;", "value", "", "<init>", "(Z)V", "getValue", "()Z", "component1", "copy", "equals", "other", "", "hashCode", "", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
        public static final /* data */ class Writable extends Event {
            private final boolean value;

            public static /* synthetic */ Writable copy$default(Writable writable, boolean z, int i, Object obj) {
                if ((i & 1) != 0) {
                    z = writable.value;
                }
                return writable.copy(z);
            }

            /* JADX INFO: renamed from: component1, reason: from getter */
            public final boolean getValue() {
                return this.value;
            }

            public final Writable copy(boolean value) {
                return new Writable(value);
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                return (other instanceof Writable) && this.value == ((Writable) other).value;
            }

            public int hashCode() {
                return Boolean.hashCode(this.value);
            }

            public String toString() {
                return "Writable(value=" + this.value + ")";
            }

            public Writable(boolean value) {
                super(null);
                this.value = value;
            }

            public final boolean getValue() {
                return this.value;
            }
        }

        /* JADX INFO: compiled from: Iap2LinkEngine.kt */
        @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u001d\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\r\u001a\u0004\u0018\u00010\u0005HÆ\u0003J!\u0010\u000e\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0003HÖ\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0016"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkEngine$Event$Dead;", "Lcom/carplay/iap2/transport/Iap2LinkEngine$Event;", "reason", "", "restartInput", "", "<init>", "(Ljava/lang/String;[B)V", "getReason", "()Ljava/lang/String;", "getRestartInput", "()[B", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "", "toString", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
        public static final /* data */ class Dead extends Event {
            private final String reason;
            private final byte[] restartInput;

            public static /* synthetic */ Dead copy$default(Dead dead, String str, byte[] bArr, int i, Object obj) {
                if ((i & 1) != 0) {
                    str = dead.reason;
                }
                if ((i & 2) != 0) {
                    bArr = dead.restartInput;
                }
                return dead.copy(str, bArr);
            }

            /* JADX INFO: renamed from: component1, reason: from getter */
            public final String getReason() {
                return this.reason;
            }

            /* JADX INFO: renamed from: component2, reason: from getter */
            public final byte[] getRestartInput() {
                return this.restartInput;
            }

            public final Dead copy(String reason, byte[] restartInput) {
                return new Dead(reason, restartInput);
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                if (!(other instanceof Dead)) {
                    return false;
                }
                Dead dead = (Dead) other;
                return Intrinsics.areEqual(this.reason, dead.reason) && Intrinsics.areEqual(this.restartInput, dead.restartInput);
            }

            public int hashCode() {
                return ((this.reason == null ? 0 : this.reason.hashCode()) * 31) + (this.restartInput != null ? Arrays.hashCode(this.restartInput) : 0);
            }

            public String toString() {
                return "Dead(reason=" + this.reason + ", restartInput=" + Arrays.toString(this.restartInput) + ")";
            }

            public Dead(String reason, byte[] restartInput) {
                super(null);
                this.reason = reason;
                this.restartInput = restartInput;
            }

            public /* synthetic */ Dead(String str, byte[] bArr, int i, DefaultConstructorMarker defaultConstructorMarker) {
                this(str, (i & 2) != 0 ? null : bArr);
            }

            public final String getReason() {
                return this.reason;
            }

            public final byte[] getRestartInput() {
                return this.restartInput;
            }
        }
    }

    /* JADX INFO: compiled from: Iap2LinkEngine.kt */
    @Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J'\u0010\u000f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0014\u001a\u00020\u0015HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\t¨\u0006\u0016"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkEngine$SessionDescriptor;", "", "id", "", "kind", "version", "<init>", "(III)V", "getId", "()I", "getKind", "getVersion", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class SessionDescriptor {
        private final int id;
        private final int kind;
        private final int version;

        public static /* synthetic */ SessionDescriptor copy$default(SessionDescriptor sessionDescriptor, int i, int i2, int i3, int i4, Object obj) {
            if ((i4 & 1) != 0) {
                i = sessionDescriptor.id;
            }
            if ((i4 & 2) != 0) {
                i2 = sessionDescriptor.kind;
            }
            if ((i4 & 4) != 0) {
                i3 = sessionDescriptor.version;
            }
            return sessionDescriptor.copy(i, i2, i3);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final int getId() {
            return this.id;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final int getKind() {
            return this.kind;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final int getVersion() {
            return this.version;
        }

        public final SessionDescriptor copy(int id, int kind, int version) {
            return new SessionDescriptor(id, kind, version);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof SessionDescriptor)) {
                return false;
            }
            SessionDescriptor sessionDescriptor = (SessionDescriptor) other;
            return this.id == sessionDescriptor.id && this.kind == sessionDescriptor.kind && this.version == sessionDescriptor.version;
        }

        public int hashCode() {
            return (((Integer.hashCode(this.id) * 31) + Integer.hashCode(this.kind)) * 31) + Integer.hashCode(this.version);
        }

        public String toString() {
            return "SessionDescriptor(id=" + this.id + ", kind=" + this.kind + ", version=" + this.version + ")";
        }

        public SessionDescriptor(int id, int kind, int version) {
            this.id = id;
            this.kind = kind;
            this.version = version;
            int i = this.id;
            if (!(i >= 0 && i < 256)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            int i2 = this.kind;
            if (!(i2 >= 0 && i2 < 256)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            int i3 = this.version;
            if (i3 >= 0 && i3 < 256) {
            } else {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
        }

        public final int getId() {
            return this.id;
        }

        public final int getKind() {
            return this.kind;
        }

        public final int getVersion() {
            return this.version;
        }
    }

    /* JADX INFO: compiled from: Iap2LinkEngine.kt */
    @Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0012\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\b\u0018\u0000 '2\u00020\u0001:\u0001'BE\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n¢\u0006\u0004\b\f\u0010\rJ\u0006\u0010\u0017\u001a\u00020\u0018J\t\u0010\u0019\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001a\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001b\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001d\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001e\u001a\u00020\u0003HÆ\u0003J\u000f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nHÆ\u0003JU\u0010 \u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nHÆ\u0001J\u0013\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010$\u001a\u00020\u0003HÖ\u0001J\t\u0010%\u001a\u00020&HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000fR\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000fR\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000fR\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016¨\u0006("}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkEngine$SynchronizationPayload;", "", "maxOutgoing", "", "maxLength", "retransmissionTimeoutMillis", "acknowledgementTimeoutMillis", "maxRetransmissions", "maxAcknowledgements", "sessions", "", "Lcom/carplay/iap2/transport/Iap2LinkEngine$SessionDescriptor;", "<init>", "(IIIIIILjava/util/List;)V", "getMaxOutgoing", "()I", "getMaxLength", "getRetransmissionTimeoutMillis", "getAcknowledgementTimeoutMillis", "getMaxRetransmissions", "getMaxAcknowledgements", "getSessions", "()Ljava/util/List;", "encode", "", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "toString", "", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class SynchronizationPayload {

        /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
        public static final Companion INSTANCE = new Companion(null);
        private final int acknowledgementTimeoutMillis;
        private final int maxAcknowledgements;
        private final int maxLength;
        private final int maxOutgoing;
        private final int maxRetransmissions;
        private final int retransmissionTimeoutMillis;
        private final List<SessionDescriptor> sessions;

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ SynchronizationPayload copy$default(SynchronizationPayload synchronizationPayload, int i, int i2, int i3, int i4, int i5, int i6, List list, int i7, Object obj) {
            if ((i7 & 1) != 0) {
                i = synchronizationPayload.maxOutgoing;
            }
            if ((i7 & 2) != 0) {
                i2 = synchronizationPayload.maxLength;
            }
            if ((i7 & 4) != 0) {
                i3 = synchronizationPayload.retransmissionTimeoutMillis;
            }
            if ((i7 & 8) != 0) {
                i4 = synchronizationPayload.acknowledgementTimeoutMillis;
            }
            if ((i7 & 16) != 0) {
                i5 = synchronizationPayload.maxRetransmissions;
            }
            if ((i7 & 32) != 0) {
                i6 = synchronizationPayload.maxAcknowledgements;
            }
            if ((i7 & 64) != 0) {
                list = synchronizationPayload.sessions;
            }
            int i8 = i6;
            List list2 = list;
            int i9 = i5;
            int i10 = i3;
            return synchronizationPayload.copy(i, i2, i10, i4, i9, i8, list2);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final int getMaxOutgoing() {
            return this.maxOutgoing;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final int getMaxLength() {
            return this.maxLength;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final int getRetransmissionTimeoutMillis() {
            return this.retransmissionTimeoutMillis;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final int getAcknowledgementTimeoutMillis() {
            return this.acknowledgementTimeoutMillis;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final int getMaxRetransmissions() {
            return this.maxRetransmissions;
        }

        /* JADX INFO: renamed from: component6, reason: from getter */
        public final int getMaxAcknowledgements() {
            return this.maxAcknowledgements;
        }

        public final List<SessionDescriptor> component7() {
            return this.sessions;
        }

        public final SynchronizationPayload copy(int maxOutgoing, int maxLength, int retransmissionTimeoutMillis, int acknowledgementTimeoutMillis, int maxRetransmissions, int maxAcknowledgements, List<SessionDescriptor> sessions) {
            Intrinsics.checkNotNullParameter(sessions, "sessions");
            return new SynchronizationPayload(maxOutgoing, maxLength, retransmissionTimeoutMillis, acknowledgementTimeoutMillis, maxRetransmissions, maxAcknowledgements, sessions);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof SynchronizationPayload)) {
                return false;
            }
            SynchronizationPayload synchronizationPayload = (SynchronizationPayload) other;
            return this.maxOutgoing == synchronizationPayload.maxOutgoing && this.maxLength == synchronizationPayload.maxLength && this.retransmissionTimeoutMillis == synchronizationPayload.retransmissionTimeoutMillis && this.acknowledgementTimeoutMillis == synchronizationPayload.acknowledgementTimeoutMillis && this.maxRetransmissions == synchronizationPayload.maxRetransmissions && this.maxAcknowledgements == synchronizationPayload.maxAcknowledgements && Intrinsics.areEqual(this.sessions, synchronizationPayload.sessions);
        }

        public int hashCode() {
            return (((((((((((Integer.hashCode(this.maxOutgoing) * 31) + Integer.hashCode(this.maxLength)) * 31) + Integer.hashCode(this.retransmissionTimeoutMillis)) * 31) + Integer.hashCode(this.acknowledgementTimeoutMillis)) * 31) + Integer.hashCode(this.maxRetransmissions)) * 31) + Integer.hashCode(this.maxAcknowledgements)) * 31) + this.sessions.hashCode();
        }

        public String toString() {
            return "SynchronizationPayload(maxOutgoing=" + this.maxOutgoing + ", maxLength=" + this.maxLength + ", retransmissionTimeoutMillis=" + this.retransmissionTimeoutMillis + ", acknowledgementTimeoutMillis=" + this.acknowledgementTimeoutMillis + ", maxRetransmissions=" + this.maxRetransmissions + ", maxAcknowledgements=" + this.maxAcknowledgements + ", sessions=" + this.sessions + ")";
        }

        public SynchronizationPayload(int maxOutgoing, int maxLength, int retransmissionTimeoutMillis, int acknowledgementTimeoutMillis, int maxRetransmissions, int maxAcknowledgements, List<SessionDescriptor> sessions) {
            Intrinsics.checkNotNullParameter(sessions, "sessions");
            this.maxOutgoing = maxOutgoing;
            this.maxLength = maxLength;
            this.retransmissionTimeoutMillis = retransmissionTimeoutMillis;
            this.acknowledgementTimeoutMillis = acknowledgementTimeoutMillis;
            this.maxRetransmissions = maxRetransmissions;
            this.maxAcknowledgements = maxAcknowledgements;
            this.sessions = sessions;
            int i = this.maxOutgoing;
            if (!(i >= 0 && i < 256)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            int i2 = this.maxLength;
            if (!(i2 >= 0 && i2 < 65536)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            int i3 = this.retransmissionTimeoutMillis;
            if (!(i3 >= 0 && i3 < 65536)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            int i4 = this.acknowledgementTimeoutMillis;
            if (!(i4 >= 0 && i4 < 65536)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            int i5 = this.maxRetransmissions;
            if (!(i5 >= 0 && i5 < 256)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            int i6 = this.maxAcknowledgements;
            if (i6 >= 0 && i6 < 256) {
            } else {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
        }

        public final int getMaxOutgoing() {
            return this.maxOutgoing;
        }

        public final int getMaxLength() {
            return this.maxLength;
        }

        public final int getRetransmissionTimeoutMillis() {
            return this.retransmissionTimeoutMillis;
        }

        public final int getAcknowledgementTimeoutMillis() {
            return this.acknowledgementTimeoutMillis;
        }

        public final int getMaxRetransmissions() {
            return this.maxRetransmissions;
        }

        public final int getMaxAcknowledgements() {
            return this.maxAcknowledgements;
        }

        public final List<SessionDescriptor> getSessions() {
            return this.sessions;
        }

        public final byte[] encode() {
            ByteArrayOutputStream out = new ByteArrayOutputStream((this.sessions.size() * 3) + 10);
            out.write(1);
            out.write(this.maxOutgoing);
            Iap2LinkEngine.INSTANCE.writeU16(out, this.maxLength);
            Iap2LinkEngine.INSTANCE.writeU16(out, this.retransmissionTimeoutMillis);
            Iap2LinkEngine.INSTANCE.writeU16(out, this.acknowledgementTimeoutMillis);
            out.write(this.maxRetransmissions);
            out.write(this.maxAcknowledgements);
            Iterable $this$forEach$iv = this.sessions;
            for (Object element$iv : $this$forEach$iv) {
                SessionDescriptor it = (SessionDescriptor) element$iv;
                out.write(it.getId());
                out.write(it.getKind());
                out.write(it.getVersion());
            }
            byte[] byteArray = out.toByteArray();
            Intrinsics.checkNotNullExpressionValue(byteArray, "toByteArray(...)");
            return byteArray;
        }

        /* JADX INFO: compiled from: Iap2LinkEngine.kt */
        @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u0007¨\u0006\b"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkEngine$SynchronizationPayload$Companion;", "", "<init>", "()V", "decode", "Lcom/carplay/iap2/transport/Iap2LinkEngine$SynchronizationPayload;", "bytes", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            public final SynchronizationPayload decode(byte[] bytes) {
                Intrinsics.checkNotNullParameter(bytes, "bytes");
                if (bytes.length < 10 || Iap2LinkEngine.INSTANCE.u8(bytes[0]) != 1) {
                    return null;
                }
                ArrayList descriptors = new ArrayList();
                for (int offset = 10; offset + 3 <= bytes.length; offset += 3) {
                    descriptors.add(new SessionDescriptor(Iap2LinkEngine.INSTANCE.u8(bytes[offset]), Iap2LinkEngine.INSTANCE.u8(bytes[offset + 1]), Iap2LinkEngine.INSTANCE.u8(bytes[offset + 2])));
                }
                return new SynchronizationPayload(Iap2LinkEngine.INSTANCE.u8(bytes[1]), Iap2LinkEngine.INSTANCE.readU16(bytes, 2), Iap2LinkEngine.INSTANCE.readU16(bytes, 4), Iap2LinkEngine.INSTANCE.readU16(bytes, 6), Iap2LinkEngine.INSTANCE.u8(bytes[8]), Iap2LinkEngine.INSTANCE.u8(bytes[9]), descriptors);
            }
        }
    }

    /* JADX INFO: compiled from: Iap2LinkEngine.kt */
    @Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J;\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0019\u001a\u00020\u0003HÖ\u0001J\t\u0010\u001a\u001a\u00020\u001bHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000bR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000bR\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000b¨\u0006\u001c"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkEngine$Header;", "", "length", "", "control", "sequence", "acknowledgement", "sessionId", "<init>", "(IIIII)V", "getLength", "()I", "getControl", "getSequence", "getAcknowledgement", "getSessionId", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class Header {
        private final int acknowledgement;
        private final int control;
        private final int length;
        private final int sequence;
        private final int sessionId;

        public static /* synthetic */ Header copy$default(Header header, int i, int i2, int i3, int i4, int i5, int i6, Object obj) {
            if ((i6 & 1) != 0) {
                i = header.length;
            }
            if ((i6 & 2) != 0) {
                i2 = header.control;
            }
            if ((i6 & 4) != 0) {
                i3 = header.sequence;
            }
            if ((i6 & 8) != 0) {
                i4 = header.acknowledgement;
            }
            if ((i6 & 16) != 0) {
                i5 = header.sessionId;
            }
            int i7 = i5;
            int i8 = i3;
            return header.copy(i, i2, i8, i4, i7);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final int getLength() {
            return this.length;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final int getControl() {
            return this.control;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final int getSequence() {
            return this.sequence;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final int getAcknowledgement() {
            return this.acknowledgement;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final int getSessionId() {
            return this.sessionId;
        }

        public final Header copy(int length, int control, int sequence, int acknowledgement, int sessionId) {
            return new Header(length, control, sequence, acknowledgement, sessionId);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Header)) {
                return false;
            }
            Header header = (Header) other;
            return this.length == header.length && this.control == header.control && this.sequence == header.sequence && this.acknowledgement == header.acknowledgement && this.sessionId == header.sessionId;
        }

        public int hashCode() {
            return (((((((Integer.hashCode(this.length) * 31) + Integer.hashCode(this.control)) * 31) + Integer.hashCode(this.sequence)) * 31) + Integer.hashCode(this.acknowledgement)) * 31) + Integer.hashCode(this.sessionId);
        }

        public String toString() {
            return "Header(length=" + this.length + ", control=" + this.control + ", sequence=" + this.sequence + ", acknowledgement=" + this.acknowledgement + ", sessionId=" + this.sessionId + ")";
        }

        public Header(int length, int control, int sequence, int acknowledgement, int sessionId) {
            this.length = length;
            this.control = control;
            this.sequence = sequence;
            this.acknowledgement = acknowledgement;
            this.sessionId = sessionId;
        }

        public final int getLength() {
            return this.length;
        }

        public final int getControl() {
            return this.control;
        }

        public final int getSequence() {
            return this.sequence;
        }

        public final int getAcknowledgement() {
            return this.acknowledgement;
        }

        public final int getSessionId() {
            return this.sessionId;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: Iap2LinkEngine.kt */
    @Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0016\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B3\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\t¢\u0006\u0004\b\n\u0010\u000bJ\t\u0010\u0019\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001a\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001b\u001a\u00020\u0006HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001d\u001a\u00020\tHÆ\u0003J;\u0010\u001e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\tHÆ\u0001J\u0013\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\"\u001a\u00020\u0003HÖ\u0001J\t\u0010#\u001a\u00020$HÖ\u0001R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\rR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0007\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\r\"\u0004\b\u0014\u0010\u000fR\u001a\u0010\b\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018¨\u0006%"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkEngine$Packet;", "", "sequence", "", "sessionId", "payload", "", "retransmissions", "deadlineMillis", "", "<init>", "(II[BIJ)V", "getSequence", "()I", "setSequence", "(I)V", "getSessionId", "getPayload", "()[B", "getRetransmissions", "setRetransmissions", "getDeadlineMillis", "()J", "setDeadlineMillis", "(J)V", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    static final /* data */ class Packet {
        private long deadlineMillis;
        private final byte[] payload;
        private int retransmissions;
        private int sequence;
        private final int sessionId;

        public static /* synthetic */ Packet copy$default(Packet packet, int i, int i2, byte[] bArr, int i3, long j, int i4, Object obj) {
            if ((i4 & 1) != 0) {
                i = packet.sequence;
            }
            if ((i4 & 2) != 0) {
                i2 = packet.sessionId;
            }
            if ((i4 & 4) != 0) {
                bArr = packet.payload;
            }
            if ((i4 & 8) != 0) {
                i3 = packet.retransmissions;
            }
            if ((i4 & 16) != 0) {
                j = packet.deadlineMillis;
            }
            long j2 = j;
            return packet.copy(i, i2, bArr, i3, j2);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final int getSequence() {
            return this.sequence;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final int getSessionId() {
            return this.sessionId;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final byte[] getPayload() {
            return this.payload;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final int getRetransmissions() {
            return this.retransmissions;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final long getDeadlineMillis() {
            return this.deadlineMillis;
        }

        public final Packet copy(int sequence, int sessionId, byte[] payload, int retransmissions, long deadlineMillis) {
            Intrinsics.checkNotNullParameter(payload, "payload");
            return new Packet(sequence, sessionId, payload, retransmissions, deadlineMillis);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Packet)) {
                return false;
            }
            Packet packet = (Packet) other;
            return this.sequence == packet.sequence && this.sessionId == packet.sessionId && Intrinsics.areEqual(this.payload, packet.payload) && this.retransmissions == packet.retransmissions && this.deadlineMillis == packet.deadlineMillis;
        }

        public int hashCode() {
            return (((((((Integer.hashCode(this.sequence) * 31) + Integer.hashCode(this.sessionId)) * 31) + Arrays.hashCode(this.payload)) * 31) + Integer.hashCode(this.retransmissions)) * 31) + Long.hashCode(this.deadlineMillis);
        }

        public String toString() {
            return "Packet(sequence=" + this.sequence + ", sessionId=" + this.sessionId + ", payload=" + Arrays.toString(this.payload) + ", retransmissions=" + this.retransmissions + ", deadlineMillis=" + this.deadlineMillis + ")";
        }

        public Packet(int sequence, int sessionId, byte[] payload, int retransmissions, long deadlineMillis) {
            Intrinsics.checkNotNullParameter(payload, "payload");
            this.sequence = sequence;
            this.sessionId = sessionId;
            this.payload = payload;
            this.retransmissions = retransmissions;
            this.deadlineMillis = deadlineMillis;
        }

        /* JADX WARN: Illegal instructions before constructor call */
        public /* synthetic */ Packet(int i, int i2, byte[] bArr, int i3, long j, int i4, DefaultConstructorMarker defaultConstructorMarker) {
            int i5;
            long j2;
            if ((i4 & 8) == 0) {
                i5 = i3;
            } else {
                i5 = 0;
            }
            if ((i4 & 16) == 0) {
                j2 = j;
            } else {
                j2 = 0;
            }
            this(i, i2, bArr, i5, j2);
        }

        public final int getSequence() {
            return this.sequence;
        }

        public final void setSequence(int i) {
            this.sequence = i;
        }

        public final int getSessionId() {
            return this.sessionId;
        }

        public final byte[] getPayload() {
            return this.payload;
        }

        public final int getRetransmissions() {
            return this.retransmissions;
        }

        public final void setRetransmissions(int i) {
            this.retransmissions = i;
        }

        public final long getDeadlineMillis() {
            return this.deadlineMillis;
        }

        public final void setDeadlineMillis(long j) {
            this.deadlineMillis = j;
        }
    }

    /* JADX INFO: renamed from: state, reason: from getter */
    public final State getState() {
        return this.state;
    }

    /* JADX INFO: renamed from: writable, reason: from getter */
    public final boolean getWritable() {
        return this.writable;
    }

    public final boolean outboundAcknowledged() {
        return this.state == State.NORMAL && this.queued.isEmpty() && this.unacknowledged.isEmpty();
    }

    /* JADX INFO: renamed from: peerSynchronization, reason: from getter */
    public final SynchronizationPayload getPeerSynchronization() {
        return this.peerSynchronization;
    }

    public final SynchronizationPayload validatedPeerSynchronization() {
        SynchronizationPayload synchronizationPayload = this.peerSynchronization;
        if (this.peerSynchronizationReceived) {
            return synchronizationPayload;
        }
        return null;
    }

    public final Integer negotiatedControlSessionId() {
        Integer numValueOf = Integer.valueOf(this.controlSessionId);
        numValueOf.intValue();
        if (this.peerSynchronizationReceived) {
            return numValueOf;
        }
        return null;
    }

    public final byte[] takeOutput() {
        byte[] bytes = this.output.toByteArray();
        this.output.reset();
        Intrinsics.checkNotNull(bytes);
        return bytes;
    }

    public final Event pollEvent() {
        if (this.events.isEmpty()) {
            return null;
        }
        return this.events.removeFirst();
    }

    public final Long nextDeadlineMillis() {
        return (Long) CollectionsKt.minOrNull((Iterable) CollectionsKt.listOfNotNull((Object[]) new Long[]{this.markerDeadlineMillis, this.synchronizationDeadlineMillis, this.sendAcknowledgementDeadlineMillis, this.receiveAcknowledgementDeadlineMillis}));
    }

    public final void start(boolean wiredInitiator, long nowMillis) {
        if (this.state != State.IDLE) {
            return;
        }
        this.state = State.DETECTING;
        if (this.config.getStartup() != Iap2LinkStartup.ACCESSORY_FIRST && appendOutput(IAP2_MARKER)) {
            this.markerDeadlineMillis = Long.valueOf(1000 + nowMillis);
            if (!wiredInitiator || this.config.getStartup() != Iap2LinkStartup.LEGACY || this.state == State.DEAD) {
                return;
            }
            enterNegotiating$default(this, nowMillis, false, 2, null);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void feed(byte[] bytes, long nowMillis) {
        int i;
        byte[] bArr;
        Event.Dead dead;
        Object[] objArr;
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        if (this.state != State.DEAD) {
            if (bytes.length == 0) {
                return;
            }
            int i2 = 0;
            while (true) {
                i = 2;
                bArr = null;
                objArr = 0;
                if (i2 >= bytes.length || this.state == State.DEAD) {
                    break;
                }
                int iRemainingCapacity = this.receive.remainingCapacity();
                if (iRemainingCapacity == 0) {
                    if (!parseAvailable(nowMillis)) {
                        die$default(this, "iAP2 inbound frame exceeds 65535 bytes", null, 2, null);
                        return;
                    }
                } else {
                    int iMin = Math.min(iRemainingCapacity, bytes.length - i2);
                    this.receive.append(bytes, i2, iMin);
                    i2 += iMin;
                    while (parseAvailable(nowMillis) && this.state != State.DEAD) {
                    }
                }
            }
            Event eventPeekLast = this.events.peekLast();
            Event.Dead dead2 = eventPeekLast instanceof Event.Dead ? (Event.Dead) eventPeekLast : null;
            if ((dead2 != null ? dead2.getRestartInput() : null) != null && i2 < bytes.length) {
                int length = bytes.length - i2;
                this.events.removeLast();
                ArrayDeque<Event> arrayDeque = this.events;
                if (length <= 65535 - dead2.getRestartInput().length) {
                    dead = Event.Dead.copy$default(dead2, null, ArraysKt.plus(dead2.getRestartInput(), ArraysKt.copyOfRange(bytes, i2, bytes.length)), 1, null);
                } else {
                    dead = new Event.Dead("iAP2 restart input limit exceeded", bArr, i, objArr == true ? 1 : 0);
                }
                arrayDeque.add(dead);
            }
        }
    }

    public final void feedEof() {
        die$default(this, null, null, 2, null);
    }

    /* JADX WARN: Code duplicated, block: B:17:0x002f  */
    /* JADX WARN: Code duplicated, block: B:33:0x0063  */
    /* JADX WARN: Code duplicated, block: B:48:0x0097  */
    /* JADX WARN: Code duplicated, block: B:61:0x00c4  */
    public final void advanceTime(long nowMillis) {
        Long lNextDeadlineMillis;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        while (this.state != State.DEAD && (lNextDeadlineMillis = nextDeadlineMillis()) != null) {
            long deadline = lNextDeadlineMillis.longValue();
            if (deadline > nowMillis) {
                return;
            }
            Long l = this.markerDeadlineMillis;
            if (l != null) {
                long it = l.longValue();
                if ((it <= nowMillis ? 1 : 0) == 1) {
                    z = true;
                } else {
                    z = false;
                }
            } else {
                z = false;
            }
            if (z) {
                this.markerDeadlineMillis = null;
                if (this.state == State.DETECTING && appendOutput(IAP2_MARKER)) {
                    this.markerDeadlineMillis = Long.valueOf(1000 + nowMillis);
                }
            } else {
                Long l2 = this.synchronizationDeadlineMillis;
                if (l2 != null) {
                    long it2 = l2.longValue();
                    if ((it2 <= nowMillis ? 1 : 0) == 1) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                } else {
                    z2 = false;
                }
                if (z2) {
                    this.synchronizationDeadlineMillis = null;
                    if (this.state == State.NEGOTIATING) {
                        sendSynchronization();
                        if (this.state != State.DEAD) {
                            this.synchronizationDeadlineMillis = Long.valueOf(SYNCHRONIZATION_RESEND_MILLIS + nowMillis);
                        }
                    }
                } else {
                    Long l3 = this.sendAcknowledgementDeadlineMillis;
                    if (l3 != null) {
                        long it3 = l3.longValue();
                        if ((it3 <= nowMillis ? 1 : 0) == 1) {
                            z3 = true;
                        } else {
                            z3 = false;
                        }
                    } else {
                        z3 = false;
                    }
                    if (z3) {
                        this.sendAcknowledgementDeadlineMillis = null;
                        if (this.state == State.NORMAL) {
                            this.lastAcknowledgedReceived = Integer.valueOf(this.lastReceivedInOrder);
                            sendAcknowledgement();
                        }
                    } else {
                        Long l4 = this.receiveAcknowledgementDeadlineMillis;
                        if (l4 != null) {
                            long it4 = l4.longValue();
                            z4 = ((it4 > nowMillis ? 1 : (it4 == nowMillis ? 0 : -1)) <= 0 ? 1 : 0) == 1;
                        }
                        if (z4) {
                            this.receiveAcknowledgementDeadlineMillis = null;
                            retransmitDuePacket(nowMillis);
                        }
                    }
                }
            }
        }
    }

    public final void sendControl(byte[] bytes, long nowMillis) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        sendSession(this.controlSessionId, bytes, nowMillis);
    }

    public final void sendSession(int sessionId, byte[] bytes, long nowMillis) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        if (!(sessionId >= 0 && sessionId < 256)) {
            throw new IllegalArgumentException("iAP2 session id must fit in one byte".toString());
        }
        if (!(bytes.length <= 65525)) {
            throw new IllegalArgumentException("iAP2 session payload exceeds 65525 bytes".toString());
        }
        if ((this.peerSynchronizationReceived || this.state == State.NORMAL) && !peerPayloadIsAcceptable(bytes)) {
            throw new IllegalArgumentException(("iAP2 session payload exceeds peer maxLength " + this.peerSynchronization.getMaxLength()).toString());
        }
        byte[] bArrCopyOf = Arrays.copyOf(bytes, bytes.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        sendPacket(new Packet(0, sessionId, bArrCopyOf, 0, 0L, 24, null), nowMillis);
    }

    private final boolean parseAvailable(long nowMillis) {
        if (this.state == State.DEAD) {
            return false;
        }
        if (this.state == State.DETECTING) {
            if (this.config.getStartup() != Iap2LinkStartup.LEGACY) {
                return parsePhoneDetection(nowMillis);
            }
            if (this.receive.getSize() < IAP2_MARKER.length) {
                return false;
            }
            if (!this.receive.matches(IAP2_MARKER)) {
                die$default(this, "iAP2 marker was not received", null, 2, null);
                return false;
            }
            this.receive.discard(IAP2_MARKER.length);
            enterNegotiating$default(this, nowMillis, false, 2, null);
            return true;
        }
        Header pending = this.awaitingPayload;
        if (pending != null) {
            int payloadWithChecksumBytes = pending.getLength() - 9;
            if (this.receive.getSize() < payloadWithChecksumBytes) {
                return false;
            }
            byte[] payloadWithChecksum = this.receive.take(payloadWithChecksumBytes);
            this.awaitingPayload = null;
            if (INSTANCE.checksumValid(payloadWithChecksum)) {
                byte[] bArrCopyOf = Arrays.copyOf(payloadWithChecksum, payloadWithChecksum.length - 1);
                Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
                processFrame(pending, bArrCopyOf, nowMillis);
            } else {
                reportPacket("REJECTED", pending, false);
            }
            return true;
        }
        boolean discarded = false;
        while (this.receive.getSize() >= 2 && (this.receive.get(0) != -1 || this.receive.get(1) != 90)) {
            if (this.config.getNegotiateControlSession() && this.state == State.NORMAL && this.receive.get(0) == IAP2_MARKER[0] && this.receive.get(1) == IAP2_MARKER[1]) {
                if (this.receive.getSize() < IAP2_MARKER.length) {
                    return discarded;
                }
                if (this.receive.matches(IAP2_MARKER)) {
                    boolean ignoreRepeatedDetectionMarker = this.config.getIgnoreRepeatedDetectionMarker();
                    ReceiveBuffer receiveBuffer = this.receive;
                    if (ignoreRepeatedDetectionMarker) {
                        receiveBuffer.discard(IAP2_MARKER.length);
                        discarded = true;
                    } else {
                        die("车机在控制阶段重新发送 iAP2 检测包，旧链路已结束", receiveBuffer.take(this.receive.getSize()));
                        return false;
                    }
                }
            }
            this.receive.discard(1);
            discarded = true;
        }
        if (this.receive.getSize() < 9) {
            return discarded;
        }
        byte[] rawHeader = this.receive.take(9);
        Header header = INSTANCE.parseHeader(rawHeader);
        if (header == null || header.getLength() < 9) {
            return true;
        }
        if (header.getLength() <= 65535) {
            if (header.getLength() == 9) {
                processFrame(header, null, nowMillis);
            } else {
                this.awaitingPayload = header;
            }
            return true;
        }
        die$default(this, "iAP2 frame length " + header.getLength() + " exceeds 65535", null, 2, null);
        return false;
    }

    private final boolean parsePhoneDetection(long nowMillis) {
        if (this.receive.getSize() < 2) {
            return false;
        }
        if (this.receive.get(0) == IAP2_MARKER[0] && this.receive.get(1) == IAP2_MARKER[1]) {
            if (this.receive.getSize() < IAP2_MARKER.length) {
                return false;
            }
            if (this.receive.matches(IAP2_MARKER)) {
                this.receive.discard(IAP2_MARKER.length);
                if (this.config.getStartup() == Iap2LinkStartup.ACCESSORY_FIRST) {
                    if (!appendOutput(IAP2_MARKER)) {
                        return false;
                    }
                    this.respondingToPeerSynchronization = true;
                    enterNegotiating(nowMillis, false);
                } else {
                    enterNegotiating$default(this, nowMillis, false, 2, null);
                }
                return true;
            }
        } else if (this.receive.get(0) == -1 && this.receive.get(1) == 90) {
            if (this.receive.getSize() < 9) {
                return false;
            }
            Companion companion = INSTANCE;
            byte[] bArr = new byte[9];
            for (int i = 0; i < 9; i++) {
                bArr[i] = this.receive.get(i);
            }
            Header header = companion.parseHeader(bArr);
            if (header != null && (header.getControl() & 16) != 0) {
                die$default(this, "peer sent iAP2 reset", null, 2, null);
                return false;
            }
            if (header != null && header.getLength() > 9 && (header.getControl() & 128) != 0) {
                this.respondingToPeerSynchronization = true;
                enterNegotiating(nowMillis, false);
                return true;
            }
        }
        this.receive.discard(1);
        return true;
    }

    static /* synthetic */ void enterNegotiating$default(Iap2LinkEngine iap2LinkEngine, long j, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = true;
        }
        iap2LinkEngine.enterNegotiating(j, z);
    }

    private final void enterNegotiating(long nowMillis, boolean sendImmediately) {
        this.state = State.NEGOTIATING;
        this.markerDeadlineMillis = null;
        if (sendImmediately) {
            sendSynchronization();
            if (this.state != State.DEAD) {
                this.synchronizationDeadlineMillis = Long.valueOf(SYNCHRONIZATION_RESEND_MILLIS + nowMillis);
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:14:0x0032  */
    private final void processFrame(Header header, byte[] payload, long nowMillis) {
        boolean z;
        long j;
        reportPacket$default(this, "RECEIVED", header, false, 4, null);
        if ((header.getControl() & 16) != 0) {
            die$default(this, "peer sent iAP2 reset", null, 2, null);
            return;
        }
        if (this.localSynchronizationSequence != null) {
            int acknowledgement = header.getAcknowledgement();
            Integer num = this.localSynchronizationSequence;
            if (num != null && acknowledgement == num.intValue()) {
                z = true;
            } else {
                z = false;
            }
        } else {
            z = false;
        }
        boolean confirmsLocalSynchronization = z;
        if ((header.getControl() & 128) == 0) {
            j = nowMillis;
        } else {
            if (this.config.getNegotiateControlSession() && (payload == null || payload.length < 13 || (payload.length - 10) % 3 != 0 || INSTANCE.u8(payload[0]) != 1)) {
                die$default(this, "peer iAP2 synchronization payload is malformed", null, 2, null);
                return;
            }
            if (!this.config.getNegotiateControlSession() || this.state != State.NORMAL) {
                SynchronizationPayload it = SynchronizationPayload.INSTANCE.decode(payload == null ? new byte[0] : payload);
                if (it == null) {
                    j = nowMillis;
                } else {
                    boolean confirmed = this.config.getNegotiateControlSession() && confirmsLocalSynchronization && (header.getControl() & 64) != 0;
                    handleSynchronization(it, header.getSequence(), nowMillis, confirmed);
                    j = nowMillis;
                }
                if (this.state == State.DEAD) {
                    return;
                }
            } else {
                SynchronizationPayload repeated = SynchronizationPayload.INSTANCE.decode(payload == null ? new byte[0] : payload);
                if (Intrinsics.areEqual(repeated, this.peerSynchronization)) {
                    int sequence = header.getSequence();
                    Integer num2 = this.peerSynchronizationSequence;
                    if (num2 != null && sequence == num2.intValue()) {
                        if ((header.getControl() & 64) == 0 || !confirmsLocalSynchronization) {
                            return;
                        }
                        sendAcknowledgement();
                        return;
                    }
                }
                die$default(this, "peer changed iAP2 synchronization during an active session", null, 2, null);
                return;
            }
        }
        if ((header.getControl() & 64) != 0) {
            this.accumulatedAcknowledgements++;
            handleAcknowledgement(header.getAcknowledgement(), j, confirmsLocalSynchronization);
        }
        if ((header.getControl() & 32) != 0 && payload != null) {
            handleExtendedAcknowledgement(payload);
        }
        if ((header.getControl() & (-65)) == 0 && payload != null) {
            handleData(new Packet(header.getSequence(), header.getSessionId(), payload, 0, 0L, 24, null), j);
        }
        if (this.peerSynchronization.getMaxAcknowledgements() > 0 && this.accumulatedAcknowledgements >= this.peerSynchronization.getMaxAcknowledgements()) {
            this.accumulatedAcknowledgements = 0;
            this.lastAcknowledgedReceived = Integer.valueOf(this.lastReceivedInOrder);
            sendAcknowledgement();
        }
    }

    private final void handleSynchronization(SynchronizationPayload sync, int sequence, long nowMillis, boolean confirmsLocalSynchronization) {
        Object element$iv;
        SessionDescriptor it;
        if (this.state != State.NEGOTIATING) {
            return;
        }
        if (this.config.getNegotiateControlSession()) {
            Iterable $this$firstOrNull$iv = sync.getSessions();
            Iterator it2 = $this$firstOrNull$iv.iterator();
            do {
                if (!it2.hasNext()) {
                    element$iv = null;
                    break;
                } else {
                    element$iv = it2.next();
                    it = (SessionDescriptor) element$iv;
                }
            } while (!(it.getKind() == 0 && it.getVersion() == 2 && it.getId() != 0));
            SessionDescriptor control = (SessionDescriptor) element$iv;
            if (sync.getMaxOutgoing() == 0 || sync.getMaxLength() <= 10 || control == null) {
                die$default(this, "peer iAP2 synchronization has no usable control session", null, 2, null);
                return;
            }
            this.controlSessionId = control.getId();
        }
        this.peerSynchronization = sync;
        this.peerSynchronizationReceived = true;
        this.peerSynchronizationSequence = Integer.valueOf(sequence);
        this.lastReceivedInOrder = sequence;
        this.lastAcknowledgedReceived = Integer.valueOf(sequence);
        if (this.respondingToPeerSynchronization && !confirmsLocalSynchronization) {
            sendSynchronization();
            this.synchronizationDeadlineMillis = Long.valueOf(SYNCHRONIZATION_RESEND_MILLIS + nowMillis);
        } else {
            sendAcknowledgement();
        }
    }

    private final void handleAcknowledgement(int acknowledgement, long nowMillis, boolean confirmsLocalSynchronization) {
        Packet packet;
        Integer previous;
        boolean z;
        if (this.state == State.NEGOTIATING) {
            if (!this.config.getNegotiateControlSession() || (confirmsLocalSynchronization && this.peerSynchronizationReceived)) {
                if (this.peerSynchronizationReceived && peerLimitsAreUsable()) {
                    Iterable $this$any$iv = this.queued;
                    if (!($this$any$iv instanceof Collection) || !((Collection) $this$any$iv).isEmpty()) {
                        Iterator it = $this$any$iv.iterator();
                        while (true) {
                            if (it.hasNext()) {
                                Object element$iv = it.next();
                                Packet it2 = (Packet) element$iv;
                                if (!peerPayloadIsAcceptable(it2.getPayload())) {
                                    z = true;
                                    break;
                                }
                            } else {
                                z = false;
                                break;
                            }
                        }
                    } else {
                        z = false;
                    }
                    if (!z) {
                        this.state = State.NORMAL;
                        this.synchronizationDeadlineMillis = null;
                        setWritable(true);
                    }
                }
                die$default(this, "peer iAP2 maxLength cannot carry pending control packets", null, 2, null);
                return;
            }
            return;
        }
        if (this.config.getMaximumInFlightPackets() != null && this.state == State.NORMAL && (previous = this.lastSentAcknowledged) != null && INSTANCE.sequenceDistance(acknowledgement, previous) > INSTANCE.sequenceDistance(this.sentSequence, previous)) {
            return;
        }
        this.lastSentAcknowledged = Integer.valueOf(acknowledgement);
        boolean rearmed = false;
        while (!this.unacknowledged.isEmpty()) {
            Packet first = (Packet) CollectionsKt.first((List) this.unacknowledged);
            int distance = INSTANCE.sequenceDistance(first.getSequence(), this.lastSentAcknowledged);
            if (distance > 0 && distance <= this.peerSynchronization.getMaxAcknowledgements() + 10) {
                this.receiveAcknowledgementDeadlineMillis = Long.valueOf(first.getDeadlineMillis());
                rearmed = true;
                break;
            }
            this.unacknowledged.remove(0);
        }
        if (!rearmed) {
            this.receiveAcknowledgementDeadlineMillis = null;
        }
        while (INSTANCE.sequenceDistance(this.sentSequence, this.lastSentAcknowledged) < outgoingWindowLimit() && (packet = this.queued.pollFirst()) != null) {
            sendPacket(packet, nowMillis);
            setWritable(true);
        }
    }

    private final void handleExtendedAcknowledgement(byte[] missing) {
        if (this.state != State.NORMAL) {
            return;
        }
        Iterator<Packet> it = this.unacknowledged.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (it.hasNext()) {
            Packet next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            Packet packet = next;
            boolean z = false;
            for (byte element$iv : missing) {
                byte it2 = INSTANCE.u8(element$iv) == packet.getSequence() ? (byte) 1 : (byte) 0;
                if (it2 != 0) {
                    z = true;
                    break;
                }
            }
            if (z) {
                packet.setRetransmissions(packet.getRetransmissions() + 1);
                if (packet.getRetransmissions() == this.peerSynchronization.getMaxRetransmissions()) {
                    die$default(this, "iAP2 packet " + packet.getSequence() + " was not acknowledged", null, 2, null);
                    return;
                } else {
                    sendData(packet.getSequence(), packet.getSessionId(), packet.getPayload());
                    this.sendAcknowledgementDeadlineMillis = null;
                    this.receiveAcknowledgementDeadlineMillis = Long.valueOf(packet.getDeadlineMillis());
                }
            }
        }
    }

    private final void handleData(Packet packet, long nowMillis) {
        boolean z;
        int distance = INSTANCE.sequenceDistance(packet.getSequence(), Integer.valueOf(this.lastReceivedInOrder));
        if (distance == 0 || distance > this.peerSynchronization.getMaxOutgoing() + 10) {
            sendAcknowledgement();
            return;
        }
        Iterable $this$any$iv = this.outOfOrder;
        if (!($this$any$iv instanceof Collection) || !((Collection) $this$any$iv).isEmpty()) {
            Iterator it = $this$any$iv.iterator();
            while (true) {
                if (it.hasNext()) {
                    Object element$iv = it.next();
                    Packet it2 = (Packet) element$iv;
                    if (it2.getSequence() == packet.getSequence()) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            }
        } else {
            z = false;
        }
        if (z) {
            return;
        }
        if (this.outOfOrder.size() >= this.config.getMaximumOutOfOrderPackets()) {
            die$default(this, "iAP2 out-of-order packet limit exceeded", null, 2, null);
            return;
        }
        this.outOfOrder.add(packet);
        if (distance > 1) {
            if (distance >= this.peerSynchronization.getMaxOutgoing()) {
                ArrayList missing = new ArrayList();
                int sequence = this.lastReceivedInOrder;
                while (INSTANCE.sequenceDistance(packet.getSequence(), Integer.valueOf(sequence)) > 1) {
                    sequence = INSTANCE.nextSequence(sequence);
                    missing.add(Integer.valueOf(sequence));
                }
                this.sendAcknowledgementDeadlineMillis = null;
                sendExtendedAcknowledgement(missing);
                return;
            }
            return;
        }
        List $this$sortBy$iv = this.outOfOrder;
        if ($this$sortBy$iv.size() > 1) {
            CollectionsKt.sortWith($this$sortBy$iv, new Comparator() { // from class: com.carplay.iap2.transport.Iap2LinkEngine$handleData$$inlined$sortBy$1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    Iap2LinkEngine.Packet it3 = (Iap2LinkEngine.Packet) t;
                    Iap2LinkEngine.Packet it4 = (Iap2LinkEngine.Packet) t2;
                    return ComparisonsKt.compareValues(Integer.valueOf(Iap2LinkEngine.INSTANCE.sequenceDistance(it3.getSequence(), Integer.valueOf(this.this$0.lastReceivedInOrder))), Integer.valueOf(Iap2LinkEngine.INSTANCE.sequenceDistance(it4.getSequence(), Integer.valueOf(this.this$0.lastReceivedInOrder))));
                }
            });
        }
        while (!this.outOfOrder.isEmpty() && INSTANCE.sequenceDistance(((Packet) CollectionsKt.first((List) this.outOfOrder)).getSequence(), Integer.valueOf(this.lastReceivedInOrder)) == 1) {
            Packet packetRemove = this.outOfOrder.remove(0);
            Intrinsics.checkNotNullExpressionValue(packetRemove, "removeAt(...)");
            Packet inOrder = packetRemove;
            this.lastReceivedInOrder = inOrder.getSequence();
            if (inOrder.getSessionId() == this.controlSessionId) {
                enqueueEvent(new Event.Control(inOrder.getPayload()));
            } else {
                enqueueEvent(new Event.Session(inOrder.getSessionId(), inOrder.getPayload()));
            }
        }
        if (this.peerSynchronization.getMaxAcknowledgements() == 0) {
            return;
        }
        int windowBeforeForcedAck = RangesKt.coerceAtLeast(this.peerSynchronization.getMaxOutgoing() - this.config.getMaxOutgoingDelta(), 1);
        if (INSTANCE.sequenceDistance(this.lastReceivedInOrder, this.lastAcknowledgedReceived) >= windowBeforeForcedAck) {
            this.sendAcknowledgementDeadlineMillis = null;
            this.lastAcknowledgedReceived = Integer.valueOf(this.lastReceivedInOrder);
            sendAcknowledgement();
            return;
        }
        this.sendAcknowledgementDeadlineMillis = Long.valueOf(((long) this.peerSynchronization.getAcknowledgementTimeoutMillis()) + nowMillis);
    }

    private final void sendPacket(Packet packet, long nowMillis) {
        int outstanding = INSTANCE.sequenceDistance(this.sentSequence, this.lastSentAcknowledged);
        boolean full = true;
        if (this.config.getMaximumInFlightPackets() == null) {
            if (outstanding <= this.peerSynchronization.getMaxOutgoing()) {
                full = false;
            }
        } else if (outstanding < outgoingWindowLimit()) {
            full = false;
        }
        if (this.state != State.NORMAL || full) {
            if (this.queued.size() >= this.config.getMaximumQueuedPackets()) {
                die$default(this, "iAP2 outbound queue limit exceeded", null, 2, null);
                return;
            } else {
                this.queued.add(packet);
                setWritable(false);
                return;
            }
        }
        this.sentSequence = INSTANCE.nextSequence(this.sentSequence);
        packet.setSequence(this.sentSequence);
        packet.setRetransmissions(0);
        packet.setDeadlineMillis(((long) this.peerSynchronization.getRetransmissionTimeoutMillis()) + nowMillis);
        this.sendAcknowledgementDeadlineMillis = null;
        sendData(packet.getSequence(), packet.getSessionId(), packet.getPayload());
        this.lastAcknowledgedReceived = Integer.valueOf(this.lastReceivedInOrder);
        if (this.peerSynchronization.getMaxRetransmissions() > 0) {
            this.unacknowledged.add(packet);
            this.receiveAcknowledgementDeadlineMillis = Long.valueOf(packet.getDeadlineMillis());
        } else {
            this.lastSentAcknowledged = Integer.valueOf(this.sentSequence);
        }
    }

    private final int outgoingWindowLimit() {
        Integer maximumInFlightPackets = this.config.getMaximumInFlightPackets();
        if (maximumInFlightPackets != null) {
            int it = maximumInFlightPackets.intValue();
            return Math.min(it, this.peerSynchronization.getMaxOutgoing());
        }
        return this.peerSynchronization.getMaxOutgoing();
    }

    private final void retransmitDuePacket(long nowMillis) {
        Object minElem$iv;
        Long lValueOf;
        if (this.state != State.NORMAL || this.unacknowledged.isEmpty()) {
            return;
        }
        Iterable $this$minByOrNull$iv = this.unacknowledged;
        Iterator iterator$iv = $this$minByOrNull$iv.iterator();
        if (iterator$iv.hasNext()) {
            minElem$iv = iterator$iv.next();
            if (iterator$iv.hasNext()) {
                Packet it = (Packet) minElem$iv;
                long minValue$iv = it.getDeadlineMillis();
                do {
                    Object e$iv = iterator$iv.next();
                    Packet it2 = (Packet) e$iv;
                    long v$iv = it2.getDeadlineMillis();
                    if (minValue$iv > v$iv) {
                        minElem$iv = e$iv;
                        minValue$iv = v$iv;
                    }
                } while (iterator$iv.hasNext());
            }
        } else {
            minElem$iv = null;
        }
        Packet next = (Packet) minElem$iv;
        if (next == null) {
            return;
        }
        Iterable $this$filter$iv = this.unacknowledged;
        Collection destination$iv$iv = new ArrayList();
        Iterator it3 = $this$filter$iv.iterator();
        while (true) {
            if (!it3.hasNext()) {
                break;
            }
            Object element$iv$iv = it3.next();
            Packet it4 = (Packet) element$iv$iv;
            if (it4 != next) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        Iterator it5 = ((List) destination$iv$iv).iterator();
        if (it5.hasNext()) {
            Packet it6 = (Packet) it5.next();
            lValueOf = Long.valueOf(it6.getDeadlineMillis());
            while (it5.hasNext()) {
                Packet it7 = (Packet) it5.next();
                Long lValueOf2 = Long.valueOf(it7.getDeadlineMillis());
                if (lValueOf.compareTo(lValueOf2) > 0) {
                    lValueOf = lValueOf2;
                }
            }
        } else {
            lValueOf = null;
        }
        Long secondDeadline = lValueOf;
        next.setDeadlineMillis(((long) this.peerSynchronization.getRetransmissionTimeoutMillis()) + nowMillis);
        next.setRetransmissions(next.getRetransmissions() + 1);
        if (next.getRetransmissions() == this.peerSynchronization.getMaxRetransmissions()) {
            die$default(this, "iAP2 packet " + next.getSequence() + " was not acknowledged", null, 2, null);
        } else {
            sendData(next.getSequence(), next.getSessionId(), next.getPayload());
            this.receiveAcknowledgementDeadlineMillis = secondDeadline == null ? Long.valueOf(next.getDeadlineMillis()) : secondDeadline;
        }
    }

    private final void sendSynchronization() {
        boolean answering = this.respondingToPeerSynchronization && this.peerSynchronizationReceived;
        int control = (answering ? 64 : 0) | 128;
        writePacket(localSynchronization().encode(), this.sentSequence, control, 0);
        if (this.state != State.DEAD) {
            this.localSynchronizationSequence = Integer.valueOf(this.sentSequence);
        }
    }

    private final void sendAcknowledgement() {
        writePacket(null, this.sentSequence, 64, 0);
    }

    private final void sendExtendedAcknowledgement(List<Integer> missing) {
        List<Integer> $this$map$iv = missing;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (Object item$iv$iv : $this$map$iv) {
            int it = ((Number) item$iv$iv).intValue();
            destination$iv$iv.add(Byte.valueOf((byte) it));
        }
        writePacket(CollectionsKt.toByteArray((List) destination$iv$iv), this.sentSequence, 32, 0);
    }

    private final void sendData(int sequence, int sessionId, byte[] payload) {
        writePacket(payload, sequence, 64, sessionId);
    }

    private final void writePacket(byte[] payload, int sequence, int control, int sessionId) {
        if (this.state == State.DEAD) {
            return;
        }
        this.accumulatedAcknowledgements = 0;
        int length = payload != null ? payload.length + 10 : 9;
        byte[] header = new byte[9];
        header[0] = -1;
        header[1] = LINK_START_LOW;
        header[2] = (byte) (length >>> 8);
        header[3] = (byte) length;
        header[4] = (byte) control;
        header[5] = (byte) sequence;
        header[6] = (byte) this.lastReceivedInOrder;
        header[7] = (byte) sessionId;
        header[8] = (byte) INSTANCE.checksum(header, 8);
        ByteArrayOutputStream frame = new ByteArrayOutputStream(length);
        frame.write(header);
        if (payload != null) {
            frame.write(payload);
            frame.write(INSTANCE.checksum(payload, payload.length));
        }
        byte[] byteArray = frame.toByteArray();
        Intrinsics.checkNotNullExpressionValue(byteArray, "toByteArray(...)");
        if (appendOutput(byteArray)) {
            reportPacket$default(this, "QUEUED", new Header(length, control, sequence, this.lastReceivedInOrder, sessionId), false, 4, null);
        }
    }

    static /* synthetic */ void reportPacket$default(Iap2LinkEngine iap2LinkEngine, String str, Header header, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = true;
        }
        iap2LinkEngine.reportPacket(str, header, z);
    }

    private final void reportPacket(String action, Header header, boolean payloadChecksumValid) {
        Function1<Iap2PacketMetadata, Unit> function1 = this.onPacketMetadata;
        if (function1 == null) {
            return;
        }
        try {
            try {
                function1.invoke(new Iap2PacketMetadata(action, header.getControl(), header.getSequence(), header.getAcknowledgement(), header.getSessionId(), header.getLength(), this.state, payloadChecksumValid));
            } catch (Exception e) {
            }
        } catch (Exception e2) {
        }
    }

    private final void setWritable(boolean value) {
        if (this.writable == value) {
            return;
        }
        this.writable = value;
        enqueueEvent(new Event.Writable(value));
    }

    static /* synthetic */ void die$default(Iap2LinkEngine iap2LinkEngine, String str, byte[] bArr, int i, Object obj) {
        if ((i & 2) != 0) {
            bArr = null;
        }
        iap2LinkEngine.die(str, bArr);
    }

    private final void die(String reason, byte[] restartInput) {
        if (this.state == State.DEAD) {
            return;
        }
        this.markerDeadlineMillis = null;
        this.synchronizationDeadlineMillis = null;
        this.sendAcknowledgementDeadlineMillis = null;
        this.receiveAcknowledgementDeadlineMillis = null;
        this.state = State.DEAD;
        this.writable = false;
        this.output.reset();
        this.events.clear();
        this.events.add(new Event.Dead(reason, restartInput));
    }

    private final boolean appendOutput(byte[] bytes) {
        if (this.state == State.DEAD) {
            return false;
        }
        if (bytes.length > this.config.getMaximumPendingOutputBytes() - this.output.size()) {
            die$default(this, "iAP2 pending output limit exceeded", null, 2, null);
            return false;
        }
        this.output.write(bytes);
        return true;
    }

    private final void enqueueEvent(Event event) {
        if (this.state == State.DEAD) {
            return;
        }
        if (this.events.size() >= this.config.getMaximumPendingEvents()) {
            die$default(this, "iAP2 pending event limit exceeded", null, 2, null);
        } else {
            this.events.add(event);
        }
    }

    private final boolean peerLimitsAreUsable() {
        return this.peerSynchronization.getMaxLength() > 10;
    }

    private final boolean peerPayloadIsAcceptable(byte[] payload) {
        return peerLimitsAreUsable() && payload.length <= (this.peerSynchronization.getMaxLength() + (-9)) - 1;
    }

    private final SynchronizationPayload localSynchronization() {
        SynchronizationPayload synchronizationOverride = this.config.getSynchronizationOverride();
        if (synchronizationOverride == null) {
            int maxOutgoing = this.config.getMaxOutgoing();
            int i = this.config.getZeroAcknowledgements() ? 0 : DEFAULT_RETRANSMISSION_TIMEOUT_MILLIS;
            int acknowledgementTimeoutMillis = this.config.getZeroAcknowledgements() ? 0 : this.config.getAcknowledgementTimeoutMillis();
            int i2 = this.config.getZeroAcknowledgements() ? 0 : 4;
            int i3 = 3;
            if (this.config.getZeroAcknowledgements()) {
                i3 = 0;
            }
            return new SynchronizationPayload(maxOutgoing, 65535, i, acknowledgementTimeoutMillis, i2, i3, CollectionsKt.listOf((Object[]) new SessionDescriptor[]{new SessionDescriptor(10, 0, this.config.getControlSessionVersion()), new SessionDescriptor(11, 2, 1), new SessionDescriptor(12, 1, 2)}));
        }
        return synchronizationOverride;
    }

    /* JADX INFO: compiled from: Iap2LinkEngine.kt */
    @Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0005\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0002\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0006\u0010\f\u001a\u00020\u0007J\u0011\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0007H\u0086\u0002J\u001e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u0007J\u000e\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0007J\u000e\u0010\u0016\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0007J\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0005J\b\u0010\u001a\u001a\u00020\u0011H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\t\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\n\u0010\u000b¨\u0006\u001b"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkEngine$ReceiveBuffer;", "", "<init>", "()V", "bytes", "", "head", "", "tail", "size", "getSize", "()I", "remainingCapacity", "get", "", "index", "append", "", "source", "offset", "count", "discard", "take", "matches", "", "expected", "compact", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class ReceiveBuffer {
        private final byte[] bytes = new byte[65535];
        private int head;
        private int tail;

        public final int getSize() {
            return this.tail - this.head;
        }

        public final int remainingCapacity() {
            compact();
            return this.bytes.length - this.tail;
        }

        public final byte get(int index) {
            return this.bytes[this.head + index];
        }

        public final void append(byte[] source, int offset, int count) {
            Intrinsics.checkNotNullParameter(source, "source");
            if (!(count <= remainingCapacity())) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            ArraysKt.copyInto(source, this.bytes, this.tail, offset, offset + count);
            this.tail += count;
        }

        public final void discard(int count) {
            if (!(count >= 0 && count <= getSize())) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            this.head += count;
            if (this.head == this.tail) {
                this.head = 0;
                this.tail = 0;
            }
        }

        public final byte[] take(int count) {
            boolean z = false;
            if (count >= 0 && count <= getSize()) {
                z = true;
            }
            if (!z) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            byte[] bArrCopyOfRange = ArraysKt.copyOfRange(this.bytes, this.head, this.head + count);
            discard(count);
            return bArrCopyOfRange;
        }

        public final boolean matches(byte[] expected) {
            Intrinsics.checkNotNullParameter(expected, "expected");
            Iterable $this$all$iv = ArraysKt.getIndices(expected);
            if (($this$all$iv instanceof Collection) && ((Collection) $this$all$iv).isEmpty()) {
                return true;
            }
            Iterator it = $this$all$iv.iterator();
            while (it.hasNext()) {
                int element$iv = ((IntIterator) it).nextInt();
                int it2 = this.bytes[this.head + element$iv] == expected[element$iv] ? 1 : 0;
                if (it2 == 0) {
                    return false;
                }
            }
            return true;
        }

        private final void compact() {
            if (this.head == 0) {
                return;
            }
            if (this.head < this.tail) {
                ArraysKt.copyInto(this.bytes, this.bytes, 0, this.head, this.tail);
            }
            this.tail -= this.head;
            this.head = 0;
        }
    }

    /* JADX INFO: compiled from: Iap2LinkEngine.kt */
    @Metadata(d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0010\u0005\n\u0002\b\u0007\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\"\u001a\u0004\u0018\u00010#2\u0006\u0010$\u001a\u00020\tH\u0002J\u0010\u0010%\u001a\u00020&2\u0006\u0010$\u001a\u00020\tH\u0002J\u0018\u0010'\u001a\u00020\u00052\u0006\u0010$\u001a\u00020\t2\u0006\u0010(\u001a\u00020\u0005H\u0002J\u001f\u0010)\u001a\u00020\u00052\u0006\u0010*\u001a\u00020\u00052\b\u0010+\u001a\u0004\u0018\u00010\u0005H\u0002¢\u0006\u0002\u0010,J\u0010\u0010-\u001a\u00020\u00052\u0006\u0010*\u001a\u00020\u0005H\u0002J\u0018\u0010.\u001a\u00020\u00052\u0006\u0010$\u001a\u00020\t2\u0006\u0010/\u001a\u00020\u0005H\u0002J\u0010\u00100\u001a\u00020\u00052\u0006\u00101\u001a\u00020\u0014H\u0002J\u0018\u00102\u001a\u0002032\u0006\u00104\u001a\u0002052\u0006\u00106\u001a\u00020\u0005H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0014X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001cX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u00067"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkEngine$Companion;", "", "<init>", "()V", "CONTROL_SESSION_ID", "", "EA_SESSION_ID", "FILE_TRANSFER_SESSION_ID", "IAP2_MARKER", "", "getIAP2_MARKER", "()[B", "HEADER_BYTES", "CHECKSUM_BYTES", "MAX_WIRE_FRAME_BYTES", "MAX_PAYLOAD_BYTES", "SESSION_DESCRIPTOR_BYTES", "SYNCHRONIZATION_FIXED_BYTES", "SYNCHRONIZATION_VERSION", "LINK_START_HIGH", "", "LINK_START_LOW", "CONTROL_SYNCHRONIZE", "CONTROL_ACKNOWLEDGEMENT", "CONTROL_EXTENDED_ACKNOWLEDGEMENT", "CONTROL_RESET", "INITIAL_SEQUENCE", "MARKER_RESEND_MILLIS", "", "SYNCHRONIZATION_RESEND_MILLIS", "DEFAULT_RETRANSMISSION_TIMEOUT_MILLIS", "DEFAULT_MAX_RETRANSMISSIONS", "DEFAULT_MAX_ACKNOWLEDGEMENTS", "ACK_SLACK", "parseHeader", "Lcom/carplay/iap2/transport/Iap2LinkEngine$Header;", "bytes", "checksumValid", "", "checksum", "count", "sequenceDistance", "sequence", "previous", "(ILjava/lang/Integer;)I", "nextSequence", "readU16", "offset", "u8", "byte", "writeU16", "", "out", "Ljava/io/ByteArrayOutputStream;", "value", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final byte[] getIAP2_MARKER() {
            return Iap2LinkEngine.IAP2_MARKER;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final Header parseHeader(byte[] bytes) {
            if (bytes.length == 9 && checksumValid(bytes) && bytes[0] == -1 && bytes[1] == 90) {
                return new Header(readU16(bytes, 2), u8(bytes[4]), u8(bytes[5]), u8(bytes[6]), u8(bytes[7]));
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean checksumValid(byte[] bytes) {
            int accumulator$iv = 0;
            for (byte element$iv : bytes) {
                int sum = accumulator$iv;
                accumulator$iv = (Iap2LinkEngine.INSTANCE.u8(element$iv) + sum) & 255;
            }
            return accumulator$iv == 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int checksum(byte[] bytes, int count) {
            Iterator<T> it = ArraysKt.take(bytes, count).iterator();
            int iU8 = 0;
            while (it.hasNext()) {
                byte it2 = ((Number) it.next()).byteValue();
                iU8 += Iap2LinkEngine.INSTANCE.u8(it2);
            }
            return (-iU8) & 255;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int sequenceDistance(int sequence, Integer previous) {
            if (previous == null) {
                return 0;
            }
            return (sequence - previous.intValue()) & 255;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int nextSequence(int sequence) {
            return (sequence + 1) & 255;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int readU16(byte[] bytes, int offset) {
            return (u8(bytes[offset]) << 8) | u8(bytes[offset + 1]);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int u8(byte b) {
            return b & 255;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void writeU16(ByteArrayOutputStream out, int value) {
            out.write(value >>> 8);
            out.write(value);
        }
    }
}
