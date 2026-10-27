package com.carplay.adapter.carlife;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.Surface;
import com.carplay.adapter.PhoneProjectionAdapter;
import com.carplay.adapter.ProjectionCapabilities;
import com.carplay.adapter.ProjectionGrant;
import com.carplay.adapter.ProjectionObserver;
import com.carplay.media.Pcm16Resampler;
import com.carplay.sender.VideoPackets;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.UByte;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

/* JADX INFO: compiled from: CarLifeProjectionAdapter.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000Ø\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u001c\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b%\u0018\u0000 ´\u00012\u00020\u0001:\u0002´\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010X\u001a\u00020\t2\u0006\u0010Y\u001a\u00020 H\u0016J\u0010\u0010Z\u001a\u00020A2\u0006\u0010Y\u001a\u00020 H\u0016J\u0010\u0010[\u001a\u00020\\2\u0006\u0010Y\u001a\u00020 H\u0002J\u0018\u0010]\u001a\u00020\t2\u0006\u0010Y\u001a\u00020 2\u0006\u0010^\u001a\u00020\u0005H\u0002J\u0010\u0010_\u001a\u00020A2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016J\u0010\u0010`\u001a\u00020A2\u0006\u0010a\u001a\u00020bH\u0016J \u0010c\u001a\u00020A2\u0006\u0010d\u001a\u00020N2\u0006\u0010e\u001a\u00020N2\u0006\u0010f\u001a\u00020\tH\u0016J\b\u0010g\u001a\u00020AH\u0016J\b\u0010h\u001a\u00020AH\u0016J\u0010\u0010i\u001a\u00020A2\u0006\u0010j\u001a\u00020\tH\u0002J \u0010k\u001a\u00020\t2\u0006\u0010f\u001a\u00020@2\u0006\u0010d\u001a\u00020N2\u0006\u0010e\u001a\u00020NH\u0016J.\u0010l\u001a\u00020\t2\u0006\u0010Y\u001a\u00020 2\u0006\u0010m\u001a\u00020N2\u0006\u0010n\u001a\u00020N2\u0006\u0010o\u001a\u00020NH\u0096@¢\u0006\u0002\u0010pJ.\u0010q\u001a\u00020A2\u001e\u0010r\u001a\u001a\u0012\u0004\u0012\u00020@\u0012\u0004\u0012\u00020%\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020A0?H\u0096@¢\u0006\u0002\u0010sJ\"\u0010t\u001a\u00020A2\u0006\u0010u\u001a\u00020%2\u0006\u0010v\u001a\u00020\u00132\b\u0010w\u001a\u0004\u0018\u00010\u0015H\u0002J\u0010\u0010x\u001a\u00020\t2\u0006\u0010u\u001a\u00020%H\u0002J2\u0010y\u001a\u00020A2\u0006\u0010v\u001a\u00020\u00132\u0006\u0010z\u001a\u00020N2\u0006\u0010{\u001a\u00020T2\u0006\u0010|\u001a\u00020T2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0002J\"\u0010}\u001a\u00020A2\u0006\u0010f\u001a\u00020@2\u0006\u0010d\u001a\u00020N2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0002J\u001b\u0010\u0096\u0001\u001a\u00020A2\u0006\u0010f\u001a\u00020@2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0002J\u0013\u0010\u0097\u0001\u001a\u00020A2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0002J\u001b\u0010\u0098\u0001\u001a\u00020A2\u0006\u0010f\u001a\u00020@2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0002J\t\u0010\u0099\u0001\u001a\u00020AH\u0002J\u0012\u0010\u009a\u0001\u001a\u00020@2\u0007\u0010\u009b\u0001\u001a\u00020@H\u0002J\u0012\u0010¡\u0001\u001a\u00020%2\u0007\u0010¢\u0001\u001a\u00020%H\u0002J*\u0010£\u0001\u001a\u00020A2\u0006\u0010z\u001a\u00020N2\u0006\u0010{\u001a\u00020T2\u0006\u0010|\u001a\u00020T2\u0007\u0010¤\u0001\u001a\u00020NH\u0016J\u001b\u0010¥\u0001\u001a\u00020A2\u0007\u0010¦\u0001\u001a\u00020N2\u0007\u0010§\u0001\u001a\u00020\tH\u0016J\u0010\u0010¨\u0001\u001a\u00020AH\u0096@¢\u0006\u0003\u0010©\u0001J\t\u0010ª\u0001\u001a\u00020AH\u0002J\u0019\u0010«\u0001\u001a\u00020A2\u0006\u0010Y\u001a\u00020 2\u0006\u0010^\u001a\u00020\u0005H\u0002J\u0019\u0010¬\u0001\u001a\u00020A2\u0006\u0010Y\u001a\u00020 2\u0006\u0010^\u001a\u00020\u0005H\u0002J\u0018\u0010\u00ad\u0001\u001a\u00020A2\u0006\u0010Y\u001a\u00020 H\u0082@¢\u0006\u0003\u0010®\u0001J\u001a\u0010¯\u0001\u001a\u00020A2\u0006\u0010Y\u001a\u00020 2\u0007\u0010°\u0001\u001a\u00020\\H\u0002J\t\u0010±\u0001\u001a\u00020AH\u0002J\u0011\u0010²\u0001\u001a\u00020\u00052\u0006\u0010Y\u001a\u00020 H\u0002J\u0018\u0010³\u0001\u001a\u00020\u00052\u0006\u0010Y\u001a\u00020 H\u0082@¢\u0006\u0003\u0010®\u0001R\u0014\u0010\u0004\u001a\u00020\u0005X\u0096D¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0014\u0010\b\u001a\u00020\tX\u0096D¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R$\u0010\u0016\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0018\u0018\u00010\u0017X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001f\u001a\u0004\u0018\u00010 X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u0004\u0018\u00010 X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010&\u001a\u0004\u0018\u00010'X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010+\u001a\u0004\u0018\u00010,X\u0082\u000e¢\u0006\u0002\n\u0000R(\u0010.\u001a\u0004\u0018\u00010,2\b\u0010-\u001a\u0004\u0018\u00010,8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b/\u00100\"\u0004\b1\u00102R\u001c\u00103\u001a\u0004\u0018\u00010#X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b4\u00105\"\u0004\b6\u00107R\u001c\u00108\u001a\u0004\u0018\u000109X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b:\u0010;\"\u0004\b<\u0010=R(\u0010>\u001a\u001c\u0012\u0004\u0012\u00020@\u0012\u0004\u0012\u00020%\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020A\u0018\u00010?X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010B\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010C\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010D\u001a\u00020EX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010F\u001a\u00020GX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u00020#X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010I\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010J\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010K\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010L\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010M\u001a\u00020NX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010O\u001a\u00020NX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010P\u001a\u00020NX\u0082\u000e¢\u0006\u0002\n\u0000R&\u0010Q\u001a\u001a\u0012\u0004\u0012\u00020N\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020T\u0012\u0004\u0012\u00020T0S0RX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010U\u001a\u00020NX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010V\u001a\u00020NX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010W\u001a\u00020\u001eX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010~\u001a\u0004\u0018\u00010\u007fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u0080\u0001\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u0081\u0001\u001a\u00020NX\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u0082\u0001\u001a\u00020NX\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u0083\u0001\u001a\u00020NX\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u0084\u0001\u001a\u00020NX\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u0085\u0001\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u0086\u0001\u001a\u00020NX\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0087\u0001\u001a\u0005\u0018\u00010\u0088\u0001X\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u0089\u0001\u001a\u00020NX\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u008a\u0001\u001a\u00020NX\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u008b\u0001\u001a\u00020NX\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u008c\u0001\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u008d\u0001\u001a\u00030\u008e\u0001X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u008f\u0001\u001a\t\u0012\u0004\u0012\u00020@0\u0090\u0001X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0091\u0001\u001a\u0004\u0018\u00010'X\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u0092\u0001\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u0093\u0001\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u0094\u0001\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u0095\u0001\u001a\u00020NX\u0082D¢\u0006\u0002\n\u0000R\u0011\u0010\u009c\u0001\u001a\u0004\u0018\u00010\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u009d\u0001\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u009e\u0001\u001a\u00020NX\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u009f\u0001\u001a\u00020NX\u0082\u000e¢\u0006\u0002\n\u0000R\u000f\u0010 \u0001\u001a\u00020NX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006µ\u0001"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeProjectionAdapter;", "Lcom/carplay/adapter/PhoneProjectionAdapter;", "<init>", "()V", "name", "", "getName", "()Ljava/lang/String;", "requiresScreenGrant", "", "getRequiresScreenGrant", "()Z", "capabilities", "Lcom/carplay/adapter/ProjectionCapabilities;", "getCapabilities", "()Lcom/carplay/adapter/ProjectionCapabilities;", "observer", "Lcom/carplay/adapter/ProjectionObserver;", "session", "Lcom/carplay/adapter/carlife/CarLifeSession;", "mediaPublisher", "Lcom/carplay/adapter/carlife/CarLifeMediaPublisher;", "mediaSnapshotProvider", "Lkotlin/Function0;", "Lcom/carplay/adapter/carlife/CarLifeMediaSnapshot;", "getMediaSnapshotProvider", "()Lkotlin/jvm/functions/Function0;", "setMediaSnapshotProvider", "(Lkotlin/jvm/functions/Function0;)V", "started", "Ljava/util/concurrent/atomic/AtomicBoolean;", "hostContext", "Landroid/content/Context;", "lifecycleContext", "lifecycleLock", "", "lifecycleGeneration", "", "hostRetryThread", "Ljava/lang/Thread;", "hostRetryGeneration", "lastHostStartAt", "lastRetiredAt", "_panel", "Lcom/carplay/adapter/carlife/CarLifePanel;", "value", "panel", "getPanel", "()Lcom/carplay/adapter/carlife/CarLifePanel;", "setPanel", "(Lcom/carplay/adapter/carlife/CarLifePanel;)V", "safeAreaPolicy", "getSafeAreaPolicy", "()Ljava/lang/Object;", "setSafeAreaPolicy", "(Ljava/lang/Object;)V", "videoProcessor", "Lcom/carplay/adapter/carlife/CarLifeVideoProcessor;", "getVideoProcessor", "()Lcom/carplay/adapter/carlife/CarLifeVideoProcessor;", "setVideoProcessor", "(Lcom/carplay/adapter/carlife/CarLifeVideoProcessor;)V", "frameSink", "Lkotlin/Function3;", "", "", "ptsBaseSeconds", "ptsCounter", "keyFrameGate", "Lcom/carplay/adapter/carlife/KeyFrameRebuildGate;", "videoAssembler", "Lcom/carplay/adapter/carlife/CarLifeVideoAssembler;", "videoStateLock", "videoEpoch", "recoveryRound", "encoderGeneration", "lastPts", "streamFps", "", "sourceWidth", "sourceHeight", "activeTouchPoints", "Ljava/util/concurrent/ConcurrentHashMap;", "Lkotlin/Pair;", "", "touchMappingLogged", "bridgeTouchLogged", "mediaDataSeen", "isSupported", "context", "warmUp", "connectIntent", "Landroid/content/Intent;", "startCarLifeHost", "reason", "setObserver", "setGrant", "grant", "Lcom/carplay/adapter/ProjectionGrant;", "configureAudio", "sampleRate", "channels", "pcm", "requestKeyFrame", "requestScreenRestoreKeyFrame", "requestCarLifeKeyFrame", "retry", "deliverMicrophone", "prepare", "width", "height", "fps", "(Landroid/content/Context;IIILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startProjection", "onFrameEncoded", "(Lkotlin/jvm/functions/Function3;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "abortStart", "generation", "live", "publisher", "isLifecycleActive", "handleBridgeTouch", "action", "x", "y", "handleBridgeAudio", "audioEncoder", "Landroid/media/MediaCodec;", "negotiatedPcm", "negotiatedSampleRate", "negotiatedChannels", "audioSampleRate", "audioChannels", "audioPtsUs", "audioBytesPerFrame", "resampler", "Lcom/carplay/media/Pcm16Resampler;", "resamplerInputRate", "resamplerOutputRate", "resamplerChannels", "aacOutputDiagnosticLogged", "audioQueueLock", "Ljava/lang/Object;", "audioQueue", "Ljava/util/ArrayDeque;", "audioWorker", "audioQueueDrops", "audioConfigGeneration", "audioWorkerGeneration", "audioQueueLimit", "enqueueAudio", "startAudioWorker", "encodeAudio", "releaseAudioEncoder", "stripAdtsHeader", "bytes", "preparedAddress", "preparedHostId", "preparedWidth", "preparedHeight", "preparedFps", "presentationTime", "timestampSeconds", "onTouchEvent", "pointerId", "onKeyEvent", "keyCode", "isDown", "stopProjection", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "releaseCarLife", "logCarLifeInstallState", "retireCarLifeProcess", "notifyHost", "(Landroid/content/Context;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "launchRetry", "intent", "cancelHostRetry", "stableHostId", "localAddress", "Companion", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarLifeProjectionAdapter implements PhoneProjectionAdapter {
    public static final String CARLIFE_ACTIVITY = "com.baidu.carlife.CarlifeActivity";
    public static final String CARLIFE_CONNECT_SERVICE = "com.baidu.carlife.service.CarlifeConnectService";
    public static final int CARLIFE_ENCODER_FPS = 30;
    public static final int CARLIFE_ENCODER_HEIGHT = 600;
    public static final int CARLIFE_ENCODER_WIDTH = 1024;
    public static final String CARLIFE_PACKAGE = "com.baidu.carlife.honor";
    public static final int HOST_NOTIFY_ATTEMPTS = 30;
    public static final long HOST_NOTIFY_INTERVAL_MS = 3200;
    public static final long HOST_RETIRE_COOLDOWN_MS = 15000;
    public static final long HOST_RETIRE_SETTLE_MS = 350;
    public static final long HOST_START_DEBOUNCE_MS = 2500;
    private volatile CarLifePanel _panel;
    private boolean aacOutputDiagnosticLogged;
    private int audioBytesPerFrame;
    private long audioConfigGeneration;
    private MediaCodec audioEncoder;
    private long audioPtsUs;
    private long audioQueueDrops;
    private Thread audioWorker;
    private int bridgeTouchLogged;
    private long encoderGeneration;
    private volatile Function3<? super byte[], ? super Long, ? super Boolean, Unit> frameSink;
    private volatile Context hostContext;
    private long hostRetryGeneration;
    private volatile Thread hostRetryThread;
    private long lastHostStartAt;
    private volatile long lastPts;
    private long lastRetiredAt;
    private volatile Context lifecycleContext;
    private long lifecycleGeneration;
    private volatile CarLifeMediaPublisher mediaPublisher;
    private volatile Function0<CarLifeMediaSnapshot> mediaSnapshotProvider;
    private volatile boolean negotiatedPcm;
    private ProjectionObserver observer;
    private volatile String preparedAddress;
    private volatile int preparedHeight;
    private volatile int preparedWidth;
    private volatile long ptsCounter;
    private long recoveryRound;
    private final boolean requiresScreenGrant;
    private Pcm16Resampler resampler;
    private int resamplerChannels;
    private int resamplerInputRate;
    private int resamplerOutputRate;
    private volatile Object safeAreaPolicy;
    private volatile CarLifeSession session;
    private volatile int sourceHeight;
    private volatile int sourceWidth;
    private int touchMappingLogged;
    private long videoEpoch;
    private volatile CarLifeVideoProcessor videoProcessor;
    private final String name = "CarLife 车机模式";
    private final AtomicBoolean started = new AtomicBoolean(false);
    private final Object lifecycleLock = new Object();
    private volatile long ptsBaseSeconds = -1;
    private final KeyFrameRebuildGate keyFrameGate = new KeyFrameRebuildGate();
    private final CarLifeVideoAssembler videoAssembler = new CarLifeVideoAssembler();
    private final Object videoStateLock = new Object();
    private volatile int streamFps = 30;
    private final ConcurrentHashMap<Integer, Pair<Float, Float>> activeTouchPoints = new ConcurrentHashMap<>();
    private final AtomicBoolean mediaDataSeen = new AtomicBoolean(false);
    private volatile int negotiatedSampleRate = 48000;
    private volatile int negotiatedChannels = 2;
    private volatile int audioSampleRate = 48000;
    private volatile int audioChannels = 2;
    private final Object audioQueueLock = new Object();
    private final ArrayDeque<byte[]> audioQueue = new ArrayDeque<>();
    private long audioWorkerGeneration = -1;
    private final int audioQueueLimit = 24;
    private volatile String preparedHostId = "carlink-cast-host";
    private volatile int preparedFps = 30;

    /* JADX INFO: renamed from: com.carplay.adapter.carlife.CarLifeProjectionAdapter$prepare$1, reason: invalid class name */
    /* JADX INFO: compiled from: CarLifeProjectionAdapter.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.carplay.adapter.carlife.CarLifeProjectionAdapter", f = "CarLifeProjectionAdapter.kt", i = {0, 0, 0, 0, 0, 1, 1, 1, 1, 1}, l = {373, 375}, m = "prepare", n = {"context", "hostId", "width", "height", "fps", "context", "hostId", "width", "height", "fps"}, s = {"L$0", "L$1", "I$0", "I$1", "I$2", "L$0", "L$1", "I$0", "I$1", "I$2"})
    static final class AnonymousClass1 extends ContinuationImpl {
        int I$0;
        int I$1;
        int I$2;
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return CarLifeProjectionAdapter.this.prepare(null, 0, 0, 0, this);
        }
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
        return new ProjectionCapabilities(true, true, true, false, "由手机自带 CarLife 渲染车机界面并直接输出 H.264，无需录屏授权");
    }

    public final Function0<CarLifeMediaSnapshot> getMediaSnapshotProvider() {
        return this.mediaSnapshotProvider;
    }

    public final void setMediaSnapshotProvider(Function0<CarLifeMediaSnapshot> function0) {
        this.mediaSnapshotProvider = function0;
    }

    /* JADX INFO: renamed from: getPanel, reason: from getter */
    public final CarLifePanel get_panel() {
        return this._panel;
    }

    public final void setPanel(CarLifePanel value) {
        ProjectionObserver projectionObserver;
        CarLifePanel old = this._panel;
        this._panel = value;
        if (!Intrinsics.areEqual(old, value) && value != null && (projectionObserver = this.observer) != null) {
            projectionObserver.diagnostic("安全区初始化：panel=" + value.getPanelWidth() + "x" + value.getPanelHeight() + ", safe=@(" + value.getSafeX() + "," + value.getSafeY() + ")/" + value.getSafeWidth() + "x" + value.getSafeHeight() + ", drawUiOutside=" + value.getDrawUiOutsideSafeArea() + ", needAdapterScreen=" + value.getNeedAdapterScreen());
        }
    }

    public final Object getSafeAreaPolicy() {
        return this.safeAreaPolicy;
    }

    public final void setSafeAreaPolicy(Object obj) {
        this.safeAreaPolicy = obj;
    }

    public final CarLifeVideoProcessor getVideoProcessor() {
        return this.videoProcessor;
    }

    public final void setVideoProcessor(CarLifeVideoProcessor carLifeVideoProcessor) {
        this.videoProcessor = carLifeVideoProcessor;
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public boolean isSupported(Context context) {
        Object objM159constructorimpl;
        Intrinsics.checkNotNullParameter(context, "context");
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLifeProjectionAdapter carLifeProjectionAdapter = this;
            context.getPackageManager().getPackageInfo("com.baidu.carlife.honor", 0);
            objM159constructorimpl = Result.m159constructorimpl(true);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = false;
        }
        return ((Boolean) objM159constructorimpl).booleanValue();
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public void warmUp(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.lifecycleContext = context.getApplicationContext();
        logCarLifeInstallState(context, "预热");
        retireCarLifeProcess(context, "预热前清理旧进程");
        startCarLifeHost(context, "预热");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Intent connectIntent(Context context) {
        Intent $this$connectIntent_u24lambda_u241 = new Intent();
        $this$connectIntent_u24lambda_u241.setClassName("com.baidu.carlife.honor", CARLIFE_CONNECT_SERVICE);
        $this$connectIntent_u24lambda_u241.putExtra("bindPackageName", context.getPackageName());
        $this$connectIntent_u24lambda_u241.putExtra("bindClassName", CarLifeHostService.class.getName());
        $this$connectIntent_u24lambda_u241.putExtra("bindVersion", "1.0");
        $this$connectIntent_u24lambda_u241.putExtra("usb_type", 0);
        $this$connectIntent_u24lambda_u241.putExtra("manufacturer", "honor");
        $this$connectIntent_u24lambda_u241.putExtra("is_box", false);
        return $this$connectIntent_u24lambda_u241;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean startCarLifeHost(Context context, String reason) {
        Object objM159constructorimpl;
        Object result;
        ProjectionObserver projectionObserver;
        synchronized (this.lifecycleLock) {
            long now = SystemClock.uptimeMillis();
            if (now - this.lastHostStartAt < HOST_START_DEBOUNCE_MS) {
                ProjectionObserver projectionObserver2 = this.observer;
                if (projectionObserver2 != null) {
                    projectionObserver2.diagnostic(reason + "：跳过重复启动请求，距离上次启动 " + (now - this.lastHostStartAt) + "ms");
                }
                return true;
            }
            this.lastHostStartAt = now;
            Unit unit = Unit.INSTANCE;
            Intent intent = connectIntent(context);
            try {
                Result.Companion companion = Result.INSTANCE;
                CarLifeProjectionAdapter carLifeProjectionAdapter = this;
                objM159constructorimpl = Result.m159constructorimpl(context.getPackageManager().resolveService(intent, 131072));
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            if (Result.m165isFailureimpl(objM159constructorimpl)) {
                objM159constructorimpl = null;
            }
            ResolveInfo resolved = (ResolveInfo) objM159constructorimpl;
            if (resolved == null) {
                ProjectionObserver projectionObserver3 = this.observer;
                if (projectionObserver3 != null) {
                    projectionObserver3.diagnostic(reason + "：CarLife 服务不可解析：com.baidu.carlife.honor/com.baidu.carlife.service.CarlifeConnectService；请确认 Honor CarLife+ 组件完整安装且未被系统停用");
                    return false;
                }
                return false;
            }
            try {
                Result.Companion companion3 = Result.INSTANCE;
                CarLifeProjectionAdapter carLifeProjectionAdapter2 = this;
                result = Result.m159constructorimpl(context.startService(intent));
            } catch (Throwable th2) {
                Result.Companion companion4 = Result.INSTANCE;
                result = Result.m159constructorimpl(ResultKt.createFailure(th2));
            }
            Throwable failure = Result.m162exceptionOrNullimpl(result);
            if (failure != null) {
                try {
                    Result.Companion companion5 = Result.INSTANCE;
                    if (!(failure instanceof IllegalStateException)) {
                        throw failure;
                    }
                    ProjectionObserver projectionObserver4 = this.observer;
                    if (projectionObserver4 != null) {
                        projectionObserver4.diagnostic(reason + "：普通启动被系统后台限制，改用前台服务启动 CarLife");
                    }
                    result = Result.m159constructorimpl(context.startForegroundService(intent));
                } catch (Throwable th3) {
                    Result.Companion companion6 = Result.INSTANCE;
                    result = Result.m159constructorimpl(ResultKt.createFailure(th3));
                }
            }
            Throwable it = Result.m162exceptionOrNullimpl(result);
            if (it != null && (projectionObserver = this.observer) != null) {
                String simpleName = it.getClass().getSimpleName();
                String message = it.getMessage();
                ServiceInfo serviceInfo = resolved.serviceInfo;
                String str = serviceInfo != null ? serviceInfo.packageName : null;
                ServiceInfo serviceInfo2 = resolved.serviceInfo;
                projectionObserver.diagnostic(reason + "：启动 CarLife 失败：" + simpleName + ": " + message + "; component=" + str + "/" + (serviceInfo2 != null ? serviceInfo2.name : null));
            }
            if (Result.m166isSuccessimpl(result)) {
                ComponentName component = (ComponentName) (Result.m165isFailureimpl(result) ? null : result);
                ProjectionObserver projectionObserver5 = this.observer;
                if (component != null) {
                    if (projectionObserver5 != null) {
                        projectionObserver5.diagnostic(reason + "：已请求启动 CarLife 服务（" + component.flattenToShortString() + "）；等待 CarLife 自行绑定宿主");
                    }
                } else if (projectionObserver5 != null) {
                    projectionObserver5.diagnostic(reason + "：CarLife 服务未返回组件，等待 TCP 会话验证");
                }
            }
            return Result.m166isSuccessimpl(result);
        }
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public void setObserver(ProjectionObserver observer) {
        Intrinsics.checkNotNullParameter(observer, "observer");
        this.observer = observer;
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public void setGrant(ProjectionGrant grant) {
        Intrinsics.checkNotNullParameter(grant, "grant");
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public void configureAudio(int sampleRate, int channels, boolean pcm) {
        if (CollectionsKt.listOf((Object[]) new Integer[]{44100, 48000}).contains(Integer.valueOf(sampleRate))) {
            this.negotiatedSampleRate = sampleRate;
        }
        this.negotiatedChannels = channels == 1 ? 1 : 2;
        this.negotiatedPcm = pcm;
        synchronized (this.audioQueueLock) {
            this.audioConfigGeneration++;
            this.audioQueueLock.notifyAll();
            Unit unit = Unit.INSTANCE;
        }
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public void requestKeyFrame() {
        requestCarLifeKeyFrame(false);
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public void requestScreenRestoreKeyFrame() {
        requestCarLifeKeyFrame(true);
    }

    private final void requestCarLifeKeyFrame(boolean retry) {
        Object objM159constructorimpl;
        synchronized (this.videoStateLock) {
            KeyFrameRebuildGate keyFrameRebuildGate = this.keyFrameGate;
            try {
                boolean accepted = retry ? keyFrameRebuildGate.beginRetry() : keyFrameRebuildGate.request();
                if (!accepted) {
                    ProjectionObserver projectionObserver = this.observer;
                    if (projectionObserver != null) {
                        projectionObserver.diagnostic("CarLife 关键帧请求已合并（上一轮仍在等待 IDR）");
                    }
                    return;
                }
                this.recoveryRound++;
                ProjectionObserver projectionObserver2 = this.observer;
                if (projectionObserver2 != null) {
                    projectionObserver2.diagnostic("CarLife 恢复轮次 #" + this.recoveryRound + "：" + (retry ? "屏幕恢复重试" : "关键帧请求") + "，重置视频组装器并等待 VIDEO_ENCODER_INIT_DONE");
                }
                this.videoAssembler.reset(true);
                Unit unit = Unit.INSTANCE;
                CarLifeSession live = this.session;
                if (live == null) {
                    synchronized (this.videoStateLock) {
                        this.keyFrameGate.reset();
                        Unit unit2 = Unit.INSTANCE;
                    }
                    ProjectionObserver projectionObserver3 = this.observer;
                    if (projectionObserver3 != null) {
                        projectionObserver3.diagnostic("关键帧请求被忽略：CarLife 会话尚未启动");
                        return;
                    }
                    return;
                }
                try {
                    Result.Companion companion = Result.INSTANCE;
                    CarLifeProjectionAdapter $this$requestCarLifeKeyFrame_u24lambda_u2411 = this;
                    Unit unit3 = null;
                    if (live.requestKeyFrame()) {
                        ProjectionObserver projectionObserver4 = $this$requestCarLifeKeyFrame_u24lambda_u2411.observer;
                        if (projectionObserver4 != null) {
                            projectionObserver4.diagnostic("已发送 CarLife VIDEO_ENCODER_INIT，等待新的 SPS/PPS 和 IDR");
                            unit3 = Unit.INSTANCE;
                        }
                    } else {
                        synchronized ($this$requestCarLifeKeyFrame_u24lambda_u2411.videoStateLock) {
                            $this$requestCarLifeKeyFrame_u24lambda_u2411.keyFrameGate.reset();
                            Unit unit4 = Unit.INSTANCE;
                        }
                        ProjectionObserver projectionObserver5 = $this$requestCarLifeKeyFrame_u24lambda_u2411.observer;
                        if (projectionObserver5 != null) {
                            projectionObserver5.diagnostic("CarLife 尚未就绪或恢复请求已合并；本轮不再重建编码器");
                            unit3 = Unit.INSTANCE;
                        }
                    }
                    objM159constructorimpl = Result.m159constructorimpl(unit3);
                } catch (Throwable th) {
                    Result.Companion companion2 = Result.INSTANCE;
                    objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
                }
                Throwable it = Result.m162exceptionOrNullimpl(objM159constructorimpl);
                if (it != null) {
                    synchronized (this.videoStateLock) {
                        this.keyFrameGate.reset();
                        Unit unit5 = Unit.INSTANCE;
                    }
                    ProjectionObserver projectionObserver6 = this.observer;
                    if (projectionObserver6 != null) {
                        projectionObserver6.diagnostic("请求 CarLife 重建编码器失败：" + it.getMessage());
                    }
                }
            } catch (Throwable th2) {
                throw th2;
            }
        }
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public boolean deliverMicrophone(byte[] pcm, int sampleRate, int channels) {
        Intrinsics.checkNotNullParameter(pcm, "pcm");
        return false;
    }

    /* JADX WARN: Code duplicated, block: B:29:0x00fe A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:30:0x00ff  */
    /* JADX WARN: Code duplicated, block: B:33:0x0108  */
    /* JADX WARN: Code duplicated, block: B:7:0x0014  */
    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public Object prepare(Context context, int width, int height, int fps, Continuation<? super Boolean> continuation) throws Throwable {
        AnonymousClass1 anonymousClass1;
        String hostId;
        Object objLocalAddress;
        CarLifeProjectionAdapter carLifeProjectionAdapter;
        ProjectionObserver projectionObserver;
        if (continuation instanceof AnonymousClass1) {
            anonymousClass1 = (AnonymousClass1) continuation;
            if ((anonymousClass1.label & Integer.MIN_VALUE) != 0) {
                anonymousClass1.label -= Integer.MIN_VALUE;
            } else {
                anonymousClass1 = new AnonymousClass1(continuation);
            }
        } else {
            anonymousClass1 = new AnonymousClass1(continuation);
        }
        Object $result = anonymousClass1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (anonymousClass1.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                if (!(width > 0 && height > 0 && fps > 0)) {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
                this.preparedWidth = width;
                this.preparedHeight = height;
                this.preparedFps = fps;
                this.sourceWidth = 0;
                this.sourceHeight = 0;
                this.streamFps = fps;
                this.bridgeTouchLogged = 0;
                this.mediaDataSeen.set(false);
                this.hostContext = context.getApplicationContext();
                this.lifecycleContext = context.getApplicationContext();
                retireCarLifeProcess(context, "准备新会话");
                CarLifeBridge.INSTANCE.clear();
                CarLifeBridge.INSTANCE.setLog(new Function1() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda1
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return CarLifeProjectionAdapter.prepare$lambda$14(this.f$0, (String) obj);
                    }
                });
                CarLifeBridge.INSTANCE.setWidth(width);
                CarLifeBridge.INSTANCE.setHeight(height);
                CarLifeBridge.INSTANCE.setFps(fps);
                hostId = stableHostId(context);
                this.preparedHostId = hostId;
                CarLifeBridge.INSTANCE.setDeviceId(hostId);
                CarLifeBridge carLifeBridge = CarLifeBridge.INSTANCE;
                String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
                if (string == null) {
                    string = "";
                }
                carLifeBridge.setDeviceVCode(string);
                anonymousClass1.L$0 = context;
                anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(hostId);
                anonymousClass1.I$0 = width;
                anonymousClass1.I$1 = height;
                anonymousClass1.I$2 = fps;
                anonymousClass1.label = 1;
                if (notifyHost(context, anonymousClass1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(context);
                anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(hostId);
                anonymousClass1.L$2 = this;
                anonymousClass1.I$0 = width;
                anonymousClass1.I$1 = height;
                anonymousClass1.I$2 = fps;
                anonymousClass1.label = 2;
                objLocalAddress = localAddress(context, anonymousClass1);
                if (objLocalAddress == coroutine_suspended) {
                    return coroutine_suspended;
                }
                carLifeProjectionAdapter = this;
                carLifeProjectionAdapter.preparedAddress = (String) objLocalAddress;
                projectionObserver = this.observer;
                if (projectionObserver != null) {
                    projectionObserver.diagnostic("CarLife 车机模式：连接 " + this.preparedAddress + ":7240，AirPlay 画布 " + width + "x" + height + "@" + fps + "；CarLife 编码尺寸由手机决定");
                }
                return Boxing.boxBoolean(true);
            case 1:
                fps = anonymousClass1.I$2;
                height = anonymousClass1.I$1;
                width = anonymousClass1.I$0;
                hostId = (String) anonymousClass1.L$1;
                context = (Context) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                anonymousClass1.L$0 = SpillingKt.nullOutSpilledVariable(context);
                anonymousClass1.L$1 = SpillingKt.nullOutSpilledVariable(hostId);
                anonymousClass1.L$2 = this;
                anonymousClass1.I$0 = width;
                anonymousClass1.I$1 = height;
                anonymousClass1.I$2 = fps;
                anonymousClass1.label = 2;
                objLocalAddress = localAddress(context, anonymousClass1);
                if (objLocalAddress == coroutine_suspended) {
                    return coroutine_suspended;
                }
                carLifeProjectionAdapter = this;
                carLifeProjectionAdapter.preparedAddress = (String) objLocalAddress;
                projectionObserver = this.observer;
                if (projectionObserver != null) {
                    projectionObserver.diagnostic("CarLife 车机模式：连接 " + this.preparedAddress + ":7240，AirPlay 画布 " + width + "x" + height + "@" + fps + "；CarLife 编码尺寸由手机决定");
                }
                return Boxing.boxBoolean(true);
            case 2:
                fps = anonymousClass1.I$2;
                height = anonymousClass1.I$1;
                width = anonymousClass1.I$0;
                carLifeProjectionAdapter = (CarLifeProjectionAdapter) anonymousClass1.L$2;
                ResultKt.throwOnFailure($result);
                objLocalAddress = $result;
                carLifeProjectionAdapter.preparedAddress = (String) objLocalAddress;
                projectionObserver = this.observer;
                if (projectionObserver != null) {
                    projectionObserver.diagnostic("CarLife 车机模式：连接 " + this.preparedAddress + ":7240，AirPlay 画布 " + width + "x" + height + "@" + fps + "；CarLife 编码尺寸由手机决定");
                }
                return Boxing.boxBoolean(true);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    static final Unit prepare$lambda$14(CarLifeProjectionAdapter this$0, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        ProjectionObserver projectionObserver = this$0.observer;
        if (projectionObserver != null) {
            projectionObserver.diagnostic(it);
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public Object startProjection(Function3<? super byte[], ? super Long, ? super Boolean, Unit> function3, Continuation<? super Unit> continuation) throws Throwable {
        int i;
        Unit unit;
        Unit unit2;
        long generation;
        Object objM159constructorimpl;
        synchronized (this.lifecycleLock) {
            if (!this.started.compareAndSet(false, true)) {
                ProjectionObserver projectionObserver = this.observer;
                if (projectionObserver != null) {
                    projectionObserver.diagnostic("CarLife 投屏重复启动请求，沿用当前会话");
                }
                return Unit.INSTANCE;
            }
            this.lifecycleGeneration++;
            long generation2 = this.lifecycleGeneration;
            final ProjectionObserver observer = this.observer;
            String address = this.preparedAddress;
            if (address == null) {
                throw new IllegalStateException("CarLife 尚未准备就绪".toString());
            }
            this.streamFps = this.preparedFps;
            synchronized (this.videoStateLock) {
                this.videoEpoch++;
                this.recoveryRound = 0L;
                this.encoderGeneration = 0L;
                this.frameSink = function3;
                CarLifeVideoAssembler.reset$default(this.videoAssembler, false, 1, null);
                this.keyFrameGate.reset();
                Unit unit3 = Unit.INSTANCE;
            }
            CarLifeVideoProcessor processor = this.videoProcessor;
            if (processor != null) {
                try {
                    Result.Companion companion = Result.INSTANCE;
                    final CarLifeProjectionAdapter $this$startProjection_u24lambda_u2427_u24lambda_u2424 = this;
                    Context context = $this$startProjection_u24lambda_u2427_u24lambda_u2424.hostContext;
                    if (context == null) {
                        throw new IllegalStateException("Required value was null.".toString());
                    }
                    processor.start(context, $this$startProjection_u24lambda_u2427_u24lambda_u2424.preparedWidth, $this$startProjection_u24lambda_u2427_u24lambda_u2424.preparedHeight, $this$startProjection_u24lambda_u2427_u24lambda_u2424.preparedFps, new Function1() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda2
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return CarLifeProjectionAdapter.startProjection$lambda$27$lambda$24$lambda$17(observer, (byte[]) obj);
                        }
                    }, new Function3() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda12
                        @Override // kotlin.jvm.functions.Function3
                        public final Object invoke(Object obj, Object obj2, Object obj3) {
                            return CarLifeProjectionAdapter.startProjection$lambda$27$lambda$24$lambda$18(this.f$0, (byte[]) obj, ((Long) obj2).longValue(), ((Boolean) obj3).booleanValue());
                        }
                    }, new Function1() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda13
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return CarLifeProjectionAdapter.startProjection$lambda$27$lambda$24$lambda$19(observer, (String) obj);
                        }
                    }, new Function1() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda14
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return CarLifeProjectionAdapter.startProjection$lambda$27$lambda$24$lambda$22(observer, (Throwable) obj);
                        }
                    }, new Function0() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda15
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return CarLifeProjectionAdapter.startProjection$lambda$27$lambda$24$lambda$23(this.f$0);
                        }
                    });
                    objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
                    Throwable failure = Result.m162exceptionOrNullimpl(objM159constructorimpl);
                    if (failure != null) {
                        String message = failure.getMessage();
                        if (message == null) {
                            message = "";
                        }
                        String text = message;
                        unit = null;
                        if (StringsKt.contains$default((CharSequence) text, (CharSequence) "已经启动", false, 2, (Object) null)) {
                            i = 1;
                        } else if (StringsKt.contains$default((CharSequence) text, (CharSequence) "重复启动", false, 2, (Object) null)) {
                            i = 1;
                            unit = null;
                        } else {
                            i = 1;
                            if (StringsKt.contains((CharSequence) text, (CharSequence) "already started", true)) {
                                unit = null;
                            } else {
                                if (observer != null) {
                                    String simpleName = failure.getClass().getSimpleName();
                                    String message2 = failure.getMessage();
                                    if (message2 == null) {
                                        message2 = "(无消息)";
                                    }
                                    observer.diagnostic("全画布合成器启动失败：" + simpleName + ": " + message2);
                                }
                                if (observer != null) {
                                    String simpleName2 = failure.getClass().getSimpleName();
                                    String message3 = failure.getMessage();
                                    if (message3 == null) {
                                        message3 = "(无消息)";
                                    }
                                    observer.diagnostic("全画布合成器不可用，回退到 CarLife 原始视频：" + simpleName2 + ": " + message3);
                                }
                                try {
                                    Result.Companion companion2 = Result.INSTANCE;
                                    CarLifeProjectionAdapter carLifeProjectionAdapter = this;
                                    processor.stop();
                                    Result.m159constructorimpl(Unit.INSTANCE);
                                } catch (Throwable th) {
                                    Result.Companion companion3 = Result.INSTANCE;
                                    Result.m159constructorimpl(ResultKt.createFailure(th));
                                }
                                unit = null;
                                this.videoProcessor = null;
                            }
                        }
                        if (observer != null) {
                            observer.diagnostic("全画布合成器已在运行，忽略重复启动，不回退原始 CarLife 视频");
                        }
                    } else {
                        i = 1;
                        unit = null;
                    }
                } catch (Throwable th2) {
                    Result.Companion companion4 = Result.INSTANCE;
                    objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th2));
                }
            } else {
                i = 1;
                unit = null;
            }
            if (!isLifecycleActive(generation2)) {
                try {
                    Result.Companion companion5 = Result.INSTANCE;
                    CarLifeProjectionAdapter $this$startProjection_u24lambda_u2428 = this;
                    CarLifeVideoProcessor carLifeVideoProcessor = $this$startProjection_u24lambda_u2428.videoProcessor;
                    if (carLifeVideoProcessor != null) {
                        carLifeVideoProcessor.stop();
                        unit2 = Unit.INSTANCE;
                    } else {
                        unit2 = unit;
                    }
                    Result.m159constructorimpl(unit2);
                } catch (Throwable th3) {
                    Result.Companion companion6 = Result.INSTANCE;
                    Result.m159constructorimpl(ResultKt.createFailure(th3));
                }
                return Unit.INSTANCE;
            }
            this.activeTouchPoints.clear();
            this.touchMappingLogged = 0;
            final long sourceEpoch = this.videoEpoch;
            Integer numBoxInt = Boxing.boxInt(this.preparedWidth);
            int it = numBoxInt.intValue();
            int it2 = it > 0 ? i : 0;
            if (it2 == 0) {
                numBoxInt = unit;
            }
            int iIntValue = numBoxInt != 0 ? numBoxInt.intValue() : 1024;
            Integer numBoxInt2 = Boxing.boxInt(this.preparedHeight);
            int it3 = numBoxInt2.intValue();
            int it4 = it3 > 0 ? i : 0;
            if (it4 == 0) {
                numBoxInt2 = unit;
            }
            int iIntValue2 = numBoxInt2 != 0 ? numBoxInt2.intValue() : CARLIFE_ENCODER_HEIGHT;
            Integer numBoxInt3 = Boxing.boxInt(this.preparedFps);
            int it5 = numBoxInt3.intValue();
            int it6 = it5 > 0 ? i : 0;
            if (it6 == 0) {
                numBoxInt3 = unit;
            }
            final CarLifeSession live = new CarLifeSession(address, iIntValue, iIntValue2, numBoxInt3 != 0 ? numBoxInt3.intValue() : 30, new Function1() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda16
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return CarLifeProjectionAdapter.startProjection$lambda$32(observer, (String) obj);
                }
            }, this.preparedHostId, get_panel(), new Function3() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda17
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj, Object obj2, Object obj3) {
                    return CarLifeProjectionAdapter.startProjection$lambda$36(this.f$0, observer, sourceEpoch, ((Integer) obj).intValue(), ((Integer) obj2).intValue(), ((Integer) obj3).intValue());
                }
            }, new Function3() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda18
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj, Object obj2, Object obj3) {
                    return CarLifeProjectionAdapter.startProjection$lambda$42(this.f$0, sourceEpoch, observer, (byte[]) obj, ((Long) obj2).longValue(), ((Boolean) obj3).booleanValue());
                }
            }, new Function3() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda19
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj, Object obj2, Object obj3) {
                    return CarLifeProjectionAdapter.startProjection$lambda$45(this.f$0, observer, ((Integer) obj).intValue(), ((Integer) obj2).intValue(), ((Integer) obj3).intValue());
                }
            }, new Function1() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda20
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return CarLifeProjectionAdapter.startProjection$lambda$46(this.f$0, observer, (byte[]) obj);
                }
            }, new Function0() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda3
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return CarLifeProjectionAdapter.startProjection$lambda$48(this.f$0, observer);
                }
            }, new Function3() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda4
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj, Object obj2, Object obj3) {
                    return CarLifeProjectionAdapter.startProjection$lambda$49(observer, ((Integer) obj).intValue(), ((Integer) obj2).intValue(), ((Integer) obj3).intValue());
                }
            }, new Function1() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda5
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return CarLifeProjectionAdapter.startProjection$lambda$50(observer, (byte[]) obj);
                }
            }, new Function1() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda6
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return CarLifeProjectionAdapter.startProjection$lambda$51(observer, (Throwable) obj);
                }
            }, this);
            Context context2 = this.hostContext;
            if (context2 == null) {
                throw new IllegalStateException("Required value was null.".toString());
            }
            CarLifeMediaPublisher publisher = new CarLifeMediaPublisher(context2, new Function1() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda7
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return CarLifeProjectionAdapter.startProjection$lambda$52(observer, (String) obj);
                }
            }, this.mediaSnapshotProvider);
            synchronized (this.lifecycleLock) {
                try {
                    try {
                        if (this.started.get()) {
                            generation = generation2;
                            if (this.lifecycleGeneration == generation) {
                                this.session = live;
                                this.mediaPublisher = publisher;
                                CarLifeBridge.INSTANCE.setStop(new Function0() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda8
                                    @Override // kotlin.jvm.functions.Function0
                                    public final Object invoke() {
                                        return CarLifeProjectionAdapter.startProjection$lambda$57$lambda$53(observer);
                                    }
                                });
                                CarLifeBridge.INSTANCE.setTouch(new Function3() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda9
                                    @Override // kotlin.jvm.functions.Function3
                                    public final Object invoke(Object obj, Object obj2, Object obj3) {
                                        return CarLifeProjectionAdapter.startProjection$lambda$57$lambda$54(this.f$0, live, observer, ((Integer) obj).intValue(), ((Float) obj2).floatValue(), ((Float) obj3).floatValue());
                                    }
                                });
                                CarLifeBridge.INSTANCE.setKey(new Function2() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda10
                                    @Override // kotlin.jvm.functions.Function2
                                    public final Object invoke(Object obj, Object obj2) {
                                        return CarLifeProjectionAdapter.startProjection$lambda$57$lambda$55(observer, ((Integer) obj).intValue(), ((Boolean) obj2).booleanValue());
                                    }
                                });
                                CarLifeBridge.INSTANCE.setAudio(new Function2() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda11
                                    @Override // kotlin.jvm.functions.Function2
                                    public final Object invoke(Object obj, Object obj2) {
                                        return CarLifeProjectionAdapter.startProjection$lambda$57$lambda$56(this.f$0, observer, (byte[]) obj, ((Integer) obj2).intValue());
                                    }
                                });
                                publisher.start();
                                Unit unit4 = Unit.INSTANCE;
                                if (!isLifecycleActive(generation)) {
                                    abortStart(generation, live, publisher);
                                    return Unit.INSTANCE;
                                }
                                startAudioWorker(observer);
                                if (!isLifecycleActive(generation)) {
                                    abortStart(generation, live, publisher);
                                    return Unit.INSTANCE;
                                }
                                try {
                                    CarLifeSession.connect$default(live, 0, 1, null);
                                    return Unit.INSTANCE;
                                } catch (Throwable failure2) {
                                    abortStart(generation, live, publisher);
                                    throw failure2;
                                }
                            }
                        } else {
                            generation = generation2;
                        }
                        abortStart(generation, live, publisher);
                        return Unit.INSTANCE;
                    } catch (Throwable th4) {
                        th = th4;
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                }
            }
        }
    }

    static final Unit startProjection$lambda$27$lambda$24$lambda$17(ProjectionObserver $observer, byte[] it) {
        Intrinsics.checkNotNullParameter(it, "it");
        if ($observer != null) {
            $observer.videoConfig(it);
        }
        return Unit.INSTANCE;
    }

    static final Unit startProjection$lambda$27$lambda$24$lambda$18(CarLifeProjectionAdapter $this_runCatching, byte[] bytes, long ptsUs, boolean keyFrame) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        Function3<? super byte[], ? super Long, ? super Boolean, Unit> function3 = $this_runCatching.frameSink;
        if (function3 != null) {
            function3.invoke(bytes, Long.valueOf(ptsUs), Boolean.valueOf(keyFrame));
        }
        return Unit.INSTANCE;
    }

    static final Unit startProjection$lambda$27$lambda$24$lambda$19(ProjectionObserver $observer, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        if ($observer != null) {
            $observer.diagnostic(it);
        }
        return Unit.INSTANCE;
    }

    static final Unit startProjection$lambda$27$lambda$24$lambda$22(final ProjectionObserver $observer, final Throwable error) {
        Intrinsics.checkNotNullParameter(error, "error");
        Thread $this$startProjection_u24lambda_u2427_u24lambda_u2424_u24lambda_u2422_u24lambda_u2421 = new Thread(new Runnable() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CarLifeProjectionAdapter.startProjection$lambda$27$lambda$24$lambda$22$lambda$20($observer, error);
            }
        }, "carlink-composer-failure");
        $this$startProjection_u24lambda_u2427_u24lambda_u2424_u24lambda_u2422_u24lambda_u2421.setDaemon(true);
        $this$startProjection_u24lambda_u2427_u24lambda_u2424_u24lambda_u2422_u24lambda_u2421.start();
        return Unit.INSTANCE;
    }

    static final void startProjection$lambda$27$lambda$24$lambda$22$lambda$20(ProjectionObserver $observer, Throwable $error) {
        if ($observer != null) {
            $observer.failed($error);
        }
    }

    static final Unit startProjection$lambda$27$lambda$24$lambda$23(CarLifeProjectionAdapter $this_runCatching) {
        $this_runCatching.requestKeyFrame();
        return Unit.INSTANCE;
    }

    static final Unit startProjection$lambda$32(ProjectionObserver $observer, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        if ($observer != null) {
            $observer.diagnostic(it);
        }
        return Unit.INSTANCE;
    }

    static final Unit startProjection$lambda$36(CarLifeProjectionAdapter this$0, ProjectionObserver $observer, long $sourceEpoch, int width, int height, int fps) {
        long generation;
        String str;
        this$0.streamFps = fps;
        synchronized (this$0.videoStateLock) {
            this$0.encoderGeneration++;
        }
        synchronized (this$0.videoStateLock) {
            generation = this$0.encoderGeneration;
        }
        synchronized (this$0.videoStateLock) {
            if ($sourceEpoch == this$0.videoEpoch) {
                this$0.sourceWidth = width;
                this$0.sourceHeight = height;
            }
            Unit unit = Unit.INSTANCE;
        }
        if ($observer != null) {
            int i = this$0.preparedWidth;
            int i2 = this$0.preparedHeight;
            if (this$0.recoveryRound > 0) {
                str = "（恢复轮次 #" + this$0.recoveryRound + "）";
            } else {
                str = "";
            }
            $observer.diagnostic("CarLife 编码器 generation=" + generation + " 就绪：" + width + "x" + height + "@" + fps + "；AirPlay 合成画布 " + i + "x" + i2 + str);
        }
        return Unit.INSTANCE;
    }

    static final Unit startProjection$lambda$42(CarLifeProjectionAdapter this$0, long $sourceEpoch, ProjectionObserver $observer, byte[] data, long timestampSeconds, boolean keyFrame) throws Throwable {
        Object obj;
        Function3<? super byte[], ? super Long, ? super Boolean, Unit> function3;
        Intrinsics.checkNotNullParameter(data, "data");
        Object obj2 = this$0.videoStateLock;
        synchronized (obj2) {
            try {
                if ($sourceEpoch != this$0.videoEpoch || (function3 = this$0.frameSink) == null) {
                    obj = obj2;
                } else {
                    try {
                        CarLifeVideoBatch batch = this$0.videoAssembler.offer(data, timestampSeconds, keyFrame);
                        if (batch == null) {
                            obj = obj2;
                        } else {
                            byte[] it = batch.getAvcC();
                            if (it != null) {
                                if ($observer != null) {
                                    obj = obj2;
                                    try {
                                        $observer.diagnostic("CarLife " + (batch.getReusedAvcC() ? "复用上一轮 SPS/PPS" : "新 SPS/PPS") + " 已组装：" + it.length + "B，encoder generation=" + this$0.encoderGeneration + "，恢复轮次 #" + this$0.recoveryRound);
                                    } catch (Throwable th) {
                                        th = th;
                                        throw th;
                                    }
                                } else {
                                    obj = obj2;
                                }
                                CarLifeVideoProcessor processor = this$0.videoProcessor;
                                if (processor != null) {
                                    Integer numValueOf = Integer.valueOf(this$0.sourceWidth);
                                    int width = numValueOf.intValue();
                                    if (!Boolean.valueOf(width > 0).booleanValue()) {
                                        numValueOf = null;
                                    }
                                    int iIntValue = numValueOf != null ? numValueOf.intValue() : 1024;
                                    Integer numValueOf2 = Integer.valueOf(this$0.sourceHeight);
                                    int height = numValueOf2.intValue();
                                    Integer num = height > 0 ? numValueOf2 : null;
                                    processor.configureInput(iIntValue, num != null ? num.intValue() : CARLIFE_ENCODER_HEIGHT, this$0.streamFps, it);
                                } else if ($observer != null) {
                                    $observer.videoConfig(it);
                                }
                            } else {
                                obj = obj2;
                            }
                            Iterable $this$forEach$iv = batch.getFrames();
                            int $i$f$forEach = 0;
                            Iterator it2 = $this$forEach$iv.iterator();
                            while (it2.hasNext()) {
                                Object element$iv = it2.next();
                                CarLifeVideoFrame frame = (CarLifeVideoFrame) element$iv;
                                if (frame.getKeyFrame() && $observer != null) {
                                    $observer.diagnostic("CarLife 新 IDR 已到达：" + frame.getData().length + "B，encoder generation=" + this$0.encoderGeneration + "，恢复轮次 #" + this$0.recoveryRound);
                                }
                                long ptsUs = this$0.presentationTime(frame.getTimestampSeconds());
                                CarLifeVideoProcessor processor2 = this$0.videoProcessor;
                                if (processor2 == null) {
                                    function3.invoke(VideoPackets.INSTANCE.lengthPrefixed(frame.getData()), Long.valueOf(ptsUs), Boolean.valueOf(frame.getKeyFrame()));
                                } else {
                                    processor2.offerFrame(frame.getData(), ptsUs, frame.getKeyFrame());
                                }
                                if (frame.getKeyFrame()) {
                                    this$0.keyFrameGate.observeFrame(true);
                                }
                                $i$f$forEach = $i$f$forEach;
                                it2 = it2;
                                $this$forEach$iv = $this$forEach$iv;
                                batch = batch;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        obj = obj2;
                        throw th;
                    }
                }
                Unit unit = Unit.INSTANCE;
                return Unit.INSTANCE;
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    static final Unit startProjection$lambda$45(CarLifeProjectionAdapter this$0, ProjectionObserver $observer, int sampleRate, int channels, int i) {
        String str;
        synchronized (this$0.audioQueueLock) {
            Integer numValueOf = Integer.valueOf(sampleRate);
            int it = numValueOf.intValue();
            boolean z = false;
            int i2 = 1;
            if (8000 <= it && it < 96001) {
                z = true;
            }
            if (!Boolean.valueOf(z).booleanValue()) {
                numValueOf = null;
            }
            this$0.audioSampleRate = numValueOf != null ? numValueOf.intValue() : 48000;
            if (channels != 1) {
                i2 = 2;
            }
            this$0.audioChannels = i2;
            this$0.audioConfigGeneration++;
            this$0.audioQueue.clear();
            this$0.audioQueueLock.notifyAll();
            Unit unit = Unit.INSTANCE;
        }
        if (this$0.negotiatedPcm) {
            if ($observer != null) {
                int i3 = this$0.audioSampleRate;
                int i4 = this$0.audioChannels;
                if (this$0.audioSampleRate == this$0.negotiatedSampleRate) {
                    str = "";
                } else {
                    str = "（重采样到 " + this$0.negotiatedSampleRate + "Hz）";
                }
                $observer.diagnostic("CarLife 音频：" + i3 + "Hz / " + i4 + " 声道，LPCM 直通" + str);
            }
        } else if ($observer != null) {
            $observer.diagnostic("CarLife 音频：" + this$0.audioSampleRate + "Hz / " + this$0.audioChannels + " 声道，转为 AAC " + this$0.negotiatedSampleRate + "Hz");
        }
        return Unit.INSTANCE;
    }

    static final Unit startProjection$lambda$46(CarLifeProjectionAdapter this$0, ProjectionObserver $observer, byte[] it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.mediaDataSeen.set(true);
        this$0.enqueueAudio(it, $observer);
        return Unit.INSTANCE;
    }

    static final Unit startProjection$lambda$48(CarLifeProjectionAdapter this$0, ProjectionObserver $observer) {
        synchronized (this$0.audioQueueLock) {
            this$0.audioQueue.clear();
            this$0.audioConfigGeneration++;
            this$0.audioQueueLock.notifyAll();
            Unit unit = Unit.INSTANCE;
        }
        if ($observer != null) {
            $observer.diagnostic("CarLife 音频来源已结束：清空音频队列，保留视频/触控会话");
        }
        return Unit.INSTANCE;
    }

    static final Unit startProjection$lambda$49(ProjectionObserver $observer, int sampleRate, int channels, int format) {
        if ($observer != null) {
            $observer.diagnostic("CarLife TTS 输入：" + sampleRate + "Hz/" + channels + "ch 格式=" + format + "（暂不混入媒体）");
        }
        return Unit.INSTANCE;
    }

    static final Unit startProjection$lambda$50(ProjectionObserver $observer, byte[] data) {
        Intrinsics.checkNotNullParameter(data, "data");
        if ($observer != null) {
            $observer.diagnostic("CarLife TTS 数据：" + data.length + "B（独立提示音来源，媒体流不受影响）");
        }
        return Unit.INSTANCE;
    }

    static final Unit startProjection$lambda$51(ProjectionObserver $observer, Throwable error) {
        Intrinsics.checkNotNullParameter(error, "error");
        if ($observer != null) {
            $observer.failed(error);
        }
        return Unit.INSTANCE;
    }

    static final Unit startProjection$lambda$52(ProjectionObserver $observer, String message) {
        Intrinsics.checkNotNullParameter(message, "message");
        if ($observer != null) {
            $observer.diagnostic(message);
        }
        return Unit.INSTANCE;
    }

    static final Unit startProjection$lambda$57$lambda$53(ProjectionObserver $observer) {
        if ($observer != null) {
            $observer.diagnostic("CarLife 请求停止投屏；开始释放 AirPlay/音频会话");
        }
        if ($observer != null) {
            $observer.failed(new IllegalStateException("CarLife 请求停止投屏"));
        }
        return Unit.INSTANCE;
    }

    static final Unit startProjection$lambda$57$lambda$54(CarLifeProjectionAdapter this$0, CarLifeSession $live, ProjectionObserver $observer, int action, float x, float y) {
        this$0.handleBridgeTouch($live, action, x, y, $observer);
        return Unit.INSTANCE;
    }

    static final Unit startProjection$lambda$57$lambda$55(ProjectionObserver $observer, int carLifeCode, boolean down) {
        if (!down) {
            return Unit.INSTANCE;
        }
        Integer androidCode = CarLifeHardKey.INSTANCE.toAndroid(carLifeCode);
        if (androidCode != null && $observer != null) {
            $observer.diagnostic("车机硬键已转换：CarLife code=" + carLifeCode + " → Android KeyCode=" + androidCode);
        }
        return Unit.INSTANCE;
    }

    static final Unit startProjection$lambda$57$lambda$56(CarLifeProjectionAdapter this$0, ProjectionObserver $observer, byte[] pcm, int sampleRate) {
        Intrinsics.checkNotNullParameter(pcm, "pcm");
        this$0.handleBridgeAudio(pcm, sampleRate, $observer);
        return Unit.INSTANCE;
    }

    private final void abortStart(long generation, CarLifeSession live, CarLifeMediaPublisher publisher) {
        Unit unit;
        Unit unit2;
        synchronized (this.lifecycleLock) {
            unit = null;
            if (this.lifecycleGeneration == generation) {
                this.lifecycleGeneration++;
                this.started.set(false);
                if (this.session == live) {
                    this.session = null;
                }
                if (publisher != null && this.mediaPublisher == publisher) {
                    this.mediaPublisher = null;
                }
                CarLifeBridge.INSTANCE.clear();
            }
            Unit unit3 = Unit.INSTANCE;
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLifeProjectionAdapter $this$abortStart_u24lambda_u2459 = this;
            Thread thread = $this$abortStart_u24lambda_u2459.audioWorker;
            if (thread != null) {
                thread.interrupt();
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
            CarLifeProjectionAdapter carLifeProjectionAdapter = this;
            if (publisher != null) {
                publisher.close();
                unit = Unit.INSTANCE;
            }
            Result.m159constructorimpl(unit);
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        try {
            Result.Companion companion5 = Result.INSTANCE;
            CarLifeProjectionAdapter carLifeProjectionAdapter2 = this;
            live.close();
            Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th3) {
            Result.Companion companion6 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th3));
        }
    }

    private final boolean isLifecycleActive(long generation) {
        boolean z;
        synchronized (this.lifecycleLock) {
            z = this.started.get() && this.lifecycleGeneration == generation;
        }
        return z;
    }

    private final void handleBridgeTouch(CarLifeSession live, int action, float x, float y, ProjectionObserver observer) {
        Pair<Float, Float> pairMapPanelTouchToSource;
        String panelInfo;
        String processorInfo;
        Integer numValueOf = Integer.valueOf(this.sourceWidth);
        int it = numValueOf.intValue();
        int it2 = it > 0 ? 1 : 0;
        if (it2 == 0) {
            numValueOf = null;
        }
        int targetWidth = numValueOf != null ? numValueOf.intValue() : this.preparedWidth;
        Integer numValueOf2 = Integer.valueOf(this.sourceHeight);
        int it3 = numValueOf2.intValue();
        int it4 = it3 > 0 ? 1 : 0;
        if (it4 == 0) {
            numValueOf2 = null;
        }
        int targetHeight = numValueOf2 != null ? numValueOf2.intValue() : this.preparedHeight;
        Integer numValueOf3 = Integer.valueOf(CarLifeBridge.INSTANCE.getCastWidth());
        int it5 = numValueOf3.intValue();
        int it6 = it5 > 0 ? 1 : 0;
        if (it6 == 0) {
            numValueOf3 = null;
        }
        int virtualWidth = numValueOf3 != null ? numValueOf3.intValue() : targetWidth;
        Integer numValueOf4 = Integer.valueOf(CarLifeBridge.INSTANCE.getCastHeight());
        int it7 = numValueOf4.intValue();
        int it8 = it7 > 0 ? 1 : 0;
        if (it8 == 0) {
            numValueOf4 = null;
        }
        int virtualHeight = numValueOf4 != null ? numValueOf4.intValue() : targetHeight;
        CarLifePanel geometry = get_panel();
        Pair<Float, Float> pairMapVirtualTouchToPanel = geometry != null ? geometry.mapVirtualTouchToPanel(x, y, virtualWidth, virtualHeight) : null;
        float panelX = pairMapVirtualTouchToPanel != null ? pairMapVirtualTouchToPanel.getFirst().floatValue() : (targetWidth * x) / virtualWidth;
        float panelY = pairMapVirtualTouchToPanel != null ? pairMapVirtualTouchToPanel.getSecond().floatValue() : (targetHeight * y) / virtualHeight;
        boolean usedSafeAreaMapping = pairMapVirtualTouchToPanel != null;
        if (this.videoProcessor != null) {
            CarLifeVideoProcessor carLifeVideoProcessor = this.videoProcessor;
            pairMapPanelTouchToSource = carLifeVideoProcessor != null ? carLifeVideoProcessor.mapTouchToSource(panelX, panelY) : null;
        } else {
            pairMapPanelTouchToSource = geometry != null ? geometry.mapPanelTouchToSource(panelX, panelY, targetWidth, targetHeight) : null;
        }
        if (geometry != null && pairMapPanelTouchToSource == null) {
            if (observer != null) {
                String str = String.format("%.1f", Arrays.copyOf(new Object[]{Float.valueOf(panelX)}, 1));
                Intrinsics.checkNotNullExpressionValue(str, "format(...)");
                String str2 = String.format("%.1f", Arrays.copyOf(new Object[]{Float.valueOf(panelY)}, 1));
                Intrinsics.checkNotNullExpressionValue(str2, "format(...)");
                observer.diagnostic("CarLife 触控安全区验证：panel=(" + str + "," + str2 + ") 落在安全区外 [safe=" + geometry.getSafeX() + "," + geometry.getSafeY() + "," + geometry.getSafeWidth() + "x" + geometry.getSafeHeight() + "]，已忽略");
                return;
            }
            return;
        }
        int targetWidth2 = targetWidth;
        int targetHeight2 = targetHeight;
        Pair<Float, Float> pair = pairMapPanelTouchToSource;
        boolean usedSafeAreaMapping2 = usedSafeAreaMapping;
        float mappedX = pair != null ? pair.getFirst().floatValue() : RangesKt.coerceIn(panelX, 0.0f, RangesKt.coerceAtLeast(targetWidth2 - 1, 0));
        float mappedY = pair != null ? pair.getSecond().floatValue() : RangesKt.coerceIn(panelY, 0.0f, RangesKt.coerceAtLeast(targetHeight2 - 1, 0));
        if (this.bridgeTouchLogged < 16) {
            if (geometry != null) {
                panelInfo = "panel=" + geometry.getPanelWidth() + "x" + geometry.getPanelHeight() + "@(" + geometry.getSafeX() + "," + geometry.getSafeY() + "/" + geometry.getSafeWidth() + "x" + geometry.getSafeHeight() + "), ";
            } else {
                panelInfo = "";
            }
            if (this.videoProcessor != null) {
                processorInfo = "通过视频处理器";
            } else {
                processorInfo = usedSafeAreaMapping2 ? "通过安全区映射" : "简单缩放";
            }
            if (observer != null) {
                int i = this.bridgeTouchLogged;
                this.bridgeTouchLogged = i + 1;
                String str3 = String.format("%.0f", Arrays.copyOf(new Object[]{Float.valueOf(x)}, 1));
                Intrinsics.checkNotNullExpressionValue(str3, "format(...)");
                String str4 = String.format("%.0f", Arrays.copyOf(new Object[]{Float.valueOf(y)}, 1));
                Intrinsics.checkNotNullExpressionValue(str4, "format(...)");
                String str5 = String.format("%.1f", Arrays.copyOf(new Object[]{Float.valueOf(panelX)}, 1));
                Intrinsics.checkNotNullExpressionValue(str5, "format(...)");
                String str6 = String.format("%.1f", Arrays.copyOf(new Object[]{Float.valueOf(panelY)}, 1));
                Intrinsics.checkNotNullExpressionValue(str6, "format(...)");
                String str7 = String.format("%.0f", Arrays.copyOf(new Object[]{Float.valueOf(mappedX)}, 1));
                Intrinsics.checkNotNullExpressionValue(str7, "format(...)");
                String str8 = String.format("%.0f", Arrays.copyOf(new Object[]{Float.valueOf(mappedY)}, 1));
                Intrinsics.checkNotNullExpressionValue(str8, "format(...)");
                observer.diagnostic("触控映射 #" + i + ": [virtual(" + str3 + "," + str4 + ")@" + virtualWidth + "x" + virtualHeight + "] → [panel(" + str5 + "," + str6 + ")] → [source(" + str7 + "," + str8 + ")@" + targetWidth2 + "x" + targetHeight2 + "] " + panelInfo + processorInfo);
            }
        } else {
            mappedY = mappedY;
            mappedX = mappedX;
        }
        live.touch(action, (int) mappedX, (int) mappedY);
    }

    private final void handleBridgeAudio(byte[] pcm, int sampleRate, ProjectionObserver observer) {
        if (this.mediaDataSeen.get()) {
            return;
        }
        if (!(pcm.length == 0)) {
            if (8000 <= sampleRate && sampleRate < 96001) {
                synchronized (this.audioQueueLock) {
                    if (this.audioSampleRate != sampleRate) {
                        this.audioSampleRate = sampleRate;
                        this.audioConfigGeneration++;
                        this.audioQueue.clear();
                    }
                    this.audioQueueLock.notifyAll();
                    Unit unit = Unit.INSTANCE;
                }
            }
            enqueueAudio(pcm, observer);
        }
    }

    private final void enqueueAudio(byte[] pcm, ProjectionObserver observer) {
        if ((pcm.length == 0) || !this.started.get()) {
            return;
        }
        synchronized (this.audioQueueLock) {
            if (this.started.get()) {
                if (this.audioQueue.size() >= this.audioQueueLimit) {
                    this.audioQueue.removeFirst();
                    this.audioQueueDrops++;
                    if ((this.audioQueueDrops == 1 || this.audioQueueDrops % 100 == 0) && observer != null) {
                        observer.diagnostic("CarLife 音频队列丢弃过期块：" + this.audioQueueDrops);
                    }
                }
                ArrayDeque<byte[]> arrayDeque = this.audioQueue;
                byte[] bArrCopyOf = Arrays.copyOf(pcm, pcm.length);
                Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
                arrayDeque.addLast(bArrCopyOf);
                this.audioQueueLock.notifyAll();
                Unit unit = Unit.INSTANCE;
            }
        }
    }

    private final void startAudioWorker(final ProjectionObserver observer) {
        if (!(this.audioWorker == null)) {
            throw new IllegalStateException("CarLife 音频线程已启动".toString());
        }
        Thread $this$startAudioWorker_u24lambda_u2474 = new Thread(new Runnable() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                CarLifeProjectionAdapter.startAudioWorker$lambda$73(this.f$0, observer);
            }
        }, "carlife-audio-encode");
        $this$startAudioWorker_u24lambda_u2474.setDaemon(true);
        this.audioWorker = $this$startAudioWorker_u24lambda_u2474;
        Thread thread = this.audioWorker;
        Intrinsics.checkNotNull(thread);
        thread.start();
    }

    static final void startAudioWorker$lambda$73(CarLifeProjectionAdapter this$0, ProjectionObserver $observer) {
        byte[] pcm;
        long generation;
        while (this$0.started.get()) {
            try {
                synchronized (this$0.audioQueueLock) {
                    while (this$0.audioQueue.isEmpty() && this$0.started.get()) {
                        try {
                            this$0.audioQueueLock.wait(250L);
                        } catch (InterruptedException e) {
                            if (!this$0.started.get()) {
                                synchronized (this$0.audioQueueLock) {
                                    this$0.audioQueue.clear();
                                    this$0.audioQueueLock.notifyAll();
                                    Unit unit = Unit.INSTANCE;
                                    this$0.releaseAudioEncoder();
                                    this$0.audioWorker = null;
                                    return;
                                }
                            }
                        }
                    }
                    pcm = !this$0.started.get() ? null : this$0.audioQueue.pollFirst();
                }
                if (pcm != null) {
                    synchronized (this$0.audioQueueLock) {
                        generation = this$0.audioConfigGeneration;
                    }
                    if (generation != this$0.audioWorkerGeneration) {
                        this$0.releaseAudioEncoder();
                        this$0.audioPtsUs = 0L;
                        this$0.audioWorkerGeneration = generation;
                    }
                    this$0.encodeAudio(pcm, $observer);
                }
            } catch (Throwable th) {
                synchronized (this$0.audioQueueLock) {
                    this$0.audioQueue.clear();
                    this$0.audioQueueLock.notifyAll();
                    Unit unit2 = Unit.INSTANCE;
                    this$0.releaseAudioEncoder();
                    this$0.audioWorker = null;
                    throw th;
                }
            }
        }
        synchronized (this$0.audioQueueLock) {
            this$0.audioQueue.clear();
            this$0.audioQueueLock.notifyAll();
            Unit unit3 = Unit.INSTANCE;
        }
        this$0.releaseAudioEncoder();
        this$0.audioWorker = null;
    }

    /* JADX WARN: Code duplicated, block: B:20:0x0038 A[Catch: Exception -> 0x020c, TryCatch #0 {Exception -> 0x020c, blocks: (B:3:0x0008, B:24:0x0055, B:25:0x005a, B:7:0x001c, B:9:0x0020, B:11:0x0026, B:13:0x002a, B:21:0x004d, B:20:0x0038, B:49:0x00b0, B:51:0x00b5, B:53:0x00f3, B:54:0x00f9, B:56:0x00fc, B:58:0x0106, B:61:0x010e, B:63:0x014f, B:65:0x0159, B:67:0x015d, B:69:0x0163, B:71:0x016d, B:72:0x017f, B:74:0x0189, B:76:0x018e, B:80:0x0198, B:82:0x01cb, B:85:0x01e5, B:29:0x006f, B:31:0x0073, B:33:0x0079, B:35:0x007d, B:47:0x0096, B:48:0x00ab), top: B:93:0x0008 }] */
    /* JADX WARN: Code duplicated, block: B:42:0x008f  */
    /* JADX WARN: Code duplicated, block: B:43:0x0090  */
    /* JADX WARN: Code duplicated, block: B:45:0x0093  */
    private final void encodeAudio(byte[] pcm, ProjectionObserver observer) {
        long j;
        byte[] bArrConvert;
        boolean z;
        MediaCodec encoder;
        int offset;
        Pcm16Resampler active;
        byte[] converted;
        CarLifeProjectionAdapter carLifeProjectionAdapter = this;
        try {
            int rate = carLifeProjectionAdapter.negotiatedSampleRate;
            int sourceRate = carLifeProjectionAdapter.audioSampleRate;
            if (carLifeProjectionAdapter.negotiatedPcm) {
                if (sourceRate == rate) {
                    converted = pcm;
                } else {
                    Pcm16Resampler pcm16Resampler = carLifeProjectionAdapter.resampler;
                    if (pcm16Resampler != null) {
                        active = carLifeProjectionAdapter.resamplerInputRate == sourceRate && carLifeProjectionAdapter.resamplerOutputRate == rate && carLifeProjectionAdapter.resamplerChannels == carLifeProjectionAdapter.audioChannels ? pcm16Resampler : null;
                        if (active == null) {
                            active = new Pcm16Resampler(sourceRate, rate, carLifeProjectionAdapter.audioChannels);
                            carLifeProjectionAdapter.resampler = active;
                            carLifeProjectionAdapter.resamplerInputRate = sourceRate;
                            carLifeProjectionAdapter.resamplerOutputRate = rate;
                            carLifeProjectionAdapter.resamplerChannels = carLifeProjectionAdapter.audioChannels;
                        }
                    } else {
                        active = new Pcm16Resampler(sourceRate, rate, carLifeProjectionAdapter.audioChannels);
                        carLifeProjectionAdapter.resampler = active;
                        carLifeProjectionAdapter.resamplerInputRate = sourceRate;
                        carLifeProjectionAdapter.resamplerOutputRate = rate;
                        carLifeProjectionAdapter.resamplerChannels = carLifeProjectionAdapter.audioChannels;
                    }
                    converted = active.convert(pcm);
                }
                if (observer != null) {
                    observer.audioFrame(converted, carLifeProjectionAdapter.audioPtsUs, true);
                }
                carLifeProjectionAdapter.audioPtsUs += (((long) (converted.length / (carLifeProjectionAdapter.audioChannels * 2))) * 1000000) / ((long) rate);
                return;
            }
            if (sourceRate == rate) {
                bArrConvert = pcm;
                j = 1000000;
            } else {
                Pcm16Resampler it = carLifeProjectionAdapter.resampler;
                if (it != null) {
                    if (carLifeProjectionAdapter.resamplerInputRate != sourceRate || carLifeProjectionAdapter.resamplerOutputRate != rate) {
                        j = 1000000;
                    } else {
                        j = 1000000;
                        z = carLifeProjectionAdapter.resamplerChannels == carLifeProjectionAdapter.audioChannels;
                        if (z) {
                            it = null;
                        }
                        if (it == null) {
                        }
                        bArrConvert = it.convert(pcm);
                    }
                    if (z) {
                        it = null;
                    }
                    if (it == null) {
                    }
                    bArrConvert = it.convert(pcm);
                } else {
                    j = 1000000;
                }
                it = new Pcm16Resampler(sourceRate, rate, carLifeProjectionAdapter.audioChannels);
                carLifeProjectionAdapter.resampler = it;
                carLifeProjectionAdapter.resamplerInputRate = sourceRate;
                carLifeProjectionAdapter.resamplerOutputRate = rate;
                carLifeProjectionAdapter.resamplerChannels = carLifeProjectionAdapter.audioChannels;
                bArrConvert = it.convert(pcm);
            }
            byte[] converted2 = bArrConvert;
            MediaCodec encoder2 = carLifeProjectionAdapter.audioEncoder;
            if (encoder2 == null) {
                MediaFormat format = MediaFormat.createAudioFormat("audio/mp4a-latm", rate, carLifeProjectionAdapter.audioChannels);
                format.setInteger("aac-profile", 2);
                format.setInteger("bitrate", 128000);
                format.setInteger("max-input-size", 65536);
                Intrinsics.checkNotNullExpressionValue(format, "apply(...)");
                encoder2 = MediaCodec.createEncoderByType("audio/mp4a-latm");
                encoder2.configure(format, (Surface) null, (MediaCrypto) null, 1);
                encoder2.start();
                carLifeProjectionAdapter.audioEncoder = encoder2;
                carLifeProjectionAdapter.audioBytesPerFrame = carLifeProjectionAdapter.audioChannels * 1024 * 2;
            }
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
            int offset2 = 0;
            while (offset2 < converted2.length) {
                int index = encoder2.dequeueInputBuffer(1000L);
                if (index < 0) {
                    encoder = encoder2;
                    offset = offset2;
                } else {
                    ByteBuffer input = encoder2.getInputBuffer(index);
                    if (input != null) {
                        input.clear();
                        int chunk = Math.min(input.capacity(), converted2.length - offset2);
                        input.put(converted2, offset2, chunk);
                        MediaCodec encoder3 = encoder2;
                        encoder3.queueInputBuffer(index, 0, chunk, carLifeProjectionAdapter.audioPtsUs, 0);
                        encoder = encoder3;
                        carLifeProjectionAdapter.audioPtsUs += (((long) (chunk / (carLifeProjectionAdapter.audioChannels * 2))) * j) / ((long) rate);
                        offset = offset2 + chunk;
                    } else {
                        return;
                    }
                }
                encoder2 = encoder;
                int out = encoder2.dequeueOutputBuffer(info, 0L);
                while (out >= 0) {
                    if (info.size > 0 && (info.flags & 2) == 0) {
                        byte[] bytes = new byte[info.size];
                        ByteBuffer $this$encodeAudio_u24lambda_u2480 = encoder2.getOutputBuffer(out);
                        if ($this$encodeAudio_u24lambda_u2480 != null) {
                            $this$encodeAudio_u24lambda_u2480.position(info.offset);
                            $this$encodeAudio_u24lambda_u2480.limit(info.offset + info.size);
                            $this$encodeAudio_u24lambda_u2480.get(bytes);
                        }
                        byte[] payload = carLifeProjectionAdapter.stripAdtsHeader(bytes);
                        if (!carLifeProjectionAdapter.aacOutputDiagnosticLogged) {
                            carLifeProjectionAdapter.aacOutputDiagnosticLogged = true;
                            if (observer != null) {
                                observer.diagnostic("CarLife AAC 首帧：" + bytes.length + "B，ADTS=" + (payload.length != bytes.length ? "已剥离" : "无") + "，输出=" + payload.length + "B");
                            }
                        }
                        if (observer != null) {
                            ProjectionObserver.audioFrame$default(observer, payload, info.presentationTimeUs, false, 4, null);
                        }
                    }
                    encoder2.releaseOutputBuffer(out, false);
                    int iDequeueOutputBuffer = encoder2.dequeueOutputBuffer(info, 0L);
                    encoder2 = encoder2;
                    carLifeProjectionAdapter = this;
                    out = iDequeueOutputBuffer;
                }
                carLifeProjectionAdapter = this;
                offset2 = offset;
            }
        } catch (Exception error) {
            if (observer != null) {
                observer.diagnostic("CarLife 音频编码失败：" + error.getMessage());
            }
        }
    }

    private final void releaseAudioEncoder() {
        Unit unit;
        Unit unit2;
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLifeProjectionAdapter $this$releaseAudioEncoder_u24lambda_u2481 = this;
            MediaCodec mediaCodec = $this$releaseAudioEncoder_u24lambda_u2481.audioEncoder;
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
            CarLifeProjectionAdapter $this$releaseAudioEncoder_u24lambda_u2482 = this;
            MediaCodec mediaCodec2 = $this$releaseAudioEncoder_u24lambda_u2482.audioEncoder;
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
        this.audioEncoder = null;
        this.audioPtsUs = 0L;
        this.aacOutputDiagnosticLogged = false;
        this.resampler = null;
        this.resamplerInputRate = 0;
        this.resamplerOutputRate = 0;
        this.resamplerChannels = 0;
    }

    private final byte[] stripAdtsHeader(byte[] bytes) {
        if (bytes.length < 7) {
            return bytes;
        }
        int sync = ((bytes[0] & UByte.MAX_VALUE) << 4) | ((bytes[1] & 240) >>> 4);
        if (sync != 4095) {
            return bytes;
        }
        int protectionAbsent = bytes[1] & 1;
        int headerSize = protectionAbsent != 1 ? 9 : 7;
        int frameLength = ((bytes[4] & UByte.MAX_VALUE) << 3) | ((bytes[3] & 3) << 11) | ((bytes[5] & 224) >>> 5);
        if (headerSize >= bytes.length || frameLength < headerSize || frameLength > bytes.length) {
            return bytes;
        }
        return ArraysKt.copyOfRange(bytes, headerSize, frameLength);
    }

    private final long presentationTime(long timestampSeconds) {
        if (timestampSeconds != this.ptsBaseSeconds) {
            this.ptsBaseSeconds = timestampSeconds;
            this.ptsCounter = 0L;
        }
        long interval = 1000000 / ((long) RangesKt.coerceAtLeast(this.streamFps, 1));
        long micros = this.ptsCounter * interval;
        this.ptsCounter++;
        long pts = (1000000 * timestampSeconds) + micros;
        if (pts <= this.lastPts) {
            pts = this.lastPts + interval;
        }
        this.lastPts = pts;
        return pts;
    }

    /* JADX WARN: Code duplicated, block: B:17:0x0032  */
    /* JADX WARN: Code duplicated, block: B:21:0x0066  */
    /* JADX WARN: Code duplicated, block: B:23:0x0074  */
    /* JADX WARN: Code duplicated, block: B:24:0x0076  */
    /* JADX WARN: Code duplicated, block: B:26:0x0079  */
    /* JADX WARN: Code duplicated, block: B:27:0x0094 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:28:0x0096  */
    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public void onTouchEvent(int action, float x, float y, int pointerId) {
        float f;
        float f2;
        Pair<Float, Float> pairMapTouchToSource;
        int index;
        ProjectionObserver projectionObserver;
        Pair<Float, Float> pairRemove;
        Pair<Float, Float> pair;
        CarLifeSession live = this.session;
        if (live == null) {
        }
        CarLifeVideoProcessor processor = this.videoProcessor;
        if (processor != null) {
            f = x;
            f2 = y;
            pairMapTouchToSource = processor.mapTouchToSource(f, f2);
            if (pairMapTouchToSource == null) {
            }
            switch (action) {
                case 0:
                    if (pairMapTouchToSource != null) {
                        this.activeTouchPoints.put(Integer.valueOf(pointerId), pairMapTouchToSource);
                        index = this.touchMappingLogged;
                        this.touchMappingLogged = index + 1;
                        if ((index >= 8 || index % 40 == 0) && (projectionObserver = this.observer) != null) {
                            String str = String.format("%.1f", Arrays.copyOf(new Object[]{Float.valueOf(f)}, 1));
                            Intrinsics.checkNotNullExpressionValue(str, "format(...)");
                            String str2 = String.format("%.1f", Arrays.copyOf(new Object[]{Float.valueOf(f2)}, 1));
                            Intrinsics.checkNotNullExpressionValue(str2, "format(...)");
                            projectionObserver.diagnostic("CarLife 触控映射 #" + index + " panel=(" + str + "," + str2 + ") → source=(" + ((int) pairMapTouchToSource.getFirst().floatValue()) + "," + ((int) pairMapTouchToSource.getSecond().floatValue()) + ") 尺寸=" + this.sourceWidth + "x" + this.sourceHeight + "，使用与视频相同的 viewport 逆变换");
                        }
                        live.touch(action, (int) pairMapTouchToSource.getFirst().floatValue(), (int) pairMapTouchToSource.getSecond().floatValue());
                    }
                    break;
                case 1:
                case 3:
                    pairRemove = this.activeTouchPoints.remove(Integer.valueOf(pointerId));
                    if (pairMapTouchToSource == null) {
                        pair = pairRemove;
                    } else {
                        pair = pairMapTouchToSource;
                    }
                    if (pair != null) {
                        live.touch(1, (int) pair.getFirst().floatValue(), (int) pair.getSecond().floatValue());
                    }
                    break;
                case 2:
                    if (!this.activeTouchPoints.containsKey(Integer.valueOf(pointerId)) && pairMapTouchToSource != null) {
                        this.activeTouchPoints.put(Integer.valueOf(pointerId), pairMapTouchToSource);
                        live.touch(action, (int) pairMapTouchToSource.getFirst().floatValue(), (int) pairMapTouchToSource.getSecond().floatValue());
                        break;
                    }
                    break;
            }
        }
        f = x;
        f2 = y;
        pairMapTouchToSource = processor == null ? TuplesKt.to(Float.valueOf(f), Float.valueOf(f2)) : null;
        switch (action) {
            case 0:
                if (pairMapTouchToSource != null) {
                    this.activeTouchPoints.put(Integer.valueOf(pointerId), pairMapTouchToSource);
                    index = this.touchMappingLogged;
                    this.touchMappingLogged = index + 1;
                    if (index >= 8) {
                        String str3 = String.format("%.1f", Arrays.copyOf(new Object[]{Float.valueOf(f)}, 1));
                        Intrinsics.checkNotNullExpressionValue(str3, "format(...)");
                        String str4 = String.format("%.1f", Arrays.copyOf(new Object[]{Float.valueOf(f2)}, 1));
                        Intrinsics.checkNotNullExpressionValue(str4, "format(...)");
                        projectionObserver.diagnostic("CarLife 触控映射 #" + index + " panel=(" + str3 + "," + str4 + ") → source=(" + ((int) pairMapTouchToSource.getFirst().floatValue()) + "," + ((int) pairMapTouchToSource.getSecond().floatValue()) + ") 尺寸=" + this.sourceWidth + "x" + this.sourceHeight + "，使用与视频相同的 viewport 逆变换");
                    } else {
                        String str5 = String.format("%.1f", Arrays.copyOf(new Object[]{Float.valueOf(f)}, 1));
                        Intrinsics.checkNotNullExpressionValue(str5, "format(...)");
                        String str6 = String.format("%.1f", Arrays.copyOf(new Object[]{Float.valueOf(f2)}, 1));
                        Intrinsics.checkNotNullExpressionValue(str6, "format(...)");
                        projectionObserver.diagnostic("CarLife 触控映射 #" + index + " panel=(" + str5 + "," + str6 + ") → source=(" + ((int) pairMapTouchToSource.getFirst().floatValue()) + "," + ((int) pairMapTouchToSource.getSecond().floatValue()) + ") 尺寸=" + this.sourceWidth + "x" + this.sourceHeight + "，使用与视频相同的 viewport 逆变换");
                    }
                    live.touch(action, (int) pairMapTouchToSource.getFirst().floatValue(), (int) pairMapTouchToSource.getSecond().floatValue());
                }
                break;
            case 1:
            case 3:
                pairRemove = this.activeTouchPoints.remove(Integer.valueOf(pointerId));
                if (pairMapTouchToSource == null) {
                    pair = pairRemove;
                } else {
                    pair = pairMapTouchToSource;
                }
                if (pair != null) {
                    live.touch(1, (int) pair.getFirst().floatValue(), (int) pair.getSecond().floatValue());
                }
                break;
            case 2:
                if (!this.activeTouchPoints.containsKey(Integer.valueOf(pointerId))) {
                }
                break;
        }
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public void onKeyEvent(int keyCode, boolean isDown) {
        if (isDown) {
            Integer numFromAndroid = CarLifeHardKey.INSTANCE.fromAndroid(keyCode);
            if (numFromAndroid == null) {
                CarLifeProjectionAdapter $this$onKeyEvent_u24lambda_u2483 = this;
                ProjectionObserver projectionObserver = $this$onKeyEvent_u24lambda_u2483.observer;
                if (projectionObserver != null) {
                    projectionObserver.diagnostic("车机按键 " + CarLifeHardKey.INSTANCE.describe(keyCode) + " 在 CarLife 里没有对应码，已忽略");
                    return;
                }
                return;
            }
            int carLife = numFromAndroid.intValue();
            CarLifeSession carLifeSession = this.session;
            if (carLifeSession != null) {
                carLifeSession.key(carLife);
            }
        }
    }

    @Override // com.carplay.adapter.PhoneProjectionAdapter
    public Object stopProjection(Continuation<? super Unit> continuation) {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        Object objM159constructorimpl3;
        Object objM159constructorimpl4;
        Object objM159constructorimpl5;
        ProjectionObserver projectionObserver;
        ProjectionObserver projectionObserver2;
        Unit unit;
        ProjectionObserver projectionObserver3;
        Unit unit2;
        ProjectionObserver projectionObserver4;
        Unit unit3;
        ProjectionObserver projectionObserver5;
        Unit unit4;
        try {
            synchronized (this.lifecycleLock) {
                this.lifecycleGeneration++;
                this.started.set(false);
                CarLifeBridge.INSTANCE.clear();
                Unit unit5 = Unit.INSTANCE;
            }
            cancelHostRetry();
            synchronized (this.audioQueueLock) {
                this.audioQueue.clear();
                this.audioQueueLock.notifyAll();
                Unit unit6 = Unit.INSTANCE;
            }
            Thread thread = this.audioWorker;
            if (thread != null) {
                thread.interrupt();
            }
            try {
                Result.Companion companion = Result.INSTANCE;
                CarLifeProjectionAdapter $this$stopProjection_u24lambda_u2486 = this;
                Thread thread2 = $this$stopProjection_u24lambda_u2486.audioWorker;
                if (thread2 != null) {
                    thread2.join(1500L);
                    unit4 = Unit.INSTANCE;
                } else {
                    unit4 = null;
                }
                objM159constructorimpl = Result.m159constructorimpl(unit4);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            Throwable it = Result.m162exceptionOrNullimpl(objM159constructorimpl);
            if (it != null && (projectionObserver5 = this.observer) != null) {
                projectionObserver5.diagnostic("等待 CarLife 音频线程结束失败：" + it.getMessage());
            }
            this.audioWorker = null;
            try {
                Result.Companion companion3 = Result.INSTANCE;
                CarLifeProjectionAdapter $this$stopProjection_u24lambda_u2488 = this;
                CarLifeSession carLifeSession = $this$stopProjection_u24lambda_u2488.session;
                if (carLifeSession != null) {
                    carLifeSession.close();
                    unit3 = Unit.INSTANCE;
                } else {
                    unit3 = null;
                }
                objM159constructorimpl2 = Result.m159constructorimpl(unit3);
            } catch (Throwable th2) {
                Result.Companion companion4 = Result.INSTANCE;
                objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th2));
            }
            Throwable it2 = Result.m162exceptionOrNullimpl(objM159constructorimpl2);
            if (it2 != null && (projectionObserver4 = this.observer) != null) {
                projectionObserver4.diagnostic("关闭 CarLife 会话失败：" + it2.getMessage());
            }
            this.session = null;
            try {
                Result.Companion companion5 = Result.INSTANCE;
                CarLifeProjectionAdapter $this$stopProjection_u24lambda_u2490 = this;
                CarLifeMediaPublisher carLifeMediaPublisher = $this$stopProjection_u24lambda_u2490.mediaPublisher;
                if (carLifeMediaPublisher != null) {
                    carLifeMediaPublisher.close();
                    unit2 = Unit.INSTANCE;
                } else {
                    unit2 = null;
                }
                objM159constructorimpl3 = Result.m159constructorimpl(unit2);
            } catch (Throwable th3) {
                Result.Companion companion6 = Result.INSTANCE;
                objM159constructorimpl3 = Result.m159constructorimpl(ResultKt.createFailure(th3));
            }
            Throwable it3 = Result.m162exceptionOrNullimpl(objM159constructorimpl3);
            if (it3 != null && (projectionObserver3 = this.observer) != null) {
                projectionObserver3.diagnostic("关闭 CarLife 媒体发布器失败：" + it3.getMessage());
            }
            this.mediaPublisher = null;
            try {
                Result.Companion companion7 = Result.INSTANCE;
                CarLifeProjectionAdapter $this$stopProjection_u24lambda_u2492 = this;
                CarLifeVideoProcessor carLifeVideoProcessor = $this$stopProjection_u24lambda_u2492.videoProcessor;
                if (carLifeVideoProcessor != null) {
                    carLifeVideoProcessor.stop();
                    unit = Unit.INSTANCE;
                } else {
                    unit = null;
                }
                objM159constructorimpl4 = Result.m159constructorimpl(unit);
            } catch (Throwable th4) {
                Result.Companion companion8 = Result.INSTANCE;
                objM159constructorimpl4 = Result.m159constructorimpl(ResultKt.createFailure(th4));
            }
            Throwable it4 = Result.m162exceptionOrNullimpl(objM159constructorimpl4);
            if (it4 != null && (projectionObserver2 = this.observer) != null) {
                projectionObserver2.diagnostic("关闭全画布合成器失败：" + it4.getMessage());
            }
            this.activeTouchPoints.clear();
            this.mediaSnapshotProvider = null;
            synchronized (this.videoStateLock) {
                this.videoEpoch++;
                this.recoveryRound = 0L;
                this.encoderGeneration = 0L;
                this.frameSink = null;
                CarLifeVideoAssembler.reset$default(this.videoAssembler, false, 1, null);
                this.keyFrameGate.reset();
                Unit unit7 = Unit.INSTANCE;
            }
            this.ptsBaseSeconds = -1L;
            this.ptsCounter = 0L;
            this.lastPts = 0L;
            synchronized (this.audioQueueLock) {
                this.audioConfigGeneration++;
                this.audioWorkerGeneration = -1L;
                Unit unit8 = Unit.INSTANCE;
            }
            try {
                Result.Companion companion9 = Result.INSTANCE;
                CarLifeProjectionAdapter $this$stopProjection_u24lambda_u2496 = this;
                $this$stopProjection_u24lambda_u2496.releaseAudioEncoder();
                objM159constructorimpl5 = Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th5) {
                Result.Companion companion10 = Result.INSTANCE;
                objM159constructorimpl5 = Result.m159constructorimpl(ResultKt.createFailure(th5));
            }
            Throwable it5 = Result.m162exceptionOrNullimpl(objM159constructorimpl5);
            if (it5 != null && (projectionObserver = this.observer) != null) {
                projectionObserver.diagnostic("释放 CarLife 音频编码器失败：" + it5.getMessage());
            }
            releaseCarLife();
            return Unit.INSTANCE;
        } catch (Throwable th6) {
            releaseCarLife();
            throw th6;
        }
    }

    private final void releaseCarLife() {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        Object objM159constructorimpl3;
        ProjectionObserver projectionObserver;
        Unit unit;
        ProjectionObserver projectionObserver2;
        Unit unit2;
        ProjectionObserver projectionObserver3;
        Unit unit3;
        Context context = this.hostContext;
        if (context == null && (context = this.lifecycleContext) == null) {
            return;
        }
        cancelHostRetry();
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLifeProjectionAdapter $this$releaseCarLife_u24lambda_u2498 = this;
            boolean stopped = context.stopService($this$releaseCarLife_u24lambda_u2498.connectIntent(context));
            ProjectionObserver projectionObserver4 = $this$releaseCarLife_u24lambda_u2498.observer;
            if (projectionObserver4 != null) {
                projectionObserver4.diagnostic("停止 CarLife 连接服务：返回=" + stopped);
                unit3 = Unit.INSTANCE;
            } else {
                unit3 = null;
            }
            objM159constructorimpl = Result.m159constructorimpl(unit3);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        Throwable it = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (it != null && (projectionObserver3 = this.observer) != null) {
            projectionObserver3.diagnostic("停止 CarLife 连接服务失败：" + it.getMessage());
        }
        try {
            Result.Companion companion3 = Result.INSTANCE;
            CarLifeProjectionAdapter $this$releaseCarLife_u24lambda_u24100 = this;
            boolean stopped2 = context.stopService(new Intent(context, (Class<?>) CarLifeHostService.class));
            ProjectionObserver projectionObserver5 = $this$releaseCarLife_u24lambda_u24100.observer;
            if (projectionObserver5 != null) {
                projectionObserver5.diagnostic("停止 CarLink 宿主服务：返回=" + stopped2);
                unit2 = Unit.INSTANCE;
            } else {
                unit2 = null;
            }
            objM159constructorimpl2 = Result.m159constructorimpl(unit2);
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        Throwable it2 = Result.m162exceptionOrNullimpl(objM159constructorimpl2);
        if (it2 != null && (projectionObserver2 = this.observer) != null) {
            projectionObserver2.diagnostic("停止 CarLink 宿主服务失败：" + it2.getMessage());
        }
        try {
            Result.Companion companion5 = Result.INSTANCE;
            CarLifeProjectionAdapter $this$releaseCarLife_u24lambda_u24102 = this;
            ActivityManager manager = (ActivityManager) context.getSystemService(ActivityManager.class);
            if (manager != null) {
                manager.killBackgroundProcesses("com.baidu.carlife.honor");
            }
            ProjectionObserver projectionObserver6 = $this$releaseCarLife_u24lambda_u24102.observer;
            if (projectionObserver6 != null) {
                projectionObserver6.diagnostic("已释放 CarLife，交还手机音频通道");
                unit = Unit.INSTANCE;
            } else {
                unit = null;
            }
            objM159constructorimpl3 = Result.m159constructorimpl(unit);
        } catch (Throwable th3) {
            Result.Companion companion6 = Result.INSTANCE;
            objM159constructorimpl3 = Result.m159constructorimpl(ResultKt.createFailure(th3));
        }
        Throwable it3 = Result.m162exceptionOrNullimpl(objM159constructorimpl3);
        if (it3 != null && (projectionObserver = this.observer) != null) {
            projectionObserver.diagnostic("释放 CarLife 失败：" + it3.getMessage());
        }
        CarLifeBridge.INSTANCE.resetForNewSession();
        this.hostContext = null;
        this.lifecycleContext = null;
    }

    private final void logCarLifeInstallState(Context context, String reason) {
        Object objM159constructorimpl;
        ProjectionObserver projectionObserver;
        Unit unit;
        String str;
        ServiceInfo serviceInfo;
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLifeProjectionAdapter $this$logCarLifeInstallState_u24lambda_u24104 = this;
            ApplicationInfo info = context.getPackageManager().getApplicationInfo("com.baidu.carlife.honor", 0);
            Intrinsics.checkNotNullExpressionValue(info, "getApplicationInfo(...)");
            ResolveInfo service = context.getPackageManager().resolveService($this$logCarLifeInstallState_u24lambda_u24104.connectIntent(context), 131072);
            ProjectionObserver projectionObserver2 = $this$logCarLifeInstallState_u24lambda_u24104.observer;
            if (projectionObserver2 != null) {
                boolean z = info.enabled;
                boolean z2 = (info.flags & 2097152) != 0;
                if (service == null || (serviceInfo = service.serviceInfo) == null || (str = serviceInfo.name) == null) {
                    str = "不可解析";
                }
                projectionObserver2.diagnostic(reason + "：CarLife 安装状态 enabled=" + z + " stopped=" + z2 + " hostService=" + str);
                unit = Unit.INSTANCE;
            } else {
                unit = null;
            }
            objM159constructorimpl = Result.m159constructorimpl(unit);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        Throwable it = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (it == null || (projectionObserver = this.observer) == null) {
            return;
        }
        projectionObserver.diagnostic(reason + "：CarLife 安装/服务检查失败：" + it.getClass().getSimpleName() + ": " + it.getMessage());
    }

    private final void retireCarLifeProcess(Context context, String reason) {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        ProjectionObserver projectionObserver;
        Unit unit;
        ProjectionObserver projectionObserver2;
        cancelHostRetry();
        long now = SystemClock.uptimeMillis();
        synchronized (this.lifecycleLock) {
            if (now - this.lastRetiredAt < 15000) {
                ProjectionObserver projectionObserver3 = this.observer;
                if (projectionObserver3 != null) {
                    projectionObserver3.diagnostic(reason + "：复用刚完成的 CarLife 进程清理");
                }
                return;
            }
            this.lastRetiredAt = now;
            Unit unit2 = Unit.INSTANCE;
            try {
                Result.Companion companion = Result.INSTANCE;
                CarLifeProjectionAdapter $this$retireCarLifeProcess_u24lambda_u24107 = this;
                objM159constructorimpl = Result.m159constructorimpl(Boolean.valueOf(context.stopService($this$retireCarLifeProcess_u24lambda_u24107.connectIntent(context))));
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            Throwable it = Result.m162exceptionOrNullimpl(objM159constructorimpl);
            if (it != null && (projectionObserver2 = this.observer) != null) {
                projectionObserver2.diagnostic(reason + "：停止 CarLife 服务失败：" + it.getMessage());
            }
            try {
                Result.Companion companion3 = Result.INSTANCE;
                CarLifeProjectionAdapter carLifeProjectionAdapter = this;
                ActivityManager activityManager = (ActivityManager) context.getSystemService(ActivityManager.class);
                if (activityManager != null) {
                    activityManager.killBackgroundProcesses("com.baidu.carlife.honor");
                    unit = Unit.INSTANCE;
                } else {
                    unit = null;
                }
                objM159constructorimpl2 = Result.m159constructorimpl(unit);
            } catch (Throwable th2) {
                Result.Companion companion4 = Result.INSTANCE;
                objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th2));
            }
            Throwable it2 = Result.m162exceptionOrNullimpl(objM159constructorimpl2);
            if (it2 != null && (projectionObserver = this.observer) != null) {
                projectionObserver.diagnostic(reason + "：释放 CarLife 进程失败：" + it2.getMessage());
            }
            CarLifeBridge.INSTANCE.resetForNewSession();
            try {
                Thread.sleep(350L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /* JADX INFO: renamed from: com.carplay.adapter.carlife.CarLifeProjectionAdapter$notifyHost$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: CarLifeProjectionAdapter.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.carplay.adapter.carlife.CarLifeProjectionAdapter$notifyHost$2", f = "CarLifeProjectionAdapter.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    static final class C00282 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ Context $context;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C00282(Context context, Continuation<? super C00282> continuation) {
            super(2, continuation);
            this.$context = context;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return CarLifeProjectionAdapter.this.new C00282(this.$context, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C00282) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) throws Throwable {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    Intent intent = CarLifeProjectionAdapter.this.connectIntent(this.$context);
                    CarLifeProjectionAdapter.this.startCarLifeHost(this.$context, "准备会话");
                    CarLifeProjectionAdapter.this.launchRetry(this.$context, intent);
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object notifyHost(Context context, Continuation<? super Unit> continuation) {
        Object objWithContext = BuildersKt.withContext(Dispatchers.getIO(), new C00282(context, null), continuation);
        return objWithContext == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objWithContext : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void launchRetry(final Context context, Intent intent) {
        final long generation;
        cancelHostRetry();
        synchronized (this.lifecycleLock) {
            this.hostRetryGeneration++;
            generation = this.hostRetryGeneration;
        }
        Thread $this$launchRetry_u24lambda_u24115 = new Thread(new Runnable() { // from class: com.carplay.adapter.carlife.CarLifeProjectionAdapter$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                CarLifeProjectionAdapter.launchRetry$lambda$114(this.f$0, context, generation);
            }
        }, "carlife-host-bind");
        $this$launchRetry_u24lambda_u24115.setDaemon(true);
        $this$launchRetry_u24lambda_u24115.start();
        this.hostRetryThread = $this$launchRetry_u24lambda_u24115;
    }

    static final void launchRetry$lambda$114(CarLifeProjectionAdapter this$0, Context $context, long $generation) {
        Object obj;
        ProjectionObserver projectionObserver;
        int attempt = 0;
        while (attempt < 30) {
            try {
                synchronized (this$0.lifecycleLock) {
                    if ($generation != this$0.hostRetryGeneration) {
                        synchronized (this$0.lifecycleLock) {
                            if ($generation == this$0.hostRetryGeneration) {
                                this$0.hostRetryThread = null;
                            }
                            Unit unit = Unit.INSTANCE;
                        }
                        return;
                    }
                    Unit unit2 = Unit.INSTANCE;
                    if (CarLifeBridge.INSTANCE.getCallbackReady()) {
                        ProjectionObserver projectionObserver2 = this$0.observer;
                        if (projectionObserver2 != null) {
                            projectionObserver2.diagnostic("CarLife 宿主绑定与 CastCallback 已就绪");
                        }
                        synchronized (this$0.lifecycleLock) {
                            if ($generation == this$0.hostRetryGeneration) {
                                this$0.hostRetryThread = null;
                            }
                            Unit unit3 = Unit.INSTANCE;
                        }
                        return;
                    }
                    if (attempt > 0) {
                        this$0.startCarLifeHost($context, "宿主绑定重试 #" + attempt);
                    }
                    attempt++;
                    if (attempt < 30) {
                        Thread.sleep(HOST_NOTIFY_INTERVAL_MS);
                    }
                }
            } catch (InterruptedException e) {
                obj = this$0.lifecycleLock;
                synchronized (obj) {
                    if ($generation == this$0.hostRetryGeneration) {
                        this$0.hostRetryThread = null;
                    }
                    Unit unit4 = Unit.INSTANCE;
                }
            } catch (Throwable th) {
                synchronized (this$0.lifecycleLock) {
                    if ($generation == this$0.hostRetryGeneration) {
                        this$0.hostRetryThread = null;
                    }
                    Unit unit5 = Unit.INSTANCE;
                    throw th;
                }
            }
        }
        if (!CarLifeBridge.INSTANCE.getCallbackReady() && (projectionObserver = this$0.observer) != null) {
            projectionObserver.diagnostic(CarLifeBridge.INSTANCE.getBound() ? "CarLife Binder 已绑定，但 CastCallback 未就绪；已完成 30 次会话内退避重试，等待 Binder death 或下一轮会话" : "CarLife 宿主 Binder 尚未绑定；已完成 30 次会话内退避重试，TCP 端口仍会继续等待手机端连接");
        }
        obj = this$0.lifecycleLock;
        synchronized (obj) {
            if ($generation == this$0.hostRetryGeneration) {
                this$0.hostRetryThread = null;
            }
            Unit unit6 = Unit.INSTANCE;
        }
    }

    private final void cancelHostRetry() {
        synchronized (this.lifecycleLock) {
            this.hostRetryGeneration++;
            Thread thread = this.hostRetryThread;
            if (thread != null) {
                thread.interrupt();
            }
            this.hostRetryThread = null;
            Unit unit = Unit.INSTANCE;
        }
    }

    private final String stableHostId(Context context) {
        String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
        if (string == null) {
            string = "";
        }
        String str = string;
        if (StringsKt.isBlank(str)) {
            str = "carlink-cast-host";
        }
        return str;
    }

    /* JADX INFO: renamed from: com.carplay.adapter.carlife.CarLifeProjectionAdapter$localAddress$2, reason: invalid class name */
    /* JADX INFO: compiled from: CarLifeProjectionAdapter.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.carplay.adapter.carlife.CarLifeProjectionAdapter$localAddress$2", f = "CarLifeProjectionAdapter.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super String>, Object> {
        int label;

        AnonymousClass2(Continuation<? super AnonymousClass2> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass2(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super String> continuation) {
            return ((AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Code duplicated, block: B:22:0x0067  */
        /* JADX WARN: Code duplicated, block: B:31:0x0081  */
        /* JADX WARN: Code duplicated, block: B:33:0x0084  */
        /* JADX WARN: Code duplicated, block: B:37:0x0096  */
        /* JADX WARN: Code duplicated, block: B:42:0x00ad  */
        /* JADX WARN: Code duplicated, block: B:48:0x00b8  */
        /* JADX WARN: Code duplicated, block: B:59:0x00b0 A[SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:62:? A[LOOP:1: B:35:0x0090->B:62:?, LOOP_END, SYNTHETIC] */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) throws Throwable {
            Iterable iterableEmptyList;
            Object element$iv;
            NetworkInterface networkInterface;
            Iterable iterableEmptyList2;
            InetAddress inetAddress;
            String hostAddress;
            InetAddress it;
            boolean z;
            Enumeration<InetAddress> inetAddresses;
            boolean z2;
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                    Object obj = null;
                    if (networkInterfaces != null) {
                        Iterable list = Collections.list(networkInterfaces);
                        Intrinsics.checkNotNullExpressionValue(list, "list(...)");
                        iterableEmptyList = (List) list;
                    } else {
                        iterableEmptyList = null;
                    }
                    if (iterableEmptyList == null) {
                        iterableEmptyList = CollectionsKt.emptyList();
                    }
                    Iterable $this$firstOrNull$iv = iterableEmptyList;
                    Iterator it2 = $this$firstOrNull$iv.iterator();
                    do {
                        if (it2.hasNext()) {
                            element$iv = it2.next();
                            NetworkInterface it3 = (NetworkInterface) element$iv;
                            if (!it3.isUp() || it3.isLoopback()) {
                                z2 = false;
                            } else {
                                String name = it3.getName();
                                Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
                                if (StringsKt.startsWith$default(name, "wlan", false, 2, (Object) null)) {
                                    z2 = true;
                                } else {
                                    z2 = false;
                                }
                            }
                        } else {
                            element$iv = null;
                        }
                        networkInterface = (NetworkInterface) element$iv;
                        if (networkInterface != null || (inetAddresses = networkInterface.getInetAddresses()) == null) {
                            iterableEmptyList2 = null;
                        } else {
                            Iterable list2 = Collections.list(inetAddresses);
                            Intrinsics.checkNotNullExpressionValue(list2, "list(...)");
                            iterableEmptyList2 = (List) list2;
                        }
                        if (iterableEmptyList2 == null) {
                            iterableEmptyList2 = CollectionsKt.emptyList();
                        }
                        Iterable $this$firstOrNull$iv2 = iterableEmptyList2;
                        for (Object element$iv2 : $this$firstOrNull$iv2) {
                            it = (InetAddress) element$iv2;
                            if ((it instanceof Inet4Address) || ((Inet4Address) it).isLoopbackAddress()) {
                                z = false;
                            } else {
                                z = true;
                            }
                            if (z) {
                                obj = element$iv2;
                                inetAddress = (InetAddress) obj;
                                if (inetAddress != null || (hostAddress = inetAddress.getHostAddress()) == null) {
                                    throw new IllegalStateException("未找到可用的 WLAN 地址，CarLife 会忽略回环连接".toString());
                                }
                                return hostAddress;
                            }
                        }
                        inetAddress = (InetAddress) obj;
                        if (inetAddress != null) {
                        }
                        throw new IllegalStateException("未找到可用的 WLAN 地址，CarLife 会忽略回环连接".toString());
                    } while (!z2);
                    networkInterface = (NetworkInterface) element$iv;
                    if (networkInterface != null) {
                        iterableEmptyList2 = null;
                    } else {
                        iterableEmptyList2 = null;
                    }
                    if (iterableEmptyList2 == null) {
                        iterableEmptyList2 = CollectionsKt.emptyList();
                    }
                    Iterable $this$firstOrNull$iv3 = iterableEmptyList2;
                    while (r3.hasNext()) {
                        it = (InetAddress) element$iv2;
                        if (it instanceof Inet4Address) {
                            z = false;
                        } else {
                            z = false;
                        }
                        if (z) {
                            obj = element$iv2;
                            inetAddress = (InetAddress) obj;
                            if (inetAddress != null) {
                            }
                            throw new IllegalStateException("未找到可用的 WLAN 地址，CarLife 会忽略回环连接".toString());
                        }
                    }
                    inetAddress = (InetAddress) obj;
                    if (inetAddress != null) {
                    }
                    throw new IllegalStateException("未找到可用的 WLAN 地址，CarLife 会忽略回环连接".toString());
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object localAddress(Context context, Continuation<? super String> continuation) {
        return BuildersKt.withContext(Dispatchers.getIO(), new AnonymousClass2(null), continuation);
    }
}
