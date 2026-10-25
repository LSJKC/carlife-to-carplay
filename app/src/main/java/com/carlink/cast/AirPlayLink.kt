package com.carlink.cast

import android.content.Context
import com.carplay.adapter.PhoneProjectionAdapter
import com.carplay.adapter.ProjectionObserver
import com.carplay.adapter.carlife.CarLifeProjectionAdapter
import com.carplay.iap2.controller.PhoneBootstrap
import com.carplay.iap2.controller.PhoneBootstrapPurpose
import com.carplay.iap2.controller.PhoneBootstrapProfile
import com.carplay.iap2.controller.WirelessEndpoint
import com.carplay.iap2.session.Iap2Session
import com.carplay.iap2.wire.Iap2Frame
import com.carplay.iap2.transport.BlockingDuplexByteStream
import com.carplay.sender.AccessoryAuthentication
import com.carplay.sender.AccessoryTrust
import com.carplay.sender.AirPlaySender
import com.carplay.sender.ControllerIdentity
import com.carplay.sender.MfiController
import com.carplay.sender.PairedAccessory
import com.carplay.sender.PeerAddress
import com.carplay.sender.PhonePairing
import com.carplay.sender.SenderAudioFormat
import com.carplay.sender.SenderDisplay
import com.carplay.sender.SenderEvents
import kotlinx.coroutines.runBlocking
import java.io.Closeable
import java.net.DatagramSocket
import java.net.Inet6Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * The Wi-Fi half of one session.
 *
 * Order of operations matches the head unit: pair-verify (pair-setup on first
 * use) on the control socket, an independently authenticated type 130 iAP
 * control stream, /info for the display size, then SETUP 110 + RECORD and the
 * encrypted video stream.
 *
 * Bluetooth is deliberately absent from teardown logic: it is kept open by
 * [BluetoothBootstrap] for the whole session because this head unit resets the
 * entire session, video included, when the bootstrap disappears.
 */
internal class AirPlayLink(
    private val context: Context,
    private val identity: ControllerIdentity,
    private val vehicle: VehicleNetwork,
    private val source: PhoneProjectionAdapter,
    private val settings: CastSettings,
    private val enableAudio: Boolean,
    private val endpoint: WirelessEndpoint,
    private val peerAddress: String,
    private val trustFor: (Boolean) -> AccessoryAuthentication,
    private val hid: Iap2HidDispatcher,
    private val bluetoothAlive: () -> Boolean,
    private val bluetoothIdentity: () -> ByteArray?,
    private val log: (String) -> Unit,
    private val terminalFailure: (Throwable) -> Unit = {},
) : Closeable {

    private val closed = AtomicBoolean()
    private val audioProfiles = AudioProfileMemory(context, peerAddress)
    private val nowPlaying = NowPlayingPublisher(context, log)
    @Volatile private var nowPlayingSubscription = 0L
    private val failed = AtomicBoolean()
    private val resources = mutableListOf<Closeable>()
    @Volatile private var sender: AirPlaySender? = null
    @Volatile private var display: SenderDisplay? = null
    private var worker: Thread? = null
    private var controlServer: ControlInvitationServer? = null
    @Volatile private var discoveredPort: Int? = null
    @Volatile private var discoveredKey: String? = null
    /**
     * Set once the type 130 tunnel has been independently authenticated.
     *
     * The receiver closes the Bluetooth bootstrap itself as the final step of
     * the wireless handoff and keeps the session on Wi-Fi; before that point a
     * lost link really does make the car reset everything.
     */
    @Volatile private var wifiControlReady = false
    private val bluetoothReleased = AtomicBoolean()

    fun isWirelessHandoffComplete(): Boolean = wifiControlReady

    fun isHealthy(): Boolean = !closed.get() && !failed.get() && vehicle.isAlive()

    /**
     * Runs the session on its own thread.
     *
     * [onFailure] receives a terminal error; [onFinished] always runs once the
     * worker exits, which is how the owner learns that a clean close (for
     * example the car ending the session) is not a hang.
     */
    fun start(onFailure: (Throwable) -> Unit, onFinished: () -> Unit) {
        worker = Thread({
            try {
                run()
            } catch (failure: Throwable) {
                if (!closed.get()) {
                    failed.set(true)
                    log("投屏会话失败：${failure.message ?: failure.javaClass.simpleName}")
                    close()
                    onFailure(failure)
                }
            } finally {
                onFinished()
            }
        }, "carlink-airplay").apply { isDaemon = true; start() }
    }

    private fun <T : Closeable> own(value: T): T {
        synchronized(resources) { resources += value }
        return value
    }

    private fun run() {
        // The invitation listener must exist before discovery so the published
        // service carries its real port.
        val network = checkNotNull(vehicle.network) { "车机网络尚未就绪" }
        val phoneAddress = vehicle.localAddress()
        controlServer = own(ControlInvitationServer(network, phoneAddress, log)).also { it.start() }
        val targets = resolveTargets()
        val controlPort = endpoint.port ?: discoveredPort ?: 7000
        // Publish while the AirPlay handshake runs, as the iPhone does: the car
        // invites this service once it accepts the controller.
        val discovery = own(AirPlayDiscovery(
            context = context,
            network = network,
            local = phoneAddress,
            controlPort = controlServer?.port ?: 0,
            transportId = identity.id,
            log = log,
        ))
        runCatching { discovery.start() }.onFailure { log("Bonjour 启动失败：${it.message}") }
        log("连接车机 AirPlay：${targets.first().remote.hostAddress}:$controlPort")
        var receiver: InetAddress? = targets.first().remote
        var local: InetAddress? = targets.first().local
        val connect: (Int) -> Socket = { port ->
            val socket = Socket()
            vehicle.bind(socket)
            var connected = false
            var lastFailure: Exception? = null
            val deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10)
            for (target in targets) {
                val remaining = ((deadline - System.nanoTime()) / 1_000_000).coerceAtLeast(1).toInt()
                try {
                    socket.bind(InetSocketAddress(target.local, 0))
                    socket.connect(InetSocketAddress(target.remote, port), remaining)
                    receiver = target.remote
                    local = target.local
                    connected = true
                    break
                } catch (failure: Exception) {
                    lastFailure = failure
                }
            }
            if (!connected) {
                socket.close()
                throw java.io.IOException("车机服务连接失败", lastFailure)
            }
            log("车机 TCP 已固定：interface=${vehicle.link?.interfaceName}，" +
                "IPv6=${socket.localAddress is Inet6Address}，port=$port")
            socket
        }
        val channel = own(AirPlaySender(
            identity = identity,
            pair = { pairing -> pairingFor(pairing) },
            connect = connect,
            bindDatagram = { vehicle.bindDatagram(it) },
            events = events(),
            savePair = { settings.savePair(peerAddress, it) },
            legacyAuthentication = { wire ->
                log("车机使用旧版 MFiSAP 认证")
                MfiController.authenticate(
                    { bytes -> wire.request("POST", "/auth-setup", bytes,
                        mapOf("Content-Type" to "application/octet-stream")).body },
                    trustFor(true),
                )
            },
            audioProfileOrder = { candidates ->
                audioProfiles.order(candidates)
            },
            audioProfileWorking = { profile ->
                audioProfiles.recordWorking(profile)
                log("车机音频档位已记忆：${profile.key}")
            },
            audioProfileRejected = { profile ->
                audioProfiles.recordRejected(profile)
                log("车机音频档位已后置：${profile.key}（SETUP 后实际音频流未稳定）")
            },
            // This must be the phone's own address on the vehicle network.
            // Passing the car's address here made NtpClock bind to a foreign
            // address and the whole session died with EADDRNOTAVAIL.
            localAddress = { local },
        ))
        sender = channel
        source.setObserver(object : ProjectionObserver {
            override fun videoConfig(avcC: ByteArray) = channel.config(avcC)
            override fun audioFrame(bytes: ByteArray, sampleTimeUs: Long, pcm: Boolean) {
                CastLog.sourceAudio(bytes, pcm)
                try {
                    channel.audio(bytes, sampleTimeUs)
                } catch (error: Throwable) {
                    log("音频来源交付失败：${error.message ?: error.javaClass.simpleName}，输入=${bytes.size}B，格式=${if (pcm) "PCM" else "AAC"}")
                }
            }
            override fun diagnostic(message: String) = log(message)
            override fun failed(error: Throwable) {
                if (!closed.get()) {
                    log("画面来源异常：${error.message ?: error.javaClass.simpleName}")
                    close()
                }
            }
        })
        val selected = channel.start(controlPort, enableAudio)
        display = selected
        hid.setDisplay(selected)
        CastLog.display(selected.width, selected.height, selected.fps)
        log("车机显示 ${selected.width}×${selected.height}@${selected.fps}")
        while (!closed.get()) {
            Thread.sleep(500)
            // The car's own Wi-Fi lease is the real lifeline. Once it is gone
            // the picture has nowhere to go, but nothing above used to notice:
            // the loop spun forever, CarLife kept encoding into a dead socket,
            // and CarLife kept the phone's audio route, so everything else on
            // the phone went silent until the app was force-stopped by hand.
            if (!vehicle.isAlive()) throw IllegalStateException("车机无线网络已断开")
            if (!bluetoothAlive()) {
                if (wifiControlReady) {
                    if (bluetoothReleased.compareAndSet(false, true)) {
                        log("车机已完成无线交接并释放蓝牙链路；会话继续走 Wi-Fi")
                    }
                } else {
                    throw IllegalStateException("蓝牙链路已断开，车机将重置会话")
                }
            }
        }
    }

    /**
     * 0x4301's addresses first; when the car omits them (or they do not fit
     * this interface) the discovered AirPlay service supplies them instead.
     */
    private fun resolveTargets(): List<PeerAddress> {
        if (endpoint.addresses.isNotEmpty()) {
            val direct = vehicle.resolveTargets(endpoint.addresses)
            if (direct.isNotEmpty()) return direct
            log("0x4301 地址与本机网络不匹配，改用 Bonjour 发现")
        }
        val network = checkNotNull(vehicle.network) { "车机网络尚未就绪" }
        AirPlayDiscovery(
            context = context,
            network = network,
            local = vehicle.localAddress(),
            controlPort = controlServer?.port ?: 0,
            transportId = identity.id,
            log = log,
        ).use { discovery ->
            runCatching { discovery.start() }
                .onFailure { failure -> log("Bonjour 启动失败：${failure.message}") }
            val found = discovery.await()
                ?: error("未发现车机 AirPlay 服务；请确认车机屏幕已进入 CarPlay")
            val targets = vehicle.resolveTargets(found.addresses.mapNotNull { address -> address.hostAddress })
            require(targets.isNotEmpty()) { "发现的车机地址与本机网络不匹配" }
            // The discovered port is the receiver's control port; the sender
            // dials it directly.
            discoveredPort = found.port
            return targets
        }
    }

    /** First use runs pair-setup with the CarPlay PIN, then verifies the car's key. */
    private fun pairingFor(pairing: PhonePairing): ByteArray {
        val saved: PairedAccessory? = settings.paired(peerAddress)
        // 0x4301 field 4 is only treated as a key when it is actually a
        // 32-byte hex key; on this firmware it usually carries a device id,
        // which must not be compared against the AirPlay pairing identity.
        val peerKey = endpoint.publicKey
            ?.takeIf { it.length == 64 && it.all { c -> c in '0'..'9' || c.lowercaseChar() in 'a'..'f' } }
            ?.chunked(2)?.map { it.toInt(16).toByte() }?.toByteArray()
        if (saved != null) {
            try {
                if (peerKey != null) {
                    check(AccessoryTrust.equal(saved.publicKey, peerKey)) {
                        "车机配对公钥已变化，请清除配对记录后重试"
                    }
                }
                log("使用已保存的车机配对身份")
                return pairing.verify(saved)
            } catch (failure: Exception) {
                log("已保存配对失效（${failure.message}），重新配对")
                settings.removePair(peerAddress)
            }
        }
        val created = pairing.setup("3939", advertisedKey = peerKey)
        settings.savePair(peerAddress, created)
        return pairing.verify(created)
    }

    private fun handleControlMessage(runtime: Iap2Session, frame: Iap2Frame) {
        if (frame.messageId in setOf(0x6800, 0x6802, 0x6803)) {
            log("Wi-Fi iAP2 HID 0x${frame.messageId.toString(16)} 长度=${frame.payload.size} " +
                "hex=${frame.payload.joinToString(" ") { "%02X".format(it) }}")
            hid.handle(frame, runtime::send)
            return
        }
        when (frame.messageId) {
            0x5000 -> {
                nowPlayingSubscription = nowPlaying.subscribe(frame, runtime::send, runtime::sendFileTransfer)
                log("Wi-Fi iAP2 0x5000：已开始真实 NowPlaying 发布")
            }
            0x5002 -> {
                nowPlaying.unsubscribe(nowPlayingSubscription)
                log("Wi-Fi iAP2 0x5002：已停止 NowPlaying 发布")
            }
            0x4157, 0x4154, 0x4170 -> {
                Iap2SubscriptionReplies.forRequest(frame.messageId)?.let { reply ->
                    runtime.send(reply)
                    log("Wi-Fi iAP2 0x${frame.messageId.toString(16)}：已回复空闲订阅状态")
                }
            }
            else -> log("Wi-Fi 控制消息 0x${frame.messageId.toString(16)}")
        }
    }

    private fun events() = object : SenderEvents {
        override fun diagnostic(message: String) = log(message)

        override fun outputDisplay(display: SenderDisplay): SenderDisplay {
            val carLife = source as? CarLifeProjectionAdapter ?: return display
            val panel = CarPanelGeometry.panelFor(display)
            carLife.panel = panel
            val safeAreaPolicy = settings.safeAreaPolicy(peerAddress)
            carLife.safeAreaPolicy = safeAreaPolicy
            (carLife.videoProcessor as? CarLifePanelCompositor)?.let { compositor ->
                compositor.updateSafeAreaPolicy(safeAreaPolicy)
                compositor.updatePanel(panel)
            }
            log(
                "按车机主显示准备 CarLife 全画布：${display.width}x${display.height}" +
                    (display.safeArea?.let {
                        "，safeArea=${it.width}x${it.height}+${it.x},${it.y}"
                    } ?: "，未声明 safeArea") +
                    "；安全区方案=${safeAreaPolicy.mode.name}，启用=${safeAreaPolicy.enabled}",
            )
            return display
        }

        override fun display(display: SenderDisplay) {
            // This is the preparation half of the Buick-E5 ordering.  The
            // source learns the full panel geometry before screen SETUP, but
            // its encoder is not started until the sender has completed RECORD.
            val requested = settings.frameRateOverride.takeIf { it > 0 } ?: display.fps
            if (requested != display.fps) {
                log("帧率覆盖：车机声明 ${display.fps}fps，改用 ${requested}fps")
            }
            CastLog.stage(CastStage.MEDIA, "车机显示 ${display.width}×${display.height}，准备画面")
            runBlocking {
                check(source.prepare(context, display.width, display.height, requested)) {
                    "画面来源未准备就绪"
                }
            }
        }

        override fun startDisplay(display: SenderDisplay) {
            val current = sender ?: error("发送端尚未就绪")
            runBlocking {
                source.startProjection(current::frame)
            }
            log("屏幕 SETUP/RECORD 已完成，已启动画面编码器")
        }

        override fun keyFrameRequested() = source.requestKeyFrame()

        override fun screenRestoreKeyFrameRequested() = source.requestScreenRestoreKeyFrame()

        override fun input(command: Map<*, *>): Boolean = Dispatch.input(command, source, display)

        override fun screenOwnership(owned: Boolean) {
            log(if (owned) "车机已将屏幕交给手机" else "车机切回原生界面，暂停画面")
        }

        override fun sent(bytes: Int, queuedNanos: Long) {
            if (CastLog.state.value.frames == 0L) CastLog.stage(CastStage.STREAMING, "画面已发送到车机")
            CastLog.video(bytes)
        }

        override fun dropped(count: Long) = repeat(count.toInt()) { CastLog.video(0, dropped = true) }

        override fun audioSent() = CastLog.audio()

        override fun audioFormat(format: SenderAudioFormat) {
            source.configureAudio(format.sampleRate, format.channels, format.isPcm)
            log(
                "音频：${if (format.isPcm) "LPCM" else "AAC-LC"} ${format.sampleRate}Hz / " +
                    "${format.channels} 声道（type ${format.streamType}）",
            )
        }

        override fun bluetoothControlAvailable(): Boolean {
            val alive = !closed.get() && bluetoothAlive()
            log("蓝牙控制通道：可用=$alive")
            return alive
        }

        override fun iapControl(stream: BlockingDuplexByteStream): CompletableFuture<Unit> {
            val done = CompletableFuture<Unit>()
            Thread({
                try {
                    // The Wi-Fi command channel carries a link the car opened
                    // itself, so it must be answered as a responder; the type-130
                    // data stream is the reverse case.
                    val runtime = if ((stream as? com.carplay.sender.PhoneIapTunnel)?.isCommandTunnel == true) {
                        Iap2Session.commandTunnel(
                            stream,
                            onTrace = { log("iAP2：$it") },
                            onFileTransferDatagram = { datagram -> nowPlaying.onFileTransferDatagram(datagram) },
                        )
                    } else {
                        Iap2Session.controllerTunnel(stream)
                    }
                    check(runtime.awaitReady(15_000)) { "Wi-Fi iAP2 链路同步超时" }
                    CastLog.stage(CastStage.PAIR, "Wi-Fi 控制通道已同步，独立验证车机")
                    val trust = trustFor(false)
                    val control = PhoneBootstrap(
                        identifier = identity.id,
                        validateCertificates = trust::validate,
                        verifyChallenge = trust::verifyAuthentication,
                        send = runtime::send,
                        onStage = log,
                        onEndpoint = { /* The control channel must not move the endpoint. */ },
                        onOther = { frame -> handleControlMessage(runtime, frame) },
                        profile = PhoneBootstrapProfile.MODERN_WIRELESS,
                        transportIdentifier = { identity.id },
                        expectedAccessoryIdentity = bluetoothIdentity(),
                        purpose = PhoneBootstrapPurpose.CONTROL_SESSION,
                    )
                    control.begin()
                    val deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(30)
                    var identifyRetries = 0
                    var nextIdentifyRetry = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(2_500)
                    while (!closed.get()) {
                        if (!control.status().controlReady) {
                            check(System.nanoTime() < deadline) { "Wi-Fi iAP2 认证超时" }
                        }
                        if (!control.status().identified &&
                            identifyRetries < 2 &&
                            System.nanoTime() >= nextIdentifyRetry
                        ) {
                            // Some E2LB sessions open the command tunnel before
                            // the receiver has armed its identification reader.
                            // A bounded repeat of 0x1d00 recovers that race;
                            // unbounded repeats would themselves trigger the
                            // peer-detection restart watchdog.
                            identifyRetries++
                            control.begin()
                            nextIdentifyRetry = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(3_000)
                            log("Wi-Fi iAP2 尚未收到 0x1d01，重发 StartIdentification（第 ${identifyRetries} 次）")
                        }
                        val frame = runtime.recv(1000) ?: continue
                        control.receive(frame)
                        if (control.status().controlReady && !done.isDone) {
                            runtime.awaitOutboundAcknowledged(3000)
                            wifiControlReady = true
                            log("Wi-Fi 控制通道独立验证完成；蓝牙链路保持连接")
                            done.complete(Unit)
                            scheduleDefaultNowPlaying(runtime)
                        }
                    }
                } catch (failure: Throwable) {
                    done.completeExceptionally(failure)
                }
            }, "carlink-wifi-control").apply { isDaemon = true; start() }
            return done
        }

        private fun scheduleDefaultNowPlaying(runtime: Iap2Session) {
            Thread({
                try {
                    Thread.sleep(2_500)
                    if (!closed.get() && !nowPlaying.hasSubscription()) {
                        nowPlaying.subscribeDefault(
                            sender = runtime::send,
                            fileSender = runtime::sendFileTransfer,
                        )
                    }
                } catch (_: InterruptedException) {
                    // Session teardown owns the next state.
                } catch (failure: Throwable) {
                    if (!closed.get()) log("NowPlaying 默认订阅失败：${failure.message}")
                }
            }, "carlink-now-playing-fallback").apply { isDaemon = true; start() }
        }

        override fun failed(error: Throwable) {
            if (!closed.get() && failed.compareAndSet(false, true)) {
                val detail = "${error.javaClass.simpleName}: ${error.message ?: "无消息"}"
                log("AirPlay 会话异常：$detail")
                terminalFailure(error)
                close()
            }
        }
    }

    override fun close() {
        if (!closed.compareAndSet(false, true)) return
        // Stop the source before closing AirPlaySender.  AirPlaySender.close()
        // interrupts its worker threads; doing it first can interrupt the
        // CarLife audio/compositor cleanup and skip releaseCarLife entirely.
        runCatching { runBlocking { source.stopProjection() } }
            .onFailure { log("画面来源释放失败：${it.javaClass.simpleName}: ${it.message}") }
        runCatching { sender?.close() }
            .onFailure { log("AirPlay 发送端关闭失败：${it.message}") }
        synchronized(resources) {
            resources.reversed().forEach { resource ->
                runCatching { resource.close() }
                    .onFailure { log("AirPlay 资源关闭失败：${it.message}") }
            }
            resources.clear()
        }
        nowPlaying.close()
        worker?.interrupt()
    }
}
