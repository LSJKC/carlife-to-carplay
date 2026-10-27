package com.carplay.iap2.transport;

import java.io.InputStream;
import java.io.OutputStream;
import kotlin.Metadata;

/* JADX INFO: compiled from: BluetoothRfcommDuplexStream.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\b`\u0018\u00002\u00020\u0001J\n\u0010\u0002\u001a\u0004\u0018\u00010\u0003H&J\n\u0010\u0004\u001a\u0004\u0018\u00010\u0005H&J\b\u0010\u0006\u001a\u00020\u0007H&¨\u0006\bÀ\u0006\u0003"}, d2 = {"Lcom/carplay/iap2/transport/BluetoothRfcommSocketAccess;", "", "inputStream", "Ljava/io/InputStream;", "outputStream", "Ljava/io/OutputStream;", "close", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface BluetoothRfcommSocketAccess {
    void close();

    InputStream inputStream();

    OutputStream outputStream();
}
