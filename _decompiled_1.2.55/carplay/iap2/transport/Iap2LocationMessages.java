package com.carplay.iap2.transport;

import com.carplay.iap2.message.Iap2ControlMessages;
import com.carplay.iap2.wire.Iap2Frame;
import com.carplay.iap2.wire.Iap2Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2LocationClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\n2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LocationMessages;", "", "<init>", "()V", "START_LOCATION_INFORMATION", "", "LOCATION_INFORMATION", "STOP_LOCATION_INFORMATION", "VEHICLE_SPEED_DATA", "requestedComponents", "", "frame", "Lcom/carplay/iap2/wire/Iap2Frame;", "locationInformation", "nmeaSentence", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2LocationMessages {
    public static final Iap2LocationMessages INSTANCE = new Iap2LocationMessages();
    public static final int LOCATION_INFORMATION = 65531;
    public static final int START_LOCATION_INFORMATION = 65530;
    public static final int STOP_LOCATION_INFORMATION = 65532;
    public static final int VEHICLE_SPEED_DATA = 4;

    private Iap2LocationMessages() {
    }

    public final Set<Integer> requestedComponents(Iap2Frame frame) {
        Object objM159constructorimpl;
        Intrinsics.checkNotNullParameter(frame, "frame");
        try {
            Result.Companion companion = Result.INSTANCE;
            Iap2LocationMessages iap2LocationMessages = this;
            Iterable $this$map$iv = frame.body().asList();
            Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
            for (Object item$iv$iv : $this$map$iv) {
                Iap2Parameter it = (Iap2Parameter) item$iv$iv;
                destination$iv$iv.add(Integer.valueOf(it.getId()));
            }
            objM159constructorimpl = Result.m159constructorimpl(CollectionsKt.toSortedSet((List) destination$iv$iv));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        Set setEmptySet = SetsKt.emptySet();
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = setEmptySet;
        }
        return (Set) objM159constructorimpl;
    }

    public final Iap2Frame locationInformation(String nmeaSentence) {
        Intrinsics.checkNotNullParameter(nmeaSentence, "nmeaSentence");
        if (nmeaSentence.length() > 0) {
            return Iap2ControlMessages.INSTANCE.locationInformation(nmeaSentence);
        }
        throw new IllegalArgumentException("NMEA sentence must not be empty".toString());
    }
}
