package com.carplay.iap2.message;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2Messages.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B%\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003¢\u0006\u0004\b\u0006\u0010\u0007J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\r\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u000e\u001a\u0004\u0018\u00010\u0003HÆ\u0003J-\u0010\u000f\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\t¨\u0006\u0017"}, d2 = {"Lcom/carplay/iap2/message/Iap2CarPlayAvailability;", "", "wired", "Lcom/carplay/iap2/message/Iap2AvailabilityState;", "wireless", "themeAssets", "<init>", "(Lcom/carplay/iap2/message/Iap2AvailabilityState;Lcom/carplay/iap2/message/Iap2AvailabilityState;Lcom/carplay/iap2/message/Iap2AvailabilityState;)V", "getWired", "()Lcom/carplay/iap2/message/Iap2AvailabilityState;", "getWireless", "getThemeAssets", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class Iap2CarPlayAvailability {
    private final Iap2AvailabilityState themeAssets;
    private final Iap2AvailabilityState wired;
    private final Iap2AvailabilityState wireless;

    public static /* synthetic */ Iap2CarPlayAvailability copy$default(Iap2CarPlayAvailability iap2CarPlayAvailability, Iap2AvailabilityState iap2AvailabilityState, Iap2AvailabilityState iap2AvailabilityState2, Iap2AvailabilityState iap2AvailabilityState3, int i, Object obj) {
        if ((i & 1) != 0) {
            iap2AvailabilityState = iap2CarPlayAvailability.wired;
        }
        if ((i & 2) != 0) {
            iap2AvailabilityState2 = iap2CarPlayAvailability.wireless;
        }
        if ((i & 4) != 0) {
            iap2AvailabilityState3 = iap2CarPlayAvailability.themeAssets;
        }
        return iap2CarPlayAvailability.copy(iap2AvailabilityState, iap2AvailabilityState2, iap2AvailabilityState3);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final Iap2AvailabilityState getWired() {
        return this.wired;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final Iap2AvailabilityState getWireless() {
        return this.wireless;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final Iap2AvailabilityState getThemeAssets() {
        return this.themeAssets;
    }

    public final Iap2CarPlayAvailability copy(Iap2AvailabilityState wired, Iap2AvailabilityState wireless, Iap2AvailabilityState themeAssets) {
        return new Iap2CarPlayAvailability(wired, wireless, themeAssets);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Iap2CarPlayAvailability)) {
            return false;
        }
        Iap2CarPlayAvailability iap2CarPlayAvailability = (Iap2CarPlayAvailability) other;
        return Intrinsics.areEqual(this.wired, iap2CarPlayAvailability.wired) && Intrinsics.areEqual(this.wireless, iap2CarPlayAvailability.wireless) && Intrinsics.areEqual(this.themeAssets, iap2CarPlayAvailability.themeAssets);
    }

    public int hashCode() {
        return ((((this.wired == null ? 0 : this.wired.hashCode()) * 31) + (this.wireless == null ? 0 : this.wireless.hashCode())) * 31) + (this.themeAssets != null ? this.themeAssets.hashCode() : 0);
    }

    public String toString() {
        return "Iap2CarPlayAvailability(wired=" + this.wired + ", wireless=" + this.wireless + ", themeAssets=" + this.themeAssets + ")";
    }

    public Iap2CarPlayAvailability(Iap2AvailabilityState wired, Iap2AvailabilityState wireless, Iap2AvailabilityState themeAssets) {
        this.wired = wired;
        this.wireless = wireless;
        this.themeAssets = themeAssets;
    }

    public final Iap2AvailabilityState getWired() {
        return this.wired;
    }

    public final Iap2AvailabilityState getWireless() {
        return this.wireless;
    }

    public final Iap2AvailabilityState getThemeAssets() {
        return this.themeAssets;
    }
}
