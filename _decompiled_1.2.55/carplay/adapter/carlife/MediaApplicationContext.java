package com.carplay.adapter.carlife;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CarLifeMediaPublisher.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0080\b\u0018\u00002\u00020\u0001B/\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003¢\u0006\u0004\b\b\u0010\tJ\b\u0010\u0010\u001a\u00020\u0003H\u0016J\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J1\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0019\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000bR\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000b¨\u0006\u001a"}, d2 = {"Lcom/carplay/adapter/carlife/MediaApplicationContext;", "", "packageName", "", "pid", "", "appName", "audioCategory", "<init>", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;)V", "getPackageName", "()Ljava/lang/String;", "getPid", "()I", "getAppName", "getAudioCategory", "toString", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "adapter-carlife"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class MediaApplicationContext {
    private final String appName;
    private final String audioCategory;
    private final String packageName;
    private final int pid;

    public MediaApplicationContext() {
        this(null, 0, null, null, 15, null);
    }

    public static /* synthetic */ MediaApplicationContext copy$default(MediaApplicationContext mediaApplicationContext, String str, int i, String str2, String str3, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            str = mediaApplicationContext.packageName;
        }
        if ((i2 & 2) != 0) {
            i = mediaApplicationContext.pid;
        }
        if ((i2 & 4) != 0) {
            str2 = mediaApplicationContext.appName;
        }
        if ((i2 & 8) != 0) {
            str3 = mediaApplicationContext.audioCategory;
        }
        return mediaApplicationContext.copy(str, i, str2, str3);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getPackageName() {
        return this.packageName;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final int getPid() {
        return this.pid;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getAppName() {
        return this.appName;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final String getAudioCategory() {
        return this.audioCategory;
    }

    public final MediaApplicationContext copy(String packageName, int pid, String appName, String audioCategory) {
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        Intrinsics.checkNotNullParameter(appName, "appName");
        Intrinsics.checkNotNullParameter(audioCategory, "audioCategory");
        return new MediaApplicationContext(packageName, pid, appName, audioCategory);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MediaApplicationContext)) {
            return false;
        }
        MediaApplicationContext mediaApplicationContext = (MediaApplicationContext) other;
        return Intrinsics.areEqual(this.packageName, mediaApplicationContext.packageName) && this.pid == mediaApplicationContext.pid && Intrinsics.areEqual(this.appName, mediaApplicationContext.appName) && Intrinsics.areEqual(this.audioCategory, mediaApplicationContext.audioCategory);
    }

    public int hashCode() {
        return (((((this.packageName.hashCode() * 31) + Integer.hashCode(this.pid)) * 31) + this.appName.hashCode()) * 31) + this.audioCategory.hashCode();
    }

    public MediaApplicationContext(String packageName, int pid, String appName, String audioCategory) {
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        Intrinsics.checkNotNullParameter(appName, "appName");
        Intrinsics.checkNotNullParameter(audioCategory, "audioCategory");
        this.packageName = packageName;
        this.pid = pid;
        this.appName = appName;
        this.audioCategory = audioCategory;
    }

    public /* synthetic */ MediaApplicationContext(String str, int i, String str2, String str3, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? "" : str, (i2 & 2) != 0 ? 0 : i, (i2 & 4) != 0 ? "" : str2, (i2 & 8) != 0 ? "default" : str3);
    }

    public final String getPackageName() {
        return this.packageName;
    }

    public final int getPid() {
        return this.pid;
    }

    public final String getAppName() {
        return this.appName;
    }

    public final String getAudioCategory() {
        return this.audioCategory;
    }

    public String toString() {
        return "【 LOCAL (Android) ❯ " + this.packageName + " (" + this.pid + ") " + this.appName + " ❯ " + this.audioCategory + " 】";
    }
}
