package com.carplay.mfi;

import com.carplay.iap2.message.Iap2AuthenticationMessages;
import com.carplay.iap2.session.Iap2Session;
import com.carplay.iap2.wire.Iap2Frame;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Iap2MfiAuthenticationClient.kt */
/* JADX INFO: loaded from: classes11.dex */
@Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0004\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019B\u0019\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J.\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\u0014\b\u0002\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\t0\u000fJ \u0010\u0011\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\rH\u0002J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010\u0017\u001a\u00020\r2\u0006\u0010\f\u001a\u00020\rH\u0002J\u0010\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0014\u001a\u00020\rH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Lcom/carplay/mfi/Iap2MfiAuthenticationClient;", "", "authentication", "Lcom/carplay/mfi/MfiAuthenticator;", "maximumCertificateLength", "", "<init>", "(Lcom/carplay/mfi/MfiAuthenticator;I)V", "run", "", "session", "Lcom/carplay/iap2/session/Iap2Session;", "timeoutMillis", "", "onProgress", "Lkotlin/Function1;", "", "send", "frame", "Lcom/carplay/iap2/wire/Iap2Frame;", "deadlineNanos", "requireParameterZero", "", "deadlineAfter", "remainingMillis", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2MfiAuthenticationClient {
    private static final int AUTHENTICATION_FAILED = 43524;
    private static final int AUTHENTICATION_SUCCEEDED = 43525;
    private static final int CERTIFICATE = 43521;
    private static final long DEFAULT_TIMEOUT_MILLIS = 30000;
    private static final int MAXIMUM_CHALLENGE_BYTES = 128;
    private static final int MAX_CSM_CERTIFICATE_BYTES = 65525;
    private static final long MAX_TIMEOUT_MILLIS = 300000;
    private static final long NANOS_PER_MILLISECOND = 1000000;
    private static final int REQUEST_CERTIFICATE = 43520;
    private static final int REQUEST_CHALLENGE = 43522;
    private static final int RESPONSE = 43523;
    private final MfiAuthenticator authentication;
    private final int maximumCertificateLength;

    /* JADX INFO: compiled from: Iap2MfiAuthenticationClient.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[MfiCertificateType.values().length];
            try {
                iArr[MfiCertificateType.MFI.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[MfiCertificateType.BAA.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public Iap2MfiAuthenticationClient(MfiAuthenticator authentication, int maximumCertificateLength) {
        Intrinsics.checkNotNullParameter(authentication, "authentication");
        this.authentication = authentication;
        this.maximumCertificateLength = maximumCertificateLength;
        int i = this.maximumCertificateLength;
        boolean z = false;
        if (1 <= i && i < 65526) {
            z = true;
        }
        if (z) {
        } else {
            throw new IllegalArgumentException("maximumCertificateLength must be in 1..65525".toString());
        }
    }

    public /* synthetic */ Iap2MfiAuthenticationClient(MfiAuthenticator mfiAuthenticator, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(mfiAuthenticator, (i2 & 2) != 0 ? 65525 : i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void run$default(Iap2MfiAuthenticationClient iap2MfiAuthenticationClient, Iap2Session iap2Session, long j, Function1 function1, int i, Object obj) {
        if ((i & 2) != 0) {
            j = 30000;
        }
        if ((i & 4) != 0) {
            function1 = new Function1() { // from class: com.carplay.mfi.Iap2MfiAuthenticationClient$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj2) {
                    return Iap2MfiAuthenticationClient.run$lambda$1((String) obj2);
                }
            };
        }
        iap2MfiAuthenticationClient.run(iap2Session, j, function1);
    }

    static final Unit run$lambda$1(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    public final void run(Iap2Session session, long timeoutMillis, Function1<? super String, Unit> onProgress) {
        Iap2Frame certificateFrame;
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(onProgress, "onProgress");
        if (!(1 <= timeoutMillis && timeoutMillis < 300001)) {
            throw new IllegalArgumentException("timeoutMillis must be in 1..300000".toString());
        }
        long deadlineNanos = deadlineAfter(timeoutMillis);
        byte[] certificate = this.authentication.readCertificate(this.maximumCertificateLength);
        switch (WhenMappings.$EnumSwitchMapping$0[this.authentication.getCertificateType().ordinal()]) {
            case 1:
                certificateFrame = Iap2AuthenticationMessages.INSTANCE.accessoryCertificate(certificate);
                break;
            case 2:
                certificateFrame = Iap2AuthenticationMessages.INSTANCE.accessoryCertificateBody(certificate);
                break;
            default:
                throw new NoWhenBranchMatchedException();
        }
        int certificatePayloadBytes = certificateFrame.getPayload().length;
        onProgress.invoke("mfi type=" + this.authentication.getCertificateType() + " certificate loaded bytes=" + certificate.length + " payload bytes=" + certificatePayloadBytes);
        while (true) {
            long remaining = remainingMillis(deadlineNanos);
            if (remaining == 0) {
                throw new Iap2MfiAuthenticationException("Timed out waiting for iAP2 MFi authentication", null, 2, null);
            }
            Iap2Frame frame = session.recv(remaining);
            if (frame == null) {
                throw new Iap2MfiAuthenticationException("Timed out waiting for iAP2 MFi authentication", null, 2, null);
            }
            switch (frame.getMessageId()) {
                case REQUEST_CERTIFICATE /* 43520 */:
                    onProgress.invoke("iap2 mfi rx=0xaa00 request-certificate");
                    send(session, certificateFrame, deadlineNanos);
                    onProgress.invoke("iap2 mfi tx=0xaa01 certificate bytes=" + certificatePayloadBytes);
                    break;
                case CERTIFICATE /* 43521 */:
                case RESPONSE /* 43523 */:
                default:
                    String string = Integer.toString(frame.getMessageId(), CharsKt.checkRadix(16));
                    Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                    throw new Iap2MfiAuthenticationException("Unexpected iAP2 MFi message 0x" + StringsKt.padStart(string, 4, '0'), null, 2, null);
                case REQUEST_CHALLENGE /* 43522 */:
                    byte[] challenge = requireParameterZero(frame);
                    int length = challenge.length;
                    if (!(1 <= length && length < 129)) {
                        throw new Iap2MfiAuthenticationException("Invalid iAP2 MFi challenge length " + challenge.length, null, 2, null);
                    }
                    onProgress.invoke("iap2 mfi rx=0xaa02 challenge bytes=" + challenge.length);
                    byte[] signature = this.authentication.signChallenge(challenge);
                    send(session, Iap2AuthenticationMessages.INSTANCE.response(signature), deadlineNanos);
                    onProgress.invoke("iap2 mfi tx=0xaa03 signature bytes=" + signature.length);
                    break;
                    break;
                case AUTHENTICATION_FAILED /* 43524 */:
                    throw new Iap2MfiAuthenticationException("iPhone sent AuthenticationFailed", null, 2, null);
                case AUTHENTICATION_SUCCEEDED /* 43525 */:
                    onProgress.invoke("iap2 mfi rx=0xaa05 authentication-succeeded");
                    return;
            }
        }
    }

    private final void send(Iap2Session session, Iap2Frame frame, long deadlineNanos) throws Iap2MfiAuthenticationException {
        long remaining = remainingMillis(deadlineNanos);
        if (remaining == 0) {
            throw new Iap2MfiAuthenticationException("Timed out sending iAP2 MFi authentication reply", null, 2, null);
        }
        session.send(frame, remaining);
    }

    private final byte[] requireParameterZero(Iap2Frame frame) throws Iap2MfiAuthenticationException {
        try {
            return Iap2AuthenticationMessages.INSTANCE.challenge(frame);
        } catch (Exception failure) {
            throw new Iap2MfiAuthenticationException("iAP2 MFi challenge is missing parameter 0", failure);
        }
    }

    private final long deadlineAfter(long timeoutMillis) {
        long now = System.nanoTime();
        long delta = 1000000 * timeoutMillis;
        if (Long.MAX_VALUE - now < delta) {
            return Long.MAX_VALUE;
        }
        return now + delta;
    }

    private final long remainingMillis(long deadlineNanos) {
        long remaining = deadlineNanos - System.nanoTime();
        if (remaining <= 0) {
            return 0L;
        }
        return RangesKt.coerceAtMost(((remaining + 1000000) - 1) / 1000000, 300000L);
    }
}
