package com.carlink.cast;

import kotlin.Metadata;

/* JADX INFO: compiled from: WallpaperCropGeometry.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0080\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J'\u0010\u000f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\t¨\u0006\u0017"}, d2 = {"Lcom/carlink/cast/WallpaperCropTransform;", "", "scale", "", "left", "top", "<init>", "(FFF)V", "getScale", "()F", "getLeft", "getTop", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class WallpaperCropTransform {
    private final float left;
    private final float scale;
    private final float top;

    public static /* synthetic */ WallpaperCropTransform copy$default(WallpaperCropTransform wallpaperCropTransform, float f, float f2, float f3, int i, Object obj) {
        if ((i & 1) != 0) {
            f = wallpaperCropTransform.scale;
        }
        if ((i & 2) != 0) {
            f2 = wallpaperCropTransform.left;
        }
        if ((i & 4) != 0) {
            f3 = wallpaperCropTransform.top;
        }
        return wallpaperCropTransform.copy(f, f2, f3);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final float getScale() {
        return this.scale;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final float getLeft() {
        return this.left;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final float getTop() {
        return this.top;
    }

    public final WallpaperCropTransform copy(float scale, float left, float top) {
        return new WallpaperCropTransform(scale, left, top);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof WallpaperCropTransform)) {
            return false;
        }
        WallpaperCropTransform wallpaperCropTransform = (WallpaperCropTransform) other;
        return Float.compare(this.scale, wallpaperCropTransform.scale) == 0 && Float.compare(this.left, wallpaperCropTransform.left) == 0 && Float.compare(this.top, wallpaperCropTransform.top) == 0;
    }

    public int hashCode() {
        return (((Float.hashCode(this.scale) * 31) + Float.hashCode(this.left)) * 31) + Float.hashCode(this.top);
    }

    public String toString() {
        return "WallpaperCropTransform(scale=" + this.scale + ", left=" + this.left + ", top=" + this.top + ")";
    }

    public WallpaperCropTransform(float scale, float left, float top) {
        this.scale = scale;
        this.left = left;
        this.top = top;
    }

    public final float getScale() {
        return this.scale;
    }

    public final float getLeft() {
        return this.left;
    }

    public final float getTop() {
        return this.top;
    }
}
