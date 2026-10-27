package com.carplay.iap2.transport;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: RfcommConnectionStrategy.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u001e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\nJy\u0010\u000b\u001a\u0002H\f\"\u0004\b\u0000\u0010\f2\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\f\u0010\b\u001a\b\u0012\u0004\u0012\u0002H\f0\u000e2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\f0\u000e2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\u000e2\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u000f0\u00132\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u000f0\u0013¢\u0006\u0002\u0010\u0016JW\u0010\u0017\u001a\u0002H\f\"\u0004\b\u0000\u0010\f2\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u0002H\f0\u000e2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\f0\u000e2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\u000e2\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u000f0\u0013¢\u0006\u0002\u0010\u0018JO\u0010\u0017\u001a\u0002H\f\"\u0004\b\u0000\u0010\f2\f\u0010\b\u001a\b\u0012\u0004\u0012\u0002H\f0\u000e2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\f0\u000e2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\u000e2\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u000f0\u0013¢\u0006\u0002\u0010\u0019¨\u0006\u001a"}, d2 = {"Lcom/carplay/iap2/transport/RfcommConnectionStrategy;", "", "<init>", "()V", "shouldFallbackToInboundAfterSilentHandshake", "", "direction", "Lcom/carplay/iap2/transport/RfcommConnectionDirection;", "outbound", "receivedBytes", "", "connectWithPhoneService", "T", "register", "Lkotlin/Function0;", "", "inbound", "cancelled", "registrationFailure", "Lkotlin/Function1;", "Ljava/io/IOException;", "fallback", "(Lcom/carplay/iap2/transport/RfcommConnectionDirection;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "connect", "(Lcom/carplay/iap2/transport/RfcommConnectionDirection;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "(Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class RfcommConnectionStrategy {
    public static final RfcommConnectionStrategy INSTANCE = new RfcommConnectionStrategy();

    private RfcommConnectionStrategy() {
    }

    public final boolean shouldFallbackToInboundAfterSilentHandshake(RfcommConnectionDirection direction, boolean outbound, long receivedBytes) {
        Intrinsics.checkNotNullParameter(direction, "direction");
        return direction == RfcommConnectionDirection.PHONE_INITIATED && outbound && receivedBytes == 0;
    }

    public final <T> T connectWithPhoneService(RfcommConnectionDirection direction, Function0<Unit> register, Function0<? extends T> outbound, Function0<? extends T> inbound, Function0<Boolean> cancelled, Function1<? super IOException, Unit> registrationFailure, Function1<? super IOException, Unit> fallback) throws IOException {
        Intrinsics.checkNotNullParameter(direction, "direction");
        Intrinsics.checkNotNullParameter(register, "register");
        Intrinsics.checkNotNullParameter(outbound, "outbound");
        Intrinsics.checkNotNullParameter(inbound, "inbound");
        Intrinsics.checkNotNullParameter(cancelled, "cancelled");
        Intrinsics.checkNotNullParameter(registrationFailure, "registrationFailure");
        Intrinsics.checkNotNullParameter(fallback, "fallback");
        if (cancelled.invoke().booleanValue()) {
            throw new CancellationException("蓝牙连接已取消");
        }
        try {
            register.invoke();
        } catch (IOException e) {
            if (cancelled.invoke().booleanValue()) {
                CancellationException cancellationException = new CancellationException("蓝牙连接已取消");
                cancellationException.initCause(e);
                throw cancellationException;
            }
            if (direction == RfcommConnectionDirection.VEHICLE_INITIATED) {
                throw e;
            }
            registrationFailure.invoke(e);
        }
        if (cancelled.invoke().booleanValue()) {
            throw new CancellationException("蓝牙连接已取消");
        }
        return (T) connect(direction, outbound, inbound, cancelled, fallback);
    }

    public final <T> T connect(RfcommConnectionDirection direction, Function0<? extends T> outbound, Function0<? extends T> inbound, Function0<Boolean> cancelled, Function1<? super IOException, Unit> fallback) {
        Intrinsics.checkNotNullParameter(direction, "direction");
        Intrinsics.checkNotNullParameter(outbound, "outbound");
        Intrinsics.checkNotNullParameter(inbound, "inbound");
        Intrinsics.checkNotNullParameter(cancelled, "cancelled");
        Intrinsics.checkNotNullParameter(fallback, "fallback");
        if (direction == RfcommConnectionDirection.PHONE_INITIATED) {
            return (T) connect(outbound, inbound, cancelled, fallback);
        }
        if (cancelled.invoke().booleanValue()) {
            throw new CancellationException("蓝牙连接已取消");
        }
        return inbound.invoke();
    }

    public final <T> T connect(Function0<? extends T> outbound, Function0<? extends T> inbound, Function0<Boolean> cancelled, Function1<? super IOException, Unit> fallback) {
        Intrinsics.checkNotNullParameter(outbound, "outbound");
        Intrinsics.checkNotNullParameter(inbound, "inbound");
        Intrinsics.checkNotNullParameter(cancelled, "cancelled");
        Intrinsics.checkNotNullParameter(fallback, "fallback");
        if (cancelled.invoke().booleanValue()) {
            throw new CancellationException("蓝牙连接已取消");
        }
        try {
            return outbound.invoke();
        } catch (IOException failure) {
            if (cancelled.invoke().booleanValue()) {
                CancellationException $this$connect_u24lambda_u241 = new CancellationException("蓝牙连接已取消");
                $this$connect_u24lambda_u241.initCause(failure);
                throw $this$connect_u24lambda_u241;
            }
            fallback.invoke(failure);
            if (cancelled.invoke().booleanValue()) {
                throw new CancellationException("蓝牙连接已取消");
            }
            return inbound.invoke();
        }
    }
}
