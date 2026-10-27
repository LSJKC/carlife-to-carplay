package com.carplay.iap2.transport;

import com.carplay.iap2.wire.Iap2Frame;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: Iap2LocationClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u0000 \u001c2\u00020\u0001:\u0001\u001cB5\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u0012\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t¢\u0006\u0004\b\u000b\u0010\fJ\"\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u00132\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00070\u0005J\u001a\u0010\u0015\u001a\u00020\u00072\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00070\u0005J\u000e\u0010\u0016\u001a\u00020\n2\u0006\u0010\u0017\u001a\u00020\nJ\b\u0010\u0018\u001a\u00020\nH\u0002J\u001c\u0010\u0019\u001a\u00020\u00072\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00070\u0005H\u0002J\b\u0010\u001a\u001a\u00020\u000eH\u0002J\u001c\u0010\u001b\u001a\u00020\u00072\u0012\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u00070\u0005H\u0002R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LocationReporter;", "", "provider", "Lcom/carplay/iap2/transport/Iap2LocationProvider;", "onProgress", "Lkotlin/Function1;", "", "", "nanoTime", "Lkotlin/Function0;", "", "<init>", "(Lcom/carplay/iap2/transport/Iap2LocationProvider;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;)V", "active", "", "sentLogged", "lastAttemptNanos", "handle", "frame", "Lcom/carplay/iap2/wire/Iap2Frame;", "send", "tick", "pollTimeout", "remainingMillis", "sinceAttemptMillis", "start", "startProvider", "sendLatest", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2LocationReporter {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final long POLL_INTERVAL_MILLIS = 1000;
    private boolean active;
    private long lastAttemptNanos;
    private final Function0<Long> nanoTime;
    private final Function1<String, Unit> onProgress;
    private final Iap2LocationProvider provider;
    private boolean sentLogged;

    /* JADX WARN: Multi-variable type inference failed */
    public Iap2LocationReporter(Iap2LocationProvider provider, Function1<? super String, Unit> onProgress, Function0<Long> nanoTime) {
        Intrinsics.checkNotNullParameter(onProgress, "onProgress");
        Intrinsics.checkNotNullParameter(nanoTime, "nanoTime");
        this.provider = provider;
        this.onProgress = onProgress;
        this.nanoTime = nanoTime;
    }

    public /* synthetic */ Iap2LocationReporter(Iap2LocationProvider iap2LocationProvider, Function1 function1, AnonymousClass1 anonymousClass1, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(iap2LocationProvider, function1, (i & 4) != 0 ? AnonymousClass1.INSTANCE : anonymousClass1);
    }

    /* JADX INFO: renamed from: com.carplay.iap2.transport.Iap2LocationReporter$1, reason: invalid class name */
    /* JADX INFO: compiled from: Iap2LocationClient.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function0<Long> {
        public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

        AnonymousClass1() {
            super(0, System.class, "nanoTime", "nanoTime()J", 0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        public final Long invoke() {
            return Long.valueOf(System.nanoTime());
        }
    }

    public final boolean handle(Iap2Frame frame, Function1<? super Iap2Frame, Unit> send) {
        Intrinsics.checkNotNullParameter(frame, "frame");
        Intrinsics.checkNotNullParameter(send, "send");
        switch (frame.getMessageId()) {
            case Iap2LocationMessages.START_LOCATION_INFORMATION /* 65530 */:
                Set<Integer> setRequestedComponents = Iap2LocationMessages.INSTANCE.requestedComponents(frame);
                this.onProgress.invoke("iap2 rx=0xfffa start-location-information components=" + setRequestedComponents);
                Iap2LocationProvider iap2LocationProvider = this.provider;
                if (iap2LocationProvider != null) {
                    iap2LocationProvider.onRequested(setRequestedComponents);
                }
                start(send);
                return true;
            case Iap2LocationMessages.LOCATION_INFORMATION /* 65531 */:
            default:
                return false;
            case Iap2LocationMessages.STOP_LOCATION_INFORMATION /* 65532 */:
                this.onProgress.invoke("iap2 rx=0xfffc stop-location-information");
                this.active = false;
                this.sentLogged = false;
                Iap2LocationProvider iap2LocationProvider2 = this.provider;
                if (iap2LocationProvider2 == null) {
                    return true;
                }
                iap2LocationProvider2.stop();
                return true;
        }
    }

    public final void tick(Function1<? super Iap2Frame, Unit> send) {
        Intrinsics.checkNotNullParameter(send, "send");
        if (!this.active || sinceAttemptMillis() < 1000) {
            return;
        }
        sendLatest(send);
    }

    public final long pollTimeout(long remainingMillis) {
        return this.active ? Math.min(remainingMillis, RangesKt.coerceAtLeast(1000 - sinceAttemptMillis(), 1L)) : remainingMillis;
    }

    private final long sinceAttemptMillis() {
        return (this.nanoTime.invoke().longValue() - this.lastAttemptNanos) / 1000000;
    }

    private final void start(Function1<? super Iap2Frame, Unit> send) {
        this.active = startProvider();
        this.sentLogged = false;
        if (this.active) {
            sendLatest(send);
        }
    }

    private final boolean startProvider() {
        if (this.provider == null) {
            return false;
        }
        try {
            boolean started = this.provider.start();
            if (!started) {
                this.onProgress.invoke("iap2 location provider did not start");
            }
            return started;
        } catch (Exception error) {
            this.onProgress.invoke("iap2 location provider start failed: " + error.getMessage());
            return false;
        }
    }

    private final void sendLatest(Function1<? super Iap2Frame, Unit> send) {
        String sentence;
        this.lastAttemptNanos = this.nanoTime.invoke().longValue();
        Iap2LocationProvider iap2LocationProvider = this.provider;
        if (iap2LocationProvider == null || (sentence = iap2LocationProvider.latestNmea()) == null) {
            return;
        }
        send.invoke(Iap2LocationMessages.INSTANCE.locationInformation(sentence));
        if (!this.sentLogged) {
            this.sentLogged = true;
            this.onProgress.invoke("iap2 tx=0xfffb location-information");
        }
    }

    /* JADX INFO: compiled from: Iap2LocationClient.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LocationReporter$Companion;", "", "<init>", "()V", "POLL_INTERVAL_MILLIS", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
