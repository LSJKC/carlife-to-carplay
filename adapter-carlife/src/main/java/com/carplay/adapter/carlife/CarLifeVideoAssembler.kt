package com.carplay.adapter.carlife

internal data class CarLifeVideoFrame(
    val data: ByteArray,
    val timestampSeconds: Long,
    val keyFrame: Boolean,
)

internal data class CarLifeVideoBatch(
    val avcC: ByteArray?,
    val frames: List<CarLifeVideoFrame>,
    /** True when the last valid parameter sets were reused after a rebuild. */
    val reusedAvcC: Boolean = false,
)

/**
 * Collects the first SPS/PPS pair and holds an IDR that arrives before it.
 *
 * CarLife may split encoder configuration and its first picture across stream
 * packets. The sender cannot accept the IDR until its avcC record is installed,
 * but dropping that IDR can leave a GOP=0 encoder with no later recovery frame.
 */
internal class CarLifeVideoAssembler {
    private val configCollector = CarLifeAvcConfigCollector()
    private var configSent = false
    /** Last complete config remains valid across a CarLife encoder rebuild. */
    private var lastAvcC: ByteArray? = null
    private val pendingFrames = mutableListOf<CarLifeVideoFrame>()
    private var pendingOverflowed = false
    private var awaitingKeyFrame = false

    @Synchronized
    fun reset(preserveConfig: Boolean = false) {
        configCollector.reset()
        if (!preserveConfig) lastAvcC = null
        configSent = false
        pendingFrames.clear()
        pendingOverflowed = false
        awaitingKeyFrame = false
    }

    @Synchronized
    fun offer(data: ByteArray, timestampSeconds: Long, keyFrame: Boolean): CarLifeVideoBatch? {
        val picture = CarLifeProto.pictureAccessUnit(data)
        val frame = picture?.let { CarLifeVideoFrame(it, timestampSeconds, keyFrame) }
        if (configSent) {
            if (awaitingKeyFrame && !keyFrame) return null
            if (keyFrame) awaitingKeyFrame = false
            return frame?.let { CarLifeVideoBatch(null, listOf(it)) }
        }

        val collectedAvcC = configCollector.offer(data)
        if (collectedAvcC != null) lastAvcC = collectedAvcC.copyOf()
        // Some CarLife builds recreate MediaCodec and emit an IDR without
        // repeating SPS/PPS. The previous avcC is still valid for the same
        // codec dimensions, and re-emitting it lets the AirPlay decoder start
        // the new epoch instead of waiting forever for a CONFIG that will
        // never arrive.
        val reusedAvcC = collectedAvcC == null && keyFrame && lastAvcC != null
        val avcC = collectedAvcC ?: if (reusedAvcC) lastAvcC else null
        if (keyFrame) {
            // A new IDR supersedes an older recovery attempt. Keep the IDR
            // itself and any dependent access units that follow it until the
            // split SPS/PPS arrives.
            pendingFrames.clear()
            pendingOverflowed = false
        }
        if (frame != null && !pendingOverflowed && (keyFrame || pendingFrames.isNotEmpty())) {
            if (pendingFrames.size >= MAX_PENDING_FRAMES) {
                // Skipping a dependent picture can invalidate every later P
                // frame. Drop the incomplete GOP and wait for a fresh IDR.
                pendingFrames.clear()
                pendingOverflowed = true
            } else {
                pendingFrames.add(frame)
            }
        }
        if (avcC == null) return null

        configSent = true
        if (pendingOverflowed) {
            pendingOverflowed = false
            awaitingKeyFrame = true
            return CarLifeVideoBatch(avcC, emptyList())
        }
        val frames = pendingFrames.toList()
        pendingFrames.clear()
        if (frames.none { it.keyFrame }) awaitingKeyFrame = true
        return CarLifeVideoBatch(avcC, frames, reusedAvcC)
    }

    private companion object {
        /** Bounds memory if CarLife never sends the second parameter set. */
        const val MAX_PENDING_FRAMES = 32
    }
}
