package com.carplay.adapter.carlife;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Surface;
import com.baidu.carlife.mixing.CastConfig;
import com.baidu.carlife.mixing.MusicInfo;
import com.baidu.carlife.mixing.aidl.ICarLifeCastCallback;
import com.baidu.carlife.mixing.aidl.ICarLifeCastManager;
import com.baidu.carlife.mixing.aidl.ISystemAudioCallback;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CarLifeHostService.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u00001\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\b\u0010\u0006\u001a\u00020\u0003H\u0016J\u0012\u0010\u0007\u001a\u00020\u00032\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0016J\u0012\u0010\n\u001a\u00020\u00032\b\u0010\b\u001a\u0004\u0018\u00010\u000bH\u0016J\u0014\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0016¨\u0006\u0010"}, d2 = {"com/carplay/adapter/carlife/CarLifeHostService$binder$1", "Lcom/baidu/carlife/mixing/aidl/ICarLifeCastManager$Stub;", "prepareCast", "", "config", "Lcom/baidu/carlife/mixing/CastConfig;", "stopCast", "setSystemAudioCallback", "callback", "Lcom/baidu/carlife/mixing/aidl/ISystemAudioCallback;", "setCastCallback", "Lcom/baidu/carlife/mixing/aidl/ICarLifeCastCallback;", "invokeMethod", "Landroid/os/Bundle;", "intent", "Landroid/content/Intent;", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarLifeHostService$binder$1 extends ICarLifeCastManager.Stub {
    final /* synthetic */ CarLifeHostService this$0;

    CarLifeHostService$binder$1(CarLifeHostService $receiver) {
        this.this$0 = $receiver;
    }

    @Override // com.baidu.carlife.mixing.aidl.ICarLifeCastManager
    public void prepareCast(CastConfig config) {
        Surface surface = config != null ? config.getSurface() : null;
        CarLifeBridge.INSTANCE.setCastWidth(config != null ? config.getWidth() : 0);
        CarLifeBridge.INSTANCE.setCastHeight(config != null ? config.getHeight() : 0);
        CarLifeBridge.INSTANCE.getLog().invoke("prepareCast: " + (config != null ? Integer.valueOf(config.getWidth()) : null) + "x" + (config != null ? Integer.valueOf(config.getHeight()) : null) + " dpi=" + (config != null ? Integer.valueOf(config.getDpi()) : null) + " surface=" + (surface == null ? "无" : "有效=" + surface.isValid()) + "；这是 CarLife 手机端虚拟画布，不是车机 H.264 画布；车机整屏/安全区由 VIDEO_ENCODER_INIT 单独协商");
        CarLifeHostService carLifeHostService = this.this$0;
        CarLifeHostService carLifeHostService2 = this.this$0;
        synchronized (carLifeHostService) {
            carLifeHostService2.pendingCastPrepared = true;
            Unit unit = Unit.INSTANCE;
        }
        this.this$0.dispatchCastPrepared(0);
    }

    @Override // com.baidu.carlife.mixing.aidl.ICarLifeCastManager
    public void stopCast() {
        CarLifeBridge.INSTANCE.getLog().invoke("stopCast");
        final Function0<Unit> stop = CarLifeBridge.INSTANCE.getStop();
        if (stop != null) {
            final CarLifeHostService carLifeHostService = this.this$0;
            Thread $this$stopCast_u24lambda_u244 = new Thread(new Runnable() { // from class: com.carplay.adapter.carlife.CarLifeHostService$binder$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    CarLifeHostService$binder$1.stopCast$lambda$3(this.f$0, carLifeHostService, stop);
                }
            }, "carlife-stop-notify");
            $this$stopCast_u24lambda_u244.setDaemon(true);
            $this$stopCast_u24lambda_u244.start();
        }
    }

    static final void stopCast$lambda$3(CarLifeHostService$binder$1 this$0, CarLifeHostService this$1, Function0 $stop) {
        Object objM159constructorimpl;
        try {
            Result.Companion companion = Result.INSTANCE;
            $stop.invoke();
            objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        Throwable it = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (it != null) {
            CarLifeBridge.INSTANCE.getLog().invoke("CarLife 停止回调失败：" + it.getMessage());
        }
        this$1.stopSelf();
    }

    @Override // com.baidu.carlife.mixing.aidl.ICarLifeCastManager
    public void setSystemAudioCallback(final ISystemAudioCallback callback) {
        if (callback == null) {
            return;
        }
        new Thread(new Runnable() { // from class: com.carplay.adapter.carlife.CarLifeHostService$binder$1$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CarLifeHostService$binder$1.setSystemAudioCallback$lambda$6(this.f$0, callback);
            }
        }, "carlife-audio").start();
    }

    static final void setSystemAudioCallback$lambda$6(CarLifeHostService$binder$1 this$0, ISystemAudioCallback $callback) {
        try {
            Result.Companion companion = Result.INSTANCE;
            $callback.onAudioStateChange(0, 0);
            Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
    }

    @Override // com.baidu.carlife.mixing.aidl.ICarLifeCastManager
    public void setCastCallback(final ICarLifeCastCallback callback) {
        Pair pair;
        Object objM159constructorimpl;
        Pair pair2;
        CarLifeHostService carLifeHostService = this.this$0;
        if (callback == null) {
            CarLifeHostService carLifeHostService2 = this.this$0;
            synchronized (carLifeHostService) {
                pair2 = TuplesKt.to(carLifeHostService2.castCallbackBinder, Long.valueOf(carLifeHostService2.callbackGeneration));
            }
            final IBinder oldBinder = (IBinder) pair2.component1();
            final long generation = ((Number) pair2.component2()).longValue();
            if (oldBinder == null) {
                CarLifeBridge.INSTANCE.markCallbackUnready();
                return;
            }
            CarLifeBridge.INSTANCE.getLog().invoke("CarLife 收到空 CastCallback，延迟确认旧会话退出");
            final CarLifeHostService carLifeHostService3 = this.this$0;
            Thread $this$setCastCallback_u24lambda_u2411 = new Thread(new Runnable() { // from class: com.carplay.adapter.carlife.CarLifeHostService$binder$1$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    CarLifeHostService$binder$1.setCastCallback$lambda$10(carLifeHostService3, generation, oldBinder);
                }
            }, "carlife-callback-retire");
            $this$setCastCallback_u24lambda_u2411.setDaemon(true);
            $this$setCastCallback_u24lambda_u2411.start();
            return;
        }
        CarLifeHostService carLifeHostService4 = this.this$0;
        synchronized (carLifeHostService) {
            IBinder old = carLifeHostService4.castCallbackBinder;
            carLifeHostService4.castCallback = callback;
            carLifeHostService4.castCallbackBinder = callback.asBinder();
            carLifeHostService4.callbackGeneration++;
            pair = TuplesKt.to(old, Long.valueOf(carLifeHostService4.callbackGeneration));
        }
        IBinder oldBinder2 = (IBinder) pair.component1();
        final long generation2 = ((Number) pair.component2()).longValue();
        IBinder.DeathRecipient oldRecipient = this.this$0.callbackDeathRecipient;
        if (oldRecipient != null && oldBinder2 != null) {
            try {
                Result.Companion companion = Result.INSTANCE;
                CarLifeHostService$binder$1 carLifeHostService$binder$1 = this;
                Result.m159constructorimpl(Boolean.valueOf(oldBinder2.unlinkToDeath(oldRecipient, 0)));
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th));
            }
        }
        final IBinder binder = callback.asBinder();
        final CarLifeHostService carLifeHostService5 = this.this$0;
        IBinder.DeathRecipient recipient = new IBinder.DeathRecipient() { // from class: com.carplay.adapter.carlife.CarLifeHostService$binder$1$$ExternalSyntheticLambda3
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                CarLifeHostService$binder$1.setCastCallback$lambda$16(carLifeHostService5, generation2, binder);
            }
        };
        this.this$0.callbackDeathRecipient = recipient;
        try {
            Result.Companion companion3 = Result.INSTANCE;
            CarLifeHostService$binder$1 carLifeHostService$binder$2 = this;
            binder.linkToDeath(recipient, 0);
            objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        Throwable it = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (it != null) {
            CarLifeBridge.INSTANCE.getLog().invoke("CarLife CastCallback linkToDeath 失败：" + it.getMessage());
        }
        CarLifeBridge carLifeBridge = CarLifeBridge.INSTANCE;
        final CarLifeHostService carLifeHostService6 = this.this$0;
        carLifeBridge.setMusicInfo(new Function1() { // from class: com.carplay.adapter.carlife.CarLifeHostService$binder$1$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return CarLifeHostService$binder$1.setCastCallback$lambda$20(this.f$0, carLifeHostService6, (MusicInfo) obj);
            }
        });
        CarLifeBridge carLifeBridge2 = CarLifeBridge.INSTANCE;
        final CarLifeHostService carLifeHostService7 = this.this$0;
        carLifeBridge2.setMusicProgress(new Function1() { // from class: com.carplay.adapter.carlife.CarLifeHostService$binder$1$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return CarLifeHostService$binder$1.setCastCallback$lambda$22(this.f$0, carLifeHostService7, ((Long) obj).longValue());
            }
        });
        CarLifeBridge.INSTANCE.markCallbackReady();
        final CarLifeHostService carLifeHostService8 = this.this$0;
        new Thread(new Runnable() { // from class: com.carplay.adapter.carlife.CarLifeHostService$binder$1$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                CarLifeHostService$binder$1.setCastCallback$lambda$27(this.f$0, callback, carLifeHostService8);
            }
        }, "carlife-cast-ready").start();
        this.this$0.dispatchCastPrepared(0);
    }

    static final void setCastCallback$lambda$10(CarLifeHostService this$0, long $generation, IBinder $oldBinder) {
        boolean shouldRetire;
        try {
            Thread.sleep(750L);
            synchronized (this$0) {
                shouldRetire = this$0.callbackGeneration == $generation && this$0.castCallbackBinder == $oldBinder;
            }
            if (shouldRetire) {
                synchronized (this$0) {
                    this$0.castCallback = null;
                    this$0.castCallbackBinder = null;
                    this$0.pendingCastPrepared = false;
                    this$0.callbackGeneration++;
                    this$0.callbackDeathRecipient = null;
                    Unit unit = Unit.INSTANCE;
                }
                CarLifeBridge.INSTANCE.setMusicInfo(null);
                CarLifeBridge.INSTANCE.setMusicProgress(null);
                CarLifeBridge.INSTANCE.markCallbackUnready();
                CarLifeBridge.INSTANCE.getLog().invoke("CarLife 旧 CastCallback 已确认退出");
                return;
            }
            CarLifeBridge.INSTANCE.getLog().invoke("忽略旧 CarLife 空 CastCallback：新会话已接管");
        } catch (InterruptedException e) {
        }
    }

    static final void setCastCallback$lambda$16(CarLifeHostService this$0, long $generation, IBinder $binder) {
        boolean current;
        synchronized (this$0) {
            current = false;
            if (this$0.callbackGeneration == $generation && this$0.castCallbackBinder == $binder) {
                this$0.castCallback = null;
                this$0.castCallbackBinder = null;
                this$0.pendingCastPrepared = false;
                current = true;
            }
        }
        if (current) {
            CarLifeBridge.INSTANCE.markCallbackUnready();
            CarLifeBridge.INSTANCE.getLog().invoke("CarLife CastCallback Binder 已死亡");
        }
    }

    static final Unit setCastCallback$lambda$20(CarLifeHostService$binder$1 this$0, CarLifeHostService this$1, MusicInfo info) {
        Unit unit;
        Intrinsics.checkNotNullParameter(info, "info");
        try {
            Result.Companion companion = Result.INSTANCE;
            ICarLifeCastCallback iCarLifeCastCallbackCurrentCallback = this$1.currentCallback();
            if (iCarLifeCastCallbackCurrentCallback != null) {
                iCarLifeCastCallbackCurrentCallback.sendMusicInfo(info);
                unit = Unit.INSTANCE;
            } else {
                unit = null;
            }
            Result.m159constructorimpl(unit);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        return Unit.INSTANCE;
    }

    static final Unit setCastCallback$lambda$22(CarLifeHostService$binder$1 this$0, CarLifeHostService this$1, long position) {
        Unit unit;
        try {
            Result.Companion companion = Result.INSTANCE;
            ICarLifeCastCallback iCarLifeCastCallbackCurrentCallback = this$1.currentCallback();
            if (iCarLifeCastCallbackCurrentCallback != null) {
                iCarLifeCastCallbackCurrentCallback.notifyMusicProgress(position);
                unit = Unit.INSTANCE;
            } else {
                unit = null;
            }
            Result.m159constructorimpl(unit);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        return Unit.INSTANCE;
    }

    static final void setCastCallback$lambda$27(CarLifeHostService$binder$1 this$0, ICarLifeCastCallback $target, CarLifeHostService this$1) {
        try {
            Result.Companion companion = Result.INSTANCE;
            $target.onServiceReady();
            Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        try {
            Result.Companion companion3 = Result.INSTANCE;
            $target.addTouchListener(this$1.touchListener);
            Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        try {
            Result.Companion companion5 = Result.INSTANCE;
            $target.addKeyListener(this$1.keyListener);
            Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th3) {
            Result.Companion companion6 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th3));
        }
        try {
            Result.Companion companion7 = Result.INSTANCE;
            $target.addAudioListener(this$1.audioListener);
            Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th4) {
            Result.Companion companion8 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th4));
        }
    }

    @Override // com.baidu.carlife.mixing.aidl.ICarLifeCastManager
    public Bundle invokeMethod(Intent intent) {
        String action;
        if (intent == null || (action = intent.getAction()) == null) {
            return null;
        }
        CarLifeBridge.INSTANCE.getLog().invoke("invokeMethod: " + action);
        return CarLifeBridge.INSTANCE.invoke(action, intent.getExtras());
    }
}
