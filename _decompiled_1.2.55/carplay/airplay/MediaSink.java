package com.carplay.airplay;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CarPlayMediaEngine.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0018\u0010\b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\nH\u0016J(\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0016J\u001e\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00030\u0012H\u0016J$\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00030\u0014H\u0016J\u0018\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J \u0010\u0019\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u0005H\u0016J(\u0010\u001f\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010 \u001a\u00020\n2\u0006\u0010!\u001a\u00020\u0005H\u0016J\u0010\u0010\"\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u001bH\u0016J\u0018\u0010#\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010$\u001a\u00020%H\u0016J\u0010\u0010&\u001a\u00020\u00032\u0006\u0010\u001a\u001a\u00020\u001bH\u0016J\u0010\u0010'\u001a\u00020\u00032\u0006\u0010(\u001a\u00020\nH\u0016¨\u0006)À\u0006\u0003"}, d2 = {"Lcom/carplay/airplay/MediaSink;", "", "onVideoCodec", "", "type", "", "codec", "Lcom/carplay/airplay/VideoCodec;", "onVideoConfig", "codecData", "", "onVideoFrame", "naluBytes", "senderNanos", "", "arrivalNanos", "setVideoRecoveryHandler", "handler", "Lkotlin/Function0;", "setVideoDiagnosticHandler", "Lkotlin/Function1;", "", "onScreenStreamActive", "active", "", "onAudioStarted", "id", "Lcom/carplay/airplay/AudioStreamId;", "format", "Lcom/carplay/airplay/AudioFormat;", "firstSample", "onAudioRtp", "rtp", "sample", "onAudioStopped", "onMicrophoneStarted", "config", "Lcom/carplay/airplay/MicrophoneConfig;", "onMicrophoneStopped", "onIapMessage", "bytes", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface MediaSink {

    /* JADX INFO: compiled from: CarPlayMediaEngine.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final class DefaultImpls {
        @Deprecated
        public static void onVideoCodec(MediaSink $this, int type, VideoCodec codec) {
            Intrinsics.checkNotNullParameter(codec, "codec");
            MediaSink.super.onVideoCodec(type, codec);
        }

        @Deprecated
        public static void onVideoConfig(MediaSink $this, int type, byte[] codecData) {
            Intrinsics.checkNotNullParameter(codecData, "codecData");
            MediaSink.super.onVideoConfig(type, codecData);
        }

        @Deprecated
        public static void onVideoFrame(MediaSink $this, int type, byte[] naluBytes) {
            Intrinsics.checkNotNullParameter(naluBytes, "naluBytes");
            MediaSink.super.onVideoFrame(type, naluBytes);
        }

        @Deprecated
        public static void onVideoFrame(MediaSink $this, int type, byte[] naluBytes, long senderNanos, long arrivalNanos) {
            Intrinsics.checkNotNullParameter(naluBytes, "naluBytes");
            MediaSink.super.onVideoFrame(type, naluBytes, senderNanos, arrivalNanos);
        }

        @Deprecated
        public static void setVideoRecoveryHandler(MediaSink $this, int type, Function0<Unit> handler) {
            Intrinsics.checkNotNullParameter(handler, "handler");
            MediaSink.super.setVideoRecoveryHandler(type, handler);
        }

        @Deprecated
        public static void setVideoDiagnosticHandler(MediaSink $this, int type, Function1<? super String, Unit> handler) {
            Intrinsics.checkNotNullParameter(handler, "handler");
            MediaSink.super.setVideoDiagnosticHandler(type, handler);
        }

        @Deprecated
        public static void onScreenStreamActive(MediaSink $this, int type, boolean active) {
            MediaSink.super.onScreenStreamActive(type, active);
        }

        @Deprecated
        public static void onAudioStarted(MediaSink $this, AudioStreamId id, AudioFormat format, int firstSample) {
            Intrinsics.checkNotNullParameter(id, "id");
            Intrinsics.checkNotNullParameter(format, "format");
            MediaSink.super.onAudioStarted(id, format, firstSample);
        }

        @Deprecated
        public static void onAudioRtp(MediaSink $this, AudioStreamId id, AudioFormat format, byte[] rtp, int sample) {
            Intrinsics.checkNotNullParameter(id, "id");
            Intrinsics.checkNotNullParameter(format, "format");
            Intrinsics.checkNotNullParameter(rtp, "rtp");
            MediaSink.super.onAudioRtp(id, format, rtp, sample);
        }

        @Deprecated
        public static void onAudioStopped(MediaSink $this, AudioStreamId id) {
            Intrinsics.checkNotNullParameter(id, "id");
            MediaSink.super.onAudioStopped(id);
        }

        @Deprecated
        public static void onMicrophoneStarted(MediaSink $this, AudioStreamId id, MicrophoneConfig config) {
            Intrinsics.checkNotNullParameter(id, "id");
            Intrinsics.checkNotNullParameter(config, "config");
            MediaSink.super.onMicrophoneStarted(id, config);
        }

        @Deprecated
        public static void onMicrophoneStopped(MediaSink $this, AudioStreamId id) {
            Intrinsics.checkNotNullParameter(id, "id");
            MediaSink.super.onMicrophoneStopped(id);
        }

        @Deprecated
        public static void onIapMessage(MediaSink $this, byte[] bytes) {
            Intrinsics.checkNotNullParameter(bytes, "bytes");
            MediaSink.super.onIapMessage(bytes);
        }
    }

    default void onVideoCodec(int type, VideoCodec codec) {
        Intrinsics.checkNotNullParameter(codec, "codec");
    }

    default void onVideoConfig(int type, byte[] codecData) {
        Intrinsics.checkNotNullParameter(codecData, "codecData");
    }

    default void onVideoFrame(int type, byte[] naluBytes) {
        Intrinsics.checkNotNullParameter(naluBytes, "naluBytes");
    }

    default void onVideoFrame(int type, byte[] naluBytes, long senderNanos, long arrivalNanos) {
        Intrinsics.checkNotNullParameter(naluBytes, "naluBytes");
        onVideoFrame(type, naluBytes);
    }

    default void setVideoRecoveryHandler(int type, Function0<Unit> handler) {
        Intrinsics.checkNotNullParameter(handler, "handler");
    }

    default void setVideoDiagnosticHandler(int type, Function1<? super String, Unit> handler) {
        Intrinsics.checkNotNullParameter(handler, "handler");
    }

    default void onScreenStreamActive(int type, boolean active) {
    }

    default void onAudioStarted(AudioStreamId id, AudioFormat format, int firstSample) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(format, "format");
    }

    default void onAudioRtp(AudioStreamId id, AudioFormat format, byte[] rtp, int sample) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(format, "format");
        Intrinsics.checkNotNullParameter(rtp, "rtp");
    }

    default void onAudioStopped(AudioStreamId id) {
        Intrinsics.checkNotNullParameter(id, "id");
    }

    default void onMicrophoneStarted(AudioStreamId id, MicrophoneConfig config) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(config, "config");
    }

    default void onMicrophoneStopped(AudioStreamId id) {
        Intrinsics.checkNotNullParameter(id, "id");
    }

    default void onIapMessage(byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
    }
}
