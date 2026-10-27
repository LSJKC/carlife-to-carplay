package com.carplay.airplay;

import com.carplay.sender.MonotonicAudioTimestamp;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.jvm.internal.FloatCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.text.CharsKt;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: BplistCodec.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0006\n\u0002\u0010\u0016\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001:\u0003'()B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0007\u001a\u00020\u0005J\u0010\u0010\b\u001a\u00020\u00052\b\u0010\t\u001a\u0004\u0018\u00010\u0001J*\u0010\n\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0002J\u0010\u0010\u0010\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u0010\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0010\u0010\u0016\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0017H\u0002J\u0018\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u000eH\u0002J\u0018\u0010\u001b\u001a\u00020\u00052\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u0018\u0010\u001e\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u00152\u0006\u0010\u001f\u001a\u00020\u000eH\u0002J(\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020\u00052\u0006\u0010#\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u00152\u0006\u0010\u001f\u001a\u00020\u000eH\u0002J \u0010$\u001a\u00020\u00152\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010#\u001a\u00020\u00152\u0006\u0010\u001f\u001a\u00020\u000eH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082T¢\u0006\u0002\n\u0000¨\u0006*"}, d2 = {"Lcom/carplay/airplay/BplistCodec;", "", "<init>", "()V", "magic", "", "decode", "bytes", "encode", "root", "readObject", "offsets", "", "refSize", "", "index", "encodeString", "value", "", "encodeInt", "number", "", "encodeUnsignedInt", "Ljava/math/BigInteger;", "marker", "type", "count", "refBytes", "refs", "", "bigEndian", "size", "writeBigEndian", "", "target", "offset", "readBigEndianLong", "APPLE_EPOCH_SECONDS", "", "Node", "Leaf", "Container", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class BplistCodec {
    private static final double APPLE_EPOCH_SECONDS = 9.783072E8d;
    public static final BplistCodec INSTANCE = new BplistCodec();
    private static final byte[] magic;

    private BplistCodec() {
    }

    static {
        byte[] bytes = "bplist00".getBytes(Charsets.US_ASCII);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        magic = bytes;
    }

    public final Object decode(byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        if (!(bytes.length >= 40)) {
            throw new IllegalArgumentException("bplist: too short".toString());
        }
        if (!Arrays.equals(ArraysKt.copyOfRange(bytes, 0, magic.length), magic)) {
            throw new IllegalArgumentException("bplist: bad magic".toString());
        }
        int trailer = bytes.length - 32;
        int offsetSize = bytes[trailer + 6] & UByte.MAX_VALUE;
        int refSize = bytes[trailer + 7] & UByte.MAX_VALUE;
        int numObjects = (int) readBigEndianLong(bytes, trailer + 8, 8);
        int topObject = (int) readBigEndianLong(bytes, trailer + 16, 8);
        long offsetTable = readBigEndianLong(bytes, trailer + 24, 8);
        long[] offsets = new long[numObjects];
        int length = offsets.length;
        for (int index = 0; index < length; index++) {
            offsets[index] = readBigEndianLong(bytes, (((long) index) * ((long) offsetSize)) + offsetTable, offsetSize);
        }
        return readObject(bytes, offsets, refSize, topObject);
    }

    public final byte[] encode(Object root) {
        int i;
        int i2;
        byte[] serialized;
        ArrayList nodes = new ArrayList();
        int topIndex = encode$add(nodes, root);
        if (nodes.size() > 65535) {
            i = 4;
        } else {
            i = nodes.size() > 255 ? 2 : 1;
        }
        int refSize = i;
        ArrayList parts = new ArrayList(nodes.size() + 2);
        parts.add(magic);
        int cursor = magic.length;
        ArrayList offsets = new ArrayList(nodes.size());
        Iterator it = nodes.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        int cursor2 = cursor;
        while (it.hasNext()) {
            Object next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            Node node = (Node) next;
            offsets.add(Integer.valueOf(cursor2));
            if (node instanceof Leaf) {
                serialized = ((Leaf) node).getBody();
            } else if (node instanceof Container) {
                serialized = ArraysKt.plus(((Container) node).getHead(), refBytes(((Container) node).getRefs(), refSize));
            } else {
                throw new NoWhenBranchMatchedException();
            }
            parts.add(serialized);
            cursor2 += serialized.length;
        }
        int offsetTableOffset = cursor2;
        if (cursor2 > 65535) {
            i2 = 4;
        } else {
            i2 = cursor2 > 255 ? 2 : 1;
        }
        int offsetSize = i2;
        Iterator it2 = offsets.iterator();
        Intrinsics.checkNotNullExpressionValue(it2, "iterator(...)");
        while (it2.hasNext()) {
            Object next2 = it2.next();
            Intrinsics.checkNotNullExpressionValue(next2, "next(...)");
            int offset = ((Number) next2).intValue();
            parts.add(bigEndian(offset, offsetSize));
        }
        byte[] trailer = new byte[32];
        trailer[6] = (byte) offsetSize;
        trailer[7] = (byte) refSize;
        writeBigEndian(trailer, 8, nodes.size(), 8);
        writeBigEndian(trailer, 16, topIndex, 8);
        writeBigEndian(trailer, 24, offsetTableOffset, 8);
        parts.add(trailer);
        ArrayList $this$toTypedArray$iv = parts;
        byte[][] bArr = (byte[][]) $this$toTypedArray$iv.toArray(new byte[0][]);
        return BytesKt.concatBytes((byte[][]) Arrays.copyOf(bArr, bArr.length));
    }

    private static final int encode$add(ArrayList<Node> arrayList, Object value) {
        Leaf leaf;
        int index = arrayList.size();
        arrayList.add(new Leaf(new byte[0]));
        if (value instanceof Boolean) {
            arrayList.set(index, new Leaf(new byte[]{((Boolean) value).booleanValue() ? (byte) 9 : (byte) 8}));
        } else if (value instanceof BigInteger) {
            if (!(((BigInteger) value).signum() >= 0)) {
                throw new IllegalArgumentException("bplist: negative integers are not supported".toString());
            }
            if (!(((BigInteger) value).bitLength() <= 64)) {
                throw new IllegalArgumentException("bplist: integer exceeds 64 bits".toString());
            }
            arrayList.set(index, new Leaf(INSTANCE.encodeUnsignedInt((BigInteger) value)));
        } else if ((value instanceof Byte) || (value instanceof Short) || (value instanceof Integer) || (value instanceof Long)) {
            Intrinsics.checkNotNull(value, "null cannot be cast to non-null type kotlin.Number");
            long number = ((Number) value).longValue();
            if (number >= 0) {
                leaf = new Leaf(INSTANCE.encodeInt(number));
            } else {
                leaf = new Leaf(ArraysKt.plus(new byte[]{35}, INSTANCE.bigEndian(Double.doubleToRawLongBits(number), 8)));
            }
            arrayList.set(index, leaf);
        } else if ((value instanceof Float) || (value instanceof Double)) {
            Intrinsics.checkNotNull(value, "null cannot be cast to non-null type kotlin.Number");
            arrayList.set(index, new Leaf(ArraysKt.plus(new byte[]{35}, INSTANCE.bigEndian(Double.doubleToRawLongBits(((Number) value).doubleValue()), 8))));
        } else if (value instanceof String) {
            arrayList.set(index, new Leaf(INSTANCE.encodeString((String) value)));
        } else if (value instanceof byte[]) {
            arrayList.set(index, new Leaf(ArraysKt.plus(INSTANCE.marker(4, ((byte[]) value).length), (byte[]) value)));
        } else if (value instanceof List) {
            Iterable $this$map$iv = (Iterable) value;
            Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
            for (Object item$iv$iv : $this$map$iv) {
                destination$iv$iv.add(Integer.valueOf(encode$add(arrayList, item$iv$iv)));
            }
            int[] refs = CollectionsKt.toIntArray((List) destination$iv$iv);
            arrayList.set(index, new Container(INSTANCE.marker(10, ((List) value).size()), refs));
        } else if (value instanceof Map) {
            List entries = CollectionsKt.toList(((Map) value).entrySet());
            List $this$map$iv2 = entries;
            Collection destination$iv$iv2 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv2, 10));
            for (Object item$iv$iv2 : $this$map$iv2) {
                Map.Entry it = (Map.Entry) item$iv$iv2;
                destination$iv$iv2.add(Integer.valueOf(encode$add(arrayList, String.valueOf(it.getKey()))));
            }
            int[] keyRefs = CollectionsKt.toIntArray((List) destination$iv$iv2);
            List $this$map$iv3 = entries;
            Collection destination$iv$iv3 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv3, 10));
            for (Object item$iv$iv3 : $this$map$iv3) {
                Map.Entry it2 = (Map.Entry) item$iv$iv3;
                destination$iv$iv3.add(Integer.valueOf(encode$add(arrayList, it2.getValue())));
            }
            int[] valueRefs = CollectionsKt.toIntArray((List) destination$iv$iv3);
            arrayList.set(index, new Container(INSTANCE.marker(13, entries.size()), ArraysKt.plus(keyRefs, valueRefs)));
        } else {
            throw new IllegalArgumentException("bplist: unsupported value " + value);
        }
        return index;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final Object readObject(byte[] bytes, long[] offsets, int refSize, int index) {
        Boolean bool;
        Serializable serializableValueOf;
        Ref.IntRef position = new Ref.IntRef();
        position.element = (int) offsets[index];
        int markerByte = bytes[position.element] & UByte.MAX_VALUE;
        int type = markerByte >>> 4;
        int nibble = markerByte & 15;
        position.element++;
        switch (type) {
            case 0:
                switch (nibble) {
                    case 0:
                        bool = null;
                        break;
                    case 8:
                        bool = false;
                        break;
                    case 9:
                        bool = true;
                        break;
                    default:
                        throw new IllegalArgumentException("bplist: unsupported primitive 0x0" + nibble);
                }
                return bool;
            case 1:
                return Long.valueOf(readBigEndianLong(bytes, position.element, 1 << nibble));
            case 2:
                int size = 1 << nibble;
                switch (size) {
                    case 4:
                        FloatCompanionObject floatCompanionObject = FloatCompanionObject.INSTANCE;
                        serializableValueOf = Float.valueOf(Float.intBitsToFloat((int) readBigEndianLong(bytes, position.element, size)));
                        break;
                    case 8:
                        DoubleCompanionObject doubleCompanionObject = DoubleCompanionObject.INSTANCE;
                        serializableValueOf = Double.valueOf(Double.longBitsToDouble(readBigEndianLong(bytes, position.element, size)));
                        break;
                    default:
                        throw new IllegalArgumentException("bplist: unsupported real size " + size);
                }
                return serializableValueOf;
            case 3:
                DoubleCompanionObject doubleCompanionObject2 = DoubleCompanionObject.INSTANCE;
                return new Date(Math.round((Double.longBitsToDouble(readBigEndianLong(bytes, position.element, 8)) + APPLE_EPOCH_SECONDS) * 1000.0d));
            case 4:
                int count = readObject$readCount(nibble, bytes, position);
                return (Serializable) ArraysKt.copyOfRange(bytes, position.element, position.element + count);
            case 5:
                int count2 = readObject$readCount(nibble, bytes, position);
                return new String(bytes, position.element, count2, Charsets.US_ASCII);
            case 6:
                int count3 = readObject$readCount(nibble, bytes, position);
                return new String(bytes, position.element, count3 * 2, Charsets.UTF_16BE);
            case 7:
            case 9:
            case 11:
            default:
                String string = Integer.toString(type, CharsKt.checkRadix(16));
                Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                throw new IllegalArgumentException("bplist: unsupported object type 0x" + string);
            case 8:
                return Long.valueOf(readBigEndianLong(bytes, position.element, nibble + 1));
            case 10:
            case 12:
                int count4 = readObject$readCount(nibble, bytes, position);
                ArrayList array = new ArrayList(count4);
                int reference = 0;
                while (reference < count4) {
                    int i = reference;
                    array.add(readObject(bytes, offsets, refSize, (int) readBigEndianLong(bytes, ((long) position.element) + (((long) reference) * ((long) refSize)), refSize)));
                    reference = i + 1;
                }
                return array;
            case 13:
                int count5 = readObject$readCount(nibble, bytes, position);
                LinkedHashMap dict = new LinkedHashMap(count5);
                int i2 = 0;
                while (i2 < count5) {
                    int count6 = count5;
                    LinkedHashMap dict2 = dict;
                    int keyReference = (int) readBigEndianLong(bytes, ((long) position.element) + (((long) i2) * ((long) refSize)), refSize);
                    int valueReference = (int) readBigEndianLong(bytes, ((long) position.element) + (((long) (count6 + i2)) * ((long) refSize)), refSize);
                    dict2.put(String.valueOf(readObject(bytes, offsets, refSize, keyReference)), readObject(bytes, offsets, refSize, valueReference));
                    i2++;
                    count5 = count6;
                    dict = dict2;
                }
                return dict;
        }
    }

    private static final int readObject$readCount(int nibble, byte[] $bytes, Ref.IntRef position) {
        if (nibble != 15) {
            return nibble;
        }
        int sizeMarker = $bytes[position.element] & UByte.MAX_VALUE;
        position.element++;
        int intBytes = 1 << (sizeMarker & 15);
        int count = (int) INSTANCE.readBigEndianLong($bytes, position.element, intBytes);
        position.element += intBytes;
        return count;
    }

    private final byte[] encodeString(String value) {
        String $this$all$iv = value;
        boolean ascii = false;
        int i = 0;
        while (true) {
            if (i < $this$all$iv.length()) {
                char element$iv = $this$all$iv.charAt(i);
                if (!(element$iv <= 127)) {
                    break;
                }
                i++;
            } else {
                ascii = true;
                break;
            }
        }
        if (ascii) {
            byte[] bArrMarker = marker(5, value.length());
            byte[] bytes = value.getBytes(Charsets.US_ASCII);
            Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
            return ArraysKt.plus(bArrMarker, bytes);
        }
        byte[] bArrMarker2 = marker(6, value.length());
        byte[] bytes2 = value.getBytes(Charsets.UTF_16BE);
        Intrinsics.checkNotNullExpressionValue(bytes2, "getBytes(...)");
        return ArraysKt.plus(bArrMarker2, bytes2);
    }

    private final byte[] encodeInt(long number) {
        int size;
        int log = 2;
        if (number > MonotonicAudioTimestamp.RTP_MASK) {
            size = 8;
        } else if (number > 65535) {
            size = 4;
        } else {
            size = number > 255 ? 2 : 1;
        }
        switch (size) {
            case 2:
                log = 1;
                break;
            case 4:
                break;
            case 8:
                log = 3;
                break;
            default:
                log = 0;
                break;
        }
        return ArraysKt.plus(new byte[]{(byte) (log | 16)}, bigEndian(number, size));
    }

    private final byte[] encodeUnsignedInt(BigInteger number) {
        int log = 2;
        int size = 8;
        if (number.bitLength() <= 32) {
            if (number.bitLength() > 16) {
                size = 4;
            } else {
                size = number.bitLength() > 8 ? 2 : 1;
            }
        }
        switch (size) {
            case 2:
                log = 1;
                break;
            case 4:
                break;
            case 8:
                log = 3;
                break;
            default:
                log = 0;
                break;
        }
        byte[] raw = number.toByteArray();
        byte[] bArr = {(byte) (log | 16)};
        Intrinsics.checkNotNull(raw);
        return ArraysKt.plus(bArr, ArraysKt.copyOfRange(raw, raw.length - size, raw.length));
    }

    private final byte[] marker(int type, int count) {
        int size;
        int log;
        if (count < 15) {
            return new byte[]{(byte) ((type << 4) | count)};
        }
        if (count > 65535) {
            size = 4;
        } else {
            size = count > 255 ? 2 : 1;
        }
        switch (size) {
            case 2:
                log = 1;
                break;
            case 3:
            default:
                log = 0;
                break;
            case 4:
                log = 2;
                break;
        }
        return ArraysKt.plus(new byte[]{(byte) (15 | (type << 4)), (byte) (log | 16)}, bigEndian(count, size));
    }

    private final byte[] refBytes(int[] refs, int refSize) {
        byte[] output = new byte[refs.length * refSize];
        int length = refs.length;
        for (int index = 0; index < length; index++) {
            writeBigEndian(output, index * refSize, refs[index], refSize);
        }
        return output;
    }

    private final byte[] bigEndian(long value, int size) {
        byte[] output = new byte[size];
        writeBigEndian(output, 0, value, size);
        return output;
    }

    private final void writeBigEndian(byte[] target, int offset, long value, int size) {
        long current = value;
        for (int index = size - 1; -1 < index; index--) {
            target[offset + index] = (byte) (255 & current);
            current >>>= 8;
        }
    }

    private final long readBigEndianLong(byte[] bytes, long offset, int size) {
        long value = 0;
        int base = (int) offset;
        for (int index = 0; index < size; index++) {
            value = (value << 8) | (((long) bytes[base + index]) & 255);
        }
        return value;
    }

    /* JADX INFO: compiled from: BplistCodec.kt */
    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b2\u0018\u00002\u00020\u0001B\t\b\u0004¢\u0006\u0004\b\u0002\u0010\u0003\u0082\u0001\u0002\u0004\u0005¨\u0006\u0006"}, d2 = {"Lcom/carplay/airplay/BplistCodec$Node;", "", "<init>", "()V", "Lcom/carplay/airplay/BplistCodec$Container;", "Lcom/carplay/airplay/BplistCodec$Leaf;", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static abstract class Node {
        public /* synthetic */ Node(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Node() {
        }
    }

    /* JADX INFO: compiled from: BplistCodec.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"Lcom/carplay/airplay/BplistCodec$Leaf;", "Lcom/carplay/airplay/BplistCodec$Node;", "body", "", "<init>", "([B)V", "getBody", "()[B", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Leaf extends Node {
        private final byte[] body;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public Leaf(byte[] body) {
            super(null);
            Intrinsics.checkNotNullParameter(body, "body");
            this.body = body;
        }

        public final byte[] getBody() {
            return this.body;
        }
    }

    /* JADX INFO: compiled from: BplistCodec.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0007\b\u0002\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\f"}, d2 = {"Lcom/carplay/airplay/BplistCodec$Container;", "Lcom/carplay/airplay/BplistCodec$Node;", "head", "", "refs", "", "<init>", "([B[I)V", "getHead", "()[B", "getRefs", "()[I", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Container extends Node {
        private final byte[] head;
        private final int[] refs;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public Container(byte[] head, int[] refs) {
            super(null);
            Intrinsics.checkNotNullParameter(head, "head");
            Intrinsics.checkNotNullParameter(refs, "refs");
            this.head = head;
            this.refs = refs;
        }

        public final byte[] getHead() {
            return this.head;
        }

        public final int[] getRefs() {
            return this.refs;
        }
    }
}
