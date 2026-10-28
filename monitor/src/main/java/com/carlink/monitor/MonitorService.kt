package com.carlink.monitor

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * 后台采样服务。
 *
 * 必须是前台服务：断连发生在用投屏的时候，那时用户根本不在这个应用的界面
 * 上。普通后台服务会被冻结，采样一断，最该看的那几秒正好没有记录。
 */
class MonitorService : Service() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var job: Job? = null
    private val sampler = Sampler()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopSelf()
            return START_NOT_STICKY
        }
        startForeground(NOTIFICATION_ID, notification("正在采样"))
        if (job == null) job = scope.launch { loop() }
        return START_STICKY
    }

    private suspend fun loop() {
        var previous = Sample(0, emptyList(), emptyList(), null, null, null, emptyMap())
        var havePrevious = false
        while (scope.isActive) {
            val interval = MonitorState.intervalMillis
            if (!MonitorState.running) break
            val sample = sampler.sample()
            MonitorState.push(sample, Verdict.diff(if (havePrevious) previous else null, sample))
            previous = sample
            havePrevious = true
            updateNotification(Verdict.line(sample))
            // 睡眠也按当前间隔走：用户中途改间隔时不必重启服务
            delay(interval)
        }
        stopSelf()
    }

    private fun notification(text: String): Notification {
        val manager = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(CHANNEL, "CarLife 监测", NotificationManager.IMPORTANCE_LOW).apply {
                    description = "持续采样 CarLife 的运行状态"
                    setShowBadge(false)
                },
            )
        }
        val open = PendingIntent.getActivity(
            this, 0, Intent(this, MonitorActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
        return Notification.Builder(this, CHANNEL)
            .setContentTitle("CarLife 监测中")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setContentIntent(open)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification(text: String) {
        getSystemService(NotificationManager::class.java)
            .notify(NOTIFICATION_ID, notification(text.take(120)))
    }

    override fun onDestroy() {
        MonitorState.running = false
        scope.cancel()
        super.onDestroy()
    }

    companion object {
        private const val CHANNEL = "carlife-monitor"
        private const val NOTIFICATION_ID = 4101
        const val ACTION_STOP = "com.carlink.monitor.STOP"

        fun start(context: Context) {
            MonitorState.running = true
            val intent = Intent(context, MonitorService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, MonitorService::class.java))
        }
    }
}

/**
 * 采样结果的内存窗口。
 *
 * 只留最近 [MAX_SAMPLES] 轮：这个工具要连续跑几十分钟，把每一轮都留着既
 * 没有用也会在长时间运行后把内存吃光。判读需要的是「断连前后那几轮」，不是
 * 全部历史。
 */
object MonitorState {
    const val MAX_SAMPLES = 1200

    @Volatile var running: Boolean = false
    @Volatile var intervalMillis: Long = 2_000L

    private val samples = ArrayDeque<Sample>()
    private val events = ArrayDeque<Verdict.Event>()

    fun push(sample: Sample, newEvents: List<Verdict.Event>) = synchronized(this) {
        samples.addLast(sample)
        while (samples.size > MAX_SAMPLES) samples.removeFirst()
        events.addAll(newEvents)
        while (events.size > MAX_SAMPLES) events.removeFirst()
        listeners.forEach { it() }
    }

    fun snapshot(): Pair<List<Sample>, List<Verdict.Event>> = synchronized(this) {
        samples.toList() to events.toList()
    }

    fun clear() = synchronized(this) {
        samples.clear(); events.clear()
        listeners.forEach { it() }
    }

    private val listeners = mutableListOf<() -> Unit>()
    fun observe(listener: () -> Unit) { synchronized(this) { listeners += listener } }
    fun forget(listener: () -> Unit) { synchronized(this) { listeners -= listener } }

    /** 导出成纯文本，格式便于贴到对话里或存文件。 */
    fun export(): String = synchronized(this) {
        buildString {
            appendLine("# CarLife 监测记录  样本=${samples.size}  事件=${events.size}")
            appendLine("# 采样间隔=${intervalMillis}ms")
            appendLine()
            samples.forEach { appendLine(Verdict.line(it)) }
            appendLine()
            appendLine("# ---- 事件 ----")
            events.forEach { e ->
                appendLine("[${e.severity}] ${e.atMillis} ${e.text}")
            }
        }
    }
}
