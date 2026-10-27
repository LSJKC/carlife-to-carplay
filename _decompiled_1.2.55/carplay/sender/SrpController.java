package com.carplay.sender;

import com.carplay.airplay.AirPlayCrypto;
import com.carplay.airplay.PairSetup;
import com.carplay.airplay.Srp6a;
import java.math.BigInteger;
import java.security.SecureRandom;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: PhonePairing.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000e\b\u0000\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\r\u001a\n \u000e*\u0004\u0018\u00010\n0\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u000f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0012\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u0011\u0010\u0014\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0011R\u0011\u0010\u0016\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0011¨\u0006\u0018"}, d2 = {"Lcom/carplay/sender/SrpController;", "", "salt", "", "serverPublic", "pin", "", "<init>", "([B[BLjava/lang/String;)V", "n", "Ljava/math/BigInteger;", "g", "a", "public", "kotlin.jvm.PlatformType", "publicKey", "getPublicKey", "()[B", "key", "getKey", "proof", "getProof", "serverProof", "getServerProof", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class SrpController {
    private final BigInteger a;
    private final BigInteger g;
    private final byte[] key;
    private final BigInteger n;
    private final byte[] proof;
    private final BigInteger public;
    private final byte[] publicKey;
    private final byte[] serverProof;

    public SrpController(byte[] salt, byte[] serverPublic, String pin) {
        Intrinsics.checkNotNullParameter(salt, "salt");
        Intrinsics.checkNotNullParameter(serverPublic, "serverPublic");
        Intrinsics.checkNotNullParameter(pin, "pin");
        this.n = Srp6a.INSTANCE.modulus$core_airplay();
        this.g = Srp6a.INSTANCE.generator$core_airplay();
        byte[] p0 = new byte[32];
        new SecureRandom().nextBytes(p0);
        Unit unit = Unit.INSTANCE;
        this.a = new BigInteger(1, p0);
        this.public = this.g.modPow(this.a, this.n);
        Srp6a srp6a = Srp6a.INSTANCE;
        BigInteger bigInteger = this.public;
        Intrinsics.checkNotNullExpressionValue(bigInteger, "public");
        this.publicKey = srp6a.pad$core_airplay(bigInteger);
        BigInteger b = new BigInteger(1, serverPublic);
        if (Intrinsics.areEqual(b.mod(this.n), BigInteger.ZERO)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        BigInteger u = new BigInteger(1, AirPlayCrypto.INSTANCE.sha512(this.publicKey, Srp6a.INSTANCE.pad$core_airplay(b)));
        if (Intrinsics.areEqual(u, BigInteger.ZERO)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        AirPlayCrypto airPlayCrypto = AirPlayCrypto.INSTANCE;
        AirPlayCrypto airPlayCrypto2 = AirPlayCrypto.INSTANCE;
        byte[] bytes = ("Pair-Setup:" + pin).getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        BigInteger x = new BigInteger(1, airPlayCrypto.sha512(salt, airPlayCrypto2.sha512(bytes)));
        BigInteger k = new BigInteger(1, AirPlayCrypto.INSTANCE.sha512(Srp6a.INSTANCE.toBytes$core_airplay(this.n), Srp6a.INSTANCE.pad$core_airplay(this.g)));
        BigInteger bigIntegerModPow = this.g.modPow(x, this.n);
        Intrinsics.checkNotNullExpressionValue(bigIntegerModPow, "modPow(...)");
        BigInteger bigIntegerMultiply = k.multiply(bigIntegerModPow);
        Intrinsics.checkNotNullExpressionValue(bigIntegerMultiply, "multiply(...)");
        BigInteger bigIntegerSubtract = b.subtract(bigIntegerMultiply);
        Intrinsics.checkNotNullExpressionValue(bigIntegerSubtract, "subtract(...)");
        BigInteger bigIntegerMod = bigIntegerSubtract.mod(this.n);
        BigInteger bigInteger2 = this.a;
        BigInteger bigIntegerMultiply2 = u.multiply(x);
        Intrinsics.checkNotNullExpressionValue(bigIntegerMultiply2, "multiply(...)");
        BigInteger bigIntegerAdd = bigInteger2.add(bigIntegerMultiply2);
        Intrinsics.checkNotNullExpressionValue(bigIntegerAdd, "add(...)");
        BigInteger secret = bigIntegerMod.modPow(bigIntegerAdd, this.n);
        AirPlayCrypto airPlayCrypto3 = AirPlayCrypto.INSTANCE;
        Srp6a srp6a2 = Srp6a.INSTANCE;
        Intrinsics.checkNotNull(secret);
        this.key = airPlayCrypto3.sha512(srp6a2.toBytes$core_airplay(secret));
        byte[] hashN = AirPlayCrypto.INSTANCE.sha512(Srp6a.INSTANCE.toBytes$core_airplay(this.n));
        byte[] hashG = AirPlayCrypto.INSTANCE.sha512(Srp6a.INSTANCE.toBytes$core_airplay(this.g));
        int length = hashN.length;
        byte[] xor = new byte[length];
        for (int i = 0; i < length; i++) {
            xor[i] = (byte) (hashN[i] ^ hashG[i]);
        }
        AirPlayCrypto airPlayCrypto4 = AirPlayCrypto.INSTANCE;
        AirPlayCrypto airPlayCrypto5 = AirPlayCrypto.INSTANCE;
        byte[] bytes2 = PairSetup.SETUP_USERNAME.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes2, "getBytes(...)");
        this.proof = airPlayCrypto4.sha512(xor, airPlayCrypto5.sha512(bytes2), salt, this.publicKey, Srp6a.INSTANCE.pad$core_airplay(b), this.key);
        this.serverProof = AirPlayCrypto.INSTANCE.sha512(this.publicKey, this.proof, this.key);
    }

    public final byte[] getPublicKey() {
        return this.publicKey;
    }

    public final byte[] getKey() {
        return this.key;
    }

    public final byte[] getProof() {
        return this.proof;
    }

    public final byte[] getServerProof() {
        return this.serverProof;
    }
}
