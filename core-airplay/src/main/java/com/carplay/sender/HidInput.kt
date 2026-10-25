package com.carplay.sender

/**
 * One touch point.
 *
 * [x] and [y] are normalised to the descriptor's logical extent. [maxX]/[maxY]
 * are that extent in the car's own units, which is not necessarily the video
 * size: this head unit reports touches across the whole physical panel while
 * the CarPlay video occupies only part of it, so scaling by the video width
 * shifts every touch towards the left edge.
 */
data class TouchSample(
    val id: Int,
    val x: Float,
    val y: Float,
    val down: Boolean,
    val maxX: Int = 0,
    val maxY: Int = 0,
)
data class HidField(val page: Int, val usage: Int, val offset: Int, val size: Int, val maximum: Int)

/** Reads the advertised descriptor so report offsets and coordinate ranges are negotiated. */
class HidInput(descriptor: ByteArray) {
    private val fields = mutableListOf<HidField>()
    private var bits = 0
    private var reportId: Int? = null
    val isTouch get() = fields.any { it.page == 0x0d && it.usage == 0x33 } && fields.any { it.page == 1 && it.usage == 0x30 }
    init {
        require(descriptor.size in 1..8192)
        var cursor = 0; var page = 0; var reportSize = 0; var reportCount = 0; var maximum = 0
        val usages = mutableListOf<Int>()
        while (cursor < descriptor.size) {
            val prefix = descriptor[cursor++].toInt() and 255
            require(prefix != 0xfe) { "不支持扩展 HID 项" }
            val size = when (prefix and 3) { 3 -> 4; else -> prefix and 3 }
            require(cursor + size <= descriptor.size)
            var value = 0; repeat(size) { value = value or ((descriptor[cursor++].toInt() and 255) shl (8 * it)) }
            when (prefix and 0xfc) {
                0x04 -> page = value
                0x08 -> usages += value
                0x24 -> maximum = value
                0x74 -> reportSize = value
                0x94 -> reportCount = value
                0x84 -> {
                    require(size == 1) { "Report ID 必须是 1 字节" }
                    val id = value and 0xff
                    require(reportId == null || reportId == id) {
                        "不支持多个 Report ID 的输入设备"
                    }
                    reportId = id
                }
                0x80 -> {
                    require(reportSize in 1..32 && reportCount in 1..64 && bits + reportSize * reportCount <= 4096)
                    repeat(reportCount) { slot ->
                        if (value and 1 == 0) fields += HidField(page,usages.getOrElse(slot) { usages.lastOrNull() ?: 0 },bits,reportSize,maximum)
                        bits += reportSize
                    }
                    usages.clear()
                }
                0xa0, 0xc0 -> usages.clear()
            }
        }
    }
    /**
     * Decodes one touch report.
     *
     * The report is read field by field and every field that does not fit
     * inside the bytes actually received is skipped, rather than the whole
     * report being rejected. This car sends a 5-byte report for a descriptor
     * whose two finger records span more than that; requiring the full width
     * threw on *every* touch report, and the exception took the command loop
     * with it, so the driver's taps never reached the phone at all.
     */
    fun touches(report: ByteArray): List<TouchSample> {
        if (report.isEmpty() || report.size > 512) return emptyList()
        val payload =
            if (reportId == null) {
                report
            } else {
                if ((report[0].toInt() and 0xff) != reportId) return emptyList()
                report.copyOfRange(1, report.size)
            }
        val available = payload.size * 8
        val result = mutableListOf<TouchSample>()
        var id = 0; var down = false
        var xRaw = 0; var xMax = 1; var hasX = false
        for (field in fields) {
            if (field.offset + field.size > available) continue
            val value = read(payload,field)
            when {
                field.page == 0x0d && field.usage == 0x38 -> id = value
                field.page == 0x0d && field.usage == 0x33 -> down = value != 0
                field.page == 1 && field.usage == 0x30 -> {
                    xRaw = value
                    xMax = if (field.maximum > 0) field.maximum else 1
                    hasX = true
                }
                field.page == 1 && field.usage == 0x31 -> {
                    if (!hasX) continue
                    val yMax = if (field.maximum > 0) field.maximum else 1
                    result += TouchSample(
                        id, xRaw.toFloat() / xMax, value.toFloat() / yMax, down,
                        xMax, yMax,
                    )
                    hasX = false
                }
            }
        }
        return result
    }

    /** The panel extent the car reports touches in, or null when it never declared one. */
    val touchExtent: Pair<Int, Int>?
        get() {
            val width = fields.firstOrNull { it.page == 1 && it.usage == 0x30 }?.maximum ?: return null
            val height = fields.firstOrNull { it.page == 1 && it.usage == 0x31 }?.maximum ?: return null
            return if (width > 0 && height > 0) width to height else null
        }
    private fun read(bytes: ByteArray, field: HidField): Int {
        var value = 0
        repeat(field.size) { bit -> val index = field.offset + bit; value = value or (((bytes[index/8].toInt() ushr (index%8)) and 1) shl bit) }
        return value
    }
}

data class CaptureGeometry(val sourceWidth: Int, val sourceHeight: Int, val width: Int, val height: Int) {
    init { require(sourceWidth > 0 && sourceHeight > 0 && width > 0 && height > 0) }
    fun point(x: Float, y: Float): Pair<Float,Float>? {
        val scale = minOf(width.toFloat()/sourceWidth,height.toFloat()/sourceHeight)
        val left = (width-sourceWidth*scale)/2; val top = (height-sourceHeight*scale)/2
        val mappedX = (x-left)/scale; val mappedY = (y-top)/scale
        if (mappedX !in 0f..sourceWidth.toFloat() || mappedY !in 0f..sourceHeight.toFloat()) return null
        return mappedX to mappedY
    }
}
