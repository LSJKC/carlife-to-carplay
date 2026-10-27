package com.carplay.sender;

import com.carplay.airplay.AirPlayCrypto;
import com.carplay.airplay.BplistCodec;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.jmdns.impl.constants.DNSConstants;
import kotlin.Metadata;
import kotlin.TuplesKt;
import kotlin.collections.ArraysKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: VideoPackets.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0010\u0012\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\u0007\u001a\u00020\u0006J\u000e\u0010\b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006J\u0016\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006JF\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\b\b\u0002\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u00122\b\b\u0002\u0010\u0014\u001a\u00020\u000f2\b\b\u0002\u0010\u0015\u001a\u00020\u000fJ&\u0010\u0016\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0019J,\u0010\u001b\u001a\u00020\u00062\u0006\u0010\u001c\u001a\u00020\u001d2\b\b\u0002\u0010\u001e\u001a\u00020\u000f2\b\b\u0002\u0010\u001f\u001a\u00020\u000f2\b\b\u0002\u0010 \u001a\u00020!¨\u0006\""}, d2 = {"Lcom/carplay/sender/VideoPackets;", "", "<init>", "()V", "nals", "", "", "bytes", "lengthPrefixed", "avcC", "spsBytes", "ppsBytes", "config", "body", "width", "", "height", "displayX", "", "displayY", "displayWidth", "displayHeight", "frame", "key", "counter", "", "ntp", "keepAlive", "withStats", "", "framesPerSecond", "sentFrames", "bytesPerSecond", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class VideoPackets {
    public static final VideoPackets INSTANCE = new VideoPackets();

    private VideoPackets() {
    }

    public final List<byte[]> nals(byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        int length = bytes.length;
        if (!(1 <= length && length < 4194305)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        List result = new ArrayList();
        if (nals$start(bytes, 0) == 0) {
            int offset = 0;
            while (offset < bytes.length) {
                if (!(bytes.length - offset >= 4)) {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
                int size = ByteBuffer.wrap(bytes, offset, 4).getInt();
                int offset2 = offset + 4;
                if (!(1 <= size && size <= bytes.length - offset2)) {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
                result.add(ArraysKt.copyOfRange(bytes, offset2, offset2 + size));
                offset = offset2 + size;
            }
        } else {
            int begin = nals$start(bytes, 0);
            int cursor = begin;
            while (cursor < bytes.length) {
                int prefix = nals$start(bytes, cursor);
                if (prefix > 0) {
                    if (cursor > begin) {
                        result.add(ArraysKt.copyOfRange(bytes, begin, cursor));
                    }
                    begin = cursor + prefix;
                    cursor = begin;
                } else {
                    cursor++;
                }
            }
            if (begin < bytes.length) {
                result.add(ArraysKt.copyOfRange(bytes, begin, bytes.length));
            }
        }
        if (result.isEmpty()) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        return result;
    }

    private static final int nals$start(byte[] $bytes, int i) {
        if (i + 4 <= $bytes.length && $bytes[i] == 0 && $bytes[i + 1] == 0 && $bytes[i + 2] == 0 && $bytes[i + 3] == 1) {
            return 4;
        }
        return (i + 3 <= $bytes.length && $bytes[i] == 0 && $bytes[i + 1] == 0 && $bytes[i + 2] == 1) ? 3 : 0;
    }

    public final byte[] lengthPrefixed(byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        Iterable iterableNals = nals(bytes);
        Iterator<T> it = iterableNals.iterator();
        int length = 0;
        while (it.hasNext()) {
            length += ((byte[]) it.next()).length + 4;
        }
        ByteBuffer $this$lengthPrefixed_u24lambda_u242 = ByteBuffer.allocate(length);
        Iterable $this$forEach$iv = iterableNals;
        for (Object element$iv : $this$forEach$iv) {
            byte[] it2 = (byte[]) element$iv;
            $this$lengthPrefixed_u24lambda_u242.putInt(it2.length);
            $this$lengthPrefixed_u24lambda_u242.put(it2);
        }
        byte[] bArrArray = $this$lengthPrefixed_u24lambda_u242.array();
        Intrinsics.checkNotNullExpressionValue(bArrArray, "array(...)");
        return bArrArray;
    }

    public final byte[] avcC(byte[] spsBytes, byte[] ppsBytes) {
        Intrinsics.checkNotNullParameter(spsBytes, "spsBytes");
        Intrinsics.checkNotNullParameter(ppsBytes, "ppsBytes");
        Iterable $this$first$iv = nals(spsBytes);
        for (Object element$iv : $this$first$iv) {
            byte[] it = (byte[]) element$iv;
            boolean z = false;
            if ((it[0] & 31) == 7) {
                byte[] sps = (byte[]) element$iv;
                Iterable $this$first$iv2 = nals(ppsBytes);
                for (Object element$iv2 : $this$first$iv2) {
                    byte[] it2 = (byte[]) element$iv2;
                    if ((it2[0] & 31) == 8) {
                        byte[] pps = (byte[]) element$iv2;
                        int length = sps.length;
                        if (4 <= length && length < 65536) {
                            int length2 = pps.length;
                            if (1 <= length2 && length2 < 65536) {
                                z = true;
                            }
                        }
                        if (!z) {
                            throw new IllegalArgumentException("Failed requirement.".toString());
                        }
                        ByteBuffer $this$avcC_u24lambda_u245 = ByteBuffer.allocate(sps.length + 11 + pps.length);
                        $this$avcC_u24lambda_u245.put((byte) 1);
                        $this$avcC_u24lambda_u245.put(sps[1]);
                        $this$avcC_u24lambda_u245.put(sps[2]);
                        $this$avcC_u24lambda_u245.put(sps[3]);
                        $this$avcC_u24lambda_u245.put((byte) -1);
                        $this$avcC_u24lambda_u245.put((byte) -31);
                        $this$avcC_u24lambda_u245.putShort((short) sps.length);
                        $this$avcC_u24lambda_u245.put(sps);
                        $this$avcC_u24lambda_u245.put((byte) 1);
                        $this$avcC_u24lambda_u245.putShort((short) pps.length);
                        $this$avcC_u24lambda_u245.put(pps);
                        byte[] bArrArray = $this$avcC_u24lambda_u245.array();
                        Intrinsics.checkNotNullExpressionValue(bArrArray, "array(...)");
                        return bArrArray;
                    }
                }
                throw new NoSuchElementException("Collection contains no element matching the predicate.");
            }
        }
        throw new NoSuchElementException("Collection contains no element matching the predicate.");
    }

    public static /* synthetic */ byte[] config$default(VideoPackets videoPackets, byte[] bArr, int i, int i2, float f, float f2, int i3, int i4, int i5, Object obj) {
        if ((i5 & 8) != 0) {
            f = 0.0f;
        }
        if ((i5 & 16) != 0) {
            f2 = 0.0f;
        }
        if ((i5 & 32) != 0) {
            i3 = i;
        }
        if ((i5 & 64) != 0) {
            i4 = i2;
        }
        return videoPackets.config(bArr, i, i2, f, f2, i3, i4);
    }

    public final byte[] config(byte[] body, int width, int height, float displayX, float displayY, int displayWidth, int displayHeight) {
        Intrinsics.checkNotNullParameter(body, "body");
        if (width > 0 && height > 0 && displayWidth > 0 && displayHeight > 0) {
            if (!(displayX >= 0.0f && displayY >= 0.0f)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            if (!(body.length <= 65535)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            ByteBuffer header = ByteBuffer.allocate(128).order(ByteOrder.LITTLE_ENDIAN);
            header.putInt(0, body.length);
            header.put(4, (byte) 1);
            header.put(6, (byte) 4);
            header.putFloat(16, width);
            header.putFloat(20, height);
            header.putFloat(32, 0.0f);
            header.putFloat(36, 0.0f);
            header.putFloat(40, width);
            header.putFloat(44, height);
            header.putFloat(48, displayX);
            header.putFloat(52, displayY);
            header.putFloat(56, displayWidth);
            header.putFloat(60, displayHeight);
            byte[] bArrArray = header.array();
            Intrinsics.checkNotNullExpressionValue(bArrArray, "array(...)");
            return ArraysKt.plus(bArrArray, body);
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }

    public final byte[] frame(byte[] body, byte[] key, long counter, long ntp) {
        Intrinsics.checkNotNullParameter(body, "body");
        Intrinsics.checkNotNullParameter(key, "key");
        int length = body.length;
        if (!((1 <= length && length < 4194305) && key.length == 32 && counter >= 0)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        ByteBuffer $this$frame_u24lambda_u246 = ByteBuffer.allocate(128).order(ByteOrder.LITTLE_ENDIAN);
        $this$frame_u24lambda_u246.putInt(0, body.length + 16);
        $this$frame_u24lambda_u246.putLong(8, ntp);
        byte[] header = $this$frame_u24lambda_u246.array();
        Intrinsics.checkNotNull(header);
        return ArraysKt.plus(header, AirPlayCrypto.INSTANCE.chachaSeal(key, AirPlayCrypto.INSTANCE.nonce64(counter), body, header));
    }

    public static /* synthetic */ byte[] keepAlive$default(VideoPackets videoPackets, boolean z, int i, int i2, double d, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = 0;
        }
        if ((i3 & 8) != 0) {
            d = 0.0d;
        }
        return videoPackets.keepAlive(z, i, i2, d);
    }

    public final byte[] keepAlive(boolean withStats, int framesPerSecond, int sentFrames, double bytesPerSecond) {
        byte[] body = withStats ? BplistCodec.INSTANCE.encode(MapsKt.linkedMapOf(TuplesKt.to("txUsageAvg", Double.valueOf(bytesPerSecond)), TuplesKt.to("encoderCurrentFPS", Long.valueOf(framesPerSecond)), TuplesKt.to("sentFramesAvg", Long.valueOf(sentFrames)), TuplesKt.to("queuedFramesAvg", 0L), TuplesKt.to("lossAvg", Double.valueOf(0.0d)))) : new byte[0];
        byte[] header = new byte[128];
        ByteBuffer little = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN);
        little.putInt(0, body.length);
        header[4] = (byte) (body.length == 0 ? 2 : 5);
        if (!(body.length == 0)) {
            little.putFloat(DNSConstants.KNOWN_ANSWER_TTL, 0.0f);
            little.putFloat(124, body.length);
        }
        return ArraysKt.plus(header, body);
    }
}
