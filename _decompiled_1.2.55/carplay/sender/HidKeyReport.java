package com.carplay.sender;

import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: HidKeys.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B+\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0004\b\n\u0010\u000bJ\u000f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\u000f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006HÆ\u0003J\t\u0010\u0014\u001a\u00020\tHÆ\u0003J3\u0010\u0015\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\b\b\u0002\u0010\b\u001a\u00020\tHÆ\u0001J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0019\u001a\u00020\tHÖ\u0001J\t\u0010\u001a\u001a\u00020\u001bHÖ\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u001c"}, d2 = {"Lcom/carplay/sender/HidKeyReport;", "", "held", "", "Lcom/carplay/sender/HidUsage;", "changes", "", "Lcom/carplay/sender/HidKeyChange;", "wheel", "", "<init>", "(Ljava/util/Set;Ljava/util/List;I)V", "getHeld", "()Ljava/util/Set;", "getChanges", "()Ljava/util/List;", "getWheel", "()I", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class HidKeyReport {
    private final List<HidKeyChange> changes;
    private final Set<HidUsage> held;
    private final int wheel;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ HidKeyReport copy$default(HidKeyReport hidKeyReport, Set set, List list, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            set = hidKeyReport.held;
        }
        if ((i2 & 2) != 0) {
            list = hidKeyReport.changes;
        }
        if ((i2 & 4) != 0) {
            i = hidKeyReport.wheel;
        }
        return hidKeyReport.copy(set, list, i);
    }

    public final Set<HidUsage> component1() {
        return this.held;
    }

    public final List<HidKeyChange> component2() {
        return this.changes;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int getWheel() {
        return this.wheel;
    }

    public final HidKeyReport copy(Set<HidUsage> held, List<HidKeyChange> changes, int wheel) {
        Intrinsics.checkNotNullParameter(held, "held");
        Intrinsics.checkNotNullParameter(changes, "changes");
        return new HidKeyReport(held, changes, wheel);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HidKeyReport)) {
            return false;
        }
        HidKeyReport hidKeyReport = (HidKeyReport) other;
        return Intrinsics.areEqual(this.held, hidKeyReport.held) && Intrinsics.areEqual(this.changes, hidKeyReport.changes) && this.wheel == hidKeyReport.wheel;
    }

    public int hashCode() {
        return (((this.held.hashCode() * 31) + this.changes.hashCode()) * 31) + Integer.hashCode(this.wheel);
    }

    public String toString() {
        return "HidKeyReport(held=" + this.held + ", changes=" + this.changes + ", wheel=" + this.wheel + ")";
    }

    public HidKeyReport(Set<HidUsage> held, List<HidKeyChange> changes, int wheel) {
        Intrinsics.checkNotNullParameter(held, "held");
        Intrinsics.checkNotNullParameter(changes, "changes");
        this.held = held;
        this.changes = changes;
        this.wheel = wheel;
    }

    public final List<HidKeyChange> getChanges() {
        return this.changes;
    }

    public final Set<HidUsage> getHeld() {
        return this.held;
    }

    public final int getWheel() {
        return this.wheel;
    }
}
