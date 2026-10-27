package com.carplay.iap2.transport;

import java.io.IOException;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PhoneBluetoothSessionTransport.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00060\u0001j\u0002`\u0002:\u0001\u0012B\u000f\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0004\b\u0005\u0010\u0006J\u0010\u0010\r\u001a\u00020\u00042\b\b\u0002\u0010\u000e\u001a\u00020\u000fJ\b\u0010\u0010\u001a\u00020\u0011H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0018\u00010\fR\u00020\u0000X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lcom/carplay/iap2/transport/PhoneBluetoothSessionTransport;", "Ljava/lang/AutoCloseable;", "Lkotlin/AutoCloseable;", "underlying", "Lcom/carplay/iap2/transport/BlockingDuplexByteStream;", "<init>", "(Lcom/carplay/iap2/transport/BlockingDuplexByteStream;)V", "lock", "", "closed", "", "current", "Lcom/carplay/iap2/transport/PhoneBluetoothSessionTransport$Lease;", "open", "initialInput", "", "close", "", "Lease", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class PhoneBluetoothSessionTransport implements AutoCloseable {
    private boolean closed;
    private Lease current;
    private final Object lock;
    private final BlockingDuplexByteStream underlying;

    public PhoneBluetoothSessionTransport(BlockingDuplexByteStream underlying) {
        Intrinsics.checkNotNullParameter(underlying, "underlying");
        this.underlying = underlying;
        this.lock = new Object();
    }

    public static /* synthetic */ BlockingDuplexByteStream open$default(PhoneBluetoothSessionTransport phoneBluetoothSessionTransport, byte[] bArr, int i, Object obj) {
        if ((i & 1) != 0) {
            bArr = new byte[0];
        }
        return phoneBluetoothSessionTransport.open(bArr);
    }

    /* JADX WARN: Code duplicated, block: B:22:0x0030  */
    public final BlockingDuplexByteStream open(byte[] initialInput) {
        boolean z;
        Lease it;
        Intrinsics.checkNotNullParameter(initialInput, "initialInput");
        synchronized (this.lock) {
            if (this.closed) {
                throw new IllegalStateException("蓝牙连接已关闭".toString());
            }
            Lease lease = this.current;
            boolean z2 = true;
            if ((lease == null || lease.getClosed()) ? false : true) {
                throw new IllegalStateException("旧 iAP2 协议实例尚未关闭".toString());
            }
            Lease lease2 = this.current;
            if (lease2 == null) {
                z = false;
            } else {
                if ((lease2.getOperations() == 0 ? 1 : 0) == 0) {
                    z = true;
                } else {
                    z = false;
                }
            }
            if (z) {
                throw new IllegalStateException("旧 iAP2 协议实例仍在读写".toString());
            }
            if (initialInput.length > 65535) {
                z2 = false;
            }
            if (!z2) {
                throw new IllegalArgumentException("重新检测的待处理数据过多".toString());
            }
            byte[] bArrCopyOf = Arrays.copyOf(initialInput, initialInput.length);
            Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
            it = new Lease(this, bArrCopyOf);
            this.current = it;
        }
        return it;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        synchronized (this.lock) {
            if (this.closed) {
                return;
            }
            this.closed = true;
            Lease lease = this.current;
            if (lease != null) {
                lease.setClosed(true);
            }
            Unit unit = Unit.INSTANCE;
            this.underlying.close();
        }
    }

    /* JADX INFO: compiled from: PhoneBluetoothSessionTransport.kt */
    @Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0002\b\u0082\u0004\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\b\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0003H\u0016J\u001a\u0010\u0016\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\b\u0010\u001a\u001a\u00020\u0013H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001a\u0010\f\u001a\u00020\rX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011¨\u0006\u001b"}, d2 = {"Lcom/carplay/iap2/transport/PhoneBluetoothSessionTransport$Lease;", "Lcom/carplay/iap2/transport/BlockingDuplexByteStream;", "initialInput", "", "<init>", "(Lcom/carplay/iap2/transport/PhoneBluetoothSessionTransport;[B)V", "closed", "", "getClosed", "()Z", "setClosed", "(Z)V", "operations", "", "getOperations", "()I", "setOperations", "(I)V", "checkOpen", "", "send", "data", "recv", "maxBytes", "timeoutMillis", "", "close", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private final class Lease implements BlockingDuplexByteStream {
        private boolean closed;
        private byte[] initialInput;
        private int operations;
        final /* synthetic */ PhoneBluetoothSessionTransport this$0;

        public Lease(PhoneBluetoothSessionTransport this$0, byte[] initialInput) {
            Intrinsics.checkNotNullParameter(initialInput, "initialInput");
            this.this$0 = this$0;
            this.initialInput = initialInput;
        }

        public final boolean getClosed() {
            return this.closed;
        }

        public final void setClosed(boolean z) {
            this.closed = z;
        }

        public final int getOperations() {
            return this.operations;
        }

        public final void setOperations(int i) {
            this.operations = i;
        }

        private final void checkOpen() throws IOException {
            if (this.closed || this.this$0.closed || this.this$0.current != this) {
                throw new IOException("旧 iAP2 协议实例已关闭");
            }
        }

        @Override // com.carplay.iap2.transport.BlockingDuplexByteStream
        public void send(byte[] data) {
            Intrinsics.checkNotNullParameter(data, "data");
            synchronized (this.this$0.lock) {
                checkOpen();
                this.operations++;
            }
            try {
                this.this$0.underlying.send(data);
                synchronized (this.this$0.lock) {
                    this.operations--;
                }
            } catch (Throwable th) {
                synchronized (this.this$0.lock) {
                    this.operations--;
                    throw th;
                }
            }
        }

        @Override // com.carplay.iap2.transport.BlockingDuplexByteStream
        public byte[] recv(int maxBytes, long timeoutMillis) {
            if (!(maxBytes > 0 && timeoutMillis >= 0)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            synchronized (this.this$0.lock) {
                checkOpen();
                if (!(this.initialInput.length == 0)) {
                    int size = Math.min(maxBytes, this.initialInput.length);
                    byte[] bArrCopyOfRange = ArraysKt.copyOfRange(this.initialInput, 0, size);
                    this.initialInput = ArraysKt.copyOfRange(this.initialInput, size, this.initialInput.length);
                    return bArrCopyOfRange;
                }
                this.operations++;
                try {
                    byte[] bArrRecv = this.this$0.underlying.recv(maxBytes, timeoutMillis);
                    synchronized (this.this$0.lock) {
                        checkOpen();
                        Unit unit = Unit.INSTANCE;
                    }
                    synchronized (this.this$0.lock) {
                        this.operations--;
                    }
                    return bArrRecv;
                } catch (Throwable th) {
                    synchronized (this.this$0.lock) {
                        this.operations--;
                        throw th;
                    }
                }
            }
        }

        @Override // com.carplay.iap2.transport.BlockingDuplexByteStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            synchronized (this.this$0.lock) {
                this.closed = true;
                this.initialInput = new byte[0];
                Unit unit = Unit.INSTANCE;
            }
        }
    }
}
