package com.carplay.iap2.wire;

import com.carplay.sender.MonotonicAudioTimestamp;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Iap2WireCodec.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0002\b\u000f\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0006\u0010\u0004\u001a\u00020\u0005J\u000e\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\n\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\r\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u000fJ\u000e\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u0010\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u000fJ\u000e\u0010\u0012\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005J\u000e\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0014J\u0014\u0010\u0015\u001a\u00020\u00052\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00140\u0017J\u0014\u0010\u0018\u001a\u00020\u00052\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\b0\u0017J\u0018\u0010\u0019\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u00052\b\b\u0002\u0010\u001a\u001a\u00020\bJ\u0018\u0010\u001b\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u00052\b\b\u0002\u0010\u001a\u001a\u00020\bJ\"\u0010\u001c\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u00052\b\b\u0002\u0010\u001a\u001a\u00020\b2\b\b\u0002\u0010\u001d\u001a\u00020\u000bJ\u0018\u0010\u001e\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u00052\b\b\u0002\u0010\u001a\u001a\u00020\bJ\u0018\u0010\u001f\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u00052\b\b\u0002\u0010\u001a\u001a\u00020\bJ\u0018\u0010 \u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u00052\b\b\u0002\u0010\u001a\u001a\u00020\bJ\u0018\u0010!\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u00052\b\b\u0002\u0010\u001a\u001a\u00020\bJ\u0018\u0010\"\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u00052\b\b\u0002\u0010\u001a\u001a\u00020\bJ\u000e\u0010#\u001a\u00020\u00052\u0006\u0010$\u001a\u00020\u0005J\u000e\u0010%\u001a\u00020\u00142\u0006\u0010$\u001a\u00020\u0005J\u0014\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00140'2\u0006\u0010$\u001a\u00020\u0005J\u0014\u0010(\u001a\b\u0012\u0004\u0012\u00020\b0'2\u0006\u0010$\u001a\u00020\u0005J\u0010\u0010\u0006\u001a\u00020\b2\u0006\u0010)\u001a\u00020*H\u0002¨\u0006+"}, d2 = {"Lcom/carplay/iap2/wire/Iap2WireCodec;", "", "<init>", "()V", "void", "", "u8", "value", "", "i8", "bool", "", "u16", "i16", "u32", "", "i32", "u64", "bytes", "string", "", "strings", "values", "", "u16List", "readU8", "offset", "readI8", "readBool", "nonzeroIsTrue", "readU16", "readI16", "readU32", "readI32", "readU64", "readBytes", "payload", "readString", "readStrings", "", "readU16List", "byte", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2WireCodec {
    public static final Iap2WireCodec INSTANCE = new Iap2WireCodec();

    private Iap2WireCodec() {
    }

    /* JADX INFO: renamed from: void, reason: not valid java name */
    public final byte[] m55void() {
        return new byte[0];
    }

    public final byte[] u8(int value) {
        if (value >= 0 && value < 256) {
            return new byte[]{(byte) value};
        }
        throw new IllegalArgumentException("u8 value must be in 0..255".toString());
    }

    public final byte[] i8(int value) {
        if (-128 <= value && value < 128) {
            return new byte[]{(byte) value};
        }
        throw new IllegalArgumentException("i8 value must be in -128..127".toString());
    }

    public final byte[] bool(boolean value) {
        return u8(value ? 1 : 0);
    }

    public final byte[] u16(int value) {
        if (value >= 0 && value < 65536) {
            return new byte[]{(byte) (value >>> 8), (byte) value};
        }
        throw new IllegalArgumentException("u16 value must be in 0..65535".toString());
    }

    public final byte[] i16(int value) {
        boolean z = false;
        if (-32768 <= value && value < 32768) {
            z = true;
        }
        if (z) {
            return u16(65535 & value);
        }
        throw new IllegalArgumentException("i16 value must be in -32768..32767".toString());
    }

    public final byte[] u32(long value) {
        if (0 <= value && value < 4294967296L) {
            return new byte[]{(byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value};
        }
        throw new IllegalArgumentException("u32 value must be in 0..4294967295".toString());
    }

    public final byte[] u32(int value) {
        return u32(value);
    }

    public final byte[] i32(int value) {
        return u32(((long) value) & MonotonicAudioTimestamp.RTP_MASK);
    }

    public final byte[] u64(long value) {
        if (value >= 0) {
            return new byte[]{(byte) (value >>> 56), (byte) (value >>> 48), (byte) (value >>> 40), (byte) (value >>> 32), (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value};
        }
        throw new IllegalArgumentException("u64 value must be non-negative in this API".toString());
    }

    public final byte[] bytes(byte[] value) {
        Intrinsics.checkNotNullParameter(value, "value");
        byte[] bArrCopyOf = Arrays.copyOf(value, value.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        return bArrCopyOf;
    }

    public final byte[] string(String value) {
        Intrinsics.checkNotNullParameter(value, "value");
        if (StringsKt.contains$default((CharSequence) value, (char) 0, false, 2, (Object) null)) {
            throw new IllegalArgumentException("NUL-terminated strings must not contain U+0000".toString());
        }
        return ArraysKt.plus(StringsKt.encodeToByteArray(value), new byte[]{0});
    }

    public final byte[] strings(Iterable<String> values) {
        Intrinsics.checkNotNullParameter(values, "values");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        for (String value : values) {
            output.write(string(value));
        }
        byte[] byteArray = output.toByteArray();
        Intrinsics.checkNotNullExpressionValue(byteArray, "toByteArray(...)");
        return byteArray;
    }

    public final byte[] u16List(Iterable<Integer> values) {
        Intrinsics.checkNotNullParameter(values, "values");
        List materialized = CollectionsKt.toList(values);
        byte[] output = new byte[materialized.size() * 2];
        List $this$forEachIndexed$iv = materialized;
        int index = 0;
        for (Object item$iv : $this$forEachIndexed$iv) {
            int index$iv = index + 1;
            if (index < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            int value = ((Number) item$iv).intValue();
            boolean z = false;
            if (value >= 0 && value < 65536) {
                z = true;
            }
            if (!z) {
                throw new IllegalArgumentException("u16[] value must be in 0..65535".toString());
            }
            output[index * 2] = (byte) (value >>> 8);
            output[(index * 2) + 1] = (byte) value;
            index = index$iv;
        }
        return output;
    }

    public static /* synthetic */ int readU8$default(Iap2WireCodec iap2WireCodec, byte[] bArr, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return iap2WireCodec.readU8(bArr, i);
    }

    public final int readU8(byte[] bytes, int offset) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return u8(bytes[offset]);
    }

    public static /* synthetic */ int readI8$default(Iap2WireCodec iap2WireCodec, byte[] bArr, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return iap2WireCodec.readI8(bArr, i);
    }

    public final int readI8(byte[] bytes, int offset) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return bytes[offset];
    }

    public static /* synthetic */ boolean readBool$default(Iap2WireCodec iap2WireCodec, byte[] bArr, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = true;
        }
        return iap2WireCodec.readBool(bArr, i, z);
    }

    public final boolean readBool(byte[] bytes, int offset, boolean nonzeroIsTrue) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        int value = readU8(bytes, offset);
        if (nonzeroIsTrue) {
            if (value == 0) {
                return false;
            }
        } else if (value != 1) {
            return false;
        }
        return true;
    }

    public static /* synthetic */ int readU16$default(Iap2WireCodec iap2WireCodec, byte[] bArr, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return iap2WireCodec.readU16(bArr, i);
    }

    public final int readU16(byte[] bytes, int offset) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return (u8(bytes[offset]) << 8) | u8(bytes[offset + 1]);
    }

    public static /* synthetic */ int readI16$default(Iap2WireCodec iap2WireCodec, byte[] bArr, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return iap2WireCodec.readI16(bArr, i);
    }

    public final int readI16(byte[] bytes, int offset) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        int value = readU16(bytes, offset);
        return (32768 & value) != 0 ? (-65536) | value : value;
    }

    public static /* synthetic */ long readU32$default(Iap2WireCodec iap2WireCodec, byte[] bArr, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return iap2WireCodec.readU32(bArr, i);
    }

    public final long readU32(byte[] bytes, int offset) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return (((long) u8(bytes[offset])) << 24) | (((long) u8(bytes[offset + 1])) << 16) | (((long) u8(bytes[offset + 2])) << 8) | ((long) u8(bytes[offset + 3]));
    }

    public static /* synthetic */ int readI32$default(Iap2WireCodec iap2WireCodec, byte[] bArr, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return iap2WireCodec.readI32(bArr, i);
    }

    public final int readI32(byte[] bytes, int offset) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return (int) readU32(bytes, offset);
    }

    public static /* synthetic */ long readU64$default(Iap2WireCodec iap2WireCodec, byte[] bArr, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return iap2WireCodec.readU64(bArr, i);
    }

    public final long readU64(byte[] bytes, int offset) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        long value = 0;
        for (int i = 0; i < 8; i++) {
            int index = i;
            value = (value << 8) | ((long) INSTANCE.u8(bytes[offset + index]));
        }
        return value;
    }

    public final byte[] readBytes(byte[] payload) {
        Intrinsics.checkNotNullParameter(payload, "payload");
        byte[] bArrCopyOf = Arrays.copyOf(payload, payload.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        return bArrCopyOf;
    }

    public final String readString(byte[] payload) throws Iap2ProtocolException {
        Intrinsics.checkNotNullParameter(payload, "payload");
        if ((payload.length == 0) || ArraysKt.last(payload) != 0) {
            throw new Iap2ProtocolException("CSM string is not NUL terminated", null, 2, null);
        }
        byte[] bArrCopyOf = Arrays.copyOf(payload, payload.length - 1);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        return StringsKt.decodeToString(bArrCopyOf);
    }

    public final List<String> readStrings(byte[] payload) throws Iap2ProtocolException {
        Intrinsics.checkNotNullParameter(payload, "payload");
        if (payload.length == 0) {
            return CollectionsKt.emptyList();
        }
        if (ArraysKt.last(payload) != 0) {
            throw new Iap2ProtocolException("CSM string[] is not NUL terminated", null, 2, null);
        }
        ArrayList values = new ArrayList();
        int start = 0;
        int length = payload.length;
        for (int index = 0; index < length; index++) {
            if (payload[index] == 0) {
                values.add(StringsKt.decodeToString(ArraysKt.copyOfRange(payload, start, index)));
                start = index + 1;
            }
        }
        return values;
    }

    public final List<Integer> readU16List(byte[] payload) throws Iap2ProtocolException {
        Intrinsics.checkNotNullParameter(payload, "payload");
        if (payload.length % 2 != 0) {
            throw new Iap2ProtocolException("CSM u16[] payload has odd length " + payload.length, null, 2, null);
        }
        int length = payload.length / 2;
        ArrayList arrayList = new ArrayList(length);
        for (int i = 0; i < length; i++) {
            int index = i;
            arrayList.add(Integer.valueOf(INSTANCE.readU16(payload, index * 2)));
        }
        return arrayList;
    }

    private final int u8(byte b) {
        return b & UByte.MAX_VALUE;
    }
}
