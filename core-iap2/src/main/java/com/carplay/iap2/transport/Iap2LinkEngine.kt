package com.carplay.iap2.transport

import java.io.ByteArrayOutputStream
import java.util.ArrayDeque

/**
 * The iAP2 link layer, deliberately kept independent from I/O, threads, CSM, and media.
 *
 * The caller supplies a monotonic millisecond clock, writes [takeOutput] to its byte stream, and
 * feeds arbitrary receive fragments back through [feed]. It exposes control-session bytes and raw
 * payloads from other negotiated sessions. It does not open a Lockdown connection or own a
 * [BlockingDuplexByteStream].
 *
 * An inbound wire frame is limited to the largest u16 length (`65535` bytes total).  Pending
 * outbound and out-of-order packets are additionally capped by [Iap2LinkConfig] (64 by default).
 * Pending output is capped at 1 MiB and pending events at 256 entries by default, so neither peer
 * input nor a caller that temporarily stops draining the engine can grow memory without bound.
 */
class Iap2LinkEngine(
    private val config: Iap2LinkConfig = Iap2LinkConfig(),
    private val onPacketMetadata: ((Iap2PacketMetadata) -> Unit)? = null,
) {
    enum class State {
        IDLE,
        DETECTING,
        NEGOTIATING,
        NORMAL,
        DEAD,
    }

    sealed class Event {
        data class Control(val bytes: ByteArray) : Event()
        data class Session(val sessionId: Int, val bytes: ByteArray) : Event()
        data class Writable(val value: Boolean) : Event()
        data class Dead(val reason: String?, val restartInput: ByteArray? = null) : Event()
    }

    data class SessionDescriptor(
        val id: Int,
        val kind: Int,
        val version: Int,
    ) {
        init {
            require(id in 0..0xff)
            require(kind in 0..0xff)
            require(version in 0..0xff)
        }
    }

    data class SynchronizationPayload(
        val maxOutgoing: Int,
        val maxLength: Int,
        val retransmissionTimeoutMillis: Int,
        val acknowledgementTimeoutMillis: Int,
        val maxRetransmissions: Int,
        val maxAcknowledgements: Int,
        val sessions: List<SessionDescriptor>,
    ) {
        init {
            require(maxOutgoing in 0..0xff)
            require(maxLength in 0..MAX_WIRE_FRAME_BYTES)
            require(retransmissionTimeoutMillis in 0..0xffff)
            require(acknowledgementTimeoutMillis in 0..0xffff)
            require(maxRetransmissions in 0..0xff)
            require(maxAcknowledgements in 0..0xff)
        }

        fun encode(): ByteArray {
            val out = ByteArrayOutputStream(10 + sessions.size * SESSION_DESCRIPTOR_BYTES)
            out.write(SYNCHRONIZATION_VERSION)
            out.write(maxOutgoing)
            writeU16(out, maxLength)
            writeU16(out, retransmissionTimeoutMillis)
            writeU16(out, acknowledgementTimeoutMillis)
            out.write(maxRetransmissions)
            out.write(maxAcknowledgements)
            sessions.forEach {
                out.write(it.id)
                out.write(it.kind)
                out.write(it.version)
            }
            return out.toByteArray()
        }

        companion object {
            fun decode(bytes: ByteArray): SynchronizationPayload? {
                if (bytes.size < SYNCHRONIZATION_FIXED_BYTES || u8(bytes[0]) != SYNCHRONIZATION_VERSION) {
                    return null
                }
                val descriptors = ArrayList<SessionDescriptor>()
                var offset = SYNCHRONIZATION_FIXED_BYTES
                while (offset + SESSION_DESCRIPTOR_BYTES <= bytes.size) {
                    descriptors += SessionDescriptor(u8(bytes[offset]), u8(bytes[offset + 1]), u8(bytes[offset + 2]))
                    offset += SESSION_DESCRIPTOR_BYTES
                }
                return SynchronizationPayload(
                    maxOutgoing = u8(bytes[1]),
                    maxLength = readU16(bytes, 2),
                    retransmissionTimeoutMillis = readU16(bytes, 4),
                    acknowledgementTimeoutMillis = readU16(bytes, 6),
                    maxRetransmissions = u8(bytes[8]),
                    maxAcknowledgements = u8(bytes[9]),
                    sessions = descriptors,
                )
            }
        }
    }

    private data class Header(
        val length: Int,
        val control: Int,
        val sequence: Int,
        val acknowledgement: Int,
        val sessionId: Int,
    )

    private data class Packet(
        var sequence: Int,
        val sessionId: Int,
        val payload: ByteArray,
        var retransmissions: Int = 0,
        var deadlineMillis: Long = 0,
    )

    private var state = State.IDLE
    private var peerSynchronization = localSynchronization()
    private var peerSynchronizationReceived = false
    private var respondingToPeerSynchronization = false
    private var localSynchronizationSequence: Int? = null
    private var peerSynchronizationSequence: Int? = null
    private var controlSessionId = CONTROL_SESSION_ID
    private var sentSequence = INITIAL_SEQUENCE
    /**
     * Whether a repeated detection marker should be echoed *right now*.
     *
     * The profile flag says which firmware tolerates an echo at all; this says
     * whether the session has actually got past the point where the car wants
     * a rebuild.
     *
     * Field evidence from this head unit family:
     *
     *   before 0x4301 is answered  → echo keeps a link the car has abandoned;
     *                                it never starts the second round and the
     *                                session dies on the watchdog
     *                                (B233, 2026-11-01: 20 s of silence, then
     *                                "第二轮 iAP2 同步未完成")
     *   after the car is running   → echo is a heartbeat, and refusing it kills
     *                                the link and drops every queued message,
     *                                0x5001 media above all
     *
     * Set to true by [allowRepeatedMarkerEcho] once the wireless configuration
     * has arrived.
     */
    @Volatile
    private var markerEchoAllowed = false
    /** See [sendAcknowledgement]: per-transport sequence rule. */
    private val advanceSequenceOnAcknowledgement =
        config.advanceSequenceOnAcknowledgement
    private var lastSentAcknowledged: Int? = null
    private var lastReceivedInOrder = 0
    private var lastAcknowledgedReceived: Int? = null
    private var accumulatedAcknowledgements = 0
    private var writable = false

    private val unacknowledged = ArrayList<Packet>()
    private val queued = ArrayDeque<Packet>()
    private val outOfOrder = ArrayList<Packet>()
    private val events = ArrayDeque<Event>()
    private val output = ByteArrayOutputStream()
    private val receive = ReceiveBuffer()
    private var awaitingPayload: Header? = null

    private var markerDeadlineMillis: Long? = null
    /** Detection markers echoed back while the link is up; diagnostics only. */
    private var markerEchoes = 0L
    /** How many times the peer restarted the link under us; diagnostics only. */
    private var linkRestartEpoch = 0L
    private var synchronizationDeadlineMillis: Long? = null
    private var sendAcknowledgementDeadlineMillis: Long? = null
    private var receiveAcknowledgementDeadlineMillis: Long? = null

    fun state(): State = state

    fun writable(): Boolean = writable

    /** All queued data has been acknowledged by the peer; this is not application acceptance. */
    fun outboundAcknowledged(): Boolean = state == State.NORMAL && queued.isEmpty() && unacknowledged.isEmpty()

    fun peerSynchronization(): SynchronizationPayload = peerSynchronization

    fun validatedPeerSynchronization(): SynchronizationPayload? =
        peerSynchronization.takeIf { peerSynchronizationReceived }

    fun negotiatedControlSessionId(): Int? = controlSessionId.takeIf { peerSynchronizationReceived }

    fun takeOutput(): ByteArray {
        val bytes = output.toByteArray()
        output.reset()
        return bytes
    }

    fun pollEvent(): Event? = if (events.isEmpty()) null else events.removeFirst()

    fun nextDeadlineMillis(): Long? = listOfNotNull(
        markerDeadlineMillis,
        synchronizationDeadlineMillis,
        sendAcknowledgementDeadlineMillis,
        receiveAcknowledgementDeadlineMillis,
    ).minOrNull()

    /** Starts a link attempt. Wired CarKit uses [wiredInitiator] = true. */
    fun start(wiredInitiator: Boolean, nowMillis: Long) {
        if (state != State.IDLE) return
        state = State.DETECTING
        if (config.startup == Iap2LinkStartup.ACCESSORY_FIRST) return
        if (!appendOutput(IAP2_MARKER)) return
        markerDeadlineMillis = nowMillis + MARKER_RESEND_MILLIS
        if (wiredInitiator && config.startup == Iap2LinkStartup.LEGACY && state != State.DEAD) enterNegotiating(nowMillis)
    }

    /** Supplies any received byte fragment; empty fragments are harmless. */
    fun feed(bytes: ByteArray, nowMillis: Long) {
        if (state == State.DEAD || bytes.isEmpty()) return
        var offset = 0
        while (offset < bytes.size && state != State.DEAD) {
            val room = receive.remainingCapacity()
            if (room == 0) {
                if (!parseAvailable(nowMillis)) {
                    die("iAP2 inbound frame exceeds $MAX_WIRE_FRAME_BYTES bytes")
                    return
                }
                continue
            }
            val count = minOf(room, bytes.size - offset)
            receive.append(bytes, offset, count)
            offset += count
            while (parseAvailable(nowMillis) && state != State.DEAD) {
                // Drain all complete frames before accepting more input.
            }
        }
        // A detection marker may share this read with a new SYN. Preserve the unconsumed
        // tail only for that explicit terminal reason, bounded like every other wire buffer.
        val terminal = events.peekLast() as? Event.Dead
        if (terminal?.restartInput != null && offset < bytes.size) {
            val tailBytes = bytes.size - offset
            events.removeLast()
            events += if (tailBytes <= MAX_WIRE_FRAME_BYTES - terminal.restartInput.size) {
                terminal.copy(restartInput = terminal.restartInput + bytes.copyOfRange(offset, bytes.size))
            } else Event.Dead("iAP2 restart input limit exceeded")
        }
    }

    /** The byte stream ended. */
    fun feedEof() {
        die(null)
    }

    /** Advances all due protocol timers with the caller's monotonic clock. */
    fun advanceTime(nowMillis: Long) {
        while (state != State.DEAD) {
            val deadline = nextDeadlineMillis() ?: return
            if (deadline > nowMillis) return
            when {
                markerDeadlineMillis?.let { it <= nowMillis } == true -> {
                    markerDeadlineMillis = null
                    if (state == State.DETECTING) {
                        if (appendOutput(IAP2_MARKER)) {
                            markerDeadlineMillis = nowMillis + MARKER_RESEND_MILLIS
                        }
                    }
                }

                synchronizationDeadlineMillis?.let { it <= nowMillis } == true -> {
                    synchronizationDeadlineMillis = null
                    if (state == State.NEGOTIATING) {
                        sendSynchronization()
                        if (state != State.DEAD) {
                            synchronizationDeadlineMillis = nowMillis + SYNCHRONIZATION_RESEND_MILLIS
                        }
                    }
                }

                sendAcknowledgementDeadlineMillis?.let { it <= nowMillis } == true -> {
                    sendAcknowledgementDeadlineMillis = null
                    if (state == State.NORMAL) {
                        lastAcknowledgedReceived = lastReceivedInOrder
                        sendAcknowledgement()
                    }
                }

                receiveAcknowledgementDeadlineMillis?.let { it <= nowMillis } == true -> {
                    receiveAcknowledgementDeadlineMillis = null
                    retransmitDuePacket(nowMillis)
                }
            }
        }
    }

    /** Queues a complete raw control-session payload (session id 10). */
    fun sendControl(bytes: ByteArray, nowMillis: Long) {
        sendSession(controlSessionId, bytes, nowMillis)
    }

    /** Queues one raw payload on a negotiated iAP2 session. */
    fun sendSession(sessionId: Int, bytes: ByteArray, nowMillis: Long) {
        require(sessionId in 0..0xff) { "iAP2 session id must fit in one byte" }
        require(bytes.size <= MAX_PAYLOAD_BYTES) {
            "iAP2 session payload exceeds $MAX_PAYLOAD_BYTES bytes"
        }
        if (peerSynchronizationReceived || state == State.NORMAL) {
            require(peerPayloadIsAcceptable(bytes)) {
                "iAP2 session payload exceeds peer maxLength ${peerSynchronization.maxLength}"
            }
        }
        sendPacket(Packet(0, sessionId, bytes.copyOf()), nowMillis)
    }

    private fun parseAvailable(nowMillis: Long): Boolean {
        if (state == State.DEAD) return false
        if (state == State.DETECTING) {
            if (config.startup != Iap2LinkStartup.LEGACY) return parsePhoneDetection(nowMillis)
            if (receive.size < IAP2_MARKER.size) return false
            if (!receive.matches(IAP2_MARKER)) {
                die("iAP2 marker was not received")
                return false
            }
            receive.discard(IAP2_MARKER.size)
            enterNegotiating(nowMillis)
            return true
        }

        val pending = awaitingPayload
        if (pending != null) {
            val payloadWithChecksumBytes = pending.length - HEADER_BYTES
            if (receive.size < payloadWithChecksumBytes) return false
            val payloadWithChecksum = receive.take(payloadWithChecksumBytes)
            awaitingPayload = null
            if (checksumValid(payloadWithChecksum)) {
                processFrame(
                    pending,
                    payloadWithChecksum.copyOf(payloadWithChecksum.size - CHECKSUM_BYTES),
                    nowMillis,
                )
            } else reportPacket("REJECTED", pending, payloadChecksumValid = false)
            return true
        }

        var discarded = false
        while (receive.size >= 2 && (receive[0] != LINK_START_HIGH || receive[1] != LINK_START_LOW)) {
            if (config.negotiateControlSession && state == State.NORMAL &&
                receive[0] == IAP2_MARKER[0] && receive[1] == IAP2_MARKER[1]) {
                if (receive.size < IAP2_MARKER.size) return discarded
                if (receive.matches(IAP2_MARKER)) {
                    /*
                     * A repeated detection marker is a heartbeat on this car.
                     *
                     * Whether to treat it as one is a per-firmware decision, so
                     * it stays behind a config flag — but the flag now means
                     * "echo the marker and keep the link" on both transports
                     * that need it, not just the Wi-Fi command tunnel.
                     *
                     * The 1.3.7 field capture shows what the old behavior cost
                     * on the Buick. The car repeats the marker while the link
                     * is up; the Bluetooth link used to call
                     * `die("车机在控制阶段重新发送 iAP2 检测包，旧链路已结束")`
                     * and drop everything queued behind it:
                     *
                     *     3468 NowPlaying 发送失败：车机在控制阶段重新发送
                     *          iAP2 检测包，旧链路已结束
                     *     5470 (again)
                     *     7472 (again)
                     *
                     * 0x5001 is the media metadata message. Losing it every
                     * two seconds is exactly why the head unit sits on
                     * "正在加载媒体" with no title, artist or artwork, and why
                     * its audio route never settles. The 13.1 s teardown that
                     * follows is the same watchdog the Wi-Fi path was already
                     * fixed for in 1.2.80.
                     *
                     * Vivo keeps the terminal behavior: `vivoReady()` fixtures
                     * in PhoneBluetoothHandshakeTest assert that a repeated
                     * marker ends the link there, and that firmware has not
                     * been observed sending one mid-session.
                     */
                    if (config.ignoreRepeatedDetectionMarker && markerEchoAllowed) {
                        receive.discard(IAP2_MARKER.size)
                        if (!appendOutput(IAP2_MARKER)) return false
                        markerEchoes += 1
                        reportPacket("MARKER-ECHOED", Header(9, CONTROL_ACKNOWLEDGEMENT, 0, 0, 0))
                        discarded = true
                        continue
                    }
                    /*
                     * Before the session is established, a repeated marker is
                     * the car asking for the second round — do exactly that.
                     *
                     * This is the only path that has ever produced 0x4301 on
                     * this car. The working capture reads:
                     *
                     *     2261  send 0x4300
                     *     4891  car repeats the marker -> die() -> rebuild
                     *     4893  second round starts on the same RFCOMM
                     *     5172  re-identify inside it
                     *     6842  0x4301 arrives **in the second round**
                     *
                     * So the marker is not noise to be swallowed (1.3.37) and
                     * the rebuild is not something to put off on a timer
                     * (1.3.38): both of those leave the car waiting. It has to
                     * happen the moment the marker arrives.
                     *
                     * After 0x4301 the echo is switched on, this branch stops
                     * being reached, and the marker means "still alive" again.
                     */
                    die("车机在控制阶段重新发送 iAP2 检测包，旧链路已结束", receive.take(receive.size))
                    return false
                }
            }
            receive.discard(1)
            discarded = true
        }
        if (receive.size < HEADER_BYTES) return discarded
        val rawHeader = receive.take(HEADER_BYTES)
        val header = parseHeader(rawHeader)
        if (header == null || header.length < HEADER_BYTES) return true
        if (header.length > MAX_WIRE_FRAME_BYTES) {
            die("iAP2 frame length ${header.length} exceeds $MAX_WIRE_FRAME_BYTES")
            return false
        }
        if (header.length == HEADER_BYTES) {
            processFrame(header, null, nowMillis)
        } else {
            awaitingPayload = header
        }
        return true
    }

    private fun parsePhoneDetection(nowMillis: Long): Boolean {
        if (receive.size < 2) return false
        if (receive[0] == IAP2_MARKER[0] && receive[1] == IAP2_MARKER[1]) {
            if (receive.size < IAP2_MARKER.size) return false
            if (receive.matches(IAP2_MARKER)) {
                receive.discard(IAP2_MARKER.size)
                if (config.startup == Iap2LinkStartup.ACCESSORY_FIRST) {
                    if (!appendOutput(IAP2_MARKER)) return false
                    // No bare phone SYN: the accessory initiates this profile.
                    respondingToPeerSynchronization = true
                    enterNegotiating(nowMillis, sendImmediately = false)
                } else enterNegotiating(nowMillis)
                return true
            }
        } else if (receive[0] == LINK_START_HIGH && receive[1] == LINK_START_LOW) {
            if (receive.size < HEADER_BYTES) return false
            val header = parseHeader(ByteArray(HEADER_BYTES) { receive[it] })
            if (header != null && (header.control and CONTROL_RESET) != 0) {
                die("peer sent iAP2 reset")
                return false
            }
            if (header != null && header.length > HEADER_BYTES &&
                (header.control and CONTROL_SYNCHRONIZE) != 0) {
                // Some accessories send SYN directly, without a detection marker.
                respondingToPeerSynchronization = true
                enterNegotiating(nowMillis, sendImmediately = false)
                return true
            }
        }
        receive.discard(1)
        return true
    }

    private fun enterNegotiating(nowMillis: Long, sendImmediately: Boolean = true) {
        state = State.NEGOTIATING
        markerDeadlineMillis = null
        if (!sendImmediately) return
        sendSynchronization()
        if (state != State.DEAD) {
            synchronizationDeadlineMillis = nowMillis + SYNCHRONIZATION_RESEND_MILLIS
        }
    }

    private fun processFrame(header: Header, payload: ByteArray?, nowMillis: Long) {
        reportPacket("RECEIVED", header)
        if ((header.control and CONTROL_RESET) != 0) {
            die("peer sent iAP2 reset")
            return
        }
        val confirmsLocalSynchronization = localSynchronizationSequence != null &&
            header.acknowledgement == localSynchronizationSequence
        if ((header.control and CONTROL_SYNCHRONIZE) != 0) {
            if (config.negotiateControlSession &&
                (payload == null || payload.size < 13 || (payload.size - SYNCHRONIZATION_FIXED_BYTES) % SESSION_DESCRIPTOR_BYTES != 0 ||
                    u8(payload[0]) != SYNCHRONIZATION_VERSION)) {
                die("peer iAP2 synchronization payload is malformed")
                return
            }
            if (config.negotiateControlSession && state == State.NORMAL) {
                /*
                 * A SYN in the middle of an active session restarts the link.
                 *
                 * This used to `die()` whenever the payload or sequence
                 * differed from the one negotiated at startup, and that single
                 * call is what tore down every field session:
                 *
                 *     16618 iAP2 RX [phone-command] FAILED: peer changed iAP2
                 *           synchronization during an active session
                 *     19473 AirPlay 会话异常：SocketException: Connection reset
                 *
                 * The car restarts its iAP2 link about 7.5 s in. The reference
                 * sender answers that with a full `link-restart` — clear the
                 * queues, drop the old authentication, negotiate again, then
                 * replay the whole 0xAA00 → 0x1D01 bootstrap — and stays up for
                 * 110 s on the same car.
                 *
                 * Only an identical replayed handshake ACK is a no-op; anything
                 * else is a real restart.
                 */
                val repeated = SynchronizationPayload.decode(payload ?: ByteArray(0))
                if (repeated != null && repeated == peerSynchronization &&
                    header.sequence == peerSynchronizationSequence
                ) {
                    if ((header.control and CONTROL_ACKNOWLEDGEMENT) != 0 && confirmsLocalSynchronization) {
                        sendAcknowledgement()
                    }
                    // A replayed handshake ACK must not replace a newer data acknowledgement.
                    return
                }
                if (repeated != null) {
                    restartLink(repeated, header.sequence, nowMillis, confirmsLocalSynchronization)
                    return
                }
                die("peer iAP2 synchronization payload is undecodable")
                return
            }
            SynchronizationPayload.decode(payload ?: ByteArray(0))?.let {
                val confirmed = config.negotiateControlSession && confirmsLocalSynchronization &&
                    (header.control and CONTROL_ACKNOWLEDGEMENT) != 0
                handleSynchronization(it, header.sequence, nowMillis, confirmed)
            }
            if (state == State.DEAD) return
        }
        if ((header.control and CONTROL_ACKNOWLEDGEMENT) != 0) {
            accumulatedAcknowledgements += 1
            handleAcknowledgement(header.acknowledgement, nowMillis, confirmsLocalSynchronization)
        }
        if ((header.control and CONTROL_EXTENDED_ACKNOWLEDGEMENT) != 0 && payload != null) {
            handleExtendedAcknowledgement(payload)
        }
        if ((header.control and CONTROL_ACKNOWLEDGEMENT.inv()) == 0 && payload != null) {
            handleData(Packet(header.sequence, header.sessionId, payload), nowMillis)
        }
        if (peerSynchronization.maxAcknowledgements > 0 &&
            accumulatedAcknowledgements >= peerSynchronization.maxAcknowledgements
        ) {
            accumulatedAcknowledgements = 0
            lastAcknowledgedReceived = lastReceivedInOrder
            sendAcknowledgement()
        }
    }

    /**
     * True when a SYN seen while the link is up really starts a new link.
     *
     * A plain ACK that happens to carry the SYN bit must not tear anything
     * down, so this only fires for an unacknowledged peer SYN — the shape the
     * car sends when it decides to restart: `flags=0x80`, `ack=0`.
     */
    private fun startsNewPeerLink(sync: SynchronizationPayload, sequence: Int): Boolean =
        sync.sessions.isNotEmpty() && sequence != peerSynchronizationSequence

    /**
     * Tears the current link down and negotiates a fresh one, keeping the
     * transport open. Mirrors the `link-restart`.
     *
     * Everything negotiated on the old link is invalid: the control session
     * id, the peer limits, the retransmission state, the queued and
     * outstanding packets. Authentication is cleared too, because the car
     * restarts the whole bootstrap — its trace shows a second
     * 0xAA00 -> 0xAA01 -> 0xAA03 -> 0xAA05 -> 0x1D00 -> 0x1D01 round.
     */
    private fun restartLink(
        sync: SynchronizationPayload,
        sequence: Int,
        nowMillis: Long,
        confirmsLocalSynchronization: Boolean,
    ) {
        linkRestartEpoch += 1
        queued.clear()
        unacknowledged.clear()
        outOfOrder.clear()
        awaitingPayload = null
        lastSentAcknowledged = sentSequence
        lastReceivedInOrder = sequence
        lastAcknowledgedReceived = sequence
        accumulatedAcknowledgements = 0
        reportPacket("LINK-RESTART", Header(0, CONTROL_SYNCHRONIZE, sequence, 0, 0))
        state = State.NEGOTIATING
        respondingToPeerSynchronization = true
        enterNegotiating(nowMillis, sendImmediately = false)
        peerSynchronization = sync
        peerSynchronizationReceived = true
        peerSynchronizationSequence = sequence
        if (config.negotiateControlSession) {
            sync.sessions.firstOrNull { it.kind == 0 && it.version == 2 && it.id != 0 }
                ?.let { controlSessionId = it.id }
        }
        sendSynchronization()
        synchronizationDeadlineMillis = nowMillis + SYNCHRONIZATION_RESEND_MILLIS
        if (confirmsLocalSynchronization) sendAcknowledgement()
        /*
         * Tell the session above us.
         *
         * Everything negotiated on the old link is gone: the iAP2 session's
         * authentication, its identification and every CSM handler it
         * installed. Without this call the link comes back up and then nobody
         * speaks on it — measured on 1.2.91, where LINK-RESTART fired at
         * 24130 ms and no 0xAA00 ever followed, with the car resetting the
         * connection 2.9 s later.
         */
        runCatching { config.onLinkRestart?.invoke() }
    }

    private fun handleSynchronization(sync: SynchronizationPayload, sequence: Int, nowMillis: Long,
        confirmsLocalSynchronization: Boolean) {
        /*
         * A peer SYN while the link is already up starts a NEW link.
         *
         * The old `if (state != State.NEGOTIATING) return` dropped it silently.
         * an established sender does the opposite and says so in its trace:
         *
         *     +7531ms RX link flags=0x80 seq=73 ack=0 session=0 bytes=16
         *     +7532ms link-restart epoch=1; authentication-and-readiness-cleared
         *     +7532ms link-restart queues-cleared outstanding-cleared
         *              control-decoder-reset; waiting-for-new-handshake
         *     +7533ms TX link flags=0xC0 seq=40 ack=73 ...   (SYN/ACK to the new one)
         *
         * followed by a complete second bootstrap (0xAA00 -> 0xAA01 -> ...
         * -> 0x1D01). This car sends that fresh SYN about 7.5 s into a session
         * and, measured over every field capture, destroys the control
         * connection 13.09-13.16 s after RECORD. Ignoring the restarted
         * handshake is the only thing this sender does differently from the
         * one that survives 110 s on the same car.
         */
        if (state == State.NORMAL && (confirmsLocalSynchronization.not() || startsNewPeerLink(sync, sequence))) {
            restartLink(sync, sequence, nowMillis, confirmsLocalSynchronization)
            return
        }
        if (state != State.NEGOTIATING) return
        if (config.negotiateControlSession) {
            val control = sync.sessions.firstOrNull { it.kind == 0 && it.version == 2 && it.id != 0 }
            if (sync.maxOutgoing == 0 || sync.maxLength <= HEADER_BYTES + CHECKSUM_BYTES || control == null) {
                die("peer iAP2 synchronization has no usable control session")
                return
            }
            controlSessionId = control.id
        }
        peerSynchronization = sync
        peerSynchronizationReceived = true
        peerSynchronizationSequence = sequence
        lastReceivedInOrder = sequence
        lastAcknowledgedReceived = sequence
        if (respondingToPeerSynchronization && !confirmsLocalSynchronization) {
            sendSynchronization()
            synchronizationDeadlineMillis = nowMillis + SYNCHRONIZATION_RESEND_MILLIS
        } else sendAcknowledgement()
    }

    private fun handleAcknowledgement(acknowledgement: Int, nowMillis: Long, confirmsLocalSynchronization: Boolean) {
        if (state == State.NEGOTIATING) {
            if (config.negotiateControlSession && (!confirmsLocalSynchronization || !peerSynchronizationReceived)) return
            if (!peerSynchronizationReceived || !peerLimitsAreUsable() || queued.any { !peerPayloadIsAcceptable(it.payload) }) {
                die("peer iAP2 maxLength cannot carry pending control packets")
                return
            }
            state = State.NORMAL
            synchronizationDeadlineMillis = null
            setWritable(true)
        }
        if (config.maximumInFlightPackets != null && state == State.NORMAL) {
            val previous = lastSentAcknowledged
            if (previous != null && sequenceDistance(acknowledgement, previous) > sequenceDistance(sentSequence, previous)) {
                // A stale ACK or one for unsent data cannot release the phone's pending queue.
                return
            }
        }
        lastSentAcknowledged = acknowledgement

        var rearmed = false
        while (unacknowledged.isNotEmpty()) {
            val first = unacknowledged.first()
            val distance = sequenceDistance(first.sequence, lastSentAcknowledged)
            if (distance > 0 && distance <= peerSynchronization.maxAcknowledgements + ACK_SLACK) {
                receiveAcknowledgementDeadlineMillis = first.deadlineMillis
                rearmed = true
                break
            }
            unacknowledged.removeAt(0)
        }
        if (!rearmed) receiveAcknowledgementDeadlineMillis = null

        while (sequenceDistance(sentSequence, lastSentAcknowledged) < outgoingWindowLimit()) {
            val packet = queued.pollFirst() ?: break
            sendPacket(packet, nowMillis)
            setWritable(true)
        }
    }

    private fun handleExtendedAcknowledgement(missing: ByteArray) {
        if (state != State.NORMAL) return
        for (packet in unacknowledged) {
            if (!missing.any { u8(it) == packet.sequence }) continue
            packet.retransmissions += 1
            if (packet.retransmissions == peerSynchronization.maxRetransmissions) {
                die("iAP2 packet ${packet.sequence} was not acknowledged")
                return
            }
            sendData(packet.sequence, packet.sessionId, packet.payload)
            sendAcknowledgementDeadlineMillis = null
            receiveAcknowledgementDeadlineMillis = packet.deadlineMillis
        }
    }

    private fun handleData(packet: Packet, nowMillis: Long) {
        val distance = sequenceDistance(packet.sequence, lastReceivedInOrder)
        if (distance == 0 || distance > peerSynchronization.maxOutgoing + ACK_SLACK) {
            sendAcknowledgement()
            return
        }
        if (outOfOrder.any { it.sequence == packet.sequence }) return
        if (outOfOrder.size >= config.maximumOutOfOrderPackets) {
            die("iAP2 out-of-order packet limit exceeded")
            return
        }
        outOfOrder += packet
        if (distance > 1) {
            if (distance >= peerSynchronization.maxOutgoing) {
                val missing = ArrayList<Int>()
                var sequence = lastReceivedInOrder
                while (sequenceDistance(packet.sequence, sequence) > 1) {
                    sequence = nextSequence(sequence)
                    missing += sequence
                }
                sendAcknowledgementDeadlineMillis = null
                sendExtendedAcknowledgement(missing)
            }
            return
        }

        outOfOrder.sortBy { sequenceDistance(it.sequence, lastReceivedInOrder) }
        while (outOfOrder.isNotEmpty() && sequenceDistance(outOfOrder.first().sequence, lastReceivedInOrder) == 1) {
            val inOrder = outOfOrder.removeAt(0)
            lastReceivedInOrder = inOrder.sequence
            if (inOrder.sessionId == controlSessionId) {
                enqueueEvent(Event.Control(inOrder.payload))
            } else {
                enqueueEvent(Event.Session(inOrder.sessionId, inOrder.payload))
            }
        }

        if (peerSynchronization.maxAcknowledgements == 0) return
        val windowBeforeForcedAck = (peerSynchronization.maxOutgoing - config.maxOutgoingDelta).coerceAtLeast(1)
        if (sequenceDistance(lastReceivedInOrder, lastAcknowledgedReceived) >= windowBeforeForcedAck) {
            sendAcknowledgementDeadlineMillis = null
            lastAcknowledgedReceived = lastReceivedInOrder
            sendAcknowledgement()
        } else {
            sendAcknowledgementDeadlineMillis = nowMillis + peerSynchronization.acknowledgementTimeoutMillis
        }
    }

    private fun sendPacket(packet: Packet, nowMillis: Long) {
        val outstanding = sequenceDistance(sentSequence, lastSentAcknowledged)
        val full = if (config.maximumInFlightPackets == null) outstanding > peerSynchronization.maxOutgoing
            else outstanding >= outgoingWindowLimit()
        if (state != State.NORMAL ||
            full
        ) {
            if (queued.size >= config.maximumQueuedPackets) {
                die("iAP2 outbound queue limit exceeded")
            } else {
                queued += packet
                setWritable(false)
            }
            return
        }
        sentSequence = nextSequence(sentSequence)
        packet.sequence = sentSequence
        packet.retransmissions = 0
        packet.deadlineMillis = nowMillis + peerSynchronization.retransmissionTimeoutMillis
        sendAcknowledgementDeadlineMillis = null
        sendData(packet.sequence, packet.sessionId, packet.payload)
        lastAcknowledgedReceived = lastReceivedInOrder
        if (peerSynchronization.maxRetransmissions > 0) {
            unacknowledged += packet
            receiveAcknowledgementDeadlineMillis = packet.deadlineMillis
        } else {
            lastSentAcknowledged = sentSequence
        }
    }

    private fun outgoingWindowLimit(): Int =
        config.maximumInFlightPackets?.let { minOf(it, peerSynchronization.maxOutgoing) }
            ?: peerSynchronization.maxOutgoing

    private fun retransmitDuePacket(nowMillis: Long) {
        if (state != State.NORMAL || unacknowledged.isEmpty()) return
        val next = unacknowledged.minByOrNull { it.deadlineMillis } ?: return
        val secondDeadline = unacknowledged.filter { it !== next }.minOfOrNull { it.deadlineMillis }
        next.deadlineMillis = nowMillis + peerSynchronization.retransmissionTimeoutMillis
        next.retransmissions += 1
        if (next.retransmissions == peerSynchronization.maxRetransmissions) {
            die("iAP2 packet ${next.sequence} was not acknowledged")
            return
        }
        sendData(next.sequence, next.sessionId, next.payload)
        receiveAcknowledgementDeadlineMillis = secondDeadline ?: next.deadlineMillis
    }

    /**
     * Lets a repeated detection marker be echoed as a heartbeat.
     *
     * Called once the session is established — specifically when the wireless
     * configuration (0x4301) has arrived. Before that the echo must stay off:
     * see [markerEchoAllowed].
     */
    fun allowRepeatedMarkerEcho() {
        markerEchoAllowed = true
    }

    private fun sendSynchronization() {
        val answering = respondingToPeerSynchronization && peerSynchronizationReceived
        val control = CONTROL_SYNCHRONIZE or if (answering) CONTROL_ACKNOWLEDGEMENT else 0
        // SYN|ACK confirms the peer's sequence, while its payload advertises our own limits.
        writePacket(
            localSynchronization().encode(),
            sentSequence,
            control,
            0,
        )
        if (state != State.DEAD) localSynchronizationSequence = sentSequence
    }

    private fun sendAcknowledgement() {
        /*
         * Whether a pure ACK consumes a sequence number is a per-transport
         * decision on this car.
         *
         * The Bluetooth link keeps repeating the last sent sequence: that is
         * what a real iPhone does over RFCOMM, and the handshake test
         * `hardwareTraceSequencePatternKeepsAckAt99AndIdentificationAt100`
         * pins it from an actual capture.
         *
         * The Wi-Fi command channel is the other way round. The reference
         * sender's trace there shows acknowledgements sharing one increasing
         * sequence space with its data packets:
         *
         *     data  seq=100 101 102 103 104 105
         *     ack   seq=100 102 103 104 109 109
         *
         * …and it is the only transport where we have never received a single
         * CSM: no 0x1D01 in reply to 0x1D00, in every session, on every video
         * source. Repeating 99 forever is the one thing that differs there.
         */
        if (advanceSequenceOnAcknowledgement) {
            sentSequence = nextSequence(sentSequence)
            /*
             * …and it must not count against the peer's receive window.
             *
             * A pure acknowledgement carries no payload, so the peer never
             * acknowledges it back. Advancing the sequence without crediting it
             * leaves `outstanding = sentSequence - lastSentAcknowledged` growing
             * by one per second with nobody able to shrink it, until the window
             * (this car declares 5) is full and every real message is parked in
             * the queue forever:
             *
             *     +5888ms  link NORMAL, peerWindow=5
             *     +5943ms  0x1D00 sent — no PACKET QUEUED follows
             *     +6913ms  car still says ack=99; we are at 101
             *     …        car repeats seq=175 ack=99 once a second until teardown
             *
             * Crediting the sequence here keeps the window measuring what it is
             * supposed to measure: unacknowledged *data*.
             */
            lastSentAcknowledged = sentSequence
        }
        writePacket(null, sentSequence, CONTROL_ACKNOWLEDGEMENT, 0)
    }

    private fun sendExtendedAcknowledgement(missing: List<Int>) {
        if (advanceSequenceOnAcknowledgement) {
            sentSequence = nextSequence(sentSequence)
            lastSentAcknowledged = sentSequence
        }
        writePacket(missing.map { it.toByte() }.toByteArray(), sentSequence, CONTROL_EXTENDED_ACKNOWLEDGEMENT, 0)
    }

    private fun sendData(sequence: Int, sessionId: Int, payload: ByteArray) {
        writePacket(payload, sequence, CONTROL_ACKNOWLEDGEMENT, sessionId)
    }

    private fun writePacket(payload: ByteArray?, sequence: Int, control: Int, sessionId: Int) {
        if (state == State.DEAD) return
        accumulatedAcknowledgements = 0
        val length = payload?.size?.plus(HEADER_BYTES + CHECKSUM_BYTES) ?: HEADER_BYTES
        val header = ByteArray(HEADER_BYTES)
        header[0] = LINK_START_HIGH
        header[1] = LINK_START_LOW
        header[2] = (length ushr 8).toByte()
        header[3] = length.toByte()
        header[4] = control.toByte()
        header[5] = sequence.toByte()
        header[6] = lastReceivedInOrder.toByte()
        header[7] = sessionId.toByte()
        header[8] = checksum(header, HEADER_BYTES - CHECKSUM_BYTES).toByte()
        val frame = ByteArrayOutputStream(length)
        frame.write(header)
        if (payload != null) {
            frame.write(payload)
            frame.write(checksum(payload, payload.size))
        }
        if (appendOutput(frame.toByteArray())) reportPacket("QUEUED", Header(length, control, sequence, lastReceivedInOrder, sessionId))
    }

    private fun reportPacket(action: String, header: Header, payloadChecksumValid: Boolean = true) {
        val observer = onPacketMetadata ?: return
        try { observer(Iap2PacketMetadata(action, header.control, header.sequence, header.acknowledgement,
            header.sessionId, header.length, state, payloadChecksumValid)) }
        catch (_: Exception) { /* Header diagnostics cannot change protocol semantics. */ }
    }

    private fun setWritable(value: Boolean) {
        if (writable == value) return
        writable = value
        enqueueEvent(Event.Writable(value))
    }

    private fun die(reason: String?, restartInput: ByteArray? = null) {
        if (state == State.DEAD) return
        markerDeadlineMillis = null
        synchronizationDeadlineMillis = null
        sendAcknowledgementDeadlineMillis = null
        receiveAcknowledgementDeadlineMillis = null
        state = State.DEAD
        writable = false
        output.reset()
        events.clear()
        events += Event.Dead(reason, restartInput)
    }

    private fun appendOutput(bytes: ByteArray): Boolean {
        if (state == State.DEAD) return false
        if (bytes.size > config.maximumPendingOutputBytes - output.size()) {
            die("iAP2 pending output limit exceeded")
            return false
        }
        output.write(bytes)
        return true
    }

    private fun enqueueEvent(event: Event) {
        if (state == State.DEAD) return
        if (events.size >= config.maximumPendingEvents) {
            die("iAP2 pending event limit exceeded")
            return
        }
        events += event
    }

    // A usable session-10 link must carry at least one control byte in addition to its frame
    // header and payload checksum.  This also gives higher layers a positive chunk limit.
    private fun peerLimitsAreUsable(): Boolean = peerSynchronization.maxLength > HEADER_BYTES + CHECKSUM_BYTES

    private fun peerPayloadIsAcceptable(payload: ByteArray): Boolean =
        peerLimitsAreUsable() && payload.size <= peerSynchronization.maxLength - HEADER_BYTES - CHECKSUM_BYTES

    private fun localSynchronization() = config.synchronizationOverride ?: SynchronizationPayload(
        maxOutgoing = config.maxOutgoing,
        maxLength = MAX_WIRE_FRAME_BYTES,
        retransmissionTimeoutMillis = if (config.zeroAcknowledgements) 0 else DEFAULT_RETRANSMISSION_TIMEOUT_MILLIS,
        acknowledgementTimeoutMillis = if (config.zeroAcknowledgements) 0 else config.acknowledgementTimeoutMillis,
        maxRetransmissions = if (config.zeroAcknowledgements) 0 else DEFAULT_MAX_RETRANSMISSIONS,
        maxAcknowledgements = if (config.zeroAcknowledgements) 0 else DEFAULT_MAX_ACKNOWLEDGEMENTS,
        sessions = listOf(
            SessionDescriptor(CONTROL_SESSION_ID, 0, config.controlSessionVersion),
            SessionDescriptor(EA_SESSION_ID, 2, 1),
            SessionDescriptor(FILE_TRANSFER_SESSION_ID, 1, 2),
        ),
    )

    private class ReceiveBuffer {
        private val bytes = ByteArray(MAX_WIRE_FRAME_BYTES)
        private var head = 0
        private var tail = 0

        val size: Int get() = tail - head

        fun remainingCapacity(): Int {
            compact()
            return bytes.size - tail
        }

        operator fun get(index: Int): Byte = bytes[head + index]

        fun append(source: ByteArray, offset: Int, count: Int) {
            require(count <= remainingCapacity())
            source.copyInto(bytes, tail, offset, offset + count)
            tail += count
        }

        fun discard(count: Int) {
            require(count in 0..size)
            head += count
            if (head == tail) {
                head = 0
                tail = 0
            }
        }

        fun take(count: Int): ByteArray {
            require(count in 0..size)
            return bytes.copyOfRange(head, head + count).also { discard(count) }
        }

        fun matches(expected: ByteArray): Boolean = expected.indices.all { bytes[head + it] == expected[it] }

        private fun compact() {
            if (head == 0) return
            if (head < tail) bytes.copyInto(bytes, 0, head, tail)
            tail -= head
            head = 0
        }
    }

    companion object {
        const val CONTROL_SESSION_ID = 10
        const val EA_SESSION_ID = 11
        const val FILE_TRANSFER_SESSION_ID = 12

        val IAP2_MARKER = byteArrayOf(0xff.toByte(), 0x55, 0x02, 0x00, 0xee.toByte(), 0x10)

        private const val HEADER_BYTES = 9
        private const val CHECKSUM_BYTES = 1
        const val MAX_WIRE_FRAME_BYTES = 0xffff
        const val MAX_PAYLOAD_BYTES = MAX_WIRE_FRAME_BYTES - HEADER_BYTES - CHECKSUM_BYTES
        private const val SESSION_DESCRIPTOR_BYTES = 3
        private const val SYNCHRONIZATION_FIXED_BYTES = 10
        private const val SYNCHRONIZATION_VERSION = 1
        private const val LINK_START_HIGH: Byte = 0xff.toByte()
        private const val LINK_START_LOW: Byte = 0x5a
        private const val CONTROL_SYNCHRONIZE = 0x80
        private const val CONTROL_ACKNOWLEDGEMENT = 0x40
        private const val CONTROL_EXTENDED_ACKNOWLEDGEMENT = 0x20
        private const val CONTROL_RESET = 0x10
        private const val INITIAL_SEQUENCE = 99
        private const val MARKER_RESEND_MILLIS = 1_000L
        private const val SYNCHRONIZATION_RESEND_MILLIS = 500L
        private const val DEFAULT_RETRANSMISSION_TIMEOUT_MILLIS = 4_000
        private const val DEFAULT_MAX_RETRANSMISSIONS = 4
        private const val DEFAULT_MAX_ACKNOWLEDGEMENTS = 3
        private const val ACK_SLACK = 10

        private fun parseHeader(bytes: ByteArray): Header? {
            if (bytes.size != HEADER_BYTES || !checksumValid(bytes)) return null
            if (bytes[0] != LINK_START_HIGH || bytes[1] != LINK_START_LOW) return null
            return Header(
                length = readU16(bytes, 2),
                control = u8(bytes[4]),
                sequence = u8(bytes[5]),
                acknowledgement = u8(bytes[6]),
                sessionId = u8(bytes[7]),
            )
        }

        private fun checksumValid(bytes: ByteArray): Boolean = bytes.fold(0) { sum, byte -> (sum + u8(byte)) and 0xff } == 0

        private fun checksum(bytes: ByteArray, count: Int): Int =
            (-bytes.take(count).sumOf { u8(it) }) and 0xff

        private fun sequenceDistance(sequence: Int, previous: Int?): Int =
            if (previous == null) 0 else (sequence - previous) and 0xff

        private fun nextSequence(sequence: Int): Int = (sequence + 1) and 0xff

        private fun readU16(bytes: ByteArray, offset: Int): Int = (u8(bytes[offset]) shl 8) or u8(bytes[offset + 1])

        private fun u8(byte: Byte): Int = byte.toInt() and 0xff

        private fun writeU16(out: ByteArrayOutputStream, value: Int) {
            out.write(value ushr 8)
            out.write(value)
        }
    }
}

/** Configuration advertised by the local link endpoint. */
data class Iap2LinkConfig(
    val maxOutgoing: Int = 30,
    val maxOutgoingDelta: Int = 0,
    val acknowledgementTimeoutMillis: Int = 500,
    val zeroAcknowledgements: Boolean = false,
    val controlSessionVersion: Int = 1,
    val maximumQueuedPackets: Int = 64,
    val maximumOutOfOrderPackets: Int = 64,
    val maximumPendingOutputBytes: Int = 1_048_576,
    val maximumPendingEvents: Int = 256,
    val startup: Iap2LinkStartup = Iap2LinkStartup.LEGACY,
    val synchronizationOverride: Iap2LinkEngine.SynchronizationPayload? = null,
    val negotiateControlSession: Boolean = false,
    /**
     * Some AirPlay command tunnels repeat the six-byte detection marker after
     * negotiation. Bluetooth profiles keep the historical terminal behavior;
     * only the explicitly selected Wi-Fi command profile enables this.
     */
    val ignoreRepeatedDetectionMarker: Boolean = false,
    /**
     * Whether a pure ACK consumes a sequence number.
     *
     * False on Bluetooth (the iPhone repeats its last sequence there and a
     * captured handshake pins it), true on the Wi-Fi command channel
     * (an established sender advances there and that link has never once
     * produced a CSM).
     */
    val advanceSequenceOnAcknowledgement: Boolean = false,
    /** Optional phone send limit; never replaces the peer's negotiated synchronization fields. */
    val maximumInFlightPackets: Int? = null,
    /**
     * Called when the peer restarts the link under us.
     *
     * The link layer can only clear its own queues; the iAP2 session above it
     * (authentication, identification, the CSM handlers) still believes it is
     * ready and will never speak again on the new link. an established sender
     * replays the whole bootstrap after a restart, so the owner has to be told.
     */
    val onLinkRestart: (() -> Unit)? = null,
) {
    init {
        require(maxOutgoing in 0..0xff)
        require(maxOutgoingDelta in 0..0xff)
        require(acknowledgementTimeoutMillis in 0..0xffff)
        require(controlSessionVersion in 0..0xff)
        require(maximumQueuedPackets in 1..256)
        require(maximumOutOfOrderPackets in 1..256)
        require(maximumPendingOutputBytes >= 1)
        require(maximumPendingEvents >= 1)
        require(maximumInFlightPackets == null || maximumInFlightPackets in 1..0xff)
    }
}

