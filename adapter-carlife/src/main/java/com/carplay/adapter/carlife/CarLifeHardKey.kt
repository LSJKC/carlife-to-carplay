package com.carplay.adapter.carlife

/**
 * CarLife's own hard-key codes.
 *
 * These are **not** Android `KeyEvent` codes. CarLife carries an integer in
 * `MSG_TOUCH_CAR_HARD_KEY_CODE` (425992) and interprets it through its own small
 * enumeration (`CarlifeTouchManager.onHardKeyCodeEvent`). Sending Android codes
 * into that space does not merely do nothing — the two spaces overlap, so:
 *
 *  - Android `KEYCODE_HOME` (3) lands on CarLife 3 = hang up the call;
 *  - Android `KEYCODE_DPAD_CENTER` (23) lands on CarLife 23 = directional up;
 *  - Android `KEYCODE_BACK` (4) and `KEYCODE_DPAD_UP` (19) do not exist there.
 *
 * That is why buttons "did nothing" instead of working, and why a phone button
 * could end a call. Every code below is taken from the table in
 * `CarlifeTouchManager.java` lines 1434-1517.
 */
internal object CarLifeHardKey {

    /** Answer an incoming call. */
    const val ANSWER = 2

    /** End the call. */
    const val HANG_UP = 3

    /** Knob turned clockwise. */
    const val KNOB_CLOCKWISE = 6

    /** Knob turned anticlockwise. */
    const val KNOB_ANTICLOCKWISE = 7

    /** Back out of the current screen. */
    const val BACK = 14

    /** Confirm the focused item. */
    const val OK = 20

    const val LEFT = 21
    const val RIGHT = 22
    const val UP = 23
    const val DOWN = 24

    /** Diagonal selector moves, used by the focus engine's grid. */
    const val LEFT_UP = 25
    const val RIGHT_UP = 26
    const val LEFT_DOWN = 27
    const val RIGHT_DOWN = 28

    /** Start and stop media playback. */
    const val PLAY = 31
    const val STOP = 32
    const val NEXT = 29
    const val PREVIOUS = 30

    /** Digits: 35 maps to 0, so digit `d` is `35 + d`. */
    const val DIGIT_BASE = 35
    const val DELETE = 47
    const val CLEAR = 48
    const val PLUS = 49

    /**
     * Voice recognition start and stop.
     *
     * These are CarLife's own codes (`CommonParams.KEYCODE_VR_START` /
     * `KEYCODE_VR_STOP`), so the car's voice button is a hard key here after
     * all — CarLife then runs recognition itself and reads the *phone's*
     * microphone, which is why its preference is called `UserPhoneMicFlag`.
     */
    const val VR_START = 33
    const val VR_STOP = 34

    /**
     * Translates an Android key code into CarLife's enumeration.
     *
     * Null means "CarLife has no code for this", which is logged and dropped
     * rather than guessed: mapping an unknown button onto a nearby number would
     * silently trigger an unrelated action such as hanging up.
     */
    fun fromAndroid(keyCode: Int): Int? = when (keyCode) {
        android.view.KeyEvent.KEYCODE_BACK -> BACK
        android.view.KeyEvent.KEYCODE_ESCAPE -> BACK
        // CarLife has no "home". Its BACK (14) is what returns to the CarLife
        // home screen, which is exactly what the car's home button means here.
        // Sending Android's 3 instead would land on CarLife 3 = hang up.
        android.view.KeyEvent.KEYCODE_HOME -> BACK
        android.view.KeyEvent.KEYCODE_DPAD_CENTER -> OK
        android.view.KeyEvent.KEYCODE_ENTER -> OK
        android.view.KeyEvent.KEYCODE_NUMPAD_ENTER -> OK
        android.view.KeyEvent.KEYCODE_DPAD_LEFT -> LEFT
        android.view.KeyEvent.KEYCODE_DPAD_RIGHT -> RIGHT
        android.view.KeyEvent.KEYCODE_DPAD_UP -> UP
        android.view.KeyEvent.KEYCODE_DPAD_DOWN -> DOWN
        android.view.KeyEvent.KEYCODE_MEDIA_PLAY -> PLAY
        android.view.KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> PLAY
        android.view.KeyEvent.KEYCODE_MEDIA_PAUSE -> STOP
        android.view.KeyEvent.KEYCODE_MEDIA_STOP -> STOP
        android.view.KeyEvent.KEYCODE_MEDIA_NEXT -> NEXT
        android.view.KeyEvent.KEYCODE_MEDIA_PREVIOUS -> PREVIOUS
        android.view.KeyEvent.KEYCODE_CALL -> ANSWER
        android.view.KeyEvent.KEYCODE_ENDCALL -> HANG_UP
        android.view.KeyEvent.KEYCODE_0 -> DIGIT_BASE
        android.view.KeyEvent.KEYCODE_1 -> DIGIT_BASE + 1
        android.view.KeyEvent.KEYCODE_2 -> DIGIT_BASE + 2
        android.view.KeyEvent.KEYCODE_3 -> DIGIT_BASE + 3
        android.view.KeyEvent.KEYCODE_4 -> DIGIT_BASE + 4
        android.view.KeyEvent.KEYCODE_5 -> DIGIT_BASE + 5
        android.view.KeyEvent.KEYCODE_6 -> DIGIT_BASE + 6
        android.view.KeyEvent.KEYCODE_7 -> DIGIT_BASE + 7
        android.view.KeyEvent.KEYCODE_8 -> DIGIT_BASE + 8
        android.view.KeyEvent.KEYCODE_9 -> DIGIT_BASE + 9
        android.view.KeyEvent.KEYCODE_DEL -> DELETE
        // Voice is a hard key after all: CarLife defines KEYCODE_VR_START (33)
        // and runs recognition itself against the phone's own microphone.
        android.view.KeyEvent.KEYCODE_VOICE_ASSIST -> VR_START
        // Deliberately absent: KEYCODE_HOME, which CarLife reads as 3 = hang up.
        else -> null
    }

    /** Human-readable name for the diagnostic line. */
    fun describe(keyCode: Int): String = android.view.KeyEvent.keyCodeToString(keyCode)
}
