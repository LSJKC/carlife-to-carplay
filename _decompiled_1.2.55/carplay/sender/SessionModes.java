package com.carplay.sender;

import com.carlink.cast.CastSettings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: SessionModes.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010%\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0002\u0015\u0016B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0016\u0010\b\u001a\u00020\t2\u000e\u0010\n\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u000bJ\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00010\u000bJ\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0010\u001a\u00020\u000fJ\u0006\u0010\u0011\u001a\u00020\u000fJ\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u000fR\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0017"}, d2 = {"Lcom/carplay/sender/SessionModes;", "", "<init>", "()V", "resources", "", "", "Lcom/carplay/sender/SessionModes$Resource;", "change", "Lcom/carplay/sender/SessionModes$Change;", "command", "", "snapshot", "", "claimScreen", "", "ownsScreen", "ownsAudio", CastSettings.KEY_AUDIO, "", "phone", "Resource", "Change", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class SessionModes {
    private final Map<Long, Resource> resources = MapsKt.mutableMapOf(TuplesKt.to(1L, new Resource(0, 0, 3, null)), TuplesKt.to(2L, new Resource(2, 2)));

    /* JADX INFO: compiled from: SessionModes.kt */
    @Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\u001b\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\u000f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\b\"\u0004\b\f\u0010\n¨\u0006\u0017"}, d2 = {"Lcom/carplay/sender/SessionModes$Resource;", "", "owner", "", "permanent", "<init>", "(JJ)V", "getOwner", "()J", "setOwner", "(J)V", "getPermanent", "setPermanent", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class Resource {
        private long owner;
        private long permanent;

        public Resource() {
            this(0L, 0L, 3, null);
        }

        public static /* synthetic */ Resource copy$default(Resource resource, long j, long j2, int i, Object obj) {
            if ((i & 1) != 0) {
                j = resource.owner;
            }
            if ((i & 2) != 0) {
                j2 = resource.permanent;
            }
            return resource.copy(j, j2);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final long getOwner() {
            return this.owner;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final long getPermanent() {
            return this.permanent;
        }

        public final Resource copy(long owner, long permanent) {
            return new Resource(owner, permanent);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Resource)) {
                return false;
            }
            Resource resource = (Resource) other;
            return this.owner == resource.owner && this.permanent == resource.permanent;
        }

        public int hashCode() {
            return (Long.hashCode(this.owner) * 31) + Long.hashCode(this.permanent);
        }

        public String toString() {
            return "Resource(owner=" + this.owner + ", permanent=" + this.permanent + ")";
        }

        public Resource(long owner, long permanent) {
            this.owner = owner;
            this.permanent = permanent;
        }

        public /* synthetic */ Resource(long j, long j2, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this((i & 1) != 0 ? 1L : j, (i & 2) != 0 ? 1L : j2);
        }

        public final long getOwner() {
            return this.owner;
        }

        public final long getPermanent() {
            return this.permanent;
        }

        public final void setOwner(long j) {
            this.owner = j;
        }

        public final void setPermanent(long j) {
            this.permanent = j;
        }
    }

    /* JADX INFO: compiled from: SessionModes.kt */
    @Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u000f\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B+\u0012\u0012\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006¢\u0006\u0004\b\b\u0010\tJ\u0015\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u0003HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0006HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0006HÆ\u0003J3\u0010\u0012\u001a\u00020\u00002\u0014\b\u0002\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u0013\u001a\u00020\u00062\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0004HÖ\u0001R\u001d\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0007\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\r¨\u0006\u0018"}, d2 = {"Lcom/carplay/sender/SessionModes$Change;", "", "reply", "", "", "screenRestored", "", "audioRestored", "<init>", "(Ljava/util/Map;ZZ)V", "getReply", "()Ljava/util/Map;", "getScreenRestored", "()Z", "getAudioRestored", "component1", "component2", "component3", "copy", "equals", "other", "hashCode", "", "toString", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final /* data */ class Change {
        private final boolean audioRestored;
        private final Map<String, Object> reply;
        private final boolean screenRestored;

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Change copy$default(Change change, Map map, boolean z, boolean z2, int i, Object obj) {
            if ((i & 1) != 0) {
                map = change.reply;
            }
            if ((i & 2) != 0) {
                z = change.screenRestored;
            }
            if ((i & 4) != 0) {
                z2 = change.audioRestored;
            }
            return change.copy(map, z, z2);
        }

        public final Map<String, Object> component1() {
            return this.reply;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final boolean getScreenRestored() {
            return this.screenRestored;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final boolean getAudioRestored() {
            return this.audioRestored;
        }

        public final Change copy(Map<String, ? extends Object> reply, boolean screenRestored, boolean audioRestored) {
            Intrinsics.checkNotNullParameter(reply, "reply");
            return new Change(reply, screenRestored, audioRestored);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Change)) {
                return false;
            }
            Change change = (Change) other;
            return Intrinsics.areEqual(this.reply, change.reply) && this.screenRestored == change.screenRestored && this.audioRestored == change.audioRestored;
        }

        public int hashCode() {
            return (((this.reply.hashCode() * 31) + Boolean.hashCode(this.screenRestored)) * 31) + Boolean.hashCode(this.audioRestored);
        }

        public String toString() {
            return "Change(reply=" + this.reply + ", screenRestored=" + this.screenRestored + ", audioRestored=" + this.audioRestored + ")";
        }

        public Change(Map<String, ? extends Object> reply, boolean screenRestored, boolean audioRestored) {
            Intrinsics.checkNotNullParameter(reply, "reply");
            this.reply = reply;
            this.screenRestored = screenRestored;
            this.audioRestored = audioRestored;
        }

        public final Map<String, Object> getReply() {
            return this.reply;
        }

        public final boolean getScreenRestored() {
            return this.screenRestored;
        }

        public final boolean getAudioRestored() {
            return this.audioRestored;
        }
    }

    public final synchronized Change change(Map<?, ?> command) {
        boolean screenRestored;
        boolean audioRestored;
        Intrinsics.checkNotNullParameter(command, "command");
        if (!Intrinsics.areEqual(command.get("type"), "changeModes")) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        Object obj = command.get("params");
        Map parameters = obj instanceof Map ? (Map) obj : null;
        if (parameters == null) {
            throw new IllegalStateException("changeModes 参数缺失".toString());
        }
        Object obj2 = parameters.get("resources");
        Iterable iterable = obj2 instanceof List ? (List) obj2 : null;
        if (iterable == null) {
            throw new IllegalStateException("资源请求缺失".toString());
        }
        Iterable $this$map$iv = iterable;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        Iterator it = $this$map$iv.iterator();
        while (true) {
            boolean z = true;
            if (!it.hasNext()) {
                List<Pair> requests = (List) destination$iv$iv;
                screenRestored = false;
                audioRestored = false;
                for (Pair pair : requests) {
                    long id = ((Number) pair.component1()).longValue();
                    int type = ((Number) pair.component2()).intValue();
                    Resource resource = (Resource) MapsKt.getValue(this.resources, Long.valueOf(id));
                    boolean wasPhone = resource.getOwner() == 1 ? z : false;
                    boolean wasPhone2 = wasPhone;
                    switch (type) {
                        case 1:
                            resource.setOwner(2L);
                            resource.setPermanent(2L);
                            break;
                        case 2:
                            resource.setOwner(1L);
                            resource.setPermanent(1L);
                            break;
                        case 3:
                            resource.setOwner(2L);
                            break;
                        case 4:
                            resource.setOwner(resource.getPermanent());
                            break;
                        default:
                            throw new IllegalStateException("不支持的资源转移类型".toString());
                    }
                    if (wasPhone2 || resource.getOwner() != 1) {
                        z = true;
                    } else {
                        if (id == 1) {
                            screenRestored = true;
                        }
                        if (id == 2) {
                            audioRestored = true;
                            z = true;
                        } else {
                            z = true;
                        }
                    }
                }
            } else {
                Object item$iv$iv = it.next();
                Map entry = item$iv$iv instanceof Map ? (Map) item$iv$iv : null;
                if (entry == null) {
                    throw new IllegalStateException("资源请求无效".toString());
                }
                Object obj3 = entry.get("resourceID");
                Number number = obj3 instanceof Number ? (Number) obj3 : null;
                if (number == null) {
                    throw new IllegalStateException("资源编号缺失".toString());
                }
                long id2 = number.longValue();
                if (!this.resources.containsKey(Long.valueOf(id2))) {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
                Object obj4 = entry.get("transferType");
                Number number2 = obj4 instanceof Number ? (Number) obj4 : null;
                if (number2 == null) {
                    throw new IllegalStateException("资源转移类型缺失".toString());
                }
                int type2 = number2.intValue();
                if (1 > type2 || type2 >= 5) {
                    z = false;
                }
                if (!z) {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
                destination$iv$iv.add(TuplesKt.to(Long.valueOf(id2), Integer.valueOf(type2)));
            }
        }
        return new Change(snapshot(), screenRestored, audioRestored);
    }

    public final synchronized Map<String, Object> snapshot() {
        Pair[] pairArr;
        pairArr = new Pair[2];
        int i = 0;
        pairArr[0] = TuplesKt.to("type", "modesChanged");
        Pair[] pairArr2 = new Pair[2];
        Map<Long, Resource> map = this.resources;
        Collection destination$iv$iv = new ArrayList(map.size());
        for (Map.Entry<Long, Resource> entry : map.entrySet()) {
            long id = entry.getKey().longValue();
            Resource resource = entry.getValue();
            Pair[] pairArr3 = new Pair[3];
            int i2 = i;
            pairArr3[i2] = TuplesKt.to("resourceID", Long.valueOf(id));
            pairArr3[1] = TuplesKt.to("entity", Long.valueOf(resource.getOwner()));
            pairArr3[2] = TuplesKt.to("permanentEntity", Long.valueOf(resource.getPermanent()));
            destination$iv$iv.add(MapsKt.mapOf(pairArr3));
            i = i2;
        }
        int i3 = i;
        pairArr2[i3] = TuplesKt.to("resources", (List) destination$iv$iv);
        Map[] mapArr = new Map[3];
        Pair[] pairArr4 = new Pair[2];
        pairArr4[i3] = TuplesKt.to("appStateID", 2);
        pairArr4[1] = TuplesKt.to("entity", Integer.valueOf(i3));
        mapArr[i3] = MapsKt.mapOf(pairArr4);
        Pair[] pairArr5 = new Pair[2];
        pairArr5[i3] = TuplesKt.to("appStateID", 3);
        pairArr5[1] = TuplesKt.to("entity", Integer.valueOf(i3));
        mapArr[1] = MapsKt.mapOf(pairArr5);
        Pair[] pairArr6 = new Pair[3];
        pairArr6[i3] = TuplesKt.to("appStateID", 1);
        pairArr6[1] = TuplesKt.to("entity", Integer.valueOf(i3));
        pairArr6[2] = TuplesKt.to("speechMode", -1);
        mapArr[2] = MapsKt.mapOf(pairArr6);
        pairArr2[1] = TuplesKt.to("appStates", CollectionsKt.listOf((Object[]) mapArr));
        pairArr[1] = TuplesKt.to("params", MapsKt.mapOf(pairArr2));
        return MapsKt.mapOf(pairArr);
    }

    public final synchronized boolean claimScreen() {
        Resource resource = (Resource) MapsKt.getValue(this.resources, 1L);
        if (resource.getOwner() == 1) {
            return false;
        }
        resource.setOwner(1L);
        resource.setPermanent(1L);
        return true;
    }

    public final synchronized boolean ownsScreen() {
        return ((Resource) MapsKt.getValue(this.resources, 1L)).getOwner() == 1;
    }

    public final synchronized boolean ownsAudio() {
        return ((Resource) MapsKt.getValue(this.resources, 2L)).getOwner() == 1;
    }

    public final synchronized void audio(boolean phone) {
        Resource $this$audio_u24lambda_u242 = (Resource) MapsKt.getValue(this.resources, 2L);
        $this$audio_u24lambda_u242.setOwner(phone ? 1L : 2L);
        $this$audio_u24lambda_u242.setPermanent($this$audio_u24lambda_u242.getOwner());
    }
}
