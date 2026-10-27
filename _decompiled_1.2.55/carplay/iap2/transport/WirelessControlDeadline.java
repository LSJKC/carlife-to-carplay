package com.carplay.iap2.transport;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2WirelessControlClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\b\u0000\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u0016\u0010\u000b\u001a\u00020\u00032\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\t0\u0005J\u0016\u0010\r\u001a\u00020\u00032\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\t0\u0005R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lcom/carplay/iap2/transport/WirelessControlDeadline;", "", "timeoutMillis", "", "nowNanos", "Lkotlin/Function0;", "<init>", "(JLkotlin/jvm/functions/Function0;)V", "extendedForLiveSession", "", "deadlineNanos", "remainingMillis", "keepAlive", "requireRemaining", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class WirelessControlDeadline {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final long MAX_POLL_MILLIS = 300000;

    @Deprecated
    public static final long NANOS_PER_MILLISECOND = 1000000;
    private final long deadlineNanos;
    private boolean extendedForLiveSession;
    private final Function0<Long> nowNanos;

    /* JADX INFO: renamed from: com.carplay.iap2.transport.WirelessControlDeadline$1, reason: invalid class name */
    /* JADX INFO: compiled from: Iap2WirelessControlClient.kt */
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

    public WirelessControlDeadline(long timeoutMillis, Function0<Long> nowNanos) {
        Intrinsics.checkNotNullParameter(nowNanos, "nowNanos");
        this.nowNanos = nowNanos;
        long j = Long.MAX_VALUE;
        if (timeoutMillis != Long.MAX_VALUE) {
            long now = this.nowNanos.invoke().longValue();
            long delta = 1000000 * timeoutMillis;
            if (now <= Long.MAX_VALUE - delta) {
                j = now + delta;
            }
        }
        this.deadlineNanos = j;
    }

    public /* synthetic */ WirelessControlDeadline(long j, AnonymousClass1 anonymousClass1, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(j, (i & 2) != 0 ? AnonymousClass1.INSTANCE : anonymousClass1);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ long remainingMillis$default(WirelessControlDeadline wirelessControlDeadline, Function0 function0, int i, Object obj) {
        if ((i & 1) != 0) {
            function0 = new Function0() { // from class: com.carplay.iap2.transport.WirelessControlDeadline$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return Boolean.valueOf(WirelessControlDeadline.remainingMillis$lambda$0());
                }
            };
        }
        return wirelessControlDeadline.remainingMillis(function0);
    }

    static final boolean remainingMillis$lambda$0() {
        return false;
    }

    public final long remainingMillis(Function0<Boolean> keepAlive) {
        Intrinsics.checkNotNullParameter(keepAlive, "keepAlive");
        if (this.extendedForLiveSession) {
            if (keepAlive.invoke().booleanValue()) {
                return MAX_POLL_MILLIS;
            }
            return 0L;
        }
        if (this.deadlineNanos == Long.MAX_VALUE) {
            return MAX_POLL_MILLIS;
        }
        long remaining = this.deadlineNanos - this.nowNanos.invoke().longValue();
        if (remaining <= 0) {
            if (!keepAlive.invoke().booleanValue()) {
                return 0L;
            }
            this.extendedForLiveSession = true;
            return MAX_POLL_MILLIS;
        }
        return Math.min(MAX_POLL_MILLIS, ((remaining + 1000000) - 1) / 1000000);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ long requireRemaining$default(WirelessControlDeadline wirelessControlDeadline, Function0 function0, int i, Object obj) {
        if ((i & 1) != 0) {
            function0 = new Function0() { // from class: com.carplay.iap2.transport.WirelessControlDeadline$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return Boolean.valueOf(WirelessControlDeadline.requireRemaining$lambda$1());
                }
            };
        }
        return wirelessControlDeadline.requireRemaining(function0);
    }

    static final boolean requireRemaining$lambda$1() {
        return false;
    }

    public final long requireRemaining(Function0<Boolean> keepAlive) throws IphoneUsbException.TimedOut {
        Intrinsics.checkNotNullParameter(keepAlive, "keepAlive");
        long it = remainingMillis(keepAlive);
        if (it == 0) {
            throw new IphoneUsbException.TimedOut("Timed out during wireless iAP2 control bring-up", null, 2, null);
        }
        return it;
    }

    /* JADX INFO: compiled from: Iap2WirelessControlClient.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"Lcom/carplay/iap2/transport/WirelessControlDeadline$Companion;", "", "<init>", "()V", "NANOS_PER_MILLISECOND", "", "MAX_POLL_MILLIS", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
