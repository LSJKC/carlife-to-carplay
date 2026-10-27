package com.carplay.sender;

import com.carlink.cast.CastSettings;
import com.carplay.airplay.AirPlayCrypto;
import com.carplay.airplay.PairSetup;
import com.carplay.airplay.PairVerify;
import com.carplay.airplay.Tlv8Codec;
import com.carplay.airplay.Tlv8Item;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: PhonePairing.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\n\u0018\u00002\u00020\u0001B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0018\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00070\u0005\u0012\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\t¢\u0006\u0004\b\f\u0010\rJ \u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u00072\u0006\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u0006H\u0002J9\u0010\u0012\u001a\u00020\u00072*\u0010\u0013\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00070\u00150\u0014\"\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00070\u0015H\u0002¢\u0006\u0002\u0010\u0017JU\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00070\u00192\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u001b\u001a\u00020\u00162*\u0010\u0013\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00070\u00150\u0014\"\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00070\u0015H\u0002¢\u0006\u0002\u0010\u001cJ$\u0010\u001d\u001a\u00020\n2\u0006\u0010\u001e\u001a\u00020\u00062\b\u0010\u001f\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010 \u001a\u0004\u0018\u00010\u0006J\u000e\u0010!\u001a\u00020\u00072\u0006\u0010\"\u001a\u00020\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R \u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\tX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006#"}, d2 = {"Lcom/carplay/sender/PhonePairing;", "", CastSettings.KEY_IDENTITY, "Lcom/carplay/sender/ControllerIdentity;", "exchange", "Lkotlin/Function2;", "", "", "save", "Lkotlin/Function1;", "Lcom/carplay/sender/PairedAccessory;", "", "<init>", "(Lcom/carplay/sender/ControllerIdentity;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function1;)V", "hkdf", "key", "salt", "info", "tlv", "fields", "", "Lkotlin/Pair;", "", "([Lkotlin/Pair;)[B", "request", "", "path", "expected", "(Ljava/lang/String;I[Lkotlin/Pair;)Ljava/util/Map;", "setup", "pin", "advertisedKey", "expectedIdentity", "verify", CastSettings.KEY_PEER, "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class PhonePairing {
    private final Function2<String, byte[], byte[]> exchange;
    private final ControllerIdentity identity;
    private final Function1<PairedAccessory, Unit> save;

    /* JADX WARN: Multi-variable type inference failed */
    public PhonePairing(ControllerIdentity identity, Function2<? super String, ? super byte[], byte[]> exchange, Function1<? super PairedAccessory, Unit> save) {
        Intrinsics.checkNotNullParameter(identity, "identity");
        Intrinsics.checkNotNullParameter(exchange, "exchange");
        Intrinsics.checkNotNullParameter(save, "save");
        this.identity = identity;
        this.exchange = exchange;
        this.save = save;
    }

    private final byte[] hkdf(byte[] key, String salt, String info) {
        AirPlayCrypto airPlayCrypto = AirPlayCrypto.INSTANCE;
        byte[] bytes = salt.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        byte[] bytes2 = info.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes2, "getBytes(...)");
        return AirPlayCrypto.hkdfSha512$default(airPlayCrypto, key, bytes, bytes2, 0, 8, null);
    }

    private final byte[] tlv(Pair<Integer, byte[]>... fields) {
        Tlv8Codec tlv8Codec = Tlv8Codec.INSTANCE;
        Collection destination$iv$iv = new ArrayList(fields.length);
        for (Pair<Integer, byte[]> pair : fields) {
            destination$iv$iv.add(new Tlv8Item(pair.getFirst().intValue(), pair.getSecond()));
        }
        return tlv8Codec.encode((List) destination$iv$iv);
    }

    private final Map<Integer, byte[]> request(String path, int expected, Pair<Integer, byte[]>... fields) {
        Map<Integer, byte[]> mapDecode = Tlv8Codec.INSTANCE.decode(this.exchange.invoke(path, tlv((Pair[]) Arrays.copyOf(fields, fields.length))));
        if (!(mapDecode.get(7) == null)) {
            byte[] bArr = mapDecode.get(7);
            throw new IllegalStateException(("车机拒绝配对：" + (bArr != null ? ArraysKt.firstOrNull(bArr) : null)).toString());
        }
        byte[] bArr2 = mapDecode.get(6);
        if (bArr2 != null && Arrays.equals(bArr2, new byte[]{(byte) expected})) {
            return mapDecode;
        }
        throw new IllegalStateException("配对阶段不匹配".toString());
    }

    public static /* synthetic */ PairedAccessory setup$default(PhonePairing phonePairing, String str, byte[] bArr, String str2, int i, Object obj) {
        if ((i & 4) != 0) {
            str2 = null;
        }
        return phonePairing.setup(str, bArr, str2);
    }

    /* JADX WARN: Code duplicated, block: B:26:0x01c2  */
    /* JADX WARN: Code duplicated, block: B:28:0x01ea  */
    /* JADX WARN: Code duplicated, block: B:30:0x01f5  */
    /* JADX WARN: Code duplicated, block: B:32:0x0203  */
    public final PairedAccessory setup(String pin, byte[] advertisedKey, String expectedIdentity) {
        boolean z;
        byte[] accessorySign;
        Intrinsics.checkNotNullParameter(pin, "pin");
        if (StringsKt.isBlank(pin)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        Map<Integer, byte[]> mapRequest = request("/pair-setup", 2, TuplesKt.to(6, new byte[]{1}), TuplesKt.to(0, new byte[]{0}));
        SrpController client = new SrpController((byte[]) MapsKt.getValue(mapRequest, 2), (byte[]) MapsKt.getValue(mapRequest, 3), pin);
        boolean z2 = false;
        if (!AccessoryTrust.INSTANCE.equal((byte[]) MapsKt.getValue(request("/pair-setup", 4, TuplesKt.to(6, new byte[]{3}), TuplesKt.to(3, client.getPublicKey()), TuplesKt.to(4, client.getProof())), 4), client.getServerProof())) {
            throw new IllegalStateException("SRP 服务端证明无效".toString());
        }
        byte[] key = hkdf(client.getKey(), PairSetup.ENCRYPT_SALT, PairSetup.ENCRYPT_INFO);
        byte[] id = this.identity.getId().getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(id, "getBytes(...)");
        byte[] signKey = hkdf(client.getKey(), PairSetup.CONTROLLER_SIGN_SALT, PairSetup.CONTROLLER_SIGN_INFO);
        byte[] sign = AirPlayCrypto.INSTANCE.ed25519Sign(this.identity.getPrivateKey(), ArraysKt.plus(ArraysKt.plus(signKey, id), this.identity.getPublicKey()));
        byte[] encrypted = AirPlayCrypto.chachaSeal$default(AirPlayCrypto.INSTANCE, key, AirPlayCrypto.INSTANCE.nonceLabel(PairSetup.MSG05), tlv(TuplesKt.to(1, id), TuplesKt.to(3, this.identity.getPublicKey()), TuplesKt.to(10, sign)), null, 8, null);
        Map<Integer, byte[]> mapDecode = Tlv8Codec.INSTANCE.decode(AirPlayCrypto.chachaOpen$default(AirPlayCrypto.INSTANCE, key, AirPlayCrypto.INSTANCE.nonceLabel(PairSetup.MSG06), (byte[]) MapsKt.getValue(request("/pair-setup", 6, TuplesKt.to(6, new byte[]{5}), TuplesKt.to(5, encrypted)), 5), null, 8, null));
        byte[] peerKey = (byte[]) MapsKt.getValue(mapDecode, 3);
        byte[] peerId = (byte[]) MapsKt.getValue(mapDecode, 1);
        if (!(peerKey.length == 32)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (!(advertisedKey == null || AccessoryTrust.INSTANCE.equal(peerKey, advertisedKey))) {
            throw new IllegalStateException("配对公钥与已认证车机不一致".toString());
        }
        String peerIdStr = new String(peerId, Charsets.UTF_8);
        if (expectedIdentity != null) {
            z = true;
            if (StringsKt.equals(peerIdStr, expectedIdentity, true)) {
            }
            if (z2) {
                throw new IllegalStateException("配对标识与车机声明不一致".toString());
            }
            accessorySign = hkdf(client.getKey(), PairSetup.ACCESSORY_SIGN_SALT, PairSetup.ACCESSORY_SIGN_INFO);
            if (AirPlayCrypto.INSTANCE.ed25519Verify(peerKey, ArraysKt.plus(ArraysKt.plus(accessorySign, peerId), peerKey), (byte[]) MapsKt.getValue(mapDecode, 10))) {
                throw new IllegalStateException("车机配对签名无效".toString());
            }
            PairedAccessory pairedAccessory = new PairedAccessory(peerIdStr, peerKey);
            this.save.invoke(pairedAccessory);
            return pairedAccessory;
        }
        z = true;
        z2 = z;
        if (z2) {
            throw new IllegalStateException("配对标识与车机声明不一致".toString());
        }
        accessorySign = hkdf(client.getKey(), PairSetup.ACCESSORY_SIGN_SALT, PairSetup.ACCESSORY_SIGN_INFO);
        if (AirPlayCrypto.INSTANCE.ed25519Verify(peerKey, ArraysKt.plus(ArraysKt.plus(accessorySign, peerId), peerKey), (byte[]) MapsKt.getValue(mapDecode, 10))) {
            throw new IllegalStateException("车机配对签名无效".toString());
        }
        PairedAccessory pairedAccessory2 = new PairedAccessory(peerIdStr, peerKey);
        this.save.invoke(pairedAccessory2);
        return pairedAccessory2;
    }

    public final byte[] verify(PairedAccessory peer) {
        Intrinsics.checkNotNullParameter(peer, "peer");
        AirPlayCrypto.X25519KeyPair ephemeral = AirPlayCrypto.INSTANCE.x25519Generate();
        Map<Integer, byte[]> mapRequest = request("/pair-verify", 2, TuplesKt.to(6, new byte[]{1}), TuplesKt.to(3, ephemeral.getPublicKey()));
        byte[] server = (byte[]) MapsKt.getValue(mapRequest, 3);
        if (!(server.length == 32)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        byte[] shared = AirPlayCrypto.INSTANCE.x25519Shared(ephemeral.getPrivateKey(), server);
        byte[] key = hkdf(shared, PairVerify.VERIFY_ENCRYPT_SALT, PairVerify.VERIFY_ENCRYPT_INFO);
        Map<Integer, byte[]> mapDecode = Tlv8Codec.INSTANCE.decode(AirPlayCrypto.chachaOpen$default(AirPlayCrypto.INSTANCE, key, AirPlayCrypto.INSTANCE.nonceLabel(PairVerify.MSG02), (byte[]) MapsKt.getValue(mapRequest, 5), null, 8, null));
        String peerId = new String((byte[]) MapsKt.getValue(mapDecode, 1), Charsets.UTF_8);
        if (!StringsKt.equals(peerId, peer.getId(), true)) {
            throw new IllegalStateException("车机配对身份发生变化".toString());
        }
        if (!AirPlayCrypto.INSTANCE.ed25519Verify(peer.getPublicKey(), ArraysKt.plus(ArraysKt.plus(server, (byte[]) MapsKt.getValue(mapDecode, 1)), ephemeral.getPublicKey()), (byte[]) MapsKt.getValue(mapDecode, 10))) {
            throw new IllegalStateException("车机验证签名无效".toString());
        }
        byte[] id = this.identity.getId().getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(id, "getBytes(...)");
        byte[] sign = AirPlayCrypto.INSTANCE.ed25519Sign(this.identity.getPrivateKey(), ArraysKt.plus(ArraysKt.plus(ephemeral.getPublicKey(), id), server));
        byte[] sealed = AirPlayCrypto.chachaSeal$default(AirPlayCrypto.INSTANCE, key, AirPlayCrypto.INSTANCE.nonceLabel(PairVerify.MSG03), tlv(TuplesKt.to(1, id), TuplesKt.to(10, sign)), null, 8, null);
        request("/pair-verify", 4, TuplesKt.to(6, new byte[]{3}), TuplesKt.to(5, sealed));
        return shared;
    }
}
