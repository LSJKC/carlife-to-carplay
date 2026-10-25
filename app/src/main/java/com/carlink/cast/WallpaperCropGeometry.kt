package com.carlink.cast

import kotlin.math.roundToInt

internal data class WallpaperCropTransform(
    val scale: Float,
    val left: Float,
    val top: Float,
)

internal object WallpaperCropGeometry {
    fun viewportHeight(viewportWidth: Int, outputWidth: Int, outputHeight: Int): Int {
        require(viewportWidth > 0 && outputWidth > 0 && outputHeight > 0)
        return (viewportWidth.toDouble() * outputHeight / outputWidth).roundToInt().coerceAtLeast(1)
    }

    fun cover(
        sourceWidth: Int,
        sourceHeight: Int,
        viewportWidth: Int,
        viewportHeight: Int,
    ): WallpaperCropTransform {
        require(sourceWidth > 0 && sourceHeight > 0 && viewportWidth > 0 && viewportHeight > 0)
        val scale = maxOf(
            viewportWidth.toFloat() / sourceWidth,
            viewportHeight.toFloat() / sourceHeight,
        )
        return constrain(
            WallpaperCropTransform(
                scale = scale,
                left = (viewportWidth - sourceWidth * scale) / 2f,
                top = (viewportHeight - sourceHeight * scale) / 2f,
            ),
            sourceWidth,
            sourceHeight,
            viewportWidth,
            viewportHeight,
        )
    }

    fun zoomAround(
        current: WallpaperCropTransform,
        factor: Float,
        focusX: Float,
        focusY: Float,
        sourceWidth: Int,
        sourceHeight: Int,
        viewportWidth: Int,
        viewportHeight: Int,
        minimumScale: Float,
    ): WallpaperCropTransform {
        val scale = (current.scale * factor).coerceIn(minimumScale, minimumScale * 8f)
        val ratio = scale / current.scale
        return constrain(
            WallpaperCropTransform(
                scale = scale,
                left = focusX - (focusX - current.left) * ratio,
                top = focusY - (focusY - current.top) * ratio,
            ),
            sourceWidth,
            sourceHeight,
            viewportWidth,
            viewportHeight,
        )
    }

    fun constrain(
        transform: WallpaperCropTransform,
        sourceWidth: Int,
        sourceHeight: Int,
        viewportWidth: Int,
        viewportHeight: Int,
    ): WallpaperCropTransform {
        val scaledWidth = sourceWidth * transform.scale
        val scaledHeight = sourceHeight * transform.scale
        return transform.copy(
            left = transform.left.coerceIn(viewportWidth - scaledWidth, 0f),
            top = transform.top.coerceIn(viewportHeight - scaledHeight, 0f),
        )
    }
}
