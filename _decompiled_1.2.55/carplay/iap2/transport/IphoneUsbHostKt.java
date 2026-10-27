package com.carplay.iap2.transport;

import android.hardware.usb.UsbEndpoint;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;

/* JADX INFO: compiled from: IphoneUsbHost.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000¨\u0006\u0004"}, d2 = {"describeUsbEndpoint", "", "endpoint", "Landroid/hardware/usb/UsbEndpoint;", "core-iap2"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class IphoneUsbHostKt {
    public static final String describeUsbEndpoint(UsbEndpoint endpoint) {
        Intrinsics.checkNotNullParameter(endpoint, "endpoint");
        String string = Integer.toString(endpoint.getAddress(), CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return "0x" + string + "(direction=" + endpoint.getDirection() + ",type=" + endpoint.getType() + ",maxPacket=" + endpoint.getMaxPacketSize() + ")";
    }
}
