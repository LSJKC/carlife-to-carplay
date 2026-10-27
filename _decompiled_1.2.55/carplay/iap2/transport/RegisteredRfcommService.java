package com.carplay.iap2.transport;

import androidx.core.app.NotificationCompat;
import java.lang.AutoCloseable;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: RegisteredRfcommService.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\u0018\u0000*\f\b\u0000\u0010\u0001*\u00060\u0002j\u0002`\u00032\u00060\u0002j\u0002`\u0003B\u0015\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u000b\u0010\f\u001a\u00028\u0000¢\u0006\u0002\u0010\rJ\b\u0010\u000e\u001a\u00020\u000fH\u0016R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\b\u001a\u0004\u0018\u00018\u0000X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lcom/carplay/iap2/transport/RegisteredRfcommService;", "T", "Ljava/lang/AutoCloseable;", "Lkotlin/AutoCloseable;", "register", "Lkotlin/Function0;", "<init>", "(Lkotlin/jvm/functions/Function0;)V", NotificationCompat.CATEGORY_SERVICE, "Ljava/lang/AutoCloseable;", "closed", "", "acquire", "()Ljava/lang/AutoCloseable;", "close", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class RegisteredRfcommService<T extends AutoCloseable> implements AutoCloseable {
    private boolean closed;
    private final Function0<T> register;
    private T service;

    /* JADX WARN: Multi-variable type inference failed */
    public RegisteredRfcommService(Function0<? extends T> register) {
        Intrinsics.checkNotNullParameter(register, "register");
        this.register = register;
    }

    public final synchronized T acquire() {
        T t;
        if (this.closed) {
            throw new IllegalStateException("手机蓝牙服务已关闭".toString());
        }
        t = this.service;
        if (t == null) {
            T tInvoke = this.register.invoke();
            this.service = tInvoke;
            t = tInvoke;
        }
        return t;
    }

    @Override // java.lang.AutoCloseable
    public synchronized void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
        AutoCloseable current = this.service;
        this.service = null;
        if (current != null) {
            RegisteredRfcommService$$ExternalSyntheticAutoCloseableDispatcher0.m(current);
        }
    }
}
