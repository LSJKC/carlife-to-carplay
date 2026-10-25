package com.carplay.adapter.carlife

import android.view.KeyEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Pins the Android → CarLife key translation.
 *
 * These assertions exist because the two code spaces overlap: sending Android
 * codes straight through made the home button hang up a call (3) and the centre
 * button move the selection up (23). A regression here is silent on a desk test
 * and only shows up in a car, so every mapping is spelled out.
 */
class CarLifeHardKeyTest {

    @Test
    fun `the codes that overlap dangerously are translated, not forwarded`() {
        // Android HOME is 3, which CarLife reads as hang up.
        assertEquals(CarLifeHardKey.BACK, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_HOME))
        // Android DPAD_CENTER is 23, which CarLife reads as directional up.
        assertEquals(CarLifeHardKey.OK, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_DPAD_CENTER))
        // Android BACK is 4, which CarLife does not define at all.
        assertEquals(CarLifeHardKey.BACK, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_BACK))
    }

    @Test
    fun `navigation and media keys map to their CarLife counterparts`() {
        assertEquals(CarLifeHardKey.LEFT, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_DPAD_LEFT))
        assertEquals(CarLifeHardKey.RIGHT, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_DPAD_RIGHT))
        assertEquals(CarLifeHardKey.UP, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_DPAD_UP))
        assertEquals(CarLifeHardKey.DOWN, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_DPAD_DOWN))
        assertEquals(CarLifeHardKey.OK, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_ENTER))
        assertEquals(CarLifeHardKey.PLAY, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_MEDIA_PLAY))
        assertEquals(CarLifeHardKey.PLAY, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE))
        assertEquals(CarLifeHardKey.STOP, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_MEDIA_PAUSE))
        assertEquals(CarLifeHardKey.NEXT, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_MEDIA_NEXT))
        assertEquals(CarLifeHardKey.PREVIOUS, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_MEDIA_PREVIOUS))
        assertEquals(CarLifeHardKey.ANSWER, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_CALL))
        assertEquals(CarLifeHardKey.HANG_UP, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_ENDCALL))
    }

    @Test
    fun `CarPlay next and previous use the CarLife hard-key channel`() {
        // Dispatch translates CarPlay HID media usages to Android media keys,
        // then CarLifeProjectionAdapter translates those keys to 29/30.
        // The adapter sends those values through MSG_TOUCH_CAR_HARD_KEY_CODE;
        // E5's AudioManager route is intentionally not part of this path.
        assertEquals(29, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_MEDIA_NEXT))
        assertEquals(30, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_MEDIA_PREVIOUS))
        assertEquals(CarLifeProtocol.MSG_TOUCH_CAR_HARD_KEY_CODE, 425992)
    }

    @Test
    fun `digits use CarLife's own base and delete sits after them`() {
        assertEquals(CarLifeHardKey.DIGIT_BASE, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_0))
        assertEquals(CarLifeHardKey.DIGIT_BASE + 9, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_9))
        assertEquals(CarLifeHardKey.DELETE, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_DEL))
    }

    @Test
    fun `voice is a hard key that starts CarLife's own recognition`() {
        // CarLife defines KEYCODE_VR_START (33); it then records from the
        // phone's microphone itself, which is why no audio has to be uplinked.
        assertEquals(CarLifeHardKey.VR_START, CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_VOICE_ASSIST))
    }

    @Test
    fun `keys CarLife has no code for are dropped instead of guessed`() {
        // Guessing here would trigger an unrelated action; 3 means hang up.
        assertNull(CarLifeHardKey.fromAndroid(KeyEvent.KEYCODE_MENU))
    }
}
