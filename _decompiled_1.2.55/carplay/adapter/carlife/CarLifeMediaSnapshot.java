package com.carplay.adapter.carlife;

import android.graphics.Bitmap;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.bouncycastle.i18n.MessageBundle;

/* JADX INFO: compiled from: CarLifeMediaPublisher.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b*\b\u0086\b\u0018\u00002\u00020\u0001B\u008d\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0010\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0012¢\u0006\u0004\b\u0015\u0010\u0016J\t\u0010*\u001a\u00020\u0003HÆ\u0003J\t\u0010+\u001a\u00020\u0003HÆ\u0003J\t\u0010,\u001a\u00020\u0003HÆ\u0003J\t\u0010-\u001a\u00020\u0003HÆ\u0003J\t\u0010.\u001a\u00020\bHÆ\u0003J\t\u0010/\u001a\u00020\bHÆ\u0003J\t\u00100\u001a\u00020\u000bHÆ\u0003J\t\u00101\u001a\u00020\u0003HÆ\u0003J\u000b\u00102\u001a\u0004\u0018\u00010\u000eHÆ\u0003J\u000b\u00103\u001a\u0004\u0018\u00010\u0010HÆ\u0003J\t\u00104\u001a\u00020\u0012HÆ\u0003J\t\u00105\u001a\u00020\u0003HÆ\u0003J\t\u00106\u001a\u00020\u0012HÆ\u0003J\u008f\u0001\u00107\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u0012HÆ\u0001J\u0013\u00108\u001a\u00020\u000b2\b\u00109\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010:\u001a\u00020\u0012HÖ\u0001J\t\u0010;\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0018R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0018R\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0018R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\t\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001dR\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010\f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0018R\u0013\u0010\r\u001a\u0004\u0018\u00010\u000e¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u0010¢\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0011\u0010\u0011\u001a\u00020\u0012¢\u0006\b\n\u0000\u001a\u0004\b&\u0010'R\u0011\u0010\u0013\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b(\u0010\u0018R\u0011\u0010\u0014\u001a\u00020\u0012¢\u0006\b\n\u0000\u001a\u0004\b)\u0010'¨\u0006<"}, d2 = {"Lcom/carplay/adapter/carlife/CarLifeMediaSnapshot;", "", MessageBundle.TITLE_ENTRY, "", "artist", "album", "lyrics", "durationMs", "", "positionMs", "playing", "", "packageName", "artwork", "Landroid/graphics/Bitmap;", "artworkBytes", "", "applicationPid", "", "applicationName", "audioSessionId", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJZLjava/lang/String;Landroid/graphics/Bitmap;[BILjava/lang/String;I)V", "getTitle", "()Ljava/lang/String;", "getArtist", "getAlbum", "getLyrics", "getDurationMs", "()J", "getPositionMs", "getPlaying", "()Z", "getPackageName", "getArtwork", "()Landroid/graphics/Bitmap;", "getArtworkBytes", "()[B", "getApplicationPid", "()I", "getApplicationName", "getAudioSessionId", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "copy", "equals", "other", "hashCode", "toString", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class CarLifeMediaSnapshot {
    private final String album;
    private final String applicationName;
    private final int applicationPid;
    private final String artist;
    private final Bitmap artwork;
    private final byte[] artworkBytes;
    private final int audioSessionId;
    private final long durationMs;
    private final String lyrics;
    private final String packageName;
    private final boolean playing;
    private final long positionMs;
    private final String title;

    public CarLifeMediaSnapshot() {
        this(null, null, null, null, 0L, 0L, false, null, null, null, 0, null, 0, 8191, null);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getTitle() {
        return this.title;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final byte[] getArtworkBytes() {
        return this.artworkBytes;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final int getApplicationPid() {
        return this.applicationPid;
    }

    /* JADX INFO: renamed from: component12, reason: from getter */
    public final String getApplicationName() {
        return this.applicationName;
    }

    /* JADX INFO: renamed from: component13, reason: from getter */
    public final int getAudioSessionId() {
        return this.audioSessionId;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getArtist() {
        return this.artist;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getAlbum() {
        return this.album;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final String getLyrics() {
        return this.lyrics;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final long getDurationMs() {
        return this.durationMs;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final long getPositionMs() {
        return this.positionMs;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final boolean getPlaying() {
        return this.playing;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final String getPackageName() {
        return this.packageName;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final Bitmap getArtwork() {
        return this.artwork;
    }

    public final CarLifeMediaSnapshot copy(String title, String artist, String album, String lyrics, long durationMs, long positionMs, boolean playing, String packageName, Bitmap artwork, byte[] artworkBytes, int applicationPid, String applicationName, int audioSessionId) {
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(artist, "artist");
        Intrinsics.checkNotNullParameter(album, "album");
        Intrinsics.checkNotNullParameter(lyrics, "lyrics");
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        Intrinsics.checkNotNullParameter(applicationName, "applicationName");
        return new CarLifeMediaSnapshot(title, artist, album, lyrics, durationMs, positionMs, playing, packageName, artwork, artworkBytes, applicationPid, applicationName, audioSessionId);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CarLifeMediaSnapshot)) {
            return false;
        }
        CarLifeMediaSnapshot carLifeMediaSnapshot = (CarLifeMediaSnapshot) other;
        return Intrinsics.areEqual(this.title, carLifeMediaSnapshot.title) && Intrinsics.areEqual(this.artist, carLifeMediaSnapshot.artist) && Intrinsics.areEqual(this.album, carLifeMediaSnapshot.album) && Intrinsics.areEqual(this.lyrics, carLifeMediaSnapshot.lyrics) && this.durationMs == carLifeMediaSnapshot.durationMs && this.positionMs == carLifeMediaSnapshot.positionMs && this.playing == carLifeMediaSnapshot.playing && Intrinsics.areEqual(this.packageName, carLifeMediaSnapshot.packageName) && Intrinsics.areEqual(this.artwork, carLifeMediaSnapshot.artwork) && Intrinsics.areEqual(this.artworkBytes, carLifeMediaSnapshot.artworkBytes) && this.applicationPid == carLifeMediaSnapshot.applicationPid && Intrinsics.areEqual(this.applicationName, carLifeMediaSnapshot.applicationName) && this.audioSessionId == carLifeMediaSnapshot.audioSessionId;
    }

    public int hashCode() {
        return (((((((((((((((((((((((this.title.hashCode() * 31) + this.artist.hashCode()) * 31) + this.album.hashCode()) * 31) + this.lyrics.hashCode()) * 31) + Long.hashCode(this.durationMs)) * 31) + Long.hashCode(this.positionMs)) * 31) + Boolean.hashCode(this.playing)) * 31) + this.packageName.hashCode()) * 31) + (this.artwork == null ? 0 : this.artwork.hashCode())) * 31) + (this.artworkBytes != null ? Arrays.hashCode(this.artworkBytes) : 0)) * 31) + Integer.hashCode(this.applicationPid)) * 31) + this.applicationName.hashCode()) * 31) + Integer.hashCode(this.audioSessionId);
    }

    public String toString() {
        return "CarLifeMediaSnapshot(title=" + this.title + ", artist=" + this.artist + ", album=" + this.album + ", lyrics=" + this.lyrics + ", durationMs=" + this.durationMs + ", positionMs=" + this.positionMs + ", playing=" + this.playing + ", packageName=" + this.packageName + ", artwork=" + this.artwork + ", artworkBytes=" + Arrays.toString(this.artworkBytes) + ", applicationPid=" + this.applicationPid + ", applicationName=" + this.applicationName + ", audioSessionId=" + this.audioSessionId + ")";
    }

    public CarLifeMediaSnapshot(String title, String artist, String album, String lyrics, long durationMs, long positionMs, boolean playing, String packageName, Bitmap artwork, byte[] artworkBytes, int applicationPid, String applicationName, int audioSessionId) {
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(artist, "artist");
        Intrinsics.checkNotNullParameter(album, "album");
        Intrinsics.checkNotNullParameter(lyrics, "lyrics");
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        Intrinsics.checkNotNullParameter(applicationName, "applicationName");
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.lyrics = lyrics;
        this.durationMs = durationMs;
        this.positionMs = positionMs;
        this.playing = playing;
        this.packageName = packageName;
        this.artwork = artwork;
        this.artworkBytes = artworkBytes;
        this.applicationPid = applicationPid;
        this.applicationName = applicationName;
        this.audioSessionId = audioSessionId;
    }

    public /* synthetic */ CarLifeMediaSnapshot(String str, String str2, String str3, String str4, long j, long j2, boolean z, String str5, Bitmap bitmap, byte[] bArr, int i, String str6, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? "" : str, (i3 & 2) != 0 ? "" : str2, (i3 & 4) != 0 ? "" : str3, (i3 & 8) != 0 ? "" : str4, (i3 & 16) != 0 ? 0L : j, (i3 & 32) == 0 ? j2 : 0L, (i3 & 64) != 0 ? false : z, (i3 & 128) != 0 ? "" : str5, (i3 & 256) != 0 ? null : bitmap, (i3 & 512) == 0 ? bArr : null, (i3 & 1024) != 0 ? 0 : i, (i3 & 2048) == 0 ? str6 : "", (i3 & 4096) != 0 ? 0 : i2);
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

    public final boolean getPlaying() {
        return this.playing;
    }

    public final String getPackageName() {
        return this.packageName;
    }

    public final Bitmap getArtwork() {
        return this.artwork;
    }

    public final byte[] getArtworkBytes() {
        return this.artworkBytes;
    }

    public final int getApplicationPid() {
        return this.applicationPid;
    }

    public final String getApplicationName() {
        return this.applicationName;
    }

    public final int getAudioSessionId() {
        return this.audioSessionId;
    }
}
