package com.carplay.airplay;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: MicrophonePacketizer.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\r\u0018\u00002\u00020\u0001B%\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006¢\u0006\u0004\b\u0007\u0010\bR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\n\"\u0004\b\u000e\u0010\fR\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012¨\u0006\u0013"}, d2 = {"Lcom/carplay/airplay/MicrophoneCounters;", "", "sequence", "", "timestamp", "nonce", "", "<init>", "(IIJ)V", "getSequence", "()I", "setSequence", "(I)V", "getTimestamp", "setTimestamp", "getNonce", "()J", "setNonce", "(J)V", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MicrophoneCounters {
    private long nonce;
    private int sequence;
    private int timestamp;

    public MicrophoneCounters() {
        this(0, 0, 0L, 7, null);
    }

    public MicrophoneCounters(int sequence, int timestamp, long nonce) {
        this.sequence = sequence;
        this.timestamp = timestamp;
        this.nonce = nonce;
    }

    public /* synthetic */ MicrophoneCounters(int i, int i2, long j, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? 0 : i, (i3 & 2) != 0 ? 0 : i2, (i3 & 4) != 0 ? 0L : j);
    }

    public final int getSequence() {
        return this.sequence;
    }

    public final void setSequence(int i) {
        this.sequence = i;
    }

    public final int getTimestamp() {
        return this.timestamp;
    }

    public final void setTimestamp(int i) {
        this.timestamp = i;
    }

    public final long getNonce() {
        return this.nonce;
    }

    public final void setNonce(long j) {
        this.nonce = j;
    }
}
