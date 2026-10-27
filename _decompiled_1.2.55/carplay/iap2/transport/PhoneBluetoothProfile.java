package com.carplay.iap2.transport;

import com.carplay.adapter.carlife.CarLifeSession;
import com.carplay.airplay.AirPlayDisplaySettings;
import com.carplay.airplay.AirPlayIapTunnelStream;
import java.util.List;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.CollectionsKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlinx.coroutines.scheduling.WorkQueueKt;
import org.bouncycastle.jcajce.util.AnnotatedPrivateKey;

/* JADX INFO: compiled from: PhoneBluetoothProfile.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0019\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00000\u0016J\r\u0010\u0017\u001a\u00020\u0018H\u0000¢\u0006\u0002\b\u0019R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0011\u001a\u00020\u00128F¢\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014j\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010¨\u0006\u001a"}, d2 = {"Lcom/carplay/iap2/transport/PhoneBluetoothProfile;", "", AnnotatedPrivateKey.LABEL, "", "readyTimeoutMillis", "", "<init>", "(Ljava/lang/String;ILjava/lang/String;J)V", "getLabel", "()Ljava/lang/String;", "getReadyTimeoutMillis", "()J", "ACCESSORY_FIRST", "VIVO_ACCESSORY_FIRST", "COMMAND_TUNNEL", "CAPTURE_IMMEDIATE", "LEGACY_CONTROLLER", "accessoryInitiates", "", "getAccessoryInitiates", "()Z", "attempts", "", "linkConfig", "Lcom/carplay/iap2/transport/Iap2LinkConfig;", "linkConfig$core_iap2", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public enum PhoneBluetoothProfile {
    ACCESSORY_FIRST("accessory-first", CarLifeSession.CONNECT_RETRY_WINDOW_MS),
    VIVO_ACCESSORY_FIRST("vivo-accessory-first", CarLifeSession.CONNECT_RETRY_WINDOW_MS),
    COMMAND_TUNNEL("command-tunnel", CarLifeSession.CONNECT_RETRY_WINDOW_MS),
    CAPTURE_IMMEDIATE("detection-then-phone-syn", AirPlayIapTunnelStream.EVENT_READY_TIMEOUT_MILLIS),
    LEGACY_CONTROLLER("legacy-immediate-controller", AirPlayIapTunnelStream.EVENT_READY_TIMEOUT_MILLIS);

    private final String label;
    private final long readyTimeoutMillis;
    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

    /* JADX INFO: compiled from: PhoneBluetoothProfile.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[PhoneBluetoothProfile.values().length];
            try {
                iArr[PhoneBluetoothProfile.ACCESSORY_FIRST.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[PhoneBluetoothProfile.VIVO_ACCESSORY_FIRST.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[PhoneBluetoothProfile.COMMAND_TUNNEL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[PhoneBluetoothProfile.CAPTURE_IMMEDIATE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[PhoneBluetoothProfile.LEGACY_CONTROLLER.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public static EnumEntries<PhoneBluetoothProfile> getEntries() {
        return $ENTRIES;
    }

    PhoneBluetoothProfile(String label, long readyTimeoutMillis) {
        this.label = label;
        this.readyTimeoutMillis = readyTimeoutMillis;
    }

    public final String getLabel() {
        return this.label;
    }

    public final long getReadyTimeoutMillis() {
        return this.readyTimeoutMillis;
    }

    public final boolean getAccessoryInitiates() {
        return this == ACCESSORY_FIRST || this == VIVO_ACCESSORY_FIRST || this == COMMAND_TUNNEL;
    }

    public final List<PhoneBluetoothProfile> attempts() {
        switch (WhenMappings.$EnumSwitchMapping$0[ordinal()]) {
            case 1:
            case 2:
            case 3:
                return CollectionsKt.listOf((Object[]) new PhoneBluetoothProfile[]{this, CAPTURE_IMMEDIATE, LEGACY_CONTROLLER});
            case 4:
                return CollectionsKt.listOf((Object[]) new PhoneBluetoothProfile[]{this, LEGACY_CONTROLLER});
            case 5:
                return CollectionsKt.listOf(this);
            default:
                throw new NoWhenBranchMatchedException();
        }
    }

    public final Iap2LinkConfig linkConfig$core_iap2() {
        List listListOf;
        if (this == LEGACY_CONTROLLER) {
            return new Iap2LinkConfig(4, 0, 0, false, 2, 0, 0, 0, 0, null, null, false, false, null, 16366, null);
        }
        boolean responder = getAccessoryInitiates();
        int i = this == VIVO_ACCESSORY_FIRST ? 5 : WorkQueueKt.MASK;
        int i2 = (this == ACCESSORY_FIRST || this == COMMAND_TUNNEL) ? 65535 : 2048;
        int i3 = responder ? 1500 : AirPlayDisplaySettings.MAX_REPORTED_PHYSICAL_MM;
        int i4 = responder ? 73 : 256;
        int i5 = responder ? 3 : 1;
        switch (WhenMappings.$EnumSwitchMapping$0[ordinal()]) {
            case 1:
            case 2:
                listListOf = CollectionsKt.listOf((Object[]) new Iap2LinkEngine.SessionDescriptor[]{new Iap2LinkEngine.SessionDescriptor(1, 0, 2), new Iap2LinkEngine.SessionDescriptor(2, 1, 2)});
                break;
            case 3:
                listListOf = CollectionsKt.listOf((Object[]) new Iap2LinkEngine.SessionDescriptor[]{new Iap2LinkEngine.SessionDescriptor(1, 0, 2), new Iap2LinkEngine.SessionDescriptor(2, 1, 2), new Iap2LinkEngine.SessionDescriptor(12, 1, 2)});
                break;
            default:
                listListOf = CollectionsKt.listOf(new Iap2LinkEngine.SessionDescriptor(1, 0, 2));
                break;
        }
        Iap2LinkEngine.SynchronizationPayload synchronization = new Iap2LinkEngine.SynchronizationPayload(i, i2, i3, i4, 30, i5, listListOf);
        return new Iap2LinkConfig(synchronization.getMaxOutgoing(), 0, synchronization.getAcknowledgementTimeoutMillis(), false, 2, 0, 0, 0, 0, responder ? Iap2LinkStartup.ACCESSORY_FIRST : Iap2LinkStartup.DETECTION_THEN_SYN, synchronization, true, this == COMMAND_TUNNEL, 1, 490, null);
    }
}
