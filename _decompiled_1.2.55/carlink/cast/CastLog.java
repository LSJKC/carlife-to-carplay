package com.carlink.cast;

import android.content.Context;
import android.util.AtomicFile;
import androidx.core.app.NotificationCompat;
import com.carplay.airplay.AirPlayDisplaySettings;
import com.google.android.material.card.MaterialCardViewHelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.UByte;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.collections.SetsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;
import org.bouncycastle.i18n.ErrorBundle;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: compiled from: CastLog.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000\u0088\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u0012\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u000f\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020(J\u000e\u0010)\u001a\u00020\u00162\u0006\u0010*\u001a\u00020\u0016J\u0018\u0010+\u001a\u00020&2\u0006\u0010+\u001a\u00020,2\b\b\u0002\u0010-\u001a\u00020\u0016J\u001e\u0010.\u001a\u00020&2\u0006\u0010/\u001a\u00020\u00072\u0006\u00100\u001a\u00020\u00072\u0006\u00101\u001a\u00020\u0007J\u0016\u00102\u001a\u00020&2\u0006\u00103\u001a\u00020\u00162\u0006\u00104\u001a\u00020\u0016J\u0018\u00105\u001a\u00020&2\u0006\u00106\u001a\u00020\u00072\b\b\u0002\u00107\u001a\u00020\u0018J\u0006\u00108\u001a\u00020&J\u0016\u00109\u001a\u00020&2\u0006\u00106\u001a\u00020:2\u0006\u0010;\u001a\u00020\u0018J\u0006\u0010<\u001a\u00020&J\u0012\u0010=\u001a\u00020&2\n\b\u0002\u0010>\u001a\u0004\u0018\u00010\u0016J\u0016\u0010?\u001a\u00020&2\u0006\u0010@\u001a\u00020A2\u0006\u0010>\u001a\u00020BJ\u0018\u0010C\u001a\u00020&2\u0006\u0010D\u001a\u00020E2\b\u0010F\u001a\u0004\u0018\u00010:J\f\u0010G\u001a\b\u0012\u0004\u0012\u00020I0HJ\u0012\u0010J\u001a\u0004\u0018\u00010\u00132\b\b\u0002\u0010K\u001a\u00020\u0016J-\u0010L\u001a\u00020E2\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010M\u001a\u00020\u00052\u0006\u0010N\u001a\u00020\u00052\u0006\u0010O\u001a\u00020\u0018H\u0000¢\u0006\u0002\bPJ\u0015\u0010Q\u001a\u00020\r2\u0006\u0010R\u001a\u00020EH\u0000¢\u0006\u0002\bSJ\b\u0010T\u001a\u00020&H\u0002J\u0012\u0010U\u001a\u0004\u0018\u00010\r2\u0006\u0010V\u001a\u00020\u0016H\u0002J\u0018\u0010W\u001a\u00020&2\u0006\u00102\u001a\u00020X2\u0006\u0010Y\u001a\u00020\u0018H\u0002J\u0010\u0010Z\u001a\u00020&2\u0006\u0010[\u001a\u00020\u0013H\u0002J\u001e\u0010\\\u001a\b\u0012\u0004\u0012\u00020\u00130H2\u0006\u0010V\u001a\u00020\u00162\u0006\u0010]\u001a\u00020\u0013H\u0002J \u0010^\u001a\u00020&2\u0006\u0010[\u001a\u00020\u00132\u0006\u00106\u001a\u00020:2\u0006\u0010Y\u001a\u00020\u0018H\u0002J\u0010\u0010_\u001a\u00020&2\u0006\u0010`\u001a\u00020\u0005H\u0002J\b\u0010a\u001a\u00020&H\u0002J\u0012\u0010b\u001a\u0004\u0018\u00010E2\u0006\u0010V\u001a\u00020\u0016H\u0002J\u001a\u0010c\u001a\u0004\u0018\u00010E2\u0006\u0010V\u001a\u00020\u00162\u0006\u0010]\u001a\u00020\u0013H\u0002J\b\u0010d\u001a\u00020&H\u0002J\u0018\u0010e\u001a\n f*\u0004\u0018\u00010E0E2\u0006\u00102\u001a\u00020XH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006g"}, d2 = {"Lcom/carlink/cast/CastLog;", "", "<init>", "()V", "JOURNAL_LIMIT_BYTES", "", "JOURNAL_MAX_SEGMENTS", "", "CHECKPOINT_INTERVAL_MS", "SYNC_INTERVAL_MS", "RETAIN_SESSIONS", "mutable", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/carlink/cast/CastState;", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "directory", "Ljava/io/File;", "journal", "activeSessionId", "", "initialized", "", "startedAt", "lastCheckpointAt", "lastDurableSyncAt", "journalTruncated", "sampleAt", "sampleFrames", "sampleBytes", "sourceAudioBytes", "sourceAudioFrames", "sourceAudioPeak", "sourceAudioUnits", "sourceAudioPcmNonZeroBytes", "initialize", "", "context", "Landroid/content/Context;", "begin", "source", "stage", "Lcom/carlink/cast/CastStage;", "detail", "display", "width", "height", "fps", NotificationCompat.CATEGORY_EVENT, "level", "message", "video", "bytes", "dropped", CastSettings.KEY_AUDIO, "sourceAudio", "", "pcm", "input", "stop", "failure", "recordCrash", "thread", "Ljava/lang/Thread;", "", "recordProcessExit", ErrorBundle.DETAIL_ENTRY, "Lorg/json/JSONObject;", "trace", "sessions", "", "Lcom/carlink/cast/CastSessionRecord;", "export", "sessionId", "snapshotJson", "startedAtMillis", "savedAtMillis", "truncated", "snapshotJson$app", "stateFromSnapshot", "snapshot", "stateFromSnapshot$app", "restoreLatestSession", "restoreLegacySession", "id", "appendJournal", "Lcom/carlink/cast/CastEvent;", "forceSync", "rotateJournal", "file", "journalFiles", "dir", "writeJournalBytes", "checkpointIfDue", "now", "persistSnapshot", "readSnapshot", "legacySummary", "pruneOldSessions", "json", "kotlin.jvm.PlatformType", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CastLog {
    private static final long CHECKPOINT_INTERVAL_MS = 5000;
    private static final long JOURNAL_LIMIT_BYTES = 2097152;
    private static final int JOURNAL_MAX_SEGMENTS = 8;
    private static final int RETAIN_SESSIONS = 20;
    private static final long SYNC_INTERVAL_MS = 3000;
    private static volatile File directory;
    private static boolean initialized;
    private static volatile File journal;
    private static boolean journalTruncated;
    private static long lastCheckpointAt;
    private static long lastDurableSyncAt;
    private static long sampleAt;
    private static long sampleBytes;
    private static long sampleFrames;
    private static long sourceAudioBytes;
    private static long sourceAudioFrames;
    private static long sourceAudioPcmNonZeroBytes;
    private static int sourceAudioPeak;
    private static long sourceAudioUnits;
    private static long startedAt;
    public static final CastLog INSTANCE = new CastLog();
    private static final MutableStateFlow<CastState> mutable = StateFlowKt.MutableStateFlow(new CastState(null, null, null, false, null, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, null, null, 131071, null));
    private static final StateFlow<CastState> state = FlowKt.asStateFlow(mutable);
    private static volatile String activeSessionId = "";

    private CastLog() {
    }

    public final StateFlow<CastState> getState() {
        return state;
    }

    public final synchronized void initialize(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        if (initialized) {
            return;
        }
        File file = new File(context.getFilesDir(), "sessions");
        file.mkdirs();
        directory = file;
        initialized = true;
        restoreLatestSession();
    }

    public final synchronized String begin(String source) {
        String id;
        Intrinsics.checkNotNullParameter(source, "source");
        id = UUID.randomUUID().toString();
        Intrinsics.checkNotNullExpressionValue(id, "toString(...)");
        startedAt = System.currentTimeMillis();
        sampleAt = startedAt;
        lastCheckpointAt = 0L;
        lastDurableSyncAt = 0L;
        sampleFrames = 0L;
        sampleBytes = 0L;
        sourceAudioBytes = 0L;
        sourceAudioFrames = 0L;
        sourceAudioPeak = 0;
        sourceAudioUnits = 0L;
        sourceAudioPcmNonZeroBytes = 0L;
        journalTruncated = false;
        activeSessionId = id;
        File file = directory;
        journal = file != null ? new File(file, id + ".jsonl") : null;
        mutable.setValue(new CastState(id, CastStage.BLUETOOTH, CastStage.BLUETOOTH.getDetail(), true, null, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0, 0L, source, null, 98288, null));
        event("INFO", "会话开始，画面来源：" + source + "，版本=1.2.55(175)，构建=carlink-cast-1.2.55-175-carlife-media-key-routing");
        persistSnapshot();
        pruneOldSessions();
        return id;
    }

    public static /* synthetic */ void stage$default(CastLog castLog, CastStage castStage, String str, int i, Object obj) {
        if ((i & 2) != 0) {
            str = castStage.getDetail();
        }
        castLog.stage(castStage, str);
    }

    public final synchronized void stage(CastStage stage, String detail) {
        Intrinsics.checkNotNullParameter(stage, "stage");
        Intrinsics.checkNotNullParameter(detail, "detail");
        CastState current = mutable.getValue();
        if (current.getStage() == stage && Intrinsics.areEqual(current.getDetail(), detail)) {
            return;
        }
        mutable.setValue(CastState.copy$default(current, null, stage, detail, !SetsKt.setOf((Object[]) new CastStage[]{CastStage.STOPPED, CastStage.FAILED}).contains(stage), null, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0, 0L, null, null, 131057, null));
        event(stage == CastStage.FAILED ? "ERROR" : "INFO", detail);
        persistSnapshot();
    }

    public final synchronized void display(int width, int height, int fps) {
        String value = width + "×" + height + " · " + fps + "fps";
        if (Intrinsics.areEqual(mutable.getValue().getDisplay(), value)) {
            return;
        }
        mutable.setValue(CastState.copy$default(mutable.getValue(), null, null, null, false, null, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0, 0L, null, value, 65535, null));
        event("INFO", "协商画布：" + value);
        persistSnapshot();
    }

    public final synchronized void event(String level, String message) {
        Intrinsics.checkNotNullParameter(level, "level");
        Intrinsics.checkNotNullParameter(message, "message");
        long now = System.currentTimeMillis();
        CastEvent event = new CastEvent(RangesKt.coerceAtLeast(now - startedAt, 0L), level, StringsKt.take(message, AirPlayDisplaySettings.MAX_REPORTED_PHYSICAL_MM));
        CastState current = mutable.getValue();
        mutable.setValue(CastState.copy$default(current, null, null, null, false, CollectionsKt.takeLast(CollectionsKt.plus((Collection<? extends CastEvent>) current.getEvents(), event), MaterialCardViewHelper.DEFAULT_FADE_ANIM_DURATION), null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0, 0L, null, null, 131055, null));
        appendJournal(event, Intrinsics.areEqual(level, "ERROR") || Intrinsics.areEqual(level, "FATAL") || Intrinsics.areEqual(level, "WARN"));
        if (Intrinsics.areEqual(level, "ERROR") || Intrinsics.areEqual(level, "FATAL") || now - lastCheckpointAt >= 5000 || !mutable.getValue().getRunning()) {
            persistSnapshot();
        }
    }

    public static /* synthetic */ void video$default(CastLog castLog, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        castLog.video(i, z);
    }

    public final synchronized void video(int bytes, boolean dropped) {
        CastState current = mutable.getValue();
        mutable.setValue(dropped ? CastState.copy$default(current, null, null, null, false, null, null, 0L, 0L, current.getDropped() + 1, 0L, 0L, 0L, 0L, 0, 0L, null, null, 130815, null) : CastState.copy$default(current, null, null, null, false, null, null, current.getFrames() + 1, current.getBytes() + ((long) bytes), 0L, 0L, 0L, 0L, 0L, 0, 0L, null, null, 130879, null));
        long now = System.currentTimeMillis();
        if (now - sampleAt >= 1000) {
            double seconds = (now - sampleAt) / 1000.0d;
            CastState latest = mutable.getValue();
            CastMetric sample = new CastMetric(RangesKt.coerceAtLeast(now - startedAt, 0L), (latest.getFrames() - sampleFrames) / seconds, (((latest.getBytes() - sampleBytes) * 8) / seconds) / 1000.0d);
            mutable.setValue(CastState.copy$default(latest, null, null, null, false, null, CollectionsKt.takeLast(CollectionsKt.plus((Collection<? extends CastMetric>) latest.getMetrics(), sample), 90), 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0, 0L, null, null, 131039, null));
            sampleAt = now;
            sampleFrames = latest.getFrames();
            sampleBytes = latest.getBytes();
        }
        checkpointIfDue(now);
    }

    public final synchronized void audio() {
        long packets = mutable.getValue().getAudioPackets() + 1;
        mutable.setValue(CastState.copy$default(mutable.getValue(), null, null, null, false, null, null, 0L, 0L, 0L, packets, 0L, 0L, 0L, 0, 0L, null, null, 130559, null));
        if (packets == 1 || packets % 200 == 0) {
            event("INFO", "AirPlay RTP 音频已发 " + packets + " 包");
        }
    }

    public final synchronized void sourceAudio(byte[] bytes, boolean pcm) {
        String str;
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        sourceAudioBytes += (long) bytes.length;
        if (!pcm) {
            sourceAudioUnits++;
        }
        if (pcm) {
            for (int index = 0; index + 1 < bytes.length; index += 2) {
                int raw = (bytes[index] & UByte.MAX_VALUE) | (bytes[index + 1] << 8);
                int sample = Math.abs((int) ((short) raw));
                if (sample > sourceAudioPeak) {
                    sourceAudioPeak = sample;
                }
                if (bytes[index] != 0 || bytes[index + 1] != 0) {
                    sourceAudioPcmNonZeroBytes += 2;
                }
            }
            sourceAudioFrames += (long) (bytes.length / 2);
        }
        mutable.setValue(CastState.copy$default(mutable.getValue(), null, null, null, false, null, null, 0L, 0L, 0L, 0L, sourceAudioBytes, sourceAudioUnits, sourceAudioPcmNonZeroBytes, sourceAudioPeak, 0L, null, null, 115711, null));
        if (sourceAudioBytes == bytes.length || sourceAudioBytes / 16384 != (sourceAudioBytes - ((long) bytes.length)) / 16384) {
            if (pcm) {
                str = "来源音频：" + sourceAudioBytes + "B，" + sourceAudioFrames + "采样，非零=" + sourceAudioPcmNonZeroBytes + "B，峰值=" + sourceAudioPeak + "，编码输入=PCM";
            } else {
                str = "来源音频：" + sourceAudioBytes + "B，" + sourceAudioUnits + "个AAC access unit，编码输入=AAC";
            }
            event("INFO", str);
        } else {
            checkpointIfDue(System.currentTimeMillis());
        }
    }

    public final synchronized void input() {
        mutable.setValue(CastState.copy$default(mutable.getValue(), null, null, null, false, null, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0, mutable.getValue().getInputEvents() + 1, null, null, 114687, null));
        checkpointIfDue(System.currentTimeMillis());
    }

    public static /* synthetic */ void stop$default(CastLog castLog, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            str = null;
        }
        castLog.stop(str);
    }

    public final synchronized void stop(String failure) {
        CastState castStateCopy$default;
        CastState current = mutable.getValue();
        if (StringsKt.isBlank(current.getSessionId())) {
            return;
        }
        MutableStateFlow<CastState> mutableStateFlow = mutable;
        if (failure != null) {
            castStateCopy$default = CastState.copy$default(current, null, CastStage.FAILED, failure, false, null, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0, 0L, null, null, 131057, null);
        } else {
            castStateCopy$default = CastState.copy$default(current, null, CastStage.STOPPED, CastStage.STOPPED.getDetail(), false, null, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0, 0L, null, null, 131057, null);
        }
        mutableStateFlow.setValue(castStateCopy$default);
        event(failure != null ? "ERROR" : "INFO", failure == null ? "会话结束" : failure);
        persistSnapshot();
    }

    /* JADX WARN: Code duplicated, block: B:75:0x028f A[Catch: all -> 0x029c, TryCatch #13 {all -> 0x029c, blocks: (B:73:0x027e, B:75:0x028f, B:76:0x0292), top: B:104:0x027e }] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v50, types: [com.carlink.cast.CastLog] */
    /* JADX WARN: Type inference failed for: r13v1 */
    /* JADX WARN: Type inference failed for: r13v11, types: [java.io.FileDescriptor] */
    /* JADX WARN: Type inference failed for: r13v13 */
    /* JADX WARN: Type inference failed for: r13v14, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r13v15 */
    /* JADX WARN: Type inference failed for: r13v16 */
    /* JADX WARN: Type inference failed for: r13v17 */
    /* JADX WARN: Type inference failed for: r13v2 */
    /* JADX WARN: Type inference failed for: r13v3, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r13v4 */
    /* JADX WARN: Type inference failed for: r13v5 */
    public final void recordCrash(Thread thread, Throwable failure) {
        long j;
        ?? fd;
        ?? r13;
        CastLog castLog;
        Throwable th;
        Intrinsics.checkNotNullParameter(thread, "thread");
        Intrinsics.checkNotNullParameter(failure, "failure");
        long jCurrentTimeMillis = System.currentTimeMillis();
        String str = activeSessionId;
        if (StringsKt.isBlank(str)) {
            str = "crash-" + jCurrentTimeMillis;
        }
        String str2 = str;
        File file = directory;
        if (file == null) {
            return;
        }
        File file2 = new File(file, str2 + ".crash.txt");
        String name = failure.getClass().getName();
        String message = failure.getMessage();
        if (message == null) {
            message = "(无消息)";
        }
        String str3 = "未捕获异常：" + name + ": " + message + "，线程=" + thread.getName();
        try {
            try {
                Result.Companion companion = Result.INSTANCE;
                CastLog castLog2 = this;
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                try {
                    PrintWriter printWriter2 = printWriter;
                    printWriter2.println("timestamp=" + jCurrentTimeMillis);
                    j = jCurrentTimeMillis;
                    try {
                        printWriter2.println("thread=" + thread.getName());
                        failure.printStackTrace(printWriter2);
                        Unit unit = Unit.INSTANCE;
                        CloseableKt.closeFinally(printWriter, null);
                        String string = stringWriter.toString();
                        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                        FileOutputStream fileOutputStream = new FileOutputStream(file2, false);
                        try {
                            FileOutputStream fileOutputStream2 = fileOutputStream;
                            byte[] bytes = string.getBytes(Charsets.UTF_8);
                            Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
                            fileOutputStream2.write(bytes);
                            fileOutputStream2.getFD().sync();
                            Unit unit2 = Unit.INSTANCE;
                            CloseableKt.closeFinally(fileOutputStream, null);
                            Result.m159constructorimpl(Unit.INSTANCE);
                        } catch (Throwable th2) {
                            try {
                                throw th2;
                            } catch (Throwable th3) {
                                CloseableKt.closeFinally(fileOutputStream, th2);
                                throw th3;
                            }
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        try {
                            throw th;
                        } catch (Throwable th5) {
                            CloseableKt.closeFinally(printWriter, th);
                            throw th5;
                        }
                    }
                } catch (Throwable th6) {
                    j = jCurrentTimeMillis;
                    th = th6;
                }
            } catch (Throwable th7) {
                th = th7;
                Result.Companion companion2 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th));
            }
        } catch (Throwable th8) {
            th = th8;
            j = jCurrentTimeMillis;
            Result.Companion companion3 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (StringsKt.isBlank(activeSessionId)) {
            try {
                Result.Companion companion4 = Result.INSTANCE;
                CastLog castLog3 = this;
                CastEvent castEvent = new CastEvent(0L, "FATAL", str3);
                fd = ".jsonl";
                FileOutputStream fileOutputStream3 = new FileOutputStream(new File(file, str2 + ".jsonl"), false);
                try {
                    try {
                        FileOutputStream fileOutputStream4 = fileOutputStream3;
                        byte[] bytes2 = (castLog3.json(castEvent) + "\n").getBytes(Charsets.UTF_8);
                        Intrinsics.checkNotNullExpressionValue(bytes2, "getBytes(...)");
                        fileOutputStream4.write(bytes2);
                        fd = fileOutputStream4.getFD();
                        fd.sync();
                        Unit unit3 = Unit.INSTANCE;
                        CloseableKt.closeFinally(fileOutputStream3, null);
                        CastState castState = new CastState(str2, CastStage.FAILED, str3, false, CollectionsKt.listOf(castEvent), null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0, 0L, null, null, 131048, null);
                        fd = str3;
                        try {
                            AtomicFile atomicFile = new AtomicFile(new File(file, str2 + ".snapshot.json"));
                            FileOutputStream fileOutputStreamStartWrite = atomicFile.startWrite();
                            try {
                                try {
                                    String string2 = castLog3.snapshotJson$app(castState, j, j, false).toString();
                                    Intrinsics.checkNotNullExpressionValue(string2, "toString(...)");
                                    byte[] bytes3 = string2.getBytes(Charsets.UTF_8);
                                    Intrinsics.checkNotNullExpressionValue(bytes3, "getBytes(...)");
                                    fileOutputStreamStartWrite.write(bytes3);
                                    fileOutputStreamStartWrite.getFD().sync();
                                    atomicFile.finishWrite(fileOutputStreamStartWrite);
                                    Result.m159constructorimpl(Unit.INSTANCE);
                                    r13 = fd;
                                } catch (Throwable th9) {
                                    th = th9;
                                    atomicFile.failWrite(fileOutputStreamStartWrite);
                                    throw th;
                                }
                            } catch (Throwable th10) {
                                th = th10;
                            }
                        } catch (Throwable th11) {
                            th = th11;
                            Result.Companion companion5 = Result.INSTANCE;
                            Result.m159constructorimpl(ResultKt.createFailure(th));
                            r13 = fd;
                        }
                    } catch (Throwable th12) {
                        th = th12;
                        Result.Companion companion6 = Result.INSTANCE;
                        Result.m159constructorimpl(ResultKt.createFailure(th));
                        r13 = fd;
                        Result.Companion companion7 = Result.INSTANCE;
                        castLog = this;
                        if (!StringsKt.isBlank(activeSessionId)) {
                            castLog.event("FATAL", r13);
                        }
                        castLog.persistSnapshot();
                        Result.m159constructorimpl(Unit.INSTANCE);
                    }
                } catch (Throwable th13) {
                    fd = str3;
                    try {
                        throw th13;
                    } catch (Throwable th14) {
                        CloseableKt.closeFinally(fileOutputStream3, th13);
                        throw th14;
                    }
                }
            } catch (Throwable th15) {
                th = th15;
                fd = str3;
            }
        } else {
            r13 = str3;
        }
        try {
            Result.Companion companion8 = Result.INSTANCE;
            castLog = this;
            if (!StringsKt.isBlank(activeSessionId)) {
                castLog.event("FATAL", r13);
            }
            castLog.persistSnapshot();
            Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th16) {
            Result.Companion companion9 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th16));
        }
    }

    /* JADX WARN: Code duplicated, block: B:55:0x014a  */
    public final synchronized void recordProcessExit(JSONObject details, byte[] trace) {
        String str;
        Intrinsics.checkNotNullParameter(details, "details");
        File dir = directory;
        if (dir == null) {
            return;
        }
        long timestamp = details.optLong("timestamp", System.currentTimeMillis());
        boolean hasSession = !StringsKt.isBlank(mutable.getValue().getSessionId());
        String sessionId = mutable.getValue().getSessionId();
        if (StringsKt.isBlank(sessionId)) {
            sessionId = "process-" + timestamp;
        }
        String id = sessionId;
        String base = id + ".exit-" + timestamp;
        try {
            Result.Companion companion = Result.INSTANCE;
            CastLog castLog = this;
            FileOutputStream fileOutputStream = new FileOutputStream(new File(dir, base + ".json"), false);
            try {
                FileOutputStream fileOutputStream2 = fileOutputStream;
                String string = details.toString();
                Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                byte[] bytes = string.getBytes(Charsets.UTF_8);
                Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
                fileOutputStream2.write(bytes);
                fileOutputStream2.getFD().sync();
                Unit unit = Unit.INSTANCE;
                try {
                    CloseableKt.closeFinally(fileOutputStream, null);
                    Result.m159constructorimpl(Unit.INSTANCE);
                } catch (Throwable th) {
                    th = th;
                    Result.Companion companion2 = Result.INSTANCE;
                    Result.m159constructorimpl(ResultKt.createFailure(th));
                }
            } catch (Throwable th2) {
                try {
                    throw th2;
                } catch (Throwable th3) {
                    CloseableKt.closeFinally(fileOutputStream, th2);
                    throw th3;
                }
            }
        } catch (Throwable th4) {
            th = th4;
        }
        if (trace == null) {
            str = null;
        } else {
            if (trace.length == 0) {
                str = null;
            } else {
                try {
                    Result.Companion companion3 = Result.INSTANCE;
                    CastLog castLog2 = this;
                    str = null;
                    FileOutputStream fileOutputStream3 = new FileOutputStream(new File(dir, base + ".trace"), false);
                    try {
                        try {
                            FileOutputStream fileOutputStream4 = fileOutputStream3;
                            str = null;
                            fileOutputStream4.write(trace);
                            fileOutputStream4.getFD().sync();
                            Unit unit2 = Unit.INSTANCE;
                            str = null;
                            CloseableKt.closeFinally(fileOutputStream3, null);
                            Result.m159constructorimpl(Unit.INSTANCE);
                        } catch (Throwable th5) {
                            str = null;
                            try {
                                throw th5;
                            } catch (Throwable th6) {
                                CloseableKt.closeFinally(fileOutputStream3, th5);
                                throw th6;
                            }
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        Result.Companion companion4 = Result.INSTANCE;
                        Result.m159constructorimpl(ResultKt.createFailure(th));
                    }
                } catch (Throwable th8) {
                    th = th8;
                    str = null;
                }
            }
        }
        String strOptString = details.optString("reasonLabel", details.optString("reason"));
        String strOptString2 = details.optString("description");
        Intrinsics.checkNotNull(strOptString2);
        if (StringsKt.isBlank(strOptString2)) {
            strOptString2 = str;
        }
        String str2 = strOptString2 != null ? "；" + strOptString2 : str;
        if (str2 == null) {
            str2 = "";
        }
        String message = "Android 记录到上次进程退出：" + strOptString + str2;
        if (hasSession) {
            event("ERROR", message);
        } else {
            CastEvent diagnosticEvent = new CastEvent(0L, "ERROR", message);
            activeSessionId = id;
            startedAt = timestamp;
            journal = new File(dir, id + ".jsonl");
            mutable.setValue(new CastState(id, CastStage.FAILED, message, false, CollectionsKt.listOf(diagnosticEvent), null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0, 0L, null, null, 131048, null));
            appendJournal(diagnosticEvent, true);
        }
        persistSnapshot();
    }

    /* JADX WARN: Code duplicated, block: B:20:0x008a  */
    /* JADX WARN: Code duplicated, block: B:22:0x0098  */
    /* JADX WARN: Code duplicated, block: B:73:0x01f2  */
    /* JADX WARN: Code duplicated, block: B:77:0x0201  */
    /* JADX WARN: Code duplicated, block: B:78:0x0204  */
    public final List<CastSessionRecord> sessions() {
        String strOptString;
        File dir;
        Iterable iterable;
        boolean z;
        boolean z2;
        int i;
        Iterable iterable2;
        boolean z3;
        boolean z4;
        CastSessionRecord castSessionRecord;
        boolean z5;
        boolean z6;
        File[] fileArr;
        String name;
        File dir2 = directory;
        if (dir2 == null) {
            return CollectionsKt.emptyList();
        }
        File[] fileArrListFiles = dir2.listFiles();
        if (fileArrListFiles == null) {
            fileArrListFiles = new File[0];
        }
        int i2 = 0;
        Collection arrayList = new ArrayList();
        File[] fileArr2 = fileArrListFiles;
        int length = fileArr2.length;
        int i3 = 0;
        while (true) {
            String strRemoveSuffix = null;
            if (i3 >= length) {
                break;
            }
            File file = fileArr2[i3];
            String name2 = file.getName();
            Intrinsics.checkNotNullExpressionValue(name2, "getName(...)");
            File[] fileArr3 = fileArrListFiles;
            int i4 = i2;
            if (StringsKt.endsWith$default(name2, ".snapshot.json", false, 2, (Object) null)) {
                String name3 = file.getName();
                Intrinsics.checkNotNullExpressionValue(name3, "getName(...)");
                strRemoveSuffix = StringsKt.removeSuffix(name3, (CharSequence) ".snapshot.json");
                fileArr = fileArr2;
            } else {
                String name4 = file.getName();
                Intrinsics.checkNotNullExpressionValue(name4, "getName(...)");
                fileArr = fileArr2;
                if (StringsKt.contains$default((CharSequence) name4, (CharSequence) ".part-", false, 2, (Object) null)) {
                    String name5 = file.getName();
                    Intrinsics.checkNotNullExpressionValue(name5, "getName(...)");
                    if (StringsKt.endsWith$default(name5, ".jsonl", false, 2, (Object) null)) {
                        String name6 = file.getName();
                        Intrinsics.checkNotNullExpressionValue(name6, "getName(...)");
                        strRemoveSuffix = StringsKt.substringBefore$default(name6, ".part-", (String) null, 2, (Object) null);
                    } else {
                        name = file.getName();
                        Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
                        if (StringsKt.endsWith$default(name, ".jsonl", false, 2, (Object) null)) {
                            String name7 = file.getName();
                            Intrinsics.checkNotNullExpressionValue(name7, "getName(...)");
                            strRemoveSuffix = StringsKt.removeSuffix(name7, (CharSequence) ".jsonl");
                        }
                    }
                } else {
                    name = file.getName();
                    Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
                    if (StringsKt.endsWith$default(name, ".jsonl", false, 2, (Object) null)) {
                        String name8 = file.getName();
                        Intrinsics.checkNotNullExpressionValue(name8, "getName(...)");
                        strRemoveSuffix = StringsKt.removeSuffix(name8, (CharSequence) ".jsonl");
                    }
                }
            }
            if (strRemoveSuffix != null) {
                arrayList.add(strRemoveSuffix);
            }
            i3++;
            fileArrListFiles = fileArr3;
            i2 = i4;
            fileArr2 = fileArr;
        }
        List ids = CollectionsKt.distinct((List) arrayList);
        List list = ids;
        int i5 = 0;
        Collection arrayList2 = new ArrayList();
        Iterable<String> iterable3 = list;
        for (String str : iterable3) {
            JSONObject snapshot = INSTANCE.readSnapshot(str);
            List ids2 = ids;
            Iterable iterable4 = list;
            File file2 = new File(dir2, str + ".jsonl");
            if (snapshot == null && INSTANCE.journalFiles(str, dir2).isEmpty()) {
                dir = dir2;
                iterable = iterable3;
                i = 2;
                iterable2 = null;
                castSessionRecord = null;
            } else {
                long jOptLong = snapshot != null ? snapshot.optLong("startedAtMillis") : file2.lastModified();
                if (snapshot == null || (strOptString = snapshot.optString("stage")) == null) {
                    strOptString = "旧版日志";
                }
                String str2 = strOptString;
                String strOptString2 = snapshot != null ? snapshot.optString("source") : null;
                if (strOptString2 == null) {
                    strOptString2 = "";
                }
                String str3 = strOptString2;
                long jOptLong2 = snapshot != null ? snapshot.optLong("frames") : 0L;
                if (new File(dir2, str + ".crash.txt").isFile()) {
                    dir = dir2;
                    iterable = iterable3;
                    z = true;
                    z2 = false;
                    i = 2;
                    iterable2 = null;
                } else {
                    File[] fileArrListFiles2 = dir2.listFiles();
                    if (fileArrListFiles2 != null) {
                        int i6 = 0;
                        int length2 = fileArrListFiles2.length;
                        dir = dir2;
                        int i7 = 0;
                        while (true) {
                            if (i7 >= length2) {
                                iterable = iterable3;
                                z2 = false;
                                i = 2;
                                iterable2 = null;
                                z6 = false;
                                break;
                            }
                            int i8 = i7;
                            String name9 = fileArrListFiles2[i7].getName();
                            Intrinsics.checkNotNullExpressionValue(name9, "getName(...)");
                            File[] fileArr4 = fileArrListFiles2;
                            int i9 = length2;
                            int i10 = i6;
                            iterable = iterable3;
                            z2 = false;
                            i = 2;
                            iterable2 = null;
                            if (StringsKt.startsWith$default(name9, str + ".exit-", false, 2, (Object) null)) {
                                z6 = true;
                                break;
                            }
                            i7 = i8 + 1;
                            i6 = i10;
                            iterable3 = iterable;
                            fileArrListFiles2 = fileArr4;
                            length2 = i9;
                        }
                        z = true;
                        if (z6) {
                            z5 = true;
                        }
                        if (z5) {
                            z3 = z2;
                        }
                        if (snapshot == null) {
                            z4 = z;
                        } else {
                            z4 = z2;
                        }
                        castSessionRecord = new CastSessionRecord(str, jOptLong, str2, str3, jOptLong2, z3, z4);
                    } else {
                        dir = dir2;
                        iterable = iterable3;
                        z = true;
                        z2 = false;
                        i = 2;
                        iterable2 = null;
                    }
                    z5 = z2;
                    if (z5) {
                        z3 = z2;
                    }
                    if (snapshot == null) {
                        z4 = z;
                    } else {
                        z4 = z2;
                    }
                    castSessionRecord = new CastSessionRecord(str, jOptLong, str2, str3, jOptLong2, z3, z4);
                }
                z3 = z;
                if (snapshot == null) {
                    z4 = z;
                } else {
                    z4 = z2;
                }
                castSessionRecord = new CastSessionRecord(str, jOptLong, str2, str3, jOptLong2, z3, z4);
            }
            if (castSessionRecord != null) {
                arrayList2.add(castSessionRecord);
            }
            iterable3 = iterable;
            ids = ids2;
            list = iterable4;
            i5 = i5;
            dir2 = dir;
        }
        return CollectionsKt.sortedWith((List) arrayList2, new Comparator() { // from class: com.carlink.cast.CastLog$sessions$$inlined$sortedByDescending$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                return ComparisonsKt.compareValues(Long.valueOf(((CastSessionRecord) t2).getStartedAtMillis()), Long.valueOf(((CastSessionRecord) t).getStartedAtMillis()));
            }
        });
    }

    public static /* synthetic */ File export$default(CastLog castLog, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            str = mutable.getValue().getSessionId();
        }
        return castLog.export(str);
    }

    /* JADX WARN: Code duplicated, block: B:139:0x0325 A[Catch: all -> 0x03f0, TryCatch #10 {all -> 0x03f0, blocks: (B:129:0x02d7, B:131:0x02ee, B:133:0x0302, B:139:0x0325, B:140:0x0328, B:141:0x0336, B:143:0x0357, B:144:0x035f, B:146:0x0365, B:150:0x03ca, B:158:0x03e5, B:159:0x03e8, B:160:0x03e9, B:156:0x03e3), top: B:205:0x02d7, inners: #2 }] */
    /* JADX WARN: Code duplicated, block: B:234:0x0328 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:71:0x017c A[Catch: all -> 0x0235, TryCatch #13 {all -> 0x0235, blocks: (B:63:0x0157, B:65:0x015d, B:71:0x017c, B:73:0x0184, B:74:0x018e, B:76:0x01ab, B:77:0x01b4, B:79:0x01ba, B:81:0x01c2, B:82:0x01c5, B:86:0x0214, B:94:0x022c, B:95:0x022f, B:109:0x026c, B:111:0x028e, B:117:0x029a, B:118:0x029d, B:92:0x022a, B:110:0x027e, B:115:0x0298), top: B:210:0x0157, inners: #7, #14, #17 }] */
    /* JADX WARN: Code duplicated, block: B:72:0x0182  */
    public final synchronized File export(String sessionId) {
        Throwable th;
        File target;
        Throwable th2;
        File[] fileArr;
        int i;
        File dir;
        File[] fileArr2;
        int i2;
        boolean z;
        Throwable th3;
        File[] fileArr3;
        int i3;
        File target2;
        boolean z2;
        Throwable th4;
        String sessionId2 = sessionId;
        synchronized (this) {
            Intrinsics.checkNotNullParameter(sessionId2, "sessionId");
            File file = directory;
            if (file == null) {
                return null;
            }
            File dir2 = file;
            if (StringsKt.isBlank(sessionId2)) {
                return null;
            }
            if (Intrinsics.areEqual(sessionId2, mutable.getValue().getSessionId())) {
                persistSnapshot();
            }
            JSONObject snapshot = readSnapshot(sessionId);
            if (snapshot == null && (snapshot = legacySummary(sessionId2, dir2)) == null) {
                return null;
            }
            JSONObject snapshot2 = snapshot;
            File target3 = new File(dir2, sessionId2 + ".zip");
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(target3, false));
            try {
                ZipOutputStream zipOutputStream2 = zipOutputStream;
                zipOutputStream2.putNextEntry(new ZipEntry("session.json"));
                String string = snapshot2.toString();
                Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                byte[] bytes = string.getBytes(Charsets.UTF_8);
                Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
                zipOutputStream2.write(bytes);
                zipOutputStream2.closeEntry();
                File file2 = new File(dir2, sessionId2 + ".jsonl");
                if (!file2.isFile()) {
                    file2 = null;
                }
                if (file2 != null) {
                    File file3 = file2;
                    try {
                        zipOutputStream2.putNextEntry(new ZipEntry("events.jsonl"));
                        FileInputStream fileInputStream = new FileInputStream(file3);
                        try {
                            try {
                                ByteStreamsKt.copyTo$default(fileInputStream, zipOutputStream2, 0, 2, null);
                                try {
                                    CloseableKt.closeFinally(fileInputStream, null);
                                    zipOutputStream2.closeEntry();
                                } catch (Throwable th5) {
                                    th = th5;
                                    try {
                                        throw th;
                                    } catch (Throwable th6) {
                                        CloseableKt.closeFinally(zipOutputStream, th);
                                        throw th6;
                                    }
                                }
                            } catch (Throwable th7) {
                                th4 = th7;
                                try {
                                    throw th4;
                                } catch (Throwable th8) {
                                    try {
                                        CloseableKt.closeFinally(fileInputStream, th4);
                                        throw th8;
                                    } catch (Throwable th9) {
                                        th = th9;
                                        throw th;
                                    }
                                }
                            }
                        } catch (Throwable th10) {
                            th4 = th10;
                        }
                    } catch (Throwable th11) {
                        th = th11;
                    }
                }
                try {
                    File[] fileArrListFiles = dir2.listFiles();
                    if (fileArrListFiles != null) {
                        int i4 = 0;
                        try {
                            Collection arrayList = new ArrayList();
                            int length = fileArrListFiles.length;
                            int i5 = 0;
                            while (i5 < length) {
                                File file4 = fileArrListFiles[i5];
                                if (file4.isFile()) {
                                    String name = file4.getName();
                                    Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
                                    fileArr3 = fileArrListFiles;
                                    i3 = i4;
                                    target2 = target3;
                                    try {
                                        if (StringsKt.startsWith$default(name, sessionId2 + ".part-", false, 2, (Object) null)) {
                                            String name2 = file4.getName();
                                            Intrinsics.checkNotNullExpressionValue(name2, "getName(...)");
                                            z2 = StringsKt.endsWith$default(name2, ".jsonl", false, 2, (Object) null);
                                        }
                                        if (z2) {
                                            arrayList.add(file4);
                                        }
                                        i5++;
                                        fileArrListFiles = fileArr3;
                                        i4 = i3;
                                        target3 = target2;
                                    } catch (Throwable th12) {
                                        th = th12;
                                        throw th;
                                    }
                                } else {
                                    fileArr3 = fileArrListFiles;
                                    i3 = i4;
                                    target2 = target3;
                                }
                                if (z2) {
                                    arrayList.add(file4);
                                }
                                i5++;
                                fileArrListFiles = fileArr3;
                                i4 = i3;
                                target3 = target2;
                            }
                            target = target3;
                            Iterable iterableSortedWith = CollectionsKt.sortedWith((List) arrayList, new Comparator() { // from class: com.carlink.cast.CastLog$export$lambda$35$$inlined$sortedBy$1
                                /* JADX WARN: Multi-variable type inference failed */
                                @Override // java.util.Comparator
                                public final int compare(T t, T t2) {
                                    return ComparisonsKt.compareValues(((File) t).getName(), ((File) t2).getName());
                                }
                            });
                            if (iterableSortedWith != null) {
                                Iterable iterable = iterableSortedWith;
                                int i6 = 0;
                                int i7 = 0;
                                Iterator it = iterable.iterator();
                                while (it.hasNext()) {
                                    Object next = it.next();
                                    int i8 = i7 + 1;
                                    if (i7 < 0) {
                                        CollectionsKt.throwIndexOverflow();
                                    }
                                    File file5 = (File) next;
                                    int i9 = i6;
                                    Iterable iterable2 = iterable;
                                    zipOutputStream2.putNextEntry(new ZipEntry("events/part-" + (i7 + 1) + ".jsonl"));
                                    Intrinsics.checkNotNull(file5);
                                    FileInputStream fileInputStream2 = new FileInputStream(file5);
                                    try {
                                        Iterator it2 = it;
                                        try {
                                            ByteStreamsKt.copyTo$default(fileInputStream2, zipOutputStream2, 0, 2, null);
                                            CloseableKt.closeFinally(fileInputStream2, null);
                                            zipOutputStream2.closeEntry();
                                            i7 = i8;
                                            i6 = i9;
                                            iterable = iterable2;
                                            it = it2;
                                        } catch (Throwable th13) {
                                            th3 = th13;
                                            try {
                                                throw th3;
                                            } catch (Throwable th14) {
                                                CloseableKt.closeFinally(fileInputStream2, th3);
                                                throw th14;
                                            }
                                        }
                                    } catch (Throwable th15) {
                                        th3 = th15;
                                    }
                                }
                            }
                        } catch (Throwable th16) {
                            th = th16;
                            throw th;
                        }
                    } else {
                        target = target3;
                    }
                    try {
                        File file6 = new File(dir2, sessionId2 + ".crash.txt");
                        if (!file6.isFile()) {
                            file6 = null;
                        }
                        if (file6 != null) {
                            File file7 = file6;
                            zipOutputStream2.putNextEntry(new ZipEntry("crash.txt"));
                            FileInputStream fileInputStream3 = new FileInputStream(file7);
                            try {
                                ByteStreamsKt.copyTo$default(fileInputStream3, zipOutputStream2, 0, 2, null);
                                CloseableKt.closeFinally(fileInputStream3, null);
                                zipOutputStream2.closeEntry();
                            } catch (Throwable th17) {
                                try {
                                    throw th17;
                                } catch (Throwable th18) {
                                    CloseableKt.closeFinally(fileInputStream3, th17);
                                    throw th18;
                                }
                            }
                        }
                        File[] fileArrListFiles2 = dir2.listFiles();
                        if (fileArrListFiles2 != null) {
                            int i10 = 0;
                            try {
                                Collection arrayList2 = new ArrayList();
                                File[] fileArr4 = fileArrListFiles2;
                                int i11 = 0;
                                int length2 = fileArr4.length;
                                int i12 = 0;
                                while (i12 < length2) {
                                    File file8 = fileArr4[i12];
                                    if (file8.isFile()) {
                                        fileArr = fileArrListFiles2;
                                        String name3 = file8.getName();
                                        i = i10;
                                        Intrinsics.checkNotNullExpressionValue(name3, "getName(...)");
                                        dir = dir2;
                                        try {
                                            fileArr2 = fileArr4;
                                            i2 = i11;
                                            if (StringsKt.startsWith$default(name3, sessionId2 + ".exit-", false, 2, (Object) null)) {
                                                String name4 = file8.getName();
                                                Intrinsics.checkNotNullExpressionValue(name4, "getName(...)");
                                                if (!StringsKt.endsWith$default(name4, ".json", false, 2, (Object) null)) {
                                                    String name5 = file8.getName();
                                                    Intrinsics.checkNotNullExpressionValue(name5, "getName(...)");
                                                    if (StringsKt.endsWith$default(name5, ".trace", false, 2, (Object) null)) {
                                                    }
                                                }
                                                z = true;
                                            }
                                            if (z) {
                                                arrayList2.add(file8);
                                            }
                                            i12++;
                                            fileArrListFiles2 = fileArr;
                                            i10 = i;
                                            dir2 = dir;
                                            fileArr4 = fileArr2;
                                            i11 = i2;
                                        } catch (Throwable th19) {
                                            th = th19;
                                            throw th;
                                        }
                                    } else {
                                        fileArr = fileArrListFiles2;
                                        i = i10;
                                        dir = dir2;
                                        fileArr2 = fileArr4;
                                        i2 = i11;
                                    }
                                    z = false;
                                    if (z) {
                                        arrayList2.add(file8);
                                    }
                                    i12++;
                                    fileArrListFiles2 = fileArr;
                                    i10 = i;
                                    dir2 = dir;
                                    fileArr4 = fileArr2;
                                    i11 = i2;
                                }
                                Iterable iterableSortedWith2 = CollectionsKt.sortedWith((List) arrayList2, new Comparator() { // from class: com.carlink.cast.CastLog$export$lambda$35$$inlined$sortedBy$2
                                    /* JADX WARN: Multi-variable type inference failed */
                                    @Override // java.util.Comparator
                                    public final int compare(T t, T t2) {
                                        return ComparisonsKt.compareValues(((File) t).getName(), ((File) t2).getName());
                                    }
                                });
                                if (iterableSortedWith2 != null) {
                                    Iterable iterable3 = iterableSortedWith2;
                                    int i13 = 0;
                                    Iterator it3 = iterable3.iterator();
                                    while (it3.hasNext()) {
                                        File file9 = (File) it3.next();
                                        String name6 = file9.getName();
                                        Intrinsics.checkNotNullExpressionValue(name6, "getName(...)");
                                        zipOutputStream2.putNextEntry(new ZipEntry("process-exit/" + StringsKt.removePrefix(name6, (CharSequence) (sessionId2 + "."))));
                                        Intrinsics.checkNotNull(file9);
                                        FileInputStream fileInputStream4 = new FileInputStream(file9);
                                        try {
                                            Iterator it4 = it3;
                                            int i14 = i13;
                                            Iterable iterable4 = iterable3;
                                            try {
                                                ByteStreamsKt.copyTo$default(fileInputStream4, zipOutputStream2, 0, 2, null);
                                                CloseableKt.closeFinally(fileInputStream4, null);
                                                zipOutputStream2.closeEntry();
                                                sessionId2 = sessionId;
                                                i13 = i14;
                                                it3 = it4;
                                                iterable3 = iterable4;
                                            } catch (Throwable th20) {
                                                th2 = th20;
                                                try {
                                                    throw th2;
                                                } catch (Throwable th21) {
                                                    CloseableKt.closeFinally(fileInputStream4, th2);
                                                    throw th21;
                                                }
                                            }
                                        } catch (Throwable th22) {
                                            th2 = th22;
                                        }
                                    }
                                    Unit unit = Unit.INSTANCE;
                                }
                            } catch (Throwable th23) {
                                th = th23;
                            }
                        }
                        CloseableKt.closeFinally(zipOutputStream, null);
                        return target;
                    } catch (Throwable th24) {
                        th = th24;
                    }
                } catch (Throwable th25) {
                    th = th25;
                }
            } catch (Throwable th26) {
                th = th26;
            }
        }
    }

    public final synchronized JSONObject snapshotJson$app(CastState state2, long startedAtMillis, long savedAtMillis, boolean truncated) {
        JSONObject jSONObjectPut;
        Intrinsics.checkNotNullParameter(state2, "state");
        JSONObject jSONObjectPut2 = new JSONObject().put("schemaVersion", 2).put("app", "CarLink Cast").put("versionName", BuildConfig.VERSION_NAME).put("versionCode", BuildConfig.VERSION_CODE).put("buildId", BuildConfig.BUILD_ID).put("sessionId", state2.getSessionId()).put("startedAtMillis", startedAtMillis).put("savedAtMillis", savedAtMillis).put("stage", state2.getStage().name()).put("detail", state2.getDetail()).put("running", state2.getRunning()).put("journalTruncated", truncated).put("source", state2.getSource()).put("display", state2.getDisplay()).put("frames", state2.getFrames()).put("bytes", state2.getBytes()).put("dropped", state2.getDropped()).put("audioPackets", state2.getAudioPackets()).put("sourceAudioBytes", state2.getSourceAudioBytes()).put("sourceAudioUnits", state2.getSourceAudioUnits()).put("sourceAudioPcmNonZeroBytes", state2.getSourceAudioPcmNonZeroBytes()).put("sourceAudioPeak", state2.getSourceAudioPeak()).put("inputEvents", state2.getInputEvents());
        JSONArray jSONArray = new JSONArray();
        Iterator it = state2.getEvents().iterator();
        while (it.hasNext()) {
            jSONArray.put(INSTANCE.json((CastEvent) it.next()));
        }
        Unit unit = Unit.INSTANCE;
        JSONObject jSONObjectPut3 = jSONObjectPut2.put("events", jSONArray);
        JSONArray jSONArray2 = new JSONArray();
        for (CastMetric castMetric : state2.getMetrics()) {
            jSONArray2.put(new JSONObject().put("elapsedMs", castMetric.getElapsedMillis()).put("fps", castMetric.getFps()).put("kbps", castMetric.getKbps()));
        }
        Unit unit2 = Unit.INSTANCE;
        jSONObjectPut = jSONObjectPut3.put("metrics", jSONArray2);
        Intrinsics.checkNotNullExpressionValue(jSONObjectPut, "put(...)");
        return jSONObjectPut;
    }

    public final synchronized CastState stateFromSnapshot$app(JSONObject snapshot) {
        Object objM159constructorimpl;
        CastStage castStage;
        String strOptString;
        boolean zOptBoolean;
        Iterable iterableUntil;
        Collection arrayList;
        Iterator<Integer> it;
        String str;
        CastStage castStage2;
        String str2;
        List listTakeLast;
        List listTakeLast2;
        long jOptLong;
        long jOptLong2;
        long jOptLong3;
        long jOptLong4;
        long jOptLong5;
        long jOptLong6;
        long jOptLong7;
        int iOptInt;
        long jOptLong8;
        String strOptString2;
        String strOptString3;
        Intrinsics.checkNotNullParameter(snapshot, "snapshot");
        JSONArray jSONArrayOptJSONArray = snapshot.optJSONArray("events");
        if (jSONArrayOptJSONArray == null) {
            jSONArrayOptJSONArray = new JSONArray();
        }
        JSONArray events = jSONArrayOptJSONArray;
        JSONArray jSONArrayOptJSONArray2 = snapshot.optJSONArray("metrics");
        if (jSONArrayOptJSONArray2 == null) {
            jSONArrayOptJSONArray2 = new JSONArray();
        }
        JSONArray metrics = jSONArrayOptJSONArray2;
        String strOptString4 = snapshot.optString("sessionId");
        Intrinsics.checkNotNullExpressionValue(strOptString4, "optString(...)");
        try {
            Result.Companion companion = Result.INSTANCE;
            CastLog castLog = this;
            String strOptString5 = snapshot.optString("stage");
            Intrinsics.checkNotNullExpressionValue(strOptString5, "optString(...)");
            objM159constructorimpl = Result.m159constructorimpl(CastStage.valueOf(strOptString5));
            while (true) {
                CastEvent castEvent = null;
                if (!it.hasNext()) {
                    break;
                }
                JSONObject jSONObjectOptJSONObject = events.optJSONObject(((IntIterator) it).nextInt());
                if (jSONObjectOptJSONObject != null) {
                    long jOptLong9 = jSONObjectOptJSONObject.optLong("elapsedMs");
                    String strOptString6 = jSONObjectOptJSONObject.optString("level");
                    Intrinsics.checkNotNullExpressionValue(strOptString6, "optString(...)");
                    String strOptString7 = jSONObjectOptJSONObject.optString("message");
                    Intrinsics.checkNotNullExpressionValue(strOptString7, "optString(...)");
                    castEvent = new CastEvent(jOptLong9, strOptString6, strOptString7);
                }
                if (castEvent != null) {
                    arrayList.add(castEvent);
                }
                iterableUntil = iterableUntil;
                strOptString4 = strOptString4;
                castStage = castStage;
                strOptString = strOptString;
                events = events;
            }
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        CastStage castStage3 = CastStage.STOPPED;
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = castStage3;
        }
        castStage = (CastStage) objM159constructorimpl;
        strOptString = snapshot.optString("detail", CastStage.STOPPED.getDetail());
        Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
        zOptBoolean = snapshot.optBoolean("running");
        iterableUntil = RangesKt.until(0, events.length());
        arrayList = new ArrayList();
        it = iterableUntil.iterator();
        str = strOptString4;
        castStage2 = castStage;
        str2 = strOptString;
        listTakeLast = CollectionsKt.takeLast((List) arrayList, MaterialCardViewHelper.DEFAULT_FADE_ANIM_DURATION);
        Iterable iterableUntil2 = RangesKt.until(0, metrics.length());
        int i = 0;
        Collection arrayList2 = new ArrayList();
        Iterator<Integer> it2 = iterableUntil2.iterator();
        while (it2.hasNext()) {
            JSONObject jSONObjectOptJSONObject2 = metrics.optJSONObject(((IntIterator) it2).nextInt());
            CastMetric castMetric = jSONObjectOptJSONObject2 != null ? new CastMetric(jSONObjectOptJSONObject2.optLong("elapsedMs"), jSONObjectOptJSONObject2.optDouble("fps"), jSONObjectOptJSONObject2.optDouble("kbps")) : null;
            if (castMetric != null) {
                arrayList2.add(castMetric);
            }
            iterableUntil2 = iterableUntil2;
            i = i;
        }
        listTakeLast2 = CollectionsKt.takeLast((List) arrayList2, 90);
        jOptLong = snapshot.optLong("frames");
        jOptLong2 = snapshot.optLong("bytes");
        jOptLong3 = snapshot.optLong("dropped");
        jOptLong4 = snapshot.optLong("audioPackets");
        jOptLong5 = snapshot.optLong("sourceAudioBytes");
        jOptLong6 = snapshot.optLong("sourceAudioUnits");
        jOptLong7 = snapshot.optLong("sourceAudioPcmNonZeroBytes");
        iOptInt = snapshot.optInt("sourceAudioPeak");
        jOptLong8 = snapshot.optLong("inputEvents");
        strOptString2 = snapshot.optString("source");
        Intrinsics.checkNotNullExpressionValue(strOptString2, "optString(...)");
        strOptString3 = snapshot.optString("display");
        Intrinsics.checkNotNullExpressionValue(strOptString3, "optString(...)");
        return new CastState(str, castStage2, str2, zOptBoolean, listTakeLast, listTakeLast2, jOptLong, jOptLong2, jOptLong3, jOptLong4, jOptLong5, jOptLong6, jOptLong7, iOptInt, jOptLong8, strOptString2, strOptString3);
    }

    /* JADX WARN: Code duplicated, block: B:26:0x007a  */
    /* JADX WARN: Code duplicated, block: B:28:0x008a  */
    private final void restoreLatestSession() {
        CastState castStateRestoreLegacySession;
        long elapsedMillis;
        CastState recovered;
        CastSessionRecord latest = (CastSessionRecord) CollectionsKt.firstOrNull((List) sessions());
        if (latest == null) {
            return;
        }
        String id = latest.getSessionId();
        JSONObject snapshot = readSnapshot(id);
        if ((snapshot == null || (castStateRestoreLegacySession = stateFromSnapshot$app(snapshot)) == null) && (castStateRestoreLegacySession = restoreLegacySession(id)) == null) {
            return;
        }
        CastState loaded = castStateRestoreLegacySession;
        activeSessionId = id;
        File file = new File(directory, id + ".jsonl");
        if (!file.isFile()) {
            file = null;
        }
        journal = file;
        if (snapshot != null) {
            Long lValueOf = Long.valueOf(snapshot.optLong("startedAtMillis"));
            Long l = (lValueOf.longValue() > 0L ? 1 : (lValueOf.longValue() == 0L ? 0 : -1)) > 0 ? lValueOf : null;
            if (l != null) {
                elapsedMillis = l.longValue();
            } else {
                long jCurrentTimeMillis = System.currentTimeMillis();
                CastEvent castEvent = (CastEvent) CollectionsKt.lastOrNull((List) loaded.getEvents());
                elapsedMillis = jCurrentTimeMillis - (castEvent != null ? castEvent.getElapsedMillis() : 0L);
            }
        } else {
            long jCurrentTimeMillis2 = System.currentTimeMillis();
            CastEvent castEvent2 = (CastEvent) CollectionsKt.lastOrNull((List) loaded.getEvents());
            elapsedMillis = jCurrentTimeMillis2 - (castEvent2 != null ? castEvent2.getElapsedMillis() : 0L);
        }
        startedAt = elapsedMillis;
        if (loaded.getRunning()) {
            recovered = CastState.copy$default(loaded, null, CastStage.FAILED, "上次连接未正常结束；日志已恢复，可导出诊断", false, null, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0, 0L, null, null, 131057, null);
        } else {
            recovered = loaded;
        }
        mutable.setValue(recovered);
        sampleAt = System.currentTimeMillis();
        sampleFrames = recovered.getFrames();
        sampleBytes = recovered.getBytes();
        sourceAudioBytes = recovered.getSourceAudioBytes();
        sourceAudioUnits = recovered.getSourceAudioUnits();
        sourceAudioPeak = recovered.getSourceAudioPeak();
        sourceAudioPcmNonZeroBytes = recovered.getSourceAudioPcmNonZeroBytes();
        journalTruncated = snapshot != null ? snapshot.optBoolean("journalTruncated") : false;
        if (loaded.getRunning()) {
            event("ERROR", "检测到上一次会话进程中断；可能是崩溃、系统回收或强制停止，具体原因随后从 Android 退出记录补充");
            persistSnapshot();
        }
    }

    private final CastState restoreLegacySession(String id) {
        Object objM159constructorimpl;
        File dir = directory;
        if (dir == null) {
            return null;
        }
        List<File> listJournalFiles = journalFiles(id, dir);
        if (listJournalFiles.isEmpty()) {
            return null;
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            final CastLog castLog = this;
            final List arrayList = new ArrayList();
            Iterator it = listJournalFiles.iterator();
            while (it.hasNext()) {
                FilesKt.forEachLine$default((File) it.next(), null, new Function1() { // from class: com.carlink.cast.CastLog$$ExternalSyntheticLambda1
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return CastLog.restoreLegacySession$lambda$50$lambda$49$lambda$48(this.f$0, arrayList, (String) obj);
                    }
                }, 1, null);
            }
            objM159constructorimpl = Result.m159constructorimpl(CollectionsKt.takeLast(arrayList, MaterialCardViewHelper.DEFAULT_FADE_ANIM_DURATION));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        List listEmptyList = CollectionsKt.emptyList();
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = listEmptyList;
        }
        List events = (List) objM159constructorimpl;
        CastState restored = new CastState(id, CastStage.FAILED, "旧版本会话日志已恢复，可导出", false, events, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0, 0L, null, null, 131048, null);
        activeSessionId = id;
        journal = new File(dir, id + ".jsonl");
        long jCurrentTimeMillis = System.currentTimeMillis();
        CastEvent castEvent = (CastEvent) CollectionsKt.lastOrNull(events);
        startedAt = jCurrentTimeMillis - (castEvent != null ? castEvent.getElapsedMillis() : 0L);
        mutable.setValue(restored);
        event("WARN", "从旧版本 JSONL 恢复日志；旧版未保存视频、音频等会话快照");
        persistSnapshot();
        return mutable.getValue();
    }

    static final Unit restoreLegacySession$lambda$50$lambda$49$lambda$48(CastLog $this_runCatching, List $restored, String line) {
        Intrinsics.checkNotNullParameter(line, "line");
        try {
            Result.Companion companion = Result.INSTANCE;
            JSONObject jSONObject = new JSONObject(line);
            long jOptLong = jSONObject.optLong("elapsedMs");
            String strOptString = jSONObject.optString("level");
            Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
            String strOptString2 = jSONObject.optString("message");
            Intrinsics.checkNotNullExpressionValue(strOptString2, "optString(...)");
            $restored.add(new CastEvent(jOptLong, strOptString, strOptString2));
            Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        return Unit.INSTANCE;
    }

    private final void appendJournal(CastEvent event, boolean forceSync) {
        File file = journal;
        if (file == null) {
            return;
        }
        byte[] bytes = (json(event) + "\n").getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        long currentLength = file.length();
        if (((long) bytes.length) + currentLength > JOURNAL_LIMIT_BYTES) {
            rotateJournal(file);
        }
        writeJournalBytes(file, bytes, forceSync);
    }

    /* JADX WARN: Code duplicated, block: B:19:0x0074  */
    /* JADX WARN: Code duplicated, block: B:46:0x0077 A[SYNTHETIC] */
    private final void rotateJournal(File file) {
        List existing;
        Object objM159constructorimpl;
        File[] fileArr;
        int i;
        File[] fileArr2;
        boolean z;
        String id = activeSessionId;
        File dir = file.getParentFile();
        if (dir == null) {
            return;
        }
        File[] fileArrListFiles = dir.listFiles();
        if (fileArrListFiles != null) {
            int i2 = 0;
            Collection arrayList = new ArrayList();
            File[] fileArr3 = fileArrListFiles;
            int length = fileArr3.length;
            int i3 = 0;
            while (i3 < length) {
                File file2 = fileArr3[i3];
                if (file2.isFile()) {
                    String name = file2.getName();
                    Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
                    fileArr = fileArrListFiles;
                    i = i2;
                    fileArr2 = fileArr3;
                    if (StringsKt.startsWith$default(name, id + ".part-", false, 2, (Object) null)) {
                        String name2 = file2.getName();
                        Intrinsics.checkNotNullExpressionValue(name2, "getName(...)");
                        z = StringsKt.endsWith$default(name2, ".jsonl", false, 2, (Object) null);
                    }
                    if (z) {
                        arrayList.add(file2);
                    }
                    i3++;
                    fileArr3 = fileArr2;
                    fileArrListFiles = fileArr;
                    i2 = i;
                } else {
                    fileArr = fileArrListFiles;
                    i = i2;
                    fileArr2 = fileArr3;
                }
                if (z) {
                    arrayList.add(file2);
                }
                i3++;
                fileArr3 = fileArr2;
                fileArrListFiles = fileArr;
                i2 = i;
            }
            existing = CollectionsKt.sortedWith((List) arrayList, new Comparator() { // from class: com.carlink.cast.CastLog$rotateJournal$$inlined$sortedBy$1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    return ComparisonsKt.compareValues(((File) t).getName(), ((File) t2).getName());
                }
            });
        } else {
            existing = null;
        }
        if (existing == null) {
            existing = CollectionsKt.emptyList();
        }
        if (existing.size() >= 8) {
            ((File) CollectionsKt.first(existing)).delete();
        }
        long suffix = System.currentTimeMillis();
        File rotated = new File(dir, id + ".part-" + suffix + ".jsonl");
        try {
            Result.Companion companion = Result.INSTANCE;
            CastLog castLog = this;
            if (file.isFile() && file.length() > 0) {
                file.renameTo(rotated);
            }
            file.createNewFile();
            journalTruncated = false;
            byte[] bytes = (castLog.json(new CastEvent(RangesKt.coerceAtLeast(System.currentTimeMillis() - startedAt, 0L), "INFO", "详细日志已滚动分片保存，旧分片仍会随会话导出")) + "\n").getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
            castLog.writeJournalBytes(file, bytes, true);
            objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m162exceptionOrNullimpl(objM159constructorimpl) != null) {
            journalTruncated = true;
        }
    }

    /* JADX WARN: Code duplicated, block: B:21:0x0081  */
    /* JADX WARN: Code duplicated, block: B:30:0x0084 A[SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v7 */
    /* JADX WARN: Type inference failed for: r2v8 */
    private final List<File> journalFiles(String id, File dir) {
        List<File> listSortedWith;
        File[] fileArr;
        Object[] objArr;
        ?? r2;
        File[] fileArrListFiles = dir.listFiles();
        if (fileArrListFiles != null) {
            Collection arrayList = new ArrayList();
            int length = fileArrListFiles.length;
            int i = 0;
            while (i < length) {
                File file = fileArrListFiles[i];
                if (file.isFile()) {
                    if (Intrinsics.areEqual(file.getName(), id + ".jsonl")) {
                        fileArr = fileArrListFiles;
                    } else {
                        String name = file.getName();
                        Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
                        fileArr = fileArrListFiles;
                        objArr = null;
                        if (StringsKt.startsWith$default(name, id + ".part-", false, 2, (Object) null)) {
                            String name2 = file.getName();
                            Intrinsics.checkNotNullExpressionValue(name2, "getName(...)");
                            if (StringsKt.endsWith$default(name2, ".jsonl", false, 2, (Object) null)) {
                            }
                            if (r2 != 0) {
                                arrayList.add(file);
                            }
                            i++;
                            fileArrListFiles = fileArr;
                        }
                    }
                    r2 = 1;
                    if (r2 != 0) {
                        arrayList.add(file);
                    }
                    i++;
                    fileArrListFiles = fileArr;
                } else {
                    fileArr = fileArrListFiles;
                    objArr = null;
                }
                r2 = objArr;
                if (r2 != 0) {
                    arrayList.add(file);
                }
                i++;
                fileArrListFiles = fileArr;
            }
            listSortedWith = CollectionsKt.sortedWith((List) arrayList, new Comparator() { // from class: com.carlink.cast.CastLog$journalFiles$$inlined$sortedBy$1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    return ComparisonsKt.compareValues(((File) t).getName(), ((File) t2).getName());
                }
            });
        } else {
            listSortedWith = null;
        }
        return listSortedWith == null ? CollectionsKt.emptyList() : listSortedWith;
    }

    private final void writeJournalBytes(File file, byte[] bytes, boolean forceSync) {
        try {
            Result.Companion companion = Result.INSTANCE;
            CastLog castLog = this;
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                parentFile.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            try {
                FileOutputStream fileOutputStream2 = fileOutputStream;
                fileOutputStream2.write(bytes);
                fileOutputStream2.flush();
                long jCurrentTimeMillis = System.currentTimeMillis();
                if (forceSync || jCurrentTimeMillis - lastDurableSyncAt >= SYNC_INTERVAL_MS) {
                    fileOutputStream2.getFD().sync();
                    lastDurableSyncAt = jCurrentTimeMillis;
                }
                Unit unit = Unit.INSTANCE;
                CloseableKt.closeFinally(fileOutputStream, null);
                Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    CloseableKt.closeFinally(fileOutputStream, th);
                    throw th2;
                }
            }
        } catch (Throwable th3) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th3));
        }
    }

    private final void checkpointIfDue(long now) {
        if (!StringsKt.isBlank(mutable.getValue().getSessionId()) && now - lastCheckpointAt >= 5000) {
            persistSnapshot();
        }
    }

    private final void persistSnapshot() {
        String id = mutable.getValue().getSessionId();
        File dir = directory;
        if (dir == null || StringsKt.isBlank(id)) {
            return;
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            JSONObject jSONObjectSnapshotJson$app = snapshotJson$app(mutable.getValue(), startedAt, System.currentTimeMillis(), journalTruncated);
            AtomicFile atomicFile = new AtomicFile(new File(dir, id + ".snapshot.json"));
            FileOutputStream fileOutputStreamStartWrite = atomicFile.startWrite();
            try {
                String string = jSONObjectSnapshotJson$app.toString();
                Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                byte[] bytes = string.getBytes(Charsets.UTF_8);
                Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
                fileOutputStreamStartWrite.write(bytes);
                fileOutputStreamStartWrite.getFD().sync();
                atomicFile.finishWrite(fileOutputStreamStartWrite);
                lastCheckpointAt = System.currentTimeMillis();
                Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th) {
                atomicFile.failWrite(fileOutputStreamStartWrite);
                throw th;
            }
        } catch (Throwable th2) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
    }

    private final JSONObject readSnapshot(String id) {
        Object objM159constructorimpl;
        File file = directory;
        if (file != null) {
            File file2 = new File(file, id + ".snapshot.json");
            if (!file2.isFile()) {
                file2 = null;
            }
            if (file2 != null) {
                try {
                    Result.Companion companion = Result.INSTANCE;
                    CastLog castLog = this;
                    FileInputStream fileInputStreamOpenRead = new AtomicFile(file2).openRead();
                    Intrinsics.checkNotNullExpressionValue(fileInputStreamOpenRead, "openRead(...)");
                    Reader inputStreamReader = new InputStreamReader(fileInputStreamOpenRead, Charsets.UTF_8);
                    BufferedReader bufferedReader = inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, 8192);
                    try {
                        String text = TextStreamsKt.readText(bufferedReader);
                        CloseableKt.closeFinally(bufferedReader, null);
                        objM159constructorimpl = Result.m159constructorimpl(new JSONObject(text));
                    } catch (Throwable th) {
                        try {
                            throw th;
                        } catch (Throwable th2) {
                            CloseableKt.closeFinally(bufferedReader, th);
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    Result.Companion companion2 = Result.INSTANCE;
                    objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th3));
                }
                return (JSONObject) (Result.m165isFailureimpl(objM159constructorimpl) ? null : objM159constructorimpl);
            }
        }
        return null;
    }

    private final JSONObject legacySummary(String id, File dir) {
        Object objM159constructorimpl;
        List<File> listJournalFiles = journalFiles(id, dir);
        File journalFile = (File) CollectionsKt.lastOrNull((List) listJournalFiles);
        if (journalFile == null) {
            return null;
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            final CastLog castLog = this;
            objM159constructorimpl = Result.m159constructorimpl((JSONObject) SequencesKt.lastOrNull(SequencesKt.flatMap(CollectionsKt.asSequence(listJournalFiles), new Function1() { // from class: com.carlink.cast.CastLog$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return CastLog.legacySummary$lambda$67$lambda$66(this.f$0, (File) obj);
                }
            })));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        JSONObject lastEvent = (JSONObject) (Result.m165isFailureimpl(objM159constructorimpl) ? null : objM159constructorimpl);
        return new JSONObject().put("schemaVersion", 1).put("app", "CarLink Cast").put("sessionId", id).put("startedAtMillis", RangesKt.coerceAtLeast(journalFile.lastModified() - (lastEvent != null ? lastEvent.optLong("elapsedMs") : 0L), 0L)).put("savedAtMillis", journalFile.lastModified()).put("stage", "LEGACY").put("detail", "旧版本日志没有会话状态快照").put("running", false).put("journalTruncated", journalFile.length() >= JOURNAL_LIMIT_BYTES).put("source", "").put("display", "").put("recoveredFromLegacyJournal", true).put("events", new JSONArray()).put("metrics", new JSONArray());
    }

    static final Sequence legacySummary$lambda$67$lambda$66(final CastLog $this_runCatching, File file) {
        Intrinsics.checkNotNullParameter(file, "file");
        return SequencesKt.mapNotNull(CollectionsKt.asSequence(FilesKt.readLines$default(file, null, 1, null)), new Function1() { // from class: com.carlink.cast.CastLog$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return CastLog.legacySummary$lambda$67$lambda$66$lambda$65(this.f$0, (String) obj);
            }
        });
    }

    static final JSONObject legacySummary$lambda$67$lambda$66$lambda$65(CastLog $this_runCatching, String line) {
        Object objM159constructorimpl;
        Intrinsics.checkNotNullParameter(line, "line");
        try {
            Result.Companion companion = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(new JSONObject(line));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = null;
        }
        return (JSONObject) objM159constructorimpl;
    }

    private final void pruneOldSessions() {
        File[] fileArrListFiles;
        File dir;
        Set oldIds;
        File[] fileArr;
        int i;
        File[] fileArr2;
        boolean z;
        File dir2 = directory;
        if (dir2 == null) {
            return;
        }
        Iterable iterableDrop = CollectionsKt.drop(sessions(), 20);
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterableDrop, 10));
        Iterator it = iterableDrop.iterator();
        while (it.hasNext()) {
            arrayList.add(((CastSessionRecord) it.next()).getSessionId());
        }
        Set oldIds2 = CollectionsKt.toSet((List) arrayList);
        if (!oldIds2.isEmpty() && (fileArrListFiles = dir2.listFiles()) != null) {
            int i2 = 0;
            Collection arrayList2 = new ArrayList();
            File[] fileArr3 = fileArrListFiles;
            int length = fileArr3.length;
            int i3 = 0;
            while (i3 < length) {
                File file = fileArr3[i3];
                Iterable iterable = oldIds2;
                if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
                    Iterator it2 = iterable.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            dir = dir2;
                            oldIds = oldIds2;
                            fileArr = fileArrListFiles;
                            i = i2;
                            fileArr2 = fileArr3;
                            z = false;
                            break;
                        }
                        String str = (String) it2.next();
                        dir = dir2;
                        String name = file.getName();
                        oldIds = oldIds2;
                        Intrinsics.checkNotNullExpressionValue(name, "getName(...)");
                        fileArr = fileArrListFiles;
                        i = i2;
                        fileArr2 = fileArr3;
                        if (StringsKt.startsWith$default(name, str + ".", false, 2, (Object) null)) {
                            z = true;
                            break;
                        }
                        dir2 = dir;
                        oldIds2 = oldIds;
                        fileArrListFiles = fileArr;
                        i2 = i;
                        fileArr3 = fileArr2;
                    }
                } else {
                    dir = dir2;
                    oldIds = oldIds2;
                    fileArr = fileArrListFiles;
                    i = i2;
                    fileArr2 = fileArr3;
                    z = false;
                }
                if (z) {
                    arrayList2.add(file);
                }
                i3++;
                dir2 = dir;
                oldIds2 = oldIds;
                fileArrListFiles = fileArr;
                i2 = i;
                fileArr3 = fileArr2;
            }
            Iterator it3 = ((List) arrayList2).iterator();
            while (it3.hasNext()) {
                ((File) it3.next()).delete();
            }
        }
    }

    private final JSONObject json(CastEvent event) {
        return new JSONObject().put("elapsedMs", event.getElapsedMillis()).put("level", event.getLevel()).put("message", event.getMessage());
    }
}
