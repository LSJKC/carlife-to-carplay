package com.carlink.cast;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CastLog.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0018\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B?\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\n¢\u0006\u0004\b\f\u0010\rJ\t\u0010\u0018\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0019\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001a\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001b\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001d\u001a\u00020\nHÆ\u0003J\t\u0010\u001e\u001a\u00020\nHÆ\u0003JO\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\nHÆ\u0001J\u0013\u0010 \u001a\u00020\n2\b\u0010!\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\"\u001a\u00020#HÖ\u0001J\t\u0010$\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000fR\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000fR\u0011\u0010\b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0011R\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u000b\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0016¨\u0006%"}, d2 = {"Lcom/carlink/cast/CastSessionRecord;", "", "sessionId", "", "startedAtMillis", "", "stage", "source", "frames", "hasCrash", "", "legacy", "<init>", "(Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;JZZ)V", "getSessionId", "()Ljava/lang/String;", "getStartedAtMillis", "()J", "getStage", "getSource", "getFrames", "getHasCrash", "()Z", "getLegacy", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class CastSessionRecord {
    private final long frames;
    private final boolean hasCrash;
    private final boolean legacy;
    private final String sessionId;
    private final String source;
    private final String stage;
    private final long startedAtMillis;

    public static /* synthetic */ CastSessionRecord copy$default(CastSessionRecord castSessionRecord, String str, long j, String str2, String str3, long j2, boolean z, boolean z2, int i, Object obj) {
        if ((i & 1) != 0) {
            str = castSessionRecord.sessionId;
        }
        if ((i & 2) != 0) {
            j = castSessionRecord.startedAtMillis;
        }
        if ((i & 4) != 0) {
            str2 = castSessionRecord.stage;
        }
        if ((i & 8) != 0) {
            str3 = castSessionRecord.source;
        }
        if ((i & 16) != 0) {
            j2 = castSessionRecord.frames;
        }
        if ((i & 32) != 0) {
            z = castSessionRecord.hasCrash;
        }
        if ((i & 64) != 0) {
            z2 = castSessionRecord.legacy;
        }
        long j3 = j2;
        String str4 = str2;
        return castSessionRecord.copy(str, j, str4, str3, j3, z, z2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getSessionId() {
        return this.sessionId;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final long getStartedAtMillis() {
        return this.startedAtMillis;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getStage() {
        return this.stage;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final String getSource() {
        return this.source;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final long getFrames() {
        return this.frames;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final boolean getHasCrash() {
        return this.hasCrash;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final boolean getLegacy() {
        return this.legacy;
    }

    public final CastSessionRecord copy(String sessionId, long startedAtMillis, String stage, String source, long frames, boolean hasCrash, boolean legacy) {
        Intrinsics.checkNotNullParameter(sessionId, "sessionId");
        Intrinsics.checkNotNullParameter(stage, "stage");
        Intrinsics.checkNotNullParameter(source, "source");
        return new CastSessionRecord(sessionId, startedAtMillis, stage, source, frames, hasCrash, legacy);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CastSessionRecord)) {
            return false;
        }
        CastSessionRecord castSessionRecord = (CastSessionRecord) other;
        return Intrinsics.areEqual(this.sessionId, castSessionRecord.sessionId) && this.startedAtMillis == castSessionRecord.startedAtMillis && Intrinsics.areEqual(this.stage, castSessionRecord.stage) && Intrinsics.areEqual(this.source, castSessionRecord.source) && this.frames == castSessionRecord.frames && this.hasCrash == castSessionRecord.hasCrash && this.legacy == castSessionRecord.legacy;
    }

    public int hashCode() {
        return (((((((((((this.sessionId.hashCode() * 31) + Long.hashCode(this.startedAtMillis)) * 31) + this.stage.hashCode()) * 31) + this.source.hashCode()) * 31) + Long.hashCode(this.frames)) * 31) + Boolean.hashCode(this.hasCrash)) * 31) + Boolean.hashCode(this.legacy);
    }

    public String toString() {
        return "CastSessionRecord(sessionId=" + this.sessionId + ", startedAtMillis=" + this.startedAtMillis + ", stage=" + this.stage + ", source=" + this.source + ", frames=" + this.frames + ", hasCrash=" + this.hasCrash + ", legacy=" + this.legacy + ")";
    }

    public CastSessionRecord(String sessionId, long startedAtMillis, String stage, String source, long frames, boolean hasCrash, boolean legacy) {
        Intrinsics.checkNotNullParameter(sessionId, "sessionId");
        Intrinsics.checkNotNullParameter(stage, "stage");
        Intrinsics.checkNotNullParameter(source, "source");
        this.sessionId = sessionId;
        this.startedAtMillis = startedAtMillis;
        this.stage = stage;
        this.source = source;
        this.frames = frames;
        this.hasCrash = hasCrash;
        this.legacy = legacy;
    }

    public final String getSessionId() {
        return this.sessionId;
    }

    public final long getStartedAtMillis() {
        return this.startedAtMillis;
    }

    public final String getStage() {
        return this.stage;
    }

    public final String getSource() {
        return this.source;
    }

    public final long getFrames() {
        return this.frames;
    }

    public final boolean getHasCrash() {
        return this.hasCrash;
    }

    public final boolean getLegacy() {
        return this.legacy;
    }
}
