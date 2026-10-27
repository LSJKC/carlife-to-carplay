package com.carlink.cast;

import com.carplay.sender.SenderDisplay;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* JADX INFO: compiled from: AirPlayLink.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.carlink.cast.AirPlayLink$events$1$display$1", f = "AirPlayLink.kt", i = {}, l = {363}, m = "invokeSuspend", n = {}, s = {})
final class AirPlayLink$events$1$display$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ SenderDisplay $display;
    final /* synthetic */ int $requested;
    int label;
    final /* synthetic */ AirPlayLink this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    AirPlayLink$events$1$display$1(AirPlayLink airPlayLink, SenderDisplay senderDisplay, int i, Continuation<? super AirPlayLink$events$1$display$1> continuation) {
        super(2, continuation);
        this.this$0 = airPlayLink;
        this.$display = senderDisplay;
        this.$requested = i;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AirPlayLink$events$1$display$1(this.this$0, this.$display, this.$requested, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AirPlayLink$events$1$display$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) throws Throwable {
        Object objPrepare;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                this.label = 1;
                objPrepare = this.this$0.source.prepare(this.this$0.context, this.$display.getWidth(), this.$display.getHeight(), this.$requested, this);
                if (objPrepare == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                ResultKt.throwOnFailure($result);
                objPrepare = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (((Boolean) objPrepare).booleanValue()) {
            return Unit.INSTANCE;
        }
        throw new IllegalStateException("画面来源未准备就绪".toString());
    }
}
