package com.carplay.iap2.transport;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2WiredControlClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007¢\u0006\u0004\b\t\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0007HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0007HÆ\u0003J1\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001a\u001a\u00020\u0007HÖ\u0001J\t\u0010\u001b\u001a\u00020\u001cHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\b\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010¨\u0006\u001d"}, d2 = {"Lcom/carplay/iap2/transport/Iap2WiredControlResult;", "", "terminal", "Lcom/carplay/iap2/transport/Iap2WiredControlTerminal;", "stage", "Lcom/carplay/iap2/transport/Iap2WiredControlStage;", "forwardedFrames", "", "carPlayStartSessionsSent", "<init>", "(Lcom/carplay/iap2/transport/Iap2WiredControlTerminal;Lcom/carplay/iap2/transport/Iap2WiredControlStage;II)V", "getTerminal", "()Lcom/carplay/iap2/transport/Iap2WiredControlTerminal;", "getStage", "()Lcom/carplay/iap2/transport/Iap2WiredControlStage;", "getForwardedFrames", "()I", "getCarPlayStartSessionsSent", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class Iap2WiredControlResult {
    private final int carPlayStartSessionsSent;
    private final int forwardedFrames;
    private final Iap2WiredControlStage stage;
    private final Iap2WiredControlTerminal terminal;

    public static /* synthetic */ Iap2WiredControlResult copy$default(Iap2WiredControlResult iap2WiredControlResult, Iap2WiredControlTerminal iap2WiredControlTerminal, Iap2WiredControlStage iap2WiredControlStage, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            iap2WiredControlTerminal = iap2WiredControlResult.terminal;
        }
        if ((i3 & 2) != 0) {
            iap2WiredControlStage = iap2WiredControlResult.stage;
        }
        if ((i3 & 4) != 0) {
            i = iap2WiredControlResult.forwardedFrames;
        }
        if ((i3 & 8) != 0) {
            i2 = iap2WiredControlResult.carPlayStartSessionsSent;
        }
        return iap2WiredControlResult.copy(iap2WiredControlTerminal, iap2WiredControlStage, i, i2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final Iap2WiredControlTerminal getTerminal() {
        return this.terminal;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final Iap2WiredControlStage getStage() {
        return this.stage;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int getForwardedFrames() {
        return this.forwardedFrames;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int getCarPlayStartSessionsSent() {
        return this.carPlayStartSessionsSent;
    }

    public final Iap2WiredControlResult copy(Iap2WiredControlTerminal terminal, Iap2WiredControlStage stage, int forwardedFrames, int carPlayStartSessionsSent) {
        Intrinsics.checkNotNullParameter(terminal, "terminal");
        Intrinsics.checkNotNullParameter(stage, "stage");
        return new Iap2WiredControlResult(terminal, stage, forwardedFrames, carPlayStartSessionsSent);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Iap2WiredControlResult)) {
            return false;
        }
        Iap2WiredControlResult iap2WiredControlResult = (Iap2WiredControlResult) other;
        return this.terminal == iap2WiredControlResult.terminal && this.stage == iap2WiredControlResult.stage && this.forwardedFrames == iap2WiredControlResult.forwardedFrames && this.carPlayStartSessionsSent == iap2WiredControlResult.carPlayStartSessionsSent;
    }

    public int hashCode() {
        return (((((this.terminal.hashCode() * 31) + this.stage.hashCode()) * 31) + Integer.hashCode(this.forwardedFrames)) * 31) + Integer.hashCode(this.carPlayStartSessionsSent);
    }

    public String toString() {
        return "Iap2WiredControlResult(terminal=" + this.terminal + ", stage=" + this.stage + ", forwardedFrames=" + this.forwardedFrames + ", carPlayStartSessionsSent=" + this.carPlayStartSessionsSent + ")";
    }

    public Iap2WiredControlResult(Iap2WiredControlTerminal terminal, Iap2WiredControlStage stage, int forwardedFrames, int carPlayStartSessionsSent) {
        Intrinsics.checkNotNullParameter(terminal, "terminal");
        Intrinsics.checkNotNullParameter(stage, "stage");
        this.terminal = terminal;
        this.stage = stage;
        this.forwardedFrames = forwardedFrames;
        this.carPlayStartSessionsSent = carPlayStartSessionsSent;
    }

    public final Iap2WiredControlTerminal getTerminal() {
        return this.terminal;
    }

    public final Iap2WiredControlStage getStage() {
        return this.stage;
    }

    public final int getForwardedFrames() {
        return this.forwardedFrames;
    }

    public final int getCarPlayStartSessionsSent() {
        return this.carPlayStartSessionsSent;
    }
}
