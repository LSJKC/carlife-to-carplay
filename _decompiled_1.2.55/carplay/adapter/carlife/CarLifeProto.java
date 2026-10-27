package com.carplay.adapter.carlife;

import com.carplay.sender.MonotonicAudioTimestamp;
import com.carplay.sender.VideoPackets;
import java.io.ByteArrayOutputStream;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Triple;
import kotlin.TuplesKt;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import kotlinx.coroutines.scheduling.WorkQueueKt;

/* JADX INFO: compiled from: CarLifeProtocol.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0012\bÀ\u0002\u0018\u00002\u00020\u0001:\u000212B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u0005J\u000e\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tJ \u0010\n\u001a\u0014\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\r\u001a\u00020\u0005JZ\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\f2\b\b\u0002\u0010\u0012\u001a\u00020\f2\b\b\u0002\u0010\u0013\u001a\u00020\f2\b\b\u0002\u0010\u0014\u001a\u00020\f2\b\b\u0002\u0010\u0015\u001a\u00020\f2\b\b\u0002\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u0017J \u0010\u0019\u001a\u0014\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\f0\u000b2\u0006\u0010\r\u001a\u00020\u0005J\u000e\u0010\u001a\u001a\u00020\u00052\u0006\u0010\u001b\u001a\u00020\tJ\u000e\u0010\u001c\u001a\u00020\u00052\u0006\u0010\u001d\u001a\u00020\u0017J7\u0010\u001e\u001a\u00020\u00052*\u0010\u001f\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\f0!0 \"\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\f0!¢\u0006\u0002\u0010\"J\u000e\u0010#\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u0005J\u000e\u0010$\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0005J\u000e\u0010%\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u0005J\u001e\u0010&\u001a\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00050!2\u0006\u0010\u0006\u001a\u00020\u0005J\u0010\u0010'\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u0005J\u001c\u0010'\u001a\u0004\u0018\u00010\u00052\b\u0010(\u001a\u0004\u0018\u00010\u00052\b\u0010)\u001a\u0004\u0018\u00010\u0005J\u0016\u0010*\u001a\u00020\u00052\u0006\u0010+\u001a\u00020\f2\u0006\u0010,\u001a\u00020\fJ\u001e\u0010-\u001a\u00020\u00052\u0006\u0010.\u001a\u00020\f2\u0006\u0010+\u001a\u00020\f2\u0006\u0010,\u001a\u00020\fJ\u000e\u0010/\u001a\u00020\u00052\u0006\u00100\u001a\u00020\f¨\u00063"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeProto;", "", "<init>", "()V", "pictureAccessUnit", "", "accessUnit", "statisticInfo", "cuid", "", "decodeAudioInit", "Lkotlin/Triple;", "", "payload", "videoEncoderInfo", "width", "height", "frameRate", "areaWidth", "areaHeight", "areaX", "areaY", "drawOutsideSafeArea", "", "needAdapterScreen", "decodeVideoEncoderInfo", "authenRequest", "randomValue", "authenResult", "ok", "featureConfig", "entries", "", "Lkotlin/Pair;", "([Lkotlin/Pair;)[B", "decodeAuthenResponse", "decodeMatchStatus", "describeDeviceInfo", "avcParameterSets", "avcConfig", "sps", "pps", "touchPoint", "x", "y", "touchAction", "action", "hardKey", "keyCode", "Writer", "Reader", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarLifeProto {
    public static final CarLifeProto INSTANCE = new CarLifeProto();

    private CarLifeProto() {
    }

    public final byte[] pictureAccessUnit(byte[] accessUnit) {
        int startCodeSize;
        Intrinsics.checkNotNullParameter(accessUnit, "accessUnit");
        ByteArrayOutputStream out = new ByteArrayOutputStream(accessUnit.length);
        boolean foundPicture = false;
        boolean includeBytes = false;
        int i = 0;
        while (i < accessUnit.length) {
            boolean z = false;
            if (i + 3 < accessUnit.length && accessUnit[i] == 0 && accessUnit[i + 1] == 0 && accessUnit[i + 2] == 0 && accessUnit[i + 3] == 1) {
                startCodeSize = 4;
            } else {
                startCodeSize = (i + 2 < accessUnit.length && accessUnit[i] == 0 && accessUnit[i + 1] == 0 && accessUnit[i + 2] == 1) ? 3 : 0;
            }
            if (startCodeSize == 0) {
                if (includeBytes) {
                    out.write(accessUnit, i, 1);
                }
                i++;
            } else {
                int header = i + startCodeSize;
                if (header >= accessUnit.length) {
                    break;
                }
                int nalType = accessUnit[header] & 31;
                if (1 <= nalType && nalType < 6) {
                    z = true;
                }
                if (z) {
                    foundPicture = true;
                    includeBytes = true;
                    out.write(accessUnit, i, Math.min(startCodeSize, accessUnit.length - i));
                } else {
                    includeBytes = false;
                }
                i = header;
            }
        }
        if (foundPicture) {
            return out.toByteArray();
        }
        return null;
    }

    /* JADX INFO: compiled from: CarLifeProtocol.kt */
    @Metadata(d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0016\u0010\u0006\u001a\u00020\u00002\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bJ\u0016\u0010\n\u001a\u00020\u00002\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u000bJ\u0016\u0010\f\u001a\u00020\u00002\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\rJ\u0016\u0010\u000e\u001a\u00020\u00002\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\u0010J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\bH\u0002J\u0010\u0010\u0014\u001a\u00020\u00122\u0006\u0010\t\u001a\u00020\u0015H\u0002J\u0006\u0010\u0016\u001a\u00020\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeProto$Writer;", "", "<init>", "()V", "out", "Ljava/io/ByteArrayOutputStream;", "int32", "field", "", "value", "bool", "", "string", "", "message", "body", "", "tag", "", "wireType", "varint", "", "toByteArray", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Writer {
        private final ByteArrayOutputStream out = new ByteArrayOutputStream(64);

        public final Writer int32(int field, int value) {
            Writer $this$int32_u24lambda_u240 = this;
            $this$int32_u24lambda_u240.tag(field, 0);
            $this$int32_u24lambda_u240.varint(((long) value) & MonotonicAudioTimestamp.RTP_MASK);
            return this;
        }

        public final Writer bool(int field, boolean value) {
            Writer $this$bool_u24lambda_u241 = this;
            $this$bool_u24lambda_u241.tag(field, 0);
            $this$bool_u24lambda_u241.varint(value ? 1L : 0L);
            return this;
        }

        public final Writer string(int field, String value) {
            Intrinsics.checkNotNullParameter(value, "value");
            Writer $this$string_u24lambda_u242 = this;
            byte[] bytes = value.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
            $this$string_u24lambda_u242.tag(field, 2);
            $this$string_u24lambda_u242.varint(bytes.length);
            $this$string_u24lambda_u242.out.write(bytes, 0, bytes.length);
            return this;
        }

        public final Writer message(int field, byte[] body) {
            Intrinsics.checkNotNullParameter(body, "body");
            Writer $this$message_u24lambda_u243 = this;
            $this$message_u24lambda_u243.tag(field, 2);
            $this$message_u24lambda_u243.varint(body.length);
            $this$message_u24lambda_u243.out.write(body, 0, body.length);
            return this;
        }

        private final void tag(int field, int wireType) {
            varint((((long) field) << 3) | ((long) wireType));
        }

        private final void varint(long value) {
            long v = value;
            while (true) {
                int b = (int) (127 & v);
                v >>>= 7;
                ByteArrayOutputStream byteArrayOutputStream = this.out;
                if (v == 0) {
                    byteArrayOutputStream.write(b);
                    return;
                }
                byteArrayOutputStream.write(b | 128);
            }
        }

        public final byte[] toByteArray() {
            byte[] byteArray = this.out.toByteArray();
            Intrinsics.checkNotNullExpressionValue(byteArray, "toByteArray(...)");
            return byteArray;
        }
    }

    /* JADX INFO: compiled from: CarLifeProtocol.kt */
    @Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\u0018\u00002\u00020\u0001B#\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005¢\u0006\u0004\b\u0007\u0010\bJ\u0006\u0010\n\u001a\u00020\u0005J\u0006\u0010\u000b\u001a\u00020\u0005J\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\u000e\u001a\u00020\u0003J\u0006\u0010\u000f\u001a\u00020\u0010J\u0010\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u0005H\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeProto$Reader;", "", "buffer", "", "pos", "", "end", "<init>", "([BII)V", "lastWireType", "nextField", "readInt32", "readString", "", "readBytes", "skip", "", "advance", "count", "varint", "", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Reader {
        private final byte[] buffer;
        private final int end;
        private int lastWireType;
        private int pos;

        public Reader(byte[] buffer, int pos, int end) {
            Intrinsics.checkNotNullParameter(buffer, "buffer");
            this.buffer = buffer;
            this.pos = pos;
            this.end = end;
        }

        public /* synthetic */ Reader(byte[] bArr, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
            this(bArr, (i3 & 2) != 0 ? 0 : i, (i3 & 4) != 0 ? bArr.length : i2);
        }

        public final int nextField() {
            if (this.pos >= this.end) {
                return 0;
            }
            long key = varint();
            this.lastWireType = (int) (7 & key);
            return (int) (key >>> 3);
        }

        public final int readInt32() {
            return (int) varint();
        }

        public final String readString() {
            int length = (int) varint();
            if (!(length >= 0 && this.pos + length <= this.end)) {
                throw new IllegalArgumentException(("CarLife protobuf 字符串长度越界：" + length).toString());
            }
            String value = new String(this.buffer, this.pos, length, Charsets.UTF_8);
            this.pos += length;
            return value;
        }

        public final byte[] readBytes() {
            int length = (int) varint();
            if (!(length >= 0 && this.pos + length <= this.end)) {
                throw new IllegalArgumentException(("CarLife protobuf 字段长度越界：" + length).toString());
            }
            byte[] value = ArraysKt.copyOfRange(this.buffer, this.pos, this.pos + length);
            this.pos += length;
            return value;
        }

        public final void skip() {
            switch (this.lastWireType) {
                case 0:
                    varint();
                    return;
                case 1:
                    advance(8);
                    return;
                case 2:
                    advance((int) varint());
                    return;
                case 3:
                case 4:
                default:
                    throw new IllegalStateException(("CarLife protobuf 不支持的字段类型 " + this.lastWireType).toString());
                case 5:
                    advance(4);
                    return;
            }
        }

        private final void advance(int count) {
            if (!(count >= 0 && this.pos + count <= this.end)) {
                throw new IllegalArgumentException(("CarLife protobuf 字段长度越界：" + count).toString());
            }
            this.pos += count;
        }

        private final long varint() {
            boolean z;
            long result = 0;
            int shift = 0;
            do {
                z = false;
                if (!(this.pos < this.end)) {
                    throw new IllegalArgumentException("CarLife protobuf 数据提前结束".toString());
                }
                byte[] bArr = this.buffer;
                int i = this.pos;
                this.pos = i + 1;
                int b = bArr[i];
                result |= ((long) (b & WorkQueueKt.MASK)) << shift;
                if ((b & 128) == 0) {
                    return result;
                }
                shift += 7;
                if (shift < 64) {
                    z = true;
                }
            } while (z);
            throw new IllegalArgumentException("CarLife protobuf varint 过长".toString());
        }
    }

    public final byte[] statisticInfo(String cuid) {
        Intrinsics.checkNotNullParameter(cuid, "cuid");
        return new Writer().string(1, cuid).string(2, "8.8.4").int32(3, 884).string(4, "20011100").int32(5, 1).toByteArray();
    }

    public final Triple<Integer, Integer, Integer> decodeAudioInit(byte[] payload) {
        Intrinsics.checkNotNullParameter(payload, "payload");
        int sampleRate = 0;
        int channels = 0;
        int format = 0;
        Reader reader = new Reader(payload, 0, 0, 6, null);
        while (true) {
            int field = reader.nextField();
            switch (field) {
                case 0:
                    return new Triple<>(Integer.valueOf(sampleRate), Integer.valueOf(channels), Integer.valueOf(format));
                case 1:
                    sampleRate = reader.readInt32();
                    break;
                case 2:
                    channels = reader.readInt32();
                    break;
                case 3:
                    format = reader.readInt32();
                    break;
                default:
                    reader.skip();
                    break;
            }
        }
    }

    public static /* synthetic */ byte[] videoEncoderInfo$default(CarLifeProto carLifeProto, int i, int i2, int i3, int i4, int i5, int i6, int i7, boolean z, boolean z2, int i8, Object obj) {
        if ((i8 & 8) != 0) {
            i4 = 0;
        }
        if ((i8 & 16) != 0) {
            i5 = 0;
        }
        if ((i8 & 32) != 0) {
            i6 = 0;
        }
        if ((i8 & 64) != 0) {
            i7 = 0;
        }
        if ((i8 & 128) != 0) {
            z = false;
        }
        if ((i8 & 256) != 0) {
            z2 = false;
        }
        return carLifeProto.videoEncoderInfo(i, i2, i3, i4, i5, i6, i7, z, z2);
    }

    public final byte[] videoEncoderInfo(int width, int height, int frameRate, int areaWidth, int areaHeight, int areaX, int areaY, boolean drawOutsideSafeArea, boolean needAdapterScreen) {
        Writer writer = new Writer().int32(1, width).int32(2, height).int32(3, frameRate);
        if (areaWidth != 0) {
            writer.int32(4, areaWidth);
        }
        if (areaHeight != 0) {
            writer.int32(5, areaHeight);
        }
        if (areaX != 0) {
            writer.int32(6, areaX);
        }
        if (areaY != 0) {
            writer.int32(7, areaY);
        }
        if (drawOutsideSafeArea) {
            writer.bool(8, true);
        }
        if (needAdapterScreen) {
            writer.bool(9, true);
        }
        return writer.toByteArray();
    }

    public final Triple<Integer, Integer, Integer> decodeVideoEncoderInfo(byte[] payload) {
        Intrinsics.checkNotNullParameter(payload, "payload");
        int width = 0;
        int height = 0;
        int frameRate = 0;
        Reader reader = new Reader(payload, 0, 0, 6, null);
        while (true) {
            int field = reader.nextField();
            switch (field) {
                case 0:
                    return new Triple<>(Integer.valueOf(width), Integer.valueOf(height), Integer.valueOf(frameRate));
                case 1:
                    width = reader.readInt32();
                    break;
                case 2:
                    height = reader.readInt32();
                    break;
                case 3:
                    frameRate = reader.readInt32();
                    break;
                default:
                    reader.skip();
                    break;
            }
        }
    }

    public final byte[] authenRequest(String randomValue) {
        Intrinsics.checkNotNullParameter(randomValue, "randomValue");
        return new Writer().string(1, randomValue).toByteArray();
    }

    public final byte[] authenResult(boolean ok) {
        return new Writer().bool(1, ok).toByteArray();
    }

    public final byte[] featureConfig(Pair<String, Integer>... entries) {
        Intrinsics.checkNotNullParameter(entries, "entries");
        Writer writer = new Writer().int32(1, entries.length);
        for (Pair<String, Integer> pair : entries) {
            String key = pair.component1();
            int value = pair.component2().intValue();
            writer.message(2, new Writer().string(1, key).int32(2, value).toByteArray());
        }
        return writer.toByteArray();
    }

    public final String decodeAuthenResponse(byte[] payload) {
        Throwable th;
        Object objM159constructorimpl;
        String string;
        Intrinsics.checkNotNullParameter(payload, "payload");
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLifeProto carLifeProto = this;
            try {
                Reader reader = new Reader(payload, 0, 0, 6, null);
                while (true) {
                    int field = reader.nextField();
                    switch (field) {
                        case 0:
                            string = "";
                            break;
                        case 1:
                            string = reader.readString();
                            break;
                        default:
                            reader.skip();
                            break;
                    }
                }
                objM159constructorimpl = Result.m159constructorimpl(string);
            } catch (Throwable th2) {
                th = th2;
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
        } catch (Throwable th3) {
            th = th3;
        }
        return (String) (Result.m165isFailureimpl(objM159constructorimpl) ? "" : objM159constructorimpl);
    }

    public final int decodeMatchStatus(byte[] payload) {
        Throwable th;
        Object objM159constructorimpl;
        int int32;
        Intrinsics.checkNotNullParameter(payload, "payload");
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLifeProto carLifeProto = this;
            try {
                Reader reader = new Reader(payload, 0, 0, 6, null);
                while (true) {
                    int field = reader.nextField();
                    switch (field) {
                        case 0:
                            int32 = 0;
                            break;
                        case 1:
                            int32 = reader.readInt32();
                            break;
                        default:
                            reader.skip();
                            break;
                    }
                }
                objM159constructorimpl = Result.m159constructorimpl(Integer.valueOf(int32));
            } catch (Throwable th2) {
                th = th2;
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
        } catch (Throwable th3) {
            th = th3;
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = 0;
        }
        return ((Number) objM159constructorimpl).intValue();
    }

    public final String describeDeviceInfo(byte[] payload) {
        Object objM159constructorimpl;
        String it;
        Intrinsics.checkNotNullParameter(payload, "payload");
        StringBuilder parts = new StringBuilder();
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLifeProto carLifeProto = this;
            Reader reader = new Reader(payload, 0, 0, 6, null);
            while (true) {
                int field = reader.nextField();
                if (field == 0) {
                    break;
                }
                if (field == 21 || field == 25) {
                    parts.append("f" + field + "=" + reader.readInt32() + " ");
                } else {
                    if (1 <= field && field < 21) {
                        String it2 = reader.readString();
                        it = it2.length() > 0 ? it2 : null;
                        if (it != null) {
                            parts.append("f" + field + "=" + it + " ");
                        }
                    } else if (field == 22 || field == 23 || field == 24 || field == 26) {
                        String it3 = reader.readString();
                        it = it3.length() > 0 ? it3 : null;
                        if (it != null) {
                            parts.append("f" + field + "=" + it + " ");
                        }
                    } else {
                        reader.skip();
                    }
                }
            }
            String string = parts.toString();
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            objM159constructorimpl = Result.m159constructorimpl(StringsKt.trim((CharSequence) string).toString());
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        Throwable it4 = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (it4 != null) {
            String string2 = parts.append("(解析在 f 处停止：" + it4.getMessage() + ")").toString();
            Intrinsics.checkNotNullExpressionValue(string2, "toString(...)");
            objM159constructorimpl = StringsKt.trim((CharSequence) string2).toString();
        }
        return (String) objM159constructorimpl;
    }

    /* JADX WARN: Code duplicated, block: B:58:0x0094  */
    /* JADX WARN: Code duplicated, block: B:61:0x009c  */
    /* JADX WARN: Code duplicated, block: B:62:0x00a1  */
    public final Pair<byte[], byte[]> avcParameterSets(byte[] accessUnit) {
        int startCodeSize;
        int nextStartCodeSize;
        Intrinsics.checkNotNullParameter(accessUnit, "accessUnit");
        byte[] sps = null;
        byte[] pps = null;
        int i = 0;
        while (i < accessUnit.length) {
            if (i + 3 < accessUnit.length && accessUnit[i] == 0 && accessUnit[i + 1] == 0 && accessUnit[i + 2] == 0 && accessUnit[i + 3] == 1) {
                startCodeSize = 4;
            } else {
                startCodeSize = (i + 2 < accessUnit.length && accessUnit[i] == 0 && accessUnit[i + 1] == 0 && accessUnit[i + 2] == 1) ? 3 : 0;
            }
            if (startCodeSize == 0) {
                i++;
            } else {
                int header = i + startCodeSize;
                int end = accessUnit.length;
                for (int next = header + 1; next < accessUnit.length; next++) {
                    if (next + 3 < accessUnit.length && accessUnit[next] == 0 && accessUnit[next + 1] == 0 && accessUnit[next + 2] == 0 && accessUnit[next + 3] == 1) {
                        nextStartCodeSize = 4;
                    } else {
                        nextStartCodeSize = (next + 2 < accessUnit.length && accessUnit[next] == 0 && accessUnit[next + 1] == 0 && accessUnit[next + 2] == 1) ? 3 : 0;
                    }
                    if (nextStartCodeSize != 0) {
                        end = next;
                        if (header < end) {
                            switch (accessUnit[header] & 31) {
                                case 7:
                                    sps = ArraysKt.copyOfRange(accessUnit, i, end);
                                    break;
                                case 8:
                                    pps = ArraysKt.copyOfRange(accessUnit, i, end);
                                    break;
                            }
                        }
                        i = end;
                    }
                }
                if (header < end) {
                    switch (accessUnit[header] & 31) {
                        case 7:
                            sps = ArraysKt.copyOfRange(accessUnit, i, end);
                            break;
                        case 8:
                            pps = ArraysKt.copyOfRange(accessUnit, i, end);
                            break;
                    }
                }
                i = end;
            }
        }
        return TuplesKt.to(sps, pps);
    }

    public final byte[] avcConfig(byte[] accessUnit) {
        Intrinsics.checkNotNullParameter(accessUnit, "accessUnit");
        Pair<byte[], byte[]> pairAvcParameterSets = avcParameterSets(accessUnit);
        byte[] sps = pairAvcParameterSets.component1();
        byte[] pps = pairAvcParameterSets.component2();
        return avcConfig(sps, pps);
    }

    public final byte[] avcConfig(byte[] sps, byte[] pps) {
        if (sps == null || pps == null) {
            return null;
        }
        return VideoPackets.INSTANCE.avcC(sps, pps);
    }

    public final byte[] touchPoint(int x, int y) {
        return new Writer().int32(1, x).int32(2, y).toByteArray();
    }

    public final byte[] touchAction(int action, int x, int y) {
        return new Writer().int32(1, action).int32(2, x).int32(3, y).toByteArray();
    }

    public final byte[] hardKey(int keyCode) {
        return new Writer().int32(1, keyCode).toByteArray();
    }
}
