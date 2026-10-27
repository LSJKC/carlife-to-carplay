package com.carplay.adapter.carlife;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CarLifeProtocol.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\t\n\u0002\b\t\b\u0000\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0004\b\b\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u0010"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeStreamPacket;", "", "serviceType", "", "data", "", "timestampSeconds", "", "<init>", "(I[BJ)V", "getServiceType", "()I", "getData", "()[B", "getTimestampSeconds", "()J", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarLifeStreamPacket {
    private final byte[] data;
    private final int serviceType;
    private final long timestampSeconds;

    public CarLifeStreamPacket(int serviceType, byte[] data, long timestampSeconds) {
        Intrinsics.checkNotNullParameter(data, "data");
        this.serviceType = serviceType;
        this.data = data;
        this.timestampSeconds = timestampSeconds;
    }

    public final byte[] getData() {
        return this.data;
    }

    public final int getServiceType() {
        return this.serviceType;
    }

    public final long getTimestampSeconds() {
        return this.timestampSeconds;
    }
}
