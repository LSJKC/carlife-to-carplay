package com.carplay.iap2.transport;

import android.hardware.usb.UsbConfiguration;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.CharsKt;

/* JADX INFO: compiled from: IphoneCarPlayConfiguration.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u0012J\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0016\u001a\u00020\u0012J\u001c\u0010\u0019\u001a\u0010\u0012\u0004\u0012\u00020\u001b\u0012\u0004\u0012\u00020\u001b\u0018\u00010\u001a2\u0006\u0010\u001c\u001a\u00020\u0018J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u0016\u001a\u00020\u0012H\u0002J\u0010\u0010\u001f\u001a\u00020\u001e2\u0006\u0010\u0016\u001a\u00020\u0012H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000¨\u0006 "}, d2 = {"Lcom/carplay/iap2/transport/IphoneCarPlayConfiguration;", "", "<init>", "()V", "TAG", "", "USBMUX_CLASS", "", "USBMUX_SUBCLASS", "USBMUX_PROTOCOL", "APPLE_ETHERNET_CLASS", "APPLE_ETHERNET_SUBCLASS", "APPLE_ETHERNET_PROTOCOL", "NCM_CONTROL_CLASS", "NCM_CONTROL_SUBCLASS", "PREFERRED_USBMUX_OUT", "PREFERRED_USBMUX_IN", "find", "Landroid/hardware/usb/UsbConfiguration;", "device", "Landroid/hardware/usb/UsbDevice;", "describe", "configuration", "usbMuxInterface", "Landroid/hardware/usb/UsbInterface;", "usbMuxEndpoints", "Lkotlin/Pair;", "Landroid/hardware/usb/UsbEndpoint;", "usbInterface", "hasCdcNcm", "", "hasAppleEthernet", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class IphoneCarPlayConfiguration {
    private static final int APPLE_ETHERNET_CLASS = 255;
    private static final int APPLE_ETHERNET_PROTOCOL = 1;
    private static final int APPLE_ETHERNET_SUBCLASS = 253;
    public static final IphoneCarPlayConfiguration INSTANCE = new IphoneCarPlayConfiguration();
    private static final int NCM_CONTROL_CLASS = 2;
    private static final int NCM_CONTROL_SUBCLASS = 13;
    private static final int PREFERRED_USBMUX_IN = 133;
    private static final int PREFERRED_USBMUX_OUT = 4;
    public static final String TAG = "xcertplay-usb";
    private static final int USBMUX_CLASS = 255;
    private static final int USBMUX_PROTOCOL = 2;
    private static final int USBMUX_SUBCLASS = 254;

    private IphoneCarPlayConfiguration() {
    }

    public final UsbConfiguration find(UsbDevice device) {
        String strDescribe;
        Object element$iv;
        Object element$iv2;
        UsbConfiguration it;
        IphoneCarPlayConfiguration iphoneCarPlayConfiguration;
        UsbConfiguration it2;
        IphoneCarPlayConfiguration iphoneCarPlayConfiguration2;
        Intrinsics.checkNotNullParameter(device, "device");
        Iterable $this$map$iv = RangesKt.until(0, device.getConfigurationCount());
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        Iterator<Integer> it3 = $this$map$iv.iterator();
        while (it3.hasNext()) {
            int item$iv$iv = ((IntIterator) it3).nextInt();
            destination$iv$iv.add(device.getConfiguration(item$iv$iv));
        }
        Iterable configurations = (List) destination$iv$iv;
        Iterable $this$firstOrNull$iv = configurations;
        Iterator it4 = $this$firstOrNull$iv.iterator();
        do {
            strDescribe = null;
            if (!it4.hasNext()) {
                element$iv = null;
                break;
            }
            element$iv = it4.next();
            it2 = (UsbConfiguration) element$iv;
            iphoneCarPlayConfiguration2 = INSTANCE;
            Intrinsics.checkNotNull(it2);
        } while (!(iphoneCarPlayConfiguration2.usbMuxInterface(it2) != null && INSTANCE.hasCdcNcm(it2) && INSTANCE.hasAppleEthernet(it2)));
        UsbConfiguration chosen = (UsbConfiguration) element$iv;
        if (chosen == null) {
            Iterable $this$firstOrNull$iv2 = configurations;
            Iterator it5 = $this$firstOrNull$iv2.iterator();
            do {
                if (!it5.hasNext()) {
                    element$iv2 = null;
                    break;
                }
                element$iv2 = it5.next();
                it = (UsbConfiguration) element$iv2;
                iphoneCarPlayConfiguration = INSTANCE;
                Intrinsics.checkNotNull(it);
            } while (!(iphoneCarPlayConfiguration.usbMuxInterface(it) != null && INSTANCE.hasCdcNcm(it)));
            chosen = (UsbConfiguration) element$iv2;
        }
        Integer numValueOf = chosen != null ? Integer.valueOf(chosen.getId()) : null;
        Iterable $this$map$iv2 = configurations;
        Collection destination$iv$iv2 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv2, 10));
        for (Object item$iv$iv2 : $this$map$iv2) {
            destination$iv$iv2.add(Integer.valueOf(((UsbConfiguration) item$iv$iv2).getId()));
        }
        Collection destination$iv$iv3 = (List) destination$iv$iv2;
        if (chosen != null) {
            UsbConfiguration p0 = chosen;
            strDescribe = describe(p0);
        }
        Log.i("xcertplay-usb", "carplay config chosen=" + numValueOf + " available=" + destination$iv$iv3 + " detail=" + strDescribe);
        return chosen;
    }

    public final String describe(final UsbConfiguration configuration) {
        Intrinsics.checkNotNullParameter(configuration, "configuration");
        return CollectionsKt.joinToString$default(RangesKt.until(0, configuration.getInterfaceCount()), ",", null, null, 0, null, new Function1() { // from class: com.carplay.iap2.transport.IphoneCarPlayConfiguration$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return IphoneCarPlayConfiguration.describe$lambda$4(configuration, ((Integer) obj).intValue());
            }
        }, 30, null);
    }

    static final CharSequence describe$lambda$4(UsbConfiguration $configuration, int index) {
        UsbInterface usbInterface = $configuration.getInterface(index);
        Intrinsics.checkNotNullExpressionValue(usbInterface, "getInterface(...)");
        int id = usbInterface.getId();
        int alternateSetting = usbInterface.getAlternateSetting();
        String string = Integer.toString(usbInterface.getInterfaceClass(), CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        String string2 = Integer.toString(usbInterface.getInterfaceSubclass(), CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string2, "toString(...)");
        String string3 = Integer.toString(usbInterface.getInterfaceProtocol(), CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string3, "toString(...)");
        return id + "/" + alternateSetting + ":" + string + "." + string2 + "." + string3 + "x" + usbInterface.getEndpointCount();
    }

    public final UsbInterface usbMuxInterface(UsbConfiguration configuration) {
        Object element$iv;
        Intrinsics.checkNotNullParameter(configuration, "configuration");
        Iterable $this$map$iv = RangesKt.until(0, configuration.getInterfaceCount());
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        Iterator<Integer> it = $this$map$iv.iterator();
        while (it.hasNext()) {
            int item$iv$iv = ((IntIterator) it).nextInt();
            destination$iv$iv.add(configuration.getInterface(item$iv$iv));
        }
        Iterable $this$firstOrNull$iv = (List) destination$iv$iv;
        Iterator it2 = $this$firstOrNull$iv.iterator();
        while (it2.hasNext()) {
            element$iv = it2.next();
            UsbInterface it3 = (UsbInterface) element$iv;
            if (it3.getInterfaceClass() == 255 && it3.getInterfaceSubclass() == USBMUX_SUBCLASS && it3.getInterfaceProtocol() == 2) {
                return (UsbInterface) element$iv;
            }
        }
        element$iv = null;
        return (UsbInterface) element$iv;
    }

    public final Pair<UsbEndpoint, UsbEndpoint> usbMuxEndpoints(UsbInterface usbInterface) {
        Object element$iv;
        Object element$iv2;
        UsbEndpoint it;
        UsbEndpoint it2;
        Intrinsics.checkNotNullParameter(usbInterface, "usbInterface");
        Iterable $this$map$iv = RangesKt.until(0, usbInterface.getEndpointCount());
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        Iterator<Integer> it3 = $this$map$iv.iterator();
        while (it3.hasNext()) {
            int item$iv$iv = ((IntIterator) it3).nextInt();
            destination$iv$iv.add(usbInterface.getEndpoint(item$iv$iv));
        }
        Iterable endpoints = (List) destination$iv$iv;
        Iterable $this$firstOrNull$iv = endpoints;
        Iterator it4 = $this$firstOrNull$iv.iterator();
        do {
            if (!it4.hasNext()) {
                element$iv = null;
                break;
            }
            element$iv = it4.next();
            it2 = (UsbEndpoint) element$iv;
        } while (!(it2.getAddress() == 4 && it2.getDirection() == 0 && it2.getType() == 2));
        UsbEndpoint out = (UsbEndpoint) element$iv;
        if (out == null) {
            Iterable $this$singleOrNull$iv = endpoints;
            Object single$iv = null;
            boolean found$iv = false;
            Iterator it5 = $this$singleOrNull$iv.iterator();
            while (true) {
                if (it5.hasNext()) {
                    Object element$iv3 = it5.next();
                    UsbEndpoint it6 = (UsbEndpoint) element$iv3;
                    if (it6.getDirection() == 0 && it6.getType() == 2) {
                        if (found$iv) {
                            single$iv = null;
                            break;
                        }
                        single$iv = element$iv3;
                        found$iv = true;
                    }
                } else {
                    if (!found$iv) {
                        single$iv = null;
                        break;
                    }
                    break;
                }
            }
            out = (UsbEndpoint) single$iv;
        }
        Iterable $this$firstOrNull$iv2 = endpoints;
        Iterator it7 = $this$firstOrNull$iv2.iterator();
        do {
            if (!it7.hasNext()) {
                element$iv2 = null;
                break;
            }
            element$iv2 = it7.next();
            it = (UsbEndpoint) element$iv2;
        } while (!(it.getAddress() == PREFERRED_USBMUX_IN && it.getDirection() == 128 && it.getType() == 2));
        UsbEndpoint input = (UsbEndpoint) element$iv2;
        if (input == null) {
            Iterable $this$singleOrNull$iv2 = endpoints;
            Object single$iv2 = null;
            boolean found$iv2 = false;
            Iterator it8 = $this$singleOrNull$iv2.iterator();
            while (true) {
                if (it8.hasNext()) {
                    Object element$iv4 = it8.next();
                    UsbEndpoint it9 = (UsbEndpoint) element$iv4;
                    if (it9.getDirection() == 128 && it9.getType() == 2) {
                        if (found$iv2) {
                            single$iv2 = null;
                            break;
                        }
                        single$iv2 = element$iv4;
                        found$iv2 = true;
                    }
                } else {
                    if (!found$iv2) {
                        single$iv2 = null;
                        break;
                    }
                    break;
                }
            }
            input = (UsbEndpoint) single$iv2;
        }
        if (out == null || input == null) {
            return null;
        }
        return TuplesKt.to(out, input);
    }

    private final boolean hasCdcNcm(UsbConfiguration configuration) {
        Iterable $this$map$iv = RangesKt.until(0, configuration.getInterfaceCount());
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        Iterator<Integer> it = $this$map$iv.iterator();
        while (it.hasNext()) {
            int item$iv$iv = ((IntIterator) it).nextInt();
            destination$iv$iv.add(configuration.getInterface(item$iv$iv));
        }
        Iterable $this$any$iv = (List) destination$iv$iv;
        if (($this$any$iv instanceof Collection) && ((Collection) $this$any$iv).isEmpty()) {
            return false;
        }
        for (Object element$iv : $this$any$iv) {
            UsbInterface it2 = (UsbInterface) element$iv;
            if (it2.getInterfaceClass() == 2 && it2.getInterfaceSubclass() == 13) {
                return true;
            }
        }
        return false;
    }

    private final boolean hasAppleEthernet(UsbConfiguration configuration) {
        Iterable $this$map$iv = RangesKt.until(0, configuration.getInterfaceCount());
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        Iterator<Integer> it = $this$map$iv.iterator();
        while (it.hasNext()) {
            int item$iv$iv = ((IntIterator) it).nextInt();
            destination$iv$iv.add(configuration.getInterface(item$iv$iv));
        }
        Iterable $this$any$iv = (List) destination$iv$iv;
        if (($this$any$iv instanceof Collection) && ((Collection) $this$any$iv).isEmpty()) {
            return false;
        }
        for (Object element$iv : $this$any$iv) {
            UsbInterface it2 = (UsbInterface) element$iv;
            if (it2.getInterfaceClass() == 255 && it2.getInterfaceSubclass() == APPLE_ETHERNET_SUBCLASS && it2.getInterfaceProtocol() == 1) {
                return true;
            }
        }
        return false;
    }
}
