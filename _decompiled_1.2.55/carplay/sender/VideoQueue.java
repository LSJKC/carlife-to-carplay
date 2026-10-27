package com.carplay.sender;

import java.util.ArrayDeque;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: VideoQueue.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u001b\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\u000e\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\u000bJ\u0006\u0010\u0018\u001a\u00020\u0019J\u000e\u0010\u001a\u001a\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\u000bJ)\u0010\u001b\u001a\u0004\u0018\u0001H\u001c\"\u0004\b\u0000\u0010\u001c2\u0006\u0010\u0017\u001a\u00020\u000b2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u0002H\u001c0\u001e¢\u0006\u0002\u0010\u001fJ\b\u0010 \u001a\u0004\u0018\u00010\u000bJ\u0006\u0010!\u001a\u00020\u0019R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015¨\u0006\""}, d2 = {"Lcom/carplay/sender/VideoQueue;", "", "capacity", "", "byteLimit", "<init>", "(II)V", "lock", "Ljava/lang/Object;", "frames", "Ljava/util/ArrayDeque;", "Lcom/carplay/sender/EncodedVideo;", "bytes", "closed", "", "waitingForKeyFrame", "epoch", "", "value", "dropped", "getDropped", "()J", "offer", "frame", "resetForKeyFrame", "", "isCurrent", "withCurrent", "T", "action", "Lkotlin/Function0;", "(Lcom/carplay/sender/EncodedVideo;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "take", "close", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class VideoQueue {
    private final int byteLimit;
    private int bytes;
    private final int capacity;
    private boolean closed;
    private long dropped;
    private long epoch;
    private final ArrayDeque<EncodedVideo> frames;
    private final Object lock;
    private boolean waitingForKeyFrame;

    /* JADX WARN: Illegal instructions before constructor call */
    public VideoQueue() {
        int i = 0;
        this(i, i, 3, null);
    }

    public VideoQueue(int capacity, int byteLimit) {
        this.capacity = capacity;
        this.byteLimit = byteLimit;
        this.lock = new Object();
        this.frames = new ArrayDeque<>();
        this.waitingForKeyFrame = true;
        if (this.capacity > 0 && this.byteLimit > 0) {
        } else {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
    }

    public /* synthetic */ VideoQueue(int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? 8 : i, (i3 & 2) != 0 ? 8388608 : i2);
    }

    public final long getDropped() {
        return this.dropped;
    }

    public final boolean offer(EncodedVideo frame) {
        Intrinsics.checkNotNullParameter(frame, "frame");
        synchronized (this.lock) {
            if (this.closed) {
                return false;
            }
            int i = this.byteLimit;
            int length = frame.getBytes().length;
            if (!(1 <= length && length <= i)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            if (this.waitingForKeyFrame && !frame.getKeyFrame()) {
                this.dropped++;
                return false;
            }
            if (this.frames.size() >= this.capacity || this.bytes + frame.getBytes().length > this.byteLimit) {
                this.dropped += (long) this.frames.size();
                this.frames.clear();
                this.bytes = 0;
                this.waitingForKeyFrame = true;
                if (!frame.getKeyFrame()) {
                    this.dropped++;
                    return false;
                }
            }
            this.waitingForKeyFrame = false;
            ArrayDeque<EncodedVideo> arrayDeque = this.frames;
            byte[] bytes = frame.getBytes();
            byte[] bArrCopyOf = Arrays.copyOf(bytes, bytes.length);
            Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
            arrayDeque.addLast(EncodedVideo.copy$default(frame, bArrCopyOf, 0L, false, 0L, this.epoch, 14, null));
            this.bytes += frame.getBytes().length;
            this.lock.notifyAll();
            return true;
        }
    }

    public final void resetForKeyFrame() {
        synchronized (this.lock) {
            this.dropped += (long) this.frames.size();
            this.frames.clear();
            this.bytes = 0;
            this.waitingForKeyFrame = true;
            this.epoch = this.epoch == Long.MAX_VALUE ? 0L : this.epoch + 1;
            this.lock.notifyAll();
            Unit unit = Unit.INSTANCE;
        }
    }

    public final boolean isCurrent(EncodedVideo frame) {
        boolean z;
        Intrinsics.checkNotNullParameter(frame, "frame");
        synchronized (this.lock) {
            z = !this.closed && frame.getEpoch() == this.epoch;
        }
        return z;
    }

    public final <T> T withCurrent(EncodedVideo frame, Function0<? extends T> action) {
        T tInvoke;
        Intrinsics.checkNotNullParameter(frame, "frame");
        Intrinsics.checkNotNullParameter(action, "action");
        synchronized (this.lock) {
            tInvoke = (this.closed || frame.getEpoch() != this.epoch) ? null : action.invoke();
        }
        return tInvoke;
    }

    public final EncodedVideo take() {
        EncodedVideo encodedVideo;
        synchronized (this.lock) {
            while (this.frames.isEmpty() && !this.closed) {
                this.lock.wait();
            }
            if (this.frames.isEmpty()) {
                encodedVideo = null;
            } else {
                EncodedVideo encodedVideoRemoveFirst = this.frames.removeFirst();
                EncodedVideo it = encodedVideoRemoveFirst;
                this.bytes -= it.getBytes().length;
                encodedVideo = encodedVideoRemoveFirst;
            }
        }
        return encodedVideo;
    }

    public final void close() {
        synchronized (this.lock) {
            this.closed = true;
            this.frames.clear();
            this.bytes = 0;
            this.lock.notifyAll();
            Unit unit = Unit.INSTANCE;
        }
    }
}
