package com.carplay.mfi;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: MfiAuthenticationClient.kt */
/* JADX INFO: loaded from: classes11.dex */
@Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\b¨\u0006\n"}, d2 = {"Lcom/carplay/mfi/BaaCertificatePair;", "", "leaf", "", "intermediate", "<init>", "([B[B)V", "getLeaf", "()[B", "getIntermediate", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class BaaCertificatePair {
    private final byte[] intermediate;
    private final byte[] leaf;

    public BaaCertificatePair(byte[] leaf, byte[] intermediate) {
        Intrinsics.checkNotNullParameter(leaf, "leaf");
        Intrinsics.checkNotNullParameter(intermediate, "intermediate");
        byte[] bArrCopyOf = Arrays.copyOf(leaf, leaf.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        this.leaf = bArrCopyOf;
        byte[] bArrCopyOf2 = Arrays.copyOf(intermediate, intermediate.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf2, "copyOf(...)");
        this.intermediate = bArrCopyOf2;
        if (this.leaf.length == 0) {
            throw new IllegalArgumentException("BAA leaf certificate must not be empty".toString());
        }
        if (this.intermediate.length == 0) {
            throw new IllegalArgumentException("BAA intermediate certificate must not be empty".toString());
        }
    }

    public final byte[] getLeaf() {
        return this.leaf;
    }

    public final byte[] getIntermediate() {
        return this.intermediate;
    }
}
