package com.carplay.adapter;

import android.content.Intent;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PhoneProjectionAdapter.kt */
/* JADX INFO: loaded from: classes11.dex */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0015"}, d2 = {"Lcom/carplay/adapter/ProjectionGrant;", "", "resultCode", "", "data", "Landroid/content/Intent;", "<init>", "(ILandroid/content/Intent;)V", "getResultCode", "()I", "getData", "()Landroid/content/Intent;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class ProjectionGrant {
    private final Intent data;
    private final int resultCode;

    public static /* synthetic */ ProjectionGrant copy$default(ProjectionGrant projectionGrant, int i, Intent intent, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = projectionGrant.resultCode;
        }
        if ((i2 & 2) != 0) {
            intent = projectionGrant.data;
        }
        return projectionGrant.copy(i, intent);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getResultCode() {
        return this.resultCode;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final Intent getData() {
        return this.data;
    }

    public final ProjectionGrant copy(int resultCode, Intent data) {
        Intrinsics.checkNotNullParameter(data, "data");
        return new ProjectionGrant(resultCode, data);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ProjectionGrant)) {
            return false;
        }
        ProjectionGrant projectionGrant = (ProjectionGrant) other;
        return this.resultCode == projectionGrant.resultCode && Intrinsics.areEqual(this.data, projectionGrant.data);
    }

    public int hashCode() {
        return (Integer.hashCode(this.resultCode) * 31) + this.data.hashCode();
    }

    public String toString() {
        return "ProjectionGrant(resultCode=" + this.resultCode + ", data=" + this.data + ")";
    }

    public ProjectionGrant(int resultCode, Intent data) {
        Intrinsics.checkNotNullParameter(data, "data");
        this.resultCode = resultCode;
        this.data = data;
    }

    public final Intent getData() {
        return this.data;
    }

    public final int getResultCode() {
        return this.resultCode;
    }
}
