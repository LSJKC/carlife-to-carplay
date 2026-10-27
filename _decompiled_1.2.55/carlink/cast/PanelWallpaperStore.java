package com.carlink.cast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: PanelWallpaperStore.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\"\u0010\u0010\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010\u0014\u001a\u00020\u0015J\u0016\u0010\u0016\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u0011J\u000e\u0010\u0018\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u001a\u0010\u0019\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0014\u001a\u00020\u0015J\u001a\u0010\u001a\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u0014\u001a\u00020\u0015H\u0002J\u0010\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\b\u001a\u00020\t8F¢\u0006\u0006\u001a\u0004\b\n\u0010\u000b¨\u0006\u001e"}, d2 = {"Lcom/carlink/cast/PanelWallpaperStore;", "", "<init>", "()V", "WALLPAPER_FILE", "", "revisionCounter", "Ljava/util/concurrent/atomic/AtomicLong;", "revision", "", "getRevision", "()J", "hasCustomWallpaper", "", "context", "Landroid/content/Context;", "decodeUri", "Landroid/graphics/Bitmap;", "uri", "Landroid/net/Uri;", "maxSize", "", "setFromBitmap", "bitmap", "reset", "decode", "decodeFile", "file", "Ljava/io/File;", "wallpaperFile", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class PanelWallpaperStore {
    private static final String WALLPAPER_FILE = "panel-wallpaper.png";
    public static final PanelWallpaperStore INSTANCE = new PanelWallpaperStore();
    private static final AtomicLong revisionCounter = new AtomicLong(0);

    private PanelWallpaperStore() {
    }

    public final long getRevision() {
        return revisionCounter.get();
    }

    public final boolean hasCustomWallpaper(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        File fileWallpaperFile = wallpaperFile(context);
        return fileWallpaperFile.exists() && fileWallpaperFile.length() > 0;
    }

    public static /* synthetic */ Bitmap decodeUri$default(PanelWallpaperStore panelWallpaperStore, Context context, Uri uri, int i, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            i = 3072;
        }
        return panelWallpaperStore.decodeUri(context, uri, i);
    }

    static final void decodeUri$lambda$2$lambda$1(int $maxSize, ImageDecoder decoder, ImageDecoder.ImageInfo info, ImageDecoder.Source source) {
        Intrinsics.checkNotNullParameter(decoder, "decoder");
        Intrinsics.checkNotNullParameter(info, "info");
        Intrinsics.checkNotNullParameter(source, "<unused var>");
        decoder.setAllocator(1);
        int longest = Math.max(info.getSize().getWidth(), info.getSize().getHeight());
        if (longest > $maxSize) {
            decoder.setTargetSampleSize(RangesKt.coerceAtLeast((int) Math.floor(((double) longest) / ((double) $maxSize)), 1));
        }
    }

    public final Bitmap decodeUri(Context context, Uri uri, final int maxSize) {
        Object objM159constructorimpl;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(uri, "uri");
        try {
            Result.Companion companion = Result.INSTANCE;
            PanelWallpaperStore panelWallpaperStore = this;
            ImageDecoder.Source sourceCreateSource = ImageDecoder.createSource(context.getContentResolver(), uri);
            Intrinsics.checkNotNullExpressionValue(sourceCreateSource, "createSource(...)");
            objM159constructorimpl = Result.m159constructorimpl(ImageDecoder.decodeBitmap(sourceCreateSource, new ImageDecoder.OnHeaderDecodedListener() { // from class: com.carlink.cast.PanelWallpaperStore$$ExternalSyntheticLambda0
                @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
                public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
                    PanelWallpaperStore.decodeUri$lambda$2$lambda$1(maxSize, imageDecoder, imageInfo, source);
                }
            }));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = null;
        }
        Bitmap bitmap = (Bitmap) objM159constructorimpl;
        if (bitmap == null) {
            return null;
        }
        if (bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
            return bitmap;
        }
        return null;
    }

    public final boolean setFromBitmap(Context context, Bitmap bitmap) {
        Object objM159constructorimpl;
        Object objM159constructorimpl2;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(bitmap, "bitmap");
        try {
            Result.Companion companion = Result.INSTANCE;
            PanelWallpaperStore panelWallpaperStore = this;
            if (!(bitmap.getWidth() == 1972 && bitmap.getHeight() == 520)) {
                throw new IllegalArgumentException("壁纸裁剪输出必须为 1972x520".toString());
            }
            File fileWallpaperFile = panelWallpaperStore.wallpaperFile(context);
            File file = new File(context.getFilesDir(), "panel-wallpaper.png.tmp");
            try {
                OutputStream fileOutputStream = new FileOutputStream(file);
                BufferedOutputStream bufferedOutputStream = fileOutputStream instanceof BufferedOutputStream ? (BufferedOutputStream) fileOutputStream : new BufferedOutputStream(fileOutputStream, 8192);
                try {
                    if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream)) {
                        Unit unit = Unit.INSTANCE;
                        CloseableKt.closeFinally(bufferedOutputStream, null);
                        try {
                            Result.Companion companion2 = Result.INSTANCE;
                            objM159constructorimpl2 = Result.m159constructorimpl(Files.move(file.toPath(), fileWallpaperFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE));
                        } catch (Throwable th) {
                            Result.Companion companion3 = Result.INSTANCE;
                            objM159constructorimpl2 = Result.m159constructorimpl(ResultKt.createFailure(th));
                        }
                        if (Result.m162exceptionOrNullimpl(objM159constructorimpl2) != null) {
                            Files.move(file.toPath(), fileWallpaperFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }
                        revisionCounter.incrementAndGet();
                        file.delete();
                        objM159constructorimpl = Result.m159constructorimpl(true);
                    } else {
                        throw new IllegalStateException("Check failed.");
                    }
                } catch (Throwable th2) {
                    try {
                        throw th2;
                    } catch (Throwable th3) {
                        CloseableKt.closeFinally(bufferedOutputStream, th2);
                        throw th3;
                    }
                }
            } catch (Throwable th4) {
                file.delete();
                throw th4;
            }
        } catch (Throwable th5) {
            Result.Companion companion4 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th5));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = false;
        }
        return ((Boolean) objM159constructorimpl).booleanValue();
    }

    public final boolean reset(Context context) {
        Object objM159constructorimpl;
        Intrinsics.checkNotNullParameter(context, "context");
        try {
            Result.Companion companion = Result.INSTANCE;
            wallpaperFile(context).delete();
            revisionCounter.incrementAndGet();
            objM159constructorimpl = Result.m159constructorimpl(true);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = false;
        }
        return ((Boolean) objM159constructorimpl).booleanValue();
    }

    public static /* synthetic */ Bitmap decode$default(PanelWallpaperStore panelWallpaperStore, Context context, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 2560;
        }
        return panelWallpaperStore.decode(context, i);
    }

    public final Bitmap decode(Context context, int maxSize) {
        Intrinsics.checkNotNullParameter(context, "context");
        File file = wallpaperFile(context);
        if (!file.exists() || file.length() == 0) {
            return null;
        }
        return decodeFile(file, maxSize);
    }

    static final void decodeFile$lambda$11$lambda$10(int $maxSize, ImageDecoder decoder, ImageDecoder.ImageInfo info, ImageDecoder.Source source) {
        Intrinsics.checkNotNullParameter(decoder, "decoder");
        Intrinsics.checkNotNullParameter(info, "info");
        Intrinsics.checkNotNullParameter(source, "<unused var>");
        decoder.setAllocator(1);
        int longest = Math.max(info.getSize().getWidth(), info.getSize().getHeight());
        if (longest > $maxSize) {
            decoder.setTargetSampleSize(RangesKt.coerceAtLeast((int) Math.floor(((double) longest) / ((double) $maxSize)), 1));
        }
    }

    private final Bitmap decodeFile(File file, final int maxSize) {
        Object objM159constructorimpl;
        try {
            Result.Companion companion = Result.INSTANCE;
            PanelWallpaperStore panelWallpaperStore = this;
            ImageDecoder.Source sourceCreateSource = ImageDecoder.createSource(file);
            Intrinsics.checkNotNullExpressionValue(sourceCreateSource, "createSource(...)");
            objM159constructorimpl = Result.m159constructorimpl(ImageDecoder.decodeBitmap(sourceCreateSource, new ImageDecoder.OnHeaderDecodedListener() { // from class: com.carlink.cast.PanelWallpaperStore$$ExternalSyntheticLambda1
                @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
                public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
                    PanelWallpaperStore.decodeFile$lambda$11$lambda$10(maxSize, imageDecoder, imageInfo, source);
                }
            }));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = null;
        }
        Bitmap bitmap = (Bitmap) objM159constructorimpl;
        if (bitmap == null) {
            return null;
        }
        if (bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
            return bitmap;
        }
        return null;
    }

    private final File wallpaperFile(Context context) {
        return new File(context.getFilesDir(), WALLPAPER_FILE);
    }
}
