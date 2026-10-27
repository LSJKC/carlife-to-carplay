package com.carplay.iap2.transport;

import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: BluetoothRfcommDuplexStream.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0011\u0018\u0000 C2\u00020\u0001:\u0001CB'\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0014\b\u0002\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005¢\u0006\u0004\b\b\u0010\tB'\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\n\u0012\u0014\b\u0002\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005¢\u0006\u0004\b\b\u0010\u000bJ\u0010\u0010#\u001a\u00020\u00072\u0006\u0010$\u001a\u00020\u0011H\u0016J\u001a\u0010%\u001a\u0004\u0018\u00010\u00112\u0006\u0010&\u001a\u00020\u00132\u0006\u0010'\u001a\u00020\u001bH\u0016J\b\u0010(\u001a\u00020\u0007H\u0016J\b\u0010)\u001a\u00020\u0007H\u0002J\u0012\u0010*\u001a\u0004\u0018\u00010\u00112\u0006\u0010&\u001a\u00020\u0013H\u0002J\u0010\u0010+\u001a\u00020\u00192\u0006\u0010,\u001a\u00020\u0019H\u0002J/\u0010-\u001a\u0002H.\"\b\b\u0000\u0010.*\u00020/2\u0006\u00100\u001a\u0002012\u000e\u00102\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H.03H\u0002¢\u0006\u0002\u00104J\u0010\u00105\u001a\u00020\u00072\u0006\u00106\u001a\u00020\u0019H\u0002J\u0010\u00107\u001a\u00020\u00072\u0006\u00108\u001a\u00020\u0006H\u0002J\u0010\u00109\u001a\u00020\u00072\u0006\u0010:\u001a\u00020\u0006H\u0002J\n\u0010;\u001a\u0004\u0018\u00010\u0019H\u0002J\b\u0010<\u001a\u00020\u0015H\u0002J\b\u0010=\u001a\u00020\u0015H\u0002J\b\u0010>\u001a\u00020\u0015H\u0002J\u0010\u0010?\u001a\u00020\u001b2\u0006\u0010'\u001a\u00020\u001bH\u0002J\u001a\u0010@\u001a\u00020\u00192\b\u0010A\u001a\u0004\u0018\u00010\u00192\u0006\u0010B\u001a\u00020\u0019H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0015X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006D"}, d2 = {"Lcom/carplay/iap2/transport/BluetoothRfcommDuplexStream;", "Lcom/carplay/iap2/transport/BlockingDuplexByteStream;", "socket", "Lcom/carplay/iap2/transport/BluetoothRfcommSocketAccess;", "onDiagnostic", "Lkotlin/Function1;", "", "", "<init>", "(Lcom/carplay/iap2/transport/BluetoothRfcommSocketAccess;Lkotlin/jvm/functions/Function1;)V", "Landroid/bluetooth/BluetoothSocket;", "(Landroid/bluetooth/BluetoothSocket;Lkotlin/jvm/functions/Function1;)V", "lock", "Ljava/lang/Object;", "sendLock", "pending", "Ljava/util/ArrayDeque;", "", "pendingBytes", "", "peerEnded", "", "closed", "socketCloseStarted", "failure", "Ljava/io/IOException;", "readCalls", "", "receivedBytes", "input", "Ljava/io/InputStream;", "output", "Ljava/io/OutputStream;", "reader", "Ljava/lang/Thread;", "send", "data", "recv", "maxBytes", "timeoutMillis", "close", "readLoop", "takePendingLocked", "fail", "io", "acquireStream", "T", "", "operation", "Lcom/carplay/iap2/transport/BluetoothRfcommStreamException$Operation;", "acquire", "Lkotlin/Function0;", "(Lcom/carplay/iap2/transport/BluetoothRfcommStreamException$Operation;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "reportFailure", "error", "reportReadTerminal", "result", "report", "message", "closeSocketOnce", "isClosed", "isStopping", "endedCleanly", "deadlineAfter", "combine", "first", "second", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class BluetoothRfcommDuplexStream implements BlockingDuplexByteStream {
    private static final long CLOSE_JOIN_MILLIS = 1000;
    private static final Companion Companion = new Companion(null);
    private static final byte[] EMPTY = new byte[0];
    private static final int MAX_PENDING_BYTES = 65536;
    private static final long NANOS_PER_MILLISECOND = 1000000;
    private static final int READ_CHUNK_BYTES = 8192;
    private boolean closed;
    private IOException failure;
    private final InputStream input;
    private final Object lock;
    private final Function1<String, Unit> onDiagnostic;
    private final OutputStream output;
    private boolean peerEnded;
    private final ArrayDeque<byte[]> pending;
    private int pendingBytes;
    private long readCalls;
    private final Thread reader;
    private long receivedBytes;
    private final Object sendLock;
    private final BluetoothRfcommSocketAccess socket;
    private boolean socketCloseStarted;

    /* JADX WARN: Multi-variable type inference failed */
    public BluetoothRfcommDuplexStream(BluetoothRfcommSocketAccess socket, Function1<? super String, Unit> onDiagnostic) {
        Intrinsics.checkNotNullParameter(socket, "socket");
        Intrinsics.checkNotNullParameter(onDiagnostic, "onDiagnostic");
        this.socket = socket;
        this.onDiagnostic = onDiagnostic;
        this.lock = new Object();
        this.sendLock = new Object();
        this.pending = new ArrayDeque<>();
        this.input = (InputStream) acquireStream(BluetoothRfcommStreamException.Operation.INPUT_STREAM, new BluetoothRfcommDuplexStream$input$1(this.socket));
        this.output = (OutputStream) acquireStream(BluetoothRfcommStreamException.Operation.OUTPUT_STREAM, new BluetoothRfcommDuplexStream$output$1(this.socket));
        Thread $this$reader_u24lambda_u242 = new Thread(new Runnable() { // from class: com.carplay.iap2.transport.BluetoothRfcommDuplexStream$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.readLoop();
            }
        }, "xcertplay-bluetooth-rfcomm-reader");
        $this$reader_u24lambda_u242.setDaemon(true);
        this.reader = $this$reader_u24lambda_u242;
        report("Bluetooth RFCOMM streams ready input=true output=true");
        this.reader.start();
    }

    public /* synthetic */ BluetoothRfcommDuplexStream(BluetoothRfcommSocketAccess bluetoothRfcommSocketAccess, Function1 function1, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(bluetoothRfcommSocketAccess, (Function1<? super String, Unit>) ((i & 2) != 0 ? new Function1() { // from class: com.carplay.iap2.transport.BluetoothRfcommDuplexStream$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BluetoothRfcommDuplexStream._init_$lambda$0((String) obj);
            }
        } : function1));
    }

    static final Unit _init_$lambda$0(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public BluetoothRfcommDuplexStream(final BluetoothSocket socket, Function1<? super String, Unit> onDiagnostic) {
        this(new BluetoothRfcommSocketAccess() { // from class: com.carplay.iap2.transport.BluetoothRfcommDuplexStream.3
            @Override // com.carplay.iap2.transport.BluetoothRfcommSocketAccess
            public InputStream inputStream() {
                return socket.getInputStream();
            }

            @Override // com.carplay.iap2.transport.BluetoothRfcommSocketAccess
            public OutputStream outputStream() {
                return socket.getOutputStream();
            }

            @Override // com.carplay.iap2.transport.BluetoothRfcommSocketAccess
            public void close() throws IOException {
                socket.close();
            }
        }, onDiagnostic);
        Intrinsics.checkNotNullParameter(socket, "socket");
        Intrinsics.checkNotNullParameter(onDiagnostic, "onDiagnostic");
    }

    public /* synthetic */ BluetoothRfcommDuplexStream(BluetoothSocket bluetoothSocket, Function1 function1, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(bluetoothSocket, (Function1<? super String, Unit>) ((i & 2) != 0 ? new Function1() { // from class: com.carplay.iap2.transport.BluetoothRfcommDuplexStream$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BluetoothRfcommDuplexStream._init_$lambda$1((String) obj);
            }
        } : function1));
    }

    static final Unit _init_$lambda$1(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    @Override // com.carplay.iap2.transport.BlockingDuplexByteStream
    public void send(byte[] data) {
        Intrinsics.checkNotNullParameter(data, "data");
        synchronized (this.sendLock) {
            synchronized (this.lock) {
                IOException it = this.failure;
                if (it != null) {
                    throw it;
                }
                if (this.closed) {
                    throw new IOException("Bluetooth RFCOMM stream is closed");
                }
                Unit unit = Unit.INSTANCE;
            }
            try {
                this.output.write(data);
                this.output.flush();
                Unit unit2 = Unit.INSTANCE;
            } catch (Exception error) {
                BluetoothRfcommStreamException io = new BluetoothRfcommStreamException(BluetoothRfcommStreamException.Operation.WRITE, BluetoothRfcommStreamException.Reason.WRITE_FAILED, error);
                throw fail(io);
            }
        }
    }

    @Override // com.carplay.iap2.transport.BlockingDuplexByteStream
    public byte[] recv(int maxBytes, long timeoutMillis) {
        if (!(maxBytes > 0)) {
            throw new IllegalArgumentException("maxBytes must be positive".toString());
        }
        long j = 0;
        if (!(timeoutMillis >= 0)) {
            throw new IllegalArgumentException("timeoutMillis must not be negative".toString());
        }
        long deadlineNanos = deadlineAfter(timeoutMillis);
        synchronized (this.lock) {
            while (true) {
                byte[] bArrTakePendingLocked = takePendingLocked(maxBytes);
                if (bArrTakePendingLocked != null) {
                    return bArrTakePendingLocked;
                }
                IOException it = this.failure;
                if (it != null) {
                    throw it;
                }
                if (!this.peerEnded && !this.closed) {
                    long remainingNanos = deadlineNanos - System.nanoTime();
                    if (remainingNanos <= j) {
                        return null;
                    }
                    try {
                        this.lock.wait(remainingNanos / 1000000, (int) (remainingNanos % 1000000));
                        j = 0;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return null;
                    }
                }
                return EMPTY;
            }
        }
    }

    @Override // com.carplay.iap2.transport.BlockingDuplexByteStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        boolean firstClose;
        synchronized (this.lock) {
            if (this.closed) {
                firstClose = false;
            } else {
                firstClose = true;
                this.closed = true;
                this.lock.notifyAll();
            }
        }
        if (firstClose) {
            IOException closeFailure = closeSocketOnce();
            if (Thread.currentThread() != this.reader) {
                try {
                    this.reader.join(1000L);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    closeFailure = combine(closeFailure, new IOException("Interrupted while closing the Bluetooth RFCOMM reader", interrupted));
                }
                if (this.reader.isAlive()) {
                    closeFailure = combine(closeFailure, new IOException("Bluetooth RFCOMM reader did not stop after close"));
                }
            }
            if (closeFailure != null) {
                throw closeFailure;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void readLoop() {
        IOException readFailure;
        IOException closeFailure;
        byte[] bArrCopyOf;
        while (true) {
            try {
                try {
                    try {
                        synchronized (this.lock) {
                            while (!this.closed && this.failure == null && this.pendingBytes >= 65536) {
                                this.lock.wait();
                            }
                            if (!this.closed && this.failure == null) {
                                int readSize = Math.min(8192, 65536 - this.pendingBytes);
                                byte[] buffer = new byte[readSize];
                                this.readCalls++;
                                int count = this.input.read(buffer);
                                switch (count) {
                                    case -1:
                                        if (!isStopping()) {
                                            reportReadTerminal("ENDED");
                                        }
                                        synchronized (this.lock) {
                                            this.peerEnded = true;
                                            this.lock.notifyAll();
                                            Unit unit = Unit.INSTANCE;
                                        }
                                        IOException closeFailure2 = closeSocketOnce();
                                        if (closeFailure2 == null || isClosed() || endedCleanly()) {
                                            return;
                                        }
                                        fail(closeFailure2);
                                        return;
                                    case 0:
                                        break;
                                    default:
                                        synchronized (this.lock) {
                                            if (!this.closed) {
                                                this.receivedBytes += (long) count;
                                                ArrayDeque<byte[]> arrayDeque = this.pending;
                                                if (count == buffer.length) {
                                                    bArrCopyOf = buffer;
                                                } else {
                                                    bArrCopyOf = Arrays.copyOf(buffer, count);
                                                    Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
                                                }
                                                arrayDeque.addLast(bArrCopyOf);
                                                this.pendingBytes += count;
                                                this.lock.notifyAll();
                                                Unit unit2 = Unit.INSTANCE;
                                                break;
                                            }
                                        }
                                        IOException closeFailure3 = closeSocketOnce();
                                        if (closeFailure3 == null || isClosed() || endedCleanly()) {
                                            return;
                                        }
                                        fail(closeFailure3);
                                        return;
                                }
                            }
                        }
                    } catch (InterruptedException interrupted) {
                        Thread.currentThread().interrupt();
                        readFailure = isStopping() ? null : new IOException("Bluetooth RFCOMM reader was interrupted", interrupted);
                        if (readFailure != null) {
                            IOException it = readFailure;
                            reportReadTerminal("FAILED");
                            fail(it);
                        }
                        closeFailure = closeSocketOnce();
                        if (readFailure != null || closeFailure == null || isClosed() || endedCleanly()) {
                            return;
                        }
                        fail(closeFailure);
                        return;
                    }
                } catch (IOException io) {
                    readFailure = isStopping() ? null : new BluetoothRfcommStreamException(BluetoothRfcommStreamException.Operation.READ, BluetoothRfcommStreamException.Reason.READ_FAILED, io);
                    if (readFailure != null) {
                        IOException it2 = readFailure;
                        reportReadTerminal("FAILED");
                        fail(it2);
                    }
                    closeFailure = closeSocketOnce();
                    if (readFailure != null || closeFailure == null || isClosed() || endedCleanly()) {
                        return;
                    }
                    fail(closeFailure);
                    return;
                } catch (Throwable failure) {
                    readFailure = isStopping() ? null : new BluetoothRfcommStreamException(BluetoothRfcommStreamException.Operation.READ, BluetoothRfcommStreamException.Reason.READ_FAILED, failure);
                    if (failure instanceof Error) {
                        throw failure;
                    }
                    if (readFailure != null) {
                        IOException it3 = readFailure;
                        reportReadTerminal("FAILED");
                        fail(it3);
                    }
                    closeFailure = closeSocketOnce();
                    if (readFailure != null || closeFailure == null || isClosed() || endedCleanly()) {
                        return;
                    }
                    fail(closeFailure);
                    return;
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    reportReadTerminal("FAILED");
                    fail(null);
                }
                IOException closeFailure4 = closeSocketOnce();
                if (0 == 0 && closeFailure4 != null && !isClosed() && !endedCleanly()) {
                    fail(closeFailure4);
                }
                throw th;
            }
        }
        IOException closeFailure5 = closeSocketOnce();
        if (closeFailure5 == null || isClosed() || endedCleanly()) {
            return;
        }
        fail(closeFailure5);
    }

    private final byte[] takePendingLocked(int maxBytes) {
        byte[] chunk = this.pending.pollFirst();
        if (chunk == null) {
            return null;
        }
        this.pendingBytes -= chunk.length;
        if (chunk.length <= maxBytes) {
            this.lock.notifyAll();
            return chunk;
        }
        byte[] head = Arrays.copyOf(chunk, maxBytes);
        Intrinsics.checkNotNullExpressionValue(head, "copyOf(...)");
        byte[] tail = ArraysKt.copyOfRange(chunk, maxBytes, chunk.length);
        this.pending.addFirst(tail);
        this.pendingBytes += tail.length;
        this.lock.notifyAll();
        return head;
    }

    private final IOException fail(IOException io) {
        synchronized (this.lock) {
            IOException iOException = this.failure;
            if (iOException != null) {
                return iOException;
            }
            if (this.closed) {
                return new IOException("Bluetooth RFCOMM stream is closed");
            }
            this.failure = io;
            this.lock.notifyAll();
            Unit unit = Unit.INSTANCE;
            reportFailure(io);
            IOException it = closeSocketOnce();
            if (it != null && it != io) {
                ExceptionsKt.addSuppressed(io, it);
            }
            return io;
        }
    }

    private final <T> T acquireStream(BluetoothRfcommStreamException.Operation operation, Function0<? extends T> acquire) throws Throwable {
        BluetoothRfcommStreamException.Operation operation2;
        Throwable error;
        try {
            T tInvoke = acquire.invoke();
            if (tInvoke != null) {
                return tInvoke;
            }
            operation2 = operation;
            try {
                throw new BluetoothRfcommStreamException(operation2, BluetoothRfcommStreamException.Reason.STREAM_UNAVAILABLE, null, 4, null);
            } catch (Throwable th) {
                error = th;
            }
        } catch (Throwable th2) {
            operation2 = operation;
            error = th2;
        }
        BluetoothRfcommStreamException failure = error instanceof BluetoothRfcommStreamException ? (BluetoothRfcommStreamException) error : new BluetoothRfcommStreamException(operation2, BluetoothRfcommStreamException.Reason.STREAM_ACCESS_FAILED, error);
        reportFailure(failure);
        Throwable thCloseSocketOnce = closeSocketOnce();
        if (thCloseSocketOnce != null) {
            Throwable p0 = thCloseSocketOnce;
            failure.addSuppressed(p0);
        }
        if (error instanceof Error) {
            throw error;
        }
        throw failure;
    }

    private final void reportFailure(IOException error) {
        Object operation;
        String simpleName;
        Throwable cause;
        Class<?> cls;
        String simpleName2;
        Class<?> cls2;
        BluetoothRfcommStreamException.Reason reason;
        BluetoothRfcommStreamException streamFailure = error instanceof BluetoothRfcommStreamException ? (BluetoothRfcommStreamException) error : null;
        Object obj = "UNKNOWN";
        if (streamFailure == null || (operation = streamFailure.getOperation()) == null) {
            operation = "UNKNOWN";
        }
        if (streamFailure != null && (reason = streamFailure.getReason()) != null) {
            obj = reason;
        }
        String simpleName3 = error.getClass().getSimpleName();
        Throwable cause2 = error.getCause();
        String str = "none";
        if (cause2 == null || (cls2 = cause2.getClass()) == null || (simpleName = cls2.getSimpleName()) == null) {
            simpleName = "none";
        }
        Throwable cause3 = error.getCause();
        if (cause3 != null && (cause = cause3.getCause()) != null && (cls = cause.getClass()) != null && (simpleName2 = cls.getSimpleName()) != null) {
            str = simpleName2;
        }
        report("Bluetooth RFCOMM stream result=FAILED operation=" + operation + " reason=" + obj + " failureClass=" + simpleName3 + " causeClass=" + simpleName + " nestedCauseClass=" + str);
    }

    private final void reportReadTerminal(String result) {
        report("Bluetooth RFCOMM reader result=" + result + " beforeFirstByte=" + (this.receivedBytes == 0) + " readCalls=" + this.readCalls + " receivedBytes=" + this.receivedBytes);
    }

    private final void report(String message) {
        try {
            this.onDiagnostic.invoke(message);
        } catch (Exception e) {
        }
    }

    private final IOException closeSocketOnce() {
        synchronized (this.lock) {
            if (this.socketCloseStarted) {
                return null;
            }
            this.socketCloseStarted = true;
            Unit unit = Unit.INSTANCE;
            try {
                this.socket.close();
                return null;
            } catch (Throwable failure) {
                if (failure instanceof Error) {
                    throw failure;
                }
                return new IOException("Could not close the Bluetooth RFCOMM socket", failure);
            }
        }
    }

    private final boolean isClosed() {
        boolean z;
        synchronized (this.lock) {
            z = this.closed;
        }
        return z;
    }

    private final boolean isStopping() {
        boolean z;
        synchronized (this.lock) {
            z = this.closed || this.failure != null;
        }
        return z;
    }

    private final boolean endedCleanly() {
        boolean z;
        synchronized (this.lock) {
            z = this.closed || this.peerEnded;
        }
        return z;
    }

    private final long deadlineAfter(long timeoutMillis) {
        long now = System.nanoTime();
        long delta = 1000000 * timeoutMillis;
        if (Long.MAX_VALUE - now < delta) {
            return Long.MAX_VALUE;
        }
        return now + delta;
    }

    private final IOException combine(IOException first, IOException second) {
        if (first == null) {
            return second;
        }
        if (first != second) {
            ExceptionsKt.addSuppressed(first, second);
        }
        return first;
    }

    /* JADX INFO: compiled from: BluetoothRfcommDuplexStream.kt */
    @Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lcom/carplay/iap2/transport/BluetoothRfcommDuplexStream$Companion;", "", "<init>", "()V", "READ_CHUNK_BYTES", "", "MAX_PENDING_BYTES", "CLOSE_JOIN_MILLIS", "", "NANOS_PER_MILLISECOND", "EMPTY", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
