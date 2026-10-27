package com.carplay.airplay;

import android.util.Log;
import com.carplay.iap2.transport.BlockingDuplexByteStream;
import com.carplay.sender.MonotonicAudioTimestamp;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.text.Charsets;
import org.bouncycastle.jcajce.util.AnnotatedPrivateKey;

/* JADX INFO: compiled from: CarPlayMediaEngine.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000¬\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010#\n\u0000\n\u0002\u0010\"\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0013\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u0000 S2\u00020\u0001:\u0006NOPQRSB%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0004\b\b\u0010\tJ\u0010\u0010$\u001a\u00020\u00052\u0006\u0010%\u001a\u00020\u0015H\u0002J\u001e\u0010+\u001a\u00020,2\u0014\u0010-\u001a\u0010\u0012\u0004\u0012\u00020*\u0012\u0004\u0012\u00020\u0005\u0018\u00010)H\u0016J5\u0010.\u001a\u0004\u0018\u00010/2\u0006\u0010%\u001a\u00020\u00152\u0006\u00100\u001a\u00020/2\u0014\u00101\u001a\u0010\u0012\u0004\u0012\u000203\u0012\u0006\u0012\u0004\u0018\u00010\u001c02H\u0016¢\u0006\u0002\u00104J>\u00105\u001a\u0012\u0012\u0004\u0012\u000203\u0012\u0006\u0012\u0004\u0018\u00010\u001c\u0018\u0001022\u0006\u0010%\u001a\u00020\u00152\u0006\u00100\u001a\u00020/2\u0014\u00101\u001a\u0010\u0012\u0004\u0012\u000203\u0012\u0006\u0012\u0004\u0018\u00010\u001c02H\u0016J6\u00106\u001a\u0012\u0012\u0004\u0012\u000203\u0012\u0006\u0012\u0004\u0018\u00010\u001c\u0018\u0001022\u0006\u0010%\u001a\u00020\u00152\u0014\u00101\u001a\u0010\u0012\u0004\u0012\u000203\u0012\u0006\u0012\u0004\u0018\u00010\u001c02H\u0016J>\u00107\u001a\u0012\u0012\u0004\u0012\u000203\u0012\u0006\u0012\u0004\u0018\u00010\u001c\u0018\u0001022\u0006\u0010%\u001a\u00020\u00152\u0006\u00108\u001a\u0002032\u0014\u00109\u001a\u0010\u0012\u0004\u0012\u000203\u0012\u0006\u0012\u0004\u0018\u00010\u001c02H\u0016J6\u0010:\u001a\u0012\u0012\u0004\u0012\u000203\u0012\u0006\u0012\u0004\u0018\u00010\u001c\u0018\u0001022\u0006\u0010%\u001a\u00020\u00152\u0014\u00101\u001a\u0010\u0012\u0004\u0012\u000203\u0012\u0006\u0012\u0004\u0018\u00010\u001c02H\u0016J>\u0010;\u001a\u0012\u0012\u0004\u0012\u000203\u0012\u0006\u0012\u0004\u0018\u00010\u001c\u0018\u0001022\u0006\u0010%\u001a\u00020\u00152\u0006\u0010<\u001a\u0002032\u0014\u00101\u001a\u0010\u0012\u0004\u0012\u000203\u0012\u0006\u0012\u0004\u0018\u00010\u001c02H\u0002J\u0010\u0010=\u001a\u00020,2\u0006\u0010%\u001a\u00020\u0015H\u0016J \u0010>\u001a\u0012\u0012\u0004\u0012\u000203\u0012\u0006\u0012\u0004\u0018\u00010\u001c\u0018\u0001022\u0006\u0010%\u001a\u00020\u0015H\u0016J\u0018\u0010?\u001a\u00020,2\u0006\u0010%\u001a\u00020\u00152\u0006\u00100\u001a\u00020/H\u0016J\u0010\u0010@\u001a\u00020,2\u0006\u0010%\u001a\u00020\u0015H\u0016J\u0012\u0010A\u001a\u0004\u0018\u00010\u001a2\u0006\u0010%\u001a\u00020\u0015H\u0002J\u0010\u0010B\u001a\u00020,2\u0006\u0010%\u001a\u00020\u0015H\u0002J\u0018\u0010C\u001a\u00020,2\u0006\u0010%\u001a\u00020\u00152\u0006\u0010D\u001a\u00020\u0016H\u0002J\u0014\u0010E\u001a\u00020,2\n\b\u0002\u0010%\u001a\u0004\u0018\u00010\u0015H\u0002J(\u0010F\u001a\u0004\u0018\u00010G2\u0006\u0010%\u001a\u00020\u00152\u0014\u00101\u001a\u0010\u0012\u0004\u0012\u000203\u0012\u0006\u0012\u0004\u0018\u00010\u001c02H\u0002J8\u0010H\u001a\u0004\u0018\u00010\u00112\u0006\u0010%\u001a\u00020\u00152\u0006\u00100\u001a\u00020/2\u0014\u00101\u001a\u0010\u0012\u0004\u0012\u000203\u0012\u0006\u0012\u0004\u0018\u00010\u001c022\u0006\u0010I\u001a\u00020JH\u0002J0\u0010K\u001a\u0004\u0018\u00010G2\u0006\u0010%\u001a\u00020\u00152\u0014\u00101\u001a\u0010\u0012\u0004\u0012\u000203\u0012\u0006\u0012\u0004\u0018\u00010\u001c022\u0006\u0010L\u001a\u000203H\u0002J\u0010\u0010M\u001a\u00020\u00052\u0006\u00100\u001a\u00020/H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u000f0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00110\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00130\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00160\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00180\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u001a0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u001fX\u0082\u000e¢\u0006\u0002\n\u0000R2\u0010 \u001a&\u0012\f\u0012\n \"*\u0004\u0018\u00010\u00150\u0015 \"*\u0012\u0012\f\u0012\n \"*\u0004\u0018\u00010\u00150\u0015\u0018\u00010#0!X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020'X\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010(\u001a\u0010\u0012\u0004\u0012\u00020*\u0012\u0004\u0012\u00020\u0005\u0018\u00010)X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006T"}, d2 = {"Lcom/carplay/airplay/CarPlayMediaEngine;", "Lcom/carplay/airplay/AirPlayMediaHandler;", "sink", "Lcom/carplay/airplay/MediaSink;", "microphoneEnabled", "", "audioCaptureDirectory", "Ljava/io/File;", "<init>", "(Lcom/carplay/airplay/MediaSink;ZLjava/io/File;)V", "streams", "Ljava/util/concurrent/ConcurrentHashMap;", "Lcom/carplay/airplay/CarPlayMediaEngine$StreamKey;", "Ljava/io/Closeable;", "audioMeta", "Lcom/carplay/airplay/CarPlayMediaEngine$AudioMeta;", "pendingMicrophone", "Lcom/carplay/airplay/MicrophoneConfig;", "audioCaptures", "Lcom/carplay/airplay/AudioPacketCapture;", "pendingIapTunnels", "Lcom/carplay/airplay/AirPlaySession;", "Lcom/carplay/airplay/CarPlayMediaEngine$PendingIapTunnel;", "videoSettingsChannels", "Lcom/carplay/airplay/VideoSettingsChannel;", "bufferedStreams", "Lcom/carplay/airplay/CarPlayMediaEngine$BufferedStream;", "bufferedOwnershipLock", "", "bufferedOwner", "bufferedTransition", "Lcom/carplay/airplay/CarPlayMediaEngine$BufferedTransition;", "retiredBufferedSessions", "", "kotlin.jvm.PlatformType", "", "mayInstallBufferedLocked", "session", "nextRemoteControlStreamId", "", "iapTunnelHandler", "Lkotlin/Function1;", "Lcom/carplay/iap2/transport/BlockingDuplexByteStream;", "setIapTunnelHandler", "", "handler", "onScreen", "", "type", "stream", "", "", "(Lcom/carplay/airplay/AirPlaySession;ILjava/util/Map;)Ljava/lang/Integer;", "onAudio", "onBufferedAudio", "onBufferedAudioControl", "method", "body", "onDataStream", "videoDataStream", "uuid", "onSetupResponseSent", "onFeedback", "onTeardown", "onSessionClosed", "detachBufferedLocked", "retireBuffered", "replacePendingIapTunnel", "next", "clearPendingIapTunnel", "outputKey", "", "microphoneConfig", "format", "Lcom/carplay/airplay/AudioFormat;", "dataStreamKey", AnnotatedPrivateKey.LABEL, "isScreenStreamType", "StreamKey", "AudioMeta", "PendingIapTunnel", "BufferedStream", "BufferedTransition", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarPlayMediaEngine implements AirPlayMediaHandler {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final String DATASTREAM_INPUT_KEY = "DataStream-Input-Encryption-Key";

    @Deprecated
    public static final String DATASTREAM_OUTPUT_KEY = "DataStream-Output-Encryption-Key";

    @Deprecated
    public static final long FIRST_REMOTE_CONTROL_STREAM_ID = 3;

    @Deprecated
    public static final String IAP_DATASTREAM_UUID = "E9459FD0-BCAD-4C45-820F-1E72447EF2F2";

    @Deprecated
    public static final long OPUS_24K = 536870912;

    @Deprecated
    public static final long OPUS_48K = 1073741824;

    @Deprecated
    public static final int STREAM_TYPE_ALT_SCREEN = 111;

    @Deprecated
    public static final int STREAM_TYPE_DATA = 130;

    @Deprecated
    public static final int STREAM_TYPE_MAIN_AUDIO = 100;

    @Deprecated
    public static final int STREAM_TYPE_MAIN_SCREEN = 110;

    @Deprecated
    public static final String TAG = "xcertplay-usb";

    @Deprecated
    public static final long VIDEO_SETTINGS_STREAM_ID = 2;
    private final File audioCaptureDirectory;
    private final ConcurrentHashMap<StreamKey, AudioPacketCapture> audioCaptures;
    private final ConcurrentHashMap<StreamKey, AudioMeta> audioMeta;
    private AirPlaySession bufferedOwner;
    private final Object bufferedOwnershipLock;
    private final ConcurrentHashMap<AirPlaySession, BufferedStream> bufferedStreams;
    private BufferedTransition bufferedTransition;
    private volatile Function1<? super BlockingDuplexByteStream, Boolean> iapTunnelHandler;
    private final boolean microphoneEnabled;
    private volatile long nextRemoteControlStreamId;
    private final ConcurrentHashMap<AirPlaySession, PendingIapTunnel> pendingIapTunnels;
    private final ConcurrentHashMap<StreamKey, MicrophoneConfig> pendingMicrophone;
    private final Set<AirPlaySession> retiredBufferedSessions;
    private final MediaSink sink;
    private final ConcurrentHashMap<StreamKey, Closeable> streams;
    private final ConcurrentHashMap<AirPlaySession, VideoSettingsChannel> videoSettingsChannels;

    public CarPlayMediaEngine(MediaSink sink, boolean microphoneEnabled, File audioCaptureDirectory) {
        Intrinsics.checkNotNullParameter(sink, "sink");
        this.sink = sink;
        this.microphoneEnabled = microphoneEnabled;
        this.audioCaptureDirectory = audioCaptureDirectory;
        this.streams = new ConcurrentHashMap<>();
        this.audioMeta = new ConcurrentHashMap<>();
        this.pendingMicrophone = new ConcurrentHashMap<>();
        this.audioCaptures = new ConcurrentHashMap<>();
        this.pendingIapTunnels = new ConcurrentHashMap<>();
        this.videoSettingsChannels = new ConcurrentHashMap<>();
        this.bufferedStreams = new ConcurrentHashMap<>();
        this.bufferedOwnershipLock = new Object();
        this.retiredBufferedSessions = Collections.newSetFromMap(new WeakHashMap());
        this.nextRemoteControlStreamId = 3L;
    }

    public /* synthetic */ CarPlayMediaEngine(MediaSink mediaSink, boolean z, File file, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(mediaSink, (i & 2) != 0 ? false : z, (i & 4) != 0 ? null : file);
    }

    /* JADX INFO: compiled from: CarPlayMediaEngine.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0080\b\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0007HÆ\u0003J'\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0017\u001a\u00020\u0005HÖ\u0001J\t\u0010\u0018\u001a\u00020\u0007HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u0019"}, d2 = {"Lcom/carplay/airplay/CarPlayMediaEngine$StreamKey;", "", "session", "Lcom/carplay/airplay/AirPlaySession;", "type", "", "audioType", "", "<init>", "(Lcom/carplay/airplay/AirPlaySession;ILjava/lang/String;)V", "getSession", "()Lcom/carplay/airplay/AirPlaySession;", "getType", "()I", "getAudioType", "()Ljava/lang/String;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class StreamKey {
        private final String audioType;
        private final AirPlaySession session;
        private final int type;

        public static /* synthetic */ StreamKey copy$default(StreamKey streamKey, AirPlaySession airPlaySession, int i, String str, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                airPlaySession = streamKey.session;
            }
            if ((i2 & 2) != 0) {
                i = streamKey.type;
            }
            if ((i2 & 4) != 0) {
                str = streamKey.audioType;
            }
            return streamKey.copy(airPlaySession, i, str);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final AirPlaySession getSession() {
            return this.session;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final int getType() {
            return this.type;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final String getAudioType() {
            return this.audioType;
        }

        public final StreamKey copy(AirPlaySession session, int type, String audioType) {
            Intrinsics.checkNotNullParameter(session, "session");
            Intrinsics.checkNotNullParameter(audioType, "audioType");
            return new StreamKey(session, type, audioType);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof StreamKey)) {
                return false;
            }
            StreamKey streamKey = (StreamKey) other;
            return Intrinsics.areEqual(this.session, streamKey.session) && this.type == streamKey.type && Intrinsics.areEqual(this.audioType, streamKey.audioType);
        }

        public int hashCode() {
            return (((this.session.hashCode() * 31) + Integer.hashCode(this.type)) * 31) + this.audioType.hashCode();
        }

        public String toString() {
            return "StreamKey(session=" + this.session + ", type=" + this.type + ", audioType=" + this.audioType + ")";
        }

        public StreamKey(AirPlaySession session, int type, String audioType) {
            Intrinsics.checkNotNullParameter(session, "session");
            Intrinsics.checkNotNullParameter(audioType, "audioType");
            this.session = session;
            this.type = type;
            this.audioType = audioType;
        }

        public /* synthetic */ StreamKey(AirPlaySession airPlaySession, int i, String str, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            this(airPlaySession, i, (i2 & 4) != 0 ? "" : str);
        }

        public final AirPlaySession getSession() {
            return this.session;
        }

        public final int getType() {
            return this.type;
        }

        public final String getAudioType() {
            return this.audioType;
        }
    }

    /* JADX INFO: compiled from: CarPlayMediaEngine.kt */
    @Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u001c\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001BA\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0001\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\u0004\b\u000b\u0010\fJ\t\u0010\u001e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001f\u001a\u00020\u0005HÆ\u0003J\u000b\u0010 \u001a\u0004\u0018\u00010\u0001HÆ\u0003J\t\u0010!\u001a\u00020\u0003HÆ\u0003J\u0010\u0010\"\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u0015J\u0010\u0010#\u001a\u0004\u0018\u00010\nHÆ\u0003¢\u0006\u0002\u0010\u001aJP\u0010$\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00012\b\b\u0002\u0010\u0007\u001a\u00020\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\nHÆ\u0001¢\u0006\u0002\u0010%J\u0013\u0010&\u001a\u00020'2\b\u0010(\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010)\u001a\u00020\u0003HÖ\u0001J\t\u0010*\u001a\u00020+HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000eR\u001e\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u0018\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001e\u0010\t\u001a\u0004\u0018\u00010\nX\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u001d\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001c¨\u0006,"}, d2 = {"Lcom/carplay/airplay/CarPlayMediaEngine$AudioMeta;", "", "type", "", "format", "Lcom/carplay/airplay/AudioFormat;", "connectionId", "playoutLatencyMs", "firstSample", "originNs", "", "<init>", "(ILcom/carplay/airplay/AudioFormat;Ljava/lang/Object;ILjava/lang/Integer;Ljava/lang/Long;)V", "getType", "()I", "getFormat", "()Lcom/carplay/airplay/AudioFormat;", "getConnectionId", "()Ljava/lang/Object;", "getPlayoutLatencyMs", "getFirstSample", "()Ljava/lang/Integer;", "setFirstSample", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "getOriginNs", "()Ljava/lang/Long;", "setOriginNs", "(Ljava/lang/Long;)V", "Ljava/lang/Long;", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "(ILcom/carplay/airplay/AudioFormat;Ljava/lang/Object;ILjava/lang/Integer;Ljava/lang/Long;)Lcom/carplay/airplay/CarPlayMediaEngine$AudioMeta;", "equals", "", "other", "hashCode", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class AudioMeta {
        private final Object connectionId;
        private volatile Integer firstSample;
        private final AudioFormat format;
        private volatile Long originNs;
        private final int playoutLatencyMs;
        private final int type;

        public static /* synthetic */ AudioMeta copy$default(AudioMeta audioMeta, int i, AudioFormat audioFormat, Object obj, int i2, Integer num, Long l, int i3, Object obj2) {
            if ((i3 & 1) != 0) {
                i = audioMeta.type;
            }
            if ((i3 & 2) != 0) {
                audioFormat = audioMeta.format;
            }
            if ((i3 & 4) != 0) {
                obj = audioMeta.connectionId;
            }
            if ((i3 & 8) != 0) {
                i2 = audioMeta.playoutLatencyMs;
            }
            if ((i3 & 16) != 0) {
                num = audioMeta.firstSample;
            }
            if ((i3 & 32) != 0) {
                l = audioMeta.originNs;
            }
            Integer num2 = num;
            Long l2 = l;
            return audioMeta.copy(i, audioFormat, obj, i2, num2, l2);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final int getType() {
            return this.type;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final AudioFormat getFormat() {
            return this.format;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final Object getConnectionId() {
            return this.connectionId;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final int getPlayoutLatencyMs() {
            return this.playoutLatencyMs;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final Integer getFirstSample() {
            return this.firstSample;
        }

        /* JADX INFO: renamed from: component6, reason: from getter */
        public final Long getOriginNs() {
            return this.originNs;
        }

        public final AudioMeta copy(int type, AudioFormat format, Object connectionId, int playoutLatencyMs, Integer firstSample, Long originNs) {
            Intrinsics.checkNotNullParameter(format, "format");
            return new AudioMeta(type, format, connectionId, playoutLatencyMs, firstSample, originNs);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof AudioMeta)) {
                return false;
            }
            AudioMeta audioMeta = (AudioMeta) other;
            return this.type == audioMeta.type && Intrinsics.areEqual(this.format, audioMeta.format) && Intrinsics.areEqual(this.connectionId, audioMeta.connectionId) && this.playoutLatencyMs == audioMeta.playoutLatencyMs && Intrinsics.areEqual(this.firstSample, audioMeta.firstSample) && Intrinsics.areEqual(this.originNs, audioMeta.originNs);
        }

        public int hashCode() {
            return (((((((((Integer.hashCode(this.type) * 31) + this.format.hashCode()) * 31) + (this.connectionId == null ? 0 : this.connectionId.hashCode())) * 31) + Integer.hashCode(this.playoutLatencyMs)) * 31) + (this.firstSample == null ? 0 : this.firstSample.hashCode())) * 31) + (this.originNs != null ? this.originNs.hashCode() : 0);
        }

        public String toString() {
            return "AudioMeta(type=" + this.type + ", format=" + this.format + ", connectionId=" + this.connectionId + ", playoutLatencyMs=" + this.playoutLatencyMs + ", firstSample=" + this.firstSample + ", originNs=" + this.originNs + ")";
        }

        public AudioMeta(int type, AudioFormat format, Object connectionId, int playoutLatencyMs, Integer firstSample, Long originNs) {
            Intrinsics.checkNotNullParameter(format, "format");
            this.type = type;
            this.format = format;
            this.connectionId = connectionId;
            this.playoutLatencyMs = playoutLatencyMs;
            this.firstSample = firstSample;
            this.originNs = originNs;
        }

        /* JADX WARN: Illegal instructions before constructor call */
        public /* synthetic */ AudioMeta(int i, AudioFormat audioFormat, Object obj, int i2, Integer num, Long l, int i3, DefaultConstructorMarker defaultConstructorMarker) {
            Long l2;
            num = (i3 & 16) != 0 ? null : num;
            if ((i3 & 32) == 0) {
                l2 = l;
            } else {
                l2 = null;
            }
            this(i, audioFormat, obj, i2, num, l2);
        }

        public final int getType() {
            return this.type;
        }

        public final AudioFormat getFormat() {
            return this.format;
        }

        public final Object getConnectionId() {
            return this.connectionId;
        }

        public final int getPlayoutLatencyMs() {
            return this.playoutLatencyMs;
        }

        public final Integer getFirstSample() {
            return this.firstSample;
        }

        public final void setFirstSample(Integer num) {
            this.firstSample = num;
        }

        public final Long getOriginNs() {
            return this.originNs;
        }

        public final void setOriginNs(Long l) {
            this.originNs = l;
        }
    }

    /* JADX INFO: compiled from: CarPlayMediaEngine.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B#\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\u0015\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005HÆ\u0003J)\u0010\u0010\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0014\b\u0002\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005HÆ\u0001J\u0013\u0010\u0011\u001a\u00020\u00072\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001d\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u0017"}, d2 = {"Lcom/carplay/airplay/CarPlayMediaEngine$PendingIapTunnel;", "", "bridge", "Lcom/carplay/airplay/AirPlayIapTunnelStream;", "handler", "Lkotlin/Function1;", "Lcom/carplay/iap2/transport/BlockingDuplexByteStream;", "", "<init>", "(Lcom/carplay/airplay/AirPlayIapTunnelStream;Lkotlin/jvm/functions/Function1;)V", "getBridge", "()Lcom/carplay/airplay/AirPlayIapTunnelStream;", "getHandler", "()Lkotlin/jvm/functions/Function1;", "component1", "component2", "copy", "equals", "other", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class PendingIapTunnel {
        private final AirPlayIapTunnelStream bridge;
        private final Function1<BlockingDuplexByteStream, Boolean> handler;

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ PendingIapTunnel copy$default(PendingIapTunnel pendingIapTunnel, AirPlayIapTunnelStream airPlayIapTunnelStream, Function1 function1, int i, Object obj) {
            if ((i & 1) != 0) {
                airPlayIapTunnelStream = pendingIapTunnel.bridge;
            }
            if ((i & 2) != 0) {
                function1 = pendingIapTunnel.handler;
            }
            return pendingIapTunnel.copy(airPlayIapTunnelStream, function1);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final AirPlayIapTunnelStream getBridge() {
            return this.bridge;
        }

        public final Function1<BlockingDuplexByteStream, Boolean> component2() {
            return this.handler;
        }

        public final PendingIapTunnel copy(AirPlayIapTunnelStream bridge, Function1<? super BlockingDuplexByteStream, Boolean> handler) {
            Intrinsics.checkNotNullParameter(bridge, "bridge");
            Intrinsics.checkNotNullParameter(handler, "handler");
            return new PendingIapTunnel(bridge, handler);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof PendingIapTunnel)) {
                return false;
            }
            PendingIapTunnel pendingIapTunnel = (PendingIapTunnel) other;
            return Intrinsics.areEqual(this.bridge, pendingIapTunnel.bridge) && Intrinsics.areEqual(this.handler, pendingIapTunnel.handler);
        }

        public int hashCode() {
            return (this.bridge.hashCode() * 31) + this.handler.hashCode();
        }

        public String toString() {
            return "PendingIapTunnel(bridge=" + this.bridge + ", handler=" + this.handler + ")";
        }

        /* JADX WARN: Multi-variable type inference failed */
        public PendingIapTunnel(AirPlayIapTunnelStream bridge, Function1<? super BlockingDuplexByteStream, Boolean> handler) {
            Intrinsics.checkNotNullParameter(bridge, "bridge");
            Intrinsics.checkNotNullParameter(handler, "handler");
            this.bridge = bridge;
            this.handler = handler;
        }

        public final AirPlayIapTunnelStream getBridge() {
            return this.bridge;
        }

        public final Function1<BlockingDuplexByteStream, Boolean> getHandler() {
            return this.handler;
        }
    }

    /* JADX INFO: compiled from: CarPlayMediaEngine.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0002\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0001¢\u0006\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0001¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u000b"}, d2 = {"Lcom/carplay/airplay/CarPlayMediaEngine$BufferedStream;", "", "stream", "Lcom/carplay/airplay/BufferedAudioStream;", "connectionId", "<init>", "(Lcom/carplay/airplay/BufferedAudioStream;Ljava/lang/Object;)V", "getStream", "()Lcom/carplay/airplay/BufferedAudioStream;", "getConnectionId", "()Ljava/lang/Object;", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class BufferedStream {
        private final Object connectionId;
        private final BufferedAudioStream stream;

        public BufferedStream(BufferedAudioStream stream, Object connectionId) {
            Intrinsics.checkNotNullParameter(stream, "stream");
            this.stream = stream;
            this.connectionId = connectionId;
        }

        public final Object getConnectionId() {
            return this.connectionId;
        }

        public final BufferedAudioStream getStream() {
            return this.stream;
        }
    }

    /* JADX INFO: compiled from: CarPlayMediaEngine.kt */
    @Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\t\b\u0002\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001a\u0010\f\u001a\u00020\rX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001c\u0010\u0012\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u000b\"\u0004\b\u0014\u0010\u0015¨\u0006\u0016"}, d2 = {"Lcom/carplay/airplay/CarPlayMediaEngine$BufferedTransition;", "", "session", "Lcom/carplay/airplay/AirPlaySession;", "candidate", "Lcom/carplay/airplay/CarPlayMediaEngine$BufferedStream;", "<init>", "(Lcom/carplay/airplay/AirPlaySession;Lcom/carplay/airplay/CarPlayMediaEngine$BufferedStream;)V", "getSession", "()Lcom/carplay/airplay/AirPlaySession;", "getCandidate", "()Lcom/carplay/airplay/CarPlayMediaEngine$BufferedStream;", "cancelled", "", "getCancelled", "()Z", "setCancelled", "(Z)V", "retiring", "getRetiring", "setRetiring", "(Lcom/carplay/airplay/CarPlayMediaEngine$BufferedStream;)V", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class BufferedTransition {
        private boolean cancelled;
        private final BufferedStream candidate;
        private BufferedStream retiring;
        private final AirPlaySession session;

        public BufferedTransition(AirPlaySession session, BufferedStream candidate) {
            Intrinsics.checkNotNullParameter(session, "session");
            this.session = session;
            this.candidate = candidate;
        }

        public final BufferedStream getCandidate() {
            return this.candidate;
        }

        public final AirPlaySession getSession() {
            return this.session;
        }

        public final boolean getCancelled() {
            return this.cancelled;
        }

        public final void setCancelled(boolean z) {
            this.cancelled = z;
        }

        public final BufferedStream getRetiring() {
            return this.retiring;
        }

        public final void setRetiring(BufferedStream bufferedStream) {
            this.retiring = bufferedStream;
        }
    }

    /* JADX WARN: Code duplicated, block: B:16:0x0029  */
    private final boolean mayInstallBufferedLocked(AirPlaySession session) {
        boolean z;
        BufferedAudioStream stream;
        BufferedTransition it = this.bufferedTransition;
        if (it != null) {
            if (it.getCandidate() != null) {
                z = false;
            } else {
                BufferedStream retiring = it.getRetiring();
                if ((retiring == null || (stream = retiring.getStream()) == null || !stream.getOutputCleanupComplete()) ? false : true) {
                    z = true;
                } else {
                    z = false;
                }
            }
            if (!z) {
                it = null;
            }
            if (it != null) {
                this.bufferedTransition = null;
            }
        }
        return (session.isClosed$core_airplay() || this.retiredBufferedSessions.contains(session) || this.bufferedTransition != null) ? false : true;
    }

    @Override // com.carplay.airplay.AirPlayMediaHandler
    public void setIapTunnelHandler(Function1<? super BlockingDuplexByteStream, Boolean> handler) {
        this.iapTunnelHandler = handler;
    }

    @Override // com.carplay.airplay.AirPlayMediaHandler
    public Integer onScreen(final AirPlaySession session, final int type, Map<String, ? extends Object> stream) throws IOException {
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(stream, "stream");
        byte[] key = outputKey(session, stream);
        if (key == null) {
            return null;
        }
        final StreamKey streamKey = new StreamKey(session, type, null, 4, null);
        Log.i("xcertplay-usb", "airplay screen key connectionID=" + CarPlayMediaEngineKt.unsignedPlistDecimal(stream.get("streamConnectionID")));
        final ScreenStream screen = new ScreenStream(key, new CarPlayMediaEngine$onScreen$screen$1(session));
        this.sink.setVideoDiagnosticHandler(type, new Function1() { // from class: com.carplay.airplay.CarPlayMediaEngine$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return CarPlayMediaEngine.onScreen$lambda$2(session, (String) obj);
            }
        });
        this.sink.setVideoRecoveryHandler(type, new Function0() { // from class: com.carplay.airplay.CarPlayMediaEngine$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return CarPlayMediaEngine.onScreen$lambda$3(this.f$0, streamKey, screen, type, session);
            }
        });
        int port = screen.listen(new CarPlayMediaEngine$onScreen$port$1(type, this, streamKey, screen, session));
        Closeable closeablePut = this.streams.put(streamKey, screen);
        if (closeablePut != null) {
            closeablePut.close();
        }
        this.sink.onScreenStreamActive(type, true);
        return Integer.valueOf(port);
    }

    static final Unit onScreen$lambda$2(AirPlaySession $session, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        if (Intrinsics.areEqual(it, "first frame rendered")) {
            $session.videoFrameRendered$core_airplay();
        }
        $session.logDebug$core_airplay("Video: " + it);
        return Unit.INSTANCE;
    }

    static final Unit onScreen$lambda$3(CarPlayMediaEngine this$0, StreamKey $streamKey, ScreenStream $screen, int $type, AirPlaySession $session) {
        Map<String, ? extends Object> mapMapOf;
        if (this$0.streams.get($streamKey) == $screen) {
            if ($type == 111) {
                mapMapOf = MapsKt.mapOf(TuplesKt.to("type", "forceKeyFrame"), TuplesKt.to("params", MapsKt.mapOf(TuplesKt.to("uuid", AirPlayInfoPlist.ALT_UUID))));
            } else {
                mapMapOf = MapsKt.mapOf(TuplesKt.to("type", "forceKeyFrame"));
            }
            boolean sent = $session.sendCommand(mapMapOf);
            $session.logDebug$core_airplay("Video recovery: requested keyframe sent=" + sent);
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:8:0x002d  */
    @Override // com.carplay.airplay.AirPlayMediaHandler
    public Map<String, Object> onAudio(AirPlaySession session, int type, Map<String, ? extends Object> stream) throws IOException {
        String audioType;
        MicrophoneConfig microphone;
        AudioPacketCapture audioPacketCapture;
        String string;
        int i = type;
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(stream, "stream");
        Object obj = stream.get("audioType");
        if (obj == null || (string = obj.toString()) == null) {
            audioType = "default";
        } else {
            audioType = string.toLowerCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(audioType, "toLowerCase(...)");
            if (audioType == null) {
                audioType = "default";
            }
        }
        StreamKey streamKey = new StreamKey(session, i, audioType);
        final AudioStreamId streamId = new AudioStreamId(i, audioType);
        Closeable closeableRemove = this.streams.remove(streamKey);
        if (closeableRemove != null) {
            closeableRemove.close();
        }
        this.audioMeta.remove(streamKey);
        AudioPacketCapture audioPacketCaptureRemove = this.audioCaptures.remove(streamKey);
        if (audioPacketCaptureRemove != null) {
            audioPacketCaptureRemove.close();
        }
        if (this.pendingMicrophone.remove(streamKey) != null) {
            this.sink.onMicrophoneStopped(streamId);
        }
        this.sink.onAudioStopped(streamId);
        byte[] key = outputKey(session, stream);
        if (key == null) {
            return null;
        }
        AudioStreamCodec audioStreamCodec = AudioStreamCodec.INSTANCE;
        Object obj2 = stream.get("audioFormat");
        Number number = obj2 instanceof Number ? (Number) obj2 : null;
        final AudioFormat format = audioStreamCodec.fromFormatBits(number != null ? number.longValue() : 0L, i, audioType);
        AudioCodecKind codec = format.getCodec();
        int sampleRate = format.getSampleRate();
        int channels = format.getChannels();
        Object obj3 = stream.get("audioFormat");
        Number number2 = obj3 instanceof Number ? (Number) obj3 : null;
        String hexString = Long.toHexString(number2 != null ? number2.longValue() : 0L);
        Object obj4 = stream.get("dataPort");
        Number number3 = obj4 instanceof Number ? (Number) obj4 : null;
        Log.i("xcertplay-usb", "airplay audio format type=" + i + " audioType=" + audioType + " codec=" + codec + " rate=" + sampleRate + " channels=" + channels + " formatBits=0x" + hexString + " micPort=" + (number3 != null ? number3.intValue() : 0));
        Object connectionId = stream.get("streamConnectionID");
        Object obj5 = stream.get("audioLatencyMs");
        Number number4 = obj5 instanceof Number ? (Number) obj5 : null;
        int latencyMs = number4 != null ? number4.intValue() : 0;
        final AudioMeta meta = new AudioMeta(i, format, connectionId, latencyMs, null, null, 48, null);
        MicrophoneConfig microphone2 = microphoneConfig(session, i, stream, format);
        if (microphone2 != null) {
            this.pendingMicrophone.put(streamKey, microphone2);
        }
        File it = this.audioCaptureDirectory;
        if (it != null) {
            microphone = microphone2;
            AudioPacketCapture audioPacketCapture2 = new AudioPacketCapture(it, i, 0, 0L, 12, null);
            i = i;
            audioPacketCapture = audioPacketCapture2;
        } else {
            microphone = microphone2;
            audioPacketCapture = null;
        }
        final AudioPacketCapture capture = audioPacketCapture;
        if (capture != null) {
            this.audioCaptures.put(streamKey, capture);
        }
        AudioStream audio = new AudioStream(key, i, new CarPlayMediaEngine$onAudio$audio$1(session));
        final MicrophoneConfig microphone3 = microphone;
        Pair<Integer, Integer> pairListen = audio.listen(new AudioStream.Listener() { // from class: com.carplay.airplay.CarPlayMediaEngine.onAudio.1
            @Override // com.carplay.airplay.AudioStream.Listener
            public void onStarted(int firstSample) {
                meta.setFirstSample(Integer.valueOf(firstSample));
                meta.setOriginNs(Long.valueOf(System.nanoTime()));
                this.sink.onAudioStarted(streamId, format, firstSample);
                MicrophoneConfig it2 = microphone3;
                if (it2 != null) {
                    CarPlayMediaEngine carPlayMediaEngine = this;
                    carPlayMediaEngine.sink.onMicrophoneStarted(streamId, it2);
                }
            }

            @Override // com.carplay.airplay.AudioStream.Listener
            public void onRtp(byte[] rtp, int sample) {
                Intrinsics.checkNotNullParameter(rtp, "rtp");
                this.sink.onAudioRtp(streamId, format, rtp, sample);
            }

            @Override // com.carplay.airplay.AudioStream.Listener
            public void onPacket(byte[] wire, byte[] rtp, Integer sample, Throwable error) {
                Intrinsics.checkNotNullParameter(wire, "wire");
                AudioPacketCapture audioPacketCapture3 = capture;
                if (audioPacketCapture3 != null) {
                    audioPacketCapture3.record(wire, rtp, sample, error);
                }
            }
        });
        int dataPort = pairListen.component1().intValue();
        int controlPort = pairListen.component2().intValue();
        this.streams.put(streamKey, audio);
        this.audioMeta.put(streamKey, meta);
        Pair[] pairArr = new Pair[4];
        pairArr[0] = TuplesKt.to("type", Integer.valueOf(i));
        pairArr[1] = TuplesKt.to("dataPort", Integer.valueOf(dataPort));
        pairArr[2] = TuplesKt.to("controlPort", Integer.valueOf(controlPort));
        pairArr[3] = TuplesKt.to("streamConnectionID", CarPlayMediaEngineKt.unsignedPlistInteger(connectionId == null ? 0L : connectionId));
        return MapsKt.linkedMapOf(pairArr);
    }

    /* JADX WARN: Code duplicated, block: B:104:0x018f  */
    /* JADX WARN: Code duplicated, block: B:125:0x01c1 A[Catch: all -> 0x01b5, TRY_LEAVE, TryCatch #2 {all -> 0x01b5, blocks: (B:110:0x019a, B:112:0x019e, B:114:0x01a4, B:116:0x01aa, B:125:0x01c1), top: B:173:0x019a }] */
    /* JADX WARN: Code duplicated, block: B:127:0x01d4  */
    /* JADX WARN: Code duplicated, block: B:130:0x01da A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:131:0x01dc  */
    /* JADX WARN: Code duplicated, block: B:132:0x01e1 A[Catch: all -> 0x0281, TRY_LEAVE, TryCatch #0 {all -> 0x0281, blocks: (B:128:0x01d6, B:132:0x01e1), top: B:169:0x01d6 }] */
    /* JADX WARN: Code duplicated, block: B:139:0x01f6  */
    /* JADX WARN: Code duplicated, block: B:143:0x01fc  */
    /* JADX WARN: Code duplicated, block: B:145:0x0202  */
    /* JADX WARN: Code duplicated, block: B:147:0x020f A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:148:0x0210  */
    /* JADX WARN: Code duplicated, block: B:41:0x00b0  */
    /* JADX WARN: Code duplicated, block: B:54:0x00d5  */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.carplay.airplay.AirPlayMediaHandler
    public Map<String, Object> onBufferedAudio(AirPlaySession session, Map<String, ? extends Object> stream) throws Throwable {
        boolean zMayInstallBufferedLocked;
        boolean z;
        boolean z2;
        boolean z3;
        Object objM159constructorimpl;
        boolean z4;
        char c;
        BufferedTransition bufferedTransition;
        BufferedAudioStream stream2;
        Object obj;
        BufferedAudioStream stream3;
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(stream, "stream");
        synchronized (this.bufferedOwnershipLock) {
            zMayInstallBufferedLocked = mayInstallBufferedLocked(session);
        }
        Map<String, Object> map = null;
        if (!zMayInstallBufferedLocked) {
            return null;
        }
        StreamKey streamKey = new StreamKey(session, 103, "media");
        Object obj2 = stream.get("shk");
        byte[] bArr = obj2 instanceof byte[] ? (byte[]) obj2 : null;
        if (bArr == null || bArr.length != 32) {
            Log.w("xcertplay-usb", "buffered audio SETUP without a 32-byte shk; declined");
            return 0;
        }
        AudioStreamCodec audioStreamCodec = AudioStreamCodec.INSTANCE;
        Object obj3 = stream.get("audioFormat");
        Number number = obj3 instanceof Number ? (Number) obj3 : null;
        AudioFormat audioFormatFromFormatBits = audioStreamCodec.fromFormatBits(number != null ? number.longValue() : 0L, 103, "media");
        if (audioFormatFromFormatBits.getCodec() != AudioCodecKind.AAC_LC) {
            Log.w("xcertplay-usb", "buffered audio format " + stream.get("audioFormat") + " is not AAC-LC; declined");
            return null;
        }
        Object obj4 = stream.get("ct");
        if (obj4 == null) {
            z = false;
        } else {
            if (((obj4 instanceof Number) && ((Number) obj4).intValue() == 4) ? false : true) {
                z = true;
            } else {
                z = false;
            }
        }
        if (!z) {
            Object obj5 = stream.get("spf");
            if (obj5 == null) {
                z2 = false;
            } else {
                if (((obj5 instanceof Number) && ((Number) obj5).intValue() == 1024) ? false : true) {
                    z2 = true;
                } else {
                    z2 = false;
                }
            }
            if (!z2) {
                InetAddress remoteAddress$core_airplay = session.getRemoteAddress$core_airplay();
                if (remoteAddress$core_airplay == null) {
                    return null;
                }
                try {
                    BufferedAudioStream bufferedAudioStream = new BufferedAudioStream(bArr, audioFormatFromFormatBits, this.sink, new CarPlayMediaEngine$onBufferedAudio$buffered$1(session), null, remoteAddress$core_airplay, 16, null);
                    BufferedStream bufferedStream = new BufferedStream(bufferedAudioStream, stream.get("streamConnectionID"));
                    BufferedTransition bufferedTransition2 = new BufferedTransition(session, bufferedStream);
                    BufferedStream bufferedStreamDetachBufferedLocked = null;
                    synchronized (this.bufferedOwnershipLock) {
                        if (mayInstallBufferedLocked(session)) {
                            this.bufferedTransition = bufferedTransition2;
                            AirPlaySession airPlaySession = this.bufferedOwner;
                            if (airPlaySession != null) {
                                bufferedStreamDetachBufferedLocked = detachBufferedLocked(airPlaySession);
                                if (airPlaySession != session) {
                                    this.retiredBufferedSessions.add(airPlaySession);
                                }
                            } else {
                                map = null;
                            }
                            bufferedTransition2.setRetiring(bufferedStreamDetachBufferedLocked);
                            z3 = true;
                        } else {
                            map = null;
                            z3 = false;
                        }
                    }
                    if (!z3) {
                        bufferedAudioStream.close();
                        return map;
                    }
                    try {
                        Result.Companion companion = Result.INSTANCE;
                        CarPlayMediaEngine carPlayMediaEngine = this;
                        if (bufferedStreamDetachBufferedLocked == null || (stream3 = bufferedStreamDetachBufferedLocked.getStream()) == null) {
                            obj = map;
                        } else {
                            stream3.close();
                            obj = Unit.INSTANCE;
                        }
                        objM159constructorimpl = Result.m159constructorimpl(obj);
                    } catch (Throwable th) {
                        Result.Companion companion2 = Result.INSTANCE;
                        objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
                    }
                    if (Result.m166isSuccessimpl(objM159constructorimpl)) {
                        if ((bufferedStreamDetachBufferedLocked == null || (stream2 = bufferedStreamDetachBufferedLocked.getStream()) == null || stream2.getOutputCleanupComplete()) ? false : true) {
                            z4 = false;
                        } else {
                            z4 = true;
                        }
                    } else {
                        z4 = false;
                    }
                    boolean z5 = z4;
                    synchronized (this.bufferedOwnershipLock) {
                        try {
                            if (z5) {
                                try {
                                    if (this.bufferedTransition == bufferedTransition2 && !bufferedTransition2.getCancelled() && !session.isClosed$core_airplay() && !this.retiredBufferedSessions.contains(session)) {
                                        c = 1;
                                    }
                                    if (c != 0) {
                                        this.streams.put(streamKey, bufferedAudioStream);
                                        this.bufferedStreams.put(session, bufferedStream);
                                        this.bufferedOwner = session;
                                    }
                                    if (this.bufferedTransition != bufferedTransition2) {
                                        if (z5) {
                                            bufferedTransition = map;
                                        } else {
                                            try {
                                                BufferedTransition bufferedTransition3 = new BufferedTransition(session, map);
                                                bufferedTransition3.setRetiring(bufferedStreamDetachBufferedLocked);
                                                bufferedTransition = bufferedTransition3;
                                            } catch (Throwable th2) {
                                                th = th2;
                                                throw th;
                                            }
                                        }
                                        this.bufferedTransition = bufferedTransition;
                                    }
                                    if (c == 0) {
                                        bufferedAudioStream.close();
                                        return null;
                                    }
                                    bufferedAudioStream.start();
                                    if (this.bufferedStreams.get(session) != bufferedStream) {
                                        return null;
                                    }
                                    Log.i("xcertplay-usb", "buffered audio stream client=" + stream.get("clientID") + " rate=" + audioFormatFromFormatBits.getSampleRate() + " port=" + bufferedAudioStream.getBoundPort());
                                    return MapsKt.linkedMapOf(TuplesKt.to("type", 103), TuplesKt.to("dataPort", Integer.valueOf(bufferedAudioStream.getBoundPort())), TuplesKt.to("audioBufferSize", 8388608));
                                } catch (Throwable th3) {
                                    th = th3;
                                    throw th;
                                }
                            }
                            if (this.bufferedTransition != bufferedTransition2) {
                                if (z5) {
                                    bufferedTransition = map;
                                } else {
                                    BufferedTransition bufferedTransition4 = new BufferedTransition(session, map);
                                    bufferedTransition4.setRetiring(bufferedStreamDetachBufferedLocked);
                                    bufferedTransition = bufferedTransition4;
                                }
                                this.bufferedTransition = bufferedTransition;
                            }
                            if (c == 0) {
                                bufferedAudioStream.close();
                                return null;
                            }
                            bufferedAudioStream.start();
                            if (this.bufferedStreams.get(session) != bufferedStream) {
                                return null;
                            }
                            Log.i("xcertplay-usb", "buffered audio stream client=" + stream.get("clientID") + " rate=" + audioFormatFromFormatBits.getSampleRate() + " port=" + bufferedAudioStream.getBoundPort());
                            return MapsKt.linkedMapOf(TuplesKt.to("type", 103), TuplesKt.to("dataPort", Integer.valueOf(bufferedAudioStream.getBoundPort())), TuplesKt.to("audioBufferSize", 8388608));
                        } catch (Throwable th4) {
                            th = th4;
                        }
                        c = 0;
                        if (c != 0) {
                            this.streams.put(streamKey, bufferedAudioStream);
                            this.bufferedStreams.put(session, bufferedStream);
                            this.bufferedOwner = session;
                        }
                    }
                } catch (Exception e) {
                    Log.w("xcertplay-usb", "buffered audio listener failed", e);
                    return null;
                }
            }
        }
        Log.w("xcertplay-usb", "buffered audio requires AAC and 1024 samples per frame; declined");
        return 0;
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    @Override // com.carplay.airplay.AirPlayMediaHandler
    public Map<String, Object> onBufferedAudioControl(AirPlaySession session, String method, Map<String, ? extends Object> body) {
        BufferedAudioStream buffered;
        Integer rate;
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(method, "method");
        Intrinsics.checkNotNullParameter(body, "body");
        synchronized (this.bufferedOwnershipLock) {
            BufferedStream bufferedStream = this.bufferedStreams.get(session);
            buffered = bufferedStream != null ? bufferedStream.getStream() : null;
        }
        if (buffered == null) {
            return null;
        }
        Object obj = body.get("rtpTime");
        Number number = obj instanceof Number ? (Number) obj : null;
        Long rtpTime = number != null ? Long.valueOf(number.longValue()) : null;
        Object obj2 = body.get("rate");
        Number number2 = obj2 instanceof Number ? (Number) obj2 : null;
        Double dValueOf = number2 != null ? Double.valueOf(number2.doubleValue()) : null;
        if (Intrinsics.areEqual(dValueOf, 0.0d)) {
            rate = 0;
        } else {
            rate = Intrinsics.areEqual(dValueOf, 1.0d) ? 1 : null;
        }
        switch (method.hashCode()) {
            case -1591110622:
                if (!method.equals("SETRATE")) {
                    return null;
                }
                break;
            case -1417895997:
                if (!method.equals("FLUSHBUFFERED")) {
                    return null;
                }
                Object obj3 = body.get("flushUntilTS");
                Number number3 = obj3 instanceof Number ? (Number) obj3 : null;
                if (number3 == null) {
                    return null;
                }
                long it = number3.longValue();
                buffered.flush(Long.valueOf(it));
                return null;
            case -817303388:
                if (!method.equals("SETRATEANCHORTIME")) {
                    return null;
                }
                break;
            case -309524117:
                if (method.equals("GETANCHOR")) {
                    return buffered.anchor();
                }
                return null;
            default:
                return null;
        }
        if (rate == null || !new IntRange(0, 1).contains(rate.intValue())) {
            return null;
        }
        return buffered.setRate(rtpTime, rate.intValue(), session.syncedNtp());
    }

    @Override // com.carplay.airplay.AirPlayMediaHandler
    public Map<String, Object> onDataStream(final AirPlaySession session, Map<String, ? extends Object> stream) throws UnknownHostException {
        byte[] shared;
        String seed;
        int boundPort;
        String hostAddress;
        Map<String, Object> mapVideoDataStream;
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(stream, "stream");
        Object obj = stream.get("clientTypeUUID");
        String str = obj instanceof String ? (String) obj : null;
        if (str != null) {
            String uuid = str.toUpperCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(uuid, "toUpperCase(...)");
            if (uuid != null) {
                if (session.getVideoInCar() && (mapVideoDataStream = videoDataStream(session, uuid, stream)) != null) {
                    return mapVideoDataStream;
                }
                if (!Intrinsics.areEqual(uuid, IAP_DATASTREAM_UUID) || (shared = session.getSharedSecret()) == null || (seed = CarPlayMediaEngineKt.unsignedPlistDecimal(stream.get("seed"))) == null) {
                    return null;
                }
                String strUnsignedPlistDecimal = CarPlayMediaEngineKt.unsignedPlistDecimal(stream.get("streamConnectionID"));
                if (strUnsignedPlistDecimal == null) {
                    strUnsignedPlistDecimal = "none";
                }
                session.logDebug$core_airplay("AirPlay iAP SETUP uuid=" + uuid + " seed=" + seed + " streamConnectionID=" + strUnsignedPlistDecimal);
                AirPlayCrypto airPlayCrypto = AirPlayCrypto.INSTANCE;
                byte[] bytes = ("DataStream-Salt" + seed).getBytes(Charsets.US_ASCII);
                Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
                byte[] bytes2 = DATASTREAM_OUTPUT_KEY.getBytes(Charsets.US_ASCII);
                Intrinsics.checkNotNullExpressionValue(bytes2, "getBytes(...)");
                byte[] key = airPlayCrypto.hkdfSha512(shared, bytes, bytes2, 32);
                InetAddress localAddress = session.getLocalAddress();
                if (localAddress == null) {
                    InetAddress remoteAddress$core_airplay = session.getRemoteAddress$core_airplay();
                    if (remoteAddress$core_airplay instanceof Inet6Address) {
                        localAddress = InetAddress.getByName("::");
                    } else {
                        localAddress = remoteAddress$core_airplay instanceof Inet4Address ? InetAddress.getByName("0.0.0.0") : InetAddress.getByName("0.0.0.0");
                    }
                }
                Intrinsics.checkNotNull(localAddress);
                IapTunnel tunnel = new IapTunnel(key, localAddress);
                AirPlayIapTunnelStream bridge = new AirPlayIapTunnelStream(session, tunnel, null, 4, null);
                Function1<? super BlockingDuplexByteStream, Boolean> function1 = this.iapTunnelHandler;
                try {
                    if (function1 != null) {
                        boundPort = bridge.listen();
                        InetAddress localAddress2 = session.getLocalAddress();
                        if (localAddress2 == null || (hostAddress = localAddress2.getHostAddress()) == null) {
                            hostAddress = "wildcard";
                        }
                        session.logDebug$core_airplay("AirPlay iAP tunnel listening address=" + hostAddress + " port=" + boundPort);
                        replacePendingIapTunnel(session, new PendingIapTunnel(bridge, function1));
                    } else {
                        boundPort = tunnel.listen(new IapTunnel.Listener() { // from class: com.carplay.airplay.CarPlayMediaEngine$onDataStream$port$1
                            @Override // com.carplay.airplay.IapTunnel.Listener
                            public void onDebug(String message) {
                                super.onDebug(message);
                            }

                            @Override // com.carplay.airplay.IapTunnel.Listener
                            public void onOpen(String remoteAddress) {
                                super.onOpen(remoteAddress);
                            }

                            @Override // com.carplay.airplay.IapTunnel.Listener
                            public void onIap(byte[] bytes3) {
                                Intrinsics.checkNotNullParameter(bytes3, "bytes");
                                this.this$0.sink.onIapMessage(bytes3);
                            }

                            @Override // com.carplay.airplay.IapTunnel.Listener
                            public void onClosed(Throwable cause) {
                                String message;
                                if (cause == null || (message = cause.getMessage()) == null) {
                                    message = "peer EOF";
                                }
                                Log.w("xcertplay-usb", "iAP tunnel ended reason=" + message);
                                session.close();
                            }
                        });
                    }
                    int port = boundPort;
                    this.streams.put(new StreamKey(session, 130, null, 4, null), function1 != null ? bridge : tunnel);
                    LinkedHashMap $this$onDataStream_u24lambda_u2417 = MapsKt.linkedMapOf(TuplesKt.to("type", 130), TuplesKt.to("streamID", 1L), TuplesKt.to("dataPort", Integer.valueOf(port)));
                    Object connectionId = stream.get("streamConnectionID");
                    if (connectionId != null) {
                        $this$onDataStream_u24lambda_u2417.put("streamConnectionID", CarPlayMediaEngineKt.unsignedPlistInteger(connectionId));
                    }
                    return $this$onDataStream_u24lambda_u2417;
                } catch (Throwable error) {
                    bridge.close();
                    throw error;
                }
            }
        }
        return null;
    }

    private final Map<String, Object> videoDataStream(final AirPlaySession session, String uuid, Map<String, ? extends Object> stream) throws IOException {
        byte[] shared;
        String seed;
        if (VideoInCar.INSTANCE.getREMOTE_CONTROL_UUIDS().contains(uuid)) {
            Object obj = stream.get("controlType");
            Number number = obj instanceof Number ? (Number) obj : null;
            if (number != null && number.intValue() == 1) {
                long streamId = this.nextRemoteControlStreamId;
                this.nextRemoteControlStreamId = 1 + streamId;
                session.logTrace$core_airplay("video remote-control stream accepted uuid=" + uuid + " streamID=" + streamId);
                return MapsKt.linkedMapOf(TuplesKt.to("type", 130), TuplesKt.to("streamID", Long.valueOf(streamId)));
            }
        }
        if (!Intrinsics.areEqual(uuid, VideoInCar.SETTINGS_CHANNEL_UUID) || (shared = session.getSharedSecret()) == null || (seed = CarPlayMediaEngineKt.unsignedPlistDecimal(stream.get("seed"))) == null) {
            return null;
        }
        VideoSettingsChannel channel = new VideoSettingsChannel(videoDataStream$key(shared, seed, DATASTREAM_OUTPUT_KEY), videoDataStream$key(shared, seed, DATASTREAM_INPUT_KEY), new Function1() { // from class: com.carplay.airplay.CarPlayMediaEngine$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj2) {
                return CarPlayMediaEngine.videoDataStream$lambda$18(session, (String) obj2);
            }
        });
        InetAddress localAddress = session.getLocalAddress();
        if (localAddress == null) {
            localAddress = InetAddress.getByName("::");
        }
        Intrinsics.checkNotNull(localAddress);
        int port = channel.listen(localAddress);
        VideoSettingsChannel videoSettingsChannelPut = this.videoSettingsChannels.put(session, channel);
        if (videoSettingsChannelPut != null) {
            videoSettingsChannelPut.close();
        }
        session.logTrace$core_airplay("video settings stream listening port=" + port);
        LinkedHashMap $this$videoDataStream_u24lambda_u2420 = MapsKt.linkedMapOf(TuplesKt.to("type", 130), TuplesKt.to("streamID", 2L), TuplesKt.to("dataPort", Integer.valueOf(port)));
        Object connectionId = stream.get("streamConnectionID");
        if (connectionId != null) {
            $this$videoDataStream_u24lambda_u2420.put("streamConnectionID", CarPlayMediaEngineKt.unsignedPlistInteger(connectionId));
        }
        return $this$videoDataStream_u24lambda_u2420;
    }

    private static final byte[] videoDataStream$key(byte[] shared, String seed, String label) {
        AirPlayCrypto airPlayCrypto = AirPlayCrypto.INSTANCE;
        byte[] bytes = ("DataStream-Salt" + seed).getBytes(Charsets.US_ASCII);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        byte[] bytes2 = label.getBytes(Charsets.US_ASCII);
        Intrinsics.checkNotNullExpressionValue(bytes2, "getBytes(...)");
        return airPlayCrypto.hkdfSha512(shared, bytes, bytes2, 32);
    }

    static final Unit videoDataStream$lambda$18(AirPlaySession $session, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $session.logDebug$core_airplay(it);
        return Unit.INSTANCE;
    }

    @Override // com.carplay.airplay.AirPlayMediaHandler
    public void onSetupResponseSent(AirPlaySession session) {
        boolean attached;
        Intrinsics.checkNotNullParameter(session, "session");
        PendingIapTunnel pending = this.pendingIapTunnels.remove(session);
        if (pending == null) {
            return;
        }
        try {
            attached = pending.getHandler().invoke(pending.getBridge()).booleanValue();
        } catch (Throwable error) {
            Log.w("xcertplay-usb", "iAP tunnel relay attachment failed", error);
            attached = false;
        }
        if (!attached) {
            Log.w("xcertplay-usb", "iAP tunnel relay attachment was rejected after SETUP");
            pending.getBridge().close();
            session.close();
        }
    }

    @Override // com.carplay.airplay.AirPlayMediaHandler
    public Map<String, Object> onFeedback(AirPlaySession session) {
        Intrinsics.checkNotNullParameter(session, "session");
        Iterable iterableEntrySet = this.audioMeta.entrySet();
        Intrinsics.checkNotNullExpressionValue(iterableEntrySet, "<get-entries>(...)");
        Iterable $this$filter$iv = iterableEntrySet;
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv : $this$filter$iv) {
            Map.Entry entry = (Map.Entry) element$iv$iv;
            Intrinsics.checkNotNull(entry);
            Object key = entry.getKey();
            Intrinsics.checkNotNullExpressionValue(key, "component1(...)");
            StreamKey key2 = (StreamKey) key;
            if (key2.getSession() == session) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        Iterable $this$map$iv = (List) destination$iv$iv;
        Collection destination$iv$iv2 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (Object item$iv$iv : $this$map$iv) {
            Map.Entry it = (Map.Entry) item$iv$iv;
            destination$iv$iv2.add((AudioMeta) it.getValue());
        }
        List active = (List) destination$iv$iv2;
        BufferedStream buffered = this.bufferedStreams.get(session);
        if (active.isEmpty() && buffered == null) {
            return null;
        }
        List $this$map$iv2 = active;
        Collection destination$iv$iv3 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv2, 10));
        for (Object item$iv$iv2 : $this$map$iv2) {
            AudioMeta meta = (AudioMeta) item$iv$iv2;
            LinkedHashMap entry2 = MapsKt.linkedMapOf(TuplesKt.to("type", Integer.valueOf(meta.getType())), TuplesKt.to("sampleRate", Integer.valueOf(meta.getFormat().getSampleRate())));
            Integer firstSample = meta.getFirstSample();
            Long originNs = meta.getOriginNs();
            if (firstSample != null && originNs != null) {
                long nowNs = System.nanoTime();
                double elapsedSec = Math.max(0.0d, ((nowNs - originNs.longValue()) / 1.0E9d) - (((double) meta.getPlayoutLatencyMs()) / 1000.0d));
                long firstUnsigned = ((long) firstSample.intValue()) & MonotonicAudioTimestamp.RTP_MASK;
                long sampleTime = (Math.round(((double) meta.getFormat().getSampleRate()) * elapsedSec) + firstUnsigned) & MonotonicAudioTimestamp.RTP_MASK;
                LinkedHashMap linkedHashMap = entry2;
                Object connectionId = meta.getConnectionId();
                if (connectionId == null) {
                    connectionId = 0L;
                }
                linkedHashMap.put("streamConnectionID", CarPlayMediaEngineKt.unsignedPlistInteger(connectionId));
                entry2.put("timestamp", session.syncedNtp());
                entry2.put("sampleTime", Long.valueOf(sampleTime));
            }
            destination$iv$iv3.add(entry2);
            active = active;
            buffered = buffered;
        }
        BufferedStream buffered2 = buffered;
        List streams = CollectionsKt.plus(destination$iv$iv3, (Iterable) CollectionsKt.listOfNotNull(buffered2 != null ? buffered2.getStream().feedback(session.syncedNtp(), buffered2.getConnectionId()) : null));
        return MapsKt.linkedMapOf(TuplesKt.to("streams", streams));
    }

    @Override // com.carplay.airplay.AirPlayMediaHandler
    public void onTeardown(AirPlaySession session, int type) throws IOException {
        Intrinsics.checkNotNullParameter(session, "session");
        if (type == 130) {
            clearPendingIapTunnel(session);
        }
        if (type == 103) {
            retireBuffered(session);
            return;
        }
        Iterable iterableKeySet = this.streams.keySet();
        Intrinsics.checkNotNullExpressionValue(iterableKeySet, "<get-keys>(...)");
        Iterable $this$filter$iv = iterableKeySet;
        Collection destination$iv$iv = new ArrayList();
        Iterator it = $this$filter$iv.iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            Object element$iv$iv = it.next();
            StreamKey it2 = (StreamKey) element$iv$iv;
            if (it2.getSession() == session && it2.getType() == type) {
                z = true;
            }
            if (z) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        Iterable tornDown = (List) destination$iv$iv;
        Iterable $this$forEach$iv = tornDown;
        for (Object element$iv : $this$forEach$iv) {
            StreamKey key = (StreamKey) element$iv;
            AudioStreamId streamId = new AudioStreamId(key.getType(), key.getAudioType());
            if (this.pendingMicrophone.remove(key) != null) {
                this.sink.onMicrophoneStopped(streamId);
            }
            this.audioMeta.remove(key);
            AudioPacketCapture audioPacketCaptureRemove = this.audioCaptures.remove(key);
            if (audioPacketCaptureRemove != null) {
                audioPacketCaptureRemove.close();
            }
            if (key.getType() != 103) {
                this.sink.onAudioStopped(streamId);
            }
            Closeable closeableRemove = this.streams.remove(key);
            if (closeableRemove != null) {
                closeableRemove.close();
            }
        }
        if (isScreenStreamType(type)) {
            this.sink.onScreenStreamActive(type, false);
        }
    }

    @Override // com.carplay.airplay.AirPlayMediaHandler
    public void onSessionClosed(AirPlaySession session) throws IOException {
        Intrinsics.checkNotNullParameter(session, "session");
        clearPendingIapTunnel(session);
        retireBuffered(session);
        VideoSettingsChannel videoSettingsChannelRemove = this.videoSettingsChannels.remove(session);
        if (videoSettingsChannelRemove != null) {
            videoSettingsChannelRemove.close();
        }
        Iterable iterableKeySet = this.streams.keySet();
        Intrinsics.checkNotNullExpressionValue(iterableKeySet, "<get-keys>(...)");
        Iterable $this$filter$iv = iterableKeySet;
        Collection destination$iv$iv = new ArrayList();
        Iterator it = $this$filter$iv.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Object element$iv$iv = it.next();
            StreamKey it2 = (StreamKey) element$iv$iv;
            if (it2.getSession() == session && it2.getType() != 103) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        Iterable sessionStreams = (List) destination$iv$iv;
        Iterable $this$filter$iv2 = sessionStreams;
        Collection destination$iv$iv2 = new ArrayList();
        for (Object element$iv$iv2 : $this$filter$iv2) {
            StreamKey it3 = (StreamKey) element$iv$iv2;
            if (isScreenStreamType(it3.getType())) {
                destination$iv$iv2.add(element$iv$iv2);
            }
        }
        Iterable $this$forEach$iv = (List) destination$iv$iv2;
        for (Object element$iv : $this$forEach$iv) {
            StreamKey it4 = (StreamKey) element$iv;
            this.sink.onScreenStreamActive(it4.getType(), false);
        }
        Iterable $this$forEach$iv2 = sessionStreams;
        for (Object element$iv2 : $this$forEach$iv2) {
            StreamKey it5 = (StreamKey) element$iv2;
            Closeable closeableRemove = this.streams.remove(it5);
            if (closeableRemove != null) {
                closeableRemove.close();
            }
        }
        Set<StreamKey> setKeySet = this.audioMeta.keySet();
        Intrinsics.checkNotNullExpressionValue(setKeySet, "<get-keys>(...)");
        Set<StreamKey> setKeySet2 = this.pendingMicrophone.keySet();
        Intrinsics.checkNotNullExpressionValue(setKeySet2, "<get-keys>(...)");
        Set<StreamKey> setKeySet3 = this.audioCaptures.keySet();
        Intrinsics.checkNotNullExpressionValue(setKeySet3, "<get-keys>(...)");
        Iterable $this$forEach$iv3 = CarPlayMediaEngineKt.audioKeysForSession(session, setKeySet, setKeySet2, setKeySet3);
        for (Object element$iv3 : $this$forEach$iv3) {
            StreamKey key = (StreamKey) element$iv3;
            AudioStreamId streamId = new AudioStreamId(key.getType(), key.getAudioType());
            boolean hadMicrophone = this.pendingMicrophone.remove(key) != null;
            boolean hadAudio = this.audioMeta.remove(key) != null;
            AudioPacketCapture audioPacketCaptureRemove = this.audioCaptures.remove(key);
            if (audioPacketCaptureRemove != null) {
                audioPacketCaptureRemove.close();
            }
            if (hadMicrophone) {
                this.sink.onMicrophoneStopped(streamId);
            }
            if (hadAudio) {
                this.sink.onAudioStopped(streamId);
            }
        }
    }

    private final BufferedStream detachBufferedLocked(AirPlaySession session) {
        BufferedStream previous = this.bufferedStreams.remove(session);
        if (previous != null) {
            this.streams.remove(new StreamKey(session, 103, "media"), previous.getStream());
        }
        if (this.bufferedOwner == session) {
            this.bufferedOwner = null;
        }
        return previous;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v2, types: [T, com.carplay.airplay.CarPlayMediaEngine$BufferedTransition] */
    private final void retireBuffered(AirPlaySession session) {
        boolean z;
        BufferedStream previous;
        BufferedAudioStream stream;
        BufferedAudioStream stream2;
        BufferedAudioStream stream3;
        BufferedStream candidate = null;
        Ref.ObjectRef retirement = new Ref.ObjectRef();
        synchronized (this.bufferedOwnershipLock) {
            BufferedTransition it = this.bufferedTransition;
            z = false;
            if (it != null) {
                if (!(it.getSession() == session)) {
                    it = null;
                }
                if (it != null) {
                    it.setCancelled(true);
                    candidate = it.getCandidate();
                }
            }
            previous = detachBufferedLocked(session);
            if (previous != null && this.bufferedTransition == null) {
                ?? bufferedTransition = new BufferedTransition(session, null);
                bufferedTransition.setRetiring(previous);
                retirement.element = bufferedTransition;
                this.bufferedTransition = (BufferedTransition) retirement.element;
            }
        }
        if (candidate != null) {
            try {
                BufferedAudioStream stream4 = candidate.getStream();
                if (stream4 != null) {
                    stream4.close();
                }
            } catch (Throwable th) {
                synchronized (this.bufferedOwnershipLock) {
                    if (retirement.element != 0 && this.bufferedTransition == retirement.element) {
                        if (previous != null && (stream = previous.getStream()) != null && stream.getOutputCleanupComplete()) {
                            z = true;
                        }
                        if (z) {
                            this.bufferedTransition = null;
                        }
                    }
                    Unit unit = Unit.INSTANCE;
                    throw th;
                }
            }
        }
        if (previous != null && (stream3 = previous.getStream()) != null) {
            stream3.close();
        }
        synchronized (this.bufferedOwnershipLock) {
            if (retirement.element != 0 && this.bufferedTransition == retirement.element) {
                if (previous != null && (stream2 = previous.getStream()) != null && stream2.getOutputCleanupComplete()) {
                    z = true;
                }
                if (z) {
                    this.bufferedTransition = null;
                }
            }
            Unit unit2 = Unit.INSTANCE;
        }
    }

    private final void replacePendingIapTunnel(AirPlaySession session, PendingIapTunnel next) {
        AirPlayIapTunnelStream bridge;
        PendingIapTunnel previous = this.pendingIapTunnels.put(session, next);
        if (previous == null || (bridge = previous.getBridge()) == null) {
            return;
        }
        bridge.close();
    }

    static /* synthetic */ void clearPendingIapTunnel$default(CarPlayMediaEngine carPlayMediaEngine, AirPlaySession airPlaySession, int i, Object obj) {
        if ((i & 1) != 0) {
            airPlaySession = null;
        }
        carPlayMediaEngine.clearPendingIapTunnel(airPlaySession);
    }

    private final void clearPendingIapTunnel(AirPlaySession session) {
        AirPlayIapTunnelStream bridge;
        ConcurrentHashMap<AirPlaySession, PendingIapTunnel> concurrentHashMap = this.pendingIapTunnels;
        if (session == null) {
            Collection<PendingIapTunnel> collectionValues = concurrentHashMap.values();
            Intrinsics.checkNotNullExpressionValue(collectionValues, "<get-values>(...)");
            Iterable pending = CollectionsKt.toList(collectionValues);
            this.pendingIapTunnels.clear();
            Iterable $this$forEach$iv = pending;
            for (Object element$iv : $this$forEach$iv) {
                PendingIapTunnel it = (PendingIapTunnel) element$iv;
                it.getBridge().close();
            }
            return;
        }
        PendingIapTunnel pendingIapTunnelRemove = concurrentHashMap.remove(session);
        if (pendingIapTunnelRemove == null || (bridge = pendingIapTunnelRemove.getBridge()) == null) {
            return;
        }
        bridge.close();
    }

    private final byte[] outputKey(AirPlaySession session, Map<String, ? extends Object> stream) {
        return dataStreamKey(session, stream, DATASTREAM_OUTPUT_KEY);
    }

    private final MicrophoneConfig microphoneConfig(AirPlaySession session, int type, Map<String, ? extends Object> stream, AudioFormat format) {
        InetAddress host;
        byte[] key;
        long j;
        int opusBitrate;
        if (this.microphoneEnabled && type == 100) {
            if (!Intrinsics.areEqual(format.getAudioType(), "telephony") && !Intrinsics.areEqual(format.getAudioType(), "speechrecognition")) {
                return null;
            }
            Object obj = stream.get("dataPort");
            Number number = obj instanceof Number ? (Number) obj : null;
            if (number == null) {
                return null;
            }
            int port = number.intValue();
            if (!(1 <= port && port < 65536) || (host = session.getRemoteAddress$core_airplay()) == null || (key = dataStreamKey(session, stream, DATASTREAM_INPUT_KEY)) == null) {
                return null;
            }
            Object obj2 = stream.get("audioFormat");
            Number number2 = obj2 instanceof Number ? (Number) obj2 : null;
            long formatBits = number2 != null ? number2.longValue() : 0L;
            Object obj3 = stream.get("framesPerPacket");
            Number number3 = obj3 instanceof Number ? (Number) obj3 : null;
            int framesPerPacket = number3 != null ? number3.intValue() : 0;
            int frameMillis = 20;
            if (format.getCodec() != AudioCodecKind.OPUS && framesPerPacket > 0) {
                j = 0;
                frameMillis = RangesKt.coerceIn((int) Math.round((((double) framesPerPacket) * 1000.0d) / ((double) format.getSampleRate())), 5, 60);
            } else {
                j = 0;
            }
            if ((OPUS_48K & formatBits) != j) {
                opusBitrate = 96000;
            } else {
                opusBitrate = (OPUS_24K & formatBits) != j ? 64000 : 48000;
            }
            return new MicrophoneConfig(format.getAudioType(), format.getSampleRate(), format.getChannels(), type, frameMillis, host, port, key, format.getCodec(), format.getCodec() == AudioCodecKind.OPUS ? Integer.valueOf(opusBitrate) : null, MicrophoneConfig.INSTANCE.opusClockRate(formatBits));
        }
        return null;
    }

    private final byte[] dataStreamKey(AirPlaySession session, Map<String, ? extends Object> stream, String label) {
        String connectionId;
        byte[] shared = session.getSharedSecret();
        if (shared == null || (connectionId = CarPlayMediaEngineKt.unsignedPlistDecimal(stream.get("streamConnectionID"))) == null) {
            return null;
        }
        AirPlayCrypto airPlayCrypto = AirPlayCrypto.INSTANCE;
        byte[] bytes = ("DataStream-Salt" + connectionId).getBytes(Charsets.US_ASCII);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        byte[] bytes2 = label.getBytes(Charsets.US_ASCII);
        Intrinsics.checkNotNullExpressionValue(bytes2, "getBytes(...)");
        return airPlayCrypto.hkdfSha512(shared, bytes, bytes2, 32);
    }

    private final boolean isScreenStreamType(int type) {
        return type == 110 || type == 111;
    }

    /* JADX INFO: compiled from: CarPlayMediaEngine.kt */
    @Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\t\n\u0002\b\u0004\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lcom/carplay/airplay/CarPlayMediaEngine$Companion;", "", "<init>", "()V", "TAG", "", "STREAM_TYPE_MAIN_SCREEN", "", "STREAM_TYPE_ALT_SCREEN", "STREAM_TYPE_MAIN_AUDIO", "STREAM_TYPE_DATA", "DATASTREAM_OUTPUT_KEY", "DATASTREAM_INPUT_KEY", "IAP_DATASTREAM_UUID", "VIDEO_SETTINGS_STREAM_ID", "", "FIRST_REMOTE_CONTROL_STREAM_ID", "OPUS_24K", "OPUS_48K", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
