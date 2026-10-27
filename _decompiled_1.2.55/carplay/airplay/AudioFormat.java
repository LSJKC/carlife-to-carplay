package com.carplay.airplay;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AudioStream.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B1\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\t¢\u0006\u0004\b\n\u0010\u000bJ\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0018\u001a\u00020\tHÆ\u0003J;\u0010\u0019\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\tHÆ\u0001J\u0013\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001d\u001a\u00020\u0005HÖ\u0001J\t\u0010\u001e\u001a\u00020\tHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0011\u0010\u0007\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000fR\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013¨\u0006\u001f"}, d2 = {"Lcom/carplay/airplay/AudioFormat;", "", "codec", "Lcom/carplay/airplay/AudioCodecKind;", "sampleRate", "", "channels", "payloadType", "audioType", "", "<init>", "(Lcom/carplay/airplay/AudioCodecKind;IIILjava/lang/String;)V", "getCodec", "()Lcom/carplay/airplay/AudioCodecKind;", "getSampleRate", "()I", "getChannels", "getPayloadType", "getAudioType", "()Ljava/lang/String;", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class AudioFormat {
    private final String audioType;
    private final int channels;
    private final AudioCodecKind codec;
    private final int payloadType;
    private final int sampleRate;

    public static /* synthetic */ AudioFormat copy$default(AudioFormat audioFormat, AudioCodecKind audioCodecKind, int i, int i2, int i3, String str, int i4, Object obj) {
        if ((i4 & 1) != 0) {
            audioCodecKind = audioFormat.codec;
        }
        if ((i4 & 2) != 0) {
            i = audioFormat.sampleRate;
        }
        if ((i4 & 4) != 0) {
            i2 = audioFormat.channels;
        }
        if ((i4 & 8) != 0) {
            i3 = audioFormat.payloadType;
        }
        if ((i4 & 16) != 0) {
            str = audioFormat.audioType;
        }
        String str2 = str;
        int i5 = i2;
        return audioFormat.copy(audioCodecKind, i, i5, i3, str2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final AudioCodecKind getCodec() {
        return this.codec;
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
    public final String getAudioType() {
        return this.audioType;
    }

    public final AudioFormat copy(AudioCodecKind codec, int sampleRate, int channels, int payloadType, String audioType) {
        Intrinsics.checkNotNullParameter(codec, "codec");
        Intrinsics.checkNotNullParameter(audioType, "audioType");
        return new AudioFormat(codec, sampleRate, channels, payloadType, audioType);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AudioFormat)) {
            return false;
        }
        AudioFormat audioFormat = (AudioFormat) other;
        return this.codec == audioFormat.codec && this.sampleRate == audioFormat.sampleRate && this.channels == audioFormat.channels && this.payloadType == audioFormat.payloadType && Intrinsics.areEqual(this.audioType, audioFormat.audioType);
    }

    public int hashCode() {
        return (((((((this.codec.hashCode() * 31) + Integer.hashCode(this.sampleRate)) * 31) + Integer.hashCode(this.channels)) * 31) + Integer.hashCode(this.payloadType)) * 31) + this.audioType.hashCode();
    }

    public String toString() {
        return "AudioFormat(codec=" + this.codec + ", sampleRate=" + this.sampleRate + ", channels=" + this.channels + ", payloadType=" + this.payloadType + ", audioType=" + this.audioType + ")";
    }

    public AudioFormat(AudioCodecKind codec, int sampleRate, int channels, int payloadType, String audioType) {
        Intrinsics.checkNotNullParameter(codec, "codec");
        Intrinsics.checkNotNullParameter(audioType, "audioType");
        this.codec = codec;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.payloadType = payloadType;
        this.audioType = audioType;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ AudioFormat(AudioCodecKind audioCodecKind, int i, int i2, int i3, String str, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        String str2;
        if ((i4 & 16) == 0) {
            str2 = str;
        } else {
            str2 = "media";
        }
        this(audioCodecKind, i, i2, i3, str2);
    }

    public final AudioCodecKind getCodec() {
        return this.codec;
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

    public final String getAudioType() {
        return this.audioType;
    }
}
