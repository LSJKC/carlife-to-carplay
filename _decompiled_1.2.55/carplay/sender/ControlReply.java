package com.carplay.sender;

import androidx.core.app.NotificationCompat;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: RtspWire.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\r\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u001f\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0015"}, d2 = {"Lcom/carplay/sender/ControlReply;", "", NotificationCompat.CATEGORY_STATUS, "", "body", "", "<init>", "(I[B)V", "getStatus", "()I", "getBody", "()[B", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class ControlReply {
    private final byte[] body;
    private final int status;

    public static /* synthetic */ ControlReply copy$default(ControlReply controlReply, int i, byte[] bArr, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = controlReply.status;
        }
        if ((i2 & 2) != 0) {
            bArr = controlReply.body;
        }
        return controlReply.copy(i, bArr);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getStatus() {
        return this.status;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final byte[] getBody() {
        return this.body;
    }

    public final ControlReply copy(int status, byte[] body) {
        return new ControlReply(status, body);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ControlReply)) {
            return false;
        }
        ControlReply controlReply = (ControlReply) other;
        return this.status == controlReply.status && Intrinsics.areEqual(this.body, controlReply.body);
    }

    public int hashCode() {
        return (Integer.hashCode(this.status) * 31) + (this.body == null ? 0 : Arrays.hashCode(this.body));
    }

    public String toString() {
        return "ControlReply(status=" + this.status + ", body=" + Arrays.toString(this.body) + ")";
    }

    public ControlReply(int status, byte[] body) {
        this.status = status;
        this.body = body;
    }

    public /* synthetic */ ControlReply(int i, byte[] bArr, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, (i2 & 2) != 0 ? null : bArr);
    }

    public final byte[] getBody() {
        return this.body;
    }

    public final int getStatus() {
        return this.status;
    }
}
