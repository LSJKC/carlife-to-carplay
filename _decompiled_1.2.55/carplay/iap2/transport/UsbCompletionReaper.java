package com.carplay.iap2.transport;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArrayDeque;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: UsbRequestQueue.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0004\b\u0000\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003B\u001f\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u000e\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u0007¢\u0006\u0004\b\b\u0010\tJ\u0006\u0010\u0015\u001a\u00020\u0016J\u0015\u0010\u0017\u001a\u0004\u0018\u00018\u00002\u0006\u0010\u0018\u001a\u00020\u0019¢\u0006\u0002\u0010\u001aJ\b\u0010\u001b\u001a\u00020\u0016H\u0016J\b\u0010\u001c\u001a\u00020\u0016H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lcom/carplay/iap2/transport/UsbCompletionReaper;", "T", "", "Ljava/io/Closeable;", "threadName", "", "reapBlocking", "Lkotlin/Function0;", "<init>", "(Ljava/lang/String;Lkotlin/jvm/functions/Function0;)V", "lock", "Ljava/lang/Object;", "completed", "Lkotlin/collections/ArrayDeque;", "inFlight", "", "failed", "", "closed", "thread", "Ljava/lang/Thread;", "queued", "", "await", "timeoutMillis", "", "(J)Ljava/lang/Object;", "close", "reapLoop", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class UsbCompletionReaper<T> implements Closeable {
    private boolean closed;
    private final ArrayDeque<T> completed;
    private boolean failed;
    private int inFlight;
    private final Object lock;
    private final Function0<T> reapBlocking;
    private Thread thread;
    private final String threadName;

    /* JADX WARN: Multi-variable type inference failed */
    public UsbCompletionReaper(String threadName, Function0<? extends T> reapBlocking) {
        Intrinsics.checkNotNullParameter(threadName, "threadName");
        Intrinsics.checkNotNullParameter(reapBlocking, "reapBlocking");
        this.threadName = threadName;
        this.reapBlocking = reapBlocking;
        this.lock = new Object();
        this.completed = new ArrayDeque<>();
    }

    public final void queued() {
        synchronized (this.lock) {
            if (this.closed) {
                throw new IllegalStateException("USB request reaper is closed".toString());
            }
            this.inFlight++;
            if (this.thread == null && !this.failed) {
                Thread $this$queued_u24lambda_u242_u24lambda_u241 = new Thread(new Runnable() { // from class: com.carplay.iap2.transport.UsbCompletionReaper$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.reapLoop();
                    }
                }, this.threadName);
                $this$queued_u24lambda_u242_u24lambda_u241.setDaemon(true);
                $this$queued_u24lambda_u242_u24lambda_u241.start();
                this.thread = $this$queued_u24lambda_u242_u24lambda_u241;
            }
            this.lock.notifyAll();
            Unit unit = Unit.INSTANCE;
        }
    }

    public final T await(long timeoutMillis) throws TimeoutException {
        T tRemoveFirstOrNull;
        synchronized (this.lock) {
            try {
                if (!(timeoutMillis > 0)) {
                    throw new IllegalArgumentException("timeoutMillis must be positive".toString());
                }
                long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMillis);
                while (this.completed.isEmpty() && !this.failed) {
                    long remaining = deadline - System.nanoTime();
                    if (remaining <= 0) {
                        throw new TimeoutException("No USB request completed within " + timeoutMillis + " ms");
                    }
                    TimeUnit.NANOSECONDS.timedWait(this.lock, remaining);
                }
                tRemoveFirstOrNull = this.completed.removeFirstOrNull();
            } catch (Throwable th) {
                throw th;
            }
        }
        return tRemoveFirstOrNull;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        synchronized (this.lock) {
            this.closed = true;
            this.lock.notifyAll();
            Unit unit = Unit.INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void reapLoop() {
        T tInvoke;
        while (true) {
            synchronized (this.lock) {
                while (this.inFlight == 0 && !this.closed) {
                    this.lock.wait();
                }
                if (this.inFlight == 0) {
                    this.thread = null;
                    return;
                }
                Unit unit = Unit.INSTANCE;
                try {
                    tInvoke = this.reapBlocking.invoke();
                } catch (RuntimeException e) {
                    tInvoke = null;
                }
                synchronized (this.lock) {
                    if (tInvoke != null) {
                        this.inFlight--;
                        this.completed.addLast(tInvoke);
                        this.lock.notifyAll();
                        Unit unit2 = Unit.INSTANCE;
                    } else {
                        this.failed = true;
                        this.thread = null;
                        this.lock.notifyAll();
                        return;
                    }
                    throw th;
                }
            }
        }
    }
}
