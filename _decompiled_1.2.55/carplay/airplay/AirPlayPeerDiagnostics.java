package com.carplay.airplay;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import org.bouncycastle.i18n.ErrorBundle;

/* JADX INFO: compiled from: AirPlayPeerDiagnostics.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u001e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lcom/carplay/airplay/AirPlayPeerDiagnostics;", "", "<init>", "()V", "modelToken", "Lkotlin/text/Regex;", "versionToken", ErrorBundle.SUMMARY_ENTRY, "", "model", "osVersion", "sourceVersion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AirPlayPeerDiagnostics {
    public static final AirPlayPeerDiagnostics INSTANCE = new AirPlayPeerDiagnostics();
    private static final Regex modelToken = new Regex("(?:iPhone|iPad|iPod)[0-9]{1,3},[0-9]{1,3}");
    private static final Regex versionToken = new Regex("[0-9][0-9A-Za-z._-]{0,39}");

    private AirPlayPeerDiagnostics() {
    }

    private static final String summary$version(String value) {
        String p0 = value;
        String str = versionToken.matches(p0) ? value : null;
        return str == null ? "not_reported" : str;
    }

    public final String summary(String model, String osVersion, String sourceVersion) {
        Intrinsics.checkNotNullParameter(model, "model");
        Intrinsics.checkNotNullParameter(osVersion, "osVersion");
        Intrinsics.checkNotNullParameter(sourceVersion, "sourceVersion");
        String p0 = model;
        String str = modelToken.matches(p0) ? model : null;
        if (str == null) {
            str = "not_reported";
        }
        return "Phone display peer model=" + str + " ios=" + summary$version(osVersion) + " airplay=" + summary$version(sourceVersion);
    }
}
