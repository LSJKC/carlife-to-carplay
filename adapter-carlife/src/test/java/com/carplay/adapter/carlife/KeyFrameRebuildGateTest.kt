package com.carplay.adapter.carlife

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class KeyFrameRebuildGateTest {
    @Test
    fun `coalesces one handover and opens immediately after the IDR`() {
        val gate = KeyFrameRebuildGate()

        assertTrue(gate.request())
        assertTrue(gate.isInFlight())
        assertFalse(gate.request())

        gate.observeFrame(false)
        assertFalse(gate.request())

        gate.observeFrame(true)
        assertFalse(gate.isInFlight())
        assertTrue(gate.request())
    }

    @Test
    fun `accepts a fresh keyframe request after the previous rebuild IDR`() {
        val gate = KeyFrameRebuildGate()

        assertTrue(gate.request())
        assertFalse(gate.request())
        assertFalse(gate.beginRetry())
        gate.observeFrame(true)

        assertTrue(gate.request())
        assertFalse(gate.request())
        gate.observeFrame(true)
        assertTrue(gate.beginRetry())
    }

    @Test
    fun `ordinary frames do not release an in-flight rebuild`() {
        val gate = KeyFrameRebuildGate()

        assertTrue(gate.request())
        gate.observeFrame(false)

        assertTrue(gate.isInFlight())
        assertFalse(gate.request())
    }

    @Test
    fun `reset allows a new request when a session is torn down`() {
        val gate = KeyFrameRebuildGate()
        assertTrue(gate.request())

        gate.reset()

        assertFalse(gate.isInFlight())
        assertTrue(gate.request())
    }
}
