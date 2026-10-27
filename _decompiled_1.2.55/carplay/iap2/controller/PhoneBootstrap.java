package com.carplay.iap2.controller;

import androidx.core.app.NotificationCompat;
import androidx.core.view.PointerIconCompat;
import com.carplay.iap2.body.Iap2BodyBuilder;
import com.carplay.iap2.body.Iap2BodyReader;
import com.carplay.iap2.message.Iap2CarPlayMessages;
import com.carplay.iap2.message.Iap2Messages;
import com.carplay.iap2.transport.Iap2IdentificationClient;
import com.carplay.iap2.wire.Iap2Frame;
import com.carplay.iap2.wire.Iap2Parameter;
import com.carplay.iap2.wire.Iap2ProtocolException;
import com.carplay.iap2.wire.Iap2WireCodec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;

/* JADX INFO: compiled from: PhoneBootstrap.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\"\n\u0002\u0010\b\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001BÛ\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u0012\u001e\u0010\b\u001a\u001a\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n0\t\u0012\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u0005\u0012\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\r0\u0005\u0012\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\r0\u0005\u0012\u0014\b\u0002\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u0005\u0012\u0014\b\u0002\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\r0\u0005\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0015\u0012\u000e\b\u0002\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00030\u0017\u0012\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u0006\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u001a¢\u0006\u0004\b\u001b\u0010\u001cJ\u0006\u0010.\u001a\u00020\rJ\u0006\u0010/\u001a\u00020\u0013J\b\u00100\u001a\u0004\u0018\u00010#J\b\u00101\u001a\u0004\u0018\u00010\u0006J\b\u00102\u001a\u00020\rH\u0002J\u000e\u00103\u001a\u00020\r2\u0006\u00104\u001a\u00020\fJ\b\u00105\u001a\u00020\rH\u0002J\b\u00106\u001a\u00020\rH\u0002J\u0010\u00107\u001a\u00020\f2\u0006\u00108\u001a\u00020*H\u0002J\u0010\u00109\u001a\u00020\u00062\u0006\u0010:\u001a\u00020;H\u0002J\u0010\u0010<\u001a\u00020\n2\u0006\u00108\u001a\u00020*H\u0002J\u0010\u0010=\u001a\u00020\r2\u0006\u0010>\u001a\u00020\u0003H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R&\u0010\b\u001a\u001a\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\r0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\r0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\r0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00030\u0017X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u0004\u0018\u00010#X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010$\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010%\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010&\u001a\u0004\u0018\u00010\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010(\u001a\b\u0012\u0004\u0012\u00020*0)X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010+\u001a\b\u0012\u0004\u0012\u00020*0)X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010,\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010-\u001a\u0004\u0018\u00010\u0013X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006?"}, d2 = {"Lcom/carplay/iap2/controller/PhoneBootstrap;", "", "identifier", "", "validateCertificates", "Lkotlin/Function1;", "", "Ljava/security/cert/X509Certificate;", "verifyChallenge", "Lkotlin/Function3;", "", "send", "Lcom/carplay/iap2/wire/Iap2Frame;", "", "onStage", "onEndpoint", "Lcom/carplay/iap2/controller/WirelessEndpoint;", "onOther", "onStatus", "Lcom/carplay/iap2/controller/BootstrapStatus;", "profile", "Lcom/carplay/iap2/controller/PhoneBootstrapProfile;", "transportIdentifier", "Lkotlin/Function0;", "expectedAccessoryIdentity", "purpose", "Lcom/carplay/iap2/controller/PhoneBootstrapPurpose;", "<init>", "(Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lcom/carplay/iap2/controller/PhoneBootstrapProfile;Lkotlin/jvm/functions/Function0;[BLcom/carplay/iap2/controller/PhoneBootstrapPurpose;)V", "identified", "authenticated", "published", "networkRequestQueued", "controlReady", "versionInfo", "Lcom/carplay/iap2/controller/AccessoryVersionInfo;", "leaf", "challenge", "endpoint", "supportsStart", "accessoryReceives", "", "", "accessorySends", "identification", "lastReportedStatus", "begin", NotificationCompat.CATEGORY_STATUS, "accessoryVersionInfo", "accessoryIdentityFingerprint", "reportStatus", "receive", "frame", "publish", "publishDeviceNotifications", "empty", "id", "identityFingerprint", "body", "Lcom/carplay/iap2/body/Iap2BodyReader;", "acceptsNotification", "diagnostic", "message", "core-iap2"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class PhoneBootstrap {
    private Set<Integer> accessoryReceives;
    private Set<Integer> accessorySends;
    private boolean authenticated;
    private byte[] challenge;
    private boolean controlReady;
    private WirelessEndpoint endpoint;
    private final byte[] expectedAccessoryIdentity;
    private Iap2Frame identification;
    private boolean identified;
    private final String identifier;
    private BootstrapStatus lastReportedStatus;
    private X509Certificate leaf;
    private boolean networkRequestQueued;
    private final Function1<WirelessEndpoint, Unit> onEndpoint;
    private final Function1<Iap2Frame, Unit> onOther;
    private final Function1<String, Unit> onStage;
    private final Function1<BootstrapStatus, Unit> onStatus;
    private final PhoneBootstrapProfile profile;
    private boolean published;
    private final PhoneBootstrapPurpose purpose;
    private final Function1<Iap2Frame, Unit> send;
    private boolean supportsStart;
    private final Function0<String> transportIdentifier;
    private final Function1<byte[], X509Certificate> validateCertificates;
    private final Function3<X509Certificate, byte[], byte[], Boolean> verifyChallenge;
    private AccessoryVersionInfo versionInfo;

    /* JADX WARN: Multi-variable type inference failed */
    public PhoneBootstrap(String identifier, Function1<? super byte[], ? extends X509Certificate> validateCertificates, Function3<? super X509Certificate, ? super byte[], ? super byte[], Boolean> verifyChallenge, Function1<? super Iap2Frame, Unit> send, Function1<? super String, Unit> onStage, Function1<? super WirelessEndpoint, Unit> onEndpoint, Function1<? super Iap2Frame, Unit> onOther, Function1<? super BootstrapStatus, Unit> onStatus, PhoneBootstrapProfile profile, Function0<String> transportIdentifier, byte[] expectedAccessoryIdentity, PhoneBootstrapPurpose purpose) {
        byte[] bArrCopyOf;
        Intrinsics.checkNotNullParameter(identifier, "identifier");
        Intrinsics.checkNotNullParameter(validateCertificates, "validateCertificates");
        Intrinsics.checkNotNullParameter(verifyChallenge, "verifyChallenge");
        Intrinsics.checkNotNullParameter(send, "send");
        Intrinsics.checkNotNullParameter(onStage, "onStage");
        Intrinsics.checkNotNullParameter(onEndpoint, "onEndpoint");
        Intrinsics.checkNotNullParameter(onOther, "onOther");
        Intrinsics.checkNotNullParameter(onStatus, "onStatus");
        Intrinsics.checkNotNullParameter(profile, "profile");
        Intrinsics.checkNotNullParameter(transportIdentifier, "transportIdentifier");
        Intrinsics.checkNotNullParameter(purpose, "purpose");
        this.identifier = identifier;
        this.validateCertificates = validateCertificates;
        this.verifyChallenge = verifyChallenge;
        this.send = send;
        this.onStage = onStage;
        this.onEndpoint = onEndpoint;
        this.onOther = onOther;
        this.onStatus = onStatus;
        this.profile = profile;
        this.transportIdentifier = transportIdentifier;
        this.purpose = purpose;
        if (expectedAccessoryIdentity != null) {
            bArrCopyOf = Arrays.copyOf(expectedAccessoryIdentity, expectedAccessoryIdentity.length);
            Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        } else {
            bArrCopyOf = null;
        }
        this.expectedAccessoryIdentity = bArrCopyOf;
        this.accessoryReceives = SetsKt.emptySet();
        this.accessorySends = SetsKt.emptySet();
    }

    public /* synthetic */ PhoneBootstrap(String str, Function1 function1, Function3 function3, Function1 function2, Function1 function4, Function1 function5, Function1 function6, Function1 function7, PhoneBootstrapProfile phoneBootstrapProfile, Function0 function0, byte[] bArr, PhoneBootstrapPurpose phoneBootstrapPurpose, int i, DefaultConstructorMarker defaultConstructorMarker) {
        final String str2;
        Function0 function8;
        Function1 function9 = (i & 64) != 0 ? new Function1() { // from class: com.carplay.iap2.controller.PhoneBootstrap$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PhoneBootstrap._init_$lambda$0((Iap2Frame) obj);
            }
        } : function6;
        Function1 function10 = (i & 128) != 0 ? new Function1() { // from class: com.carplay.iap2.controller.PhoneBootstrap$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PhoneBootstrap._init_$lambda$1((BootstrapStatus) obj);
            }
        } : function7;
        PhoneBootstrapProfile phoneBootstrapProfile2 = (i & 256) != 0 ? PhoneBootstrapProfile.GENERIC : phoneBootstrapProfile;
        if ((i & 512) != 0) {
            str2 = str;
            function8 = new Function0() { // from class: com.carplay.iap2.controller.PhoneBootstrap$$ExternalSyntheticLambda2
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return PhoneBootstrap._init_$lambda$2(str2);
                }
            };
        } else {
            str2 = str;
            function8 = function0;
        }
        this(str2, function1, function3, function2, function4, function5, function9, function10, phoneBootstrapProfile2, function8, (i & 1024) != 0 ? null : bArr, (i & 2048) != 0 ? PhoneBootstrapPurpose.WIRELESS_CONFIGURATION : phoneBootstrapPurpose);
    }

    static final Unit _init_$lambda$0(Iap2Frame it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    static final Unit _init_$lambda$1(BootstrapStatus it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    static final String _init_$lambda$2(String $identifier) {
        return $identifier;
    }

    public final void begin() {
        this.onStage.invoke("识别车机");
        this.send.invoke(empty(Iap2IdentificationClient.START_IDENTIFICATION));
        reportStatus();
    }

    public final synchronized BootstrapStatus status() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        z = this.identified;
        z2 = this.authenticated;
        z3 = true;
        z4 = this.leaf != null;
        if (this.challenge == null) {
            z3 = false;
        }
        return new BootstrapStatus(z, z2, z4, z3, this.endpoint != null, this.networkRequestQueued, this.supportsStart, this.controlReady);
    }

    public final synchronized AccessoryVersionInfo accessoryVersionInfo() {
        return this.versionInfo;
    }

    public final synchronized byte[] accessoryIdentityFingerprint() {
        Iap2Frame it;
        it = this.identification;
        return it != null ? identityFingerprint(Iap2BodyReader.INSTANCE.of(it)) : null;
    }

    private final void reportStatus() {
        BootstrapStatus current = status();
        if (Intrinsics.areEqual(current, this.lastReportedStatus)) {
            return;
        }
        this.lastReportedStatus = current;
        try {
            this.onStatus.invoke(current);
        } catch (Exception e) {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v10 */
    /* JADX WARN: Type inference failed for: r7v11 */
    /* JADX WARN: Type inference failed for: r7v15 */
    public final synchronized void receive(Iap2Frame frame) {
        WirelessEndpoint incoming;
        WirelessEndpoint merged;
        Intrinsics.checkNotNullParameter(frame, "frame");
        Iap2BodyReader body = Iap2BodyReader.INSTANCE.of(frame);
        boolean z = true;
        Object element$iv$iv = null;
        switch (frame.getMessageId()) {
            case Iap2IdentificationClient.IDENTIFICATION_INFORMATION /* 7425 */:
                if (this.identified) {
                    throw new IllegalStateException("收到重复识别信息".toString());
                }
                body.string(0);
                body.string(1);
                body.string(2);
                byte[] expected = this.expectedAccessoryIdentity;
                if (expected != null && !MessageDigest.isEqual(expected, identityFingerprint(body))) {
                    throw new IllegalStateException("重新协商时车机身份信息已变化".toString());
                }
                List<Integer> listU16List = body.u16List(6);
                List<Integer> listU16List2 = body.u16List(7);
                this.accessoryReceives = CollectionsKt.toSet(listU16List2);
                this.accessorySends = CollectionsKt.toSet(listU16List);
                this.identification = frame;
                this.versionInfo = AccessoryVersionInfo.INSTANCE.fromIdentification(body);
                this.supportsStart = listU16List.contains(17153) && this.accessoryReceives.contains(17152);
                if (this.profile != PhoneBootstrapProfile.GENERIC) {
                    diagnostic("车机消息能力：发送=" + receive$ids(listU16List) + "；接收=" + receive$ids(listU16List2) + "；modern4300=" + this.supportsStart);
                    diagnostic("车机识别结构：" + IdentificationDiagnostics.INSTANCE.describe(body));
                    AccessoryVersionInfo accessoryVersionInfo = this.versionInfo;
                    Intrinsics.checkNotNull(accessoryVersionInfo);
                    diagnostic("车机版本信息：" + accessoryVersionInfo.diagnostic() + "；固件及 iAP2 会话版本不等于 CarPlay SDK 版本，SDK 等待 0x4301 声明");
                }
                this.identified = true;
                this.send.invoke(empty(Iap2IdentificationClient.IDENTIFICATION_ACCEPTED));
                this.send.invoke(empty(43520));
                this.onStage.invoke("验证车机身份");
                break;
                break;
            case Iap2IdentificationClient.IDENTIFICATION_REJECTED /* 7427 */:
            case 43524:
                throw new IllegalStateException("车机拒绝识别或认证".toString());
            case 17153:
            case 22275:
                if (!(this.identified && this.authenticated)) {
                    throw new IllegalStateException("认证完成前收到网络配置".toString());
                }
                if (frame.getMessageId() == 22275) {
                    incoming = new WirelessEndpoint(body.string(1), body.string(2), body.u8(3), body.u8(4), null, null, null, null, null, null, PointerIconCompat.TYPE_TEXT, null);
                } else {
                    Integer numOptionalU8 = body.optionalU8(8);
                    if (!(numOptionalU8 == null || numOptionalU8.intValue() != 1)) {
                        throw new IllegalStateException("车机要求的双向认证尚未配置".toString());
                    }
                    Iap2BodyReader wireless = body.group(1);
                    String strString = wireless.string(0);
                    String strString2 = wireless.string(1);
                    int iU8 = wireless.u8(4);
                    int iU9 = wireless.u8(2);
                    Iterable $this$filter$iv = wireless.list();
                    Collection destination$iv$iv = new ArrayList();
                    Collection destination$iv$iv2 = destination$iv$iv;
                    for (Object element$iv$iv2 : $this$filter$iv) {
                        if ((((Iap2Parameter) element$iv$iv2).getId() == 3 ? 1 : element$iv$iv) != 0) {
                            Collection destination$iv$iv3 = destination$iv$iv2;
                            destination$iv$iv3.add(element$iv$iv2);
                            destination$iv$iv2 = destination$iv$iv3;
                        }
                        element$iv$iv = null;
                    }
                    Iterable $this$map$iv = (List) destination$iv$iv2;
                    Collection destination$iv$iv4 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                    for (Object item$iv$iv : $this$map$iv) {
                        destination$iv$iv4.add(Iap2WireCodec.INSTANCE.readString(((Iap2Parameter) item$iv$iv).getPayload()));
                    }
                    ArrayList arrayList = (List) destination$iv$iv4;
                    long it = body.u32(2);
                    if (!(1 <= it && it < 65536)) {
                        throw new IllegalArgumentException("Failed requirement.".toString());
                    }
                    Unit unit = Unit.INSTANCE;
                    incoming = new WirelessEndpoint(strString, strString2, iU8, iU9, arrayList, Integer.valueOf((int) it), body.optionalString(3), body.optionalString(4), AccessoryVersionInfo.INSTANCE.optionalVersionString$core_iap2(body, 5), AccessoryVersionInfo.INSTANCE.optionalVersionString$core_iap2(body, 6));
                }
                if (frame.getMessageId() == 17153) {
                    diagnostic("车机协议版本声明：sourceVersion=" + AccessoryVersionInfo.INSTANCE.safe$core_iap2(incoming.getSourceVersion()) + " carPlaySDK=" + AccessoryVersionInfo.INSTANCE.safe$core_iap2(incoming.getCarPlaySdkVersion()));
                }
                WirelessEndpoint wirelessEndpoint = this.endpoint;
                if (wirelessEndpoint == null || (merged = wirelessEndpoint.merge(incoming)) == null) {
                    merged = incoming;
                }
                if (!Intrinsics.areEqual(merged, this.endpoint)) {
                    this.endpoint = merged;
                    this.onStage.invoke("无线配置已接收");
                    this.onEndpoint.invoke(merged);
                }
                break;
                break;
            case 22272:
                Function1<Iap2Frame, Unit> function1 = this.send;
                Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
                Iap2BodyBuilder $this$receive_u24lambda_u2418 = Iap2BodyBuilder.INSTANCE.opaque(22273);
                $this$receive_u24lambda_u2418.u8(0, 2);
                function1.invoke($this$receive_u24lambda_u2418.build());
                break;
            case 43521:
                if (!this.identified || this.leaf != null) {
                    z = false;
                }
                if (!z) {
                    throw new IllegalStateException("认证证书时序错误".toString());
                }
                X509Certificate trusted = this.validateCertificates.invoke(body.bytes(0));
                this.leaf = trusted;
                byte[] p0 = new byte[trusted.getPublicKey() instanceof ECPublicKey ? 32 : 20];
                new SecureRandom().nextBytes(p0);
                this.challenge = p0;
                Function1<Iap2Frame, Unit> function2 = this.send;
                Iap2Messages iap2Messages2 = Iap2Messages.INSTANCE;
                Iap2BodyBuilder $this$receive_u24lambda_u249 = Iap2BodyBuilder.INSTANCE.opaque(43522);
                byte[] bArr = this.challenge;
                Intrinsics.checkNotNull(bArr);
                $this$receive_u24lambda_u249.bytes(0, bArr);
                function2.invoke($this$receive_u24lambda_u249.build());
                break;
                break;
            case 43523:
                if (this.authenticated) {
                    throw new IllegalStateException("重复认证响应".toString());
                }
                X509Certificate certificate = this.leaf;
                if (certificate == null) {
                    throw new IllegalStateException("未请求的认证响应".toString());
                }
                byte[] nonce = this.challenge;
                if (nonce == null) {
                    throw new IllegalStateException("认证挑战不存在".toString());
                }
                if (!this.verifyChallenge.invoke(certificate, nonce, body.bytes(0)).booleanValue()) {
                    this.send.invoke(empty(43524));
                    throw new IllegalStateException("车机认证签名无效".toString());
                }
                this.authenticated = true;
                this.challenge = null;
                this.send.invoke(empty(43525));
                publish();
                break;
                break;
            default:
                this.onOther.invoke(frame);
                break;
        }
        reportStatus();
    }

    private static final String receive$ids(List<Integer> list) {
        String str;
        String strJoinToString$default = CollectionsKt.joinToString$default(CollectionsKt.take(list, 24), ",", null, null, 0, null, new Function1() { // from class: com.carplay.iap2.controller.PhoneBootstrap$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PhoneBootstrap.receive$ids$lambda$7(((Integer) obj).intValue());
            }
        }, 30, null);
        if (list.size() > 24) {
            str = ",… total=" + list.size();
        } else {
            str = "";
        }
        return strJoinToString$default + str;
    }

    static final CharSequence receive$ids$lambda$7(int it) {
        String string = Integer.toString(it, CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return "0x" + string;
    }

    private final void publish() throws Iap2ProtocolException {
        AccessoryWirelessComponent wirelessComponent;
        int i;
        Iap2Frame availability;
        String shape;
        if (this.published) {
            return;
        }
        if (this.purpose == PhoneBootstrapPurpose.CONTROL_SESSION) {
            this.published = true;
            publishDeviceNotifications();
            this.controlReady = true;
            this.onStage.invoke("Wi-Fi iAP2 独立识别和签名验证完成");
            return;
        }
        if (this.profile == PhoneBootstrapProfile.VIVO_REFERENCE && this.supportsStart) {
            AccessoryWirelessComponents accessoryWirelessComponents = AccessoryWirelessComponents.INSTANCE;
            Iap2BodyReader.Companion companion = Iap2BodyReader.INSTANCE;
            Iap2Frame iap2Frame = this.identification;
            if (iap2Frame == null) {
                throw new IllegalStateException("车机识别信息不存在".toString());
            }
            wirelessComponent = accessoryWirelessComponents.select(companion.of(iap2Frame));
        } else {
            wirelessComponent = null;
        }
        boolean publishReferenceReadiness = (this.profile == PhoneBootstrapProfile.VIVO_WIRELESS_READY || this.profile.getUsesFlatAvailability()) && this.supportsStart;
        boolean publishTransport = publishReferenceReadiness || acceptsNotification(19982);
        boolean needsTransport = publishTransport || (this.profile.getUsesPhoneTransportString() && this.supportsStart);
        String transport = needsTransport ? this.transportIdentifier.invoke() : null;
        if (this.profile != PhoneBootstrapProfile.GENERIC && needsTransport && !Intrinsics.areEqual(BluetoothTransportIdentity.INSTANCE.normalizedAddress(transport), transport)) {
            throw new IllegalArgumentException("请在诊断设置填写手机自身的真实蓝牙地址".toString());
        }
        if (this.profile != PhoneBootstrapProfile.GENERIC && !this.supportsStart) {
            if (!(this.accessoryReceives.contains(22274) && this.accessorySends.contains(22275))) {
                throw new IllegalStateException("车机未声明可用的无线配置请求，无法发起无线 CarPlay".toString());
            }
        }
        if (publishReferenceReadiness) {
            diagnostic("vivo 无线就绪方案：0x4e0e 和 0x4e0d 在 0x4300 前按参考流程发送；车机声明接收：4e0e=" + this.accessoryReceives.contains(19982) + "，4e0d=" + this.accessoryReceives.contains(19981) + "；0x4300 保持手机地址字符串");
        }
        this.published = true;
        publishDeviceNotifications();
        if (publishTransport) {
            Function1<Iap2Frame, Unit> function1 = this.send;
            Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
            Iap2BodyBuilder $this$publish_u24lambda_u2422 = Iap2BodyBuilder.INSTANCE.opaque(19982);
            if (transport != null) {
                $this$publish_u24lambda_u2422.string(0, transport);
                function1.invoke($this$publish_u24lambda_u2422.build());
            } else {
                throw new IllegalStateException("Required value was null.".toString());
            }
        }
        if (this.profile == PhoneBootstrapProfile.GENERIC) {
            if (this.supportsStart) {
                Function1<Iap2Frame, Unit> function2 = this.send;
                Iap2CarPlayMessages iap2CarPlayMessages = Iap2CarPlayMessages.INSTANCE;
                if (transport == null) {
                    throw new IllegalStateException("Required value was null.".toString());
                }
                function2.invoke(Iap2CarPlayMessages.buildAvailability$default(iap2CarPlayMessages, false, transport, false, null, 13, null));
            }
            this.send.invoke(empty(22274));
        } else {
            if (publishReferenceReadiness || acceptsNotification(19981)) {
                Function1<Iap2Frame, Unit> function3 = this.send;
                Iap2Messages iap2Messages2 = Iap2Messages.INSTANCE;
                Iap2BodyBuilder $this$publish_u24lambda_u2423 = Iap2BodyBuilder.INSTANCE.opaque(19981);
                $this$publish_u24lambda_u2423.u8(0, 1);
                function3.invoke($this$publish_u24lambda_u2423.build());
            }
            if (!this.supportsStart) {
                this.send.invoke(empty(22274));
            } else {
                if (this.profile == PhoneBootstrapProfile.MODERN_WIRELESS && PhoneBluetoothRecovery.INSTANCE.isObservedFirmware(this.versionInfo)) {
                    diagnostic("GM 完整无线声明：0x4300 同时包含 group(0) 有线传输标识与 group(1) 蓝牙传输标识，满足车机 IAPAuthenticator 属性 1/2 门控");
                }
                if (this.profile == PhoneBootstrapProfile.VIVO_WIRELESS_MINIMAL) {
                    Iap2Frame availability2 = Iap2CarPlayMessages.INSTANCE.buildMinimalWirelessAvailability();
                    diagnostic("网络发布方案：0x4300，最小无线属性 group(1)/available=1，省略可选传输标识；帧=" + availability2.encodedFrame().length + "B");
                    this.send.invoke(availability2);
                } else if (this.profile.getUsesPhoneTransportString()) {
                    boolean flat = this.profile.getUsesFlatAvailability();
                    boolean includeWired = this.profile == PhoneBootstrapProfile.MODERN_WIRELESS;
                    if (flat) {
                        Iap2CarPlayMessages iap2CarPlayMessages2 = Iap2CarPlayMessages.INSTANCE;
                        if (transport == null) {
                            throw new IllegalStateException("Required value was null.".toString());
                        }
                        availability = iap2CarPlayMessages2.buildFlatWirelessAvailability(transport);
                        i = 22274;
                    } else if (includeWired) {
                        Iap2CarPlayMessages iap2CarPlayMessages3 = Iap2CarPlayMessages.INSTANCE;
                        if (transport != null) {
                            availability = Iap2CarPlayMessages.buildAvailability$default(iap2CarPlayMessages3, false, transport, true, this.identifier, 1, null);
                            i = 22274;
                        } else {
                            throw new IllegalStateException("Required value was null.".toString());
                        }
                    } else {
                        Iap2CarPlayMessages iap2CarPlayMessages4 = Iap2CarPlayMessages.INSTANCE;
                        if (transport != null) {
                            i = 22274;
                            availability = Iap2CarPlayMessages.buildAvailability$default(iap2CarPlayMessages4, false, transport, false, null, 13, null);
                        } else {
                            throw new IllegalStateException("Required value was null.".toString());
                        }
                    }
                    Iap2BodyReader body = Iap2BodyReader.INSTANCE.of(availability);
                    int transportBytes = (flat ? body : body.group(1)).bytes(1).length;
                    if (flat) {
                        shape = "根参数=[0,1]，参数0=1，参数1";
                    } else {
                        shape = includeWired ? "有线参数0/1=手机UUID+无线参数1/1" : "无线参数1/1";
                    }
                    diagnostic("网络发布方案：0x4300，" + shape + "=手机蓝牙地址字符串，长度=" + transportBytes + "B，帧=" + availability.encodedFrame().length + "B；车机组件编号与手机 UUID 不用于该字段");
                    this.send.invoke(availability);
                    if (this.profile == PhoneBootstrapProfile.VIVO_FLAT_WIFI_REQUEST) {
                        diagnostic("主动请求车机无线配置：按小米参考流程将 0x5702 排在 0x4300 后；车机声明接收5702=" + this.accessoryReceives.contains(Integer.valueOf(i)) + "，发送5703=" + this.accessorySends.contains(22275) + "；是否获得配置以实际响应为准");
                        this.send.invoke(empty(22274));
                    }
                } else if (wirelessComponent != null) {
                    AccessoryWirelessComponent component = wirelessComponent;
                    diagnostic("网络发布方案：0x4300，车机无线组件=" + component.getId() + "，来源=0x1d01/24/0，supported=" + component.getSupported() + " available=" + component.getAvailable());
                    this.send.invoke(Iap2CarPlayMessages.INSTANCE.buildWirelessComponentAvailability(component.getId()));
                } else {
                    throw new IllegalStateException("Required value was null.".toString());
                }
            }
        }
        this.networkRequestQueued = true;
        this.onStage.invoke("认证通过，等待无线配置");
    }

    private final void publishDeviceNotifications() {
        if (acceptsNotification(19977)) {
            Function1<Iap2Frame, Unit> function1 = this.send;
            Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
            Iap2BodyBuilder $this$publishDeviceNotifications_u24lambda_u2424 = Iap2BodyBuilder.INSTANCE.opaque(19977);
            $this$publishDeviceNotifications_u24lambda_u2424.string(0, "CarPlay Android");
            function1.invoke($this$publishDeviceNotifications_u24lambda_u2424.build());
        }
        if (acceptsNotification(19978)) {
            Function1<Iap2Frame, Unit> function2 = this.send;
            Iap2Messages iap2Messages2 = Iap2Messages.INSTANCE;
            Iap2BodyBuilder $this$publishDeviceNotifications_u24lambda_u2425 = Iap2BodyBuilder.INSTANCE.opaque(19978);
            $this$publishDeviceNotifications_u24lambda_u2425.string(0, "zh");
            function2.invoke($this$publishDeviceNotifications_u24lambda_u2425.build());
        }
        if (!acceptsNotification(19980)) {
            return;
        }
        Function1<Iap2Frame, Unit> function3 = this.send;
        Iap2Messages iap2Messages3 = Iap2Messages.INSTANCE;
        Iap2BodyBuilder $this$publishDeviceNotifications_u24lambda_u2426 = Iap2BodyBuilder.INSTANCE.opaque(19980);
        $this$publishDeviceNotifications_u24lambda_u2426.string(0, this.identifier);
        function3.invoke($this$publishDeviceNotifications_u24lambda_u2426.build());
    }

    private final Iap2Frame empty(int id) {
        Iap2Messages iap2Messages = Iap2Messages.INSTANCE;
        return Iap2BodyBuilder.INSTANCE.opaque(id).build();
    }

    private final byte[] identityFingerprint(Iap2BodyReader body) throws NoSuchAlgorithmException {
        MessageDigest $this$identityFingerprint_u24lambda_u2431 = MessageDigest.getInstance("SHA-256");
        Iterable $this$filter$iv = body.list();
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv : $this$filter$iv) {
            Iap2Parameter it = (Iap2Parameter) element$iv$iv;
            int id = it.getId();
            boolean z = true;
            if (!(id >= 0 && id < 6) && it.getId() != 34) {
                z = false;
            }
            if (z) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        Iterable $this$sortedBy$iv = (List) destination$iv$iv;
        Iterable $this$forEach$iv = CollectionsKt.sortedWith($this$sortedBy$iv, new Comparator() { // from class: com.carplay.iap2.controller.PhoneBootstrap$identityFingerprint$lambda$31$$inlined$sortedBy$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                Iap2Parameter it2 = (Iap2Parameter) t;
                Iap2Parameter it3 = (Iap2Parameter) t2;
                return ComparisonsKt.compareValues(Integer.valueOf(it2.getId()), Integer.valueOf(it3.getId()));
            }
        });
        for (Object element$iv : $this$forEach$iv) {
            Iap2Parameter it2 = (Iap2Parameter) element$iv;
            $this$identityFingerprint_u24lambda_u2431.update((byte) (it2.getId() >>> 8));
            $this$identityFingerprint_u24lambda_u2431.update((byte) it2.getId());
            $this$identityFingerprint_u24lambda_u2431.update((byte) (it2.getPayload().length >>> 8));
            $this$identityFingerprint_u24lambda_u2431.update((byte) it2.getPayload().length);
            $this$identityFingerprint_u24lambda_u2431.update(it2.getPayload());
        }
        byte[] bArrDigest = $this$identityFingerprint_u24lambda_u2431.digest();
        Intrinsics.checkNotNullExpressionValue(bArrDigest, "run(...)");
        return bArrDigest;
    }

    private final boolean acceptsNotification(int id) {
        if (this.purpose == PhoneBootstrapPurpose.CONTROL_SESSION) {
            return this.accessoryReceives.contains(Integer.valueOf(id));
        }
        if (this.profile == PhoneBootstrapProfile.GENERIC || this.accessoryReceives.contains(Integer.valueOf(id))) {
            return true;
        }
        String string = Integer.toString(id, CharsKt.checkRadix(16));
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        diagnostic("车机未声明接收 0x" + string + "，跳过可选通知");
        return false;
    }

    private final void diagnostic(String message) {
        try {
            this.onStage.invoke(message);
        } catch (Exception e) {
        }
    }
}
