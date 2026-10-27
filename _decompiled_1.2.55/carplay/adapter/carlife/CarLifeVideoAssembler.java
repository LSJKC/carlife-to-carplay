package com.carplay.adapter.carlife;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CarLifeVideoAssembler.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\b\u0000\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u0007J \u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0007R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0019"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeVideoAssembler;", "", "<init>", "()V", "configCollector", "Lcom/carplay/adapter/carlife/CarLifeAvcConfigCollector;", "configSent", "", "lastAvcC", "", "pendingFrames", "", "Lcom/carplay/adapter/carlife/CarLifeVideoFrame;", "pendingOverflowed", "awaitingKeyFrame", "reset", "", "preserveConfig", "offer", "Lcom/carplay/adapter/carlife/CarLifeVideoBatch;", "data", "timestampSeconds", "", "keyFrame", "Companion", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarLifeVideoAssembler {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int MAX_PENDING_FRAMES = 32;
    private boolean awaitingKeyFrame;
    private boolean configSent;
    private byte[] lastAvcC;
    private boolean pendingOverflowed;
    private final CarLifeAvcConfigCollector configCollector = new CarLifeAvcConfigCollector();
    private final List<CarLifeVideoFrame> pendingFrames = new ArrayList();

    public static /* synthetic */ void reset$default(CarLifeVideoAssembler carLifeVideoAssembler, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        carLifeVideoAssembler.reset(z);
    }

    public final synchronized void reset(boolean preserveConfig) {
        this.configCollector.reset();
        if (!preserveConfig) {
            this.lastAvcC = null;
        }
        this.configSent = false;
        this.pendingFrames.clear();
        this.pendingOverflowed = false;
        this.awaitingKeyFrame = false;
    }

    public final synchronized CarLifeVideoBatch offer(byte[] data, long timestampSeconds, boolean keyFrame) {
        byte[] bArr;
        CarLifeVideoFrame it;
        Intrinsics.checkNotNullParameter(data, "data");
        byte[] picture = CarLifeProto.INSTANCE.pictureAccessUnit(data);
        CarLifeVideoBatch carLifeVideoBatch = null;
        CarLifeVideoFrame frame = picture != null ? new CarLifeVideoFrame(picture, timestampSeconds, keyFrame) : null;
        boolean z = false;
        if (this.configSent) {
            if (this.awaitingKeyFrame && !keyFrame) {
                return null;
            }
            if (keyFrame) {
                this.awaitingKeyFrame = false;
            }
            if (frame != null) {
                CarLifeVideoFrame it2 = frame;
                carLifeVideoBatch = new CarLifeVideoBatch(null, CollectionsKt.listOf(it2), false, 4, null);
            }
            return carLifeVideoBatch;
        }
        byte[] collectedAvcC = this.configCollector.offer(data);
        if (collectedAvcC != null) {
            byte[] bArrCopyOf = Arrays.copyOf(collectedAvcC, collectedAvcC.length);
            Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
            this.lastAvcC = bArrCopyOf;
        }
        boolean reusedAvcC = collectedAvcC == null && keyFrame && this.lastAvcC != null;
        if (collectedAvcC == null) {
            bArr = reusedAvcC ? this.lastAvcC : null;
        } else {
            bArr = collectedAvcC;
        }
        byte[] avcC = bArr;
        if (keyFrame) {
            this.pendingFrames.clear();
            this.pendingOverflowed = false;
        }
        if (frame != null && !this.pendingOverflowed && (keyFrame || !this.pendingFrames.isEmpty())) {
            int size = this.pendingFrames.size();
            List<CarLifeVideoFrame> list = this.pendingFrames;
            if (size >= 32) {
                list.clear();
                this.pendingOverflowed = true;
            } else {
                list.add(frame);
            }
        }
        if (avcC == null) {
            return null;
        }
        this.configSent = true;
        if (this.pendingOverflowed) {
            this.pendingOverflowed = false;
            this.awaitingKeyFrame = true;
            return new CarLifeVideoBatch(avcC, CollectionsKt.emptyList(), false, 4, null);
        }
        List frames = CollectionsKt.toList(this.pendingFrames);
        this.pendingFrames.clear();
        List $this$none$iv = frames;
        if (($this$none$iv instanceof Collection) && $this$none$iv.isEmpty()) {
            z = true;
        } else {
            Iterator it3 = $this$none$iv.iterator();
            do {
                if (!it3.hasNext()) {
                    z = true;
                    break;
                }
                Object element$iv = it3.next();
                it = (CarLifeVideoFrame) element$iv;
            } while (!it.getKeyFrame());
        }
        if (z) {
            this.awaitingKeyFrame = true;
        }
        return new CarLifeVideoBatch(avcC, frames, reusedAvcC);
    }

    /* JADX INFO: compiled from: CarLifeVideoAssembler.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeVideoAssembler$Companion;", "", "<init>", "()V", "MAX_PENDING_FRAMES", "", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
