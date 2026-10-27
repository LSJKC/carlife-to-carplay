package com.carplay.sender;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AirPlaySender.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\u0004\b\n\u0010\u000bJ\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0007HÆ\u0003J\u000b\u0010\u0018\u001a\u0004\u0018\u00010\tHÆ\u0003J?\u0010\u0019\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\tHÆ\u0001J\u0013\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001d\u001a\u00020\u0003HÖ\u0001J\t\u0010\u001e\u001a\u00020\u0007HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0013\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013¨\u0006\u001f"}, d2 = {"Lcom/carplay/sender/SenderDisplay;", "", "width", "", "height", "fps", "uuid", "", "safeArea", "Lcom/carplay/sender/SenderSafeArea;", "<init>", "(IIILjava/lang/String;Lcom/carplay/sender/SenderSafeArea;)V", "getWidth", "()I", "getHeight", "getFps", "getUuid", "()Ljava/lang/String;", "getSafeArea", "()Lcom/carplay/sender/SenderSafeArea;", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class SenderDisplay {
    private final int fps;
    private final int height;
    private final SenderSafeArea safeArea;
    private final String uuid;
    private final int width;

    public static /* synthetic */ SenderDisplay copy$default(SenderDisplay senderDisplay, int i, int i2, int i3, String str, SenderSafeArea senderSafeArea, int i4, Object obj) {
        if ((i4 & 1) != 0) {
            i = senderDisplay.width;
        }
        if ((i4 & 2) != 0) {
            i2 = senderDisplay.height;
        }
        if ((i4 & 4) != 0) {
            i3 = senderDisplay.fps;
        }
        if ((i4 & 8) != 0) {
            str = senderDisplay.uuid;
        }
        if ((i4 & 16) != 0) {
            senderSafeArea = senderDisplay.safeArea;
        }
        SenderSafeArea senderSafeArea2 = senderSafeArea;
        int i5 = i3;
        return senderDisplay.copy(i, i2, i5, str, senderSafeArea2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getWidth() {
        return this.width;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getHeight() {
        return this.height;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int getFps() {
        return this.fps;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final String getUuid() {
        return this.uuid;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final SenderSafeArea getSafeArea() {
        return this.safeArea;
    }

    public final SenderDisplay copy(int width, int height, int fps, String uuid, SenderSafeArea safeArea) {
        return new SenderDisplay(width, height, fps, uuid, safeArea);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SenderDisplay)) {
            return false;
        }
        SenderDisplay senderDisplay = (SenderDisplay) other;
        return this.width == senderDisplay.width && this.height == senderDisplay.height && this.fps == senderDisplay.fps && Intrinsics.areEqual(this.uuid, senderDisplay.uuid) && Intrinsics.areEqual(this.safeArea, senderDisplay.safeArea);
    }

    public int hashCode() {
        return (((((((Integer.hashCode(this.width) * 31) + Integer.hashCode(this.height)) * 31) + Integer.hashCode(this.fps)) * 31) + (this.uuid == null ? 0 : this.uuid.hashCode())) * 31) + (this.safeArea != null ? this.safeArea.hashCode() : 0);
    }

    public String toString() {
        return "SenderDisplay(width=" + this.width + ", height=" + this.height + ", fps=" + this.fps + ", uuid=" + this.uuid + ", safeArea=" + this.safeArea + ")";
    }

    public SenderDisplay(int width, int height, int fps, String uuid, SenderSafeArea safeArea) {
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.uuid = uuid;
        this.safeArea = safeArea;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ SenderDisplay(int i, int i2, int i3, String str, SenderSafeArea senderSafeArea, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        SenderSafeArea senderSafeArea2;
        if ((i4 & 16) == 0) {
            senderSafeArea2 = senderSafeArea;
        } else {
            senderSafeArea2 = null;
        }
        this(i, i2, i3, str, senderSafeArea2);
    }

    public final int getWidth() {
        return this.width;
    }

    public final int getHeight() {
        return this.height;
    }

    public final int getFps() {
        return this.fps;
    }

    public final String getUuid() {
        return this.uuid;
    }

    public final SenderSafeArea getSafeArea() {
        return this.safeArea;
    }
}
