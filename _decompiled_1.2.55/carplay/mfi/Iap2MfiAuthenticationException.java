package com.carplay.mfi;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2MfiAuthenticationClient.kt */
/* JADX INFO: loaded from: classes11.dex */
@Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"Lcom/carplay/mfi/Iap2MfiAuthenticationException;", "Lcom/carplay/mfi/MfiException;", "message", "", "cause", "", "<init>", "(Ljava/lang/String;Ljava/lang/Throwable;)V", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2MfiAuthenticationException extends MfiException {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public Iap2MfiAuthenticationException(String message, Throwable cause) {
        super(message, cause, null);
        Intrinsics.checkNotNullParameter(message, "message");
    }

    public /* synthetic */ Iap2MfiAuthenticationException(String str, Throwable th, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, (i & 2) != 0 ? null : th);
    }
}
