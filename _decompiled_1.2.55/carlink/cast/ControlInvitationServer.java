package com.carlink.cast;

import android.net.Network;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructPollfd;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Discovery.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0000\u0018\u00002\u00020\u0001B+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007¢\u0006\u0004\b\n\u0010\u000bJ\u0006\u0010\u0017\u001a\u00020\tJ\b\u0010\u0018\u001a\u00020\tH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\n \u0010*\u0004\u0018\u00010\u000f0\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0011\u001a\u00020\u0012¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0019"}, d2 = {"Lcom/carlink/cast/ControlInvitationServer;", "Ljava/io/Closeable;", "network", "Landroid/net/Network;", "local", "Ljava/net/InetAddress;", "log", "Lkotlin/Function1;", "", "", "<init>", "(Landroid/net/Network;Ljava/net/InetAddress;Lkotlin/jvm/functions/Function1;)V", "closed", "Ljava/util/concurrent/atomic/AtomicBoolean;", "descriptor", "Ljava/io/FileDescriptor;", "kotlin.jvm.PlatformType", "port", "", "getPort", "()I", "thread", "Ljava/lang/Thread;", "start", "close", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ControlInvitationServer implements Closeable {
    private final AtomicBoolean closed;
    private final FileDescriptor descriptor;
    private final InetAddress local;
    private final Function1<String, Unit> log;
    private final Network network;
    private final int port;
    private Thread thread;

    /* JADX WARN: Multi-variable type inference failed */
    public ControlInvitationServer(Network network, InetAddress local, Function1<? super String, Unit> log) throws IOException {
        Intrinsics.checkNotNullParameter(network, "network");
        Intrinsics.checkNotNullParameter(local, "local");
        Intrinsics.checkNotNullParameter(log, "log");
        this.network = network;
        this.local = local;
        this.log = log;
        this.closed = new AtomicBoolean();
        this.descriptor = Os.socket(this.local instanceof Inet6Address ? OsConstants.AF_INET6 : OsConstants.AF_INET, OsConstants.SOCK_STREAM, OsConstants.IPPROTO_TCP);
        try {
            this.network.bindSocket(this.descriptor);
            Os.setsockoptInt(this.descriptor, OsConstants.SOL_SOCKET, OsConstants.SO_REUSEADDR, 1);
            Os.bind(this.descriptor, this.local, 0);
            Os.listen(this.descriptor, 8);
            SocketAddress socketAddress = Os.getsockname(this.descriptor);
            Intrinsics.checkNotNull(socketAddress, "null cannot be cast to non-null type java.net.InetSocketAddress");
            this.port = ((InetSocketAddress) socketAddress).getPort();
        } catch (Exception failure) {
            try {
                Result.Companion companion = Result.INSTANCE;
                Os.close(this.descriptor);
                Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            throw new IOException("控制邀请监听创建失败", failure);
        }
    }

    public final int getPort() {
        return this.port;
    }

    public final void start() {
        Thread thread = new Thread(new Runnable() { // from class: com.carlink.cast.ControlInvitationServer$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ControlInvitationServer.start$lambda$9(this.f$0);
            }
        }, "carlink-invitation");
        thread.setDaemon(true);
        thread.start();
        this.thread = thread;
    }

    /* JADX WARN: Code duplicated, block: B:81:0x0151  */
    static final void start$lambda$9(ControlInvitationServer this$0) {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        byte[] buffer;
        Object objM159constructorimpl3;
        Throwable thM162exceptionOrNullimpl;
        Throwable th;
        String str;
        Throwable th2;
        byte[] buffer2 = new byte[4096];
        while (!this$0.closed.get()) {
            StructPollfd structPollfd = new StructPollfd();
            structPollfd.fd = this$0.descriptor;
            structPollfd.events = (short) OsConstants.POLLIN;
            try {
                Result.Companion companion = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(Integer.valueOf(Os.poll(new StructPollfd[]{structPollfd}, 1000)));
            } catch (Throwable th3) {
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th3));
            }
            if (Result.m165isFailureimpl(objM159constructorimpl)) {
                objM159constructorimpl = 0;
            }
            int ready = ((Number) objM159constructorimpl).intValue();
            if (ready > 0) {
                try {
                    Result.Companion companion3 = Result.INSTANCE;
                    objM159constructorimpl2 = Result.m159constructorimpl(Os.accept(this$0.descriptor, null));
                } catch (Throwable th4) {
                    Result.Companion companion4 = Result.INSTANCE;
                    objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th4));
                }
                if (Result.m165isFailureimpl(objM159constructorimpl2)) {
                    objM159constructorimpl2 = null;
                }
                FileDescriptor accepted = (FileDescriptor) objM159constructorimpl2;
                if (accepted == null) {
                    continue;
                } else {
                    try {
                        Result.Companion companion5 = Result.INSTANCE;
                        FileInputStream fileInputStream = new FileInputStream(accepted);
                        try {
                            int i = fileInputStream.read(buffer2);
                            if (i > 0) {
                                try {
                                    str = new String(buffer2, 0, i, Charsets.US_ASCII);
                                } catch (Throwable th5) {
                                    buffer = buffer2;
                                    th = th5;
                                    try {
                                        throw th;
                                    } catch (Throwable th6) {
                                        CloseableKt.closeFinally(fileInputStream, th);
                                        throw th6;
                                    }
                                }
                            } else {
                                str = "";
                            }
                            boolean zStartsWith$default = StringsKt.startsWith$default(str, "GET /ctrl-int/1/connect", false, 2, (Object) null);
                            String str2 = zStartsWith$default ? "200 OK" : "400 Bad Request";
                            FileOutputStream fileOutputStream = new FileOutputStream(accepted);
                            try {
                                FileOutputStream fileOutputStream2 = fileOutputStream;
                                buffer = buffer2;
                                try {
                                    try {
                                        byte[] bytes = ("HTTP/1.1 " + str2 + "\r\nContent-Length: 0\r\n\r\n").getBytes(Charsets.UTF_8);
                                        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
                                        fileOutputStream2.write(bytes);
                                        fileOutputStream2.flush();
                                        Unit unit = Unit.INSTANCE;
                                        try {
                                            CloseableKt.closeFinally(fileOutputStream, null);
                                            if (zStartsWith$default) {
                                                this$0.log.invoke("收到车机控制连接邀请");
                                            }
                                            Unit unit2 = Unit.INSTANCE;
                                            try {
                                                CloseableKt.closeFinally(fileInputStream, null);
                                                objM159constructorimpl3 = Result.m159constructorimpl(Unit.INSTANCE);
                                            } catch (Throwable th7) {
                                                th = th7;
                                                Result.Companion companion6 = Result.INSTANCE;
                                                objM159constructorimpl3 = Result.m159constructorimpl(ResultKt.createFailure(th));
                                                thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl3);
                                                if (thM162exceptionOrNullimpl != null) {
                                                    this$0.log.invoke("控制邀请处理失败：" + thM162exceptionOrNullimpl.getMessage());
                                                }
                                                Result.Companion companion7 = Result.INSTANCE;
                                                Os.close(accepted);
                                                Result.m159constructorimpl(Unit.INSTANCE);
                                                buffer2 = buffer;
                                            }
                                            thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl3);
                                            if (thM162exceptionOrNullimpl != null) {
                                                this$0.log.invoke("控制邀请处理失败：" + thM162exceptionOrNullimpl.getMessage());
                                            }
                                            try {
                                                Result.Companion companion8 = Result.INSTANCE;
                                                Os.close(accepted);
                                                Result.m159constructorimpl(Unit.INSTANCE);
                                                buffer2 = buffer;
                                            } catch (Throwable th8) {
                                                Result.Companion companion9 = Result.INSTANCE;
                                                Result.m159constructorimpl(ResultKt.createFailure(th8));
                                                buffer2 = buffer;
                                            }
                                        } catch (Throwable th9) {
                                            th = th9;
                                            throw th;
                                        }
                                    } catch (Throwable th10) {
                                        th2 = th10;
                                        try {
                                            throw th2;
                                        } catch (Throwable th11) {
                                            CloseableKt.closeFinally(fileOutputStream, th2);
                                            throw th11;
                                        }
                                    }
                                } catch (Throwable th12) {
                                    th2 = th12;
                                }
                            } catch (Throwable th13) {
                                buffer = buffer2;
                                th2 = th13;
                            }
                        } catch (Throwable th14) {
                            buffer = buffer2;
                            th = th14;
                        }
                    } catch (Throwable th15) {
                        th = th15;
                        buffer = buffer2;
                    }
                }
            }
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.closed.compareAndSet(false, true)) {
            try {
                Result.Companion companion = Result.INSTANCE;
                Os.shutdown(this.descriptor, OsConstants.SHUT_RDWR);
                Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            try {
                Result.Companion companion3 = Result.INSTANCE;
                Os.close(this.descriptor);
                Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th2) {
                Result.Companion companion4 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th2));
            }
            Thread thread = this.thread;
            if (thread != null) {
                thread.interrupt();
            }
        }
    }
}
