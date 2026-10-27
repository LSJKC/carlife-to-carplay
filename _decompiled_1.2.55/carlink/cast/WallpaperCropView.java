package com.carlink.cast;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import androidx.core.app.NotificationCompat;
import com.carplay.airplay.NtpClock;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: WallpaperCropView.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0001\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J(\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u0018H\u0014J\u0010\u0010\u001c\u001a\u00020\u00162\u0006\u0010\u001d\u001a\u00020\u001eH\u0014J\u0010\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\"H\u0016J\b\u0010#\u001a\u00020 H\u0016J\u0006\u0010$\u001a\u00020\u0005R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006%"}, d2 = {"Lcom/carlink/cast/WallpaperCropView;", "Landroid/view/View;", "context", "Landroid/content/Context;", "source", "Landroid/graphics/Bitmap;", "<init>", "(Landroid/content/Context;Landroid/graphics/Bitmap;)V", "imagePaint", "Landroid/graphics/Paint;", "imageDestination", "Landroid/graphics/RectF;", "framePaint", "transform", "Lcom/carlink/cast/WallpaperCropTransform;", "minimumScale", "", "lastX", "lastY", "scaleDetector", "Landroid/view/ScaleGestureDetector;", "onSizeChanged", "", "w", "", "h", "oldw", "oldh", "onDraw", "canvas", "Landroid/graphics/Canvas;", "onTouchEvent", "", NotificationCompat.CATEGORY_EVENT, "Landroid/view/MotionEvent;", "performClick", "createCroppedBitmap", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class WallpaperCropView extends View {
    private final Paint framePaint;
    private final RectF imageDestination;
    private final Paint imagePaint;
    private float lastX;
    private float lastY;
    private float minimumScale;
    private final ScaleGestureDetector scaleDetector;
    private final Bitmap source;
    private WallpaperCropTransform transform;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public WallpaperCropView(Context context, Bitmap source) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(source, "source");
        this.source = source;
        this.imagePaint = new Paint(3);
        this.imageDestination = new RectF();
        Paint paint = new Paint(1);
        paint.setColor(-1);
        paint.setAlpha(NtpClock.PT_REQUEST);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getResources().getDisplayMetrics().density * 2.0f);
        this.framePaint = paint;
        this.minimumScale = 1.0f;
        this.scaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() { // from class: com.carlink.cast.WallpaperCropView$scaleDetector$1
            @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScale(ScaleGestureDetector detector) {
                Intrinsics.checkNotNullParameter(detector, "detector");
                WallpaperCropTransform current = this.this$0.transform;
                if (current == null) {
                    return false;
                }
                this.this$0.transform = WallpaperCropGeometry.INSTANCE.zoomAround(current, detector.getScaleFactor(), detector.getFocusX(), detector.getFocusY(), this.this$0.source.getWidth(), this.this$0.source.getHeight(), this.this$0.getWidth(), this.this$0.getHeight(), this.this$0.minimumScale);
                this.this$0.invalidate();
                return true;
            }
        });
        setClickable(true);
        setContentDescription(context.getString(R.string.wallpaper_crop_preview));
        setBackgroundColor(0);
    }

    @Override // android.view.View
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w <= 0 || h <= 0) {
            return;
        }
        WallpaperCropTransform initial = WallpaperCropGeometry.INSTANCE.cover(this.source.getWidth(), this.source.getHeight(), w, h);
        this.transform = initial;
        this.minimumScale = initial.getScale();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        super.onDraw(canvas);
        if (getWidth() > 0 && getHeight() > 0) {
            WallpaperCropTransform current = this.transform;
            if (current == null) {
                current = WallpaperCropGeometry.INSTANCE.cover(this.source.getWidth(), this.source.getHeight(), getWidth(), getHeight());
            }
            float right = current.getLeft() + (this.source.getWidth() * current.getScale());
            float bottom = current.getTop() + (this.source.getHeight() * current.getScale());
            this.imageDestination.set(current.getLeft(), current.getTop(), right, bottom);
            canvas.drawBitmap(this.source, (Rect) null, this.imageDestination, this.imagePaint);
            canvas.drawRect(this.framePaint.getStrokeWidth() / 2.0f, this.framePaint.getStrokeWidth() / 2.0f, getWidth() - (this.framePaint.getStrokeWidth() / 2.0f), getHeight() - (this.framePaint.getStrokeWidth() / 2.0f), this.framePaint);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        WallpaperCropTransform current;
        Intrinsics.checkNotNullParameter(event, "event");
        this.scaleDetector.onTouchEvent(event);
        switch (event.getActionMasked()) {
            case 0:
                this.lastX = event.getX();
                this.lastY = event.getY();
                break;
            case 1:
            case 3:
                performClick();
                break;
            case 2:
                if (event.getPointerCount() == 1 && !this.scaleDetector.isInProgress() && (current = this.transform) != null) {
                    this.transform = WallpaperCropGeometry.INSTANCE.constrain(WallpaperCropTransform.copy$default(current, 0.0f, (current.getLeft() + event.getX()) - this.lastX, (current.getTop() + event.getY()) - this.lastY, 1, null), this.source.getWidth(), this.source.getHeight(), getWidth(), getHeight());
                    invalidate();
                }
                this.lastX = event.getX();
                this.lastY = event.getY();
                break;
        }
        return true;
    }

    @Override // android.view.View
    public boolean performClick() {
        super.performClick();
        return true;
    }

    public final Bitmap createCroppedBitmap() {
        if (!(getWidth() > 0 && getHeight() > 0)) {
            throw new IllegalStateException("裁剪框尚未布局".toString());
        }
        WallpaperCropTransform current = this.transform;
        if (current == null) {
            current = WallpaperCropGeometry.INSTANCE.cover(this.source.getWidth(), this.source.getHeight(), getWidth(), getHeight());
        }
        Bitmap result = Bitmap.createBitmap(CarPanelGeometry.PANEL_WIDTH, CarPanelGeometry.PANEL_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        float scaleX = 1972.0f / getWidth();
        float scaleY = 520.0f / getHeight();
        RectF destination = new RectF(current.getLeft() * scaleX, current.getTop() * scaleY, (current.getLeft() + (this.source.getWidth() * current.getScale())) * scaleX, (current.getTop() + (this.source.getHeight() * current.getScale())) * scaleY);
        canvas.drawBitmap(this.source, (Rect) null, destination, this.imagePaint);
        return result;
    }
}
