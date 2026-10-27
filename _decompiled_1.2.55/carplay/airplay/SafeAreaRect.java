package com.carplay.airplay;

import kotlin.Metadata;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: SafeArea.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0004\b\u0007\u0010\bJ\u0016\u0010\u0012\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u0003J\t\u0010\u0015\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0003HÆ\u0003J1\u0010\u0019\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001d\u001a\u00020\u0003HÖ\u0001J\t\u0010\u001e\u001a\u00020\u001fHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u0011\u0010\u000e\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\nR\u0011\u0010\u0010\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u0011\u0010\n¨\u0006 "}, d2 = {"Lcom/carplay/airplay/SafeAreaRect;", "", "left", "", "top", "right", "bottom", "<init>", "(IIII)V", "getLeft", "()I", "getTop", "getRight", "getBottom", "width", "getWidth", "height", "getHeight", "clampTo", "widthPixels", "heightPixels", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class SafeAreaRect {
    private final int bottom;
    private final int left;
    private final int right;
    private final int top;

    public static /* synthetic */ SafeAreaRect copy$default(SafeAreaRect safeAreaRect, int i, int i2, int i3, int i4, int i5, Object obj) {
        if ((i5 & 1) != 0) {
            i = safeAreaRect.left;
        }
        if ((i5 & 2) != 0) {
            i2 = safeAreaRect.top;
        }
        if ((i5 & 4) != 0) {
            i3 = safeAreaRect.right;
        }
        if ((i5 & 8) != 0) {
            i4 = safeAreaRect.bottom;
        }
        return safeAreaRect.copy(i, i2, i3, i4);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getLeft() {
        return this.left;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getTop() {
        return this.top;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int getRight() {
        return this.right;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int getBottom() {
        return this.bottom;
    }

    public final SafeAreaRect copy(int left, int top, int right, int bottom) {
        return new SafeAreaRect(left, top, right, bottom);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SafeAreaRect)) {
            return false;
        }
        SafeAreaRect safeAreaRect = (SafeAreaRect) other;
        return this.left == safeAreaRect.left && this.top == safeAreaRect.top && this.right == safeAreaRect.right && this.bottom == safeAreaRect.bottom;
    }

    public int hashCode() {
        return (((((Integer.hashCode(this.left) * 31) + Integer.hashCode(this.top)) * 31) + Integer.hashCode(this.right)) * 31) + Integer.hashCode(this.bottom);
    }

    public String toString() {
        return "SafeAreaRect(left=" + this.left + ", top=" + this.top + ", right=" + this.right + ", bottom=" + this.bottom + ")";
    }

    public SafeAreaRect(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        if (this.left >= 0 && this.top >= 0 && this.right > this.left && this.bottom > this.top) {
        } else {
            throw new IllegalArgumentException("Safe area coordinates must describe a non-empty rectangle".toString());
        }
    }

    public final int getLeft() {
        return this.left;
    }

    public final int getTop() {
        return this.top;
    }

    public final int getRight() {
        return this.right;
    }

    public final int getBottom() {
        return this.bottom;
    }

    public final int getWidth() {
        return this.right - this.left;
    }

    public final int getHeight() {
        return this.bottom - this.top;
    }

    public final SafeAreaRect clampTo(int widthPixels, int heightPixels) {
        if (!(widthPixels > 0 && heightPixels > 0)) {
            throw new IllegalArgumentException("Activity dimensions must be positive".toString());
        }
        int clampedLeft = RangesKt.coerceIn(this.left, 0, widthPixels - 1);
        int clampedTop = RangesKt.coerceIn(this.top, 0, heightPixels - 1);
        int clampedRight = RangesKt.coerceIn(this.right, clampedLeft + 1, widthPixels);
        int clampedBottom = RangesKt.coerceIn(this.bottom, clampedTop + 1, heightPixels);
        return new SafeAreaRect(clampedLeft, clampedTop, clampedRight, clampedBottom);
    }
}
