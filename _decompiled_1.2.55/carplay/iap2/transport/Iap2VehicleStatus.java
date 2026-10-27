package com.carplay.iap2.transport;

import androidx.core.app.NotificationCompat;
import com.carplay.iap2.body.Iap2BodyBuilder;
import com.carplay.iap2.message.Iap2Messages;
import com.carplay.iap2.wire.Iap2Frame;
import com.carplay.iap2.wire.Iap2ProtocolException;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: Iap2VehicleStatus.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u001a\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0016"}, d2 = {"Lcom/carplay/iap2/transport/Iap2VehicleStatus;", "", "<init>", "()V", "START_VEHICLE_STATUS_UPDATES", "", "VEHICLE_STATUS_UPDATE", "STOP_VEHICLE_STATUS_UPDATES", "UPDATE_INTERVAL_MILLIS", "", "ENGINE_TYPE_ELECTRIC", "electricVehicleComponents", "", "Lcom/carplay/iap2/body/Iap2BodyBuilder;", "name", "", "connectors", "Lcom/carplay/iap2/transport/EvChargingConnectors;", "update", "Lcom/carplay/iap2/wire/Iap2Frame;", NotificationCompat.CATEGORY_STATUS, "Lcom/carplay/iap2/transport/VehicleStatusSnapshot;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2VehicleStatus {
    private static final int ENGINE_TYPE_ELECTRIC = 2;
    public static final Iap2VehicleStatus INSTANCE = new Iap2VehicleStatus();
    public static final int START_VEHICLE_STATUS_UPDATES = 41216;
    public static final int STOP_VEHICLE_STATUS_UPDATES = 41218;
    public static final long UPDATE_INTERVAL_MILLIS = 30000;
    public static final int VEHICLE_STATUS_UPDATE = 41217;

    private Iap2VehicleStatus() {
    }

    public final void electricVehicleComponents(Iap2BodyBuilder $this$electricVehicleComponents, final String name, final EvChargingConnectors connectors) throws Iap2ProtocolException {
        Intrinsics.checkNotNullParameter($this$electricVehicleComponents, "<this>");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(connectors, "connectors");
        $this$electricVehicleComponents.group(20, new Function1() { // from class: com.carplay.iap2.transport.Iap2VehicleStatus$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Iap2VehicleStatus.electricVehicleComponents$lambda$0(name, connectors, (Iap2BodyBuilder) obj);
            }
        });
        $this$electricVehicleComponents.group(21, new Function1() { // from class: com.carplay.iap2.transport.Iap2VehicleStatus$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Iap2VehicleStatus.electricVehicleComponents$lambda$1(name, (Iap2BodyBuilder) obj);
            }
        });
    }

    static final Unit electricVehicleComponents$lambda$0(String $name, EvChargingConnectors $connectors, Iap2BodyBuilder group) {
        Intrinsics.checkNotNullParameter(group, "$this$group");
        group.u16(0, 3);
        group.string(1, $name);
        group.u8(2, 2);
        group.string(6, $name);
        group.string(8, $name);
        for (int connector : $connectors.getWireValues()) {
            group.u8(11, connector);
        }
        return Unit.INSTANCE;
    }

    static final Unit electricVehicleComponents$lambda$1(String $name, Iap2BodyBuilder group) {
        Intrinsics.checkNotNullParameter(group, "$this$group");
        group.u16(0, 4);
        group.string(1, $name);
        group.m51void(3);
        group.m51void(6);
        group.m51void(11);
        group.m51void(15);
        group.m51void(21);
        group.m51void(30);
        return Unit.INSTANCE;
    }

    public final Iap2Frame update(VehicleStatusSnapshot status) {
        Intrinsics.checkNotNullParameter(status, "status");
        Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
        Iap2BodyBuilder $this$update_u24lambda_u244 = Iap2BodyBuilder.INSTANCE.opaque(VEHICLE_STATUS_UPDATE);
        $this$update_u24lambda_u244.u16(3, RangesKt.coerceIn(status.getRangeKm(), 0, 65535));
        $this$update_u24lambda_u244.bool(6, status.getRangeWarning());
        $this$update_u24lambda_u244.u16(11, RangesKt.coerceIn(status.getRangeKm(), 0, 65535));
        $this$update_u24lambda_u244.bool(15, status.getRangeWarning());
        if (status.getCurrentChargeWh() != null || status.getMaxChargeWh() != null) {
            $this$update_u24lambda_u244.u32(21, 0L);
        }
        Long currentChargeWh = status.getCurrentChargeWh();
        if (currentChargeWh != null) {
            long it = currentChargeWh.longValue();
            $this$update_u24lambda_u244.u32(22, RangesKt.coerceAtLeast(it, 0L));
        }
        Long maxChargeWh = status.getMaxChargeWh();
        if (maxChargeWh != null) {
            long it2 = maxChargeWh.longValue();
            $this$update_u24lambda_u244.u32(23, RangesKt.coerceAtLeast(it2, 0L));
        }
        $this$update_u24lambda_u244.u32(24, MathKt.roundToLong(RangesKt.coerceIn(status.getBatteryPercent(), 0.0d, 100.0d) * 1000.0d));
        $this$update_u24lambda_u244.bool(25, status.getCharging());
        $this$update_u24lambda_u244.u16(30, RangesKt.coerceIn(status.getMaxRangeKm(), 0, 65535));
        return $this$update_u24lambda_u244.build();
    }
}
