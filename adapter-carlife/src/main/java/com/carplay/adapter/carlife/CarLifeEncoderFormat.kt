package com.carplay.adapter.carlife

import org.json.JSONObject

/**
 * Validated layout fields for CarLife's `carlife_encode_format` host callback.
 *
 * CarLife 8.8.5 applies returned entries to MediaFormat. The host must only
 * return the vendor geometry keys here: codec dimensions and frame rate belong
 * to VIDEO_ENCODER_INIT and must not be changed as a side effect of a local
 * safe-area viewport.
 */
internal data class CarLifeEncoderFormat(
    val panelWidth: Int,
    val panelHeight: Int,
    val safeWidth: Int,
    val safeHeight: Int,
    val safeX: Int,
    val safeY: Int,
    val needAdapterScreen: Boolean,
    val drawUiOutsideSafeArea: Boolean,
    val frameRate: Int?,
    val realWidth: Int?,
    val realHeight: Int?,
    val defaultWidth: Int?,
    val defaultHeight: Int?,
    val resolutionAccurate: Boolean?,
) {
    fun mediaFormatOverrides(): Map<String, Any> = buildMap {
        /*
         * Report the geometry CarLife should encode at.
         *
         * CarLife asks for these on carlife_encode_format and, when the answer
         * is missing, falls back to the resolution baked into its own settings
         * screen — a low default that looks soft on a wide panel. The car's
         * panel is already known from the AirPlay display negotiation, so
         * answer with it and CarLife renders natively instead of upscaling a
         * small frame. The coded dimensions still come from
         * VIDEO_ENCODER_INIT; these keys describe the layout CarLife draws.
         */
        put("save_area_width", safeWidth)
        put("save_area_height", safeHeight)
        put("save_area_x", safeX)
        put("save_area_y", safeY)
        put("need_adapter_screen", if (needAdapterScreen) 1 else 0)
        put("draw_ui_outside_safe_area", if (drawUiOutsideSafeArea) 1 else 0)
        put("panel_width", panelWidth)
        put("panel_height", panelHeight)
        put("real_width", panelWidth)
        put("real_height", panelHeight)
        put("default_width", panelWidth)
        put("default_height", panelHeight)
        put("res_accuracy", true)
    }

    companion object {
        fun fromJson(params: String?): CarLifeEncoderFormat? {
            if (params.isNullOrBlank()) return null
            val json = runCatching { JSONObject(params) }.getOrNull() ?: return null

            val panelWidth = json.int("width") ?: return null
            val panelHeight = json.int("height") ?: return null
            val safeWidth = json.int("save_area_width") ?: return null
            val safeHeight = json.int("save_area_height") ?: return null
            val safeX = json.int("save_area_x") ?: return null
            val safeY = json.int("save_area_y") ?: return null

            if (panelWidth !in MIN_PANEL_WIDTH..MAX_PANEL_DIMENSION) return null
            if (panelHeight !in MIN_PANEL_HEIGHT..MAX_PANEL_DIMENSION) return null
            if (safeWidth !in MIN_SAFE_WIDTH..panelWidth) return null
            if (safeHeight !in MIN_SAFE_HEIGHT..panelHeight) return null
            if (safeX < 0 || safeY < 0) return null
            if (safeX.toLong() + safeWidth > panelWidth) return null
            if (safeY.toLong() + safeHeight > panelHeight) return null

            return CarLifeEncoderFormat(
                panelWidth = panelWidth,
                panelHeight = panelHeight,
                safeWidth = safeWidth,
                safeHeight = safeHeight,
                safeX = safeX,
                safeY = safeY,
                needAdapterScreen = json.int("need_adapter_screen") == 1,
                drawUiOutsideSafeArea = json.int("draw_ui_outside_safe_area") == 1,
                frameRate = json.int("frame-rate") ?: json.int("frame_rate"),
                realWidth = json.int("real_width"),
                realHeight = json.int("real_height"),
                defaultWidth = json.int("default_width"),
                defaultHeight = json.int("default_height"),
                resolutionAccurate = json.boolean("res_accuracy"),
            )
        }

        private fun JSONObject.int(key: String): Int? =
            when (val value = opt(key)) {
                is Number -> value.toInt()
                is String -> value.toIntOrNull()
                else -> null
            }

        private fun JSONObject.boolean(key: String): Boolean? =
            when (val value = opt(key)) {
                is Boolean -> value
                is Number -> value.toInt() != 0
                is String -> value.toBooleanStrictOrNull()
                else -> null
            }

        private const val MIN_PANEL_WIDTH = 320
        private const val MIN_PANEL_HEIGHT = 160
        private const val MIN_SAFE_WIDTH = 320
        private const val MIN_SAFE_HEIGHT = 160
        private const val MAX_PANEL_DIMENSION = 16_384
    }
}
