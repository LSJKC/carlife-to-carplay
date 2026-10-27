package com.carplay.adapter;

import com.carlink.cast.CastSettings;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: PhoneProjectionAdapter.kt */
/* JADX INFO: loaded from: classes11.dex */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0012\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0004\b\t\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0016\u001a\u00020\bHÆ\u0003J;\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\bHÆ\u0001J\u0013\u0010\u0018\u001a\u00020\u00032\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001a\u001a\u00020\u001bHÖ\u0001J\t\u0010\u001c\u001a\u00020\bHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\fR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\fR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u001d"}, d2 = {"Lcom/carplay/adapter/ProjectionCapabilities;", "", "video", "", CastSettings.KEY_AUDIO, "input", "microphone", "detail", "", "<init>", "(ZZZZLjava/lang/String;)V", "getVideo", "()Z", "getAudio", "getInput", "getMicrophone", "getDetail", "()Ljava/lang/String;", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "", "toString", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class ProjectionCapabilities {
    private final boolean audio;
    private final String detail;
    private final boolean input;
    private final boolean microphone;
    private final boolean video;

    public static /* synthetic */ ProjectionCapabilities copy$default(ProjectionCapabilities projectionCapabilities, boolean z, boolean z2, boolean z3, boolean z4, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            z = projectionCapabilities.video;
        }
        if ((i & 2) != 0) {
            z2 = projectionCapabilities.audio;
        }
        if ((i & 4) != 0) {
            z3 = projectionCapabilities.input;
        }
        if ((i & 8) != 0) {
            z4 = projectionCapabilities.microphone;
        }
        if ((i & 16) != 0) {
            str = projectionCapabilities.detail;
        }
        String str2 = str;
        boolean z5 = z3;
        return projectionCapabilities.copy(z, z2, z5, z4, str2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final boolean getVideo() {
        return this.video;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final boolean getAudio() {
        return this.audio;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final boolean getInput() {
        return this.input;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final boolean getMicrophone() {
        return this.microphone;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final String getDetail() {
        return this.detail;
    }

    public final ProjectionCapabilities copy(boolean video, boolean audio, boolean input, boolean microphone, String detail) {
        Intrinsics.checkNotNullParameter(detail, "detail");
        return new ProjectionCapabilities(video, audio, input, microphone, detail);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ProjectionCapabilities)) {
            return false;
        }
        ProjectionCapabilities projectionCapabilities = (ProjectionCapabilities) other;
        return this.video == projectionCapabilities.video && this.audio == projectionCapabilities.audio && this.input == projectionCapabilities.input && this.microphone == projectionCapabilities.microphone && Intrinsics.areEqual(this.detail, projectionCapabilities.detail);
    }

    public int hashCode() {
        return (((((((Boolean.hashCode(this.video) * 31) + Boolean.hashCode(this.audio)) * 31) + Boolean.hashCode(this.input)) * 31) + Boolean.hashCode(this.microphone)) * 31) + this.detail.hashCode();
    }

    public String toString() {
        return "ProjectionCapabilities(video=" + this.video + ", audio=" + this.audio + ", input=" + this.input + ", microphone=" + this.microphone + ", detail=" + this.detail + ")";
    }

    public ProjectionCapabilities(boolean video, boolean audio, boolean input, boolean microphone, String detail) {
        Intrinsics.checkNotNullParameter(detail, "detail");
        this.video = video;
        this.audio = audio;
        this.input = input;
        this.microphone = microphone;
        this.detail = detail;
    }

    public final boolean getAudio() {
        return this.audio;
    }

    public final String getDetail() {
        return this.detail;
    }

    public final boolean getInput() {
        return this.input;
    }

    public final boolean getMicrophone() {
        return this.microphone;
    }

    public final boolean getVideo() {
        return this.video;
    }
}
