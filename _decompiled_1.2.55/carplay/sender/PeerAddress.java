package com.carplay.sender;

import java.net.InetAddress;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PeerAddresses.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\b¨\u0006\u0014"}, d2 = {"Lcom/carplay/sender/PeerAddress;", "", "remote", "Ljava/net/InetAddress;", "local", "<init>", "(Ljava/net/InetAddress;Ljava/net/InetAddress;)V", "getRemote", "()Ljava/net/InetAddress;", "getLocal", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class PeerAddress {
    private final InetAddress local;
    private final InetAddress remote;

    public static /* synthetic */ PeerAddress copy$default(PeerAddress peerAddress, InetAddress inetAddress, InetAddress inetAddress2, int i, Object obj) {
        if ((i & 1) != 0) {
            inetAddress = peerAddress.remote;
        }
        if ((i & 2) != 0) {
            inetAddress2 = peerAddress.local;
        }
        return peerAddress.copy(inetAddress, inetAddress2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final InetAddress getRemote() {
        return this.remote;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final InetAddress getLocal() {
        return this.local;
    }

    public final PeerAddress copy(InetAddress remote, InetAddress local) {
        Intrinsics.checkNotNullParameter(remote, "remote");
        Intrinsics.checkNotNullParameter(local, "local");
        return new PeerAddress(remote, local);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PeerAddress)) {
            return false;
        }
        PeerAddress peerAddress = (PeerAddress) other;
        return Intrinsics.areEqual(this.remote, peerAddress.remote) && Intrinsics.areEqual(this.local, peerAddress.local);
    }

    public int hashCode() {
        return (this.remote.hashCode() * 31) + this.local.hashCode();
    }

    public String toString() {
        return "PeerAddress(remote=" + this.remote + ", local=" + this.local + ")";
    }

    public PeerAddress(InetAddress remote, InetAddress local) {
        Intrinsics.checkNotNullParameter(remote, "remote");
        Intrinsics.checkNotNullParameter(local, "local");
        this.remote = remote;
        this.local = local;
    }

    public final InetAddress getLocal() {
        return this.local;
    }

    public final InetAddress getRemote() {
        return this.remote;
    }
}
