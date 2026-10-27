package com.carlink.cast;

import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;

/* JADX INFO: compiled from: BluetoothBootstrap.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
final /* synthetic */ class BluetoothBootstrap$negotiate$connected$4 extends FunctionReferenceImpl implements Function0<Boolean> {
    BluetoothBootstrap$negotiate$connected$4(Object obj) {
        super(0, obj, AtomicBoolean.class, "get", "get()Z", 0);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    public final Boolean invoke() {
        return Boolean.valueOf(((AtomicBoolean) this.receiver).get());
    }
}
