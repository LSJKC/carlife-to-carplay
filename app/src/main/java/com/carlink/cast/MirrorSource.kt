package com.carlink.cast

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.Surface
import com.carplay.adapter.PhoneProjectionAdapter
import com.carplay.adapter.ProjectionCapabilities
import com.carplay.adapter.ProjectionGrant
import com.carplay.adapter.ProjectionObserver
import com.carplay.sender.CaptureGeometry
import com.carplay.sender.VideoPackets
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Mirror source: the authorized screen, encoded by this app.
 *
 * This is the fallback when the phone has no built-in CarLife. Unlike the
 * CarLife source it needs a fresh MediaProjection grant on every connection,
 * and touch input only works while the accessibility service is enabled.
 */
internal class MirrorSource(private val context: Context) : PhoneProjectionAdapter {

    override val name: String = "屏幕镜像"
    override val requiresScreenGrant: Boolean = true
    override val capabilities: ProjectionCapabilities
        get() = ProjectionCapabilities(true, true, TouchBridge.available(), false,
            "镜像手机屏幕，需要每次授权录屏；触控回传需要无障碍服务")

    private var observer: ProjectionObserver? = null
    private var grant: ProjectionGrant? = null
    private var projection: MediaProjection? = null
    private var codec: MediaCodec? = null
    private var inputSurface: Surface? = null
    private var thread: HandlerThread? = null
    private var virtualDisplay: android.hardware.display.VirtualDisplay? = null
    private val running = AtomicBoolean(false)
    private var width = 0
    private var height = 0
    private var fps = 30
    private var geometry: CaptureGeometry? = null
    private var audioEnabled = false
    private var audioRate = 48_000
    /** True when the car took LPCM, so captured PCM goes out without encoding. */
    @Volatile private var negotiatedPcm = false
    private var audio: android.media.AudioRecord? = null
    private var audioCodec: MediaCodec? = null
    private var audioThread: Thread? = null

    override fun setObserver(observer: ProjectionObserver) { this.observer = observer }
    override fun setGrant(grant: ProjectionGrant) { this.grant = grant }

    override fun isSupported(context: Context) =
        context.getSystemService(MediaProjectionManager::class.java) != null

    override fun configureAudio(sampleRate: Int, channels: Int, pcm: Boolean) {
        if (sampleRate in listOf(44_100, 48_000)) audioRate = sampleRate
        negotiatedPcm = pcm
    }

    override fun requestKeyFrame() {
        runCatching {
            codec?.setParameters(Bundle().apply { putInt(MediaCodec.PARAMETER_KEY_REQUEST_SYNC_FRAME, 0) })
        }
    }

    override suspend fun prepare(context: Context, width: Int, height: Int, fps: Int): Boolean {
        require(width > 0 && height > 0 && width % 2 == 0 && height % 2 == 0)
        this.width = width
        this.height = height
        this.fps = fps.coerceIn(1, 60)
        return true
    }

    fun enableAudio(enabled: Boolean) { audioEnabled = enabled }

    override suspend fun startProjection(onFrameEncoded: (ByteArray, Long, Boolean) -> Unit) {
        check(running.compareAndSet(false, true)) { "镜像已启动" }
        val consent = grant ?: error("请先授权屏幕投射")
        val manager = context.getSystemService(MediaProjectionManager::class.java)
        val media = manager.getMediaProjection(consent.resultCode, consent.data)
            ?: error("屏幕投射授权被拒绝")
        projection = media
        grant = null
        val worker = HandlerThread("carlink-mirror").apply { start() }
        thread = worker
        val handler = Handler(worker.looper)
        media.registerCallback(object : MediaProjection.Callback() {
            override fun onStop() {
                if (running.get()) observer?.failed(IllegalStateException("系统已停止屏幕投射，请重新授权"))
            }
        }, handler)
        val bounds = context.resources.displayMetrics
        geometry = CaptureGeometry(bounds.widthPixels, bounds.heightPixels, width, height)
        val encoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
        codec = encoder
        encoder.setCallback(object : MediaCodec.Callback() {
            override fun onInputBufferAvailable(codec: MediaCodec, index: Int) = Unit
            override fun onOutputBufferAvailable(codec: MediaCodec, index: Int, info: MediaCodec.BufferInfo) {
                try {
                    if (running.get() && info.size > 0 && info.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG == 0) {
                        val buffer = codec.getOutputBuffer(index) ?: return
                        buffer.position(info.offset)
                        buffer.limit(info.offset + info.size)
                        val bytes = ByteArray(info.size)
                        buffer.get(bytes)
                        // MediaCodec emits Annex-B (start codes); AirPlay frames
                        // carry four-byte big-endian NAL lengths. Sending the raw
                        // buffer made the car decode a length of 1 and show a
                        // blank, transparent CarPlay window.
                        onFrameEncoded(VideoPackets.lengthPrefixed(bytes), info.presentationTimeUs,
                            info.flags and MediaCodec.BUFFER_FLAG_KEY_FRAME != 0)
                    }
                } catch (error: Exception) {
                    observer?.failed(error)
                } finally {
                    runCatching { codec.releaseOutputBuffer(index, false) }
                }
            }

            override fun onError(codec: MediaCodec, error: MediaCodec.CodecException) {
                observer?.failed(error)
            }

            override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {
                try {
                    fun field(name: String) = format.getByteBuffer(name)?.duplicate()?.let { buffer ->
                        ByteArray(buffer.remaining()).also(buffer::get)
                    }
                    val sps = field("csd-0") ?: return
                    val pps = field("csd-1") ?: return
                    observer?.videoConfig(VideoPackets.avcC(sps, pps))
                } catch (error: Exception) {
                    observer?.failed(error)
                }
            }
        }, handler)
        val format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height).apply {
            setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
            setInteger(MediaFormat.KEY_BIT_RATE, (width.toLong() * height * fps / 8).coerceIn(2_000_000, 24_000_000).toInt())
            setInteger(MediaFormat.KEY_FRAME_RATE, fps)
            // Five-second GOP, the same value CarWith uses (NativeVideoSelection.java).
            // A
            // sync frame every second on a large raster is a third of the
            // bitrate spent on redundancy. The car asks for one explicitly
            // One-second GOP, matching an established sender which
            // keeps this car connected. max-bframes=0 and
            // repeat-previous-frame-after come from the same block: no
            // B-frames (each is reorder latency) and a 100 ms keep-alive frame
            // so a stalled source does not leave the receiver waiting.
            setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)
            setInteger("max-bframes", 0)
            setLong("repeat-previous-frame-after", 100_000L)
            if (Build.VERSION.SDK_INT >= 29) setFloat(MediaFormat.KEY_MAX_FPS_TO_ENCODER, fps.toFloat())
        }
        encoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        val surface = encoder.createInputSurface()
        inputSurface = surface
        encoder.start()
        virtualDisplay = media.createVirtualDisplay("CarLink Mirror", width, height,
            context.resources.displayMetrics.densityDpi,
            android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            surface, null, handler)
        if (audioEnabled) startAudio(media, handler)
        observer?.diagnostic("镜像编码已启动：$width×$height@$fps")
    }

    private fun startAudio(media: MediaProjection, handler: Handler) {
        if (Build.VERSION.SDK_INT < 29) return
        if (context.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) !=
            android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            observer?.diagnostic("未授予录音权限，跳过音频")
            return
        }
        val config = android.media.AudioPlaybackCaptureConfiguration.Builder(media)
            .addMatchingUsage(android.media.AudioAttributes.USAGE_MEDIA)
            .addMatchingUsage(android.media.AudioAttributes.USAGE_GAME)
            .build()
        val buffer = maxOf(8192,
            android.media.AudioRecord.getMinBufferSize(audioRate,
                android.media.AudioFormat.CHANNEL_IN_STEREO,
                android.media.AudioFormat.ENCODING_PCM_16BIT))
        val record = android.media.AudioRecord.Builder()
            .setAudioFormat(android.media.AudioFormat.Builder()
                .setSampleRate(audioRate)
                .setEncoding(android.media.AudioFormat.ENCODING_PCM_16BIT)
                .setChannelMask(android.media.AudioFormat.CHANNEL_IN_STEREO).build())
            .setBufferSizeInBytes(buffer)
            .setAudioPlaybackCaptureConfig(config).build()
        audio = record
        if (record.state != android.media.AudioRecord.STATE_INITIALIZED) {
            observer?.diagnostic("音频捕获初始化失败")
            return
        }
        if (negotiatedPcm) {
            startPcmAudio(record)
            return
        }
        val encoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC)
        audioCodec = encoder
        encoder.configure(MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, audioRate, 2).apply {
            setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC)
            setInteger(MediaFormat.KEY_BIT_RATE, 128_000)
            setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, buffer)
        }, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        encoder.start()
        record.startRecording()
        audioThread = Thread({
            val info = MediaCodec.BufferInfo()
            var samples = 0L
            try {
                while (running.get()) {
                    val index = encoder.dequeueInputBuffer(10_000)
                    if (index >= 0) {
                        val input = encoder.getInputBuffer(index)
                        if (input != null) {
                            input.clear()
                            val read = record.read(input, minOf(input.capacity(), buffer),
                                android.media.AudioRecord.READ_BLOCKING)
                            if (read > 0) {
                                encoder.queueInputBuffer(index, 0, read,
                                    samples * 1_000_000 / audioRate, 0)
                                samples += read / 4
                            }
                        }
                    }
                    var out = encoder.dequeueOutputBuffer(info, 0)
                    while (out >= 0) {
                        if (info.size > 0 && info.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG == 0) {
                            val bytes = ByteArray(info.size)
                            encoder.getOutputBuffer(out)?.apply {
                                position(info.offset)
                                get(bytes)
                            }
                            observer?.audioFrame(bytes, info.presentationTimeUs)
                        }
                        encoder.releaseOutputBuffer(out, false)
                        out = encoder.dequeueOutputBuffer(info, 0)
                    }
                }
            } catch (_: Exception) {
                // A stopped capture is expected during teardown.
            }
        }, "carlink-mirror-audio").apply { isDaemon = true; start() }
    }

    /**
     * Forwards captured PCM straight to the car.
     *
     * The car accepted an LPCM stream, so there is nothing to encode: the
     * captured samples are already 16-bit little-endian stereo at the rate it
     * negotiated, which is exactly the wire format.
     */
    private fun startPcmAudio(record: android.media.AudioRecord) {
        record.startRecording()
        audioThread = Thread({
            val buffer = ByteArray(4096)
            var samples = 0L
            try {
                while (running.get()) {
                    val read = record.read(buffer, 0, buffer.size, android.media.AudioRecord.READ_BLOCKING)
                    if (read > 0) {
                        val pcm = if (read == buffer.size) buffer.copyOf() else buffer.copyOf(read)
                        observer?.audioFrame(pcm, samples * 1_000_000 / audioRate, pcm = true)
                        samples += read / 4
                    }
                }
            } catch (_: Exception) {
                // A stopped capture is expected during teardown.
            }
        }, "carlink-mirror-pcm").apply { isDaemon = true; start() }
        observer?.diagnostic("镜像音频：LPCM $audioRate Hz，直接透传")
    }

    override fun onTouchEvent(action: Int, x: Float, y: Float, pointerId: Int) {
        val point = geometry?.point(x, y) ?: return
        TouchBridge.dispatch(action, point.first, point.second, pointerId)
    }

    override fun onKeyEvent(keyCode: Int, isDown: Boolean) {
        TouchBridge.dispatchKey(keyCode, isDown)
    }

    override fun deliverMicrophone(pcm: ByteArray, sampleRate: Int, channels: Int): Boolean = false

    override suspend fun stopProjection() = release()

    private fun release() {
        if (!running.compareAndSet(true, false)) return
        runCatching { audio?.stop() }
        runCatching { audio?.release() }
        audio = null
        runCatching { audioCodec?.stop() }
        runCatching { audioCodec?.release() }
        audioCodec = null
        virtualDisplay?.release()
        virtualDisplay = null
        runCatching { codec?.stop() }
        runCatching { codec?.release() }
        codec = null
        inputSurface?.release()
        inputSurface = null
        runCatching { projection?.stop() }
        projection = null
        thread?.quitSafely()
        thread = null
    }
}
