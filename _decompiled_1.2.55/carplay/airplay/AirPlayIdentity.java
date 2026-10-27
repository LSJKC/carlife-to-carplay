package com.carplay.airplay;

import java.util.Arrays;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AirPlayIdentity.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\b\u0018\u0000 \u001a2\u00020\u0001:\u0001\u001aB\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0006HÆ\u0003J'\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001J\t\u0010\u0019\u001a\u00020\u0006HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\r¨\u0006\u001b"}, d2 = {"Lcom/carplay/airplay/AirPlayIdentity;", "", "privateKey", "", "publicKey", "pairingId", "", "<init>", "([B[BLjava/lang/String;)V", "getPrivateKey", "()[B", "getPublicKey", "getPairingId", "()Ljava/lang/String;", "publicKeyHex", "getPublicKeyHex", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class AirPlayIdentity {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final String pairingId;
    private final byte[] privateKey;
    private final byte[] publicKey;

    public static /* synthetic */ AirPlayIdentity copy$default(AirPlayIdentity airPlayIdentity, byte[] bArr, byte[] bArr2, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            bArr = airPlayIdentity.privateKey;
        }
        if ((i & 2) != 0) {
            bArr2 = airPlayIdentity.publicKey;
        }
        if ((i & 4) != 0) {
            str = airPlayIdentity.pairingId;
        }
        return airPlayIdentity.copy(bArr, bArr2, str);
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
    public final String getPairingId() {
        return this.pairingId;
    }

    public final AirPlayIdentity copy(byte[] privateKey, byte[] publicKey, String pairingId) {
        Intrinsics.checkNotNullParameter(privateKey, "privateKey");
        Intrinsics.checkNotNullParameter(publicKey, "publicKey");
        Intrinsics.checkNotNullParameter(pairingId, "pairingId");
        return new AirPlayIdentity(privateKey, publicKey, pairingId);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AirPlayIdentity)) {
            return false;
        }
        AirPlayIdentity airPlayIdentity = (AirPlayIdentity) other;
        return Intrinsics.areEqual(this.privateKey, airPlayIdentity.privateKey) && Intrinsics.areEqual(this.publicKey, airPlayIdentity.publicKey) && Intrinsics.areEqual(this.pairingId, airPlayIdentity.pairingId);
    }

    public int hashCode() {
        return (((Arrays.hashCode(this.privateKey) * 31) + Arrays.hashCode(this.publicKey)) * 31) + this.pairingId.hashCode();
    }

    public String toString() {
        return "AirPlayIdentity(privateKey=" + Arrays.toString(this.privateKey) + ", publicKey=" + Arrays.toString(this.publicKey) + ", pairingId=" + this.pairingId + ")";
    }

    public AirPlayIdentity(byte[] privateKey, byte[] publicKey, String pairingId) {
        Intrinsics.checkNotNullParameter(privateKey, "privateKey");
        Intrinsics.checkNotNullParameter(publicKey, "publicKey");
        Intrinsics.checkNotNullParameter(pairingId, "pairingId");
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.pairingId = pairingId;
    }

    public final byte[] getPrivateKey() {
        return this.privateKey;
    }

    public final byte[] getPublicKey() {
        return this.publicKey;
    }

    public final String getPairingId() {
        return this.pairingId;
    }

    public final String getPublicKeyHex() {
        return BytesKt.toHexString(this.publicKey);
    }

    /* JADX INFO: compiled from: AirPlayIdentity.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0006\u0010\u0004\u001a\u00020\u0005¨\u0006\u0006"}, d2 = {"Lcom/carplay/airplay/AirPlayIdentity$Companion;", "", "<init>", "()V", "generate", "Lcom/carplay/airplay/AirPlayIdentity;", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final AirPlayIdentity generate() {
            AirPlayCrypto.Ed25519KeyPair keyPair = AirPlayCrypto.INSTANCE.ed25519Generate();
            byte[] privateKey = keyPair.getPrivateKey();
            byte[] publicKey = keyPair.getPublicKey();
            String string = UUID.randomUUID().toString();
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            return new AirPlayIdentity(privateKey, publicKey, string);
        }
    }
}
