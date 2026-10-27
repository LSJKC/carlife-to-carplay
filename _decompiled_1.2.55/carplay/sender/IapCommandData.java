package com.carplay.sender;

import java.net.ProtocolException;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: SenderNegotiation.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00012\b\b\u0002\u0010\u0007\u001a\u00020\b¨\u0006\t"}, d2 = {"Lcom/carplay/sender/IapCommandData;", "", "<init>", "()V", "payload", "", "value", "response", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class IapCommandData {
    public static final IapCommandData INSTANCE = new IapCommandData();

    private IapCommandData() {
    }

    public static /* synthetic */ byte[] payload$default(IapCommandData iapCommandData, Object obj, boolean z, int i, Object obj2) {
        if ((i & 2) != 0) {
            z = false;
        }
        return iapCommandData.payload(obj, z);
    }

    public final byte[] payload(Object value, boolean response) throws ProtocolException {
        Object raw;
        if (value instanceof byte[]) {
            raw = response ? (byte[]) value : null;
        } else if (value instanceof Map) {
            if (((Map) value).get("type") != null && !Intrinsics.areEqual(((Map) value).get("type"), "iAPSendMessage")) {
                return null;
            }
            Object params = ((Map) value).get("params");
            if (params != null && !(params instanceof Map)) {
                throw new ProtocolException("iAP params 类型无效");
            }
            Map map = params instanceof Map ? (Map) params : null;
            if (map != null && (obj = map.get("data")) != null) {
                raw = obj;
            } else if (response) {
                Object obj = ((Map) value).get("data");
                raw = obj;
            } else {
                raw = null;
            }
        } else {
            raw = null;
        }
        if (raw == null) {
            return null;
        }
        byte[] data = raw instanceof byte[] ? (byte[]) raw : null;
        if (data == null) {
            throw new ProtocolException("iAP 控制数据不是二进制字段");
        }
        if (data.length == 0) {
            return null;
        }
        if (data.length > 1048576) {
            throw new ProtocolException("iAP 控制数据超出接收上限");
        }
        return data;
    }
}
