package com.carlink.cast;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Path;
import android.net.Uri;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.app.NotificationCompat;
import com.carplay.airplay.ClusterTurnCardOverlay;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function4;

/* JADX INFO: compiled from: TouchBridge.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\t\u001a\u00020\nH\u0014J\b\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\fH\u0002J\u0012\u0010\u000e\u001a\u00020\n2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0016J\b\u0010\u0011\u001a\u00020\nH\u0016J\b\u0010\u0012\u001a\u00020\nH\u0016R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lcom/carlink/cast/TouchService;", "Landroid/accessibilityservice/AccessibilityService;", "<init>", "()V", "stroke", "Landroid/accessibilityservice/GestureDescription$StrokeDescription;", "lastX", "", "lastY", "onServiceConnected", "", "openDialer", "", "launchVoiceSearch", "onAccessibilityEvent", NotificationCompat.CATEGORY_EVENT, "Landroid/view/accessibility/AccessibilityEvent;", "onInterrupt", "onDestroy", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class TouchService extends AccessibilityService {
    private float lastX;
    private float lastY;
    private GestureDescription.StrokeDescription stroke;

    @Override // android.accessibilityservice.AccessibilityService
    protected void onServiceConnected() {
        TouchBridge.INSTANCE.setTouch(new Function4() { // from class: com.carlink.cast.TouchService$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function4
            public final Object invoke(Object obj, Object obj2, Object obj3, Object obj4) {
                return Boolean.valueOf(TouchService.onServiceConnected$lambda$1(this.f$0, ((Integer) obj).intValue(), ((Float) obj2).floatValue(), ((Float) obj3).floatValue(), ((Integer) obj4).intValue()));
            }
        });
        TouchBridge.INSTANCE.setKey(new Function2() { // from class: com.carlink.cast.TouchService$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return Boolean.valueOf(TouchService.onServiceConnected$lambda$2(this.f$0, ((Integer) obj).intValue(), ((Boolean) obj2).booleanValue()));
            }
        });
    }

    static final boolean onServiceConnected$lambda$1(TouchService this$0, int action, float x, float y, int pointer) {
        GestureDescription.StrokeDescription next;
        boolean accepted = false;
        if (CastLog.INSTANCE.getState().getValue().getRunning() && pointer == 0) {
            Path path = new Path();
            path.moveTo(this$0.stroke == null ? x : this$0.lastX, this$0.stroke == null ? y : this$0.lastY);
            path.lineTo(x, y);
            boolean continuing = action != 1;
            GestureDescription.StrokeDescription strokeDescription = this$0.stroke;
            if (strokeDescription == null || (next = strokeDescription.continueStroke(path, 0L, 32L, continuing)) == null) {
                next = new GestureDescription.StrokeDescription(path, 0L, 32L, continuing);
            }
            accepted = this$0.dispatchGesture(new GestureDescription.Builder().addStroke(next).build(), null, null);
            if (accepted) {
                this$0.stroke = continuing ? next : null;
                this$0.lastX = x;
                this$0.lastY = y;
            }
        }
        return accepted;
    }

    static final boolean onServiceConnected$lambda$2(TouchService this$0, int code, boolean down) {
        if (!CastLog.INSTANCE.getState().getValue().getRunning()) {
            return false;
        }
        if (!down) {
            return true;
        }
        switch (code) {
            case 3:
                return this$0.performGlobalAction(2);
            case 4:
                return this$0.performGlobalAction(1);
            case 5:
                return this$0.openDialer();
            case 6:
                return this$0.performGlobalAction(1);
            case ClusterTurnCardOverlay.DEFAULT_OPACITY_PERCENT /* 85 */:
            default:
                return false;
            case 231:
                return this$0.launchVoiceSearch();
        }
    }

    private final boolean openDialer() {
        Object objM159constructorimpl;
        Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:"));
        intent.addFlags(268435456);
        try {
            Result.Companion companion = Result.INSTANCE;
            startActivity(intent);
            objM159constructorimpl = Result.m159constructorimpl(true);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (thM162exceptionOrNullimpl != null) {
            CastLog.INSTANCE.event("WARN", "打开拨号盘失败：" + thM162exceptionOrNullimpl.getMessage());
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = false;
        }
        return ((Boolean) objM159constructorimpl).booleanValue();
    }

    private final boolean launchVoiceSearch() {
        Object objM159constructorimpl;
        for (String action : CollectionsKt.listOf((Object[]) new String[]{"android.intent.action.ASSIST", "android.intent.action.VOICE_COMMAND"})) {
            Intent intent = new Intent(action);
            intent.addFlags(268435456);
            try {
                Result.Companion companion = Result.INSTANCE;
                startActivity(intent);
                objM159constructorimpl = Result.m159constructorimpl(true);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            if (Result.m165isFailureimpl(objM159constructorimpl)) {
                objM159constructorimpl = false;
            }
            if (((Boolean) objM159constructorimpl).booleanValue()) {
                return true;
            }
        }
        CastLog.INSTANCE.event("WARN", "本机没有可用的语音助手");
        return false;
    }

    @Override // android.accessibilityservice.AccessibilityService
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override // android.accessibilityservice.AccessibilityService
    public void onInterrupt() {
        this.stroke = null;
    }

    @Override // android.app.Service
    public void onDestroy() {
        TouchBridge.INSTANCE.setTouch(null);
        TouchBridge.INSTANCE.setKey(null);
        this.stroke = null;
        super.onDestroy();
    }
}
