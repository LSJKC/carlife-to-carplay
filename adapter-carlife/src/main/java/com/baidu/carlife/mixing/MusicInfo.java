package com.baidu.carlife.mixing;

import android.os.Parcel;
import android.os.Parcelable;

/** Mirror of Honor CarLife 8.8.5's MusicInfo parcelable. */
public final class MusicInfo implements Parcelable {
    private final String song;
    private final String artist;
    private final String source;
    private final long duration;
    private final int index;
    private final int playlistSize;

    public MusicInfo(String song, String artist, String source,
                     long duration, int index, int playlistSize) {
        this.song = song;
        this.artist = artist;
        this.source = source;
        this.duration = duration;
        this.index = index;
        this.playlistSize = playlistSize;
    }

    private MusicInfo(Parcel in) {
        this.source = in.readString();
        this.song = in.readString();
        this.artist = in.readString();
        this.duration = in.readLong();
        this.playlistSize = in.readInt();
        this.index = in.readInt();
    }

    public String getSong() { return song; }
    public String getArtist() { return artist; }
    public String getSource() { return source; }
    public long getDuration() { return duration; }
    public int getIndex() { return index; }
    public int getPlaylistSize() { return playlistSize; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(source);
        out.writeString(song);
        out.writeString(artist);
        out.writeLong(duration);
        out.writeInt(playlistSize);
        out.writeInt(index);
    }

    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel in) { return new MusicInfo(in); }
        @Override
        public MusicInfo[] newArray(int size) { return new MusicInfo[size]; }
    };
}
