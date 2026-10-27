package com.carplay.adapter.carlife;

import java.util.Map;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.json.JSONObject;

/* JADX INFO: compiled from: CarLifeEncoderFormat.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u001e\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0016\b\u0080\b\u0018\u0000 ?2\u00020\u0001:\u0001?B\u0083\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\n\u0012\b\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\r\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0011\u001a\u0004\u0018\u00010\n¢\u0006\u0004\b\u0012\u0010\u0013J\u0012\u0010(\u001a\u000e\u0012\u0004\u0012\u00020*\u0012\u0004\u0012\u00020\u00010)J\t\u0010+\u001a\u00020\u0003HÆ\u0003J\t\u0010,\u001a\u00020\u0003HÆ\u0003J\t\u0010-\u001a\u00020\u0003HÆ\u0003J\t\u0010.\u001a\u00020\u0003HÆ\u0003J\t\u0010/\u001a\u00020\u0003HÆ\u0003J\t\u00100\u001a\u00020\u0003HÆ\u0003J\t\u00101\u001a\u00020\nHÆ\u0003J\t\u00102\u001a\u00020\nHÆ\u0003J\u0010\u00103\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u001fJ\u0010\u00104\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u001fJ\u0010\u00105\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u001fJ\u0010\u00106\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u001fJ\u0010\u00107\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u001fJ\u0010\u00108\u001a\u0004\u0018\u00010\nHÆ\u0003¢\u0006\u0002\u0010&J¦\u0001\u00109\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\n2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\nHÆ\u0001¢\u0006\u0002\u0010:J\u0013\u0010;\u001a\u00020\n2\b\u0010<\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010=\u001a\u00020\u0003HÖ\u0001J\t\u0010>\u001a\u00020*HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0015R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0015R\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0015R\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0015R\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0015R\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u000b\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001cR\u0015\u0010\f\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010 \u001a\u0004\b\u001e\u0010\u001fR\u0015\u0010\r\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010 \u001a\u0004\b!\u0010\u001fR\u0015\u0010\u000e\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010 \u001a\u0004\b\"\u0010\u001fR\u0015\u0010\u000f\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010 \u001a\u0004\b#\u0010\u001fR\u0015\u0010\u0010\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010 \u001a\u0004\b$\u0010\u001fR\u0015\u0010\u0011\u001a\u0004\u0018\u00010\n¢\u0006\n\n\u0002\u0010'\u001a\u0004\b%\u0010&¨\u0006@"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeEncoderFormat;", "", "panelWidth", "", "panelHeight", "safeWidth", "safeHeight", "safeX", "safeY", "needAdapterScreen", "", "drawUiOutsideSafeArea", "frameRate", "realWidth", "realHeight", "defaultWidth", "defaultHeight", "resolutionAccurate", "<init>", "(IIIIIIZZLjava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Boolean;)V", "getPanelWidth", "()I", "getPanelHeight", "getSafeWidth", "getSafeHeight", "getSafeX", "getSafeY", "getNeedAdapterScreen", "()Z", "getDrawUiOutsideSafeArea", "getFrameRate", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getRealWidth", "getRealHeight", "getDefaultWidth", "getDefaultHeight", "getResolutionAccurate", "()Ljava/lang/Boolean;", "Ljava/lang/Boolean;", "mediaFormatOverrides", "", "", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "copy", "(IIIIIIZZLjava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Boolean;)Lcom/carplay/adapter/carlife/CarLifeEncoderFormat;", "equals", "other", "hashCode", "toString", "Companion", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class CarLifeEncoderFormat {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final int MAX_PANEL_DIMENSION = 16384;
    private static final int MIN_PANEL_HEIGHT = 160;
    private static final int MIN_PANEL_WIDTH = 320;
    private static final int MIN_SAFE_HEIGHT = 160;
    private static final int MIN_SAFE_WIDTH = 320;
    private final Integer defaultHeight;
    private final Integer defaultWidth;
    private final boolean drawUiOutsideSafeArea;
    private final Integer frameRate;
    private final boolean needAdapterScreen;
    private final int panelHeight;
    private final int panelWidth;
    private final Integer realHeight;
    private final Integer realWidth;
    private final Boolean resolutionAccurate;
    private final int safeHeight;
    private final int safeWidth;
    private final int safeX;
    private final int safeY;

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getPanelWidth() {
        return this.panelWidth;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final Integer getRealWidth() {
        return this.realWidth;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final Integer getRealHeight() {
        return this.realHeight;
    }

    /* JADX INFO: renamed from: component12, reason: from getter */
    public final Integer getDefaultWidth() {
        return this.defaultWidth;
    }

    /* JADX INFO: renamed from: component13, reason: from getter */
    public final Integer getDefaultHeight() {
        return this.defaultHeight;
    }

    /* JADX INFO: renamed from: component14, reason: from getter */
    public final Boolean getResolutionAccurate() {
        return this.resolutionAccurate;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getPanelHeight() {
        return this.panelHeight;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int getSafeWidth() {
        return this.safeWidth;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int getSafeHeight() {
        return this.safeHeight;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final int getSafeX() {
        return this.safeX;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final int getSafeY() {
        return this.safeY;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final boolean getNeedAdapterScreen() {
        return this.needAdapterScreen;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final boolean getDrawUiOutsideSafeArea() {
        return this.drawUiOutsideSafeArea;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final Integer getFrameRate() {
        return this.frameRate;
    }

    public final CarLifeEncoderFormat copy(int panelWidth, int panelHeight, int safeWidth, int safeHeight, int safeX, int safeY, boolean needAdapterScreen, boolean drawUiOutsideSafeArea, Integer frameRate, Integer realWidth, Integer realHeight, Integer defaultWidth, Integer defaultHeight, Boolean resolutionAccurate) {
        return new CarLifeEncoderFormat(panelWidth, panelHeight, safeWidth, safeHeight, safeX, safeY, needAdapterScreen, drawUiOutsideSafeArea, frameRate, realWidth, realHeight, defaultWidth, defaultHeight, resolutionAccurate);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CarLifeEncoderFormat)) {
            return false;
        }
        CarLifeEncoderFormat carLifeEncoderFormat = (CarLifeEncoderFormat) other;
        return this.panelWidth == carLifeEncoderFormat.panelWidth && this.panelHeight == carLifeEncoderFormat.panelHeight && this.safeWidth == carLifeEncoderFormat.safeWidth && this.safeHeight == carLifeEncoderFormat.safeHeight && this.safeX == carLifeEncoderFormat.safeX && this.safeY == carLifeEncoderFormat.safeY && this.needAdapterScreen == carLifeEncoderFormat.needAdapterScreen && this.drawUiOutsideSafeArea == carLifeEncoderFormat.drawUiOutsideSafeArea && Intrinsics.areEqual(this.frameRate, carLifeEncoderFormat.frameRate) && Intrinsics.areEqual(this.realWidth, carLifeEncoderFormat.realWidth) && Intrinsics.areEqual(this.realHeight, carLifeEncoderFormat.realHeight) && Intrinsics.areEqual(this.defaultWidth, carLifeEncoderFormat.defaultWidth) && Intrinsics.areEqual(this.defaultHeight, carLifeEncoderFormat.defaultHeight) && Intrinsics.areEqual(this.resolutionAccurate, carLifeEncoderFormat.resolutionAccurate);
    }

    public int hashCode() {
        return (((((((((((((((((((((((((Integer.hashCode(this.panelWidth) * 31) + Integer.hashCode(this.panelHeight)) * 31) + Integer.hashCode(this.safeWidth)) * 31) + Integer.hashCode(this.safeHeight)) * 31) + Integer.hashCode(this.safeX)) * 31) + Integer.hashCode(this.safeY)) * 31) + Boolean.hashCode(this.needAdapterScreen)) * 31) + Boolean.hashCode(this.drawUiOutsideSafeArea)) * 31) + (this.frameRate == null ? 0 : this.frameRate.hashCode())) * 31) + (this.realWidth == null ? 0 : this.realWidth.hashCode())) * 31) + (this.realHeight == null ? 0 : this.realHeight.hashCode())) * 31) + (this.defaultWidth == null ? 0 : this.defaultWidth.hashCode())) * 31) + (this.defaultHeight == null ? 0 : this.defaultHeight.hashCode())) * 31) + (this.resolutionAccurate != null ? this.resolutionAccurate.hashCode() : 0);
    }

    public String toString() {
        return "CarLifeEncoderFormat(panelWidth=" + this.panelWidth + ", panelHeight=" + this.panelHeight + ", safeWidth=" + this.safeWidth + ", safeHeight=" + this.safeHeight + ", safeX=" + this.safeX + ", safeY=" + this.safeY + ", needAdapterScreen=" + this.needAdapterScreen + ", drawUiOutsideSafeArea=" + this.drawUiOutsideSafeArea + ", frameRate=" + this.frameRate + ", realWidth=" + this.realWidth + ", realHeight=" + this.realHeight + ", defaultWidth=" + this.defaultWidth + ", defaultHeight=" + this.defaultHeight + ", resolutionAccurate=" + this.resolutionAccurate + ")";
    }

    public CarLifeEncoderFormat(int panelWidth, int panelHeight, int safeWidth, int safeHeight, int safeX, int safeY, boolean needAdapterScreen, boolean drawUiOutsideSafeArea, Integer frameRate, Integer realWidth, Integer realHeight, Integer defaultWidth, Integer defaultHeight, Boolean resolutionAccurate) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.safeWidth = safeWidth;
        this.safeHeight = safeHeight;
        this.safeX = safeX;
        this.safeY = safeY;
        this.needAdapterScreen = needAdapterScreen;
        this.drawUiOutsideSafeArea = drawUiOutsideSafeArea;
        this.frameRate = frameRate;
        this.realWidth = realWidth;
        this.realHeight = realHeight;
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        this.resolutionAccurate = resolutionAccurate;
    }

    public final int getPanelWidth() {
        return this.panelWidth;
    }

    public final int getPanelHeight() {
        return this.panelHeight;
    }

    public final int getSafeWidth() {
        return this.safeWidth;
    }

    public final int getSafeHeight() {
        return this.safeHeight;
    }

    public final int getSafeX() {
        return this.safeX;
    }

    public final int getSafeY() {
        return this.safeY;
    }

    public final boolean getNeedAdapterScreen() {
        return this.needAdapterScreen;
    }

    public final boolean getDrawUiOutsideSafeArea() {
        return this.drawUiOutsideSafeArea;
    }

    public final Integer getFrameRate() {
        return this.frameRate;
    }

    public final Integer getRealWidth() {
        return this.realWidth;
    }

    public final Integer getRealHeight() {
        return this.realHeight;
    }

    public final Integer getDefaultWidth() {
        return this.defaultWidth;
    }

    public final Integer getDefaultHeight() {
        return this.defaultHeight;
    }

    public final Boolean getResolutionAccurate() {
        return this.resolutionAccurate;
    }

    public final Map<String, Object> mediaFormatOverrides() {
        Map mapCreateMapBuilder = MapsKt.createMapBuilder();
        mapCreateMapBuilder.put("save_area_width", Integer.valueOf(this.safeWidth));
        mapCreateMapBuilder.put("save_area_height", Integer.valueOf(this.safeHeight));
        mapCreateMapBuilder.put("save_area_x", Integer.valueOf(this.safeX));
        mapCreateMapBuilder.put("save_area_y", Integer.valueOf(this.safeY));
        mapCreateMapBuilder.put("need_adapter_screen", Integer.valueOf(this.needAdapterScreen ? 1 : 0));
        mapCreateMapBuilder.put("draw_ui_outside_safe_area", Integer.valueOf(this.drawUiOutsideSafeArea ? 1 : 0));
        return MapsKt.build(mapCreateMapBuilder);
    }

    /* JADX INFO: compiled from: CarLifeEncoderFormat.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007J\u001b\u0010\b\u001a\u0004\u0018\u00010\t*\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0007H\u0002¢\u0006\u0002\u0010\fJ\u001b\u0010\r\u001a\u0004\u0018\u00010\u000e*\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0007H\u0002¢\u0006\u0002\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeEncoderFormat$Companion;", "", "<init>", "()V", "fromJson", "Lcom/carplay/adapter/carlife/CarLifeEncoderFormat;", "params", "", "int", "", "Lorg/json/JSONObject;", "key", "(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/Integer;", "boolean", "", "(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/Boolean;", "MIN_PANEL_WIDTH", "MIN_PANEL_HEIGHT", "MIN_SAFE_WIDTH", "MIN_SAFE_HEIGHT", "MAX_PANEL_DIMENSION", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final CarLifeEncoderFormat fromJson(String params) {
            Object objM159constructorimpl;
            Integer numM47int;
            String str = params;
            if (str == null || StringsKt.isBlank(str)) {
                return null;
            }
            try {
                Result.Companion companion = Result.INSTANCE;
                Companion companion2 = this;
                objM159constructorimpl = Result.m159constructorimpl(new JSONObject(params));
            } catch (Throwable th) {
                Result.Companion companion3 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            if (Result.m165isFailureimpl(objM159constructorimpl)) {
                objM159constructorimpl = null;
            }
            JSONObject json = (JSONObject) objM159constructorimpl;
            if (json == null || (numM47int = m47int(json, "width")) == null) {
                return null;
            }
            int panelWidth = numM47int.intValue();
            Integer numM47int2 = m47int(json, "height");
            if (numM47int2 == null) {
                return null;
            }
            int panelHeight = numM47int2.intValue();
            Integer numM47int3 = m47int(json, "save_area_width");
            if (numM47int3 == null) {
                return null;
            }
            int safeWidth = numM47int3.intValue();
            Integer numM47int4 = m47int(json, "save_area_height");
            if (numM47int4 == null) {
                return null;
            }
            int safeHeight = numM47int4.intValue();
            Integer numM47int5 = m47int(json, "save_area_x");
            if (numM47int5 == null) {
                return null;
            }
            int safeX = numM47int5.intValue();
            Integer numM47int6 = m47int(json, "save_area_y");
            if (numM47int6 == null) {
                return null;
            }
            int safeY = numM47int6.intValue();
            if (!(320 <= panelWidth && panelWidth < 16385)) {
                return null;
            }
            if (!(160 <= panelHeight && panelHeight < 16385)) {
                return null;
            }
            if (!(320 <= safeWidth && safeWidth <= panelWidth)) {
                return null;
            }
            if (!(160 <= safeHeight && safeHeight <= panelHeight) || safeX < 0 || safeY < 0 || ((long) safeX) + ((long) safeWidth) > panelWidth || ((long) safeY) + ((long) safeHeight) > panelHeight) {
                return null;
            }
            Integer numM47int7 = m47int(json, "need_adapter_screen");
            boolean z = numM47int7 != null && numM47int7.intValue() == 1;
            Integer numM47int8 = m47int(json, "draw_ui_outside_safe_area");
            boolean z2 = numM47int8 != null && numM47int8.intValue() == 1;
            Integer numM47int9 = m47int(json, "frame-rate");
            if (numM47int9 == null) {
                numM47int9 = m47int(json, "frame_rate");
            }
            return new CarLifeEncoderFormat(panelWidth, panelHeight, safeWidth, safeHeight, safeX, safeY, z, z2, numM47int9, m47int(json, "real_width"), m47int(json, "real_height"), m47int(json, "default_width"), m47int(json, "default_height"), m46boolean(json, "res_accuracy"));
        }

        /* JADX INFO: renamed from: int, reason: not valid java name */
        private final Integer m47int(JSONObject $this$int, String key) {
            Object value = $this$int.opt(key);
            if (value instanceof Number) {
                return Integer.valueOf(((Number) value).intValue());
            }
            if (value instanceof String) {
                return StringsKt.toIntOrNull((String) value);
            }
            return null;
        }

        /* JADX INFO: renamed from: boolean, reason: not valid java name */
        private final Boolean m46boolean(JSONObject $this$boolean, String key) {
            Object value = $this$boolean.opt(key);
            if (value instanceof Boolean) {
                return (Boolean) value;
            }
            if (value instanceof Number) {
                return Boolean.valueOf(((Number) value).intValue() != 0);
            }
            if (value instanceof String) {
                return StringsKt.toBooleanStrictOrNull((String) value);
            }
            return null;
        }
    }
}
