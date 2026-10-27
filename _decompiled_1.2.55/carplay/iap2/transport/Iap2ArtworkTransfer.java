package com.carplay.iap2.transport;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2FileTransferReceiver.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0015"}, d2 = {"Lcom/carplay/iap2/transport/Iap2ArtworkTransfer;", "", "id", "", "bytes", "", "<init>", "(I[B)V", "getId", "()I", "getBytes", "()[B", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class Iap2ArtworkTransfer {
    private final byte[] bytes;
    private final int id;

    public static /* synthetic */ Iap2ArtworkTransfer copy$default(Iap2ArtworkTransfer iap2ArtworkTransfer, int i, byte[] bArr, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = iap2ArtworkTransfer.id;
        }
        if ((i2 & 2) != 0) {
            bArr = iap2ArtworkTransfer.bytes;
        }
        return iap2ArtworkTransfer.copy(i, bArr);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getId() {
        return this.id;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final byte[] getBytes() {
        return this.bytes;
    }

    public final Iap2ArtworkTransfer copy(int id, byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return new Iap2ArtworkTransfer(id, bytes);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Iap2ArtworkTransfer)) {
            return false;
        }
        Iap2ArtworkTransfer iap2ArtworkTransfer = (Iap2ArtworkTransfer) other;
        return this.id == iap2ArtworkTransfer.id && Intrinsics.areEqual(this.bytes, iap2ArtworkTransfer.bytes);
    }

    public int hashCode() {
        return (Integer.hashCode(this.id) * 31) + Arrays.hashCode(this.bytes);
    }

    public String toString() {
        return "Iap2ArtworkTransfer(id=" + this.id + ", bytes=" + Arrays.toString(this.bytes) + ")";
    }

    public Iap2ArtworkTransfer(int id, byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        this.id = id;
        this.bytes = bytes;
    }

    public final byte[] getBytes() {
        return this.bytes;
    }

    public final int getId() {
        return this.id;
    }
}
