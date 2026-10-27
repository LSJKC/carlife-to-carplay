package com.carlink.cast;

import android.content.res.TypedArray;
import java.util.concurrent.ExecutorService;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes15.dex */
public final /* synthetic */ class BluetoothBootstrap$$ExternalSyntheticAutoCloseableDispatcher0 {
    public static /* synthetic */ void m(Object obj) throws Exception {
        if (obj instanceof AutoCloseable) {
            ((AutoCloseable) obj).close();
            return;
        }
        if (obj instanceof ExecutorService) {
            BluetoothBootstrap$$ExternalSyntheticAutoCloseableForwarder1.m((ExecutorService) obj);
        } else if (obj instanceof TypedArray) {
            ((TypedArray) obj).recycle();
        } else {
            BluetoothBootstrap$$ExternalSyntheticThrowIAE2.m(obj);
        }
    }
}
