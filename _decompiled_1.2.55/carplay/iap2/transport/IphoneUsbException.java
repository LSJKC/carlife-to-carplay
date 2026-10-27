package com.carplay.iap2.transport;

import java.io.IOException;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: IphoneUsbHost.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0004\b\t\n\u000bB\u001d\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007\u0082\u0001\u0004\f\r\u000e\u000f¨\u0006\u0010"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbException;", "Ljava/io/IOException;", "message", "", "cause", "", "<init>", "(Ljava/lang/String;Ljava/lang/Throwable;)V", "PermissionDenied", "DeviceUnavailable", "TimedOut", "Protocol", "Lcom/carplay/iap2/transport/IphoneUsbException$DeviceUnavailable;", "Lcom/carplay/iap2/transport/IphoneUsbException$PermissionDenied;", "Lcom/carplay/iap2/transport/IphoneUsbException$Protocol;", "Lcom/carplay/iap2/transport/IphoneUsbException$TimedOut;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public abstract class IphoneUsbException extends IOException {
    public /* synthetic */ IphoneUsbException(String str, Throwable th, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, th);
    }

    /* JADX INFO: compiled from: IphoneUsbHost.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbException$PermissionDenied;", "Lcom/carplay/iap2/transport/IphoneUsbException;", "message", "", "cause", "", "<init>", "(Ljava/lang/String;Ljava/lang/Throwable;)V", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class PermissionDenied extends IphoneUsbException {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public PermissionDenied(String message, Throwable cause) {
            super(message, cause, null);
            Intrinsics.checkNotNullParameter(message, "message");
        }

        public /* synthetic */ PermissionDenied(String str, Throwable th, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, (i & 2) != 0 ? null : th);
        }
    }

    private IphoneUsbException(String message, Throwable cause) {
        super(message, cause);
    }

    public /* synthetic */ IphoneUsbException(String str, Throwable th, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, (i & 2) != 0 ? null : th, null);
    }

    /* JADX INFO: compiled from: IphoneUsbHost.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbException$DeviceUnavailable;", "Lcom/carplay/iap2/transport/IphoneUsbException;", "message", "", "cause", "", "<init>", "(Ljava/lang/String;Ljava/lang/Throwable;)V", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class DeviceUnavailable extends IphoneUsbException {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public DeviceUnavailable(String message, Throwable cause) {
            super(message, cause, null);
            Intrinsics.checkNotNullParameter(message, "message");
        }

        public /* synthetic */ DeviceUnavailable(String str, Throwable th, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, (i & 2) != 0 ? null : th);
        }
    }

    /* JADX INFO: compiled from: IphoneUsbHost.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbException$TimedOut;", "Lcom/carplay/iap2/transport/IphoneUsbException;", "message", "", "cause", "", "<init>", "(Ljava/lang/String;Ljava/lang/Throwable;)V", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class TimedOut extends IphoneUsbException {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public TimedOut(String message, Throwable cause) {
            super(message, cause, null);
            Intrinsics.checkNotNullParameter(message, "message");
        }

        public /* synthetic */ TimedOut(String str, Throwable th, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, (i & 2) != 0 ? null : th);
        }
    }

    /* JADX INFO: compiled from: IphoneUsbHost.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005¨\u0006\u0006"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbException$Protocol;", "Lcom/carplay/iap2/transport/IphoneUsbException;", "message", "", "<init>", "(Ljava/lang/String;)V", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Protocol extends IphoneUsbException {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public Protocol(String message) {
            super(message, null, 2, 0 == true ? 1 : 0);
            Intrinsics.checkNotNullParameter(message, "message");
        }
    }
}
