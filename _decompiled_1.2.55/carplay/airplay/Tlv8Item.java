package com.carplay.airplay;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Tlv8Codec.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0015"}, d2 = {"Lcom/carplay/airplay/Tlv8Item;", "", "type", "", "value", "", "<init>", "(I[B)V", "getType", "()I", "getValue", "()[B", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class Tlv8Item {
    private final int type;
    private final byte[] value;

    public static /* synthetic */ Tlv8Item copy$default(Tlv8Item tlv8Item, int i, byte[] bArr, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = tlv8Item.type;
        }
        if ((i2 & 2) != 0) {
            bArr = tlv8Item.value;
        }
        return tlv8Item.copy(i, bArr);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getType() {
        return this.type;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final byte[] getValue() {
        return this.value;
    }

    public final Tlv8Item copy(int type, byte[] value) {
        Intrinsics.checkNotNullParameter(value, "value");
        return new Tlv8Item(type, value);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Tlv8Item)) {
            return false;
        }
        Tlv8Item tlv8Item = (Tlv8Item) other;
        return this.type == tlv8Item.type && Intrinsics.areEqual(this.value, tlv8Item.value);
    }

    public int hashCode() {
        return (Integer.hashCode(this.type) * 31) + Arrays.hashCode(this.value);
    }

    public String toString() {
        return "Tlv8Item(type=" + this.type + ", value=" + Arrays.toString(this.value) + ")";
    }

    public Tlv8Item(int type, byte[] value) {
        Intrinsics.checkNotNullParameter(value, "value");
        this.type = type;
        this.value = value;
    }

    public final int getType() {
        return this.type;
    }

    public final byte[] getValue() {
        return this.value;
    }
}
