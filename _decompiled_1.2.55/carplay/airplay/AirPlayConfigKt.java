package com.carplay.airplay;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AirPlayConfig.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\"\u0018\u0010\u0000\u001a\u00020\u0001*\u00020\u00028@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004¨\u0006\u0005"}, d2 = {"bufferedAudioOutputEnabled", "", "Lcom/carplay/airplay/AirPlayConfig;", "getBufferedAudioOutputEnabled", "(Lcom/carplay/airplay/AirPlayConfig;)Z", "core-airplay"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class AirPlayConfigKt {
    public static final boolean getBufferedAudioOutputEnabled(AirPlayConfig $this$bufferedAudioOutputEnabled) {
        Intrinsics.checkNotNullParameter($this$bufferedAudioOutputEnabled, "<this>");
        return $this$bufferedAudioOutputEnabled.getMainBufferedAudio() && !$this$bufferedAudioOutputEnabled.getDisableAudioOutput();
    }
}
