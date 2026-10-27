package com.carlink.cast;

import kotlin.Metadata;

/* JADX INFO: compiled from: CastLog.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0006\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005¢\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0005HÆ\u0003J'\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\f¨\u0006\u0019"}, d2 = {"Lcom/carlink/cast/CastMetric;", "", "elapsedMillis", "", "fps", "", "kbps", "<init>", "(JDD)V", "getElapsedMillis", "()J", "getFps", "()D", "getKbps", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class CastMetric {
    private final long elapsedMillis;
    private final double fps;
    private final double kbps;

    public static /* synthetic */ CastMetric copy$default(CastMetric castMetric, long j, double d, double d2, int i, Object obj) {
        if ((i & 1) != 0) {
            j = castMetric.elapsedMillis;
        }
        long j2 = j;
        if ((i & 2) != 0) {
            d = castMetric.fps;
        }
        double d3 = d;
        if ((i & 4) != 0) {
            d2 = castMetric.kbps;
        }
        return castMetric.copy(j2, d3, d2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final long getElapsedMillis() {
        return this.elapsedMillis;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final double getFps() {
        return this.fps;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final double getKbps() {
        return this.kbps;
    }

    public final CastMetric copy(long elapsedMillis, double fps, double kbps) {
        return new CastMetric(elapsedMillis, fps, kbps);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CastMetric)) {
            return false;
        }
        CastMetric castMetric = (CastMetric) other;
        return this.elapsedMillis == castMetric.elapsedMillis && Double.compare(this.fps, castMetric.fps) == 0 && Double.compare(this.kbps, castMetric.kbps) == 0;
    }

    public int hashCode() {
        return (((Long.hashCode(this.elapsedMillis) * 31) + Double.hashCode(this.fps)) * 31) + Double.hashCode(this.kbps);
    }

    public String toString() {
        return "CastMetric(elapsedMillis=" + this.elapsedMillis + ", fps=" + this.fps + ", kbps=" + this.kbps + ")";
    }

    public CastMetric(long elapsedMillis, double fps, double kbps) {
        this.elapsedMillis = elapsedMillis;
        this.fps = fps;
        this.kbps = kbps;
    }

    public final long getElapsedMillis() {
        return this.elapsedMillis;
    }

    public final double getFps() {
        return this.fps;
    }

    public final double getKbps() {
        return this.kbps;
    }
}
