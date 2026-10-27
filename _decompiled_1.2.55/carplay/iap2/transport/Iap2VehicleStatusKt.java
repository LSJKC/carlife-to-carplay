package com.carplay.iap2.transport;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2VehicleStatus.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003¨\u0006\u0004"}, d2 = {"withVehicleStatusFrom", "Lcom/carplay/iap2/transport/Iap2IdentificationConfig;", "provider", "Lcom/carplay/iap2/transport/VehicleStatusProvider;", "core-iap2"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class Iap2VehicleStatusKt {
    public static final Iap2IdentificationConfig withVehicleStatusFrom(Iap2IdentificationConfig $this$withVehicleStatusFrom, VehicleStatusProvider provider) {
        Intrinsics.checkNotNullParameter($this$withVehicleStatusFrom, "<this>");
        if ($this$withVehicleStatusFrom.getVehicleStatusEnabled()) {
            if ((provider != null ? provider.snapshot() : null) == null) {
                return $this$withVehicleStatusFrom.copy((15871 & 1) != 0 ? $this$withVehicleStatusFrom.name : null, (15871 & 2) != 0 ? $this$withVehicleStatusFrom.modelIdentifier : null, (15871 & 4) != 0 ? $this$withVehicleStatusFrom.manufacturer : null, (15871 & 8) != 0 ? $this$withVehicleStatusFrom.serialNumber : null, (15871 & 16) != 0 ? $this$withVehicleStatusFrom.firmwareVersion : null, (15871 & 32) != 0 ? $this$withVehicleStatusFrom.hardwareVersion : null, (15871 & 64) != 0 ? $this$withVehicleStatusFrom.carPlayUsbInterfaceNumber : 0, (15871 & 128) != 0 ? $this$withVehicleStatusFrom.language : null, (15871 & 256) != 0 ? $this$withVehicleStatusFrom.externalAccessoryProtocol : null, (15871 & 512) != 0 ? $this$withVehicleStatusFrom.wireless : null, (15871 & 1024) != 0 ? $this$withVehicleStatusFrom.locationInformationEnabled : false, (15871 & 2048) != 0 ? $this$withVehicleStatusFrom.vehicleStatusEnabled : false, (15871 & 4096) != 0 ? $this$withVehicleStatusFrom.chargingConnectors : null, (15871 & 8192) != 0 ? $this$withVehicleStatusFrom.vehicleSpeedEnabled : false);
            }
        }
        return $this$withVehicleStatusFrom;
    }
}
