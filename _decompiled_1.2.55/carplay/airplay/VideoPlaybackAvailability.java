package com.carplay.airplay;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: VideoInCar.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0000\u0018\u00002\u00020\u0001B\u001b\u0012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0004J\u000e\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u0004J\u000e\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u0004J\b\u0010\u0013\u001a\u00020\rH\u0002R\u001a\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0007\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\bR\u000e\u0010\t\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000b\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\b¨\u0006\u0014"}, d2 = {"Lcom/carplay/airplay/VideoPlaybackAvailability;", "", "send", "Lkotlin/Function1;", "", "<init>", "(Lkotlin/jvm/functions/Function1;)V", "desired", "Ljava/lang/Boolean;", "featureEnabled", "eventReady", "lastSent", "setDesired", "Lcom/carplay/airplay/VideoPlaybackDelivery;", "allowed", "setFeatureEnabled", "enabled", "setEventReady", "ready", "flush", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class VideoPlaybackAvailability {
    private Boolean desired;
    private boolean eventReady;
    private boolean featureEnabled;
    private Boolean lastSent;
    private final Function1<Boolean, Boolean> send;

    /* JADX WARN: Multi-variable type inference failed */
    public VideoPlaybackAvailability(Function1<? super Boolean, Boolean> send) {
        Intrinsics.checkNotNullParameter(send, "send");
        this.send = send;
    }

    public final synchronized VideoPlaybackDelivery setDesired(boolean allowed) {
        this.desired = Boolean.valueOf(allowed);
        return flush();
    }

    public final synchronized VideoPlaybackDelivery setFeatureEnabled(boolean enabled) {
        if (this.featureEnabled != enabled) {
            this.featureEnabled = enabled;
            if (!enabled) {
                this.lastSent = null;
            }
        }
        return flush();
    }

    public final synchronized VideoPlaybackDelivery setEventReady(boolean ready) {
        if (this.eventReady != ready) {
            this.eventReady = ready;
            if (!ready) {
                this.lastSent = null;
            }
        }
        return flush();
    }

    private final VideoPlaybackDelivery flush() {
        Boolean bool = this.desired;
        if (bool == null) {
            return VideoPlaybackDelivery.QUEUED;
        }
        boolean next = bool.booleanValue();
        if (!this.featureEnabled || !this.eventReady) {
            return VideoPlaybackDelivery.QUEUED;
        }
        if (Intrinsics.areEqual(this.lastSent, Boolean.valueOf(next))) {
            return VideoPlaybackDelivery.UNCHANGED;
        }
        if (this.send.invoke(Boolean.valueOf(next)).booleanValue()) {
            this.lastSent = Boolean.valueOf(next);
            return VideoPlaybackDelivery.SENT;
        }
        return VideoPlaybackDelivery.QUEUED;
    }
}
