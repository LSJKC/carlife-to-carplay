package com.carplay.adapter.carlife;

import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: CarLifePanel.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\u0018\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BK\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\n¢\u0006\u0004\b\f\u0010\rJ4\u0010\u0018\u001a\u0010\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u001a\u0018\u00010\u00192\u0006\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u001a2\u0006\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u001e\u001a\u00020\u0003J8\u0010\u001f\u001a\u0010\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u001a\u0018\u00010\u00192\u0006\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u001a2\b\b\u0002\u0010\u001d\u001a\u00020\u00032\b\b\u0002\u0010\u001e\u001a\u00020\u0003J4\u0010 \u001a\u0010\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u001a\u0018\u00010\u00192\u0006\u0010!\u001a\u00020\u001a2\u0006\u0010\"\u001a\u00020\u001a2\u0006\u0010#\u001a\u00020\u00032\u0006\u0010$\u001a\u00020\u0003J4\u0010%\u001a\u0010\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u001a\u0018\u00010\u00192\u0006\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u001a2\u0006\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u001e\u001a\u00020\u0003J\t\u0010&\u001a\u00020\u0003HÆ\u0003J\t\u0010'\u001a\u00020\u0003HÆ\u0003J\t\u0010(\u001a\u00020\u0003HÆ\u0003J\t\u0010)\u001a\u00020\u0003HÆ\u0003J\t\u0010*\u001a\u00020\u0003HÆ\u0003J\t\u0010+\u001a\u00020\u0003HÆ\u0003J\t\u0010,\u001a\u00020\nHÆ\u0003J\t\u0010-\u001a\u00020\nHÆ\u0003JY\u0010.\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\nHÆ\u0001J\u0013\u0010/\u001a\u00020\n2\b\u00100\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u00101\u001a\u00020\u0003HÖ\u0001J\t\u00102\u001a\u000203HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000fR\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000fR\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000fR\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u000b\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0016¨\u00064"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifePanel;", "", "panelWidth", "", "panelHeight", "safeX", "safeY", "safeWidth", "safeHeight", "drawUiOutsideSafeArea", "", "needAdapterScreen", "<init>", "(IIIIIIZZ)V", "getPanelWidth", "()I", "getPanelHeight", "getSafeX", "getSafeY", "getSafeWidth", "getSafeHeight", "getDrawUiOutsideSafeArea", "()Z", "getNeedAdapterScreen", "mapTouchToSource", "Lkotlin/Pair;", "", "panelX", "panelY", "sourceWidth", "sourceHeight", "mapSafeTouchToSource", "mapVirtualTouchToPanel", "x", "y", "virtualWidth", "virtualHeight", "mapPanelTouchToSource", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "hashCode", "toString", "", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class CarLifePanel {
    private final boolean drawUiOutsideSafeArea;
    private final boolean needAdapterScreen;
    private final int panelHeight;
    private final int panelWidth;
    private final int safeHeight;
    private final int safeWidth;
    private final int safeX;
    private final int safeY;

    public static /* synthetic */ CarLifePanel copy$default(CarLifePanel carLifePanel, int i, int i2, int i3, int i4, int i5, int i6, boolean z, boolean z2, int i7, Object obj) {
        if ((i7 & 1) != 0) {
            i = carLifePanel.panelWidth;
        }
        if ((i7 & 2) != 0) {
            i2 = carLifePanel.panelHeight;
        }
        if ((i7 & 4) != 0) {
            i3 = carLifePanel.safeX;
        }
        if ((i7 & 8) != 0) {
            i4 = carLifePanel.safeY;
        }
        if ((i7 & 16) != 0) {
            i5 = carLifePanel.safeWidth;
        }
        if ((i7 & 32) != 0) {
            i6 = carLifePanel.safeHeight;
        }
        if ((i7 & 64) != 0) {
            z = carLifePanel.drawUiOutsideSafeArea;
        }
        if ((i7 & 128) != 0) {
            z2 = carLifePanel.needAdapterScreen;
        }
        boolean z3 = z;
        boolean z4 = z2;
        int i8 = i5;
        int i9 = i6;
        return carLifePanel.copy(i, i2, i3, i4, i8, i9, z3, z4);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getPanelWidth() {
        return this.panelWidth;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getPanelHeight() {
        return this.panelHeight;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int getSafeX() {
        return this.safeX;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int getSafeY() {
        return this.safeY;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final int getSafeWidth() {
        return this.safeWidth;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final int getSafeHeight() {
        return this.safeHeight;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final boolean getDrawUiOutsideSafeArea() {
        return this.drawUiOutsideSafeArea;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final boolean getNeedAdapterScreen() {
        return this.needAdapterScreen;
    }

    public final CarLifePanel copy(int panelWidth, int panelHeight, int safeX, int safeY, int safeWidth, int safeHeight, boolean drawUiOutsideSafeArea, boolean needAdapterScreen) {
        return new CarLifePanel(panelWidth, panelHeight, safeX, safeY, safeWidth, safeHeight, drawUiOutsideSafeArea, needAdapterScreen);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CarLifePanel)) {
            return false;
        }
        CarLifePanel carLifePanel = (CarLifePanel) other;
        return this.panelWidth == carLifePanel.panelWidth && this.panelHeight == carLifePanel.panelHeight && this.safeX == carLifePanel.safeX && this.safeY == carLifePanel.safeY && this.safeWidth == carLifePanel.safeWidth && this.safeHeight == carLifePanel.safeHeight && this.drawUiOutsideSafeArea == carLifePanel.drawUiOutsideSafeArea && this.needAdapterScreen == carLifePanel.needAdapterScreen;
    }

    public int hashCode() {
        return (((((((((((((Integer.hashCode(this.panelWidth) * 31) + Integer.hashCode(this.panelHeight)) * 31) + Integer.hashCode(this.safeX)) * 31) + Integer.hashCode(this.safeY)) * 31) + Integer.hashCode(this.safeWidth)) * 31) + Integer.hashCode(this.safeHeight)) * 31) + Boolean.hashCode(this.drawUiOutsideSafeArea)) * 31) + Boolean.hashCode(this.needAdapterScreen);
    }

    public String toString() {
        return "CarLifePanel(panelWidth=" + this.panelWidth + ", panelHeight=" + this.panelHeight + ", safeX=" + this.safeX + ", safeY=" + this.safeY + ", safeWidth=" + this.safeWidth + ", safeHeight=" + this.safeHeight + ", drawUiOutsideSafeArea=" + this.drawUiOutsideSafeArea + ", needAdapterScreen=" + this.needAdapterScreen + ")";
    }

    public CarLifePanel(int panelWidth, int panelHeight, int safeX, int safeY, int safeWidth, int safeHeight, boolean drawUiOutsideSafeArea, boolean needAdapterScreen) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.safeX = safeX;
        this.safeY = safeY;
        this.safeWidth = safeWidth;
        this.safeHeight = safeHeight;
        this.drawUiOutsideSafeArea = drawUiOutsideSafeArea;
        this.needAdapterScreen = needAdapterScreen;
        if (!(this.panelWidth > 0 && this.panelHeight > 0)) {
            throw new IllegalArgumentException("面板尺寸必须为正".toString());
        }
        if (!(this.safeWidth > 0 && this.safeHeight > 0)) {
            throw new IllegalArgumentException("安全区尺寸必须为正".toString());
        }
        if (!(this.safeX >= 0 && this.safeY >= 0)) {
            throw new IllegalArgumentException("安全区原点不能为负".toString());
        }
        if (!(this.safeX + this.safeWidth <= this.panelWidth)) {
            throw new IllegalArgumentException("安全区超出面板宽度".toString());
        }
        if (!(this.safeY + this.safeHeight <= this.panelHeight)) {
            throw new IllegalArgumentException("安全区超出面板高度".toString());
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ CarLifePanel(int i, int i2, int i3, int i4, int i5, int i6, boolean z, boolean z2, int i7, DefaultConstructorMarker defaultConstructorMarker) {
        boolean z3;
        z = (i7 & 64) != 0 ? true : z;
        if ((i7 & 128) == 0) {
            z3 = z2;
        } else {
            z3 = true;
        }
        this(i, i2, i3, i4, i5, i6, z, z3);
    }

    public final int getPanelWidth() {
        return this.panelWidth;
    }

    public final int getPanelHeight() {
        return this.panelHeight;
    }

    public final int getSafeX() {
        return this.safeX;
    }

    public final int getSafeY() {
        return this.safeY;
    }

    public final int getSafeWidth() {
        return this.safeWidth;
    }

    public final int getSafeHeight() {
        return this.safeHeight;
    }

    public final boolean getDrawUiOutsideSafeArea() {
        return this.drawUiOutsideSafeArea;
    }

    public final boolean getNeedAdapterScreen() {
        return this.needAdapterScreen;
    }

    public final Pair<Float, Float> mapTouchToSource(float panelX, float panelY, int sourceWidth, int sourceHeight) {
        if (sourceWidth <= 0 || sourceHeight <= 0 || panelX < 0.0f || panelX >= this.panelWidth || panelY < 0.0f || panelY >= this.panelHeight) {
            return null;
        }
        return TuplesKt.to(Float.valueOf((sourceWidth * panelX) / this.panelWidth), Float.valueOf((sourceHeight * panelY) / this.panelHeight));
    }

    public static /* synthetic */ Pair mapSafeTouchToSource$default(CarLifePanel carLifePanel, float f, float f2, int i, int i2, int i3, Object obj) {
        if ((i3 & 4) != 0) {
            i = carLifePanel.safeWidth;
        }
        if ((i3 & 8) != 0) {
            i2 = carLifePanel.safeHeight;
        }
        return carLifePanel.mapSafeTouchToSource(f, f2, i, i2);
    }

    public final Pair<Float, Float> mapSafeTouchToSource(float panelX, float panelY, int sourceWidth, int sourceHeight) {
        if (sourceWidth <= 0 || sourceHeight <= 0 || panelX < this.safeX || panelX >= this.safeX + this.safeWidth || panelY < this.safeY || panelY >= this.safeY + this.safeHeight) {
            return null;
        }
        return TuplesKt.to(Float.valueOf(RangesKt.coerceIn(((panelX - this.safeX) * sourceWidth) / this.safeWidth, 0.0f, sourceWidth - 1)), Float.valueOf(RangesKt.coerceIn(((panelY - this.safeY) * sourceHeight) / this.safeHeight, 0.0f, sourceHeight - 1)));
    }

    public final Pair<Float, Float> mapVirtualTouchToPanel(float x, float y, int virtualWidth, int virtualHeight) {
        if (virtualWidth <= 0 || virtualHeight <= 0 || x < 0.0f || y < 0.0f || x >= virtualWidth || y >= virtualHeight) {
            return null;
        }
        float virtualAspect = virtualWidth / virtualHeight;
        float safeAspect = this.safeWidth / this.safeHeight;
        float fullAspect = this.panelWidth / this.panelHeight;
        if (Math.abs(virtualAspect - safeAspect) < Math.abs(virtualAspect - fullAspect)) {
            return TuplesKt.to(Float.valueOf(this.safeX + ((this.safeWidth * x) / virtualWidth)), Float.valueOf(this.safeY + ((this.safeHeight * y) / virtualHeight)));
        }
        return TuplesKt.to(Float.valueOf((this.panelWidth * x) / virtualWidth), Float.valueOf((this.panelHeight * y) / virtualHeight));
    }

    public final Pair<Float, Float> mapPanelTouchToSource(float panelX, float panelY, int sourceWidth, int sourceHeight) {
        if (sourceWidth == this.safeWidth && sourceHeight == this.safeHeight) {
            return mapSafeTouchToSource(panelX, panelY, sourceWidth, sourceHeight);
        }
        return mapTouchToSource(panelX, panelY, sourceWidth, sourceHeight);
    }
}
