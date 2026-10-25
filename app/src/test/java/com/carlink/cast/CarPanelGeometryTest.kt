package com.carlink.cast

import com.carplay.sender.SenderDisplay
import com.carplay.sender.SenderSafeArea
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CarPanelGeometryTest {
    @Test
    fun `buick panel uses measured outer canvas and safe rectangle`() {
        assertEquals(1972, CarPanelGeometry.PANEL_WIDTH)
        assertEquals(520, CarPanelGeometry.PANEL_HEIGHT)
        assertEquals(1408, CarPanelGeometry.SAFE_WIDTH)
        assertEquals(476, CarPanelGeometry.SAFE_HEIGHT)
        assertEquals(278, CarPanelGeometry.SAFE_X)
        assertEquals(44, CarPanelGeometry.SAFE_Y)
        assertTrue(CarPanelGeometry.SAFE_X + CarPanelGeometry.SAFE_WIDTH <= CarPanelGeometry.PANEL_WIDTH)
        assertEquals(
            CarPanelGeometry.PANEL_HEIGHT,
            CarPanelGeometry.SAFE_Y + CarPanelGeometry.SAFE_HEIGHT,
        )
    }

    @Test
    fun `safe point stays in video coordinates without panel offset`() {
        val point = CarPanelGeometry.mapSafePoint(1f, 1f)
        assertNotNull(point)
        assertEquals(1408f, point!!.first, 0f)
        assertEquals(476f, point.second, 0f)
    }

    @Test
    fun `CarLife touch maps across the full physical display`() {
        assertEquals(0f to 0f, CarPanelGeometry.mapDisplayTouchToPanel(0f, 0f))
        assertEquals(985.5f to 259.5f, CarPanelGeometry.mapDisplayTouchToPanel(0.5f, 0.5f))
        assertEquals(1971f to 519f, CarPanelGeometry.mapDisplayTouchToPanel(1f, 1f))
        assertNull(CarPanelGeometry.mapDisplayTouchToPanel(1.01f, 0.5f))
    }

    @Test
    fun `full panel input preserves every pixel of the measured outer canvas`() {
        val panel = CarPanelGeometry.panelFor(
            SenderDisplay(1972, 520, 30, "main-display", SenderSafeArea(278, 44, 1408, 476)),
        )
        assertEquals(
            CarPanelGeometry.VideoPlacement(
                x = 0,
                y = 0,
                width = 1972,
                height = 520,
                layout = CarPanelGeometry.VideoLayout.FULL_PANEL,
                sourceX = 0,
                sourceY = 0,
                sourceWidth = 1972,
                sourceHeight = 520,
            ),
            CarPanelGeometry.videoPlacement(1972, 520, panel),
        )
        assertEquals(
            listOf(
                0f, 0f,
                1f, 0f,
                0f, 1f,
                1f, 1f,
            ),
            CarPanelGeometry.videoTextureCoordinates(
                CarPanelGeometry.videoPlacement(1972, 520, panel),
                1972,
                520,
            ).toList(),
        )
    }

    @Test
    fun `safe area sized stream is placed at the measured card rectangle`() {
        assertEquals(
            CarPanelGeometry.VideoPlacement(
                x = 278,
                y = 44,
                width = 1408,
                height = 476,
                layout = CarPanelGeometry.VideoLayout.SAFE_AREA,
                sourceX = 0,
                sourceY = 0,
                sourceWidth = 1408,
                sourceHeight = 476,
            ),
            CarPanelGeometry.videoPlacement(1408, 476),
        )
        assertEquals(
            listOf(0f, 0f, 1f, 0f, 0f, 1f, 1f, 1f),
            CarPanelGeometry.videoTextureCoordinates(CarPanelGeometry.videoPlacement(1408, 476), 1408, 476).toList(),
        )
    }

    @Test
    fun `scaled full panel stream preserves its complete source`() {
        assertEquals(
            CarPanelGeometry.VideoLayout.FULL_PANEL,
            CarPanelGeometry.videoPlacement(3840, 1012).layout,
        )
        val placement = CarPanelGeometry.videoPlacement(3840, 1012)
        assertEquals(0, placement.sourceX)
        assertEquals(0, placement.sourceY)
        assertEquals(3840, placement.sourceWidth)
        assertEquals(1012, placement.sourceHeight)
        assertEquals(CarPanelGeometry.PANEL_WIDTH, placement.width)
        assertEquals(CarPanelGeometry.PANEL_HEIGHT, placement.height)
    }

    @Test
    fun `strict safe policy preserves a full panel source so side controls remain visible`() {
        val panel = CarPanelGeometry.panelFor(
            SenderDisplay(1972, 520, 30, "main-display", SenderSafeArea(278, 44, 1408, 476)),
        )
        val placement = CarPanelGeometry.videoPlacement(
            1972,
            520,
            panel,
            CarPanelGeometry.SafeAreaPolicy(enabled = true),
        )
        assertEquals(CarPanelGeometry.VideoLayout.FULL_PANEL, placement.layout)
        assertEquals(0, placement.x)
        assertEquals(0, placement.y)
        assertEquals(1972, placement.width)
        assertEquals(520, placement.height)
        assertEquals(0, placement.sourceX)
        assertEquals(0, placement.sourceY)
        assertEquals(1972, placement.sourceWidth)
        assertEquals(520, placement.sourceHeight)
    }

    @Test
    fun `relaxed safe policy also preserves an opaque full panel source`() {
        val panel = CarPanelGeometry.panelFor(
            SenderDisplay(1972, 520, 30, "main-display", SenderSafeArea(278, 44, 1408, 476)),
        )
        val placement = CarPanelGeometry.videoPlacement(
            1972,
            520,
            panel,
            CarPanelGeometry.SafeAreaPolicy(
                enabled = true,
                mode = CarPanelGeometry.SafeAreaMode.RELAXED,
            ),
        )
        assertEquals(CarPanelGeometry.VideoLayout.FULL_PANEL, placement.layout)
        assertEquals(0, placement.x)
        assertEquals(0, placement.y)
        assertEquals(1972, placement.width)
        assertEquals(520, placement.height)
        assertEquals(0, placement.sourceX)
        assertEquals(0, placement.sourceY)
        assertEquals(1972, placement.sourceWidth)
        assertEquals(520, placement.sourceHeight)
    }

    @Test
    fun `off policy preserves the full canvas without fitting`() {
        val panel = CarPanelGeometry.panelFor(
            SenderDisplay(1972, 520, 30, "main-display", SenderSafeArea(278, 44, 1408, 476)),
        )
        val placement = CarPanelGeometry.videoPlacement(
            1972,
            520,
            panel,
            CarPanelGeometry.SafeAreaPolicy(enabled = false),
        )
        assertEquals(CarPanelGeometry.VideoLayout.FULL_PANEL, placement.layout)
        assertEquals(1972, placement.width)
        assertEquals(520, placement.height)
    }

    @Test
    fun `receiver supplied safe area replaces fallback crop coordinates`() {
        val panel = CarPanelGeometry.panelFor(
            SenderDisplay(1972, 520, 30, "main-display", SenderSafeArea(278, 44, 1408, 476)),
        )
        val placement = CarPanelGeometry.videoPlacement(1972, 520, panel)
        assertEquals(CarPanelGeometry.VideoLayout.FULL_PANEL, placement.layout)
        assertEquals(0, placement.x)
        assertEquals(0, placement.y)
        assertEquals(0, placement.sourceX)
        assertEquals(0, placement.sourceY)
        assertEquals(1972, placement.sourceWidth)
        assertEquals(520, placement.sourceHeight)
        assertEquals(1972, placement.width)
        assertEquals(520, placement.height)
    }

    @Test
    fun `touch mapping inverts both full panel crop and safe sized input`() {
        val panel = CarPanelGeometry.panelFor(
            SenderDisplay(1972, 520, 30, "main-display", SenderSafeArea(278, 44, 1408, 476)),
        )
        val fullInput = CarPanelGeometry.videoPlacement(1972, 520, panel)
        assertEquals(
            278f to 44f,
            CarPanelGeometry.mapVideoTouchToSource(278f, 44f, fullInput, 1972, 520),
        )

        val safeInput = CarPanelGeometry.videoPlacement(1408, 476, panel)
        assertEquals(
            0f to 0f,
            CarPanelGeometry.mapVideoTouchToSource(278f, 44f, safeInput, 1408, 476),
        )
        assertNull(CarPanelGeometry.mapVideoTouchToSource(277f, 44f, safeInput, 1408, 476))
    }

    @Test
    fun `reported full display and safe area stay separate`() {
        val display = SenderDisplay(
            1972,
            520,
            30,
            "main-display",
            SenderSafeArea(278, 44, 1408, 476),
        )
        val panel = CarPanelGeometry.panelFor(display)

        assertEquals(1972, panel.panelWidth)
        assertEquals(520, panel.panelHeight)
        assertEquals(278, panel.safeX)
        assertEquals(44, panel.safeY)
        assertEquals(1408, panel.safeWidth)
        assertEquals(476, panel.safeHeight)
        assertEquals(
            CarPanelGeometry.VideoPlacement(
                x = 278,
                y = 44,
                width = 1408,
                height = 476,
                layout = CarPanelGeometry.VideoLayout.SAFE_AREA,
                sourceX = 0,
                sourceY = 0,
                sourceWidth = 1408,
                sourceHeight = 476,
            ),
            CarPanelGeometry.videoPlacement(1408, 476, panel),
        )
        assertEquals(CarPanelGeometry.VideoLayout.FULL_PANEL, CarPanelGeometry.videoPlacement(1972, 520, panel).layout)
    }

    @Test
    fun `missing receiver safe area uses only the reported display`() {
        val display = SenderDisplay(1408, 476, 30, "main-display")
        val panel = CarPanelGeometry.panelFor(display)
        assertEquals(1408, panel.panelWidth)
        assertEquals(476, panel.panelHeight)
        assertEquals(0, panel.safeX)
        assertEquals(0, panel.safeY)
        assertEquals(1408, panel.safeWidth)
        assertEquals(476, panel.safeHeight)
    }
}
