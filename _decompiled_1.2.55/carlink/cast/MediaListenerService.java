package com.carlink.cast;

import android.app.Notification;
import android.content.ComponentName;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import androidx.core.app.NotificationCompat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: MediaListenerService.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\u000bH\u0016J\u0012\u0010\r\u001a\u00020\u000b2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0016J\u0012\u0010\u0010\u001a\u00020\u000b2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0016J\b\u0010\u0011\u001a\u00020\u000bH\u0016J\u0010\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u0007H\u0002J\u0012\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0002R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\t0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0018"}, d2 = {"Lcom/carlink/cast/MediaListenerService;", "Landroid/service/notification/NotificationListenerService;", "<init>", "()V", "controllers", "Ljava/util/concurrent/ConcurrentHashMap;", "", "Landroid/media/session/MediaController;", "callbacks", "Landroid/media/session/MediaController$Callback;", "onListenerConnected", "", "onListenerDisconnected", "onNotificationPosted", "sbn", "Landroid/service/notification/StatusBarNotification;", "onNotificationRemoved", "onDestroy", "attach", "controller", "mediaToken", "Landroid/media/session/MediaSession$Token;", "notification", "Landroid/app/Notification;", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MediaListenerService extends NotificationListenerService {
    private final ConcurrentHashMap<String, MediaController> controllers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, MediaController.Callback> callbacks = new ConcurrentHashMap<>();

    @Override // android.service.notification.NotificationListenerService
    public void onListenerConnected() {
        Object objM159constructorimpl;
        super.onListenerConnected();
        MediaSessionSnapshotStore.INSTANCE.initialize(this);
        MediaSessionSnapshotStore.INSTANCE.setListenerConnected(true);
        MediaSessionManager manager = (MediaSessionManager) getSystemService(MediaSessionManager.class);
        try {
            Result.Companion companion = Result.INSTANCE;
            List<MediaController> activeSessions = manager != null ? manager.getActiveSessions(new ComponentName(this, (Class<?>) MediaListenerService.class)) : null;
            if (activeSessions == null) {
                activeSessions = CollectionsKt.emptyList();
            }
            objM159constructorimpl = Result.m159constructorimpl(activeSessions);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        List listEmptyList = CollectionsKt.emptyList();
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = listEmptyList;
        }
        Iterable active = (List) objM159constructorimpl;
        Iterator it = active.iterator();
        while (it.hasNext()) {
            attach((MediaController) it.next());
        }
    }

    @Override // android.service.notification.NotificationListenerService
    public void onListenerDisconnected() {
        MediaSessionSnapshotStore.INSTANCE.setListenerConnected(false);
        MediaSessionSnapshotStore.INSTANCE.setCurrent(null);
        try {
            Result.Companion companion = Result.INSTANCE;
            NotificationListenerService.requestRebind(new ComponentName(this, (Class<?>) MediaListenerService.class));
            Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        super.onListenerDisconnected();
    }

    @Override // android.service.notification.NotificationListenerService
    public void onNotificationPosted(StatusBarNotification sbn) {
        Object objM159constructorimpl;
        if (sbn == null) {
            return;
        }
        MediaSessionSnapshotStore.INSTANCE.initialize(this);
        Notification notification = sbn.getNotification();
        Intrinsics.checkNotNullExpressionValue(notification, "getNotification(...)");
        MediaSession.Token token = mediaToken(notification);
        if (token != null) {
            try {
                Result.Companion companion = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(new MediaController(this, token));
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            if (Result.m165isFailureimpl(objM159constructorimpl)) {
                objM159constructorimpl = null;
            }
            MediaController controller = (MediaController) objM159constructorimpl;
            if (controller != null) {
                attach(controller);
                return;
            }
        }
        MediaSessionSnapshotStore.INSTANCE.updateNotification(sbn);
    }

    @Override // android.service.notification.NotificationListenerService
    public void onNotificationRemoved(StatusBarNotification sbn) {
        MediaController.Callback callbackRemove;
        MediaController mediaControllerRemove;
        if (sbn != null && (callbackRemove = this.callbacks.remove(sbn.getPackageName())) != null && (mediaControllerRemove = this.controllers.remove(sbn.getPackageName())) != null) {
            mediaControllerRemove.unregisterCallback(callbackRemove);
        }
    }

    @Override // android.service.notification.NotificationListenerService, android.app.Service
    public void onDestroy() {
        MediaSessionSnapshotStore.INSTANCE.setListenerConnected(false);
        MediaSessionSnapshotStore.INSTANCE.setCurrent(null);
        for (Map.Entry<String, MediaController.Callback> entry : this.callbacks.entrySet()) {
            String key = entry.getKey();
            MediaController.Callback value = entry.getValue();
            MediaController mediaController = this.controllers.get(key);
            if (mediaController != null) {
                mediaController.unregisterCallback(value);
            }
        }
        this.callbacks.clear();
        this.controllers.clear();
        super.onDestroy();
    }

    private final void attach(final MediaController controller) {
        MediaController mediaController;
        String key = controller.getPackageName();
        MediaController.Callback callbackRemove = this.callbacks.remove(key);
        if (callbackRemove != null && (mediaController = this.controllers.get(key)) != null) {
            mediaController.unregisterCallback(callbackRemove);
        }
        MediaController.Callback callback = new MediaController.Callback() { // from class: com.carlink.cast.MediaListenerService$attach$callback$1
            @Override // android.media.session.MediaController.Callback
            public void onMetadataChanged(MediaMetadata metadata) {
                MediaSessionSnapshotStore.INSTANCE.update(controller);
            }

            @Override // android.media.session.MediaController.Callback
            public void onPlaybackStateChanged(PlaybackState state) {
                MediaSessionSnapshotStore.INSTANCE.update(controller);
            }
        };
        this.controllers.put(key, controller);
        this.callbacks.put(key, callback);
        try {
            Result.Companion companion = Result.INSTANCE;
            MediaListenerService mediaListenerService = this;
            controller.registerCallback(callback);
            Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        MediaSessionSnapshotStore.INSTANCE.update(controller);
    }

    private final MediaSession.Token mediaToken(Notification notification) {
        Bundle extras = notification.extras;
        if (extras == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 33) {
            return (MediaSession.Token) extras.getParcelable(NotificationCompat.EXTRA_MEDIA_SESSION, MediaSession.Token.class);
        }
        return (MediaSession.Token) extras.getParcelable(NotificationCompat.EXTRA_MEDIA_SESSION);
    }
}
