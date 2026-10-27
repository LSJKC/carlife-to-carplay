package com.carplay.iap2.transport;

import com.carplay.iap2.wire.Iap2Frame;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2VehicleStatus.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0000\u0018\u00002\u00020\u0001B5\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u0012\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t¢\u0006\u0004\b\u000b\u0010\fJ\"\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u00122\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00070\u0005J\u001a\u0010\u0014\u001a\u00020\u00072\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00070\u0005J\u000e\u0010\u0015\u001a\u00020\n2\u0006\u0010\u0016\u001a\u00020\nJ\u001c\u0010\u0017\u001a\u00020\u00072\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00070\u0005H\u0002R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lcom/carplay/iap2/transport/Iap2VehicleStatusReporter;", "", "provider", "Lcom/carplay/iap2/transport/VehicleStatusProvider;", "onProgress", "Lkotlin/Function1;", "", "", "nanoTime", "Lkotlin/Function0;", "", "<init>", "(Lcom/carplay/iap2/transport/VehicleStatusProvider;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;)V", "active", "", "lastSentNanos", "handle", "frame", "Lcom/carplay/iap2/wire/Iap2Frame;", "send", "tick", "pollTimeout", "remainingMillis", "sendNow", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2VehicleStatusReporter {
    private boolean active;
    private long lastSentNanos;
    private final Function0<Long> nanoTime;
    private final Function1<String, Unit> onProgress;
    private final VehicleStatusProvider provider;

    /* JADX WARN: Multi-variable type inference failed */
    public Iap2VehicleStatusReporter(VehicleStatusProvider provider, Function1<? super String, Unit> onProgress, Function0<Long> nanoTime) {
        Intrinsics.checkNotNullParameter(onProgress, "onProgress");
        Intrinsics.checkNotNullParameter(nanoTime, "nanoTime");
        this.provider = provider;
        this.onProgress = onProgress;
        this.nanoTime = nanoTime;
    }

    public /* synthetic */ Iap2VehicleStatusReporter(VehicleStatusProvider vehicleStatusProvider, Function1 function1, AnonymousClass1 anonymousClass1, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(vehicleStatusProvider, function1, (i & 4) != 0 ? AnonymousClass1.INSTANCE : anonymousClass1);
    }

    /* JADX INFO: renamed from: com.carplay.iap2.transport.Iap2VehicleStatusReporter$1, reason: invalid class name */
    /* JADX INFO: compiled from: Iap2VehicleStatus.kt */
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
            case Iap2VehicleStatus.START_VEHICLE_STATUS_UPDATES /* 41216 */:
                this.active = this.provider != null;
                this.onProgress.invoke("iap2 rx=0xa100 start-vehicle-status provider=" + (this.provider != null));
                if (!this.active) {
                    return true;
                }
                sendNow(send);
                return true;
            case Iap2VehicleStatus.VEHICLE_STATUS_UPDATE /* 41217 */:
            default:
                return false;
            case Iap2VehicleStatus.STOP_VEHICLE_STATUS_UPDATES /* 41218 */:
                this.active = false;
                this.onProgress.invoke("iap2 rx=0xa102 stop-vehicle-status");
                return true;
        }
    }

    public final void tick(Function1<? super Iap2Frame, Unit> send) {
        Intrinsics.checkNotNullParameter(send, "send");
        if (!this.active || this.nanoTime.invoke().longValue() - this.lastSentNanos < 30000000000L) {
            return;
        }
        sendNow(send);
    }

    public final long pollTimeout(long remainingMillis) {
        return this.active ? Math.min(remainingMillis, 30000L) : remainingMillis;
    }

    private final void sendNow(Function1<? super Iap2Frame, Unit> send) {
        VehicleStatusSnapshot status;
        VehicleStatusProvider vehicleStatusProvider = this.provider;
        if (vehicleStatusProvider == null || (status = vehicleStatusProvider.snapshot()) == null) {
            return;
        }
        send.invoke(Iap2VehicleStatus.INSTANCE.update(status));
        this.lastSentNanos = this.nanoTime.invoke().longValue();
        this.onProgress.invoke("iap2 tx=0xa101 vehicle-status range=" + status.getRangeKm() + "km battery=" + status.getBatteryPercent() + "% warning=" + status.getRangeWarning());
    }
}
