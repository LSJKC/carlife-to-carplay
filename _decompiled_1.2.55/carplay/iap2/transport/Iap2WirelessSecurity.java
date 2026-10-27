package com.carplay.iap2.transport;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: Iap2WirelessControlClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0002\b\n\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0011\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\f¨\u0006\r"}, d2 = {"Lcom/carplay/iap2/transport/Iap2WirelessSecurity;", "", "wireValue", "", "<init>", "(Ljava/lang/String;II)V", "getWireValue", "()I", "NONE", "WEP", "WPA_WPA2", "WPA3_TRANSITION", "WPA3_ONLY", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public enum Iap2WirelessSecurity {
    NONE(0),
    WEP(1),
    WPA_WPA2(2),
    WPA3_TRANSITION(3),
    WPA3_ONLY(4);

    private final int wireValue;
    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

    public static EnumEntries<Iap2WirelessSecurity> getEntries() {
        return $ENTRIES;
    }

    Iap2WirelessSecurity(int wireValue) {
        this.wireValue = wireValue;
    }

    public final int getWireValue() {
        return this.wireValue;
    }
}
