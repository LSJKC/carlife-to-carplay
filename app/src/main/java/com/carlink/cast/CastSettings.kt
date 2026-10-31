package com.carlink.cast

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.carplay.airplay.AirPlayCrypto
import com.carplay.sender.ControllerIdentity
import com.carplay.sender.PairedAccessory
import org.json.JSONObject
import java.security.KeyStore
import java.security.KeyPairGenerator
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/** Which picture the phone produces. */
enum class SourceMode(val label: String) {
    AUTO("自动"),
    CARLIFE("CarLife 车机模式"),
    MIRROR("屏幕镜像");

    companion object {
        fun restore(value: String?): SourceMode = entries.firstOrNull { it.name == value } ?: AUTO
    }
}

/**
 * All persistent state for the app.
 *
 * Keys and pairing material are sealed with an AndroidKeyStore AES key, so a
 * backup or a rooted file copy cannot impersonate this phone's AirPlay identity.
 */
class CastSettings(context: Context) {
    private val app = context.applicationContext
    private val prefs: SharedPreferences = app.getSharedPreferences("carlink", Context.MODE_PRIVATE)

    var peerAddress: String?
        get() = prefs.getString(KEY_PEER, null)
        set(value) = prefs.edit().putString(KEY_PEER, value).apply()

    var peerName: String?
        get() = prefs.getString(KEY_PEER_NAME, null)
        set(value) = prefs.edit().putString(KEY_PEER_NAME, value).apply()

    var sourceMode: SourceMode
        get() = SourceMode.restore(prefs.getString(KEY_SOURCE, null))
        set(value) = prefs.edit().putString(KEY_SOURCE, value.name).apply()

    /**
     * CarLife supplies encoded media audio itself; mirror mode needs the user
     * to opt in because it captures playback through Android's audio API.
     * Respect an explicit choice in either mode.
     */
    var audio: Boolean
        get() = prefs.getBoolean(KEY_AUDIO, defaultAudioEnabled(sourceMode))
        set(value) = prefs.edit().putBoolean(KEY_AUDIO, value).apply()

    var touch: Boolean
        get() = prefs.getBoolean(KEY_TOUCH, true)
        set(value) = prefs.edit().putBoolean(KEY_TOUCH, value).apply()

    /**
     * Start a session by itself when the car's Bluetooth link appears.
     *
     * Off by default. This is **not** what makes the first connection work:
     * that path belongs to Honor's Car Connect settings (通用模式 → 通过无线
     * WiFi 连接 → 下一步 → 开始连接), which starts CarLife's own connect
     * service. Switching this on does not replace that, and turning it on by
     * default only risks starting a session nobody asked for.
     */
    var autoConnect: Boolean
        get() = prefs.getBoolean(KEY_AUTO_CONNECT, false)
        set(value) = prefs.edit().putBoolean(KEY_AUTO_CONNECT, value).apply()

    /**
     * CarLife has its own lifecycle trigger.  It follows the car's Bluetooth
     * presence after the first successful pairing, while the legacy
     * [autoConnect] switch remains reserved for screen-mirror sessions.
     */
    var carLifeAutoSession: Boolean
        get() = prefs.getBoolean(KEY_CARLIFE_AUTO_SESSION, true)
        set(value) = prefs.edit().putBoolean(KEY_CARLIFE_AUTO_SESSION, value).apply()

    /**
     * The frame rate to ask for, overriding what the car declares.
     *
     * This head unit advertises `maxFPS: 30` in `/info`, and a real iPhone gets
     * the same number, so 30 is the safe default. It is exposed anyway because
     * a declared maximum is not proof of what the decoder can sustain, and the
     * only way to settle that is to try. Off-spec values are not silently
     * clamped here: the value is passed through and the car's own answer is
     * logged, so a rejected or ignored request is visible instead of looking
     * like the setting did nothing.
     */
    var frameRateOverride: Int
        get() = prefs.getInt(KEY_FRAME_RATE, 0)
        set(value) = prefs.edit().putInt(KEY_FRAME_RATE, value.coerceIn(0, 120)).apply()

    /**
     * Video bitrate in Mbps for the compositor's re-encode.
     *
     * 8 is the value that has been in every build so far and the value a
     * sender that stays connected uses on this head unit, so it is the
     * default. Lower trades sharpness for headroom on a congested link;
     * higher spends bandwidth for detail. The encoder clamps whatever is
     * asked to what the hardware actually supports, and the clamped value is
     * logged, so a request the codec refuses is visible rather than silent.
     */
    var videoBitrateMbps: Int
        get() = prefs.getInt(KEY_BITRATE_MBPS, 8)
        set(value) = prefs.edit().putInt(KEY_BITRATE_MBPS, value.coerceIn(4, 24)).apply()

    /**
     * Render scale, as a percentage of the panel's native size.
     *
     * Below 100% the phone encodes a smaller picture and the car upscales it:
     * cheaper on the phone and on the link, softer on screen. Above 100% the
     * opposite. 30-160% matches what the reference UI offers; the encoder is
     * asked for the scaled size, so this is a real resolution change rather
     * than a display filter.
     */
    var displayScalePercent: Int
        get() = prefs.getInt(KEY_DISPLAY_SCALE, 100)
        set(value) = prefs.edit().putInt(KEY_DISPLAY_SCALE, value.coerceIn(30, 160)).apply()

    /**
     * Minimum gap between touch move events sent to the car, in milliseconds.
     *
     * 0 forwards every move. A small gap keeps a drag from flooding the input
     * path on phones whose touch panel reports faster than the link can carry;
     * down and up events are never throttled, only moves.
     */
    var touchMoveThrottleMs: Int
        get() = prefs.getInt(KEY_TOUCH_THROTTLE, 0)
        set(value) = prefs.edit().putInt(KEY_TOUCH_THROTTLE, value.coerceIn(0, 48)).apply()

    /**
     * Forward CarLife's H.264 untouched instead of compositing and re-encoding.
     *
     * Off by default: the compositor is what paints wallpaper outside the safe
     * area, and that is the picture this app is built around. On, the pipeline
     * skips the decode/composite/encode stage entirely and CarLife is asked to
     * encode the full panel instead of the safe rectangle — the shape an established sender
     * uses on the same Honor CarLife build.
     *
     * Kept as a setting rather than a build-time choice because it doubles as a
     * measurement: it removes the whole encoder stage from the path, so if a
     * session-level problem persists with it on, that stage is not the cause.
     */
    var videoPassthrough: Boolean
        get() = prefs.getBoolean(KEY_VIDEO_PASSTHROUGH, false)
        set(value) = prefs.edit().putBoolean(KEY_VIDEO_PASSTHROUGH, value).apply()

    fun safeAreaPolicy(endpoint: String? = peerAddress): CarPanelGeometry.SafeAreaPolicy {
        val scope = safeAreaScope(endpoint)
        val mode = when (prefs.getString("$scope:mode", null)) {
            "RELAXED" -> CarPanelGeometry.SafeAreaMode.RELAXED
            "CUSTOM" -> CarPanelGeometry.SafeAreaMode.CUSTOM
            "OFF" -> CarPanelGeometry.SafeAreaMode.OFF
            else -> CarPanelGeometry.SafeAreaMode.STRICT
        }
        return CarPanelGeometry.SafeAreaPolicy(
            // CarLife 8.8.x reports a 1972x520 opaque coded frame. The
            // compositor must preserve that frame so the side rail and bottom
            // controls are not cropped; safe viewport placement applies only
            // when the source itself is safe-area sized.
            enabled = prefs.getBoolean(KEY_SAFE_AREA_ENABLED, true),
            mode = mode,
            left = prefs.getInt("$scope:left", 0),
            top = prefs.getInt("$scope:top", 0),
            right = prefs.getInt("$scope:right", 0),
            bottom = prefs.getInt("$scope:bottom", 0),
        )
    }

    fun saveSafeAreaPolicy(
        endpoint: String? = peerAddress,
        policy: CarPanelGeometry.SafeAreaPolicy,
    ) {
        val scope = safeAreaScope(endpoint)
        prefs.edit()
            .putBoolean(KEY_SAFE_AREA_ENABLED, policy.enabled)
            .putString("$scope:mode", policy.mode.name)
            .putInt("$scope:left", policy.left.coerceAtLeast(0))
            .putInt("$scope:top", policy.top.coerceAtLeast(0))
            .putInt("$scope:right", policy.right.coerceAtLeast(0))
            .putInt("$scope:bottom", policy.bottom.coerceAtLeast(0))
            .apply()
    }

    /** A user-supplied address, used only when the system withholds the real one. */
    var manualBluetoothAddress: String?
        get() = prefs.getString(KEY_BT, null)
        set(value) = prefs.edit().putString(KEY_BT, value?.trim()?.uppercase()).apply()

    fun accessoryKey(endpoint: String): ByteArray? =
        prefs.getString("accessoryKey:$endpoint", null)?.let(::unseal)

    fun saveAccessoryKey(endpoint: String, key: ByteArray) {
        check(prefs.edit().putString("accessoryKey:$endpoint", seal(key)).commit())
    }

    fun removeAccessoryKey(endpoint: String) {
        prefs.edit().remove("accessoryKey:$endpoint").apply()
    }

    fun paired(endpoint: String): PairedAccessory? = prefs.getString("pair:$endpoint", null)?.let {
        val json = JSONObject(String(unseal(it)))
        PairedAccessory(json.getString("id"), decode(json.getString("public")))
    }

    fun savePair(endpoint: String, peer: PairedAccessory) {
        val json = JSONObject().put("id", peer.id).put("public", encode(peer.publicKey))
        check(prefs.edit().putString("pair:$endpoint", seal(json.toString().toByteArray())).commit())
    }

    fun removePair(endpoint: String) {
        prefs.edit().remove("pair:$endpoint").apply()
    }

    fun hasPair(endpoint: String): Boolean = prefs.contains("pair:$endpoint")

    /**
     * True once a session with this car actually reached the screen.
     *
     * This is what separates the two cases the driver meets. The very first
     * connection to a car cannot be automated: Honor's own Car Connect flow is
     * what grants CarLife its wireless pairing, and those activities are
     * exported=false, so the app can only take the driver to that page. Every
     * later connection can skip it, and that is only safe to decide from a
     * recorded success rather than from "a device is paired" — a car that was
     * paired and never worked would otherwise be driven straight into a failing
     * automatic attempt.
     */
    fun hasConnected(endpoint: String): Boolean =
        prefs.getBoolean("connected:$endpoint", false) &&
            prefs.getInt("connectedProofVersion:$endpoint", 0) == CONNECTED_PROOF_VERSION

    fun markConnected(endpoint: String) {
        prefs.edit()
            .putBoolean("connected:$endpoint", true)
            .putInt("connectedProofVersion:$endpoint", CONNECTED_PROOF_VERSION)
            .apply()
    }

    fun clearConnected(endpoint: String) {
        prefs.edit()
            .remove("connected:$endpoint")
            .remove("connectedProofVersion:$endpoint")
            .apply()
    }

    /** A stable Ed25519 identity, generated once and kept sealed. */
    fun identity(): ControllerIdentity = synchronized(LOCK) {
        val stored = prefs.getString(KEY_IDENTITY, null)
        if (stored != null) {
            val json = JSONObject(String(unseal(stored)))
            ControllerIdentity(decode(json.getString("private")), decode(json.getString("public")), json.getString("id"))
        } else {
            val pair = AirPlayCrypto.ed25519Generate()
            val identity = ControllerIdentity(pair.privateKey, pair.publicKey, UUID.randomUUID().toString())
            val json = JSONObject().put("private", encode(pair.privateKey))
                .put("public", encode(pair.publicKey)).put("id", identity.id)
            check(prefs.edit().putString(KEY_IDENTITY, seal(json.toString().toByteArray())).commit())
            identity
        }
    }

    private fun secret(): SecretKey = synchronized(LOCK) {
        val store = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        if (!store.containsAlias(KEY_STORE)) {
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore").apply {
                init(
                    KeyGenParameterSpec.Builder(KEY_STORE, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setKeySize(256).build()
                )
            }.generateKey()
        }
        store.getKey(KEY_STORE, null) as SecretKey
    }

    private fun seal(bytes: ByteArray): String = Cipher.getInstance("AES/GCM/NoPadding").run {
        init(Cipher.ENCRYPT_MODE, secret())
        encode(iv + doFinal(bytes))
    }

    private fun unseal(value: String): ByteArray = Cipher.getInstance("AES/GCM/NoPadding").run {
        val bytes = decode(value)
        require(bytes.size >= 28)
        init(Cipher.DECRYPT_MODE, secret(), GCMParameterSpec(128, bytes.copyOfRange(0, 12)))
        doFinal(bytes.copyOfRange(12, bytes.size))
    }

    private fun encode(bytes: ByteArray) = Base64.encodeToString(bytes, Base64.NO_WRAP)
    private fun decode(value: String) = Base64.decode(value, Base64.NO_WRAP)

    private fun safeAreaScope(endpoint: String?): String =
        "safeArea:" + endpoint.orEmpty().ifBlank { "default" }

    companion object {
        internal fun defaultAudioEnabled(sourceMode: SourceMode): Boolean =
            sourceMode != SourceMode.MIRROR

        const val KEY_PEER = "peer"
        const val KEY_PEER_NAME = "peerName"
        const val KEY_SOURCE = "sourceMode"
        const val KEY_AUDIO = "audio"
        const val KEY_TOUCH = "touch"
        const val KEY_AUTO_CONNECT = "autoConnect"
        const val KEY_CARLIFE_AUTO_SESSION = "carLifeAutoSession"
        const val KEY_FRAME_RATE = "frameRateOverride"
    const val KEY_DISPLAY_SCALE = "displayScalePercent"
    const val KEY_BITRATE_MBPS = "videoBitrateMbps"
    const val KEY_TOUCH_THROTTLE = "touchMoveThrottleMs"
        const val KEY_SAFE_AREA_ENABLED = "safeAreaEnabled"
        const val KEY_VIDEO_PASSTHROUGH = "videoPassthrough"
        const val KEY_BT = "manualBluetoothAddress"
        const val KEY_IDENTITY = "identity"
        const val KEY_STORE = "carlink-storage"
        /**
         * A previous build marked a peer connected after only one video frame.
         * Bump this whenever the proof semantics change so that stale markers
         * cannot re-enable automatic retries after an update.
         */
        private const val CONNECTED_PROOF_VERSION = 2
        val LOCK = Any()
    }
}
