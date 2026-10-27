package com.carplay.iap2.message;

import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Iap2Messages.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\b\u0012\u0006\u0010\t\u001a\u00020\u0006¢\u0006\u0004\b\n\u0010\u000bJ\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0006HÆ\u0003J\u000f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00030\bHÆ\u0003J\t\u0010\u0018\u001a\u00020\u0006HÆ\u0003JA\u0010\u0019\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\b2\b\b\u0002\u0010\t\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001d\u001a\u00020\u0006HÖ\u0001J\t\u0010\u001e\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00030\b¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\t\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0010¨\u0006\u001f"}, d2 = {"Lcom/carplay/iap2/message/Iap2WirelessSessionParameters;", "", "ssid", "", "passphrase", "channel", "", "ipAddresses", "", "securityType", "<init>", "(Ljava/lang/String;Ljava/lang/String;ILjava/util/List;I)V", "getSsid", "()Ljava/lang/String;", "getPassphrase", "getChannel", "()I", "getIpAddresses", "()Ljava/util/List;", "getSecurityType", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class Iap2WirelessSessionParameters {
    private final int channel;
    private final List<String> ipAddresses;
    private final String passphrase;
    private final int securityType;
    private final String ssid;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ Iap2WirelessSessionParameters copy$default(Iap2WirelessSessionParameters iap2WirelessSessionParameters, String str, String str2, int i, List list, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            str = iap2WirelessSessionParameters.ssid;
        }
        if ((i3 & 2) != 0) {
            str2 = iap2WirelessSessionParameters.passphrase;
        }
        if ((i3 & 4) != 0) {
            i = iap2WirelessSessionParameters.channel;
        }
        if ((i3 & 8) != 0) {
            list = iap2WirelessSessionParameters.ipAddresses;
        }
        if ((i3 & 16) != 0) {
            i2 = iap2WirelessSessionParameters.securityType;
        }
        int i4 = i2;
        int i5 = i;
        return iap2WirelessSessionParameters.copy(str, str2, i5, list, i4);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getSsid() {
        return this.ssid;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getPassphrase() {
        return this.passphrase;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int getChannel() {
        return this.channel;
    }

    public final List<String> component4() {
        return this.ipAddresses;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final int getSecurityType() {
        return this.securityType;
    }

    public final Iap2WirelessSessionParameters copy(String ssid, String passphrase, int channel, List<String> ipAddresses, int securityType) {
        Intrinsics.checkNotNullParameter(ssid, "ssid");
        Intrinsics.checkNotNullParameter(passphrase, "passphrase");
        Intrinsics.checkNotNullParameter(ipAddresses, "ipAddresses");
        return new Iap2WirelessSessionParameters(ssid, passphrase, channel, ipAddresses, securityType);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Iap2WirelessSessionParameters)) {
            return false;
        }
        Iap2WirelessSessionParameters iap2WirelessSessionParameters = (Iap2WirelessSessionParameters) other;
        return Intrinsics.areEqual(this.ssid, iap2WirelessSessionParameters.ssid) && Intrinsics.areEqual(this.passphrase, iap2WirelessSessionParameters.passphrase) && this.channel == iap2WirelessSessionParameters.channel && Intrinsics.areEqual(this.ipAddresses, iap2WirelessSessionParameters.ipAddresses) && this.securityType == iap2WirelessSessionParameters.securityType;
    }

    public int hashCode() {
        return (((((((this.ssid.hashCode() * 31) + this.passphrase.hashCode()) * 31) + Integer.hashCode(this.channel)) * 31) + this.ipAddresses.hashCode()) * 31) + Integer.hashCode(this.securityType);
    }

    public String toString() {
        return "Iap2WirelessSessionParameters(ssid=" + this.ssid + ", passphrase=" + this.passphrase + ", channel=" + this.channel + ", ipAddresses=" + this.ipAddresses + ", securityType=" + this.securityType + ")";
    }

    /* JADX WARN: Code duplicated, block: B:13:0x003f  */
    public Iap2WirelessSessionParameters(String ssid, String passphrase, int channel, List<String> ipAddresses, int securityType) {
        boolean z;
        Intrinsics.checkNotNullParameter(ssid, "ssid");
        Intrinsics.checkNotNullParameter(passphrase, "passphrase");
        Intrinsics.checkNotNullParameter(ipAddresses, "ipAddresses");
        this.ssid = ssid;
        this.passphrase = passphrase;
        this.channel = channel;
        this.ipAddresses = ipAddresses;
        this.securityType = securityType;
        if (StringsKt.isBlank(this.ssid)) {
            throw new IllegalArgumentException("wireless SSID must not be blank".toString());
        }
        if (this.securityType == 0) {
            z = true;
        } else {
            if (this.passphrase.length() > 0) {
                z = true;
            } else {
                z = false;
            }
        }
        if (!z) {
            throw new IllegalArgumentException("secured wireless network requires a passphrase".toString());
        }
        int i = this.channel;
        if (!(i >= 0 && i < 256)) {
            throw new IllegalArgumentException("wireless channel must be in 0..255".toString());
        }
        if (this.ipAddresses.isEmpty()) {
            throw new IllegalArgumentException("wireless IP address list must not be empty".toString());
        }
        int i2 = this.securityType;
        if (!(i2 >= 0 && i2 < 256)) {
            throw new IllegalArgumentException("wireless security type must be in 0..255".toString());
        }
    }

    public final String getSsid() {
        return this.ssid;
    }

    public final String getPassphrase() {
        return this.passphrase;
    }

    public final int getChannel() {
        return this.channel;
    }

    public final List<String> getIpAddresses() {
        return this.ipAddresses;
    }

    public final int getSecurityType() {
        return this.securityType;
    }
}
