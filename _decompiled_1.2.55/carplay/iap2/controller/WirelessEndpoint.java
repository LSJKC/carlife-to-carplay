package com.carplay.iap2.controller;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.text.Charsets;
import kotlin.text.Regex;

/* JADX INFO: compiled from: PhoneBootstrap.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b%\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0086\b\u0018\u00002\u00020\u0001Bs\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003¢\u0006\u0004\b\u000f\u0010\u0010J\u000e\u0010 \u001a\u00020\u00002\u0006\u0010!\u001a\u00020\u0000J\t\u0010\"\u001a\u00020\u0003HÆ\u0003J\t\u0010#\u001a\u00020\u0003HÆ\u0003J\t\u0010$\u001a\u00020\u0006HÆ\u0003J\t\u0010%\u001a\u00020\u0006HÆ\u0003J\u000f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00030\tHÆ\u0003J\u0010\u0010'\u001a\u0004\u0018\u00010\u0006HÆ\u0003¢\u0006\u0002\u0010\u001aJ\u000b\u0010(\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010+\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u0082\u0001\u0010,\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003HÆ\u0001¢\u0006\u0002\u0010-J\u0013\u0010.\u001a\u00020/2\b\u0010!\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u00100\u001a\u00020\u0006HÖ\u0001J\t\u00101\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0012R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0007\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0015R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00030\t¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0015\u0010\n\u001a\u0004\u0018\u00010\u0006¢\u0006\n\n\u0002\u0010\u001b\u001a\u0004\b\u0019\u0010\u001aR\u0013\u0010\u000b\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0012R\u0013\u0010\f\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0012R\u0013\u0010\r\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0012R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0012¨\u00062"}, d2 = {"Lcom/carplay/iap2/controller/WirelessEndpoint;", "", "ssid", "", "password", "security", "", "channel", "addresses", "", "port", "receiverId", "publicKey", "sourceVersion", "carPlaySdkVersion", "<init>", "(Ljava/lang/String;Ljava/lang/String;IILjava/util/List;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getSsid", "()Ljava/lang/String;", "getPassword", "getSecurity", "()I", "getChannel", "getAddresses", "()Ljava/util/List;", "getPort", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getReceiverId", "getPublicKey", "getSourceVersion", "getCarPlaySdkVersion", "merge", "other", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "copy", "(Ljava/lang/String;Ljava/lang/String;IILjava/util/List;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lcom/carplay/iap2/controller/WirelessEndpoint;", "equals", "", "hashCode", "toString", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class WirelessEndpoint {
    private final List<String> addresses;
    private final String carPlaySdkVersion;
    private final int channel;
    private final String password;
    private final Integer port;
    private final String publicKey;
    private final String receiverId;
    private final int security;
    private final String sourceVersion;
    private final String ssid;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ WirelessEndpoint copy$default(WirelessEndpoint wirelessEndpoint, String str, String str2, int i, int i2, List list, Integer num, String str3, String str4, String str5, String str6, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            str = wirelessEndpoint.ssid;
        }
        if ((i3 & 2) != 0) {
            str2 = wirelessEndpoint.password;
        }
        if ((i3 & 4) != 0) {
            i = wirelessEndpoint.security;
        }
        if ((i3 & 8) != 0) {
            i2 = wirelessEndpoint.channel;
        }
        if ((i3 & 16) != 0) {
            list = wirelessEndpoint.addresses;
        }
        if ((i3 & 32) != 0) {
            num = wirelessEndpoint.port;
        }
        if ((i3 & 64) != 0) {
            str3 = wirelessEndpoint.receiverId;
        }
        if ((i3 & 128) != 0) {
            str4 = wirelessEndpoint.publicKey;
        }
        if ((i3 & 256) != 0) {
            str5 = wirelessEndpoint.sourceVersion;
        }
        if ((i3 & 512) != 0) {
            str6 = wirelessEndpoint.carPlaySdkVersion;
        }
        String str7 = str5;
        String str8 = str6;
        String str9 = str3;
        String str10 = str4;
        List list2 = list;
        Integer num2 = num;
        return wirelessEndpoint.copy(str, str2, i, i2, list2, num2, str9, str10, str7, str8);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getSsid() {
        return this.ssid;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final String getCarPlaySdkVersion() {
        return this.carPlaySdkVersion;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getPassword() {
        return this.password;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int getSecurity() {
        return this.security;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int getChannel() {
        return this.channel;
    }

    public final List<String> component5() {
        return this.addresses;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final Integer getPort() {
        return this.port;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final String getReceiverId() {
        return this.receiverId;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final String getPublicKey() {
        return this.publicKey;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final String getSourceVersion() {
        return this.sourceVersion;
    }

    public final WirelessEndpoint copy(String ssid, String password, int security, int channel, List<String> addresses, Integer port, String receiverId, String publicKey, String sourceVersion, String carPlaySdkVersion) {
        Intrinsics.checkNotNullParameter(ssid, "ssid");
        Intrinsics.checkNotNullParameter(password, "password");
        Intrinsics.checkNotNullParameter(addresses, "addresses");
        return new WirelessEndpoint(ssid, password, security, channel, addresses, port, receiverId, publicKey, sourceVersion, carPlaySdkVersion);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof WirelessEndpoint)) {
            return false;
        }
        WirelessEndpoint wirelessEndpoint = (WirelessEndpoint) other;
        return Intrinsics.areEqual(this.ssid, wirelessEndpoint.ssid) && Intrinsics.areEqual(this.password, wirelessEndpoint.password) && this.security == wirelessEndpoint.security && this.channel == wirelessEndpoint.channel && Intrinsics.areEqual(this.addresses, wirelessEndpoint.addresses) && Intrinsics.areEqual(this.port, wirelessEndpoint.port) && Intrinsics.areEqual(this.receiverId, wirelessEndpoint.receiverId) && Intrinsics.areEqual(this.publicKey, wirelessEndpoint.publicKey) && Intrinsics.areEqual(this.sourceVersion, wirelessEndpoint.sourceVersion) && Intrinsics.areEqual(this.carPlaySdkVersion, wirelessEndpoint.carPlaySdkVersion);
    }

    public int hashCode() {
        return (((((((((((((((((this.ssid.hashCode() * 31) + this.password.hashCode()) * 31) + Integer.hashCode(this.security)) * 31) + Integer.hashCode(this.channel)) * 31) + this.addresses.hashCode()) * 31) + (this.port == null ? 0 : this.port.hashCode())) * 31) + (this.receiverId == null ? 0 : this.receiverId.hashCode())) * 31) + (this.publicKey == null ? 0 : this.publicKey.hashCode())) * 31) + (this.sourceVersion == null ? 0 : this.sourceVersion.hashCode())) * 31) + (this.carPlaySdkVersion != null ? this.carPlaySdkVersion.hashCode() : 0);
    }

    public String toString() {
        return "WirelessEndpoint(ssid=" + this.ssid + ", password=" + this.password + ", security=" + this.security + ", channel=" + this.channel + ", addresses=" + this.addresses + ", port=" + this.port + ", receiverId=" + this.receiverId + ", publicKey=" + this.publicKey + ", sourceVersion=" + this.sourceVersion + ", carPlaySdkVersion=" + this.carPlaySdkVersion + ")";
    }

    /* JADX WARN: Code duplicated, block: B:23:0x00b1  */
    public WirelessEndpoint(String ssid, String password, int security, int channel, List<String> addresses, Integer port, String receiverId, String publicKey, String sourceVersion, String carPlaySdkVersion) {
        boolean z;
        Intrinsics.checkNotNullParameter(ssid, "ssid");
        Intrinsics.checkNotNullParameter(password, "password");
        Intrinsics.checkNotNullParameter(addresses, "addresses");
        this.ssid = ssid;
        this.password = password;
        this.security = security;
        this.channel = channel;
        this.addresses = addresses;
        this.port = port;
        this.receiverId = receiverId;
        this.publicKey = publicKey;
        this.sourceVersion = sourceVersion;
        this.carPlaySdkVersion = carPlaySdkVersion;
        byte[] bytes = this.ssid.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        int length = bytes.length;
        boolean z2 = true;
        if (!(length >= 0 && length < 33)) {
            throw new IllegalArgumentException("SSID 长度无效".toString());
        }
        boolean zContains = SetsKt.setOf((Object[]) new Integer[]{0, 2, 3, 4}).contains(Integer.valueOf(this.security));
        int i = this.security;
        if (!zContains) {
            throw new IllegalArgumentException(("不支持的无线安全类型：" + i).toString());
        }
        if (i == 0) {
            z = true;
        } else {
            int length2 = this.password.length();
            if ((8 <= length2 && length2 < 64) || new Regex("[a-fA-F0-9]{64}").matches(this.password)) {
                z = true;
            } else {
                z = false;
            }
        }
        if (!z) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        int i2 = this.channel;
        if (!(i2 >= 0 && i2 < 256)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (this.port != null && !new IntRange(1, 65535).contains(this.port.intValue())) {
            z2 = false;
        }
        if (z2) {
        } else {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
    }

    public /* synthetic */ WirelessEndpoint(String str, String str2, int i, int i2, List list, Integer num, String str3, String str4, String str5, String str6, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, str2, i, i2, (i3 & 16) != 0 ? CollectionsKt.emptyList() : list, (i3 & 32) != 0 ? null : num, (i3 & 64) != 0 ? null : str3, (i3 & 128) != 0 ? null : str4, (i3 & 256) != 0 ? null : str5, (i3 & 512) != 0 ? null : str6);
    }

    public final int getChannel() {
        return this.channel;
    }

    public final String getPassword() {
        return this.password;
    }

    public final int getSecurity() {
        return this.security;
    }

    public final String getSsid() {
        return this.ssid;
    }

    public final List<String> getAddresses() {
        return this.addresses;
    }

    public final Integer getPort() {
        return this.port;
    }

    public final String getPublicKey() {
        return this.publicKey;
    }

    public final String getReceiverId() {
        return this.receiverId;
    }

    public final String getCarPlaySdkVersion() {
        return this.carPlaySdkVersion;
    }

    public final String getSourceVersion() {
        return this.sourceVersion;
    }

    public final WirelessEndpoint merge(WirelessEndpoint other) {
        Intrinsics.checkNotNullParameter(other, "other");
        if (!(Intrinsics.areEqual(this.ssid, other.ssid) && Intrinsics.areEqual(this.password, other.password) && this.security == other.security)) {
            throw new IllegalArgumentException("同一连接的无线凭据发生变化，请重新连接".toString());
        }
        List<String> list = other.addresses;
        if (list.isEmpty()) {
            list = this.addresses;
        }
        List<String> list2 = list;
        Integer num = other.port;
        if (num == null) {
            num = this.port;
        }
        Integer num2 = num;
        String str = other.receiverId;
        if (str == null) {
            str = this.receiverId;
        }
        String str2 = str;
        String str3 = other.publicKey;
        if (str3 == null) {
            str3 = this.publicKey;
        }
        String str4 = str3;
        String str5 = other.sourceVersion;
        if (str5 == null) {
            str5 = this.sourceVersion;
        }
        String str6 = str5;
        String str7 = other.carPlaySdkVersion;
        if (str7 == null) {
            str7 = this.carPlaySdkVersion;
        }
        return copy$default(other, null, null, 0, 0, list2, num2, str2, str4, str6, str7, 15, null);
    }
}
