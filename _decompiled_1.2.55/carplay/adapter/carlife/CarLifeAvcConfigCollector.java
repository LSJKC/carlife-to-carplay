package com.carplay.adapter.carlife;

import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CarLifeAvcConfigCollector.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0000\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0006\u0010\u0007\u001a\u00020\bJ\u0010\u0010\t\u001a\u0004\u0018\u00010\u00052\u0006\u0010\n\u001a\u00020\u0005R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0005X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeAvcConfigCollector;", "", "<init>", "()V", "sps", "", "pps", "reset", "", "offer", "accessUnit", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarLifeAvcConfigCollector {
    private byte[] pps;
    private byte[] sps;

    public final synchronized void reset() {
        this.sps = null;
        this.pps = null;
    }

    public final synchronized byte[] offer(byte[] accessUnit) {
        Intrinsics.checkNotNullParameter(accessUnit, "accessUnit");
        Pair<byte[], byte[]> pairAvcParameterSets = CarLifeProto.INSTANCE.avcParameterSets(accessUnit);
        byte[] newSps = pairAvcParameterSets.component1();
        byte[] newPps = pairAvcParameterSets.component2();
        if (newSps != null) {
            this.sps = newSps;
        }
        if (newPps != null) {
            this.pps = newPps;
        }
        return CarLifeProto.INSTANCE.avcConfig(this.sps, this.pps);
    }
}
