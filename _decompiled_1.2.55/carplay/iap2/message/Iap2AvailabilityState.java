package com.carplay.iap2.message;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2Messages.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000e\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u001b\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u0010\u0010\r\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\tJ\u000b\u0010\u000e\u001a\u0004\u0018\u00010\u0005HÆ\u0003J&\u0010\u000f\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005HÆ\u0001¢\u0006\u0002\u0010\u0010J\u0013\u0010\u0011\u001a\u00020\u00032\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0005HÖ\u0001R\u0015\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\n\u001a\u0004\b\b\u0010\tR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u0016"}, d2 = {"Lcom/carplay/iap2/message/Iap2AvailabilityState;", "", "available", "", "identifier", "", "<init>", "(Ljava/lang/Boolean;Ljava/lang/String;)V", "getAvailable", "()Ljava/lang/Boolean;", "Ljava/lang/Boolean;", "getIdentifier", "()Ljava/lang/String;", "component1", "component2", "copy", "(Ljava/lang/Boolean;Ljava/lang/String;)Lcom/carplay/iap2/message/Iap2AvailabilityState;", "equals", "other", "hashCode", "", "toString", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class Iap2AvailabilityState {
    private final Boolean available;
    private final String identifier;

    public static /* synthetic */ Iap2AvailabilityState copy$default(Iap2AvailabilityState iap2AvailabilityState, Boolean bool, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            bool = iap2AvailabilityState.available;
        }
        if ((i & 2) != 0) {
            str = iap2AvailabilityState.identifier;
        }
        return iap2AvailabilityState.copy(bool, str);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final Boolean getAvailable() {
        return this.available;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getIdentifier() {
        return this.identifier;
    }

    public final Iap2AvailabilityState copy(Boolean available, String identifier) {
        return new Iap2AvailabilityState(available, identifier);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Iap2AvailabilityState)) {
            return false;
        }
        Iap2AvailabilityState iap2AvailabilityState = (Iap2AvailabilityState) other;
        return Intrinsics.areEqual(this.available, iap2AvailabilityState.available) && Intrinsics.areEqual(this.identifier, iap2AvailabilityState.identifier);
    }

    public int hashCode() {
        return ((this.available == null ? 0 : this.available.hashCode()) * 31) + (this.identifier != null ? this.identifier.hashCode() : 0);
    }

    public String toString() {
        return "Iap2AvailabilityState(available=" + this.available + ", identifier=" + this.identifier + ")";
    }

    public Iap2AvailabilityState(Boolean available, String identifier) {
        this.available = available;
        this.identifier = identifier;
    }

    public final Boolean getAvailable() {
        return this.available;
    }

    public final String getIdentifier() {
        return this.identifier;
    }
}
