package com.carplay.iap2.transport;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Iap2WiredControlClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\u0018\u0000 \u00142\u00020\u0001:\u0001\u0014B9\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0004\u0012\u0006\u0010\b\u001a\u00020\u0004\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0004¢\u0006\u0004\b\n\u0010\u000bR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0007\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\b\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0013\u0010\t\u001a\u0004\u0018\u00010\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000fR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013¨\u0006\u0015"}, d2 = {"Lcom/carplay/iap2/transport/Iap2WiredCarPlayEndpoint;", "", "ipv6Addresses", "", "", "airPlayPort", "", "publicKey", "sourceVersion", "deviceIdentifier", "<init>", "(Ljava/util/List;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getAirPlayPort", "()I", "getPublicKey", "()Ljava/lang/String;", "getSourceVersion", "getDeviceIdentifier", "getIpv6Addresses", "()Ljava/util/List;", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2WiredCarPlayEndpoint {
    private static final Companion Companion = new Companion(null);
    private final int airPlayPort;
    private final String deviceIdentifier;
    private final List<String> ipv6Addresses;
    private final String publicKey;
    private final String sourceVersion;

    public Iap2WiredCarPlayEndpoint(List<String> ipv6Addresses, int airPlayPort, String publicKey, String sourceVersion, String deviceIdentifier) {
        boolean z;
        Intrinsics.checkNotNullParameter(ipv6Addresses, "ipv6Addresses");
        Intrinsics.checkNotNullParameter(publicKey, "publicKey");
        Intrinsics.checkNotNullParameter(sourceVersion, "sourceVersion");
        this.airPlayPort = airPlayPort;
        this.publicKey = publicKey;
        this.sourceVersion = sourceVersion;
        this.deviceIdentifier = deviceIdentifier;
        this.ipv6Addresses = CollectionsKt.toList(ipv6Addresses);
        if (ipv6Addresses.isEmpty()) {
            throw new IllegalArgumentException("At least one wired IPv6 address is required".toString());
        }
        List<String> $this$all$iv = ipv6Addresses;
        Companion companion = Companion;
        if (!($this$all$iv instanceof Collection) || !$this$all$iv.isEmpty()) {
            Iterator it = $this$all$iv.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = true;
                    break;
                }
                Object element$iv = it.next();
                String p0 = (String) element$iv;
                if (!companion.isIpv6Literal(p0)) {
                    z = false;
                    break;
                }
            }
        } else {
            z = true;
        }
        if (!z) {
            throw new IllegalArgumentException("Every wired address must be an IPv6 text literal".toString());
        }
        int i = this.airPlayPort;
        if (!(1 <= i && i < 65536)) {
            throw new IllegalArgumentException("airPlayPort must be in 1..65535".toString());
        }
        if (!(this.publicKey.length() > 0)) {
            throw new IllegalArgumentException("publicKey is required and must not be empty".toString());
        }
        if (!(this.sourceVersion.length() > 0)) {
            throw new IllegalArgumentException("sourceVersion is required and must not be empty".toString());
        }
        if (StringsKt.contains$default((CharSequence) this.publicKey, (char) 0, false, 2, (Object) null)) {
            throw new IllegalArgumentException("publicKey must not contain U+0000".toString());
        }
        if (StringsKt.contains$default((CharSequence) this.sourceVersion, (char) 0, false, 2, (Object) null)) {
            throw new IllegalArgumentException("sourceVersion must not contain U+0000".toString());
        }
        String it2 = this.deviceIdentifier;
        if (it2 != null) {
            if (!(it2.length() > 0)) {
                throw new IllegalArgumentException("deviceIdentifier is optional, but must not be empty when provided".toString());
            }
            if (StringsKt.contains$default((CharSequence) it2, (char) 0, false, 2, (Object) null)) {
                throw new IllegalArgumentException("deviceIdentifier must not contain U+0000".toString());
            }
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ Iap2WiredCarPlayEndpoint(List list, int i, String str, String str2, String str3, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        String str4;
        if ((i2 & 16) == 0) {
            str4 = str3;
        } else {
            str4 = null;
        }
        this(list, i, str, str2, str4);
    }

    public final int getAirPlayPort() {
        return this.airPlayPort;
    }

    public final String getPublicKey() {
        return this.publicKey;
    }

    public final String getSourceVersion() {
        return this.sourceVersion;
    }

    public final String getDeviceIdentifier() {
        return this.deviceIdentifier;
    }

    public final List<String> getIpv6Addresses() {
        return this.ipv6Addresses;
    }

    /* JADX INFO: compiled from: Iap2WiredControlClient.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007¨\u0006\b"}, d2 = {"Lcom/carplay/iap2/transport/Iap2WiredCarPlayEndpoint$Companion;", "", "<init>", "()V", "isIpv6Literal", "", "value", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean isIpv6Literal(String value) {
            Intrinsics.checkNotNullParameter(value, "value");
            if (StringsKt.contains$default((CharSequence) value, '%', false, 2, (Object) null) || StringsKt.contains$default((CharSequence) value, (char) 0, false, 2, (Object) null) || !StringsKt.contains$default((CharSequence) value, ':', false, 2, (Object) null)) {
                return false;
            }
            try {
                return InetAddress.getByName(value) instanceof Inet6Address;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
