package com.carlink.cast;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: WallpaperCropGeometry.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0006\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u001e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005J&\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005JN\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u0010J.\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005¨\u0006\u0016"}, d2 = {"Lcom/carlink/cast/WallpaperCropGeometry;", "", "<init>", "()V", "viewportHeight", "", "viewportWidth", "outputWidth", "outputHeight", "cover", "Lcom/carlink/cast/WallpaperCropTransform;", "sourceWidth", "sourceHeight", "zoomAround", "current", "factor", "", "focusX", "focusY", "minimumScale", "constrain", "transform", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class WallpaperCropGeometry {
    public static final WallpaperCropGeometry INSTANCE = new WallpaperCropGeometry();

    private WallpaperCropGeometry() {
    }

    public final int viewportHeight(int viewportWidth, int outputWidth, int outputHeight) {
        if (viewportWidth > 0 && outputWidth > 0 && outputHeight > 0) {
            return RangesKt.coerceAtLeast(MathKt.roundToInt((((double) viewportWidth) * ((double) outputHeight)) / ((double) outputWidth)), 1);
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }

    public final WallpaperCropTransform cover(int sourceWidth, int sourceHeight, int viewportWidth, int viewportHeight) {
        if (!(sourceWidth > 0 && sourceHeight > 0 && viewportWidth > 0 && viewportHeight > 0)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        float scale = Math.max(viewportWidth / sourceWidth, viewportHeight / sourceHeight);
        return constrain(new WallpaperCropTransform(scale, (viewportWidth - (sourceWidth * scale)) / 2.0f, (viewportHeight - (sourceHeight * scale)) / 2.0f), sourceWidth, sourceHeight, viewportWidth, viewportHeight);
    }

    public final WallpaperCropTransform zoomAround(WallpaperCropTransform current, float factor, float focusX, float focusY, int sourceWidth, int sourceHeight, int viewportWidth, int viewportHeight, float minimumScale) {
        Intrinsics.checkNotNullParameter(current, "current");
        float scale = RangesKt.coerceIn(current.getScale() * factor, minimumScale, 8.0f * minimumScale);
        float ratio = scale / current.getScale();
        return constrain(new WallpaperCropTransform(scale, focusX - ((focusX - current.getLeft()) * ratio), focusY - ((focusY - current.getTop()) * ratio)), sourceWidth, sourceHeight, viewportWidth, viewportHeight);
    }

    public final WallpaperCropTransform constrain(WallpaperCropTransform transform, int sourceWidth, int sourceHeight, int viewportWidth, int viewportHeight) {
        Intrinsics.checkNotNullParameter(transform, "transform");
        float scaledWidth = sourceWidth * transform.getScale();
        float scaledHeight = sourceHeight * transform.getScale();
        return WallpaperCropTransform.copy$default(transform, 0.0f, RangesKt.coerceIn(transform.getLeft(), viewportWidth - scaledWidth, 0.0f), RangesKt.coerceIn(transform.getTop(), viewportHeight - scaledHeight, 0.0f), 1, null);
    }
}
