package com.carplay.airplay;

import androidx.core.app.NotificationCompat;
import com.carlink.cast.CastSettings;
import java.util.Map;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: PairSetup.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u0000 \u001b2\u00020\u0001:\u0001\u001bB\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u000e\u0010\u0011\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u000bJ\b\u0010\u0013\u001a\u00020\u000bH\u0002J\u001c\u0010\u0014\u001a\u00020\u000b2\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u000b0\u0016H\u0002J\u001c\u0010\u0018\u001a\u00020\u000b2\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u000b0\u0016H\u0002J\u0010\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u001a\u001a\u00020\u0017H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u000e\u001a\u00020\r2\u0006\u0010\f\u001a\u00020\r@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u001c"}, d2 = {"Lcom/carplay/airplay/PairSetup;", "", CastSettings.KEY_IDENTITY, "Lcom/carplay/airplay/AirPlayIdentity;", "pairings", "Lcom/carplay/airplay/PairingStore;", "<init>", "(Lcom/carplay/airplay/AirPlayIdentity;Lcom/carplay/airplay/PairingStore;)V", "srp", "Lcom/carplay/airplay/Srp6aSession;", "sessionKey", "", "value", "", "complete", "getComplete", "()Z", "handle", "body", "m2", "m4", "tlv", "", "", "m6", NotificationCompat.CATEGORY_ERROR, "state", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class PairSetup {

    @Deprecated
    public static final String ACCESSORY_SIGN_INFO = "Pair-Setup-Accessory-Sign-Info";

    @Deprecated
    public static final String ACCESSORY_SIGN_SALT = "Pair-Setup-Accessory-Sign-Salt";

    @Deprecated
    public static final String CONTROLLER_SIGN_INFO = "Pair-Setup-Controller-Sign-Info";

    @Deprecated
    public static final String CONTROLLER_SIGN_SALT = "Pair-Setup-Controller-Sign-Salt";
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final String ENCRYPT_INFO = "Pair-Setup-Encrypt-Info";

    @Deprecated
    public static final String ENCRYPT_SALT = "Pair-Setup-Encrypt-Salt";

    @Deprecated
    public static final int ERROR_AUTHENTICATION = 2;

    @Deprecated
    public static final String MSG05 = "PS-Msg05";

    @Deprecated
    public static final String MSG06 = "PS-Msg06";

    @Deprecated
    public static final String SETUP_CODE = "3939";

    @Deprecated
    public static final String SETUP_USERNAME = "Pair-Setup";

    @Deprecated
    public static final int TYPE_ENCRYPTED_DATA = 5;

    @Deprecated
    public static final int TYPE_ERROR = 7;

    @Deprecated
    public static final int TYPE_IDENTIFIER = 1;

    @Deprecated
    public static final int TYPE_METHOD = 0;

    @Deprecated
    public static final int TYPE_PROOF = 4;

    @Deprecated
    public static final int TYPE_PUBLIC_KEY = 3;

    @Deprecated
    public static final int TYPE_SALT = 2;

    @Deprecated
    public static final int TYPE_SIGNATURE = 10;

    @Deprecated
    public static final int TYPE_STATE = 6;
    private boolean complete;
    private final AirPlayIdentity identity;
    private final PairingStore pairings;
    private byte[] sessionKey;
    private Srp6aSession srp;

    public PairSetup(AirPlayIdentity identity, PairingStore pairings) {
        Intrinsics.checkNotNullParameter(identity, "identity");
        Intrinsics.checkNotNullParameter(pairings, "pairings");
        this.identity = identity;
        this.pairings = pairings;
    }

    public final boolean getComplete() {
        return this.complete;
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
                    return m2();
                }
            } catch (Exception e) {
                return err(state != null ? state.intValue() : 0);
            }
        }
        if (state != null && state.intValue() == 3) {
            return m4(mapDecode);
        }
        if (state != null && state.intValue() == 5) {
            return m6(mapDecode);
        }
        return err(state != null ? state.intValue() : 0);
    }

    private final byte[] m2() {
        Srp6aSession session = Srp6a.INSTANCE.start(SETUP_USERNAME, SETUP_CODE);
        this.srp = session;
        return Tlv8Codec.INSTANCE.encode(CollectionsKt.listOf((Object[]) new Tlv8Item[]{new Tlv8Item(6, new byte[]{2}), new Tlv8Item(3, session.getPublicKey()), new Tlv8Item(2, session.getSalt())}));
    }

    private final byte[] m4(Map<Integer, byte[]> tlv) {
        byte[] publicKeyA = tlv.get(3);
        byte[] proof = tlv.get(4);
        Srp6aSession session = this.srp;
        if (session == null || publicKeyA == null || proof == null) {
            return err(4);
        }
        Srp6aResult result = session.verify(publicKeyA, proof);
        if (!result.getOk() || result.getSessionKey() == null || result.getServerProof() == null) {
            return err(4);
        }
        this.sessionKey = result.getSessionKey();
        return Tlv8Codec.INSTANCE.encode(CollectionsKt.listOf((Object[]) new Tlv8Item[]{new Tlv8Item(6, new byte[]{4}), new Tlv8Item(4, result.getServerProof())}));
    }

    private final byte[] m6(Map<Integer, byte[]> tlv) {
        byte[] encrypted = tlv.get(5);
        byte[] key = this.sessionKey;
        if (key == null || encrypted == null) {
            return err(6);
        }
        byte[] encryptionKey = AirPlayCrypto.hkdfSha512$default(AirPlayCrypto.INSTANCE, key, BytesKt.asciiBytes(ENCRYPT_SALT), BytesKt.asciiBytes(ENCRYPT_INFO), 0, 8, null);
        Map<Integer, byte[]> mapDecode = Tlv8Codec.INSTANCE.decode(AirPlayCrypto.chachaOpen$default(AirPlayCrypto.INSTANCE, encryptionKey, AirPlayCrypto.INSTANCE.nonceLabel(MSG05), encrypted, null, 8, null));
        byte[] controllerId = mapDecode.get(1);
        byte[] controllerLtpk = mapDecode.get(3);
        byte[] controllerSignature = mapDecode.get(10);
        if (controllerId == null || controllerLtpk == null || controllerSignature == null) {
            return err(6);
        }
        byte[] controllerSignKey = AirPlayCrypto.hkdfSha512$default(AirPlayCrypto.INSTANCE, key, BytesKt.asciiBytes(CONTROLLER_SIGN_SALT), BytesKt.asciiBytes(CONTROLLER_SIGN_INFO), 0, 8, null);
        byte[] controllerSignData = BytesKt.concatBytes(controllerSignKey, controllerId, controllerLtpk);
        if (!AirPlayCrypto.INSTANCE.ed25519Verify(controllerLtpk, controllerSignData, controllerSignature)) {
            return err(6);
        }
        this.pairings.save(new String(controllerId, Charsets.UTF_8), controllerLtpk);
        byte[] accessoryId = this.identity.getPairingId().getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(accessoryId, "getBytes(...)");
        byte[] accessorySignKey = AirPlayCrypto.hkdfSha512$default(AirPlayCrypto.INSTANCE, key, BytesKt.asciiBytes(ACCESSORY_SIGN_SALT), BytesKt.asciiBytes(ACCESSORY_SIGN_INFO), 0, 8, null);
        byte[] accessorySignData = BytesKt.concatBytes(accessorySignKey, accessoryId, this.identity.getPublicKey());
        byte[] accessorySignature = AirPlayCrypto.INSTANCE.ed25519Sign(this.identity.getPrivateKey(), accessorySignData);
        byte[] subResponse = Tlv8Codec.INSTANCE.encode(CollectionsKt.listOf((Object[]) new Tlv8Item[]{new Tlv8Item(1, accessoryId), new Tlv8Item(3, this.identity.getPublicKey()), new Tlv8Item(10, accessorySignature)}));
        byte[] sealed = AirPlayCrypto.chachaSeal$default(AirPlayCrypto.INSTANCE, encryptionKey, AirPlayCrypto.INSTANCE.nonceLabel(MSG06), subResponse, null, 8, null);
        this.complete = true;
        return Tlv8Codec.INSTANCE.encode(CollectionsKt.listOf((Object[]) new Tlv8Item[]{new Tlv8Item(6, new byte[]{6}), new Tlv8Item(5, sealed)}));
    }

    private final byte[] err(int state) {
        return Tlv8Codec.INSTANCE.encode(CollectionsKt.listOf((Object[]) new Tlv8Item[]{new Tlv8Item(6, new byte[]{(byte) state}), new Tlv8Item(7, new byte[]{2})}));
    }

    /* JADX INFO: compiled from: PairSetup.kt */
    @Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\u000b\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Lcom/carplay/airplay/PairSetup$Companion;", "", "<init>", "()V", "TYPE_METHOD", "", "TYPE_IDENTIFIER", "TYPE_SALT", "TYPE_PUBLIC_KEY", "TYPE_PROOF", "TYPE_ENCRYPTED_DATA", "TYPE_STATE", "TYPE_ERROR", "TYPE_SIGNATURE", "SETUP_USERNAME", "", "SETUP_CODE", "ERROR_AUTHENTICATION", "ENCRYPT_SALT", "ENCRYPT_INFO", "CONTROLLER_SIGN_SALT", "CONTROLLER_SIGN_INFO", "ACCESSORY_SIGN_SALT", "ACCESSORY_SIGN_INFO", "MSG05", "MSG06", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
