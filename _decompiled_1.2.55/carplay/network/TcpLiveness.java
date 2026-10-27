package com.carplay.network;

import android.os.ParcelFileDescriptor;
import android.system.Os;
import android.system.OsConstants;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.FileDescriptor;
import java.net.Socket;
import java.net.SocketException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: TcpLiveness.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\"\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\t0\rR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u000f"}, d2 = {"Lcom/carplay/network/TcpLiveness;", "", "<init>", "()V", "TCP_KEEPIDLE", "", "TCP_KEEPINTVL", "TCP_KEEPCNT", "configure", "", "socket", "Ljava/net/Socket;", "diagnostic", "Lkotlin/Function1;", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class TcpLiveness {
    public static final TcpLiveness INSTANCE = new TcpLiveness();
    private static final int TCP_KEEPCNT = 6;
    private static final int TCP_KEEPIDLE = 4;
    private static final int TCP_KEEPINTVL = 5;

    private TcpLiveness() {
    }

    public final void configure(Socket socket, Function1<? super String, Unit> diagnostic) throws SocketException {
        Intrinsics.checkNotNullParameter(socket, "socket");
        Intrinsics.checkNotNullParameter(diagnostic, "diagnostic");
        socket.setKeepAlive(true);
        try {
            ParcelFileDescriptor parcelFileDescriptorFromSocket = ParcelFileDescriptor.fromSocket(socket);
            try {
                ParcelFileDescriptor duplicate = parcelFileDescriptorFromSocket;
                FileDescriptor fd = duplicate.getFileDescriptor();
                Os.setsockoptInt(fd, OsConstants.IPPROTO_TCP, 4, 10);
                Os.setsockoptInt(fd, OsConstants.IPPROTO_TCP, 5, 3);
                Os.setsockoptInt(fd, OsConstants.IPPROTO_TCP, 6, 3);
                Os.setsockoptInt(fd, OsConstants.IPPROTO_TCP, OsConstants.TCP_USER_TIMEOUT, AccessibilityNodeInfoCompat.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_MAX_LENGTH);
                diagnostic.invoke("TCP peer health enabled idle=10s interval=3s count=3 deadline=20000ms; no video-idle timeout");
                Unit unit = Unit.INSTANCE;
                CloseableKt.closeFinally(parcelFileDescriptorFromSocket, null);
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    CloseableKt.closeFinally(parcelFileDescriptorFromSocket, th);
                    throw th2;
                }
            }
        } catch (Exception failure) {
            diagnostic.invoke("TCP peer health tuning unavailable: " + failure.getClass().getSimpleName());
        }
    }
}
