package com.carplay.adapter.carlife

/**
 * Serializes CarLife's encoder lifecycle.
 *
 * A restore request can arrive while the first encoder is still being
 * created. Sending another VIDEO_ENCODER_INIT in that window makes CarLife
 * replace the first codec with its 20 fps default. Ignore restores until the
 * initial START has completed; the car can request recovery again afterwards.
 */
internal class CarLifeEncoderLifecycle {
    private enum class Phase {
        IDLE,
        INIT_SENT,
        WAITING_FOR_INITIAL_START,
        STREAMING,
        REBUILD_SENT,
        WAITING_FOR_REBUILD_START,
    }

    private var phase = Phase.IDLE
    @Synchronized
    fun requestInitial(): Boolean {
        if (phase != Phase.IDLE) return false
        phase = Phase.INIT_SENT
        return true
    }

    @Synchronized
    fun requestRebuild(): Boolean {
        when (phase) {
            Phase.IDLE,
            Phase.INIT_SENT,
            Phase.WAITING_FOR_INITIAL_START -> return false
            Phase.STREAMING -> {
                phase = Phase.REBUILD_SENT
                return true
            }
            Phase.REBUILD_SENT,
            Phase.WAITING_FOR_REBUILD_START -> return false
        }
    }

    @Synchronized
    fun onEncoderInitDone(): Boolean {
        phase = when (phase) {
            Phase.INIT_SENT -> Phase.WAITING_FOR_INITIAL_START
            Phase.REBUILD_SENT -> Phase.WAITING_FOR_REBUILD_START
            else -> return false
        }
        return true
    }

    /** Must be called after VIDEO_ENCODER_START has been written. */
    @Synchronized
    fun onEncoderStarted(): Boolean {
        when (phase) {
            Phase.WAITING_FOR_INITIAL_START -> Unit
            Phase.WAITING_FOR_REBUILD_START -> Unit
            else -> return false
        }
        phase = Phase.STREAMING
        return false
    }

    @Synchronized
    fun reset() {
        phase = Phase.IDLE
    }
}
