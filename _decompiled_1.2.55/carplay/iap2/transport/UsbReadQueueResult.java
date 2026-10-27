package com.carplay.iap2.transport;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: UsbReadQueuePolicy.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0013\n\u0002\u0010\u000e\n\u0000\b\u0080\b\u0018\u00002\u00020\u0001B#\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0005HÆ\u0003J\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\u000eJ.\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005HÆ\u0001¢\u0006\u0002\u0010\u0014J\u0013\u0010\u0015\u001a\u00020\u00032\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0017\u001a\u00020\u0005HÖ\u0001J\t\u0010\u0018\u001a\u00020\u0019HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0015\u0010\u0006\u001a\u0004\u0018\u00010\u0005¢\u0006\n\n\u0002\u0010\u000f\u001a\u0004\b\r\u0010\u000e¨\u0006\u001a"}, d2 = {"Lcom/carplay/iap2/transport/UsbReadQueueResult;", "", "queued", "", "firstBytes", "", "fallbackBytes", "<init>", "(ZILjava/lang/Integer;)V", "getQueued", "()Z", "getFirstBytes", "()I", "getFallbackBytes", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "component1", "component2", "component3", "copy", "(ZILjava/lang/Integer;)Lcom/carplay/iap2/transport/UsbReadQueueResult;", "equals", "other", "hashCode", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class UsbReadQueueResult {
    private final Integer fallbackBytes;
    private final int firstBytes;
    private final boolean queued;

    public static /* synthetic */ UsbReadQueueResult copy$default(UsbReadQueueResult usbReadQueueResult, boolean z, int i, Integer num, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            z = usbReadQueueResult.queued;
        }
        if ((i2 & 2) != 0) {
            i = usbReadQueueResult.firstBytes;
        }
        if ((i2 & 4) != 0) {
            num = usbReadQueueResult.fallbackBytes;
        }
        return usbReadQueueResult.copy(z, i, num);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final boolean getQueued() {
        return this.queued;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getFirstBytes() {
        return this.firstBytes;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final Integer getFallbackBytes() {
        return this.fallbackBytes;
    }

    public final UsbReadQueueResult copy(boolean queued, int firstBytes, Integer fallbackBytes) {
        return new UsbReadQueueResult(queued, firstBytes, fallbackBytes);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UsbReadQueueResult)) {
            return false;
        }
        UsbReadQueueResult usbReadQueueResult = (UsbReadQueueResult) other;
        return this.queued == usbReadQueueResult.queued && this.firstBytes == usbReadQueueResult.firstBytes && Intrinsics.areEqual(this.fallbackBytes, usbReadQueueResult.fallbackBytes);
    }

    public int hashCode() {
        return (((Boolean.hashCode(this.queued) * 31) + Integer.hashCode(this.firstBytes)) * 31) + (this.fallbackBytes == null ? 0 : this.fallbackBytes.hashCode());
    }

    public String toString() {
        return "UsbReadQueueResult(queued=" + this.queued + ", firstBytes=" + this.firstBytes + ", fallbackBytes=" + this.fallbackBytes + ")";
    }

    public UsbReadQueueResult(boolean queued, int firstBytes, Integer fallbackBytes) {
        this.queued = queued;
        this.firstBytes = firstBytes;
        this.fallbackBytes = fallbackBytes;
    }

    public /* synthetic */ UsbReadQueueResult(boolean z, int i, Integer num, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(z, i, (i2 & 4) != 0 ? null : num);
    }

    public final Integer getFallbackBytes() {
        return this.fallbackBytes;
    }

    public final int getFirstBytes() {
        return this.firstBytes;
    }

    public final boolean getQueued() {
        return this.queued;
    }
}
