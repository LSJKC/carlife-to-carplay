package com.carplay.iap2.transport;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Iap2IdentificationClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u0012\n\u0002\b\u0004\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\r\u0010\f\u001a\u00020\u000bH\u0000¢\u0006\u0002\b\rR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\bR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lcom/carplay/iap2/transport/Iap2WirelessIdentification;", "", "bluetoothMac", "", "ssid", "<init>", "(Ljava/lang/String;Ljava/lang/String;)V", "getBluetoothMac", "()Ljava/lang/String;", "getSsid", "macBytes", "", "bluetoothMacBytes", "bluetoothMacBytes$core_iap2", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2WirelessIdentification {
    private static final Companion Companion = new Companion(null);
    private static final Regex MAC_ADDRESS = new Regex("^[0-9A-Fa-f]{2}(:[0-9A-Fa-f]{2}){5}$");
    private final String bluetoothMac;
    private final byte[] macBytes;
    private final String ssid;

    public Iap2WirelessIdentification(String bluetoothMac, String ssid) {
        Intrinsics.checkNotNullParameter(bluetoothMac, "bluetoothMac");
        Intrinsics.checkNotNullParameter(ssid, "ssid");
        this.bluetoothMac = bluetoothMac;
        this.ssid = ssid;
        if (!MAC_ADDRESS.matches(this.bluetoothMac)) {
            throw new IllegalArgumentException("bluetoothMac must be six colon-separated hexadecimal bytes".toString());
        }
        if (StringsKt.isBlank(this.ssid)) {
            throw new IllegalArgumentException("Wireless SSID must not be blank".toString());
        }
        if (StringsKt.contains$default((CharSequence) this.ssid, (char) 0, false, 2, (Object) null)) {
            throw new IllegalArgumentException("Wireless SSID must not contain U+0000".toString());
        }
        byte[] bArr = new byte[6];
        for (int i = 0; i < 6; i++) {
            int i2 = i * 3;
            String strSubstring = this.bluetoothMac.substring(i2, i2 + 2);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
            bArr[i] = (byte) Integer.parseInt(strSubstring, CharsKt.checkRadix(16));
        }
        this.macBytes = bArr;
    }

    public final String getBluetoothMac() {
        return this.bluetoothMac;
    }

    public final String getSsid() {
        return this.ssid;
    }

    public final byte[] bluetoothMacBytes$core_iap2() {
        byte[] bArr = this.macBytes;
        byte[] bArrCopyOf = Arrays.copyOf(bArr, bArr.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        return bArrCopyOf;
    }

    /* JADX INFO: compiled from: Iap2IdentificationClient.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lcom/carplay/iap2/transport/Iap2WirelessIdentification$Companion;", "", "<init>", "()V", "MAC_ADDRESS", "Lkotlin/text/Regex;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
