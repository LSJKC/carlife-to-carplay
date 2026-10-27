package com.carplay.iap2.controller;

import androidx.core.app.NotificationCompat;
import java.util.Set;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: PhoneBluetoothRecovery.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005¢\u0006\u0004\b\u0006\u0010\u0007J(\u0010\u000f\u001a\u00020\t2\b\u0010\u0010\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\tJ\u0006\u0010\u0017\u001a\u00020\u0003R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\"\u0010\f\u001a\u0004\u0018\u00010\u000b2\b\u0010\n\u001a\u0004\u0018\u00010\u000b@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u0019"}, d2 = {"Lcom/carplay/iap2/controller/PhoneBluetoothRecovery;", "", "deadlineNanos", "", "nanoTime", "Lkotlin/Function0;", "<init>", "(JLkotlin/jvm/functions/Function0;)V", "used", "", "value", "", "lastRejection", "getLastRejection", "()Ljava/lang/String;", "claim", "version", "Lcom/carplay/iap2/controller/AccessoryVersionInfo;", NotificationCompat.CATEGORY_STATUS, "Lcom/carplay/iap2/controller/BootstrapStatus;", "profile", "Lcom/carplay/iap2/controller/PhoneBootstrapProfile;", "cancelled", "remainingMillis", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class PhoneBluetoothRecovery {
    private final long deadlineNanos;
    private String lastRejection;
    private final Function0<Long> nanoTime;
    private boolean used;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final AccessoryVersionInfo OBSERVED_FIRMWARE_A2LL = new AccessoryVersionInfo("A2LL", "General Motors", "aegean_orange_n-user 12 SQBP5-39 67 release-keys", "SA8155-1.0");
    private static final AccessoryVersionInfo OBSERVED_FIRMWARE_E2LB = new AccessoryVersionInfo("E2LB", "Saic General Motors", "aegean_n-user 12 SQBP5-39 83 release-keys", "SA8155-1.0");
    private static final Set<AccessoryVersionInfo> OBSERVED_FIRMWARES = SetsKt.setOf((Object[]) new AccessoryVersionInfo[]{OBSERVED_FIRMWARE_A2LL, OBSERVED_FIRMWARE_E2LB});
    private static final AccessoryVersionInfo OBSERVED_FIRMWARE = OBSERVED_FIRMWARE_A2LL;

    /* JADX INFO: renamed from: com.carplay.iap2.controller.PhoneBluetoothRecovery$1, reason: invalid class name */
    /* JADX INFO: compiled from: PhoneBluetoothRecovery.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function0<Long> {
        public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

        AnonymousClass1() {
            super(0, System.class, "nanoTime", "nanoTime()J", 0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        public final Long invoke() {
            return Long.valueOf(System.nanoTime());
        }
    }

    public PhoneBluetoothRecovery(long deadlineNanos, Function0<Long> nanoTime) {
        Intrinsics.checkNotNullParameter(nanoTime, "nanoTime");
        this.deadlineNanos = deadlineNanos;
        this.nanoTime = nanoTime;
    }

    public /* synthetic */ PhoneBluetoothRecovery(long j, AnonymousClass1 anonymousClass1, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(j, (i & 2) != 0 ? AnonymousClass1.INSTANCE : anonymousClass1);
    }

    public final String getLastRejection() {
        return this.lastRejection;
    }

    public final boolean claim(AccessoryVersionInfo version, BootstrapStatus status, PhoneBootstrapProfile profile, boolean cancelled) {
        String str;
        Intrinsics.checkNotNullParameter(status, "status");
        Intrinsics.checkNotNullParameter(profile, "profile");
        if (cancelled) {
            str = "连接已停止";
        } else if (this.used) {
            str = "本次连接已完成一次重新协商，不循环重试";
        } else if (remainingMillis() == 0) {
            str = "原控制预算已耗尽";
        } else if (!SetsKt.setOf((Object[]) new PhoneBootstrapProfile[]{PhoneBootstrapProfile.VIVO_WIRELESS_READY, PhoneBootstrapProfile.MODERN_WIRELESS}).contains(profile)) {
            str = "当前请求方案未启用该兼容处理";
        } else if (!INSTANCE.isObservedFirmware(version)) {
            str = "当前固件不匹配已观察的 GM (A2LL/E2LB) 版本";
        } else if (!status.getIdentified() || !status.getAuthenticated()) {
            str = "识别和签名验证尚未完成";
        } else if (!status.getModernWirelessSupported() || !status.getNetworkRequestQueued()) {
            str = "尚未完成现代无线请求发布";
        } else {
            str = status.getEndpointReceived() ? "网络配置已收到，不在引导阶段重新开始" : null;
        }
        this.lastRejection = str;
        if (this.lastRejection != null) {
            return false;
        }
        this.used = true;
        return true;
    }

    public final long remainingMillis() {
        return RangesKt.coerceAtLeast((this.deadlineNanos - this.nanoTime.invoke().longValue()) / 1000000, 0L);
    }

    /* JADX INFO: compiled from: PhoneBluetoothRecovery.kt */
    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\"\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0005R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u000b¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u001c\u0010\u000e\u001a\u00020\u00058\u0006X\u0087\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u000f\u0010\u0003\u001a\u0004\b\u0010\u0010\u0007¨\u0006\u0014"}, d2 = {"Lcom/carplay/iap2/controller/PhoneBluetoothRecovery$Companion;", "", "<init>", "()V", "OBSERVED_FIRMWARE_A2LL", "Lcom/carplay/iap2/controller/AccessoryVersionInfo;", "getOBSERVED_FIRMWARE_A2LL", "()Lcom/carplay/iap2/controller/AccessoryVersionInfo;", "OBSERVED_FIRMWARE_E2LB", "getOBSERVED_FIRMWARE_E2LB", "OBSERVED_FIRMWARES", "", "getOBSERVED_FIRMWARES", "()Ljava/util/Set;", "OBSERVED_FIRMWARE", "getOBSERVED_FIRMWARE$annotations", "getOBSERVED_FIRMWARE", "isObservedFirmware", "", "version", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @Deprecated(message = "Use OBSERVED_FIRMWARE_A2LL or OBSERVED_FIRMWARES", replaceWith = @ReplaceWith(expression = "OBSERVED_FIRMWARE_A2LL", imports = {}))
        public static /* synthetic */ void getOBSERVED_FIRMWARE$annotations() {
        }

        private Companion() {
        }

        public final AccessoryVersionInfo getOBSERVED_FIRMWARE_A2LL() {
            return PhoneBluetoothRecovery.OBSERVED_FIRMWARE_A2LL;
        }

        public final AccessoryVersionInfo getOBSERVED_FIRMWARE_E2LB() {
            return PhoneBluetoothRecovery.OBSERVED_FIRMWARE_E2LB;
        }

        public final Set<AccessoryVersionInfo> getOBSERVED_FIRMWARES() {
            return PhoneBluetoothRecovery.OBSERVED_FIRMWARES;
        }

        public final AccessoryVersionInfo getOBSERVED_FIRMWARE() {
            return PhoneBluetoothRecovery.OBSERVED_FIRMWARE;
        }

        public final boolean isObservedFirmware(AccessoryVersionInfo version) {
            return CollectionsKt.contains(getOBSERVED_FIRMWARES(), version);
        }
    }
}
