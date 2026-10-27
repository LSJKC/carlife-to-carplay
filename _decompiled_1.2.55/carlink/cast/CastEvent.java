package com.carlink.cast;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CastLog.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005¢\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0005HÆ\u0003J'\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\f¨\u0006\u0018"}, d2 = {"Lcom/carlink/cast/CastEvent;", "", "elapsedMillis", "", "level", "", "message", "<init>", "(JLjava/lang/String;Ljava/lang/String;)V", "getElapsedMillis", "()J", "getLevel", "()Ljava/lang/String;", "getMessage", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class CastEvent {
    private final long elapsedMillis;
    private final String level;
    private final String message;

    public static /* synthetic */ CastEvent copy$default(CastEvent castEvent, long j, String str, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            j = castEvent.elapsedMillis;
        }
        if ((i & 2) != 0) {
            str = castEvent.level;
        }
        if ((i & 4) != 0) {
            str2 = castEvent.message;
        }
        return castEvent.copy(j, str, str2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final long getElapsedMillis() {
        return this.elapsedMillis;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getLevel() {
        return this.level;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getMessage() {
        return this.message;
    }

    public final CastEvent copy(long elapsedMillis, String level, String message) {
        Intrinsics.checkNotNullParameter(level, "level");
        Intrinsics.checkNotNullParameter(message, "message");
        return new CastEvent(elapsedMillis, level, message);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CastEvent)) {
            return false;
        }
        CastEvent castEvent = (CastEvent) other;
        return this.elapsedMillis == castEvent.elapsedMillis && Intrinsics.areEqual(this.level, castEvent.level) && Intrinsics.areEqual(this.message, castEvent.message);
    }

    public int hashCode() {
        return (((Long.hashCode(this.elapsedMillis) * 31) + this.level.hashCode()) * 31) + this.message.hashCode();
    }

    public String toString() {
        return "CastEvent(elapsedMillis=" + this.elapsedMillis + ", level=" + this.level + ", message=" + this.message + ")";
    }

    public CastEvent(long elapsedMillis, String level, String message) {
        Intrinsics.checkNotNullParameter(level, "level");
        Intrinsics.checkNotNullParameter(message, "message");
        this.elapsedMillis = elapsedMillis;
        this.level = level;
        this.message = message;
    }

    public final long getElapsedMillis() {
        return this.elapsedMillis;
    }

    public final String getLevel() {
        return this.level;
    }

    public final String getMessage() {
        return this.message;
    }
}
