package com.carlink.monitor

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * 监测主界面。
 *
 * 只有一个目的：**断连之后能立刻看到「谁先死」**。所以界面顺序按这个问题的
 * 重要性排：结论在最上面，其次实时快照，最后是原始记录。
 */
class MonitorActivity : AppCompatActivity() {

    private lateinit var statusLine: TextView
    private lateinit var verdictList: LinearLayout
    private lateinit var rawLog: TextView
    private lateinit var startButton: Button

    private val refresh = object : Runnable {
        override fun run() {
            render()
            verdictList.postDelayed(this, 1000L)
        }
    }

    private val onStateChanged: () -> Unit = { runOnUiThread { render() } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(buildLayout())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        MonitorState.observe(onStateChanged)
    }

    override fun onStart() {
        super.onStart()
        verdictList.post(refresh)
    }

    override fun onStop() {
        verdictList.removeCallbacks(refresh)
        super.onStop()
    }

    override fun onDestroy() {
        MonitorState.forget(onStateChanged)
        super.onDestroy()
    }

    private fun render() {
        val (samples, events) = MonitorState.snapshot()
        val sample = samples.lastOrNull()

        statusLine.text = when {
            !MonitorState.running -> "未在监测"
            sample == null -> "已启动，等待第一轮采样…"
            !sample.hasRoot -> "没有 root：请先取得 su 授权"
            else -> "${samples.size} 轮 · 每 ${MonitorState.intervalMillis / 1000} 秒一次 · " +
                Verdict.line(sample)
        }
        statusLine.setTextColor(when {
            !MonitorState.running -> MUTED
            sample?.hasRoot == false -> Color.rgb(200, 60, 60)
            else -> Color.rgb(30, 130, 70)
        })

        startButton.text = if (MonitorState.running) "停止监测" else "开始监测"

        // 事件倒序：最新的在最上面，断连那一刻就在第一行
        verdictList.removeAllViews()
        events.asReversed().take(MAX_SHOWN_EVENTS).forEach { event ->
            verdictList.addView(TextView(this).apply {
                text = "${time(event.atMillis)}  ${event.text}"
                setTextColor(
                    when (event.severity) {
                        Verdict.Severity.ALERT -> Color.rgb(200, 40, 40)
                        Verdict.Severity.NOTICE -> Color.rgb(190, 120, 20)
                        Verdict.Severity.INFO -> Color.rgb(70, 70, 70)
                    },
                )
                textSize = 13f
                setPadding(0, dp(6), 0, dp(6))
            })
        }
        if (events.isEmpty()) {
            verdictList.addView(TextView(this).apply {
                text = if (MonitorState.running) "还没有值得记录的变化。" else "点上面的按钮开始。"
                setTextColor(MUTED); textSize = 13f
            })
        }

        rawLog.text = samples.asReversed().take(MAX_SHOWN_SAMPLES)
            .joinToString("\n") { "${time(it.atMillis)}  ${Verdict.line(it)}" }
            .ifEmpty { "（无）" }
    }

    private fun buildLayout(): ScrollView {
        val root = ScrollView(this)
        val column = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(16), dp(16), dp(16))
        }
        root.addView(column, LayoutParamsCompat())

        column.addView(title("CarLife 运行监测"))
        column.addView(hint(
            "回答一个问题：断连的时候，CarLife 还活着吗？\n" +
                "只读，不改任何东西。需要 root。",
        ))

        statusLine = TextView(this).apply {
            textSize = 13f
            typeface = Typeface.MONOSPACE
            setPadding(0, dp(12), 0, dp(12))
        }
        column.addView(statusLine)

        startButton = Button(this).apply {
            text = "开始监测"
            setOnClickListener {
                if (MonitorState.running) {
                    MonitorService.stop(this@MonitorActivity)
                    MonitorState.running = false
                } else {
                    MonitorService.start(this@MonitorActivity)
                    this@MonitorActivity.runOnUiThread { render() }
                }
            }
        }
        column.addView(startButton)

        val actions = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, dp(8), 0, dp(8))
        }
        actions.addView(smallButton("复制全部") {
            val text = MonitorState.export()
            if (text.isBlank()) return@smallButton
            getSystemService(ClipboardManager::class.java)
                .setPrimaryClip(ClipData.newPlainText("CarLife 监测", text))
            toast("已复制 ${text.length} 字")
        })
        actions.addView(smallButton("清空") {
            MonitorState.clear()
            render()
        })
        column.addView(actions)

        column.addView(section("判读"))
        verdictList = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        column.addView(verdictList)

        column.addView(section("每轮采样"))
        rawLog = TextView(this).apply {
            textSize = 11f
            typeface = Typeface.MONOSPACE
            setTextColor(MUTED)
            movementMethod = ScrollingMovementMethod()
        }
        column.addView(rawLog)

        return root
    }

    private fun title(text: String) = TextView(this).apply {
        this.text = text
        textSize = 20f
        setTypeface(typeface, Typeface.BOLD)
    }

    private fun hint(text: String) = TextView(this).apply {
        this.text = text
        textSize = 12f
        setTextColor(MUTED)
        setPadding(0, dp(6), 0, 0)
    }

    private fun section(text: String) = TextView(this).apply {
        this.text = text
        textSize = 13f
        setTypeface(typeface, Typeface.BOLD)
        setPadding(0, dp(18), 0, dp(4))
    }

    private fun smallButton(label: String, onClick: () -> Unit) = Button(this).apply {
        text = label
        textSize = 12f
        setOnClickListener { onClick() }
    }

    private fun toast(text: String) =
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()

    private fun time(millis: Long): String {
        val date = java.util.Date(millis)
        return java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.US).format(date)
    }

    /** 让布局参数既可编译又不用为一个 MATCH_PARENT 引入完整 ViewGroup 容器。 */
    private fun LayoutParamsCompat() = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

    private companion object {
        const val MAX_SHOWN_EVENTS = 200
        const val MAX_SHOWN_SAMPLES = 200
        val MUTED = Color.rgb(120, 120, 120)
    }
}
