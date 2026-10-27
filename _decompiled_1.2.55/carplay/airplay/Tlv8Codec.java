package com.carplay.airplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Tlv8Codec.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0014\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nJ\u001a\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\b0\r2\u0006\u0010\u000e\u001a\u00020\bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lcom/carplay/airplay/Tlv8Codec;", "", "<init>", "()V", "SEPARATOR_TYPE", "", "MAX_FRAGMENT", "encode", "", "items", "", "Lcom/carplay/airplay/Tlv8Item;", "decode", "", "buffer", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Tlv8Codec {
    public static final Tlv8Codec INSTANCE = new Tlv8Codec();
    private static final int MAX_FRAGMENT = 255;
    private static final int SEPARATOR_TYPE = 255;

    private Tlv8Codec() {
    }

    public final byte[] encode(List<Tlv8Item> items) {
        Intrinsics.checkNotNullParameter(items, "items");
        ArrayList chunks = new ArrayList();
        Integer previousType = null;
        for (Tlv8Item item : items) {
            int type = item.getType();
            if (previousType != null && previousType.intValue() == type) {
                chunks.add(new byte[]{-1, 0});
            }
            int offset = 0;
            do {
                int length = Math.min(255, item.getValue().length - offset);
                chunks.add(new byte[]{(byte) item.getType(), (byte) length});
                chunks.add(ArraysKt.copyOfRange(item.getValue(), offset, offset + length));
                offset += length;
            } while (offset < item.getValue().length);
            previousType = Integer.valueOf(item.getType());
        }
        ArrayList $this$toTypedArray$iv = chunks;
        byte[][] bArr = (byte[][]) $this$toTypedArray$iv.toArray(new byte[0][]);
        return BytesKt.concatBytes((byte[][]) Arrays.copyOf(bArr, bArr.length));
    }

    public final Map<Integer, byte[]> decode(byte[] buffer) {
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        HashMap output = new HashMap();
        int position = 0;
        Integer lastType = null;
        int lastLength = 0;
        while (position + 2 <= buffer.length) {
            int type = buffer[position] & UByte.MAX_VALUE;
            int length = buffer[position + 1] & UByte.MAX_VALUE;
            int position2 = position + 2;
            if (position2 + length > buffer.length) {
                break;
            }
            byte[] value = ArraysKt.copyOfRange(buffer, position2, position2 + length);
            position = position2 + length;
            if (lastType != null && type == lastType.intValue() && lastLength == 255) {
                byte[] previous = (byte[]) output.get(Integer.valueOf(type));
                output.put(Integer.valueOf(type), previous != null ? ArraysKt.plus(previous, value) : value);
            } else {
                output.put(Integer.valueOf(type), value);
            }
            lastType = Integer.valueOf(type);
            lastLength = length;
        }
        return output;
    }
}
