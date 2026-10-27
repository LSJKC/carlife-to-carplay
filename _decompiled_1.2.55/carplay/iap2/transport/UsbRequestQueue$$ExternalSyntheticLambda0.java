package com.carplay.iap2.transport;

import com.android.tools.r8.annotations.LambdaMethod;
import com.android.tools.r8.annotations.SynthesizedClassV2;
import kotlin.jvm.functions.Function0;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes9.dex */
@LambdaMethod(holder = "Lcom/carplay/iap2/transport/UsbRequestQueue;", method = "reaper$lambda$0", proto = "(Lcom/carplay/iap2/transport/UsbRequestQueue;)Landroid/hardware/usb/UsbRequest;")
@SynthesizedClassV2(apiLevel = -2, kind = 19, versionHash = "4b55be2c9864cfa0f3e2262a2208567ab6bc862a59e7853c580a1f24fbae9ba1")
public final /* synthetic */ class UsbRequestQueue$$ExternalSyntheticLambda0 implements Function0 {
    public final /* synthetic */ UsbRequestQueue f$0;

    @Override // kotlin.jvm.functions.Function0
    public final Object invoke() {
        return this.f$0.connection.requestWait();
    }
}
