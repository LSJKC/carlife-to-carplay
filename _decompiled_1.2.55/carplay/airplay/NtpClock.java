package com.carplay.airplay;

import com.carlink.cast.CastSettings;
import java.io.Closeable;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: NtpClock.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0013\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\u0012\n\u0002\b\n\u0018\u0000 92\u00020\u0001:\u00019B3\u0012\u0012\b\u0002\u0010\u0002\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003\u0012\u0016\b\u0002\u0010\u0005\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b\u0018\u00010\u0006¢\u0006\u0004\b\t\u0010\nB\u001d\b\u0016\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006¢\u0006\u0004\b\t\u0010\u000bJ\u0006\u0010%\u001a\u00020\u001cJ\u0016\u0010&\u001a\u00020\b2\u0006\u0010'\u001a\u00020\u00042\u0006\u0010(\u001a\u00020\u001cJ\u0006\u0010)\u001a\u00020\"J\b\u0010*\u001a\u00020\bH\u0016J\b\u0010+\u001a\u00020\bH\u0002J\b\u0010,\u001a\u00020\bH\u0002J\b\u0010-\u001a\u00020\bH\u0002J\u0018\u0010.\u001a\u00020\b2\u0006\u0010/\u001a\u0002002\u0006\u00101\u001a\u00020\u0016H\u0002J\u0018\u00102\u001a\u00020\b2\u0006\u00103\u001a\u0002002\u0006\u00101\u001a\u00020\u0016H\u0002J\u0010\u00104\u001a\u00020\b2\u0006\u00105\u001a\u000200H\u0002J\u0010\u00106\u001a\u00020\b2\u0006\u00107\u001a\u00020\u001fH\u0002J\n\u00108\u001a\u0004\u0018\u00010\u0007H\u0002R\u0018\u0010\u0002\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0005\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b\u0018\u00010\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001cX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u001fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010!\u001a\u0004\u0018\u00010\"X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006:"}, d2 = {"Lcom/carplay/airplay/NtpClock;", "Ljava/io/Closeable;", "localAddress", "Lkotlin/Function0;", "Ljava/net/InetAddress;", "configureSocket", "Lkotlin/Function1;", "Ljava/net/DatagramSocket;", "", "<init>", "(Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;)V", "(Lkotlin/jvm/functions/Function1;)V", "running", "Ljava/util/concurrent/atomic/AtomicBoolean;", "socketLock", "", "clockLock", "socket", "receiver", "Ljava/lang/Thread;", "sender", CastSettings.KEY_PEER, "Ljava/net/InetSocketAddress;", "clockOffsetNs", "", "delays", "", "delayIndex", "", "pickCount", "pickRtt", "", "pickOffset", "pendingT1", "Ljava/math/BigInteger;", "synced", "", "listen", "start", "peerAddress", "port", "syncedNtp", "close", "runSender", "sendRequest", "runReceiver", "handleMessage", "message", "", "source", "respondToRequest", "request", "handleResponse", "response", "applyOffset", "offsetSec", "currentSocket", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class NtpClock implements Closeable {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int DELAY_WINDOW = 8;

    @Deprecated
    public static final int NTP_ORIGINATE_OFFSET = 8;

    @Deprecated
    public static final int NTP_PACKET_BYTES = 32;

    @Deprecated
    public static final int NTP_RECEIVE_OFFSET = 16;

    @Deprecated
    public static final int NTP_TRANSMIT_OFFSET = 24;

    @Deprecated
    public static final int PICK_COUNT = 2;

    @Deprecated
    public static final int PT_REQUEST = 210;

    @Deprecated
    public static final int PT_RESPONSE = 211;

    @Deprecated
    public static final int RECEIVE_BUFFER_BYTES = 2048;

    @Deprecated
    public static final long REQUEST_INTERVAL_MS = 1000;

    @Deprecated
    public static final double SLEW_GAIN = 0.125d;

    @Deprecated
    public static final double STEP_THRESHOLD_SEC = 0.128d;

    @Deprecated
    public static final double TWO32_DOUBLE = 4.294967296E9d;
    private final Object clockLock;
    private long clockOffsetNs;
    private final Function1<DatagramSocket, Unit> configureSocket;
    private int delayIndex;
    private final double[] delays;
    private final Function0<InetAddress> localAddress;
    private InetSocketAddress peer;
    private BigInteger pendingT1;
    private int pickCount;
    private double pickOffset;
    private double pickRtt;
    private Thread receiver;
    private final AtomicBoolean running;
    private Thread sender;
    private DatagramSocket socket;
    private final Object socketLock;
    private boolean synced;

    /* JADX WARN: Multi-variable type inference failed */
    public NtpClock() {
        this(null, 0 == true ? 1 : 0, 3, 0 == true ? 1 : 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public NtpClock(Function0<? extends InetAddress> function0, Function1<? super DatagramSocket, Unit> function1) {
        this.localAddress = function0;
        this.configureSocket = function1;
        this.running = new AtomicBoolean(false);
        this.socketLock = new Object();
        this.clockLock = new Object();
        this.clockOffsetNs = NtpClockKt.wallClockNtpOffsetNs(System.nanoTime());
        double[] dArr = new double[8];
        for (int i = 0; i < 8; i++) {
            dArr[i] = Double.POSITIVE_INFINITY;
        }
        this.delays = dArr;
        this.pickCount = 2;
        this.pickRtt = Double.POSITIVE_INFINITY;
    }

    public /* synthetic */ NtpClock(Function0 function0, Function1 function1, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : function0, (i & 2) != 0 ? null : function1);
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public NtpClock(Function1<? super DatagramSocket, Unit> configureSocket) {
        this(null, configureSocket);
        Intrinsics.checkNotNullParameter(configureSocket, "configureSocket");
    }

    public final int listen() {
        InetAddress byName;
        if (this.running.getAndSet(true)) {
            throw new IllegalStateException("NtpClock is already running".toString());
        }
        DatagramSocket bound = new DatagramSocket((SocketAddress) null);
        try {
            Function1<DatagramSocket, Unit> function1 = this.configureSocket;
            if (function1 != null) {
                function1.invoke(bound);
            }
            bound.setReuseAddress(true);
            Function0<InetAddress> function0 = this.localAddress;
            if (function0 == null || (byName = function0.invoke()) == null) {
                byName = InetAddress.getByName("::");
            }
            bound.bind(new InetSocketAddress(byName, 0));
            synchronized (this.socketLock) {
                this.socket = bound;
                Unit unit = Unit.INSTANCE;
            }
            Thread $this$listen_u24lambda_u242 = new Thread(new Runnable() { // from class: com.carplay.airplay.NtpClock$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() throws IOException {
                    this.f$0.runReceiver();
                }
            }, "airplay-ntp-rx");
            $this$listen_u24lambda_u242.setDaemon(true);
            $this$listen_u24lambda_u242.start();
            this.receiver = $this$listen_u24lambda_u242;
            return bound.getLocalPort();
        } catch (Throwable failure) {
            bound.close();
            synchronized (this.socketLock) {
                if (this.socket == bound) {
                    this.socket = null;
                }
                Unit unit2 = Unit.INSTANCE;
                this.running.set(false);
                throw failure;
            }
        }
    }

    public final void start(InetAddress peerAddress, int port) {
        Intrinsics.checkNotNullParameter(peerAddress, "peerAddress");
        if (!(port > 0)) {
            throw new IllegalArgumentException("timing port must be positive".toString());
        }
        this.peer = new InetSocketAddress(peerAddress, port);
        Thread $this$start_u24lambda_u245 = new Thread(new Runnable() { // from class: com.carplay.airplay.NtpClock$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.runSender();
            }
        }, "airplay-ntp-tx");
        $this$start_u24lambda_u245.setDaemon(true);
        $this$start_u24lambda_u245.start();
        this.sender = $this$start_u24lambda_u245;
    }

    public final BigInteger syncedNtp() {
        BigInteger bigIntegerNtpFromNanos;
        synchronized (this.clockLock) {
            bigIntegerNtpFromNanos = NtpClockKt.ntpFromNanos(System.nanoTime() + this.clockOffsetNs);
        }
        return bigIntegerNtpFromNanos;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.running.set(false);
        synchronized (this.socketLock) {
            DatagramSocket datagramSocket = this.socket;
            if (datagramSocket != null) {
                datagramSocket.close();
            }
            this.socket = null;
            Unit unit = Unit.INSTANCE;
        }
        Thread thread = this.sender;
        if (thread != null) {
            thread.interrupt();
        }
        Thread thread2 = this.receiver;
        if (thread2 != null) {
            thread2.interrupt();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void runSender() {
        while (this.running.get()) {
            try {
                sendRequest();
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                return;
            } catch (Exception e2) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e3) {
                    return;
                }
            }
        }
    }

    private final void sendRequest() throws IOException {
        InetSocketAddress destination = this.peer;
        if (destination == null) {
            return;
        }
        byte[] packet = new byte[32];
        packet[0] = ByteCompanionObject.MIN_VALUE;
        packet[1] = -46;
        NtpClockKt.writeU16Be(packet, 2, 7);
        BigInteger t1 = syncedNtp();
        synchronized (this.clockLock) {
            this.pendingT1 = t1;
            Unit unit = Unit.INSTANCE;
        }
        ArraysKt.copyInto$default(NtpClockKt.ntpBytes(t1), packet, 24, 0, 0, 12, (Object) null);
        DatagramSocket datagramSocketCurrentSocket = currentSocket();
        if (datagramSocketCurrentSocket != null) {
            datagramSocketCurrentSocket.send(new DatagramPacket(packet, packet.length, destination));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void runReceiver() throws IOException {
        DatagramSocket sock;
        byte[] buffer = new byte[2048];
        while (this.running.get() && (sock = currentSocket()) != null) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                sock.receive(packet);
                byte[] data = packet.getData();
                Intrinsics.checkNotNullExpressionValue(data, "getData(...)");
                byte[] message = Arrays.copyOf(data, packet.getLength());
                Intrinsics.checkNotNullExpressionValue(message, "copyOf(...)");
                InetAddress address = packet.getAddress();
                if (message.length >= 32 && address != null) {
                    handleMessage(message, new InetSocketAddress(address, packet.getPort()));
                }
            } catch (Exception e) {
                if (!this.running.get()) {
                    return;
                }
            }
        }
    }

    private final void handleMessage(byte[] message, InetSocketAddress source) throws IOException {
        switch (message[1] & UByte.MAX_VALUE) {
            case PT_REQUEST /* 210 */:
                respondToRequest(message, source);
                break;
            case 211:
                handleResponse(message);
                break;
        }
    }

    private final void respondToRequest(byte[] request, InetSocketAddress source) throws IOException {
        byte[] response = new byte[32];
        response[0] = ByteCompanionObject.MIN_VALUE;
        response[1] = -45;
        NtpClockKt.writeU16Be(response, 2, 7);
        ArraysKt.copyInto(request, response, 8, 24, 32);
        ArraysKt.copyInto$default(NtpClockKt.ntpBytes(syncedNtp()), response, 16, 0, 0, 12, (Object) null);
        ArraysKt.copyInto$default(NtpClockKt.ntpBytes(syncedNtp()), response, 24, 0, 0, 12, (Object) null);
        DatagramSocket datagramSocketCurrentSocket = currentSocket();
        if (datagramSocketCurrentSocket != null) {
            datagramSocketCurrentSocket.send(new DatagramPacket(response, response.length, source));
        }
    }

    private final void handleResponse(byte[] response) {
        BigInteger t4 = syncedNtp();
        BigInteger t1 = NtpClockKt.readNtp(response, 8);
        BigInteger t2 = NtpClockKt.readNtp(response, 16);
        BigInteger t3 = NtpClockKt.readNtp(response, 24);
        synchronized (this.clockLock) {
            BigInteger pending = this.pendingT1;
            if (pending == null || !Intrinsics.areEqual(pending, t1)) {
                return;
            }
            this.pendingT1 = null;
            Unit unit = Unit.INSTANCE;
            double offset = (t2.subtract(t1).add(t3.subtract(t4)).doubleValue() * 0.5d) / 4.294967296E9d;
            double rtt = t4.subtract(t1).subtract(t3.subtract(t2)).doubleValue() / 4.294967296E9d;
            if (rtt < 0.0d) {
                return;
            }
            if (rtt < this.pickRtt) {
                this.pickRtt = rtt;
                this.pickOffset = offset;
            }
            this.pickCount--;
            if (this.pickCount > 0) {
                return;
            }
            double selectedRtt = this.pickRtt;
            double selectedOffset = this.pickOffset;
            this.pickCount = 2;
            this.pickRtt = Double.POSITIVE_INFINITY;
            synchronized (this.clockLock) {
                double[] $this$all$iv = this.delays;
                int length = $this$all$iv.length;
                boolean useSample = false;
                int i = 0;
                while (true) {
                    if (i < length) {
                        double element$iv = $this$all$iv[i];
                        if (!(selectedRtt <= element$iv)) {
                            break;
                        } else {
                            i++;
                        }
                    } else {
                        useSample = true;
                        break;
                    }
                }
                this.delays[this.delayIndex] = selectedRtt;
                this.delayIndex = (this.delayIndex + 1) % 8;
                if (useSample) {
                    applyOffset(selectedOffset);
                }
                Unit unit2 = Unit.INSTANCE;
            }
        }
    }

    private final void applyOffset(double offsetSec) {
        boolean stepping = !this.synced || Math.abs(offsetSec) > 0.128d;
        double applied = stepping ? offsetSec : 0.125d * offsetSec;
        this.clockOffsetNs += Math.round(1.0E9d * applied);
        if (stepping) {
            ArraysKt.fill$default(this.delays, Double.POSITIVE_INFINITY, 0, 0, 6, (Object) null);
            this.delayIndex = 0;
            this.pendingT1 = null;
            this.synced = true;
        }
    }

    private final DatagramSocket currentSocket() {
        DatagramSocket datagramSocket;
        synchronized (this.socketLock) {
            datagramSocket = this.socket;
        }
        return datagramSocket;
    }

    /* JADX INFO: compiled from: NtpClock.kt */
    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0005\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lcom/carplay/airplay/NtpClock$Companion;", "", "<init>", "()V", "PT_REQUEST", "", "PT_RESPONSE", "NTP_PACKET_BYTES", "NTP_ORIGINATE_OFFSET", "NTP_RECEIVE_OFFSET", "NTP_TRANSMIT_OFFSET", "REQUEST_INTERVAL_MS", "", "RECEIVE_BUFFER_BYTES", "STEP_THRESHOLD_SEC", "", "SLEW_GAIN", "DELAY_WINDOW", "PICK_COUNT", "TWO32_DOUBLE", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
