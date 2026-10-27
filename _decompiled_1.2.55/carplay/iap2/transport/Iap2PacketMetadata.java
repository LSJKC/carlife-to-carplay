package com.carplay.iap2.transport;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;

/* JADX INFO: compiled from: Iap2PacketMetadata.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u001d\b\u0086\b\u0018\u00002\u00020\u0001BI\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\u0005\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r¢\u0006\u0004\b\u000e\u0010\u000fJ\u0006\u0010\u001c\u001a\u00020\u0003J\t\u0010\u001d\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001e\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001f\u001a\u00020\u0005HÆ\u0003J\t\u0010 \u001a\u00020\u0005HÆ\u0003J\t\u0010!\u001a\u00020\u0005HÆ\u0003J\t\u0010\"\u001a\u00020\u0005HÆ\u0003J\t\u0010#\u001a\u00020\u000bHÆ\u0003J\t\u0010$\u001a\u00020\rHÆ\u0003JY\u0010%\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u00052\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\rHÆ\u0001J\u0013\u0010&\u001a\u00020\r2\b\u0010'\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010(\u001a\u00020\u0005HÖ\u0001J\t\u0010)\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013R\u0011\u0010\u0007\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0013R\u0011\u0010\b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0013R\u0011\u0010\t\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0013R\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\f\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001b¨\u0006*"}, d2 = {"Lcom/carplay/iap2/transport/Iap2PacketMetadata;", "", "action", "", "flags", "", "sequence", "acknowledgement", "sessionId", "length", "state", "Lcom/carplay/iap2/transport/Iap2LinkEngine$State;", "payloadChecksumValid", "", "<init>", "(Ljava/lang/String;IIIIILcom/carplay/iap2/transport/Iap2LinkEngine$State;Z)V", "getAction", "()Ljava/lang/String;", "getFlags", "()I", "getSequence", "getAcknowledgement", "getSessionId", "getLength", "getState", "()Lcom/carplay/iap2/transport/Iap2LinkEngine$State;", "getPayloadChecksumValid", "()Z", "diagnostic", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "other", "hashCode", "toString", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class Iap2PacketMetadata {
    private final int acknowledgement;
    private final String action;
    private final int flags;
    private final int length;
    private final boolean payloadChecksumValid;
    private final int sequence;
    private final int sessionId;
    private final Iap2LinkEngine.State state;

    public static /* synthetic */ Iap2PacketMetadata copy$default(Iap2PacketMetadata iap2PacketMetadata, String str, int i, int i2, int i3, int i4, int i5, Iap2LinkEngine.State state, boolean z, int i6, Object obj) {
        if ((i6 & 1) != 0) {
            str = iap2PacketMetadata.action;
        }
        if ((i6 & 2) != 0) {
            i = iap2PacketMetadata.flags;
        }
        if ((i6 & 4) != 0) {
            i2 = iap2PacketMetadata.sequence;
        }
        if ((i6 & 8) != 0) {
            i3 = iap2PacketMetadata.acknowledgement;
        }
        if ((i6 & 16) != 0) {
            i4 = iap2PacketMetadata.sessionId;
        }
        if ((i6 & 32) != 0) {
            i5 = iap2PacketMetadata.length;
        }
        if ((i6 & 64) != 0) {
            state = iap2PacketMetadata.state;
        }
        if ((i6 & 128) != 0) {
            z = iap2PacketMetadata.payloadChecksumValid;
        }
        Iap2LinkEngine.State state2 = state;
        boolean z2 = z;
        int i7 = i4;
        int i8 = i5;
        return iap2PacketMetadata.copy(str, i, i2, i3, i7, i8, state2, z2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getAction() {
        return this.action;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getFlags() {
        return this.flags;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int getSequence() {
        return this.sequence;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int getAcknowledgement() {
        return this.acknowledgement;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final int getSessionId() {
        return this.sessionId;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final int getLength() {
        return this.length;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final Iap2LinkEngine.State getState() {
        return this.state;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final boolean getPayloadChecksumValid() {
        return this.payloadChecksumValid;
    }

    public final Iap2PacketMetadata copy(String action, int flags, int sequence, int acknowledgement, int sessionId, int length, Iap2LinkEngine.State state, boolean payloadChecksumValid) {
        Intrinsics.checkNotNullParameter(action, "action");
        Intrinsics.checkNotNullParameter(state, "state");
        return new Iap2PacketMetadata(action, flags, sequence, acknowledgement, sessionId, length, state, payloadChecksumValid);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Iap2PacketMetadata)) {
            return false;
        }
        Iap2PacketMetadata iap2PacketMetadata = (Iap2PacketMetadata) other;
        return Intrinsics.areEqual(this.action, iap2PacketMetadata.action) && this.flags == iap2PacketMetadata.flags && this.sequence == iap2PacketMetadata.sequence && this.acknowledgement == iap2PacketMetadata.acknowledgement && this.sessionId == iap2PacketMetadata.sessionId && this.length == iap2PacketMetadata.length && this.state == iap2PacketMetadata.state && this.payloadChecksumValid == iap2PacketMetadata.payloadChecksumValid;
    }

    public int hashCode() {
        return (((((((((((((this.action.hashCode() * 31) + Integer.hashCode(this.flags)) * 31) + Integer.hashCode(this.sequence)) * 31) + Integer.hashCode(this.acknowledgement)) * 31) + Integer.hashCode(this.sessionId)) * 31) + Integer.hashCode(this.length)) * 31) + this.state.hashCode()) * 31) + Boolean.hashCode(this.payloadChecksumValid);
    }

    public String toString() {
        return "Iap2PacketMetadata(action=" + this.action + ", flags=" + this.flags + ", sequence=" + this.sequence + ", acknowledgement=" + this.acknowledgement + ", sessionId=" + this.sessionId + ", length=" + this.length + ", state=" + this.state + ", payloadChecksumValid=" + this.payloadChecksumValid + ")";
    }

    public Iap2PacketMetadata(String action, int flags, int sequence, int acknowledgement, int sessionId, int length, Iap2LinkEngine.State state, boolean payloadChecksumValid) {
        Intrinsics.checkNotNullParameter(action, "action");
        Intrinsics.checkNotNullParameter(state, "state");
        this.action = action;
        this.flags = flags;
        this.sequence = sequence;
        this.acknowledgement = acknowledgement;
        this.sessionId = sessionId;
        this.length = length;
        this.state = state;
        this.payloadChecksumValid = payloadChecksumValid;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ Iap2PacketMetadata(String str, int i, int i2, int i3, int i4, int i5, Iap2LinkEngine.State state, boolean z, int i6, DefaultConstructorMarker defaultConstructorMarker) {
        boolean z2;
        if ((i6 & 128) == 0) {
            z2 = z;
        } else {
            z2 = true;
        }
        this(str, i, i2, i3, i4, i5, state, z2);
    }

    public final String getAction() {
        return this.action;
    }

    public final int getFlags() {
        return this.flags;
    }

    public final int getSequence() {
        return this.sequence;
    }

    public final int getAcknowledgement() {
        return this.acknowledgement;
    }

    public final int getSessionId() {
        return this.sessionId;
    }

    public final int getLength() {
        return this.length;
    }

    public final Iap2LinkEngine.State getState() {
        return this.state;
    }

    public final boolean getPayloadChecksumValid() {
        return this.payloadChecksumValid;
    }

    public final String diagnostic() {
        String str = this.action;
        String string = Integer.toString(this.flags, CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return str + " flags=0x" + string + " seq=" + this.sequence + " ack=" + this.acknowledgement + " session=" + this.sessionId + " length=" + this.length + " state=" + this.state + " checksum=" + this.payloadChecksumValid;
    }
}
