package com.carplay.airplay;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AirPlayConfig.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b3\b\u0086\b\u0018\u00002\u00020\u0001B¹\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\b\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\r\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u000b¢\u0006\u0004\b\u001a\u0010\u001bJ\t\u00103\u001a\u00020\u0003HÆ\u0003J\t\u00104\u001a\u00020\u0003HÆ\u0003J\t\u00105\u001a\u00020\u0003HÆ\u0003J\t\u00106\u001a\u00020\u0003HÆ\u0003J\t\u00107\u001a\u00020\bHÆ\u0003J\u000b\u00108\u001a\u0004\u0018\u00010\bHÆ\u0003J\t\u00109\u001a\u00020\u000bHÆ\u0003J\t\u0010:\u001a\u00020\rHÆ\u0003J\t\u0010;\u001a\u00020\rHÆ\u0003J\t\u0010<\u001a\u00020\u000bHÆ\u0003J\t\u0010=\u001a\u00020\u000bHÆ\u0003J\t\u0010>\u001a\u00020\u000bHÆ\u0003J\t\u0010?\u001a\u00020\u0003HÆ\u0003J\t\u0010@\u001a\u00020\u0003HÆ\u0003J\t\u0010A\u001a\u00020\u0003HÆ\u0003J\u000f\u0010B\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016HÆ\u0003J\t\u0010C\u001a\u00020\u000bHÆ\u0003J\t\u0010D\u001a\u00020\u000bHÆ\u0003JÅ\u0001\u0010E\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\r2\b\b\u0002\u0010\u000f\u001a\u00020\u000b2\b\b\u0002\u0010\u0010\u001a\u00020\u000b2\b\b\u0002\u0010\u0011\u001a\u00020\u000b2\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u00032\u000e\b\u0002\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\b\b\u0002\u0010\u0018\u001a\u00020\u000b2\b\b\u0002\u0010\u0019\u001a\u00020\u000bHÆ\u0001J\u0013\u0010F\u001a\u00020\u000b2\b\u0010G\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010H\u001a\u00020\rHÖ\u0001J\t\u0010I\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001dR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001dR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b \u0010\u001dR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u0013\u0010\t\u001a\u0004\u0018\u00010\b¢\u0006\b\n\u0000\u001a\u0004\b#\u0010\"R\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0011\u0010\f\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b&\u0010'R\u0011\u0010\u000e\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b(\u0010'R\u0011\u0010\u000f\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b)\u0010%R\u0011\u0010\u0010\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b*\u0010%R\u0011\u0010\u0011\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b+\u0010%R\u0011\u0010\u0012\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001dR\u0011\u0010\u0013\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b-\u0010\u001dR\u0011\u0010\u0014\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b.\u0010\u001dR\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016¢\u0006\b\n\u0000\u001a\u0004\b/\u00100R\u0011\u0010\u0018\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b1\u0010%R\u0011\u0010\u0019\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b2\u0010%¨\u0006J"}, d2 = {"Lcom/carplay/airplay/AirPlayConfig;", "", "deviceName", "", "deviceId", "btMac", "sourceVersion", "main", "Lcom/carplay/airplay/AirPlayDisplayConfig;", "cluster", "rightHandDrive", "", "port", "", "entertainmentSampleRate", "hevc", "disableAudioOutput", "microphone", "manufacturer", "model", "oemLabel", "icons", "", "Lcom/carplay/airplay/AirPlayIcon;", "videoInCar", "mainBufferedAudio", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/carplay/airplay/AirPlayDisplayConfig;Lcom/carplay/airplay/AirPlayDisplayConfig;ZIIZZZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;ZZ)V", "getDeviceName", "()Ljava/lang/String;", "getDeviceId", "getBtMac", "getSourceVersion", "getMain", "()Lcom/carplay/airplay/AirPlayDisplayConfig;", "getCluster", "getRightHandDrive", "()Z", "getPort", "()I", "getEntertainmentSampleRate", "getHevc", "getDisableAudioOutput", "getMicrophone", "getManufacturer", "getModel", "getOemLabel", "getIcons", "()Ljava/util/List;", "getVideoInCar", "getMainBufferedAudio", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "copy", "equals", "other", "hashCode", "toString", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class AirPlayConfig {
    private final String btMac;
    private final AirPlayDisplayConfig cluster;
    private final String deviceId;
    private final String deviceName;
    private final boolean disableAudioOutput;
    private final int entertainmentSampleRate;
    private final boolean hevc;
    private final List<AirPlayIcon> icons;
    private final AirPlayDisplayConfig main;
    private final boolean mainBufferedAudio;
    private final String manufacturer;
    private final boolean microphone;
    private final String model;
    private final String oemLabel;
    private final int port;
    private final boolean rightHandDrive;
    private final String sourceVersion;
    private final boolean videoInCar;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ AirPlayConfig copy$default(AirPlayConfig airPlayConfig, String str, String str2, String str3, String str4, AirPlayDisplayConfig airPlayDisplayConfig, AirPlayDisplayConfig airPlayDisplayConfig2, boolean z, int i, int i2, boolean z2, boolean z3, boolean z4, String str5, String str6, String str7, List list, boolean z5, boolean z6, int i3, Object obj) {
        boolean z7;
        boolean z8;
        String str8 = (i3 & 1) != 0 ? airPlayConfig.deviceName : str;
        String str9 = (i3 & 2) != 0 ? airPlayConfig.deviceId : str2;
        String str10 = (i3 & 4) != 0 ? airPlayConfig.btMac : str3;
        String str11 = (i3 & 8) != 0 ? airPlayConfig.sourceVersion : str4;
        AirPlayDisplayConfig airPlayDisplayConfig3 = (i3 & 16) != 0 ? airPlayConfig.main : airPlayDisplayConfig;
        AirPlayDisplayConfig airPlayDisplayConfig4 = (i3 & 32) != 0 ? airPlayConfig.cluster : airPlayDisplayConfig2;
        boolean z9 = (i3 & 64) != 0 ? airPlayConfig.rightHandDrive : z;
        int i4 = (i3 & 128) != 0 ? airPlayConfig.port : i;
        int i5 = (i3 & 256) != 0 ? airPlayConfig.entertainmentSampleRate : i2;
        boolean z10 = (i3 & 512) != 0 ? airPlayConfig.hevc : z2;
        boolean z11 = (i3 & 1024) != 0 ? airPlayConfig.disableAudioOutput : z3;
        boolean z12 = (i3 & 2048) != 0 ? airPlayConfig.microphone : z4;
        String str12 = (i3 & 4096) != 0 ? airPlayConfig.manufacturer : str5;
        String str13 = (i3 & 8192) != 0 ? airPlayConfig.model : str6;
        String str14 = str8;
        String str15 = (i3 & 16384) != 0 ? airPlayConfig.oemLabel : str7;
        List list2 = (i3 & 32768) != 0 ? airPlayConfig.icons : list;
        boolean z13 = (i3 & 65536) != 0 ? airPlayConfig.videoInCar : z5;
        if ((i3 & 131072) != 0) {
            z8 = z13;
            z7 = airPlayConfig.mainBufferedAudio;
        } else {
            z7 = z6;
            z8 = z13;
        }
        return airPlayConfig.copy(str14, str9, str10, str11, airPlayDisplayConfig3, airPlayDisplayConfig4, z9, i4, i5, z10, z11, z12, str12, str13, str15, list2, z8, z7);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getDeviceName() {
        return this.deviceName;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final boolean getHevc() {
        return this.hevc;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final boolean getDisableAudioOutput() {
        return this.disableAudioOutput;
    }

    /* JADX INFO: renamed from: component12, reason: from getter */
    public final boolean getMicrophone() {
        return this.microphone;
    }

    /* JADX INFO: renamed from: component13, reason: from getter */
    public final String getManufacturer() {
        return this.manufacturer;
    }

    /* JADX INFO: renamed from: component14, reason: from getter */
    public final String getModel() {
        return this.model;
    }

    /* JADX INFO: renamed from: component15, reason: from getter */
    public final String getOemLabel() {
        return this.oemLabel;
    }

    public final List<AirPlayIcon> component16() {
        return this.icons;
    }

    /* JADX INFO: renamed from: component17, reason: from getter */
    public final boolean getVideoInCar() {
        return this.videoInCar;
    }

    /* JADX INFO: renamed from: component18, reason: from getter */
    public final boolean getMainBufferedAudio() {
        return this.mainBufferedAudio;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getDeviceId() {
        return this.deviceId;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getBtMac() {
        return this.btMac;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final String getSourceVersion() {
        return this.sourceVersion;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final AirPlayDisplayConfig getMain() {
        return this.main;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final AirPlayDisplayConfig getCluster() {
        return this.cluster;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final boolean getRightHandDrive() {
        return this.rightHandDrive;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final int getPort() {
        return this.port;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final int getEntertainmentSampleRate() {
        return this.entertainmentSampleRate;
    }

    public final AirPlayConfig copy(String deviceName, String deviceId, String btMac, String sourceVersion, AirPlayDisplayConfig main, AirPlayDisplayConfig cluster, boolean rightHandDrive, int port, int entertainmentSampleRate, boolean hevc, boolean disableAudioOutput, boolean microphone, String manufacturer, String model, String oemLabel, List<AirPlayIcon> icons, boolean videoInCar, boolean mainBufferedAudio) {
        Intrinsics.checkNotNullParameter(deviceName, "deviceName");
        Intrinsics.checkNotNullParameter(deviceId, "deviceId");
        Intrinsics.checkNotNullParameter(btMac, "btMac");
        Intrinsics.checkNotNullParameter(sourceVersion, "sourceVersion");
        Intrinsics.checkNotNullParameter(main, "main");
        Intrinsics.checkNotNullParameter(manufacturer, "manufacturer");
        Intrinsics.checkNotNullParameter(model, "model");
        Intrinsics.checkNotNullParameter(oemLabel, "oemLabel");
        Intrinsics.checkNotNullParameter(icons, "icons");
        return new AirPlayConfig(deviceName, deviceId, btMac, sourceVersion, main, cluster, rightHandDrive, port, entertainmentSampleRate, hevc, disableAudioOutput, microphone, manufacturer, model, oemLabel, icons, videoInCar, mainBufferedAudio);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AirPlayConfig)) {
            return false;
        }
        AirPlayConfig airPlayConfig = (AirPlayConfig) other;
        return Intrinsics.areEqual(this.deviceName, airPlayConfig.deviceName) && Intrinsics.areEqual(this.deviceId, airPlayConfig.deviceId) && Intrinsics.areEqual(this.btMac, airPlayConfig.btMac) && Intrinsics.areEqual(this.sourceVersion, airPlayConfig.sourceVersion) && Intrinsics.areEqual(this.main, airPlayConfig.main) && Intrinsics.areEqual(this.cluster, airPlayConfig.cluster) && this.rightHandDrive == airPlayConfig.rightHandDrive && this.port == airPlayConfig.port && this.entertainmentSampleRate == airPlayConfig.entertainmentSampleRate && this.hevc == airPlayConfig.hevc && this.disableAudioOutput == airPlayConfig.disableAudioOutput && this.microphone == airPlayConfig.microphone && Intrinsics.areEqual(this.manufacturer, airPlayConfig.manufacturer) && Intrinsics.areEqual(this.model, airPlayConfig.model) && Intrinsics.areEqual(this.oemLabel, airPlayConfig.oemLabel) && Intrinsics.areEqual(this.icons, airPlayConfig.icons) && this.videoInCar == airPlayConfig.videoInCar && this.mainBufferedAudio == airPlayConfig.mainBufferedAudio;
    }

    public int hashCode() {
        return (((((((((((((((((((((((((((((((((this.deviceName.hashCode() * 31) + this.deviceId.hashCode()) * 31) + this.btMac.hashCode()) * 31) + this.sourceVersion.hashCode()) * 31) + this.main.hashCode()) * 31) + (this.cluster == null ? 0 : this.cluster.hashCode())) * 31) + Boolean.hashCode(this.rightHandDrive)) * 31) + Integer.hashCode(this.port)) * 31) + Integer.hashCode(this.entertainmentSampleRate)) * 31) + Boolean.hashCode(this.hevc)) * 31) + Boolean.hashCode(this.disableAudioOutput)) * 31) + Boolean.hashCode(this.microphone)) * 31) + this.manufacturer.hashCode()) * 31) + this.model.hashCode()) * 31) + this.oemLabel.hashCode()) * 31) + this.icons.hashCode()) * 31) + Boolean.hashCode(this.videoInCar)) * 31) + Boolean.hashCode(this.mainBufferedAudio);
    }

    public String toString() {
        return "AirPlayConfig(deviceName=" + this.deviceName + ", deviceId=" + this.deviceId + ", btMac=" + this.btMac + ", sourceVersion=" + this.sourceVersion + ", main=" + this.main + ", cluster=" + this.cluster + ", rightHandDrive=" + this.rightHandDrive + ", port=" + this.port + ", entertainmentSampleRate=" + this.entertainmentSampleRate + ", hevc=" + this.hevc + ", disableAudioOutput=" + this.disableAudioOutput + ", microphone=" + this.microphone + ", manufacturer=" + this.manufacturer + ", model=" + this.model + ", oemLabel=" + this.oemLabel + ", icons=" + this.icons + ", videoInCar=" + this.videoInCar + ", mainBufferedAudio=" + this.mainBufferedAudio + ")";
    }

    public AirPlayConfig(String deviceName, String deviceId, String btMac, String sourceVersion, AirPlayDisplayConfig main, AirPlayDisplayConfig cluster, boolean rightHandDrive, int port, int entertainmentSampleRate, boolean hevc, boolean disableAudioOutput, boolean microphone, String manufacturer, String model, String oemLabel, List<AirPlayIcon> icons, boolean videoInCar, boolean mainBufferedAudio) {
        Intrinsics.checkNotNullParameter(deviceName, "deviceName");
        Intrinsics.checkNotNullParameter(deviceId, "deviceId");
        Intrinsics.checkNotNullParameter(btMac, "btMac");
        Intrinsics.checkNotNullParameter(sourceVersion, "sourceVersion");
        Intrinsics.checkNotNullParameter(main, "main");
        Intrinsics.checkNotNullParameter(manufacturer, "manufacturer");
        Intrinsics.checkNotNullParameter(model, "model");
        Intrinsics.checkNotNullParameter(oemLabel, "oemLabel");
        Intrinsics.checkNotNullParameter(icons, "icons");
        this.deviceName = deviceName;
        this.deviceId = deviceId;
        this.btMac = btMac;
        this.sourceVersion = sourceVersion;
        this.main = main;
        this.cluster = cluster;
        this.rightHandDrive = rightHandDrive;
        this.port = port;
        this.entertainmentSampleRate = entertainmentSampleRate;
        this.hevc = hevc;
        this.disableAudioOutput = disableAudioOutput;
        this.microphone = microphone;
        this.manufacturer = manufacturer;
        this.model = model;
        this.oemLabel = oemLabel;
        this.icons = icons;
        this.videoInCar = videoInCar;
        this.mainBufferedAudio = mainBufferedAudio;
    }

    public /* synthetic */ AirPlayConfig(String str, String str2, String str3, String str4, AirPlayDisplayConfig airPlayDisplayConfig, AirPlayDisplayConfig airPlayDisplayConfig2, boolean z, int i, int i2, boolean z2, boolean z3, boolean z4, String str5, String str6, String str7, List list, boolean z5, boolean z6, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, str2, str3, str4, airPlayDisplayConfig, (i3 & 32) != 0 ? null : airPlayDisplayConfig2, (i3 & 64) != 0 ? false : z, (i3 & 128) != 0 ? 7000 : i, (i3 & 256) != 0 ? 48000 : i2, (i3 & 512) != 0 ? false : z2, (i3 & 1024) != 0 ? false : z3, (i3 & 2048) != 0 ? false : z4, (i3 & 4096) != 0 ? "xcertplay" : str5, (i3 & 8192) != 0 ? "xcertplay" : str6, (i3 & 16384) != 0 ? "xcertplay" : str7, (32768 & i3) != 0 ? CollectionsKt.emptyList() : list, (65536 & i3) != 0 ? false : z5, (i3 & 131072) != 0 ? false : z6);
    }

    public final String getDeviceName() {
        return this.deviceName;
    }

    public final String getDeviceId() {
        return this.deviceId;
    }

    public final String getBtMac() {
        return this.btMac;
    }

    public final String getSourceVersion() {
        return this.sourceVersion;
    }

    public final AirPlayDisplayConfig getMain() {
        return this.main;
    }

    public final AirPlayDisplayConfig getCluster() {
        return this.cluster;
    }

    public final boolean getRightHandDrive() {
        return this.rightHandDrive;
    }

    public final int getPort() {
        return this.port;
    }

    public final int getEntertainmentSampleRate() {
        return this.entertainmentSampleRate;
    }

    public final boolean getHevc() {
        return this.hevc;
    }

    public final boolean getDisableAudioOutput() {
        return this.disableAudioOutput;
    }

    public final boolean getMicrophone() {
        return this.microphone;
    }

    public final String getManufacturer() {
        return this.manufacturer;
    }

    public final String getModel() {
        return this.model;
    }

    public final String getOemLabel() {
        return this.oemLabel;
    }

    public final List<AirPlayIcon> getIcons() {
        return this.icons;
    }

    public final boolean getVideoInCar() {
        return this.videoInCar;
    }

    public final boolean getMainBufferedAudio() {
        return this.mainBufferedAudio;
    }
}
