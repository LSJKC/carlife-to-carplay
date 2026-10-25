package com.carlink.cast

/**
 * Debounces Android's repeated ACL_CONNECTED broadcasts for one car.
 *
 * A single vehicle connection can produce several broadcasts while the
 * wireless CarPlay handoff is still being negotiated. Starting a second
 * CastService generation for each one makes the Bluetooth link and CarLife
 * listener compete with themselves.
 */
internal object AutoConnectCoordinator {
    private const val CLAIM_WINDOW_MS = 20_000L
    /*
     * A failed wireless handoff normally produces a second ACL_CONNECTED
     * broadcast about 30 seconds later.  Retrying on that edge immediately
     * creates the Bluetooth/Wi-Fi storm seen in the Display logs, so automatic
     * retries use a bounded exponential backoff. Manual starts never consult
     * this value.
     */
    private const val FAILURE_BACKOFF_MS = 60_000L
    private const val MAX_FAILURE_BACKOFF_MS = 180_000L
    private const val DUPLICATE_FAILURE_WINDOW_MS = 5_000L
    private val lock = Any()
    private var claimedAddress: String? = null
    private var claimedAt = 0L
    private val retryNotBefore = mutableMapOf<String, Long>()
    private val failureCount = mutableMapOf<String, Int>()
    private val lastFailureAt = mutableMapOf<String, Long>()

    fun claim(address: String): Boolean = synchronized(lock) {
        val now = android.os.SystemClock.elapsedRealtime()
        val key = address.normalized()
        val blockedUntil = retryNotBefore[key] ?: 0L
        if (blockedUntil > now) return false
        if (blockedUntil != 0L) retryNotBefore.remove(key)
        if (claimedAddress.equals(address, ignoreCase = true) &&
            now - claimedAt < CLAIM_WINDOW_MS
        ) return false
        claimedAddress = address
        claimedAt = now
        true
    }

    /**
     * Records an automatic-attempt failure without making the user wait when
     * they press the manual connect button.  ACL_CONNECTED broadcasts are the
     * only callers that consult this cooldown.
     */
    fun recordFailure(address: String, delayMillis: Long = FAILURE_BACKOFF_MS) = synchronized(lock) {
        val now = android.os.SystemClock.elapsedRealtime()
        val key = address.normalized()
        val previous = lastFailureAt[key] ?: 0L
        if (now - previous < DUPLICATE_FAILURE_WINDOW_MS) {
            if (claimedAddress.equals(address, ignoreCase = true)) {
                claimedAddress = null
                claimedAt = 0L
            }
            return
        }
        lastFailureAt[key] = now
        val count = (failureCount[key] ?: 0) + 1
        failureCount[key] = count
        val multiplier = 1L shl (count - 1).coerceIn(0, 2)
        val delay = (delayMillis.coerceAtLeast(0L) * multiplier)
            .coerceAtMost(MAX_FAILURE_BACKOFF_MS)
        val until = now + delay
        retryNotBefore[key] = maxOf(retryNotBefore[key] ?: 0L, until)
        if (claimedAddress.equals(address, ignoreCase = true)) {
            claimedAddress = null
            claimedAt = 0L
        }
    }

    fun recordSuccess(address: String) = synchronized(lock) {
        val key = address.normalized()
        retryNotBefore.remove(key)
        failureCount.remove(key)
        lastFailureAt.remove(key)
    }

    fun backoffRemaining(address: String): Long = synchronized(lock) {
        val now = android.os.SystemClock.elapsedRealtime()
        val remaining = (retryNotBefore[address.normalized()] ?: 0L) - now
        if (remaining <= 0L) {
            retryNotBefore.remove(address.normalized())
            0L
        } else {
            remaining
        }
    }

    fun release(address: String? = null) = synchronized(lock) {
        if (address == null || claimedAddress.equals(address, ignoreCase = true)) {
            claimedAddress = null
            claimedAt = 0L
        }
    }

    private fun String.normalized(): String = trim().uppercase(java.util.Locale.US)
}
