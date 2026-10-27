package com.carplay.iap2.message;

import com.carplay.iap2.body.Iap2BodyBuilder;
import com.carplay.iap2.body.Iap2BodyReader;
import com.carplay.iap2.catalog.Iap2Endpoint;
import com.carplay.iap2.wire.Iap2Frame;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2Messages.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J-\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0017\u0010\b\u001a\u0013\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\t¢\u0006\u0002\b\fH\u0086\bø\u0001\u0000J-\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0017\u0010\b\u001a\u0013\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\t¢\u0006\u0002\b\fH\u0086\bø\u0001\u0000J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0005\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0013"}, d2 = {"Lcom/carplay/iap2/message/Iap2Messages;", "", "<init>", "()V", "build", "Lcom/carplay/iap2/wire/Iap2Frame;", "endpoint", "Lcom/carplay/iap2/catalog/Iap2Endpoint;", "block", "Lkotlin/Function1;", "Lcom/carplay/iap2/body/Iap2BodyBuilder;", "", "Lkotlin/ExtensionFunctionType;", "buildRaw", "messageId", "", "reader", "Lcom/carplay/iap2/body/Iap2BodyReader;", "frame", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2Messages {
    public static final Iap2Messages INSTANCE = new Iap2Messages();

    private Iap2Messages() {
    }

    public final Iap2Frame build(Iap2Endpoint endpoint, Function1<? super Iap2BodyBuilder, Unit> block) {
        Intrinsics.checkNotNullParameter(endpoint, "endpoint");
        Intrinsics.checkNotNullParameter(block, "block");
        Iap2BodyBuilder iap2BodyBuilderOf = Iap2BodyBuilder.INSTANCE.of(endpoint);
        block.invoke(iap2BodyBuilderOf);
        return iap2BodyBuilderOf.build();
    }

    public final Iap2Frame buildRaw(int messageId, Function1<? super Iap2BodyBuilder, Unit> block) {
        Intrinsics.checkNotNullParameter(block, "block");
        Iap2BodyBuilder iap2BodyBuilderOpaque = Iap2BodyBuilder.INSTANCE.opaque(messageId);
        block.invoke(iap2BodyBuilderOpaque);
        return iap2BodyBuilderOpaque.build();
    }

    public final Iap2BodyReader reader(Iap2Frame frame) {
        Intrinsics.checkNotNullParameter(frame, "frame");
        return Iap2BodyReader.INSTANCE.of(frame);
    }
}
