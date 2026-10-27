package com.carplay.sender;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: SenderNegotiation.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\u000bJ$\u0010\u000f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005HÆ\u0001¢\u0006\u0002\u0010\u0010J\u0013\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0014\u001a\u00020\u0005HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0015\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000b¨\u0006\u0017"}, d2 = {"Lcom/carplay/sender/SenderControlTransport;", "", "kind", "Lcom/carplay/sender/SenderControlKind;", "port", "", "<init>", "(Lcom/carplay/sender/SenderControlKind;Ljava/lang/Integer;)V", "getKind", "()Lcom/carplay/sender/SenderControlKind;", "getPort", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "component1", "component2", "copy", "(Lcom/carplay/sender/SenderControlKind;Ljava/lang/Integer;)Lcom/carplay/sender/SenderControlTransport;", "equals", "", "other", "hashCode", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class SenderControlTransport {
    private final SenderControlKind kind;
    private final Integer port;

    public static /* synthetic */ SenderControlTransport copy$default(SenderControlTransport senderControlTransport, SenderControlKind senderControlKind, Integer num, int i, Object obj) {
        if ((i & 1) != 0) {
            senderControlKind = senderControlTransport.kind;
        }
        if ((i & 2) != 0) {
            num = senderControlTransport.port;
        }
        return senderControlTransport.copy(senderControlKind, num);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final SenderControlKind getKind() {
        return this.kind;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final Integer getPort() {
        return this.port;
    }

    public final SenderControlTransport copy(SenderControlKind kind, Integer port) {
        Intrinsics.checkNotNullParameter(kind, "kind");
        return new SenderControlTransport(kind, port);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SenderControlTransport)) {
            return false;
        }
        SenderControlTransport senderControlTransport = (SenderControlTransport) other;
        return this.kind == senderControlTransport.kind && Intrinsics.areEqual(this.port, senderControlTransport.port);
    }

    public int hashCode() {
        return (this.kind.hashCode() * 31) + (this.port == null ? 0 : this.port.hashCode());
    }

    public String toString() {
        return "SenderControlTransport(kind=" + this.kind + ", port=" + this.port + ")";
    }

    public SenderControlTransport(SenderControlKind kind, Integer port) {
        Intrinsics.checkNotNullParameter(kind, "kind");
        this.kind = kind;
        this.port = port;
    }

    public /* synthetic */ SenderControlTransport(SenderControlKind senderControlKind, Integer num, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(senderControlKind, (i & 2) != 0 ? null : num);
    }

    public final SenderControlKind getKind() {
        return this.kind;
    }

    public final Integer getPort() {
        return this.port;
    }
}
