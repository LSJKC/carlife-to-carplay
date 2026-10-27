package com.carplay.mfi;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;

/* JADX INFO: compiled from: MfiAuthenticationClient.kt */
/* JADX INFO: loaded from: classes11.dex */
@Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u001d\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007R\u0015\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\n\u001a\u0004\b\b\u0010\t¨\u0006\u000b"}, d2 = {"Lcom/carplay/mfi/MfiAuthenticationFailedException;", "Lcom/carplay/mfi/MfiException;", "errorCode", "", "cause", "", "<init>", "(Ljava/lang/Integer;Ljava/lang/Throwable;)V", "getErrorCode", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MfiAuthenticationFailedException extends MfiException {
    private final Integer errorCode;

    public /* synthetic */ MfiAuthenticationFailedException(Integer num, Throwable th, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(num, (i & 2) != 0 ? null : th);
    }

    public final Integer getErrorCode() {
        return this.errorCode;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public MfiAuthenticationFailedException(Integer errorCode, Throwable cause) {
        String str;
        if (errorCode == null) {
            str = "MFi authentication failed (error code unavailable)";
        } else {
            String string = Integer.toString(errorCode.intValue(), CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            str = "MFi authentication failed with error 0x" + string;
        }
        super(str, cause, null);
        this.errorCode = errorCode;
    }
}
