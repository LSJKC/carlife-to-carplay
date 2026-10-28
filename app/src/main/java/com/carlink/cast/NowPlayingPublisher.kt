package com.carlink.cast

import android.content.Context
import android.content.ComponentName
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.net.Uri
import com.carplay.iap2.body.Iap2BodyReader
import com.carplay.iap2.message.Iap2Messages
import com.carplay.iap2.message.Iap2ControlMessages
import com.carplay.iap2.wire.Iap2Frame
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import java.io.ByteArrayOutputStream

/**
 * Publishes the Now Playing fields requested by the head unit.
 *
 * The request is a subscription: 0x5000 names the fields, and 0x5001 is then
 * sent again when the media session changes. Keeping this separate from either
 * Bluetooth or Wi-Fi makes both halves expose the same metadata contract.
 */
internal class NowPlayingPublisher(
    context: Context,
    private val log: (String) -> Unit,
) : AutoCloseable {

    private val appContext = context.applicationContext
    private val manager = appContext.getSystemService(MediaSessionManager::class.java)
    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor {
        Thread(it, "carlink-now-playing").apply { isDaemon = true }
    }
    private val sendLock = Any()
    @Volatile private var request: Request? = null
    @Volatile private var send: ((Iap2Frame) -> Unit)? = null
    @Volatile private var sendFileTransfer: ((ByteArray) -> Boolean)? = null
    @Volatile private var subscriptionToken = 0L
    private val nextToken = AtomicLong()
    @Volatile private var closed = false
    @Volatile private var permissionLogged = false
    @Volatile private var emptySessionLogged = false
    @Volatile private var periodic: ScheduledFuture<*>? = null
    private var lastSessionDiagnostic: String? = null
    private val artworkLock = Any()
    private var nextArtworkId = 1
    private var artworkFingerprint = 0L
    private var artworkTransferId: Int? = null
    private var pendingArtwork: PendingArtwork? = null
    private val emptySnapshotLogged = java.util.concurrent.atomic.AtomicBoolean()

    private data class PendingArtwork(
        val id: Int,
        val fingerprint: Long,
        val bytes: ByteArray,
    )

    data class Request(
        val media: Set<Int>,
        val playback: Set<Int>,
    )

    data class Snapshot(
        val title: String = "",
        val artist: String = "",
        val album: String = "",
        val lyrics: String = "",
        val app: String = "",
        val durationMs: Long = 0,
        val positionMs: Long = 0,
        val state: Int = 0,
        val artwork: ByteArray? = null,
        val artworkFingerprint: Long = 0,
    )

    /**
     * Installs/replaces a subscription and immediately sends the first update.
     */
    fun subscribe(
        frame: Iap2Frame,
        sender: (Iap2Frame) -> Unit,
        fileSender: ((ByteArray) -> Boolean)? = null,
    ): Long {
        val parsed = runCatching { parseRequest(frame) }.getOrElse {
            log("NowPlaying 订阅解析失败：${it.message}")
            return 0L
        }
        val token = nextToken.incrementAndGet()
        subscriptionToken = token
        request = parsed
        send = sender
        sendFileTransfer = fileSender
        synchronized(artworkLock) {
            artworkFingerprint = 0L
            artworkTransferId = null
            pendingArtwork = null
        }
        publish()
        log(
            "NowPlaying 订阅#$token：media=${parsed.media.sorted()} " +
                "playback=${parsed.playback.sorted()} " +
                "封面字段=${if (0x1a in parsed.media) "请求" else "未请求"}",
        )
        if (executor.isShutdown) return token
        if (periodic == null) {
            periodic = executor.scheduleWithFixedDelay(::publish, 2, 2, TimeUnit.SECONDS)
        }
        return token
    }

    fun unsubscribe(token: Long? = null) {
        if (token != null && token != subscriptionToken) {
            log("NowPlaying 忽略旧订阅#$token 的停止请求，当前订阅#$subscriptionToken 仍有效")
            return
        }
        request = null
        send = null
        sendFileTransfer = null
        synchronized(artworkLock) {
            pendingArtwork = null
            artworkTransferId = null
        }
    }

    fun hasSubscription(): Boolean = request != null && send != null

    /**
     * Some GM firmware advertises NowPlaying but delays 0x5000 until the
     * driver's media page is already visible.  Keep a local copy of the
     * standard request and publish 0x5001 without sending a reverse-direction
     * 0x5000 frame.  A real car request still replaces this immediately.
     */
    fun subscribeDefault(
        sender: (Iap2Frame) -> Unit,
        fileSender: ((ByteArray) -> Boolean)? = null,
    ): Long {
        val frame = Iap2ControlMessages.subscriptions().first()
        val token = subscribe(frame, sender, fileSender)
        log("NowPlaying 未收到车机订阅，启用默认媒体字段发布")
        return token
    }

    fun parseRequest(frame: Iap2Frame): Request {
        require(frame.messageId == 0x5000) {
            "Expected StartNowPlayingUpdates, got 0x${frame.messageId.toString(16)}"
        }
        val body = Iap2Messages.reader(frame)
        fun fields(groupId: Int): Set<Int> =
            body.optionalGroup(groupId)?.list()?.map { it.id }?.toSet().orEmpty()
        return Request(fields(0), fields(1))
    }

    fun encode(snapshot: Snapshot, request: Request, artworkId: Int? = null): Iap2Frame =
        Iap2Messages.buildRaw(0x5001) {
            if (request.media.isNotEmpty()) {
                group(0) {
                    // CarPlay field 26 is ArtworkFileTransferIdentifier, not
                    // lyric text. Sending a string there makes the HU leave
                    // the media card in "loading" even though audio is alive.
                    if (0 in request.media) bytes(0, persistentId(snapshot))
                    if (1 in request.media) string(1, snapshot.title)
                    if (4 in request.media) u32(4, snapshot.durationMs.coerceIn(0, 0xffff_ffffL))
                    if (6 in request.media) string(6, snapshot.album)
                    if (12 in request.media) string(12, snapshot.artist)
                    if (26 in request.media) u8(26, artworkId ?: ARTWORK_NONE)
                }
            }
            if (request.playback.isNotEmpty()) {
                group(1) {
                    /*
                     * Field ids below 12 are from the 对照实现 sender's
                     * NowPlaying encoder (`Tg.b()`), which is the one this car
                     * answers to:
                     *
                     *     0  player state
                     *     1  position (u32)
                     *     2  queue index
                     *     3  queue length
                     *     7  app name
                     *     12 volume (0..65535)
                     *     13, 14  zero
                     *     16 title
                     *
                     * Fields 2/3 are sent as 0/1 rather than omitted: a
                     * receiver that renders "n of m" treats a missing index as
                     * "unknown track" and leaves the card unpopulated.
                     */
                    if (0 in request.playback) u8(0, snapshot.state.coerceIn(0, 2))
                    if (1 in request.playback) u32(1, snapshot.positionMs.coerceIn(0, 0xffff_ffffL))
                    if (2 in request.playback) u32(2, 0)
                    if (3 in request.playback) u32(3, 1)
                    if (7 in request.playback) string(7, snapshot.app)
                    if (12 in request.playback) u16(12, volumeField())
                    if (13 in request.playback) u32(13, 0)
                    if (14 in request.playback) u32(14, 0)
                    if (16 in request.playback) string(16, snapshot.app)
                }
            }
        }

    /**
     * Volume as the receiver expects it: 0..65535, 0xFFFF = full scale.
     *
     * Read from the media stream itself so the car's card matches the phone's
     * slider. A session with no readable stream reports full scale rather than
     * zero — reporting 0 makes the car render a muted card.
     */
    private fun volumeField(): Int {
        val stream = runCatching {
            appContext.getSystemService(android.media.AudioManager::class.java)
                ?.getStreamVolume(android.media.AudioManager.STREAM_MUSIC)
        }.getOrNull() ?: return 0xffff
        val max = runCatching {
            appContext.getSystemService(android.media.AudioManager::class.java)
                ?.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC)
        }.getOrNull() ?: return 0xffff
        if (max <= 0) return 0xffff
        return ((stream.toLong() * 0xffffL) / max).toInt().coerceIn(0, 0xffff)
    }

    private fun persistentId(snapshot: Snapshot): ByteArray {
        var hash = FNV_OFFSET
        val value = "${snapshot.app}\u0000${snapshot.title}\u0000${snapshot.artist}\u0000${snapshot.album}"
        value.toByteArray(Charsets.UTF_8).forEach { byte ->
            hash = (hash xor (byte.toLong() and 0xffL)) * FNV_PRIME
        }
        if (hash == 0L) hash = 1L
        return ByteArray(8) { index ->
            (hash ushr ((7 - index) * 8)).toByte()
        }
    }

    private fun publish() {
        if (closed) return
        val currentRequest = request ?: return
        val target = send ?: return
        val snapshot = readSnapshot()
        /*
         * During MediaSession handoff Android can briefly expose a controller
         * with no metadata. Sending an empty 0x5001 at that point makes the HU
         * replace a valid card with its permanent "loading" state. Keep the
         * last meaningful card until the next snapshot contains actual media.
         */
        val hasMetadata = snapshot.title.isNotBlank() ||
            snapshot.artist.isNotBlank() ||
            snapshot.album.isNotBlank() ||
            snapshot.lyrics.isNotBlank() ||
            snapshot.durationMs > 0L ||
            snapshot.artwork != null
        if (!hasMetadata) {
            if (emptySnapshotLogged.compareAndSet(false, true)) {
                log("NowPlaying 暂无可发布媒体元数据，跳过空 0x5001，避免覆盖车机已有媒体卡")
            }
            return
        }
        emptySnapshotLogged.set(false)
        maybeStartArtwork(snapshot)
        val currentArtworkId = synchronized(artworkLock) { artworkTransferId }
        val frame = runCatching { encode(snapshot, currentRequest, currentArtworkId) }.getOrElse {
            log("NowPlaying 编码失败：${it.message}")
            return
        }
        runCatching {
            synchronized(sendLock) { target(frame) }
        }.onFailure { log("NowPlaying 发送失败：${it.message}") }
        log(
            "NowPlaying 0x5001：title=${snapshot.title.logValue()} " +
                "artist=${snapshot.artist.logValue()} " +
                "album=${snapshot.album.logValue()} " +
                "lyrics=${snapshot.lyrics.logValue()} " +
                "app=${snapshot.app.logValue()} duration=${snapshot.durationMs} " +
                "art=${currentArtworkId ?: ARTWORK_NONE} artBytes=${snapshot.artwork?.size ?: 0} " +
                "position=${snapshot.positionMs} state=${snapshot.state}",
        )
    }

    private fun String.logValue(): String =
        if (isBlank()) "<空>" else replace("\n", "\\n").take(120)

    private fun readSnapshot(): Snapshot {
        val fallback = MediaSessionSnapshotStore.current
        val active = try {
            val component = ComponentName(appContext, MediaListenerService::class.java)
            manager?.getActiveSessions(component).orEmpty().ifEmpty {
                manager?.getActiveSessions(null).orEmpty()
            }
        } catch (failure: SecurityException) {
            if (!permissionLogged) {
                permissionLogged = true
                log("NowPlaying 无法读取 MediaSession（未授予通知使用权）：${failure.message}")
            }
            emptyList()
        }
        if (active.isEmpty() && !emptySessionLogged) {
            emptySessionLogged = true
            log(
                "NowPlaying 没有可读的 MediaSession；" +
                    "请确认已授予 CarLink Cast 通知使用权，且播放器已建立媒体会话",
            )
        } else if (active.isNotEmpty()) {
            emptySessionLogged = false
        }
        val controller = chooseController(active)
        if (controller == null) {
            if (fallback != null) {
                if (fallback.updatedAt > (lastFallbackUpdateAt ?: Long.MIN_VALUE)) {
                    lastFallbackUpdateAt = fallback.updatedAt
                    log("NowPlaying 使用通知监听回退：${fallback.packageName}")
                }
                return Snapshot(
                    title = fallback.title,
                    artist = fallback.artist,
                    album = fallback.album,
                    lyrics = fallback.lyrics,
                    durationMs = fallback.durationMs,
                    positionMs = fallback.positionMs,
                    state = fallback.state,
                    app = fallback.packageName,
                    artwork = fallback.artwork,
                    artworkFingerprint = fallback.artwork?.let(::fingerprint) ?: 0L,
                )
            }
        }
        val metadata = controller?.metadata
        val playback = controller?.playbackState
        val sessionDiagnostic = buildString {
            append("NowPlaying MediaSession：总数=${active.size}")
            if (controller == null) {
                append("，未选中")
            } else {
                append("，选中=${controller.packageName}")
                append("，state=${playback?.state ?: 0}")
                append("，metadata=${metadata != null}")
                append("，title=${firstMetadataString(metadata, MediaMetadata.METADATA_KEY_TITLE, MediaMetadata.METADATA_KEY_DISPLAY_TITLE).isNotBlank()}")
                append("，artist=${firstMetadataString(metadata, MediaMetadata.METADATA_KEY_ARTIST, MediaMetadata.METADATA_KEY_ALBUM_ARTIST, MediaMetadata.METADATA_KEY_AUTHOR).isNotBlank()}")
                append("，album=${firstMetadataString(metadata, MediaMetadata.METADATA_KEY_ALBUM, MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE).isNotBlank()}")
                append("，lyrics=${readLyrics(metadata, playback).isNotBlank()}")
            }
        }
        if (sessionDiagnostic != lastSessionDiagnostic) {
            lastSessionDiagnostic = sessionDiagnostic
            log(sessionDiagnostic)
        }
        val playing = playback?.state == PlaybackState.STATE_PLAYING ||
            (playback == null && fallback?.state == 1)
        val duration = metadata?.getLong(MediaMetadata.METADATA_KEY_DURATION)?.coerceAtLeast(0) ?: 0
        val basePosition = playback?.position?.coerceAtLeast(0) ?: 0
        val position = if (playing && playback != null) {
            val elapsed = (android.os.SystemClock.elapsedRealtime() - playback.lastPositionUpdateTime)
                .coerceAtLeast(0)
            (basePosition + elapsed * playback.playbackSpeed.toDouble()).toLong().coerceAtLeast(0)
        } else {
            basePosition
        }.let { if (duration > 0) it.coerceAtMost(duration) else it }
        val artwork = artworkBytes(metadata) ?: fallback?.artwork
        return Snapshot(
            title = firstMetadataString(
                metadata,
                MediaMetadata.METADATA_KEY_TITLE,
                MediaMetadata.METADATA_KEY_DISPLAY_TITLE,
            ).ifBlank { fallback?.title.orEmpty() },
            artist = firstMetadataString(
                metadata,
                MediaMetadata.METADATA_KEY_ARTIST,
                MediaMetadata.METADATA_KEY_ALBUM_ARTIST,
                MediaMetadata.METADATA_KEY_AUTHOR,
            ).ifBlank { fallback?.artist.orEmpty() },
            album = firstMetadataString(
                metadata,
                MediaMetadata.METADATA_KEY_ALBUM,
                MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE,
            ).ifBlank { fallback?.album.orEmpty() },
            lyrics = readLyrics(metadata, playback).ifBlank { fallback?.lyrics.orEmpty() },
            app = controller?.packageName.orEmpty().ifBlank { fallback?.packageName.orEmpty() },
            durationMs = if (duration > 0) duration else fallback?.durationMs ?: 0,
            positionMs = if (playback != null) position else fallback?.positionMs ?: 0,
            state = when (playback?.state) {
                PlaybackState.STATE_PLAYING -> 1
                PlaybackState.STATE_PAUSED -> 2
                else -> fallback?.state ?: 0
            },
            artwork = artwork,
            artworkFingerprint = artwork?.let(::fingerprint) ?: 0L,
        )
    }

    /**
     * Sends the iAP2 v2 artwork transfer after the head unit asks for START.
     * The setup itself is announced by field 26 in the following 0x5001.
     */
    fun onFileTransferDatagram(datagram: ByteArray) {
        if (datagram.size < 2) return
        val id = datagram[0].toInt() and 0xff
        val opcode = datagram[1].toInt() and 0x0f
        val pending = synchronized(artworkLock) {
            val value = pendingArtwork
            if (value == null || value.id != id) return
            when (opcode) {
                OP_START -> value
                OP_SUCCESS -> {
                    pendingArtwork = null
                    value
                }
                OP_CANCEL -> {
                    pendingArtwork = null
                    artworkTransferId = null
                    artworkFingerprint = 0L
                    null
                }
                else -> null
            }
        }
        when (opcode) {
            OP_START -> pending?.let { sendArtworkChunks(it) }
            OP_SUCCESS -> log("NowPlaying 封面传输完成：id=$id")
            OP_CANCEL -> log("NowPlaying 封面被车机取消：id=$id")
        }
    }

    private fun maybeStartArtwork(snapshot: Snapshot) {
        val currentRequest = request ?: return
        if (26 !in currentRequest.media) return
        val sender = sendFileTransfer ?: return
        val bytes = snapshot.artwork
        val fingerprint = snapshot.artworkFingerprint
        synchronized(artworkLock) {
            if (fingerprint == artworkFingerprint && (fingerprint == 0L || artworkTransferId != null)) return
            if (fingerprint == 0L) {
                pendingArtwork = null
                artworkTransferId = null
                artworkFingerprint = 0L
                return
            }
            val id = nextArtworkId.coerceIn(1, 0x7f)
            nextArtworkId = if (id >= 0x7f) 1 else id + 1
            val setup = ByteArray(12)
            setup[0] = id.toByte()
            setup[1] = OP_SETUP.toByte()
            val size = bytes?.size?.toLong() ?: 0L
            for (index in 0 until 8) {
                setup[2 + index] = (size ushr ((7 - index) * 8)).toByte()
            }
            setup[10] = 0
            setup[11] = ARTWORK_FILE_TYPE.toByte()
            pendingArtwork = PendingArtwork(id, fingerprint, bytes ?: ByteArray(0))
            artworkTransferId = id
            artworkFingerprint = fingerprint
            if (!sender(setup)) {
                pendingArtwork = null
                artworkTransferId = null
                artworkFingerprint = 0L
                log("NowPlaying 封面 SETUP 未能排队：${bytes?.size ?: 0}B")
                return
            }
            log("NowPlaying 封面 SETUP：id=$id size=${bytes?.size ?: 0}B")
        }
    }

    private fun sendArtworkChunks(artwork: PendingArtwork) {
        val sender = sendFileTransfer ?: return
        var offset = 0
        while (offset < artwork.bytes.size) {
            val count = minOf(ARTWORK_CHUNK_BYTES, artwork.bytes.size - offset)
            val last = offset + count >= artwork.bytes.size
            val packet = ByteArray(2 + count)
            packet[0] = artwork.id.toByte()
            packet[1] = (OP_DATA or (if (offset == 0) FLAG_FIRST else 0) or
                (if (last) FLAG_LAST else 0)).toByte()
            artwork.bytes.copyInto(packet, 2, offset, offset + count)
            if (!sender(packet)) {
                log("NowPlaying 封面 DATA 排队失败：id=${artwork.id} offset=$offset")
                return
            }
            offset += count
        }
        log("NowPlaying 封面 DATA 已发送：id=${artwork.id} bytes=${artwork.bytes.size}")
    }

    private fun artworkBytes(metadata: MediaMetadata?): ByteArray? {
        val bitmap = runCatching {
            metadata?.getBitmap(MediaMetadata.METADATA_KEY_ART)
                ?: metadata?.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)
        }.getOrNull() ?: artworkFromUri(metadata)
        if (bitmap == null) return null
        return runCatching {
            ByteArrayOutputStream().use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 86, output)
                output.toByteArray()
            }.takeIf { it.isNotEmpty() && it.size <= MAX_ARTWORK_BYTES }
        }.getOrNull()
    }

    private fun artworkFromUri(metadata: MediaMetadata?): Bitmap? {
        val value = runCatching {
            metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM_ART_URI)
                ?: metadata?.getString("android.media.metadata.ART_URI")
        }.getOrNull().orEmpty()
        if (value.isBlank()) return null
        return runCatching {
            appContext.contentResolver.openInputStream(Uri.parse(value))?.use(BitmapFactory::decodeStream)
        }.getOrNull()
    }

    /**
     * Some phones expose a transient playing controller before its metadata
     * arrives. Selecting it unconditionally turns every later 0x5001 update
     * into an empty media card even when another active controller already has
     * the real title/artwork. Prefer a playing controller with usable metadata,
     * then fall back to the playing controller for progress-only updates.
     */
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
        return text || artwork != null || readLyrics(metadata, null).isNotBlank()
    }

    private fun firstMetadataString(metadata: MediaMetadata?, vararg keys: String): String =
        keys.asSequence()
            .mapNotNull { key -> runCatching { metadata?.getString(key) }.getOrNull() }
            .firstOrNull { it.isNotBlank() }
            .orEmpty()

    private fun readLyrics(metadata: MediaMetadata?, playback: PlaybackState?): String {
        val keys = arrayOf(
            METADATA_KEY_LYRICS,
            "android.media.metadata.LYRIC",
            "ucar.media.metadata.LYRICS_WHOLE",
            "com.tencent.qqmusic.metadata.LYRICS",
        )
        val fromMetadata = keys.asSequence()
            .mapNotNull { key -> runCatching { metadata?.getString(key) }.getOrNull() }
            .firstOrNull { it.isNotBlank() }
        if (!fromMetadata.isNullOrBlank()) return fromMetadata
        return keys.asSequence()
            .mapNotNull { key ->
                runCatching { playback?.extras?.getCharSequence(key)?.toString() }.getOrNull()
                    ?: runCatching { playback?.extras?.getString(key) }.getOrNull()
            }
            .firstOrNull { it.isNotBlank() }
            .orEmpty()
    }

    private fun fingerprint(bytes: ByteArray): Long {
        var hash = FNV_OFFSET
        bytes.forEach { byte -> hash = (hash xor (byte.toLong() and 0xffL)) * FNV_PRIME }
        return if (hash == 0L) 1L else hash
    }

    @Volatile private var lastFallbackUpdateAt: Long? = null

    override fun close() {
        if (closed) return
        closed = true
        request = null
        send = null
        sendFileTransfer = null
        subscriptionToken = 0L
        synchronized(artworkLock) {
            pendingArtwork = null
            artworkTransferId = null
        }
        periodic?.cancel(false)
        executor.shutdownNow()
    }

    private companion object {
        const val METADATA_KEY_LYRICS = "android.media.metadata.LYRICS"
        const val ARTWORK_NONE = 0x80
        const val ARTWORK_FILE_TYPE = 2
        const val OP_DATA = 0
        const val OP_START = 1
        const val OP_CANCEL = 2
        const val OP_SETUP = 4
        const val OP_SUCCESS = 5
        const val FLAG_FIRST = 0x80
        const val FLAG_LAST = 0x40
        const val ARTWORK_CHUNK_BYTES = 1_024
        const val MAX_ARTWORK_BYTES = 2 * 1024 * 1024
        const val FNV_OFFSET = -3750763034362895579L
        const val FNV_PRIME = 1099511628211L
    }
}
