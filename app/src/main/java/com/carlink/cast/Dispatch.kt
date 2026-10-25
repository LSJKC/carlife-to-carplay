package com.carlink.cast

import android.view.KeyEvent
import com.carplay.adapter.PhoneProjectionAdapter
import com.carplay.sender.HidKeyReport
import com.carplay.sender.HidUsage
import com.carplay.sender.SenderDisplay
import com.carplay.sender.TouchSample

/**
 * Car input, translated once for every source.
 *
 * Touch coordinates arrive normalised to the display the car negotiated, which
 * is exactly what both sources expect, so no scaling happens here. CarLife
 * wants the same numbers; mirroring maps them back through its capture geometry.
 */
internal object Dispatch {
    private val contacts = mutableSetOf<Int>()

    fun input(command: Map<*, *>, source: PhoneProjectionAdapter, display: SenderDisplay?): Boolean {
        try {
            when (command["type"]) {
                "keySamples" -> {
                    val report = command["report"] as? HidKeyReport ?: return false
                    fun map(usage: HidUsage): Int? = when (usage.page to usage.usage) {
                        9 to 1 -> KeyEvent.KEYCODE_DPAD_CENTER
                        0x0c to 0x223 -> KeyEvent.KEYCODE_HOME
                        0x0c to 0x224 -> KeyEvent.KEYCODE_BACK
                        0x0c to 0xb0 -> KeyEvent.KEYCODE_MEDIA_PLAY
                        0x0c to 0xb1 -> KeyEvent.KEYCODE_MEDIA_PAUSE
                        0x0c to 0xcd -> KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                        0x0c to 0xb5 -> KeyEvent.KEYCODE_MEDIA_NEXT
                        0x0c to 0xb6 -> KeyEvent.KEYCODE_MEDIA_PREVIOUS
                        // HID Telephony Device page. The car's call button arrives
                        // here, not on the consumer page: this head unit sends
                        // usage 0x20 (hook switch) when the driver presses it and
                        // 0x26 when the call is dropped. Dropping these was what
                        // made the button do nothing while the car itself still
                        // believed CarPlay had been asked to open its phone UI.
                        0x0b to 0x20 -> KeyEvent.KEYCODE_CALL
                        0x0b to 0x21 -> KeyEvent.KEYCODE_CALL
                        0x0b to 0x23 -> KeyEvent.KEYCODE_CALL
                        0x0b to 0x26 -> KeyEvent.KEYCODE_ENDCALL
                        0x0b to 0x2e -> KeyEvent.KEYCODE_MUTE
                        // Voice: the car's steering-wheel/toolbar voice button.
                        // Both the consumer-page Voice Command usage and the
                        // telephony-page "phone mute" alias appear on real
                        // head units; either one means "start talking".
                        0x0c to 0x221 -> KeyEvent.KEYCODE_VOICE_ASSIST
                        0x0c to 0xcf -> KeyEvent.KEYCODE_VOICE_ASSIST
                        else -> null
                    }
                    // Record what the car sent even when the usage is unknown,
                    // so the button map can be built from real presses.
                    report.changes.forEach { change ->
                        val mapped = map(change.usage)
                        CastLog.event("INFO", "车机按键 usage=0x${change.usage.usage.toString(16)} " +
                            "page=0x${change.usage.page.toString(16)} ${if (change.down) "按下" else "松开"} " +
                            "→ ${mapped ?: "未映射"}")
                    }
                    // An unmapped usage is acknowledged, not refused. Answering
                    // 501 to a key the map does not know made the car retry the
                    // report and then drop the whole session; a button whose
                    // meaning is unknown is simply not dispatched locally.
                    report.changes.forEach { change ->
                        val mapped = map(change.usage) ?: return@forEach
                        source.onKeyEvent(mapped, change.down)
                        CastLog.input()
                    }
                    if (report.wheel != 0) CastLog.event("INFO", "车机旋钮 wheel=${report.wheel}")
                    repeat(kotlin.math.abs(report.wheel)) {
                        val key = if (report.wheel < 0) KeyEvent.KEYCODE_DPAD_UP else KeyEvent.KEYCODE_DPAD_DOWN
                        source.onKeyEvent(key, true)
                        source.onKeyEvent(key, false)
                        CastLog.input()
                    }
                }
                "requestSiri" -> {
                    val action = ((command["params"] as? Map<*, *>)?.get("siriAction") as? Number)?.toInt()
                    if (action !in setOf(2, 3)) return false
                    source.onKeyEvent(KeyEvent.KEYCODE_VOICE_ASSIST, action == 2)
                    CastLog.input()
                }
                // The head unit polls these while it owns the screen. They are
                // notifications, not requests: answering 501 made it drop the
                // session about half a minute in, which is what "过一会儿自己
                // 断开" looked like in the field. setLimitedUI arrives in the
                // same burst as the driver returns to the car's own UI, so it
                // belongs here too.
                "requestUI", "setNightMode", "updateViewArea", "setLimitedUI" -> {
                    // updateViewArea is the only command that can tell us, at
                    // runtime, which part of the panel CarPlay should occupy, so
                    // its parameters are printed instead of just acknowledged.
                    // Discarding them was leaving the left-edge placement with
                    // no runtime evidence at all.
                    if (command["type"] == "updateViewArea") {
                        CastLog.event("INFO", "车机命令 updateViewArea 参数=${describe(command["params"])}")
                    } else {
                        CastLog.event("INFO", "车机命令 ${command["type"]} 已确认")
                    }
                }
                "touchSamples" -> {
                    val screen = display ?: return false
                    (command["samples"] as? List<*>)?.filterIsInstance<TouchSample>()?.forEach { touch ->
                        val previous = touch.id in contacts
                        if (touch.down || previous) {
                            val action = if (!touch.down) 1 else if (previous) 2 else 0
                            val (px, py) = touchToVideo(touch, screen)
                            if (!px.isNaN() && !py.isNaN()) {
                                source.onTouchEvent(action, px, py, touch.id)
                                CastLog.input()
                            }
                            if (touch.down) contacts += touch.id else contacts -= touch.id
                        }
                    }
                }
                "hidSendReport" -> {
                    val report = command["hidReport"] as? ByteArray ?: return false
                    if (command["uuid"]?.toString() != "2a2a2a2a") {
                        // Unknown HID devices are still acknowledged. Returning 501
                        // makes the head unit retry the same report and can stall the
                        // event channel; only the known touch layout is dispatched.
                        CastLog.event("INFO", "未支持的车机输入报告：长度 ${report.size}，已确认并忽略")
                        return true
                    }
                    require(report.size == 12)
                    for (offset in listOf(0, 6)) {
                        val x = ((report[offset + 2].toInt() and 255) or
                            ((report[offset + 3].toInt() and 255) shl 8)).toFloat()
                        val y = ((report[offset + 4].toInt() and 255) or
                            ((report[offset + 5].toInt() and 255) shl 8)).toFloat()
                        source.onTouchEvent(
                            if (report[offset + 1].toInt() and 1 != 0) 2 else 1,
                            x, y, report[offset].toInt() and 255,
                        )
                    }
                    CastLog.input()
                }
                else -> {
                    CastLog.event("INFO", "车机操作未支持：${command["type"]}")
                    return false
                }
            }
            return true
        } catch (error: Exception) {
            CastLog.event("WARN", "车机操作未执行：${error.message}")
            return false
        }
    }

    /**
     * Converts one car touch report into the pixel space of the streamed video.
     *
     * [TouchSample.x] and [TouchSample.y] arrive already normalised to 0..1, so
     * they are scaled by the *video* size. Multiplying them by the car's HID
     * extent a second time was the defect that made every tap land in the
     * bottom-right corner: a contact at 0.68/0.83 became 1506x617 in a
     * 1408x476 picture and was clamped to the last pixel on both axes, so only
     * drags — which the car reads as a gesture regardless of position — ever
     * appeared to work.
     *
     * The extent is not used as a scale because it does not need to be: this
     * panel reports 2200x744 and the stream is 1408x476, and the two have the
     * same aspect ratio to three digits (2.957), so the touch surface maps onto
     * the video by pure scaling with no offset.
     */
    private fun touchToVideo(touch: TouchSample, screen: SenderDisplay): Pair<Float, Float> {
        val extentX = touch.maxX.takeIf { it > 0 } ?: screen.width
        val extentY = touch.maxY.takeIf { it > 0 } ?: screen.height
        // The GM HID reports the whole panel (2200x744), while AirPlay accepts
        // the negotiated 1408x476 view. Convert through the declared logical
        // extent exactly once and reject points outside that view instead of
        // clamping them into its last row/column.
        val rawX = touch.x * extentX
        val rawY = touch.y * extentY
        val scaledX = rawX * (screen.width - 1) / extentX
        val scaledY = rawY * (screen.height - 1) / extentY
        if (scaledX !in 0f..(screen.width - 1).toFloat() ||
            scaledY !in 0f..(screen.height - 1).toFloat()
        ) {
            logTouch(touch, screen, scaledX, scaledY, extentX, extentY)
            return Float.NaN to Float.NaN
        }
        logTouch(touch, screen, scaledX, scaledY, extentX, extentY)
        return scaledX to scaledY
    }

    /** Contacts already described; keeps the journal readable while still proving the mapping. */
    private var touchLogged = 0

    /**
     * Records the first few contacts in full and then only every fiftieth.
     *
     * The mapping cannot be checked from a summary: the raw axis value, the
     * extent the car declared and the resulting pixel all have to be visible
     * together, or "the touch lands somewhere else" has no evidence behind it.
     */
    private fun logTouch(
        touch: TouchSample,
        screen: SenderDisplay,
        px: Float,
        py: Float,
        extentX: Int,
        extentY: Int,
    ) {
        val index = touchLogged++
        if (index >= 12 && index % 40 != 0) return
        // Everything needed to solve the offset arithmetically is on one line:
        // the fraction the car sent, the extent it was normalised against, and
        // the pixel this app then dispatched. Tapping a known point and reading
        // this line gives the residual shift directly, instead of another round
        // of guessing at it.
        CastLog.event(
            "INFO",
            "触摸 #$index id=${touch.id} ${if (touch.down) "按下" else "抬起"} " +
                "原始=(${Math.round(touch.x * extentX)}, ${Math.round(touch.y * extentY)}) " +
                "归一化=(${"%.4f".format(touch.x)}, ${"%.4f".format(touch.y)}) " +
                "量程=${extentX}×${extentY} " +
                "→ 画面=(${px.toInt()}, ${py.toInt()}) / ${screen.width}×${screen.height}",
        )
    }

    /**
     * Renders a command's parameters for the log without assuming their shape.
     *
     * updateViewArea carries nested maps and numbers whose names are only known
     * to the car, so the whole structure is flattened rather than looked up by
     * a guessed key.
     */
    private fun describe(value: Any?, depth: Int = 0): String = when (value) {
        null -> "null"
        is Map<*, *> -> value.entries.joinToString(", ", "{", "}") { (key, item) ->
            "$key=${describe(item, depth + 1)}"
        }
        is List<*> -> value.joinToString(", ", "[", "]") { describe(it, depth + 1) }
        is ByteArray -> value.joinToString("") { "%02X".format(it) }
        else -> value.toString()
    }

    fun reset() = contacts.clear()
}
