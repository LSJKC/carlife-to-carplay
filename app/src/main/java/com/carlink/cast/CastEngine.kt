package com.carlink.cast

import android.bluetooth.BluetoothManager
import android.content.Context
import com.carplay.adapter.PhoneProjectionAdapter
import com.carplay.adapter.carlife.CarLifeProjectionAdapter
import com.carplay.sender.AccessoryAuthentication
import com.carplay.sender.BondedAccessoryTrust
import java.io.Closeable
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.runBlocking

/**
 * One session, from the first Bluetooth packet to the last video frame.
 *
 * The sequence is fixed by what a real iPhone does on this head unit:
 *
 *   1. Bluetooth iAP2 identify + authenticate (the car validates the phone's
 *      CarPlay availability, not an MFi chip);
 *   2. 0x4300 announces wireless support with the phone's real Bluetooth MAC;
 *   3. 0x4301 delivers SSID/passphrase/addresses/port, which are used verbatim;
 *   4. the phone joins that hotspot and pins every socket to it;
 *   5. AirPlay pair-verify, type 130 control, /info, SETUP 110, RECORD;
 *   6. frames flow from CarLife's car-mode encoder or from screen mirroring.
 *
 * The Bluetooth link is kept for the whole session and is only closed when the
 * session ends. That is not an optimisation: the head unit treats the bootstrap
 * as a required resource and resets the whole session when it vanishes.
 */
internal class CastEngine(
    private val context: Context,
    private val settings: CastSettings,
    private val source: PhoneProjectionAdapter,
    private val log: (String) -> Unit,
) : Closeable {

    private val closed = AtomicBoolean()
    private val finished = AtomicBoolean()
    @Volatile private var bluetooth: BluetoothBootstrap? = null
    @Volatile private var vehicle: VehicleNetwork? = null
    @Volatile private var airplay: AirPlayLink? = null
    private val hid = Iap2HidDispatcher(source, CastLog.logger(LogChannel.INPUT))

    fun run(onFailure: (Throwable) -> Unit) {
        try {
            val peer = settings.peerAddress ?: error("请先选择已配对的车机")
            // Applied before any input can arrive, so the first drag is already
            // using the configured gap.
            Dispatch.touchMoveThrottleMs = settings.touchMoveThrottleMs
            // A key bound in an earlier session may be a device id rather than
            // an AirPlay public key; such a value can never verify and would
            // abort every connection, so it is dropped once at startup.
            settings.accessoryKey(peer)?.let { key ->
                if (!isPublicKeyEncoding(key)) {
                    settings.removeAccessoryKey(peer)
                    CastLog.event("INFO", "已丢弃无效的车机公钥绑定")
                }
            }
            val adapter = context.getSystemService(BluetoothManager::class.java)?.adapter
                ?: error("手机不支持蓝牙")
            check(adapter.isEnabled) { "请打开蓝牙" }
            val address = LocalBluetoothAddress.read(context, adapter, settings.manualBluetoothAddress)
                ?: error("系统未提供本机蓝牙地址，请在设置中手动填写")
            val identity = settings.identity()
            CastLog.event("INFO", "本机蓝牙地址 $address；画面来源 ${source.name}")

            // Bring the vendor host up before the Bluetooth handshake. CarLife
            // only opens its seven listening ports once its process exists, and
            // the handshake below takes long enough that starting it afterwards
            // leaves the ports refused for the whole first attempt.
            runCatching { source.warmUp(context) }
                .onFailure { CastLog.event("WARN", "画面来源预热失败：${it.message}") }

            CastLog.stage(CastStage.BLUETOOTH, "与车机建立 iAP2 链路")
            /*
             * One media publisher for both links.
             *
             * The Bluetooth exchange is the one that reaches identification on
             * this firmware, so that is where the subscription has to start;
             * the Wi-Fi command channel keeps its own fallback so a future
             * firmware that does answer there still works.
             */
            val nowPlaying = NowPlayingPublisher(context, CastLog.logger(LogChannel.MEDIA))
            val link = BluetoothBootstrap(
                context = context,
                peerAddress = peer,
                identityId = identity.id,
                // The captured iPhone declares its real adapter address here;
                // the head unit matches it against the RFCOMM peer, so the
                // persistent AirPlay UUID is not a substitute.
                transportIdentifier = address,
                trust = { authenticated -> trustFor(peer, authenticated) },
                log = CastLog.logger(LogChannel.BT),
                startNowPlaying = { send, sendFile ->
                    val token = nowPlaying.subscribeDefault(send, sendFile)
                    log("已在蓝牙链路订阅车机媒体信息：token=$token")
                },
            )
            bluetooth = link
            val result = link.connect()
            check(!closed.get()) { "连接已停止" }
            CastLog.stage(CastStage.NETWORK, "车机已下发无线配置，加入 ${result.endpoint.ssid}")

            val network = VehicleNetwork(context, result.endpoint, CastLog.logger(LogChannel.NETWORK))
            vehicle = network
            network.observeDefaultNetwork()
            network.join()
            check(!closed.get()) { "连接已停止" }

            CastLog.stage(CastStage.DISCOVER, "确认车机 AirPlay 端点")
            val channel = AirPlayLink(
                context = context,
                identity = identity,
                vehicle = network,
                source = source,
                settings = settings,
                // A source that cannot produce audio must not negotiate an
                // audio stream the car will wait on.
                enableAudio = settings.audio && source.capabilities.audio,
                endpoint = result.endpoint,
                peerAddress = peer,
                trustFor = { authenticated -> trustFor(peer, authenticated) },
                hid = hid,
                bluetoothAlive = { link.isAlive() },
                bluetoothIdentity = { link.identityFingerprint() },
                // Retired only after the Wi-Fi side is carrying the session;
                // the Android Bluetooth pairing is untouched, so calls and
                // media over A2DP/HFP keep working.
                onBluetoothRetire = { link.retireAfterHandoff() },
                log = CastLog.logger(LogChannel.SESSION),
                logChannel = { channel, message -> CastLog.event(channel, "INFO", message) },
                nowPlayingPublisher = nowPlaying,
            )
            airplay = channel
            channel.start(
                onFailure = { failure -> onFailure(failure) },
                onFinished = { finished.set(true) },
            )
        } catch (failure: Throwable) {
            finished.set(true)
            if (!closed.get()) onFailure(failure)
        }
    }

    /** True once the session has ended for any reason. */
    fun hasFinished(): Boolean = finished.get()

    /** True when the bytes are an X.509 SubjectPublicKeyInfo for EC or RSA. */
    private fun isPublicKeyEncoding(key: ByteArray): Boolean {
        val spec = java.security.spec.X509EncodedKeySpec(key)
        return runCatching { java.security.KeyFactory.getInstance("EC").generatePublic(spec) }.isSuccess ||
            runCatching { java.security.KeyFactory.getInstance("RSA").generatePublic(spec) }.isSuccess
    }

    private fun trustFor(peer: String, authenticatedBluetooth: Boolean): AccessoryAuthentication =
        BondedAccessoryTrust(settings.accessoryKey(peer), authenticatedBluetooth) { key ->
            settings.saveAccessoryKey(peer, key)
            CastLog.event("INFO", "已绑定车机认证公钥")
        }

    /**
     * Closes the Bluetooth link only, as fast as the caller's thread allows.
     *
     * The service calls this the moment the driver cancels, on whatever thread
     * the request arrived, before the rest of the teardown is queued. The full
     * [close] can block on AirPlay worker joins and `stopProjection()`, and the
     * car has no way to observe any of that — what it reacts to is the RFCOMM
     * socket disappearing.
     *
     * Idempotent: closing an already-closed link is a no-op.
     */
    fun disconnectBluetoothNow() {
        runCatching { bluetooth?.close() }
        bluetooth = null
    }

    override fun close() {
        if (!closed.compareAndSet(false, true)) return
        /*
         * Bluetooth goes first, and it is not a detail.
         *
         * Everything below this line can block: closing AirPlay joins workers,
         * releasing the source runs `stopProjection()` inside `runBlocking`, and
         * the Wi-Fi lease teardown waits on the system. The car has no way to
         * observe any of that — the one thing it reacts to immediately is the
         * RFCOMM socket going away. Closing it last meant the head unit sat on
         * a live-looking session for as long as this call took:
         *
         *     18766ms  car reset the event channel
         *     18964ms  we were still snapshotting and handing back resources
         *     19158ms  Bluetooth finally closed          (392 ms after the car)
         *     21446ms  the car reconnected and started again by itself
         *
         * Closing it first makes the cancel visible to the car in the same
         * breath as the button press. The earlier ordering reasoned that a car
         * which loses Bluetooth mid-teardown would reset the session before the
         * Wi-Fi side finished — but at this point the session is already being
         * torn down, and a reset is exactly what the driver asked for.
         */
        runCatching { bluetooth?.close() }
        bluetooth = null
        runCatching { airplay?.close() }
        airplay = null
        // CarLife may have been warmed up before AirPlay was created, or an
        // AirPlay worker may have been interrupted before its own close path
        // reached the source.  Always release the source at this outermost
        // boundary as well; the adapters are designed to make this idempotent.
        runCatching { runBlocking { source.stopProjection() } }
            .onFailure { CastLog.event("WARN", "释放画面来源失败：${it.message}") }
        runCatching { vehicle?.close() }
        vehicle = null
        hid.reset()
        Dispatch.reset()
    }

    companion object {
        /** CarLife is the preferred source on phones that ship it. */
        fun carLifeAvailable(context: Context): Boolean =
            runCatching { CarLifeProjectionAdapter().isSupported(context) }.getOrDefault(false)
    }
}
