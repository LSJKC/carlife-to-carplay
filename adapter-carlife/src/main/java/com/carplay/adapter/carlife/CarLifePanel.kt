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
     * Maps a point inside the safe rectangle into CarLife's video.
     *
     * Used when the video source itself is the safe rectangle: the point is
     * shifted by the safe origin, scaled to the safe size, and clamped.
     */
    fun mapSafeTouchToSource(
        panelX: Float,
        panelY: Float,
        sourceWidth: Int = safeWidth,
        sourceHeight: Int = safeHeight,
    ): Pair<Float, Float>? {
        if (sourceWidth <= 0 || sourceHeight <= 0) return null
        if (panelX < safeX || panelX >= safeX + safeWidth) return null
        if (panelY < safeY || panelY >= safeY + safeHeight) return null
        return ((panelX - safeX) * sourceWidth / safeWidth).coerceIn(0f, sourceWidth - 1f) to
            ((panelY - safeY) * sourceHeight / safeHeight).coerceIn(0f, sourceHeight - 1f)
    }

    /**
     * Maps a point in a virtual display (AirPlay's canvas) onto this panel.
     *
     * The virtual display is whichever of the two shapes it matches better:
     * a display shaped like the safe view is laid inside the safe rectangle
     * (overlaid on a separately composited wallpaper), anything else covers
     * the full panel.
     */
    fun mapVirtualTouchToPanel(
        x: Float,
        y: Float,
        virtualWidth: Int,
        virtualHeight: Int,
    ): Pair<Float, Float>? {
        if (virtualWidth <= 0 || virtualHeight <= 0) return null
        if (x < 0f || y < 0f || x >= virtualWidth || y >= virtualHeight) return null
        val virtualAspect = virtualWidth / virtualHeight
        val safeAspect = safeWidth / safeHeight
        val fullAspect = panelWidth / panelHeight
        return if (Math.abs(virtualAspect - safeAspect) < Math.abs(virtualAspect - fullAspect)) {
            safeX + safeWidth * x / virtualWidth to safeY + safeHeight * y / virtualHeight
        } else {
            panelWidth * x / virtualWidth to panelHeight * y / virtualHeight
        }
    }

    /**
     * Maps a panel point into the CarLife source, whichever rectangle the
     * encoder was actually initialised with.
     */
    fun mapPanelTouchToSource(
        panelX: Float,
        panelY: Float,
        sourceWidth: Int,
        sourceHeight: Int,
    ): Pair<Float, Float>? =
        if (sourceWidth == safeWidth && sourceHeight == safeHeight) {
            mapSafeTouchToSource(panelX, panelY, sourceWidth, sourceHeight)
        } else {
            mapTouchToSource(panelX, panelY, sourceWidth, sourceHeight)
        }
}
