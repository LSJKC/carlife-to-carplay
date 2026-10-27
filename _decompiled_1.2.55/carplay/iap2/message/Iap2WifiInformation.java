package com.carplay.iap2.message;

import androidx.core.app.NotificationCompat;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Iap2Messages.kt */
/* JADX INFO: loaded from: classes3.dex */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\r\b\u0086\b\u0018\u00002\u00020\u0001B3\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0006¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\rJ\u000b\u0010\u0018\u001a\u0004\u0018\u00010\u0006HÆ\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0006HÆ\u0003J<\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0006HÆ\u0001¢\u0006\u0002\u0010\u001bJ\u0013\u0010\u001c\u001a\u00020\u00132\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001e\u001a\u00020\u0003HÖ\u0001J\t\u0010\u001f\u001a\u00020\u0006HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0015\u0010\u0004\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\u000e\u001a\u0004\b\f\u0010\rR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\u0012\u001a\u00020\u00138F¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015¨\u0006 "}, d2 = {"Lcom/carplay/iap2/message/Iap2WifiInformation;", "", NotificationCompat.CATEGORY_STATUS, "", "securityType", "ssid", "", "passphrase", "<init>", "(ILjava/lang/Integer;Ljava/lang/String;Ljava/lang/String;)V", "getStatus", "()I", "getSecurityType", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getSsid", "()Ljava/lang/String;", "getPassphrase", "successful", "", "getSuccessful", "()Z", "component1", "component2", "component3", "component4", "copy", "(ILjava/lang/Integer;Ljava/lang/String;Ljava/lang/String;)Lcom/carplay/iap2/message/Iap2WifiInformation;", "equals", "other", "hashCode", "toString", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final /* data */ class Iap2WifiInformation {
    private final String passphrase;
    private final Integer securityType;
    private final String ssid;
    private final int status;

    public static /* synthetic */ Iap2WifiInformation copy$default(Iap2WifiInformation iap2WifiInformation, int i, Integer num, String str, String str2, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = iap2WifiInformation.status;
        }
        if ((i2 & 2) != 0) {
            num = iap2WifiInformation.securityType;
        }
        if ((i2 & 4) != 0) {
            str = iap2WifiInformation.ssid;
        }
        if ((i2 & 8) != 0) {
            str2 = iap2WifiInformation.passphrase;
        }
        return iap2WifiInformation.copy(i, num, str, str2);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final int getStatus() {
        return this.status;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final Integer getSecurityType() {
        return this.securityType;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getSsid() {
        return this.ssid;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final String getPassphrase() {
        return this.passphrase;
    }

    public final Iap2WifiInformation copy(int status, Integer securityType, String ssid, String passphrase) {
        return new Iap2WifiInformation(status, securityType, ssid, passphrase);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Iap2WifiInformation)) {
            return false;
        }
        Iap2WifiInformation iap2WifiInformation = (Iap2WifiInformation) other;
        return this.status == iap2WifiInformation.status && Intrinsics.areEqual(this.securityType, iap2WifiInformation.securityType) && Intrinsics.areEqual(this.ssid, iap2WifiInformation.ssid) && Intrinsics.areEqual(this.passphrase, iap2WifiInformation.passphrase);
    }

    public int hashCode() {
        return (((((Integer.hashCode(this.status) * 31) + (this.securityType == null ? 0 : this.securityType.hashCode())) * 31) + (this.ssid == null ? 0 : this.ssid.hashCode())) * 31) + (this.passphrase != null ? this.passphrase.hashCode() : 0);
    }

    public String toString() {
        return "Iap2WifiInformation(status=" + this.status + ", securityType=" + this.securityType + ", ssid=" + this.ssid + ", passphrase=" + this.passphrase + ")";
    }

    public Iap2WifiInformation(int status, Integer securityType, String ssid, String passphrase) {
        this.status = status;
        this.securityType = securityType;
        this.ssid = ssid;
        this.passphrase = passphrase;
    }

    public /* synthetic */ Iap2WifiInformation(int i, Integer num, String str, String str2, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, (i2 & 2) != 0 ? null : num, (i2 & 4) != 0 ? null : str, (i2 & 8) != 0 ? null : str2);
    }

    public final int getStatus() {
        return this.status;
    }

    public final Integer getSecurityType() {
        return this.securityType;
    }

    public final String getSsid() {
        return this.ssid;
    }

    public final String getPassphrase() {
        return this.passphrase;
    }

    public final boolean getSuccessful() {
        return this.status == 0;
    }
}
