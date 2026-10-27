package com.carplay.adapter.carlife;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CarLifeVideoAssembler.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000f\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0080\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0007HÆ\u0003J'\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00072\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001J\t\u0010\u0018\u001a\u00020\u0019HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u001a"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeVideoFrame;", "", "data", "", "timestampSeconds", "", "keyFrame", "", "<init>", "([BJZ)V", "getData", "()[B", "getTimestampSeconds", "()J", "getKeyFrame", "()Z", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "", "toString", "", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class CarLifeVideoFrame {
    private final byte[] data;
    private final boolean keyFrame;
    private final long timestampSeconds;

    public static /* synthetic */ CarLifeVideoFrame copy$default(CarLifeVideoFrame carLifeVideoFrame, byte[] bArr, long j, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            bArr = carLifeVideoFrame.data;
        }
        if ((i & 2) != 0) {
            j = carLifeVideoFrame.timestampSeconds;
        }
        if ((i & 4) != 0) {
            z = carLifeVideoFrame.keyFrame;
        }
        return carLifeVideoFrame.copy(bArr, j, z);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final byte[] getData() {
        return this.data;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final long getTimestampSeconds() {
        return this.timestampSeconds;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final boolean getKeyFrame() {
        return this.keyFrame;
    }

    public final CarLifeVideoFrame copy(byte[] data, long timestampSeconds, boolean keyFrame) {
        Intrinsics.checkNotNullParameter(data, "data");
        return new CarLifeVideoFrame(data, timestampSeconds, keyFrame);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CarLifeVideoFrame)) {
            return false;
        }
        CarLifeVideoFrame carLifeVideoFrame = (CarLifeVideoFrame) other;
        return Intrinsics.areEqual(this.data, carLifeVideoFrame.data) && this.timestampSeconds == carLifeVideoFrame.timestampSeconds && this.keyFrame == carLifeVideoFrame.keyFrame;
    }

    public int hashCode() {
        return (((Arrays.hashCode(this.data) * 31) + Long.hashCode(this.timestampSeconds)) * 31) + Boolean.hashCode(this.keyFrame);
    }

    public String toString() {
        return "CarLifeVideoFrame(data=" + Arrays.toString(this.data) + ", timestampSeconds=" + this.timestampSeconds + ", keyFrame=" + this.keyFrame + ")";
    }

    public CarLifeVideoFrame(byte[] data, long timestampSeconds, boolean keyFrame) {
        Intrinsics.checkNotNullParameter(data, "data");
        this.data = data;
        this.timestampSeconds = timestampSeconds;
        this.keyFrame = keyFrame;
    }

    public final byte[] getData() {
        return this.data;
    }

    public final long getTimestampSeconds() {
        return this.timestampSeconds;
    }

    public final boolean getKeyFrame() {
        return this.keyFrame;
    }
}
