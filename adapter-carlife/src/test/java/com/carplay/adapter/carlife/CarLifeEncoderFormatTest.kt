package com.carplay.adapter.carlife

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class CarLifeEncoderFormatTest {
    @Test
    fun `returns safe-area metadata without overriding CarLife codec dimensions`() {
        val format = CarLifeEncoderFormat.fromJson(
            """{"width":1972,"height":520,"save_area_width":1408,"save_area_height":476,"save_area_x":278,"save_area_y":44,"need_adapter_screen":1,"draw_ui_outside_safe_area":0,"real_width":1972,"real_height":520}""",
        )

        assertEquals(1972, format?.panelWidth)
        assertEquals(520, format?.panelHeight)
        assertEquals(
            mapOf(
                "save_area_width" to 1408,
                "save_area_height" to 476,
                "save_area_x" to 278,
                "save_area_y" to 44,
                "need_adapter_screen" to 1,
                "draw_ui_outside_safe_area" to 0,
            ),
            format?.mediaFormatOverrides(),
        )
        assertEquals(null, format?.mediaFormatOverrides()?.get("width"))
        assertEquals(null, format?.mediaFormatOverrides()?.get("height"))
    }

    @Test
    fun `preserves safe geometry and explicitly disables unrequested flags`() {
        val format = CarLifeEncoderFormat.fromJson(
            """{"width":1972,"height":520,"save_area_width":1408,"save_area_height":476,"save_area_x":278,"save_area_y":44}""",
        )

        assertEquals(
            mapOf(
                "save_area_width" to 1408,
                "save_area_height" to 476,
                "save_area_x" to 278,
                "save_area_y" to 44,
                "need_adapter_screen" to 0,
                "draw_ui_outside_safe_area" to 0,
            ),
            format?.mediaFormatOverrides(),
        )
    }

    @Test
    fun `rejects missing or out of panel geometry`() {
        assertNull(CarLifeEncoderFormat.fromJson(null))
        assertNull(CarLifeEncoderFormat.fromJson("not-json"))
        assertNull(
            CarLifeEncoderFormat.fromJson(
                """{"width":1972,"height":520,"save_area_width":1408,"save_area_height":476,"save_area_x":565,"save_area_y":44}""",
            ),
        )
        assertNull(
            CarLifeEncoderFormat.fromJson(
                """{"width":1972,"height":520,"save_area_width":0,"save_area_height":476,"save_area_x":0,"save_area_y":0}""",
            ),
        )
    }
}
