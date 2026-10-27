package com.carplay.airplay;

import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AirPlayConfig.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b,\b\u0086\b\u0018\u00002\u00020\u0001B\u009b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\n\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000f\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u0012\u0010\b\u0002\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\u0013\u0018\u00010\u0012\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0003¢\u0006\u0004\b\u0015\u0010\u0016J\t\u0010,\u001a\u00020\u0003HÆ\u0003J\t\u0010-\u001a\u00020\u0003HÆ\u0003J\u0010\u0010.\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u001bJ\u0010\u0010/\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u001bJ\t\u00100\u001a\u00020\u0003HÆ\u0003J\t\u00101\u001a\u00020\u0003HÆ\u0003J\u000b\u00102\u001a\u0004\u0018\u00010\nHÆ\u0003J\u000b\u00103\u001a\u0004\u0018\u00010\nHÆ\u0003J\u0010\u00104\u001a\u0004\u0018\u00010\rHÆ\u0003¢\u0006\u0002\u0010$J\u000b\u00105\u001a\u0004\u0018\u00010\u000fHÆ\u0003J\u0010\u00106\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u001bJ\u0011\u00107\u001a\n\u0012\u0004\u0012\u00020\u0013\u0018\u00010\u0012HÆ\u0003J\t\u00108\u001a\u00020\u0003HÆ\u0003J¦\u0001\u00109\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00032\u0010\b\u0002\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\u0013\u0018\u00010\u00122\b\b\u0002\u0010\u0014\u001a\u00020\u0003HÆ\u0001¢\u0006\u0002\u0010:J\u0013\u0010;\u001a\u00020\r2\b\u0010<\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010=\u001a\u00020\u0003HÖ\u0001J\t\u0010>\u001a\u00020\u000fHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0018R\u0015\u0010\u0005\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\u001c\u001a\u0004\b\u001a\u0010\u001bR\u0015\u0010\u0006\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\u001c\u001a\u0004\b\u001d\u0010\u001bR\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0018R\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0018R\u0013\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\n¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010!R\u0015\u0010\f\u001a\u0004\u0018\u00010\r¢\u0006\n\n\u0002\u0010%\u001a\u0004\b#\u0010$R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u000f¢\u0006\b\n\u0000\u001a\u0004\b&\u0010'R\u0015\u0010\u0010\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\u001c\u001a\u0004\b(\u0010\u001bR\u0019\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\u0013\u0018\u00010\u0012¢\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0011\u0010\u0014\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b+\u0010\u0018¨\u0006?"}, d2 = {"Lcom/carplay/airplay/AirPlayDisplayConfig;", "", "widthPixels", "", "heightPixels", "widthPhysicalMm", "heightPhysicalMm", "fps", "primaryInputDevice", "viewArea", "Lcom/carplay/airplay/AirPlayInsets;", "safeArea", "safeAreaDrawOutside", "", "initialUrl", "", "features", "viewAreas", "", "Lcom/carplay/airplay/AirPlayViewArea;", "initialViewArea", "<init>", "(IILjava/lang/Integer;Ljava/lang/Integer;IILcom/carplay/airplay/AirPlayInsets;Lcom/carplay/airplay/AirPlayInsets;Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/Integer;Ljava/util/List;I)V", "getWidthPixels", "()I", "getHeightPixels", "getWidthPhysicalMm", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getHeightPhysicalMm", "getFps", "getPrimaryInputDevice", "getViewArea", "()Lcom/carplay/airplay/AirPlayInsets;", "getSafeArea", "getSafeAreaDrawOutside", "()Ljava/lang/Boolean;", "Ljava/lang/Boolean;", "getInitialUrl", "()Ljava/lang/String;", "getFeatures", "getViewAreas", "()Ljava/util/List;", "getInitialViewArea", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "copy", "(IILjava/lang/Integer;Ljava/lang/Integer;IILcom/carplay/airplay/AirPlayInsets;Lcom/carplay/airplay/AirPlayInsets;Ljava/lang/Boolean;Ljava/lang/String;Ljava/lang/Integer;Ljava/util/List;I)Lcom/carplay/airplay/AirPlayDisplayConfig;", "equals", "other", "hashCode", "toString", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class AirPlayDisplayConfig {
    private final Integer features;
    private final int fps;
    private final Integer heightPhysicalMm;
    private final int heightPixels;
    private final String initialUrl;
    private final int initialViewArea;
    private final int primaryInputDevice;
    private final AirPlayInsets safeArea;
    private final Boolean safeAreaDrawOutside;
    private final AirPlayInsets viewArea;
    private final List<AirPlayViewArea> viewAreas;
    private final Integer widthPhysicalMm;
    private final int widthPixels;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ AirPlayDisplayConfig copy$default(AirPlayDisplayConfig airPlayDisplayConfig, int i, int i2, Integer num, Integer num2, int i3, int i4, AirPlayInsets airPlayInsets, AirPlayInsets airPlayInsets2, Boolean bool, String str, Integer num3, List list, int i5, int i6, Object obj) {
        if ((i6 & 1) != 0) {
            i = airPlayDisplayConfig.widthPixels;
        }
        return airPlayDisplayConfig.copy(i, (i6 & 2) != 0 ? airPlayDisplayConfig.heightPixels : i2, (i6 & 4) != 0 ? airPlayDisplayConfig.widthPhysicalMm : num, (i6 & 8) != 0 ? airPlayDisplayConfig.heightPhysicalMm : num2, (i6 & 16) != 0 ? airPlayDisplayConfig.fps : i3, (i6 & 32) != 0 ? airPlayDisplayConfig.primaryInputDevice : i4, (i6 & 64) != 0 ? airPlayDisplayConfig.viewArea : airPlayInsets, (i6 & 128) != 0 ? airPlayDisplayConfig.safeArea : airPlayInsets2, (i6 & 256) != 0 ? airPlayDisplayConfig.safeAreaDrawOutside : bool, (i6 & 512) != 0 ? airPlayDisplayConfig.initialUrl : str, (i6 & 1024) != 0 ? airPlayDisplayConfig.features : num3, (i6 & 2048) != 0 ? airPlayDisplayConfig.viewAreas : list, (i6 & 4096) != 0 ? airPlayDisplayConfig.initialViewArea : i5);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getWidthPixels() {
        return this.widthPixels;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final String getInitialUrl() {
        return this.initialUrl;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final Integer getFeatures() {
        return this.features;
    }

    public final List<AirPlayViewArea> component12() {
        return this.viewAreas;
    }

    /* JADX INFO: renamed from: component13, reason: from getter */
    public final int getInitialViewArea() {
        return this.initialViewArea;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getHeightPixels() {
        return this.heightPixels;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final Integer getWidthPhysicalMm() {
        return this.widthPhysicalMm;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final Integer getHeightPhysicalMm() {
        return this.heightPhysicalMm;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final int getFps() {
        return this.fps;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final int getPrimaryInputDevice() {
        return this.primaryInputDevice;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final AirPlayInsets getViewArea() {
        return this.viewArea;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final AirPlayInsets getSafeArea() {
        return this.safeArea;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final Boolean getSafeAreaDrawOutside() {
        return this.safeAreaDrawOutside;
    }

    public final AirPlayDisplayConfig copy(int widthPixels, int heightPixels, Integer widthPhysicalMm, Integer heightPhysicalMm, int fps, int primaryInputDevice, AirPlayInsets viewArea, AirPlayInsets safeArea, Boolean safeAreaDrawOutside, String initialUrl, Integer features, List<AirPlayViewArea> viewAreas, int initialViewArea) {
        return new AirPlayDisplayConfig(widthPixels, heightPixels, widthPhysicalMm, heightPhysicalMm, fps, primaryInputDevice, viewArea, safeArea, safeAreaDrawOutside, initialUrl, features, viewAreas, initialViewArea);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AirPlayDisplayConfig)) {
            return false;
        }
        AirPlayDisplayConfig airPlayDisplayConfig = (AirPlayDisplayConfig) other;
        return this.widthPixels == airPlayDisplayConfig.widthPixels && this.heightPixels == airPlayDisplayConfig.heightPixels && Intrinsics.areEqual(this.widthPhysicalMm, airPlayDisplayConfig.widthPhysicalMm) && Intrinsics.areEqual(this.heightPhysicalMm, airPlayDisplayConfig.heightPhysicalMm) && this.fps == airPlayDisplayConfig.fps && this.primaryInputDevice == airPlayDisplayConfig.primaryInputDevice && Intrinsics.areEqual(this.viewArea, airPlayDisplayConfig.viewArea) && Intrinsics.areEqual(this.safeArea, airPlayDisplayConfig.safeArea) && Intrinsics.areEqual(this.safeAreaDrawOutside, airPlayDisplayConfig.safeAreaDrawOutside) && Intrinsics.areEqual(this.initialUrl, airPlayDisplayConfig.initialUrl) && Intrinsics.areEqual(this.features, airPlayDisplayConfig.features) && Intrinsics.areEqual(this.viewAreas, airPlayDisplayConfig.viewAreas) && this.initialViewArea == airPlayDisplayConfig.initialViewArea;
    }

    public int hashCode() {
        return (((((((((((((((((((((((Integer.hashCode(this.widthPixels) * 31) + Integer.hashCode(this.heightPixels)) * 31) + (this.widthPhysicalMm == null ? 0 : this.widthPhysicalMm.hashCode())) * 31) + (this.heightPhysicalMm == null ? 0 : this.heightPhysicalMm.hashCode())) * 31) + Integer.hashCode(this.fps)) * 31) + Integer.hashCode(this.primaryInputDevice)) * 31) + (this.viewArea == null ? 0 : this.viewArea.hashCode())) * 31) + (this.safeArea == null ? 0 : this.safeArea.hashCode())) * 31) + (this.safeAreaDrawOutside == null ? 0 : this.safeAreaDrawOutside.hashCode())) * 31) + (this.initialUrl == null ? 0 : this.initialUrl.hashCode())) * 31) + (this.features == null ? 0 : this.features.hashCode())) * 31) + (this.viewAreas != null ? this.viewAreas.hashCode() : 0)) * 31) + Integer.hashCode(this.initialViewArea);
    }

    public String toString() {
        return "AirPlayDisplayConfig(widthPixels=" + this.widthPixels + ", heightPixels=" + this.heightPixels + ", widthPhysicalMm=" + this.widthPhysicalMm + ", heightPhysicalMm=" + this.heightPhysicalMm + ", fps=" + this.fps + ", primaryInputDevice=" + this.primaryInputDevice + ", viewArea=" + this.viewArea + ", safeArea=" + this.safeArea + ", safeAreaDrawOutside=" + this.safeAreaDrawOutside + ", initialUrl=" + this.initialUrl + ", features=" + this.features + ", viewAreas=" + this.viewAreas + ", initialViewArea=" + this.initialViewArea + ")";
    }

    public AirPlayDisplayConfig(int widthPixels, int heightPixels, Integer widthPhysicalMm, Integer heightPhysicalMm, int fps, int primaryInputDevice, AirPlayInsets viewArea, AirPlayInsets safeArea, Boolean safeAreaDrawOutside, String initialUrl, Integer features, List<AirPlayViewArea> list, int initialViewArea) {
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
        this.widthPhysicalMm = widthPhysicalMm;
        this.heightPhysicalMm = heightPhysicalMm;
        this.fps = fps;
        this.primaryInputDevice = primaryInputDevice;
        this.viewArea = viewArea;
        this.safeArea = safeArea;
        this.safeAreaDrawOutside = safeAreaDrawOutside;
        this.initialUrl = initialUrl;
        this.features = features;
        this.viewAreas = list;
        this.initialViewArea = initialViewArea;
    }

    public /* synthetic */ AirPlayDisplayConfig(int i, int i2, Integer num, Integer num2, int i3, int i4, AirPlayInsets airPlayInsets, AirPlayInsets airPlayInsets2, Boolean bool, String str, Integer num3, List list, int i5, int i6, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, i2, (i6 & 4) != 0 ? null : num, (i6 & 8) != 0 ? null : num2, (i6 & 16) != 0 ? 60 : i3, (i6 & 32) != 0 ? 1 : i4, (i6 & 64) != 0 ? null : airPlayInsets, (i6 & 128) != 0 ? null : airPlayInsets2, (i6 & 256) != 0 ? null : bool, (i6 & 512) != 0 ? null : str, (i6 & 1024) != 0 ? null : num3, (i6 & 2048) != 0 ? null : list, (i6 & 4096) != 0 ? 0 : i5);
    }

    public final int getWidthPixels() {
        return this.widthPixels;
    }

    public final int getHeightPixels() {
        return this.heightPixels;
    }

    public final Integer getWidthPhysicalMm() {
        return this.widthPhysicalMm;
    }

    public final Integer getHeightPhysicalMm() {
        return this.heightPhysicalMm;
    }

    public final int getFps() {
        return this.fps;
    }

    public final int getPrimaryInputDevice() {
        return this.primaryInputDevice;
    }

    public final AirPlayInsets getViewArea() {
        return this.viewArea;
    }

    public final AirPlayInsets getSafeArea() {
        return this.safeArea;
    }

    public final Boolean getSafeAreaDrawOutside() {
        return this.safeAreaDrawOutside;
    }

    public final String getInitialUrl() {
        return this.initialUrl;
    }

    public final Integer getFeatures() {
        return this.features;
    }

    public final List<AirPlayViewArea> getViewAreas() {
        return this.viewAreas;
    }

    public final int getInitialViewArea() {
        return this.initialViewArea;
    }
}
