package com.carplay.iap2.body;

import com.carplay.iap2.catalog.Iap2Endpoint;
import com.carplay.iap2.catalog.Iap2FieldSpec;
import com.carplay.iap2.catalog.Iap2WireType;
import com.carplay.iap2.wire.Iap2Frame;
import com.carplay.iap2.wire.Iap2Parameter;
import com.carplay.iap2.wire.Iap2ParameterList;
import com.carplay.iap2.wire.Iap2ProtocolException;
import com.carplay.iap2.wire.Iap2WireCodec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Iap2BodyBuilder.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u0000 H2\u00020\u0001:\u0002GHB3\b\u0002\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0004\b\u000b\u0010\fJ\u000e\u0010\u0012\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u0005J\u0016\u0010\u0014\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u0005J\u0016\u0010\u0016\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\nJ\u0016\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u0005J\u0016\u0010\u0018\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u0005J\u0016\u0010\u0019\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u0005J\u0016\u0010\u001a\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u0005J\u0016\u0010\u001a\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u001bJ\u0016\u0010\u001c\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u0005J\u0016\u0010\u001d\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u001bJ\u0016\u0010\u001e\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u001fJ\u0016\u0010 \u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020!J\u001c\u0010\"\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\f\u0010#\u001a\b\u0012\u0004\u0012\u00020!0$J\u001c\u0010%\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00050$J\u0016\u0010&\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010'\u001a\u00020\u001fJ'\u0010(\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\u0017\u0010)\u001a\u0013\u0012\u0004\u0012\u00020\u0000\u0012\u0004\u0012\u00020+0*¢\u0006\u0002\b,J\u0016\u0010-\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010.\u001a\u00020\nJ\u001d\u0010/\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\b\u0010\u0015\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u00100J\u001d\u00101\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\b\u0010\u0015\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u00100J\u001d\u00102\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\b\u0010\u0015\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u00100J\u001d\u00103\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\b\u0010\u0015\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u00100J\u001d\u00104\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\b\u0010\u0015\u001a\u0004\u0018\u00010\u001b¢\u0006\u0002\u00105J\u001d\u00106\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\b\u0010\u0015\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u00100J\u001d\u00107\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\b\u0010\u0015\u001a\u0004\u0018\u00010\u001b¢\u0006\u0002\u00105J\u0018\u00108\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\b\u0010\u0015\u001a\u0004\u0018\u00010\u001fJ\u0018\u00109\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\b\u0010\u0015\u001a\u0004\u0018\u00010!J\u0006\u0010:\u001a\u00020;J\u000e\u0010:\u001a\u00020;2\u0006\u0010<\u001a\u00020\u0005J\b\u0010=\u001a\u00020\u001fH\u0002J \u0010>\u001a\u00020\u00002\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010'\u001a\u00020\u001f2\u0006\u0010?\u001a\u00020@H\u0002J\u0012\u0010A\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0013\u001a\u00020\u0005H\u0002J\u0016\u0010B\u001a\u00020+2\f\u0010C\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0002J\u0018\u0010D\u001a\u00020\n2\u0006\u0010E\u001a\u00020@2\u0006\u0010F\u001a\u00020@H\u0002R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\rR\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u000e\u001a\u0012\u0012\u0004\u0012\u00020\u00100\u000fj\b\u0012\u0004\u0012\u00020\u0010`\u0011X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006I"}, d2 = {"Lcom/carplay/iap2/body/Iap2BodyBuilder;", "", "endpoint", "Lcom/carplay/iap2/catalog/Iap2Endpoint;", "explicitMessageId", "", "fields", "", "Lcom/carplay/iap2/catalog/Iap2FieldSpec;", "validateSchema", "", "<init>", "(Lcom/carplay/iap2/catalog/Iap2Endpoint;Ljava/lang/Integer;Ljava/util/List;Z)V", "Ljava/lang/Integer;", "entries", "Ljava/util/ArrayList;", "Lcom/carplay/iap2/body/Iap2BodyBuilder$Entry;", "Lkotlin/collections/ArrayList;", "void", "id", "u8", "value", "bool", "i8", "u16", "i16", "u32", "", "i32", "u64", "bytes", "", "string", "", "strings", "values", "", "u16List", "raw", "payload", "group", "block", "Lkotlin/Function1;", "", "Lkotlin/ExtensionFunctionType;", "optionalVoid", "present", "optionalU8", "(ILjava/lang/Integer;)Lcom/carplay/iap2/body/Iap2BodyBuilder;", "optionalI8", "optionalU16", "optionalI16", "optionalU32", "(ILjava/lang/Long;)Lcom/carplay/iap2/body/Iap2BodyBuilder;", "optionalI32", "optionalU64", "optionalBytes", "optionalString", "build", "Lcom/carplay/iap2/wire/Iap2Frame;", "messageId", "encodeValidated", "add", "wireType", "Lcom/carplay/iap2/catalog/Iap2WireType;", "schemaField", "validate", "specs", "wireTypeMatches", "expected", "actual", "Entry", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2BodyBuilder {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final Iap2Endpoint endpoint;
    private final ArrayList<Entry> entries;
    private final Integer explicitMessageId;
    private final List<Iap2FieldSpec> fields;
    private final boolean validateSchema;

    public /* synthetic */ Iap2BodyBuilder(Iap2Endpoint iap2Endpoint, Integer num, List list, boolean z, DefaultConstructorMarker defaultConstructorMarker) {
        this(iap2Endpoint, num, list, z);
    }

    private Iap2BodyBuilder(Iap2Endpoint endpoint, Integer explicitMessageId, List<Iap2FieldSpec> list, boolean validateSchema) {
        this.endpoint = endpoint;
        this.explicitMessageId = explicitMessageId;
        this.fields = list;
        this.validateSchema = validateSchema;
        this.entries = new ArrayList<>();
    }

    /* JADX INFO: compiled from: Iap2BodyBuilder.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001J\t\u0010\u0014\u001a\u00020\u0015HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0016"}, d2 = {"Lcom/carplay/iap2/body/Iap2BodyBuilder$Entry;", "", "parameter", "Lcom/carplay/iap2/wire/Iap2Parameter;", "wireType", "Lcom/carplay/iap2/catalog/Iap2WireType;", "<init>", "(Lcom/carplay/iap2/wire/Iap2Parameter;Lcom/carplay/iap2/catalog/Iap2WireType;)V", "getParameter", "()Lcom/carplay/iap2/wire/Iap2Parameter;", "getWireType", "()Lcom/carplay/iap2/catalog/Iap2WireType;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class Entry {
        private final Iap2Parameter parameter;
        private final Iap2WireType wireType;

        public static /* synthetic */ Entry copy$default(Entry entry, Iap2Parameter iap2Parameter, Iap2WireType iap2WireType, int i, Object obj) {
            if ((i & 1) != 0) {
                iap2Parameter = entry.parameter;
            }
            if ((i & 2) != 0) {
                iap2WireType = entry.wireType;
            }
            return entry.copy(iap2Parameter, iap2WireType);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final Iap2Parameter getParameter() {
            return this.parameter;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final Iap2WireType getWireType() {
            return this.wireType;
        }

        public final Entry copy(Iap2Parameter parameter, Iap2WireType wireType) {
            Intrinsics.checkNotNullParameter(parameter, "parameter");
            Intrinsics.checkNotNullParameter(wireType, "wireType");
            return new Entry(parameter, wireType);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) other;
            return Intrinsics.areEqual(this.parameter, entry.parameter) && this.wireType == entry.wireType;
        }

        public int hashCode() {
            return (this.parameter.hashCode() * 31) + this.wireType.hashCode();
        }

        public String toString() {
            return "Entry(parameter=" + this.parameter + ", wireType=" + this.wireType + ")";
        }

        public Entry(Iap2Parameter parameter, Iap2WireType wireType) {
            Intrinsics.checkNotNullParameter(parameter, "parameter");
            Intrinsics.checkNotNullParameter(wireType, "wireType");
            this.parameter = parameter;
            this.wireType = wireType;
        }

        public final Iap2Parameter getParameter() {
            return this.parameter;
        }

        public final Iap2WireType getWireType() {
            return this.wireType;
        }
    }

    /* JADX INFO: renamed from: void, reason: not valid java name */
    public final Iap2BodyBuilder m51void(int id) {
        return add(id, Iap2WireCodec.INSTANCE.m55void(), Iap2WireType.VOID);
    }

    public final Iap2BodyBuilder u8(int id, int value) {
        return add(id, Iap2WireCodec.INSTANCE.u8(value), Iap2WireType.U8);
    }

    public final Iap2BodyBuilder bool(int id, boolean value) {
        return add(id, Iap2WireCodec.INSTANCE.bool(value), Iap2WireType.U8);
    }

    public final Iap2BodyBuilder i8(int id, int value) {
        return add(id, Iap2WireCodec.INSTANCE.i8(value), Iap2WireType.I8);
    }

    public final Iap2BodyBuilder u16(int id, int value) {
        return add(id, Iap2WireCodec.INSTANCE.u16(value), Iap2WireType.U16);
    }

    public final Iap2BodyBuilder i16(int id, int value) {
        return add(id, Iap2WireCodec.INSTANCE.i16(value), Iap2WireType.I16);
    }

    public final Iap2BodyBuilder u32(int id, int value) {
        return u32(id, value);
    }

    public final Iap2BodyBuilder u32(int id, long value) {
        return add(id, Iap2WireCodec.INSTANCE.u32(value), Iap2WireType.U32);
    }

    public final Iap2BodyBuilder i32(int id, int value) {
        return add(id, Iap2WireCodec.INSTANCE.i32(value), Iap2WireType.I32);
    }

    public final Iap2BodyBuilder u64(int id, long value) {
        return add(id, Iap2WireCodec.INSTANCE.u64(value), Iap2WireType.U64);
    }

    public final Iap2BodyBuilder bytes(int id, byte[] value) {
        Intrinsics.checkNotNullParameter(value, "value");
        return add(id, Iap2WireCodec.INSTANCE.bytes(value), Iap2WireType.BYTES);
    }

    public final Iap2BodyBuilder string(int id, String value) {
        Intrinsics.checkNotNullParameter(value, "value");
        return add(id, Iap2WireCodec.INSTANCE.string(value), Iap2WireType.STRING);
    }

    public final Iap2BodyBuilder strings(int id, Iterable<String> values) {
        Intrinsics.checkNotNullParameter(values, "values");
        return add(id, Iap2WireCodec.INSTANCE.strings(values), Iap2WireType.STRING_LIST);
    }

    public final Iap2BodyBuilder u16List(int id, Iterable<Integer> values) {
        Intrinsics.checkNotNullParameter(values, "values");
        return add(id, Iap2WireCodec.INSTANCE.u16List(values), Iap2WireType.U16_LIST);
    }

    public final Iap2BodyBuilder raw(int id, byte[] payload) {
        Intrinsics.checkNotNullParameter(payload, "payload");
        byte[] bArrCopyOf = Arrays.copyOf(payload, payload.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        return add(id, bArrCopyOf, Iap2WireType.OPAQUE);
    }

    public final Iap2BodyBuilder group(int id, Function1<? super Iap2BodyBuilder, Unit> block) throws Iap2ProtocolException {
        Intrinsics.checkNotNullParameter(block, "block");
        Iap2FieldSpec iap2FieldSpecSchemaField = schemaField(id);
        List<Iap2FieldSpec> children = iap2FieldSpecSchemaField != null ? iap2FieldSpecSchemaField.getChildren() : null;
        if (children == null) {
            children = CollectionsKt.emptyList();
        }
        Iap2BodyBuilder child = new Iap2BodyBuilder(null, null, children, this.validateSchema);
        block.invoke(child);
        byte[] encoded = child.encodeValidated();
        add(id, encoded, Iap2WireType.GROUP);
        return this;
    }

    public final Iap2BodyBuilder optionalVoid(int id, boolean present) {
        return present ? m51void(id) : this;
    }

    public final Iap2BodyBuilder optionalU8(int id, Integer value) {
        return value == null ? this : u8(id, value.intValue());
    }

    public final Iap2BodyBuilder optionalI8(int id, Integer value) {
        return value == null ? this : i8(id, value.intValue());
    }

    public final Iap2BodyBuilder optionalU16(int id, Integer value) {
        return value == null ? this : u16(id, value.intValue());
    }

    public final Iap2BodyBuilder optionalI16(int id, Integer value) {
        return value == null ? this : i16(id, value.intValue());
    }

    public final Iap2BodyBuilder optionalU32(int id, Long value) {
        return value == null ? this : u32(id, value.longValue());
    }

    public final Iap2BodyBuilder optionalI32(int id, Integer value) {
        return value == null ? this : i32(id, value.intValue());
    }

    public final Iap2BodyBuilder optionalU64(int id, Long value) {
        return value == null ? this : u64(id, value.longValue());
    }

    public final Iap2BodyBuilder optionalBytes(int id, byte[] value) {
        return value == null ? this : bytes(id, value);
    }

    public final Iap2BodyBuilder optionalString(int id, String value) {
        return value == null ? this : string(id, value);
    }

    public final Iap2Frame build() {
        int target;
        Iap2Endpoint iap2Endpoint = this.endpoint;
        if (iap2Endpoint != null) {
            target = iap2Endpoint.getId();
        } else {
            Integer num = this.explicitMessageId;
            if (num == null) {
                throw new IllegalStateException("A raw body builder needs an explicit message id");
            }
            target = num.intValue();
        }
        return build(target);
    }

    public final Iap2Frame build(int messageId) {
        return new Iap2Frame(messageId, encodeValidated());
    }

    private final byte[] encodeValidated() throws Iap2ProtocolException {
        if (this.validateSchema && !this.fields.isEmpty()) {
            validate(this.fields);
        }
        Iap2ParameterList.Companion companion = Iap2ParameterList.INSTANCE;
        Iterable $this$map$iv = this.entries;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (Object item$iv$iv : $this$map$iv) {
            Entry it = (Entry) item$iv$iv;
            destination$iv$iv.add(it.getParameter());
        }
        return companion.of((List) destination$iv$iv).encode();
    }

    private final Iap2BodyBuilder add(int id, byte[] payload, Iap2WireType wireType) {
        boolean z = false;
        if (id >= 0 && id < 65536) {
            z = true;
        }
        if (!z) {
            throw new IllegalArgumentException("CSM parameter id must fit in u16".toString());
        }
        this.entries.add(new Entry(new Iap2Parameter(id, payload), wireType));
        return this;
    }

    private final Iap2FieldSpec schemaField(int id) {
        Object element$iv;
        Iterable $this$firstOrNull$iv = this.fields;
        Iterator it = $this$firstOrNull$iv.iterator();
        while (it.hasNext()) {
            element$iv = it.next();
            Iap2FieldSpec it2 = (Iap2FieldSpec) element$iv;
            if (it2.getId() == id) {
                return (Iap2FieldSpec) element$iv;
            }
        }
        element$iv = null;
        return (Iap2FieldSpec) element$iv;
    }

    private final void validate(List<Iap2FieldSpec> specs) throws Iap2ProtocolException {
        int count$iv;
        int i;
        List<Iap2FieldSpec> $this$associateBy$iv = specs;
        int i2 = 16;
        int capacity$iv = RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault($this$associateBy$iv, 10)), 16);
        Map byId = new LinkedHashMap(capacity$iv);
        for (Object element$iv$iv : $this$associateBy$iv) {
            Iap2FieldSpec p0 = (Iap2FieldSpec) element$iv$iv;
            byId.put(Integer.valueOf(p0.getId()), element$iv$iv);
        }
        Iterator<Entry> it = this.entries.iterator();
        Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
        while (true) {
            boolean z = true;
            if (!it.hasNext()) {
                int i3 = i2;
                List<Iap2FieldSpec> $this$filter$iv = specs;
                Collection destination$iv$iv = new ArrayList();
                for (Object element$iv$iv2 : $this$filter$iv) {
                    Iap2FieldSpec it2 = (Iap2FieldSpec) element$iv$iv2;
                    if (it2.getRequired()) {
                        destination$iv$iv.add(element$iv$iv2);
                    }
                }
                for (Iap2FieldSpec spec : (List) destination$iv$iv) {
                    Iterable $this$none$iv = this.entries;
                    if (!($this$none$iv instanceof Collection) || !((Collection) $this$none$iv).isEmpty()) {
                        Iterator it3 = $this$none$iv.iterator();
                        while (true) {
                            if (!it3.hasNext()) {
                                z = true;
                                break;
                            }
                            Object element$iv = it3.next();
                            Entry it4 = (Entry) element$iv;
                            if (it4.getParameter().getId() == spec.getId()) {
                                z = false;
                                break;
                            }
                        }
                    }
                    if (z) {
                        String string = Integer.toString(spec.getId(), CharsKt.checkRadix(i3));
                        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                        throw new Iap2ProtocolException("Missing required body parameter 0x" + StringsKt.padStart(string, 4, '0') + " (" + spec.getName() + ")", null, 2, null);
                    }
                    z = true;
                }
                return;
            }
            Entry next = it.next();
            Intrinsics.checkNotNullExpressionValue(next, "next(...)");
            Entry entry = next;
            Iap2FieldSpec spec2 = (Iap2FieldSpec) byId.get(Integer.valueOf(entry.getParameter().getId()));
            if (spec2 == null) {
                String string2 = Integer.toString(entry.getParameter().getId(), CharsKt.checkRadix(i2));
                Intrinsics.checkNotNullExpressionValue(string2, "toString(...)");
                throw new Iap2ProtocolException("Unknown body parameter 0x" + StringsKt.padStart(string2, 4, '0'), null, 2, null);
            }
            if (!wireTypeMatches(spec2.getType(), entry.getWireType())) {
                String string3 = Integer.toString(spec2.getId(), CharsKt.checkRadix(i2));
                Intrinsics.checkNotNullExpressionValue(string3, "toString(...)");
                throw new Iap2ProtocolException("Body parameter 0x" + StringsKt.padStart(string3, 4, '0') + " expected " + spec2.getType() + ", got " + entry.getWireType(), null, 2, null);
            }
            if (!spec2.getRepeatable()) {
                Iterable $this$count$iv = this.entries;
                if (($this$count$iv instanceof Collection) && ((Collection) $this$count$iv).isEmpty()) {
                    i = i2;
                    count$iv = 0;
                } else {
                    count$iv = 0;
                    for (Object element$iv2 : $this$count$iv) {
                        Entry it5 = (Entry) element$iv2;
                        i2 = i2;
                        if (it5.getParameter().getId() == spec2.getId()) {
                            count$iv++;
                            if (count$iv < 0) {
                                CollectionsKt.throwCountOverflow();
                            }
                        }
                    }
                    i = i2;
                }
                if (count$iv > 1) {
                    String string4 = Integer.toString(spec2.getId(), CharsKt.checkRadix(i));
                    Intrinsics.checkNotNullExpressionValue(string4, "toString(...)");
                    throw new Iap2ProtocolException("Body parameter 0x" + StringsKt.padStart(string4, 4, '0') + " is not repeatable", null, 2, null);
                }
                i2 = i;
            }
        }
    }

    private final boolean wireTypeMatches(Iap2WireType expected, Iap2WireType actual) {
        return expected == Iap2WireType.OPAQUE || expected == actual;
    }

    /* JADX INFO: compiled from: Iap2BodyBuilder.kt */
    @Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007J\u000e\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\n¨\u0006\u000b"}, d2 = {"Lcom/carplay/iap2/body/Iap2BodyBuilder$Companion;", "", "<init>", "()V", "of", "Lcom/carplay/iap2/body/Iap2BodyBuilder;", "endpoint", "Lcom/carplay/iap2/catalog/Iap2Endpoint;", "opaque", "messageId", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Iap2BodyBuilder of(Iap2Endpoint endpoint) {
            Intrinsics.checkNotNullParameter(endpoint, "endpoint");
            return new Iap2BodyBuilder(endpoint, null, endpoint.getFields(), true, null);
        }

        public final Iap2BodyBuilder opaque(int messageId) {
            return new Iap2BodyBuilder(null, Integer.valueOf(messageId), CollectionsKt.emptyList(), false, null);
        }
    }
}
