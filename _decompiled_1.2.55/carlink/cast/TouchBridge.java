package com.carlink.cast;

import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function4;

/* JADX INFO: compiled from: TouchBridge.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0007\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000e\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0006\u0010\u0013\u001a\u00020\bJ&\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0018\u001a\u00020\u0006J\u0016\u0010\u0019\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u001b\u001a\u00020\bR:\u0010\u0004\u001a\"\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\b\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR.\u0010\r\u001a\u0016\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b\u0018\u00010\u000eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012¨\u0006\u001c"}, d2 = {"Lcom/carlink/cast/TouchBridge;", "", "<init>", "()V", CastSettings.KEY_TOUCH, "Lkotlin/Function4;", "", "", "", "getTouch", "()Lkotlin/jvm/functions/Function4;", "setTouch", "(Lkotlin/jvm/functions/Function4;)V", "key", "Lkotlin/Function2;", "getKey", "()Lkotlin/jvm/functions/Function2;", "setKey", "(Lkotlin/jvm/functions/Function2;)V", "available", "dispatch", "action", "x", "y", "pointerId", "dispatchKey", "keyCode", "isDown", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class TouchBridge {
    public static final TouchBridge INSTANCE = new TouchBridge();
    private static volatile Function2<? super Integer, ? super Boolean, Boolean> key;
    private static volatile Function4<? super Integer, ? super Float, ? super Float, ? super Integer, Boolean> touch;

    private TouchBridge() {
    }

    public final Function4<Integer, Float, Float, Integer, Boolean> getTouch() {
        return touch;
    }

    public final void setTouch(Function4<? super Integer, ? super Float, ? super Float, ? super Integer, Boolean> function4) {
        touch = function4;
    }

    public final Function2<Integer, Boolean, Boolean> getKey() {
        return key;
    }

    public final void setKey(Function2<? super Integer, ? super Boolean, Boolean> function2) {
        key = function2;
    }

    public final boolean available() {
        return touch != null;
    }

    public final boolean dispatch(int action, float x, float y, int pointerId) {
        Function4<? super Integer, ? super Float, ? super Float, ? super Integer, Boolean> function4 = touch;
        if (function4 != null) {
            return function4.invoke(Integer.valueOf(action), Float.valueOf(x), Float.valueOf(y), Integer.valueOf(pointerId)).booleanValue();
        }
        return false;
    }

    public final boolean dispatchKey(int keyCode, boolean isDown) {
        Function2<? super Integer, ? super Boolean, Boolean> function2 = key;
        if (function2 != null) {
            return function2.invoke(Integer.valueOf(keyCode), Boolean.valueOf(isDown)).booleanValue();
        }
        return false;
    }
}
