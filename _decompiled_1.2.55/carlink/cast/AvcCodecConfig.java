package com.carlink.cast;

import com.carplay.sender.VideoPackets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: AvcCodecConfig.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\bÀ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J%\u0010\u0006\u001a\u0004\u0018\u00010\u00052\u0016\u0010\u0007\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00050\b\"\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\tJ\u0016\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u000b2\u0006\u0010\f\u001a\u00020\u0005H\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0005H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lcom/carlink/cast/AvcCodecConfig;", "", "<init>", "()V", "startCode", "", "fromParameterSets", "buffers", "", "([[B)[B", "readNals", "", "bytes", "nalType", "", "nal", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AvcCodecConfig {
    public static final AvcCodecConfig INSTANCE = new AvcCodecConfig();
    private static final byte[] startCode = {0, 0, 0, 1};

    private AvcCodecConfig() {
    }

    public final byte[] fromParameterSets(byte[]... buffers) {
        Object next;
        Object next2;
        Object objM159constructorimpl;
        Intrinsics.checkNotNullParameter(buffers, "buffers");
        Iterable iterableFilterNotNull = ArraysKt.filterNotNull(buffers);
        Collection arrayList = new ArrayList();
        Iterator it = iterableFilterNotNull.iterator();
        while (it.hasNext()) {
            CollectionsKt.addAll(arrayList, readNals((byte[]) it.next()));
        }
        Iterable nals = (List) arrayList;
        Iterator it2 = nals.iterator();
        do {
            if (!it2.hasNext()) {
                next = null;
                break;
            }
            next = it2.next();
        } while (!(INSTANCE.nalType((byte[]) next) == 7));
        byte[] sps = (byte[]) next;
        if (sps == null) {
            return null;
        }
        Iterator it3 = nals.iterator();
        do {
            if (!it3.hasNext()) {
                next2 = null;
                break;
            }
            next2 = it3.next();
        } while (!(INSTANCE.nalType((byte[]) next2) == 8));
        byte[] pps = (byte[]) next2;
        if (pps == null) {
            return null;
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            AvcCodecConfig avcCodecConfig = this;
            objM159constructorimpl = Result.m159constructorimpl(VideoPackets.INSTANCE.avcC(ArraysKt.plus(startCode, sps), ArraysKt.plus(startCode, pps)));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        return (byte[]) (Result.m165isFailureimpl(objM159constructorimpl) ? null : objM159constructorimpl);
    }

    private final List<byte[]> readNals(byte[] bytes) {
        Object objM159constructorimpl;
        if (bytes.length == 0) {
            return CollectionsKt.emptyList();
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            AvcCodecConfig avcCodecConfig = this;
            objM159constructorimpl = Result.m159constructorimpl(VideoPackets.INSTANCE.nals(bytes));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = null;
        }
        List<byte[]> list = (List) objM159constructorimpl;
        if (list != null) {
            return list;
        }
        int iNalType = nalType(bytes);
        return 1 <= iNalType && iNalType < 24 ? CollectionsKt.listOf(bytes) : CollectionsKt.emptyList();
    }

    private final int nalType(byte[] nal) {
        Byte bFirstOrNull = ArraysKt.firstOrNull(nal);
        if (bFirstOrNull != null) {
            return bFirstOrNull.byteValue() & 31;
        }
        return 0;
    }
}
