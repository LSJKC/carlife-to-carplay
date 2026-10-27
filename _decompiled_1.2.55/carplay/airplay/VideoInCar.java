package com.carplay.airplay;

import com.carplay.compat.Base64Compat;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: VideoInCar.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\t\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\bÆ\u0002\u0018\u00002\u00020\u0001:\u0002?@B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0018\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00020\u00052\b\b\u0002\u0010\u001d\u001a\u00020\u0011J\u000e\u0010\u001e\u001a\u00020\u00052\u0006\u0010\u001f\u001a\u00020 J$\u0010!\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\"2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010\u0010\u001a\u00020\u0011J(\u0010#\u001a\u0004\u0018\u00010$2\u0014\u0010%\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\"2\b\b\u0002\u0010\u001d\u001a\u00020\u0011J#\u0010&\u001a\u0004\u0018\u00010\r2\u0014\u0010%\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\"¢\u0006\u0002\u0010'J2\u0010(\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\"2\b\u0010)\u001a\u0004\u0018\u00010\u00012\b\u0010*\u001a\u0004\u0018\u00010\u00012\b\u0010+\u001a\u0004\u0018\u00010,J\u001e\u0010-\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\"2\b\u0010)\u001a\u0004\u0018\u00010\u0001J2\u0010.\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\"2\b\u0010)\u001a\u0004\u0018\u00010\u00012\b\u0010/\u001a\u0004\u0018\u00010\u00012\b\u0010+\u001a\u0004\u0018\u00010,J&\u00100\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\"2\b\u0010*\u001a\u0004\u0018\u00010\u00012\u0006\u00101\u001a\u00020\rJ&\u00102\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\"2\u0006\u00103\u001a\u00020\u00112\b\u0010*\u001a\u0004\u0018\u00010\u0001J\u001c\u00104\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\"2\u0006\u00105\u001a\u000206JP\u00107\u001a:\u0012\u0004\u0012\u00020\u0005\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\"08j\u001c\u0012\u0004\u0012\u00020\u0005\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\"`92\u0006\u0010:\u001a\u0002062\u0006\u0010;\u001a\u000206H\u0002J(\u0010<\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\"*\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\"H\u0002J\u001f\u00105\u001a\u0004\u0018\u0001062\u000e\u0010=\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\"H\u0002¢\u0006\u0002\u0010>R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\t¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\rX\u0086T¢\u0006\u0002\n\u0000R\u001a\u0010\u0010\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\r0\u0017X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00050\tX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006A"}, d2 = {"Lcom/carplay/airplay/VideoInCar;", "", "<init>", "()V", "FEATURE", "", "UI_URL", "SETTINGS_CHANNEL_UUID", "REMOTE_CONTROL_UUIDS", "", "getREMOTE_CONTROL_UUIDS", "()Ljava/util/Set;", "ERROR_NETWORK", "", "ERROR_DECODER", "ERROR_INCOMPATIBLE_ASSET", "allowed", "", "getAllowed", "()Z", "setAllowed", "(Z)V", "ADDITIONAL_FEATURE_BITS", "", "APP_SCHEME", "Lkotlin/text/Regex;", "NOT_APP_SCHEMES", "isPlayableUrl", "url", "iphoneLoadsAppSchemes", "featuresEx", "legacyFeatures", "", "info", "", "parseItem", "Lcom/carplay/airplay/VideoInCar$Item;", "message", "seekMillis", "(Ljava/util/Map;)Ljava/lang/Integer;", "playbackInfoResponse", "messageId", "itemUuid", "state", "Lcom/carplay/airplay/VideoInCar$PlayerState;", "seekResponse", "propertyResponse", "key", "errorNotification", "code", "playbackStateNotification", "playing", "cmTime", "seconds", "", "range", "Ljava/util/LinkedHashMap;", "Lkotlin/collections/LinkedHashMap;", "start", "duration", "withoutNulls", "time", "(Ljava/util/Map;)Ljava/lang/Double;", "Item", "PlayerState", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class VideoInCar {
    public static final int ERROR_DECODER = -12911;
    public static final int ERROR_INCOMPATIBLE_ASSET = -12927;
    public static final int ERROR_NETWORK = -17221;
    public static final String FEATURE = "videoPlayback";
    public static final String SETTINGS_CHANNEL_UUID = "BB493F61-A6B8-4769-8D74-80C23A9F71C4";
    public static final String UI_URL = "videoplayback:";
    private static volatile boolean allowed;
    public static final VideoInCar INSTANCE = new VideoInCar();
    private static final Set<String> REMOTE_CONTROL_UUIDS = SetsKt.setOf((Object[]) new String[]{"A6B27562-B43A-4F2D-B75F-82391E250194", "E3DC3EA6-E6C3-4B30-847C-B7ACFEBEA654"});
    private static final List<Integer> ADDITIONAL_FEATURE_BITS = CollectionsKt.listOf((Object[]) new Integer[]{0, 64});
    private static final Regex APP_SCHEME = new Regex("[a-z][a-z0-9+.-]*");
    private static final Set<String> NOT_APP_SCHEMES = SetsKt.setOf((Object[]) new String[]{"file", "content", "asset", "rawresource", "android.resource", "skd"});

    private VideoInCar() {
    }

    public final Set<String> getREMOTE_CONTROL_UUIDS() {
        return REMOTE_CONTROL_UUIDS;
    }

    public final boolean getAllowed() {
        return allowed;
    }

    public final void setAllowed(boolean z) {
        allowed = z;
    }

    public static /* synthetic */ boolean isPlayableUrl$default(VideoInCar videoInCar, String str, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return videoInCar.isPlayableUrl(str, z);
    }

    public final boolean isPlayableUrl(String url, boolean iphoneLoadsAppSchemes) {
        Intrinsics.checkNotNullParameter(url, "url");
        String scheme = StringsKt.substringBefore(url, ':', "").toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(scheme, "toLowerCase(...)");
        return Intrinsics.areEqual(scheme, "http") || Intrinsics.areEqual(scheme, "https") || (iphoneLoadsAppSchemes && APP_SCHEME.matches(scheme) && !NOT_APP_SCHEMES.contains(scheme));
    }

    public final String featuresEx(long legacyFeatures) {
        List<Byte> listEmptyList;
        BigInteger bigIntegerValueOf = BigInteger.valueOf(legacyFeatures);
        Iterable $this$forEach$iv = ADDITIONAL_FEATURE_BITS;
        for (Object element$iv : $this$forEach$iv) {
            int it = ((Number) element$iv).intValue();
            bigIntegerValueOf = bigIntegerValueOf.setBit(it);
        }
        byte[] byteArray = bigIntegerValueOf.toByteArray();
        Intrinsics.checkNotNullExpressionValue(byteArray, "toByteArray(...)");
        byte[] $this$dropLastWhile$iv = ArraysKt.reversedArray(byteArray);
        for (int index$iv = ArraysKt.getLastIndex($this$dropLastWhile$iv); -1 < index$iv; index$iv--) {
            byte it2 = $this$dropLastWhile$iv[index$iv];
            if (!(it2 == 0)) {
                listEmptyList = ArraysKt.take($this$dropLastWhile$iv, index$iv + 1);
                byte[] littleEndian = CollectionsKt.toByteArray(listEmptyList);
                return Base64Compat.INSTANCE.encode(littleEndian);
            }
        }
        listEmptyList = CollectionsKt.emptyList();
        byte[] littleEndian2 = CollectionsKt.toByteArray(listEmptyList);
        return Base64Compat.INSTANCE.encode(littleEndian2);
    }

    public final Map<String, Object> info(long legacyFeatures, boolean allowed2) {
        return MapsKt.linkedMapOf(TuplesKt.to("videoPlaybackAllowed", Boolean.valueOf(allowed2)), TuplesKt.to("featuresEx", featuresEx(legacyFeatures)), TuplesKt.to("playbackCapabilities", MapsKt.linkedMapOf(TuplesKt.to("supportsOfflineHLS", false), TuplesKt.to("supportsAirPlayVideoWithSharePlay", false), TuplesKt.to("supportsInterstitials", false), TuplesKt.to("supportsIntegratedTimeline", false), TuplesKt.to("supportsAVMetrics", false), TuplesKt.to("supportsUIForAudioOnlyContent", true), TuplesKt.to("supportsFPSSecureStop", false))));
    }

    /* JADX INFO: compiled from: VideoInCar.kt */
    @Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B!\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0004\b\u0007\u0010\bJ\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0001HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0004HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0006HÆ\u0003J)\u0010\u0012\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0006HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0004HÖ\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0001¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u0018"}, d2 = {"Lcom/carplay/airplay/VideoInCar$Item;", "", "uuid", "url", "", "startMillis", "", "<init>", "(Ljava/lang/Object;Ljava/lang/String;I)V", "getUuid", "()Ljava/lang/Object;", "getUrl", "()Ljava/lang/String;", "getStartMillis", "()I", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Item {
        private final int startMillis;
        private final String url;
        private final Object uuid;

        public static /* synthetic */ Item copy$default(Item item, Object obj, String str, int i, int i2, Object obj2) {
            if ((i2 & 1) != 0) {
                obj = item.uuid;
            }
            if ((i2 & 2) != 0) {
                str = item.url;
            }
            if ((i2 & 4) != 0) {
                i = item.startMillis;
            }
            return item.copy(obj, str, i);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final Object getUuid() {
            return this.uuid;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final String getUrl() {
            return this.url;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final int getStartMillis() {
            return this.startMillis;
        }

        public final Item copy(Object uuid, String url, int startMillis) {
            Intrinsics.checkNotNullParameter(url, "url");
            return new Item(uuid, url, startMillis);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Item)) {
                return false;
            }
            Item item = (Item) other;
            return Intrinsics.areEqual(this.uuid, item.uuid) && Intrinsics.areEqual(this.url, item.url) && this.startMillis == item.startMillis;
        }

        public int hashCode() {
            return ((((this.uuid == null ? 0 : this.uuid.hashCode()) * 31) + this.url.hashCode()) * 31) + Integer.hashCode(this.startMillis);
        }

        public String toString() {
            return "Item(uuid=" + this.uuid + ", url=" + this.url + ", startMillis=" + this.startMillis + ")";
        }

        public Item(Object uuid, String url, int startMillis) {
            Intrinsics.checkNotNullParameter(url, "url");
            this.uuid = uuid;
            this.url = url;
            this.startMillis = startMillis;
        }

        public final int getStartMillis() {
            return this.startMillis;
        }

        public final String getUrl() {
            return this.url;
        }

        public final Object getUuid() {
            return this.uuid;
        }
    }

    public static /* synthetic */ Item parseItem$default(VideoInCar videoInCar, Map map, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return videoInCar.parseItem(map, z);
    }

    public final Item parseItem(Map<String, ? extends Object> message, boolean iphoneLoadsAppSchemes) {
        int startMillis;
        Intrinsics.checkNotNullParameter(message, "message");
        Object obj = message.get("item");
        Double startSeconds = null;
        Map item = obj instanceof Map ? (Map) obj : null;
        if (item == null) {
            return null;
        }
        Object obj2 = item.get("Content-Location");
        String url = obj2 instanceof String ? (String) obj2 : null;
        if (url == null || !isPlayableUrl(url, iphoneLoadsAppSchemes)) {
            return null;
        }
        Object obj3 = item.get("Start-Position-Seconds");
        Number number = obj3 instanceof Number ? (Number) obj3 : null;
        if (number != null) {
            startSeconds = Double.valueOf(number.doubleValue());
        } else {
            Object obj4 = item.get("Start-Position");
            Map<?, ?> map = obj4 instanceof Map ? (Map) obj4 : null;
            if (map != null) {
                startSeconds = seconds(map);
            }
        }
        if (startSeconds != null) {
            double it = startSeconds.doubleValue();
            startMillis = (int) RangesKt.coerceIn((long) (1000.0d * it), 0L, 2147483647L);
        } else {
            startMillis = 0;
        }
        return new Item(item.get("uuid"), url, startMillis);
    }

    public final Integer seekMillis(Map<String, ? extends Object> message) {
        Double dSeconds;
        Intrinsics.checkNotNullParameter(message, "message");
        Object obj = message.get("time");
        Map<?, ?> map = obj instanceof Map ? (Map) obj : null;
        if (map == null || (dSeconds = seconds(map)) == null) {
            return null;
        }
        double it = dSeconds.doubleValue();
        return Integer.valueOf((int) RangesKt.coerceIn((long) (1000.0d * it), 0L, 2147483647L));
    }

    /* JADX INFO: compiled from: VideoInCar.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0014\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0006¢\u0006\u0004\b\t\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0006HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0006HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0006HÆ\u0003J;\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u0018\u001a\u00020\u00032\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001a\u001a\u00020\u001bHÖ\u0001J\t\u0010\u001c\u001a\u00020\u001dHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\fR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0007\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0011\u0010\b\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000f¨\u0006\u001e"}, d2 = {"Lcom/carplay/airplay/VideoInCar$PlayerState;", "", "prepared", "", "playing", "positionSeconds", "", "durationSeconds", "bufferedSeconds", "<init>", "(ZZDDD)V", "getPrepared", "()Z", "getPlaying", "getPositionSeconds", "()D", "getDurationSeconds", "getBufferedSeconds", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class PlayerState {
        private final double bufferedSeconds;
        private final double durationSeconds;
        private final boolean playing;
        private final double positionSeconds;
        private final boolean prepared;

        public static /* synthetic */ PlayerState copy$default(PlayerState playerState, boolean z, boolean z2, double d, double d2, double d3, int i, Object obj) {
            if ((i & 1) != 0) {
                z = playerState.prepared;
            }
            if ((i & 2) != 0) {
                z2 = playerState.playing;
            }
            if ((i & 4) != 0) {
                d = playerState.positionSeconds;
            }
            if ((i & 8) != 0) {
                d2 = playerState.durationSeconds;
            }
            if ((i & 16) != 0) {
                d3 = playerState.bufferedSeconds;
            }
            double d4 = d3;
            double d5 = d2;
            return playerState.copy(z, z2, d, d5, d4);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final boolean getPrepared() {
            return this.prepared;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final boolean getPlaying() {
            return this.playing;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final double getPositionSeconds() {
            return this.positionSeconds;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final double getDurationSeconds() {
            return this.durationSeconds;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final double getBufferedSeconds() {
            return this.bufferedSeconds;
        }

        public final PlayerState copy(boolean prepared, boolean playing, double positionSeconds, double durationSeconds, double bufferedSeconds) {
            return new PlayerState(prepared, playing, positionSeconds, durationSeconds, bufferedSeconds);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof PlayerState)) {
                return false;
            }
            PlayerState playerState = (PlayerState) other;
            return this.prepared == playerState.prepared && this.playing == playerState.playing && Double.compare(this.positionSeconds, playerState.positionSeconds) == 0 && Double.compare(this.durationSeconds, playerState.durationSeconds) == 0 && Double.compare(this.bufferedSeconds, playerState.bufferedSeconds) == 0;
        }

        public int hashCode() {
            return (((((((Boolean.hashCode(this.prepared) * 31) + Boolean.hashCode(this.playing)) * 31) + Double.hashCode(this.positionSeconds)) * 31) + Double.hashCode(this.durationSeconds)) * 31) + Double.hashCode(this.bufferedSeconds);
        }

        public String toString() {
            return "PlayerState(prepared=" + this.prepared + ", playing=" + this.playing + ", positionSeconds=" + this.positionSeconds + ", durationSeconds=" + this.durationSeconds + ", bufferedSeconds=" + this.bufferedSeconds + ")";
        }

        public PlayerState(boolean prepared, boolean playing, double positionSeconds, double durationSeconds, double bufferedSeconds) {
            this.prepared = prepared;
            this.playing = playing;
            this.positionSeconds = positionSeconds;
            this.durationSeconds = durationSeconds;
            this.bufferedSeconds = bufferedSeconds;
        }

        public final boolean getPrepared() {
            return this.prepared;
        }

        public final boolean getPlaying() {
            return this.playing;
        }

        public final double getPositionSeconds() {
            return this.positionSeconds;
        }

        public final double getDurationSeconds() {
            return this.durationSeconds;
        }

        public final double getBufferedSeconds() {
            return this.bufferedSeconds;
        }
    }

    public final Map<String, Object> playbackInfoResponse(Object messageId, Object itemUuid, PlayerState state) {
        String str;
        boolean ready = state != null && state.getPrepared();
        double duration = state != null ? state.getDurationSeconds() : 0.0d;
        Pair[] pairArr = new Pair[4];
        pairArr[0] = TuplesKt.to("item", MapsKt.linkedMapOf(TuplesKt.to("uuid", itemUuid)));
        pairArr[1] = TuplesKt.to("rate", Double.valueOf(state != null && state.getPlaying() ? 1.0d : 0.0d));
        pairArr[2] = TuplesKt.to("readyToPlay", Boolean.valueOf(ready));
        pairArr[3] = TuplesKt.to("playbackLikelyToKeepUp", Boolean.valueOf(ready));
        LinkedHashMap info = MapsKt.linkedMapOf(pairArr);
        if (state != null && ready) {
            info.put("position", cmTime(state.getPositionSeconds()));
            info.put("duration", cmTime(duration));
            info.put("seekableTimeRanges", CollectionsKt.listOf(range(0.0d, duration)));
            info.put("loadedTimeRanges", CollectionsKt.listOf(range(0.0d, state.getBufferedSeconds())));
        }
        info.put("droppedVideoFrames", 0L);
        info.put("totalVideoFrames", 0L);
        info.put("decodedFrameCount", 0L);
        LinkedHashMap linkedHashMap = info;
        if (state == null || !ready) {
            str = "loading";
        } else {
            str = state.getPlaying() ? "playing" : "paused";
        }
        linkedHashMap.put("playbackState", str);
        info.put("interstitialInfo", new LinkedHashMap());
        return withoutNulls(MapsKt.linkedMapOf(TuplesKt.to("info", info), TuplesKt.to("kind", "response"), TuplesKt.to("type", "playbackInfo"), TuplesKt.to("messageID", messageId)));
    }

    public final Map<String, Object> seekResponse(Object messageId) {
        return withoutNulls(MapsKt.linkedMapOf(TuplesKt.to("type", "seek"), TuplesKt.to("kind", "response"), TuplesKt.to("messageID", messageId)));
    }

    public final Map<String, Object> propertyResponse(Object messageId, Object key, PlayerState state) {
        Object value;
        boolean ready = state != null && state.getPrepared();
        if (Intrinsics.areEqual(key, "hasEnabledAudio")) {
            value = true;
        } else if (Intrinsics.areEqual(key, "muted")) {
            value = false;
        } else if (Intrinsics.areEqual(key, "seekableTimeRanges")) {
            if (ready) {
                Intrinsics.checkNotNull(state);
                value = CollectionsKt.listOf(range(0.0d, state.getDurationSeconds()));
            } else {
                value = null;
            }
        } else if (Intrinsics.areEqual(key, "loadedTimeRanges") && ready) {
            Intrinsics.checkNotNull(state);
            value = CollectionsKt.listOf(range(0.0d, state.getBufferedSeconds()));
        } else {
            value = null;
        }
        return withoutNulls(MapsKt.linkedMapOf(TuplesKt.to("key", key), TuplesKt.to("value", value), TuplesKt.to("kind", "response"), TuplesKt.to("type", "property"), TuplesKt.to("messageID", messageId)));
    }

    public final Map<String, Object> errorNotification(Object itemUuid, int code) {
        return withoutNulls(MapsKt.linkedMapOf(TuplesKt.to("type", "error"), TuplesKt.to("error", MapsKt.linkedMapOf(TuplesKt.to("domain", ""), TuplesKt.to("code", Long.valueOf(code)))), TuplesKt.to("uuid", itemUuid)));
    }

    public final Map<String, Object> playbackStateNotification(boolean playing, Object itemUuid) {
        return playing ? withoutNulls(MapsKt.linkedMapOf(TuplesKt.to("type", "playbackState"), TuplesKt.to("name", "playing"), TuplesKt.to("item", MapsKt.linkedMapOf(TuplesKt.to("uuid", itemUuid))))) : MapsKt.linkedMapOf(TuplesKt.to("type", "playbackState"), TuplesKt.to("name", "paused"));
    }

    public final Map<String, Object> cmTime(double seconds) {
        return MapsKt.linkedMapOf(TuplesKt.to("value", Long.valueOf(Math.round(1000.0d * seconds))), TuplesKt.to("timescale", 1000L), TuplesKt.to("flags", 1L), TuplesKt.to("epoch", 0L));
    }

    private final LinkedHashMap<String, Map<String, Object>> range(double start, double duration) {
        return MapsKt.linkedMapOf(TuplesKt.to("start", cmTime(start)), TuplesKt.to("duration", cmTime(duration)));
    }

    private static final Object withoutNulls$strip(Object value) {
        if (!(value instanceof Map)) {
            if (!(value instanceof List)) {
                return value;
            }
            Iterable $this$map$iv = CollectionsKt.filterNotNull((Iterable) value);
            Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
            for (Object item$iv$iv : $this$map$iv) {
                Object p0 = withoutNulls$strip(item$iv$iv);
                destination$iv$iv.add(p0);
            }
            return (List) destination$iv$iv;
        }
        Iterable $this$filter$iv = ((Map) value).entrySet();
        Collection destination$iv$iv2 = new ArrayList();
        for (Object element$iv$iv : $this$filter$iv) {
            if (((Map.Entry) element$iv$iv).getValue() != null) {
                destination$iv$iv2.add(element$iv$iv);
            }
        }
        Iterable $this$associateTo$iv = (List) destination$iv$iv2;
        Map destination$iv = new LinkedHashMap();
        for (Object element$iv : $this$associateTo$iv) {
            Map.Entry it = (Map.Entry) element$iv;
            Pair pair = TuplesKt.to(String.valueOf(it.getKey()), withoutNulls$strip(it.getValue()));
            destination$iv.put(pair.getFirst(), pair.getSecond());
        }
        return destination$iv;
    }

    private final Map<String, Object> withoutNulls(Map<String, ? extends Object> map) {
        Object objWithoutNulls$strip = withoutNulls$strip(map);
        Intrinsics.checkNotNull(objWithoutNulls$strip, "null cannot be cast to non-null type kotlin.collections.Map<kotlin.String, kotlin.Any?>");
        return (Map) objWithoutNulls$strip;
    }

    private final Double seconds(Map<?, ?> time) {
        Object obj = time.get("value");
        Number number = obj instanceof Number ? (Number) obj : null;
        if (number == null) {
            return null;
        }
        double value = number.doubleValue();
        Object obj2 = time.get("timescale");
        Number number2 = obj2 instanceof Number ? (Number) obj2 : null;
        if (number2 != null) {
            Double dValueOf = Double.valueOf(number2.doubleValue());
            double it = dValueOf.doubleValue();
            if (!(it > 0.0d)) {
                dValueOf = null;
            }
            if (dValueOf != null) {
                double timescale = dValueOf.doubleValue();
                return Double.valueOf(value / timescale);
            }
        }
        return null;
    }
}
