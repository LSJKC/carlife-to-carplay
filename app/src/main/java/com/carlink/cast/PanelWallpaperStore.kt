package com.carlink.cast

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.floor

object PanelWallpaperStore {
    private const val WALLPAPER_FILE = "panel-wallpaper.png"
    private val revisionCounter = AtomicLong(0)

    val revision: Long get() = revisionCounter.get()

    fun hasCustomWallpaper(context: Context): Boolean =
        wallpaperFile(context).let { it.exists() && it.length() > 0L }

    fun decodeUri(context: Context, uri: Uri, maxSize: Int = 3072): Bitmap? = runCatching {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            val longest = maxOf(info.size.width, info.size.height)
            if (longest > maxSize) {
                // ImageDecoder's sample size is a divisor. Rounding up would
                // decode below the requested canvas and then enlarge it in GL,
                // which made a sharp user wallpaper visibly soft.
                decoder.setTargetSampleSize(
                    floor(longest.toDouble() / maxSize).toInt().coerceAtLeast(1),
                )
            }
        }
    }.getOrNull()?.takeIf { it.width > 0 && it.height > 0 }

    fun setFromBitmap(context: Context, bitmap: Bitmap): Boolean = runCatching {
        require(bitmap.width == CarPanelGeometry.PANEL_WIDTH && bitmap.height == CarPanelGeometry.PANEL_HEIGHT) {
            "壁纸裁剪输出必须为 ${CarPanelGeometry.PANEL_WIDTH}x${CarPanelGeometry.PANEL_HEIGHT}"
        }
        val target = wallpaperFile(context)
        val temporary = File(context.filesDir, "$WALLPAPER_FILE.tmp")
        try {
            temporary.outputStream().buffered().use { output ->
                check(bitmap.compress(Bitmap.CompressFormat.PNG, 100, output))
            }
            runCatching {
                Files.move(
                    temporary.toPath(),
                    target.toPath(),
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE,
                )
            }.getOrElse {
                Files.move(temporary.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
            revisionCounter.incrementAndGet()
            true
        } finally {
            temporary.delete()
        }
    }.getOrDefault(false)

    fun reset(context: Context): Boolean = runCatching {
        wallpaperFile(context).delete()
        revisionCounter.incrementAndGet()
        true
    }.getOrDefault(false)

    fun decode(context: Context, maxSize: Int = 2560): Bitmap? {
        val file = wallpaperFile(context)
        if (!file.exists() || file.length() == 0L) return null
        return decodeFile(file, maxSize)
    }

    private fun decodeFile(file: File, maxSize: Int): Bitmap? = runCatching {
        val source = ImageDecoder.createSource(file)
        ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            val longest = maxOf(info.size.width, info.size.height)
            if (longest > maxSize) {
                decoder.setTargetSampleSize(
                    floor(longest.toDouble() / maxSize).toInt().coerceAtLeast(1),
                )
            }
        }
    }.getOrNull()?.takeIf { it.width > 0 && it.height > 0 }

    private fun wallpaperFile(context: Context) = File(context.filesDir, WALLPAPER_FILE)
}
