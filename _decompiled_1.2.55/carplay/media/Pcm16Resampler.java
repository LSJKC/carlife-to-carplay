package com.carplay.media;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.CollectionsKt;
import kotlin.internal.ProgressionUtilKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Pcm16Resampler.kt */
/* JADX INFO: loaded from: classes9.dex */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003¢\u0006\u0004\b\u0006\u0010\u0007J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lcom/carplay/media/Pcm16Resampler;", "", "inputRate", "", "outputRate", "channels", "<init>", "(III)V", "inputFrames", "", "nextOutput", "previous", "", "convert", "", "data", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Pcm16Resampler {
    private final int channels;
    private long inputFrames;
    private final int inputRate;
    private long nextOutput;
    private final int outputRate;
    private int[] previous;

    public Pcm16Resampler(int inputRate, int outputRate, int channels) {
        this.inputRate = inputRate;
        this.outputRate = outputRate;
        this.channels = channels;
        boolean z = false;
        if (CollectionsKt.listOf((Object[]) new Integer[]{44100, 48000}).contains(Integer.valueOf(this.inputRate)) && CollectionsKt.listOf((Object[]) new Integer[]{44100, 48000}).contains(Integer.valueOf(this.outputRate)) && this.channels == 2) {
            z = true;
        }
        if (!z) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        this.previous = new int[this.channels];
    }

    public /* synthetic */ Pcm16Resampler(int i, int i2, int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, i2, (i4 & 4) != 0 ? 2 : i3);
    }

    public final byte[] convert(byte[] data) {
        ByteArrayOutputStream output;
        int i;
        long j;
        long fraction;
        int channel;
        int value;
        byte[] data2 = data;
        Intrinsics.checkNotNullParameter(data2, "data");
        int i2 = 0;
        if (!(data2.length % (this.channels * 2) == 0)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (this.inputRate == this.outputRate) {
            byte[] bArrCopyOf = Arrays.copyOf(data2, data2.length);
            Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
            return bArrCopyOf;
        }
        long capacity = ((((long) data2.length) * ((long) this.outputRate)) / ((long) this.inputRate)) + ((long) (this.channels * 2));
        if (!(capacity <= 2147483647L)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        ByteArrayOutputStream output2 = new ByteArrayOutputStream((int) capacity);
        int length = data2.length - 1;
        int i3 = this.channels * 2;
        if (i3 <= 0) {
            throw new IllegalArgumentException("Step must be positive, was: " + i3 + ".");
        }
        int offset = 0;
        int progressionLastElement = ProgressionUtilKt.getProgressionLastElement(0, length, i3);
        if (0 <= progressionLastElement) {
            while (true) {
                int i4 = this.channels;
                int[] current = new int[i4];
                for (int i5 = i2; i5 < i4; i5++) {
                    int at = (i5 * 2) + offset;
                    current[i5] = (short) ((data2[at] & UByte.MAX_VALUE) | (data2[at + 1] << 8));
                }
                long index = this.inputFrames;
                this.inputFrames = index + 1;
                while (true) {
                    i = progressionLastElement;
                    if (this.nextOutput > ((long) this.outputRate) * index) {
                        break;
                    }
                    if (index == 0) {
                        fraction = 0;
                        j = 0;
                    } else {
                        j = 0;
                        fraction = this.nextOutput - ((index - 1) * ((long) this.outputRate));
                    }
                    int channel2 = 0;
                    for (int i6 = this.channels; channel2 < i6; i6 = i6) {
                        if (index == j) {
                            value = current[channel2];
                            channel = channel2;
                        } else {
                            channel = channel2;
                            value = (int) ((((((long) this.outputRate) - fraction) * ((long) this.previous[channel2])) + (((long) current[channel]) * fraction)) / ((long) this.outputRate));
                        }
                        output2.write(value & 255);
                        output2.write((value >> 8) & 255);
                        channel2 = channel + 1;
                    }
                    this.nextOutput += (long) this.inputRate;
                    progressionLastElement = i;
                    output2 = output2;
                }
                output = output2;
                this.previous = current;
                if (offset == i) {
                    break;
                }
                offset += i3;
                progressionLastElement = i;
                output2 = output;
                i2 = 0;
                data2 = data;
            }
        } else {
            output = output2;
        }
        byte[] byteArray = output.toByteArray();
        Intrinsics.checkNotNullExpressionValue(byteArray, "toByteArray(...)");
        return byteArray;
    }
}
