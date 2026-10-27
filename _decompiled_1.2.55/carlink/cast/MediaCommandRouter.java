package com.carlink.cast;

import android.content.Context;
import android.media.AudioManager;
import android.os.SystemClock;
import android.view.KeyEvent;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.scheduling.WorkQueueKt;

/* JADX INFO: compiled from: MediaCommandRouter.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\"\n\u0000\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0005J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fR\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u000eX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lcom/carlink/cast/MediaCommandRouter;", "", "<init>", "()V", "appContext", "Landroid/content/Context;", "initialize", "", "context", "dispatch", "", "keyCode", "", "SUPPORTED_MEDIA_KEYS", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MediaCommandRouter {
    public static final MediaCommandRouter INSTANCE = new MediaCommandRouter();
    private static final Set<Integer> SUPPORTED_MEDIA_KEYS = SetsKt.setOf((Object[]) new Integer[]{87, 88, 126, Integer.valueOf(WorkQueueKt.MASK), 85});
    private static volatile Context appContext;

    private MediaCommandRouter() {
    }

    public final void initialize(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        appContext = context.getApplicationContext();
    }

    public final boolean dispatch(int keyCode) {
        AudioManager audio;
        int keyCode2;
        Throwable th;
        Object objM159constructorimpl;
        Context context = appContext;
        if (context == null || (audio = (AudioManager) context.getSystemService(AudioManager.class)) == null || !SUPPORTED_MEDIA_KEYS.contains(Integer.valueOf(keyCode))) {
            return false;
        }
        long now = SystemClock.uptimeMillis();
        try {
            Result.Companion companion = Result.INSTANCE;
            MediaCommandRouter mediaCommandRouter = this;
            keyCode2 = keyCode;
            try {
                audio.dispatchMediaKeyEvent(new KeyEvent(now, now, 0, keyCode2, 0));
                audio.dispatchMediaKeyEvent(new KeyEvent(now, now, 1, keyCode2, 0));
                objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th2) {
                th = th2;
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
        } catch (Throwable th3) {
            keyCode2 = keyCode;
            th = th3;
        }
        Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (thM162exceptionOrNullimpl != null) {
            CastLog.INSTANCE.event("WARN", "媒体按键转发失败：" + thM162exceptionOrNullimpl.getClass().getSimpleName() + ": " + thM162exceptionOrNullimpl.getMessage());
        }
        boolean success = Result.m166isSuccessimpl(objM159constructorimpl);
        if (success) {
            CastLog.INSTANCE.event("INFO", "车机媒体键已按 E5 路由注入：" + KeyEvent.keyCodeToString(keyCode2));
        }
        return success;
    }
}
