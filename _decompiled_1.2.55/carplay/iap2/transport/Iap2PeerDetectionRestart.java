package com.carplay.iap2.transport;

import java.io.IOException;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PhoneBluetoothSessionTransport.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0019\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u0006\u0010\u0004\u001a\u00020\u0005R\u000e\u0010\b\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lcom/carplay/iap2/transport/Iap2PeerDetectionRestart;", "Ljava/io/IOException;", "reason", "", "initialInput", "", "<init>", "(Ljava/lang/String;[B)V", "pending", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2PeerDetectionRestart extends IOException {
    private final byte[] pending;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Iap2PeerDetectionRestart(String reason, byte[] initialInput) {
        super(reason);
        Intrinsics.checkNotNullParameter(reason, "reason");
        Intrinsics.checkNotNullParameter(initialInput, "initialInput");
        byte[] bArrCopyOf = Arrays.copyOf(initialInput, initialInput.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        this.pending = bArrCopyOf;
    }

    public final byte[] initialInput() {
        byte[] bArr = this.pending;
        byte[] bArrCopyOf = Arrays.copyOf(bArr, bArr.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        return bArrCopyOf;
    }
}
