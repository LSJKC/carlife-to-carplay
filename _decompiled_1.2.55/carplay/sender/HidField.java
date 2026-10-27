package com.carplay.sender;

import kotlin.Metadata;

/* JADX INFO: compiled from: HidInput.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J;\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0019\u001a\u00020\u0003HÖ\u0001J\t\u0010\u001a\u001a\u00020\u001bHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000bR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000bR\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000b¨\u0006\u001c"}, d2 = {"Lcom/carplay/sender/HidField;", "", "page", "", "usage", "offset", "size", "maximum", "<init>", "(IIIII)V", "getPage", "()I", "getUsage", "getOffset", "getSize", "getMaximum", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class HidField {
    private final int maximum;
    private final int offset;
    private final int page;
    private final int size;
    private final int usage;

    public static /* synthetic */ HidField copy$default(HidField hidField, int i, int i2, int i3, int i4, int i5, int i6, Object obj) {
        if ((i6 & 1) != 0) {
            i = hidField.page;
        }
        if ((i6 & 2) != 0) {
            i2 = hidField.usage;
        }
        if ((i6 & 4) != 0) {
            i3 = hidField.offset;
        }
        if ((i6 & 8) != 0) {
            i4 = hidField.size;
        }
        if ((i6 & 16) != 0) {
            i5 = hidField.maximum;
        }
        int i7 = i5;
        int i8 = i3;
        return hidField.copy(i, i2, i8, i4, i7);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getPage() {
        return this.page;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getUsage() {
        return this.usage;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int getOffset() {
        return this.offset;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int getSize() {
        return this.size;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final int getMaximum() {
        return this.maximum;
    }

    public final HidField copy(int page, int usage, int offset, int size, int maximum) {
        return new HidField(page, usage, offset, size, maximum);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HidField)) {
            return false;
        }
        HidField hidField = (HidField) other;
        return this.page == hidField.page && this.usage == hidField.usage && this.offset == hidField.offset && this.size == hidField.size && this.maximum == hidField.maximum;
    }

    public int hashCode() {
        return (((((((Integer.hashCode(this.page) * 31) + Integer.hashCode(this.usage)) * 31) + Integer.hashCode(this.offset)) * 31) + Integer.hashCode(this.size)) * 31) + Integer.hashCode(this.maximum);
    }

    public String toString() {
        return "HidField(page=" + this.page + ", usage=" + this.usage + ", offset=" + this.offset + ", size=" + this.size + ", maximum=" + this.maximum + ")";
    }

    public HidField(int page, int usage, int offset, int size, int maximum) {
        this.page = page;
        this.usage = usage;
        this.offset = offset;
        this.size = size;
        this.maximum = maximum;
    }

    public final int getMaximum() {
        return this.maximum;
    }

    public final int getOffset() {
        return this.offset;
    }

    public final int getPage() {
        return this.page;
    }

    public final int getSize() {
        return this.size;
    }

    public final int getUsage() {
        return this.usage;
    }
}
