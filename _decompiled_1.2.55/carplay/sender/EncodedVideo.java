package com.carplay.sender;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: VideoQueue.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0015\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B3\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\u0005¢\u0006\u0004\b\n\u0010\u000bJ\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0007HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0005HÆ\u0003J;\u0010\u0019\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u001a\u001a\u00020\u00072\b\u0010\u001b\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001c\u001a\u00020\u001dHÖ\u0001J\t\u0010\u001e\u001a\u00020\u001fHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000fR\u0011\u0010\t\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000f¨\u0006 "}, d2 = {"Lcom/carplay/sender/EncodedVideo;", "", "bytes", "", "ptsUs", "", "keyFrame", "", "queuedAtNanos", "epoch", "<init>", "([BJZJJ)V", "getBytes", "()[B", "getPtsUs", "()J", "getKeyFrame", "()Z", "getQueuedAtNanos", "getEpoch", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class EncodedVideo {
    private final byte[] bytes;
    private final long epoch;
    private final boolean keyFrame;
    private final long ptsUs;
    private final long queuedAtNanos;

    public static /* synthetic */ EncodedVideo copy$default(EncodedVideo encodedVideo, byte[] bArr, long j, boolean z, long j2, long j3, int i, Object obj) {
        if ((i & 1) != 0) {
            bArr = encodedVideo.bytes;
        }
        if ((i & 2) != 0) {
            j = encodedVideo.ptsUs;
        }
        if ((i & 4) != 0) {
            z = encodedVideo.keyFrame;
        }
        if ((i & 8) != 0) {
            j2 = encodedVideo.queuedAtNanos;
        }
        if ((i & 16) != 0) {
            j3 = encodedVideo.epoch;
        }
        boolean z2 = z;
        return encodedVideo.copy(bArr, j, z2, j2, j3);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final byte[] getBytes() {
        return this.bytes;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final long getPtsUs() {
        return this.ptsUs;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final boolean getKeyFrame() {
        return this.keyFrame;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final long getQueuedAtNanos() {
        return this.queuedAtNanos;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final long getEpoch() {
        return this.epoch;
    }

    public final EncodedVideo copy(byte[] bytes, long ptsUs, boolean keyFrame, long queuedAtNanos, long epoch) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return new EncodedVideo(bytes, ptsUs, keyFrame, queuedAtNanos, epoch);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EncodedVideo)) {
            return false;
        }
        EncodedVideo encodedVideo = (EncodedVideo) other;
        return Intrinsics.areEqual(this.bytes, encodedVideo.bytes) && this.ptsUs == encodedVideo.ptsUs && this.keyFrame == encodedVideo.keyFrame && this.queuedAtNanos == encodedVideo.queuedAtNanos && this.epoch == encodedVideo.epoch;
    }

    public int hashCode() {
        return (((((((Arrays.hashCode(this.bytes) * 31) + Long.hashCode(this.ptsUs)) * 31) + Boolean.hashCode(this.keyFrame)) * 31) + Long.hashCode(this.queuedAtNanos)) * 31) + Long.hashCode(this.epoch);
    }

    public String toString() {
        return "EncodedVideo(bytes=" + Arrays.toString(this.bytes) + ", ptsUs=" + this.ptsUs + ", keyFrame=" + this.keyFrame + ", queuedAtNanos=" + this.queuedAtNanos + ", epoch=" + this.epoch + ")";
    }

    public EncodedVideo(byte[] bytes, long ptsUs, boolean keyFrame, long queuedAtNanos, long epoch) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        this.bytes = bytes;
        this.ptsUs = ptsUs;
        this.keyFrame = keyFrame;
        this.queuedAtNanos = queuedAtNanos;
        this.epoch = epoch;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ EncodedVideo(byte[] bArr, long j, boolean z, long j2, long j3, int i, DefaultConstructorMarker defaultConstructorMarker) {
        long jNanoTime;
        long j4;
        if ((i & 8) == 0) {
            jNanoTime = j2;
        } else {
            jNanoTime = System.nanoTime();
        }
        if ((i & 16) == 0) {
            j4 = j3;
        } else {
            j4 = 0;
        }
        this(bArr, j, z, jNanoTime, j4);
    }

    public final byte[] getBytes() {
        return this.bytes;
    }

    public final long getPtsUs() {
        return this.ptsUs;
    }

    public final boolean getKeyFrame() {
        return this.keyFrame;
    }

    public final long getQueuedAtNanos() {
        return this.queuedAtNanos;
    }

    public final long getEpoch() {
        return this.epoch;
    }
}
