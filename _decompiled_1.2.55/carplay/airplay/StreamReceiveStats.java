package com.carplay.airplay;

import androidx.core.os.EnvironmentCompat;
import com.carplay.sender.MonotonicAudioTimestamp;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import org.bouncycastle.jcajce.util.AnnotatedPrivateKey;

/* JADX INFO: compiled from: StreamReceiveStats.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b \n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0000\u0018\u0000 42\u00020\u0001:\u00014B3\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b¢\u0006\u0004\b\n\u0010\u000bJ\u0006\u0010&\u001a\u00020\u0006J+\u0010'\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u00102\n\b\u0002\u0010)\u001a\u0004\u0018\u00010\u00102\n\b\u0002\u0010*\u001a\u0004\u0018\u00010\u0010¢\u0006\u0002\u0010+J\u0016\u0010,\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\t2\u0006\u0010(\u001a\u00020\u0010J\u0006\u0010.\u001a\u00020\u0006J\u0010\u0010/\u001a\u00020\u00062\b\b\u0002\u00100\u001a\u000201J\f\u00102\u001a\u00020\t*\u00020\u0010H\u0002J\b\u00103\u001a\u00020\u0003H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0019\u001a\u0004\u0018\u00010\u0010X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u001aR\u0012\u0010\u001b\u001a\u0004\u0018\u00010\u0010X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u001aR\u000e\u0010\u001c\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000¨\u00065"}, d2 = {"Lcom/carplay/airplay/StreamReceiveStats;", "", AnnotatedPrivateKey.LABEL, "", "report", "Lkotlin/Function1;", "", "nowNs", "Lkotlin/Function0;", "", "<init>", "(Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;)V", "windowStart", "readStart", "processingStart", "packets", "", "bytes", "maxReadNs", "readsOver250Ms", "seqGapAfterReadOver250Ms", "seqMissingAfterReadOver250Ms", "lastReceivedNs", "maxInterArrivalNs", "maxProcessNs", "lastTimestamp", "Ljava/lang/Integer;", "nextSequence", "forwardGapPackets", "sequenceGapEvents", "maxSequenceGap", "lastSequenceGap", "lastSequenceGapAtMs", "lateOrDuplicate", "decryptSamples", "decryptBytes", "decryptSumNs", "maxDecryptNs", "reading", "received", "size", "sequence", "timestamp", "(ILjava/lang/Integer;Ljava/lang/Integer;)V", "decrypted", "durationNs", "processed", "flush", "ended", "", "toUnsignedLong", "decryptSummary", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class StreamReceiveStats {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final long LONG_READ_NS = 250000000;
    private long bytes;
    private long decryptBytes;
    private int decryptSamples;
    private long decryptSumNs;
    private int forwardGapPackets;
    private final String label;
    private long lastReceivedNs;
    private String lastSequenceGap;
    private long lastSequenceGapAtMs;
    private Integer lastTimestamp;
    private int lateOrDuplicate;
    private long maxDecryptNs;
    private long maxInterArrivalNs;
    private long maxProcessNs;
    private long maxReadNs;
    private int maxSequenceGap;
    private Integer nextSequence;
    private final Function0<Long> nowNs;
    private int packets;
    private long processingStart;
    private long readStart;
    private int readsOver250Ms;
    private final Function1<String, Unit> report;
    private int seqGapAfterReadOver250Ms;
    private int seqMissingAfterReadOver250Ms;
    private int sequenceGapEvents;
    private long windowStart;

    /* JADX WARN: Multi-variable type inference failed */
    public StreamReceiveStats(String label, Function1<? super String, Unit> report, Function0<Long> nowNs) {
        Intrinsics.checkNotNullParameter(label, "label");
        Intrinsics.checkNotNullParameter(report, "report");
        Intrinsics.checkNotNullParameter(nowNs, "nowNs");
        this.label = label;
        this.report = report;
        this.nowNs = nowNs;
        this.windowStart = this.nowNs.invoke().longValue();
        this.readStart = this.windowStart;
        this.processingStart = this.windowStart;
        this.lastSequenceGap = "none";
        this.lastSequenceGapAtMs = -1L;
    }

    public /* synthetic */ StreamReceiveStats(String str, Function1 function1, AnonymousClass1 anonymousClass1, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, function1, (i & 4) != 0 ? AnonymousClass1.INSTANCE : anonymousClass1);
    }

    /* JADX INFO: renamed from: com.carplay.airplay.StreamReceiveStats$1, reason: invalid class name */
    /* JADX INFO: compiled from: StreamReceiveStats.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function0<Long> {
        public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

        AnonymousClass1() {
            super(0, System.class, "nanoTime", "nanoTime()J", 0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        public final Long invoke() {
            return Long.valueOf(System.nanoTime());
        }
    }

    public final void reading() {
        this.readStart = this.nowNs.invoke().longValue();
    }

    public static /* synthetic */ void received$default(StreamReceiveStats streamReceiveStats, int i, Integer num, Integer num2, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            num = null;
        }
        if ((i2 & 4) != 0) {
            num2 = null;
        }
        streamReceiveStats.received(i, num, num2);
    }

    public final void received(int size, Integer sequence, Integer timestamp) {
        this.processingStart = this.nowNs.invoke().longValue();
        long readNs = this.processingStart - this.readStart;
        this.maxReadNs = Math.max(this.maxReadNs, readNs);
        if (readNs >= LONG_READ_NS) {
            this.readsOver250Ms++;
        }
        if (this.lastReceivedNs != 0) {
            this.maxInterArrivalNs = Math.max(this.maxInterArrivalNs, this.processingStart - this.lastReceivedNs);
        }
        this.lastReceivedNs = this.processingStart;
        this.packets++;
        this.bytes += (long) size;
        if (sequence != null) {
            Integer expected = this.nextSequence;
            int delta = expected == null ? 0 : (sequence.intValue() - expected.intValue()) & 65535;
            if (delta < 32768) {
                this.forwardGapPackets += delta;
                if (delta > 0) {
                    this.sequenceGapEvents++;
                    if (readNs >= LONG_READ_NS) {
                        this.seqGapAfterReadOver250Ms++;
                        this.seqMissingAfterReadOver250Ms += delta;
                    }
                    this.maxSequenceGap = Math.max(this.maxSequenceGap, delta);
                    Integer num = this.lastTimestamp;
                    Object objValueOf = EnvironmentCompat.MEDIA_UNKNOWN;
                    Object objValueOf2 = num != null ? Long.valueOf(toUnsignedLong(num.intValue())) : EnvironmentCompat.MEDIA_UNKNOWN;
                    if (timestamp != null) {
                        objValueOf = Long.valueOf(toUnsignedLong(timestamp.intValue()));
                    }
                    this.lastSequenceGap = "expected=" + expected + " received=" + sequence + " missing=" + delta + " previousRtpTs=" + objValueOf2 + " receivedRtpTs=" + objValueOf;
                    this.lastSequenceGapAtMs = (this.processingStart - this.windowStart) / 1000000;
                }
                this.nextSequence = Integer.valueOf((sequence.intValue() + 1) & 65535);
            } else {
                this.lateOrDuplicate++;
            }
        }
        if (timestamp != null) {
            this.lastTimestamp = timestamp;
        }
    }

    public final void decrypted(long durationNs, int size) {
        this.decryptSamples++;
        this.decryptBytes += (long) size;
        this.decryptSumNs += durationNs;
        this.maxDecryptNs = Math.max(this.maxDecryptNs, durationNs);
    }

    public final void processed() {
        this.maxProcessNs = Math.max(this.maxProcessNs, this.nowNs.invoke().longValue() - this.processingStart);
        flush$default(this, false, 1, null);
    }

    public static /* synthetic */ void flush$default(StreamReceiveStats streamReceiveStats, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        streamReceiveStats.flush(z);
    }

    public final void flush(boolean ended) {
        long now;
        long now2 = this.nowNs.invoke().longValue();
        if (ended || now2 - this.windowStart >= 5000000000L) {
            try {
                Result.Companion companion = Result.INSTANCE;
                StreamReceiveStats $this$flush_u24lambda_u240 = this;
                now = now2;
                try {
                    try {
                        $this$flush_u24lambda_u240.report.invoke("Receive: " + $this$flush_u24lambda_u240.label + " packets=" + $this$flush_u24lambda_u240.packets + " bytes=" + $this$flush_u24lambda_u240.bytes + " readMaxMs=" + ($this$flush_u24lambda_u240.maxReadNs / 1000000) + " processMaxUs=" + ($this$flush_u24lambda_u240.maxProcessNs / 1000) + " seqForwardGaps=" + $this$flush_u24lambda_u240.forwardGapPackets + " lateOrDuplicate=" + $this$flush_u24lambda_u240.lateOrDuplicate + " interArrivalMaxMs=" + ($this$flush_u24lambda_u240.maxInterArrivalNs / 1000000) + " seqGapEvents=" + $this$flush_u24lambda_u240.sequenceGapEvents + " seqGapMax=" + $this$flush_u24lambda_u240.maxSequenceGap + " seqGapLast=[" + $this$flush_u24lambda_u240.lastSequenceGap + "] seqGapAtMs=" + $this$flush_u24lambda_u240.lastSequenceGapAtMs + " ended=" + ended + " windowMs=" + (RangesKt.coerceAtLeast(now - $this$flush_u24lambda_u240.windowStart, 0L) / 1000000) + " readsOver250Ms=" + $this$flush_u24lambda_u240.readsOver250Ms + " seqGapAfterReadOver250Ms=" + $this$flush_u24lambda_u240.seqGapAfterReadOver250Ms + " seqMissingAfterReadOver250Ms=" + $this$flush_u24lambda_u240.seqMissingAfterReadOver250Ms + $this$flush_u24lambda_u240.decryptSummary());
                        Result.m159constructorimpl(Unit.INSTANCE);
                    } catch (Throwable th) {
                        th = th;
                        Result.Companion companion2 = Result.INSTANCE;
                        Result.m159constructorimpl(ResultKt.createFailure(th));
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Throwable th3) {
                th = th3;
                now = now2;
            }
            this.windowStart = now;
            this.decryptSamples = 0;
            this.decryptBytes = 0L;
            this.decryptSumNs = 0L;
            this.maxDecryptNs = 0L;
            this.packets = 0;
            this.bytes = 0L;
            this.maxReadNs = 0L;
            this.readsOver250Ms = 0;
            this.seqGapAfterReadOver250Ms = 0;
            this.seqMissingAfterReadOver250Ms = 0;
            this.maxInterArrivalNs = 0L;
            this.maxProcessNs = 0L;
            this.forwardGapPackets = 0;
            this.sequenceGapEvents = 0;
            this.maxSequenceGap = 0;
            this.lastSequenceGap = "none";
            this.lastSequenceGapAtMs = -1L;
            this.lateOrDuplicate = 0;
        }
    }

    private final long toUnsignedLong(int $this$toUnsignedLong) {
        return ((long) $this$toUnsignedLong) & MonotonicAudioTimestamp.RTP_MASK;
    }

    private final String decryptSummary() {
        if (this.decryptSamples == 0) {
            return "";
        }
        long mbPerSecond = this.decryptSumNs != 0 ? (this.decryptBytes * 1000) / this.decryptSumNs : 0L;
        return " decryptAvgUs=" + ((this.decryptSumNs / ((long) this.decryptSamples)) / 1000) + " decryptMaxUs=" + (this.maxDecryptNs / 1000) + " decryptMBps=" + mbPerSecond;
    }

    /* JADX INFO: compiled from: StreamReceiveStats.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lcom/carplay/airplay/StreamReceiveStats$Companion;", "", "<init>", "()V", "LONG_READ_NS", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
