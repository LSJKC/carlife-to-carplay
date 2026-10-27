package com.carplay.sender;

import java.util.ArrayDeque;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AudioQueueWait.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\u001a8\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0000¨\u0006\n"}, d2 = {"takeAudioQueueItem", "Lcom/carplay/sender/AudioQueueTake;", "T", "lock", "Ljava/lang/Object;", "queue", "Ljava/util/ArrayDeque;", "isRunning", "Lkotlin/Function0;", "", "core-airplay"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class AudioQueueWaitKt {
    public static final <T> AudioQueueTake<T> takeAudioQueueItem(Object lock, ArrayDeque<T> queue, Function0<Boolean> isRunning) {
        Object it;
        AudioQueueTake.Stopped item;
        Intrinsics.checkNotNullParameter(lock, "lock");
        Intrinsics.checkNotNullParameter(queue, "queue");
        Intrinsics.checkNotNullParameter(isRunning, "isRunning");
        try {
            synchronized (lock) {
                while (queue.isEmpty() && isRunning.invoke().booleanValue()) {
                    lock.wait(250L);
                }
                item = (isRunning.invoke().booleanValue() && (it = queue.pollFirst()) != null) ? new AudioQueueTake.Item(it) : AudioQueueTake.Stopped.INSTANCE;
            }
            return item;
        } catch (InterruptedException e) {
            return AudioQueueTake.Interrupted.INSTANCE;
        }
    }
}
