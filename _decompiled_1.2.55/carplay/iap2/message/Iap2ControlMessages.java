package com.carplay.iap2.message;

import com.carplay.iap2.body.Iap2BodyBuilder;
import com.carplay.iap2.catalog.Iap2Endpoint;
import com.carplay.iap2.catalog.Iap2Endpoints;
import com.carplay.iap2.wire.Iap2Frame;
import com.carplay.iap2.wire.Iap2ProtocolException;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2Messages.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003JA\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\f\u001a\u00020\t¢\u0006\u0002\u0010\rJ\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\u000fJ\u000e\u0010\u0010\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u0012¨\u0006\u0013"}, d2 = {"Lcom/carplay/iap2/message/Iap2ControlMessages;", "", "<init>", "()V", "powerSourceUpdate", "Lcom/carplay/iap2/wire/Iap2Frame;", "availableCurrentMilliAmps", "", "charging", "", "availableSiphoningCurrentMilliAmps", "reserveCurrentMilliAmps", "maxNonSiphoningCurrentExceeded", "(ILjava/lang/Boolean;Ljava/lang/Integer;Ljava/lang/Integer;Z)Lcom/carplay/iap2/wire/Iap2Frame;", "subscriptions", "", "locationInformation", "nmeaSentence", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2ControlMessages {
    public static final Iap2ControlMessages INSTANCE = new Iap2ControlMessages();

    private Iap2ControlMessages() {
    }

    public static /* synthetic */ Iap2Frame powerSourceUpdate$default(Iap2ControlMessages iap2ControlMessages, int i, Boolean bool, Integer num, Integer num2, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            bool = null;
        }
        if ((i2 & 4) != 0) {
            num = null;
        }
        if ((i2 & 8) != 0) {
            num2 = null;
        }
        if ((i2 & 16) != 0) {
            z = false;
        }
        return iap2ControlMessages.powerSourceUpdate(i, bool, num, num2, z);
    }

    public final Iap2Frame powerSourceUpdate(int availableCurrentMilliAmps, Boolean charging, Integer availableSiphoningCurrentMilliAmps, Integer reserveCurrentMilliAmps, boolean maxNonSiphoningCurrentExceeded) {
        Integer numValueOf;
        Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
        Iap2Endpoint endpoint$iv = Iap2Endpoints.INSTANCE.getPOWER_SOURCE_UPDATE();
        Iap2BodyBuilder $this$powerSourceUpdate_u24lambda_u241 = Iap2BodyBuilder.INSTANCE.of(endpoint$iv);
        int i = 0;
        $this$powerSourceUpdate_u24lambda_u241.u16(0, availableCurrentMilliAmps);
        if (charging != null) {
            boolean it = charging.booleanValue();
            if (it) {
                i = 1;
            }
            numValueOf = Integer.valueOf(i);
        } else {
            numValueOf = null;
        }
        $this$powerSourceUpdate_u24lambda_u241.optionalU8(1, numValueOf);
        $this$powerSourceUpdate_u24lambda_u241.optionalU16(3, availableSiphoningCurrentMilliAmps);
        $this$powerSourceUpdate_u24lambda_u241.optionalU16(4, reserveCurrentMilliAmps);
        $this$powerSourceUpdate_u24lambda_u241.optionalVoid(5, maxNonSiphoningCurrentExceeded);
        return $this$powerSourceUpdate_u24lambda_u241.build();
    }

    public final List<Iap2Frame> subscriptions() throws Iap2ProtocolException {
        Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
        Iap2Endpoint endpoint$iv = Iap2Endpoints.INSTANCE.getSTART_NOW_PLAYING_UPDATES();
        Iap2BodyBuilder $this$subscriptions_u24lambda_u245 = Iap2BodyBuilder.INSTANCE.of(endpoint$iv);
        $this$subscriptions_u24lambda_u245.group(0, new Function1() { // from class: com.carplay.iap2.message.Iap2ControlMessages$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Iap2ControlMessages.subscriptions$lambda$5$lambda$2((Iap2BodyBuilder) obj);
            }
        });
        $this$subscriptions_u24lambda_u245.group(1, new Function1() { // from class: com.carplay.iap2.message.Iap2ControlMessages$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Iap2ControlMessages.subscriptions$lambda$5$lambda$4((Iap2BodyBuilder) obj);
            }
        });
        Unit unit = Unit.INSTANCE;
        Iap2Messages iap2Messages2 = Iap2Messages.INSTANCE;
        Iap2Endpoint endpoint$iv2 = Iap2Endpoints.INSTANCE.getSTART_ROUTE_GUIDANCE_UPDATES();
        Iap2BodyBuilder $this$subscriptions_u24lambda_u246 = Iap2BodyBuilder.INSTANCE.of(endpoint$iv2);
        $this$subscriptions_u24lambda_u246.u16(0, 42);
        $this$subscriptions_u24lambda_u246.m51void(1);
        $this$subscriptions_u24lambda_u246.m51void(2);
        Unit unit2 = Unit.INSTANCE;
        Iap2Messages iap2Messages3 = Iap2Messages.INSTANCE;
        Iap2Endpoint endpoint$iv3 = Iap2Endpoints.INSTANCE.getSTART_POWER_UPDATES();
        Iap2BodyBuilder $this$subscriptions_u24lambda_u247 = Iap2BodyBuilder.INSTANCE.of(endpoint$iv3);
        $this$subscriptions_u24lambda_u247.m51void(4);
        $this$subscriptions_u24lambda_u247.m51void(5);
        $this$subscriptions_u24lambda_u247.m51void(6);
        Unit unit3 = Unit.INSTANCE;
        Iap2Messages iap2Messages4 = Iap2Messages.INSTANCE;
        Iap2Endpoint endpoint$iv4 = Iap2Endpoints.INSTANCE.getSTART_COMMUNICATIONS_UPDATES();
        Iap2BodyBuilder $this$subscriptions_u24lambda_u248 = Iap2BodyBuilder.INSTANCE.of(endpoint$iv4);
        $this$subscriptions_u24lambda_u248.m51void(0);
        $this$subscriptions_u24lambda_u248.m51void(4);
        $this$subscriptions_u24lambda_u248.m51void(5);
        Unit unit4 = Unit.INSTANCE;
        Iap2Messages iap2Messages5 = Iap2Messages.INSTANCE;
        Iap2Endpoint endpoint$iv5 = Iap2Endpoints.INSTANCE.getSTART_CALL_STATE_UPDATES();
        Iap2BodyBuilder $this$subscriptions_u24lambda_u249 = Iap2BodyBuilder.INSTANCE.of(endpoint$iv5);
        $this$subscriptions_u24lambda_u249.m51void(0);
        $this$subscriptions_u24lambda_u249.m51void(1);
        $this$subscriptions_u24lambda_u249.m51void(2);
        $this$subscriptions_u24lambda_u249.m51void(3);
        $this$subscriptions_u24lambda_u249.m51void(4);
        $this$subscriptions_u24lambda_u249.m51void(11);
        Unit unit5 = Unit.INSTANCE;
        return CollectionsKt.listOf((Object[]) new Iap2Frame[]{$this$subscriptions_u24lambda_u245.build(), $this$subscriptions_u24lambda_u246.build(), $this$subscriptions_u24lambda_u247.build(), $this$subscriptions_u24lambda_u248.build(), $this$subscriptions_u24lambda_u249.build()});
    }

    static final Unit subscriptions$lambda$5$lambda$2(Iap2BodyBuilder group) {
        Intrinsics.checkNotNullParameter(group, "$this$group");
        Iterable $this$forEach$iv = CollectionsKt.listOf((Object[]) new Integer[]{1, 4, 6, 12, 26});
        for (Object element$iv : $this$forEach$iv) {
            int p0 = ((Number) element$iv).intValue();
            group.m51void(p0);
        }
        return Unit.INSTANCE;
    }

    static final Unit subscriptions$lambda$5$lambda$4(Iap2BodyBuilder group) {
        Intrinsics.checkNotNullParameter(group, "$this$group");
        Iterable $this$forEach$iv = CollectionsKt.listOf((Object[]) new Integer[]{0, 1, 7});
        for (Object element$iv : $this$forEach$iv) {
            int p0 = ((Number) element$iv).intValue();
            group.m51void(p0);
        }
        return Unit.INSTANCE;
    }

    public final Iap2Frame locationInformation(String nmeaSentence) {
        Intrinsics.checkNotNullParameter(nmeaSentence, "nmeaSentence");
        if (!(nmeaSentence.length() > 0)) {
            throw new IllegalArgumentException("NMEA sentence must not be empty".toString());
        }
        Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
        Iap2Endpoint endpoint$iv = Iap2Endpoints.INSTANCE.getLOCATION_INFORMATION();
        Iap2BodyBuilder $this$locationInformation_u24lambda_u2411 = Iap2BodyBuilder.INSTANCE.of(endpoint$iv);
        $this$locationInformation_u24lambda_u2411.string(0, nmeaSentence);
        return $this$locationInformation_u24lambda_u2411.build();
    }
}
