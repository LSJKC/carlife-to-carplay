package com.carplay.iap2.message;

import com.carplay.iap2.body.Iap2BodyBuilder;
import com.carplay.iap2.body.Iap2BodyReader;
import com.carplay.iap2.catalog.Iap2Endpoint;
import com.carplay.iap2.catalog.Iap2Endpoints;
import com.carplay.iap2.wire.Iap2Frame;
import com.carplay.iap2.wire.Iap2ParameterList;
import com.carplay.iap2.wire.Iap2ProtocolException;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.text.CharsKt;
import org.bouncycastle.jcajce.util.AnnotatedPrivateKey;

/* JADX INFO: compiled from: Iap2Messages.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010 \n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\t\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0006\u0010\u0004\u001a\u00020\u0005J\u000e\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bJ\u0006\u0010\t\u001a\u00020\u0005J\u000e\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\fJ{\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\b2\u000e\b\u0002\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\b0\u00122\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00142\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u00162\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\n\b\u0002\u0010\u001b\u001a\u0004\u0018\u00010\u001c¢\u0006\u0002\u0010\u001dJ\u000e\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0005J\u000e\u0010\u001e\u001a\u00020\u001f2\u0006\u0010!\u001a\u00020\"J!\u0010#\u001a\u0004\u0018\u00010\u001c2\b\u0010$\u001a\u0004\u0018\u00010\f2\u0006\u0010%\u001a\u00020\bH\u0002¢\u0006\u0002\u0010&J.\u0010'\u001a\u00020\u00052\b\b\u0002\u0010(\u001a\u00020\u001c2\u0006\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010)\u001a\u00020\u001c2\n\b\u0002\u0010*\u001a\u0004\u0018\u00010\b¨\u0006+"}, d2 = {"Lcom/carplay/iap2/message/Iap2CarPlayMessages;", "", "<init>", "()V", "buildMinimalWirelessAvailability", "Lcom/carplay/iap2/wire/Iap2Frame;", "buildFlatWirelessAvailability", "bluetoothTransportIdentifier", "", "buildVivoReferenceAvailability", "buildWirelessComponentAvailability", "componentId", "", "startSession", "airPlayPort", "publicKey", "sourceVersion", "wiredIpv6Addresses", "", "wiredReserved", "", "wireless", "Lcom/carplay/iap2/message/Iap2WirelessSessionParameters;", "deviceIdentifier", "sdkVersion", "clusterAsset", "Lcom/carplay/iap2/message/Iap2ClusterAsset;", "mutualAuth", "", "(ILjava/lang/String;Ljava/lang/String;Ljava/util/List;Ljava/lang/Long;Lcom/carplay/iap2/message/Iap2WirelessSessionParameters;Ljava/lang/String;Ljava/lang/String;Lcom/carplay/iap2/message/Iap2ClusterAsset;Ljava/lang/Boolean;)Lcom/carplay/iap2/wire/Iap2Frame;", "availability", "Lcom/carplay/iap2/message/Iap2CarPlayAvailability;", "frame", "payload", "", "decodeAvailability", "value", AnnotatedPrivateKey.LABEL, "(Ljava/lang/Integer;Ljava/lang/String;)Ljava/lang/Boolean;", "buildAvailability", "wirelessSupported", "wiredSupported", "usbTransportIdentifier", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2CarPlayMessages {
    public static final Iap2CarPlayMessages INSTANCE = new Iap2CarPlayMessages();

    private Iap2CarPlayMessages() {
    }

    public final Iap2Frame buildMinimalWirelessAvailability() throws Iap2ProtocolException {
        Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
        Iap2BodyBuilder $this$buildMinimalWirelessAvailability_u24lambda_u241 = Iap2BodyBuilder.INSTANCE.opaque(17152);
        $this$buildMinimalWirelessAvailability_u24lambda_u241.group(1, new Function1() { // from class: com.carplay.iap2.message.Iap2CarPlayMessages$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Iap2CarPlayMessages.buildMinimalWirelessAvailability$lambda$1$lambda$0((Iap2BodyBuilder) obj);
            }
        });
        return $this$buildMinimalWirelessAvailability_u24lambda_u241.build();
    }

    static final Unit buildMinimalWirelessAvailability$lambda$1$lambda$0(Iap2BodyBuilder group) {
        Intrinsics.checkNotNullParameter(group, "$this$group");
        group.u8(0, 1);
        return Unit.INSTANCE;
    }

    public final Iap2Frame buildFlatWirelessAvailability(String bluetoothTransportIdentifier) {
        Intrinsics.checkNotNullParameter(bluetoothTransportIdentifier, "bluetoothTransportIdentifier");
        Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
        Iap2BodyBuilder $this$buildFlatWirelessAvailability_u24lambda_u242 = Iap2BodyBuilder.INSTANCE.opaque(17152);
        $this$buildFlatWirelessAvailability_u24lambda_u242.u8(0, 1);
        $this$buildFlatWirelessAvailability_u24lambda_u242.string(1, bluetoothTransportIdentifier);
        return $this$buildFlatWirelessAvailability_u24lambda_u242.build();
    }

    public final Iap2Frame buildVivoReferenceAvailability() {
        return buildWirelessComponentAvailability(2);
    }

    public final Iap2Frame buildWirelessComponentAvailability(final int componentId) throws Iap2ProtocolException {
        boolean z = false;
        if (componentId >= 0 && componentId < 65536) {
            z = true;
        }
        if (!z) {
            throw new IllegalArgumentException("Wireless CarPlay component id must fit in u16".toString());
        }
        Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
        Iap2BodyBuilder $this$buildWirelessComponentAvailability_u24lambda_u245 = Iap2BodyBuilder.INSTANCE.opaque(17152);
        $this$buildWirelessComponentAvailability_u24lambda_u245.group(1, new Function1() { // from class: com.carplay.iap2.message.Iap2CarPlayMessages$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Iap2CarPlayMessages.buildWirelessComponentAvailability$lambda$5$lambda$4(componentId, (Iap2BodyBuilder) obj);
            }
        });
        return $this$buildWirelessComponentAvailability_u24lambda_u245.build();
    }

    static final Unit buildWirelessComponentAvailability$lambda$5$lambda$4(int $componentId, Iap2BodyBuilder group) {
        Intrinsics.checkNotNullParameter(group, "$this$group");
        group.u8(0, 1);
        group.u16(1, $componentId);
        return Unit.INSTANCE;
    }

    public final Iap2Frame startSession(int airPlayPort, String publicKey, String sourceVersion, final List<String> wiredIpv6Addresses, final Long wiredReserved, final Iap2WirelessSessionParameters wireless, String deviceIdentifier, String sdkVersion, final Iap2ClusterAsset clusterAsset, Boolean mutualAuth) throws Iap2ProtocolException {
        Integer numValueOf;
        Intrinsics.checkNotNullParameter(publicKey, "publicKey");
        Intrinsics.checkNotNullParameter(sourceVersion, "sourceVersion");
        Intrinsics.checkNotNullParameter(wiredIpv6Addresses, "wiredIpv6Addresses");
        if (!(1 <= airPlayPort && airPlayPort < 65536)) {
            throw new IllegalArgumentException("airPlayPort must be in 1..65535".toString());
        }
        if (!(publicKey.length() > 0)) {
            throw new IllegalArgumentException("publicKey must not be empty".toString());
        }
        if (!(sourceVersion.length() > 0)) {
            throw new IllegalArgumentException("sourceVersion must not be empty".toString());
        }
        Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
        Iap2Endpoint endpoint$iv = Iap2Endpoints.INSTANCE.getCARPLAY_START_SESSION();
        Iap2BodyBuilder $this$startSession_u24lambda_u2417 = Iap2BodyBuilder.INSTANCE.of(endpoint$iv);
        if (!wiredIpv6Addresses.isEmpty() || wiredReserved != null) {
            $this$startSession_u24lambda_u2417.group(0, new Function1() { // from class: com.carplay.iap2.message.Iap2CarPlayMessages$$ExternalSyntheticLambda4
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2CarPlayMessages.startSession$lambda$17$lambda$10(wiredIpv6Addresses, wiredReserved, (Iap2BodyBuilder) obj);
                }
            });
        }
        if (wireless != null) {
            $this$startSession_u24lambda_u2417.group(1, new Function1() { // from class: com.carplay.iap2.message.Iap2CarPlayMessages$$ExternalSyntheticLambda5
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2CarPlayMessages.startSession$lambda$17$lambda$13$lambda$12(wireless, (Iap2BodyBuilder) obj);
                }
            });
        }
        $this$startSession_u24lambda_u2417.u32(2, airPlayPort);
        $this$startSession_u24lambda_u2417.optionalString(3, deviceIdentifier);
        $this$startSession_u24lambda_u2417.string(4, publicKey);
        $this$startSession_u24lambda_u2417.string(5, sourceVersion);
        $this$startSession_u24lambda_u2417.optionalString(6, sdkVersion);
        if (clusterAsset != null) {
            $this$startSession_u24lambda_u2417.group(7, new Function1() { // from class: com.carplay.iap2.message.Iap2CarPlayMessages$$ExternalSyntheticLambda6
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2CarPlayMessages.startSession$lambda$17$lambda$15$lambda$14(clusterAsset, (Iap2BodyBuilder) obj);
                }
            });
        }
        if (mutualAuth != null) {
            boolean it = mutualAuth.booleanValue();
            numValueOf = Integer.valueOf(it ? 1 : 0);
        } else {
            numValueOf = null;
        }
        $this$startSession_u24lambda_u2417.optionalU8(8, numValueOf);
        return $this$startSession_u24lambda_u2417.build();
    }

    static final Unit startSession$lambda$17$lambda$10(List $wiredIpv6Addresses, Long $wiredReserved, Iap2BodyBuilder group) {
        Intrinsics.checkNotNullParameter(group, "$this$group");
        List $this$forEach$iv = $wiredIpv6Addresses;
        for (Object element$iv : $this$forEach$iv) {
            String it = (String) element$iv;
            group.string(0, it);
        }
        group.optionalU32(1, $wiredReserved);
        return Unit.INSTANCE;
    }

    static final Unit startSession$lambda$17$lambda$13$lambda$12(Iap2WirelessSessionParameters $it, Iap2BodyBuilder group) {
        Intrinsics.checkNotNullParameter(group, "$this$group");
        group.string(0, $it.getSsid());
        group.string(1, $it.getPassphrase());
        group.u8(2, $it.getChannel());
        Iterable $this$forEach$iv = $it.getIpAddresses();
        for (Object element$iv : $this$forEach$iv) {
            String address = (String) element$iv;
            group.string(3, address);
        }
        group.u8(4, $it.getSecurityType());
        return Unit.INSTANCE;
    }

    static final Unit startSession$lambda$17$lambda$15$lambda$14(Iap2ClusterAsset $it, Iap2BodyBuilder group) {
        Intrinsics.checkNotNullParameter(group, "$this$group");
        group.string(0, $it.getAssetId());
        group.u32(1, $it.getAssetVersion());
        return Unit.INSTANCE;
    }

    public final Iap2CarPlayAvailability availability(Iap2Frame frame) {
        Intrinsics.checkNotNullParameter(frame, "frame");
        if (!(frame.getMessageId() == Iap2Endpoints.INSTANCE.getCARPLAY_AVAILABILITY().getId())) {
            String string = Integer.toString(frame.getMessageId(), CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            throw new IllegalArgumentException(("Expected CarPlayAvailability, got 0x" + string).toString());
        }
        return availability(frame.getPayload());
    }

    public final Iap2CarPlayAvailability availability(byte[] payload) {
        Intrinsics.checkNotNullParameter(payload, "payload");
        Iap2BodyReader body = Iap2BodyReader.INSTANCE.of(Iap2ParameterList.INSTANCE.parse(payload));
        Iap2BodyReader it = body.optionalGroup(0);
        Iap2AvailabilityState wired = it != null ? new Iap2AvailabilityState(INSTANCE.decodeAvailability(it.optionalU8(0), "wired"), it.optionalString(1)) : null;
        Iap2BodyReader it2 = body.optionalGroup(1);
        Iap2AvailabilityState wireless = it2 != null ? new Iap2AvailabilityState(INSTANCE.decodeAvailability(it2.optionalU8(0), "wireless"), it2.optionalString(1)) : null;
        Iap2BodyReader it3 = body.optionalGroup(2);
        Iap2AvailabilityState themeAssets = it3 != null ? new Iap2AvailabilityState(INSTANCE.decodeAvailability(it3.optionalU8(0), "themeAssets"), null) : null;
        return new Iap2CarPlayAvailability(wired, wireless, themeAssets);
    }

    private final Boolean decodeAvailability(Integer value, String label) throws Iap2ProtocolException {
        if (value == null) {
            return null;
        }
        if (new IntRange(0, 1).contains(value.intValue())) {
            return Boolean.valueOf(value.intValue() == 1);
        }
        throw new Iap2ProtocolException(label + " availability must be 0 or 1, got " + value, null, 2, null);
    }

    public static /* synthetic */ Iap2Frame buildAvailability$default(Iap2CarPlayMessages iap2CarPlayMessages, boolean z, String str, boolean z2, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            z = true;
        }
        if ((i & 4) != 0) {
            z2 = false;
        }
        if ((i & 8) != 0) {
            str2 = null;
        }
        return iap2CarPlayMessages.buildAvailability(z, str, z2, str2);
    }

    public final Iap2Frame buildAvailability(boolean wirelessSupported, final String bluetoothTransportIdentifier, boolean wiredSupported, final String usbTransportIdentifier) throws Iap2ProtocolException {
        Intrinsics.checkNotNullParameter(bluetoothTransportIdentifier, "bluetoothTransportIdentifier");
        Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
        Iap2Endpoint endpoint$iv = Iap2Endpoints.INSTANCE.getCARPLAY_AVAILABILITY();
        Iap2BodyBuilder $this$buildAvailability_u24lambda_u2424 = Iap2BodyBuilder.INSTANCE.of(endpoint$iv);
        if (wiredSupported && usbTransportIdentifier != null) {
            $this$buildAvailability_u24lambda_u2424.group(0, new Function1() { // from class: com.carplay.iap2.message.Iap2CarPlayMessages$$ExternalSyntheticLambda2
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2CarPlayMessages.buildAvailability$lambda$24$lambda$22(usbTransportIdentifier, (Iap2BodyBuilder) obj);
                }
            });
        }
        if (wirelessSupported) {
            $this$buildAvailability_u24lambda_u2424.group(1, new Function1() { // from class: com.carplay.iap2.message.Iap2CarPlayMessages$$ExternalSyntheticLambda3
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2CarPlayMessages.buildAvailability$lambda$24$lambda$23(bluetoothTransportIdentifier, (Iap2BodyBuilder) obj);
                }
            });
        }
        return $this$buildAvailability_u24lambda_u2424.build();
    }

    static final Unit buildAvailability$lambda$24$lambda$22(String $usbTransportIdentifier, Iap2BodyBuilder group) {
        Intrinsics.checkNotNullParameter(group, "$this$group");
        group.u8(0, 1);
        group.string(1, $usbTransportIdentifier);
        return Unit.INSTANCE;
    }

    static final Unit buildAvailability$lambda$24$lambda$23(String $bluetoothTransportIdentifier, Iap2BodyBuilder group) {
        Intrinsics.checkNotNullParameter(group, "$this$group");
        group.u8(0, 1);
        group.string(1, $bluetoothTransportIdentifier);
        return Unit.INSTANCE;
    }
}
