package com.carplay.iap2.transport;

import java.nio.ByteBuffer;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: UsbReadQueuePolicy.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0000\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J0\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00100\u000fR\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0006¨\u0006\u0012"}, d2 = {"Lcom/carplay/iap2/transport/UsbReadQueuePolicy;", "", "<init>", "()V", "successfulLimit", "", "Ljava/lang/Integer;", "queue", "Lcom/carplay/iap2/transport/UsbReadQueueResult;", "buffer", "Ljava/nio/ByteBuffer;", "checkOpen", "Lkotlin/Function0;", "", "submit", "Lkotlin/Function1;", "", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class UsbReadQueuePolicy {

    @Deprecated
    public static final int COMPATIBILITY_BYTES = 16384;
    private static final Companion Companion = new Companion(null);
    private Integer successfulLimit;

    public final UsbReadQueueResult queue(ByteBuffer buffer, Function0<Unit> checkOpen, Function1<? super ByteBuffer, Boolean> submit) {
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        Intrinsics.checkNotNullParameter(checkOpen, "checkOpen");
        Intrinsics.checkNotNullParameter(submit, "submit");
        if (!(buffer.isDirect() && !buffer.isReadOnly())) {
            throw new IllegalArgumentException("USB read requires a writable direct buffer".toString());
        }
        checkOpen.invoke();
        int position = buffer.position();
        int originalLimit = buffer.limit();
        int iRemaining = buffer.remaining();
        Integer num = this.successfulLimit;
        int firstBytes = Math.min(iRemaining, num != null ? num.intValue() : buffer.remaining());
        buffer.limit(position + firstBytes);
        if (submit.invoke(buffer).booleanValue()) {
            return new UsbReadQueueResult(true, firstBytes, null, 4, null);
        }
        if (!(buffer.position() == position && buffer.limit() == position + firstBytes)) {
            throw new IllegalStateException("Rejected USB queue changed its buffer state".toString());
        }
        if (firstBytes <= 16384) {
            buffer.limit(originalLimit);
            return new UsbReadQueueResult(false, firstBytes, null, 4, null);
        }
        checkOpen.invoke();
        buffer.limit(position + 16384);
        boolean queued = submit.invoke(buffer).booleanValue();
        if (queued) {
            this.successfulLimit = 16384;
        } else {
            buffer.limit(originalLimit);
        }
        return new UsbReadQueueResult(queued, firstBytes, 16384);
    }

    /* JADX INFO: compiled from: UsbReadQueuePolicy.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lcom/carplay/iap2/transport/UsbReadQueuePolicy$Companion;", "", "<init>", "()V", "COMPATIBILITY_BYTES", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
