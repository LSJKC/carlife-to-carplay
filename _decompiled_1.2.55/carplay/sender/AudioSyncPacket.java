package com.carplay.sender;

import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AudioSyncPacket.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J.\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\u0010J \u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\t2\u0006\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\fH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0016"}, d2 = {"Lcom/carplay/sender/AudioSyncPacket;", "", "<init>", "()V", "SIZE", "", "PAYLOAD_TYPE", "FIRST_FLAG", "build", "", "sequence", "currentTimestamp", "", "nextTimestamp", "ntp", "first", "", "writeU32", "", "target", "offset", "value", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AudioSyncPacket {
    private static final int FIRST_FLAG = 16;
    public static final AudioSyncPacket INSTANCE = new AudioSyncPacket();
    private static final int PAYLOAD_TYPE = 212;
    public static final int SIZE = 20;

    private AudioSyncPacket() {
    }

    public final byte[] build(int sequence, long currentTimestamp, long nextTimestamp, byte[] ntp, boolean first) {
        Intrinsics.checkNotNullParameter(ntp, "ntp");
        if (!(ntp.length == 8)) {
            throw new IllegalArgumentException("NTP timestamp must be 8 bytes".toString());
        }
        byte[] packet = new byte[20];
        packet[0] = (byte) ((first ? 16 : 0) | 128);
        packet[1] = -44;
        packet[2] = (byte) (sequence >>> 8);
        packet[3] = (byte) sequence;
        writeU32(packet, 4, currentTimestamp);
        ArraysKt.copyInto$default(ntp, packet, 8, 0, 0, 12, (Object) null);
        writeU32(packet, 16, nextTimestamp);
        return packet;
    }

    private final void writeU32(byte[] target, int offset, long value) {
        int v = (int) value;
        target[offset] = (byte) (v >>> 24);
        target[offset + 1] = (byte) (v >>> 16);
        target[offset + 2] = (byte) (v >>> 8);
        target[offset + 3] = (byte) v;
    }
}
