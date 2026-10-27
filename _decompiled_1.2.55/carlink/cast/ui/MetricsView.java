package com.carlink.cast.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.view.View;
import com.carlink.cast.CastMetric;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: MetricsView.kt */
/* JADX INFO: loaded from: classes14.dex */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0014R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R0\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014¨\u0006\u0019"}, d2 = {"Lcom/carlink/cast/ui/MetricsView;", "Landroid/view/View;", "context", "Landroid/content/Context;", "<init>", "(Landroid/content/Context;)V", "density", "", "line", "Landroid/graphics/Paint;", "fill", "baseline", "idle", "value", "", "Lcom/carlink/cast/CastMetric;", "samples", "getSamples", "()Ljava/util/List;", "setSamples", "(Ljava/util/List;)V", "onDraw", "", "canvas", "Landroid/graphics/Canvas;", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class MetricsView extends View {
    private final Paint baseline;
    private final float density;
    private final Paint fill;
    private final Paint idle;
    private final Paint line;
    private List<CastMetric> samples;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MetricsView(Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        this.density = getResources().getDisplayMetrics().density;
        Paint paint = new Paint(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(this.density * 1.6f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(Palette.ACCENT);
        this.line = paint;
        Paint paint2 = new Paint(1);
        paint2.setStyle(Paint.Style.FILL);
        this.fill = paint2;
        Paint paint3 = new Paint(1);
        paint3.setColor(Palette.TRACK);
        paint3.setStrokeWidth(this.density * 1.0f);
        this.baseline = paint3;
        Paint paint4 = new Paint(1);
        paint4.setColor(Palette.TRACK);
        paint4.setStrokeWidth(this.density * 1.6f);
        paint4.setStrokeCap(Paint.Cap.ROUND);
        this.idle = paint4;
        this.samples = CollectionsKt.emptyList();
    }

    public final List<CastMetric> getSamples() {
        return this.samples;
    }

    public final void setSamples(List<CastMetric> value) {
        Intrinsics.checkNotNullParameter(value, "value");
        this.samples = value;
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        super.onDraw(canvas);
        float left = this.density;
        float right = getWidth() - this.density;
        float top = this.density * 3.0f;
        float bottom = getHeight() - (this.density * 2.0f);
        if (this.samples.size() < 2) {
            float y = (top + bottom) / 2.0f;
            canvas.drawLine(left, y, right, y, this.idle);
            return;
        }
        canvas.drawLine(left, bottom, right, bottom, this.baseline);
        Iterator<T> it = this.samples.iterator();
        if (!it.hasNext()) {
            throw new NoSuchElementException();
        }
        double fps = ((CastMetric) it.next()).getFps();
        while (it.hasNext()) {
            fps = Math.max(fps, ((CastMetric) it.next()).getFps());
        }
        double max = RangesKt.coerceAtLeast(fps, 1.0d);
        Path path = new Path();
        Path area = new Path();
        Iterable iterable = this.samples;
        int i = 0;
        for (Object obj : iterable) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            float left2 = left;
            float size = left2 + ((i * (right - left2)) / (this.samples.size() - 1));
            Iterable iterable2 = iterable;
            float top2 = top;
            float fps2 = bottom - ((float) (((double) (bottom - top)) * (((CastMetric) obj).getFps() / max)));
            if (i == 0) {
                path.moveTo(size, fps2);
                area.moveTo(size, bottom);
                area.lineTo(size, fps2);
            } else {
                path.lineTo(size, fps2);
                area.lineTo(size, fps2);
            }
            i = i2;
            left = left2;
            iterable = iterable2;
            top = top2;
        }
        area.lineTo(right, bottom);
        area.close();
        this.fill.setShader(new LinearGradient(0.0f, top, 0.0f, bottom, 1431080447, 5017087, Shader.TileMode.CLAMP));
        canvas.drawPath(area, this.fill);
        canvas.drawPath(path, this.line);
    }
}
