package com.carplay.iap2.trace;

import com.carplay.iap2.catalog.Iap2Endpoint;
import com.carplay.iap2.catalog.Iap2Endpoints;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Iap2FrameFormatter.kt */
/* JADX INFO: loaded from: classes6.dex */
@Metadata(d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0006\n\u0002\u0010\u0004\n\u0002\b\u0006\n\u0002\u0010\u0005\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u001e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u000eJ-\u0010\u000f\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\t2\b\u0010\u0010\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0011\u001a\u00020\u0012¢\u0006\u0002\u0010\u0013J4\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00192\u0006\u0010\u001b\u001a\u00020\u00052\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\t0\u001dH\u0002J \u0010\u001e\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u0005H\u0002J\u0010\u0010\"\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020 H\u0002J(\u0010#\u001a\u00020\t2\u0006\u0010$\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010%\u001a\u00020\u00052\u0006\u0010&\u001a\u00020'H\u0002J\u0010\u0010(\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020 H\u0002J\u0012\u0010)\u001a\u0004\u0018\u00010\t2\u0006\u0010\u001f\u001a\u00020 H\u0002J\u0010\u0010*\u001a\u00020\t2\u0006\u0010&\u001a\u00020\tH\u0002J\u0010\u0010+\u001a\u00020\t2\u0006\u0010&\u001a\u00020\tH\u0002J\u0010\u0010,\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020 H\u0002J\u0010\u0010-\u001a\u00020\u00052\u0006\u0010&\u001a\u00020.H\u0002J\u0010\u0010/\u001a\u00020\t2\u0006\u0010\u001b\u001a\u00020\u0005H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u00060"}, d2 = {"Lcom/carplay/iap2/trace/Iap2FrameFormatter;", "", "<init>", "()V", "MAX_PARAMETERS", "", "MAX_STRING_CHARS", "MAX_HEX_BYTES", "format", "", "direction", "Lcom/carplay/iap2/trace/Iap2TraceDirection;", "context", "frame", "Lcom/carplay/iap2/wire/Iap2Frame;", "formatFailure", "messageId", "failure", "", "(Lcom/carplay/iap2/trace/Iap2TraceDirection;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Throwable;)Ljava/lang/String;", "appendParameters", "", "parameters", "Lcom/carplay/iap2/wire/Iap2ParameterList;", "specs", "", "Lcom/carplay/iap2/catalog/Iap2FieldSpec;", "depth", "lines", "", "formatKnown", "payload", "", "spec", "formatOpaque", "scalar", "name", "size", "value", "", "bytes", "readableString", "quote", "truncate", "hexPreview", "u8", "", "indent", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2FrameFormatter {
    public static final Iap2FrameFormatter INSTANCE = new Iap2FrameFormatter();
    private static final int MAX_HEX_BYTES = 24;
    private static final int MAX_PARAMETERS = 80;
    private static final int MAX_STRING_CHARS = 240;

    /* JADX INFO: compiled from: Iap2FrameFormatter.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[Iap2WireType.values().length];
            try {
                iArr[Iap2WireType.VOID.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Iap2WireType.U8.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Iap2WireType.I8.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[Iap2WireType.U16.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[Iap2WireType.I16.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[Iap2WireType.U32.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[Iap2WireType.I32.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[Iap2WireType.U64.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[Iap2WireType.U16_LIST.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[Iap2WireType.BYTES.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[Iap2WireType.STRING.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[Iap2WireType.STRING_LIST.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr[Iap2WireType.GROUP.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                iArr[Iap2WireType.OPAQUE.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    private Iap2FrameFormatter() {
    }

    public final String format(Iap2TraceDirection direction, String context, Iap2Frame frame) {
        Iap2ParameterList parameters;
        Intrinsics.checkNotNullParameter(direction, "direction");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(frame, "frame");
        Iap2Endpoint endpoint = Iap2Endpoints.INSTANCE.byId(frame.getMessageId());
        byte[] encoded = frame.encodedFrame();
        try {
            parameters = Iap2ParameterList.INSTANCE.parse(frame.getPayload());
        } catch (Iap2ProtocolException e) {
            parameters = null;
        }
        Iterable lines = new ArrayList();
        if (parameters == null) {
            ((Collection) lines).add("  body=malformed TLV");
        } else {
            List<Iap2FieldSpec> fields = endpoint != null ? endpoint.getFields() : null;
            if (fields == null) {
                fields = CollectionsKt.emptyList();
            }
            appendParameters(parameters, fields, 1, (List) lines);
        }
        ((Collection) lines).add("  raw-body=" + hexPreview(frame.getPayload()));
        String endpointText = endpoint != null ? " " + endpoint.getName() : null;
        if (endpointText == null) {
            endpointText = "";
        }
        StringBuilder $this$format_u24lambda_u242 = new StringBuilder();
        $this$format_u24lambda_u242.append("IAP2 ");
        $this$format_u24lambda_u242.append(direction.getLabel());
        $this$format_u24lambda_u242.append(" [");
        $this$format_u24lambda_u242.append(context);
        $this$format_u24lambda_u242.append("] 0x");
        String string = Integer.toString(frame.getMessageId(), CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        $this$format_u24lambda_u242.append(StringsKt.padStart(string, 4, '0'));
        $this$format_u24lambda_u242.append(endpointText);
        $this$format_u24lambda_u242.append(" frame=");
        $this$format_u24lambda_u242.append(encoded.length);
        $this$format_u24lambda_u242.append("B body=");
        $this$format_u24lambda_u242.append(parameters == null ? "malformed" : parameters.getSize() + " params");
        Iterable $this$forEach$iv = lines;
        for (Object element$iv : $this$forEach$iv) {
            Iap2ParameterList parameters2 = parameters;
            String line = (String) element$iv;
            $this$format_u24lambda_u242.append('\n');
            $this$format_u24lambda_u242.append(line);
            parameters = parameters2;
        }
        return $this$format_u24lambda_u242.toString();
    }

    public final String formatFailure(Iap2TraceDirection direction, String context, Integer messageId, Throwable failure) {
        String str;
        Intrinsics.checkNotNullParameter(direction, "direction");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(failure, "failure");
        StringBuilder $this$formatFailure_u24lambda_u245 = new StringBuilder();
        $this$formatFailure_u24lambda_u245.append("IAP2 ");
        $this$formatFailure_u24lambda_u245.append(direction.getLabel());
        $this$formatFailure_u24lambda_u245.append(" [");
        $this$formatFailure_u24lambda_u245.append(context);
        $this$formatFailure_u24lambda_u245.append(']');
        if (messageId != null) {
            int it = messageId.intValue();
            $this$formatFailure_u24lambda_u245.append(" 0x");
            String string = Integer.toString(it, CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            $this$formatFailure_u24lambda_u245.append(StringsKt.padStart(string, 4, '0'));
            Iap2Endpoint endpoint = Iap2Endpoints.INSTANCE.byId(it);
            if (endpoint != null) {
                str = " " + endpoint.getName();
            } else {
                str = null;
            }
            if (str == null) {
                str = "";
            }
            $this$formatFailure_u24lambda_u245.append(str);
        }
        $this$formatFailure_u24lambda_u245.append(" FAILED: ");
        String message = failure.getMessage();
        if (message == null) {
            message = failure.getClass().getSimpleName();
        }
        $this$formatFailure_u24lambda_u245.append(message);
        return $this$formatFailure_u24lambda_u245.toString();
    }

    private final void appendParameters(Iap2ParameterList parameters, List<Iap2FieldSpec> specs, int depth, List<String> lines) {
        String name;
        String value;
        List<Iap2FieldSpec> $this$associateBy$iv = specs;
        int capacity$iv = RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault($this$associateBy$iv, 10)), 16);
        Map fields = new LinkedHashMap(capacity$iv);
        for (Object element$iv$iv : $this$associateBy$iv) {
            Iap2FieldSpec p0 = (Iap2FieldSpec) element$iv$iv;
            fields.put(Integer.valueOf(p0.getId()), element$iv$iv);
        }
        Iterable $this$forEach$iv = CollectionsKt.take(parameters.asList(), 80);
        for (Object element$iv : $this$forEach$iv) {
            Iap2Parameter parameter = (Iap2Parameter) element$iv;
            Iap2FieldSpec field = (Iap2FieldSpec) fields.get(Integer.valueOf(parameter.getId()));
            if (field == null || (name = field.getName()) == null) {
                name = "param";
            }
            if (field != null) {
                value = INSTANCE.formatKnown(parameter.getPayload(), field, depth);
            } else {
                value = INSTANCE.formatOpaque(parameter.getPayload());
            }
            lines.add(INSTANCE.indent(depth) + "[" + parameter.getId() + "] " + name + ": " + value);
        }
        if (parameters.getSize() > 80) {
            lines.add(indent(depth) + "... " + (parameters.getSize() - 80) + " more parameters");
        }
    }

    private final String formatKnown(byte[] payload, Iap2FieldSpec spec, int depth) {
        String strScalar;
        try {
            boolean z = true;
            switch (WhenMappings.$EnumSwitchMapping$0[spec.getType().ordinal()]) {
                case 1:
                    if (payload.length != 0) {
                        z = false;
                    }
                    strScalar = !z ? "void? " + hexPreview(payload) : "void";
                    break;
                case 2:
                    strScalar = scalar("u8", payload, 1, Integer.valueOf(Iap2WireCodec.readU8$default(Iap2WireCodec.INSTANCE, payload, 0, 2, null)));
                    break;
                case 3:
                    strScalar = scalar("i8", payload, 1, Integer.valueOf(Iap2WireCodec.readI8$default(Iap2WireCodec.INSTANCE, payload, 0, 2, null)));
                    break;
                case 4:
                    strScalar = scalar("u16", payload, 2, Integer.valueOf(Iap2WireCodec.readU16$default(Iap2WireCodec.INSTANCE, payload, 0, 2, null)));
                    break;
                case 5:
                    strScalar = scalar("i16", payload, 2, Integer.valueOf(Iap2WireCodec.readI16$default(Iap2WireCodec.INSTANCE, payload, 0, 2, null)));
                    break;
                case 6:
                    strScalar = scalar("u32", payload, 4, Long.valueOf(Iap2WireCodec.readU32$default(Iap2WireCodec.INSTANCE, payload, 0, 2, null)));
                    break;
                case 7:
                    strScalar = scalar("i32", payload, 4, Integer.valueOf(Iap2WireCodec.readI32$default(Iap2WireCodec.INSTANCE, payload, 0, 2, null)));
                    break;
                case 8:
                    strScalar = scalar("u64", payload, 8, Long.valueOf(Iap2WireCodec.readU64$default(Iap2WireCodec.INSTANCE, payload, 0, 2, null)));
                    break;
                case 9:
                    strScalar = CollectionsKt.joinToString$default(Iap2WireCodec.INSTANCE.readU16List(payload), null, "[", "]", 0, null, new Function1() { // from class: com.carplay.iap2.trace.Iap2FrameFormatter$$ExternalSyntheticLambda1
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return Iap2FrameFormatter.formatKnown$lambda$7(((Integer) obj).intValue());
                        }
                    }, 25, null);
                    break;
                case 10:
                    strScalar = bytes(payload);
                    break;
                case 11:
                    strScalar = quote(truncate(Iap2WireCodec.INSTANCE.readString(payload)));
                    break;
                case 12:
                    strScalar = CollectionsKt.joinToString$default(Iap2WireCodec.INSTANCE.readStrings(payload), null, "[", "]", 0, null, new Function1() { // from class: com.carplay.iap2.trace.Iap2FrameFormatter$$ExternalSyntheticLambda2
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return Iap2FrameFormatter.formatKnown$lambda$8((String) obj);
                        }
                    }, 25, null);
                    break;
                case 13:
                    Iap2ParameterList nested = Iap2ParameterList.INSTANCE.parse(payload);
                    ArrayList nestedLines = new ArrayList();
                    appendParameters(nested, spec.getChildren(), depth + 1, nestedLines);
                    strScalar = !nestedLines.isEmpty() ? CollectionsKt.joinToString$default(nestedLines, "\n", "{\n", "\n" + indent(depth) + "}", 0, null, null, 56, null) : "{}";
                    break;
                case 14:
                    strScalar = formatOpaque(payload);
                    break;
                default:
                    throw new NoWhenBranchMatchedException();
            }
            return strScalar;
        } catch (Exception e) {
            return formatOpaque(payload);
        }
    }

    static final CharSequence formatKnown$lambda$7(int it) {
        String string = Integer.toString(it, CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return "0x" + StringsKt.padStart(string, 4, '0');
    }

    static final CharSequence formatKnown$lambda$8(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return INSTANCE.quote(INSTANCE.truncate(it));
    }

    private final String formatOpaque(byte[] payload) {
        if (payload.length == 0) {
            return "void";
        }
        String it = readableString(payload);
        if (it != null) {
            return INSTANCE.quote(INSTANCE.truncate(it));
        }
        switch (payload.length) {
            case 1:
                return "u8=" + u8(payload[0]) + " (0x" + hexPreview(payload) + ")";
            case 2:
                return "u16=" + Iap2WireCodec.readU16$default(Iap2WireCodec.INSTANCE, payload, 0, 2, null) + " (0x" + hexPreview(payload) + ")";
            case 4:
                return "u32=" + Iap2WireCodec.readU32$default(Iap2WireCodec.INSTANCE, payload, 0, 2, null) + " (0x" + hexPreview(payload) + ")";
            case 8:
                return "u64=" + Iap2WireCodec.readU64$default(Iap2WireCodec.INSTANCE, payload, 0, 2, null) + " (0x" + hexPreview(payload) + ")";
            default:
                return bytes(payload);
        }
    }

    private final String scalar(String name, byte[] payload, int size, Number value) {
        if (payload.length == size) {
            return name + "=" + value + " (0x" + hexPreview(payload) + ")";
        }
        throw new IllegalArgumentException(("expected " + size + " bytes").toString());
    }

    private final String bytes(byte[] payload) {
        return "bytes=" + payload.length + " [" + hexPreview(payload) + "]";
    }

    private final String readableString(byte[] payload) {
        boolean z = true;
        if ((payload.length == 0) || ArraysKt.last(payload) != 0) {
            return null;
        }
        byte[] content = Arrays.copyOf(payload, payload.length - 1);
        Intrinsics.checkNotNullExpressionValue(content, "copyOf(...)");
        int length = content.length;
        int i = 0;
        while (true) {
            if (i < length) {
                int element$iv = content[i];
                int value = element$iv & 255;
                if (((value >= 32 || value == 9) ? (byte) 0 : (byte) 1) != 0) {
                    break;
                }
                i++;
            } else {
                z = false;
                break;
            }
        }
        if (z) {
            return null;
        }
        String decoded = StringsKt.decodeToString(content);
        if (StringsKt.contains$default((CharSequence) decoded, (char) 65533, false, 2, (Object) null)) {
            return null;
        }
        return decoded;
    }

    private final String quote(String value) {
        return "\"" + StringsKt.replace$default(StringsKt.replace$default(StringsKt.replace$default(StringsKt.replace$default(StringsKt.replace$default(value, "\\", "\\\\", false, 4, (Object) null), "\"", "\\\"", false, 4, (Object) null), "\r", "\\r", false, 4, (Object) null), "\n", "\\n", false, 4, (Object) null), "\t", "\\t", false, 4, (Object) null) + "\"";
    }

    private final String truncate(String value) {
        if (value.length() <= MAX_STRING_CHARS) {
            return value;
        }
        return StringsKt.take(value, MAX_STRING_CHARS) + "...";
    }

    private final String hexPreview(byte[] payload) {
        if (payload.length == 0) {
            return "<empty>";
        }
        String shown = CollectionsKt.joinToString$default(ArraysKt.take(payload, 24), " ", null, null, 0, null, new Function1() { // from class: com.carplay.iap2.trace.Iap2FrameFormatter$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Iap2FrameFormatter.hexPreview$lambda$12(((Byte) obj).byteValue());
            }
        }, 30, null);
        if (payload.length <= 24) {
            return shown;
        }
        return shown + " ... (+" + (payload.length - 24) + "B)";
    }

    static final CharSequence hexPreview$lambda$12(byte it) {
        String str = String.format("%02x", Arrays.copyOf(new Object[]{Integer.valueOf(it & UByte.MAX_VALUE)}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    private final int u8(byte value) {
        return value & UByte.MAX_VALUE;
    }

    private final String indent(int depth) {
        return StringsKt.repeat("  ", depth);
    }
}
