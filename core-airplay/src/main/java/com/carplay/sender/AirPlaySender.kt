package com.carplay.sender
import android.os.SystemClock

import com.carplay.airplay.*
import com.carplay.iap2.transport.BlockingDuplexByteStream
import java.io.Closeable
import java.math.BigInteger
import java.net.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.SecureRandom
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.util.ArrayDeque
import java.util.concurrent.locks.LockSupport

data class SenderSafeArea(val x: Int, val y: Int, val width: Int, val height: Int)

data class SenderDisplay(
    val width: Int,
    val height: Int,
    val fps: Int,
    val uuid: String?,
    val safeArea: SenderSafeArea? = null,
)
interface SenderEvents {
    fun diagnostic(message: String)
    /** Lets the source prepare its canvas from the receiver's full display contract. */
    fun outputDisplay(display: SenderDisplay): SenderDisplay = display
    /**
     * Prepare the source after the car's display contract is known, but before
     * the AirPlay screen stream is opened.  This keeps capture failures from
     * issuing RECORD while allowing vendor sources to start their host process.
     */
    fun display(display: SenderDisplay)
    /** Start the actual encoder after screen SETUP, dataPort and RECORD. */
    fun startDisplay(display: SenderDisplay) = Unit
    fun keyFrameRequested()
    fun screenRestoreKeyFrameRequested() = keyFrameRequested()
    fun input(command: Map<*, *>): Boolean
    fun screenOwnership(owned: Boolean)
    fun sent(bytes: Int, queuedNanos: Long)
    fun dropped(count: Long)
    fun audioSent()
    fun audioFormat(format: SenderAudioFormat)
    fun bluetoothControlAvailable(): Boolean
    fun iapControl(stream: BlockingDuplexByteStream): CompletableFuture<Unit>
    fun failed(error: Throwable)
}

/** Controller-side transport. Each instance owns all of its sockets, counters and workers. */
class AirPlaySender(
    private val identity: ControllerIdentity,
    private val pair: (PhonePairing) -> ByteArray,
    private val connect: (Int) -> Socket,
    private val bindDatagram: (DatagramSocket) -> Unit,
    private val events: SenderEvents,
    private val savePair: (PairedAccessory) -> Unit,
    private val legacyAuthentication: ((RtspWire) -> LegacySecurity)? = null,
    private val localAddress: () -> InetAddress? = { null },
    /** Optional receiver-specific audio profile memory supplied by the app layer. */
    private val audioProfileOrder: ((List<SenderAudioFormat>) -> List<SenderAudioFormat>)? = null,
    private val audioProfileWorking: ((SenderAudioFormat) -> Unit)? = null,
    private val audioProfileRejected: ((SenderAudioFormat) -> Unit)? = null,
) : Closeable {
    private val closed = AtomicBoolean()
    private val resources = CopyOnWriteArrayList<Closeable>()
    private val queue = VideoQueue()
    private val clock = NtpClock(localAddress,bindDatagram)
    private val modes = SessionModes()
    private var control: RtspWire? = null
    private var video: Socket? = null
    private var videoKey: ByteArray? = null
    /** Serializes video frames/config and the independent TCP keep-alive. */
    private val videoWriteLock = Any()
    private var videoFramesSinceKeepAlive = 0
    private var videoBytesSinceKeepAlive = 0L
    private var videoKeepAliveCount = 0L
    /** When RECORD first completed, for the disconnect heartbeat below. */
    @Volatile private var recordCompletedNanos = 0L
    /*
     * Disconnect heartbeat.
     *
     * This head unit destroys the session 13.1 s after RECORD on every build
     * measured so far — 13055 / 13102 / 13131 / 13127 ms across 1.2.97, two
     * 1.3.1 captures with and without audio, and 1.3.2. The spread is 76 ms
     * over four sessions and two months of traffic shapes, which means a
     * timer inside the car, not a reaction to anything we stream.
     *
     * The one thing that clearly differs from a sender that stays up is who
     * speaks on the event and command channels. A heartbeat that records, once
     * a second, how long it has been since *the car* last said anything makes
     * the failure legible: if inbound traffic stops at second 3 and the reset
     * lands at second 13, the car had already given up long before it acted,
     * and the silence — not the reset — is the thing to explain.
     */
    @Volatile private var lastInboundEventNanos = 0L
    @Volatile private var lastInboundIapNanos = 0L
    @Volatile private var inboundEventCount = 0L
    @Volatile private var inboundIapCount = 0L
    private var heartbeatCount = 0L
    /** Keep-alives start after the initial CONFIG/frame pair is on the wire. */
    @Volatile private var videoStreamReady = false
    @Volatile private var avcC: ByteArray? = null
    /** Set when a decoder restart means the parameter sets must be sent again. */
    @Volatile private var resendConfig = false
    /** No frame may reach the car until this epoch's avcC is installed. */
    @Volatile private var configPending = true
    /**
     * Invalidates the previous video epoch.
     *
     * It is also bumped when a P-frame arrives during recovery: that frame is
     * rejected before CONFIG is written, so its epoch can never clear the
     * handover flag. Keeping the flag across rejected frames makes the next
     * accepted IDR resend avcC even when the source did not rebuild its codec.
     */
    private val videoEpochLock = Any()
    private var videoEpoch = 0L
    /** Monotonic source CONFIG generation; prevents an old SPS/PPS from releasing a new handover. */
    private var configGeneration = 0L
    private var requiredConfigGeneration = 0L
    @Volatile private var videoRecoveryPending = false
    /** Monotonic recovery round, shared with diagnostics so one restore can be followed end-to-end. */
    private var videoRecoveryRound = 0L
    /** Prevents a burst of non-IDR frames from requesting the same rebuild repeatedly. */
    @Volatile private var keyFramePending = false
    private var format: SenderDisplay? = null
    private var audioSocket: DatagramSocket? = null
    private var audioSyncSocket: DatagramSocket? = null
    private var audioSyncThread: Thread? = null
    private var audioAddress: InetSocketAddress? = null
    private var audioKey: ByteArray? = null
    private var audioCounter = 0L
    private var audioSequence = SecureRandom().nextInt(65536)

    /**
     * One negotiated LPCM stream.
     *
     * A working sender on this head unit brings up TWO of them — type 101
     * `audioType=default` and type 100 `audioType=media` — each in its OWN
     * SETUP with its own dataPort, and feeds both the same PCM. The car reports
     * a growing sampleTime for both, so both are live outputs rather than a
     * main/backup pair.
     *
     * Packing several streams into ONE SETUP does not work here: this receiver
     * answers only the extra entries and silently drops the main one. That is
     * why the multi-stream SETUP was removed in an earlier build; separate
     * SETUPs are what it actually expects.
     */
    private class SecondaryAudio(
        val format: SenderAudioFormat,
        val socket: DatagramSocket,
        val address: InetSocketAddress,
        val key: ByteArray,
    ) {
        var sequence = SecureRandom().nextInt(65536)
        var counter = 0L
        var packets = 0L
    }

    /** Extra LPCM streams fed alongside the primary one. */
    private val secondaryAudio = mutableListOf<SecondaryAudio>()
    /**
     * Audio queue entries belong to one ownership generation.  A chunk may
     * already have left [audioQueue] when the head unit takes the resource,
     * so checking [modes] only when dequeuing is not enough to stop its
     * remainder from being emitted.
     */
    private data class AudioChunk(val bytes: ByteArray, val ptsUs: Long, val generation: Long)
    private val audioQueueLock = Object()
    private val audioQueue = ArrayDeque<AudioChunk>()
    /** Incremented whenever the head unit changes audio ownership. */
    private var audioGeneration = 0L
    private var audioWorker: Thread? = null
    private var audioQueueDrops = 0L
    /** Partial LPCM frame held by the dedicated sender so timestamps stay aligned. */
    private var pcmCarry = ByteArray(0)
    /** Next moment an LPCM packet may leave, so the car receives an even 200/s. */
    private var audioNextSendNanos = 0L
    /** Samples already handed to the car, used to advance the PCM RTP clock. */
    private var pcmSamples = 0
    /**
     * Random RTP base for the PCM clock.
     *
     * A stream that starts at sample time zero makes the car treat every packet
     * as ancient: it schedules playback against the session's network clock, so
     * a timestamp near 0 is already in the past and gets dropped. Starting from
     * an arbitrary base is what a real sender does, and it was the difference
     * between "packets arrive" and "sound comes out".
     */
    private val pcmBase = SecureRandom().nextInt(0x4000_0000)
    /** Set once, so a lost audio grant is reported once instead of per packet. */
    private var audioSuppressed = false
    private var audioPacketsSent = 0L
    private var pcmChunks = 0L
    private var pcmCarryDropped = 0L

    /** Last sampleTime the receiver reported for our audio stream, and how often. */
    @Volatile private var receiverAudioSampleTime: Long = -1L
    @Volatile private var receiverFeedbackCount = 0L
    /** /feedback requests written; diagnostics only. */
    @Volatile private var feedbackCount = 0L
    @Volatile private var lastFeedbackReportAt = 0L

    @Volatile private var lastAudioTimestamp: Long? = null
    @Volatile private var nextAudioTimestamp: Long? = null
    @Volatile private var audioSyncSequence = 0
    private var aacTimestampClock: MonotonicAudioTimestamp? = null
    private val audioSsrc = SecureRandom().nextInt()
    private var workers = mutableListOf<Thread>()
    private val touchDevices = mutableMapOf<String,HidInput>()
    private val keyDevices = mutableMapOf<String,HidKeys>()
    private val armedInputs = mutableListOf<String>()
    @Volatile private var iap: PhoneIapTunnel? = null
    /** Set while the Wi-Fi command-channel iAP2 link is being attached. */
    private var commandIapStarting = false
    /** How many command-channel links have been opened, so a restart can rebuild one. */
    private var commandIapAttempts = 0
    /**
     * How many command-channel links failed to reach control-ready.
     *
     * Only this counter is capped. [commandIapAttempts] counts every link ever
     * opened and is diagnostics only: the car legitimately reopens this link
     * mid-session, and capping successes would eventually leave a marker
     * unanswered.
     */
    private var commandIapFailures = 0
    private var commandModes = false
    private var selectedAudio: SenderAudioFormat? = null

    /** The /info reply, kept so the companion SETUP can echo what this car declared. */
    @Volatile private var sessionInfo: Map<*, *> = emptyMap<Any, Any>()

    @Volatile private var audioProfileStable = false
    @Volatile private var audioProfileSetupComplete = false
    private val iapLock = Any()
    private var openingIap = false
    /**
     * True when the head unit listed `iAPChannel` among the features it enabled.
     *
     * Kept for diagnostics only: it turned out NOT to decide whether the Wi-Fi
     * command channel can carry iAP2. See [answerCommandChannel].
     */
    private var carEnablesIapChannel = false
    private val earlyIap = ArrayDeque<ByteArray>()
    private var earlyIapBytes = 0
    /** Commands already reported once, so a per-second retry cannot flood the log. */
    private val reportedCommands = java.util.Collections.synchronizedSet(mutableSetOf<String>())
    /** Touch reports seen so far; only the first few are written out in full. */
    private var touchReports = 0

    private fun receiveIap(data: ByteArray): Boolean = synchronized(iapLock) {
        if (closed.get()) return@synchronized false
        iap?.let { return@synchronized it.offer(data) }
        if (!openingIap) return@synchronized false
        check(data.size <= 1_048_576 - earlyIapBytes) { "iAP 启动接收队列超出限制" }
        earlyIap.addLast(data.copyOf()); earlyIapBytes += data.size
        true
    }

    /**
     * Opens the iAP2 link this head unit carries on the Wi-Fi command channel.
     *
     * This firmware never enables `iAPChannel`, rejects the type-130 stream,
     * and then delivers iAP2 through `POST /command` `iAPSendMessage` anyway,
     * starting with its six-byte link marker (`FF 55 02 00 EE 10`) once a
     * second. Acknowledging those with 200 is NOT enough — measured on four
     * captures, the car resets the control connection 12.9 s after the first
     * marker, every time:
     *
     *     1.2.60  marker 8842  → reset 21791  (+12.9 s)
     *     1.2.62  marker 10043 → reset 22956  (+12.9 s)
     *     1.2.63  marker 10158 → reset 23011  (+12.9 s)
     *     1.2.64  marker 7322  → reset 20204  (+12.9 s)
     *
     * 1.2.58 concluded from the missing `iAPChannel` that this channel could
     * never work, because our 0x1d00 was never answered. The channel does work;
     * the order was wrong. The reference sender drives it authentication-first
     * on the same car:
     *
     *     RX 0xAA00 → TX 0xAA02 → RX 0xAA03 → TX 0xAA05 → TX 0x1D00 → RX 0x1D01
     *
     * and then exchanges real control traffic (0x4154, 0x4157, 0x4170, 0x4C01,
     * 0x5000, 0x5001, 0x6800) over it for the whole session. [PhoneBootstrap]
     * now uses that order for [PhoneBootstrapPurpose.CONTROL_SESSION].
     */
    private fun ensureCommandIap(): PhoneIapTunnel? {
        val wire = control ?: return null
        synchronized(iapLock) {
            if (closed.get()) return null
            // A tunnel that has already failed must not be handed out again:
            // it would silently swallow the car's marker and the session would
            // time out exactly the way this fix exists to prevent.
            iap?.let { if (!it.isClosed) return it else iap = null }
            if (commandIapStarting) return iap
            /*
             * Only FAILED links count toward the cap.
             *
             * The car closes this iAP2 round on purpose once it has what it
             * needs and reopens one later in the same session — the reference
             * sender runs two full rounds in a 115 s session (its log shows
             * 0xAA00 → 0x1D01 twice). Counting successes here would exhaust a
             * small budget on a healthy connection and leave the next marker
             * unanswered, which is the teardown this whole path exists to
             * prevent.
             */
            if (commandIapFailures >= MAX_COMMAND_IAP_FAILURES) return null
            commandIapAttempts++
            commandIapStarting = true
            val tunnel = PhoneIapTunnel(sendHandler = { data ->
                wire.request(
                    "POST", "/command",
                    encode(mapOf("type" to "iAPSendMessage", "params" to mapOf("data" to data))),
                    plistHeaders,
                ) { receiveIapResponse(it, typedOnly = false) }
            })
            iap = tunnel
            openingIap = false
            earlyIapBytes = 0
            val ready = events.iapControl(tunnel)
            events.diagnostic(
                "车机通过 Wi-Fi 命令通道发起 iAP2；已建立命令链路（第 $commandIapAttempts 次）",
            )
            ready.whenComplete { _, error ->
                synchronized(iapLock) {
                    commandIapStarting = false
                    if (error != null) {
                        events.diagnostic("Wi-Fi 命令通道 iAP2 认证未完成：${error.message ?: error.javaClass.simpleName}")
                        commandIapFailures++
                        // Drop the dead tunnel so the next marker starts afresh.
                        if (iap === tunnel) iap = null
                    }
                }
            }
            return tunnel
        }
    }

    private fun receiveIapResponse(message: ControlMessage, typedOnly: Boolean) {
        if (message.body.isEmpty()) return
        val decoded = try { BplistCodec.decode(message.body) } catch (error: Exception) {
            if (typedOnly) return
            throw java.net.ProtocolException("iAP 命令响应不是有效 plist").apply { initCause(error) }
        }
        if (typedOnly && (decoded as? Map<*, *>)?.get("type") != "iAPSendMessage") return
        val received = IapCommandData.payload(decoded,response = true)
        if (received != null) check(receiveIap(received)) { "iAP 响应数据未被控制通道接收" }
        events.diagnostic("iAP 命令回复：bodyBytes=${message.body.size}，字段数=${(decoded as? Map<*, *>)?.size ?: 0}，控制字节=${received?.size ?: 0}")
    }

    /**
     * Starts a new decoder epoch and asks the source for an IDR.
     *
     * The head unit destroys its video decoder whenever the driver leaves
     * CarPlay and comes back, so the first frames of the new decoder must be
     * preceded by the parameter sets again; otherwise it renders the background
     * and nothing else while the stream itself keeps flowing.
     *
     * CONFIG is written by the video worker when the new IDR reaches the head
     * of the queue, not here: writing it on the event thread would let it race
     * the previous epoch's in-flight frame and the IDR it is meant to precede.
     */
    fun requestKeyFrame(restore: Boolean = false) {
        if (closed.get()) return
        var notify = false
        var round = 0L
        synchronized(videoEpochLock) {
            // requestUI can be repeated while the car is still rebuilding its
            // decoder. Do not turn that poll into a second VIDEO_ENCODER_INIT:
            // CarLife replaces the active codec and commonly falls back to
            // 20 fps when two INITs overlap.
            if (keyFramePending || videoRecoveryPending) {
                events.diagnostic("视频恢复请求已合并：上一轮仍在等待新 IDR")
                return
            }
            videoEpoch++
            videoRecoveryRound++
            round = videoRecoveryRound
            resendConfig = true
            configPending = true
            videoRecoveryPending = true
            requiredConfigGeneration = configGeneration + 1
            keyFramePending = true
            notify = true
        }
        queue.resetForKeyFrame()
        events.diagnostic(
            "视频恢复轮次 #$round：${if (restore) "屏幕恢复" else "关键帧请求"}，" +
                "epoch=$videoEpoch，要求 CONFIG generation >= $requiredConfigGeneration",
        )
        if (notify) {
            if (restore) events.screenRestoreKeyFrameRequested() else events.keyFrameRequested()
        }
    }

    private fun <T : Closeable> own(resource: T): T {
        resources += resource
        if (closed.get()) { resource.close(); error("会话已停止") }
        return resource
    }

    private fun describeFailure(error: Throwable): String = buildString {
        var cause: Throwable? = error
        var depth = 0
        while (cause != null && depth < 4) {
            if (depth > 0) append(" <- ")
            append(cause.javaClass.simpleName)
            cause.message?.takeIf { it.isNotBlank() }?.let { append(": ").append(it) }
            cause = cause.cause
            depth++
        }
        error.stackTrace.take(8).forEach { append("\n  at ").append(it) }
    }

    private fun run(name: String, action: () -> Unit) {
        val worker = Thread({
            try {
                action()
            } catch (error: Exception) {
                if (!closed.get()) {
                    events.diagnostic("$name 异常：${describeFailure(error)}")
                    events.failed(error)
                }
            }
        }, name).apply { isDaemon = true }
        synchronized(workers) { workers += worker }; worker.start()
    }
    fun start(port: Int, enableAudio: Boolean): SenderDisplay {
        val socket = own(connect(port)).apply { soTimeout = 15000; tcpNoDelay = true }
        val wire = own(RtspWire(socket, events::diagnostic)); control = wire
        val pairing = PhonePairing(identity, { path, body -> wire.request("POST", path, body, pairingHeaders).body }, savePair)
        legacyAuthentication?.invoke(wire)
        // This receiver authenticates the accessory with MFiSAP but still runs
        // standard pair-setup/pair-verify: its event channel and every
        // DataStream key are derived from the pair-verify shared secret.
        // Skipping it made the car reset the whole session the moment the
        // event port was dialled.
        val shared = pair(pairing)
        fun key(salt: String, label: String) = AirPlayCrypto.hkdfSha512(shared, salt.toByteArray(), label.toByteArray())
        wire.cipher = ControlCipher(key("Control-Salt", "Control-Read-Encryption-Key"), key("Control-Salt", "Control-Write-Encryption-Key"))
        val timingPort = clock.listen(); resources += clock
        val setup = plist(wire.request("SETUP", "/", encode(mapOf(
            /*
             * 对照实现's SETUP body, recovered from 0.3.19 `C0281k1`.
             *
             * Fourteen keys, in its order:
             *
             *     deviceID, macAddress, name, model, osName, osBuildVersion,
             *     osVersion, sourceVersion, timingPort, sessionUUID,
             *     sessionCorrelationUUID, statsCollectionEnabled,
             *             keepAliveLowPower, features
             *
             * This sender answers with ten — it had no `macAddress`, no OS
             * identity and no `osVersion` at all, and described itself as
             * `model="Android"`. 对照实现 declares a concrete iPhone
             * (`model="iPhone14,2"`, `osName="iPhone OS"`, `osVersion="15.0.2"`,
             * `osBuildVersion="19A404"`, `sourceVersion="566.25.21"`), and it is
             * the sender observed holding this head unit open.
             *
             * Whether the car keys any behavior off these identity fields is
             * not known — the 13.1 s teardown could well be indifferent to all
             * of them. But declaring `Android` while pretending to be a CarPlay
             * sender is a needless inconsistency, and matching a working sender
             * field for field costs nothing.
             */
            "deviceID" to identity.id,
            "macAddress" to identity.id.uppercase(),
            "name" to "CarPlay Android",
            "model" to "iPhone14,2",
            "osName" to "iPhone OS",
            "osBuildVersion" to "19A404",
            "osVersion" to "15.0.2",
            "sourceVersion" to "566.25.21",
            "sessionUUID" to java.util.UUID.randomUUID().toString().uppercase(),
            "timingPort" to timingPort,
            "sessionCorrelationUUID" to java.util.UUID.randomUUID().toString().uppercase(),
            /*
             * `statsCollectionEnabled=false` stops the car expecting periodic
             * statistics on the feedback channel. `keepAliveLowPower=true`
             * selects the relaxed keep-alive profile — an earlier comment here
             * argued the opposite, but the sender that actually holds this car
             * open asks for low power from the start, and its keep-alive is an
             * explicit ping rather than a tight cadence.
             */
            "statsCollectionEnabled" to false,
            "keepAliveLowPower" to true,
            "features" to CONTROLLER_FEATURES,
        )), plistHeaders).body)
        val enabledFeatures = (setup["enabledFeatures"] as? List<*>)
            ?.map { it.toString() }
            .orEmpty()
        /*
         * Whether this head unit advertises iAP2 on the Wi-Fi command channel.
         *
         * The Buick never does: it advertises exactly altScreen, viewAreas and
         * enhancedSiri, the same three the iPhone negotiates in its own capture.
         * That absence is NOT evidence the channel is unusable — 1.2.58 read it
         * that way only because our 0x1d00 went unanswered, and the reason was
         * the bootstrap order, not the channel. The reference sender drives a
         * complete iAP2 link over this very channel on this very car while
         * advertising the same three features. The flag is kept for the log.
         */
        carEnablesIapChannel = enabledFeatures.any { it.equals("iAPChannel", ignoreCase = true) }
        events.diagnostic(
            "AirPlay 能力协商：车机启用=${enabledFeatures.ifEmpty { listOf("未返回") }}；" +
                "本机提议=${CONTROLLER_FEATURES.joinToString(",")}",
        )
        val timing = number(setup["timingPort"]); require(timing in 1..65535)
        clock.start(socket.inetAddress, timing)
        (setup["keepAlivePort"] as? Number)?.toInt()?.takeIf { it in 1..65535 }?.let {
            startAirPlayKeepAlive(socket.inetAddress, it)
        }
        val info = plist(wire.request("GET", "/info").body)
        sessionInfo = info
        (info["hidDevices"] as? List<*>)?.forEach { entry ->
            val device = entry as? Map<*, *> ?: return@forEach
            val uuid = device["uuid"] as? String ?: return@forEach
            val descriptor = device["hidDescriptor"] as? ByteArray ?: return@forEach
            try {
                val input = HidInput(descriptor)
                if (input.isTouch) {
                    touchDevices[uuid] = input
                    // The touch mapping depends entirely on the logical extent
                    // the car declares, and this head unit reports touches over
                    // the whole physical panel while the picture covers only
                    // part of it. Without the extent and the descriptor in the
                    // journal there is no way to tell a scaling bug from a
                    // car that simply reports in panel units, which is why the
                    // "taps land somewhere else" report could not be closed.
                    val extent = input.touchExtent
                    events.diagnostic(
                        "触摸描述符 uuid=$uuid 量程=${extent?.let { "${it.first}x${it.second}" } ?: "未声明"} " +
                            "字节=${descriptor.size} hex=${descriptor.joinToString(" ") { "%02X".format(it) }}",
                    )
                } else {
                    keyDevices[uuid] = HidKeys(descriptor)
                    events.diagnostic("按键描述符 uuid=$uuid 字节=${descriptor.size}")
                }
                // Every advertised device is armed, not just the touch surface:
                // media keys and the knob are separate uuids on this head unit.
                // The actual command is sent once the event channel is up.
                armedInputs += uuid
            } catch (error: Exception) { events.diagnostic("输入描述符不支持：${error.message}") }
        }
        /*
         * Physical buttons the car drives through /info rather than HID.
         *
         * This head unit advertises only a 29-byte consumer-control descriptor
         * and never sends a single HID report, so steering-wheel media keys
         * cannot arrive that way. `buttonInfo` is the CarPlay side's separate
         * declaration of physical buttons, and this app never read it — which
         * is why the previous track / next track buttons did nothing no matter
         * what the CarLife key mapping said.
         *
         * The whole structure is printed first. Its exact shape is not known
         * yet, and guessing at field names is what produced the 29/30 key-code
         * mistake; the content has to be seen before anything is wired to it.
         */
        (info["buttonInfo"] as? Map<*, *>)?.let { buttons ->
            events.diagnostic(
                "车机物理按键声明：${buttons.entries.joinToString(" | ") { (k, v) ->
                    "$k=${describeButtonValue(v)}"
                }}",
            )
        } ?: events.diagnostic("车机未声明 buttonInfo")
        val displays = (info["displays"] as? List<*>)?.mapNotNull { it as? Map<*, *> }.orEmpty()
        displays.forEach { entry ->
            events.diagnostic(
                "车机显示项 uuid=${entry["uuid"]} type=${entry["type"]} " +
                    "${entry["widthPixels"]}x${entry["heightPixels"]}@${entry["maxFPS"]} " +
                    "viewAreas=${describeViewAreas(entry)} initialViewArea=${entry["initialViewArea"]}",
            )
            // The physical size, feature flags and every raw key are printed
            // too: the picture lands at the left edge of this irregular panel,
            // and fixing that needs the numbers the car actually declared
            // rather than an inference about them.
            events.diagnostic("  物理尺寸 ${entry["widthPhysical"]}x${entry["heightPhysical"]}mm " +
                "features=${entry["features"]} primaryInputDevice=${entry["primaryInputDevice"]}")
            events.diagnostic("  字段 ${entry.keys.joinToString(",") { it.toString() }}")
        }
        (info["features"] as? Number)?.let { events.diagnostic("车机特性位=0x${it.toLong().toString(16)}") }
        // Every audio entry is printed field by field, never just counted.
        //
        // Whether this car can take a microphone is decided entirely by these
        // entries: `audioInputFormats` is the bitmask of PCM rates the car will
        // accept *from* the phone, and only a `speechRecognition` entry can
        // carry it. The old line logged "项=9" and nothing else, so the one
        // question that blocked microphone support — what this firmware
        // actually advertises — could not be answered from a session log at
        // all. These lines are observation only; nothing here changes what is
        // negotiated or sent. They exist so the next run answers the question
        // instead of requiring another guess.
        val audioEntries = (info["audioFormats"] as? List<*>)?.mapNotNull { it as? Map<*, *> }.orEmpty()
        events.diagnostic("车机音频格式项=${audioEntries.size}")
        audioEntries.forEach { entry ->
            val output = (entry["audioOutputFormats"] as? Number)?.toLong()
            val input = (entry["audioInputFormats"] as? Number)?.toLong()
            events.diagnostic(
                "  音频项 type=${entry["type"]} audioType=${entry["audioType"]} " +
                    "output=${output?.let { "0x${it.toString(16)}(${describeAudioBits(it)})" } ?: "无"} " +
                    "input=${input?.let { "0x${it.toString(16)}(${describeAudioBits(it)})" } ?: "无"}",
            )
            events.diagnostic("    字段 ${entry.keys.joinToString(",") { it.toString() }}")
        }
        val speech = audioEntries.filter { it["audioType"] == "speechRecognition" }
        val duplex = speech.filter { ((it["audioInputFormats"] as? Number)?.toLong() ?: 0L) and 0x50L != 0L }
        events.diagnostic(
            when {
                speech.isEmpty() -> "车机未声明 speechRecognition 项：麦克风上行不可能，无需再试"
                duplex.isEmpty() -> "车机声明了 ${speech.size} 个 speechRecognition 项，但 audioInputFormats 不含 0x10/0x40：只能下行音频"
                else -> "车机支持麦克风上行：${duplex.size} 个 speechRecognition 项，可接受 " +
                    describeAudioBits(0x50L)
            },
        )
        // The top-level keys are printed because a field nobody looked for is
        // the usual reason a fix stalls: `features`, a latency cap or an extra
        // stream description can sit here without appearing in any log.
        events.diagnostic("/info 顶层字段 ${info.keys.joinToString(",") { it.toString() }}")
        // Pick the display that can actually show CarPlay. A car may declare
        // more than one (cluster, passenger, main); taking the first blindly
        // risks streaming into a screen nobody is looking at. A display with
        // view areas is the main one, and failing that the largest wins.
        val usable = displays.filter { (it["widthPixels"] as? Number)?.toInt()?.let { w -> w > 0 } == true }
        val display = usable.firstOrNull { (it["viewAreas"] as? List<*>)?.isNotEmpty() == true }
            ?: usable.maxByOrNull {
                ((it["widthPixels"] as? Number)?.toInt() ?: 0).toLong() *
                    ((it["heightPixels"] as? Number)?.toInt() ?: 0)
            }
            ?: error("车机未提供有效显示参数")
        if (usable.size > 1) {
            events.diagnostic(
                "车机声明了 ${usable.size} 个显示项，选用 ${display["widthPixels"]}x" +
                    "${display["heightPixels"]}（uuid=${display["uuid"]}）",
            )
        }
        val area = selectedViewArea(display)
        val safeArea = selectedSafeArea(display, area)
        val negotiated = SenderDisplay(
            number(display["widthPixels"]),
            number(display["heightPixels"]),
            number(display["maxFPS"]).also { require(it in 1..60) },
            display["uuid"] as? String,
            safeArea,
        )
        val selected = events.outputDisplay(negotiated)
        require(selected.width > 0 && selected.height > 0 && selected.width % 2 == 0 && selected.height % 2 == 0)
        if (selected != negotiated) {
            events.diagnostic(
                "输出画布由车机主显示 ${negotiated.width}x${negotiated.height} " +
                    "调整为 ${selected.width}x${selected.height}",
            )
        }
        area?.let {
            events.diagnostic(
                "车机初始 viewArea：${it["widthPixels"]}x${it["heightPixels"]} " +
                    "origin=${it["originXPixels"]},${it["originYPixels"]}",
            )
        }
        safeArea?.let {
            events.diagnostic(
                "车机 safeArea：${it.width}x${it.height}+${it.x},${it.y}；" +
                    "全画布=${selected.width}x${selected.height}",
            )
        } ?: events.diagnostic("车机未提供 safeArea；全画布使用 ${selected.width}x${selected.height}")
        format = selected
        val eventPort = number(setup["eventPort"]); require(eventPort in 1..65535)
        val eventWire = own(RtspWire(own(connect(eventPort)).apply { soTimeout = 0; tcpNoDelay = true }))
        eventWire.cipher = ControlCipher(key("Events-Salt", "Events-Write-Encryption-Key"), key("Events-Salt", "Events-Read-Encryption-Key"))
        run("carplay-events") {
            while (!closed.get()) {
                val command = eventWire.next()
                lastInboundEventNanos = System.nanoTime(); inboundEventCount++
                if (command.status == null) { val reply = handleCommand(command.body); eventWire.respond(command,reply.body,reply.status) }
                else if (command.status in 200..299) receiveIapResponse(command,typedOnly = true)
            }
        }
        // Arming belongs on the event channel: this is the connection the car
        // sends hidSendReport on, and it does not exist until now. Posting it
        // earlier (on the control wire) is what left every device unarmed.
        // The event pump is the sole reader for this socket. Queue arming as a
        // command and let the reader consume its response; calling request()
        // here would race eventWire.next() and steal an unrelated response.
        armedInputs.forEach { uuid ->
            eventWire.sendCommand(
                encode(mapOf("type" to "hidSetInputMode", "params" to mapOf("hidInputMode" to 0, "uuid" to uuid))),
                plistHeaders,
            )
        }
        if (armedInputs.isNotEmpty()) {
            events.diagnostic(
                "hidSetInputMode 已排队 ${armedInputs.size}：${armedInputs.joinToString(",")}；" +
                    "触控设备=${touchDevices.size} 按键设备=${keyDevices.size}",
            )
        }
        wire.onRequest = { handleCommand(it.body) }
        wire.onResponse = { receiveIapResponse(it,typedOnly = true) }
        // `/feedback` is normally a timing response, but the Buick command
        // transport also uses it to deliver delayed iAP2 packets. RtspWire's
        // shared reader marks those CSeqs as ignored; keep the payload path
        // alive so Wi-Fi command authentication cannot stall after 0x1d00.
        wire.onIgnoredResponse = {
            recordAudioFeedback(it)
            receiveIapResponse(it, typedOnly = true)
        }
        synchronized(iapLock) { openingIap = true }
        val seed = SecureRandom().nextLong().and(Long.MAX_VALUE)
        /*
         * No type-130 SETUP. The reference sender never sends one here, and
         * says why in its own trace:
         *
         *   Runtime type130 skipped: vendor compatibility profile; SETUP
         *   rejects types outside 100..111 and may tear down session
         *
         * This car answers our 130 with a 200 and an empty body, which looks
         * like acceptance but is not: measured from RECORD, the control
         * connection is destroyed 13.09-13.16 s later in every single capture.
         * The reference sender, which skips 130 entirely, keeps the same
         * session alive for 110 s.
         *
         * Its SETUP sequence on this car is exactly one screen stream:
         *
         *   SETUP streams=[{type:110, ...}]  (+ optional 101/100 audio later)
         *
         * so the iAP2 control link stays on the authenticated Bluetooth
         * connection and the Wi-Fi command channel is only used for the
         * car's own markers.
         */
        val iapReply: Map<*, *> = emptyMap<Any, Any>()
        val transport = synchronized(iapLock) {
            val selectedTransport = SenderNegotiation.control(iapReply)
            if (selectedTransport.kind == SenderControlKind.AUTHENTICATED_BLUETOOTH) {
                /*
                 * Always the case now: no type-130 SETUP is sent at all, so
                 * there is no reply to negotiate from. Control stays on the
                 * authenticated Bluetooth link, exactly as the reference
                 * sender does on this car.
                 *
                 * [ensureCommandIap] can still attach a command-channel link
                 * from a marker the car sends; that path is gated on the car
                 * advertising iAPChannel, which this one never does.
                 */
                if (earlyIap.isNotEmpty()) {
                    events.diagnostic(
                        "车机已开始发送命令通道 iAP2；链路将由首个标记建立，" +
                            "控制通道沿用已认证蓝牙（iAPChannel=${carEnablesIapChannel}）",
                    )
                }
                selectedTransport.also { openingIap = false }
            } else {
                selectedTransport
            }
        }
        events.diagnostic("控制通道：${transport.kind}（未发送 type 130 SETUP）")
        commandModes = transport.kind != SenderControlKind.WIFI_DATASTREAM
        val tunnel = when (transport.kind) {
            SenderControlKind.WIFI_DATASTREAM -> own(PhoneIapTunnel(own(connect(checkNotNull(transport.port))).apply { tcpNoDelay = true },
                key("DataStream-Salt$seed", "DataStream-Output-Encryption-Key")))
            SenderControlKind.WIFI_COMMAND -> own(PhoneIapTunnel(sendHandler = { data ->
                wire.request("POST", "/command", encode(mapOf(
                    "type" to "iAPSendMessage",
                    "params" to mapOf("data" to data)
                )), plistHeaders) { receiveIapResponse(it,typedOnly = false) }
            }))
            SenderControlKind.AUTHENTICATED_BLUETOOTH -> {
                check(events.bluetoothControlAvailable()) { "车机未接受 type 130，且已认证的蓝牙控制通道不可用" }
                events.diagnostic("车机未接受 type 130；保留实际已认证的蓝牙控制，Wi-Fi 媒体继续使用配对验证后的加密连接")
                null
            }
        }
        synchronized(iapLock) {
            check(!closed.get()) { "iAP 通道建立前会话已关闭" }
            iap = tunnel; openingIap = false
            while (earlyIap.isNotEmpty()) check(tunnel?.offer(earlyIap.removeFirst()) == true) { "启动 iAP 数据未能交接" }
            earlyIapBytes = 0
        }
        val controlReady = tunnel?.let(events::iapControl)
        controlReady?.let { own(Closeable { it.cancel(true) }) }

        if (transport.kind == SenderControlKind.WIFI_DATASTREAM) {
            events.diagnostic("Wi-Fi iAP2 控制通道已建立，等待独立识别和认证后再启动媒体")
            checkNotNull(controlReady).get(45, TimeUnit.SECONDS)
            check(!closed.get()) { "Wi-Fi iAP2 认证完成前连接已停止" }
        }

        if (enableAudio) {
            /*
             * PCM first, matching the 对照实现 sender.
             *
             * `hasHighQualityMediaCapability` used to force AAC ahead of PCM
             * whenever the car declared a type-102 media entry — and this car
             * always does, so the PCM branch was unreachable in practice. The
             * reference sender that stays connected drives type=100 instead, so
             * the capability gate no longer overrides the order. The value is
             * still computed and logged, because a head unit that refuses PCM
             * is the case the AAC fallback exists for.
             */
            val aacCapable = hasHighQualityMediaCapability(info)
            val preferAacMedia = false
            events.diagnostic(
                "音频格式优先级：PCM(100) 优先，AAC(102) 作为回退" +
                    "（车机声明 media AAC=$aacCapable）",
            )
            val candidates = SenderNegotiation.audioCandidates(info, preferAacMedia = preferAacMedia)
            val ordered = audioProfileOrder?.invoke(candidates).orEmpty().ifEmpty { candidates }
            selectedAudio = ordered.firstOrNull()
                ?: error("车机未声明可发送的媒体格式（type 100/102、44.1/48 kHz）；请关闭音频重试")
            selectedAudio!!.also {
                events.diagnostic(
                    "音频协商：type=${it.streamType}，${if (it.isPcm) "LPCM" else "AAC-LC"} " +
                        "${it.sampleRate} Hz / ${it.channels} 声道，" +
                        "按车机 audioOutputFormats 选择${if (preferAacMedia) "（media AAC 优先）" else ""}" +
                        "；候选=${ordered.joinToString("/") { candidate -> candidate.key }}",
                )
                events.audioFormat(it)
            }
        }
        // The reference Buick-E5 flow declares ownership before screen SETUP,
        // prepares the source, then starts its encoder only after RECORD.
        //
        // The screen has to be claimed, not assumed. This car's /info starts
        // with the screen owned by the head unit (initialPermanentEntity 2), so
        // a snapshot that leaves it there is telling the car the phone does not
        // want to display — and the video worker drops every frame while
        // ownsScreen() is false. Claiming here is what makes the picture appear
        // at all; the car can still take the screen back later through
        // changeModes, which is handled separately.
        if (modes.claimScreen()) {
            events.diagnostic("会话开始：已申请屏幕资源由手机显示")
        }
        if (enableAudio && !modes.ownsAudio()) setAudioOwnership(true)
        wire.request("POST", "/command", encode(modes.snapshot()), plistHeaders)
        events.diagnostic(
            "初始资源所有权已声明：屏幕=${if (modes.ownsScreen()) "手机" else "车机"}，" +
                "音频=${if (modes.ownsAudio()) "手机" else "车机"}",
        )
        events.display(selected)
        val connection = SecureRandom().nextLong().and(Long.MAX_VALUE)
        /*
         * Screen SETUP shape: latencyMs, uuid, displayWidth/displayHeight.
         *
         * displayWidth/displayHeight were removed in 1.2.75 to match the
         * reference sender's four-key body, then put back here: that change had
         * no on-car verification behind it, and 1.2.75 also changed the iAP2
         * bootstrap at the same time, so a regression could not be attributed
         * to either one. Both keys were present in every build that produced a
         * picture on this car. Removing them is a separate experiment, to be
         * run alone once the session is known to survive.
         *
         * The key is `uuid`, not `displayUUID` — the latter is the shape a
         * different Delphi firmware uses, and sending it here left the car
         * without the display it was supposed to place the picture on.
         */
        val stream = linkedMapOf<String, Any>(
            "type" to 110,
            "streamConnectionID" to connection,
            "latencyMs" to SCREEN_LATENCY_MS,
            "displayWidth" to selected.width,
            "displayHeight" to selected.height,
        )
        selected.uuid?.let { stream["uuid"] = it }
        val reply = streams(wire.request("SETUP", "/", encode(mapOf("streams" to listOf(stream))), plistHeaders).body)
        val screen = reply.firstOrNull { number(it["type"]) == 110 } ?: error("车机拒绝视频流")
        val dataPort = number(screen["dataPort"]); require(dataPort in 1..65535)
        events.diagnostic(
            "屏幕 SETUP 已接受：type=110，画布=${selected.width}x${selected.height}，dataPort=$dataPort",
        )
        videoKey = key("DataStream-Salt$connection", "DataStream-Output-Encryption-Key")
        video = own(connect(dataPort))
        events.diagnostic("视频 dataPort 已连接：$dataPort")
        videoFramesSinceKeepAlive = 0
        videoBytesSinceKeepAlive = 0
        videoKeepAliveCount = 0
        videoStreamReady = false
        // Activate the screen before the optional audio SETUP. GM firmware keeps a
        // short screen-session watchdog; delaying RECORD behind a slow audio setup
        // can reset the whole session before the first video packet arrives.
        wire.request("RECORD", "/")
        events.diagnostic("屏幕 RECORD 已完成，等待 startDisplay 启动来源编码器")
        recordCompletedNanos = System.nanoTime()
        startDisconnectHeartbeat()
        val keepAliveWithStats = (info["keepAliveSendStatsAsBody"] == true) ||
            ((info["keepAliveSendStatsAsBody"] as? Number)?.toInt() == 1)
        startVideoKeepAlive(keepAliveWithStats)
        // Buick-E5 starts its encoder only after the screen session is active.
        // The source was prepared above so a failed capture still aborts before
        // SETUP/RECORD; this callback is the actual CarLife VIDEO_ENCODER_INIT.
        events.startDisplay(selected)
        if (enableAudio) {
            setupAudio(wire, checkNotNull(selectedAudio), shared, socket.inetAddress)
            /*
             * Claim the audio resource now that the stream exists.
             *
             * The initial snapshot is declared before the screen SETUP, when the
             * car has no audio stream to hand over yet; it keeps audio for
             * itself. Until this app owns resource 2 every captured chunk is
             * dropped at enqueue, which is what "sound came out once and then
             * nothing" looks like from the driver's seat. Declaring again here
             * puts the claim in front of a car that can actually answer it.
             */
            runCatching {
                synchronized(audioQueueLock) {
                    if (!modes.ownsAudio()) {
                        setAudioOwnership(true)
                        events.diagnostic("音频流已建立，重新声明音频资源由手机播放")
                    }
                }
                wire.request("POST", "/command", encode(modes.snapshot()), plistHeaders)
            }.onFailure {
                events.diagnostic("音频所有权声明失败：${it.message}")
            }
            audioProfileSetupComplete = true
            audioProfileStable = false
            /*
             * A SETUP/200 only proves that the receiver accepted the
             * descriptor.  The Buick traces show that a bad audio profile can
             * still tear down the whole session several seconds later, so do
             * not mark it working until the session has survived the same
             * observation window used by the app's connection proof — *and*
             * the car has actually consumed audio in that window.
             *
             * Surviving alone is not enough: this head unit tears the session
             * down 13.1 s after RECORD no matter what is negotiated, so a
             * profile can outlive a short window without a single packet
             * having been rendered. `audioPacketsSent` is incremented by the
             * audio worker on every RTP write, so requiring it to have grown
             * separates "the car accepted the parameters" from "the car
             * played something", which is the only distinction that makes a
             * remembered profile worth promoting.
             */
            val stableProfile = checkNotNull(selectedAudio)
            val packetsAtSetup = audioPacketsSent
            run("carplay-audio-profile-stable") {
                Thread.sleep(AUDIO_PROFILE_STABLE_WINDOW_MS)
                if (!closed.get()) {
                    audioProfileStable = true
                    val flowed = audioPacketsSent - packetsAtSetup
                    if (flowed > 0L) {
                        audioProfileWorking?.invoke(stableProfile)
                        events.diagnostic(
                            "音频档位已确认可用：${stableProfile.key}" +
                                "（${AUDIO_PROFILE_STABLE_WINDOW_MS / 1000} 秒内发出 $flowed 个包）",
                        )
                    } else {
                        events.diagnostic(
                            "音频档位未记为可用：${stableProfile.key} 在" +
                                "${AUDIO_PROFILE_STABLE_WINDOW_MS / 1000} 秒内没有发出任何音频包",
                        )
                    }
                }
            }
            // The stream is added after RECORD; a second RECORD is required to
            // activate it without delaying the screen watchdog.
            wire.request("RECORD", "/")
            startAudioWorker()
        }

        run("carplay-video-send") {
            var counter = 0L; var sentConfig: ByteArray? = null; var basePts: Long? = null; var baseNtp = 0L
            while (!closed.get()) {
                val frame = queue.take() ?: break
                // A handoff can reset the queue after this worker has already
                // dequeued a frame. Keep the epoch check and both media writes
                // in one queue critical section so reset cannot land between
                // the check and output.write().
                val sentBytes = queue.withCurrent(frame) {
                    if (!modes.ownsScreen()) return@withCurrent null
                    val config = avcC ?: return@withCurrent null
                    synchronized(videoWriteLock) {
                        val output = checkNotNull(video).getOutputStream()
                        if (!modes.ownsScreen()) return@withCurrent null
                        if (basePts == null) { basePts = frame.ptsUs; baseNtp = clock.syncedNtp().toLong() }
                        val elapsed = frame.ptsUs - basePts!!; require(elapsed >= 0)
                        // The preamble timestamp is computed first: the codec
                        // config packet has to carry the same value as the
                        // frame it introduces. UxPlay discards an SPS/PPS whose
                        // timestamp does not match the following payload's
                        // ("prepended sps_pps timestamp does not match
                        // timestamp of video payload ... discarding"), so the
                        // two writes cannot be ordered the other way round.
                        val ntp = baseNtp + (elapsed / 1_000_000 shl 32) + ((elapsed % 1_000_000) * 4_294_967_296L / 1_000_000)
                        if (configPending) {
                            resendConfig = false
                            val packet = VideoPackets.config(config, selected.width, selected.height, ntp)
                            output.write(packet)
                            sentConfig = config.copyOf()
                            configPending = false
                            videoStreamReady = true
                            events.diagnostic(
                                "视频 CONFIG 已写出：generation=$configGeneration，" +
                                    "大小=${config.size}B，恢复轮次 #$videoRecoveryRound，" +
                                    VideoPackets.describeConfig(packet),
                            )
                        } else if (sentConfig?.contentEquals(config) != true || resendConfig) {
                            resendConfig = false
                            val packet = VideoPackets.config(config, selected.width, selected.height, ntp)
                            output.write(packet)
                            sentConfig = config.copyOf()
                            videoStreamReady = true
                            events.diagnostic(
                                "视频 CONFIG 已重写：generation=$configGeneration，" +
                                    "大小=${config.size}B，恢复轮次 #$videoRecoveryRound，" +
                                    VideoPackets.describeConfig(packet),
                            )
                        }
                        val packet = VideoPackets.frame(
                            frame.bytes, checkNotNull(videoKey), counter++, ntp, frame.keyFrame,
                        )
                        output.write(packet); output.flush()
                        if (frame.keyFrame) {
                            events.diagnostic(
                                "视频首个/恢复 IDR 已写出：恢复轮次 #$videoRecoveryRound，" +
                                    "epoch=${frame.epoch}，payload=${frame.bytes.size}B，" +
                                    VideoPackets.describeFrame(packet),
                            )
                        }
                        videoStreamReady = true
                        videoFramesSinceKeepAlive++
                        videoBytesSinceKeepAlive += frame.bytes.size.toLong()
                        packet.size
                    }
                }
                if (sentBytes == null) {
                    events.diagnostic("丢弃屏幕切换期间取出的旧视频帧：epoch=${frame.epoch}")
                    continue
                }
                events.sent(sentBytes, System.nanoTime() - frame.queuedAtNanos)
            }
        }
        run("carplay-feedback") {
            while (!closed.get()) {
                try {
                    if (transport.kind == SenderControlKind.WIFI_COMMAND) {
                        /*
                         * The command transport can carry delayed iAP2
                         * packets in the feedback response, so this mode
                         * needs a real reader immediately.
                         */
                        wire.request("POST", "/feedback") {
                            receiveIapResponse(it, typedOnly = true)
                        }
                    } else {
                        /*
                         * On the dedicated data-stream transport the main
                         * RTSP request is already the reader and consumes
                         * ignored feedback responses while SETUP is in
                         * flight. Keep this write non-blocking so a slow
                         * receiver cannot hold the request monitor.
                         *
                         * The reply is still read (through the ignored-response
                         * path) because it is the only evidence the car is
                         * consuming audio: each entry carries the receiver's
                         * sampleTime, which stays at zero for a stream the car
                         * has opened but never plays. Audio started and the
                         * session died four seconds later in every capture, and
                         * without this number there is no way to tell a broken
                         * audio clock from a car that simply will not decode the
                         * stream.
                         */
                        wire.sendAndForget("POST", "/feedback")
                        feedbackCount += 1
                        if (feedbackCount <= 3 || feedbackCount % 10 == 0L) {
                            events.diagnostic(
                                "已发送 /feedback #$feedbackCount" +
                                    "（视频帧=${videoFramesSinceKeepAlive}，" +
                                    "音频包=${audioPacketsSent}）",
                            )
                        }
                        reportAudioFeedback()
                    }
                } catch (error: Exception) {
                    if (!closed.get()) throw error
                    break
                }
                if (!closed.get()) Thread.sleep(1000)
            }
        }
        if (transport.kind == SenderControlKind.WIFI_COMMAND) {
            events.diagnostic("Wi-Fi 媒体已 RECORD，等待 Wi-Fi iAP2 独立识别和认证完成")
            checkNotNull(controlReady).get(45, TimeUnit.SECONDS)
            check(!closed.get()) { "Wi-Fi iAP2 认证完成前连接已停止" }
        }

        return selected
    }

    /**
     * SETUP's UDP keepAlivePort is independent of RTSP feedback and the
     * screen-stream keep-alive. The receiver watchdog expects a fresh random
     * four-byte datagram roughly every five seconds.
     */
    private fun startAirPlayKeepAlive(remote: InetAddress, port: Int) {
        val udp = own(DatagramSocket(null))
        bindDatagram(udp)
        localAddress()?.let { udp.bind(InetSocketAddress(it, 0)) }
        run("carplay-udp-keepalive") {
            val random = SecureRandom()
            try {
                var count = 0L
                while (!closed.get() && !udp.isClosed) {
                    val payload = ByteArray(4).also(random::nextBytes)
                    udp.send(DatagramPacket(payload, payload.size, remote, port))
                    count++
                    if (count <= 2 || count % 60 == 0L) {
                        events.diagnostic(
                            "AirPlay UDP keepAlive #$count → ${remote.hostAddress}:$port" +
                                "（间隔 ${AIRPLAY_KEEPALIVE_INTERVAL_MS}ms）",
                        )
                    }
                    Thread.sleep(AIRPLAY_KEEPALIVE_INTERVAL_MS)
                }
            } catch (_: InterruptedException) {
                // Session close interrupts workers after closing owned sockets.
            } catch (error: Exception) {
                if (!closed.get()) events.diagnostic("AirPlay UDP keepAlive 结束：${error.message}")
            }
        }
    }

    /**
     * One decimal place, without a format string.
     *
     * `String.format("%.1f", ...)` in a diagnostic path is a liability: it is
     * the exact call that threw `IllegalFormatConversionException` on 1.3.3's
     * first heartbeat and killed the session. Rounding a non-negative duration
     * is arithmetic, so do the arithmetic.
     */
    private fun oneDecimal(value: Double): String {
        // 截断而非四舍五入：这是诊断用的时长，显示 2.9s 而实际是 2.84s 会让
        // 人以为是两个不同的时刻。Math.round 在 .05 这类值上还会向下取整，
        // 结果更难解释。
        val tenths = (value.coerceAtLeast(0.0) * 10.0).toLong()
        return "${tenths / 10}.${tenths % 10}"
    }

    /**
     * Records, once a second, whether the car is still talking to us.
     *
     * The session is destroyed 13.1 s after RECORD on every build measured. What
     * no capture has shown yet is *when the car stopped caring*, because the
     * journal only prints the last four seconds as a stack trace. This thread
     * makes the shape visible:
     *
     *     RECORD 心跳 #3 +3.0s：车机静默 3.0s，事件通道累计=0 命令通道累计=0
     *
     * If those counters stay at zero for the whole session, then this car never
     * speaks on either Wi-Fi channel once RECORD is in — the marker it sends
     * every second is *our* expectation, not an observation — and the 13.1 s
     * timer is tearing down a session the car decided was one-way. If they grow
     * and then stop, the last value before the silence is the thing to explain.
     */
    private fun startDisconnectHeartbeat() {
        val recordedAt = recordCompletedNanos
        run("carplay-heartbeat") {
            try {
                while (!closed.get()) {
                    Thread.sleep(1000)
                    if (closed.get()) break
                    heartbeatCount++
                    val now = System.nanoTime()
                    /*
                     * Built by concatenation, not String.format.
                     *
                     * 1.3.3 shipped this line as a format string and it threw
                     * `IllegalFormatConversionException: d != java.lang.Double`
                     * on the first tick, which took the worker down and with it
                     * the whole AirPlay session at 8.3 s — the heartbeat built
                     * to diagnose a disconnect caused one. There is no reason to
                     * route a dozen counters through a format string that can
                     * fail at runtime; the values are all read once here anyway.
                     */
                    fun silence(at: Long): String =
                        if (at == 0L) "从未" else oneDecimal((now - at) / 1_000_000_000.0) + "s"
                    events.diagnostic(
                        "RECORD 心跳 #$heartbeatCount +${oneDecimal((now - recordedAt) / 1_000_000_000.0)}s：" +
                            "车机静默 事件=${silence(lastInboundEventNanos)}(共$inboundEventCount) " +
                            "命令=${silence(lastInboundIapNanos)}(共$inboundIapCount)；" +
                            "本端视频帧=$videoKeepAliveCount 音频包=$audioPacketsSent " +
                            "/feedback=$feedbackCount " +
                            "屏幕=${if (modes.ownsScreen()) "手机" else "车机"} " +
                            "音频=${if (modes.ownsAudio()) "手机" else "车机"}",
                    )
                }
            } catch (_: InterruptedException) {
                // Session close interrupts this worker.
            }
        }
    }

    /**
     * Keep the video TCP stream alive even while the CarLife source is waiting
     * for a fresh SPS/PPS/IDR after a screen handover.
     */
    private fun startVideoKeepAlive(withStats: Boolean) {
        run("carplay-video-keepalive") {
            try {
                // Preserve CONFIG/frame ordering. Once the first frame epoch
                // is installed, this also covers handovers where the source
                // is waiting for a fresh IDR.
                while (!closed.get() && !videoStreamReady) Thread.sleep(25)
                while (!closed.get()) {
                    Thread.sleep(1000)
                    if (closed.get()) break
                    val socket = video ?: break
                    synchronized(videoWriteLock) {
                        if (closed.get() || socket.isClosed) return@synchronized
                        val frameCount = videoFramesSinceKeepAlive
                        val byteCount = videoBytesSinceKeepAlive
                        videoFramesSinceKeepAlive = 0
                        videoBytesSinceKeepAlive = 0
                        val packet = VideoPackets.keepAlive(
                            withStats = withStats,
                            framesPerSecond = frameCount,
                            sentFrames = frameCount,
                            bytesPerSecond = byteCount.toDouble(),
                        )
                        socket.getOutputStream().write(packet)
                        socket.getOutputStream().flush()
                        videoKeepAliveCount++
                        if (videoKeepAliveCount <= 3 || videoKeepAliveCount % 30 == 0L) {
                            events.diagnostic(
                                "视频 TCP keepAlive #$videoKeepAliveCount " +
                                    "opcode=${if (withStats) 5 else 2} frames=$frameCount",
                            )
                        }
                    }
                }
            } catch (_: InterruptedException) {
                // Session close interrupts workers after closing owned sockets.
            } catch (error: Exception) {
                if (!closed.get()) events.diagnostic("视频 TCP keepAlive 结束：${error.message}")
            }
        }
    }

    /**
     * Opens the media audio stream.
     *
     * The SETUP body carries the format and scheduling description the car
     * needs to play it. AAC follows the Buick iPhone's compact request shape;
     * PCM and the compatibility fallback retain the expanded fields used by
     * older receivers.
     */
    private fun setupAudio(wire: RtspWire, format: SenderAudioFormat, shared: ByteArray, address: InetAddress) {
        val id = SecureRandom().nextLong().and(Long.MAX_VALUE)
        val local = own(DatagramSocket(null)); bindDatagram(local); local.bind(InetSocketAddress(localAddress(),0))
        /*
         * Two descriptor shapes, one stream per SETUP.
         *
         * Shape A is the iPhone's own request for MainHighAudio, recovered from
         * the Buick sysdiagnose for this exact head unit. It carries exactly
         * six keys and no others:
         *
         *     controlPort, spf, audioFormat,
         *     supportsHighAccuracyTimestamps, audioType, audioLatencyMs
         *
         * Shape B is what a-sender (github.com/sender/a-sender) sends to
         * receivers that reject shape A: the stream is described completely
         * with type, streamConnectionID, input and
         * supportsRTPPacketRedundancy. Its notes record that a receiver may
         * treat an absent `input` as "this stream cannot be used".
         *
         * The two are mutually exclusive, not additive. Adding shape B's extra
         * keys on top of shape A describes a different stream than the one the
         * iPhone brings up, and a strict receiver rejects the SETUP outright
         * instead of ignoring what it does not know. Shape A is tried first
         * because it is the only shape verified on this car; shape B is the
         * fallback for firmware that wants the explicit form.
         *
         * Both are sent as a single-entry "streams" array. An earlier build
         * packed the main stream plus two hard-coded Opus placeholders (types
         * 101 and 106) into one SETUP; this car answered with only the 101 and
         * 106 entries, dropping the main stream.
         */
        fun descriptor(streamType: Int, audioFormat: Long, legacy: Boolean, audioType: String = "media"): Map<String, Any> = linkedMapOf<String, Any>().apply {
            if (legacy) {
                // Shape B — the explicit form. Kept as the fallback for
                // receivers that want `supportsRTPPacketRedundancy`.
                put("type", streamType)
                put("streamConnectionID", id)
                put("audioFormat", audioFormat)
                put("audioType", audioType)
                put("audioLatencyMs", if (format.isPcm) PCM_LATENCY_MS else AAC_LATENCY_MS)
                put("input", false)
                put("supportsRTPPacketRedundancy", false)
                put("supportsHighAccuracyTimestamps", true)
                put("spf", format.samplesPerPacket)
                put("ct", if (format.isPcm) 1 else 4)
                put("sr", format.sampleRate)
            } else {
                /*
                 * Shape A — 对照实现's audio SETUP, recovered from 0.3.19.
                 *
                 * Its `C0281k1` builds exactly six keys:
                 *
                 *     { type:100, streamConnectionID, audioFormat, audioType,
                 *       audioLatencyMs, supportsHighAccuracyTimestamps:TRUE }
                 *
                 * with the profile `new S0(48000, 32768L, 250L, "media",
                 * <source>)` — 48 kHz, 0x8000 (PCM stereo), 250 ms.
                 *
                 * That sender runs on this exact car, so its shape is the
                 * reference even where the iPhone capture disagrees: the phone
                 * sends four keys with `audioLatencyMs: 32`, while 对照实现
                 * sends six with 250 and `streamConnectionID`. Sending the
                 * phone's five-key body was worth trying, but a sender known to
                 * hold this car open beats a different sender on different
                 * hardware — and the phone's own capture is from a session that
                 * also ran over USB, where several of these fields come from
                 * the accessory instead.
                 *
                 * `supportsHighAccuracyTimestamps` is `Boolean.TRUE` there, not
                 * false: 对照实现 always asks for timestamp alignment.
                 */
                put("type", 100)
                put("streamConnectionID", id)
                put("audioFormat", audioFormat)
                put("audioType", audioType)
                put("audioLatencyMs", if (format.isPcm) PCM_LATENCY_MS else AAC_LATENCY_MS)
                put("supportsHighAccuracyTimestamps", true)
            }
        }

        fun buildMultiStreamSetup(legacy: Boolean): List<Map<String, Any>> =
            listOf(descriptor(format.streamType, format.formatBits, legacy, "media"))

        // Which shape the car took is recorded, because it is the only evidence
        // that can settle whether `controlPort` was ever the reason the car
        // accepted audio and stayed silent. Without this line a working session
        // and a silent one produce identical logs.
        var usedLegacy = false
        // Log the exact descriptors sent, and why the first attempt failed.
        // Without this the only visible symptom is a bare "SETUP / → 400" with
        // no way to tell which stream the car objected to or what it was told.
        val primarySetup = buildMultiStreamSetup(false)
        events.diagnostic(
            "音频 SETUP 请求：${primarySetup.joinToString(" | ") { stream ->
                "type=${stream["type"]} audioType=${stream["audioType"]} " +
                    "fmt=0x${(stream["audioFormat"] as? Number)?.toLong()?.toString(16) ?: "-"} " +
                    "spf=${stream["spf"] ?: "-"}"
            }}",
        )
        val accepted = runCatching {
            val replyStreams = streams(
                wire.request("SETUP", "/", encode(mapOf("streams" to primarySetup)), plistHeaders).body,
            )
            // Report what came back before picking one: a 200 whose streams do
            // not carry the type we asked for looks identical to a rejection
            // unless the reply is in the log.
            events.diagnostic(
                "音频 SETUP 回包：${replyStreams.joinToString(" | ") { stream ->
                    "type=${number(stream["type"])} keys=${stream.keys.joinToString(",")}"
                }}",
            )
            /*
             * Pick the entry that carries a data port.
             *
             * This car answers the reference SETUP with a type=101 and a
             * type=106 entry, both with streamConnectionID and dataPort, and
             * with no type=102 entry at all — the main AAC stream is allocated
             * silently and never echoed. Requiring the reply to name our
             * stream type therefore declared a perfectly good 200 a failure and
             * re-sent the same request in the legacy shape, which this car
             * answers with 400. The transport is the data port: whichever entry
             * carries one is the stream to play into.
             */
            replyStreams.firstOrNull { number(it["type"]) == format.streamType }
                ?: replyStreams.firstOrNull { number(it["dataPort"]) in 1..65535 }
        }.onFailure {
            events.diagnostic("音频 SETUP 参考字段集被拒：${it.message}")
        }.getOrNull() ?: run {
            usedLegacy = true
            events.diagnostic("音频 SETUP 未接受参考字段集，回退为含 controlPort 的旧字段集")
            val legacyStreams = streams(
                wire.request("SETUP", "/", encode(mapOf("streams" to buildMultiStreamSetup(true))), plistHeaders).body,
            )
            events.diagnostic(
                "音频 SETUP 回包（旧字段集）：${legacyStreams.joinToString(" | ") { stream ->
                    "type=${number(stream["type"])} keys=${stream.keys.joinToString(",")}"
                }}",
            )
            legacyStreams.firstOrNull { number(it["type"]) == format.streamType }
        } ?: error("车机拒绝协商后的音频流")
        events.diagnostic(
            "音频 SETUP 已接受：${if (usedLegacy) "旧字段集（含 controlPort）" else "参考字段集（无 controlPort）"}（已发送多流描述防 30s 超时），" +
                "车机回包字段 ${accepted.keys.joinToString(",") { it.toString() }}",
        )
        val port = number(accepted["dataPort"]); require(port in 1..65535)
        audioAddress = InetSocketAddress(address, port); audioSocket = local
        aacTimestampClock = if (format.isPcm) null else MonotonicAudioTimestamp(format)
        lastAudioTimestamp = null
        nextAudioTimestamp = null
        audioPacketsSent = 0L
        val controlPort = (accepted["controlPort"] as? Number)?.toInt()?.and(0xffff) ?: 0
        // MainHighAudio responses carry the streamConnectionID generated by
        // the receiver. The iPhone capture uses that returned value (including
        // its unsigned decimal representation) for the data-stream key; using
        // the request-side random id here silently encrypts packets with the
        // wrong salt on receivers that allocate the id themselves.
        val responseId = accepted["streamConnectionID"] ?: id
        val responseSeed = unsignedPlistDecimal(responseId) ?: id.toString()
        audioKey = AirPlayCrypto.hkdfSha512(
            shared,
            "DataStream-Salt$responseSeed".toByteArray(),
            "DataStream-Output-Encryption-Key".toByteArray(),
        )
        events.diagnostic(
            "音频流已建立：type=${format.streamType} ${format.codec} ${format.sampleRate}Hz/" +
                "${format.channels}声道 spf=${format.samplesPerPacket} port=$port " +
                "audioLatencyMs=${if (format.isPcm) PCM_LATENCY_MS else AAC_LATENCY_MS} " +
                "inputBufferSamples=${if (format.isPcm) PCM_INPUT_BUFFER_SAMPLES else AAC_INPUT_BUFFER_SAMPLES} " +
                "outputBufferSamples=${if (format.isPcm) PCM_OUTPUT_BUFFER_SAMPLES else AAC_OUTPUT_BUFFER_SAMPLES} " +
                "streamConnectionID=$responseSeed",
        )
        events.diagnostic(
            "音频参数对照（对照实现 同款）：每包 ${format.samplesPerPacket} 帧" +
                "（${"%.2f".format(1000.0 * format.samplesPerPacket / format.sampleRate)} ms）、" +
                "延迟 ${if (format.isPcm) PCM_LATENCY_MS else AAC_LATENCY_MS} ms、" +
                "RTP Marker=固定 0、音频 RTCP=不发送",
        )
        /*
         * Do not send RTP↔NTP sync packets.
         *
         * Every session logged this, never once successfully:
         *
         *     16029 音频同步包暂时发不出去（第 1 次）：sendto failed:
         *           ECONNREFUSED (Connection refused)；继续重试
         *
         * The car hands back a controlPort in the audio SETUP reply but never
         * listens on it here, so the connected UDP socket draws ICMP port
         * unreachable on every attempt. The reference sender never sends these
         * on this car at all — its audio SETUP reply carries no controlPort
         * and its trace has no sync traffic.
         *
         * A datagram stream to a closed port during the receiver's watchdog
         * window is pure downside: no anchoring effect, and one more thing the
         * connection can be reset over.
         */
        if (controlPort in 1..65535) {
            events.diagnostic(
                "车机回包带 controlPort=$controlPort，但本实现不发送 RTP↔NTP 同步包" +
                    "（参考实现在本车也无法送达，持续 ECONNREFUSED）",
            )
        } else {
            events.diagnostic("车机音频回包未提供 controlPort：跳过 RTP↔NTP 同步包")
        }
        /*
         * Do NOT open the companion stream on this transport.
         *
         * 1.2.76 started one unconditionally and every session paid for it:
         *
         *     6616 第二条音频流 SETUP 失败：车机拒绝 SETUP /：400
         *
         * The car answers 400 to a second audio SETUP in the same session. The
         * iPhone's second stream exists (AudioAlt, Opus) but its trace shows it
         * brought up LATER and torn down after ~1.2 s — it is a separate
         * session-scoped stream, not a second SETUP appended to ours.
         *
         * Sending it costs a rejected SETUP mid-session, and a 400 during the
         * screen stream's watchdog window is exactly the kind of traffic the
         * receiver resets a connection over. Leave it out until the reference
         * sender's timing can be reproduced properly.
         */
    }

    /**
     * The `(audioFormat, samplesPerPacket)` pair for the companion stream.
     *
     * Taken from the iPhone sysdiagnose for this head unit, which is the only
     * source that shows both streams' negotiated formats:
     *
     *   AudioMain  MainHighAudio/media, AAC, 48000 Hz, Stereo, 1024 spf, 0x00800000
     *   AudioAlt   AltAudio/default,  Opus, 48000 Hz, Mono,     960 spf, 0x40000000
     *
     * `0x40000000` is Opus 48 kHz mono in the `audioOutputFormats` bit space
     * this car advertises, and it is exactly what /info lists for its
     * `type=101 audioType=default` entry.
     *
     * When the main stream is not AAC the companion falls back to the same
     * LPCM shape the receiver accepted for type 100, because a receiver that
     * took LPCM for the main stream will not take Opus here.
     */
    /**
     * The `(audioFormat, samplesPerPacket)` pair for the companion stream.
     *
     * Prefer what THIS car declared for type 101 in /info. The E2LB lists
     * `type=101 audioType=default output=0x40008000` (PCM48k/2 + OPUS48k/1),
     * and 0x40000000 (Opus 48k mono) is the subset the iPhone actually uses.
     * Both bits are accepted; the declared mask is the safer one to echo.
     *
     * The iPhone's own framing for that stream is 960 samples/packet.
     */
    private fun companionFormat(main: SenderAudioFormat): Pair<Long, Int> = when {
        main.codec == com.carplay.airplay.AudioCodecKind.AAC_LC ->
            declaredCompanionFormatBits() to COMPANION_OPUS_SAMPLES_PER_PACKET
        else -> main.formatBits to main.samplesPerPacket
    }

    /**
     * The `audioOutputFormats` mask this car declared for its type 101 entry,
     * or the iPhone's 0x40000000 when /info did not carry one.
     */
    private fun declaredCompanionFormatBits(): Long {
        val entries = (sessionInfo["audioFormats"] as? List<*>)
            ?.mapNotNull { it as? Map<*, *> }
            .orEmpty()
        val entry = entries.firstOrNull {
            (it["type"] as? Number)?.toInt() == 101 &&
                (it["audioType"] == null || it["audioType"] == "default")
        } ?: return COMPANION_OPUS_FORMAT
        val raw = entry["audioOutputFormats"]
        val bits = when (raw) {
            is Number -> raw.toLong()
            is String -> raw.toLongOrNull(16) ?: 0L
            else -> 0L
        }
        return if (bits != 0L) bits else COMPANION_OPUS_FORMAT
    }

    /**
     * Opens the second LPCM stream (the one the primary SETUP does not cover).
     *
     * Failures are reported and swallowed: the primary stream is already
     * running, and losing the companion must not take the session down.
     */
    private fun setupCompanionAudio(
        wire: RtspWire,
        format: SenderAudioFormat,
        shared: ByteArray,
        address: InetAddress,
    ) {
        /*
         * Which stream to pair with the main one.
         *
         * The iPhone on this car runs MainHighAudio (102, AAC) alongside
         * AltAudio (101, Opus). Only type 100 and 101 were handled here, so a
         * session that negotiated the AAC main stream — which is every session
         * on this car — returned immediately and the companion was never
         * brought up. That is why 1.2.76's "unconditional" call changed
         * nothing: the early return was on the stream type, not on the caller.
         */
        val companionType = when (format.streamType) {
            100 -> 101
            101 -> 100
            // AAC main stream (102) pairs with AltAudio.
            102 -> 101
            else -> return
        }
        val companionAudioType = if (companionType == 101) "default" else "media"
        /*
         * The companion does not reuse the main stream's format.
         *
         * The iPhone sysdiagnose for this car shows the two streams carrying
         * different codecs and different framing:
         *
         *   AudioMain  MainHighAudio/media, AAC, 48000 Hz, Stereo,
         *              1024 samples/packet, 0x00800000, IL=48000 OL=57552
         *   AudioAlt   AltAudio/default,  Opus, 48000 Hz, Mono,
         *              960 samples/packet, 0x40000000, IL=3840 OL=3840
         *
         * Only the main stream is the AAC media path. The alternate is a
         * separate, narrower Opus stream — it is not "the same PCM on a second
         * port". Sending it the main stream's audioFormat/spf describes a
         * stream the receiver never asked for.
         */
        val companion = companionFormat(format)
        val companionSamplesPerPacket = companion.second
        val companionFormatBits = companion.first
        val id = SecureRandom().nextLong().and(Long.MAX_VALUE)
        val local = runCatching {
            DatagramSocket(null).also {
                bindDatagram(it)
                it.bind(InetSocketAddress(localAddress(), 0))
            }
        }.getOrElse {
            events.diagnostic("第二条音频流套接字创建失败：${it.message}")
            return
        }
        own(local)
        val descriptor = linkedMapOf<String, Any>(
            "type" to companionType,
            "audioType" to companionAudioType,
            "audioFormat" to companionFormatBits,
            "streamConnectionID" to id,
            // JBL = 80 ms for AltAudio in the iPhone capture, against 1000 ms
            // for MainHighAudio. The two streams are scheduled independently.
            "audioLatencyMs" to COMPANION_LATENCY_MS,
            "spf" to companionSamplesPerPacket,
            "sr" to format.sampleRate,
            "ct" to 1,
            "input" to false,
        )
        val reply = runCatching {
            streams(
                wire.request(
                    "SETUP", "/",
                    encode(mapOf("streams" to listOf(descriptor))),
                    plistHeaders,
                ).body,
            )
        }.onFailure {
            events.diagnostic("第二条音频流 SETUP 失败：${it.message}")
        }.getOrNull() ?: return
        val accepted = reply.firstOrNull { number(it["dataPort"]) in 1..65535 }
        if (accepted == null) {
            events.diagnostic(
                "第二条音频流未被接受：回包=${reply.joinToString(" | ") { s ->
                    "type=${number(s["type"])} keys=${s.keys.joinToString(",")}"
                }}",
            )
            return
        }
        val port = number(accepted["dataPort"])
        val responseSeed = unsignedPlistDecimal(accepted["streamConnectionID"] ?: id) ?: id.toString()
        val key = AirPlayCrypto.hkdfSha512(
            shared,
            "DataStream-Salt$responseSeed".toByteArray(),
            "DataStream-Output-Encryption-Key".toByteArray(),
        )
        synchronized(audioQueueLock) {
            secondaryAudio += SecondaryAudio(
                format = format,
                socket = local,
                address = InetSocketAddress(address, port),
                key = key,
            )
        }
        events.diagnostic(
            "第二条音频流已建立：type=$companionType audioType=$companionAudioType " +
                "port=$port spf=${format.samplesPerPacket} audioLatencyMs=$PCM_LATENCY_MS " +
                "streamConnectionID=$responseSeed",
        )
    }

    /**
     * The Buick/iPhone path emits one RTP-to-NTP anchor per second on the
     * receiver-provided audio control port. Do not send a zero-to-zero probe:
     * some firmware treats it as an invalid pre-RECORD stream and tears down
     * an otherwise healthy audio session.
     */
    private fun startAudioSync(
        address: InetAddress,
        controlPort: Int,
        format: SenderAudioFormat,
    ) {
        audioSyncThread?.let { if (it.isAlive) return }
        val socket = runCatching {
            DatagramSocket(null).also {
                bindDatagram(it)
                it.bind(InetSocketAddress(localAddress(), 0))
                it.connect(address, controlPort)
            }
        }.getOrElse {
            events.diagnostic("音频 RTP↔NTP 同步套接字创建失败：${it.message}")
            return
        }
        audioSyncSocket = socket
        audioSyncSequence = 0
        val worker = Thread({
            var first = true
            var consecutiveFailures = 0
            /*
             * A refused sync packet is not a dead session.
             *
             * The car answers the audio SETUP with a controlPort before it is
             * listening on it, so the first packet or two draw an ICMP port
             * unreachable, which connected UDP reports as ECONNREFUSED. The
             * loop below used to treat that as fatal and exit, leaving the
             * session with no RTP↔NTP anchor at all — the car then guessed
             * scheduling from arrival times and the audio never settled.
             * Keep sending; only give up after a long run of failures or when
             * the session itself is closing.
             */
            while (!closed.get() && !socket.isClosed) {
                val current = lastAudioTimestamp
                val next = nextAudioTimestamp
                if (current == null || next == null) {
                    runCatching { Thread.sleep(25L) }.onFailure { return@Thread }
                    continue
                }
                val packet = AudioSyncPacket.build(
                    sequence = audioSyncSequence,
                    currentTimestamp = current,
                    nextTimestamp = next,
                    ntp = ntpBytes(clock.syncedNtp()),
                    first = first,
                )
                val sent = runCatching { socket.send(DatagramPacket(packet, packet.size)) }
                if (sent.isSuccess) {
                    consecutiveFailures = 0
                    audioSyncSequence = (audioSyncSequence + 1) and 0xffff
                    if (first) {
                        first = false
                        events.diagnostic(
                            "音频首个 RTP↔NTP 同步包已发送：controlPort=$controlPort " +
                                "ts=$current→$next rate=${format.sampleRate}Hz",
                        )
                    }
                } else {
                    consecutiveFailures++
                    val reason = sent.exceptionOrNull()?.message ?: "未知错误"
                    if (consecutiveFailures == 1 || consecutiveFailures % 100 == 0) {
                        events.diagnostic(
                            "音频同步包暂时发不出去（第 $consecutiveFailures 次）：$reason；继续重试",
                        )
                    }
                    if (consecutiveFailures >= AUDIO_SYNC_MAX_FAILURES) {
                        events.diagnostic("音频同步包连续失败 $consecutiveFailures 次，停止同步线程")
                        return@Thread
                    }
                }
                runCatching { Thread.sleep(AUDIO_SYNC_INTERVAL_MS) }.onFailure { return@Thread }
            }
        }, "carplay-audio-sync").apply { isDaemon = true }
        audioSyncThread = worker
        worker.start()
        events.diagnostic("音频 RTP↔NTP 同步已启用：$address:$controlPort，每 ${AUDIO_SYNC_INTERVAL_MS}ms")
    }

    private fun ntpBytes(value: BigInteger): ByteArray {
        val raw = value.and(BigInteger.ONE.shiftLeft(64).subtract(BigInteger.ONE)).toByteArray()
        val trimmed = if (raw.size > 8) raw.copyOfRange(raw.size - 8, raw.size) else raw
        return ByteArray(8).also { trimmed.copyInto(it, it.size - trimmed.size) }
    }
    fun config(bytes: ByteArray) {
        synchronized(videoEpochLock) {
            avcC = bytes.copyOf()
            configGeneration++
            events.diagnostic(
                "收到视频 CONFIG：generation=$configGeneration，大小=${bytes.size}B" +
                    if (videoRecoveryPending) "（恢复轮次要求 >= $requiredConfigGeneration）" else "",
            )
        }
    }
    fun frame(bytes: ByteArray, ptsUs: Long, keyFrame: Boolean) {
        if (closed.get() || !modes.ownsScreen()) return
        if (avcC == null) return
        if (videoRecoveryPending && !keyFrame) {
            return
        }
        var recovered = false
        if (keyFrame) {
            synchronized(videoEpochLock) {
                // A rebuilt CarLife encoder must publish its new SPS/PPS before
                // its IDR.  Accepting an IDR against the old CONFIG is the
                // exact sequence that produces a transparent recovered window.
                if (videoRecoveryPending && configGeneration < requiredConfigGeneration) {
                    events.diagnostic(
                        "丢弃恢复轮次过早 IDR：CONFIG generation=$configGeneration，" +
                            "需要 >= $requiredConfigGeneration",
                    )
                    return
                }
                keyFramePending = false
                recovered = videoRecoveryPending
                videoRecoveryPending = false
            }
        }
        if (recovered) events.diagnostic("恢复轮次已接收新 IDR，等待视频线程先发 CONFIG 再发首帧")
        val before = queue.dropped
        // Only a closed queue means the sender is gone. A P frame rejected
        // while the queue waits for a key frame is ordinary and must not
        // restart the source's encoder: see VideoQueue.OfferResult.
        if (queue.offer(EncodedVideo(bytes, ptsUs, keyFrame)) == VideoQueue.OfferResult.CLOSED) {
            requestKeyFrame()
        }
        val dropped = queue.dropped - before
        if (dropped > 0) events.dropped(dropped)
    }
    /**
     * Sends one media packet.
     *
     * For AAC the caller supplies exactly one access unit. For LPCM the caller
     * supplies raw PCM; it is split into the 5 ms packets the stream was opened
     * with, because the car schedules playback per packet and a larger payload
     * would run ahead of its clock.
     */
    fun audio(bytes: ByteArray, ptsUs: Long) {
        if (closed.get() || bytes.isEmpty()) return
        synchronized(audioQueueLock) {
            // Keep the ownership check and generation snapshot under the same
            // lock used by changeModes.  Otherwise a transfer can race between
            // the check and enqueue, leaving a fresh-looking stale chunk.
            if (closed.get() || !modes.ownsAudio()) return
            if (audioQueue.size >= AUDIO_QUEUE_LIMIT) {
                audioQueue.removeFirst()
                audioQueueDrops++
                if (audioQueueDrops == 1L || audioQueueDrops % 100L == 0L) {
                    events.diagnostic("音频实时队列丢弃过期块：$audioQueueDrops")
                }
            }
            audioQueue.addLast(AudioChunk(bytes.copyOf(), ptsUs, audioGeneration))
            audioQueueLock.notifyAll()
        }
    }

    private fun startAudioWorker() {
        if (audioWorker != null) return
        audioWorker = Thread({
            while (!closed.get()) {
                val chunk = when (val result = takeAudioQueueItem(audioQueueLock, audioQueue) { !closed.get() }) {
                    is AudioQueueTake.Item -> result.value
                    AudioQueueTake.Stopped -> break
                    AudioQueueTake.Interrupted -> {
                        if (!closed.get()) {
                            val failure = InterruptedException("AirPlay audio sender was interrupted")
                            events.diagnostic("音频发送线程被意外中断：会话将正常收尾")
                            events.failed(failure)
                        }
                        Thread.currentThread().interrupt()
                        break
                    }
                }
                runCatching { sendAudioChunk(chunk) }.onFailure {
                    if (!closed.get()) events.diagnostic("音频发送线程异常：${it.message}")
                }
            }
        }, "carplay-audio-send").apply { isDaemon = true; start() }
    }

    /**
     * Records the receiver's audio clock from a `/feedback` reply.
     *
     * Each entry carries `sampleTime`, the number of samples the car has
     * actually consumed for that stream. It is the only way to tell a stream
     * the car accepted from one it opened and never plays: a working stream
     * advances, a broken one stays at zero. Every capture so far died about
     * four seconds after audio started, and without this number there was no
     * way to say whether the car was consuming what we sent.
     */
    private fun recordAudioFeedback(message: ControlMessage) {
        if (message.body.isEmpty()) return
        val root = runCatching { plist(message.body) }.getOrNull() ?: return
        val streams = root["streams"] as? List<*> ?: return
        val ours = selectedAudio?.streamType ?: return
        streams.forEach { raw ->
            val entry = raw as? Map<*, *> ?: return@forEach
            val sampleTime = (entry["sampleTime"] as? Number)?.toLong() ?: return@forEach
            receiverAudioSampleTime = sampleTime
            receiverFeedbackCount++
        }
        if (reportedCommands.add("feedback:${streams.size}")) {
            events.diagnostic("车机音频回执：streams=${streams.size}，sampleTime=$receiverAudioSampleTime（本机类型 $ours）")
        }
    }

    /** Periodic one-liner so a stalled receiver is visible in the journal. */
    private fun reportAudioFeedback() {
        val now = SystemClock.elapsedRealtime()
        if (now - lastFeedbackReportAt < 5_000) return
        lastFeedbackReportAt = now
        if (receiverAudioSampleTime < 0) return
        val samples = receiverAudioSampleTime
        val seconds = samples.toDouble() / (selectedAudio?.sampleRate ?: 48_000).toDouble()
        events.diagnostic(
            "车机音频进度：sampleTime=$samples（${"%.1f".format(seconds)}s），回执 ${receiverFeedbackCount} 次",
        )
    }

    private fun sendAudioChunk(chunk: AudioChunk) {
        val key = audioKey ?: return
        val format = selectedAudio ?: return
        if (format.isPcm) {
            val frameBytes = format.pcmPacketBytes
            val merged = synchronized(audioQueueLock) {
                if (!isCurrentAudioChunkLocked(chunk)) return
                if (pcmCarry.isEmpty()) chunk.bytes else pcmCarry + chunk.bytes
            }
            var offset = 0
            while (merged.size - offset >= frameBytes && !closed.get()) {
                synchronized(audioQueueLock) {
                    // Ownership can change while this chunk is being paced.
                    // Keep the check and packet send atomic so no remainder
                    // leaves after the transfer has been processed.
                    if (!isCurrentAudioChunkLocked(chunk)) return
                    val payload = merged.copyOfRange(offset, offset + frameBytes)
                    paceAudioPacket(
                        1_000_000_000L * format.samplesPerPacket / format.sampleRate,
                    )
                    val timestamp = pcmTimestamp(format)
                    sendAudioPacket(key, format, pcmToWire(payload), timestamp)
                    sendCompanionAudio(payload, timestamp)
                    pcmSamples += format.samplesPerPacket
                    offset += frameBytes
                    pcmCarry = if (offset >= merged.size) {
                        ByteArray(0)
                    } else {
                        merged.copyOfRange(offset, merged.size)
                    }
                }
            }
            synchronized(audioQueueLock) {
                if (!isCurrentAudioChunkLocked(chunk)) return
                pcmCarry = if (offset >= merged.size) ByteArray(0) else merged.copyOfRange(offset, merged.size)
                if (pcmCarry.size > frameBytes * 4) {
                    // The backlog grew past four packets: the source is running
                    // ahead of the wire. Dropping it keeps latency bounded,
                    // which is the same trade the 对照实现 sender makes with
                    // its ring buffer's expiry counter.
                    pcmCarryDropped += pcmCarry.size
                    pcmCarry = ByteArray(0)
                }
                // Report the buffer state every 100 chunks. The 对照实现
                // sender tracks underrun/expiry/overflow separately; this
                // sender has one carry buffer, so the equivalent numbers are
                // how much is still pending, how much was dropped, and how many
                // samples have gone out.
                if (++pcmChunks % 100L == 0L) {
                    events.diagnostic(
                        "音频缓冲：待发=${pcmCarry.size}B 已丢弃=${pcmCarryDropped}B " +
                            "已发采样=$pcmSamples 每包=${format.samplesPerPacket} 帧",
                    )
                }
            }
            return
        }
        synchronized(audioQueueLock) {
            if (!isCurrentAudioChunkLocked(chunk)) return
            paceAudioPacket(1_000_000_000L * format.samplesPerPacket / format.sampleRate)
            val timestamp = aacTimestampClock?.next(chunk.ptsUs)
                ?: format.rtpTimestamp(chunk.ptsUs)
            sendAudioPacket(key, format, chunk.bytes, timestamp)
            /*
             * Keep the companion fed on the AAC path too.
             *
             * The iPhone's AltAudio stream carries its own Opus encoding of the
             * same audio at the same instant. This sender has no Opus encoder,
             * so it writes the AAC access unit on both streams rather than
             * leaving the companion silent: a stream that is declared in SETUP
             * and then never receives a packet is worse than one carrying a
             * codec the receiver may not decode, because the car's audio
             * watchdog is counting packets, not decoding them.
             */
            sendCompanionAudio(chunk.bytes, timestamp)
        }
    }

    private fun isCurrentAudioChunkLocked(chunk: AudioChunk): Boolean =
        !closed.get() && chunk.generation == audioGeneration && modes.ownsAudio()

    /**
     * Changes the local ownership declaration and invalidates media that was
     * produced under the previous owner.  RTP sequence, nonce and sample
     * counters intentionally survive: they are stream state, not queue state.
     */
    private fun setAudioOwnership(phone: Boolean) {
        synchronized(audioQueueLock) {
            val changed = modes.ownsAudio() != phone
            modes.audio(phone)
            if (changed) invalidateAudioGenerationLocked()
        }
    }

    /** Must be called with [audioQueueLock] held. */
    private fun invalidateAudioGenerationLocked() {
        audioGeneration++
        audioQueue.clear()
        pcmCarry = ByteArray(0)
        // Re-anchor pacing after a handover without touching the RTP clock.
        audioNextSendNanos = 0L
        audioQueueLock.notifyAll()
    }

    /**
     * Paces packets to their media duration so chunks never leave in a burst.
     *
     * A working sender runs a media-clock pacer and parks until the packet's
     * negotiated duration has elapsed. Without it, a source that
     * hands over a large PCM block — CarLife does — makes this method emit
     * dozens of packets back to back, so the car receives a burst whose arrival
     * times have no relation to the timestamps inside them. The stream is
     * accepted and stays silent, which is exactly the symptom here.
     *
     * The sleep is bounded to one interval so a long stall upstream cannot make
     * the next block catch up by sending a flood.
     */
    private fun paceAudioPacket(intervalNanos: Long = AUDIO_PACKET_INTERVAL_NANOS) {
        val now = System.nanoTime()
        val wait = audioNextSendNanos - now
        if (wait > 0) {
            if (wait > intervalNanos) {
                // The deadline fell far behind (the source stalled). Re-anchor
                // instead of sleeping through the whole backlog.
                audioNextSendNanos = now + intervalNanos
            } else {
                LockSupport.parkNanos(wait)
            }
        }
        val after = System.nanoTime()
        audioNextSendNanos = maxOf(after, audioNextSendNanos) + intervalNanos
    }

    /**
     * CarPlay's LPCM payload is big-endian, while Android captures and CarLife
     * both hand over little-endian 16-bit samples. Sending them byte for byte
     * produced noise the car played as silence.
     */
    private fun pcmToWire(pcm: ByteArray): ByteArray {
        val out = pcm.copyOf()
        var i = 0
        while (i + 1 < out.size) {
            val first = out[i]; out[i] = out[i + 1]; out[i + 1] = first
            i += 2
        }
        return out
    }

    /**
     * The RTP clock for the PCM stream.
     *
     * It starts at [pcmBase] rather than zero. The car schedules playback
     * against the session's own network clock, so a timestamp near zero is
     * already in its past and every packet is discarded as stale — which is
     * exactly what "the stream is set up and packets are sent, but nothing is
     * audible" looked like. Advancing by the packet's own sample count keeps
     * the two ends in step.
     */
    private fun pcmTimestamp(format: SenderAudioFormat): Int = pcmBase + pcmSamples

    private fun sendAudioPacket(key: ByteArray, format: SenderAudioFormat, payload: ByteArray, timestamp: Int) {
        // SSRC is a literal zero. A sender that works on this exact head unit
        // writes 0 here, and a random SSRC is the only field of this header
        // that never matched it; the car accepted the stream and played
        // nothing, which is the one symptom left unexplained by timing.
        val header = ByteBuffer.allocate(12).order(ByteOrder.BIG_ENDIAN).apply {
            put(0x80.toByte()); put((format.streamType and 0x7f).toByte())
            putShort((audioSequence++).toShort()); putInt(timestamp); putInt(0)
        }.array()
        val nonce = AirPlayCrypto.nonce64(audioCounter++)
        val sealed = AirPlayCrypto.chachaSeal(key, nonce, payload, header.copyOfRange(4,12))
        val packet = header + sealed + nonce.copyOfRange(4,12)
        checkNotNull(audioSocket).send(DatagramPacket(packet, packet.size, audioAddress))
        lastAudioTimestamp = timestamp.toLong() and 0xffff_ffffL
        nextAudioTimestamp = lastAudioTimestamp!! + format.samplesPerPacket
        events.audioSent()
        // First packet in full detail, then one summary per second: enough to
        // tell "we are sending" apart from "we are sending nothing".
        val sent = ++audioPacketsSent
        if (sent == 1L) {
            events.diagnostic(
                "音频首包：type=${format.streamType} ${format.codec} ${payload.size}B " +
                    "ts=$timestamp seq=${(audioSequence - 1) and 0xffff} 目标=$audioAddress",
            )
        } else if (sent % 200L == 0L) {
            events.diagnostic("音频已发送 $sent 包（${format.sampleRate}Hz，第 $timestamp 采样）")
        }
    }

    /**
     * Feeds the same PCM to every companion LPCM stream.
     *
     * Each stream has its own sequence counter, encryption counter and nonce
     * but shares the RTP timestamp, because they are the same audio at the same
     * instant — that is how the reference sender drives them.
     */
    private fun sendCompanionAudio(payload: ByteArray, timestamp: Int) {
        if (secondaryAudio.isEmpty()) return
        val wirePayload = pcmToWire(payload)
        val streams = synchronized(audioQueueLock) { secondaryAudio.toList() }
        for (stream in streams) {
            if (closed.get()) return
            val header = ByteBuffer.allocate(12).order(ByteOrder.BIG_ENDIAN).apply {
                put(0x80.toByte()); put((stream.format.streamType and 0x7f).toByte())
                putShort((stream.sequence++).toShort()); putInt(timestamp); putInt(0)
            }.array()
            val nonce = AirPlayCrypto.nonce64(stream.counter++)
            val sealed = AirPlayCrypto.chachaSeal(stream.key, nonce, wirePayload, header.copyOfRange(4, 12))
            val packet = header + sealed + nonce.copyOfRange(4, 12)
            runCatching {
                stream.socket.send(DatagramPacket(packet, packet.size, stream.address))
            }.onFailure {
                if (!closed.get()) events.diagnostic("第二条音频流发送失败：${it.message}")
                return
            }
            val sent = ++stream.packets
            if (sent == 1L) {
                events.diagnostic(
                    "第二条音频流首包：type=${stream.format.streamType} ${payload.size}B " +
                        "ts=$timestamp 目标=${stream.address}",
                )
            }
        }
    }
    private fun handleCommand(body: ByteArray): ControlReply {
        if (body.isEmpty()) return ControlReply(501)
        val command = plist(body)
        when (command["type"]) {
            "iAPSendMessage" -> {
                val data = IapCommandData.payload(command)
                if (data == null) return ControlReply(200)
                /*
                 * Only run an iAP2 link here when the car enabled iAPChannel.
                 *
                 * This head unit sends the six-byte link marker on the command
                 * channel even though it never enables iAPChannel: its own
                 * iPhone capture records `iAPChannel = 0` and proposes a
                 * feature list without it, and the live connection negotiates
                 * exactly altScreen/viewAreas/enhancedSiri. Opening a link
                 * anyway reaches READY, answers nothing, retries
                 * StartIdentification every three seconds, and the car resets
                 * the control connection about twenty seconds later. Control
                 * stays on the authenticated Bluetooth link; the markers are
                 * acknowledged with 200 and nothing else.
                 */
                /*
                 * Only run a link when the car actually enables iAPChannel.
                 *
                 * 1.2.75 removed this gate so the link would come up from the
                 * first marker. The 1.2.77 field logs show what that produces
                 * on this car: the link reaches READY, we send 0xAA00, the car
                 * acknowledges the frame and then never sends 0xAA01 — for the
                 * whole session, in all three captures. It answers neither
                 * 0x1D00 nor 0xAA00, so the channel is closed by firmware
                 * policy here and a link on it buys nothing.
                 *
                 * Control stays on the authenticated Bluetooth link, and the
                 * markers are acknowledged with 200.
                 */
                /*
                 * Attach the command-channel link on the car's first marker,
                 * whether or not it advertises iAPChannel.
                 *
                 * This car never lists iAPChannel, so the 1.2.78 gate kept the
                 * link from being built at all — and with no link there is
                 * nothing to echo the marker on, which is why 1.2.80's echo fix
                 * never ran. The marker echo in Iap2LinkEngine is what keeps
                 * the session alive.
                 */
                val forwarded = receiveIap(data) ||
                    (ensureCommandIap()?.offer(data) == true)
                lastInboundIapNanos = System.nanoTime(); inboundIapCount++
                val description = describeIapMessage(data)
                if (!forwarded && reportedCommands.add("iAP:$description")) {
                    events.diagnostic(
                        "iAP 控制数据未被链路接收：字节=${data.size}（$description）",
                    )
                } else if (reportedCommands.add("first:$description")) {
                    events.diagnostic("收到车机 iAP2：字节=${data.size}（$description），已交给链路")
                }
                return ControlReply(200)
            }
            "forceKeyFrame" -> requestKeyFrame()
            "changeModes" -> {
                // Serialize the ownership transition with the audio worker.
                // This both invalidates queued/carry-over PCM on a loss and
                // lets an in-flight chunk finish at most its current packet.
                val change = synchronized(audioQueueLock) {
                    val ownedBefore = modes.ownsAudio()
                    val result = modes.change(command)
                    val ownedAfter = modes.ownsAudio()
                    if (ownedBefore != ownedAfter) {
                        invalidateAudioGenerationLocked()
                        /*
                         * Losing audio must be visible.
                         *
                         * While the car owns the audio resource every captured
                         * chunk is dropped at enqueue without a trace, so a
                         * session could play a single packet and then go
                         * silent with nothing in the journal to explain it.
                         */
                        events.diagnostic(
                            if (ownedAfter) "车机把音频资源交还手机，恢复发送"
                            else "车机收回了音频资源，暂停发送音频",
                        )
                        /*
                         * Print what the car actually asked for, not only the
                         * outcome.
                         *
                         * A takeover here is the car withdrawing a resource it
                         * granted; the command body says which resources it
                         * wants and whether it named a reason. Without this the
                         * journal shows the withdrawal but never the request,
                         * which is exactly the gap that made "car hands back
                         * the screen one second after restart" unexplainable.
                         */
                        events.diagnostic(
                            "资源归属变更请求：${command["params"]} " +
                                "→ 屏幕=${if (modes.ownsScreen()) "手机" else "车机"} " +
                                "音频=${if (modes.ownsAudio()) "手机" else "车机"}",
                        )
                    }
                    result
                }
                val response = change.reply
                // Screen and audio are independent resources. Returning the
                // screen must never reclaim audio that the head unit still owns.
                if (change.audioRestored && selectedAudio != null) {
                    setAudioOwnership(true)
                    events.diagnostic("车机已归还音频资源，重新声明由手机播放")
                }
                // Regaining the screen means a fresh decoder on the car side.
                // Announce the ownership snapshot first, then rebuild the
                // source encoder so CONFIG/IDR cannot overtake the grant.
                if (change.screenRestored) {
                    run("carplay-change-modes-recovery") {
                        runCatching {
                            control?.request("POST", "/command", encode(modes.snapshot()), plistHeaders)
                        }.onSuccess {
                            events.diagnostic("changeModes 屏幕归还已声明，开始请求恢复关键帧")
                        }.onFailure {
                            events.diagnostic("changeModes 屏幕归还声明失败：${it.message}")
                        }
                        if (!closed.get()) requestKeyFrame(restore = true)
                    }
                }
                events.screenOwnership(modes.ownsScreen())
                if (commandModes) {
                    run("carplay-modes-update") {
                        runCatching { control?.request("POST", "/command", encode(modes.snapshot()), plistHeaders) }
                    }
                }
                // The 200 body for changeModes is a *grant reply*, not a
                // modesChanged notification: a working sender on this firmware
                // answers `{status: 0, params: {resources, appStates}}`. Echoing
                // the notification shape back (`type: modesChanged`) leaves the
                // car with no grant for the resource it just asked about — the
                // reply is accepted, then the picture goes black about five
                // seconds later and the session tears down.
                return ControlReply(200, encode(mapOf(
                    "status" to 0,
                    "params" to (response["params"] ?: emptyMap<String, Any>()),
                )))
            }
            "hidSendReport" -> {
                val uuid = command["uuid"]?.toString()
                val device = touchDevices[uuid]
                val keys = keyDevices[uuid]
                val report = command["hidReport"] as? ByteArray
                if (report == null) return ControlReply(if (events.input(command)) 200 else 501)
                // Touch and key reports are logged very differently on purpose.
                // A finger drag produces dozens of touch reports per second,
                // and printing each one flooded the bounded journal so badly
                // that the driver's button presses were pushed out of it — the
                // one thing that could not be reconstructed afterwards. Touch
                // reports are therefore summarised and throttled; key reports
                // keep their full raw bytes because the button map is still
                // being learned from them.
                val isTouch = device != null
                if (isTouch) {
                    // The first reports are kept in full. A 5-byte report under a
                    // descriptor that spans more than that is precisely the case
                    // where the coordinate fields can be misread, and without
                    // the bytes there is no way to tell which axis came out
                    // wrong — which is why "the touch lands somewhere else"
                    // could not be closed from earlier journals.
                    val seen = touchReports++
                    if (seen < TOUCH_REPORTS_LOGGED) {
                        val extent = device?.touchExtent
                        events.diagnostic(
                            "触摸原始 #$seen 长度=${report.size} " +
                                "hex=${report.joinToString(" ") { "%02X".format(it) }} " +
                                "量程=${extent?.let { "${it.first}x${it.second}" } ?: "未声明"}",
                        )
                    }
                } else {
                    events.diagnostic("车机按键报告 uuid=$uuid 长度=${report.size} " +
                        "hex=${report.joinToString(" ") { "%02X".format(it) }}")
                }
                val accepted = when {
                    device != null -> events.input(mapOf("type" to "touchSamples", "samples" to device.touches(report)))
                    keys != null -> events.input(mapOf("type" to "keySamples", "report" to keys.report(report)))
                    else -> events.input(command)
                }
                return ControlReply(if (accepted) 200 else 501)
            }
            // Notifications the head unit polls while it owns the screen. This
            // firmware repeats setLimitedUI roughly once a second after the
            // driver returns to the native UI; answering 501 to it ended the
            // session with "Connection reset" in the field.
            // requestUI is how the car asks the phone to show its picture
            // again. It is the only signal left after the car took the screen
            // for its own UI and did not send a mirroring changeModes back, so
            // it must reclaim ownership; otherwise every frame stays dropped
            // and CarPlay returns as an empty transparent window.
            "requestUI" -> {
                val reclaimed = modes.claimScreen()
                // requestUI is an ownership hand-back signal. Once the phone
                // already owns the screen, repeated polls must not start a
                // second encoder rebuild; that overlap is what made CarLife
                // replace a 30 fps codec with its 20 fps default.
                val recoveryNeeded = reclaimed
                if (reclaimed) {
                    events.screenOwnership(true)
                }
                if (recoveryNeeded) {
                    // The event reader remains the only reader for the event socket. The
                    // ownership snapshot travels on the separate control socket, serialized
                    // by RtspWire alongside feedback requests.
                    run("carplay-request-ui-recovery") {
                        runCatching {
                            control?.request("POST", "/command", encode(modes.snapshot()), plistHeaders)
                        }.onSuccess {
                            events.diagnostic("requestUI 资源声明已发送，开始请求 CarLife 恢复关键帧")
                        }.onFailure {
                            events.diagnostic("requestUI 资源声明失败：${it.message}")
                        }
                        if (!closed.get()) {
                            // Keep the ownership snapshot ahead of the
                            // encoder rebuild.  Otherwise CONFIG/IDR can reach
                            // the car while its screen grant is still pending.
                            requestKeyFrame(restore = true)
                        }
                    }
                    events.diagnostic(
                        if (reclaimed) "requestUI 屏幕归还已确认"
                        else "requestUI 检测到上一轮恢复仍未完成，重试资源声明和关键帧",
                    )
                } else {
                    events.diagnostic("requestUI 已收到：屏幕仍归手机且没有待恢复关键帧")
                }
                val accepted = events.input(command)
                return ControlReply(if (accepted) 200 else 501)
            }
            "requestSiri", "setNightMode", "updateViewArea", "setLimitedUI" ->
                return ControlReply(if (events.input(command)) 200 else 501)
            else -> { events.diagnostic("不支持的车机命令：${command["type"]}"); return ControlReply(501) }
        }
        return ControlReply(200)
    }
    /**
     * A one-line snapshot of everything this session counted.
     *
     * Written from `close()` so the last journal entry before a disconnect
     * says what the two ends actually exchanged. The 13-second teardown has
     * survived four hypotheses; this turns the next capture into evidence
     * instead of guesswork.
     */
    /**
     * True once this session has sent at least one video frame.
     *
     * The command channel above uses this as its "the session is live" signal:
     * `identified` never becomes true on this firmware, so it cannot answer
     * whether a restarted link belongs to an established stream or to one that
     * never came up.
     */
    fun isStreaming(): Boolean = videoKeepAliveCount > 0L && modes.ownsScreen()

    fun sessionSnapshot(): String {
        val frames = videoKeepAliveCount
        val audio = audioPacketsSent
        val feedback = feedbackCount
        val tunnel = iap?.diagnosticSummary() ?: "iAP2通道=未建立"
        val owns = "屏幕=${if (modes.ownsScreen()) "手机" else "车机"}，" +
            "音频=${if (modes.ownsAudio()) "手机" else "车机"}"
        val hid = "HID触控设备=${touchDevices.size} 按键设备=${keyDevices.size}"
        /*
         * The audio half of the snapshot carries the same numbers a field
         * capture needs to compare against the reference sender: which format
         * won, how many frames per packet, and how much of the carry buffer was
         * dropped. Without them "audio stopped" cannot be told apart from "audio
         * was never started".
         */
        val audioDetail = selectedAudio?.let {
            "音频格式=${it.streamType}/${it.codec} ${it.sampleRate}Hz/" +
                "${it.channels}声道 每包${it.samplesPerPacket}帧 " +
                "延迟=${if (it.isPcm) PCM_LATENCY_MS else AAC_LATENCY_MS}ms"
        } ?: "音频格式=未协商"
        return "会话快照：视频帧=$frames 音频包=$audio /feedback=$feedback " +
            "$owns $tunnel $hid $audioDetail " +
            "待发缓冲=${pcmCarry.size}B 丢弃=${pcmCarryDropped}B"
    }

    override fun close() {
        if (!closed.get()) {
            runCatching { events.diagnostic(sessionSnapshot()) }
        }
        if (!closed.compareAndSet(false,true)) return
        /*
         * A profile is only considered rejected when SETUP succeeded, real
         * audio packets were already emitted, and the session died before the
         * same 20-second proof window used for a working profile.  This keeps
         * display/Bluetooth failures from poisoning the audio candidate list.
         */
        if (audioProfileSetupComplete && !audioProfileStable && audioPacketsSent > 0L) {
            selectedAudio?.let { audioProfileRejected?.invoke(it) }
        }
        audioProfileSetupComplete = false
        audioProfileStable = false
        synchronized(iapLock) { openingIap = false; earlyIap.clear(); earlyIapBytes = 0 }
        queue.close()
        synchronized(audioQueueLock) { audioQueue.clear(); audioQueueLock.notifyAll() }
        audioWorker?.interrupt(); audioWorker = null
        audioSyncThread?.interrupt(); audioSyncThread = null
        runCatching { audioSyncSocket?.close() }
        audioSyncSocket = null
        resources.reversed().forEach { runCatching { it.close() } }; resources.clear()
        synchronized(workers) { workers.forEach { it.interrupt() }; workers.clear() }
    }
    companion object {
        private const val IAP_DATASTREAM_UUID = "E9459FD0-BCAD-4C45-820F-1E72447EF2F2"
        /** iAP2 link detection marker; the car repeats it until the link answers. */
        private val IAP2_MARKER = byteArrayOf(0xff.toByte(), 0x55, 0x02, 0x00, 0xee.toByte(), 0x10)
        /**
         * How many iAP2 links may be opened on the Wi-Fi command channel.
         *
         * The car legitimately restarts this link (it closed the Bluetooth one
         * the same way), so a dead tunnel has to be replaceable; the cap only
         * stops a car that answers nothing from spinning up links forever.
         */
        private const val MAX_COMMAND_IAP_FAILURES = 4
        /** How many touch reports are written out in full before only counts remain. */
        private const val TOUCH_REPORTS_LOGGED = 8

        /**
         * One LPCM packet carries 5 ms of audio, so 200 packets per second.
         *
         * A working sender on this firmware paces packets against exactly this
         * interval; see [paceAudioPacket].
         */
        private const val AUDIO_PACKET_INTERVAL_NANOS = 5_000_000L
        private const val AUDIO_SYNC_INTERVAL_MS = 1_000L
        /**
         * UDP keep-alive spacing, matching the iPhone's Standard keep-alive
         * controller, which the capture shows sending every 2017 ms.
         */
        private const val AIRPLAY_KEEPALIVE_INTERVAL_MS = 2_000L
        /** Sync packets that may fail in a row before the loop gives up. */
        private const val AUDIO_SYNC_MAX_FAILURES = 300
        private const val AUDIO_QUEUE_LIMIT = 24

        /**
         * Scheduling lead for AAC media on this head unit. The Buick iPhone
         * capture reports JBL/audioLatencyMs = 1000 ms for MainHighAudio/media
         * AAC. This is the receiver's scheduling lead, not the 80 ms lead used
         * by the auxiliary Opus streams.
         */
        private const val AAC_LATENCY_MS = 1000

        /**
         * Opus 48 kHz mono, the format the Buick capture shows for AltAudio.
         *
         * The iPhone opens this as an auxiliary stream alongside MainHighAudio
         * (`AltAudio/default, Opus, 48000 Hz, Mono, 960 samples/packet,
         * 0x40000000`). It is not taken from /info: that unit advertises
         * 0x40008000 for types 101/106, which is a different shape and is not
         * what the phone actually sends.
         */
        private const val ALT_AUDIO_OPUS_FORMAT = 0x40000000L
        /**
         * Scheduling lead for LPCM — 对照实现's value, not the iPhone's.
         *
         * The sysdiagnose captures show the phone asking for 32 ms on this car
         * with a four-key body. 对照实现 asks for 250 ms with a six-key body,
         * and 对照实现 is the sender actually observed holding this head unit
         * open. Two different senders on two different transports is not an
         * experiment that isolates the latency field, so the value that ships
         * is the one from the sender whose behavior is known here.
         */
        private const val PCM_LATENCY_MS = 250

        /**
         * The capture exposes these as HAL input/output sample buffers (IL/OL).
         * They are diagnostics only: this receiver accepted no corresponding
         * SETUP keys, so serializing guessed fields would risk rejection.
         */
        /**
         * Companion (AltAudio / type 101) shape, from the iPhone sysdiagnose:
         * `AltAudio/default, Opus, 48000 Hz, Mono, 960 samples/packet,
         * 0x40000000, IL = 3840 samples, OL = 3840 samples`.
         */
        private const val COMPANION_OPUS_FORMAT = 0x40000000L
        private const val COMPANION_OPUS_SAMPLES_PER_PACKET = 960

        /** AltAudio schedules 80 ms ahead; MainHighAudio schedules 1000 ms. */
        private const val COMPANION_LATENCY_MS = 80

        private const val AAC_INPUT_BUFFER_SAMPLES = 48_000
        private const val AAC_OUTPUT_BUFFER_SAMPLES = 57_552
        private const val PCM_INPUT_BUFFER_SAMPLES = 0
        private const val PCM_OUTPUT_BUFFER_SAMPLES = 0
        private const val AUDIO_PROFILE_STABLE_WINDOW_MS = 20_000L

        /**
         * Scheduling lead for the screen stream.
         *
         * 75 ms is what every build that produced a picture on this car used.
         * 1.2.75 raised it to 100 to match the reference sender's SETUP, then
         * this reverted: the two values differ by one frame at 30 fps, and the
         * change rode along with the iAP2 bootstrap rework, so a regression
         * would not have been attributable. Worth trying on its own later.
         */
        private const val SCREEN_LATENCY_MS = 75

        private val CONTROLLER_FEATURES = listOf(
            "uiContext",
            "viewAreas",
            "cornerMasks",
            "focusTransfer",
            "h.264Level5.1",
            "mainBuffered",
            "altScreen",
            "enhancedSiri",
            "hevc",
            "sessionManagement",
            "logTransfer",
            "iAPChannel",
            "vehicleStateProtocol",
            "videoPlayback",
        )

        /**
         * The AirPlay 2 audio-format bitmask, for turning a mask into words.
         *
         * Each bit is one encoder/rate/depth/channel combination a peer may
         * accept. Only the bits that matter for diagnosing this head unit are
         * named: the media formats it picks for the downlink (48/44.1 kHz
         * stereo PCM, AAC-LC) and the mono speech formats a microphone uplink
         * would use (16/24 kHz PCM, Opus). Anything else is reported as an
         * unknown bit rather than silently dropped.
         */
        private val AUDIO_BITS: List<Pair<Long, String>> = listOf(
            0x10L to "PCM16k/1",
            0x20L to "PCM16k/2",
            0x40L to "PCM24k/1",
            0x80L to "PCM24k/2",
            0x400L to "PCM44.1k/1",
            0x800L to "PCM44.1k/2",
            0x4000L to "PCM48k/1",
            0x8000L to "PCM48k/2",
            0x400000L to "AAC-LC44.1k/2",
            0x800000L to "AAC-LC48k/2",
            0x20000000L to "OPUS24k/1",
            0x40000000L to "OPUS48k/1",
        )
        private val plistHeaders = mapOf("Content-Type" to "application/x-apple-binary-plist")
        private val pairingHeaders = mapOf("Content-Type" to "application/pairing+tlv8", "X-Apple-HKP" to "3")
        private fun encode(value: Any) = BplistCodec.encode(value)
        private fun plist(bytes: ByteArray) = BplistCodec.decode(bytes) as? Map<*, *> ?: error("车机返回无效 plist")
    private fun number(value: Any?) = (value as? Number)?.toInt() ?: error("车机参数缺失")

    /** The selected view-area metadata, if the receiver supplies it. */
    private fun selectedViewArea(display: Map<*, *>): Map<*, *>? {
        val areas = (display["viewAreas"] as? List<*>)?.mapNotNull { it as? Map<*, *> }.orEmpty()
        if (areas.isEmpty()) return null
        val index = (display["initialViewArea"] as? Number)?.toInt() ?: 0
        return areas.getOrNull(index) ?: areas.first()
    }

    /**
     * The Buick E2LB /君越 contract exposes a real `type=102/media AAC-LC`
     * path alongside the legacy PCM entry.  This is an observed capability
     * profile, not a brand-wide hard-coded audio choice.
     */
    private fun hasHighQualityMediaCapability(info: Map<*, *>): Boolean {
        /*
         * A type-102 media AAC declaration is the capability gate. It is
         * deliberately independent of the Bluetooth display name, brand, or
         * model: another head unit can advertise the same stream and should
         * receive the same path.
         */
        val entries = (info["audioFormats"] as? List<*>)
            ?.mapNotNull { it as? Map<*, *> }
            .orEmpty()
        return entries.any {
            (it["type"] as? Number)?.toInt() == 102 &&
                it["audioType"]?.toString()?.trim()?.equals("media", ignoreCase = true) == true &&
                ((it["audioOutputFormats"] as? Number)?.toLong() ?: 0L) and 0x800000L != 0L
        }
    }

    private fun selectedSafeArea(
        display: Map<*, *>,
        area: Map<*, *>?,
    ): SenderSafeArea? {
        val safe = area?.get("safeArea") as? Map<*, *>
            ?: display["safeArea"] as? Map<*, *>
            ?: return null
        val areaX = (area?.get("originXPixels") as? Number)?.toInt() ?: 0
        val areaY = (area?.get("originYPixels") as? Number)?.toInt() ?: 0
        val x = areaX + ((safe["originXPixels"] as? Number)?.toInt() ?: return null)
        val y = areaY + ((safe["originYPixels"] as? Number)?.toInt() ?: return null)
        val width = (safe["widthPixels"] as? Number)?.toInt() ?: return null
        val height = (safe["heightPixels"] as? Number)?.toInt() ?: return null
        val displayWidth = (display["widthPixels"] as? Number)?.toInt() ?: return null
        val displayHeight = (display["heightPixels"] as? Number)?.toInt() ?: return null
        if (x < 0 || y < 0 || width <= 0 || height <= 0 ||
            x.toLong() + width > displayWidth || y.toLong() + height > displayHeight
        ) return null
        return SenderSafeArea(x, y, width, height)
    }

    /**
     * Names the audio-format bits a mask carries.
     *
     * The bitmask in `audioOutputFormats` / `audioInputFormats` is the only
     * place a head unit states what it will accept, and reading it as a hex
     * number alone is useless: 0x8050 looks like noise until it is decoded as
     * "48 kHz stereo out, plus 16 kHz and 24 kHz mono in". The values follow the
     * published AirPlay 2 audio-format table, which also matches what a working
     * sender on this firmware selects from.
     */
    private fun describeAudioBits(mask: Long): String {
        if (mask == 0L) return "空"
        val named = AUDIO_BITS.mapNotNull { (bit, name) -> if (mask and bit != 0L) name else null }
        val unknown = mask and AUDIO_BITS.fold(0L) { acc, entry -> acc or entry.first }.inv()
        return buildList {
            addAll(named)
            if (unknown != 0L) add("未知位 0x${unknown.toString(16)}")
        }.joinToString("/")
    }

    /** Prints a buttonInfo value without assuming its type. */
    private fun describeButtonValue(value: Any?): String = when (value) {
        null -> "null"
        is ByteArray -> "bytes(${value.size})=${value.joinToString(" ") { "%02X".format(it) }}"
        is List<*> -> "[${value.joinToString(",") { describeButtonValue(it) }}]"
        is Map<*, *> -> "{${value.entries.joinToString(",") { "${it.key}:${describeButtonValue(it.value)}" }}}"
        else -> value.toString()
    }

    private fun describeViewAreas(display: Map<*, *>): String {
        val areas = (display["viewAreas"] as? List<*>)?.mapNotNull { it as? Map<*, *> }.orEmpty()
        if (areas.isEmpty()) return "none"
        return areas.joinToString(";") { area ->
            val safe = area["safeArea"] as? Map<*, *>
            "origin=${area["originXPixels"] ?: 0},${area["originYPixels"] ?: 0}" +
                (safe?.let {
                    " safe=${it["widthPixels"]}x${it["heightPixels"]}+" +
                        "${it["originXPixels"]},${it["originYPixels"]}"
                } ?: " size=${area["widthPixels"] ?: "?"}x${area["heightPixels"] ?: "?"}")
        }
    }

    /**
     * Names a bare iAP2 CSM frame.
     *
     * This head unit sends six-byte payloads, which is exactly the CSM header
     * (`0x4040 | u16 length | u16 messageId`) with an empty body. Printing the
     * message id turns "字节=6" into something that can be matched against the
     * iPhone captures.
     */
    private fun describeIapMessage(data: ByteArray): String {
        // The link layer's detection marker, which this car repeats every second
        // until something answers it. Naming it separately keeps the log honest:
        // it is not a CSM frame and must never be treated as one.
        if (data.size == IAP2_MARKER.size && data.contentEquals(IAP2_MARKER)) return "iAP2 链路标记"
        if (data.size < 6 || data[0] != 0x40.toByte() || data[1] != 0x40.toByte()) {
            return "非 CSM"
        }
        val length = ((data[2].toInt() and 0xff) shl 8) or (data[3].toInt() and 0xff)
        val id = ((data[4].toInt() and 0xff) shl 8) or (data[5].toInt() and 0xff)
        return "CSM 0x${id.toString(16).padStart(4, '0')} len=$length"
    }
        private fun streams(bytes: ByteArray) = (plist(bytes)["streams"] as? List<*>)?.mapNotNull { it as? Map<*, *> } ?: error("车机未返回流信息")
    }
}
