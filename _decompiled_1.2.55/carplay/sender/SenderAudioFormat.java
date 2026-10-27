package com.carplay.sender;

import com.carplay.airplay.AudioCodecKind;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: SenderNegotiation.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0012\b\u0086\b\u0018\u00002\u00020\u0001B;\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003¢\u0006\u0004\b\u000b\u0010\fJ\u000e\u0010\u001f\u001a\u00020\u00032\u0006\u0010 \u001a\u00020\u0007J\t\u0010!\u001a\u00020\u0003HÆ\u0003J\t\u0010\"\u001a\u00020\u0003HÆ\u0003J\t\u0010#\u001a\u00020\u0003HÆ\u0003J\t\u0010$\u001a\u00020\u0007HÆ\u0003J\t\u0010%\u001a\u00020\tHÆ\u0003J\t\u0010&\u001a\u00020\u0003HÆ\u0003JE\u0010'\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u0003HÆ\u0001J\u0013\u0010(\u001a\u00020\u00172\b\u0010)\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010*\u001a\u00020\u0003HÖ\u0001J\t\u0010+\u001a\u00020\u001aHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000eR\u0011\u0010\u0016\u001a\u00020\u00178F¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0018R\u0011\u0010\u0019\u001a\u00020\u001a8F¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u001d\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b\u001e\u0010\u000e¨\u0006,"}, d2 = {"Lcom/carplay/sender/SenderAudioFormat;", "", "streamType", "", "sampleRate", "channels", "formatBits", "", "codec", "Lcom/carplay/airplay/AudioCodecKind;", "samplesPerPacket", "<init>", "(IIIJLcom/carplay/airplay/AudioCodecKind;I)V", "getStreamType", "()I", "getSampleRate", "getChannels", "getFormatBits", "()J", "getCodec", "()Lcom/carplay/airplay/AudioCodecKind;", "getSamplesPerPacket", "isPcm", "", "()Z", "key", "", "getKey", "()Ljava/lang/String;", "pcmPacketBytes", "getPcmPacketBytes", "rtpTimestamp", "ptsUs", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "toString", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class SenderAudioFormat {
    private final int channels;
    private final AudioCodecKind codec;
    private final long formatBits;
    private final int sampleRate;
    private final int samplesPerPacket;
    private final int streamType;

    public static /* synthetic */ SenderAudioFormat copy$default(SenderAudioFormat senderAudioFormat, int i, int i2, int i3, long j, AudioCodecKind audioCodecKind, int i4, int i5, Object obj) {
        if ((i5 & 1) != 0) {
            i = senderAudioFormat.streamType;
        }
        if ((i5 & 2) != 0) {
            i2 = senderAudioFormat.sampleRate;
        }
        if ((i5 & 4) != 0) {
            i3 = senderAudioFormat.channels;
        }
        if ((i5 & 8) != 0) {
            j = senderAudioFormat.formatBits;
        }
        if ((i5 & 16) != 0) {
            audioCodecKind = senderAudioFormat.codec;
        }
        if ((i5 & 32) != 0) {
            i4 = senderAudioFormat.samplesPerPacket;
        }
        long j2 = j;
        int i6 = i3;
        return senderAudioFormat.copy(i, i2, i6, j2, audioCodecKind, i4);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getStreamType() {
        return this.streamType;
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
    public final long getFormatBits() {
        return this.formatBits;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final AudioCodecKind getCodec() {
        return this.codec;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final int getSamplesPerPacket() {
        return this.samplesPerPacket;
    }

    public final SenderAudioFormat copy(int streamType, int sampleRate, int channels, long formatBits, AudioCodecKind codec, int samplesPerPacket) {
        Intrinsics.checkNotNullParameter(codec, "codec");
        return new SenderAudioFormat(streamType, sampleRate, channels, formatBits, codec, samplesPerPacket);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SenderAudioFormat)) {
            return false;
        }
        SenderAudioFormat senderAudioFormat = (SenderAudioFormat) other;
        return this.streamType == senderAudioFormat.streamType && this.sampleRate == senderAudioFormat.sampleRate && this.channels == senderAudioFormat.channels && this.formatBits == senderAudioFormat.formatBits && this.codec == senderAudioFormat.codec && this.samplesPerPacket == senderAudioFormat.samplesPerPacket;
    }

    public int hashCode() {
        return (((((((((Integer.hashCode(this.streamType) * 31) + Integer.hashCode(this.sampleRate)) * 31) + Integer.hashCode(this.channels)) * 31) + Long.hashCode(this.formatBits)) * 31) + this.codec.hashCode()) * 31) + Integer.hashCode(this.samplesPerPacket);
    }

    public String toString() {
        return "SenderAudioFormat(streamType=" + this.streamType + ", sampleRate=" + this.sampleRate + ", channels=" + this.channels + ", formatBits=" + this.formatBits + ", codec=" + this.codec + ", samplesPerPacket=" + this.samplesPerPacket + ")";
    }

    public SenderAudioFormat(int streamType, int sampleRate, int channels, long formatBits, AudioCodecKind codec, int samplesPerPacket) {
        Intrinsics.checkNotNullParameter(codec, "codec");
        this.streamType = streamType;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.formatBits = formatBits;
        this.codec = codec;
        this.samplesPerPacket = samplesPerPacket;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ SenderAudioFormat(int i, int i2, int i3, long j, AudioCodecKind audioCodecKind, int i4, int i5, DefaultConstructorMarker defaultConstructorMarker) {
        AudioCodecKind audioCodecKind2;
        int i6;
        if ((i5 & 16) == 0) {
            audioCodecKind2 = audioCodecKind;
        } else {
            audioCodecKind2 = AudioCodecKind.AAC_LC;
        }
        if ((i5 & 32) == 0) {
            i6 = i4;
        } else {
            i6 = 1024;
        }
        this(i, i2, i3, j, audioCodecKind2, i6);
    }

    public final int getStreamType() {
        return this.streamType;
    }

    public final int getSampleRate() {
        return this.sampleRate;
    }

    public final int getChannels() {
        return this.channels;
    }

    public final long getFormatBits() {
        return this.formatBits;
    }

    public final AudioCodecKind getCodec() {
        return this.codec;
    }

    public final int getSamplesPerPacket() {
        return this.samplesPerPacket;
    }

    public final boolean isPcm() {
        return this.codec == AudioCodecKind.LPCM;
    }

    public final String getKey() {
        return this.streamType + "|" + (isPcm() ? "PCM" : "AAC") + "|" + this.sampleRate + "|" + this.formatBits + "|" + this.channels + "|" + this.samplesPerPacket;
    }

    public final int getPcmPacketBytes() {
        return this.samplesPerPacket * this.channels * 2;
    }

    public final int rtpTimestamp(long ptsUs) {
        if (!(ptsUs >= 0)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        long seconds = (ptsUs / 1000000) % 4294967296L;
        long fractional = (((ptsUs % 1000000) * ((long) this.sampleRate)) + 500000) / 1000000;
        return (int) ((((long) this.sampleRate) * seconds) + fractional);
    }
}
