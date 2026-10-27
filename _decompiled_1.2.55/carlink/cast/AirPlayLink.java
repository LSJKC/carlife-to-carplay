package com.carlink.cast;

import android.content.Context;
import android.net.LinkProperties;
import android.net.Network;
import com.carplay.adapter.PhoneProjectionAdapter;
import com.carplay.adapter.ProjectionObserver;
import com.carplay.adapter.carlife.CarLifePanel;
import com.carplay.adapter.carlife.CarLifeProjectionAdapter;
import com.carplay.adapter.carlife.CarLifeVideoProcessor;
import com.carplay.airplay.AirPlaySession;
import com.carplay.airplay.PairSetup;
import com.carplay.iap2.controller.PhoneBootstrap;
import com.carplay.iap2.controller.PhoneBootstrapProfile;
import com.carplay.iap2.controller.PhoneBootstrapPurpose;
import com.carplay.iap2.controller.WirelessEndpoint;
import com.carplay.iap2.session.Iap2Session;
import com.carplay.iap2.transport.BlockingDuplexByteStream;
import com.carplay.iap2.wire.Iap2Frame;
import com.carplay.sender.AccessoryAuthentication;
import com.carplay.sender.AccessoryTrust;
import com.carplay.sender.AirPlaySender;
import com.carplay.sender.ControllerIdentity;
import com.carplay.sender.LegacySecurity;
import com.carplay.sender.MfiController;
import com.carplay.sender.PairedAccessory;
import com.carplay.sender.PeerAddress;
import com.carplay.sender.PhoneIapTunnel;
import com.carplay.sender.PhonePairing;
import com.carplay.sender.RtspWire;
import com.carplay.sender.SenderAudioFormat;
import com.carplay.sender.SenderDisplay;
import com.carplay.sender.SenderEvents;
import com.carplay.sender.SenderSafeArea;
import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.AdaptedFunctionReference;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.RangesKt;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt__BuildersKt;
import org.slf4j.Marker;

/* JADX INFO: compiled from: AirPlayLink.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000Ë\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0004*\u0001P\b\u0000\u0018\u00002\u00020\u0001B«\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00140\u0013\u0012\u0006\u0010\u0015\u001a\u00020\u0016\u0012\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\r0\u0018\u0012\u000e\u0010\u0019\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001a0\u0018\u0012\u0012\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u001c0\u0013\u0012\u0014\b\u0002\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020\u001c0\u0013¢\u0006\u0004\b\u001f\u0010 J\u0006\u0010:\u001a\u00020\rJ\u0006\u0010;\u001a\u00020\rJ(\u0010<\u001a\u00020\u001c2\u0012\u0010=\u001a\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020\u001c0\u00132\f\u0010>\u001a\b\u0012\u0004\u0012\u00020\u001c0\u0018J\u001f\u0010?\u001a\u0002H@\"\b\b\u0000\u0010@*\u00020\u00012\u0006\u0010A\u001a\u0002H@H\u0002¢\u0006\u0002\u0010BJ\b\u0010C\u001a\u00020\u001cH\u0002J\u000e\u0010D\u001a\b\u0012\u0004\u0012\u00020F0EH\u0002J\u0010\u0010G\u001a\u00020\u001a2\u0006\u0010H\u001a\u00020IH\u0002J\u0018\u0010J\u001a\u00020\u001c2\u0006\u0010K\u001a\u00020L2\u0006\u0010M\u001a\u00020NH\u0002J\r\u0010O\u001a\u00020PH\u0002¢\u0006\u0002\u0010QJ\b\u0010R\u001a\u00020\u001cH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00140\u0013X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\r0\u0018X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0019\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001a0\u0018X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u001c0\u0013X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020\u001c0\u0013X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020(X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\"X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00010+X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010,\u001a\u0004\u0018\u00010-X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010.\u001a\u0004\u0018\u00010/X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u00100\u001a\u0004\u0018\u000101X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u00102\u001a\u0004\u0018\u000103X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u00104\u001a\u0004\u0018\u000105X\u0082\u000e¢\u0006\u0004\n\u0002\u00106R\u0010\u00107\u001a\u0004\u0018\u00010\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00108\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020\"X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006S"}, d2 = {"Lcom/carlink/cast/AirPlayLink;", "Ljava/io/Closeable;", "context", "Landroid/content/Context;", CastSettings.KEY_IDENTITY, "Lcom/carplay/sender/ControllerIdentity;", "vehicle", "Lcom/carlink/cast/VehicleNetwork;", "source", "Lcom/carplay/adapter/PhoneProjectionAdapter;", "settings", "Lcom/carlink/cast/CastSettings;", "enableAudio", "", "endpoint", "Lcom/carplay/iap2/controller/WirelessEndpoint;", "peerAddress", "", "trustFor", "Lkotlin/Function1;", "Lcom/carplay/sender/AccessoryAuthentication;", "hid", "Lcom/carlink/cast/Iap2HidDispatcher;", "bluetoothAlive", "Lkotlin/Function0;", "bluetoothIdentity", "", "log", "", "terminalFailure", "", "<init>", "(Landroid/content/Context;Lcom/carplay/sender/ControllerIdentity;Lcom/carlink/cast/VehicleNetwork;Lcom/carplay/adapter/PhoneProjectionAdapter;Lcom/carlink/cast/CastSettings;ZLcom/carplay/iap2/controller/WirelessEndpoint;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lcom/carlink/cast/Iap2HidDispatcher;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "closed", "Ljava/util/concurrent/atomic/AtomicBoolean;", "audioProfiles", "Lcom/carlink/cast/AudioProfileMemory;", "nowPlaying", "Lcom/carlink/cast/NowPlayingPublisher;", "nowPlayingSubscription", "", "failed", "resources", "", "sender", "Lcom/carplay/sender/AirPlaySender;", "display", "Lcom/carplay/sender/SenderDisplay;", "worker", "Ljava/lang/Thread;", "controlServer", "Lcom/carlink/cast/ControlInvitationServer;", "discoveredPort", "", "Ljava/lang/Integer;", "discoveredKey", "wifiControlReady", "bluetoothReleased", "isWirelessHandoffComplete", "isHealthy", "start", "onFailure", "onFinished", "own", "T", "value", "(Ljava/io/Closeable;)Ljava/io/Closeable;", "run", "resolveTargets", "", "Lcom/carplay/sender/PeerAddress;", "pairingFor", "pairing", "Lcom/carplay/sender/PhonePairing;", "handleControlMessage", "runtime", "Lcom/carplay/iap2/session/Iap2Session;", "frame", "Lcom/carplay/iap2/wire/Iap2Frame;", "events", "com/carlink/cast/AirPlayLink$events$1", "()Lcom/carlink/cast/AirPlayLink$events$1;", "close", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AirPlayLink implements Closeable {
    private final AudioProfileMemory audioProfiles;
    private final Function0<Boolean> bluetoothAlive;
    private final Function0<byte[]> bluetoothIdentity;
    private final AtomicBoolean bluetoothReleased;
    private final AtomicBoolean closed;
    private final Context context;
    private ControlInvitationServer controlServer;
    private volatile String discoveredKey;
    private volatile Integer discoveredPort;
    private volatile SenderDisplay display;
    private final boolean enableAudio;
    private final WirelessEndpoint endpoint;
    private final AtomicBoolean failed;
    private final Iap2HidDispatcher hid;
    private final ControllerIdentity identity;
    private final Function1<String, Unit> log;
    private final NowPlayingPublisher nowPlaying;
    private volatile long nowPlayingSubscription;
    private final String peerAddress;
    private final List<Closeable> resources;
    private volatile AirPlaySender sender;
    private final CastSettings settings;
    private final PhoneProjectionAdapter source;
    private final Function1<Throwable, Unit> terminalFailure;
    private final Function1<Boolean, AccessoryAuthentication> trustFor;
    private final VehicleNetwork vehicle;
    private volatile boolean wifiControlReady;
    private Thread worker;

    /* JADX WARN: Multi-variable type inference failed */
    public AirPlayLink(Context context, ControllerIdentity identity, VehicleNetwork vehicle, PhoneProjectionAdapter source, CastSettings settings, boolean enableAudio, WirelessEndpoint endpoint, String peerAddress, Function1<? super Boolean, ? extends AccessoryAuthentication> trustFor, Iap2HidDispatcher hid, Function0<Boolean> bluetoothAlive, Function0<byte[]> bluetoothIdentity, Function1<? super String, Unit> log, Function1<? super Throwable, Unit> terminalFailure) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(identity, "identity");
        Intrinsics.checkNotNullParameter(vehicle, "vehicle");
        Intrinsics.checkNotNullParameter(source, "source");
        Intrinsics.checkNotNullParameter(settings, "settings");
        Intrinsics.checkNotNullParameter(endpoint, "endpoint");
        Intrinsics.checkNotNullParameter(peerAddress, "peerAddress");
        Intrinsics.checkNotNullParameter(trustFor, "trustFor");
        Intrinsics.checkNotNullParameter(hid, "hid");
        Intrinsics.checkNotNullParameter(bluetoothAlive, "bluetoothAlive");
        Intrinsics.checkNotNullParameter(bluetoothIdentity, "bluetoothIdentity");
        Intrinsics.checkNotNullParameter(log, "log");
        Intrinsics.checkNotNullParameter(terminalFailure, "terminalFailure");
        this.context = context;
        this.identity = identity;
        this.vehicle = vehicle;
        this.source = source;
        this.settings = settings;
        this.enableAudio = enableAudio;
        this.endpoint = endpoint;
        this.peerAddress = peerAddress;
        this.trustFor = trustFor;
        this.hid = hid;
        this.bluetoothAlive = bluetoothAlive;
        this.bluetoothIdentity = bluetoothIdentity;
        this.log = log;
        this.terminalFailure = terminalFailure;
        this.closed = new AtomicBoolean();
        this.audioProfiles = new AudioProfileMemory(this.context, this.peerAddress);
        this.nowPlaying = new NowPlayingPublisher(this.context, this.log);
        this.failed = new AtomicBoolean();
        this.resources = new ArrayList();
        this.bluetoothReleased = new AtomicBoolean();
    }

    public /* synthetic */ AirPlayLink(Context context, ControllerIdentity controllerIdentity, VehicleNetwork vehicleNetwork, PhoneProjectionAdapter phoneProjectionAdapter, CastSettings castSettings, boolean z, WirelessEndpoint wirelessEndpoint, String str, Function1 function1, Iap2HidDispatcher iap2HidDispatcher, Function0 function0, Function0 function2, Function1 function3, Function1 function4, int i, DefaultConstructorMarker defaultConstructorMarker) {
        Function1 function5;
        if ((i & 8192) == 0) {
            function5 = function4;
        } else {
            function5 = new Function1() { // from class: com.carlink.cast.AirPlayLink$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return AirPlayLink._init_$lambda$0((Throwable) obj);
                }
            };
        }
        this(context, controllerIdentity, vehicleNetwork, phoneProjectionAdapter, castSettings, z, wirelessEndpoint, str, function1, iap2HidDispatcher, function0, function2, function3, function5);
    }

    static final Unit _init_$lambda$0(Throwable it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: isWirelessHandoffComplete, reason: from getter */
    public final boolean getWifiControlReady() {
        return this.wifiControlReady;
    }

    public final boolean isHealthy() {
        return (this.closed.get() || this.failed.get() || !this.vehicle.isAlive()) ? false : true;
    }

    public final void start(final Function1<? super Throwable, Unit> onFailure, final Function0<Unit> onFinished) {
        Intrinsics.checkNotNullParameter(onFailure, "onFailure");
        Intrinsics.checkNotNullParameter(onFinished, "onFinished");
        Thread thread = new Thread(new Runnable() { // from class: com.carlink.cast.AirPlayLink$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                AirPlayLink.start$lambda$1(this.f$0, onFailure, onFinished);
            }
        }, "carlink-airplay");
        thread.setDaemon(true);
        thread.start();
        this.worker = thread;
    }

    static final void start$lambda$1(AirPlayLink this$0, Function1 $onFailure, Function0 $onFinished) {
        try {
            this$0.run();
        } catch (Throwable failure) {
            try {
                if (!this$0.closed.get()) {
                    this$0.failed.set(true);
                    Function1<String, Unit> function1 = this$0.log;
                    String message = failure.getMessage();
                    if (message == null) {
                        message = failure.getClass().getSimpleName();
                    }
                    function1.invoke("投屏会话失败：" + message);
                    this$0.close();
                    $onFailure.invoke(failure);
                }
            } finally {
                $onFinished.invoke();
            }
        }
    }

    private final <T extends Closeable> T own(T value) {
        synchronized (this.resources) {
            this.resources.add(value);
            Unit unit = Unit.INSTANCE;
        }
        return value;
    }

    /* JADX WARN: Type inference failed for: r3v13, types: [T, java.net.InetAddress] */
    /* JADX WARN: Type inference failed for: r6v7, types: [T, java.net.InetAddress] */
    private final void run() throws InterruptedException, IOException {
        Object objM159constructorimpl;
        Network network = this.vehicle.getNetwork();
        if (network == null) {
            throw new IllegalStateException("车机网络尚未就绪".toString());
        }
        InetAddress phoneAddress = this.vehicle.localAddress();
        ControlInvitationServer controlInvitationServer = (ControlInvitationServer) own(new ControlInvitationServer(network, phoneAddress, this.log));
        controlInvitationServer.start();
        this.controlServer = controlInvitationServer;
        final List<PeerAddress> listResolveTargets = resolveTargets();
        Integer port = this.endpoint.getPort();
        int controlPort = (port == null && (port = this.discoveredPort) == null) ? 7000 : port.intValue();
        Context context = this.context;
        ControlInvitationServer controlInvitationServer2 = this.controlServer;
        AirPlayDiscovery discovery = (AirPlayDiscovery) own(new AirPlayDiscovery(context, network, phoneAddress, controlInvitationServer2 != null ? controlInvitationServer2.getPort() : 0, this.identity.getId(), this.log));
        try {
            Result.Companion companion = Result.INSTANCE;
            AirPlayLink airPlayLink = this;
            discovery.start();
            objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (thM162exceptionOrNullimpl != null) {
            this.log.invoke("Bonjour 启动失败：" + thM162exceptionOrNullimpl.getMessage());
        }
        this.log.invoke("连接车机 AirPlay：" + ((PeerAddress) CollectionsKt.first((List) listResolveTargets)).getRemote().getHostAddress() + ":" + controlPort);
        final Ref.ObjectRef receiver = new Ref.ObjectRef();
        receiver.element = ((PeerAddress) CollectionsKt.first((List) listResolveTargets)).getRemote();
        final Ref.ObjectRef local = new Ref.ObjectRef();
        local.element = ((PeerAddress) CollectionsKt.first((List) listResolveTargets)).getLocal();
        Function1 connect = new Function1() { // from class: com.carlink.cast.AirPlayLink$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AirPlayLink.run$lambda$8(this.f$0, listResolveTargets, receiver, local, ((Integer) obj).intValue());
            }
        };
        final AirPlaySender channel = (AirPlaySender) own(new AirPlaySender(this.identity, new Function1() { // from class: com.carlink.cast.AirPlayLink$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AirPlayLink.run$lambda$9(this.f$0, (PhonePairing) obj);
            }
        }, connect, new Function1() { // from class: com.carlink.cast.AirPlayLink$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AirPlayLink.run$lambda$10(this.f$0, (DatagramSocket) obj);
            }
        }, events(), new Function1() { // from class: com.carlink.cast.AirPlayLink$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AirPlayLink.run$lambda$11(this.f$0, (PairedAccessory) obj);
            }
        }, new Function1() { // from class: com.carlink.cast.AirPlayLink$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AirPlayLink.run$lambda$13(this.f$0, (RtspWire) obj);
            }
        }, new Function0() { // from class: com.carlink.cast.AirPlayLink$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AirPlayLink.run$lambda$14(local);
            }
        }, new Function1() { // from class: com.carlink.cast.AirPlayLink$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AirPlayLink.run$lambda$15(this.f$0, (List) obj);
            }
        }, new Function1() { // from class: com.carlink.cast.AirPlayLink$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AirPlayLink.run$lambda$16(this.f$0, (SenderAudioFormat) obj);
            }
        }, new Function1() { // from class: com.carlink.cast.AirPlayLink$$ExternalSyntheticLambda9
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AirPlayLink.run$lambda$17(this.f$0, (SenderAudioFormat) obj);
            }
        }));
        this.sender = channel;
        this.source.setObserver(new ProjectionObserver() { // from class: com.carlink.cast.AirPlayLink.run.4
            @Override // com.carplay.adapter.ProjectionObserver
            public void videoConfig(byte[] avcC) {
                Intrinsics.checkNotNullParameter(avcC, "avcC");
                channel.config(avcC);
            }

            @Override // com.carplay.adapter.ProjectionObserver
            public void audioFrame(byte[] bytes, long sampleTimeUs, boolean pcm) {
                Intrinsics.checkNotNullParameter(bytes, "bytes");
                CastLog.INSTANCE.sourceAudio(bytes, pcm);
                try {
                    channel.audio(bytes, sampleTimeUs);
                } catch (Throwable error) {
                    Function1 function1 = this.log;
                    String message = error.getMessage();
                    if (message == null) {
                        message = error.getClass().getSimpleName();
                    }
                    function1.invoke("音频来源交付失败：" + message + "，输入=" + bytes.length + "B，格式=" + (pcm ? "PCM" : "AAC"));
                }
            }

            @Override // com.carplay.adapter.ProjectionObserver
            public void diagnostic(String message) {
                Intrinsics.checkNotNullParameter(message, "message");
                this.log.invoke(message);
            }

            @Override // com.carplay.adapter.ProjectionObserver
            public void failed(Throwable error) {
                Intrinsics.checkNotNullParameter(error, "error");
                if (!this.closed.get()) {
                    Function1 function1 = this.log;
                    String message = error.getMessage();
                    if (message == null) {
                        message = error.getClass().getSimpleName();
                    }
                    function1.invoke("画面来源异常：" + message);
                    this.close();
                }
            }
        });
        SenderDisplay selected = channel.start(controlPort, this.enableAudio);
        this.display = selected;
        this.hid.setDisplay(selected);
        CastLog.INSTANCE.display(selected.getWidth(), selected.getHeight(), selected.getFps());
        this.log.invoke("车机显示 " + selected.getWidth() + "×" + selected.getHeight() + "@" + selected.getFps());
        while (!this.closed.get()) {
            Thread.sleep(500L);
            if (!this.vehicle.isAlive()) {
                throw new IllegalStateException("车机无线网络已断开");
            }
            if (!this.bluetoothAlive.invoke().booleanValue()) {
                if (!this.wifiControlReady) {
                    throw new IllegalStateException("蓝牙链路已断开，车机将重置会话");
                }
                if (this.bluetoothReleased.compareAndSet(false, true)) {
                    this.log.invoke("车机已完成无线交接并释放蓝牙链路；会话继续走 Wi-Fi");
                }
            }
        }
    }

    /* JADX WARN: Type inference failed for: r8v8, types: [T, java.net.InetAddress] */
    /* JADX WARN: Type inference failed for: r8v9, types: [T, java.net.InetAddress] */
    static final Socket run$lambda$8(AirPlayLink this$0, List $targets, Ref.ObjectRef $receiver, Ref.ObjectRef $local, int port) throws IOException {
        Socket socket = new Socket();
        this$0.vehicle.bind(socket);
        boolean connected = false;
        Exception lastFailure = null;
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10L);
        Iterator it = $targets.iterator();
        while (it.hasNext()) {
            PeerAddress target = (PeerAddress) it.next();
            int remaining = (int) RangesKt.coerceAtLeast((deadline - System.nanoTime()) / 1000000, 1L);
            try {
                socket.bind(new InetSocketAddress(target.getLocal(), 0));
                socket.connect(new InetSocketAddress(target.getRemote(), port), remaining);
                $receiver.element = target.getRemote();
                $local.element = target.getLocal();
                connected = true;
                break;
            } catch (Exception failure) {
                lastFailure = failure;
            }
        }
        if (!connected) {
            socket.close();
            throw new IOException("车机服务连接失败", lastFailure);
        }
        Function1<String, Unit> function1 = this$0.log;
        LinkProperties link = this$0.vehicle.getLink();
        function1.invoke("车机 TCP 已固定：interface=" + (link != null ? link.getInterfaceName() : null) + "，IPv6=" + (socket.getLocalAddress() instanceof Inet6Address) + "，port=" + port);
        return socket;
    }

    static final byte[] run$lambda$9(AirPlayLink this$0, PhonePairing pairing) {
        Intrinsics.checkNotNullParameter(pairing, "pairing");
        return this$0.pairingFor(pairing);
    }

    static final Unit run$lambda$10(AirPlayLink this$0, DatagramSocket it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.vehicle.bindDatagram(it);
        return Unit.INSTANCE;
    }

    static final Unit run$lambda$11(AirPlayLink this$0, PairedAccessory it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.settings.savePair(this$0.peerAddress, it);
        return Unit.INSTANCE;
    }

    static final LegacySecurity run$lambda$13(AirPlayLink this$0, final RtspWire wire) {
        Intrinsics.checkNotNullParameter(wire, "wire");
        this$0.log.invoke("车机使用旧版 MFiSAP 认证");
        return MfiController.INSTANCE.authenticate(new Function1() { // from class: com.carlink.cast.AirPlayLink$$ExternalSyntheticLambda10
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AirPlayLink.run$lambda$13$lambda$12(wire, (byte[]) obj);
            }
        }, this$0.trustFor.invoke(true));
    }

    static final byte[] run$lambda$13$lambda$12(RtspWire $wire, byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        return RtspWire.request$default($wire, "POST", "/auth-setup", bytes, MapsKt.mapOf(TuplesKt.to("Content-Type", AirPlaySession.OCTET_CONTENT_TYPE)), null, 16, null).getBody();
    }

    static final List run$lambda$15(AirPlayLink this$0, List candidates) {
        Intrinsics.checkNotNullParameter(candidates, "candidates");
        return this$0.audioProfiles.order(candidates);
    }

    static final Unit run$lambda$16(AirPlayLink this$0, SenderAudioFormat profile) {
        Intrinsics.checkNotNullParameter(profile, "profile");
        this$0.audioProfiles.recordWorking(profile);
        this$0.log.invoke("车机音频档位已记忆：" + profile.getKey());
        return Unit.INSTANCE;
    }

    static final Unit run$lambda$17(AirPlayLink this$0, SenderAudioFormat profile) {
        Intrinsics.checkNotNullParameter(profile, "profile");
        this$0.audioProfiles.recordRejected(profile);
        this$0.log.invoke("车机音频档位已后置：" + profile.getKey() + "（SETUP 后实际音频流未稳定）");
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static final InetAddress run$lambda$14(Ref.ObjectRef $local) {
        return (InetAddress) $local.element;
    }

    private final List<PeerAddress> resolveTargets() throws IOException {
        Object objM159constructorimpl;
        if (!this.endpoint.getAddresses().isEmpty()) {
            List<PeerAddress> listResolveTargets = this.vehicle.resolveTargets(this.endpoint.getAddresses());
            if (!listResolveTargets.isEmpty()) {
                return listResolveTargets;
            }
            this.log.invoke("0x4301 地址与本机网络不匹配，改用 Bonjour 发现");
        }
        Network network = this.vehicle.getNetwork();
        if (network == null) {
            throw new IllegalStateException("车机网络尚未就绪".toString());
        }
        Context context = this.context;
        InetAddress inetAddressLocalAddress = this.vehicle.localAddress();
        ControlInvitationServer controlInvitationServer = this.controlServer;
        AirPlayDiscovery airPlayDiscovery = new AirPlayDiscovery(context, network, inetAddressLocalAddress, controlInvitationServer != null ? controlInvitationServer.getPort() : 0, this.identity.getId(), this.log);
        try {
            AirPlayDiscovery airPlayDiscovery2 = airPlayDiscovery;
            try {
                Result.Companion companion = Result.INSTANCE;
                AirPlayLink airPlayLink = this;
                airPlayDiscovery2.start();
                objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl);
            if (thM162exceptionOrNullimpl != null) {
                this.log.invoke("Bonjour 启动失败：" + thM162exceptionOrNullimpl.getMessage());
            }
            AirPlayDiscovery.Found foundAwait$default = AirPlayDiscovery.await$default(airPlayDiscovery2, 0L, 1, null);
            if (foundAwait$default == null) {
                throw new IllegalStateException("未发现车机 AirPlay 服务；请确认车机屏幕已进入 CarPlay".toString());
            }
            VehicleNetwork vehicleNetwork = this.vehicle;
            Iterable addresses = foundAwait$default.getAddresses();
            Collection arrayList = new ArrayList();
            Iterator it = addresses.iterator();
            while (it.hasNext()) {
                String hostAddress = ((InetAddress) it.next()).getHostAddress();
                if (hostAddress != null) {
                    arrayList.add(hostAddress);
                }
            }
            List<PeerAddress> listResolveTargets2 = vehicleNetwork.resolveTargets((List) arrayList);
            if (listResolveTargets2.isEmpty()) {
                throw new IllegalArgumentException("发现的车机地址与本机网络不匹配".toString());
            }
            this.discoveredPort = Integer.valueOf(foundAwait$default.getPort());
            CloseableKt.closeFinally(airPlayDiscovery, null);
            return listResolveTargets2;
        } catch (Throwable th2) {
            try {
                throw th2;
            } catch (Throwable th3) {
                CloseableKt.closeFinally(airPlayDiscovery, th2);
                throw th3;
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:25:0x0055  */
    /* JADX WARN: Code duplicated, block: B:43:0x00b5  */
    private final byte[] pairingFor(PhonePairing pairing) {
        byte[] peerKey;
        Iterable iterableChunked;
        boolean z;
        boolean z2;
        PairedAccessory saved = this.settings.paired(this.peerAddress);
        String publicKey = this.endpoint.getPublicKey();
        if (publicKey != null) {
            boolean z3 = false;
            if (publicKey.length() == 64) {
                String str = publicKey;
                int i = 0;
                while (true) {
                    if (i >= str.length()) {
                        z = true;
                        break;
                    }
                    char cCharAt = str.charAt(i);
                    if ('0' <= cCharAt && cCharAt < ':') {
                        z2 = true;
                    } else {
                        char lowerCase = Character.toLowerCase(cCharAt);
                        if ('a' <= lowerCase && lowerCase < 'g') {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                    }
                    if (!z2) {
                        z = false;
                        break;
                    }
                    i++;
                }
                if (z) {
                    z3 = true;
                }
            }
            if (!z3) {
                publicKey = null;
            }
            if (publicKey == null || (iterableChunked = StringsKt.chunked(publicKey, 2)) == null) {
                peerKey = null;
            } else {
                Iterable iterable = iterableChunked;
                Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
                Iterator it = iterable.iterator();
                while (it.hasNext()) {
                    arrayList.add(Byte.valueOf((byte) Integer.parseInt((String) it.next(), CharsKt.checkRadix(16))));
                }
                peerKey = CollectionsKt.toByteArray((List) arrayList);
            }
        } else {
            peerKey = null;
        }
        if (saved != null) {
            if (peerKey != null) {
                try {
                    if (!AccessoryTrust.INSTANCE.equal(saved.getPublicKey(), peerKey)) {
                        throw new IllegalStateException("车机配对公钥已变化，请清除配对记录后重试".toString());
                    }
                } catch (Exception failure) {
                    this.log.invoke("已保存配对失效（" + failure.getMessage() + "），重新配对");
                    this.settings.removePair(this.peerAddress);
                }
            }
            this.log.invoke("使用已保存的车机配对身份");
            return pairing.verify(saved);
        }
        PairedAccessory created = PhonePairing.setup$default(pairing, PairSetup.SETUP_CODE, peerKey, null, 4, null);
        this.settings.savePair(this.peerAddress, created);
        return pairing.verify(created);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void handleControlMessage(Iap2Session runtime, Iap2Frame frame) {
        if (SetsKt.setOf((Object[]) new Integer[]{26624, 26626, 26627}).contains(Integer.valueOf(frame.getMessageId()))) {
            Function1<String, Unit> function1 = this.log;
            String string = Integer.toString(frame.getMessageId(), CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            function1.invoke("Wi-Fi iAP2 HID 0x" + string + " 长度=" + frame.getPayload().length + " hex=" + ArraysKt.joinToString$default(frame.getPayload(), (CharSequence) " ", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, new Function1() { // from class: com.carlink.cast.AirPlayLink$$ExternalSyntheticLambda11
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return AirPlayLink.handleControlMessage$lambda$28(((Byte) obj).byteValue());
                }
            }, 30, (Object) null));
            this.hid.handle(frame, new AnonymousClass2(runtime));
        }
        switch (frame.getMessageId()) {
            case 16724:
            case 16727:
            case 16752:
                Iap2Frame iap2FrameForRequest = Iap2SubscriptionReplies.INSTANCE.forRequest(frame.getMessageId());
                if (iap2FrameForRequest != null) {
                    Iap2Session.send$default(runtime, iap2FrameForRequest, 0L, 2, null);
                    Function1<String, Unit> function2 = this.log;
                    String string2 = Integer.toString(frame.getMessageId(), CharsKt.checkRadix(16));
                    Intrinsics.checkNotNullExpressionValue(string2, "toString(...)");
                    function2.invoke("Wi-Fi iAP2 0x" + string2 + "：已回复空闲订阅状态");
                }
                break;
            case 20480:
                this.nowPlayingSubscription = this.nowPlaying.subscribe(frame, new AnonymousClass3(runtime), new AnonymousClass4(runtime));
                this.log.invoke("Wi-Fi iAP2 0x5000：已开始真实 NowPlaying 发布");
                break;
            case 20482:
                this.nowPlaying.unsubscribe(Long.valueOf(this.nowPlayingSubscription));
                this.log.invoke("Wi-Fi iAP2 0x5002：已停止 NowPlaying 发布");
                break;
            default:
                Function1<String, Unit> function3 = this.log;
                String string3 = Integer.toString(frame.getMessageId(), CharsKt.checkRadix(16));
                Intrinsics.checkNotNullExpressionValue(string3, "toString(...)");
                function3.invoke("Wi-Fi 控制消息 0x" + string3);
                break;
        }
    }

    /* JADX INFO: renamed from: com.carlink.cast.AirPlayLink$handleControlMessage$2, reason: invalid class name */
    /* JADX INFO: compiled from: AirPlayLink.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass2 extends AdaptedFunctionReference implements Function1<Iap2Frame, Unit> {
        AnonymousClass2(Object obj) {
            super(1, obj, Iap2Session.class, "send", "send(Lcom/carplay/iap2/wire/Iap2Frame;J)V", 0);
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(Iap2Frame iap2Frame) {
            invoke2(iap2Frame);
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2(Iap2Frame p0) {
            Intrinsics.checkNotNullParameter(p0, "p0");
            Iap2Session.send$default((Iap2Session) this.receiver, p0, 0L, 2, null);
        }
    }

    static final CharSequence handleControlMessage$lambda$28(byte it) {
        String str = String.format("%02X", Arrays.copyOf(new Object[]{Byte.valueOf(it)}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    /* JADX INFO: renamed from: com.carlink.cast.AirPlayLink$handleControlMessage$3, reason: invalid class name */
    /* JADX INFO: compiled from: AirPlayLink.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass3 extends AdaptedFunctionReference implements Function1<Iap2Frame, Unit> {
        AnonymousClass3(Object obj) {
            super(1, obj, Iap2Session.class, "send", "send(Lcom/carplay/iap2/wire/Iap2Frame;J)V", 0);
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(Iap2Frame iap2Frame) {
            invoke2(iap2Frame);
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2(Iap2Frame p0) {
            Intrinsics.checkNotNullParameter(p0, "p0");
            Iap2Session.send$default((Iap2Session) this.receiver, p0, 0L, 2, null);
        }
    }

    /* JADX INFO: renamed from: com.carlink.cast.AirPlayLink$handleControlMessage$4, reason: invalid class name */
    /* JADX INFO: compiled from: AirPlayLink.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass4 extends AdaptedFunctionReference implements Function1<byte[], Boolean> {
        AnonymousClass4(Object obj) {
            super(1, obj, Iap2Session.class, "sendFileTransfer", "sendFileTransfer([BJ)Z", 0);
        }

        @Override // kotlin.jvm.functions.Function1
        public final Boolean invoke(byte[] p0) {
            Intrinsics.checkNotNullParameter(p0, "p0");
            return Boolean.valueOf(Iap2Session.sendFileTransfer$default((Iap2Session) this.receiver, p0, 0L, 2, null));
        }
    }

    /* JADX INFO: renamed from: com.carlink.cast.AirPlayLink$events$1, reason: invalid class name */
    /* JADX INFO: compiled from: AirPlayLink.kt */
    @Metadata(d1 = {"\u0000_\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010$\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0016J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u0007H\u0016J\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u0007H\u0016J\b\u0010\n\u001a\u00020\u0003H\u0016J\b\u0010\u000b\u001a\u00020\u0003H\u0016J\u0018\u0010\f\u001a\u00020\r2\u000e\u0010\u000e\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u000fH\u0016J\u0010\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\rH\u0016J\u0018\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0017\u001a\u00020\u00032\u0006\u0010\u0018\u001a\u00020\u0016H\u0016J\b\u0010\u0019\u001a\u00020\u0003H\u0016J\u0010\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u001b\u001a\u00020\u001cH\u0016J\b\u0010\u001d\u001a\u00020\rH\u0016J\u0016\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00030\u001f2\u0006\u0010 \u001a\u00020!H\u0016J\u0010\u0010\"\u001a\u00020\u00032\u0006\u0010#\u001a\u00020$H\u0002J\u0010\u0010%\u001a\u00020\u00032\u0006\u0010&\u001a\u00020'H\u0016¨\u0006("}, d2 = {"com/carlink/cast/AirPlayLink$events$1", "Lcom/carplay/sender/SenderEvents;", "diagnostic", "", "message", "", "outputDisplay", "Lcom/carplay/sender/SenderDisplay;", "display", "startDisplay", "keyFrameRequested", "screenRestoreKeyFrameRequested", "input", "", "command", "", "screenOwnership", "owned", "sent", "bytes", "", "queuedNanos", "", "dropped", "count", "audioSent", "audioFormat", "format", "Lcom/carplay/sender/SenderAudioFormat;", "bluetoothControlAvailable", "iapControl", "Ljava/util/concurrent/CompletableFuture;", "stream", "Lcom/carplay/iap2/transport/BlockingDuplexByteStream;", "scheduleDefaultNowPlaying", "runtime", "Lcom/carplay/iap2/session/Iap2Session;", "failed", "error", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class AnonymousClass1 implements SenderEvents {
        AnonymousClass1() {
        }

        @Override // com.carplay.sender.SenderEvents
        public void diagnostic(String message) {
            Intrinsics.checkNotNullParameter(message, "message");
            AirPlayLink.this.log.invoke(message);
        }

        /* JADX WARN: Code duplicated, block: B:17:0x00a5  */
        @Override // com.carplay.sender.SenderEvents
        public SenderDisplay outputDisplay(SenderDisplay display) {
            String str;
            Intrinsics.checkNotNullParameter(display, "display");
            PhoneProjectionAdapter phoneProjectionAdapter = AirPlayLink.this.source;
            CarLifeProjectionAdapter carLife = phoneProjectionAdapter instanceof CarLifeProjectionAdapter ? (CarLifeProjectionAdapter) phoneProjectionAdapter : null;
            if (carLife == null) {
                return display;
            }
            CarLifePanel panel = CarPanelGeometry.INSTANCE.panelFor(display);
            carLife.setPanel(panel);
            CarPanelGeometry.SafeAreaPolicy safeAreaPolicy = AirPlayLink.this.settings.safeAreaPolicy(AirPlayLink.this.peerAddress);
            carLife.setSafeAreaPolicy(safeAreaPolicy);
            CarLifeVideoProcessor videoProcessor = carLife.getVideoProcessor();
            CarLifePanelCompositor carLifePanelCompositor = videoProcessor instanceof CarLifePanelCompositor ? (CarLifePanelCompositor) videoProcessor : null;
            if (carLifePanelCompositor != null) {
                carLifePanelCompositor.updateSafeAreaPolicy(safeAreaPolicy);
                carLifePanelCompositor.updatePanel(panel);
            }
            Function1 function1 = AirPlayLink.this.log;
            int width = display.getWidth();
            int height = display.getHeight();
            SenderSafeArea safeArea = display.getSafeArea();
            if (safeArea != null) {
                str = "，safeArea=" + safeArea.getWidth() + "x" + safeArea.getHeight() + Marker.ANY_NON_NULL_MARKER + safeArea.getX() + "," + safeArea.getY();
                if (str == null) {
                    str = "，未声明 safeArea";
                }
            } else {
                str = "，未声明 safeArea";
            }
            function1.invoke("按车机主显示准备 CarLife 全画布：" + width + "x" + height + str + "；安全区方案=" + safeAreaPolicy.getMode().name() + "，启用=" + safeAreaPolicy.getEnabled());
            return display;
        }

        @Override // com.carplay.sender.SenderEvents
        public void display(SenderDisplay display) throws InterruptedException {
            Intrinsics.checkNotNullParameter(display, "display");
            Integer numValueOf = Integer.valueOf(AirPlayLink.this.settings.getFrameRateOverride());
            if (!(numValueOf.intValue() > 0)) {
                numValueOf = null;
            }
            int requested = numValueOf != null ? numValueOf.intValue() : display.getFps();
            if (requested != display.getFps()) {
                AirPlayLink.this.log.invoke("帧率覆盖：车机声明 " + display.getFps() + "fps，改用 " + requested + "fps");
            }
            CastLog.INSTANCE.stage(CastStage.MEDIA, "车机显示 " + display.getWidth() + "×" + display.getHeight() + "，准备画面");
            BuildersKt__BuildersKt.runBlocking$default(null, new AirPlayLink$events$1$display$1(AirPlayLink.this, display, requested, null), 1, null);
        }

        @Override // com.carplay.sender.SenderEvents
        public void startDisplay(SenderDisplay display) throws InterruptedException {
            Intrinsics.checkNotNullParameter(display, "display");
            AirPlaySender current = AirPlayLink.this.sender;
            if (current != null) {
                BuildersKt__BuildersKt.runBlocking$default(null, new AirPlayLink$events$1$startDisplay$1(AirPlayLink.this, current, null), 1, null);
                AirPlayLink.this.log.invoke("屏幕 SETUP/RECORD 已完成，已启动画面编码器");
                return;
            }
            throw new IllegalStateException("发送端尚未就绪".toString());
        }

        @Override // com.carplay.sender.SenderEvents
        public void keyFrameRequested() {
            AirPlayLink.this.source.requestKeyFrame();
        }

        @Override // com.carplay.sender.SenderEvents
        public void screenRestoreKeyFrameRequested() {
            AirPlayLink.this.source.requestScreenRestoreKeyFrame();
        }

        @Override // com.carplay.sender.SenderEvents
        public boolean input(Map<?, ?> command) {
            Intrinsics.checkNotNullParameter(command, "command");
            return Dispatch.INSTANCE.input(command, AirPlayLink.this.source, AirPlayLink.this.display);
        }

        @Override // com.carplay.sender.SenderEvents
        public void screenOwnership(boolean owned) {
            AirPlayLink.this.log.invoke(owned ? "车机已将屏幕交给手机" : "车机切回原生界面，暂停画面");
        }

        @Override // com.carplay.sender.SenderEvents
        public void sent(int bytes, long queuedNanos) {
            if (CastLog.INSTANCE.getState().getValue().getFrames() == 0) {
                CastLog.INSTANCE.stage(CastStage.STREAMING, "画面已发送到车机");
            }
            CastLog.video$default(CastLog.INSTANCE, bytes, false, 2, null);
        }

        @Override // com.carplay.sender.SenderEvents
        public void dropped(long count) {
            int i = (int) count;
            for (int i2 = 0; i2 < i; i2++) {
                CastLog.INSTANCE.video(0, true);
            }
        }

        @Override // com.carplay.sender.SenderEvents
        public void audioSent() {
            CastLog.INSTANCE.audio();
        }

        @Override // com.carplay.sender.SenderEvents
        public void audioFormat(SenderAudioFormat format) {
            Intrinsics.checkNotNullParameter(format, "format");
            AirPlayLink.this.source.configureAudio(format.getSampleRate(), format.getChannels(), format.isPcm());
            Function1 function1 = AirPlayLink.this.log;
            String str = format.isPcm() ? "LPCM" : "AAC-LC";
            function1.invoke("音频：" + str + " " + format.getSampleRate() + "Hz / " + format.getChannels() + " 声道（type " + format.getStreamType() + "）");
        }

        @Override // com.carplay.sender.SenderEvents
        public boolean bluetoothControlAvailable() {
            boolean alive = !AirPlayLink.this.closed.get() && ((Boolean) AirPlayLink.this.bluetoothAlive.invoke()).booleanValue();
            AirPlayLink.this.log.invoke("蓝牙控制通道：可用=" + alive);
            return alive;
        }

        @Override // com.carplay.sender.SenderEvents
        public CompletableFuture<Unit> iapControl(final BlockingDuplexByteStream stream) {
            Intrinsics.checkNotNullParameter(stream, "stream");
            final CompletableFuture<Unit> completableFuture = new CompletableFuture<>();
            final AirPlayLink airPlayLink = AirPlayLink.this;
            Thread thread = new Thread(new Runnable() { // from class: com.carlink.cast.AirPlayLink$events$1$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    AirPlayLink.AnonymousClass1.iapControl$lambda$11(stream, airPlayLink, completableFuture, this);
                }
            }, "carlink-wifi-control");
            thread.setDaemon(true);
            thread.start();
            return completableFuture;
        }

        static final void iapControl$lambda$11(BlockingDuplexByteStream $stream, final AirPlayLink this$0, CompletableFuture $done, AnonymousClass1 this$1) {
            boolean z;
            try {
                PhoneIapTunnel phoneIapTunnel = $stream instanceof PhoneIapTunnel ? (PhoneIapTunnel) $stream : null;
                boolean z2 = true;
                int i = 2;
                final Iap2Session runtime = phoneIapTunnel != null && phoneIapTunnel.isCommandTunnel() ? Iap2Session.INSTANCE.commandTunnel($stream, new Function1() { // from class: com.carlink.cast.AirPlayLink$events$1$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return AirPlayLink.AnonymousClass1.iapControl$lambda$11$lambda$4(this$0, (String) obj);
                    }
                }, new Function1() { // from class: com.carlink.cast.AirPlayLink$events$1$$ExternalSyntheticLambda1
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return AirPlayLink.AnonymousClass1.iapControl$lambda$11$lambda$5(this$0, (byte[]) obj);
                    }
                }) : Iap2Session.Companion.controllerTunnel$default(Iap2Session.INSTANCE, $stream, null, 2, null);
                try {
                    if (!runtime.awaitReady(15000L)) {
                        throw new IllegalStateException("Wi-Fi iAP2 链路同步超时".toString());
                    }
                    CastLog.INSTANCE.stage(CastStage.PAIR, "Wi-Fi 控制通道已同步，独立验证车机");
                    AccessoryAuthentication trust = (AccessoryAuthentication) this$0.trustFor.invoke(false);
                    PhoneBootstrap control = new PhoneBootstrap(this$0.identity.getId(), new AirPlayLink$events$1$iapControl$1$control$1(trust), new AirPlayLink$events$1$iapControl$1$control$2(trust), new AirPlayLink$events$1$iapControl$1$control$3(runtime), this$0.log, new Function1() { // from class: com.carlink.cast.AirPlayLink$events$1$$ExternalSyntheticLambda2
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return AirPlayLink.AnonymousClass1.iapControl$lambda$11$lambda$7((WirelessEndpoint) obj);
                        }
                    }, new Function1() { // from class: com.carlink.cast.AirPlayLink$events$1$$ExternalSyntheticLambda3
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return AirPlayLink.AnonymousClass1.iapControl$lambda$11$lambda$8(this$0, runtime, (Iap2Frame) obj);
                        }
                    }, null, PhoneBootstrapProfile.MODERN_WIRELESS, new Function0() { // from class: com.carlink.cast.AirPlayLink$events$1$$ExternalSyntheticLambda4
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return this$0.identity.getId();
                        }
                    }, (byte[]) this$0.bluetoothIdentity.invoke(), PhoneBootstrapPurpose.CONTROL_SESSION, 128, null);
                    control.begin();
                    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(30L);
                    int identifyRetries = 0;
                    long nextIdentifyRetry = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(CarLifeProjectionAdapter.HOST_START_DEBOUNCE_MS);
                    while (!this$0.closed.get()) {
                        if (!control.status().getControlReady()) {
                            if (!(System.nanoTime() < deadline ? z2 : false)) {
                                throw new IllegalStateException("Wi-Fi iAP2 认证超时".toString());
                            }
                        }
                        if (!control.status().getIdentified() && identifyRetries < i && System.nanoTime() >= nextIdentifyRetry) {
                            identifyRetries++;
                            control.begin();
                            long nextIdentifyRetry2 = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(3000L);
                            this$0.log.invoke("Wi-Fi iAP2 尚未收到 0x1d01，重发 StartIdentification（第 " + identifyRetries + " 次）");
                            nextIdentifyRetry = nextIdentifyRetry2;
                        }
                        Iap2Frame frame = runtime.recv(1000L);
                        if (frame == null) {
                            z = true;
                        } else {
                            control.receive(frame);
                            if (!control.status().getControlReady() || $done.isDone()) {
                                z = true;
                            } else {
                                runtime.awaitOutboundAcknowledged(3000L);
                                z = true;
                                this$0.wifiControlReady = true;
                                this$0.log.invoke("Wi-Fi 控制通道独立验证完成；蓝牙链路保持连接");
                                $done.complete(Unit.INSTANCE);
                                this$1.scheduleDefaultNowPlaying(runtime);
                            }
                        }
                        z2 = z;
                        i = 2;
                    }
                    return;
                } catch (Throwable th) {
                    failure = th;
                }
            } catch (Throwable th2) {
                failure = th2;
            }
            $done.completeExceptionally(failure);
        }

        static final Unit iapControl$lambda$11$lambda$4(AirPlayLink this$0, String it) {
            Intrinsics.checkNotNullParameter(it, "it");
            this$0.log.invoke("iAP2：" + it);
            return Unit.INSTANCE;
        }

        static final Unit iapControl$lambda$11$lambda$5(AirPlayLink this$0, byte[] datagram) {
            Intrinsics.checkNotNullParameter(datagram, "datagram");
            this$0.nowPlaying.onFileTransferDatagram(datagram);
            return Unit.INSTANCE;
        }

        static final Unit iapControl$lambda$11$lambda$7(WirelessEndpoint it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return Unit.INSTANCE;
        }

        static final Unit iapControl$lambda$11$lambda$8(AirPlayLink this$0, Iap2Session $runtime, Iap2Frame frame) {
            Intrinsics.checkNotNullParameter(frame, "frame");
            this$0.handleControlMessage($runtime, frame);
            return Unit.INSTANCE;
        }

        private final void scheduleDefaultNowPlaying(final Iap2Session runtime) {
            final AirPlayLink airPlayLink = AirPlayLink.this;
            Thread thread = new Thread(new Runnable() { // from class: com.carlink.cast.AirPlayLink$events$1$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    AirPlayLink.AnonymousClass1.scheduleDefaultNowPlaying$lambda$14(airPlayLink, runtime);
                }
            }, "carlink-now-playing-fallback");
            thread.setDaemon(true);
            thread.start();
        }

        static final void scheduleDefaultNowPlaying$lambda$14(AirPlayLink this$0, Iap2Session $runtime) {
            try {
                Thread.sleep(CarLifeProjectionAdapter.HOST_START_DEBOUNCE_MS);
                if (!this$0.closed.get() && !this$0.nowPlaying.hasSubscription()) {
                    this$0.nowPlaying.subscribeDefault(new AirPlayLink$events$1$scheduleDefaultNowPlaying$1$1($runtime), new AirPlayLink$events$1$scheduleDefaultNowPlaying$1$2($runtime));
                }
            } catch (InterruptedException e) {
            } catch (Throwable failure) {
                if (!this$0.closed.get()) {
                    this$0.log.invoke("NowPlaying 默认订阅失败：" + failure.getMessage());
                }
            }
        }

        @Override // com.carplay.sender.SenderEvents
        public void failed(Throwable error) {
            Intrinsics.checkNotNullParameter(error, "error");
            if (!AirPlayLink.this.closed.get() && AirPlayLink.this.failed.compareAndSet(false, true)) {
                String simpleName = error.getClass().getSimpleName();
                String message = error.getMessage();
                if (message == null) {
                    message = "无消息";
                }
                String detail = simpleName + ": " + message;
                AirPlayLink.this.log.invoke("AirPlay 会话异常：" + detail);
                AirPlayLink.this.terminalFailure.invoke(error);
                AirPlayLink.this.close();
            }
        }
    }

    private final AnonymousClass1 events() {
        return new AnonymousClass1();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        Object objM159constructorimpl3;
        if (this.closed.compareAndSet(false, true)) {
            Unit unit = null;
            try {
                Result.Companion companion = Result.INSTANCE;
                BuildersKt__BuildersKt.runBlocking$default(null, new AirPlayLink$close$1$1(this, null), 1, null);
                objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl);
            if (thM162exceptionOrNullimpl != null) {
                this.log.invoke("画面来源释放失败：" + thM162exceptionOrNullimpl.getClass().getSimpleName() + ": " + thM162exceptionOrNullimpl.getMessage());
            }
            try {
                Result.Companion companion3 = Result.INSTANCE;
                AirPlaySender airPlaySender = this.sender;
                if (airPlaySender != null) {
                    airPlaySender.close();
                    unit = Unit.INSTANCE;
                }
                objM159constructorimpl2 = Result.m159constructorimpl(unit);
            } catch (Throwable th2) {
                Result.Companion companion4 = Result.INSTANCE;
                objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th2));
            }
            Throwable thM162exceptionOrNullimpl2 = Result.m162exceptionOrNullimpl(objM159constructorimpl2);
            if (thM162exceptionOrNullimpl2 != null) {
                this.log.invoke("AirPlay 发送端关闭失败：" + thM162exceptionOrNullimpl2.getMessage());
            }
            synchronized (this.resources) {
                for (Closeable closeable : CollectionsKt.reversed(this.resources)) {
                    try {
                        Result.Companion companion5 = Result.INSTANCE;
                        AirPlayLink airPlayLink = this;
                        closeable.close();
                        objM159constructorimpl3 = Result.m159constructorimpl(Unit.INSTANCE);
                    } catch (Throwable th3) {
                        Result.Companion companion6 = Result.INSTANCE;
                        objM159constructorimpl3 = Result.m159constructorimpl(ResultKt.createFailure(th3));
                    }
                    Throwable thM162exceptionOrNullimpl3 = Result.m162exceptionOrNullimpl(objM159constructorimpl3);
                    if (thM162exceptionOrNullimpl3 != null) {
                        this.log.invoke("AirPlay 资源关闭失败：" + thM162exceptionOrNullimpl3.getMessage());
                    }
                }
                this.resources.clear();
                Unit unit2 = Unit.INSTANCE;
            }
            this.nowPlaying.close();
            Thread thread = this.worker;
            if (thread != null) {
                thread.interrupt();
            }
        }
    }
}
