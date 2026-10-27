package com.carplay.airplay;

import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.ClosedRange;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: CarPlayClusterDisplay.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000b\bÆ\u0002\u0018\u00002\u00020\u0001:\u00012B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010\n\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tJ,\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u001b\u0012\u0004\u0012\u00020\u001b0\u001a2\u0006\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u001d\u001a\u00020\u00122\b\b\u0002\u0010\u001e\u001a\u00020\fJH\u0010'\u001a\u00020(2\u0006\u0010)\u001a\u00020\u00122\u0006\u0010*\u001a\u00020\u00122\b\b\u0002\u0010+\u001a\u00020\u00122\b\b\u0002\u0010\u001c\u001a\u00020\u00122\b\b\u0002\u0010\u001d\u001a\u00020\u00122\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010,\u001a\u00020\fJ2\u0010-\u001a\u00020\f2\u0006\u0010.\u001a\u00020\u00122\u0006\u0010/\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u001d\u001a\u00020\u00122\b\b\u0002\u0010,\u001a\u00020\fH\u0002J\u0010\u00100\u001a\u00020\u00122\u0006\u00101\u001a\u00020\u001bH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u0011\u0010\u000b\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000eR\u000e\u0010\u0011\u001a\u00020\u0012X\u0086T¢\u0006\u0002\n\u0000R\u0011\u0010\u0013\u001a\u00020\u0014¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0017\u001a\u00020\u0014¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0016R\u000e\u0010\u001f\u001a\u00020\u001bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0012X\u0086T¢\u0006\u0002\n\u0000R\u0017\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00120\"¢\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u000e\u0010%\u001a\u00020\u0012X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u0012X\u0082T¢\u0006\u0002\n\u0000¨\u00063"}, d2 = {"Lcom/carplay/airplay/CarPlayClusterDisplay;", "", "<init>", "()V", "MAP_URL", "", "usesCustomTurnCard", "", "content", "Lcom/carplay/airplay/CarPlayClusterDisplay$Content;", "usesOfficialTurnCard", "SAFE_AREA_PERCENT", "Lcom/carplay/airplay/AirPlayInsets;", "getSAFE_AREA_PERCENT", "()Lcom/carplay/airplay/AirPlayInsets;", "VIRTUAL_SAFE_AREA_PERCENT", "getVIRTUAL_SAFE_AREA_PERCENT", "MARKER_STEP_PERCENT", "", "horizontalSteps", "Lkotlin/ranges/IntRange;", "getHorizontalSteps", "()Lkotlin/ranges/IntRange;", "verticalSteps", "getVerticalSteps", "markerPercent", "Lkotlin/Pair;", "", "horizontalStep", "verticalStep", "area", "MARKER_MARGIN", "STREAM_SCALE_PERCENT", "scalePresets", "", "getScalePresets", "()Ljava/util/List;", "WIDTH_PHYSICAL_MM", "FPS", "config", "Lcom/carplay/airplay/AirPlayDisplayConfig;", "widthPixels", "heightPixels", "scalePercent", "baseSafeArea", "safeArea", "width", "height", "roundTo8", "value", "Content", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarPlayClusterDisplay {
    private static final int FPS = 30;
    public static final String MAP_URL = "maps:/car/instrumentcluster/map";
    private static final double MARKER_MARGIN = 5.0d;
    public static final int MARKER_STEP_PERCENT = 10;
    public static final int STREAM_SCALE_PERCENT = 83;
    private static final int WIDTH_PHYSICAL_MM = 292;
    public static final CarPlayClusterDisplay INSTANCE = new CarPlayClusterDisplay();
    private static final AirPlayInsets SAFE_AREA_PERCENT = new AirPlayInsets(16, 25, 35, 36);
    private static final AirPlayInsets VIRTUAL_SAFE_AREA_PERCENT = new AirPlayInsets(8, 8, 8, 8);
    private static final IntRange horizontalSteps = new IntRange(-4, 4);
    private static final IntRange verticalSteps = new IntRange(-3, 3);
    private static final List<Integer> scalePresets = CollectionsKt.listOf((Object[]) new Integer[]{100, 83, 67, 125});

    private CarPlayClusterDisplay() {
    }

    /* JADX INFO: compiled from: CarPlayClusterDisplay.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0011\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000b¨\u0006\f"}, d2 = {"Lcom/carplay/airplay/CarPlayClusterDisplay$Content;", "", "url", "", "<init>", "(Ljava/lang/String;ILjava/lang/String;)V", "getUrl", "()Ljava/lang/String;", "MAP", "TURN_CARD", "INSTRUMENTS", "MAP_WITH_CUSTOM_CARD", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public enum Content {
        MAP(CarPlayClusterDisplay.MAP_URL),
        TURN_CARD("maps:/car/instrumentcluster/instructioncard"),
        INSTRUMENTS("maps:/car/instrumentcluster"),
        MAP_WITH_CUSTOM_CARD(CarPlayClusterDisplay.MAP_URL);

        private final String url;
        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

        public static EnumEntries<Content> getEntries() {
            return $ENTRIES;
        }

        Content(String url) {
            this.url = url;
        }

        public final String getUrl() {
            return this.url;
        }
    }

    public final boolean usesCustomTurnCard(Content content) {
        Intrinsics.checkNotNullParameter(content, "content");
        return content == Content.MAP_WITH_CUSTOM_CARD;
    }

    public final boolean usesOfficialTurnCard(Content content) {
        Intrinsics.checkNotNullParameter(content, "content");
        return content == Content.TURN_CARD || content == Content.INSTRUMENTS;
    }

    public final AirPlayInsets getSAFE_AREA_PERCENT() {
        return SAFE_AREA_PERCENT;
    }

    public final AirPlayInsets getVIRTUAL_SAFE_AREA_PERCENT() {
        return VIRTUAL_SAFE_AREA_PERCENT;
    }

    public final IntRange getHorizontalSteps() {
        return horizontalSteps;
    }

    public final IntRange getVerticalSteps() {
        return verticalSteps;
    }

    public static /* synthetic */ Pair markerPercent$default(CarPlayClusterDisplay carPlayClusterDisplay, int i, int i2, AirPlayInsets airPlayInsets, int i3, Object obj) {
        if ((i3 & 4) != 0) {
            airPlayInsets = SAFE_AREA_PERCENT;
        }
        return carPlayClusterDisplay.markerPercent(i, i2, airPlayInsets);
    }

    public final Pair<Double, Double> markerPercent(int horizontalStep, int verticalStep, AirPlayInsets area) {
        Intrinsics.checkNotNullParameter(area, "area");
        double x = (((double) ((area.getLeft() + 100) - area.getRight())) / 2.0d) + ((double) (RangesKt.coerceIn(horizontalStep, (ClosedRange<Integer>) horizontalSteps) * 10));
        double y = (((double) ((area.getTop() + 100) - area.getBottom())) / 2.0d) + ((double) (RangesKt.coerceIn(verticalStep, (ClosedRange<Integer>) verticalSteps) * 10));
        return TuplesKt.to(Double.valueOf(RangesKt.coerceIn(x, MARKER_MARGIN, 95.0d)), Double.valueOf(RangesKt.coerceIn(y, MARKER_MARGIN, 95.0d)));
    }

    public final List<Integer> getScalePresets() {
        return scalePresets;
    }

    public final AirPlayDisplayConfig config(int widthPixels, int heightPixels, int scalePercent, int horizontalStep, int verticalStep, Content content, AirPlayInsets baseSafeArea) {
        Intrinsics.checkNotNullParameter(content, "content");
        Intrinsics.checkNotNullParameter(baseSafeArea, "baseSafeArea");
        int scale = RangesKt.coerceIn(scalePercent, 25, 150);
        int height = roundTo8(((double) (heightPixels * scale)) / 100.0d);
        int width = roundTo8((((double) height) * ((double) widthPixels)) / ((double) heightPixels));
        int iRound = (int) Math.round((292.0d * ((double) height)) / ((double) width));
        String url = content.getUrl();
        return new AirPlayDisplayConfig(width, height, Integer.valueOf(WIDTH_PHYSICAL_MM), Integer.valueOf(iRound), 30, 0, null, safeArea(width, height, horizontalStep, verticalStep, baseSafeArea), true, url, 0, null, 0, 6208, null);
    }

    static /* synthetic */ AirPlayInsets safeArea$default(CarPlayClusterDisplay carPlayClusterDisplay, int i, int i2, int i3, int i4, AirPlayInsets airPlayInsets, int i5, Object obj) {
        AirPlayInsets airPlayInsets2;
        if ((i5 & 16) == 0) {
            airPlayInsets2 = airPlayInsets;
        } else {
            airPlayInsets2 = SAFE_AREA_PERCENT;
        }
        return carPlayClusterDisplay.safeArea(i, i2, i3, i4, airPlayInsets2);
    }

    private final AirPlayInsets safeArea(int width, int height, int horizontalStep, int verticalStep, AirPlayInsets baseSafeArea) {
        Pair<Double, Double> pairMarkerPercent = markerPercent(horizontalStep, verticalStep, baseSafeArea);
        double x = pairMarkerPercent.component1().doubleValue();
        double y = pairMarkerPercent.component2().doubleValue();
        double halfWidth = Math.min(((double) ((100 - baseSafeArea.getLeft()) - baseSafeArea.getRight())) / 2.0d, Math.min(x, 100.0d - x));
        double halfHeight = Math.min(((double) ((100 - baseSafeArea.getTop()) - baseSafeArea.getBottom())) / 2.0d, Math.min(y, 100.0d - y));
        return new AirPlayInsets((int) ((((double) height) * (y - halfHeight)) / 100.0d), (int) ((((double) height) * ((100.0d - y) - halfHeight)) / 100.0d), (int) ((((double) width) * (x - halfWidth)) / 100.0d), (int) ((((double) width) * ((100.0d - x) - halfWidth)) / 100.0d));
    }

    private final int roundTo8(double value) {
        return RangesKt.coerceAtLeast((int) (Math.round(value / 8.0d) * 8), 8);
    }
}
