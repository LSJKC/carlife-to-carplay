package com.carplay.airplay;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AirPlaySession.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J1\u0010\u0012\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001J\t\u0010\u0018\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\n¨\u0006\u0019"}, d2 = {"Lcom/carplay/airplay/AirPlayDeviceInfo;", "", "name", "", "deviceId", "wifiMac", "model", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getName", "()Ljava/lang/String;", "getDeviceId", "getWifiMac", "getModel", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class AirPlayDeviceInfo {
    private final String deviceId;
    private final String model;
    private final String name;
    private final String wifiMac;

    public static /* synthetic */ AirPlayDeviceInfo copy$default(AirPlayDeviceInfo airPlayDeviceInfo, String str, String str2, String str3, String str4, int i, Object obj) {
        if ((i & 1) != 0) {
            str = airPlayDeviceInfo.name;
        }
        if ((i & 2) != 0) {
            str2 = airPlayDeviceInfo.deviceId;
        }
        if ((i & 4) != 0) {
            str3 = airPlayDeviceInfo.wifiMac;
        }
        if ((i & 8) != 0) {
            str4 = airPlayDeviceInfo.model;
        }
        return airPlayDeviceInfo.copy(str, str2, str3, str4);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getName() {
        return this.name;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getDeviceId() {
        return this.deviceId;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getWifiMac() {
        return this.wifiMac;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final String getModel() {
        return this.model;
    }

    public final AirPlayDeviceInfo copy(String name, String deviceId, String wifiMac, String model) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(deviceId, "deviceId");
        Intrinsics.checkNotNullParameter(wifiMac, "wifiMac");
        Intrinsics.checkNotNullParameter(model, "model");
        return new AirPlayDeviceInfo(name, deviceId, wifiMac, model);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AirPlayDeviceInfo)) {
            return false;
        }
        AirPlayDeviceInfo airPlayDeviceInfo = (AirPlayDeviceInfo) other;
        return Intrinsics.areEqual(this.name, airPlayDeviceInfo.name) && Intrinsics.areEqual(this.deviceId, airPlayDeviceInfo.deviceId) && Intrinsics.areEqual(this.wifiMac, airPlayDeviceInfo.wifiMac) && Intrinsics.areEqual(this.model, airPlayDeviceInfo.model);
    }

    public int hashCode() {
        return (((((this.name.hashCode() * 31) + this.deviceId.hashCode()) * 31) + this.wifiMac.hashCode()) * 31) + this.model.hashCode();
    }

    public String toString() {
        return "AirPlayDeviceInfo(name=" + this.name + ", deviceId=" + this.deviceId + ", wifiMac=" + this.wifiMac + ", model=" + this.model + ")";
    }

    public AirPlayDeviceInfo(String name, String deviceId, String wifiMac, String model) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(deviceId, "deviceId");
        Intrinsics.checkNotNullParameter(wifiMac, "wifiMac");
        Intrinsics.checkNotNullParameter(model, "model");
        this.name = name;
        this.deviceId = deviceId;
        this.wifiMac = wifiMac;
        this.model = model;
    }

    public final String getName() {
        return this.name;
    }

    public final String getDeviceId() {
        return this.deviceId;
    }

    public final String getWifiMac() {
        return this.wifiMac;
    }

    public final String getModel() {
        return this.model;
    }
}
