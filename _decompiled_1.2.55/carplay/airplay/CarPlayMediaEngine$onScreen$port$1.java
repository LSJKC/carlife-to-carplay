package com.carplay.airplay;

import android.util.Log;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CarPlayMediaEngine.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u00001\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0011\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0096\u0001J\u0011\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0096\u0001J!\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000b2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0096\u0001J\u0011\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000bH\u0096\u0001¨\u0006\u0011"}, d2 = {"com/carplay/airplay/CarPlayMediaEngine$onScreen$port$1", "Lcom/carplay/airplay/ScreenStream$Listener;", "onClosed", "", "cause", "", "onCodec", "codec", "Lcom/carplay/airplay/VideoCodec;", "onConfig", "codecData", "", "onFrame", "naluBytes", "senderNanos", "", "arrivalNanos", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarPlayMediaEngine$onScreen$port$1 implements ScreenStream.Listener {
    private final /* synthetic */ ScreenStream.Listener $$delegate_0;
    final /* synthetic */ ScreenStream $screen;
    final /* synthetic */ AirPlaySession $session;
    final /* synthetic */ CarPlayMediaEngine.StreamKey $streamKey;
    final /* synthetic */ int $type;
    final /* synthetic */ CarPlayMediaEngine this$0;

    @Override // com.carplay.airplay.ScreenStream.Listener
    public void onCodec(VideoCodec codec) {
        Intrinsics.checkNotNullParameter(codec, "codec");
        this.$$delegate_0.onCodec(codec);
    }

    @Override // com.carplay.airplay.ScreenStream.Listener
    public void onConfig(byte[] codecData) {
        Intrinsics.checkNotNullParameter(codecData, "codecData");
        this.$$delegate_0.onConfig(codecData);
    }

    @Override // com.carplay.airplay.ScreenStream.Listener
    public void onFrame(byte[] naluBytes) {
        Intrinsics.checkNotNullParameter(naluBytes, "naluBytes");
        this.$$delegate_0.onFrame(naluBytes);
    }

    @Override // com.carplay.airplay.ScreenStream.Listener
    public void onFrame(byte[] naluBytes, long senderNanos, long arrivalNanos) {
        Intrinsics.checkNotNullParameter(naluBytes, "naluBytes");
        this.$$delegate_0.onFrame(naluBytes, senderNanos, arrivalNanos);
    }

    CarPlayMediaEngine$onScreen$port$1(int $type, final CarPlayMediaEngine $receiver, final CarPlayMediaEngine.StreamKey $streamKey, final ScreenStream $screen, AirPlaySession $session) {
        this.$type = $type;
        this.this$0 = $receiver;
        this.$streamKey = $streamKey;
        this.$screen = $screen;
        this.$session = $session;
        this.$$delegate_0 = CarPlayMediaEngineKt.currentScreenListener($type, $receiver.sink, new Function0() { // from class: com.carplay.airplay.CarPlayMediaEngine$onScreen$port$1$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return Boolean.valueOf(CarPlayMediaEngine$onScreen$port$1.__delegate_0$lambda$0($receiver, $streamKey, $screen));
            }
        });
    }

    static final boolean __delegate_0$lambda$0(CarPlayMediaEngine this$0, CarPlayMediaEngine.StreamKey $streamKey, ScreenStream $screen) {
        return this$0.streams.get($streamKey) == $screen;
    }

    @Override // com.carplay.airplay.ScreenStream.Listener
    public void onClosed(Throwable cause) {
        String message;
        int i = this.$type;
        if (cause == null || (message = cause.getMessage()) == null) {
            message = "peer EOF";
        }
        Log.w("xcertplay-usb", "screen stream ended type=" + i + " reason=" + message);
        if (this.this$0.streams.remove(this.$streamKey, this.$screen)) {
            this.this$0.sink.onScreenStreamActive(this.$type, false);
        }
        this.$session.close();
    }
}
