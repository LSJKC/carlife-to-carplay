package com.carplay.iap2.controller;

import com.carplay.iap2.body.Iap2BodyReader;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: AccessoryVersionInfo.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\b\u0018\u0000 \u001a2\u00020\u0001:\u0001\u001aB/\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0003¢\u0006\u0004\b\u0007\u0010\bJ\u0006\u0010\u000e\u001a\u00020\u0003J\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0010\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0012\u001a\u0004\u0018\u00010\u0003HÆ\u0003J9\u0010\u0013\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001J\t\u0010\u0019\u001a\u00020\u0003HÖ\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\n¨\u0006\u001b"}, d2 = {"Lcom/carplay/iap2/controller/AccessoryVersionInfo;", "", "model", "", "manufacturer", "firmware", "hardware", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getModel", "()Ljava/lang/String;", "getManufacturer", "getFirmware", "getHardware", "diagnostic", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class AccessoryVersionInfo {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final String firmware;
    private final String hardware;
    private final String manufacturer;
    private final String model;

    public static /* synthetic */ AccessoryVersionInfo copy$default(AccessoryVersionInfo accessoryVersionInfo, String str, String str2, String str3, String str4, int i, Object obj) {
        if ((i & 1) != 0) {
            str = accessoryVersionInfo.model;
        }
        if ((i & 2) != 0) {
            str2 = accessoryVersionInfo.manufacturer;
        }
        if ((i & 4) != 0) {
            str3 = accessoryVersionInfo.firmware;
        }
        if ((i & 8) != 0) {
            str4 = accessoryVersionInfo.hardware;
        }
        return accessoryVersionInfo.copy(str, str2, str3, str4);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getModel() {
        return this.model;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getManufacturer() {
        return this.manufacturer;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getFirmware() {
        return this.firmware;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final String getHardware() {
        return this.hardware;
    }

    public final AccessoryVersionInfo copy(String model, String manufacturer, String firmware, String hardware) {
        return new AccessoryVersionInfo(model, manufacturer, firmware, hardware);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AccessoryVersionInfo)) {
            return false;
        }
        AccessoryVersionInfo accessoryVersionInfo = (AccessoryVersionInfo) other;
        return Intrinsics.areEqual(this.model, accessoryVersionInfo.model) && Intrinsics.areEqual(this.manufacturer, accessoryVersionInfo.manufacturer) && Intrinsics.areEqual(this.firmware, accessoryVersionInfo.firmware) && Intrinsics.areEqual(this.hardware, accessoryVersionInfo.hardware);
    }

    public int hashCode() {
        return ((((((this.model == null ? 0 : this.model.hashCode()) * 31) + (this.manufacturer == null ? 0 : this.manufacturer.hashCode())) * 31) + (this.firmware == null ? 0 : this.firmware.hashCode())) * 31) + (this.hardware != null ? this.hardware.hashCode() : 0);
    }

    public String toString() {
        return "AccessoryVersionInfo(model=" + this.model + ", manufacturer=" + this.manufacturer + ", firmware=" + this.firmware + ", hardware=" + this.hardware + ")";
    }

    public AccessoryVersionInfo(String model, String manufacturer, String firmware, String hardware) {
        this.model = model;
        this.manufacturer = manufacturer;
        this.firmware = firmware;
        this.hardware = hardware;
    }

    public final String getFirmware() {
        return this.firmware;
    }

    public final String getHardware() {
        return this.hardware;
    }

    public final String getManufacturer() {
        return this.manufacturer;
    }

    public final String getModel() {
        return this.model;
    }

    public final String diagnostic() {
        return "model=" + INSTANCE.safe$core_iap2(this.model) + " manufacturer=" + INSTANCE.safe$core_iap2(this.manufacturer) + " firmware=" + INSTANCE.safe$core_iap2(this.firmware) + " hardware=" + INSTANCE.safe$core_iap2(this.hardware);
    }

    /* JADX INFO: compiled from: AccessoryVersionInfo.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007J\u001f\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0000¢\u0006\u0002\b\fJ\u0017\u0010\r\u001a\u00020\t2\b\u0010\u000e\u001a\u0004\u0018\u00010\tH\u0000¢\u0006\u0002\b\u000f¨\u0006\u0010"}, d2 = {"Lcom/carplay/iap2/controller/AccessoryVersionInfo$Companion;", "", "<init>", "()V", "fromIdentification", "Lcom/carplay/iap2/controller/AccessoryVersionInfo;", "body", "Lcom/carplay/iap2/body/Iap2BodyReader;", "optionalVersionString", "", "id", "", "optionalVersionString$core_iap2", "safe", "value", "safe$core_iap2", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final AccessoryVersionInfo fromIdentification(Iap2BodyReader body) {
            Intrinsics.checkNotNullParameter(body, "body");
            return new AccessoryVersionInfo(optionalVersionString$core_iap2(body, 1), optionalVersionString$core_iap2(body, 2), optionalVersionString$core_iap2(body, 4), optionalVersionString$core_iap2(body, 5));
        }

        public final String optionalVersionString$core_iap2(Iap2BodyReader body, int id) {
            Intrinsics.checkNotNullParameter(body, "body");
            try {
                String it = body.optionalString(id);
                if (it != null) {
                    int length = it.length();
                    boolean z = false;
                    if (1 <= length && length < 257) {
                        z = true;
                    }
                    if (z) {
                        return it;
                    }
                    return null;
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        }

        public final String safe$core_iap2(String value) {
            if (value == null) {
                return "未提供";
            }
            return "\"" + StringsKt.take(new Regex("(?i)[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}").replace(new Regex("(?i)([0-9a-f]{2}[:-]){5}[0-9a-f]{2}").replace(new Regex("[\\p{Cntrl}\\\"]").replace(value, "?"), "[address]"), "[identifier]"), 128) + "\"";
        }
    }
}
