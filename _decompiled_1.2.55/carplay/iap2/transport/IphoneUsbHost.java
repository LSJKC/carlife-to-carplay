package com.carplay.iap2.transport;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConfiguration;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.util.Log;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: IphoneUsbHost.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000\u0088\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u0000 92\u00020\u0001:\u000556789B?\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\f0\u000b¢\u0006\u0004\b\r\u0010\u000eJ\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012J\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0013J\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0019\u001a\u00020\u001aJ\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0019\u001a\u00020\u001aJ\u001a\u0010\u001c\u001a\u00020\u001d2\u0012\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\f0\u000bJ\u001a\u0010\u001f\u001a\u00020\u001d2\u0012\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\f0\u000bJ*\u0010!\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u00132\u0006\u0010\"\u001a\u00020#2\u0012\u0010$\u001a\u000e\u0012\u0004\u0012\u00020%\u0012\u0004\u0012\u00020\f0\u000bJ*\u0010&\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u00132\u0006\u0010\"\u001a\u00020#2\u0012\u0010$\u001a\u000e\u0012\u0004\u0012\u00020'\u0012\u0004\u0012\u00020\f0\u000bJ$\u0010(\u001a\u00020%2\u0006\u0010\u0016\u001a\u00020\u00132\u0012\u0010)\u001a\u000e\u0012\u0004\u0012\u00020*\u0012\u0004\u0012\u00020%0\u000bH\u0002J\u0010\u0010+\u001a\u00020,2\u0006\u0010\u0016\u001a\u00020\u0013H\u0002J\u0010\u0010-\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u0013H\u0002J\b\u0010.\u001a\u00020/H\u0002J$\u00100\u001a\u00020\u001d2\u0006\u00101\u001a\u0002022\u0012\u00103\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\f0\u000bH\u0002J\u000e\u00104\u001a\u0004\u0018\u00010\u0013*\u00020\u001aH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\n \u0010*\u0004\u0018\u00010\u00030\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006:"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbHost;", "", "context", "Landroid/content/Context;", "usbManager", "Landroid/hardware/usb/UsbManager;", "matcher", "Lcom/carplay/iap2/transport/IphoneUsbMatcher;", "permissionAction", "", "onDiagnostic", "Lkotlin/Function1;", "", "<init>", "(Landroid/content/Context;Landroid/hardware/usb/UsbManager;Lcom/carplay/iap2/transport/IphoneUsbMatcher;Ljava/lang/String;Lkotlin/jvm/functions/Function1;)V", "appContext", "kotlin.jvm.PlatformType", "discover", "", "Landroid/hardware/usb/UsbDevice;", "requestPermission", "Lcom/carplay/iap2/transport/IphoneUsbHost$PermissionRequest;", "device", "parsePermissionResult", "Lcom/carplay/iap2/transport/IphoneUsbHost$PermissionResult;", "intent", "Landroid/content/Intent;", "parseAttachedDevice", "registerPermissionReceiver", "Ljava/io/Closeable;", "onResult", "registerAttachReceiver", "onAttached", "requestCarPlayReenumerationAsync", "executor", "Ljava/util/concurrent/Executor;", "callback", "Lcom/carplay/iap2/transport/IphoneUsbHost$TransitionResult;", "openIap2UsbSessionAsync", "Lcom/carplay/iap2/transport/IphoneUsbHost$Iap2SessionResult;", "runTransition", "operation", "Landroid/hardware/usb/UsbDeviceConnection;", "openIap2UsbSession", "Lcom/carplay/iap2/transport/Iap2UsbSession;", "requireConfiguredDevice", "permissionPendingIntent", "Landroid/app/PendingIntent;", "registerReceiver", "filter", "Landroid/content/IntentFilter;", "onReceive", "usbDevice", "PermissionRequest", "PermissionResult", "TransitionResult", "Iap2SessionResult", "Companion", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class IphoneUsbHost {
    private static final int CARPLAY_CONFIGURATION_INDEX = 4;
    private static final int CARPLAY_CONFIGURATION_REQUEST = 82;
    private static final int CONTROL_TRANSFER_TIMEOUT_MILLIS = 1000;
    private static final int USB_VENDOR_DEVICE_IN = 192;
    private static final int VENDOR_RESPONSE_LENGTH = 1;
    private final Context appContext;
    private final IphoneUsbMatcher matcher;
    private final Function1<String, Unit> onDiagnostic;
    private final String permissionAction;
    private final UsbManager usbManager;

    /* JADX WARN: Multi-variable type inference failed */
    public IphoneUsbHost(Context context, UsbManager usbManager, IphoneUsbMatcher matcher, String permissionAction, Function1<? super String, Unit> onDiagnostic) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(usbManager, "usbManager");
        Intrinsics.checkNotNullParameter(matcher, "matcher");
        Intrinsics.checkNotNullParameter(permissionAction, "permissionAction");
        Intrinsics.checkNotNullParameter(onDiagnostic, "onDiagnostic");
        this.usbManager = usbManager;
        this.matcher = matcher;
        this.permissionAction = permissionAction;
        this.onDiagnostic = onDiagnostic;
        this.appContext = context.getApplicationContext();
    }

    public /* synthetic */ IphoneUsbHost(Context context, UsbManager usbManager, IphoneUsbMatcher iphoneUsbMatcher, String str, Function1 function1, int i, DefaultConstructorMarker defaultConstructorMarker) {
        String str2;
        Function1 function2;
        if ((i & 8) == 0) {
            str2 = str;
        } else {
            str2 = context.getPackageName() + ".IPHONE_USB_PERMISSION";
        }
        if ((i & 16) == 0) {
            function2 = function1;
        } else {
            function2 = new Function1() { // from class: com.carplay.iap2.transport.IphoneUsbHost$$ExternalSyntheticLambda4
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return IphoneUsbHost._init_$lambda$0((String) obj);
                }
            };
        }
        this(context, usbManager, iphoneUsbMatcher, str2, function2);
    }

    static final Unit _init_$lambda$0(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    /* JADX INFO: compiled from: IphoneUsbHost.kt */
    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0002\u0004\u0005B\t\b\u0004¢\u0006\u0004\b\u0002\u0010\u0003\u0082\u0001\u0002\u0006\u0007¨\u0006\b"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbHost$PermissionRequest;", "", "<init>", "()V", "AlreadyGranted", "Requested", "Lcom/carplay/iap2/transport/IphoneUsbHost$PermissionRequest$AlreadyGranted;", "Lcom/carplay/iap2/transport/IphoneUsbHost$PermissionRequest$Requested;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static abstract class PermissionRequest {
        public /* synthetic */ PermissionRequest(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: compiled from: IphoneUsbHost.kt */
        @Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\t\u0010\b\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rHÖ\u0003J\t\u0010\u000e\u001a\u00020\u000fHÖ\u0001J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0012"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbHost$PermissionRequest$AlreadyGranted;", "Lcom/carplay/iap2/transport/IphoneUsbHost$PermissionRequest;", "device", "Landroid/hardware/usb/UsbDevice;", "<init>", "(Landroid/hardware/usb/UsbDevice;)V", "getDevice", "()Landroid/hardware/usb/UsbDevice;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
        public static final /* data */ class AlreadyGranted extends PermissionRequest {
            private final UsbDevice device;

            public static /* synthetic */ AlreadyGranted copy$default(AlreadyGranted alreadyGranted, UsbDevice usbDevice, int i, Object obj) {
                if ((i & 1) != 0) {
                    usbDevice = alreadyGranted.device;
                }
                return alreadyGranted.copy(usbDevice);
            }

            /* JADX INFO: renamed from: component1, reason: from getter */
            public final UsbDevice getDevice() {
                return this.device;
            }

            public final AlreadyGranted copy(UsbDevice device) {
                Intrinsics.checkNotNullParameter(device, "device");
                return new AlreadyGranted(device);
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                return (other instanceof AlreadyGranted) && Intrinsics.areEqual(this.device, ((AlreadyGranted) other).device);
            }

            public int hashCode() {
                return this.device.hashCode();
            }

            public String toString() {
                return "AlreadyGranted(device=" + this.device + ")";
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public AlreadyGranted(UsbDevice device) {
                super(null);
                Intrinsics.checkNotNullParameter(device, "device");
                this.device = device;
            }

            public final UsbDevice getDevice() {
                return this.device;
            }
        }

        private PermissionRequest() {
        }

        /* JADX INFO: compiled from: IphoneUsbHost.kt */
        @Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\t\u0010\b\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rHÖ\u0003J\t\u0010\u000e\u001a\u00020\u000fHÖ\u0001J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0012"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbHost$PermissionRequest$Requested;", "Lcom/carplay/iap2/transport/IphoneUsbHost$PermissionRequest;", "device", "Landroid/hardware/usb/UsbDevice;", "<init>", "(Landroid/hardware/usb/UsbDevice;)V", "getDevice", "()Landroid/hardware/usb/UsbDevice;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
        public static final /* data */ class Requested extends PermissionRequest {
            private final UsbDevice device;

            public static /* synthetic */ Requested copy$default(Requested requested, UsbDevice usbDevice, int i, Object obj) {
                if ((i & 1) != 0) {
                    usbDevice = requested.device;
                }
                return requested.copy(usbDevice);
            }

            /* JADX INFO: renamed from: component1, reason: from getter */
            public final UsbDevice getDevice() {
                return this.device;
            }

            public final Requested copy(UsbDevice device) {
                Intrinsics.checkNotNullParameter(device, "device");
                return new Requested(device);
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                return (other instanceof Requested) && Intrinsics.areEqual(this.device, ((Requested) other).device);
            }

            public int hashCode() {
                return this.device.hashCode();
            }

            public String toString() {
                return "Requested(device=" + this.device + ")";
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public Requested(UsbDevice device) {
                super(null);
                Intrinsics.checkNotNullParameter(device, "device");
                this.device = device;
            }

            public final UsbDevice getDevice() {
                return this.device;
            }
        }
    }

    /* JADX INFO: compiled from: IphoneUsbHost.kt */
    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0002\u0004\u0005B\t\b\u0004¢\u0006\u0004\b\u0002\u0010\u0003\u0082\u0001\u0002\u0006\u0007¨\u0006\b"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbHost$PermissionResult;", "", "<init>", "()V", "Granted", "Denied", "Lcom/carplay/iap2/transport/IphoneUsbHost$PermissionResult$Denied;", "Lcom/carplay/iap2/transport/IphoneUsbHost$PermissionResult$Granted;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static abstract class PermissionResult {
        public /* synthetic */ PermissionResult(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: compiled from: IphoneUsbHost.kt */
        @Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\t\u0010\b\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rHÖ\u0003J\t\u0010\u000e\u001a\u00020\u000fHÖ\u0001J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0012"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbHost$PermissionResult$Granted;", "Lcom/carplay/iap2/transport/IphoneUsbHost$PermissionResult;", "device", "Landroid/hardware/usb/UsbDevice;", "<init>", "(Landroid/hardware/usb/UsbDevice;)V", "getDevice", "()Landroid/hardware/usb/UsbDevice;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
        public static final /* data */ class Granted extends PermissionResult {
            private final UsbDevice device;

            public static /* synthetic */ Granted copy$default(Granted granted, UsbDevice usbDevice, int i, Object obj) {
                if ((i & 1) != 0) {
                    usbDevice = granted.device;
                }
                return granted.copy(usbDevice);
            }

            /* JADX INFO: renamed from: component1, reason: from getter */
            public final UsbDevice getDevice() {
                return this.device;
            }

            public final Granted copy(UsbDevice device) {
                Intrinsics.checkNotNullParameter(device, "device");
                return new Granted(device);
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                return (other instanceof Granted) && Intrinsics.areEqual(this.device, ((Granted) other).device);
            }

            public int hashCode() {
                return this.device.hashCode();
            }

            public String toString() {
                return "Granted(device=" + this.device + ")";
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public Granted(UsbDevice device) {
                super(null);
                Intrinsics.checkNotNullParameter(device, "device");
                this.device = device;
            }

            public final UsbDevice getDevice() {
                return this.device;
            }
        }

        private PermissionResult() {
        }

        /* JADX INFO: compiled from: IphoneUsbHost.kt */
        @Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\t\u0010\b\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rHÖ\u0003J\t\u0010\u000e\u001a\u00020\u000fHÖ\u0001J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0012"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbHost$PermissionResult$Denied;", "Lcom/carplay/iap2/transport/IphoneUsbHost$PermissionResult;", "device", "Landroid/hardware/usb/UsbDevice;", "<init>", "(Landroid/hardware/usb/UsbDevice;)V", "getDevice", "()Landroid/hardware/usb/UsbDevice;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
        public static final /* data */ class Denied extends PermissionResult {
            private final UsbDevice device;

            public static /* synthetic */ Denied copy$default(Denied denied, UsbDevice usbDevice, int i, Object obj) {
                if ((i & 1) != 0) {
                    usbDevice = denied.device;
                }
                return denied.copy(usbDevice);
            }

            /* JADX INFO: renamed from: component1, reason: from getter */
            public final UsbDevice getDevice() {
                return this.device;
            }

            public final Denied copy(UsbDevice device) {
                Intrinsics.checkNotNullParameter(device, "device");
                return new Denied(device);
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                return (other instanceof Denied) && Intrinsics.areEqual(this.device, ((Denied) other).device);
            }

            public int hashCode() {
                return this.device.hashCode();
            }

            public String toString() {
                return "Denied(device=" + this.device + ")";
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public Denied(UsbDevice device) {
                super(null);
                Intrinsics.checkNotNullParameter(device, "device");
                this.device = device;
            }

            public final UsbDevice getDevice() {
                return this.device;
            }
        }
    }

    /* JADX INFO: compiled from: IphoneUsbHost.kt */
    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0002\u0004\u0005B\t\b\u0004¢\u0006\u0004\b\u0002\u0010\u0003\u0082\u0001\u0002\u0006\u0007¨\u0006\b"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbHost$TransitionResult;", "", "<init>", "()V", "ReenumerationRequested", "Failed", "Lcom/carplay/iap2/transport/IphoneUsbHost$TransitionResult$Failed;", "Lcom/carplay/iap2/transport/IphoneUsbHost$TransitionResult$ReenumerationRequested;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static abstract class TransitionResult {
        public /* synthetic */ TransitionResult(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private TransitionResult() {
        }

        /* JADX INFO: compiled from: IphoneUsbHost.kt */
        @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\bÆ\n\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0013\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007HÖ\u0003J\t\u0010\b\u001a\u00020\tHÖ\u0001J\t\u0010\n\u001a\u00020\u000bHÖ\u0001¨\u0006\f"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbHost$TransitionResult$ReenumerationRequested;", "Lcom/carplay/iap2/transport/IphoneUsbHost$TransitionResult;", "<init>", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
        public static final /* data */ class ReenumerationRequested extends TransitionResult {
            public static final ReenumerationRequested INSTANCE = new ReenumerationRequested();

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                if (!(other instanceof ReenumerationRequested)) {
                    return false;
                }
                return true;
            }

            public int hashCode() {
                return -1232530071;
            }

            public String toString() {
                return "ReenumerationRequested";
            }

            private ReenumerationRequested() {
                super(null);
            }
        }

        /* JADX INFO: compiled from: IphoneUsbHost.kt */
        @Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\t\u0010\b\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rHÖ\u0003J\t\u0010\u000e\u001a\u00020\u000fHÖ\u0001J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0012"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbHost$TransitionResult$Failed;", "Lcom/carplay/iap2/transport/IphoneUsbHost$TransitionResult;", "error", "Lcom/carplay/iap2/transport/IphoneUsbException;", "<init>", "(Lcom/carplay/iap2/transport/IphoneUsbException;)V", "getError", "()Lcom/carplay/iap2/transport/IphoneUsbException;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
        public static final /* data */ class Failed extends TransitionResult {
            private final IphoneUsbException error;

            public static /* synthetic */ Failed copy$default(Failed failed, IphoneUsbException iphoneUsbException, int i, Object obj) {
                if ((i & 1) != 0) {
                    iphoneUsbException = failed.error;
                }
                return failed.copy(iphoneUsbException);
            }

            /* JADX INFO: renamed from: component1, reason: from getter */
            public final IphoneUsbException getError() {
                return this.error;
            }

            public final Failed copy(IphoneUsbException error) {
                Intrinsics.checkNotNullParameter(error, "error");
                return new Failed(error);
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                return (other instanceof Failed) && Intrinsics.areEqual(this.error, ((Failed) other).error);
            }

            public int hashCode() {
                return this.error.hashCode();
            }

            public String toString() {
                return "Failed(error=" + this.error + ")";
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public Failed(IphoneUsbException error) {
                super(null);
                Intrinsics.checkNotNullParameter(error, "error");
                this.error = error;
            }

            public final IphoneUsbException getError() {
                return this.error;
            }
        }
    }

    /* JADX INFO: compiled from: IphoneUsbHost.kt */
    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0002\u0004\u0005B\t\b\u0004¢\u0006\u0004\b\u0002\u0010\u0003\u0082\u0001\u0002\u0006\u0007¨\u0006\b"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbHost$Iap2SessionResult;", "", "<init>", "()V", "Connected", "Failed", "Lcom/carplay/iap2/transport/IphoneUsbHost$Iap2SessionResult$Connected;", "Lcom/carplay/iap2/transport/IphoneUsbHost$Iap2SessionResult$Failed;", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static abstract class Iap2SessionResult {
        public /* synthetic */ Iap2SessionResult(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: compiled from: IphoneUsbHost.kt */
        @Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\t\u0010\b\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rHÖ\u0003J\t\u0010\u000e\u001a\u00020\u000fHÖ\u0001J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0012"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbHost$Iap2SessionResult$Connected;", "Lcom/carplay/iap2/transport/IphoneUsbHost$Iap2SessionResult;", "session", "Lcom/carplay/iap2/transport/Iap2UsbSession;", "<init>", "(Lcom/carplay/iap2/transport/Iap2UsbSession;)V", "getSession", "()Lcom/carplay/iap2/transport/Iap2UsbSession;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
        public static final /* data */ class Connected extends Iap2SessionResult {
            private final Iap2UsbSession session;

            public static /* synthetic */ Connected copy$default(Connected connected, Iap2UsbSession iap2UsbSession, int i, Object obj) {
                if ((i & 1) != 0) {
                    iap2UsbSession = connected.session;
                }
                return connected.copy(iap2UsbSession);
            }

            /* JADX INFO: renamed from: component1, reason: from getter */
            public final Iap2UsbSession getSession() {
                return this.session;
            }

            public final Connected copy(Iap2UsbSession session) {
                Intrinsics.checkNotNullParameter(session, "session");
                return new Connected(session);
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                return (other instanceof Connected) && Intrinsics.areEqual(this.session, ((Connected) other).session);
            }

            public int hashCode() {
                return this.session.hashCode();
            }

            public String toString() {
                return "Connected(session=" + this.session + ")";
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public Connected(Iap2UsbSession session) {
                super(null);
                Intrinsics.checkNotNullParameter(session, "session");
                this.session = session;
            }

            public final Iap2UsbSession getSession() {
                return this.session;
            }
        }

        private Iap2SessionResult() {
        }

        /* JADX INFO: compiled from: IphoneUsbHost.kt */
        @Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\t\u0010\b\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\t\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rHÖ\u0003J\t\u0010\u000e\u001a\u00020\u000fHÖ\u0001J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0012"}, d2 = {"Lcom/carplay/iap2/transport/IphoneUsbHost$Iap2SessionResult$Failed;", "Lcom/carplay/iap2/transport/IphoneUsbHost$Iap2SessionResult;", "error", "Lcom/carplay/iap2/transport/IphoneUsbException;", "<init>", "(Lcom/carplay/iap2/transport/IphoneUsbException;)V", "getError", "()Lcom/carplay/iap2/transport/IphoneUsbException;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
        public static final /* data */ class Failed extends Iap2SessionResult {
            private final IphoneUsbException error;

            public static /* synthetic */ Failed copy$default(Failed failed, IphoneUsbException iphoneUsbException, int i, Object obj) {
                if ((i & 1) != 0) {
                    iphoneUsbException = failed.error;
                }
                return failed.copy(iphoneUsbException);
            }

            /* JADX INFO: renamed from: component1, reason: from getter */
            public final IphoneUsbException getError() {
                return this.error;
            }

            public final Failed copy(IphoneUsbException error) {
                Intrinsics.checkNotNullParameter(error, "error");
                return new Failed(error);
            }

            public boolean equals(Object other) {
                if (this == other) {
                    return true;
                }
                return (other instanceof Failed) && Intrinsics.areEqual(this.error, ((Failed) other).error);
            }

            public int hashCode() {
                return this.error.hashCode();
            }

            public String toString() {
                return "Failed(error=" + this.error + ")";
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public Failed(IphoneUsbException error) {
                super(null);
                Intrinsics.checkNotNullParameter(error, "error");
                this.error = error;
            }

            public final IphoneUsbException getError() {
                return this.error;
            }
        }
    }

    public final List<UsbDevice> discover() {
        Iterable iterableValues = this.usbManager.getDeviceList().values();
        Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
        Iterable $this$filter$iv = iterableValues;
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv : $this$filter$iv) {
            UsbDevice it = (UsbDevice) element$iv$iv;
            if (this.matcher.matches(it.getVendorId(), it.getProductId())) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        return (List) destination$iv$iv;
    }

    public final PermissionRequest requestPermission(UsbDevice device) throws IphoneUsbException {
        Intrinsics.checkNotNullParameter(device, "device");
        requireConfiguredDevice(device);
        if (this.usbManager.hasPermission(device)) {
            return new PermissionRequest.AlreadyGranted(device);
        }
        this.usbManager.requestPermission(device, permissionPendingIntent());
        return new PermissionRequest.Requested(device);
    }

    public final PermissionResult parsePermissionResult(Intent intent) {
        UsbDevice device;
        Intrinsics.checkNotNullParameter(intent, "intent");
        if (!Intrinsics.areEqual(intent.getAction(), this.permissionAction) || (device = usbDevice(intent)) == null || !this.matcher.matches(device.getVendorId(), device.getProductId())) {
            return null;
        }
        if (intent.getBooleanExtra("permission", false)) {
            return new PermissionResult.Granted(device);
        }
        return new PermissionResult.Denied(device);
    }

    public final UsbDevice parseAttachedDevice(Intent intent) {
        UsbDevice device;
        Intrinsics.checkNotNullParameter(intent, "intent");
        if (Intrinsics.areEqual(intent.getAction(), "android.hardware.usb.action.USB_DEVICE_ATTACHED") && (device = usbDevice(intent)) != null && this.matcher.matches(device.getVendorId(), device.getProductId())) {
            return device;
        }
        return null;
    }

    static final Unit registerPermissionReceiver$lambda$3(IphoneUsbHost this$0, Function1 $onResult, Intent it) {
        Intrinsics.checkNotNullParameter(it, "it");
        PermissionResult permissionResult = this$0.parsePermissionResult(it);
        if (permissionResult != null) {
            $onResult.invoke(permissionResult);
        }
        return Unit.INSTANCE;
    }

    public final Closeable registerPermissionReceiver(final Function1<? super PermissionResult, Unit> onResult) {
        Intrinsics.checkNotNullParameter(onResult, "onResult");
        return registerReceiver(new IntentFilter(this.permissionAction), new Function1() { // from class: com.carplay.iap2.transport.IphoneUsbHost$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return IphoneUsbHost.registerPermissionReceiver$lambda$3(this.f$0, onResult, (Intent) obj);
            }
        });
    }

    public final Closeable registerAttachReceiver(final Function1<? super UsbDevice, Unit> onAttached) {
        Intrinsics.checkNotNullParameter(onAttached, "onAttached");
        return registerReceiver(new IntentFilter("android.hardware.usb.action.USB_DEVICE_ATTACHED"), new Function1() { // from class: com.carplay.iap2.transport.IphoneUsbHost$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return IphoneUsbHost.registerAttachReceiver$lambda$4(this.f$0, onAttached, (Intent) obj);
            }
        });
    }

    static final Unit registerAttachReceiver$lambda$4(IphoneUsbHost this$0, Function1 $onAttached, Intent it) {
        Intrinsics.checkNotNullParameter(it, "it");
        UsbDevice attachedDevice = this$0.parseAttachedDevice(it);
        if (attachedDevice != null) {
            $onAttached.invoke(attachedDevice);
        }
        return Unit.INSTANCE;
    }

    public final void requestCarPlayReenumerationAsync(final UsbDevice device, Executor executor, final Function1<? super TransitionResult, Unit> callback) {
        Intrinsics.checkNotNullParameter(device, "device");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(callback, "callback");
        executor.execute(new Runnable() { // from class: com.carplay.iap2.transport.IphoneUsbHost$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                callback.invoke(this.runTransition(device, new Function1() { // from class: com.carplay.iap2.transport.IphoneUsbHost$$ExternalSyntheticLambda2
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return IphoneUsbHost.requestCarPlayReenumerationAsync$lambda$6$lambda$5((UsbDeviceConnection) obj);
                    }
                }));
            }
        });
    }

    static final TransitionResult requestCarPlayReenumerationAsync$lambda$6$lambda$5(UsbDeviceConnection connection) throws IphoneUsbException.Protocol {
        Intrinsics.checkNotNullParameter(connection, "connection");
        byte[] response = new byte[1];
        int transferred = connection.controlTransfer(192, CARPLAY_CONFIGURATION_REQUEST, 0, 4, response, response.length, 1000);
        if (transferred != response.length) {
            throw new IphoneUsbException.Protocol("CarPlay configuration request transferred " + transferred + " of " + response.length + " bytes");
        }
        return TransitionResult.ReenumerationRequested.INSTANCE;
    }

    public final void openIap2UsbSessionAsync(final UsbDevice device, Executor executor, final Function1<? super Iap2SessionResult, Unit> callback) {
        Intrinsics.checkNotNullParameter(device, "device");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(callback, "callback");
        executor.execute(new Runnable() { // from class: com.carplay.iap2.transport.IphoneUsbHost$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                IphoneUsbHost.openIap2UsbSessionAsync$lambda$7(this.f$0, device, callback);
            }
        });
    }

    static final void openIap2UsbSessionAsync$lambda$7(IphoneUsbHost this$0, UsbDevice $device, Function1 $callback) {
        Iap2SessionResult.Connected result;
        try {
            result = new Iap2SessionResult.Connected(this$0.openIap2UsbSession($device));
        } catch (IphoneUsbException error) {
            result = new Iap2SessionResult.Failed(error);
        } catch (SecurityException error2) {
            result = new Iap2SessionResult.Failed(new IphoneUsbException.PermissionDenied("USB permission was denied", error2));
        } catch (RuntimeException error3) {
            result = new Iap2SessionResult.Failed(new IphoneUsbException.DeviceUnavailable("iPhone USBMUX operation failed", error3));
        }
        try {
            $callback.invoke(result);
        } catch (Throwable error4) {
            if (result instanceof Iap2SessionResult.Connected) {
                try {
                    ((Iap2SessionResult.Connected) result).getSession().close();
                } catch (Throwable closeError) {
                    ExceptionsKt.addSuppressed(error4, closeError);
                }
            }
            throw error4;
        }
    }

    private final TransitionResult runTransition(UsbDevice device, Function1<? super UsbDeviceConnection, ? extends TransitionResult> operation) {
        try {
            requireConfiguredDevice(device);
            if (!this.usbManager.hasPermission(device)) {
                throw new IphoneUsbException.PermissionDenied("USB permission has not been granted", null, 2, null);
            }
            UsbDeviceConnection connection = this.usbManager.openDevice(device);
            if (connection == null) {
                throw new IphoneUsbException.DeviceUnavailable("UsbManager could not open the iPhone", null, 2, null);
            }
            try {
                return operation.invoke(connection);
            } finally {
                connection.close();
            }
        } catch (IphoneUsbException error) {
            return new TransitionResult.Failed(error);
        } catch (SecurityException error2) {
            return new TransitionResult.Failed(new IphoneUsbException.PermissionDenied("USB permission was denied", error2));
        } catch (RuntimeException error3) {
            return new TransitionResult.Failed(new IphoneUsbException.DeviceUnavailable("iPhone USB operation failed", error3));
        }
    }

    /* JADX WARN: Not initialized variable reg: 17, insn: 0x0142: MOVE (r6 I:??[OBJECT, ARRAY]) = (r17 I:??[OBJECT, ARRAY] A[D('claimedInterface' android.hardware.usb.UsbInterface)]), block:B:36:0x0142 */
    private final Iap2UsbSession openIap2UsbSession(UsbDevice device) throws Throwable {
        UsbInterface claimedInterface;
        requireConfiguredDevice(device);
        if (!this.usbManager.hasPermission(device)) {
            throw new IphoneUsbException.PermissionDenied("USB permission has not been granted", null, 2, null);
        }
        UsbDeviceConnection connection = this.usbManager.openDevice(device);
        if (connection == null) {
            throw new IphoneUsbException.DeviceUnavailable("UsbManager could not open the iPhone", null, 2, null);
        }
        UsbInterface claimedInterface2 = null;
        try {
            UsbConfiguration configuration = IphoneCarPlayConfiguration.INSTANCE.find(device);
            try {
                if (configuration == null) {
                    throw new IphoneUsbException.Protocol("Re-enumerated iPhone exposes no USBMUX CarPlay configuration");
                }
                if (!connection.setConfiguration(configuration)) {
                    try {
                        Log.w("xcertplay-usb", "setConfiguration " + configuration.getId() + " reported failure; claiming anyway");
                    } catch (Throwable th) {
                        error = th;
                    }
                }
                UsbInterface usbMux = IphoneCarPlayConfiguration.INSTANCE.usbMuxInterface(configuration);
                if (usbMux == null) {
                    throw new IphoneUsbException.Protocol("CarPlay configuration exposes no USBMUX interface");
                }
                Pair<UsbEndpoint, UsbEndpoint> pairUsbMuxEndpoints = IphoneCarPlayConfiguration.INSTANCE.usbMuxEndpoints(usbMux);
                if (pairUsbMuxEndpoints == null) {
                    throw new IphoneUsbException.Protocol("USBMUX interface exposes no bulk endpoint pair");
                }
                Log.i("xcertplay-usb", "usbmux config=" + configuration.getId() + " iface=" + usbMux.getId() + " alt=" + usbMux.getAlternateSetting() + " class=" + usbMux.getInterfaceClass() + "/" + usbMux.getInterfaceSubclass() + "/" + usbMux.getInterfaceProtocol() + " endpoints=" + usbMux.getEndpointCount() + " out=" + IphoneUsbHostKt.describeUsbEndpoint(pairUsbMuxEndpoints.getFirst()) + " in=" + IphoneUsbHostKt.describeUsbEndpoint(pairUsbMuxEndpoints.getSecond()));
                if (!connection.claimInterface(usbMux, true)) {
                    throw new IphoneUsbException.DeviceUnavailable("Android could not claim USBMUX interface 1", null, 2, null);
                }
                claimedInterface2 = usbMux;
                return new Iap2UsbSession(connection, pairUsbMuxEndpoints.getFirst(), pairUsbMuxEndpoints.getSecond(), this.onDiagnostic);
            } catch (Throwable th2) {
                error = th2;
                claimedInterface2 = claimedInterface;
            }
        } catch (Throwable th3) {
            error = th3;
        }
        if (claimedInterface2 != null) {
            connection.releaseInterface(claimedInterface2);
        }
        connection.close();
        throw error;
    }

    private final void requireConfiguredDevice(UsbDevice device) throws IphoneUsbException.DeviceUnavailable {
        if (!this.matcher.matches(device.getVendorId(), device.getProductId())) {
            throw new IphoneUsbException.DeviceUnavailable("USB device is not a configured iPhone identity", null, 2, null);
        }
    }

    private final PendingIntent permissionPendingIntent() {
        Intent intent = new Intent(this.permissionAction).setPackage(this.appContext.getPackageName());
        Intrinsics.checkNotNullExpressionValue(intent, "setPackage(...)");
        PendingIntent broadcast = PendingIntent.getBroadcast(this.appContext, 0, intent, 201326592);
        Intrinsics.checkNotNullExpressionValue(broadcast, "getBroadcast(...)");
        return broadcast;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.carplay.iap2.transport.IphoneUsbHost$registerReceiver$receiver$1] */
    private final Closeable registerReceiver(IntentFilter filter, final Function1<? super Intent, Unit> onReceive) {
        final ?? r0 = new BroadcastReceiver() { // from class: com.carplay.iap2.transport.IphoneUsbHost$registerReceiver$receiver$1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                Intrinsics.checkNotNullParameter(context, "context");
                Intrinsics.checkNotNullParameter(intent, "intent");
                onReceive.invoke(intent);
            }
        };
        int i = Build.VERSION.SDK_INT;
        Context context = this.appContext;
        if (i >= 33) {
            context.registerReceiver((BroadcastReceiver) r0, filter, 4);
        } else {
            context.registerReceiver((BroadcastReceiver) r0, filter);
        }
        final AtomicBoolean registered = new AtomicBoolean(true);
        return new Closeable() { // from class: com.carplay.iap2.transport.IphoneUsbHost$$ExternalSyntheticLambda6
            @Override // java.io.Closeable, java.lang.AutoCloseable
            public final void close() {
                IphoneUsbHost.registerReceiver$lambda$8(registered, this, r0);
            }
        };
    }

    static final void registerReceiver$lambda$8(AtomicBoolean $registered, IphoneUsbHost this$0, IphoneUsbHost$registerReceiver$receiver$1 $receiver) {
        if ($registered.compareAndSet(true, false)) {
            this$0.appContext.unregisterReceiver($receiver);
        }
    }

    private final UsbDevice usbDevice(Intent $this$usbDevice) {
        if (Build.VERSION.SDK_INT >= 33) {
            return (UsbDevice) $this$usbDevice.getParcelableExtra("device", UsbDevice.class);
        }
        return (UsbDevice) $this$usbDevice.getParcelableExtra("device");
    }
}
