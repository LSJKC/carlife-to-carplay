package com.carplay.iap2.transport;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;

/* JADX INFO: compiled from: Iap2LinkEngine.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b-\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0097\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u000e\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0007\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0003¢\u0006\u0004\b\u0014\u0010\u0015J\t\u0010*\u001a\u00020\u0003HÆ\u0003J\t\u0010+\u001a\u00020\u0003HÆ\u0003J\t\u0010,\u001a\u00020\u0003HÆ\u0003J\t\u0010-\u001a\u00020\u0007HÆ\u0003J\t\u0010.\u001a\u00020\u0003HÆ\u0003J\t\u0010/\u001a\u00020\u0003HÆ\u0003J\t\u00100\u001a\u00020\u0003HÆ\u0003J\t\u00101\u001a\u00020\u0003HÆ\u0003J\t\u00102\u001a\u00020\u0003HÆ\u0003J\t\u00103\u001a\u00020\u000eHÆ\u0003J\u000b\u00104\u001a\u0004\u0018\u00010\u0010HÆ\u0003J\t\u00105\u001a\u00020\u0007HÆ\u0003J\t\u00106\u001a\u00020\u0007HÆ\u0003J\u0010\u00107\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010(J\u009e\u0001\u00108\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u00072\b\b\u0002\u0010\u0012\u001a\u00020\u00072\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0003HÆ\u0001¢\u0006\u0002\u00109J\u0013\u0010:\u001a\u00020\u00072\b\u0010;\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010<\u001a\u00020\u0003HÖ\u0001J\t\u0010=\u001a\u00020>HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0017R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0017R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0017R\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0017R\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0017R\u0011\u0010\u000b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0017R\u0011\u0010\f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b \u0010\u0017R\u0011\u0010\r\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u0010¢\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0011\u0010\u0011\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001bR\u0011\u0010\u0012\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001bR\u0015\u0010\u0013\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010)\u001a\u0004\b'\u0010(¨\u0006?"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkConfig;", "", "maxOutgoing", "", "maxOutgoingDelta", "acknowledgementTimeoutMillis", "zeroAcknowledgements", "", "controlSessionVersion", "maximumQueuedPackets", "maximumOutOfOrderPackets", "maximumPendingOutputBytes", "maximumPendingEvents", "startup", "Lcom/carplay/iap2/transport/Iap2LinkStartup;", "synchronizationOverride", "Lcom/carplay/iap2/transport/Iap2LinkEngine$SynchronizationPayload;", "negotiateControlSession", "ignoreRepeatedDetectionMarker", "maximumInFlightPackets", "<init>", "(IIIZIIIIILcom/carplay/iap2/transport/Iap2LinkStartup;Lcom/carplay/iap2/transport/Iap2LinkEngine$SynchronizationPayload;ZZLjava/lang/Integer;)V", "getMaxOutgoing", "()I", "getMaxOutgoingDelta", "getAcknowledgementTimeoutMillis", "getZeroAcknowledgements", "()Z", "getControlSessionVersion", "getMaximumQueuedPackets", "getMaximumOutOfOrderPackets", "getMaximumPendingOutputBytes", "getMaximumPendingEvents", "getStartup", "()Lcom/carplay/iap2/transport/Iap2LinkStartup;", "getSynchronizationOverride", "()Lcom/carplay/iap2/transport/Iap2LinkEngine$SynchronizationPayload;", "getNegotiateControlSession", "getIgnoreRepeatedDetectionMarker", "getMaximumInFlightPackets", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "copy", "(IIIZIIIIILcom/carplay/iap2/transport/Iap2LinkStartup;Lcom/carplay/iap2/transport/Iap2LinkEngine$SynchronizationPayload;ZZLjava/lang/Integer;)Lcom/carplay/iap2/transport/Iap2LinkConfig;", "equals", "other", "hashCode", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class Iap2LinkConfig {
    private final int acknowledgementTimeoutMillis;
    private final int controlSessionVersion;
    private final boolean ignoreRepeatedDetectionMarker;
    private final int maxOutgoing;
    private final int maxOutgoingDelta;
    private final Integer maximumInFlightPackets;
    private final int maximumOutOfOrderPackets;
    private final int maximumPendingEvents;
    private final int maximumPendingOutputBytes;
    private final int maximumQueuedPackets;
    private final boolean negotiateControlSession;
    private final Iap2LinkStartup startup;
    private final Iap2LinkEngine.SynchronizationPayload synchronizationOverride;
    private final boolean zeroAcknowledgements;

    public Iap2LinkConfig() {
        this(0, 0, 0, false, 0, 0, 0, 0, 0, null, null, false, false, null, 16383, null);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getMaxOutgoing() {
        return this.maxOutgoing;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final Iap2LinkStartup getStartup() {
        return this.startup;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final Iap2LinkEngine.SynchronizationPayload getSynchronizationOverride() {
        return this.synchronizationOverride;
    }

    /* JADX INFO: renamed from: component12, reason: from getter */
    public final boolean getNegotiateControlSession() {
        return this.negotiateControlSession;
    }

    /* JADX INFO: renamed from: component13, reason: from getter */
    public final boolean getIgnoreRepeatedDetectionMarker() {
        return this.ignoreRepeatedDetectionMarker;
    }

    /* JADX INFO: renamed from: component14, reason: from getter */
    public final Integer getMaximumInFlightPackets() {
        return this.maximumInFlightPackets;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getMaxOutgoingDelta() {
        return this.maxOutgoingDelta;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int getAcknowledgementTimeoutMillis() {
        return this.acknowledgementTimeoutMillis;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final boolean getZeroAcknowledgements() {
        return this.zeroAcknowledgements;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final int getControlSessionVersion() {
        return this.controlSessionVersion;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final int getMaximumQueuedPackets() {
        return this.maximumQueuedPackets;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final int getMaximumOutOfOrderPackets() {
        return this.maximumOutOfOrderPackets;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final int getMaximumPendingOutputBytes() {
        return this.maximumPendingOutputBytes;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final int getMaximumPendingEvents() {
        return this.maximumPendingEvents;
    }

    public final Iap2LinkConfig copy(int maxOutgoing, int maxOutgoingDelta, int acknowledgementTimeoutMillis, boolean zeroAcknowledgements, int controlSessionVersion, int maximumQueuedPackets, int maximumOutOfOrderPackets, int maximumPendingOutputBytes, int maximumPendingEvents, Iap2LinkStartup startup, Iap2LinkEngine.SynchronizationPayload synchronizationOverride, boolean negotiateControlSession, boolean ignoreRepeatedDetectionMarker, Integer maximumInFlightPackets) {
        Intrinsics.checkNotNullParameter(startup, "startup");
        return new Iap2LinkConfig(maxOutgoing, maxOutgoingDelta, acknowledgementTimeoutMillis, zeroAcknowledgements, controlSessionVersion, maximumQueuedPackets, maximumOutOfOrderPackets, maximumPendingOutputBytes, maximumPendingEvents, startup, synchronizationOverride, negotiateControlSession, ignoreRepeatedDetectionMarker, maximumInFlightPackets);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Iap2LinkConfig)) {
            return false;
        }
        Iap2LinkConfig iap2LinkConfig = (Iap2LinkConfig) other;
        return this.maxOutgoing == iap2LinkConfig.maxOutgoing && this.maxOutgoingDelta == iap2LinkConfig.maxOutgoingDelta && this.acknowledgementTimeoutMillis == iap2LinkConfig.acknowledgementTimeoutMillis && this.zeroAcknowledgements == iap2LinkConfig.zeroAcknowledgements && this.controlSessionVersion == iap2LinkConfig.controlSessionVersion && this.maximumQueuedPackets == iap2LinkConfig.maximumQueuedPackets && this.maximumOutOfOrderPackets == iap2LinkConfig.maximumOutOfOrderPackets && this.maximumPendingOutputBytes == iap2LinkConfig.maximumPendingOutputBytes && this.maximumPendingEvents == iap2LinkConfig.maximumPendingEvents && this.startup == iap2LinkConfig.startup && Intrinsics.areEqual(this.synchronizationOverride, iap2LinkConfig.synchronizationOverride) && this.negotiateControlSession == iap2LinkConfig.negotiateControlSession && this.ignoreRepeatedDetectionMarker == iap2LinkConfig.ignoreRepeatedDetectionMarker && Intrinsics.areEqual(this.maximumInFlightPackets, iap2LinkConfig.maximumInFlightPackets);
    }

    public int hashCode() {
        return (((((((((((((((((((((((((Integer.hashCode(this.maxOutgoing) * 31) + Integer.hashCode(this.maxOutgoingDelta)) * 31) + Integer.hashCode(this.acknowledgementTimeoutMillis)) * 31) + Boolean.hashCode(this.zeroAcknowledgements)) * 31) + Integer.hashCode(this.controlSessionVersion)) * 31) + Integer.hashCode(this.maximumQueuedPackets)) * 31) + Integer.hashCode(this.maximumOutOfOrderPackets)) * 31) + Integer.hashCode(this.maximumPendingOutputBytes)) * 31) + Integer.hashCode(this.maximumPendingEvents)) * 31) + this.startup.hashCode()) * 31) + (this.synchronizationOverride == null ? 0 : this.synchronizationOverride.hashCode())) * 31) + Boolean.hashCode(this.negotiateControlSession)) * 31) + Boolean.hashCode(this.ignoreRepeatedDetectionMarker)) * 31) + (this.maximumInFlightPackets != null ? this.maximumInFlightPackets.hashCode() : 0);
    }

    public String toString() {
        return "Iap2LinkConfig(maxOutgoing=" + this.maxOutgoing + ", maxOutgoingDelta=" + this.maxOutgoingDelta + ", acknowledgementTimeoutMillis=" + this.acknowledgementTimeoutMillis + ", zeroAcknowledgements=" + this.zeroAcknowledgements + ", controlSessionVersion=" + this.controlSessionVersion + ", maximumQueuedPackets=" + this.maximumQueuedPackets + ", maximumOutOfOrderPackets=" + this.maximumOutOfOrderPackets + ", maximumPendingOutputBytes=" + this.maximumPendingOutputBytes + ", maximumPendingEvents=" + this.maximumPendingEvents + ", startup=" + this.startup + ", synchronizationOverride=" + this.synchronizationOverride + ", negotiateControlSession=" + this.negotiateControlSession + ", ignoreRepeatedDetectionMarker=" + this.ignoreRepeatedDetectionMarker + ", maximumInFlightPackets=" + this.maximumInFlightPackets + ")";
    }

    /* JADX WARN: Code duplicated, block: B:64:0x00d4 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:65:0x00d5  */
    public Iap2LinkConfig(int maxOutgoing, int maxOutgoingDelta, int acknowledgementTimeoutMillis, boolean zeroAcknowledgements, int controlSessionVersion, int maximumQueuedPackets, int maximumOutOfOrderPackets, int maximumPendingOutputBytes, int maximumPendingEvents, Iap2LinkStartup startup, Iap2LinkEngine.SynchronizationPayload synchronizationOverride, boolean negotiateControlSession, boolean ignoreRepeatedDetectionMarker, Integer maximumInFlightPackets) {
        boolean z;
        boolean z2;
        Intrinsics.checkNotNullParameter(startup, "startup");
        this.maxOutgoing = maxOutgoing;
        this.maxOutgoingDelta = maxOutgoingDelta;
        this.acknowledgementTimeoutMillis = acknowledgementTimeoutMillis;
        this.zeroAcknowledgements = zeroAcknowledgements;
        this.controlSessionVersion = controlSessionVersion;
        this.maximumQueuedPackets = maximumQueuedPackets;
        this.maximumOutOfOrderPackets = maximumOutOfOrderPackets;
        this.maximumPendingOutputBytes = maximumPendingOutputBytes;
        this.maximumPendingEvents = maximumPendingEvents;
        this.startup = startup;
        this.synchronizationOverride = synchronizationOverride;
        this.negotiateControlSession = negotiateControlSession;
        this.ignoreRepeatedDetectionMarker = ignoreRepeatedDetectionMarker;
        this.maximumInFlightPackets = maximumInFlightPackets;
        int i = this.maxOutgoing;
        if (!(i >= 0 && i < 256)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        int i2 = this.maxOutgoingDelta;
        if (!(i2 >= 0 && i2 < 256)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        int i3 = this.acknowledgementTimeoutMillis;
        if (!(i3 >= 0 && i3 < 65536)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        int i4 = this.controlSessionVersion;
        if (!(i4 >= 0 && i4 < 256)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        int i5 = this.maximumQueuedPackets;
        if (!(1 <= i5 && i5 < 257)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        int i6 = this.maximumOutOfOrderPackets;
        if (!(1 <= i6 && i6 < 257)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (!(this.maximumPendingOutputBytes >= 1)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (!(this.maximumPendingEvents >= 1)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (this.maximumInFlightPackets != null) {
            z = true;
            if (!new IntRange(1, 255).contains(this.maximumInFlightPackets.intValue())) {
                z2 = false;
            }
            if (z2) {
            } else {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
        }
        z = true;
        z2 = z;
        if (z2) {
        } else {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
    }

    public /* synthetic */ Iap2LinkConfig(int i, int i2, int i3, boolean z, int i4, int i5, int i6, int i7, int i8, Iap2LinkStartup iap2LinkStartup, Iap2LinkEngine.SynchronizationPayload synchronizationPayload, boolean z2, boolean z3, Integer num, int i9, DefaultConstructorMarker defaultConstructorMarker) {
        this((i9 & 1) != 0 ? 30 : i, (i9 & 2) != 0 ? 0 : i2, (i9 & 4) != 0 ? 500 : i3, (i9 & 8) != 0 ? false : z, (i9 & 16) != 0 ? 1 : i4, (i9 & 32) != 0 ? 64 : i5, (i9 & 64) == 0 ? i6 : 64, (i9 & 128) != 0 ? 1048576 : i7, (i9 & 256) != 0 ? 256 : i8, (i9 & 512) != 0 ? Iap2LinkStartup.LEGACY : iap2LinkStartup, (i9 & 1024) != 0 ? null : synchronizationPayload, (i9 & 2048) != 0 ? false : z2, (i9 & 4096) == 0 ? z3 : false, (i9 & 8192) == 0 ? num : null);
    }

    public final int getMaxOutgoing() {
        return this.maxOutgoing;
    }

    public final int getMaxOutgoingDelta() {
        return this.maxOutgoingDelta;
    }

    public final int getAcknowledgementTimeoutMillis() {
        return this.acknowledgementTimeoutMillis;
    }

    public final boolean getZeroAcknowledgements() {
        return this.zeroAcknowledgements;
    }

    public final int getControlSessionVersion() {
        return this.controlSessionVersion;
    }

    public final int getMaximumQueuedPackets() {
        return this.maximumQueuedPackets;
    }

    public final int getMaximumOutOfOrderPackets() {
        return this.maximumOutOfOrderPackets;
    }

    public final int getMaximumPendingOutputBytes() {
        return this.maximumPendingOutputBytes;
    }

    public final int getMaximumPendingEvents() {
        return this.maximumPendingEvents;
    }

    public final Iap2LinkStartup getStartup() {
        return this.startup;
    }

    public final Iap2LinkEngine.SynchronizationPayload getSynchronizationOverride() {
        return this.synchronizationOverride;
    }

    public final boolean getNegotiateControlSession() {
        return this.negotiateControlSession;
    }

    public final boolean getIgnoreRepeatedDetectionMarker() {
        return this.ignoreRepeatedDetectionMarker;
    }

    public final Integer getMaximumInFlightPackets() {
        return this.maximumInFlightPackets;
    }
}
