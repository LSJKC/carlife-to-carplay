package com.carlink.cast

import com.carplay.adapter.carlife.CarLifePanel
import com.carplay.sender.SenderDisplay

/**
 * Fallback geometry from the captured Buick CarLife session.
 *
 * A valid AirPlay /info display now takes precedence. Its width and height are
 * the stream canvas; nested viewAreas.safeArea is only the UI/content rectangle.
 */
object CarPanelGeometry {
    const val PANEL_WIDTH = 1972
    const val PANEL_HEIGHT = 520
    const val SAFE_WIDTH = 1408
    const val SAFE_HEIGHT = 476
    const val SAFE_X = 278
    const val SAFE_Y = 44

    enum class SafeAreaMode {
        STRICT,
        RELAXED,
        CUSTOM,
        OFF,
    }

    data class SafeAreaPolicy(
        val enabled: Boolean = false,
        val mode: SafeAreaMode = SafeAreaMode.STRICT,
        val left: Int = 0,
        val top: Int = 0,
        val right: Int = 0,
        val bottom: Int = 0,
    )

    enum class VideoLayout {
        /** CarLife delivered a frame for the physical panel; preserve every pixel. */
        FULL_PANEL,
        /** CarLife delivered only the safe-area rectangle; place it on the panel. */
        SAFE_AREA,
        /** Preserve the complete source frame, but fit it into the selected viewport. */
        SAFE_VIEWPORT,
    }

    data class VideoPlacement(
        val x: Int,
        val y: Int,
        val width: Int,
        val height: Int,
        val layout: VideoLayout,
        val sourceX: Int,
        val sourceY: Int,
        val sourceWidth: Int,
        val sourceHeight: Int,
    )

    private fun fallbackPanel() = CarLifePanel(
        panelWidth = PANEL_WIDTH,
        panelHeight = PANEL_HEIGHT,
        safeX = SAFE_X,
        safeY = SAFE_Y,
        safeWidth = SAFE_WIDTH,
        safeHeight = SAFE_HEIGHT,
    )

    fun panelFor(display: SenderDisplay): CarLifePanel {
        val safe = display.safeArea
        return if (safe == null) {
            CarLifePanel(
                panelWidth = display.width,
                panelHeight = display.height,
                safeX = 0,
                safeY = 0,
                safeWidth = display.width,
                safeHeight = display.height,
            )
        } else {
            CarLifePanel(
                panelWidth = display.width,
                panelHeight = display.height,
                safeX = safe.x,
                safeY = safe.y,
                safeWidth = safe.width,
                safeHeight = safe.height,
            )
        }
    }

    /** HID touch samples are normalized to the physical display, not its content rectangle. */
    fun mapDisplayTouchToPanel(
        normalizedX: Float,
        normalizedY: Float,
        panel: CarLifePanel,
    ): Pair<Float, Float>? {
        if (normalizedX !in 0f..1f || normalizedY !in 0f..1f) return null
        return normalizedX * (panel.panelWidth - 1) to
            normalizedY * (panel.panelHeight - 1)
    }

    fun mapDisplayTouchToPanel(normalizedX: Float, normalizedY: Float): Pair<Float, Float>? =
        mapDisplayTouchToPanel(normalizedX, normalizedY, fallbackPanel())

    /** Maps a normalized point into the content rectangle's local coordinates. */
    fun mapSafePoint(normalizedX: Float, normalizedY: Float): Pair<Float, Float>? {
        if (normalizedX !in 0f..1f || normalizedY !in 0f..1f) return null
        return normalizedX * SAFE_WIDTH to normalizedY * SAFE_HEIGHT
    }

    /**
     * Chooses a single coordinate contract for the compositor.
     *
     * A frame whose source is the physical panel must be preserved in full.
     * Cropping a `1972x520` frame to `1408x476` silently removes CarLife's
     * side rail, bottom bar, and any other UI outside the safe rectangle.
     * Only a source that is actually encoded at the safe-area size may be
     * placed at the safe-area origin.
     */
    fun videoPlacement(
        sourceWidth: Int,
        sourceHeight: Int,
        panel: CarLifePanel,
        policy: SafeAreaPolicy = SafeAreaPolicy(),
    ): VideoPlacement {
        require(sourceWidth > 0 && sourceHeight > 0)
        val exactPanel = sourceWidth == panel.panelWidth && sourceHeight == panel.panelHeight
        val exactSafe = sourceWidth == panel.safeWidth && sourceHeight == panel.safeHeight
        val fullPanelAspect = aspectError(sourceWidth, sourceHeight, panel.panelWidth, panel.panelHeight)
        val looksLikeFullPanel = exactPanel || fullPanelAspect <= FULL_PANEL_ASPECT_TOLERANCE
        val sourceIsSafeArea = exactSafe && !looksLikeFullPanel
        // A CarLife source encoded at the physical panel size already
        // contains the complete UI (side rail/bottom bar included). E5 can
        // resize its virtual display before encoding, but CarLife cannot be
        // resized after the fact; fitting a full-panel frame into safeArea
        // therefore shrinks and clips the very UI the user needs. Apply a
        // safe viewport only when the source itself is the zero-origin safe
        // canvas.
        // A full-panel CarLife frame is opaque H.264, not an alpha layer.
        // Cropping it into safeArea deletes the side rail and bottom controls.
        // Safe viewport placement is reserved for a source that is actually
        // encoded as the zero-origin safe rectangle.
        val viewport = if (sourceIsSafeArea) resolveViewport(panel, policy) else null
        if (viewport != null) {
            return VideoPlacement(
                x = viewport.first,
                y = viewport.second,
                width = viewport.third,
                height = viewport.fourth,
                layout = if (sourceIsSafeArea && viewport ==
                    Quad(panel.safeX, panel.safeY, panel.safeWidth, panel.safeHeight)
                ) {
                    VideoLayout.SAFE_AREA
                } else {
                    VideoLayout.SAFE_VIEWPORT
                },
                sourceX = 0,
                sourceY = 0,
                sourceWidth = sourceWidth,
                sourceHeight = sourceHeight,
            )
        }
        return VideoPlacement(
            x = if (sourceIsSafeArea) panel.safeX else 0,
            y = if (sourceIsSafeArea) panel.safeY else 0,
            width = if (sourceIsSafeArea) panel.safeWidth else panel.panelWidth,
            height = if (sourceIsSafeArea) panel.safeHeight else panel.panelHeight,
            layout = if (sourceIsSafeArea) VideoLayout.SAFE_AREA else VideoLayout.FULL_PANEL,
            sourceX = 0,
            sourceY = 0,
            sourceWidth = sourceWidth,
            sourceHeight = sourceHeight,
        )
    }

    fun videoPlacement(sourceWidth: Int, sourceHeight: Int): VideoPlacement =
        videoPlacement(sourceWidth, sourceHeight, fallbackPanel())

    private fun resolveViewport(
        panel: CarLifePanel,
        policy: SafeAreaPolicy,
    ): Quad? {
        if (!policy.enabled || policy.mode == SafeAreaMode.OFF) return null
        val margins = when (policy.mode) {
            SafeAreaMode.STRICT -> Quad(panel.safeX, panel.safeY, panel.panelWidth - panel.safeX - panel.safeWidth,
                panel.panelHeight - panel.safeY - panel.safeHeight)
            SafeAreaMode.RELAXED -> Quad(
                panel.safeX / 2,
                panel.safeY / 2,
                (panel.panelWidth - panel.safeX - panel.safeWidth) / 2,
                (panel.panelHeight - panel.safeY - panel.safeHeight) / 2,
            )
            SafeAreaMode.CUSTOM -> Quad(
                policy.left.coerceAtLeast(0),
                policy.top.coerceAtLeast(0),
                policy.right.coerceAtLeast(0),
                policy.bottom.coerceAtLeast(0),
            )
            SafeAreaMode.OFF -> return null
        }
        val x = margins.first
        val y = margins.second
        val width = panel.panelWidth - margins.first - margins.third
        val height = panel.panelHeight - margins.second - margins.fourth
        if (width < MIN_VIEWPORT_WIDTH || height < MIN_VIEWPORT_HEIGHT) return null
        if (x < 0 || y < 0 || x + width > panel.panelWidth || y + height > panel.panelHeight) return null
        if (x == 0 && y == 0 && width == panel.panelWidth && height == panel.panelHeight) return null
        return Quad(x, y, width, height)
    }

    private data class Quad(
        val first: Int,
        val second: Int,
        val third: Int,
        val fourth: Int,
    )

    fun videoTextureCoordinates(
        placement: VideoPlacement,
        sourceWidth: Int,
        sourceHeight: Int,
    ): FloatArray {
        val u0 = placement.sourceX.toFloat() / sourceWidth
        val v0 = placement.sourceY.toFloat() / sourceHeight
        val u1 = (placement.sourceX + placement.sourceWidth).toFloat() / sourceWidth
        val v1 = (placement.sourceY + placement.sourceHeight).toFloat() / sourceHeight
        return floatArrayOf(u0, v0, u1, v0, u0, v1, u1, v1)
    }

    fun mapVideoTouchToSource(
        panelX: Float,
        panelY: Float,
        placement: VideoPlacement,
        sourceWidth: Int,
        sourceHeight: Int,
    ): Pair<Float, Float>? {
        if (sourceWidth <= 0 || sourceHeight <= 0 ||
            panelX < placement.x || panelY < placement.y ||
            panelX >= placement.x + placement.width ||
            panelY >= placement.y + placement.height
        ) return null
        val x = placement.sourceX +
            (panelX - placement.x) * placement.sourceWidth / placement.width
        val y = placement.sourceY +
            (panelY - placement.y) * placement.sourceHeight / placement.height
        return x.coerceIn(0f, (sourceWidth - 1).toFloat()) to
            y.coerceIn(0f, (sourceHeight - 1).toFloat())
    }

    private fun aspectError(sourceWidth: Int, sourceHeight: Int, targetWidth: Int, targetHeight: Int): Double =
        kotlin.math.abs(
            sourceWidth.toDouble() * targetHeight - targetWidth.toDouble() * sourceHeight,
        ) / (targetWidth.toDouble() * sourceHeight)

    private const val FULL_PANEL_ASPECT_TOLERANCE = 0.01
    private const val MIN_VIEWPORT_WIDTH = 320
    private const val MIN_VIEWPORT_HEIGHT = 160
}
