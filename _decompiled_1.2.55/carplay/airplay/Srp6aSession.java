package com.carplay.airplay;

import java.math.BigInteger;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Srp6a.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B1\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\u0005¢\u0006\u0004\b\t\u0010\nJ\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u0003R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\r\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\f¨\u0006\u0013"}, d2 = {"Lcom/carplay/airplay/Srp6aSession;", "", "salt", "", "publicKeyB", "Ljava/math/BigInteger;", "identifier", "verifier", "privateExponent", "<init>", "([BLjava/math/BigInteger;[BLjava/math/BigInteger;Ljava/math/BigInteger;)V", "getSalt", "()[B", "publicKey", "getPublicKey", "verify", "Lcom/carplay/airplay/Srp6aResult;", "publicKeyA", "clientProof", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Srp6aSession {
    private final byte[] identifier;
    private final BigInteger privateExponent;
    private final BigInteger publicKeyB;
    private final byte[] salt;
    private final BigInteger verifier;

    public Srp6aSession(byte[] salt, BigInteger publicKeyB, byte[] identifier, BigInteger verifier, BigInteger privateExponent) {
        Intrinsics.checkNotNullParameter(salt, "salt");
        Intrinsics.checkNotNullParameter(publicKeyB, "publicKeyB");
        Intrinsics.checkNotNullParameter(identifier, "identifier");
        Intrinsics.checkNotNullParameter(verifier, "verifier");
        Intrinsics.checkNotNullParameter(privateExponent, "privateExponent");
        this.salt = salt;
        this.publicKeyB = publicKeyB;
        this.identifier = identifier;
        this.verifier = verifier;
        this.privateExponent = privateExponent;
    }

    public final byte[] getSalt() {
        return this.salt;
    }

    public final byte[] getPublicKey() {
        return Srp6a.INSTANCE.pad$core_airplay(this.publicKeyB);
    }

    public final Srp6aResult verify(byte[] publicKeyA, byte[] clientProof) {
        Intrinsics.checkNotNullParameter(publicKeyA, "publicKeyA");
        Intrinsics.checkNotNullParameter(clientProof, "clientProof");
        BigInteger a = Srp6a.INSTANCE.toBigInt$core_airplay(publicKeyA);
        BigInteger n = Srp6a.INSTANCE.modulus$core_airplay();
        if (a.mod(n).signum() == 0) {
            return new Srp6aResult(false, null, null, 6, null);
        }
        BigInteger u = Srp6a.INSTANCE.toBigInt$core_airplay(AirPlayCrypto.INSTANCE.sha512(Srp6a.INSTANCE.pad$core_airplay(a), Srp6a.INSTANCE.pad$core_airplay(this.publicKeyB)));
        BigInteger bigIntegerModPow = this.verifier.modPow(u, n);
        Intrinsics.checkNotNullExpressionValue(bigIntegerModPow, "modPow(...)");
        BigInteger bigIntegerMultiply = a.multiply(bigIntegerModPow);
        Intrinsics.checkNotNullExpressionValue(bigIntegerMultiply, "multiply(...)");
        BigInteger s = bigIntegerMultiply.mod(n).modPow(this.privateExponent, n);
        AirPlayCrypto airPlayCrypto = AirPlayCrypto.INSTANCE;
        Srp6a srp6a = Srp6a.INSTANCE;
        Intrinsics.checkNotNull(s);
        byte[] sessionKey = airPlayCrypto.sha512(srp6a.toBytes$core_airplay(s));
        byte[] hashN = AirPlayCrypto.INSTANCE.sha512(Srp6a.INSTANCE.toBytes$core_airplay(n));
        byte[] hashG = AirPlayCrypto.INSTANCE.sha512(Srp6a.INSTANCE.toBytes$core_airplay(Srp6a.INSTANCE.generator$core_airplay()));
        byte[] hashXor = new byte[hashN.length];
        int length = hashN.length;
        for (int index = 0; index < length; index++) {
            hashXor[index] = (byte) (hashN[index] ^ hashG[index]);
        }
        byte[] expected = AirPlayCrypto.INSTANCE.sha512(hashXor, AirPlayCrypto.INSTANCE.sha512(this.identifier), this.salt, Srp6a.INSTANCE.pad$core_airplay(a), Srp6a.INSTANCE.pad$core_airplay(this.publicKeyB), sessionKey);
        if (!Arrays.equals(expected, clientProof)) {
            return new Srp6aResult(false, null, null, 6, null);
        }
        byte[] serverProof = AirPlayCrypto.INSTANCE.sha512(Srp6a.INSTANCE.pad$core_airplay(a), clientProof, sessionKey);
        return new Srp6aResult(true, sessionKey, serverProof);
    }
}
