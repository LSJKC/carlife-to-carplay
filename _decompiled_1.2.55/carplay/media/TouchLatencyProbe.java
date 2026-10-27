package com.carplay.media;

import kotlin.Metadata;

/* JADX INFO: compiled from: TouchLatencyProbe.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0005\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u0005J\u000e\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u0005R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\n¨\u0006\u0011"}, d2 = {"Lcom/carplay/media/TouchLatencyProbe;", "", "<init>", "()V", "pendingTouchNs", "", "maxSendNs", "getMaxSendNs", "()J", "setMaxSendNs", "(J)V", "onTouchSent", "", "sentAtNs", "sendDurationNs", "onFrame", "nowNs", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class TouchLatencyProbe {
    public static final TouchLatencyProbe INSTANCE = new TouchLatencyProbe();
    private static volatile long maxSendNs;
    private static volatile long pendingTouchNs;

    private TouchLatencyProbe() {
    }

    public final long getMaxSendNs() {
        return maxSendNs;
    }

    public final void setMaxSendNs(long j) {
        maxSendNs = j;
    }

    public final void onTouchSent(long sentAtNs, long sendDurationNs) {
        if (pendingTouchNs == 0) {
            pendingTouchNs = sentAtNs;
        }
        if (sendDurationNs > maxSendNs) {
            maxSendNs = sendDurationNs;
        }
    }

    public final long onFrame(long nowNs) {
        long touch = pendingTouchNs;
        if (touch == 0) {
            return -1L;
        }
        pendingTouchNs = 0L;
        return nowNs - touch;
    }
}
