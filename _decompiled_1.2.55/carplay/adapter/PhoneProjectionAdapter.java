package com.carplay.adapter;

import android.content.Context;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PhoneProjectionAdapter.kt */
/* JADX INFO: loaded from: classes11.dex */
@Metadata(d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0002\b\b\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH&J\u0010\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\fH&J\"\u0010\r\u001a\u00020\u00072\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\b\b\u0002\u0010\u0011\u001a\u00020\u0012H&J\b\u0010\u0013\u001a\u00020\u0007H&J\b\u0010\u0014\u001a\u00020\u0007H\u0016J \u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0011\u001a\u00020\u00162\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH&J\u0010\u0010\u001e\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020 H&J\u0010\u0010!\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020 H\u0016J.\u0010\"\u001a\u00020\u00122\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010#\u001a\u00020\u000f2\u0006\u0010$\u001a\u00020\u000f2\u0006\u0010%\u001a\u00020\u000fH¦@¢\u0006\u0002\u0010&J[\u0010'\u001a\u00020\u00072K\u0010(\u001aG\u0012\u0013\u0012\u00110\u0016¢\u0006\f\b*\u0012\b\b\u0017\u0012\u0004\b\b(+\u0012\u0013\u0012\u00110,¢\u0006\f\b*\u0012\b\b\u0017\u0012\u0004\b\b(-\u0012\u0013\u0012\u00110\u0012¢\u0006\f\b*\u0012\b\b\u0017\u0012\u0004\b\b(.\u0012\u0004\u0012\u00020\u00070)H¦@¢\u0006\u0002\u0010/J(\u00100\u001a\u00020\u00072\u0006\u00101\u001a\u00020\u000f2\u0006\u00102\u001a\u0002032\u0006\u00104\u001a\u0002032\u0006\u00105\u001a\u00020\u000fH&J\u0018\u00106\u001a\u00020\u00072\u0006\u00107\u001a\u00020\u000f2\u0006\u00108\u001a\u00020\u0012H&J\u000e\u00109\u001a\u00020\u0007H¦@¢\u0006\u0002\u0010:R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0017\u001a\u00020\u0018X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001aR\u0014\u0010\u001b\u001a\u00020\u00128VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u001d¨\u0006;À\u0006\u0003"}, d2 = {"Lcom/carplay/adapter/PhoneProjectionAdapter;", "", "capabilities", "Lcom/carplay/adapter/ProjectionCapabilities;", "getCapabilities", "()Lcom/carplay/adapter/ProjectionCapabilities;", "setObserver", "", "observer", "Lcom/carplay/adapter/ProjectionObserver;", "setGrant", "grant", "Lcom/carplay/adapter/ProjectionGrant;", "configureAudio", "sampleRate", "", "channels", "pcm", "", "requestKeyFrame", "requestScreenRestoreKeyFrame", "deliverMicrophone", "", "name", "", "getName", "()Ljava/lang/String;", "requiresScreenGrant", "getRequiresScreenGrant", "()Z", "isSupported", "context", "Landroid/content/Context;", "warmUp", "prepare", "width", "height", "fps", "(Landroid/content/Context;IIILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startProjection", "onFrameEncoded", "Lkotlin/Function3;", "Lkotlin/ParameterName;", "data", "", "ptsUs", "isKeyFrame", "(Lkotlin/jvm/functions/Function3;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onTouchEvent", "action", "x", "", "y", "pointerId", "onKeyEvent", "keyCode", "isDown", "stopProjection", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface PhoneProjectionAdapter {
    void configureAudio(int sampleRate, int channels, boolean pcm);

    boolean deliverMicrophone(byte[] pcm, int sampleRate, int channels);

    ProjectionCapabilities getCapabilities();

    String getName();

    boolean isSupported(Context context);

    void onKeyEvent(int keyCode, boolean isDown);

    void onTouchEvent(int action, float x, float y, int pointerId);

    Object prepare(Context context, int i, int i2, int i3, Continuation<? super Boolean> continuation);

    void requestKeyFrame();

    void setGrant(ProjectionGrant grant);

    void setObserver(ProjectionObserver observer);

    Object startProjection(Function3<? super byte[], ? super Long, ? super Boolean, Unit> function3, Continuation<? super Unit> continuation);

    Object stopProjection(Continuation<? super Unit> continuation);

    /* JADX INFO: compiled from: PhoneProjectionAdapter.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final class DefaultImpls {
        @Deprecated
        public static void requestScreenRestoreKeyFrame(PhoneProjectionAdapter $this) {
            PhoneProjectionAdapter.super.requestScreenRestoreKeyFrame();
        }

        @Deprecated
        public static boolean getRequiresScreenGrant(PhoneProjectionAdapter $this) {
            return PhoneProjectionAdapter.super.getRequiresScreenGrant();
        }

        @Deprecated
        public static void warmUp(PhoneProjectionAdapter $this, Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            PhoneProjectionAdapter.super.warmUp(context);
        }
    }

    static /* synthetic */ void configureAudio$default(PhoneProjectionAdapter phoneProjectionAdapter, int i, int i2, boolean z, int i3, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: configureAudio");
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        phoneProjectionAdapter.configureAudio(i, i2, z);
    }

    default void requestScreenRestoreKeyFrame() {
        requestKeyFrame();
    }

    default boolean getRequiresScreenGrant() {
        return true;
    }

    default void warmUp(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
    }
}
