package com.carlink.cast;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CastLog.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b3\b\u0086\b\u0018\u00002\u00020\u0001B½\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u0012\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\n\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0017\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u001a\u001a\u00020\u0003¢\u0006\u0004\b\u001b\u0010\u001cJ\t\u00104\u001a\u00020\u0003HÆ\u0003J\t\u00105\u001a\u00020\u0005HÆ\u0003J\t\u00106\u001a\u00020\u0003HÆ\u0003J\t\u00107\u001a\u00020\bHÆ\u0003J\u000f\u00108\u001a\b\u0012\u0004\u0012\u00020\u000b0\nHÆ\u0003J\u000f\u00109\u001a\b\u0012\u0004\u0012\u00020\r0\nHÆ\u0003J\t\u0010:\u001a\u00020\u000fHÆ\u0003J\t\u0010;\u001a\u00020\u000fHÆ\u0003J\t\u0010<\u001a\u00020\u000fHÆ\u0003J\t\u0010=\u001a\u00020\u000fHÆ\u0003J\t\u0010>\u001a\u00020\u000fHÆ\u0003J\t\u0010?\u001a\u00020\u000fHÆ\u0003J\t\u0010@\u001a\u00020\u000fHÆ\u0003J\t\u0010A\u001a\u00020\u0017HÆ\u0003J\t\u0010B\u001a\u00020\u000fHÆ\u0003J\t\u0010C\u001a\u00020\u0003HÆ\u0003J\t\u0010D\u001a\u00020\u0003HÆ\u0003J¿\u0001\u0010E\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\n2\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u000f2\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\b\b\u0002\u0010\u0012\u001a\u00020\u000f2\b\b\u0002\u0010\u0013\u001a\u00020\u000f2\b\b\u0002\u0010\u0014\u001a\u00020\u000f2\b\b\u0002\u0010\u0015\u001a\u00020\u000f2\b\b\u0002\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u000f2\b\b\u0002\u0010\u0019\u001a\u00020\u00032\b\b\u0002\u0010\u001a\u001a\u00020\u0003HÆ\u0001J\u0013\u0010F\u001a\u00020\b2\b\u0010G\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010H\u001a\u00020\u0017HÖ\u0001J\t\u0010I\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001eR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n¢\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\n¢\u0006\b\n\u0000\u001a\u0004\b&\u0010%R\u0011\u0010\u000e\u001a\u00020\u000f¢\u0006\b\n\u0000\u001a\u0004\b'\u0010(R\u0011\u0010\u0010\u001a\u00020\u000f¢\u0006\b\n\u0000\u001a\u0004\b)\u0010(R\u0011\u0010\u0011\u001a\u00020\u000f¢\u0006\b\n\u0000\u001a\u0004\b*\u0010(R\u0011\u0010\u0012\u001a\u00020\u000f¢\u0006\b\n\u0000\u001a\u0004\b+\u0010(R\u0011\u0010\u0013\u001a\u00020\u000f¢\u0006\b\n\u0000\u001a\u0004\b,\u0010(R\u0011\u0010\u0014\u001a\u00020\u000f¢\u0006\b\n\u0000\u001a\u0004\b-\u0010(R\u0011\u0010\u0015\u001a\u00020\u000f¢\u0006\b\n\u0000\u001a\u0004\b.\u0010(R\u0011\u0010\u0016\u001a\u00020\u0017¢\u0006\b\n\u0000\u001a\u0004\b/\u00100R\u0011\u0010\u0018\u001a\u00020\u000f¢\u0006\b\n\u0000\u001a\u0004\b1\u0010(R\u0011\u0010\u0019\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b2\u0010\u001eR\u0011\u0010\u001a\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b3\u0010\u001e¨\u0006J"}, d2 = {"Lcom/carlink/cast/CastState;", "", "sessionId", "", "stage", "Lcom/carlink/cast/CastStage;", "detail", "running", "", "events", "", "Lcom/carlink/cast/CastEvent;", "metrics", "Lcom/carlink/cast/CastMetric;", "frames", "", "bytes", "dropped", "audioPackets", "sourceAudioBytes", "sourceAudioUnits", "sourceAudioPcmNonZeroBytes", "sourceAudioPeak", "", "inputEvents", "source", "display", "<init>", "(Ljava/lang/String;Lcom/carlink/cast/CastStage;Ljava/lang/String;ZLjava/util/List;Ljava/util/List;JJJJJJJIJLjava/lang/String;Ljava/lang/String;)V", "getSessionId", "()Ljava/lang/String;", "getStage", "()Lcom/carlink/cast/CastStage;", "getDetail", "getRunning", "()Z", "getEvents", "()Ljava/util/List;", "getMetrics", "getFrames", "()J", "getBytes", "getDropped", "getAudioPackets", "getSourceAudioBytes", "getSourceAudioUnits", "getSourceAudioPcmNonZeroBytes", "getSourceAudioPeak", "()I", "getInputEvents", "getSource", "getDisplay", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "copy", "equals", "other", "hashCode", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class CastState {
    private final long audioPackets;
    private final long bytes;
    private final String detail;
    private final String display;
    private final long dropped;
    private final List<CastEvent> events;
    private final long frames;
    private final long inputEvents;
    private final List<CastMetric> metrics;
    private final boolean running;
    private final String sessionId;
    private final String source;
    private final long sourceAudioBytes;
    private final long sourceAudioPcmNonZeroBytes;
    private final int sourceAudioPeak;
    private final long sourceAudioUnits;
    private final CastStage stage;

    public CastState() {
        this(null, null, null, false, null, null, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0, 0L, null, null, 131071, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ CastState copy$default(CastState castState, String str, CastStage castStage, String str2, boolean z, List list, List list2, long j, long j2, long j3, long j4, long j5, long j6, long j7, int i, long j8, String str3, String str4, int i2, Object obj) {
        String str5;
        long j9;
        String str6 = (i2 & 1) != 0 ? castState.sessionId : str;
        CastStage castStage2 = (i2 & 2) != 0 ? castState.stage : castStage;
        String str7 = (i2 & 4) != 0 ? castState.detail : str2;
        boolean z2 = (i2 & 8) != 0 ? castState.running : z;
        List list3 = (i2 & 16) != 0 ? castState.events : list;
        List list4 = (i2 & 32) != 0 ? castState.metrics : list2;
        long j10 = (i2 & 64) != 0 ? castState.frames : j;
        long j11 = (i2 & 128) != 0 ? castState.bytes : j2;
        long j12 = (i2 & 256) != 0 ? castState.dropped : j3;
        long j13 = (i2 & 512) != 0 ? castState.audioPackets : j4;
        String str8 = str6;
        CastStage castStage3 = castStage2;
        long j14 = (i2 & 1024) != 0 ? castState.sourceAudioBytes : j5;
        long j15 = (i2 & 2048) != 0 ? castState.sourceAudioUnits : j6;
        long j16 = (i2 & 4096) != 0 ? castState.sourceAudioPcmNonZeroBytes : j7;
        int i3 = (i2 & 8192) != 0 ? castState.sourceAudioPeak : i;
        long j17 = (i2 & 16384) != 0 ? castState.inputEvents : j8;
        String str9 = (i2 & 32768) != 0 ? castState.source : str3;
        if ((i2 & 65536) != 0) {
            j9 = j17;
            str5 = castState.display;
        } else {
            str5 = str4;
            j9 = j17;
        }
        return castState.copy(str8, castStage3, str7, z2, list3, list4, j10, j11, j12, j13, j14, j15, j16, i3, j9, str9, str5);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getSessionId() {
        return this.sessionId;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final long getAudioPackets() {
        return this.audioPackets;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final long getSourceAudioBytes() {
        return this.sourceAudioBytes;
    }

    /* JADX INFO: renamed from: component12, reason: from getter */
    public final long getSourceAudioUnits() {
        return this.sourceAudioUnits;
    }

    /* JADX INFO: renamed from: component13, reason: from getter */
    public final long getSourceAudioPcmNonZeroBytes() {
        return this.sourceAudioPcmNonZeroBytes;
    }

    /* JADX INFO: renamed from: component14, reason: from getter */
    public final int getSourceAudioPeak() {
        return this.sourceAudioPeak;
    }

    /* JADX INFO: renamed from: component15, reason: from getter */
    public final long getInputEvents() {
        return this.inputEvents;
    }

    /* JADX INFO: renamed from: component16, reason: from getter */
    public final String getSource() {
        return this.source;
    }

    /* JADX INFO: renamed from: component17, reason: from getter */
    public final String getDisplay() {
        return this.display;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final CastStage getStage() {
        return this.stage;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getDetail() {
        return this.detail;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final boolean getRunning() {
        return this.running;
    }

    public final List<CastEvent> component5() {
        return this.events;
    }

    public final List<CastMetric> component6() {
        return this.metrics;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final long getFrames() {
        return this.frames;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final long getBytes() {
        return this.bytes;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final long getDropped() {
        return this.dropped;
    }

    public final CastState copy(String sessionId, CastStage stage, String detail, boolean running, List<CastEvent> events, List<CastMetric> metrics, long frames, long bytes, long dropped, long audioPackets, long sourceAudioBytes, long sourceAudioUnits, long sourceAudioPcmNonZeroBytes, int sourceAudioPeak, long inputEvents, String source, String display) {
        Intrinsics.checkNotNullParameter(sessionId, "sessionId");
        Intrinsics.checkNotNullParameter(stage, "stage");
        Intrinsics.checkNotNullParameter(detail, "detail");
        Intrinsics.checkNotNullParameter(events, "events");
        Intrinsics.checkNotNullParameter(metrics, "metrics");
        Intrinsics.checkNotNullParameter(source, "source");
        Intrinsics.checkNotNullParameter(display, "display");
        return new CastState(sessionId, stage, detail, running, events, metrics, frames, bytes, dropped, audioPackets, sourceAudioBytes, sourceAudioUnits, sourceAudioPcmNonZeroBytes, sourceAudioPeak, inputEvents, source, display);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CastState)) {
            return false;
        }
        CastState castState = (CastState) other;
        return Intrinsics.areEqual(this.sessionId, castState.sessionId) && this.stage == castState.stage && Intrinsics.areEqual(this.detail, castState.detail) && this.running == castState.running && Intrinsics.areEqual(this.events, castState.events) && Intrinsics.areEqual(this.metrics, castState.metrics) && this.frames == castState.frames && this.bytes == castState.bytes && this.dropped == castState.dropped && this.audioPackets == castState.audioPackets && this.sourceAudioBytes == castState.sourceAudioBytes && this.sourceAudioUnits == castState.sourceAudioUnits && this.sourceAudioPcmNonZeroBytes == castState.sourceAudioPcmNonZeroBytes && this.sourceAudioPeak == castState.sourceAudioPeak && this.inputEvents == castState.inputEvents && Intrinsics.areEqual(this.source, castState.source) && Intrinsics.areEqual(this.display, castState.display);
    }

    public int hashCode() {
        return (((((((((((((((((((((((((((((((this.sessionId.hashCode() * 31) + this.stage.hashCode()) * 31) + this.detail.hashCode()) * 31) + Boolean.hashCode(this.running)) * 31) + this.events.hashCode()) * 31) + this.metrics.hashCode()) * 31) + Long.hashCode(this.frames)) * 31) + Long.hashCode(this.bytes)) * 31) + Long.hashCode(this.dropped)) * 31) + Long.hashCode(this.audioPackets)) * 31) + Long.hashCode(this.sourceAudioBytes)) * 31) + Long.hashCode(this.sourceAudioUnits)) * 31) + Long.hashCode(this.sourceAudioPcmNonZeroBytes)) * 31) + Integer.hashCode(this.sourceAudioPeak)) * 31) + Long.hashCode(this.inputEvents)) * 31) + this.source.hashCode()) * 31) + this.display.hashCode();
    }

    public String toString() {
        return "CastState(sessionId=" + this.sessionId + ", stage=" + this.stage + ", detail=" + this.detail + ", running=" + this.running + ", events=" + this.events + ", metrics=" + this.metrics + ", frames=" + this.frames + ", bytes=" + this.bytes + ", dropped=" + this.dropped + ", audioPackets=" + this.audioPackets + ", sourceAudioBytes=" + this.sourceAudioBytes + ", sourceAudioUnits=" + this.sourceAudioUnits + ", sourceAudioPcmNonZeroBytes=" + this.sourceAudioPcmNonZeroBytes + ", sourceAudioPeak=" + this.sourceAudioPeak + ", inputEvents=" + this.inputEvents + ", source=" + this.source + ", display=" + this.display + ")";
    }

    public CastState(String sessionId, CastStage stage, String detail, boolean running, List<CastEvent> events, List<CastMetric> metrics, long frames, long bytes, long dropped, long audioPackets, long sourceAudioBytes, long sourceAudioUnits, long sourceAudioPcmNonZeroBytes, int sourceAudioPeak, long inputEvents, String source, String display) {
        Intrinsics.checkNotNullParameter(sessionId, "sessionId");
        Intrinsics.checkNotNullParameter(stage, "stage");
        Intrinsics.checkNotNullParameter(detail, "detail");
        Intrinsics.checkNotNullParameter(events, "events");
        Intrinsics.checkNotNullParameter(metrics, "metrics");
        Intrinsics.checkNotNullParameter(source, "source");
        Intrinsics.checkNotNullParameter(display, "display");
        this.sessionId = sessionId;
        this.stage = stage;
        this.detail = detail;
        this.running = running;
        this.events = events;
        this.metrics = metrics;
        this.frames = frames;
        this.bytes = bytes;
        this.dropped = dropped;
        this.audioPackets = audioPackets;
        this.sourceAudioBytes = sourceAudioBytes;
        this.sourceAudioUnits = sourceAudioUnits;
        this.sourceAudioPcmNonZeroBytes = sourceAudioPcmNonZeroBytes;
        this.sourceAudioPeak = sourceAudioPeak;
        this.inputEvents = inputEvents;
        this.source = source;
        this.display = display;
    }

    public /* synthetic */ CastState(String str, CastStage castStage, String str2, boolean z, List list, List list2, long j, long j2, long j3, long j4, long j5, long j6, long j7, int i, long j8, String str3, String str4, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? "" : str, (i2 & 2) != 0 ? CastStage.STOPPED : castStage, (i2 & 4) != 0 ? "在车机上选择本机，然后开始投屏" : str2, (i2 & 8) != 0 ? false : z, (i2 & 16) != 0 ? CollectionsKt.emptyList() : list, (i2 & 32) != 0 ? CollectionsKt.emptyList() : list2, (i2 & 64) != 0 ? 0L : j, (i2 & 128) != 0 ? 0L : j2, (i2 & 256) != 0 ? 0L : j3, (i2 & 512) != 0 ? 0L : j4, (i2 & 1024) != 0 ? 0L : j5, (i2 & 2048) != 0 ? 0L : j6, (i2 & 4096) != 0 ? 0L : j7, (i2 & 8192) == 0 ? i : 0, (i2 & 16384) == 0 ? j8 : 0L, (32768 & i2) != 0 ? "" : str3, (i2 & 65536) == 0 ? str4 : "");
    }

    public final String getSessionId() {
        return this.sessionId;
    }

    public final CastStage getStage() {
        return this.stage;
    }

    public final String getDetail() {
        return this.detail;
    }

    public final boolean getRunning() {
        return this.running;
    }

    public final List<CastEvent> getEvents() {
        return this.events;
    }

    public final List<CastMetric> getMetrics() {
        return this.metrics;
    }

    public final long getFrames() {
        return this.frames;
    }

    public final long getBytes() {
        return this.bytes;
    }

    public final long getDropped() {
        return this.dropped;
    }

    public final long getAudioPackets() {
        return this.audioPackets;
    }

    public final long getSourceAudioBytes() {
        return this.sourceAudioBytes;
    }

    public final long getSourceAudioUnits() {
        return this.sourceAudioUnits;
    }

    public final long getSourceAudioPcmNonZeroBytes() {
        return this.sourceAudioPcmNonZeroBytes;
    }

    public final int getSourceAudioPeak() {
        return this.sourceAudioPeak;
    }

    public final long getInputEvents() {
        return this.inputEvents;
    }

    public final String getSource() {
        return this.source;
    }

    public final String getDisplay() {
        return this.display;
    }
}
