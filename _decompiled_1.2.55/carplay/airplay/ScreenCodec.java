package com.carplay.airplay;

import com.carplay.adapter.carlife.KeyFrameRebuildGate;
import com.carplay.sender.MonotonicAudioTimestamp;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: ScreenStream.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007J&\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0007J\u000e\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0007J\u001a\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00070\u00112\u0006\u0010\u000f\u001a\u00020\u0007J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u000f\u001a\u00020\u0007H\u0002J\u0018\u0010\u0015\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\tH\u0002R\u000e\u0010\b\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\tX\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0019"}, d2 = {"Lcom/carplay/airplay/ScreenCodec;", "", "<init>", "()V", "senderNanos", "", "header", "", "SENDER_TIME_OFFSET", "", "decryptFrame", "key", "counter", "body", "lengthPrefixedToAnnexB", "payload", "detectConfig", "Lkotlin/Pair;", "Lcom/carplay/airplay/VideoCodec;", "looksLikeAvcC", "", "readU16Be", "source", "offset", "TAG_SIZE", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ScreenCodec {
    public static final ScreenCodec INSTANCE = new ScreenCodec();
    private static final int SENDER_TIME_OFFSET = 8;
    public static final int TAG_SIZE = 16;

    private ScreenCodec() {
    }

    public final long senderNanos(byte[] header) {
        Intrinsics.checkNotNullParameter(header, "header");
        if (header.length < 16) {
            return 0L;
        }
        long raw = 0;
        for (int index = 7; -1 < index; index--) {
            raw = (raw << 8) | (((long) header[index + 8]) & 255);
        }
        return ((raw >>> 32) * KeyFrameRebuildGate.RETRY_MIN_INTERVAL_NANOS) + (((MonotonicAudioTimestamp.RTP_MASK & raw) * KeyFrameRebuildGate.RETRY_MIN_INTERVAL_NANOS) >>> 32);
    }

    public final byte[] decryptFrame(byte[] key, long counter, byte[] header, byte[] body) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(header, "header");
        Intrinsics.checkNotNullParameter(body, "body");
        return body.length < 16 ? body : AirPlayCrypto.INSTANCE.chachaOpen(key, AirPlayCrypto.INSTANCE.nonce64(counter), body, header);
    }

    public final byte[] lengthPrefixedToAnnexB(byte[] payload) {
        Intrinsics.checkNotNullParameter(payload, "payload");
        if (payload.length < 4 || ScreenStreamKt.startsWithStartCode(payload)) {
            return payload;
        }
        int offset = 0;
        while (offset + 4 <= payload.length) {
            int length = ScreenStreamKt.readU32Be(payload, offset);
            int offset2 = offset + 4;
            if (length <= 0 || offset2 + length > payload.length) {
                return payload;
            }
            offset = offset2 + length;
        }
        int length2 = payload.length;
        if (offset != length2) {
            return payload;
        }
        int offset3 = 0;
        while (offset3 + 4 <= payload.length) {
            int length3 = ScreenStreamKt.readU32Be(payload, offset3);
            payload[offset3] = 0;
            payload[offset3 + 1] = 0;
            payload[offset3 + 2] = 0;
            payload[offset3 + 3] = 1;
            offset3 += length3 + 4;
        }
        return payload;
    }

    public final Pair<VideoCodec, byte[]> detectConfig(byte[] payload) {
        Intrinsics.checkNotNullParameter(payload, "payload");
        int index = 4;
        int length = payload.length - 4;
        if (4 <= length) {
            while (true) {
                String fourcc = new String(payload, index, 4, Charsets.US_ASCII);
                if (!Intrinsics.areEqual(fourcc, "hvcC")) {
                    if (!Intrinsics.areEqual(fourcc, "avcC")) {
                        if (index == length) {
                            break;
                        }
                        index++;
                    } else {
                        return TuplesKt.to(VideoCodec.H264, ArraysKt.copyOfRange(payload, index + 4, payload.length));
                    }
                } else {
                    return TuplesKt.to(VideoCodec.H265, ArraysKt.copyOfRange(payload, index + 4, payload.length));
                }
            }
        }
        return TuplesKt.to(looksLikeAvcC(payload) ? VideoCodec.H264 : VideoCodec.H265, payload);
    }

    private final boolean looksLikeAvcC(byte[] payload) {
        if (payload.length < 9 || (payload[5] & 31) < 1) {
            return false;
        }
        int spsLength = readU16Be(payload, 6);
        return spsLength + 8 <= payload.length && (payload[8] & 31) == 7;
    }

    private final int readU16Be(byte[] source, int offset) {
        return ((source[offset] & UByte.MAX_VALUE) << 8) | (source[offset + 1] & UByte.MAX_VALUE);
    }
}
