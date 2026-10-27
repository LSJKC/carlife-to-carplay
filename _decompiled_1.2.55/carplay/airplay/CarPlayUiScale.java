package com.carplay.airplay;

import java.util.List;
import javax.jmdns.impl.constants.DNSConstants;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import org.bouncycastle.jcajce.util.AnnotatedPrivateKey;

/* JADX INFO: compiled from: CarPlayUiScale.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u0005J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000b\u001a\u00020\u0005J\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\u0005R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t¨\u0006\u0011"}, d2 = {"Lcom/carplay/airplay/CarPlayUiScale;", "", "<init>", "()V", "DEFAULT", "", "presets", "", "getPresets", "()Ljava/util/List;", "sanitize", "percent", AnnotatedPrivateKey.LABEL, "", "apply", "Lcom/carplay/airplay/AirPlayDisplayConfig;", "display", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarPlayUiScale {
    public static final int DEFAULT = 100;
    public static final CarPlayUiScale INSTANCE = new CarPlayUiScale();
    private static final List<Integer> presets = CollectionsKt.listOf((Object[]) new Integer[]{75, 85, 100, Integer.valueOf(DNSConstants.RESPONSE_MAX_WAIT_INTERVAL)});

    private CarPlayUiScale() {
    }

    public final List<Integer> getPresets() {
        return presets;
    }

    public final int sanitize(int percent) {
        Integer numValueOf = Integer.valueOf(percent);
        int it = numValueOf.intValue();
        if (!presets.contains(Integer.valueOf(it))) {
            numValueOf = null;
        }
        if (numValueOf != null) {
            return numValueOf.intValue();
        }
        return 100;
    }

    public final String label(int percent) {
        switch (sanitize(percent)) {
            case 75:
                return "Smaller";
            case ClusterTurnCardOverlay.DEFAULT_OPACITY_PERCENT /* 85 */:
                return "Small";
            case DNSConstants.RESPONSE_MAX_WAIT_INTERVAL /* 115 */:
                return "Large";
            default:
                return "Default";
        }
    }

    public final AirPlayDisplayConfig apply(AirPlayDisplayConfig display, int percent) {
        Intrinsics.checkNotNullParameter(display, "display");
        int scale = sanitize(percent);
        if (scale == 100) {
            return display;
        }
        if (!(display.getWidthPixels() > 0 && display.getHeightPixels() > 0)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        int width = apply$pixels(scale, display.getWidthPixels());
        int height = apply$pixels(scale, display.getHeightPixels());
        return (scale >= 100 || (Math.max(width, height) <= 3840 && Math.min(width, height) <= 2160)) ? AirPlayDisplayConfig.copy$default(display, width, height, null, null, 0, 0, apply$insets(height, display, width, display.getViewArea()), apply$insets(height, display, width, display.getSafeArea()), null, null, null, null, 0, 7996, null) : display;
    }

    private static final int apply$pixels(int scale, int value) {
        return (int) (((((((long) value) * 100) / ((long) scale)) + 1) / 2) * 2);
    }

    private static final AirPlayInsets apply$insets(int height, AirPlayDisplayConfig $display, int width, AirPlayInsets value) {
        if (value != null) {
            return value.copy(MathKt.roundToInt((((double) value.getTop()) * ((double) height)) / ((double) $display.getHeightPixels())), MathKt.roundToInt((((double) value.getBottom()) * ((double) height)) / ((double) $display.getHeightPixels())), MathKt.roundToInt((((double) value.getLeft()) * ((double) width)) / ((double) $display.getWidthPixels())), MathKt.roundToInt((((double) value.getRight()) * ((double) width)) / ((double) $display.getWidthPixels())));
        }
        return null;
    }
}
