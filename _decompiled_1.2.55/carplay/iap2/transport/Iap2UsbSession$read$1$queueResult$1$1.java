package com.carplay.iap2.transport;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;

/* JADX INFO: compiled from: IphoneUsbHost.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
final /* synthetic */ class Iap2UsbSession$read$1$queueResult$1$1 extends FunctionReferenceImpl implements Function0<Unit> {
    Iap2UsbSession$read$1$queueResult$1$1(Object obj) {
        super(0, obj, Iap2UsbSession.class, "checkOpenLocked", "checkOpenLocked()V", 0);
    }

    @Override // kotlin.jvm.functions.Function0
    public /* bridge */ /* synthetic */ Unit invoke() throws IphoneUsbException {
        invoke2();
        return Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
    public final void invoke2() throws IphoneUsbException {
        ((Iap2UsbSession) this.receiver).checkOpenLocked();
    }
}
