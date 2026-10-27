package com.carplay.sender;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: HidInput.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0018\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B;\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003¢\u0006\u0004\b\u000b\u0010\fJ\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0019\u001a\u00020\bHÆ\u0003J\t\u0010\u001a\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001b\u001a\u00020\u0003HÆ\u0003JE\u0010\u001c\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u001d\u001a\u00020\b2\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001f\u001a\u00020\u0003HÖ\u0001J\t\u0010 \u001a\u00020!HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000eR\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000e¨\u0006\""}, d2 = {"Lcom/carplay/sender/TouchSample;", "", "id", "", "x", "", "y", "down", "", "maxX", "maxY", "<init>", "(IFFZII)V", "getId", "()I", "getX", "()F", "getY", "getDown", "()Z", "getMaxX", "getMaxY", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class TouchSample {
    private final boolean down;
    private final int id;
    private final int maxX;
    private final int maxY;
    private final float x;
    private final float y;

    public static /* synthetic */ TouchSample copy$default(TouchSample touchSample, int i, float f, float f2, boolean z, int i2, int i3, int i4, Object obj) {
        if ((i4 & 1) != 0) {
            i = touchSample.id;
        }
        if ((i4 & 2) != 0) {
            f = touchSample.x;
        }
        if ((i4 & 4) != 0) {
            f2 = touchSample.y;
        }
        if ((i4 & 8) != 0) {
            z = touchSample.down;
        }
        if ((i4 & 16) != 0) {
            i2 = touchSample.maxX;
        }
        if ((i4 & 32) != 0) {
            i3 = touchSample.maxY;
        }
        int i5 = i2;
        int i6 = i3;
        return touchSample.copy(i, f, f2, z, i5, i6);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getId() {
        return this.id;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float getX() {
        return this.x;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final float getY() {
        return this.y;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final boolean getDown() {
        return this.down;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final int getMaxX() {
        return this.maxX;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final int getMaxY() {
        return this.maxY;
    }

    public final TouchSample copy(int id, float x, float y, boolean down, int maxX, int maxY) {
        return new TouchSample(id, x, y, down, maxX, maxY);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TouchSample)) {
            return false;
        }
        TouchSample touchSample = (TouchSample) other;
        return this.id == touchSample.id && Float.compare(this.x, touchSample.x) == 0 && Float.compare(this.y, touchSample.y) == 0 && this.down == touchSample.down && this.maxX == touchSample.maxX && this.maxY == touchSample.maxY;
    }

    public int hashCode() {
        return (((((((((Integer.hashCode(this.id) * 31) + Float.hashCode(this.x)) * 31) + Float.hashCode(this.y)) * 31) + Boolean.hashCode(this.down)) * 31) + Integer.hashCode(this.maxX)) * 31) + Integer.hashCode(this.maxY);
    }

    public String toString() {
        return "TouchSample(id=" + this.id + ", x=" + this.x + ", y=" + this.y + ", down=" + this.down + ", maxX=" + this.maxX + ", maxY=" + this.maxY + ")";
    }

    public TouchSample(int id, float x, float y, boolean down, int maxX, int maxY) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.down = down;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ TouchSample(int i, float f, float f2, boolean z, int i2, int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        int i5;
        i2 = (i4 & 16) != 0 ? 0 : i2;
        if ((i4 & 32) == 0) {
            i5 = i3;
        } else {
            i5 = 0;
        }
        this(i, f, f2, z, i2, i5);
    }

    public final int getId() {
        return this.id;
    }

    public final float getX() {
        return this.x;
    }

    public final float getY() {
        return this.y;
    }

    public final boolean getDown() {
        return this.down;
    }

    public final int getMaxX() {
        return this.maxX;
    }

    public final int getMaxY() {
        return this.maxY;
    }
}
