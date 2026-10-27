package com.carplay.sender;

import com.carlink.cast.CastSettings;
import com.carplay.airplay.AirPlaySession;
import com.carplay.airplay.AudioCodecKind;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: SenderNegotiation.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\b\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0016\u0010\u0004\u001a\u00020\u00052\u000e\u0010\u0006\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0007J \u0010\b\u001a\u00020\t2\u000e\u0010\n\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u00072\b\b\u0002\u0010\u000b\u001a\u00020\fJ&\u0010\r\u001a\b\u0012\u0004\u0012\u00020\t0\u000e2\u000e\u0010\n\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u00072\b\b\u0002\u0010\u000b\u001a\u00020\fR\u000e\u0010\u000f\u001a\u00020\u0010X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lcom/carplay/sender/SenderNegotiation;", "", "<init>", "()V", "control", "Lcom/carplay/sender/SenderControlTransport;", "reply", "", CastSettings.KEY_AUDIO, "Lcom/carplay/sender/SenderAudioFormat;", "info", "preferAacMedia", "", "audioCandidates", "", "PCM_SAMPLES_PER_PACKET", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class SenderNegotiation {
    public static final SenderNegotiation INSTANCE = new SenderNegotiation();
    public static final int PCM_SAMPLES_PER_PACKET = 352;

    private SenderNegotiation() {
    }

    public final SenderControlTransport control(Map<?, ?> reply) throws ProtocolException {
        Intrinsics.checkNotNullParameter(reply, "reply");
        Object raw = reply.get("streams");
        if (raw == null) {
            return new SenderControlTransport(SenderControlKind.AUTHENTICATED_BLUETOOTH, null, 2, null);
        }
        Iterable streams = raw instanceof List ? (List) raw : null;
        if (streams == null) {
            throw new ProtocolException("type 130 回复的 streams 类型无效");
        }
        Iterable $this$map$iv = streams;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (Object item$iv$iv : $this$map$iv) {
            Map map = item$iv$iv instanceof Map ? (Map) item$iv$iv : null;
            if (map == null) {
                throw new ProtocolException("type 130 回复的流结构无效");
            }
            destination$iv$iv.add(map);
        }
        Iterable $this$filter$iv = (List) destination$iv$iv;
        Collection destination$iv$iv2 = new ArrayList();
        for (Object element$iv$iv : $this$filter$iv) {
            Map it = (Map) element$iv$iv;
            Object obj = it.get("type");
            Number number = obj instanceof Number ? (Number) obj : null;
            if (number != null && number.longValue() == 130) {
                destination$iv$iv2.add(element$iv$iv);
            }
        }
        List accepted = (List) destination$iv$iv2;
        if (accepted.isEmpty()) {
            return new SenderControlTransport(SenderControlKind.AUTHENTICATED_BLUETOOTH, null, 2, null);
        }
        if (accepted.size() != 1) {
            throw new ProtocolException("车机返回多个 iAP 控制流");
        }
        Map stream = (Map) CollectionsKt.single(accepted);
        Object rawPort = stream.get("dataPort");
        if (rawPort == null) {
            return new SenderControlTransport(SenderControlKind.WIFI_COMMAND, null, 2, null);
        }
        Number port = rawPort instanceof Number ? (Number) rawPort : null;
        if (port == null) {
            throw new ProtocolException("iAP dataPort 类型无效");
        }
        long value = port.longValue();
        if (value == 0) {
            return new SenderControlTransport(SenderControlKind.WIFI_COMMAND, null, 2, null);
        }
        if (1 <= value && value < 65536) {
            if (port.doubleValue() == ((double) value)) {
                return new SenderControlTransport(SenderControlKind.WIFI_DATASTREAM, Integer.valueOf((int) value));
            }
        }
        throw new ProtocolException("iAP dataPort 超出范围");
    }

    public static /* synthetic */ SenderAudioFormat audio$default(SenderNegotiation senderNegotiation, Map map, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return senderNegotiation.audio(map, z);
    }

    public final SenderAudioFormat audio(Map<?, ?> info, boolean preferAacMedia) throws ProtocolException {
        Intrinsics.checkNotNullParameter(info, "info");
        SenderAudioFormat senderAudioFormat = (SenderAudioFormat) CollectionsKt.firstOrNull((List) audioCandidates(info, preferAacMedia));
        if (senderAudioFormat != null) {
            return senderAudioFormat;
        }
        throw new ProtocolException("车机未声明可发送的媒体格式（type 100/102、44.1/48 kHz）；请关闭音频重试");
    }

    public static /* synthetic */ List audioCandidates$default(SenderNegotiation senderNegotiation, Map map, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return senderNegotiation.audioCandidates(map, z);
    }

    public final List<SenderAudioFormat> audioCandidates(Map<?, ?> info, boolean preferAacMedia) {
        Collection collectionEmptyList;
        int i;
        boolean z;
        boolean z2;
        Intrinsics.checkNotNullParameter(info, "info");
        Object obj = info.get("audioFormats");
        Iterable iterable = obj instanceof List ? (List) obj : null;
        if (iterable != null) {
            ArrayList arrayList = new ArrayList();
            for (Object obj2 : iterable) {
                Map map = obj2 instanceof Map ? (Map) obj2 : null;
                if (map != null) {
                    arrayList.add(map);
                }
            }
            collectionEmptyList = arrayList;
        } else {
            collectionEmptyList = null;
        }
        if (collectionEmptyList == null) {
            collectionEmptyList = CollectionsKt.emptyList();
        }
        ArrayList arrayList2 = new ArrayList();
        Iterator it = collectionEmptyList.iterator();
        while (true) {
            i = 2;
            z = true;
            z2 = false;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            Map map2 = (Map) next;
            List listListOf = CollectionsKt.listOf((Object[]) new Integer[]{100, Integer.valueOf(AirPlaySession.STREAM_TYPE_ALT_AUDIO), 102});
            Object obj3 = map2.get("type");
            Number number = obj3 instanceof Number ? (Number) obj3 : null;
            if (!CollectionsKt.contains(listListOf, number != null ? Integer.valueOf(number.intValue()) : null) || (map2.get("audioType") != null && !CollectionsKt.contains(CollectionsKt.listOf((Object[]) new String[]{"media", "default"}), map2.get("audioType")))) {
                z = false;
            }
            if (z) {
                arrayList2.add(next);
            }
        }
        ArrayList arrayList3 = arrayList2;
        if (preferAacMedia) {
            List<SenderAudioFormat> listAudioCandidates$aacCandidates = audioCandidates$aacCandidates(arrayList3);
            if (!listAudioCandidates$aacCandidates.isEmpty()) {
                return listAudioCandidates$aacCandidates;
            }
        }
        ArrayList<Map> arrayList4 = new ArrayList();
        for (Object obj4 : arrayList3) {
            boolean z3 = z;
            Object obj5 = ((Map) obj4).get("type");
            boolean z4 = z2;
            Number number2 = obj5 instanceof Number ? (Number) obj5 : null;
            if ((number2 == null || number2.intValue() != 100) ? z4 : z3) {
                arrayList4.add(obj4);
            }
            z = z3;
            z2 = z4;
        }
        boolean z5 = z;
        boolean z6 = z2;
        ArrayList arrayList5 = new ArrayList();
        for (Map map3 : arrayList4) {
            Pair[] pairArr = new Pair[i];
            pairArr[z6 ? 1 : 0] = TuplesKt.to(48000, 32768L);
            pairArr[z5 ? 1 : 0] = TuplesKt.to(44100, 2048L);
            List<Pair> listListOf2 = CollectionsKt.listOf((Object[]) pairArr);
            ArrayList arrayList6 = new ArrayList();
            for (Pair pair : listListOf2) {
                int iIntValue = ((Number) pair.component1()).intValue();
                long jLongValue = ((Number) pair.component2()).longValue();
                SenderAudioFormat senderAudioFormat = (audioCandidates$bits(map3) & jLongValue) == 0 ? null : new SenderAudioFormat(100, iIntValue, 2, jLongValue, AudioCodecKind.LPCM, PCM_SAMPLES_PER_PACKET);
                if (senderAudioFormat != null) {
                    arrayList6.add(senderAudioFormat);
                }
            }
            CollectionsKt.addAll(arrayList5, arrayList6);
            i = 2;
        }
        List listSortedWith = CollectionsKt.sortedWith(arrayList5, new Comparator() { // from class: com.carplay.sender.SenderNegotiation$audioCandidates$$inlined$sortedByDescending$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                SenderAudioFormat it2 = (SenderAudioFormat) t2;
                SenderAudioFormat it3 = (SenderAudioFormat) t;
                return ComparisonsKt.compareValues(Integer.valueOf(it2.getSampleRate()), Integer.valueOf(it3.getSampleRate()));
            }
        });
        List<SenderAudioFormat> listAudioCandidates$aacCandidates2 = audioCandidates$aacCandidates(arrayList3);
        return !listSortedWith.isEmpty() ? CollectionsKt.plus((Collection) listSortedWith, (Iterable) listAudioCandidates$aacCandidates2) : listAudioCandidates$aacCandidates2;
    }

    private static final long audioCandidates$bits(Map<?, ?> map) {
        Long longOrNull;
        Object raw = map.get("audioOutputFormats");
        if (raw instanceof Number) {
            return ((Number) raw).longValue();
        }
        if ((raw instanceof String) && (longOrNull = StringsKt.toLongOrNull((String) raw, 16)) != null) {
            return longOrNull.longValue();
        }
        return 0L;
    }

    private static final List<SenderAudioFormat> audioCandidates$aacCandidates(List<? extends Map<?, ?>> list) {
        Collection destination$iv$iv;
        Collection destination$iv$iv2;
        SenderAudioFormat senderAudioFormat;
        List<? extends Map<?, ?>> $this$flatMap$iv = list;
        Collection destination$iv$iv3 = new ArrayList();
        for (Object element$iv$iv : $this$flatMap$iv) {
            Map entry = (Map) element$iv$iv;
            Object obj = entry.get("type");
            Number number = obj instanceof Number ? (Number) obj : null;
            if (number != null) {
                int type = number.intValue();
                Iterable $this$mapNotNull$iv = CollectionsKt.listOf((Object[]) new Pair[]{TuplesKt.to(48000, 8388608L), TuplesKt.to(44100, 4194304L)});
                Collection destination$iv$iv4 = new ArrayList();
                for (Object element$iv$iv$iv : $this$mapNotNull$iv) {
                    Pair pair = (Pair) element$iv$iv$iv;
                    int rate = ((Number) pair.component1()).intValue();
                    long flag = ((Number) pair.component2()).longValue();
                    if ((audioCandidates$bits(entry) & flag) != 0) {
                        destination$iv$iv2 = destination$iv$iv4;
                        senderAudioFormat = new SenderAudioFormat(type, rate, 2, flag, null, 0, 48, null);
                    } else {
                        destination$iv$iv2 = destination$iv$iv4;
                        senderAudioFormat = null;
                    }
                    if (senderAudioFormat != null) {
                        destination$iv$iv2.add(senderAudioFormat);
                    }
                    destination$iv$iv4 = destination$iv$iv2;
                }
                destination$iv$iv = (List) destination$iv$iv4;
            } else {
                destination$iv$iv = CollectionsKt.emptyList();
            }
            Collection list$iv$iv = destination$iv$iv;
            CollectionsKt.addAll(destination$iv$iv3, list$iv$iv);
        }
        final Comparator comparator = new Comparator() { // from class: com.carplay.sender.SenderNegotiation$audioCandidates$aacCandidates$$inlined$compareByDescending$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                SenderAudioFormat it = (SenderAudioFormat) t2;
                Integer numValueOf = Integer.valueOf(it.getSampleRate());
                SenderAudioFormat it2 = (SenderAudioFormat) t;
                return ComparisonsKt.compareValues(numValueOf, Integer.valueOf(it2.getSampleRate()));
            }
        };
        return CollectionsKt.sortedWith((List) destination$iv$iv3, new Comparator() { // from class: com.carplay.sender.SenderNegotiation$audioCandidates$aacCandidates$$inlined$thenBy$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int previousCompare = comparator.compare(t, t2);
                if (previousCompare != 0) {
                    return previousCompare;
                }
                SenderAudioFormat it = (SenderAudioFormat) t;
                Integer numValueOf = Integer.valueOf(it.getStreamType());
                SenderAudioFormat it2 = (SenderAudioFormat) t2;
                return ComparisonsKt.compareValues(numValueOf, Integer.valueOf(it2.getStreamType()));
            }
        });
    }
}
