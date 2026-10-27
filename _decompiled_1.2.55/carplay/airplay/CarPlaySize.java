package com.carplay.airplay;

import com.google.android.material.card.MaterialCardViewHelper;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.bouncycastle.jcajce.util.AnnotatedPrivateKey;

/* JADX INFO: compiled from: CarPlaySize.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u000b\b\u0086\u0081\u0002\u0018\u0000 \u000f2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u000fB\u0019\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000e¨\u0006\u0010"}, d2 = {"Lcom/carplay/airplay/CarPlaySize;", "", AnnotatedPrivateKey.LABEL, "", "widthMillimeters", "", "<init>", "(Ljava/lang/String;ILjava/lang/String;I)V", "getLabel", "()Ljava/lang/String;", "getWidthMillimeters", "()I", "LARGE", "MEDIUM", "SMALL", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public enum CarPlaySize {
    LARGE("Large", 250),
    MEDIUM("Medium", MaterialCardViewHelper.DEFAULT_FADE_ANIM_DURATION),
    SMALL("Small", 350);

    private final String label;
    private final int widthMillimeters;
    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final CarPlaySize DEFAULT = MEDIUM;

    public static EnumEntries<CarPlaySize> getEntries() {
        return $ENTRIES;
    }

    CarPlaySize(String label, int widthMillimeters) {
        this.label = label;
        this.widthMillimeters = widthMillimeters;
    }

    public final String getLabel() {
        return this.label;
    }

    public final int getWidthMillimeters() {
        return this.widthMillimeters;
    }

    /* JADX INFO: compiled from: CarPlaySize.kt */
    @Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\nR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u000b"}, d2 = {"Lcom/carplay/airplay/CarPlaySize$Companion;", "", "<init>", "()V", "DEFAULT", "Lcom/carplay/airplay/CarPlaySize;", "getDEFAULT", "()Lcom/carplay/airplay/CarPlaySize;", "fromWidthMillimeters", "millimeters", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final CarPlaySize getDEFAULT() {
            return CarPlaySize.DEFAULT;
        }

        public final CarPlaySize fromWidthMillimeters(int millimeters) {
            Iterable $this$minBy$iv = CarPlaySize.getEntries();
            Iterator<CarPlaySize> it = $this$minBy$iv.iterator();
            if (!it.hasNext()) {
                throw new NoSuchElementException();
            }
            Object minElem$iv = it.next();
            if (it.hasNext()) {
                CarPlaySize it2 = (CarPlaySize) minElem$iv;
                int minValue$iv = Math.abs(it2.getWidthMillimeters() - millimeters);
                do {
                    Object e$iv = it.next();
                    CarPlaySize it3 = (CarPlaySize) e$iv;
                    int v$iv = Math.abs(it3.getWidthMillimeters() - millimeters);
                    if (minValue$iv > v$iv) {
                        minElem$iv = e$iv;
                        minValue$iv = v$iv;
                    }
                } while (it.hasNext());
            }
            return (CarPlaySize) minElem$iv;
        }
    }
}
