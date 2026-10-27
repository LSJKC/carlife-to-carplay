package com.carlink.cast;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: WallpaperTextureGeometry.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0014\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J&\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u0007J\u000e\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005¨\u0006\r"}, d2 = {"Lcom/carlink/cast/WallpaperTextureGeometry;", "", "<init>", "()V", "aspectFillUv", "", "sourceWidth", "", "sourceHeight", "targetWidth", "targetHeight", "quadCoordinates", "uv", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class WallpaperTextureGeometry {
    public static final WallpaperTextureGeometry INSTANCE = new WallpaperTextureGeometry();

    private WallpaperTextureGeometry() {
    }

    public final float[] aspectFillUv(int sourceWidth, int sourceHeight, int targetWidth, int targetHeight) {
        if (!(sourceWidth > 0 && sourceHeight > 0 && targetWidth > 0 && targetHeight > 0)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        float sourceAspect = sourceWidth / sourceHeight;
        float targetAspect = targetWidth / targetHeight;
        if (sourceAspect > targetAspect) {
            float fraction = targetAspect / sourceAspect;
            float inset = (1.0f - fraction) / 2.0f;
            return new float[]{inset, 1.0f - inset, 0.0f, 1.0f};
        }
        float fraction2 = sourceAspect / targetAspect;
        float inset2 = (1.0f - fraction2) / 2.0f;
        return new float[]{0.0f, 1.0f, inset2, 1.0f - inset2};
    }

    public final float[] quadCoordinates(float[] uv) {
        Intrinsics.checkNotNullParameter(uv, "uv");
        if (!(uv.length == 4)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        float u0 = uv[0];
        float u1 = uv[1];
        float v0 = uv[2];
        float v1 = uv[3];
        return new float[]{u0, v1, u1, v1, u0, v0, u1, v0};
    }
}
