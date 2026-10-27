package com.carplay.sender;

import kotlin.Metadata;

/* JADX INFO: compiled from: HidKeys.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0010\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\b¨\u0006\u0013"}, d2 = {"Lcom/carplay/sender/HidUsage;", "", "page", "", "usage", "<init>", "(II)V", "getPage", "()I", "getUsage", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class HidUsage {
    private final int page;
    private final int usage;

    public static /* synthetic */ HidUsage copy$default(HidUsage hidUsage, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            i = hidUsage.page;
        }
        if ((i3 & 2) != 0) {
            i2 = hidUsage.usage;
        }
        return hidUsage.copy(i, i2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getPage() {
        return this.page;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getUsage() {
        return this.usage;
    }

    public final HidUsage copy(int page, int usage) {
        return new HidUsage(page, usage);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HidUsage)) {
            return false;
        }
        HidUsage hidUsage = (HidUsage) other;
        return this.page == hidUsage.page && this.usage == hidUsage.usage;
    }

    public int hashCode() {
        return (Integer.hashCode(this.page) * 31) + Integer.hashCode(this.usage);
    }

    public String toString() {
        return "HidUsage(page=" + this.page + ", usage=" + this.usage + ")";
    }

    public HidUsage(int page, int usage) {
        this.page = page;
        this.usage = usage;
    }

    public final int getPage() {
        return this.page;
    }

    public final int getUsage() {
        return this.usage;
    }
}
