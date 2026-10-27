package com.carlink.cast;

import com.carplay.sender.AirPlaySender;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;

/* JADX INFO: compiled from: AirPlayLink.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.carlink.cast.AirPlayLink$events$1$startDisplay$1", f = "AirPlayLink.kt", i = {}, l = {372}, m = "invokeSuspend", n = {}, s = {})
final class AirPlayLink$events$1$startDisplay$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ AirPlaySender $current;
    int label;
    final /* synthetic */ AirPlayLink this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    AirPlayLink$events$1$startDisplay$1(AirPlayLink airPlayLink, AirPlaySender airPlaySender, Continuation<? super AirPlayLink$events$1$startDisplay$1> continuation) {
        super(2, continuation);
        this.this$0 = airPlayLink;
        this.$current = airPlaySender;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AirPlayLink$events$1$startDisplay$1(this.this$0, this.$current, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AirPlayLink$events$1$startDisplay$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX INFO: renamed from: com.carlink.cast.AirPlayLink$events$1$startDisplay$1$1, reason: invalid class name */
    /* JADX INFO: compiled from: AirPlayLink.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function3<byte[], Long, Boolean, Unit> {
        AnonymousClass1(Object obj) {
            super(3, obj, AirPlaySender.class, "frame", "frame([BJZ)V", 0);
        }

        @Override // kotlin.jvm.functions.Function3
        public /* bridge */ /* synthetic */ Unit invoke(byte[] bArr, Long l, Boolean bool) {
            invoke(bArr, l.longValue(), bool.booleanValue());
            return Unit.INSTANCE;
        }

        public final void invoke(byte[] p0, long p1, boolean p2) {
            Intrinsics.checkNotNullParameter(p0, "p0");
            ((AirPlaySender) this.receiver).frame(p0, p1, p2);
        }
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) throws Throwable {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                this.label = 1;
                if (this.this$0.source.startProjection(new AnonymousClass1(this.$current), this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Unit.INSTANCE;
    }
}
