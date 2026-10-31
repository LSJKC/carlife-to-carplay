package com.carplay.iap2.transport

/** Explicit phone compatibility profiles; legacy receiver factories retain their original configuration. */
enum class PhoneBluetoothProfile(val label: String, val readyTimeoutMillis: Long) {
    ACCESSORY_FIRST("accessory-first", 25_000),
    VIVO_ACCESSORY_FIRST("vivo-accessory-first", 25_000),
    COMMAND_TUNNEL("command-tunnel", 25_000),
    CAPTURE_IMMEDIATE("detection-then-phone-syn", 10_000),
    LEGACY_CONTROLLER("legacy-immediate-controller", 10_000);

    val accessoryInitiates: Boolean
        get() = this == ACCESSORY_FIRST || this == VIVO_ACCESSORY_FIRST || this == COMMAND_TUNNEL

    fun attempts(): List<PhoneBluetoothProfile> = when (this) {
        ACCESSORY_FIRST, VIVO_ACCESSORY_FIRST, COMMAND_TUNNEL -> listOf(this, CAPTURE_IMMEDIATE, LEGACY_CONTROLLER)
        CAPTURE_IMMEDIATE -> listOf(this, LEGACY_CONTROLLER)
        LEGACY_CONTROLLER -> listOf(this)
    }

    internal fun linkConfig(): Iap2LinkConfig {
        if (this == LEGACY_CONTROLLER) return Iap2LinkConfig(maxOutgoing = 4, controlSessionVersion = 2)
        val responder = accessoryInitiates
        val synchronization = Iap2LinkEngine.SynchronizationPayload(
            maxOutgoing = if (this == VIVO_ACCESSORY_FIRST) 5 else 127,
            maxLength = if (this == ACCESSORY_FIRST || this == COMMAND_TUNNEL) {
                Iap2LinkEngine.MAX_WIRE_FRAME_BYTES
            } else 2048,
            retransmissionTimeoutMillis = if (responder) 1500 else 2000,
            acknowledgementTimeoutMillis = if (responder) 73 else 256,
            maxRetransmissions = 30,
            maxAcknowledgements = if (responder) 3 else 1,
            /*
             * The command tunnel declares the same two sessions the car does.
             *
             * It used to add a third, session 12 (file transfer), which the
             * accessory never declares. The link layer accepts our SYN either
             * way, so the difference is invisible in the handshake — but on the
             * Wi-Fi command transport the car then answered *nothing* at the
             * CSM layer for the life of the link. Field capture 2026-10-30,
             * same car, same minute:
             *
             *   ours   SYN 29B, sessions=1,2,12 -> 0x1D00 sent, 0x1D01 never
             *          arrives, `已收CSM=0` for 10 s, car sends LINK-RESTART
             *   theirs SYN 16B, sessions=1,2   -> 0x1D01 arrives 26 ms later,
             *          link stays up for 975 s with no restart
             *
             * `Iap2CsmChannel.fileTransferSessionId` binds to the declared
             * override and falls back to 12 when none is present, so file
             * transfer still addresses the same session without advertising
             * it.
             */
            sessions = when (this) {
                ACCESSORY_FIRST, VIVO_ACCESSORY_FIRST, COMMAND_TUNNEL -> listOf(
                    Iap2LinkEngine.SessionDescriptor(1, 0, 2),
                    Iap2LinkEngine.SessionDescriptor(2, 1, 2),
                )
                else -> listOf(Iap2LinkEngine.SessionDescriptor(1, 0, 2))
            },
        )
        return Iap2LinkConfig(
            maxOutgoing = synchronization.maxOutgoing,
            acknowledgementTimeoutMillis = synchronization.acknowledgementTimeoutMillis,
            controlSessionVersion = 2,
            startup = if (responder) Iap2LinkStartup.ACCESSORY_FIRST else Iap2LinkStartup.DETECTION_THEN_SYN,
            synchronizationOverride = synchronization,
            negotiateControlSession = true,
            /*
             * Only the Wi-Fi command tunnel echoes a repeated marker.
             *
             * 1.3.9 extended this to ACCESSORY_FIRST on the theory that the
             * Buick repeats the marker on Bluetooth too and was being killed by
             * the terminal behavior. That was wrong, and 1.3.7 versus 1.3.14
             * proves it: the same car, the same app otherwise —
             *
             *     1.3.7   no echo on Bluetooth -> the link layer calls die(),
             *             the caller rebuilds, and the car starts its second
             *             iAP2 round at 2354 ms. Connection succeeds.
             *
             *     1.3.14+ echoes the marker at 2572 ms -> the car answers with
             *             LINK-RESTART at 2637 ms and then goes completely
             *             silent. It never starts the second round, and the
             *             session dies on the 20 s watchdog.
             *
             * So on this firmware the repeated marker IS the car asking for the
             * link to be torn down and rebuilt. Answering it keeps a link alive
             * that the car has already abandoned, which is the one thing that
             * stops the rebuild from happening.
             *
             * COMMAND_TUNNEL keeps the echo: there dropping the marker is what
             * produced the 12.9 s teardown fixed in 1.2.80, and the two
             * transports demonstrably want opposite behavior.
             */
            /*
             * The command channel also advances its sequence on pure ACKs.
             *
             * Both facts come from the same trace and both were wrong here for
             * the same reason — the Bluetooth behaviour had been generalised to
             * every transport. On this car the Wi-Fi command link answers
             * nothing at the CSM layer, and it is the only place these two
             * settings differ from Bluetooth.
             */
            /*
             * Echo the marker on every transport, but only while the link is
             * NORMAL.
             *
             * The 1.3.7 versus 1.3.14 comparison was about the handshake: the
             * car sends a marker while negotiating, and answering it there
             * keeps a link alive that the car has already abandoned, which
             * stops the rebuild. That finding still holds.
             *
             * This is the other phase. Once the control session is up the car
             * keeps repeating the marker as a heartbeat, and the old terminal
             * behaviour called die() on it. Every message queued behind it went
             * with the link — 0x5001 media updates above all:
             *
             *     13388  0x5001 NowPlayingUpdate FAILED:
             *            车机在控制阶段重新发送 iAP2 检测包，旧链路已结束
             *     13389  NowPlaying 发送失败
             *
             * That is why the media card sits on "正在加载" and why the session
             * ends at 13.1 s: the link dies on a heartbeat.
             *
             * Ignoring the marker during negotiation is unchanged — the flag
             * only reaches the NORMAL-state branch.
             */
            /*
             * vivo keeps the terminal behaviour: its fixtures assert that a
             * repeated marker ends the link (PhoneBluetoothHandshakeTest
             * vivoReady cases), and that firmware has not been observed sending
             * one mid-session. Every other profile echoes it — see the comment
             * above.
             */
            ignoreRepeatedDetectionMarker = this != VIVO_ACCESSORY_FIRST,
            /*
             * A pure acknowledgement repeats the last sequence on this car.
             *
             * This was set to true for COMMAND_TUNNEL on the theory that the
             * reference sender advances its acknowledgement sequence. Its trace
             * says the opposite, and the difference is what keeps 0x1d01 from
             * ever arriving:
             *
             *   reference  rx ctl=64 seq=104 ack=99  sid=0 bytes=0
             *              tx ctl=64 seq=100 ack=104 sid=1 bytes=6   <- 0x1d00
             *              rx ctl=64 seq=105 ack=100 sid=1 bytes=752 <- 0x1d01
             *              tx ctl=64 seq=100 ack=105 sid=0 bytes=0   <- pure ACK
             *              tx ctl=64 seq=101 ack=105 sid=1 bytes=6
             *
             *              Every pure ACK reuses the sequence it just answered
             *              with; only frames carrying a payload advance it.
             *
             *   ours       tx ctl=64 seq=100 ack=19  sid=0 bytes=0   <- pure ACK
             *              tx ctl=64 seq=101 ack=19  sid=1 bytes=16  <- 0x1d00
             *              car ack=99 forever, 0x1d00 re-sent every 1.7 s
             *
             *              Advancing on the pure ACK pushed 0x1d00 from 100 to
             *              101, and the car never acknowledged anything past 99.
             */
            advanceSequenceOnAcknowledgement = false,
            /*
             * Only the Bluetooth accessory-first link answers a marker with a
             * SYN, and only when the lease was opened by us — which on this
             * phone means the second iAP2 round, see
             * [Iap2LinkEngine.allowOpenerSynchronization].
             *
             * The Wi-Fi command tunnel is opened by the car sending the marker,
             * and an established sender answers that with the marker alone.
             */
            answerMarkerWithSynchronization = this == ACCESSORY_FIRST || this == COMMAND_TUNNEL,
            maximumInFlightPackets = 1,
        )
    }
}

enum class Iap2LinkStartup { LEGACY, ACCESSORY_FIRST, DETECTION_THEN_SYN }

/** Counts are taken at the protocol stream boundary; peer fields are absent until a valid SYN is received. */
data class Iap2HandshakeStatus(
    val state: Iap2LinkEngine.State = Iap2LinkEngine.State.IDLE,
    val sentBytes: Long = 0,
    val receivedBytes: Long = 0,
    val peerSynchronization: Iap2LinkEngine.SynchronizationPayload? = null,
    val controlSessionId: Int? = null,
)
