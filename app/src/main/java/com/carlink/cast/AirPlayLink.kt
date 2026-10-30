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
import kotlinx.coroutines.launch
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
    /**
     * Shared with the Bluetooth link, which subscribes first.
     *
     * The head unit's own identification names 0x5000 among the messages it
     * sends and 0x5001 among the ones it receives, and the Bluetooth exchange
     * completes every session while the Wi-Fi command channel never does.
     * Owning the publisher above both links lets the subscription start on the
     * transport that actually works instead of the one that does not.
     */
    private val nowPlayingPublisher: NowPlayingPublisher? = null,
    private val terminalFailure: (Throwable) -> Unit = {},
) : Closeable {

    private val closed = AtomicBoolean()
    /**
     * Owns work that must not sit on the sender's activation path.
     *
     * Currently just the source launch: `events.startDisplay` is called
     * synchronously between RECORD and the audio SETUP, so anything it blocks
     * on is charged directly against the head unit's 13.1 s countdown.
     * Cancelled in [close] so a session teardown cannot leave a source thread
     * starting against a socket that is already gone.
     */
    private val sourceScope = kotlinx.coroutines.CoroutineScope(
        kotlinx.coroutines.SupervisorJob() + kotlinx.coroutines.Dispatchers.IO,
    )
    private val audioProfiles = AudioProfileMemory(context, peerAddress)
    private val nowPlaying = nowPlayingPublisher ?: NowPlayingPublisher(context, log)
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
                val ordered = audioProfiles.order(candidates)
                log(
                    "音频档位排序：本机首选=${candidates.firstOrNull()?.key ?: "无"}" +
                        "，记忆可用=${audioProfiles.lastWorking() ?: "无"}" +
                        "，已拒=${audioProfiles.rejected().ifEmpty { listOf("无") }.joinToString(",")}" +
                        " → 实际顺序=${ordered.joinToString("/") { it.key }}",
                )
                ordered
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
            /*
             * Do not block the sender's activation path here.
             *
             * `events.startDisplay` is called synchronously from AirPlaySender
             * between RECORD and the audio SETUP, so whatever this function
             * does sits directly on the car's 13.1 s countdown. It used to
             * `runBlocking` on `source.startProjection`, which waits for
             * CarLife to open its seven ports — 4.4 s in the field when the
             * vendor process needs a cold start, measured from
             *
             *     10066 RECORD 200
             *     13076 宿主绑定重试 #1：CarLife 未运行          (+3.0 s)
             *     15483 CarLife 端口 7/7 已连接                  (+5.4 s)
             *     16721 画面已发送到车机                         (+6.7 s)
             *
             * The car's own reference numbers are EndpointRECORDTimeMs=17 and
             * TotalSETUP=289 — nothing in this protocol is supposed to take
             * seconds. Blocking here also delayed the audio SETUP behind it,
             * which is the other half of "有画面但没声音".
             *
             * So the source starts on its own thread and the activation path
             * continues immediately. Video frames simply begin arriving when
             * the encoder is ready, which is what the sender already handles
             * (it drops nothing and holds the stream open with keep-alives
             * until the first CONFIG/IDR appears).
             */
            sourceScope.launch {
                try {
                    source.startProjection(current::frame)
                    log("屏幕 SETUP/RECORD 已完成，已启动画面编码器")
                } catch (error: Throwable) {
                    if (!closed.get()) {
                        log("启动画面来源失败：${error.message ?: error.javaClass.simpleName}")
                    }
                }
            }
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
            // Set by the link layer when the car restarts its iAP2 link under
            // us; the bootstrap below then replays from 0xAA00.
            val linkRestarted = java.util.concurrent.atomic.AtomicBoolean(false)
            val restartSignal = java.util.concurrent.CountDownLatch(1)
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
                            onLinkRestart = {
                                // The car restarted its iAP2 link under us. The
                                // session above believes it is still ready; tell
                                // it to replay the whole bootstrap, or nothing
                                // will ever speak on the new link.
                                log("车机重启了 iAP2 链路：重新执行引导")
                                linkRestarted.set(true)
                                restartSignal.countDown()
                            },
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
                    /*
                     * `begin()` above is the opening message, so the retry
                     * block below must not send a second one.
                     *
                     * It used to start at 0 and fire 800 ms later, which put two
                     * 0x1D00 frames on this channel in the first second of
                     * every session — visible in the 2026-10-30 field capture at
                     * 8168 ms and 9170 ms. The bootstrap's own contract is one
                     * opening message and then silence; a duplicate is exactly
                     * the unsolicited traffic that contract exists to avoid.
                     */
                    var identifyRetries = 1
                    /*
                     * No second opening message on a timer.
                     *
                     * `begin()` above already put 0x1D00 on the wire. Arming the
                     * retry an interval later is what produced two identifications
                     * in the first second of every session in the 2026-10-30 field
                     * capture (8168 ms and 9170 ms). The bootstrap's contract — and
                     * the argument written out at length below — is one opening
                     * message and then silence, so the retry stays disarmed unless
                     * something later re-arms it explicitly.
                     */
                    var nextIdentifyRetry = Long.MAX_VALUE
                    /*
                     * Counts what the car actually answers on this transport.
                     *
                     * The command channel has never produced a single CSM
                     * message in any field capture: our 0x1D00 reaches it (the
                     * link layer acknowledges the packet) and nothing comes
                     * back. The iPhone capture shows the opposite split — the
                     * identification exchange happens on the Bluetooth link,
                     * and this Wi-Fi transport is only used for commands once
                     * that is done. These two counters make that measurable
                     * per session instead of inferred from a capture.
                     */
                    var csmReceived = 0
                    var identifyRequested = 0
                    var lastReport = System.nanoTime()
                    /*
                     * Challenge-length fallback.
                     *
                     * The Bluetooth link answers a 32-byte challenge on this
                     * car in under a second, so 32 is right there. This channel
                     * has produced no answer at all, and a receiver that goes
                     * quiet after the certificate is exactly the symptom a
                     * wrong candidate length produces. Each candidate gets its
                     * own short window before the next one goes out; the ladder
                     * is only walked while a certificate is held and no
                     * signature has arrived.
                     */
                    var nextChallengeRetry = Long.MAX_VALUE
                    var challengeWindowArmed = false
                    while (!closed.get()) {
                        if (!control.status().controlReady) {
                            check(System.nanoTime() < deadline) { "Wi-Fi iAP2 认证超时" }
                        }
                        if (!control.status().identified &&
                            identifyRetries < COMMAND_IAP_MAX_ATTEMPTS &&
                            System.nanoTime() >= nextIdentifyRetry
                        ) {
                            /*
                             * Ask for identification once, then wait for the car.
                             *
                             * This used to rotate 0xAA06 / 0xAA00 / 0x1D00 every
                             * 800 ms on the theory that one of them would be the
                             * message this firmware answered. 对照实现's own
                             * state machine, recovered from 0.3.19
                             * (`C0003a1.b()`, dispatched on the car's message id),
                             * shows that is the wrong shape:
                             *
                             *   0x1D01 -> reply 0x1D02; if not yet authenticated
                             *             send 0xAA00 carrying the challenge
                             *   0xAA01 -> parse the certificate, send 0xAA02
                             *             with a fresh random challenge
                             *   0xAA03 -> verify the signature; on success send
                             *             0xAA05, and only then 0x1D00
                             *   0xAA04 -> authentication failed
                             *   0xAA06 -> if nothing has been sent yet, send 0xAA00
                             *   default -> ignore
                             *
                             * It never volunteers a message the car did not ask
                             * for, and it opens with 0x1D00 exactly once. Every
                             * message it sends is a direct reply to one it just
                             * received. [PhoneBootstrap] already implements that
                             * dispatch; this loop was the part fighting it.
                             *
                             * So: one opening message, then silence. If the car
                             * says nothing, saying more cannot help — and on this
                             * head unit it appears to hurt, because fourteen
                             * unsolicited CSMs inside the 13.1 s window is a very
                             * different thing from the two the car itself sends.
                             */
                            identifyRetries++
                            identifyRequested++
                            control.begin()
                            nextIdentifyRetry = Long.MAX_VALUE
                            val stage = control.status()
                            log(
                                "Wi-Fi iAP2 引导：已发 0x1D00（模式=等待车机），之后不再主动发送；" +
                                    "identified=${stage.identified} authenticated=${stage.authenticated} " +
                                    "certificate=${stage.certificateReceived} 已收CSM=$csmReceived",
                            )
                        }
                        if (System.nanoTime() - lastReport >= TimeUnit.SECONDS.toNanos(5)) {
                            lastReport = System.nanoTime()
                            log(
                                "命令通道 5 秒汇总：已发识别=$identifyRequested 已收CSM=$csmReceived " +
                                    "identified=${control.status().identified}",
                            )
                        }
                        /*
                         * Certificate held, signature still missing: walk the
                         * challenge-length ladder.
                         *
                         * `challengePending` is true only between 0xAA01 and
                         * 0xAA03, which is the window where a receiver that
                         * disliked the challenge length goes quiet. Nothing is
                         * sent once it has answered.
                         */
                        val waitingForSignature = control.status().let {
                            it.certificateReceived && it.challengePending && !it.authenticated
                        }
                        if (!waitingForSignature) {
                            challengeWindowArmed = false
                        } else if (!challengeWindowArmed) {
                            challengeWindowArmed = true
                            nextChallengeRetry = System.nanoTime() +
                                TimeUnit.MILLISECONDS.toNanos(COMMAND_IAP_CHALLENGE_WINDOW_MILLIS)
                        } else if (System.nanoTime() >= nextChallengeRetry) {
                            val length = control.retryChallengeWithNextLength()
                            if (length != null) {
                                nextChallengeRetry = System.nanoTime() +
                                    TimeUnit.MILLISECONDS.toNanos(COMMAND_IAP_CHALLENGE_WINDOW_MILLIS)
                                log("命令通道换挑战长度：${length}B，窗口 ${COMMAND_IAP_CHALLENGE_WINDOW_MILLIS}ms")
                            } else {
                                nextChallengeRetry = Long.MAX_VALUE
                                log("命令通道挑战长度阶梯已走完，仍无 0xAA03")
                            }
                        }
                        /*
                         * The car restarted its iAP2 link.
                         *
                         * The 对照实现 sender states the rule this car
                         * actually wants (`V0`):
                         *
                         *   "IAPLINK: 网络认证及状态 ACK 就绪；不重发 RECORD、
                         *    不改变屏幕归属"
                         *
                         * and it separates two link states, `status-acked` and
                         * `network-ready`. Every earlier build here did the
                         * opposite: it replayed the entire bootstrap — 0xAA00,
                         * 0x1D00, the lot — on every restart. Field captures
                         * show that restart arriving 10.0 s after RECORD with
                         * the session torn down 2.9 s later, at 13084/13089/
                         * 13108 ms across sessions; a sender that stays up does
                         * not do that work at all.
                         *
                         * So: if this link was already identified, keep quiet —
                         * reset only the link layer's own bookkeeping and wait.
                         * Only a link that never finished identification needs
                         * the bootstrap replayed.
                         */
                        if (linkRestarted.compareAndSet(true, false)) {
                            /*
                             * A link restart resets the exchange and re-arms
                             * exactly one opening message.
                             *
                             * 对照实现 says this in its own words (`V0`,
                             * recovered from 0.3.19):
                             *
                             *   "IAPLINK: 网络认证及状态 ACK 就绪；不重发 RECORD、
                             *    不改变屏幕归属"
                             *
                             * and its state machine keeps `u` (identified) and
                             * `k` (authenticated) as the only two flags, clearing
                             * them with the link and never volunteering a message
                             * the car did not ask for. Its `0x1D00` is sent from
                             * exactly two places: once as the opening move, and
                             * again after a successful `0xAA05` when `u` is still
                             * false.
                             *
                             * So the restart clears the negotiated state and sends
                             * one 0x1D00. It no longer replays a full round on a
                             * timer — that is what produced fourteen unsolicited
                             * CSMs inside a 13.1 s window on the field capture,
                             * against the two the car itself sent.
                             */
                            control.restartForNewLink()
                            identifyRetries = 1
                            identifyRequested++
                            control.begin()
                            nextIdentifyRetry = Long.MAX_VALUE
                            log(
                                "车机重开了 iAP2 链路：清空协商状态并重发一次 0x1D00，" +
                                    "之后等待车机（推流中=${sender?.isStreaming() == true}）",
                            )
                        }
                        val frame = runtime.recv(1000) ?: continue
                        csmReceived++
                        log("命令通道收到 CSM：0x${frame.messageId.toString(16)} " +
                            "body=${frame.payload.size}B（本会话第 $csmReceived 条）")
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
                // The car dropped us, not the driver. Tell the source before the
                // close below runs, so its teardown keeps the vendor process
                // warm instead of paying a cold start on the next attempt.
                runCatching { source.noteAbnormalEnd() }
                terminalFailure(error)
                close()
            }
        }
    }

    override fun close() {
        if (!closed.compareAndSet(false, true)) return
        /*
         * Record the iAP2 HID state before anything is torn down.
         *
         * Touch, media keys, the phone buttons and the voice button all arrive
         * on the iAP2 command channel after the car sends 0x6800 and we answer
         * it. If this line says "0x6800 未到达" the car never offered input at
         * all, and nothing downstream (CarLifeHardKey, the Honor broadcast, the
         * Android media keys) was ever reachable — which is the difference
         * between a mapping bug and a transport that never came up.
         */
        runCatching { log("输入链路快照：${hid.describe()}") }
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
        // Cancel before interrupting the worker: a source launch still in
        // flight would otherwise keep opening sockets against a dead session.
        runCatching { sourceScope.coroutineContext[kotlinx.coroutines.Job]?.cancel() }
        worker?.interrupt()
    }

    private companion object {
        /**
         * How many bootstrap attempts a session may spend.
         *
         * The counter is only ever advanced by an explicit re-arm — the link
         * restart below — so this is a ceiling on restarts handled, not a timer.
         */
        const val COMMAND_IAP_MAX_ATTEMPTS = 40

        /**
         * How long one challenge-length candidate gets before the next is sent.
         *
         * The Bluetooth link turns a 32-byte challenge into a 0xAA03 inside
         * 700 ms on this car, so 3 s is four times the working round trip: long
         * enough not to cut a slow answer short, short enough that the whole
         * three-candidate ladder finishes well inside the 13.1 s teardown.
         */
        const val COMMAND_IAP_CHALLENGE_WINDOW_MILLIS = 3_000L
    }
}
