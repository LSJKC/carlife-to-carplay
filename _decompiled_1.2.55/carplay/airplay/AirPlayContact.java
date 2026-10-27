package com.carplay.airplay;

import kotlin.Metadata;

/* JADX INFO: compiled from: AirPlayHid.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0012\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B'\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0004\b\t\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0015\u001a\u00020\bHÆ\u0003J1\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\bHÆ\u0001J\u0013\u0010\u0017\u001a\u00020\b2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0019\u001a\u00020\u0003HÖ\u0001J\t\u0010\u001a\u001a\u00020\u001bHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u001c"}, d2 = {"Lcom/carplay/airplay/AirPlayContact;", "", "id", "", "x", "", "y", "down", "", "<init>", "(IDDZ)V", "getId", "()I", "getX", "()D", "getY", "getDown", "()Z", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class AirPlayContact {
    private final boolean down;
    private final int id;
    private final double x;
    private final double y;

    public static /* synthetic */ AirPlayContact copy$default(AirPlayContact airPlayContact, int i, double d, double d2, boolean z, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = airPlayContact.id;
        }
        if ((i2 & 2) != 0) {
            d = airPlayContact.x;
        }
        if ((i2 & 4) != 0) {
            d2 = airPlayContact.y;
        }
        if ((i2 & 8) != 0) {
            z = airPlayContact.down;
        }
        boolean z2 = z;
        return airPlayContact.copy(i, d, d2, z2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getId() {
        return this.id;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final double getX() {
        return this.x;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final double getY() {
        return this.y;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final boolean getDown() {
        return this.down;
    }

    public final AirPlayContact copy(int id, double x, double y, boolean down) {
        return new AirPlayContact(id, x, y, down);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AirPlayContact)) {
            return false;
        }
        AirPlayContact airPlayContact = (AirPlayContact) other;
        return this.id == airPlayContact.id && Double.compare(this.x, airPlayContact.x) == 0 && Double.compare(this.y, airPlayContact.y) == 0 && this.down == airPlayContact.down;
    }

    public int hashCode() {
        return (((((Integer.hashCode(this.id) * 31) + Double.hashCode(this.x)) * 31) + Double.hashCode(this.y)) * 31) + Boolean.hashCode(this.down);
    }

    public String toString() {
        return "AirPlayContact(id=" + this.id + ", x=" + this.x + ", y=" + this.y + ", down=" + this.down + ")";
    }

    public AirPlayContact(int id, double x, double y, boolean down) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.down = down;
    }

    public final int getId() {
        return this.id;
    }

    public final double getX() {
        return this.x;
    }

    public final double getY() {
        return this.y;
    }

    public final boolean getDown() {
        return this.down;
    }
}
