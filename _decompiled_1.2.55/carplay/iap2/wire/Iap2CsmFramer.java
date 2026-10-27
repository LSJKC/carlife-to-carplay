package com.carplay.iap2.wire;

import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2CsmFramer.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0003\u0018\u0000 \u00102\u00020\u0001:\u0002\u000f\u0010B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u0006\u0010\t\u001a\u00020\nJ\u0016\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\b0\u000eH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lcom/carplay/iap2/wire/Iap2CsmFramer;", "", "<init>", "()V", "receive", "Lcom/carplay/iap2/wire/Iap2CsmFramer$ReceiveBuffer;", "offer", "", "Lcom/carplay/iap2/wire/Iap2Frame;", "chunk", "", "drain", "", "frames", "", "ReceiveBuffer", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2CsmFramer {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final int HEADER_BYTES = 6;
    public static final int MAX_BODY_BYTES = 65529;
    public static final int MAX_FRAME_BYTES = 65535;
    public static final int MAX_LINK_CHUNK_BYTES = 65525;
    public static final int MAX_PARAM_BYTES = 65535;
    public static final int MIN_FRAME_BYTES = 6;
    public static final int START = 16448;
    private static final byte START_HIGH = 64;
    private static final byte START_LOW = 64;
    private final ReceiveBuffer receive = new ReceiveBuffer(65535);

    public final List<Iap2Frame> offer(byte[] chunk) {
        Intrinsics.checkNotNullParameter(chunk, "chunk");
        if (chunk.length == 0) {
            return CollectionsKt.emptyList();
        }
        ArrayList frames = new ArrayList();
        int offset = 0;
        while (offset < chunk.length) {
            drain(frames);
            int count = Math.min(chunk.length - offset, this.receive.remainingCapacity());
            if (!(count > 0)) {
                throw new IllegalStateException("CSM receive buffer could not make progress".toString());
            }
            this.receive.append(chunk, offset, count);
            offset += count;
        }
        drain(frames);
        return frames;
    }

    private final void drain(List<Iap2Frame> frames) {
        while (true) {
            if (this.receive.getSize() >= 2 && !this.receive.hasStart()) {
                this.receive.discard(1);
            } else {
                if (this.receive.getSize() < 6) {
                    return;
                }
                int length = this.receive.u16(2);
                ReceiveBuffer receiveBuffer = this.receive;
                if (length < 6) {
                    receiveBuffer.discard(1);
                } else {
                    if (receiveBuffer.getSize() < length) {
                        return;
                    }
                    int messageId = this.receive.u16(4);
                    byte[] payload = this.receive.copyOfRange(6, length);
                    this.receive.discard(length);
                    frames.add(new Iap2Frame(messageId, payload));
                }
            }
        }
    }

    /* JADX INFO: compiled from: Iap2CsmFramer.kt */
    @Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0006\b\u0002\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0006\u0010\r\u001a\u00020\u0003J\u001e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u0003J\u000e\u0010\u0013\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u0003J\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u0003J\u0016\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u0003J\b\u0010\u001a\u001a\u00020\u000fH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\n\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\f¨\u0006\u001b"}, d2 = {"Lcom/carplay/iap2/wire/Iap2CsmFramer$ReceiveBuffer;", "", "maximumSize", "", "<init>", "(I)V", "bytes", "", "head", "tail", "size", "getSize", "()I", "remainingCapacity", "append", "", "source", "offset", "count", "discard", "hasStart", "", "u16", "copyOfRange", "from", "to", "compact", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class ReceiveBuffer {
        private final byte[] bytes;
        private int head;
        private final int maximumSize;
        private int tail;

        public ReceiveBuffer(int maximumSize) {
            this.maximumSize = maximumSize;
            this.bytes = new byte[this.maximumSize];
        }

        public final int getSize() {
            return this.tail - this.head;
        }

        public final int remainingCapacity() {
            compact();
            return this.maximumSize - this.tail;
        }

        public final void append(byte[] source, int offset, int count) {
            Intrinsics.checkNotNullParameter(source, "source");
            boolean z = false;
            if (count >= 0 && count <= remainingCapacity()) {
                z = true;
            }
            if (!z) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            ArraysKt.copyInto(source, this.bytes, this.tail, offset, offset + count);
            this.tail += count;
        }

        public final void discard(int count) {
            if (!(count >= 0 && count <= getSize())) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            this.head += count;
            if (this.head == this.tail) {
                this.head = 0;
                this.tail = 0;
            }
        }

        public final boolean hasStart() {
            return this.bytes[this.head] == 64 && this.bytes[this.head + 1] == 64;
        }

        public final int u16(int offset) {
            return ((this.bytes[this.head + offset] & UByte.MAX_VALUE) << 8) | (this.bytes[this.head + offset + 1] & UByte.MAX_VALUE);
        }

        public final byte[] copyOfRange(int from, int to) {
            return ArraysKt.copyOfRange(this.bytes, this.head + from, this.head + to);
        }

        private final void compact() {
            if (this.head == 0) {
                return;
            }
            if (this.head < this.tail) {
                ArraysKt.copyInto(this.bytes, this.bytes, 0, this.head, this.tail);
            }
            this.tail -= this.head;
            this.head = 0;
        }
    }

    /* JADX INFO: compiled from: Iap2CsmFramer.kt */
    @Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0005\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u0010J\u0016\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u0010J\u001c\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00100\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0005J\u001c\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00100\u00162\u0006\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u0019\u001a\u00020\u0005J\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0017\u001a\u00020\u0010H\u0002J\u0018\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u00102\u0006\u0010\u001f\u001a\u00020\u0005H\u0002J \u0010 \u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u00102\u0006\u0010\u001f\u001a\u00020\u00052\u0006\u0010!\u001a\u00020\u0005H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082T¢\u0006\u0002\n\u0000¨\u0006\""}, d2 = {"Lcom/carplay/iap2/wire/Iap2CsmFramer$Companion;", "", "<init>", "()V", "START", "", "HEADER_BYTES", "MIN_FRAME_BYTES", "MAX_FRAME_BYTES", "MAX_BODY_BYTES", "MAX_PARAM_BYTES", "MAX_LINK_CHUNK_BYTES", "START_HIGH", "", "START_LOW", "encodeFrame", "", "messageId", "payload", "encodeParam", "parameterId", "splitForLink", "", "frame", "Lcom/carplay/iap2/wire/Iap2Frame;", "linkChunkSize", "completeFrame", "requireCompleteFrame", "", "readU16", "bytes", "offset", "writeU16", "value", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final byte[] encodeFrame(int messageId, byte[] payload) {
            Intrinsics.checkNotNullParameter(payload, "payload");
            if (!(messageId >= 0 && messageId < 65536)) {
                throw new IllegalArgumentException("CSM message id must fit in u16".toString());
            }
            if (!(payload.length <= 65529)) {
                throw new IllegalArgumentException("CSM payload exceeds 65529 bytes".toString());
            }
            byte[] frame = new byte[payload.length + 6];
            writeU16(frame, 0, Iap2CsmFramer.START);
            writeU16(frame, 2, frame.length);
            writeU16(frame, 4, messageId);
            ArraysKt.copyInto$default(payload, frame, 6, 0, 0, 12, (Object) null);
            return frame;
        }

        public final byte[] encodeParam(int parameterId, byte[] payload) {
            Intrinsics.checkNotNullParameter(payload, "payload");
            if (!(parameterId >= 0 && parameterId < 65536)) {
                throw new IllegalArgumentException("CSM parameter id must fit in u16".toString());
            }
            if (!(payload.length <= 65531)) {
                throw new IllegalArgumentException("CSM parameter payload exceeds 65531 bytes".toString());
            }
            byte[] parameter = new byte[payload.length + 4];
            writeU16(parameter, 0, parameter.length);
            writeU16(parameter, 2, parameterId);
            ArraysKt.copyInto$default(payload, parameter, 4, 0, 0, 12, (Object) null);
            return parameter;
        }

        public final List<byte[]> splitForLink(Iap2Frame frame, int linkChunkSize) {
            Intrinsics.checkNotNullParameter(frame, "frame");
            return splitForLink(frame.encodedFrame(), linkChunkSize);
        }

        public final List<byte[]> splitForLink(byte[] completeFrame, int linkChunkSize) {
            Intrinsics.checkNotNullParameter(completeFrame, "completeFrame");
            if (!(1 <= linkChunkSize && linkChunkSize < 65526)) {
                throw new IllegalArgumentException("iAP2 link chunk size must be in 1..65525".toString());
            }
            requireCompleteFrame(completeFrame);
            int length = ((completeFrame.length + linkChunkSize) - 1) / linkChunkSize;
            ArrayList arrayList = new ArrayList(length);
            for (int i = 0; i < length; i++) {
                int index = i;
                int start = index * linkChunkSize;
                arrayList.add(ArraysKt.copyOfRange(completeFrame, start, Math.min(start + linkChunkSize, completeFrame.length)));
            }
            return arrayList;
        }

        private final void requireCompleteFrame(byte[] frame) {
            int length = frame.length;
            if (!(6 <= length && length < 65536)) {
                throw new IllegalArgumentException("CSM frame size must be 6..65535".toString());
            }
            if (!(readU16(frame, 0) == 16448)) {
                throw new IllegalArgumentException("CSM frame must start with 0x4040".toString());
            }
            if (!(readU16(frame, 2) == frame.length)) {
                throw new IllegalArgumentException("CSM frame length must equal its complete size".toString());
            }
        }

        private final int readU16(byte[] bytes, int offset) {
            return ((bytes[offset] & UByte.MAX_VALUE) << 8) | (bytes[offset + 1] & UByte.MAX_VALUE);
        }

        private final void writeU16(byte[] bytes, int offset, int value) {
            bytes[offset] = (byte) (value >>> 8);
            bytes[offset + 1] = (byte) value;
        }
    }
}
