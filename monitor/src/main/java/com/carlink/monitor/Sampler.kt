package com.carlink.monitor

import java.io.File

/**
 * 一次采样的完整快照。
 *
 * 所有字段都是原始读数，判断逻辑放在 [Verdict] 里，这样采样和解读可以分开
 * 验证：采样错了看一眼就知道，判断错了能单独改。
 */
data class Sample(
    val atMillis: Long,
    val processes: List<ProcInfo>,
    val sockets: List<SocketInfo>,
    val encoderRefs: Int?,
    val carLifeLayers: Int?,
    val audioMode: String?,
    val fdCounts: Map<Int, Int?>,
    val errors: List<String> = emptyList(),
) {
    val hasRoot: Boolean get() = errors.none { it == Sampler.ERR_NO_ROOT }
}

data class ProcInfo(
    val pid: Int,
    val ppid: Int,
    val name: String,
    /** R/S/D/Z/T — Z is a zombie and the single most important value here. */
    val state: Char,
    val cpuMillis: Long,
    val rssKb: Long,
    val threads: Int,
) {
    val isDead: Boolean get() = state == 'Z' || state == 'X' || state == 'x'
}

data class SocketInfo(
    val port: Int,
    val state: String,
    val txQueue: Long,
    val rxQueue: Long,
)

/**
 * 采样器。只读，不写、不改、不停任何东西。
 *
 * 全部通过 `su -c` 跑，不依赖应用自身的权限：CarLife 是别的应用，读它的
 * /proc/<pid>/{stat,fd} 和 dumpsys 都要求 root。用 root 也意味着不碰任何
 * 应用数据目录，只读系统公开的运行时状态。
 */
class Sampler(private val runner: Shell = RootShell()) {

    fun sample(): Sample {
        val now = System.currentTimeMillis()
        val errors = mutableListOf<String>()

        if (runner.run("id -u")?.trim() != "0") {
            // 没有 root 也要出结果：进程列表在 Android 10+ 被限制成只能看
            // 自己的，但至少能报「没有 root」而不是静默返回空列表冒充
            // 「CarLife 没在跑」，那是两种完全不同的结论。
            return Sample(now, emptyList(), emptyList(), null, null, null, emptyMap(),
                listOf(ERR_NO_ROOT))
        }

        val processes = readProcesses(errors)
        val sockets = readSockets(processes, errors)
        val encoder = readCount(
            "dumpsys media.codec 2>/dev/null | grep -c 'c2.qti.avc.encoder'", errors,
        )
        val layers = readCount(
            "dumpsys SurfaceFlinger --list 2>/dev/null | grep -icE 'carlife|carlink|hihonor\\.auto'",
            errors,
        )
        val audio = runner.run("dumpsys audio 2>/dev/null | grep -m1 'mode:'")
            ?.trim()?.takeIf { it.isNotEmpty() }

        // fd 数量只对 CarLife 本体有意义，而且每多一个进程就多一次 su 往返。
        // 只统计名字里带 carlife/carlink 的，别把荣耀车联宿主的 fd 也算进来
        // 冒充泄漏——第一次运行时报出的「持续增长」大多是采样时点不同造成的。
        val fds = processes
            .filter { isCarLifeProcess(it.name) }
            .associate { it.pid to readCount("ls /proc/${it.pid}/fd 2>/dev/null | wc -l", errors) }

        return Sample(now, processes, sockets, encoder, layers, audio, fds, errors)
    }

    private fun isCarLifeProcess(name: String) =
        name.contains("carlife", ignoreCase = true) || name.contains("carlink", ignoreCase = true)

    private fun readProcesses(errors: MutableList<String>): List<ProcInfo> {
        // 一次 shell 把所有候选进程的 stat 读出来，而不是每个进程起一次 su。
        // su 的进程启动开销是几十毫秒，进程多了采样周期会被拉长，而不规律的
        // 采样正好会毁掉「CPU 时间增量」这个指标。
        //
        // 脚本里一律避开 ${...} 花括号展开：这段字符串要经过 Kotlin 模板和
        // shell 两层解析，花括号在两层里含义不同，最省事的写法是只用 basename。
        //
        // 三条排除规则都来自第一次真机运行暴露的误报：
        //
        //   * 名字和命令行都以 `!` 开头的是已退出的僵尸/墓碑条目，不是活进程。
        //   * `sh` 是 su 自己——root 包装器跑的是 sh，每个用 root 的东西都会
        //     留下一个，它跟 CarLife 没有任何关系，却因为匹配了 `carlink.monitor`
        //     的命令行被当成 CarLife 进程收了进来。
        //   * 命令行匹配只认包名本身，不认任意包含它的路径，否则任何命令行里
        //     恰好带了这几个字的进程都会被算进来。
        val script = listOf(
            "for d in /proc/[0-9]*; do",
            "  [ -r \"\$d/cmdline\" ] || continue",
            "  n=\$(cat \"\$d/comm\" 2>/dev/null)",
            "  case \"\$n\" in '!'*) continue;; esac",
            "  case \"\$n\" in sh|ash|bash|su|toybox) continue;; esac",
            "  c=\$(tr '\\0' ' ' < \"\$d/cmdline\" 2>/dev/null)",
            "  case \"\$c\" in '!'*) continue;; esac",
            "  case \"\$n \$c\" in",
            "    *baidu.carlife*|*com.hihonor.auto*|*com.carlink.cast*|*com.carlink.monitor*)",
            "      echo \"PROC \$(basename \$d) \$(cat \"\$d/stat\" 2>/dev/null)\";;",
            "  esac",
            "done",
        ).joinToString("\n")

        val raw = runner.run(script)
        if (raw == null) {
            errors += ERR_PROC_UNREADABLE
            return emptyList()
        }
        return raw.lineSequence()
            .filter { it.startsWith("PROC ") }
            .mapNotNull { line -> parseStat(line.removePrefix("PROC ")) }
            .toList()
    }

    /**
     * 解析 `/proc/<pid>/stat`。
     *
     * 第二字段 comm 用括号包着，里面可能有空格（`(some proc)`），所以不能
     * 按空白切分——必须从最后一个 `)` 之后开始数。字段序号按 proc(5)：
     * 之后第 1 个是 state，第 2 个是 ppid，第 12/13 是 utime/stime，
     * 第 18 是 num_threads，第 22 是 rss（单位页）。
     */
    internal fun parseStat(entry: String): ProcInfo? {
        val pid = entry.substringBefore(' ').toIntOrNull() ?: return null
        // comm 被括号包裹，从第一个 '(' 到最后一个 ')' 之间全部是名字，
        // 名字里允许出现空格，理论上也允许出现括号。用 indexOf/lastIndexOf
        // 而不是 substringAfter('(')，否则名字以 '(' 开头时会吃掉一个字符——
        // 第一次真机运行就出现过 `om.hihonor.auto`：真实名字是
        // `com.hihonor.auto`，首字母被切掉了。
        val open = entry.indexOf('(')
        val close = entry.lastIndexOf(") ")
        if (open < 0 || close <= open) return null
        val name = entry.substring(open + 1, close)
        val fields = entry.substring(close + 2).split(' ').filter { it.isNotEmpty() }
        if (fields.size < 22) return null
        val state = fields[0].firstOrNull() ?: return null
        return ProcInfo(
            pid = pid,
            ppid = fields[1].toIntOrNull() ?: 0,
            name = name,
            state = state,
            cpuMillis = ((fields[11].toLongOrNull() ?: 0L) + (fields[12].toLongOrNull() ?: 0L)) * 10L,
            threads = fields[17].toIntOrNull() ?: 0,
            rssKb = (fields[21].toLongOrNull() ?: 0L) * 4L,
        )
    }

    /**
     * 读 CarLife 的 TCP 端口。
     *
     * 必须在 root 下遍历 /proc/<pid>/net 而不是读 /proc/net：从 Android 10 起
     * `/proc/net` 被 mount 成每个网络命名空间一份，普通进程读到的只有自己
     * 那个命名空间的 socket。第一次真机运行时这里恒为「无连接」——而
     * carlink-cast 的同一时刻日志显示 7 个端口全部 ESTABLISHED。
     *
     * 换成读目标进程自己的 `/proc/<pid>/net/tcp{,6}`：那是它所在的命名空间，
     * 只要进程是 CarLife 本体，这里就能看到它开的全部端口。
     */
    private fun readSockets(processes: List<ProcInfo>, errors: MutableList<String>): List<SocketInfo> {
        val out = mutableListOf<SocketInfo>()
        var readAny = false
        for (proc in processes) {
            for (file in listOf("tcp", "tcp6")) {
                val raw = runner.run("cat /proc/${proc.pid}/net/$file 2>/dev/null") ?: continue
                if (raw.isBlank()) continue
                readAny = true
                for (line in raw.lineSequence().drop(1)) {
                    val fields = line.trim().split(Regex("\\s+"))
                    if (fields.size < 5) continue
                    val port = fields[1].substringAfter(':', "").toIntOrNull(16) ?: continue
                    if (port !in CARLIFE_PORTS) continue
                    val queue = fields[4].split(':')
                    val socket = SocketInfo(
                        port = port,
                        state = stateName(fields[3]),
                        txQueue = queue.getOrNull(0)?.toLongOrNull(16) ?: 0L,
                        rxQueue = queue.getOrNull(1)?.toLongOrNull(16) ?: 0L,
                    )
                    // 同一个端口在多个进程的命名空间里会重复出现，按 (端口, 状态) 去重
                    if (out.none { it.port == port && it.state == socket.state }) out += socket
                }
            }
        }
        if (!readAny && processes.isNotEmpty()) errors += ERR_SOCKETS_UNREADABLE
        return out
    }

    private fun readCount(command: String, errors: MutableList<String>): Int? =
        runner.run(command)?.trim()?.toIntOrNull()

    internal fun stateName(hex: String) = when (hex.uppercase()) {
        "01" -> "ESTAB"; "02" -> "SYN_SENT"; "03" -> "SYN_RECV"
        "04" -> "FIN_W1"; "05" -> "FIN_W2"; "06" -> "TIME_WT"
        "07" -> "CLOSE"; "08" -> "CLOSE_WT"; "09" -> "LAST_ACK"
        "0A" -> "LISTEN"; "0B" -> "CLOSING"
        else -> hex
    }

    companion object {
        const val ERR_NO_ROOT = "没有 root 权限，读不到 CarLife 的进程状态"
        const val ERR_PROC_UNREADABLE = "读不到 /proc 进程列表"
        const val ERR_SOCKETS_UNREADABLE = "读不到 CarLife 的 socket 表"
        val CARLIFE_PORTS = setOf(7240, 8240, 9240, 9241, 9242, 9340, 9440)
    }
}

/**
 * 跑一条只读 shell 命令，返回 stdout；失败或超时返回 null。
 *
 * 只有一个抽象方法，测试里可以直接用 lambda 伪造一个不存在的 root 环境。
 * 「有没有 root」不单独建一个接口方法——它本身就是 `id -u` 的结果，多一个
 * 方法就多一处可以和真实情况不一致的地方。
 */
fun interface Shell {
    fun run(command: String): String?
}

/**
 * `su -c` 包装。
 *
 * 每次都新起一个 su 进程：常驻 su shell 省开销，但它在多个采样线程之间共享
 * stdin/stdout 时很容易串味，一次读到上一轮残留的输出就会得出完全错误的结论。
 * 以监测工具来说，读错比读慢危险得多。
 */
class RootShell(private val timeoutMillis: Long = 8_000L) : Shell {
    fun available(): Boolean = run("id -u")?.trim() == "0"

    override fun run(command: String): String? = try {
        val process = ProcessBuilder("su", "-c", command)
            .redirectErrorStream(false)
            .start()
        val out = process.inputStream.bufferedReader().use { it.readText() }
        if (!process.waitFor(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS)) {
            process.destroyForcibly()
            null
        } else out
    } catch (_: Exception) {
        null
    }
}
