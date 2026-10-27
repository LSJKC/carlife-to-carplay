package com.carplay.airplay;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: AirPlayHid.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0017\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001BC\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\u0007¢\u0006\u0004\b\n\u0010\u000bJ\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0007HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0007HÆ\u0003J\t\u0010\u0019\u001a\u00020\u0007HÆ\u0003JE\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u0007HÆ\u0001J\u0013\u0010\u001b\u001a\u00020\u00032\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001d\u001a\u00020\u0007HÖ\u0001J\t\u0010\u001e\u001a\u00020\u001fHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\b\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\t\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011¨\u0006 "}, d2 = {"Lcom/carplay/airplay/AirPlayKnobState;", "", "select", "", "home", "back", "x", "", "y", "wheel", "<init>", "(ZZZIII)V", "getSelect", "()Z", "getHome", "getBack", "getX", "()I", "getY", "getWheel", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class AirPlayKnobState {
    private final boolean back;
    private final boolean home;
    private final boolean select;
    private final int wheel;
    private final int x;
    private final int y;

    public AirPlayKnobState() {
        this(false, false, false, 0, 0, 0, 63, null);
    }

    public static /* synthetic */ AirPlayKnobState copy$default(AirPlayKnobState airPlayKnobState, boolean z, boolean z2, boolean z3, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 1) != 0) {
            z = airPlayKnobState.select;
        }
        if ((i4 & 2) != 0) {
            z2 = airPlayKnobState.home;
        }
        if ((i4 & 4) != 0) {
            z3 = airPlayKnobState.back;
        }
        if ((i4 & 8) != 0) {
            i = airPlayKnobState.x;
        }
        if ((i4 & 16) != 0) {
            i2 = airPlayKnobState.y;
        }
        if ((i4 & 32) != 0) {
            i3 = airPlayKnobState.wheel;
        }
        int i5 = i2;
        int i6 = i3;
        return airPlayKnobState.copy(z, z2, z3, i, i5, i6);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final boolean getSelect() {
        return this.select;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final boolean getHome() {
        return this.home;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final boolean getBack() {
        return this.back;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int getX() {
        return this.x;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final int getY() {
        return this.y;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final int getWheel() {
        return this.wheel;
    }

    public final AirPlayKnobState copy(boolean select, boolean home, boolean back, int x, int y, int wheel) {
        return new AirPlayKnobState(select, home, back, x, y, wheel);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AirPlayKnobState)) {
            return false;
        }
        AirPlayKnobState airPlayKnobState = (AirPlayKnobState) other;
        return this.select == airPlayKnobState.select && this.home == airPlayKnobState.home && this.back == airPlayKnobState.back && this.x == airPlayKnobState.x && this.y == airPlayKnobState.y && this.wheel == airPlayKnobState.wheel;
    }

    public int hashCode() {
        return (((((((((Boolean.hashCode(this.select) * 31) + Boolean.hashCode(this.home)) * 31) + Boolean.hashCode(this.back)) * 31) + Integer.hashCode(this.x)) * 31) + Integer.hashCode(this.y)) * 31) + Integer.hashCode(this.wheel);
    }

    public String toString() {
        return "AirPlayKnobState(select=" + this.select + ", home=" + this.home + ", back=" + this.back + ", x=" + this.x + ", y=" + this.y + ", wheel=" + this.wheel + ")";
    }

    public AirPlayKnobState(boolean select, boolean home, boolean back, int x, int y, int wheel) {
        this.select = select;
        this.home = home;
        this.back = back;
        this.x = x;
        this.y = y;
        this.wheel = wheel;
    }

    public /* synthetic */ AirPlayKnobState(boolean z, boolean z2, boolean z3, int i, int i2, int i3, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this((i4 & 1) != 0 ? false : z, (i4 & 2) != 0 ? false : z2, (i4 & 4) != 0 ? false : z3, (i4 & 8) != 0 ? 0 : i, (i4 & 16) != 0 ? 0 : i2, (i4 & 32) != 0 ? 0 : i3);
    }

    public final boolean getSelect() {
        return this.select;
    }

    public final boolean getHome() {
        return this.home;
    }

    public final boolean getBack() {
        return this.back;
    }

    public final int getX() {
        return this.x;
    }

    public final int getY() {
        return this.y;
    }

    public final int getWheel() {
        return this.wheel;
    }
}
