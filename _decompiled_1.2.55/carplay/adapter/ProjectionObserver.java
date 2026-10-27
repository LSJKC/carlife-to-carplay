package com.carplay.adapter;

import kotlin.Metadata;

/* JADX INFO: compiled from: PhoneProjectionAdapter.kt */
/* JADX INFO: loaded from: classes11.dex */
@Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\"\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000bH&J\u0010\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u000eH&J\u0010\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u0011H&¨\u0006\u0012À\u0006\u0003"}, d2 = {"Lcom/carplay/adapter/ProjectionObserver;", "", "videoConfig", "", "avcC", "", "audioFrame", "bytes", "sampleTimeUs", "", "pcm", "", "diagnostic", "message", "", "failed", "error", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface ProjectionObserver {
    void audioFrame(byte[] bytes, long sampleTimeUs, boolean pcm);

    void diagnostic(String message);

    void failed(Throwable error);

    void videoConfig(byte[] avcC);

    /* JADX INFO: compiled from: PhoneProjectionAdapter.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final class DefaultImpls {
    }

    static /* synthetic */ void audioFrame$default(ProjectionObserver projectionObserver, byte[] bArr, long j, boolean z, int i, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: audioFrame");
        }
        if ((i & 4) != 0) {
            z = false;
        }
        projectionObserver.audioFrame(bArr, j, z);
    }
}
