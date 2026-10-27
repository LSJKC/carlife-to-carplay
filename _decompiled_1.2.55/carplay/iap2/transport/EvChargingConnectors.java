package com.carplay.iap2.transport;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;

/* JADX INFO: compiled from: Iap2VehicleStatus.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u0015\n\u0002\u0010\b\n\u0002\b\b\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0015\b\u0002\u0012\n\u0010\u0002\u001a\u00020\u0003\"\u00020\u0004¢\u0006\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0002\u001a\u00020\u0003X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000b¨\u0006\f"}, d2 = {"Lcom/carplay/iap2/transport/EvChargingConnectors;", "", "wireValues", "", "", "<init>", "(Ljava/lang/String;I[I)V", "getWireValues$core_iap2", "()[I", "CCS2_TYPE2", "GB_T", "CCS1_J1772", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public enum EvChargingConnectors {
    CCS2_TYPE2(1, 4),
    GB_T(5, 6),
    CCS1_J1772(0, 2);

    private final int[] wireValues;
    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

    public static EnumEntries<EvChargingConnectors> getEntries() {
        return $ENTRIES;
    }

    EvChargingConnectors(int... wireValues) {
        this.wireValues = wireValues;
    }

    /* JADX INFO: renamed from: getWireValues$core_iap2, reason: from getter */
    public final int[] getWireValues() {
        return this.wireValues;
    }
}
