package com.carlink.cast;

import com.carplay.sender.AccessoryAuthentication;
import java.security.cert.X509Certificate;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AirPlayLink.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
final /* synthetic */ class AirPlayLink$events$1$iapControl$1$control$1 extends FunctionReferenceImpl implements Function1<byte[], X509Certificate> {
    AirPlayLink$events$1$iapControl$1$control$1(Object obj) {
        super(1, obj, AccessoryAuthentication.class, "validate", "validate([B)Ljava/security/cert/X509Certificate;", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public final X509Certificate invoke(byte[] p0) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        return ((AccessoryAuthentication) this.receiver).validate(p0);
    }
}
