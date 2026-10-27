package com.carplay.iap2.transport;

import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2IdentificationClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0000¨\u0006\u0006"}, d2 = {"forWirelessLink", "Lcom/carplay/iap2/transport/Iap2IdentificationConfig;", "role", "Lcom/carplay/iap2/transport/Iap2WirelessLinkRole;", "wirelessIdentification", "Lcom/carplay/iap2/transport/Iap2WirelessIdentification;", "core-iap2"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class Iap2IdentificationClientKt {

    /* JADX INFO: compiled from: Iap2IdentificationClient.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[Iap2WirelessLinkRole.values().length];
            try {
                iArr[Iap2WirelessLinkRole.BLUETOOTH_BOOTSTRAP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Iap2WirelessLinkRole.RUNTIME_TUNNEL.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public static final Iap2IdentificationConfig forWirelessLink(Iap2IdentificationConfig $this$forWirelessLink, Iap2WirelessLinkRole role, Iap2WirelessIdentification wirelessIdentification) {
        Intrinsics.checkNotNullParameter($this$forWirelessLink, "<this>");
        Intrinsics.checkNotNullParameter(role, "role");
        Intrinsics.checkNotNullParameter(wirelessIdentification, "wirelessIdentification");
        Iap2IdentificationConfig wirelessConfig = $this$forWirelessLink.copy((15871 & 1) != 0 ? $this$forWirelessLink.name : null, (15871 & 2) != 0 ? $this$forWirelessLink.modelIdentifier : null, (15871 & 4) != 0 ? $this$forWirelessLink.manufacturer : null, (15871 & 8) != 0 ? $this$forWirelessLink.serialNumber : null, (15871 & 16) != 0 ? $this$forWirelessLink.firmwareVersion : null, (15871 & 32) != 0 ? $this$forWirelessLink.hardwareVersion : null, (15871 & 64) != 0 ? $this$forWirelessLink.carPlayUsbInterfaceNumber : 0, (15871 & 128) != 0 ? $this$forWirelessLink.language : null, (15871 & 256) != 0 ? $this$forWirelessLink.externalAccessoryProtocol : null, (15871 & 512) != 0 ? $this$forWirelessLink.wireless : wirelessIdentification, (15871 & 1024) != 0 ? $this$forWirelessLink.locationInformationEnabled : false, (15871 & 2048) != 0 ? $this$forWirelessLink.vehicleStatusEnabled : false, (15871 & 4096) != 0 ? $this$forWirelessLink.chargingConnectors : null, (15871 & 8192) != 0 ? $this$forWirelessLink.vehicleSpeedEnabled : false);
        switch (WhenMappings.$EnumSwitchMapping$0[role.ordinal()]) {
            case 1:
                return wirelessConfig.copy((15871 & 1) != 0 ? wirelessConfig.name : null, (15871 & 2) != 0 ? wirelessConfig.modelIdentifier : null, (15871 & 4) != 0 ? wirelessConfig.manufacturer : null, (15871 & 8) != 0 ? wirelessConfig.serialNumber : null, (15871 & 16) != 0 ? wirelessConfig.firmwareVersion : null, (15871 & 32) != 0 ? wirelessConfig.hardwareVersion : null, (15871 & 64) != 0 ? wirelessConfig.carPlayUsbInterfaceNumber : 0, (15871 & 128) != 0 ? wirelessConfig.language : null, (15871 & 256) != 0 ? wirelessConfig.externalAccessoryProtocol : null, (15871 & 512) != 0 ? wirelessConfig.wireless : null, (15871 & 1024) != 0 ? wirelessConfig.locationInformationEnabled : false, (15871 & 2048) != 0 ? wirelessConfig.vehicleStatusEnabled : false, (15871 & 4096) != 0 ? wirelessConfig.chargingConnectors : null, (15871 & 8192) != 0 ? wirelessConfig.vehicleSpeedEnabled : false);
            case 2:
                return wirelessConfig;
            default:
                throw new NoWhenBranchMatchedException();
        }
    }
}
