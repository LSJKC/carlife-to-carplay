package com.carplay.airplay;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AirPlayTcpAccepted.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u000f\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0007HÆ\u0003J'\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00052\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001J\t\u0010\u0018\u001a\u00020\u0019HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u001a"}, d2 = {"Lcom/carplay/airplay/AirPlayTcpAccepted;", "", "listener", "Lcom/carplay/airplay/AirPlayListenerIdentity;", "internal", "", "acceptedAtNanos", "", "<init>", "(Lcom/carplay/airplay/AirPlayListenerIdentity;ZJ)V", "getListener", "()Lcom/carplay/airplay/AirPlayListenerIdentity;", "getInternal", "()Z", "getAcceptedAtNanos", "()J", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class AirPlayTcpAccepted {
    private final long acceptedAtNanos;
    private final boolean internal;
    private final AirPlayListenerIdentity listener;

    public static /* synthetic */ AirPlayTcpAccepted copy$default(AirPlayTcpAccepted airPlayTcpAccepted, AirPlayListenerIdentity airPlayListenerIdentity, boolean z, long j, int i, Object obj) {
        if ((i & 1) != 0) {
            airPlayListenerIdentity = airPlayTcpAccepted.listener;
        }
        if ((i & 2) != 0) {
            z = airPlayTcpAccepted.internal;
        }
        if ((i & 4) != 0) {
            j = airPlayTcpAccepted.acceptedAtNanos;
        }
        return airPlayTcpAccepted.copy(airPlayListenerIdentity, z, j);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final AirPlayListenerIdentity getListener() {
        return this.listener;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final boolean getInternal() {
        return this.internal;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final long getAcceptedAtNanos() {
        return this.acceptedAtNanos;
    }

    public final AirPlayTcpAccepted copy(AirPlayListenerIdentity listener, boolean internal, long acceptedAtNanos) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        return new AirPlayTcpAccepted(listener, internal, acceptedAtNanos);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AirPlayTcpAccepted)) {
            return false;
        }
        AirPlayTcpAccepted airPlayTcpAccepted = (AirPlayTcpAccepted) other;
        return Intrinsics.areEqual(this.listener, airPlayTcpAccepted.listener) && this.internal == airPlayTcpAccepted.internal && this.acceptedAtNanos == airPlayTcpAccepted.acceptedAtNanos;
    }

    public int hashCode() {
        return (((this.listener.hashCode() * 31) + Boolean.hashCode(this.internal)) * 31) + Long.hashCode(this.acceptedAtNanos);
    }

    public String toString() {
        return "AirPlayTcpAccepted(listener=" + this.listener + ", internal=" + this.internal + ", acceptedAtNanos=" + this.acceptedAtNanos + ")";
    }

    public AirPlayTcpAccepted(AirPlayListenerIdentity listener, boolean internal, long acceptedAtNanos) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.listener = listener;
        this.internal = internal;
        this.acceptedAtNanos = acceptedAtNanos;
    }

    public final AirPlayListenerIdentity getListener() {
        return this.listener;
    }

    public final boolean getInternal() {
        return this.internal;
    }

    public final long getAcceptedAtNanos() {
        return this.acceptedAtNanos;
    }
}
