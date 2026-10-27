package com.carplay.airplay;

import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: Bytes.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a!\u0010\u0000\u001a\u00020\u00012\u0012\u0010\u0002\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00010\u0003\"\u00020\u0001H\u0000¢\u0006\u0002\u0010\u0004\u001a\f\u0010\u0005\u001a\u00020\u0001*\u00020\u0006H\u0000\u001a\f\u0010\u0007\u001a\u00020\u0006*\u00020\u0001H\u0000¨\u0006\b"}, d2 = {"concatBytes", "", "arrays", "", "([[B)[B", "asciiBytes", "", "toHexString", "core-airplay"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class BytesKt {
    public static final byte[] concatBytes(byte[]... arrays) {
        Intrinsics.checkNotNullParameter(arrays, "arrays");
        int length = 0;
        for (byte[] it : arrays) {
            length += it.length;
        }
        byte[] output = new byte[length];
        int offset = 0;
        for (byte[] array : arrays) {
            ArraysKt.copyInto$default(array, output, offset, 0, 0, 12, (Object) null);
            offset += array.length;
        }
        return output;
    }

    public static final byte[] asciiBytes(String $this$asciiBytes) {
        Intrinsics.checkNotNullParameter($this$asciiBytes, "<this>");
        byte[] bytes = $this$asciiBytes.getBytes(Charsets.US_ASCII);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        return bytes;
    }

    public static final String toHexString(byte[] $this$toHexString) {
        Intrinsics.checkNotNullParameter($this$toHexString, "<this>");
        char[] output = new char[$this$toHexString.length * 2];
        int length = $this$toHexString.length;
        for (int index = 0; index < length; index++) {
            int value = $this$toHexString[index] & UByte.MAX_VALUE;
            output[index * 2] = "0123456789abcdef".charAt(value >>> 4);
            output[(index * 2) + 1] = "0123456789abcdef".charAt(value & 15);
        }
        return new String(output);
    }
}
