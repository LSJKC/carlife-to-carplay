package com.carplay.sender;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AudioQueueWait.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bp\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\u00020\u0002:\u0003\u0003\u0004\u0005\u0082\u0001\u0003\u0006\u0007\b¨\u0006\tÀ\u0006\u0003"}, d2 = {"Lcom/carplay/sender/AudioQueueTake;", "T", "", "Item", "Stopped", "Interrupted", "Lcom/carplay/sender/AudioQueueTake$Interrupted;", "Lcom/carplay/sender/AudioQueueTake$Item;", "Lcom/carplay/sender/AudioQueueTake$Stopped;", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface AudioQueueTake<T> {

    /* JADX INFO: compiled from: AudioQueueWait.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u000f\u0012\u0006\u0010\u0003\u001a\u00028\u0001¢\u0006\u0004\b\u0004\u0010\u0005J\u000e\u0010\t\u001a\u00028\u0001HÆ\u0003¢\u0006\u0002\u0010\u0007J\u001e\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00010\u00002\b\b\u0002\u0010\u0003\u001a\u00028\u0001HÆ\u0001¢\u0006\u0002\u0010\u000bJ\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fHÖ\u0003J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001R\u0013\u0010\u0003\u001a\u00028\u0001¢\u0006\n\n\u0002\u0010\b\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0014"}, d2 = {"Lcom/carplay/sender/AudioQueueTake$Item;", "T", "Lcom/carplay/sender/AudioQueueTake;", "value", "<init>", "(Ljava/lang/Object;)V", "getValue", "()Ljava/lang/Object;", "Ljava/lang/Object;", "component1", "copy", "(Ljava/lang/Object;)Lcom/carplay/sender/AudioQueueTake$Item;", "equals", "", "other", "", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Item<T> implements AudioQueueTake<T> {
        private final T value;

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Item copy$default(Item item, Object obj, int i, Object obj2) {
            if ((i & 1) != 0) {
                obj = item.value;
            }
            return item.copy(obj);
        }

        public final T component1() {
            return this.value;
        }

        public final Item<T> copy(T value) {
            return new Item<>(value);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            return (other instanceof Item) && Intrinsics.areEqual(this.value, ((Item) other).value);
        }

        public int hashCode() {
            if (this.value == null) {
                return 0;
            }
            return this.value.hashCode();
        }

        public String toString() {
            return "Item(value=" + this.value + ")";
        }

        public Item(T t) {
            this.value = t;
        }

        public final T getValue() {
            return this.value;
        }
    }

    /* JADX INFO: compiled from: AudioQueueWait.kt */
    @Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0001\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\bÆ\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0003\u0010\u0004J\u0013\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bHÖ\u0003J\t\u0010\t\u001a\u00020\nHÖ\u0001J\t\u0010\u000b\u001a\u00020\fHÖ\u0001¨\u0006\r"}, d2 = {"Lcom/carplay/sender/AudioQueueTake$Stopped;", "Lcom/carplay/sender/AudioQueueTake;", "", "<init>", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Stopped implements AudioQueueTake {
        public static final Stopped INSTANCE = new Stopped();

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Stopped)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return 1154393255;
        }

        public String toString() {
            return "Stopped";
        }

        private Stopped() {
        }
    }

    /* JADX INFO: compiled from: AudioQueueWait.kt */
    @Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0001\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\bÆ\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0003\u0010\u0004J\u0013\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bHÖ\u0003J\t\u0010\t\u001a\u00020\nHÖ\u0001J\t\u0010\u000b\u001a\u00020\fHÖ\u0001¨\u0006\r"}, d2 = {"Lcom/carplay/sender/AudioQueueTake$Interrupted;", "Lcom/carplay/sender/AudioQueueTake;", "", "<init>", "()V", "equals", "", "other", "", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Interrupted implements AudioQueueTake {
        public static final Interrupted INSTANCE = new Interrupted();

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Interrupted)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return -1557183460;
        }

        public String toString() {
            return "Interrupted";
        }

        private Interrupted() {
        }
    }
}
