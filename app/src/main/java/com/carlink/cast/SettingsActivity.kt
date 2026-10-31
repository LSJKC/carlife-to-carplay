package com.carlink.cast

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
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

        /*
         * The safe area is one setting, not two.
         *
         * A separate pair of percentage sliders used to sit here, added with
         * the extra margins wired on top of the existing safe-area rectangle.
         * That would have stacked two unrelated mechanisms: the car already
         * declares where its own chrome sits (278 px on the left here), and a
         * second percentage would have been added to it, making the slider feel
         * violently sensitive for no visible reason.
         *
         * So this page edits the same policy the older sheet does — the same
         * keys, the same pixel units — and the older sheet keeps working.
         */
        page.addView(safeAreaCard())
        page.addView(choiceRow(
            title = "画面质量（码率）",
            detail = "带宽换清晰度。8 Mbps 是默认，也是长期实测能稳住的值；" +
                "无线环境好、画面复杂时可以往上调，网络拥挤时往下调能减少卡顿。",
            options = listOf(4 to "4 Mbps", 8 to "8 Mbps", 12 to "12", 16 to "16", 24 to "24"),
            current = settings.videoBitrateMbps,
        ) { settings.videoBitrateMbps = it })

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
            detail = "默认 0（关闭）。关闭时车机上的每一次移动都会原样送达，反应最跟手。" +
                "只有当画面流畅但触摸明显滞后时才调大；它只影响拖动，按下和抬起永远立即送达，" +
                "不会影响连接。",
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

    /**
     * Pixel-exact safe area, matching how the older sheet already stores it.
     *
     * Pixels rather than percent: the car states its own safe rectangle in
     * pixels, and a margin measured in the same unit is the one thing that can
     * be compared with it directly. Typing `278` means 278 px, which is exactly
     * what the car declares.
     */
    private fun safeAreaCard(): View {
        val current = settings.safeAreaPolicy()
        val box = card()
        box.addView(TextView(this).apply {
            text = "安全区留边"
            style(14f, R.color.text_primary, bold = true)
        })
        box.addView(TextView(this).apply {
            text = "手机画面可以占用的区域。车机自己声明的安全区会在这里自动填好；" +
                "手动改动后以你填的为准。单位是像素，起点在面板左上角。"
            style(11.5f, R.color.text_secondary)
            setPadding(0, dp(4f), 0, dp(10f))
            setLineSpacing(dp(3f).toFloat(), 1f)
        })

        val modes = listOf(
            CarPanelGeometry.SafeAreaMode.STRICT to "跟随车机声明",
            CarPanelGeometry.SafeAreaMode.CUSTOM to "手动填写",
            CarPanelGeometry.SafeAreaMode.OFF to "关闭（铺满整屏）",
        )
        val modeRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        modes.forEach { (mode, label) ->
            val picked = mode == current.mode
            modeRow.addView(TextView(this).apply {
                text = label
                style(12f, if (picked) R.color.on_accent else R.color.text_primary, bold = picked)
                gravity = Gravity.CENTER
                background = pill(if (picked) R.color.accent else R.color.surface_high)
                isClickable = true
                setOnClickListener {
                    settings.saveSafeAreaPolicy(
                        endpoint = settings.peerAddress,
                        policy = current.copy(mode = mode),
                    )
                    recreate()
                }
            }, LinearLayout.LayoutParams(0, dp(38f), 1f).apply { marginEnd = dp(6f) })
        }
        box.addView(modeRow)

        if (current.mode == CarPanelGeometry.SafeAreaMode.CUSTOM) {
            val fields = listOf("左", "上", "右", "下")
            val inputs = listOf(current.left, current.top, current.right, current.bottom)
                .mapIndexed { index, value ->
                    EditText(this).apply {
                        hint = fields[index]
                        inputType = InputType.TYPE_CLASS_NUMBER
                        setSingleLine(true)
                        setText(value.toString())
                        setTextColor(color(R.color.text_primary))
                        setHintTextColor(color(R.color.text_tertiary))
                        backgroundTintList = android.content.res.ColorStateList.valueOf(color(R.color.accent))
                    }
                }
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0, dp(10f), 0, 0)
            }
            inputs.forEach { field ->
                row.addView(field, LinearLayout.LayoutParams(0, dp(52f), 1f).apply { marginEnd = dp(6f) })
            }
            box.addView(row)

            box.addView(TextView(this).apply {
                text = "保存"
                style(13f, R.color.on_accent, bold = true)
                gravity = Gravity.CENTER
                background = pill(R.color.accent)
                isClickable = true
                setOnClickListener {
                    fun read(field: EditText, fallback: Int) =
                        field.text?.toString()?.trim()?.toIntOrNull() ?: fallback
                    settings.saveSafeAreaPolicy(
                        endpoint = settings.peerAddress,
                        policy = current.copy(
                            mode = CarPanelGeometry.SafeAreaMode.CUSTOM,
                            left = read(inputs[0], current.left),
                            top = read(inputs[1], current.top),
                            right = read(inputs[2], current.right),
                            bottom = read(inputs[3], current.bottom),
                        ),
                    )
                    Toast.makeText(this@SettingsActivity, "已保存，下次连接生效", Toast.LENGTH_SHORT).show()
                }
            }, LinearLayout.LayoutParams(-1, dp(44f)).apply { topMargin = dp(10f) })
        }
        return box
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
