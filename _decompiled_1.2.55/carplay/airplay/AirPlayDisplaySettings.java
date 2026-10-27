package com.carplay.airplay;

import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: AirPlayDisplaySettings.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\r\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0005J\u000e\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0005J\u000e\u0010\u0016\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0005J6\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u001a\u001a\u00020\u00052\u0006\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u001c\u001a\u00020\u00052\u0006\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u000eJ\u000e\u0010\u001f\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0005J\u000e\u0010 \u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0005J(\u0010!\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u00052\u0006\u0010\"\u001a\u00020\u00052\u0006\u0010#\u001a\u00020\u00052\u0006\u0010$\u001a\u00020\u0005H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u0011\u0010\r\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006%"}, d2 = {"Lcom/carplay/airplay/AirPlayDisplaySettings;", "", "<init>", "()V", "MIN_FPS", "", "MAX_FPS", "FPS_STEP", "DEFAULT_FPS", "MIN_WIDTH_PHYSICAL_MM", "MAX_WIDTH_PHYSICAL_MM", "WIDTH_PHYSICAL_MM_STEP", "DEFAULT_WIDTH_PHYSICAL_MM", "DEFAULT_PHYSICAL_SIZE_BASIS", "Lcom/carplay/airplay/AirPlayPhysicalSizeBasis;", "getDEFAULT_PHYSICAL_SIZE_BASIS", "()Lcom/carplay/airplay/AirPlayPhysicalSizeBasis;", "MIN_REPORTED_PHYSICAL_MM", "MAX_REPORTED_PHYSICAL_MM", "sanitizeFps", "value", "sanitizeWidthPhysicalMm", "sanitizeReportedPhysicalMm", "resolvePhysicalSizeMm", "Lcom/carplay/airplay/AirPlayPhysicalSizeMm;", "currentWidthPixels", "currentHeightPixels", "maximumWidthPixels", "maximumHeightPixels", "referenceMillimeters", "basis", "fpsProgress", "widthPhysicalMmProgress", "snapToStep", "minimum", "maximum", "step", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AirPlayDisplaySettings {
    public static final int DEFAULT_FPS = 60;
    public static final int DEFAULT_WIDTH_PHYSICAL_MM = 200;
    public static final int FPS_STEP = 5;
    public static final int MAX_FPS = 60;
    public static final int MAX_REPORTED_PHYSICAL_MM = 2000;
    public static final int MAX_WIDTH_PHYSICAL_MM = 400;
    public static final int MIN_FPS = 30;
    public static final int MIN_REPORTED_PHYSICAL_MM = 1;
    public static final int MIN_WIDTH_PHYSICAL_MM = 100;
    public static final int WIDTH_PHYSICAL_MM_STEP = 50;
    public static final AirPlayDisplaySettings INSTANCE = new AirPlayDisplaySettings();
    private static final AirPlayPhysicalSizeBasis DEFAULT_PHYSICAL_SIZE_BASIS = AirPlayPhysicalSizeBasis.WIDTH;

    /* JADX INFO: compiled from: AirPlayDisplaySettings.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[AirPlayPhysicalSizeBasis.values().length];
            try {
                iArr[AirPlayPhysicalSizeBasis.WIDTH.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[AirPlayPhysicalSizeBasis.HEIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    private AirPlayDisplaySettings() {
    }

    public final AirPlayPhysicalSizeBasis getDEFAULT_PHYSICAL_SIZE_BASIS() {
        return DEFAULT_PHYSICAL_SIZE_BASIS;
    }

    public final int sanitizeFps(int value) {
        return snapToStep(value, 30, 60, 5);
    }

    public final int sanitizeWidthPhysicalMm(int value) {
        return snapToStep(value, 100, MAX_WIDTH_PHYSICAL_MM, 50);
    }

    public final int sanitizeReportedPhysicalMm(int value) {
        return RangesKt.coerceIn(value, 1, MAX_REPORTED_PHYSICAL_MM);
    }

    public final AirPlayPhysicalSizeMm resolvePhysicalSizeMm(int currentWidthPixels, int currentHeightPixels, int maximumWidthPixels, int maximumHeightPixels, int referenceMillimeters, AirPlayPhysicalSizeBasis basis) {
        double referencePixels;
        double heightMm;
        double widthMm;
        Intrinsics.checkNotNullParameter(basis, "basis");
        boolean z = false;
        if (!(currentWidthPixels > 0 && currentHeightPixels > 0)) {
            throw new IllegalArgumentException("Current display dimensions must be positive".toString());
        }
        if (maximumWidthPixels > 0 && maximumHeightPixels > 0) {
            z = true;
        }
        if (!z) {
            throw new IllegalArgumentException("Maximum display dimensions must be positive".toString());
        }
        switch (WhenMappings.$EnumSwitchMapping$0[basis.ordinal()]) {
            case 1:
                referencePixels = ((double) currentWidthPixels) / ((double) maximumWidthPixels);
                break;
            case 2:
                referencePixels = ((double) currentHeightPixels) / ((double) maximumHeightPixels);
                break;
            default:
                throw new NoWhenBranchMatchedException();
        }
        double referenceMm = ((double) RangesKt.coerceAtLeast(referenceMillimeters, 1)) * referencePixels;
        switch (WhenMappings.$EnumSwitchMapping$0[basis.ordinal()]) {
            case 1:
                double widthMm2 = currentHeightPixels;
                heightMm = (widthMm2 * referenceMm) / ((double) currentWidthPixels);
                widthMm = referenceMm;
                break;
            case 2:
                heightMm = referenceMm;
                widthMm = (((double) currentWidthPixels) * heightMm) / ((double) currentHeightPixels);
                break;
            default:
                throw new NoWhenBranchMatchedException();
        }
        return new AirPlayPhysicalSizeMm(sanitizeReportedPhysicalMm((int) Math.round(widthMm)), sanitizeReportedPhysicalMm((int) Math.round(heightMm)));
    }

    public final int fpsProgress(int value) {
        return (sanitizeFps(value) - 30) / 5;
    }

    public final int widthPhysicalMmProgress(int value) {
        return (sanitizeWidthPhysicalMm(value) - 100) / 50;
    }

    private final int snapToStep(int value, int minimum, int maximum, int step) {
        int clamped = RangesKt.coerceIn(value, minimum, maximum);
        return ((((clamped - minimum) + (step / 2)) / step) * step) + minimum;
    }
}
