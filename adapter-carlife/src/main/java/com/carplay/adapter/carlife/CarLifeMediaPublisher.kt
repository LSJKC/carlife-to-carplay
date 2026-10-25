package com.carplay.adapter.carlife

import android.content.Context
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.net.Uri
import com.baidu.carlife.mixing.MusicInfo
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

data class CarLifeMediaSnapshot(
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val lyrics: String = "",
    val durationMs: Long = 0L,
    val positionMs: Long = 0L,
    val playing: Boolean = false,
    val packageName: String = "",
    val artwork: Bitmap? = null,
    val artworkBytes: ByteArray? = null,
    // 增强的应用上下文信息（对标 iPhone 实机 Endpoint）
    val applicationPid: Int = 0,
    val applicationName: String = "",
    val audioSessionId: Int = 0,
)

/**
 * 媒体应用的完整上下文信息，对标 iPhone 实机的 NowPlaying Endpoint
 *
 * iPhone 实机格式：【 LOCL (iPhone) ❯ com.package (PID) AppName ❯ audio_category 】
 * Android 目标格式：【 LOCAL (Android) ❯ com.package (PID) AppName ❯ audio_category 】
 */
internal data class MediaApplicationContext(
    val packageName: String = "",
    val pid: Int = 0,
    val appName: String = "",
    val audioCategory: String = "default",
) {
    override fun toString(): String {
        return "【 LOCAL (Android) ❯ $packageName ($pid) $appName ❯ $audioCategory 】"
    }
}

/**
 * Publishes one shared Android MediaSession snapshot through both CarLife
 * metadata paths:
 *
 * 1. the vendor host callback (MusicInfo + progress), and
 * 2. Honor's `carlife_vehicle_music_*` broadcast consumed by MusicInfoSender.
 *
 * The broadcast is what carries the album artwork and lyric line; the AIDL
 * callback is kept because some CarLife builds only update their card from it.
 */
internal class CarLifeMediaPublisher(
    private val context: Context,
    private val log: (String) -> Unit,
    private val fallback: (() -> CarLifeMediaSnapshot?)? = null,
) : AutoCloseable {
    private val app = context.applicationContext
    private val sessions = app.getSystemService(MediaSessionManager::class.java)
    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor {
        Thread(it, "carlife-media-publisher").apply { isDaemon = true }
    }
    private val artworkExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor {
        Thread(it, "carlife-artwork-cache").apply { isDaemon = true }
    }
    @Volatile private var closed = false
    @Volatile private var started = false
    private var lastKey: String? = null
    private var lastPosition = Long.MIN_VALUE
    private var lastInfoSentAt = 0L
    private var fallbackArtworkHash: Int? = null
    private var fallbackArtworkBitmap: Bitmap? = null
    @Volatile private var cachedArtworkUri: String? = null
    @Volatile private var cachedArtworkBitmap: Bitmap? = null
    @Volatile private var artworkDecodePending = false
    private var emptySessionLogged = false
    private val nowPlayingEmptyLogged = java.util.concurrent.atomic.AtomicBoolean()

    // 诊断统计
    private var pollCount = 0L
    private var lastLoggedEndpoint: String? = null

    fun start() {
        if (closed || started) return
        synchronized(this) {
            if (closed || started) return
            started = true
            pollCount = 0
            log("[媒体] CarLife 媒体发布器启动: poll_interval=1s")
            runCatching {
                executor.scheduleWithFixedDelay(::publish, 0, 1, TimeUnit.SECONDS)
            }.onFailure {
                started = false
                if (!closed) log("[媒体] CarLife 媒体发布器启动失败：${it.message}")
            }
        }
    }

    private fun publish() {
        if (closed) return
        val active = runCatching {
            val component = ComponentName(app, "com.carlink.cast.MediaListenerService")
            sessions?.getActiveSessions(component).orEmpty().ifEmpty {
                sessions?.getActiveSessions(null).orEmpty()
            }
        }.getOrElse {
            log("CarLife 媒体读取失败：${it.message ?: it.javaClass.simpleName}")
            emptyList()
        }
        if (active.isEmpty()) {
            if (!emptySessionLogged) {
                emptySessionLogged = true
                log("CarLife 媒体没有可读 MediaSession；请开启通知使用权并开始播放")
            }
        } else {
            emptySessionLogged = false
        }
        val controller = chooseController(active)
        val metadata = controller?.metadata
        val playback = controller?.playbackState
        val fallbackSnapshot = fallback?.invoke()
        val title = firstString(
            metadata,
            MediaMetadata.METADATA_KEY_TITLE,
            MediaMetadata.METADATA_KEY_DISPLAY_TITLE,
        ).ifBlank { fallbackSnapshot?.title.orEmpty() }
        val artist = firstString(
            metadata,
            MediaMetadata.METADATA_KEY_ARTIST,
            MediaMetadata.METADATA_KEY_ALBUM_ARTIST,
            MediaMetadata.METADATA_KEY_AUTHOR,
        ).ifBlank { fallbackSnapshot?.artist.orEmpty() }
        val album = firstString(
            metadata,
            MediaMetadata.METADATA_KEY_ALBUM,
            MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE,
        ).ifBlank { fallbackSnapshot?.album.orEmpty() }
        val lyrics = firstString(
            metadata,
            "android.media.metadata.LYRICS",
            "android.media.metadata.LYRIC",
            "ucar.media.metadata.LYRICS_WHOLE",
            "com.tencent.qqmusic.metadata.LYRICS",
        ).ifBlank { fallbackSnapshot?.lyrics.orEmpty() }
        val duration = metadata?.getLong(MediaMetadata.METADATA_KEY_DURATION)
            ?.coerceAtLeast(0L)
            ?.takeIf { it > 0L }
            ?: fallbackSnapshot?.durationMs?.coerceAtLeast(0L)
            ?: 0L
        val playing = playback?.state == PlaybackState.STATE_PLAYING ||
            (playback == null && fallbackSnapshot?.playing == true)
        val basePosition = playback?.position?.coerceAtLeast(0L)
            ?: fallbackSnapshot?.positionMs?.coerceAtLeast(0L)
            ?: 0L
        val position = if (playing && playback != null) {
            (basePosition + (
                (android.os.SystemClock.elapsedRealtime() - playback.lastPositionUpdateTime)
                    .coerceAtLeast(0) * playback.playbackSpeed.toDouble()
                ).toLong()).coerceAtLeast(0L)
        } else basePosition
        val boundedPosition = if (duration > 0) position.coerceAtMost(duration) else position
        val source = controller?.packageName ?: fallbackSnapshot?.packageName.orEmpty()
        val cover = artwork(metadata) ?: fallbackArtwork(fallbackSnapshot)
        /*
         * MediaSessionManager can briefly return a controller whose metadata is
         * null or empty while the player is switching tracks, while notification
         * access is reconnecting, or while CarLife is still starting. Sending
         * that empty snapshot replaces a valid HU card with "loading" every
         * retry interval. Keep the last valid card on the head unit until a
         * meaningful snapshot is available.
         */
        val hasMetadata = title.isNotBlank() || artist.isNotBlank() || album.isNotBlank() ||
            lyrics.isNotBlank() || duration > 0L || cover != null
        if (!hasMetadata) {
            if (nowPlayingEmptyLogged.compareAndSet(false, true)) {
                log("CarLife 暂无可发布媒体元数据，跳过空快照，避免覆盖车机已有媒体卡")
            }
            return
        }
        nowPlayingEmptyLogged.set(false)
        // getBitmap() may return a new parcelled Bitmap instance every poll,
        // so generationId/object identity is not stable. Dimensions alone are
        // also insufficient: two tracks commonly use the same cover size.
        // Hash the pixels so same-sized artwork changes are published too.
        val coverKey = cover?.let(::artworkKey).orEmpty()
        val key = "$source\u0000$title\u0000$artist\u0000$album\u0000$duration\u0000$lyrics\u0000$coverKey"

        /*
         * The first broadcast often races CarLife's own connect service:
         * MusicInfoSender drops it while isCarlifeConnected() is still false.
         * Re-send unchanged metadata at a low rate so the media card can
         * recover without flooding the iAP2/control path.
         */
        val now = android.os.SystemClock.elapsedRealtime()
        val infoChanged = key != lastKey

        // 增强的诊断日志（对标 iPhone tracev3）
        pollCount++
        if (infoChanged) {
            val appContext = extractApplicationContext(source, controller)
            if (lastLoggedEndpoint != appContext.toString()) {
                lastLoggedEndpoint = appContext.toString()
                log("[媒体] 轮询周期 #$pollCount - 媒体变化")
                log("  [Endpoint] $appContext")
            }
        }
        if (infoChanged || now - lastInfoSentAt >= INFO_RETRY_INTERVAL_MS) {
            lastKey = key
            lastInfoSentAt = now
            val info = MusicInfo(title, artist, source, duration, 0, 0)
            CarLifeBridge.publishMusic(info)
            val intent = Intent(ACTION_MUSIC_INFO).setPackage(CARLIFE_PACKAGE)
                .putExtra("song", title)
                .putExtra("artist", artist)
                .putExtra("album", album)
                .putExtra("duration", duration.coerceAtMost(Int.MAX_VALUE.toLong()).toInt())
                .putExtra("index", 0)
                .putExtra("listnum", 0)
                .putExtra("mode", 0)
                .putExtra("mediaUrl", source)
                .putExtra("mediaLyric", lyrics)
                .putExtra("coverUrl", "")
            cover?.let { intent.putExtra("cover", it) }
            runCatching { app.sendBroadcast(intent) }
                .onFailure { log("CarLife 媒体信息广播失败：${it.message}") }
            // 增强的诊断日志
            log(
                "  [Metadata] title=${title.ifBlank { "<空>" }} " +
                    "artist=${artist.ifBlank { "<空>" }} album=${album.ifBlank { "<空>" }} " +
                    "duration=${duration}ms"
            )
            log(
                "  [Playback] playing=$playing position=$boundedPosition ms" +
                    " lyrics=${lyrics.isNotBlank()} cover=${cover != null}"
            )
            log("  [Status] 发送 MusicInfo via AIDL + Intent via 广播")
        }

        if (boundedPosition != lastPosition || infoChanged) {
            lastPosition = boundedPosition
            CarLifeBridge.publishMusicProgress(boundedPosition, lyrics)
            val progress = Intent(ACTION_MUSIC_PROGRESS).setPackage(CARLIFE_PACKAGE)
                .putExtra("duration", duration.coerceAtMost(Int.MAX_VALUE.toLong()).toInt())
                .putExtra("curTime", boundedPosition.coerceAtMost(Int.MAX_VALUE.toLong()).toInt())
                .putExtra("mediaLyric", lyrics)
            runCatching { app.sendBroadcast(progress) }
                .onFailure { log("CarLife 媒体进度广播失败：${it.message}") }
        }
    }

    private fun artwork(metadata: MediaMetadata?): Bitmap? {
        val source = runCatching {
            metadata?.getBitmap(MediaMetadata.METADATA_KEY_ART)
                ?: metadata?.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)
                ?: artworkFromUri(metadata)
        }.getOrNull() ?: return null
        if (source.width <= 200 && source.height <= 200) return source
        return runCatching { Bitmap.createScaledBitmap(source, 200, 200, true) }.getOrNull()
    }

    private fun artworkFromUri(metadata: MediaMetadata?): Bitmap? {
        val value = runCatching {
            metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM_ART_URI)
                ?: metadata?.getString("android.media.metadata.ART_URI")
        }.getOrNull().orEmpty()
        if (value.isBlank()) return null
        cachedArtworkBitmap?.let { if (cachedArtworkUri == value && !it.isRecycled) return it }
        if (cachedArtworkUri != value) {
            cachedArtworkUri = value
            cachedArtworkBitmap = null
        }
        if (!artworkDecodePending) {
            artworkDecodePending = true
            runCatching {
                artworkExecutor.execute {
                    val decoded = runCatching {
                        app.contentResolver.openInputStream(Uri.parse(value))
                            ?.use(BitmapFactory::decodeStream)
                    }.getOrNull()
                    val old = cachedArtworkBitmap
                    if (cachedArtworkUri == value) {
                        cachedArtworkBitmap = decoded
                        if (old != null && old !== decoded) runCatching { old.recycle() }
                    } else {
                        decoded?.let { runCatching { it.recycle() } }
                    }
                    artworkDecodePending = false
                }
            }.onFailure {
                artworkDecodePending = false
                log("CarLife 封面 URI 异步读取失败：${it.message ?: it.javaClass.simpleName}")
            }
        }
        return null
    }

    private fun artworkKey(bitmap: Bitmap): String = runCatching {
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var hash = 1
        pixels.forEach { hash = 31 * hash + it }
        "${bitmap.width}x${bitmap.height}:$hash"
    }.getOrDefault("${bitmap.width}x${bitmap.height}")

    private fun firstString(metadata: MediaMetadata?, vararg keys: String): String =
        keys.asSequence()
            .mapNotNull { key -> runCatching { metadata?.getString(key) }.getOrNull() }
            .firstOrNull { it.isNotBlank() }
            .orEmpty()

    private fun chooseController(active: List<MediaController>): MediaController? {
        val playing = active.filter { it.playbackState?.state == PlaybackState.STATE_PLAYING }
        return playing.firstOrNull { hasUsableMetadata(it.metadata) }
            ?: active.firstOrNull { hasUsableMetadata(it.metadata) }
            ?: playing.firstOrNull()
            ?: active.firstOrNull()
    }

    private fun hasUsableMetadata(metadata: MediaMetadata?): Boolean {
        if (metadata == null) return false
        val text = listOf(
            MediaMetadata.METADATA_KEY_TITLE,
            MediaMetadata.METADATA_KEY_DISPLAY_TITLE,
            MediaMetadata.METADATA_KEY_ARTIST,
            MediaMetadata.METADATA_KEY_ALBUM,
            MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE,
        ).any { key -> runCatching { metadata.getString(key).orEmpty().isNotBlank() }.getOrDefault(false) }
        val artwork = runCatching {
            metadata.getBitmap(MediaMetadata.METADATA_KEY_ART)
                ?: metadata.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)
                ?: artworkFromUri(metadata)
        }.getOrNull()
        return text || artwork != null
    }

    private fun fallbackArtwork(snapshot: CarLifeMediaSnapshot?): Bitmap? {
        snapshot?.artwork?.let { return it }
        val bytes = snapshot?.artworkBytes ?: return null
        val hash = bytes.contentHashCode()
        if (hash == fallbackArtworkHash && fallbackArtworkBitmap?.isRecycled == false) {
            return fallbackArtworkBitmap
        }
        runCatching { fallbackArtworkBitmap?.recycle() }
        fallbackArtworkHash = hash
        fallbackArtworkBitmap = runCatching {
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)?.let { source ->
                if (source.width <= 200 && source.height <= 200) source
                else Bitmap.createScaledBitmap(source, 200, 200, true)
            }
        }.getOrNull()
        return fallbackArtworkBitmap
    }

    /**
     * 提取媒体应用的完整上下文信息，对标 iPhone 实机 NowPlaying Endpoint
     * 格式：【 LOCAL (Android) ❯ com.package (PID) AppName ❯ audio_category 】
     */
    private fun extractApplicationContext(packageName: String, controller: MediaController?): MediaApplicationContext {
        val pid = getApplicationPid(packageName)
        val appName = getApplicationName(packageName)
        val audioCategory = "playback"  // Android 上通常使用 playback

        return MediaApplicationContext(
            packageName = packageName,
            pid = pid,
            appName = appName,
            audioCategory = audioCategory
        )
    }

    /**
     * 通过 ActivityManager 获取应用的进程 ID
     */
    private fun getApplicationPid(packageName: String): Int {
        return runCatching {
            val am = app.getSystemService(android.app.ActivityManager::class.java) ?: return@runCatching 0
            val processes = am.runningAppProcesses ?: return@runCatching 0
            processes.find { it.processName == packageName }?.pid ?: 0
        }.getOrDefault(0)
    }

    /**
     * 通过 PackageManager 获取应用的显示名称
     */
    private fun getApplicationName(packageName: String): String {
        return runCatching {
            val pm = app.packageManager
            pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0)).toString()
        }.getOrDefault(packageName)
    }

    override fun close() {
        synchronized(this) {
            if (closed) return
            closed = true
        }
        executor.shutdownNow()
        artworkExecutor.shutdownNow()
        runCatching { cachedArtworkBitmap?.recycle() }
        cachedArtworkBitmap = null
        cachedArtworkUri = null
        runCatching { fallbackArtworkBitmap?.recycle() }
        fallbackArtworkBitmap = null
        fallbackArtworkHash = null
    }

    private companion object {
        const val CARLIFE_PACKAGE = "com.baidu.carlife.honor"
        const val ACTION_MUSIC_INFO = "carlife_vehicle_music_info"
        const val ACTION_MUSIC_PROGRESS = "carlife_vehicle_music_progress"
        const val INFO_RETRY_INTERVAL_MS = 5_000L
    }
}
