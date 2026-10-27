package com.carlink.cast.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Theme.kt */
/* JADX INFO: loaded from: classes14.dex */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0004\u001a\u00020\u0005*\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0012\u0010\t\u001a\u00020\u0005*\u00020\u00062\u0006\u0010\n\u001a\u00020\u0005J\u0012\u0010\u000b\u001a\u00020\f*\u00020\u00062\u0006\u0010\r\u001a\u00020\u0005J\"\u0010\u000e\u001a\u00020\f*\u00020\u00062\u0006\u0010\r\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\bJ.\u0010\u0011\u001a\u00020\u0012*\u00020\u00132\u0006\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u00052\b\b\u0002\u0010\u0016\u001a\u00020\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u0017¨\u0006\u0019"}, d2 = {"Lcom/carlink/cast/ui/Ui;", "", "<init>", "()V", "dp", "", "Landroid/content/Context;", "value", "", "color", "res", "pill", "Landroid/graphics/drawable/GradientDrawable;", "fill", "panel", "stroke", "radiusDp", "style", "", "Landroid/widget/TextView;", "size", "colorRes", "bold", "", "mono", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class Ui {
    public static final Ui INSTANCE = new Ui();

    private Ui() {
    }

    public final int dp(Context $this$dp, float value) {
        Intrinsics.checkNotNullParameter($this$dp, "<this>");
        return (int) TypedValue.applyDimension(1, value, $this$dp.getResources().getDisplayMetrics());
    }

    public final int color(Context $this$color, int res) {
        Intrinsics.checkNotNullParameter($this$color, "<this>");
        return ContextCompat.getColor($this$color, res);
    }

    public final GradientDrawable pill(Context $this$pill, int fill) {
        Intrinsics.checkNotNullParameter($this$pill, "<this>");
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setCornerRadius(INSTANCE.dp($this$pill, 999.0f));
        gradientDrawable.setColor(INSTANCE.color($this$pill, fill));
        return gradientDrawable;
    }

    public final GradientDrawable panel(Context $this$panel, int fill, int stroke, float radiusDp) {
        Intrinsics.checkNotNullParameter($this$panel, "<this>");
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setCornerRadius(INSTANCE.dp($this$panel, radiusDp));
        gradientDrawable.setColor(INSTANCE.color($this$panel, fill));
        gradientDrawable.setStroke(INSTANCE.dp($this$panel, 1.0f), INSTANCE.color($this$panel, stroke));
        return gradientDrawable;
    }

    public static /* synthetic */ void style$default(Ui ui, TextView textView, float f, int i, boolean z, boolean z2, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            z = false;
        }
        if ((i2 & 8) != 0) {
            z2 = false;
        }
        ui.style(textView, f, i, z, z2);
    }

    public final void style(TextView $this$style, float size, int colorRes, boolean bold, boolean mono) {
        Typeface typefaceCreate;
        Intrinsics.checkNotNullParameter($this$style, "<this>");
        $this$style.setTextSize(2, size);
        Context context = $this$style.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "getContext(...)");
        $this$style.setTextColor(color(context, colorRes));
        if (mono) {
            typefaceCreate = Typeface.MONOSPACE;
        } else {
            typefaceCreate = bold ? Typeface.create("sans-serif-medium", 1) : Typeface.create("sans-serif", 0);
        }
        $this$style.setTypeface(typefaceCreate);
    }
}
