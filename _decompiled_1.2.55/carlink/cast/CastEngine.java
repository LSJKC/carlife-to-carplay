package com.carlink.cast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import com.carplay.adapter.PhoneProjectionAdapter;
import com.carplay.adapter.carlife.CarLifeProjectionAdapter;
import com.carplay.sender.AccessoryAuthentication;
import com.carplay.sender.BondedAccessoryTrust;
import com.carplay.sender.ControllerIdentity;
import java.io.Closeable;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__BuildersKt;

/* JADX INFO: compiled from: CastEngine.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0000\u0018\u0000 )2\u00020\u0001:\u0001)B3\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\t¢\u0006\u0004\b\f\u0010\rJ\u001a\u0010\u0019\u001a\u00020\u000b2\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u001b\u0012\u0004\u0012\u00020\u000b0\tJ\u0006\u0010\u001c\u001a\u00020\u001dJ\u0006\u0010\u001e\u001a\u00020\u001dJ\u0006\u0010\u001f\u001a\u00020\u001dJ\u0006\u0010 \u001a\u00020\u001dJ\u0010\u0010!\u001a\u00020\u001d2\u0006\u0010\"\u001a\u00020#H\u0002J\u0018\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\n2\u0006\u0010'\u001a\u00020\u001dH\u0002J\b\u0010(\u001a\u00020\u000bH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006*"}, d2 = {"Lcom/carlink/cast/CastEngine;", "Ljava/io/Closeable;", "context", "Landroid/content/Context;", "settings", "Lcom/carlink/cast/CastSettings;", "source", "Lcom/carplay/adapter/PhoneProjectionAdapter;", "log", "Lkotlin/Function1;", "", "", "<init>", "(Landroid/content/Context;Lcom/carlink/cast/CastSettings;Lcom/carplay/adapter/PhoneProjectionAdapter;Lkotlin/jvm/functions/Function1;)V", "closed", "Ljava/util/concurrent/atomic/AtomicBoolean;", "finished", "bluetooth", "Lcom/carlink/cast/BluetoothBootstrap;", "vehicle", "Lcom/carlink/cast/VehicleNetwork;", "airplay", "Lcom/carlink/cast/AirPlayLink;", "hid", "Lcom/carlink/cast/Iap2HidDispatcher;", "run", "onFailure", "", "hasFinished", "", "isHealthy", "isWirelessHandoffComplete", "isBluetoothAlive", "isPublicKeyEncoding", "key", "", "trustFor", "Lcom/carplay/sender/AccessoryAuthentication;", CastSettings.KEY_PEER, "authenticatedBluetooth", "close", "Companion", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CastEngine implements Closeable {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private volatile AirPlayLink airplay;
    private volatile BluetoothBootstrap bluetooth;
    private final AtomicBoolean closed;
    private final Context context;
    private final AtomicBoolean finished;
    private final Iap2HidDispatcher hid;
    private final Function1<String, Unit> log;
    private final CastSettings settings;
    private final PhoneProjectionAdapter source;
    private volatile VehicleNetwork vehicle;

    /* JADX WARN: Multi-variable type inference failed */
    public CastEngine(Context context, CastSettings settings, PhoneProjectionAdapter source, Function1<? super String, Unit> log) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(settings, "settings");
        Intrinsics.checkNotNullParameter(source, "source");
        Intrinsics.checkNotNullParameter(log, "log");
        this.context = context;
        this.settings = settings;
        this.source = source;
        this.log = log;
        this.closed = new AtomicBoolean();
        this.finished = new AtomicBoolean();
        this.hid = new Iap2HidDispatcher(this.source, new Function1() { // from class: com.carlink.cast.CastEngine$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return CastEngine.hid$lambda$0(this.f$0, (String) obj);
            }
        });
    }

    static final Unit hid$lambda$0(CastEngine this$0, String message) {
        Intrinsics.checkNotNullParameter(message, "message");
        this$0.log.invoke(message);
        return Unit.INSTANCE;
    }

    public final void run(final Function1<? super Throwable, Unit> onFailure) {
        BluetoothAdapter adapter;
        Object objM159constructorimpl;
        Intrinsics.checkNotNullParameter(onFailure, "onFailure");
        try {
            final String peer = this.settings.getPeerAddress();
            if (peer == null) {
                throw new IllegalStateException("请先选择已配对的车机".toString());
            }
            byte[] bArrAccessoryKey = this.settings.accessoryKey(peer);
            if (bArrAccessoryKey != null && !isPublicKeyEncoding(bArrAccessoryKey)) {
                this.settings.removeAccessoryKey(peer);
                CastLog.INSTANCE.event("INFO", "已丢弃无效的车机公钥绑定");
            }
            BluetoothManager bluetoothManager = (BluetoothManager) this.context.getSystemService(BluetoothManager.class);
            if (bluetoothManager == null || (adapter = bluetoothManager.getAdapter()) == null) {
                throw new IllegalStateException("手机不支持蓝牙".toString());
            }
            if (!adapter.isEnabled()) {
                throw new IllegalStateException("请打开蓝牙".toString());
            }
            String address = LocalBluetoothAddress.INSTANCE.read(this.context, adapter, this.settings.getManualBluetoothAddress());
            if (address == null) {
                throw new IllegalStateException("系统未提供本机蓝牙地址，请在设置中手动填写".toString());
            }
            ControllerIdentity identity = this.settings.identity();
            CastLog.INSTANCE.event("INFO", "本机蓝牙地址 " + address + "；画面来源 " + this.source.getName());
            try {
                Result.Companion companion = Result.INSTANCE;
                CastEngine castEngine = this;
                castEngine.source.warmUp(castEngine.context);
                objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl);
            if (thM162exceptionOrNullimpl != null) {
                CastLog.INSTANCE.event("WARN", "画面来源预热失败：" + thM162exceptionOrNullimpl.getMessage());
            }
            CastLog.INSTANCE.stage(CastStage.BLUETOOTH, "与车机建立 iAP2 链路");
            final BluetoothBootstrap link = new BluetoothBootstrap(this.context, peer, identity.getId(), address, new Function1() { // from class: com.carlink.cast.CastEngine$$ExternalSyntheticLambda2
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return this.f$0.trustFor(peer, ((Boolean) obj).booleanValue());
                }
            }, this.hid, new Function1() { // from class: com.carlink.cast.CastEngine$$ExternalSyntheticLambda3
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return CastEngine.run$lambda$6(this.f$0, (String) obj);
                }
            });
            this.bluetooth = link;
            BluetoothBootstrap.Result result = BluetoothBootstrap.connect$default(link, 0L, 1, null);
            if (this.closed.get()) {
                throw new IllegalStateException("连接已停止".toString());
            }
            CastLog.INSTANCE.stage(CastStage.NETWORK, "车机已下发无线配置，加入 " + result.getEndpoint().getSsid());
            VehicleNetwork network = new VehicleNetwork(this.context, result.getEndpoint(), new Function1() { // from class: com.carlink.cast.CastEngine$$ExternalSyntheticLambda4
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return CastEngine.run$lambda$8(this.f$0, (String) obj);
                }
            });
            this.vehicle = network;
            network.observeDefaultNetwork();
            VehicleNetwork.join$default(network, 0L, 1, null);
            if (this.closed.get()) {
                throw new IllegalStateException("连接已停止".toString());
            }
            CastLog.INSTANCE.stage(CastStage.DISCOVER, "确认车机 AirPlay 端点");
            boolean audioEnabled = this.settings.getAudio() && this.source.getCapabilities().getAudio();
            CastLog.INSTANCE.event("INFO", "媒体音频：" + (audioEnabled ? "启用" : "关闭") + "，来源=" + this.source.getName() + "，来源支持=" + this.source.getCapabilities().getAudio());
            AirPlayLink channel = new AirPlayLink(this.context, identity, network, this.source, this.settings, audioEnabled, result.getEndpoint(), peer, new Function1() { // from class: com.carlink.cast.CastEngine$$ExternalSyntheticLambda5
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return this.f$0.trustFor(peer, ((Boolean) obj).booleanValue());
                }
            }, this.hid, new Function0() { // from class: com.carlink.cast.CastEngine$$ExternalSyntheticLambda6
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return Boolean.valueOf(link.isAlive());
                }
            }, new Function0() { // from class: com.carlink.cast.CastEngine$$ExternalSyntheticLambda7
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return link.identityFingerprint();
                }
            }, new Function1() { // from class: com.carlink.cast.CastEngine$$ExternalSyntheticLambda8
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return CastEngine.run$lambda$13(this.f$0, (String) obj);
                }
            }, new Function1() { // from class: com.carlink.cast.CastEngine$$ExternalSyntheticLambda9
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return CastEngine.run$lambda$14(this.f$0, onFailure, (Throwable) obj);
                }
            });
            this.airplay = channel;
            channel.start(new Function1() { // from class: com.carlink.cast.CastEngine$$ExternalSyntheticLambda10
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return CastEngine.run$lambda$15(onFailure, (Throwable) obj);
                }
            }, new Function0() { // from class: com.carlink.cast.CastEngine$$ExternalSyntheticLambda11
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return CastEngine.run$lambda$16(this.f$0);
                }
            });
        } catch (Throwable failure) {
            this.finished.set(true);
            if (this.closed.get()) {
                return;
            }
            onFailure.invoke(failure);
        }
    }

    static final Unit run$lambda$6(CastEngine this$0, String message) {
        Intrinsics.checkNotNullParameter(message, "message");
        this$0.log.invoke(message);
        return Unit.INSTANCE;
    }

    static final Unit run$lambda$8(CastEngine this$0, String message) {
        Intrinsics.checkNotNullParameter(message, "message");
        this$0.log.invoke(message);
        return Unit.INSTANCE;
    }

    static final Unit run$lambda$13(CastEngine this$0, String message) {
        Intrinsics.checkNotNullParameter(message, "message");
        this$0.log.invoke(message);
        return Unit.INSTANCE;
    }

    static final Unit run$lambda$14(CastEngine this$0, Function1 $onFailure, Throwable failure) {
        Intrinsics.checkNotNullParameter(failure, "failure");
        if (!this$0.closed.get()) {
            $onFailure.invoke(failure);
        }
        return Unit.INSTANCE;
    }

    static final Unit run$lambda$15(Function1 $onFailure, Throwable failure) {
        Intrinsics.checkNotNullParameter(failure, "failure");
        $onFailure.invoke(failure);
        return Unit.INSTANCE;
    }

    static final Unit run$lambda$16(CastEngine this$0) {
        this$0.finished.set(true);
        return Unit.INSTANCE;
    }

    public final boolean hasFinished() {
        return this.finished.get();
    }

    public final boolean isHealthy() {
        if (this.closed.get() || this.finished.get()) {
            return false;
        }
        VehicleNetwork vehicleNetwork = this.vehicle;
        if (!(vehicleNetwork != null && vehicleNetwork.isAlive())) {
            return false;
        }
        AirPlayLink airPlayLink = this.airplay;
        return airPlayLink != null && airPlayLink.isHealthy();
    }

    public final boolean isWirelessHandoffComplete() {
        AirPlayLink airPlayLink = this.airplay;
        return airPlayLink != null && airPlayLink.getWifiControlReady();
    }

    public final boolean isBluetoothAlive() {
        BluetoothBootstrap bluetoothBootstrap = this.bluetooth;
        return bluetoothBootstrap != null && bluetoothBootstrap.isAlive();
    }

    private final boolean isPublicKeyEncoding(byte[] key) {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        X509EncodedKeySpec spec = new X509EncodedKeySpec(key);
        try {
            Result.Companion companion = Result.INSTANCE;
            CastEngine castEngine = this;
            objM159constructorimpl = Result.m159constructorimpl(KeyFactory.getInstance("EC").generatePublic(spec));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (!Result.m166isSuccessimpl(objM159constructorimpl)) {
            try {
                Result.Companion companion3 = Result.INSTANCE;
                CastEngine castEngine2 = this;
                objM159constructorimpl2 = Result.m159constructorimpl(KeyFactory.getInstance("RSA").generatePublic(spec));
            } catch (Throwable th2) {
                Result.Companion companion4 = Result.INSTANCE;
                objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th2));
            }
            if (!Result.m166isSuccessimpl(objM159constructorimpl2)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final AccessoryAuthentication trustFor(final String peer, boolean authenticatedBluetooth) {
        return new BondedAccessoryTrust(this.settings.accessoryKey(peer), authenticatedBluetooth, new Function1() { // from class: com.carlink.cast.CastEngine$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return CastEngine.trustFor$lambda$19(this.f$0, peer, (byte[]) obj);
            }
        });
    }

    static final Unit trustFor$lambda$19(CastEngine this$0, String $peer, byte[] key) {
        Intrinsics.checkNotNullParameter(key, "key");
        this$0.settings.saveAccessoryKey($peer, key);
        CastLog.INSTANCE.event("INFO", "已绑定车机认证公钥");
        return Unit.INSTANCE;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        Object objM159constructorimpl;
        Unit unit;
        Unit unit2;
        Unit unit3;
        if (this.closed.compareAndSet(false, true)) {
            try {
                Result.Companion companion = Result.INSTANCE;
                AirPlayLink airPlayLink = this.airplay;
                if (airPlayLink != null) {
                    airPlayLink.close();
                    unit3 = Unit.INSTANCE;
                } else {
                    unit3 = null;
                }
                Result.m159constructorimpl(unit3);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            this.airplay = null;
            try {
                Result.Companion companion3 = Result.INSTANCE;
                BuildersKt__BuildersKt.runBlocking$default(null, new CastEngine$close$2$1(this, null), 1, null);
                objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th2) {
                Result.Companion companion4 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th2));
            }
            Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl);
            if (thM162exceptionOrNullimpl != null) {
                CastLog.INSTANCE.event("WARN", "释放画面来源失败：" + thM162exceptionOrNullimpl.getMessage());
            }
            try {
                Result.Companion companion5 = Result.INSTANCE;
                VehicleNetwork vehicleNetwork = this.vehicle;
                if (vehicleNetwork != null) {
                    vehicleNetwork.close();
                    unit2 = Unit.INSTANCE;
                } else {
                    unit2 = null;
                }
                Result.m159constructorimpl(unit2);
            } catch (Throwable th3) {
                Result.Companion companion6 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th3));
            }
            this.vehicle = null;
            try {
                Result.Companion companion7 = Result.INSTANCE;
                BluetoothBootstrap bluetoothBootstrap = this.bluetooth;
                if (bluetoothBootstrap != null) {
                    bluetoothBootstrap.close();
                    unit = Unit.INSTANCE;
                } else {
                    unit = null;
                }
                Result.m159constructorimpl(unit);
            } catch (Throwable th4) {
                Result.Companion companion8 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th4));
            }
            this.bluetooth = null;
            this.hid.reset();
            Dispatch.INSTANCE.reset();
        }
    }

    /* JADX INFO: compiled from: CastEngine.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007¨\u0006\b"}, d2 = {"Lcom/carlink/cast/CastEngine$Companion;", "", "<init>", "()V", "carLifeAvailable", "", "context", "Landroid/content/Context;", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean carLifeAvailable(Context context) {
            Object objM159constructorimpl;
            Intrinsics.checkNotNullParameter(context, "context");
            try {
                Result.Companion companion = Result.INSTANCE;
                Companion companion2 = this;
                objM159constructorimpl = Result.m159constructorimpl(Boolean.valueOf(new CarLifeProjectionAdapter().isSupported(context)));
            } catch (Throwable th) {
                Result.Companion companion3 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            if (Result.m165isFailureimpl(objM159constructorimpl)) {
                objM159constructorimpl = false;
            }
            return ((Boolean) objM159constructorimpl).booleanValue();
        }
    }
}
