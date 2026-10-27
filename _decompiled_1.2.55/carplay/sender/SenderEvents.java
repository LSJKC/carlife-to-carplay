package com.carplay.sender;

import com.carplay.iap2.transport.BlockingDuplexByteStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AirPlaySender.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010$\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0016J\u0010\u0010\b\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u0007H&J\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u0007H\u0016J\b\u0010\n\u001a\u00020\u0003H&J\b\u0010\u000b\u001a\u00020\u0003H\u0016J\u0018\u0010\f\u001a\u00020\r2\u000e\u0010\u000e\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u000fH&J\u0010\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\rH&J\u0018\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H&J\u0010\u0010\u0017\u001a\u00020\u00032\u0006\u0010\u0018\u001a\u00020\u0016H&J\b\u0010\u0019\u001a\u00020\u0003H&J\u0010\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u001b\u001a\u00020\u001cH&J\b\u0010\u001d\u001a\u00020\rH&J\u0016\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u00030\u001f2\u0006\u0010 \u001a\u00020!H&J\u0010\u0010\"\u001a\u00020\u00032\u0006\u0010#\u001a\u00020$H&¨\u0006%À\u0006\u0003"}, d2 = {"Lcom/carplay/sender/SenderEvents;", "", "diagnostic", "", "message", "", "outputDisplay", "Lcom/carplay/sender/SenderDisplay;", "display", "startDisplay", "keyFrameRequested", "screenRestoreKeyFrameRequested", "input", "", "command", "", "screenOwnership", "owned", "sent", "bytes", "", "queuedNanos", "", "dropped", "count", "audioSent", "audioFormat", "format", "Lcom/carplay/sender/SenderAudioFormat;", "bluetoothControlAvailable", "iapControl", "Ljava/util/concurrent/CompletableFuture;", "stream", "Lcom/carplay/iap2/transport/BlockingDuplexByteStream;", "failed", "error", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface SenderEvents {
    void audioFormat(SenderAudioFormat format);

    void audioSent();

    boolean bluetoothControlAvailable();

    void diagnostic(String message);

    void display(SenderDisplay display);

    void dropped(long count);

    void failed(Throwable error);

    CompletableFuture<Unit> iapControl(BlockingDuplexByteStream stream);

    boolean input(Map<?, ?> command);

    void keyFrameRequested();

    void screenOwnership(boolean owned);

    void sent(int bytes, long queuedNanos);

    /* JADX INFO: compiled from: AirPlaySender.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final class DefaultImpls {
        @Deprecated
        public static SenderDisplay outputDisplay(SenderEvents $this, SenderDisplay display) {
            Intrinsics.checkNotNullParameter(display, "display");
            return SenderEvents.super.outputDisplay(display);
        }

        @Deprecated
        public static void startDisplay(SenderEvents $this, SenderDisplay display) {
            Intrinsics.checkNotNullParameter(display, "display");
            SenderEvents.super.startDisplay(display);
        }

        @Deprecated
        public static void screenRestoreKeyFrameRequested(SenderEvents $this) {
            SenderEvents.super.screenRestoreKeyFrameRequested();
        }
    }

    default SenderDisplay outputDisplay(SenderDisplay display) {
        Intrinsics.checkNotNullParameter(display, "display");
        return display;
    }

    default void startDisplay(SenderDisplay display) {
        Intrinsics.checkNotNullParameter(display, "display");
    }

    default void screenRestoreKeyFrameRequested() {
        keyFrameRequested();
    }
}
