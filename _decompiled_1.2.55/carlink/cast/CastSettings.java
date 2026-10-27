package com.carlink.cast;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Base64;
import com.carplay.airplay.AirPlayCrypto;
import com.carplay.sender.ControllerIdentity;
import com.carplay.sender.PairedAccessory;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.UUID;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.jmdns.impl.constants.DNSConstants;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import org.json.JSONObject;

/* JADX INFO: compiled from: CastSettings.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 P2\u00020\u0001:\u0001PB\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0012\u0010/\u001a\u0002002\n\b\u0002\u00101\u001a\u0004\u0018\u00010\u000bJ\u001a\u00102\u001a\u0002032\n\b\u0002\u00101\u001a\u0004\u0018\u00010\u000b2\u0006\u00104\u001a\u000200J\u0010\u00108\u001a\u0004\u0018\u0001092\u0006\u00101\u001a\u00020\u000bJ\u0016\u0010:\u001a\u0002032\u0006\u00101\u001a\u00020\u000b2\u0006\u0010;\u001a\u000209J\u000e\u0010<\u001a\u0002032\u0006\u00101\u001a\u00020\u000bJ\u0010\u0010=\u001a\u0004\u0018\u00010>2\u0006\u00101\u001a\u00020\u000bJ\u0016\u0010?\u001a\u0002032\u0006\u00101\u001a\u00020\u000b2\u0006\u0010@\u001a\u00020>J\u000e\u0010A\u001a\u0002032\u0006\u00101\u001a\u00020\u000bJ\u000e\u0010B\u001a\u00020\u001a2\u0006\u00101\u001a\u00020\u000bJ\u000e\u0010C\u001a\u00020\u001a2\u0006\u00101\u001a\u00020\u000bJ\u000e\u0010D\u001a\u0002032\u0006\u00101\u001a\u00020\u000bJ\u000e\u0010E\u001a\u0002032\u0006\u00101\u001a\u00020\u000bJ\u0006\u0010F\u001a\u00020GJ\b\u0010H\u001a\u00020IH\u0002J\u0010\u0010J\u001a\u00020\u000b2\u0006\u0010K\u001a\u000209H\u0002J\u0010\u0010L\u001a\u0002092\u0006\u0010\n\u001a\u00020\u000bH\u0002J\u0018\u0010M\u001a\n \u0007*\u0004\u0018\u00010\u000b0\u000b2\u0006\u0010K\u001a\u000209H\u0002J\u0018\u0010N\u001a\n \u0007*\u0004\u0018\u000109092\u0006\u0010\n\u001a\u00020\u000bH\u0002J\u0012\u0010O\u001a\u00020\u000b2\b\u00101\u001a\u0004\u0018\u00010\u000bH\u0002R\u0016\u0010\u0006\u001a\n \u0007*\u0004\u0018\u00010\u00030\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R(\u0010\f\u001a\u0004\u0018\u00010\u000b2\b\u0010\n\u001a\u0004\u0018\u00010\u000b8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R(\u0010\u0011\u001a\u0004\u0018\u00010\u000b2\b\u0010\n\u001a\u0004\u0018\u00010\u000b8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b\u0012\u0010\u000e\"\u0004\b\u0013\u0010\u0010R$\u0010\u0015\u001a\u00020\u00142\u0006\u0010\n\u001a\u00020\u00148F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R$\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\n\u001a\u00020\u001a8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR$\u0010 \u001a\u00020\u001a2\u0006\u0010\n\u001a\u00020\u001a8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b!\u0010\u001d\"\u0004\b\"\u0010\u001fR$\u0010#\u001a\u00020\u001a2\u0006\u0010\n\u001a\u00020\u001a8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b$\u0010\u001d\"\u0004\b%\u0010\u001fR$\u0010&\u001a\u00020\u001a2\u0006\u0010\n\u001a\u00020\u001a8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b'\u0010\u001d\"\u0004\b(\u0010\u001fR$\u0010*\u001a\u00020)2\u0006\u0010\n\u001a\u00020)8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R(\u00105\u001a\u0004\u0018\u00010\u000b2\b\u0010\n\u001a\u0004\u0018\u00010\u000b8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b6\u0010\u000e\"\u0004\b7\u0010\u0010¨\u0006\u0006"}, d2 = {"Lcom/carlink/cast/CastSettings;", "", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "app", "kotlin.jvm.PlatformType", "prefs", "Landroid/content/SharedPreferences;", "value", "", "peerAddress", "getPeerAddress", "()Ljava/lang/String;", "setPeerAddress", "(Ljava/lang/String;)V", CastSettings.KEY_PEER_NAME, "getPeerName", "setPeerName", "Lcom/carlink/cast/SourceMode;", CastSettings.KEY_SOURCE, "getSourceMode", "()Lcom/carlink/cast/SourceMode;", "setSourceMode", "(Lcom/carlink/cast/SourceMode;)V", "", CastSettings.KEY_AUDIO, "getAudio", "()Z", "setAudio", "(Z)V", CastSettings.KEY_TOUCH, "getTouch", "setTouch", CastSettings.KEY_AUTO_CONNECT, "getAutoConnect", "setAutoConnect", CastSettings.KEY_CARLIFE_AUTO_SESSION, "getCarLifeAutoSession", "setCarLifeAutoSession", "", CastSettings.KEY_FRAME_RATE, "getFrameRateOverride", "()I", "setFrameRateOverride", "(I)V", "safeAreaPolicy", "Lcom/carlink/cast/CarPanelGeometry$SafeAreaPolicy;", "endpoint", "saveSafeAreaPolicy", "", "policy", CastSettings.KEY_BT, "getManualBluetoothAddress", "setManualBluetoothAddress", "accessoryKey", "", "saveAccessoryKey", "key", "removeAccessoryKey", "paired", "Lcom/carplay/sender/PairedAccessory;", "savePair", CastSettings.KEY_PEER, "removePair", "hasPair", "hasConnected", "markConnected", "clearConnected", CastSettings.KEY_IDENTITY, "Lcom/carplay/sender/ControllerIdentity;", "secret", "Ljavax/crypto/SecretKey;", "seal", "bytes", "unseal", "encode", "decode", "safeAreaScope", "Companion"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CastSettings {
    private static final int CONNECTED_PROOF_VERSION = 2;
    public static final String KEY_AUDIO = "audio";
    public static final String KEY_AUTO_CONNECT = "autoConnect";
    public static final String KEY_BT = "manualBluetoothAddress";
    public static final String KEY_CARLIFE_AUTO_SESSION = "carLifeAutoSession";
    public static final String KEY_FRAME_RATE = "frameRateOverride";
    public static final String KEY_IDENTITY = "identity";
    public static final String KEY_PEER = "peer";
    public static final String KEY_PEER_NAME = "peerName";
    public static final String KEY_SAFE_AREA_ENABLED = "safeAreaEnabled";
    public static final String KEY_SOURCE = "sourceMode";
    public static final String KEY_STORE = "carlink-storage";
    public static final String KEY_TOUCH = "touch";
    private final Context app;
    private final SharedPreferences prefs;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final Object LOCK = new Object();

    public CastSettings(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.app = context.getApplicationContext();
        SharedPreferences sharedPreferences = this.app.getSharedPreferences("carlink", 0);
        Intrinsics.checkNotNullExpressionValue(sharedPreferences, "getSharedPreferences(...)");
        this.prefs = sharedPreferences;
    }

    public final String getPeerAddress() {
        return this.prefs.getString(KEY_PEER, null);
    }

    public final void setPeerAddress(String value) {
        this.prefs.edit().putString(KEY_PEER, value).apply();
    }

    public final String getPeerName() {
        return this.prefs.getString(KEY_PEER_NAME, null);
    }

    public final void setPeerName(String value) {
        this.prefs.edit().putString(KEY_PEER_NAME, value).apply();
    }

    public final SourceMode getSourceMode() {
        return SourceMode.INSTANCE.restore(this.prefs.getString(KEY_SOURCE, null));
    }

    public final void setSourceMode(SourceMode value) {
        Intrinsics.checkNotNullParameter(value, "value");
        this.prefs.edit().putString(KEY_SOURCE, value.name()).apply();
    }

    public final boolean getAudio() {
        return this.prefs.getBoolean(KEY_AUDIO, INSTANCE.defaultAudioEnabled$app(getSourceMode()));
    }

    public final void setAudio(boolean value) {
        this.prefs.edit().putBoolean(KEY_AUDIO, value).apply();
    }

    public final boolean getTouch() {
        return this.prefs.getBoolean(KEY_TOUCH, true);
    }

    public final void setTouch(boolean value) {
        this.prefs.edit().putBoolean(KEY_TOUCH, value).apply();
    }

    public final boolean getAutoConnect() {
        return this.prefs.getBoolean(KEY_AUTO_CONNECT, false);
    }

    public final void setAutoConnect(boolean value) {
        this.prefs.edit().putBoolean(KEY_AUTO_CONNECT, value).apply();
    }

    public final boolean getCarLifeAutoSession() {
        return this.prefs.getBoolean(KEY_CARLIFE_AUTO_SESSION, true);
    }

    public final void setCarLifeAutoSession(boolean value) {
        this.prefs.edit().putBoolean(KEY_CARLIFE_AUTO_SESSION, value).apply();
    }

    public final int getFrameRateOverride() {
        return this.prefs.getInt(KEY_FRAME_RATE, 0);
    }

    public final void setFrameRateOverride(int value) {
        this.prefs.edit().putInt(KEY_FRAME_RATE, RangesKt.coerceIn(value, 0, DNSConstants.KNOWN_ANSWER_TTL)).apply();
    }

    public static /* synthetic */ CarPanelGeometry.SafeAreaPolicy safeAreaPolicy$default(CastSettings castSettings, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            str = castSettings.getPeerAddress();
        }
        return castSettings.safeAreaPolicy(str);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:19:0x004c  */
    public final CarPanelGeometry.SafeAreaPolicy safeAreaPolicy(String endpoint) {
        CarPanelGeometry.SafeAreaMode safeAreaMode;
        String scope = safeAreaScope(endpoint);
        String string = this.prefs.getString(scope + ":mode", null);
        if (string != null) {
            switch (string) {
                case "OFF":
                    safeAreaMode = CarPanelGeometry.SafeAreaMode.OFF;
                    break;
                case "RELAXED":
                    safeAreaMode = CarPanelGeometry.SafeAreaMode.RELAXED;
                    break;
                case "CUSTOM":
                    safeAreaMode = CarPanelGeometry.SafeAreaMode.CUSTOM;
                    break;
                default:
                    safeAreaMode = CarPanelGeometry.SafeAreaMode.STRICT;
                    break;
            }
        } else {
            safeAreaMode = CarPanelGeometry.SafeAreaMode.STRICT;
        }
        CarPanelGeometry.SafeAreaMode mode = safeAreaMode;
        return new CarPanelGeometry.SafeAreaPolicy(this.prefs.getBoolean(KEY_SAFE_AREA_ENABLED, true), mode, this.prefs.getInt(scope + ":left", 0), this.prefs.getInt(scope + ":top", 0), this.prefs.getInt(scope + ":right", 0), this.prefs.getInt(scope + ":bottom", 0));
    }

    public static /* synthetic */ void saveSafeAreaPolicy$default(CastSettings castSettings, String str, CarPanelGeometry.SafeAreaPolicy safeAreaPolicy, int i, Object obj) {
        if ((i & 1) != 0) {
            str = castSettings.getPeerAddress();
        }
        castSettings.saveSafeAreaPolicy(str, safeAreaPolicy);
    }

    public final void saveSafeAreaPolicy(String endpoint, CarPanelGeometry.SafeAreaPolicy policy) {
        Intrinsics.checkNotNullParameter(policy, "policy");
        String scope = safeAreaScope(endpoint);
        this.prefs.edit().putBoolean(KEY_SAFE_AREA_ENABLED, policy.getEnabled()).putString(scope + ":mode", policy.getMode().name()).putInt(scope + ":left", RangesKt.coerceAtLeast(policy.getLeft(), 0)).putInt(scope + ":top", RangesKt.coerceAtLeast(policy.getTop(), 0)).putInt(scope + ":right", RangesKt.coerceAtLeast(policy.getRight(), 0)).putInt(scope + ":bottom", RangesKt.coerceAtLeast(policy.getBottom(), 0)).apply();
    }

    public final String getManualBluetoothAddress() {
        return this.prefs.getString(KEY_BT, null);
    }

    public final void setManualBluetoothAddress(String value) {
        String upperCase;
        String string;
        SharedPreferences.Editor editorEdit = this.prefs.edit();
        if (value == null || (string = StringsKt.trim((CharSequence) value).toString()) == null) {
            upperCase = null;
        } else {
            upperCase = string.toUpperCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(upperCase, "toUpperCase(...)");
        }
        editorEdit.putString(KEY_BT, upperCase).apply();
    }

    public final byte[] accessoryKey(String endpoint) {
        Intrinsics.checkNotNullParameter(endpoint, "endpoint");
        String string = this.prefs.getString("accessoryKey:" + endpoint, null);
        if (string != null) {
            return unseal(string);
        }
        return null;
    }

    public final void saveAccessoryKey(String endpoint, byte[] key) {
        Intrinsics.checkNotNullParameter(endpoint, "endpoint");
        Intrinsics.checkNotNullParameter(key, "key");
        if (!this.prefs.edit().putString("accessoryKey:" + endpoint, seal(key)).commit()) {
            throw new IllegalStateException("Check failed.");
        }
    }

    public final void removeAccessoryKey(String endpoint) {
        Intrinsics.checkNotNullParameter(endpoint, "endpoint");
        this.prefs.edit().remove("accessoryKey:" + endpoint).apply();
    }

    public final PairedAccessory paired(String endpoint) {
        Intrinsics.checkNotNullParameter(endpoint, "endpoint");
        String string = this.prefs.getString("pair:" + endpoint, null);
        if (string == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject(new String(unseal(string), Charsets.UTF_8));
        String string2 = jSONObject.getString("id");
        Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
        String string3 = jSONObject.getString("public");
        Intrinsics.checkNotNullExpressionValue(string3, "getString(...)");
        byte[] bArrDecode = decode(string3);
        Intrinsics.checkNotNullExpressionValue(bArrDecode, "decode(...)");
        return new PairedAccessory(string2, bArrDecode);
    }

    public final void savePair(String endpoint, PairedAccessory peer) {
        Intrinsics.checkNotNullParameter(endpoint, "endpoint");
        Intrinsics.checkNotNullParameter(peer, "peer");
        JSONObject json = new JSONObject().put("id", peer.getId()).put("public", encode(peer.getPublicKey()));
        SharedPreferences.Editor editorEdit = this.prefs.edit();
        String str = "pair:" + endpoint;
        String string = json.toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        byte[] bytes = string.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        if (!editorEdit.putString(str, seal(bytes)).commit()) {
            throw new IllegalStateException("Check failed.");
        }
    }

    public final void removePair(String endpoint) {
        Intrinsics.checkNotNullParameter(endpoint, "endpoint");
        this.prefs.edit().remove("pair:" + endpoint).apply();
    }

    public final boolean hasPair(String endpoint) {
        Intrinsics.checkNotNullParameter(endpoint, "endpoint");
        return this.prefs.contains("pair:" + endpoint);
    }

    public final boolean hasConnected(String endpoint) {
        Intrinsics.checkNotNullParameter(endpoint, "endpoint");
        return this.prefs.getBoolean(new StringBuilder().append("connected:").append(endpoint).toString(), false) && this.prefs.getInt(new StringBuilder().append("connectedProofVersion:").append(endpoint).toString(), 0) == 2;
    }

    public final void markConnected(String endpoint) {
        Intrinsics.checkNotNullParameter(endpoint, "endpoint");
        this.prefs.edit().putBoolean("connected:" + endpoint, true).putInt("connectedProofVersion:" + endpoint, 2).apply();
    }

    public final void clearConnected(String endpoint) {
        Intrinsics.checkNotNullParameter(endpoint, "endpoint");
        this.prefs.edit().remove("connected:" + endpoint).remove("connectedProofVersion:" + endpoint).apply();
    }

    public final ControllerIdentity identity() {
        ControllerIdentity controllerIdentity;
        synchronized (LOCK) {
            String string = this.prefs.getString(KEY_IDENTITY, null);
            if (string != null) {
                JSONObject jSONObject = new JSONObject(new String(unseal(string), Charsets.UTF_8));
                String string2 = jSONObject.getString("private");
                Intrinsics.checkNotNullExpressionValue(string2, "getString(...)");
                byte[] bArrDecode = decode(string2);
                Intrinsics.checkNotNullExpressionValue(bArrDecode, "decode(...)");
                String string3 = jSONObject.getString("public");
                Intrinsics.checkNotNullExpressionValue(string3, "getString(...)");
                byte[] bArrDecode2 = decode(string3);
                Intrinsics.checkNotNullExpressionValue(bArrDecode2, "decode(...)");
                String string4 = jSONObject.getString("id");
                Intrinsics.checkNotNullExpressionValue(string4, "getString(...)");
                controllerIdentity = new ControllerIdentity(bArrDecode, bArrDecode2, string4);
            } else {
                AirPlayCrypto.Ed25519KeyPair ed25519KeyPairEd25519Generate = AirPlayCrypto.INSTANCE.ed25519Generate();
                byte[] privateKey = ed25519KeyPairEd25519Generate.getPrivateKey();
                byte[] publicKey = ed25519KeyPairEd25519Generate.getPublicKey();
                String string5 = UUID.randomUUID().toString();
                Intrinsics.checkNotNullExpressionValue(string5, "toString(...)");
                controllerIdentity = new ControllerIdentity(privateKey, publicKey, string5);
                JSONObject jSONObjectPut = new JSONObject().put("private", encode(ed25519KeyPairEd25519Generate.getPrivateKey())).put("public", encode(ed25519KeyPairEd25519Generate.getPublicKey())).put("id", controllerIdentity.getId());
                SharedPreferences.Editor editorEdit = this.prefs.edit();
                String string6 = jSONObjectPut.toString();
                Intrinsics.checkNotNullExpressionValue(string6, "toString(...)");
                byte[] bytes = string6.getBytes(Charsets.UTF_8);
                Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
                if (!editorEdit.putString(KEY_IDENTITY, seal(bytes)).commit()) {
                    throw new IllegalStateException("Check failed.");
                }
            }
        }
        return controllerIdentity;
    }

    private final SecretKey secret() {
        SecretKey secretKey;
        synchronized (LOCK) {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            if (!keyStore.containsAlias(KEY_STORE)) {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "AndroidKeyStore");
                keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_STORE, 3).setBlockModes("GCM").setEncryptionPaddings("NoPadding").setKeySize(256).build());
                keyGenerator.generateKey();
            }
            Key key = keyStore.getKey(KEY_STORE, null);
            Intrinsics.checkNotNull(key, "null cannot be cast to non-null type javax.crypto.SecretKey");
            secretKey = (SecretKey) key;
        }
        return secretKey;
    }

    private final String seal(byte[] bytes) throws BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(1, secret());
        byte[] iv = cipher.getIV();
        Intrinsics.checkNotNullExpressionValue(iv, "getIV(...)");
        byte[] bArrDoFinal = cipher.doFinal(bytes);
        Intrinsics.checkNotNullExpressionValue(bArrDoFinal, "doFinal(...)");
        String strEncode = encode(ArraysKt.plus(iv, bArrDoFinal));
        Intrinsics.checkNotNullExpressionValue(strEncode, "run(...)");
        return strEncode;
    }

    private final byte[] unseal(String value) throws BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] bArrDecode = decode(value);
        if (!(bArrDecode.length >= 28)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        SecretKey secretKeySecret = secret();
        Intrinsics.checkNotNull(bArrDecode);
        cipher.init(2, secretKeySecret, new GCMParameterSpec(128, ArraysKt.copyOfRange(bArrDecode, 0, 12)));
        byte[] bArrDoFinal = cipher.doFinal(ArraysKt.copyOfRange(bArrDecode, 12, bArrDecode.length));
        Intrinsics.checkNotNullExpressionValue(bArrDoFinal, "run(...)");
        return bArrDoFinal;
    }

    private final String encode(byte[] bytes) {
        return Base64.encodeToString(bytes, 2);
    }

    private final byte[] decode(String value) {
        return Base64.decode(value, 2);
    }

    private final String safeAreaScope(String endpoint) {
        String str = endpoint == null ? "" : endpoint;
        if (StringsKt.isBlank(str)) {
            str = "default";
        }
        return "safeArea:" + ((Object) str);
    }

    /* JADX INFO: compiled from: CastSettings.kt */
    @Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\b\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0015\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0000¢\u0006\u0002\b\bR\u000e\u0010\t\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082T¢\u0006\u0002\n\u0000R\u0011\u0010\u0018\u001a\u00020\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001a¨\u0006\u001b"}, d2 = {"Lcom/carlink/cast/CastSettings$Companion;", "", "<init>", "()V", "defaultAudioEnabled", "", CastSettings.KEY_SOURCE, "Lcom/carlink/cast/SourceMode;", "defaultAudioEnabled$app", "KEY_PEER", "", "KEY_PEER_NAME", "KEY_SOURCE", "KEY_AUDIO", "KEY_TOUCH", "KEY_AUTO_CONNECT", "KEY_CARLIFE_AUTO_SESSION", "KEY_FRAME_RATE", "KEY_SAFE_AREA_ENABLED", "KEY_BT", "KEY_IDENTITY", "KEY_STORE", "CONNECTED_PROOF_VERSION", "", "LOCK", "getLOCK", "()Ljava/lang/Object;", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean defaultAudioEnabled$app(SourceMode sourceMode) {
            Intrinsics.checkNotNullParameter(sourceMode, "sourceMode");
            return sourceMode != SourceMode.MIRROR;
        }

        public final Object getLOCK() {
            return CastSettings.LOCK;
        }
    }
}
