package com.carlink.cast;

import android.os.SystemClock;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: AutoConnectCoordinator.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010%\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u000bJ\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0014\u001a\u00020\u000b2\b\b\u0002\u0010\u0017\u001a\u00020\u0005J\u0015\u0010\u0018\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0014\u001a\u00020\u000b¢\u0006\u0002\u0010\u0019J\u000e\u0010\u001a\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u000bJ\u0012\u0010\u001b\u001a\u00020\u00162\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u000bJ\f\u0010\u001c\u001a\u00020\u000b*\u00020\u000bH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00050\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00100\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00050\u000eX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lcom/carlink/cast/AutoConnectCoordinator;", "", "<init>", "()V", "CLAIM_WINDOW_MS", "", "FAILURE_BACKOFF_MS", "MAX_FAILURE_BACKOFF_MS", "DUPLICATE_FAILURE_WINDOW_MS", "lock", "claimedAddress", "", "claimedAt", "retryNotBefore", "", "failureCount", "", "lastFailureAt", "claim", "", "address", "recordFailure", "", "delayMillis", "recordSuccess", "(Ljava/lang/String;)Ljava/lang/Long;", "backoffRemaining", "release", "normalized", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AutoConnectCoordinator {
    private static final long CLAIM_WINDOW_MS = 20000;
    private static final long DUPLICATE_FAILURE_WINDOW_MS = 5000;
    private static final long FAILURE_BACKOFF_MS = 60000;
    private static final long MAX_FAILURE_BACKOFF_MS = 180000;
    private static String claimedAddress;
    private static long claimedAt;
    public static final AutoConnectCoordinator INSTANCE = new AutoConnectCoordinator();
    private static final Object lock = new Object();
    private static final Map<String, Long> retryNotBefore = new LinkedHashMap();
    private static final Map<String, Integer> failureCount = new LinkedHashMap();
    private static final Map<String, Long> lastFailureAt = new LinkedHashMap();

    private AutoConnectCoordinator() {
    }

    public final boolean claim(String address) {
        Intrinsics.checkNotNullParameter(address, "address");
        synchronized (lock) {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            String strNormalized = INSTANCE.normalized(address);
            Long l = retryNotBefore.get(strNormalized);
            long jLongValue = l != null ? l.longValue() : 0L;
            if (jLongValue > jElapsedRealtime) {
                return false;
            }
            if (jLongValue != 0) {
                retryNotBefore.remove(strNormalized);
            }
            if (StringsKt.equals(claimedAddress, address, true) && jElapsedRealtime - claimedAt < CLAIM_WINDOW_MS) {
                return false;
            }
            claimedAddress = address;
            claimedAt = jElapsedRealtime;
            return true;
        }
    }

    public static /* synthetic */ void recordFailure$default(AutoConnectCoordinator autoConnectCoordinator, String str, long j, int i, Object obj) throws Throwable {
        if ((i & 2) != 0) {
            j = 60000;
        }
        autoConnectCoordinator.recordFailure(str, j);
    }

    public final void recordFailure(String address, long delayMillis) throws Throwable {
        Intrinsics.checkNotNullParameter(address, "address");
        synchronized (lock) {
            try {
                long jElapsedRealtime = SystemClock.elapsedRealtime();
                String strNormalized = INSTANCE.normalized(address);
                Long l = lastFailureAt.get(strNormalized);
                if (jElapsedRealtime - (l != null ? l.longValue() : 0L) < 5000) {
                    if (StringsKt.equals(claimedAddress, address, true)) {
                        claimedAddress = null;
                        claimedAt = 0L;
                    }
                    return;
                }
                lastFailureAt.put(strNormalized, Long.valueOf(jElapsedRealtime));
                Integer num = failureCount.get(strNormalized);
                int iIntValue = (num != null ? num.intValue() : 0) + 1;
                failureCount.put(strNormalized, Integer.valueOf(iIntValue));
                try {
                    long jCoerceAtMost = jElapsedRealtime + RangesKt.coerceAtMost(RangesKt.coerceAtLeast(delayMillis, 0L) * (1 << RangesKt.coerceIn(iIntValue - 1, 0, 2)), MAX_FAILURE_BACKOFF_MS);
                    Map<String, Long> map = retryNotBefore;
                    Long l2 = retryNotBefore.get(strNormalized);
                    map.put(strNormalized, Long.valueOf(Math.max(l2 != null ? l2.longValue() : 0L, jCoerceAtMost)));
                    if (StringsKt.equals(claimedAddress, address, true)) {
                        claimedAddress = null;
                        claimedAt = 0L;
                    }
                    Unit unit = Unit.INSTANCE;
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    public final Long recordSuccess(String address) {
        Long lRemove;
        Intrinsics.checkNotNullParameter(address, "address");
        synchronized (lock) {
            String strNormalized = INSTANCE.normalized(address);
            retryNotBefore.remove(strNormalized);
            failureCount.remove(strNormalized);
            lRemove = lastFailureAt.remove(strNormalized);
        }
        return lRemove;
    }

    public final long backoffRemaining(String address) {
        long j;
        Intrinsics.checkNotNullParameter(address, "address");
        synchronized (lock) {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            Long l = retryNotBefore.get(INSTANCE.normalized(address));
            j = 0;
            long jLongValue = (l != null ? l.longValue() : 0L) - jElapsedRealtime;
            if (jLongValue <= 0) {
                retryNotBefore.remove(INSTANCE.normalized(address));
            } else {
                j = jLongValue;
            }
        }
        return j;
    }

    public static /* synthetic */ void release$default(AutoConnectCoordinator autoConnectCoordinator, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            str = null;
        }
        autoConnectCoordinator.release(str);
    }

    public final void release(String address) {
        synchronized (lock) {
            if (address != null) {
                try {
                    if (StringsKt.equals(claimedAddress, address, true)) {
                        claimedAddress = null;
                        claimedAt = 0L;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            } else {
                claimedAddress = null;
                claimedAt = 0L;
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    private final String normalized(String $this$normalized) {
        String string = StringsKt.trim((CharSequence) $this$normalized).toString();
        Locale US = Locale.US;
        Intrinsics.checkNotNullExpressionValue(US, "US");
        String upperCase = string.toUpperCase(US);
        Intrinsics.checkNotNullExpressionValue(upperCase, "toUpperCase(...)");
        return upperCase;
    }
}
