package com.carplay.airplay;

import java.util.ArrayList;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.bouncycastle.crypto.InvalidCipherTextException;

/* JADX INFO: compiled from: ControlCipher.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u0000 \u00142\u00020\u0001:\u0002\u0013\u0014B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0003J\u000e\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0003J\u0018\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0012\u001a\u00020\u0010H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lcom/carplay/airplay/ControlCipher;", "", "readKey", "", "writeKey", "<init>", "([B[B)V", "readCounter", "", "writeCounter", "decrypt", "Lcom/carplay/airplay/ControlCipher$Decrypted;", "buffer", "encrypt", "plaintext", "readU16Le", "", "source", "offset", "Decrypted", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ControlCipher {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int HEADER_SIZE = 2;

    @Deprecated
    public static final int MAX_PAYLOAD = 16384;

    @Deprecated
    public static final int TAG_SIZE = 16;
    private long readCounter;
    private final byte[] readKey;
    private long writeCounter;
    private final byte[] writeKey;

    /* JADX INFO: compiled from: ControlCipher.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\b¨\u0006\u0014"}, d2 = {"Lcom/carplay/airplay/ControlCipher$Decrypted;", "", "data", "", "rest", "<init>", "([B[B)V", "getData", "()[B", "getRest", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Decrypted {
        private final byte[] data;
        private final byte[] rest;

        public static /* synthetic */ Decrypted copy$default(Decrypted decrypted, byte[] bArr, byte[] bArr2, int i, Object obj) {
            if ((i & 1) != 0) {
                bArr = decrypted.data;
            }
            if ((i & 2) != 0) {
                bArr2 = decrypted.rest;
            }
            return decrypted.copy(bArr, bArr2);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final byte[] getData() {
            return this.data;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final byte[] getRest() {
            return this.rest;
        }

        public final Decrypted copy(byte[] data, byte[] rest) {
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(rest, "rest");
            return new Decrypted(data, rest);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Decrypted)) {
                return false;
            }
            Decrypted decrypted = (Decrypted) other;
            return Intrinsics.areEqual(this.data, decrypted.data) && Intrinsics.areEqual(this.rest, decrypted.rest);
        }

        public int hashCode() {
            return (Arrays.hashCode(this.data) * 31) + Arrays.hashCode(this.rest);
        }

        public String toString() {
            return "Decrypted(data=" + Arrays.toString(this.data) + ", rest=" + Arrays.toString(this.rest) + ")";
        }

        public Decrypted(byte[] data, byte[] rest) {
            Intrinsics.checkNotNullParameter(data, "data");
            Intrinsics.checkNotNullParameter(rest, "rest");
            this.data = data;
            this.rest = rest;
        }

        public final byte[] getData() {
            return this.data;
        }

        public final byte[] getRest() {
            return this.rest;
        }
    }

    public ControlCipher(byte[] readKey, byte[] writeKey) {
        Intrinsics.checkNotNullParameter(readKey, "readKey");
        Intrinsics.checkNotNullParameter(writeKey, "writeKey");
        this.readKey = readKey;
        this.writeKey = writeKey;
    }

    public final Decrypted decrypt(byte[] buffer) {
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        ArrayList output = new ArrayList();
        int offset = 0;
        while (buffer.length - offset >= 2) {
            int length = readU16Le(buffer, offset);
            int frameEnd = offset + 2 + length + 16;
            if (buffer.length < frameEnd) {
                break;
            }
            byte[] aad = ArraysKt.copyOfRange(buffer, offset, offset + 2);
            byte[] ciphertextAndTag = ArraysKt.copyOfRange(buffer, offset + 2, frameEnd);
            output.add(AirPlayCrypto.INSTANCE.chachaOpen(this.readKey, AirPlayCrypto.INSTANCE.nonce64(this.readCounter), ciphertextAndTag, aad));
            this.readCounter++;
            offset = frameEnd;
        }
        ArrayList $this$toTypedArray$iv = output;
        byte[][] bArr = (byte[][]) $this$toTypedArray$iv.toArray(new byte[0][]);
        return new Decrypted(BytesKt.concatBytes((byte[][]) Arrays.copyOf(bArr, bArr.length)), ArraysKt.copyOfRange(buffer, offset, buffer.length));
    }

    public final byte[] encrypt(byte[] plaintext) throws InvalidCipherTextException {
        Intrinsics.checkNotNullParameter(plaintext, "plaintext");
        ArrayList output = new ArrayList();
        int offset = 0;
        do {
            byte[] chunk = ArraysKt.copyOfRange(plaintext, offset, Math.min(offset + 16384, plaintext.length));
            byte[] header = {(byte) chunk.length, (byte) (chunk.length >>> 8)};
            byte[] ciphertextAndTag = AirPlayCrypto.INSTANCE.chachaSeal(this.writeKey, AirPlayCrypto.INSTANCE.nonce64(this.writeCounter), chunk, header);
            output.add(header);
            output.add(ciphertextAndTag);
            this.writeCounter++;
            offset += 16384;
        } while (offset < plaintext.length);
        ArrayList $this$toTypedArray$iv = output;
        byte[][] bArr = (byte[][]) $this$toTypedArray$iv.toArray(new byte[0][]);
        return BytesKt.concatBytes((byte[][]) Arrays.copyOf(bArr, bArr.length));
    }

    private final int readU16Le(byte[] source, int offset) {
        return (source[offset] & UByte.MAX_VALUE) | ((source[offset + 1] & UByte.MAX_VALUE) << 8);
    }

    /* JADX INFO: compiled from: ControlCipher.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lcom/carplay/airplay/ControlCipher$Companion;", "", "<init>", "()V", "HEADER_SIZE", "", "TAG_SIZE", "MAX_PAYLOAD", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
