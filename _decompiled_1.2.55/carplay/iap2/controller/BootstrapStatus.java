package com.carplay.iap2.controller;

import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: PhoneBootstrap.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001BM\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003¢\u0006\u0004\b\u000b\u0010\fJ\u0006\u0010\u001a\u001a\u00020\u001bJ\t\u0010\u001c\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001d\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001f\u001a\u00020\u0003HÆ\u0003J\t\u0010 \u001a\u00020\u0003HÆ\u0003J\t\u0010!\u001a\u00020\u0003HÆ\u0003J\t\u0010\"\u001a\u00020\u0003HÆ\u0003J\t\u0010#\u001a\u00020\u0003HÆ\u0003JY\u0010$\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u0003HÆ\u0001J\u0013\u0010%\u001a\u00020\u00032\b\u0010&\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010'\u001a\u00020(HÖ\u0001J\t\u0010)\u001a\u00020\u001bHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000eR\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000eR\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000eR\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000eR\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000eR\u0011\u0010\u0016\u001a\u00020\u00178F¢\u0006\u0006\u001a\u0004\b\u0018\u0010\u0019¨\u0006*"}, d2 = {"Lcom/carplay/iap2/controller/BootstrapStatus;", "", "identified", "", "authenticated", "certificateReceived", "challengePending", "endpointReceived", "networkRequestQueued", "modernWirelessSupported", "controlReady", "<init>", "(ZZZZZZZZ)V", "getIdentified", "()Z", "getAuthenticated", "getCertificateReceived", "getChallengePending", "getEndpointReceived", "getNetworkRequestQueued", "getModernWirelessSupported", "getControlReady", "phase", "Lcom/carplay/iap2/controller/BootstrapPhase;", "getPhase", "()Lcom/carplay/iap2/controller/BootstrapPhase;", "timeoutDetail", "", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "hashCode", "", "toString", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class BootstrapStatus {
    private final boolean authenticated;
    private final boolean certificateReceived;
    private final boolean challengePending;
    private final boolean controlReady;
    private final boolean endpointReceived;
    private final boolean identified;
    private final boolean modernWirelessSupported;
    private final boolean networkRequestQueued;

    /* JADX INFO: compiled from: PhoneBootstrap.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[BootstrapPhase.values().length];
            try {
                iArr[BootstrapPhase.IDENTIFY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[BootstrapPhase.CERTIFICATE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[BootstrapPhase.CHALLENGE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[BootstrapPhase.WIRELESS_CONFIGURATION.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[BootstrapPhase.READY.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[BootstrapPhase.CONTROL_READY.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public static /* synthetic */ BootstrapStatus copy$default(BootstrapStatus bootstrapStatus, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, int i, Object obj) {
        if ((i & 1) != 0) {
            z = bootstrapStatus.identified;
        }
        if ((i & 2) != 0) {
            z2 = bootstrapStatus.authenticated;
        }
        if ((i & 4) != 0) {
            z3 = bootstrapStatus.certificateReceived;
        }
        if ((i & 8) != 0) {
            z4 = bootstrapStatus.challengePending;
        }
        if ((i & 16) != 0) {
            z5 = bootstrapStatus.endpointReceived;
        }
        if ((i & 32) != 0) {
            z6 = bootstrapStatus.networkRequestQueued;
        }
        if ((i & 64) != 0) {
            z7 = bootstrapStatus.modernWirelessSupported;
        }
        if ((i & 128) != 0) {
            z8 = bootstrapStatus.controlReady;
        }
        boolean z9 = z7;
        boolean z10 = z8;
        boolean z11 = z5;
        boolean z12 = z6;
        return bootstrapStatus.copy(z, z2, z3, z4, z11, z12, z9, z10);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final boolean getIdentified() {
        return this.identified;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final boolean getAuthenticated() {
        return this.authenticated;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final boolean getCertificateReceived() {
        return this.certificateReceived;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final boolean getChallengePending() {
        return this.challengePending;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final boolean getEndpointReceived() {
        return this.endpointReceived;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final boolean getNetworkRequestQueued() {
        return this.networkRequestQueued;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final boolean getModernWirelessSupported() {
        return this.modernWirelessSupported;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final boolean getControlReady() {
        return this.controlReady;
    }

    public final BootstrapStatus copy(boolean identified, boolean authenticated, boolean certificateReceived, boolean challengePending, boolean endpointReceived, boolean networkRequestQueued, boolean modernWirelessSupported, boolean controlReady) {
        return new BootstrapStatus(identified, authenticated, certificateReceived, challengePending, endpointReceived, networkRequestQueued, modernWirelessSupported, controlReady);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BootstrapStatus)) {
            return false;
        }
        BootstrapStatus bootstrapStatus = (BootstrapStatus) other;
        return this.identified == bootstrapStatus.identified && this.authenticated == bootstrapStatus.authenticated && this.certificateReceived == bootstrapStatus.certificateReceived && this.challengePending == bootstrapStatus.challengePending && this.endpointReceived == bootstrapStatus.endpointReceived && this.networkRequestQueued == bootstrapStatus.networkRequestQueued && this.modernWirelessSupported == bootstrapStatus.modernWirelessSupported && this.controlReady == bootstrapStatus.controlReady;
    }

    public int hashCode() {
        return (((((((((((((Boolean.hashCode(this.identified) * 31) + Boolean.hashCode(this.authenticated)) * 31) + Boolean.hashCode(this.certificateReceived)) * 31) + Boolean.hashCode(this.challengePending)) * 31) + Boolean.hashCode(this.endpointReceived)) * 31) + Boolean.hashCode(this.networkRequestQueued)) * 31) + Boolean.hashCode(this.modernWirelessSupported)) * 31) + Boolean.hashCode(this.controlReady);
    }

    public String toString() {
        return "BootstrapStatus(identified=" + this.identified + ", authenticated=" + this.authenticated + ", certificateReceived=" + this.certificateReceived + ", challengePending=" + this.challengePending + ", endpointReceived=" + this.endpointReceived + ", networkRequestQueued=" + this.networkRequestQueued + ", modernWirelessSupported=" + this.modernWirelessSupported + ", controlReady=" + this.controlReady + ")";
    }

    public BootstrapStatus(boolean identified, boolean authenticated, boolean certificateReceived, boolean challengePending, boolean endpointReceived, boolean networkRequestQueued, boolean modernWirelessSupported, boolean controlReady) {
        this.identified = identified;
        this.authenticated = authenticated;
        this.certificateReceived = certificateReceived;
        this.challengePending = challengePending;
        this.endpointReceived = endpointReceived;
        this.networkRequestQueued = networkRequestQueued;
        this.modernWirelessSupported = modernWirelessSupported;
        this.controlReady = controlReady;
    }

    public /* synthetic */ BootstrapStatus(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(z, z2, z3, z4, z5, (i & 32) != 0 ? false : z6, (i & 64) != 0 ? false : z7, (i & 128) != 0 ? false : z8);
    }

    public final boolean getAuthenticated() {
        return this.authenticated;
    }

    public final boolean getCertificateReceived() {
        return this.certificateReceived;
    }

    public final boolean getIdentified() {
        return this.identified;
    }

    public final boolean getChallengePending() {
        return this.challengePending;
    }

    public final boolean getEndpointReceived() {
        return this.endpointReceived;
    }

    public final boolean getNetworkRequestQueued() {
        return this.networkRequestQueued;
    }

    public final boolean getControlReady() {
        return this.controlReady;
    }

    public final boolean getModernWirelessSupported() {
        return this.modernWirelessSupported;
    }

    public final BootstrapPhase getPhase() {
        if (this.endpointReceived) {
            return BootstrapPhase.READY;
        }
        if (this.controlReady) {
            return BootstrapPhase.CONTROL_READY;
        }
        if (this.authenticated) {
            return BootstrapPhase.WIRELESS_CONFIGURATION;
        }
        if (this.challengePending) {
            return BootstrapPhase.CHALLENGE;
        }
        return this.identified ? BootstrapPhase.CERTIFICATE : BootstrapPhase.IDENTIFY;
    }

    public final String timeoutDetail() {
        switch (WhenMappings.$EnumSwitchMapping$0[getPhase().ordinal()]) {
            case 1:
                return "等待车机识别信息超时：未收到 0x1d01";
            case 2:
                return "车机识别已完成，等待认证证书超时：未收到 0xaa01";
            case 3:
                return "认证挑战已发送，等待车机签名响应超时：未收到 0xaa03";
            case 4:
                return "车机签名验证通过，等待无线配置超时";
            case 5:
                return "无线配置已收到，但连接请求未完成";
            case 6:
                return "Wi-Fi iAP2 已完成独立识别和认证";
            default:
                throw new NoWhenBranchMatchedException();
        }
    }
}
