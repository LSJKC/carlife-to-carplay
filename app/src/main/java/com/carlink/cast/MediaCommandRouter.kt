package com.carlink.cast

import android.content.Context
import android.media.AudioManager
import android.view.KeyEvent
import android.os.SystemClock

/**
 * Routes steering-wheel transport keys to the phone's active player.
 *
 * CarLife's hard-key socket is for the car UI; it does not automatically
 * forward NEXT/PREVIOUS to the Android player that owns the audio. Buick-E5
 * sends the pair through AudioManager, which is the system-wide media-key
 * route and does not depend on notification-listener access or on a
 * MediaController being selected correctly.
 */
internal object MediaCommandRouter {
    @Volatile private var appContext: Context? = null

    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    fun dispatch(keyCode: Int): Boolean {
        val context = appContext ?: return false
        val audio = context.getSystemService(AudioManager::class.java) ?: return false
        if (keyCode !in SUPPORTED_MEDIA_KEYS) return false

        // Match Buick-E5: one complete DOWN/UP pair.  Do not call a
        // MediaController first; an accessible controller can still be the
        // wrong app or have no transport callback, and that would swallow
        // NEXT/PREVIOUS before this system-wide route gets a chance.
        val now = SystemClock.uptimeMillis()
        val success = runCatching {
            audio.dispatchMediaKeyEvent(KeyEvent(now, now, KeyEvent.ACTION_DOWN, keyCode, 0))
            audio.dispatchMediaKeyEvent(KeyEvent(now, now, KeyEvent.ACTION_UP, keyCode, 0))
        }.onFailure {
            CastLog.event("WARN", "媒体按键转发失败：${it.javaClass.simpleName}: ${it.message}")
        }.isSuccess
        if (success) {
            CastLog.event("INFO", "车机媒体键已按 E5 路由注入：${KeyEvent.keyCodeToString(keyCode)}")
        }
        return success
    }

    private val SUPPORTED_MEDIA_KEYS = setOf(
        KeyEvent.KEYCODE_MEDIA_NEXT,
        KeyEvent.KEYCODE_MEDIA_PREVIOUS,
        KeyEvent.KEYCODE_MEDIA_PLAY,
        KeyEvent.KEYCODE_MEDIA_PAUSE,
        KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
    )
}
