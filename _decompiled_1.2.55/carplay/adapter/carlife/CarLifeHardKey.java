package com.carplay.adapter.carlife;

import android.view.KeyEvent;
import com.carplay.airplay.CarPlayDisplayScale;
import com.carplay.airplay.ClusterTurnCardOverlay;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.scheduling.WorkQueueKt;

/* JADX INFO: compiled from: CarLifeHardKey.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u001d\n\u0002\u0010\u000e\n\u0000\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0015\u0010\u001d\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u001e\u001a\u00020\u0005¢\u0006\u0002\u0010\u001fJ\u0015\u0010 \u001a\u0004\u0018\u00010\u00052\u0006\u0010!\u001a\u00020\u0005¢\u0006\u0002\u0010\u001fJ\u000e\u0010\"\u001a\u00020#2\u0006\u0010\u001e\u001a\u00020\u0005R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006$"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeHardKey;", "", "<init>", "()V", "ANSWER", "", "HANG_UP", "KNOB_CLOCKWISE", "KNOB_ANTICLOCKWISE", "BACK", "OK", "LEFT", "RIGHT", "UP", "DOWN", "LEFT_UP", "RIGHT_UP", "LEFT_DOWN", "RIGHT_DOWN", "PLAY", "STOP", "NEXT", "PREVIOUS", "DIGIT_BASE", "DELETE", "CLEAR", "PLUS", "VR_START", "VR_STOP", "fromAndroid", "keyCode", "(I)Ljava/lang/Integer;", "toAndroid", "carLifeCode", "describe", "", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarLifeHardKey {
    public static final int ANSWER = 2;
    public static final int BACK = 14;
    public static final int CLEAR = 48;
    public static final int DELETE = 47;
    public static final int DIGIT_BASE = 35;
    public static final int DOWN = 24;
    public static final int HANG_UP = 3;
    public static final CarLifeHardKey INSTANCE = new CarLifeHardKey();
    public static final int KNOB_ANTICLOCKWISE = 7;
    public static final int KNOB_CLOCKWISE = 6;
    public static final int LEFT = 21;
    public static final int LEFT_DOWN = 27;
    public static final int LEFT_UP = 25;
    public static final int NEXT = 29;
    public static final int OK = 20;
    public static final int PLAY = 31;
    public static final int PLUS = 49;
    public static final int PREVIOUS = 30;
    public static final int RIGHT = 22;
    public static final int RIGHT_DOWN = 28;
    public static final int RIGHT_UP = 26;
    public static final int STOP = 32;
    public static final int UP = 23;
    public static final int VR_START = 33;
    public static final int VR_STOP = 34;

    private CarLifeHardKey() {
    }

    public final Integer fromAndroid(int keyCode) {
        switch (keyCode) {
            case 3:
                return 14;
            case 4:
                return 14;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 35;
            case 8:
                return 36;
            case 9:
                return 37;
            case 10:
                return 38;
            case 11:
                return 39;
            case 12:
                return 40;
            case 13:
                return 41;
            case 14:
                return 42;
            case 15:
                return 43;
            case 16:
                return 44;
            case 19:
                return 23;
            case 20:
                return 24;
            case 21:
                return 21;
            case 22:
                return 22;
            case 23:
                return 20;
            case 66:
                return 20;
            case 67:
                return 47;
            case ClusterTurnCardOverlay.DEFAULT_OPACITY_PERCENT /* 85 */:
                return 31;
            case 86:
            case WorkQueueKt.MASK /* 127 */:
                return 32;
            case 87:
                return 29;
            case 88:
                return 30;
            case 111:
                return 14;
            case 126:
                return 31;
            case CarPlayDisplayScale.MAX_PERCENT /* 160 */:
                return 20;
            case 231:
                return 33;
            default:
                return null;
        }
    }

    public final Integer toAndroid(int carLifeCode) {
        switch (carLifeCode) {
            case 2:
                return 5;
            case 3:
                return 6;
            case 14:
                return 4;
            case 20:
                return 23;
            case 21:
                return 21;
            case 22:
                return 22;
            case 23:
                return 19;
            case 24:
                return 20;
            case 29:
                return 87;
            case 30:
                return 88;
            case 31:
                return 126;
            case 32:
                return 86;
            case 33:
                return 231;
            default:
                return null;
        }
    }

    public final String describe(int keyCode) {
        String strKeyCodeToString = KeyEvent.keyCodeToString(keyCode);
        Intrinsics.checkNotNullExpressionValue(strKeyCodeToString, "keyCodeToString(...)");
        return strKeyCodeToString;
    }
}
