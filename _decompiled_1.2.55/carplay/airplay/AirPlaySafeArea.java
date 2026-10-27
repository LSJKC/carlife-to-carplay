package com.carplay.airplay;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: SafeArea.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\f\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0016\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007J\u0016\u0010\t\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007J \u0010\n\u001a\u00020\u00052\b\u0010\u000b\u001a\u0004\u0018\u00010\u00052\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0007J.\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0007J0\u0010\u0014\u001a\u00020\u00152\b\u0010\u000b\u001a\u0004\u0018\u00010\u00052\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u0007J \u0010\u0018\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0002J \u0010\u0019\u001a\u00020\u00072\u0006\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\u00072\u0006\u0010\u001c\u001a\u00020\u0007H\u0002J \u0010\u001d\u001a\u00020\u00072\u0006\u0010\u001e\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020\u00072\u0006\u0010 \u001a\u00020\u0007H\u0002¨\u0006!"}, d2 = {"Lcom/carplay/airplay/AirPlaySafeArea;", "", "<init>", "()V", "full", "Lcom/carplay/airplay/SafeAreaRect;", "widthPixels", "", "heightPixels", "default", "resolve", "mapping", "activityWidthPixels", "activityHeightPixels", "scaleRect", "rect", "sourceWidthPixels", "sourceHeightPixels", "targetWidthPixels", "targetHeightPixels", "toInsets", "Lcom/carplay/airplay/AirPlayInsets;", "displayWidthPixels", "displayHeightPixels", "alignDimensionsToEven", "scaleCoordinate", "coordinate", "source", "target", "roundFraction", "value", "numerator", "denominator", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AirPlaySafeArea {
    public static final AirPlaySafeArea INSTANCE = new AirPlaySafeArea();

    private AirPlaySafeArea() {
    }

    public final SafeAreaRect full(int widthPixels, int heightPixels) {
        if (widthPixels > 0 && heightPixels > 0) {
            return new SafeAreaRect(0, 0, widthPixels, heightPixels);
        }
        throw new IllegalArgumentException("Activity dimensions must be positive".toString());
    }

    /* JADX INFO: renamed from: default, reason: not valid java name */
    public final SafeAreaRect m48default(int widthPixels, int heightPixels) {
        if (!(widthPixels > 0 && heightPixels > 0)) {
            throw new IllegalArgumentException("Activity dimensions must be positive".toString());
        }
        int left = RangesKt.coerceIn(roundFraction(widthPixels, 1, 4), 0, widthPixels - 1);
        int top = RangesKt.coerceIn(roundFraction(heightPixels, 1, 4), 0, heightPixels - 1);
        int right = RangesKt.coerceIn(roundFraction(widthPixels, 3, 4), left + 1, widthPixels);
        int bottom = RangesKt.coerceIn(roundFraction(heightPixels, 3, 4), top + 1, heightPixels);
        return new SafeAreaRect(left, top, right, bottom);
    }

    public final SafeAreaRect resolve(SafeAreaRect mapping, int activityWidthPixels, int activityHeightPixels) {
        SafeAreaRect safeAreaRectClampTo;
        return (mapping == null || (safeAreaRectClampTo = mapping.clampTo(activityWidthPixels, activityHeightPixels)) == null) ? full(activityWidthPixels, activityHeightPixels) : safeAreaRectClampTo;
    }

    public final SafeAreaRect scaleRect(SafeAreaRect rect, int sourceWidthPixels, int sourceHeightPixels, int targetWidthPixels, int targetHeightPixels) {
        Intrinsics.checkNotNullParameter(rect, "rect");
        if (!(sourceWidthPixels > 0 && sourceHeightPixels > 0)) {
            throw new IllegalArgumentException("Source dimensions must be positive".toString());
        }
        if (!(targetWidthPixels > 0 && targetHeightPixels > 0)) {
            throw new IllegalArgumentException("Target dimensions must be positive".toString());
        }
        SafeAreaRect clamped = rect.clampTo(sourceWidthPixels, sourceHeightPixels);
        int left = RangesKt.coerceIn(scaleCoordinate(clamped.getLeft(), sourceWidthPixels, targetWidthPixels), 0, targetWidthPixels - 1);
        int top = RangesKt.coerceIn(scaleCoordinate(clamped.getTop(), sourceHeightPixels, targetHeightPixels), 0, targetHeightPixels - 1);
        int right = RangesKt.coerceIn(scaleCoordinate(clamped.getRight(), sourceWidthPixels, targetWidthPixels), left + 1, targetWidthPixels);
        int bottom = RangesKt.coerceIn(scaleCoordinate(clamped.getBottom(), sourceHeightPixels, targetHeightPixels), top + 1, targetHeightPixels);
        return new SafeAreaRect(left, top, right, bottom);
    }

    public final AirPlayInsets toInsets(SafeAreaRect mapping, int activityWidthPixels, int activityHeightPixels, int displayWidthPixels, int displayHeightPixels) {
        if (!(activityWidthPixels > 0 && activityHeightPixels > 0)) {
            throw new IllegalArgumentException("Activity dimensions must be positive".toString());
        }
        if (!(displayWidthPixels > 0 && displayHeightPixels > 0)) {
            throw new IllegalArgumentException("Display dimensions must be positive".toString());
        }
        SafeAreaRect rect = resolve(mapping, activityWidthPixels, activityHeightPixels);
        int left = RangesKt.coerceIn(scaleCoordinate(rect.getLeft(), activityWidthPixels, displayWidthPixels), 0, displayWidthPixels - 1);
        int top = RangesKt.coerceIn(scaleCoordinate(rect.getTop(), activityHeightPixels, displayHeightPixels), 0, displayHeightPixels - 1);
        int right = RangesKt.coerceIn(scaleCoordinate(rect.getRight(), activityWidthPixels, displayWidthPixels), left + 1, displayWidthPixels);
        int bottom = RangesKt.coerceIn(scaleCoordinate(rect.getBottom(), activityHeightPixels, displayHeightPixels), top + 1, displayHeightPixels);
        SafeAreaRect aligned = alignDimensionsToEven(new SafeAreaRect(left, top, right, bottom), displayWidthPixels, displayHeightPixels);
        return new AirPlayInsets(aligned.getTop(), displayHeightPixels - aligned.getBottom(), aligned.getLeft(), displayWidthPixels - aligned.getRight());
    }

    private final SafeAreaRect alignDimensionsToEven(SafeAreaRect rect, int widthPixels, int heightPixels) {
        int left = rect.getLeft();
        int top = rect.getTop();
        int right = rect.getRight();
        int bottom = rect.getBottom();
        if ((right - left) % 2 != 0) {
            if (right < widthPixels) {
                right++;
            } else {
                left--;
            }
        }
        if ((bottom - top) % 2 != 0) {
            if (bottom < heightPixels) {
                bottom++;
            } else {
                top--;
            }
        }
        return new SafeAreaRect(RangesKt.coerceIn(left, 0, widthPixels - 1), RangesKt.coerceIn(top, 0, heightPixels - 1), RangesKt.coerceIn(right, left + 1, widthPixels), RangesKt.coerceIn(bottom, top + 1, heightPixels));
    }

    private final int scaleCoordinate(int coordinate, int source, int target) {
        return (int) (((((long) coordinate) * ((long) target)) + (((long) source) / 2)) / ((long) source));
    }

    private final int roundFraction(int value, int numerator, int denominator) {
        return (int) (((((long) value) * ((long) numerator)) + (((long) denominator) / 2)) / ((long) denominator));
    }
}
