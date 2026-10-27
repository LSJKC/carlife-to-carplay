package com.carplay.iap2.controller;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: PhoneBootstrap.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, d2 = {"Lcom/carplay/iap2/controller/PhoneBootstrapPurpose;", "", "<init>", "(Ljava/lang/String;I)V", "WIRELESS_CONFIGURATION", "CONTROL_SESSION", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public enum PhoneBootstrapPurpose {
    WIRELESS_CONFIGURATION,
    CONTROL_SESSION;

    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

    public static EnumEntries<PhoneBootstrapPurpose> getEntries() {
        return $ENTRIES;
    }
}
