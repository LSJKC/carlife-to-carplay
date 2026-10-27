package com.carplay.iap2.controller;

import com.carplay.iap2.body.Iap2BodyReader;
import com.carplay.iap2.wire.Iap2Parameter;
import com.carplay.iap2.wire.Iap2ParameterList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: IdentificationDiagnostics.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\"\n\u0000\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\nH\u0002R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\fX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lcom/carplay/iap2/controller/IdentificationDiagnostics;", "", "<init>", "()V", "describe", "", "body", "Lcom/carplay/iap2/body/Iap2BodyReader;", "number", "id", "", "TRANSPORT_PARAMETERS", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class IdentificationDiagnostics {
    public static final IdentificationDiagnostics INSTANCE = new IdentificationDiagnostics();
    private static final Set<Integer> TRANSPORT_PARAMETERS = SetsKt.setOf((Object[]) new Integer[]{16, 17, 24});

    private IdentificationDiagnostics() {
    }

    public final String describe(Iap2BodyReader body) {
        Intrinsics.checkNotNullParameter(body, "body");
        List<Iap2Parameter> list = body.list();
        String shape = CollectionsKt.joinToString$default(CollectionsKt.take(list, 48), ",", null, null, 0, null, new Function1() { // from class: com.carplay.iap2.controller.IdentificationDiagnostics$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return IdentificationDiagnostics.describe$lambda$0((Iap2Parameter) obj);
            }
        }, 30, null) + (list.size() > 48 ? ",… total=" + list.size() : "");
        List<Iap2Parameter> $this$filter$iv = list;
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv : $this$filter$iv) {
            Iap2Parameter it = (Iap2Parameter) element$iv$iv;
            if (TRANSPORT_PARAMETERS.contains(Integer.valueOf(it.getId()))) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        List transports = (List) destination$iv$iv;
        String components = CollectionsKt.joinToString$default(CollectionsKt.take(transports, 8), ";", null, null, 0, null, new Function1() { // from class: com.carplay.iap2.controller.IdentificationDiagnostics$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return IdentificationDiagnostics.describe$lambda$3((Iap2Parameter) obj);
            }
        }, 30, null) + (transports.size() > 8 ? ";… total=" + transports.size() : "");
        String str = components;
        if (str.length() == 0) {
            str = "none-advertised";
        }
        return "params=" + shape + " transports=" + ((Object) str);
    }

    static final CharSequence describe$lambda$0(Iap2Parameter it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getId() + ":" + it.getPayload().length + "B";
    }

    static final CharSequence describe$lambda$3(Iap2Parameter parameter) {
        String transport;
        Intrinsics.checkNotNullParameter(parameter, "parameter");
        try {
            Iap2BodyReader group = Iap2BodyReader.INSTANCE.of(Iap2ParameterList.INSTANCE.parse(parameter.getPayload()));
            String component = INSTANCE.number(group, 0);
            if (parameter.getId() == 24) {
                transport = ",transportId=" + INSTANCE.number(group, 3);
            } else {
                transport = "";
            }
            String fields = CollectionsKt.joinToString$default(CollectionsKt.take(group.list(), 12), ",", null, null, 0, null, new Function1() { // from class: com.carplay.iap2.controller.IdentificationDiagnostics$$ExternalSyntheticLambda2
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return IdentificationDiagnostics.describe$lambda$3$lambda$2((Iap2Parameter) obj);
                }
            }, 30, null);
            return "p" + parameter.getId() + "(component=" + component + transport + ",fields=" + fields + ")";
        } catch (Exception e) {
            return "p" + parameter.getId() + "(malformed,size=" + parameter.getPayload().length + "B)";
        }
    }

    static final CharSequence describe$lambda$3$lambda$2(Iap2Parameter it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getId() + ":" + it.getPayload().length + "B";
    }

    private final String number(Iap2BodyReader body, int id) {
        byte[] raw = body.optionalRaw(id);
        if (raw == null) {
            return "missing";
        }
        if (raw.length == 2) {
            return String.valueOf(body.u16(id));
        }
        return "size=" + raw.length + "B";
    }
}
