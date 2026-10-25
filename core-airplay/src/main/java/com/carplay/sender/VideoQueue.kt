package com.carplay.sender

import java.util.ArrayDeque

data class EncodedVideo(
    val bytes: ByteArray,
    val ptsUs: Long,
    val keyFrame: Boolean,
    val queuedAtNanos: Long = System.nanoTime(),
    /**
     * Decoder epoch assigned by [VideoQueue].
     *
     * The default keeps the existing source-compatible constructor usable by
     * callers that create frames before handing them to the queue.
     */
    val epoch: Long = 0L,
)

/** Once dependent frames are dropped, resume only on a new independently decodable frame. */
class VideoQueue(private val capacity: Int = 8, private val byteLimit: Int = 8 * 1024 * 1024) {
    private val lock = Object()
    private val frames = ArrayDeque<EncodedVideo>()
    private var bytes = 0
    private var closed = false
    private var waitingForKeyFrame = true
    /** Incremented whenever the consumer must start at a fresh key frame. */
    private var epoch = 0L
    var dropped: Long = 0; private set
    init { require(capacity > 0 && byteLimit > 0) }
    fun offer(frame: EncodedVideo): Boolean = synchronized(lock) {
        if (closed) return false
        require(frame.bytes.size in 1..byteLimit)
        if (waitingForKeyFrame && !frame.keyFrame) { dropped++; return false }
        if (frames.size >= capacity || bytes + frame.bytes.size > byteLimit) {
            dropped += frames.size; frames.clear(); bytes = 0; waitingForKeyFrame = true
            if (!frame.keyFrame) { dropped++; return false }
        }
        waitingForKeyFrame = false
        frames.addLast(frame.copy(bytes = frame.bytes.copyOf(), epoch = epoch))
        bytes += frame.bytes.size
        lock.notifyAll()
        true
    }

    /**
     * Starts a new decoder epoch.
     *
     * A head-unit screen handoff normally destroys the decoder while the
     * producer keeps running. Any queued P-frame belongs to the old epoch and
     * cannot be decoded after the handoff, so retaining it would let the
     * sender put stale data in front of the rebuilt encoder's IDR.
     */
    fun resetForKeyFrame() = synchronized(lock) {
        dropped += frames.size.toLong()
        frames.clear()
        bytes = 0
        waitingForKeyFrame = true
        epoch = if (epoch == Long.MAX_VALUE) 0L else epoch + 1L
        lock.notifyAll()
    }

    /**
     * Returns whether a frame belongs to the current decoder epoch.
     *
     * A sender worker can be blocked in [take] while another thread resets the
     * queue for a screen handoff. The frame has already left [frames] at that
     * point, so clearing the queue alone cannot remove it. The epoch check lets
     * the worker discard that borrowed frame before writing it to the car.
     */
    fun isCurrent(frame: EncodedVideo): Boolean = synchronized(lock) {
        !closed && frame.epoch == epoch
    }

    /**
     * Runs [action] while the frame is still in the active decoder epoch.
     *
     * The queue lock is intentionally held for the duration of the action.
     * A reset therefore cannot land between the epoch check and a media write.
     * Keep callbacks out of [action]: they would run while a handoff is waiting
     * for this critical section to finish.
     */
    fun <T> withCurrent(frame: EncodedVideo, action: () -> T): T? = synchronized(lock) {
        if (closed || frame.epoch != epoch) null else action()
    }

    fun take(): EncodedVideo? = synchronized(lock) {
        while (frames.isEmpty() && !closed) lock.wait()
        if (frames.isEmpty()) null else frames.removeFirst().also { bytes -= it.bytes.size }
    }
    fun close() = synchronized(lock) { closed = true; frames.clear(); bytes = 0; lock.notifyAll() }
}
