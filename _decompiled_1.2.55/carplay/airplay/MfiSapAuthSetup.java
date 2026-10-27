package com.carplay.airplay;

import com.carplay.mfi.BaaCertificatePair;
import com.carplay.mfi.MfiAuthenticator;
import com.carplay.mfi.MfiCertificateType;
import com.carplay.mfi.MfiInvalidDataException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import org.bouncycastle.pqc.jcajce.spec.McElieceCCA2KeyGenParameterSpec;

/* JADX INFO: compiled from: MfiSapAuthSetup.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000bJ \u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\bH\u0002J(\u0010\u0010\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\bH\u0002J\u0010\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\bH\u0002J!\u0010\u0015\u001a\u00020\b2\u0012\u0010\u0016\u001a\n\u0012\u0006\b\u0001\u0012\u00020\b0\u0017\"\u00020\bH\u0002¢\u0006\u0002\u0010\u0018J!\u0010\u0019\u001a\u00020\b2\u0012\u0010\u0016\u001a\n\u0012\u0006\b\u0001\u0012\u00020\b0\u0017\"\u00020\bH\u0002¢\u0006\u0002\u0010\u0018J%\u0010\u001a\u001a\u00020\b2\u0006\u0010\u001b\u001a\u00020\u001c2\u000e\u0010\u0016\u001a\n\u0012\u0006\b\u0001\u0012\u00020\b0\u0017H\u0002¢\u0006\u0002\u0010\u001dJ \u0010\u001e\u001a\u00020\b2\u0006\u0010\u001f\u001a\u00020\b2\u0006\u0010 \u001a\u00020\b2\u0006\u0010!\u001a\u00020\bH\u0002J \u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020\b2\u0006\u0010%\u001a\u00020\u00052\u0006\u0010&\u001a\u00020\u0005H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006'"}, d2 = {"Lcom/carplay/airplay/MfiSapAuthSetup;", "", "<init>", "()V", "VERSION", "", "PUBLIC_KEY_BYTES", "handle", "", "body", "mfi", "Lcom/carplay/mfi/MfiAuthenticator;", "buildMfiResponse", "publicKey", "certificate", "encryptedSignature", "buildBaaResponse", "leaf", "baicBlob", "encodeBaaIntermediate", "intermediate", "sha1", "parts", "", "([[B)[B", "sha256", "digest", "algorithm", "", "(Ljava/lang/String;[[B)[B", "aesCtr128", "key", "iv", "data", "putU32", "", "target", "offset", "value", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MfiSapAuthSetup {
    public static final MfiSapAuthSetup INSTANCE = new MfiSapAuthSetup();
    private static final int PUBLIC_KEY_BYTES = 32;
    private static final int VERSION = 1;

    /* JADX INFO: compiled from: MfiSapAuthSetup.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[MfiCertificateType.values().length];
            try {
                iArr[MfiCertificateType.MFI.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[MfiCertificateType.BAA.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    private MfiSapAuthSetup() {
    }

    public final byte[] handle(byte[] body, MfiAuthenticator mfi) throws BadPaddingException, MfiInvalidDataException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        boolean z;
        byte[] digest;
        Intrinsics.checkNotNullParameter(body, "body");
        Intrinsics.checkNotNullParameter(mfi, "mfi");
        if (body.length != 33 || body[0] != 1) {
            return null;
        }
        byte[] peerPublicKey = ArraysKt.copyOfRange(body, 1, body.length);
        AirPlayCrypto.X25519KeyPair pair = AirPlayCrypto.INSTANCE.x25519Generate();
        byte[] shared = AirPlayCrypto.INSTANCE.x25519Shared(pair.getPrivateKey(), peerPublicKey);
        int length = shared.length;
        int i = 0;
        while (true) {
            if (i < length) {
                byte element$iv = shared[i];
                byte it = element$iv == 0 ? (byte) 1 : (byte) 0;
                if (it == 0) {
                    z = false;
                    break;
                }
                i++;
            } else {
                z = true;
                break;
            }
        }
        if (z) {
            return null;
        }
        byte[] aesKey = Arrays.copyOf(sha1(BytesKt.asciiBytes("AES-KEY"), shared), 16);
        Intrinsics.checkNotNullExpressionValue(aesKey, "copyOf(...)");
        byte[] aesIv = Arrays.copyOf(sha1(BytesKt.asciiBytes("AES-IV"), shared), 16);
        Intrinsics.checkNotNullExpressionValue(aesIv, "copyOf(...)");
        switch (WhenMappings.$EnumSwitchMapping$0[mfi.getCertificateType().ordinal()]) {
            case 1:
                byte[] certificate = MfiAuthenticator.readCertificate$default(mfi, 0, 1, null);
                if (mfi.protocolMajor() == 2) {
                    digest = sha1(pair.getPublicKey(), peerPublicKey);
                } else {
                    digest = sha256(pair.getPublicKey(), peerPublicKey);
                }
                byte[] encryptedSignature = aesCtr128(aesKey, aesIv, mfi.signChallenge(digest));
                return buildMfiResponse(pair.getPublicKey(), certificate, encryptedSignature);
            case 2:
                BaaCertificatePair certificates = mfi.baaCertificates();
                byte[] encryptedSignature2 = aesCtr128(aesKey, aesIv, mfi.signChallenge(ArraysKt.plus(pair.getPublicKey(), peerPublicKey)));
                byte[] certificate2 = buildBaaResponse(pair.getPublicKey(), certificates.getLeaf(), encryptedSignature2, encodeBaaIntermediate(certificates.getIntermediate()));
                return certificate2;
            default:
                throw new NoWhenBranchMatchedException();
        }
    }

    private final byte[] buildMfiResponse(byte[] publicKey, byte[] certificate, byte[] encryptedSignature) {
        byte[] response = new byte[certificate.length + 36 + 4 + encryptedSignature.length];
        ArraysKt.copyInto$default(publicKey, response, 0, 0, 0, 12, (Object) null);
        putU32(response, 32, certificate.length);
        ArraysKt.copyInto$default(certificate, response, 36, 0, 0, 12, (Object) null);
        putU32(response, certificate.length + 36, encryptedSignature.length);
        ArraysKt.copyInto$default(encryptedSignature, response, certificate.length + 40, 0, 0, 12, (Object) null);
        return response;
    }

    private final byte[] buildBaaResponse(byte[] publicKey, byte[] leaf, byte[] encryptedSignature, byte[] baicBlob) {
        byte[] response = new byte[leaf.length + 36 + 4 + encryptedSignature.length + 4 + baicBlob.length];
        ArraysKt.copyInto$default(publicKey, response, 0, 0, 0, 12, (Object) null);
        putU32(response, 32, leaf.length);
        int offset = 32 + 4;
        ArraysKt.copyInto$default(leaf, response, offset, 0, 0, 12, (Object) null);
        int offset2 = offset + leaf.length;
        putU32(response, offset2, encryptedSignature.length);
        int offset3 = offset2 + 4;
        ArraysKt.copyInto$default(encryptedSignature, response, offset3, 0, 0, 12, (Object) null);
        int offset4 = offset3 + encryptedSignature.length;
        putU32(response, offset4, baicBlob.length);
        ArraysKt.copyInto$default(baicBlob, response, offset4 + 4, 0, 0, 12, (Object) null);
        return response;
    }

    private final byte[] encodeBaaIntermediate(byte[] intermediate) {
        byte[] header;
        if (intermediate.length <= 32) {
            header = new byte[]{-31, 68, 98, 97, 73, 67, (byte) (intermediate.length + 112)};
        } else if (intermediate.length <= 255) {
            header = new byte[]{-31, 68, 98, 97, 73, 67, -111, (byte) intermediate.length};
        } else {
            header = intermediate.length <= 65535 ? new byte[]{-31, 68, 98, 97, 73, 67, -110, (byte) intermediate.length, (byte) (intermediate.length >>> 8)} : new byte[]{-31, 68, 98, 97, 73, 67, -109, (byte) intermediate.length, (byte) (intermediate.length >>> 8), (byte) (intermediate.length >>> 16), (byte) (intermediate.length >>> 24)};
        }
        return ArraysKt.plus(header, intermediate);
    }

    private final byte[] sha1(byte[]... parts) {
        return digest(McElieceCCA2KeyGenParameterSpec.SHA1, parts);
    }

    private final byte[] sha256(byte[]... parts) {
        return digest("SHA-256", parts);
    }

    private final byte[] digest(String algorithm, byte[][] parts) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        for (byte[] part : parts) {
            digest.update(part);
        }
        byte[] bArrDigest = digest.digest();
        Intrinsics.checkNotNullExpressionValue(bArrDigest, "digest(...)");
        return bArrDigest;
    }

    private final byte[] aesCtr128(byte[] key, byte[] iv, byte[] data) throws BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(1, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        byte[] bArrDoFinal = cipher.doFinal(data);
        Intrinsics.checkNotNullExpressionValue(bArrDoFinal, "doFinal(...)");
        return bArrDoFinal;
    }

    private final void putU32(byte[] target, int offset, int value) {
        target[offset] = (byte) (value >>> 24);
        target[offset + 1] = (byte) (value >>> 16);
        target[offset + 2] = (byte) (value >>> 8);
        target[offset + 3] = (byte) value;
    }
}
