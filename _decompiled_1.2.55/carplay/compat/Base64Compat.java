package com.carplay.compat;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Base64Compat.kt */
/* JADX INFO: loaded from: classes2.dex */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\nJ\u001e\u0010\u000b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0005J\u000e\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u0005J\u000e\u0010\u0011\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\nJ-\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0013\u001a\u00020\u00142\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\r0\u0016H\u0082\bJ\u0010\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\rH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0019"}, d2 = {"Lcom/carplay/compat/Base64Compat;", "", "<init>", "()V", "ALPHABET", "", "DECODE", "", "encode", "bytes", "", "encodeLines", "lineLength", "", "separator", "decode", "encoded", "decodeMime", "length", "ignoreOtherCharacters", "", "charAt", "Lkotlin/Function1;", "decodeValue", "code", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Base64Compat {
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    private static final int[] DECODE;
    public static final Base64Compat INSTANCE = new Base64Compat();

    private Base64Compat() {
    }

    static {
        int[] table = new int[128];
        int i = 0;
        for (int i2 = 0; i2 < 128; i2++) {
            table[i2] = -1;
        }
        int index$iv = 0;
        while (i < $this$forEachIndexed$iv.length()) {
            char item$iv = $this$forEachIndexed$iv.charAt(i);
            table[item$iv] = index$iv;
            i++;
            index$iv++;
        }
        DECODE = table;
    }

    public final String encode(byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        StringBuilder out = new StringBuilder(((bytes.length + 2) / 3) * 4);
        int index = 0;
        while (index + 3 <= bytes.length) {
            int bits = ((bytes[index] & UByte.MAX_VALUE) << 16) | ((bytes[index + 1] & UByte.MAX_VALUE) << 8) | (bytes[index + 2] & UByte.MAX_VALUE);
            out.append(ALPHABET.charAt((bits >>> 18) & 63)).append(ALPHABET.charAt((bits >>> 12) & 63)).append(ALPHABET.charAt((bits >>> 6) & 63)).append(ALPHABET.charAt(bits & 63));
            index += 3;
        }
        switch (bytes.length - index) {
            case 1:
                int bits2 = (bytes[index] & UByte.MAX_VALUE) << 16;
                out.append(ALPHABET.charAt((bits2 >>> 18) & 63)).append(ALPHABET.charAt((bits2 >>> 12) & 63)).append("==");
                break;
            case 2:
                int bits3 = ((bytes[index] & UByte.MAX_VALUE) << 16) | ((bytes[index + 1] & UByte.MAX_VALUE) << 8);
                out.append(ALPHABET.charAt((bits3 >>> 18) & 63)).append(ALPHABET.charAt((bits3 >>> 12) & 63)).append(ALPHABET.charAt((bits3 >>> 6) & 63)).append('=');
                break;
        }
        String string = out.toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    public final String encodeLines(byte[] bytes, int lineLength, String separator) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        Intrinsics.checkNotNullParameter(separator, "separator");
        if (lineLength > 0 && lineLength % 4 == 0) {
            return CollectionsKt.joinToString$default(StringsKt.chunked(encode(bytes), lineLength), separator, null, null, 0, null, null, 62, null);
        }
        throw new IllegalArgumentException("Line length must be a positive multiple of 4".toString());
    }

    /* JADX WARN: Code duplicated, block: B:13:0x003e A[PHI: r9
  0x003e: PHI (r9v3 'it' int) = (r9v2 'it' int), (r9v9 'it' int) binds: [B:6:0x002b, B:10:0x0039] A[DONT_GENERATE, DONT_INLINE]] */
    public final byte[] decode(String encoded) {
        boolean complete$iv;
        encoded = encoded;
        Intrinsics.checkNotNullParameter(encoded, "encoded");
        int length$iv = encoded.length();
        byte[] out$iv = new byte[((length$iv / 4) * 3) + 2];
        int size$iv = 0;
        int it = 0;
        int bits$iv = 0;
        int count$iv = 0;
        while (it < length$iv) {
            it++;
            int code$iv = encoded.charAt(it);
            if (code$iv == 61) {
                if (count$iv != 3) {
                    if (count$iv == 2 && it < length$iv) {
                        char cCharAt = encoded.charAt(it);
                        it++;
                        complete$iv = cCharAt == '=';
                    }
                }
                int index$iv = it;
                if (!complete$iv) {
                    throw new IllegalArgumentException("Input byte array has wrong 4-byte ending unit".toString());
                }
                if (index$iv >= length$iv) {
                    break;
                }
                encoded.charAt(index$iv);
                throw new IllegalArgumentException(("Input byte array has incorrect ending byte at " + ((index$iv + 1) - 1)).toString());
            }
            int value$iv = decodeValue(code$iv);
            if (value$iv < 0) {
                throw new IllegalArgumentException("Illegal base64 character " + Integer.toHexString(code$iv));
            }
            bits$iv = (bits$iv << 6) | value$iv;
            count$iv++;
            if (count$iv == 4) {
                int size$iv2 = size$iv + 1;
                out$iv[size$iv] = (byte) (bits$iv >> 16);
                int size$iv3 = size$iv2 + 1;
                out$iv[size$iv2] = (byte) (bits$iv >> 8);
                out$iv[size$iv3] = (byte) bits$iv;
                bits$iv = 0;
                count$iv = 0;
                size$iv = size$iv3 + 1;
            }
        }
        switch (count$iv) {
            case 1:
                throw new IllegalArgumentException("Last unit does not have enough valid bits");
            case 2:
                out$iv[size$iv] = (byte) (bits$iv >> 4);
                size$iv++;
                break;
            case 3:
                int size$iv4 = size$iv + 1;
                out$iv[size$iv] = (byte) (bits$iv >> 10);
                size$iv = size$iv4 + 1;
                out$iv[size$iv4] = (byte) (bits$iv >> 2);
                break;
        }
        if (size$iv == out$iv.length) {
            return out$iv;
        }
        byte[] out$iv2 = Arrays.copyOf(out$iv, size$iv);
        Intrinsics.checkNotNullExpressionValue(out$iv2, "copyOf(...)");
        ArraysKt.fill$default(out$iv, (byte) 0, 0, 0, 6, (Object) null);
        return out$iv2;
    }

    /* JADX WARN: Code duplicated, block: B:14:0x003c A[PHI: r9
  0x003c: PHI (r9v3 'it' int) = (r9v2 'it' int), (r9v8 'it' int) binds: [B:7:0x0029, B:11:0x0037] A[DONT_GENERATE, DONT_INLINE]] */
    public final byte[] decodeMime(byte[] encoded) {
        boolean complete$iv;
        encoded = encoded;
        Intrinsics.checkNotNullParameter(encoded, "encoded");
        int length$iv = encoded.length;
        byte[] out$iv = new byte[((length$iv / 4) * 3) + 2];
        int size$iv = 0;
        int it = 0;
        int bits$iv = 0;
        int count$iv = 0;
        while (it < length$iv) {
            it++;
            int i = encoded[it] & UByte.MAX_VALUE;
            if (i == 61) {
                if (count$iv != 3) {
                    if (count$iv == 2 && it < length$iv) {
                        int index$iv = it + 1;
                        int i2 = encoded[it] & 255;
                        it = index$iv;
                        complete$iv = i2 == 61;
                    }
                }
                if (!complete$iv) {
                    throw new IllegalArgumentException("Input byte array has wrong 4-byte ending unit".toString());
                }
                int index$iv2 = it;
                while (index$iv2 < length$iv) {
                    int index$iv3 = index$iv2 + 1;
                    if (!(decodeValue(encoded[index$iv2] & UByte.MAX_VALUE) < 0)) {
                        throw new IllegalArgumentException(("Input byte array has incorrect ending byte at " + (index$iv3 - 1)).toString());
                    }
                    index$iv2 = index$iv3;
                }
                break;
            }
            int value$iv = decodeValue(i);
            if (value$iv >= 0) {
                bits$iv = (bits$iv << 6) | value$iv;
                count$iv++;
                if (count$iv == 4) {
                    int size$iv2 = size$iv + 1;
                    out$iv[size$iv] = (byte) (bits$iv >> 16);
                    int size$iv3 = size$iv2 + 1;
                    out$iv[size$iv2] = (byte) (bits$iv >> 8);
                    out$iv[size$iv3] = (byte) bits$iv;
                    bits$iv = 0;
                    count$iv = 0;
                    size$iv = size$iv3 + 1;
                }
            }
        }
        switch (count$iv) {
            case 1:
                throw new IllegalArgumentException("Last unit does not have enough valid bits");
            case 2:
                out$iv[size$iv] = (byte) (bits$iv >> 4);
                size$iv++;
                break;
            case 3:
                int size$iv4 = size$iv + 1;
                out$iv[size$iv] = (byte) (bits$iv >> 10);
                size$iv = size$iv4 + 1;
                out$iv[size$iv4] = (byte) (bits$iv >> 2);
                break;
        }
        if (size$iv == out$iv.length) {
            return out$iv;
        }
        byte[] out$iv2 = Arrays.copyOf(out$iv, size$iv);
        Intrinsics.checkNotNullExpressionValue(out$iv2, "copyOf(...)");
        ArraysKt.fill$default(out$iv, (byte) 0, 0, 0, 6, (Object) null);
        return out$iv2;
    }

    /* JADX WARN: Code duplicated, block: B:14:0x0049 A[PHI: r8
  0x0049: PHI (r8v11 'index' int) = (r8v10 'index' int), (r8v18 'index' int) binds: [B:7:0x002d, B:11:0x0044] A[DONT_GENERATE, DONT_INLINE]] */
    private final byte[] decode(int length, boolean ignoreOtherCharacters, Function1<? super Integer, Integer> charAt) {
        boolean complete;
        byte[] out = new byte[((length / 4) * 3) + 2];
        int size = 0;
        int index = 0;
        int bits = 0;
        int count = 0;
        while (index < length) {
            index++;
            int code = charAt.invoke(Integer.valueOf(index)).intValue();
            if (code == 61) {
                if (count == 3) {
                    complete = true;
                } else {
                    if (count == 2 && index < length) {
                        int iIntValue = charAt.invoke(Integer.valueOf(index)).intValue();
                        index++;
                        if (iIntValue == 61) {
                            complete = true;
                        }
                    }
                    complete = false;
                }
                if (!complete) {
                    throw new IllegalArgumentException("Input byte array has wrong 4-byte ending unit".toString());
                }
                int index2 = index;
                while (index2 < length) {
                    int index3 = index2 + 1;
                    int trailing = charAt.invoke(Integer.valueOf(index2)).intValue();
                    if (!(ignoreOtherCharacters && decodeValue(trailing) < 0)) {
                        throw new IllegalArgumentException(("Input byte array has incorrect ending byte at " + (index3 - 1)).toString());
                    }
                    index2 = index3;
                }
                break;
            }
            int value = decodeValue(code);
            if (value < 0) {
                if (!ignoreOtherCharacters) {
                    throw new IllegalArgumentException("Illegal base64 character " + Integer.toHexString(code));
                }
            } else {
                bits = (bits << 6) | value;
                count++;
                if (count == 4) {
                    int size2 = size + 1;
                    out[size] = (byte) (bits >> 16);
                    int size3 = size2 + 1;
                    out[size2] = (byte) (bits >> 8);
                    out[size3] = (byte) bits;
                    bits = 0;
                    count = 0;
                    size = size3 + 1;
                }
            }
        }
        switch (count) {
            case 1:
                throw new IllegalArgumentException("Last unit does not have enough valid bits");
            case 2:
                out[size] = (byte) (bits >> 4);
                size++;
                break;
            case 3:
                int size4 = size + 1;
                out[size] = (byte) (bits >> 10);
                size = size4 + 1;
                out[size4] = (byte) (bits >> 2);
                break;
        }
        if (size == out.length) {
            return out;
        }
        byte[] bArrCopyOf = Arrays.copyOf(out, size);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        ArraysKt.fill$default(out, (byte) 0, 0, 0, 6, (Object) null);
        return bArrCopyOf;
    }

    private final int decodeValue(int code) {
        boolean z = false;
        if (code >= 0 && code < 128) {
            z = true;
        }
        if (z) {
            return DECODE[code];
        }
        return -1;
    }
}
