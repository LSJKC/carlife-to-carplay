package com.carlink.cast;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import com.carplay.iap2.body.Iap2BodyBuilder;
import com.carplay.iap2.body.Iap2BodyReader;
import com.carplay.iap2.message.Iap2ControlMessages;
import com.carplay.iap2.message.Iap2Messages;
import com.carplay.iap2.wire.Iap2Frame;
import com.carplay.iap2.wire.Iap2Parameter;
import com.carplay.iap2.wire.Iap2ProtocolException;
import com.carplay.sender.MonotonicAudioTimestamp;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import javax.jmdns.impl.constants.DNSConstants;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.UByte;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.sequences.SequencesKt;
import kotlin.text.CharsKt;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import kotlinx.coroutines.scheduling.WorkQueueKt;
import org.bouncycastle.i18n.MessageBundle;

/* JADX INFO: compiled from: NowPlayingPublisher.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000ª\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\b\u0000\u0018\u0000 _2\u00060\u0001j\u0002`\u0002:\u0004\\]^_B#\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006¢\u0006\u0004\b\t\u0010\nJ:\u0010.\u001a\u00020\u001b2\u0006\u0010/\u001a\u00020\u00162\u0012\u00100\u001a\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\b0\u00062\u0016\b\u0002\u00101\u001a\u0010\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u0019\u0018\u00010\u0006J\u0017\u00102\u001a\u00020\b2\n\b\u0002\u00103\u001a\u0004\u0018\u00010\u001b¢\u0006\u0002\u00104J\u0006\u00105\u001a\u00020\u0019J2\u00106\u001a\u00020\u001b2\u0012\u00100\u001a\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\b0\u00062\u0016\b\u0002\u00101\u001a\u0010\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u0019\u0018\u00010\u0006J\u000e\u00107\u001a\u00020\u00142\u0006\u0010/\u001a\u00020\u0016J'\u00108\u001a\u00020\u00162\u0006\u00109\u001a\u00020:2\u0006\u0010\u0013\u001a\u00020\u00142\n\b\u0002\u0010;\u001a\u0004\u0018\u00010&¢\u0006\u0002\u0010<J\u0010\u0010=\u001a\u00020\u00182\u0006\u00109\u001a\u00020:H\u0002J\b\u0010>\u001a\u00020\bH\u0002J\f\u0010?\u001a\u00020\u0007*\u00020\u0007H\u0002J\b\u0010@\u001a\u00020:H\u0002J\u000e\u0010A\u001a\u00020\b2\u0006\u0010B\u001a\u00020\u0018J\u0010\u0010C\u001a\u00020\b2\u0006\u00109\u001a\u00020:H\u0002J\u0010\u0010D\u001a\u00020\b2\u0006\u0010E\u001a\u00020+H\u0002J\u0014\u0010F\u001a\u0004\u0018\u00010\u00182\b\u0010G\u001a\u0004\u0018\u00010HH\u0002J\u0014\u0010I\u001a\u0004\u0018\u00010J2\b\u0010G\u001a\u0004\u0018\u00010HH\u0002J\u0018\u0010K\u001a\u0004\u0018\u00010L2\f\u0010M\u001a\b\u0012\u0004\u0012\u00020L0NH\u0002J\u0012\u0010O\u001a\u00020\u00192\b\u0010G\u001a\u0004\u0018\u00010HH\u0002J+\u0010P\u001a\u00020\u00072\b\u0010G\u001a\u0004\u0018\u00010H2\u0012\u0010Q\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00070R\"\u00020\u0007H\u0002¢\u0006\u0002\u0010SJ\u001c\u0010T\u001a\u00020\u00072\b\u0010G\u001a\u0004\u0018\u00010H2\b\u0010U\u001a\u0004\u0018\u00010VH\u0002J\u0010\u0010W\u001a\u00020\u001b2\u0006\u0010X\u001a\u00020\u0018H\u0002J\b\u0010[\u001a\u00020\bH\u0016R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u000b\u001a\n \f*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\r\u001a\n \f*\u0004\u0018\u00010\u000e0\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\u0015\u001a\u0010\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\b\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\u0017\u001a\u0010\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u0019\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010!\u001a\b\u0012\u0002\b\u0003\u0018\u00010\"X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010#\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0012X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u001bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010(\u001a\u0004\u0018\u00010&X\u0082\u000e¢\u0006\u0004\n\u0002\u0010)R\u0010\u0010*\u001a\u0004\u0018\u00010+X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020-X\u0082\u0004¢\u0006\u0002\n\u0000R\u0012\u0010Y\u001a\u0004\u0018\u00010\u001bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010Z¨\u0006`"}, d2 = {"Lcom/carlink/cast/NowPlayingPublisher;", "Ljava/lang/AutoCloseable;", "Lkotlin/AutoCloseable;", "context", "Landroid/content/Context;", "log", "Lkotlin/Function1;", "", "", "<init>", "(Landroid/content/Context;Lkotlin/jvm/functions/Function1;)V", "appContext", "kotlin.jvm.PlatformType", "manager", "Landroid/media/session/MediaSessionManager;", "executor", "Ljava/util/concurrent/ScheduledExecutorService;", "sendLock", "", "request", "Lcom/carlink/cast/NowPlayingPublisher$Request;", "send", "Lcom/carplay/iap2/wire/Iap2Frame;", "sendFileTransfer", "", "", "subscriptionToken", "", "nextToken", "Ljava/util/concurrent/atomic/AtomicLong;", "closed", "permissionLogged", "emptySessionLogged", "periodic", "Ljava/util/concurrent/ScheduledFuture;", "lastSessionDiagnostic", "artworkLock", "nextArtworkId", "", "artworkFingerprint", "artworkTransferId", "Ljava/lang/Integer;", "pendingArtwork", "Lcom/carlink/cast/NowPlayingPublisher$PendingArtwork;", "emptySnapshotLogged", "Ljava/util/concurrent/atomic/AtomicBoolean;", "subscribe", "frame", "sender", "fileSender", "unsubscribe", "token", "(Ljava/lang/Long;)V", "hasSubscription", "subscribeDefault", "parseRequest", "encode", "snapshot", "Lcom/carlink/cast/NowPlayingPublisher$Snapshot;", "artworkId", "(Lcom/carlink/cast/NowPlayingPublisher$Snapshot;Lcom/carlink/cast/NowPlayingPublisher$Request;Ljava/lang/Integer;)Lcom/carplay/iap2/wire/Iap2Frame;", "persistentId", "publish", "logValue", "readSnapshot", "onFileTransferDatagram", "datagram", "maybeStartArtwork", "sendArtworkChunks", "artwork", "artworkBytes", "metadata", "Landroid/media/MediaMetadata;", "artworkFromUri", "Landroid/graphics/Bitmap;", "chooseController", "Landroid/media/session/MediaController;", "active", "", "hasUsableMetadata", "firstMetadataString", "keys", "", "(Landroid/media/MediaMetadata;[Ljava/lang/String;)Ljava/lang/String;", "readLyrics", "playback", "Landroid/media/session/PlaybackState;", "fingerprint", "bytes", "lastFallbackUpdateAt", "Ljava/lang/Long;", "close", "PendingArtwork", "Request", "Snapshot", "Companion", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class NowPlayingPublisher implements AutoCloseable {

    @Deprecated
    public static final int ARTWORK_CHUNK_BYTES = 1024;

    @Deprecated
    public static final int ARTWORK_FILE_TYPE = 2;

    @Deprecated
    public static final int ARTWORK_NONE = 128;
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int FLAG_FIRST = 128;

    @Deprecated
    public static final int FLAG_LAST = 64;

    @Deprecated
    public static final long FNV_OFFSET = -3750763034362895579L;

    @Deprecated
    public static final long FNV_PRIME = 1099511628211L;

    @Deprecated
    public static final int MAX_ARTWORK_BYTES = 2097152;

    @Deprecated
    public static final String METADATA_KEY_LYRICS = "android.media.metadata.LYRICS";

    @Deprecated
    public static final int OP_CANCEL = 2;

    @Deprecated
    public static final int OP_DATA = 0;

    @Deprecated
    public static final int OP_SETUP = 4;

    @Deprecated
    public static final int OP_START = 1;

    @Deprecated
    public static final int OP_SUCCESS = 5;
    private final Context appContext;
    private long artworkFingerprint;
    private final Object artworkLock;
    private Integer artworkTransferId;
    private volatile boolean closed;
    private volatile boolean emptySessionLogged;
    private final AtomicBoolean emptySnapshotLogged;
    private final ScheduledExecutorService executor;
    private volatile Long lastFallbackUpdateAt;
    private String lastSessionDiagnostic;
    private final Function1<String, Unit> log;
    private final MediaSessionManager manager;
    private int nextArtworkId;
    private final AtomicLong nextToken;
    private PendingArtwork pendingArtwork;
    private volatile ScheduledFuture<?> periodic;
    private volatile boolean permissionLogged;
    private volatile Request request;
    private volatile Function1<? super Iap2Frame, Unit> send;
    private volatile Function1<? super byte[], Boolean> sendFileTransfer;
    private final Object sendLock;
    private volatile long subscriptionToken;

    /* JADX WARN: Multi-variable type inference failed */
    public NowPlayingPublisher(Context context, Function1<? super String, Unit> log) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(log, "log");
        this.log = log;
        this.appContext = context.getApplicationContext();
        this.manager = (MediaSessionManager) this.appContext.getSystemService(MediaSessionManager.class);
        ScheduledExecutorService scheduledExecutorServiceNewSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() { // from class: com.carlink.cast.NowPlayingPublisher$$ExternalSyntheticLambda5
            @Override // java.util.concurrent.ThreadFactory
            public final Thread newThread(Runnable runnable) {
                return NowPlayingPublisher.executor$lambda$1(runnable);
            }
        });
        Intrinsics.checkNotNullExpressionValue(scheduledExecutorServiceNewSingleThreadScheduledExecutor, "newSingleThreadScheduledExecutor(...)");
        this.executor = scheduledExecutorServiceNewSingleThreadScheduledExecutor;
        this.sendLock = new Object();
        this.nextToken = new AtomicLong();
        this.artworkLock = new Object();
        this.nextArtworkId = 1;
        this.emptySnapshotLogged = new AtomicBoolean();
    }

    static final Thread executor$lambda$1(Runnable it) {
        Thread thread = new Thread(it, "carlink-now-playing");
        thread.setDaemon(true);
        return thread;
    }

    /* JADX INFO: compiled from: NowPlayingPublisher.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0012\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0007HÆ\u0003J'\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0017\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0018\u001a\u00020\u0019HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u001a"}, d2 = {"Lcom/carlink/cast/NowPlayingPublisher$PendingArtwork;", "", "id", "", "fingerprint", "", "bytes", "", "<init>", "(IJ[B)V", "getId", "()I", "getFingerprint", "()J", "getBytes", "()[B", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class PendingArtwork {
        private final byte[] bytes;
        private final long fingerprint;
        private final int id;

        public static /* synthetic */ PendingArtwork copy$default(PendingArtwork pendingArtwork, int i, long j, byte[] bArr, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                i = pendingArtwork.id;
            }
            if ((i2 & 2) != 0) {
                j = pendingArtwork.fingerprint;
            }
            if ((i2 & 4) != 0) {
                bArr = pendingArtwork.bytes;
            }
            return pendingArtwork.copy(i, j, bArr);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final int getId() {
            return this.id;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final long getFingerprint() {
            return this.fingerprint;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final byte[] getBytes() {
            return this.bytes;
        }

        public final PendingArtwork copy(int id, long fingerprint, byte[] bytes) {
            Intrinsics.checkNotNullParameter(bytes, "bytes");
            return new PendingArtwork(id, fingerprint, bytes);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof PendingArtwork)) {
                return false;
            }
            PendingArtwork pendingArtwork = (PendingArtwork) other;
            return this.id == pendingArtwork.id && this.fingerprint == pendingArtwork.fingerprint && Intrinsics.areEqual(this.bytes, pendingArtwork.bytes);
        }

        public int hashCode() {
            return (((Integer.hashCode(this.id) * 31) + Long.hashCode(this.fingerprint)) * 31) + Arrays.hashCode(this.bytes);
        }

        public String toString() {
            return "PendingArtwork(id=" + this.id + ", fingerprint=" + this.fingerprint + ", bytes=" + Arrays.toString(this.bytes) + ")";
        }

        public PendingArtwork(int id, long fingerprint, byte[] bytes) {
            Intrinsics.checkNotNullParameter(bytes, "bytes");
            this.id = id;
            this.fingerprint = fingerprint;
            this.bytes = bytes;
        }

        public final int getId() {
            return this.id;
        }

        public final long getFingerprint() {
            return this.fingerprint;
        }

        public final byte[] getBytes() {
            return this.bytes;
        }
    }

    /* JADX INFO: compiled from: NowPlayingPublisher.kt */
    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\"\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B#\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\u0004\b\u0006\u0010\u0007J\u000f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\u000f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J)\u0010\r\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0004HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\t¨\u0006\u0014"}, d2 = {"Lcom/carlink/cast/NowPlayingPublisher$Request;", "", "media", "", "", "playback", "<init>", "(Ljava/util/Set;Ljava/util/Set;)V", "getMedia", "()Ljava/util/Set;", "getPlayback", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Request {
        private final Set<Integer> media;
        private final Set<Integer> playback;

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Request copy$default(Request request, Set set, Set set2, int i, Object obj) {
            if ((i & 1) != 0) {
                set = request.media;
            }
            if ((i & 2) != 0) {
                set2 = request.playback;
            }
            return request.copy(set, set2);
        }

        public final Set<Integer> component1() {
            return this.media;
        }

        public final Set<Integer> component2() {
            return this.playback;
        }

        public final Request copy(Set<Integer> media, Set<Integer> playback) {
            Intrinsics.checkNotNullParameter(media, "media");
            Intrinsics.checkNotNullParameter(playback, "playback");
            return new Request(media, playback);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Request)) {
                return false;
            }
            Request request = (Request) other;
            return Intrinsics.areEqual(this.media, request.media) && Intrinsics.areEqual(this.playback, request.playback);
        }

        public int hashCode() {
            return (this.media.hashCode() * 31) + this.playback.hashCode();
        }

        public String toString() {
            return "Request(media=" + this.media + ", playback=" + this.playback + ")";
        }

        public Request(Set<Integer> media, Set<Integer> playback) {
            Intrinsics.checkNotNullParameter(media, "media");
            Intrinsics.checkNotNullParameter(playback, "playback");
            this.media = media;
            this.playback = playback;
        }

        public final Set<Integer> getMedia() {
            return this.media;
        }

        public final Set<Integer> getPlayback() {
            return this.playback;
        }
    }

    /* JADX INFO: compiled from: NowPlayingPublisher.kt */
    @Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u001d\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0086\b\u0018\u00002\u00020\u0001Bm\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\t\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e\u0012\b\b\u0002\u0010\u000f\u001a\u00020\t¢\u0006\u0004\b\u0010\u0010\u0011J\t\u0010 \u001a\u00020\u0003HÆ\u0003J\t\u0010!\u001a\u00020\u0003HÆ\u0003J\t\u0010\"\u001a\u00020\u0003HÆ\u0003J\t\u0010#\u001a\u00020\u0003HÆ\u0003J\t\u0010$\u001a\u00020\u0003HÆ\u0003J\t\u0010%\u001a\u00020\tHÆ\u0003J\t\u0010&\u001a\u00020\tHÆ\u0003J\t\u0010'\u001a\u00020\fHÆ\u0003J\u000b\u0010(\u001a\u0004\u0018\u00010\u000eHÆ\u0003J\t\u0010)\u001a\u00020\tHÆ\u0003Jo\u0010*\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\t2\b\b\u0002\u0010\u000b\u001a\u00020\f2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\tHÆ\u0001J\u0013\u0010+\u001a\u00020,2\b\u0010-\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010.\u001a\u00020\fHÖ\u0001J\t\u0010/\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0013R\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0013R\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0013R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\n\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0019R\u0011\u0010\u000b\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0013\u0010\r\u001a\u0004\u0018\u00010\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\u000f\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0019¨\u0006\u0007"}, d2 = {"Lcom/carlink/cast/NowPlayingPublisher$Snapshot;", "", MessageBundle.TITLE_ENTRY, "", "artist", "album", "lyrics", "app", "durationMs", "", "positionMs", "state", "", "artwork", "", "artworkFingerprint", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJI[BJ)V", "getTitle", "()Ljava/lang/String;", "getArtist", "getAlbum", "getLyrics", "getApp", "getDurationMs", "()J", "getPositionMs", "getState", "()I", "getArtwork", "()[B", "getArtworkFingerprint", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "copy", "equals", "", "other", "hashCode", "toString"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Snapshot {
        private final String album;
        private final String app;
        private final String artist;
        private final byte[] artwork;
        private final long artworkFingerprint;
        private final long durationMs;
        private final String lyrics;
        private final long positionMs;
        private final int state;
        private final String title;

        public Snapshot() {
            this(null, null, null, null, null, 0L, 0L, 0, null, 0L, 1023, null);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final String getTitle() {
            return this.title;
        }

        /* JADX INFO: renamed from: component10, reason: from getter */
        public final long getArtworkFingerprint() {
            return this.artworkFingerprint;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final String getArtist() {
            return this.artist;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final String getAlbum() {
            return this.album;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final String getLyrics() {
            return this.lyrics;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final String getApp() {
            return this.app;
        }

        /* JADX INFO: renamed from: component6, reason: from getter */
        public final long getDurationMs() {
            return this.durationMs;
        }

        /* JADX INFO: renamed from: component7, reason: from getter */
        public final long getPositionMs() {
            return this.positionMs;
        }

        /* JADX INFO: renamed from: component8, reason: from getter */
        public final int getState() {
            return this.state;
        }

        /* JADX INFO: renamed from: component9, reason: from getter */
        public final byte[] getArtwork() {
            return this.artwork;
        }

        public final Snapshot copy(String title, String artist, String album, String lyrics, String app, long durationMs, long positionMs, int state, byte[] artwork, long artworkFingerprint) {
            Intrinsics.checkNotNullParameter(title, "title");
            Intrinsics.checkNotNullParameter(artist, "artist");
            Intrinsics.checkNotNullParameter(album, "album");
            Intrinsics.checkNotNullParameter(lyrics, "lyrics");
            Intrinsics.checkNotNullParameter(app, "app");
            return new Snapshot(title, artist, album, lyrics, app, durationMs, positionMs, state, artwork, artworkFingerprint);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Snapshot)) {
                return false;
            }
            Snapshot snapshot = (Snapshot) other;
            return Intrinsics.areEqual(this.title, snapshot.title) && Intrinsics.areEqual(this.artist, snapshot.artist) && Intrinsics.areEqual(this.album, snapshot.album) && Intrinsics.areEqual(this.lyrics, snapshot.lyrics) && Intrinsics.areEqual(this.app, snapshot.app) && this.durationMs == snapshot.durationMs && this.positionMs == snapshot.positionMs && this.state == snapshot.state && Intrinsics.areEqual(this.artwork, snapshot.artwork) && this.artworkFingerprint == snapshot.artworkFingerprint;
        }

        public int hashCode() {
            return (((((((((((((((((this.title.hashCode() * 31) + this.artist.hashCode()) * 31) + this.album.hashCode()) * 31) + this.lyrics.hashCode()) * 31) + this.app.hashCode()) * 31) + Long.hashCode(this.durationMs)) * 31) + Long.hashCode(this.positionMs)) * 31) + Integer.hashCode(this.state)) * 31) + (this.artwork == null ? 0 : Arrays.hashCode(this.artwork))) * 31) + Long.hashCode(this.artworkFingerprint);
        }

        public String toString() {
            return "Snapshot(title=" + this.title + ", artist=" + this.artist + ", album=" + this.album + ", lyrics=" + this.lyrics + ", app=" + this.app + ", durationMs=" + this.durationMs + ", positionMs=" + this.positionMs + ", state=" + this.state + ", artwork=" + Arrays.toString(this.artwork) + ", artworkFingerprint=" + this.artworkFingerprint + ")";
        }

        public Snapshot(String title, String artist, String album, String lyrics, String app, long durationMs, long positionMs, int state, byte[] artwork, long artworkFingerprint) {
            Intrinsics.checkNotNullParameter(title, "title");
            Intrinsics.checkNotNullParameter(artist, "artist");
            Intrinsics.checkNotNullParameter(album, "album");
            Intrinsics.checkNotNullParameter(lyrics, "lyrics");
            Intrinsics.checkNotNullParameter(app, "app");
            this.title = title;
            this.artist = artist;
            this.album = album;
            this.lyrics = lyrics;
            this.app = app;
            this.durationMs = durationMs;
            this.positionMs = positionMs;
            this.state = state;
            this.artwork = artwork;
            this.artworkFingerprint = artworkFingerprint;
        }

        public /* synthetic */ Snapshot(String str, String str2, String str3, String str4, String str5, long j, long j2, int i, byte[] bArr, long j3, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            this((i2 & 1) != 0 ? "" : str, (i2 & 2) != 0 ? "" : str2, (i2 & 4) != 0 ? "" : str3, (i2 & 8) != 0 ? "" : str4, (i2 & 16) == 0 ? str5 : "", (i2 & 32) != 0 ? 0L : j, (i2 & 64) != 0 ? 0L : j2, (i2 & 128) != 0 ? 0 : i, (i2 & 256) != 0 ? null : bArr, (i2 & 512) == 0 ? j3 : 0L);
        }

        public final String getTitle() {
            return this.title;
        }

        public final String getArtist() {
            return this.artist;
        }

        public final String getAlbum() {
            return this.album;
        }

        public final String getLyrics() {
            return this.lyrics;
        }

        public final String getApp() {
            return this.app;
        }

        public final long getDurationMs() {
            return this.durationMs;
        }

        public final long getPositionMs() {
            return this.positionMs;
        }

        public final int getState() {
            return this.state;
        }

        public final byte[] getArtwork() {
            return this.artwork;
        }

        public final long getArtworkFingerprint() {
            return this.artworkFingerprint;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ long subscribe$default(NowPlayingPublisher nowPlayingPublisher, Iap2Frame iap2Frame, Function1 function1, Function1 function2, int i, Object obj) {
        if ((i & 4) != 0) {
            function2 = null;
        }
        return nowPlayingPublisher.subscribe(iap2Frame, function1, function2);
    }

    public final long subscribe(Iap2Frame frame, Function1<? super Iap2Frame, Unit> sender, Function1<? super byte[], Boolean> fileSender) {
        Object objM159constructorimpl;
        Intrinsics.checkNotNullParameter(frame, "frame");
        Intrinsics.checkNotNullParameter(sender, "sender");
        try {
            Result.Companion companion = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(parseRequest(frame));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (thM162exceptionOrNullimpl != null) {
            this.log.invoke("NowPlaying 订阅解析失败：" + thM162exceptionOrNullimpl.getMessage());
            return 0L;
        }
        Request parsed = (Request) objM159constructorimpl;
        long token = this.nextToken.incrementAndGet();
        this.subscriptionToken = token;
        this.request = parsed;
        this.send = sender;
        this.sendFileTransfer = fileSender;
        synchronized (this.artworkLock) {
            this.artworkFingerprint = 0L;
            this.artworkTransferId = null;
            this.pendingArtwork = null;
            Unit unit = Unit.INSTANCE;
        }
        publish();
        this.log.invoke("NowPlaying 订阅#" + token + "：media=" + CollectionsKt.sorted(parsed.getMedia()) + " playback=" + CollectionsKt.sorted(parsed.getPlayback()) + " 封面字段=" + (parsed.getMedia().contains(26) ? "请求" : "未请求"));
        if (!this.executor.isShutdown() && this.periodic == null) {
            this.periodic = this.executor.scheduleWithFixedDelay(new Runnable() { // from class: com.carlink.cast.NowPlayingPublisher$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.publish();
                }
            }, 2L, 2L, TimeUnit.SECONDS);
        }
        return token;
    }

    public static /* synthetic */ void unsubscribe$default(NowPlayingPublisher nowPlayingPublisher, Long l, int i, Object obj) {
        if ((i & 1) != 0) {
            l = null;
        }
        nowPlayingPublisher.unsubscribe(l);
    }

    public final void unsubscribe(Long token) {
        if (token != null) {
            if (token.longValue() != this.subscriptionToken) {
                this.log.invoke("NowPlaying 忽略旧订阅#" + token + " 的停止请求，当前订阅#" + this.subscriptionToken + " 仍有效");
                return;
            }
        }
        this.request = null;
        this.send = null;
        this.sendFileTransfer = null;
        synchronized (this.artworkLock) {
            this.pendingArtwork = null;
            this.artworkTransferId = null;
            Unit unit = Unit.INSTANCE;
        }
    }

    public final boolean hasSubscription() {
        return (this.request == null || this.send == null) ? false : true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ long subscribeDefault$default(NowPlayingPublisher nowPlayingPublisher, Function1 function1, Function1 function2, int i, Object obj) {
        if ((i & 2) != 0) {
            function2 = null;
        }
        return nowPlayingPublisher.subscribeDefault(function1, function2);
    }

    public final long subscribeDefault(Function1<? super Iap2Frame, Unit> sender, Function1<? super byte[], Boolean> fileSender) {
        Intrinsics.checkNotNullParameter(sender, "sender");
        Iap2Frame frame = (Iap2Frame) CollectionsKt.first((List) Iap2ControlMessages.INSTANCE.subscriptions());
        long token = subscribe(frame, sender, fileSender);
        this.log.invoke("NowPlaying 未收到车机订阅，启用默认媒体字段发布");
        return token;
    }

    public final Request parseRequest(Iap2Frame frame) {
        Intrinsics.checkNotNullParameter(frame, "frame");
        if (!(frame.getMessageId() == 20480)) {
            String string = Integer.toString(frame.getMessageId(), CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            throw new IllegalArgumentException(("Expected StartNowPlayingUpdates, got 0x" + string).toString());
        }
        Iap2BodyReader body = Iap2Messages.INSTANCE.reader(frame);
        return new Request(parseRequest$fields(body, 0), parseRequest$fields(body, 1));
    }

    private static final Set<Integer> parseRequest$fields(Iap2BodyReader body, int groupId) {
        Set<Integer> set;
        Iterable list;
        Iap2BodyReader iap2BodyReaderOptionalGroup = body.optionalGroup(groupId);
        if (iap2BodyReaderOptionalGroup == null || (list = iap2BodyReaderOptionalGroup.list()) == null) {
            set = null;
        } else {
            Iterable iterable = list;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            Iterator it = iterable.iterator();
            while (it.hasNext()) {
                arrayList.add(Integer.valueOf(((Iap2Parameter) it.next()).getId()));
            }
            set = CollectionsKt.toSet((List) arrayList);
        }
        return set == null ? SetsKt.emptySet() : set;
    }

    public static /* synthetic */ Iap2Frame encode$default(NowPlayingPublisher nowPlayingPublisher, Snapshot snapshot, Request request, Integer num, int i, Object obj) {
        if ((i & 4) != 0) {
            num = null;
        }
        return nowPlayingPublisher.encode(snapshot, request, num);
    }

    public final Iap2Frame encode(final Snapshot snapshot, final Request request, final Integer artworkId) throws Iap2ProtocolException {
        Intrinsics.checkNotNullParameter(snapshot, "snapshot");
        Intrinsics.checkNotNullParameter(request, "request");
        Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
        Iap2BodyBuilder iap2BodyBuilderOpaque = Iap2BodyBuilder.INSTANCE.opaque(20481);
        if (!request.getMedia().isEmpty()) {
            iap2BodyBuilderOpaque.group(0, new Function1() { // from class: com.carlink.cast.NowPlayingPublisher$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return NowPlayingPublisher.encode$lambda$10$lambda$8(request, this, snapshot, artworkId, (Iap2BodyBuilder) obj);
                }
            });
        }
        if (!request.getPlayback().isEmpty()) {
            iap2BodyBuilderOpaque.group(1, new Function1() { // from class: com.carlink.cast.NowPlayingPublisher$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return NowPlayingPublisher.encode$lambda$10$lambda$9(request, snapshot, (Iap2BodyBuilder) obj);
                }
            });
        }
        return iap2BodyBuilderOpaque.build();
    }

    static final Unit encode$lambda$10$lambda$8(Request $request, NowPlayingPublisher this$0, Snapshot $snapshot, Integer $artworkId, Iap2BodyBuilder group) {
        Intrinsics.checkNotNullParameter(group, "$this$group");
        if ($request.getMedia().contains(0)) {
            group.bytes(0, this$0.persistentId($snapshot));
        }
        if ($request.getMedia().contains(1)) {
            group.string(1, $snapshot.getTitle());
        }
        if ($request.getMedia().contains(4)) {
            group.u32(4, RangesKt.coerceIn($snapshot.getDurationMs(), 0L, MonotonicAudioTimestamp.RTP_MASK));
        }
        if ($request.getMedia().contains(6)) {
            group.string(6, $snapshot.getAlbum());
        }
        if ($request.getMedia().contains(12)) {
            group.string(12, $snapshot.getArtist());
        }
        if ($request.getMedia().contains(26)) {
            group.u8(26, $artworkId != null ? $artworkId.intValue() : 128);
        }
        return Unit.INSTANCE;
    }

    static final Unit encode$lambda$10$lambda$9(Request $request, Snapshot $snapshot, Iap2BodyBuilder group) {
        Intrinsics.checkNotNullParameter(group, "$this$group");
        if ($request.getPlayback().contains(0)) {
            group.u8(0, RangesKt.coerceIn($snapshot.getState(), 0, 2));
        }
        if ($request.getPlayback().contains(1)) {
            group.u32(1, RangesKt.coerceIn($snapshot.getPositionMs(), 0L, MonotonicAudioTimestamp.RTP_MASK));
        }
        if ($request.getPlayback().contains(7)) {
            group.string(7, $snapshot.getApp());
        }
        if ($request.getPlayback().contains(16)) {
            group.string(16, $snapshot.getApp());
        }
        return Unit.INSTANCE;
    }

    private final byte[] persistentId(Snapshot snapshot) {
        long hash = FNV_OFFSET;
        String value = snapshot.getApp() + "\u0000" + snapshot.getTitle() + "\u0000" + snapshot.getArtist() + "\u0000" + snapshot.getAlbum();
        byte[] bytes = value.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        for (byte b : bytes) {
            hash = ((((long) b) & 255) ^ hash) * FNV_PRIME;
        }
        if (hash == 0) {
            hash = 1;
        }
        byte[] bArr = new byte[8];
        for (int i = 0; i < 8; i++) {
            bArr[i] = (byte) (hash >>> ((7 - i) * 8));
        }
        return bArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void publish() {
        Request currentRequest;
        Function1<? super Iap2Frame, Unit> function1;
        Integer currentArtworkId;
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        if (this.closed || (currentRequest = this.request) == null || (function1 = this.send) == null) {
            return;
        }
        Snapshot snapshot = readSnapshot();
        boolean hasMetadata = (StringsKt.isBlank(snapshot.getTitle()) && StringsKt.isBlank(snapshot.getArtist()) && StringsKt.isBlank(snapshot.getAlbum()) && StringsKt.isBlank(snapshot.getLyrics()) && snapshot.getDurationMs() <= 0 && snapshot.getArtwork() == null) ? false : true;
        AtomicBoolean atomicBoolean = this.emptySnapshotLogged;
        if (!hasMetadata) {
            if (atomicBoolean.compareAndSet(false, true)) {
                this.log.invoke("NowPlaying 暂无可发布媒体元数据，跳过空 0x5001，避免覆盖车机已有媒体卡");
                return;
            }
            return;
        }
        atomicBoolean.set(false);
        maybeStartArtwork(snapshot);
        synchronized (this.artworkLock) {
            currentArtworkId = this.artworkTransferId;
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(encode(snapshot, currentRequest, currentArtworkId));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (thM162exceptionOrNullimpl != null) {
            this.log.invoke("NowPlaying 编码失败：" + thM162exceptionOrNullimpl.getMessage());
            return;
        }
        Iap2Frame frame = (Iap2Frame) objM159constructorimpl;
        try {
            Result.Companion companion3 = Result.INSTANCE;
            synchronized (this.sendLock) {
                function1.invoke(frame);
                Unit unit = Unit.INSTANCE;
            }
            objM159constructorimpl2 = Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        Throwable thM162exceptionOrNullimpl2 = Result.m162exceptionOrNullimpl(objM159constructorimpl2);
        if (thM162exceptionOrNullimpl2 != null) {
            this.log.invoke("NowPlaying 发送失败：" + thM162exceptionOrNullimpl2.getMessage());
        }
        Function1<String, Unit> function2 = this.log;
        String strLogValue = logValue(snapshot.getTitle());
        String strLogValue2 = logValue(snapshot.getArtist());
        String strLogValue3 = logValue(snapshot.getAlbum());
        String strLogValue4 = logValue(snapshot.getLyrics());
        String strLogValue5 = logValue(snapshot.getApp());
        long durationMs = snapshot.getDurationMs();
        int iIntValue = currentArtworkId != null ? currentArtworkId.intValue() : 128;
        byte[] artwork = snapshot.getArtwork();
        function2.invoke("NowPlaying 0x5001：title=" + strLogValue + " artist=" + strLogValue2 + " album=" + strLogValue3 + " lyrics=" + strLogValue4 + " app=" + strLogValue5 + " duration=" + durationMs + " art=" + iIntValue + " artBytes=" + (artwork != null ? artwork.length : 0) + " position=" + snapshot.getPositionMs() + " state=" + snapshot.getState());
    }

    private final String logValue(String $this$logValue) {
        return StringsKt.isBlank($this$logValue) ? "<空>" : StringsKt.take(StringsKt.replace$default($this$logValue, "\n", "\\n", false, 4, (Object) null), DNSConstants.KNOWN_ANSWER_TTL);
    }

    /* JADX WARN: Code duplicated, block: B:85:0x028b  */
    private final Snapshot readSnapshot() {
        List<MediaController> listEmptyList;
        int i;
        boolean playing;
        long j;
        long jCoerceAtLeast;
        long position;
        long durationMs;
        long positionMs;
        int state;
        MediaSessionSnapshotStore.Snapshot fallback = MediaSessionSnapshotStore.INSTANCE.getCurrent();
        try {
            ComponentName component = new ComponentName(this.appContext, (Class<?>) MediaListenerService.class);
            MediaSessionManager mediaSessionManager = this.manager;
            List<MediaController> activeSessions = mediaSessionManager != null ? mediaSessionManager.getActiveSessions(component) : null;
            if (activeSessions == null) {
                activeSessions = CollectionsKt.emptyList();
            }
            List<MediaController> list = activeSessions;
            if (list.isEmpty()) {
                MediaSessionManager mediaSessionManager2 = this.manager;
                List<MediaController> activeSessions2 = mediaSessionManager2 != null ? mediaSessionManager2.getActiveSessions(null) : null;
                if (activeSessions2 == null) {
                    activeSessions2 = CollectionsKt.emptyList();
                }
                list = activeSessions2;
            }
            listEmptyList = list;
        } catch (SecurityException failure) {
            if (!this.permissionLogged) {
                this.permissionLogged = true;
                this.log.invoke("NowPlaying 无法读取 MediaSession（未授予通知使用权）：" + failure.getMessage());
            }
            listEmptyList = CollectionsKt.emptyList();
        }
        if (listEmptyList.isEmpty() && !this.emptySessionLogged) {
            this.emptySessionLogged = true;
            this.log.invoke("NowPlaying 没有可读的 MediaSession；请确认已授予 CarLink Cast 通知使用权，且播放器已建立媒体会话");
        } else if (!listEmptyList.isEmpty()) {
            this.emptySessionLogged = false;
        }
        MediaController controller = chooseController(listEmptyList);
        if (controller == null && fallback != null) {
            long updatedAt = fallback.getUpdatedAt();
            Long l = this.lastFallbackUpdateAt;
            if (updatedAt > (l != null ? l.longValue() : Long.MIN_VALUE)) {
                this.lastFallbackUpdateAt = Long.valueOf(fallback.getUpdatedAt());
                this.log.invoke("NowPlaying 使用通知监听回退：" + fallback.getPackageName());
            }
            String title = fallback.getTitle();
            String artist = fallback.getArtist();
            String album = fallback.getAlbum();
            String lyrics = fallback.getLyrics();
            long durationMs2 = fallback.getDurationMs();
            long positionMs2 = fallback.getPositionMs();
            int state2 = fallback.getState();
            String packageName = fallback.getPackageName();
            byte[] artwork = fallback.getArtwork();
            byte[] artwork2 = fallback.getArtwork();
            return new Snapshot(title, artist, album, lyrics, packageName, durationMs2, positionMs2, state2, artwork, artwork2 != null ? fingerprint(artwork2) : 0L);
        }
        MediaMetadata metadata = controller != null ? controller.getMetadata() : null;
        PlaybackState playback = controller != null ? controller.getPlaybackState() : null;
        StringBuilder sb = new StringBuilder();
        sb.append("NowPlaying MediaSession：总数=" + listEmptyList.size());
        if (controller == null) {
            sb.append("，未选中");
            i = 1;
        } else {
            i = 1;
            sb.append("，选中=" + controller.getPackageName());
            sb.append("，state=" + (playback != null ? playback.getState() : 0));
            sb.append("，metadata=" + (metadata != null));
            sb.append("，title=" + (!StringsKt.isBlank(firstMetadataString(metadata, "android.media.metadata.TITLE", "android.media.metadata.DISPLAY_TITLE"))));
            sb.append("，artist=" + (!StringsKt.isBlank(firstMetadataString(metadata, "android.media.metadata.ARTIST", "android.media.metadata.ALBUM_ARTIST", "android.media.metadata.AUTHOR"))));
            sb.append("，album=" + (!StringsKt.isBlank(firstMetadataString(metadata, "android.media.metadata.ALBUM", "android.media.metadata.DISPLAY_SUBTITLE"))));
            sb.append("，lyrics=" + (!StringsKt.isBlank(readLyrics(metadata, playback))));
        }
        String sessionDiagnostic = sb.toString();
        if (!Intrinsics.areEqual(sessionDiagnostic, this.lastSessionDiagnostic)) {
            this.lastSessionDiagnostic = sessionDiagnostic;
            this.log.invoke(sessionDiagnostic);
        }
        if (((playback == null || playback.getState() != 3) ? 0 : i) != 0) {
            playing = true;
        } else {
            if (playback == null) {
                if (fallback != null && fallback.getState() == i) {
                    playing = true;
                }
            }
            playing = false;
        }
        if (metadata != null) {
            j = 0;
            jCoerceAtLeast = RangesKt.coerceAtLeast(metadata.getLong("android.media.metadata.DURATION"), 0L);
        } else {
            j = 0;
            jCoerceAtLeast = 0;
        }
        long duration = jCoerceAtLeast;
        long basePosition = playback != null ? RangesKt.coerceAtLeast(playback.getPosition(), j) : 0L;
        if (!playing || playback == null) {
            position = basePosition;
        } else {
            long elapsed = RangesKt.coerceAtLeast(SystemClock.elapsedRealtime() - playback.getLastPositionUpdateTime(), 0L);
            position = RangesKt.coerceAtLeast((long) (basePosition + (((double) playback.getPlaybackSpeed()) * elapsed)), 0L);
        }
        if (duration > 0) {
            position = RangesKt.coerceAtMost(position, duration);
        }
        byte[] bArrArtworkBytes = artworkBytes(metadata);
        if (bArrArtworkBytes == null) {
            bArrArtworkBytes = fallback != 0 ? fallback.getArtwork() : null;
        }
        byte[] artwork3 = bArrArtworkBytes;
        String strFirstMetadataString = firstMetadataString(metadata, "android.media.metadata.TITLE", "android.media.metadata.DISPLAY_TITLE");
        if (StringsKt.isBlank(strFirstMetadataString)) {
            String title2 = fallback != 0 ? fallback.getTitle() : null;
            if (title2 == null) {
                title2 = "";
            }
            strFirstMetadataString = title2;
        }
        String str = strFirstMetadataString;
        String strFirstMetadataString2 = firstMetadataString(metadata, "android.media.metadata.ARTIST", "android.media.metadata.ALBUM_ARTIST", "android.media.metadata.AUTHOR");
        if (StringsKt.isBlank(strFirstMetadataString2)) {
            String artist2 = fallback != 0 ? fallback.getArtist() : null;
            if (artist2 == null) {
                artist2 = "";
            }
            strFirstMetadataString2 = artist2;
        }
        String str2 = strFirstMetadataString2;
        String strFirstMetadataString3 = firstMetadataString(metadata, "android.media.metadata.ALBUM", "android.media.metadata.DISPLAY_SUBTITLE");
        if (StringsKt.isBlank(strFirstMetadataString3)) {
            String album2 = fallback != 0 ? fallback.getAlbum() : null;
            if (album2 == null) {
                album2 = "";
            }
            strFirstMetadataString3 = album2;
        }
        String str3 = strFirstMetadataString3;
        String lyrics2 = readLyrics(metadata, playback);
        if (StringsKt.isBlank(lyrics2)) {
            String lyrics3 = fallback != 0 ? fallback.getLyrics() : null;
            if (lyrics3 == null) {
                lyrics3 = "";
            }
            lyrics2 = lyrics3;
        }
        String str4 = lyrics2;
        String packageName2 = controller != 0 ? controller.getPackageName() : null;
        if (packageName2 == null) {
            packageName2 = "";
        }
        String str5 = packageName2;
        if (StringsKt.isBlank(str5)) {
            String packageName3 = fallback != 0 ? fallback.getPackageName() : null;
            str5 = packageName3 != null ? packageName3 : "";
        }
        String str6 = str5;
        if (duration > 0) {
            durationMs = duration;
        } else {
            durationMs = fallback != 0 ? fallback.getDurationMs() : 0L;
        }
        if (playback != null) {
            positionMs = position;
        } else {
            positionMs = fallback != 0 ? fallback.getPositionMs() : 0L;
        }
        Integer numValueOf = playback != null ? Integer.valueOf(playback.getState()) : null;
        if (numValueOf != null && numValueOf.intValue() == 3) {
            state = 1;
        } else if (numValueOf != null && numValueOf.intValue() == 2) {
            state = 2;
        } else {
            state = fallback != 0 ? fallback.getState() : 0;
        }
        return new Snapshot(str, str2, str3, str4, str6, durationMs, positionMs, state, artwork3, artwork3 != null ? fingerprint(artwork3) : 0L);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public final void onFileTransferDatagram(byte[] datagram) {
        Intrinsics.checkNotNullParameter(datagram, "datagram");
        if (datagram.length < 2) {
            return;
        }
        int id = datagram[0] & UByte.MAX_VALUE;
        int opcode = datagram[1] & 15;
        synchronized (this.artworkLock) {
            PendingArtwork pendingArtwork = this.pendingArtwork;
            if (pendingArtwork != null && pendingArtwork.getId() == id) {
                PendingArtwork pending = null;
                switch (opcode) {
                    case 1:
                        pending = pendingArtwork;
                        break;
                    case 2:
                        this.pendingArtwork = null;
                        this.artworkTransferId = null;
                        this.artworkFingerprint = 0L;
                        break;
                    case 5:
                        this.pendingArtwork = null;
                        pending = pendingArtwork;
                        break;
                }
                switch (opcode) {
                    case 1:
                        if (pending != null) {
                            sendArtworkChunks(pending);
                            return;
                        }
                        return;
                    case 2:
                        this.log.invoke("NowPlaying 封面被车机取消：id=" + id);
                        return;
                    case 3:
                    case 4:
                    default:
                        return;
                    case 5:
                        this.log.invoke("NowPlaying 封面传输完成：id=" + id);
                        return;
                }
            }
        }
    }

    private final void maybeStartArtwork(Snapshot snapshot) {
        Function1<? super byte[], Boolean> function1;
        Request currentRequest = this.request;
        if (currentRequest == null || !currentRequest.getMedia().contains(26) || (function1 = this.sendFileTransfer) == null) {
            return;
        }
        byte[] bytes = snapshot.getArtwork();
        long fingerprint = snapshot.getArtworkFingerprint();
        synchronized (this.artworkLock) {
            if (fingerprint != this.artworkFingerprint || (fingerprint != 0 && this.artworkTransferId == null)) {
                if (fingerprint == 0) {
                    this.pendingArtwork = null;
                    this.artworkTransferId = null;
                    this.artworkFingerprint = 0L;
                    return;
                }
                int iCoerceIn = RangesKt.coerceIn(this.nextArtworkId, 1, WorkQueueKt.MASK);
                this.nextArtworkId = iCoerceIn >= 127 ? 1 : iCoerceIn + 1;
                byte[] bArr = new byte[12];
                bArr[0] = (byte) iCoerceIn;
                bArr[1] = 4;
                long length = bytes != null ? bytes.length : 0L;
                int i = 0;
                while (i < 8) {
                    byte[] bArr2 = bArr;
                    bArr2[i + 2] = (byte) (length >>> ((7 - i) * 8));
                    i++;
                    bArr = bArr2;
                }
                byte[] bArr3 = bArr;
                bArr3[10] = 0;
                bArr3[11] = 2;
                this.pendingArtwork = new PendingArtwork(iCoerceIn, fingerprint, bytes == null ? new byte[0] : bytes);
                this.artworkTransferId = Integer.valueOf(iCoerceIn);
                this.artworkFingerprint = fingerprint;
                if (!function1.invoke(bArr3).booleanValue()) {
                    this.pendingArtwork = null;
                    this.artworkTransferId = null;
                    this.artworkFingerprint = 0L;
                    this.log.invoke("NowPlaying 封面 SETUP 未能排队：" + (bytes != null ? bytes.length : 0) + "B");
                    return;
                }
                this.log.invoke("NowPlaying 封面 SETUP：id=" + iCoerceIn + " size=" + (bytes != null ? bytes.length : 0) + "B");
                Unit unit = Unit.INSTANCE;
            }
        }
    }

    private final void sendArtworkChunks(PendingArtwork artwork) {
        Function1<? super byte[], Boolean> function1 = this.sendFileTransfer;
        if (function1 == null) {
            return;
        }
        int offset = 0;
        while (offset < artwork.getBytes().length) {
            int count = Math.min(1024, artwork.getBytes().length - offset);
            boolean last = offset + count >= artwork.getBytes().length;
            byte[] packet = new byte[count + 2];
            packet[0] = (byte) artwork.getId();
            packet[1] = (byte) ((last ? 64 : 0) | (offset == 0 ? 128 : 0) | 0);
            ArraysKt.copyInto(artwork.getBytes(), packet, 2, offset, offset + count);
            if (!function1.invoke(packet).booleanValue()) {
                this.log.invoke("NowPlaying 封面 DATA 排队失败：id=" + artwork.getId() + " offset=" + offset);
                return;
            }
            offset += count;
        }
        this.log.invoke("NowPlaying 封面 DATA 已发送：id=" + artwork.getId() + " bytes=" + artwork.getBytes().length);
    }

    private final byte[] artworkBytes(MediaMetadata metadata) {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        Bitmap bitmap;
        try {
            Result.Companion companion = Result.INSTANCE;
            NowPlayingPublisher nowPlayingPublisher = this;
            if (metadata == null || (bitmap = metadata.getBitmap("android.media.metadata.ART")) == null) {
                bitmap = metadata != null ? metadata.getBitmap("android.media.metadata.ALBUM_ART") : null;
            }
            objM159constructorimpl = Result.m159constructorimpl(bitmap);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = null;
        }
        Bitmap bitmap2 = (Bitmap) objM159constructorimpl;
        if (bitmap2 == null) {
            bitmap2 = artworkFromUri(metadata);
        }
        if (bitmap2 == null) {
            return null;
        }
        try {
            Result.Companion companion3 = Result.INSTANCE;
            NowPlayingPublisher nowPlayingPublisher2 = this;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                ByteArrayOutputStream byteArrayOutputStream2 = byteArrayOutputStream;
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 86, byteArrayOutputStream2);
                byte[] byteArray = byteArrayOutputStream2.toByteArray();
                CloseableKt.closeFinally(byteArrayOutputStream, null);
                Intrinsics.checkNotNull(byteArray);
                boolean z = true;
                if ((byteArray.length == 0) || byteArray.length > 2097152) {
                    z = false;
                }
                if (!z) {
                    byteArray = null;
                }
                objM159constructorimpl2 = Result.m159constructorimpl(byteArray);
            } catch (Throwable th2) {
                try {
                    throw th2;
                } catch (Throwable th3) {
                    CloseableKt.closeFinally(byteArrayOutputStream, th2);
                    throw th3;
                }
            }
        } catch (Throwable th4) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th4));
        }
        return (byte[]) (Result.m165isFailureimpl(objM159constructorimpl2) ? null : objM159constructorimpl2);
    }

    private final Bitmap artworkFromUri(MediaMetadata metadata) {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        Bitmap bitmapDecodeStream;
        String string;
        try {
            Result.Companion companion = Result.INSTANCE;
            NowPlayingPublisher nowPlayingPublisher = this;
            if (metadata == null || (string = metadata.getString("android.media.metadata.ALBUM_ART_URI")) == null) {
                string = metadata != null ? metadata.getString("android.media.metadata.ART_URI") : null;
            }
            objM159constructorimpl = Result.m159constructorimpl(string);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = null;
        }
        String value = (String) objM159constructorimpl;
        if (value == null) {
            value = "";
        }
        if (StringsKt.isBlank(value)) {
            return null;
        }
        try {
            Result.Companion companion3 = Result.INSTANCE;
            InputStream inputStreamOpenInputStream = this.appContext.getContentResolver().openInputStream(Uri.parse(value));
            if (inputStreamOpenInputStream == null) {
                bitmapDecodeStream = null;
            } else {
                InputStream inputStream = inputStreamOpenInputStream;
                try {
                    bitmapDecodeStream = BitmapFactory.decodeStream(inputStream);
                    CloseableKt.closeFinally(inputStream, null);
                } catch (Throwable th2) {
                    try {
                        throw th2;
                    } catch (Throwable th3) {
                        CloseableKt.closeFinally(inputStream, th2);
                        throw th3;
                    }
                }
            }
            objM159constructorimpl2 = Result.m159constructorimpl(bitmapDecodeStream);
        } catch (Throwable th4) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th4));
        }
        return (Bitmap) (Result.m165isFailureimpl(objM159constructorimpl2) ? null : objM159constructorimpl2);
    }

    private final MediaController chooseController(List<MediaController> active) {
        Object obj;
        Object next;
        Collection arrayList = new ArrayList();
        for (Object obj2 : active) {
            PlaybackState playbackState = ((MediaController) obj2).getPlaybackState();
            boolean z = false;
            if (playbackState != null && playbackState.getState() == 3) {
                z = true;
            }
            if (z) {
                arrayList.add(obj2);
            }
        }
        List playing = (List) arrayList;
        Iterator it = playing.iterator();
        do {
            obj = null;
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
        } while (!hasUsableMetadata(((MediaController) next).getMetadata()));
        MediaController mediaController = (MediaController) next;
        if (mediaController != null) {
            return mediaController;
        }
        for (Object obj3 : active) {
            if (hasUsableMetadata(((MediaController) obj3).getMetadata())) {
                obj = obj3;
                break;
            }
        }
        MediaController mediaController2 = (MediaController) obj;
        if (mediaController2 != null) {
            return mediaController2;
        }
        MediaController mediaController3 = (MediaController) CollectionsKt.firstOrNull(playing);
        return mediaController3 == null ? (MediaController) CollectionsKt.firstOrNull((List) active) : mediaController3;
    }

    private final boolean hasUsableMetadata(MediaMetadata metadata) {
        boolean text;
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        if (metadata == null) {
            return false;
        }
        Iterable iterableListOf = CollectionsKt.listOf((Object[]) new String[]{"android.media.metadata.TITLE", "android.media.metadata.DISPLAY_TITLE", "android.media.metadata.ARTIST", "android.media.metadata.ALBUM", "android.media.metadata.DISPLAY_SUBTITLE"});
        if (!(iterableListOf instanceof Collection) || !((Collection) iterableListOf).isEmpty()) {
            Iterator it = iterableListOf.iterator();
            while (true) {
                if (!it.hasNext()) {
                    text = false;
                    break;
                }
                String str = (String) it.next();
                try {
                    Result.Companion companion = Result.INSTANCE;
                    NowPlayingPublisher nowPlayingPublisher = this;
                    String string = metadata.getString(str);
                    if (string == null) {
                        string = "";
                    }
                    objM159constructorimpl = Result.m159constructorimpl(Boolean.valueOf(!StringsKt.isBlank(string)));
                } catch (Throwable th) {
                    Result.Companion companion2 = Result.INSTANCE;
                    objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
                }
                if (Result.m165isFailureimpl(objM159constructorimpl)) {
                    objM159constructorimpl = false;
                }
                if (((Boolean) objM159constructorimpl).booleanValue()) {
                    text = true;
                    break;
                }
            }
        } else {
            text = false;
        }
        try {
            Result.Companion companion3 = Result.INSTANCE;
            NowPlayingPublisher nowPlayingPublisher2 = this;
            Bitmap bitmap = metadata.getBitmap("android.media.metadata.ART");
            if (bitmap == null && (bitmap = metadata.getBitmap("android.media.metadata.ALBUM_ART")) == null) {
                bitmap = nowPlayingPublisher2.artworkFromUri(metadata);
            }
            objM159constructorimpl2 = Result.m159constructorimpl(bitmap);
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl2)) {
            objM159constructorimpl2 = null;
        }
        Bitmap artwork = (Bitmap) objM159constructorimpl2;
        return (!text && artwork == null && StringsKt.isBlank(readLyrics(metadata, null))) ? false : true;
    }

    private final String firstMetadataString(final MediaMetadata metadata, String... keys) {
        Object next;
        Iterator it = SequencesKt.mapNotNull(ArraysKt.asSequence(keys), new Function1() { // from class: com.carlink.cast.NowPlayingPublisher$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return NowPlayingPublisher.firstMetadataString$lambda$44(this.f$0, metadata, (String) obj);
            }
        }).iterator();
        do {
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
        } while (StringsKt.isBlank((String) next));
        String str = (String) next;
        return str == null ? "" : str;
    }

    static final String firstMetadataString$lambda$44(NowPlayingPublisher this$0, MediaMetadata $metadata, String key) {
        Object objM159constructorimpl;
        Intrinsics.checkNotNullParameter(key, "key");
        try {
            Result.Companion companion = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl($metadata != null ? $metadata.getString(key) : null);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        return (String) (Result.m165isFailureimpl(objM159constructorimpl) ? null : objM159constructorimpl);
    }

    private final String readLyrics(final MediaMetadata metadata, final PlaybackState playback) {
        Object obj;
        Object next;
        String[] keys = {METADATA_KEY_LYRICS, "android.media.metadata.LYRIC", "ucar.media.metadata.LYRICS_WHOLE", "com.tencent.qqmusic.metadata.LYRICS"};
        Iterator it = SequencesKt.mapNotNull(ArraysKt.asSequence(keys), new Function1() { // from class: com.carlink.cast.NowPlayingPublisher$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj2) {
                return NowPlayingPublisher.readLyrics$lambda$47(this.f$0, metadata, (String) obj2);
            }
        }).iterator();
        do {
            obj = null;
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
        } while (StringsKt.isBlank((String) next));
        String fromMetadata = (String) next;
        String str = fromMetadata;
        if (!(str == null || StringsKt.isBlank(str))) {
            return fromMetadata;
        }
        for (Object obj2 : SequencesKt.mapNotNull(ArraysKt.asSequence(keys), new Function1() { // from class: com.carlink.cast.NowPlayingPublisher$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj3) {
                return NowPlayingPublisher.readLyrics$lambda$51(this.f$0, playback, (String) obj3);
            }
        })) {
            if (!StringsKt.isBlank((String) obj2)) {
                obj = obj2;
                break;
            }
        }
        String str2 = (String) obj;
        return str2 == null ? "" : str2;
    }

    static final String readLyrics$lambda$47(NowPlayingPublisher this$0, MediaMetadata $metadata, String key) {
        Object objM159constructorimpl;
        Intrinsics.checkNotNullParameter(key, "key");
        try {
            Result.Companion companion = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl($metadata != null ? $metadata.getString(key) : null);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        return (String) (Result.m165isFailureimpl(objM159constructorimpl) ? null : objM159constructorimpl);
    }

    static final String readLyrics$lambda$51(NowPlayingPublisher this$0, PlaybackState $playback, String key) {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        Bundle extras;
        Bundle extras2;
        CharSequence charSequence;
        Intrinsics.checkNotNullParameter(key, "key");
        try {
            Result.Companion companion = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(($playback == null || (extras2 = $playback.getExtras()) == null || (charSequence = extras2.getCharSequence(key)) == null) ? null : charSequence.toString());
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = null;
        }
        String str = (String) objM159constructorimpl;
        if (str != null) {
            return str;
        }
        try {
            Result.Companion companion3 = Result.INSTANCE;
            objM159constructorimpl2 = Result.m159constructorimpl(($playback == null || (extras = $playback.getExtras()) == null) ? null : extras.getString(key));
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        return (String) (Result.m165isFailureimpl(objM159constructorimpl2) ? null : objM159constructorimpl2);
    }

    private final long fingerprint(byte[] bytes) {
        long hash = FNV_OFFSET;
        for (byte b : bytes) {
            hash = ((((long) b) & 255) ^ hash) * FNV_PRIME;
        }
        if (hash == 0) {
            return 1L;
        }
        return hash;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
        this.request = null;
        this.send = null;
        this.sendFileTransfer = null;
        this.subscriptionToken = 0L;
        synchronized (this.artworkLock) {
            this.pendingArtwork = null;
            this.artworkTransferId = null;
            Unit unit = Unit.INSTANCE;
        }
        ScheduledFuture<?> scheduledFuture = this.periodic;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
        this.executor.shutdownNow();
    }

    /* JADX INFO: compiled from: NowPlayingPublisher.kt */
    @Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\t\n\u0002\b\u0002\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0013X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lcom/carlink/cast/NowPlayingPublisher$Companion;", "", "<init>", "()V", "METADATA_KEY_LYRICS", "", "ARTWORK_NONE", "", "ARTWORK_FILE_TYPE", "OP_DATA", "OP_START", "OP_CANCEL", "OP_SETUP", "OP_SUCCESS", "FLAG_FIRST", "FLAG_LAST", "ARTWORK_CHUNK_BYTES", "MAX_ARTWORK_BYTES", "FNV_OFFSET", "", "FNV_PRIME", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
