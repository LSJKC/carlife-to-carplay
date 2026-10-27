package com.carlink.cast;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.service.notification.StatusBarNotification;
import androidx.core.app.NotificationCompat;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.collections.ArraysKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.sequences.SequencesKt;
import kotlin.text.StringsKt;
import org.bouncycastle.i18n.MessageBundle;

/* JADX INFO: compiled from: MediaListenerService.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\bÀ\u0002\u0018\u00002\u00020\u0001:\u0001*B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0005J\u000e\u0010\u0015\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u001aJ\u0014\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0002J\u0014\u0010\u001f\u001a\u0004\u0018\u00010 2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0002J+\u0010!\u001a\u00020\"2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\u0012\u0010#\u001a\n\u0012\u0006\b\u0001\u0012\u00020\"0$\"\u00020\"H\u0002¢\u0006\u0002\u0010%J+\u0010&\u001a\u00020\"2\b\u0010'\u001a\u0004\u0018\u00010(2\u0012\u0010#\u001a\n\u0012\u0006\b\u0001\u0012\u00020\"0$\"\u00020\"H\u0002¢\u0006\u0002\u0010)R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001a\u0010\f\u001a\u00020\rX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011¨\u0006+"}, d2 = {"Lcom/carlink/cast/MediaSessionSnapshotStore;", "", "<init>", "()V", "appContext", "Landroid/content/Context;", "current", "Lcom/carlink/cast/MediaSessionSnapshotStore$Snapshot;", "getCurrent", "()Lcom/carlink/cast/MediaSessionSnapshotStore$Snapshot;", "setCurrent", "(Lcom/carlink/cast/MediaSessionSnapshotStore$Snapshot;)V", "listenerConnected", "", "getListenerConnected", "()Z", "setListenerConnected", "(Z)V", "initialize", "", "context", "update", "controller", "Landroid/media/session/MediaController;", "updateNotification", "sbn", "Landroid/service/notification/StatusBarNotification;", "artworkBytes", "", "metadata", "Landroid/media/MediaMetadata;", "artworkFromUri", "Landroid/graphics/Bitmap;", "firstMetadataString", "", "keys", "", "(Landroid/media/MediaMetadata;[Ljava/lang/String;)Ljava/lang/String;", "firstBundleString", "bundle", "Landroid/os/Bundle;", "(Landroid/os/Bundle;[Ljava/lang/String;)Ljava/lang/String;", "Snapshot", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MediaSessionSnapshotStore {
    public static final MediaSessionSnapshotStore INSTANCE = new MediaSessionSnapshotStore();
    private static volatile Context appContext;
    private static volatile Snapshot current;
    private static volatile boolean listenerConnected;

    private MediaSessionSnapshotStore() {
    }

    /* JADX INFO: compiled from: MediaListenerService.kt */
    @Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u001c\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B[\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\t\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\t\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000f¢\u0006\u0004\b\u0010\u0010\u0011J\t\u0010 \u001a\u00020\u0003HÆ\u0003J\t\u0010!\u001a\u00020\u0003HÆ\u0003J\t\u0010\"\u001a\u00020\u0003HÆ\u0003J\t\u0010#\u001a\u00020\u0003HÆ\u0003J\t\u0010$\u001a\u00020\u0003HÆ\u0003J\t\u0010%\u001a\u00020\tHÆ\u0003J\t\u0010&\u001a\u00020\tHÆ\u0003J\t\u0010'\u001a\u00020\fHÆ\u0003J\t\u0010(\u001a\u00020\tHÆ\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u000fHÆ\u0003Jo\u0010*\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\t2\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\t2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000fHÆ\u0001J\u0013\u0010+\u001a\u00020,2\b\u0010-\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010.\u001a\u00020\fHÖ\u0001J\t\u0010/\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0013R\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0013R\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0013R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\n\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0019R\u0011\u0010\u000b\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\r\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0019R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001f¨\u00060"}, d2 = {"Lcom/carlink/cast/MediaSessionSnapshotStore$Snapshot;", "", "packageName", "", MessageBundle.TITLE_ENTRY, "artist", "album", "lyrics", "durationMs", "", "positionMs", "state", "", "updatedAt", "artwork", "", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJIJ[B)V", "getPackageName", "()Ljava/lang/String;", "getTitle", "getArtist", "getAlbum", "getLyrics", "getDurationMs", "()J", "getPositionMs", "getState", "()I", "getUpdatedAt", "getArtwork", "()[B", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "copy", "equals", "", "other", "hashCode", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Snapshot {
        private final String album;
        private final String artist;
        private final byte[] artwork;
        private final long durationMs;
        private final String lyrics;
        private final String packageName;
        private final long positionMs;
        private final int state;
        private final String title;
        private final long updatedAt;

        public static /* synthetic */ Snapshot copy$default(Snapshot snapshot, String str, String str2, String str3, String str4, String str5, long j, long j2, int i, long j3, byte[] bArr, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                str = snapshot.packageName;
            }
            return snapshot.copy(str, (i2 & 2) != 0 ? snapshot.title : str2, (i2 & 4) != 0 ? snapshot.artist : str3, (i2 & 8) != 0 ? snapshot.album : str4, (i2 & 16) != 0 ? snapshot.lyrics : str5, (i2 & 32) != 0 ? snapshot.durationMs : j, (i2 & 64) != 0 ? snapshot.positionMs : j2, (i2 & 128) != 0 ? snapshot.state : i, (i2 & 256) != 0 ? snapshot.updatedAt : j3, (i2 & 512) != 0 ? snapshot.artwork : bArr);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final String getPackageName() {
            return this.packageName;
        }

        /* JADX INFO: renamed from: component10, reason: from getter */
        public final byte[] getArtwork() {
            return this.artwork;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final String getTitle() {
            return this.title;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final String getArtist() {
            return this.artist;
        }

        /* JADX INFO: renamed from: component4, reason: from getter */
        public final String getAlbum() {
            return this.album;
        }

        /* JADX INFO: renamed from: component5, reason: from getter */
        public final String getLyrics() {
            return this.lyrics;
        }

        /* JADX INFO: renamed from: component6, reason: from getter */
        public final long getDurationMs() {
            return this.durationMs;
        }

        /* JADX INFO: renamed from: component7, reason: from getter */
        public final long getPositionMs() {
            return this.positionMs;
        }

        /* JADX INFO: renamed from: component8, reason: from getter */
        public final int getState() {
            return this.state;
        }

        /* JADX INFO: renamed from: component9, reason: from getter */
        public final long getUpdatedAt() {
            return this.updatedAt;
        }

        public final Snapshot copy(String packageName, String title, String artist, String album, String lyrics, long durationMs, long positionMs, int state, long updatedAt, byte[] artwork) {
            Intrinsics.checkNotNullParameter(packageName, "packageName");
            Intrinsics.checkNotNullParameter(title, "title");
            Intrinsics.checkNotNullParameter(artist, "artist");
            Intrinsics.checkNotNullParameter(album, "album");
            Intrinsics.checkNotNullParameter(lyrics, "lyrics");
            return new Snapshot(packageName, title, artist, album, lyrics, durationMs, positionMs, state, updatedAt, artwork);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Snapshot)) {
                return false;
            }
            Snapshot snapshot = (Snapshot) other;
            return Intrinsics.areEqual(this.packageName, snapshot.packageName) && Intrinsics.areEqual(this.title, snapshot.title) && Intrinsics.areEqual(this.artist, snapshot.artist) && Intrinsics.areEqual(this.album, snapshot.album) && Intrinsics.areEqual(this.lyrics, snapshot.lyrics) && this.durationMs == snapshot.durationMs && this.positionMs == snapshot.positionMs && this.state == snapshot.state && this.updatedAt == snapshot.updatedAt && Intrinsics.areEqual(this.artwork, snapshot.artwork);
        }

        public int hashCode() {
            return (((((((((((((((((this.packageName.hashCode() * 31) + this.title.hashCode()) * 31) + this.artist.hashCode()) * 31) + this.album.hashCode()) * 31) + this.lyrics.hashCode()) * 31) + Long.hashCode(this.durationMs)) * 31) + Long.hashCode(this.positionMs)) * 31) + Integer.hashCode(this.state)) * 31) + Long.hashCode(this.updatedAt)) * 31) + (this.artwork == null ? 0 : Arrays.hashCode(this.artwork));
        }

        public String toString() {
            return "Snapshot(packageName=" + this.packageName + ", title=" + this.title + ", artist=" + this.artist + ", album=" + this.album + ", lyrics=" + this.lyrics + ", durationMs=" + this.durationMs + ", positionMs=" + this.positionMs + ", state=" + this.state + ", updatedAt=" + this.updatedAt + ", artwork=" + Arrays.toString(this.artwork) + ")";
        }

        public Snapshot(String packageName, String title, String artist, String album, String lyrics, long durationMs, long positionMs, int state, long updatedAt, byte[] artwork) {
            Intrinsics.checkNotNullParameter(packageName, "packageName");
            Intrinsics.checkNotNullParameter(title, "title");
            Intrinsics.checkNotNullParameter(artist, "artist");
            Intrinsics.checkNotNullParameter(album, "album");
            Intrinsics.checkNotNullParameter(lyrics, "lyrics");
            this.packageName = packageName;
            this.title = title;
            this.artist = artist;
            this.album = album;
            this.lyrics = lyrics;
            this.durationMs = durationMs;
            this.positionMs = positionMs;
            this.state = state;
            this.updatedAt = updatedAt;
            this.artwork = artwork;
        }

        /* JADX WARN: Illegal instructions before constructor call */
        public /* synthetic */ Snapshot(String str, String str2, String str3, String str4, String str5, long j, long j2, int i, long j3, byte[] bArr, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            byte[] bArr2;
            if ((i2 & 512) == 0) {
                bArr2 = bArr;
            } else {
                bArr2 = null;
            }
            this(str, str2, str3, str4, str5, j, j2, i, j3, bArr2);
        }

        public final String getPackageName() {
            return this.packageName;
        }

        public final String getTitle() {
            return this.title;
        }

        public final String getArtist() {
            return this.artist;
        }

        public final String getAlbum() {
            return this.album;
        }

        public final String getLyrics() {
            return this.lyrics;
        }

        public final long getDurationMs() {
            return this.durationMs;
        }

        public final long getPositionMs() {
            return this.positionMs;
        }

        public final int getState() {
            return this.state;
        }

        public final long getUpdatedAt() {
            return this.updatedAt;
        }

        public final byte[] getArtwork() {
            return this.artwork;
        }
    }

    public final Snapshot getCurrent() {
        return current;
    }

    public final void setCurrent(Snapshot snapshot) {
        current = snapshot;
    }

    public final boolean getListenerConnected() {
        return listenerConnected;
    }

    public final void setListenerConnected(boolean z) {
        listenerConnected = z;
    }

    public final void initialize(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        appContext = context.getApplicationContext();
    }

    public final void update(MediaController controller) {
        int i;
        Intrinsics.checkNotNullParameter(controller, "controller");
        MediaMetadata metadata = controller.getMetadata();
        PlaybackState playback = controller.getPlaybackState();
        long now = SystemClock.elapsedRealtime();
        boolean playing = playback != null && playback.getState() == 3;
        long base = playback != null ? RangesKt.coerceAtLeast(playback.getPosition(), 0L) : 0L;
        long position = (!playing || playback == null) ? base : base + ((long) (RangesKt.coerceAtLeast(now - playback.getLastPositionUpdateTime(), 0L) * ((double) playback.getPlaybackSpeed())));
        String packageName = controller.getPackageName();
        Intrinsics.checkNotNullExpressionValue(packageName, "getPackageName(...)");
        String strFirstMetadataString = firstMetadataString(metadata, "android.media.metadata.TITLE", "android.media.metadata.DISPLAY_TITLE");
        String strFirstMetadataString2 = firstMetadataString(metadata, "android.media.metadata.ARTIST", "android.media.metadata.ALBUM_ARTIST", "android.media.metadata.AUTHOR");
        int i2 = 0;
        String strFirstMetadataString3 = firstMetadataString(metadata, "android.media.metadata.ALBUM", "android.media.metadata.DISPLAY_SUBTITLE");
        String strFirstMetadataString4 = firstMetadataString(metadata, NowPlayingPublisher.METADATA_KEY_LYRICS, "android.media.metadata.LYRIC", "ucar.media.metadata.LYRICS_WHOLE", "com.tencent.qqmusic.metadata.LYRICS");
        if (StringsKt.isBlank(strFirstMetadataString4)) {
            i = 3;
            strFirstMetadataString4 = INSTANCE.firstBundleString(playback != null ? playback.getExtras() : null, NowPlayingPublisher.METADATA_KEY_LYRICS, "android.media.metadata.LYRIC", "ucar.media.metadata.LYRICS_WHOLE", "com.tencent.qqmusic.metadata.LYRICS");
        } else {
            i = 3;
        }
        String str = strFirstMetadataString4;
        long jCoerceAtLeast = metadata != null ? RangesKt.coerceAtLeast(metadata.getLong("android.media.metadata.DURATION"), 0L) : 0L;
        Integer numValueOf = playback != 0 ? Integer.valueOf(playback.getState()) : null;
        if (numValueOf != null && numValueOf.intValue() == i) {
            i2 = 1;
        } else if (numValueOf != null && numValueOf.intValue() == 2) {
            i2 = 2;
        }
        current = new Snapshot(packageName, strFirstMetadataString, strFirstMetadataString2, strFirstMetadataString3, str, jCoerceAtLeast, position, i2, now, artworkBytes(metadata));
    }

    public final void updateNotification(StatusBarNotification sbn) {
        Intrinsics.checkNotNullParameter(sbn, "sbn");
        Bundle extras = sbn.getNotification().extras;
        if (extras == null) {
            return;
        }
        CharSequence charSequence = extras.getCharSequence(NotificationCompat.EXTRA_TITLE);
        String string = charSequence != null ? charSequence.toString() : null;
        if (string == null) {
            string = "";
        }
        String title = string;
        CharSequence charSequence2 = extras.getCharSequence(NotificationCompat.EXTRA_TEXT);
        String string2 = charSequence2 != null ? charSequence2.toString() : null;
        String artist = string2 == null ? "" : string2;
        CharSequence charSequence3 = extras.getCharSequence(NotificationCompat.EXTRA_SUB_TEXT);
        String string3 = charSequence3 != null ? charSequence3.toString() : null;
        String album = string3 == null ? "" : string3;
        if (StringsKt.isBlank(title) && StringsKt.isBlank(artist)) {
            return;
        }
        String packageName = sbn.getPackageName();
        Intrinsics.checkNotNullExpressionValue(packageName, "getPackageName(...)");
        CharSequence charSequence4 = extras.getCharSequence(NotificationCompat.EXTRA_BIG_TEXT);
        String string4 = charSequence4 != null ? charSequence4.toString() : null;
        current = new Snapshot(packageName, title, artist, album, string4 == null ? "" : string4, 0L, 0L, 1, SystemClock.elapsedRealtime(), null);
    }

    private final byte[] artworkBytes(MediaMetadata metadata) {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        Bitmap bitmap;
        try {
            Result.Companion companion = Result.INSTANCE;
            MediaSessionSnapshotStore mediaSessionSnapshotStore = this;
            if (metadata == null || (bitmap = metadata.getBitmap("android.media.metadata.ART")) == null) {
                bitmap = metadata != null ? metadata.getBitmap("android.media.metadata.ALBUM_ART") : null;
            }
            objM159constructorimpl = Result.m159constructorimpl(bitmap);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = null;
        }
        Bitmap bitmap2 = (Bitmap) objM159constructorimpl;
        if (bitmap2 == null) {
            bitmap2 = artworkFromUri(metadata);
        }
        if (bitmap2 == null) {
            return null;
        }
        try {
            Result.Companion companion3 = Result.INSTANCE;
            MediaSessionSnapshotStore mediaSessionSnapshotStore2 = this;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                ByteArrayOutputStream byteArrayOutputStream2 = byteArrayOutputStream;
                bitmap2.compress(Bitmap.CompressFormat.JPEG, 86, byteArrayOutputStream2);
                byte[] byteArray = byteArrayOutputStream2.toByteArray();
                Intrinsics.checkNotNull(byteArray);
                if (!(!(byteArray.length == 0) && byteArray.length <= 2097152)) {
                    byteArray = null;
                }
                CloseableKt.closeFinally(byteArrayOutputStream, null);
                objM159constructorimpl2 = Result.m159constructorimpl(byteArray);
            } catch (Throwable th2) {
                try {
                    throw th2;
                } catch (Throwable th3) {
                    CloseableKt.closeFinally(byteArrayOutputStream, th2);
                    throw th3;
                }
            }
        } catch (Throwable th4) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th4));
        }
        return (byte[]) (Result.m165isFailureimpl(objM159constructorimpl2) ? null : objM159constructorimpl2);
    }

    private final Bitmap artworkFromUri(MediaMetadata metadata) {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        Bitmap bitmapDecodeStream;
        ContentResolver contentResolver;
        InputStream inputStreamOpenInputStream;
        String string;
        try {
            Result.Companion companion = Result.INSTANCE;
            MediaSessionSnapshotStore mediaSessionSnapshotStore = this;
            if (metadata == null || (string = metadata.getString("android.media.metadata.ALBUM_ART_URI")) == null) {
                string = metadata != null ? metadata.getString("android.media.metadata.ART_URI") : null;
            }
            objM159constructorimpl = Result.m159constructorimpl(string);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = null;
        }
        String value = (String) objM159constructorimpl;
        if (value == null) {
            value = "";
        }
        if (StringsKt.isBlank(value)) {
            return null;
        }
        try {
            Result.Companion companion3 = Result.INSTANCE;
            MediaSessionSnapshotStore mediaSessionSnapshotStore2 = this;
            Context context = appContext;
            if (context == null || (contentResolver = context.getContentResolver()) == null || (inputStreamOpenInputStream = contentResolver.openInputStream(Uri.parse(value))) == null) {
                bitmapDecodeStream = null;
            } else {
                InputStream inputStream = inputStreamOpenInputStream;
                try {
                    bitmapDecodeStream = BitmapFactory.decodeStream(inputStream);
                    CloseableKt.closeFinally(inputStream, null);
                } catch (Throwable th2) {
                    try {
                        throw th2;
                    } catch (Throwable th3) {
                        CloseableKt.closeFinally(inputStream, th2);
                        throw th3;
                    }
                }
            }
            objM159constructorimpl2 = Result.m159constructorimpl(bitmapDecodeStream);
        } catch (Throwable th4) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th4));
        }
        return (Bitmap) (Result.m165isFailureimpl(objM159constructorimpl2) ? null : objM159constructorimpl2);
    }

    private final String firstMetadataString(final MediaMetadata metadata, String... keys) {
        Object next;
        Iterator it = SequencesKt.mapNotNull(ArraysKt.asSequence(keys), new Function1() { // from class: com.carlink.cast.MediaSessionSnapshotStore$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MediaSessionSnapshotStore.firstMetadataString$lambda$9(metadata, (String) obj);
            }
        }).iterator();
        do {
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
        } while (StringsKt.isBlank((String) next));
        String str = (String) next;
        return str == null ? "" : str;
    }

    static final String firstMetadataString$lambda$9(MediaMetadata $metadata, String key) {
        Object objM159constructorimpl;
        Intrinsics.checkNotNullParameter(key, "key");
        MediaSessionSnapshotStore mediaSessionSnapshotStore = INSTANCE;
        try {
            Result.Companion companion = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl($metadata != null ? $metadata.getString(key) : null);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        return (String) (Result.m165isFailureimpl(objM159constructorimpl) ? null : objM159constructorimpl);
    }

    private final String firstBundleString(final Bundle bundle, String... keys) {
        Object next;
        Iterator it = SequencesKt.mapNotNull(ArraysKt.asSequence(keys), new Function1() { // from class: com.carlink.cast.MediaSessionSnapshotStore$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return MediaSessionSnapshotStore.firstBundleString$lambda$13(bundle, (String) obj);
            }
        }).iterator();
        do {
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
        } while (StringsKt.isBlank((String) next));
        String str = (String) next;
        return str == null ? "" : str;
    }

    static final String firstBundleString$lambda$13(Bundle $bundle, String key) {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        CharSequence charSequence;
        Intrinsics.checkNotNullParameter(key, "key");
        MediaSessionSnapshotStore mediaSessionSnapshotStore = INSTANCE;
        try {
            Result.Companion companion = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(($bundle == null || (charSequence = $bundle.getCharSequence(key)) == null) ? null : charSequence.toString());
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = null;
        }
        String str = (String) objM159constructorimpl;
        if (str != null) {
            return str;
        }
        MediaSessionSnapshotStore mediaSessionSnapshotStore2 = INSTANCE;
        try {
            Result.Companion companion3 = Result.INSTANCE;
            objM159constructorimpl2 = Result.m159constructorimpl($bundle != null ? $bundle.getString(key) : null);
        } catch (Throwable th2) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th2));
        }
        return (String) (Result.m165isFailureimpl(objM159constructorimpl2) ? null : objM159constructorimpl2);
    }
}
