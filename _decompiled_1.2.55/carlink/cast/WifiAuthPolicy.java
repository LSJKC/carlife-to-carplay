package com.carlink.cast;

import kotlin.Metadata;

/* JADX INFO: compiled from: VehicleNetwork.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0016\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t¨\u0006\n"}, d2 = {"Lcom/carlink/cast/WifiAuthPolicy;", "", "<init>", "()V", "select", "Lcom/carlink/cast/WifiAuth;", "securityType", "", "supportsSae", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class WifiAuthPolicy {
    public static final WifiAuthPolicy INSTANCE = new WifiAuthPolicy();

    private WifiAuthPolicy() {
    }

    public final WifiAuth select(int securityType, boolean supportsSae) {
        switch (securityType) {
            case 0:
                return WifiAuth.OPEN;
            case 1:
            default:
                throw new IllegalStateException(("车机提供了不支持的无线安全类型：" + securityType).toString());
            case 2:
                return WifiAuth.WPA2;
            case 3:
                if (supportsSae) {
                    return WifiAuth.WPA3;
                }
                throw new IllegalStateException("车机要求 WPA3-SAE，当前手机不支持".toString());
            case 4:
                return supportsSae ? WifiAuth.WPA3 : WifiAuth.WPA2;
        }
    }
}
