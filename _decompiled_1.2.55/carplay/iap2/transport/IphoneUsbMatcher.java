package com.carplay.iap2.transport;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;

/* JADX INFO: compiled from: IphoneUsbHost.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB!\b\u0002\u0012\u000e\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0004\b\u0007\u0010\bB\u0017\b\u0016\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\t¢\u0006\u0004\b\u0007\u0010\nJ\u0016\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rR\u0016\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbMatcher;", "", "allowedDevices", "", "Lcom/carplay/iap2/transport/UsbDeviceId;", "allowAnyAppleProduct", "", "<init>", "(Ljava/util/Set;Z)V", "", "(Ljava/util/Collection;)V", "matches", "vendorId", "", "productId", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class IphoneUsbMatcher {
    public static final int APPLE_VENDOR_ID = 1452;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final boolean allowAnyAppleProduct;
    private final Set<UsbDeviceId> allowedDevices;

    public /* synthetic */ IphoneUsbMatcher(Set set, boolean z, DefaultConstructorMarker defaultConstructorMarker) {
        this(set, z);
    }

    /* JADX WARN: Code duplicated, block: B:28:0x005a  */
    private IphoneUsbMatcher(Set<UsbDeviceId> set, boolean allowAnyAppleProduct) {
        boolean z;
        boolean z2;
        this.allowedDevices = set;
        this.allowAnyAppleProduct = allowAnyAppleProduct;
        if (!(this.allowedDevices == null || !this.allowedDevices.isEmpty())) {
            throw new IllegalArgumentException("At least one iPhone USB identity is required when not using Apple-vendor discovery".toString());
        }
        if (this.allowedDevices != null) {
            Iterable $this$all$iv = this.allowedDevices;
            if (!($this$all$iv instanceof Collection) || !((Collection) $this$all$iv).isEmpty()) {
                Iterator it = $this$all$iv.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        z2 = true;
                        break;
                    }
                    Object element$iv = it.next();
                    UsbDeviceId it2 = (UsbDeviceId) element$iv;
                    if (!(it2.getVendorId() == 1452)) {
                        z2 = false;
                        break;
                    }
                }
            } else {
                z2 = true;
            }
            z = z2;
        }
        if (z) {
            return;
        }
        String string = Integer.toString(APPLE_VENDOR_ID, CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        throw new IllegalArgumentException(("iPhone USB identities must use Apple vendor ID 0x" + string).toString());
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public IphoneUsbMatcher(Collection<UsbDeviceId> allowedDevices) {
        this(CollectionsKt.toSet(allowedDevices), false);
        Intrinsics.checkNotNullParameter(allowedDevices, "allowedDevices");
    }

    public final boolean matches(int vendorId, int productId) {
        if (this.allowAnyAppleProduct) {
            return vendorId == 1452;
        }
        Set<UsbDeviceId> setEmptySet = this.allowedDevices;
        if (setEmptySet == null) {
            setEmptySet = SetsKt.emptySet();
        }
        return setEmptySet.contains(new UsbDeviceId(vendorId, productId));
    }

    /* JADX INFO: compiled from: IphoneUsbHost.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0006\u0010\u0006\u001a\u00020\u0007R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbMatcher$Companion;", "", "<init>", "()V", "APPLE_VENDOR_ID", "", "appleVendor", "Lcom/carplay/iap2/transport/IphoneUsbMatcher;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final IphoneUsbMatcher appleVendor() {
            return new IphoneUsbMatcher(null, true, 0 == true ? 1 : 0);
        }
    }
}
