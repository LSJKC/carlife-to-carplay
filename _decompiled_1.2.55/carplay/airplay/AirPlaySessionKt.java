package com.carplay.airplay;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AirPlaySession.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000D\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\u001a$\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u0003H\u0000\u001a\u001a\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00032\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0000\u001a\u0012\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0000\u001a\f\u0010\u000f\u001a\u00020\u0001*\u00020\u0010H\u0002\u001a\"\u0010\u0011\u001a\u0012\u0012\u0004\u0012\u00020\u0001\u0012\u0006\u0012\u0004\u0018\u00010\n\u0018\u00010\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\nH\u0002\u001a\u0012\u0010\u0014\u001a\u00020\u00012\b\u0010\u0013\u001a\u0004\u0018\u00010\nH\u0002\u001a\u0019\u0010\u0015\u001a\u0004\u0018\u00010\u00162\b\u0010\u0013\u001a\u0004\u0018\u00010\nH\u0002¢\u0006\u0002\u0010\u0017\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"MAIN_BUFFERED_FEATURE", "", "setupEnabledFeatures", "", "config", "Lcom/carplay/airplay/AirPlayConfig;", "proposed", "teardownStreamTypes", "", "body", "", "safeClose", "", "closeable", "Ljava/io/Closeable;", "toHex", "", "asMap", "", "value", "string", "long", "", "(Ljava/lang/Object;)Ljava/lang/Long;", "core-airplay"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class AirPlaySessionKt {
    public static final String MAIN_BUFFERED_FEATURE = "mainBuffered";

    public static final List<String> setupEnabledFeatures(AirPlayConfig config, List<?> list) {
        Intrinsics.checkNotNullParameter(config, "config");
        List features = new ArrayList();
        if (config.getHevc()) {
            features.add("hevc");
        }
        features.add("iAPChannel");
        features.add("viewAreas");
        if (config.getCluster() != null) {
            features.add("altScreen");
        }
        if (config.getVideoInCar()) {
            if ((list == null ? CollectionsKt.emptyList() : list).contains(VideoInCar.FEATURE)) {
                features.add(VideoInCar.FEATURE);
            }
        }
        if (AirPlayConfigKt.getBufferedAudioOutputEnabled(config)) {
            if ((list == null ? CollectionsKt.emptyList() : list).contains(MAIN_BUFFERED_FEATURE)) {
                features.add(MAIN_BUFFERED_FEATURE);
            }
        }
        return features;
    }

    public static final List<Integer> teardownStreamTypes(Object body) {
        Pair pair;
        Map map = body instanceof Map ? (Map) body : null;
        Object obj = map != null ? map.get("streams") : null;
        Iterable iterableEmptyList = obj instanceof List ? (List) obj : null;
        if (iterableEmptyList == null) {
            iterableEmptyList = CollectionsKt.emptyList();
        }
        Iterable $this$mapNotNull$iv = iterableEmptyList;
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv$iv : $this$mapNotNull$iv) {
            Map stream = element$iv$iv$iv instanceof Map ? (Map) element$iv$iv$iv : null;
            if (stream == null) {
                pair = null;
            } else {
                Object obj2 = stream.get("type");
                Number number = obj2 instanceof Number ? (Number) obj2 : null;
                if (number != null) {
                    int type = number.intValue();
                    Integer numValueOf = Integer.valueOf(type);
                    Object obj3 = stream.get("streamID");
                    Number number2 = obj3 instanceof Number ? (Number) obj3 : null;
                    pair = TuplesKt.to(numValueOf, number2 != null ? Long.valueOf(number2.longValue()) : null);
                } else {
                    pair = null;
                }
            }
            if (pair != null) {
                destination$iv$iv.add(pair);
            }
            $this$mapNotNull$iv = $this$mapNotNull$iv;
        }
        List entries = (List) destination$iv$iv;
        if (entries.isEmpty()) {
            return null;
        }
        List $this$filterNot$iv = entries;
        Collection destination$iv$iv2 = new ArrayList();
        for (Object element$iv$iv : $this$filterNot$iv) {
            Pair pair2 = (Pair) element$iv$iv;
            int type2 = ((Number) pair2.component1()).intValue();
            Long streamId = (Long) pair2.component2();
            if (!((type2 != 130 || streamId == null || streamId.longValue() == 1) ? false : true)) {
                destination$iv$iv2.add(element$iv$iv);
            }
        }
        Iterable $this$map$iv = (List) destination$iv$iv2;
        Collection destination$iv$iv3 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (Object item$iv$iv : $this$map$iv) {
            Pair it = (Pair) item$iv$iv;
            destination$iv$iv3.add(Integer.valueOf(((Number) it.getFirst()).intValue()));
        }
        return (List) destination$iv$iv3;
    }

    public static final void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final String toHex(byte[] $this$toHex) {
        return ArraysKt.joinToString$default($this$toHex, (CharSequence) "", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, new Function1() { // from class: com.carplay.airplay.AirPlaySessionKt$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AirPlaySessionKt.toHex$lambda$3(((Byte) obj).byteValue());
            }
        }, 30, (Object) null);
    }

    static final CharSequence toHex$lambda$3(byte b) {
        String str = String.format("%02x", Arrays.copyOf(new Object[]{Integer.valueOf(b & UByte.MAX_VALUE)}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Map<String, Object> asMap(Object value) {
        Map map = value instanceof Map ? (Map) value : null;
        if (map == null) {
            return null;
        }
        LinkedHashMap result = new LinkedHashMap(map.size());
        for (Map.Entry entry : map.entrySet()) {
            Object key = entry.getKey();
            Object entry2 = entry.getValue();
            result.put(String.valueOf(key), entry2);
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final String string(Object value) {
        String str = value instanceof String ? (String) value : null;
        return str == null ? "" : str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: long, reason: not valid java name */
    public static final Long m49long(Object value) {
        Number number = value instanceof Number ? (Number) value : null;
        if (number != null) {
            return Long.valueOf(number.longValue());
        }
        return null;
    }
}
