package com.carlink.cast

import android.content.Context
import android.util.AtomicFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * The connection ladder, in the order the head unit actually walks it.
 *
 * Titles and details are written for the person holding the phone, not for
 * someone reading a protocol trace: they say what is happening and, where it
 * matters, what the driver can do about it. The message ids and protocol names
 * stay in the exported log.
 */
enum class CastStage(val title: String, val detail: String, val step: Int) {
    BLUETOOTH("连接车机蓝牙", "正在和车机建立蓝牙连接", 1),
    IDENTIFY("识别车机", "正在确认车机型号", 2),
    AUTHENTICATE("验证车机", "正在确认车机身份，防止连到假冒设备", 3),
    NETWORK("连接车机网络", "正在接入车机自己的 Wi-Fi", 4),
    DISCOVER("查找投屏服务", "正在车机网络上寻找投屏入口", 5),
    PAIR("建立安全连接", "正在和车机建立加密连接", 6),
    MEDIA("准备画面", "正在按车机屏幕尺寸准备画面", 7),
    STREAMING("正在投屏", "画面已经投到车机", 8),
    STOPPED("已停止", "投屏已结束", 0),
    FAILED("连接失败", "请点「开始投屏」重试，或查看导出日志", 0);

    companion object {
        val ladder: List<CastStage> = listOf(BLUETOOTH, IDENTIFY, AUTHENTICATE, NETWORK, DISCOVER, PAIR, MEDIA, STREAMING)
    }
}

/**
 * Which part of the system a log line came from.
 *
 * The exported journal is a single time-ordered stream — one timeline is what
 * lets an audio stall be read against the iAP2 frame that preceded it — but
 * every line carries exactly one channel tag so the same file can be filtered
 * down to one subsystem without losing its timestamps.
 *
 * Tags are uppercase and fixed-width enough to line up in a plain text viewer,
 * and they are deliberately coarse: a tag that appears six times is a tag that
 * does not help anyone find anything.
 */
enum class LogChannel(val tag: String) {
    /** Bluetooth iAP2: RFCOMM link, link-layer frames, identification, authentication. */
    BT("BT"),
    /** Wi-Fi iAP2 command channel: link sync, CSM frames, 0x4300/0x4301. */
    IAP("IAP"),
    /** RTSP: SETUP, RECORD, TEARDOWN, command channel, keepalives. */
    RTSP("RTSP"),
    /** AirPlay session state: resources, modes, ownership, handoff. */
    AIRPLAY("AIRPLAY"),
    /** Video: encoder, frames, CONFIG/IDR, bitrate, drops. */
    VIDEO("VIDEO"),
    /** Audio: negotiation, SETUP, RTP packets, the car's sampleTime. */
    AUDIO("AUDIO"),
    /** CarLife: process, ports, encoder negotiation, its own audio/media ports. */
    CARLIFE("CARLIFE"),
    /** Touch and hardware keys in both directions. */
    INPUT("INPUT"),
    /** Now-playing metadata (0x5000/0x5001, cover art). */
    MEDIA("MEDIA"),
    /** Wi-Fi association, addressing, routing. */
    NETWORK("NETWORK"),
    /** Session lifecycle: start, stages, teardown, recovery. */
    SESSION("SESSION"),
    /** Periodic counters: fps, bitrate, packet rates, buffer depth. */
    PERF("PERF"),
    /** Everything that does not fit above, and lines predating channels. */
    APP("APP"),
}

data class CastEvent(
    val elapsedMillis: Long,
    val level: String,
    val message: String,
    /** Subsystem tag; [LogChannel.APP] for lines that do not name one. */
    val channel: String = LogChannel.APP.tag,
    /** Thread that produced the line, so interleaved workers stay readable. */
    val thread: String = "",
)

data class CastMetric(val elapsedMillis: Long, val fps: Double, val kbps: Double)

data class CastState(
    val sessionId: String = "",
    val stage: CastStage = CastStage.STOPPED,
    val detail: String = "在车机上选择本机，然后开始投屏",
    val running: Boolean = false,
    val events: List<CastEvent> = emptyList(),
    val metrics: List<CastMetric> = emptyList(),
    val frames: Long = 0,
    val bytes: Long = 0,
    val dropped: Long = 0,
    val audioPackets: Long = 0,
    val sourceAudioBytes: Long = 0,
    val sourceAudioUnits: Long = 0,
    val sourceAudioPcmNonZeroBytes: Long = 0,
    val sourceAudioPeak: Int = 0,
    val inputEvents: Long = 0,
    val source: String = "",
    val display: String = "",
)

data class CastSessionRecord(
    val sessionId: String,
    val startedAtMillis: Long,
    val stage: String,
    val source: String,
    val frames: Long,
    val hasCrash: Boolean,
    val legacy: Boolean,
)

/**
 * A bounded in-memory view backed by a durable per-session event journal.
 *
 * The full journal survives process death. A small atomic snapshot lets the
 * app restore the latest session and export it after a crash or system kill.
 */
object CastLog {
    private const val JOURNAL_LIMIT_BYTES = 2L * 1024 * 1024
    private const val JOURNAL_MAX_SEGMENTS = 8
    private const val CHECKPOINT_INTERVAL_MS = 5_000L
    private const val SYNC_INTERVAL_MS = 3_000L
    private const val RETAIN_SESSIONS = 20

    private val mutable = MutableStateFlow(CastState())
    val state = mutable.asStateFlow()

    @Volatile private var directory: File? = null
    /** Application context, kept only to read the device facts at session start. */
    @Volatile private var appContext: Context? = null
    @Volatile private var journal: File? = null
    @Volatile private var activeSessionId = ""
    private var initialized = false
    private var startedAt = 0L
    private var lastCheckpointAt = 0L
    private var lastDurableSyncAt = 0L
    private var journalTruncated = false
    private var sampleAt = 0L
    private var sampleFrames = 0L
    private var sampleBytes = 0L
    private var sourceAudioBytes = 0L
    private var sourceAudioFrames = 0L
    private var sourceAudioPeak = 0
    private var sourceAudioUnits = 0L
    private var sourceAudioPcmNonZeroBytes = 0L

    @Synchronized fun initialize(context: Context) {
        if (initialized) return
        appContext = context.applicationContext
        directory = File(context.filesDir, "sessions").apply { mkdirs() }
        initialized = true
        restoreLatestSession()
    }

    @Synchronized fun begin(source: String): String {
        val id = UUID.randomUUID().toString()
        startedAt = System.currentTimeMillis()
        sampleAt = startedAt
        lastCheckpointAt = 0
        lastDurableSyncAt = 0
        sampleFrames = 0
        sampleBytes = 0
        sourceAudioBytes = 0
        sourceAudioFrames = 0
        sourceAudioPeak = 0
        sourceAudioUnits = 0
        sourceAudioPcmNonZeroBytes = 0
        journalTruncated = false
        activeSessionId = id
        journal = directory?.let { File(it, "$id.jsonl") }
        mutable.value = CastState(
            sessionId = id,
            running = true,
            source = source,
            stage = CastStage.BLUETOOTH,
            detail = CastStage.BLUETOOTH.detail,
        )
        event(LogChannel.SESSION, "INFO",
            "会话开始，画面来源：$source，版本=${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})，构建=${BuildConfig.BUILD_ID}")
        /*
         * The environment block.
         *
         * Everything here is a fact that a reader otherwise has to ask for
         * later: which build of the phone's software is running, what the
         * device says it is, and which subsystems are even available. A report
         * of "no sound" means something different on a phone with no
         * notification access than on one that has it.
         */
        deviceFacts().forEach { (channel, line) -> event(channel, "INFO", line) }
        persistSnapshot()
        pruneOldSessions()
        return id
    }

    /** Device and build facts, emitted once at the head of every session. */
    private fun deviceFacts(): List<Pair<LogChannel, String>> {
        val app = appContext ?: return emptyList()
        val facts = mutableListOf<Pair<LogChannel, String>>()
        runCatching {
            val info = app.packageManager.getPackageInfo(app.packageName, 0)
            facts += LogChannel.SESSION to
                "应用：${info.packageName} 版本=${info.versionName} 构建时间=${info.lastUpdateTime}"
        }
        runCatching {
            val pm = app.packageManager
            val enabled = pm.getApplicationInfo("com.baidu.carlife.honor", 0).enabled
            facts += LogChannel.CARLIFE to "CarLife 已安装（enabled=$enabled）"
        }.onFailure {
            facts += LogChannel.CARLIFE to "CarLife 未安装或不可见：${it.javaClass.simpleName}"
        }
        runCatching {
            val notificationAccess = android.provider.Settings.Secure.getString(
                app.contentResolver,
                "enabled_notification_listeners",
            ).orEmpty().contains(app.packageName)
            facts += LogChannel.MEDIA to "通知使用权=$notificationAccess（影响 NowPlaying 与媒体卡）"
        }
        runCatching {
            val adapter = app.getSystemService(android.bluetooth.BluetoothManager::class.java)?.adapter
            facts += LogChannel.BT to
                "蓝牙开关=${adapter?.isEnabled} 已配对设备=${adapter?.bondedDevices?.size ?: -1}"
        }
        runCatching {
            val wifi = app.getSystemService(android.net.wifi.WifiManager::class.java)
            facts += LogChannel.NETWORK to
                "Wi-Fi 开关=${wifi?.isWifiEnabled} 锁=${wifi?.createWifiLock(android.net.wifi.WifiManager.WIFI_MODE_FULL_HIGH_PERF, "carlink-probe") != null}"
        }
        runCatching {
            facts += LogChannel.SESSION to
                "设备：${android.os.Build.MANUFACTURER}/${android.os.Build.MODEL} " +
                "Android=${android.os.Build.VERSION.RELEASE}(${android.os.Build.VERSION.SDK_INT})"
        }
        facts += LogChannel.SESSION to
            "时间：${java.time.LocalDateTime.now()} 时区=${java.util.TimeZone.getDefault().id}"
        return facts
    }

    @Synchronized fun stage(stage: CastStage, detail: String = stage.detail) {
        val current = mutable.value
        if (current.stage == stage && current.detail == detail) return
        mutable.value = current.copy(
            stage = stage,
            detail = detail,
            running = stage !in setOf(CastStage.STOPPED, CastStage.FAILED),
        )
        event(if (stage == CastStage.FAILED) "ERROR" else "INFO", detail)
        persistSnapshot()
    }

    @Synchronized fun display(width: Int, height: Int, fps: Int) {
        val value = "${width}×$height · ${fps}fps"
        if (mutable.value.display == value) return
        mutable.value = mutable.value.copy(display = value)
        event("INFO", "协商画布：$value")
        persistSnapshot()
    }


    /**
     * How many events the in-memory snapshot keeps.
     *
     * The disk journal is append-only and unbounded per session; this only
     * bounds what the UI and the exported JSON hold, so a whole session fits.
     */
    private const val MAX_EVENTS_IN_MEMORY = 3000

    @Synchronized fun event(level: String, message: String) =
        event(LogChannel.APP, level, message)

    /**
     * Records one line against a subsystem.
     *
     * The channel is stored, not just prefixed: the exported journal keeps it
     * as its own JSON field so `jq 'select(.channel=="AUDIO")'` works, and the
     * rendered text keeps the same tag at the head of the line so a plain text
     * reader can do the same by eye.
     */
    @Synchronized fun event(channel: LogChannel, level: String, message: String) {
        val now = System.currentTimeMillis()
        val event = CastEvent(
            elapsedMillis = (now - startedAt).coerceAtLeast(0),
            level = level,
            message = message.take(4000),
            channel = channel.tag,
            thread = Thread.currentThread().name,
        )
        val current = mutable.value
        /*
         * Keep far more history than one screenful.
         *
         * 300 events was not enough to hold a whole session: the handshake
         * lines, the first key frame and the teardown never coexisted in the
         * same file, which is why so many earlier diagnoses had to infer the
         * opening from the closing. A field session produces roughly 400-900
         * events, so 3000 holds one whole run with room to spare, and the
         * journal on disk (appendJournal below) already has no such limit.
         */
        val merged = current.events + event
        mutable.value = current.copy(
            events = if (merged.size > MAX_EVENTS_IN_MEMORY) merged.takeLast(MAX_EVENTS_IN_MEMORY) else merged,
        )
        appendJournal(event, forceSync = level == "ERROR" || level == "FATAL" || level == "WARN")
        if (level == "ERROR" || level == "FATAL" || now - lastCheckpointAt >= CHECKPOINT_INTERVAL_MS ||
            !mutable.value.running
        ) {
            persistSnapshot()
        }
    }

    /**
     * A logger bound to one channel.
     *
     * Modules take one of these instead of a bare `(String) -> Unit` so their
     * lines land in the right channel without every call site repeating the
     * tag — and so a module that gains a channel later only changes the point
     * where its logger is created.
     */
    fun logger(channel: LogChannel, level: String = "INFO"): (String) -> Unit =
        { message -> event(channel, level, message) }

    @Synchronized fun video(bytes: Int, dropped: Boolean = false) {        val current = mutable.value
        mutable.value = if (dropped) current.copy(dropped = current.dropped + 1)
        else current.copy(frames = current.frames + 1, bytes = current.bytes + bytes)
        val now = System.currentTimeMillis()
        if (now - sampleAt >= 1000) {
            val seconds = (now - sampleAt) / 1000.0
            val latest = mutable.value
            val sample = CastMetric(
                (now - startedAt).coerceAtLeast(0),
                (latest.frames - sampleFrames) / seconds,
                (latest.bytes - sampleBytes) * 8 / seconds / 1000,
            )
            mutable.value = latest.copy(metrics = (latest.metrics + sample).takeLast(90))
            sampleAt = now
            sampleFrames = latest.frames
            sampleBytes = latest.bytes
        }
        checkpointIfDue(now)
    }

    @Synchronized fun audio() {
        val packets = mutable.value.audioPackets + 1
        mutable.value = mutable.value.copy(audioPackets = packets)
        if (packets == 1L || packets % 200L == 0L) {
            event("INFO", "AirPlay RTP 音频已发 $packets 包")
        }
    }

    @Synchronized fun sourceAudio(bytes: ByteArray, pcm: Boolean) {
        sourceAudioBytes += bytes.size
        if (!pcm) sourceAudioUnits++
        if (pcm) {
            var index = 0
            while (index + 1 < bytes.size) {
                // CarLife PCM is signed little-endian. Reading it as unsigned
                // turned every negative half-cycle into 32768 and made even
                // ordinary audio look like a saturated, broken signal.
                val raw = (bytes[index].toInt() and 0xff) or (bytes[index + 1].toInt() shl 8)
                val sample = kotlin.math.abs(raw.toShort().toInt())
                if (sample > sourceAudioPeak) sourceAudioPeak = sample
                if (bytes[index].toInt() != 0 || bytes[index + 1].toInt() != 0) {
                    sourceAudioPcmNonZeroBytes += 2
                }
                index += 2
            }
            sourceAudioFrames += bytes.size / 2
        }
        mutable.value = mutable.value.copy(
            sourceAudioBytes = sourceAudioBytes,
            sourceAudioUnits = sourceAudioUnits,
            sourceAudioPcmNonZeroBytes = sourceAudioPcmNonZeroBytes,
            sourceAudioPeak = sourceAudioPeak,
        )
        if (sourceAudioBytes == bytes.size.toLong() ||
            sourceAudioBytes / 16384 != (sourceAudioBytes - bytes.size) / 16384
        ) {
            event(
                "INFO",
                if (pcm) {
                    "来源音频：${sourceAudioBytes}B，${sourceAudioFrames}采样，" +
                        "非零=${sourceAudioPcmNonZeroBytes}B，峰值=$sourceAudioPeak，编码输入=PCM"
                } else {
                    "来源音频：${sourceAudioBytes}B，${sourceAudioUnits}个AAC access unit，编码输入=AAC"
                },
            )
        } else {
            checkpointIfDue(System.currentTimeMillis())
        }
    }

    @Synchronized fun input() {
        mutable.value = mutable.value.copy(inputEvents = mutable.value.inputEvents + 1)
        checkpointIfDue(System.currentTimeMillis())
    }

    @Synchronized fun stop(failure: String? = null) {
        val current = mutable.value
        if (current.sessionId.isBlank()) return
        mutable.value = if (failure != null) {
            current.copy(stage = CastStage.FAILED, detail = failure, running = false)
        } else {
            current.copy(stage = CastStage.STOPPED, detail = CastStage.STOPPED.detail, running = false)
        }
        event(if (failure != null) "ERROR" else "INFO", failure ?: "会话结束")
        persistSnapshot()
    }

    /** Records a Java/Kotlin crash before Android's normal handler terminates the process. */
    fun recordCrash(thread: Thread, failure: Throwable) {
        val now = System.currentTimeMillis()
        val id = activeSessionId.ifBlank { "crash-$now" }
        val dir = directory ?: return
        val crashFile = File(dir, "$id.crash.txt")
        val summary = "未捕获异常：${failure.javaClass.name}: ${failure.message ?: "(无消息)"}，线程=${thread.name}"
        runCatching {
            val text = StringWriter().also { writer ->
                PrintWriter(writer).use { out ->
                    out.println("timestamp=$now")
                    out.println("thread=${thread.name}")
                    failure.printStackTrace(out)
                }
            }.toString()
            FileOutputStream(crashFile, false).use { output ->
                output.write(text.toByteArray(Charsets.UTF_8))
                output.fd.sync()
            }
        }
        if (activeSessionId.isBlank()) {
            runCatching {
                val event = CastEvent(0, "FATAL", summary)
                FileOutputStream(File(dir, "$id.jsonl"), false).use { output ->
                    output.write((json(event).toString() + "\n").toByteArray(Charsets.UTF_8))
                    output.fd.sync()
                }
                val state = CastState(
                    sessionId = id,
                    stage = CastStage.FAILED,
                    detail = summary,
                    events = listOf(event),
                )
                val atomic = AtomicFile(File(dir, "$id.snapshot.json"))
                val output = atomic.startWrite()
                try {
                    output.write(snapshotJson(state, now, now, truncated = false).toString().toByteArray(Charsets.UTF_8))
                    output.fd.sync()
                    atomic.finishWrite(output)
                } catch (writeFailure: Throwable) {
                    atomic.failWrite(output)
                    throw writeFailure
                }
            }
        }
        runCatching {
            if (activeSessionId.isNotBlank()) event("FATAL", summary)
            persistSnapshot()
        }
    }

    /** Stores Android's post-restart process-exit record and any ANR/crash trace it retained. */
    @Synchronized fun recordProcessExit(details: JSONObject, trace: ByteArray?) {
        val dir = directory ?: return
        val timestamp = details.optLong("timestamp", System.currentTimeMillis())
        val hasSession = mutable.value.sessionId.isNotBlank()
        val id = mutable.value.sessionId.ifBlank { "process-$timestamp" }
        val base = "$id.exit-$timestamp"
        runCatching {
            FileOutputStream(File(dir, "$base.json"), false).use { output ->
                output.write(details.toString().toByteArray(Charsets.UTF_8))
                output.fd.sync()
            }
        }
        if (trace != null && trace.isNotEmpty()) {
            runCatching {
                FileOutputStream(File(dir, "$base.trace"), false).use { output ->
                    output.write(trace)
                    output.fd.sync()
                }
            }
        }
        val message = "Android 记录到上次进程退出：${details.optString("reasonLabel", details.optString("reason"))}" +
            details.optString("description").takeIf { it.isNotBlank() }?.let { "；$it" }.orEmpty()
        if (hasSession) {
            event("ERROR", message)
        } else {
            val diagnosticEvent = CastEvent(0, "ERROR", message)
            activeSessionId = id
            startedAt = timestamp
            journal = File(dir, "$id.jsonl")
            mutable.value = CastState(
                sessionId = id,
                stage = CastStage.FAILED,
                detail = message,
                events = listOf(diagnosticEvent),
            )
            appendJournal(diagnosticEvent, forceSync = true)
        }
        persistSnapshot()
    }

    fun sessions(): List<CastSessionRecord> {
        val dir = directory ?: return emptyList()
        val ids = dir.listFiles().orEmpty().mapNotNull { file ->
            when {
                file.name.endsWith(".snapshot.json") -> file.name.removeSuffix(".snapshot.json")
                file.name.contains(".part-") && file.name.endsWith(".jsonl") ->
                    file.name.substringBefore(".part-")
                file.name.endsWith(".jsonl") -> file.name.removeSuffix(".jsonl")
                else -> null
            }
        }.distinct()
        return ids.mapNotNull { id ->
            val snapshot = readSnapshot(id)
            val journalFile = File(dir, "$id.jsonl")
            if (snapshot == null && journalFiles(id, dir).isEmpty()) return@mapNotNull null
            CastSessionRecord(
                sessionId = id,
                startedAtMillis = snapshot?.optLong("startedAtMillis") ?: journalFile.lastModified(),
                stage = snapshot?.optString("stage") ?: "旧版日志",
                source = snapshot?.optString("source").orEmpty(),
                frames = snapshot?.optLong("frames") ?: 0L,
                hasCrash = File(dir, "$id.crash.txt").isFile ||
                    dir.listFiles()?.any { it.name.startsWith("$id.exit-") } == true,
                legacy = snapshot == null,
            )
        }.sortedByDescending { it.startedAtMillis }
    }

    /** Creates a ZIP for the selected session, including recovered and crash evidence. */
    @Synchronized fun export(sessionId: String = mutable.value.sessionId): File? {
        val dir = directory ?: return null
        if (sessionId.isBlank()) return null
        if (sessionId == mutable.value.sessionId) persistSnapshot()
        val snapshot = readSnapshot(sessionId) ?: legacySummary(sessionId, dir) ?: return null
        val startedAtMillis = snapshot.optLong("startedAtMillis", 0L)
        val target = File(dir, "$sessionId.zip")
        ZipOutputStream(FileOutputStream(target, false)).use { zip ->
            zip.putNextEntry(ZipEntry("session.json"))
            zip.write(snapshot.toString().toByteArray(Charsets.UTF_8))
            zip.closeEntry()
            File(dir, "$sessionId.jsonl").takeIf { it.isFile }?.let { file ->
                zip.putNextEntry(ZipEntry("events.jsonl"))
                file.inputStream().use { it.copyTo(zip) }
                zip.closeEntry()
            }
            val parts = dir.listFiles()?.filter {
                it.isFile && it.name.startsWith("$sessionId.part-") && it.name.endsWith(".jsonl")
            }?.sortedBy { it.name }.orEmpty()
            parts.forEachIndexed { index, file ->
                zip.putNextEntry(ZipEntry("events/part-${index + 1}.jsonl"))
                file.inputStream().use { it.copyTo(zip) }
                zip.closeEntry()
            }
            /*
             * A readable timeline and one file per channel.
             *
             * The JSONL above is the machine format, but reading a session by
             * hand out of JSONL means scrolling through escaped strings. These
             * two extra views are generated from the same lines:
             *
             *   timeline.txt  — every channel, merged, in 对照实现's shape
             *                   (`[time] [session] [thread] [+ms] TAG: text`)
             *   channel/X.log — the same lines for one subsystem only
             *
             * Both are rebuilt at export time, so a session recorded before
             * channels existed simply has everything under APP.
             */
            val journalFiles = listOfNotNull(File(dir, "$sessionId.jsonl").takeIf { it.isFile }) + parts
            val events = journalFiles.flatMap { readJournalEvents(it) }
            if (events.isNotEmpty()) {
                zip.putNextEntry(ZipEntry("timeline.txt"))
                zip.write(renderTimeline(events, sessionId, startedAtMillis).toByteArray(Charsets.UTF_8))
                zip.closeEntry()
                events.groupBy { it.channel }.toSortedMap().forEach { (channel, lines) ->
                    zip.putNextEntry(ZipEntry("channel/${channel.lowercase()}.log"))
                    zip.write(renderTimeline(lines, sessionId, startedAtMillis).toByteArray(Charsets.UTF_8))
                    zip.closeEntry()
                }
            }
            File(dir, "$sessionId.crash.txt").takeIf { it.isFile }?.let { file ->
                zip.putNextEntry(ZipEntry("crash.txt"))
                file.inputStream().use { it.copyTo(zip) }
                zip.closeEntry()
            }
            dir.listFiles()?.filter {
                it.isFile && it.name.startsWith("$sessionId.exit-") &&
                    (it.name.endsWith(".json") || it.name.endsWith(".trace"))
            }?.sortedBy { it.name }?.forEach { file ->
                val suffix = file.name.removePrefix("$sessionId.")
                zip.putNextEntry(ZipEntry("process-exit/$suffix"))
                file.inputStream().use { it.copyTo(zip) }
                zip.closeEntry()
            }
        }
        return target
    }

    /** Reads one journal shard back into events; malformed lines are skipped. */
    private fun readJournalEvents(file: File): List<CastEvent> = runCatching {
        file.useLines { lines ->
            lines.mapNotNull { line ->
                runCatching { JSONObject(line) }.getOrNull()?.let {
                    CastEvent(
                        elapsedMillis = it.optLong("elapsedMs"),
                        level = it.optString("level", "INFO"),
                        message = it.optString("message"),
                        channel = it.optString("channel", LogChannel.APP.tag),
                        thread = it.optString("thread"),
                    )
                }
            }.toList()
        }
    }.getOrDefault(emptyList())

    /**
     * Renders events the way they are read in a text editor.
     *
     * Shape and column order follow the reference sender so two captures can be
     * diffed by eye without reformatting either:
     *
     *     [10:42:07.376] [5a22d0fa/B] [airplay-audio] [+1234ms] AUDIO: 音频首包 …
     *
     * The tag is padded so messages start at the same column for the whole
     * file; the level is only shown when it is not INFO, keeping the common
     * case free of noise while errors stay greppable.
     */
    private fun renderTimeline(events: List<CastEvent>, sessionId: String, startedAtMillis: Long): String {
        val shortSession = sessionId.take(8)
        return buildString {
            events.forEach { event ->
                append('[').append(clock(event.elapsedMillis, startedAtMillis)).append("] ")
                append('[').append(shortSession).append("/B] ")
                append('[').append(event.thread.ifBlank { "-" }).append("] ")
                append("[+").append(event.elapsedMillis).append("ms] ")
                if (event.level != "INFO") {
                    append(event.level).append(' ')
                }
                append(event.channel.padEnd(CHANNEL_TAG_WIDTH)).append(": ")
                append(event.message)
                append('\n')
            }
        }
    }

    private fun clock(elapsedMillis: Long, startedAtMillis: Long): String {
        val at = java.time.Instant.ofEpochMilli((startedAtMillis + elapsedMillis).coerceAtLeast(0L))
            .atZone(java.time.ZoneId.systemDefault())
        return String.format(
            java.util.Locale.US,
            "%02d:%02d:%02d.%03d",
            at.hour, at.minute, at.second, at.nano / 1_000_000,
        )
    }

    @Synchronized internal fun snapshotJson(
        state: CastState,
        startedAtMillis: Long,
        savedAtMillis: Long,
        truncated: Boolean,
    ): JSONObject = JSONObject()
        .put("schemaVersion", 2)
        .put("app", "CarLink Cast")
        .put("versionName", BuildConfig.VERSION_NAME)
        .put("versionCode", BuildConfig.VERSION_CODE)
        .put("buildId", BuildConfig.BUILD_ID)
        .put("sessionId", state.sessionId)
        .put("startedAtMillis", startedAtMillis)
        .put("savedAtMillis", savedAtMillis)
        .put("stage", state.stage.name)
        .put("detail", state.detail)
        .put("running", state.running)
        .put("journalTruncated", truncated)
        .put("source", state.source)
        .put("display", state.display)
        .put("frames", state.frames)
        .put("bytes", state.bytes)
        .put("dropped", state.dropped)
        .put("audioPackets", state.audioPackets)
        .put("sourceAudioBytes", state.sourceAudioBytes)
        .put("sourceAudioUnits", state.sourceAudioUnits)
        .put("sourceAudioPcmNonZeroBytes", state.sourceAudioPcmNonZeroBytes)
        .put("sourceAudioPeak", state.sourceAudioPeak)
        .put("inputEvents", state.inputEvents)
        .put("events", JSONArray().apply { state.events.forEach { put(json(it)) } })
        .put("metrics", JSONArray().apply {
            state.metrics.forEach {
                put(JSONObject().put("elapsedMs", it.elapsedMillis).put("fps", it.fps).put("kbps", it.kbps))
            }
        })

    @Synchronized internal fun stateFromSnapshot(snapshot: JSONObject): CastState {
        val events = snapshot.optJSONArray("events") ?: JSONArray()
        val metrics = snapshot.optJSONArray("metrics") ?: JSONArray()
        return CastState(
            sessionId = snapshot.optString("sessionId"),
            stage = runCatching { CastStage.valueOf(snapshot.optString("stage")) }.getOrDefault(CastStage.STOPPED),
            detail = snapshot.optString("detail", CastStage.STOPPED.detail),
            running = snapshot.optBoolean("running"),
            events = (0 until events.length()).mapNotNull { index ->
                events.optJSONObject(index)?.let {
                    CastEvent(
                        elapsedMillis = it.optLong("elapsedMs"),
                        level = it.optString("level"),
                        message = it.optString("message"),
                        // Sessions recorded before channels existed have
                        // neither field; they read back as APP with no thread
                        // rather than failing to load.
                        channel = it.optString("channel", LogChannel.APP.tag),
                        thread = it.optString("thread"),
                    )
                }
            }.takeLast(3000),
            metrics = (0 until metrics.length()).mapNotNull { index ->
                metrics.optJSONObject(index)?.let {
                    CastMetric(it.optLong("elapsedMs"), it.optDouble("fps"), it.optDouble("kbps"))
                }
            }.takeLast(90),
            frames = snapshot.optLong("frames"),
            bytes = snapshot.optLong("bytes"),
            dropped = snapshot.optLong("dropped"),
            audioPackets = snapshot.optLong("audioPackets"),
            sourceAudioBytes = snapshot.optLong("sourceAudioBytes"),
            sourceAudioUnits = snapshot.optLong("sourceAudioUnits"),
            sourceAudioPcmNonZeroBytes = snapshot.optLong("sourceAudioPcmNonZeroBytes"),
            sourceAudioPeak = snapshot.optInt("sourceAudioPeak"),
            inputEvents = snapshot.optLong("inputEvents"),
            source = snapshot.optString("source"),
            display = snapshot.optString("display"),
        )
    }

    private fun restoreLatestSession() {
        val latest = sessions().firstOrNull() ?: return
        val id = latest.sessionId
        val snapshot = readSnapshot(id)
        val loaded = snapshot?.let(::stateFromSnapshot) ?: restoreLegacySession(id) ?: return
        activeSessionId = id
        journal = File(directory, "$id.jsonl").takeIf { it.isFile }
        startedAt = snapshot?.optLong("startedAtMillis")?.takeIf { it > 0 }
            ?: (System.currentTimeMillis() - (loaded.events.lastOrNull()?.elapsedMillis ?: 0L))
        val recovered = if (loaded.running) {
            loaded.copy(
                stage = CastStage.FAILED,
                detail = "上次连接未正常结束；日志已恢复，可导出诊断",
                running = false,
            )
        } else loaded
        mutable.value = recovered
        sampleAt = System.currentTimeMillis()
        sampleFrames = recovered.frames
        sampleBytes = recovered.bytes
        sourceAudioBytes = recovered.sourceAudioBytes
        sourceAudioUnits = recovered.sourceAudioUnits
        sourceAudioPeak = recovered.sourceAudioPeak
        sourceAudioPcmNonZeroBytes = recovered.sourceAudioPcmNonZeroBytes
        journalTruncated = snapshot?.optBoolean("journalTruncated") ?: false
        if (loaded.running) {
            event("ERROR", "检测到上一次会话进程中断；可能是崩溃、系统回收或强制停止，具体原因随后从 Android 退出记录补充")
            persistSnapshot()
        }
    }

    private fun restoreLegacySession(id: String): CastState? {
        val dir = directory ?: return null
        val files = journalFiles(id, dir)
        if (files.isEmpty()) return null
        val events = runCatching {
            val restored = mutableListOf<CastEvent>()
            files.forEach { file ->
                file.forEachLine { line ->
                    runCatching {
                        val value = JSONObject(line)
                        restored += CastEvent(
                            value.optLong("elapsedMs"),
                            value.optString("level"),
                            value.optString("message"),
                        )
                    }
                }
            }
            restored.takeLast(300)
        }.getOrDefault(emptyList<CastEvent>())
        val restored = CastState(
            sessionId = id,
            stage = CastStage.FAILED,
            detail = "旧版本会话日志已恢复，可导出",
            events = events,
        )
        activeSessionId = id
        journal = File(dir, "$id.jsonl")
        startedAt = System.currentTimeMillis() - (events.lastOrNull()?.elapsedMillis ?: 0L)
        mutable.value = restored
        event("WARN", "从旧版本 JSONL 恢复日志；旧版未保存视频、音频等会话快照")
        persistSnapshot()
        return mutable.value
    }

    private fun appendJournal(event: CastEvent, forceSync: Boolean) {
        val file = journal ?: return
        val bytes = (json(event).toString() + "\n").toByteArray(Charsets.UTF_8)
        val currentLength = file.length()
        if (currentLength + bytes.size > JOURNAL_LIMIT_BYTES) {
            rotateJournal(file)
        }
        writeJournalBytes(file, bytes, forceSync)
    }

    private fun rotateJournal(file: File) {
        val id = activeSessionId
        val dir = file.parentFile ?: return
        val existing = dir.listFiles()?.filter {
            it.isFile && it.name.startsWith("$id.part-") && it.name.endsWith(".jsonl")
        }?.sortedBy { it.name }.orEmpty()
        if (existing.size >= JOURNAL_MAX_SEGMENTS) {
            existing.first().delete()
        }
        val suffix = System.currentTimeMillis()
        val rotated = File(dir, "$id.part-$suffix.jsonl")
        runCatching {
            if (file.isFile && file.length() > 0) file.renameTo(rotated)
            file.createNewFile()
            journalTruncated = false
            val notice = json(CastEvent(
                (System.currentTimeMillis() - startedAt).coerceAtLeast(0),
                "INFO",
                "详细日志已滚动分片保存，旧分片仍会随会话导出",
            )).toString() + "\n"
            writeJournalBytes(file, notice.toByteArray(Charsets.UTF_8), forceSync = true)
        }.onFailure {
            journalTruncated = true
        }
    }

    private fun journalFiles(id: String, dir: File): List<File> =
        dir.listFiles()?.filter {
            it.isFile && (
                it.name == "$id.jsonl" ||
                    (it.name.startsWith("$id.part-") && it.name.endsWith(".jsonl"))
                )
        }?.sortedBy { it.name }.orEmpty()

    private fun writeJournalBytes(file: File, bytes: ByteArray, forceSync: Boolean) {
        runCatching {
            file.parentFile?.mkdirs()
            FileOutputStream(file, true).use { output ->
                output.write(bytes)
                output.flush()
                val now = System.currentTimeMillis()
                if (forceSync || now - lastDurableSyncAt >= SYNC_INTERVAL_MS) {
                    output.fd.sync()
                    lastDurableSyncAt = now
                }
            }
        }
    }

    private fun checkpointIfDue(now: Long) {
        if (mutable.value.sessionId.isNotBlank() && now - lastCheckpointAt >= CHECKPOINT_INTERVAL_MS) {
            persistSnapshot()
        }
    }

    private fun persistSnapshot() {
        val id = mutable.value.sessionId
        val dir = directory ?: return
        if (id.isBlank()) return
        runCatching {
            val snapshot = snapshotJson(mutable.value, startedAt, System.currentTimeMillis(), journalTruncated)
            val atomic = AtomicFile(File(dir, "$id.snapshot.json"))
            val output = atomic.startWrite()
            try {
                output.write(snapshot.toString().toByteArray(Charsets.UTF_8))
                output.fd.sync()
                atomic.finishWrite(output)
                lastCheckpointAt = System.currentTimeMillis()
            } catch (failure: Throwable) {
                atomic.failWrite(output)
                throw failure
            }
        }
    }

    private fun readSnapshot(id: String): JSONObject? {
        val file = directory?.let { File(it, "$id.snapshot.json") }?.takeIf { it.isFile } ?: return null
        return runCatching { JSONObject(AtomicFile(file).openRead().bufferedReader().use { it.readText() }) }
            .getOrNull()
    }

    private fun legacySummary(id: String, dir: File): JSONObject? {
        val files = journalFiles(id, dir)
        val journalFile = files.lastOrNull() ?: return null
        val lastEvent = runCatching {
            files.asSequence().flatMap { file ->
                file.readLines().asSequence().mapNotNull { line ->
                    runCatching { JSONObject(line) }.getOrNull()
                }
            }.lastOrNull()
        }.getOrNull()
        return JSONObject()
            .put("schemaVersion", 1)
            .put("app", "CarLink Cast")
            .put("sessionId", id)
            .put("startedAtMillis", (journalFile.lastModified() - (lastEvent?.optLong("elapsedMs") ?: 0L)).coerceAtLeast(0))
            .put("savedAtMillis", journalFile.lastModified())
            .put("stage", "LEGACY")
            .put("detail", "旧版本日志没有会话状态快照")
            .put("running", false)
            .put("journalTruncated", journalFile.length() >= 2 * 1024 * 1024)
            .put("source", "")
            .put("display", "")
            .put("recoveredFromLegacyJournal", true)
            .put("events", JSONArray())
            .put("metrics", JSONArray())
    }

    private fun pruneOldSessions() {
        val dir = directory ?: return
        val oldIds = sessions().drop(RETAIN_SESSIONS).map { it.sessionId }.toSet()
        if (oldIds.isEmpty()) return
        dir.listFiles()?.filter { file -> oldIds.any { id -> file.name.startsWith("$id.") } }
            ?.forEach { it.delete() }
    }

    private fun json(event: CastEvent) = JSONObject()
        .put("elapsedMs", event.elapsedMillis)
        .put("level", event.level)
        .put("channel", event.channel)
        .put("thread", event.thread)
        .put("message", event.message)

    /** Column width for channel tags in the rendered timeline. */
    private const val CHANNEL_TAG_WIDTH = 8
}
