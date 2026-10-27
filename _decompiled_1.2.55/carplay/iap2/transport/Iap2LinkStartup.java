package com.carplay.iap2.transport;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: PhoneBluetoothProfile.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006¨\u0006\u0007"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LinkStartup;", "", "<init>", "(Ljava/lang/String;I)V", "LEGACY", "ACCESSORY_FIRST", "DETECTION_THEN_SYN", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public enum Iap2LinkStartup {
    LEGACY,
    ACCESSORY_FIRST,
    DETECTION_THEN_SYN;

    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

    public static EnumEntries<Iap2LinkStartup> getEntries() {
        return $ENTRIES;
    }
}
