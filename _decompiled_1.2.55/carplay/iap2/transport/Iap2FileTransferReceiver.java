package com.carplay.iap2.transport;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2FileTransferReceiver.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0010\t\n\u0002\b\u0007\u0018\u0000 \u001f2\u00020\u0001:\u0003\u001d\u001e\u001fB\u0011\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\u000e\u001a\u00020\u000fJ\u0018\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\rH\u0002J \u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\rH\u0002J\u0010\u0010\u0014\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u0003H\u0002J\u0010\u0010\u0015\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0003H\u0002J\u0010\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0003H\u0002J\u0010\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u0003H\u0002J\u0018\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\u0003H\u0002J\u0018\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\u0003H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R*\u0010\u0006\u001a\u001e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\b0\u0007j\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\b`\tX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006 "}, d2 = {"Lcom/carplay/iap2/transport/Iap2FileTransferReceiver;", "", "maximumArtworkBytes", "", "<init>", "(I)V", "pending", "Ljava/util/LinkedHashMap;", "Lcom/carplay/iap2/transport/Iap2FileTransferReceiver$Pending;", "Lkotlin/collections/LinkedHashMap;", "accept", "Lcom/carplay/iap2/transport/Iap2FileTransferReceiver$Outcome;", "datagram", "", "clear", "", "setup", "id", "data", "flags", "rejected", "start", "cancel", "success", "readU64", "", "bytes", "offset", "readU16", "Outcome", "Pending", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2FileTransferReceiver {
    public static final int DEFAULT_MAXIMUM_ARTWORK_BYTES = 2097152;
    private static final int FILE_TYPE_OFFSET = 10;
    private static final int FLAG_LAST = 64;
    private static final int HEADER_BYTES = 2;
    private static final int MAXIMUM_PENDING_TRANSFERS = 4;
    private static final int NOW_PLAYING_ARTWORK_TYPE = 2;
    private static final int OPCODE_CANCEL = 2;
    private static final int OPCODE_DATA = 0;
    private static final int OPCODE_MASK = 15;
    private static final int OPCODE_SETUP = 4;
    private static final int OPCODE_START = 1;
    private static final int OPCODE_SUCCESS = 5;
    private static final int SIZE_OFFSET = 2;
    private static final int VERSION_2_SETUP_BYTES = 12;
    private final int maximumArtworkBytes;
    private final LinkedHashMap<Integer, Pending> pending;

    public Iap2FileTransferReceiver() {
        this(0, 1, null);
    }

    public Iap2FileTransferReceiver(int maximumArtworkBytes) {
        this.maximumArtworkBytes = maximumArtworkBytes;
        this.pending = new LinkedHashMap<>();
        if (this.maximumArtworkBytes > 0) {
        } else {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
    }

    public /* synthetic */ Iap2FileTransferReceiver(int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? 2097152 : i);
    }

    /* JADX INFO: compiled from: Iap2FileTransferReceiver.kt */
    @Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B#\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\u0004\b\u0007\u0010\bJ\u000f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\u000b\u0010\u000e\u001a\u0004\u0018\u00010\u0006HÆ\u0003J%\u0010\u000f\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006HÆ\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u0017"}, d2 = {"Lcom/carplay/iap2/transport/Iap2FileTransferReceiver$Outcome;", "", "replies", "", "", "completed", "Lcom/carplay/iap2/transport/Iap2ArtworkTransfer;", "<init>", "(Ljava/util/List;Lcom/carplay/iap2/transport/Iap2ArtworkTransfer;)V", "getReplies", "()Ljava/util/List;", "getCompleted", "()Lcom/carplay/iap2/transport/Iap2ArtworkTransfer;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Outcome {
        private final Iap2ArtworkTransfer completed;
        private final List<byte[]> replies;

        /* JADX WARN: Multi-variable type inference failed */
        public Outcome() {
            this(null, 0 == true ? 1 : 0, 3, 0 == true ? 1 : 0);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Outcome copy$default(Outcome outcome, List list, Iap2ArtworkTransfer iap2ArtworkTransfer, int i, Object obj) {
            if ((i & 1) != 0) {
                list = outcome.replies;
            }
            if ((i & 2) != 0) {
                iap2ArtworkTransfer = outcome.completed;
            }
            return outcome.copy(list, iap2ArtworkTransfer);
        }

        public final List<byte[]> component1() {
            return this.replies;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final Iap2ArtworkTransfer getCompleted() {
            return this.completed;
        }

        public final Outcome copy(List<byte[]> replies, Iap2ArtworkTransfer completed) {
            Intrinsics.checkNotNullParameter(replies, "replies");
            return new Outcome(replies, completed);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Outcome)) {
                return false;
            }
            Outcome outcome = (Outcome) other;
            return Intrinsics.areEqual(this.replies, outcome.replies) && Intrinsics.areEqual(this.completed, outcome.completed);
        }

        public int hashCode() {
            return (this.replies.hashCode() * 31) + (this.completed == null ? 0 : this.completed.hashCode());
        }

        public String toString() {
            return "Outcome(replies=" + this.replies + ", completed=" + this.completed + ")";
        }

        public Outcome(List<byte[]> replies, Iap2ArtworkTransfer completed) {
            Intrinsics.checkNotNullParameter(replies, "replies");
            this.replies = replies;
            this.completed = completed;
        }

        public /* synthetic */ Outcome(List list, Iap2ArtworkTransfer iap2ArtworkTransfer, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this((i & 1) != 0 ? CollectionsKt.emptyList() : list, (i & 2) != 0 ? null : iap2ArtworkTransfer);
        }

        public final List<byte[]> getReplies() {
            return this.replies;
        }

        public final Iap2ArtworkTransfer getCompleted() {
            return this.completed;
        }
    }

    /* JADX INFO: compiled from: Iap2FileTransferReceiver.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0015"}, d2 = {"Lcom/carplay/iap2/transport/Iap2FileTransferReceiver$Pending;", "", "expectedBytes", "", "output", "Ljava/io/ByteArrayOutputStream;", "<init>", "(ILjava/io/ByteArrayOutputStream;)V", "getExpectedBytes", "()I", "getOutput", "()Ljava/io/ByteArrayOutputStream;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class Pending {
        private final int expectedBytes;
        private final ByteArrayOutputStream output;

        public static /* synthetic */ Pending copy$default(Pending pending, int i, ByteArrayOutputStream byteArrayOutputStream, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                i = pending.expectedBytes;
            }
            if ((i2 & 2) != 0) {
                byteArrayOutputStream = pending.output;
            }
            return pending.copy(i, byteArrayOutputStream);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final int getExpectedBytes() {
            return this.expectedBytes;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final ByteArrayOutputStream getOutput() {
            return this.output;
        }

        public final Pending copy(int expectedBytes, ByteArrayOutputStream output) {
            Intrinsics.checkNotNullParameter(output, "output");
            return new Pending(expectedBytes, output);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Pending)) {
                return false;
            }
            Pending pending = (Pending) other;
            return this.expectedBytes == pending.expectedBytes && Intrinsics.areEqual(this.output, pending.output);
        }

        public int hashCode() {
            return (Integer.hashCode(this.expectedBytes) * 31) + this.output.hashCode();
        }

        public String toString() {
            return "Pending(expectedBytes=" + this.expectedBytes + ", output=" + this.output + ")";
        }

        public Pending(int expectedBytes, ByteArrayOutputStream output) {
            Intrinsics.checkNotNullParameter(output, "output");
            this.expectedBytes = expectedBytes;
            this.output = output;
        }

        public /* synthetic */ Pending(int i, ByteArrayOutputStream byteArrayOutputStream, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            this(i, (i2 & 2) != 0 ? new ByteArrayOutputStream(i) : byteArrayOutputStream);
        }

        public final int getExpectedBytes() {
            return this.expectedBytes;
        }

        public final ByteArrayOutputStream getOutput() {
            return this.output;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final synchronized Outcome accept(byte[] datagram) {
        Outcome outcomeData;
        Intrinsics.checkNotNullParameter(datagram, "datagram");
        int i = 3;
        List list = null;
        Object[] objArr = 0;
        Object[] objArr2 = 0;
        Object[] objArr3 = 0;
        Object[] objArr4 = 0;
        Object[] objArr5 = 0;
        Object[] objArr6 = 0;
        Object[] objArr7 = 0;
        Object[] objArr8 = 0;
        Object[] objArr9 = 0;
        Object[] objArr10 = 0;
        if (datagram.length < 2) {
            return new Outcome(list, objArr10 == true ? 1 : 0, i, objArr9 == true ? 1 : 0);
        }
        int i2 = datagram[0] & UByte.MAX_VALUE;
        int i3 = 1;
        int i4 = datagram[1] & UByte.MAX_VALUE;
        switch (i4 & 15) {
            case 0:
                outcomeData = data(i2, i4, datagram);
                break;
            case 1:
            case 3:
            default:
                outcomeData = new Outcome(objArr8 == true ? 1 : 0, objArr7 == true ? 1 : 0, i, objArr6 == true ? 1 : 0);
                break;
            case 2:
                if (this.pending.remove(Integer.valueOf(i2)) == null) {
                    return new Outcome(objArr5 == true ? 1 : 0, objArr4 == true ? 1 : 0, i, objArr3 == true ? 1 : 0);
                }
                outcomeData = new Outcome(objArr2 == true ? 1 : 0, new Iap2ArtworkTransfer(i2, new byte[0]), i3, objArr == true ? 1 : 0);
                break;
            case 4:
                outcomeData = setup(i2, datagram);
                break;
        }
        return outcomeData;
    }

    public final synchronized void clear() {
        this.pending.clear();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final Outcome setup(int id, byte[] datagram) {
        this.pending.remove(Integer.valueOf(id));
        int i = 2;
        Iap2ArtworkTransfer iap2ArtworkTransfer = null;
        Object[] objArr = 0;
        Object[] objArr2 = 0;
        Object[] objArr3 = 0;
        Object[] objArr4 = 0;
        Object[] objArr5 = 0;
        Object[] objArr6 = 0;
        Object[] objArr7 = 0;
        if (datagram.length < 12) {
            return new Outcome(CollectionsKt.listOf(cancel(id)), iap2ArtworkTransfer, i, objArr7 == true ? 1 : 0);
        }
        long u64 = readU64(datagram, 2);
        if (readU16(datagram, 10) != 2) {
            return new Outcome(CollectionsKt.listOf(cancel(id)), objArr6 == true ? 1 : 0, i, objArr5 == true ? 1 : 0);
        }
        if (u64 < 0 || u64 > this.maximumArtworkBytes) {
            return rejected(id);
        }
        if (u64 == 0) {
            return new Outcome(CollectionsKt.listOf((Object[]) new byte[][]{start(id), success(id)}), new Iap2ArtworkTransfer(id, new byte[0]));
        }
        this.pending.put(Integer.valueOf(id), new Pending((int) u64, objArr4 == true ? 1 : 0, i, objArr3 == true ? 1 : 0));
        while (this.pending.size() > 4) {
            LinkedHashMap<Integer, Pending> linkedHashMap = this.pending;
            Set<Integer> setKeySet = this.pending.keySet();
            Intrinsics.checkNotNullExpressionValue(setKeySet, "<get-keys>(...)");
            linkedHashMap.remove(CollectionsKt.first(setKeySet));
        }
        return new Outcome(CollectionsKt.listOf(start(id)), objArr2 == true ? 1 : 0, i, objArr == true ? 1 : 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final Outcome data(int id, int flags, byte[] datagram) {
        Pending pending = this.pending.get(Integer.valueOf(id));
        int i = 3;
        List list = null;
        Object[] objArr = 0;
        Object[] objArr2 = 0;
        Object[] objArr3 = 0;
        Object[] objArr4 = 0;
        Object[] objArr5 = 0;
        if (pending == null) {
            return new Outcome(list, objArr5 == true ? 1 : 0, i, objArr4 == true ? 1 : 0);
        }
        int length = datagram.length - 2;
        if (length > pending.getExpectedBytes() - pending.getOutput().size()) {
            this.pending.remove(Integer.valueOf(id));
            return rejected(id);
        }
        pending.getOutput().write(datagram, 2, length);
        if (!((flags & 64) != 0) && pending.getOutput().size() < pending.getExpectedBytes()) {
            return new Outcome(objArr3 == true ? 1 : 0, objArr2 == true ? 1 : 0, i, objArr == true ? 1 : 0);
        }
        this.pending.remove(Integer.valueOf(id));
        if (pending.getOutput().size() != pending.getExpectedBytes()) {
            return rejected(id);
        }
        List listListOf = CollectionsKt.listOf(success(id));
        byte[] byteArray = pending.getOutput().toByteArray();
        Intrinsics.checkNotNullExpressionValue(byteArray, "toByteArray(...)");
        return new Outcome(listListOf, new Iap2ArtworkTransfer(id, byteArray));
    }

    private final Outcome rejected(int id) {
        return new Outcome(CollectionsKt.listOf(cancel(id)), new Iap2ArtworkTransfer(id, new byte[0]));
    }

    private final byte[] start(int id) {
        return new byte[]{(byte) id, 1};
    }

    private final byte[] cancel(int id) {
        return new byte[]{(byte) id, 2};
    }

    private final byte[] success(int id) {
        return new byte[]{(byte) id, 5};
    }

    private final long readU64(byte[] bytes, int offset) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            int it = i;
            if (value < 0) {
                return -1L;
            }
            value = (value << 8) | (((long) bytes[offset + it]) & 255);
        }
        return value;
    }

    private final int readU16(byte[] bytes, int offset) {
        return ((bytes[offset] & UByte.MAX_VALUE) << 8) | (bytes[offset + 1] & UByte.MAX_VALUE);
    }
}
