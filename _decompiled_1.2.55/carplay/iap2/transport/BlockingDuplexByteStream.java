package com.carplay.iap2.transport;

import java.io.Closeable;
import kotlin.Metadata;

/* JADX INFO: compiled from: BlockingDuplexByteStream.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u001a\u0010\u0006\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\u0003H&¨\u0006\fÀ\u0006\u0003"}, d2 = {"Lcom/carplay/iap2/transport/BlockingDuplexByteStream;", "Ljava/io/Closeable;", "send", "", "data", "", "recv", "maxBytes", "", "timeoutMillis", "", "close", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface BlockingDuplexByteStream extends Closeable {
    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close();

    byte[] recv(int maxBytes, long timeoutMillis);

    void send(byte[] data);
}
