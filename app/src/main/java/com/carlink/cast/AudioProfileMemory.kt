package com.carlink.cast

import android.content.Context
import com.carplay.sender.SenderAudioFormat

/**
 * Remembers the audio profile that a specific head unit accepted.
 *
 * The Buick-E5 project uses the same idea for receivers whose advertised
 * format list contains a profile that can SETUP successfully but later tears
 * down the session.  This store only reorders profiles; it never invents a
 * format that was not advertised by the current `/info` response.
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

    fun order(candidates: List<SenderAudioFormat>): List<SenderAudioFormat> {
        if (candidates.isEmpty()) return emptyList()
        val working = prefs.getString("$scope:working", null)
        val rejected = prefs.getString("$scope:rejected", null)
            ?.split(',')
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?.toSet()
            .orEmpty()
        val preferred = candidates.firstOrNull { it.key == working }
        val rest = candidates.filter { it.key != preferred?.key }
        val safe = rest.filter { it.key !in rejected }
        val risky = rest.filter { it.key in rejected }
        return (listOfNotNull(preferred) + safe + risky).distinctBy { it.key }
    }

    fun recordWorking(profile: SenderAudioFormat) {
        val key = profile.key
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
}
