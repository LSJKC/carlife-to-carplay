package com.carplay.iap2.transport;

import com.carplay.iap2.message.Iap2AvailabilityState;
import com.carplay.iap2.message.Iap2CarPlayMessages;
import com.carplay.iap2.message.Iap2ControlMessages;
import com.carplay.iap2.session.Iap2Session;
import com.carplay.iap2.wire.Iap2Frame;
import com.carplay.mfi.Iap2MfiAuthenticationClient;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Iap2WiredControlClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000  2\u00020\u0001:\u0001 B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007Jl\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00132\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0014\b\u0002\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u00190\u00172\u0014\b\u0002\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u001b\u0012\u0004\u0012\u00020\u00190\u0017J\u0018\u0010\u001c\u001a\u00020\u00192\u0006\u0010\u001d\u001a\u00020\u00182\u0006\u0010\u001e\u001a\u00020\u001fH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006!"}, d2 = {"Lcom/carplay/iap2/transport/Iap2WiredControlClient;", "", "session", "Lcom/carplay/iap2/session/Iap2Session;", "mfi", "Lcom/carplay/mfi/Iap2MfiAuthenticationClient;", "<init>", "(Lcom/carplay/iap2/session/Iap2Session;Lcom/carplay/mfi/Iap2MfiAuthenticationClient;)V", "run", "Lcom/carplay/iap2/transport/Iap2WiredControlResult;", "identification", "Lcom/carplay/iap2/transport/Iap2IdentificationConfig;", "endpoint", "Lcom/carplay/iap2/transport/Iap2WiredCarPlayEndpoint;", "availableCurrentMilliAmps", "", "timeoutMillis", "", "locationProvider", "Lcom/carplay/iap2/transport/Iap2LocationProvider;", "vehicleStatusProvider", "Lcom/carplay/iap2/transport/VehicleStatusProvider;", "onIncoming", "Lkotlin/Function1;", "Lcom/carplay/iap2/wire/Iap2Frame;", "", "onProgress", "", "send", "frame", "deadlineNanos", "Lcom/carplay/iap2/transport/Iap2ControlDeadline;", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2WiredControlClient {
    private static final int CARPLAY_AVAILABILITY = 17152;
    private static final int CARPLAY_START_SESSION = 17153;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final long DEFAULT_TIMEOUT_MILLIS = 60000;
    private static final long MAX_RECV_TIMEOUT_MILLIS = 300000;
    private static final long MAX_TIMEOUT_MILLIS = 86400000;
    private static final long NANOS_PER_MILLISECOND = 1000000;
    public static final long NO_TIMEOUT_MILLIS = Long.MAX_VALUE;
    private final Iap2MfiAuthenticationClient mfi;
    private final Iap2Session session;

    public Iap2WiredControlClient(Iap2Session session, Iap2MfiAuthenticationClient mfi) {
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(mfi, "mfi");
        this.session = session;
        this.mfi = mfi;
    }

    static final Unit run$lambda$0(Iap2Frame it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    static final Unit run$lambda$1(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    public final Iap2WiredControlResult run(Iap2IdentificationConfig identification, Iap2WiredCarPlayEndpoint endpoint, int availableCurrentMilliAmps, long timeoutMillis, Iap2LocationProvider locationProvider, VehicleStatusProvider vehicleStatusProvider, Function1<? super Iap2Frame, Unit> onIncoming, Function1<? super String, Unit> onProgress) throws Throwable {
        Intrinsics.checkNotNullParameter(identification, "identification");
        Intrinsics.checkNotNullParameter(endpoint, "endpoint");
        Intrinsics.checkNotNullParameter(onIncoming, "onIncoming");
        Intrinsics.checkNotNullParameter(onProgress, "onProgress");
        boolean z = true;
        if (!(availableCurrentMilliAmps >= 0 && availableCurrentMilliAmps < 65536)) {
            throw new IllegalArgumentException("availableCurrentMilliAmps must be in 0..65535".toString());
        }
        if (timeoutMillis != Long.MAX_VALUE) {
            if (!(1 <= timeoutMillis && timeoutMillis < 86400001)) {
                z = false;
            }
        }
        if (!z) {
            throw new IllegalArgumentException("timeoutMillis must be in 1..86400000 or NO_TIMEOUT_MILLIS".toString());
        }
        final Iap2ControlDeadline deadlineNanos = new Iap2ControlDeadline(timeoutMillis, null, 2, null);
        Iap2IdentificationConfig identified = Iap2VehicleStatusKt.withVehicleStatusFrom(identification, vehicleStatusProvider);
        if (identified.getVehicleStatusEnabled() != identification.getVehicleStatusEnabled()) {
            onProgress.invoke("iap2 no battery reading: not declaring an electric vehicle");
        }
        new Iap2IdentificationClient(this.session).identify(identified, INSTANCE.requireRemaining(deadlineNanos));
        onProgress.invoke("iap2 identification accepted");
        Iap2WiredControlStage iap2WiredControlStage = Iap2WiredControlStage.IDENTIFIED;
        this.mfi.run(this.session, INSTANCE.requireRemaining(deadlineNanos), onProgress);
        Iap2WiredControlStage stage = Iap2WiredControlStage.AUTHENTICATED;
        onProgress.invoke("iap2 authentication accepted");
        deadlineNanos.authenticated();
        send(INSTANCE.powerSourceUpdate(availableCurrentMilliAmps), deadlineNanos);
        for (Iap2Frame subscription : INSTANCE.subscriptions()) {
            send(subscription, deadlineNanos);
        }
        Iap2WiredControlStage stage2 = Iap2WiredControlStage.SUBSCRIBED;
        onProgress.invoke("iap2 power/subscriptions sent");
        int forwardedFrames = 0;
        Iap2LocationReporter location = new Iap2LocationReporter(locationProvider, onProgress, null, 4, null);
        Iap2VehicleStatusReporter vehicleStatus = new Iap2VehicleStatusReporter(vehicleStatusProvider, onProgress, null, 4, null);
        Iap2WiredControlStage stage3 = stage2;
        int carPlayStartSessions = 0;
        while (true) {
            try {
                long remaining = INSTANCE.remainingMillis(deadlineNanos);
                if (remaining == 0) {
                    Iap2WiredControlResult iap2WiredControlResult = new Iap2WiredControlResult(Iap2WiredControlTerminal.TIMED_OUT, stage3, forwardedFrames, carPlayStartSessions);
                    if (locationProvider != null) {
                        locationProvider.stop();
                    }
                    return iap2WiredControlResult;
                }
                location.tick(new Function1() { // from class: com.carplay.iap2.transport.Iap2WiredControlClient$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return Iap2WiredControlClient.run$lambda$4(this.f$0, deadlineNanos, (Iap2Frame) obj);
                    }
                });
                vehicleStatus.tick(new Function1() { // from class: com.carplay.iap2.transport.Iap2WiredControlClient$$ExternalSyntheticLambda1
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return Iap2WiredControlClient.run$lambda$5(this.f$0, deadlineNanos, (Iap2Frame) obj);
                    }
                });
                try {
                    long pollTimeout = vehicleStatus.pollTimeout(location.pollTimeout(remaining));
                    Iap2Frame incoming = this.session.recv(pollTimeout);
                    if (incoming != null) {
                        switch (incoming.getMessageId()) {
                            case CARPLAY_AVAILABILITY /* 17152 */:
                                onProgress.invoke("iap2 rx=0x4300 carplay-availability");
                                onProgress.invoke(INSTANCE.carPlayAvailabilitySummary(incoming.getPayload()));
                                send(INSTANCE.carPlayStartSession(endpoint), deadlineNanos);
                                stage3 = Iap2WiredControlStage.CARPLAY_START_SENT;
                                carPlayStartSessions++;
                                onProgress.invoke("iap2 tx=0x4301 carplay-start-session");
                                continue;
                            case Iap2VehicleStatus.START_VEHICLE_STATUS_UPDATES /* 41216 */:
                            case Iap2VehicleStatus.STOP_VEHICLE_STATUS_UPDATES /* 41218 */:
                                vehicleStatus.handle(incoming, new Function1() { // from class: com.carplay.iap2.transport.Iap2WiredControlClient$$ExternalSyntheticLambda3
                                    @Override // kotlin.jvm.functions.Function1
                                    public final Object invoke(Object obj) {
                                        return Iap2WiredControlClient.run$lambda$7(this.f$0, deadlineNanos, (Iap2Frame) obj);
                                    }
                                });
                                break;
                            case Iap2LocationMessages.START_LOCATION_INFORMATION /* 65530 */:
                            case Iap2LocationMessages.STOP_LOCATION_INFORMATION /* 65532 */:
                                location.handle(incoming, new Function1() { // from class: com.carplay.iap2.transport.Iap2WiredControlClient$$ExternalSyntheticLambda2
                                    @Override // kotlin.jvm.functions.Function1
                                    public final Object invoke(Object obj) {
                                        return Iap2WiredControlClient.run$lambda$6(this.f$0, deadlineNanos, (Iap2Frame) obj);
                                    }
                                });
                                break;
                            default:
                                String string = Integer.toString(incoming.getMessageId(), CharsKt.checkRadix(16));
                                Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                                onProgress.invoke("iap2 rx=0x" + StringsKt.padStart(string, 4, '0'));
                                onIncoming.invoke(incoming);
                                forwardedFrames++;
                                continue;
                        }
                    } else {
                        if (this.session.isClosed()) {
                            Iap2WiredControlResult iap2WiredControlResult2 = new Iap2WiredControlResult(Iap2WiredControlTerminal.CHANNEL_CLOSED, stage3, forwardedFrames, carPlayStartSessions);
                            if (locationProvider != null) {
                                locationProvider.stop();
                            }
                            return iap2WiredControlResult2;
                        }
                        if (INSTANCE.remainingMillis(deadlineNanos) == 0) {
                            Iap2WiredControlResult iap2WiredControlResult3 = new Iap2WiredControlResult(Iap2WiredControlTerminal.TIMED_OUT, stage3, forwardedFrames, carPlayStartSessions);
                            if (locationProvider != null) {
                                locationProvider.stop();
                            }
                            return iap2WiredControlResult3;
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
            if (locationProvider != null) {
                locationProvider.stop();
            }
            throw th;
        }
    }

    static final Unit run$lambda$4(Iap2WiredControlClient this$0, Iap2ControlDeadline $deadlineNanos, Iap2Frame it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.send(it, $deadlineNanos);
        return Unit.INSTANCE;
    }

    static final Unit run$lambda$5(Iap2WiredControlClient this$0, Iap2ControlDeadline $deadlineNanos, Iap2Frame it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.send(it, $deadlineNanos);
        return Unit.INSTANCE;
    }

    static final Unit run$lambda$6(Iap2WiredControlClient this$0, Iap2ControlDeadline $deadlineNanos, Iap2Frame it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.send(it, $deadlineNanos);
        return Unit.INSTANCE;
    }

    static final Unit run$lambda$7(Iap2WiredControlClient this$0, Iap2ControlDeadline $deadlineNanos, Iap2Frame it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.send(it, $deadlineNanos);
        return Unit.INSTANCE;
    }

    private final void send(Iap2Frame frame, Iap2ControlDeadline deadlineNanos) {
        this.session.send(frame, INSTANCE.requireRemaining(deadlineNanos));
    }

    /* JADX INFO: compiled from: Iap2WiredControlClient.kt */
    @Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0007J\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0011J\u000e\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018J\u0010\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\u0010\u0010\u001c\u001a\u00020\u00052\u0006\u0010\u001a\u001a\u00020\u001bH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lcom/carplay/iap2/transport/Iap2WiredControlClient$Companion;", "", "<init>", "()V", "NO_TIMEOUT_MILLIS", "", "CARPLAY_AVAILABILITY", "", "CARPLAY_START_SESSION", "DEFAULT_TIMEOUT_MILLIS", "MAX_TIMEOUT_MILLIS", "MAX_RECV_TIMEOUT_MILLIS", "NANOS_PER_MILLISECOND", "powerSourceUpdate", "Lcom/carplay/iap2/wire/Iap2Frame;", "availableCurrentMilliAmps", "subscriptions", "", "carPlayStartSession", "endpoint", "Lcom/carplay/iap2/transport/Iap2WiredCarPlayEndpoint;", "carPlayAvailabilitySummary", "", "payload", "", "requireRemaining", "deadlineNanos", "Lcom/carplay/iap2/transport/Iap2ControlDeadline;", "remainingMillis", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Iap2Frame powerSourceUpdate(int availableCurrentMilliAmps) {
            return Iap2ControlMessages.powerSourceUpdate$default(Iap2ControlMessages.INSTANCE, availableCurrentMilliAmps, true, null, null, false, 28, null);
        }

        public final List<Iap2Frame> subscriptions() {
            return Iap2ControlMessages.INSTANCE.subscriptions();
        }

        public final Iap2Frame carPlayStartSession(Iap2WiredCarPlayEndpoint endpoint) {
            Intrinsics.checkNotNullParameter(endpoint, "endpoint");
            Iap2CarPlayMessages iap2CarPlayMessages = Iap2CarPlayMessages.INSTANCE;
            List<String> ipv6Addresses = endpoint.getIpv6Addresses();
            return iap2CarPlayMessages.startSession(endpoint.getAirPlayPort(), endpoint.getPublicKey(), endpoint.getSourceVersion(), (944 & 8) != 0 ? CollectionsKt.emptyList() : ipv6Addresses, (944 & 16) != 0 ? null : null, (944 & 32) != 0 ? null : null, (944 & 64) != 0 ? null : endpoint.getDeviceIdentifier(), (944 & 128) != 0 ? null : null, (944 & 256) != 0 ? null : null, (944 & 512) != 0 ? null : null);
        }

        public final String carPlayAvailabilitySummary(byte[] payload) {
            Intrinsics.checkNotNullParameter(payload, "payload");
            try {
                Iap2AvailabilityState availability = Iap2CarPlayMessages.INSTANCE.availability(payload).getWired();
                Boolean available = availability != null ? availability.getAvailable() : null;
                String transport = availability != null ? availability.getIdentifier() : null;
                return "iap2 4300 wiredAvailable=" + available + " usbTransport=" + (transport == null ? "none" : transport);
            } catch (RuntimeException error) {
                return "iap2 4300 decode failed: " + error.getMessage();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final long requireRemaining(Iap2ControlDeadline deadlineNanos) throws IphoneUsbException.TimedOut {
            long it = remainingMillis(deadlineNanos);
            if (it == 0) {
                throw new IphoneUsbException.TimedOut("Timed out during wired iAP2 control bring-up", null, 2, null);
            }
            return it;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final long remainingMillis(Iap2ControlDeadline deadlineNanos) {
            return deadlineNanos.remainingMillis();
        }
    }
}
