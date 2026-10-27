package com.carplay.iap2.transport;

import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Iap2IdentificationClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b+\b\u0086\b\u0018\u00002\u00020\u0001B\u0087\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0013\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0010¢\u0006\u0004\b\u0015\u0010\u0016BU\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003¢\u0006\u0004\b\u0015\u0010\u0017J\t\u0010+\u001a\u00020\u0003HÆ\u0003J\t\u0010,\u001a\u00020\u0003HÆ\u0003J\t\u0010-\u001a\u00020\u0003HÆ\u0003J\t\u0010.\u001a\u00020\u0003HÆ\u0003J\t\u0010/\u001a\u00020\u0003HÆ\u0003J\t\u00100\u001a\u00020\u0003HÆ\u0003J\t\u00101\u001a\u00020\nHÆ\u0003J\t\u00102\u001a\u00020\u0003HÆ\u0003J\t\u00103\u001a\u00020\u0003HÆ\u0003J\u000b\u00104\u001a\u0004\u0018\u00010\u000eHÆ\u0003J\t\u00105\u001a\u00020\u0010HÆ\u0003J\t\u00106\u001a\u00020\u0010HÆ\u0003J\t\u00107\u001a\u00020\u0013HÆ\u0003J\t\u00108\u001a\u00020\u0010HÆ\u0003J\u0097\u0001\u00109\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u00102\b\b\u0002\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010\u0014\u001a\u00020\u0010HÆ\u0001J\u0013\u0010:\u001a\u00020\u00102\b\u0010;\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010<\u001a\u00020\nHÖ\u0001J\t\u0010=\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0019R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0019R\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0019R\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0019R\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0019R\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010\u000b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0019R\u0011\u0010\f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0019R\u0013\u0010\r\u001a\u0004\u0018\u00010\u000e¢\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0011\u0010\u000f\u001a\u00020\u0010¢\u0006\b\n\u0000\u001a\u0004\b%\u0010&R\u0011\u0010\u0011\u001a\u00020\u0010¢\u0006\b\n\u0000\u001a\u0004\b'\u0010&R\u0011\u0010\u0012\u001a\u00020\u0013¢\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0011\u0010\u0014\u001a\u00020\u0010¢\u0006\b\n\u0000\u001a\u0004\b*\u0010&¨\u0006>"}, d2 = {"Lcom/carplay/iap2/transport/Iap2IdentificationConfig;", "", "name", "", "modelIdentifier", "manufacturer", "serialNumber", "firmwareVersion", "hardwareVersion", "carPlayUsbInterfaceNumber", "", "language", "externalAccessoryProtocol", "wireless", "Lcom/carplay/iap2/transport/Iap2WirelessIdentification;", "locationInformationEnabled", "", "vehicleStatusEnabled", "chargingConnectors", "Lcom/carplay/iap2/transport/EvChargingConnectors;", "vehicleSpeedEnabled", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Lcom/carplay/iap2/transport/Iap2WirelessIdentification;ZZLcom/carplay/iap2/transport/EvChargingConnectors;Z)V", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/carplay/iap2/transport/Iap2WirelessIdentification;Ljava/lang/String;Ljava/lang/String;)V", "getName", "()Ljava/lang/String;", "getModelIdentifier", "getManufacturer", "getSerialNumber", "getFirmwareVersion", "getHardwareVersion", "getCarPlayUsbInterfaceNumber", "()I", "getLanguage", "getExternalAccessoryProtocol", "getWireless", "()Lcom/carplay/iap2/transport/Iap2WirelessIdentification;", "getLocationInformationEnabled", "()Z", "getVehicleStatusEnabled", "getChargingConnectors", "()Lcom/carplay/iap2/transport/EvChargingConnectors;", "getVehicleSpeedEnabled", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "copy", "equals", "other", "hashCode", "toString", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class Iap2IdentificationConfig {
    private final int carPlayUsbInterfaceNumber;
    private final EvChargingConnectors chargingConnectors;
    private final String externalAccessoryProtocol;
    private final String firmwareVersion;
    private final String hardwareVersion;
    private final String language;
    private final boolean locationInformationEnabled;
    private final String manufacturer;
    private final String modelIdentifier;
    private final String name;
    private final String serialNumber;
    private final boolean vehicleSpeedEnabled;
    private final boolean vehicleStatusEnabled;
    private final Iap2WirelessIdentification wireless;

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getName() {
        return this.name;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final Iap2WirelessIdentification getWireless() {
        return this.wireless;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final boolean getLocationInformationEnabled() {
        return this.locationInformationEnabled;
    }

    /* JADX INFO: renamed from: component12, reason: from getter */
    public final boolean getVehicleStatusEnabled() {
        return this.vehicleStatusEnabled;
    }

    /* JADX INFO: renamed from: component13, reason: from getter */
    public final EvChargingConnectors getChargingConnectors() {
        return this.chargingConnectors;
    }

    /* JADX INFO: renamed from: component14, reason: from getter */
    public final boolean getVehicleSpeedEnabled() {
        return this.vehicleSpeedEnabled;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getModelIdentifier() {
        return this.modelIdentifier;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getManufacturer() {
        return this.manufacturer;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final String getSerialNumber() {
        return this.serialNumber;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final String getFirmwareVersion() {
        return this.firmwareVersion;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final String getHardwareVersion() {
        return this.hardwareVersion;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final int getCarPlayUsbInterfaceNumber() {
        return this.carPlayUsbInterfaceNumber;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final String getLanguage() {
        return this.language;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final String getExternalAccessoryProtocol() {
        return this.externalAccessoryProtocol;
    }

    public final Iap2IdentificationConfig copy(String name, String modelIdentifier, String manufacturer, String serialNumber, String firmwareVersion, String hardwareVersion, int carPlayUsbInterfaceNumber, String language, String externalAccessoryProtocol, Iap2WirelessIdentification wireless, boolean locationInformationEnabled, boolean vehicleStatusEnabled, EvChargingConnectors chargingConnectors, boolean vehicleSpeedEnabled) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(modelIdentifier, "modelIdentifier");
        Intrinsics.checkNotNullParameter(manufacturer, "manufacturer");
        Intrinsics.checkNotNullParameter(serialNumber, "serialNumber");
        Intrinsics.checkNotNullParameter(firmwareVersion, "firmwareVersion");
        Intrinsics.checkNotNullParameter(hardwareVersion, "hardwareVersion");
        Intrinsics.checkNotNullParameter(language, "language");
        Intrinsics.checkNotNullParameter(externalAccessoryProtocol, "externalAccessoryProtocol");
        Intrinsics.checkNotNullParameter(chargingConnectors, "chargingConnectors");
        return new Iap2IdentificationConfig(name, modelIdentifier, manufacturer, serialNumber, firmwareVersion, hardwareVersion, carPlayUsbInterfaceNumber, language, externalAccessoryProtocol, wireless, locationInformationEnabled, vehicleStatusEnabled, chargingConnectors, vehicleSpeedEnabled);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Iap2IdentificationConfig)) {
            return false;
        }
        Iap2IdentificationConfig iap2IdentificationConfig = (Iap2IdentificationConfig) other;
        return Intrinsics.areEqual(this.name, iap2IdentificationConfig.name) && Intrinsics.areEqual(this.modelIdentifier, iap2IdentificationConfig.modelIdentifier) && Intrinsics.areEqual(this.manufacturer, iap2IdentificationConfig.manufacturer) && Intrinsics.areEqual(this.serialNumber, iap2IdentificationConfig.serialNumber) && Intrinsics.areEqual(this.firmwareVersion, iap2IdentificationConfig.firmwareVersion) && Intrinsics.areEqual(this.hardwareVersion, iap2IdentificationConfig.hardwareVersion) && this.carPlayUsbInterfaceNumber == iap2IdentificationConfig.carPlayUsbInterfaceNumber && Intrinsics.areEqual(this.language, iap2IdentificationConfig.language) && Intrinsics.areEqual(this.externalAccessoryProtocol, iap2IdentificationConfig.externalAccessoryProtocol) && Intrinsics.areEqual(this.wireless, iap2IdentificationConfig.wireless) && this.locationInformationEnabled == iap2IdentificationConfig.locationInformationEnabled && this.vehicleStatusEnabled == iap2IdentificationConfig.vehicleStatusEnabled && this.chargingConnectors == iap2IdentificationConfig.chargingConnectors && this.vehicleSpeedEnabled == iap2IdentificationConfig.vehicleSpeedEnabled;
    }

    public int hashCode() {
        return (((((((((((((((((((((((((this.name.hashCode() * 31) + this.modelIdentifier.hashCode()) * 31) + this.manufacturer.hashCode()) * 31) + this.serialNumber.hashCode()) * 31) + this.firmwareVersion.hashCode()) * 31) + this.hardwareVersion.hashCode()) * 31) + Integer.hashCode(this.carPlayUsbInterfaceNumber)) * 31) + this.language.hashCode()) * 31) + this.externalAccessoryProtocol.hashCode()) * 31) + (this.wireless == null ? 0 : this.wireless.hashCode())) * 31) + Boolean.hashCode(this.locationInformationEnabled)) * 31) + Boolean.hashCode(this.vehicleStatusEnabled)) * 31) + this.chargingConnectors.hashCode()) * 31) + Boolean.hashCode(this.vehicleSpeedEnabled);
    }

    public String toString() {
        return "Iap2IdentificationConfig(name=" + this.name + ", modelIdentifier=" + this.modelIdentifier + ", manufacturer=" + this.manufacturer + ", serialNumber=" + this.serialNumber + ", firmwareVersion=" + this.firmwareVersion + ", hardwareVersion=" + this.hardwareVersion + ", carPlayUsbInterfaceNumber=" + this.carPlayUsbInterfaceNumber + ", language=" + this.language + ", externalAccessoryProtocol=" + this.externalAccessoryProtocol + ", wireless=" + this.wireless + ", locationInformationEnabled=" + this.locationInformationEnabled + ", vehicleStatusEnabled=" + this.vehicleStatusEnabled + ", chargingConnectors=" + this.chargingConnectors + ", vehicleSpeedEnabled=" + this.vehicleSpeedEnabled + ")";
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Iap2IdentificationConfig(String name, String modelIdentifier, String manufacturer, String serialNumber, String firmwareVersion, String hardwareVersion, int i, String language, String externalAccessoryProtocol, Iap2WirelessIdentification iap2WirelessIdentification, boolean z, boolean z2, EvChargingConnectors chargingConnectors, boolean z3) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(modelIdentifier, "modelIdentifier");
        Intrinsics.checkNotNullParameter(manufacturer, "manufacturer");
        Intrinsics.checkNotNullParameter(serialNumber, "serialNumber");
        Intrinsics.checkNotNullParameter(firmwareVersion, "firmwareVersion");
        Intrinsics.checkNotNullParameter(hardwareVersion, "hardwareVersion");
        Intrinsics.checkNotNullParameter(language, "language");
        Intrinsics.checkNotNullParameter(externalAccessoryProtocol, "externalAccessoryProtocol");
        Intrinsics.checkNotNullParameter(chargingConnectors, "chargingConnectors");
        this.name = name;
        this.modelIdentifier = modelIdentifier;
        this.manufacturer = manufacturer;
        this.serialNumber = serialNumber;
        this.firmwareVersion = firmwareVersion;
        this.hardwareVersion = hardwareVersion;
        this.carPlayUsbInterfaceNumber = i;
        this.language = language;
        this.externalAccessoryProtocol = externalAccessoryProtocol;
        this.wireless = iap2WirelessIdentification;
        this.locationInformationEnabled = z;
        this.vehicleStatusEnabled = z2;
        this.chargingConnectors = chargingConnectors;
        this.vehicleSpeedEnabled = z3;
        boolean z4 = false;
        Iterator it = CollectionsKt.listOf((Object[]) new String[]{this.name, this.modelIdentifier, this.manufacturer, this.serialNumber, this.firmwareVersion, this.hardwareVersion}).iterator();
        while (it.hasNext()) {
            if (StringsKt.isBlank((String) it.next())) {
                throw new IllegalArgumentException("Identification identity strings must not be blank".toString());
            }
        }
        if (StringsKt.isBlank(this.language)) {
            throw new IllegalArgumentException("Identification language must not be blank".toString());
        }
        if (StringsKt.isBlank(this.externalAccessoryProtocol)) {
            throw new IllegalArgumentException("External accessory protocol must not be blank".toString());
        }
        List listListOf = CollectionsKt.listOf((Object[]) new String[]{this.name, this.modelIdentifier, this.manufacturer, this.serialNumber, this.firmwareVersion, this.hardwareVersion, this.language, this.externalAccessoryProtocol});
        Iterator it2 = listListOf.iterator();
        while (it2.hasNext()) {
            List list = listListOf;
            boolean z5 = z4;
            if (StringsKt.contains$default((String) it2.next(), z5 ? (char) 1 : (char) 0, z5, 2, (Object) null)) {
                throw new IllegalArgumentException("NUL-terminated identification strings must not contain U+0000".toString());
            }
            z4 = z5 ? 1 : 0;
            listListOf = list;
        }
        boolean z6 = z4;
        int i2 = this.carPlayUsbInterfaceNumber;
        if (((i2 < 0 || i2 >= 256) ? z6 ? 1 : 0 : 1) == 0) {
            throw new IllegalArgumentException("carPlayUsbInterfaceNumber must be in 0..255".toString());
        }
    }

    public /* synthetic */ Iap2IdentificationConfig(String str, String str2, String str3, String str4, String str5, String str6, int i, String str7, String str8, Iap2WirelessIdentification iap2WirelessIdentification, boolean z, boolean z2, EvChargingConnectors evChargingConnectors, boolean z3, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, str2, str3, str4, str5, str6, i, (i2 & 128) != 0 ? "en" : str7, (i2 & 256) != 0 ? "com.shilapi.xcertplay" : str8, (i2 & 512) != 0 ? null : iap2WirelessIdentification, (i2 & 1024) != 0 ? false : z, (i2 & 2048) != 0 ? false : z2, (i2 & 4096) != 0 ? EvChargingConnectors.CCS2_TYPE2 : evChargingConnectors, (i2 & 8192) != 0 ? false : z3);
    }

    public final String getName() {
        return this.name;
    }

    public final String getModelIdentifier() {
        return this.modelIdentifier;
    }

    public final String getManufacturer() {
        return this.manufacturer;
    }

    public final String getSerialNumber() {
        return this.serialNumber;
    }

    public final String getFirmwareVersion() {
        return this.firmwareVersion;
    }

    public final String getHardwareVersion() {
        return this.hardwareVersion;
    }

    public final int getCarPlayUsbInterfaceNumber() {
        return this.carPlayUsbInterfaceNumber;
    }

    public final String getLanguage() {
        return this.language;
    }

    public final String getExternalAccessoryProtocol() {
        return this.externalAccessoryProtocol;
    }

    public final Iap2WirelessIdentification getWireless() {
        return this.wireless;
    }

    public final boolean getLocationInformationEnabled() {
        return this.locationInformationEnabled;
    }

    public final boolean getVehicleStatusEnabled() {
        return this.vehicleStatusEnabled;
    }

    public final EvChargingConnectors getChargingConnectors() {
        return this.chargingConnectors;
    }

    public final boolean getVehicleSpeedEnabled() {
        return this.vehicleSpeedEnabled;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ Iap2IdentificationConfig(String str, String str2, String str3, String str4, String str5, String str6, Iap2WirelessIdentification iap2WirelessIdentification, String str7, String str8, int i, DefaultConstructorMarker defaultConstructorMarker) {
        String str9;
        String str10;
        if ((i & 128) == 0) {
            str9 = str7;
        } else {
            str9 = "en";
        }
        if ((i & 256) == 0) {
            str10 = str8;
        } else {
            str10 = "com.shilapi.xcertplay";
        }
        this(str, str2, str3, str4, str5, str6, iap2WirelessIdentification, str9, str10);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public Iap2IdentificationConfig(String name, String modelIdentifier, String manufacturer, String serialNumber, String firmwareVersion, String hardwareVersion, Iap2WirelessIdentification wireless, String language, String externalAccessoryProtocol) {
        this(name, modelIdentifier, manufacturer, serialNumber, firmwareVersion, hardwareVersion, 0, language, externalAccessoryProtocol, wireless, false, false, null, false, 15360, null);
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(modelIdentifier, "modelIdentifier");
        Intrinsics.checkNotNullParameter(manufacturer, "manufacturer");
        Intrinsics.checkNotNullParameter(serialNumber, "serialNumber");
        Intrinsics.checkNotNullParameter(firmwareVersion, "firmwareVersion");
        Intrinsics.checkNotNullParameter(hardwareVersion, "hardwareVersion");
        Intrinsics.checkNotNullParameter(wireless, "wireless");
        Intrinsics.checkNotNullParameter(language, "language");
        Intrinsics.checkNotNullParameter(externalAccessoryProtocol, "externalAccessoryProtocol");
    }
}
