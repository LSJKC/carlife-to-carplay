package com.carplay.iap2.transport;

import java.util.Collection;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Ch341DeviceMatcher.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bR\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lcom/carplay/iap2/transport/Ch341DeviceMatcher;", "", "allowedDevices", "", "Lcom/carplay/iap2/transport/UsbDeviceId;", "<init>", "(Ljava/util/Collection;)V", "", "matches", "", "vendorId", "", "productId", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Ch341DeviceMatcher {
    private final Set<UsbDeviceId> allowedDevices;

    public Ch341DeviceMatcher(Collection<UsbDeviceId> allowedDevices) {
        Intrinsics.checkNotNullParameter(allowedDevices, "allowedDevices");
        this.allowedDevices = CollectionsKt.toSet(allowedDevices);
    }

    public final boolean matches(int vendorId, int productId) {
        return this.allowedDevices.contains(new UsbDeviceId(vendorId, productId));
    }
}
