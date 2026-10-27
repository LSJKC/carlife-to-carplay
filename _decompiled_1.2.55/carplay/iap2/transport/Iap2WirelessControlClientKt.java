package com.carplay.iap2.transport;

import com.carplay.iap2.wire.Iap2Frame;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2WirelessControlClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u001e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a8\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00010\u00052\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0000¨\u0006\t"}, d2 = {"sendStartSession", "", "endpoint", "Lcom/carplay/iap2/transport/Iap2WirelessCarPlayEndpoint;", "send", "Lkotlin/Function1;", "Lcom/carplay/iap2/wire/Iap2Frame;", "onSent", "Lcom/carplay/iap2/transport/Iap2StartSessionSent;", "core-iap2"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class Iap2WirelessControlClientKt {
    public static final void sendStartSession(Iap2WirelessCarPlayEndpoint endpoint, Function1<? super Iap2Frame, Unit> send, Function1<? super Iap2StartSessionSent, Unit> onSent) {
        Intrinsics.checkNotNullParameter(endpoint, "endpoint");
        Intrinsics.checkNotNullParameter(send, "send");
        Intrinsics.checkNotNullParameter(onSent, "onSent");
        send.invoke(Iap2WirelessControlClient.INSTANCE.carPlayStartSession(endpoint));
        onSent.invoke(new Iap2StartSessionSent(System.nanoTime()));
    }
}
