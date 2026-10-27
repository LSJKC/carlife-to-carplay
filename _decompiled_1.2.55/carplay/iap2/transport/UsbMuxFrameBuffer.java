package com.carplay.iap2.transport;

import java.util.Collection;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.UByte;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.IntIterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: UsbMuxFrameBuffer.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u000b\b\u0000\u0018\u0000  2\u00020\u0001:\u0001 B\u001d\u0012\u0014\b\u0002\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003¢\u0006\u0004\b\u0006\u0010\u0007J\u000e\u0010\u0012\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\tJ\b\u0010\u0014\u001a\u0004\u0018\u00010\u000bJ\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u000bH\u0002J \u0010\u0018\u001a\u00020\u00162\u0006\u0010\u0019\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\rH\u0002J\u0010\u0010\u001c\u001a\u00020\u00052\u0006\u0010\u001d\u001a\u00020\u0004H\u0002J\u0018\u0010\u001e\u001a\u00020\r2\u0006\u0010\u0019\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\rH\u0002J\u0018\u0010\u001f\u001a\u00020\r2\u0006\u0010\u0019\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\rH\u0002R\u001a\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00050\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u000f\u001a\u00020\r8F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011¨\u0006!"}, d2 = {"Lcom/carplay/iap2/transport/UsbMuxFrameBuffer;", "", "diagnostic", "Lkotlin/Function1;", "", "", "<init>", "(Lkotlin/jvm/functions/Function1;)V", "bytes", "", "optionalReplyPadding", "Lcom/carplay/iap2/transport/UsbMuxFrame;", "paddingReports", "", "lastUsbReadBytes", "bufferedBytes", "getBufferedBytes", "()I", "append", "transfer", "takeFrame", "canHaveOptionalReplyPadding", "", "frame", "isCapturedDiagnosticPayload", "source", "offset", "length", "report", "line", "readU16", "readU32", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class UsbMuxFrameBuffer {

    @Deprecated
    public static final int DIAGNOSTIC_TEXT_SUBTYPE = 4;

    @Deprecated
    public static final int HEADER_BYTES = 16;

    @Deprecated
    public static final int MAX_DIAGNOSTIC_PAYLOAD_BYTES = 1024;

    @Deprecated
    public static final int MAX_FRAME_BYTES = 65536;

    @Deprecated
    public static final int MAX_PADDING_REPORTS = 4;

    @Deprecated
    public static final int PADDING_BYTES = 4;

    @Deprecated
    public static final int PROTOCOL_DIAGNOSTIC = 1;

    @Deprecated
    public static final int PROTOCOL_TCP = 6;

    @Deprecated
    public static final int PROTOCOL_VERSION = 0;

    @Deprecated
    public static final int TCP_HEADER_BYTES = 20;

    @Deprecated
    public static final int VERSION_BYTES = 20;
    private byte[] bytes;
    private final Function1<String, Unit> diagnostic;
    private int lastUsbReadBytes;
    private UsbMuxFrame optionalReplyPadding;
    private int paddingReports;
    private static final Companion Companion = new Companion(null);
    private static final int CAPTURED_REPLY_MAGIC = -87098674;

    /* JADX WARN: Multi-variable type inference failed */
    public UsbMuxFrameBuffer() {
        this(null, 1, 0 == true ? 1 : 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public UsbMuxFrameBuffer(Function1<? super String, Unit> diagnostic) {
        Intrinsics.checkNotNullParameter(diagnostic, "diagnostic");
        this.diagnostic = diagnostic;
        this.bytes = new byte[0];
    }

    public /* synthetic */ UsbMuxFrameBuffer(Function1 function1, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? new Function1() { // from class: com.carplay.iap2.transport.UsbMuxFrameBuffer$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return UsbMuxFrameBuffer._init_$lambda$0((String) obj);
            }
        } : function1);
    }

    static final Unit _init_$lambda$0(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    public final int getBufferedBytes() {
        return this.bytes.length;
    }

    public final void append(byte[] transfer) {
        Intrinsics.checkNotNullParameter(transfer, "transfer");
        this.lastUsbReadBytes = transfer.length;
        this.bytes = ArraysKt.plus(this.bytes, transfer);
    }

    /* JADX WARN: Code duplicated, block: B:53:0x00a4  */
    /* JADX WARN: Code duplicated, block: B:54:0x00a6 A[ADDED_TO_REGION] */
    /* JADX WARN: Code duplicated, block: B:66:0x00c6  */
    /* JADX WARN: Code duplicated, block: B:81:0x0152  */
    /* JADX WARN: Failed to apply debug info
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r18v0 ??, new type: com.carplay.iap2.transport.UsbMuxFrame
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.applyDebugInfo(TypeUpdate.java:77)
    	at jadx.core.dex.visitors.debuginfo.DebugInfoApplyVisitor.applyDebugInfo(DebugInfoApplyVisitor.java:137)
    	at jadx.core.dex.visitors.debuginfo.DebugInfoApplyVisitor.applyDebugInfo(DebugInfoApplyVisitor.java:133)
    	at jadx.core.dex.visitors.debuginfo.DebugInfoApplyVisitor.searchAndApplyVarDebugInfo(DebugInfoApplyVisitor.java:79)
    	at jadx.core.dex.visitors.debuginfo.DebugInfoApplyVisitor.lambda$applyDebugInfo$0(DebugInfoApplyVisitor.java:68)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.debuginfo.DebugInfoApplyVisitor.applyDebugInfo(DebugInfoApplyVisitor.java:68)
    	at jadx.core.dex.visitors.debuginfo.DebugInfoApplyVisitor.visit(DebugInfoApplyVisitor.java:55)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r17v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r17v0 ??, new type: com.carplay.iap2.transport.UsbMuxFrame
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r17v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r17v0 ??, new type: com.carplay.iap2.transport.UsbMuxFrame
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r17v1 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r17v1 ??, new type: com.carplay.iap2.transport.UsbMuxFrame
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r17v1 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r17v1 ??, new type: com.carplay.iap2.transport.UsbMuxFrame
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r18v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r18v0 ??, new type: com.carplay.iap2.transport.UsbMuxFrame
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r18v0 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r18v0 ??, new type: com.carplay.iap2.transport.UsbMuxFrame
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r24v0 'this'  ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r24v0 'this'  ??, new type: com.carplay.iap2.transport.UsbMuxFrameBuffer
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r2v10 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v10 ??, new type: com.carplay.iap2.transport.UsbMuxFrame
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r2v10 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v10 ??, new type: com.carplay.iap2.transport.UsbMuxFrame
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r2v11 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v11 ??, new type: com.carplay.iap2.transport.UsbMuxFrame
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r2v11 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v11 ??, new type: com.carplay.iap2.transport.UsbMuxFrame
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r2v12 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v12 ??, new type: com.carplay.iap2.transport.UsbMuxFrame
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:159)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:136)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:241)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:224)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r2v12 ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r2v12 ??, new type: com.carplay.iap2.transport.UsbMuxFrame
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:59)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:147)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:125)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$2(TypeInferenceVisitor.java:103)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:103)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Failed to set immutable type for var: r24v0 'this'  ??
    jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r24v0 'this'  ??, new type: com.carplay.iap2.transport.UsbMuxFrameBuffer
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.applyWithWiderIgnSame(TypeUpdate.java:73)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setImmutableType(TypeInferenceVisitor.java:111)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$1(TypeInferenceVisitor.java:102)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:102)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
    Caused by: java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: Type update failed for variable: r24v0 'this'  com.carplay.iap2.transport.UsbMuxFrameBuffer, new type: com.carplay.iap2.transport.UsbMuxFrameBuffer
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:109)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.applyWithWiderIgnSame(TypeUpdate.java:73)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.applyResolvedVars(TypeSearch.java:100)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:76)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:119)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
    Caused by: java.lang.NullPointerException
     */
    public final UsbMuxFrame takeFrame() throws IphoneUsbException.Protocol {
        UsbMuxFrame usbMuxFrame;
        char c;
        boolean z;
        boolean zIsCapturedDiagnosticPayload;
        if (this.bytes.length < 16) {
            return null;
        }
        int length = readU32(this.bytes, 4);
        if (16 <= length && length < 65537) {
            usbMuxFrame = 0;
            c = '\f';
        } else {
            UsbMuxFrame usbMuxFrame2 = this.optionalReplyPadding;
            if (usbMuxFrame2 != null && readU32(this.bytes, 0) != 6 && this.bytes.length < 20) {
                return null;
            }
            int u32 = readU32(this.bytes, 4);
            int u33 = readU32(this.bytes, 8);
            if (usbMuxFrame2 != null && readU32(this.bytes, 0) != 6) {
                c = '\f';
                if (readU32(this.bytes, 12) == CAPTURED_REPLY_MAGIC) {
                    z = true;
                }
                if (!z && u32 == 6) {
                    if (36 <= u33 && u33 < 65537) {
                        if (this.bytes.length < 40) {
                            return null;
                        }
                        int i = ((this.bytes[32] >>> 4) & 15) * 4;
                        zIsCapturedDiagnosticPayload = (!(20 <= i && i <= u33 + (-16)) || readU16(this.bytes, 20) == 0 || readU16(this.bytes, 22) == 0) ? false : true;
                    } else if (!z) {
                        zIsCapturedDiagnosticPayload = false;
                    } else {
                        zIsCapturedDiagnosticPayload = false;
                    }
                } else if (!z && u32 == 1) {
                    if (18 <= u33 && u33 < 1041) {
                        if (this.bytes.length < u33 + 4) {
                            return null;
                        }
                        zIsCapturedDiagnosticPayload = isCapturedDiagnosticPayload(this.bytes, 20, u33 - 16);
                    } else {
                        zIsCapturedDiagnosticPayload = false;
                    }
                } else {
                    zIsCapturedDiagnosticPayload = false;
                }
                if (usbMuxFrame2 == null && zIsCapturedDiagnosticPayload) {
                    this.bytes = ArraysKt.copyOfRange(this.bytes, 4, this.bytes.length);
                    this.optionalReplyPadding = null;
                    int i2 = this.paddingReports;
                    this.paddingReports = i2 + 1;
                    if (i2 < 4) {
                        report("USBMUX optional reply padding skipped bytes=4 previousProtocol=" + usbMuxFrame2.getProtocol() + " previousLength=" + usbMuxFrame2.getLength() + " nextProtocol=" + u32 + " nextLength=" + u33 + " lastUsbReadBytes=" + this.lastUsbReadBytes + " bufferedBytes=" + this.bytes.length);
                    }
                    if (this.bytes.length < 16) {
                        return 0;
                    }
                    length = readU32(this.bytes, 4);
                } else {
                    report("USBMUX framing rejected declaredLength=" + length + " bufferedBytes=" + this.bytes.length + " lastUsbReadBytes=" + this.lastUsbReadBytes + " optionalReplyPadding=" + (usbMuxFrame2 != null));
                    throw new IphoneUsbException.Protocol("Invalid USBMUX frame length " + length);
                }
            } else {
                c = '\f';
            }
            z = false;
            if (!z) {
                if (!z) {
                    zIsCapturedDiagnosticPayload = false;
                } else {
                    zIsCapturedDiagnosticPayload = false;
                }
            } else if (!z) {
                zIsCapturedDiagnosticPayload = false;
            } else {
                zIsCapturedDiagnosticPayload = false;
            }
            if (usbMuxFrame2 == null) {
            }
            report("USBMUX framing rejected declaredLength=" + length + " bufferedBytes=" + this.bytes.length + " lastUsbReadBytes=" + this.lastUsbReadBytes + " optionalReplyPadding=" + (usbMuxFrame2 != null));
            throw new IphoneUsbException.Protocol("Invalid USBMUX frame length " + length);
        }
        if (this.bytes.length < length) {
            return usbMuxFrame;
        }
        UsbMuxFrame usbMuxFrame3 = new UsbMuxFrame(readU32(this.bytes, 0), length, readU32(this.bytes, 8), ((this.bytes[c] & UByte.MAX_VALUE) << 8) | (this.bytes[13] & UByte.MAX_VALUE), ArraysKt.copyOfRange(this.bytes, 16, length));
        this.bytes = ArraysKt.copyOfRange(this.bytes, length, this.bytes.length);
        this.optionalReplyPadding = canHaveOptionalReplyPadding(usbMuxFrame3) ? usbMuxFrame3 : usbMuxFrame;
        return usbMuxFrame3;
    }

    private final boolean canHaveOptionalReplyPadding(UsbMuxFrame frame) {
        if (frame.getProtocol() == 0 && frame.getLength() == 20 && frame.getWord8() == 2) {
            return true;
        }
        if (frame.getProtocol() == 1 && frame.getWord8() == CAPTURED_REPLY_MAGIC) {
            return isCapturedDiagnosticPayload(frame.getPayload(), 0, frame.getPayload().length);
        }
        if (frame.getProtocol() != 6 || frame.getWord8() != CAPTURED_REPLY_MAGIC || frame.getPayload().length < 20) {
            return false;
        }
        int tcpHeaderBytes = ((frame.getPayload()[12] >>> 4) & 15) * 4;
        return (!(20 <= tcpHeaderBytes && tcpHeaderBytes <= frame.getPayload().length) || readU16(frame.getPayload(), 0) == 0 || readU16(frame.getPayload(), 2) == 0) ? false : true;
    }

    private final boolean isCapturedDiagnosticPayload(byte[] source, int offset, int length) {
        boolean z;
        if ((2 <= length && length < 1025) && source[offset] == 4) {
            Iterable $this$all$iv = RangesKt.until(offset + 1, offset + length);
            if (!($this$all$iv instanceof Collection) || !((Collection) $this$all$iv).isEmpty()) {
                Iterator it = $this$all$iv.iterator();
                while (true) {
                    if (it.hasNext()) {
                        int element$iv = ((IntIterator) it).nextInt();
                        byte b = source[element$iv];
                        int it2 = (32 > b || b >= 127) ? 0 : 1;
                        if (it2 == 0) {
                            z = false;
                            break;
                        }
                    } else {
                        z = true;
                        break;
                    }
                }
            } else {
                z = true;
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    private final void report(String line) {
        try {
            Result.Companion companion = Result.INSTANCE;
            UsbMuxFrameBuffer $this$report_u24lambda_u242 = this;
            $this$report_u24lambda_u242.diagnostic.invoke(line);
            Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
    }

    private final int readU16(byte[] source, int offset) {
        return ((source[offset] & UByte.MAX_VALUE) << 8) | (source[offset + 1] & UByte.MAX_VALUE);
    }

    private final int readU32(byte[] source, int offset) {
        return ((source[offset] & UByte.MAX_VALUE) << 24) | ((source[offset + 1] & UByte.MAX_VALUE) << 16) | ((source[offset + 2] & UByte.MAX_VALUE) << 8) | (source[offset + 3] & UByte.MAX_VALUE);
    }

    /* JADX INFO: compiled from: UsbMuxFrameBuffer.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u000e\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\u00020\u0005X\u0086D¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012¨\u0006\u0013"}, d2 = {"Lcom/carplay/iap2/transport/UsbMuxFrameBuffer$Companion;", "", "<init>", "()V", "HEADER_BYTES", "", "TCP_HEADER_BYTES", "MAX_FRAME_BYTES", "VERSION_BYTES", "PADDING_BYTES", "MAX_PADDING_REPORTS", "PROTOCOL_VERSION", "PROTOCOL_DIAGNOSTIC", "PROTOCOL_TCP", "DIAGNOSTIC_TEXT_SUBTYPE", "MAX_DIAGNOSTIC_PAYLOAD_BYTES", "CAPTURED_REPLY_MAGIC", "getCAPTURED_REPLY_MAGIC", "()I", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final int getCAPTURED_REPLY_MAGIC() {
            return UsbMuxFrameBuffer.CAPTURED_REPLY_MAGIC;
        }
    }
}
