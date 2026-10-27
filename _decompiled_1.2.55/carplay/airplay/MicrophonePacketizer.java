package com.carplay.airplay;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.scheduling.WorkQueueKt;
import org.bouncycastle.crypto.InvalidCipherTextException;

/* JADX INFO: compiled from: MicrophonePacketizer.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J.\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\u0005J\u000e\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\tJ \u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u0005H\u0002J \u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u0005H\u0002J \u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u0019H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Lcom/carplay/airplay/MicrophonePacketizer;", "", "<init>", "()V", "RTP_HEADER_LEN", "", "TAG_LEN", "NONCE_LEN", "sealPacket", "", "key", "payloadType", "counters", "Lcom/carplay/airplay/MicrophoneCounters;", "body", "samples", "toWirePcm", "pcmLittleEndian", "putU16Be", "", "target", "offset", "value", "putU32Be", "putU64Le", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MicrophonePacketizer {
    public static final MicrophonePacketizer INSTANCE = new MicrophonePacketizer();
    public static final int NONCE_LEN = 8;
    public static final int RTP_HEADER_LEN = 12;
    public static final int TAG_LEN = 16;

    private MicrophonePacketizer() {
    }

    public final byte[] sealPacket(byte[] key, int payloadType, MicrophoneCounters counters, byte[] body, int samples) throws InvalidCipherTextException {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(counters, "counters");
        Intrinsics.checkNotNullParameter(body, "body");
        byte[] header = new byte[12];
        header[0] = ByteCompanionObject.MIN_VALUE;
        header[1] = (byte) (payloadType & WorkQueueKt.MASK);
        putU16Be(header, 2, counters.getSequence());
        putU32Be(header, 4, counters.getTimestamp());
        byte[] nonce = new byte[12];
        putU64Le(nonce, 4, counters.getNonce());
        byte[] sealed = AirPlayCrypto.INSTANCE.chachaSeal(key, nonce, body, ArraysKt.copyOfRange(header, 4, 12));
        byte[] packet = new byte[sealed.length + 12 + 8];
        ArraysKt.copyInto$default(header, packet, 0, 0, 0, 12, (Object) null);
        ArraysKt.copyInto$default(sealed, packet, 12, 0, 0, 12, (Object) null);
        putU64Le(packet, sealed.length + 12, counters.getNonce());
        counters.setSequence((counters.getSequence() + 1) & 65535);
        counters.setTimestamp(counters.getTimestamp() + samples);
        counters.setNonce(counters.getNonce() + 1);
        return packet;
    }

    public final byte[] toWirePcm(byte[] pcmLittleEndian) {
        Intrinsics.checkNotNullParameter(pcmLittleEndian, "pcmLittleEndian");
        byte[] output = Arrays.copyOf(pcmLittleEndian, pcmLittleEndian.length);
        Intrinsics.checkNotNullExpressionValue(output, "copyOf(...)");
        for (int index = 0; index + 1 < output.length; index += 2) {
            byte first = output[index];
            output[index] = output[index + 1];
            output[index + 1] = first;
        }
        return output;
    }

    private final void putU16Be(byte[] target, int offset, int value) {
        target[offset] = (byte) (value >>> 8);
        target[offset + 1] = (byte) value;
    }

    private final void putU32Be(byte[] target, int offset, int value) {
        target[offset] = (byte) (value >>> 24);
        target[offset + 1] = (byte) (value >>> 16);
        target[offset + 2] = (byte) (value >>> 8);
        target[offset + 3] = (byte) value;
    }

    private final void putU64Le(byte[] target, int offset, long value) {
        long remaining = value;
        for (int index = 0; index < 8; index++) {
            target[offset + index] = (byte) remaining;
            remaining >>>= 8;
        }
    }
}
