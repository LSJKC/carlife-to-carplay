package com.carplay.sender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: RtspWire.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001\bB\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007¨\u0006\t"}, d2 = {"Lcom/carplay/sender/ControlFraming;", "", "<init>", "()V", "parse", "Lcom/carplay/sender/ControlFraming$Parsed;", "bytes", "", "Parsed", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ControlFraming {
    public static final ControlFraming INSTANCE = new ControlFraming();

    /* JADX INFO: compiled from: RtspWire.kt */
    @Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001d\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0004\b\u0007\u0010\bJ\u000f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\t\u0010\u000e\u001a\u00020\u0006HÆ\u0003J#\u0010\u000f\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u0017"}, d2 = {"Lcom/carplay/sender/ControlFraming$Parsed;", "", "messages", "", "Lcom/carplay/sender/ControlMessage;", "rest", "", "<init>", "(Ljava/util/List;[B)V", "getMessages", "()Ljava/util/List;", "getRest", "()[B", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Parsed {
        private final List<ControlMessage> messages;
        private final byte[] rest;

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Parsed copy$default(Parsed parsed, List list, byte[] bArr, int i, Object obj) {
            if ((i & 1) != 0) {
                list = parsed.messages;
            }
            if ((i & 2) != 0) {
                bArr = parsed.rest;
            }
            return parsed.copy(list, bArr);
        }

        public final List<ControlMessage> component1() {
            return this.messages;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final byte[] getRest() {
            return this.rest;
        }

        public final Parsed copy(List<ControlMessage> messages, byte[] rest) {
            Intrinsics.checkNotNullParameter(messages, "messages");
            Intrinsics.checkNotNullParameter(rest, "rest");
            return new Parsed(messages, rest);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Parsed)) {
                return false;
            }
            Parsed parsed = (Parsed) other;
            return Intrinsics.areEqual(this.messages, parsed.messages) && Intrinsics.areEqual(this.rest, parsed.rest);
        }

        public int hashCode() {
            return (this.messages.hashCode() * 31) + Arrays.hashCode(this.rest);
        }

        public String toString() {
            return "Parsed(messages=" + this.messages + ", rest=" + Arrays.toString(this.rest) + ")";
        }

        public Parsed(List<ControlMessage> messages, byte[] rest) {
            Intrinsics.checkNotNullParameter(messages, "messages");
            Intrinsics.checkNotNullParameter(rest, "rest");
            this.messages = messages;
            this.rest = rest;
        }

        public final List<ControlMessage> getMessages() {
            return this.messages;
        }

        public final byte[] getRest() {
            return this.rest;
        }
    }

    private ControlFraming() {
    }

    public final Parsed parse(byte[] bytes) {
        int size;
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        int i = 0;
        int i2 = 1;
        if (!(bytes.length <= 2200000)) {
            throw new IllegalArgumentException("控制缓冲超过上限".toString());
        }
        List messages = new ArrayList();
        int offset = 0;
        while (offset < bytes.length) {
            int end = -1;
            int length = bytes.length - 3;
            for (int index = offset; index < length; index++) {
                if (bytes[index] == 13 && bytes[index + 1] == 10 && bytes[index + 2] == 13 && bytes[index + 3] == 10) {
                    end = index;
                    break;
                }
            }
            if (end == -1) {
                if (bytes.length - offset < 65536) {
                    i = i2;
                }
                if (i != 0) {
                    break;
                }
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            if ((end - offset < 65536 ? i2 : i) == 0) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            String str = new String(bytes, offset, end - offset, Charsets.US_ASCII);
            String[] strArr = new String[i2];
            strArr[i] = "\r\n";
            List lines = StringsKt.split$default((CharSequence) str, strArr, false, 0, 6, (Object) null);
            LinkedHashMap headers = new LinkedHashMap();
            Iterable $this$forEach$iv = CollectionsKt.drop(lines, i2);
            for (Object element$iv : $this$forEach$iv) {
                String it = (String) element$iv;
                int separator = StringsKt.indexOf$default((CharSequence) it, ':', 0, false, 6, (Object) null);
                if ((separator > 0 ? i2 : i) == 0) {
                    throw new IllegalArgumentException("控制头无效".toString());
                }
                String strSubstring = it.substring(i, separator);
                Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
                int end2 = end;
                String name = StringsKt.trim((CharSequence) strSubstring).toString().toLowerCase(Locale.ROOT);
                Intrinsics.checkNotNullExpressionValue(name, "toLowerCase(...)");
                if (headers.containsKey(name)) {
                    throw new IllegalArgumentException(("重复控制头：" + name).toString());
                }
                List lines2 = lines;
                String strSubstring2 = it.substring(separator + 1);
                Intrinsics.checkNotNullExpressionValue(strSubstring2, "substring(...)");
                headers.put(name, StringsKt.trim((CharSequence) strSubstring2).toString());
                end = end2;
                lines = lines2;
                i = 0;
                i2 = 1;
            }
            int end3 = end;
            List lines3 = lines;
            String it2 = (String) headers.get("content-length");
            if (it2 != null) {
                Integer intOrNull = StringsKt.toIntOrNull(it2);
                if (intOrNull == null) {
                    throw new IllegalStateException("Content-Length 无效".toString());
                }
                size = intOrNull.intValue();
            } else {
                size = 0;
            }
            if (!(size >= 0 && size < 2000001)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            if ((bytes.length - end3) - 4 < size) {
                break;
            }
            messages.add(new ControlMessage((String) CollectionsKt.first(lines3), headers, ArraysKt.copyOfRange(bytes, end3 + 4, end3 + 4 + size)));
            offset = end3 + 4 + size;
            i = 0;
            i2 = 1;
        }
        return new Parsed(messages, ArraysKt.copyOfRange(bytes, offset, bytes.length));
    }
}
