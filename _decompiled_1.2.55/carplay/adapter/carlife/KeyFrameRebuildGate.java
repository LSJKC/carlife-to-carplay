package com.carplay.adapter.carlife;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: KeyFrameRebuildGate.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0000\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0006\u0010\b\u001a\u00020\u0005J\u0006\u0010\t\u001a\u00020\u0005J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0005J\u0006\u0010\r\u001a\u00020\u000bJ\u0006\u0010\u000e\u001a\u00020\u0005R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lcom/carplay/adapter/carlife/KeyFrameRebuildGate;", "", "<init>", "()V", "inFlight", "", "lastRequestNanos", "", "request", "beginRetry", "observeFrame", "", "keyFrame", "reset", "isInFlight", "Companion", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class KeyFrameRebuildGate {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final long RETRY_MIN_INTERVAL_NANOS = 1000000000;
    private boolean inFlight;
    private long lastRequestNanos;

    public final synchronized boolean request() {
        if (this.inFlight) {
            return false;
        }
        this.inFlight = true;
        this.lastRequestNanos = System.nanoTime();
        return true;
    }

    public final synchronized boolean beginRetry() {
        if (this.inFlight && System.nanoTime() - this.lastRequestNanos < RETRY_MIN_INTERVAL_NANOS) {
            return false;
        }
        this.inFlight = true;
        this.lastRequestNanos = System.nanoTime();
        return true;
    }

    public final synchronized void observeFrame(boolean keyFrame) {
        if (keyFrame) {
            this.inFlight = false;
        }
    }

    public final synchronized void reset() {
        this.inFlight = false;
        this.lastRequestNanos = 0L;
    }

    public final synchronized boolean isInFlight() {
        return this.inFlight;
    }

    /* JADX INFO: compiled from: KeyFrameRebuildGate.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lcom/carplay/adapter/carlife/KeyFrameRebuildGate$Companion;", "", "<init>", "()V", "RETRY_MIN_INTERVAL_NANOS", "", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
