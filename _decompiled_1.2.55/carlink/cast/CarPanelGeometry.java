package com.carlink.cast;

import com.carplay.adapter.carlife.CarLifePanel;
import com.carplay.sender.SenderDisplay;
import com.carplay.sender.SenderSafeArea;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import org.bouncycastle.asn1.BERTags;

/* JADX INFO: compiled from: CarPanelGeometry.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0014\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u000b\bÆ\u0002\u0018\u00002\u00020\u0001:\u0005,-./0B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u000b\u001a\u00020\fH\u0002J\u000e\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000fJ,\u0010\u0010\u001a\u0010\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u0012\u0018\u00010\u00112\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\fJ$\u0010\u0010\u001a\u0010\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u0012\u0018\u00010\u00112\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0012J$\u0010\u0016\u001a\u0010\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u0012\u0018\u00010\u00112\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0012J(\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u001a\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\f2\b\b\u0002\u0010\u001b\u001a\u00020\u001cJ\u0016\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u001a\u001a\u00020\u0005J\u001a\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\u0006\u0010\u0015\u001a\u00020\f2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u001e\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u001a\u001a\u00020\u0005J<\u0010\"\u001a\u0010\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u0012\u0018\u00010\u00112\u0006\u0010#\u001a\u00020\u00122\u0006\u0010$\u001a\u00020\u00122\u0006\u0010!\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u001a\u001a\u00020\u0005J(\u0010%\u001a\u00020&2\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u001a\u001a\u00020\u00052\u0006\u0010'\u001a\u00020\u00052\u0006\u0010(\u001a\u00020\u0005H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020&X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u00061"}, d2 = {"Lcom/carlink/cast/CarPanelGeometry;", "", "<init>", "()V", "PANEL_WIDTH", "", "PANEL_HEIGHT", "SAFE_WIDTH", "SAFE_HEIGHT", "SAFE_X", "SAFE_Y", "fallbackPanel", "Lcom/carplay/adapter/carlife/CarLifePanel;", "panelFor", "display", "Lcom/carplay/sender/SenderDisplay;", "mapDisplayTouchToPanel", "Lkotlin/Pair;", "", "normalizedX", "normalizedY", "panel", "mapSafePoint", "videoPlacement", "Lcom/carlink/cast/CarPanelGeometry$VideoPlacement;", "sourceWidth", "sourceHeight", "policy", "Lcom/carlink/cast/CarPanelGeometry$SafeAreaPolicy;", "resolveViewport", "Lcom/carlink/cast/CarPanelGeometry$Quad;", "videoTextureCoordinates", "", "placement", "mapVideoTouchToSource", "panelX", "panelY", "aspectError", "", "targetWidth", "targetHeight", "FULL_PANEL_ASPECT_TOLERANCE", "MIN_VIEWPORT_WIDTH", "MIN_VIEWPORT_HEIGHT", "SafeAreaMode", "SafeAreaPolicy", "VideoLayout", "VideoPlacement", "Quad", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarPanelGeometry {
    private static final double FULL_PANEL_ASPECT_TOLERANCE = 0.01d;
    public static final CarPanelGeometry INSTANCE = new CarPanelGeometry();
    private static final int MIN_VIEWPORT_HEIGHT = 160;
    private static final int MIN_VIEWPORT_WIDTH = 320;
    public static final int PANEL_HEIGHT = 520;
    public static final int PANEL_WIDTH = 1972;
    public static final int SAFE_HEIGHT = 476;
    public static final int SAFE_WIDTH = 1408;
    public static final int SAFE_X = 278;
    public static final int SAFE_Y = 44;

    /* JADX INFO: compiled from: CarPanelGeometry.kt */
    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007¨\u0006\b"}, d2 = {"Lcom/carlink/cast/CarPanelGeometry$SafeAreaMode;", "", "<init>", "(Ljava/lang/String;I)V", "STRICT", "RELAXED", "CUSTOM", "OFF", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public enum SafeAreaMode {
        STRICT,
        RELAXED,
        CUSTOM,
        OFF;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

        public static EnumEntries<SafeAreaMode> getEntries() {
            return $ENTRIES;
        }
    }

    /* JADX INFO: compiled from: CarPanelGeometry.kt */
    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006¨\u0006\u0007"}, d2 = {"Lcom/carlink/cast/CarPanelGeometry$VideoLayout;", "", "<init>", "(Ljava/lang/String;I)V", "FULL_PANEL", "SAFE_AREA", "SAFE_VIEWPORT", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public enum VideoLayout {
        FULL_PANEL,
        SAFE_AREA,
        SAFE_VIEWPORT;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

        public static EnumEntries<VideoLayout> getEntries() {
            return $ENTRIES;
        }
    }

    /* JADX INFO: compiled from: CarPanelGeometry.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[SafeAreaMode.values().length];
            try {
                iArr[SafeAreaMode.STRICT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[SafeAreaMode.RELAXED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[SafeAreaMode.CUSTOM.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[SafeAreaMode.OFF.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    private CarPanelGeometry() {
    }

    /* JADX INFO: compiled from: CarPanelGeometry.kt */
    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0019\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BC\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\u0007\u0012\b\b\u0002\u0010\n\u001a\u00020\u0007¢\u0006\u0004\b\u000b\u0010\fJ\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0007HÆ\u0003J\t\u0010\u0019\u001a\u00020\u0007HÆ\u0003J\t\u0010\u001a\u001a\u00020\u0007HÆ\u0003J\t\u0010\u001b\u001a\u00020\u0007HÆ\u0003JE\u0010\u001c\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u00072\b\b\u0002\u0010\n\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u001d\u001a\u00020\u00032\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001f\u001a\u00020\u0007HÖ\u0001J\t\u0010 \u001a\u00020!HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\b\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0012R\u0011\u0010\t\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u0011\u0010\n\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0012¨\u0006\""}, d2 = {"Lcom/carlink/cast/CarPanelGeometry$SafeAreaPolicy;", "", "enabled", "", "mode", "Lcom/carlink/cast/CarPanelGeometry$SafeAreaMode;", "left", "", "top", "right", "bottom", "<init>", "(ZLcom/carlink/cast/CarPanelGeometry$SafeAreaMode;IIII)V", "getEnabled", "()Z", "getMode", "()Lcom/carlink/cast/CarPanelGeometry$SafeAreaMode;", "getLeft", "()I", "getTop", "getRight", "getBottom", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "toString", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class SafeAreaPolicy {
        private final int bottom;
        private final boolean enabled;
        private final int left;
        private final SafeAreaMode mode;
        private final int right;
        private final int top;

        public SafeAreaPolicy() {
            this(false, null, 0, 0, 0, 0, 63, null);
        }

        public static /* synthetic */ SafeAreaPolicy copy$default(SafeAreaPolicy safeAreaPolicy, boolean z, SafeAreaMode safeAreaMode, int i, int i2, int i3, int i4, int i5, Object obj) {
            if ((i5 & 1) != 0) {
                z = safeAreaPolicy.enabled;
            }
            if ((i5 & 2) != 0) {
                safeAreaMode = safeAreaPolicy.mode;
            }
            if ((i5 & 4) != 0) {
                i = safeAreaPolicy.left;
            }
            if ((i5 & 8) != 0) {
                i2 = safeAreaPolicy.top;
            }
            if ((i5 & 16) != 0) {
                i3 = safeAreaPolicy.right;
            }
            if ((i5 & 32) != 0) {
                i4 = safeAreaPolicy.bottom;
            }
            int i6 = i3;
            int i7 = i4;
            return safeAreaPolicy.copy(z, safeAreaMode, i, i2, i6, i7);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final boolean getEnabled() {
            return this.enabled;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final SafeAreaMode getMode() {
            return this.mode;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final int getLeft() {
            return this.left;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final int getTop() {
            return this.top;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final int getRight() {
            return this.right;
        }

        /* JADX INFO: renamed from: component6, reason: from getter */
        public final int getBottom() {
            return this.bottom;
        }

        public final SafeAreaPolicy copy(boolean enabled, SafeAreaMode mode, int left, int top, int right, int bottom) {
            Intrinsics.checkNotNullParameter(mode, "mode");
            return new SafeAreaPolicy(enabled, mode, left, top, right, bottom);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof SafeAreaPolicy)) {
                return false;
            }
            SafeAreaPolicy safeAreaPolicy = (SafeAreaPolicy) other;
            return this.enabled == safeAreaPolicy.enabled && this.mode == safeAreaPolicy.mode && this.left == safeAreaPolicy.left && this.top == safeAreaPolicy.top && this.right == safeAreaPolicy.right && this.bottom == safeAreaPolicy.bottom;
        }

        public int hashCode() {
            return (((((((((Boolean.hashCode(this.enabled) * 31) + this.mode.hashCode()) * 31) + Integer.hashCode(this.left)) * 31) + Integer.hashCode(this.top)) * 31) + Integer.hashCode(this.right)) * 31) + Integer.hashCode(this.bottom);
        }

        public String toString() {
            return "SafeAreaPolicy(enabled=" + this.enabled + ", mode=" + this.mode + ", left=" + this.left + ", top=" + this.top + ", right=" + this.right + ", bottom=" + this.bottom + ")";
        }

        public SafeAreaPolicy(boolean enabled, SafeAreaMode mode, int left, int top, int right, int bottom) {
            Intrinsics.checkNotNullParameter(mode, "mode");
            this.enabled = enabled;
            this.mode = mode;
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public /* synthetic */ SafeAreaPolicy(boolean z, SafeAreaMode safeAreaMode, int i, int i2, int i3, int i4, int i5, DefaultConstructorMarker defaultConstructorMarker) {
            this((i5 & 1) != 0 ? false : z, (i5 & 2) != 0 ? SafeAreaMode.STRICT : safeAreaMode, (i5 & 4) != 0 ? 0 : i, (i5 & 8) != 0 ? 0 : i2, (i5 & 16) != 0 ? 0 : i3, (i5 & 32) != 0 ? 0 : i4);
        }

        public final boolean getEnabled() {
            return this.enabled;
        }

        public final SafeAreaMode getMode() {
            return this.mode;
        }

        public final int getLeft() {
            return this.left;
        }

        public final int getTop() {
            return this.top;
        }

        public final int getRight() {
            return this.right;
        }

        public final int getBottom() {
            return this.bottom;
        }
    }

    /* JADX INFO: compiled from: CarPanelGeometry.kt */
    @Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u001c\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BO\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\u0006\u0010\u000b\u001a\u00020\u0003\u0012\u0006\u0010\f\u001a\u00020\u0003¢\u0006\u0004\b\r\u0010\u000eJ\t\u0010\u001a\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001b\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001d\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001e\u001a\u00020\bHÆ\u0003J\t\u0010\u001f\u001a\u00020\u0003HÆ\u0003J\t\u0010 \u001a\u00020\u0003HÆ\u0003J\t\u0010!\u001a\u00020\u0003HÆ\u0003J\t\u0010\"\u001a\u00020\u0003HÆ\u0003Jc\u0010#\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u0003HÆ\u0001J\u0013\u0010$\u001a\u00020%2\b\u0010&\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010'\u001a\u00020\u0003HÖ\u0001J\t\u0010(\u001a\u00020)HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0010R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0010R\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0010R\u0011\u0010\u000b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0010R\u0011\u0010\f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0010¨\u0006*"}, d2 = {"Lcom/carlink/cast/CarPanelGeometry$VideoPlacement;", "", "x", "", "y", "width", "height", "layout", "Lcom/carlink/cast/CarPanelGeometry$VideoLayout;", "sourceX", "sourceY", "sourceWidth", "sourceHeight", "<init>", "(IIIILcom/carlink/cast/CarPanelGeometry$VideoLayout;IIII)V", "getX", "()I", "getY", "getWidth", "getHeight", "getLayout", "()Lcom/carlink/cast/CarPanelGeometry$VideoLayout;", "getSourceX", "getSourceY", "getSourceWidth", "getSourceHeight", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "toString", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class VideoPlacement {
        private final int height;
        private final VideoLayout layout;
        private final int sourceHeight;
        private final int sourceWidth;
        private final int sourceX;
        private final int sourceY;
        private final int width;
        private final int x;
        private final int y;

        public static /* synthetic */ VideoPlacement copy$default(VideoPlacement videoPlacement, int i, int i2, int i3, int i4, VideoLayout videoLayout, int i5, int i6, int i7, int i8, int i9, Object obj) {
            if ((i9 & 1) != 0) {
                i = videoPlacement.x;
            }
            if ((i9 & 2) != 0) {
                i2 = videoPlacement.y;
            }
            if ((i9 & 4) != 0) {
                i3 = videoPlacement.width;
            }
            if ((i9 & 8) != 0) {
                i4 = videoPlacement.height;
            }
            if ((i9 & 16) != 0) {
                videoLayout = videoPlacement.layout;
            }
            if ((i9 & 32) != 0) {
                i5 = videoPlacement.sourceX;
            }
            if ((i9 & 64) != 0) {
                i6 = videoPlacement.sourceY;
            }
            if ((i9 & 128) != 0) {
                i7 = videoPlacement.sourceWidth;
            }
            if ((i9 & 256) != 0) {
                i8 = videoPlacement.sourceHeight;
            }
            int i10 = i7;
            int i11 = i8;
            int i12 = i5;
            int i13 = i6;
            VideoLayout videoLayout2 = videoLayout;
            int i14 = i3;
            return videoPlacement.copy(i, i2, i14, i4, videoLayout2, i12, i13, i10, i11);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final int getX() {
            return this.x;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final int getY() {
            return this.y;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final int getWidth() {
            return this.width;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final int getHeight() {
            return this.height;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final VideoLayout getLayout() {
            return this.layout;
        }

        /* JADX INFO: renamed from: component6, reason: from getter */
        public final int getSourceX() {
            return this.sourceX;
        }

        /* JADX INFO: renamed from: component7, reason: from getter */
        public final int getSourceY() {
            return this.sourceY;
        }

        /* JADX INFO: renamed from: component8, reason: from getter */
        public final int getSourceWidth() {
            return this.sourceWidth;
        }

        /* JADX INFO: renamed from: component9, reason: from getter */
        public final int getSourceHeight() {
            return this.sourceHeight;
        }

        public final VideoPlacement copy(int x, int y, int width, int height, VideoLayout layout, int sourceX, int sourceY, int sourceWidth, int sourceHeight) {
            Intrinsics.checkNotNullParameter(layout, "layout");
            return new VideoPlacement(x, y, width, height, layout, sourceX, sourceY, sourceWidth, sourceHeight);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof VideoPlacement)) {
                return false;
            }
            VideoPlacement videoPlacement = (VideoPlacement) other;
            return this.x == videoPlacement.x && this.y == videoPlacement.y && this.width == videoPlacement.width && this.height == videoPlacement.height && this.layout == videoPlacement.layout && this.sourceX == videoPlacement.sourceX && this.sourceY == videoPlacement.sourceY && this.sourceWidth == videoPlacement.sourceWidth && this.sourceHeight == videoPlacement.sourceHeight;
        }

        public int hashCode() {
            return (((((((((((((((Integer.hashCode(this.x) * 31) + Integer.hashCode(this.y)) * 31) + Integer.hashCode(this.width)) * 31) + Integer.hashCode(this.height)) * 31) + this.layout.hashCode()) * 31) + Integer.hashCode(this.sourceX)) * 31) + Integer.hashCode(this.sourceY)) * 31) + Integer.hashCode(this.sourceWidth)) * 31) + Integer.hashCode(this.sourceHeight);
        }

        public String toString() {
            return "VideoPlacement(x=" + this.x + ", y=" + this.y + ", width=" + this.width + ", height=" + this.height + ", layout=" + this.layout + ", sourceX=" + this.sourceX + ", sourceY=" + this.sourceY + ", sourceWidth=" + this.sourceWidth + ", sourceHeight=" + this.sourceHeight + ")";
        }

        public VideoPlacement(int x, int y, int width, int height, VideoLayout layout, int sourceX, int sourceY, int sourceWidth, int sourceHeight) {
            Intrinsics.checkNotNullParameter(layout, "layout");
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.layout = layout;
            this.sourceX = sourceX;
            this.sourceY = sourceY;
            this.sourceWidth = sourceWidth;
            this.sourceHeight = sourceHeight;
        }

        public final int getX() {
            return this.x;
        }

        public final int getY() {
            return this.y;
        }

        public final int getWidth() {
            return this.width;
        }

        public final int getHeight() {
            return this.height;
        }

        public final VideoLayout getLayout() {
            return this.layout;
        }

        public final int getSourceX() {
            return this.sourceX;
        }

        public final int getSourceY() {
            return this.sourceY;
        }

        public final int getSourceWidth() {
            return this.sourceWidth;
        }

        public final int getSourceHeight() {
            return this.sourceHeight;
        }
    }

    private final CarLifePanel fallbackPanel() {
        return new CarLifePanel(PANEL_WIDTH, PANEL_HEIGHT, SAFE_X, 44, SAFE_WIDTH, SAFE_HEIGHT, false, false, BERTags.PRIVATE, null);
    }

    public final CarLifePanel panelFor(SenderDisplay display) {
        Intrinsics.checkNotNullParameter(display, "display");
        SenderSafeArea safe = display.getSafeArea();
        if (safe == null) {
            return new CarLifePanel(display.getWidth(), display.getHeight(), 0, 0, display.getWidth(), display.getHeight(), false, false, BERTags.PRIVATE, null);
        }
        return new CarLifePanel(display.getWidth(), display.getHeight(), safe.getX(), safe.getY(), safe.getWidth(), safe.getHeight(), false, false, BERTags.PRIVATE, null);
    }

    public final Pair<Float, Float> mapDisplayTouchToPanel(float normalizedX, float normalizedY, CarLifePanel panel) {
        Intrinsics.checkNotNullParameter(panel, "panel");
        boolean z = false;
        if (!(0.0f <= normalizedX && normalizedX <= 1.0f)) {
            return null;
        }
        if (0.0f <= normalizedY && normalizedY <= 1.0f) {
            z = true;
        }
        if (!z) {
            return null;
        }
        return TuplesKt.to(Float.valueOf((panel.getPanelWidth() - 1) * normalizedX), Float.valueOf((panel.getPanelHeight() - 1) * normalizedY));
    }

    public final Pair<Float, Float> mapDisplayTouchToPanel(float normalizedX, float normalizedY) {
        return mapDisplayTouchToPanel(normalizedX, normalizedY, fallbackPanel());
    }

    public final Pair<Float, Float> mapSafePoint(float normalizedX, float normalizedY) {
        if (0.0f <= normalizedX && normalizedX <= 1.0f) {
            if (!(0.0f <= normalizedY && normalizedY <= 1.0f)) {
                return null;
            }
            return TuplesKt.to(Float.valueOf(1408.0f * normalizedX), Float.valueOf(476.0f * normalizedY));
        }
        return null;
    }

    public static /* synthetic */ VideoPlacement videoPlacement$default(CarPanelGeometry carPanelGeometry, int i, int i2, CarLifePanel carLifePanel, SafeAreaPolicy safeAreaPolicy, int i3, Object obj) {
        if ((i3 & 8) != 0) {
            safeAreaPolicy = new SafeAreaPolicy(false, null, 0, 0, 0, 0, 63, null);
        }
        return carPanelGeometry.videoPlacement(i, i2, carLifePanel, safeAreaPolicy);
    }

    public final VideoPlacement videoPlacement(int sourceWidth, int sourceHeight, CarLifePanel panel, SafeAreaPolicy policy) {
        int i;
        int i2;
        VideoLayout videoLayout;
        Intrinsics.checkNotNullParameter(panel, "panel");
        Intrinsics.checkNotNullParameter(policy, "policy");
        if (!(sourceWidth > 0 && sourceHeight > 0)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        boolean exactPanel = sourceWidth == panel.getPanelWidth() && sourceHeight == panel.getPanelHeight();
        boolean exactSafe = sourceWidth == panel.getSafeWidth() && sourceHeight == panel.getSafeHeight();
        double fullPanelAspect = aspectError(sourceWidth, sourceHeight, panel.getPanelWidth(), panel.getPanelHeight());
        boolean looksLikeFullPanel = exactPanel || fullPanelAspect <= FULL_PANEL_ASPECT_TOLERANCE;
        boolean sourceIsSafeArea = exactSafe && !looksLikeFullPanel;
        Quad viewport = sourceIsSafeArea ? resolveViewport(panel, policy) : null;
        if (viewport != null) {
            int first = viewport.getFirst();
            int second = viewport.getSecond();
            int third = viewport.getThird();
            int fourth = viewport.getFourth();
            if (sourceIsSafeArea) {
                i = first;
                i2 = second;
                if (Intrinsics.areEqual(viewport, new Quad(panel.getSafeX(), panel.getSafeY(), panel.getSafeWidth(), panel.getSafeHeight()))) {
                    videoLayout = VideoLayout.SAFE_AREA;
                }
                return new VideoPlacement(i, i2, third, fourth, videoLayout, 0, 0, sourceWidth, sourceHeight);
            }
            i = first;
            i2 = second;
            videoLayout = VideoLayout.SAFE_VIEWPORT;
            return new VideoPlacement(i, i2, third, fourth, videoLayout, 0, 0, sourceWidth, sourceHeight);
        }
        return new VideoPlacement(sourceIsSafeArea ? panel.getSafeX() : 0, sourceIsSafeArea ? panel.getSafeY() : 0, sourceIsSafeArea ? panel.getSafeWidth() : panel.getPanelWidth(), sourceIsSafeArea ? panel.getSafeHeight() : panel.getPanelHeight(), sourceIsSafeArea ? VideoLayout.SAFE_AREA : VideoLayout.FULL_PANEL, 0, 0, sourceWidth, sourceHeight);
    }

    public final VideoPlacement videoPlacement(int sourceWidth, int sourceHeight) {
        return videoPlacement$default(this, sourceWidth, sourceHeight, fallbackPanel(), null, 8, null);
    }

    private final Quad resolveViewport(CarLifePanel panel, SafeAreaPolicy policy) {
        Quad margins;
        if (!policy.getEnabled() || policy.getMode() == SafeAreaMode.OFF) {
            return null;
        }
        switch (WhenMappings.$EnumSwitchMapping$0[policy.getMode().ordinal()]) {
            case 1:
                margins = new Quad(panel.getSafeX(), panel.getSafeY(), (panel.getPanelWidth() - panel.getSafeX()) - panel.getSafeWidth(), (panel.getPanelHeight() - panel.getSafeY()) - panel.getSafeHeight());
                break;
            case 2:
                margins = new Quad(panel.getSafeX() / 2, panel.getSafeY() / 2, ((panel.getPanelWidth() - panel.getSafeX()) - panel.getSafeWidth()) / 2, ((panel.getPanelHeight() - panel.getSafeY()) - panel.getSafeHeight()) / 2);
                break;
            case 3:
                margins = new Quad(RangesKt.coerceAtLeast(policy.getLeft(), 0), RangesKt.coerceAtLeast(policy.getTop(), 0), RangesKt.coerceAtLeast(policy.getRight(), 0), RangesKt.coerceAtLeast(policy.getBottom(), 0));
                break;
            case 4:
                return null;
            default:
                throw new NoWhenBranchMatchedException();
        }
        int x = margins.getFirst();
        int y = margins.getSecond();
        int width = (panel.getPanelWidth() - margins.getFirst()) - margins.getThird();
        int height = (panel.getPanelHeight() - margins.getSecond()) - margins.getFourth();
        if (width < 320 || height < 160 || x < 0 || y < 0 || x + width > panel.getPanelWidth() || y + height > panel.getPanelHeight()) {
            return null;
        }
        if (x == 0 && y == 0 && width == panel.getPanelWidth() && height == panel.getPanelHeight()) {
            return null;
        }
        return new Quad(x, y, width, height);
    }

    /* JADX INFO: compiled from: CarPanelGeometry.kt */
    @Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J1\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\n¨\u0006\u0019"}, d2 = {"Lcom/carlink/cast/CarPanelGeometry$Quad;", "", "first", "", "second", "third", "fourth", "<init>", "(IIII)V", "getFirst", "()I", "getSecond", "getThird", "getFourth", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class Quad {
        private final int first;
        private final int fourth;
        private final int second;
        private final int third;

        public static /* synthetic */ Quad copy$default(Quad quad, int i, int i2, int i3, int i4, int i5, Object obj) {
            if ((i5 & 1) != 0) {
                i = quad.first;
            }
            if ((i5 & 2) != 0) {
                i2 = quad.second;
            }
            if ((i5 & 4) != 0) {
                i3 = quad.third;
            }
            if ((i5 & 8) != 0) {
                i4 = quad.fourth;
            }
            return quad.copy(i, i2, i3, i4);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final int getFirst() {
            return this.first;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final int getSecond() {
            return this.second;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final int getThird() {
            return this.third;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final int getFourth() {
            return this.fourth;
        }

        public final Quad copy(int first, int second, int third, int fourth) {
            return new Quad(first, second, third, fourth);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Quad)) {
                return false;
            }
            Quad quad = (Quad) other;
            return this.first == quad.first && this.second == quad.second && this.third == quad.third && this.fourth == quad.fourth;
        }

        public int hashCode() {
            return (((((Integer.hashCode(this.first) * 31) + Integer.hashCode(this.second)) * 31) + Integer.hashCode(this.third)) * 31) + Integer.hashCode(this.fourth);
        }

        public String toString() {
            return "Quad(first=" + this.first + ", second=" + this.second + ", third=" + this.third + ", fourth=" + this.fourth + ")";
        }

        public Quad(int first, int second, int third, int fourth) {
            this.first = first;
            this.second = second;
            this.third = third;
            this.fourth = fourth;
        }

        public final int getFirst() {
            return this.first;
        }

        public final int getSecond() {
            return this.second;
        }

        public final int getThird() {
            return this.third;
        }

        public final int getFourth() {
            return this.fourth;
        }
    }

    public final float[] videoTextureCoordinates(VideoPlacement placement, int sourceWidth, int sourceHeight) {
        Intrinsics.checkNotNullParameter(placement, "placement");
        float u0 = placement.getSourceX() / sourceWidth;
        float v0 = placement.getSourceY() / sourceHeight;
        float u1 = (placement.getSourceX() + placement.getSourceWidth()) / sourceWidth;
        float v1 = (placement.getSourceY() + placement.getSourceHeight()) / sourceHeight;
        return new float[]{u0, v0, u1, v0, u0, v1, u1, v1};
    }

    public final Pair<Float, Float> mapVideoTouchToSource(float panelX, float panelY, VideoPlacement placement, int sourceWidth, int sourceHeight) {
        Intrinsics.checkNotNullParameter(placement, "placement");
        if (sourceWidth <= 0 || sourceHeight <= 0 || panelX < placement.getX() || panelY < placement.getY() || panelX >= placement.getX() + placement.getWidth() || panelY >= placement.getY() + placement.getHeight()) {
            return null;
        }
        float x = placement.getSourceX() + (((panelX - placement.getX()) * placement.getSourceWidth()) / placement.getWidth());
        float y = placement.getSourceY() + (((panelY - placement.getY()) * placement.getSourceHeight()) / placement.getHeight());
        return TuplesKt.to(Float.valueOf(RangesKt.coerceIn(x, 0.0f, sourceWidth - 1)), Float.valueOf(RangesKt.coerceIn(y, 0.0f, sourceHeight - 1)));
    }

    private final double aspectError(int sourceWidth, int sourceHeight, int targetWidth, int targetHeight) {
        return Math.abs((((double) sourceWidth) * ((double) targetHeight)) - (((double) targetWidth) * ((double) sourceHeight))) / (((double) targetWidth) * ((double) sourceHeight));
    }
}
