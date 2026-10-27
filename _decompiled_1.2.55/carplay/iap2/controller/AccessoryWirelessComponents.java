package com.carplay.iap2.controller;

import com.carplay.iap2.body.Iap2BodyReader;
import com.carplay.iap2.wire.Iap2Parameter;
import com.carplay.iap2.wire.Iap2ParameterList;
import com.carplay.iap2.wire.Iap2ProtocolException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AccessoryWirelessComponents.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b¨\u0006\n"}, d2 = {"Lcom/carplay/iap2/controller/AccessoryWirelessComponents;", "", "<init>", "()V", "read", "", "Lcom/carplay/iap2/controller/AccessoryWirelessComponent;", "body", "Lcom/carplay/iap2/body/Iap2BodyReader;", "select", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AccessoryWirelessComponents {
    public static final AccessoryWirelessComponents INSTANCE = new AccessoryWirelessComponents();

    private AccessoryWirelessComponents() {
    }

    public final List<AccessoryWirelessComponent> read(Iap2BodyReader body) throws Iap2ProtocolException {
        Intrinsics.checkNotNullParameter(body, "body");
        Iterable $this$map$iv = body.all(24);
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (Object item$iv$iv : $this$map$iv) {
            Iap2Parameter parameter = (Iap2Parameter) item$iv$iv;
            Iap2BodyReader component = Iap2BodyReader.INSTANCE.of(Iap2ParameterList.INSTANCE.parse(parameter.getPayload()));
            if (component.all(0).size() != 1) {
                throw new Iap2ProtocolException("无线 CarPlay 组件编号缺失或重复", null, 2, null);
            }
            destination$iv$iv.add(new AccessoryWirelessComponent(component.u16(0), component.optionalVoid(2), component.optionalVoid(4)));
        }
        return (List) destination$iv$iv;
    }

    public final AccessoryWirelessComponent select(Iap2BodyReader body) throws Iap2ProtocolException {
        Intrinsics.checkNotNullParameter(body, "body");
        Iterable iterable = read(body);
        if (((Collection) iterable).isEmpty()) {
            throw new IllegalStateException("车机未提供无线 CarPlay 组件编号，无法构造网络请求".toString());
        }
        Iterable $this$filter$iv = iterable;
        Collection destination$iv$iv = new ArrayList();
        Iterator it = $this$filter$iv.iterator();
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            Object element$iv$iv = it.next();
            AccessoryWirelessComponent it2 = (AccessoryWirelessComponent) element$iv$iv;
            if (it2.getSupported() && it2.getAvailable()) {
                z = true;
            }
            if (z) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        List eligible = (List) destination$iv$iv;
        if (eligible.isEmpty()) {
            throw new IllegalStateException("车机没有声明可用的无线 CarPlay 组件，请检查车机无线 CarPlay 设置".toString());
        }
        if (eligible.size() == 1) {
            return (AccessoryWirelessComponent) CollectionsKt.single(eligible);
        }
        throw new IllegalStateException("车机声明多个可用无线 CarPlay 组件，无法自动确定请求目标".toString());
    }
}
