package com.carlink.cast;

import android.view.KeyEvent;
import com.carplay.adapter.PhoneProjectionAdapter;
import com.carplay.adapter.carlife.CarLifePanel;
import com.carplay.adapter.carlife.CarLifeProjectionAdapter;
import com.carplay.sender.HidKeyChange;
import com.carplay.sender.HidKeyReport;
import com.carplay.sender.HidUsage;
import com.carplay.sender.SenderDisplay;
import com.carplay.sender.TouchSample;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlinx.coroutines.scheduling.WorkQueueKt;

/* JADX INFO: compiled from: Dispatch.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010#\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\"\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0004\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J(\u0010\u0007\u001a\u00020\b2\u000e\u0010\t\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\n2\u0006\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eJ,\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00110\u00102\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u000b\u001a\u00020\fH\u0002J8\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u001d\u001a\u00020\u0006H\u0002J\u001c\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010\u00012\b\b\u0002\u0010!\u001a\u00020\u0006H\u0002J\u0006\u0010\"\u001a\u00020\u0019R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00060\u0017X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006#"}, d2 = {"Lcom/carlink/cast/Dispatch;", "", "<init>", "()V", "contacts", "", "", "input", "", "command", "", "source", "Lcom/carplay/adapter/PhoneProjectionAdapter;", "display", "Lcom/carplay/sender/SenderDisplay;", "touchToVideo", "Lkotlin/Pair;", "", CastSettings.KEY_TOUCH, "Lcom/carplay/sender/TouchSample;", "screen", "touchLogged", "MEDIA_KEYS", "", "logTouch", "", "px", "py", "extentX", "extentY", "describe", "", "value", "depth", "reset", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Dispatch {
    private static int touchLogged;
    public static final Dispatch INSTANCE = new Dispatch();
    private static final Set<Integer> contacts = new LinkedHashSet();
    private static final Set<Integer> MEDIA_KEYS = SetsKt.setOf((Object[]) new Integer[]{87, 88, 126, Integer.valueOf(WorkQueueKt.MASK), 85});

    private Dispatch() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final boolean input(Map<?, ?> command, PhoneProjectionAdapter source, SenderDisplay display) {
        String str = "toString(...)";
        Intrinsics.checkNotNullParameter(command, "command");
        Intrinsics.checkNotNullParameter(source, "source");
        try {
            Object obj = command.get("type");
            if (Intrinsics.areEqual(obj, "keySamples")) {
                Object obj2 = command.get("report");
                HidKeyReport hidKeyReport = obj2 instanceof HidKeyReport ? (HidKeyReport) obj2 : null;
                if (hidKeyReport == null) {
                    return false;
                }
                List<HidKeyChange> changes = hidKeyReport.getChanges();
                int i = 0;
                for (HidKeyChange hidKeyChange : changes) {
                    Integer numInput$map = input$map(hidKeyChange.getUsage());
                    CastLog castLog = CastLog.INSTANCE;
                    String string = Integer.toString(hidKeyChange.getUsage().getUsage(), CharsKt.checkRadix(16));
                    Intrinsics.checkNotNullExpressionValue(string, str);
                    String string2 = Integer.toString(hidKeyChange.getUsage().getPage(), CharsKt.checkRadix(16));
                    Intrinsics.checkNotNullExpressionValue(string2, str);
                    castLog.event("INFO", "车机按键 usage=0x" + string + " page=0x" + string2 + " " + (hidKeyChange.getDown() ? "按下" : "松开") + " → " + (numInput$map == null ? "未映射" : numInput$map));
                    str = str;
                    changes = changes;
                    i = i;
                }
                for (HidKeyChange hidKeyChange2 : hidKeyReport.getChanges()) {
                    Integer numInput$map2 = input$map(hidKeyChange2.getUsage());
                    if (numInput$map2 != null) {
                        int iIntValue = numInput$map2.intValue();
                        if (MEDIA_KEYS.contains(Integer.valueOf(iIntValue))) {
                            if (source instanceof CarLifeProjectionAdapter) {
                                if (hidKeyChange2.getDown()) {
                                    ((CarLifeProjectionAdapter) source).onKeyEvent(iIntValue, true);
                                    CastLog.INSTANCE.event("INFO", "CarPlay 媒体键已桥接到 CarLife：" + KeyEvent.keyCodeToString(iIntValue));
                                }
                            } else if (hidKeyChange2.getDown()) {
                                MediaCommandRouter.INSTANCE.dispatch(iIntValue);
                                source.onKeyEvent(iIntValue, true);
                            }
                        } else {
                            source.onKeyEvent(iIntValue, hidKeyChange2.getDown());
                        }
                        CastLog.INSTANCE.input();
                    }
                }
                if (hidKeyReport.getWheel() != 0) {
                    CastLog.INSTANCE.event("INFO", "车机旋钮 wheel=" + hidKeyReport.getWheel());
                }
                int iAbs = Math.abs(hidKeyReport.getWheel());
                for (int i2 = 0; i2 < iAbs; i2++) {
                    int i3 = hidKeyReport.getWheel() < 0 ? 19 : 20;
                    source.onKeyEvent(i3, true);
                    source.onKeyEvent(i3, false);
                    CastLog.INSTANCE.input();
                }
                return true;
            }
            if (Intrinsics.areEqual(obj, "requestSiri")) {
                Object obj3 = command.get("params");
                Map map = obj3 instanceof Map ? (Map) obj3 : null;
                Object obj4 = map != null ? map.get("siriAction") : null;
                Number number = obj4 instanceof Number ? (Number) obj4 : null;
                Integer numValueOf = number != null ? Integer.valueOf(number.intValue()) : null;
                if (!CollectionsKt.contains(SetsKt.setOf((Object[]) new Integer[]{2, 3}), numValueOf)) {
                    return false;
                }
                source.onKeyEvent(231, numValueOf != null && numValueOf.intValue() == 2);
                CastLog.INSTANCE.input();
                return true;
            }
            if (!Intrinsics.areEqual(obj, "requestUI") && !Intrinsics.areEqual(obj, "setNightMode") && !Intrinsics.areEqual(obj, "updateViewArea") && !Intrinsics.areEqual(obj, "setLimitedUI")) {
                if (Intrinsics.areEqual(obj, "touchSamples")) {
                    if (display == null) {
                        return false;
                    }
                    Object obj5 = command.get("samples");
                    List list = obj5 instanceof List ? (List) obj5 : null;
                    if (list == null) {
                        return true;
                    }
                    ArrayList<TouchSample> arrayList = new ArrayList();
                    for (Object obj6 : list) {
                        if (obj6 instanceof TouchSample) {
                            arrayList.add(obj6);
                        }
                    }
                    for (TouchSample touchSample : arrayList) {
                        boolean zContains = contacts.contains(Integer.valueOf(touchSample.getId()));
                        if (touchSample.getDown() || zContains) {
                            int i4 = touchSample.getDown() ? zContains ? 2 : 0 : 1;
                            Pair<Float, Float> pair = INSTANCE.touchToVideo(touchSample, display, source);
                            float fFloatValue = pair.component1().floatValue();
                            float fFloatValue2 = pair.component2().floatValue();
                            if (!Float.isNaN(fFloatValue) && !Float.isNaN(fFloatValue2)) {
                                source.onTouchEvent(i4, fFloatValue, fFloatValue2, touchSample.getId());
                                CastLog.INSTANCE.input();
                            }
                            if (touchSample.getDown()) {
                                contacts.add(Integer.valueOf(touchSample.getId()));
                            } else {
                                contacts.remove(Integer.valueOf(touchSample.getId()));
                            }
                        }
                    }
                    return true;
                }
                if (Intrinsics.areEqual(obj, "hidSendReport")) {
                    Object obj7 = command.get("hidReport");
                    byte[] bArr = obj7 instanceof byte[] ? (byte[]) obj7 : null;
                    if (bArr == null) {
                        return false;
                    }
                    Object obj8 = command.get("uuid");
                    if (!Intrinsics.areEqual(obj8 != null ? obj8.toString() : null, "2a2a2a2a")) {
                        CastLog.INSTANCE.event("INFO", "未支持的车机输入报告：长度 " + bArr.length + "，已确认并忽略");
                        return true;
                    }
                    if (!(bArr.length == 12)) {
                        throw new IllegalArgumentException("Failed requirement.".toString());
                    }
                    Iterator it = CollectionsKt.listOf((Object[]) new Integer[]{0, 6}).iterator();
                    while (it.hasNext()) {
                        int iIntValue2 = ((Number) it.next()).intValue();
                        source.onTouchEvent((bArr[iIntValue2 + 1] & 1) != 0 ? 2 : 1, (bArr[iIntValue2 + 2] & UByte.MAX_VALUE) | ((bArr[iIntValue2 + 3] & UByte.MAX_VALUE) << 8), (bArr[iIntValue2 + 4] & UByte.MAX_VALUE) | ((bArr[iIntValue2 + 5] & UByte.MAX_VALUE) << 8), bArr[iIntValue2] & UByte.MAX_VALUE);
                    }
                    CastLog.INSTANCE.input();
                    return true;
                }
                CastLog.INSTANCE.event("INFO", "车机操作未支持：" + command.get("type"));
                return false;
            }
            try {
                if (Intrinsics.areEqual(command.get("type"), "updateViewArea")) {
                    CastLog.INSTANCE.event("INFO", "车机命令 updateViewArea 参数=" + describe$default(this, command.get("params"), 0, 2, null));
                } else {
                    CastLog.INSTANCE.event("INFO", "车机命令 " + command.get("type") + " 已确认");
                }
                return true;
            } catch (Exception e) {
                e = e;
                CastLog.INSTANCE.event("WARN", "车机操作未执行：" + e.getMessage());
                return false;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    private static final Integer input$map(HidUsage usage) {
        Pair pair = TuplesKt.to(Integer.valueOf(usage.getPage()), Integer.valueOf(usage.getUsage()));
        if (Intrinsics.areEqual(pair, TuplesKt.to(9, 1))) {
            return 23;
        }
        if (Intrinsics.areEqual(pair, TuplesKt.to(12, 547))) {
            return 3;
        }
        if (Intrinsics.areEqual(pair, TuplesKt.to(12, 548))) {
            return 4;
        }
        if (Intrinsics.areEqual(pair, TuplesKt.to(12, 176))) {
            return 126;
        }
        if (Intrinsics.areEqual(pair, TuplesKt.to(12, 177))) {
            return Integer.valueOf(WorkQueueKt.MASK);
        }
        if (Intrinsics.areEqual(pair, TuplesKt.to(12, 205))) {
            return 85;
        }
        if (Intrinsics.areEqual(pair, TuplesKt.to(12, 181))) {
            return 87;
        }
        if (Intrinsics.areEqual(pair, TuplesKt.to(12, 182))) {
            return 88;
        }
        if (!Intrinsics.areEqual(pair, TuplesKt.to(11, 32)) && !Intrinsics.areEqual(pair, TuplesKt.to(11, 33)) && !Intrinsics.areEqual(pair, TuplesKt.to(11, 35))) {
            if (Intrinsics.areEqual(pair, TuplesKt.to(11, 38))) {
                return 6;
            }
            if (Intrinsics.areEqual(pair, TuplesKt.to(11, 46))) {
                return 91;
            }
            return (Intrinsics.areEqual(pair, TuplesKt.to(12, 545)) || Intrinsics.areEqual(pair, TuplesKt.to(12, 207))) ? 231 : null;
        }
        return 5;
    }

    private final Pair<Float, Float> touchToVideo(TouchSample touch, SenderDisplay screen, PhoneProjectionAdapter source) {
        Pair<Float, Float> pairMapDisplayTouchToPanel;
        Integer numValueOf = Integer.valueOf(touch.getMaxX());
        if ((numValueOf.intValue() > 0 ? 1 : 0) == 0) {
            numValueOf = null;
        }
        int extentX = numValueOf != null ? numValueOf.intValue() : screen.getWidth();
        Integer numValueOf2 = Integer.valueOf(touch.getMaxY());
        Integer num = (numValueOf2.intValue() > 0 ? 1 : 0) != 0 ? numValueOf2 : null;
        int extentY = num != null ? num.intValue() : screen.getHeight();
        if (source instanceof CarLifeProjectionAdapter) {
            CarLifePanel panel = ((CarLifeProjectionAdapter) source).get_panel();
            if (panel == null) {
                panel = CarPanelGeometry.INSTANCE.panelFor(screen);
            }
            pairMapDisplayTouchToPanel = CarPanelGeometry.INSTANCE.mapDisplayTouchToPanel(touch.getX(), touch.getY(), panel);
            if (pairMapDisplayTouchToPanel == null) {
                return TuplesKt.to(Float.valueOf(Float.NaN), Float.valueOf(Float.NaN));
            }
        } else {
            pairMapDisplayTouchToPanel = TuplesKt.to(Float.valueOf(touch.getX() * screen.getWidth()), Float.valueOf(touch.getY() * screen.getHeight()));
        }
        float scaledX = pairMapDisplayTouchToPanel.component1().floatValue();
        float scaledY = pairMapDisplayTouchToPanel.component2().floatValue();
        if (0.0f <= scaledX && scaledX <= ((float) screen.getWidth())) {
            if (0.0f <= scaledY && scaledY <= ((float) screen.getHeight())) {
                logTouch(touch, screen, scaledX, scaledY, extentX, extentY);
                return TuplesKt.to(Float.valueOf(scaledX), Float.valueOf(scaledY));
            }
        }
        logTouch(touch, screen, scaledX, scaledY, extentX, extentY);
        return TuplesKt.to(Float.valueOf(Float.NaN), Float.valueOf(Float.NaN));
    }

    private final void logTouch(TouchSample touch, SenderDisplay screen, float px, float py, int extentX, int extentY) {
        int index = touchLogged;
        touchLogged = index + 1;
        if (index < 12 || index % 40 == 0) {
            CastLog castLog = CastLog.INSTANCE;
            int id = touch.getId();
            String str = touch.getDown() ? "按下" : "抬起";
            int iRound = Math.round(touch.getX() * extentX);
            int iRound2 = Math.round(touch.getY() * extentY);
            String str2 = String.format("%.4f", Arrays.copyOf(new Object[]{Float.valueOf(touch.getX())}, 1));
            Intrinsics.checkNotNullExpressionValue(str2, "format(...)");
            String str3 = String.format("%.4f", Arrays.copyOf(new Object[]{Float.valueOf(touch.getY())}, 1));
            Intrinsics.checkNotNullExpressionValue(str3, "format(...)");
            castLog.event("INFO", "触摸 #" + index + " id=" + id + " " + str + " 原始=(" + iRound + ", " + iRound2 + ") 归一化=(" + str2 + ", " + str3 + ") 量程=" + extentX + "×" + extentY + " → 画面=(" + ((int) px) + ", " + ((int) py) + ") / " + screen.getWidth() + "×" + screen.getHeight());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String describe(Object value, final int depth) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Map) {
            return CollectionsKt.joinToString$default(((Map) value).entrySet(), ", ", "{", "}", 0, null, new Function1() { // from class: com.carlink.cast.Dispatch$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Dispatch.describe$lambda$6(depth, (Map.Entry) obj);
                }
            }, 24, null);
        }
        if (value instanceof List) {
            return CollectionsKt.joinToString$default((Iterable) value, ", ", "[", "]", 0, null, new Function1() { // from class: com.carlink.cast.Dispatch$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Dispatch.INSTANCE.describe(obj, depth + 1);
                }
            }, 24, null);
        }
        return value instanceof byte[] ? ArraysKt.joinToString$default((byte[]) value, (CharSequence) "", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, new Function1() { // from class: com.carlink.cast.Dispatch$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Dispatch.describe$lambda$8(((Byte) obj).byteValue());
            }
        }, 30, (Object) null) : value.toString();
    }

    static /* synthetic */ String describe$default(Dispatch dispatch, Object obj, int i, int i2, Object obj2) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return dispatch.describe(obj, i);
    }

    static final CharSequence describe$lambda$6(int $depth, Map.Entry entry) {
        Intrinsics.checkNotNullParameter(entry, "<destruct>");
        Object key = entry.getKey();
        Object item = entry.getValue();
        return key + "=" + INSTANCE.describe(item, $depth + 1);
    }

    static final CharSequence describe$lambda$8(byte it) {
        String str = String.format("%02X", Arrays.copyOf(new Object[]{Byte.valueOf(it)}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    public final void reset() {
        contacts.clear();
    }
}
