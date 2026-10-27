package com.carlink.cast;

import android.content.Context;
import android.net.Network;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: Discovery.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000\u008e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003*\u0002#&\b\u0000\u0018\u00002\u00020\u0001:\u00014BC\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000e0\r¢\u0006\u0004\b\u000f\u0010\u0010J2\u0010(\u001a\u00020\u000e2\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00070*2\u0006\u0010+\u001a\u00020\t2\u0012\u0010,\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020.0-H\u0002J\u0006\u0010/\u001a\u00020\u000eJ\u0012\u00100\u001a\u0004\u0018\u00010\u00152\b\b\u0002\u00101\u001a\u000202J\b\u00103\u001a\u00020\u000eH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000e0\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0016\u001a\n \u0018*\u0004\u0018\u00010\u00170\u0017X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001cX\u0082\u000e¢\u0006\u0002\n\u0000RN\u0010\u001e\u001aB\u0012\f\u0012\n \u0018*\u0004\u0018\u00010\u000b0\u000b\u0012\f\u0012\n \u0018*\u0004\u0018\u00010\u001c0\u001c \u0018* \u0012\f\u0012\n \u0018*\u0004\u0018\u00010\u000b0\u000b\u0012\f\u0012\n \u0018*\u0004\u0018\u00010\u001c0\u001c\u0018\u00010\u001f0\u001fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u00020#X\u0082\u0004¢\u0006\u0004\n\u0002\u0010$R\u0010\u0010%\u001a\u00020&X\u0082\u0004¢\u0006\u0004\n\u0002\u0010'¨\u00065"}, d2 = {"Lcom/carlink/cast/AirPlayDiscovery;", "Ljava/io/Closeable;", "context", "Landroid/content/Context;", "network", "Landroid/net/Network;", "local", "Ljava/net/InetAddress;", "controlPort", "", "transportId", "", "log", "Lkotlin/Function1;", "", "<init>", "(Landroid/content/Context;Landroid/net/Network;Ljava/net/InetAddress;ILjava/lang/String;Lkotlin/jvm/functions/Function1;)V", "closed", "Ljava/util/concurrent/atomic/AtomicBoolean;", "result", "Ljava/util/concurrent/CompletableFuture;", "Lcom/carlink/cast/AirPlayDiscovery$Found;", "manager", "Landroid/net/nsd/NsdManager;", "kotlin.jvm.PlatformType", "legacy", "Ljavax/jmdns/JmDNS;", "registered", "", "discovering", "resolving", "Ljava/util/concurrent/ConcurrentHashMap$KeySetView;", "executor", "Ljava/util/concurrent/Executor;", "registration", "com/carlink/cast/AirPlayDiscovery$registration$1", "Lcom/carlink/cast/AirPlayDiscovery$registration$1;", "listener", "com/carlink/cast/AirPlayDiscovery$listener$1", "Lcom/carlink/cast/AirPlayDiscovery$listener$1;", "deliver", "addresses", "", "port", "attributes", "", "", "start", "await", "timeoutMillis", "", "close", "Found", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AirPlayDiscovery implements Closeable {
    private final AtomicBoolean closed;
    private final Context context;
    private final int controlPort;
    private boolean discovering;
    private final Executor executor;
    private JmDNS legacy;
    private final AirPlayDiscovery$listener$1 listener;
    private final InetAddress local;
    private final Function1<String, Unit> log;
    private final NsdManager manager;
    private final Network network;
    private boolean registered;
    private final AirPlayDiscovery$registration$1 registration;
    private final ConcurrentHashMap.KeySetView<String, Boolean> resolving;
    private final CompletableFuture<Found> result;
    private final String transportId;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v12, types: [com.carlink.cast.AirPlayDiscovery$registration$1] */
    /* JADX WARN: Type inference failed for: r0v13, types: [com.carlink.cast.AirPlayDiscovery$listener$1] */
    public AirPlayDiscovery(Context context, Network network, InetAddress local, int controlPort, String transportId, Function1<? super String, Unit> log) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(network, "network");
        Intrinsics.checkNotNullParameter(local, "local");
        Intrinsics.checkNotNullParameter(transportId, "transportId");
        Intrinsics.checkNotNullParameter(log, "log");
        this.context = context;
        this.network = network;
        this.local = local;
        this.controlPort = controlPort;
        this.transportId = transportId;
        this.log = log;
        this.closed = new AtomicBoolean();
        this.result = new CompletableFuture<>();
        this.manager = (NsdManager) this.context.getSystemService(NsdManager.class);
        this.resolving = ConcurrentHashMap.newKeySet();
        this.executor = new Executor() { // from class: com.carlink.cast.AirPlayDiscovery$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                runnable.run();
            }
        };
        this.registration = new NsdManager.RegistrationListener() { // from class: com.carlink.cast.AirPlayDiscovery$registration$1
            @Override // android.net.nsd.NsdManager.RegistrationListener
            public void onServiceRegistered(NsdServiceInfo info) {
                Intrinsics.checkNotNullParameter(info, "info");
                boolean z = this.this$0.closed.get();
                AirPlayDiscovery airPlayDiscovery = this.this$0;
                if (z) {
                    try {
                        Result.Companion companion = Result.INSTANCE;
                        airPlayDiscovery.manager.unregisterService(this);
                        Result.m159constructorimpl(Unit.INSTANCE);
                        return;
                    } catch (Throwable th) {
                        Result.Companion companion2 = Result.INSTANCE;
                        Result.m159constructorimpl(ResultKt.createFailure(th));
                        return;
                    }
                }
                airPlayDiscovery.log.invoke("控制服务已发布到车机网络：" + info.getServiceName());
            }

            @Override // android.net.nsd.NsdManager.RegistrationListener
            public void onRegistrationFailed(NsdServiceInfo info, int code) {
                Intrinsics.checkNotNullParameter(info, "info");
                this.this$0.registered = false;
                this.this$0.log.invoke("控制服务发布失败：" + code);
            }

            @Override // android.net.nsd.NsdManager.RegistrationListener
            public void onServiceUnregistered(NsdServiceInfo info) {
                Intrinsics.checkNotNullParameter(info, "info");
            }

            @Override // android.net.nsd.NsdManager.RegistrationListener
            public void onUnregistrationFailed(NsdServiceInfo info, int code) {
                Intrinsics.checkNotNullParameter(info, "info");
            }
        };
        this.listener = new NsdManager.DiscoveryListener() { // from class: com.carlink.cast.AirPlayDiscovery$listener$1
            @Override // android.net.nsd.NsdManager.DiscoveryListener
            public void onDiscoveryStarted(String type) {
                Intrinsics.checkNotNullParameter(type, "type");
                this.this$0.log.invoke("开始发现车机 AirPlay 服务");
            }

            @Override // android.net.nsd.NsdManager.DiscoveryListener
            public void onDiscoveryStopped(String type) {
                Intrinsics.checkNotNullParameter(type, "type");
            }

            @Override // android.net.nsd.NsdManager.DiscoveryListener
            public void onStartDiscoveryFailed(String type, int code) {
                Intrinsics.checkNotNullParameter(type, "type");
                this.this$0.discovering = false;
                if (!this.this$0.closed.get()) {
                    this.this$0.result.completeExceptionally(new IOException("Bonjour 发现失败：" + code));
                }
            }

            @Override // android.net.nsd.NsdManager.DiscoveryListener
            public void onStopDiscoveryFailed(String type, int code) {
                Intrinsics.checkNotNullParameter(type, "type");
            }

            @Override // android.net.nsd.NsdManager.DiscoveryListener
            public void onServiceLost(NsdServiceInfo info) {
                Intrinsics.checkNotNullParameter(info, "info");
            }

            @Override // android.net.nsd.NsdManager.DiscoveryListener
            public void onServiceFound(final NsdServiceInfo info) {
                Object objM159constructorimpl;
                Intrinsics.checkNotNullParameter(info, "info");
                if (this.this$0.closed.get() || this.this$0.resolving.size() >= 8 || !this.this$0.resolving.add(info.getServiceName())) {
                    return;
                }
                NsdServiceInfo request = new NsdServiceInfo();
                request.setServiceName(info.getServiceName());
                request.setServiceType(info.getServiceType());
                if (Build.VERSION.SDK_INT >= 33) {
                    request.setNetwork(request.getNetwork());
                }
                final AirPlayDiscovery airPlayDiscovery = this.this$0;
                try {
                    Result.Companion companion = Result.INSTANCE;
                    AirPlayDiscovery$listener$1 airPlayDiscovery$listener$1 = this;
                    airPlayDiscovery.manager.resolveService(request, new NsdManager.ResolveListener() { // from class: com.carlink.cast.AirPlayDiscovery$listener$1$onServiceFound$1$1
                        @Override // android.net.nsd.NsdManager.ResolveListener
                        public void onResolveFailed(NsdServiceInfo service, int code) {
                            Intrinsics.checkNotNullParameter(service, "service");
                            airPlayDiscovery.resolving.remove(info.getServiceName());
                        }

                        @Override // android.net.nsd.NsdManager.ResolveListener
                        public void onServiceResolved(NsdServiceInfo service) {
                            List<InetAddress> listListOfNotNull;
                            Intrinsics.checkNotNullParameter(service, "service");
                            airPlayDiscovery.resolving.remove(info.getServiceName());
                            if (airPlayDiscovery.closed.get()) {
                                return;
                            }
                            if (Build.VERSION.SDK_INT < 33 || Intrinsics.areEqual(service.getNetwork(), airPlayDiscovery.network)) {
                                if (Build.VERSION.SDK_INT >= 34) {
                                    listListOfNotNull = service.getHostAddresses();
                                    Intrinsics.checkNotNullExpressionValue(listListOfNotNull, "getHostAddresses(...)");
                                } else {
                                    listListOfNotNull = CollectionsKt.listOfNotNull(service.getHost());
                                }
                                AirPlayDiscovery airPlayDiscovery2 = airPlayDiscovery;
                                int port = service.getPort();
                                Map<String, byte[]> attributes = service.getAttributes();
                                Intrinsics.checkNotNullExpressionValue(attributes, "getAttributes(...)");
                                airPlayDiscovery2.deliver(listListOfNotNull, port, attributes);
                            }
                        }
                    });
                    objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
                } catch (Throwable th) {
                    Result.Companion companion2 = Result.INSTANCE;
                    objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
                }
                AirPlayDiscovery airPlayDiscovery2 = this.this$0;
                if (Result.m162exceptionOrNullimpl(objM159constructorimpl) != null) {
                    airPlayDiscovery2.resolving.remove(info.getServiceName());
                }
            }
        };
    }

    /* JADX INFO: compiled from: Discovery.kt */
    @Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B'\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b¢\u0006\u0004\b\t\u0010\nJ\u000f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0006HÆ\u0003J\u000b\u0010\u0013\u001a\u0004\u0018\u00010\bHÆ\u0003J/\u0010\u0014\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\bHÆ\u0001J\u0013\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0018\u001a\u00020\u0006HÖ\u0001J\t\u0010\u0019\u001a\u00020\bHÖ\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0013\u0010\u0007\u001a\u0004\u0018\u00010\b¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u001a"}, d2 = {"Lcom/carlink/cast/AirPlayDiscovery$Found;", "", "addresses", "", "Ljava/net/InetAddress;", "port", "", "publicKey", "", "<init>", "(Ljava/util/List;ILjava/lang/String;)V", "getAddresses", "()Ljava/util/List;", "getPort", "()I", "getPublicKey", "()Ljava/lang/String;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Found {
        private final List<InetAddress> addresses;
        private final int port;
        private final String publicKey;

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Found copy$default(Found found, List list, int i, String str, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                list = found.addresses;
            }
            if ((i2 & 2) != 0) {
                i = found.port;
            }
            if ((i2 & 4) != 0) {
                str = found.publicKey;
            }
            return found.copy(list, i, str);
        }

        public final List<InetAddress> component1() {
            return this.addresses;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final int getPort() {
            return this.port;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final String getPublicKey() {
            return this.publicKey;
        }

        public final Found copy(List<? extends InetAddress> addresses, int port, String publicKey) {
            Intrinsics.checkNotNullParameter(addresses, "addresses");
            return new Found(addresses, port, publicKey);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Found)) {
                return false;
            }
            Found found = (Found) other;
            return Intrinsics.areEqual(this.addresses, found.addresses) && this.port == found.port && Intrinsics.areEqual(this.publicKey, found.publicKey);
        }

        public int hashCode() {
            return (((this.addresses.hashCode() * 31) + Integer.hashCode(this.port)) * 31) + (this.publicKey == null ? 0 : this.publicKey.hashCode());
        }

        public String toString() {
            return "Found(addresses=" + this.addresses + ", port=" + this.port + ", publicKey=" + this.publicKey + ")";
        }

        /* JADX WARN: Multi-variable type inference failed */
        public Found(List<? extends InetAddress> addresses, int port, String publicKey) {
            Intrinsics.checkNotNullParameter(addresses, "addresses");
            this.addresses = addresses;
            this.port = port;
            this.publicKey = publicKey;
        }

        public final List<InetAddress> getAddresses() {
            return this.addresses;
        }

        public final int getPort() {
            return this.port;
        }

        public final String getPublicKey() {
            return this.publicKey;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void deliver(List<? extends InetAddress> addresses, int port, Map<String, byte[]> attributes) {
        boolean z = false;
        if (1 <= port && port < 65536) {
            z = true;
        }
        if (!z || addresses.isEmpty()) {
            return;
        }
        byte[] bArr = attributes.get("pk");
        String key = bArr != null ? new String(bArr, Charsets.UTF_8) : null;
        if (this.result.complete(new Found(addresses, port, key))) {
            this.log.invoke("已发现车机 AirPlay：" + ((InetAddress) CollectionsKt.first((List) addresses)).getHostAddress() + ":" + port);
        }
    }

    public final void start() throws IOException {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        Object objM159constructorimpl3;
        if (Build.VERSION.SDK_INT >= 33) {
            NsdServiceInfo nsdServiceInfo = new NsdServiceInfo();
            nsdServiceInfo.setServiceName("CarLink Cast");
            nsdServiceInfo.setServiceType("_carplay-ctrl._tcp");
            nsdServiceInfo.setPort(this.controlPort);
            nsdServiceInfo.setNetwork(nsdServiceInfo.getNetwork());
            nsdServiceInfo.setAttribute("id", this.transportId);
            nsdServiceInfo.setAttribute("srcvers", "320.17");
            this.registered = true;
            try {
                Result.Companion companion = Result.INSTANCE;
                AirPlayDiscovery airPlayDiscovery = this;
                airPlayDiscovery.manager.registerService(nsdServiceInfo, 1, airPlayDiscovery.registration);
                objM159constructorimpl2 = Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl2);
            if (thM162exceptionOrNullimpl != null) {
                this.registered = false;
                this.log.invoke("控制服务发布异常：" + thM162exceptionOrNullimpl.getMessage());
            }
            this.discovering = true;
            try {
                Result.Companion companion3 = Result.INSTANCE;
                AirPlayDiscovery airPlayDiscovery2 = this;
                airPlayDiscovery2.manager.discoverServices("_airplay._tcp", 1, airPlayDiscovery2.network, airPlayDiscovery2.executor, airPlayDiscovery2.listener);
                objM159constructorimpl3 = Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th2) {
                Result.Companion companion4 = Result.INSTANCE;
                objM159constructorimpl3 = Result.m159constructorimpl(ResultKt.createFailure(th2));
            }
            Throwable thM162exceptionOrNullimpl2 = Result.m162exceptionOrNullimpl(objM159constructorimpl3);
            if (thM162exceptionOrNullimpl2 != null) {
                this.discovering = false;
                this.log.invoke("Bonjour 发现异常：" + thM162exceptionOrNullimpl2.getMessage());
            }
            Result.m158boximpl(objM159constructorimpl3);
            return;
        }
        final JmDNS dns = JmDNS.create(this.local, "CarLink Cast");
        this.legacy = dns;
        try {
            Result.Companion companion5 = Result.INSTANCE;
            AirPlayDiscovery airPlayDiscovery3 = this;
            dns.registerService(ServiceInfo.create("_carplay-ctrl._tcp.local.", "CarLink Cast", airPlayDiscovery3.controlPort, 0, 0, (Map<String, ?>) MapsKt.mapOf(TuplesKt.to("id", airPlayDiscovery3.transportId), TuplesKt.to("srcvers", "320.17"))));
            objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th3) {
            Result.Companion companion6 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th3));
        }
        Throwable thM162exceptionOrNullimpl3 = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (thM162exceptionOrNullimpl3 != null) {
            this.log.invoke("控制服务发布异常：" + thM162exceptionOrNullimpl3.getMessage());
        }
        dns.addServiceListener("_airplay._tcp.local.", new ServiceListener() { // from class: com.carlink.cast.AirPlayDiscovery.start.7
            @Override // javax.jmdns.ServiceListener
            public void serviceAdded(ServiceEvent event) {
                Intrinsics.checkNotNullParameter(event, "event");
                if (!AirPlayDiscovery.this.closed.get()) {
                    dns.requestServiceInfo(event.getType(), event.getName(), true);
                }
            }

            @Override // javax.jmdns.ServiceListener
            public void serviceRemoved(ServiceEvent event) {
                Intrinsics.checkNotNullParameter(event, "event");
            }

            @Override // javax.jmdns.ServiceListener
            public void serviceResolved(ServiceEvent event) {
                Intrinsics.checkNotNullParameter(event, "event");
                if (AirPlayDiscovery.this.closed.get()) {
                    return;
                }
                ServiceInfo info = event.getInfo();
                AirPlayDiscovery airPlayDiscovery4 = AirPlayDiscovery.this;
                InetAddress[] inetAddresses = info.getInetAddresses();
                Intrinsics.checkNotNullExpressionValue(inetAddresses, "getInetAddresses(...)");
                List list = ArraysKt.toList(inetAddresses);
                int port = info.getPort();
                Enumeration<String> propertyNames = info.getPropertyNames();
                Intrinsics.checkNotNullExpressionValue(propertyNames, "getPropertyNames(...)");
                Iterable list2 = Collections.list(propertyNames);
                Intrinsics.checkNotNullExpressionValue(list2, "list(...)");
                Iterable iterable = (List) list2;
                LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(iterable, 10)), 16));
                for (Object obj : iterable) {
                    LinkedHashMap linkedHashMap2 = linkedHashMap;
                    byte[] propertyBytes = info.getPropertyBytes((String) obj);
                    linkedHashMap2.put(obj, propertyBytes == null ? new byte[0] : propertyBytes);
                }
                airPlayDiscovery4.deliver(list, port, linkedHashMap);
            }
        });
        this.log.invoke("Bonjour 使用车机网络本地地址（Android 10–12）");
    }

    public static /* synthetic */ Found await$default(AirPlayDiscovery airPlayDiscovery, long j, int i, Object obj) {
        if ((i & 1) != 0) {
            j = 20000;
        }
        return airPlayDiscovery.await(j);
    }

    public final Found await(long timeoutMillis) {
        try {
            return this.result.get(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return null;
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        Unit unit;
        if (this.closed.compareAndSet(false, true)) {
            if (Build.VERSION.SDK_INT >= 33) {
                if (this.discovering) {
                    try {
                        Result.Companion companion = Result.INSTANCE;
                        AirPlayDiscovery airPlayDiscovery = this;
                        airPlayDiscovery.manager.stopServiceDiscovery(airPlayDiscovery.listener);
                        Result.m159constructorimpl(Unit.INSTANCE);
                    } catch (Throwable th) {
                        Result.Companion companion2 = Result.INSTANCE;
                        Result.m159constructorimpl(ResultKt.createFailure(th));
                    }
                }
                if (this.registered) {
                    try {
                        Result.Companion companion3 = Result.INSTANCE;
                        AirPlayDiscovery airPlayDiscovery2 = this;
                        airPlayDiscovery2.manager.unregisterService(airPlayDiscovery2.registration);
                        Result.m159constructorimpl(Unit.INSTANCE);
                    } catch (Throwable th2) {
                        Result.Companion companion4 = Result.INSTANCE;
                        Result.m159constructorimpl(ResultKt.createFailure(th2));
                    }
                }
            }
            try {
                Result.Companion companion5 = Result.INSTANCE;
                JmDNS jmDNS = this.legacy;
                if (jmDNS != null) {
                    jmDNS.close();
                    unit = Unit.INSTANCE;
                } else {
                    unit = null;
                }
                Result.m159constructorimpl(unit);
            } catch (Throwable th3) {
                Result.Companion companion6 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th3));
            }
            this.legacy = null;
        }
    }
}
