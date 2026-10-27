package com.carplay.sender;

import com.carplay.airplay.ControlCipher;
import com.carplay.airplay.IapTunnel;
import com.carplay.iap2.transport.BlockingDuplexByteStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PhoneIapTunnel.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\n\u0018\u0000 +2\u00020\u0001:\u0001+B7\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u0016\b\u0002\u0010\u0006\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\b\u0018\u00010\u0007¢\u0006\u0004\b\t\u0010\nJ\u0006\u0010!\u001a\u00020\"J\u000e\u0010#\u001a\u00020\f2\u0006\u0010$\u001a\u00020\u0005J\u0010\u0010%\u001a\u00020\b2\u0006\u0010&\u001a\u00020\u0005H\u0016J\u001a\u0010'\u001a\u0004\u0018\u00010\u00052\u0006\u0010(\u001a\u00020\u00182\u0006\u0010)\u001a\u00020\u001dH\u0016J\b\u0010*\u001a\u00020\bH\u0016R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0006\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\b\u0018\u00010\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u000b\u001a\u00020\f8F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\rR\u0011\u0010\u000e\u001a\u00020\f8F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\rR\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00050\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001dX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u001dX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\u001dX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006,"}, d2 = {"Lcom/carplay/sender/PhoneIapTunnel;", "Lcom/carplay/iap2/transport/BlockingDuplexByteStream;", "socket", "Ljava/net/Socket;", "writeKey", "", "sendHandler", "Lkotlin/Function1;", "", "<init>", "(Ljava/net/Socket;[BLkotlin/jvm/functions/Function1;)V", "isCommandTunnel", "", "()Z", "isClosed", "cipher", "Lcom/carplay/airplay/ControlCipher;", "lock", "Ljava/lang/Object;", "writeLock", "", "pending", "Ljava/util/ArrayDeque;", "pendingBytes", "", "closed", "failure", "Ljava/io/IOException;", "txPackets", "", "txBytes", "rxPackets", "rxBytes", "diagnosticSummary", "", "offer", "bytes", "send", "data", "recv", "maxBytes", "timeoutMillis", "close", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class PhoneIapTunnel implements BlockingDuplexByteStream {
    private static final int MAX_PENDING_BYTES = 1048576;
    private final ControlCipher cipher;
    private boolean closed;
    private IOException failure;
    private final Object lock;
    private final ArrayDeque<byte[]> pending;
    private int pendingBytes;
    private long rxBytes;
    private long rxPackets;
    private final Function1<byte[], Unit> sendHandler;
    private final Socket socket;
    private long txBytes;
    private long txPackets;
    private final Object writeLock;

    public PhoneIapTunnel() {
        this(null, null, null, 7, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public PhoneIapTunnel(Socket socket, byte[] writeKey, Function1<? super byte[], Unit> function1) {
        this.socket = socket;
        this.sendHandler = function1;
        this.cipher = writeKey != null ? new ControlCipher(writeKey, writeKey) : null;
        this.lock = new Object();
        this.writeLock = new Object();
        this.pending = new ArrayDeque<>();
    }

    public /* synthetic */ PhoneIapTunnel(Socket socket, byte[] bArr, Function1 function1, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : socket, (i & 2) != 0 ? null : bArr, (i & 4) != 0 ? null : function1);
    }

    public final boolean isCommandTunnel() {
        return this.socket == null;
    }

    public final boolean isClosed() {
        boolean z;
        synchronized (this.lock) {
            z = this.closed || this.failure != null;
        }
        return z;
    }

    public final String diagnosticSummary() {
        String str;
        synchronized (this.lock) {
            String str2 = isCommandTunnel() ? "command" : "tcp+event";
            str = "mode=" + str2 + " txPackets=" + this.txPackets + " txBytes=" + this.txBytes + " rxPackets=" + this.rxPackets + " rxBytes=" + this.rxBytes + " pendingBytes=" + this.pendingBytes;
        }
        return str;
    }

    public final boolean offer(byte[] bytes) throws IOException {
        Socket socket;
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        if (bytes.length == 0) {
            return false;
        }
        boolean overflow = false;
        synchronized (this.lock) {
            if (!this.closed && this.failure == null) {
                if (bytes.length > 1048576 - this.pendingBytes) {
                    this.failure = new IOException("Wi-Fi iAP2 接收队列超出限制");
                    overflow = true;
                } else {
                    ArrayDeque<byte[]> arrayDeque = this.pending;
                    byte[] bArrCopyOf = Arrays.copyOf(bytes, bytes.length);
                    Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
                    arrayDeque.addLast(bArrCopyOf);
                    this.pendingBytes += bytes.length;
                    this.rxPackets++;
                    this.rxBytes += (long) bytes.length;
                }
                this.lock.notifyAll();
                Unit unit = Unit.INSTANCE;
                if (overflow && (socket = this.socket) != null) {
                    socket.close();
                }
                return !overflow;
            }
            return false;
        }
    }

    @Override // com.carplay.iap2.transport.BlockingDuplexByteStream
    public void send(byte[] data) {
        Intrinsics.checkNotNullParameter(data, "data");
        int length = data.length;
        if (!(1 <= length && length < 65536)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        synchronized (this.writeLock) {
            synchronized (this.lock) {
                IOException it = this.failure;
                if (it != null) {
                    throw it;
                }
                if (this.closed) {
                    throw new IOException("Wi-Fi iAP2 通道已关闭");
                }
                Unit unit = Unit.INSTANCE;
            }
            try {
                if (this.socket != null && this.cipher != null) {
                    byte[] header = ByteBuffer.allocate(32).order(ByteOrder.BIG_ENDIAN).putInt(0, data.length + 32).putInt(16, IapTunnel.MSG_TYPE_COMM).array();
                    OutputStream $this$send_u24lambda_u248_u24lambda_u245 = this.socket.getOutputStream();
                    ControlCipher controlCipher = this.cipher;
                    Intrinsics.checkNotNull(header);
                    $this$send_u24lambda_u248_u24lambda_u245.write(controlCipher.encrypt(ArraysKt.plus(header, data)));
                    $this$send_u24lambda_u248_u24lambda_u245.flush();
                } else {
                    if (this.sendHandler == null) {
                        throw new IOException("Wi-Fi iAP2 通道无有效发送实现");
                    }
                    this.sendHandler.invoke(data);
                }
                synchronized (this.lock) {
                    this.txPackets++;
                    this.txBytes += (long) data.length;
                    Unit unit2 = Unit.INSTANCE;
                }
                Unit unit3 = Unit.INSTANCE;
            } catch (IOException error) {
                synchronized (this.lock) {
                    this.failure = error;
                    this.lock.notifyAll();
                    Unit unit4 = Unit.INSTANCE;
                    Socket socket = this.socket;
                    if (socket != null) {
                        socket.close();
                    }
                    throw error;
                }
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:12:0x0019  */
    @Override // com.carplay.iap2.transport.BlockingDuplexByteStream
    public byte[] recv(int maxBytes, long timeoutMillis) {
        boolean z;
        long j = 0;
        int i = 0;
        if (maxBytes > 0) {
            z = (0L > timeoutMillis ? 1 : (0L == timeoutMillis ? 0 : -1)) <= 0 && (timeoutMillis > 86400001L ? 1 : (timeoutMillis == 86400001L ? 0 : -1)) < 0;
        }
        if (!z) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        long deadline = System.nanoTime() + (timeoutMillis * 1000000);
        synchronized (this.lock) {
            while (true) {
                IOException it = this.failure;
                if (it != null) {
                    throw it;
                }
                if (this.closed) {
                    return new byte[i];
                }
                byte[] chunk = this.pending.pollFirst();
                if (chunk != null) {
                    this.pendingBytes -= chunk.length;
                    if (chunk.length <= maxBytes) {
                        return chunk;
                    }
                    byte[] tail = ArraysKt.copyOfRange(chunk, maxBytes, chunk.length);
                    this.pending.addFirst(tail);
                    this.pendingBytes += tail.length;
                    byte[] bArrCopyOf = Arrays.copyOf(chunk, maxBytes);
                    Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
                    return bArrCopyOf;
                }
                long remaining = deadline - System.nanoTime();
                if (remaining <= j) {
                    return null;
                }
                try {
                    this.lock.wait(remaining / 1000000, (int) (remaining % 1000000));
                    i = 0;
                    j = 0;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
        }
    }

    @Override // com.carplay.iap2.transport.BlockingDuplexByteStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        synchronized (this.lock) {
            if (this.closed) {
                return;
            }
            this.closed = true;
            this.pending.clear();
            this.pendingBytes = 0;
            this.lock.notifyAll();
            Unit unit = Unit.INSTANCE;
            Socket socket = this.socket;
            if (socket != null) {
                socket.close();
            }
        }
    }
}
