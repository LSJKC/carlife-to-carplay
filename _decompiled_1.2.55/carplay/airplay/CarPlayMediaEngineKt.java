package com.carplay.airplay;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CarPlayMediaEngine.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000D\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\u001a@\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00020\u00062\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00020\u0006H\u0000\u001a&\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010H\u0000\u001a\u0014\u0010\u0012\u001a\u0004\u0018\u00010\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0000\u001a\u0012\u0010\u0016\u001a\u00020\u00152\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0000¨\u0006\u0017"}, d2 = {"audioKeysForSession", "", "Lcom/carplay/airplay/CarPlayMediaEngine$StreamKey;", "session", "Lcom/carplay/airplay/AirPlaySession;", "audioMetaKeys", "", "pendingMicrophoneKeys", "audioCaptureKeys", "currentScreenListener", "Lcom/carplay/airplay/ScreenStream$Listener;", "type", "", "sink", "Lcom/carplay/airplay/MediaSink;", "isCurrent", "Lkotlin/Function0;", "", "unsignedPlistDecimal", "", "value", "", "unsignedPlistInteger", "core-airplay"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class CarPlayMediaEngineKt {
    public static final Set<CarPlayMediaEngine.StreamKey> audioKeysForSession(AirPlaySession session, Collection<CarPlayMediaEngine.StreamKey> audioMetaKeys, Collection<CarPlayMediaEngine.StreamKey> pendingMicrophoneKeys, Collection<CarPlayMediaEngine.StreamKey> audioCaptureKeys) {
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(audioMetaKeys, "audioMetaKeys");
        Intrinsics.checkNotNullParameter(pendingMicrophoneKeys, "pendingMicrophoneKeys");
        Intrinsics.checkNotNullParameter(audioCaptureKeys, "audioCaptureKeys");
        Set $this$audioKeysForSession_u24lambda_u243 = SetsKt.createSetBuilder();
        Collection<CarPlayMediaEngine.StreamKey> $this$filter$iv = audioMetaKeys;
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv : $this$filter$iv) {
            CarPlayMediaEngine.StreamKey it = (CarPlayMediaEngine.StreamKey) element$iv$iv;
            if (it.getSession() == session) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        $this$audioKeysForSession_u24lambda_u243.addAll((List) destination$iv$iv);
        Collection<CarPlayMediaEngine.StreamKey> $this$filter$iv2 = pendingMicrophoneKeys;
        Collection destination$iv$iv2 = new ArrayList();
        for (Object element$iv$iv2 : $this$filter$iv2) {
            CarPlayMediaEngine.StreamKey it2 = (CarPlayMediaEngine.StreamKey) element$iv$iv2;
            if (it2.getSession() == session) {
                destination$iv$iv2.add(element$iv$iv2);
            }
        }
        $this$audioKeysForSession_u24lambda_u243.addAll((List) destination$iv$iv2);
        Collection<CarPlayMediaEngine.StreamKey> $this$filter$iv3 = audioCaptureKeys;
        Collection destination$iv$iv3 = new ArrayList();
        for (Object element$iv$iv3 : $this$filter$iv3) {
            CarPlayMediaEngine.StreamKey it3 = (CarPlayMediaEngine.StreamKey) element$iv$iv3;
            if (it3.getSession() == session) {
                destination$iv$iv3.add(element$iv$iv3);
            }
        }
        $this$audioKeysForSession_u24lambda_u243.addAll((List) destination$iv$iv3);
        return SetsKt.build($this$audioKeysForSession_u24lambda_u243);
    }

    public static final ScreenStream.Listener currentScreenListener(final int type, final MediaSink sink, final Function0<Boolean> isCurrent) {
        Intrinsics.checkNotNullParameter(sink, "sink");
        Intrinsics.checkNotNullParameter(isCurrent, "isCurrent");
        return new ScreenStream.Listener() { // from class: com.carplay.airplay.CarPlayMediaEngineKt.currentScreenListener.1
            @Override // com.carplay.airplay.ScreenStream.Listener
            public void onClosed(Throwable cause) {
                super.onClosed(cause);
            }

            @Override // com.carplay.airplay.ScreenStream.Listener
            public void onCodec(VideoCodec codec) {
                Intrinsics.checkNotNullParameter(codec, "codec");
                if (isCurrent.invoke().booleanValue()) {
                    sink.onVideoCodec(type, codec);
                }
            }

            @Override // com.carplay.airplay.ScreenStream.Listener
            public void onConfig(byte[] codecData) {
                Intrinsics.checkNotNullParameter(codecData, "codecData");
                if (isCurrent.invoke().booleanValue()) {
                    sink.onVideoConfig(type, codecData);
                }
            }

            @Override // com.carplay.airplay.ScreenStream.Listener
            public void onFrame(byte[] naluBytes) {
                Intrinsics.checkNotNullParameter(naluBytes, "naluBytes");
                if (isCurrent.invoke().booleanValue()) {
                    sink.onVideoFrame(type, naluBytes);
                }
            }

            @Override // com.carplay.airplay.ScreenStream.Listener
            public void onFrame(byte[] naluBytes, long senderNanos, long arrivalNanos) {
                Intrinsics.checkNotNullParameter(naluBytes, "naluBytes");
                if (isCurrent.invoke().booleanValue()) {
                    sink.onVideoFrame(type, naluBytes, senderNanos, arrivalNanos);
                }
            }
        };
    }

    public static final String unsignedPlistDecimal(Object value) {
        if (value instanceof Long) {
            return Long.toUnsignedString(((Number) value).longValue());
        }
        if (value instanceof Integer) {
            return Integer.toUnsignedString(((Number) value).intValue());
        }
        if (value instanceof Short) {
            return String.valueOf(((Number) value).shortValue() & 65535);
        }
        if (value instanceof Byte) {
            return String.valueOf(((Number) value).byteValue() & UByte.MAX_VALUE);
        }
        if (value instanceof BigInteger) {
            if (((BigInteger) value).signum() >= 0) {
                return ((BigInteger) value).toString();
            }
            return null;
        }
        Number number = value instanceof Number ? (Number) value : null;
        if (number == null) {
            return null;
        }
        long p0 = number.longValue();
        return Long.toUnsignedString(p0);
    }

    public static final Object unsignedPlistInteger(Object value) {
        if (value instanceof Long) {
            if (((Number) value).longValue() < 0) {
                return new BigInteger(Long.toUnsignedString(((Number) value).longValue()));
            }
        } else if (value instanceof Integer) {
            if (((Number) value).intValue() < 0) {
                return new BigInteger(Integer.toUnsignedString(((Number) value).intValue()));
            }
        } else if (value == null) {
            return 0L;
        }
        return value;
    }
}
