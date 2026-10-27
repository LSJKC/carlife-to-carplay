package com.carplay.adapter.carlife;

import android.content.Context;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;

/* JADX INFO: compiled from: CarLifeVideoProcessor.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\u0092\u0001\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00030\u000b2\u001e\u0010\r\u001a\u001a\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00030\u000e2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00030\u000b2\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00030\u000b2\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00030\u0016H&J(\u0010\u0017\u001a\u00020\u00032\u0006\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u0019\u001a\u00020\u00072\u0006\u0010\u001a\u001a\u00020\u00072\u0006\u0010\u001b\u001a\u00020\fH&J \u0010\u001c\u001a\u00020\u00032\u0006\u0010\u001d\u001a\u00020\f2\u0006\u0010\u001e\u001a\u00020\u000f2\u0006\u0010\u001f\u001a\u00020\u0010H&J&\u0010 \u001a\u0010\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\"\u0018\u00010!2\u0006\u0010#\u001a\u00020\"2\u0006\u0010$\u001a\u00020\"H&J\b\u0010%\u001a\u00020\u0003H&¨\u0006&À\u0006\u0003"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeVideoProcessor;", "", "start", "", "context", "Landroid/content/Context;", "outputWidth", "", "outputHeight", "outputFps", "onConfig", "Lkotlin/Function1;", "", "onFrame", "Lkotlin/Function3;", "", "", "onDiagnostic", "", "onFailure", "", "onNeedKeyFrame", "Lkotlin/Function0;", "configureInput", "width", "height", "fps", "avcC", "offerFrame", "accessUnit", "ptsUs", "keyFrame", "mapTouchToSource", "Lkotlin/Pair;", "", "panelX", "panelY", "stop", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface CarLifeVideoProcessor {
    void configureInput(int width, int height, int fps, byte[] avcC);

    Pair<Float, Float> mapTouchToSource(float panelX, float panelY);

    void offerFrame(byte[] accessUnit, long ptsUs, boolean keyFrame);

    void start(Context context, int outputWidth, int outputHeight, int outputFps, Function1<? super byte[], Unit> onConfig, Function3<? super byte[], ? super Long, ? super Boolean, Unit> onFrame, Function1<? super String, Unit> onDiagnostic, Function1<? super Throwable, Unit> onFailure, Function0<Unit> onNeedKeyFrame);

    void stop();
}
