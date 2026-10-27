package com.carplay.sender;

import com.carplay.airplay.CarPlayDisplayScale;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.UByte;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.bouncycastle.asn1.BERTags;

/* JADX INFO: compiled from: HidInput.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\u0006\u0010\u0013\u001a\u00020\u0003J\u0018\u0010\u0018\u001a\u00020\n2\u0006\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\bH\u0002R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u000b\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0004\n\u0002\u0010\fR\u0011\u0010\r\u001a\u00020\u000e8F¢\u0006\u0006\u001a\u0004\b\r\u0010\u000fR\u001f\u0010\u0014\u001a\u0010\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n\u0018\u00010\u00158F¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017¨\u0006\u001b"}, d2 = {"Lcom/carplay/sender/HidInput;", "", "descriptor", "", "<init>", "([B)V", "fields", "", "Lcom/carplay/sender/HidField;", "bits", "", "reportId", "Ljava/lang/Integer;", "isTouch", "", "()Z", "touches", "", "Lcom/carplay/sender/TouchSample;", "report", "touchExtent", "Lkotlin/Pair;", "getTouchExtent", "()Lkotlin/Pair;", "read", "bytes", "field", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class HidInput {
    private int bits;
    private final List<HidField> fields;
    private Integer reportId;

    /* JADX WARN: Code duplicated, block: B:70:0x00e2  */
    public HidInput(byte[] descriptor) {
        int i;
        Object objValueOf;
        Integer num;
        Intrinsics.checkNotNullParameter(descriptor, "descriptor");
        this.fields = new ArrayList();
        int length = descriptor.length;
        int i2 = 1;
        if (!(1 <= length && length < 8193)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        int cursor = 0;
        int reportCount = 0;
        List usages = new ArrayList();
        int page = 0;
        int reportSize = 0;
        int maximum = 0;
        while (cursor < page) {
            cursor++;
            int prefix = descriptor[cursor] & 255;
            if ((prefix != 254 ? i2 : 0) == 0) {
                throw new IllegalArgumentException("不支持扩展 HID 项".toString());
            }
            int size = (prefix & 3) == 3 ? 4 : prefix & 3;
            if ((cursor + size <= descriptor.length ? i2 : 0) == 0) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            int value = 0;
            int i3 = 0;
            while (i3 < size) {
                int it = i3;
                value |= (descriptor[cursor] & 255) << (it * 8);
                i3++;
                cursor++;
            }
            switch (prefix & 252) {
                case 4:
                    page = value;
                    i2 = 1;
                    continue;
                case 8:
                    usages.add(Integer.valueOf(value));
                    break;
                case 36:
                    maximum = value;
                    i2 = 1;
                    continue;
                case 116:
                    reportSize = value;
                    i2 = 1;
                    continue;
                case 128:
                    if (((i2 > reportSize || reportSize >= 33) ? 0 : i2) == 0) {
                        i = 0;
                    } else {
                        if (((i2 > reportCount || reportCount >= 65) ? 0 : i2) == 0 || this.bits + (reportSize * reportCount) > 4096) {
                            i = 0;
                        } else {
                            i = i2;
                        }
                    }
                    if (i == 0) {
                        throw new IllegalArgumentException("Failed requirement.".toString());
                    }
                    int i4 = 0;
                    while (i4 < reportCount) {
                        int slot = i4;
                        if ((value & 1) == 0) {
                            List<HidField> list = this.fields;
                            if (((slot < 0 || slot >= usages.size()) ? 0 : i2) != 0) {
                                objValueOf = usages.get(slot);
                            } else {
                                Integer num2 = (Integer) CollectionsKt.lastOrNull(usages);
                                objValueOf = Integer.valueOf(num2 != null ? num2.intValue() : 0);
                            }
                            list.add(new HidField(page, ((Number) objValueOf).intValue(), this.bits, reportSize, maximum));
                        }
                        this.bits += reportSize;
                        i4++;
                        i2 = 1;
                    }
                    usages.clear();
                    break;
                    break;
                case 132:
                    if ((size == i2 ? i2 : 0) == 0) {
                        throw new IllegalArgumentException("Report ID 必须是 1 字节".toString());
                    }
                    int id = value & 255;
                    if (((this.reportId == null || ((num = this.reportId) != null && num.intValue() == id)) ? i2 : 0) == 0) {
                        throw new IllegalArgumentException("不支持多个 Report ID 的输入设备".toString());
                    }
                    this.reportId = Integer.valueOf(id);
                    break;
                    break;
                case 148:
                    reportCount = value;
                    continue;
                case CarPlayDisplayScale.MAX_PERCENT /* 160 */:
                case BERTags.PRIVATE /* 192 */:
                    usages.clear();
                    break;
            }
            i2 = 1;
        }
    }

    public final boolean isTouch() {
        boolean z;
        boolean z2;
        Iterable $this$any$iv = this.fields;
        if (!($this$any$iv instanceof Collection) || !((Collection) $this$any$iv).isEmpty()) {
            Iterator it = $this$any$iv.iterator();
            while (true) {
                if (!it.hasNext()) {
                    z = false;
                    break;
                }
                Object element$iv = it.next();
                HidField it2 = (HidField) element$iv;
                if (it2.getPage() == 13 && it2.getUsage() == 51) {
                    z = true;
                    break;
                }
            }
        } else {
            z = false;
        }
        if (z) {
            Iterable $this$any$iv2 = this.fields;
            if (!($this$any$iv2 instanceof Collection) || !((Collection) $this$any$iv2).isEmpty()) {
                Iterator it3 = $this$any$iv2.iterator();
                while (true) {
                    if (!it3.hasNext()) {
                        z2 = false;
                        break;
                    }
                    Object element$iv2 = it3.next();
                    HidField it4 = (HidField) element$iv2;
                    if (it4.getPage() == 1 && it4.getUsage() == 48) {
                        z2 = true;
                        break;
                    }
                }
            } else {
                z2 = false;
            }
            if (z2) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final List<TouchSample> touches(byte[] report) {
        byte[] payload;
        HidInput hidInput = this;
        Intrinsics.checkNotNullParameter(report, "report");
        int i = 1;
        if ((report.length == 0) || report.length > 512) {
            return CollectionsKt.emptyList();
        }
        if (hidInput.reportId == null) {
            payload = report;
        } else {
            int i2 = report[0] & UByte.MAX_VALUE;
            Integer num = hidInput.reportId;
            if (num == null || i2 != num.intValue()) {
                return CollectionsKt.emptyList();
            }
            payload = ArraysKt.copyOfRange(report, 1, report.length);
        }
        int available = payload.length * 8;
        List result = new ArrayList();
        int xRaw = 0;
        int xMax = 1;
        boolean hasX = false;
        int id = 0;
        boolean z = 0;
        for (HidField field : hidInput.fields) {
            if (field.getOffset() + field.getSize() <= available) {
                int value = hidInput.read(payload, field);
                if (field.getPage() == 13 && field.getUsage() == 56) {
                    id = value;
                } else if (field.getPage() == 13 && field.getUsage() == 51) {
                    z = value != 0 ? i : 0;
                } else if (field.getPage() == i && field.getUsage() == 48) {
                    xRaw = value;
                    xMax = field.getMaximum() > 0 ? field.getMaximum() : i;
                    hasX = true;
                } else if (field.getPage() == i && field.getUsage() == 49 && hasX) {
                    int yMax = field.getMaximum() > 0 ? field.getMaximum() : i;
                    result.add(new TouchSample(id, xRaw / xMax, value / yMax, z, xMax, yMax));
                    hasX = false;
                    i = 1;
                    hidInput = this;
                }
            }
            xMax = xMax;
            i = 1;
            hidInput = this;
        }
        return result;
    }

    public final Pair<Integer, Integer> getTouchExtent() {
        Object element$iv;
        Object element$iv2;
        HidField it;
        HidField it2;
        Iterable $this$firstOrNull$iv = this.fields;
        Iterator it3 = $this$firstOrNull$iv.iterator();
        do {
            if (!it3.hasNext()) {
                element$iv = null;
                break;
            }
            element$iv = it3.next();
            it2 = (HidField) element$iv;
        } while (!(it2.getPage() == 1 && it2.getUsage() == 48));
        HidField hidField = (HidField) element$iv;
        if (hidField == null) {
            return null;
        }
        int width = hidField.getMaximum();
        Iterable $this$firstOrNull$iv2 = this.fields;
        Iterator it4 = $this$firstOrNull$iv2.iterator();
        do {
            if (!it4.hasNext()) {
                element$iv2 = null;
                break;
            }
            element$iv2 = it4.next();
            it = (HidField) element$iv2;
        } while (!(it.getPage() == 1 && it.getUsage() == 49));
        HidField hidField2 = (HidField) element$iv2;
        if (hidField2 == null) {
            return null;
        }
        int height = hidField2.getMaximum();
        if (width <= 0 || height <= 0) {
            return null;
        }
        return TuplesKt.to(Integer.valueOf(width), Integer.valueOf(height));
    }

    private final int read(byte[] bytes, HidField field) {
        int value = 0;
        int size = field.getSize();
        for (int i = 0; i < size; i++) {
            int bit = i;
            int index = field.getOffset() + bit;
            value |= ((bytes[index / 8] >>> (index % 8)) & 1) << bit;
        }
        return value;
    }
}
