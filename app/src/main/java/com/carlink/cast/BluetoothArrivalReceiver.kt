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
        if (intent?.action != BluetoothDevice.ACTION_ACL_CONNECTED) return
        val device: BluetoothDevice = if (android.os.Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        } ?: return
        if (androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.BLUETOOTH_CONNECT,
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) return
        val settings = CastSettings(context)
        val knownCar = device.address.equals(settings.peerAddress, ignoreCase = true) ||
            settings.hasConnected(device.address)
        if (!knownCar) return
        if (device.bondState != BluetoothDevice.BOND_BONDED) return

        // CarLife renders and encodes its own car UI, so it needs nothing from
        // the user. Mirroring would need a capture grant that only an activity
        // can request, and silently popping one over the car UI is wrong.
        val carLife = CastEngine.carLifeAvailable(context)
        /*
         * The two paths answer to different switches on purpose.
         *
         * CarLife follows `carLifeAutoSession`, the "CarLife 自动跟车" toggle;
         * mirroring follows `autoConnect`, because it needs a fresh capture
         * grant. `carLifeAutoSession` was defined, shown in settings and saved
         * for a long time without any code reading it, so turning it on did
         * nothing and the car only connected when the button was pressed.
         */
        if (!(if (carLife) settings.carLifeAutoSession else settings.autoConnect)) return
        if (!carLife) {
            CastLog.event("INFO", "车机蓝牙已连接，但屏幕镜像需要录屏授权，未自动开始")
            return
        }
        CastLog.event("INFO", "车机蓝牙已连接，自动开始投屏")
        val request = Intent(context, CastService::class.java)
            .setAction(CastService.ACTION_START)
        runCatching { ContextCompat.startForegroundService(context, request) }
            .onFailure { CastLog.event("WARN", "自动开始失败：${it.message}") }
    }
}
