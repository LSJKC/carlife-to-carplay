package com.carplay.sender;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERSequence;

/* JADX INFO: compiled from: AccessoryTrust.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0002\u0018\u0000 \r2\u00020\u0001:\u0001\rB\u0015\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\u0004\b\u0005\u0010\u0006J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\u000b\u001a\u00020\fH\u0016R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lcom/carplay/sender/AccessoryTrust;", "Lcom/carplay/sender/AccessoryAuthentication;", "roots", "", "Ljava/security/cert/X509Certificate;", "<init>", "(Ljava/util/Collection;)V", "anchors", "", "Ljava/security/cert/TrustAnchor;", "validate", "encoded", "", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AccessoryTrust implements AccessoryAuthentication {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final Set<TrustAnchor> anchors;

    public AccessoryTrust(Collection<? extends X509Certificate> roots) {
        Intrinsics.checkNotNullParameter(roots, "roots");
        Collection<? extends X509Certificate> $this$map$iv = roots;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (Object item$iv$iv : $this$map$iv) {
            X509Certificate it = (X509Certificate) item$iv$iv;
            destination$iv$iv.add(new TrustAnchor(it, null));
        }
        this.anchors = CollectionsKt.toSet((List) destination$iv$iv);
    }

    @Override // com.carplay.sender.AccessoryAuthentication
    public boolean verifyAuthentication(X509Certificate certificate, byte[] challenge, byte[] response) {
        return super.verifyAuthentication(certificate, challenge, response);
    }

    @Override // com.carplay.sender.AccessoryAuthentication
    public X509Certificate validate(byte[] encoded) throws CertPathBuilderException, CertificateException, InvalidAlgorithmParameterException {
        Intrinsics.checkNotNullParameter(encoded, "encoded");
        int length = encoded.length;
        if (!(1 <= length && length < 65001)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (this.anchors.isEmpty()) {
            throw new IllegalStateException("请先导入合法来源的车机认证根证书".toString());
        }
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        Iterable iterableGenerateCertificates = factory.generateCertificates(new ByteArrayInputStream(encoded));
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
            X509Certificate it3 = (X509Certificate) element$iv;
            if (it3.getBasicConstraints() < 0) {
                if (found$iv) {
                    single$iv = null;
                    break;
                }
                single$iv = element$iv;
                found$iv = true;
            }
        }
        X509Certificate leaf = (X509Certificate) single$iv;
        if (leaf == null) {
            throw new IllegalStateException("车机证书链不包含唯一设备证书".toString());
        }
        Iterable<X509Certificate> $this$forEach$iv = certificates;
        for (X509Certificate it4 : $this$forEach$iv) {
            it4.checkValidity();
        }
        X509CertSelector $this$validate_u24lambda_u245 = new X509CertSelector();
        $this$validate_u24lambda_u245.setCertificate(leaf);
        PKIXBuilderParameters $this$validate_u24lambda_u246 = new PKIXBuilderParameters(this.anchors, $this$validate_u24lambda_u245);
        $this$validate_u24lambda_u246.setRevocationEnabled(false);
        $this$validate_u24lambda_u246.addCertStore(CertStore.getInstance("Collection", new CollectionCertStoreParameters((Collection) certificates)));
        CertPathBuilder.getInstance("PKIX").build($this$validate_u24lambda_u246);
        return leaf;
    }

    /* JADX INFO: compiled from: AccessoryTrust.kt */
    @Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u001e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tJ\u0016\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\t¨\u0006\u000e"}, d2 = {"Lcom/carplay/sender/AccessoryTrust$Companion;", "", "<init>", "()V", "verifyChallenge", "", "certificate", "Ljava/security/cert/X509Certificate;", "challenge", "", "response", "equal", "a", "b", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean verifyChallenge(X509Certificate certificate, byte[] challenge, byte[] response) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
            Signature signature;
            byte[] digestInfo;
            Intrinsics.checkNotNullParameter(certificate, "certificate");
            Intrinsics.checkNotNullParameter(challenge, "challenge");
            Intrinsics.checkNotNullParameter(response, "response");
            PublicKey key = certificate.getPublicKey();
            if (key instanceof ECPublicKey) {
                if (!(((ECPublicKey) key).getParams().getOrder().bitLength() == 256 && challenge.length == 32)) {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
                signature = Signature.getInstance("NONEwithECDSA");
            } else {
                if (!(key instanceof RSAPublicKey)) {
                    throw new IllegalStateException("不支持的车机认证密钥".toString());
                }
                if (!SetsKt.setOf((Object[]) new Integer[]{20, 32}).contains(Integer.valueOf(challenge.length))) {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
                signature = Signature.getInstance("NONEwithRSA");
            }
            signature.initVerify(key);
            if (key instanceof RSAPublicKey) {
                String hex = challenge.length == 32 ? "3031300d060960864801650304020105000420" : "3021300906052b0e03021a05000414";
                Iterable $this$map$iv = StringsKt.chunked(hex, 2);
                Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                for (Object item$iv$iv : $this$map$iv) {
                    String it = (String) item$iv$iv;
                    destination$iv$iv.add(Byte.valueOf((byte) Integer.parseInt(it, CharsKt.checkRadix(16))));
                }
                digestInfo = ArraysKt.plus(CollectionsKt.toByteArray((List) destination$iv$iv), challenge);
            } else {
                digestInfo = challenge;
            }
            signature.update(digestInfo);
            byte[] der = ((key instanceof ECPublicKey) && response.length == 64) ? new DERSequence(new ASN1Integer[]{new ASN1Integer(new BigInteger(1, ArraysKt.copyOfRange(response, 0, 32))), new ASN1Integer(new BigInteger(1, ArraysKt.copyOfRange(response, 32, 64)))}).getEncoded() : response;
            return signature.verify(der);
        }

        public final boolean equal(byte[] a, byte[] b) {
            Intrinsics.checkNotNullParameter(a, "a");
            Intrinsics.checkNotNullParameter(b, "b");
            return MessageDigest.isEqual(a, b);
        }
    }
}
