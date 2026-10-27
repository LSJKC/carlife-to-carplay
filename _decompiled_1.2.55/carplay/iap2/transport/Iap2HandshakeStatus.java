package com.carplay.iap2.transport;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PhoneBluetoothProfile.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0014\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B=\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\u0004\b\u000b\u0010\fJ\t\u0010\u0017\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0019\u001a\u00020\u0005HÆ\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\bHÆ\u0003J\u0010\u0010\u001b\u001a\u0004\u0018\u00010\nHÆ\u0003¢\u0006\u0002\u0010\u0015JD\u0010\u001c\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\nHÆ\u0001¢\u0006\u0002\u0010\u001dJ\u0013\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010!\u001a\u00020\nHÖ\u0001J\t\u0010\"\u001a\u00020#HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0015\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\n\n\u0002\u0010\u0016\u001a\u0004\b\u0014\u0010\u0015¨\u0006$"}, d2 = {"Lcom/carplay/iap2/transport/Iap2HandshakeStatus;", "", "state", "Lcom/carplay/iap2/transport/Iap2LinkEngine$State;", "sentBytes", "", "receivedBytes", "peerSynchronization", "Lcom/carplay/iap2/transport/Iap2LinkEngine$SynchronizationPayload;", "controlSessionId", "", "<init>", "(Lcom/carplay/iap2/transport/Iap2LinkEngine$State;JJLcom/carplay/iap2/transport/Iap2LinkEngine$SynchronizationPayload;Ljava/lang/Integer;)V", "getState", "()Lcom/carplay/iap2/transport/Iap2LinkEngine$State;", "getSentBytes", "()J", "getReceivedBytes", "getPeerSynchronization", "()Lcom/carplay/iap2/transport/Iap2LinkEngine$SynchronizationPayload;", "getControlSessionId", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "component1", "component2", "component3", "component4", "component5", "copy", "(Lcom/carplay/iap2/transport/Iap2LinkEngine$State;JJLcom/carplay/iap2/transport/Iap2LinkEngine$SynchronizationPayload;Ljava/lang/Integer;)Lcom/carplay/iap2/transport/Iap2HandshakeStatus;", "equals", "", "other", "hashCode", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class Iap2HandshakeStatus {
    private final Integer controlSessionId;
    private final Iap2LinkEngine.SynchronizationPayload peerSynchronization;
    private final long receivedBytes;
    private final long sentBytes;
    private final Iap2LinkEngine.State state;

    public Iap2HandshakeStatus() {
        this(null, 0L, 0L, null, null, 31, null);
    }

    public static /* synthetic */ Iap2HandshakeStatus copy$default(Iap2HandshakeStatus iap2HandshakeStatus, Iap2LinkEngine.State state, long j, long j2, Iap2LinkEngine.SynchronizationPayload synchronizationPayload, Integer num, int i, Object obj) {
        if ((i & 1) != 0) {
            state = iap2HandshakeStatus.state;
        }
        if ((i & 2) != 0) {
            j = iap2HandshakeStatus.sentBytes;
        }
        if ((i & 4) != 0) {
            j2 = iap2HandshakeStatus.receivedBytes;
        }
        if ((i & 8) != 0) {
            synchronizationPayload = iap2HandshakeStatus.peerSynchronization;
        }
        if ((i & 16) != 0) {
            num = iap2HandshakeStatus.controlSessionId;
        }
        long j3 = j2;
        return iap2HandshakeStatus.copy(state, j, j3, synchronizationPayload, num);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final Iap2LinkEngine.State getState() {
        return this.state;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final long getSentBytes() {
        return this.sentBytes;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final long getReceivedBytes() {
        return this.receivedBytes;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final Iap2LinkEngine.SynchronizationPayload getPeerSynchronization() {
        return this.peerSynchronization;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final Integer getControlSessionId() {
        return this.controlSessionId;
    }

    public final Iap2HandshakeStatus copy(Iap2LinkEngine.State state, long sentBytes, long receivedBytes, Iap2LinkEngine.SynchronizationPayload peerSynchronization, Integer controlSessionId) {
        Intrinsics.checkNotNullParameter(state, "state");
        return new Iap2HandshakeStatus(state, sentBytes, receivedBytes, peerSynchronization, controlSessionId);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Iap2HandshakeStatus)) {
            return false;
        }
        Iap2HandshakeStatus iap2HandshakeStatus = (Iap2HandshakeStatus) other;
        return this.state == iap2HandshakeStatus.state && this.sentBytes == iap2HandshakeStatus.sentBytes && this.receivedBytes == iap2HandshakeStatus.receivedBytes && Intrinsics.areEqual(this.peerSynchronization, iap2HandshakeStatus.peerSynchronization) && Intrinsics.areEqual(this.controlSessionId, iap2HandshakeStatus.controlSessionId);
    }

    public int hashCode() {
        return (((((((this.state.hashCode() * 31) + Long.hashCode(this.sentBytes)) * 31) + Long.hashCode(this.receivedBytes)) * 31) + (this.peerSynchronization == null ? 0 : this.peerSynchronization.hashCode())) * 31) + (this.controlSessionId != null ? this.controlSessionId.hashCode() : 0);
    }

    public String toString() {
        return "Iap2HandshakeStatus(state=" + this.state + ", sentBytes=" + this.sentBytes + ", receivedBytes=" + this.receivedBytes + ", peerSynchronization=" + this.peerSynchronization + ", controlSessionId=" + this.controlSessionId + ")";
    }

    public Iap2HandshakeStatus(Iap2LinkEngine.State state, long sentBytes, long receivedBytes, Iap2LinkEngine.SynchronizationPayload peerSynchronization, Integer controlSessionId) {
        Intrinsics.checkNotNullParameter(state, "state");
        this.state = state;
        this.sentBytes = sentBytes;
        this.receivedBytes = receivedBytes;
        this.peerSynchronization = peerSynchronization;
        this.controlSessionId = controlSessionId;
    }

    public /* synthetic */ Iap2HandshakeStatus(Iap2LinkEngine.State state, long j, long j2, Iap2LinkEngine.SynchronizationPayload synchronizationPayload, Integer num, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? Iap2LinkEngine.State.IDLE : state, (i & 2) != 0 ? 0L : j, (i & 4) != 0 ? 0L : j2, (i & 8) != 0 ? null : synchronizationPayload, (i & 16) != 0 ? null : num);
    }

    public final Iap2LinkEngine.State getState() {
        return this.state;
    }

    public final long getSentBytes() {
        return this.sentBytes;
    }

    public final long getReceivedBytes() {
        return this.receivedBytes;
    }

    public final Iap2LinkEngine.SynchronizationPayload getPeerSynchronization() {
        return this.peerSynchronization;
    }

    public final Integer getControlSessionId() {
        return this.controlSessionId;
    }
}
