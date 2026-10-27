package com.carplay.airplay;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AirPlayConfig.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0016\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B7\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0003HÆ\u0003J\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u0010JB\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003HÆ\u0001¢\u0006\u0002\u0010\u0018J\u0013\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001c\u001a\u00020\u0003HÖ\u0001J\t\u0010\u001d\u001a\u00020\u001eHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000bR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000bR\u0015\u0010\u0007\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\u0011\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u001f"}, d2 = {"Lcom/carplay/airplay/AirPlayViewArea;", "", "width", "", "height", "originX", "originY", "dockEdge", "<init>", "(IIIILjava/lang/Integer;)V", "getWidth", "()I", "getHeight", "getOriginX", "getOriginY", "getDockEdge", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "component1", "component2", "component3", "component4", "component5", "copy", "(IIIILjava/lang/Integer;)Lcom/carplay/airplay/AirPlayViewArea;", "equals", "", "other", "hashCode", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class AirPlayViewArea {
    private final Integer dockEdge;
    private final int height;
    private final int originX;
    private final int originY;
    private final int width;

    public static /* synthetic */ AirPlayViewArea copy$default(AirPlayViewArea airPlayViewArea, int i, int i2, int i3, int i4, Integer num, int i5, Object obj) {
        if ((i5 & 1) != 0) {
            i = airPlayViewArea.width;
        }
        if ((i5 & 2) != 0) {
            i2 = airPlayViewArea.height;
        }
        if ((i5 & 4) != 0) {
            i3 = airPlayViewArea.originX;
        }
        if ((i5 & 8) != 0) {
            i4 = airPlayViewArea.originY;
        }
        if ((i5 & 16) != 0) {
            num = airPlayViewArea.dockEdge;
        }
        Integer num2 = num;
        int i6 = i3;
        return airPlayViewArea.copy(i, i2, i6, i4, num2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getWidth() {
        return this.width;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getHeight() {
        return this.height;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int getOriginX() {
        return this.originX;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int getOriginY() {
        return this.originY;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final Integer getDockEdge() {
        return this.dockEdge;
    }

    public final AirPlayViewArea copy(int width, int height, int originX, int originY, Integer dockEdge) {
        return new AirPlayViewArea(width, height, originX, originY, dockEdge);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AirPlayViewArea)) {
            return false;
        }
        AirPlayViewArea airPlayViewArea = (AirPlayViewArea) other;
        return this.width == airPlayViewArea.width && this.height == airPlayViewArea.height && this.originX == airPlayViewArea.originX && this.originY == airPlayViewArea.originY && Intrinsics.areEqual(this.dockEdge, airPlayViewArea.dockEdge);
    }

    public int hashCode() {
        return (((((((Integer.hashCode(this.width) * 31) + Integer.hashCode(this.height)) * 31) + Integer.hashCode(this.originX)) * 31) + Integer.hashCode(this.originY)) * 31) + (this.dockEdge == null ? 0 : this.dockEdge.hashCode());
    }

    public String toString() {
        return "AirPlayViewArea(width=" + this.width + ", height=" + this.height + ", originX=" + this.originX + ", originY=" + this.originY + ", dockEdge=" + this.dockEdge + ")";
    }

    public AirPlayViewArea(int width, int height, int originX, int originY, Integer dockEdge) {
        this.width = width;
        this.height = height;
        this.originX = originX;
        this.originY = originY;
        this.dockEdge = dockEdge;
    }

    public /* synthetic */ AirPlayViewArea(int i, int i2, int i3, int i4, Integer num, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, i2, (i5 & 4) != 0 ? 0 : i3, (i5 & 8) != 0 ? 0 : i4, (i5 & 16) != 0 ? null : num);
    }

    public final int getWidth() {
        return this.width;
    }

    public final int getHeight() {
        return this.height;
    }

    public final int getOriginX() {
        return this.originX;
    }

    public final int getOriginY() {
        return this.originY;
    }

    public final Integer getDockEdge() {
        return this.dockEdge;
    }
}
