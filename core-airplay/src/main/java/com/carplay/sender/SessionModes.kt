package com.carplay.sender

/** Ownership derives from requests and notifications; packets being sent are not ownership proof. */
class SessionModes {
    private data class Resource(var owner: Long = 2, var permanent: Long = 2)

    /*
     * Both resources start on the car, exactly as the head unit opens a session.
     *
     * A real iPhone on this very car (BUICK_2874, sysdiagnose 2026-10-31 07:58)
     * prints its ownership trajectory and it does not start on the phone:
     *
     *     07:58:22.535  Initial modes : Car Take screen (priority Nice), Take mainAudio
     *     07:58:22.535  Modes changed: screen Car (permanent: Car), mainAudio Car (permanent: Car)
     *     07:58:22.545  Modes changed: screen Car (permanent: Car), mainAudio iOS (permanent: Car)
     *     07:58:23.493  Modes changed: screen iOS (permanent: iOS), mainAudio iOS (permanent: Car)
     *
     * Two things in that trace matter:
     *
     *  - the phone never owns the screen at declaration time; it takes it about
     *    a second *after* the audio SETUP, as a real transition the car sees;
     *  - `mainAudio` keeps `permanent: Car` for the whole session even while the
     *    phone owns it. [claimScreen] and the audio claim below therefore move
     *    `owner` only.
     *
     * Starting the screen on the phone with `permanent: iOS` skipped that
     * transition: the car was told the phone already had the screen, so the
     * changeModes it sends when it takes the screen for its own UI
     * ("Car Take screen (priority User)") arrived out of order. In the field
     * that showed up as the screen flipping Car/iOS twice and the session being
     * reset 13.1 s after RECORD (cc1104l 11797 → 13004 → reset 20856), while the
     * same car's real iPhone did the same flips at 42.7 s and 47.6 s and simply
     * kept running.
     */
    private val resources = mutableMapOf(1L to Resource(), 2L to Resource(2, 2))
    /** Result of a changeModes request, split by resource so callers cannot reclaim audio on a screen restore. */
    data class Change(
        val reply: Map<String, Any>,
        val screenRestored: Boolean,
        val audioRestored: Boolean,
    )

    @Synchronized fun change(command: Map<*, *>): Change {
        require(command["type"] == "changeModes")
        val parameters = command["params"] as? Map<*, *> ?: error("changeModes 参数缺失")
        val requests = (parameters["resources"] as? List<*> ?: error("资源请求缺失")).map { item ->
            val entry = item as? Map<*, *> ?: error("资源请求无效")
            val id = (entry["resourceID"] as? Number)?.toLong() ?: error("资源编号缺失")
            require(id in resources)
            val type = (entry["transferType"] as? Number)?.toInt() ?: error("资源转移类型缺失")
            require(type in 1..4)
            id to type
        }
        var screenRestored = false
        var audioRestored = false
        for ((id,type) in requests) {
            val resource = resources.getValue(id)
            val wasPhone = resource.owner == 1L
            when (type) {
                1 -> { resource.owner = 2; resource.permanent = 2 }
                2 -> { resource.owner = 1; resource.permanent = 1 }
                3 -> resource.owner = 2
                4 -> resource.owner = resource.permanent
                else -> error("不支持的资源转移类型")
            }
            if (!wasPhone && resource.owner == 1L) {
                if (id == 1L) screenRestored = true
                if (id == 2L) audioRestored = true
            }
        }
        return Change(snapshot(), screenRestored, audioRestored)
    }
    @Synchronized fun snapshot(): Map<String, Any> = mapOf("type" to "modesChanged", "params" to mapOf(
        "resources" to resources.map { (id, resource) -> mapOf("resourceID" to id, "entity" to resource.owner, "permanentEntity" to resource.permanent) },
        // appStates is not decoration. An established sender on this firmware always
        // announces the three app states with entity 0, and the speech state
        // additionally carries speechMode -1. Leaving the key out leaves the
        // car without a state table for the phone's apps, and the resources
        // list alone did not restore speakers/HID after the car borrowed them.
        "appStates" to listOf(
            mapOf("appStateID" to 2, "entity" to 0),
            mapOf("appStateID" to 3, "entity" to 0),
            mapOf("appStateID" to 1, "entity" to 0, "speechMode" to -1),
        ),
    ))
    /**
     * Takes the screen back after the car asks the phone to show its picture.
     *
     * Returns true when ownership actually changed, so the caller knows a fresh
     * decoder needs a key frame. The car hands the screen to its own UI with a
     * changeModes request and does **not** always send the mirroring
     * changeModes when the driver returns; the only remaining signal is
     * `requestUI`, and ignoring it left ownership with the car, which meant
     * every frame was dropped and the picture came back transparent.
     */
    @Synchronized fun claimScreen(): Boolean {
        val resource = resources.getValue(1)
        if (resource.owner == 1L) return false
        resource.owner = 1
        // requestUI is an explicit phone reclaim.  If the car had taken the
        // screen permanently earlier (transferType=1) and only returned it
        // through requestUI, leaving permanent=2 made the next temporary
        // restore (transferType=4) hand the screen back to the car again.
        resource.permanent = 1
        return true
    }

    @Synchronized fun ownsScreen() = resources.getValue(1).owner == 1L
    @Synchronized fun ownsAudio() = resources.getValue(2).owner == 1L
    @Synchronized fun audio(phone: Boolean) {
        /*
         * Only `owner` moves. The real iPhone on this car keeps
         * `mainAudio (permanent: Car)` for the entire session — 07:58:22.545
         * through 07:58:53.149 in the 2026-10-31 capture, every single line —
         * while `owner` goes Car → iOS → iOS. Moving `permanent` with it told
         * the car the phone had claimed audio for good, so the car's own
         * "borrow it back" requests came out of order with our snapshot.
         */
        resources.getValue(2).owner = if (phone) 1 else 2
    }

    /**
     * Hands both resources back to the car, for a session that is ending.
     *
     * A sender that just closes its sockets leaves the head unit holding a
     * CarPlay session it still believes is live: the car's own UI does not
     * come back until its watchdog expires. The graceful retire sends
     * the same ownership snapshot with both resources on the accessory before
     * TEARDOWN, and the car returns to its home screen immediately.
     *
     * Returns true when anything actually changed, so the caller only spends a
     * frame on the wire when the car would otherwise be left thinking the
     * phone still owns the screen.
     */
    @Synchronized fun release(): Boolean {
        var changed = false
        for (id in listOf(1L, 2L)) {
            val resource = resources.getValue(id)
            if (resource.owner != 2L || resource.permanent != 2L) {
                resource.owner = 2
                resource.permanent = 2
                changed = true
            }
        }
        return changed
    }
}
