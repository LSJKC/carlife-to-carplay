package com.carplay.sender;

import java.io.ByteArrayInputStream;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: BondedAccessoryTrust.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B-\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\b0\u0007¢\u0006\u0004\b\t\u0010\nJ\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0003H\u0016J \u0010\u0010\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u0003H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\u0003X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lcom/carplay/sender/BondedAccessoryTrust;", "Lcom/carplay/sender/AccessoryAuthentication;", "pinnedKey", "", "authenticatedBluetooth", "", "saveKey", "Lkotlin/Function1;", "", "<init>", "([BZLkotlin/jvm/functions/Function1;)V", "pinned", "candidate", "validate", "Ljava/security/cert/X509Certificate;", "encoded", "verifyAuthentication", "certificate", "challenge", "response", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class BondedAccessoryTrust implements AccessoryAuthentication {
    private final boolean authenticatedBluetooth;
    private byte[] candidate;
    private byte[] pinned;
    private final Function1<byte[], Unit> saveKey;

    /* JADX WARN: Multi-variable type inference failed */
    public BondedAccessoryTrust(byte[] pinnedKey, boolean authenticatedBluetooth, Function1<? super byte[], Unit> saveKey) {
        byte[] bArrCopyOf;
        Intrinsics.checkNotNullParameter(saveKey, "saveKey");
        this.authenticatedBluetooth = authenticatedBluetooth;
        this.saveKey = saveKey;
        if (pinnedKey != null) {
            bArrCopyOf = Arrays.copyOf(pinnedKey, pinnedKey.length);
            Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        } else {
            bArrCopyOf = null;
        }
        this.pinned = bArrCopyOf;
    }

    @Override // com.carplay.sender.AccessoryAuthentication
    public synchronized X509Certificate validate(byte[] encoded) {
        X509Certificate leaf;
        Intrinsics.checkNotNullParameter(encoded, "encoded");
        int length = encoded.length;
        if (!(1 <= length && length < 65001)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (!(this.pinned != null || this.authenticatedBluetooth)) {
            throw new IllegalStateException("首次绑定认证身份必须通过已配对车机的安全蓝牙连接".toString());
        }
        Iterable iterableGenerateCertificates = CertificateFactory.getInstance("X.509").generateCertificates(new ByteArrayInputStream(encoded));
        Intrinsics.checkNotNullExpressionValue(iterableGenerateCertificates, "generateCertificates(...)");
        Iterable $this$map$iv = iterableGenerateCertificates;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (Object item$iv$iv : $this$map$iv) {
            Certificate it = (Certificate) item$iv$iv;
            Intrinsics.checkNotNull(it, "null cannot be cast to non-null type java.security.cert.X509Certificate");
            destination$iv$iv.add((X509Certificate) it);
        }
        Iterable certificates = (List) destination$iv$iv;
        Iterable $this$singleOrNull$iv = certificates;
        Object single$iv = null;
        boolean found$iv = false;
        Iterator it2 = $this$singleOrNull$iv.iterator();
        while (true) {
            if (!it2.hasNext()) {
                if (!found$iv) {
                    single$iv = null;
                    break;
                }
                break;
            }
            Object element$iv = it2.next();
            if (((X509Certificate) element$iv).getBasicConstraints() < 0) {
                if (found$iv) {
                    single$iv = null;
                    break;
                }
                single$iv = element$iv;
                found$iv = true;
            }
        }
        leaf = (X509Certificate) single$iv;
        if (leaf == null) {
            throw new IllegalStateException("车机未提供唯一的认证设备证书".toString());
        }
        leaf.checkValidity();
        PublicKey key = leaf.getPublicKey();
        if (key instanceof ECPublicKey) {
            if (!(((ECPublicKey) key).getParams().getOrder().bitLength() == 256)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
        } else {
            if (!(key instanceof RSAPublicKey)) {
                throw new IllegalStateException("不支持的车机认证密钥".toString());
            }
            if (!(((RSAPublicKey) key).getModulus().bitLength() >= 1024)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
        }
        byte[] key2 = leaf.getPublicKey().getEncoded();
        byte[] it3 = this.pinned;
        if (it3 != null) {
            AccessoryTrust.Companion companion = AccessoryTrust.INSTANCE;
            Intrinsics.checkNotNull(key2);
            if (!companion.equal(it3, key2)) {
                throw new IllegalStateException("车机认证公钥已变化，已停止连接；请确认车机身份".toString());
            }
        }
        Intrinsics.checkNotNull(key2);
        byte[] bArrCopyOf = Arrays.copyOf(key2, key2.length);
        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        this.candidate = bArrCopyOf;
        return leaf;
    }

    @Override // com.carplay.sender.AccessoryAuthentication
    public synchronized boolean verifyAuthentication(X509Certificate certificate, byte[] challenge, byte[] response) {
        boolean z;
        Intrinsics.checkNotNullParameter(certificate, "certificate");
        Intrinsics.checkNotNullParameter(challenge, "challenge");
        Intrinsics.checkNotNullParameter(response, "response");
        certificate.checkValidity();
        byte[] key = certificate.getPublicKey().getEncoded();
        byte[] it = this.candidate;
        if (it != null) {
            AccessoryTrust.Companion companion = AccessoryTrust.INSTANCE;
            Intrinsics.checkNotNull(key);
            z = companion.equal(it, key);
        } else {
            z = false;
        }
        if (!z) {
            throw new IllegalStateException("认证响应不属于当前车机证书".toString());
        }
        byte[] it2 = this.pinned;
        if (it2 != null) {
            AccessoryTrust.Companion companion2 = AccessoryTrust.INSTANCE;
            Intrinsics.checkNotNull(key);
            if (!companion2.equal(it2, key)) {
                throw new IllegalStateException("车机认证身份不一致".toString());
            }
        }
        if (!AccessoryTrust.INSTANCE.verifyChallenge(certificate, challenge, response)) {
            return false;
        }
        if (this.pinned == null) {
            if (!this.authenticatedBluetooth) {
                throw new IllegalStateException("无线通道不能自行绑定首次认证身份".toString());
            }
            Function1<byte[], Unit> function1 = this.saveKey;
            Intrinsics.checkNotNull(key);
            byte[] bArrCopyOf = Arrays.copyOf(key, key.length);
            Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
            function1.invoke(bArrCopyOf);
            byte[] bArrCopyOf2 = Arrays.copyOf(key, key.length);
            Intrinsics.checkNotNullExpressionValue(bArrCopyOf2, "copyOf(...)");
            this.pinned = bArrCopyOf2;
        }
        return true;
    }
}
