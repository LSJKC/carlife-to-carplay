package com.carplay.airplay;

import android.os.Build;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.modes.ChaCha20Poly1305;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.params.HKDFParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.X25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.X25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.jcajce.util.AnnotatedPrivateKey;

/* JADX INFO: compiled from: AirPlayCrypto.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000i\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0011\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005*\u0001\u000f\bÆ\u0002\u0018\u00002\u00020\u0001:\u0003GHIB\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0006\u0010\u0014\u001a\u00020\u0015J\u0016\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u0017J\u0006\u0010\u001a\u001a\u00020\u001bJ\u0016\u0010\u001c\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u0017J\u001e\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u00172\u0006\u0010!\u001a\u00020\u0017J(\u0010\"\u001a\u00020\u00172\u0006\u0010#\u001a\u00020\u00172\u0006\u0010$\u001a\u00020\u00172\u0006\u0010%\u001a\u00020\u00172\b\b\u0002\u0010&\u001a\u00020\u0005J\u001f\u0010'\u001a\u00020\u00172\u0012\u0010(\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00170)\"\u00020\u0017¢\u0006\u0002\u0010*J(\u0010+\u001a\u00020\u00172\u0006\u0010,\u001a\u00020\u00172\u0006\u0010-\u001a\u00020\u00172\u0006\u0010.\u001a\u00020\u00172\b\b\u0002\u0010/\u001a\u00020\u0017J(\u00100\u001a\u00020\u00172\u0006\u0010,\u001a\u00020\u00172\u0006\u0010-\u001a\u00020\u00172\u0006\u00101\u001a\u00020\u00172\b\b\u0002\u0010/\u001a\u00020\u0017JA\u00102\u001a\u0004\u0018\u00010\u00172\u0006\u00103\u001a\u00020\u00052\u0006\u0010,\u001a\u00020\u00172\u0006\u0010-\u001a\u00020\u00172\u0006\u00104\u001a\u00020\u00172\u0006\u0010/\u001a\u00020\u00172\b\b\u0002\u00105\u001a\u00020\u001fH\u0000¢\u0006\u0002\b6J\u0018\u00107\u001a\u0002082\u0006\u00109\u001a\u0002082\u0006\u0010:\u001a\u00020\u0005H\u0002J/\u0010;\u001a\u00020\u00172\u0006\u0010,\u001a\u00020\u00172\u0006\u0010-\u001a\u00020\u00172\u0006\u0010.\u001a\u00020\u00172\b\b\u0002\u0010/\u001a\u00020\u0017H\u0000¢\u0006\u0002\b<J/\u0010=\u001a\u00020\u00172\u0006\u0010,\u001a\u00020\u00172\u0006\u0010-\u001a\u00020\u00172\u0006\u00101\u001a\u00020\u00172\b\b\u0002\u0010/\u001a\u00020\u0017H\u0000¢\u0006\u0002\b>J\u000e\u0010?\u001a\u00020\u00172\u0006\u0010@\u001a\u00020AJ\u000e\u0010B\u001a\u00020\u00172\u0006\u0010C\u001a\u00020\rJ%\u0010D\u001a\u00020\u00172\u0006\u0010D\u001a\u00020E2\u000e\u0010(\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00170)H\u0002¢\u0006\u0002\u0010FR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0010R\u0011\u0010\u0011\u001a\u00020\r8F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013¨\u0006J"}, d2 = {"Lcom/carplay/airplay/AirPlayCrypto;", "", "<init>", "()V", "MAC_BITS", "", "NONCE_SIZE", "LABEL_SIZE", "MAX_RETAINED_BUFFER", "random", "Ljava/security/SecureRandom;", "PLATFORM_CHACHA_NAMES", "", "", "platformState", "com/carplay/airplay/AirPlayCrypto$platformState$1", "Lcom/carplay/airplay/AirPlayCrypto$platformState$1;", "chachaImplementation", "getChachaImplementation", "()Ljava/lang/String;", "x25519Generate", "Lcom/carplay/airplay/AirPlayCrypto$X25519KeyPair;", "x25519Shared", "", "privateKeyRaw", "peerPublicKeyRaw", "ed25519Generate", "Lcom/carplay/airplay/AirPlayCrypto$Ed25519KeyPair;", "ed25519Sign", "data", "ed25519Verify", "", "publicKeyRaw", "signature", "hkdfSha512", "inputKeyMaterial", "salt", "info", "length", "sha512", "parts", "", "([[B)[B", "chachaSeal", "key", "nonce", "plaintext", "aad", "chachaOpen", "ciphertextAndTag", "platformChacha", "mode", "input", "useDirectBuffers", "platformChacha$core_airplay", "sized", "Ljava/nio/ByteBuffer;", "buffer", "size", "chachaSealBouncyCastle", "chachaSealBouncyCastle$core_airplay", "chachaOpenBouncyCastle", "chachaOpenBouncyCastle$core_airplay", "nonce64", "counter", "", "nonceLabel", AnnotatedPrivateKey.LABEL, "digest", "Lorg/bouncycastle/crypto/Digest;", "(Lorg/bouncycastle/crypto/Digest;[[B)[B", "PlatformChacha", "X25519KeyPair", "Ed25519KeyPair", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AirPlayCrypto {
    private static final int LABEL_SIZE = 8;
    private static final int MAC_BITS = 128;
    private static final int MAX_RETAINED_BUFFER = 1048576;
    private static final int NONCE_SIZE = 12;
    public static final AirPlayCrypto INSTANCE = new AirPlayCrypto();
    private static final SecureRandom random = new SecureRandom();
    private static final List<String> PLATFORM_CHACHA_NAMES = CollectionsKt.listOf((Object[]) new String[]{"ChaCha20/Poly1305/NoPadding", "ChaCha20-Poly1305"});
    private static final AirPlayCrypto$platformState$1 platformState = new ThreadLocal<PlatformChacha>() { // from class: com.carplay.airplay.AirPlayCrypto$platformState$1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        /* JADX WARN: Code duplicated, block: B:18:0x0044  */
        /* JADX WARN: Code duplicated, block: B:25:? A[RETURN, SYNTHETIC] */
        @Override // java.lang.ThreadLocal
        public AirPlayCrypto.PlatformChacha initialValue() {
            Cipher p0;
            Object objM159constructorimpl;
            for (String it : AirPlayCrypto.PLATFORM_CHACHA_NAMES) {
                try {
                    Result.Companion companion = Result.INSTANCE;
                    AirPlayCrypto$platformState$1 airPlayCrypto$platformState$1 = this;
                    objM159constructorimpl = Result.m159constructorimpl(Cipher.getInstance(it));
                } catch (Throwable th) {
                    Result.Companion companion2 = Result.INSTANCE;
                    objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
                }
                if (Result.m165isFailureimpl(objM159constructorimpl)) {
                    objM159constructorimpl = null;
                }
                p0 = (Cipher) objM159constructorimpl;
                if (p0 != null) {
                    if (p0 != null) {
                        return new AirPlayCrypto.PlatformChacha(p0);
                    }
                    return null;
                }
            }
            p0 = null;
            if (p0 != null) {
                return new AirPlayCrypto.PlatformChacha(p0);
            }
            return null;
        }
    };

    private AirPlayCrypto() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: AirPlayCrypto.kt */
    @Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u000b\"\u0004\b\u0010\u0010\rR\u001a\u0010\u0011\u001a\u00020\u0012X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016¨\u0006\u0017"}, d2 = {"Lcom/carplay/airplay/AirPlayCrypto$PlatformChacha;", "", "cipher", "Ljavax/crypto/Cipher;", "<init>", "(Ljavax/crypto/Cipher;)V", "getCipher", "()Ljavax/crypto/Cipher;", "input", "Ljava/nio/ByteBuffer;", "getInput", "()Ljava/nio/ByteBuffer;", "setInput", "(Ljava/nio/ByteBuffer;)V", "output", "getOutput", "setOutput", "implementation", "", "getImplementation", "()Ljava/lang/String;", "setImplementation", "(Ljava/lang/String;)V", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    static final class PlatformChacha {
        private final Cipher cipher;
        private String implementation;
        private ByteBuffer input;
        private ByteBuffer output;

        public PlatformChacha(Cipher cipher) {
            Intrinsics.checkNotNullParameter(cipher, "cipher");
            this.cipher = cipher;
            ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(0);
            Intrinsics.checkNotNullExpressionValue(byteBufferAllocateDirect, "allocateDirect(...)");
            this.input = byteBufferAllocateDirect;
            ByteBuffer byteBufferAllocateDirect2 = ByteBuffer.allocateDirect(0);
            Intrinsics.checkNotNullExpressionValue(byteBufferAllocateDirect2, "allocateDirect(...)");
            this.output = byteBufferAllocateDirect2;
            String name = this.cipher.getProvider().getName();
            Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
            this.implementation = name;
        }

        public final Cipher getCipher() {
            return this.cipher;
        }

        public final ByteBuffer getInput() {
            return this.input;
        }

        public final void setInput(ByteBuffer byteBuffer) {
            Intrinsics.checkNotNullParameter(byteBuffer, "<set-?>");
            this.input = byteBuffer;
        }

        public final ByteBuffer getOutput() {
            return this.output;
        }

        public final void setOutput(ByteBuffer byteBuffer) {
            Intrinsics.checkNotNullParameter(byteBuffer, "<set-?>");
            this.output = byteBuffer;
        }

        public final String getImplementation() {
            return this.implementation;
        }

        public final void setImplementation(String str) {
            Intrinsics.checkNotNullParameter(str, "<set-?>");
            this.implementation = str;
        }
    }

    public final String getChachaImplementation() {
        String implementation;
        PlatformChacha platformChacha = platformState.get();
        return (platformChacha == null || (implementation = platformChacha.getImplementation()) == null) ? "BouncyCastle" : implementation;
    }

    /* JADX INFO: compiled from: AirPlayCrypto.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\b¨\u0006\u0014"}, d2 = {"Lcom/carplay/airplay/AirPlayCrypto$X25519KeyPair;", "", "privateKey", "", "publicKey", "<init>", "([B[B)V", "getPrivateKey", "()[B", "getPublicKey", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class X25519KeyPair {
        private final byte[] privateKey;
        private final byte[] publicKey;

        public static /* synthetic */ X25519KeyPair copy$default(X25519KeyPair x25519KeyPair, byte[] bArr, byte[] bArr2, int i, Object obj) {
            if ((i & 1) != 0) {
                bArr = x25519KeyPair.privateKey;
            }
            if ((i & 2) != 0) {
                bArr2 = x25519KeyPair.publicKey;
            }
            return x25519KeyPair.copy(bArr, bArr2);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final byte[] getPrivateKey() {
            return this.privateKey;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final byte[] getPublicKey() {
            return this.publicKey;
        }

        public final X25519KeyPair copy(byte[] privateKey, byte[] publicKey) {
            Intrinsics.checkNotNullParameter(privateKey, "privateKey");
            Intrinsics.checkNotNullParameter(publicKey, "publicKey");
            return new X25519KeyPair(privateKey, publicKey);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof X25519KeyPair)) {
                return false;
            }
            X25519KeyPair x25519KeyPair = (X25519KeyPair) other;
            return Intrinsics.areEqual(this.privateKey, x25519KeyPair.privateKey) && Intrinsics.areEqual(this.publicKey, x25519KeyPair.publicKey);
        }

        public int hashCode() {
            return (Arrays.hashCode(this.privateKey) * 31) + Arrays.hashCode(this.publicKey);
        }

        public String toString() {
            return "X25519KeyPair(privateKey=" + Arrays.toString(this.privateKey) + ", publicKey=" + Arrays.toString(this.publicKey) + ")";
        }

        public X25519KeyPair(byte[] privateKey, byte[] publicKey) {
            Intrinsics.checkNotNullParameter(privateKey, "privateKey");
            Intrinsics.checkNotNullParameter(publicKey, "publicKey");
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }

        public final byte[] getPrivateKey() {
            return this.privateKey;
        }

        public final byte[] getPublicKey() {
            return this.publicKey;
        }
    }

    /* JADX INFO: compiled from: AirPlayCrypto.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\b¨\u0006\u0014"}, d2 = {"Lcom/carplay/airplay/AirPlayCrypto$Ed25519KeyPair;", "", "privateKey", "", "publicKey", "<init>", "([B[B)V", "getPrivateKey", "()[B", "getPublicKey", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Ed25519KeyPair {
        private final byte[] privateKey;
        private final byte[] publicKey;

        public static /* synthetic */ Ed25519KeyPair copy$default(Ed25519KeyPair ed25519KeyPair, byte[] bArr, byte[] bArr2, int i, Object obj) {
            if ((i & 1) != 0) {
                bArr = ed25519KeyPair.privateKey;
            }
            if ((i & 2) != 0) {
                bArr2 = ed25519KeyPair.publicKey;
            }
            return ed25519KeyPair.copy(bArr, bArr2);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final byte[] getPrivateKey() {
            return this.privateKey;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final byte[] getPublicKey() {
            return this.publicKey;
        }

        public final Ed25519KeyPair copy(byte[] privateKey, byte[] publicKey) {
            Intrinsics.checkNotNullParameter(privateKey, "privateKey");
            Intrinsics.checkNotNullParameter(publicKey, "publicKey");
            return new Ed25519KeyPair(privateKey, publicKey);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Ed25519KeyPair)) {
                return false;
            }
            Ed25519KeyPair ed25519KeyPair = (Ed25519KeyPair) other;
            return Intrinsics.areEqual(this.privateKey, ed25519KeyPair.privateKey) && Intrinsics.areEqual(this.publicKey, ed25519KeyPair.publicKey);
        }

        public int hashCode() {
            return (Arrays.hashCode(this.privateKey) * 31) + Arrays.hashCode(this.publicKey);
        }

        public String toString() {
            return "Ed25519KeyPair(privateKey=" + Arrays.toString(this.privateKey) + ", publicKey=" + Arrays.toString(this.publicKey) + ")";
        }

        public Ed25519KeyPair(byte[] privateKey, byte[] publicKey) {
            Intrinsics.checkNotNullParameter(privateKey, "privateKey");
            Intrinsics.checkNotNullParameter(publicKey, "publicKey");
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }

        public final byte[] getPrivateKey() {
            return this.privateKey;
        }

        public final byte[] getPublicKey() {
            return this.publicKey;
        }
    }

    public final X25519KeyPair x25519Generate() {
        X25519PrivateKeyParameters privateKey = new X25519PrivateKeyParameters(random);
        byte[] encoded = privateKey.getEncoded();
        Intrinsics.checkNotNullExpressionValue(encoded, "getEncoded(...)");
        byte[] encoded2 = privateKey.generatePublicKey().getEncoded();
        Intrinsics.checkNotNullExpressionValue(encoded2, "getEncoded(...)");
        return new X25519KeyPair(encoded, encoded2);
    }

    public final byte[] x25519Shared(byte[] privateKeyRaw, byte[] peerPublicKeyRaw) {
        Intrinsics.checkNotNullParameter(privateKeyRaw, "privateKeyRaw");
        Intrinsics.checkNotNullParameter(peerPublicKeyRaw, "peerPublicKeyRaw");
        X25519PrivateKeyParameters privateKey = new X25519PrivateKeyParameters(privateKeyRaw, 0);
        X25519PublicKeyParameters peerPublicKey = new X25519PublicKeyParameters(peerPublicKeyRaw, 0);
        byte[] shared = new byte[32];
        privateKey.generateSecret(peerPublicKey, shared, 0);
        return shared;
    }

    public final Ed25519KeyPair ed25519Generate() {
        Ed25519PrivateKeyParameters privateKey = new Ed25519PrivateKeyParameters(random);
        byte[] encoded = privateKey.getEncoded();
        Intrinsics.checkNotNullExpressionValue(encoded, "getEncoded(...)");
        byte[] encoded2 = privateKey.generatePublicKey().getEncoded();
        Intrinsics.checkNotNullExpressionValue(encoded2, "getEncoded(...)");
        return new Ed25519KeyPair(encoded, encoded2);
    }

    public final byte[] ed25519Sign(byte[] privateKeyRaw, byte[] data) {
        Intrinsics.checkNotNullParameter(privateKeyRaw, "privateKeyRaw");
        Intrinsics.checkNotNullParameter(data, "data");
        Ed25519Signer signer = new Ed25519Signer();
        signer.init(true, new Ed25519PrivateKeyParameters(privateKeyRaw, 0));
        signer.update(data, 0, data.length);
        byte[] bArrGenerateSignature = signer.generateSignature();
        Intrinsics.checkNotNullExpressionValue(bArrGenerateSignature, "generateSignature(...)");
        return bArrGenerateSignature;
    }

    public final boolean ed25519Verify(byte[] publicKeyRaw, byte[] data, byte[] signature) {
        Intrinsics.checkNotNullParameter(publicKeyRaw, "publicKeyRaw");
        Intrinsics.checkNotNullParameter(data, "data");
        Intrinsics.checkNotNullParameter(signature, "signature");
        try {
            Ed25519Signer signer = new Ed25519Signer();
            signer.init(false, new Ed25519PublicKeyParameters(publicKeyRaw, 0));
            signer.update(data, 0, data.length);
            return signer.verifySignature(signature);
        } catch (Exception e) {
            return false;
        }
    }

    public static /* synthetic */ byte[] hkdfSha512$default(AirPlayCrypto airPlayCrypto, byte[] bArr, byte[] bArr2, byte[] bArr3, int i, int i2, Object obj) {
        if ((i2 & 8) != 0) {
            i = 32;
        }
        return airPlayCrypto.hkdfSha512(bArr, bArr2, bArr3, i);
    }

    public final byte[] hkdfSha512(byte[] inputKeyMaterial, byte[] salt, byte[] info, int length) {
        Intrinsics.checkNotNullParameter(inputKeyMaterial, "inputKeyMaterial");
        Intrinsics.checkNotNullParameter(salt, "salt");
        Intrinsics.checkNotNullParameter(info, "info");
        HKDFBytesGenerator generator = new HKDFBytesGenerator(new SHA512Digest());
        generator.init(new HKDFParameters(inputKeyMaterial, salt, info));
        byte[] output = new byte[length];
        generator.generateBytes(output, 0, length);
        return output;
    }

    public final byte[] sha512(byte[]... parts) {
        Intrinsics.checkNotNullParameter(parts, "parts");
        return digest(new SHA512Digest(), parts);
    }

    public static /* synthetic */ byte[] chachaSeal$default(AirPlayCrypto airPlayCrypto, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, int i, Object obj) {
        if ((i & 8) != 0) {
            bArr4 = new byte[0];
        }
        return airPlayCrypto.chachaSeal(bArr, bArr2, bArr3, bArr4);
    }

    public final byte[] chachaSeal(byte[] key, byte[] nonce, byte[] plaintext, byte[] aad) throws InvalidCipherTextException {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(nonce, "nonce");
        Intrinsics.checkNotNullParameter(plaintext, "plaintext");
        Intrinsics.checkNotNullParameter(aad, "aad");
        byte[] key2 = platformChacha$core_airplay$default(this, 1, key, nonce, plaintext, aad, false, 32, null);
        if (key2 == null) {
            key2 = chachaSealBouncyCastle$core_airplay(key, nonce, plaintext, aad);
            PlatformChacha platformChacha = platformState.get();
            if (platformChacha != null) {
                platformChacha.setImplementation("BouncyCastle");
            }
        }
        return key2;
    }

    public static /* synthetic */ byte[] chachaOpen$default(AirPlayCrypto airPlayCrypto, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, int i, Object obj) {
        if ((i & 8) != 0) {
            bArr4 = new byte[0];
        }
        return airPlayCrypto.chachaOpen(bArr, bArr2, bArr3, bArr4);
    }

    public final byte[] chachaOpen(byte[] key, byte[] nonce, byte[] ciphertextAndTag, byte[] aad) throws InvalidCipherTextException {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(nonce, "nonce");
        Intrinsics.checkNotNullParameter(ciphertextAndTag, "ciphertextAndTag");
        Intrinsics.checkNotNullParameter(aad, "aad");
        byte[] key2 = platformChacha$core_airplay$default(this, 2, key, nonce, ciphertextAndTag, aad, false, 32, null);
        if (key2 == null) {
            key2 = chachaOpenBouncyCastle$core_airplay(key, nonce, ciphertextAndTag, aad);
            PlatformChacha platformChacha = platformState.get();
            if (platformChacha != null) {
                platformChacha.setImplementation("BouncyCastle");
            }
        }
        return key2;
    }

    public static /* synthetic */ byte[] platformChacha$core_airplay$default(AirPlayCrypto airPlayCrypto, int i, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, boolean z, int i2, Object obj) {
        boolean z2;
        if ((i2 & 32) == 0) {
            z2 = z;
        } else {
            z2 = Build.VERSION.SDK_INT >= 31;
        }
        return airPlayCrypto.platformChacha$core_airplay(i, bArr, bArr2, bArr3, bArr4, z2);
    }

    public final byte[] platformChacha$core_airplay(int mode, byte[] key, byte[] nonce, byte[] input, byte[] aad, boolean useDirectBuffers) throws AEADBadTagException {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(nonce, "nonce");
        Intrinsics.checkNotNullParameter(input, "input");
        Intrinsics.checkNotNullParameter(aad, "aad");
        PlatformChacha state = platformState.get();
        if (state == null) {
            return null;
        }
        try {
            Cipher cipher = state.getCipher();
            try {
                cipher.init(mode, new SecretKeySpec(key, "ChaCha20"), new IvParameterSpec(nonce));
                if (!(aad.length == 0)) {
                    cipher.updateAAD(aad);
                }
                if (!useDirectBuffers) {
                    byte[] bArrDoFinal = cipher.doFinal(input);
                    String name = cipher.getProvider().getName();
                    Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
                    state.setImplementation(name);
                    return bArrDoFinal;
                }
                ByteBuffer source = sized(state.getInput(), input.length);
                if (source.capacity() <= 1048576) {
                    state.setInput(source);
                }
                source.put(input).flip();
                ByteBuffer target = sized(state.getOutput(), RangesKt.coerceAtLeast(cipher.getOutputSize(input.length), 0));
                if (target.capacity() <= 1048576) {
                    state.setOutput(target);
                }
                int written = cipher.doFinal(source, target);
                target.flip();
                byte[] it = new byte[written];
                target.get(it);
                String name2 = cipher.getProvider().getName();
                Intrinsics.checkNotNullExpressionValue(name2, "getName(...)");
                state.setImplementation(name2);
                return it;
            } catch (AEADBadTagException failure) {
                throw failure;
            } catch (GeneralSecurityException e) {
                return null;
            }
        } catch (AEADBadTagException failure2) {
            throw failure2;
        } catch (GeneralSecurityException e2) {
        }
    }

    private final ByteBuffer sized(ByteBuffer buffer, int size) {
        ByteBuffer byteBufferAllocateDirect;
        if (buffer.capacity() >= size) {
            byteBufferAllocateDirect = buffer;
        } else {
            byteBufferAllocateDirect = ByteBuffer.allocateDirect(size > 1048576 ? size : RangesKt.coerceAtMost(Math.max(size, buffer.capacity() * 2), 1048576));
        }
        ByteBuffer $this$sized_u24lambda_u246 = byteBufferAllocateDirect;
        $this$sized_u24lambda_u246.clear();
        $this$sized_u24lambda_u246.limit(size);
        Intrinsics.checkNotNullExpressionValue(byteBufferAllocateDirect, "apply(...)");
        return byteBufferAllocateDirect;
    }

    public static /* synthetic */ byte[] chachaSealBouncyCastle$core_airplay$default(AirPlayCrypto airPlayCrypto, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, int i, Object obj) {
        if ((i & 8) != 0) {
            bArr4 = new byte[0];
        }
        return airPlayCrypto.chachaSealBouncyCastle$core_airplay(bArr, bArr2, bArr3, bArr4);
    }

    public final byte[] chachaSealBouncyCastle$core_airplay(byte[] key, byte[] nonce, byte[] plaintext, byte[] aad) throws InvalidCipherTextException {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(nonce, "nonce");
        Intrinsics.checkNotNullParameter(plaintext, "plaintext");
        Intrinsics.checkNotNullParameter(aad, "aad");
        ChaCha20Poly1305 cipher = new ChaCha20Poly1305();
        cipher.init(true, new AEADParameters(new KeyParameter(key), 128, nonce, aad));
        byte[] output = new byte[cipher.getOutputSize(plaintext.length)];
        int length = cipher.processBytes(plaintext, 0, plaintext.length, output, 0);
        cipher.doFinal(output, length);
        return output;
    }

    public static /* synthetic */ byte[] chachaOpenBouncyCastle$core_airplay$default(AirPlayCrypto airPlayCrypto, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, int i, Object obj) {
        if ((i & 8) != 0) {
            bArr4 = new byte[0];
        }
        return airPlayCrypto.chachaOpenBouncyCastle$core_airplay(bArr, bArr2, bArr3, bArr4);
    }

    public final byte[] chachaOpenBouncyCastle$core_airplay(byte[] key, byte[] nonce, byte[] ciphertextAndTag, byte[] aad) throws InvalidCipherTextException {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(nonce, "nonce");
        Intrinsics.checkNotNullParameter(ciphertextAndTag, "ciphertextAndTag");
        Intrinsics.checkNotNullParameter(aad, "aad");
        ChaCha20Poly1305 cipher = new ChaCha20Poly1305();
        cipher.init(false, new AEADParameters(new KeyParameter(key), 128, nonce, aad));
        byte[] output = new byte[cipher.getOutputSize(ciphertextAndTag.length)];
        int processed = cipher.processBytes(ciphertextAndTag, 0, ciphertextAndTag.length, output, 0);
        int finalized = cipher.doFinal(output, processed);
        int outputLength = processed + finalized;
        if (outputLength == output.length) {
            return output;
        }
        byte[] bArrCopyOf = Arrays.copyOf(output, outputLength);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        return bArrCopyOf;
    }

    public final byte[] nonce64(long counter) {
        byte[] nonce = new byte[12];
        long value = counter;
        for (int index = 4; index < 12; index++) {
            nonce[index] = (byte) value;
            value >>>= 8;
        }
        return nonce;
    }

    public final byte[] nonceLabel(String label) {
        Intrinsics.checkNotNullParameter(label, "label");
        byte[] nonce = new byte[12];
        byte[] ascii = BytesKt.asciiBytes(label);
        ArraysKt.copyInto(ascii, nonce, 4, 0, Math.min(ascii.length, 8));
        return nonce;
    }

    private final byte[] digest(Digest digest, byte[][] parts) {
        for (byte[] part : parts) {
            digest.update(part, 0, part.length);
        }
        byte[] output = new byte[digest.getDigestSize()];
        digest.doFinal(output, 0);
        return output;
    }
}
