package com.carplay.iap2.body;

import com.carplay.iap2.catalog.Iap2Endpoint;
import com.carplay.iap2.wire.Iap2Frame;
import com.carplay.iap2.wire.Iap2Parameter;
import com.carplay.iap2.wire.Iap2ParameterList;
import com.carplay.iap2.wire.Iap2ProtocolException;
import com.carplay.iap2.wire.Iap2WireCodec;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: Iap2BodyReader.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0010\n\u0002\u0010\t\n\u0002\b\u0014\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 D2\u00020\u0001:\u0001DB\u0011\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u0007J\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\r\u001a\u00020\u0007J\u0018\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\r\u001a\u00020\u00072\b\b\u0002\u0010\u0011\u001a\u00020\u0012J\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00142\u0006\u0010\r\u001a\u00020\u0007J\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\r\u001a\u00020\u0007J\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u00162\u0006\u0010\r\u001a\u00020\u0007J\u000e\u0010\u0018\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u0007J\u000e\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u0007J\u000e\u0010\u001a\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0007J\u0015\u0010\u001b\u001a\u0004\u0018\u00010\u00072\u0006\u0010\r\u001a\u00020\u0007¢\u0006\u0002\u0010\u001cJ\u000e\u0010\u001d\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0007J\u0015\u0010\u001e\u001a\u0004\u0018\u00010\u00072\u0006\u0010\r\u001a\u00020\u0007¢\u0006\u0002\u0010\u001cJ\u000e\u0010\u001f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u0007J\u0015\u0010 \u001a\u0004\u0018\u00010\u000b2\u0006\u0010\r\u001a\u00020\u0007¢\u0006\u0002\u0010!J\u000e\u0010\"\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0007J\u0015\u0010#\u001a\u0004\u0018\u00010\u00072\u0006\u0010\r\u001a\u00020\u0007¢\u0006\u0002\u0010\u001cJ\u000e\u0010$\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0007J\u0015\u0010%\u001a\u0004\u0018\u00010\u00072\u0006\u0010\r\u001a\u00020\u0007¢\u0006\u0002\u0010\u001cJ\u000e\u0010&\u001a\u00020'2\u0006\u0010\r\u001a\u00020\u0007J\u0015\u0010(\u001a\u0004\u0018\u00010'2\u0006\u0010\r\u001a\u00020\u0007¢\u0006\u0002\u0010)J\u000e\u0010*\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0007J\u0015\u0010+\u001a\u0004\u0018\u00010\u00072\u0006\u0010\r\u001a\u00020\u0007¢\u0006\u0002\u0010\u001cJ\u000e\u0010,\u001a\u00020'2\u0006\u0010\r\u001a\u00020\u0007J\u0015\u0010-\u001a\u0004\u0018\u00010'2\u0006\u0010\r\u001a\u00020\u0007¢\u0006\u0002\u0010)J\u000e\u0010.\u001a\u00020\u00162\u0006\u0010\r\u001a\u00020\u0007J\u0010\u0010/\u001a\u0004\u0018\u00010\u00162\u0006\u0010\r\u001a\u00020\u0007J\u000e\u00100\u001a\u00020\u00122\u0006\u0010\r\u001a\u00020\u0007J\u0010\u00101\u001a\u0004\u0018\u00010\u00122\u0006\u0010\r\u001a\u00020\u0007J\u0014\u00102\u001a\b\u0012\u0004\u0012\u00020\u00120\u00142\u0006\u0010\r\u001a\u00020\u0007J\u0016\u00103\u001a\n\u0012\u0004\u0012\u00020\u0012\u0018\u00010\u00142\u0006\u0010\r\u001a\u00020\u0007J\u0014\u00104\u001a\b\u0012\u0004\u0012\u00020\u00070\u00142\u0006\u0010\r\u001a\u00020\u0007J\u0016\u00105\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u00142\u0006\u0010\r\u001a\u00020\u0007J\u000e\u00106\u001a\u00020\u00002\u0006\u0010\r\u001a\u00020\u0007J\u0010\u00107\u001a\u0004\u0018\u00010\u00002\u0006\u0010\r\u001a\u00020\u0007J\f\u00108\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0014J\u0018\u00109\u001a\u00020\u00162\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010:\u001a\u00020\u0007H\u0002J \u0010;\u001a\u00020<2\u0006\u0010=\u001a\u00020\u000f2\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010:\u001a\u00020\u0007H\u0002J\u0018\u0010>\u001a\u00020<2\u0006\u0010=\u001a\u00020\u000f2\u0006\u0010\r\u001a\u00020\u0007H\u0002J\u0018\u0010?\u001a\u00020@2\u0006\u0010\r\u001a\u00020\u00072\u0006\u0010A\u001a\u00020@H\u0002J\u0010\u0010B\u001a\u00020\u00122\u0006\u0010\r\u001a\u00020\u0007H\u0002J\u0010\u0010C\u001a\u00020\u00122\u0006\u0010\r\u001a\u00020\u0007H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006E"}, d2 = {"Lcom/carplay/iap2/body/Iap2BodyReader;", "", "parameters", "Lcom/carplay/iap2/wire/Iap2ParameterList;", "<init>", "(Lcom/carplay/iap2/wire/Iap2ParameterList;)V", "size", "", "getSize", "()I", "isEmpty", "", "has", "id", "first", "Lcom/carplay/iap2/wire/Iap2Parameter;", "require", "description", "", "all", "", "raw", "", "optionalRaw", "void", "optionalVoid", "u8", "optionalU8", "(I)Ljava/lang/Integer;", "i8", "optionalI8", "bool", "optionalBool", "(I)Ljava/lang/Boolean;", "u16", "optionalU16", "i16", "optionalI16", "u32", "", "optionalU32", "(I)Ljava/lang/Long;", "i32", "optionalI32", "u64", "optionalU64", "bytes", "optionalBytes", "string", "optionalString", "strings", "optionalStrings", "u16List", "optionalU16List", "group", "optionalGroup", "list", "scalar", "expectedBytes", "requireSize", "", "parameter", "requireEmpty", "malformed", "Lcom/carplay/iap2/wire/Iap2ProtocolException;", "failure", "defaultDescription", "hex", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2BodyReader {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final Iap2ParameterList parameters;

    public /* synthetic */ Iap2BodyReader(Iap2ParameterList iap2ParameterList, DefaultConstructorMarker defaultConstructorMarker) {
        this(iap2ParameterList);
    }

    private Iap2BodyReader(Iap2ParameterList parameters) {
        this.parameters = parameters;
    }

    public final int getSize() {
        return this.parameters.getSize();
    }

    public final boolean isEmpty() {
        return this.parameters.isEmpty();
    }

    public final boolean has(int id) {
        return this.parameters.has(id);
    }

    public final Iap2Parameter first(int id) {
        return this.parameters.first(id);
    }

    public static /* synthetic */ Iap2Parameter require$default(Iap2BodyReader iap2BodyReader, int i, String str, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            str = iap2BodyReader.defaultDescription(i);
        }
        return iap2BodyReader.require(i, str);
    }

    public final Iap2Parameter require(int id, String description) {
        Intrinsics.checkNotNullParameter(description, "description");
        return this.parameters.require(id, description);
    }

    public final List<Iap2Parameter> all(int id) {
        return this.parameters.all(id);
    }

    public final byte[] raw(int id) {
        return require$default(this, id, null, 2, null).getPayload();
    }

    public final byte[] optionalRaw(int id) {
        Iap2Parameter iap2ParameterFirst = first(id);
        if (iap2ParameterFirst != null) {
            return iap2ParameterFirst.getPayload();
        }
        return null;
    }

    /* JADX INFO: renamed from: void, reason: not valid java name */
    public final boolean m52void(int id) throws Iap2ProtocolException {
        Iap2Parameter parameter = require$default(this, id, null, 2, null);
        requireEmpty(parameter, id);
        return true;
    }

    public final boolean optionalVoid(int id) throws Iap2ProtocolException {
        Iap2Parameter it = first(id);
        if (it != null) {
            requireEmpty(it, id);
            return true;
        }
        return false;
    }

    public final int u8(int id) {
        return Iap2WireCodec.readU8$default(Iap2WireCodec.INSTANCE, scalar(id, 1), 0, 2, null);
    }

    public final Integer optionalU8(int id) throws Iap2ProtocolException {
        Iap2Parameter it = first(id);
        if (it == null) {
            return null;
        }
        requireSize(it, id, 1);
        return Integer.valueOf(Iap2WireCodec.readU8$default(Iap2WireCodec.INSTANCE, it.getPayload(), 0, 2, null));
    }

    public final int i8(int id) {
        return Iap2WireCodec.readI8$default(Iap2WireCodec.INSTANCE, scalar(id, 1), 0, 2, null);
    }

    public final Integer optionalI8(int id) throws Iap2ProtocolException {
        Iap2Parameter it = first(id);
        if (it == null) {
            return null;
        }
        requireSize(it, id, 1);
        return Integer.valueOf(Iap2WireCodec.readI8$default(Iap2WireCodec.INSTANCE, it.getPayload(), 0, 2, null));
    }

    public final boolean bool(int id) {
        return u8(id) != 0;
    }

    public final Boolean optionalBool(int id) throws Iap2ProtocolException {
        Integer numOptionalU8 = optionalU8(id);
        if (numOptionalU8 == null) {
            return null;
        }
        int it = numOptionalU8.intValue();
        return Boolean.valueOf(it != 0);
    }

    public final int u16(int id) {
        return Iap2WireCodec.readU16$default(Iap2WireCodec.INSTANCE, scalar(id, 2), 0, 2, null);
    }

    public final Integer optionalU16(int id) throws Iap2ProtocolException {
        Iap2Parameter it = first(id);
        if (it == null) {
            return null;
        }
        requireSize(it, id, 2);
        return Integer.valueOf(Iap2WireCodec.readU16$default(Iap2WireCodec.INSTANCE, it.getPayload(), 0, 2, null));
    }

    public final int i16(int id) {
        return Iap2WireCodec.readI16$default(Iap2WireCodec.INSTANCE, scalar(id, 2), 0, 2, null);
    }

    public final Integer optionalI16(int id) throws Iap2ProtocolException {
        Iap2Parameter it = first(id);
        if (it == null) {
            return null;
        }
        requireSize(it, id, 2);
        return Integer.valueOf(Iap2WireCodec.readI16$default(Iap2WireCodec.INSTANCE, it.getPayload(), 0, 2, null));
    }

    public final long u32(int id) {
        return Iap2WireCodec.readU32$default(Iap2WireCodec.INSTANCE, scalar(id, 4), 0, 2, null);
    }

    public final Long optionalU32(int id) throws Iap2ProtocolException {
        Iap2Parameter it = first(id);
        if (it == null) {
            return null;
        }
        requireSize(it, id, 4);
        return Long.valueOf(Iap2WireCodec.readU32$default(Iap2WireCodec.INSTANCE, it.getPayload(), 0, 2, null));
    }

    public final int i32(int id) {
        return Iap2WireCodec.readI32$default(Iap2WireCodec.INSTANCE, scalar(id, 4), 0, 2, null);
    }

    public final Integer optionalI32(int id) throws Iap2ProtocolException {
        Iap2Parameter it = first(id);
        if (it == null) {
            return null;
        }
        requireSize(it, id, 4);
        return Integer.valueOf(Iap2WireCodec.readI32$default(Iap2WireCodec.INSTANCE, it.getPayload(), 0, 2, null));
    }

    public final long u64(int id) {
        return Iap2WireCodec.readU64$default(Iap2WireCodec.INSTANCE, scalar(id, 8), 0, 2, null);
    }

    public final Long optionalU64(int id) throws Iap2ProtocolException {
        Iap2Parameter it = first(id);
        if (it == null) {
            return null;
        }
        requireSize(it, id, 8);
        return Long.valueOf(Iap2WireCodec.readU64$default(Iap2WireCodec.INSTANCE, it.getPayload(), 0, 2, null));
    }

    public final byte[] bytes(int id) {
        return raw(id);
    }

    public final byte[] optionalBytes(int id) {
        return optionalRaw(id);
    }

    public final String string(int id) throws Iap2ProtocolException {
        try {
            return Iap2WireCodec.INSTANCE.readString(raw(id));
        } catch (Iap2ProtocolException failure) {
            throw malformed(id, failure);
        }
    }

    public final String optionalString(int id) throws Iap2ProtocolException {
        Iap2Parameter it = first(id);
        if (it == null) {
            return null;
        }
        try {
            return Iap2WireCodec.INSTANCE.readString(it.getPayload());
        } catch (Iap2ProtocolException failure) {
            throw malformed(id, failure);
        }
    }

    public final List<String> strings(int id) throws Iap2ProtocolException {
        try {
            return Iap2WireCodec.INSTANCE.readStrings(raw(id));
        } catch (Iap2ProtocolException failure) {
            throw malformed(id, failure);
        }
    }

    public final List<String> optionalStrings(int id) throws Iap2ProtocolException {
        Iap2Parameter it = first(id);
        if (it == null) {
            return null;
        }
        try {
            return Iap2WireCodec.INSTANCE.readStrings(it.getPayload());
        } catch (Iap2ProtocolException failure) {
            throw malformed(id, failure);
        }
    }

    public final List<Integer> u16List(int id) throws Iap2ProtocolException {
        try {
            return Iap2WireCodec.INSTANCE.readU16List(raw(id));
        } catch (Iap2ProtocolException failure) {
            throw malformed(id, failure);
        }
    }

    public final List<Integer> optionalU16List(int id) throws Iap2ProtocolException {
        Iap2Parameter it = first(id);
        if (it == null) {
            return null;
        }
        try {
            return Iap2WireCodec.INSTANCE.readU16List(it.getPayload());
        } catch (Iap2ProtocolException failure) {
            throw malformed(id, failure);
        }
    }

    public final Iap2BodyReader group(int id) {
        return new Iap2BodyReader(Iap2ParameterList.INSTANCE.parse(raw(id)));
    }

    public final Iap2BodyReader optionalGroup(int id) {
        byte[] it = optionalRaw(id);
        if (it != null) {
            return new Iap2BodyReader(Iap2ParameterList.INSTANCE.parse(it));
        }
        return null;
    }

    public final List<Iap2Parameter> list() {
        return this.parameters.asList();
    }

    private final byte[] scalar(int id, int expectedBytes) throws Iap2ProtocolException {
        Iap2Parameter parameter = require$default(this, id, null, 2, null);
        requireSize(parameter, id, expectedBytes);
        return parameter.getPayload();
    }

    private final void requireSize(Iap2Parameter parameter, int id, int expectedBytes) throws Iap2ProtocolException {
        if (parameter.getPayload().length != expectedBytes) {
            throw new Iap2ProtocolException("Parameter 0x" + hex(id) + " expected " + expectedBytes + " bytes, got " + parameter.getPayload().length, null, 2, null);
        }
    }

    private final void requireEmpty(Iap2Parameter parameter, int id) throws Iap2ProtocolException {
        if (!(parameter.getPayload().length == 0)) {
            throw new Iap2ProtocolException("Parameter 0x" + hex(id) + " must be void", null, 2, null);
        }
    }

    private final Iap2ProtocolException malformed(int id, Iap2ProtocolException failure) {
        return new Iap2ProtocolException("Malformed parameter 0x" + hex(id), failure);
    }

    private final String defaultDescription(int id) {
        return "parameter 0x" + hex(id);
    }

    private final String hex(int id) {
        String string = Integer.toString(id, CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return StringsKt.padStart(string, 4, '0');
    }

    /* JADX INFO: compiled from: Iap2BodyReader.kt */
    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tJ\u0016\u0010\u0004\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0006\u001a\u00020\u0007¨\u0006\f"}, d2 = {"Lcom/carplay/iap2/body/Iap2BodyReader$Companion;", "", "<init>", "()V", "of", "Lcom/carplay/iap2/body/Iap2BodyReader;", "frame", "Lcom/carplay/iap2/wire/Iap2Frame;", "body", "Lcom/carplay/iap2/wire/Iap2ParameterList;", "endpoint", "Lcom/carplay/iap2/catalog/Iap2Endpoint;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Iap2BodyReader of(Iap2Frame frame) {
            Intrinsics.checkNotNullParameter(frame, "frame");
            return new Iap2BodyReader(Iap2ParameterList.INSTANCE.parse(frame.getPayload()), null);
        }

        public final Iap2BodyReader of(Iap2ParameterList body) {
            Intrinsics.checkNotNullParameter(body, "body");
            return new Iap2BodyReader(body, null);
        }

        public final Iap2BodyReader of(Iap2Endpoint endpoint, Iap2Frame frame) {
            Intrinsics.checkNotNullParameter(endpoint, "endpoint");
            Intrinsics.checkNotNullParameter(frame, "frame");
            if (!(frame.getMessageId() == endpoint.getId())) {
                String string = Integer.toString(frame.getMessageId(), CharsKt.checkRadix(16));
                Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                throw new IllegalArgumentException(("Frame 0x" + string + " is not " + endpoint.getName()).toString());
            }
            return of(frame);
        }
    }
}
