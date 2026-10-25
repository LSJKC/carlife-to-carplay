package com.carplay.sender

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.ArrayDeque
import java.util.concurrent.atomic.AtomicReference

class AudioQueueWaitTest {
    @Test
    fun interruptedWaitIsReportedAsControlFlow() {
        val result = AtomicReference<AudioQueueTake<String>?>()
        val thread = Thread {
            Thread.currentThread().interrupt()
            result.set(takeAudioQueueItem(Object(), ArrayDeque()) { true })
        }

        thread.start()
        thread.join(1000)

        assertFalse(thread.isAlive)
        assertEquals(AudioQueueTake.Interrupted, result.get())
    }

    @Test
    fun closedQueueReturnsStoppedAndQueuedItemIsDelivered() {
        val lock = Object()
        val queue = ArrayDeque<String>().apply { addLast("pcm") }

        assertEquals(AudioQueueTake.Item("pcm"), takeAudioQueueItem(lock, queue) { true })
        assertTrue(queue.isEmpty())
        assertEquals(AudioQueueTake.Stopped, takeAudioQueueItem(lock, queue) { false })
    }
}
