package com.carplay.iap2.catalog;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2Endpoint.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001BA\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f¢\u0006\u0004\b\u000e\u0010\u000fJ\t\u0010\u001b\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001d\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001e\u001a\u00020\bHÆ\u0003J\t\u0010\u001f\u001a\u00020\nHÆ\u0003J\u000f\u0010 \u001a\b\u0012\u0004\u0012\u00020\r0\fHÆ\u0003JK\u0010!\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fHÆ\u0001J\u0013\u0010\"\u001a\u00020#2\b\u0010$\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010%\u001a\u00020\u0003HÖ\u0001J\t\u0010&\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001a¨\u0006'"}, d2 = {"Lcom/carplay/iap2/catalog/Iap2Endpoint;", "", "id", "", "name", "", "feature", "direction", "Lcom/carplay/iap2/catalog/Iap2Direction;", "confidence", "Lcom/carplay/iap2/catalog/Iap2Confidence;", "fields", "", "Lcom/carplay/iap2/catalog/Iap2FieldSpec;", "<init>", "(ILjava/lang/String;Ljava/lang/String;Lcom/carplay/iap2/catalog/Iap2Direction;Lcom/carplay/iap2/catalog/Iap2Confidence;Ljava/util/List;)V", "getId", "()I", "getName", "()Ljava/lang/String;", "getFeature", "getDirection", "()Lcom/carplay/iap2/catalog/Iap2Direction;", "getConfidence", "()Lcom/carplay/iap2/catalog/Iap2Confidence;", "getFields", "()Ljava/util/List;", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "toString", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class Iap2Endpoint {
    private final Iap2Confidence confidence;
    private final Iap2Direction direction;
    private final String feature;
    private final List<Iap2FieldSpec> fields;
    private final int id;
    private final String name;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ Iap2Endpoint copy$default(Iap2Endpoint iap2Endpoint, int i, String str, String str2, Iap2Direction iap2Direction, Iap2Confidence iap2Confidence, List list, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = iap2Endpoint.id;
        }
        if ((i2 & 2) != 0) {
            str = iap2Endpoint.name;
        }
        if ((i2 & 4) != 0) {
            str2 = iap2Endpoint.feature;
        }
        if ((i2 & 8) != 0) {
            iap2Direction = iap2Endpoint.direction;
        }
        if ((i2 & 16) != 0) {
            iap2Confidence = iap2Endpoint.confidence;
        }
        if ((i2 & 32) != 0) {
            list = iap2Endpoint.fields;
        }
        Iap2Confidence iap2Confidence2 = iap2Confidence;
        List list2 = list;
        return iap2Endpoint.copy(i, str, str2, iap2Direction, iap2Confidence2, list2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getId() {
        return this.id;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getName() {
        return this.name;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getFeature() {
        return this.feature;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final Iap2Direction getDirection() {
        return this.direction;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final Iap2Confidence getConfidence() {
        return this.confidence;
    }

    public final List<Iap2FieldSpec> component6() {
        return this.fields;
    }

    public final Iap2Endpoint copy(int id, String name, String feature, Iap2Direction direction, Iap2Confidence confidence, List<Iap2FieldSpec> fields) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(feature, "feature");
        Intrinsics.checkNotNullParameter(direction, "direction");
        Intrinsics.checkNotNullParameter(confidence, "confidence");
        Intrinsics.checkNotNullParameter(fields, "fields");
        return new Iap2Endpoint(id, name, feature, direction, confidence, fields);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Iap2Endpoint)) {
            return false;
        }
        Iap2Endpoint iap2Endpoint = (Iap2Endpoint) other;
        return this.id == iap2Endpoint.id && Intrinsics.areEqual(this.name, iap2Endpoint.name) && Intrinsics.areEqual(this.feature, iap2Endpoint.feature) && this.direction == iap2Endpoint.direction && this.confidence == iap2Endpoint.confidence && Intrinsics.areEqual(this.fields, iap2Endpoint.fields);
    }

    public int hashCode() {
        return (((((((((Integer.hashCode(this.id) * 31) + this.name.hashCode()) * 31) + this.feature.hashCode()) * 31) + this.direction.hashCode()) * 31) + this.confidence.hashCode()) * 31) + this.fields.hashCode();
    }

    public String toString() {
        return "Iap2Endpoint(id=" + this.id + ", name=" + this.name + ", feature=" + this.feature + ", direction=" + this.direction + ", confidence=" + this.confidence + ", fields=" + this.fields + ")";
    }

    public Iap2Endpoint(int id, String name, String feature, Iap2Direction direction, Iap2Confidence confidence, List<Iap2FieldSpec> fields) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(feature, "feature");
        Intrinsics.checkNotNullParameter(direction, "direction");
        Intrinsics.checkNotNullParameter(confidence, "confidence");
        Intrinsics.checkNotNullParameter(fields, "fields");
        this.id = id;
        this.name = name;
        this.feature = feature;
        this.direction = direction;
        this.confidence = confidence;
        this.fields = fields;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ Iap2Endpoint(int i, String str, String str2, Iap2Direction iap2Direction, Iap2Confidence iap2Confidence, List list, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        Iap2Confidence iap2Confidence2;
        List listEmptyList;
        if ((i2 & 16) == 0) {
            iap2Confidence2 = iap2Confidence;
        } else {
            iap2Confidence2 = Iap2Confidence.HIGH;
        }
        if ((i2 & 32) == 0) {
            listEmptyList = list;
        } else {
            listEmptyList = CollectionsKt.emptyList();
        }
        this(i, str, str2, iap2Direction, iap2Confidence2, listEmptyList);
    }

    public final int getId() {
        return this.id;
    }

    public final String getName() {
        return this.name;
    }

    public final String getFeature() {
        return this.feature;
    }

    public final Iap2Direction getDirection() {
        return this.direction;
    }

    public final Iap2Confidence getConfidence() {
        return this.confidence;
    }

    public final List<Iap2FieldSpec> getFields() {
        return this.fields;
    }
}
