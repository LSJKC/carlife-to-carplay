package com.carplay.airplay;

import android.util.Log;
import com.carlink.cast.CastSettings;
import com.carplay.media.TouchLatencyProbe;
import com.carplay.mfi.MfiAuthenticator;
import com.carplay.network.TcpLiveness;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Triple;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.CharsKt;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: AirPlaySession.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\u0092\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 È\u00012\u00020\u0001:\u0004Ç\u0001È\u0001BA\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f¢\u0006\u0004\b\u0010\u0010\u0011J\u0006\u0010c\u001a\u00020dJ\u0015\u0010e\u001a\u00020f2\u0006\u0010g\u001a\u00020'H\u0000¢\u0006\u0002\bhJ\r\u0010i\u001a\u00020fH\u0000¢\u0006\u0002\bjJ\u0015\u0010k\u001a\u00020f2\u0006\u0010g\u001a\u00020'H\u0000¢\u0006\u0002\blJ\u0006\u0010m\u001a\u00020fJ\b\u0010n\u001a\u00020fH\u0016J\u000f\u0010o\u001a\u0004\u0018\u000108H\u0000¢\u0006\u0002\bpJ\u000e\u0010q\u001a\u00020<2\u0006\u0010r\u001a\u00020<J\b\u0010x\u001a\u0004\u0018\u00010'J#\u0010y\u001a\u0018\u0012\u0004\u0012\u00020.\u0012\u0006\u0012\u0004\u0018\u00010'\u0012\u0004\u0012\u000208\u0018\u00010zH\u0000¢\u0006\u0002\b{J\u0016\u0010|\u001a\u00020<2\u0006\u0010}\u001a\u00020'2\u0006\u0010~\u001a\u00020<J\u001e\u0010\u007f\u001a\u00020<2\u0006\u0010}\u001a\u00020'2\u0006\u0010~\u001a\u00020<H\u0000¢\u0006\u0003\b\u0080\u0001J\u001a\u0010\u0081\u0001\u001a\u00020<2\u0007\u0010\u0082\u0001\u001a\u00020v2\u0006\u0010}\u001a\u00020'H\u0002J\t\u0010\u0083\u0001\u001a\u00020fH\u0002J\t\u0010\u0084\u0001\u001a\u00020fH\u0002J\t\u0010\u0085\u0001\u001a\u00020fH\u0002J\u0010\u0010\u0086\u0001\u001a\u00020<2\u0007\u0010\u0087\u0001\u001a\u00020<J\u0010\u0010\u0088\u0001\u001a\u00020<2\u0007\u0010\u0089\u0001\u001a\u00020.J\u001f\u0010\u008a\u0001\u001a\u00020<2\u0016\u0010\u008b\u0001\u001a\u0011\u0012\u0004\u0012\u00020'\u0012\u0006\u0012\u0004\u0018\u0001080\u008c\u0001J(\u0010\u008d\u0001\u001a\u00020<2\b\u0010\u008e\u0001\u001a\u00030\u008f\u00012\u0015\u0010g\u001a\u0011\u0012\u0004\u0012\u00020'\u0012\u0006\u0012\u0004\u0018\u0001080\u008c\u0001J\u0019\u0010\u0090\u0001\u001a\u00030\u0091\u00012\u0007\u0010\u0092\u0001\u001a\u00020<H\u0000¢\u0006\u0003\b\u0093\u0001J,\u0010\u0094\u0001\u001a\u00020<2\u0016\u0010\u008b\u0001\u001a\u0011\u0012\u0004\u0012\u00020'\u0012\u0006\u0012\u0004\u0018\u0001080\u008c\u00012\t\b\u0002\u0010\u0095\u0001\u001a\u00020'H\u0002J\u0018\u0010\u0096\u0001\u001a\u00020<2\u000f\u0010\u0097\u0001\u001a\n\u0012\u0005\u0012\u00030\u0099\u00010\u0098\u0001J\u001c\u0010\u009a\u0001\u001a\u00020f2\b\u0010\u009b\u0001\u001a\u00030\u009c\u00012\t\b\u0002\u0010\u009d\u0001\u001a\u00020<J\u0010\u0010\u009e\u0001\u001a\u00020<2\u0007\u0010\u009f\u0001\u001a\u00020<J\u0010\u0010 \u0001\u001a\u00020f2\u0007\u0010\u0089\u0001\u001a\u00020.J\u0010\u0010¡\u0001\u001a\u00020f2\u0007\u0010\u0089\u0001\u001a\u00020.J\u0007\u0010¢\u0001\u001a\u00020fJ\u001c\u0010£\u0001\u001a\u00020<2\u0007\u0010¤\u0001\u001a\u00020!2\n\b\u0002\u0010¥\u0001\u001a\u00030\u008f\u0001J\u0010\u0010¦\u0001\u001a\u00020<2\u0007\u0010§\u0001\u001a\u00020<J\t\u0010¨\u0001\u001a\u00020<H\u0002J\u001b\u0010©\u0001\u001a\u00020<2\u0007\u0010ª\u0001\u001a\u00020.2\u0007\u0010«\u0001\u001a\u00020!H\u0002J\t\u0010¬\u0001\u001a\u00020fH\u0002J\u0014\u0010\u00ad\u0001\u001a\u00030®\u00012\b\u0010¯\u0001\u001a\u00030°\u0001H\u0002J\t\u0010±\u0001\u001a\u00020fH\u0002J\u001c\u0010²\u0001\u001a\u00020f2\u0006\u0010g\u001a\u00020'2\t\b\u0002\u0010³\u0001\u001a\u00020<H\u0002J\u0011\u0010´\u0001\u001a\u00020f2\u0006\u0010g\u001a\u00020'H\u0002J\u0014\u0010µ\u0001\u001a\u00030®\u00012\b\u0010¯\u0001\u001a\u00030°\u0001H\u0002J \u0010¶\u0001\u001a\u000b\u0012\u0006\u0012\u0004\u0018\u0001080\u0098\u00012\f\u0010·\u0001\u001a\u0007\u0012\u0002\b\u00030\u0098\u0001H\u0002J\u0014\u0010¸\u0001\u001a\u00030®\u00012\b\u0010¯\u0001\u001a\u00030°\u0001H\u0002J\u0014\u0010¹\u0001\u001a\u00030®\u00012\b\u0010¯\u0001\u001a\u00030°\u0001H\u0002J\u0012\u0010º\u0001\u001a\u00020.2\u0007\u0010»\u0001\u001a\u00020.H\u0002J\t\u0010¼\u0001\u001a\u00020.H\u0002J\u0011\u0010½\u0001\u001a\u00020f2\u0006\u0010\u0002\u001a\u00020LH\u0002J\t\u0010¾\u0001\u001a\u00020.H\u0002J\t\u0010¿\u0001\u001a\u00020fH\u0002J\u0012\u0010À\u0001\u001a\u00020f2\u0007\u0010Á\u0001\u001a\u00020AH\u0002J\u0011\u0010Â\u0001\u001a\u00020f2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002J\"\u0010Ã\u0001\u001a\u00020f2\u0007\u0010Ä\u0001\u001a\u00020'2\u000e\u0010Å\u0001\u001a\t\u0012\u0004\u0012\u00020f0Æ\u0001H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\u00020\u0013X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0016\u001a\u00020\u0017X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u001c\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u001a\u0010 \u001a\u00020!X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R\u001a\u0010&\u001a\u00020'X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R$\u0010,\u001a\u0012\u0012\u0004\u0012\u00020.0-j\b\u0012\u0004\u0012\u00020.`/X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b0\u00101R\u001e\u00103\u001a\u00020.2\u0006\u00102\u001a\u00020.@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b4\u00105R\u000e\u00106\u001a\u00020.X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u00107\u001a\u0004\u0018\u000108X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00109\u001a\u00020:X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010;\u001a\u00020<8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b=\u0010>R\u000e\u0010?\u001a\u00020:X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010@\u001a\u0004\u0018\u00010AX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010B\u001a\u0004\u0018\u00010\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010C\u001a\u0004\u0018\u00010\u001bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010D\u001a\u00020.X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010E\u001a\u0004\u0018\u00010<X\u0082\u000e¢\u0006\u0004\n\u0002\u0010FR\u000e\u0010G\u001a\u00020:X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u00020:X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010I\u001a\u00020JX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010K\u001a\u0004\u0018\u00010LX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010M\u001a\u0004\u0018\u00010NX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010O\u001a\u000208X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010P\u001a\b\u0012\u0004\u0012\u00020N0QX\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010R\u001a\u00020'¢\u0006\b\n\u0000\u001a\u0004\bS\u0010)R\u0013\u0010T\u001a\u0004\u0018\u00010U¢\u0006\b\n\u0000\u001a\u0004\bV\u0010WR\u0010\u0010X\u001a\u0004\u0018\u00010UX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010Y\u001a\u0004\u0018\u00010U8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\bZ\u0010WR\u0013\u0010[\u001a\u0004\u0018\u00010'8F¢\u0006\u0006\u001a\u0004\b\\\u0010)R\u0013\u0010]\u001a\u0004\u0018\u00010!8F¢\u0006\u0006\u001a\u0004\b^\u0010#R\u0011\u0010_\u001a\u00020<8F¢\u0006\u0006\u001a\u0004\b`\u0010>R\u000e\u0010a\u001a\u00020bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010s\u001a\u000208X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010t\u001a\u000208X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010u\u001a\u0004\u0018\u00010vX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010w\u001a\u0004\u0018\u00010'X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006É\u0001"}, d2 = {"Lcom/carplay/airplay/AirPlaySession;", "Ljava/io/Closeable;", "socket", "Ljava/net/Socket;", "config", "Lcom/carplay/airplay/AirPlayConfig;", CastSettings.KEY_IDENTITY, "Lcom/carplay/airplay/AirPlayIdentity;", "pairings", "Lcom/carplay/airplay/PairingStore;", "mfi", "Lcom/carplay/mfi/MfiAuthenticator;", "listener", "Lcom/carplay/airplay/AirPlaySessionListener;", "media", "Lcom/carplay/airplay/AirPlayMediaHandler;", "<init>", "(Ljava/net/Socket;Lcom/carplay/airplay/AirPlayConfig;Lcom/carplay/airplay/AirPlayIdentity;Lcom/carplay/airplay/PairingStore;Lcom/carplay/mfi/MfiAuthenticator;Lcom/carplay/airplay/AirPlaySessionListener;Lcom/carplay/airplay/AirPlayMediaHandler;)V", "pairSetup", "Lcom/carplay/airplay/PairSetup;", "getPairSetup$core_airplay", "()Lcom/carplay/airplay/PairSetup;", "pairVerify", "Lcom/carplay/airplay/PairVerify;", "getPairVerify$core_airplay", "()Lcom/carplay/airplay/PairVerify;", "cipher", "Lcom/carplay/airplay/ControlCipher;", "getCipher$core_airplay", "()Lcom/carplay/airplay/ControlCipher;", "setCipher$core_airplay", "(Lcom/carplay/airplay/ControlCipher;)V", "encBuf", "", "getEncBuf$core_airplay", "()[B", "setEncBuf$core_airplay", "([B)V", "deviceBtMac", "", "getDeviceBtMac$core_airplay", "()Ljava/lang/String;", "setDeviceBtMac$core_airplay", "(Ljava/lang/String;)V", "activeStreams", "Ljava/util/LinkedHashSet;", "", "Lkotlin/collections/LinkedHashSet;", "getActiveStreams$core_airplay", "()Ljava/util/LinkedHashSet;", "value", "clusterStream", "getClusterStream", "()I", "clusterStreamSetups", "mainScreenToken", "", "closed", "Ljava/util/concurrent/atomic/AtomicBoolean;", "isClosed", "", "isClosed$core_airplay", "()Z", "notified", "eventServer", "Ljava/net/ServerSocket;", "eventSocket", "eventCipher", "eventCseq", "pendingNightMode", "Ljava/lang/Boolean;", "firstTouchSendLogged", "touchSendFailureLogged", "ntp", "Lcom/carplay/airplay/NtpClock;", "keepAliveSocket", "Ljava/net/DatagramSocket;", "keepAliveThread", "Ljava/lang/Thread;", "eventWriteLock", "eventThreads", "Ljava/util/concurrent/CopyOnWriteArrayList;", "host", "getHost", "localAddress", "Ljava/net/InetAddress;", "getLocalAddress", "()Ljava/net/InetAddress;", "peerAddress", "remoteAddress", "getRemoteAddress$core_airplay", "controllerId", "getControllerId", "sharedSecret", "getSharedSecret", "videoInCar", "getVideoInCar", "videoPlaybackAvailability", "Lcom/carplay/airplay/VideoPlaybackAvailability;", "syncedNtp", "Ljava/math/BigInteger;", "logDebug", "", "message", "logDebug$core_airplay", "videoFrameRendered", "videoFrameRendered$core_airplay", "logTrace", "logTrace$core_airplay", "start", "close", "mainScreenSessionToken", "mainScreenSessionToken$core_airplay", "setClusterUiShown", "shown", "clusterContentLock", "clusterCommandLock", "clusterContent", "Lcom/carplay/airplay/AirPlaySession$ClusterContent;", "requestedClusterUrl", "clusterUrl", "clusterContentRoute", "Lkotlin/Triple;", "clusterContentRoute$core_airplay", "setClusterUrl", "url", "send", "restoreClusterUrl", "restoreClusterUrl$core_airplay", "showClusterUrl", "content", "beginClusterContent", "clearClusterContent", "reapplyClusterContent", "changeMapZoomLevel", "zoomIn", "showViewArea", "index", "sendCommand", "command", "", "sendRemoteControlMessage", "streamId", "", "setVideoPlaybackAllowed", "Lcom/carplay/airplay/VideoPlaybackDelivery;", "allowed", "setVideoPlaybackAllowed$core_airplay", "sendCommandLocked", "extraHeaders", "sendTouch", "contacts", "", "Lcom/carplay/airplay/AirPlayContact;", "sendKnob", "state", "Lcom/carplay/airplay/AirPlayKnobState;", "momentary", "sendKnobSelect", "down", "sendMedia", "sendTelephony", "invokeSiri", "sendIapMessage", "data", "timeoutMillis", "setNightMode", "night", "sendPendingNightModeLocked", "sendHidReport", "uid", "report", "runControl", "handle", "Lcom/carplay/airplay/RtspMessage$Response;", "request", "Lcom/carplay/airplay/RtspMessage$Request;", "notifySetupResponseSent", "debugLog", "uiVisible", "trace", "handleSetup", "handleStreams", "streams", "handleCommand", "handleTeardown", "openTiming", "peerPort", "openKeepAlive", "runKeepAlive", "openEvent", "teardown", "acceptEvent", "server", "runEventRead", "spawnEvent", "name", "body", "Lkotlin/Function0;", "ClusterContent", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AirPlaySession implements Closeable {

    @Deprecated
    public static final long EVENT_READY_POLL_MILLIS = 25;

    @Deprecated
    public static final long NANOS_PER_MILLISECOND = 1000000;

    @Deprecated
    public static final String OCTET_CONTENT_TYPE = "application/octet-stream";

    @Deprecated
    public static final String PAIRING_CONTENT_TYPE = "application/pairing+tlv8";

    @Deprecated
    public static final String PLIST_CONTENT_TYPE = "application/x-apple-binary-plist";

    @Deprecated
    public static final int READ_CHUNK_BYTES = 16384;

    @Deprecated
    public static final int STREAM_TYPE_ALT_AUDIO = 101;

    @Deprecated
    public static final int STREAM_TYPE_ALT_SCREEN = 111;

    @Deprecated
    public static final int STREAM_TYPE_AUX_AUDIO = 106;

    @Deprecated
    public static final int STREAM_TYPE_DATA = 130;

    @Deprecated
    public static final int STREAM_TYPE_MAIN_AUDIO = 100;

    @Deprecated
    public static final int STREAM_TYPE_MAIN_BUFFERED_AUDIO = 103;

    @Deprecated
    public static final int STREAM_TYPE_MAIN_HIGH_AUDIO = 102;

    @Deprecated
    public static final int STREAM_TYPE_MAIN_SCREEN = 110;

    @Deprecated
    public static final String TAG = "xcertplay-usb";

    @Deprecated
    public static final int ZOOM_DIRECTION_IN = 0;

    @Deprecated
    public static final int ZOOM_DIRECTION_OUT = 1;
    private final LinkedHashSet<Integer> activeStreams;
    private ControlCipher cipher;
    private final AtomicBoolean closed;
    private final Object clusterCommandLock;
    private volatile ClusterContent clusterContent;
    private final Object clusterContentLock;
    private volatile int clusterStream;
    private int clusterStreamSetups;
    private final AirPlayConfig config;
    private String deviceBtMac;
    private byte[] encBuf;
    private ControlCipher eventCipher;
    private int eventCseq;
    private ServerSocket eventServer;
    private Socket eventSocket;
    private final CopyOnWriteArrayList<Thread> eventThreads;
    private final Object eventWriteLock;
    private final AtomicBoolean firstTouchSendLogged;
    private final String host;
    private final AirPlayIdentity identity;
    private DatagramSocket keepAliveSocket;
    private Thread keepAliveThread;
    private final AirPlaySessionListener listener;
    private final InetAddress localAddress;
    private volatile Object mainScreenToken;
    private final AirPlayMediaHandler media;
    private final MfiAuthenticator mfi;
    private final AtomicBoolean notified;
    private final NtpClock ntp;
    private final PairSetup pairSetup;
    private final PairVerify pairVerify;
    private final PairingStore pairings;
    private final InetAddress peerAddress;
    private Boolean pendingNightMode;
    private String requestedClusterUrl;
    private final Socket socket;
    private final AtomicBoolean touchSendFailureLogged;
    private final VideoPlaybackAvailability videoPlaybackAvailability;
    private static final Companion Companion = new Companion(null);
    private static final Set<String> BUFFERED_AUDIO_METHODS = SetsKt.setOf((Object[]) new String[]{"SETRATE", "SETRATEANCHORTIME", "GETANCHOR", "FLUSHBUFFERED"});

    public AirPlaySession(Socket socket, AirPlayConfig config, AirPlayIdentity identity, PairingStore pairings, MfiAuthenticator mfi, AirPlaySessionListener listener, AirPlayMediaHandler media) {
        String hostAddress;
        Intrinsics.checkNotNullParameter(socket, "socket");
        Intrinsics.checkNotNullParameter(config, "config");
        Intrinsics.checkNotNullParameter(identity, "identity");
        Intrinsics.checkNotNullParameter(pairings, "pairings");
        Intrinsics.checkNotNullParameter(listener, "listener");
        Intrinsics.checkNotNullParameter(media, "media");
        this.socket = socket;
        this.config = config;
        this.identity = identity;
        this.pairings = pairings;
        this.mfi = mfi;
        this.listener = listener;
        this.media = media;
        this.pairSetup = new PairSetup(this.identity, this.pairings);
        this.pairVerify = new PairVerify(this.identity, this.pairings);
        this.encBuf = new byte[0];
        String str = "";
        this.deviceBtMac = "";
        this.activeStreams = new LinkedHashSet<>();
        this.closed = new AtomicBoolean(false);
        this.notified = new AtomicBoolean(false);
        this.firstTouchSendLogged = new AtomicBoolean(false);
        this.touchSendFailureLogged = new AtomicBoolean(false);
        this.ntp = new NtpClock(null, null, 3, null);
        this.eventWriteLock = new Object();
        this.eventThreads = new CopyOnWriteArrayList<>();
        InetAddress inetAddress = this.socket.getInetAddress();
        if (inetAddress != null && (hostAddress = inetAddress.getHostAddress()) != null) {
            str = hostAddress;
        }
        this.host = str;
        this.localAddress = this.socket.getLocalAddress();
        this.peerAddress = this.socket.getInetAddress();
        this.videoPlaybackAvailability = new VideoPlaybackAvailability(new Function1() { // from class: com.carplay.airplay.AirPlaySession$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(this.f$0.sendCommand(MapsKt.linkedMapOf(TuplesKt.to("type", "setVideoPlaybackAllowed"), TuplesKt.to("params", MapsKt.linkedMapOf(TuplesKt.to("videoPlaybackAllowed", Boolean.valueOf(((Boolean) obj).booleanValue())))))));
            }
        });
        this.clusterContentLock = new Object();
        this.clusterCommandLock = new Object();
        AirPlayDisplayConfig cluster = this.config.getCluster();
        this.requestedClusterUrl = cluster != null ? cluster.getInitialUrl() : null;
    }

    /* JADX INFO: renamed from: getPairSetup$core_airplay, reason: from getter */
    public final PairSetup getPairSetup() {
        return this.pairSetup;
    }

    /* JADX INFO: renamed from: getPairVerify$core_airplay, reason: from getter */
    public final PairVerify getPairVerify() {
        return this.pairVerify;
    }

    /* JADX INFO: renamed from: getCipher$core_airplay, reason: from getter */
    public final ControlCipher getCipher() {
        return this.cipher;
    }

    public final void setCipher$core_airplay(ControlCipher controlCipher) {
        this.cipher = controlCipher;
    }

    /* JADX INFO: renamed from: getEncBuf$core_airplay, reason: from getter */
    public final byte[] getEncBuf() {
        return this.encBuf;
    }

    public final void setEncBuf$core_airplay(byte[] bArr) {
        Intrinsics.checkNotNullParameter(bArr, "<set-?>");
        this.encBuf = bArr;
    }

    /* JADX INFO: renamed from: getDeviceBtMac$core_airplay, reason: from getter */
    public final String getDeviceBtMac() {
        return this.deviceBtMac;
    }

    public final void setDeviceBtMac$core_airplay(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.deviceBtMac = str;
    }

    public final LinkedHashSet<Integer> getActiveStreams$core_airplay() {
        return this.activeStreams;
    }

    public final int getClusterStream() {
        return this.clusterStream;
    }

    public final boolean isClosed$core_airplay() {
        return this.closed.get();
    }

    public final String getHost() {
        return this.host;
    }

    public final InetAddress getLocalAddress() {
        return this.localAddress;
    }

    public final InetAddress getRemoteAddress$core_airplay() {
        SocketAddress remoteSocketAddress = this.socket.getRemoteSocketAddress();
        InetSocketAddress inetSocketAddress = remoteSocketAddress instanceof InetSocketAddress ? (InetSocketAddress) remoteSocketAddress : null;
        if (inetSocketAddress != null) {
            return inetSocketAddress.getAddress();
        }
        return null;
    }

    public final String getControllerId() {
        return this.pairVerify.getControllerId();
    }

    public final byte[] getSharedSecret() {
        byte[] sharedSecret = this.pairVerify.getSharedSecret();
        if (sharedSecret == null) {
            return null;
        }
        byte[] bArrCopyOf = Arrays.copyOf(sharedSecret, sharedSecret.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        return bArrCopyOf;
    }

    public final boolean getVideoInCar() {
        return this.config.getVideoInCar();
    }

    public final BigInteger syncedNtp() {
        return this.ntp.syncedNtp();
    }

    public final void logDebug$core_airplay(String message) {
        Intrinsics.checkNotNullParameter(message, "message");
        debugLog$default(this, message, false, 2, null);
    }

    public final void videoFrameRendered$core_airplay() {
        if (!this.closed.get()) {
            this.listener.onVideoFrameRendered(this);
        }
    }

    public final void logTrace$core_airplay(String message) {
        Intrinsics.checkNotNullParameter(message, "message");
        trace(message);
    }

    public final void start() {
        Thread $this$start_u24lambda_u241 = new Thread(new Runnable() { // from class: com.carplay.airplay.AirPlaySession$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() throws Throwable {
                this.f$0.runControl();
            }
        }, "airplay-control");
        $this$start_u24lambda_u241.setDaemon(true);
        $this$start_u24lambda_u241.start();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.closed.compareAndSet(false, true)) {
            this.mainScreenToken = null;
            clearClusterContent();
            AirPlaySessionKt.safeClose(this.socket);
            try {
                this.media.onSessionClosed(this);
            } catch (Exception error) {
                Log.w("xcertplay-usb", "airplay media stream teardown failed", error);
            }
            teardown();
            if (this.notified.compareAndSet(false, true)) {
                this.listener.onSessionEnded(this);
            }
        }
    }

    public final Object mainScreenSessionToken$core_airplay() {
        if (this.closed.get()) {
            return null;
        }
        return this.mainScreenToken;
    }

    public final boolean setClusterUiShown(boolean shown) {
        ClusterContent content;
        synchronized (this.clusterCommandLock) {
            if (!this.closed.get() && this.clusterStream > 0) {
                synchronized (this.clusterContentLock) {
                    content = this.clusterContent;
                }
                if (content == null) {
                    return false;
                }
                if (!shown) {
                    boolean sent = sendCommand(MapsKt.mapOf(TuplesKt.to("type", "stopUI"), TuplesKt.to("params", MapsKt.mapOf(TuplesKt.to("uuid", AirPlayInfoPlist.ALT_UUID)))));
                    synchronized (this.clusterContentLock) {
                        ClusterContent current = this.clusterContent;
                        if (current == null) {
                            return false;
                        }
                        if (!this.closed.get() && current.getStream() == content.getStream()) {
                            if (sent) {
                                this.clusterContent = ClusterContent.copy$default(current, 0, null, null, false, null, 23, null);
                            }
                            return sent;
                        }
                        if (sent) {
                            this.clusterContent = ClusterContent.copy$default(current, 0, null, null, false, new Object(), 3, null);
                        }
                        return false;
                    }
                }
                String requested = content.getRequested();
                if (requested == null) {
                    return false;
                }
                return showClusterUrl(content, requested);
            }
            return false;
        }
    }

    /* JADX INFO: compiled from: AirPlaySession.kt */
    @Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0017\b\u0082\b\u0018\u00002\u00020\u0001B7\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\u0001¢\u0006\u0004\b\n\u0010\u000bJ\t\u0010\u0015\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\u0016\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\t\u0010\u0018\u001a\u00020\bHÆ\u0003J\t\u0010\u0019\u001a\u00020\u0001HÆ\u0003J?\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u0001HÆ\u0001J\u0013\u0010\u001b\u001a\u00020\b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001d\u001a\u00020\u0003HÖ\u0001J\t\u0010\u001e\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\t\u001a\u00020\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014¨\u0006\u001f"}, d2 = {"Lcom/carplay/airplay/AirPlaySession$ClusterContent;", "", "stream", "", "requested", "", "displayed", "shown", "", "token", "<init>", "(ILjava/lang/String;Ljava/lang/String;ZLjava/lang/Object;)V", "getStream", "()I", "getRequested", "()Ljava/lang/String;", "getDisplayed", "getShown", "()Z", "getToken", "()Ljava/lang/Object;", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "toString", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class ClusterContent {
        private final String displayed;
        private final String requested;
        private final boolean shown;
        private final int stream;
        private final Object token;

        public static /* synthetic */ ClusterContent copy$default(ClusterContent clusterContent, int i, String str, String str2, boolean z, Object obj, int i2, Object obj2) {
            if ((i2 & 1) != 0) {
                i = clusterContent.stream;
            }
            if ((i2 & 2) != 0) {
                str = clusterContent.requested;
            }
            if ((i2 & 4) != 0) {
                str2 = clusterContent.displayed;
            }
            if ((i2 & 8) != 0) {
                z = clusterContent.shown;
            }
            if ((i2 & 16) != 0) {
                obj = clusterContent.token;
            }
            Object obj3 = obj;
            String str3 = str2;
            return clusterContent.copy(i, str, str3, z, obj3);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final int getStream() {
            return this.stream;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final String getRequested() {
            return this.requested;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final String getDisplayed() {
            return this.displayed;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final boolean getShown() {
            return this.shown;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final Object getToken() {
            return this.token;
        }

        public final ClusterContent copy(int stream, String requested, String displayed, boolean shown, Object token) {
            Intrinsics.checkNotNullParameter(token, "token");
            return new ClusterContent(stream, requested, displayed, shown, token);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ClusterContent)) {
                return false;
            }
            ClusterContent clusterContent = (ClusterContent) other;
            return this.stream == clusterContent.stream && Intrinsics.areEqual(this.requested, clusterContent.requested) && Intrinsics.areEqual(this.displayed, clusterContent.displayed) && this.shown == clusterContent.shown && Intrinsics.areEqual(this.token, clusterContent.token);
        }

        public int hashCode() {
            return (((((((Integer.hashCode(this.stream) * 31) + (this.requested == null ? 0 : this.requested.hashCode())) * 31) + (this.displayed != null ? this.displayed.hashCode() : 0)) * 31) + Boolean.hashCode(this.shown)) * 31) + this.token.hashCode();
        }

        public String toString() {
            return "ClusterContent(stream=" + this.stream + ", requested=" + this.requested + ", displayed=" + this.displayed + ", shown=" + this.shown + ", token=" + this.token + ")";
        }

        public ClusterContent(int stream, String requested, String displayed, boolean shown, Object token) {
            Intrinsics.checkNotNullParameter(token, "token");
            this.stream = stream;
            this.requested = requested;
            this.displayed = displayed;
            this.shown = shown;
            this.token = token;
        }

        public /* synthetic */ ClusterContent(int i, String str, String str2, boolean z, Object obj, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            boolean z2;
            Object obj2;
            if ((i2 & 8) == 0) {
                z2 = z;
            } else {
                z2 = true;
            }
            if ((i2 & 16) == 0) {
                obj2 = obj;
            } else {
                obj2 = new Object();
            }
            this(i, str, str2, z2, obj2);
        }

        public final String getDisplayed() {
            return this.displayed;
        }

        public final String getRequested() {
            return this.requested;
        }

        public final int getStream() {
            return this.stream;
        }

        public final boolean getShown() {
            return this.shown;
        }

        public final Object getToken() {
            return this.token;
        }
    }

    public final String clusterUrl() {
        Triple<Integer, String, Object> tripleClusterContentRoute$core_airplay = clusterContentRoute$core_airplay();
        if (tripleClusterContentRoute$core_airplay != null) {
            return tripleClusterContentRoute$core_airplay.getSecond();
        }
        return null;
    }

    public final Triple<Integer, String, Object> clusterContentRoute$core_airplay() {
        ClusterContent content = this.clusterContent;
        if (content == null || this.closed.get() || content.getStream() != this.clusterStream) {
            return null;
        }
        return new Triple<>(Integer.valueOf(content.getStream()), content.getDisplayed(), content.getToken());
    }

    public final boolean setClusterUrl(String url, boolean send) throws Throwable {
        Throwable th;
        Intrinsics.checkNotNullParameter(url, "url");
        synchronized (this.clusterCommandLock) {
            try {
                synchronized (this.clusterContentLock) {
                    try {
                        try {
                            ClusterContent current = this.clusterContent;
                            if (current == null) {
                                try {
                                    return false;
                                } catch (Throwable th2) {
                                    th = th2;
                                    throw th;
                                }
                            }
                            if (this.closed.get() || current.getStream() != this.clusterStream || this.config.getCluster() == null) {
                                return false;
                            }
                            this.requestedClusterUrl = url;
                            try {
                                ClusterContent content = ClusterContent.copy$default(current, 0, url, null, false, null, 29, null);
                                this.clusterContent = content;
                                return !send ? true : showClusterUrl(content, url);
                            } catch (Throwable th3) {
                                th = th3;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                        }
                        throw th;
                    } catch (Throwable th5) {
                        th = th5;
                    }
                }
            } catch (Throwable th6) {
                th = th6;
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:34:0x0058  */
    public final boolean restoreClusterUrl$core_airplay(String url, boolean send) throws Throwable {
        Throwable th;
        Throwable th2;
        String url2;
        ClusterContent it;
        boolean zShowClusterUrl;
        Intrinsics.checkNotNullParameter(url, "url");
        synchronized (this.clusterCommandLock) {
            try {
                synchronized (this.clusterContentLock) {
                    try {
                        try {
                            if (this.closed.get() || this.config.getCluster() == null) {
                                return false;
                            }
                            this.requestedClusterUrl = url;
                            ClusterContent clusterContent = this.clusterContent;
                            if (clusterContent != null) {
                                url2 = url;
                                try {
                                    it = ClusterContent.copy$default(clusterContent, 0, url2, null, false, null, 29, null);
                                    if (it != null) {
                                        this.clusterContent = it;
                                    }
                                    if (it == null && send && (!it.getShown() || !Intrinsics.areEqual(it.getDisplayed(), url2))) {
                                        zShowClusterUrl = showClusterUrl(it, url2);
                                    } else {
                                        zShowClusterUrl = true;
                                    }
                                    return zShowClusterUrl;
                                } catch (Throwable th3) {
                                    th2 = th3;
                                }
                            } else {
                                url2 = url;
                            }
                            it = null;
                            if (it == null) {
                                zShowClusterUrl = true;
                            } else {
                                zShowClusterUrl = true;
                            }
                            return zShowClusterUrl;
                        } catch (Throwable th4) {
                            th2 = th4;
                        }
                        throw th2;
                    } catch (Throwable th5) {
                        th = th5;
                        throw th;
                    }
                }
            } catch (Throwable th6) {
                th = th6;
            }
        }
    }

    private final boolean showClusterUrl(ClusterContent content, String url) {
        if (this.closed.get() || this.clusterStream != content.getStream()) {
            return false;
        }
        boolean uiSent = sendCommand(MapsKt.mapOf(TuplesKt.to("type", "showUI"), TuplesKt.to("params", MapsKt.mapOf(TuplesKt.to("uuid", AirPlayInfoPlist.ALT_UUID), TuplesKt.to("url", url)))));
        boolean sent = uiSent && this.clusterStream == content.getStream() && !this.closed.get() && sendCommand(MapsKt.mapOf(TuplesKt.to("type", "forceKeyFrame"), TuplesKt.to("params", MapsKt.mapOf(TuplesKt.to("uuid", AirPlayInfoPlist.ALT_UUID)))));
        synchronized (this.clusterContentLock) {
            ClusterContent current = this.clusterContent;
            if (current == null) {
                return false;
            }
            if (!this.closed.get() && current.getStream() == content.getStream() && this.clusterStream == content.getStream()) {
                if (uiSent) {
                    this.clusterContent = ClusterContent.copy$default(current, 0, null, sent ? url : null, true, new Object(), 3, null);
                }
                return sent;
            }
            if (uiSent) {
                this.clusterContent = ClusterContent.copy$default(current, 0, null, null, false, new Object(), 11, null);
            }
            return false;
        }
    }

    private final void beginClusterContent() {
        synchronized (this.clusterContentLock) {
            this.clusterStreamSetups++;
            int stream = this.clusterStreamSetups;
            AirPlayDisplayConfig cluster = this.config.getCluster();
            String initial = cluster != null ? cluster.getInitialUrl() : null;
            this.clusterContent = new ClusterContent(stream, this.requestedClusterUrl, initial, false, null, 24, null);
            this.clusterStream = stream;
            Unit unit = Unit.INSTANCE;
        }
    }

    private final void clearClusterContent() {
        synchronized (this.clusterContentLock) {
            this.clusterStream = 0;
            this.clusterContent = null;
            Unit unit = Unit.INSTANCE;
        }
    }

    private final void reapplyClusterContent() {
        synchronized (this.clusterCommandLock) {
            ClusterContent content = this.clusterContent;
            if (content == null) {
                return;
            }
            String requested = content.getRequested();
            if (requested == null) {
                return;
            }
            if (content.getShown() && !Intrinsics.areEqual(requested, content.getDisplayed())) {
                showClusterUrl(content, requested);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public final boolean changeMapZoomLevel(boolean zoomIn) {
        return this.clusterStream > 0 && sendCommand(MapsKt.linkedMapOf(TuplesKt.to("type", "changeMapZoomLevel"), TuplesKt.to("params", MapsKt.linkedMapOf(TuplesKt.to("uuid", AirPlayInfoPlist.ALT_UUID), TuplesKt.to("zoomDirection", Integer.valueOf(!zoomIn ? 1 : 0))))));
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public final boolean showViewArea(int index) {
        AirPlayInfoPlist airPlayInfoPlist = AirPlayInfoPlist.INSTANCE;
        List<AirPlayViewArea> viewAreas = this.config.getMain().getViewAreas();
        return sendCommand(airPlayInfoPlist.viewAreaCommand(index, viewAreas != null ? viewAreas.size() : 1));
    }

    public final boolean sendCommand(Map<String, ? extends Object> command) {
        boolean zSendCommandLocked$default;
        Intrinsics.checkNotNullParameter(command, "command");
        synchronized (this.eventWriteLock) {
            zSendCommandLocked$default = sendCommandLocked$default(this, command, null, 2, null);
        }
        return zSendCommandLocked$default;
    }

    public final boolean sendRemoteControlMessage(long streamId, Map<String, ? extends Object> message) {
        boolean zSendCommandLocked;
        Intrinsics.checkNotNullParameter(message, "message");
        synchronized (this.eventWriteLock) {
            zSendCommandLocked = sendCommandLocked(MapsKt.linkedMapOf(TuplesKt.to("params", MapsKt.linkedMapOf(TuplesKt.to("data", BplistCodec.INSTANCE.encode(message))))), "X-Apple-StreamID: " + streamId + "\r\n");
        }
        return zSendCommandLocked;
    }

    public final VideoPlaybackDelivery setVideoPlaybackAllowed$core_airplay(boolean allowed) {
        return this.videoPlaybackAvailability.setDesired(allowed);
    }

    static /* synthetic */ boolean sendCommandLocked$default(AirPlaySession airPlaySession, Map map, String str, int i, Object obj) {
        if ((i & 2) != 0) {
            str = "";
        }
        return airPlaySession.sendCommandLocked(map, str);
    }

    private final boolean sendCommandLocked(Map<String, ? extends Object> command, String extraHeaders) {
        ControlCipher cipher;
        Socket socket = this.eventSocket;
        if (socket == null || (cipher = this.eventCipher) == null) {
            return false;
        }
        this.eventCseq++;
        byte[] body = BplistCodec.INSTANCE.encode(command);
        String head = "POST /command RTSP/1.0\r\n" + extraHeaders + "Content-Type: application/x-apple-binary-plist\r\nContent-Length: " + body.length + "\r\nCSeq: " + this.eventCseq + "\r\n\r\n";
        trace("airplay event tx headers=" + head + " bodyHex=" + AirPlaySessionKt.toHex(body));
        try {
            byte[] bytes = head.getBytes(Charsets.US_ASCII);
            Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
            byte[] bytes2 = cipher.encrypt(ArraysKt.plus(bytes, body));
            OutputStream output = socket.getOutputStream();
            output.write(bytes2);
            output.flush();
            return true;
        } catch (Exception error) {
            Log.w("xcertplay-usb", "airplay event command failed type=" + command.get("type"), error);
            close();
            return false;
        }
    }

    public final boolean sendTouch(List<AirPlayContact> contacts) {
        Intrinsics.checkNotNullParameter(contacts, "contacts");
        List<AirPlayContact> $this$map$iv = contacts;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (Object item$iv$iv : $this$map$iv) {
            AirPlayContact it = (AirPlayContact) item$iv$iv;
            destination$iv$iv.add(AirPlayContact.copy$default(it, 0, it.getX() * ((double) this.config.getMain().getWidthPixels()), it.getY() * ((double) this.config.getMain().getHeightPixels()), false, 9, null));
            $this$map$iv = $this$map$iv;
        }
        List scaled = (List) destination$iv$iv;
        byte[] report = AirPlayHid.INSTANCE.touchReport(scaled);
        long sendStartNs = System.nanoTime();
        boolean sent = sendHidReport(AirPlayHid.TOUCH_HID_UID, report);
        if (sent) {
            TouchLatencyProbe.INSTANCE.onTouchSent(sendStartNs, System.nanoTime() - sendStartNs);
        }
        if (sent && this.firstTouchSendLogged.compareAndSet(false, true)) {
            AirPlayContact first = (AirPlayContact) CollectionsKt.firstOrNull(scaled);
            Log.i("xcertplay-usb", "airplay touch report sent contacts=" + scaled.size() + " first=(" + (first != null ? Double.valueOf(first.getX()) : null) + "," + (first != null ? Double.valueOf(first.getY()) : null) + ",down=" + (first != null ? Boolean.valueOf(first.getDown()) : null) + ") report=" + BytesKt.toHexString(report));
        } else if (!sent && this.touchSendFailureLogged.compareAndSet(false, true)) {
            Log.w("xcertplay-usb", "airplay touch dropped: event channel is not ready");
        }
        return sent;
    }

    public static /* synthetic */ void sendKnob$default(AirPlaySession airPlaySession, AirPlayKnobState airPlayKnobState, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = true;
        }
        airPlaySession.sendKnob(airPlayKnobState, z);
    }

    public final void sendKnob(AirPlayKnobState state, boolean momentary) {
        Intrinsics.checkNotNullParameter(state, "state");
        sendHidReport(AirPlayHid.KNOB_HID_UID, AirPlayHid.INSTANCE.knobReport(state));
        if (momentary) {
            sendHidReport(AirPlayHid.KNOB_HID_UID, AirPlayHid.INSTANCE.knobReport(new AirPlayKnobState(false, false, false, 0, 0, 0, 63, null)));
        }
    }

    public final boolean sendKnobSelect(boolean down) {
        return sendHidReport(AirPlayHid.KNOB_HID_UID, AirPlayHid.INSTANCE.knobReport(new AirPlayKnobState(down, false, false, 0, 0, 0, 62, null)));
    }

    public final void sendMedia(int index) {
        sendHidReport(AirPlayHid.MEDIA_HID_UID, AirPlayHid.INSTANCE.mediaReport(index));
        sendHidReport(AirPlayHid.MEDIA_HID_UID, AirPlayHid.INSTANCE.mediaReport(0));
    }

    public final void sendTelephony(int index) {
        sendHidReport(AirPlayHid.TELEPHONY_HID_UID, AirPlayHid.INSTANCE.telephonyReport(index));
        sendHidReport(AirPlayHid.TELEPHONY_HID_UID, AirPlayHid.INSTANCE.telephonyReport(0));
    }

    public final void invokeSiri() {
        sendCommand(MapsKt.linkedMapOf(TuplesKt.to("type", "requestSiri"), TuplesKt.to("params", MapsKt.linkedMapOf(TuplesKt.to("siriAction", 2)))));
        sendCommand(MapsKt.linkedMapOf(TuplesKt.to("type", "requestSiri"), TuplesKt.to("params", MapsKt.linkedMapOf(TuplesKt.to("siriAction", 3)))));
    }

    public static /* synthetic */ boolean sendIapMessage$default(AirPlaySession airPlaySession, byte[] bArr, long j, int i, Object obj) {
        if ((i & 2) != 0) {
            j = 0;
        }
        return airPlaySession.sendIapMessage(bArr, j);
    }

    public final boolean sendIapMessage(byte[] data, long timeoutMillis) throws InterruptedException {
        AirPlaySession airPlaySession = this;
        Intrinsics.checkNotNullParameter(data, "data");
        long sleepMillis = 0;
        boolean z = false;
        if (!(timeoutMillis >= 0)) {
            throw new IllegalArgumentException("timeoutMillis must not be negative".toString());
        }
        int i = 2;
        LinkedHashMap command = MapsKt.linkedMapOf(TuplesKt.to("type", "iAPSendMessage"), TuplesKt.to("params", MapsKt.linkedMapOf(TuplesKt.to("data", data))));
        if (timeoutMillis == 0) {
            return airPlaySession.sendCommand(command);
        }
        long deadlineNanos = System.nanoTime() + (timeoutMillis * 1000000);
        while (true) {
            synchronized (airPlaySession.eventWriteLock) {
                try {
                    Boolean sent = null;
                    if (airPlaySession.eventSocket != null) {
                        try {
                            if (airPlaySession.eventCipher != null) {
                                sent = Boolean.valueOf(sendCommandLocked$default(airPlaySession, command, null, i, null));
                            }
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    }
                    if (sent != null) {
                        return sent.booleanValue();
                    }
                    if (airPlaySession.closed.get()) {
                        return z;
                    }
                    long remainingNanos = deadlineNanos - System.nanoTime();
                    if (remainingNanos <= sleepMillis) {
                        return z;
                    }
                    long deadlineNanos2 = deadlineNanos;
                    boolean z2 = z;
                    long sleepMillis2 = RangesKt.coerceAtLeast(Math.min(25L, ((remainingNanos + 1000000) - 1) / 1000000), 1L);
                    try {
                        Thread.sleep(sleepMillis2);
                        airPlaySession = this;
                        z = z2;
                        deadlineNanos = deadlineNanos2;
                        i = 2;
                        sleepMillis = 0;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return z2;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
        }
    }

    public final boolean setNightMode(boolean night) {
        boolean zSendPendingNightModeLocked;
        synchronized (this.eventWriteLock) {
            this.pendingNightMode = Boolean.valueOf(night);
            zSendPendingNightModeLocked = sendPendingNightModeLocked();
        }
        return zSendPendingNightModeLocked;
    }

    private final boolean sendPendingNightModeLocked() {
        Boolean bool = this.pendingNightMode;
        if (bool == null) {
            return true;
        }
        boolean night = bool.booleanValue();
        boolean sent = sendCommandLocked$default(this, MapsKt.linkedMapOf(TuplesKt.to("type", "setNightMode"), TuplesKt.to("params", MapsKt.linkedMapOf(TuplesKt.to("nightMode", Boolean.valueOf(night))))), null, 2, null);
        if (sent) {
            this.pendingNightMode = null;
        }
        return sent;
    }

    private final boolean sendHidReport(int uid, byte[] report) {
        String string = Integer.toString(uid, CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return sendCommand(MapsKt.linkedMapOf(TuplesKt.to("type", "hidSendReport"), TuplesKt.to("uuid", string), TuplesKt.to("hidReport", report)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:115:0x03e6 A[Catch: all -> 0x042a, TryCatch #3 {all -> 0x042a, blocks: (B:113:0x03e0, B:115:0x03e6, B:116:0x03ee, B:118:0x040a), top: B:128:0x03e0 }] */
    /* JADX WARN: Code duplicated, block: B:118:0x040a A[Catch: all -> 0x042a, TRY_LEAVE, TryCatch #3 {all -> 0x042a, blocks: (B:113:0x03e0, B:115:0x03e6, B:116:0x03ee, B:118:0x040a), top: B:128:0x03e0 }] */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    public final void runControl() throws Throwable {
        StringBuilder sb;
        LinkedHashSet<Integer> linkedHashSet;
        String message;
        ControlCipher activeCipher;
        RtspMessage.Response response;
        byte[] bArrEncrypt;
        String str = " body=";
        String str2 = " ";
        TcpLiveness.INSTANCE.configure(this.socket, new Function1() { // from class: com.carplay.airplay.AirPlaySession$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AirPlaySession.runControl$lambda$21(this.f$0, (String) obj);
            }
        });
        BufferedInputStream input = new BufferedInputStream(this.socket.getInputStream());
        BufferedOutputStream output = new BufferedOutputStream(this.socket.getOutputStream());
        byte[] accumulated = new byte[0];
        byte[] buffer = new byte[16384];
        String closeReason = "session closed";
        byte[] accumulated2 = accumulated;
        while (!this.closed.get()) {
            try {
                int count = input.read(buffer);
                if (count < 0) {
                    closeReason = "peer EOF";
                    break;
                }
                int count2 = count;
                byte[] bArrCopyOf = Arrays.copyOf(buffer, count2);
                Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
                byte[] plaintext = bArrCopyOf;
                ControlCipher activeCipher2 = this.cipher;
                if (activeCipher2 != null) {
                    try {
                        try {
                            this.encBuf = ArraysKt.plus(this.encBuf, plaintext);
                            try {
                                activeCipher = activeCipher2;
                                try {
                                    try {
                                        ControlCipher.Decrypted decrypted = activeCipher.decrypt(this.encBuf);
                                        try {
                                            this.encBuf = decrypted.getRest();
                                            plaintext = decrypted.getData();
                                        } catch (Exception e) {
                                            error = e;
                                            try {
                                                message = error.getMessage();
                                                if (message == null) {
                                                    message = error.getClass().getSimpleName();
                                                }
                                                closeReason = "control I/O failed: " + message;
                                                if (!this.closed.get()) {
                                                    Log.e("xcertplay-usb", "airplay " + closeReason, error);
                                                }
                                                linkedHashSet = this.activeStreams;
                                                sb = new StringBuilder();
                                                debugLog$default(this, sb.append("airplay control closing reason=").append(closeReason).append(" activeStreams=").append(linkedHashSet).toString(), false, 2, null);
                                                close();
                                            } catch (Throwable th) {
                                                th = th;
                                                debugLog$default(this, "airplay control closing reason=" + closeReason + " activeStreams=" + this.activeStreams, false, 2, null);
                                                close();
                                                throw th;
                                            }
                                        }
                                    } catch (Exception e2) {
                                        error = e2;
                                        String message2 = error.getMessage();
                                        if (message2 == null) {
                                            message2 = error.getClass().getSimpleName();
                                        }
                                        closeReason = "control decrypt failed: " + message2;
                                        Log.e("xcertplay-usb", "airplay " + closeReason + " encrypted=" + this.encBuf.length, error);
                                        linkedHashSet = this.activeStreams;
                                        sb = new StringBuilder();
                                        debugLog$default(this, sb.append("airplay control closing reason=").append(closeReason).append(" activeStreams=").append(linkedHashSet).toString(), false, 2, null);
                                        close();
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    debugLog$default(this, "airplay control closing reason=" + closeReason + " activeStreams=" + this.activeStreams, false, 2, null);
                                    close();
                                    throw th;
                                }
                            } catch (Exception e3) {
                                error = e3;
                            }
                        } catch (Exception e4) {
                            error = e4;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } else {
                    activeCipher = activeCipher2;
                }
                try {
                    RtspMessage.Parsed parsed = RtspMessage.INSTANCE.parseMessages(ArraysKt.plus(accumulated2, plaintext));
                    byte[] accumulated3 = parsed.getRest();
                    try {
                        for (RtspMessage.Request request : parsed.getMessages()) {
                            activeCipher = activeCipher;
                            String str3 = request.getHeaders().get("cseq");
                            if (str3 == null) {
                                str3 = "-";
                            }
                            String cseq = str3;
                            count2 = count2;
                            String path = request.getPath().toLowerCase(Locale.ROOT);
                            Intrinsics.checkNotNullExpressionValue(path, "toLowerCase(...)");
                            buffer = buffer;
                            closeReason = closeReason;
                            accumulated3 = accumulated3;
                            try {
                                try {
                                    boolean showInDebugOverlay = (StringsKt.endsWith$default(path, "/feedback", false, 2, (Object) null) || (Intrinsics.areEqual(request.getMethod(), "POST") && StringsKt.endsWith$default(path, "/command", false, 2, (Object) null))) ? false : true;
                                    plaintext = plaintext;
                                    debugLog("airplay rx " + request.getMethod() + str2 + request.getPath() + " cseq=" + cseq + str + request.getBody().length, showInDebugOverlay);
                                    if (showInDebugOverlay) {
                                        debugLog(AirPlayControlDiagnostics.INSTANCE.request(request.getMethod(), request.getPath(), request.getBody().length), false);
                                    }
                                    trace("airplay control rx headers=" + request.getHeaders() + " bodyHex=" + AirPlaySessionKt.toHex(request.getBody()));
                                    try {
                                        response = handle(request);
                                    } catch (Exception error) {
                                        Log.e("xcertplay-usb", "airplay handler failed " + request.getMethod() + str2 + request.getPath() + " cseq=" + cseq, error);
                                        response = new RtspMessage.Response(null, 500, null, null, null, 29, null);
                                    }
                                    Integer status = response.getStatus();
                                    str2 = str2;
                                    debugLog("airplay tx status=" + (status != null ? status.intValue() : 200) + " cseq=" + cseq + str + response.getBody().length, showInDebugOverlay);
                                    if (showInDebugOverlay) {
                                        Integer status2 = response.getStatus();
                                        debugLog("airplay control response status=" + (status2 != null ? status2.intValue() : 200) + " contentBytes=" + response.getBody().length, false);
                                    }
                                    byte[] wire = RtspMessage.INSTANCE.buildResponse(request, response);
                                    trace("airplay control tx wireHex=" + AirPlaySessionKt.toHex(wire));
                                    ControlCipher controlCipher = this.cipher;
                                    if (controlCipher == null || (bArrEncrypt = controlCipher.encrypt(wire)) == null) {
                                        bArrEncrypt = wire;
                                    }
                                    output.write(bArrEncrypt);
                                    if (this.cipher == null && this.pairVerify.getKeys() != null) {
                                        PairVerify.ControlKeys keys = this.pairVerify.getKeys();
                                        Intrinsics.checkNotNull(keys);
                                        this.cipher = new ControlCipher(keys.getReadKey(), keys.getWriteKey());
                                        debugLog$default(this, "airplay control encryption enabled", false, 2, null);
                                        str = str;
                                    }
                                } catch (Throwable th4) {
                                    th = th4;
                                    closeReason = closeReason;
                                    debugLog$default(this, "airplay control closing reason=" + closeReason + " activeStreams=" + this.activeStreams, false, 2, null);
                                    close();
                                    throw th;
                                }
                            } catch (Exception e5) {
                                error = e5;
                                closeReason = closeReason;
                                message = error.getMessage();
                                if (message == null) {
                                    message = error.getClass().getSimpleName();
                                }
                                closeReason = "control I/O failed: " + message;
                                if (!this.closed.get()) {
                                    Log.e("xcertplay-usb", "airplay " + closeReason, error);
                                }
                                linkedHashSet = this.activeStreams;
                                sb = new StringBuilder();
                                debugLog$default(this, sb.append("airplay control closing reason=").append(closeReason).append(" activeStreams=").append(linkedHashSet).toString(), false, 2, null);
                                close();
                            }
                        }
                        String str4 = str;
                        String str5 = str2;
                        byte[] buffer2 = buffer;
                        String closeReason2 = closeReason;
                        byte[] accumulated4 = accumulated3;
                        output.flush();
                        notifySetupResponseSent();
                        input = input;
                        buffer = buffer2;
                        closeReason = closeReason2;
                        accumulated2 = accumulated4;
                        str2 = str5;
                        str = str4;
                    } catch (Exception e6) {
                        error = e6;
                    } catch (Throwable th5) {
                        th = th5;
                    }
                } catch (Exception e7) {
                    error = e7;
                } catch (Throwable th6) {
                    th = th6;
                }
            } catch (Exception e8) {
                error = e8;
            } catch (Throwable th7) {
                th = th7;
            }
        }
        linkedHashSet = this.activeStreams;
        sb = new StringBuilder();
        debugLog$default(this, sb.append("airplay control closing reason=").append(closeReason).append(" activeStreams=").append(linkedHashSet).toString(), false, 2, null);
        close();
    }

    static final Unit runControl$lambda$21(AirPlaySession this$0, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        debugLog$default(this$0, it, false, 2, null);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    private final RtspMessage.Response handle(RtspMessage.Request request) {
        String requestInfo;
        Object objM159constructorimpl;
        Map<String, ? extends Object> mapEmptyMap;
        String method = request.getMethod();
        int i = 200;
        switch (method.hashCode()) {
            case -1881579439:
                if (method.equals("RECORD")) {
                    this.listener.onSessionActive(this);
                    return new RtspMessage.Response(null, i, null, null, null, 29, null);
                }
                break;
            case -880847356:
                if (method.equals("TEARDOWN")) {
                    return handleTeardown(request);
                }
                break;
            case 78791261:
                if (method.equals("SETUP")) {
                    return handleSetup(request);
                }
                break;
        }
        if (AirPlayConfigKt.getBufferedAudioOutputEnabled(this.config) && BUFFERED_AUDIO_METHODS.contains(request.getMethod())) {
            if ((request.getBody().length == 0) && Intrinsics.areEqual(request.getMethod(), "GETANCHOR")) {
                mapEmptyMap = MapsKt.emptyMap();
            } else {
                try {
                    Result.Companion companion = Result.INSTANCE;
                    AirPlaySession airPlaySession = this;
                    objM159constructorimpl = Result.m159constructorimpl(AirPlaySessionKt.asMap(BplistCodec.INSTANCE.decode(request.getBody())));
                } catch (Throwable th) {
                    Result.Companion companion2 = Result.INSTANCE;
                    objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
                }
                if (Result.m165isFailureimpl(objM159constructorimpl)) {
                    objM159constructorimpl = null;
                }
                mapEmptyMap = (Map) objM159constructorimpl;
                if (mapEmptyMap == null) {
                    return new RtspMessage.Response(null, Integer.valueOf(AirPlayDisplaySettings.MAX_WIDTH_PHYSICAL_MM), null, null, null, 29, null);
                }
            }
            Map<String, Object> mapOnBufferedAudioControl = this.media.onBufferedAudioControl(this, request.getMethod(), mapEmptyMap);
            if (!Intrinsics.areEqual(request.getMethod(), "GETANCHOR")) {
                debugLog$default(this, "airplay " + request.getMethod() + " " + mapEmptyMap + " -> " + (mapOnBufferedAudioControl == null ? "ok" : mapOnBufferedAudioControl), false, 2, null);
            }
            if (mapOnBufferedAudioControl == null) {
                return new RtspMessage.Response(null, i, null, null, null, 29, null);
            }
            return new RtspMessage.Response(null, null, null, MapsKt.mapOf(TuplesKt.to("Content-Type", PLIST_CONTENT_TYPE)), BplistCodec.INSTANCE.encode(mapOnBufferedAudioControl), 7, null);
        }
        String path = request.getPath().toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(path, "toLowerCase(...)");
        if (StringsKt.endsWith$default(path, "/pair-setup", false, 2, (Object) null)) {
            return new RtspMessage.Response(null, null, null, MapsKt.mapOf(TuplesKt.to("Content-Type", PAIRING_CONTENT_TYPE)), this.pairSetup.handle(request.getBody()), 7, null);
        }
        if (StringsKt.endsWith$default(path, "/pair-verify", false, 2, (Object) null)) {
            return new RtspMessage.Response(null, null, null, MapsKt.mapOf(TuplesKt.to("Content-Type", PAIRING_CONTENT_TYPE)), this.pairVerify.handle(request.getBody()), 7, null);
        }
        if (StringsKt.endsWith$default(path, "/auth-setup", false, 2, (Object) null)) {
            MfiAuthenticator it = this.mfi;
            byte[] body = it != null ? MfiSapAuthSetup.INSTANCE.handle(request.getBody(), it) : null;
            if (body == null) {
                return new RtspMessage.Response(null, Integer.valueOf(AirPlayDisplaySettings.MAX_WIDTH_PHYSICAL_MM), null, null, null, 29, null);
            }
            return new RtspMessage.Response(null, null, null, MapsKt.mapOf(TuplesKt.to("Content-Type", OCTET_CONTENT_TYPE)), body, 7, null);
        }
        if (!StringsKt.endsWith$default(path, "/info", false, 2, (Object) null)) {
            if (Intrinsics.areEqual(request.getMethod(), "POST") && StringsKt.endsWith$default(path, "/command", false, 2, (Object) null)) {
                return handleCommand(request);
            }
            if (!Intrinsics.areEqual(request.getMethod(), "POST") || !StringsKt.endsWith$default(path, "/feedback", false, 2, (Object) null)) {
                return new RtspMessage.Response(null, i, null, null, null, 29, null);
            }
            Map<String, Object> mapOnFeedback = this.media.onFeedback(this);
            if (mapOnFeedback == null) {
                return new RtspMessage.Response(null, i, null, null, null, 29, null);
            }
            return new RtspMessage.Response(null, null, null, MapsKt.mapOf(TuplesKt.to("Content-Type", PLIST_CONTENT_TYPE)), BplistCodec.INSTANCE.encode(mapOnFeedback), 7, null);
        }
        Map<String, Object> mapBuild = AirPlayInfoPlist.INSTANCE.build(this.config);
        if (!(request.getBody().length == 0)) {
            try {
                requestInfo = String.valueOf(BplistCodec.INSTANCE.decode(request.getBody()));
            } catch (Exception e) {
                requestInfo = "<unparseable " + request.getBody().length + " bytes>";
            }
            debugLog$default(this, "airplay /info request=" + requestInfo, false, 2, null);
        }
        Object obj = mapBuild.get("features");
        Object obj2 = mapBuild.get("audioFormats");
        List list = obj2 instanceof List ? (List) obj2 : null;
        int size = list != null ? list.size() : 0;
        Object obj3 = mapBuild.get("audioLatencies");
        List list2 = obj3 instanceof List ? (List) obj3 : null;
        Log.i("xcertplay-usb", "airplay /info features=" + obj + " audioFormats=" + size + " audioLatencies=" + (list2 != null ? list2.size() : 0));
        debugLog$default(this, "airplay /info videoInCar=" + this.config.getVideoInCar() + " videoPlaybackAllowed=" + (this.config.getVideoInCar() ? Boolean.valueOf(VideoInCar.INSTANCE.getAllowed()) : "not-offered"), false, 2, null);
        debugLog$default(this, "airplay /info displays=" + mapBuild.get("displays"), false, 2, null);
        return new RtspMessage.Response(null, null, null, MapsKt.mapOf(TuplesKt.to("Content-Type", PLIST_CONTENT_TYPE)), BplistCodec.INSTANCE.encode(mapBuild), 7, null);
    }

    private final void notifySetupResponseSent() {
        try {
            this.media.onSetupResponseSent(this);
        } catch (Exception error) {
            Log.w("xcertplay-usb", "airplay SETUP response callback failed", error);
        }
        reapplyClusterContent();
    }

    static /* synthetic */ void debugLog$default(AirPlaySession airPlaySession, String str, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = true;
        }
        airPlaySession.debugLog(str, z);
    }

    private final void debugLog(String message, boolean uiVisible) {
        Log.i("xcertplay-usb", message);
        if (uiVisible) {
            try {
                this.listener.onDebugLog(message);
            } catch (Exception error) {
                Log.w("xcertplay-usb", "debug log callback failed", error);
            }
        }
    }

    private final void trace(String message) {
        try {
            this.listener.onDebugLog("TRACE " + message);
        } catch (Exception error) {
            Log.w("xcertplay-usb", "trace log callback failed", error);
        }
    }

    /* JADX WARN: Code duplicated, block: B:36:0x0143  */
    private final RtspMessage.Response handleSetup(RtspMessage.Request request) {
        try {
            Map dict = AirPlaySessionKt.asMap(BplistCodec.INSTANCE.decode(request.getBody()));
            if (dict != null) {
                debugLog$default(this, "airplay SETUP keys=" + CollectionsKt.sorted(dict.keySet()), false, 2, null);
                Object obj = dict.get("streams");
                List<?> list = obj instanceof List ? (List) obj : null;
                if (list == null) {
                    String name = AirPlaySessionKt.string(dict.get("name"));
                    String deviceId = AirPlaySessionKt.string(dict.get("deviceID"));
                    String wifiMac = AirPlaySessionKt.string(dict.get("macAddress")).toLowerCase(Locale.ROOT);
                    Intrinsics.checkNotNullExpressionValue(wifiMac, "toLowerCase(...)");
                    String model = AirPlaySessionKt.string(dict.get("model"));
                    debugLog$default(this, AirPlayPeerDiagnostics.INSTANCE.summary(model, AirPlaySessionKt.string(dict.get("osVersion")), AirPlaySessionKt.string(dict.get("sourceVersion"))), false, 2, null);
                    if (deviceId.length() > 0) {
                        this.deviceBtMac = deviceId;
                    }
                    if (name.length() > 0) {
                        this.listener.onDeviceInfo(this, new AirPlayDeviceInfo(name, deviceId, wifiMac, model));
                    } else {
                        if (deviceId.length() > 0) {
                            this.listener.onDeviceInfo(this, new AirPlayDeviceInfo(name, deviceId, wifiMac, model));
                        } else {
                            if (wifiMac.length() > 0) {
                                this.listener.onDeviceInfo(this, new AirPlayDeviceInfo(name, deviceId, wifiMac, model));
                            }
                        }
                    }
                    Long lM49long = AirPlaySessionKt.m49long(dict.get("timingPort"));
                    int peerTimingPort = lM49long != null ? (int) lM49long.longValue() : 0;
                    LinkedHashMap response = MapsKt.linkedMapOf(TuplesKt.to("timingPort", Integer.valueOf(openTiming(peerTimingPort))), TuplesKt.to("eventPort", Integer.valueOf(openEvent())));
                    if (Intrinsics.areEqual(dict.get("keepAliveLowPower"), (Object) true) || Intrinsics.areEqual(dict.get("keepAliveLowPower"), (Object) 1L)) {
                        response.put("keepAlivePort", Integer.valueOf(openKeepAlive()));
                    }
                    AirPlayConfig airPlayConfig = this.config;
                    Object obj2 = dict.get("features");
                    List<String> list2 = AirPlaySessionKt.setupEnabledFeatures(airPlayConfig, obj2 instanceof List ? (List) obj2 : null);
                    boolean videoPlaybackEnabled = list2.contains(VideoInCar.FEATURE);
                    VideoPlaybackDelivery videoDelivery = this.videoPlaybackAvailability.setFeatureEnabled(videoPlaybackEnabled);
                    debugLog$default(this, "airplay video playback negotiated=" + videoPlaybackEnabled + " availability=" + videoDelivery, false, 2, null);
                    response.put("enabledFeatures", list2);
                    return new RtspMessage.Response(null, null, null, MapsKt.mapOf(TuplesKt.to("Content-Type", PLIST_CONTENT_TYPE)), BplistCodec.INSTANCE.encode(response), 7, null);
                }
                List<Object> listHandleStreams = handleStreams(list);
                debugLog$default(this, "airplay SETUP response streams=" + listHandleStreams, false, 2, null);
                byte[] body = BplistCodec.INSTANCE.encode(MapsKt.linkedMapOf(TuplesKt.to("streams", listHandleStreams)));
                trace("airplay SETUP response bplistHex=" + AirPlaySessionKt.toHex(body));
                return new RtspMessage.Response(null, null, null, MapsKt.mapOf(TuplesKt.to("Content-Type", PLIST_CONTENT_TYPE)), body, 7, null);
            }
            return new RtspMessage.Response(null, Integer.valueOf(AirPlayDisplaySettings.MAX_WIDTH_PHYSICAL_MM), null, null, null, 29, null);
        } catch (Exception error) {
            Log.e("xcertplay-usb", "airplay SETUP plist decode failed body=" + request.getBody().length, error);
            return new RtspMessage.Response(null, Integer.valueOf(AirPlayDisplaySettings.MAX_WIDTH_PHYSICAL_MM), null, null, null, 29, null);
        }
    }

    private final List<Object> handleStreams(List<?> streams) {
        Long lM49long;
        Object obj;
        Object obj2;
        Object obj3;
        Object obj4;
        ArrayList result = new ArrayList();
        for (Object entry : streams) {
            Map<String, ? extends Object> mapAsMap = AirPlaySessionKt.asMap(entry);
            if (mapAsMap != null && (lM49long = AirPlaySessionKt.m49long(mapAsMap.get("type"))) != null) {
                int type = (int) lM49long.longValue();
                debugLog$default(this, "airplay SETUP stream type=" + type + " payload=" + mapAsMap, false, 2, null);
                Object obj5 = "none";
                switch (type) {
                    case 100:
                    case STREAM_TYPE_ALT_AUDIO /* 101 */:
                    case 102:
                    case STREAM_TYPE_AUX_AUDIO /* 106 */:
                        Map<String, Object> mapOnAudio = this.media.onAudio(this, type, mapAsMap);
                        boolean z = mapOnAudio != null;
                        if (mapOnAudio == null || (obj = mapOnAudio.get("dataPort")) == null) {
                            obj = "none";
                        }
                        if (mapOnAudio != null && (obj2 = mapOnAudio.get("controlPort")) != null) {
                            obj5 = obj2;
                        }
                        debugLog$default(this, "airplay audio stream type=" + type + " accepted=" + z + " dataPort=" + obj + " controlPort=" + obj5, false, 2, null);
                        if (mapOnAudio != null) {
                            this.activeStreams.add(Integer.valueOf(type));
                            result.add(mapOnAudio);
                        }
                        break;
                    case 103:
                        Map<String, Object> mapOnBufferedAudio = AirPlayConfigKt.getBufferedAudioOutputEnabled(this.config) ? this.media.onBufferedAudio(this, mapAsMap) : null;
                        boolean z2 = mapOnBufferedAudio != null;
                        if (mapOnBufferedAudio != null && (obj3 = mapOnBufferedAudio.get("dataPort")) != null) {
                            obj5 = obj3;
                        }
                        debugLog$default(this, "airplay buffered audio stream accepted=" + z2 + " dataPort=" + obj5, false, 2, null);
                        if (mapOnBufferedAudio != null) {
                            this.activeStreams.add(Integer.valueOf(type));
                            result.add(mapOnBufferedAudio);
                        }
                        break;
                    case 110:
                    case 111:
                        Integer port = this.media.onScreen(this, type, mapAsMap);
                        debugLog$default(this, "airplay screen stream type=" + type + " dataPort=" + (port == null ? "rejected" : port), false, 2, null);
                        if (port != null) {
                            this.activeStreams.add(Integer.valueOf(type));
                            if (type == 111) {
                                beginClusterContent();
                            } else {
                                this.mainScreenToken = new Object();
                            }
                            result.add(MapsKt.linkedMapOf(TuplesKt.to("type", Integer.valueOf(type)), TuplesKt.to("dataPort", port)));
                        }
                        break;
                    case 130:
                        Map<String, Object> mapOnDataStream = this.media.onDataStream(this, mapAsMap);
                        boolean z3 = mapOnDataStream != null;
                        if (mapOnDataStream != null && (obj4 = mapOnDataStream.get("dataPort")) != null) {
                            obj5 = obj4;
                        }
                        debugLog$default(this, "airplay data stream type=" + type + " accepted=" + z3 + " dataPort=" + obj5, false, 2, null);
                        if (mapOnDataStream != null) {
                            this.activeStreams.add(Integer.valueOf(type));
                            result.add(mapOnDataStream);
                        }
                        break;
                    default:
                        debugLog$default(this, "airplay unsupported stream type=" + type, false, 2, null);
                        break;
                }
            }
        }
        return result;
    }

    private final RtspMessage.Response handleCommand(RtspMessage.Request request) {
        Map mapEmptyMap;
        Object objM159constructorimpl;
        try {
            mapEmptyMap = AirPlaySessionKt.asMap(BplistCodec.INSTANCE.decode(request.getBody()));
            if (mapEmptyMap == null) {
                mapEmptyMap = MapsKt.emptyMap();
            }
        } catch (Exception e) {
            mapEmptyMap = MapsKt.emptyMap();
        }
        Map body = mapEmptyMap;
        String type = AirPlaySessionKt.string(body.get("type"));
        Map<String, ? extends Object> mapAsMap = AirPlaySessionKt.asMap(body.get("params"));
        if (mapAsMap == null) {
            mapAsMap = MapsKt.emptyMap();
        }
        Map<String, ? extends Object> map = mapAsMap;
        debugLog$default(this, "airplay command type=" + type + " keys=" + CollectionsKt.sorted(map.keySet()), false, 2, null);
        String str = request.getHeaders().get("x-apple-streamid");
        Long streamId = str != null ? StringsKt.toLongOrNull(str) : null;
        Object obj = map.get("data");
        byte[] data = obj instanceof byte[] ? (byte[]) obj : null;
        if (streamId == null || data == null) {
            if (Intrinsics.areEqual(type, "requestUI") && Intrinsics.areEqual(AirPlaySessionKt.string(map.get("url")), VideoInCar.UI_URL)) {
                this.listener.onVideoPlaybackUiRequested(this);
            } else if (Intrinsics.areEqual(type, "requestUI")) {
                this.listener.onHostUiRequested(this);
            }
            this.listener.onCommand(this, type, map);
            return new RtspMessage.Response(null, 200, null, null, null, 29, null);
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            AirPlaySession airPlaySession = this;
            objM159constructorimpl = Result.m159constructorimpl(AirPlaySessionKt.asMap(BplistCodec.INSTANCE.decode(data)));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        Map<String, ? extends Object> map2 = (Map) (Result.m165isFailureimpl(objM159constructorimpl) ? null : objM159constructorimpl);
        if (map2 != null) {
            this.listener.onRemoteControlMessage(this, streamId.longValue(), map2);
        }
        return new RtspMessage.Response(null, 200, null, null, null, 29, null);
    }

    private final RtspMessage.Response handleTeardown(RtspMessage.Request request) {
        Object decodedBody;
        try {
            decodedBody = BplistCodec.INSTANCE.decode(request.getBody());
        } catch (Exception e) {
            decodedBody = null;
        }
        List<Integer> listTeardownStreamTypes = AirPlaySessionKt.teardownStreamTypes(decodedBody);
        debugLog$default(this, "airplay TEARDOWN types=" + (listTeardownStreamTypes == null ? "all" : listTeardownStreamTypes) + " activeBefore=" + this.activeStreams + " body=" + request.getBody().length + " bytes payload=" + decodedBody, false, 2, null);
        trace("airplay TEARDOWN raw" + request.getBody().length + "Hex=" + AirPlaySessionKt.toHex(request.getBody()));
        if (listTeardownStreamTypes == null || listTeardownStreamTypes.contains(110)) {
            this.mainScreenToken = null;
        }
        if (listTeardownStreamTypes == null || listTeardownStreamTypes.contains(111)) {
            clearClusterContent();
        }
        if (listTeardownStreamTypes == null) {
            Iterable $this$forEach$iv = CollectionsKt.toList(this.activeStreams);
            for (Object element$iv : $this$forEach$iv) {
                int it = ((Number) element$iv).intValue();
                this.media.onTeardown(this, it);
            }
            this.activeStreams.clear();
        } else {
            List<Integer> $this$forEach$iv2 = listTeardownStreamTypes;
            for (Object element$iv2 : $this$forEach$iv2) {
                int type = ((Number) element$iv2).intValue();
                if (this.activeStreams.remove(Integer.valueOf(type))) {
                    this.media.onTeardown(this, type);
                }
            }
        }
        if (!this.activeStreams.contains(111)) {
            clearClusterContent();
        }
        return new RtspMessage.Response(null, 200, null, null, null, 29, null);
    }

    private final int openTiming(int peerPort) {
        InetAddress it;
        int port = this.ntp.listen();
        if (peerPort > 0 && (it = this.peerAddress) != null) {
            this.ntp.start(it, peerPort);
        }
        return port;
    }

    private final int openKeepAlive() throws SocketException {
        final DatagramSocket socket = new DatagramSocket((SocketAddress) null);
        socket.setReuseAddress(true);
        socket.bind(new InetSocketAddress(InetAddress.getByName("::"), 0));
        this.keepAliveSocket = socket;
        Thread $this$openKeepAlive_u24lambda_u2430 = new Thread(new Runnable() { // from class: com.carplay.airplay.AirPlaySession$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.runKeepAlive(socket);
            }
        }, "airplay-keepalive");
        $this$openKeepAlive_u24lambda_u2430.setDaemon(true);
        $this$openKeepAlive_u24lambda_u2430.start();
        this.keepAliveThread = $this$openKeepAlive_u24lambda_u2430;
        return socket.getLocalPort();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void runKeepAlive(DatagramSocket socket) {
        byte[] buffer = new byte[512];
        while (!this.closed.get()) {
            try {
                socket.receive(new DatagramPacket(buffer, buffer.length));
            } catch (Exception e) {
                if (this.closed.get()) {
                    return;
                }
            }
        }
    }

    private final int openEvent() {
        final ServerSocket server = new ServerSocket(0, 50, InetAddress.getByName("::"));
        this.eventServer = server;
        spawnEvent("airplay-event-accept", new Function0() { // from class: com.carplay.airplay.AirPlaySession$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AirPlaySession.openEvent$lambda$31(this.f$0, server);
            }
        });
        return server.getLocalPort();
    }

    static final Unit openEvent$lambda$31(AirPlaySession this$0, ServerSocket $server) {
        this$0.acceptEvent($server);
        return Unit.INSTANCE;
    }

    private final void teardown() {
        this.videoPlaybackAvailability.setEventReady(false);
        this.videoPlaybackAvailability.setFeatureEnabled(false);
        this.ntp.close();
        AirPlaySessionKt.safeClose(this.keepAliveSocket);
        this.keepAliveSocket = null;
        Thread thread = this.keepAliveThread;
        if (thread != null) {
            thread.interrupt();
        }
        this.keepAliveThread = null;
        AirPlaySessionKt.safeClose(this.eventServer);
        this.eventServer = null;
        AirPlaySessionKt.safeClose(this.eventSocket);
        this.eventSocket = null;
        this.eventCipher = null;
        Iterable $this$forEach$iv = this.eventThreads;
        for (Object element$iv : $this$forEach$iv) {
            Thread it = (Thread) element$iv;
            it.interrupt();
        }
        this.eventThreads.clear();
    }

    private final void acceptEvent(ServerSocket server) {
        try {
            Socket socket = server.accept();
            socket.setSoLinger(true, 0);
            socket.setTcpNoDelay(true);
            debugLog$default(this, "airplay event connection accepted from " + socket.getRemoteSocketAddress() + " noDelay=" + socket.getTcpNoDelay(), false, 2, null);
            this.eventSocket = socket;
            byte[] shared = this.pairVerify.getSharedSecret();
            if (shared == null) {
                Log.e("xcertplay-usb", "airplay event rejected: pair-verify shared secret unavailable");
                AirPlaySessionKt.safeClose(socket);
                close();
                return;
            }
            byte[] writeKey = AirPlayCrypto.INSTANCE.hkdfSha512(shared, BytesKt.asciiBytes("Events-Salt"), BytesKt.asciiBytes("Events-Write-Encryption-Key"), 32);
            byte[] readKey = AirPlayCrypto.INSTANCE.hkdfSha512(shared, BytesKt.asciiBytes("Events-Salt"), BytesKt.asciiBytes("Events-Read-Encryption-Key"), 32);
            this.eventCipher = new ControlCipher(readKey, writeKey);
            synchronized (this.eventWriteLock) {
                sendPendingNightModeLocked();
            }
            VideoPlaybackDelivery videoDelivery = this.videoPlaybackAvailability.setEventReady(true);
            debugLog$default(this, "airplay video event ready availability=" + videoDelivery, false, 2, null);
            reapplyClusterContent();
            Intrinsics.checkNotNull(socket);
            runEventRead(socket);
        } catch (Exception error) {
            if (!this.closed.get()) {
                Log.e("xcertplay-usb", "airplay event accept failed", error);
                close();
            }
        }
    }

    private final void runEventRead(Socket socket) {
        int count;
        ControlCipher cipher;
        Object obj = null;
        try {
            try {
                BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream());
                byte[] encrypted = new byte[0];
                byte[] plaintext = new byte[0];
                byte[] buffer = new byte[16384];
                while (!this.closed.get() && (count = input.read(buffer)) >= 0 && (cipher = this.eventCipher) != null) {
                    byte[] bArrCopyOf = Arrays.copyOf(buffer, count);
                    Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
                    byte[] encrypted2 = ArraysKt.plus(encrypted, bArrCopyOf);
                    try {
                        ControlCipher.Decrypted decrypted = cipher.decrypt(encrypted2);
                        byte[] encrypted3 = decrypted.getRest();
                        RtspMessage.Parsed parsed = RtspMessage.INSTANCE.parseMessages(ArraysKt.plus(plaintext, decrypted.getData()));
                        byte[] plaintext2 = parsed.getRest();
                        Iterator<RtspMessage.Request> it = parsed.getMessages().iterator();
                        while (it.hasNext()) {
                            RtspMessage.Request message = it.next();
                            input = input;
                            parsed = parsed;
                            if (StringsKt.startsWith$default(message.getMethod(), "RTSP/", false, 2, obj)) {
                                obj = null;
                            } else if (!StringsKt.startsWith$default(message.getMethod(), "HTTP/", false, 2, obj)) {
                                String method = message.getMethod();
                                String path = message.getPath();
                                String str = message.getHeaders().get("cseq");
                                if (str == null) {
                                    str = "-";
                                }
                                Iterator<RtspMessage.Request> it2 = it;
                                debugLog$default(this, "airplay event rx " + method + " " + path + " cseq=" + str + " body=" + message.getBody().length, false, 2, null);
                                byte[] response = RtspMessage.INSTANCE.buildResponse(message, new RtspMessage.Response(null, 200, null, null, null, 29, null));
                                trace("airplay event rx headers=" + message.getHeaders() + " bodyHex=" + AirPlaySessionKt.toHex(message.getBody()));
                                trace("airplay event tx wireHex=" + AirPlaySessionKt.toHex(response));
                                synchronized (this.eventWriteLock) {
                                    output.write(cipher.encrypt(response));
                                    output.flush();
                                    Unit unit = Unit.INSTANCE;
                                }
                                it = it2;
                                obj = null;
                            }
                        }
                        encrypted = encrypted3;
                        plaintext = plaintext2;
                        obj = null;
                    } catch (Exception error) {
                        Log.e("xcertplay-usb", "airplay event decrypt failed encrypted=" + encrypted2.length, error);
                    }
                }
                debugLog$default(this, "airplay event connection closed", false, 2, null);
                this.videoPlaybackAvailability.setEventReady(false);
                if (this.eventSocket == socket) {
                    this.eventSocket = null;
                }
                this.eventCipher = null;
                AirPlaySessionKt.safeClose(socket);
                if (this.closed.get()) {
                    return;
                }
            } catch (Throwable th) {
                debugLog$default(this, "airplay event connection closed", false, 2, null);
                this.videoPlaybackAvailability.setEventReady(false);
                if (this.eventSocket == socket) {
                    this.eventSocket = null;
                }
                this.eventCipher = null;
                AirPlaySessionKt.safeClose(socket);
                if (!this.closed.get()) {
                    close();
                }
                throw th;
            }
        } catch (Exception error2) {
            if (!this.closed.get()) {
                Log.e("xcertplay-usb", "airplay event read failed", error2);
            }
            debugLog$default(this, "airplay event connection closed", false, 2, null);
            this.videoPlaybackAvailability.setEventReady(false);
            if (this.eventSocket == socket) {
                this.eventSocket = null;
            }
            this.eventCipher = null;
            AirPlaySessionKt.safeClose(socket);
            if (this.closed.get()) {
                return;
            }
        }
        close();
    }

    private final void spawnEvent(String name, final Function0<Unit> body) {
        Thread thread = new Thread(new Runnable() { // from class: com.carplay.airplay.AirPlaySession$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                body.invoke();
            }
        }, name);
        thread.setDaemon(true);
        this.eventThreads.add(thread);
        thread.start();
    }

    /* JADX INFO: compiled from: AirPlaySession.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\"\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00050\u0013X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\nX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0018X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Lcom/carplay/airplay/AirPlaySession$Companion;", "", "<init>", "()V", "TAG", "", "PLIST_CONTENT_TYPE", "PAIRING_CONTENT_TYPE", "OCTET_CONTENT_TYPE", "STREAM_TYPE_MAIN_SCREEN", "", "STREAM_TYPE_ALT_SCREEN", "STREAM_TYPE_MAIN_AUDIO", "STREAM_TYPE_ALT_AUDIO", "STREAM_TYPE_MAIN_HIGH_AUDIO", "STREAM_TYPE_AUX_AUDIO", "STREAM_TYPE_DATA", "STREAM_TYPE_MAIN_BUFFERED_AUDIO", "BUFFERED_AUDIO_METHODS", "", "READ_CHUNK_BYTES", "ZOOM_DIRECTION_IN", "ZOOM_DIRECTION_OUT", "EVENT_READY_POLL_MILLIS", "", "NANOS_PER_MILLISECOND", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
