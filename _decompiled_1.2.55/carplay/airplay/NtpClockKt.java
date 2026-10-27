package com.carplay.airplay;

import com.carplay.adapter.carlife.KeyFrameRebuildGate;
import java.math.BigInteger;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: NtpClock.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000&\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u001a\u0010\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u0001H\u0002\u001a\b\u0010\b\u001a\u00020\u0003H\u0002\u001a\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u0001H\u0002\u001a\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0003H\u0002\u001a\u0018\u0010\u000e\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002\u001a \u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\r\u001a\u00020\u0010H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u0016\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003X\u0082\u0004¢\u0006\u0002\n\u0000\"\u0016\u0010\u0005\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"NTP_EPOCH_OFFSET", "", "TWO32", "Ljava/math/BigInteger;", "kotlin.jvm.PlatformType", "NANOS_PER_SECOND", "wallClockNtpOffsetNs", "monoNs", "ntp64Now", "ntpFromNanos", "ns", "ntpBytes", "", "value", "readNtp", "offset", "", "writeU16Be", "", "target", "core-airplay"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class NtpClockKt {
    private static final long NTP_EPOCH_OFFSET = 2208988800L;
    private static final BigInteger TWO32 = BigInteger.ONE.shiftLeft(32);
    private static final BigInteger NANOS_PER_SECOND = BigInteger.valueOf(KeyFrameRebuildGate.RETRY_MIN_INTERVAL_NANOS);

    /* JADX INFO: Access modifiers changed from: private */
    public static final long wallClockNtpOffsetNs(long monoNs) {
        BigInteger ntpNs = ntp64Now().and(BigInteger.ONE.shiftLeft(64).subtract(BigInteger.ONE)).multiply(NANOS_PER_SECOND).shiftRight(32);
        return ntpNs.subtract(BigInteger.valueOf(monoNs)).longValue();
    }

    private static final BigInteger ntp64Now() {
        double seconds = (System.currentTimeMillis() / 1000.0d) + 2.2089888E9d;
        long whole = (long) Math.floor(seconds);
        long fraction = (long) Math.floor((seconds - whole) * TWO32.doubleValue());
        BigInteger bigIntegerOr = BigInteger.valueOf(whole).shiftLeft(32).or(BigInteger.valueOf(fraction));
        Intrinsics.checkNotNullExpressionValue(bigIntegerOr, "or(...)");
        return bigIntegerOr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final BigInteger ntpFromNanos(long ns) {
        BigInteger seconds = BigInteger.valueOf(Math.floorDiv(ns, KeyFrameRebuildGate.RETRY_MIN_INTERVAL_NANOS));
        BigInteger nanos = BigInteger.valueOf(Math.floorMod(ns, KeyFrameRebuildGate.RETRY_MIN_INTERVAL_NANOS));
        BigInteger bigIntegerOr = seconds.shiftLeft(32).or(nanos.shiftLeft(32).divide(NANOS_PER_SECOND));
        Intrinsics.checkNotNullExpressionValue(bigIntegerOr, "or(...)");
        return bigIntegerOr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final byte[] ntpBytes(BigInteger value) {
        byte[] source;
        byte[] bytes = value.and(BigInteger.ONE.shiftLeft(64).subtract(BigInteger.ONE)).toByteArray();
        byte[] target = new byte[8];
        if (bytes.length > 8) {
            Intrinsics.checkNotNull(bytes);
            source = ArraysKt.copyOfRange(bytes, bytes.length - 8, bytes.length);
        } else {
            source = bytes;
        }
        Intrinsics.checkNotNull(source);
        ArraysKt.copyInto$default(source, target, target.length - source.length, 0, 0, 12, (Object) null);
        return target;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final BigInteger readNtp(byte[] value, int offset) {
        return new BigInteger(1, ArraysKt.copyOfRange(value, offset, offset + 8));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void writeU16Be(byte[] target, int offset, int value) {
        target[offset] = (byte) ((value >>> 8) & 255);
        target[offset + 1] = (byte) (value & 255);
    }
}
