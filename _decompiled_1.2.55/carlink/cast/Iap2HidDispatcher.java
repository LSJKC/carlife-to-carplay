package com.carlink.cast;

import com.carplay.adapter.PhoneProjectionAdapter;
import com.carplay.iap2.body.Iap2BodyBuilder;
import com.carplay.iap2.body.Iap2BodyReader;
import com.carplay.iap2.message.Iap2Messages;
import com.carplay.iap2.wire.Iap2Frame;
import com.carplay.iap2.wire.Iap2Parameter;
import com.carplay.iap2.wire.Iap2ProtocolException;
import com.carplay.sender.HidInput;
import com.carplay.sender.HidKeyReport;
import com.carplay.sender.HidKeys;
import com.carplay.sender.SenderDisplay;
import com.carplay.sender.TouchSample;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.CharsKt;

/* JADX INFO: compiled from: Iap2HidDispatcher.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0000\u0018\u0000 \u001d2\u00020\u0001:\u0003\u001b\u001c\u001dB#\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005¢\u0006\u0004\b\b\u0010\tJ\u0010\u0010\u0010\u001a\u00020\u00072\b\u0010\u0011\u001a\u0004\u0018\u00010\u000fJ\"\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0012\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00070\u0005J$\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0012\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00070\u0005H\u0002J\u0010\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0010\u0010\u0019\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0006\u0010\u001a\u001a\u00020\u0007R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001e"}, d2 = {"Lcom/carlink/cast/Iap2HidDispatcher;", "", "source", "Lcom/carplay/adapter/PhoneProjectionAdapter;", "log", "Lkotlin/Function1;", "", "", "<init>", "(Lcom/carplay/adapter/PhoneProjectionAdapter;Lkotlin/jvm/functions/Function1;)V", "devices", "Ljava/util/concurrent/ConcurrentHashMap;", "", "Lcom/carlink/cast/Iap2HidDispatcher$Device;", "display", "Lcom/carplay/sender/SenderDisplay;", "setDisplay", "value", "handle", "", "frame", "Lcom/carplay/iap2/wire/Iap2Frame;", "send", "start", "report", "stop", "reset", "Device", "DescriptorCandidate", "Companion", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Iap2HidDispatcher {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int HID_REPORT_DATA_PARAM = 1;
    private final ConcurrentHashMap<Integer, Device> devices;
    private volatile SenderDisplay display;
    private final Function1<String, Unit> log;
    private final PhoneProjectionAdapter source;

    /* JADX WARN: Multi-variable type inference failed */
    public Iap2HidDispatcher(PhoneProjectionAdapter source, Function1<? super String, Unit> log) {
        Intrinsics.checkNotNullParameter(source, "source");
        Intrinsics.checkNotNullParameter(log, "log");
        this.source = source;
        this.log = log;
        this.devices = new ConcurrentHashMap<>();
    }

    /* JADX INFO: compiled from: Iap2HidDispatcher.kt */
    @Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B?\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u000b¢\u0006\u0004\b\f\u0010\rJ\t\u0010\u001a\u001a\u00020\u0003HÆ\u0003J\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u0011J\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u0011J\t\u0010\u001d\u001a\u00020\u0007HÆ\u0003J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\tHÆ\u0003J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\u000bHÆ\u0003JR\u0010 \u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u000bHÆ\u0001¢\u0006\u0002\u0010!J\u0013\u0010\"\u001a\u00020#2\b\u0010$\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010%\u001a\u00020\u0003HÖ\u0001J\t\u0010&\u001a\u00020'HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0015\u0010\u0004\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0010\u0010\u0011R\u0015\u0010\u0005\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0013\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0013\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0013\u0010\n\u001a\u0004\u0018\u00010\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019¨\u0006("}, d2 = {"Lcom/carlink/cast/Iap2HidDispatcher$Device;", "", "id", "", "width", "height", "descriptor", "", "keys", "Lcom/carplay/sender/HidKeys;", CastSettings.KEY_TOUCH, "Lcom/carplay/sender/HidInput;", "<init>", "(ILjava/lang/Integer;Ljava/lang/Integer;[BLcom/carplay/sender/HidKeys;Lcom/carplay/sender/HidInput;)V", "getId", "()I", "getWidth", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getHeight", "getDescriptor", "()[B", "getKeys", "()Lcom/carplay/sender/HidKeys;", "getTouch", "()Lcom/carplay/sender/HidInput;", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "(ILjava/lang/Integer;Ljava/lang/Integer;[BLcom/carplay/sender/HidKeys;Lcom/carplay/sender/HidInput;)Lcom/carlink/cast/Iap2HidDispatcher$Device;", "equals", "", "other", "hashCode", "toString", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class Device {
        private final byte[] descriptor;
        private final Integer height;
        private final int id;
        private final HidKeys keys;
        private final HidInput touch;
        private final Integer width;

        public static /* synthetic */ Device copy$default(Device device, int i, Integer num, Integer num2, byte[] bArr, HidKeys hidKeys, HidInput hidInput, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                i = device.id;
            }
            if ((i2 & 2) != 0) {
                num = device.width;
            }
            if ((i2 & 4) != 0) {
                num2 = device.height;
            }
            if ((i2 & 8) != 0) {
                bArr = device.descriptor;
            }
            if ((i2 & 16) != 0) {
                hidKeys = device.keys;
            }
            if ((i2 & 32) != 0) {
                hidInput = device.touch;
            }
            HidKeys hidKeys2 = hidKeys;
            HidInput hidInput2 = hidInput;
            return device.copy(i, num, num2, bArr, hidKeys2, hidInput2);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final int getId() {
            return this.id;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final Integer getWidth() {
            return this.width;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final Integer getHeight() {
            return this.height;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final byte[] getDescriptor() {
            return this.descriptor;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final HidKeys getKeys() {
            return this.keys;
        }

        /* JADX INFO: renamed from: component6, reason: from getter */
        public final HidInput getTouch() {
            return this.touch;
        }

        public final Device copy(int id, Integer width, Integer height, byte[] descriptor, HidKeys keys, HidInput touch) {
            Intrinsics.checkNotNullParameter(descriptor, "descriptor");
            return new Device(id, width, height, descriptor, keys, touch);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Device)) {
                return false;
            }
            Device device = (Device) other;
            return this.id == device.id && Intrinsics.areEqual(this.width, device.width) && Intrinsics.areEqual(this.height, device.height) && Intrinsics.areEqual(this.descriptor, device.descriptor) && Intrinsics.areEqual(this.keys, device.keys) && Intrinsics.areEqual(this.touch, device.touch);
        }

        public int hashCode() {
            return (((((((((Integer.hashCode(this.id) * 31) + (this.width == null ? 0 : this.width.hashCode())) * 31) + (this.height == null ? 0 : this.height.hashCode())) * 31) + Arrays.hashCode(this.descriptor)) * 31) + (this.keys == null ? 0 : this.keys.hashCode())) * 31) + (this.touch != null ? this.touch.hashCode() : 0);
        }

        public String toString() {
            return "Device(id=" + this.id + ", width=" + this.width + ", height=" + this.height + ", descriptor=" + Arrays.toString(this.descriptor) + ", keys=" + this.keys + ", touch=" + this.touch + ")";
        }

        public Device(int id, Integer width, Integer height, byte[] descriptor, HidKeys keys, HidInput touch) {
            Intrinsics.checkNotNullParameter(descriptor, "descriptor");
            this.id = id;
            this.width = width;
            this.height = height;
            this.descriptor = descriptor;
            this.keys = keys;
            this.touch = touch;
        }

        public final int getId() {
            return this.id;
        }

        public final Integer getWidth() {
            return this.width;
        }

        public final Integer getHeight() {
            return this.height;
        }

        public final byte[] getDescriptor() {
            return this.descriptor;
        }

        public final HidKeys getKeys() {
            return this.keys;
        }

        public final HidInput getTouch() {
            return this.touch;
        }
    }

    public final void setDisplay(SenderDisplay value) {
        this.display = value;
    }

    public final boolean handle(Iap2Frame frame, Function1<? super Iap2Frame, Unit> send) {
        boolean zStart;
        Intrinsics.checkNotNullParameter(frame, "frame");
        Intrinsics.checkNotNullParameter(send, "send");
        try {
            switch (frame.getMessageId()) {
                case 26624:
                    zStart = start(frame, send);
                    break;
                case 26625:
                default:
                    zStart = false;
                    break;
                case 26626:
                    zStart = report(frame);
                    break;
                case 26627:
                    zStart = stop(frame);
                    break;
            }
            return zStart;
        } catch (Throwable failure) {
            Function1<String, Unit> function1 = this.log;
            String string = Integer.toString(frame.getMessageId(), CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            function1.invoke("iAP2 HID 0x" + string + " 处理失败：" + failure.getMessage());
            return true;
        }
    }

    /* JADX WARN: Code duplicated, block: B:63:0x0152  */
    private final boolean start(Iap2Frame frame, Function1<? super Iap2Frame, Unit> send) throws Iap2ProtocolException {
        byte[] bArrOptionalBytes;
        Object next;
        String str;
        Iap2BodyReader body = Iap2BodyReader.INSTANCE.of(frame);
        Integer numOptionalU16 = body.optionalU16(0);
        int id = numOptionalU16 != null ? numOptionalU16.intValue() : 0;
        Integer width = body.optionalU16(1);
        Integer height = body.optionalU16(2);
        Sequence sequenceAsSequence = CollectionsKt.asSequence(body.list());
        final Comparator comparator = new Comparator() { // from class: com.carlink.cast.Iap2HidDispatcher$start$$inlined$compareByDescending$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                return ComparisonsKt.compareValues(Boolean.valueOf(((Iap2Parameter) t2).getId() == 4), Boolean.valueOf(((Iap2Parameter) t).getId() == 4));
            }
        };
        DescriptorCandidate descriptor = (DescriptorCandidate) SequencesKt.firstOrNull(SequencesKt.mapNotNull(SequencesKt.filter(SequencesKt.map(SequencesKt.sortedWith(sequenceAsSequence, new Comparator() { // from class: com.carlink.cast.Iap2HidDispatcher$start$$inlined$thenByDescending$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int previousCompare = comparator.compare(t, t2);
                return previousCompare != 0 ? previousCompare : ComparisonsKt.compareValues(Integer.valueOf(((Iap2Parameter) t2).getPayload().length), Integer.valueOf(((Iap2Parameter) t).getPayload().length));
            }
        }), new Function1() { // from class: com.carlink.cast.Iap2HidDispatcher$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Iap2HidDispatcher.start$lambda$2((Iap2Parameter) obj);
            }
        }), new Function1() { // from class: com.carlink.cast.Iap2HidDispatcher$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(Iap2HidDispatcher.start$lambda$3((byte[]) obj));
            }
        }), new Function1() { // from class: com.carlink.cast.Iap2HidDispatcher$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Iap2HidDispatcher.start$lambda$7(this.f$0, (byte[]) obj);
            }
        }));
        if ((descriptor == null || (bArrOptionalBytes = descriptor.getBytes()) == null) && (bArrOptionalBytes = body.optionalBytes(4)) == null) {
            Iterator it = body.list().iterator();
            if (it.hasNext()) {
                next = it.next();
                if (it.hasNext()) {
                    int length = ((Iap2Parameter) next).getPayload().length;
                    do {
                        Object next2 = it.next();
                        int length2 = ((Iap2Parameter) next2).getPayload().length;
                        if (length < length2) {
                            next = next2;
                            length = length2;
                        }
                    } while (it.hasNext());
                }
            } else {
                next = null;
            }
            Iap2Parameter iap2Parameter = (Iap2Parameter) next;
            bArrOptionalBytes = iap2Parameter != null ? iap2Parameter.getPayload() : null;
            if (bArrOptionalBytes == null) {
                bArrOptionalBytes = new byte[0];
            }
        }
        byte[] descriptorBytes = bArrOptionalBytes;
        HidKeys keys = descriptor != null ? descriptor.getKeys() : null;
        HidInput touch = descriptor != null ? descriptor.getTouch() : null;
        ConcurrentHashMap<Integer, Device> concurrentHashMap = this.devices;
        Integer numValueOf = Integer.valueOf(id);
        byte[] bArrCopyOf = Arrays.copyOf(descriptorBytes, descriptorBytes.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        concurrentHashMap.put(numValueOf, new Device(id, width, height, bArrCopyOf, keys, touch));
        Pair<Integer, Integer> touchExtent = touch != null ? touch.getTouchExtent() : null;
        Function1<String, Unit> function1 = this.log;
        Object obj = width == null ? "?" : width;
        Object obj2 = height != null ? height : "?";
        int length3 = descriptorBytes.length;
        boolean z = keys != null;
        boolean z2 = touch != null;
        if (touchExtent != null) {
            Pair<Integer, Integer> pair = touchExtent;
            str = " extent=" + pair.getFirst() + "×" + pair.getSecond();
            if (str == null) {
            }
            function1.invoke("iAP2 HID 0x6800：device=" + id + " " + obj + "×" + obj2 + " descriptor=" + length3 + "B keys=" + z + " touch=" + z2 + str + (descriptor == null ? "（描述符待重试）" : ""));
            Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
            Iap2BodyBuilder iap2BodyBuilderOpaque = Iap2BodyBuilder.INSTANCE.opaque(26625);
            iap2BodyBuilderOpaque.u16(0, 0);
            iap2BodyBuilderOpaque.bytes(1, new byte[]{0});
            send.invoke(iap2BodyBuilderOpaque.build());
            return true;
        }
        str = "";
        function1.invoke("iAP2 HID 0x6800：device=" + id + " " + obj + "×" + obj2 + " descriptor=" + length3 + "B keys=" + z + " touch=" + z2 + str + (descriptor == null ? "（描述符待重试）" : ""));
        Iap2Messages iap2Messages2 = Iap2Messages.INSTANCE;
        Iap2BodyBuilder iap2BodyBuilderOpaque2 = Iap2BodyBuilder.INSTANCE.opaque(26625);
        iap2BodyBuilderOpaque2.u16(0, 0);
        iap2BodyBuilderOpaque2.bytes(1, new byte[]{0});
        send.invoke(iap2BodyBuilderOpaque2.build());
        return true;
    }

    static final byte[] start$lambda$2(Iap2Parameter it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getPayload();
    }

    static final boolean start$lambda$3(byte[] it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return !(it.length == 0);
    }

    static final DescriptorCandidate start$lambda$7(Iap2HidDispatcher this$0, byte[] candidate) {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        Intrinsics.checkNotNullParameter(candidate, "candidate");
        try {
            Result.Companion companion = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(new HidKeys(candidate));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = null;
        }
        HidKeys keys = (HidKeys) objM159constructorimpl;
        try {
            Result.Companion companion3 = Result.INSTANCE;
            HidInput hidInput = new HidInput(candidate);
            if (!hidInput.isTouch()) {
                hidInput = null;
            }
            objM159constructorimpl2 = Result.m159constructorimpl(hidInput);
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl2)) {
            objM159constructorimpl2 = null;
        }
        HidInput touch = (HidInput) objM159constructorimpl2;
        if (keys == null && touch == null) {
            return null;
        }
        return new DescriptorCandidate(candidate, keys, touch);
    }

    private final boolean report(Iap2Frame frame) throws Iap2ProtocolException {
        Object objM159constructorimpl;
        Object next;
        Iap2BodyReader body = Iap2BodyReader.INSTANCE.of(frame);
        Integer numOptionalU16 = body.optionalU16(0);
        int id = numOptionalU16 != null ? numOptionalU16.intValue() : 0;
        Iterable list = body.list();
        Collection arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Object next2 = it.next();
            if (!(((Iap2Parameter) next2).getPayload().length == 0)) {
                arrayList.add(next2);
            }
        }
        List nonEmpty = (List) arrayList;
        byte[] bArrOptionalBytes = body.optionalBytes(1);
        if (bArrOptionalBytes == null) {
            Iterator it2 = SequencesKt.filter(CollectionsKt.asSequence(body.list()), new Function1() { // from class: com.carlink.cast.Iap2HidDispatcher$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Boolean.valueOf(Iap2HidDispatcher.report$lambda$12((Iap2Parameter) obj));
                }
            }).iterator();
            if (it2.hasNext()) {
                next = it2.next();
                if (it2.hasNext()) {
                    int length = ((Iap2Parameter) next).getPayload().length;
                    do {
                        Object next3 = it2.next();
                        int length2 = ((Iap2Parameter) next3).getPayload().length;
                        if (length < length2) {
                            next = next3;
                            length = length2;
                        }
                    } while (it2.hasNext());
                }
            } else {
                next = null;
            }
            Iap2Parameter iap2Parameter = (Iap2Parameter) next;
            bArrOptionalBytes = iap2Parameter != null ? iap2Parameter.getPayload() : null;
            if (bArrOptionalBytes == null) {
                Iap2Parameter iap2Parameter2 = (Iap2Parameter) CollectionsKt.lastOrNull(nonEmpty);
                bArrOptionalBytes = iap2Parameter2 != null ? iap2Parameter2.getPayload() : null;
                if (bArrOptionalBytes == null) {
                    bArrOptionalBytes = new byte[0];
                }
            }
        }
        byte[] report = bArrOptionalBytes;
        Device device = this.devices.get(Integer.valueOf(id));
        if (device == null) {
            Collection<Device> collectionValues = this.devices.values();
            Intrinsics.checkNotNullExpressionValue(collectionValues, "<get-values>(...)");
            device = (Device) CollectionsKt.singleOrNull(collectionValues);
        }
        Device device2 = device;
        if (device2 == null) {
            this.log.invoke("iAP2 HID 0x6802：未知 device=" + id + " report=" + report.length + "B " + ArraysKt.joinToString$default(report, (CharSequence) " ", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, new Function1() { // from class: com.carlink.cast.Iap2HidDispatcher$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return Iap2HidDispatcher.report$lambda$14(((Byte) obj).byteValue());
                }
            }, 30, (Object) null));
            return true;
        }
        if (device2.getKeys() != null) {
            try {
                Result.Companion companion = Result.INSTANCE;
                Iap2HidDispatcher iap2HidDispatcher = this;
                objM159constructorimpl = Result.m159constructorimpl(device2.getKeys().report(report));
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            if (Result.m165isFailureimpl(objM159constructorimpl)) {
                objM159constructorimpl = null;
            }
            HidKeyReport parsed = (HidKeyReport) objM159constructorimpl;
            if (parsed == null) {
                this.log.invoke("iAP2 HID 按键报告无法解析：device=" + id + " " + report.length + "B");
            } else {
                Dispatch.INSTANCE.input(MapsKt.mapOf(TuplesKt.to("type", "keySamples"), TuplesKt.to("report", parsed)), this.source, this.display);
            }
        } else if (device2.getTouch() != null) {
            List<TouchSample> list2 = device2.getTouch().touches(report);
            if (list2.isEmpty()) {
                this.log.invoke("iAP2 HID 触摸报告为空：device=" + id + " " + report.length + "B");
            } else {
                Dispatch.INSTANCE.input(MapsKt.mapOf(TuplesKt.to("type", "touchSamples"), TuplesKt.to("samples", list2)), this.source, this.display);
            }
        } else {
            this.log.invoke("iAP2 HID：device=" + id + " 没有可用解析器");
        }
        return true;
    }

    static final boolean report$lambda$12(Iap2Parameter it) {
        Intrinsics.checkNotNullParameter(it, "it");
        if (it.getId() != 0) {
            return !(it.getPayload().length == 0);
        }
        return false;
    }

    static final CharSequence report$lambda$14(byte it) {
        String str = String.format("%02X", Arrays.copyOf(new Object[]{Byte.valueOf(it)}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    private final boolean stop(Iap2Frame frame) throws Iap2ProtocolException {
        Integer id = Iap2BodyReader.INSTANCE.of(frame).optionalU16(0);
        ConcurrentHashMap<Integer, Device> concurrentHashMap = this.devices;
        if (id == null) {
            concurrentHashMap.clear();
            this.log.invoke("iAP2 HID 0x6803：停止全部设备");
            return true;
        }
        concurrentHashMap.remove(id);
        this.log.invoke("iAP2 HID 0x6803：停止 device=" + id);
        return true;
    }

    public final void reset() {
        this.devices.clear();
        this.display = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: Iap2HidDispatcher.kt */
    @Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B#\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u0010\u0012\u001a\u0004\u0018\u00010\u0007HÆ\u0003J+\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007HÆ\u0001J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001J\t\u0010\u0019\u001a\u00020\u001aHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u001b"}, d2 = {"Lcom/carlink/cast/Iap2HidDispatcher$DescriptorCandidate;", "", "bytes", "", "keys", "Lcom/carplay/sender/HidKeys;", CastSettings.KEY_TOUCH, "Lcom/carplay/sender/HidInput;", "<init>", "([BLcom/carplay/sender/HidKeys;Lcom/carplay/sender/HidInput;)V", "getBytes", "()[B", "getKeys", "()Lcom/carplay/sender/HidKeys;", "getTouch", "()Lcom/carplay/sender/HidInput;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    static final /* data */ class DescriptorCandidate {
        private final byte[] bytes;
        private final HidKeys keys;
        private final HidInput touch;

        public static /* synthetic */ DescriptorCandidate copy$default(DescriptorCandidate descriptorCandidate, byte[] bArr, HidKeys hidKeys, HidInput hidInput, int i, Object obj) {
            if ((i & 1) != 0) {
                bArr = descriptorCandidate.bytes;
            }
            if ((i & 2) != 0) {
                hidKeys = descriptorCandidate.keys;
            }
            if ((i & 4) != 0) {
                hidInput = descriptorCandidate.touch;
            }
            return descriptorCandidate.copy(bArr, hidKeys, hidInput);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final byte[] getBytes() {
            return this.bytes;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final HidKeys getKeys() {
            return this.keys;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final HidInput getTouch() {
            return this.touch;
        }

        public final DescriptorCandidate copy(byte[] bytes, HidKeys keys, HidInput touch) {
            Intrinsics.checkNotNullParameter(bytes, "bytes");
            return new DescriptorCandidate(bytes, keys, touch);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof DescriptorCandidate)) {
                return false;
            }
            DescriptorCandidate descriptorCandidate = (DescriptorCandidate) other;
            return Intrinsics.areEqual(this.bytes, descriptorCandidate.bytes) && Intrinsics.areEqual(this.keys, descriptorCandidate.keys) && Intrinsics.areEqual(this.touch, descriptorCandidate.touch);
        }

        public int hashCode() {
            return (((Arrays.hashCode(this.bytes) * 31) + (this.keys == null ? 0 : this.keys.hashCode())) * 31) + (this.touch != null ? this.touch.hashCode() : 0);
        }

        public String toString() {
            return "DescriptorCandidate(bytes=" + Arrays.toString(this.bytes) + ", keys=" + this.keys + ", touch=" + this.touch + ")";
        }

        public DescriptorCandidate(byte[] bytes, HidKeys keys, HidInput touch) {
            Intrinsics.checkNotNullParameter(bytes, "bytes");
            this.bytes = bytes;
            this.keys = keys;
            this.touch = touch;
        }

        public final byte[] getBytes() {
            return this.bytes;
        }

        public final HidKeys getKeys() {
            return this.keys;
        }

        public final HidInput getTouch() {
            return this.touch;
        }
    }

    /* JADX INFO: compiled from: Iap2HidDispatcher.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lcom/carlink/cast/Iap2HidDispatcher$Companion;", "", "<init>", "()V", "HID_REPORT_DATA_PARAM", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
