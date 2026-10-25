package com.carlink.cast

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings

/**
 * The phone's own Bluetooth address, which 0x4300 must carry.
 *
 * Android 6 hides the real adapter address from apps; the secure setting is
 * readable on many vendor builds, and a user-supplied value is the last resort.
 * A placeholder such as 02:00:00:00:00:00 is rejected by the head unit, so it
 * is treated as missing rather than sent.
 */
internal object LocalBluetoothAddress {
    private val pattern = Regex("(?:[0-9A-F]{2}:){5}[0-9A-F]{2}")
    private val reserved = setOf("02:00:00:00:00:00", "00:00:00:00:00:00", "FF:FF:FF:FF:FF:FF")

    fun normalize(value: String?): String? {
        val address = value?.trim()?.uppercase() ?: return null
        return address.takeIf { pattern.matches(it) && it !in reserved }
    }

    @SuppressLint("HardwareIds", "MissingPermission")
    fun read(context: Context, adapter: BluetoothAdapter, manual: String?): String? {
        val canRead = Build.VERSION.SDK_INT < 31 ||
            context.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        if (canRead) {
            val fromAdapter = runCatching { adapter.address }.getOrNull()
            normalize(fromAdapter)?.let { return it }
        }
        val secure = runCatching {
            Settings.Secure.getString(context.contentResolver, "bluetooth_address")
        }.getOrNull()
        normalize(secure)?.let { return it }
        return normalize(manual)
    }
}
