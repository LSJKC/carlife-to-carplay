package com.carplay.iap2.controller;

import java.util.Locale;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: BluetoothTransportIdentity.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0005R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\nX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lcom/carplay/iap2/controller/BluetoothTransportIdentity;", "", "<init>", "()V", "normalizedAddress", "", "value", "ADDRESS", "Lkotlin/text/Regex;", "RESERVED", "", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class BluetoothTransportIdentity {
    public static final BluetoothTransportIdentity INSTANCE = new BluetoothTransportIdentity();
    private static final Regex ADDRESS = new Regex("(?:[0-9A-F]{2}:){5}[0-9A-F]{2}");
    private static final Set<String> RESERVED = SetsKt.setOf((Object[]) new String[]{"02:00:00:00:00:00", "00:00:00:00:00:00", "FF:FF:FF:FF:FF:FF"});

    private BluetoothTransportIdentity() {
    }

    public final String normalizedAddress(String value) {
        String string;
        if (value != null && (string = StringsKt.trim((CharSequence) value).toString()) != null) {
            Locale ROOT = Locale.ROOT;
            Intrinsics.checkNotNullExpressionValue(ROOT, "ROOT");
            String address = string.toUpperCase(ROOT);
            Intrinsics.checkNotNullExpressionValue(address, "toUpperCase(...)");
            if (address == null || !ADDRESS.matches(address) || RESERVED.contains(address)) {
                return null;
            }
            return address;
        }
        return null;
    }
}
