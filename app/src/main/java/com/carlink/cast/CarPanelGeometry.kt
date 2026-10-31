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
        /** Extra blank columns inside the usable area, as a percent of panel width. */
        val extraLeftPercent: Int = 0,
        val extraRightPercent: Int = 0,
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
        /*
         * The safe-area policy always applies, whatever size CarLife encodes.
         *
         * CarLife ignores save_area_* when drawing: it always fills the canvas
         * it was told to encode, so a "safe area" build still arrives as
         * 1972x520. Two facts force the picture to be placed rather than
         * passed through:
         *
         *  - The car checks the encoder's reported size against the canvas it
         *    declared and answers SETUP with 400 when they differ, so the
         *    source can never be shrunk to the safe rectangle.
         *  - The driver still asked for CarLife inside the safe rectangle with
         *    wallpaper around it.
         *
         * SAFE_VIEWPORT exists for exactly this: keep the whole source frame
         * (no cropping, so no control is lost) and scale it into the safe
         * rectangle, leaving the wallpaper visible outside. STRICT/RELAXED/
         * CUSTOM choose that rectangle; OFF and a rectangle equal to the panel
         * leave the source full-screen.
         */
        val viewport = resolveViewport(panel, policy)
        if (viewport != null) {
            /*
             * Fill the safe rectangle exactly.
             *
             * CarLife is told to encode at the safe-area size (1408x476), so
             * the source already has the rectangle's shape and this is a
             * straight 1:1 placement. A source that arrives at the panel size
             * anyway (CarLife has been seen to ignore the requested size) is
             * fitted by the smaller ratio so nothing is cropped or stretched.
             */
            val fitted = if (sourceWidth == viewport.third && sourceHeight == viewport.fourth) {
                Quad(viewport.first, viewport.second, viewport.third, viewport.fourth)
            } else {
                fitInside(
                    outerX = viewport.first,
                    outerY = viewport.second,
                    outerWidth = viewport.third,
                    outerHeight = viewport.fourth,
                    sourceWidth = sourceWidth,
                    sourceHeight = sourceHeight,
                )
            }
            return VideoPlacement(
                x = fitted.first,
                y = fitted.second,
                width = fitted.third,
                height = fitted.fourth,
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
        /*
         * Extra blank columns the driver asked for, on top of whatever the
         * car's own safe area already reserves.
         *
         * The background is painted across the whole panel regardless; this
         * only pulls the phone's picture and its touch mapping inwards, which
         * is what "左右安全留边" means on the reference UI. Capped at a third of
         * the panel so a large percentage cannot collapse the viewport.
         */
        val extraLeft = (policy.extraLeftPercent.coerceIn(0, 20) * panel.panelWidth) / 100
        val extraRight = (policy.extraRightPercent.coerceIn(0, 20) * panel.panelWidth) / 100
        val cap = panel.panelWidth / 3
        val leftMargin = margins.first + extraLeft.coerceAtMost(cap)
        val rightMargin = margins.third + extraRight.coerceAtMost(cap)

        val x = leftMargin
        val y = margins.second
        val width = panel.panelWidth - leftMargin - rightMargin
        val height = panel.panelHeight - margins.second - margins.fourth
        if (width < MIN_VIEWPORT_WIDTH || height < MIN_VIEWPORT_HEIGHT) return null
        if (x < 0 || y < 0 || x + width > panel.panelWidth || y + height > panel.panelHeight) return null
        if (x == 0 && y == 0 && width == panel.panelWidth && height == panel.panelHeight) return null
        return Quad(x, y, width, height)
    }

    /**
     * Largest rectangle with the source's aspect ratio that covers the outer
     * rectangle, centred on it, clamped so it never leaves the panel.
     *
     * Unlike a plain "cover" this does not crop the source: the rectangle is
     * allowed to be bigger than the outer one, and the panel bounds decide how
     * far that is allowed to go.
     */
    /**
     * Scales the whole source frame into [outerWidth] x [outerHeight],
     * preserving its aspect ratio, and centres it.
     *
     * Uses the smaller of the two ratios, so the result always fits: nothing
     * is cropped, nothing is stretched, and the leftover space is wallpaper.
     */
    private fun fitInside(
        outerX: Int,
        outerY: Int,
        outerWidth: Int,
        outerHeight: Int,
        sourceWidth: Int,
        sourceHeight: Int,
    ): Quad {
        if (outerWidth <= 0 || outerHeight <= 0 || sourceWidth <= 0 || sourceHeight <= 0) {
            return Quad(outerX, outerY, outerWidth, outerHeight)
        }
        val scale = minOf(
            outerWidth.toDouble() / sourceWidth,
            outerHeight.toDouble() / sourceHeight,
        )
        val width = (sourceWidth * scale).toInt().coerceIn(1, outerWidth)
        val height = (sourceHeight * scale).toInt().coerceIn(1, outerHeight)
        val x = outerX + (outerWidth - width) / 2
        val y = outerY + (outerHeight - height) / 2
        return Quad(x, y, width, height)
    }

    private fun coverInside(
        outerX: Int,
        outerY: Int,
        outerWidth: Int,
        outerHeight: Int,
        sourceWidth: Int,
        sourceHeight: Int,
        panelWidth: Int,
        panelHeight: Int,
    ): Quad {
        if (outerWidth <= 0 || outerHeight <= 0 || sourceWidth <= 0 || sourceHeight <= 0) {
            return Quad(outerX, outerY, outerWidth, outerHeight)
        }
        val scale = maxOf(
            outerWidth.toDouble() / sourceWidth,
            outerHeight.toDouble() / sourceHeight,
        )
        var width = (sourceWidth * scale).toInt().coerceAtLeast(1)
        var height = (sourceHeight * scale).toInt().coerceAtLeast(1)
        // Never grow past the panel; a panel-sized frame is already the limit.
        if (width > panelWidth) {
            width = panelWidth
            height = (sourceHeight.toDouble() * panelWidth / sourceWidth).toInt().coerceAtLeast(1)
        }
        if (height > panelHeight) {
            height = panelHeight
            width = (sourceWidth.toDouble() * panelHeight / sourceHeight).toInt().coerceAtLeast(1)
        }
        val x = (outerX + (outerWidth - width) / 2).coerceIn(0, (panelWidth - width).coerceAtLeast(0))
        val y = (outerY + (outerHeight - height) / 2).coerceIn(0, (panelHeight - height).coerceAtLeast(0))
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
