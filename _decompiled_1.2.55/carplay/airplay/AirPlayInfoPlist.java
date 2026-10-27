package com.carplay.airplay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: AirPlayInfoPlist.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0015\u001a\u00020\r2\u0006\u0010\u0016\u001a\u00020\u0017J\u001c\u0010\u0018\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u00192\u0006\u0010\u0016\u001a\u00020\u0017J\u001e\u0010\u001a\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u00192\u0006\u0010\u001b\u001a\u00020\bH\u0002J\u0016\u0010\u001c\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0019H\u0002J\u001c\u0010\u001d\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u00190\u001eH\u0002J6\u0010\u001f\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u00190\u001e2\u0006\u0010 \u001a\u00020\b2\u0006\u0010!\u001a\u00020\"2\b\b\u0002\u0010#\u001a\u00020\"H\u0002J$\u0010'\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u00192\u0006\u0010(\u001a\u00020\b2\u0006\u0010)\u001a\u00020\bJ.\u0010*\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u00192\u0006\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020\b2\u0006\u0010.\u001a\u00020\u0005H\u0002J*\u0010/\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u00192\u0006\u0010+\u001a\u00020,2\n\b\u0002\u00100\u001a\u0004\u0018\u000101H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\bX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000¨\u00062"}, d2 = {"Lcom/carplay/airplay/AirPlayInfoPlist;", "", "<init>", "()V", "MAIN_UUID", "", "ALT_UUID", "STREAM_TYPE_MAIN_SCREEN", "", "STREAM_TYPE_ALT_SCREEN", "DISPLAY_FEATURE_KNOBS", "DISPLAY_FEATURE_HIGH_FIDELITY_TOUCH", "CARPLAY_FEATURES", "", "CARPLAY_AUDIO_FEATURES", "CARPLAY_FEATURES_NO_AUDIO", "RESOURCE_SCREEN", "RESOURCE_AUDIO", "TRANSFER_TAKE", "PRIORITY_NICE_TO_HAVE", "CONSTRAINT_ANYTIME", "features", "config", "Lcom/carplay/airplay/AirPlayConfig;", "build", "", "resource", "resourceId", "modes", "audioLatencies", "", "audioFormats", "entertainmentRate", "microphone", "", AirPlaySessionKt.MAIN_BUFFERED_FEATURE, "DOCK_EDGE_BOTTOM", "DOCK_EDGE_DRIVER_SIDE", "VIEW_AREA_ANIMATION_MILLIS", "viewAreaCommand", "index", "areaCount", "displayEntry", "display", "Lcom/carplay/airplay/AirPlayDisplayConfig;", "type", "uuid", "areaDict", "area", "Lcom/carplay/airplay/AirPlayViewArea;", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AirPlayInfoPlist {
    public static final String ALT_UUID = "b7e6c5a0-2222-4000-8000-000000000002";
    private static final long CARPLAY_AUDIO_FEATURES = 1099584244224L;
    private static final long CARPLAY_FEATURES = 418060152546L;
    private static final int CONSTRAINT_ANYTIME = 100;
    private static final int DISPLAY_FEATURE_HIGH_FIDELITY_TOUCH = 8;
    private static final int DISPLAY_FEATURE_KNOBS = 2;
    public static final int DOCK_EDGE_BOTTOM = 1;
    public static final int DOCK_EDGE_DRIVER_SIDE = 2;
    public static final String MAIN_UUID = "b7e6c5a0-1111-4000-8000-000000000001";
    private static final int PRIORITY_NICE_TO_HAVE = 100;
    private static final int RESOURCE_AUDIO = 2;
    private static final int RESOURCE_SCREEN = 1;
    private static final int STREAM_TYPE_ALT_SCREEN = 111;
    private static final int STREAM_TYPE_MAIN_SCREEN = 110;
    private static final int TRANSFER_TAKE = 1;
    private static final int VIEW_AREA_ANIMATION_MILLIS = 300;
    public static final AirPlayInfoPlist INSTANCE = new AirPlayInfoPlist();
    private static final long CARPLAY_FEATURES_NO_AUDIO = 417987798242L;

    private AirPlayInfoPlist() {
    }

    public final long features(AirPlayConfig config) {
        Intrinsics.checkNotNullParameter(config, "config");
        return config.getDisableAudioOutput() ? CARPLAY_FEATURES_NO_AUDIO : CARPLAY_FEATURES;
    }

    public final Map<String, Object> build(AirPlayConfig config) {
        Intrinsics.checkNotNullParameter(config, "config");
        ArrayList arrayListArrayListOf = CollectionsKt.arrayListOf(displayEntry(config.getMain(), 110, MAIN_UUID));
        AirPlayDisplayConfig cluster = config.getCluster();
        if (cluster != null) {
            arrayListArrayListOf.add(INSTANCE.displayEntry(cluster, 111, ALT_UUID));
        }
        char c = 0;
        boolean z = true;
        char c2 = 2;
        LinkedHashMap linkedHashMapLinkedMapOf = MapsKt.linkedMapOf(TuplesKt.to("sourceVersion", config.getSourceVersion()), TuplesKt.to("features", Long.valueOf(features(config))), TuplesKt.to("statusFlags", 4L), TuplesKt.to("model", config.getModel()), TuplesKt.to("manufacturer", config.getManufacturer()), TuplesKt.to("deviceID", config.getDeviceId()), TuplesKt.to("bluetoothIDs", CollectionsKt.listOf(config.getBtMac())), TuplesKt.to("name", config.getDeviceName()), TuplesKt.to("rightHandDrive", Boolean.valueOf(config.getRightHandDrive())), TuplesKt.to("keepAliveLowPower", false), TuplesKt.to("keepAliveSendStatsAsBody", false), TuplesKt.to("modes", modes()));
        if (!config.getDisableAudioOutput()) {
            linkedHashMapLinkedMapOf.put("audioLatencies", audioLatencies());
            linkedHashMapLinkedMapOf.put("audioFormats", audioFormats(config.getEntertainmentSampleRate(), config.getMicrophone(), config.getMainBufferedAudio()));
        }
        linkedHashMapLinkedMapOf.put("extendedFeatures", CollectionsKt.listOf((Object[]) new String[]{"vocoderInfo", "enhancedRequestCarUI"}));
        linkedHashMapLinkedMapOf.put("displays", arrayListArrayListOf);
        linkedHashMapLinkedMapOf.put("hidDevices", CollectionsKt.listOf((Object[]) new Map[]{AirPlayHid.INSTANCE.touchHidDevice(config.getMain().getWidthPixels(), config.getMain().getHeightPixels(), MAIN_UUID), AirPlayHid.INSTANCE.knobHidDevice(MAIN_UUID), AirPlayHid.INSTANCE.mediaHidDevice(MAIN_UUID), AirPlayHid.INSTANCE.telephonyHidDevice(MAIN_UUID)}));
        if (!config.getIcons().isEmpty()) {
            linkedHashMapLinkedMapOf.put("oemIconVisible", true);
            linkedHashMapLinkedMapOf.put("oemIconLabel", config.getOemLabel());
            LinkedHashMap linkedHashMap = linkedHashMapLinkedMapOf;
            List<AirPlayIcon> icons = config.getIcons();
            ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(icons, 10));
            for (AirPlayIcon airPlayIcon : icons) {
                char c3 = c;
                Pair[] pairArr = new Pair[4];
                boolean z2 = z;
                char c4 = c2;
                pairArr[c3] = TuplesKt.to("imageData", airPlayIcon.getData());
                pairArr[z2 ? 1 : 0] = TuplesKt.to("widthPixels", Integer.valueOf(airPlayIcon.getWidthPixels()));
                pairArr[c4] = TuplesKt.to("heightPixels", Integer.valueOf(airPlayIcon.getHeightPixels()));
                pairArr[3] = TuplesKt.to("prerendered", Boolean.valueOf(z2));
                arrayList.add(MapsKt.linkedMapOf(pairArr));
                c = c3;
                z = z2 ? 1 : 0;
                c2 = c4;
            }
            linkedHashMap.put("oemIcons", arrayList);
        }
        if (config.getHevc()) {
            linkedHashMapLinkedMapOf.put("hevcInfo", MapsKt.emptyMap());
        }
        if (AirPlayConfigKt.getBufferedAudioOutputEnabled(config)) {
            linkedHashMapLinkedMapOf.put("mainBufferedInfo", MapsKt.emptyMap());
        }
        if (config.getVideoInCar()) {
            linkedHashMapLinkedMapOf.put("videoPlaybackInfo", VideoInCar.INSTANCE.info(features(config), VideoInCar.INSTANCE.getAllowed()));
        }
        return linkedHashMapLinkedMapOf;
    }

    private final Map<String, Object> resource(int resourceId) {
        return MapsKt.linkedMapOf(TuplesKt.to("resourceID", Integer.valueOf(resourceId)), TuplesKt.to("transferType", 1), TuplesKt.to("transferPriority", 100), TuplesKt.to("takeConstraint", 100), TuplesKt.to("borrowConstraint", 100), TuplesKt.to("unborrowConstraint", 100));
    }

    private final Map<String, Object> modes() {
        return MapsKt.linkedMapOf(TuplesKt.to("resources", CollectionsKt.listOf((Object[]) new Map[]{resource(1), resource(2)})), TuplesKt.to("appStates", CollectionsKt.listOf((Object[]) new LinkedHashMap[]{MapsKt.linkedMapOf(TuplesKt.to("appStateID", 2), TuplesKt.to("state", false)), MapsKt.linkedMapOf(TuplesKt.to("appStateID", 1), TuplesKt.to("speechMode", -1)), MapsKt.linkedMapOf(TuplesKt.to("appStateID", 3), TuplesKt.to("state", false))})));
    }

    static /* synthetic */ Map audioLatencies$base$default(int i, String str, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            str = null;
        }
        return audioLatencies$base(i, str);
    }

    private static final Map<String, Object> audioLatencies$base(int type, String audioType) {
        LinkedHashMap entry = MapsKt.linkedMapOf(TuplesKt.to("type", Integer.valueOf(type)), TuplesKt.to("inputLatencyMicros", 0L), TuplesKt.to("outputLatencyMicros", 0L));
        if (audioType != null) {
            entry.put("audioType", audioType);
        }
        return entry;
    }

    private final List<Map<String, Object>> audioLatencies() {
        return CollectionsKt.listOf((Object[]) new Map[]{audioLatencies$base$default(100, null, 2, null), audioLatencies$base(100, "default"), audioLatencies$base(100, "media"), audioLatencies$base(100, "telephony"), audioLatencies$base(100, "speechRecognition"), audioLatencies$base(100, "alert"), audioLatencies$base$default(AirPlaySession.STREAM_TYPE_ALT_AUDIO, null, 2, null), audioLatencies$base(AirPlaySession.STREAM_TYPE_ALT_AUDIO, "default"), audioLatencies$base(102, "default")});
    }

    static /* synthetic */ List audioFormats$default(AirPlayInfoPlist airPlayInfoPlist, int i, boolean z, boolean z2, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            z2 = false;
        }
        return airPlayInfoPlist.audioFormats(i, z, z2);
    }

    static /* synthetic */ Map audioFormats$format$default(int i, String str, int i2, Integer num, int i3, Object obj) {
        if ((i3 & 8) != 0) {
            num = null;
        }
        return audioFormats$format(i, str, i2, num);
    }

    private static final Map<String, Object> audioFormats$format(int type, String audioType, int outputFormats, Integer inputFormats) {
        LinkedHashMap entry = MapsKt.linkedMapOf(TuplesKt.to("type", Integer.valueOf(type)), TuplesKt.to("audioType", audioType), TuplesKt.to("audioOutputFormats", Integer.valueOf(outputFormats)));
        if (inputFormats != null) {
            entry.put("audioInputFormats", inputFormats);
        }
        return entry;
    }

    private final List<Map<String, Object>> audioFormats(int entertainmentRate, boolean microphone, boolean mainBuffered) {
        boolean is48 = entertainmentRate == 48000;
        int pcm = 1020 | (is48 ? 49152 : 3072);
        int pcmMono = (is48 ? 16384 : 1024) | 340;
        int aacLc = is48 ? 8388608 : 4194304;
        Integer pcmInput = microphone ? Integer.valueOf(pcmMono) : null;
        Integer wirelessInput = microphone ? Integer.valueOf(pcmMono | 1879048192) : null;
        return CollectionsKt.plus((Collection) CollectionsKt.listOf((Object[]) new Map[]{audioFormats$format(100, "compatibility", pcm, pcmInput), audioFormats$format$default(AirPlaySession.STREAM_TYPE_ALT_AUDIO, "compatibility", pcm, null, 8, null), audioFormats$format(100, "default", pcm | 1879048192, wirelessInput), audioFormats$format$default(100, "alert", pcm | 1879048192, null, 8, null), audioFormats$format$default(100, "media", pcm, null, 8, null), audioFormats$format(100, "telephony", pcmMono | 1879048192, wirelessInput), audioFormats$format(100, "speechRecognition", pcmMono | 1879048192, wirelessInput), audioFormats$format$default(AirPlaySession.STREAM_TYPE_ALT_AUDIO, "default", pcm | 1879048192, null, 8, null), audioFormats$format$default(102, "media", aacLc, null, 8, null)}), (Iterable) (mainBuffered ? CollectionsKt.listOf(audioFormats$format$default(103, "media", aacLc, null, 8, null)) : CollectionsKt.emptyList()));
    }

    public final Map<String, Object> viewAreaCommand(int index, int areaCount) {
        Pair[] pairArr = new Pair[2];
        pairArr[0] = TuplesKt.to("type", "updateViewArea");
        Pair[] pairArr2 = new Pair[4];
        pairArr2[0] = TuplesKt.to("uuid", MAIN_UUID);
        pairArr2[1] = TuplesKt.to("viewAreaIndex", Integer.valueOf(index));
        pairArr2[2] = TuplesKt.to("animationDurationMillis", 300);
        Iterable $this$filter$iv = RangesKt.until(0, areaCount);
        Collection destination$iv$iv = new ArrayList();
        for (Integer num : $this$filter$iv) {
            int it = num.intValue();
            int it2 = it != index ? 1 : 0;
            if (it2 != 0) {
                destination$iv$iv.add(num);
            }
        }
        pairArr2[3] = TuplesKt.to("adjacentViewAreas", (List) destination$iv$iv);
        pairArr[1] = TuplesKt.to("params", MapsKt.linkedMapOf(pairArr2));
        return MapsKt.linkedMapOf(pairArr);
    }

    private final Map<String, Object> displayEntry(AirPlayDisplayConfig display, int type, String uuid) {
        List listListOf;
        AirPlayDisplaySettings airPlayDisplaySettings = AirPlayDisplaySettings.INSTANCE;
        Integer widthPhysicalMm = display.getWidthPhysicalMm();
        int widthPhysical = airPlayDisplaySettings.sanitizeReportedPhysicalMm(widthPhysicalMm != null ? widthPhysicalMm.intValue() : 200);
        AirPlayDisplaySettings airPlayDisplaySettings2 = AirPlayDisplaySettings.INSTANCE;
        Integer heightPhysicalMm = display.getHeightPhysicalMm();
        int heightPhysical = airPlayDisplaySettings2.sanitizeReportedPhysicalMm(heightPhysicalMm != null ? heightPhysicalMm.intValue() : (int) Math.round((((double) widthPhysical) * ((double) display.getHeightPixels())) / ((double) display.getWidthPixels())));
        int fps = AirPlayDisplaySettings.INSTANCE.sanitizeFps(display.getFps());
        Pair[] pairArr = new Pair[9];
        pairArr[0] = TuplesKt.to("uuid", uuid);
        pairArr[1] = TuplesKt.to("type", Integer.valueOf(type));
        pairArr[2] = TuplesKt.to("maxFPS", Integer.valueOf(fps));
        pairArr[3] = TuplesKt.to("widthPixels", Integer.valueOf(display.getWidthPixels()));
        pairArr[4] = TuplesKt.to("heightPixels", Integer.valueOf(display.getHeightPixels()));
        pairArr[5] = TuplesKt.to("widthPhysical", Integer.valueOf(widthPhysical));
        pairArr[6] = TuplesKt.to("heightPhysical", Integer.valueOf(heightPhysical));
        Integer features = display.getFeatures();
        pairArr[7] = TuplesKt.to("features", Integer.valueOf(features != null ? features.intValue() : 10));
        pairArr[8] = TuplesKt.to("primaryInputDevice", Integer.valueOf(display.getPrimaryInputDevice()));
        LinkedHashMap entry = MapsKt.linkedMapOf(pairArr);
        List<AirPlayViewArea> viewAreas = display.getViewAreas();
        if (viewAreas == null || viewAreas.isEmpty()) {
            viewAreas = null;
        }
        LinkedHashMap linkedHashMap = entry;
        if (viewAreas != null) {
            Iterable $this$map$iv = viewAreas;
            Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
            for (Object item$iv$iv : $this$map$iv) {
                AirPlayViewArea it = (AirPlayViewArea) item$iv$iv;
                destination$iv$iv.add(INSTANCE.areaDict(display, it));
            }
            listListOf = (List) destination$iv$iv;
        } else {
            listListOf = CollectionsKt.listOf(areaDict$default(this, display, null, 2, null));
        }
        linkedHashMap.put("viewAreas", listListOf);
        entry.put("initialViewArea", Integer.valueOf(viewAreas == null ? 0 : RangesKt.coerceIn(display.getInitialViewArea(), 0, CollectionsKt.getLastIndex(viewAreas))));
        if (viewAreas != null && viewAreas.size() > 1) {
            entry.put("viewAreaTransitionControl", true);
        }
        if (display.getInitialUrl() != null) {
            entry.put("initialURL", display.getInitialUrl());
        }
        return entry;
    }

    static /* synthetic */ Map areaDict$default(AirPlayInfoPlist airPlayInfoPlist, AirPlayDisplayConfig airPlayDisplayConfig, AirPlayViewArea airPlayViewArea, int i, Object obj) {
        if ((i & 2) != 0) {
            airPlayViewArea = null;
        }
        return airPlayInfoPlist.areaDict(airPlayDisplayConfig, airPlayViewArea);
    }

    private final Map<String, Object> areaDict(AirPlayDisplayConfig display, AirPlayViewArea area) {
        AirPlayInsets view;
        Integer dockEdge;
        int width = display.getWidthPixels();
        int height = display.getHeightPixels();
        if (area != null) {
            view = new AirPlayInsets(area.getOriginY(), (height - area.getOriginY()) - area.getHeight(), area.getOriginX(), (width - area.getOriginX()) - area.getWidth());
        } else {
            view = display.getViewArea();
            if (view == null) {
                view = new AirPlayInsets(0, 0, 0, 0, 15, null);
            }
        }
        LinkedHashMap result = MapsKt.linkedMapOf(TuplesKt.to("widthPixels", Integer.valueOf((width - view.getLeft()) - view.getRight())), TuplesKt.to("heightPixels", Integer.valueOf((height - view.getTop()) - view.getBottom())), TuplesKt.to("originXPixels", Integer.valueOf(view.getLeft())), TuplesKt.to("originYPixels", Integer.valueOf(view.getTop())));
        if (area != null && (dockEdge = area.getDockEdge()) != null) {
            int it = dockEdge.intValue();
            result.put("viewAreaStatusBarEdge", Integer.valueOf(it));
        }
        AirPlayInsets displaySafe = display.getSafeArea();
        if (displaySafe == null) {
            displaySafe = new AirPlayInsets(0, 0, 0, 0, 15, null);
        }
        AirPlayInsets clipped = area == null ? displaySafe : new AirPlayInsets(Math.max(displaySafe.getTop(), view.getTop()), Math.max(displaySafe.getBottom(), view.getBottom()), Math.max(displaySafe.getLeft(), view.getLeft()), Math.max(displaySafe.getRight(), view.getRight()));
        AirPlayInsets safe = (area == null || (clipped.getLeft() + clipped.getRight() < width && clipped.getTop() + clipped.getBottom() < height)) ? clipped : view;
        Pair[] pairArr = new Pair[5];
        pairArr[0] = TuplesKt.to("widthPixels", Integer.valueOf((width - safe.getLeft()) - safe.getRight()));
        pairArr[r9] = TuplesKt.to("heightPixels", Integer.valueOf((height - safe.getTop()) - safe.getBottom()));
        pairArr[2] = TuplesKt.to("originXPixels", Integer.valueOf(safe.getLeft()));
        pairArr[3] = TuplesKt.to("originYPixels", Integer.valueOf(safe.getTop()));
        Boolean safeAreaDrawOutside = display.getSafeAreaDrawOutside();
        pairArr[4] = TuplesKt.to("drawUIOutsideSafeArea", Boolean.valueOf(safeAreaDrawOutside != null ? safeAreaDrawOutside.booleanValue() : true));
        LinkedHashMap safeArea = MapsKt.linkedMapOf(pairArr);
        result.put("safeArea", safeArea);
        return result;
    }
}
