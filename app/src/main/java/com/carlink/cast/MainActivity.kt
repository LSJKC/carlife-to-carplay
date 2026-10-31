package com.carlink.cast

import android.Manifest
import android.app.Activity
import androidx.appcompat.app.AlertDialog
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.text.InputType
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.EditText
import android.widget.ImageView
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.NestedScrollView
import com.carlink.cast.ui.MetricsView
import com.carlink.cast.ui.ProtocolContent
import com.carlink.cast.ui.StageRingView
import com.carlink.cast.ui.Ui.dp
import com.carlink.cast.ui.Ui.pill
import com.carlink.cast.ui.Ui.style
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private lateinit var settings: CastSettings
    /** Horizontal pager holding the three top-level pages. */
    private var pager: HorizontalScrollView? = null
    private var pageDots: LinearLayout? = null
    private var currentPage = 0

    private lateinit var ring: StageRingView
    private lateinit var stageDetail: TextView
    private lateinit var deviceName: TextView
    private lateinit var sourceBadge: TextView
    private lateinit var startButton: MaterialButton
    private lateinit var stageProgress: TextView
    private lateinit var stageSegments: List<View>
    private lateinit var fpsMetric: Metric
    private lateinit var bitrateMetric: Metric
    private lateinit var resolutionMetric: Metric
    private lateinit var durationMetric: Metric
    private lateinit var fpsSpark: MetricsView
    private lateinit var logToggle: TextView
    private lateinit var logText: TextView
    private lateinit var logBody: View

    private val peers = mutableListOf<Pair<String, String>>() // address to name
    private var selectedPeerIndex = -1
    private var logExpanded = false
    private var pendingCapture = false
    /** Set when the user pressed connect while runtime permissions were missing. */
    private var pendingStartSession = false
    /** Set when the Honor Car Connect page was the action waiting on permission. */
    private var pendingHonorCarConnect = false
    private var pendingExport: java.io.File? = null
    private var lastActiveStep = 0
    private var durationSession = ""
    private var durationAnchor = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = CastSettings(this)
        CastLog.initialize(this)
        window.statusBarColor = Color.parseColor("#0B1220")
        window.navigationBarColor = Color.parseColor("#0B1220")
        /*
         * A failure building the screen must be readable, not a silent exit.
         *
         * The screen is built entirely in code, so one bad layout call takes
         * the whole activity down before anything is drawn — the app just
         * disappears with no way to tell which call did it. Building it inside
         * runCatching and showing the message keeps the failure on screen and
         * in the journal.
         */
        val screen = runCatching { buildScreen() }.getOrElse { failure ->
            CastLog.event(LogChannel.APP, "FATAL", "界面构建失败：${failure.stackTraceToString()}")
            buildFailureScreen(failure)
        }
        setContentView(screen)
        runCatching { bindState() }.onFailure {
            CastLog.event(LogChannel.APP, "FATAL", "状态绑定失败：${it.stackTraceToString()}")
        }
        refreshPeers()
    }

    // ---------------------------------------------------------------- layout

    /**
     * Three top-level pages, swiped horizontally.
     *
     * The settings used to be a separate activity pushed on top of the main
     * screen, which read as a pop-up over the thing it belongs to. Here every
     * page is a peer: the same frame, the same title bar, the same dock, and a
     * horizontal swipe (or a dock tap) moves between them. A plain
     * HorizontalScrollView does the paging — one child per page, each the
     * screen width — which avoids pulling in a pager library and keeps each
     * page's own vertical scrolling exactly as it was.
     */
    private fun buildScreen(): View {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(color(R.color.bg))
        }
        root.addView(buildTopBar())

        pager = SnappingPager(this, PAGE_COUNT).apply {
            isHorizontalScrollBarEnabled = false
            isFillViewport = true
            overScrollMode = View.OVER_SCROLL_NEVER
            setOnScrollChangeListener { _, _, _, _, _ -> updatePageIndicator() }
        }
        val strip = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        pager!!.addView(strip, ViewGroup.LayoutParams(-2, -1))

        // Width is fixed once the pager itself has been measured; using the
        // display metric up front produced pages a few pixels wider than the
        // screen on some densities, which is why a sliver of the neighbouring
        // page showed through and a swipe could rest between two pages.
        listOf(buildHomePage(), buildSettingsPage(), buildConnectPage()).forEach { content ->
            strip.addView(page(content), LinearLayout.LayoutParams(pageWidth(), -1))
        }
        pager!!.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            val width = pager!!.width
            if (width <= 0) return@addOnLayoutChangeListener
            for (index in 0 until strip.childCount) {
                val child = strip.getChildAt(index)
                if (child.layoutParams.width != width) {
                    child.layoutParams = (child.layoutParams as LinearLayout.LayoutParams).apply { this.width = width }
                }
            }
        }
        root.addView(pager, LinearLayout.LayoutParams(-1, 0, 1f))

        root.addView(buildPageIndicator())
        root.addView(buildDock())
        return root
    }

    /**
     * A horizontal pager that always settles on a page boundary.
     *
     * A plain HorizontalScrollView stops wherever the fling ends, which left
     * the settings page half on screen and half off — the one complaint about
     * this layout that was purely mechanical. On release the nearest boundary
     * is chosen and scrolled to.
     */
    private class SnappingPager(
        context: android.content.Context,
        private val pageCount: Int,
    ) : HorizontalScrollView(context) {
        override fun onTouchEvent(ev: android.view.MotionEvent): Boolean {
            val handled = super.onTouchEvent(ev)
            if (ev.actionMasked == android.view.MotionEvent.ACTION_UP ||
                ev.actionMasked == android.view.MotionEvent.ACTION_CANCEL
            ) {
                val width = width
                if (width > 0) {
                    val page = Math.round(scrollX.toFloat() / width).coerceIn(0, pageCount - 1)
                    smoothScrollTo(page * width, 0)
                }
            }
            return handled
        }
    }

    /**
     * Width of one page: the pager's measured width, falling back to the
     * display only before the first layout pass.
     *
     * Using the display metric alone can be a few pixels off on some densities,
     * and every pixel of mismatch shows up as the neighbouring page peeking in
     * at the edge of the screen.
     */
    private fun pageWidth(): Int {
        val measured = pager?.width ?: 0
        if (measured > 0) return measured
        val display = resources.displayMetrics.widthPixels
        return if (display > 0) display else dp(360f)
    }

    /** One page: its own vertical scroll view, clipped to the page width. */
    private fun page(content: View): View = ScrollView(this).apply {
        isFillViewport = true
        isVerticalScrollBarEnabled = false
        addView(content, ViewGroup.LayoutParams(-1, -2))
    }

    /** Column with the standard page padding. */
    private fun pageColumn(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(20f), dp(4f), dp(20f), dp(24f))
    }

    private fun buildHomePage(): View {
        val column = pageColumn()
        column.addView(buildDevicePill(), LinearLayout.LayoutParams(-1, -2))
        column.addView(buildHero(), LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(12f) })
        column.addView(buildMetricsGrid(), LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(16f) })
        column.addView(buildStageCard(), LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(12f) })
        column.addView(buildLogCard(), LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(12f) })
        return column
    }

    /** Settings live on their own page now, built from the same rows. */
    private fun buildSettingsPage(): View {
        val column = pageColumn()
        column.addView(sectionHeading(getString(R.string.settings_section_display), "画面在车机上的大小、边距与画质。改动多数在下次连接时生效。"))
        column.addView(buildSafeAreaCard())
        column.addView(buildChoicesCard())
        column.addView(sectionHeading(getString(R.string.settings_section_audio), "声音是否从车机播放。"))
        column.addView(buildToggleCard(
            getString(R.string.settings_audio), getString(R.string.settings_audio_detail), settings.audio,
        ) { settings.audio = it })
        column.addView(sectionHeading(getString(R.string.settings_section_input), "车机屏幕上的触摸如何传回手机。"))
        column.addView(buildTouchThrottleCard())
        column.addView(actionCard("触摸服务", "系统需要开启无障碍服务，车机触摸才能被收到。",
            if (isTouchServiceEnabled()) "已开启" else "去开启") { openAccessibility() })
        column.addView(actionCard("通知使用权", "读取车机媒体信息（歌名、歌手、封面）需要它。",
            if (isMediaListenerEnabled()) "已开启" else "去开启") { openMediaListenerSettings() })
        column.addView(sectionHeading(getString(R.string.settings_section_behavior), "连接前后的行为。"))
        column.addView(buildToggleCard(
            getString(R.string.settings_auto_connect), getString(R.string.settings_auto_connect_detail), settings.autoConnect,
        ) { settings.autoConnect = it })
        column.addView(buildToggleCard(
            getString(R.string.settings_carlife_auto_session), getString(R.string.settings_carlife_auto_session_detail), settings.carLifeAutoSession,
        ) { settings.carLifeAutoSession = it })
        column.addView(buildToggleCard(
            getString(R.string.settings_video_passthrough), getString(R.string.settings_video_passthrough_detail), settings.videoPassthrough,
        ) { settings.videoPassthrough = it })
        if (settings.sourceMode != SourceMode.MIRROR) {
            column.addView(actionCard(getString(R.string.settings_wallpaper),
                "合成画布时安全区外侧显示的背景，共九张。" + wallpaperStatus(), "选择") { showWallpaperSheet() })
        }
        column.addView(TextView(this).apply {
            text = getString(R.string.settings_version, BuildConfig.VERSION_NAME)
            style(10.5f, R.color.text_tertiary)
            setPadding(0, dp(18f), 0, 0)
        })
        return column
    }

    /** Connection, pairing and diagnostics. */
    private fun buildConnectPage(): View {
        val column = pageColumn()
        column.addView(sectionHeading("连接", "车机配对、连接引导与故障诊断。"))
        column.addView(actionCard(
            getString(R.string.settings_connect_guide),
            getString(R.string.settings_connect_guide_detail),
            "打开",
        ) { openHonorCarConnect() })
        column.addView(actionCard(
            "选择车机",
            "当前：${settings.peerName ?: getString(R.string.device_none)}。点开后可以更换目标车机。",
            "选择",
        ) { showPeerSheet() })
        column.addView(sectionHeading("进阶", "一般不需要改动。"))
        val adapter = getSystemService(BluetoothManager::class.java)?.adapter
        val currentAddress = adapter?.let { LocalBluetoothAddress.read(this, it, settings.manualBluetoothAddress) }
        column.addView(actionCard(
            getString(R.string.settings_bluetooth),
            currentAddress?.let { getString(R.string.settings_bluetooth_current, it) }
                ?: getString(R.string.settings_bluetooth_detail),
            "修改",
        ) { editBluetoothAddress() })
        column.addView(actionCard(
            getString(R.string.settings_clear_pair),
            getString(R.string.settings_clear_pair_detail),
            "清除",
        ) {
            val peer = selectedPeer()
            if (peer == null) {
                toast(getString(R.string.toast_no_peer))
            } else {
                settings.removePair(peer.first)
                settings.clearConnected(peer.first)
                toast(getString(R.string.toast_pair_cleared))
            }
        })
        column.addView(actionCard(
            getString(R.string.settings_export),
            getString(R.string.settings_export_detail),
            "导出",
        ) { exportLog() })
        return column
    }

    /**
     * The bottom dock: the few things a driver reaches for.
     *
     * Settings used to be an icon in the top-right corner, which is the least
     * discoverable position on a phone screen and the first thing that was
     * reported as unfriendly. A dock at the bottom puts every entry within
     * thumb reach and gives each one a label.
     */
    /** Last-resort screen: says what failed instead of vanishing. */
    private fun buildFailureScreen(failure: Throwable): View = ScrollView(this).apply {
        setBackgroundColor(Color.parseColor("#0B1220"))
        addView(TextView(this@MainActivity).apply {
            text = buildString {
                append("界面构建失败")
                append(System.lineSeparator())
                append(System.lineSeparator())
                append(failure.javaClass.simpleName)
                append(": ")
                append(failure.message)
                append(System.lineSeparator())
                append(System.lineSeparator())
                append(failure.stackTraceToString().take(2000))
            }
            setTextColor(Color.parseColor("#E6EDF7"))
            textSize = 12f
            setPadding(dp(20f), dp(60f), dp(20f), dp(40f))
            setTextIsSelectable(true)
        })
    }

    /** Small heading with a one-line explanation, used at the top of a group. */
    private fun sectionHeading(title: String, detail: String): View =
        LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, dp(16f), 0, dp(8f))
            addView(TextView(this@MainActivity).apply {
                text = title
                style(13f, R.color.accent, bold = true)
                letterSpacing = 0.05f
            })
            addView(TextView(this@MainActivity).apply {
                text = detail
                style(11.5f, R.color.text_tertiary)
                setPadding(0, dp(3f), 0, 0)
                setLineSpacing(dp(3f).toFloat(), 1f)
            })
        }

    /** A card holding one labelled control; the page supplies the control. */
    /**
     * A card with a title, an explanation, and room for one control.
     *
     * The children go into a vertical LinearLayout, not straight into the
     * MaterialCardView: that view is a FrameLayout, so every direct child is
     * placed at (0,0) and they all draw on top of each other. That is exactly
     * what the settings page looked like — four lines of text stacked in the
     * same spot, unreadable.
     */
    private fun settingCard(title: String, detail: String): LinearLayout {
        val cardView = card(16f)
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16f), dp(14f), dp(16f), dp(14f))
        }
        cardView.addView(box, ViewGroup.LayoutParams(-1, -2))
        cardView.layoutParams = LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(10f) }
        box.addView(TextView(this).apply {
            text = title
            style(14f, R.color.text_primary, bold = true)
        })
        box.addView(TextView(this).apply {
            text = detail
            style(11.5f, R.color.text_secondary)
            setPadding(0, dp(4f), 0, dp(10f))
            setLineSpacing(dp(3f).toFloat(), 1f)
        })
        return box
    }

    private fun actionCard(title: String, detail: String, action: String, onClick: () -> Unit): View {
        val box = settingCard(title, detail)
        box.addView(secondaryButton(action).apply { setOnClickListener { onClick() } },
            LinearLayout.LayoutParams(-1, dp(42f)))
        return box
    }

    private fun buildToggleCard(title: String, detail: String, value: Boolean, onChanged: (Boolean) -> Unit): View {
        val box = settingCard(title, detail)
        var on = value
        box.addView(com.google.android.material.materialswitch.MaterialSwitch(this).apply {
            text = if (on) "已开启" else "已关闭"
            isChecked = on
            setTextColor(color(R.color.text_primary))
            setOnCheckedChangeListener { _, checked ->
                on = checked
                text = if (checked) "已开启" else "已关闭"
                onChanged(checked)
            }
        }, LinearLayout.LayoutParams(-1, dp(44f)))
        return box
    }

    /** Safe area: the same pixel values the old sheet stored, edited in place. */
    private fun buildSafeAreaCard(): View {
        val current = settings.safeAreaPolicy()
        val box = settingCard("安全区留边",
            "手机画面可以占用的区域。车机声明的安全区会自动带出来；手动填写后以你填的为准，单位是像素。")
        val modes = listOf(
            CarPanelGeometry.SafeAreaMode.STRICT to "跟随车机",
            CarPanelGeometry.SafeAreaMode.CUSTOM to "手动填写",
            CarPanelGeometry.SafeAreaMode.OFF to "关闭",
        )
        val fieldsBox = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        val buttons = mutableListOf<TextView>()

        fun paint(mode: CarPanelGeometry.SafeAreaMode) {
            buttons.forEachIndexed { index, button ->
                val picked = modes[index].first == mode
                button.setTextColor(color(if (picked) R.color.on_accent else R.color.text_primary))
                button.background = pill(if (picked) R.color.accent else R.color.surface_high)
            }
            fieldsBox.removeAllViews()
            if (mode != CarPanelGeometry.SafeAreaMode.CUSTOM) return
            val saved = settings.safeAreaPolicy()
            val labels = listOf("左", "上", "右", "下")
            val inputs = listOf(saved.left, saved.top, saved.right, saved.bottom)
                .mapIndexed { index, value ->
                    EditText(this).apply {
                        hint = labels[index]
                        inputType = InputType.TYPE_CLASS_NUMBER
                        setSingleLine(true)
                        setText(value.toString())
                        setTextColor(color(R.color.text_primary))
                        setHintTextColor(color(R.color.text_tertiary))
                    }
                }
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0, dp(10f), 0, 0)
            }
            inputs.forEach {
                row.addView(it, LinearLayout.LayoutParams(0, dp(50f), 1f).apply { marginEnd = dp(6f) })
            }
            fieldsBox.addView(row)
            val status = TextView(this).apply {
                text = ""
                style(11.5f, R.color.accent, bold = true)
                setPadding(0, dp(8f), 0, 0)
            }
            fieldsBox.addView(secondaryButton("保存").apply {
                setOnClickListener {
                    fun read(field: EditText, fallback: Int) =
                        field.text?.toString()?.trim()?.toIntOrNull() ?: fallback
                    settings.saveSafeAreaPolicy(
                        endpoint = settings.peerAddress,
                        policy = saved.copy(
                            mode = CarPanelGeometry.SafeAreaMode.CUSTOM,
                            left = read(inputs[0], saved.left),
                            top = read(inputs[1], saved.top),
                            right = read(inputs[2], saved.right),
                            bottom = read(inputs[3], saved.bottom),
                        ),
                    )
                    status.text = "已保存，下次连接生效"
                }
            }, LinearLayout.LayoutParams(-1, dp(42f)).apply { topMargin = dp(8f) })
            fieldsBox.addView(status)
        }

        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        modes.forEach { (mode, label) ->
            val button = TextView(this).apply {
                text = label
                style(12f, R.color.text_primary)
                gravity = Gravity.CENTER
                background = pill(R.color.surface_high)
                isClickable = true
                setOnClickListener {
                    settings.saveSafeAreaPolicy(
                        endpoint = settings.peerAddress,
                        policy = settings.safeAreaPolicy().copy(mode = mode),
                    )
                    paint(mode)
                }
            }
            buttons += button
            row.addView(button, LinearLayout.LayoutParams(0, dp(42f), 1f).apply { marginEnd = dp(6f) })
        }
        box.addView(row)
        box.addView(fieldsBox)
        paint(current.mode)
        return box
    }

    /** Frame rate and bitrate: plain choices, applied on the spot. */
    private fun buildChoicesCard(): View {
        val box = settingCard("画面质量与帧率",
            "码率越高越清晰也越吃带宽；帧率 60 更流畅。8 Mbps / 30fps 是默认值。")
        val fpsValues = listOf(30, 60)
        val fpsRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val fpsButtons = mutableListOf<TextView>()
        fun paintFps(picked: Int) {
            fpsButtons.forEachIndexed { index, button ->
                val on = fpsValues[index] == picked
                button.setTextColor(color(if (on) R.color.on_accent else R.color.text_primary))
                button.background = pill(if (on) R.color.accent else R.color.surface_high)
            }
        }
        fpsValues.forEach { value ->
            val button = TextView(this).apply {
                text = value.toString() + " fps"
                style(13f, R.color.text_primary)
                gravity = Gravity.CENTER
                background = pill(R.color.surface_high)
                isClickable = true
                setOnClickListener {
                    settings.frameRateOverride = value
                    paintFps(value)
                }
            }
            fpsButtons += button
            fpsRow.addView(button, LinearLayout.LayoutParams(0, dp(42f), 1f).apply { marginEnd = dp(6f) })
        }
        box.addView(fpsRow)
        paintFps(settings.frameRateOverride.takeIf { it > 0 } ?: 30)

        val rates = listOf(4, 8, 12, 16)
        val bitrateRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, dp(8f), 0, 0)
        }
        val bitrateButtons = mutableListOf<TextView>()
        fun paintBitrate(picked: Int) {
            bitrateButtons.forEachIndexed { index, button ->
                val on = rates[index] == picked
                button.setTextColor(color(if (on) R.color.on_accent else R.color.text_primary))
                button.background = pill(if (on) R.color.accent else R.color.surface_high)
            }
        }
        rates.forEach { mbps ->
            val button = TextView(this).apply {
                text = mbps.toString() + " M"
                style(12.5f, R.color.text_primary)
                gravity = Gravity.CENTER
                background = pill(R.color.surface_high)
                isClickable = true
                setOnClickListener {
                    settings.videoBitrateMbps = mbps
                    paintBitrate(mbps)
                }
            }
            bitrateButtons += button
            bitrateRow.addView(button, LinearLayout.LayoutParams(0, dp(40f), 1f).apply { marginEnd = dp(6f) })
        }
        box.addView(bitrateRow)
        paintBitrate(settings.videoBitrateMbps)
        return box
    }

    private fun buildTouchThrottleCard(): View {
        val box = settingCard("触摸响应间隔",
            "默认关闭，最跟手。只有画面流畅但触摸明显滞后时才调大；只影响拖动，按下抬起永远立即送达。")
        val values = listOf(0, 8, 16, 24, 48)
        val labels = listOf("关闭", "8ms", "16ms", "24ms", "48ms")
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val buttons = mutableListOf<TextView>()
        fun paint(picked: Int) {
            buttons.forEachIndexed { index, button ->
                val on = values[index] == picked
                button.setTextColor(color(if (on) R.color.on_accent else R.color.text_primary))
                button.background = pill(if (on) R.color.accent else R.color.surface_high)
            }
        }
        values.forEachIndexed { index, value ->
            val button = TextView(this).apply {
                text = labels[index]
                style(12f, R.color.text_primary)
                gravity = Gravity.CENTER
                background = pill(R.color.surface_high)
                isClickable = true
                setOnClickListener {
                    settings.touchMoveThrottleMs = value
                    paint(value)
                }
            }
            buttons += button
            row.addView(button, LinearLayout.LayoutParams(0, dp(40f), 1f).apply { marginEnd = dp(5f) })
        }
        box.addView(row)
        paint(settings.touchMoveThrottleMs)
        return box
    }

    private fun buildPageIndicator(): View {
        val dots = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(0, dp(4f), 0, dp(2f))
        }
        repeat(PAGE_COUNT) { index ->
            dots.addView(View(this).apply {
                background = pill(R.color.track)
                alpha = if (index == 0) 1f else 0.4f
            }, LinearLayout.LayoutParams(dp(6f), dp(6f)).apply {
                marginStart = dp(4f)
                marginEnd = dp(4f)
            })
        }
        pageDots = dots
        return dots
    }

    private fun updatePageIndicator() {
        val pager = pager ?: return
        val width = pageWidth()
        if (width <= 0) return
        if (width <= 0) return
        val index = ((pager.scrollX + width / 2) / width).coerceIn(0, PAGE_COUNT - 1)
        if (index == currentPage) return
        currentPage = index
        pageDots?.let { dots ->
            for (i in 0 until dots.childCount) {
                dots.getChildAt(i)?.alpha = if (i == index) 1f else 0.4f
            }
        }
    }

    private fun goToPage(index: Int) {
        val pager = pager ?: return
        currentPage = index
        pager.smoothScrollTo(index * pageWidth(), 0)
        pageDots?.let { dots ->
            for (i in 0 until dots.childCount) {
                dots.getChildAt(i)?.alpha = if (i == index) 1f else 0.4f
            }
        }
    }

    private fun buildDock(): View {
        val dock = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(16f), dp(8f), dp(16f), dp(14f))
            setBackgroundColor(color(R.color.bg))
        }
        fun item(icon: Int, label: String, onClick: () -> Unit): View =
            LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                isClickable = true
                isFocusable = true
                setOnClickListener { onClick() }
                addView(ImageView(this@MainActivity).apply {
                    setImageResource(icon)
                    imageTintList = tint(R.color.text_secondary)
                }, LinearLayout.LayoutParams(dp(22f), dp(22f)))
                addView(TextView(this@MainActivity).apply {
                    text = label
                    style(10.5f, R.color.text_secondary)
                    setPadding(0, dp(4f), 0, 0)
                })
            }

        // Each dock entry is a peer page, not a pop-up on top of the current
        // one: tapping switches the pager, swiping does the same.
        dock.addView(item(R.drawable.ic_cast, "投屏") { goToPage(0) },
            LinearLayout.LayoutParams(0, -2, 1f))
        dock.addView(item(R.drawable.ic_settings, getString(R.string.action_settings)) { goToPage(1) },
            LinearLayout.LayoutParams(0, -2, 1f))
        dock.addView(item(R.drawable.ic_car, "连接") { goToPage(2) },
            LinearLayout.LayoutParams(0, -2, 1f))
        return dock
    }

    private fun buildTopBar(): View {
        val bar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(20f), dp(16f), dp(12f), dp(10f))
        }

        val mark = card(12f).apply {
            isClickable = false
            setCardBackgroundColor(color(R.color.surface_high))
        }
        mark.addView(ImageView(this).apply {
            setImageResource(R.drawable.ic_cast)
            imageTintList = tint(R.color.cyan)
        }, FrameLayout.LayoutParams(dp(20f), dp(20f), Gravity.CENTER))
        bar.addView(mark, LinearLayout.LayoutParams(dp(40f), dp(40f)))

        val titles = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12f), 0, 0, 0)
        }
        titles.addView(TextView(this).apply {
            text = getString(R.string.app_name)
            style(18f, R.color.text_primary, bold = true)
        })
        titles.addView(TextView(this).apply {
            text = getString(R.string.app_subtitle)
            style(11f, R.color.text_tertiary)
            setPadding(0, dp(1f), 0, 0)
        })
        bar.addView(titles, LinearLayout.LayoutParams(0, -2, 1f))

        /*
         * Text entries, not two unlabelled icons in the corner.
         *
         * The settings sheet holds every option this app has, and a bare gear in
         * the top-right is the least discoverable place to put it — the first
         * feedback on the UI was that the corner was unfriendly and that the
         * functions were not explained. A labelled button says what it opens.
         */
        bar.addView(textButton(getString(R.string.action_protocol)) { showProtocol() })
        // Settings moved to the bottom dock: a top-right corner is the least
        // discoverable place for the one entry that holds every option.
        return bar
    }

    /**
     * The car selector, sized to be found.
     *
     * It used to be a 46dp strip with a small icon and a name that could be
     * cut off — easy to miss and easy to mistake for a label rather than a
     * button. This is now a full-width card: the car name in the largest type
     * on the screen after the title, its address underneath, and the action
     * spelled out on the right.
     */
    private fun buildDevicePill(): View {
        val pillCard = card(18f).apply {
            isClickable = true
            isFocusable = true
            setOnClickListener { showPeerSheet() }
        }
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(16f), dp(16f), dp(16f), dp(16f))
        }
        pillCard.addView(row, FrameLayout.LayoutParams(-1, -1))

        row.addView(ImageView(this).apply {
            setImageResource(R.drawable.ic_car)
            imageTintList = tint(R.color.accent)
        }, LinearLayout.LayoutParams(dp(26f), dp(26f)))

        val texts = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(14f), 0, dp(10f), 0)
        }
        texts.addView(TextView(this).apply {
            text = "投屏到"
            style(11f, R.color.text_tertiary)
        })
        deviceName = TextView(this).apply {
            text = getString(R.string.device_none)
            style(17f, R.color.text_primary, bold = true)
            maxLines = 1
            ellipsize = android.text.TextUtils.TruncateAt.END
            setPadding(0, dp(2f), 0, 0)
        }
        texts.addView(deviceName)
        row.addView(texts, LinearLayout.LayoutParams(0, -2, 1f))

        // Stacked on the right: what the button does, and which source is in
        // use. Both used to compete for the same strip and neither was legible.
        val right = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.END
        }
        right.addView(TextView(this).apply {
            text = "更换"
            style(12.5f, R.color.accent, bold = true)
        })
        sourceBadge = TextView(this).apply {
            text = shortSourceLabel(settings.sourceMode)
            style(10.5f, R.color.text_secondary)
            setPadding(dp(8f), dp(3f), dp(8f), dp(3f))
            background = pill(R.color.track)
        }
        right.addView(sourceBadge, LinearLayout.LayoutParams(-2, -2).apply { topMargin = dp(5f) })
        row.addView(right)
        return pillCard
    }

    private fun buildHero(): View {
        val column = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
        }

        val ringSize = resources.getDimensionPixelSize(R.dimen.ring_size)
        ring = StageRingView(this)
        val ringBox = FrameLayout(this)
        ringBox.addView(ring, FrameLayout.LayoutParams(ringSize, ringSize, Gravity.CENTER))
        column.addView(ringBox, LinearLayout.LayoutParams(-1, ringSize))

        stageDetail = TextView(this).apply {
            text = getString(R.string.status_idle_detail)
            style(12.5f, R.color.text_secondary)
            gravity = Gravity.CENTER
            maxLines = 2
            minHeight = dp(38f)
            setLineSpacing(dp(2f).toFloat(), 1f)
            setPadding(dp(8f), dp(2f), dp(8f), 0)
        }
        column.addView(stageDetail, LinearLayout.LayoutParams(-1, -2))

        startButton = MaterialButton(this, null,
            com.google.android.material.R.attr.materialButtonStyle).apply {
            text = getString(R.string.action_start)
            textSize = 16f
            isAllCaps = false
            typeface = android.graphics.Typeface.create("sans-serif-medium", android.graphics.Typeface.BOLD)
            setTextColor(color(R.color.on_accent))
            backgroundTintList = tint(R.color.accent)
            cornerRadius = dp(20f)
            insetTop = 0
            insetBottom = 0
            stateListAnimator = null
            setIconResource(R.drawable.ic_cast)
            iconSize = dp(20f)
            iconPadding = dp(10f)
            iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
            iconTint = tint(R.color.on_accent)
            setOnClickListener { onPrimaryAction() }
        }
        column.addView(startButton, LinearLayout.LayoutParams(-1, dp(56f)).apply {
            topMargin = dp(14f)
        })
        return column
    }

    private fun buildStageCard(): View {
        val stageCard = card(20f)
        val column = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16f), dp(14f), dp(16f), dp(15f))
        }
        stageCard.addView(column)

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        header.addView(sectionTitle(getString(R.string.stages_title)), LinearLayout.LayoutParams(0, -2, 1f))
        stageProgress = TextView(this).apply {
            text = getString(R.string.stage_not_started)
            style(11f, R.color.text_secondary)
        }
        header.addView(stageProgress)
        column.addView(header)

        val bar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val segments = mutableListOf<View>()
        CastStage.ladder.forEachIndexed { index, _ ->
            val segment = View(this).apply {
                background = pill(R.color.track)
            }
            val params = LinearLayout.LayoutParams(0, dp(4f), 1f)
            if (index < CastStage.ladder.lastIndex) params.marginEnd = dp(4f)
            bar.addView(segment, params)
            segments.add(segment)
        }
        stageSegments = segments
        column.addView(bar, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(11f) })
        return stageCard
    }

    private fun buildMetricsGrid(): View {
        val grid = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }

        val first = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            weightSum = 2f
        }
        fpsMetric = metricCard(getString(R.string.metrics_fps), getString(R.string.unit_fps), spark = true)
        first.addView(fpsMetric.card, LinearLayout.LayoutParams(0, -2, 1f).apply { marginEnd = dp(5f) })
        bitrateMetric = metricCard(getString(R.string.metrics_bitrate), getString(R.string.unit_kbps))
        first.addView(bitrateMetric.card, LinearLayout.LayoutParams(0, -2, 1f).apply { marginStart = dp(5f) })
        grid.addView(first)

        val second = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            weightSum = 2f
        }
        resolutionMetric = metricCard(getString(R.string.metrics_resolution), null)
        second.addView(resolutionMetric.card, LinearLayout.LayoutParams(0, -2, 1f).apply { marginEnd = dp(5f) })
        durationMetric = metricCard(getString(R.string.metrics_duration), null)
        second.addView(durationMetric.card, LinearLayout.LayoutParams(0, -2, 1f).apply { marginStart = dp(5f) })
        grid.addView(second, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(10f) })
        return grid
    }

    private fun metricCard(label: String, unitText: String?, spark: Boolean = false): Metric {
        val metricCard = card(20f).apply { minimumHeight = dp(76f) }
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(14f), dp(11f), dp(14f), dp(11f))
        }
        metricCard.addView(content, FrameLayout.LayoutParams(-1, -2, Gravity.CENTER_VERTICAL))

        content.addView(TextView(this).apply {
            text = label
            style(10.5f, R.color.text_tertiary, bold = true)
            letterSpacing = 0.04f
        })

        val valueRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val value = TextView(this).apply {
            text = "—"
            style(21f, R.color.text_primary, bold = true)
            maxLines = 1
            ellipsize = android.text.TextUtils.TruncateAt.END
        }
        valueRow.addView(value)
        val unit = TextView(this).apply {
            text = unitText.orEmpty()
            style(10f, R.color.text_tertiary)
            setPadding(dp(4f), dp(4f), 0, 0)
        }
        valueRow.addView(unit)
        if (spark) {
            valueRow.addView(View(this), LinearLayout.LayoutParams(0, 1, 1f))
            fpsSpark = MetricsView(this)
            valueRow.addView(fpsSpark, LinearLayout.LayoutParams(dp(52f), dp(22f)))
        }
        content.addView(valueRow, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(3f) })
        return Metric(metricCard, value, unit)
    }

    private fun buildLogCard(): View {
        val logCard = card(20f)
        val column = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        logCard.addView(column)

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(16f), dp(14f), dp(16f), dp(14f))
            isClickable = true
            isFocusable = true
            setOnClickListener { toggleLog() }
        }
        header.addView(sectionTitle(getString(R.string.logs_title)), LinearLayout.LayoutParams(0, -2, 1f))
        logToggle = TextView(this).apply {
            text = getString(R.string.logs_expand)
            style(11.5f, R.color.accent, bold = true)
        }
        header.addView(logToggle)
        column.addView(header)

        val scroll = ScrollView(this).apply {
            visibility = View.GONE
            isVerticalScrollBarEnabled = false
        }
        logText = TextView(this).apply {
            text = getString(R.string.logs_empty)
            style(10.5f, R.color.text_tertiary, mono = true)
            setTextIsSelectable(true)
            setLineSpacing(dp(2f).toFloat(), 1f)
            setPadding(dp(16f), 0, dp(16f), dp(14f))
        }
        scroll.addView(logText, ViewGroup.LayoutParams(-1, -2))
        column.addView(scroll, LinearLayout.LayoutParams(-1, dp(150f)))
        logBody = scroll
        return logCard
    }

    private fun sectionTitle(text: String) = TextView(this).apply {
        this.text = text
        style(11f, R.color.text_tertiary, bold = true)
        letterSpacing = 0.08f
    }

    private fun toggleLog() {
        logExpanded = !logExpanded
        logToggle.text = getString(if (logExpanded) R.string.logs_collapse else R.string.logs_expand)
        logBody.visibility = if (logExpanded) View.VISIBLE else View.GONE
    }

    // ---------------------------------------------------------------- state

    private fun bindState() {
        scope.launch {
            CastLog.state.collectLatest { state -> render(state) }
        }
        // The duration card is the only value that keeps moving without a new
        // event, so it gets its own one-second tick.
        scope.launch {
            while (true) {
                delay(1000)
                val state = CastLog.state.value
                if (state.running) durationMetric.value.text = durationText(state)
            }
        }
    }

    private fun render(state: CastState) {
        if (state.stage != CastStage.FAILED && state.stage != CastStage.STOPPED) {
            lastActiveStep = state.stage.step
        }
        ring.failedStep = lastActiveStep
        ring.state = state

        stageDetail.text = if (state.stage == CastStage.STOPPED && state.sessionId.isBlank()) {
            getString(R.string.status_idle_detail)
        } else {
            state.detail
        }

        val latest = state.metrics.lastOrNull()
        fpsMetric.value.text = latest?.let { "%.1f".format(Locale.US, it.fps) } ?: "—"
        when {
            latest == null -> {
                bitrateMetric.value.text = "—"
                bitrateMetric.unit.text = getString(R.string.unit_kbps)
            }
            latest.kbps >= 1000 -> {
                bitrateMetric.value.text = "%.1f".format(Locale.US, latest.kbps / 1000)
                bitrateMetric.unit.text = getString(R.string.unit_mbps)
            }
            else -> {
                bitrateMetric.value.text = "%.0f".format(Locale.US, latest.kbps)
                bitrateMetric.unit.text = getString(R.string.unit_kbps)
            }
        }
        resolutionMetric.value.text = state.display.substringBefore("·").trim().ifBlank { "—" }
        durationMetric.value.text = durationText(state)
        fpsSpark.samples = state.metrics

        logText.text = state.events.takeLast(80).joinToString("\n") {
            val seconds = (it.elapsedMillis / 1000).toInt()
            // The channel tag is what makes the on-screen log readable while a
            // session is still running: it separates the audio lines from the
            // iAP2 frames without needing the exported file.
            "%02d:%02d  %-8s %s".format(Locale.US, seconds / 60, seconds % 60, it.channel, it.message)
        }.ifBlank { getString(R.string.logs_empty) }

        sourceBadge.text = sourceChipText(state)
        val running = state.running
        startButton.text = getString(if (running) R.string.action_stop else R.string.action_start)
        startButton.setIconResource(if (running) R.drawable.ic_close else R.drawable.ic_cast)
        val foreground = color(if (running) R.color.on_danger else R.color.on_accent)
        startButton.setTextColor(foreground)
        startButton.iconTint = ColorStateList.valueOf(foreground)
        startButton.backgroundTintList = tint(if (running) R.color.danger else R.color.accent)
        renderStages(state)
    }

    private fun renderStages(state: CastState) {
        val step = state.stage.step
        val failedStep = if (state.stage == CastStage.FAILED) lastActiveStep else 0
        stageSegments.forEachIndexed { index, segment ->
            val position = index + 1
            val colorRes = when (state.stage) {
                CastStage.STOPPED ->
                    if (state.frames > 0) R.color.accent else R.color.track
                CastStage.FAILED -> when {
                    failedStep == 0 -> R.color.track
                    position < failedStep -> R.color.accent
                    position == failedStep -> R.color.danger
                    else -> R.color.track
                }
                else -> when {
                    position < step -> R.color.accent
                    position == step ->
                        if (state.stage == CastStage.STREAMING) R.color.success else R.color.cyan
                    else -> R.color.track
                }
            }
            segment.background = pill(colorRes)
        }
        stageProgress.text = when {
            state.stage == CastStage.STOPPED ->
                getString(if (state.sessionId.isBlank()) R.string.stage_not_started else R.string.stage_ended)
            state.stage == CastStage.FAILED -> getString(R.string.stage_interrupted)
            else -> getString(R.string.stage_step, state.stage.step, CastStage.STREAMING.step)
        }
    }

    private fun sourceChipText(state: CastState): String = when {
        state.source.contains("CarLife", ignoreCase = true) -> getString(R.string.source_short_carlife)
        state.source.isNotBlank() -> getString(R.string.source_short_mirror)
        else -> shortSourceLabel(settings.sourceMode)
    }

    private fun shortSourceLabel(mode: SourceMode) = when (mode) {
        SourceMode.AUTO -> getString(R.string.source_short_auto)
        SourceMode.CARLIFE -> getString(R.string.source_short_carlife)
        SourceMode.MIRROR -> getString(R.string.source_short_mirror)
    }

    private fun durationText(state: CastState): String {
        if (state.sessionId.isBlank()) return "—"
        val lastReported = maxOf(
            state.events.lastOrNull()?.elapsedMillis ?: 0L,
            state.metrics.lastOrNull()?.elapsedMillis ?: 0L,
        )
        if (state.sessionId != durationSession) {
            durationSession = state.sessionId
            durationAnchor = SystemClock.elapsedRealtime() - lastReported
        }
        val elapsed = if (state.running) {
            SystemClock.elapsedRealtime() - durationAnchor
        } else {
            lastReported
        }
        return formatDuration(elapsed)
    }

    private fun formatDuration(millis: Long): String {
        val total = (millis / 1000).coerceAtLeast(0)
        val hours = total / 3600
        val minutes = (total % 3600) / 60
        val seconds = total % 60
        return if (hours > 0) {
            "%d:%02d:%02d".format(Locale.US, hours, minutes, seconds)
        } else {
            "%02d:%02d".format(Locale.US, minutes, seconds)
        }
    }

    // ---------------------------------------------------------------- actions

    @android.annotation.SuppressLint("MissingPermission")
    private fun onPrimaryAction() {
        if (CastLog.state.value.running) {
            startService(Intent(this, CastService::class.java).setAction(CastService.ACTION_STOP))
            return
        }
        if (pendingCapture) return
        pendingStartSession = true
        if (!ensurePermissions()) return
        pendingStartSession = false
        val peer = selectedPeer() ?: run {
            toast(getString(R.string.toast_no_peer))
            return
        }
        settings.peerAddress = peer.first
        settings.peerName = peer.second
        val adapter = getSystemService(BluetoothManager::class.java)?.adapter
        if (adapter?.isEnabled != true) {
            toast(getString(R.string.toast_no_bt))
            return
        }
        val device = adapter.getRemoteDevice(peer.first)
        if (device.bondState != android.bluetooth.BluetoothDevice.BOND_BONDED) {
            toast(getString(R.string.toast_pair_first))
            return
        }
        // The button always starts the session. It never diverts to Honor's page.
        //
        // An earlier revision did divert on the first connection, and that was a
        // dead end: Honor's flow has to be left open for CarLife to stay
        // connected, so coming back to this app tore the car link down, and
        // nothing here had started a session in the first place. The driver was
        // stuck with no way forward. Starting first and offering the page as a
        // separate hint cannot get anyone stuck: if this app can complete the
        // handshake by itself it just works, and if the car really does need
        // Honor's grant, the failure text says so while the hint is still one
        // tap away in settings.
        requestCaptureIfNeeded()
    }

    private fun startSession(capture: Intent?, captureResult: Int) {
        val request = Intent(this, CastService::class.java)
            .setAction(CastService.ACTION_START)
            .putExtra(CastService.EXTRA_CAPTURE_RESULT, captureResult)
        if (capture != null) {
            if (Build.VERSION.SDK_INT >= 33) {
                request.putExtra(CastService.EXTRA_CAPTURE, capture)
            } else {
                @Suppress("DEPRECATION") request.putExtra(CastService.EXTRA_CAPTURE, capture)
            }
        }
        ContextCompat.startForegroundService(this, request)
    }

    private fun requestCaptureIfNeeded(): Boolean {
        val usesCarLife = settings.sourceMode != SourceMode.MIRROR &&
            CastEngine.carLifeAvailable(this)
        if (usesCarLife) {
            startSession(null, Activity.RESULT_CANCELED)
            return true
        }
        if (settings.sourceMode == SourceMode.CARLIFE) {
            toast(getString(R.string.toast_carlife_missing))
            return true
        }
        val manager = getSystemService(MediaProjectionManager::class.java)
        pendingCapture = true
        val consent = if (Build.VERSION.SDK_INT >= 34) {
            manager.createScreenCaptureIntent(
                android.media.projection.MediaProjectionConfig.createConfigForDefaultDisplay()
            )
        } else {
            manager.createScreenCaptureIntent()
        }
        startActivityForResult(consent, REQUEST_CAPTURE)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EXPORT) {
            val file = pendingExport
            pendingExport = null
            val uri = data?.data
            if (resultCode != Activity.RESULT_OK || uri == null || file == null) return
            runCatching {
                contentResolver.openOutputStream(uri)?.use { output ->
                    file.inputStream().use { it.copyTo(output) }
                } ?: error("无法写入所选位置")
            }.onSuccess {
                toast(getString(R.string.toast_export_saved))
            }.onFailure {
                toast(getString(R.string.toast_export_failed, it.message ?: it.javaClass.simpleName))
            }
            return
        }
        if (requestCode == REQUEST_WALLPAPER) {
            val uri = data?.data
            if (resultCode != Activity.RESULT_OK || uri == null) return
            scope.launch(Dispatchers.IO) {
                val bitmap = PanelWallpaperStore.decodeUri(this@MainActivity, uri)
                launch(Dispatchers.Main) {
                    if (bitmap != null) {
                        showWallpaperCropSheet(bitmap)
                    } else {
                        toast(getString(R.string.toast_wallpaper_failed))
                    }
                }
            }
            return
        }
        if (requestCode != REQUEST_CAPTURE) return
        pendingCapture = false
        if (resultCode != Activity.RESULT_OK || data == null) {
            toast(getString(R.string.toast_capture_canceled))
            return
        }
        startSession(data, resultCode)
    }

    private fun ensurePermissions(): Boolean {
        val wanted = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= 31) {
            wanted += Manifest.permission.BLUETOOTH_CONNECT
            wanted += Manifest.permission.BLUETOOTH_SCAN
        }
        if (Build.VERSION.SDK_INT >= 33) {
            wanted += Manifest.permission.NEARBY_WIFI_DEVICES
            wanted += Manifest.permission.POST_NOTIFICATIONS
        }
        if (settings.audio && settings.sourceMode != SourceMode.CARLIFE) {
            wanted += Manifest.permission.RECORD_AUDIO
        }
        val missing = wanted.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (missing.isEmpty()) return true
        requestPermissions(missing.toTypedArray(), REQUEST_PERMISSIONS)
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != REQUEST_PERMISSIONS) return
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            refreshPeers()
            if (pendingHonorCarConnect) {
                pendingHonorCarConnect = false
                openHonorCarConnect()
            } else if (pendingStartSession) {
                pendingStartSession = false
                onPrimaryAction()
            }
        } else {
            pendingStartSession = false
            pendingHonorCarConnect = false
            toast(getString(R.string.toast_no_bt))
        }
    }

    // ---------------------------------------------------------------- peers

    @android.annotation.SuppressLint("MissingPermission")
    private fun refreshPeers() {
        if (Build.VERSION.SDK_INT >= 31 &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ensurePermissions()
            return
        }
        val adapter = getSystemService(BluetoothManager::class.java)?.adapter
        val bonded = adapter?.bondedDevices.orEmpty()
            .map { it.address to (it.name ?: getString(R.string.device_unnamed)) }
            .sortedBy { it.second }
        peers.clear()
        peers += bonded
        selectedPeerIndex = bonded.indexOfFirst { it.first.equals(settings.peerAddress, true) }
        if (selectedPeerIndex >= 0) {
            settings.peerName = bonded[selectedPeerIndex].second
        }
        updateDeviceLabel()
    }

    private fun updateDeviceLabel() {
        if (!::deviceName.isInitialized) return
        val peer = peers.getOrNull(selectedPeerIndex)
        val fallbackName = settings.peerName
        deviceName.text = when {
            peer != null -> "${getString(R.string.device_title)} · ${peer.second}"
            fallbackName != null && settings.peerAddress != null ->
                "${getString(R.string.device_title)} · $fallbackName"
            else -> getString(R.string.device_none)
        }
        deviceName.setTextColor(color(if (peer != null || settings.peerAddress != null) {
            R.color.text_primary
        } else {
            R.color.text_tertiary
        }))
    }

    private fun selectedPeer(): Pair<String, String>? {
        peers.getOrNull(selectedPeerIndex)?.let { return it }
        return settings.peerAddress?.let { it to (settings.peerName ?: it) }
    }

    // ---------------------------------------------------------------- sheets

    private fun showPeerSheet() {
        val (sheet, content) = sheetContent()
        content.addView(sheetTitle(getString(R.string.device_pick_title)))

        /*
         * Say what this list is for before showing it.
         *
         * A bare list of Bluetooth addresses left people guessing which entry
         * was theirs and what selecting one would do. The explanation is one
         * short line, so it costs almost nothing and removes the guess.
         */
        content.addView(TextView(this).apply {
            text = getString(R.string.device_pick_howto)
            style(12f, R.color.text_secondary)
            setLineSpacing(dp(3f).toFloat(), 1f)
            setPadding(0, dp(2f), 0, dp(10f))
        })

        val listBox = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        content.addView(listBox)

        fun renderList() {
            listBox.removeAllViews()
            if (peers.isEmpty()) {
                // An empty list is not a dead end: it names the exact step that
                // is missing instead of repeating "not paired" once.
                listBox.addView(TextView(this).apply {
                    text = getString(R.string.device_pick_empty_hint)
                    style(12.5f, R.color.text_secondary)
                    setLineSpacing(dp(4f).toFloat(), 1f)
                    setPadding(dp(2f), dp(10f), dp(2f), dp(10f))
                })
                return
            }
            peers.forEachIndexed { index, peer ->
                val selected = index == selectedPeerIndex
                // Whole row: bigger tap target than a line of text, and the
                // selected one is filled rather than marked with a tick nobody
                // notices.
                val row = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
                    setPadding(dp(12f), dp(14f), dp(12f), dp(14f))
                    background = if (selected) pill(R.color.accent_soft) else selectableBackground()
                    isClickable = true
                    isFocusable = true
                    setOnClickListener {
                        selectedPeerIndex = index
                        settings.peerAddress = peer.first
                        settings.peerName = peer.second
                        updateDeviceLabel()
                        sheet.dismiss()
                    }
                }
                row.addView(ImageView(this).apply {
                    setImageResource(R.drawable.ic_car)
                    imageTintList = tint(if (selected) R.color.accent else R.color.text_secondary)
                }, LinearLayout.LayoutParams(dp(22f), dp(22f)).apply { marginEnd = dp(12f) })

                val texts = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
                texts.addView(TextView(this).apply {
                    text = peer.second.ifBlank { getString(R.string.device_unnamed) }
                    style(15f, R.color.text_primary, bold = selected)
                    maxLines = 1
                    ellipsize = android.text.TextUtils.TruncateAt.END
                })
                texts.addView(TextView(this).apply {
                    text = peer.first
                    style(11.5f, R.color.text_tertiary, mono = true)
                    setPadding(0, dp(3f), 0, 0)
                })
                row.addView(texts, LinearLayout.LayoutParams(0, -2, 1f))

                if (selected) {
                    row.addView(TextView(this).apply {
                        text = "已选"
                        style(11f, R.color.accent, bold = true)
                    }, LinearLayout.LayoutParams(-2, -2).apply { marginStart = dp(8f) })
                }
                listBox.addView(row, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(2f) })
            }
        }
        renderList()

        val refresh = secondaryButton(getString(R.string.device_refresh)).apply {
            setIconResource(R.drawable.ic_refresh)
            iconSize = dp(18f)
            iconPadding = dp(8f)
            iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
            iconTint = tint(R.color.text_secondary)
            setOnClickListener {
                refreshPeers()
                renderList()
            }
        }
        content.addView(refresh, LinearLayout.LayoutParams(-1, dp(46f)).apply { topMargin = dp(16f) })
        sheet.show()
    }

    private fun showSettings() {
        val (sheet, content) = sheetContent()
        content.addView(sheetTitle(getString(R.string.settings_title)))
        content.addView(TextView(this).apply {
            text = "改动多数在下次连接时生效；标了「立即生效」的会马上起作用。"
            style(11.5f, R.color.text_tertiary)
            setLineSpacing(dp(3f).toFloat(), 1f)
            setPadding(0, 0, 0, dp(10f))
        })

        content.addView(sectionLabel(getString(R.string.settings_section_display)))
        content.addView(buildSourceBlock())
        content.addView(divider(top = 2, bottom = 2))

        content.addView(sectionLabel(getString(R.string.settings_section_audio)))
        content.addView(toggleRow(
            getString(R.string.settings_audio),
            getString(R.string.settings_audio_detail),
            settings.audio,
        ) { settings.audio = it })

        if (settings.sourceMode != SourceMode.MIRROR) {
            content.addView(navRow(
                getString(R.string.settings_wallpaper),
                getString(
                    R.string.settings_wallpaper_detail,
                    wallpaperStatus(),
                ),
            ) { showWallpaperSheet() })
        }

        content.addView(navRow(
            getString(R.string.settings_touch),
            if (isTouchServiceEnabled()) getString(R.string.settings_touch_enabled)
            else getString(R.string.settings_touch_detail),
        ) { openAccessibility() })

        content.addView(navRow(
            getString(R.string.settings_media_access),
            if (isMediaListenerEnabled()) getString(R.string.settings_media_access_enabled)
            else getString(R.string.settings_media_access_detail),
        ) { openMediaListenerSettings() })

        content.addView(sectionLabel(getString(R.string.settings_section_behavior)))
        content.addView(toggleRow(
            getString(R.string.settings_auto_connect),
            getString(R.string.settings_auto_connect_detail),
            settings.autoConnect,
        ) { settings.autoConnect = it })

        content.addView(toggleRow(
            getString(R.string.settings_carlife_auto_session),
            getString(R.string.settings_carlife_auto_session_detail),
            settings.carLifeAutoSession,
        ) { settings.carLifeAutoSession = it })

        content.addView(toggleRow(
            getString(R.string.settings_video_passthrough),
            getString(R.string.settings_video_passthrough_detail),
            settings.videoPassthrough,
        ) { settings.videoPassthrough = it })

        // The first connection has to be started from Honor's own Car Connect
        // page: that flow is what grants CarLife its wireless pairing. Those
        // activities are exported=false, so this app cannot start the connect
        // itself — it can only take the driver to the page that can.
        content.addView(navRow(
            getString(R.string.settings_connect_guide),
            getString(R.string.settings_connect_guide_detail),
        ) { openHonorCarConnect() })

        // Frame rate: a plain 30/60 choice rather than a number picker.
        //
        // The car declares 30 and that stays the default, but a declared
        // maximum is not proof of what it can sustain, so the other value is
        // one tap away instead of requiring a new build. The value applies to
        // the next session, like the other options here.
        content.addView(navRow(
            getString(R.string.settings_frame_rate),
            getString(
                R.string.settings_frame_rate_detail,
                settings.frameRateOverride.takeIf { it > 0 } ?: 30,
            ),
        ) { showFrameRateSheet() })

        content.addView(navRow(
            getString(R.string.settings_safe_area),
            getString(R.string.settings_safe_area_detail),
        ) { showSafeAreaSheet() })

        content.addView(divider(top = 6, bottom = 6))
        content.addView(TextView(this).apply {
            text = getString(R.string.settings_advanced)
            style(10.5f, R.color.text_tertiary, bold = true)
            letterSpacing = 0.08f
            setPadding(0, 0, 0, dp(2f))
        })

        val adapter = getSystemService(BluetoothManager::class.java)?.adapter
        val currentAddress = adapter?.let {
            LocalBluetoothAddress.read(this, it, settings.manualBluetoothAddress)
        }
        content.addView(navRow(
            getString(R.string.settings_bluetooth),
            currentAddress?.let { getString(R.string.settings_bluetooth_current, it) }
                ?: getString(R.string.settings_bluetooth_detail),
            secondary = true,
        ) { editBluetoothAddress() })

        content.addView(navRow(
            getString(R.string.settings_clear_pair),
            getString(R.string.settings_clear_pair_detail),
            secondary = true,
        ) {
            val peer = selectedPeer()
            if (peer == null) {
                toast(getString(R.string.toast_no_peer))
            } else {
                settings.removePair(peer.first)
                // Drop the success record with the pairing it was earned on.
                // Keeping it would let the next press skip Honor's first-time
                // page and walk straight into a handshake that cannot complete.
                settings.clearConnected(peer.first)
                toast(getString(R.string.toast_pair_cleared))
            }
        })

        content.addView(navRow(
            getString(R.string.settings_export),
            getString(R.string.settings_export_detail),
            secondary = true,
        ) { exportLog() })

        content.addView(TextView(this).apply {
            text = getString(R.string.settings_version, BuildConfig.VERSION_NAME)
            style(10.5f, R.color.text_tertiary)
            setPadding(0, dp(18f), 0, 0)
        })
        sheet.show()
    }

    private fun buildSourceBlock(): View {
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, dp(10f), 0, dp(12f))
        }
        box.addView(TextView(this).apply {
            text = getString(R.string.source_title)
            style(14f, R.color.text_primary)
        })

        val detail = TextView(this).apply {
            text = sourceDetail(settings.sourceMode)
            style(11f, R.color.text_tertiary)
            setLineSpacing(dp(2f).toFloat(), 1f)
        }

        val buttons = mutableListOf<Pair<SourceMode, MaterialButton>>()
        fun paint() {
            buttons.forEach { (mode, button) ->
                val selected = mode == settings.sourceMode
                button.backgroundTintList = tint(
                    if (selected) R.color.accent_soft else R.color.surface_high
                )
                button.setTextColor(color(if (selected) R.color.accent else R.color.text_secondary))
                button.strokeWidth = if (selected) dp(1f) else 0
                if (selected) button.strokeColor = tint(R.color.accent)
            }
        }

        val group = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        SourceMode.entries.forEachIndexed { index, mode ->
            val button = MaterialButton(this, null,
                com.google.android.material.R.attr.materialButtonStyle).apply {
                text = shortSourceLabel(mode)
                textSize = 12f
                isAllCaps = false
                cornerRadius = dp(20f)
                insetTop = 0
                insetBottom = 0
                minimumHeight = 0
                stateListAnimator = null
                setPadding(dp(4f), 0, dp(4f), 0)
                setOnClickListener {
                    if (settings.sourceMode == mode) return@setOnClickListener
                    settings.sourceMode = mode
                    detail.text = sourceDetail(mode)
                    sourceBadge.text = shortSourceLabel(mode)
                    paint()
                    if (CastLog.state.value.running) toast(getString(R.string.toast_source_next))
                }
            }
            val params = LinearLayout.LayoutParams(0, dp(40f), 1f)
            if (index > 0) params.marginStart = dp(8f)
            group.addView(button, params)
            buttons += mode to button
        }
        paint()
        box.addView(group, LinearLayout.LayoutParams(-1, dp(40f)).apply { topMargin = dp(10f) })
        box.addView(detail, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(8f) })
        return box
    }

    private fun editBluetoothAddress() {
        val (sheet, content) = sheetContent()
        content.addView(sheetTitle(getString(R.string.dialog_bt_title)))

        val inputLayout = TextInputLayout(this).apply {
            setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE)
            setBoxBackgroundColor(color(R.color.surface))
            setBoxStrokeColorStateList(ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_focused), intArrayOf()),
                intArrayOf(color(R.color.accent), color(R.color.stroke)),
            ))
            setBoxStrokeWidth(dp(1f))
            setBoxStrokeWidthFocused(dp(1f))
            setBoxCornerRadii(dp(14f).toFloat(), dp(14f).toFloat(), dp(14f).toFloat(), dp(14f).toFloat())
            hint = getString(R.string.dialog_bt_title)
            helperText = getString(R.string.dialog_bt_help)
            setHelperTextColor(tint(R.color.text_tertiary))
        }
        val input = TextInputEditText(inputLayout.context).apply {
            setText(settings.manualBluetoothAddress.orEmpty())
            inputType = InputType.TYPE_CLASS_TEXT or
                InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or
                InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
            setSingleLine()
            textSize = 15f
            setTextColor(color(R.color.text_primary))
            setHintTextColor(tint(R.color.text_tertiary))
        }
        inputLayout.addView(input, LinearLayout.LayoutParams(-1, -2))
        content.addView(inputLayout, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(12f) })

        fun save() {
            val value = input.text?.toString()?.trim().orEmpty()
            if (value.isBlank()) {
                settings.manualBluetoothAddress = null
                toast(getString(R.string.toast_bt_cleared))
                sheet.dismiss()
                return
            }
            if (LocalBluetoothAddress.normalize(value) == null) {
                inputLayout.error = getString(R.string.dialog_bt_invalid)
                return
            }
            settings.manualBluetoothAddress = value
            toast(getString(R.string.toast_bt_saved))
            sheet.dismiss()
        }

        val actions = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val clear = secondaryButton(getString(R.string.dialog_clear)).apply {
            textSize = 12f
            setOnClickListener {
                settings.manualBluetoothAddress = null
                toast(getString(R.string.toast_bt_cleared))
                sheet.dismiss()
            }
        }
        actions.addView(clear, LinearLayout.LayoutParams(0, dp(44f), 1f).apply { marginEnd = dp(8f) })
        actions.addView(secondaryButton(getString(R.string.dialog_cancel)).apply {
            setOnClickListener { sheet.dismiss() }
        }, LinearLayout.LayoutParams(-2, dp(44f)).apply { marginEnd = dp(8f) })
        actions.addView(primaryButton(getString(R.string.dialog_save)).apply {
            setOnClickListener { save() }
        }, LinearLayout.LayoutParams(-2, dp(44f)))
        content.addView(actions, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(18f) })
        sheet.show()
    }

    private fun showProtocol() {
        val (sheet, content) = sheetContent()
        content.addView(sheetTitle(getString(R.string.protocol_title)))
        content.addView(TextView(this).apply {
            text = getString(R.string.protocol_subtitle)
            style(11f, R.color.text_tertiary)
            setPadding(0, dp(2f), 0, dp(4f))
        })
        ProtocolContent.sections.forEach { section ->
            content.addView(TextView(this).apply {
                text = section.title
                style(13.5f, R.color.accent, bold = true)
                setPadding(0, dp(16f), 0, dp(4f))
            })
            content.addView(TextView(this).apply {
                text = section.body
                style(12.5f, R.color.text_secondary)
                setLineSpacing(dp(4f).toFloat(), 1f)
            })
        }
        sheet.show()
    }

    // ---------------------------------------------------------------- sheet pieces

    private fun sheetContent(): Pair<BottomSheetDialog, LinearLayout> {
        val sheet = BottomSheetDialog(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(24f), dp(6f), dp(24f), dp(28f))
        }
        val scroll = NestedScrollView(this).apply {
            isFillViewport = false
            isVerticalScrollBarEnabled = false
            addView(content, ViewGroup.LayoutParams(-1, -2))
        }
        sheet.setContentView(scroll)
        sheet.behavior.maxHeight = (resources.displayMetrics.heightPixels * 0.9f).toInt()
        sheet.setOnShowListener {
            sheet.behavior.skipCollapsed = true
            sheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        content.addView(dragHandle())
        return sheet to content
    }

    private fun dragHandle(): View {
        val wrap = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(0, dp(4f), 0, dp(16f))
        }
        wrap.addView(View(this).apply {
            background = pill(R.color.track)
        }, LinearLayout.LayoutParams(dp(36f), dp(4f)))
        return wrap
    }

    private fun sheetTitle(text: String) = TextView(this).apply {
        this.text = text
        style(20f, R.color.text_primary, bold = true)
        setPadding(0, 0, 0, dp(4f))
    }

    /**
     * Picks the frame rate the next session asks for.
     *
     * Only the two values worth testing are offered. 30 is what the car
     * declares and what a real iPhone gets, so it is the default; 60 is there
     * because a declared maximum is not proof of what the decoder sustains, and
     * the only way to know is to run it once and read the journal.
     */
    private fun showFrameRateSheet() {
        val (sheet, content) = sheetContent()
        content.addView(sheetTitle(getString(R.string.settings_frame_rate)))
        val options = listOf(
            0 to getString(R.string.frame_rate_auto),
            30 to getString(R.string.frame_rate_30),
            60 to getString(R.string.frame_rate_60),
        )
        val current = settings.frameRateOverride
        options.forEach { (value, label) ->
            content.addView(navRow(
                label,
                if (value == current) getString(R.string.frame_rate_selected) else "",
            ) {
                settings.frameRateOverride = value
                toast(getString(R.string.toast_source_next))
                sheet.dismiss()
            })
        }
        content.addView(TextView(this).apply {
            text = getString(R.string.frame_rate_note)
            style(11f, R.color.text_tertiary)
            setPadding(dp(4f), dp(10f), dp(4f), 0)
            setLineSpacing(dp(3f).toFloat(), 1f)
        })
        sheet.show()
    }

    private fun showSafeAreaSheet() {
        val (sheet, content) = sheetContent()
        content.addView(sheetTitle(getString(R.string.safe_area_title)))
        val current = settings.safeAreaPolicy()
        val enabled = MaterialSwitch(this).apply {
            text = getString(R.string.safe_area_enabled)
            isChecked = current.enabled
            setTextColor(color(R.color.text_primary))
        }
        content.addView(enabled, LinearLayout.LayoutParams(-1, dp(48f)))

        val options = listOf(
            CarPanelGeometry.SafeAreaMode.STRICT to getString(R.string.safe_area_strict),
            CarPanelGeometry.SafeAreaMode.RELAXED to getString(R.string.safe_area_relaxed),
            CarPanelGeometry.SafeAreaMode.CUSTOM to getString(R.string.safe_area_custom),
            CarPanelGeometry.SafeAreaMode.OFF to getString(R.string.safe_area_off),
        )
        val modes = RadioGroup(this).apply { orientation = RadioGroup.VERTICAL }
        options.forEachIndexed { index, (mode, label) ->
            modes.addView(RadioButton(this).apply {
                id = 54_000 + index
                tag = mode
                text = label
                setTextColor(color(R.color.text_primary))
                buttonTintList = tint(R.color.accent)
            })
        }
        content.addView(modes)

        content.addView(TextView(this).apply {
            text = getString(R.string.safe_area_margin_note)
            style(11f, R.color.text_tertiary)
            setLineSpacing(dp(2f).toFloat(), 1f)
            setPadding(dp(4f), dp(6f), dp(4f), dp(6f))
        })

        val marginRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val inputs = listOf("左", "上", "右", "下").map { hintText ->
            EditText(this).apply {
                hint = hintText
                inputType = InputType.TYPE_CLASS_NUMBER
                setSingleLine(true)
                setTextColor(color(R.color.text_primary))
                setHintTextColor(tint(R.color.text_tertiary))
                backgroundTintList = tint(R.color.accent)
                marginRow.addView(this, LinearLayout.LayoutParams(0, dp(52f), 1f))
            }
        }
        listOf(current.left, current.top, current.right, current.bottom)
            .forEachIndexed { index, value -> inputs[index].setText(value.toString()) }
        content.addView(marginRow)

        val selectedMode = options.indexOfFirst { it.first == current.mode }.coerceAtLeast(0)
        modes.check(54_000 + selectedMode)
        val actions = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        actions.addView(secondaryButton(getString(R.string.dialog_cancel)).apply {
            setOnClickListener { sheet.dismiss() }
        }, LinearLayout.LayoutParams(0, dp(44f), 1f).apply { marginEnd = dp(8f) })
        actions.addView(primaryButton(getString(R.string.dialog_save)).apply {
            setOnClickListener {
                val mode = (modes.findViewById<RadioButton>(modes.checkedRadioButtonId)?.tag
                    as? CarPanelGeometry.SafeAreaMode) ?: CarPanelGeometry.SafeAreaMode.STRICT
                val values = inputs.map { it.text.toString().toIntOrNull()?.coerceAtLeast(0) ?: 0 }
                val panelWidth = CarPanelGeometry.PANEL_WIDTH
                val panelHeight = CarPanelGeometry.PANEL_HEIGHT
                if (mode == CarPanelGeometry.SafeAreaMode.CUSTOM &&
                    (values[0] + values[2] > panelWidth - 320 ||
                        values[1] + values[3] > panelHeight - 160)
                ) {
                    toast("边距过大，请至少保留 320×160")
                    return@setOnClickListener
                }
                settings.saveSafeAreaPolicy(
                    policy = CarPanelGeometry.SafeAreaPolicy(
                        enabled = enabled.isChecked,
                        mode = mode,
                        left = values[0],
                        top = values[1],
                        right = values[2],
                        bottom = values[3],
                    ),
                )
                toast(getString(R.string.toast_source_next))
                sheet.dismiss()
            }
        }, LinearLayout.LayoutParams(0, dp(44f), 1f))
        content.addView(actions, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(16f) })
        sheet.show()
    }

    private fun wallpaperStatus(): String =
        if (PanelWallpaperStore.hasCustomWallpaper(this)) {
            getString(R.string.wallpaper_custom)
        } else {
            getString(R.string.wallpaper_default)
        }

    private fun showWallpaperSheet(initialPreview: android.graphics.Bitmap? = null) {
        val (sheet, content) = sheetContent()
        content.addView(sheetTitle(getString(R.string.settings_wallpaper)))

        val preview = ImageView(this).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            setBackgroundColor(color(R.color.surface_high))
            contentDescription = getString(R.string.wallpaper_preview)
        }
        val previewWidth = (resources.displayMetrics.widthPixels - 2 * dp(24f)).coerceAtLeast(1)
        val previewHeight = WallpaperCropGeometry.viewportHeight(
            previewWidth,
            CarPanelGeometry.PANEL_WIDTH,
            CarPanelGeometry.PANEL_HEIGHT,
        )
        content.addView(preview, LinearLayout.LayoutParams(-1, previewHeight).apply { topMargin = dp(12f) })
        content.addView(TextView(this).apply {
            text = getString(R.string.wallpaper_alpha_note)
            style(11f, R.color.text_tertiary)
            setLineSpacing(dp(2f).toFloat(), 1f)
            setPadding(dp(2f), dp(8f), dp(2f), 0)
        })

        fun updatePreview(bitmap: android.graphics.Bitmap?) {
            preview.setImageBitmap(bitmap)
            preview.setBackgroundColor(
                if (bitmap == null) Color.TRANSPARENT else color(R.color.surface_high),
            )
        }
        if (initialPreview != null) {
            updatePreview(initialPreview)
        } else {
            scope.launch(Dispatchers.IO) {
                val bitmap = PanelWallpaperStore.decode(this@MainActivity, 1280)
                launch(Dispatchers.Main) {
                    if (!isFinishing && !isDestroyed) updatePreview(bitmap)
                }
            }
        }

        val actions = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        actions.addView(secondaryButton(getString(R.string.wallpaper_reset)).apply {
            setOnClickListener {
                if (PanelWallpaperStore.reset(this@MainActivity)) {
                    updatePreview(null)
                    toast(getString(R.string.toast_wallpaper_reset))
                } else {
                    toast(getString(R.string.wallpaper_default))
                }
            }
        }, LinearLayout.LayoutParams(0, dp(44f), 1f).apply { marginEnd = dp(8f) })
        actions.addView(primaryButton(getString(R.string.wallpaper_choose)).apply {
            setOnClickListener {
                sheet.dismiss()
                launchWallpaperPicker()
            }
        }, LinearLayout.LayoutParams(0, dp(44f), 1f))
        content.addView(actions, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(16f) })
        /*
         * Bundled wallpapers, one row below the picker.
         *
         * The picker is the flexible path but it needs the driver to already
         * own a 1972×520 image; almost nobody does, so the feature looked
         * broken. These are the same nine images an established sender ships, cropped to
         * the panel by [BuiltInWallpapers] instead of by the user.
         */
        content.addView(secondaryButton(getString(R.string.wallpaper_builtin)).apply {
            setOnClickListener {
                sheet.dismiss()
                showBuiltInWallpaperSheet()
            }
        }, LinearLayout.LayoutParams(-1, dp(44f)).apply { topMargin = dp(8f) })
        sheet.show()
    }

    /**
     * Grid of the bundled wallpapers.
     *
     * Applying is done off the UI thread: each entry is a multi-megapixel JPEG
     * that has to be decoded, scaled and cropped, which is long enough to drop
     * frames if it ran inline.
     */
    private fun showBuiltInWallpaperSheet() {
        val (sheet, content) = sheetContent()
        content.addView(sheetTitle(getString(R.string.wallpaper_builtin)))
        content.addView(TextView(this).apply {
            text = getString(R.string.wallpaper_builtin_note)
            style(11f, R.color.text_tertiary)
            setLineSpacing(dp(2f).toFloat(), 1f)
            setPadding(0, dp(2f), 0, dp(10f))
        })

        val columns = 3
        var row: LinearLayout? = null
        BuiltInWallpapers.entries.forEachIndexed { index, entry ->
            if (index % columns == 0) {
                row = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
                }
                content.addView(row, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(8f) })
            }
            row?.addView(builtInWallpaperTile(entry) { sheet.dismiss() },
                LinearLayout.LayoutParams(0, -2, 1f).apply {
                    marginEnd = if (index % columns == columns - 1) 0 else dp(8f)
                })
        }
        sheet.show()
    }

    private fun builtInWallpaperTile(
        entry: BuiltInWallpapers.Entry,
        onApplied: () -> Unit,
    ): View {
        val tile = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            background = selectableBackground()
            isClickable = true
            isFocusable = true
        }
        val preview = ImageView(this).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            setBackgroundColor(color(R.color.surface_high))
        }
        val height = (resources.displayMetrics.widthPixels / 3.4).toInt()
            .coerceAtLeast(dp(56f))
        tile.addView(preview, LinearLayout.LayoutParams(-1, height))
        tile.addView(TextView(this).apply {
            text = entry.label
            style(11f, R.color.text_secondary)
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(0, dp(6f), 0, 0)
        }, LinearLayout.LayoutParams(-1, -2))

        // Thumbnails are decoded lazily at tile size; decoding nine full-size
        // JPEGs just to show a grid would cost more memory than the panel.
        scope.launch(Dispatchers.IO) {
            val bitmap = runCatching {
                assets.open(entry.asset).use { stream ->
                    val bounds = android.graphics.BitmapFactory.Options().apply { inJustDecodeBounds = true }
                    android.graphics.BitmapFactory.decodeStream(stream, null, bounds)
                    val sample = (bounds.outWidth / 480).coerceAtLeast(1)
                    assets.open(entry.asset).use { second ->
                        android.graphics.BitmapFactory.decodeStream(
                            second, null,
                            android.graphics.BitmapFactory.Options().apply { inSampleSize = sample },
                        )
                    }
                }
            }.getOrNull()
            launch(Dispatchers.Main) {
                if (!isFinishing && !isDestroyed) preview.setImageBitmap(bitmap)
            }
        }

        tile.setOnClickListener {
            it.isEnabled = false
            scope.launch(Dispatchers.IO) {
                val applied = BuiltInWallpapers.apply(this@MainActivity, entry)
                launch(Dispatchers.Main) {
                    it.isEnabled = true
                    if (applied) {
                        toast(getString(R.string.toast_wallpaper_saved))
                        onApplied()
                    } else {
                        // PanelWallpaperStore only replaces the file after the
                        // new one is written, so a failure here leaves the
                        // previous wallpaper in place — say so rather than
                        // implying the panel is now blank.
                        toast(getString(R.string.wallpaper_builtin_failed))
                    }
                }
            }
        }
        return tile
    }

    private fun showWallpaperCropSheet(bitmap: android.graphics.Bitmap) {
        val (sheet, content) = sheetContent()
        content.addView(sheetTitle(getString(R.string.wallpaper_crop_title)))
        content.addView(TextView(this).apply {
            text = getString(R.string.wallpaper_crop_ratio)
            style(11f, R.color.text_tertiary)
            setPadding(0, dp(2f), 0, dp(8f))
        })
        val frameWidth = (resources.displayMetrics.widthPixels - 2 * dp(24f)).coerceAtLeast(1)
        val frameHeight = WallpaperCropGeometry.viewportHeight(
            frameWidth,
            CarPanelGeometry.PANEL_WIDTH,
            CarPanelGeometry.PANEL_HEIGHT,
        )
        val crop = WallpaperCropView(
            context = this,
            source = bitmap,
        ).apply {
            setBackgroundColor(color(R.color.surface_high))
        }
        content.addView(crop, LinearLayout.LayoutParams(-1, frameHeight))
        sheet.setOnDismissListener {
            if (!bitmap.isRecycled) bitmap.recycle()
        }

        val actions = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        actions.addView(secondaryButton(getString(R.string.wallpaper_crop_reselect)).apply {
            setOnClickListener {
                sheet.dismiss()
                launchWallpaperPicker()
            }
        }, LinearLayout.LayoutParams(0, dp(44f), 1f).apply { marginEnd = dp(8f) })
        actions.addView(primaryButton(getString(R.string.wallpaper_crop_apply)).apply {
            setOnClickListener { button ->
                button.isEnabled = false
                val cropped = runCatching { crop.createCroppedBitmap() }.getOrElse {
                    button.isEnabled = true
                    toast(getString(R.string.toast_wallpaper_failed))
                    return@setOnClickListener
                }
                sheet.dismiss()
                scope.launch(Dispatchers.IO) {
                    val saved = PanelWallpaperStore.setFromBitmap(this@MainActivity, cropped)
                    cropped.recycle()
                    val preview = if (saved) PanelWallpaperStore.decode(this@MainActivity, 1280) else null
                    launch(Dispatchers.Main) {
                        if (saved) {
                            toast(getString(R.string.toast_wallpaper_saved))
                            showWallpaperSheet(preview)
                        } else {
                            toast(getString(R.string.toast_wallpaper_failed))
                        }
                    }
                }
            }
        }, LinearLayout.LayoutParams(0, dp(44f), 1f))
        content.addView(actions, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(16f) })
        sheet.show()
    }

    private fun launchWallpaperPicker() {
        val picker = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        runCatching { startActivityForResult(picker, REQUEST_WALLPAPER) }
            .onFailure { toast(getString(R.string.toast_wallpaper_picker_failed)) }
    }

    private fun navRow(
        title: String,
        detail: String,
        secondary: Boolean = false,
        onClick: () -> Unit,
    ): View {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(4f), dp(12f), dp(4f), dp(12f))
            background = selectableBackground()
            isClickable = true
            isFocusable = true
            setOnClickListener { onClick() }
        }
        val texts = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        texts.addView(TextView(this).apply {
            text = title
            style(14f, if (secondary) R.color.text_secondary else R.color.text_primary)
        })
        texts.addView(TextView(this).apply {
            text = detail
            style(11f, R.color.text_tertiary)
            setPadding(0, dp(2f), 0, 0)
            setLineSpacing(dp(2f).toFloat(), 1f)
        })
        row.addView(texts, LinearLayout.LayoutParams(0, -2, 1f))
        row.addView(ImageView(this).apply {
            setImageResource(R.drawable.ic_chevron_right)
            imageTintList = tint(R.color.text_tertiary)
        }, LinearLayout.LayoutParams(dp(16f), dp(16f)).apply { marginStart = dp(8f) })
        return row
    }

    private fun toggleRow(
        title: String,
        detail: String,
        checked: Boolean,
        onChange: (Boolean) -> Unit,
    ): View {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(4f), dp(10f), dp(4f), dp(10f))
        }
        val texts = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        texts.addView(TextView(this).apply {
            text = title
            style(14f, R.color.text_primary)
        })
        texts.addView(TextView(this).apply {
            text = detail
            style(11f, R.color.text_tertiary)
            setPadding(0, dp(2f), 0, 0)
            setLineSpacing(dp(2f).toFloat(), 1f)
        })
        row.addView(texts, LinearLayout.LayoutParams(0, -2, 1f))
        row.addView(MaterialSwitch(this).apply {
            isChecked = checked
            contentDescription = title
            setOnCheckedChangeListener { _, value -> onChange(value) }
        }, LinearLayout.LayoutParams(-2, -2).apply { marginStart = dp(12f) })
        return row
    }

    private fun divider(top: Int = 4, bottom: Int = 4): View = View(this).apply {
        setBackgroundColor(color(R.color.track))
        layoutParams = LinearLayout.LayoutParams(-1, dp(1f)).apply {
            topMargin = dp(top.toFloat())
            bottomMargin = dp(bottom.toFloat())
        }
    }

    private fun primaryButton(label: String): MaterialButton =
        MaterialButton(this, null, com.google.android.material.R.attr.materialButtonStyle).apply {
            text = label
            textSize = 14f
            isAllCaps = false
            backgroundTintList = tint(R.color.accent)
            setTextColor(color(R.color.on_accent))
            cornerRadius = dp(14f)
            insetTop = 0
            insetBottom = 0
            minimumHeight = 0
            stateListAnimator = null
            setPadding(dp(18f), 0, dp(18f), 0)
        }

    private fun secondaryButton(label: String): MaterialButton =
        MaterialButton(this, null,
            com.google.android.material.R.attr.materialButtonOutlinedStyle).apply {
            text = label
            textSize = 13f
            isAllCaps = false
            setTextColor(color(R.color.text_secondary))
            strokeColor = tint(R.color.stroke)
            strokeWidth = dp(1f)
            cornerRadius = dp(14f)
            insetTop = 0
            insetBottom = 0
            minimumHeight = 0
            stateListAnimator = null
            setPadding(dp(12f), 0, dp(12f), 0)
        }

    /** A small heading that groups related settings. */
    private fun sectionLabel(text: String): View = TextView(this).apply {
        this.text = text
        style(11.5f, R.color.accent, bold = true)
        letterSpacing = 0.06f
        setPadding(0, dp(14f), 0, dp(6f))
    }

    /** A compact labelled button for the top bar: readable without an icon. */
    private fun textButton(label: String, onClick: () -> Unit): View {
        val view = TextView(this).apply {
            text = label
            style(13f, R.color.text_primary, bold = true)
            gravity = Gravity.CENTER
            setPadding(dp(12f), 0, dp(12f), 0)
            background = pill(R.color.surface_high)
            isClickable = true
            isFocusable = true
            setOnClickListener { onClick() }
        }
        view.layoutParams = LinearLayout.LayoutParams(-2, dp(36f))
        return view
    }

    private fun iconButton(
        icon: Int,
        description: String,
        size: Int = 40,
        onClick: () -> Unit,
    ): MaterialButton =
        MaterialButton(this, null, com.google.android.material.R.attr.materialIconButtonStyle).apply {
            setIconResource(icon)
            iconTint = tint(R.color.text_secondary)
            iconSize = dp(20f)
            contentDescription = description
            insetTop = 0
            insetBottom = 0
            setOnClickListener { onClick() }
            layoutParams = LinearLayout.LayoutParams(dp(size.toFloat()), dp(size.toFloat())).apply {
                marginStart = dp(4f)
            }
        }

    private fun selectableBackground(): Drawable? {
        val value = TypedValue()
        theme.resolveAttribute(android.R.attr.selectableItemBackground, value, true)
        return if (value.resourceId != 0) ContextCompat.getDrawable(this, value.resourceId) else null
    }

    private fun card(radiusDp: Float): MaterialCardView = MaterialCardView(this).apply {
        setCardBackgroundColor(color(R.color.surface))
        radius = dp(radiusDp).toFloat()
        strokeColor = color(R.color.stroke)
        strokeWidth = dp(1f)
        cardElevation = 0f
        rippleColor = tint(R.color.stroke_soft)
    }

    private fun tint(res: Int): ColorStateList = ColorStateList.valueOf(color(res))

    private fun color(res: Int): Int = ContextCompat.getColor(this, res)

    private class Metric(
        val card: MaterialCardView,
        val value: TextView,
        val unit: TextView,
    )

    // ---------------------------------------------------------------- misc

    private fun sourceDetail(mode: SourceMode) = when (mode) {
        SourceMode.AUTO -> getString(R.string.source_auto_detail)
        SourceMode.CARLIFE -> getString(R.string.source_carlife_detail)
        SourceMode.MIRROR -> getString(R.string.source_mirror_detail)
    }

    private fun isTouchServiceEnabled(): Boolean {
        val expected = "$packageName/${TouchService::class.java.name}"
        val enabled = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
        ).orEmpty()
        return enabled.split(':').any { it.equals(expected, ignoreCase = true) } ||
            TouchBridge.available()
    }

    private fun isMediaListenerEnabled(): Boolean {
        val expected = "$packageName/${MediaListenerService::class.java.name}"
        val enabled = Settings.Secure.getString(
            contentResolver,
            "enabled_notification_listeners",
        ).orEmpty()
        return enabled.split(':').any { it.equals(expected, ignoreCase = true) }
    }

    override fun onResume() {
        super.onResume()
        if (::sourceBadge.isInitialized) {
            sourceBadge.text = sourceChipText(CastLog.state.value)
        }
        if (CastLog.state.value.sessionId.isNotBlank()) {
            CastLog.event("INFO", "主界面回到前台")
        }
    }

    override fun onStop() {
        if (CastLog.state.value.sessionId.isNotBlank()) {
            CastLog.event("INFO", "主界面进入后台，finishing=$isFinishing")
        }
        super.onStop()
    }

    private fun openAccessibility() {
        runCatching { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }
            .onFailure { toast(getString(R.string.toast_accessibility_failed)) }
    }

    private fun openMediaListenerSettings() {
        runCatching {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }.onFailure {
            toast(getString(R.string.toast_media_access_failed))
        }
    }

    /**
     * Takes the driver to Honor's Car Connect page.
     *
     * The first connection is not something this app can start: the whole
     * flow — 通用模式 → 通过无线 WiFi 连接 → 下一步 → 开始连接 — lives in
     * com.hihonor.auto and every one of those activities is `exported=false`
     * (verified in its manifest), so no third-party app can launch them. What
     * that flow does is grant CarLife its wireless pairing; afterwards CarLife
     * reconnects on its own when it sees the car's Bluetooth link.
     *
     * `com.hihonor.auto.settings.ManagerActivity` is the one entry point that
     * is both exported and free of a signature permission, so this lands the
     * driver one tap away from the button instead of in the system settings.
     * Each candidate is tried in order and the first that resolves wins; if
     * none do, the system settings page is the fallback.
     */
    private fun openHonorCarConnect() {
        // The session is started here, before the page opens, and keeps running
        // in the background while the driver is inside Honor's flow.
        //
        // This is the whole point. Honor's page cancels the connection the
        // moment it is left, so "open it, then come back and start" can never
        // work — it loops. Starting first means the app's own handshake is
        // already under way and does not depend on that page staying in front;
        // the page only supplies the grant CarLife wants. When the driver
        // leaves it, nothing here needs to be resumed.
        /*
         * Open the page first; start the background attempt second.
         *
         * These used to be the other way round, with  on the permission
         * path — so a permission prompt, or any hiccup in starting the session,
         * produced a button that did nothing at all. Tapping it must always
         * reach Honor's page; whether a session is also started is a separate
         * concern that must not be able to block that.
         */
        if (!CastLog.state.value.running && !pendingCapture) {
            CastLog.event("INFO", "打开荣耀车联的同时在后台开始投屏")
            pendingHonorCarConnect = true
            if (ensurePermissions()) {
                pendingHonorCarConnect = false
                runCatching { requestCaptureIfNeeded() }
                    .onFailure { CastLog.event("WARN", "后台开始投屏失败：${it.message}") }
            }
        }
        val candidates = listOf(
            Intent().setClassName(HONOR_AUTO_PACKAGE, HONOR_AUTO_MANAGER),
            // The CarLife-specific settings alias, when the build declares it.
            Intent().setClassName(HONOR_AUTO_PACKAGE, "$HONOR_AUTO_PACKAGE.settings.carlifeSettingsAlias"),
            // Fall back to the app's own entry screen rather than nothing.
            Intent(Intent.ACTION_MAIN)
                .setClassName(HONOR_AUTO_PACKAGE, "$HONOR_AUTO_PACKAGE.HonorAutoMainActivity"),
        )
        for (intent in candidates) {
            val reached = runCatching {
                startActivity(intent)
                true
            }.getOrElse { failure ->
                CastLog.event("WARN", "打开荣耀车联失败（${intent.component?.className}）：${failure.message}")
                false
            }
            if (reached) return
        }
        // Last resort: the Car Connect entry inside the system settings app.
        val settingsFallback = runCatching {
            startActivity(Intent("com.android.settings").setClassName(
                "com.android.settings",
                "com.android.settings.Settings\$HonorAutoSettingsActivity",
            ))
            true
        }.getOrDefault(false)
        if (!settingsFallback) {
            runCatching { startActivity(Intent(Settings.ACTION_SETTINGS)) }
            toast(getString(R.string.toast_connect_guide_failed))
        }
    }

    /**
     * Writes the session zip and lets the user pick where it goes.
     *
     * The old path shared a file:// URI from the app-private directory, which
     * Android refuses to hand to other apps and which no file manager can see.
     * The system create-document picker writes anywhere the user chooses.
     */
    private fun exportLog() {
        val sessions = CastLog.sessions()
        if (sessions.isEmpty()) {
            toast(getString(R.string.toast_export_empty))
            return
        }
        val formatter = java.text.SimpleDateFormat("MM-dd HH:mm:ss", Locale.getDefault())
        val labels = sessions.map { session ->
            val date = formatter.format(java.util.Date(session.startedAtMillis))
            val source = session.source.ifBlank { if (session.legacy) "旧版会话" else "未知来源" }
            val status = if (session.hasCrash) "有退出/崩溃记录" else session.stage
            "$date · $source · $status · ${session.frames} 帧"
        }
        AlertDialog.Builder(this)
            .setTitle(R.string.export_session_title)
            .setItems(labels.toTypedArray()) { _, index ->
                exportLog(sessions[index].sessionId)
            }
            .setNegativeButton(R.string.dialog_cancel, null)
            .show()
    }

    private fun exportLog(sessionId: String) {
        val file = CastLog.export(sessionId)
        if (file == null) {
            toast(getString(R.string.toast_export_empty))
            return
        }
        pendingExport = file
        val name = "carlink-${sessionId.take(8)}.zip"
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/zip"
            putExtra(Intent.EXTRA_TITLE, name)
        }
        runCatching { startActivityForResult(intent, REQUEST_EXPORT) }
            .onFailure {
                pendingExport = null
                shareExported(file)
            }
    }

    /** Fallback for devices without a document picker: share through a content URI. */
    private fun shareExported(file: java.io.File) {
        val uri = runCatching {
            FileProvider.getUriForFile(this, "$packageName.logs", file)
        }.getOrNull() ?: run {
            toast(getString(R.string.toast_export_failed, file.name))
            return
        }
        val share = Intent(Intent.ACTION_SEND).apply {
            type = "application/zip"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        runCatching { startActivity(Intent.createChooser(share, getString(R.string.settings_export))) }
            .onFailure { toast(getString(R.string.toast_export_failed, it.message ?: "")) }
    }

    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    override fun onDestroy() {
        if (CastLog.state.value.sessionId.isNotBlank()) {
            CastLog.event(
                "INFO",
                "主界面销毁，finishing=$isFinishing，changingConfigurations=$isChangingConfigurations",
            )
        }
        scope.cancel()
        super.onDestroy()
    }

        private companion object {
        const val PAGE_COUNT = 3
            const val REQUEST_PERMISSIONS = 21
            const val REQUEST_CAPTURE = 22
            const val REQUEST_EXPORT = 23
            const val REQUEST_WALLPAPER = 24

            /** Honor's Car Connect app, which owns the first-connection flow. */
            const val HONOR_AUTO_PACKAGE = "com.hihonor.auto"

            /**
             * Its settings entry. Exported and free of a signature permission,
             * unlike every page of the connect flow itself.
             */
            const val HONOR_AUTO_MANAGER = "$HONOR_AUTO_PACKAGE.settings.ManagerActivity"
        }

}
