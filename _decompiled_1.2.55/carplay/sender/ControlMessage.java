package com.carplay.sender;

import androidx.core.app.NotificationCompat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: RtspWire.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0010\u0012\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\u0015\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u0005HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0007HÆ\u0003J3\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u0014\b\u0002\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001b\u001a\u00020\u0011HÖ\u0001J\t\u0010\u001c\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001d\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\u0010\u001a\u0004\u0018\u00010\u00118F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013¨\u0006\u001d"}, d2 = {"Lcom/carplay/sender/ControlMessage;", "", "firstLine", "", "headers", "", "body", "", "<init>", "(Ljava/lang/String;Ljava/util/Map;[B)V", "getFirstLine", "()Ljava/lang/String;", "getHeaders", "()Ljava/util/Map;", "getBody", "()[B", NotificationCompat.CATEGORY_STATUS, "", "getStatus", "()Ljava/lang/Integer;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class ControlMessage {
    private final byte[] body;
    private final String firstLine;
    private final Map<String, String> headers;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ ControlMessage copy$default(ControlMessage controlMessage, String str, Map map, byte[] bArr, int i, Object obj) {
        if ((i & 1) != 0) {
            str = controlMessage.firstLine;
        }
        if ((i & 2) != 0) {
            map = controlMessage.headers;
        }
        if ((i & 4) != 0) {
            bArr = controlMessage.body;
        }
        return controlMessage.copy(str, map, bArr);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getFirstLine() {
        return this.firstLine;
    }

    public final Map<String, String> component2() {
        return this.headers;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final byte[] getBody() {
        return this.body;
    }

    public final ControlMessage copy(String firstLine, Map<String, String> headers, byte[] body) {
        Intrinsics.checkNotNullParameter(firstLine, "firstLine");
        Intrinsics.checkNotNullParameter(headers, "headers");
        Intrinsics.checkNotNullParameter(body, "body");
        return new ControlMessage(firstLine, headers, body);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ControlMessage)) {
            return false;
        }
        ControlMessage controlMessage = (ControlMessage) other;
        return Intrinsics.areEqual(this.firstLine, controlMessage.firstLine) && Intrinsics.areEqual(this.headers, controlMessage.headers) && Intrinsics.areEqual(this.body, controlMessage.body);
    }

    public int hashCode() {
        return (((this.firstLine.hashCode() * 31) + this.headers.hashCode()) * 31) + Arrays.hashCode(this.body);
    }

    public String toString() {
        return "ControlMessage(firstLine=" + this.firstLine + ", headers=" + this.headers + ", body=" + Arrays.toString(this.body) + ")";
    }

    public ControlMessage(String firstLine, Map<String, String> headers, byte[] body) {
        Intrinsics.checkNotNullParameter(firstLine, "firstLine");
        Intrinsics.checkNotNullParameter(headers, "headers");
        Intrinsics.checkNotNullParameter(body, "body");
        this.firstLine = firstLine;
        this.headers = headers;
        this.body = body;
    }

    public final byte[] getBody() {
        return this.body;
    }

    public final String getFirstLine() {
        return this.firstLine;
    }

    public final Map<String, String> getHeaders() {
        return this.headers;
    }

    public final Integer getStatus() {
        String str;
        List it = StringsKt.split$default((CharSequence) this.firstLine, new char[]{' '}, false, 0, 6, (Object) null);
        if (!(StringsKt.startsWith$default((String) CollectionsKt.first(it), "RTSP/", false, 2, (Object) null) || StringsKt.startsWith$default((String) CollectionsKt.first(it), "HTTP/", false, 2, (Object) null))) {
            it = null;
        }
        if (it == null || (str = (String) CollectionsKt.getOrNull(it, 1)) == null) {
            return null;
        }
        return StringsKt.toIntOrNull(str);
    }
}
