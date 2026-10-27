package com.carplay.airplay;

import java.util.Locale;
import kotlin.Metadata;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: AirPlayControlDiagnostics.kt */
/* JADX INFO: loaded from: classes10.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u001e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\t¨\u0006\n"}, d2 = {"Lcom/carplay/airplay/AirPlayControlDiagnostics;", "", "<init>", "()V", "request", "", "method", "path", "bytes", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AirPlayControlDiagnostics {
    public static final AirPlayControlDiagnostics INSTANCE = new AirPlayControlDiagnostics();

    private AirPlayControlDiagnostics() {
    }

    public final String request(String method, String path, int bytes) {
        Intrinsics.checkNotNullParameter(method, "method");
        Intrinsics.checkNotNullParameter(path, "path");
        String verb = SetsKt.setOf((Object[]) new String[]{"GET", "POST", "SETUP", "RECORD", "TEARDOWN", "OPTIONS", "GET_PARAMETER", "SET_PARAMETER"}).contains(method) ? method : null;
        if (verb == null) {
            verb = "OTHER";
        }
        String it = StringsKt.substringAfterLast$default(StringsKt.substringBefore$default(StringsKt.substringBefore$default(path, '?', (String) null, 2, (Object) null), '#', (String) null, 2, (Object) null), '/', (String) null, 2, (Object) null).toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(it, "toLowerCase(...)");
        String route = SetsKt.setOf((Object[]) new String[]{"info", "pair-setup", "pair-verify", "auth-setup", "fp-setup"}).contains(it) ? it : null;
        if (route == null) {
            route = "other";
        }
        return "airplay control request method=" + verb + " route=" + route + " contentBytes=" + bytes;
    }
}
