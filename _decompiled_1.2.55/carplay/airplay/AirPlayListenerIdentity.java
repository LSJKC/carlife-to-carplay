package com.carplay.airplay;

import java.util.concurrent.atomic.AtomicLong;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: AirPlayTcpAccepted.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0004\u0018\u0000 \f2\u00020\u0001:\u0001\fB\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\r"}, d2 = {"Lcom/carplay/airplay/AirPlayListenerIdentity;", "", "generation", "", "<init>", "(I)V", "getGeneration", "()I", "id", "", "getId", "()J", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AirPlayListenerIdentity {
    private static final Companion Companion = new Companion(null);
    private static final AtomicLong nextId = new AtomicLong();
    private final int generation;
    private final long id = nextId.incrementAndGet();

    public AirPlayListenerIdentity(int generation) {
        this.generation = generation;
    }

    public final int getGeneration() {
        return this.generation;
    }

    /* JADX INFO: compiled from: AirPlayTcpAccepted.kt */
    @Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\b"}, d2 = {"Lcom/carplay/airplay/AirPlayListenerIdentity$Companion;", "", "<init>", "()V", "nextId", "Ljava/util/concurrent/atomic/AtomicLong;", "getNextId", "()Ljava/util/concurrent/atomic/AtomicLong;", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final AtomicLong getNextId() {
            return AirPlayListenerIdentity.nextId;
        }
    }

    public final long getId() {
        return this.id;
    }
}
