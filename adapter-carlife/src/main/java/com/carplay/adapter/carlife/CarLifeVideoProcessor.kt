package com.carplay.adapter.carlife

import android.content.Context

/**
 * Optional video-only transform for CarLife's H.264 stream.
 *
 * Implementations must accept input without blocking CarLife's socket reader.
 * Audio, input, and CarLife session ownership stay in the adapter.
 */
interface CarLifeVideoProcessor {
    fun start(
        context: Context,
        outputWidth: Int,
        outputHeight: Int,
        outputFps: Int,
        onConfig: (ByteArray) -> Unit,
        onFrame: (ByteArray, Long, Boolean) -> Unit,
        onDiagnostic: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        onNeedKeyFrame: () -> Unit,
    )

    fun configureInput(width: Int, height: Int, fps: Int, avcC: ByteArray)

    fun offerFrame(accessUnit: ByteArray, ptsUs: Long, keyFrame: Boolean)

    /**
     * Asks the output encoder for an IDR without touching the source.
     *
     * The receiver asks for a key frame after every decoder rebuild and every
     * screen handover. The cheap answer is to make this encoder emit a sync
     * frame, which lands in the next frame or two. The expensive answer —
     * rebuilding CarLife's encoder — takes three seconds and drops the
     * picture, and the receiver has usually given up by then.
     *
     * Returns false when the encoder is not running yet, so the caller can
     * fall back to rebuilding the source.
     */
    fun requestOutputKeyFrame(): Boolean = false

    fun mapTouchToSource(panelX: Float, panelY: Float): Pair<Float, Float>?

    fun stop()
}
