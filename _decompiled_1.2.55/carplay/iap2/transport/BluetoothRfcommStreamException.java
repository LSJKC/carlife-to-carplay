package com.carplay.iap2.transport;

import java.io.IOException;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: BluetoothRfcommDuplexStream.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\t\u0018\u00002\u00020\u0001:\u0002\u000e\u000fB%\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0004\b\b\u0010\tR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u0010"}, d2 = {"Lcom/carplay/iap2/transport/BluetoothRfcommStreamException;", "Ljava/io/IOException;", "operation", "Lcom/carplay/iap2/transport/BluetoothRfcommStreamException$Operation;", "reason", "Lcom/carplay/iap2/transport/BluetoothRfcommStreamException$Reason;", "cause", "", "<init>", "(Lcom/carplay/iap2/transport/BluetoothRfcommStreamException$Operation;Lcom/carplay/iap2/transport/BluetoothRfcommStreamException$Reason;Ljava/lang/Throwable;)V", "getOperation", "()Lcom/carplay/iap2/transport/BluetoothRfcommStreamException$Operation;", "getReason", "()Lcom/carplay/iap2/transport/BluetoothRfcommStreamException$Reason;", "Operation", "Reason", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class BluetoothRfcommStreamException extends IOException {
    private final Operation operation;
    private final Reason reason;

    /* JADX INFO: compiled from: BluetoothRfcommDuplexStream.kt */
    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007¨\u0006\b"}, d2 = {"Lcom/carplay/iap2/transport/BluetoothRfcommStreamException$Operation;", "", "<init>", "(Ljava/lang/String;I)V", "INPUT_STREAM", "OUTPUT_STREAM", "READ", "WRITE", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public enum Operation {
        INPUT_STREAM,
        OUTPUT_STREAM,
        READ,
        WRITE;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

        public static EnumEntries<Operation> getEntries() {
            return $ENTRIES;
        }
    }

    /* JADX INFO: compiled from: BluetoothRfcommDuplexStream.kt */
    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007¨\u0006\b"}, d2 = {"Lcom/carplay/iap2/transport/BluetoothRfcommStreamException$Reason;", "", "<init>", "(Ljava/lang/String;I)V", "STREAM_UNAVAILABLE", "STREAM_ACCESS_FAILED", "READ_FAILED", "WRITE_FAILED", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public enum Reason {
        STREAM_UNAVAILABLE,
        STREAM_ACCESS_FAILED,
        READ_FAILED,
        WRITE_FAILED;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

        public static EnumEntries<Reason> getEntries() {
            return $ENTRIES;
        }
    }

    public /* synthetic */ BluetoothRfcommStreamException(Operation operation, Reason reason, Throwable th, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(operation, reason, (i & 4) != 0 ? null : th);
    }

    public final Operation getOperation() {
        return this.operation;
    }

    public final Reason getReason() {
        return this.reason;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public BluetoothRfcommStreamException(Operation operation, Reason reason, Throwable cause) {
        Intrinsics.checkNotNullParameter(operation, "operation");
        Intrinsics.checkNotNullParameter(reason, "reason");
        String lowerCase = operation.name().toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        String lowerCase2 = reason.name().toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(lowerCase2, "toLowerCase(...)");
        super("Bluetooth RFCOMM " + lowerCase + " failed: " + lowerCase2, cause);
        this.operation = operation;
        this.reason = reason;
    }
}
