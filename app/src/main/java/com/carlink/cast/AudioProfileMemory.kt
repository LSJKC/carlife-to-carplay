package com.carlink.cast

import android.content.Context
import com.carplay.sender.SenderAudioFormat

/**
 * Remembers which audio profile a specific head unit actually played.
 *
 * The store only reorders profiles; it never invents a format that was not
 * advertised by the current `/info` response. Two rules keep it from
 * overriding the sender's own preference order:
 *
 * 1. A remembered profile is promoted only when it is one this build would
 *    have chosen anyway, or when the leading candidate has been *rejected*.
 *    Otherwise a stale "working" entry from an older build silently undoes a
 *    deliberate change to the preference order — which is exactly what
 *    happened between 1.3.0 and 1.3.2: PCM was made first, and this store
 *    kept putting the previously remembered AAC ahead of it.
 *
 * 2. "Working" now means audio packets actually flowed and were still flowing
 *    when the check fired. It used to be recorded as soon as SETUP returned
 *    200, which says only that the receiver accepted the parameters — the
 *    field log ends with `音频档位已后置：102…（SETUP 后实际音频流未稳定）`,
 *    the store demoting the very profile it had just promoted.
 */
internal class AudioProfileMemory(context: Context, private val receiver: String) {
    private val prefs = context.applicationContext.getSharedPreferences(
        "carlink_audio_profiles",
        Context.MODE_PRIVATE,
    )
    private val scope = receiver.lowercase()
        .replace(Regex("[^a-z0-9]+"), "_")
        .trim('_')
        .take(64)
        .ifBlank { "unknown" }

    /**
     * Demotes profiles this receiver has already refused; otherwise leaves the
     * sender's preference order alone.
     *
     * The sender's candidate order is the authoritative statement of what this
     * build wants to negotiate (`SenderNegotiation.audioCandidates`: PCM before
     * AAC). This store may only take a profile *away* from the front, never put
     * one back: a "working" entry is stale the moment the preference order
     * changes, and one survived here across three builds, quietly turning the
     * PCM-first change into an AAC-first session under a log line claiming the
     * opposite.
     *
     * The remembered working profile is therefore reported, not applied — it is
     * still the last profile that actually produced audio on this receiver, and
     * that is what makes a demotion readable in the log.
     */
    fun order(candidates: List<SenderAudioFormat>): List<SenderAudioFormat> {
        if (candidates.isEmpty()) return emptyList()
        discardUnreliableHistory()
        val rejected = prefs.getString("$scope:rejected", null)
            ?.split(',')
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?.toSet()
            .orEmpty()
        // Preserve the incoming order on both sides of the split: only the
        // demotion itself is this store's decision.
        val (safe, refused) = candidates.partition { it.key !in rejected }
        return (safe + refused).distinctBy { it.key }
    }

    /** The last profile that produced audio on this receiver, for diagnostics only. */
    fun lastWorking(): String? = prefs.getString("$scope:working", null)

    /**
     * Drops every demotion this build did not earn.
     *
     * Up to 1.3.21 a profile was marked rejected whenever a session ended after
     * carrying any audio packets at all. On this head unit the *session* is torn
     * down at 13.1 s for reasons that have nothing to do with the codec, so that
     * rule wrote off every profile it ever tried — the field log shows PCM
     * marked `已拒` before it had ever been used, which then kept AAC first for
     * the life of the install.
     *
     * Those entries cannot be re-judged, and keeping them would let a bug in the
     * judging rule outlive its fix. The rule itself is fixed as well
     * ([AUDIO_PROFILE_REJECT_MIN_PACKETS]); this only clears the damage already
     * on disk, once.
     *
     * 1.3.45 re-clears with a new flag key. The 1.3.22 pass had already run on
     * every install, so entries written after it — including the pairs seen in
     * `cc1104i` where *both* `102|AAC…` and `100|PCM…` sat in the rejected set
     * at once, which makes the demotion meaningless — survived into 1.3.44.
     * A store that believes every profile is refused cannot order anything.
     */
    private fun discardUnreliableHistory() {
        if (prefs.getBoolean(KEY_HISTORY_CLEARED, false)) return
        prefs.edit()
            .remove("$scope:rejected")
            .remove("$scope:working")
            .putBoolean(KEY_HISTORY_CLEARED, true)
            .apply()
    }

    /** Profiles this receiver has refused, in the order they were refused. */
    fun rejected(): List<String> = prefs.getString("$scope:rejected", null)
        ?.split(',')
        ?.map { it.trim() }
        ?.filter { it.isNotEmpty() }
        .orEmpty()

    fun recordWorking(profile: SenderAudioFormat) {
        val key = profile.key
        // A profile that just produced audio is not rejected any more. Without
        // this, a demotion was permanent: `rejected` only ever grew, so a
        // profile demoted once by a bad build stayed behind every alternative
        // for the life of the install, even after the reason was fixed.
        val rejected = prefs.getString("$scope:rejected", null)
            ?.split(',')
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() && it != key }
            ?.distinct()
            ?.joinToString(",")
            .orEmpty()
        prefs.edit()
            .putString("$scope:working", key)
            .putString("$scope:rejected", rejected)
            .apply()
    }

    /**
     * Move a profile behind candidates that have already proven stable.
     *
     * A SETUP response alone is not proof that a profile works: the receiver
     * can accept it and tear the stream down once real packets arrive. The
     * sender calls this only after that stronger failure condition.
     */
    fun recordRejected(profile: SenderAudioFormat) {
        val key = profile.key
        val rejected = prefs.getString("$scope:rejected", null)
            ?.split(',')
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?.toMutableList()
            ?: mutableListOf()
        rejected.removeAll { it == key }
        rejected += key
        prefs.edit()
            .putString("$scope:rejected", rejected.distinct().joinToString(","))
            .apply()
    }

    private companion object {
        /** One-time marker: demotions written by the old judging rule are void. */
        const val KEY_HISTORY_CLEARED = "history_cleared_1_3_45"
    }
}
