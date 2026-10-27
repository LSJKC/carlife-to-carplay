package com.carplay.airplay;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AirPlayConfig.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0006HÆ\u0003J'\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0015\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u0018"}, d2 = {"Lcom/carplay/airplay/AirPlayIcon;", "", "widthPixels", "", "heightPixels", "data", "", "<init>", "(II[B)V", "getWidthPixels", "()I", "getHeightPixels", "getData", "()[B", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class AirPlayIcon {
    private final byte[] data;
    private final int heightPixels;
    private final int widthPixels;

    public static /* synthetic */ AirPlayIcon copy$default(AirPlayIcon airPlayIcon, int i, int i2, byte[] bArr, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = airPlayIcon.widthPixels;
        }
        if ((i3 & 2) != 0) {
            i2 = airPlayIcon.heightPixels;
        }
        if ((i3 & 4) != 0) {
            bArr = airPlayIcon.data;
        }
        return airPlayIcon.copy(i, i2, bArr);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getWidthPixels() {
        return this.widthPixels;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getHeightPixels() {
        return this.heightPixels;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final byte[] getData() {
        return this.data;
    }

    public final AirPlayIcon copy(int widthPixels, int heightPixels, byte[] data) {
        Intrinsics.checkNotNullParameter(data, "data");
        return new AirPlayIcon(widthPixels, heightPixels, data);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AirPlayIcon)) {
            return false;
        }
        AirPlayIcon airPlayIcon = (AirPlayIcon) other;
        return this.widthPixels == airPlayIcon.widthPixels && this.heightPixels == airPlayIcon.heightPixels && Intrinsics.areEqual(this.data, airPlayIcon.data);
    }

    public int hashCode() {
        return (((Integer.hashCode(this.widthPixels) * 31) + Integer.hashCode(this.heightPixels)) * 31) + Arrays.hashCode(this.data);
    }

    public String toString() {
        return "AirPlayIcon(widthPixels=" + this.widthPixels + ", heightPixels=" + this.heightPixels + ", data=" + Arrays.toString(this.data) + ")";
    }

    public AirPlayIcon(int widthPixels, int heightPixels, byte[] data) {
        Intrinsics.checkNotNullParameter(data, "data");
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
        this.data = data;
    }

    public final int getWidthPixels() {
        return this.widthPixels;
    }

    public final int getHeightPixels() {
        return this.heightPixels;
    }

    public final byte[] getData() {
        return this.data;
    }
}
