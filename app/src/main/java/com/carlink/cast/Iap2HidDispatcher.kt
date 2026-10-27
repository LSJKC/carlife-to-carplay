package com.carlink.cast

import com.carplay.adapter.PhoneProjectionAdapter
import com.carplay.iap2.body.Iap2BodyReader
import com.carplay.iap2.message.Iap2Messages
import com.carplay.iap2.wire.Iap2Frame
import com.carplay.sender.HidInput
import com.carplay.sender.HidKeys
import com.carplay.sender.SenderDisplay
import java.util.concurrent.ConcurrentHashMap

/**
 * Shared iAP2 HID path for the Bluetooth bootstrap and the Wi-Fi command link.
 *
 * The head unit may advertise a descriptor on Bluetooth and deliver reports
 * after the AirPlay handover. Keeping the device table at session scope avoids
 * losing that descriptor when the transport changes.
 */
internal class Iap2HidDispatcher(
    private val source: PhoneProjectionAdapter,
    private val log: (String) -> Unit,
) {
    private data class Device(
        val id: Int,
        val width: Int?,
        val height: Int?,
        val descriptor: ByteArray,
        val keys: HidKeys?,
        val touch: HidInput?,
    )

    private val devices = ConcurrentHashMap<Int, Device>()

    /**
     * One line for the session snapshot.
     *
     * The whole input chain (touch, media keys, phone and voice buttons) is
     * dead until the car sends 0x6800 and we answer it. Recording the count and
     * what each descriptor carried is what tells "the car never asked" apart
     * from "the car asked and the descriptor was unusable".
     */
    fun describe(): String {
        if (devices.isEmpty()) return "iAP2-HID=未注册（0x6800 未到达）"
        return "iAP2-HID=" + devices.values.joinToString(",") { d ->
            "dev=${d.id} ${d.width ?: "?"}x${d.height ?: "?"} " +
                "desc=${d.descriptor.size}B keys=${d.keys != null} touch=${d.touch != null}"
        }
    }

    @Volatile
    private var display: SenderDisplay? = null

    fun setDisplay(value: SenderDisplay?) {
        display = value
    }

    /**
     * Handles one accessory-to-phone HID frame.
     *
     * A malformed or unknown report is consumed and logged. Returning true is
     * deliberate: rejecting an input frame makes some head units retry it and
     * eventually tear down the entire control channel.
     */
    fun handle(frame: Iap2Frame, send: (Iap2Frame) -> Unit): Boolean {
        return try {
            when (frame.messageId) {
                0x6800 -> start(frame, send)
                0x6802 -> report(frame)
                0x6803 -> stop(frame)
                else -> false
            }
        } catch (failure: Throwable) {
            log("iAP2 HID 0x${frame.messageId.toString(16)} 处理失败：${failure.message}")
            true
        }
    }

    private fun start(frame: Iap2Frame, send: (Iap2Frame) -> Unit): Boolean {
        val body = Iap2BodyReader.of(frame)
        val id = body.optionalU16(0) ?: 0
        val width = body.optionalU16(1)
        val height = body.optionalU16(2)
        val descriptor = body.list()
            .asSequence()
            // 0x0004 is the normal HID descriptor parameter. Prefer it, but
            // do not assume every vendor firmware keeps that numbering.
            .sortedWith(compareByDescending<com.carplay.iap2.wire.Iap2Parameter> { it.id == 4 }
                .thenByDescending { it.payload.size })
            .map { it.payload }
            .filter { it.isNotEmpty() }
            .mapNotNull { candidate ->
                val keys = runCatching { HidKeys(candidate) }.getOrNull()
                val touch = runCatching { HidInput(candidate).takeIf { it.isTouch } }.getOrNull()
                if (keys != null || touch != null) DescriptorCandidate(candidate, keys, touch) else null
            }
            .firstOrNull()
        val descriptorBytes = descriptor?.bytes
            ?: body.optionalBytes(4)
            ?: body.list().maxByOrNull { it.payload.size }?.payload
            ?: ByteArray(0)
        val keys = descriptor?.keys
        val touch = descriptor?.touch
        // Keep the device entry even when the descriptor is malformed. Some
        // head units resend StartHID after the first report; retaining the
        // entry gives that retry a chance to replace the decoder instead of
        // making all later reports look like an unknown device forever.
        devices[id] = Device(id, width, height, descriptorBytes.copyOf(), keys, touch)
        val extent = touch?.touchExtent
        log(
            "iAP2 HID 0x6800：device=$id ${width ?: "?"}×${height ?: "?"} " +
                "descriptor=${descriptorBytes.size}B " +
                "keys=${keys != null} touch=${touch != null}" +
                (extent?.let { " extent=${it.first}×${it.second}" } ?: "") +
                if (descriptor == null) "（描述符待重试）" else "",
        )
        // Buick-E5 reference and the 1.2.7 trace both use status u16(0)=0
        // plus a one-byte zero parameter for DeviceHIDReport.
        send(Iap2Messages.buildRaw(0x6801) {
            u16(0, 0)
            bytes(1, byteArrayOf(0))
        })
        return true
    }

    private fun report(frame: Iap2Frame): Boolean {
        val body = Iap2BodyReader.of(frame)
        val id = body.optionalU16(0) ?: 0
        val nonEmpty = body.list().filter { it.payload.isNotEmpty() }
        val report = body.optionalBytes(HID_REPORT_DATA_PARAM)
            ?: body.list()
                .asSequence()
                .filter { it.id != 0 && it.payload.isNotEmpty() }
                .maxByOrNull { it.payload.size }
                ?.payload
            ?: nonEmpty.lastOrNull()?.payload
            ?: ByteArray(0)
        // A few firmware revisions omit the device id on a one-device
        // session. Falling back to the only cached decoder preserves the
        // report without weakening multi-device routing.
        val device = devices[id] ?: devices.values.singleOrNull()
        if (device == null) {
            log(
                "iAP2 HID 0x6802：未知 device=$id report=${report.size}B " +
                    report.joinToString(" ") { "%02X".format(it) },
            )
            return true
        }
        when {
            device.keys != null -> {
                val parsed = runCatching { device.keys.report(report) }.getOrNull()
                if (parsed == null) {
                    log("iAP2 HID 按键报告无法解析：device=$id ${report.size}B")
                } else {
                    Dispatch.input(mapOf("type" to "keySamples", "report" to parsed), source, display)
                }
            }
            device.touch != null -> {
                val samples = device.touch.touches(report)
                if (samples.isEmpty()) {
                    log("iAP2 HID 触摸报告为空：device=$id ${report.size}B")
                } else {
                    Dispatch.input(mapOf("type" to "touchSamples", "samples" to samples), source, display)
                }
            }
            else -> log("iAP2 HID：device=$id 没有可用解析器")
        }
        return true
    }

    private fun stop(frame: Iap2Frame): Boolean {
        val id = Iap2BodyReader.of(frame).optionalU16(0)
        if (id == null) {
            devices.clear()
            log("iAP2 HID 0x6803：停止全部设备")
        } else {
            devices.remove(id)
            log("iAP2 HID 0x6803：停止 device=$id")
        }
        return true
    }

    fun reset() {
        devices.clear()
        display = null
    }

    private data class DescriptorCandidate(
        val bytes: ByteArray,
        val keys: HidKeys?,
        val touch: HidInput?,
    )

    private companion object {
        const val HID_REPORT_DATA_PARAM = 1
    }
}
