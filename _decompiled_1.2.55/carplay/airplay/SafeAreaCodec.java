package com.carplay.airplay;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: SafeArea.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007J\u0012\u0010\b\u001a\u0004\u0018\u00010\u00072\b\u0010\t\u001a\u0004\u0018\u00010\u0005¨\u0006\n"}, d2 = {"Lcom/carplay/airplay/SafeAreaCodec;", "", "<init>", "()V", "encode", "", "rect", "Lcom/carplay/airplay/SafeAreaRect;", "decode", "value", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class SafeAreaCodec {
    public static final SafeAreaCodec INSTANCE = new SafeAreaCodec();

    private SafeAreaCodec() {
    }

    public final String encode(SafeAreaRect rect) {
        Intrinsics.checkNotNullParameter(rect, "rect");
        return CollectionsKt.joinToString$default(CollectionsKt.listOf((Object[]) new Integer[]{Integer.valueOf(rect.getLeft()), Integer.valueOf(rect.getTop()), Integer.valueOf(rect.getRight()), Integer.valueOf(rect.getBottom())}), ",", null, null, 0, null, null, 62, null);
    }

    public final SafeAreaRect decode(String value) {
        List parts;
        if (value == null || (parts = StringsKt.split$default((CharSequence) value, new char[]{','}, false, 0, 6, (Object) null)) == null || parts.size() != 4) {
            return null;
        }
        try {
            return new SafeAreaRect(Integer.parseInt((String) parts.get(0)), Integer.parseInt((String) parts.get(1)), Integer.parseInt((String) parts.get(2)), Integer.parseInt((String) parts.get(3)));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
