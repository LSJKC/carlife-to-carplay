package com.carplay.iap2.transport;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbRequest;
import android.os.Build;
import java.io.Closeable;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeoutException;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: IphoneUsbHost.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0003\u0018\u0000 -2\u00020\u0001:\u0001-B7\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0014\b\u0002\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\b¢\u0006\u0004\b\u000b\u0010\fJ\u0016\u0010\u001b\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fJ\u0010\u0010 \u001a\u0004\u0018\u00010\u001d2\u0006\u0010\u001e\u001a\u00020!J\b\u0010\"\u001a\u00020\nH\u0016J\b\u0010#\u001a\u00020\nH\u0002J\b\u0010$\u001a\u00020\nH\u0002J\u0010\u0010%\u001a\u00020\n2\u0006\u0010&\u001a\u00020\u0016H\u0002J\u001c\u0010'\u001a\u00020(2\u0006\u0010)\u001a\u00020\t2\n\b\u0002\u0010*\u001a\u0004\u0018\u00010+H\u0002J\u0010\u0010,\u001a\u00020\t2\u0006\u0010\u001e\u001a\u00020!H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006."}, d2 = {"Lcom/carplay/iap2/transport/Iap2UsbSession;", "Ljava/io/Closeable;", "connection", "Landroid/hardware/usb/UsbDeviceConnection;", "outEndpoint", "Landroid/hardware/usb/UsbEndpoint;", "inEndpoint", "onDiagnostic", "Lkotlin/Function1;", "", "", "<init>", "(Landroid/hardware/usb/UsbDeviceConnection;Landroid/hardware/usb/UsbEndpoint;Landroid/hardware/usb/UsbEndpoint;Lkotlin/jvm/functions/Function1;)V", "stateLock", "", "readLock", "writeLock", "closed", "", "failure", "Lcom/carplay/iap2/transport/IphoneUsbException;", "pendingRead", "Landroid/hardware/usb/UsbRequest;", "readQueuePolicy", "Lcom/carplay/iap2/transport/UsbReadQueuePolicy;", "requests", "Lcom/carplay/iap2/transport/UsbRequestQueue;", "write", "data", "", "timeoutMillis", "", "read", "", "close", "checkOpen", "checkOpenLocked", "drainCancelledRead", "request", "failSession", "Lcom/carplay/iap2/transport/IphoneUsbException$DeviceUnavailable;", "message", "cause", "", "requestDiagnostics", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2UsbSession implements Closeable {

    @Deprecated
    public static final long CANCEL_DRAIN_TIMEOUT_MILLIS = 1000;
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int USBMUX_READ_CHUNK_BYTES = 65536;
    private boolean closed;
    private final UsbDeviceConnection connection;
    private IphoneUsbException failure;
    private final UsbEndpoint inEndpoint;
    private final Function1<String, Unit> onDiagnostic;
    private final UsbEndpoint outEndpoint;
    private UsbRequest pendingRead;
    private final Object readLock;
    private final UsbReadQueuePolicy readQueuePolicy;
    private final UsbRequestQueue requests;
    private final Object stateLock;
    private final Object writeLock;

    /* JADX WARN: Multi-variable type inference failed */
    public Iap2UsbSession(UsbDeviceConnection connection, UsbEndpoint outEndpoint, UsbEndpoint inEndpoint, Function1<? super String, Unit> onDiagnostic) {
        Intrinsics.checkNotNullParameter(connection, "connection");
        Intrinsics.checkNotNullParameter(outEndpoint, "outEndpoint");
        Intrinsics.checkNotNullParameter(inEndpoint, "inEndpoint");
        Intrinsics.checkNotNullParameter(onDiagnostic, "onDiagnostic");
        this.connection = connection;
        this.outEndpoint = outEndpoint;
        this.inEndpoint = inEndpoint;
        this.onDiagnostic = onDiagnostic;
        this.stateLock = new Object();
        this.readLock = new Object();
        this.writeLock = new Object();
        this.readQueuePolicy = new UsbReadQueuePolicy();
        this.requests = new UsbRequestQueue(this.connection, "usbmux-read-reaper");
    }

    public /* synthetic */ Iap2UsbSession(UsbDeviceConnection usbDeviceConnection, UsbEndpoint usbEndpoint, UsbEndpoint usbEndpoint2, Function1 function1, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(usbDeviceConnection, usbEndpoint, usbEndpoint2, (i & 8) != 0 ? new Function1() { // from class: com.carplay.iap2.transport.Iap2UsbSession$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Iap2UsbSession._init_$lambda$0((String) obj);
            }
        } : function1);
    }

    static final Unit _init_$lambda$0(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    public final void write(byte[] data, int timeoutMillis) {
        int transferred;
        Intrinsics.checkNotNullParameter(data, "data");
        synchronized (this.writeLock) {
            checkOpen();
            boolean z = true;
            if (!(timeoutMillis > 0)) {
                throw new IllegalArgumentException("timeoutMillis must be positive".toString());
            }
            if (data.length != 0) {
                z = false;
            }
            if (!z && (transferred = this.connection.bulkTransfer(this.outEndpoint, data, data.length, timeoutMillis)) != data.length) {
                throw new IphoneUsbException.DeviceUnavailable("USBMUX write transferred " + transferred + " of " + data.length + " bytes", null, 2, null);
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    public final byte[] read(long timeoutMillis) throws IphoneUsbException {
        Object obj;
        UsbReadQueueResult queueResult;
        byte[] bArr;
        synchronized (this.readLock) {
            checkOpen();
            if (!(timeoutMillis > 0)) {
                throw new IllegalArgumentException("timeoutMillis must be positive".toString());
            }
            final UsbRequest request = new UsbRequest();
            boolean initialized = false;
            try {
                try {
                    try {
                        if (!request.initialize(this.connection, this.inEndpoint)) {
                            throw new IphoneUsbException.DeviceUnavailable("Android could not initialize USBMUX read request (" + requestDiagnostics(timeoutMillis) + ")", null, 2, null);
                        }
                        initialized = true;
                        ByteBuffer buffer = ByteBuffer.allocateDirect(65536);
                        synchronized (this.stateLock) {
                            checkOpenLocked();
                            this.pendingRead = request;
                            UsbReadQueuePolicy usbReadQueuePolicy = this.readQueuePolicy;
                            Intrinsics.checkNotNull(buffer);
                            queueResult = usbReadQueuePolicy.queue(buffer, new Iap2UsbSession$read$1$queueResult$1$1(this), new Function1() { // from class: com.carplay.iap2.transport.Iap2UsbSession$$ExternalSyntheticLambda1
                                @Override // kotlin.jvm.functions.Function1
                                public final Object invoke(Object obj2) {
                                    return Boolean.valueOf(Iap2UsbSession.read$lambda$9$lambda$5$lambda$4(this.f$0, request, (ByteBuffer) obj2));
                                }
                            });
                        }
                        if (!queueResult.getQueued()) {
                            try {
                                String strRequestDiagnostics = requestDiagnostics(timeoutMillis);
                                int firstBytes = queueResult.getFirstBytes();
                                Object fallbackBytes = queueResult.getFallbackBytes();
                                if (fallbackBytes == null) {
                                    fallbackBytes = "not_attempted";
                                }
                                throw new IphoneUsbException.DeviceUnavailable("Android could not queue USBMUX read request (" + strRequestDiagnostics + " firstBytes=" + firstBytes + " fallbackBytes=" + fallbackBytes + ")", null, 2, null);
                            } catch (IphoneUsbException error) {
                                throw error;
                            } catch (RuntimeException e) {
                                error = e;
                                throw failSession("USBMUX read failed", error);
                            } catch (Throwable th) {
                                error = th;
                                obj = this.stateLock;
                                synchronized (obj) {
                                    try {
                                        if (this.pendingRead == request) {
                                            this.pendingRead = null;
                                        }
                                        Unit unit = Unit.INSTANCE;
                                    } catch (Throwable th2) {
                                        th = th2;
                                        throw th;
                                    }
                                }
                                if (initialized) {
                                    request.cancel();
                                }
                                request.close();
                                throw error;
                            }
                        }
                        if (queueResult.getFallbackBytes() != null) {
                            try {
                                Result.Companion companion = Result.INSTANCE;
                                Iap2UsbSession $this$read_u24lambda_u249_u24lambda_u246 = this;
                                try {
                                    $this$read_u24lambda_u249_u24lambda_u246.onDiagnostic.invoke("USBMUX read queue compatibility fallback api=" + Build.VERSION.SDK_INT + " endpoint=" + IphoneUsbHostKt.describeUsbEndpoint($this$read_u24lambda_u249_u24lambda_u246.inEndpoint) + " firstBytes=" + queueResult.getFirstBytes() + " fallbackBytes=" + queueResult.getFallbackBytes());
                                    Result.m159constructorimpl(Unit.INSTANCE);
                                } catch (Throwable th3) {
                                    th = th3;
                                    Result.Companion companion2 = Result.INSTANCE;
                                    Result.m159constructorimpl(ResultKt.createFailure(th));
                                }
                            } catch (Throwable th4) {
                                th = th4;
                            }
                        }
                        try {
                            UsbRequest completed = this.requests.await(timeoutMillis);
                            if (completed == null) {
                                throw failSession$default(this, "Android returned no USBMUX read request", null, 2, null);
                            }
                            if (completed != request) {
                                throw failSession$default(this, "Android completed an unexpected USB request", null, 2, null);
                            }
                            byte[] it = new byte[buffer.position()];
                            buffer.flip();
                            buffer.get(it);
                            synchronized (this.stateLock) {
                                if (this.pendingRead == request) {
                                    this.pendingRead = null;
                                }
                                Unit unit2 = Unit.INSTANCE;
                            }
                            request.cancel();
                            request.close();
                            bArr = it;
                        } catch (TimeoutException e2) {
                            drainCancelledRead(request);
                            obj = this.stateLock;
                            synchronized (obj) {
                                try {
                                    if (this.pendingRead == request) {
                                        this.pendingRead = null;
                                    }
                                    Unit unit3 = Unit.INSTANCE;
                                    request.cancel();
                                    request.close();
                                    bArr = null;
                                } catch (Throwable th5) {
                                    th = th5;
                                    throw th;
                                }
                            }
                        }
                    } catch (IphoneUsbException error2) {
                        throw error2;
                    } catch (RuntimeException e3) {
                        error = e3;
                    }
                } catch (Throwable th6) {
                    error = th6;
                }
            } catch (IphoneUsbException error3) {
                throw error3;
            } catch (RuntimeException e4) {
                error = e4;
            } catch (Throwable th7) {
                error = th7;
            }
        }
        return bArr;
    }

    static final boolean read$lambda$9$lambda$5$lambda$4(Iap2UsbSession this$0, UsbRequest $request, ByteBuffer it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return this$0.requests.queue($request, it);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        synchronized (this.stateLock) {
            if (this.closed) {
                return;
            }
            this.closed = true;
            UsbRequest requestToCancel = this.pendingRead;
            if (requestToCancel != null) {
                requestToCancel.cancel();
            }
            this.requests.close();
            this.connection.close();
        }
    }

    private final void checkOpen() {
        synchronized (this.stateLock) {
            checkOpenLocked();
            Unit unit = Unit.INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void checkOpenLocked() throws IphoneUsbException {
        IphoneUsbException it = this.failure;
        if (it != null) {
            throw it;
        }
        if (this.closed) {
            throw new IphoneUsbException.DeviceUnavailable("USBMUX session is closed", null, 2, null);
        }
    }

    private final void drainCancelledRead(UsbRequest request) throws IphoneUsbException.DeviceUnavailable {
        if (!request.cancel()) {
            throw failSession$default(this, "Android could not cancel timed out USBMUX read request", null, 2, null);
        }
        try {
            UsbRequest completed = this.requests.await(1000L);
            if (completed != request) {
                throw failSession$default(this, "Android did not drain the cancelled USBMUX read request", null, 2, null);
            }
        } catch (TimeoutException e) {
            throw failSession$default(this, "Timed out draining cancelled USBMUX read request", null, 2, null);
        }
    }

    static /* synthetic */ IphoneUsbException.DeviceUnavailable failSession$default(Iap2UsbSession iap2UsbSession, String str, Throwable th, int i, Object obj) {
        if ((i & 2) != 0) {
            th = null;
        }
        return iap2UsbSession.failSession(str, th);
    }

    private final IphoneUsbException.DeviceUnavailable failSession(String message, Throwable cause) {
        IphoneUsbException.DeviceUnavailable error = new IphoneUsbException.DeviceUnavailable(message, cause);
        synchronized (this.stateLock) {
            if (this.failure == null) {
                this.failure = error;
            }
            Unit unit = Unit.INSTANCE;
        }
        return error;
    }

    private final String requestDiagnostics(long timeoutMillis) {
        StringBuilder $this$requestDiagnostics_u24lambda_u2414 = new StringBuilder();
        $this$requestDiagnostics_u24lambda_u2414.append("api=").append(Build.VERSION.SDK_INT);
        $this$requestDiagnostics_u24lambda_u2414.append(" endpoint=").append(IphoneUsbHostKt.describeUsbEndpoint(this.inEndpoint));
        $this$requestDiagnostics_u24lambda_u2414.append(" timeoutMs=").append(timeoutMillis);
        return $this$requestDiagnostics_u24lambda_u2414.toString();
    }

    /* JADX INFO: compiled from: IphoneUsbHost.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lcom/carplay/iap2/transport/Iap2UsbSession$Companion;", "", "<init>", "()V", "USBMUX_READ_CHUNK_BYTES", "", "CANCEL_DRAIN_TIMEOUT_MILLIS", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
