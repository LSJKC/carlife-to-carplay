package com.carplay.sender;

import com.carplay.airplay.AirPlayCrypto;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import org.bouncycastle.pqc.jcajce.spec.McElieceCCA2KeyGenParameterSpec;

/* JADX INFO: compiled from: MfiController.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\"\u0010\u0004\u001a\u00020\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\nJ\"\u0010\u0004\u001a\u00020\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\u000b¨\u0006\f"}, d2 = {"Lcom/carplay/sender/MfiController;", "", "<init>", "()V", "authenticate", "Lcom/carplay/sender/LegacySecurity;", "exchange", "Lkotlin/Function1;", "", "trust", "Lcom/carplay/sender/AccessoryTrust;", "Lcom/carplay/sender/AccessoryAuthentication;", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MfiController {
    public static final MfiController INSTANCE = new MfiController();

    private MfiController() {
    }

    public final LegacySecurity authenticate(Function1<? super byte[], byte[]> exchange, AccessoryTrust trust) {
        Intrinsics.checkNotNullParameter(exchange, "exchange");
        Intrinsics.checkNotNullParameter(trust, "trust");
        return authenticate(exchange, (AccessoryAuthentication) trust);
    }

    public final LegacySecurity authenticate(Function1<? super byte[], byte[]> exchange, AccessoryAuthentication trust) throws BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        boolean z;
        Intrinsics.checkNotNullParameter(exchange, "exchange");
        Intrinsics.checkNotNullParameter(trust, "trust");
        AirPlayCrypto.X25519KeyPair client = AirPlayCrypto.INSTANCE.x25519Generate();
        byte[] response = exchange.invoke(ArraysKt.plus(new byte[]{1}, client.getPublicKey()));
        int length = response.length;
        if (!(40 <= length && length < 100001)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        ByteBuffer data = ByteBuffer.wrap(response);
        byte[] server = new byte[32];
        data.get(server);
        X509Certificate certificate = trust.validate(authenticate$field(data, 65000));
        byte[] sealed = authenticate$field(data, 1024);
        if (data.hasRemaining()) {
            throw new IllegalArgumentException("不支持的扩展 MFiSAP 认证格式".toString());
        }
        byte[] shared = AirPlayCrypto.INSTANCE.x25519Shared(client.getPrivateKey(), server);
        int length2 = shared.length;
        int i = 0;
        while (true) {
            if (i >= length2) {
                z = false;
                break;
            }
            byte element$iv = shared[i];
            byte it = element$iv != 0 ? (byte) 1 : (byte) 0;
            if (it != 0) {
                z = true;
                break;
            }
            i++;
        }
        if (!z) {
            throw new IllegalStateException("Check failed.");
        }
        byte[] aesKey = authenticate$hash(shared, "AES-KEY");
        byte[] aesIv = authenticate$hash(shared, "AES-IV");
        Cipher $this$authenticate_u24lambda_u243 = Cipher.getInstance("AES/CTR/NoPadding");
        boolean z2 = false;
        $this$authenticate_u24lambda_u243.init(2, new SecretKeySpec(aesKey, "AES"), new IvParameterSpec(aesIv));
        byte[] signature = $this$authenticate_u24lambda_u243.doFinal(sealed);
        List algorithms = certificate.getPublicKey() instanceof ECPublicKey ? CollectionsKt.listOf("SHA-256") : CollectionsKt.listOf((Object[]) new String[]{McElieceCCA2KeyGenParameterSpec.SHA1, "SHA-256"});
        List $this$any$iv = algorithms;
        if (!($this$any$iv instanceof Collection) || !$this$any$iv.isEmpty()) {
            for (Object element$iv2 : $this$any$iv) {
                String algorithm = (String) element$iv2;
                AirPlayCrypto.X25519KeyPair client2 = client;
                byte[] digest = MessageDigest.getInstance(algorithm).digest(ArraysKt.plus(server, client2.getPublicKey()));
                Intrinsics.checkNotNull(digest);
                Intrinsics.checkNotNull(signature);
                if (trust.verifyAuthentication(certificate, digest, signature)) {
                    z2 = true;
                    break;
                }
                client = client2;
            }
        }
        if (z2) {
            return new LegacySecurity(shared, aesKey, aesIv);
        }
        throw new IllegalStateException("车机 MFiSAP 签名无效".toString());
    }

    private static final byte[] authenticate$field(ByteBuffer data, int maximum) {
        if (!(data.remaining() >= 4)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        int size = data.getInt();
        if (!((1 <= size && size <= maximum) && data.remaining() >= size)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        byte[] p0 = new byte[size];
        data.get(p0);
        return p0;
    }

    private static final byte[] authenticate$hash(byte[] shared, String label) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(McElieceCCA2KeyGenParameterSpec.SHA1);
        byte[] bytes = label.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        byte[] bArrDigest = messageDigest.digest(ArraysKt.plus(bytes, shared));
        Intrinsics.checkNotNullExpressionValue(bArrDigest, "digest(...)");
        byte[] bArrCopyOf = Arrays.copyOf(bArrDigest, 16);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        return bArrCopyOf;
    }
}
