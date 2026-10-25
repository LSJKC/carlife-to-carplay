package com.carplay.adapter.carlife

/**
 * The panel CarLife may draw on, plus the rectangle its cards belong in.
 *
 * `VideoMsgHandler.initMediaCodec` copies these fields into `Recorder`.
 * `MediaFormatUtils.setVendorEncoderParams` then sends them to the host through
 * `carlife_encode_format`; the host must return the safe-area vendor keys for
 * them to be applied to `MediaFormat`. The reply must not change coded size.
 *
 * The outer panel describes the physical canvas and the safe area marks where
 * cards live. CarLink composites wallpaper separately, so CarLife controls stay
 * constrained to that safe rectangle.
 */
data class CarLifePanel(
    /** Whole physical surface described to CarLife, in pixels. */
    val panelWidth: Int,
    val panelHeight: Int,
    /** Where cards go, inside [panelWidth] x [panelHeight]. */
    val safeX: Int,
    val safeY: Int,
    val safeWidth: Int,
    val safeHeight: Int,
    /**
     * Allow CarLife controls to extend beyond the safe area.
     *
     * CarLife emits an opaque H.264 frame rather than an alpha virtual
     * display. Restricting a full 1972x520 frame to 1408x476 removes the side
     * rail and bottom controls, so the real-car default keeps the full UI.
     */
    val drawUiOutsideSafeArea: Boolean = true,
    /** Let CarLife size its layout itself rather than reusing a fixed one. */
    val needAdapterScreen: Boolean = true,
) {
    init {
        require(panelWidth > 0 && panelHeight > 0) { "面板尺寸必须为正" }
        require(safeWidth > 0 && safeHeight > 0) { "安全区尺寸必须为正" }
        require(safeX >= 0 && safeY >= 0) { "安全区原点不能为负" }
        require(safeX + safeWidth <= panelWidth) { "安全区超出面板宽度" }
        require(safeY + safeHeight <= panelHeight) { "安全区超出面板高度" }
    }

    /**
     * Maps a point scaled into the AirPlay panel back into CarLife's video.
     *
     * Buick's HID extent has the same aspect as the safe view, not the outer
     * canvas. Treating those normalized coordinates as panel coordinates and
     * subtracting safeX shifts the right side left by the panel inset.
     */
    fun mapTouchToSource(
        panelX: Float,
        panelY: Float,
        sourceWidth: Int,
        sourceHeight: Int,
    ): Pair<Float, Float>? {
        if (sourceWidth <= 0 || sourceHeight <= 0) return null
        if (panelX < 0f || panelX >= panelWidth || panelY < 0f || panelY >= panelHeight) return null
        return panelX * sourceWidth / panelWidth to panelY * sourceHeight / panelHeight
    }

    /**
     * Maps physical-panel coordinates into the zero-origin CarLife safe source.
     *
     * Binder touch callbacks can still be reported in outer virtual-display
     * coordinates even though the encoder now contains only the safe rectangle.
     */
    fun mapSafeTouchToSource(
        panelX: Float,
        panelY: Float,
        sourceWidth: Int = safeWidth,
        sourceHeight: Int = safeHeight,
    ): Pair<Float, Float>? {
        if (sourceWidth <= 0 || sourceHeight <= 0) return null
        if (panelX < safeX || panelX >= safeX + safeWidth ||
            panelY < safeY || panelY >= safeY + safeHeight
        ) return null
        return (
            (panelX - safeX) * sourceWidth / safeWidth
            ).coerceIn(0f, (sourceWidth - 1).toFloat()) to
            (
                (panelY - safeY) * sourceHeight / safeHeight
                ).coerceIn(0f, (sourceHeight - 1).toFloat())
    }

    /**
     * CarLife's Binder callback uses the size from prepareCast, which is
     * either the safe virtual display or the physical full panel depending on
     * the vendor build. Do not assume it is always the outer panel: scaling a
     * safe-sized callback into 1972x520 shifts the right half outside the
     * 1408x476 content rectangle.
     */
    fun mapVirtualTouchToPanel(
        x: Float,
        y: Float,
        virtualWidth: Int,
        virtualHeight: Int,
    ): Pair<Float, Float>? {
        if (virtualWidth <= 0 || virtualHeight <= 0 ||
            x < 0f || y < 0f || x >= virtualWidth || y >= virtualHeight
        ) return null
        val virtualAspect = virtualWidth.toFloat() / virtualHeight
        val safeAspect = safeWidth.toFloat() / safeHeight
        val fullAspect = panelWidth.toFloat() / panelHeight
        return if (kotlin.math.abs(virtualAspect - safeAspect) <
            kotlin.math.abs(virtualAspect - fullAspect)
        ) {
            safeX + x * safeWidth / virtualWidth to
                safeY + y * safeHeight / virtualHeight
        } else {
            x * panelWidth / virtualWidth to y * panelHeight / virtualHeight
        }
    }

    /** Converts physical-panel coordinates to whichever source canvas CarLife encoded. */
    fun mapPanelTouchToSource(
        panelX: Float,
        panelY: Float,
        sourceWidth: Int,
        sourceHeight: Int,
    ): Pair<Float, Float>? {
        if (sourceWidth == safeWidth && sourceHeight == safeHeight) {
            return mapSafeTouchToSource(panelX, panelY, sourceWidth, sourceHeight)
        }
        return mapTouchToSource(panelX, panelY, sourceWidth, sourceHeight)
    }
}
