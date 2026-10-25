package com.carlink.cast

import android.app.Notification
import android.content.ComponentName
import android.content.Context
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSession
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.service.notification.StatusBarNotification
import android.service.notification.NotificationListenerService
import java.util.concurrent.ConcurrentHashMap

/**
 * Declaring this listener lets the user grant notification access, which is
 * required by MediaSessionManager before this app can read player metadata.
 */
internal object MediaSessionSnapshotStore {
    @Volatile private var appContext: Context? = null
    data class Snapshot(
        val packageName: String,
        val title: String,
        val artist: String,
        val album: String,
        val lyrics: String,
        val durationMs: Long,
        val positionMs: Long,
        val state: Int,
        val updatedAt: Long,
        val artwork: ByteArray? = null,
    )

    @Volatile var current: Snapshot? = null
    @Volatile var listenerConnected: Boolean = false

    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    fun update(controller: MediaController) {
        val metadata = controller.metadata
        val playback = controller.playbackState
        val now = android.os.SystemClock.elapsedRealtime()
        val playing = playback?.state == android.media.session.PlaybackState.STATE_PLAYING
        val base = playback?.position?.coerceAtLeast(0L) ?: 0L
        val position = if (playing && playback != null) {
            base + ((now - playback.lastPositionUpdateTime).coerceAtLeast(0) *
                playback.playbackSpeed.toDouble()).toLong()
        } else base
        current = Snapshot(
            packageName = controller.packageName,
            title = firstMetadataString(
                metadata,
                MediaMetadata.METADATA_KEY_TITLE,
                "android.media.metadata.DISPLAY_TITLE",
            ),
            artist = firstMetadataString(
                metadata,
                MediaMetadata.METADATA_KEY_ARTIST,
                MediaMetadata.METADATA_KEY_ALBUM_ARTIST,
                MediaMetadata.METADATA_KEY_AUTHOR,
            ),
            album = firstMetadataString(
                metadata,
                MediaMetadata.METADATA_KEY_ALBUM,
                MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE,
            ),
            lyrics = firstMetadataString(
                metadata,
                "android.media.metadata.LYRICS",
                "android.media.metadata.LYRIC",
                "ucar.media.metadata.LYRICS_WHOLE",
                "com.tencent.qqmusic.metadata.LYRICS",
            ).ifBlank {
                firstBundleString(
                    playback?.extras,
                    "android.media.metadata.LYRICS",
                    "android.media.metadata.LYRIC",
                    "ucar.media.metadata.LYRICS_WHOLE",
                    "com.tencent.qqmusic.metadata.LYRICS",
                )
            },
            durationMs = metadata?.getLong(MediaMetadata.METADATA_KEY_DURATION)?.coerceAtLeast(0L) ?: 0L,
            positionMs = position,
            state = when (playback?.state) {
                android.media.session.PlaybackState.STATE_PLAYING -> 1
                android.media.session.PlaybackState.STATE_PAUSED -> 2
                else -> 0
            },
            updatedAt = now,
            artwork = artworkBytes(metadata),
        )
    }

    fun updateNotification(sbn: StatusBarNotification) {
        val extras = sbn.notification.extras ?: return
        val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString().orEmpty()
        val artist = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString().orEmpty()
        val album = extras.getCharSequence(Notification.EXTRA_SUB_TEXT)?.toString().orEmpty()
        if (title.isBlank() && artist.isBlank()) return
        current = Snapshot(
            packageName = sbn.packageName,
            title = title,
            artist = artist,
            album = album,
            lyrics = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString().orEmpty(),
            durationMs = 0,
            positionMs = 0,
            state = 1,
            updatedAt = android.os.SystemClock.elapsedRealtime(),
            artwork = null,
        )
    }

    private fun artworkBytes(metadata: MediaMetadata?): ByteArray? {
        val bitmap = runCatching {
            metadata?.getBitmap(MediaMetadata.METADATA_KEY_ART)
                ?: metadata?.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)
        }.getOrNull() ?: artworkFromUri(metadata)
        if (bitmap == null) return null
        return runCatching {
            java.io.ByteArrayOutputStream().use { output ->
                bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 86, output)
                output.toByteArray().takeIf { it.isNotEmpty() && it.size <= 2 * 1024 * 1024 }
            }
        }.getOrNull()
    }

    private fun artworkFromUri(metadata: MediaMetadata?): android.graphics.Bitmap? {
        val value = runCatching {
            metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM_ART_URI)
                ?: metadata?.getString("android.media.metadata.ART_URI")
        }.getOrNull().orEmpty()
        if (value.isBlank()) return null
        return runCatching {
            appContext?.contentResolver
                ?.openInputStream(Uri.parse(value))
                ?.use { input -> BitmapFactory.decodeStream(input) }
        }.getOrNull()
    }

    private fun firstMetadataString(metadata: MediaMetadata?, vararg keys: String): String =
        keys.asSequence()
            .mapNotNull { key -> runCatching { metadata?.getString(key) }.getOrNull() }
            .firstOrNull { it.isNotBlank() }
            .orEmpty()

    private fun firstBundleString(bundle: android.os.Bundle?, vararg keys: String): String =
        keys.asSequence()
            .mapNotNull { key ->
                runCatching { bundle?.getCharSequence(key)?.toString() }.getOrNull()
                    ?: runCatching { bundle?.getString(key) }.getOrNull()
            }
            .firstOrNull { it.isNotBlank() }
            .orEmpty()
}

class MediaListenerService : NotificationListenerService() {
    private val controllers = ConcurrentHashMap<String, MediaController>()
    private val callbacks = ConcurrentHashMap<String, MediaController.Callback>()

    override fun onListenerConnected() {
        super.onListenerConnected()
        MediaSessionSnapshotStore.initialize(this)
        MediaSessionSnapshotStore.listenerConnected = true
        val manager = getSystemService(android.media.session.MediaSessionManager::class.java)
        val active = runCatching {
            manager?.getActiveSessions(ComponentName(this, MediaListenerService::class.java)).orEmpty()
        }.getOrDefault(emptyList())
        active.forEach(::attach)
    }

    override fun onListenerDisconnected() {
        MediaSessionSnapshotStore.listenerConnected = false
        MediaSessionSnapshotStore.current = null
        if (Build.VERSION.SDK_INT >= 24) {
            runCatching {
                requestRebind(ComponentName(this, MediaListenerService::class.java))
            }
        }
        super.onListenerDisconnected()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        sbn ?: return
        MediaSessionSnapshotStore.initialize(this)
        val token = mediaToken(sbn.notification)
        if (token != null) {
            val controller = runCatching { MediaController(this, token) }.getOrNull()
            if (controller != null) {
                attach(controller)
                return
            }
        }
        MediaSessionSnapshotStore.updateNotification(sbn)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        sbn ?: return
        callbacks.remove(sbn.packageName)?.let { callback ->
            controllers.remove(sbn.packageName)?.unregisterCallback(callback)
        }
    }

    override fun onDestroy() {
        MediaSessionSnapshotStore.listenerConnected = false
        MediaSessionSnapshotStore.current = null
        callbacks.forEach { (packageName, callback) ->
            controllers[packageName]?.unregisterCallback(callback)
        }
        callbacks.clear()
        controllers.clear()
        super.onDestroy()
    }

    private fun attach(controller: MediaController) {
        val key = controller.packageName
        callbacks.remove(key)?.let { old -> controllers[key]?.unregisterCallback(old) }
        val callback = object : MediaController.Callback() {
            override fun onMetadataChanged(metadata: MediaMetadata?) {
                MediaSessionSnapshotStore.update(controller)
            }

            override fun onPlaybackStateChanged(state: android.media.session.PlaybackState?) {
                MediaSessionSnapshotStore.update(controller)
            }
        }
        controllers[key] = controller
        callbacks[key] = callback
        runCatching { controller.registerCallback(callback) }
        MediaSessionSnapshotStore.update(controller)
    }

    private fun mediaToken(notification: Notification): MediaSession.Token? {
        val extras = notification.extras ?: return null
        return if (Build.VERSION.SDK_INT >= 33) {
            extras.getParcelable(Notification.EXTRA_MEDIA_SESSION, MediaSession.Token::class.java)
        } else {
            @Suppress("DEPRECATION")
            extras.getParcelable(Notification.EXTRA_MEDIA_SESSION)
        }
    }
}
