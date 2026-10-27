package com.carplay.iap2.transport;

import java.io.IOException;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: I2cTransport.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0006\b\t\n\u000b\f\rB\u001d\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007\u0082\u0001\u0006\u000e\u000f\u0010\u0011\u0012\u0013¨\u0006\u0014"}, d2 = {"Lcom/carplay/iap2/transport/I2cTransportException;", "Ljava/io/IOException;", "message", "", "cause", "", "<init>", "(Ljava/lang/String;Ljava/lang/Throwable;)V", "InvalidRequest", "DeviceUnavailable", "PermissionDenied", "TimedOut", "Nack", "Protocol", "Lcom/carplay/iap2/transport/I2cTransportException$DeviceUnavailable;", "Lcom/carplay/iap2/transport/I2cTransportException$InvalidRequest;", "Lcom/carplay/iap2/transport/I2cTransportException$Nack;", "Lcom/carplay/iap2/transport/I2cTransportException$PermissionDenied;", "Lcom/carplay/iap2/transport/I2cTransportException$Protocol;", "Lcom/carplay/iap2/transport/I2cTransportException$TimedOut;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public abstract class I2cTransportException extends IOException {
    public /* synthetic */ I2cTransportException(String str, Throwable th, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, th);
    }

    /* JADX INFO: compiled from: I2cTransport.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005¨\u0006\u0006"}, d2 = {"Lcom/carplay/iap2/transport/I2cTransportException$InvalidRequest;", "Lcom/carplay/iap2/transport/I2cTransportException;", "message", "", "<init>", "(Ljava/lang/String;)V", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class InvalidRequest extends I2cTransportException {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public InvalidRequest(String message) {
            super(message, null, 2, 0 == true ? 1 : 0);
            Intrinsics.checkNotNullParameter(message, "message");
        }
    }

    private I2cTransportException(String message, Throwable cause) {
        super(message, cause);
    }

    public /* synthetic */ I2cTransportException(String str, Throwable th, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, (i & 2) != 0 ? null : th, null);
    }

    /* JADX INFO: compiled from: I2cTransport.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"Lcom/carplay/iap2/transport/I2cTransportException$DeviceUnavailable;", "Lcom/carplay/iap2/transport/I2cTransportException;", "message", "", "cause", "", "<init>", "(Ljava/lang/String;Ljava/lang/Throwable;)V", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class DeviceUnavailable extends I2cTransportException {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public DeviceUnavailable(String message, Throwable cause) {
            super(message, cause, null);
            Intrinsics.checkNotNullParameter(message, "message");
        }

        public /* synthetic */ DeviceUnavailable(String str, Throwable th, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, (i & 2) != 0 ? null : th);
        }
    }

    /* JADX INFO: compiled from: I2cTransport.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005¨\u0006\u0006"}, d2 = {"Lcom/carplay/iap2/transport/I2cTransportException$PermissionDenied;", "Lcom/carplay/iap2/transport/I2cTransportException;", "message", "", "<init>", "(Ljava/lang/String;)V", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class PermissionDenied extends I2cTransportException {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public PermissionDenied(String message) {
            super(message, null, 2, 0 == true ? 1 : 0);
            Intrinsics.checkNotNullParameter(message, "message");
        }
    }

    /* JADX INFO: compiled from: I2cTransport.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"Lcom/carplay/iap2/transport/I2cTransportException$TimedOut;", "Lcom/carplay/iap2/transport/I2cTransportException;", "message", "", "cause", "", "<init>", "(Ljava/lang/String;Ljava/lang/Throwable;)V", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class TimedOut extends I2cTransportException {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public TimedOut(String message, Throwable cause) {
            super(message, cause, null);
            Intrinsics.checkNotNullParameter(message, "message");
        }

        public /* synthetic */ TimedOut(String str, Throwable th, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, (i & 2) != 0 ? null : th);
        }
    }

    /* JADX INFO: compiled from: I2cTransport.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005¨\u0006\u0006"}, d2 = {"Lcom/carplay/iap2/transport/I2cTransportException$Nack;", "Lcom/carplay/iap2/transport/I2cTransportException;", "message", "", "<init>", "(Ljava/lang/String;)V", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Nack extends I2cTransportException {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public Nack(String message) {
            super(message, null, 2, 0 == true ? 1 : 0);
            Intrinsics.checkNotNullParameter(message, "message");
        }
    }

    /* JADX INFO: compiled from: I2cTransport.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"Lcom/carplay/iap2/transport/I2cTransportException$Protocol;", "Lcom/carplay/iap2/transport/I2cTransportException;", "message", "", "cause", "", "<init>", "(Ljava/lang/String;Ljava/lang/Throwable;)V", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Protocol extends I2cTransportException {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public Protocol(String message, Throwable cause) {
            super(message, cause, null);
            Intrinsics.checkNotNullParameter(message, "message");
        }

        public /* synthetic */ Protocol(String str, Throwable th, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(str, (i & 2) != 0 ? null : th);
        }
    }
}
