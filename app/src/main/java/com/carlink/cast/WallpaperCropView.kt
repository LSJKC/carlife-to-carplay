package com.carlink.cast

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.graphics.createBitmap

@SuppressLint("ViewConstructor")
internal class WallpaperCropView(
    context: Context,
    private val source: Bitmap,
) : View(context) {
    private val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    private val imageDestination = RectF()
    private val framePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        alpha = 210
        style = Paint.Style.STROKE
        strokeWidth = 2f * resources.displayMetrics.density
    }
    private var transform: WallpaperCropTransform? = null
    private var minimumScale = 1f
    private var lastX = 0f
    private var lastY = 0f
    private val scaleDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val current = transform ?: return false
            transform = WallpaperCropGeometry.zoomAround(
                current = current,
                factor = detector.scaleFactor,
                focusX = detector.focusX,
                focusY = detector.focusY,
                sourceWidth = source.width,
                sourceHeight = source.height,
                viewportWidth = width,
                viewportHeight = height,
                minimumScale = minimumScale,
            )
            invalidate()
            return true
        }
    })

    init {
        isClickable = true
        contentDescription = context.getString(R.string.wallpaper_crop_preview)
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w <= 0 || h <= 0) return
        val initial = WallpaperCropGeometry.cover(source.width, source.height, w, h)
        transform = initial
        minimumScale = initial.scale
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (width <= 0 || height <= 0) return
        val current = transform ?: WallpaperCropGeometry.cover(source.width, source.height, width, height)
        val right = current.left + source.width * current.scale
        val bottom = current.top + source.height * current.scale
        imageDestination.set(current.left, current.top, right, bottom)
        canvas.drawBitmap(source, null, imageDestination, imagePaint)
        canvas.drawRect(
            framePaint.strokeWidth / 2f,
            framePaint.strokeWidth / 2f,
            width - framePaint.strokeWidth / 2f,
            height - framePaint.strokeWidth / 2f,
            framePaint,
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.pointerCount == 1 && !scaleDetector.isInProgress) {
                    val current = transform
                    if (current != null) {
                        transform = WallpaperCropGeometry.constrain(
                            current.copy(
                                left = current.left + event.x - lastX,
                                top = current.top + event.y - lastY,
                            ),
                            source.width,
                            source.height,
                            width,
                            height,
                        )
                        invalidate()
                    }
                }
                lastX = event.x
                lastY = event.y
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                performClick()
                return true
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    fun createCroppedBitmap(): Bitmap {
        check(width > 0 && height > 0) { "裁剪框尚未布局" }
        val current = transform ?: WallpaperCropGeometry.cover(source.width, source.height, width, height)
        val result = createBitmap(
            CarPanelGeometry.PANEL_WIDTH,
            CarPanelGeometry.PANEL_HEIGHT,
            Bitmap.Config.ARGB_8888,
        )
        val canvas = Canvas(result)
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        val scaleX = CarPanelGeometry.PANEL_WIDTH.toFloat() / width
        val scaleY = CarPanelGeometry.PANEL_HEIGHT.toFloat() / height
        val destination = RectF(
            current.left * scaleX,
            current.top * scaleY,
            (current.left + source.width * current.scale) * scaleX,
            (current.top + source.height * current.scale) * scaleY,
        )
        canvas.drawBitmap(source, null, destination, imagePaint)
        return result
    }
}
