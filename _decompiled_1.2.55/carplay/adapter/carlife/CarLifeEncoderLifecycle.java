package com.carplay.adapter.carlife;

import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: CarLifeEncoderLifecycle.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u0001:\u0001\rB\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0006\u0010\u0006\u001a\u00020\u0007J\u0006\u0010\b\u001a\u00020\u0007J\u0006\u0010\t\u001a\u00020\u0007J\u0006\u0010\n\u001a\u00020\u0007J\u0006\u0010\u000b\u001a\u00020\fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeEncoderLifecycle;", "", "<init>", "()V", "phase", "Lcom/carplay/adapter/carlife/CarLifeEncoderLifecycle$Phase;", "requestInitial", "", "requestRebuild", "onEncoderInitDone", "onEncoderStarted", "reset", "", "Phase", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarLifeEncoderLifecycle {
    private Phase phase = Phase.IDLE;

    /* JADX INFO: compiled from: CarLifeEncoderLifecycle.kt */
    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\t\b\u0082\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\t¨\u0006\n"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeEncoderLifecycle$Phase;", "", "<init>", "(Ljava/lang/String;I)V", "IDLE", "INIT_SENT", "WAITING_FOR_INITIAL_START", "STREAMING", "REBUILD_SENT", "WAITING_FOR_REBUILD_START", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private enum Phase {
        IDLE,
        INIT_SENT,
        WAITING_FOR_INITIAL_START,
        STREAMING,
        REBUILD_SENT,
        WAITING_FOR_REBUILD_START;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

        public static EnumEntries<Phase> getEntries() {
            return $ENTRIES;
        }
    }

    /* JADX INFO: compiled from: CarLifeEncoderLifecycle.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[Phase.values().length];
            try {
                iArr[Phase.IDLE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Phase.INIT_SENT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Phase.WAITING_FOR_INITIAL_START.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[Phase.STREAMING.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[Phase.REBUILD_SENT.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[Phase.WAITING_FOR_REBUILD_START.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public final synchronized boolean requestInitial() {
        if (this.phase != Phase.IDLE) {
            return false;
        }
        this.phase = Phase.INIT_SENT;
        return true;
    }

    public final synchronized boolean requestRebuild() {
        switch (WhenMappings.$EnumSwitchMapping$0[this.phase.ordinal()]) {
            case 1:
            case 2:
            case 3:
                return false;
            case 4:
                this.phase = Phase.REBUILD_SENT;
                return true;
            case 5:
            case 6:
                return false;
            default:
                throw new NoWhenBranchMatchedException();
        }
    }

    public final synchronized boolean onEncoderInitDone() {
        Phase phase;
        switch (WhenMappings.$EnumSwitchMapping$0[this.phase.ordinal()]) {
            case 2:
                phase = Phase.WAITING_FOR_INITIAL_START;
                break;
            case 5:
                phase = Phase.WAITING_FOR_REBUILD_START;
                break;
            default:
                return false;
        }
        this.phase = phase;
        return true;
    }

    public final synchronized boolean onEncoderStarted() {
        switch (WhenMappings.$EnumSwitchMapping$0[this.phase.ordinal()]) {
            case 3:
            case 6:
                this.phase = Phase.STREAMING;
                return false;
            default:
                return false;
        }
        throw th;
    }

    public final synchronized void reset() {
        this.phase = Phase.IDLE;
    }
}
