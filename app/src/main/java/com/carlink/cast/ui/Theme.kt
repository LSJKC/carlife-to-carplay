package com.carlink.cast.ui

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.carlink.cast.R

/** Small helpers so every screen shares one visual language. */
internal object Ui {
    fun Context.dp(value: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics).toInt()

    fun Context.color(res: Int): Int = ContextCompat.getColor(this, res)

    /** A soft pill used for chips, segments and secondary actions. */
    fun Context.pill(fill: Int): GradientDrawable =
        GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dp(999f).toFloat()
            setColor(color(fill))
        }

    /** A rounded panel used as a dialog background. */
    fun Context.panel(fill: Int, stroke: Int, radiusDp: Float): GradientDrawable =
        GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = dp(radiusDp).toFloat()
            setColor(color(fill))
            setStroke(dp(1f), color(stroke))
        }

    fun TextView.style(size: Float, colorRes: Int, bold: Boolean = false, mono: Boolean = false) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        setTextColor(context.color(colorRes))
        typeface = when {
            mono -> Typeface.MONOSPACE
            bold -> Typeface.create("sans-serif-medium", Typeface.BOLD)
            else -> Typeface.create("sans-serif", Typeface.NORMAL)
        }
    }
}

/** Static geometry shared by the ring and the metric cards. */
internal object Palette {
    const val ACCENT = 0xFF4C8DFF.toInt()
    const val CYAN = 0xFF2EE6D6.toInt()
    const val SUCCESS = 0xFF35D07F.toInt()
    const val WARNING = 0xFFF5A623.toInt()
    const val DANGER = 0xFFFF5C7A.toInt()
    const val TRACK = 0xFF1B2A45.toInt()
    const val TEXT_PRIMARY = 0xFFEAF1FF.toInt()
    const val TEXT_SECONDARY = 0xFF93A4C3.toInt()
    const val TEXT_TERTIARY = 0xFF63769A.toInt()
}
