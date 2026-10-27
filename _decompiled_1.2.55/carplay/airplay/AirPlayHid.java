package com.carplay.airplay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TuplesKt;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.CharsKt;
import kotlinx.coroutines.scheduling.WorkQueueKt;
import org.bouncycastle.asn1.BERTags;

/* JADX INFO: compiled from: AirPlayHid.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0015\n\u0002\b\u0005\n\u0002\u0010\u0005\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J,\u0010\u000b\u001a\u0010\u0012\u0004\u0012\u00020\r\u0012\u0006\u0012\u0004\u0018\u00010\u00010\f2\u0006\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\rJ\u001c\u0010\u0011\u001a\u0010\u0012\u0004\u0012\u00020\r\u0012\u0006\u0012\u0004\u0018\u00010\u00010\f2\u0006\u0010\u0010\u001a\u00020\rJ\u001c\u0010\u0012\u001a\u0010\u0012\u0004\u0012\u00020\r\u0012\u0006\u0012\u0004\u0018\u00010\u00010\f2\u0006\u0010\u0010\u001a\u00020\rJ\u001c\u0010\u0013\u001a\u0010\u0012\u0004\u0012\u00020\r\u0012\u0006\u0012\u0004\u0018\u00010\u00010\f2\u0006\u0010\u0010\u001a\u00020\rJ\u0014\u0010\u0014\u001a\u00020\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017J\u000e\u0010\u0019\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\u001bJ\u000e\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u0005J\u000e\u0010\u001e\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u0005J6\u0010\u001f\u001a\u0010\u0012\u0004\u0012\u00020\r\u0012\u0006\u0012\u0004\u0018\u00010\u00010\f2\u0006\u0010 \u001a\u00020\u00052\u0006\u0010!\u001a\u00020\r2\u0006\u0010\"\u001a\u00020\u00152\u0006\u0010\u0010\u001a\u00020\rH\u0002J\u0018\u0010#\u001a\u00020\u00152\u0006\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0002J\u0018\u0010$\u001a\u00020%2\u0006\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0002J\u0014\u0010\"\u001a\u00020\u00152\n\u0010)\u001a\u00020%\"\u00020\u0005H\u0002J\u0010\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020\u0005H\u0002J \u0010-\u001a\u00020.2\u0006\u0010/\u001a\u00020\u00152\u0006\u00100\u001a\u00020\u00052\u0006\u0010,\u001a\u00020\u0005H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u0015X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u0015X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020\u0015X\u0082\u0004¢\u0006\u0002\n\u0000¨\u00061"}, d2 = {"Lcom/carplay/airplay/AirPlayHid;", "", "<init>", "()V", "TOUCH_HID_UID", "", "KNOB_HID_UID", "MEDIA_HID_UID", "TELEPHONY_HID_UID", "TOUCH_CONTACTS", "BYTES_PER_FINGER", "touchHidDevice", "", "", "xMax", "yMax", "displayUuid", "knobHidDevice", "mediaHidDevice", "telephonyHidDevice", "touchReport", "", "contacts", "", "Lcom/carplay/airplay/AirPlayContact;", "knobReport", "state", "Lcom/carplay/airplay/AirPlayKnobState;", "mediaReport", "index", "telephonyReport", "hidDeviceEntry", "uid", "name", "descriptor", "multitouchDescriptor", "fingerCollection", "", "knobDescriptor", "mediaDescriptor", "telephonyDescriptor", "bytes", "clampAxis", "", "value", "writeU16Le", "", "target", "offset", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AirPlayHid {
    private static final int BYTES_PER_FINGER = 6;
    public static final int KNOB_HID_UID = 707406379;
    public static final int MEDIA_HID_UID = 707406380;
    public static final int TELEPHONY_HID_UID = 707406381;
    private static final int TOUCH_CONTACTS = 2;
    public static final int TOUCH_HID_UID = 707406378;
    public static final AirPlayHid INSTANCE = new AirPlayHid();
    private static final byte[] knobDescriptor = INSTANCE.descriptor(5, 1, 9, 8, 161, 1, 5, 9, 9, 1, 21, 0, 37, 1, 117, 1, 149, 1, 129, 2, 5, 12, 10, 35, 2, 10, 36, 2, 149, 2, 129, 2, 149, 5, 129, 1, 5, 1, 9, 1, 161, 0, 9, 48, 9, 49, 21, 129, 37, WorkQueueKt.MASK, 117, 8, 149, 2, 129, 2, BERTags.PRIVATE, 9, 56, 21, 129, 37, WorkQueueKt.MASK, 117, 8, 149, 1, 129, 6, BERTags.PRIVATE);
    private static final byte[] mediaDescriptor = INSTANCE.descriptor(5, 12, 9, 1, 161, 1, 21, 0, 37, 6, 5, 12, 10, 0, 0, 10, 176, 0, 10, 177, 0, 10, 205, 0, 10, 181, 0, 10, 182, 0, 10, 158, 2, 117, 8, 149, 1, 129, 0, BERTags.PRIVATE);
    private static final byte[] telephonyDescriptor = INSTANCE.descriptor(5, 11, 9, 7, 161, 1, 21, 0, 37, 17, 5, 11, 9, 0, 9, 32, 9, 33, 9, 38, 9, 47, 9, 176, 9, 177, 9, 178, 9, 179, 9, 180, 9, 181, 9, 182, 9, 183, 9, 184, 9, 185, 9, 186, 9, 187, 5, 7, 9, 42, 117, 8, 149, 1, 129, 0, BERTags.PRIVATE);

    private AirPlayHid() {
    }

    public final Map<String, Object> touchHidDevice(int xMax, int yMax, String displayUuid) {
        Intrinsics.checkNotNullParameter(displayUuid, "displayUuid");
        return hidDeviceEntry(TOUCH_HID_UID, "xcertplay Touchscreen", multitouchDescriptor(xMax, yMax), displayUuid);
    }

    public final Map<String, Object> knobHidDevice(String displayUuid) {
        Intrinsics.checkNotNullParameter(displayUuid, "displayUuid");
        return hidDeviceEntry(KNOB_HID_UID, "xcertplay Knob", knobDescriptor, displayUuid);
    }

    public final Map<String, Object> mediaHidDevice(String displayUuid) {
        Intrinsics.checkNotNullParameter(displayUuid, "displayUuid");
        return hidDeviceEntry(MEDIA_HID_UID, "xcertplay Media", mediaDescriptor, displayUuid);
    }

    public final Map<String, Object> telephonyHidDevice(String displayUuid) {
        Intrinsics.checkNotNullParameter(displayUuid, "displayUuid");
        return hidDeviceEntry(TELEPHONY_HID_UID, "xcertplay Telephony", telephonyDescriptor, displayUuid);
    }

    public final byte[] touchReport(List<AirPlayContact> contacts) {
        Intrinsics.checkNotNullParameter(contacts, "contacts");
        byte[] report = new byte[12];
        for (int slot = 0; slot < 2; slot++) {
            int offset = slot * 6;
            report[offset] = (byte) slot;
            AirPlayContact contact = (AirPlayContact) CollectionsKt.getOrNull(contacts, slot);
            if (contact != null) {
                report[offset + 1] = contact.getDown();
                writeU16Le(report, offset + 2, (int) Math.round(RangesKt.coerceAtLeast(contact.getX(), 0.0d)));
                writeU16Le(report, offset + 4, (int) Math.round(RangesKt.coerceAtLeast(contact.getY(), 0.0d)));
            }
        }
        return report;
    }

    public final byte[] knobReport(AirPlayKnobState state) {
        Intrinsics.checkNotNullParameter(state, "state");
        byte[] bArr = new byte[4];
        bArr[0] = (byte) ((state.getBack() ? 4 : 0) | (state.getSelect() ? 1 : 0) | (state.getHome() ? 2 : 0));
        bArr[1] = clampAxis(state.getX());
        bArr[2] = clampAxis(state.getY());
        bArr[3] = clampAxis(state.getWheel());
        return bArr;
    }

    public final byte[] mediaReport(int index) {
        return new byte[]{(byte) index};
    }

    public final byte[] telephonyReport(int index) {
        return new byte[]{(byte) index};
    }

    private final Map<String, Object> hidDeviceEntry(int uid, String name, byte[] descriptor, String displayUuid) {
        String string = Integer.toString(uid, CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return MapsKt.linkedMapOf(TuplesKt.to("hidProductID", 1L), TuplesKt.to("hidVendorID", 2L), TuplesKt.to("hidCountryCode", 0L), TuplesKt.to("uuid", string), TuplesKt.to("name", name), TuplesKt.to("displayUUID", displayUuid), TuplesKt.to("hidDescriptor", descriptor));
    }

    private final byte[] multitouchDescriptor(int xMax, int yMax) {
        ArrayList bytes = new ArrayList();
        bytes.addAll(ArraysKt.toList(new int[]{5, 13, 9, 4, 161, 1}));
        for (int i = 0; i < 2; i++) {
            bytes.addAll(ArraysKt.toList(INSTANCE.fingerCollection(xMax, yMax)));
        }
        bytes.add(Integer.valueOf(BERTags.PRIVATE));
        ArrayList $this$map$iv = bytes;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (Object item$iv$iv : $this$map$iv) {
            int it = ((Number) item$iv$iv).intValue();
            destination$iv$iv.add(Byte.valueOf((byte) it));
        }
        return CollectionsKt.toByteArray((List) destination$iv$iv);
    }

    private final int[] fingerCollection(int xMax, int yMax) {
        return new int[]{5, 13, 9, 34, 161, 2, 9, 56, 117, 8, 149, 1, 129, 2, 21, 0, 37, 1, 9, 51, 117, 1, 149, 1, 129, 2, 149, 7, 129, 3, 5, 1, 38, xMax & 255, (xMax >> 8) & 255, 9, 48, 117, 16, 149, 1, 129, 2, 38, yMax & 255, (yMax >> 8) & 255, 9, 49, 129, 2, BERTags.PRIVATE};
    }

    private final byte[] descriptor(int... bytes) {
        int length = bytes.length;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            bArr[i] = (byte) bytes[i];
        }
        return bArr;
    }

    private final byte clampAxis(int value) {
        return (byte) RangesKt.coerceIn(value, -127, WorkQueueKt.MASK);
    }

    private final void writeU16Le(byte[] target, int offset, int value) {
        target[offset] = (byte) (value & 255);
        target[offset + 1] = (byte) ((value >>> 8) & 255);
    }
}
