package com.carplay.adapter.carlife;

import android.os.Build;
import android.os.Bundle;
import com.baidu.carlife.mixing.MusicInfo;
import com.carlink.cast.CastSettings;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CarLifeHostService.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b+\n\u0002\u0018\u0002\n\u0002\b\t\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u00100\u001a\u00020\u00072\u0006\u00101\u001a\u00020)J\u0018\u00102\u001a\u00020\u00072\u0006\u00103\u001a\u00020-2\b\b\u0002\u00104\u001a\u00020\u0006J\u0006\u0010T\u001a\u00020\u0007J\u0006\u0010U\u001a\u00020\u0007J\u0006\u0010V\u001a\u00020\u0007J\u0006\u0010W\u001a\u00020\u0007J\u001a\u0010X\u001a\u0004\u0018\u00010Y2\u0006\u0010Z\u001a\u00020\u00062\b\u0010[\u001a\u0004\u0018\u00010YJ\u0006\u0010`\u001a\u00020\u0007J\u0006\u0010a\u001a\u00020\u0007R&\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR4\u0010\f\u001a\u001c\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u0007\u0018\u00010\rX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R.\u0010\u0014\u001a\u0016\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0015X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR(\u0010\u001b\u001a\u0010\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u0016\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\t\"\u0004\b\u001d\u0010\u000bR.\u0010\u001e\u001a\u0016\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0015X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0018\"\u0004\b!\u0010\u001aR\"\u0010\"\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010#X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b$\u0010%\"\u0004\b&\u0010'R(\u0010(\u001a\u0010\u0012\u0004\u0012\u00020)\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b*\u0010\t\"\u0004\b+\u0010\u000bR(\u0010,\u001a\u0010\u0012\u0004\u0012\u00020-\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010\t\"\u0004\b/\u0010\u000bR\u001e\u00106\u001a\u00020\u00162\u0006\u00105\u001a\u00020\u0016@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b7\u00108R\u001e\u00109\u001a\u00020\u00162\u0006\u00105\u001a\u00020\u0016@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b:\u00108R\u001a\u0010;\u001a\u00020\u000eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b<\u0010=\"\u0004\b>\u0010?R\u001a\u0010@\u001a\u00020\u000eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bA\u0010=\"\u0004\bB\u0010?R\u001a\u0010C\u001a\u00020\u000eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bD\u0010=\"\u0004\bE\u0010?R\u001a\u0010F\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bG\u0010H\"\u0004\bI\u0010JR\u001a\u0010K\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bL\u0010H\"\u0004\bM\u0010JR\u001a\u0010N\u001a\u00020\u000eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bO\u0010=\"\u0004\bP\u0010?R\u001a\u0010Q\u001a\u00020\u000eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bR\u0010=\"\u0004\bS\u0010?R\u000e\u0010\\\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010]\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010^\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010_\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000¨\u0006b"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeBridge;", "", "<init>", "()V", "log", "Lkotlin/Function1;", "", "", "getLog", "()Lkotlin/jvm/functions/Function1;", "setLog", "(Lkotlin/jvm/functions/Function1;)V", CastSettings.KEY_TOUCH, "Lkotlin/Function3;", "", "", "getTouch", "()Lkotlin/jvm/functions/Function3;", "setTouch", "(Lkotlin/jvm/functions/Function3;)V", "key", "Lkotlin/Function2;", "", "getKey", "()Lkotlin/jvm/functions/Function2;", "setKey", "(Lkotlin/jvm/functions/Function2;)V", "mediaKeyDispatcher", "getMediaKeyDispatcher", "setMediaKeyDispatcher", CastSettings.KEY_AUDIO, "", "getAudio", "setAudio", "stop", "Lkotlin/Function0;", "getStop", "()Lkotlin/jvm/functions/Function0;", "setStop", "(Lkotlin/jvm/functions/Function0;)V", "musicInfo", "Lcom/baidu/carlife/mixing/MusicInfo;", "getMusicInfo", "setMusicInfo", "musicProgress", "", "getMusicProgress", "setMusicProgress", "publishMusic", "info", "publishMusicProgress", "positionMs", "lyrics", "value", "bound", "getBound", "()Z", "callbackReady", "getCallbackReady", "width", "getWidth", "()I", "setWidth", "(I)V", "height", "getHeight", "setHeight", "fps", "getFps", "setFps", "deviceId", "getDeviceId", "()Ljava/lang/String;", "setDeviceId", "(Ljava/lang/String;)V", "deviceVCode", "getDeviceVCode", "setDeviceVCode", "castWidth", "getCastWidth", "setCastWidth", "castHeight", "getCastHeight", "setCastHeight", "markBound", "markCallbackReady", "markCallbackUnready", "markUnbound", "invoke", "Landroid/os/Bundle;", "action", "extras", "CARLIFE_ENCODER_FORMAT", "CARLIFE_PARAMS", "CARLIFE_REQUEST_DEVICEID", "CARLIFE_GET_BT_ADDRESS", "clear", "resetForNewSession", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarLifeBridge {
    private static final String CARLIFE_ENCODER_FORMAT = "carlife_encode_format";
    private static final String CARLIFE_GET_BT_ADDRESS = "get_bt_address";
    private static final String CARLIFE_PARAMS = "params";
    private static final String CARLIFE_REQUEST_DEVICEID = "carlife_request_device_id";
    private static volatile Function2<? super byte[], ? super Integer, Unit> audio;
    private static volatile boolean bound;
    private static volatile boolean callbackReady;
    private static volatile int castHeight;
    private static volatile int castWidth;
    private static volatile int height;
    private static volatile Function2<? super Integer, ? super Boolean, Unit> key;
    private static volatile Function1<? super Integer, Boolean> mediaKeyDispatcher;
    private static volatile Function1<? super MusicInfo, Unit> musicInfo;
    private static volatile Function1<? super Long, Unit> musicProgress;
    private static volatile Function0<Unit> stop;
    private static volatile Function3<? super Integer, ? super Float, ? super Float, Unit> touch;
    private static volatile int width;
    public static final CarLifeBridge INSTANCE = new CarLifeBridge();
    private static volatile Function1<? super String, Unit> log = new Function1() { // from class: com.carplay.adapter.carlife.CarLifeBridge$$ExternalSyntheticLambda1
        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Object obj) {
            return CarLifeBridge.log$lambda$0((String) obj);
        }
    };
    private static volatile int fps = 30;
    private static volatile String deviceId = "";
    private static volatile String deviceVCode = "";

    private CarLifeBridge() {
    }

    static final Unit log$lambda$0(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    public final Function1<String, Unit> getLog() {
        return log;
    }

    public final void setLog(Function1<? super String, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "<set-?>");
        log = function1;
    }

    public final Function3<Integer, Float, Float, Unit> getTouch() {
        return touch;
    }

    public final void setTouch(Function3<? super Integer, ? super Float, ? super Float, Unit> function3) {
        touch = function3;
    }

    public final Function2<Integer, Boolean, Unit> getKey() {
        return key;
    }

    public final void setKey(Function2<? super Integer, ? super Boolean, Unit> function2) {
        key = function2;
    }

    public final Function1<Integer, Boolean> getMediaKeyDispatcher() {
        return mediaKeyDispatcher;
    }

    public final void setMediaKeyDispatcher(Function1<? super Integer, Boolean> function1) {
        mediaKeyDispatcher = function1;
    }

    public final Function2<byte[], Integer, Unit> getAudio() {
        return audio;
    }

    public final void setAudio(Function2<? super byte[], ? super Integer, Unit> function2) {
        audio = function2;
    }

    public final Function0<Unit> getStop() {
        return stop;
    }

    public final void setStop(Function0<Unit> function0) {
        stop = function0;
    }

    public final Function1<MusicInfo, Unit> getMusicInfo() {
        return musicInfo;
    }

    public final void setMusicInfo(Function1<? super MusicInfo, Unit> function1) {
        musicInfo = function1;
    }

    public final Function1<Long, Unit> getMusicProgress() {
        return musicProgress;
    }

    public final void setMusicProgress(Function1<? super Long, Unit> function1) {
        musicProgress = function1;
    }

    public final void publishMusic(MusicInfo info) {
        Unit unit;
        Intrinsics.checkNotNullParameter(info, "info");
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLifeBridge carLifeBridge = this;
            Function1<? super MusicInfo, Unit> function1 = musicInfo;
            if (function1 != null) {
                function1.invoke(info);
                unit = Unit.INSTANCE;
            } else {
                unit = null;
            }
            Result.m159constructorimpl(unit);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
    }

    public static /* synthetic */ void publishMusicProgress$default(CarLifeBridge carLifeBridge, long j, String str, int i, Object obj) {
        if ((i & 2) != 0) {
            str = "";
        }
        carLifeBridge.publishMusicProgress(j, str);
    }

    public final void publishMusicProgress(long positionMs, String lyrics) {
        Unit unit;
        Intrinsics.checkNotNullParameter(lyrics, "lyrics");
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLifeBridge carLifeBridge = this;
            Function1<? super Long, Unit> function1 = musicProgress;
            if (function1 != null) {
                function1.invoke(Long.valueOf(positionMs));
                unit = Unit.INSTANCE;
            } else {
                unit = null;
            }
            Result.m159constructorimpl(unit);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
    }

    public final boolean getBound() {
        return bound;
    }

    public final boolean getCallbackReady() {
        return callbackReady;
    }

    public final int getWidth() {
        return width;
    }

    public final void setWidth(int i) {
        width = i;
    }

    public final int getHeight() {
        return height;
    }

    public final void setHeight(int i) {
        height = i;
    }

    public final int getFps() {
        return fps;
    }

    public final void setFps(int i) {
        fps = i;
    }

    public final String getDeviceId() {
        return deviceId;
    }

    public final void setDeviceId(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        deviceId = str;
    }

    public final String getDeviceVCode() {
        return deviceVCode;
    }

    public final void setDeviceVCode(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        deviceVCode = str;
    }

    public final int getCastWidth() {
        return castWidth;
    }

    public final void setCastWidth(int i) {
        castWidth = i;
    }

    public final int getCastHeight() {
        return castHeight;
    }

    public final void setCastHeight(int i) {
        castHeight = i;
    }

    public final void markBound() {
        if (bound) {
            return;
        }
        bound = true;
        log.invoke("CarLife 已绑定宿主服务");
    }

    public final void markCallbackReady() {
        if (callbackReady) {
            return;
        }
        callbackReady = true;
        log.invoke("CarLife CastCallback 已注册");
    }

    public final void markCallbackUnready() {
        if (callbackReady) {
            callbackReady = false;
            log.invoke("CarLife CastCallback 已失效");
        }
    }

    public final void markUnbound() {
        if (bound) {
            bound = false;
            callbackReady = false;
            log.invoke("CarLife 已断开宿主服务");
        }
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public final Bundle invoke(String action, Bundle extras) {
        String params;
        Intrinsics.checkNotNullParameter(action, "action");
        switch (action.hashCode()) {
            case -2087108212:
                if (!action.equals("carlife_music_sample_rate")) {
                    return null;
                }
                log.invoke("CarLife 音乐采样率查询：返回 48000Hz");
                Bundle $this$invoke_u24lambda_u244 = new Bundle();
                $this$invoke_u24lambda_u244.putInt("music_sample_rate", 48000);
                return $this$invoke_u24lambda_u244;
            case -2042684143:
                if (!action.equals(CARLIFE_ENCODER_FORMAT)) {
                    return null;
                }
                String params2 = extras != null ? extras.getString(CARLIFE_PARAMS) : null;
                CarLifeEncoderFormat format = CarLifeEncoderFormat.INSTANCE.fromJson(params2);
                if (format == null) {
                    log.invoke("忽略无效的 CarLife 编码安全区参数");
                    return null;
                }
                Map<String, Object> mapMediaFormatOverrides = format.mediaFormatOverrides();
                log.invoke("CarLife 编码安全区回调：面板 " + format.getPanelWidth() + "x" + format.getPanelHeight() + "，安全区 " + format.getSafeWidth() + "x" + format.getSafeHeight() + "@(" + format.getSafeX() + "," + format.getSafeY() + ")；保持 CarLife 编码宽高/FPS不变");
                Bundle $this$invoke_u24lambda_u249 = new Bundle();
                for (Map.Entry<String, Object> entry : mapMediaFormatOverrides.entrySet()) {
                    String key2 = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof Integer) {
                        $this$invoke_u24lambda_u249.putInt(key2, ((Number) value).intValue());
                        params = params2;
                    } else if (value instanceof Boolean) {
                        $this$invoke_u24lambda_u249.putBoolean(key2, ((Boolean) value).booleanValue());
                        params = params2;
                    } else if (value instanceof Long) {
                        params = params2;
                        $this$invoke_u24lambda_u249.putLong(key2, ((Number) value).longValue());
                    } else {
                        params = params2;
                        if (value instanceof Float) {
                            $this$invoke_u24lambda_u249.putFloat(key2, ((Number) value).floatValue());
                        } else {
                            $this$invoke_u24lambda_u249.putString(key2, value.toString());
                        }
                    }
                    params2 = params;
                }
                return $this$invoke_u24lambda_u249;
            case -1502654838:
                if (!action.equals("carlife_vr_use_phone_mic")) {
                    return null;
                }
                log.invoke("CarLife 麦克风查询：返回手机麦克风");
                Bundle $this$invoke_u24lambda_u245 = new Bundle();
                $this$invoke_u24lambda_u245.putBoolean("is_use_phone_mic", true);
                return $this$invoke_u24lambda_u245;
            case 868186183:
                if (!action.equals("carlife_connect_audio_type")) {
                    return null;
                }
                int audioType = extras != null ? extras.getInt("audioType", 1) : 1;
                log.invoke("CarLife 音频通道通知：audioType=" + audioType + "，保持 CarPlay 媒体音频由车机协商");
                Bundle $this$invoke_u24lambda_u243 = new Bundle();
                $this$invoke_u24lambda_u243.putInt("audioType", audioType);
                return $this$invoke_u24lambda_u243;
            case 1856203845:
                if (!action.equals(CARLIFE_REQUEST_DEVICEID)) {
                    return null;
                }
                Bundle $this$invoke_u24lambda_u246 = new Bundle();
                $this$invoke_u24lambda_u246.putString("device_id", deviceId);
                $this$invoke_u24lambda_u246.putString("device_vcode", deviceVCode);
                $this$invoke_u24lambda_u246.putString("app_version", "1.0.0");
                String str = Build.DISPLAY;
                if (str == null) {
                    str = "";
                }
                $this$invoke_u24lambda_u246.putString("rom_version", str);
                return $this$invoke_u24lambda_u246;
            default:
                return null;
        }
    }

    static final Unit clear$lambda$10(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    public final void clear() {
        log = new Function1() { // from class: com.carplay.adapter.carlife.CarLifeBridge$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return CarLifeBridge.clear$lambda$10((String) obj);
            }
        };
        touch = null;
        key = null;
        audio = null;
        stop = null;
        musicInfo = null;
        musicProgress = null;
        callbackReady = false;
    }

    public final void resetForNewSession() {
        clear();
        bound = false;
        callbackReady = false;
        castWidth = 0;
        castHeight = 0;
    }
}
