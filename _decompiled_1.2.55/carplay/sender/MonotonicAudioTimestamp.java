package com.carplay.sender;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: SenderNegotiation.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0000\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0007J\u0006\u0010\f\u001a\u00020\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\b¨\u0006\u000f"}, d2 = {"Lcom/carplay/sender/MonotonicAudioTimestamp;", "", "format", "Lcom/carplay/sender/SenderAudioFormat;", "<init>", "(Lcom/carplay/sender/SenderAudioFormat;)V", "nextTimestamp", "", "Ljava/lang/Long;", "next", "", "ptsUs", "reset", "", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MonotonicAudioTimestamp {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final long RTP_MASK = 4294967295L;
    private final SenderAudioFormat format;
    private Long nextTimestamp;

    public MonotonicAudioTimestamp(SenderAudioFormat format) {
        Intrinsics.checkNotNullParameter(format, "format");
        this.format = format;
    }

    public final int next(long ptsUs) {
        Long l = this.nextTimestamp;
        long current = l != null ? l.longValue() : ((long) this.format.rtpTimestamp(ptsUs)) & RTP_MASK;
        this.nextTimestamp = Long.valueOf((((long) this.format.getSamplesPerPacket()) + current) & RTP_MASK);
        return (int) current;
    }

    public final void reset() {
        this.nextTimestamp = null;
    }

    /* JADX INFO: compiled from: SenderNegotiation.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lcom/carplay/sender/MonotonicAudioTimestamp$Companion;", "", "<init>", "()V", "RTP_MASK", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
