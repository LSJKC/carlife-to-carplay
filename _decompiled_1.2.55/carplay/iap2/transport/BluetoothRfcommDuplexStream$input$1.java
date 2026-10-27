package com.carplay.iap2.transport;

import java.io.InputStream;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;

/* JADX INFO: compiled from: BluetoothRfcommDuplexStream.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
final /* synthetic */ class BluetoothRfcommDuplexStream$input$1 extends FunctionReferenceImpl implements Function0<InputStream> {
    BluetoothRfcommDuplexStream$input$1(Object obj) {
        super(0, obj, BluetoothRfcommSocketAccess.class, "inputStream", "inputStream()Ljava/io/InputStream;", 0);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    public final InputStream invoke() {
        return ((BluetoothRfcommSocketAccess) this.receiver).inputStream();
    }
}
