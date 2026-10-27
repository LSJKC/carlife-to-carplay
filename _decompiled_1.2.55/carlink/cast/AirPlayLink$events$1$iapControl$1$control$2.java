package com.carlink.cast;

import com.carplay.sender.AccessoryAuthentication;
import java.security.cert.X509Certificate;
import kotlin.Metadata;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AirPlayLink.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
final /* synthetic */ class AirPlayLink$events$1$iapControl$1$control$2 extends FunctionReferenceImpl implements Function3<X509Certificate, byte[], byte[], Boolean> {
    AirPlayLink$events$1$iapControl$1$control$2(Object obj) {
        super(3, obj, AccessoryAuthentication.class, "verifyAuthentication", "verifyAuthentication(Ljava/security/cert/X509Certificate;[B[B)Z", 0);
    }

    @Override // kotlin.jvm.functions.Function3
    public final Boolean invoke(X509Certificate p0, byte[] p1, byte[] p2) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        Intrinsics.checkNotNullParameter(p1, "p1");
        Intrinsics.checkNotNullParameter(p2, "p2");
        return Boolean.valueOf(((AccessoryAuthentication) this.receiver).verifyAuthentication(p0, p1, p2));
    }
}
