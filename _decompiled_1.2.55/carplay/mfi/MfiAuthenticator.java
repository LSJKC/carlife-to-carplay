package com.carplay.mfi;

import kotlin.Metadata;

/* JADX INFO: compiled from: MfiAuthenticationClient.kt */
/* JADX INFO: loaded from: classes11.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0006\u001a\u00020\u0007H&J\u0012\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u0007H&J\u0010\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\tH&J\b\u0010\r\u001a\u00020\u000eH\u0016R\u0014\u0010\u0002\u001a\u00020\u00038VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005¨\u0006\u000fÀ\u0006\u0003"}, d2 = {"Lcom/carplay/mfi/MfiAuthenticator;", "", "certificateType", "Lcom/carplay/mfi/MfiCertificateType;", "getCertificateType", "()Lcom/carplay/mfi/MfiCertificateType;", "protocolMajor", "", "readCertificate", "", "maximumOutputLength", "signChallenge", "challenge", "baaCertificates", "Lcom/carplay/mfi/BaaCertificatePair;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface MfiAuthenticator {
    int protocolMajor();

    byte[] readCertificate(int maximumOutputLength);

    byte[] signChallenge(byte[] challenge);

    /* JADX INFO: compiled from: MfiAuthenticationClient.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final class DefaultImpls {
        @Deprecated
        public static MfiCertificateType getCertificateType(MfiAuthenticator $this) {
            return MfiAuthenticator.super.getCertificateType();
        }

        @Deprecated
        public static BaaCertificatePair baaCertificates(MfiAuthenticator $this) {
            return MfiAuthenticator.super.baaCertificates();
        }
    }

    default MfiCertificateType getCertificateType() {
        return MfiCertificateType.MFI;
    }

    static /* synthetic */ byte[] readCertificate$default(MfiAuthenticator mfiAuthenticator, int i, int i2, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: readCertificate");
        }
        if ((i2 & 1) != 0) {
            i = 65525;
        }
        return mfiAuthenticator.readCertificate(i);
    }

    default BaaCertificatePair baaCertificates() throws MfiInvalidDataException {
        throw new MfiInvalidDataException("Authenticator does not provide BAA certificates", null, 2, null);
    }
}
