package com.carplay.airplay;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Srp6a.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u000f\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u0010\u0010\u001a\u0004\u0018\u00010\u0005HÆ\u0003J+\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\u00032\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0014\u001a\u00020\u0015HÖ\u0001J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\f¨\u0006\u0018"}, d2 = {"Lcom/carplay/airplay/Srp6aResult;", "", "ok", "", "sessionKey", "", "serverProof", "<init>", "(Z[B[B)V", "getOk", "()Z", "getSessionKey", "()[B", "getServerProof", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class Srp6aResult {
    private final boolean ok;
    private final byte[] serverProof;
    private final byte[] sessionKey;

    public static /* synthetic */ Srp6aResult copy$default(Srp6aResult srp6aResult, boolean z, byte[] bArr, byte[] bArr2, int i, Object obj) {
        if ((i & 1) != 0) {
            z = srp6aResult.ok;
        }
        if ((i & 2) != 0) {
            bArr = srp6aResult.sessionKey;
        }
        if ((i & 4) != 0) {
            bArr2 = srp6aResult.serverProof;
        }
        return srp6aResult.copy(z, bArr, bArr2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final boolean getOk() {
        return this.ok;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final byte[] getSessionKey() {
        return this.sessionKey;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final byte[] getServerProof() {
        return this.serverProof;
    }

    public final Srp6aResult copy(boolean ok, byte[] sessionKey, byte[] serverProof) {
        return new Srp6aResult(ok, sessionKey, serverProof);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Srp6aResult)) {
            return false;
        }
        Srp6aResult srp6aResult = (Srp6aResult) other;
        return this.ok == srp6aResult.ok && Intrinsics.areEqual(this.sessionKey, srp6aResult.sessionKey) && Intrinsics.areEqual(this.serverProof, srp6aResult.serverProof);
    }

    public int hashCode() {
        return (((Boolean.hashCode(this.ok) * 31) + (this.sessionKey == null ? 0 : Arrays.hashCode(this.sessionKey))) * 31) + (this.serverProof != null ? Arrays.hashCode(this.serverProof) : 0);
    }

    public String toString() {
        return "Srp6aResult(ok=" + this.ok + ", sessionKey=" + Arrays.toString(this.sessionKey) + ", serverProof=" + Arrays.toString(this.serverProof) + ")";
    }

    public Srp6aResult(boolean ok, byte[] sessionKey, byte[] serverProof) {
        this.ok = ok;
        this.sessionKey = sessionKey;
        this.serverProof = serverProof;
    }

    public /* synthetic */ Srp6aResult(boolean z, byte[] bArr, byte[] bArr2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(z, (i & 2) != 0 ? null : bArr, (i & 4) != 0 ? null : bArr2);
    }

    public final boolean getOk() {
        return this.ok;
    }

    public final byte[] getSessionKey() {
        return this.sessionKey;
    }

    public final byte[] getServerProof() {
        return this.serverProof;
    }
}
