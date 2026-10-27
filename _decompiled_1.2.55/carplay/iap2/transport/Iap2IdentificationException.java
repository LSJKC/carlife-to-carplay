package com.carplay.iap2.transport;

import java.io.IOException;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Iap2IdentificationClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0002\u0006\u0007B\u0011\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005\u0082\u0001\u0002\b\t¨\u0006\n"}, d2 = {"Lcom/carplay/iap2/transport/Iap2IdentificationException;", "Ljava/io/IOException;", "message", "", "<init>", "(Ljava/lang/String;)V", "Rejected", "UnexpectedMessage", "Lcom/carplay/iap2/transport/Iap2IdentificationException$Rejected;", "Lcom/carplay/iap2/transport/Iap2IdentificationException$UnexpectedMessage;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public abstract class Iap2IdentificationException extends IOException {
    public /* synthetic */ Iap2IdentificationException(String str, DefaultConstructorMarker defaultConstructorMarker) {
        this(str);
    }

    /* JADX INFO: compiled from: Iap2IdentificationClient.kt */
    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0015\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\u0004\b\u0005\u0010\u0006R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"Lcom/carplay/iap2/transport/Iap2IdentificationException$Rejected;", "Lcom/carplay/iap2/transport/Iap2IdentificationException;", "parameterIds", "", "", "<init>", "(Ljava/util/Set;)V", "getParameterIds", "()Ljava/util/Set;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Rejected extends Iap2IdentificationException {
        private final Set<Integer> parameterIds;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public Rejected(Set<Integer> parameterIds) {
            super("iAP2 identification rejected parameters " + CollectionsKt.joinToString$default(CollectionsKt.sorted(parameterIds), null, "[", "]", 0, null, new Function1() { // from class: com.carplay.iap2.transport.Iap2IdentificationException$Rejected$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2IdentificationException.Rejected._init_$lambda$0(((Integer) obj).intValue());
                }
            }, 25, null) + "; this minimal identification profile has no optional components to remove", null);
            Intrinsics.checkNotNullParameter(parameterIds, "parameterIds");
            this.parameterIds = CollectionsKt.toSet(parameterIds);
        }

        static final CharSequence _init_$lambda$0(int it) {
            String string = Integer.toString(it, CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            return "0x" + StringsKt.padStart(string, 4, '0');
        }

        public final Set<Integer> getParameterIds() {
            return this.parameterIds;
        }
    }

    private Iap2IdentificationException(String message) {
        super(message);
    }

    /* JADX INFO: compiled from: Iap2IdentificationClient.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005¨\u0006\u0006"}, d2 = {"Lcom/carplay/iap2/transport/Iap2IdentificationException$UnexpectedMessage;", "Lcom/carplay/iap2/transport/Iap2IdentificationException;", "messageId", "", "<init>", "(I)V", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class UnexpectedMessage extends Iap2IdentificationException {
        /* JADX WARN: Illegal instructions before constructor call */
        public UnexpectedMessage(int messageId) {
            String string = Integer.toString(messageId, CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            super("Unexpected iAP2 identification message 0x" + StringsKt.padStart(string, 4, '0'), null);
        }
    }
}
