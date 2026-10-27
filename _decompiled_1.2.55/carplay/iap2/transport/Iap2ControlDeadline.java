package com.carplay.iap2.transport;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: Iap2ControlDeadline.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0000\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u0006\u0010\u000b\u001a\u00020\fJ\u0006\u0010\r\u001a\u00020\u0003R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lcom/carplay/iap2/transport/Iap2ControlDeadline;", "", "timeoutMillis", "", "clockNanos", "Lkotlin/Function0;", "<init>", "(JLkotlin/jvm/functions/Function0;)V", "unlimited", "", "startedNanos", "authenticated", "", "remainingMillis", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2ControlDeadline {
    public static final long HANDSHAKE_MILLIS = 60000;
    public static final long MAX_POLL_MILLIS = 30000;
    private boolean authenticated;
    private final Function0<Long> clockNanos;
    private final long startedNanos;
    private final long timeoutMillis;
    private final boolean unlimited;

    public Iap2ControlDeadline(long timeoutMillis, Function0<Long> clockNanos) {
        Intrinsics.checkNotNullParameter(clockNanos, "clockNanos");
        this.timeoutMillis = timeoutMillis;
        this.clockNanos = clockNanos;
        this.unlimited = this.timeoutMillis == Long.MAX_VALUE;
        this.startedNanos = this.clockNanos.invoke().longValue();
    }

    public /* synthetic */ Iap2ControlDeadline(long j, AnonymousClass1 anonymousClass1, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(j, (i & 2) != 0 ? AnonymousClass1.INSTANCE : anonymousClass1);
    }

    /* JADX INFO: renamed from: com.carplay.iap2.transport.Iap2ControlDeadline$1, reason: invalid class name */
    /* JADX INFO: compiled from: Iap2ControlDeadline.kt */
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

    public final void authenticated() {
        this.authenticated = true;
    }

    public final long remainingMillis() {
        if (this.unlimited && this.authenticated) {
            return 30000L;
        }
        long budget = this.unlimited ? HANDSHAKE_MILLIS : this.timeoutMillis;
        long elapsedNanos = RangesKt.coerceAtLeast(this.clockNanos.invoke().longValue() - this.startedNanos, 0L);
        long remainingNanos = (budget * 1000000) - elapsedNanos;
        if (remainingNanos <= 0) {
            return 0L;
        }
        return RangesKt.coerceAtMost((999999 + remainingNanos) / 1000000, 30000L);
    }
}
