package com.carlink.cast

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.carplay.iap2.controller.PhoneBootstrap
import com.carplay.iap2.controller.PhoneBootstrapProfile
import com.carplay.iap2.controller.WirelessEndpoint
import com.carplay.iap2.session.Iap2Session
import com.carplay.iap2.transport.BluetoothRfcommDuplexStream
import com.carplay.iap2.transport.BlockingDuplexByteStream
import com.carplay.iap2.transport.Iap2PeerDetectionRestart
import com.carplay.iap2.transport.PhoneBluetoothProfile
import com.carplay.iap2.transport.PhoneBluetoothSessionTransport
import com.carplay.iap2.transport.RfcommConnectAttempt
import com.carplay.iap2.transport.RfcommConnectionDirection
import com.carplay.iap2.transport.RfcommConnectionStrategy
import com.carplay.iap2.transport.RegisteredRfcommService
import com.carplay.sender.AccessoryAuthentication
import com.carplay.sender.BondedAccessoryTrust
import java.io.IOException
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * The Bluetooth half of the wireless handshake, written from the observed
 * iPhone session rather than copied from the old app.
 *
 * Two facts drive the shape of this class:
 *  - the car discovers the phone over SDP and opens iAP2 itself, so the phone
 *    registers a service with the CarPlay UUID and only then dials the car's
 *    service UUID (both directions are tried, outbound first);
 *  - the link is a long-lived control channel. A captured iPhone keeps iAP2
 *    traffic flowing long after Wi-Fi takes over and never tears the link down,
 *    so this object deliberately has no "retire Bluetooth" step. The head unit
 *    resets the entire session, video included, when the bootstrap disappears.
 */
// Permissions are checked in requirePermissions() before any platform call
// that needs them; the annotation documents that the checks are centralised.
@android.annotation.SuppressLint("MissingPermission")
internal class BluetoothBootstrap(
    private val context: Context,
    private val peerAddress: String,
    private val identityId: String,
    private val transportIdentifier: String,
    private val trust: (authenticatedBluetooth: Boolean) -> AccessoryAuthentication,
    private val log: (String) -> Unit,
    /**
     * Starts the Now Playing subscription on this link.
     *
     * This transport, not the Wi-Fi command channel, is where the head unit
     * actually asks for media. Its own identification names `0x5000` in the
     * messages it sends and `0x5001` in the ones it receives, and the
     * identification/authentication exchange completes here every session.
     *
     * The command channel was tried first and never reached READY on this
     * firmware, so the media subscription scheduled behind it never ran — the
     * car showed a media card with nothing in it for the whole session.
     */
    private val startNowPlaying: ((send: (com.carplay.iap2.wire.Iap2Frame) -> Unit,
                                   sendFile: (ByteArray) -> Boolean) -> Unit)? = null,
) : AutoCloseable {

    data class Result(val endpoint: WirelessEndpoint, val accessoryIdentity: ByteArray)

    private val bluetooth: BluetoothAdapter = context.getSystemService(BluetoothManager::class.java)?.adapter
        ?: error("手机不支持蓝牙")
    private val peer: BluetoothDevice = bluetooth.getRemoteDevice(peerAddress)
    private val closed = AtomicBoolean()
    /** Set once the Wi-Fi side has taken over and this iAP2 session was retired. */
    private val retired = AtomicBoolean()
    private val owned = mutableListOf<AutoCloseable>()
    // The GM head unit sends the iAP2 marker and its own SYN; the phone answers
    // with an exact SYN/ACK echo. This is the sequence CarWith completed in the
    // field, and the one this head unit confirms.
    private val profile = PhoneBluetoothProfile.ACCESSORY_FIRST

    @Volatile private var session: Iap2Session? = null
    @Volatile private var bootstrap: PhoneBootstrap? = null
    @Volatile private var socket: BluetoothSocket? = null
    @Volatile private var receiver: Thread? = null
    @Volatile private var linkTransport: PhoneBluetoothSessionTransport? = null

    /** Set once, so a status callback that repeats cannot subscribe twice. */
    @Volatile private var nowPlayingStarted = false
    @Volatile private var authentication: AccessoryAuthentication? = null

    private var phoneService: RegisteredRfcommService<BluetoothServerSocket>? = null

    /** Set once 0x4301 has been received; the link itself stays open. */
    private val endpoint = CompletableFuture<WirelessEndpoint>()

    /**
     * Completes with the bytes that followed the car's detection marker when it
     * restarts iAP2. The E2LB head unit accepts 0x4300, closes that first round
     * and starts a second one on the same RFCOMM before it sends 0x4301.
     */
    @Volatile private var restart = CompletableFuture<ByteArray>()
    @Volatile private var restartUsed = false

    /** True while the iAP2 channel is alive and authenticated; used by the sender. */
    @Volatile private var verifiedIdentity: ByteArray? = null

    fun authenticated(): Boolean = bootstrap?.status()?.authenticated == true && !closed.get() &&
        session?.isClosed == false

    fun identityFingerprint(): ByteArray? = verifiedIdentity?.copyOf()

    /** Connects, authenticates, publishes 0x4300 and waits for 0x4301. */
    fun connect(timeoutMillis: Long = 45_000): Result {
        requirePermissions()
        check(bluetooth.isEnabled) { "请打开蓝牙" }
        /*
         * Print the pairing state before refusing.
         *
         * After unpairing on both sides this is the first thing that fails, and
         * "请先在系统蓝牙设置中与车机完成配对" alone does not say whether the
         * phone has no bond, the car rejected the bond, or the address is a
         * stale one the car no longer knows.
         *
         * The bond-state constants are the hidden `BluetoothDevice.BOND_*`
         * values, not 0..3 — an earlier version of this comment said 0/1/2/3
         * and that is simply wrong: 10 is none, 11 is bonding, 12 is bonded.
         * Printing the device type matters too, because iAP2 runs over classic
         * RFCOMM and a dual-mode device paired over LE reports type 3 as well —
         * the link parameters differ sharply between the two.
         */
        log(
            "配对状态检查：${peer.name ?: peer.address} bondState=${peer.bondState}" +
                "（10=无 11=配对中 12=已配对）" +
                " 地址=${peer.address} 类型=${peer.type}" +
                "（1=经典 2=LE 3=双模）",
        )
        check(peer.bondState == BluetoothDevice.BOND_BONDED) {
            "请先在系统蓝牙设置中与车机完成配对（当前 bondState=${peer.bondState}）"
        }
        bluetooth.cancelDiscovery()
        log("蓝牙链路：连接 ${peer.name ?: peerAddress}")
        val link = negotiate()
        socket = link.socket
        session = link.session
        linkTransport = link.transport
        // One trust decision for the whole connection: the second iAP2 round
        // reuses it instead of asking the car to prove itself again.
        authentication = trust(socket?.isConnected == true && peer.bondState == BluetoothDevice.BOND_BONDED)
        startEpoch(link.session, null)
        val deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMillis)
        while (!endpoint.isDone && !closed.get() && System.nanoTime() < deadline) {
            val slice = minOf(250L, ((deadline - System.nanoTime()) / 1_000_000).coerceAtLeast(1))
            val restarted = runCatching { restart.get(slice, TimeUnit.MILLISECONDS) }.getOrNull()
                ?: continue
            if (restartUsed) {
                log("车机再次重新检测；本次连接只处理一次重启")
                break
            }
            restartUsed = true
            val expected = bootstrap?.accessoryIdentityFingerprint()
            log("车机接受了无线声明，正在同一 RFCOMM 上开始第二轮 iAP2")
            // The head unit closed the first iAP2 round on purpose; a fresh
            // protocol instance must re-identify and re-authenticate before it
            // will hand out the Wi-Fi configuration.
            runCatching { receiver?.interrupt() }
            runCatching { session?.close() }
            runCatching { receiver?.join(1000) }
            val stream = openRestartStream(link.transport, restarted)
            val next = Iap2Session.phoneBluetooth(stream, profile) { log("iAP2：$it") }
            owned += next
            // The new instance starts in detection mode and must answer the
            // car's marker and SYN before any CSM may be sent; sending 0x1D00
            // early is rejected by the channel itself.
            val remaining = ((deadline - System.nanoTime()) / 1_000_000).coerceAtLeast(1)
            check(next.awaitPhoneBluetoothReady(profile, remaining)) {
                "第二轮 iAP2 同步未完成"
            }
            startEpoch(next, expected)
        }
        check(endpoint.isDone) { "车机未下发无线配置" }
        val endpoint = endpoint.getNow(null) ?: error("车机未下发无线配置")
        val fingerprint = bootstrap?.accessoryIdentityFingerprint()
            ?: error("车机未提供识别信息")
        verifiedIdentity = fingerprint
        return Result(endpoint, fingerprint)
    }

    /**
     * Opens the next protocol lease once the old one has finished its last read.
     * The transport refuses a new lease while the old instance is still reading,
     * so the join above is followed by a short bounded retry.
     */
    private fun openRestartStream(
        transport: PhoneBluetoothSessionTransport,
        initial: ByteArray,
    ): BlockingDuplexByteStream {
        var last: Throwable? = null
        repeat(20) {
            try {
                return transport.open(initial)
            } catch (failure: Exception) {
                last = failure
                Thread.sleep(50)
            }
        }
        throw IllegalStateException("旧 iAP2 实例未释放，无法开始第二轮", last)
    }

    private fun startEpoch(current: Iap2Session, expected: ByteArray?) {
        val boot = newBootstrap(current, checkNotNull(authentication), expected)
        bootstrap = boot
        session = current
        restart = CompletableFuture()
        startReceiver(current, boot)
        boot.begin()
    }

    private fun requirePermissions() {
        if (Build.VERSION.SDK_INT < 31) return
        check(context.checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            "蓝牙连接权限已撤销"
        }
        check(context.checkSelfPermission(android.Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED) {
            "蓝牙发现权限已撤销"
        }
    }

    private fun newBootstrap(
        current: Iap2Session,
        access: AccessoryAuthentication,
        expected: ByteArray?,
    ) = PhoneBootstrap(
        identifier = identityId,
        validateCertificates = access::validate,
        verifyChallenge = access::verifyAuthentication,
        send = { frame ->
            // Mirror the inbound wire log. A session that will not come up is
            // almost always "we sent X and the car answered nothing", and that
            // is only readable if both directions are on the record.
            log(
                "蓝牙发 → id=0x${frame.messageId.toString(16)} " +
                    "body=${frame.payload.size}B" +
                    if (frame.payload.isNotEmpty()) {
                        " hex=${frame.payload.take(16).joinToString(" ") { "%02X".format(it) }}"
                    } else "",
            )
            current.send(frame)
        },
        onStage = log,
        onEndpoint = { endpoint.complete(it) },
        onOther = { frame ->
            // Steering-wheel and panel buttons arrive as iAP2 HID messages on
            // this channel; log the raw body so the button map can be built
            // from real presses before any translation exists.
            if (frame.messageId in setOf(0x6800, 0x6802, 0x6803)) {
                val body = frame.payload
                log("车机按键消息 0x${frame.messageId.toString(16)} 长度=${body.size} " +
                    "hex=${body.joinToString(" ") { "%02X".format(it) }}")
            } else {
                log("车机消息 0x${frame.messageId.toString(16)}")
            }
            // The head unit subscribes to phone status right after
            // authentication and keeps those subscriptions for the session.
            subscriptionReply(frame.messageId)?.let { reply ->
                runCatching { current.send(reply) }
                    .onFailure { log("状态订阅回复失败：${it.message}") }
            }
        },
        onStatus = { status ->
            when {
                status.controlReady -> Unit
                status.authenticated && !status.endpointReceived -> log("车机身份已验证，等待无线配置")
            }
            /*
             * Media is subscribed once identification is done and *before* the
             * wireless configuration arrives.
             *
             * `identified` is the flag that matters: 0x5000 is a control-session
             * message, and the car rejects one that arrives before it has
             * identified the accessory. Waiting for the endpoint would be too
             * late in the other direction — the endpoint is delivered last, so
             * a receiver that missed the subscription shows an empty media card
             * for the rest of the session.
             */
            if (status.identified && !nowPlayingStarted) {
                val current = session
                if (current != null) {
                    nowPlayingStarted = true
                    runCatching {
                        startNowPlaying?.invoke(current::send, current::sendFileTransfer)
                    }.onFailure { log("订阅车机媒体信息失败：${it.message}") }
                }
            }
        },
        profile = PhoneBootstrapProfile.MODERN_WIRELESS,
        transportIdentifier = { transportIdentifier },
        expectedAccessoryIdentity = expected,
    )

    /**
     * Minimal well-formed replies to 0x5000/0x5200/0xae00/0x4157/0x4154.
     *
     * The car does not require rich content, only that the phone acknowledges
     * each subscription; an unanswered request leaves the head unit waiting and
     * has been observed to end the session shortly after the video starts.
     */
    private fun subscriptionReply(messageId: Int): com.carplay.iap2.wire.Iap2Frame? = when (messageId) {
        0x5000 -> com.carplay.iap2.message.Iap2Messages.buildRaw(0x5001) {
            group(0) { void(0) }
        }
        0x5200 -> com.carplay.iap2.message.Iap2Messages.buildRaw(0x5201) {
            u16(0, 0)
            u8(1, 0)
            u8(2, 0)
        }
        0xae00 -> com.carplay.iap2.message.Iap2Messages.buildRaw(0xae01) { u8(0, 0) }
        0x4157 -> com.carplay.iap2.message.Iap2Messages.buildRaw(0x4158) { u8(0, 0) }
        0x4154 -> com.carplay.iap2.message.Iap2Messages.buildRaw(0x4155) { u8(0, 0) }
        else -> null
    }

    private fun startReceiver(current: Iap2Session, boot: PhoneBootstrap) {
        receiver = Thread({
            /*
             * Idle watchdog on the Bluetooth link.
             *
             * The 对照实现 sender treats 20 s without any inbound iAP2 frame
             * as a dead control channel and rebuilds it:
             *
             *     "蓝牙已连接，但 N 秒未收到车机 iAP2 应答，正在重建控制通道"
             *     (`C0237ic`: `this.b = 20000L`)
             *
             * This link had no such check: a car that stopped answering simply
             * left the receiver thread blocked in recv() forever, and the
             * session looked alive while nothing could be sent on it. The
             * counters below make the same judgement with the same budget, and
             * log the last frame seen so a field capture shows what the link
             * was doing when it went quiet.
             */
            val idleLimitNanos = IDLE_REBUILD_AFTER_MS * 1_000_000L
            var lastInboundNanos = System.nanoTime()
            var inboundFrames = 0L
            var rebuiltForIdle = 0
            try {
                while (!closed.get() && !current.isClosed) {
                    val frame = current.recv(1000)
                    if (frame == null) {
                        val idleMs = (System.nanoTime() - lastInboundNanos) / 1_000_000L
                        if (idleMs >= IDLE_REBUILD_AFTER_MS) {
                            rebuiltForIdle++
                            log(
                                "蓝牙已连接，但 ${idleMs / 1000} 秒未收到车机 iAP2 应答，正在重建控制通道" +
                                    "（累计收到 $inboundFrames 帧，第 $rebuiltForIdle 次重建）",
                            )
                            // Hand the session back the way a peer-initiated
                            // restart does, so the caller rebuilds the link
                            // instead of leaving a half-dead socket open.
                            restart.complete(ByteArray(0))
                            return@Thread
                        }
                        continue
                    }
                    lastInboundNanos = System.nanoTime()
                    inboundFrames++
                    /*
                     * Log every inbound frame at the wire level.
                     *
                     * The high-level stages ("identified", "authenticated",
                     * "waiting for wireless configuration") are not enough to
                     * tell three very different situations apart:
                     *
                     *   - the car went completely silent
                     *   - the car sent something we failed to parse
                     *   - the car sent a retransmission of an earlier frame
                     *
                     * All three look identical in the stage log, and telling
                     * them apart is the whole question when a session will not
                     * come up. Sixteen bytes of hex is enough to recognise a
                     * message id and a control session id.
                     */
                    log(
                        "蓝牙收 ← id=0x${frame.messageId.toString(16)} " +
                            "body=${frame.payload.size}B 第 $inboundFrames 帧" +
                            if (frame.payload.isNotEmpty()) {
                                " hex=${frame.payload.take(16).joinToString(" ") { "%02X".format(it) }}"
                            } else "",
                    )
                    runCatching { boot.receive(frame) }
                        .onFailure { log("处理车机消息失败：${it.message}") }
                }
            } catch (restartSignal: Iap2PeerDetectionRestart) {
                // The head unit closed this round on purpose and expects a new
                // iAP2 instance; the bytes after its marker seed that instance.
                restart.complete(restartSignal.initialInput())
            } catch (_: Exception) {
                // The session's own close path reports the real reason.
            }
        }, "carlink-bluetooth").apply { isDaemon = true; start() }
    }

    /**
     * Bluetooth link idle budget, matching the 对照实现 sender's 20 s.
     *
     * That sender rebuilds its control channel after this long without an
     * inbound iAP2 frame (`C0237ic`: `this.b = 20000L`). Same number here so a
     * field capture compares like with like.
     */
    private val IDLE_REBUILD_AFTER_MS = 20_000L


    private data class Link(
        val socket: BluetoothSocket,
        val session: Iap2Session,
        val transport: PhoneBluetoothSessionTransport,
    )

    /**
     * Outbound first, then the phone's own listening service.
     *
     * The car normally accepts the phone's outbound connection to its iAP2
     * service, but a head unit that has just been told to connect may dial the
     * phone instead. Both sides register the same well-known UUID pair, so
     * either direction lands on the same protocol instance.
     */
    private fun negotiate(): Link {
        val profiles = profile.attempts()
        for ((index, candidate) in profiles.withIndex()) {
            check(!closed.get()) { "连接已停止" }
            log("蓝牙握手 ${index + 1}/${profiles.size}：${candidate.label}")
            var outbound = true
            val connected = RfcommConnectionStrategy.connectWithPhoneService(
                RfcommConnectionDirection.PHONE_INITIATED,
                { registerPhoneService() },
                { connectOutbound().also { outbound = true } },
                { listenInbound().also { outbound = false } },
                closed::get,
                { log("手机 SDP 注册失败：${it.javaClass.simpleName}；继续手机主动连接") },
                { log("主动连接未建立：${it.javaClass.simpleName}；改为等待车机连接") },
            )
            owned += connected
            val transport = PhoneBluetoothSessionTransport(BluetoothRfcommDuplexStream(connected) { })
            owned += transport
            val session = Iap2Session.phoneBluetooth(transport.open(), candidate) { log("iAP2：$it") }
            owned += session
            if (session.awaitPhoneBluetoothReady(candidate)) {
                log("iAP2 链路已同步（${if (outbound) "手机主动" else "车机回连"}）")
                /*
                 * The peer's link parameters decide whether the session can
                 * progress at all, so print them on their own line.
                 *
                 * `peerMaxLength` is the largest frame this car says it can
                 * receive, and the car computes it from the transport, not from
                 * anything we send. Two field sessions on the same hardware:
                 *
                 *     peerMaxLength=65535  -> the car answers 0x4300 with a
                 *                             second iAP2 round in ~3 s
                 *     peerMaxLength=2048   -> the car only sends empty link
                 *                             ACKs and goes silent for 18 s
                 *
                 * 0x4301 (the wireless configuration: SSID, passphrase, port,
                 * public key) is far larger than 2048 bytes, so a 2048-byte
                 * peer limit means the exchange that has to follow cannot
                 * happen at all. That is worth seeing at a glance rather than
                 * buried in the LINK trace.
                 */
                session.handshakeStatus().peerSynchronization?.let { sync ->
                    log(
                        "车机链路参数：peerMaxLength=${sync.maxLength} " +
                            "peerWindow=${sync.maxOutgoing} peerRto=${sync.retransmissionTimeoutMillis}ms" +
                            if (sync.maxLength < CARPLAY_MIN_USEFUL_MAX_LENGTH) {
                                " ⚠ 小于 $CARPLAY_MIN_USEFUL_MAX_LENGTH，" +
                                    "0x4301 无线配置无法传输（多半是配对走了 LE 或链路退化，请重新配对）"
                            } else "",
                    )
                }
                return Link(connected, session, transport)
            }
            log("iAP2 同步未完成，换下一个握手方案")
            runCatching { session.close() }
            runCatching { transport.close() }
            runCatching { connected.close() }
        }
        error("蓝牙 iAP2 同步失败：请确认车机屏幕上已选择本机")
    }

    private fun connectOutbound(): BluetoothSocket {
        val outbound = peer.createRfcommSocketToServiceRecord(ACCESSORY_UUID)
        val attempt = RfcommConnectAttempt { outbound.close() }
        val scheduler = Executors.newSingleThreadScheduledExecutor { task ->
            Thread(task, "carlink-rfcomm-timeout").apply { isDaemon = true }
        }
        try {
            attempt.connect(outbound::connect) { timeout ->
                val timer = scheduler.schedule({ timeout() }, 20_000, TimeUnit.MILLISECONDS)
                AutoCloseable { timer.cancel(false) }
            }
            check(outbound.isConnected) { "主动 RFCOMM 返回后未建立连接" }
            return outbound
        } finally {
            scheduler.shutdownNow()
        }
    }

    private fun registerPhoneService(): BluetoothServerSocket {
        val registration = phoneService ?: RegisteredRfcommService {
            bluetooth.listenUsingRfcommWithServiceRecord("iAP2", PHONE_UUID)
        }.also { phoneService = it }
        return registration.acquire()
    }

    private fun listenInbound(): BluetoothSocket {
        val listener = registerPhoneService()
        log("等待车机回连本机 iAP2 服务，最多 45 秒；请在车机蓝牙列表中选择本机")
        val deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(45)
        while (!closed.get() && System.nanoTime() < deadline) {
            try {
                val accepted = listener.accept()
                if (accepted.remoteDevice.address.equals(peerAddress, true) &&
                    accepted.remoteDevice.bondState == BluetoothDevice.BOND_BONDED
                ) {
                    log("车机已回连本机")
                    return accepted
                }
                accepted.close()
            } catch (_: IOException) {
                if (closed.get()) error("连接已停止")
            }
        }
        error("45 秒内未收到车机的蓝牙回连；请在车机屏幕上选择本机")
    }

    /** Reads the link without consuming protocol frames; used only for diagnostics. */
    fun isAlive(): Boolean = !closed.get() && socket?.isConnected == true && session?.isClosed == false

    /**
     * Retires the iAP2 control session after the Wi-Fi side has taken over.
     *
     * This is the second half of the handoff: by the time it runs, the car has
     * already been told over the Wi-Fi command channel to stop using this link.
     * Only the RFCOMM/iAP2 session goes away — the Android Bluetooth pairing,
     * and with it A2DP and HFP, stays exactly as it was, so phone calls and
     * media keep working. The reference sender's log draws the same line:
     *
     *     HANDOFF accepted; bootstrap RFCOMM retired; Android Bluetooth retained
     *
     * The receiver thread is interrupted before the socket is closed so it
     * cannot log a spurious "read failed" for a close we asked for.
     */
    fun retireAfterHandoff() {
        if (!retired.compareAndSet(false, true)) return
        log("蓝牙 iAP2 控制会话已退休（Android 蓝牙配对保留）")
        runCatching { receiver?.interrupt() }
        owned.reversed().forEach { runCatching { it.close() } }
        owned.clear()
        runCatching { phoneService?.close() }
        phoneService = null
    }

    override fun close() {
        if (!closed.compareAndSet(false, true)) return
        runCatching { receiver?.interrupt() }
        owned.reversed().forEach { runCatching { it.close() } }
        owned.clear()
        phoneService?.let { runCatching { it.close() } }
        phoneService = null
    }

    companion object {
        /** The phone's own CarPlay service; the car discovers this over SDP. */
        val PHONE_UUID: UUID = UUID.fromString("00000000-deca-fade-deca-deafdecacafe")

        /** The car's iAP2 service the phone dials. */
        val ACCESSORY_UUID: UUID = UUID.fromString("00000000-deca-fade-deca-deafdecacaff")

        /**
         * A peer `maxLength` below this cannot carry the wireless configuration.
         *
         * The 0x4301 message this session is waiting for holds an SSID, a
         * passphrase, a port and a 32-byte public key — comfortably past 2 KiB
         * even before iAP2 framing. Measured on this car: 65535 lets the
         * exchange proceed, 2048 leaves the car sending nothing but empty link
         * ACKs until the watchdog fires.
         */
        private const val CARPLAY_MIN_USEFUL_MAX_LENGTH = 8192
    }
}
