package com.carplay.sender;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: PeerAddresses.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007J0\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00070\t2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\t2\u0006\u0010\r\u001a\u00020\u000eJ\u001c\u0010\u000f\u001a\u00020\u00102\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00070\t2\u0006\u0010\u0011\u001a\u00020\u0005¨\u0006\u0012"}, d2 = {"Lcom/carplay/sender/PeerAddresses;", "", "<init>", "()V", "parse", "Ljava/net/InetAddress;", "value", "", "resolve", "", "Lcom/carplay/sender/PeerAddress;", "values", "locals", "interfaceIndex", "", "matches", "", "candidate", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class PeerAddresses {
    public static final PeerAddresses INSTANCE = new PeerAddresses();

    private PeerAddresses() {
    }

    public final InetAddress parse(String value) throws UnknownHostException {
        InetAddress address;
        Intrinsics.checkNotNullParameter(value, "value");
        int length = value.length();
        if (!((1 <= length && length < 129) && Intrinsics.areEqual(value, StringsKt.trim((CharSequence) value).toString()))) {
            throw new IllegalArgumentException("车机 IP 地址格式无效".toString());
        }
        List parts = StringsKt.split$default((CharSequence) value, new char[]{'%'}, false, 0, 6, (Object) null);
        if (!(parts.size() <= 2 && (parts.size() == 1 || new Regex("[A-Za-z0-9_.-]{1,64}").matches((CharSequence) parts.get(1))))) {
            throw new IllegalArgumentException("车机 IPv6 区域标识无效".toString());
        }
        String literal = (String) parts.get(0);
        if (!StringsKt.contains$default((CharSequence) literal, ':', false, 2, (Object) null)) {
            if (!(parts.size() == 1 && new Regex("[0-9]{1,3}(\\.[0-9]{1,3}){3}").matches(literal))) {
                throw new IllegalArgumentException("车机必须提供数字 IP 地址".toString());
            }
            Iterable $this$map$iv = StringsKt.split$default((CharSequence) literal, new char[]{'.'}, false, 0, 6, (Object) null);
            Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
            for (Object item$iv$iv : $this$map$iv) {
                String it = (String) item$iv$iv;
                int number = Integer.parseInt(it);
                if (!(number >= 0 && number < 256)) {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
                destination$iv$iv.add(Integer.valueOf(number));
            }
            Iterable $this$map$iv2 = (List) destination$iv$iv;
            Collection destination$iv$iv2 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv2, 10));
            for (Object item$iv$iv2 : $this$map$iv2) {
                int it2 = ((Number) item$iv$iv2).intValue();
                destination$iv$iv2.add(Byte.valueOf((byte) it2));
            }
            byte[] bytes = CollectionsKt.toByteArray((List) destination$iv$iv2);
            address = InetAddress.getByAddress(bytes);
        } else {
            if (!new Regex("[0-9A-Fa-f:.]+").matches(literal)) {
                throw new IllegalArgumentException("车机必须提供数字 IP 地址".toString());
            }
            address = InetAddress.getByName(literal);
        }
        if (!((address.isAnyLocalAddress() || address.isLoopbackAddress() || address.isMulticastAddress()) ? false : true)) {
            throw new IllegalArgumentException("车机 IP 地址不可用于单播连接".toString());
        }
        Intrinsics.checkNotNull(address);
        return address;
    }

    public final List<PeerAddress> resolve(List<String> values, List<? extends InetAddress> locals, int interfaceIndex) throws UnknownHostException {
        Object element$iv;
        PeerAddress peerAddress;
        int scope;
        boolean z;
        Intrinsics.checkNotNullParameter(values, "values");
        Intrinsics.checkNotNullParameter(locals, "locals");
        if (!(values.size() <= 16)) {
            throw new IllegalArgumentException("车机地址数量超过限制".toString());
        }
        List<? extends InetAddress> $this$filter$iv = locals;
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv : $this$filter$iv) {
            InetAddress it = (InetAddress) element$iv$iv;
            if ((it.isAnyLocalAddress() || it.isLoopbackAddress() || it.isMulticastAddress()) ? false : true) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        List validLocals = (List) destination$iv$iv;
        List<String> $this$map$iv = values;
        Collection destination$iv$iv2 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (Object item$iv$iv : $this$map$iv) {
            String p0 = (String) item$iv$iv;
            destination$iv$iv2.add(parse(p0));
        }
        Iterable $this$mapNotNull$iv = (List) destination$iv$iv2;
        int $i$f$mapNotNull = 0;
        Collection destination$iv$iv3 = new ArrayList();
        Iterable $this$mapNotNullTo$iv$iv = $this$mapNotNull$iv;
        for (Object element$iv$iv$iv : $this$mapNotNullTo$iv$iv) {
            InetAddress remote = (InetAddress) element$iv$iv$iv;
            List $this$firstOrNull$iv = validLocals;
            Iterator it2 = $this$firstOrNull$iv.iterator();
            do {
                if (!it2.hasNext()) {
                    element$iv = null;
                    break;
                }
                element$iv = it2.next();
                InetAddress it3 = (InetAddress) element$iv;
                z = remote instanceof Inet4Address ? it3 instanceof Inet4Address : (remote instanceof Inet6Address) && (it3 instanceof Inet6Address) && ((Inet6Address) it3).isLinkLocalAddress() == ((Inet6Address) remote).isLinkLocalAddress();
            } while (!z);
            InetAddress local = (InetAddress) element$iv;
            if (local == null) {
                peerAddress = null;
            } else if ((remote instanceof Inet6Address) && ((Inet6Address) remote).isLinkLocalAddress()) {
                Integer numValueOf = Integer.valueOf(interfaceIndex);
                int it4 = numValueOf.intValue();
                int it5 = it4 > 0 ? 1 : 0;
                if (it5 == 0) {
                    numValueOf = null;
                }
                if (numValueOf != null) {
                    scope = numValueOf.intValue();
                } else {
                    Intrinsics.checkNotNull(local, "null cannot be cast to non-null type java.net.Inet6Address");
                    scope = ((Inet6Address) local).getScopeId();
                }
                if (scope <= 0) {
                    peerAddress = null;
                } else {
                    Inet6Address byAddress = Inet6Address.getByAddress((String) null, ((Inet6Address) remote).getAddress(), scope);
                    Intrinsics.checkNotNullExpressionValue(byAddress, "getByAddress(...)");
                    Inet6Address byAddress2 = Inet6Address.getByAddress((String) null, local.getAddress(), scope);
                    Intrinsics.checkNotNullExpressionValue(byAddress2, "getByAddress(...)");
                    peerAddress = new PeerAddress(byAddress, byAddress2);
                }
            } else {
                peerAddress = new PeerAddress(remote, local);
            }
            if (peerAddress != null) {
                destination$iv$iv3.add(peerAddress);
            }
            validLocals = validLocals;
            $i$f$mapNotNull = $i$f$mapNotNull;
            $this$mapNotNull$iv = $this$mapNotNull$iv;
            $this$mapNotNullTo$iv$iv = $this$mapNotNullTo$iv$iv;
        }
        Iterable $this$distinctBy$iv = (List) destination$iv$iv3;
        HashSet set$iv = new HashSet();
        ArrayList list$iv = new ArrayList();
        for (Object e$iv : $this$distinctBy$iv) {
            PeerAddress it6 = (PeerAddress) e$iv;
            byte[] address = it6.getRemote().getAddress();
            Intrinsics.checkNotNullExpressionValue(address, "getAddress(...)");
            if (set$iv.add(ArraysKt.toList(address))) {
                list$iv.add(e$iv);
            }
        }
        return list$iv;
    }

    public final boolean matches(List<String> values, InetAddress candidate) {
        Intrinsics.checkNotNullParameter(values, "values");
        Intrinsics.checkNotNullParameter(candidate, "candidate");
        List<String> $this$any$iv = values;
        if (($this$any$iv instanceof Collection) && $this$any$iv.isEmpty()) {
            return false;
        }
        for (Object element$iv : $this$any$iv) {
            String it = (String) element$iv;
            if (Arrays.equals(INSTANCE.parse(it).getAddress(), candidate.getAddress())) {
                return true;
            }
        }
        return false;
    }
}
