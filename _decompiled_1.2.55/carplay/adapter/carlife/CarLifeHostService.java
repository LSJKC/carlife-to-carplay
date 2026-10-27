package com.carplay.adapter.carlife;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.baidu.carlife.mixing.aidl.ICarLifeCastCallback;
import com.baidu.carlife.mixing.aidl.IRemoteAudioListener;
import com.baidu.carlife.mixing.aidl.IRemoteKeyListener;
import com.baidu.carlife.mixing.aidl.IRemoteTouchListener;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Triple;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlinx.coroutines.scheduling.WorkQueueKt;

/* JADX INFO: compiled from: CarLifeHostService.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000*\u0004\u0010\u0013\u0016\u0019\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u001b\u001a\u00020\u00072\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0016J\u0012\u0010\u001e\u001a\u00020\f2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0016J\b\u0010\u001f\u001a\u00020 H\u0016J\n\u0010!\u001a\u0004\u0018\u00010\u0005H\u0002J\u0010\u0010\"\u001a\u00020 2\u0006\u0010#\u001a\u00020$H\u0002R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u00020\u0010X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0011R\u0010\u0010\u0012\u001a\u00020\u0013X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0014R\u0010\u0010\u0015\u001a\u00020\u0016X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0017R\u0010\u0010\u0018\u001a\u00020\u0019X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u001a¨\u0006%"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeHostService;", "Landroid/app/Service;", "<init>", "()V", "castCallback", "Lcom/baidu/carlife/mixing/aidl/ICarLifeCastCallback;", "castCallbackBinder", "Landroid/os/IBinder;", "callbackGeneration", "", "bindGeneration", "pendingCastPrepared", "", "callbackDeathRecipient", "Landroid/os/IBinder$DeathRecipient;", "audioListener", "com/carplay/adapter/carlife/CarLifeHostService$audioListener$1", "Lcom/carplay/adapter/carlife/CarLifeHostService$audioListener$1;", "binder", "com/carplay/adapter/carlife/CarLifeHostService$binder$1", "Lcom/carplay/adapter/carlife/CarLifeHostService$binder$1;", "touchListener", "com/carplay/adapter/carlife/CarLifeHostService$touchListener$1", "Lcom/carplay/adapter/carlife/CarLifeHostService$touchListener$1;", "keyListener", "com/carplay/adapter/carlife/CarLifeHostService$keyListener$1", "Lcom/carplay/adapter/carlife/CarLifeHostService$keyListener$1;", "onBind", "intent", "Landroid/content/Intent;", "onUnbind", "onDestroy", "", "currentCallback", "dispatchCastPrepared", "displayId", "", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarLifeHostService extends Service {
    private long bindGeneration;
    private IBinder.DeathRecipient callbackDeathRecipient;
    private long callbackGeneration;
    private ICarLifeCastCallback castCallback;
    private IBinder castCallbackBinder;
    private boolean pendingCastPrepared;
    private final CarLifeHostService$audioListener$1 audioListener = new IRemoteAudioListener.Stub() { // from class: com.carplay.adapter.carlife.CarLifeHostService$audioListener$1
        @Override // com.baidu.carlife.mixing.aidl.IRemoteAudioListener
        public void onReceiveAudioStream(byte[] data, int offset, int length, int sampleRate) {
            Function2<byte[], Integer, Unit> audio;
            if (data == null || offset < 0 || length < 0 || offset > data.length - length || (audio = CarLifeBridge.INSTANCE.getAudio()) == null) {
                return;
            }
            audio.invoke(ArraysKt.copyOfRange(data, offset, offset + length), Integer.valueOf(sampleRate));
        }
    };
    private final CarLifeHostService$binder$1 binder = new CarLifeHostService$binder$1(this);
    private final CarLifeHostService$touchListener$1 touchListener = new IRemoteTouchListener.Stub() { // from class: com.carplay.adapter.carlife.CarLifeHostService$touchListener$1
        @Override // com.baidu.carlife.mixing.aidl.IRemoteTouchListener
        public boolean onTouchEvent(MotionEvent event) {
            if (event == null) {
                return false;
            }
            Function3<Integer, Float, Float, Unit> touch = CarLifeBridge.INSTANCE.getTouch();
            if (touch != null) {
                touch.invoke(Integer.valueOf(event.getActionMasked()), Float.valueOf(event.getX()), Float.valueOf(event.getY()));
                return true;
            }
            return true;
        }
    };
    private final CarLifeHostService$keyListener$1 keyListener = new IRemoteKeyListener.Stub() { // from class: com.carplay.adapter.carlife.CarLifeHostService$keyListener$1
        @Override // com.baidu.carlife.mixing.aidl.IRemoteKeyListener
        public boolean onKeyEvent(KeyEvent event) {
            if (event == null) {
                return false;
            }
            int carLifeCode = event.getKeyCode();
            Integer androidCode = CarLifeHardKey.INSTANCE.toAndroid(carLifeCode);
            if (androidCode != null && event.getAction() == 0) {
                boolean isMediaKey = SetsKt.setOf((Object[]) new Integer[]{87, 88, 126, Integer.valueOf(WorkQueueKt.MASK), 85, 86}).contains(androidCode);
                if (isMediaKey) {
                    Function1<Integer, Boolean> mediaKeyDispatcher = CarLifeBridge.INSTANCE.getMediaKeyDispatcher();
                    boolean dispatched = mediaKeyDispatcher != null ? mediaKeyDispatcher.invoke(androidCode).booleanValue() : false;
                    if (dispatched) {
                        CarLifeBridge.INSTANCE.getLog().invoke("车机硬键已路由到 MediaSession：CarLife=" + carLifeCode + " → Android=" + androidCode);
                        return true;
                    }
                }
            }
            Function2<Integer, Boolean, Unit> key = CarLifeBridge.INSTANCE.getKey();
            if (key != null) {
                key.invoke(Integer.valueOf(carLifeCode), Boolean.valueOf(event.getAction() == 0));
            }
            return true;
        }
    };

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        Triple triple;
        synchronized (this) {
            this.bindGeneration++;
            IBinder oldBinder = this.castCallbackBinder;
            IBinder.DeathRecipient oldDeathRecipient = this.callbackDeathRecipient;
            this.castCallback = null;
            this.castCallbackBinder = null;
            this.callbackDeathRecipient = null;
            this.pendingCastPrepared = false;
            this.callbackGeneration++;
            triple = new Triple(Long.valueOf(this.bindGeneration), oldBinder, oldDeathRecipient);
        }
        long generation = ((Number) triple.component1()).longValue();
        IBinder oldBinder2 = (IBinder) triple.component2();
        IBinder.DeathRecipient oldDeathRecipient2 = (IBinder.DeathRecipient) triple.component3();
        if (oldBinder2 != null && oldDeathRecipient2 != null) {
            try {
                Result.Companion companion = Result.INSTANCE;
                CarLifeHostService carLifeHostService = this;
                Result.m159constructorimpl(Boolean.valueOf(oldBinder2.unlinkToDeath(oldDeathRecipient2, 0)));
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th));
            }
        }
        CarLifeBridge.INSTANCE.setMusicInfo(null);
        CarLifeBridge.INSTANCE.setMusicProgress(null);
        CarLifeBridge.INSTANCE.markCallbackUnready();
        CarLifeBridge.INSTANCE.getLog().invoke("CarLife 宿主服务收到绑定：uid=" + Binder.getCallingUid() + "，bindGeneration=" + generation);
        CarLifeBridge.INSTANCE.markBound();
        return this.binder;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        final long generation;
        synchronized (this) {
            generation = this.bindGeneration;
        }
        CarLifeBridge.INSTANCE.getLog().invoke("CarLife 宿主服务收到解绑：等待代际确认 bindGeneration=" + generation);
        Thread $this$onUnbind_u24lambda_u245 = new Thread(new Runnable() { // from class: com.carplay.adapter.carlife.CarLifeHostService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CarLifeHostService.onUnbind$lambda$4(this.f$0, generation);
            }
        }, "carlife-bind-retire");
        $this$onUnbind_u24lambda_u245.setDaemon(true);
        $this$onUnbind_u24lambda_u245.start();
        return super.onUnbind(intent);
    }

    static final void onUnbind$lambda$4(CarLifeHostService this$0, long $generation) {
        boolean shouldClear;
        try {
            Thread.sleep(750L);
            synchronized (this$0) {
                shouldClear = this$0.bindGeneration == $generation && this$0.castCallback == null;
            }
            if (!shouldClear) {
                CarLifeBridge.INSTANCE.getLog().invoke("忽略旧 CarLife onUnbind：新绑定或 CastCallback 已接管");
                return;
            }
            CarLifeBridge.INSTANCE.setMusicInfo(null);
            CarLifeBridge.INSTANCE.setMusicProgress(null);
            CarLifeBridge.INSTANCE.markCallbackUnready();
            CarLifeBridge.INSTANCE.markUnbound();
            CarLifeBridge.INSTANCE.getLog().invoke("CarLife 宿主解绑已确认：bindGeneration=" + $generation);
        } catch (InterruptedException e) {
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        synchronized (this) {
            this.bindGeneration++;
            this.castCallback = null;
            this.castCallbackBinder = null;
            this.pendingCastPrepared = false;
            this.callbackDeathRecipient = null;
            Unit unit = Unit.INSTANCE;
        }
        CarLifeBridge.INSTANCE.setMusicInfo(null);
        CarLifeBridge.INSTANCE.setMusicProgress(null);
        CarLifeBridge.INSTANCE.markCallbackUnready();
        CarLifeBridge.INSTANCE.markUnbound();
        CarLifeBridge.INSTANCE.getLog().invoke("CarLife 宿主服务已销毁");
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final ICarLifeCastCallback currentCallback() {
        ICarLifeCastCallback iCarLifeCastCallback;
        synchronized (this) {
            iCarLifeCastCallback = this.castCallback;
        }
        return iCarLifeCastCallback;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void dispatchCastPrepared(final int displayId) {
        synchronized (this) {
            if (this.pendingCastPrepared) {
                final ICarLifeCastCallback callback = this.castCallback;
                if (callback == null) {
                    return;
                }
                this.pendingCastPrepared = false;
                new Thread(new Runnable() { // from class: com.carplay.adapter.carlife.CarLifeHostService$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        CarLifeHostService.dispatchCastPrepared$lambda$11(this.f$0, callback, displayId);
                    }
                }, "carlife-cast-prepared").start();
            }
        }
    }

    static final void dispatchCastPrepared$lambda$11(CarLifeHostService this$0, ICarLifeCastCallback $callback, int $displayId) {
        Object objM159constructorimpl;
        try {
            Result.Companion companion = Result.INSTANCE;
            $callback.onCastPrepared($displayId);
            objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        Throwable it = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (it != null) {
            CarLifeBridge.INSTANCE.getLog().invoke("onCastPrepared 回调失败：" + it.getMessage());
        }
    }
}
