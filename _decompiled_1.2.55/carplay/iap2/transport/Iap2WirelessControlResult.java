package com.carplay.iap2.transport;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2WirelessControlClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u001d\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BG\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\u0006\u0010\t\u001a\u00020\u0007\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\u0007\u0012\u0006\u0010\r\u001a\u00020\u000b¢\u0006\u0004\b\u000e\u0010\u000fJ\t\u0010\u001c\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001d\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001e\u001a\u00020\u0007HÆ\u0003J\t\u0010\u001f\u001a\u00020\u0007HÆ\u0003J\t\u0010 \u001a\u00020\u0007HÆ\u0003J\t\u0010!\u001a\u00020\u000bHÆ\u0003J\t\u0010\"\u001a\u00020\u0007HÆ\u0003J\t\u0010#\u001a\u00020\u000bHÆ\u0003JY\u0010$\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u00072\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u00072\b\b\u0002\u0010\r\u001a\u00020\u000bHÆ\u0001J\u0013\u0010%\u001a\u00020\u000b2\b\u0010&\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010'\u001a\u00020\u0007HÖ\u0001J\t\u0010(\u001a\u00020)HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\b\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0015R\u0011\u0010\t\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0015R\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\f\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0015R\u0011\u0010\r\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0019¨\u0006*"}, d2 = {"Lcom/carplay/iap2/transport/Iap2WirelessControlResult;", "", "terminal", "Lcom/carplay/iap2/transport/Iap2WirelessControlTerminal;", "stage", "Lcom/carplay/iap2/transport/Iap2WirelessControlStage;", "forwardedFrames", "", "wifiConfigurationsSent", "carPlayStartSessionsSent", "transportNotificationSeen", "", "postTransportWiFiConfigurationsSent", "wirelessCarPlayAvailableSeen", "<init>", "(Lcom/carplay/iap2/transport/Iap2WirelessControlTerminal;Lcom/carplay/iap2/transport/Iap2WirelessControlStage;IIIZIZ)V", "getTerminal", "()Lcom/carplay/iap2/transport/Iap2WirelessControlTerminal;", "getStage", "()Lcom/carplay/iap2/transport/Iap2WirelessControlStage;", "getForwardedFrames", "()I", "getWifiConfigurationsSent", "getCarPlayStartSessionsSent", "getTransportNotificationSeen", "()Z", "getPostTransportWiFiConfigurationsSent", "getWirelessCarPlayAvailableSeen", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "hashCode", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class Iap2WirelessControlResult {
    private final int carPlayStartSessionsSent;
    private final int forwardedFrames;
    private final int postTransportWiFiConfigurationsSent;
    private final Iap2WirelessControlStage stage;
    private final Iap2WirelessControlTerminal terminal;
    private final boolean transportNotificationSeen;
    private final int wifiConfigurationsSent;
    private final boolean wirelessCarPlayAvailableSeen;

    public static /* synthetic */ Iap2WirelessControlResult copy$default(Iap2WirelessControlResult iap2WirelessControlResult, Iap2WirelessControlTerminal iap2WirelessControlTerminal, Iap2WirelessControlStage iap2WirelessControlStage, int i, int i2, int i3, boolean z, int i4, boolean z2, int i5, Object obj) {
        if ((i5 & 1) != 0) {
            iap2WirelessControlTerminal = iap2WirelessControlResult.terminal;
        }
        if ((i5 & 2) != 0) {
            iap2WirelessControlStage = iap2WirelessControlResult.stage;
        }
        if ((i5 & 4) != 0) {
            i = iap2WirelessControlResult.forwardedFrames;
        }
        if ((i5 & 8) != 0) {
            i2 = iap2WirelessControlResult.wifiConfigurationsSent;
        }
        if ((i5 & 16) != 0) {
            i3 = iap2WirelessControlResult.carPlayStartSessionsSent;
        }
        if ((i5 & 32) != 0) {
            z = iap2WirelessControlResult.transportNotificationSeen;
        }
        if ((i5 & 64) != 0) {
            i4 = iap2WirelessControlResult.postTransportWiFiConfigurationsSent;
        }
        if ((i5 & 128) != 0) {
            z2 = iap2WirelessControlResult.wirelessCarPlayAvailableSeen;
        }
        int i6 = i4;
        boolean z3 = z2;
        int i7 = i3;
        boolean z4 = z;
        return iap2WirelessControlResult.copy(iap2WirelessControlTerminal, iap2WirelessControlStage, i, i2, i7, z4, i6, z3);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final Iap2WirelessControlTerminal getTerminal() {
        return this.terminal;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final Iap2WirelessControlStage getStage() {
        return this.stage;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int getForwardedFrames() {
        return this.forwardedFrames;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int getWifiConfigurationsSent() {
        return this.wifiConfigurationsSent;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final int getCarPlayStartSessionsSent() {
        return this.carPlayStartSessionsSent;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final boolean getTransportNotificationSeen() {
        return this.transportNotificationSeen;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final int getPostTransportWiFiConfigurationsSent() {
        return this.postTransportWiFiConfigurationsSent;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final boolean getWirelessCarPlayAvailableSeen() {
        return this.wirelessCarPlayAvailableSeen;
    }

    public final Iap2WirelessControlResult copy(Iap2WirelessControlTerminal terminal, Iap2WirelessControlStage stage, int forwardedFrames, int wifiConfigurationsSent, int carPlayStartSessionsSent, boolean transportNotificationSeen, int postTransportWiFiConfigurationsSent, boolean wirelessCarPlayAvailableSeen) {
        Intrinsics.checkNotNullParameter(terminal, "terminal");
        Intrinsics.checkNotNullParameter(stage, "stage");
        return new Iap2WirelessControlResult(terminal, stage, forwardedFrames, wifiConfigurationsSent, carPlayStartSessionsSent, transportNotificationSeen, postTransportWiFiConfigurationsSent, wirelessCarPlayAvailableSeen);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Iap2WirelessControlResult)) {
            return false;
        }
        Iap2WirelessControlResult iap2WirelessControlResult = (Iap2WirelessControlResult) other;
        return this.terminal == iap2WirelessControlResult.terminal && this.stage == iap2WirelessControlResult.stage && this.forwardedFrames == iap2WirelessControlResult.forwardedFrames && this.wifiConfigurationsSent == iap2WirelessControlResult.wifiConfigurationsSent && this.carPlayStartSessionsSent == iap2WirelessControlResult.carPlayStartSessionsSent && this.transportNotificationSeen == iap2WirelessControlResult.transportNotificationSeen && this.postTransportWiFiConfigurationsSent == iap2WirelessControlResult.postTransportWiFiConfigurationsSent && this.wirelessCarPlayAvailableSeen == iap2WirelessControlResult.wirelessCarPlayAvailableSeen;
    }

    public int hashCode() {
        return (((((((((((((this.terminal.hashCode() * 31) + this.stage.hashCode()) * 31) + Integer.hashCode(this.forwardedFrames)) * 31) + Integer.hashCode(this.wifiConfigurationsSent)) * 31) + Integer.hashCode(this.carPlayStartSessionsSent)) * 31) + Boolean.hashCode(this.transportNotificationSeen)) * 31) + Integer.hashCode(this.postTransportWiFiConfigurationsSent)) * 31) + Boolean.hashCode(this.wirelessCarPlayAvailableSeen);
    }

    public String toString() {
        return "Iap2WirelessControlResult(terminal=" + this.terminal + ", stage=" + this.stage + ", forwardedFrames=" + this.forwardedFrames + ", wifiConfigurationsSent=" + this.wifiConfigurationsSent + ", carPlayStartSessionsSent=" + this.carPlayStartSessionsSent + ", transportNotificationSeen=" + this.transportNotificationSeen + ", postTransportWiFiConfigurationsSent=" + this.postTransportWiFiConfigurationsSent + ", wirelessCarPlayAvailableSeen=" + this.wirelessCarPlayAvailableSeen + ")";
    }

    public Iap2WirelessControlResult(Iap2WirelessControlTerminal terminal, Iap2WirelessControlStage stage, int forwardedFrames, int wifiConfigurationsSent, int carPlayStartSessionsSent, boolean transportNotificationSeen, int postTransportWiFiConfigurationsSent, boolean wirelessCarPlayAvailableSeen) {
        Intrinsics.checkNotNullParameter(terminal, "terminal");
        Intrinsics.checkNotNullParameter(stage, "stage");
        this.terminal = terminal;
        this.stage = stage;
        this.forwardedFrames = forwardedFrames;
        this.wifiConfigurationsSent = wifiConfigurationsSent;
        this.carPlayStartSessionsSent = carPlayStartSessionsSent;
        this.transportNotificationSeen = transportNotificationSeen;
        this.postTransportWiFiConfigurationsSent = postTransportWiFiConfigurationsSent;
        this.wirelessCarPlayAvailableSeen = wirelessCarPlayAvailableSeen;
    }

    public final Iap2WirelessControlTerminal getTerminal() {
        return this.terminal;
    }

    public final Iap2WirelessControlStage getStage() {
        return this.stage;
    }

    public final int getForwardedFrames() {
        return this.forwardedFrames;
    }

    public final int getWifiConfigurationsSent() {
        return this.wifiConfigurationsSent;
    }

    public final int getCarPlayStartSessionsSent() {
        return this.carPlayStartSessionsSent;
    }

    public final boolean getTransportNotificationSeen() {
        return this.transportNotificationSeen;
    }

    public final int getPostTransportWiFiConfigurationsSent() {
        return this.postTransportWiFiConfigurationsSent;
    }

    public final boolean getWirelessCarPlayAvailableSeen() {
        return this.wirelessCarPlayAvailableSeen;
    }
}
