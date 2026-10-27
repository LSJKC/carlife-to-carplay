package com.carplay.iap2.transport;

import android.content.res.TypedArray;
import java.util.concurrent.ExecutorService;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes9.dex */
public final /* synthetic */ class RegisteredRfcommService$$ExternalSyntheticAutoCloseableDispatcher0 {
    public static /* synthetic */ void m(Object obj) throws Exception {
        if (obj instanceof AutoCloseable) {
            ((AutoCloseable) obj).close();
            return;
        }
        if (obj instanceof ExecutorService) {
            RegisteredRfcommService$$ExternalSyntheticAutoCloseableForwarder1.m((ExecutorService) obj);
        } else if (obj instanceof TypedArray) {
            ((TypedArray) obj).recycle();
        } else {
            RegisteredRfcommService$$ExternalSyntheticThrowIAE2.m(obj);
        }
    }
}
