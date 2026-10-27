package com.carlink.cast;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.content.ContextCompat;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: BluetoothArrivalReceiver.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u001a\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0016¨\u0006\n"}, d2 = {"Lcom/carlink/cast/BluetoothArrivalReceiver;", "Landroid/content/BroadcastReceiver;", "<init>", "()V", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class BluetoothArrivalReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) throws Throwable {
        String action;
        BluetoothDevice bluetoothDevice;
        CastSettings settings;
        String peer;
        Object objM159constructorimpl;
        Intrinsics.checkNotNullParameter(context, "context");
        if (intent == null || (action = intent.getAction()) == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 33) {
            bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE", BluetoothDevice.class);
        } else {
            bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
        }
        if (bluetoothDevice != null) {
            BluetoothDevice device = bluetoothDevice;
            if (Intrinsics.areEqual(action, "android.bluetooth.device.action.ACL_DISCONNECTED")) {
                CastService.Companion companion = CastService.INSTANCE;
                String address = device.getAddress();
                Intrinsics.checkNotNullExpressionValue(address, "getAddress(...)");
                companion.onBluetoothDisconnected(context, address);
                return;
            }
            if (Intrinsics.areEqual(action, "android.bluetooth.device.action.ACL_CONNECTED") && ContextCompat.checkSelfPermission(context, "android.permission.BLUETOOTH_CONNECT") == 0 && (peer = (settings = new CastSettings(context)).getPeerAddress()) != null && StringsKt.equals(device.getAddress(), peer, true) && device.getBondState() == 12) {
                if (settings.getSourceMode() == SourceMode.MIRROR) {
                    if (settings.getAutoConnect()) {
                        CastLog.INSTANCE.event("INFO", "车机蓝牙已连接，但屏幕镜像需要前台录屏授权，未自动开始");
                        return;
                    }
                    return;
                }
                if (CastEngine.INSTANCE.carLifeAvailable(context)) {
                    if (!settings.getCarLifeAutoSession()) {
                        CastLog.INSTANCE.event("INFO", "车机蓝牙已连接，但 CarLife 自动跟车已关闭");
                        return;
                    }
                    if (!settings.hasConnected(peer)) {
                        CastLog.INSTANCE.event("INFO", "车机蓝牙已连接，但这台车还没有成功记录，等待手动完成首次连接");
                        return;
                    }
                    long backoff = AutoConnectCoordinator.INSTANCE.backoffRemaining(peer);
                    if (backoff > 0) {
                        CastLog.INSTANCE.event("INFO", "忽略自动连接：上一次会话失败，冷却剩余 " + ((backoff / 1000) + 1) + "s");
                        return;
                    }
                    if (CastService.INSTANCE.isBusy()) {
                        CastLog.INSTANCE.event("INFO", "忽略重复的车机蓝牙连接广播：投屏会话仍在运行");
                        return;
                    }
                    if (!AutoConnectCoordinator.INSTANCE.claim(peer)) {
                        CastLog.INSTANCE.event("INFO", "忽略重复的车机蓝牙连接广播：会话启动请求已在处理中");
                        return;
                    }
                    CastLog.INSTANCE.event("INFO", "车机蓝牙已连接，自动开始投屏");
                    Intent request = new Intent(context, (Class<?>) CastService.class).setAction(CastService.ACTION_START).putExtra(CastService.EXTRA_AUTO_START, true);
                    Intrinsics.checkNotNullExpressionValue(request, "putExtra(...)");
                    try {
                        Result.Companion companion2 = Result.INSTANCE;
                        BluetoothArrivalReceiver bluetoothArrivalReceiver = this;
                        ContextCompat.startForegroundService(context, request);
                        objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
                    } catch (Throwable th) {
                        Result.Companion companion3 = Result.INSTANCE;
                        objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
                    }
                    Throwable thM162exceptionOrNullimpl = Result.m162exceptionOrNullimpl(objM159constructorimpl);
                    if (thM162exceptionOrNullimpl != null) {
                        AutoConnectCoordinator.INSTANCE.release(peer);
                        AutoConnectCoordinator.recordFailure$default(AutoConnectCoordinator.INSTANCE, peer, 0L, 2, null);
                        CastLog.INSTANCE.event("WARN", "自动开始失败：" + thM162exceptionOrNullimpl.getMessage());
                    }
                }
            }
        }
    }
}
