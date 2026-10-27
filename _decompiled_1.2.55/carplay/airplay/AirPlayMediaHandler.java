package com.carplay.airplay;

import com.carplay.iap2.transport.BlockingDuplexByteStream;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AirPlaySession.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J5\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00032\u0014\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0006\u0012\u0004\u0018\u00010\u00010\bH\u0016¢\u0006\u0002\u0010\nJ>\u0010\u000b\u001a\u0012\u0012\u0004\u0012\u00020\t\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0018\u00010\b2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00032\u0014\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0006\u0012\u0004\u0018\u00010\u00010\bH\u0016J6\u0010\f\u001a\u0012\u0012\u0004\u0012\u00020\t\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0018\u00010\b2\u0006\u0010\u0004\u001a\u00020\u00052\u0014\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0006\u0012\u0004\u0018\u00010\u00010\bH\u0016J6\u0010\r\u001a\u0012\u0012\u0004\u0012\u00020\t\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0018\u00010\b2\u0006\u0010\u0004\u001a\u00020\u00052\u0014\u0010\u0007\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0006\u0012\u0004\u0018\u00010\u00010\bH\u0016J>\u0010\u000e\u001a\u0012\u0012\u0004\u0012\u00020\t\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0018\u00010\b2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\t2\u0014\u0010\u0010\u001a\u0010\u0012\u0004\u0012\u00020\t\u0012\u0006\u0012\u0004\u0018\u00010\u00010\bH\u0016J \u0010\u0011\u001a\u0012\u0012\u0004\u0012\u00020\t\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0018\u00010\b2\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0018\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0003H\u0016J\u0010\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u001e\u0010\u0015\u001a\u00020\u00132\u0014\u0010\u0016\u001a\u0010\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u0019\u0018\u00010\u0017H\u0016J\u0010\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u001bÀ\u0006\u0003"}, d2 = {"Lcom/carplay/airplay/AirPlayMediaHandler;", "", "onScreen", "", "session", "Lcom/carplay/airplay/AirPlaySession;", "type", "stream", "", "", "(Lcom/carplay/airplay/AirPlaySession;ILjava/util/Map;)Ljava/lang/Integer;", "onAudio", "onDataStream", "onBufferedAudio", "onBufferedAudioControl", "method", "body", "onFeedback", "onTeardown", "", "onSessionClosed", "setIapTunnelHandler", "handler", "Lkotlin/Function1;", "Lcom/carplay/iap2/transport/BlockingDuplexByteStream;", "", "onSetupResponseSent", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface AirPlayMediaHandler {

    /* JADX INFO: compiled from: AirPlaySession.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final class DefaultImpls {
        @Deprecated
        public static Integer onScreen(AirPlayMediaHandler $this, AirPlaySession session, int type, Map<String, ? extends Object> stream) {
            Intrinsics.checkNotNullParameter(session, "session");
            Intrinsics.checkNotNullParameter(stream, "stream");
            return AirPlayMediaHandler.super.onScreen(session, type, stream);
        }

        @Deprecated
        public static Map<String, Object> onAudio(AirPlayMediaHandler $this, AirPlaySession session, int type, Map<String, ? extends Object> stream) {
            Intrinsics.checkNotNullParameter(session, "session");
            Intrinsics.checkNotNullParameter(stream, "stream");
            return AirPlayMediaHandler.super.onAudio(session, type, stream);
        }

        @Deprecated
        public static Map<String, Object> onDataStream(AirPlayMediaHandler $this, AirPlaySession session, Map<String, ? extends Object> stream) {
            Intrinsics.checkNotNullParameter(session, "session");
            Intrinsics.checkNotNullParameter(stream, "stream");
            return AirPlayMediaHandler.super.onDataStream(session, stream);
        }

        @Deprecated
        public static Map<String, Object> onBufferedAudio(AirPlayMediaHandler $this, AirPlaySession session, Map<String, ? extends Object> stream) {
            Intrinsics.checkNotNullParameter(session, "session");
            Intrinsics.checkNotNullParameter(stream, "stream");
            return AirPlayMediaHandler.super.onBufferedAudio(session, stream);
        }

        @Deprecated
        public static Map<String, Object> onBufferedAudioControl(AirPlayMediaHandler $this, AirPlaySession session, String method, Map<String, ? extends Object> body) {
            Intrinsics.checkNotNullParameter(session, "session");
            Intrinsics.checkNotNullParameter(method, "method");
            Intrinsics.checkNotNullParameter(body, "body");
            return AirPlayMediaHandler.super.onBufferedAudioControl(session, method, body);
        }

        @Deprecated
        public static Map<String, Object> onFeedback(AirPlayMediaHandler $this, AirPlaySession session) {
            Intrinsics.checkNotNullParameter(session, "session");
            return AirPlayMediaHandler.super.onFeedback(session);
        }

        @Deprecated
        public static void onTeardown(AirPlayMediaHandler $this, AirPlaySession session, int type) {
            Intrinsics.checkNotNullParameter(session, "session");
            AirPlayMediaHandler.super.onTeardown(session, type);
        }

        @Deprecated
        public static void onSessionClosed(AirPlayMediaHandler $this, AirPlaySession session) {
            Intrinsics.checkNotNullParameter(session, "session");
            AirPlayMediaHandler.super.onSessionClosed(session);
        }

        @Deprecated
        public static void setIapTunnelHandler(AirPlayMediaHandler $this, Function1<? super BlockingDuplexByteStream, Boolean> function1) {
            AirPlayMediaHandler.super.setIapTunnelHandler(function1);
        }

        @Deprecated
        public static void onSetupResponseSent(AirPlayMediaHandler $this, AirPlaySession session) {
            Intrinsics.checkNotNullParameter(session, "session");
            AirPlayMediaHandler.super.onSetupResponseSent(session);
        }
    }

    default Integer onScreen(AirPlaySession session, int type, Map<String, ? extends Object> stream) {
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(stream, "stream");
        return null;
    }

    default Map<String, Object> onAudio(AirPlaySession session, int type, Map<String, ? extends Object> stream) {
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(stream, "stream");
        return null;
    }

    default Map<String, Object> onDataStream(AirPlaySession session, Map<String, ? extends Object> stream) {
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(stream, "stream");
        return null;
    }

    default Map<String, Object> onBufferedAudio(AirPlaySession session, Map<String, ? extends Object> stream) {
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(stream, "stream");
        return null;
    }

    default Map<String, Object> onBufferedAudioControl(AirPlaySession session, String method, Map<String, ? extends Object> body) {
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(method, "method");
        Intrinsics.checkNotNullParameter(body, "body");
        return null;
    }

    default Map<String, Object> onFeedback(AirPlaySession session) {
        Intrinsics.checkNotNullParameter(session, "session");
        return null;
    }

    default void onTeardown(AirPlaySession session, int type) {
        Intrinsics.checkNotNullParameter(session, "session");
    }

    default void onSessionClosed(AirPlaySession session) {
        Intrinsics.checkNotNullParameter(session, "session");
    }

    default void setIapTunnelHandler(Function1<? super BlockingDuplexByteStream, Boolean> handler) {
    }

    default void onSetupResponseSent(AirPlaySession session) {
        Intrinsics.checkNotNullParameter(session, "session");
    }
}
