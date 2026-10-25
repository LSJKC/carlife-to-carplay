package com.carlink.cast.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.view.View
import com.carlink.cast.CastStage
import com.carlink.cast.CastState
import kotlin.math.cos
import kotlin.math.sin

/**
 * The visual anchor of the main screen: one ring that carries the connection
 * ladder. Eight faint ticks mark the eight stages, the arc fills as the phone
 * walks them, and the centre stays quiet — one title, one line of context.
 */
internal class StageRingView(context: Context) : View(context) {

    private val density = resources.displayMetrics.density
    private val stroke = 10f * density
    private val track = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = stroke
        strokeCap = Paint.Cap.ROUND
        color = Palette.TRACK
    }
    private val progress = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = stroke
        strokeCap = Paint.Cap.ROUND
    }
    private val glow = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = stroke * 2.4f
        strokeCap = Paint.Cap.ROUND
        alpha = 26
    }
    private val label = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = android.graphics.Typeface.create("sans-serif-medium", android.graphics.Typeface.BOLD)
        textSize = 27f * density
        color = Palette.TEXT_PRIMARY
    }
    private val caption = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.NORMAL)
        textSize = 12.5f * density
        color = Palette.TEXT_SECONDARY
    }
    private val tick = Paint(Paint.ANTI_ALIAS_FLAG)
    private val dot = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bounds = RectF()

    /** The furthest stage reached, kept by the screen for failure rendering. */
    var failedStep: Int = 0

    var state: CastState = CastState()
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val size = minOf(width, height).toFloat()
        val inset = stroke * 2.1f
        bounds.set(
            (width - size) / 2 + inset,
            (height - size) / 2 + inset,
            (width + size) / 2 - inset,
            (height + size) / 2 - inset,
        )

        canvas.drawArc(bounds, 0f, 360f, false, track)

        val failed = state.stage == CastStage.FAILED
        val fraction = when {
            failed -> (failedStep.coerceIn(0, 8)) / 8f
            state.stage == CastStage.STOPPED -> if (state.frames > 0) 1f else 0f
            else -> state.stage.step / CastStage.STREAMING.step.toFloat()
        }
        val active = when {
            failed -> Palette.DANGER
            state.stage == CastStage.STREAMING -> Palette.SUCCESS
            state.stage == CastStage.STOPPED && state.frames > 0 -> Palette.TEXT_TERTIARY
            else -> Palette.ACCENT
        }

        if (fraction > 0f) {
            val sweep = SweepGradient(
                width / 2f, height / 2f,
                intArrayOf(active, Palette.CYAN, active),
                floatArrayOf(0f, 0.55f, 1f),
            )
            progress.shader = sweep
            glow.shader = sweep
            glow.color = active
            val degrees = 360f * fraction
            canvas.drawArc(bounds, -90f, degrees, false, glow)
            canvas.drawArc(bounds, -90f, degrees, false, progress)
        }

        drawTicks(canvas, fraction, failed)

        val title = when {
            failed -> "已断开"
            state.stage == CastStage.STREAMING -> "投屏中"
            state.stage == CastStage.STOPPED -> if (state.frames > 0) "已结束" else "准备就绪"
            else -> state.stage.title
        }
        canvas.drawText(title, width / 2f, height / 2f + 3f * density, label)

        val contextLine = when {
            failed -> "第 ${failedStep.coerceAtLeast(1)} / 8 步中断"
            state.stage == CastStage.STOPPED ->
                if (state.frames > 0) "本次会话已结束" else "等待开始"
            else -> "第 ${state.stage.step} / ${CastStage.STREAMING.step} 步"
        }
        canvas.drawText(contextLine, width / 2f, height / 2f + 27f * density, caption)

        if (state.running && !failed) {
            val angle = (System.currentTimeMillis() % 2400) / 2400f * 360f - 90f
            val radius = bounds.width() / 2f
            val cx = width / 2f + radius * cos(Math.toRadians(angle.toDouble())).toFloat()
            val cy = height / 2f + radius * sin(Math.toRadians(angle.toDouble())).toFloat()
            dot.color = Palette.CYAN
            canvas.drawCircle(cx, cy, stroke * 0.5f, dot)
            postInvalidateDelayed(16)
        }
    }

    private fun drawTicks(canvas: Canvas, fraction: Float, failed: Boolean) {
        val radius = bounds.width() / 2f
        val reached = Math.round(fraction * CastStage.ladder.size)
        CastStage.ladder.forEachIndexed { index, _ ->
            val angle = Math.toRadians((-90f + index * 360f / CastStage.ladder.size).toDouble())
            val cx = width / 2f + radius * cos(angle).toFloat()
            val cy = height / 2f + radius * sin(angle).toFloat()
            tick.color = when {
                failed && index + 1 == failedStep -> Palette.DANGER
                index + 1 <= reached -> Palette.ACCENT
                else -> Palette.TRACK
            }
            canvas.drawCircle(cx, cy, stroke * 0.34f, tick)
        }
    }
}
