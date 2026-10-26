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

    /** Home screen (CarLife's own 1, distinct from BACK=14). */
    const val HOME = 1

    /** Knob turned clockwise. */
    const val KNOB_CLOCKWISE = 6

    /** Knob turned anticlockwise. */
    const val KNOB_ANTICLOCKWISE = 7

    const val SETTINGS = 8
    const val MEDIA = 9
    const val RADIO = 10
    const val NAVI = 11
    const val SOURCE = 12
    const val MODE = 13

    /** Back out of the current screen. */
    const val BACK = 14

    const val VOLUME_SUB = 17
    const val VOLUME_ADD = 18
    const val MUTE = 19

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

    /*
     * Track skipping is NOT 29/30.
     *
     * `CommonParams` says 29 = KEYCODE_TEL and 30 = KEYCODE_MAIN, and
     * `CarlifeConnectInitUtils.lambda$initHardKeyCodeModule$1` confirms it:
     * 29 launches the phone screen, 30 launches the home screen. Sending a
     * steering-wheel "next track" as 29 therefore opened the dialler.
     *
     * The real skip codes are 15 and 16 (`KEYCODE_SEEK_SUB` /
     * `KEYCODE_SEEK_ADD`), which that same handler routes to
     * `MusicProvider.preSong(false)` / `nextSong(false)`.
     */
    const val NEXT = 16
    const val PREVIOUS = 15

    /** Launches the phone screen — never send this for a media button. */
    const val PHONE = 29

    /** Launches the CarLife home screen. */
    const val MAIN = 30

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
        /*
         * The car's home button means "go back to the CarLife home screen",
         * which is BACK (14). CarLife's own HOME is 1 and MAIN is 30; sending
         * Android's 3 would land on CarLife 3 = hang up the call.
         */
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
        // 16 / 15 — the SEEK codes, not 29/30. See NEXT / PREVIOUS above.
        android.view.KeyEvent.KEYCODE_MEDIA_NEXT -> NEXT
        android.view.KeyEvent.KEYCODE_MEDIA_PREVIOUS -> PREVIOUS
        android.view.KeyEvent.KEYCODE_CALL -> ANSWER
        android.view.KeyEvent.KEYCODE_ENDCALL -> HANG_UP
        android.view.KeyEvent.KEYCODE_VOLUME_UP -> VOLUME_ADD
        android.view.KeyEvent.KEYCODE_VOLUME_DOWN -> VOLUME_SUB
        android.view.KeyEvent.KEYCODE_VOLUME_MUTE -> MUTE
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
        else -> null
    }

    /**
     * Translates a CarLife key code back to Android's enumeration.
     *
     * This is used when the car sends hard keys through the AIDL callback,
     * which we need to route to Android's MediaSession. Only media-control
     * codes are meaningful here (NEXT, PREVIOUS, PLAY, STOP).
     */
    fun toAndroid(carLifeCode: Int): Int? = when (carLifeCode) {
        NEXT -> android.view.KeyEvent.KEYCODE_MEDIA_NEXT
        PREVIOUS -> android.view.KeyEvent.KEYCODE_MEDIA_PREVIOUS
        PLAY -> android.view.KeyEvent.KEYCODE_MEDIA_PLAY
        STOP -> android.view.KeyEvent.KEYCODE_MEDIA_STOP
        OK -> android.view.KeyEvent.KEYCODE_DPAD_CENTER
        BACK -> android.view.KeyEvent.KEYCODE_BACK
        UP -> android.view.KeyEvent.KEYCODE_DPAD_UP
        DOWN -> android.view.KeyEvent.KEYCODE_DPAD_DOWN
        LEFT -> android.view.KeyEvent.KEYCODE_DPAD_LEFT
        RIGHT -> android.view.KeyEvent.KEYCODE_DPAD_RIGHT
        ANSWER -> android.view.KeyEvent.KEYCODE_CALL
        HANG_UP -> android.view.KeyEvent.KEYCODE_ENDCALL
        VR_START -> android.view.KeyEvent.KEYCODE_VOICE_ASSIST
        else -> null
    }

    /** Human-readable name for the diagnostic line. */
    fun describe(keyCode: Int): String = android.view.KeyEvent.keyCodeToString(keyCode)
}
