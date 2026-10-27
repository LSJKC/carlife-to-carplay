package com.carplay.iap2.catalog;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: Iap2Endpoint.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0011\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011¨\u0006\u0012"}, d2 = {"Lcom/carplay/iap2/catalog/Iap2WireType;", "", "<init>", "(Ljava/lang/String;I)V", "VOID", "U8", "I8", "U16", "I16", "U32", "I32", "U64", "U16_LIST", "BYTES", "STRING", "STRING_LIST", "GROUP", "OPAQUE", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public enum Iap2WireType {
    VOID,
    U8,
    I8,
    U16,
    I16,
    U32,
    I32,
    U64,
    U16_LIST,
    BYTES,
    STRING,
    STRING_LIST,
    GROUP,
    OPAQUE;

    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

    public static EnumEntries<Iap2WireType> getEntries() {
        return $ENTRIES;
    }
}
