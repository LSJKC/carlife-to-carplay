package com.carlink.cast;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.core.os.EnvironmentCompat;
import com.carplay.sender.SenderAudioFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: AudioProfileMemory.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0000\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u001a\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000eJ\u000e\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \n*\u0004\u0018\u00010\t0\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lcom/carlink/cast/AudioProfileMemory;", "", "context", "Landroid/content/Context;", "receiver", "", "<init>", "(Landroid/content/Context;Ljava/lang/String;)V", "prefs", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "scope", "order", "", "Lcom/carplay/sender/SenderAudioFormat;", "candidates", "recordWorking", "", "profile", "recordRejected", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AudioProfileMemory {
    private final SharedPreferences prefs;
    private final String receiver;
    private final String scope;

    public AudioProfileMemory(Context context, String receiver) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(receiver, "receiver");
        this.receiver = receiver;
        this.prefs = context.getApplicationContext().getSharedPreferences("carlink_audio_profiles", 0);
        String lowerCase = this.receiver.toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        String strTake = StringsKt.take(StringsKt.trim(new Regex("[^a-z0-9]+").replace(lowerCase, "_"), '_'), 64);
        this.scope = StringsKt.isBlank(strTake) ? EnvironmentCompat.MEDIA_UNKNOWN : strTake;
    }

    public final List<SenderAudioFormat> order(List<SenderAudioFormat> candidates) {
        Set rejected;
        Object next;
        Iterable iterableSplit$default;
        Intrinsics.checkNotNullParameter(candidates, "candidates");
        if (candidates.isEmpty()) {
            return CollectionsKt.emptyList();
        }
        String key = null;
        String working = this.prefs.getString(this.scope + ":working", null);
        String string = this.prefs.getString(this.scope + ":rejected", null);
        if (string == null || (iterableSplit$default = StringsKt.split$default((CharSequence) string, new char[]{','}, false, 0, 6, (Object) null)) == null) {
            rejected = null;
        } else {
            Iterable iterable = iterableSplit$default;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            Iterator it = iterable.iterator();
            while (it.hasNext()) {
                arrayList.add(StringsKt.trim((CharSequence) it.next()).toString());
            }
            Collection arrayList2 = new ArrayList();
            for (Object obj : (List) arrayList) {
                if (((String) obj).length() > 0) {
                    arrayList2.add(obj);
                }
            }
            rejected = CollectionsKt.toSet((List) arrayList2);
        }
        if (rejected == null) {
            rejected = SetsKt.emptySet();
        }
        Iterator it2 = candidates.iterator();
        do {
            if (!it2.hasNext()) {
                next = null;
                break;
            }
            next = it2.next();
        } while (!Intrinsics.areEqual(((SenderAudioFormat) next).getKey(), working));
        SenderAudioFormat preferred = (SenderAudioFormat) next;
        Collection arrayList3 = new ArrayList();
        for (Object obj2 : candidates) {
            String key2 = ((SenderAudioFormat) obj2).getKey();
            if (preferred != null) {
                key = preferred.getKey();
            }
            if (!Intrinsics.areEqual(key2, key)) {
                arrayList3.add(obj2);
            }
            key = null;
        }
        Iterable rest = (List) arrayList3;
        Collection arrayList4 = new ArrayList();
        for (Object obj3 : rest) {
            if (!rejected.contains(((SenderAudioFormat) obj3).getKey())) {
                arrayList4.add(obj3);
            }
        }
        List safe = (List) arrayList4;
        Collection arrayList5 = new ArrayList();
        for (Object obj4 : rest) {
            if (rejected.contains(((SenderAudioFormat) obj4).getKey())) {
                arrayList5.add(obj4);
            }
        }
        List risky = (List) arrayList5;
        Iterable iterablePlus = CollectionsKt.plus((Collection) CollectionsKt.plus((Collection) CollectionsKt.listOfNotNull(preferred), (Iterable) safe), (Iterable) risky);
        HashSet hashSet = new HashSet();
        ArrayList arrayList6 = new ArrayList();
        for (Object obj5 : iterablePlus) {
            if (hashSet.add(((SenderAudioFormat) obj5).getKey())) {
                arrayList6.add(obj5);
            }
        }
        return arrayList6;
    }

    public final void recordWorking(SenderAudioFormat profile) {
        Iterable iterableSplit$default;
        Intrinsics.checkNotNullParameter(profile, "profile");
        String key = profile.getKey();
        String rejected = null;
        String string = this.prefs.getString(this.scope + ":rejected", null);
        if (string != null && (iterableSplit$default = StringsKt.split$default((CharSequence) string, new char[]{','}, false, 0, 6, (Object) null)) != null) {
            Iterable iterable = iterableSplit$default;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            Iterator it = iterable.iterator();
            while (it.hasNext()) {
                arrayList.add(StringsKt.trim((CharSequence) it.next()).toString());
            }
            Collection arrayList2 = new ArrayList();
            for (Object obj : (List) arrayList) {
                String str = (String) obj;
                if ((str.length() > 0) && !Intrinsics.areEqual(str, key)) {
                    arrayList2.add(obj);
                }
            }
            List listDistinct = CollectionsKt.distinct((List) arrayList2);
            if (listDistinct != null) {
                rejected = CollectionsKt.joinToString$default(listDistinct, ",", null, null, 0, null, null, 62, null);
            }
        }
        if (rejected == null) {
            rejected = "";
        }
        this.prefs.edit().putString(this.scope + ":working", key).putString(this.scope + ":rejected", rejected).apply();
    }

    /* JADX WARN: Code duplicated, block: B:22:0x00bb  */
    public final void recordRejected(SenderAudioFormat profile) {
        ArrayList rejected;
        Iterable iterableSplit$default;
        Intrinsics.checkNotNullParameter(profile, "profile");
        final String key = profile.getKey();
        String string = this.prefs.getString(this.scope + ":rejected", null);
        if (string == null || (iterableSplit$default = StringsKt.split$default((CharSequence) string, new char[]{','}, false, 0, 6, (Object) null)) == null) {
            rejected = new ArrayList();
        } else {
            Iterable iterable = iterableSplit$default;
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            Iterator it = iterable.iterator();
            while (it.hasNext()) {
                arrayList.add(StringsKt.trim((CharSequence) it.next()).toString());
            }
            Collection arrayList2 = new ArrayList();
            for (Object obj : (List) arrayList) {
                if (((String) obj).length() > 0) {
                    arrayList2.add(obj);
                }
            }
            rejected = CollectionsKt.toMutableList(arrayList2);
            if (rejected == null) {
                rejected = new ArrayList();
            }
        }
        CollectionsKt.removeAll(rejected, new Function1() { // from class: com.carlink.cast.AudioProfileMemory$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj2) {
                return Boolean.valueOf(AudioProfileMemory.recordRejected$lambda$12(key, (String) obj2));
            }
        });
        rejected.add(key);
        this.prefs.edit().putString(this.scope + ":rejected", CollectionsKt.joinToString$default(CollectionsKt.distinct(rejected), ",", null, null, 0, null, null, 62, null)).apply();
    }

    static final boolean recordRejected$lambda$12(String $key, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Intrinsics.areEqual(it, $key);
    }
}
