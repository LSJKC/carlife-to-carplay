package com.carplay.iap2.transport;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: UsbMuxFrameBuffer.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0012\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0080\b\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0004\b\t\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0016\u001a\u00020\bHÆ\u0003J;\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\bHÆ\u0001J\u0013\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001b\u001a\u00020\u0003HÖ\u0001J\t\u0010\u001c\u001a\u00020\u001dHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\fR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\fR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u001e"}, d2 = {"Lcom/carplay/iap2/transport/UsbMuxFrame;", "", "protocol", "", "length", "word8", "sequence", "payload", "", "<init>", "(IIII[B)V", "getProtocol", "()I", "getLength", "getWord8", "getSequence", "getPayload", "()[B", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class UsbMuxFrame {
    private final int length;
    private final byte[] payload;
    private final int protocol;
    private final int sequence;
    private final int word8;

    public static /* synthetic */ UsbMuxFrame copy$default(UsbMuxFrame usbMuxFrame, int i, int i2, int i3, int i4, byte[] bArr, int i5, Object obj) {
        if ((i5 & 1) != 0) {
            i = usbMuxFrame.protocol;
        }
        if ((i5 & 2) != 0) {
            i2 = usbMuxFrame.length;
        }
        if ((i5 & 4) != 0) {
            i3 = usbMuxFrame.word8;
        }
        if ((i5 & 8) != 0) {
            i4 = usbMuxFrame.sequence;
        }
        if ((i5 & 16) != 0) {
            bArr = usbMuxFrame.payload;
        }
        byte[] bArr2 = bArr;
        int i6 = i3;
        return usbMuxFrame.copy(i, i2, i6, i4, bArr2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getProtocol() {
        return this.protocol;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getLength() {
        return this.length;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int getWord8() {
        return this.word8;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int getSequence() {
        return this.sequence;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final byte[] getPayload() {
        return this.payload;
    }

    public final UsbMuxFrame copy(int protocol, int length, int word8, int sequence, byte[] payload) {
        Intrinsics.checkNotNullParameter(payload, "payload");
        return new UsbMuxFrame(protocol, length, word8, sequence, payload);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UsbMuxFrame)) {
            return false;
        }
        UsbMuxFrame usbMuxFrame = (UsbMuxFrame) other;
        return this.protocol == usbMuxFrame.protocol && this.length == usbMuxFrame.length && this.word8 == usbMuxFrame.word8 && this.sequence == usbMuxFrame.sequence && Intrinsics.areEqual(this.payload, usbMuxFrame.payload);
    }

    public int hashCode() {
        return (((((((Integer.hashCode(this.protocol) * 31) + Integer.hashCode(this.length)) * 31) + Integer.hashCode(this.word8)) * 31) + Integer.hashCode(this.sequence)) * 31) + Arrays.hashCode(this.payload);
    }

    public String toString() {
        return "UsbMuxFrame(protocol=" + this.protocol + ", length=" + this.length + ", word8=" + this.word8 + ", sequence=" + this.sequence + ", payload=" + Arrays.toString(this.payload) + ")";
    }

    public UsbMuxFrame(int protocol, int length, int word8, int sequence, byte[] payload) {
        Intrinsics.checkNotNullParameter(payload, "payload");
        this.protocol = protocol;
        this.length = length;
        this.word8 = word8;
        this.sequence = sequence;
        this.payload = payload;
    }

    public final int getProtocol() {
        return this.protocol;
    }

    public final int getLength() {
        return this.length;
    }

    public final int getWord8() {
        return this.word8;
    }

    public final int getSequence() {
        return this.sequence;
    }

    public final byte[] getPayload() {
        return this.payload;
    }
}
