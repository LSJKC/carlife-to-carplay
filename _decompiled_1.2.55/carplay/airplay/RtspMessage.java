package com.carplay.airplay;

import androidx.core.app.NotificationCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TuplesKt;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: RtspMessage.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0005\bÆ\u0002\u0018\u00002\u00020\u0001:\u0003\u001a\u001b\u001cB\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0005J\u0016\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011J'\u0010\u0012\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\bH\u0002¢\u0006\u0002\u0010\u0016J \u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u0005H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lcom/carplay/airplay/RtspMessage;", "", "<init>", "()V", "headerEnd", "", "statusText", "", "", "", "parseMessages", "Lcom/carplay/airplay/RtspMessage$Parsed;", "buffer", "buildResponse", "request", "Lcom/carplay/airplay/RtspMessage$Request;", "response", "Lcom/carplay/airplay/RtspMessage$Response;", "indexOf", "source", "target", "start", "([B[BI)Ljava/lang/Integer;", "matches", "", "offset", "Request", "Response", "Parsed", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class RtspMessage {
    public static final RtspMessage INSTANCE = new RtspMessage();
    private static final byte[] headerEnd;
    private static final Map<Integer, String> statusText;

    /* JADX INFO: compiled from: RtspMessage.kt */
    @Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010$\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B;\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0004\b\n\u0010\u000bJ\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\u0015\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u0007HÆ\u0003J\t\u0010\u0018\u001a\u00020\tHÆ\u0003JG\u0010\u0019\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\u0014\b\u0002\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u00072\b\b\u0002\u0010\b\u001a\u00020\tHÆ\u0001J\u0013\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001d\u001a\u00020\u001eHÖ\u0001J\t\u0010\u001f\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u001d\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013¨\u0006 "}, d2 = {"Lcom/carplay/airplay/RtspMessage$Request;", "", "method", "", "path", "protocol", "headers", "", "body", "", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;[B)V", "getMethod", "()Ljava/lang/String;", "getPath", "getProtocol", "getHeaders", "()Ljava/util/Map;", "getBody", "()[B", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "", "toString", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Request {
        private final byte[] body;
        private final Map<String, String> headers;
        private final String method;
        private final String path;
        private final String protocol;

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Request copy$default(Request request, String str, String str2, String str3, Map map, byte[] bArr, int i, Object obj) {
            if ((i & 1) != 0) {
                str = request.method;
            }
            if ((i & 2) != 0) {
                str2 = request.path;
            }
            if ((i & 4) != 0) {
                str3 = request.protocol;
            }
            if ((i & 8) != 0) {
                map = request.headers;
            }
            if ((i & 16) != 0) {
                bArr = request.body;
            }
            byte[] bArr2 = bArr;
            String str4 = str3;
            return request.copy(str, str2, str4, map, bArr2);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final String getMethod() {
            return this.method;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final String getPath() {
            return this.path;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final String getProtocol() {
            return this.protocol;
        }

        public final Map<String, String> component4() {
            return this.headers;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final byte[] getBody() {
            return this.body;
        }

        public final Request copy(String method, String path, String protocol, Map<String, String> headers, byte[] body) {
            Intrinsics.checkNotNullParameter(method, "method");
            Intrinsics.checkNotNullParameter(path, "path");
            Intrinsics.checkNotNullParameter(protocol, "protocol");
            Intrinsics.checkNotNullParameter(headers, "headers");
            Intrinsics.checkNotNullParameter(body, "body");
            return new Request(method, path, protocol, headers, body);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Request)) {
                return false;
            }
            Request request = (Request) other;
            return Intrinsics.areEqual(this.method, request.method) && Intrinsics.areEqual(this.path, request.path) && Intrinsics.areEqual(this.protocol, request.protocol) && Intrinsics.areEqual(this.headers, request.headers) && Intrinsics.areEqual(this.body, request.body);
        }

        public int hashCode() {
            return (((((((this.method.hashCode() * 31) + this.path.hashCode()) * 31) + this.protocol.hashCode()) * 31) + this.headers.hashCode()) * 31) + Arrays.hashCode(this.body);
        }

        public String toString() {
            return "Request(method=" + this.method + ", path=" + this.path + ", protocol=" + this.protocol + ", headers=" + this.headers + ", body=" + Arrays.toString(this.body) + ")";
        }

        public Request(String method, String path, String protocol, Map<String, String> headers, byte[] body) {
            Intrinsics.checkNotNullParameter(method, "method");
            Intrinsics.checkNotNullParameter(path, "path");
            Intrinsics.checkNotNullParameter(protocol, "protocol");
            Intrinsics.checkNotNullParameter(headers, "headers");
            Intrinsics.checkNotNullParameter(body, "body");
            this.method = method;
            this.path = path;
            this.protocol = protocol;
            this.headers = headers;
            this.body = body;
        }

        public final String getMethod() {
            return this.method;
        }

        public final String getPath() {
            return this.path;
        }

        public final String getProtocol() {
            return this.protocol;
        }

        public final Map<String, String> getHeaders() {
            return this.headers;
        }

        public final byte[] getBody() {
            return this.body;
        }
    }

    private RtspMessage() {
    }

    /* JADX INFO: compiled from: RtspMessage.kt */
    @Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010$\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0014\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001BK\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\u0014\b\u0002\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n¢\u0006\u0004\b\u000b\u0010\fJ\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010\u0010J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u0015\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\bHÆ\u0003J\t\u0010\u001b\u001a\u00020\nHÆ\u0003JR\u0010\u001c\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\u0014\b\u0002\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\b2\b\b\u0002\u0010\t\u001a\u00020\nHÆ\u0001¢\u0006\u0002\u0010\u001dJ\u0013\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010!\u001a\u00020\u0005HÖ\u0001J\t\u0010\"\u001a\u00020\u0003HÖ\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0015\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\n\n\u0002\u0010\u0011\u001a\u0004\b\u000f\u0010\u0010R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000eR\u001d\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\b¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016¨\u0006#"}, d2 = {"Lcom/carplay/airplay/RtspMessage$Response;", "", "protocol", "", NotificationCompat.CATEGORY_STATUS, "", "statusText", "headers", "", "body", "", "<init>", "(Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/util/Map;[B)V", "getProtocol", "()Ljava/lang/String;", "getStatus", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getStatusText", "getHeaders", "()Ljava/util/Map;", "getBody", "()[B", "component1", "component2", "component3", "component4", "component5", "copy", "(Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/util/Map;[B)Lcom/carplay/airplay/RtspMessage$Response;", "equals", "", "other", "hashCode", "toString", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Response {
        private final byte[] body;
        private final Map<String, String> headers;
        private final String protocol;
        private final Integer status;
        private final String statusText;

        public Response() {
            this(null, null, null, null, null, 31, null);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Response copy$default(Response response, String str, Integer num, String str2, Map map, byte[] bArr, int i, Object obj) {
            if ((i & 1) != 0) {
                str = response.protocol;
            }
            if ((i & 2) != 0) {
                num = response.status;
            }
            if ((i & 4) != 0) {
                str2 = response.statusText;
            }
            if ((i & 8) != 0) {
                map = response.headers;
            }
            if ((i & 16) != 0) {
                bArr = response.body;
            }
            byte[] bArr2 = bArr;
            String str3 = str2;
            return response.copy(str, num, str3, map, bArr2);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final String getProtocol() {
            return this.protocol;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final Integer getStatus() {
            return this.status;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final String getStatusText() {
            return this.statusText;
        }

        public final Map<String, String> component4() {
            return this.headers;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final byte[] getBody() {
            return this.body;
        }

        public final Response copy(String protocol, Integer status, String statusText, Map<String, String> headers, byte[] body) {
            Intrinsics.checkNotNullParameter(headers, "headers");
            Intrinsics.checkNotNullParameter(body, "body");
            return new Response(protocol, status, statusText, headers, body);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Response)) {
                return false;
            }
            Response response = (Response) other;
            return Intrinsics.areEqual(this.protocol, response.protocol) && Intrinsics.areEqual(this.status, response.status) && Intrinsics.areEqual(this.statusText, response.statusText) && Intrinsics.areEqual(this.headers, response.headers) && Intrinsics.areEqual(this.body, response.body);
        }

        public int hashCode() {
            return ((((((((this.protocol == null ? 0 : this.protocol.hashCode()) * 31) + (this.status == null ? 0 : this.status.hashCode())) * 31) + (this.statusText != null ? this.statusText.hashCode() : 0)) * 31) + this.headers.hashCode()) * 31) + Arrays.hashCode(this.body);
        }

        public String toString() {
            return "Response(protocol=" + this.protocol + ", status=" + this.status + ", statusText=" + this.statusText + ", headers=" + this.headers + ", body=" + Arrays.toString(this.body) + ")";
        }

        public Response(String protocol, Integer status, String statusText, Map<String, String> headers, byte[] body) {
            Intrinsics.checkNotNullParameter(headers, "headers");
            Intrinsics.checkNotNullParameter(body, "body");
            this.protocol = protocol;
            this.status = status;
            this.statusText = statusText;
            this.headers = headers;
            this.body = body;
        }

        public /* synthetic */ Response(String str, Integer num, String str2, Map map, byte[] bArr, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this((i & 1) != 0 ? null : str, (i & 2) != 0 ? null : num, (i & 4) != 0 ? null : str2, (i & 8) != 0 ? MapsKt.emptyMap() : map, (i & 16) != 0 ? new byte[0] : bArr);
        }

        public final String getProtocol() {
            return this.protocol;
        }

        public final Integer getStatus() {
            return this.status;
        }

        public final String getStatusText() {
            return this.statusText;
        }

        public final Map<String, String> getHeaders() {
            return this.headers;
        }

        public final byte[] getBody() {
            return this.body;
        }
    }

    /* JADX INFO: compiled from: RtspMessage.kt */
    @Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001d\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0004\b\u0007\u0010\bJ\u000f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\t\u0010\u000e\u001a\u00020\u0006HÆ\u0003J#\u0010\u000f\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u0017"}, d2 = {"Lcom/carplay/airplay/RtspMessage$Parsed;", "", "messages", "", "Lcom/carplay/airplay/RtspMessage$Request;", "rest", "", "<init>", "(Ljava/util/List;[B)V", "getMessages", "()Ljava/util/List;", "getRest", "()[B", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Parsed {
        private final List<Request> messages;
        private final byte[] rest;

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Parsed copy$default(Parsed parsed, List list, byte[] bArr, int i, Object obj) {
            if ((i & 1) != 0) {
                list = parsed.messages;
            }
            if ((i & 2) != 0) {
                bArr = parsed.rest;
            }
            return parsed.copy(list, bArr);
        }

        public final List<Request> component1() {
            return this.messages;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final byte[] getRest() {
            return this.rest;
        }

        public final Parsed copy(List<Request> messages, byte[] rest) {
            Intrinsics.checkNotNullParameter(messages, "messages");
            Intrinsics.checkNotNullParameter(rest, "rest");
            return new Parsed(messages, rest);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Parsed)) {
                return false;
            }
            Parsed parsed = (Parsed) other;
            return Intrinsics.areEqual(this.messages, parsed.messages) && Intrinsics.areEqual(this.rest, parsed.rest);
        }

        public int hashCode() {
            return (this.messages.hashCode() * 31) + Arrays.hashCode(this.rest);
        }

        public String toString() {
            return "Parsed(messages=" + this.messages + ", rest=" + Arrays.toString(this.rest) + ")";
        }

        public Parsed(List<Request> messages, byte[] rest) {
            Intrinsics.checkNotNullParameter(messages, "messages");
            Intrinsics.checkNotNullParameter(rest, "rest");
            this.messages = messages;
            this.rest = rest;
        }

        public final List<Request> getMessages() {
            return this.messages;
        }

        public final byte[] getRest() {
            return this.rest;
        }
    }

    static {
        byte[] bytes = "\r\n\r\n".getBytes(Charsets.US_ASCII);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        headerEnd = bytes;
        statusText = MapsKt.mapOf(TuplesKt.to(200, "OK"), TuplesKt.to(Integer.valueOf(AirPlayDisplaySettings.MAX_WIDTH_PHYSICAL_MM), "Bad Request"), TuplesKt.to(404, "Not Found"), TuplesKt.to(500, "Internal Server Error"));
    }

    public final Parsed parseMessages(byte[] buffer) {
        Integer numIndexOf;
        Integer intOrNull;
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        ArrayList messages = new ArrayList();
        int offset = 0;
        while (offset < buffer.length && (numIndexOf = indexOf(buffer, headerEnd, offset)) != null) {
            int headerEndIndex = numIndexOf.intValue();
            String headerText = new String(buffer, offset, headerEndIndex - offset, Charsets.US_ASCII);
            int i = 0;
            List lines = StringsKt.split$default((CharSequence) headerText, new String[]{"\r\n"}, false, 0, 6, (Object) null);
            if (lines.isEmpty()) {
                offset = headerEndIndex + headerEnd.length;
            } else {
                List requestLine = StringsKt.split$default((CharSequence) CollectionsKt.first(lines), new String[]{" "}, false, 3, 2, (Object) null);
                HashMap headers = new HashMap();
                for (String line : CollectionsKt.drop(lines, 1)) {
                    int separator = StringsKt.indexOf$default((CharSequence) line, ':', 0, false, 6, (Object) null);
                    if (separator != -1) {
                        String strSubstring = line.substring(i, separator);
                        Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
                        String lowerCase = StringsKt.trim((CharSequence) strSubstring).toString().toLowerCase(Locale.ROOT);
                        Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
                        String strSubstring2 = line.substring(separator + 1);
                        Intrinsics.checkNotNullExpressionValue(strSubstring2, "substring(...)");
                        headers.put(lowerCase, StringsKt.trim((CharSequence) strSubstring2).toString());
                        i = 0;
                    } else {
                        i = 0;
                    }
                }
                int bodyStart = headerEnd.length + headerEndIndex;
                String str = (String) headers.get("content-length");
                int contentLength = (str == null || (intOrNull = StringsKt.toIntOrNull(str)) == null) ? 0 : RangesKt.coerceAtLeast(intOrNull.intValue(), 0);
                int bodyEnd = bodyStart + contentLength;
                if (bodyEnd > buffer.length) {
                    break;
                }
                messages.add(new Request((String) requestLine.get(0), (String) (1 < requestLine.size() ? requestLine.get(1) : ""), (String) (2 < requestLine.size() ? requestLine.get(2) : "RTSP/1.0"), headers, ArraysKt.copyOfRange(buffer, bodyStart, bodyEnd)));
                offset = bodyEnd;
            }
        }
        return new Parsed(messages, ArraysKt.copyOfRange(buffer, offset, buffer.length));
    }

    public final byte[] buildResponse(Request request, Response response) {
        Intrinsics.checkNotNullParameter(request, "request");
        Intrinsics.checkNotNullParameter(response, "response");
        String protocol = response.getProtocol();
        if (protocol == null) {
            protocol = request.getProtocol();
        }
        Integer status = response.getStatus();
        int status2 = status != null ? status.intValue() : 200;
        String statusLine = response.getStatusText();
        if (statusLine == null && (statusLine = statusText.get(Integer.valueOf(status2))) == null) {
            statusLine = "OK";
        }
        byte[] body = response.getBody();
        LinkedHashMap headers = new LinkedHashMap(response.getHeaders());
        String it = request.getHeaders().get("cseq");
        if (it != null) {
            headers.put("CSeq", it);
        }
        headers.put("Content-Length", String.valueOf(body.length));
        StringBuilder $this$buildResponse_u24lambda_u243 = new StringBuilder();
        $this$buildResponse_u24lambda_u243.append(protocol).append(' ').append(status2).append(' ').append(statusLine).append("\r\n");
        for (Map.Entry entry : headers.entrySet()) {
            String name = (String) entry.getKey();
            String value = (String) entry.getValue();
            $this$buildResponse_u24lambda_u243.append(name).append(": ").append(value).append("\r\n");
        }
        $this$buildResponse_u24lambda_u243.append("\r\n");
        String head = $this$buildResponse_u24lambda_u243.toString();
        byte[] bytes = head.getBytes(Charsets.US_ASCII);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        return ArraysKt.plus(bytes, body);
    }

    private final Integer indexOf(byte[] source, byte[] target, int start) {
        if (target.length == 0) {
            return Integer.valueOf(start);
        }
        byte first = target[0];
        int index = start;
        int length = source.length - target.length;
        if (index > length) {
            return null;
        }
        while (true) {
            if (source[index] == first && matches(source, index, target)) {
                return Integer.valueOf(index);
            }
            if (index == length) {
                return null;
            }
            index++;
        }
    }

    private final boolean matches(byte[] source, int offset, byte[] target) {
        int length = target.length;
        for (int index = 0; index < length; index++) {
            if (source[offset + index] != target[index]) {
                return false;
            }
        }
        return true;
    }
}
