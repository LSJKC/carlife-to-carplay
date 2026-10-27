package com.carplay.airplay;

import java.util.Arrays;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PairingStore.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0012\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B%\u0012\u001c\b\u0002\u0010\u0002\u001a\u0016\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0003¢\u0006\u0004\b\u0007\u0010\bJ\u0016\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u0005J\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u00052\u0006\u0010\r\u001a\u00020\u0004J\u0006\u0010\u0010\u001a\u00020\u0006R\"\u0010\u0002\u001a\u0016\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R*\u0010\t\u001a\u001e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\nj\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0005`\u000bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lcom/carplay/airplay/PairingStore;", "", "onSave", "Lkotlin/Function2;", "", "", "", "<init>", "(Lkotlin/jvm/functions/Function2;)V", "entries", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "save", "identifier", "longTermPublicKey", "get", "clear", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class PairingStore {
    private final HashMap<String, byte[]> entries;
    private final Function2<String, byte[], Unit> onSave;

    /* JADX WARN: Multi-variable type inference failed */
    public PairingStore() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public PairingStore(Function2<? super String, ? super byte[], Unit> function2) {
        this.onSave = function2;
        this.entries = new HashMap<>();
    }

    public /* synthetic */ PairingStore(Function2 function2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : function2);
    }

    public final void save(String identifier, byte[] longTermPublicKey) {
        Intrinsics.checkNotNullParameter(identifier, "identifier");
        Intrinsics.checkNotNullParameter(longTermPublicKey, "longTermPublicKey");
        byte[] copy = Arrays.copyOf(longTermPublicKey, longTermPublicKey.length);
        Intrinsics.checkNotNullExpressionValue(copy, "copyOf(...)");
        this.entries.put(identifier, copy);
        Function2<String, byte[], Unit> function2 = this.onSave;
        if (function2 != null) {
            function2.invoke(identifier, copy);
        }
    }

    public final byte[] get(String identifier) {
        Intrinsics.checkNotNullParameter(identifier, "identifier");
        byte[] bArr = this.entries.get(identifier);
        if (bArr == null) {
            return null;
        }
        byte[] bArrCopyOf = Arrays.copyOf(bArr, bArr.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        return bArrCopyOf;
    }

    public final void clear() {
        this.entries.clear();
    }
}
