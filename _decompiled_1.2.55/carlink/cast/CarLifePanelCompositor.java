package com.carlink.cast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import com.carplay.adapter.carlife.CarLifePanel;
import com.carplay.adapter.carlife.CarLifeVideoProcessor;
import com.carplay.sender.VideoPackets;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: CarLifePanelCompositor.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000Ü\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0014\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u001d\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u001c\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\n\b\u0000\u0018\u0000 ©\u00012\u00020\u0001:\u0006§\u0001¨\u0001©\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u000e\u0010U\u001a\u00020\u001e2\u0006\u0010V\u001a\u00020\u0003J\u000e\u0010W\u001a\u00020\u001e2\u0006\u0010V\u001a\u00020\nJ\u0092\u0001\u0010X\u001a\u00020\u001e2\u0006\u0010+\u001a\u00020,2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u00182\u0012\u0010Y\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001e0\u001c2\u001e\u0010Z\u001a\u001a\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u001e0 2\u0012\u0010[\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u001e0\u001c2\u0012\u0010\\\u001a\u000e\u0012\u0004\u0012\u00020$\u0012\u0004\u0012\u00020\u001e0\u001c2\f\u0010]\u001a\b\u0012\u0004\u0012\u00020\u001e0&H\u0016J(\u0010^\u001a\u00020\u001e2\u0006\u0010_\u001a\u00020\u00182\u0006\u0010`\u001a\u00020\u00182\u0006\u0010a\u001a\u00020\u00182\u0006\u0010b\u001a\u00020\u001dH\u0016J \u0010c\u001a\u00020\u001e2\u0006\u0010d\u001a\u00020\u001d2\u0006\u0010e\u001a\u00020\u00112\u0006\u0010f\u001a\u00020\u0013H\u0016J&\u0010g\u001a\u0010\u0012\u0004\u0012\u00020i\u0012\u0004\u0012\u00020i\u0018\u00010h2\u0006\u0010j\u001a\u00020i2\u0006\u0010k\u001a\u00020iH\u0016J\b\u0010l\u001a\u00020\u001eH\u0016J\u0010\u0010m\u001a\u00020\u001e2\u0006\u0010n\u001a\u00020oH\u0002J\b\u0010p\u001a\u00020\u001eH\u0002J\b\u0010r\u001a\u00020\u001eH\u0002J\b\u0010s\u001a\u00020\u001eH\u0002J\u0010\u0010t\u001a\u00020\u001e2\u0006\u0010u\u001a\u00020\u0018H\u0002J\u0012\u0010v\u001a\u00020\u001e2\b\b\u0002\u0010w\u001a\u00020\u0013H\u0002J\b\u0010x\u001a\u00020\u001eH\u0002J\u0010\u0010z\u001a\u00020\u001e2\u0006\u0010{\u001a\u00020\u000fH\u0002J\u0010\u0010|\u001a\u00020\u00132\u0006\u0010}\u001a\u00020\rH\u0002J\b\u0010~\u001a\u00020\u001eH\u0002J\u0010\u0010\u007f\u001a\u00020\u001e2\u0006\u0010e\u001a\u00020\u0011H\u0002J\t\u0010\u0080\u0001\u001a\u00020\u001eH\u0002J\t\u0010\u0081\u0001\u001a\u00020\u001eH\u0002J?\u0010\u0082\u0001\u001a\u00020\u001e2\u0007\u0010\u0083\u0001\u001a\u00020\u00182\u0007\u0010\u0084\u0001\u001a\u00020i2\u0007\u0010\u0085\u0001\u001a\u00020i2\u0007\u0010\u0086\u0001\u001a\u00020i2\u0007\u0010\u0087\u0001\u001a\u00020i2\u0007\u0010\u0088\u0001\u001a\u00020EH\u0002J\t\u0010\u0089\u0001\u001a\u00020\u001eH\u0002J\u0015\u0010\u008a\u0001\u001a\u0004\u0018\u00010\u001d2\b\u0010\u008b\u0001\u001a\u00030\u008c\u0001H\u0002J\u0014\u0010\u008d\u0001\u001a\u0004\u0018\u00010\u001d2\u0007\u0010\u008e\u0001\u001a\u00020\u001dH\u0002J\u001d\u0010\u008f\u0001\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001d0h2\u0006\u0010b\u001a\u00020\u001dH\u0002J\u001b\u0010\u0090\u0001\u001a\u00020\u001d2\u0007\u0010\u008e\u0001\u001a\u00020\u001d2\u0007\u0010\u0091\u0001\u001a\u00020\u0018H\u0002J\u0012\u0010\u0092\u0001\u001a\u00020\u001e2\u0007\u0010\u0093\u0001\u001a\u00020\u001dH\u0002J\t\u0010\u0094\u0001\u001a\u00020\u001eH\u0002J\"\u0010\u0095\u0001\u001a\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u000f\u0012\u0006\u0012\u0004\u0018\u00010\r0h2\u0007\u0010\u0096\u0001\u001a\u00020\u0013H\u0002J\t\u0010\u0097\u0001\u001a\u00020\u001eH\u0002J\t\u0010\u0098\u0001\u001a\u00020\u001eH\u0002J\u0012\u0010\u0099\u0001\u001a\u00020\u00182\u0007\u0010\u009a\u0001\u001a\u00020\u0013H\u0002J\t\u0010\u009b\u0001\u001a\u00020\"H\u0002J\u0011\u0010\u009c\u0001\u001a\u00020i2\u0006\u0010V\u001a\u00020iH\u0002J\u0011\u0010\u009d\u0001\u001a\u00020i2\u0006\u0010V\u001a\u00020iH\u0002J\t\u0010\u009e\u0001\u001a\u00020\u0011H\u0002J\u0017\u0010\u009f\u0001\u001a\u00030 \u00012\u000b\u0010¡\u0001\u001a\u00020E\"\u00020iH\u0002J\u001b\u0010¢\u0001\u001a\u00020\u001e2\u0007\u0010£\u0001\u001a\u00020\u00132\u0007\u0010¤\u0001\u001a\u00020\"H\u0002J\u0012\u0010¥\u0001\u001a\u00020\u001e2\u0007\u0010¦\u0001\u001a\u00020\"H\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\u001b\u001a\u0010\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001e\u0018\u00010\u001cX\u0082\u000e¢\u0006\u0002\n\u0000R(\u0010\u001f\u001a\u001c\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u001e\u0018\u00010 X\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010!\u001a\u0010\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u001e\u0018\u00010\u001cX\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010#\u001a\u0010\u0012\u0004\u0012\u00020$\u0012\u0004\u0012\u00020\u001e\u0018\u00010\u001cX\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010%\u001a\n\u0012\u0004\u0012\u00020\u001e\u0018\u00010&X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010)\u001a\u0004\u0018\u00010*X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010+\u001a\u0004\u0018\u00010,X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010-\u001a\u0004\u0018\u00010.X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010/\u001a\u0004\u0018\u00010.X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u00100\u001a\u0004\u0018\u000101X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u00102\u001a\u0004\u0018\u000103X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00104\u001a\u000205X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00106\u001a\u000207X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00108\u001a\u000209X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010?\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010@\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010B\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010C\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010D\u001a\u00020EX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010F\u001a\u00020\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010G\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010I\u001a\u00020JX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010K\u001a\u00020\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010L\u001a\u0004\u0018\u00010\u001dX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010M\u001a\u00020\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010N\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010O\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010P\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010Q\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010R\u001a\u00020\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010S\u001a\u00020\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010T\u001a\u00020\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010q\u001a\u0004\u0018\u000103X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010y\u001a\u00020EX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006ª\u0001"}, d2 = {"Lcom/carlink/cast/CarLifePanelCompositor;", "Lcom/carplay/adapter/carlife/CarLifeVideoProcessor;", "initialPanel", "Lcom/carplay/adapter/carlife/CarLifePanel;", "<init>", "(Lcom/carplay/adapter/carlife/CarLifePanel;)V", "lock", "Ljava/lang/Object;", "panel", "safeAreaPolicy", "Lcom/carlink/cast/CarPanelGeometry$SafeAreaPolicy;", "frames", "Ljava/util/ArrayDeque;", "Lcom/carlink/cast/CarLifePanelCompositor$InputFrame;", "pendingFormat", "Lcom/carlink/cast/CarLifePanelCompositor$InputFormat;", "inputGeneration", "", "waitingForInputKeyFrame", "", "droppedInputFrames", "running", "Ljava/util/concurrent/atomic/AtomicBoolean;", "outputWidth", "", "outputHeight", "outputFps", "outputConfig", "Lkotlin/Function1;", "", "", "outputFrame", "Lkotlin/Function3;", "diagnostic", "", "failed", "", "needKeyFrame", "Lkotlin/Function0;", "latestTouchWidth", "latestTouchHeight", "worker", "Ljava/lang/Thread;", "context", "Landroid/content/Context;", "encoder", "Landroid/media/MediaCodec;", "decoder", "decoderSurfaceTexture", "Landroid/graphics/SurfaceTexture;", "decoderSurface", "Landroid/view/Surface;", "eglDisplay", "Landroid/opengl/EGLDisplay;", "eglContext", "Landroid/opengl/EGLContext;", "eglSurface", "Landroid/opengl/EGLSurface;", "videoTexture", "wallpaperTexture", "oesProgram", "bitmapProgram", "oesPositionLocation", "oesTextureLocation", "oesMatrixLocation", "bitmapPositionLocation", "bitmapTextureLocation", "bitmapMatrixLocation", "videoMatrix", "", "videoHasFrame", "inputWidth", "inputHeight", "videoPlacement", "Lcom/carlink/cast/CarPanelGeometry$VideoPlacement;", "videoFrameAvailable", "outputAvcC", "requestOutputSync", "nextOutputPtsUs", "lastWallpaperRevision", "renderedFrames", "encodedFrames", "firstOutputFormatLogged", "outputConfigWarningLogged", "encoderStallWarningLogged", "updatePanel", "value", "updateSafeAreaPolicy", "start", "onConfig", "onFrame", "onDiagnostic", "onFailure", "onNeedKeyFrame", "configureInput", "width", "height", "fps", "avcC", "offerFrame", "accessUnit", "ptsUs", "keyFrame", "mapTouchToSource", "Lkotlin/Pair;", "", "panelX", "panelY", "stop", "runComposer", "ready", "Ljava/util/concurrent/CountDownLatch;", "initializeEncoder", "encoderInputSurface", "initializeEgl", "initializeTextures", "setTextureParameters", "target", "refreshWallpaperIfChanged", "force", "resetWallpaperTexture", "wallpaperUv", "configureDecoder", "input", "feedDecoder", "frame", "drainDecoder", "renderFrame", "drawBitmap", "drawVideo", "drawRect", "program", "left", "bottom", "right", "top", "textureCoordinates", "drainEncoder", "configFromFormat", "format", "Landroid/media/MediaFormat;", "findAvcC", "bytes", "parseAvcC", "readAvcNal", "offset", "publishConfig", "config", "requestEncoderSyncFrame", "takeInput", "allowFrame", "releaseDecoder", "releaseResources", "buildProgram", "oes", "fragmentFloatPrecision", "px", "py", "frameIntervalUs", "floatBuffer", "Ljava/nio/FloatBuffer;", "values", "checkEgl", "ok", "stage", "log", "message", "InputFormat", "InputFrame", "Companion", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarLifePanelCompositor implements CarLifeVideoProcessor {

    @Deprecated
    public static final int EGL_RECORDABLE_ANDROID = 12610;

    @Deprecated
    public static final int ENCODER_BIT_RATE = 8000000;

    @Deprecated
    public static final long INPUT_DROP_LOG_INTERVAL = 30;

    @Deprecated
    public static final int INPUT_QUEUE_CAPACITY = 8;

    @Deprecated
    public static final String MIME_AVC = "video/avc";

    @Deprecated
    public static final String TAG = "CarLifePanelComposer";
    private int bitmapMatrixLocation;
    private int bitmapPositionLocation;
    private int bitmapProgram;
    private int bitmapTextureLocation;
    private Context context;
    private MediaCodec decoder;
    private Surface decoderSurface;
    private SurfaceTexture decoderSurfaceTexture;
    private volatile Function1<? super String, Unit> diagnostic;
    private volatile long droppedInputFrames;
    private EGLContext eglContext;
    private EGLDisplay eglDisplay;
    private EGLSurface eglSurface;
    private long encodedFrames;
    private MediaCodec encoder;
    private Surface encoderInputSurface;
    private boolean encoderStallWarningLogged;
    private volatile Function1<? super Throwable, Unit> failed;
    private boolean firstOutputFormatLogged;
    private final ArrayDeque<InputFrame> frames;
    private long inputGeneration;
    private int inputHeight;
    private int inputWidth;
    private long lastWallpaperRevision;
    private volatile int latestTouchHeight;
    private volatile int latestTouchWidth;
    private final Object lock;
    private volatile Function0<Unit> needKeyFrame;
    private long nextOutputPtsUs;
    private int oesMatrixLocation;
    private int oesPositionLocation;
    private int oesProgram;
    private int oesTextureLocation;
    private byte[] outputAvcC;
    private volatile Function1<? super byte[], Unit> outputConfig;
    private boolean outputConfigWarningLogged;
    private volatile int outputFps;
    private volatile Function3<? super byte[], ? super Long, ? super Boolean, Unit> outputFrame;
    private volatile int outputHeight;
    private volatile int outputWidth;
    private volatile CarLifePanel panel;
    private InputFormat pendingFormat;
    private long renderedFrames;
    private boolean requestOutputSync;
    private final AtomicBoolean running;
    private volatile CarPanelGeometry.SafeAreaPolicy safeAreaPolicy;
    private final AtomicBoolean videoFrameAvailable;
    private boolean videoHasFrame;
    private float[] videoMatrix;
    private CarPanelGeometry.VideoPlacement videoPlacement;
    private int videoTexture;
    private boolean waitingForInputKeyFrame;
    private int wallpaperTexture;
    private float[] wallpaperUv;
    private Thread worker;
    private static final Companion Companion = new Companion(null);
    private static final byte[] START_CODE = {0, 0, 0, 1};
    private static final float[] IDENTITY_MATRIX = {1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};

    /* JADX INFO: compiled from: CarLifePanelCompositor.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[CarPanelGeometry.VideoLayout.values().length];
            try {
                iArr[CarPanelGeometry.VideoLayout.FULL_PANEL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[CarPanelGeometry.VideoLayout.SAFE_AREA.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[CarPanelGeometry.VideoLayout.SAFE_VIEWPORT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public CarLifePanelCompositor(CarLifePanel initialPanel) {
        Intrinsics.checkNotNullParameter(initialPanel, "initialPanel");
        this.lock = new Object();
        this.panel = initialPanel;
        this.safeAreaPolicy = new CarPanelGeometry.SafeAreaPolicy(true, null, 0, 0, 0, 0, 62, null);
        this.frames = new ArrayDeque<>();
        this.waitingForInputKeyFrame = true;
        this.running = new AtomicBoolean(false);
        this.outputFps = 30;
        EGLDisplay EGL_NO_DISPLAY = EGL14.EGL_NO_DISPLAY;
        Intrinsics.checkNotNullExpressionValue(EGL_NO_DISPLAY, "EGL_NO_DISPLAY");
        this.eglDisplay = EGL_NO_DISPLAY;
        EGLContext EGL_NO_CONTEXT = EGL14.EGL_NO_CONTEXT;
        Intrinsics.checkNotNullExpressionValue(EGL_NO_CONTEXT, "EGL_NO_CONTEXT");
        this.eglContext = EGL_NO_CONTEXT;
        EGLSurface EGL_NO_SURFACE = EGL14.EGL_NO_SURFACE;
        Intrinsics.checkNotNullExpressionValue(EGL_NO_SURFACE, "EGL_NO_SURFACE");
        this.eglSurface = EGL_NO_SURFACE;
        this.oesPositionLocation = -1;
        this.oesTextureLocation = -1;
        this.oesMatrixLocation = -1;
        this.bitmapPositionLocation = -1;
        this.bitmapTextureLocation = -1;
        this.bitmapMatrixLocation = -1;
        this.videoMatrix = new float[16];
        this.videoPlacement = CarPanelGeometry.videoPlacement$default(CarPanelGeometry.INSTANCE, this.panel.getPanelWidth(), this.panel.getPanelHeight(), this.panel, null, 8, null);
        this.videoFrameAvailable = new AtomicBoolean(false);
        this.lastWallpaperRevision = Long.MIN_VALUE;
        this.wallpaperUv = new float[]{0.0f, 1.0f, 0.0f, 1.0f};
    }

    /* JADX INFO: compiled from: CarLifePanelCompositor.kt */
    @Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\t\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0004\b\n\u0010\u000bJ\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0007HÆ\u0003J\t\u0010\u0018\u001a\u00020\tHÆ\u0003J;\u0010\u0019\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\tHÆ\u0001J\u0013\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001d\u001a\u00020\u0003HÖ\u0001J\t\u0010\u001e\u001a\u00020\u001fHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013¨\u0006 "}, d2 = {"Lcom/carlink/cast/CarLifePanelCompositor$InputFormat;", "", "width", "", "height", "fps", "avcC", "", "generation", "", "<init>", "(III[BJ)V", "getWidth", "()I", "getHeight", "getFps", "getAvcC", "()[B", "getGeneration", "()J", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class InputFormat {
        private final byte[] avcC;
        private final int fps;
        private final long generation;
        private final int height;
        private final int width;

        public static /* synthetic */ InputFormat copy$default(InputFormat inputFormat, int i, int i2, int i3, byte[] bArr, long j, int i4, Object obj) {
            if ((i4 & 1) != 0) {
                i = inputFormat.width;
            }
            if ((i4 & 2) != 0) {
                i2 = inputFormat.height;
            }
            if ((i4 & 4) != 0) {
                i3 = inputFormat.fps;
            }
            if ((i4 & 8) != 0) {
                bArr = inputFormat.avcC;
            }
            if ((i4 & 16) != 0) {
                j = inputFormat.generation;
            }
            long j2 = j;
            return inputFormat.copy(i, i2, i3, bArr, j2);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final int getWidth() {
            return this.width;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final int getHeight() {
            return this.height;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final int getFps() {
            return this.fps;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final byte[] getAvcC() {
            return this.avcC;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final long getGeneration() {
            return this.generation;
        }

        public final InputFormat copy(int width, int height, int fps, byte[] avcC, long generation) {
            Intrinsics.checkNotNullParameter(avcC, "avcC");
            return new InputFormat(width, height, fps, avcC, generation);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof InputFormat)) {
                return false;
            }
            InputFormat inputFormat = (InputFormat) other;
            return this.width == inputFormat.width && this.height == inputFormat.height && this.fps == inputFormat.fps && Intrinsics.areEqual(this.avcC, inputFormat.avcC) && this.generation == inputFormat.generation;
        }

        public int hashCode() {
            return (((((((Integer.hashCode(this.width) * 31) + Integer.hashCode(this.height)) * 31) + Integer.hashCode(this.fps)) * 31) + Arrays.hashCode(this.avcC)) * 31) + Long.hashCode(this.generation);
        }

        public String toString() {
            return "InputFormat(width=" + this.width + ", height=" + this.height + ", fps=" + this.fps + ", avcC=" + Arrays.toString(this.avcC) + ", generation=" + this.generation + ")";
        }

        public InputFormat(int width, int height, int fps, byte[] avcC, long generation) {
            Intrinsics.checkNotNullParameter(avcC, "avcC");
            this.width = width;
            this.height = height;
            this.fps = fps;
            this.avcC = avcC;
            this.generation = generation;
        }

        public final byte[] getAvcC() {
            return this.avcC;
        }

        public final int getFps() {
            return this.fps;
        }

        public final long getGeneration() {
            return this.generation;
        }

        public final int getHeight() {
            return this.height;
        }

        public final int getWidth() {
            return this.width;
        }
    }

    /* JADX INFO: compiled from: CarLifePanelCompositor.kt */
    @Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000f\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0007HÆ\u0003J'\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00072\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001J\t\u0010\u0018\u001a\u00020\u0019HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u001a"}, d2 = {"Lcom/carlink/cast/CarLifePanelCompositor$InputFrame;", "", "bytes", "", "ptsUs", "", "keyFrame", "", "<init>", "([BJZ)V", "getBytes", "()[B", "getPtsUs", "()J", "getKeyFrame", "()Z", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "", "toString", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class InputFrame {
        private final byte[] bytes;
        private final boolean keyFrame;
        private final long ptsUs;

        public static /* synthetic */ InputFrame copy$default(InputFrame inputFrame, byte[] bArr, long j, boolean z, int i, Object obj) {
            if ((i & 1) != 0) {
                bArr = inputFrame.bytes;
            }
            if ((i & 2) != 0) {
                j = inputFrame.ptsUs;
            }
            if ((i & 4) != 0) {
                z = inputFrame.keyFrame;
            }
            return inputFrame.copy(bArr, j, z);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final byte[] getBytes() {
            return this.bytes;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final long getPtsUs() {
            return this.ptsUs;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final boolean getKeyFrame() {
            return this.keyFrame;
        }

        public final InputFrame copy(byte[] bytes, long ptsUs, boolean keyFrame) {
            Intrinsics.checkNotNullParameter(bytes, "bytes");
            return new InputFrame(bytes, ptsUs, keyFrame);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof InputFrame)) {
                return false;
            }
            InputFrame inputFrame = (InputFrame) other;
            return Intrinsics.areEqual(this.bytes, inputFrame.bytes) && this.ptsUs == inputFrame.ptsUs && this.keyFrame == inputFrame.keyFrame;
        }

        public int hashCode() {
            return (((Arrays.hashCode(this.bytes) * 31) + Long.hashCode(this.ptsUs)) * 31) + Boolean.hashCode(this.keyFrame);
        }

        public String toString() {
            return "InputFrame(bytes=" + Arrays.toString(this.bytes) + ", ptsUs=" + this.ptsUs + ", keyFrame=" + this.keyFrame + ")";
        }

        public InputFrame(byte[] bytes, long ptsUs, boolean keyFrame) {
            Intrinsics.checkNotNullParameter(bytes, "bytes");
            this.bytes = bytes;
            this.ptsUs = ptsUs;
            this.keyFrame = keyFrame;
        }

        public final byte[] getBytes() {
            return this.bytes;
        }

        public final boolean getKeyFrame() {
            return this.keyFrame;
        }

        public final long getPtsUs() {
            return this.ptsUs;
        }
    }

    public final void updatePanel(CarLifePanel value) {
        Intrinsics.checkNotNullParameter(value, "value");
        if (this.running.get()) {
            throw new IllegalStateException("不能在全画布合成期间修改 CarLife 面板".toString());
        }
        this.panel = value;
        this.videoPlacement = CarPanelGeometry.INSTANCE.videoPlacement(value.getPanelWidth(), value.getPanelHeight(), value, this.safeAreaPolicy);
    }

    public final void updateSafeAreaPolicy(CarPanelGeometry.SafeAreaPolicy value) {
        Intrinsics.checkNotNullParameter(value, "value");
        if (this.running.get()) {
            throw new IllegalStateException("不能在全画布合成期间修改安全区方案".toString());
        }
        this.safeAreaPolicy = value;
        this.videoPlacement = CarPanelGeometry.INSTANCE.videoPlacement(this.panel.getPanelWidth(), this.panel.getPanelHeight(), this.panel, value);
    }

    @Override // com.carplay.adapter.carlife.CarLifeVideoProcessor
    public void start(Context context, int outputWidth, int outputHeight, int outputFps, Function1<? super byte[], Unit> onConfig, Function3<? super byte[], ? super Long, ? super Boolean, Unit> onFrame, Function1<? super String, Unit> onDiagnostic, Function1<? super Throwable, Unit> onFailure, Function0<Unit> onNeedKeyFrame) throws InterruptedException {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(onConfig, "onConfig");
        Intrinsics.checkNotNullParameter(onFrame, "onFrame");
        Intrinsics.checkNotNullParameter(onDiagnostic, "onDiagnostic");
        Intrinsics.checkNotNullParameter(onFailure, "onFailure");
        Intrinsics.checkNotNullParameter(onNeedKeyFrame, "onNeedKeyFrame");
        if (!(outputWidth == this.panel.getPanelWidth() && outputHeight == this.panel.getPanelHeight())) {
            throw new IllegalArgumentException(("合成画布 " + outputWidth + "x" + outputHeight + " 与 CarLife 面板 " + this.panel.getPanelWidth() + "x" + this.panel.getPanelHeight() + " 不一致").toString());
        }
        if (!this.running.compareAndSet(false, true)) {
            onDiagnostic.invoke("全画布合成器重复启动请求，沿用当前实例");
            return;
        }
        this.context = context.getApplicationContext();
        this.outputWidth = outputWidth;
        this.outputHeight = outputHeight;
        this.outputFps = RangesKt.coerceIn(outputFps, 1, 60);
        this.outputConfig = onConfig;
        this.outputFrame = onFrame;
        this.diagnostic = onDiagnostic;
        this.failed = onFailure;
        this.needKeyFrame = onNeedKeyFrame;
        onDiagnostic.invoke("画布合成启动：panel=" + this.panel.getPanelWidth() + "x" + this.panel.getPanelHeight() + ", safe@(" + this.panel.getSafeX() + "," + this.panel.getSafeY() + ")/" + this.panel.getSafeWidth() + "x" + this.panel.getSafeHeight() + ", safeAreaPolicy=" + this.safeAreaPolicy.getMode() + " enabled=" + this.safeAreaPolicy.getEnabled());
        this.outputAvcC = null;
        this.nextOutputPtsUs = 0L;
        this.renderedFrames = 0L;
        this.encodedFrames = 0L;
        this.firstOutputFormatLogged = false;
        this.outputConfigWarningLogged = false;
        this.encoderStallWarningLogged = false;
        final CountDownLatch ready = new CountDownLatch(1);
        final Throwable[] startupFailure = new Throwable[1];
        Thread thread = new Thread(new Runnable() { // from class: com.carlink.cast.CarLifePanelCompositor$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CarLifePanelCompositor.start$lambda$3(this.f$0, ready, startupFailure);
            }
        }, "carlink-panel-composer");
        thread.setDaemon(true);
        thread.start();
        this.worker = thread;
        if (!ready.await(15L, TimeUnit.SECONDS)) {
            stop();
            throw new IllegalStateException("全画布合成器初始化超时".toString());
        }
        Throwable th = startupFailure[0];
        if (th != null) {
            String simpleName = th.getClass().getSimpleName();
            String message = th.getMessage();
            if (message == null) {
                message = "(无消息)";
            }
            throw new IllegalStateException("全画布合成器启动失败：" + simpleName + ": " + message, th);
        }
    }

    static final void start$lambda$3(CarLifePanelCompositor this$0, CountDownLatch $ready, Throwable[] $startupFailure) {
        try {
            this$0.runComposer($ready);
        } catch (Throwable error) {
            try {
                $startupFailure[0] = error;
                $ready.countDown();
                if (this$0.running.getAndSet(false)) {
                    String trace = Log.getStackTraceString(error);
                    Intrinsics.checkNotNullExpressionValue(trace, "getStackTraceString(...)");
                    String name = error.getClass().getName();
                    String message = error.getMessage();
                    if (message == null) {
                        message = "(无消息)";
                    }
                    this$0.log("全画布合成线程异常：" + name + ": " + message + "\n" + trace);
                    Function1<? super Throwable, Unit> function1 = this$0.failed;
                    if (function1 != null) {
                        function1.invoke(error);
                    }
                }
            } finally {
                this$0.releaseResources();
            }
        }
    }

    @Override // com.carplay.adapter.carlife.CarLifeVideoProcessor
    public void configureInput(int width, int height, int fps, byte[] avcC) throws Throwable {
        Intrinsics.checkNotNullParameter(avcC, "avcC");
        if (!(width > 0 && height > 0 && width % 2 == 0 && height % 2 == 0)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        synchronized (this.lock) {
            try {
                this.inputGeneration++;
                try {
                    int iCoerceIn = RangesKt.coerceIn(fps, 1, 60);
                    byte[] bArrCopyOf = Arrays.copyOf(avcC, avcC.length);
                    Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
                    this.pendingFormat = new InputFormat(width, height, iCoerceIn, bArrCopyOf, this.inputGeneration);
                    this.latestTouchWidth = width;
                    this.latestTouchHeight = height;
                    this.frames.clear();
                    this.waitingForInputKeyFrame = true;
                    this.lock.notifyAll();
                    Unit unit = Unit.INSTANCE;
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    @Override // com.carplay.adapter.carlife.CarLifeVideoProcessor
    public void offerFrame(byte[] accessUnit, long ptsUs, boolean keyFrame) {
        ArrayDeque<InputFrame> arrayDeque;
        Intrinsics.checkNotNullParameter(accessUnit, "accessUnit");
        if (this.running.get()) {
            if (accessUnit.length == 0) {
                return;
            }
            long dropped = 0;
            synchronized (this.lock) {
                if (this.waitingForInputKeyFrame && !keyFrame) {
                    this.droppedInputFrames++;
                    return;
                }
                if (this.frames.size() >= 8) {
                    if (keyFrame) {
                        while (this.frames.size() >= 8) {
                            this.frames.removeFirst();
                            dropped++;
                        }
                        this.waitingForInputKeyFrame = false;
                        ArrayDeque<InputFrame> arrayDeque2 = this.frames;
                        byte[] bArrCopyOf = Arrays.copyOf(accessUnit, accessUnit.length);
                        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
                        arrayDeque2.addLast(new InputFrame(bArrCopyOf, ptsUs, true));
                    } else {
                        int i = 0;
                        Iterator it = this.frames.iterator();
                        while (true) {
                            if (it.hasNext()) {
                                Object next = it.next();
                                if (i < 0) {
                                    CollectionsKt.throwIndexOverflow();
                                }
                                if (!((InputFrame) next).getKeyFrame()) {
                                    break;
                                } else {
                                    i++;
                                }
                            } else {
                                i = -1;
                                break;
                            }
                        }
                        if (i >= 0) {
                            int i2 = i + 1;
                            int i3 = 0;
                            while (true) {
                                arrayDeque = this.frames;
                                if (i3 >= i2) {
                                    break;
                                }
                                arrayDeque.removeFirst();
                                dropped++;
                                i3++;
                            }
                            byte[] bArrCopyOf2 = Arrays.copyOf(accessUnit, accessUnit.length);
                            Intrinsics.checkNotNullExpressionValue(bArrCopyOf2, "copyOf(...)");
                            arrayDeque.addLast(new InputFrame(bArrCopyOf2, ptsUs, false));
                        } else {
                            dropped = 0 + 1;
                        }
                    }
                    this.lock.notifyAll();
                } else {
                    if (keyFrame) {
                        this.waitingForInputKeyFrame = false;
                    }
                    ArrayDeque<InputFrame> arrayDeque3 = this.frames;
                    byte[] bArrCopyOf3 = Arrays.copyOf(accessUnit, accessUnit.length);
                    Intrinsics.checkNotNullExpressionValue(bArrCopyOf3, "copyOf(...)");
                    arrayDeque3.addLast(new InputFrame(bArrCopyOf3, ptsUs, keyFrame));
                }
                this.lock.notifyAll();
                Unit unit = Unit.INSTANCE;
                if (dropped > 0) {
                    this.droppedInputFrames += dropped;
                    if (this.droppedInputFrames == dropped || this.droppedInputFrames % 30 == 0) {
                        log("全画布输入短时拥塞：丢弃过期帧 " + dropped + "（累计 " + this.droppedInputFrames + "），保留最新关键帧，不重建 CarLife 编码器");
                    }
                }
            }
        }
    }

    @Override // com.carplay.adapter.carlife.CarLifeVideoProcessor
    public Pair<Float, Float> mapTouchToSource(float panelX, float panelY) {
        return CarPanelGeometry.INSTANCE.mapVideoTouchToSource(panelX, panelY, this.videoPlacement, this.latestTouchWidth, this.latestTouchHeight);
    }

    @Override // com.carplay.adapter.carlife.CarLifeVideoProcessor
    public void stop() throws InterruptedException {
        if (this.running.getAndSet(false)) {
            synchronized (this.lock) {
                this.lock.notifyAll();
                Unit unit = Unit.INSTANCE;
            }
            Thread thread = this.worker;
            if (thread != null) {
                thread.interrupt();
            }
            Thread thread2 = this.worker;
            if (thread2 != null) {
                thread2.join(2000L);
            }
            this.worker = null;
            this.context = null;
        }
    }

    private final void runComposer(CountDownLatch ready) throws InterruptedException, IOException {
        Object objM159constructorimpl;
        MediaFormat outputFormat;
        Function1<? super byte[], Unit> function1;
        initializeEncoder();
        initializeEgl();
        initializeTextures();
        int i = this.outputWidth;
        int i2 = this.outputHeight;
        int i3 = this.outputFps;
        int safeWidth = this.panel.getSafeWidth();
        int safeHeight = this.panel.getSafeHeight();
        int safeX = this.panel.getSafeX();
        int safeY = this.panel.getSafeY();
        MediaCodec mediaCodec = this.encoder;
        log("整屏合成已启动：输出 " + i + "x" + i2 + "@" + i3 + "，CarLife 区域 " + safeWidth + "x" + safeHeight + "@(" + safeX + "," + safeY + ")；二次编码 8Mbps，硬件编码器=" + (mediaCodec != null ? mediaCodec.getName() : null));
        drainEncoder();
        ready.countDown();
        long inputFormatGeneration = 0;
        InputFrame inputFrame = null;
        long nextRenderAt = System.nanoTime();
        while (this.running.get()) {
            Pair<InputFormat, InputFrame> pairTakeInput = takeInput(inputFrame == null);
            InputFormat format = pairTakeInput.component1();
            InputFrame frame = pairTakeInput.component2();
            if (format != null && format.getGeneration() != inputFormatGeneration) {
                configureDecoder(format);
                inputFrame = null;
                inputFormatGeneration = format.getGeneration();
                this.requestOutputSync = true;
                byte[] bArr = this.outputAvcC;
                if (bArr != null && (function1 = this.outputConfig) != null) {
                    Object objCopyOf = Arrays.copyOf(bArr, bArr.length);
                    Intrinsics.checkNotNullExpressionValue(objCopyOf, "copyOf(...)");
                    function1.invoke(objCopyOf);
                }
                log("CarLife 解码器已重建：" + format.getWidth() + "x" + format.getHeight() + "@" + format.getFps() + "，输入 CONFIG=" + format.getAvcC().length + "B，generation=" + format.getGeneration());
            }
            if (frame != null) {
                inputFrame = frame;
            }
            if (inputFrame != null) {
                if (feedDecoder(inputFrame)) {
                    inputFrame = null;
                } else {
                    Thread.sleep(2L);
                }
            }
            inputFrame = inputFrame;
            drainDecoder();
            if (this.videoFrameAvailable.getAndSet(false)) {
                SurfaceTexture surfaceTexture = this.decoderSurfaceTexture;
                if (surfaceTexture != null) {
                    surfaceTexture.updateTexImage();
                }
                SurfaceTexture surfaceTexture2 = this.decoderSurfaceTexture;
                if (surfaceTexture2 != null) {
                    surfaceTexture2.getTransformMatrix(this.videoMatrix);
                }
                if (!this.videoHasFrame) {
                    log("CarLife 视频首帧已进入全画布");
                }
                this.videoHasFrame = true;
                if (this.requestOutputSync) {
                    this.requestOutputSync = false;
                    requestEncoderSyncFrame();
                }
            }
            long now = System.nanoTime();
            if (now >= nextRenderAt) {
                refreshWallpaperIfChanged$default(this, false, 1, null);
                if (this.videoHasFrame) {
                    renderFrame(this.nextOutputPtsUs);
                    this.nextOutputPtsUs += frameIntervalUs();
                }
                drainEncoder();
                if (this.renderedFrames >= ((long) RangesKt.coerceAtLeast(this.outputFps, 1)) * 3 && this.encodedFrames == 0 && !this.encoderStallWarningLogged) {
                    this.encoderStallWarningLogged = true;
                    try {
                        Result.Companion companion = Result.INSTANCE;
                        MediaCodec mediaCodec2 = this.encoder;
                        objM159constructorimpl = Result.m159constructorimpl((mediaCodec2 == null || (outputFormat = mediaCodec2.getOutputFormat()) == null) ? null : outputFormat.toString());
                    } catch (Throwable th) {
                        Result.Companion companion2 = Result.INSTANCE;
                        objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
                    }
                    Object obj = objM159constructorimpl;
                    if (Result.m165isFailureimpl(obj)) {
                        obj = null;
                    }
                    String format2 = (String) obj;
                    if (format2 == null) {
                        format2 = "不可用";
                    }
                    log("全画布 EGL 已渲染 " + this.renderedFrames + " 帧，但编码器 3 秒内没有输出 H.264；输出格式=" + format2);
                }
                long intervalNanos = frameIntervalUs() * 1000;
                nextRenderAt = now - nextRenderAt > intervalNanos ? now + intervalNanos : nextRenderAt + intervalNanos;
            } else {
                synchronized (this.lock) {
                    this.lock.wait(RangesKt.coerceIn((nextRenderAt - now) / 1000000, 1L, 5L));
                    Unit unit = Unit.INSTANCE;
                }
            }
        }
    }

    private final void initializeEncoder() throws IOException {
        MediaFormat format = MediaFormat.createVideoFormat(MIME_AVC, this.outputWidth, this.outputHeight);
        format.setInteger("color-format", 2130708361);
        format.setInteger("bitrate", ENCODER_BIT_RATE);
        format.setInteger("frame-rate", this.outputFps);
        format.setInteger("i-frame-interval", 1);
        Intrinsics.checkNotNullExpressionValue(format, "apply(...)");
        MediaCodec mediaCodecCreateEncoderByType = MediaCodec.createEncoderByType(MIME_AVC);
        mediaCodecCreateEncoderByType.configure(format, (Surface) null, (MediaCrypto) null, 1);
        Surface surfaceCreateInputSurface = mediaCodecCreateEncoderByType.createInputSurface();
        Intrinsics.checkNotNullExpressionValue(surfaceCreateInputSurface, "createInputSurface(...)");
        mediaCodecCreateEncoderByType.start();
        this.encoderInputSurface = surfaceCreateInputSurface;
        this.encoder = mediaCodecCreateEncoderByType;
    }

    private final void initializeEgl() {
        EGLDisplay display = EGL14.eglGetDisplay(0);
        if (Intrinsics.areEqual(display, EGL14.EGL_NO_DISPLAY)) {
            throw new IllegalStateException("eglGetDisplay 失败".toString());
        }
        Intrinsics.checkNotNull(display);
        this.eglDisplay = display;
        int[] versions = new int[2];
        checkEgl(EGL14.eglInitialize(display, versions, 0, versions, 1), "eglInitialize");
        EGLConfig[] configs = new EGLConfig[1];
        int[] count = new int[1];
        int[] recordableAttributes = {12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, 4, EGL_RECORDABLE_ANDROID, 1, 12344};
        boolean chosenRecordable = EGL14.eglChooseConfig(display, recordableAttributes, 0, configs, 0, 1, count, 0) && count[0] > 0;
        if (!chosenRecordable) {
            int[] fallbackAttributes = {12324, 8, 12323, 8, 12322, 8, 12321, 8, 12352, 4, 12344};
            checkEgl(EGL14.eglChooseConfig(display, fallbackAttributes, 0, configs, 0, 1, count, 0) && count[0] > 0, "eglChooseConfig(recordable/fallback)");
            log("EGL 不支持 recordable 配置，已回退到普通 ES2 配置");
        }
        EGLConfig config = configs[0];
        if (config == null) {
            throw new IllegalStateException("Required value was null.".toString());
        }
        EGLContext eGLContextEglCreateContext = EGL14.eglCreateContext(display, config, EGL14.EGL_NO_CONTEXT, new int[]{12440, 2, 12344}, 0);
        Intrinsics.checkNotNullExpressionValue(eGLContextEglCreateContext, "eglCreateContext(...)");
        this.eglContext = eGLContextEglCreateContext;
        if (Intrinsics.areEqual(this.eglContext, EGL14.EGL_NO_CONTEXT)) {
            throw new IllegalStateException("eglCreateContext 失败".toString());
        }
        Surface surface = this.encoderInputSurface;
        if (surface == null) {
            throw new IllegalStateException("Required value was null.".toString());
        }
        EGLSurface eGLSurfaceEglCreateWindowSurface = EGL14.eglCreateWindowSurface(display, config, surface, new int[]{12344}, 0);
        Intrinsics.checkNotNullExpressionValue(eGLSurfaceEglCreateWindowSurface, "eglCreateWindowSurface(...)");
        this.eglSurface = eGLSurfaceEglCreateWindowSurface;
        if (Intrinsics.areEqual(this.eglSurface, EGL14.EGL_NO_SURFACE)) {
            throw new IllegalStateException("eglCreateWindowSurface 失败".toString());
        }
        checkEgl(EGL14.eglMakeCurrent(display, this.eglSurface, this.eglSurface, this.eglContext), "eglMakeCurrent");
    }

    private final void initializeTextures() {
        int[] videoIds = new int[1];
        GLES20.glGenTextures(1, videoIds, 0);
        this.videoTexture = videoIds[0];
        GLES20.glBindTexture(36197, this.videoTexture);
        setTextureParameters(36197);
        SurfaceTexture surfaceTexture = new SurfaceTexture(this.videoTexture);
        surfaceTexture.setDefaultBufferSize(this.panel.getPanelWidth(), this.panel.getPanelHeight());
        surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() { // from class: com.carlink.cast.CarLifePanelCompositor$$ExternalSyntheticLambda2
            @Override // android.graphics.SurfaceTexture.OnFrameAvailableListener
            public final void onFrameAvailable(SurfaceTexture surfaceTexture2) {
                CarLifePanelCompositor.initializeTextures$lambda$23$lambda$22(this.f$0, surfaceTexture2);
            }
        });
        this.decoderSurfaceTexture = surfaceTexture;
        this.decoderSurface = new Surface(this.decoderSurfaceTexture);
        int[] wallpaperIds = new int[1];
        GLES20.glGenTextures(1, wallpaperIds, 0);
        this.wallpaperTexture = wallpaperIds[0];
        GLES20.glBindTexture(3553, this.wallpaperTexture);
        setTextureParameters(3553);
        ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(4);
        byteBufferAllocateDirect.put(new byte[]{0, 0, 0, 0});
        byteBufferAllocateDirect.position(0);
        GLES20.glTexImage2D(3553, 0, 6408, 1, 1, 0, 6408, 5121, byteBufferAllocateDirect);
        this.oesProgram = buildProgram(true);
        this.bitmapProgram = buildProgram(false);
        this.oesPositionLocation = GLES20.glGetAttribLocation(this.oesProgram, "aPos");
        this.oesTextureLocation = GLES20.glGetAttribLocation(this.oesProgram, "aTexCoord");
        this.oesMatrixLocation = GLES20.glGetUniformLocation(this.oesProgram, "uSTMatrix");
        this.bitmapPositionLocation = GLES20.glGetAttribLocation(this.bitmapProgram, "aPos");
        this.bitmapTextureLocation = GLES20.glGetAttribLocation(this.bitmapProgram, "aTexCoord");
        this.bitmapMatrixLocation = GLES20.glGetUniformLocation(this.bitmapProgram, "uSTMatrix");
        refreshWallpaperIfChanged(true);
    }

    static final void initializeTextures$lambda$23$lambda$22(CarLifePanelCompositor this$0, SurfaceTexture it) {
        this$0.videoFrameAvailable.set(true);
        synchronized (this$0.lock) {
            this$0.lock.notifyAll();
            Unit unit = Unit.INSTANCE;
        }
    }

    private final void setTextureParameters(int target) {
        GLES20.glTexParameteri(target, 10241, 9729);
        GLES20.glTexParameteri(target, 10240, 9729);
        GLES20.glTexParameteri(target, 10242, 33071);
        GLES20.glTexParameteri(target, 10243, 33071);
    }

    static /* synthetic */ void refreshWallpaperIfChanged$default(CarLifePanelCompositor carLifePanelCompositor, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        carLifePanelCompositor.refreshWallpaperIfChanged(z);
    }

    private final void refreshWallpaperIfChanged(boolean force) {
        long revision = PanelWallpaperStore.INSTANCE.getRevision();
        if (force || revision != this.lastWallpaperRevision) {
            this.lastWallpaperRevision = revision;
            Context context = this.context;
            Bitmap bitmap = context != null ? PanelWallpaperStore.INSTANCE.decode(context, Math.max(this.outputWidth, this.outputHeight)) : null;
            if (bitmap == null) {
                resetWallpaperTexture();
                log("未设置壁纸，透明画布按 H.264 黑底输出");
                return;
            }
            GLES20.glBindTexture(3553, this.wallpaperTexture);
            GLUtils.texImage2D(3553, 0, bitmap, 0);
            this.wallpaperUv = WallpaperTextureGeometry.INSTANCE.aspectFillUv(bitmap.getWidth(), bitmap.getHeight(), this.outputWidth, this.outputHeight);
            String uvText = ArraysKt.joinToString$default(this.wallpaperUv, (CharSequence) ",", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, new Function1() { // from class: com.carlink.cast.CarLifePanelCompositor$$ExternalSyntheticLambda3
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return CarLifePanelCompositor.refreshWallpaperIfChanged$lambda$26(((Float) obj).floatValue());
                }
            }, 30, (Object) null);
            log("全画布壁纸已更新：" + bitmap.getWidth() + "x" + bitmap.getHeight() + " → " + this.outputWidth + "x" + this.outputHeight + "，UV=[" + uvText + "]");
            bitmap.recycle();
        }
    }

    static final CharSequence refreshWallpaperIfChanged$lambda$26(float it) {
        String str = String.format("%.4f", Arrays.copyOf(new Object[]{Float.valueOf(it)}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    private final void resetWallpaperTexture() {
        GLES20.glBindTexture(3553, this.wallpaperTexture);
        ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(4);
        byteBufferAllocateDirect.put(new byte[]{0, 0, 0, 0});
        byteBufferAllocateDirect.position(0);
        GLES20.glTexImage2D(3553, 0, 6408, 1, 1, 0, 6408, 5121, byteBufferAllocateDirect);
        this.wallpaperUv = new float[]{0.0f, 1.0f, 0.0f, 1.0f};
    }

    private final void configureDecoder(InputFormat input) throws IOException {
        String placementName;
        releaseDecoder();
        this.inputWidth = input.getWidth();
        this.inputHeight = input.getHeight();
        SurfaceTexture surfaceTexture = this.decoderSurfaceTexture;
        if (surfaceTexture != null) {
            surfaceTexture.setDefaultBufferSize(input.getWidth(), input.getHeight());
        }
        Pair<byte[], byte[]> avcC = parseAvcC(input.getAvcC());
        byte[] sps = avcC.component1();
        byte[] pps = avcC.component2();
        MediaFormat format = MediaFormat.createVideoFormat(MIME_AVC, input.getWidth(), input.getHeight());
        format.setInteger("frame-rate", input.getFps());
        format.setByteBuffer("csd-0", ByteBuffer.wrap(ArraysKt.plus(START_CODE, sps)));
        format.setByteBuffer("csd-1", ByteBuffer.wrap(ArraysKt.plus(START_CODE, pps)));
        Intrinsics.checkNotNullExpressionValue(format, "apply(...)");
        MediaCodec mediaCodecCreateDecoderByType = MediaCodec.createDecoderByType(MIME_AVC);
        Surface surface = this.decoderSurface;
        if (surface == null) {
            throw new IllegalStateException("Required value was null.".toString());
        }
        mediaCodecCreateDecoderByType.configure(format, surface, (MediaCrypto) null, 0);
        mediaCodecCreateDecoderByType.start();
        this.decoder = mediaCodecCreateDecoderByType;
        this.videoPlacement = CarPanelGeometry.INSTANCE.videoPlacement(input.getWidth(), input.getHeight(), this.panel, this.safeAreaPolicy);
        switch (WhenMappings.$EnumSwitchMapping$0[this.videoPlacement.getLayout().ordinal()]) {
            case 1:
                if (this.panel.getSafeX() != 0 || this.panel.getSafeY() != 0 || this.panel.getSafeWidth() != this.panel.getPanelWidth() || this.panel.getSafeHeight() != this.panel.getPanelHeight()) {
                    placementName = "CarLife 未采纳安全区编码，保留完整 UI " + this.videoPlacement.getWidth() + "x" + this.videoPlacement.getHeight() + "；当前无法安全叠加外部壁纸";
                } else {
                    placementName = "完整画布输入原样铺满 " + this.videoPlacement.getWidth() + "x" + this.videoPlacement.getHeight();
                }
                break;
            case 2:
                placementName = "安全区输入铺到 (" + this.videoPlacement.getX() + "," + this.videoPlacement.getY() + ") " + this.videoPlacement.getWidth() + "x" + this.videoPlacement.getHeight() + "，采样源=" + this.videoPlacement.getSourceX() + "," + this.videoPlacement.getSourceY() + " " + this.videoPlacement.getSourceWidth() + "x" + this.videoPlacement.getSourceHeight();
                break;
            case 3:
                placementName = "完整帧不裁剪，缩放到安全区 viewport (" + this.videoPlacement.getX() + "," + this.videoPlacement.getY() + ") " + this.videoPlacement.getWidth() + "x" + this.videoPlacement.getHeight() + "；外侧保留壁纸";
                break;
            default:
                throw new NoWhenBranchMatchedException();
        }
        log("CarLife 视频布局：输入 " + input.getWidth() + "x" + input.getHeight() + " → " + placementName);
        synchronized (this.lock) {
            this.waitingForInputKeyFrame = true;
            Unit unit = Unit.INSTANCE;
        }
        this.videoHasFrame = false;
        this.videoFrameAvailable.set(false);
    }

    private final boolean feedDecoder(InputFrame frame) {
        ByteBuffer buffer;
        MediaCodec codec = this.decoder;
        if (codec == null) {
            return false;
        }
        Iterable<byte[]> iterableNals = VideoPackets.INSTANCE.nals(frame.getBytes());
        Iterator<T> it = iterableNals.iterator();
        int length = 0;
        while (it.hasNext()) {
            length += ((byte[]) it.next()).length + START_CODE.length;
        }
        byte[] bArr = new byte[length];
        int length2 = 0;
        for (byte[] bArr2 : iterableNals) {
            ArraysKt.copyInto$default(START_CODE, bArr, length2, 0, 0, 12, (Object) null);
            int length3 = length2 + START_CODE.length;
            ArraysKt.copyInto$default(bArr2, bArr, length3, 0, 0, 12, (Object) null);
            length2 = length3 + bArr2.length;
        }
        int index = codec.dequeueInputBuffer(0L);
        if (index < 0 || (buffer = codec.getInputBuffer(index)) == null) {
            return false;
        }
        if (bArr.length > buffer.capacity()) {
            throw new IllegalStateException("CarLife H.264 access unit " + bArr.length + "B 超过解码器输入缓冲区");
        }
        buffer.clear();
        buffer.put(bArr);
        codec.queueInputBuffer(index, 0, bArr.length, frame.getPtsUs(), 0);
        if (frame.getKeyFrame()) {
            this.requestOutputSync = true;
        }
        return true;
    }

    private final void drainDecoder() {
        MediaCodec codec = this.decoder;
        if (codec == null) {
            return;
        }
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        for (int index = codec.dequeueOutputBuffer(info, 0L); index >= 0; index = codec.dequeueOutputBuffer(info, 0L)) {
            boolean render = info.size > 0;
            codec.releaseOutputBuffer(index, render);
        }
    }

    private final void renderFrame(long ptsUs) {
        GLES20.glViewport(0, 0, this.outputWidth, this.outputHeight);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(16384);
        drawBitmap();
        if (this.videoHasFrame) {
            drawVideo();
        }
        long presentationNs = 1000 * ptsUs;
        if (!EGLExt.eglPresentationTimeANDROID(this.eglDisplay, this.eglSurface, presentationNs)) {
            throw new IllegalStateException("eglPresentationTimeANDROID 失败".toString());
        }
        checkEgl(EGL14.eglSwapBuffers(this.eglDisplay, this.eglSurface), "eglSwapBuffers");
        this.renderedFrames++;
        if (this.renderedFrames == 1) {
            log("全画布 EGL 首帧已提交编码器：" + this.outputWidth + "x" + this.outputHeight);
        }
    }

    private final void drawBitmap() {
        GLES20.glUseProgram(this.bitmapProgram);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.wallpaperTexture);
        GLES20.glUniformMatrix4fv(this.bitmapMatrixLocation, 1, false, IDENTITY_MATRIX, 0);
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(1, 771);
        drawRect(this.bitmapProgram, -1.0f, -1.0f, 1.0f, 1.0f, WallpaperTextureGeometry.INSTANCE.quadCoordinates(this.wallpaperUv));
        GLES20.glDisable(3042);
    }

    private final void drawVideo() {
        GLES20.glUseProgram(this.oesProgram);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(36197, this.videoTexture);
        GLES20.glUniformMatrix4fv(this.oesMatrixLocation, 1, false, this.videoMatrix, 0);
        CarPanelGeometry.VideoPlacement placement = this.videoPlacement;
        float left = placement.getX();
        float top = placement.getY();
        float right = placement.getWidth() + left;
        float bottom = placement.getHeight() + top;
        drawRect(this.oesProgram, px(left), py(bottom), px(right), py(top), CarPanelGeometry.INSTANCE.videoTextureCoordinates(placement, this.inputWidth, this.inputHeight));
    }

    private final void drawRect(int program, float left, float bottom, float right, float top, float[] textureCoordinates) {
        FloatBuffer positions = floatBuffer(left, bottom, right, bottom, left, top, right, top);
        FloatBuffer coords = floatBuffer(Arrays.copyOf(textureCoordinates, textureCoordinates.length));
        int positionLocation = program == this.oesProgram ? this.oesPositionLocation : this.bitmapPositionLocation;
        int textureLocation = program == this.oesProgram ? this.oesTextureLocation : this.bitmapTextureLocation;
        GLES20.glVertexAttribPointer(positionLocation, 2, 5126, false, 0, (Buffer) positions);
        GLES20.glEnableVertexAttribArray(positionLocation);
        int textureLocation2 = textureLocation;
        GLES20.glVertexAttribPointer(textureLocation2, 2, 5126, false, 0, (Buffer) coords);
        GLES20.glEnableVertexAttribArray(textureLocation2);
        GLES20.glDrawArrays(5, 0, 4);
    }

    private final void drainEncoder() {
        Object objM159constructorimpl;
        MediaFormat outputFormat;
        byte[] bArrConfigFromFormat;
        MediaCodec codec = this.encoder;
        if (codec == null) {
            return;
        }
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        int index = codec.dequeueOutputBuffer(info, 0L);
        while (true) {
            if (index < 0) {
                break;
            }
            ByteBuffer buffer = codec.getOutputBuffer(index);
            if (info.size > 0 && buffer != null) {
                byte[] bytes = new byte[info.size];
                buffer.position(info.offset);
                buffer.limit(info.offset + info.size);
                buffer.get(bytes);
                if ((info.flags & 2) != 0) {
                    byte[] config = findAvcC(bytes);
                    if (config == null) {
                        MediaFormat outputFormat2 = codec.getOutputFormat();
                        Intrinsics.checkNotNullExpressionValue(outputFormat2, "getOutputFormat(...)");
                        config = configFromFormat(outputFormat2);
                    }
                    if (config != null) {
                        publishConfig(config);
                    } else if (!this.outputConfigWarningLogged) {
                        this.outputConfigWarningLogged = true;
                        String prefix = CollectionsKt.joinToString$default(ArraysKt.take(bytes, 16), "", null, null, 0, null, new Function1() { // from class: com.carlink.cast.CarLifePanelCompositor$$ExternalSyntheticLambda1
                            @Override // kotlin.jvm.functions.Function1
                            public final Object invoke(Object obj) {
                                return CarLifePanelCompositor.drainEncoder$lambda$35(((Byte) obj).byteValue());
                            }
                        }, 30, null);
                        log("全画布编码器 CODEC_CONFIG 无法解析 SPS/PPS：size=" + bytes.length + "B，prefix=" + prefix);
                    }
                } else {
                    if (this.outputAvcC == null && (outputFormat = codec.getOutputFormat()) != null && (bArrConfigFromFormat = configFromFormat(outputFormat)) != null) {
                        publishConfig(bArrConfigFromFormat);
                    }
                    if (this.outputAvcC != null) {
                        boolean keyFrame = (info.flags & 1) != 0;
                        Function3<? super byte[], ? super Long, ? super Boolean, Unit> function3 = this.outputFrame;
                        if (function3 != null) {
                            function3.invoke(VideoPackets.INSTANCE.lengthPrefixed(bytes), Long.valueOf(info.presentationTimeUs), Boolean.valueOf(keyFrame));
                        }
                        this.encodedFrames++;
                        if (this.encodedFrames == 1) {
                            try {
                                Result.Companion companion = Result.INSTANCE;
                                CarLifePanelCompositor carLifePanelCompositor = this;
                                objM159constructorimpl = Result.m159constructorimpl(codec.getOutputFormat().toString());
                            } catch (Throwable th) {
                                Result.Companion companion2 = Result.INSTANCE;
                                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
                            }
                            if (Result.m165isFailureimpl(objM159constructorimpl)) {
                                objM159constructorimpl = "未知";
                            }
                            Intrinsics.checkNotNullExpressionValue(objM159constructorimpl, "getOrDefault(...)");
                            log("全画布编码首帧已交给 AirPlay：" + bytes.length + "B，IDR=" + keyFrame + "，输出格式=" + ((String) objM159constructorimpl));
                        }
                    }
                }
            }
            codec.releaseOutputBuffer(index, false);
            index = codec.dequeueOutputBuffer(info, 0L);
        }
        if (index == -2) {
            MediaFormat format = codec.getOutputFormat();
            Intrinsics.checkNotNullExpressionValue(format, "getOutputFormat(...)");
            if (!this.firstOutputFormatLogged) {
                this.firstOutputFormatLogged = true;
                log("全画布编码器输出格式已就绪：" + format);
            }
            byte[] config2 = configFromFormat(format);
            if (config2 != null) {
                publishConfig(config2);
                return;
            }
            if (!this.outputConfigWarningLogged) {
                this.outputConfigWarningLogged = true;
                ByteBuffer byteBuffer = format.getByteBuffer("csd-0");
                int csd0 = byteBuffer != null ? byteBuffer.remaining() : 0;
                ByteBuffer byteBuffer2 = format.getByteBuffer("csd-1");
                int csd1 = byteBuffer2 != null ? byteBuffer2.remaining() : 0;
                log("全画布编码器格式未识别到 SPS/PPS：csd-0=" + csd0 + "B，csd-1=" + csd1 + "B");
            }
        }
    }

    static final CharSequence drainEncoder$lambda$35(byte it) {
        String str = String.format("%02x", Arrays.copyOf(new Object[]{Byte.valueOf(it)}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    private final byte[] configFromFormat(MediaFormat format) {
        byte[] bArr;
        ByteBuffer byteBufferDuplicate;
        Iterable iterableListOf = CollectionsKt.listOf((Object[]) new String[]{"csd-0", "csd-1"});
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableListOf, 10));
        Iterator it = iterableListOf.iterator();
        while (it.hasNext()) {
            ByteBuffer byteBuffer = format.getByteBuffer((String) it.next());
            if (byteBuffer == null || (byteBufferDuplicate = byteBuffer.duplicate()) == null) {
                bArr = null;
            } else {
                bArr = new byte[byteBufferDuplicate.remaining()];
                byteBufferDuplicate.get(bArr);
            }
            arrayList.add(bArr);
        }
        Collection csd = (List) arrayList;
        AvcCodecConfig avcCodecConfig = AvcCodecConfig.INSTANCE;
        byte[][] bArr2 = (byte[][]) csd.toArray(new byte[0][]);
        return avcCodecConfig.fromParameterSets((byte[][]) Arrays.copyOf(bArr2, bArr2.length));
    }

    private final byte[] findAvcC(byte[] bytes) {
        return AvcCodecConfig.INSTANCE.fromParameterSets(bytes);
    }

    private final Pair<byte[], byte[]> parseAvcC(byte[] avcC) {
        if (!(avcC.length >= 11 && avcC[0] == 1)) {
            throw new IllegalArgumentException("CarLife AVC CONFIG 格式无效".toString());
        }
        int offset = 5 + 1;
        int spsCount = avcC[5] & 31;
        if (!(spsCount > 0)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        byte[] sps = readAvcNal(avcC, offset);
        int offset2 = offset + sps.length + 2;
        if (!(offset2 < avcC.length)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        int offset3 = offset2 + 1;
        int ppsCount = avcC[offset2] & 255;
        if (!(ppsCount > 0)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        byte[] pps = readAvcNal(avcC, offset3);
        return TuplesKt.to(sps, pps);
    }

    private final byte[] readAvcNal(byte[] bytes, int offset) {
        boolean z = false;
        if (!(offset + 2 <= bytes.length)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        int length = ((bytes[offset] & 255) << 8) | (bytes[offset + 1] & 255);
        if (length > 0 && offset + 2 + length <= bytes.length) {
            z = true;
        }
        if (!z) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        return ArraysKt.copyOfRange(bytes, offset + 2, offset + 2 + length);
    }

    private final void publishConfig(byte[] config) {
        byte[] bArr = this.outputAvcC;
        boolean z = false;
        if (bArr != null && Arrays.equals(bArr, config)) {
            z = true;
        }
        boolean changed = !z;
        byte[] bArrCopyOf = Arrays.copyOf(config, config.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        this.outputAvcC = bArrCopyOf;
        Function1<? super byte[], Unit> function1 = this.outputConfig;
        if (function1 != null) {
            Object objCopyOf = Arrays.copyOf(config, config.length);
            Intrinsics.checkNotNullExpressionValue(objCopyOf, "copyOf(...)");
            function1.invoke(objCopyOf);
        }
        if (changed) {
            log("全画布编码 CONFIG 就绪：" + config.length + "B，" + this.outputWidth + "x" + this.outputHeight);
        }
    }

    private final void requestEncoderSyncFrame() {
        Object objM159constructorimpl;
        Unit unit;
        try {
            Result.Companion companion = Result.INSTANCE;
            MediaCodec mediaCodec = this.encoder;
            if (mediaCodec != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("request-sync", 0);
                mediaCodec.setParameters(bundle);
                unit = Unit.INSTANCE;
            } else {
                unit = null;
            }
            objM159constructorimpl = Result.m159constructorimpl(unit);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (thM162exceptionOrNullimpl != null) {
            log("请求全画布编码器关键帧失败：" + thM162exceptionOrNullimpl.getMessage());
        }
    }

    private final Pair<InputFormat, InputFrame> takeInput(boolean allowFrame) {
        Pair<InputFormat, InputFrame> pair;
        synchronized (this.lock) {
            if (this.pendingFormat == null && ((!allowFrame || this.frames.isEmpty()) && this.running.get())) {
                this.lock.wait(4L);
            }
            InputFormat inputFormat = this.pendingFormat;
            InputFrame inputFramePollFirst = null;
            this.pendingFormat = null;
            if (inputFormat == null && allowFrame) {
                inputFramePollFirst = this.frames.pollFirst();
            }
            pair = TuplesKt.to(inputFormat, inputFramePollFirst);
        }
        return pair;
    }

    private final void releaseDecoder() {
        Unit unit;
        Unit unit2;
        try {
            Result.Companion companion = Result.INSTANCE;
            MediaCodec mediaCodec = this.decoder;
            if (mediaCodec != null) {
                mediaCodec.stop();
                unit2 = Unit.INSTANCE;
            } else {
                unit2 = null;
            }
            Result.m159constructorimpl(unit2);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        try {
            Result.Companion companion3 = Result.INSTANCE;
            MediaCodec mediaCodec2 = this.decoder;
            if (mediaCodec2 != null) {
                mediaCodec2.release();
                unit = Unit.INSTANCE;
            } else {
                unit = null;
            }
            Result.m159constructorimpl(unit);
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        this.decoder = null;
    }

    private final void releaseResources() {
        Unit unit;
        Unit unit2;
        Unit unit3;
        Unit unit4;
        Unit unit5;
        Unit unit6;
        Unit unit7;
        releaseDecoder();
        try {
            Result.Companion companion = Result.INSTANCE;
            MediaCodec mediaCodec = this.encoder;
            if (mediaCodec != null) {
                mediaCodec.stop();
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
            MediaCodec mediaCodec2 = this.encoder;
            if (mediaCodec2 != null) {
                mediaCodec2.release();
                unit6 = Unit.INSTANCE;
            } else {
                unit6 = null;
            }
            Result.m159constructorimpl(unit6);
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        this.encoder = null;
        try {
            Result.Companion companion5 = Result.INSTANCE;
            Surface surface = this.decoderSurface;
            if (surface != null) {
                surface.release();
                unit5 = Unit.INSTANCE;
            } else {
                unit5 = null;
            }
            Result.m159constructorimpl(unit5);
        } catch (Throwable th3) {
            Result.Companion companion6 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th3));
        }
        this.decoderSurface = null;
        try {
            Result.Companion companion7 = Result.INSTANCE;
            SurfaceTexture surfaceTexture = this.decoderSurfaceTexture;
            if (surfaceTexture != null) {
                surfaceTexture.release();
                unit4 = Unit.INSTANCE;
            } else {
                unit4 = null;
            }
            Result.m159constructorimpl(unit4);
        } catch (Throwable th4) {
            Result.Companion companion8 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th4));
        }
        this.decoderSurfaceTexture = null;
        try {
            Result.Companion companion9 = Result.INSTANCE;
            Integer numValueOf = Integer.valueOf(this.wallpaperTexture);
            if ((numValueOf.intValue() != 0 ? 1 : 0) == 0) {
                numValueOf = null;
            }
            if (numValueOf != null) {
                GLES20.glDeleteTextures(1, new int[]{numValueOf.intValue()}, 0);
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
            Integer numValueOf2 = Integer.valueOf(this.videoTexture);
            if ((numValueOf2.intValue() != 0 ? 1 : 0) == 0) {
                numValueOf2 = null;
            }
            if (numValueOf2 != null) {
                GLES20.glDeleteTextures(1, new int[]{numValueOf2.intValue()}, 0);
                unit2 = Unit.INSTANCE;
            } else {
                unit2 = null;
            }
            Result.m159constructorimpl(unit2);
        } catch (Throwable th6) {
            Result.Companion companion12 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th6));
        }
        this.wallpaperTexture = 0;
        this.videoTexture = 0;
        if (!Intrinsics.areEqual(this.eglDisplay, EGL14.EGL_NO_DISPLAY)) {
            try {
                Result.Companion companion13 = Result.INSTANCE;
                Result.m159constructorimpl(Boolean.valueOf(EGL14.eglMakeCurrent(this.eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)));
            } catch (Throwable th7) {
                Result.Companion companion14 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th7));
            }
            try {
                Result.Companion companion15 = Result.INSTANCE;
                CarLifePanelCompositor carLifePanelCompositor = this;
                Result.m159constructorimpl(Boolean.valueOf(EGL14.eglDestroySurface(carLifePanelCompositor.eglDisplay, carLifePanelCompositor.eglSurface)));
            } catch (Throwable th8) {
                Result.Companion companion16 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th8));
            }
            try {
                Result.Companion companion17 = Result.INSTANCE;
                CarLifePanelCompositor carLifePanelCompositor2 = this;
                Result.m159constructorimpl(Boolean.valueOf(EGL14.eglDestroyContext(carLifePanelCompositor2.eglDisplay, carLifePanelCompositor2.eglContext)));
            } catch (Throwable th9) {
                Result.Companion companion18 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th9));
            }
            try {
                Result.Companion companion19 = Result.INSTANCE;
                Result.m159constructorimpl(Boolean.valueOf(EGL14.eglTerminate(this.eglDisplay)));
            } catch (Throwable th10) {
                Result.Companion companion20 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th10));
            }
        }
        EGLDisplay EGL_NO_DISPLAY = EGL14.EGL_NO_DISPLAY;
        Intrinsics.checkNotNullExpressionValue(EGL_NO_DISPLAY, "EGL_NO_DISPLAY");
        this.eglDisplay = EGL_NO_DISPLAY;
        EGLSurface EGL_NO_SURFACE = EGL14.EGL_NO_SURFACE;
        Intrinsics.checkNotNullExpressionValue(EGL_NO_SURFACE, "EGL_NO_SURFACE");
        this.eglSurface = EGL_NO_SURFACE;
        EGLContext EGL_NO_CONTEXT = EGL14.EGL_NO_CONTEXT;
        Intrinsics.checkNotNullExpressionValue(EGL_NO_CONTEXT, "EGL_NO_CONTEXT");
        this.eglContext = EGL_NO_CONTEXT;
        try {
            Result.Companion companion21 = Result.INSTANCE;
            Surface surface2 = this.encoderInputSurface;
            if (surface2 != null) {
                surface2.release();
                unit = Unit.INSTANCE;
            } else {
                unit = null;
            }
            Result.m159constructorimpl(unit);
        } catch (Throwable th11) {
            Result.Companion companion22 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th11));
        }
        this.encoderInputSurface = null;
    }

    private final int buildProgram(boolean oes) {
        String fragment;
        String precision = fragmentFloatPrecision();
        if (oes) {
            fragment = StringsKt.trimIndent("\n            #extension GL_OES_EGL_image_external : require\n            precision " + precision + " float;\n            varying vec2 vTexCoord;\n            uniform samplerExternalOES uTexture;\n            void main() { gl_FragColor = texture2D(uTexture, vTexCoord); }\n            ");
        } else {
            fragment = StringsKt.trimIndent("\n            precision " + precision + " float;\n            varying vec2 vTexCoord;\n            uniform sampler2D uTexture;\n            void main() { gl_FragColor = texture2D(uTexture, vTexCoord); }\n            ");
        }
        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, buildProgram$compile(35633, "attribute vec2 aPos;\nattribute vec2 aTexCoord;\nuniform mat4 uSTMatrix;\nvarying vec2 vTexCoord;\nvoid main() {\n    gl_Position = vec4(aPos, 0.0, 1.0);\n    vTexCoord = (uSTMatrix * vec4(aTexCoord, 0.0, 1.0)).xy;\n}"));
        GLES20.glAttachShader(program, buildProgram$compile(35632, fragment));
        GLES20.glLinkProgram(program);
        int[] status = new int[1];
        GLES20.glGetProgramiv(program, 35714, status, 0);
        if (!(status[0] != 0)) {
            String strGlGetProgramInfoLog = GLES20.glGetProgramInfoLog(program);
            if (strGlGetProgramInfoLog == null) {
                strGlGetProgramInfoLog = "GLSL 链接失败";
            }
            throw new IllegalStateException(strGlGetProgramInfoLog.toString());
        }
        GLES20.glUseProgram(program);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "uTexture"), 0);
        return program;
    }

    private static final int buildProgram$compile(int type, String source) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        int[] status = new int[1];
        GLES20.glGetShaderiv(shader, 35713, status, 0);
        if (!(status[0] != 0)) {
            String strGlGetShaderInfoLog = GLES20.glGetShaderInfoLog(shader);
            if (strGlGetShaderInfoLog == null) {
                strGlGetShaderInfoLog = "GLSL 编译失败";
            }
            throw new IllegalStateException(strGlGetShaderInfoLog.toString());
        }
        return shader;
    }

    /* JADX WARN: Code duplicated, block: B:16:0x0035 A[RETURN, SYNTHETIC] */
    private final String fragmentFloatPrecision() {
        int i;
        int[] range = new int[2];
        boolean z = true;
        int[] precision = new int[1];
        GLES20.glGetShaderPrecisionFormat(35632, 36338, range, 0, precision, 0);
        if (precision[0] <= 0) {
            for (int i2 : range) {
                if (i2 != 0) {
                    i = 1;
                } else {
                    i = 0;
                }
                if (i != 0) {
                    if (z) {
                        return "mediump";
                    }
                }
            }
            z = false;
            if (z) {
                return "mediump";
            }
        }
        return "highp";
    }

    private final float px(float value) {
        return ((value / this.outputWidth) * 2.0f) - 1.0f;
    }

    private final float py(float value) {
        return 1.0f - ((value / this.outputHeight) * 2.0f);
    }

    private final long frameIntervalUs() {
        return 1000000 / ((long) RangesKt.coerceAtLeast(this.outputFps, 1));
    }

    private final FloatBuffer floatBuffer(float... values) {
        FloatBuffer floatBufferAsFloatBuffer = ByteBuffer.allocateDirect(values.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBufferAsFloatBuffer.put(values);
        floatBufferAsFloatBuffer.position(0);
        Intrinsics.checkNotNullExpressionValue(floatBufferAsFloatBuffer, "apply(...)");
        return floatBufferAsFloatBuffer;
    }

    private final void checkEgl(boolean ok, String stage) {
        if (!ok) {
            String string = Integer.toString(EGL14.eglGetError(), CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            throw new IllegalStateException(("EGL " + stage + " 失败，code=0x" + string).toString());
        }
    }

    private final void log(String message) {
        Function1<? super String, Unit> function1 = this.diagnostic;
        if (function1 != null) {
            function1.invoke(message);
        }
        Log.i(TAG, message);
    }

    /* JADX INFO: compiled from: CarLifePanelCompositor.kt */
    @Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\u0014\n\u0002\b\u0003\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0086T¢\u0006\u0002\n\u0000R\u0011\u0010\r\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0011\u001a\u00020\u0012¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014¨\u0006\u0015"}, d2 = {"Lcom/carlink/cast/CarLifePanelCompositor$Companion;", "", "<init>", "()V", "TAG", "", "MIME_AVC", "EGL_RECORDABLE_ANDROID", "", "ENCODER_BIT_RATE", "INPUT_QUEUE_CAPACITY", "INPUT_DROP_LOG_INTERVAL", "", "START_CODE", "", "getSTART_CODE", "()[B", "IDENTITY_MATRIX", "", "getIDENTITY_MATRIX", "()[F", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final byte[] getSTART_CODE() {
            return CarLifePanelCompositor.START_CODE;
        }

        public final float[] getIDENTITY_MATRIX() {
            return CarLifePanelCompositor.IDENTITY_MATRIX;
        }
    }
}
