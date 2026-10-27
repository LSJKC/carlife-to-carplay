package com.carplay.sender;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PhonePairing.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0006HÆ\u0003J'\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0006HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u0018"}, d2 = {"Lcom/carplay/sender/ControllerIdentity;", "", "privateKey", "", "publicKey", "id", "", "<init>", "([B[BLjava/lang/String;)V", "getPrivateKey", "()[B", "getPublicKey", "getId", "()Ljava/lang/String;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class ControllerIdentity {
    private final String id;
    private final byte[] privateKey;
    private final byte[] publicKey;

    public static /* synthetic */ ControllerIdentity copy$default(ControllerIdentity controllerIdentity, byte[] bArr, byte[] bArr2, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            bArr = controllerIdentity.privateKey;
        }
        if ((i & 2) != 0) {
            bArr2 = controllerIdentity.publicKey;
        }
        if ((i & 4) != 0) {
            str = controllerIdentity.id;
        }
        return controllerIdentity.copy(bArr, bArr2, str);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final byte[] getPrivateKey() {
        return this.privateKey;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final byte[] getPublicKey() {
        return this.publicKey;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getId() {
        return this.id;
    }

    public final ControllerIdentity copy(byte[] privateKey, byte[] publicKey, String id) {
        Intrinsics.checkNotNullParameter(privateKey, "privateKey");
        Intrinsics.checkNotNullParameter(publicKey, "publicKey");
        Intrinsics.checkNotNullParameter(id, "id");
        return new ControllerIdentity(privateKey, publicKey, id);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ControllerIdentity)) {
            return false;
        }
        ControllerIdentity controllerIdentity = (ControllerIdentity) other;
        return Intrinsics.areEqual(this.privateKey, controllerIdentity.privateKey) && Intrinsics.areEqual(this.publicKey, controllerIdentity.publicKey) && Intrinsics.areEqual(this.id, controllerIdentity.id);
    }

    public int hashCode() {
        return (((Arrays.hashCode(this.privateKey) * 31) + Arrays.hashCode(this.publicKey)) * 31) + this.id.hashCode();
    }

    public String toString() {
        return "ControllerIdentity(privateKey=" + Arrays.toString(this.privateKey) + ", publicKey=" + Arrays.toString(this.publicKey) + ", id=" + this.id + ")";
    }

    public ControllerIdentity(byte[] privateKey, byte[] publicKey, String id) {
        Intrinsics.checkNotNullParameter(privateKey, "privateKey");
        Intrinsics.checkNotNullParameter(publicKey, "publicKey");
        Intrinsics.checkNotNullParameter(id, "id");
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.id = id;
    }

    public final String getId() {
        return this.id;
    }

    public final byte[] getPrivateKey() {
        return this.privateKey;
    }

    public final byte[] getPublicKey() {
        return this.publicKey;
    }
}
