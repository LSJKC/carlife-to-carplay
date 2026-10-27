package com.carplay.sender;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: MfiController.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003¢\u0006\u0004\b\u0006\u0010\u0007J\u0006\u0010\f\u001a\u00020\rJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J'\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\t¨\u0006\u0019"}, d2 = {"Lcom/carplay/sender/LegacySecurity;", "", "shared", "", "aesKey", "aesIv", "<init>", "([B[B[B)V", "getShared", "()[B", "getAesKey", "getAesIv", "videoCipher", "Ljavax/crypto/Cipher;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class LegacySecurity {
    private final byte[] aesIv;
    private final byte[] aesKey;
    private final byte[] shared;

    public static /* synthetic */ LegacySecurity copy$default(LegacySecurity legacySecurity, byte[] bArr, byte[] bArr2, byte[] bArr3, int i, Object obj) {
        if ((i & 1) != 0) {
            bArr = legacySecurity.shared;
        }
        if ((i & 2) != 0) {
            bArr2 = legacySecurity.aesKey;
        }
        if ((i & 4) != 0) {
            bArr3 = legacySecurity.aesIv;
        }
        return legacySecurity.copy(bArr, bArr2, bArr3);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final byte[] getShared() {
        return this.shared;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final byte[] getAesKey() {
        return this.aesKey;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final byte[] getAesIv() {
        return this.aesIv;
    }

    public final LegacySecurity copy(byte[] shared, byte[] aesKey, byte[] aesIv) {
        Intrinsics.checkNotNullParameter(shared, "shared");
        Intrinsics.checkNotNullParameter(aesKey, "aesKey");
        Intrinsics.checkNotNullParameter(aesIv, "aesIv");
        return new LegacySecurity(shared, aesKey, aesIv);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof LegacySecurity)) {
            return false;
        }
        LegacySecurity legacySecurity = (LegacySecurity) other;
        return Intrinsics.areEqual(this.shared, legacySecurity.shared) && Intrinsics.areEqual(this.aesKey, legacySecurity.aesKey) && Intrinsics.areEqual(this.aesIv, legacySecurity.aesIv);
    }

    public int hashCode() {
        return (((Arrays.hashCode(this.shared) * 31) + Arrays.hashCode(this.aesKey)) * 31) + Arrays.hashCode(this.aesIv);
    }

    public String toString() {
        return "LegacySecurity(shared=" + Arrays.toString(this.shared) + ", aesKey=" + Arrays.toString(this.aesKey) + ", aesIv=" + Arrays.toString(this.aesIv) + ")";
    }

    public LegacySecurity(byte[] shared, byte[] aesKey, byte[] aesIv) {
        Intrinsics.checkNotNullParameter(shared, "shared");
        Intrinsics.checkNotNullParameter(aesKey, "aesKey");
        Intrinsics.checkNotNullParameter(aesIv, "aesIv");
        this.shared = shared;
        this.aesKey = aesKey;
        this.aesIv = aesIv;
    }

    public final byte[] getAesIv() {
        return this.aesIv;
    }

    public final byte[] getAesKey() {
        return this.aesKey;
    }

    public final byte[] getShared() {
        return this.shared;
    }

    public final Cipher videoCipher() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher $this$videoCipher_u24lambda_u240 = Cipher.getInstance("AES/CTR/NoPadding");
        $this$videoCipher_u24lambda_u240.init(1, new SecretKeySpec(this.aesKey, "AES"), new IvParameterSpec(this.aesIv));
        Intrinsics.checkNotNullExpressionValue($this$videoCipher_u24lambda_u240, "apply(...)");
        return $this$videoCipher_u24lambda_u240;
    }
}
