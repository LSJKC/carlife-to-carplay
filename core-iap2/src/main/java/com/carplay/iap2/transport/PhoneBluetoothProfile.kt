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
            sessions = when (this) {
                ACCESSORY_FIRST, VIVO_ACCESSORY_FIRST -> listOf(
                    Iap2LinkEngine.SessionDescriptor(1, 0, 2),
                    Iap2LinkEngine.SessionDescriptor(2, 1, 2),
                )
                COMMAND_TUNNEL -> listOf(
                    Iap2LinkEngine.SessionDescriptor(1, 0, 2),
                    Iap2LinkEngine.SessionDescriptor(2, 1, 2),
                    Iap2LinkEngine.SessionDescriptor(Iap2LinkEngine.FILE_TRANSFER_SESSION_ID, 1, 2),
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
            ignoreRepeatedDetectionMarker = this == COMMAND_TUNNEL,
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
