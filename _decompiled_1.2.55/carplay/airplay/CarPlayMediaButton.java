package com.carplay.airplay;

import kotlin.Metadata;
import kotlinx.coroutines.scheduling.WorkQueueKt;

/* JADX INFO: compiled from: CarPlayMediaButton.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0006\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0005J\u000e\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0005J\u001f\u0010\u0014\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0012\u001a\u00020\u00052\b\b\u0002\u0010\u0015\u001a\u00020\u0011¢\u0006\u0002\u0010\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lcom/carplay/airplay/CarPlayMediaButton;", "", "<init>", "()V", "PLAY", "", "PAUSE", "PLAY_PAUSE", "NEXT", "PREVIOUS", "KEYCODE_BYD_AUTO_MEDIA_PLAY_PAUSE", "KEYCODE_BYD_AUTO_MEDIA_VOICE", "KEYCODE_BYD_AUTO_MEDIA_VOICE_LONG", "KEYCODE_BYD_AUTO_MEDIA_PLAY_PAUSE_DILINK3", "KEYCODE_BYD_CARPLAY_VOICE", "KEYCODE_BYD_CARPLAY_VOICE_LONG", "opensSiri", "", "keyCode", "opensSiriWhileCarPlay", "forKeyCode", "experimentalDiLink3Keys", "(IZ)Ljava/lang/Integer;", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarPlayMediaButton {
    public static final CarPlayMediaButton INSTANCE = new CarPlayMediaButton();
    public static final int KEYCODE_BYD_AUTO_MEDIA_PLAY_PAUSE = 353;
    public static final int KEYCODE_BYD_AUTO_MEDIA_PLAY_PAUSE_DILINK3 = 331;
    public static final int KEYCODE_BYD_AUTO_MEDIA_VOICE = 304;
    public static final int KEYCODE_BYD_AUTO_MEDIA_VOICE_LONG = 312;
    public static final int KEYCODE_BYD_CARPLAY_VOICE = 327;
    public static final int KEYCODE_BYD_CARPLAY_VOICE_LONG = 328;
    public static final int NEXT = 4;
    public static final int PAUSE = 2;
    public static final int PLAY = 1;
    public static final int PLAY_PAUSE = 3;
    public static final int PREVIOUS = 5;

    private CarPlayMediaButton() {
    }

    public final boolean opensSiri(int keyCode) {
        return keyCode == 231 || keyCode == 304 || keyCode == 312;
    }

    public final boolean opensSiriWhileCarPlay(int keyCode) {
        return keyCode == 327 || keyCode == 328;
    }

    public static /* synthetic */ Integer forKeyCode$default(CarPlayMediaButton carPlayMediaButton, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        return carPlayMediaButton.forKeyCode(i, z);
    }

    public final Integer forKeyCode(int keyCode, boolean experimentalDiLink3Keys) {
        switch (keyCode) {
            case 79:
            case ClusterTurnCardOverlay.DEFAULT_OPACITY_PERCENT /* 85 */:
            case 126:
            case WorkQueueKt.MASK /* 127 */:
            case KEYCODE_BYD_AUTO_MEDIA_PLAY_PAUSE /* 353 */:
                return 3;
            case 87:
                return 4;
            case 88:
                return 5;
            case KEYCODE_BYD_AUTO_MEDIA_PLAY_PAUSE_DILINK3 /* 331 */:
                if (!experimentalDiLink3Keys) {
                    return null;
                }
                return 3;
            default:
                return null;
        }
    }
}
