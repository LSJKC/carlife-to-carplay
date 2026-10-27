package com.carplay.iap2.transport;

import androidx.core.app.NotificationCompat;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: PhoneBluetoothHandshakeDeadline.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0004\b\u0007\u0010\bJ\u000e\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lcom/carplay/iap2/transport/PhoneBluetoothHandshakeDeadline;", "", "profile", "Lcom/carplay/iap2/transport/PhoneBluetoothProfile;", "clockNanos", "Lkotlin/Function0;", "", "<init>", "(Lcom/carplay/iap2/transport/PhoneBluetoothProfile;Lkotlin/jvm/functions/Function0;)V", "start", "remainingMillis", NotificationCompat.CATEGORY_STATUS, "Lcom/carplay/iap2/transport/Iap2HandshakeStatus;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class PhoneBluetoothHandshakeDeadline {
    private final Function0<Long> clockNanos;
    private final PhoneBluetoothProfile profile;
    private final long start;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: com.carplay.iap2.transport.PhoneBluetoothHandshakeDeadline$1, reason: invalid class name */
    /* JADX INFO: compiled from: PhoneBluetoothHandshakeDeadline.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function0<Long> {
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

    public PhoneBluetoothHandshakeDeadline(PhoneBluetoothProfile profile, Function0<Long> clockNanos) {
        Intrinsics.checkNotNullParameter(profile, "profile");
        Intrinsics.checkNotNullParameter(clockNanos, "clockNanos");
        this.profile = profile;
        this.clockNanos = clockNanos;
        this.start = this.clockNanos.invoke().longValue();
    }

    public /* synthetic */ PhoneBluetoothHandshakeDeadline(PhoneBluetoothProfile phoneBluetoothProfile, AnonymousClass1 anonymousClass1, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(phoneBluetoothProfile, (i & 2) != 0 ? AnonymousClass1.INSTANCE : anonymousClass1);
    }

    public final long remainingMillis(Iap2HandshakeStatus status) {
        Intrinsics.checkNotNullParameter(status, "status");
        boolean detecting = status.getState() == Iap2LinkEngine.State.IDLE || status.getState() == Iap2LinkEngine.State.DETECTING;
        long budget = (this.profile.getAccessoryInitiates() && detecting) ? 12000L : this.profile.getReadyTimeoutMillis();
        long elapsed = RangesKt.coerceAtLeast(this.clockNanos.invoke().longValue() - this.start, 0L) / 1000000;
        return RangesKt.coerceAtLeast(budget - elapsed, 0L);
    }
}
