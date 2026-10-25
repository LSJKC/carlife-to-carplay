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
    private val hid: Iap2HidDispatcher,
    private val log: (String) -> Unit,
) : AutoCloseable {

    data class Result(val endpoint: WirelessEndpoint, val accessoryIdentity: ByteArray)

    private val bluetooth: BluetoothAdapter = context.getSystemService(BluetoothManager::class.java)?.adapter
        ?: error("手机不支持蓝牙")
    private val peer: BluetoothDevice = bluetooth.getRemoteDevice(peerAddress)
    private val closed = AtomicBoolean()
    private val nowPlaying = NowPlayingPublisher(context, log)
    @Volatile private var nowPlayingSubscription = 0L
    private val owned = mutableListOf<AutoCloseable>()
    private val profile = PhoneBluetoothProfile.ACCESSORY_FIRST

    @Volatile private var session: Iap2Session? = null
    @Volatile private var bootstrap: PhoneBootstrap? = null
    @Volatile private var socket: BluetoothSocket? = null
    @Volatile private var receiver: Thread? = null
    @Volatile private var linkTransport: PhoneBluetoothSessionTransport? = null
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
        check(peer.bondState == BluetoothDevice.BOND_BONDED) { "请先在系统蓝牙设置中与车机完成配对" }
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
            val next = Iap2Session.phoneBluetooth(
                stream,
                profile,
                onTrace = {},
                onFileTransferDatagram = { datagram -> nowPlaying.onFileTransferDatagram(datagram) },
            )
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
        send = current::send,
        onStage = log,
        onEndpoint = { endpoint.complete(it) },
        onOther = { frame ->
            if (frame.messageId in setOf(0x6800, 0x6802, 0x6803)) {
                val body = frame.payload
                log("车机按键消息 0x${frame.messageId.toString(16)} 长度=${body.size} " +
                    "hex=${body.joinToString(" ") { "%02X".format(it) }}")
                hid.handle(frame, current::send)
            } else {
                log("车机消息 0x${frame.messageId.toString(16)}")
            }
            // The head unit subscribes to phone status right after
            // authentication and keeps those subscriptions for the session.
            if (frame.messageId == 0x5000) {
                nowPlayingSubscription = nowPlaying.subscribe(frame, current::send, current::sendFileTransfer)
                log("蓝牙 iAP2 0x5000：已开始真实 NowPlaying 发布")
            } else if (frame.messageId == 0x5002) {
                nowPlaying.unsubscribe(nowPlayingSubscription)
                log("蓝牙 iAP2 0x5002：已停止 NowPlaying 发布")
            }
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
    private fun subscriptionReply(messageId: Int): com.carplay.iap2.wire.Iap2Frame? =
        Iap2SubscriptionReplies.forRequest(messageId) ?: when (messageId) {
        0x5200 -> com.carplay.iap2.message.Iap2Messages.buildRaw(0x5201) {
            u16(0, 0)
            u8(1, 0)
            u8(2, 0)
        }
        0xae00 -> com.carplay.iap2.message.Iap2Messages.buildRaw(0xae01) { u8(0, 0) }
        else -> null
    }

    private fun startReceiver(current: Iap2Session, boot: PhoneBootstrap) {
        receiver = Thread({
            try {
                while (!closed.get() && !current.isClosed) {
                    val frame = current.recv(1000) ?: continue
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

    private data class Link(
        val socket: BluetoothSocket,
        val session: Iap2Session,
        val transport: PhoneBluetoothSessionTransport,
    )

    private data class HandshakeAttempt(val link: Link?, val receivedBytes: Long)

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
            val attempt = synchronize(connected, candidate, outbound)
            attempt.link?.let { return it }

            if (RfcommConnectionStrategy.shouldFallbackToInboundAfterSilentHandshake(
                    RfcommConnectionDirection.PHONE_INITIATED,
                    outbound,
                    attempt.receivedBytes,
                )
            ) {
                log("手机主动 RFCOMM 已连接但 iAP2 收到 0B；改等车机主动连入本机 iAP2 服务")
                val inbound = try {
                    listenInbound(SILENT_INBOUND_RECOVERY_TIMEOUT_MILLIS)
                } catch (failure: IOException) {
                    if (closed.get()) throw failure
                    log("车机主动回连恢复未建立：${failure.message}")
                    null
                }
                if (inbound != null) {
                    synchronize(inbound, candidate, outbound = false).link?.let { return it }
                }
            }
            log("iAP2 同步未完成，换下一个握手方案")
        }
        error("蓝牙 iAP2 同步失败：请确认车机屏幕上已选择本机")
    }

    private fun synchronize(
        connected: BluetoothSocket,
        candidate: PhoneBluetoothProfile,
        outbound: Boolean,
    ): HandshakeAttempt {
        owned += connected
        log(
            "RFCOMM socket 已建立：方向=${if (outbound) "手机主动" else "车机回连"}，" +
                "connected=${connected.isConnected}，bondState=${connected.remoteDevice.bondState}",
        )
        val transport = PhoneBluetoothSessionTransport(
            BluetoothRfcommDuplexStream(connected) { log("蓝牙流诊断：$it") },
        )
        owned += transport
        val traceHandshake = AtomicBoolean(true)
        val session = Iap2Session.phoneBluetooth(
            transport.open(),
            candidate,
            onTrace = { trace ->
                if (traceHandshake.get()) log("iAP2 握手诊断：$trace")
            },
            onFileTransferDatagram = { datagram -> nowPlaying.onFileTransferDatagram(datagram) },
        )
        owned += session
        val synchronized = session.awaitPhoneBluetoothReady(candidate)
        traceHandshake.set(false)
        val status = session.handshakeStatus()
        if (synchronized) {
            log("iAP2 链路已同步（${if (outbound) "手机主动" else "车机回连"}）")
            return HandshakeAttempt(Link(connected, session, transport), status.receivedBytes)
        }
        log(
            "iAP2 握手未完成：方向=${if (outbound) "手机主动" else "车机回连"}，方案=${candidate.label}，" +
                "状态=${status.state}，已发=${status.sentBytes}B，已收=${status.receivedBytes}B，" +
                "收到车机SYN=${status.peerSynchronization != null}，控制会话=${status.controlSessionId}",
        )
        runCatching { session.close() }
        runCatching { transport.close() }
        runCatching { connected.close() }
        return HandshakeAttempt(null, status.receivedBytes)
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

    private fun listenInbound(timeoutMillis: Long = INBOUND_CONNECT_TIMEOUT_MILLIS): BluetoothSocket {
        require(timeoutMillis > 0) { "timeoutMillis must be positive" }
        val listener = registerPhoneService()
        log(
            "等待车机回连本机 iAP2 服务，最多 ${TimeUnit.MILLISECONDS.toSeconds(timeoutMillis)} 秒；" +
                "请在车机蓝牙列表中选择本机",
        )
        val deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMillis)
        while (!closed.get() && System.nanoTime() < deadline) {
            val remainingMillis = TimeUnit.NANOSECONDS.toMillis(deadline - System.nanoTime())
                .coerceIn(1, Int.MAX_VALUE.toLong())
            val accepted = try {
                listener.accept(remainingMillis.toInt())
            } catch (failure: IOException) {
                if (closed.get()) throw IOException("连接已停止", failure)
                if (System.nanoTime() >= deadline) break
                throw failure
            } ?: break
            if (accepted.remoteDevice.address.equals(peerAddress, true) &&
                accepted.remoteDevice.bondState == BluetoothDevice.BOND_BONDED
            ) {
                log("车机已回连本机")
                return accepted
            }
            accepted.close()
        }
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeoutMillis)
        throw IOException("${seconds} 秒内未收到车机的蓝牙回连；请在车机屏幕上选择本机")
    }

    /** Reads the link without consuming protocol frames; used only for diagnostics. */
    fun isAlive(): Boolean = !closed.get() && socket?.isConnected == true && session?.isClosed == false

    override fun close() {
        if (!closed.compareAndSet(false, true)) return
        nowPlaying.close()
        runCatching { receiver?.interrupt() }
        owned.reversed().forEach { runCatching { it.close() } }
        owned.clear()
        phoneService?.let { runCatching { it.close() } }
        phoneService = null
    }

    companion object {
        private const val INBOUND_CONNECT_TIMEOUT_MILLIS = 45_000L
        private const val SILENT_INBOUND_RECOVERY_TIMEOUT_MILLIS = 15_000L

        /** The phone's own CarPlay service; the car discovers this over SDP. */
        val PHONE_UUID: UUID = UUID.fromString("00000000-deca-fade-deca-deafdecacafe")

        /** The car's iAP2 service the phone dials. */
        val ACCESSORY_UUID: UUID = UUID.fromString("00000000-deca-fade-deca-deafdecacaff")

    }
}
