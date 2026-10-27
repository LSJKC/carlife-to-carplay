package com.carplay.airplay;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: ScreenStream.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0000\n\u0002\u0010\u000e\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0002\u001a\f\u0010\u0005\u001a\u00020\u0006*\u00020\u0002H\u0002\u001a\u0018\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\u0004H\u0002\u001a\u0018\u0010\n\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00022\u0006\u0010\t\u001a\u00020\u0004H\u0002¨\u0006\u000b"}, d2 = {"hexPrefix", "", "", "length", "", "startsWithStartCode", "", "readU32Be", "source", "offset", "readU32Le", "core-airplay"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class ScreenStreamKt {
    /* JADX INFO: Access modifiers changed from: private */
    public static final String hexPrefix(byte[] $this$hexPrefix, int length) {
        return CollectionsKt.joinToString$default(ArraysKt.take($this$hexPrefix, length), "", null, null, 0, null, new Function1() { // from class: com.carplay.airplay.ScreenStreamKt$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return ScreenStreamKt.hexPrefix$lambda$0(((Byte) obj).byteValue());
            }
        }, 30, null);
    }

    static final CharSequence hexPrefix$lambda$0(byte it) {
        String str = String.format("%02x", Arrays.copyOf(new Object[]{Integer.valueOf(it & UByte.MAX_VALUE)}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean startsWithStartCode(byte[] $this$startsWithStartCode) {
        return $this$startsWithStartCode.length >= 4 && $this$startsWithStartCode[0] == 0 && $this$startsWithStartCode[1] == 0 && $this$startsWithStartCode[2] == 0 && $this$startsWithStartCode[3] == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int readU32Be(byte[] source, int offset) {
        return ((source[offset] & UByte.MAX_VALUE) << 24) | ((source[offset + 1] & UByte.MAX_VALUE) << 16) | ((source[offset + 2] & UByte.MAX_VALUE) << 8) | (source[offset + 3] & UByte.MAX_VALUE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int readU32Le(byte[] source, int offset) {
        return (source[offset] & UByte.MAX_VALUE) | ((source[offset + 1] & UByte.MAX_VALUE) << 8) | ((source[offset + 2] & UByte.MAX_VALUE) << 16) | ((source[offset + 3] & UByte.MAX_VALUE) << 24);
    }
}
