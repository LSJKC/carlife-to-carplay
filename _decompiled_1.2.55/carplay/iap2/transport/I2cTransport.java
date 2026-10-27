package com.carplay.iap2.transport;

import kotlin.Metadata;

/* JADX INFO: compiled from: I2cTransport.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0005H&¨\u0006\bÀ\u0006\u0003"}, d2 = {"Lcom/carplay/iap2/transport/I2cTransport;", "", "transaction", "", "address7Bit", "", "writeData", "readLength", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface I2cTransport {
    byte[] transaction(int address7Bit, byte[] writeData, int readLength) throws I2cTransportException;
}
