package com.carplay.iap2.transport;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbRequest;
import java.io.Closeable;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeoutException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: UsbRequestQueue.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u0016\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000fJ\u0010\u0010\u0010\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0011\u001a\u00020\u0012J\b\u0010\u0013\u001a\u00020\u0014H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\tX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lcom/carplay/iap2/transport/UsbRequestQueue;", "Ljava/io/Closeable;", "connection", "Landroid/hardware/usb/UsbDeviceConnection;", "threadName", "", "<init>", "(Landroid/hardware/usb/UsbDeviceConnection;Ljava/lang/String;)V", "reaper", "Lcom/carplay/iap2/transport/UsbCompletionReaper;", "Landroid/hardware/usb/UsbRequest;", "queue", "", "request", "buffer", "Ljava/nio/ByteBuffer;", "await", "timeoutMillis", "", "close", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class UsbRequestQueue implements Closeable {
    private final UsbDeviceConnection connection;
    private final UsbCompletionReaper<UsbRequest> reaper;

    public UsbRequestQueue(UsbDeviceConnection connection, String threadName) {
        Intrinsics.checkNotNullParameter(connection, "connection");
        Intrinsics.checkNotNullParameter(threadName, "threadName");
        this.connection = connection;
        this.reaper = null;
    }

    public final boolean queue(UsbRequest request, ByteBuffer buffer) {
        Intrinsics.checkNotNullParameter(request, "request");
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        return request.queue(buffer);
    }

    public final UsbRequest await(long timeoutMillis) throws TimeoutException {
        return this.connection.requestWait(timeoutMillis);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        UsbCompletionReaper<UsbRequest> usbCompletionReaper = this.reaper;
        if (usbCompletionReaper != null) {
            usbCompletionReaper.close();
        }
    }
}
