package com.carlink.cast

internal object WallpaperTextureGeometry {
    /** Returns [u0, u1, v0, v1] for a centered aspect-fill crop. */
    fun aspectFillUv(sourceWidth: Int, sourceHeight: Int, targetWidth: Int, targetHeight: Int): FloatArray {
        require(sourceWidth > 0 && sourceHeight > 0 && targetWidth > 0 && targetHeight > 0)
        val sourceAspect = sourceWidth.toFloat() / sourceHeight
        val targetAspect = targetWidth.toFloat() / targetHeight
        return if (sourceAspect > targetAspect) {
            val fraction = targetAspect / sourceAspect
            val inset = (1f - fraction) / 2f
            floatArrayOf(inset, 1f - inset, 0f, 1f)
        } else {
            val fraction = sourceAspect / targetAspect
            val inset = (1f - fraction) / 2f
            floatArrayOf(0f, 1f, inset, 1f - inset)
        }
    }

    /** Maps Android bitmap rows into GL's bottom-left-origin quad order. */
    fun quadCoordinates(uv: FloatArray): FloatArray {
        require(uv.size == 4)
        val (u0, u1, v0, v1) = uv
        return floatArrayOf(
            u0, v1,
            u1, v1,
            u0, v0,
            u1, v0,
        )
    }
}
