package com.carlink.cast

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.carlink.cast.ui.Ui.dp
import com.carlink.cast.ui.Ui.pill
import com.carlink.cast.ui.Ui.style
import com.google.android.material.button.MaterialButton

/**
 * The settings screen, as a page of its own.
 *
 * Everything that used to live behind a gear in the corner of the main screen
 * is here now: a scrolling page with grouped sections, each control carrying a
 * full sentence about what it does and when it takes effect. The main screen
 * keeps only what a driver needs mid-session — which car, start, stop.
 *
 * Values are written straight to [CastSettings]; nothing here talks to a live
 * session, so opening settings during a projection cannot disturb it.
 */
class SettingsActivity : AppCompatActivity() {
    private lateinit var settings: CastSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = CastSettings(this)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(color(R.color.bg))
        }
        root.addView(buildTopBar())

        val scroll = ScrollView(this).apply { isFillViewport = true }
        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20f), dp(4f), dp(20f), dp(36f))
        }
        scroll.addView(page, ViewGroup.LayoutParams(-1, -2))
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        buildDisplaySection(page)
        buildAudioSection(page)
        buildInputSection(page)
        buildConnectionSection(page)

        page.addView(TextView(this).apply {
            text = getString(R.string.settings_version, BuildConfig.VERSION_NAME)
            style(10.5f, R.color.text_tertiary)
            setPadding(0, dp(24f), 0, 0)
        })
        setContentView(root)
    }

    private fun buildTopBar(): View = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setPadding(dp(20f), dp(16f), dp(20f), dp(8f))
        addView(TextView(this@SettingsActivity).apply {
            text = getString(R.string.settings_title)
            style(22f, R.color.text_primary, bold = true)
        }, LinearLayout.LayoutParams(0, -2, 1f))
        addView(TextView(this@SettingsActivity).apply {
            text = "完成"
            style(13f, R.color.accent, bold = true)
            setPadding(dp(14f), dp(8f), dp(14f), dp(8f))
            background = pill(R.color.surface_high)
            isClickable = true
            setOnClickListener { finish() }
        })
    }

    // ---- sections -------------------------------------------------------

    private fun buildDisplaySection(page: LinearLayout) {
        page.addView(sectionHeading(
            getString(R.string.settings_section_display),
            "控制画面在车机上的大小与边距。改动在下次连接时生效。",
        ))

        page.addView(slider(
            title = "左侧安全留边",
            detail = "在车机可用区域内再往右让出一点空白，避免被车机自己的图标挡住。背景仍然铺满整块屏。",
            value = settings.safeLeftPercent,
            min = 0,
            max = 20,
            suffix = "%",
        ) { settings.safeLeftPercent = it })

        page.addView(slider(
            title = "右侧安全留边",
            detail = "同上，作用在右侧。",
            value = settings.safeRightPercent,
            min = 0,
            max = 20,
            suffix = "%",
        ) { settings.safeRightPercent = it })

        page.addView(slider(
            title = "投屏清晰度",
            detail = "100% 是车机原生分辨率。调低画面更省性能但会变软，调高更清晰但更吃带宽。" +
                "（此项暂未生效：改动编码尺寸会被部分车机判为不匹配而拒绝连接，等验证后再开启。）",
            value = settings.displayScalePercent,
            min = 30,
            max = 160,
            suffix = "%",
        ) { settings.displayScalePercent = it })

        page.addView(choiceRow(
            title = "目标帧率",
            detail = "60fps 更流畅，但需要手机和无线都跟得上。车机声明上限时以车机为准。",
            options = listOf(30 to "30 fps", 60 to "60 fps"),
            current = settings.frameRateOverride.takeIf { it > 0 } ?: 30,
        ) { settings.frameRateOverride = it })
    }

    private fun buildAudioSection(page: LinearLayout) {
        page.addView(sectionHeading(
            getString(R.string.settings_section_audio),
            "声音是否从车机播放。",
        ))
        page.addView(toggleRow(
            title = getString(R.string.settings_audio),
            detail = getString(R.string.settings_audio_detail),
            value = settings.audio,
        ) { settings.audio = it })
    }

    private fun buildInputSection(page: LinearLayout) {
        page.addView(sectionHeading(
            getString(R.string.settings_section_input),
            "车机屏幕上的触摸如何传回手机。",
        ))
        page.addView(slider(
            title = "触摸响应间隔",
            detail = "拖动手指时，两次上报之间至少隔这么久。设为 0 表示每一次都发。" +
                "画面流畅但触摸跟不上时，适当调大。",
            value = settings.touchMoveThrottleMs,
            min = 0,
            max = 48,
            suffix = " ms",
        ) { settings.touchMoveThrottleMs = it })

        page.addView(actionRow(
            title = "触摸服务",
            detail = "系统需要开启无障碍服务，车机上的触摸才能被本应用收到。",
            action = "去开启",
        ) { openAccessibility() })

        page.addView(actionRow(
            title = "通知使用权",
            detail = "读取车机上的媒体信息（歌名、歌手、封面）需要这项权限。",
            action = "去开启",
        ) { openNotificationAccess() })
    }

    private fun buildConnectionSection(page: LinearLayout) {
        page.addView(sectionHeading(
            getString(R.string.settings_section_behavior),
            "连接前后的行为。",
        ))
        page.addView(toggleRow(
            title = getString(R.string.settings_auto_connect),
            detail = getString(R.string.settings_auto_connect_detail),
            value = settings.autoConnect,
        ) { settings.autoConnect = it })

        page.addView(TextView(this).apply {
            text = getString(R.string.settings_version, BuildConfig.VERSION_NAME)
            style(10.5f, R.color.text_tertiary)
            setPadding(0, dp(18f), 0, 0)
        })
    }

    // ---- widgets --------------------------------------------------------

    private fun sectionHeading(title: String, detail: String): View =
        LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, dp(18f), 0, dp(8f))
            addView(TextView(this@SettingsActivity).apply {
                text = title
                style(12f, R.color.accent, bold = true)
                letterSpacing = 0.06f
            })
            addView(TextView(this@SettingsActivity).apply {
                text = detail
                style(11.5f, R.color.text_tertiary)
                setPadding(0, dp(3f), 0, 0)
                setLineSpacing(dp(3f).toFloat(), 1f)
            })
        }

    /**
     * A labelled slider with its live value in the corner.
     *
     * The value is shown next to the track rather than only in the label so a
     * drag has feedback while it happens; settings apply on the next
     * connection, so waiting for a session to prove the change would be a poor
     * way to find out what number was picked.
     */
    private fun slider(
        title: String,
        detail: String,
        value: Int,
        min: Int,
        max: Int,
        suffix: String,
        onChanged: (Int) -> Unit,
    ): View {
        val box = card()
        val head = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        head.addView(TextView(this).apply {
            text = title
            style(14f, R.color.text_primary, bold = true)
        }, LinearLayout.LayoutParams(0, -2, 1f))
        val valueLabel = TextView(this).apply {
            text = "$value$suffix"
            style(13f, R.color.accent, bold = true)
        }
        head.addView(valueLabel)
        box.addView(head)

        box.addView(TextView(this).apply {
            text = detail
            style(11.5f, R.color.text_secondary)
            setPadding(0, dp(4f), 0, dp(8f))
            setLineSpacing(dp(3f).toFloat(), 1f)
        })

        val bar = SeekBar(this).apply {
            this.max = max - min
            progress = (value - min).coerceIn(0, max - min)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(bar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val current = progress + min
                    valueLabel.text = "$current$suffix"
                    if (fromUser) onChanged(current)
                }
                override fun onStartTrackingTouch(bar: SeekBar?) = Unit
                override fun onStopTrackingTouch(bar: SeekBar?) = Unit
            })
        }
        box.addView(bar)
        return box
    }

    private fun choiceRow(
        title: String,
        detail: String,
        options: List<Pair<Int, String>>,
        current: Int,
        onPick: (Int) -> Unit,
    ): View {
        val box = card()
        box.addView(TextView(this).apply {
            text = title
            style(14f, R.color.text_primary, bold = true)
        })
        box.addView(TextView(this).apply {
            text = detail
            style(11.5f, R.color.text_secondary)
            setPadding(0, dp(4f), 0, dp(8f))
            setLineSpacing(dp(3f).toFloat(), 1f)
        })
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }
        options.forEach { (value, label) ->
            val selected = value == current
            row.addView(TextView(this).apply {
                text = label
                style(13f, if (selected) R.color.on_accent else R.color.text_primary, bold = selected)
                gravity = Gravity.CENTER
                background = pill(if (selected) R.color.accent else R.color.surface_high)
                isClickable = true
                setOnClickListener {
                    onPick(value)
                    recreate()
                }
            }, LinearLayout.LayoutParams(0, dp(40f), 1f).apply { marginEnd = dp(8f) })
        }
        box.addView(row)
        return box
    }

    private fun toggleRow(
        title: String,
        detail: String,
        value: Boolean,
        onChanged: (Boolean) -> Unit,
    ): View {
        val box = card()
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(TextView(this@SettingsActivity).apply {
                text = title
                style(14f, R.color.text_primary, bold = true)
            })
            addView(TextView(this@SettingsActivity).apply {
                text = detail
                style(11.5f, R.color.text_secondary)
                setPadding(0, dp(4f), 0, 0)
                setLineSpacing(dp(3f).toFloat(), 1f)
            })
            isClickable = true
            setOnClickListener {
                val next = !value
                onChanged(next)
                recreate()
            }
        }
        box.addView(content)
        return box
    }

    private fun actionRow(
        title: String,
        detail: String,
        action: String,
        onClick: () -> Unit,
    ): View {
        val box = card()
        box.addView(TextView(this).apply {
            text = title
            style(14f, R.color.text_primary, bold = true)
        })
        box.addView(TextView(this).apply {
            text = detail
            style(11.5f, R.color.text_secondary)
            setPadding(0, dp(4f), 0, dp(8f))
            setLineSpacing(dp(3f).toFloat(), 1f)
        })
        box.addView(MaterialButton(this, null,
            com.google.android.material.R.attr.materialButtonStyle).apply {
            text = action
            textSize = 12.5f
            isAllCaps = false
            setOnClickListener { onClick() }
        }, LinearLayout.LayoutParams(-2, dp(38f)))
        return box
    }

    private fun card(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(16f), dp(14f), dp(16f), dp(14f))
        background = pill(R.color.surface)
        layoutParams = LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(10f) }
    }

    private fun color(id: Int): Int = androidx.core.content.ContextCompat.getColor(this, id)

    private fun openAccessibility() {
        runCatching { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
    }

    private fun openNotificationAccess() {
        runCatching {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }.onFailure {
            runCatching { startActivity(Intent(Settings.ACTION_SETTINGS)) }
        }
    }
}
