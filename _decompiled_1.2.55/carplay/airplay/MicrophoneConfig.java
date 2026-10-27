package com.carplay.airplay;

import java.net.InetAddress;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: MicrophonePacketizer.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b*\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u0086\b\u0018\u0000 >2\u00020\u0001:\u0001>Bg\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\u0005\u0012\u0006\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0005¢\u0006\u0004\b\u0012\u0010\u0013J\t\u0010,\u001a\u00020\u0003HÆ\u0003J\t\u0010-\u001a\u00020\u0005HÆ\u0003J\t\u0010.\u001a\u00020\u0005HÆ\u0003J\t\u0010/\u001a\u00020\u0005HÆ\u0003J\t\u00100\u001a\u00020\u0005HÆ\u0003J\t\u00101\u001a\u00020\nHÆ\u0003J\t\u00102\u001a\u00020\u0005HÆ\u0003J\t\u00103\u001a\u00020\rHÆ\u0003J\t\u00104\u001a\u00020\u000fHÆ\u0003J\u0010\u00105\u001a\u0004\u0018\u00010\u0005HÆ\u0003¢\u0006\u0002\u0010#J\t\u00106\u001a\u00020\u0005HÆ\u0003J~\u00107\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000f2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0011\u001a\u00020\u0005HÆ\u0001¢\u0006\u0002\u00108J\u0013\u00109\u001a\u00020:2\b\u0010;\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010<\u001a\u00020\u0005HÖ\u0001J\t\u0010=\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0017R\u0011\u0010\u0007\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0017R\u0011\u0010\b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0017R\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u000b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0017R\u0011\u0010\f\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0011\u0010\u000e\u001a\u00020\u000f¢\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0015\u0010\u0010\u001a\u0004\u0018\u00010\u0005¢\u0006\n\n\u0002\u0010$\u001a\u0004\b\"\u0010#R\u0011\u0010\u0011\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b%\u0010\u0017R\u0011\u0010&\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b'\u0010\u0017R\u0011\u0010(\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b)\u0010\u0017R\u0011\u0010*\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b+\u0010\u0017¨\u0006?"}, d2 = {"Lcom/carplay/airplay/MicrophoneConfig;", "", "audioType", "", "sampleRate", "", "channels", "payloadType", "frameMillis", "host", "Ljava/net/InetAddress;", "port", "key", "", "codec", "Lcom/carplay/airplay/AudioCodecKind;", "bitrate", "opusClockRate", "<init>", "(Ljava/lang/String;IIIILjava/net/InetAddress;I[BLcom/carplay/airplay/AudioCodecKind;Ljava/lang/Integer;I)V", "getAudioType", "()Ljava/lang/String;", "getSampleRate", "()I", "getChannels", "getPayloadType", "getFrameMillis", "getHost", "()Ljava/net/InetAddress;", "getPort", "getKey", "()[B", "getCodec", "()Lcom/carplay/airplay/AudioCodecKind;", "getBitrate", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getOpusClockRate", "samplesPerPacket", "getSamplesPerPacket", "rtpSamplesPerPacket", "getRtpSamplesPerPacket", "frameBytes", "getFrameBytes", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "copy", "(Ljava/lang/String;IIIILjava/net/InetAddress;I[BLcom/carplay/airplay/AudioCodecKind;Ljava/lang/Integer;I)Lcom/carplay/airplay/MicrophoneConfig;", "equals", "", "other", "hashCode", "toString", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class MicrophoneConfig {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final int OPUS_CAPTURE_RATE = 48000;
    private static final int OPUS_FRAME_MILLIS = 20;
    private static final int OPUS_SAMPLES_PER_PACKET = 960;
    private final String audioType;
    private final Integer bitrate;
    private final int channels;
    private final AudioCodecKind codec;
    private final int frameMillis;
    private final InetAddress host;
    private final byte[] key;
    private final int opusClockRate;
    private final int payloadType;
    private final int port;
    private final int sampleRate;

    public static /* synthetic */ MicrophoneConfig copy$default(MicrophoneConfig microphoneConfig, String str, int i, int i2, int i3, int i4, InetAddress inetAddress, int i5, byte[] bArr, AudioCodecKind audioCodecKind, Integer num, int i6, int i7, Object obj) {
        if ((i7 & 1) != 0) {
            str = microphoneConfig.audioType;
        }
        if ((i7 & 2) != 0) {
            i = microphoneConfig.sampleRate;
        }
        if ((i7 & 4) != 0) {
            i2 = microphoneConfig.channels;
        }
        if ((i7 & 8) != 0) {
            i3 = microphoneConfig.payloadType;
        }
        if ((i7 & 16) != 0) {
            i4 = microphoneConfig.frameMillis;
        }
        if ((i7 & 32) != 0) {
            inetAddress = microphoneConfig.host;
        }
        if ((i7 & 64) != 0) {
            i5 = microphoneConfig.port;
        }
        if ((i7 & 128) != 0) {
            bArr = microphoneConfig.key;
        }
        if ((i7 & 256) != 0) {
            audioCodecKind = microphoneConfig.codec;
        }
        if ((i7 & 512) != 0) {
            num = microphoneConfig.bitrate;
        }
        if ((i7 & 1024) != 0) {
            i6 = microphoneConfig.opusClockRate;
        }
        Integer num2 = num;
        int i8 = i6;
        byte[] bArr2 = bArr;
        AudioCodecKind audioCodecKind2 = audioCodecKind;
        InetAddress inetAddress2 = inetAddress;
        int i9 = i5;
        int i10 = i4;
        int i11 = i2;
        return microphoneConfig.copy(str, i, i11, i3, i10, inetAddress2, i9, bArr2, audioCodecKind2, num2, i8);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getAudioType() {
        return this.audioType;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final Integer getBitrate() {
        return this.bitrate;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final int getOpusClockRate() {
        return this.opusClockRate;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getSampleRate() {
        return this.sampleRate;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final int getChannels() {
        return this.channels;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int getPayloadType() {
        return this.payloadType;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final int getFrameMillis() {
        return this.frameMillis;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final InetAddress getHost() {
        return this.host;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final int getPort() {
        return this.port;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final byte[] getKey() {
        return this.key;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final AudioCodecKind getCodec() {
        return this.codec;
    }

    public final MicrophoneConfig copy(String audioType, int sampleRate, int channels, int payloadType, int frameMillis, InetAddress host, int port, byte[] key, AudioCodecKind codec, Integer bitrate, int opusClockRate) {
        Intrinsics.checkNotNullParameter(audioType, "audioType");
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(codec, "codec");
        return new MicrophoneConfig(audioType, sampleRate, channels, payloadType, frameMillis, host, port, key, codec, bitrate, opusClockRate);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MicrophoneConfig)) {
            return false;
        }
        MicrophoneConfig microphoneConfig = (MicrophoneConfig) other;
        return Intrinsics.areEqual(this.audioType, microphoneConfig.audioType) && this.sampleRate == microphoneConfig.sampleRate && this.channels == microphoneConfig.channels && this.payloadType == microphoneConfig.payloadType && this.frameMillis == microphoneConfig.frameMillis && Intrinsics.areEqual(this.host, microphoneConfig.host) && this.port == microphoneConfig.port && Intrinsics.areEqual(this.key, microphoneConfig.key) && this.codec == microphoneConfig.codec && Intrinsics.areEqual(this.bitrate, microphoneConfig.bitrate) && this.opusClockRate == microphoneConfig.opusClockRate;
    }

    public int hashCode() {
        return (((((((((((((((((((this.audioType.hashCode() * 31) + Integer.hashCode(this.sampleRate)) * 31) + Integer.hashCode(this.channels)) * 31) + Integer.hashCode(this.payloadType)) * 31) + Integer.hashCode(this.frameMillis)) * 31) + this.host.hashCode()) * 31) + Integer.hashCode(this.port)) * 31) + Arrays.hashCode(this.key)) * 31) + this.codec.hashCode()) * 31) + (this.bitrate == null ? 0 : this.bitrate.hashCode())) * 31) + Integer.hashCode(this.opusClockRate);
    }

    public String toString() {
        return "MicrophoneConfig(audioType=" + this.audioType + ", sampleRate=" + this.sampleRate + ", channels=" + this.channels + ", payloadType=" + this.payloadType + ", frameMillis=" + this.frameMillis + ", host=" + this.host + ", port=" + this.port + ", key=" + Arrays.toString(this.key) + ", codec=" + this.codec + ", bitrate=" + this.bitrate + ", opusClockRate=" + this.opusClockRate + ")";
    }

    public MicrophoneConfig(String audioType, int sampleRate, int channels, int payloadType, int frameMillis, InetAddress host, int port, byte[] key, AudioCodecKind codec, Integer bitrate, int opusClockRate) {
        Intrinsics.checkNotNullParameter(audioType, "audioType");
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(codec, "codec");
        this.audioType = audioType;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.payloadType = payloadType;
        this.frameMillis = frameMillis;
        this.host = host;
        this.port = port;
        this.key = key;
        this.codec = codec;
        this.bitrate = bitrate;
        this.opusClockRate = opusClockRate;
    }

    public /* synthetic */ MicrophoneConfig(String str, int i, int i2, int i3, int i4, InetAddress inetAddress, int i5, byte[] bArr, AudioCodecKind audioCodecKind, Integer num, int i6, int i7, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, i, i2, i3, i4, inetAddress, i5, bArr, (i7 & 256) != 0 ? AudioCodecKind.LPCM : audioCodecKind, (i7 & 512) != 0 ? null : num, (i7 & 1024) != 0 ? OPUS_CAPTURE_RATE : i6);
    }

    public final String getAudioType() {
        return this.audioType;
    }

    public final int getSampleRate() {
        return this.sampleRate;
    }

    public final int getChannels() {
        return this.channels;
    }

    public final int getPayloadType() {
        return this.payloadType;
    }

    public final int getFrameMillis() {
        return this.frameMillis;
    }

    public final InetAddress getHost() {
        return this.host;
    }

    public final int getPort() {
        return this.port;
    }

    public final byte[] getKey() {
        return this.key;
    }

    public final AudioCodecKind getCodec() {
        return this.codec;
    }

    public final Integer getBitrate() {
        return this.bitrate;
    }

    public final int getOpusClockRate() {
        return this.opusClockRate;
    }

    public final int getSamplesPerPacket() {
        if (this.codec == AudioCodecKind.OPUS) {
            return OPUS_SAMPLES_PER_PACKET;
        }
        return Math.max(1, (this.sampleRate * this.frameMillis) / 1000);
    }

    public final int getRtpSamplesPerPacket() {
        return this.codec == AudioCodecKind.OPUS ? (this.opusClockRate * 20) / 1000 : getSamplesPerPacket();
    }

    public final int getFrameBytes() {
        return getSamplesPerPacket() * this.channels * 2;
    }

    /* JADX INFO: compiled from: MicrophonePacketizer.kt */
    @Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lcom/carplay/airplay/MicrophoneConfig$Companion;", "", "<init>", "()V", "OPUS_SAMPLES_PER_PACKET", "", "OPUS_CAPTURE_RATE", "OPUS_FRAME_MILLIS", "opusClockRate", "formatBits", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final int opusClockRate(long formatBits) {
            if ((CarPlayMediaEngine.OPUS_48K & formatBits) == 0 && (CarPlayMediaEngine.OPUS_24K & formatBits) != 0) {
                return 24000;
            }
            return MicrophoneConfig.OPUS_CAPTURE_RATE;
        }
    }
}
