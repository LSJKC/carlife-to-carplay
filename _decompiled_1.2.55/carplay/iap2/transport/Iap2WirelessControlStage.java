package com.carplay.iap2.transport;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: Iap2WirelessControlClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\n\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\n¨\u0006\u000b"}, d2 = {"Lcom/carplay/iap2/transport/Iap2WirelessControlStage;", "", "<init>", "(Ljava/lang/String;I)V", "IDENTIFIED", "AUTHENTICATED", "SUBSCRIBED", "WIFI_CONFIG_SENT", "CARPLAY_START_SENT", "TRANSPORT_NOTIFIED", "POST_TRANSPORT_WIFI_CONFIG_SENT", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public enum Iap2WirelessControlStage {
    IDENTIFIED,
    AUTHENTICATED,
    SUBSCRIBED,
    WIFI_CONFIG_SENT,
    CARPLAY_START_SENT,
    TRANSPORT_NOTIFIED,
    POST_TRANSPORT_WIFI_CONFIG_SENT;

    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

    public static EnumEntries<Iap2WirelessControlStage> getEntries() {
        return $ENTRIES;
    }
}
