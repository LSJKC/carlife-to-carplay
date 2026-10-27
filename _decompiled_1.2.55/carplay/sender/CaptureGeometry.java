package com.carplay.sender;

import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;

/* JADX INFO: compiled from: HidInput.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0004\b\u0007\u0010\bJ$\u0010\u000e\u001a\u0010\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000f2\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0010J\t\u0010\u0013\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J1\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001b\u001a\u00020\u0003HÖ\u0001J\t\u0010\u001c\u001a\u00020\u001dHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\n¨\u0006\u001e"}, d2 = {"Lcom/carplay/sender/CaptureGeometry;", "", "sourceWidth", "", "sourceHeight", "width", "height", "<init>", "(IIII)V", "getSourceWidth", "()I", "getSourceHeight", "getWidth", "getHeight", "point", "Lkotlin/Pair;", "", "x", "y", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class CaptureGeometry {
    private final int height;
    private final int sourceHeight;
    private final int sourceWidth;
    private final int width;

    public static /* synthetic */ CaptureGeometry copy$default(CaptureGeometry captureGeometry, int i, int i2, int i3, int i4, int i5, Object obj) {
        if ((i5 & 1) != 0) {
            i = captureGeometry.sourceWidth;
        }
        if ((i5 & 2) != 0) {
            i2 = captureGeometry.sourceHeight;
        }
        if ((i5 & 4) != 0) {
            i3 = captureGeometry.width;
        }
        if ((i5 & 8) != 0) {
            i4 = captureGeometry.height;
        }
        return captureGeometry.copy(i, i2, i3, i4);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getSourceWidth() {
        return this.sourceWidth;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getSourceHeight() {
        return this.sourceHeight;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int getWidth() {
        return this.width;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int getHeight() {
        return this.height;
    }

    public final CaptureGeometry copy(int sourceWidth, int sourceHeight, int width, int height) {
        return new CaptureGeometry(sourceWidth, sourceHeight, width, height);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CaptureGeometry)) {
            return false;
        }
        CaptureGeometry captureGeometry = (CaptureGeometry) other;
        return this.sourceWidth == captureGeometry.sourceWidth && this.sourceHeight == captureGeometry.sourceHeight && this.width == captureGeometry.width && this.height == captureGeometry.height;
    }

    public int hashCode() {
        return (((((Integer.hashCode(this.sourceWidth) * 31) + Integer.hashCode(this.sourceHeight)) * 31) + Integer.hashCode(this.width)) * 31) + Integer.hashCode(this.height);
    }

    public String toString() {
        return "CaptureGeometry(sourceWidth=" + this.sourceWidth + ", sourceHeight=" + this.sourceHeight + ", width=" + this.width + ", height=" + this.height + ")";
    }

    public CaptureGeometry(int sourceWidth, int sourceHeight, int width, int height) {
        this.sourceWidth = sourceWidth;
        this.sourceHeight = sourceHeight;
        this.width = width;
        this.height = height;
        if (this.sourceWidth > 0 && this.sourceHeight > 0 && this.width > 0 && this.height > 0) {
        } else {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
    }

    public final int getHeight() {
        return this.height;
    }

    public final int getSourceHeight() {
        return this.sourceHeight;
    }

    public final int getSourceWidth() {
        return this.sourceWidth;
    }

    public final int getWidth() {
        return this.width;
    }

    public final Pair<Float, Float> point(float x, float y) {
        float scale = Math.min(this.width / this.sourceWidth, this.height / this.sourceHeight);
        float left = (this.width - (this.sourceWidth * scale)) / 2.0f;
        float top = (this.height - (this.sourceHeight * scale)) / 2.0f;
        float mappedX = (x - left) / scale;
        float mappedY = (y - top) / scale;
        if (0.0f <= mappedX && mappedX <= ((float) this.sourceWidth)) {
            if (!(0.0f <= mappedY && mappedY <= ((float) this.sourceHeight))) {
                return null;
            }
            return TuplesKt.to(Float.valueOf(mappedX), Float.valueOf(mappedY));
        }
        return null;
    }
}
