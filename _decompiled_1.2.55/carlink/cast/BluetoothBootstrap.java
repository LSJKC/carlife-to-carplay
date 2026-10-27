package com.carlink.cast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.carplay.iap2.body.Iap2BodyBuilder;
import com.carplay.iap2.controller.BootstrapStatus;
import com.carplay.iap2.controller.PhoneBootstrap;
import com.carplay.iap2.controller.PhoneBootstrapProfile;
import com.carplay.iap2.controller.WirelessEndpoint;
import com.carplay.iap2.message.Iap2Messages;
import com.carplay.iap2.session.Iap2Session;
import com.carplay.iap2.transport.BlockingDuplexByteStream;
import com.carplay.iap2.transport.BluetoothRfcommDuplexStream;
import com.carplay.iap2.transport.Iap2HandshakeStatus;
import com.carplay.iap2.transport.Iap2PeerDetectionRestart;
import com.carplay.iap2.transport.PhoneBluetoothProfile;
import com.carplay.iap2.transport.PhoneBluetoothSessionTransport;
import com.carplay.iap2.transport.RegisteredRfcommService;
import com.carplay.iap2.transport.RfcommConnectAttempt;
import com.carplay.iap2.transport.RfcommConnectionDirection;
import com.carplay.iap2.transport.RfcommConnectionStrategy;
import com.carplay.iap2.wire.Iap2Frame;
import com.carplay.sender.AccessoryAuthentication;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.AdaptedFunctionReference;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.RangesKt;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: BluetoothBootstrap.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000Ò\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\b\u0001\u0018\u0000 ]2\u00060\u0001j\u0002`\u0002:\u0004Z[\\]Bf\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0006\u0012!\u0010\t\u001a\u001d\u0012\u0013\u0012\u00110\u000b¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u00020\u000f0\n\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00130\n¢\u0006\u0004\b\u0014\u0010\u0015J\u0006\u00109\u001a\u00020\u000bJ\b\u0010:\u001a\u0004\u0018\u000106J\u0010\u0010;\u001a\u00020<2\b\b\u0002\u0010=\u001a\u00020\u001fJ\u0018\u0010>\u001a\u00020?2\u0006\u0010@\u001a\u00020-2\u0006\u0010A\u001a\u000206H\u0002J\u001a\u0010B\u001a\u00020\u00132\u0006\u0010C\u001a\u00020%2\b\u0010D\u001a\u0004\u0018\u000106H\u0002J\b\u0010E\u001a\u00020\u0013H\u0002J\"\u0010F\u001a\u00020'2\u0006\u0010C\u001a\u00020%2\u0006\u0010G\u001a\u00020\u000f2\b\u0010D\u001a\u0004\u0018\u000106H\u0002J\u0012\u0010H\u001a\u0004\u0018\u00010I2\u0006\u0010J\u001a\u00020KH\u0002J\u0018\u0010L\u001a\u00020\u00132\u0006\u0010C\u001a\u00020%2\u0006\u0010M\u001a\u00020'H\u0002J\b\u0010N\u001a\u00020OH\u0002J \u0010P\u001a\u00020Q2\u0006\u0010R\u001a\u00020)2\u0006\u0010S\u001a\u00020#2\u0006\u0010T\u001a\u00020\u000bH\u0002J\b\u0010U\u001a\u00020)H\u0002J\b\u0010V\u001a\u000201H\u0002J\u0012\u0010W\u001a\u00020)2\b\b\u0002\u0010=\u001a\u00020\u001fH\u0002J\u0006\u0010X\u001a\u00020\u000bJ\b\u0010Y\u001a\u00020\u0013H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R)\u0010\t\u001a\u001d\u0012\u0013\u0012\u00110\u000b¢\u0006\f\b\f\u0012\b\b\r\u0012\u0004\b\b(\u000e\u0012\u0004\u0012\u00020\u000f0\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00130\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0018\u0010 \u001a\f\u0012\b\u0012\u00060\u0001j\u0002`\u00020!X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010$\u001a\u0004\u0018\u00010%X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010&\u001a\u0004\u0018\u00010'X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010(\u001a\u0004\u0018\u00010)X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010*\u001a\u0004\u0018\u00010+X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010,\u001a\u0004\u0018\u00010-X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010.\u001a\u0004\u0018\u00010\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010/\u001a\n\u0012\u0004\u0012\u000201\u0018\u000100X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u00102\u001a\b\u0012\u0004\u0012\u00020403X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u00105\u001a\b\u0012\u0004\u0012\u00020603X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u00108\u001a\u0004\u0018\u000106X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006^"}, d2 = {"Lcom/carlink/cast/BluetoothBootstrap;", "Ljava/lang/AutoCloseable;", "Lkotlin/AutoCloseable;", "context", "Landroid/content/Context;", "peerAddress", "", "identityId", "transportIdentifier", "trust", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "authenticatedBluetooth", "Lcom/carplay/sender/AccessoryAuthentication;", "hid", "Lcom/carlink/cast/Iap2HidDispatcher;", "log", "", "<init>", "(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lcom/carlink/cast/Iap2HidDispatcher;Lkotlin/jvm/functions/Function1;)V", "bluetooth", "Landroid/bluetooth/BluetoothAdapter;", CastSettings.KEY_PEER, "Landroid/bluetooth/BluetoothDevice;", "closed", "Ljava/util/concurrent/atomic/AtomicBoolean;", "nowPlaying", "Lcom/carlink/cast/NowPlayingPublisher;", "nowPlayingSubscription", "", "owned", "", "profile", "Lcom/carplay/iap2/transport/PhoneBluetoothProfile;", "session", "Lcom/carplay/iap2/session/Iap2Session;", "bootstrap", "Lcom/carplay/iap2/controller/PhoneBootstrap;", "socket", "Landroid/bluetooth/BluetoothSocket;", "receiver", "Ljava/lang/Thread;", "linkTransport", "Lcom/carplay/iap2/transport/PhoneBluetoothSessionTransport;", "authentication", "phoneService", "Lcom/carplay/iap2/transport/RegisteredRfcommService;", "Landroid/bluetooth/BluetoothServerSocket;", "endpoint", "Ljava/util/concurrent/CompletableFuture;", "Lcom/carplay/iap2/controller/WirelessEndpoint;", "restart", "", "restartUsed", "verifiedIdentity", "authenticated", "identityFingerprint", "connect", "Lcom/carlink/cast/BluetoothBootstrap$Result;", "timeoutMillis", "openRestartStream", "Lcom/carplay/iap2/transport/BlockingDuplexByteStream;", NotificationCompat.CATEGORY_TRANSPORT, "initial", "startEpoch", "current", "expected", "requirePermissions", "newBootstrap", "access", "subscriptionReply", "Lcom/carplay/iap2/wire/Iap2Frame;", "messageId", "", "startReceiver", "boot", "negotiate", "Lcom/carlink/cast/BluetoothBootstrap$Link;", "synchronize", "Lcom/carlink/cast/BluetoothBootstrap$HandshakeAttempt;", "connected", "candidate", "outbound", "connectOutbound", "registerPhoneService", "listenInbound", "isAlive", "close", "Result", "Link", "HandshakeAttempt", "Companion", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class BluetoothBootstrap implements AutoCloseable {
    private static final UUID ACCESSORY_UUID;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final long INBOUND_CONNECT_TIMEOUT_MILLIS = 45000;
    private static final UUID PHONE_UUID;
    private static final long SILENT_INBOUND_RECOVERY_TIMEOUT_MILLIS = 15000;
    private volatile AccessoryAuthentication authentication;
    private final BluetoothAdapter bluetooth;
    private volatile PhoneBootstrap bootstrap;
    private final AtomicBoolean closed;
    private final Context context;
    private final CompletableFuture<WirelessEndpoint> endpoint;
    private final Iap2HidDispatcher hid;
    private final String identityId;
    private volatile PhoneBluetoothSessionTransport linkTransport;
    private final Function1<String, Unit> log;
    private final NowPlayingPublisher nowPlaying;
    private volatile long nowPlayingSubscription;
    private final List<AutoCloseable> owned;
    private final BluetoothDevice peer;
    private final String peerAddress;
    private RegisteredRfcommService<BluetoothServerSocket> phoneService;
    private final PhoneBluetoothProfile profile;
    private volatile Thread receiver;
    private volatile CompletableFuture<byte[]> restart;
    private volatile boolean restartUsed;
    private volatile Iap2Session session;
    private volatile BluetoothSocket socket;
    private final String transportIdentifier;
    private final Function1<Boolean, AccessoryAuthentication> trust;
    private volatile byte[] verifiedIdentity;

    /* JADX WARN: Multi-variable type inference failed */
    public BluetoothBootstrap(Context context, String peerAddress, String identityId, String transportIdentifier, Function1<? super Boolean, ? extends AccessoryAuthentication> trust, Iap2HidDispatcher hid, Function1<? super String, Unit> log) {
        BluetoothAdapter adapter;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(peerAddress, "peerAddress");
        Intrinsics.checkNotNullParameter(identityId, "identityId");
        Intrinsics.checkNotNullParameter(transportIdentifier, "transportIdentifier");
        Intrinsics.checkNotNullParameter(trust, "trust");
        Intrinsics.checkNotNullParameter(hid, "hid");
        Intrinsics.checkNotNullParameter(log, "log");
        this.context = context;
        this.peerAddress = peerAddress;
        this.identityId = identityId;
        this.transportIdentifier = transportIdentifier;
        this.trust = trust;
        this.hid = hid;
        this.log = log;
        BluetoothManager bluetoothManager = (BluetoothManager) this.context.getSystemService(BluetoothManager.class);
        if (bluetoothManager == null || (adapter = bluetoothManager.getAdapter()) == null) {
            throw new IllegalStateException("手机不支持蓝牙".toString());
        }
        this.bluetooth = adapter;
        BluetoothDevice remoteDevice = this.bluetooth.getRemoteDevice(this.peerAddress);
        Intrinsics.checkNotNullExpressionValue(remoteDevice, "getRemoteDevice(...)");
        this.peer = remoteDevice;
        this.closed = new AtomicBoolean();
        this.nowPlaying = new NowPlayingPublisher(this.context, this.log);
        this.owned = new ArrayList();
        this.profile = PhoneBluetoothProfile.ACCESSORY_FIRST;
        this.endpoint = new CompletableFuture<>();
        this.restart = new CompletableFuture<>();
    }

    /* JADX INFO: compiled from: BluetoothBootstrap.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001J\t\u0010\u0014\u001a\u00020\u0015HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0016"}, d2 = {"Lcom/carlink/cast/BluetoothBootstrap$Result;", "", "endpoint", "Lcom/carplay/iap2/controller/WirelessEndpoint;", "accessoryIdentity", "", "<init>", "(Lcom/carplay/iap2/controller/WirelessEndpoint;[B)V", "getEndpoint", "()Lcom/carplay/iap2/controller/WirelessEndpoint;", "getAccessoryIdentity", "()[B", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Result {
        private final byte[] accessoryIdentity;
        private final WirelessEndpoint endpoint;

        public static /* synthetic */ Result copy$default(Result result, WirelessEndpoint wirelessEndpoint, byte[] bArr, int i, Object obj) {
            if ((i & 1) != 0) {
                wirelessEndpoint = result.endpoint;
            }
            if ((i & 2) != 0) {
                bArr = result.accessoryIdentity;
            }
            return result.copy(wirelessEndpoint, bArr);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final WirelessEndpoint getEndpoint() {
            return this.endpoint;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final byte[] getAccessoryIdentity() {
            return this.accessoryIdentity;
        }

        public final Result copy(WirelessEndpoint endpoint, byte[] accessoryIdentity) {
            Intrinsics.checkNotNullParameter(endpoint, "endpoint");
            Intrinsics.checkNotNullParameter(accessoryIdentity, "accessoryIdentity");
            return new Result(endpoint, accessoryIdentity);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Result)) {
                return false;
            }
            Result result = (Result) other;
            return Intrinsics.areEqual(this.endpoint, result.endpoint) && Intrinsics.areEqual(this.accessoryIdentity, result.accessoryIdentity);
        }

        public int hashCode() {
            return (this.endpoint.hashCode() * 31) + Arrays.hashCode(this.accessoryIdentity);
        }

        public String toString() {
            return "Result(endpoint=" + this.endpoint + ", accessoryIdentity=" + Arrays.toString(this.accessoryIdentity) + ")";
        }

        public Result(WirelessEndpoint endpoint, byte[] accessoryIdentity) {
            Intrinsics.checkNotNullParameter(endpoint, "endpoint");
            Intrinsics.checkNotNullParameter(accessoryIdentity, "accessoryIdentity");
            this.endpoint = endpoint;
            this.accessoryIdentity = accessoryIdentity;
        }

        public final byte[] getAccessoryIdentity() {
            return this.accessoryIdentity;
        }

        public final WirelessEndpoint getEndpoint() {
            return this.endpoint;
        }
    }

    public final boolean authenticated() {
        BootstrapStatus bootstrapStatusStatus;
        PhoneBootstrap phoneBootstrap = this.bootstrap;
        if (((phoneBootstrap == null || (bootstrapStatusStatus = phoneBootstrap.status()) == null || !bootstrapStatusStatus.getAuthenticated()) ? false : true) && !this.closed.get()) {
            Iap2Session iap2Session = this.session;
            if ((iap2Session == null || iap2Session.isClosed()) ? false : true) {
                return true;
            }
        }
        return false;
    }

    public final byte[] identityFingerprint() {
        byte[] bArr = this.verifiedIdentity;
        if (bArr == null) {
            return null;
        }
        byte[] bArrCopyOf = Arrays.copyOf(bArr, bArr.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        return bArrCopyOf;
    }

    public static /* synthetic */ Result connect$default(BluetoothBootstrap bluetoothBootstrap, long j, int i, Object obj) {
        if ((i & 1) != 0) {
            j = INBOUND_CONNECT_TIMEOUT_MILLIS;
        }
        return bluetoothBootstrap.connect(j);
    }

    public final Result connect(long timeoutMillis) throws InterruptedException, IOException {
        byte[] fingerprint;
        Object objM159constructorimpl;
        Object obj;
        Object obj2;
        Object obj3;
        requirePermissions();
        if (!this.bluetooth.isEnabled()) {
            throw new IllegalStateException("请打开蓝牙".toString());
        }
        boolean z = false;
        boolean z2 = true;
        if (!(this.peer.getBondState() == 12)) {
            throw new IllegalStateException("请先在系统蓝牙设置中与车机完成配对".toString());
        }
        this.bluetooth.cancelDiscovery();
        Function1<String, Unit> function1 = this.log;
        String name = this.peer.getName();
        if (name == null) {
            name = this.peerAddress;
        }
        function1.invoke("蓝牙链路：连接 " + name);
        Link link = negotiate();
        this.socket = link.getSocket();
        this.session = link.getSession();
        this.linkTransport = link.getTransport();
        Function1<Boolean, AccessoryAuthentication> function2 = this.trust;
        BluetoothSocket bluetoothSocket = this.socket;
        if ((bluetoothSocket != null && bluetoothSocket.isConnected()) && this.peer.getBondState() == 12) {
            z = true;
        }
        this.authentication = function2.invoke(Boolean.valueOf(z));
        byte[] bArr = null;
        startEpoch(link.getSession(), null);
        long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMillis);
        while (!this.endpoint.isDone() && !this.closed.get() && System.nanoTime() < deadline) {
            long slice = Math.min(250L, RangesKt.coerceAtLeast((deadline - System.nanoTime()) / 1000000, 1L));
            try {
                kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                objM159constructorimpl = kotlin.Result.m159constructorimpl(this.restart.get(slice, TimeUnit.MILLISECONDS));
            } catch (Throwable th) {
                kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
                objM159constructorimpl = kotlin.Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            if (kotlin.Result.m165isFailureimpl(objM159constructorimpl)) {
                objM159constructorimpl = bArr;
            }
            byte[] restarted = (byte[]) objM159constructorimpl;
            if (restarted != null) {
                if (this.restartUsed) {
                    this.log.invoke("车机再次重新检测；本次连接只处理一次重启");
                    break;
                }
                this.restartUsed = z2;
                PhoneBootstrap phoneBootstrap = this.bootstrap;
                byte[] expected = phoneBootstrap != null ? phoneBootstrap.accessoryIdentityFingerprint() : bArr;
                this.log.invoke("车机接受了无线声明，正在同一 RFCOMM 上开始第二轮 iAP2");
                try {
                    kotlin.Result.Companion companion3 = kotlin.Result.INSTANCE;
                    Thread thread = this.receiver;
                    if (thread != null) {
                        thread.interrupt();
                        obj3 = Unit.INSTANCE;
                    } else {
                        obj3 = bArr;
                    }
                    kotlin.Result.m159constructorimpl(obj3);
                } catch (Throwable th2) {
                    kotlin.Result.Companion companion4 = kotlin.Result.INSTANCE;
                    kotlin.Result.m159constructorimpl(ResultKt.createFailure(th2));
                }
                try {
                    kotlin.Result.Companion companion5 = kotlin.Result.INSTANCE;
                    Iap2Session iap2Session = this.session;
                    if (iap2Session != null) {
                        iap2Session.close();
                        obj2 = Unit.INSTANCE;
                    } else {
                        obj2 = bArr;
                    }
                    kotlin.Result.m159constructorimpl(obj2);
                } catch (Throwable th3) {
                    kotlin.Result.Companion companion6 = kotlin.Result.INSTANCE;
                    kotlin.Result.m159constructorimpl(ResultKt.createFailure(th3));
                }
                try {
                    kotlin.Result.Companion companion7 = kotlin.Result.INSTANCE;
                    Thread thread2 = this.receiver;
                    if (thread2 != null) {
                        thread2.join(1000L);
                        obj = Unit.INSTANCE;
                    } else {
                        obj = bArr;
                    }
                    kotlin.Result.m159constructorimpl(obj);
                } catch (Throwable th4) {
                    kotlin.Result.Companion companion8 = kotlin.Result.INSTANCE;
                    kotlin.Result.m159constructorimpl(ResultKt.createFailure(th4));
                }
                BlockingDuplexByteStream stream = openRestartStream(link.getTransport(), restarted);
                Iap2Session next = Iap2Session.INSTANCE.phoneBluetooth(stream, this.profile, new Function1() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda6
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj4) {
                        return BluetoothBootstrap.connect$lambda$6((String) obj4);
                    }
                }, new Function1() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda7
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj4) {
                        return BluetoothBootstrap.connect$lambda$7(this.f$0, (byte[]) obj4);
                    }
                });
                this.owned.add(next);
                long remaining = RangesKt.coerceAtLeast((deadline - System.nanoTime()) / 1000000, 1L);
                if (!Iap2Session.awaitPhoneBluetoothReady$default(next, this.profile, Long.valueOf(remaining), null, 4, null)) {
                    throw new IllegalStateException("第二轮 iAP2 同步未完成".toString());
                }
                startEpoch(next, expected);
                bArr = null;
                z2 = true;
            }
        }
        if (!this.endpoint.isDone()) {
            throw new IllegalStateException("车机未下发无线配置".toString());
        }
        WirelessEndpoint endpoint = this.endpoint.getNow(null);
        if (endpoint == null) {
            throw new IllegalStateException("车机未下发无线配置".toString());
        }
        PhoneBootstrap phoneBootstrap2 = this.bootstrap;
        if (phoneBootstrap2 == null || (fingerprint = phoneBootstrap2.accessoryIdentityFingerprint()) == null) {
            throw new IllegalStateException("车机未提供识别信息".toString());
        }
        this.verifiedIdentity = fingerprint;
        return new Result(endpoint, fingerprint);
    }

    static final Unit connect$lambda$6(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    static final Unit connect$lambda$7(BluetoothBootstrap this$0, byte[] datagram) {
        Intrinsics.checkNotNullParameter(datagram, "datagram");
        this$0.nowPlaying.onFileTransferDatagram(datagram);
        return Unit.INSTANCE;
    }

    private final BlockingDuplexByteStream openRestartStream(PhoneBluetoothSessionTransport transport, byte[] initial) throws InterruptedException {
        Object last = null;
        for (int i = 0; i < 20; i++) {
            try {
                return transport.open(initial);
            } catch (Exception e) {
                last = e;
                Thread.sleep(50L);
            }
        }
        throw new IllegalStateException("旧 iAP2 实例未释放，无法开始第二轮", (Throwable) last);
    }

    private final void startEpoch(Iap2Session current, byte[] expected) {
        AccessoryAuthentication accessoryAuthentication = this.authentication;
        if (accessoryAuthentication == null) {
            throw new IllegalStateException("Required value was null.".toString());
        }
        PhoneBootstrap boot = newBootstrap(current, accessoryAuthentication, expected);
        this.bootstrap = boot;
        this.session = current;
        this.restart = new CompletableFuture<>();
        startReceiver(current, boot);
        boot.begin();
    }

    private final void requirePermissions() {
        if (Build.VERSION.SDK_INT < 31) {
            return;
        }
        if (!(this.context.checkSelfPermission("android.permission.BLUETOOTH_CONNECT") == 0)) {
            throw new IllegalStateException("蓝牙连接权限已撤销".toString());
        }
        if (!(this.context.checkSelfPermission("android.permission.BLUETOOTH_SCAN") == 0)) {
            throw new IllegalStateException("蓝牙发现权限已撤销".toString());
        }
    }

    private final PhoneBootstrap newBootstrap(final Iap2Session current, AccessoryAuthentication access, byte[] expected) {
        return new PhoneBootstrap(this.identityId, new C00271(access), new AnonymousClass2(access), new AnonymousClass3(current), this.log, new Function1() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda20
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BluetoothBootstrap.newBootstrap$lambda$13(this.f$0, (WirelessEndpoint) obj);
            }
        }, new Function1() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda21
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BluetoothBootstrap.newBootstrap$lambda$20(this.f$0, current, (Iap2Frame) obj);
            }
        }, new Function1() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda22
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BluetoothBootstrap.newBootstrap$lambda$21(this.f$0, (BootstrapStatus) obj);
            }
        }, PhoneBootstrapProfile.MODERN_WIRELESS, new Function0() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda23
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return this.f$0.transportIdentifier;
            }
        }, expected, null, 2048, null);
    }

    /* JADX INFO: renamed from: com.carlink.cast.BluetoothBootstrap$newBootstrap$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BluetoothBootstrap.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class C00271 extends FunctionReferenceImpl implements Function1<byte[], X509Certificate> {
        C00271(Object obj) {
            super(1, obj, AccessoryAuthentication.class, "validate", "validate([B)Ljava/security/cert/X509Certificate;", 0);
        }

        @Override // kotlin.jvm.functions.Function1
        public final X509Certificate invoke(byte[] p0) {
            Intrinsics.checkNotNullParameter(p0, "p0");
            return ((AccessoryAuthentication) this.receiver).validate(p0);
        }
    }

    /* JADX INFO: renamed from: com.carlink.cast.BluetoothBootstrap$newBootstrap$2, reason: invalid class name */
    /* JADX INFO: compiled from: BluetoothBootstrap.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass2 extends FunctionReferenceImpl implements Function3<X509Certificate, byte[], byte[], Boolean> {
        AnonymousClass2(Object obj) {
            super(3, obj, AccessoryAuthentication.class, "verifyAuthentication", "verifyAuthentication(Ljava/security/cert/X509Certificate;[B[B)Z", 0);
        }

        @Override // kotlin.jvm.functions.Function3
        public final Boolean invoke(X509Certificate p0, byte[] p1, byte[] p2) {
            Intrinsics.checkNotNullParameter(p0, "p0");
            Intrinsics.checkNotNullParameter(p1, "p1");
            Intrinsics.checkNotNullParameter(p2, "p2");
            return Boolean.valueOf(((AccessoryAuthentication) this.receiver).verifyAuthentication(p0, p1, p2));
        }
    }

    /* JADX INFO: renamed from: com.carlink.cast.BluetoothBootstrap$newBootstrap$3, reason: invalid class name */
    /* JADX INFO: compiled from: BluetoothBootstrap.kt */
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

    static final Unit newBootstrap$lambda$13(BluetoothBootstrap this$0, WirelessEndpoint it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.endpoint.complete(it);
        return Unit.INSTANCE;
    }

    static final Unit newBootstrap$lambda$20(BluetoothBootstrap this$0, Iap2Session $current, Iap2Frame frame) {
        Throwable th;
        Object objM159constructorimpl;
        Intrinsics.checkNotNullParameter(frame, "frame");
        if (SetsKt.setOf((Object[]) new Integer[]{26624, 26626, 26627}).contains(Integer.valueOf(frame.getMessageId()))) {
            byte[] body = frame.getPayload();
            Function1<String, Unit> function1 = this$0.log;
            String string = Integer.toString(frame.getMessageId(), CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            function1.invoke("车机按键消息 0x" + string + " 长度=" + body.length + " hex=" + ArraysKt.joinToString$default(body, (CharSequence) " ", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, new Function1() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda14
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return BluetoothBootstrap.newBootstrap$lambda$20$lambda$14(((Byte) obj).byteValue());
                }
            }, 30, (Object) null));
            this$0.hid.handle(frame, new BluetoothBootstrap$newBootstrap$5$2($current));
        } else {
            Function1<String, Unit> function2 = this$0.log;
            String string2 = Integer.toString(frame.getMessageId(), CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string2, "toString(...)");
            function2.invoke("车机消息 0x" + string2);
        }
        if (frame.getMessageId() == 20480) {
            this$0.nowPlayingSubscription = this$0.nowPlaying.subscribe(frame, new BluetoothBootstrap$newBootstrap$5$3($current), new BluetoothBootstrap$newBootstrap$5$4($current));
            this$0.log.invoke("蓝牙 iAP2 0x5000：已开始真实 NowPlaying 发布");
        } else if (frame.getMessageId() == 20482) {
            this$0.nowPlaying.unsubscribe(Long.valueOf(this$0.nowPlayingSubscription));
            this$0.log.invoke("蓝牙 iAP2 0x5002：已停止 NowPlaying 发布");
        }
        Iap2Frame iap2FrameSubscriptionReply = this$0.subscriptionReply(frame.getMessageId());
        if (iap2FrameSubscriptionReply != null) {
            try {
                kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                try {
                    Iap2Session.send$default($current, iap2FrameSubscriptionReply, 0L, 2, null);
                    objM159constructorimpl = kotlin.Result.m159constructorimpl(Unit.INSTANCE);
                } catch (Throwable th2) {
                    th = th2;
                    kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
                    objM159constructorimpl = kotlin.Result.m159constructorimpl(ResultKt.createFailure(th));
                }
            } catch (Throwable th3) {
                th = th3;
            }
            Throwable thM162exceptionOrNullimpl = kotlin.Result.m162exceptionOrNullimpl(objM159constructorimpl);
            if (thM162exceptionOrNullimpl != null) {
                this$0.log.invoke("状态订阅回复失败：" + thM162exceptionOrNullimpl.getMessage());
            }
        }
        return Unit.INSTANCE;
    }

    static final CharSequence newBootstrap$lambda$20$lambda$14(byte it) {
        String str = String.format("%02X", Arrays.copyOf(new Object[]{Byte.valueOf(it)}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    static final Unit newBootstrap$lambda$21(BluetoothBootstrap this$0, BootstrapStatus status) {
        Intrinsics.checkNotNullParameter(status, "status");
        if (!status.getControlReady() && status.getAuthenticated() && !status.getEndpointReceived()) {
            this$0.log.invoke("车机身份已验证，等待无线配置");
        }
        return Unit.INSTANCE;
    }

    private final Iap2Frame subscriptionReply(int messageId) {
        Iap2Frame iap2FrameForRequest = Iap2SubscriptionReplies.INSTANCE.forRequest(messageId);
        if (iap2FrameForRequest == null) {
            switch (messageId) {
                case 20992:
                    Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
                    Iap2BodyBuilder iap2BodyBuilderOpaque = Iap2BodyBuilder.INSTANCE.opaque(20993);
                    iap2BodyBuilderOpaque.u16(0, 0);
                    iap2BodyBuilderOpaque.u8(1, 0);
                    iap2BodyBuilderOpaque.u8(2, 0);
                    return iap2BodyBuilderOpaque.build();
                case 44544:
                    Iap2Messages iap2Messages2 = Iap2Messages.INSTANCE;
                    Iap2BodyBuilder iap2BodyBuilderOpaque2 = Iap2BodyBuilder.INSTANCE.opaque(44545);
                    iap2BodyBuilderOpaque2.u8(0, 0);
                    return iap2BodyBuilderOpaque2.build();
                default:
                    return null;
            }
        }
        return iap2FrameForRequest;
    }

    private final void startReceiver(final Iap2Session current, final PhoneBootstrap boot) {
        Thread thread = new Thread(new Runnable() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                BluetoothBootstrap.startReceiver$lambda$27(this.f$0, current, boot);
            }
        }, "carlink-bluetooth");
        thread.setDaemon(true);
        thread.start();
        this.receiver = thread;
    }

    static final void startReceiver$lambda$27(BluetoothBootstrap this$0, Iap2Session $current, PhoneBootstrap $boot) {
        Object objM159constructorimpl;
        while (!this$0.closed.get() && !$current.isClosed()) {
            try {
                Iap2Frame frame = $current.recv(1000L);
                if (frame != null) {
                    try {
                        kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                        $boot.receive(frame);
                        objM159constructorimpl = kotlin.Result.m159constructorimpl(Unit.INSTANCE);
                    } catch (Throwable th) {
                        kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
                        objM159constructorimpl = kotlin.Result.m159constructorimpl(ResultKt.createFailure(th));
                    }
                    Throwable thM162exceptionOrNullimpl = kotlin.Result.m162exceptionOrNullimpl(objM159constructorimpl);
                    if (thM162exceptionOrNullimpl != null) {
                        this$0.log.invoke("处理车机消息失败：" + thM162exceptionOrNullimpl.getMessage());
                    }
                }
            } catch (Iap2PeerDetectionRestart restartSignal) {
                this$0.restart.complete(restartSignal.initialInput());
                return;
            } catch (Exception e) {
                return;
            }
        }
    }

    /* JADX INFO: compiled from: BluetoothBootstrap.kt */
    @Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0007HÆ\u0003J'\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001J\t\u0010\u0019\u001a\u00020\u001aHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u001b"}, d2 = {"Lcom/carlink/cast/BluetoothBootstrap$Link;", "", "socket", "Landroid/bluetooth/BluetoothSocket;", "session", "Lcom/carplay/iap2/session/Iap2Session;", NotificationCompat.CATEGORY_TRANSPORT, "Lcom/carplay/iap2/transport/PhoneBluetoothSessionTransport;", "<init>", "(Landroid/bluetooth/BluetoothSocket;Lcom/carplay/iap2/session/Iap2Session;Lcom/carplay/iap2/transport/PhoneBluetoothSessionTransport;)V", "getSocket", "()Landroid/bluetooth/BluetoothSocket;", "getSession", "()Lcom/carplay/iap2/session/Iap2Session;", "getTransport", "()Lcom/carplay/iap2/transport/PhoneBluetoothSessionTransport;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class Link {
        private final Iap2Session session;
        private final BluetoothSocket socket;
        private final PhoneBluetoothSessionTransport transport;

        public static /* synthetic */ Link copy$default(Link link, BluetoothSocket bluetoothSocket, Iap2Session iap2Session, PhoneBluetoothSessionTransport phoneBluetoothSessionTransport, int i, Object obj) {
            if ((i & 1) != 0) {
                bluetoothSocket = link.socket;
            }
            if ((i & 2) != 0) {
                iap2Session = link.session;
            }
            if ((i & 4) != 0) {
                phoneBluetoothSessionTransport = link.transport;
            }
            return link.copy(bluetoothSocket, iap2Session, phoneBluetoothSessionTransport);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final BluetoothSocket getSocket() {
            return this.socket;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final Iap2Session getSession() {
            return this.session;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final PhoneBluetoothSessionTransport getTransport() {
            return this.transport;
        }

        public final Link copy(BluetoothSocket socket, Iap2Session session, PhoneBluetoothSessionTransport transport) {
            Intrinsics.checkNotNullParameter(socket, "socket");
            Intrinsics.checkNotNullParameter(session, "session");
            Intrinsics.checkNotNullParameter(transport, "transport");
            return new Link(socket, session, transport);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Link)) {
                return false;
            }
            Link link = (Link) other;
            return Intrinsics.areEqual(this.socket, link.socket) && Intrinsics.areEqual(this.session, link.session) && Intrinsics.areEqual(this.transport, link.transport);
        }

        public int hashCode() {
            return (((this.socket.hashCode() * 31) + this.session.hashCode()) * 31) + this.transport.hashCode();
        }

        public String toString() {
            return "Link(socket=" + this.socket + ", session=" + this.session + ", transport=" + this.transport + ")";
        }

        public Link(BluetoothSocket socket, Iap2Session session, PhoneBluetoothSessionTransport transport) {
            Intrinsics.checkNotNullParameter(socket, "socket");
            Intrinsics.checkNotNullParameter(session, "session");
            Intrinsics.checkNotNullParameter(transport, "transport");
            this.socket = socket;
            this.session = session;
            this.transport = transport;
        }

        public final BluetoothSocket getSocket() {
            return this.socket;
        }

        public final Iap2Session getSession() {
            return this.session;
        }

        public final PhoneBluetoothSessionTransport getTransport() {
            return this.transport;
        }
    }

    /* JADX INFO: compiled from: BluetoothBootstrap.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\u0019\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0005HÆ\u0003J\u001f\u0010\u000e\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001J\t\u0010\u0014\u001a\u00020\u0015HÖ\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0016"}, d2 = {"Lcom/carlink/cast/BluetoothBootstrap$HandshakeAttempt;", "", "link", "Lcom/carlink/cast/BluetoothBootstrap$Link;", "receivedBytes", "", "<init>", "(Lcom/carlink/cast/BluetoothBootstrap$Link;J)V", "getLink", "()Lcom/carlink/cast/BluetoothBootstrap$Link;", "getReceivedBytes", "()J", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class HandshakeAttempt {
        private final Link link;
        private final long receivedBytes;

        public static /* synthetic */ HandshakeAttempt copy$default(HandshakeAttempt handshakeAttempt, Link link, long j, int i, Object obj) {
            if ((i & 1) != 0) {
                link = handshakeAttempt.link;
            }
            if ((i & 2) != 0) {
                j = handshakeAttempt.receivedBytes;
            }
            return handshakeAttempt.copy(link, j);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final Link getLink() {
            return this.link;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final long getReceivedBytes() {
            return this.receivedBytes;
        }

        public final HandshakeAttempt copy(Link link, long receivedBytes) {
            return new HandshakeAttempt(link, receivedBytes);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof HandshakeAttempt)) {
                return false;
            }
            HandshakeAttempt handshakeAttempt = (HandshakeAttempt) other;
            return Intrinsics.areEqual(this.link, handshakeAttempt.link) && this.receivedBytes == handshakeAttempt.receivedBytes;
        }

        public int hashCode() {
            return ((this.link == null ? 0 : this.link.hashCode()) * 31) + Long.hashCode(this.receivedBytes);
        }

        public String toString() {
            return "HandshakeAttempt(link=" + this.link + ", receivedBytes=" + this.receivedBytes + ")";
        }

        public HandshakeAttempt(Link link, long receivedBytes) {
            this.link = link;
            this.receivedBytes = receivedBytes;
        }

        public final Link getLink() {
            return this.link;
        }

        public final long getReceivedBytes() {
            return this.receivedBytes;
        }
    }

    private final Link negotiate() throws IOException {
        BluetoothSocket inbound;
        Link link;
        List<PhoneBluetoothProfile> listAttempts = this.profile.attempts();
        int i = 0;
        for (PhoneBluetoothProfile candidate : listAttempts) {
            int index = i;
            int i2 = i + 1;
            if (this.closed.get()) {
                throw new IllegalStateException("连接已停止".toString());
            }
            this.log.invoke("蓝牙握手 " + (index + 1) + "/" + listAttempts.size() + "：" + candidate.getLabel());
            final Ref.BooleanRef outbound = new Ref.BooleanRef();
            outbound.element = true;
            BluetoothSocket connected = (BluetoothSocket) RfcommConnectionStrategy.INSTANCE.connectWithPhoneService(RfcommConnectionDirection.PHONE_INITIATED, new Function0() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda8
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return BluetoothBootstrap.negotiate$lambda$30(this.f$0);
                }
            }, new Function0() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda9
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return BluetoothBootstrap.negotiate$lambda$32(this.f$0, outbound);
                }
            }, new Function0() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda10
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return BluetoothBootstrap.negotiate$lambda$34(this.f$0, outbound);
                }
            }, new BluetoothBootstrap$negotiate$connected$4(this.closed), new Function1() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda11
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return BluetoothBootstrap.negotiate$lambda$35(this.f$0, (IOException) obj);
                }
            }, new Function1() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda12
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return BluetoothBootstrap.negotiate$lambda$36(this.f$0, (IOException) obj);
                }
            });
            HandshakeAttempt attempt = synchronize(connected, candidate, outbound.element);
            Link link2 = attempt.getLink();
            if (link2 != null) {
                return link2;
            }
            if (RfcommConnectionStrategy.INSTANCE.shouldFallbackToInboundAfterSilentHandshake(RfcommConnectionDirection.PHONE_INITIATED, outbound.element, attempt.getReceivedBytes())) {
                this.log.invoke("手机主动 RFCOMM 已连接但 iAP2 收到 0B；改等车机主动连入本机 iAP2 服务");
                try {
                    inbound = listenInbound(15000L);
                } catch (IOException failure) {
                    if (this.closed.get()) {
                        throw failure;
                    }
                    this.log.invoke("车机主动回连恢复未建立：" + failure.getMessage());
                    inbound = null;
                }
                if (inbound != null && (link = synchronize(inbound, candidate, false).getLink()) != null) {
                    return link;
                }
            }
            this.log.invoke("iAP2 同步未完成，换下一个握手方案");
            i = i2;
        }
        throw new IllegalStateException("蓝牙 iAP2 同步失败：请确认车机屏幕上已选择本机".toString());
    }

    static final Unit negotiate$lambda$30(BluetoothBootstrap this$0) {
        this$0.registerPhoneService();
        return Unit.INSTANCE;
    }

    static final BluetoothSocket negotiate$lambda$32(BluetoothBootstrap this$0, Ref.BooleanRef $outbound) throws IOException {
        BluetoothSocket bluetoothSocketConnectOutbound = this$0.connectOutbound();
        $outbound.element = true;
        return bluetoothSocketConnectOutbound;
    }

    static final BluetoothSocket negotiate$lambda$34(BluetoothBootstrap this$0, Ref.BooleanRef $outbound) {
        BluetoothSocket bluetoothSocketListenInbound$default = listenInbound$default(this$0, 0L, 1, null);
        $outbound.element = false;
        return bluetoothSocketListenInbound$default;
    }

    static final Unit negotiate$lambda$35(BluetoothBootstrap this$0, IOException it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.log.invoke("手机 SDP 注册失败：" + it.getClass().getSimpleName() + "；继续手机主动连接");
        return Unit.INSTANCE;
    }

    static final Unit negotiate$lambda$36(BluetoothBootstrap this$0, IOException it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.log.invoke("主动连接未建立：" + it.getClass().getSimpleName() + "；改为等待车机连接");
        return Unit.INSTANCE;
    }

    private final HandshakeAttempt synchronize(BluetoothSocket connected, PhoneBluetoothProfile candidate, boolean outbound) {
        this.owned.add(connected);
        this.log.invoke("RFCOMM socket 已建立：方向=" + (outbound ? "手机主动" : "车机回连") + "，connected=" + connected.isConnected() + "，bondState=" + connected.getRemoteDevice().getBondState());
        PhoneBluetoothSessionTransport transport = new PhoneBluetoothSessionTransport(new BluetoothRfcommDuplexStream(connected, (Function1<? super String, Unit>) new Function1() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda17
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BluetoothBootstrap.synchronize$lambda$39(this.f$0, (String) obj);
            }
        }));
        this.owned.add(transport);
        final AtomicBoolean traceHandshake = new AtomicBoolean(true);
        Iap2Session session = Iap2Session.INSTANCE.phoneBluetooth(PhoneBluetoothSessionTransport.open$default(transport, null, 1, null), candidate, new Function1() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda18
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BluetoothBootstrap.synchronize$lambda$40(traceHandshake, this, (String) obj);
            }
        }, new Function1() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda19
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BluetoothBootstrap.synchronize$lambda$41(this.f$0, (byte[]) obj);
            }
        });
        this.owned.add(session);
        boolean zAwaitPhoneBluetoothReady$default = Iap2Session.awaitPhoneBluetoothReady$default(session, candidate, null, null, 6, null);
        traceHandshake.set(false);
        Iap2HandshakeStatus status = session.handshakeStatus();
        Function1<String, Unit> function1 = this.log;
        if (!zAwaitPhoneBluetoothReady$default) {
            function1.invoke("iAP2 握手未完成：方向=" + (outbound ? "手机主动" : "车机回连") + "，方案=" + candidate.getLabel() + "，状态=" + status.getState() + "，已发=" + status.getSentBytes() + "B，已收=" + status.getReceivedBytes() + "B，收到车机SYN=" + (status.getPeerSynchronization() != null) + "，控制会话=" + status.getControlSessionId());
            try {
                kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                BluetoothBootstrap bluetoothBootstrap = this;
                session.close();
                kotlin.Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th) {
                kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
                kotlin.Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            try {
                kotlin.Result.Companion companion3 = kotlin.Result.INSTANCE;
                BluetoothBootstrap bluetoothBootstrap2 = this;
                transport.close();
                kotlin.Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th2) {
                kotlin.Result.Companion companion4 = kotlin.Result.INSTANCE;
                kotlin.Result.m159constructorimpl(ResultKt.createFailure(th2));
            }
            try {
                kotlin.Result.Companion companion5 = kotlin.Result.INSTANCE;
                BluetoothBootstrap bluetoothBootstrap3 = this;
                connected.close();
                kotlin.Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th3) {
                kotlin.Result.Companion companion6 = kotlin.Result.INSTANCE;
                kotlin.Result.m159constructorimpl(ResultKt.createFailure(th3));
            }
            return new HandshakeAttempt(null, status.getReceivedBytes());
        }
        function1.invoke("iAP2 链路已同步（" + (outbound ? "手机主动" : "车机回连") + "）");
        return new HandshakeAttempt(new Link(connected, session, transport), status.getReceivedBytes());
    }

    static final Unit synchronize$lambda$39(BluetoothBootstrap this$0, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.log.invoke("蓝牙流诊断：" + it);
        return Unit.INSTANCE;
    }

    static final Unit synchronize$lambda$40(AtomicBoolean $traceHandshake, BluetoothBootstrap this$0, String trace) {
        Intrinsics.checkNotNullParameter(trace, "trace");
        if ($traceHandshake.get()) {
            this$0.log.invoke("iAP2 握手诊断：" + trace);
        }
        return Unit.INSTANCE;
    }

    static final Unit synchronize$lambda$41(BluetoothBootstrap this$0, byte[] datagram) {
        Intrinsics.checkNotNullParameter(datagram, "datagram");
        this$0.nowPlaying.onFileTransferDatagram(datagram);
        return Unit.INSTANCE;
    }

    private final BluetoothSocket connectOutbound() throws IOException {
        final BluetoothSocket outbound = this.peer.createRfcommSocketToServiceRecord(ACCESSORY_UUID);
        RfcommConnectAttempt attempt = new RfcommConnectAttempt(new Function0() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return BluetoothBootstrap.connectOutbound$lambda$45(outbound);
            }
        });
        final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda4
            @Override // java.util.concurrent.ThreadFactory
            public final Thread newThread(Runnable runnable) {
                return BluetoothBootstrap.connectOutbound$lambda$47(runnable);
            }
        });
        try {
            attempt.connect(new AnonymousClass1(outbound), new Function1() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda5
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return BluetoothBootstrap.connectOutbound$lambda$50(scheduler, (Function0) obj);
                }
            });
            if (!outbound.isConnected()) {
                throw new IllegalStateException("主动 RFCOMM 返回后未建立连接".toString());
            }
            Intrinsics.checkNotNull(outbound);
            scheduler.shutdownNow();
            return outbound;
        } catch (Throwable th) {
            scheduler.shutdownNow();
            throw th;
        }
    }

    static final Unit connectOutbound$lambda$45(BluetoothSocket $outbound) throws IOException {
        $outbound.close();
        return Unit.INSTANCE;
    }

    static final Thread connectOutbound$lambda$47(Runnable task) {
        Thread thread = new Thread(task, "carlink-rfcomm-timeout");
        thread.setDaemon(true);
        return thread;
    }

    /* JADX INFO: renamed from: com.carlink.cast.BluetoothBootstrap$connectOutbound$1, reason: invalid class name */
    /* JADX INFO: compiled from: BluetoothBootstrap.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function0<Unit> {
        AnonymousClass1(Object obj) {
            super(0, obj, BluetoothSocket.class, "connect", "connect()V", 0);
        }

        @Override // kotlin.jvm.functions.Function0
        public /* bridge */ /* synthetic */ Unit invoke() throws IOException {
            invoke2();
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2() throws IOException {
            ((BluetoothSocket) this.receiver).connect();
        }
    }

    static final AutoCloseable connectOutbound$lambda$50(ScheduledExecutorService $scheduler, final Function0 timeout) {
        Intrinsics.checkNotNullParameter(timeout, "timeout");
        final ScheduledFuture<?> scheduledFutureSchedule = $scheduler.schedule(new Runnable() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                timeout.invoke();
            }
        }, 20000L, TimeUnit.MILLISECONDS);
        return new AutoCloseable() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda16
            @Override // java.lang.AutoCloseable
            public final void close() {
                scheduledFutureSchedule.cancel(false);
            }
        };
    }

    private final BluetoothServerSocket registerPhoneService() {
        RegisteredRfcommService<BluetoothServerSocket> registeredRfcommService = this.phoneService;
        if (registeredRfcommService == null) {
            registeredRfcommService = new RegisteredRfcommService<>(new Function0() { // from class: com.carlink.cast.BluetoothBootstrap$$ExternalSyntheticLambda13
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return this.f$0.bluetooth.listenUsingRfcommWithServiceRecord("iAP2", BluetoothBootstrap.PHONE_UUID);
                }
            });
            this.phoneService = registeredRfcommService;
        }
        return (BluetoothServerSocket) registeredRfcommService.acquire();
    }

    static /* synthetic */ BluetoothSocket listenInbound$default(BluetoothBootstrap bluetoothBootstrap, long j, int i, Object obj) {
        if ((i & 1) != 0) {
            j = INBOUND_CONNECT_TIMEOUT_MILLIS;
        }
        return bluetoothBootstrap.listenInbound(j);
    }

    private final BluetoothSocket listenInbound(long timeoutMillis) throws IOException {
        if (!(timeoutMillis > 0)) {
            throw new IllegalArgumentException("timeoutMillis must be positive".toString());
        }
        BluetoothServerSocket listener = registerPhoneService();
        this.log.invoke("等待车机回连本机 iAP2 服务，最多 " + TimeUnit.MILLISECONDS.toSeconds(timeoutMillis) + " 秒；请在车机蓝牙列表中选择本机");
        long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMillis);
        while (!this.closed.get() && System.nanoTime() < deadline) {
            long remainingMillis = RangesKt.coerceIn(TimeUnit.NANOSECONDS.toMillis(deadline - System.nanoTime()), 1L, 2147483647L);
            try {
                BluetoothSocket accepted = listener.accept((int) remainingMillis);
                if (accepted == null) {
                    break;
                }
                if (StringsKt.equals(accepted.getRemoteDevice().getAddress(), this.peerAddress, true) && accepted.getRemoteDevice().getBondState() == 12) {
                    this.log.invoke("车机已回连本机");
                    return accepted;
                }
                accepted.close();
            } catch (IOException failure) {
                if (this.closed.get()) {
                    throw new IOException("连接已停止", failure);
                }
                if (System.nanoTime() < deadline) {
                    throw failure;
                }
            }
        }
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeoutMillis);
        throw new IOException(seconds + " 秒内未收到车机的蓝牙回连；请在车机屏幕上选择本机");
    }

    public final boolean isAlive() {
        if (this.closed.get()) {
            return false;
        }
        BluetoothSocket bluetoothSocket = this.socket;
        if (!(bluetoothSocket != null && bluetoothSocket.isConnected())) {
            return false;
        }
        Iap2Session iap2Session = this.session;
        return iap2Session != null && !iap2Session.isClosed();
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        Object objM159constructorimpl;
        Unit unit;
        if (this.closed.compareAndSet(false, true)) {
            this.nowPlaying.close();
            try {
                kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                Thread thread = this.receiver;
                if (thread != null) {
                    thread.interrupt();
                    unit = Unit.INSTANCE;
                } else {
                    unit = null;
                }
                kotlin.Result.m159constructorimpl(unit);
            } catch (Throwable th) {
                kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
                kotlin.Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            for (AutoCloseable autoCloseable : CollectionsKt.reversed(this.owned)) {
                try {
                    kotlin.Result.Companion companion3 = kotlin.Result.INSTANCE;
                    BluetoothBootstrap bluetoothBootstrap = this;
                    BluetoothBootstrap$$ExternalSyntheticAutoCloseableDispatcher0.m(autoCloseable);
                    kotlin.Result.m159constructorimpl(Unit.INSTANCE);
                } catch (Throwable th2) {
                    kotlin.Result.Companion companion4 = kotlin.Result.INSTANCE;
                    kotlin.Result.m159constructorimpl(ResultKt.createFailure(th2));
                }
            }
            this.owned.clear();
            RegisteredRfcommService<BluetoothServerSocket> registeredRfcommService = this.phoneService;
            if (registeredRfcommService != null) {
                try {
                    kotlin.Result.Companion companion5 = kotlin.Result.INSTANCE;
                    BluetoothBootstrap bluetoothBootstrap2 = this;
                    registeredRfcommService.close();
                    objM159constructorimpl = kotlin.Result.m159constructorimpl(Unit.INSTANCE);
                } catch (Throwable th3) {
                    kotlin.Result.Companion companion6 = kotlin.Result.INSTANCE;
                    objM159constructorimpl = kotlin.Result.m159constructorimpl(ResultKt.createFailure(th3));
                }
                kotlin.Result.m158boximpl(objM159constructorimpl);
            }
            this.phoneService = null;
        }
    }

    /* JADX INFO: compiled from: BluetoothBootstrap.kt */
    @Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\n¨\u0006\r"}, d2 = {"Lcom/carlink/cast/BluetoothBootstrap$Companion;", "", "<init>", "()V", "INBOUND_CONNECT_TIMEOUT_MILLIS", "", "SILENT_INBOUND_RECOVERY_TIMEOUT_MILLIS", "PHONE_UUID", "Ljava/util/UUID;", "getPHONE_UUID", "()Ljava/util/UUID;", "ACCESSORY_UUID", "getACCESSORY_UUID", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final UUID getPHONE_UUID() {
            return BluetoothBootstrap.PHONE_UUID;
        }

        public final UUID getACCESSORY_UUID() {
            return BluetoothBootstrap.ACCESSORY_UUID;
        }
    }

    static {
        UUID uuidFromString = UUID.fromString("00000000-deca-fade-deca-deafdecacafe");
        Intrinsics.checkNotNullExpressionValue(uuidFromString, "fromString(...)");
        PHONE_UUID = uuidFromString;
        UUID uuidFromString2 = UUID.fromString("00000000-deca-fade-deca-deafdecacaff");
        Intrinsics.checkNotNullExpressionValue(uuidFromString2, "fromString(...)");
        ACCESSORY_UUID = uuidFromString2;
    }
}
