package com.carplay.adapter.carlife

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CarLifePanelTest {
    private val panel = CarLifePanel(
        panelWidth = 1972,
        panelHeight = 520,
        safeX = 278,
        safeY = 44,
        safeWidth = 1408,
        safeHeight = 476,
    )

    @Test
    fun safeTouchUsesZeroOriginSourceCoordinates() {
        assertEquals(0f to 0f, panel.mapSafeTouchToSource(278f, 44f))
        assertEquals(1407f to 475f, panel.mapSafeTouchToSource(1685f, 519f))
        assertEquals(704f to 238f, panel.mapSafeTouchToSource(982f, 282f))
    }

    @Test
    fun touchOutsideSafeAreaIsIgnored() {
        assertNull(panel.mapSafeTouchToSource(277f, 44f))
        assertNull(panel.mapSafeTouchToSource(278f, 43f))
        assertNull(panel.mapSafeTouchToSource(1686f, 519f))
    }
}
