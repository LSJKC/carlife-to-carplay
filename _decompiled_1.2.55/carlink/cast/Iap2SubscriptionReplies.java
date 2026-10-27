package com.carlink.cast;

import com.carplay.iap2.body.Iap2BodyBuilder;
import com.carplay.iap2.message.Iap2Messages;
import com.carplay.iap2.wire.Iap2Frame;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;

/* JADX INFO: compiled from: Iap2SubscriptionReplies.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0000\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u0007J\u0018\u0010\b\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\nH\u0002J\f\u0010\u000b\u001a\u00020\f*\u00020\nH\u0002¨\u0006\r"}, d2 = {"Lcom/carlink/cast/Iap2SubscriptionReplies;", "", "<init>", "()V", "forRequest", "Lcom/carplay/iap2/wire/Iap2Frame;", "messageId", "", "frame", "fullFrameHex", "", "hexBytes", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2SubscriptionReplies {
    public static final Iap2SubscriptionReplies INSTANCE = new Iap2SubscriptionReplies();

    private Iap2SubscriptionReplies() {
    }

    public final Iap2Frame forRequest(int messageId) {
        switch (messageId) {
            case 16724:
                Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
                Iap2BodyBuilder iap2BodyBuilderOpaque = Iap2BodyBuilder.INSTANCE.opaque(16725);
                iap2BodyBuilderOpaque.u8(2, 0);
                return iap2BodyBuilderOpaque.build();
            case 16727:
                return frame(16728, "404000644158000500000100050001060005000200000e0004e697a053494de58da100000500050100050006010005000700000500080000050009000005000a000005000b000005000c010005000d000005000e000005000f0000050010000005001100");
            case 16752:
                Iap2Messages iap2Messages2 = Iap2Messages.INSTANCE;
                Iap2BodyBuilder iap2BodyBuilderOpaque2 = Iap2BodyBuilder.INSTANCE.opaque(16753);
                iap2BodyBuilderOpaque2.u8(0, 0);
                iap2BodyBuilderOpaque2.u16(2, 0);
                iap2BodyBuilderOpaque2.u8(5, 0);
                iap2BodyBuilderOpaque2.u16(7, 0);
                return iap2BodyBuilderOpaque2.build();
            default:
                return null;
        }
    }

    private final Iap2Frame frame(int messageId, String fullFrameHex) {
        byte[] bytes = hexBytes(fullFrameHex);
        if (!(bytes.length >= 6)) {
            throw new IllegalArgumentException("CSM frame too short".toString());
        }
        int declared = ((bytes[2] & UByte.MAX_VALUE) << 8) | (bytes[3] & UByte.MAX_VALUE);
        if (declared == bytes.length) {
            return new Iap2Frame(messageId, ArraysKt.copyOfRange(bytes, 6, bytes.length));
        }
        throw new IllegalArgumentException("CSM frame length mismatch".toString());
    }

    private final byte[] hexBytes(String $this$hexBytes) {
        if (!($this$hexBytes.length() % 2 == 0)) {
            throw new IllegalArgumentException("hex must contain whole bytes".toString());
        }
        int length = $this$hexBytes.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            String strSubstring = $this$hexBytes.substring(i2, i2 + 2);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
            bArr[i] = (byte) Integer.parseInt(strSubstring, CharsKt.checkRadix(16));
        }
        return bArr;
    }
}
