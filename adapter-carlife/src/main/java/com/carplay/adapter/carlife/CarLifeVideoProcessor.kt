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

    fun mapTouchToSource(panelX: Float, panelY: Float): Pair<Float, Float>?

    fun stop()
}
