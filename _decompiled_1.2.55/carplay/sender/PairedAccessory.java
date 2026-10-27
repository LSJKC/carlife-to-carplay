package com.carplay.sender;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PhonePairing.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001J\t\u0010\u0014\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0015"}, d2 = {"Lcom/carplay/sender/PairedAccessory;", "", "id", "", "publicKey", "", "<init>", "(Ljava/lang/String;[B)V", "getId", "()Ljava/lang/String;", "getPublicKey", "()[B", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class PairedAccessory {
    private final String id;
    private final byte[] publicKey;

    public static /* synthetic */ PairedAccessory copy$default(PairedAccessory pairedAccessory, String str, byte[] bArr, int i, Object obj) {
        if ((i & 1) != 0) {
            str = pairedAccessory.id;
        }
        if ((i & 2) != 0) {
            bArr = pairedAccessory.publicKey;
        }
        return pairedAccessory.copy(str, bArr);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getId() {
        return this.id;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final byte[] getPublicKey() {
        return this.publicKey;
    }

    public final PairedAccessory copy(String id, byte[] publicKey) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(publicKey, "publicKey");
        return new PairedAccessory(id, publicKey);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PairedAccessory)) {
            return false;
        }
        PairedAccessory pairedAccessory = (PairedAccessory) other;
        return Intrinsics.areEqual(this.id, pairedAccessory.id) && Intrinsics.areEqual(this.publicKey, pairedAccessory.publicKey);
    }

    public int hashCode() {
        return (this.id.hashCode() * 31) + Arrays.hashCode(this.publicKey);
    }

    public String toString() {
        return "PairedAccessory(id=" + this.id + ", publicKey=" + Arrays.toString(this.publicKey) + ")";
    }

    public PairedAccessory(String id, byte[] publicKey) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(publicKey, "publicKey");
        this.id = id;
        this.publicKey = publicKey;
    }

    public final String getId() {
        return this.id;
    }

    public final byte[] getPublicKey() {
        return this.publicKey;
    }
}
