package com.carlink.cast

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WallpaperCropGeometryTest {
    @Test
    fun `square images cover the wide panel crop without invalid bounds`() {
        assertCoversPanel(sourceWidth = 1000, sourceHeight = 1000)
    }

    @Test
    fun `portrait images cover the wide panel crop without invalid bounds`() {
        assertCoversPanel(sourceWidth = 720, sourceHeight = 1600)
    }

    @Test
    fun `panoramic images cover the wide panel crop without invalid bounds`() {
        assertCoversPanel(sourceWidth = 4000, sourceHeight = 900)
    }

    private fun assertCoversPanel(sourceWidth: Int, sourceHeight: Int) {
        val viewportWidth = CarPanelGeometry.PANEL_WIDTH
        val viewportHeight = CarPanelGeometry.PANEL_HEIGHT
        val crop = WallpaperCropGeometry.cover(
            sourceWidth = sourceWidth,
            sourceHeight = sourceHeight,
            viewportWidth = viewportWidth,
            viewportHeight = viewportHeight,
        )

        assertTrue(sourceWidth * crop.scale >= viewportWidth)
        assertTrue(sourceHeight * crop.scale >= viewportHeight)
        assertTrue(crop.left <= 0f)
        assertTrue(crop.top <= 0f)
        assertEquals(
            crop,
            WallpaperCropGeometry.constrain(
                crop,
                sourceWidth,
                sourceHeight,
                viewportWidth,
                viewportHeight,
            ),
        )
    }

    @Test
    fun `crop preview uses the panel aspect ratio`() {
        assertEquals(
            CarPanelGeometry.PANEL_HEIGHT,
            WallpaperCropGeometry.viewportHeight(
                CarPanelGeometry.PANEL_WIDTH,
                CarPanelGeometry.PANEL_WIDTH,
                CarPanelGeometry.PANEL_HEIGHT,
            ),
        )
        assertEquals(
            264,
            WallpaperCropGeometry.viewportHeight(
                1000,
                CarPanelGeometry.PANEL_WIDTH,
                CarPanelGeometry.PANEL_HEIGHT,
            ),
        )
    }

    @Test
    fun `wallpaper aspect fill maps full image width and only crops vertically`() {
        val uv = WallpaperTextureGeometry.aspectFillUv(1256, 345, 1972, 520)
        assertEquals(0f, uv[0], 0f)
        assertEquals(1f, uv[1], 0f)
        assertTrue(uv[2] > 0f)
        assertTrue(uv[3] < 1f)
        assertEquals(1f, uv[2] + uv[3], 0.0001f)
        assertEquals(
            listOf(0f, uv[3], 1f, uv[3], 0f, uv[2], 1f, uv[2]),
            WallpaperTextureGeometry.quadCoordinates(uv).toList(),
        )
    }

    @Test
    fun `wide wallpaper aspect fill crops the horizontal edges`() {
        val uv = WallpaperTextureGeometry.aspectFillUv(4000, 900, 1972, 520)
        assertTrue(uv[0] > 0f)
        assertTrue(uv[1] < 1f)
        assertEquals(1f, uv[0] + uv[1], 0.0001f)
        assertEquals(0f, uv[2], 0f)
        assertEquals(1f, uv[3], 0f)
    }
}
