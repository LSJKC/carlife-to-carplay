package com.carplay.iap2.message;

import com.carplay.iap2.body.Iap2BodyBuilder;
import com.carplay.iap2.catalog.Iap2Endpoint;
import com.carplay.iap2.catalog.Iap2Endpoints;
import com.carplay.iap2.wire.Iap2Frame;
import com.carplay.iap2.wire.Iap2ParameterList;
import com.carplay.iap2.wire.Iap2ProtocolException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;

/* JADX INFO: compiled from: Iap2Messages.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\n\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007J\u000e\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u0007J\u0016\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007J\u000e\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u0007J\u000e\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u0005¨\u0006\u0011"}, d2 = {"Lcom/carplay/iap2/message/Iap2AuthenticationMessages;", "", "<init>", "()V", "accessoryCertificate", "Lcom/carplay/iap2/wire/Iap2Frame;", "certificate", "", "accessoryCertificateBody", "body", "baaCertificatePackage", "leaf", "intermediate", "response", "signature", "challenge", "frame", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2AuthenticationMessages {
    public static final Iap2AuthenticationMessages INSTANCE = new Iap2AuthenticationMessages();

    private Iap2AuthenticationMessages() {
    }

    public final Iap2Frame accessoryCertificate(byte[] certificate) {
        Intrinsics.checkNotNullParameter(certificate, "certificate");
        Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
        Iap2Endpoint endpoint$iv = Iap2Endpoints.INSTANCE.getAUTHENTICATION_CERTIFICATE();
        Iap2BodyBuilder $this$accessoryCertificate_u24lambda_u240 = Iap2BodyBuilder.INSTANCE.of(endpoint$iv);
        $this$accessoryCertificate_u24lambda_u240.bytes(0, certificate);
        return $this$accessoryCertificate_u24lambda_u240.build();
    }

    public final Iap2Frame accessoryCertificateBody(byte[] body) throws Iap2ProtocolException {
        Intrinsics.checkNotNullParameter(body, "body");
        Iap2ParameterList.INSTANCE.parse(body);
        return new Iap2Frame(Iap2Endpoints.INSTANCE.getAUTHENTICATION_CERTIFICATE().getId(), body);
    }

    public final Iap2Frame baaCertificatePackage(byte[] leaf, byte[] intermediate) {
        Intrinsics.checkNotNullParameter(leaf, "leaf");
        Intrinsics.checkNotNullParameter(intermediate, "intermediate");
        Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
        int messageId$iv = Iap2Endpoints.INSTANCE.getAUTHENTICATION_CERTIFICATE().getId();
        Iap2BodyBuilder $this$baaCertificatePackage_u24lambda_u241 = Iap2BodyBuilder.INSTANCE.opaque(messageId$iv);
        $this$baaCertificatePackage_u24lambda_u241.bytes(0, leaf);
        $this$baaCertificatePackage_u24lambda_u241.u8(1, 1);
        $this$baaCertificatePackage_u24lambda_u241.bytes(2, intermediate);
        return $this$baaCertificatePackage_u24lambda_u241.build();
    }

    public final Iap2Frame response(byte[] signature) {
        Intrinsics.checkNotNullParameter(signature, "signature");
        Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
        Iap2Endpoint endpoint$iv = Iap2Endpoints.INSTANCE.getAUTHENTICATION_RESPONSE();
        Iap2BodyBuilder $this$response_u24lambda_u242 = Iap2BodyBuilder.INSTANCE.of(endpoint$iv);
        $this$response_u24lambda_u242.bytes(0, signature);
        return $this$response_u24lambda_u242.build();
    }

    public final byte[] challenge(Iap2Frame frame) {
        Intrinsics.checkNotNullParameter(frame, "frame");
        if (!(frame.getMessageId() == Iap2Endpoints.INSTANCE.getREQUEST_AUTHENTICATION_CHALLENGE_RESPONSE().getId())) {
            String string = Integer.toString(frame.getMessageId(), CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            throw new IllegalArgumentException(("Expected RequestAuthenticationChallengeResponse, got 0x" + string).toString());
        }
        return Iap2Messages.INSTANCE.reader(frame).bytes(0);
    }
}
