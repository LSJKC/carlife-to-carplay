package com.baidu.carlife.mixing;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.Surface;

/**
 * Mirror of the host's CastConfig parcelable.
 * Field order MUST match: width, height, dpi, surface.
 */
public final class CastConfig implements Parcelable {
    private final int width;
    private final int height;
    private final int dpi;
    private final Surface surface;

    public CastConfig(int width, int height, int dpi, Surface surface) {
        this.width = width;
        this.height = height;
        this.dpi = dpi;
        this.surface = surface;
    }

    private CastConfig(Parcel in) {
        this.width = in.readInt();
        this.height = in.readInt();
        this.dpi = in.readInt();
        this.surface = in.readParcelable(Surface.class.getClassLoader());
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getDpi() { return dpi; }
    public Surface getSurface() { return surface; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(width);
        out.writeInt(height);
        out.writeInt(dpi);
        out.writeParcelable(surface, flags);
    }

    public static final Creator<CastConfig> CREATOR = new Creator<CastConfig>() {
        @Override
        public CastConfig createFromParcel(Parcel in) { return new CastConfig(in); }
        @Override
        public CastConfig[] newArray(int size) { return new CastConfig[size]; }
    };
}
