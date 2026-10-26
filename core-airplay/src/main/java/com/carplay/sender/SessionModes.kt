package com.carplay.sender

/** Ownership derives from requests and notifications; packets being sent are not ownership proof. */
class SessionModes {
    private data class Resource(var owner: Long = 1, var permanent: Long = 1)

    /**
     * Initial ownership, taken from what the car declares in /info.
     *
     * This head unit's `modes.initialPermanentEntity` is explicit:
     *
     *     resourceID 1 (screen) permanentEntity 2   — the car owns the screen
     *     resourceID 2 (audio)  permanentEntity 1   — the phone owns audio
     *
     * Starting from the opposite values told the car that the phone already
     * owned a screen it had not granted, and that the car owned audio the
     * phone was about to send. Every ownership snapshot this app posted was
     * therefore self-contradictory, and the car answered by taking the screen
     * back and, eventually, resetting the session.
     */
    private val resources = mutableMapOf(1L to Resource(2, 2), 2L to Resource(1, 1))
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
        // appStates is not decoration. A working sender on this firmware always
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
        resources.getValue(2).apply { owner = if (phone) 1 else 2; permanent = owner }
    }
}
