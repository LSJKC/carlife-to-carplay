package com.carplay.iap2.wire;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Iap2Wire.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u0006\u0010\n\u001a\u00020\rJ\u0006\u0010\u000e\u001a\u00020\u0005J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u0012\u001a\u00020\u0003H\u0016J\b\u0010\u0013\u001a\u00020\u0014H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0004\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\f¨\u0006\u0015"}, d2 = {"Lcom/carplay/iap2/wire/Iap2Frame;", "", "messageId", "", "payload", "", "<init>", "(I[B)V", "getMessageId", "()I", "body", "getPayload", "()[B", "Lcom/carplay/iap2/wire/Iap2ParameterList;", "encodedFrame", "equals", "", "other", "hashCode", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2Frame {
    private final byte[] body;
    private final int messageId;

    public Iap2Frame(int messageId, byte[] payload) {
        Intrinsics.checkNotNullParameter(payload, "payload");
        if (!(messageId >= 0 && messageId < 65536)) {
            throw new IllegalArgumentException("CSM message id must fit in u16".toString());
        }
        if (!(payload.length <= 65529)) {
            throw new IllegalArgumentException("CSM payload exceeds 65529 bytes".toString());
        }
        this.messageId = messageId;
        byte[] bArrCopyOf = Arrays.copyOf(payload, payload.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        this.body = bArrCopyOf;
    }

    public final int getMessageId() {
        return this.messageId;
    }

    public final byte[] getPayload() {
        byte[] bArr = this.body;
        byte[] bArrCopyOf = Arrays.copyOf(bArr, bArr.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        return bArrCopyOf;
    }

    public final Iap2ParameterList body() {
        return Iap2ParameterList.INSTANCE.parse(this.body);
    }

    public final byte[] encodedFrame() {
        return Iap2CsmFramer.INSTANCE.encodeFrame(this.messageId, this.body);
    }

    public boolean equals(Object other) {
        return (other instanceof Iap2Frame) && this.messageId == ((Iap2Frame) other).messageId && Arrays.equals(this.body, ((Iap2Frame) other).body);
    }

    public int hashCode() {
        return (this.messageId * 31) + Arrays.hashCode(this.body);
    }

    public String toString() {
        String string = Integer.toString(this.messageId, CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return "Iap2Frame(messageId=0x" + StringsKt.padStart(string, 4, '0') + ", payload=" + this.body.length + " bytes)";
    }
}
