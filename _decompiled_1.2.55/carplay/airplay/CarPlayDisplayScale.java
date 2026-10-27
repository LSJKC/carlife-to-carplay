package com.carplay.airplay;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import org.bouncycastle.jcajce.util.AnnotatedPrivateKey;

/* JADX INFO: compiled from: CarPlayDisplayScale.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000b\u001a\u00020\u0005J\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\u0005J\u0016\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u0005J\u0018\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lcom/carplay/airplay/CarPlayDisplayScale;", "", "<init>", "()V", "MIN_TENTHS", "", "MAX_TENTHS", "DEFAULT_TENTHS", "MIN_PERCENT", "MAX_PERCENT", "sanitize", "tenths", AnnotatedPrivateKey.LABEL, "", "apply", "Lcom/carplay/airplay/AirPlayDisplayConfig;", "display", "applyPercent", "percent", "scalePixels", "pixels", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarPlayDisplayScale {
    public static final int DEFAULT_TENTHS = 10;
    public static final CarPlayDisplayScale INSTANCE = new CarPlayDisplayScale();
    public static final int MAX_PERCENT = 160;
    public static final int MAX_TENTHS = 10;
    public static final int MIN_PERCENT = 30;
    public static final int MIN_TENTHS = 3;

    private CarPlayDisplayScale() {
    }

    public final int sanitize(int tenths) {
        return RangesKt.coerceIn(tenths, 3, 10);
    }

    public final String label(int tenths) {
        int value = sanitize(tenths);
        return (value / 10) + "." + (value % 10) + "x";
    }

    public final AirPlayDisplayConfig apply(AirPlayDisplayConfig display, int tenths) {
        Intrinsics.checkNotNullParameter(display, "display");
        int value = sanitize(tenths);
        return AirPlayDisplayConfig.copy$default(display, scalePixels(display.getWidthPixels(), value), scalePixels(display.getHeightPixels(), value), null, null, 0, 0, null, null, null, null, null, null, 0, 8188, null);
    }

    public final AirPlayDisplayConfig applyPercent(AirPlayDisplayConfig display, int percent) {
        Intrinsics.checkNotNullParameter(display, "display");
        int value = RangesKt.coerceIn(percent, 30, MAX_PERCENT);
        return AirPlayDisplayConfig.copy$default(display, applyPercent$scale(value, display.getWidthPixels()), applyPercent$scale(value, display.getHeightPixels()), null, null, 0, 0, null, null, null, null, null, null, 0, 8188, null);
    }

    private static final int applyPercent$scale(int value, int pixels) {
        if (!(pixels > 0)) {
            throw new IllegalArgumentException("pixels must be positive".toString());
        }
        int scaled = RangesKt.coerceAtLeast((int) (((((long) pixels) * ((long) value)) + 50) / 100), 1);
        return scaled % 2 == 0 ? scaled : scaled + 1;
    }

    private final int scalePixels(int pixels, int tenths) {
        if (!(pixels > 0)) {
            throw new IllegalArgumentException("pixels must be positive".toString());
        }
        int scaled = RangesKt.coerceAtLeast((int) (((((long) pixels) * ((long) tenths)) + 5) / 10), 1);
        return scaled % 2 == 0 ? scaled : scaled + 1;
    }
}
