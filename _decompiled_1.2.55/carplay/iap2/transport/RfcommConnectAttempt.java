package com.carplay.iap2.transport;

import java.net.SocketTimeoutException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: RfcommConnectAttempt.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00060\u0001j\u0002`\u0002:\u0001\u0012B\u0015\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\u0004\b\u0006\u0010\u0007J2\u0010\f\u001a\u00020\u00052\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u001c\u0010\r\u001a\u0018\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\b\u0012\u00060\u0001j\u0002`\u00020\u000eJ\b\u0010\u000f\u001a\u00020\u0005H\u0002J\b\u0010\u0010\u001a\u00020\u0005H\u0002J\b\u0010\u0011\u001a\u00020\u0005H\u0016R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\b\u001a\u0010\u0012\f\u0012\n \u000b*\u0004\u0018\u00010\n0\n0\tX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lcom/carplay/iap2/transport/RfcommConnectAttempt;", "Ljava/lang/AutoCloseable;", "Lkotlin/AutoCloseable;", "abort", "Lkotlin/Function0;", "", "<init>", "(Lkotlin/jvm/functions/Function0;)V", "state", "Ljava/util/concurrent/atomic/AtomicReference;", "Lcom/carplay/iap2/transport/RfcommConnectAttempt$State;", "kotlin.jvm.PlatformType", "connect", "scheduleTimeout", "Lkotlin/Function1;", "ensurePending", "expire", "close", "State", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class RfcommConnectAttempt implements AutoCloseable {
    private final Function0<Unit> abort;
    private final AtomicReference<State> state;

    /* JADX INFO: compiled from: RfcommConnectAttempt.kt */
    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\b\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\b¨\u0006\t"}, d2 = {"Lcom/carplay/iap2/transport/RfcommConnectAttempt$State;", "", "<init>", "(Ljava/lang/String;I)V", "PENDING", "CONNECTED", "FAILED", "TIMED_OUT", "CANCELLED", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public enum State {
        PENDING,
        CONNECTED,
        FAILED,
        TIMED_OUT,
        CANCELLED;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

        public static EnumEntries<State> getEntries() {
            return $ENTRIES;
        }
    }

    /* JADX INFO: compiled from: RfcommConnectAttempt.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[State.values().length];
            try {
                iArr[State.PENDING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[State.TIMED_OUT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[State.CANCELLED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public RfcommConnectAttempt(Function0<Unit> abort) {
        Intrinsics.checkNotNullParameter(abort, "abort");
        this.abort = abort;
        this.state = new AtomicReference<>(State.PENDING);
    }

    public final void connect(Function0<Unit> connect, Function1<? super Function0<Unit>, ? extends AutoCloseable> scheduleTimeout) throws Exception {
        Intrinsics.checkNotNullParameter(connect, "connect");
        Intrinsics.checkNotNullParameter(scheduleTimeout, "scheduleTimeout");
        AutoCloseable deadline = null;
        try {
            try {
                ensurePending();
                deadline = scheduleTimeout.invoke(new AnonymousClass1(this));
                ensurePending();
                connect.invoke();
                if (!RfcommConnectAttempt$$ExternalSyntheticBackportWithForwarding0.m(this.state, State.PENDING, State.CONNECTED)) {
                    ensurePending();
                }
                if (deadline != null) {
                    RegisteredRfcommService$$ExternalSyntheticAutoCloseableDispatcher0.m(deadline);
                }
            } catch (Exception failure) {
                if (RfcommConnectAttempt$$ExternalSyntheticBackportWithForwarding0.m(this.state, State.PENDING, State.FAILED)) {
                    try {
                        this.abort.invoke();
                    } catch (Exception cleanup) {
                        ExceptionsKt.addSuppressed(failure, cleanup);
                    }
                }
                if (this.state.get() == State.TIMED_OUT && !(failure instanceof SocketTimeoutException)) {
                    SocketTimeoutException $this$connect_u24lambda_u240 = new SocketTimeoutException("主动 RFCOMM 连接超时");
                    $this$connect_u24lambda_u240.initCause(failure);
                    throw $this$connect_u24lambda_u240;
                }
                if (this.state.get() != State.CANCELLED || (failure instanceof CancellationException)) {
                    throw failure;
                }
                CancellationException $this$connect_u24lambda_u241 = new CancellationException("主动 RFCOMM 连接已取消");
                $this$connect_u24lambda_u241.initCause(failure);
                throw $this$connect_u24lambda_u241;
            }
        } catch (Throwable th) {
            if (deadline != null) {
                RegisteredRfcommService$$ExternalSyntheticAutoCloseableDispatcher0.m(deadline);
            }
            throw th;
        }
    }

    /* JADX INFO: renamed from: com.carplay.iap2.transport.RfcommConnectAttempt$connect$1, reason: invalid class name */
    /* JADX INFO: compiled from: RfcommConnectAttempt.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function0<Unit> {
        AnonymousClass1(Object obj) {
            super(0, obj, RfcommConnectAttempt.class, "expire", "expire()V", 0);
        }

        @Override // kotlin.jvm.functions.Function0
        public /* bridge */ /* synthetic */ Unit invoke() {
            invoke2();
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2() {
            ((RfcommConnectAttempt) this.receiver).expire();
        }
    }

    private final void ensurePending() throws SocketTimeoutException {
        State state = this.state.get();
        switch (state == null ? -1 : WhenMappings.$EnumSwitchMapping$0[state.ordinal()]) {
            case 1:
                return;
            case 2:
                throw new SocketTimeoutException("主动 RFCOMM 连接超时");
            case 3:
                throw new CancellationException("主动 RFCOMM 连接已取消");
            default:
                throw new IllegalStateException("RFCOMM 连接不能重复执行".toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void expire() {
        if (RfcommConnectAttempt$$ExternalSyntheticBackportWithForwarding0.m(this.state, State.PENDING, State.TIMED_OUT)) {
            this.abort.invoke();
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        if (RfcommConnectAttempt$$ExternalSyntheticBackportWithForwarding0.m(this.state, State.PENDING, State.CANCELLED)) {
            this.abort.invoke();
        }
    }
}
