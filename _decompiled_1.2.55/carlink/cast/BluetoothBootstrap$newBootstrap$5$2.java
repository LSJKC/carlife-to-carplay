package com.carlink.cast;

import com.carplay.iap2.session.Iap2Session;
import com.carplay.iap2.wire.Iap2Frame;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.AdaptedFunctionReference;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: BluetoothBootstrap.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
final /* synthetic */ class BluetoothBootstrap$newBootstrap$5$2 extends AdaptedFunctionReference implements Function1<Iap2Frame, Unit> {
    BluetoothBootstrap$newBootstrap$5$2(Object obj) {
        super(1, obj, Iap2Session.class, "send", "send(Lcom/carplay/iap2/wire/Iap2Frame;J)V", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ Unit invoke(Iap2Frame iap2Frame) {
        invoke2(iap2Frame);
        return Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
    public final void invoke2(Iap2Frame p0) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        Iap2Session.send$default((Iap2Session) this.receiver, p0, 0L, 2, null);
    }
}
