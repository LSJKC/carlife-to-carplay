package com.carplay.iap2.message;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Iap2Messages.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001J\t\u0010\u0014\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0015"}, d2 = {"Lcom/carplay/iap2/message/Iap2ClusterAsset;", "", "assetId", "", "assetVersion", "", "<init>", "(Ljava/lang/String;J)V", "getAssetId", "()Ljava/lang/String;", "getAssetVersion", "()J", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class Iap2ClusterAsset {
    private final String assetId;
    private final long assetVersion;

    public static /* synthetic */ Iap2ClusterAsset copy$default(Iap2ClusterAsset iap2ClusterAsset, String str, long j, int i, Object obj) {
        if ((i & 1) != 0) {
            str = iap2ClusterAsset.assetId;
        }
        if ((i & 2) != 0) {
            j = iap2ClusterAsset.assetVersion;
        }
        return iap2ClusterAsset.copy(str, j);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getAssetId() {
        return this.assetId;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final long getAssetVersion() {
        return this.assetVersion;
    }

    public final Iap2ClusterAsset copy(String assetId, long assetVersion) {
        Intrinsics.checkNotNullParameter(assetId, "assetId");
        return new Iap2ClusterAsset(assetId, assetVersion);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Iap2ClusterAsset)) {
            return false;
        }
        Iap2ClusterAsset iap2ClusterAsset = (Iap2ClusterAsset) other;
        return Intrinsics.areEqual(this.assetId, iap2ClusterAsset.assetId) && this.assetVersion == iap2ClusterAsset.assetVersion;
    }

    public int hashCode() {
        return (this.assetId.hashCode() * 31) + Long.hashCode(this.assetVersion);
    }

    public String toString() {
        return "Iap2ClusterAsset(assetId=" + this.assetId + ", assetVersion=" + this.assetVersion + ")";
    }

    public Iap2ClusterAsset(String assetId, long assetVersion) {
        Intrinsics.checkNotNullParameter(assetId, "assetId");
        this.assetId = assetId;
        this.assetVersion = assetVersion;
        if (StringsKt.isBlank(this.assetId)) {
            throw new IllegalArgumentException("cluster asset ID must not be blank".toString());
        }
        long j = this.assetVersion;
        boolean z = false;
        if (0 <= j && j < 4294967296L) {
            z = true;
        }
        if (!z) {
            throw new IllegalArgumentException("cluster asset version must fit in u32".toString());
        }
    }

    public final String getAssetId() {
        return this.assetId;
    }

    public final long getAssetVersion() {
        return this.assetVersion;
    }
}
