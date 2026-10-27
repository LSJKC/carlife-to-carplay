package com.carplay.adapter.carlife;

import java.util.Arrays;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CarLifeVideoAssembler.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000f\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0080\b\u0018\u00002\u00020\u0001B)\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b¢\u0006\u0004\b\t\u0010\nJ\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005HÆ\u0003J\t\u0010\u0013\u001a\u00020\bHÆ\u0003J/\u0010\u0014\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\b\b\u0002\u0010\u0007\u001a\u00020\bHÆ\u0001J\u0013\u0010\u0015\u001a\u00020\b2\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001J\t\u0010\u0019\u001a\u00020\u001aHÖ\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u001b"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeVideoBatch;", "", "avcC", "", "frames", "", "Lcom/carplay/adapter/carlife/CarLifeVideoFrame;", "reusedAvcC", "", "<init>", "([BLjava/util/List;Z)V", "getAvcC", "()[B", "getFrames", "()Ljava/util/List;", "getReusedAvcC", "()Z", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "", "toString", "", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class CarLifeVideoBatch {
    private final byte[] avcC;
    private final List<CarLifeVideoFrame> frames;
    private final boolean reusedAvcC;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ CarLifeVideoBatch copy$default(CarLifeVideoBatch carLifeVideoBatch, byte[] bArr, List list, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            bArr = carLifeVideoBatch.avcC;
        }
        if ((i & 2) != 0) {
            list = carLifeVideoBatch.frames;
        }
        if ((i & 4) != 0) {
            z = carLifeVideoBatch.reusedAvcC;
        }
        return carLifeVideoBatch.copy(bArr, list, z);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final byte[] getAvcC() {
        return this.avcC;
    }

    public final List<CarLifeVideoFrame> component2() {
        return this.frames;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final boolean getReusedAvcC() {
        return this.reusedAvcC;
    }

    public final CarLifeVideoBatch copy(byte[] avcC, List<CarLifeVideoFrame> frames, boolean reusedAvcC) {
        Intrinsics.checkNotNullParameter(frames, "frames");
        return new CarLifeVideoBatch(avcC, frames, reusedAvcC);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CarLifeVideoBatch)) {
            return false;
        }
        CarLifeVideoBatch carLifeVideoBatch = (CarLifeVideoBatch) other;
        return Intrinsics.areEqual(this.avcC, carLifeVideoBatch.avcC) && Intrinsics.areEqual(this.frames, carLifeVideoBatch.frames) && this.reusedAvcC == carLifeVideoBatch.reusedAvcC;
    }

    public int hashCode() {
        return ((((this.avcC == null ? 0 : Arrays.hashCode(this.avcC)) * 31) + this.frames.hashCode()) * 31) + Boolean.hashCode(this.reusedAvcC);
    }

    public String toString() {
        return "CarLifeVideoBatch(avcC=" + Arrays.toString(this.avcC) + ", frames=" + this.frames + ", reusedAvcC=" + this.reusedAvcC + ")";
    }

    public CarLifeVideoBatch(byte[] avcC, List<CarLifeVideoFrame> frames, boolean reusedAvcC) {
        Intrinsics.checkNotNullParameter(frames, "frames");
        this.avcC = avcC;
        this.frames = frames;
        this.reusedAvcC = reusedAvcC;
    }

    public /* synthetic */ CarLifeVideoBatch(byte[] bArr, List list, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(bArr, list, (i & 4) != 0 ? false : z);
    }

    public final byte[] getAvcC() {
        return this.avcC;
    }

    public final List<CarLifeVideoFrame> getFrames() {
        return this.frames;
    }

    public final boolean getReusedAvcC() {
        return this.reusedAvcC;
    }
}
