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
     *
     * Those codes still only work on the touch socket, which is the path the
     * car itself never drives on this head unit. See [BROADCAST] below for the
     * path it actually uses.
     */
    const val NEXT = 16
    const val PREVIOUS = 15

    /*
     * The broadcast path — what the carreally uses under Honor's launcher.
     *
     * CarLife's own enum (`CommonConstants.TEST_KEY_*`) is a different
     * numbering from the touch-socket codes above:
     *
     *     8101 SEEK_SUB      8102 SEEK_ADD
     *     8103 MAIN          8104 TEL        8105 NAVI       8106 MEDIA
     *     8107 VR_START      8108 VR_STOP    8109 MEDIA_START 8110 MEDIA_STOP
     *     8111 PHONE_CALL    8112 PHONE_END  8113 NAVI_QUIT  8114 FOCUS_NEXT
     *     8115 FOCUS_PRE     8116 MUTE
     *
     * Honor's `o4.a` packages them into a broadcast:
     *
     *     Intent("com.hihonor.auto.ACTION_HARD_KRY_CLICK")
     *       .putExtra("mediaKeyCodeExtra", code)   // for 8101/8102/8109/8110
     *       .putExtra("dockKeyCodeExtra",  code)   // for the dock keys
     *
     * and both `CardHostManager` and `caraudio.a` listen for exactly that
     * action with `mediaKeyCodeExtra`. an established sender shows the
     * same two codes arriving at its own listener:
     *
     *     key name=暂停 down=true code=8110 accepted=true
     *     key name=播放 down=true code=8109 accepted=true
     *
     * None of this reaches us today: the car sends no HID report, we never
     * build the iAP2 link that carries 0x6800, and the touch-socket hard key
     * is never driven. This broadcast is the remaining path.
     */
    const val BROADCAST_ACTION = "com.hihonor.auto.ACTION_HARD_KRY_CLICK"
    const val BROADCAST_MEDIA_EXTRA = "mediaKeyCodeExtra"
    const val BROADCAST_DOCK_EXTRA = "dockKeyCodeExtra"

    /** CarLife's own broadcast codes (`CommonConstants.TEST_KEY_*`). */
    const val B_SEEK_SUB = 8101
    const val B_SEEK_ADD = 8102
    const val B_MAIN = 8103
    const val B_TEL = 8104
    const val B_NAVI = 8105
    const val B_MEDIA = 8106
    const val B_VR_START = 8107
    const val B_VR_STOP = 8108
    const val B_MEDIA_START = 8109
    const val B_MEDIA_STOP = 8110
    const val B_PHONE_CALL = 8111
    const val B_PHONE_END = 8112
    const val B_NAVI_QUIT = 8113
    const val B_FOCUS_NEXT = 8114
    const val B_FOCUS_PRE = 8115
    const val B_MUTE = 8116

    /** Broadcast codes Honor carries in `mediaKeyCodeExtra` (o4.a.f26029c). */
    val BROADCAST_MEDIA_CODES = setOf(B_SEEK_SUB, B_SEEK_ADD, B_MEDIA_START, B_MEDIA_STOP)

    /** Maps an Android key to CarLife's broadcast numbering, or null. */
    fun toBroadcastCode(androidKeyCode: Int): Int? = when (androidKeyCode) {
        android.view.KeyEvent.KEYCODE_MEDIA_NEXT,
        android.view.KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> B_SEEK_ADD
        android.view.KeyEvent.KEYCODE_MEDIA_PREVIOUS,
        android.view.KeyEvent.KEYCODE_MEDIA_REWIND -> B_SEEK_SUB
        android.view.KeyEvent.KEYCODE_MEDIA_PLAY,
        android.view.KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> B_MEDIA_START
        android.view.KeyEvent.KEYCODE_MEDIA_PAUSE -> B_MEDIA_STOP
        android.view.KeyEvent.KEYCODE_MEDIA_STOP -> B_MEDIA_STOP
        android.view.KeyEvent.KEYCODE_HEADSETHOOK -> B_MEDIA_START
        else -> null
    }

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
