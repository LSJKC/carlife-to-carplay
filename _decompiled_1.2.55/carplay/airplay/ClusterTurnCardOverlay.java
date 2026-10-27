package com.carplay.airplay;

import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: ClusterTurnCardOverlay.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\f\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001 B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J.\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00052\u0006\u0010\u0017\u001a\u00020\u00052\u0006\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u001a\u001a\u00020\u0005J\u001c\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u001c\u001a\u00020\u00052\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00050\tJ\u001d\u0010\u001e\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00052\u0006\u0010\u0017\u001a\u00020\u0005H\u0000¢\u0006\u0002\b\u001fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\t¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\t¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000bR\u000e\u0010\u000e\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00050\t¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000bR\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\t¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000bR\u000e\u0010\u0013\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006!"}, d2 = {"Lcom/carplay/airplay/ClusterTurnCardOverlay;", "", "<init>", "()V", "STEP_PERCENT", "", "DEFAULT_X_PERCENT", "DEFAULT_Y_PERCENT", "xPercents", "", "getXPercents", "()Ljava/util/List;", "opacityPercents", "getOpacityPercents", "DEFAULT_OPACITY_PERCENT", "yPercents", "getYPercents", "sizePercents", "getSizePercents", "DEFAULT_SIZE_PERCENT", "card", "Lcom/carplay/airplay/ClusterTurnCardOverlay$CardRect;", "panelWidth", "panelHeight", "xPercent", "yPercent", "sizePercent", "snap", "value", "choices", "visibleWindow", "visibleWindow$core_airplay", "CardRect", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ClusterTurnCardOverlay {
    public static final int DEFAULT_OPACITY_PERCENT = 85;
    public static final int DEFAULT_SIZE_PERCENT = 55;
    public static final int DEFAULT_X_PERCENT = 76;
    public static final int DEFAULT_Y_PERCENT = 30;
    public static final int STEP_PERCENT = 1;
    public static final ClusterTurnCardOverlay INSTANCE = new ClusterTurnCardOverlay();
    private static final List<Integer> xPercents = CollectionsKt.toList(RangesKt.step(new IntRange(8, 92), 1));
    private static final List<Integer> opacityPercents = CollectionsKt.listOf((Object[]) new Integer[]{20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100});
    private static final List<Integer> yPercents = CollectionsKt.toList(RangesKt.step(new IntRange(12, 72), 1));
    private static final List<Integer> sizePercents = CollectionsKt.listOf((Object[]) new Integer[]{30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95});

    /* JADX INFO: compiled from: ClusterTurnCardOverlay.kt */
    @Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J1\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\n¨\u0006\u0019"}, d2 = {"Lcom/carplay/airplay/ClusterTurnCardOverlay$CardRect;", "", "left", "", "top", "width", "height", "<init>", "(IIII)V", "getLeft", "()I", "getTop", "getWidth", "getHeight", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class CardRect {
        private final int height;
        private final int left;
        private final int top;
        private final int width;

        public static /* synthetic */ CardRect copy$default(CardRect cardRect, int i, int i2, int i3, int i4, int i5, Object obj) {
            if ((i5 & 1) != 0) {
                i = cardRect.left;
            }
            if ((i5 & 2) != 0) {
                i2 = cardRect.top;
            }
            if ((i5 & 4) != 0) {
                i3 = cardRect.width;
            }
            if ((i5 & 8) != 0) {
                i4 = cardRect.height;
            }
            return cardRect.copy(i, i2, i3, i4);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final int getLeft() {
            return this.left;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final int getTop() {
            return this.top;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final int getWidth() {
            return this.width;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final int getHeight() {
            return this.height;
        }

        public final CardRect copy(int left, int top, int width, int height) {
            return new CardRect(left, top, width, height);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CardRect)) {
                return false;
            }
            CardRect cardRect = (CardRect) other;
            return this.left == cardRect.left && this.top == cardRect.top && this.width == cardRect.width && this.height == cardRect.height;
        }

        public int hashCode() {
            return (((((Integer.hashCode(this.left) * 31) + Integer.hashCode(this.top)) * 31) + Integer.hashCode(this.width)) * 31) + Integer.hashCode(this.height);
        }

        public String toString() {
            return "CardRect(left=" + this.left + ", top=" + this.top + ", width=" + this.width + ", height=" + this.height + ")";
        }

        public CardRect(int left, int top, int width, int height) {
            this.left = left;
            this.top = top;
            this.width = width;
            this.height = height;
        }

        public final int getHeight() {
            return this.height;
        }

        public final int getLeft() {
            return this.left;
        }

        public final int getTop() {
            return this.top;
        }

        public final int getWidth() {
            return this.width;
        }
    }

    private ClusterTurnCardOverlay() {
    }

    public final List<Integer> getXPercents() {
        return xPercents;
    }

    public final List<Integer> getOpacityPercents() {
        return opacityPercents;
    }

    public final List<Integer> getYPercents() {
        return yPercents;
    }

    public final List<Integer> getSizePercents() {
        return sizePercents;
    }

    public final CardRect card(int panelWidth, int panelHeight, int xPercent, int yPercent, int sizePercent) {
        if (!(panelWidth > 0 && panelHeight > 0)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        CardRect window = visibleWindow$core_airplay(panelWidth, panelHeight);
        int size = snap(sizePercent, sizePercents);
        int width = RangesKt.coerceAtMost(RangesKt.coerceAtLeast((window.getWidth() * size) / 100, 150), panelWidth);
        int height = RangesKt.coerceAtMost(RangesKt.coerceAtLeast((int) (width / 2.64f), 58), (int) (window.getHeight() * 0.55f));
        int x = snap(xPercent, xPercents);
        int y = snap(yPercent, yPercents);
        int left = RangesKt.coerceIn(((panelWidth * x) / 100) - (width / 2), 0, panelWidth - width);
        int top = RangesKt.coerceIn(((panelHeight * y) / 100) - (height / 2), 0, panelHeight - height);
        return new CardRect(left, top, width, height);
    }

    public final int snap(int value, List<Integer> choices) {
        Object minElem$iv;
        Intrinsics.checkNotNullParameter(choices, "choices");
        List<Integer> $this$minByOrNull$iv = choices;
        Iterator iterator$iv = $this$minByOrNull$iv.iterator();
        if (iterator$iv.hasNext()) {
            minElem$iv = iterator$iv.next();
            if (iterator$iv.hasNext()) {
                int it = ((Number) minElem$iv).intValue();
                int minValue$iv = Math.abs(it - value);
                do {
                    Object e$iv = iterator$iv.next();
                    int it2 = ((Number) e$iv).intValue();
                    int v$iv = Math.abs(it2 - value);
                    if (minValue$iv > v$iv) {
                        minElem$iv = e$iv;
                        minValue$iv = v$iv;
                    }
                } while (iterator$iv.hasNext());
            }
        } else {
            minElem$iv = null;
        }
        Integer num = (Integer) minElem$iv;
        return num != null ? num.intValue() : ((Number) CollectionsKt.first((List) choices)).intValue();
    }

    public final CardRect visibleWindow$core_airplay(int panelWidth, int panelHeight) {
        AirPlayInsets area = CarPlayClusterDisplay.INSTANCE.getSAFE_AREA_PERCENT();
        int left = (area.getLeft() * panelWidth) / 100;
        int top = (area.getTop() * panelHeight) / 100;
        int width = (((100 - area.getLeft()) - area.getRight()) * panelWidth) / 100;
        int height = (((100 - area.getTop()) - area.getBottom()) * panelHeight) / 100;
        return new CardRect(left, top, RangesKt.coerceAtLeast(width, 1), RangesKt.coerceAtLeast(height, 1));
    }
}
