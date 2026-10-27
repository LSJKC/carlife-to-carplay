package com.carplay.iap2.controller;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: PhoneBootstrap.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u0011\u0010\f\u001a\u00020\r8F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0010\u001a\u00020\r8F¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u000fj\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000b¨\u0006\u0012"}, d2 = {"Lcom/carplay/iap2/controller/PhoneBootstrapProfile;", "", "<init>", "(Ljava/lang/String;I)V", "GENERIC", "VIVO_REFERENCE", "VIVO_TRANSPORT_STRING", "VIVO_WIRELESS_READY", "VIVO_FLAT_REFERENCE", "VIVO_FLAT_WIFI_REQUEST", "VIVO_WIRELESS_MINIMAL", "MODERN_WIRELESS", "usesFlatAvailability", "", "getUsesFlatAvailability", "()Z", "usesPhoneTransportString", "getUsesPhoneTransportString", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public enum PhoneBootstrapProfile {
    GENERIC,
    VIVO_REFERENCE,
    VIVO_TRANSPORT_STRING,
    VIVO_WIRELESS_READY,
    VIVO_FLAT_REFERENCE,
    VIVO_FLAT_WIFI_REQUEST,
    VIVO_WIRELESS_MINIMAL,
    MODERN_WIRELESS;

    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

    public static EnumEntries<PhoneBootstrapProfile> getEntries() {
        return $ENTRIES;
    }

    public final boolean getUsesFlatAvailability() {
        return this == VIVO_FLAT_REFERENCE || this == VIVO_FLAT_WIFI_REQUEST;
    }

    public final boolean getUsesPhoneTransportString() {
        return this == VIVO_TRANSPORT_STRING || this == VIVO_WIRELESS_READY || this == MODERN_WIRELESS || getUsesFlatAvailability();
    }
}
