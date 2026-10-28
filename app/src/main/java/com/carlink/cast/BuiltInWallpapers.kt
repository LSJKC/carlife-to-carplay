package com.carlink.cast

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

/**
 * Bundled panel wallpapers, so the outer frame is filled without the driver
 * having to find a 1972×520 image first.
 *
 * They live in `assets/wallpapers/` as JPEG at the panel's proportions. Nothing
 * here becomes the active wallpaper by itself: [apply] decodes, scales and
 * crops to the exact panel size and then hands the bitmap to
 * [PanelWallpaperStore], which keeps the previous wallpaper if the write fails.
 */
object BuiltInWallpapers {

    data class Entry(val asset: String, val label: String)

    /*
     * Order is deliberate: the calm, low-contrast images first. The outer frame
     * sits behind CarLife's UI, so a busy wallpaper reads as visual noise at
     * the edges of the driver's peripheral vision.
     */
    val entries = listOf(
        Entry("wallpapers/aurora.jpg", "幻彩流光"),
        Entry("wallpapers/planets.jpg", "紫色星海"),
        Entry("wallpapers/autumn_night.jpg", "深蓝秋影"),
        Entry("wallpapers/autumn_day.jpg", "浅色秋影"),
        Entry("wallpapers/ducks_day.jpg", "晴日小鸭"),
        Entry("wallpapers/ducks_night.jpg", "夜色小鸭"),
        Entry("wallpapers/rainy_football.jpg", "雨中足球"),
        Entry("wallpapers/mermaid.jpg", "深海人鱼"),
        Entry("wallpapers/golden_mountains.jpg", "赤金山河"),
    )

    /**
     * Decodes one entry straight into a panel-sized bitmap.
     *
     * `inSampleSize` is chosen from the *smaller* dimension: these images are
     * already wider than tall, and sampling by the larger one would under-sample
     * the height the crop keeps.
     */
    fun load(context: Context, entry: Entry): Bitmap? = runCatching {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.assets.open(entry.asset).use { BitmapFactory.decodeStream(it, null, bounds) }
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

        val targetWidth = CarPanelGeometry.PANEL_WIDTH
        val targetHeight = CarPanelGeometry.PANEL_HEIGHT
        var sample = 1
        while (bounds.outHeight / (sample * 2) >= targetHeight &&
            bounds.outWidth / (sample * 2) >= targetWidth
        ) {
            sample *= 2
        }
        val decoded = context.assets.open(entry.asset).use { stream ->
            BitmapFactory.decodeStream(
                stream, null,
                BitmapFactory.Options().apply { inSampleSize = sample; inPreferredConfig = Bitmap.Config.ARGB_8888 },
            )
        } ?: return null
        stretchToPanel(decoded)
    }.getOrNull()

    /**
     * Fills the panel from the centre of the source.
     *
     * Aspect-correct crop rather than a stretch: these are photographs, and
     * squashing them to 1972×520 would be immediately visible on the 30-inch
     * panel. The crop keeps the vertical centre, which is where a wallpaper's
     * subject normally sits.
     */
    private fun stretchToPanel(source: Bitmap): Bitmap {
        val targetWidth = CarPanelGeometry.PANEL_WIDTH
        val targetHeight = CarPanelGeometry.PANEL_HEIGHT
        if (source.width == targetWidth && source.height == targetHeight) return source
        val scale = maxOf(
            targetWidth.toFloat() / source.width,
            targetHeight.toFloat() / source.height,
        )
        val scaledWidth = (source.width * scale).toInt().coerceAtLeast(targetWidth)
        val scaledHeight = (source.height * scale).toInt().coerceAtLeast(targetHeight)
        val scaled = Bitmap.createScaledBitmap(source, scaledWidth, scaledHeight, true)
        val x = ((scaledWidth - targetWidth) / 2).coerceAtLeast(0)
        val y = ((scaledHeight - targetHeight) / 2).coerceAtLeast(0)
        val cropped = Bitmap.createBitmap(scaled, x, y, targetWidth, targetHeight)
        if (scaled !== source && !scaled.isRecycled && scaled !== cropped) scaled.recycle()
        if (source !== scaled && source !== cropped && !source.isRecycled) source.recycle()
        return cropped
    }

    /** Decodes, crops and installs one entry. Returns false when anything fails. */
    fun apply(context: Context, entry: Entry): Boolean {
        val bitmap = load(context, entry) ?: return false
        return try {
            PanelWallpaperStore.setFromBitmap(context, bitmap)
        } finally {
            if (!bitmap.isRecycled) bitmap.recycle()
        }
    }
}
