package com.carplay.iap2.transport;

import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2LocationClient.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\bf\u0018\u00002\u00060\u0001j\u0002`\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016J\b\u0010\b\u001a\u00020\tH&J\b\u0010\n\u001a\u00020\u0004H&J\n\u0010\u000b\u001a\u0004\u0018\u00010\fH&J\b\u0010\r\u001a\u00020\u0004H\u0016¨\u0006\u000eÀ\u0006\u0003"}, d2 = {"Lcom/carplay/iap2/transport/Iap2LocationProvider;", "Ljava/lang/AutoCloseable;", "Lkotlin/AutoCloseable;", "onRequested", "", "components", "", "", "start", "", "stop", "latestNmea", "", "close", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface Iap2LocationProvider extends AutoCloseable {
    String latestNmea();

    boolean start();

    void stop();

    /* JADX INFO: compiled from: Iap2LocationClient.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final class DefaultImpls {
        @Deprecated
        public static void onRequested(Iap2LocationProvider $this, Set<Integer> components) {
            Intrinsics.checkNotNullParameter(components, "components");
            Iap2LocationProvider.super.onRequested(components);
        }

        @Deprecated
        public static void close(Iap2LocationProvider $this) {
            Iap2LocationProvider.super.close();
        }
    }

    default void onRequested(Set<Integer> components) {
        Intrinsics.checkNotNullParameter(components, "components");
    }

    @Override // java.lang.AutoCloseable
    default void close() {
        stop();
    }
}
