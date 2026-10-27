package com.carplay.iap2.transport;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2VehicleStatus.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\t\n\u0002\b\u001e\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BE\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t\u0012\b\u0010\n\u001a\u0004\u0018\u00010\t\u0012\u0006\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0005¢\u0006\u0004\b\r\u0010\u000eJ\t\u0010\u001b\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001d\u001a\u00020\u0007HÆ\u0003J\u0010\u0010\u001e\u001a\u0004\u0018\u00010\tHÆ\u0003¢\u0006\u0002\u0010\u0016J\u0010\u0010\u001f\u001a\u0004\u0018\u00010\tHÆ\u0003¢\u0006\u0002\u0010\u0016J\t\u0010 \u001a\u00020\u0003HÆ\u0003J\t\u0010!\u001a\u00020\u0005HÆ\u0003JX\u0010\"\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\t2\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u0005HÆ\u0001¢\u0006\u0002\u0010#J\u0013\u0010$\u001a\u00020\u00052\b\u0010%\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010&\u001a\u00020\u0003HÖ\u0001J\t\u0010'\u001a\u00020(HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0015\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\n\n\u0002\u0010\u0017\u001a\u0004\b\u0015\u0010\u0016R\u0015\u0010\n\u001a\u0004\u0018\u00010\t¢\u0006\n\n\u0002\u0010\u0017\u001a\u0004\b\u0018\u0010\u0016R\u0011\u0010\u000b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0010R\u0011\u0010\f\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0012¨\u0006)"}, d2 = {"Lcom/carplay/iap2/transport/VehicleStatusSnapshot;", "", "rangeKm", "", "rangeWarning", "", "batteryPercent", "", "currentChargeWh", "", "maxChargeWh", "maxRangeKm", "charging", "<init>", "(IZDLjava/lang/Long;Ljava/lang/Long;IZ)V", "getRangeKm", "()I", "getRangeWarning", "()Z", "getBatteryPercent", "()D", "getCurrentChargeWh", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getMaxChargeWh", "getMaxRangeKm", "getCharging", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "(IZDLjava/lang/Long;Ljava/lang/Long;IZ)Lcom/carplay/iap2/transport/VehicleStatusSnapshot;", "equals", "other", "hashCode", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class VehicleStatusSnapshot {
    private final double batteryPercent;
    private final boolean charging;
    private final Long currentChargeWh;
    private final Long maxChargeWh;
    private final int maxRangeKm;
    private final int rangeKm;
    private final boolean rangeWarning;

    public static /* synthetic */ VehicleStatusSnapshot copy$default(VehicleStatusSnapshot vehicleStatusSnapshot, int i, boolean z, double d, Long l, Long l2, int i2, boolean z2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = vehicleStatusSnapshot.rangeKm;
        }
        if ((i3 & 2) != 0) {
            z = vehicleStatusSnapshot.rangeWarning;
        }
        if ((i3 & 4) != 0) {
            d = vehicleStatusSnapshot.batteryPercent;
        }
        if ((i3 & 8) != 0) {
            l = vehicleStatusSnapshot.currentChargeWh;
        }
        if ((i3 & 16) != 0) {
            l2 = vehicleStatusSnapshot.maxChargeWh;
        }
        if ((i3 & 32) != 0) {
            i2 = vehicleStatusSnapshot.maxRangeKm;
        }
        if ((i3 & 64) != 0) {
            z2 = vehicleStatusSnapshot.charging;
        }
        double d2 = d;
        return vehicleStatusSnapshot.copy(i, z, d2, l, l2, i2, z2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getRangeKm() {
        return this.rangeKm;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final boolean getRangeWarning() {
        return this.rangeWarning;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final double getBatteryPercent() {
        return this.batteryPercent;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final Long getCurrentChargeWh() {
        return this.currentChargeWh;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final Long getMaxChargeWh() {
        return this.maxChargeWh;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final int getMaxRangeKm() {
        return this.maxRangeKm;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final boolean getCharging() {
        return this.charging;
    }

    public final VehicleStatusSnapshot copy(int rangeKm, boolean rangeWarning, double batteryPercent, Long currentChargeWh, Long maxChargeWh, int maxRangeKm, boolean charging) {
        return new VehicleStatusSnapshot(rangeKm, rangeWarning, batteryPercent, currentChargeWh, maxChargeWh, maxRangeKm, charging);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof VehicleStatusSnapshot)) {
            return false;
        }
        VehicleStatusSnapshot vehicleStatusSnapshot = (VehicleStatusSnapshot) other;
        return this.rangeKm == vehicleStatusSnapshot.rangeKm && this.rangeWarning == vehicleStatusSnapshot.rangeWarning && Double.compare(this.batteryPercent, vehicleStatusSnapshot.batteryPercent) == 0 && Intrinsics.areEqual(this.currentChargeWh, vehicleStatusSnapshot.currentChargeWh) && Intrinsics.areEqual(this.maxChargeWh, vehicleStatusSnapshot.maxChargeWh) && this.maxRangeKm == vehicleStatusSnapshot.maxRangeKm && this.charging == vehicleStatusSnapshot.charging;
    }

    public int hashCode() {
        return (((((((((((Integer.hashCode(this.rangeKm) * 31) + Boolean.hashCode(this.rangeWarning)) * 31) + Double.hashCode(this.batteryPercent)) * 31) + (this.currentChargeWh == null ? 0 : this.currentChargeWh.hashCode())) * 31) + (this.maxChargeWh != null ? this.maxChargeWh.hashCode() : 0)) * 31) + Integer.hashCode(this.maxRangeKm)) * 31) + Boolean.hashCode(this.charging);
    }

    public String toString() {
        return "VehicleStatusSnapshot(rangeKm=" + this.rangeKm + ", rangeWarning=" + this.rangeWarning + ", batteryPercent=" + this.batteryPercent + ", currentChargeWh=" + this.currentChargeWh + ", maxChargeWh=" + this.maxChargeWh + ", maxRangeKm=" + this.maxRangeKm + ", charging=" + this.charging + ")";
    }

    public VehicleStatusSnapshot(int rangeKm, boolean rangeWarning, double batteryPercent, Long currentChargeWh, Long maxChargeWh, int maxRangeKm, boolean charging) {
        this.rangeKm = rangeKm;
        this.rangeWarning = rangeWarning;
        this.batteryPercent = batteryPercent;
        this.currentChargeWh = currentChargeWh;
        this.maxChargeWh = maxChargeWh;
        this.maxRangeKm = maxRangeKm;
        this.charging = charging;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ VehicleStatusSnapshot(int i, boolean z, double d, Long l, Long l2, int i2, boolean z2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        boolean z3;
        if ((i3 & 64) == 0) {
            z3 = z2;
        } else {
            z3 = false;
        }
        this(i, z, d, l, l2, i2, z3);
    }

    public final int getRangeKm() {
        return this.rangeKm;
    }

    public final boolean getRangeWarning() {
        return this.rangeWarning;
    }

    public final double getBatteryPercent() {
        return this.batteryPercent;
    }

    public final Long getCurrentChargeWh() {
        return this.currentChargeWh;
    }

    public final Long getMaxChargeWh() {
        return this.maxChargeWh;
    }

    public final int getMaxRangeKm() {
        return this.maxRangeKm;
    }

    public final boolean getCharging() {
        return this.charging;
    }
}
