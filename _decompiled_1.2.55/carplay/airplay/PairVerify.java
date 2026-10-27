package com.carplay.airplay;

import androidx.core.app.NotificationCompat;
import com.carlink.cast.CastSettings;
import java.util.Arrays;
import java.util.Map;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: PairVerify.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u0000 (2\u00020\u0001:\u0002'(B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u000e\u0010\u001e\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020\tJ\u001c\u0010 \u001a\u00020\t2\u0012\u0010!\u001a\u000e\u0012\u0004\u0012\u00020#\u0012\u0004\u0012\u00020\t0\"H\u0002J\u001c\u0010$\u001a\u00020\t2\u0012\u0010!\u001a\u000e\u0012\u0004\u0012\u00020#\u0012\u0004\u0012\u00020\t0\"H\u0002J\u0010\u0010%\u001a\u00020\t2\u0006\u0010&\u001a\u00020#H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0013\u001a\u00020\u00108F¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014R\u0013\u0010\u0015\u001a\u0004\u0018\u00010\u00128F¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u0013\u0010\u0018\u001a\u0004\u0018\u00010\u000e8F¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001aR\u0013\u0010\u001b\u001a\u0004\u0018\u00010\t8F¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u001d¨\u0006)"}, d2 = {"Lcom/carplay/airplay/PairVerify;", "", CastSettings.KEY_IDENTITY, "Lcom/carplay/airplay/AirPlayIdentity;", "pairings", "Lcom/carplay/airplay/PairingStore;", "<init>", "(Lcom/carplay/airplay/AirPlayIdentity;Lcom/carplay/airplay/PairingStore;)V", "ephemeralPublicKey", "", "clientEphemeralPublicKey", "sharedSecret", "encryptionKey", "keys", "Lcom/carplay/airplay/PairVerify$ControlKeys;", "verified", "", "controllerId", "", "isVerified", "()Z", "verifiedControllerId", "getVerifiedControllerId", "()Ljava/lang/String;", "controlKeys", "getControlKeys", "()Lcom/carplay/airplay/PairVerify$ControlKeys;", "shared", "getShared", "()[B", "handle", "body", "m2", "tlv", "", "", "m4", NotificationCompat.CATEGORY_ERROR, "state", "ControlKeys", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class PairVerify {

    @Deprecated
    public static final String CONTROL_READ_KEY_INFO = "Control-Read-Encryption-Key";

    @Deprecated
    public static final String CONTROL_SALT = "Control-Salt";

    @Deprecated
    public static final String CONTROL_WRITE_KEY_INFO = "Control-Write-Encryption-Key";
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int ERROR_AUTHENTICATION = 2;

    @Deprecated
    public static final String MSG02 = "PV-Msg02";

    @Deprecated
    public static final String MSG03 = "PV-Msg03";

    @Deprecated
    public static final int TYPE_ENCRYPTED_DATA = 5;

    @Deprecated
    public static final int TYPE_ERROR = 7;

    @Deprecated
    public static final int TYPE_IDENTIFIER = 1;

    @Deprecated
    public static final int TYPE_PUBLIC_KEY = 3;

    @Deprecated
    public static final int TYPE_SIGNATURE = 10;

    @Deprecated
    public static final int TYPE_STATE = 6;

    @Deprecated
    public static final String VERIFY_ENCRYPT_INFO = "Pair-Verify-Encrypt-Info";

    @Deprecated
    public static final String VERIFY_ENCRYPT_SALT = "Pair-Verify-Encrypt-Salt";
    private byte[] clientEphemeralPublicKey;
    private String controllerId;
    private byte[] encryptionKey;
    private byte[] ephemeralPublicKey;
    private final AirPlayIdentity identity;
    private ControlKeys keys;
    private final PairingStore pairings;
    private byte[] sharedSecret;
    private boolean verified;

    public PairVerify(AirPlayIdentity identity, PairingStore pairings) {
        Intrinsics.checkNotNullParameter(identity, "identity");
        Intrinsics.checkNotNullParameter(pairings, "pairings");
        this.identity = identity;
        this.pairings = pairings;
    }

    /* JADX INFO: compiled from: PairVerify.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\b¨\u0006\u0014"}, d2 = {"Lcom/carplay/airplay/PairVerify$ControlKeys;", "", "readKey", "", "writeKey", "<init>", "([B[B)V", "getReadKey", "()[B", "getWriteKey", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class ControlKeys {
        private final byte[] readKey;
        private final byte[] writeKey;

        public static /* synthetic */ ControlKeys copy$default(ControlKeys controlKeys, byte[] bArr, byte[] bArr2, int i, Object obj) {
            if ((i & 1) != 0) {
                bArr = controlKeys.readKey;
            }
            if ((i & 2) != 0) {
                bArr2 = controlKeys.writeKey;
            }
            return controlKeys.copy(bArr, bArr2);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final byte[] getReadKey() {
            return this.readKey;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final byte[] getWriteKey() {
            return this.writeKey;
        }

        public final ControlKeys copy(byte[] readKey, byte[] writeKey) {
            Intrinsics.checkNotNullParameter(readKey, "readKey");
            Intrinsics.checkNotNullParameter(writeKey, "writeKey");
            return new ControlKeys(readKey, writeKey);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ControlKeys)) {
                return false;
            }
            ControlKeys controlKeys = (ControlKeys) other;
            return Intrinsics.areEqual(this.readKey, controlKeys.readKey) && Intrinsics.areEqual(this.writeKey, controlKeys.writeKey);
        }

        public int hashCode() {
            return (Arrays.hashCode(this.readKey) * 31) + Arrays.hashCode(this.writeKey);
        }

        public String toString() {
            return "ControlKeys(readKey=" + Arrays.toString(this.readKey) + ", writeKey=" + Arrays.toString(this.writeKey) + ")";
        }

        public ControlKeys(byte[] readKey, byte[] writeKey) {
            Intrinsics.checkNotNullParameter(readKey, "readKey");
            Intrinsics.checkNotNullParameter(writeKey, "writeKey");
            this.readKey = readKey;
            this.writeKey = writeKey;
        }

        public final byte[] getReadKey() {
            return this.readKey;
        }

        public final byte[] getWriteKey() {
            return this.writeKey;
        }
    }

    /* JADX INFO: renamed from: isVerified, reason: from getter */
    public final boolean getVerified() {
        return this.verified;
    }

    /* JADX INFO: renamed from: getVerifiedControllerId, reason: from getter */
    public final String getControllerId() {
        return this.controllerId;
    }

    /* JADX INFO: renamed from: getControlKeys, reason: from getter */
    public final ControlKeys getKeys() {
        return this.keys;
    }

    /* JADX INFO: renamed from: getShared, reason: from getter */
    public final byte[] getSharedSecret() {
        return this.sharedSecret;
    }

    public final byte[] handle(byte[] body) {
        Byte bFirstOrNull;
        Intrinsics.checkNotNullParameter(body, "body");
        Map<Integer, byte[]> mapDecode = Tlv8Codec.INSTANCE.decode(body);
        byte[] bArr = mapDecode.get(6);
        Integer state = (bArr == null || (bFirstOrNull = ArraysKt.firstOrNull(bArr)) == null) ? null : Integer.valueOf(bFirstOrNull.byteValue() & UByte.MAX_VALUE);
        if (state != null) {
            try {
                if (state.intValue() == 1) {
                    return m2(mapDecode);
                }
            } catch (Exception e) {
                return err(state != null ? state.intValue() : 0);
            }
        }
        if (state != null && state.intValue() == 3) {
            return m4(mapDecode);
        }
        return err(state != null ? state.intValue() : 0);
    }

    private final byte[] m2(Map<Integer, byte[]> tlv) {
        byte[] clientEphemeralPublicKey = tlv.get(3);
        if (clientEphemeralPublicKey == null) {
            return err(2);
        }
        AirPlayCrypto.X25519KeyPair pair = AirPlayCrypto.INSTANCE.x25519Generate();
        this.ephemeralPublicKey = pair.getPublicKey();
        this.clientEphemeralPublicKey = clientEphemeralPublicKey;
        byte[] shared = AirPlayCrypto.INSTANCE.x25519Shared(pair.getPrivateKey(), clientEphemeralPublicKey);
        this.sharedSecret = shared;
        byte[] encryptionKey = AirPlayCrypto.hkdfSha512$default(AirPlayCrypto.INSTANCE, shared, BytesKt.asciiBytes(VERIFY_ENCRYPT_SALT), BytesKt.asciiBytes(VERIFY_ENCRYPT_INFO), 0, 8, null);
        this.encryptionKey = encryptionKey;
        byte[] accessoryId = this.identity.getPairingId().getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(accessoryId, "getBytes(...)");
        byte[] signature = AirPlayCrypto.INSTANCE.ed25519Sign(this.identity.getPrivateKey(), BytesKt.concatBytes(pair.getPublicKey(), accessoryId, clientEphemeralPublicKey));
        byte[] sub = Tlv8Codec.INSTANCE.encode(CollectionsKt.listOf((Object[]) new Tlv8Item[]{new Tlv8Item(1, accessoryId), new Tlv8Item(10, signature)}));
        byte[] sealed = AirPlayCrypto.chachaSeal$default(AirPlayCrypto.INSTANCE, encryptionKey, AirPlayCrypto.INSTANCE.nonceLabel(MSG02), sub, null, 8, null);
        return Tlv8Codec.INSTANCE.encode(CollectionsKt.listOf((Object[]) new Tlv8Item[]{new Tlv8Item(6, new byte[]{2}), new Tlv8Item(3, pair.getPublicKey()), new Tlv8Item(5, sealed)}));
    }

    private final byte[] m4(Map<Integer, byte[]> tlv) {
        byte[] encrypted = tlv.get(5);
        byte[] encryptionKey = this.encryptionKey;
        byte[] shared = this.sharedSecret;
        byte[] ownEphemeralPublicKey = this.ephemeralPublicKey;
        byte[] clientEphemeralPublicKey = this.clientEphemeralPublicKey;
        if (encryptionKey == null || shared == null || ownEphemeralPublicKey == null || clientEphemeralPublicKey == null || encrypted == null) {
            return err(4);
        }
        Map<Integer, byte[]> mapDecode = Tlv8Codec.INSTANCE.decode(AirPlayCrypto.chachaOpen$default(AirPlayCrypto.INSTANCE, encryptionKey, AirPlayCrypto.INSTANCE.nonceLabel(MSG03), encrypted, null, 8, null));
        byte[] controllerId = mapDecode.get(1);
        byte[] controllerSignature = mapDecode.get(10);
        if (controllerId == null || controllerSignature == null) {
            return err(4);
        }
        byte[] controllerLtpk = this.pairings.get(new String(controllerId, Charsets.UTF_8));
        if (controllerLtpk == null) {
            return err(4);
        }
        byte[] signatureData = BytesKt.concatBytes(clientEphemeralPublicKey, controllerId, ownEphemeralPublicKey);
        if (!AirPlayCrypto.INSTANCE.ed25519Verify(controllerLtpk, signatureData, controllerSignature)) {
            return err(4);
        }
        byte[] readKey = AirPlayCrypto.hkdfSha512$default(AirPlayCrypto.INSTANCE, shared, BytesKt.asciiBytes(CONTROL_SALT), BytesKt.asciiBytes(CONTROL_WRITE_KEY_INFO), 0, 8, null);
        byte[] writeKey = AirPlayCrypto.hkdfSha512$default(AirPlayCrypto.INSTANCE, shared, BytesKt.asciiBytes(CONTROL_SALT), BytesKt.asciiBytes(CONTROL_READ_KEY_INFO), 0, 8, null);
        this.keys = new ControlKeys(readKey, writeKey);
        this.verified = true;
        this.controllerId = new String(controllerId, Charsets.UTF_8);
        return Tlv8Codec.INSTANCE.encode(CollectionsKt.listOf(new Tlv8Item(6, new byte[]{4})));
    }

    private final byte[] err(int state) {
        return Tlv8Codec.INSTANCE.encode(CollectionsKt.listOf((Object[]) new Tlv8Item[]{new Tlv8Item(6, new byte[]{(byte) state}), new Tlv8Item(7, new byte[]{2})}));
    }

    /* JADX INFO: compiled from: PairVerify.kt */
    @Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\rX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\rX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\rX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\rX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\rX\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lcom/carplay/airplay/PairVerify$Companion;", "", "<init>", "()V", "TYPE_IDENTIFIER", "", "TYPE_PUBLIC_KEY", "TYPE_ENCRYPTED_DATA", "TYPE_STATE", "TYPE_ERROR", "TYPE_SIGNATURE", "ERROR_AUTHENTICATION", "VERIFY_ENCRYPT_SALT", "", "VERIFY_ENCRYPT_INFO", "CONTROL_SALT", "CONTROL_READ_KEY_INFO", "CONTROL_WRITE_KEY_INFO", "MSG02", "MSG03", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
