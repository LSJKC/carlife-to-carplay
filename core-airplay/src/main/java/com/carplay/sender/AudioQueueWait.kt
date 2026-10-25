package com.carplay.sender

import java.util.ArrayDeque

internal sealed interface AudioQueueTake<out T> {
    data class Item<T>(val value: T) : AudioQueueTake<T>
    data object Stopped : AudioQueueTake<Nothing>
    data object Interrupted : AudioQueueTake<Nothing>
}

internal fun <T> takeAudioQueueItem(
    lock: Object,
    queue: ArrayDeque<T>,
    isRunning: () -> Boolean,
): AudioQueueTake<T> = try {
    synchronized(lock) {
        while (queue.isEmpty() && isRunning()) lock.wait(250)
        if (!isRunning()) AudioQueueTake.Stopped
        else queue.pollFirst()?.let { AudioQueueTake.Item(it) } ?: AudioQueueTake.Stopped
    }
} catch (_: InterruptedException) {
    AudioQueueTake.Interrupted
}
