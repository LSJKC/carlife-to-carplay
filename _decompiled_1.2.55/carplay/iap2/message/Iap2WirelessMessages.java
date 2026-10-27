package com.carplay.iap2.message;

import com.carplay.iap2.body.Iap2BodyBuilder;
import com.carplay.iap2.body.Iap2BodyReader;
import com.carplay.iap2.catalog.Iap2Endpoint;
import com.carplay.iap2.catalog.Iap2Endpoints;
import com.carplay.iap2.wire.Iap2Frame;
import com.carplay.iap2.wire.Iap2ProtocolException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;

/* JADX INFO: compiled from: Iap2Messages.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J2\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\rJ\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0005J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0010\u001a\u00020\u0005J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0010\u001a\u00020\u0005¨\u0006\u0015"}, d2 = {"Lcom/carplay/iap2/message/Iap2WirelessMessages;", "", "<init>", "()V", "accessoryWiFiConfiguration", "Lcom/carplay/iap2/wire/Iap2Frame;", "ssid", "", "passphrase", "channel", "", "securityType", "bssid", "", "wifiInformation", "Lcom/carplay/iap2/message/Iap2WifiInformation;", "frame", "wirelessCarPlayAvailability", "", "deviceTransportIdentifier", "Lcom/carplay/iap2/message/Iap2DeviceTransportIdentifier;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2WirelessMessages {
    public static final Iap2WirelessMessages INSTANCE = new Iap2WirelessMessages();

    private Iap2WirelessMessages() {
    }

    public static /* synthetic */ Iap2Frame accessoryWiFiConfiguration$default(Iap2WirelessMessages iap2WirelessMessages, String str, String str2, int i, int i2, byte[] bArr, int i3, Object obj) {
        byte[] bArr2;
        if ((i3 & 16) == 0) {
            bArr2 = bArr;
        } else {
            bArr2 = null;
        }
        return iap2WirelessMessages.accessoryWiFiConfiguration(str, str2, i, i2, bArr2);
    }

    public final Iap2Frame accessoryWiFiConfiguration(String ssid, String passphrase, int channel, int securityType, byte[] bssid) {
        Intrinsics.checkNotNullParameter(ssid, "ssid");
        Intrinsics.checkNotNullParameter(passphrase, "passphrase");
        Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
        Iap2Endpoint endpoint$iv = Iap2Endpoints.INSTANCE.getACCESSORY_WIFI_CONFIGURATION();
        Iap2BodyBuilder $this$accessoryWiFiConfiguration_u24lambda_u240 = Iap2BodyBuilder.INSTANCE.of(endpoint$iv);
        $this$accessoryWiFiConfiguration_u24lambda_u240.optionalBytes(0, bssid);
        $this$accessoryWiFiConfiguration_u24lambda_u240.string(1, ssid);
        $this$accessoryWiFiConfiguration_u24lambda_u240.string(2, passphrase);
        $this$accessoryWiFiConfiguration_u24lambda_u240.u8(3, securityType);
        $this$accessoryWiFiConfiguration_u24lambda_u240.u8(4, channel);
        return $this$accessoryWiFiConfiguration_u24lambda_u240.build();
    }

    public final Iap2WifiInformation wifiInformation(Iap2Frame frame) {
        Intrinsics.checkNotNullParameter(frame, "frame");
        if (!(frame.getMessageId() == Iap2Endpoints.INSTANCE.getWIFI_INFORMATION().getId())) {
            String string = Integer.toString(frame.getMessageId(), CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            throw new IllegalArgumentException(("Expected WiFiInformation, got 0x" + string).toString());
        }
        Iap2BodyReader body = Iap2Messages.INSTANCE.reader(frame);
        int status = body.u8(0);
        return status != 0 ? new Iap2WifiInformation(status, null, null, null, 14, null) : new Iap2WifiInformation(status, body.optionalU8(1), body.optionalString(2), body.optionalString(3));
    }

    public final boolean wirelessCarPlayAvailability(Iap2Frame frame) throws Iap2ProtocolException {
        Intrinsics.checkNotNullParameter(frame, "frame");
        if (!(frame.getMessageId() == Iap2Endpoints.INSTANCE.getWIRELESS_CARPLAY_UPDATE().getId())) {
            String string = Integer.toString(frame.getMessageId(), CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            throw new IllegalArgumentException(("Expected WirelessCarPlayUpdate, got 0x" + string).toString());
        }
        int value = Iap2Messages.INSTANCE.reader(frame).u8(0);
        switch (value) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                throw new Iap2ProtocolException("WirelessCarPlayUpdate availability must be 0 or 1, got " + value, null, 2, null);
        }
    }

    public final Iap2DeviceTransportIdentifier deviceTransportIdentifier(Iap2Frame frame) {
        Intrinsics.checkNotNullParameter(frame, "frame");
        if (!(frame.getMessageId() == Iap2Endpoints.INSTANCE.getDEVICE_TRANSPORT_IDENTIFIER().getId())) {
            String string = Integer.toString(frame.getMessageId(), CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            throw new IllegalArgumentException(("Expected DeviceTransportIdentifierNotification, got 0x" + string).toString());
        }
        Iap2BodyReader body = Iap2Messages.INSTANCE.reader(frame);
        return new Iap2DeviceTransportIdentifier(body.optionalString(0), body.optionalString(1));
    }
}
