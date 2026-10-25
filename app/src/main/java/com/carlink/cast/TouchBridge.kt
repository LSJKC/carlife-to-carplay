package com.carlink.cast

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Intent
import android.graphics.Path
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent

/**
 * Hands car touch and key events to the phone.
 *
 * CarLife does not need this: the car's events go into CarLife's own socket and
 * it handles them. Mirroring does, because there is no other public way to
 * inject a gesture into an arbitrary app without root.
 */
internal object TouchBridge {
    @Volatile var touch: ((Int, Float, Float, Int) -> Boolean)? = null
    @Volatile var key: ((Int, Boolean) -> Boolean)? = null

    fun available(): Boolean = touch != null

    fun dispatch(action: Int, x: Float, y: Float, pointerId: Int): Boolean =
        touch?.invoke(action, x, y, pointerId) ?: false

    fun dispatchKey(keyCode: Int, isDown: Boolean): Boolean =
        key?.invoke(keyCode, isDown) ?: false
}

/** Enabled only by the user's accessibility grant and an active session. */
class TouchService : AccessibilityService() {
    private var stroke: GestureDescription.StrokeDescription? = null
    private var lastX = 0f
    private var lastY = 0f

    override fun onServiceConnected() {
        TouchBridge.touch = { action, x, y, pointer ->
            if (!CastLog.state.value.running || pointer != 0) false else {
                val path = Path().apply {
                    moveTo(if (stroke == null) x else lastX, if (stroke == null) y else lastY)
                    lineTo(x, y)
                }
                val continuing = action != 1
                val next = stroke?.continueStroke(path, 0, 32, continuing)
                    ?: GestureDescription.StrokeDescription(path, 0, 32, continuing)
                val accepted = dispatchGesture(GestureDescription.Builder().addStroke(next).build(), null, null)
                if (accepted) {
                    stroke = if (continuing) next else null
                    lastX = x
                    lastY = y
                }
                accepted
            }
        }
        TouchBridge.key = { code, down ->
            if (!CastLog.state.value.running) false
            else if (!down) true
            else when (code) {
                KeyEvent.KEYCODE_BACK -> performGlobalAction(GLOBAL_ACTION_BACK)
                KeyEvent.KEYCODE_HOME -> performGlobalAction(GLOBAL_ACTION_HOME)
                // The car's call button arrives as HID usage 0x20 on the
                // telephony page and is mapped to KEYCODE_CALL. A real iPhone
                // shows its own CarPlay phone screen because iOS renders it;
                // an Android phone has no such screen, so the equivalent is to
                // bring up the dialer. Without this the button did nothing and
                // the head unit was left waiting on a call that never started.
                KeyEvent.KEYCODE_CALL -> openDialer()
                KeyEvent.KEYCODE_ENDCALL -> performGlobalAction(GLOBAL_ACTION_BACK)
                KeyEvent.KEYCODE_VOICE_ASSIST -> launchVoiceSearch()
                KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> false
                else -> false
            }
        }
    }

    /**
     * Opens the system dialer.
     *
     * The car's call button means "show me the phone". On a real iPhone the car
     * gets a CarPlay phone screen because iOS draws it; an Android phone has no
     * such screen, and the closest equivalent is the dialer, which shows recent
     * calls and lets the driver dial. ACTION_DIAL is used rather than
     * ACTION_CALL because it needs no CALL_PHONE permission and cannot place a
     * call by accident — the driver still confirms.
     */
    private fun openDialer(): Boolean {
        val intent = Intent(Intent.ACTION_DIAL, android.net.Uri.parse("tel:")).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        return runCatching { startActivity(intent); true }
            .onFailure { CastLog.event("WARN", "打开拨号盘失败：${it.message}") }
            .getOrDefault(false)
    }

    /** Brings up the phone's assistant, the Android counterpart of Siri. */
    private fun launchVoiceSearch(): Boolean {
        for (action in listOf(Intent.ACTION_ASSIST, Intent.ACTION_VOICE_COMMAND)) {
            val intent = Intent(action).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
            if (runCatching { startActivity(intent); true }.getOrDefault(false)) return true
        }
        CastLog.event("WARN", "本机没有可用的语音助手")
        return false
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) = Unit
    override fun onInterrupt() { stroke = null }
    override fun onDestroy() {
        TouchBridge.touch = null
        TouchBridge.key = null
        stroke = null
        super.onDestroy()
    }
}
