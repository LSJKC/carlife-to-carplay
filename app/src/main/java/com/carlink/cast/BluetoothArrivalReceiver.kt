package com.carlink.cast

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

/**
 * Starts a session when the car's Bluetooth link comes up.
 *
 * This is the whole mechanism behind "get in and it just connects": the head
 * unit raises its ACL link as soon as the phone is in range, Android broadcasts
 * `ACL_CONNECTED` for it, and a session is started without the user touching
 * anything. The app therefore has to be resident but idle, which is what the
 * manifest receiver provides.
 *
 * The broadcast is only a trigger. A source that needs a fresh screen-capture
 * grant cannot be started from here, so the receiver declines rather than
 * launching an activity behind the driver's back.
 */
class BluetoothArrivalReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        val device: BluetoothDevice = if (android.os.Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        } ?: return
        if (action == BluetoothDevice.ACTION_ACL_DISCONNECTED) {
            CastService.onBluetoothDisconnected(context, device.address)
            return
        }
        if (action != BluetoothDevice.ACTION_ACL_CONNECTED) return
        if (androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.BLUETOOTH_CONNECT,
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) return
        val settings = CastSettings(context)
        val peer = settings.peerAddress ?: return
        if (!device.address.equals(peer, ignoreCase = true)) return
        if (device.bondState != BluetoothDevice.BOND_BONDED) return
        /*
         * These are deliberately two separate policies:
         *
         *   - CarLife follows the car's Bluetooth presence once paired;
         *   - screen mirroring still follows the old autoConnect switch.
         *
         * Toggling the mirror setting must therefore never disable CarLife's
         * automatic start/stop path.
         */
        if (settings.sourceMode == SourceMode.MIRROR) {
            if (!settings.autoConnect) return
            // A background receiver cannot obtain a fresh MediaProjection
            // grant. Do not fall through to the CarLife start path merely
            // because CarLife happens to be installed on the phone.
            CastLog.event("INFO", "车机蓝牙已连接，但屏幕镜像需要前台录屏授权，未自动开始")
            return
        }
        if (!CastEngine.carLifeAvailable(context)) return
        if (!settings.carLifeAutoSession) {
            CastLog.event("INFO", "车机蓝牙已连接，但 CarLife 自动跟车已关闭")
            return
        }

        // Only a car that has already shown a picture can be started from here.
        //
        // The first connection to any car is granted inside Honor's Car Connect
        // flow, and those activities are exported=false, so a background
        // receiver cannot reproduce it. Starting anyway would burn a full
        // handshake and leave the driver with a failure notification instead of
        // the one page they actually need; the foreground path handles that case
        // and tells them where to go.
        if (!settings.hasConnected(peer)) {
            CastLog.event("INFO", "车机蓝牙已连接，但这台车还没有成功记录，等待手动完成首次连接")
            return
        }
        val backoff = AutoConnectCoordinator.backoffRemaining(peer)
        if (backoff > 0L) {
            CastLog.event("INFO", "忽略自动连接：上一次会话失败，冷却剩余 ${backoff / 1000L + 1L}s")
            return
        }

        // CarLife renders and encodes its own car UI, so it needs nothing from
        // the user. Mirroring would need a capture grant that only an activity
        // can request, and silently popping one over the car UI is wrong.
        if (CastService.isBusy()) {
            CastLog.event("INFO", "忽略重复的车机蓝牙连接广播：投屏会话仍在运行")
            return
        }
        if (!AutoConnectCoordinator.claim(peer)) {
            CastLog.event("INFO", "忽略重复的车机蓝牙连接广播：会话启动请求已在处理中")
            return
        }
        CastLog.event("INFO", "车机蓝牙已连接，自动开始投屏")
        val request = Intent(context, CastService::class.java)
            .setAction(CastService.ACTION_START)
            .putExtra(CastService.EXTRA_AUTO_START, true)
        runCatching { ContextCompat.startForegroundService(context, request) }
            .onFailure {
                AutoConnectCoordinator.release(peer)
                AutoConnectCoordinator.recordFailure(peer)
                CastLog.event("WARN", "自动开始失败：${it.message}")
            }
    }
}
