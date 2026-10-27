package com.carplay.iap2.transport;

import androidx.core.os.EnvironmentCompat;
import com.carplay.iap2.message.Iap2AvailabilityState;
import com.carplay.iap2.message.Iap2CarPlayAvailability;
import com.carplay.iap2.message.Iap2CarPlayMessages;
import com.carplay.iap2.message.Iap2DeviceTransportIdentifier;
import com.carplay.iap2.message.Iap2WirelessMessages;
import com.carplay.iap2.message.Iap2WirelessSessionParameters;
import com.carplay.iap2.session.Iap2Session;
import com.carplay.iap2.wire.Iap2Frame;
import com.carplay.mfi.Iap2MfiAuthenticationClient;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Iap2WirelessControlClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u0000 !2\u00020\u0001:\u0001!B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007Jª\u0001\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00112\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u00152\u000e\b\u0002\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00160\u00152\u0014\b\u0002\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u00160\u00192\u0014\b\u0002\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020\u00160\u00192\u000e\b\u0002\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001e0\u00152\u0014\b\u0002\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020 \u0012\u0004\u0012\u00020\u00160\u0019R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\""}, d2 = {"Lcom/carplay/iap2/transport/Iap2WirelessControlClient;", "", "session", "Lcom/carplay/iap2/session/Iap2Session;", "mfi", "Lcom/carplay/mfi/Iap2MfiAuthenticationClient;", "<init>", "(Lcom/carplay/iap2/session/Iap2Session;Lcom/carplay/mfi/Iap2MfiAuthenticationClient;)V", "run", "Lcom/carplay/iap2/transport/Iap2WirelessControlResult;", "identification", "Lcom/carplay/iap2/transport/Iap2IdentificationConfig;", "endpoint", "Lcom/carplay/iap2/transport/Iap2WirelessCarPlayEndpoint;", "timeoutMillis", "", "locationProvider", "Lcom/carplay/iap2/transport/Iap2LocationProvider;", "vehicleStatusProvider", "Lcom/carplay/iap2/transport/VehicleStatusProvider;", "onReady", "Lkotlin/Function0;", "", "beforeStartSession", "onStartSessionSent", "Lkotlin/Function1;", "Lcom/carplay/iap2/transport/Iap2StartSessionSent;", "onIncoming", "Lcom/carplay/iap2/wire/Iap2Frame;", "keepAliveAfterDeadline", "", "onProgress", "", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2WirelessControlClient {
    private static final int ACCESSORY_WIFI_CONFIGURATION = 22275;
    private static final int CARPLAY_AVAILABILITY = 17152;
    private static final int CARPLAY_START_SESSION = 17153;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final long DEFAULT_TIMEOUT_MILLIS = 60000;
    private static final int DEVICE_TRANSPORT_IDENTIFIER_NOTIFICATION = 19982;
    private static final int MAX_POST_TRANSPORT_WIFI_CONFIGURATION_SENDS = 2;
    private static final int MAX_PRE_TRANSPORT_WIFI_CONFIGURATION_SENDS = 5;
    private static final long MAX_TIMEOUT_MILLIS = 86400000;
    public static final long NO_TIMEOUT_MILLIS = Long.MAX_VALUE;
    private static final int REQUEST_ACCESSORY_WIFI_CONFIGURATION = 22274;
    private static final int WIRELESS_CARPLAY_UPDATE = 19981;
    private final Iap2MfiAuthenticationClient mfi;
    private final Iap2Session session;

    public Iap2WirelessControlClient(Iap2Session session, Iap2MfiAuthenticationClient mfi) {
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(mfi, "mfi");
        this.session = session;
        this.mfi = mfi;
    }

    static final Unit run$lambda$2(Iap2StartSessionSent it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    static final Unit run$lambda$3(Iap2Frame it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    static final boolean run$lambda$4() {
        return false;
    }

    static final Unit run$lambda$5(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:16:0x0062  */
    public final Iap2WirelessControlResult run(Iap2IdentificationConfig identification, Iap2WirelessCarPlayEndpoint endpoint, long timeoutMillis, Iap2LocationProvider locationProvider, VehicleStatusProvider vehicleStatusProvider, Function0<Unit> onReady, Function0<Unit> beforeStartSession, Function1<? super Iap2StartSessionSent, Unit> onStartSessionSent, Function1<? super Iap2Frame, Unit> onIncoming, Function0<Boolean> keepAliveAfterDeadline, Function1<? super String, Unit> onProgress) throws Throwable {
        boolean z;
        Iap2WirelessControlStage stage;
        int postTransportWiFiConfigurationsSent;
        Intrinsics.checkNotNullParameter(identification, "identification");
        Intrinsics.checkNotNullParameter(endpoint, "endpoint");
        Intrinsics.checkNotNullParameter(onReady, "onReady");
        Intrinsics.checkNotNullParameter(beforeStartSession, "beforeStartSession");
        Intrinsics.checkNotNullParameter(onStartSessionSent, "onStartSessionSent");
        Intrinsics.checkNotNullParameter(onIncoming, "onIncoming");
        Intrinsics.checkNotNullParameter(keepAliveAfterDeadline, "keepAliveAfterDeadline");
        Intrinsics.checkNotNullParameter(onProgress, "onProgress");
        if (!(identification.getWireless() != null)) {
            throw new IllegalArgumentException("Wireless control requires an Iap2IdentificationConfig with wireless transport".toString());
        }
        if (timeoutMillis != Long.MAX_VALUE) {
            z = (1L > timeoutMillis ? 1 : (1L == timeoutMillis ? 0 : -1)) <= 0 && (timeoutMillis > 86400001L ? 1 : (timeoutMillis == 86400001L ? 0 : -1)) < 0;
        }
        if (!z) {
            throw new IllegalArgumentException("timeoutMillis must be in 1..86400000 or NO_TIMEOUT_MILLIS".toString());
        }
        WirelessControlDeadline deadline = new WirelessControlDeadline(timeoutMillis, null, 2, null);
        Iap2IdentificationConfig identified = Iap2VehicleStatusKt.withVehicleStatusFrom(identification, vehicleStatusProvider);
        if (identified.getVehicleStatusEnabled() != identification.getVehicleStatusEnabled()) {
            onProgress.invoke("iap2 no battery reading: not declaring an electric vehicle");
        }
        new Iap2IdentificationClient(this.session).identify(identified, WirelessControlDeadline.requireRemaining$default(deadline, null, 1, null));
        onProgress.invoke("iap2 identification accepted");
        Iap2WirelessControlStage iap2WirelessControlStage = Iap2WirelessControlStage.IDENTIFIED;
        this.mfi.run(this.session, WirelessControlDeadline.requireRemaining$default(deadline, null, 1, null), onProgress);
        Iap2WirelessControlStage stage2 = Iap2WirelessControlStage.AUTHENTICATED;
        onProgress.invoke("iap2 authentication accepted");
        for (Iap2Frame subscription : Iap2WiredControlClient.INSTANCE.subscriptions()) {
            this.session.send(subscription, WirelessControlDeadline.requireRemaining$default(deadline, null, 1, null));
        }
        Iap2WirelessControlStage stage3 = Iap2WirelessControlStage.SUBSCRIBED;
        onProgress.invoke("iap2 subscriptions sent");
        onReady.invoke();
        int preTransportWiFiConfigurationsSent = 0;
        Iap2LocationReporter location = new Iap2LocationReporter(locationProvider, onProgress, null, 4, null);
        Iap2LocationReporter location2 = location;
        Iap2VehicleStatusReporter vehicleStatus = new Iap2VehicleStatusReporter(vehicleStatusProvider, onProgress, null, 4, null);
        Iap2WirelessControlStage stage4 = stage3;
        int forwardedFrames = 0;
        int wifiConfigurationsSent = 0;
        int carPlayStartSessionsSent = 0;
        int postTransportWiFiConfigurationsSent2 = 0;
        boolean transportNotificationSeen = false;
        boolean wirelessCarPlayAvailableSeen = false;
        while (true) {
            try {
                long remaining = deadline.remainingMillis(keepAliveAfterDeadline);
                try {
                    if (remaining == 0) {
                        Iap2WirelessControlResult iap2WirelessControlResult = new Iap2WirelessControlResult(Iap2WirelessControlTerminal.TIMED_OUT, stage4, forwardedFrames, wifiConfigurationsSent, carPlayStartSessionsSent, transportNotificationSeen, postTransportWiFiConfigurationsSent2, wirelessCarPlayAvailableSeen);
                        if (locationProvider != null) {
                            locationProvider.stop();
                        }
                        return iap2WirelessControlResult;
                    }
                    location2.tick(new AnonymousClass9(deadline, keepAliveAfterDeadline));
                    vehicleStatus.tick(new AnonymousClass10(deadline, keepAliveAfterDeadline));
                    long pollTimeout = vehicleStatus.pollTimeout(location2.pollTimeout(remaining));
                    Iap2Frame incoming = this.session.recv(pollTimeout);
                    if (incoming == null) {
                        try {
                            if (this.session.isClosed()) {
                                Iap2WirelessControlResult iap2WirelessControlResult2 = new Iap2WirelessControlResult(Iap2WirelessControlTerminal.CHANNEL_CLOSED, stage4, forwardedFrames, wifiConfigurationsSent, carPlayStartSessionsSent, transportNotificationSeen, postTransportWiFiConfigurationsSent2, wirelessCarPlayAvailableSeen);
                                if (locationProvider != null) {
                                    locationProvider.stop();
                                }
                                return iap2WirelessControlResult2;
                            }
                            if (deadline.remainingMillis(keepAliveAfterDeadline) == 0) {
                                Iap2WirelessControlResult iap2WirelessControlResult3 = new Iap2WirelessControlResult(Iap2WirelessControlTerminal.TIMED_OUT, stage4, forwardedFrames, wifiConfigurationsSent, carPlayStartSessionsSent, transportNotificationSeen, postTransportWiFiConfigurationsSent2, wirelessCarPlayAvailableSeen);
                                if (locationProvider != null) {
                                    locationProvider.stop();
                                }
                                return iap2WirelessControlResult3;
                            }
                            stage = stage4;
                            postTransportWiFiConfigurationsSent = postTransportWiFiConfigurationsSent2;
                            stage4 = stage;
                            postTransportWiFiConfigurationsSent2 = postTransportWiFiConfigurationsSent;
                            location2 = location2;
                        } catch (Throwable th) {
                            th = th;
                        }
                    } else {
                        stage = stage4;
                        postTransportWiFiConfigurationsSent = postTransportWiFiConfigurationsSent2;
                        try {
                            switch (incoming.getMessageId()) {
                                case CARPLAY_AVAILABILITY /* 17152 */:
                                    Iap2LocationReporter location3 = location2;
                                    onProgress.invoke("iap2 rx=0x4300 carplay-availability");
                                    onProgress.invoke(INSTANCE.carPlayAvailabilityDiagnostic$core_iap2(incoming));
                                    beforeStartSession.invoke();
                                    Iap2WirelessControlClientKt.sendStartSession(endpoint, new AnonymousClass11(deadline, keepAliveAfterDeadline), onStartSessionSent);
                                    stage4 = INSTANCE.later(stage, Iap2WirelessControlStage.CARPLAY_START_SENT);
                                    carPlayStartSessionsSent++;
                                    try {
                                        onProgress.invoke("iap2 tx=0x4301 carplay-start-session");
                                        postTransportWiFiConfigurationsSent2 = postTransportWiFiConfigurationsSent;
                                        location2 = location3;
                                    } catch (Throwable th2) {
                                        th = th2;
                                    }
                                    break;
                                case WIRELESS_CARPLAY_UPDATE /* 19981 */:
                                    Iap2LocationReporter location4 = location2;
                                    boolean available = Iap2WirelessMessages.INSTANCE.wirelessCarPlayAvailability(incoming);
                                    if (available) {
                                        wirelessCarPlayAvailableSeen = true;
                                    }
                                    onProgress.invoke("iap2 rx=0x4e0d wireless-carplay-update available=" + available);
                                    stage4 = stage;
                                    postTransportWiFiConfigurationsSent2 = postTransportWiFiConfigurationsSent;
                                    location2 = location4;
                                    break;
                                case DEVICE_TRANSPORT_IDENTIFIER_NOTIFICATION /* 19982 */:
                                    Iap2LocationReporter location5 = location2;
                                    transportNotificationSeen = true;
                                    Iap2DeviceTransportIdentifier identifiers = Iap2WirelessMessages.INSTANCE.deviceTransportIdentifier(incoming);
                                    Iap2WirelessControlStage stage5 = INSTANCE.later(stage, Iap2WirelessControlStage.TRANSPORT_NOTIFIED);
                                    try {
                                        String bluetoothMac = identifiers.getBluetoothMac();
                                        String str = "none";
                                        if (bluetoothMac == null) {
                                            bluetoothMac = "none";
                                        }
                                        String usbTransportIdentifier = identifiers.getUsbTransportIdentifier();
                                        if (usbTransportIdentifier != null) {
                                            str = usbTransportIdentifier;
                                        }
                                        onProgress.invoke("iap2 rx=0x4e0e device-transport-identifier bluetooth=" + bluetoothMac + " usb=" + str + "; resending 0x5703");
                                        if (postTransportWiFiConfigurationsSent >= 2) {
                                            onProgress.invoke("iap2 post-transport 0x5703 ignored: maximum Wi-Fi configuration sends reached");
                                            stage4 = stage5;
                                            postTransportWiFiConfigurationsSent2 = postTransportWiFiConfigurationsSent;
                                            location2 = location5;
                                        } else {
                                            run$sendControl(this, deadline, keepAliveAfterDeadline, INSTANCE.accessoryWiFiConfiguration(endpoint));
                                            stage4 = INSTANCE.later(stage5, Iap2WirelessControlStage.POST_TRANSPORT_WIFI_CONFIG_SENT);
                                            wifiConfigurationsSent++;
                                            postTransportWiFiConfigurationsSent2 = postTransportWiFiConfigurationsSent + 1;
                                            onProgress.invoke("iap2 tx=0x5703 post-transport accessory-wifi-configuration");
                                            location2 = location5;
                                        }
                                    } catch (Throwable th3) {
                                        th = th3;
                                    }
                                    break;
                                case REQUEST_ACCESSORY_WIFI_CONFIGURATION /* 22274 */:
                                    try {
                                        onProgress.invoke("iap2 rx=0x5702 request-wifi-configuration");
                                        boolean postTransport = transportNotificationSeen;
                                        int sentCount = postTransport ? postTransportWiFiConfigurationsSent : preTransportWiFiConfigurationsSent;
                                        int limit = postTransport ? 2 : 5;
                                        if (sentCount >= limit) {
                                            try {
                                                onProgress.invoke("iap2 0x5703 ignored: maximum Wi-Fi configuration sends reached");
                                                stage4 = stage;
                                                postTransportWiFiConfigurationsSent2 = postTransportWiFiConfigurationsSent;
                                                location2 = location2;
                                            } catch (Throwable th4) {
                                                th = th4;
                                            }
                                        } else {
                                            Iap2LocationReporter location6 = location2;
                                            run$sendControl(this, deadline, keepAliveAfterDeadline, INSTANCE.accessoryWiFiConfiguration(endpoint));
                                            stage4 = INSTANCE.later(stage, postTransport ? Iap2WirelessControlStage.POST_TRANSPORT_WIFI_CONFIG_SENT : Iap2WirelessControlStage.WIFI_CONFIG_SENT);
                                            wifiConfigurationsSent++;
                                            if (postTransport) {
                                                postTransportWiFiConfigurationsSent2 = postTransportWiFiConfigurationsSent + 1;
                                            } else {
                                                preTransportWiFiConfigurationsSent++;
                                                postTransportWiFiConfigurationsSent2 = postTransportWiFiConfigurationsSent;
                                            }
                                            try {
                                                onProgress.invoke("iap2 tx=0x5703 accessory-wifi-configuration");
                                                location2 = location6;
                                            } catch (Throwable th5) {
                                                th = th5;
                                            }
                                        }
                                    } catch (Throwable th6) {
                                        th = th6;
                                    }
                                    break;
                                case Iap2VehicleStatus.START_VEHICLE_STATUS_UPDATES /* 41216 */:
                                case Iap2VehicleStatus.STOP_VEHICLE_STATUS_UPDATES /* 41218 */:
                                    vehicleStatus.handle(incoming, new AnonymousClass12(deadline, keepAliveAfterDeadline));
                                    stage4 = stage;
                                    postTransportWiFiConfigurationsSent2 = postTransportWiFiConfigurationsSent;
                                    location2 = location2;
                                    break;
                                case Iap2LocationMessages.START_LOCATION_INFORMATION /* 65530 */:
                                case Iap2LocationMessages.STOP_LOCATION_INFORMATION /* 65532 */:
                                    try {
                                        location2.handle(incoming, new AnonymousClass13(deadline, keepAliveAfterDeadline));
                                        stage4 = stage;
                                        postTransportWiFiConfigurationsSent2 = postTransportWiFiConfigurationsSent;
                                        location2 = location2;
                                    } catch (Throwable th7) {
                                        th = th7;
                                    }
                                    break;
                                default:
                                    Iap2LocationReporter location7 = location2;
                                    try {
                                        String string = Integer.toString(incoming.getMessageId(), CharsKt.checkRadix(16));
                                        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                                        onProgress.invoke("iap2 rx=0x" + StringsKt.padStart(string, 4, '0'));
                                        try {
                                            onIncoming.invoke(incoming);
                                            forwardedFrames++;
                                            stage4 = stage;
                                            postTransportWiFiConfigurationsSent2 = postTransportWiFiConfigurationsSent;
                                            location2 = location7;
                                        } catch (Throwable th8) {
                                            th = th8;
                                        }
                                    } catch (Throwable th9) {
                                        th = th9;
                                    }
                                    break;
                            }
                        } catch (Throwable th10) {
                            th = th10;
                        }
                    }
                } catch (Throwable th11) {
                    th = th11;
                }
            } catch (Throwable th12) {
                th = th12;
            }
            if (locationProvider != null) {
                locationProvider.stop();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void run$sendControl(Iap2WirelessControlClient this$0, WirelessControlDeadline deadline, Function0<Boolean> function0, Iap2Frame frame) {
        this$0.session.send(frame, deadline.requireRemaining(function0));
    }

    /* JADX INFO: renamed from: com.carplay.iap2.transport.Iap2WirelessControlClient$run$9, reason: invalid class name */
    /* JADX INFO: compiled from: Iap2WirelessControlClient.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass9 extends FunctionReferenceImpl implements Function1<Iap2Frame, Unit> {
        final /* synthetic */ WirelessControlDeadline $deadline;
        final /* synthetic */ Function0<Boolean> $keepAliveAfterDeadline;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass9(WirelessControlDeadline wirelessControlDeadline, Function0<Boolean> function0) {
            super(1, Intrinsics.Kotlin.class, "sendControl", "run$sendControl(Lcom/carplay/iap2/transport/Iap2WirelessControlClient;Lcom/carplay/iap2/transport/WirelessControlDeadline;Lkotlin/jvm/functions/Function0;Lcom/carplay/iap2/wire/Iap2Frame;)V", 0);
            this.$deadline = wirelessControlDeadline;
            this.$keepAliveAfterDeadline = function0;
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(Iap2Frame iap2Frame) {
            invoke2(iap2Frame);
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2(Iap2Frame p0) {
            Intrinsics.checkNotNullParameter(p0, "p0");
            Iap2WirelessControlClient.run$sendControl(Iap2WirelessControlClient.this, this.$deadline, this.$keepAliveAfterDeadline, p0);
        }
    }

    /* JADX INFO: renamed from: com.carplay.iap2.transport.Iap2WirelessControlClient$run$10, reason: invalid class name */
    /* JADX INFO: compiled from: Iap2WirelessControlClient.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass10 extends FunctionReferenceImpl implements Function1<Iap2Frame, Unit> {
        final /* synthetic */ WirelessControlDeadline $deadline;
        final /* synthetic */ Function0<Boolean> $keepAliveAfterDeadline;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass10(WirelessControlDeadline wirelessControlDeadline, Function0<Boolean> function0) {
            super(1, Intrinsics.Kotlin.class, "sendControl", "run$sendControl(Lcom/carplay/iap2/transport/Iap2WirelessControlClient;Lcom/carplay/iap2/transport/WirelessControlDeadline;Lkotlin/jvm/functions/Function0;Lcom/carplay/iap2/wire/Iap2Frame;)V", 0);
            this.$deadline = wirelessControlDeadline;
            this.$keepAliveAfterDeadline = function0;
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(Iap2Frame iap2Frame) {
            invoke2(iap2Frame);
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2(Iap2Frame p0) {
            Intrinsics.checkNotNullParameter(p0, "p0");
            Iap2WirelessControlClient.run$sendControl(Iap2WirelessControlClient.this, this.$deadline, this.$keepAliveAfterDeadline, p0);
        }
    }

    /* JADX INFO: renamed from: com.carplay.iap2.transport.Iap2WirelessControlClient$run$11, reason: invalid class name */
    /* JADX INFO: compiled from: Iap2WirelessControlClient.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass11 extends FunctionReferenceImpl implements Function1<Iap2Frame, Unit> {
        final /* synthetic */ WirelessControlDeadline $deadline;
        final /* synthetic */ Function0<Boolean> $keepAliveAfterDeadline;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass11(WirelessControlDeadline wirelessControlDeadline, Function0<Boolean> function0) {
            super(1, Intrinsics.Kotlin.class, "sendControl", "run$sendControl(Lcom/carplay/iap2/transport/Iap2WirelessControlClient;Lcom/carplay/iap2/transport/WirelessControlDeadline;Lkotlin/jvm/functions/Function0;Lcom/carplay/iap2/wire/Iap2Frame;)V", 0);
            this.$deadline = wirelessControlDeadline;
            this.$keepAliveAfterDeadline = function0;
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(Iap2Frame iap2Frame) {
            invoke2(iap2Frame);
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2(Iap2Frame p0) {
            Intrinsics.checkNotNullParameter(p0, "p0");
            Iap2WirelessControlClient.run$sendControl(Iap2WirelessControlClient.this, this.$deadline, this.$keepAliveAfterDeadline, p0);
        }
    }

    /* JADX INFO: renamed from: com.carplay.iap2.transport.Iap2WirelessControlClient$run$12, reason: invalid class name */
    /* JADX INFO: compiled from: Iap2WirelessControlClient.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass12 extends FunctionReferenceImpl implements Function1<Iap2Frame, Unit> {
        final /* synthetic */ WirelessControlDeadline $deadline;
        final /* synthetic */ Function0<Boolean> $keepAliveAfterDeadline;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass12(WirelessControlDeadline wirelessControlDeadline, Function0<Boolean> function0) {
            super(1, Intrinsics.Kotlin.class, "sendControl", "run$sendControl(Lcom/carplay/iap2/transport/Iap2WirelessControlClient;Lcom/carplay/iap2/transport/WirelessControlDeadline;Lkotlin/jvm/functions/Function0;Lcom/carplay/iap2/wire/Iap2Frame;)V", 0);
            this.$deadline = wirelessControlDeadline;
            this.$keepAliveAfterDeadline = function0;
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(Iap2Frame iap2Frame) {
            invoke2(iap2Frame);
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2(Iap2Frame p0) {
            Intrinsics.checkNotNullParameter(p0, "p0");
            Iap2WirelessControlClient.run$sendControl(Iap2WirelessControlClient.this, this.$deadline, this.$keepAliveAfterDeadline, p0);
        }
    }

    /* JADX INFO: renamed from: com.carplay.iap2.transport.Iap2WirelessControlClient$run$13, reason: invalid class name */
    /* JADX INFO: compiled from: Iap2WirelessControlClient.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass13 extends FunctionReferenceImpl implements Function1<Iap2Frame, Unit> {
        final /* synthetic */ WirelessControlDeadline $deadline;
        final /* synthetic */ Function0<Boolean> $keepAliveAfterDeadline;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass13(WirelessControlDeadline wirelessControlDeadline, Function0<Boolean> function0) {
            super(1, Intrinsics.Kotlin.class, "sendControl", "run$sendControl(Lcom/carplay/iap2/transport/Iap2WirelessControlClient;Lcom/carplay/iap2/transport/WirelessControlDeadline;Lkotlin/jvm/functions/Function0;Lcom/carplay/iap2/wire/Iap2Frame;)V", 0);
            this.$deadline = wirelessControlDeadline;
            this.$keepAliveAfterDeadline = function0;
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(Iap2Frame iap2Frame) {
            invoke2(iap2Frame);
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2(Iap2Frame p0) {
            Intrinsics.checkNotNullParameter(p0, "p0");
            Iap2WirelessControlClient.run$sendControl(Iap2WirelessControlClient.this, this.$deadline, this.$keepAliveAfterDeadline, p0);
        }
    }

    /* JADX INFO: compiled from: Iap2WirelessControlClient.kt */
    @Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0015\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0000¢\u0006\u0002\b\u0015J\u000e\u0010\u0016\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\u0018J\u0018\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001b2\u0006\u0010\u001d\u001a\u00020\u001bH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\fX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u001e"}, d2 = {"Lcom/carplay/iap2/transport/Iap2WirelessControlClient$Companion;", "", "<init>", "()V", "REQUEST_ACCESSORY_WIFI_CONFIGURATION", "", "ACCESSORY_WIFI_CONFIGURATION", "CARPLAY_AVAILABILITY", "CARPLAY_START_SESSION", "WIRELESS_CARPLAY_UPDATE", "DEVICE_TRANSPORT_IDENTIFIER_NOTIFICATION", "NO_TIMEOUT_MILLIS", "", "DEFAULT_TIMEOUT_MILLIS", "MAX_TIMEOUT_MILLIS", "MAX_PRE_TRANSPORT_WIFI_CONFIGURATION_SENDS", "MAX_POST_TRANSPORT_WIFI_CONFIGURATION_SENDS", "carPlayAvailabilityDiagnostic", "", "frame", "Lcom/carplay/iap2/wire/Iap2Frame;", "carPlayAvailabilityDiagnostic$core_iap2", "accessoryWiFiConfiguration", "endpoint", "Lcom/carplay/iap2/transport/Iap2WirelessCarPlayEndpoint;", "carPlayStartSession", "later", "Lcom/carplay/iap2/transport/Iap2WirelessControlStage;", "current", "next", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final String carPlayAvailabilityDiagnostic$core_iap2(Iap2Frame frame) {
            Object available;
            Object available2;
            Boolean available3;
            Intrinsics.checkNotNullParameter(frame, "frame");
            try {
                Iap2CarPlayAvailability value = Iap2CarPlayMessages.INSTANCE.availability(frame);
                Iap2AvailabilityState wired = value.getWired();
                Object obj = EnvironmentCompat.MEDIA_UNKNOWN;
                if (wired == null || (available = wired.getAvailable()) == null) {
                    available = EnvironmentCompat.MEDIA_UNKNOWN;
                }
                Iap2AvailabilityState wireless = value.getWireless();
                if (wireless == null || (available2 = wireless.getAvailable()) == null) {
                    available2 = EnvironmentCompat.MEDIA_UNKNOWN;
                }
                Iap2AvailabilityState themeAssets = value.getThemeAssets();
                if (themeAssets != null && (available3 = themeAssets.getAvailable()) != null) {
                    obj = available3;
                }
                return "iap2 availability wired=" + available + " wireless=" + available2 + " themeAssets=" + obj;
            } catch (Exception error) {
                return "iap2 availability decode=failed failureClass=" + error.getClass().getSimpleName();
            }
        }

        public final Iap2Frame accessoryWiFiConfiguration(Iap2WirelessCarPlayEndpoint endpoint) {
            Intrinsics.checkNotNullParameter(endpoint, "endpoint");
            return Iap2WirelessMessages.INSTANCE.accessoryWiFiConfiguration(endpoint.getSsid(), endpoint.getPassphrase(), endpoint.getChannel(), endpoint.getSecurity().getWireValue(), endpoint.getAccessPointBssid());
        }

        public final Iap2Frame carPlayStartSession(Iap2WirelessCarPlayEndpoint endpoint) {
            Intrinsics.checkNotNullParameter(endpoint, "endpoint");
            Iap2CarPlayMessages iap2CarPlayMessages = Iap2CarPlayMessages.INSTANCE;
            Iap2WirelessSessionParameters iap2WirelessSessionParameters = new Iap2WirelessSessionParameters(endpoint.getSsid(), endpoint.getPassphrase(), endpoint.getChannel(), endpoint.getIpAddresses(), endpoint.getSecurity().getWireValue());
            return iap2CarPlayMessages.startSession(endpoint.getAirPlayPort(), endpoint.getPublicKey(), endpoint.getSourceVersion(), (944 & 8) != 0 ? CollectionsKt.emptyList() : null, (944 & 16) != 0 ? null : null, (944 & 32) != 0 ? null : iap2WirelessSessionParameters, (944 & 64) != 0 ? null : endpoint.getDeviceIdentifier(), (944 & 128) != 0 ? null : null, (944 & 256) != 0 ? null : null, (944 & 512) != 0 ? null : null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final Iap2WirelessControlStage later(Iap2WirelessControlStage current, Iap2WirelessControlStage next) {
            return current.ordinal() >= next.ordinal() ? current : next;
        }
    }
}
