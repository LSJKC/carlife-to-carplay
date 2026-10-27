package com.carplay.airplay;

import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: AudioPacketCapture.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\u0003\n\u0002\b\u0004\u0018\u0000 \u001e2\u00020\u0001:\u0001\u001eB+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b¢\u0006\u0004\b\t\u0010\nJ1\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u00172\b\u0010\u0019\u001a\u0004\u0018\u00010\u00052\b\u0010\u001a\u001a\u0004\u0018\u00010\u001b¢\u0006\u0002\u0010\u001cJ\b\u0010\u001d\u001a\u00020\u0015H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0011\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013¨\u0006\u001f"}, d2 = {"Lcom/carplay/airplay/AudioPacketCapture;", "Ljava/io/Closeable;", "directory", "Ljava/io/File;", "streamType", "", "maxPackets", "maxBytes", "", "<init>", "(Ljava/io/File;IIJ)V", "closed", "Ljava/util/concurrent/atomic/AtomicBoolean;", "packets", "bytesWritten", "output", "Ljava/io/DataOutputStream;", "file", "getFile", "()Ljava/io/File;", "record", "", "wire", "", "rtp", "sample", "error", "", "([B[BLjava/lang/Integer;Ljava/lang/Throwable;)V", "close", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AudioPacketCapture implements Closeable {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int FLUSH_INTERVAL_PACKETS = 16;

    @Deprecated
    public static final int HEADER_BYTES = 16;
    private static final byte[] MAGIC;

    @Deprecated
    public static final String TAG = "xcertplay-usb";

    @Deprecated
    public static final int VERSION = 1;
    private long bytesWritten;
    private final AtomicBoolean closed;
    private final File file;
    private final long maxBytes;
    private final int maxPackets;
    private final DataOutputStream output;
    private int packets;
    private final int streamType;

    public AudioPacketCapture(File directory, int streamType, int maxPackets, long maxBytes) {
        File file;
        File file2;
        DataOutputStream dataOutputStream;
        Intrinsics.checkNotNullParameter(directory, "directory");
        this.streamType = streamType;
        this.maxPackets = maxPackets;
        this.maxBytes = maxBytes;
        this.closed = new AtomicBoolean(false);
        this.bytesWritten = MAGIC.length + 16;
        if (directory.isDirectory() || directory.mkdirs()) {
            file = directory;
        } else {
            file = null;
        }
        File captureDirectory = file;
        if (captureDirectory == null) {
            file2 = new File(directory, "audio-capture-unavailable-type-" + this.streamType + ".xcap");
        } else {
            file2 = new File(captureDirectory, "audio-" + System.currentTimeMillis() + "-type-" + this.streamType + ".xcap");
        }
        this.file = file2;
        if (captureDirectory == null) {
            dataOutputStream = null;
        } else {
            try {
                DataOutputStream it = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(this.file, false)));
                it.write(MAGIC);
                it.writeInt(1);
                it.writeInt(this.streamType);
                it.writeLong(System.currentTimeMillis());
                it.flush();
                Log.i("xcertplay-usb", "audio capture started type=" + this.streamType + " file=" + this.file.getAbsolutePath());
                dataOutputStream = it;
            } catch (Exception error) {
                Log.w("xcertplay-usb", "audio capture could not open type=" + this.streamType, error);
                dataOutputStream = null;
            }
        }
        this.output = dataOutputStream;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ AudioPacketCapture(File file, int i, int i2, long j, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        int i4;
        long j2;
        if ((i3 & 4) == 0) {
            i4 = i2;
        } else {
            i4 = 512;
        }
        if ((i3 & 8) == 0) {
            j2 = j;
        } else {
            j2 = 16777216;
        }
        this(file, i, i4, j2);
    }

    public final File getFile() {
        return this.file;
    }

    /* JADX WARN: Code duplicated, block: B:20:0x003b A[Catch: all -> 0x00de, TryCatch #1 {, blocks: (B:9:0x0015, B:11:0x001d, B:16:0x0028, B:18:0x002e, B:21:0x003d, B:23:0x0044, B:24:0x0045, B:26:0x0053, B:30:0x005a, B:32:0x0065, B:34:0x0068, B:36:0x006f, B:37:0x0073, B:39:0x008c, B:40:0x0091, B:42:0x00a7, B:43:0x00ac, B:45:0x00b2, B:49:0x00d7, B:48:0x00b7, B:20:0x003b), top: B:60:0x0015, inners: #0 }] */
    public final void record(byte[] wire, byte[] rtp, Integer sample, Throwable error) {
        byte[] errorBytes;
        String message;
        Intrinsics.checkNotNullParameter(wire, "wire");
        if (this.output == null || this.closed.get()) {
            return;
        }
        synchronized (this) {
            if (!this.closed.get() && this.packets < this.maxPackets) {
                if (error == null || (message = error.getMessage()) == null) {
                    errorBytes = new byte[0];
                } else {
                    errorBytes = message.getBytes(Charsets.UTF_8);
                    Intrinsics.checkNotNullExpressionValue(errorBytes, "getBytes(...)");
                    if (errorBytes == null) {
                        errorBytes = new byte[0];
                    }
                }
                long recordBytes = ((long) wire.length) + 24 + ((long) (rtp != null ? rtp.length : 0)) + ((long) errorBytes.length);
                if (this.bytesWritten + recordBytes > this.maxBytes) {
                    close();
                    return;
                }
                try {
                    this.output.writeInt(wire.length);
                    this.output.writeInt(rtp != null ? rtp.length : -1);
                    this.output.writeInt(sample != null ? sample.intValue() : -1);
                    this.output.writeLong(System.nanoTime());
                    this.output.writeInt(errorBytes.length);
                    this.output.write(wire);
                    if (rtp != null) {
                        this.output.write(rtp);
                    }
                    this.output.write(errorBytes);
                    this.packets++;
                    this.bytesWritten += recordBytes;
                    if (this.packets % 16 == 0) {
                        this.output.flush();
                    }
                    if (this.packets >= this.maxPackets) {
                        close();
                    }
                } catch (Exception failure) {
                    Log.w("xcertplay-usb", "audio capture write failed type=" + this.streamType, failure);
                    close();
                }
                Unit unit = Unit.INSTANCE;
            }
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        int i;
        int i2;
        long j;
        String absolutePath;
        StringBuilder sb;
        try {
            if (this.closed.compareAndSet(false, true)) {
                DataOutputStream dataOutputStream = this.output;
                if (dataOutputStream != null) {
                    dataOutputStream.flush();
                }
                DataOutputStream dataOutputStream2 = this.output;
                if (dataOutputStream2 != null) {
                    dataOutputStream2.close();
                }
                if (this.output != null) {
                    i = this.streamType;
                    i2 = this.packets;
                    j = this.bytesWritten;
                    absolutePath = this.file.getAbsolutePath();
                    sb = new StringBuilder();
                    StringBuilder sbAppend = sb.append("audio capture stopped type=").append(i).append(" packets=").append(i2).append(" bytes=").append(j).append(" file=").append(absolutePath);
                }
            }
        } catch (Exception error) {
            Log.w("xcertplay-usb", "audio capture close failed type=" + this.streamType, error);
            if (this.output == null) {
                return;
            }
            i = this.streamType;
            i2 = this.packets;
            j = this.bytesWritten;
            absolutePath = this.file.getAbsolutePath();
            sb = new StringBuilder();
        } finally {
            if (this.output != null) {
                Log.i("xcertplay-usb", "audio capture stopped type=" + this.streamType + " packets=" + this.packets + " bytes=" + this.bytesWritten + " file=" + this.file.getAbsolutePath());
            }
        }
    }

    /* JADX INFO: compiled from: AudioPacketCapture.kt */
    @Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0003\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0086T¢\u0006\u0002\n\u0000R\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\r¨\u0006\u000e"}, d2 = {"Lcom/carplay/airplay/AudioPacketCapture$Companion;", "", "<init>", "()V", "TAG", "", "VERSION", "", "HEADER_BYTES", "FLUSH_INTERVAL_PACKETS", "MAGIC", "", "getMAGIC", "()[B", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final byte[] getMAGIC() {
            return AudioPacketCapture.MAGIC;
        }
    }

    static {
        byte[] bytes = "XCAUDIO1".getBytes(Charsets.US_ASCII);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        MAGIC = bytes;
    }
}
