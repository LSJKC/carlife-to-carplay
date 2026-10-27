package com.carplay.airplay;

import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AudioStream.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J \u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000bR\u000e\u0010\f\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R&\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0007\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\t0\u00110\u0010X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lcom/carplay/airplay/AudioStreamCodec;", "", "<init>", "()V", "fromFormatBits", "Lcom/carplay/airplay/AudioFormat;", "bits", "", "payloadType", "", "audioType", "", "AAC_LC_44K_STEREO", "AAC_LC_48K_STEREO", "OPUS_MONO", "PCM_FORMAT", "", "Lkotlin/Pair;", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AudioStreamCodec {
    private static final long AAC_LC_44K_STEREO = 4194304;
    private static final long AAC_LC_48K_STEREO = 8388608;
    private static final long OPUS_MONO = 1879048192;
    public static final AudioStreamCodec INSTANCE = new AudioStreamCodec();
    private static final Map<Long, Pair<Integer, Integer>> PCM_FORMAT = MapsKt.mapOf(TuplesKt.to(4L, TuplesKt.to(8000, 1)), TuplesKt.to(8L, TuplesKt.to(8000, 2)), TuplesKt.to(16L, TuplesKt.to(16000, 1)), TuplesKt.to(32L, TuplesKt.to(16000, 2)), TuplesKt.to(64L, TuplesKt.to(24000, 1)), TuplesKt.to(128L, TuplesKt.to(24000, 2)), TuplesKt.to(256L, TuplesKt.to(32000, 1)), TuplesKt.to(512L, TuplesKt.to(32000, 2)), TuplesKt.to(1024L, TuplesKt.to(44100, 1)), TuplesKt.to(2048L, TuplesKt.to(44100, 2)), TuplesKt.to(16384L, TuplesKt.to(48000, 1)), TuplesKt.to(32768L, TuplesKt.to(48000, 2)));

    private AudioStreamCodec() {
    }

    public static /* synthetic */ AudioFormat fromFormatBits$default(AudioStreamCodec audioStreamCodec, long j, int i, String str, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            str = "media";
        }
        return audioStreamCodec.fromFormatBits(j, i, str);
    }

    public final AudioFormat fromFormatBits(long bits, int payloadType, String audioType) {
        Intrinsics.checkNotNullParameter(audioType, "audioType");
        boolean isAacLc = (12582912 & bits) != 0;
        boolean isOpus = (OPUS_MONO & bits) != 0;
        Pair<Integer, Integer> pair = PCM_FORMAT.get(Long.valueOf(bits));
        if (isOpus) {
            return new AudioFormat(AudioCodecKind.OPUS, 48000, 1, payloadType, audioType);
        }
        if (isAacLc) {
            return new AudioFormat(AudioCodecKind.AAC_LC, (AAC_LC_48K_STEREO & bits) != 0 ? 48000 : 44100, 2, payloadType, audioType);
        }
        return pair != null ? new AudioFormat(AudioCodecKind.LPCM, pair.getFirst().intValue(), pair.getSecond().intValue(), payloadType, audioType) : new AudioFormat(AudioCodecKind.LPCM, 44100, 2, payloadType, audioType);
    }
}
