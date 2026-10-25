package com.carlink.cast

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CastLogPersistenceTest {
    @Test
    fun snapshotRoundTripsSessionCountersAndBoundedHistory() {
        val state = CastState(
            sessionId = "session-1",
            stage = CastStage.STREAMING,
            detail = "画面已发送",
            running = true,
            events = listOf(CastEvent(1250, "INFO", "开始投屏")),
            metrics = listOf(CastMetric(1000, 29.5, 2400.0)),
            frames = 82,
            bytes = 1_234_567,
            dropped = 2,
            audioPackets = 400,
            sourceAudioBytes = 640_000,
            sourceAudioUnits = 12,
            sourceAudioPcmNonZeroBytes = 600_000,
            sourceAudioPeak = 21000,
            inputEvents = 9,
            source = "CarLife",
            display = "1408×476 · 30fps",
        )

        val snapshot = CastLog.snapshotJson(
            state = state,
            startedAtMillis = 1_000_000,
            savedAtMillis = 1_010_000,
            truncated = true,
        )
        val restored = CastLog.stateFromSnapshot(snapshot)

        assertEquals(state, restored)
        assertEquals(1_000_000L, snapshot.optLong("startedAtMillis"))
        assertTrue(snapshot.optBoolean("journalTruncated"))
        assertTrue(restored.running)
    }

    @Test
    fun snapshotRestoresTerminalSessionAsTerminal() {
        val state = CastState(
            sessionId = "session-2",
            stage = CastStage.FAILED,
            detail = "连接断开",
            running = false,
        )

        val restored = CastLog.stateFromSnapshot(
            CastLog.snapshotJson(state, 2_000_000, 2_001_000, truncated = false),
        )

        assertEquals(CastStage.FAILED, restored.stage)
        assertEquals("连接断开", restored.detail)
        assertFalse(restored.running)
    }
}
