package com.carplay.iap2.wire;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Iap2Wire.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0005\u0018\u0000 \u001c2\u00020\u0001:\u0001\u001cB\u0017\b\u0002\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\u0006\u0010\u000b\u001a\u00020\fJ\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003J\u0011\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\bH\u0086\u0002J\u000e\u0010\u0010\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\bJ\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0011\u001a\u00020\bJ\u0018\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\b2\b\b\u0002\u0010\u0014\u001a\u00020\u0015J\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0011\u001a\u00020\bJ\u0006\u0010\u0017\u001a\u00020\u0018J\u0013\u0010\u0019\u001a\u00020\f2\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001H\u0096\u0002J\b\u0010\u001b\u001a\u00020\bH\u0016R\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0007\u001a\u00020\b8F¢\u0006\u0006\u001a\u0004\b\t\u0010\n¨\u0006\u001d"}, d2 = {"Lcom/carplay/iap2/wire/Iap2ParameterList;", "", "parameters", "", "Lcom/carplay/iap2/wire/Iap2Parameter;", "<init>", "(Ljava/util/List;)V", "size", "", "getSize", "()I", "isEmpty", "", "asList", "get", "index", "has", "id", "first", "require", "description", "", "all", "encode", "", "equals", "other", "hashCode", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2ParameterList {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final Iap2ParameterList EMPTY = new Iap2ParameterList(CollectionsKt.emptyList());
    private static final int MAX_BODY_BYTES = 65529;
    private final List<Iap2Parameter> parameters;

    public /* synthetic */ Iap2ParameterList(List list, DefaultConstructorMarker defaultConstructorMarker) {
        this(list);
    }

    private Iap2ParameterList(List<Iap2Parameter> list) {
        this.parameters = list;
    }

    public final int getSize() {
        return this.parameters.size();
    }

    public final boolean isEmpty() {
        return this.parameters.isEmpty();
    }

    public final List<Iap2Parameter> asList() {
        return CollectionsKt.toList(this.parameters);
    }

    public final Iap2Parameter get(int index) {
        return this.parameters.get(index);
    }

    public final boolean has(int id) {
        Iterable $this$any$iv = this.parameters;
        if (($this$any$iv instanceof Collection) && ((Collection) $this$any$iv).isEmpty()) {
            return false;
        }
        for (Object element$iv : $this$any$iv) {
            Iap2Parameter it = (Iap2Parameter) element$iv;
            if (it.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public final Iap2Parameter first(int id) {
        Object element$iv;
        Iterable $this$firstOrNull$iv = this.parameters;
        Iterator it = $this$firstOrNull$iv.iterator();
        while (it.hasNext()) {
            element$iv = it.next();
            Iap2Parameter it2 = (Iap2Parameter) element$iv;
            if (it2.getId() == id) {
                return (Iap2Parameter) element$iv;
            }
        }
        element$iv = null;
        return (Iap2Parameter) element$iv;
    }

    public static /* synthetic */ Iap2Parameter require$default(Iap2ParameterList iap2ParameterList, int i, String str, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            String string = Integer.toString(i, CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            str = "parameter 0x" + StringsKt.padStart(string, 4, '0');
        }
        return iap2ParameterList.require(i, str);
    }

    public final Iap2Parameter require(int id, String description) throws Iap2ProtocolException {
        Intrinsics.checkNotNullParameter(description, "description");
        Iap2Parameter iap2ParameterFirst = first(id);
        if (iap2ParameterFirst != null) {
            return iap2ParameterFirst;
        }
        throw new Iap2ProtocolException("Missing required " + description, null, 2, null);
    }

    public final List<Iap2Parameter> all(int id) {
        Iterable $this$filter$iv = this.parameters;
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv : $this$filter$iv) {
            Iap2Parameter it = (Iap2Parameter) element$iv$iv;
            if (it.getId() == id) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        return (List) destination$iv$iv;
    }

    public final byte[] encode() {
        ByteArrayOutputStream encoded = new ByteArrayOutputStream();
        for (Iap2Parameter parameter : this.parameters) {
            byte[] next = Iap2CsmFramer.INSTANCE.encodeParam(parameter.getId(), parameter.getBytes());
            if (!(next.length <= 65529 - encoded.size())) {
                throw new IllegalArgumentException("Encoded CSM parameters exceed the 65529-byte message body limit".toString());
            }
            encoded.write(next);
        }
        byte[] byteArray = encoded.toByteArray();
        Intrinsics.checkNotNullExpressionValue(byteArray, "toByteArray(...)");
        return byteArray;
    }

    public boolean equals(Object other) {
        return (other instanceof Iap2ParameterList) && Intrinsics.areEqual(this.parameters, ((Iap2ParameterList) other).parameters);
    }

    public int hashCode() {
        return this.parameters.hashCode();
    }

    /* JADX INFO: compiled from: Iap2Wire.kt */
    @Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0014\u0010\n\u001a\u00020\u00072\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fJ\u001f\u0010\n\u001a\u00020\u00072\u0012\u0010\u000b\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0\u000e\"\u00020\r¢\u0006\u0002\u0010\u000fJ\u000e\u0010\u0010\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\u0012R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t¨\u0006\u0013"}, d2 = {"Lcom/carplay/iap2/wire/Iap2ParameterList$Companion;", "", "<init>", "()V", "MAX_BODY_BYTES", "", "EMPTY", "Lcom/carplay/iap2/wire/Iap2ParameterList;", "getEMPTY", "()Lcom/carplay/iap2/wire/Iap2ParameterList;", "of", "parameters", "", "Lcom/carplay/iap2/wire/Iap2Parameter;", "", "([Lcom/carplay/iap2/wire/Iap2Parameter;)Lcom/carplay/iap2/wire/Iap2ParameterList;", "parse", "encoded", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Iap2ParameterList getEMPTY() {
            return Iap2ParameterList.EMPTY;
        }

        public final Iap2ParameterList of(Iterable<Iap2Parameter> parameters) {
            Intrinsics.checkNotNullParameter(parameters, "parameters");
            return new Iap2ParameterList(CollectionsKt.toList(parameters), null);
        }

        public final Iap2ParameterList of(Iap2Parameter... parameters) {
            Intrinsics.checkNotNullParameter(parameters, "parameters");
            return of(ArraysKt.asList(parameters));
        }

        public final Iap2ParameterList parse(byte[] encoded) throws Iap2ProtocolException {
            Intrinsics.checkNotNullParameter(encoded, "encoded");
            ArrayList parameters = new ArrayList();
            int offset = 0;
            while (true) {
                DefaultConstructorMarker defaultConstructorMarker = null;
                if (offset < encoded.length) {
                    if (encoded.length - offset < 4) {
                        throw new Iap2ProtocolException("Truncated CSM parameter header", null, 2, null);
                    }
                    int length = Iap2WireCodec.INSTANCE.readU16(encoded, offset);
                    if (length < 4 || length > encoded.length - offset) {
                        throw new Iap2ProtocolException("Invalid CSM parameter length " + length, null, 2, null);
                    }
                    parameters.add(new Iap2Parameter(Iap2WireCodec.INSTANCE.readU16(encoded, offset + 2), ArraysKt.copyOfRange(encoded, offset + 4, offset + length)));
                    offset += length;
                } else {
                    return new Iap2ParameterList(parameters, defaultConstructorMarker);
                }
            }
        }
    }
}
