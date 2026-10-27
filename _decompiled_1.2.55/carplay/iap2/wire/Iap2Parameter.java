package com.carplay.iap2.wire;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Iap2Wire.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\r\u0010\r\u001a\u00020\u0005H\u0000¢\u0006\u0002\b\u000eJ\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u0012\u001a\u00020\u0003H\u0016J\b\u0010\u0013\u001a\u00020\u0014H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\f¨\u0006\u0016"}, d2 = {"Lcom/carplay/iap2/wire/Iap2Parameter;", "", "id", "", "payload", "", "<init>", "(I[B)V", "getId", "()I", "bytes", "getPayload", "()[B", "payloadView", "payloadView$core_iap2", "equals", "", "other", "hashCode", "toString", "", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2Parameter {
    public static final int HEADER_BYTES = 4;
    private final byte[] bytes;
    private final int id;

    public Iap2Parameter(int id, byte[] payload) {
        Intrinsics.checkNotNullParameter(payload, "payload");
        if (!(id >= 0 && id < 65536)) {
            throw new IllegalArgumentException("CSM parameter id must fit in u16".toString());
        }
        if (!(payload.length <= 65531)) {
            throw new IllegalArgumentException("CSM parameter payload exceeds 65531 bytes".toString());
        }
        this.id = id;
        byte[] bArrCopyOf = Arrays.copyOf(payload, payload.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        this.bytes = bArrCopyOf;
    }

    public final int getId() {
        return this.id;
    }

    public final byte[] getPayload() {
        byte[] bArr = this.bytes;
        byte[] bArrCopyOf = Arrays.copyOf(bArr, bArr.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        return bArrCopyOf;
    }

    /* JADX INFO: renamed from: payloadView$core_iap2, reason: from getter */
    public final byte[] getBytes() {
        return this.bytes;
    }

    public boolean equals(Object other) {
        return (other instanceof Iap2Parameter) && this.id == ((Iap2Parameter) other).id && Arrays.equals(this.bytes, ((Iap2Parameter) other).bytes);
    }

    public int hashCode() {
        return (this.id * 31) + Arrays.hashCode(this.bytes);
    }

    public String toString() {
        String string = Integer.toString(this.id, CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return "Iap2Parameter(id=0x" + StringsKt.padStart(string, 4, '0') + ", payload=" + this.bytes.length + " bytes)";
    }
}
