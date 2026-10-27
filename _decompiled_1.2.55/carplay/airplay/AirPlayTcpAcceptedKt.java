package com.carplay.airplay;

import com.carlink.cast.CastSettings;
import java.net.InetAddress;
import java.net.NetworkInterface;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AirPlayTcpAccepted.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0000¨\u0006\u0005"}, d2 = {"isInternalAirPlayPeer", "", CastSettings.KEY_PEER, "Ljava/net/InetAddress;", "local", "core-airplay"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class AirPlayTcpAcceptedKt {
    public static final boolean isInternalAirPlayPeer(InetAddress peer, InetAddress local) {
        Object objM159constructorimpl;
        Intrinsics.checkNotNullParameter(peer, "peer");
        Intrinsics.checkNotNullParameter(local, "local");
        if (peer.isLoopbackAddress() || Intrinsics.areEqual(peer, local)) {
            return true;
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(Boolean.valueOf(NetworkInterface.getByInetAddress(peer) != null));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = true;
        }
        return ((Boolean) objM159constructorimpl).booleanValue();
    }
}
