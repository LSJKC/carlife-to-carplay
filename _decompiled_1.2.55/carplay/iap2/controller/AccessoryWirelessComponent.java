package com.carplay.iap2.controller;

import kotlin.Metadata;

/* JADX INFO: compiled from: AccessoryWirelessComponents.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0010\n\u0002\u0010\u000e\n\u0000\b\u0080\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005¢\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0005HÆ\u0003J'\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\u00052\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\f¨\u0006\u0017"}, d2 = {"Lcom/carplay/iap2/controller/AccessoryWirelessComponent;", "", "id", "", "supported", "", "available", "<init>", "(IZZ)V", "getId", "()I", "getSupported", "()Z", "getAvailable", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "toString", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class AccessoryWirelessComponent {
    private final boolean available;
    private final int id;
    private final boolean supported;

    public static /* synthetic */ AccessoryWirelessComponent copy$default(AccessoryWirelessComponent accessoryWirelessComponent, int i, boolean z, boolean z2, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = accessoryWirelessComponent.id;
        }
        if ((i2 & 2) != 0) {
            z = accessoryWirelessComponent.supported;
        }
        if ((i2 & 4) != 0) {
            z2 = accessoryWirelessComponent.available;
        }
        return accessoryWirelessComponent.copy(i, z, z2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getId() {
        return this.id;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final boolean getSupported() {
        return this.supported;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final boolean getAvailable() {
        return this.available;
    }

    public final AccessoryWirelessComponent copy(int id, boolean supported, boolean available) {
        return new AccessoryWirelessComponent(id, supported, available);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AccessoryWirelessComponent)) {
            return false;
        }
        AccessoryWirelessComponent accessoryWirelessComponent = (AccessoryWirelessComponent) other;
        return this.id == accessoryWirelessComponent.id && this.supported == accessoryWirelessComponent.supported && this.available == accessoryWirelessComponent.available;
    }

    public int hashCode() {
        return (((Integer.hashCode(this.id) * 31) + Boolean.hashCode(this.supported)) * 31) + Boolean.hashCode(this.available);
    }

    public String toString() {
        return "AccessoryWirelessComponent(id=" + this.id + ", supported=" + this.supported + ", available=" + this.available + ")";
    }

    public AccessoryWirelessComponent(int id, boolean supported, boolean available) {
        this.id = id;
        this.supported = supported;
        this.available = available;
    }

    public final int getId() {
        return this.id;
    }

    public final boolean getSupported() {
        return this.supported;
    }

    public final boolean getAvailable() {
        return this.available;
    }
}
