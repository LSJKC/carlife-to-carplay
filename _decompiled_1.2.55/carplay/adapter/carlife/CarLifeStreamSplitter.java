package com.carplay.adapter.carlife;

import com.carplay.sender.MonotonicAudioTimestamp;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CarLifeProtocol.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J$\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeStreamSplitter;", "", "<init>", "()V", "buffer", "", "feed", "", "Lcom/carplay/adapter/carlife/CarLifeStreamPacket;", "data", "offset", "", "length", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarLifeStreamSplitter {
    private byte[] buffer = new byte[0];

    public final List<CarLifeStreamPacket> feed(byte[] data, int offset, int length) {
        Intrinsics.checkNotNullParameter(data, "data");
        byte[] joined = new byte[this.buffer.length + length];
        System.arraycopy(this.buffer, 0, joined, 0, this.buffer.length);
        System.arraycopy(data, offset, joined, this.buffer.length, length);
        this.buffer = joined;
        ArrayList packets = new ArrayList();
        int at = 0;
        while (this.buffer.length - at >= 12) {
            int serviceType = ((this.buffer[at + 8] & UByte.MAX_VALUE) << 24) | ((this.buffer[at + 9] & UByte.MAX_VALUE) << 16) | ((this.buffer[at + 10] & UByte.MAX_VALUE) << 8) | (this.buffer[at + 11] & UByte.MAX_VALUE);
            if (CarLifeProtocol.INSTANCE.getSTREAM_IDS().contains(Integer.valueOf(serviceType))) {
                int payloadLength = ((this.buffer[at] & UByte.MAX_VALUE) << 24) | ((this.buffer[at + 1] & UByte.MAX_VALUE) << 16) | ((this.buffer[at + 2] & UByte.MAX_VALUE) << 8) | (this.buffer[at + 3] & UByte.MAX_VALUE);
                if (payloadLength < 0 || payloadLength > 67108864) {
                    at++;
                } else {
                    if ((this.buffer.length - at) - 12 < payloadLength) {
                        break;
                    }
                    if (payloadLength == 0) {
                        at += 12;
                    } else {
                        int timestamp = ((this.buffer[at + 4] & UByte.MAX_VALUE) << 24) | ((this.buffer[at + 5] & UByte.MAX_VALUE) << 16) | ((this.buffer[at + 6] & UByte.MAX_VALUE) << 8) | (this.buffer[at + 7] & UByte.MAX_VALUE);
                        packets.add(new CarLifeStreamPacket(serviceType, ArraysKt.copyOfRange(this.buffer, at + 12, at + 12 + payloadLength), ((long) timestamp) & MonotonicAudioTimestamp.RTP_MASK));
                        at += payloadLength + 12;
                    }
                }
            } else {
                at++;
            }
        }
        byte[] bArrCopyOfRange = this.buffer;
        if (at != 0) {
            bArrCopyOfRange = ArraysKt.copyOfRange(bArrCopyOfRange, at, this.buffer.length);
        }
        this.buffer = bArrCopyOfRange;
        return packets;
    }
}
