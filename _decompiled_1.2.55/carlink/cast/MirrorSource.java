package com.carlink.cast;

import android.content.Context;
import android.hardware.display.VirtualDisplay;
import android.media.AudioFormat;
import android.media.AudioPlaybackCaptureConfiguration;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;
import android.view.Surface;
import com.carplay.adapter.PhoneProjectionAdapter;
import com.carplay.adapter.ProjectionCapabilities;
import com.carplay.adapter.ProjectionGrant;
import com.carplay.adapter.ProjectionObserver;
import com.carplay.airplay.AirPlayIapTunnelStream;
import com.carplay.sender.CaptureGeometry;
import com.carplay.sender.VideoPackets;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: MirrorSource.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000 \u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\n\b\u0000\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0010\u00100\u001a\u0002012\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u0010\u00102\u001a\u0002012\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J\u0010\u00103\u001a\u00020\u000b2\u0006\u0010\u0002\u001a\u00020\u0003H\u0016J \u00104\u001a\u0002012\u0006\u00105\u001a\u00020#2\u0006\u00106\u001a\u00020#2\u0006\u00107\u001a\u00020\u000bH\u0016J\b\u00108\u001a\u000201H\u0016J.\u00109\u001a\u00020\u000b2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020#2\u0006\u0010%\u001a\u00020#H\u0096@¢\u0006\u0002\u0010:J\u000e\u0010;\u001a\u0002012\u0006\u0010<\u001a\u00020\u000bJ.\u0010=\u001a\u0002012\u001e\u0010>\u001a\u001a\u0012\u0004\u0012\u00020@\u0012\u0004\u0012\u00020A\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u0002010?H\u0096@¢\u0006\u0002\u0010BJ\u0018\u0010C\u001a\u0002012\u0006\u0010D\u001a\u00020\u00172\u0006\u0010E\u001a\u00020FH\u0002J\u0010\u0010G\u001a\u0002012\u0006\u0010H\u001a\u00020,H\u0002J(\u0010I\u001a\u0002012\u0006\u0010J\u001a\u00020#2\u0006\u0010K\u001a\u00020L2\u0006\u0010M\u001a\u00020L2\u0006\u0010N\u001a\u00020#H\u0016J\u0018\u0010O\u001a\u0002012\u0006\u0010P\u001a\u00020#2\u0006\u0010Q\u001a\u00020\u000bH\u0016J \u0010R\u001a\u00020\u000b2\u0006\u00107\u001a\u00020@2\u0006\u00105\u001a\u00020#2\u0006\u00106\u001a\u00020#H\u0016J\u000e\u0010S\u001a\u000201H\u0096@¢\u0006\u0002\u0010TJ\b\u0010U\u001a\u000201H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\u00020\u0007X\u0096D¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u000bX\u0096D¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0014\u0010\u000e\u001a\u00020\u000f8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u001dX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u001fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020#X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020#X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010&\u001a\u0004\u0018\u00010'X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020#X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010+\u001a\u0004\u0018\u00010,X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010-\u001a\u0004\u0018\u00010\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010.\u001a\u0004\u0018\u00010/X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006V"}, d2 = {"Lcom/carlink/cast/MirrorSource;", "Lcom/carplay/adapter/PhoneProjectionAdapter;", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "name", "", "getName", "()Ljava/lang/String;", "requiresScreenGrant", "", "getRequiresScreenGrant", "()Z", "capabilities", "Lcom/carplay/adapter/ProjectionCapabilities;", "getCapabilities", "()Lcom/carplay/adapter/ProjectionCapabilities;", "observer", "Lcom/carplay/adapter/ProjectionObserver;", "grant", "Lcom/carplay/adapter/ProjectionGrant;", "projection", "Landroid/media/projection/MediaProjection;", "codec", "Landroid/media/MediaCodec;", "inputSurface", "Landroid/view/Surface;", "thread", "Landroid/os/HandlerThread;", "virtualDisplay", "Landroid/hardware/display/VirtualDisplay;", "running", "Ljava/util/concurrent/atomic/AtomicBoolean;", "width", "", "height", "fps", "geometry", "Lcom/carplay/sender/CaptureGeometry;", "audioEnabled", "audioRate", "negotiatedPcm", CastSettings.KEY_AUDIO, "Landroid/media/AudioRecord;", "audioCodec", "audioThread", "Ljava/lang/Thread;", "setObserver", "", "setGrant", "isSupported", "configureAudio", "sampleRate", "channels", "pcm", "requestKeyFrame", "prepare", "(Landroid/content/Context;IIILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "enableAudio", "enabled", "startProjection", "onFrameEncoded", "Lkotlin/Function3;", "", "", "(Lkotlin/jvm/functions/Function3;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startAudio", "media", "handler", "Landroid/os/Handler;", "startPcmAudio", "record", "onTouchEvent", "action", "x", "", "y", "pointerId", "onKeyEvent", "keyCode", "isDown", "deliverMicrophone", "stopProjection", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "release", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MirrorSource implements PhoneProjectionAdapter {
    private AudioRecord audio;
    private MediaCodec audioCodec;
    private boolean audioEnabled;
    private int audioRate;
    private Thread audioThread;
    private MediaCodec codec;
    private final Context context;
    private int fps;
    private CaptureGeometry geometry;
    private ProjectionGrant grant;
    private int height;
    private Surface inputSurface;
    private final String name;
    private volatile boolean negotiatedPcm;
    private ProjectionObserver observer;
    private MediaProjection projection;
    private final boolean requiresScreenGrant;
    private final AtomicBoolean running;
    private HandlerThread thread;
    private VirtualDisplay virtualDisplay;
    private int width;

    public MirrorSource(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
        this.name = "屏幕镜像";
        this.requiresScreenGrant = true;
        this.running = new AtomicBoolean(false);
        this.fps = 30;
        this.audioRate = 48000;
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public void requestScreenRestoreKeyFrame() {
        super.requestScreenRestoreKeyFrame();
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public void warmUp(Context context) {
        super.warmUp(context);
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public String getName() {
        return this.name;
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public boolean getRequiresScreenGrant() {
        return this.requiresScreenGrant;
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public ProjectionCapabilities getCapabilities() {
        return new ProjectionCapabilities(true, true, TouchBridge.INSTANCE.available(), false, "镜像手机屏幕，需要每次授权录屏；触控回传需要无障碍服务");
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public void setObserver(ProjectionObserver observer) {
        Intrinsics.checkNotNullParameter(observer, "observer");
        this.observer = observer;
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public void setGrant(ProjectionGrant grant) {
        Intrinsics.checkNotNullParameter(grant, "grant");
        this.grant = grant;
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public boolean isSupported(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return context.getSystemService(MediaProjectionManager.class) != null;
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public void configureAudio(int sampleRate, int channels, boolean pcm) {
        if (CollectionsKt.listOf((Object[]) new Integer[]{44100, 48000}).contains(Integer.valueOf(sampleRate))) {
            this.audioRate = sampleRate;
        }
        this.negotiatedPcm = pcm;
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public void requestKeyFrame() {
        Unit unit;
        try {
            Result.Companion companion = Result.INSTANCE;
            MediaCodec mediaCodec = this.codec;
            if (mediaCodec != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("request-sync", 0);
                mediaCodec.setParameters(bundle);
                unit = Unit.INSTANCE;
            } else {
                unit = null;
            }
            Result.m159constructorimpl(unit);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public Object prepare(Context context, int width, int height, int fps, Continuation<? super Boolean> continuation) {
        if (!(width > 0 && height > 0 && width % 2 == 0 && height % 2 == 0)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        this.width = width;
        this.height = height;
        this.fps = RangesKt.coerceIn(fps, 1, 60);
        return Boxing.boxBoolean(true);
    }

    public final void enableAudio(boolean enabled) {
        this.audioEnabled = enabled;
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public Object startProjection(final Function3<? super byte[], ? super Long, ? super Boolean, Unit> function3, Continuation<? super Unit> continuation) throws IOException {
        if (!this.running.compareAndSet(false, true)) {
            throw new IllegalStateException("镜像已启动".toString());
        }
        ProjectionGrant consent = this.grant;
        if (consent == null) {
            throw new IllegalStateException("请先授权屏幕投射".toString());
        }
        MediaProjectionManager manager = (MediaProjectionManager) this.context.getSystemService(MediaProjectionManager.class);
        MediaProjection media = manager.getMediaProjection(consent.getResultCode(), consent.getData());
        if (media == null) {
            throw new IllegalStateException("屏幕投射授权被拒绝".toString());
        }
        this.projection = media;
        this.grant = null;
        HandlerThread handlerThread = new HandlerThread("carlink-mirror");
        handlerThread.start();
        this.thread = handlerThread;
        Handler handler = new Handler(handlerThread.getLooper());
        media.registerCallback(new MediaProjection.Callback() { // from class: com.carlink.cast.MirrorSource.startProjection.3
            @Override // android.media.projection.MediaProjection.Callback
            public void onStop() {
                ProjectionObserver projectionObserver;
                if (!MirrorSource.this.running.get() || (projectionObserver = MirrorSource.this.observer) == null) {
                    return;
                }
                projectionObserver.failed(new IllegalStateException("系统已停止屏幕投射，请重新授权"));
            }
        }, handler);
        DisplayMetrics bounds = this.context.getResources().getDisplayMetrics();
        this.geometry = new CaptureGeometry(bounds.widthPixels, bounds.heightPixels, this.width, this.height);
        MediaCodec encoder = MediaCodec.createEncoderByType(CarLifePanelCompositor.MIME_AVC);
        Intrinsics.checkNotNullExpressionValue(encoder, "createEncoderByType(...)");
        this.codec = encoder;
        encoder.setCallback(new MediaCodec.Callback() { // from class: com.carlink.cast.MirrorSource.startProjection.4
            @Override // android.media.MediaCodec.Callback
            public void onInputBufferAvailable(MediaCodec codec, int index) {
                Intrinsics.checkNotNullParameter(codec, "codec");
            }

            @Override // android.media.MediaCodec.Callback
            public void onOutputBufferAvailable(MediaCodec codec, int index, MediaCodec.BufferInfo info) {
                Intrinsics.checkNotNullParameter(codec, "codec");
                Intrinsics.checkNotNullParameter(info, "info");
                try {
                    try {
                        if (MirrorSource.this.running.get() && info.size > 0 && (info.flags & 2) == 0) {
                            ByteBuffer buffer = codec.getOutputBuffer(index);
                            if (buffer == null) {
                                try {
                                    Result.Companion companion = Result.INSTANCE;
                                    AnonymousClass4 anonymousClass4 = this;
                                    codec.releaseOutputBuffer(index, false);
                                    Result.m159constructorimpl(Unit.INSTANCE);
                                    return;
                                } catch (Throwable th) {
                                    Result.Companion companion2 = Result.INSTANCE;
                                    Result.m159constructorimpl(ResultKt.createFailure(th));
                                    return;
                                }
                            }
                            buffer.position(info.offset);
                            buffer.limit(info.offset + info.size);
                            byte[] bytes = new byte[info.size];
                            buffer.get(bytes);
                            Function3<byte[], Long, Boolean, Unit> function4 = function3;
                            byte[] bArrLengthPrefixed = VideoPackets.INSTANCE.lengthPrefixed(bytes);
                            Long lValueOf = Long.valueOf(info.presentationTimeUs);
                            boolean z = true;
                            if ((info.flags & 1) == 0) {
                                z = false;
                            }
                            function4.invoke(bArrLengthPrefixed, lValueOf, Boolean.valueOf(z));
                            Result.Companion companion3 = Result.INSTANCE;
                            Result.m159constructorimpl(ResultKt.createFailure(th));
                            return;
                        }
                        try {
                            Result.Companion companion4 = Result.INSTANCE;
                            AnonymousClass4 anonymousClass5 = this;
                            codec.releaseOutputBuffer(index, false);
                            Result.m159constructorimpl(Unit.INSTANCE);
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    } catch (Exception error) {
                        ProjectionObserver projectionObserver = MirrorSource.this.observer;
                        if (projectionObserver != null) {
                            projectionObserver.failed(error);
                        }
                        try {
                            Result.Companion companion5 = Result.INSTANCE;
                            codec.releaseOutputBuffer(index, false);
                            Result.m159constructorimpl(Unit.INSTANCE);
                        } catch (Throwable th3) {
                            th = th3;
                        }
                    }
                } catch (Throwable th4) {
                    try {
                        Result.Companion companion6 = Result.INSTANCE;
                        codec.releaseOutputBuffer(index, false);
                        Result.m159constructorimpl(Unit.INSTANCE);
                    } catch (Throwable th5) {
                        Result.Companion companion7 = Result.INSTANCE;
                        Result.m159constructorimpl(ResultKt.createFailure(th5));
                    }
                    throw th4;
                }
            }

            @Override // android.media.MediaCodec.Callback
            public void onError(MediaCodec codec, MediaCodec.CodecException error) {
                Intrinsics.checkNotNullParameter(codec, "codec");
                Intrinsics.checkNotNullParameter(error, "error");
                ProjectionObserver projectionObserver = MirrorSource.this.observer;
                if (projectionObserver != null) {
                    projectionObserver.failed(error);
                }
            }

            @Override // android.media.MediaCodec.Callback
            public void onOutputFormatChanged(MediaCodec codec, MediaFormat format) {
                byte[] pps;
                ProjectionObserver projectionObserver;
                Intrinsics.checkNotNullParameter(codec, "codec");
                Intrinsics.checkNotNullParameter(format, "format");
                try {
                    byte[] sps = onOutputFormatChanged$field(format, "csd-0");
                    if (sps != null && (pps = onOutputFormatChanged$field(format, "csd-1")) != null && (projectionObserver = MirrorSource.this.observer) != null) {
                        projectionObserver.videoConfig(VideoPackets.INSTANCE.avcC(sps, pps));
                    }
                } catch (Exception error) {
                    ProjectionObserver projectionObserver2 = MirrorSource.this.observer;
                    if (projectionObserver2 != null) {
                        projectionObserver2.failed(error);
                    }
                }
            }

            private static final byte[] onOutputFormatChanged$field(MediaFormat $format, String name) {
                ByteBuffer byteBufferDuplicate;
                ByteBuffer byteBuffer = $format.getByteBuffer(name);
                if (byteBuffer == null || (byteBufferDuplicate = byteBuffer.duplicate()) == null) {
                    return null;
                }
                byte[] bArr = new byte[byteBufferDuplicate.remaining()];
                byteBufferDuplicate.get(bArr);
                return bArr;
            }
        }, handler);
        MediaFormat mediaFormatCreateVideoFormat = MediaFormat.createVideoFormat(CarLifePanelCompositor.MIME_AVC, this.width, this.height);
        mediaFormatCreateVideoFormat.setInteger("color-format", 2130708361);
        mediaFormatCreateVideoFormat.setInteger("bitrate", (int) RangesKt.coerceIn(((((long) this.width) * ((long) this.height)) * ((long) this.fps)) / 8, 2000000L, 24000000L));
        mediaFormatCreateVideoFormat.setInteger("frame-rate", this.fps);
        mediaFormatCreateVideoFormat.setInteger("i-frame-interval", 1);
        mediaFormatCreateVideoFormat.setFloat("max-fps-to-encoder", this.fps);
        Intrinsics.checkNotNullExpressionValue(mediaFormatCreateVideoFormat, "apply(...)");
        encoder.configure(mediaFormatCreateVideoFormat, (Surface) null, (MediaCrypto) null, 1);
        Surface surface = encoder.createInputSurface();
        Intrinsics.checkNotNullExpressionValue(surface, "createInputSurface(...)");
        this.inputSurface = surface;
        encoder.start();
        this.virtualDisplay = media.createVirtualDisplay("CarLink Mirror", this.width, this.height, this.context.getResources().getDisplayMetrics().densityDpi, 16, surface, null, handler);
        if (this.audioEnabled) {
            startAudio(media, handler);
        }
        ProjectionObserver projectionObserver = this.observer;
        if (projectionObserver != null) {
            projectionObserver.diagnostic("镜像编码已启动：" + this.width + "×" + this.height + "@" + this.fps);
        }
        return Unit.INSTANCE;
    }

    private final void startAudio(MediaProjection media, Handler handler) throws IOException {
        if (this.context.checkSelfPermission("android.permission.RECORD_AUDIO") != 0) {
            ProjectionObserver projectionObserver = this.observer;
            if (projectionObserver != null) {
                projectionObserver.diagnostic("未授予录音权限，跳过音频");
                return;
            }
            return;
        }
        AudioPlaybackCaptureConfiguration config = new AudioPlaybackCaptureConfiguration.Builder(media).addMatchingUsage(1).addMatchingUsage(14).build();
        Intrinsics.checkNotNullExpressionValue(config, "build(...)");
        final int buffer = Math.max(8192, AudioRecord.getMinBufferSize(this.audioRate, 12, 2));
        final AudioRecord record = new AudioRecord.Builder().setAudioFormat(new AudioFormat.Builder().setSampleRate(this.audioRate).setEncoding(2).setChannelMask(12).build()).setBufferSizeInBytes(buffer).setAudioPlaybackCaptureConfig(config).build();
        this.audio = record;
        if (record.getState() != 1) {
            ProjectionObserver projectionObserver2 = this.observer;
            if (projectionObserver2 != null) {
                projectionObserver2.diagnostic("音频捕获初始化失败");
                return;
            }
            return;
        }
        if (this.negotiatedPcm) {
            Intrinsics.checkNotNull(record);
            startPcmAudio(record);
            return;
        }
        final MediaCodec encoder = MediaCodec.createEncoderByType("audio/mp4a-latm");
        Intrinsics.checkNotNullExpressionValue(encoder, "createEncoderByType(...)");
        this.audioCodec = encoder;
        MediaFormat mediaFormatCreateAudioFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", this.audioRate, 2);
        mediaFormatCreateAudioFormat.setInteger("aac-profile", 2);
        mediaFormatCreateAudioFormat.setInteger("bitrate", 128000);
        mediaFormatCreateAudioFormat.setInteger("max-input-size", buffer);
        encoder.configure(mediaFormatCreateAudioFormat, (Surface) null, (MediaCrypto) null, 1);
        encoder.start();
        record.startRecording();
        Thread thread = new Thread(new Runnable() { // from class: com.carlink.cast.MirrorSource$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MirrorSource.startAudio$lambda$7(this.f$0, encoder, record, buffer);
            }
        }, "carlink-mirror-audio");
        thread.setDaemon(true);
        thread.start();
        this.audioThread = thread;
    }

    static final void startAudio$lambda$7(MirrorSource this$0, MediaCodec $encoder, AudioRecord $record, int $buffer) {
        MirrorSource mirrorSource = this$0;
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        long samples = 0;
        while (mirrorSource.running.get()) {
            try {
                int index = $encoder.dequeueInputBuffer(AirPlayIapTunnelStream.EVENT_READY_TIMEOUT_MILLIS);
                if (index >= 0) {
                    try {
                        ByteBuffer input = $encoder.getInputBuffer(index);
                        if (input != null) {
                            input.clear();
                            try {
                                try {
                                    int read = $record.read(input, Math.min(input.capacity(), $buffer), 0);
                                    if (read > 0) {
                                        $encoder.queueInputBuffer(index, 0, read, (1000000 * samples) / ((long) mirrorSource.audioRate), 0);
                                        samples += (long) (read / 4);
                                    }
                                } catch (Exception e) {
                                    return;
                                }
                            } catch (Exception e2) {
                            }
                        }
                    } catch (Exception e3) {
                    }
                }
                int out = $encoder.dequeueOutputBuffer(info, 0L);
                while (out >= 0) {
                    if (info.size > 0 && (info.flags & 2) == 0) {
                        byte[] bytes = new byte[info.size];
                        ByteBuffer outputBuffer = $encoder.getOutputBuffer(out);
                        if (outputBuffer != null) {
                            outputBuffer.position(info.offset);
                            outputBuffer.get(bytes);
                        }
                        try {
                            ProjectionObserver projectionObserver = this$0.observer;
                            if (projectionObserver != null) {
                                ProjectionObserver.audioFrame$default(projectionObserver, bytes, info.presentationTimeUs, false, 4, null);
                            }
                        } catch (Exception e4) {
                            return;
                        }
                    }
                    $encoder.releaseOutputBuffer(out, false);
                    out = $encoder.dequeueOutputBuffer(info, 0L);
                }
                mirrorSource = this$0;
            } catch (Exception e5) {
                return;
            }
        }
    }

    private final void startPcmAudio(final AudioRecord record) {
        record.startRecording();
        Thread thread = new Thread(new Runnable() { // from class: com.carlink.cast.MirrorSource$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                MirrorSource.startPcmAudio$lambda$9(this.f$0, record);
            }
        }, "carlink-mirror-pcm");
        thread.setDaemon(true);
        thread.start();
        this.audioThread = thread;
        ProjectionObserver projectionObserver = this.observer;
        if (projectionObserver != null) {
            projectionObserver.diagnostic("镜像音频：LPCM " + this.audioRate + " Hz，直接透传");
        }
    }

    static final void startPcmAudio$lambda$9(MirrorSource this$0, AudioRecord $record) {
        byte[] pcm;
        byte[] buffer = new byte[4096];
        long samples = 0;
        while (this$0.running.get()) {
            try {
                int read = $record.read(buffer, 0, buffer.length, 0);
                if (read > 0) {
                    if (read == buffer.length) {
                        pcm = Arrays.copyOf(buffer, buffer.length);
                        Intrinsics.checkNotNullExpressionValue(pcm, "copyOf(...)");
                    } else {
                        pcm = Arrays.copyOf(buffer, read);
                        Intrinsics.checkNotNullExpressionValue(pcm, "copyOf(...)");
                    }
                    ProjectionObserver projectionObserver = this$0.observer;
                    if (projectionObserver != null) {
                        projectionObserver.audioFrame(pcm, (1000000 * samples) / ((long) this$0.audioRate), true);
                    }
                    samples += (long) (read / 4);
                }
            } catch (Exception e) {
                return;
            }
        }
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public void onTouchEvent(int action, float x, float y, int pointerId) {
        Pair<Float, Float> pairPoint;
        CaptureGeometry captureGeometry = this.geometry;
        if (captureGeometry == null || (pairPoint = captureGeometry.point(x, y)) == null) {
            return;
        }
        TouchBridge.INSTANCE.dispatch(action, pairPoint.getFirst().floatValue(), pairPoint.getSecond().floatValue(), pointerId);
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public void onKeyEvent(int keyCode, boolean isDown) {
        TouchBridge.INSTANCE.dispatchKey(keyCode, isDown);
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public boolean deliverMicrophone(byte[] pcm, int sampleRate, int channels) {
        Intrinsics.checkNotNullParameter(pcm, "pcm");
        return false;
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public Object stopProjection(Continuation<? super Unit> continuation) {
        release();
        return Unit.INSTANCE;
    }

    private final void release() {
        Unit unit;
        Unit unit2;
        Unit unit3;
        Unit unit4;
        Unit unit5;
        Unit unit6;
        Unit unit7;
        if (this.running.compareAndSet(true, false)) {
            try {
                Result.Companion companion = Result.INSTANCE;
                AudioRecord audioRecord = this.audio;
                if (audioRecord != null) {
                    audioRecord.stop();
                    unit7 = Unit.INSTANCE;
                } else {
                    unit7 = null;
                }
                Result.m159constructorimpl(unit7);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            try {
                Result.Companion companion3 = Result.INSTANCE;
                AudioRecord audioRecord2 = this.audio;
                if (audioRecord2 != null) {
                    audioRecord2.release();
                    unit6 = Unit.INSTANCE;
                } else {
                    unit6 = null;
                }
                Result.m159constructorimpl(unit6);
            } catch (Throwable th2) {
                Result.Companion companion4 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th2));
            }
            this.audio = null;
            try {
                Result.Companion companion5 = Result.INSTANCE;
                MediaCodec mediaCodec = this.audioCodec;
                if (mediaCodec != null) {
                    mediaCodec.stop();
                    unit5 = Unit.INSTANCE;
                } else {
                    unit5 = null;
                }
                Result.m159constructorimpl(unit5);
            } catch (Throwable th3) {
                Result.Companion companion6 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th3));
            }
            try {
                Result.Companion companion7 = Result.INSTANCE;
                MediaCodec mediaCodec2 = this.audioCodec;
                if (mediaCodec2 != null) {
                    mediaCodec2.release();
                    unit4 = Unit.INSTANCE;
                } else {
                    unit4 = null;
                }
                Result.m159constructorimpl(unit4);
            } catch (Throwable th4) {
                Result.Companion companion8 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th4));
            }
            this.audioCodec = null;
            VirtualDisplay virtualDisplay = this.virtualDisplay;
            if (virtualDisplay != null) {
                virtualDisplay.release();
            }
            this.virtualDisplay = null;
            try {
                Result.Companion companion9 = Result.INSTANCE;
                MediaCodec mediaCodec3 = this.codec;
                if (mediaCodec3 != null) {
                    mediaCodec3.stop();
                    unit3 = Unit.INSTANCE;
                } else {
                    unit3 = null;
                }
                Result.m159constructorimpl(unit3);
            } catch (Throwable th5) {
                Result.Companion companion10 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th5));
            }
            try {
                Result.Companion companion11 = Result.INSTANCE;
                MediaCodec mediaCodec4 = this.codec;
                if (mediaCodec4 != null) {
                    mediaCodec4.release();
                    unit2 = Unit.INSTANCE;
                } else {
                    unit2 = null;
                }
                Result.m159constructorimpl(unit2);
            } catch (Throwable th6) {
                Result.Companion companion12 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th6));
            }
            this.codec = null;
            Surface surface = this.inputSurface;
            if (surface != null) {
                surface.release();
            }
            this.inputSurface = null;
            try {
                Result.Companion companion13 = Result.INSTANCE;
                MediaProjection mediaProjection = this.projection;
                if (mediaProjection != null) {
                    mediaProjection.stop();
                    unit = Unit.INSTANCE;
                } else {
                    unit = null;
                }
                Result.m159constructorimpl(unit);
            } catch (Throwable th7) {
                Result.Companion companion14 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th7));
            }
            this.projection = null;
            HandlerThread handlerThread = this.thread;
            if (handlerThread != null) {
                handlerThread.quitSafely();
            }
            this.thread = null;
        }
    }
}
