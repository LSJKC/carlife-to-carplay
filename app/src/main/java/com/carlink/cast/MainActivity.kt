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
        setContentView(buildScreen())
        bindState()
        refreshPeers()
    }

    // ---------------------------------------------------------------- layout

    private fun buildScreen(): View {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(color(R.color.bg))
        }
        root.addView(buildTopBar())

        val scroll = ScrollView(this).apply {
            isFillViewport = true
            isVerticalScrollBarEnabled = false
        }
        val column = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20f), dp(4f), dp(20f), dp(28f))
        }
        scroll.addView(column, ViewGroup.LayoutParams(-1, -2))
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        column.addView(buildDevicePill(), LinearLayout.LayoutParams(-1, dp(46f)))
        column.addView(buildHero(), LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(12f) })
        column.addView(buildMetricsGrid(), LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(16f) })
        column.addView(buildStageCard(), LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(12f) })
        column.addView(buildLogCard(), LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(12f) })
        return root
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

        bar.addView(iconButton(R.drawable.ic_info, getString(R.string.protocol_title)) { showProtocol() })
        bar.addView(iconButton(R.drawable.ic_settings, getString(R.string.settings_title)) { showSettings() })
        return bar
    }

    private fun buildDevicePill(): View {
        val pillCard = card(23f).apply {
            isClickable = true
            isFocusable = true
            setOnClickListener { showPeerSheet() }
        }
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(14f), 0, dp(5f), 0)
        }
        pillCard.addView(row, FrameLayout.LayoutParams(-1, -1))

        row.addView(ImageView(this).apply {
            setImageResource(R.drawable.ic_car)
            imageTintList = tint(R.color.accent)
        }, LinearLayout.LayoutParams(dp(18f), dp(18f)))

        deviceName = TextView(this).apply {
            text = getString(R.string.device_none)
            style(13f, R.color.text_primary)
            maxLines = 1
            ellipsize = android.text.TextUtils.TruncateAt.END
        }
        row.addView(deviceName, LinearLayout.LayoutParams(0, -2, 1f).apply { marginStart = dp(10f) })

        sourceBadge = TextView(this).apply {
            text = shortSourceLabel(settings.sourceMode)
            style(10f, R.color.text_secondary)
            setPadding(dp(8f), dp(3f), dp(8f), dp(3f))
            background = pill(R.color.track)
        }
        row.addView(sourceBadge, LinearLayout.LayoutParams(-2, -2).apply { marginEnd = dp(2f) })

        row.addView(iconButton(R.drawable.ic_refresh, getString(R.string.device_refresh), size = 36) {
            refreshPeers()
            toast(getString(R.string.toast_refreshed))
        })
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
            "%02d:%02d  %s".format(Locale.US, seconds / 60, seconds % 60, it.message)
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
        val listBox = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, dp(6f), 0, 0)
        }
        content.addView(listBox)

        fun renderList() {
            listBox.removeAllViews()
            if (peers.isEmpty()) {
                listBox.addView(TextView(this).apply {
                    text = getString(R.string.device_hint)
                    style(12f, R.color.text_tertiary)
                    setLineSpacing(dp(3f).toFloat(), 1f)
                    setPadding(0, dp(8f), 0, dp(8f))
                })
                return
            }
            peers.forEachIndexed { index, peer ->
                val row = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
                    setPadding(dp(4f), dp(12f), dp(4f), dp(12f))
                    background = selectableBackground()
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
                    imageTintList = tint(R.color.accent)
                }, LinearLayout.LayoutParams(dp(20f), dp(20f)).apply { marginEnd = dp(12f) })

                val texts = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
                texts.addView(TextView(this).apply {
                    text = peer.second
                    style(14f, R.color.text_primary)
                    maxLines = 1
                    ellipsize = android.text.TextUtils.TruncateAt.END
                })
                texts.addView(TextView(this).apply {
                    text = peer.first
                    style(11f, R.color.text_tertiary, mono = true)
                    setPadding(0, dp(2f), 0, 0)
                })
                row.addView(texts, LinearLayout.LayoutParams(0, -2, 1f))

                if (index == selectedPeerIndex) {
                    row.addView(ImageView(this).apply {
                        setImageResource(R.drawable.ic_check)
                        imageTintList = tint(R.color.accent)
                    }, LinearLayout.LayoutParams(dp(18f), dp(18f)))
                }
                listBox.addView(row, LinearLayout.LayoutParams(-1, -2))
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
        content.addView(refresh, LinearLayout.LayoutParams(-1, dp(44f)).apply { topMargin = dp(14f) })
        sheet.show()
    }

    private fun showSettings() {
        val (sheet, content) = sheetContent()
        content.addView(sheetTitle(getString(R.string.settings_title)))

        content.addView(buildSourceBlock())
        content.addView(divider(top = 2, bottom = 2))

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
        sheet.show()
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
        if (!CastLog.state.value.running && !pendingCapture) {
            CastLog.event("INFO", "打开荣耀车联的同时在后台开始投屏")
            pendingHonorCarConnect = true
            if (ensurePermissions()) {
                pendingHonorCarConnect = false
                requestCaptureIfNeeded()
            } else {
                return
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
