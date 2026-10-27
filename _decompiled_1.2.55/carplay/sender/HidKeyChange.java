package com.carplay.sender;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: HidKeys.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00052\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0015"}, d2 = {"Lcom/carplay/sender/HidKeyChange;", "", "usage", "Lcom/carplay/sender/HidUsage;", "down", "", "<init>", "(Lcom/carplay/sender/HidUsage;Z)V", "getUsage", "()Lcom/carplay/sender/HidUsage;", "getDown", "()Z", "component1", "component2", "copy", "equals", "other", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class HidKeyChange {
    private final boolean down;
    private final HidUsage usage;

    public static /* synthetic */ HidKeyChange copy$default(HidKeyChange hidKeyChange, HidUsage hidUsage, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            hidUsage = hidKeyChange.usage;
        }
        if ((i & 2) != 0) {
            z = hidKeyChange.down;
        }
        return hidKeyChange.copy(hidUsage, z);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final HidUsage getUsage() {
        return this.usage;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final boolean getDown() {
        return this.down;
    }

    public final HidKeyChange copy(HidUsage usage, boolean down) {
        Intrinsics.checkNotNullParameter(usage, "usage");
        return new HidKeyChange(usage, down);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HidKeyChange)) {
            return false;
        }
        HidKeyChange hidKeyChange = (HidKeyChange) other;
        return Intrinsics.areEqual(this.usage, hidKeyChange.usage) && this.down == hidKeyChange.down;
    }

    public int hashCode() {
        return (this.usage.hashCode() * 31) + Boolean.hashCode(this.down);
    }

    public String toString() {
        return "HidKeyChange(usage=" + this.usage + ", down=" + this.down + ")";
    }

    public HidKeyChange(HidUsage usage, boolean down) {
        Intrinsics.checkNotNullParameter(usage, "usage");
        this.usage = usage;
        this.down = down;
    }

    public final boolean getDown() {
        return this.down;
    }

    public final HidUsage getUsage() {
        return this.usage;
    }
}
