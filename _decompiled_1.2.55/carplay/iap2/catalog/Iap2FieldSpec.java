package com.carplay.iap2.catalog;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2Endpoint.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0019\b\u0086\b\u0018\u00002\u00020\u0001BC\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\t\u0012\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00000\f¢\u0006\u0004\b\r\u0010\u000eJ\t\u0010\u001a\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001b\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0007HÆ\u0003J\t\u0010\u001d\u001a\u00020\tHÆ\u0003J\t\u0010\u001e\u001a\u00020\tHÆ\u0003J\u000f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00000\fHÆ\u0003JK\u0010 \u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\t2\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00000\fHÆ\u0001J\u0013\u0010!\u001a\u00020\t2\b\u0010\"\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010#\u001a\u00020\u0003HÖ\u0001J\t\u0010$\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\n\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0016R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00000\f¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019¨\u0006%"}, d2 = {"Lcom/carplay/iap2/catalog/Iap2FieldSpec;", "", "id", "", "name", "", "type", "Lcom/carplay/iap2/catalog/Iap2WireType;", "required", "", "repeatable", "children", "", "<init>", "(ILjava/lang/String;Lcom/carplay/iap2/catalog/Iap2WireType;ZZLjava/util/List;)V", "getId", "()I", "getName", "()Ljava/lang/String;", "getType", "()Lcom/carplay/iap2/catalog/Iap2WireType;", "getRequired", "()Z", "getRepeatable", "getChildren", "()Ljava/util/List;", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "toString", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class Iap2FieldSpec {
    private final List<Iap2FieldSpec> children;
    private final int id;
    private final String name;
    private final boolean repeatable;
    private final boolean required;
    private final Iap2WireType type;

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ Iap2FieldSpec copy$default(Iap2FieldSpec iap2FieldSpec, int i, String str, Iap2WireType iap2WireType, boolean z, boolean z2, List list, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = iap2FieldSpec.id;
        }
        if ((i2 & 2) != 0) {
            str = iap2FieldSpec.name;
        }
        if ((i2 & 4) != 0) {
            iap2WireType = iap2FieldSpec.type;
        }
        if ((i2 & 8) != 0) {
            z = iap2FieldSpec.required;
        }
        if ((i2 & 16) != 0) {
            z2 = iap2FieldSpec.repeatable;
        }
        if ((i2 & 32) != 0) {
            list = iap2FieldSpec.children;
        }
        boolean z3 = z2;
        List list2 = list;
        return iap2FieldSpec.copy(i, str, iap2WireType, z, z3, list2);
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
    public final Iap2WireType getType() {
        return this.type;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final boolean getRequired() {
        return this.required;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final boolean getRepeatable() {
        return this.repeatable;
    }

    public final List<Iap2FieldSpec> component6() {
        return this.children;
    }

    public final Iap2FieldSpec copy(int id, String name, Iap2WireType type, boolean required, boolean repeatable, List<Iap2FieldSpec> children) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(children, "children");
        return new Iap2FieldSpec(id, name, type, required, repeatable, children);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Iap2FieldSpec)) {
            return false;
        }
        Iap2FieldSpec iap2FieldSpec = (Iap2FieldSpec) other;
        return this.id == iap2FieldSpec.id && Intrinsics.areEqual(this.name, iap2FieldSpec.name) && this.type == iap2FieldSpec.type && this.required == iap2FieldSpec.required && this.repeatable == iap2FieldSpec.repeatable && Intrinsics.areEqual(this.children, iap2FieldSpec.children);
    }

    public int hashCode() {
        return (((((((((Integer.hashCode(this.id) * 31) + this.name.hashCode()) * 31) + this.type.hashCode()) * 31) + Boolean.hashCode(this.required)) * 31) + Boolean.hashCode(this.repeatable)) * 31) + this.children.hashCode();
    }

    public String toString() {
        return "Iap2FieldSpec(id=" + this.id + ", name=" + this.name + ", type=" + this.type + ", required=" + this.required + ", repeatable=" + this.repeatable + ", children=" + this.children + ")";
    }

    public Iap2FieldSpec(int id, String name, Iap2WireType type, boolean required, boolean repeatable, List<Iap2FieldSpec> children) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(children, "children");
        this.id = id;
        this.name = name;
        this.type = type;
        this.required = required;
        this.repeatable = repeatable;
        this.children = children;
    }

    public /* synthetic */ Iap2FieldSpec(int i, String str, Iap2WireType iap2WireType, boolean z, boolean z2, List list, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, str, iap2WireType, (i2 & 8) != 0 ? false : z, (i2 & 16) != 0 ? false : z2, (i2 & 32) != 0 ? CollectionsKt.emptyList() : list);
    }

    public final int getId() {
        return this.id;
    }

    public final String getName() {
        return this.name;
    }

    public final Iap2WireType getType() {
        return this.type;
    }

    public final boolean getRequired() {
        return this.required;
    }

    public final boolean getRepeatable() {
        return this.repeatable;
    }

    public final List<Iap2FieldSpec> getChildren() {
        return this.children;
    }
}
