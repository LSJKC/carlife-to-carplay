package com.carplay.sender;

import androidx.core.app.NotificationCompat;
import com.carplay.airplay.AirPlayDisplaySettings;
import com.carplay.airplay.AirPlaySession;
import com.carplay.airplay.ControlCipher;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.text.Charsets;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: RtspWire.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\u0080\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010$\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001BG\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003\u0012\u0014\b\u0002\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\b0\n¢\u0006\u0004\b\f\u0010\rB'\b\u0016\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0014\b\u0002\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\b0\n¢\u0006\u0004\b\f\u0010\u0010J$\u00107\u001a\u00020\b2\u0006\u00108\u001a\u00020\u00162\u0014\b\u0002\u00109\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0:J6\u0010;\u001a\u00020\b2\u0006\u0010<\u001a\u00020\u000b2\u0006\u0010=\u001a\u00020\u000b2\b\b\u0002\u00108\u001a\u00020\u00162\u0014\b\u0002\u00109\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0:JN\u0010>\u001a\u00020\u001a2\u0006\u0010<\u001a\u00020\u000b2\u0006\u0010=\u001a\u00020\u000b2\b\b\u0002\u00108\u001a\u00020\u00162\u0014\b\u0002\u00109\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0:2\u0016\b\u0002\u0010?\u001a\u0010\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\b\u0018\u00010\nJ\u0006\u0010@\u001a\u00020\u001aJ\"\u0010A\u001a\u00020\b2\u0006\u0010>\u001a\u00020\u001a2\b\u00108\u001a\u0004\u0018\u00010\u00162\b\b\u0002\u0010B\u001a\u00020CJ,\u0010D\u001a\u00020\b2\u0006\u0010E\u001a\u00020\u000b2\u0012\u00109\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000b0:2\u0006\u00108\u001a\u00020\u0016H\u0002J\b\u0010F\u001a\u00020\u0012H\u0002J\b\u0010G\u001a\u00020\bH\u0016R\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00060\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\b0\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0016X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0019X\u0082\u0004¢\u0006\u0002\n\u0000RN\u0010\u001b\u001aB\u0012\f\u0012\n \u001d*\u0004\u0018\u00010\u000b0\u000b\u0012\f\u0012\n \u001d*\u0004\u0018\u00010\u001e0\u001e \u001d* \u0012\f\u0012\n \u001d*\u0004\u0018\u00010\u000b0\u000b\u0012\f\u0012\n \u001d*\u0004\u0018\u00010\u001e0\u001e\u0018\u00010\u001c0\u001cX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010 \u001a\u0004\u0018\u00010!X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R&\u0010&\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020'0\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R&\u0010,\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\b0\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b-\u0010)\"\u0004\b.\u0010+R&\u0010/\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\b0\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b0\u0010)\"\u0004\b1\u0010+R\u001c\u00102\u001a\u0004\u0018\u00010\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b3\u00104\"\u0004\b5\u00106¨\u0006H"}, d2 = {"Lcom/carplay/sender/RtspWire;", "Ljava/io/Closeable;", "input", "Lkotlin/Function0;", "Ljava/io/InputStream;", "output", "Ljava/io/OutputStream;", "release", "", "trace", "Lkotlin/Function1;", "", "<init>", "(Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;)V", "socket", "Ljava/net/Socket;", "(Ljava/net/Socket;Lkotlin/jvm/functions/Function1;)V", "sequence", "", "sequenceLock", "", "plaintext", "", "encrypted", "pending", "Ljava/util/ArrayDeque;", "Lcom/carplay/sender/ControlMessage;", "ignoredResponses", "Ljava/util/concurrent/ConcurrentHashMap$KeySetView;", "kotlin.jvm.PlatformType", "", "writeLock", "cipher", "Lcom/carplay/airplay/ControlCipher;", "getCipher", "()Lcom/carplay/airplay/ControlCipher;", "setCipher", "(Lcom/carplay/airplay/ControlCipher;)V", "onRequest", "Lcom/carplay/sender/ControlReply;", "getOnRequest", "()Lkotlin/jvm/functions/Function1;", "setOnRequest", "(Lkotlin/jvm/functions/Function1;)V", "onResponse", "getOnResponse", "setOnResponse", "onIgnoredResponse", "getOnIgnoredResponse", "setOnIgnoredResponse", "session", "getSession", "()Ljava/lang/String;", "setSession", "(Ljava/lang/String;)V", "sendCommand", "body", "headers", "", "sendAndForget", "method", "path", "request", "receive", "next", "respond", NotificationCompat.CATEGORY_STATUS, "", "write", "firstLine", "nextSequence", "close", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class RtspWire implements Closeable {
    private ControlCipher cipher;
    private byte[] encrypted;
    private final ConcurrentHashMap.KeySetView<String, Boolean> ignoredResponses;
    private final Function0<InputStream> input;
    private Function1<? super ControlMessage, Unit> onIgnoredResponse;
    private Function1<? super ControlMessage, ControlReply> onRequest;
    private Function1<? super ControlMessage, Unit> onResponse;
    private final Function0<OutputStream> output;
    private final ArrayDeque<ControlMessage> pending;
    private byte[] plaintext;
    private final Function0<Unit> release;
    private long sequence;
    private final Object sequenceLock;
    private String session;
    private final Function1<String, Unit> trace;
    private final Object writeLock;

    /* JADX WARN: Multi-variable type inference failed */
    public RtspWire(Function0<? extends InputStream> input, Function0<? extends OutputStream> output, Function0<Unit> release, Function1<? super String, Unit> trace) {
        Intrinsics.checkNotNullParameter(input, "input");
        Intrinsics.checkNotNullParameter(output, "output");
        Intrinsics.checkNotNullParameter(release, "release");
        Intrinsics.checkNotNullParameter(trace, "trace");
        this.input = input;
        this.output = output;
        this.release = release;
        this.trace = trace;
        this.sequenceLock = new Object();
        this.plaintext = new byte[0];
        this.encrypted = new byte[0];
        this.pending = new ArrayDeque<>();
        this.ignoredResponses = ConcurrentHashMap.newKeySet();
        this.writeLock = new Object();
        this.onRequest = new Function1() { // from class: com.carplay.sender.RtspWire$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RtspWire.onRequest$lambda$2((ControlMessage) obj);
            }
        };
        this.onResponse = new Function1() { // from class: com.carplay.sender.RtspWire$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RtspWire.onResponse$lambda$3((ControlMessage) obj);
            }
        };
        this.onIgnoredResponse = new Function1() { // from class: com.carplay.sender.RtspWire$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RtspWire.onIgnoredResponse$lambda$4((ControlMessage) obj);
            }
        };
    }

    public /* synthetic */ RtspWire(Function0 function0, Function0 function1, Function0 function2, Function1 function3, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((Function0<? extends InputStream>) function0, (Function0<? extends OutputStream>) function1, (Function0<Unit>) function2, (Function1<? super String, Unit>) ((i & 8) != 0 ? new Function1() { // from class: com.carplay.sender.RtspWire$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RtspWire._init_$lambda$0((String) obj);
            }
        } : function3));
    }

    static final Unit _init_$lambda$0(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: com.carplay.sender.RtspWire$3, reason: invalid class name */
    /* JADX INFO: compiled from: RtspWire.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass3 extends FunctionReferenceImpl implements Function0<InputStream> {
        AnonymousClass3(Object obj) {
            super(0, obj, Socket.class, "getInputStream", "getInputStream()Ljava/io/InputStream;", 0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        public final InputStream invoke() {
            return ((Socket) this.receiver).getInputStream();
        }
    }

    /* JADX INFO: renamed from: com.carplay.sender.RtspWire$4, reason: invalid class name */
    /* JADX INFO: compiled from: RtspWire.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass4 extends FunctionReferenceImpl implements Function0<OutputStream> {
        AnonymousClass4(Object obj) {
            super(0, obj, Socket.class, "getOutputStream", "getOutputStream()Ljava/io/OutputStream;", 0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        public final OutputStream invoke() {
            return ((Socket) this.receiver).getOutputStream();
        }
    }

    /* JADX INFO: renamed from: com.carplay.sender.RtspWire$5, reason: invalid class name */
    /* JADX INFO: compiled from: RtspWire.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    static final /* synthetic */ class AnonymousClass5 extends FunctionReferenceImpl implements Function0<Unit> {
        AnonymousClass5(Object obj) {
            super(0, obj, Socket.class, "close", "close()V", 0);
        }

        @Override // kotlin.jvm.functions.Function0
        public /* bridge */ /* synthetic */ Unit invoke() throws IOException {
            invoke2();
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2() throws IOException {
            ((Socket) this.receiver).close();
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public RtspWire(Socket socket, Function1<? super String, Unit> trace) {
        this(new AnonymousClass3(socket), new AnonymousClass4(socket), new AnonymousClass5(socket), trace);
        Intrinsics.checkNotNullParameter(socket, "socket");
        Intrinsics.checkNotNullParameter(trace, "trace");
    }

    public /* synthetic */ RtspWire(Socket socket, Function1 function1, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(socket, (i & 2) != 0 ? new Function1() { // from class: com.carplay.sender.RtspWire$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return RtspWire._init_$lambda$1((String) obj);
            }
        } : function1);
    }

    static final Unit _init_$lambda$1(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    public final ControlCipher getCipher() {
        return this.cipher;
    }

    public final void setCipher(ControlCipher controlCipher) {
        this.cipher = controlCipher;
    }

    static final ControlReply onRequest$lambda$2(ControlMessage it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return new ControlReply(501, null, 2, null);
    }

    public final Function1<ControlMessage, ControlReply> getOnRequest() {
        return this.onRequest;
    }

    public final void setOnRequest(Function1<? super ControlMessage, ControlReply> function1) {
        Intrinsics.checkNotNullParameter(function1, "<set-?>");
        this.onRequest = function1;
    }

    static final Unit onResponse$lambda$3(ControlMessage it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    public final Function1<ControlMessage, Unit> getOnResponse() {
        return this.onResponse;
    }

    public final void setOnResponse(Function1<? super ControlMessage, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "<set-?>");
        this.onResponse = function1;
    }

    static final Unit onIgnoredResponse$lambda$4(ControlMessage it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    public final Function1<ControlMessage, Unit> getOnIgnoredResponse() {
        return this.onIgnoredResponse;
    }

    public final void setOnIgnoredResponse(Function1<? super ControlMessage, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "<set-?>");
        this.onIgnoredResponse = function1;
    }

    public final String getSession() {
        return this.session;
    }

    public final void setSession(String str) {
        this.session = str;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void sendCommand$default(RtspWire rtspWire, byte[] bArr, Map map, int i, Object obj) {
        if ((i & 2) != 0) {
            map = MapsKt.emptyMap();
        }
        rtspWire.sendCommand(bArr, map);
    }

    public final synchronized void sendCommand(byte[] body, Map<String, String> headers) {
        Intrinsics.checkNotNullParameter(body, "body");
        Intrinsics.checkNotNullParameter(headers, "headers");
        long seq = nextSequence();
        LinkedHashMap all = MapsKt.linkedMapOf(TuplesKt.to("CSeq", String.valueOf(seq)), TuplesKt.to("User-Agent", "AirPlay/320.17"), TuplesKt.to("Content-Length", String.valueOf(body.length)));
        String it = this.session;
        if (it != null) {
            all.put("Session", it);
        }
        all.putAll(headers);
        write("POST /command RTSP/1.0", all, body);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ void sendAndForget$default(RtspWire rtspWire, String str, String str2, byte[] bArr, Map map, int i, Object obj) throws Throwable {
        if ((i & 4) != 0) {
            bArr = new byte[0];
        }
        if ((i & 8) != 0) {
            map = MapsKt.emptyMap();
        }
        rtspWire.sendAndForget(str, str2, bArr, map);
    }

    public final void sendAndForget(String method, String path, byte[] body, Map<String, String> headers) throws Throwable {
        Object objM159constructorimpl;
        Intrinsics.checkNotNullParameter(method, "method");
        Intrinsics.checkNotNullParameter(path, "path");
        Intrinsics.checkNotNullParameter(body, "body");
        Intrinsics.checkNotNullParameter(headers, "headers");
        if (!((!new Regex("[A-Z_]+").matches(method) || StringsKt.contains$default((CharSequence) path, '\r', false, 2, (Object) null) || StringsKt.contains$default((CharSequence) path, '\n', false, 2, (Object) null)) ? false : true)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        String seq = String.valueOf(nextSequence());
        ConcurrentHashMap.KeySetView<String, Boolean> ignoredResponses = this.ignoredResponses;
        Intrinsics.checkNotNullExpressionValue(ignoredResponses, "ignoredResponses");
        ignoredResponses.add(seq);
        LinkedHashMap all = MapsKt.linkedMapOf(TuplesKt.to("CSeq", seq), TuplesKt.to("User-Agent", "AirPlay/320.17"), TuplesKt.to("Content-Length", String.valueOf(body.length)));
        String it = this.session;
        if (it != null) {
            all.put("Session", it);
        }
        all.putAll(headers);
        try {
            Result.Companion companion = Result.INSTANCE;
            RtspWire $this$sendAndForget_u24lambda_u247 = this;
            $this$sendAndForget_u24lambda_u247.write(method + " " + path + " RTSP/1.0", all, body);
            objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        Throwable it2 = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (it2 != null) {
            this.ignoredResponses.remove(seq);
            throw it2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ ControlMessage request$default(RtspWire rtspWire, String str, String str2, byte[] bArr, Map map, Function1 function1, int i, Object obj) {
        if ((i & 4) != 0) {
            bArr = new byte[0];
        }
        byte[] bArr2 = bArr;
        if ((i & 8) != 0) {
            map = MapsKt.emptyMap();
        }
        Map map2 = map;
        if ((i & 16) != 0) {
            function1 = null;
        }
        return rtspWire.request(str, str2, bArr2, map2, function1);
    }

    public final synchronized ControlMessage request(String method, String path, byte[] body, Map<String, String> headers, Function1<? super ControlMessage, Unit> receive) {
        ControlMessage message;
        Intrinsics.checkNotNullParameter(method, "method");
        Intrinsics.checkNotNullParameter(path, "path");
        Intrinsics.checkNotNullParameter(body, "body");
        Intrinsics.checkNotNullParameter(headers, "headers");
        if (!((!new Regex("[A-Z_]+").matches(method) || StringsKt.contains$default((CharSequence) path, '\r', false, 2, (Object) null) || StringsKt.contains$default((CharSequence) path, '\n', false, 2, (Object) null)) ? false : true)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        long seq = nextSequence();
        LinkedHashMap all = MapsKt.linkedMapOf(TuplesKt.to("CSeq", String.valueOf(seq)), TuplesKt.to("User-Agent", "AirPlay/320.17"), TuplesKt.to("Content-Length", String.valueOf(body.length)));
        String it = this.session;
        if (it != null) {
            all.put("Session", it);
        }
        all.putAll(headers);
        write(method + " " + path + " RTSP/1.0", all, body);
        while (true) {
            message = next();
            if (message.getStatus() == null) {
                ControlReply reply = this.onRequest.invoke(message);
                respond(message, reply.getBody(), reply.getStatus());
            } else {
                String responseSequence = message.getHeaders().get("cseq");
                if (responseSequence == null) {
                    responseSequence = "";
                }
                if (Intrinsics.areEqual(responseSequence, String.valueOf(seq))) {
                    break;
                }
                if (!this.ignoredResponses.remove(responseSequence)) {
                    if (Intrinsics.areEqual(responseSequence, String.valueOf(seq))) {
                        break;
                    }
                    throw new IllegalStateException("控制响应序号不匹配".toString());
                }
                this.onIgnoredResponse.invoke(message);
            }
        }
        this.trace.invoke(method + " " + path + " → " + message.getStatus() + ", body=" + message.getBody().length);
        IntRange intRange = new IntRange(200, 299);
        Integer status = message.getStatus();
        if (!(status != null && intRange.contains(status.intValue()))) {
            throw new IllegalStateException(("车机拒绝 " + method + " " + path + "：" + message.getStatus()).toString());
        }
        String it2 = message.getHeaders().get("session");
        if (it2 != null) {
            this.session = StringsKt.substringBefore$default(it2, ';', (String) null, 2, (Object) null);
        }
        (receive == null ? this.onResponse : receive).invoke(message);
        return message;
    }

    public final ControlMessage next() throws IOException {
        while (this.pending.isEmpty()) {
            byte[] bytes = new byte[8192];
            int count = this.input.invoke().read(bytes);
            if (count < 0) {
                throw new EOFException("控制通道关闭");
            }
            byte[] part = Arrays.copyOf(bytes, count);
            Intrinsics.checkNotNullExpressionValue(part, "copyOf(...)");
            ControlCipher active = this.cipher;
            if (active == null) {
                this.plaintext = ArraysKt.plus(this.plaintext, part);
            } else {
                this.encrypted = ArraysKt.plus(this.encrypted, part);
                if (!(this.encrypted.length <= 2200000)) {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
                ControlCipher.Decrypted result = active.decrypt(this.encrypted);
                this.encrypted = result.getRest();
                this.plaintext = ArraysKt.plus(this.plaintext, result.getData());
            }
            ControlFraming.Parsed parsed = ControlFraming.INSTANCE.parse(this.plaintext);
            this.plaintext = parsed.getRest();
            this.pending.addAll(parsed.getMessages());
        }
        ControlMessage controlMessageRemoveFirst = this.pending.removeFirst();
        Intrinsics.checkNotNullExpressionValue(controlMessageRemoveFirst, "removeFirst(...)");
        return controlMessageRemoveFirst;
    }

    public static /* synthetic */ void respond$default(RtspWire rtspWire, ControlMessage controlMessage, byte[] bArr, int i, int i2, Object obj) throws Throwable {
        if ((i2 & 4) != 0) {
            i = 200;
        }
        rtspWire.respond(controlMessage, bArr, i);
    }

    public final void respond(ControlMessage request, byte[] body, int status) throws Throwable {
        String reason;
        Intrinsics.checkNotNullParameter(request, "request");
        String strSubstringAfterLast$default = StringsKt.substringAfterLast$default(request.getFirstLine(), ' ', (String) null, 2, (Object) null);
        String protocol = "HTTP/1.0";
        if (!Intrinsics.areEqual(strSubstringAfterLast$default, "HTTP/1.0")) {
            protocol = "HTTP/1.1";
            if (!Intrinsics.areEqual(strSubstringAfterLast$default, "HTTP/1.1")) {
                protocol = "RTSP/1.0";
            }
        }
        LinkedHashMap headers = new LinkedHashMap();
        if (Intrinsics.areEqual(protocol, "RTSP/1.0") || request.getHeaders().containsKey("cseq")) {
            LinkedHashMap linkedHashMap = headers;
            String str = request.getHeaders().get("cseq");
            if (str == null) {
                str = "";
            }
            linkedHashMap.put("CSeq", str);
        }
        headers.put("Content-Length", String.valueOf(body != null ? body.length : 0));
        if (body != null) {
            headers.put("Content-Type", AirPlaySession.PLIST_CONTENT_TYPE);
        }
        switch (status) {
            case 200:
                reason = "OK";
                break;
            case AirPlayDisplaySettings.MAX_WIDTH_PHYSICAL_MM /* 400 */:
                reason = "Bad Request";
                break;
            case 403:
                reason = "Forbidden";
                break;
            default:
                reason = "Not Implemented";
                break;
        }
        write(protocol + " " + status + " " + reason, headers, body == null ? new byte[0] : body);
    }

    private final void write(String firstLine, Map<String, String> headers, byte[] body) throws Throwable {
        byte[] bArrEncrypt;
        synchronized (this.writeLock) {
            try {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    String k = entry.getKey();
                    String v = entry.getValue();
                    boolean z = false;
                    if (!StringsKt.contains$default((CharSequence) k, '\r', false, 2, (Object) null) && !StringsKt.contains$default((CharSequence) k, '\n', false, 2, (Object) null) && !StringsKt.contains$default((CharSequence) v, '\r', false, 2, (Object) null) && !StringsKt.contains$default((CharSequence) v, '\n', false, 2, (Object) null)) {
                        z = true;
                    }
                    if (!z) {
                        throw new IllegalArgumentException("Failed requirement.".toString());
                    }
                }
                StringBuilder $this$write_u24lambda_u2416_u24lambda_u2415 = new StringBuilder();
                try {
                    $this$write_u24lambda_u2416_u24lambda_u2415.append(firstLine).append("\r\n");
                    for (Map.Entry<String, String> entry2 : headers.entrySet()) {
                        $this$write_u24lambda_u2416_u24lambda_u2415.append(entry2.getKey()).append(": ").append(entry2.getValue()).append("\r\n");
                    }
                    $this$write_u24lambda_u2416_u24lambda_u2415.append("\r\n");
                    byte[] head = $this$write_u24lambda_u2416_u24lambda_u2415.toString().getBytes(Charsets.US_ASCII);
                    Intrinsics.checkNotNullExpressionValue(head, "getBytes(...)");
                    try {
                        byte[] bytes = ArraysKt.plus(head, body);
                        OutputStream outputStreamInvoke = this.output.invoke();
                        ControlCipher controlCipher = this.cipher;
                        if (controlCipher == null || (bArrEncrypt = controlCipher.encrypt(bytes)) == null) {
                            bArrEncrypt = bytes;
                        }
                        outputStreamInvoke.write(bArrEncrypt);
                        this.output.invoke().flush();
                        Unit unit = Unit.INSTANCE;
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    private final long nextSequence() {
        long j;
        synchronized (this.sequenceLock) {
            this.sequence++;
            j = this.sequence;
        }
        return j;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.release.invoke();
    }
}
