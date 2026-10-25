package com.carplay.adapter.carlife

/**
 * Coalesces encoder rebuild requests until CarLife emits the rebuilt IDR.
 *
 * Once an IDR completes a rebuild, a later independent key-frame request must
 * be allowed. Blocking that request as a late duplicate can leave AirPlay
 * waiting for a new CONFIG while every subsequent IDR is discarded.
 */
internal class KeyFrameRebuildGate {
    private var inFlight = false
    private var lastRequestNanos = 0L

    @Synchronized
    fun request(): Boolean {
        if (inFlight) return false
        inFlight = true
        lastRequestNanos = System.nanoTime()
        return true
    }

    /** Allows an explicit requestUI retry after a rebuild stalled before its IDR. */
    @Synchronized
    fun beginRetry(): Boolean {
        if (inFlight && System.nanoTime() - lastRequestNanos < RETRY_MIN_INTERVAL_NANOS) return false
        inFlight = true
        lastRequestNanos = System.nanoTime()
        return true
    }

    @Synchronized
    fun observeFrame(keyFrame: Boolean) {
        if (keyFrame) inFlight = false
    }

    @Synchronized
    fun reset() {
        inFlight = false
        lastRequestNanos = 0L
    }

    @Synchronized
    fun isInFlight(): Boolean = inFlight

    private companion object {
        const val RETRY_MIN_INTERVAL_NANOS = 1_000_000_000L
    }
}
