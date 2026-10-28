package com.carlink.monitor

/**
 * 相邻两次采样的对比，回答一个问题：**这次出了什么事。**
 *
 * 判读全部基于「变化」而不是「状态」：CarLife 端口列在 LISTEN 是常态，
 * 从 ESTAB 掉到 CLOSE_WT 才是事件。所以每一次判读都要上一次的采样做参照。
 */
object Verdict {

    enum class Severity { INFO, NOTICE, ALERT }

    data class Event(val atMillis: Long, val severity: Severity, val text: String)

    /**
     * @param previous 上一轮采样；第一轮传 null 时只报告初始状态
     * @param current  本轮采样
     */
    fun diff(previous: Sample?, current: Sample): List<Event> {
        val events = mutableListOf<Event>()
        val at = current.atMillis

        if (!current.hasRoot) {
            events += Event(at, Severity.ALERT, Sampler.ERR_NO_ROOT + "；采样结果不可信")
            return events
        }

        if (previous == null) {
            events += Event(at, Severity.INFO, describeBaseline(current))
            return events
        }

        // ---- 1. 进程存活 ----
        val before = previous.processes.associateBy { it.pid }
        val after = current.processes.associateBy { it.pid }

        for ((pid, proc) in after) {
            val old = before[pid]
            if (old != null && !old.isDead && proc.isDead) {
                events += Event(at, Severity.ALERT,
                    "${proc.name}(pid=$pid) 变成僵尸进程（state=${proc.state}）：进程已退出但父进程没回收")
            }
            if (old != null && old.state != proc.state) {
                events += Event(at, Severity.NOTICE,
                    "${proc.name}(pid=$pid) 状态 ${old.state} → ${proc.state}")
            }
            // CPU 停了但进程还在：比崩溃更难发现，也更像「卡住」而不是「死掉」
            if (old != null && !proc.isDead && proc.cpuMillis == old.cpuMillis) {
                events += Event(at, Severity.NOTICE,
                    "${proc.name}(pid=$pid) 这一轮 CPU 时间没有增长（停在 ${proc.cpuMillis}ms）")
            }
        }
        for ((pid, proc) in before) {
            if (pid !in after && !proc.isDead) {
                events += Event(at, Severity.ALERT,
                    "${proc.name}(pid=$pid) 从进程列表中消失：进程已退出")
            }
        }

        // ---- 2. 端口状态 ----
        // 键用 (端口, 状态)：同一个端口可能有监听和已连接两条记录，
        // 按端口去重会把它们合并成一条，从而漏掉真正的状态变化。
        val oldSockets = previous.sockets.groupBy { it.port }.mapValues { e -> e.value.map { it.state }.toSet() }
        val newSockets = current.sockets.groupBy { it.port }.mapValues { e -> e.value.map { it.state }.toSet() }

        for (port in Sampler.CARLIFE_PORTS.sorted()) {
            val was = oldSockets[port].orEmpty()
            val now = newSockets[port].orEmpty()
            if (was == now) continue
            val text = "CarLife 端口 $port：${was.ifEmpty { setOf("无") }.joinToString("/")} → " +
                now.ifEmpty { setOf("消失") }.joinToString("/")
            /*
             * ESTAB → CLOSE_WT 是「对端先关、本端还没关」。
             *
             * 这是整个监测里最有价值的一条：它直接回答「谁先断开」。
             * CLOSE_WAIT 只会由收到对端 FIN 产生，本端主动关闭走的是
             * FIN_WAIT_1/2。所以看到 CLOSE_WT 就可以断定不是 CarLife 先动的手。
             */
            val peerFirst = was.contains("ESTAB") && now.contains("CLOSE_WT")
            events += Event(
                at,
                if (peerFirst) Severity.ALERT else Severity.NOTICE,
                if (peerFirst) "$text（CLOSE_WT = 对端先发 FIN，CarLife 是被动关的一方）" else text,
            )
        }

        // ---- 3. 接收队列堆积 ----
        for (s in current.sockets) {
            if (s.rxQueue <= 0L) continue
            val old = previous.sockets.firstOrNull { it.port == s.port && it.state == s.state }
            if (old != null && s.rxQueue > old.rxQueue && s.rxQueue > RX_BACKLOG_BYTES) {
                events += Event(at, Severity.NOTICE,
                    "端口 ${s.port} 接收队列在堆积（${old.rxQueue}B → ${s.rxQueue}B）：数据到了但没人读")
            }
        }

        // ---- 4. 画面停摆 ----
        val encBefore = previous.encoderRefs
        val encNow = current.encoderRefs
        if (encBefore != null && encNow != null && encBefore > 0 && encNow == 0) {
            events += Event(at, Severity.ALERT,
                "AVC 编码器引用归零：CarLife 不再出画面（进程可能还活着）")
        }
        if (encBefore != null && encNow != null && encBefore == 0 && encNow > 0) {
            events += Event(at, Severity.INFO, "AVC 编码器开始被占用：CarLife 开始出画面")
        }

        // ---- 5. fd 泄漏 ----
        for ((pid, now) in current.fdCounts) {
            val old = previous.fdCounts[pid] ?: continue
            if (old != null && now != null && now - old >= FD_GROWTH_ALERT) {
                events += Event(at, Severity.NOTICE,
                    "pid=$pid 的文件描述符持续增长（$old → $now）：可能有 socket 泄漏")
            }
        }

        return events
    }

    private fun describeBaseline(s: Sample): String = buildString {
        append("基线：")
        append(if (s.processes.isEmpty()) "没有发现 CarLife 进程" else
            s.processes.joinToString("，") { "${it.name}(pid=${it.pid}) ${it.state} ${it.rssKb}KB" })
        append("；端口 ")
        append(s.sockets.joinToString("，") { "${it.port}:${it.state}" }.ifEmpty { "无" })
        append("；AVC 编码器引用=${s.encoderRefs ?: "未知"}")
        append("；CarLife 图层=${s.carLifeLayers ?: "未知"}")
    }

    /** 一行的紧凑表示，用于日志列表。 */
    fun line(s: Sample): String = buildString {
        append(if (s.processes.isEmpty()) "CarLife 未运行" else
            s.processes.joinToString(" ") { "${it.name}/${it.state}/${it.rssKb}KB/${it.threads}T" })
        append(" | ")
        append(s.sockets.filter { it.state == "ESTAB" }.map { it.port }.sorted()
            .joinToString(",").ifEmpty { "无连接" })
        append(" | enc=").append(s.encoderRefs ?: "?")
        append(" layer=").append(s.carLifeLayers ?: "?")
    }

    private const val RX_BACKLOG_BYTES = 8192L
    private const val FD_GROWTH_ALERT = 20
}
