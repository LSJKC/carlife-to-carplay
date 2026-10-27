package com.carlink.cast;

import com.carplay.iap2.session.Iap2Session;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.AdaptedFunctionReference;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: BluetoothBootstrap.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
final /* synthetic */ class BluetoothBootstrap$newBootstrap$5$4 extends AdaptedFunctionReference implements Function1<byte[], Boolean> {
    BluetoothBootstrap$newBootstrap$5$4(Object obj) {
        super(1, obj, Iap2Session.class, "sendFileTransfer", "sendFileTransfer([BJ)Z", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Boolean invoke(byte[] p0) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        return Boolean.valueOf(Iap2Session.sendFileTransfer$default((Iap2Session) this.receiver, p0, 0L, 2, null));
    }
}
