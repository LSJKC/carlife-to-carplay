package com.carplay.adapter.carlife;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CarLifeProtocol.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0007\b\u0000\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\f"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeFrame;", "", "serviceType", "", "payload", "", "<init>", "(I[B)V", "getServiceType", "()I", "getPayload", "()[B", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarLifeFrame {
    private final byte[] payload;
    private final int serviceType;

    public CarLifeFrame(int serviceType, byte[] payload) {
        Intrinsics.checkNotNullParameter(payload, "payload");
        this.serviceType = serviceType;
        this.payload = payload;
    }

    public final byte[] getPayload() {
        return this.payload;
    }

    public final int getServiceType() {
        return this.serviceType;
    }
}
