package com.carplay.airplay;

import java.math.BigInteger;
import java.security.SecureRandom;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: Srp6a.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u000f\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011J\u0015\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\nH\u0000¢\u0006\u0002\b\u0016J\u0015\u0010\u0017\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u0014H\u0000¢\u0006\u0002\b\u0019J\u0015\u0010\u001a\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\nH\u0000¢\u0006\u0002\b\u001bJ\r\u0010\u001c\u001a\u00020\nH\u0000¢\u0006\u0002\b\u001dJ\r\u0010\u001e\u001a\u00020\nH\u0000¢\u0006\u0002\b\u001fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u000b\u001a\n \f*\u0004\u0018\u00010\n0\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0011X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006#"}, d2 = {"Lcom/carplay/airplay/Srp6a;", "", "<init>", "()V", "SALT_BYTES", "", "PRIVATE_BYTES", "random", "Ljava/security/SecureRandom;", "n", "Ljava/math/BigInteger;", "g", "kotlin.jvm.PlatformType", "multiplier", "start", "Lcom/carplay/airplay/Srp6aSession;", "username", "", "password", "toBytes", "", "value", "toBytes$core_airplay", "toBigInt", "bytes", "toBigInt$core_airplay", "pad", "pad$core_airplay", "modulus", "modulus$core_airplay", "generator", "generator$core_airplay", "N_HEX", "N_BYTES", "COLON", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Srp6a {
    private static final byte[] COLON;
    private static final int N_BYTES = 384;
    private static final int PRIVATE_BYTES = 32;
    private static final int SALT_BYTES = 16;
    private static final BigInteger multiplier;
    public static final Srp6a INSTANCE = new Srp6a();
    private static final SecureRandom random = new SecureRandom();
    private static final String N_HEX = "FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3DC2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F83655D23DCA3AD961C62F356208552BB9ED529077096966D670C354E4ABC9804F1746C08CA18217C32905E462E36CE3BE39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9DE2BCBF6955817183995497CEA956AE515D2261898FA051015728E5A8AAAC42DAD33170D04507A33A85521ABDF1CBA64ECFB850458DBEF0A8AEA71575D060C7DB3970F85A6E1E4C7ABF5AE8CDB0933D71E8C94E04A25619DCEE3D2261AD2EE6BF12FFA06D98A0864D87602733EC86A64521F2B18177B200CBBE117577A615D6C770988C0BAD946E208E24FA074E5AB3143DB5BFCE0FD108E4B82D120A93AD2CAFFFFFFFFFFFFFFFF";
    private static final BigInteger n = new BigInteger(N_HEX, 16);
    private static final BigInteger g = BigInteger.valueOf(5);

    private Srp6a() {
    }

    static {
        Srp6a srp6a = INSTANCE;
        AirPlayCrypto airPlayCrypto = AirPlayCrypto.INSTANCE;
        byte[] bytes$core_airplay = INSTANCE.toBytes$core_airplay(n);
        Srp6a srp6a2 = INSTANCE;
        BigInteger g2 = g;
        Intrinsics.checkNotNullExpressionValue(g2, "g");
        multiplier = srp6a.toBigInt$core_airplay(airPlayCrypto.sha512(bytes$core_airplay, srp6a2.pad$core_airplay(g2)));
        COLON = new byte[]{58};
    }

    public final Srp6aSession start(String username, String password) {
        Intrinsics.checkNotNullParameter(username, "username");
        Intrinsics.checkNotNullParameter(password, "password");
        byte[] p0 = new byte[16];
        random.nextBytes(p0);
        byte[] identifier = username.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(identifier, "getBytes(...)");
        byte[] pass = password.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(pass, "getBytes(...)");
        BigInteger x = toBigInt$core_airplay(AirPlayCrypto.INSTANCE.sha512(p0, AirPlayCrypto.INSTANCE.sha512(identifier, COLON, pass)));
        BigInteger verifier = g.modPow(x, n);
        byte[] p1 = new byte[32];
        random.nextBytes(p1);
        BigInteger privateExponent = toBigInt$core_airplay(p1);
        BigInteger bigInteger = multiplier;
        Intrinsics.checkNotNull(verifier);
        BigInteger bigIntegerMultiply = bigInteger.multiply(verifier);
        Intrinsics.checkNotNullExpressionValue(bigIntegerMultiply, "multiply(...)");
        BigInteger bigIntegerModPow = g.modPow(privateExponent, n);
        Intrinsics.checkNotNullExpressionValue(bigIntegerModPow, "modPow(...)");
        BigInteger bigIntegerAdd = bigIntegerMultiply.add(bigIntegerModPow);
        Intrinsics.checkNotNullExpressionValue(bigIntegerAdd, "add(...)");
        BigInteger publicKeyB = bigIntegerAdd.mod(n);
        Intrinsics.checkNotNull(publicKeyB);
        return new Srp6aSession(p0, publicKeyB, identifier, verifier, privateExponent);
    }

    public final byte[] toBytes$core_airplay(BigInteger value) {
        Intrinsics.checkNotNullParameter(value, "value");
        if (value.signum() == 0) {
            return new byte[]{0};
        }
        byte[] encoded = value.toByteArray();
        if (encoded.length <= 1 || encoded[0] != 0) {
            Intrinsics.checkNotNull(encoded);
            return encoded;
        }
        Intrinsics.checkNotNull(encoded);
        return ArraysKt.copyOfRange(encoded, 1, encoded.length);
    }

    public final BigInteger toBigInt$core_airplay(byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return new BigInteger(1, bytes);
    }

    public final byte[] pad$core_airplay(BigInteger value) {
        Intrinsics.checkNotNullParameter(value, "value");
        byte[] bytes = toBytes$core_airplay(value);
        if (bytes.length >= 384) {
            return bytes;
        }
        byte[] it = new byte[384];
        ArraysKt.copyInto$default(bytes, it, 384 - bytes.length, 0, 0, 12, (Object) null);
        return it;
    }

    public final BigInteger modulus$core_airplay() {
        return n;
    }

    public final BigInteger generator$core_airplay() {
        BigInteger g2 = g;
        Intrinsics.checkNotNullExpressionValue(g2, "g");
        return g2;
    }
}
