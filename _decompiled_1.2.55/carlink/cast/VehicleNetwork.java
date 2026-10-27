package com.carlink.cast;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import com.carplay.airplay.AirPlayIapTunnelStream;
import com.carplay.iap2.controller.WirelessEndpoint;
import com.carplay.sender.PeerAddress;
import com.carplay.sender.PeerAddresses;
import java.io.Closeable;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: VehicleNetwork.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000\u0090\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0005\b\u0000\u0018\u00002\u00020\u0001B+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007¢\u0006\u0004\b\n\u0010\u000bJ\u0006\u0010%\u001a\u00020$J\u0006\u0010&\u001a\u00020'J\u001a\u0010(\u001a\b\u0012\u0004\u0012\u00020*0)2\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\b0)J\u000e\u0010,\u001a\u00020\t2\u0006\u0010-\u001a\u00020.J\u000e\u0010/\u001a\u00020\t2\u0006\u0010-\u001a\u000200J\u0006\u00101\u001a\u000202J\u000e\u00103\u001a\u00020$2\u0006\u0010\u001c\u001a\u00020\u0014J\u0010\u00104\u001a\u00020\u00142\b\b\u0002\u00105\u001a\u000206J\u0006\u00107\u001a\u00020\tJ\u0010\u00108\u001a\u0002022\u0006\u00109\u001a\u00020\u0018H\u0002J\b\u0010:\u001a\u00020\tH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\n \u000e*\u0004\u0018\u00010\r0\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013X\u0082\u0004¢\u0006\u0002\n\u0000R\"\u0010\u0015\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u0018\u0018\u00010\u00170\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001aX\u0082\u000e¢\u0006\u0002\n\u0000R\"\u0010\u001d\u001a\u0004\u0018\u00010\u00142\b\u0010\u001c\u001a\u0004\u0018\u00010\u0014@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\"\u0010 \u001a\u0004\u0018\u00010\u00182\b\u0010\u001c\u001a\u0004\u0018\u00010\u0018@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u000e\u0010#\u001a\u00020$X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006;"}, d2 = {"Lcom/carlink/cast/VehicleNetwork;", "Ljava/io/Closeable;", "context", "Landroid/content/Context;", "endpoint", "Lcom/carplay/iap2/controller/WirelessEndpoint;", "log", "Lkotlin/Function1;", "", "", "<init>", "(Landroid/content/Context;Lcom/carplay/iap2/controller/WirelessEndpoint;Lkotlin/jvm/functions/Function1;)V", "connectivity", "Landroid/net/ConnectivityManager;", "kotlin.jvm.PlatformType", "selected", "Ljava/util/concurrent/atomic/AtomicBoolean;", "ended", "result", "Ljava/util/concurrent/CompletableFuture;", "Landroid/net/Network;", "lease", "Ljava/util/concurrent/atomic/AtomicReference;", "Lkotlin/Pair;", "Landroid/net/LinkProperties;", "callbacks", "", "Landroid/net/ConnectivityManager$NetworkCallback;", "value", "network", "getNetwork", "()Landroid/net/Network;", "link", "getLink", "()Landroid/net/LinkProperties;", "lost", "", "isAlive", "localAddress", "Ljava/net/InetAddress;", "resolveTargets", "", "Lcom/carplay/sender/PeerAddress;", "values", "bind", "socket", "Ljava/net/Socket;", "bindDatagram", "Ljava/net/DatagramSocket;", "interfaceIndex", "", "isCurrent", "join", "timeoutMillis", "", "observeDefaultNetwork", "interfaceIndexOf", "properties", "close", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class VehicleNetwork implements Closeable {
    private List<ConnectivityManager.NetworkCallback> callbacks;
    private final ConnectivityManager connectivity;
    private final Context context;
    private final AtomicBoolean ended;
    private final WirelessEndpoint endpoint;
    private final AtomicReference<Pair<Network, LinkProperties>> lease;
    private volatile LinkProperties link;
    private final Function1<String, Unit> log;
    private volatile boolean lost;
    private volatile Network network;
    private final CompletableFuture<Network> result;
    private final AtomicBoolean selected;

    /* JADX INFO: compiled from: VehicleNetwork.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[WifiAuth.values().length];
            try {
                iArr[WifiAuth.OPEN.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[WifiAuth.WPA2.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[WifiAuth.WPA3.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public VehicleNetwork(Context context, WirelessEndpoint endpoint, Function1<? super String, Unit> log) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(endpoint, "endpoint");
        Intrinsics.checkNotNullParameter(log, "log");
        this.context = context;
        this.endpoint = endpoint;
        this.log = log;
        this.connectivity = (ConnectivityManager) this.context.getSystemService(ConnectivityManager.class);
        this.selected = new AtomicBoolean();
        this.ended = new AtomicBoolean();
        this.result = new CompletableFuture<>();
        this.lease = new AtomicReference<>();
        this.callbacks = new ArrayList();
    }

    public final Network getNetwork() {
        return this.network;
    }

    public final LinkProperties getLink() {
        return this.link;
    }

    public final boolean isAlive() {
        return (this.ended.get() || this.lost) ? false : true;
    }

    public final InetAddress localAddress() {
        List addresses;
        Iterable linkAddresses;
        LinkProperties linkProperties = this.link;
        Object obj = null;
        if (linkProperties == null || (linkAddresses = linkProperties.getLinkAddresses()) == null) {
            addresses = null;
        } else {
            Iterable iterable = linkAddresses;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            Iterator it = iterable.iterator();
            while (it.hasNext()) {
                arrayList.add(((LinkAddress) it.next()).getAddress());
            }
            Collection arrayList2 = new ArrayList();
            for (Object obj2 : (List) arrayList) {
                InetAddress inetAddress = (InetAddress) obj2;
                if ((inetAddress.isLoopbackAddress() || inetAddress.isAnyLocalAddress()) ? false : true) {
                    arrayList2.add(obj2);
                }
            }
            addresses = (List) arrayList2;
        }
        if (addresses == null) {
            addresses = CollectionsKt.emptyList();
        }
        for (Object obj3 : addresses) {
            if (((InetAddress) obj3) instanceof Inet4Address) {
                obj = obj3;
                break;
            }
        }
        InetAddress inetAddress2 = (InetAddress) obj;
        if (inetAddress2 == null && (inetAddress2 = (InetAddress) CollectionsKt.firstOrNull(addresses)) == null) {
            throw new IllegalStateException("车机网络尚未分配有效地址".toString());
        }
        return inetAddress2;
    }

    public final List<PeerAddress> resolveTargets(List<String> values) {
        List<? extends InetAddress> listEmptyList;
        Iterable linkAddresses;
        Intrinsics.checkNotNullParameter(values, "values");
        PeerAddresses peerAddresses = PeerAddresses.INSTANCE;
        LinkProperties linkProperties = this.link;
        if (linkProperties == null || (linkAddresses = linkProperties.getLinkAddresses()) == null) {
            listEmptyList = null;
        } else {
            Iterable iterable = linkAddresses;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            Iterator it = iterable.iterator();
            while (it.hasNext()) {
                arrayList.add(((LinkAddress) it.next()).getAddress());
            }
            listEmptyList = (List) arrayList;
        }
        if (listEmptyList == null) {
            listEmptyList = CollectionsKt.emptyList();
        }
        return peerAddresses.resolve(values, listEmptyList, interfaceIndex());
    }

    public final void bind(Socket socket) {
        Intrinsics.checkNotNullParameter(socket, "socket");
        Network current = this.network;
        if (current == null) {
            throw new IllegalStateException("车机网络尚未就绪".toString());
        }
        current.bindSocket(socket);
    }

    public final void bindDatagram(DatagramSocket socket) {
        Intrinsics.checkNotNullParameter(socket, "socket");
        Network current = this.network;
        if (current == null) {
            throw new IllegalStateException("车机网络尚未就绪".toString());
        }
        current.bindSocket(socket);
    }

    public final int interfaceIndex() throws SocketException {
        String interfaceName;
        LinkProperties linkProperties = this.link;
        if (linkProperties != null && (interfaceName = linkProperties.getInterfaceName()) != null) {
            NetworkInterface byName = NetworkInterface.getByName(interfaceName);
            Integer numValueOf = byName != null ? Integer.valueOf(byName.getIndex()) : null;
            if (numValueOf != null) {
                return numValueOf.intValue();
            }
        }
        return 0;
    }

    public final boolean isCurrent(Network value) {
        Intrinsics.checkNotNullParameter(value, "value");
        return Intrinsics.areEqual(this.network, value);
    }

    public static /* synthetic */ Network join$default(VehicleNetwork vehicleNetwork, long j, int i, Object obj) {
        if ((i & 1) != 0) {
            j = 45000;
        }
        return vehicleNetwork.join(j);
    }

    public final Network join(long timeoutMillis) throws Exception {
        WifiAuth authentication = WifiAuthPolicy.INSTANCE.select(this.endpoint.getSecurity(), ((WifiManager) this.context.getSystemService(WifiManager.class)).isWpa3SaeSupported());
        WifiNetworkSpecifier.Builder ssid = new WifiNetworkSpecifier.Builder().setSsid(this.endpoint.getSsid());
        switch (WhenMappings.$EnumSwitchMapping$0[authentication.ordinal()]) {
            case 1:
                Unit unit = Unit.INSTANCE;
                break;
            case 2:
                ssid.setWpa2Passphrase(this.endpoint.getPassword());
                break;
            case 3:
                ssid.setWpa3Passphrase(this.endpoint.getPassword());
                break;
            default:
                throw new NoWhenBranchMatchedException();
        }
        WifiNetworkSpecifier spec = ssid.build();
        Intrinsics.checkNotNullExpressionValue(spec, "build(...)");
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.carlink.cast.VehicleNetwork$join$callback$1
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(Network network) {
                Intrinsics.checkNotNullParameter(network, "network");
                if (this.this$0.ended.get()) {
                    return;
                }
                this.this$0.log.invoke("车机热点已连接：network=" + network.getNetworkHandle());
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLinkPropertiesChanged(Network network, LinkProperties properties) {
                boolean ready;
                boolean z;
                Intrinsics.checkNotNullParameter(network, "network");
                Intrinsics.checkNotNullParameter(properties, "properties");
                if (this.this$0.ended.get()) {
                    return;
                }
                this.this$0.lease.set(TuplesKt.to(network, properties));
                this.this$0.link = properties;
                Iterable linkAddresses = properties.getLinkAddresses();
                Intrinsics.checkNotNullExpressionValue(linkAddresses, "getLinkAddresses(...)");
                Iterable iterable = linkAddresses;
                Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
                Iterator it = iterable.iterator();
                while (it.hasNext()) {
                    arrayList.add(((LinkAddress) it.next()).getAddress());
                }
                List addresses = (List) arrayList;
                boolean z2 = true;
                if (this.this$0.endpoint.getAddresses().isEmpty()) {
                    List list = addresses;
                    if (!(list instanceof Collection) || !list.isEmpty()) {
                        Iterator it2 = list.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                ready = false;
                                break;
                            }
                            InetAddress inetAddress = (InetAddress) it2.next();
                            if ((inetAddress.isLoopbackAddress() || inetAddress.isAnyLocalAddress()) ? false : true) {
                                ready = true;
                                break;
                            }
                        }
                    } else {
                        ready = false;
                    }
                } else {
                    ready = !PeerAddresses.INSTANCE.resolve(this.this$0.endpoint.getAddresses(), addresses, this.this$0.interfaceIndexOf(properties)).isEmpty();
                }
                if (ready && this.this$0.selected.compareAndSet(false, true)) {
                    this.this$0.network = network;
                    Function1 function1 = this.this$0.log;
                    String interfaceName = properties.getInterfaceName();
                    List list2 = addresses;
                    if (!(list2 instanceof Collection) || !list2.isEmpty()) {
                        Iterator it3 = list2.iterator();
                        while (true) {
                            if (!it3.hasNext()) {
                                z = false;
                                break;
                            } else if (((InetAddress) it3.next()) instanceof Inet4Address) {
                                z = true;
                                break;
                            }
                        }
                    } else {
                        z = false;
                    }
                    List list3 = addresses;
                    if ((list3 instanceof Collection) && list3.isEmpty()) {
                        z2 = false;
                    } else {
                        Iterator it4 = list3.iterator();
                        while (it4.hasNext()) {
                            if (((InetAddress) it4.next()) instanceof Inet6Address) {
                            }
                        }
                        z2 = false;
                    }
                    function1.invoke("车机网络地址就绪：interface=" + interfaceName + "，IPv4=" + z + "，IPv6=" + z2);
                    this.this$0.result.complete(network);
                }
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onUnavailable() {
                if (!this.this$0.ended.get()) {
                    this.this$0.result.completeExceptionally(new IllegalStateException("车机热点连接被拒绝或超时"));
                }
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                Intrinsics.checkNotNullParameter(network, "network");
                if (!this.this$0.ended.get() && Intrinsics.areEqual(this.this$0.getNetwork(), network)) {
                    this.this$0.lost = true;
                    this.this$0.log.invoke("车机网络已断开：network=" + network.getNetworkHandle());
                    this.this$0.result.completeExceptionally(new IllegalStateException("车机无线网络断开"));
                }
            }
        };
        this.callbacks.add(networkCallback);
        NetworkRequest request = new NetworkRequest.Builder().addTransportType(1).removeCapability(12).setNetworkSpecifier(spec).build();
        this.connectivity.requestNetwork(request, networkCallback, (int) timeoutMillis);
        this.log.invoke("正在加入车机热点 " + this.endpoint.getSsid() + "（" + authentication + "），等待车机声明的地址");
        try {
            Network network = this.result.get(AirPlayIapTunnelStream.EVENT_READY_TIMEOUT_MILLIS + timeoutMillis, TimeUnit.MILLISECONDS);
            Intrinsics.checkNotNull(network);
            return network;
        } catch (Exception failure) {
            close();
            if (failure instanceof ExecutionException) {
                Throwable cause = failure.getCause();
                Exception exc = cause instanceof Exception ? (Exception) cause : null;
                if (exc == null) {
                    exc = failure;
                }
                throw exc;
            }
            throw failure;
        }
    }

    public final void observeDefaultNetwork() {
        Object objM159constructorimpl;
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.carlink.cast.VehicleNetwork$observeDefaultNetwork$callback$1
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(Network network) {
                Intrinsics.checkNotNullParameter(network, "network");
                if (!this.this$0.ended.get()) {
                    this.this$0.log.invoke("默认网络可用：network=" + network.getNetworkHandle() + "；车机连接不受影响");
                }
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                Intrinsics.checkNotNullParameter(network, "network");
                if (!this.this$0.ended.get()) {
                    this.this$0.log.invoke("默认网络变化：旧 network=" + network.getNetworkHandle() + "；车机连接保持");
                }
            }
        };
        try {
            Result.Companion companion = Result.INSTANCE;
            this.connectivity.registerDefaultNetworkCallback(networkCallback);
            objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m166isSuccessimpl(objM159constructorimpl)) {
            this.callbacks.add(networkCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int interfaceIndexOf(LinkProperties properties) throws SocketException {
        String interfaceName = properties.getInterfaceName();
        if (interfaceName != null) {
            NetworkInterface byName = NetworkInterface.getByName(interfaceName);
            Integer numValueOf = byName != null ? Integer.valueOf(byName.getIndex()) : null;
            if (numValueOf != null) {
                return numValueOf.intValue();
            }
        }
        return 0;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.ended.compareAndSet(false, true)) {
            for (ConnectivityManager.NetworkCallback networkCallback : this.callbacks) {
                try {
                    Result.Companion companion = Result.INSTANCE;
                    this.connectivity.unregisterNetworkCallback(networkCallback);
                    Result.m159constructorimpl(Unit.INSTANCE);
                } catch (Throwable th) {
                    Result.Companion companion2 = Result.INSTANCE;
                    Result.m159constructorimpl(ResultKt.createFailure(th));
                }
            }
            this.callbacks.clear();
            this.network = null;
            this.link = null;
        }
    }
}
