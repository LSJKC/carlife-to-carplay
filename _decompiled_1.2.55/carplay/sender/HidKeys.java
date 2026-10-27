package com.carplay.sender;

import com.carplay.airplay.CarPlayDisplayScale;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;
import org.bouncycastle.asn1.BERTags;

/* JADX INFO: compiled from: HidKeys.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001:\u0001\u0013B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0003R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000b\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\fR\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lcom/carplay/sender/HidKeys;", "", "descriptor", "", "<init>", "([B)V", "fields", "", "Lcom/carplay/sender/HidKeys$Field;", "bits", "", "reportId", "Ljava/lang/Integer;", "active", "", "Lcom/carplay/sender/HidUsage;", "report", "Lcom/carplay/sender/HidKeyReport;", "bytes", "Field", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class HidKeys {
    private Set<HidUsage> active;
    private int bits;
    private final List<Field> fields;
    private Integer reportId;

    /* JADX INFO: compiled from: HidKeys.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007¢\u0006\u0004\b\t\u0010\nJ\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0003HÆ\u0003J\u000f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\b0\u0007HÆ\u0003J7\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007HÆ\u0001J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0019\u001a\u00020\u0003HÖ\u0001J\t\u0010\u001a\u001a\u00020\u001bHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\fR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u001c"}, d2 = {"Lcom/carplay/sender/HidKeys$Field;", "", "offset", "", "size", "flags", "usages", "", "Lcom/carplay/sender/HidUsage;", "<init>", "(IIILjava/util/List;)V", "getOffset", "()I", "getSize", "getFlags", "getUsages", "()Ljava/util/List;", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class Field {
        private final int flags;
        private final int offset;
        private final int size;
        private final List<HidUsage> usages;

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Field copy$default(Field field, int i, int i2, int i3, List list, int i4, Object obj) {
            if ((i4 & 1) != 0) {
                i = field.offset;
            }
            if ((i4 & 2) != 0) {
                i2 = field.size;
            }
            if ((i4 & 4) != 0) {
                i3 = field.flags;
            }
            if ((i4 & 8) != 0) {
                list = field.usages;
            }
            return field.copy(i, i2, i3, list);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final int getOffset() {
            return this.offset;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final int getSize() {
            return this.size;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final int getFlags() {
            return this.flags;
        }

        public final List<HidUsage> component4() {
            return this.usages;
        }

        public final Field copy(int offset, int size, int flags, List<HidUsage> usages) {
            Intrinsics.checkNotNullParameter(usages, "usages");
            return new Field(offset, size, flags, usages);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Field)) {
                return false;
            }
            Field field = (Field) other;
            return this.offset == field.offset && this.size == field.size && this.flags == field.flags && Intrinsics.areEqual(this.usages, field.usages);
        }

        public int hashCode() {
            return (((((Integer.hashCode(this.offset) * 31) + Integer.hashCode(this.size)) * 31) + Integer.hashCode(this.flags)) * 31) + this.usages.hashCode();
        }

        public String toString() {
            return "Field(offset=" + this.offset + ", size=" + this.size + ", flags=" + this.flags + ", usages=" + this.usages + ")";
        }

        public Field(int offset, int size, int flags, List<HidUsage> usages) {
            Intrinsics.checkNotNullParameter(usages, "usages");
            this.offset = offset;
            this.size = size;
            this.flags = flags;
            this.usages = usages;
        }

        public final int getFlags() {
            return this.flags;
        }

        public final int getOffset() {
            return this.offset;
        }

        public final int getSize() {
            return this.size;
        }

        public final List<HidUsage> getUsages() {
            return this.usages;
        }
    }

    /* JADX WARN: Code duplicated, block: B:70:0x00f1  */
    public HidKeys(byte[] descriptor) {
        int $i$f$any;
        int i;
        int i2;
        int i3;
        List listListOfNotNull;
        Integer num;
        byte[] descriptor2 = descriptor;
        Intrinsics.checkNotNullParameter(descriptor2, "descriptor");
        this.fields = new ArrayList();
        this.active = SetsKt.emptySet();
        int length = descriptor2.length;
        int i4 = 1;
        if (!(1 <= length && length < 8193)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        int offset = 0;
        int page = 0;
        int size = 0;
        int count = 0;
        List usages = new ArrayList();
        while (true) {
            int i5 = 3;
            if (offset >= descriptor2.length) {
                int i6 = i4;
                Iterable $this$any$iv = this.fields;
                if (($this$any$iv instanceof Collection) && ((Collection) $this$any$iv).isEmpty()) {
                    $i$f$any = 0;
                } else {
                    for (Object element$iv : $this$any$iv) {
                        Field field = (Field) element$iv;
                        Iterable $this$any$iv2 = field.getUsages();
                        if (($this$any$iv2 instanceof Collection) && ((Collection) $this$any$iv2).isEmpty()) {
                            i = 0;
                        } else {
                            Iterator it = $this$any$iv2.iterator();
                            while (true) {
                                if (it.hasNext()) {
                                    Object element$iv2 = it.next();
                                    HidUsage it2 = (HidUsage) element$iv2;
                                    Integer[] numArr = new Integer[i5];
                                    numArr[0] = 9;
                                    numArr[i6] = 11;
                                    numArr[2] = 12;
                                    if (SetsKt.setOf((Object[]) numArr).contains(Integer.valueOf(it2.getPage()))) {
                                        i = i6;
                                    } else {
                                        i5 = 3;
                                    }
                                } else {
                                    i = 0;
                                }
                            }
                        }
                        if (i != 0) {
                            $i$f$any = i6;
                        } else {
                            i5 = 3;
                        }
                    }
                    $i$f$any = 0;
                }
                if ($i$f$any == 0) {
                    throw new IllegalArgumentException("不是支持的按键描述符".toString());
                }
                return;
            }
            offset++;
            int prefix = descriptor2[offset] & 255;
            if ((prefix != 254 ? i4 : 0) == 0) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            int bytes = (prefix & 3) == 3 ? 4 : prefix & 3;
            if ((offset + bytes <= descriptor2.length ? i4 : 0) == 0) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            int value = 0;
            int i7 = 0;
            while (i7 < bytes) {
                int it3 = i7;
                value |= (descriptor2[offset] & 255) << (it3 * 8);
                i7++;
                offset++;
            }
            switch (prefix & 252) {
                case 4:
                    page = value;
                    descriptor2 = descriptor;
                    continue;
                case 8:
                    i2 = i4;
                    usages.add(new HidUsage((value >>> 16) == 0 ? page : value >>> 16, 65535 & value));
                    break;
                case 116:
                    size = value;
                    descriptor2 = descriptor;
                    continue;
                case 128:
                    if (((i4 > size || size >= 33) ? 0 : i4) == 0) {
                        i3 = 0;
                    } else if (((i4 > count || count >= 65) ? 0 : i4) == 0 || this.bits + (size * count) > 4096) {
                        i3 = 0;
                    } else {
                        i3 = i4;
                    }
                    if (i3 == 0) {
                        throw new IllegalArgumentException("Failed requirement.".toString());
                    }
                    int i8 = 0;
                    while (i8 < count) {
                        int slot = i8;
                        if ((value & 1) == 0) {
                            List<Field> list = this.fields;
                            int i9 = this.bits;
                            if ((value & 2) == 0) {
                                listListOfNotNull = CollectionsKt.toList(usages);
                            } else {
                                HidUsage hidUsage = (HidUsage) CollectionsKt.getOrNull(usages, slot);
                                listListOfNotNull = CollectionsKt.listOfNotNull(hidUsage == null ? (HidUsage) CollectionsKt.lastOrNull(usages) : hidUsage);
                            }
                            list.add(new Field(i9, size, value, listListOfNotNull));
                        }
                        this.bits += size;
                        i8++;
                        i4 = i4;
                        prefix = prefix;
                    }
                    i2 = i4;
                    usages.clear();
                    break;
                    break;
                case 132:
                    if ((bytes == i4 ? i4 : 0) == 0) {
                        throw new IllegalArgumentException("Report ID 必须是 1 字节".toString());
                    }
                    int id = value & 255;
                    if (((this.reportId == null || ((num = this.reportId) != null && num.intValue() == id)) ? i4 : 0) == 0) {
                        throw new IllegalArgumentException("不支持多个 Report ID 的按键设备".toString());
                    }
                    this.reportId = Integer.valueOf(id);
                    i2 = i4;
                    break;
                    break;
                case 148:
                    count = value;
                    continue;
                case CarPlayDisplayScale.MAX_PERCENT /* 160 */:
                case BERTags.PRIVATE /* 192 */:
                    usages.clear();
                    i2 = i4;
                    break;
                default:
                    i2 = i4;
                    break;
            }
            descriptor2 = descriptor;
            i4 = i2;
        }
    }

    public final synchronized HidKeyReport report(byte[] bytes) {
        boolean z;
        byte[] payload;
        LinkedHashSet held;
        int wheel;
        List changes;
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        if (this.reportId == null) {
            payload = bytes;
        } else {
            if (!(bytes.length == 0)) {
                int i = bytes[0] & UByte.MAX_VALUE;
                Integer num = this.reportId;
                z = num != null && i == num.intValue();
            }
            if (!z) {
                throw new IllegalArgumentException("HID Report ID 不匹配".toString());
            }
            payload = ArraysKt.copyOfRange(bytes, 1, bytes.length);
        }
        if (!(payload.length * 8 >= this.bits && payload.length <= 512)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        held = new LinkedHashSet();
        wheel = 0;
        for (Field field : this.fields) {
            int value = 0;
            int size = field.getSize();
            for (int i2 = 0; i2 < size; i2++) {
                int bit = i2;
                int position = field.getOffset() + bit;
                value |= ((payload[position / 8] >>> (position % 8)) & 1) << bit;
            }
            if ((field.getFlags() & 2) != 0) {
                HidUsage usage = (HidUsage) CollectionsKt.firstOrNull((List) field.getUsages());
                if (usage != null) {
                    if (usage.getPage() == 1 && usage.getUsage() == 56 && (field.getFlags() & 4) != 0) {
                        wheel = field.getSize() == 8 ? (byte) value : value;
                        if (!(-127 <= wheel && wheel < 128)) {
                            throw new IllegalArgumentException("Failed requirement.".toString());
                        }
                    } else if (SetsKt.setOf((Object[]) new Integer[]{9, 11, 12}).contains(Integer.valueOf(usage.getPage())) && value != 0) {
                        held.add(usage);
                    }
                }
            } else if (value != 0) {
                LinkedHashSet linkedHashSet = held;
                Object orNull = CollectionsKt.getOrNull(field.getUsages(), value);
                if (orNull == null) {
                    throw new IllegalArgumentException("未知按键选择值".toString());
                }
                linkedHashSet.add(orNull);
            } else {
                continue;
            }
        }
        Iterable $this$map$iv = SetsKt.minus((Set) this.active, (Iterable) held);
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (Object item$iv$iv : $this$map$iv) {
            HidUsage it = (HidUsage) item$iv$iv;
            destination$iv$iv.add(new HidKeyChange(it, false));
        }
        ArrayList arrayList = (List) destination$iv$iv;
        Iterable $this$map$iv2 = SetsKt.minus((Set) held, (Iterable) this.active);
        Collection destination$iv$iv2 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv2, 10));
        for (Object item$iv$iv2 : $this$map$iv2) {
            HidUsage it2 = (HidUsage) item$iv$iv2;
            destination$iv$iv2.add(new HidKeyChange(it2, true));
        }
        changes = CollectionsKt.plus((Collection) arrayList, destination$iv$iv2);
        this.active = CollectionsKt.toSet(held);
        return new HidKeyReport(held, changes, wheel);
    }
}
