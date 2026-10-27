package com.carplay.iap2.transport;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Iap2WirelessControlClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0010\u0012\n\u0002\b\u0012\u0018\u00002\u00020\u0001Ba\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\n\u0012\u0006\u0010\u000b\u001a\u00020\u0006\u0012\u0006\u0010\f\u001a\u00020\u0003\u0012\u0006\u0010\r\u001a\u00020\u0003\u0012\u0006\u0010\u000e\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0010¢\u0006\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0014R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u000b\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0017R\u0011\u0010\f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0014R\u0011\u0010\r\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0014R\u0011\u0010\u000e\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0014R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00030\n¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u0010¢\u0006\b\n\u0000\u001a\u0004\b \u0010!¨\u0006\""}, d2 = {"Lcom/carplay/iap2/transport/Iap2WirelessCarPlayEndpoint;", "", "ssid", "", "passphrase", "channel", "", "security", "Lcom/carplay/iap2/transport/Iap2WirelessSecurity;", "ipAddresses", "", "airPlayPort", "deviceIdentifier", "publicKey", "sourceVersion", "accessPointBssid", "", "<init>", "(Ljava/lang/String;Ljava/lang/String;ILcom/carplay/iap2/transport/Iap2WirelessSecurity;Ljava/util/List;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;[B)V", "getSsid", "()Ljava/lang/String;", "getPassphrase", "getChannel", "()I", "getSecurity", "()Lcom/carplay/iap2/transport/Iap2WirelessSecurity;", "getAirPlayPort", "getDeviceIdentifier", "getPublicKey", "getSourceVersion", "getIpAddresses", "()Ljava/util/List;", "getAccessPointBssid", "()[B", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2WirelessCarPlayEndpoint {
    private final byte[] accessPointBssid;
    private final int airPlayPort;
    private final int channel;
    private final String deviceIdentifier;
    private final List<String> ipAddresses;
    private final String passphrase;
    private final String publicKey;
    private final Iap2WirelessSecurity security;
    private final String sourceVersion;
    private final String ssid;

    public Iap2WirelessCarPlayEndpoint(String ssid, String passphrase, int channel, Iap2WirelessSecurity security, List<String> ipAddresses, int airPlayPort, String deviceIdentifier, String publicKey, String sourceVersion, byte[] accessPointBssid) {
        byte[] bArrCopyOf;
        boolean z;
        Intrinsics.checkNotNullParameter(ssid, "ssid");
        Intrinsics.checkNotNullParameter(passphrase, "passphrase");
        Intrinsics.checkNotNullParameter(security, "security");
        Intrinsics.checkNotNullParameter(ipAddresses, "ipAddresses");
        Intrinsics.checkNotNullParameter(deviceIdentifier, "deviceIdentifier");
        Intrinsics.checkNotNullParameter(publicKey, "publicKey");
        Intrinsics.checkNotNullParameter(sourceVersion, "sourceVersion");
        this.ssid = ssid;
        this.passphrase = passphrase;
        this.channel = channel;
        this.security = security;
        this.airPlayPort = airPlayPort;
        this.deviceIdentifier = deviceIdentifier;
        this.publicKey = publicKey;
        this.sourceVersion = sourceVersion;
        this.ipAddresses = CollectionsKt.toList(ipAddresses);
        if (accessPointBssid != null) {
            bArrCopyOf = Arrays.copyOf(accessPointBssid, accessPointBssid.length);
            Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        } else {
            bArrCopyOf = null;
        }
        this.accessPointBssid = bArrCopyOf;
        if (!(accessPointBssid == null || accessPointBssid.length == 6)) {
            throw new IllegalArgumentException("AP address must contain six bytes".toString());
        }
        if (StringsKt.isBlank(this.ssid)) {
            throw new IllegalArgumentException("ssid is required and must not be blank".toString());
        }
        if (StringsKt.contains$default((CharSequence) this.ssid, (char) 0, false, 2, (Object) null)) {
            throw new IllegalArgumentException("ssid must not contain U+0000".toString());
        }
        if (StringsKt.contains$default((CharSequence) this.passphrase, (char) 0, false, 2, (Object) null)) {
            throw new IllegalArgumentException("passphrase must not contain U+0000".toString());
        }
        if (this.security != Iap2WirelessSecurity.NONE) {
            if (!(this.passphrase.length() > 0)) {
                throw new IllegalArgumentException("passphrase is required for secured Wi-Fi".toString());
            }
        }
        int i = this.channel;
        if (!(i >= 0 && i < 256)) {
            throw new IllegalArgumentException("channel must be in 0..255".toString());
        }
        if (ipAddresses.isEmpty()) {
            throw new IllegalArgumentException("At least one wireless IP address is required".toString());
        }
        List<String> $this$all$iv = ipAddresses;
        if (!($this$all$iv instanceof Collection) || !$this$all$iv.isEmpty()) {
            Iterator it = $this$all$iv.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = true;
                    break;
                }
                Object element$iv = it.next();
                String it2 = (String) element$iv;
                if (!((StringsKt.isBlank(it2) || StringsKt.contains$default((CharSequence) it2, (char) 0, false, 2, (Object) null)) ? false : true)) {
                    z = false;
                    break;
                }
            }
        } else {
            z = true;
        }
        if (!z) {
            throw new IllegalArgumentException("Every wireless IP address must be non-blank and must not contain U+0000".toString());
        }
        int i2 = this.airPlayPort;
        if (!(1 <= i2 && i2 < 65536)) {
            throw new IllegalArgumentException("airPlayPort must be in 1..65535".toString());
        }
        if (StringsKt.isBlank(this.deviceIdentifier)) {
            throw new IllegalArgumentException("deviceIdentifier is required and must not be blank".toString());
        }
        if (StringsKt.contains$default((CharSequence) this.deviceIdentifier, (char) 0, false, 2, (Object) null)) {
            throw new IllegalArgumentException("deviceIdentifier must not contain U+0000".toString());
        }
        if (!(this.publicKey.length() > 0)) {
            throw new IllegalArgumentException("publicKey is required and must not be empty".toString());
        }
        if (StringsKt.contains$default((CharSequence) this.publicKey, (char) 0, false, 2, (Object) null)) {
            throw new IllegalArgumentException("publicKey must not contain U+0000".toString());
        }
        if (!(this.sourceVersion.length() > 0)) {
            throw new IllegalArgumentException("sourceVersion is required and must not be empty".toString());
        }
        if (StringsKt.contains$default((CharSequence) this.sourceVersion, (char) 0, false, 2, (Object) null)) {
            throw new IllegalArgumentException("sourceVersion must not contain U+0000".toString());
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ Iap2WirelessCarPlayEndpoint(String str, String str2, int i, Iap2WirelessSecurity iap2WirelessSecurity, List list, int i2, String str3, String str4, String str5, byte[] bArr, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        byte[] bArr2;
        if ((i3 & 512) == 0) {
            bArr2 = bArr;
        } else {
            bArr2 = null;
        }
        this(str, str2, i, iap2WirelessSecurity, list, i2, str3, str4, str5, bArr2);
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

    public final Iap2WirelessSecurity getSecurity() {
        return this.security;
    }

    public final int getAirPlayPort() {
        return this.airPlayPort;
    }

    public final String getDeviceIdentifier() {
        return this.deviceIdentifier;
    }

    public final String getPublicKey() {
        return this.publicKey;
    }

    public final String getSourceVersion() {
        return this.sourceVersion;
    }

    public final List<String> getIpAddresses() {
        return this.ipAddresses;
    }

    public final byte[] getAccessPointBssid() {
        return this.accessPointBssid;
    }
}
