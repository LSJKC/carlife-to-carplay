package com.carplay.adapter.carlife;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: CarLifeSession.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0000\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000¨\u0006\u0004"}, d2 = {"isCarLifeKeyFrame", "", "data", "", "adapter-carlife"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class CarLifeSessionKt {
    public static final boolean isCarLifeKeyFrame(byte[] data) {
        Intrinsics.checkNotNullParameter(data, "data");
        int i = 0;
        while (true) {
            int startCodeSize = 0;
            if (i >= data.length) {
                return false;
            }
            if (i + 4 < data.length && data[i] == 0 && data[i + 1] == 0 && data[i + 2] == 0 && data[i + 3] == 1) {
                startCodeSize = 4;
            } else if (i + 3 < data.length && data[i] == 0 && data[i + 1] == 0 && data[i + 2] == 1) {
                startCodeSize = 3;
            }
            if (startCodeSize != 0) {
                int header = i + startCodeSize;
                if (header < data.length && (data[header] & 31) == 5) {
                    return true;
                }
                i = header;
            } else {
                i++;
            }
        }
    }
}
