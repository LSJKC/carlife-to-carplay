package com.carplay.airplay;

import androidx.core.app.NotificationCompat;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AirPlaySession.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\n\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\rH\u0016J\u0018\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0010\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0016J.\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\r2\u0014\u0010\u0014\u001a\u0010\u0012\u0004\u0012\u00020\r\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0015H\u0016J.\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\u00182\u0014\u0010\f\u001a\u0010\u0012\u0004\u0012\u00020\r\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0015H\u0016J\u0010\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\u001a\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\rH\u0016¨\u0006\u001bÀ\u0006\u0003"}, d2 = {"Lcom/carplay/airplay/AirPlaySessionListener;", "", "onTcpAccepted", "", NotificationCompat.CATEGORY_EVENT, "Lcom/carplay/airplay/AirPlayTcpAccepted;", "onSessionActive", "session", "Lcom/carplay/airplay/AirPlaySession;", "onSessionEnded", "onVideoFrameRendered", "onTransportError", "message", "", "onDeviceInfo", "info", "Lcom/carplay/airplay/AirPlayDeviceInfo;", "onHostUiRequested", "onCommand", "type", "params", "", "onRemoteControlMessage", "streamId", "", "onVideoPlaybackUiRequested", "onDebugLog", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public interface AirPlaySessionListener {

    /* JADX INFO: compiled from: AirPlaySession.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final class DefaultImpls {
        @Deprecated
        public static void onTcpAccepted(AirPlaySessionListener $this, AirPlayTcpAccepted event) {
            Intrinsics.checkNotNullParameter(event, "event");
            AirPlaySessionListener.super.onTcpAccepted(event);
        }

        @Deprecated
        public static void onSessionActive(AirPlaySessionListener $this, AirPlaySession session) {
            Intrinsics.checkNotNullParameter(session, "session");
            AirPlaySessionListener.super.onSessionActive(session);
        }

        @Deprecated
        public static void onSessionEnded(AirPlaySessionListener $this, AirPlaySession session) {
            Intrinsics.checkNotNullParameter(session, "session");
            AirPlaySessionListener.super.onSessionEnded(session);
        }

        @Deprecated
        public static void onVideoFrameRendered(AirPlaySessionListener $this, AirPlaySession session) {
            Intrinsics.checkNotNullParameter(session, "session");
            AirPlaySessionListener.super.onVideoFrameRendered(session);
        }

        @Deprecated
        public static void onTransportError(AirPlaySessionListener $this, String message) {
            Intrinsics.checkNotNullParameter(message, "message");
            AirPlaySessionListener.super.onTransportError(message);
        }

        @Deprecated
        public static void onDeviceInfo(AirPlaySessionListener $this, AirPlaySession session, AirPlayDeviceInfo info) {
            Intrinsics.checkNotNullParameter(session, "session");
            Intrinsics.checkNotNullParameter(info, "info");
            AirPlaySessionListener.super.onDeviceInfo(session, info);
        }

        @Deprecated
        public static void onHostUiRequested(AirPlaySessionListener $this, AirPlaySession session) {
            Intrinsics.checkNotNullParameter(session, "session");
            AirPlaySessionListener.super.onHostUiRequested(session);
        }

        @Deprecated
        public static void onCommand(AirPlaySessionListener $this, AirPlaySession session, String type, Map<String, ? extends Object> params) {
            Intrinsics.checkNotNullParameter(session, "session");
            Intrinsics.checkNotNullParameter(type, "type");
            Intrinsics.checkNotNullParameter(params, "params");
            AirPlaySessionListener.super.onCommand(session, type, params);
        }

        @Deprecated
        public static void onRemoteControlMessage(AirPlaySessionListener $this, AirPlaySession session, long streamId, Map<String, ? extends Object> message) {
            Intrinsics.checkNotNullParameter(session, "session");
            Intrinsics.checkNotNullParameter(message, "message");
            AirPlaySessionListener.super.onRemoteControlMessage(session, streamId, message);
        }

        @Deprecated
        public static void onVideoPlaybackUiRequested(AirPlaySessionListener $this, AirPlaySession session) {
            Intrinsics.checkNotNullParameter(session, "session");
            AirPlaySessionListener.super.onVideoPlaybackUiRequested(session);
        }

        @Deprecated
        public static void onDebugLog(AirPlaySessionListener $this, String message) {
            Intrinsics.checkNotNullParameter(message, "message");
            AirPlaySessionListener.super.onDebugLog(message);
        }
    }

    default void onTcpAccepted(AirPlayTcpAccepted event) {
        Intrinsics.checkNotNullParameter(event, "event");
    }

    default void onSessionActive(AirPlaySession session) {
        Intrinsics.checkNotNullParameter(session, "session");
    }

    default void onSessionEnded(AirPlaySession session) {
        Intrinsics.checkNotNullParameter(session, "session");
    }

    default void onVideoFrameRendered(AirPlaySession session) {
        Intrinsics.checkNotNullParameter(session, "session");
    }

    default void onTransportError(String message) {
        Intrinsics.checkNotNullParameter(message, "message");
    }

    default void onDeviceInfo(AirPlaySession session, AirPlayDeviceInfo info) {
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(info, "info");
    }

    default void onHostUiRequested(AirPlaySession session) {
        Intrinsics.checkNotNullParameter(session, "session");
    }

    default void onCommand(AirPlaySession session, String type, Map<String, ? extends Object> params) {
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(params, "params");
    }

    default void onRemoteControlMessage(AirPlaySession session, long streamId, Map<String, ? extends Object> message) {
        Intrinsics.checkNotNullParameter(session, "session");
        Intrinsics.checkNotNullParameter(message, "message");
    }

    default void onVideoPlaybackUiRequested(AirPlaySession session) {
        Intrinsics.checkNotNullParameter(session, "session");
    }

    default void onDebugLog(String message) {
        Intrinsics.checkNotNullParameter(message, "message");
    }
}
