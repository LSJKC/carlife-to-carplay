package com.carplay.sender;

import java.security.cert.X509Certificate;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AccessoryTrust.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J \u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u0005H\u0016¨\u0006\u000bÀ\u0006\u0003"}, d2 = {"Lcom/carplay/sender/AccessoryAuthentication;", "", "validate", "Ljava/security/cert/X509Certificate;", "encoded", "", "verifyAuthentication", "", "certificate", "challenge", "response", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface AccessoryAuthentication {
    X509Certificate validate(byte[] encoded);

    /* JADX INFO: compiled from: AccessoryTrust.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final class DefaultImpls {
        @Deprecated
        public static boolean verifyAuthentication(AccessoryAuthentication $this, X509Certificate certificate, byte[] challenge, byte[] response) {
            Intrinsics.checkNotNullParameter(certificate, "certificate");
            Intrinsics.checkNotNullParameter(challenge, "challenge");
            Intrinsics.checkNotNullParameter(response, "response");
            return AccessoryAuthentication.super.verifyAuthentication(certificate, challenge, response);
        }
    }

    default boolean verifyAuthentication(X509Certificate certificate, byte[] challenge, byte[] response) {
        Intrinsics.checkNotNullParameter(certificate, "certificate");
        Intrinsics.checkNotNullParameter(challenge, "challenge");
        Intrinsics.checkNotNullParameter(response, "response");
        return AccessoryTrust.INSTANCE.verifyChallenge(certificate, challenge, response);
    }
}
