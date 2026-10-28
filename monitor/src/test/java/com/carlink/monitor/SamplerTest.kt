package com.carlink.monitor

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 采样与判读的单元测试。
 *
 * 这些断言全部来自真实的 `/proc` 和 `/proc/net/tcp` 文本格式：只要解析对了，
 * 手机上跑出来的数字就是可信的；解析错了，界面会显示一个看起来很正常但完全
 * 错误的结论，那比不显示更糟。
 */
class SamplerTest {

    private val sampler = Sampler(Shell { null })

    @Test
    fun statParsesProcessNameWithSpaces() {
        // comm 用括号包着，里面可以有空格——这是不能按空白切分的原因。
        val info = sampler.parseStat(
            "1234 (some proc) S 1 1234 1234 0 -1 4194560 1000 0 0 0 55 66 0 0 20 0 7 0 999 " +
                "12345678 250 18446744073709551615 0 0 0 0 0 0 0 0 0 0 0 0 17 0 0 0 0 0 0",
        )
        assertTrue(info != null)
        assertEquals(1234, info!!.pid)
        assertEquals(1, info.ppid)
        assertEquals("some proc", info.name)
        assertEquals('S', info.state)
        // utime=55 stime=66 → (55+66)*10 = 1210 ms
        assertEquals(1210L, info.cpuMillis)
        assertEquals(7, info.threads)
        // rss=250 页 × 4KB
        assertEquals(1000L, info.rssKb)
    }

    @Test
    fun zombieIsRecognisedAsDead() {
        val info = sampler.parseStat(
            "9 (com.baidu.carlife) Z 1 9 9 0 -1 0 0 0 0 0 10 20 0 0 20 0 3 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0",
        )
        assertTrue(info!!.isDead)
    }

    @Test
    fun statRejectsTruncatedInput() {
        // 少字段时宁可不报，也不要报一个字段错位的进程
        assertEquals(null, sampler.parseStat("7 (x) S 1 7"))
        assertEquals(null, sampler.parseStat("notapid (x) S 1"))
    }

    private fun sample(
        processes: List<ProcInfo> = emptyList(),
        sockets: List<SocketInfo> = emptyList(),
        encoder: Int? = 1,
        errors: List<String> = emptyList(),
    ) = Sample(1000, processes, sockets, encoder, 1, null, emptyMap(), errors)

    private fun proc(pid: Int = 1, state: Char = 'S', cpu: Long = 100) =
        ProcInfo(pid, 1, "com.baidu.carlife", state, cpu, 1000, 5)

    @Test
    fun closeWaitMeansThePeerClosedFirst() {
        val site = Verdict.diff(
            sample(sockets = listOf(SocketInfo(8240, "ESTAB", 0, 0))),
            sample(sockets = listOf(SocketInfo(8240, "CLOSE_WT", 0, 0))),
        )
        val alert = site.first { it.text.contains("8240") }
        assertEquals(Verdict.Severity.ALERT, alert.severity)
        assertTrue(alert.text.contains("对端先发 FIN"))
    }

    @Test
    fun ownCloseIsNotReportedAsPeerClose() {
        // FIN_WAIT_1 是本端主动发的关闭，不能报成「对端先关」
        val site = Verdict.diff(
            sample(sockets = listOf(SocketInfo(8240, "ESTAB", 0, 0))),
            sample(sockets = listOf(SocketInfo(8240, "FIN_W1", 0, 0))),
        )
        assertFalse(site.any { it.text.contains("对端先发 FIN") })
    }

    @Test
    fun processDisappearingIsAnAlert() {
        val site = Verdict.diff(sample(processes = listOf(proc())), sample(processes = emptyList()))
        assertTrue(site.any { it.severity == Verdict.Severity.ALERT && it.text.contains("消失") })
    }

    @Test
    fun encoderDroppingToZeroIsReported() {
        val site = Verdict.diff(sample(encoder = 3), sample(encoder = 0))
        assertTrue(site.any { it.severity == Verdict.Severity.ALERT && it.text.contains("编码器引用归零") })
    }

    @Test
    fun stalledCpuIsNoticed() {
        val site = Verdict.diff(
            sample(processes = listOf(proc(cpu = 500))),
            sample(processes = listOf(proc(cpu = 500))),
        )
        assertTrue(site.any { it.text.contains("CPU 时间没有增长") })
    }

    @Test
    fun growingCpuIsQuiet() {
        val site = Verdict.diff(
            sample(processes = listOf(proc(cpu = 500))),
            sample(processes = listOf(proc(cpu = 900))),
        )
        assertFalse(site.any { it.text.contains("CPU 时间没有增长") })
    }

    @Test
    fun missingRootIsSurfacedInsteadOfLookingLikeNoProcess() {
        val noRoot = sample(processes = emptyList(), errors = listOf(Sampler.ERR_NO_ROOT))
        assertFalse(noRoot.hasRoot)
        val site = Verdict.diff(null, noRoot)
        assertTrue(site.any { it.severity == Verdict.Severity.ALERT && it.text.contains("root") })
    }

    @Test
    fun firstSampleDescribesBaseline() {
        val site = Verdict.diff(null, sample(processes = listOf(proc())))
        assertEquals(1, site.size)
        assertTrue(site.single().text.startsWith("基线"))
    }

    @Test
    fun portStateMappingMatchesProcNetTcp() {
        // 01=ESTABLISHED 08=CLOSE_WAIT 0A=LISTEN，这三个是判读的关键
        assertEquals("ESTAB", stateOf("01"))
        assertEquals("CLOSE_WT", stateOf("08"))
        assertEquals("LISTEN", stateOf("0A"))
        assertEquals("LISTEN", stateOf("0a"))
    }

    private fun stateOf(hex: String): String = sampler.stateName(hex)
}
