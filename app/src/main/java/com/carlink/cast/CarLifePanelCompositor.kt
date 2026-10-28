package com.carlink.cast

import android.content.Context
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLContext
import android.opengl.EGLDisplay
import android.opengl.EGLSurface
import android.opengl.EGLExt
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLUtils
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.graphics.SurfaceTexture
import com.carplay.adapter.carlife.CarLifePanel
import com.carplay.adapter.carlife.CarLifeVideoProcessor
import com.carplay.sender.VideoPackets
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.ArrayDeque
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Composites CarLife's H.264 image over a full-panel wallpaper.
 *
 * CarLife is not the same as Buick-E5's resizeable virtual display: its phone
 * encoder owns the source dimensions. The source may be a full panel or a
 * zero-origin safe-area frame; a full opaque panel is preserved so its side
 * controls cannot be cropped. Safe-area placement is used only for a source
 * that is actually encoded at the safe-area size.
 *
 * The caller only enqueues input. Codec, EGL and output callbacks are owned by
 * one worker so CarLife's control/audio socket readers never wait for rendering.
 */
internal class CarLifePanelCompositor(
    initialPanel: CarLifePanel,
) : CarLifeVideoProcessor {
    private data class InputFormat(val width: Int, val height: Int, val fps: Int, val avcC: ByteArray, val generation: Long)
    private data class InputFrame(val bytes: ByteArray, val ptsUs: Long, val keyFrame: Boolean)

    private val lock = Object()
    @Volatile private var panel = initialPanel
    @Volatile private var safeAreaPolicy = CarPanelGeometry.SafeAreaPolicy(enabled = true)
    private val frames = ArrayDeque<InputFrame>()
    private var pendingFormat: InputFormat? = null
    private var inputGeneration = 0L
    private var waitingForInputKeyFrame = true
    @Volatile private var droppedInputFrames = 0L
    private val running = AtomicBoolean(false)

    @Volatile private var outputWidth = 0
    @Volatile private var outputHeight = 0
    @Volatile private var outputFps = 30
    @Volatile private var outputConfig: ((ByteArray) -> Unit)? = null
    @Volatile private var outputFrame: ((ByteArray, Long, Boolean) -> Unit)? = null
    @Volatile private var diagnostic: ((String) -> Unit)? = null
    @Volatile private var failed: ((Throwable) -> Unit)? = null
    @Volatile private var needKeyFrame: (() -> Unit)? = null
    @Volatile private var latestTouchWidth = 0
    @Volatile private var latestTouchHeight = 0
    private var worker: Thread? = null
    private var context: Context? = null
    private var encoder: MediaCodec? = null
    private var decoder: MediaCodec? = null
    private var decoderSurfaceTexture: SurfaceTexture? = null
    private var decoderSurface: Surface? = null
    private var eglDisplay: EGLDisplay = EGL14.EGL_NO_DISPLAY
    private var eglContext: EGLContext = EGL14.EGL_NO_CONTEXT
    private var eglSurface: EGLSurface = EGL14.EGL_NO_SURFACE
    private var videoTexture = 0
    private var wallpaperTexture = 0
    private var oesProgram = 0
    private var bitmapProgram = 0
    private var oesPositionLocation = -1
    private var oesTextureLocation = -1
    private var oesMatrixLocation = -1
    private var bitmapPositionLocation = -1
    private var bitmapTextureLocation = -1
    private var bitmapMatrixLocation = -1
    private var videoMatrix = FloatArray(16)
    private var videoHasFrame = false
    private var inputWidth = 0
    private var inputHeight = 0
    private var videoPlacement =
        CarPanelGeometry.videoPlacement(panel.panelWidth, panel.panelHeight, panel)
    private val videoFrameAvailable = AtomicBoolean(false)
    private var outputAvcC: ByteArray? = null
    private var requestOutputSync = false
    private var nextOutputPtsUs = 0L
    private var lastWallpaperRevision = Long.MIN_VALUE

    /** Last time the periodic render/encode counters were logged. */
    private var lastStatsAt = 0L
    private var renderedFrames = 0L
    private var encodedFrames = 0L
    private var keyFrames = 0L
    private var lastKeyFrameNanos: Long? = null
    private var firstOutputFormatLogged = false
    private var outputConfigWarningLogged = false
    private var encoderStallWarningLogged = false

    fun updatePanel(value: CarLifePanel) {
        check(!running.get()) { "不能在全画布合成期间修改 CarLife 面板" }
        panel = value
        videoPlacement = CarPanelGeometry.videoPlacement(
            value.panelWidth,
            value.panelHeight,
            value,
            safeAreaPolicy,
        )
    }

    fun updateSafeAreaPolicy(value: CarPanelGeometry.SafeAreaPolicy) {
        check(!running.get()) { "不能在全画布合成期间修改安全区方案" }
        safeAreaPolicy = value
        videoPlacement = CarPanelGeometry.videoPlacement(
            panel.panelWidth,
            panel.panelHeight,
            panel,
            value,
        )
    }

    override fun start(
        context: Context,
        outputWidth: Int,
        outputHeight: Int,
        outputFps: Int,
        onConfig: (ByteArray) -> Unit,
        onFrame: (ByteArray, Long, Boolean) -> Unit,
        onDiagnostic: (String) -> Unit,
        onFailure: (Throwable) -> Unit,
        onNeedKeyFrame: () -> Unit,
    ) {
        require(outputWidth == panel.panelWidth && outputHeight == panel.panelHeight) {
            "合成画布 ${outputWidth}x$outputHeight 与 CarLife 面板 ${panel.panelWidth}x${panel.panelHeight} 不一致"
        }
        if (!running.compareAndSet(false, true)) {
            // A duplicate RECORD/start callback must not force the adapter to
            // abandon the compositor and fall back to the raw CarLife stream.
            // Buick's virtual-screen implementation treats start as idempotent;
            // keep the same lifecycle property for this generic compositor.
            onDiagnostic("全画布合成器重复启动请求，沿用当前实例")
            return
        }
        this.context = context.applicationContext
        this.outputWidth = outputWidth
        this.outputHeight = outputHeight
        this.outputFps = outputFps.coerceIn(1, 60)
        outputConfig = onConfig
        outputFrame = onFrame
        diagnostic = onDiagnostic
        failed = onFailure
        needKeyFrame = onNeedKeyFrame

        // Log panel and safe area configuration at start
        onDiagnostic(
            "画布合成启动：panel=${panel.panelWidth}x${panel.panelHeight}, " +
                "safe@(${panel.safeX},${panel.safeY})/${panel.safeWidth}x${panel.safeHeight}, " +
                "safeAreaPolicy=${safeAreaPolicy.mode} enabled=${safeAreaPolicy.enabled}",
        )

        outputAvcC = null
        nextOutputPtsUs = 0L
        renderedFrames = 0L
        encodedFrames = 0L
        firstOutputFormatLogged = false
        outputConfigWarningLogged = false
        encoderStallWarningLogged = false
        val ready = CountDownLatch(1)
        val startupFailure = arrayOfNulls<Throwable>(1)
        worker = Thread({
            try {
                runComposer(ready)
            } catch (error: Throwable) {
                startupFailure[0] = error
                ready.countDown()
                if (running.getAndSet(false)) {
                    val trace = android.util.Log.getStackTraceString(error)
                    log(
                        "全画布合成线程异常：${error.javaClass.name}: " +
                            (error.message ?: "(无消息)") + "\n" + trace,
                    )
                    failed?.invoke(error)
                }
            } finally {
                releaseResources()
            }
        }, "carlink-panel-composer").apply {
            isDaemon = true
            start()
        }
        check(ready.await(15, TimeUnit.SECONDS)) {
            stop()
            "全画布合成器初始化超时"
        }
        startupFailure[0]?.let {
            throw IllegalStateException(
                "全画布合成器启动失败：${it.javaClass.simpleName}: ${it.message ?: "(无消息)"}",
                it,
            )
        }
    }

    override fun configureInput(width: Int, height: Int, fps: Int, avcC: ByteArray) {
        require(width > 0 && height > 0 && width % 2 == 0 && height % 2 == 0)
        synchronized(lock) {
            inputGeneration++
            pendingFormat = InputFormat(width, height, fps.coerceIn(1, 60), avcC.copyOf(), inputGeneration)
            latestTouchWidth = width
            latestTouchHeight = height
            frames.clear()
            waitingForInputKeyFrame = true
            lock.notifyAll()
        }
    }

    /**
     * Makes this encoder emit an IDR on the next rendered frame.
     *
     * A receiver that has just rebuilt its decoder needs a key frame, and this
     * is the cheap way to give it one: no source rebuild, no three-second gap,
     * no dropped picture. The output CONFIG is unchanged, so the sender can
     * resend the same avcC ahead of the new IDR.
     */
    override fun requestOutputKeyFrame(): Boolean {
        if (!running.get()) return false
        val config = outputAvcC ?: return false
        if (encoder == null) return false
        requestOutputSync = true
        // Ask immediately as well: the flag alone is only consumed when a
        // decoded frame arrives, and a source that has gone quiet would leave
        // the receiver waiting for a key frame it never gets.
        requestEncoderSyncFrame()
        /*
         * Re-announce the CONFIG with the sync frame.
         *
         * The sender's recovery gate only releases a key frame whose CONFIG
         * generation is at least the one it asked for, and it asks for
         * generation+1. Rebuilding the source encoder satisfied that by
         * producing a fresh CONFIG; asking this encoder for a sync frame does
         * not, so without this line the gate stays shut and every frame is
         * discarded — 1265 frames produced and 9 delivered, which is exactly
         * what the "0.1 fps, no picture" session shows.
         */
        outputConfig?.invoke(config.copyOf())
        return true
    }

    override fun offerFrame(accessUnit: ByteArray, ptsUs: Long, keyFrame: Boolean) {
        if (!running.get() || accessUnit.isEmpty()) return
        var dropped = 0L
        synchronized(lock) {
            if (waitingForInputKeyFrame && !keyFrame) {
                droppedInputFrames++
                return
            }
            if (frames.size >= INPUT_QUEUE_CAPACITY) {
                /*
                 * Rendering/encoding can briefly fall behind without the
                 * CarLife encoder being broken.  Clearing the whole queue and
                 * requesting a new encoder generation made the car lose its
                 * decoder, then the next reset raced the AirPlay socket.
                 * Keep the newest dependency frame and discard stale pictures.
                 */
                if (keyFrame) {
                    while (frames.size >= INPUT_QUEUE_CAPACITY) {
                        frames.removeFirst()
                        dropped++
                    }
                    waitingForInputKeyFrame = false
                    frames.addLast(InputFrame(accessUnit.copyOf(), ptsUs, true))
                } else {
                    val stale = frames.indexOfFirst { !it.keyFrame }
                    if (stale >= 0) {
                        repeat(stale + 1) {
                            frames.removeFirst()
                            dropped++
                        }
                        frames.addLast(InputFrame(accessUnit.copyOf(), ptsUs, false))
                    } else {
                        dropped++
                    }
                }
                lock.notifyAll()
            } else {
                if (keyFrame) waitingForInputKeyFrame = false
                frames.addLast(InputFrame(accessUnit.copyOf(), ptsUs, keyFrame))
            }
            lock.notifyAll()
        }
        if (dropped > 0L) {
            droppedInputFrames += dropped
            if (droppedInputFrames == dropped ||
                droppedInputFrames % INPUT_DROP_LOG_INTERVAL == 0L
            ) {
                log(
                    "全画布输入短时拥塞：丢弃过期帧 $dropped（累计 $droppedInputFrames），" +
                        "保留最新关键帧，不重建 CarLife 编码器",
                )
            }
        }
    }

    override fun mapTouchToSource(panelX: Float, panelY: Float): Pair<Float, Float>? =
        CarPanelGeometry.mapVideoTouchToSource(
            panelX,
            panelY,
            videoPlacement,
            latestTouchWidth,
            latestTouchHeight,
        )

    override fun stop() {
        if (!running.getAndSet(false)) return
        synchronized(lock) { lock.notifyAll() }
        worker?.interrupt()
        worker?.join(2_000)
        worker = null
        context = null
    }

    private fun runComposer(ready: CountDownLatch) {
        initializeEncoder()
        initializeEgl()
        initializeTextures()
        log(
            "整屏合成已启动：输出 ${outputWidth}x$outputHeight@$outputFps，" +
                "CarLife 区域 ${panel.safeWidth}x${panel.safeHeight}@(${panel.safeX},${panel.safeY})；" +
                "二次编码 ${ENCODER_BIT_RATE / 1_000_000}Mbps，硬件编码器=${encoder?.name}",
        )
        // Do not count a wallpaper-only frame as a successful CarLife session.
        drainEncoder()
        ready.countDown()

        var inputFormatGeneration = 0L
        var pendingInputFrame: InputFrame? = null
        var nextRenderAt = System.nanoTime()
        while (running.get()) {
            val (format, frame) = takeInput(allowFrame = pendingInputFrame == null)
            if (format != null && format.generation != inputFormatGeneration) {
                configureDecoder(format)
                pendingInputFrame = null
                inputFormatGeneration = format.generation
                requestOutputSync = true
                outputAvcC?.let { outputConfig?.invoke(it.copyOf()) }
                log(
                    "CarLife 解码器已重建：${format.width}x${format.height}@${format.fps}，" +
                        "输入 CONFIG=${format.avcC.size}B，generation=${format.generation}",
                )
            }
            if (frame != null) pendingInputFrame = frame
            pendingInputFrame?.let { current ->
                if (feedDecoder(current)) {
                    pendingInputFrame = null
                } else {
                    Thread.sleep(2)
                }
            }
            drainDecoder()
            if (videoFrameAvailable.getAndSet(false)) {
                decoderSurfaceTexture?.updateTexImage()
                decoderSurfaceTexture?.getTransformMatrix(videoMatrix)
                if (!videoHasFrame) log("CarLife 视频首帧已进入全画布")
                videoHasFrame = true
                if (requestOutputSync) {
                    requestOutputSync = false
                    requestEncoderSyncFrame()
                }
            }

            val now = System.nanoTime()
            if (now >= nextRenderAt) {
                refreshWallpaperIfChanged()
                if (videoHasFrame) {
                    renderFrame(nextOutputPtsUs)
                    nextOutputPtsUs += frameIntervalUs()
                }
                drainEncoder()
                if (
                    renderedFrames >= outputFps.coerceAtLeast(1) * 3L &&
                    encodedFrames == 0L &&
                    !encoderStallWarningLogged
                ) {
                    encoderStallWarningLogged = true
                    val format = runCatching { encoder?.outputFormat?.toString() }.getOrNull() ?: "不可用"
                    log("全画布 EGL 已渲染 $renderedFrames 帧，但编码器 3 秒内没有输出 H.264；输出格式=$format")
                }
                val intervalNanos = frameIntervalUs() * 1_000L
                nextRenderAt = if (now - nextRenderAt > intervalNanos) now + intervalNanos else nextRenderAt + intervalNanos
                /*
                 * Periodic counters. The driver saw "0.1 fps, session alive,
                 * no picture", which can mean the loop is stalled, the decoder
                 * is starved, or the encoder is not draining — three very
                 * different faults that look identical from the outside.
                 */
                if (now - lastStatsAt >= 5_000_000_000L) {
                    lastStatsAt = now
                    val queued = synchronized(lock) { frames.size }
                    log(
                        "全画布统计：渲染=$renderedFrames 编码=$encodedFrames " +
                            "输入队列=$queued 丢输入=$droppedInputFrames 有画面=$videoHasFrame",
                    )
                }
            } else {
                synchronized(lock) {
                    val waitMs = ((nextRenderAt - now) / 1_000_000L).coerceIn(1L, 5L)
                    lock.wait(waitMs)
                }
            }
        }
    }

    /**
     * Seconds between the encoder's own IDRs.
     *
     * Back to 1, matching the 对照实现 sender (`Yo.java`:
     * `setInteger("i-frame-interval", 1)`), which is the sender known to hold
     * this car's session open. 1.2.96 raised this to 5 on the theory that
     * frequent key frames were starving the link; the reference that actually
     * stays connected uses 1, so the shorter interval is not the problem — the
     * encoder's other settings were. Explicit key frames are still requested on
     * demand through [requestEncoderSyncFrame].
     */
    private val keyFrameIntervalSeconds = 1

    /**
     * Clamps the requested bitrate to what this encoder actually supports.
     *
     * The 对照实现 sender reads `getBitrateRange()` from the codec and clamps
     * into it before building the format. Handing a codec a rate it cannot
     * honour either throws or gets silently rounded, and either way the car's
     * buffer math no longer matches what was negotiated.
     */
    private fun clampedBitrate(): Int = runCatching {
        val range = MediaCodec.createEncoderByType(MIME_AVC)
            .codecInfo.getCapabilitiesForType(MIME_AVC)
            .videoCapabilities.bitrateRange
        ENCODER_BIT_RATE.coerceIn(range.lower, range.upper)
    }.getOrElse { ENCODER_BIT_RATE }

    private fun initializeEncoder() {
        fun buildFormat(bitrateMode: Int?): MediaFormat =
            MediaFormat.createVideoFormat(MIME_AVC, outputWidth, outputHeight).apply {
                setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
                setInteger(MediaFormat.KEY_BIT_RATE, clampedBitrate())
                setInteger(MediaFormat.KEY_FRAME_RATE, outputFps)
                setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, keyFrameIntervalSeconds)
                /*
                 * The three settings below are copied from the 对照实现
                 * sender's encoder block; none of them existed here before.
                 *
                 *  - `max-fps-to-encoder` pins the encoder to the negotiated
                 *    rate instead of letting it run ahead and buffer.
                 *  - `repeat-previous-frame-after` = 100 ms keeps the stream
                 *    alive through a gap: without it a stalled source makes the
                 *    receiver wait for a frame that never arrives.
                 *  - `max-bframes` = 0 removes B-frames entirely. B-frames
                 *    reorder presentation, and this receiver decodes a live
                 *    30 fps stream — every B-frame is latency the car did not
                 *    ask for.
                 */
                setFloat(MediaFormat.KEY_MAX_FPS_TO_ENCODER, outputFps.toFloat())
                setLong("repeat-previous-frame-after", 100_000L)
                setInteger("max-bframes", 0)
                setInteger("priority", 0)
                bitrateMode?.let { setInteger(MediaFormat.KEY_BITRATE_MODE, it) }
            }

        /**
         * Clamps the requested bitrate to what this encoder actually supports.
         *
         * The 对照实现 sender reads `getBitrateRange()` from the codec and
         * clamps into it before building the format. Handing a codec a rate it
         * cannot honour either throws or gets silently rounded, and either way
         * the car's buffer math no longer matches what was negotiated.
         */

        /*
         * Constant bitrate, so a key frame cannot blow past the budget.
         *
         * Without this the encoder picked VBR (the session log shows
         * bitrate-mode=1) and produced 430 KB key frames on a 1972x520 canvas
         * — 3.5 Mbit in a single frame, roughly a second of the link's whole
         * budget delivered at once. The car buffers what it cannot drain, stops
         * reading, and resets the control connection twenty to twenty-five
         * seconds in. CBR keeps every frame inside the requested rate. An
         * encoder that rejects CBR falls back to its default mode.
         */
        val cbr = MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CBR
        val codec = MediaCodec.createEncoderByType(MIME_AVC)
        try {
            codec.configure(buildFormat(cbr), null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        } catch (rejected: IllegalArgumentException) {
            log("编码器不接受 CBR（${rejected.message}），改用默认码率模式")
            codec.reset()
            codec.configure(buildFormat(null), null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        }

        encoder = codec
        val input = codec.createInputSurface()
        codec.start()
        encoderInputSurface = input
    }

    private var encoderInputSurface: Surface? = null

    private fun initializeEgl() {
        val display = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        check(display != EGL14.EGL_NO_DISPLAY) { "eglGetDisplay 失败" }
        eglDisplay = display
        val versions = IntArray(2)
        checkEgl(EGL14.eglInitialize(display, versions, 0, versions, 1), "eglInitialize")
        val configs = arrayOfNulls<EGLConfig>(1)
        val count = IntArray(1)
        val recordableAttributes = intArrayOf(
            EGL14.EGL_RED_SIZE, 8,
            EGL14.EGL_GREEN_SIZE, 8,
            EGL14.EGL_BLUE_SIZE, 8,
            EGL14.EGL_ALPHA_SIZE, 8,
            EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL_RECORDABLE_ANDROID, 1,
            EGL14.EGL_NONE,
        )
        val chosenRecordable = EGL14.eglChooseConfig(
            display,
            recordableAttributes,
            0,
            configs,
            0,
            1,
            count,
            0,
        ) && count[0] > 0
        if (!chosenRecordable) {
            val fallbackAttributes = intArrayOf(
                EGL14.EGL_RED_SIZE, 8,
                EGL14.EGL_GREEN_SIZE, 8,
                EGL14.EGL_BLUE_SIZE, 8,
                EGL14.EGL_ALPHA_SIZE, 8,
                EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                EGL14.EGL_NONE,
            )
            checkEgl(
                EGL14.eglChooseConfig(
                    display,
                    fallbackAttributes,
                    0,
                    configs,
                    0,
                    1,
                    count,
                    0,
                ) && count[0] > 0,
                "eglChooseConfig(recordable/fallback)",
            )
            log("EGL 不支持 recordable 配置，已回退到普通 ES2 配置")
        }
        val config = checkNotNull(configs[0])
        eglContext = EGL14.eglCreateContext(
            display,
            config,
            EGL14.EGL_NO_CONTEXT,
            intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE),
            0,
        )
        check(eglContext != EGL14.EGL_NO_CONTEXT) { "eglCreateContext 失败" }
        eglSurface = EGL14.eglCreateWindowSurface(
            display,
            config,
            checkNotNull(encoderInputSurface),
            intArrayOf(EGL14.EGL_NONE),
            0,
        )
        check(eglSurface != EGL14.EGL_NO_SURFACE) { "eglCreateWindowSurface 失败" }
        checkEgl(EGL14.eglMakeCurrent(display, eglSurface, eglSurface, eglContext), "eglMakeCurrent")
    }

    private fun initializeTextures() {
        val videoIds = IntArray(1)
        GLES20.glGenTextures(1, videoIds, 0)
        videoTexture = videoIds[0]
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, videoTexture)
        setTextureParameters(GLES11Ext.GL_TEXTURE_EXTERNAL_OES)
        decoderSurfaceTexture = SurfaceTexture(videoTexture).apply {
            setDefaultBufferSize(panel.panelWidth, panel.panelHeight)
            setOnFrameAvailableListener {
                videoFrameAvailable.set(true)
                synchronized(lock) { lock.notifyAll() }
            }
        }
        decoderSurface = Surface(decoderSurfaceTexture)

        val wallpaperIds = IntArray(1)
        GLES20.glGenTextures(1, wallpaperIds, 0)
        wallpaperTexture = wallpaperIds[0]
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, wallpaperTexture)
        setTextureParameters(GLES20.GL_TEXTURE_2D)
        val pixel = ByteBuffer.allocateDirect(4).apply {
            put(byteArrayOf(0, 0, 0, 0))
            position(0)
        }
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, 1, 1, 0,
            GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixel,
        )
        oesProgram = buildProgram(oes = true)
        bitmapProgram = buildProgram(oes = false)
        oesPositionLocation = GLES20.glGetAttribLocation(oesProgram, "aPos")
        oesTextureLocation = GLES20.glGetAttribLocation(oesProgram, "aTexCoord")
        oesMatrixLocation = GLES20.glGetUniformLocation(oesProgram, "uSTMatrix")
        bitmapPositionLocation = GLES20.glGetAttribLocation(bitmapProgram, "aPos")
        bitmapTextureLocation = GLES20.glGetAttribLocation(bitmapProgram, "aTexCoord")
        bitmapMatrixLocation = GLES20.glGetUniformLocation(bitmapProgram, "uSTMatrix")
        refreshWallpaperIfChanged(force = true)
    }

    private fun setTextureParameters(target: Int) {
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(target, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
    }

    private fun refreshWallpaperIfChanged(force: Boolean = false) {
        val revision = PanelWallpaperStore.revision
        if (!force && revision == lastWallpaperRevision) return
        lastWallpaperRevision = revision
        val bitmap = context?.let { PanelWallpaperStore.decode(it, maxOf(outputWidth, outputHeight)) }
        if (bitmap == null) {
            resetWallpaperTexture()
            log("未设置壁纸，透明画布按 H.264 黑底输出")
            return
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, wallpaperTexture)
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
        wallpaperUv = WallpaperTextureGeometry.aspectFillUv(
            bitmap.width,
            bitmap.height,
            outputWidth,
            outputHeight,
        )
        val uvText = wallpaperUv.joinToString(",") { "%.4f".format(it) }
        log("全画布壁纸已更新：${bitmap.width}x${bitmap.height} → ${outputWidth}x$outputHeight，UV=[$uvText]")
        bitmap.recycle()
    }

    private fun resetWallpaperTexture() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, wallpaperTexture)
        val transparent = ByteBuffer.allocateDirect(4).apply {
            put(byteArrayOf(0, 0, 0, 0))
            position(0)
        }
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, 1, 1, 0,
            GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, transparent,
        )
        wallpaperUv = floatArrayOf(0f, 1f, 0f, 1f)
    }

    private var wallpaperUv = floatArrayOf(0f, 1f, 0f, 1f)

    private fun configureDecoder(input: InputFormat) {
        releaseDecoder()
        inputWidth = input.width
        inputHeight = input.height
        decoderSurfaceTexture?.setDefaultBufferSize(input.width, input.height)
        val (sps, pps) = parseAvcC(input.avcC)
        val format = MediaFormat.createVideoFormat(MIME_AVC, input.width, input.height).apply {
            setInteger(MediaFormat.KEY_FRAME_RATE, input.fps)
            setByteBuffer("csd-0", ByteBuffer.wrap(START_CODE + sps))
            setByteBuffer("csd-1", ByteBuffer.wrap(START_CODE + pps))
        }
        decoder = MediaCodec.createDecoderByType(MIME_AVC).apply {
            configure(format, checkNotNull(decoderSurface), null, 0)
            start()
        }
        videoPlacement = CarPanelGeometry.videoPlacement(input.width, input.height, panel, safeAreaPolicy)
        val placementName = when (videoPlacement.layout) {
            CarPanelGeometry.VideoLayout.FULL_PANEL ->
                if (panel.safeX != 0 || panel.safeY != 0 ||
                    panel.safeWidth != panel.panelWidth || panel.safeHeight != panel.panelHeight
                ) {
                    "CarLife 未采纳安全区编码，保留完整 UI ${videoPlacement.width}x${videoPlacement.height}；" +
                        "当前无法安全叠加外部壁纸"
                } else {
                    "完整画布输入原样铺满 ${videoPlacement.width}x${videoPlacement.height}"
                }
            CarPanelGeometry.VideoLayout.SAFE_AREA ->
                "安全区输入铺到 (${videoPlacement.x},${videoPlacement.y}) " +
                    "${videoPlacement.width}x${videoPlacement.height}，" +
                    "采样源=${videoPlacement.sourceX},${videoPlacement.sourceY} " +
                    "${videoPlacement.sourceWidth}x${videoPlacement.sourceHeight}"
            CarPanelGeometry.VideoLayout.SAFE_VIEWPORT ->
                "完整帧不裁剪，缩放到安全区 viewport " +
                    "(${videoPlacement.x},${videoPlacement.y}) " +
                    "${videoPlacement.width}x${videoPlacement.height}；" +
                    "外侧保留壁纸"
        }
        log("CarLife 视频布局：输入 ${input.width}x${input.height} → $placementName")
        synchronized(lock) { waitingForInputKeyFrame = true }
        videoHasFrame = false
        videoFrameAvailable.set(false)
    }

    private fun feedDecoder(frame: InputFrame): Boolean {
        val codec = decoder ?: return false
        val nals = VideoPackets.nals(frame.bytes)
        val annexB = ByteArray(nals.sumOf { it.size + START_CODE.size }).also { result ->
            var offset = 0
            nals.forEach { nal ->
                START_CODE.copyInto(result, offset)
                offset += START_CODE.size
                nal.copyInto(result, offset)
                offset += nal.size
            }
        }
        val index = codec.dequeueInputBuffer(0)
        if (index < 0) return false
        val buffer = codec.getInputBuffer(index) ?: return false
        if (annexB.size > buffer.capacity()) {
            throw IllegalStateException("CarLife H.264 access unit ${annexB.size}B 超过解码器输入缓冲区")
        }
        buffer.clear()
        buffer.put(annexB)
        codec.queueInputBuffer(index, 0, annexB.size, frame.ptsUs, 0)
        /*
         * CarLife's key frames do not force this encoder to emit one.
         *
         * CarLife re-sends an IDR roughly every 650 ms, and mirroring each of
         * them made this encoder produce a 250-320 KB sync frame at the same
         * cadence — about 7 Mbps of key frames alone on a 1972x520 canvas,
         * which saturated the Wi-Fi link and made the car reset the session
         * after twenty to twenty-five seconds. The encoder already emits an
         * IDR every KEY_I_FRAME_INTERVAL on its own, and a sync frame is
         * requested explicitly when the decoder is rebuilt or the car asks
         * for one, which is all the receiver needs.
         */
        return true
    }

    private fun drainDecoder() {
        val codec = decoder ?: return
        val info = MediaCodec.BufferInfo()
        var index = codec.dequeueOutputBuffer(info, 0)
        while (index >= 0) {
            val render = info.size > 0
            codec.releaseOutputBuffer(index, render)
            index = codec.dequeueOutputBuffer(info, 0)
        }
    }

    private fun renderFrame(ptsUs: Long) {
        GLES20.glViewport(0, 0, outputWidth, outputHeight)
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        drawBitmap()
        if (videoHasFrame) drawVideo()
        val presentationNs = ptsUs * 1_000L
        check(EGLExt.eglPresentationTimeANDROID(eglDisplay, eglSurface, presentationNs)) {
            "eglPresentationTimeANDROID 失败"
        }
        checkEgl(EGL14.eglSwapBuffers(eglDisplay, eglSurface), "eglSwapBuffers")
        renderedFrames++
        if (renderedFrames == 1L) {
            log("全画布 EGL 首帧已提交编码器：${outputWidth}x$outputHeight")
        }
    }

    private fun drawBitmap() {
        GLES20.glUseProgram(bitmapProgram)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, wallpaperTexture)
        GLES20.glUniformMatrix4fv(
            bitmapMatrixLocation,
            1,
            false,
            IDENTITY_MATRIX,
            0,
        )
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        drawRect(
            program = bitmapProgram,
            left = -1f,
            bottom = -1f,
            right = 1f,
            top = 1f,
            textureCoordinates = WallpaperTextureGeometry.quadCoordinates(wallpaperUv),
        )
        GLES20.glDisable(GLES20.GL_BLEND)
    }

    private fun drawVideo() {
        GLES20.glUseProgram(oesProgram)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, videoTexture)
        GLES20.glUniformMatrix4fv(oesMatrixLocation, 1, false, videoMatrix, 0)
        val placement = videoPlacement
        val left = placement.x.toFloat()
        val top = placement.y.toFloat()
        val right = left + placement.width
        val bottom = top + placement.height
        drawRect(
            program = oesProgram,
            left = px(left),
            bottom = py(bottom),
            right = px(right),
            top = py(top),
            textureCoordinates = CarPanelGeometry.videoTextureCoordinates(
                placement,
                inputWidth,
                inputHeight,
            ),
        )
    }

    private fun drawRect(
        program: Int,
        left: Float,
        bottom: Float,
        right: Float,
        top: Float,
        textureCoordinates: FloatArray,
    ) {
        val positions = floatBuffer(
            left, bottom,
            right, bottom,
            left, top,
            right, top,
        )
        val coords = floatBuffer(*textureCoordinates)
        val positionLocation = if (program == oesProgram) oesPositionLocation else bitmapPositionLocation
        val textureLocation = if (program == oesProgram) oesTextureLocation else bitmapTextureLocation
        GLES20.glVertexAttribPointer(positionLocation, 2, GLES20.GL_FLOAT, false, 0, positions)
        GLES20.glEnableVertexAttribArray(positionLocation)
        GLES20.glVertexAttribPointer(textureLocation, 2, GLES20.GL_FLOAT, false, 0, coords)
        GLES20.glEnableVertexAttribArray(textureLocation)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
    }

    private fun drainEncoder() {
        val codec = encoder ?: return
        val info = MediaCodec.BufferInfo()
        var index = codec.dequeueOutputBuffer(info, 0)
        while (index >= 0) {
            val buffer = codec.getOutputBuffer(index)
            if (info.size > 0 && buffer != null) {
                val bytes = ByteArray(info.size)
                buffer.position(info.offset)
                buffer.limit(info.offset + info.size)
                buffer.get(bytes)
                if (info.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG != 0) {
                    val config = findAvcC(bytes) ?: configFromFormat(codec.outputFormat)
                    if (config != null) {
                        publishConfig(config)
                    } else if (!outputConfigWarningLogged) {
                        outputConfigWarningLogged = true
                        val prefix = bytes.take(16).joinToString("") { "%02x".format(it) }
                        log("全画布编码器 CODEC_CONFIG 无法解析 SPS/PPS：size=${bytes.size}B，prefix=$prefix")
                    }
                } else {
                    if (outputAvcC == null) {
                        codec.outputFormat?.let(::configFromFormat)?.let(::publishConfig)
                    }
                    if (outputAvcC != null) {
                        val keyFrame = info.flags and MediaCodec.BUFFER_FLAG_KEY_FRAME != 0
                        outputFrame?.invoke(VideoPackets.lengthPrefixed(bytes), info.presentationTimeUs, keyFrame)
                        if (keyFrame) {
                            /*
                             * Key-frame cadence and size, every time.
                             *
                             * Where the IDR budget goes is not visible from the
                             * AirPlay side alone — the car only sees the result.
                             * Printing the interval between sync frames and
                             * their size here is what tells a three-second GOP
                             * from an on-demand one, and catches the encoder
                             * falling back to a short interval on its own.
                             */
                            val now = System.nanoTime()
                            val since = lastKeyFrameNanos?.let { (now - it) / 1_000_000 } ?: -1
                            lastKeyFrameNanos = now
                            keyFrames++
                            log(
                                "全画布 IDR：第 $keyFrames 个，${bytes.size}B，" +
                                    "距上一个=$since ms，间隔设定=${keyFrameIntervalSeconds}s",
                            )
                        }
                        encodedFrames++
                        if (encodedFrames == 1L) {
                            val format = runCatching { codec.outputFormat.toString() }.getOrDefault("未知")
                            log(
                                "全画布编码首帧已交给 AirPlay：${bytes.size}B，IDR=$keyFrame，" +
                                    "输出格式=$format",
                            )
                        }
                    }
                }
            }
            codec.releaseOutputBuffer(index, false)
            index = codec.dequeueOutputBuffer(info, 0)
        }
        if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
            val format = codec.outputFormat
            if (!firstOutputFormatLogged) {
                firstOutputFormatLogged = true
                /*
                 * Print the settings this build actually asked for, next to
                 * what the codec reports back.
                 *
                 * Every value here came from the 对照实现 sender and cannot
                 * be confirmed from a session log otherwise: a codec is free to
                 * ignore `max-bframes` or round the bitrate, and the only way
                 * to tell "we set it" from "the codec honoured it" is to log
                 * both sides at the moment the format is known.
                 */
                log(
                    "全画布编码器输出格式已就绪：$format\n" +
                        "  请求参数：bitrate=${clampedBitrate()}（原设 $ENCODER_BIT_RATE）" +
                        " fps=$outputFps gop=${keyFrameIntervalSeconds}s" +
                        " max-bframes=0 repeat-after=100ms",
                )
            }
            val config = configFromFormat(format)
            if (config != null) {
                publishConfig(config)
            } else if (!outputConfigWarningLogged) {
                outputConfigWarningLogged = true
                val csd0 = format.getByteBuffer("csd-0")?.remaining() ?: 0
                val csd1 = format.getByteBuffer("csd-1")?.remaining() ?: 0
                log("全画布编码器格式未识别到 SPS/PPS：csd-0=${csd0}B，csd-1=${csd1}B")
            }
        }
    }

    private fun configFromFormat(format: MediaFormat): ByteArray? {
        val csd = listOf("csd-0", "csd-1").map { key ->
            format.getByteBuffer(key)?.duplicate()?.let { buffer ->
                ByteArray(buffer.remaining()).also(buffer::get)
            }
        }
        return AvcCodecConfig.fromParameterSets(*csd.toTypedArray())
    }

    private fun findAvcC(bytes: ByteArray): ByteArray? =
        AvcCodecConfig.fromParameterSets(bytes)

    private fun parseAvcC(avcC: ByteArray): Pair<ByteArray, ByteArray> {
        require(avcC.size >= 11 && avcC[0].toInt() == 1) { "CarLife AVC CONFIG 格式无效" }
        var offset = 5
        val spsCount = avcC[offset++].toInt() and 0x1f
        require(spsCount > 0)
        val sps = readAvcNal(avcC, offset).also { offset += 2 + it.size }
        require(offset < avcC.size)
        val ppsCount = avcC[offset++].toInt() and 0xff
        require(ppsCount > 0)
        val pps = readAvcNal(avcC, offset)
        return sps to pps
    }

    private fun readAvcNal(bytes: ByteArray, offset: Int): ByteArray {
        require(offset + 2 <= bytes.size)
        val length = ((bytes[offset].toInt() and 0xff) shl 8) or (bytes[offset + 1].toInt() and 0xff)
        require(length > 0 && offset + 2 + length <= bytes.size)
        return bytes.copyOfRange(offset + 2, offset + 2 + length)
    }

    private fun publishConfig(config: ByteArray) {
        val changed = outputAvcC?.contentEquals(config) != true
        outputAvcC = config.copyOf()
        outputConfig?.invoke(config.copyOf())
        if (changed) log("全画布编码 CONFIG 就绪：${config.size}B，${outputWidth}x$outputHeight")
    }

    private fun requestEncoderSyncFrame() {
        runCatching {
            encoder?.setParameters(Bundle().apply { putInt(MediaCodec.PARAMETER_KEY_REQUEST_SYNC_FRAME, 0) })
        }.onFailure { log("请求全画布编码器关键帧失败：${it.message}") }
    }

    private fun takeInput(allowFrame: Boolean): Pair<InputFormat?, InputFrame?> = synchronized(lock) {
        if (pendingFormat == null && (!allowFrame || frames.isEmpty()) && running.get()) lock.wait(4L)
        val format = pendingFormat.also { pendingFormat = null }
        val frame = if (format == null && allowFrame) frames.pollFirst() else null
        format to frame
    }

    private fun releaseDecoder() {
        runCatching { decoder?.stop() }
        runCatching { decoder?.release() }
        decoder = null
    }

    private fun releaseResources() {
        releaseDecoder()
        runCatching { encoder?.stop() }
        runCatching { encoder?.release() }
        encoder = null
        runCatching { decoderSurface?.release() }
        decoderSurface = null
        runCatching { decoderSurfaceTexture?.release() }
        decoderSurfaceTexture = null
        runCatching { wallpaperTexture.takeIf { it != 0 }?.let { GLES20.glDeleteTextures(1, intArrayOf(it), 0) } }
        runCatching { videoTexture.takeIf { it != 0 }?.let { GLES20.glDeleteTextures(1, intArrayOf(it), 0) } }
        wallpaperTexture = 0
        videoTexture = 0
        if (eglDisplay != EGL14.EGL_NO_DISPLAY) {
            runCatching {
                EGL14.eglMakeCurrent(eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)
            }
            runCatching { EGL14.eglDestroySurface(eglDisplay, eglSurface) }
            runCatching { EGL14.eglDestroyContext(eglDisplay, eglContext) }
            runCatching { EGL14.eglTerminate(eglDisplay) }
        }
        eglDisplay = EGL14.EGL_NO_DISPLAY
        eglSurface = EGL14.EGL_NO_SURFACE
        eglContext = EGL14.EGL_NO_CONTEXT
        runCatching { encoderInputSurface?.release() }
        encoderInputSurface = null
    }

    private fun buildProgram(oes: Boolean): Int {
        val precision = fragmentFloatPrecision()
        val fragment = if (oes) {
            """
            #extension GL_OES_EGL_image_external : require
            precision $precision float;
            varying vec2 vTexCoord;
            uniform samplerExternalOES uTexture;
            void main() { gl_FragColor = texture2D(uTexture, vTexCoord); }
            """.trimIndent()
        } else {
            """
            precision $precision float;
            varying vec2 vTexCoord;
            uniform sampler2D uTexture;
            void main() { gl_FragColor = texture2D(uTexture, vTexCoord); }
            """.trimIndent()
        }
        val vertex = """
            attribute vec2 aPos;
            attribute vec2 aTexCoord;
            uniform mat4 uSTMatrix;
            varying vec2 vTexCoord;
            void main() {
                gl_Position = vec4(aPos, 0.0, 1.0);
                vTexCoord = (uSTMatrix * vec4(aTexCoord, 0.0, 1.0)).xy;
            }
        """.trimIndent()
        fun compile(type: Int, source: String): Int {
            val shader = GLES20.glCreateShader(type)
            GLES20.glShaderSource(shader, source)
            GLES20.glCompileShader(shader)
            val status = IntArray(1)
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0)
            check(status[0] != 0) { GLES20.glGetShaderInfoLog(shader) ?: "GLSL 编译失败" }
            return shader
        }
        val program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, compile(GLES20.GL_VERTEX_SHADER, vertex))
        GLES20.glAttachShader(program, compile(GLES20.GL_FRAGMENT_SHADER, fragment))
        GLES20.glLinkProgram(program)
        val status = IntArray(1)
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, status, 0)
        check(status[0] != 0) { GLES20.glGetProgramInfoLog(program) ?: "GLSL 链接失败" }
        GLES20.glUseProgram(program)
        GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "uTexture"), 0)
        return program
    }

    private fun fragmentFloatPrecision(): String {
        val range = IntArray(2)
        val precision = IntArray(1)
        GLES20.glGetShaderPrecisionFormat(
            GLES20.GL_FRAGMENT_SHADER,
            GLES20.GL_HIGH_FLOAT,
            range,
            0,
            precision,
            0,
        )
        return if (precision[0] > 0 || range.any { it != 0 }) "highp" else "mediump"
    }

    private fun px(value: Float) = value / outputWidth * 2f - 1f
    private fun py(value: Float) = 1f - value / outputHeight * 2f
    private fun frameIntervalUs() = 1_000_000L / outputFps.coerceAtLeast(1)

    private fun floatBuffer(vararg values: Float): FloatBuffer =
        ByteBuffer.allocateDirect(values.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply { put(values); position(0) }

    private fun checkEgl(ok: Boolean, stage: String) {
        check(ok) { "EGL $stage 失败，code=0x${EGL14.eglGetError().toString(16)}" }
    }

    private fun log(message: String) {
        diagnostic?.invoke(message)
        Log.i(TAG, message)
    }

    private companion object {
        const val TAG = "CarLifePanelComposer"
        const val MIME_AVC = "video/avc"
        const val EGL_RECORDABLE_ANDROID = 0x3142
        const val ENCODER_BIT_RATE = 8_000_000
        const val INPUT_QUEUE_CAPACITY = 8
        const val INPUT_DROP_LOG_INTERVAL = 30L
        val START_CODE = byteArrayOf(0, 0, 0, 1)
        val IDENTITY_MATRIX = floatArrayOf(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            0f, 0f, 0f, 1f,
        )
    }
}
