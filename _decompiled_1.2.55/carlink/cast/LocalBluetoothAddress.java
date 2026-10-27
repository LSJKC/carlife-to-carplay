package com.carlink.cast;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import java.util.Locale;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: LocalBluetoothAddress.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\t\u001a\u0004\u0018\u00010\b2\b\u0010\n\u001a\u0004\u0018\u00010\bJ$\u0010\u000b\u001a\u0004\u0018\u00010\b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\bH\u0007R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lcom/carlink/cast/LocalBluetoothAddress;", "", "<init>", "()V", "pattern", "Lkotlin/text/Regex;", "reserved", "", "", "normalize", "value", "read", "context", "Landroid/content/Context;", "adapter", "Landroid/bluetooth/BluetoothAdapter;", "manual", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class LocalBluetoothAddress {
    public static final LocalBluetoothAddress INSTANCE = new LocalBluetoothAddress();
    private static final Regex pattern = new Regex("(?:[0-9A-F]{2}:){5}[0-9A-F]{2}");
    private static final Set<String> reserved = SetsKt.setOf((Object[]) new String[]{"02:00:00:00:00:00", "00:00:00:00:00:00", "FF:FF:FF:FF:FF:FF"});

    private LocalBluetoothAddress() {
    }

    public final String normalize(String value) {
        String string;
        if (value != null && (string = StringsKt.trim((CharSequence) value).toString()) != null) {
            String address = string.toUpperCase(Locale.ROOT);
            Intrinsics.checkNotNullExpressionValue(address, "toUpperCase(...)");
            if (address != null) {
                if (pattern.matches(address) && !reserved.contains(address)) {
                    return address;
                }
                return null;
            }
        }
        return null;
    }

    public final String read(Context context, BluetoothAdapter adapter, String manual) {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(adapter, "adapter");
        boolean canRead = Build.VERSION.SDK_INT < 31 || context.checkSelfPermission("android.permission.BLUETOOTH_CONNECT") == 0;
        if (canRead) {
            try {
                Result.Companion companion = Result.INSTANCE;
                LocalBluetoothAddress localBluetoothAddress = this;
                objM159constructorimpl = Result.m159constructorimpl(adapter.getAddress());
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            if (Result.m165isFailureimpl(objM159constructorimpl)) {
                objM159constructorimpl = null;
            }
            String fromAdapter = (String) objM159constructorimpl;
            String strNormalize = normalize(fromAdapter);
            if (strNormalize != null) {
                return strNormalize;
            }
        }
        try {
            Result.Companion companion3 = Result.INSTANCE;
            LocalBluetoothAddress localBluetoothAddress2 = this;
            objM159constructorimpl2 = Result.m159constructorimpl(Settings.Secure.getString(context.getContentResolver(), "bluetooth_address"));
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        String secure = (String) (Result.m165isFailureimpl(objM159constructorimpl2) ? null : objM159constructorimpl2);
        String strNormalize2 = normalize(secure);
        if (strNormalize2 != null) {
            return strNormalize2;
        }
        return normalize(manual);
    }
}
