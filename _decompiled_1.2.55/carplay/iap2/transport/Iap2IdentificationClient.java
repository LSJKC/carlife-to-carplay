package com.carplay.iap2.transport;

import com.carplay.iap2.body.Iap2BodyBuilder;
import com.carplay.iap2.body.Iap2BodyReader;
import com.carplay.iap2.catalog.Iap2Endpoint;
import com.carplay.iap2.catalog.Iap2Endpoints;
import com.carplay.iap2.message.Iap2Messages;
import com.carplay.iap2.session.Iap2Session;
import com.carplay.iap2.wire.Iap2Frame;
import com.carplay.iap2.wire.Iap2Parameter;
import com.carplay.iap2.wire.Iap2ProtocolException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2IdentificationClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u0018\u0000 \f2\u00020\u0001:\u0001\fB\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0018\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lcom/carplay/iap2/transport/Iap2IdentificationClient;", "", "session", "Lcom/carplay/iap2/session/Iap2Session;", "<init>", "(Lcom/carplay/iap2/session/Iap2Session;)V", "identify", "", "config", "Lcom/carplay/iap2/transport/Iap2IdentificationConfig;", "timeoutMillis", "", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2IdentificationClient {
    private static final int ACCESSORY_WIFI_CONFIGURATION_INFORMATION = 22275;
    private static final long DEFAULT_TIMEOUT_MILLIS = 10000;
    public static final int IDENTIFICATION_ACCEPTED = 7426;
    public static final int IDENTIFICATION_INFORMATION = 7425;
    public static final int IDENTIFICATION_REJECTED = 7427;
    private static final int LOCATION_INFORMATION = 65531;
    private static final long MAXIMUM_TIMEOUT_MILLIS = 300000;
    private static final long NANOS_PER_MILLISECOND = 1000000;
    public static final int START_IDENTIFICATION = 7424;
    private static final int START_LOCATION_INFORMATION = 65530;
    private static final int STOP_LOCATION_INFORMATION = 65532;
    private final Iap2Session session;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final int POWER_SOURCE_UPDATE = 44547;
    private static final int CARPLAY_START_SESSION = 17153;
    private static final int[] MESSAGES_SENT_BY_ACCESSORY = {43521, 43523, 20480, 20482, 20992, 20995, 44544, 44546, 16727, 16729, 16724, 16726, POWER_SOURCE_UPDATE, CARPLAY_START_SESSION};
    private static final int CARPLAY_AVAILABILITY = 17152;
    private static final int[] MESSAGES_RECEIVED_FROM_PHONE = {43520, 43522, 43524, 43525, 59904, 59905, 20481, 20993, 20994, 44545, 16728, 16725, CARPLAY_AVAILABILITY};
    private static final int[] WIRELESS_PHONE_MESSAGES = {19981, 19982, 22274};

    public Iap2IdentificationClient(Iap2Session session) {
        Intrinsics.checkNotNullParameter(session, "session");
        this.session = session;
    }

    public static /* synthetic */ void identify$default(Iap2IdentificationClient iap2IdentificationClient, Iap2IdentificationConfig iap2IdentificationConfig, long j, int i, Object obj) throws IphoneUsbException, Iap2IdentificationException {
        if ((i & 2) != 0) {
            j = 10000;
        }
        iap2IdentificationClient.identify(iap2IdentificationConfig, j);
    }

    public final void identify(Iap2IdentificationConfig config, long timeoutMillis) throws IphoneUsbException, Iap2IdentificationException {
        Intrinsics.checkNotNullParameter(config, "config");
        boolean z = false;
        if (1 <= timeoutMillis && timeoutMillis < 300001) {
            z = true;
        }
        if (!z) {
            throw new IllegalArgumentException("timeoutMillis must be in 1..300000".toString());
        }
        long deadlineNanos = System.nanoTime() + (1000000 * timeoutMillis);
        if (!this.session.awaitReady(INSTANCE.remainingMillis(deadlineNanos))) {
            throw new IphoneUsbException.TimedOut("Timed out waiting for iAP2 control session readiness", null, 2, null);
        }
        while (true) {
            Iap2Frame frame = this.session.recv(INSTANCE.remainingMillis(deadlineNanos));
            if (frame == null) {
                throw new IphoneUsbException.TimedOut("Timed out waiting for iAP2 identification", null, 2, null);
            }
            switch (frame.getMessageId()) {
                case START_IDENTIFICATION /* 7424 */:
                    this.session.send(INSTANCE.identificationInformation(config), INSTANCE.remainingMillis(deadlineNanos));
                    break;
                case IDENTIFICATION_INFORMATION /* 7425 */:
                default:
                    throw new Iap2IdentificationException.UnexpectedMessage(frame.getMessageId());
                case IDENTIFICATION_ACCEPTED /* 7426 */:
                    return;
                case IDENTIFICATION_REJECTED /* 7427 */:
                    Iterable $this$mapTo$iv = Iap2BodyReader.INSTANCE.of(frame).list();
                    Collection destination$iv = new LinkedHashSet();
                    for (Object item$iv : $this$mapTo$iv) {
                        Iap2Parameter it = (Iap2Parameter) item$iv;
                        destination$iv.add(Integer.valueOf(it.getId()));
                    }
                    LinkedHashSet rejected = (LinkedHashSet) destination$iv;
                    throw new Iap2IdentificationException.Rejected(rejected);
            }
        }
    }

    /* JADX INFO: compiled from: Iap2IdentificationClient.kt */
    @Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0015\n\u0002\b\n\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u0010\u0010\u0011\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\nH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\nX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001e"}, d2 = {"Lcom/carplay/iap2/transport/Iap2IdentificationClient$Companion;", "", "<init>", "()V", "START_IDENTIFICATION", "", "IDENTIFICATION_INFORMATION", "IDENTIFICATION_ACCEPTED", "IDENTIFICATION_REJECTED", "DEFAULT_TIMEOUT_MILLIS", "", "MAXIMUM_TIMEOUT_MILLIS", "NANOS_PER_MILLISECOND", "identificationInformation", "Lcom/carplay/iap2/wire/Iap2Frame;", "config", "Lcom/carplay/iap2/transport/Iap2IdentificationConfig;", "remainingMillis", "deadlineNanos", "MESSAGES_SENT_BY_ACCESSORY", "", "MESSAGES_RECEIVED_FROM_PHONE", "POWER_SOURCE_UPDATE", "CARPLAY_AVAILABILITY", "CARPLAY_START_SESSION", "ACCESSORY_WIFI_CONFIGURATION_INFORMATION", "LOCATION_INFORMATION", "START_LOCATION_INFORMATION", "STOP_LOCATION_INFORMATION", "WIRELESS_PHONE_MESSAGES", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Iap2Frame identificationInformation(final Iap2IdentificationConfig config) throws Iap2ProtocolException {
            List listPlus;
            Intrinsics.checkNotNullParameter(config, "config");
            final Iap2WirelessIdentification wireless = config.getWireless();
            int[] iArrPlus = config.getLocationInformationEnabled() ? ArraysKt.plus(Iap2IdentificationClient.MESSAGES_SENT_BY_ACCESSORY, 65531) : Iap2IdentificationClient.MESSAGES_SENT_BY_ACCESSORY;
            int[] iArrPlus2 = config.getLocationInformationEnabled() ? ArraysKt.plus(ArraysKt.plus(Iap2IdentificationClient.MESSAGES_RECEIVED_FROM_PHONE, 65530), 65532) : Iap2IdentificationClient.MESSAGES_RECEIVED_FROM_PHONE;
            if (config.getVehicleStatusEnabled()) {
                iArrPlus = ArraysKt.plus(iArrPlus, Iap2VehicleStatus.VEHICLE_STATUS_UPDATE);
                iArrPlus2 = ArraysKt.plus(iArrPlus2, new int[]{Iap2VehicleStatus.START_VEHICLE_STATUS_UPDATES, Iap2VehicleStatus.STOP_VEHICLE_STATUS_UPDATES});
            }
            Iap2Messages this_$iv = Iap2Messages.INSTANCE;
            Iap2Endpoint endpoint$iv = Iap2Endpoints.INSTANCE.getIDENTIFICATION_INFORMATION();
            Iap2BodyBuilder $this$identificationInformation_u24lambda_u247 = Iap2BodyBuilder.INSTANCE.of(endpoint$iv);
            $this$identificationInformation_u24lambda_u247.string(0, config.getName());
            $this$identificationInformation_u24lambda_u247.string(1, config.getModelIdentifier());
            $this$identificationInformation_u24lambda_u247.string(2, config.getManufacturer());
            $this$identificationInformation_u24lambda_u247.string(3, config.getSerialNumber());
            $this$identificationInformation_u24lambda_u247.string(4, config.getFirmwareVersion());
            $this$identificationInformation_u24lambda_u247.string(5, config.getHardwareVersion());
            if (wireless == null) {
                listPlus = ArraysKt.asIterable(iArrPlus);
            } else {
                int[] $this$filterNot$iv = iArrPlus;
                Collection destination$iv$iv = new ArrayList();
                int length = $this$filterNot$iv.length;
                int i = 0;
                while (i < length) {
                    int element$iv$iv = $this$filterNot$iv[i];
                    Iap2Messages this_$iv2 = this_$iv;
                    int[] iArr = iArrPlus;
                    if (!(element$iv$iv == Iap2IdentificationClient.POWER_SOURCE_UPDATE)) {
                        destination$iv$iv.add(Integer.valueOf(element$iv$iv));
                    }
                    i++;
                    this_$iv = this_$iv2;
                    iArrPlus = iArr;
                }
                listPlus = CollectionsKt.plus(ArraysKt.asIterable(CollectionsKt.toIntArray((List) destination$iv$iv)), Integer.valueOf(Iap2IdentificationClient.ACCESSORY_WIFI_CONFIGURATION_INFORMATION));
            }
            $this$identificationInformation_u24lambda_u247.u16List(6, listPlus);
            $this$identificationInformation_u24lambda_u247.u16List(7, wireless == null ? ArraysKt.asIterable(iArrPlus2) : CollectionsKt.plus((Iterable) ArraysKt.asIterable(iArrPlus2), (Iterable) ArraysKt.asIterable(Iap2IdentificationClient.WIRELESS_PHONE_MESSAGES)));
            $this$identificationInformation_u24lambda_u247.u8(8, wireless == null ? 2 : 0);
            $this$identificationInformation_u24lambda_u247.u16(9, 20);
            $this$identificationInformation_u24lambda_u247.group(10, new Function1() { // from class: com.carplay.iap2.transport.Iap2IdentificationClient$Companion$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2IdentificationClient.Companion.identificationInformation$lambda$7$lambda$1(config, (Iap2BodyBuilder) obj);
                }
            });
            $this$identificationInformation_u24lambda_u247.string(12, config.getLanguage());
            $this$identificationInformation_u24lambda_u247.strings(13, CollectionsKt.listOf(config.getLanguage()));
            if (wireless == null) {
                $this$identificationInformation_u24lambda_u247.group(16, new Function1() { // from class: com.carplay.iap2.transport.Iap2IdentificationClient$Companion$$ExternalSyntheticLambda1
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return Iap2IdentificationClient.Companion.identificationInformation$lambda$7$lambda$2(config, (Iap2BodyBuilder) obj);
                    }
                });
            } else {
                $this$identificationInformation_u24lambda_u247.group(17, new Function1() { // from class: com.carplay.iap2.transport.Iap2IdentificationClient$Companion$$ExternalSyntheticLambda2
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return Iap2IdentificationClient.Companion.identificationInformation$lambda$7$lambda$3(wireless, (Iap2BodyBuilder) obj);
                    }
                });
                $this$identificationInformation_u24lambda_u247.group(24, new Function1() { // from class: com.carplay.iap2.transport.Iap2IdentificationClient$Companion$$ExternalSyntheticLambda3
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return Iap2IdentificationClient.Companion.identificationInformation$lambda$7$lambda$4(wireless, (Iap2BodyBuilder) obj);
                    }
                });
            }
            if (config.getVehicleStatusEnabled()) {
                Iap2VehicleStatus.INSTANCE.electricVehicleComponents($this$identificationInformation_u24lambda_u247, config.getManufacturer(), config.getChargingConnectors());
            }
            if (config.getLocationInformationEnabled()) {
                $this$identificationInformation_u24lambda_u247.group(22, new Function1() { // from class: com.carplay.iap2.transport.Iap2IdentificationClient$Companion$$ExternalSyntheticLambda4
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return Iap2IdentificationClient.Companion.identificationInformation$lambda$7$lambda$5(config, (Iap2BodyBuilder) obj);
                    }
                });
            }
            $this$identificationInformation_u24lambda_u247.group(30, new Function1() { // from class: com.carplay.iap2.transport.Iap2IdentificationClient$Companion$$ExternalSyntheticLambda5
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2IdentificationClient.Companion.identificationInformation$lambda$7$lambda$6((Iap2BodyBuilder) obj);
                }
            });
            return $this$identificationInformation_u24lambda_u247.build();
        }

        static final Unit identificationInformation$lambda$7$lambda$1(Iap2IdentificationConfig $config, Iap2BodyBuilder group) {
            Intrinsics.checkNotNullParameter(group, "$this$group");
            group.u8(0, 1);
            group.string(1, $config.getExternalAccessoryProtocol());
            group.u8(2, 0);
            return Unit.INSTANCE;
        }

        static final Unit identificationInformation$lambda$7$lambda$2(Iap2IdentificationConfig $config, Iap2BodyBuilder group) {
            Intrinsics.checkNotNullParameter(group, "$this$group");
            group.u16(0, 0);
            group.string(1, "USBHostTransport");
            group.m51void(2);
            group.u8(3, $config.getCarPlayUsbInterfaceNumber());
            group.m51void(4);
            return Unit.INSTANCE;
        }

        static final Unit identificationInformation$lambda$7$lambda$3(Iap2WirelessIdentification $wireless, Iap2BodyBuilder group) {
            Intrinsics.checkNotNullParameter(group, "$this$group");
            group.u16(0, 0);
            group.string(1, "blue");
            group.m51void(2);
            group.bytes(3, $wireless.bluetoothMacBytes$core_iap2());
            group.string(4, "blue");
            group.m51void(5);
            return Unit.INSTANCE;
        }

        static final Unit identificationInformation$lambda$7$lambda$4(Iap2WirelessIdentification $wireless, Iap2BodyBuilder group) {
            Intrinsics.checkNotNullParameter(group, "$this$group");
            group.u16(0, 1);
            group.string(1, $wireless.getSsid());
            group.m51void(2);
            group.u16(3, 1);
            group.m51void(4);
            group.m51void(5);
            return Unit.INSTANCE;
        }

        static final Unit identificationInformation$lambda$7$lambda$5(Iap2IdentificationConfig $config, Iap2BodyBuilder group) {
            Intrinsics.checkNotNullParameter(group, "$this$group");
            group.u16(0, 0);
            group.string(1, $config.getName());
            group.m51void(17);
            group.m51void(18);
            if ($config.getVehicleSpeedEnabled()) {
                group.m51void(20);
            }
            return Unit.INSTANCE;
        }

        static final Unit identificationInformation$lambda$7$lambda$6(Iap2BodyBuilder group) {
            Intrinsics.checkNotNullParameter(group, "$this$group");
            group.u16(0, 42);
            group.string(1, "RouteGuidance");
            group.u16(2, 64);
            group.u16(4, 64);
            group.u16(6, 8);
            return Unit.INSTANCE;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final long remainingMillis(long deadlineNanos) throws IphoneUsbException.TimedOut {
            long remainingNanos = deadlineNanos - System.nanoTime();
            if (remainingNanos <= 0) {
                throw new IphoneUsbException.TimedOut("iAP2 identification timed out", null, 2, null);
            }
            return Math.min(300000L, ((remainingNanos + 1000000) - 1) / 1000000);
        }
    }
}
