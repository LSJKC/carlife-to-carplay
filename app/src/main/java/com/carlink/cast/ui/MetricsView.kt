package com.carlink.cast.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import android.view.View
import com.carlink.cast.CastMetric

/**
 * A compact sparkline for the frame-rate card. It answers one question at a
 * glance — is the stream keeping up — and stays out of the way otherwise.
 */
internal class MetricsView(context: Context) : View(context) {

    private val density = resources.displayMetrics.density
    private val line = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1.6f * density
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        color = Palette.ACCENT
    }
    private val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val baseline = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Palette.TRACK
        strokeWidth = 1f * density
    }
    private val idle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Palette.TRACK
        strokeWidth = 1.6f * density
        strokeCap = Paint.Cap.ROUND
    }

    var samples: List<CastMetric> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val left = density
        val right = width - density
        val top = 3f * density
        val bottom = height - 2f * density
        if (samples.size < 2) {
            val y = (top + bottom) / 2f
            canvas.drawLine(left, y, right, y, idle)
            return
        }
        canvas.drawLine(left, bottom, right, bottom, baseline)
        val max = samples.maxOf { it.fps }.coerceAtLeast(1.0)
        val path = Path()
        val area = Path()
        samples.forEachIndexed { index, sample ->
            val x = left + index * (right - left) / (samples.size - 1)
            val y = bottom - (sample.fps / max * (bottom - top)).toFloat()
            if (index == 0) {
                path.moveTo(x, y)
                area.moveTo(x, bottom)
                area.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                area.lineTo(x, y)
            }
        }
        area.lineTo(right, bottom)
        area.close()
        fill.shader = LinearGradient(
            0f, top, 0f, bottom,
            0x554C8DFF, 0x004C8DFF, Shader.TileMode.CLAMP,
        )
        canvas.drawPath(area, fill)
        canvas.drawPath(path, line)
    }
}
