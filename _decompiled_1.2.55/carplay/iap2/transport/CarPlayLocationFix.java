package com.carplay.iap2.transport;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2LocationClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0018\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BS\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\u0004\b\u000b\u0010\fJ\t\u0010\u0019\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001a\u001a\u00020\u0003HÆ\u0003J\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u0011J\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u0011J\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u0011J\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u0011J\u0010\u0010\u001f\u001a\u0004\u0018\u00010\nHÆ\u0003¢\u0006\u0002\u0010\u0017J^\u0010 \u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\nHÆ\u0001¢\u0006\u0002\u0010!J\u0013\u0010\"\u001a\u00020#2\b\u0010$\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010%\u001a\u00020&HÖ\u0001J\t\u0010'\u001a\u00020(HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0015\u0010\u0005\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0010\u0010\u0011R\u0015\u0010\u0006\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0013\u0010\u0011R\u0015\u0010\u0007\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0014\u0010\u0011R\u0015\u0010\b\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0015\u0010\u0011R\u0015\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\n\n\u0002\u0010\u0018\u001a\u0004\b\u0016\u0010\u0017¨\u0006)"}, d2 = {"Lcom/carplay/iap2/transport/CarPlayLocationFix;", "", "latitudeDegrees", "", "longitudeDegrees", "altitudeMeters", "bearingDegrees", "speedMetersPerSecond", "accuracyMeters", "timestampMillis", "", "<init>", "(DDLjava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Long;)V", "getLatitudeDegrees", "()D", "getLongitudeDegrees", "getAltitudeMeters", "()Ljava/lang/Double;", "Ljava/lang/Double;", "getBearingDegrees", "getSpeedMetersPerSecond", "getAccuracyMeters", "getTimestampMillis", "()Ljava/lang/Long;", "Ljava/lang/Long;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "(DDLjava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Double;Ljava/lang/Long;)Lcom/carplay/iap2/transport/CarPlayLocationFix;", "equals", "", "other", "hashCode", "", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class CarPlayLocationFix {
    private final Double accuracyMeters;
    private final Double altitudeMeters;
    private final Double bearingDegrees;
    private final double latitudeDegrees;
    private final double longitudeDegrees;
    private final Double speedMetersPerSecond;
    private final Long timestampMillis;

    public static /* synthetic */ CarPlayLocationFix copy$default(CarPlayLocationFix carPlayLocationFix, double d, double d2, Double d3, Double d4, Double d5, Double d6, Long l, int i, Object obj) {
        if ((i & 1) != 0) {
            d = carPlayLocationFix.latitudeDegrees;
        }
        double d7 = d;
        if ((i & 2) != 0) {
            d2 = carPlayLocationFix.longitudeDegrees;
        }
        double d8 = d2;
        if ((i & 4) != 0) {
            d3 = carPlayLocationFix.altitudeMeters;
        }
        return carPlayLocationFix.copy(d7, d8, d3, (i & 8) != 0 ? carPlayLocationFix.bearingDegrees : d4, (i & 16) != 0 ? carPlayLocationFix.speedMetersPerSecond : d5, (i & 32) != 0 ? carPlayLocationFix.accuracyMeters : d6, (i & 64) != 0 ? carPlayLocationFix.timestampMillis : l);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final double getLatitudeDegrees() {
        return this.latitudeDegrees;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final double getLongitudeDegrees() {
        return this.longitudeDegrees;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final Double getAltitudeMeters() {
        return this.altitudeMeters;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final Double getBearingDegrees() {
        return this.bearingDegrees;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final Double getSpeedMetersPerSecond() {
        return this.speedMetersPerSecond;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final Double getAccuracyMeters() {
        return this.accuracyMeters;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final Long getTimestampMillis() {
        return this.timestampMillis;
    }

    public final CarPlayLocationFix copy(double latitudeDegrees, double longitudeDegrees, Double altitudeMeters, Double bearingDegrees, Double speedMetersPerSecond, Double accuracyMeters, Long timestampMillis) {
        return new CarPlayLocationFix(latitudeDegrees, longitudeDegrees, altitudeMeters, bearingDegrees, speedMetersPerSecond, accuracyMeters, timestampMillis);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CarPlayLocationFix)) {
            return false;
        }
        CarPlayLocationFix carPlayLocationFix = (CarPlayLocationFix) other;
        return Double.compare(this.latitudeDegrees, carPlayLocationFix.latitudeDegrees) == 0 && Double.compare(this.longitudeDegrees, carPlayLocationFix.longitudeDegrees) == 0 && Intrinsics.areEqual((Object) this.altitudeMeters, (Object) carPlayLocationFix.altitudeMeters) && Intrinsics.areEqual((Object) this.bearingDegrees, (Object) carPlayLocationFix.bearingDegrees) && Intrinsics.areEqual((Object) this.speedMetersPerSecond, (Object) carPlayLocationFix.speedMetersPerSecond) && Intrinsics.areEqual((Object) this.accuracyMeters, (Object) carPlayLocationFix.accuracyMeters) && Intrinsics.areEqual(this.timestampMillis, carPlayLocationFix.timestampMillis);
    }

    public int hashCode() {
        return (((((((((((Double.hashCode(this.latitudeDegrees) * 31) + Double.hashCode(this.longitudeDegrees)) * 31) + (this.altitudeMeters == null ? 0 : this.altitudeMeters.hashCode())) * 31) + (this.bearingDegrees == null ? 0 : this.bearingDegrees.hashCode())) * 31) + (this.speedMetersPerSecond == null ? 0 : this.speedMetersPerSecond.hashCode())) * 31) + (this.accuracyMeters == null ? 0 : this.accuracyMeters.hashCode())) * 31) + (this.timestampMillis != null ? this.timestampMillis.hashCode() : 0);
    }

    public String toString() {
        return "CarPlayLocationFix(latitudeDegrees=" + this.latitudeDegrees + ", longitudeDegrees=" + this.longitudeDegrees + ", altitudeMeters=" + this.altitudeMeters + ", bearingDegrees=" + this.bearingDegrees + ", speedMetersPerSecond=" + this.speedMetersPerSecond + ", accuracyMeters=" + this.accuracyMeters + ", timestampMillis=" + this.timestampMillis + ")";
    }

    /* JADX WARN: Code duplicated, block: B:18:0x0057  */
    /* JADX WARN: Code duplicated, block: B:20:0x0061  */
    /* JADX WARN: Code duplicated, block: B:21:0x0063  */
    /* JADX WARN: Code duplicated, block: B:23:0x0066  */
    /* JADX WARN: Code duplicated, block: B:28:0x007c  */
    /* JADX WARN: Code duplicated, block: B:30:0x007f  */
    /* JADX WARN: Code duplicated, block: B:31:0x0081  */
    /* JADX WARN: Code duplicated, block: B:33:0x0085 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:34:0x0086  */
    /* JADX WARN: Code duplicated, block: B:36:0x0094  */
    public CarPlayLocationFix(double latitudeDegrees, double longitudeDegrees, Double altitudeMeters, Double bearingDegrees, Double speedMetersPerSecond, Double accuracyMeters, Long timestampMillis) {
        double d;
        boolean z;
        boolean z2;
        boolean z3;
        double d2;
        boolean z4;
        this.latitudeDegrees = latitudeDegrees;
        this.longitudeDegrees = longitudeDegrees;
        this.altitudeMeters = altitudeMeters;
        this.bearingDegrees = bearingDegrees;
        this.speedMetersPerSecond = speedMetersPerSecond;
        this.accuracyMeters = accuracyMeters;
        this.timestampMillis = timestampMillis;
        if (Math.abs(this.latitudeDegrees) <= Double.MAX_VALUE) {
            d = Double.MAX_VALUE;
            double d3 = this.latitudeDegrees;
            z = ((-90.0d) > d3 ? 1 : ((-90.0d) == d3 ? 0 : -1)) <= 0 && (d3 > 90.0d ? 1 : (d3 == 90.0d ? 0 : -1)) <= 0 ? true : z;
            if (z) {
                throw new IllegalArgumentException("latitudeDegrees must be between -90 and 90".toString());
            }
            if (Math.abs(this.longitudeDegrees) <= d) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                d2 = this.longitudeDegrees;
                if (-180.0d <= d2 || d2 > 180.0d) {
                    z4 = false;
                } else {
                    z4 = true;
                }
                if (z4) {
                    z3 = true;
                } else {
                    z3 = false;
                }
            } else {
                z3 = false;
            }
            if (z3) {
            } else {
                throw new IllegalArgumentException("longitudeDegrees must be between -180 and 180".toString());
            }
        }
        d = Double.MAX_VALUE;
        z = false;
        if (z) {
            throw new IllegalArgumentException("latitudeDegrees must be between -90 and 90".toString());
        }
        if (Math.abs(this.longitudeDegrees) <= d) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2) {
            d2 = this.longitudeDegrees;
            if (-180.0d <= d2) {
                z4 = false;
            } else {
                z4 = false;
            }
            if (z4) {
                z3 = true;
            } else {
                z3 = false;
            }
        } else {
            z3 = false;
        }
        if (z3) {
        } else {
            throw new IllegalArgumentException("longitudeDegrees must be between -180 and 180".toString());
        }
    }

    public /* synthetic */ CarPlayLocationFix(double d, double d2, Double d3, Double d4, Double d5, Double d6, Long l, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(d, d2, (i & 4) != 0 ? null : d3, (i & 8) != 0 ? null : d4, (i & 16) != 0 ? null : d5, (i & 32) != 0 ? null : d6, (i & 64) != 0 ? null : l);
    }

    public final double getLatitudeDegrees() {
        return this.latitudeDegrees;
    }

    public final double getLongitudeDegrees() {
        return this.longitudeDegrees;
    }

    public final Double getAltitudeMeters() {
        return this.altitudeMeters;
    }

    public final Double getBearingDegrees() {
        return this.bearingDegrees;
    }

    public final Double getSpeedMetersPerSecond() {
        return this.speedMetersPerSecond;
    }

    public final Double getAccuracyMeters() {
        return this.accuracyMeters;
    }

    public final Long getTimestampMillis() {
        return this.timestampMillis;
    }
}
