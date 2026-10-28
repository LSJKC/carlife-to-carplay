package com.carplay.adapter

import android.content.Context
import android.content.Intent

data class ProjectionGrant(val resultCode: Int, val data: Intent)
data class ProjectionCapabilities(val video: Boolean, val audio: Boolean, val input: Boolean, val microphone: Boolean, val detail: String)

interface ProjectionObserver {
    fun videoConfig(avcC: ByteArray)

    /**
     * One media packet's payload, already in whatever the car negotiated.
     *
     * [pcm] is true when the payload is raw little-endian 16-bit PCM (LPCM
     * stream) rather than one AAC access unit; a source that can hand over PCM
     * must do so instead of encoding AAC, because this head unit plays PCM and
     * produced no sound at all for AAC.
     */
    fun audioFrame(bytes: ByteArray, sampleTimeUs: Long, pcm: Boolean = false)
    fun diagnostic(message: String)
    fun failed(error: Throwable)
}

/**
 * 厂商/平台投屏适配器接口。
 * Vivo (Jovi InCar)、小米 (CarWith)、通用 Android (MediaProjection) 均实现此接口。
 */
interface PhoneProjectionAdapter {
    val capabilities: ProjectionCapabilities
    fun setObserver(observer: ProjectionObserver)
    fun setGrant(grant: ProjectionGrant)
    /**
     * Reports the media format the car accepted.
     *
     * [pcm] is true when the car took an LPCM stream, in which case the source
     * must hand over raw PCM through [ProjectionObserver.audioFrame] instead of
     * encoding AAC.
     */
    fun configureAudio(sampleRate: Int, channels: Int, pcm: Boolean = false)
    fun requestKeyFrame()
    /**
     * A car-side screen restore is a distinct attempt, not a duplicate of the
     * previous handover. Sources that can rebuild their encoder may use this
     * to retry even if the prior IDR never arrived.
     */
    fun requestScreenRestoreKeyFrame() = requestKeyFrame()
    fun deliverMicrophone(pcm: ByteArray, sampleRate: Int, channels: Int): Boolean
    /** 适配器名称，例如 "Vivo Jovi InCar", "Xiaomi CarWith", "Generic Android" */
    val name: String

    /**
     * 该来源是否需要系统录屏授权。
     *
     * 屏幕捕获类适配器需要；由厂商宿主自行渲染并输出编码流的来源不需要，
     * 此时不应向用户弹出录屏对话框。
     */
    val requiresScreenGrant: Boolean get() = true

    /** 是否支持当前设备运行环境 */
    fun isSupported(context: Context): Boolean

    /**
     * 会话开始时的预热钩子。
     *
     * 厂商来源（例如 CarLife）需要提前把宿主进程拉起来：它的 TCP 监听只有在
     * 进程存在时才建立，等到 AirPlay 协商完再去启动就已经太晚，连接会被直接
     * 拒绝。默认不做任何事。
     */
    fun warmUp(context: Context) = Unit

    /** 准备投屏捕获通道 */
    suspend fun prepare(context: Context, width: Int, height: Int, fps: Int): Boolean

    /** 开始向 H.264 编码器/AirPlay 发送帧数据 */
    suspend fun startProjection(onFrameEncoded: (data: ByteArray, ptsUs: Long, isKeyFrame: Boolean) -> Unit)

    /** 处理车机回传的触控事件 (CarPlay HID / Touch) */
    fun onTouchEvent(action: Int, x: Float, y: Float, pointerId: Int)

    /** 处理车机旋钮/按键事件 */
    fun onKeyEvent(keyCode: Int, isDown: Boolean)

    /** 停止并释放投屏通道 */
    suspend fun stopProjection()

    /**
     * Tells the source that the session ended because the transport failed,
     * not because the driver stopped projecting.
     *
     * The difference decides whether a source is allowed to stay warm. Tearing
     * a source down completely is right when the user is done, but after a
     * disconnect the head unit will be back in seconds and a cold source start
     * spends most of its own watchdog window getting ready again. Sources that
     * can cheaply survive implement this; the default is to do nothing.
     */
    fun noteAbnormalEnd() = Unit
}
