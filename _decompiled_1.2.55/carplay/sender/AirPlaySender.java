package com.carplay.sender;

import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.core.app.NotificationCompat;
import com.carlink.cast.CastSettings;
import com.carplay.adapter.carlife.KeyFrameRebuildGate;
import com.carplay.airplay.AirPlayCrypto;
import com.carplay.airplay.AirPlaySession;
import com.carplay.airplay.AirPlaySessionKt;
import com.carplay.airplay.AudioCodecKind;
import com.carplay.airplay.BplistCodec;
import com.carplay.airplay.CarPlayMediaEngine;
import com.carplay.airplay.CarPlayMediaEngineKt;
import com.carplay.airplay.ControlCipher;
import com.carplay.airplay.NtpClock;
import com.carplay.airplay.PairVerify;
import com.carplay.airplay.VideoInCar;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BiConsumer;
import javax.jmdns.impl.constants.DNSConstants;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.UByte;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.ByteCompanionObject;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.IntRange;
import kotlin.text.CharsKt;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import kotlinx.coroutines.scheduling.WorkQueueKt;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.slf4j.Marker;

/* JADX INFO: compiled from: AirPlaySender.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\u0098\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010#\n\u0000\n\u0002\u0010\"\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0003\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 ¶\u00012\u00020\u0001:\u0004µ\u0001¶\u0001Bå\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005\u0012\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\u0005\u0012\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u0005\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0012\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0\u0005\u0012\u0016\b\u0002\u0010\u0012\u001a\u0010\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u0005\u0012\u0010\b\u0002\u0010\u0015\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00170\u0016\u0012\"\b\u0002\u0010\u0018\u001a\u001c\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u0019\u0018\u00010\u0005\u0012\u0016\b\u0002\u0010\u001b\u001a\u0010\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\r\u0018\u00010\u0005\u0012\u0016\b\u0002\u0010\u001c\u001a\u0010\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\r\u0018\u00010\u0005¢\u0006\u0004\b\u001d\u0010\u001eJ\u0010\u0010x\u001a\u0002032\u0006\u0010y\u001a\u00020\u0007H\u0002J\n\u0010z\u001a\u0004\u0018\u00010hH\u0002J\u0018\u0010{\u001a\u00020\r2\u0006\u0010|\u001a\u00020}2\u0006\u0010~\u001a\u000203H\u0002J\u0011\u0010\u007f\u001a\u00020\r2\t\b\u0002\u0010\u0080\u0001\u001a\u000203J%\u0010\u0081\u0001\u001a\u0003H\u0082\u0001\"\t\b\u0000\u0010\u0082\u0001*\u00020\u00012\b\u0010\u0083\u0001\u001a\u0003H\u0082\u0001H\u0002¢\u0006\u0003\u0010\u0084\u0001J\u0013\u0010\u0085\u0001\u001a\u00020b2\b\u0010\u0086\u0001\u001a\u00030\u0087\u0001H\u0002J!\u0010\u0088\u0001\u001a\u00020\r2\u0007\u0010\u0089\u0001\u001a\u00020b2\r\u0010\u008a\u0001\u001a\b\u0012\u0004\u0012\u00020\r0\u0016H\u0002J\u0019\u0010\u008b\u0001\u001a\u00020?2\u0007\u0010\u008c\u0001\u001a\u00020\t2\u0007\u0010\u008d\u0001\u001a\u000203J\u001b\u0010\u008e\u0001\u001a\u00020\r2\u0007\u0010\u008f\u0001\u001a\u00020\u00172\u0007\u0010\u008c\u0001\u001a\u00020\tH\u0002J\u0012\u0010\u0090\u0001\u001a\u00020\r2\u0007\u0010\u0091\u0001\u001a\u000203H\u0002J,\u0010\u0092\u0001\u001a\u00020\r2\u0007\u0010\u0093\u0001\u001a\u00020\u00132\u0006\u0010>\u001a\u00020\u001a2\u0007\u0010\u0094\u0001\u001a\u00020\u00072\u0007\u0010\u0095\u0001\u001a\u00020\u0017H\u0002J#\u0010\u0096\u0001\u001a\u00020\r2\u0007\u0010\u0095\u0001\u001a\u00020\u00172\u0007\u0010\u0097\u0001\u001a\u00020\t2\u0006\u0010>\u001a\u00020\u001aH\u0002J\u0013\u0010\u0098\u0001\u001a\u00020\u00072\b\u0010\u0099\u0001\u001a\u00030\u009a\u0001H\u0002J\u0010\u0010\u009b\u0001\u001a\u00020\r2\u0007\u0010\u009c\u0001\u001a\u00020\u0007J\"\u0010\u009d\u0001\u001a\u00020\r2\u0007\u0010\u009c\u0001\u001a\u00020\u00072\u0007\u0010\u009e\u0001\u001a\u0002002\u0007\u0010\u009f\u0001\u001a\u000203J\u0019\u0010 \u0001\u001a\u00020\r2\u0007\u0010\u009c\u0001\u001a\u00020\u00072\u0007\u0010\u009e\u0001\u001a\u000200J\t\u0010¡\u0001\u001a\u00020\rH\u0002J\u0012\u0010¢\u0001\u001a\u00020\r2\u0007\u0010£\u0001\u001a\u00020MH\u0002J\u0012\u0010¤\u0001\u001a\u0002032\u0007\u0010£\u0001\u001a\u00020MH\u0002J\u0012\u0010¥\u0001\u001a\u00020\r2\u0007\u0010¦\u0001\u001a\u000203H\u0002J\t\u0010§\u0001\u001a\u00020\rH\u0002J\u0014\u0010¨\u0001\u001a\u00020\r2\t\b\u0002\u0010©\u0001\u001a\u000200H\u0002J\u0012\u0010ª\u0001\u001a\u00020\u00072\u0007\u0010«\u0001\u001a\u00020\u0007H\u0002J\u0011\u0010¬\u0001\u001a\u00020\t2\u0006\u0010>\u001a\u00020\u001aH\u0002J,\u0010\u00ad\u0001\u001a\u00020\r2\u0007\u0010®\u0001\u001a\u00020\u00072\u0006\u0010>\u001a\u00020\u001a2\u0007\u0010¯\u0001\u001a\u00020\u00072\u0007\u0010°\u0001\u001a\u00020\tH\u0002J\u0013\u0010±\u0001\u001a\u00030²\u00012\u0007\u0010³\u0001\u001a\u00020\u0007H\u0002J\t\u0010´\u0001\u001a\u00020\rH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0010\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0012\u001a\u0010\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0015\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00170\u0016X\u0082\u0004¢\u0006\u0002\n\u0000R(\u0010\u0018\u001a\u001c\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u0019\u0018\u00010\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u001b\u001a\u0010\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\r\u0018\u00010\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u001c\u001a\u0010\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\r\u0018\u00010\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00010\"X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020(X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010)\u001a\u0004\u0018\u00010\u0013X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010*\u001a\u0004\u0018\u00010\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010+\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020-X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010.\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u000200X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00101\u001a\u000200X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00102\u001a\u000203X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u00104\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00105\u001a\u000203X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00106\u001a\u000203X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00107\u001a\u00020-X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u00108\u001a\u000200X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u00109\u001a\u000200X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u000200X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010;\u001a\u000203X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u000200X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u000203X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010>\u001a\u0004\u0018\u00010?X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010@\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010A\u001a\u0004\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010B\u001a\u0004\u0018\u00010CX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010D\u001a\u0004\u0018\u00010EX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010F\u001a\u0004\u0018\u00010\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010G\u001a\u000200X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010H\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010I\u001a\u00020JX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010K\u001a\b\u0012\u0004\u0012\u00020M0LX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010N\u001a\u000200X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010O\u001a\u0004\u0018\u00010CX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010P\u001a\u000200X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010Q\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010R\u001a\u000200X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010S\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010T\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010U\u001a\u000203X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010V\u001a\u000200X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010W\u001a\u0004\u0018\u000100X\u0082\u000e¢\u0006\u0004\n\u0002\u0010XR\u0012\u0010Y\u001a\u0004\u0018\u000100X\u0082\u000e¢\u0006\u0004\n\u0002\u0010XR\u000e\u0010Z\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010[\u001a\u0004\u0018\u00010\\X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010]\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010^\u001a\b\u0012\u0004\u0012\u00020C0_X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010`\u001a\u000e\u0012\u0004\u0012\u00020b\u0012\u0004\u0012\u00020c0aX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010d\u001a\u000e\u0012\u0004\u0012\u00020b\u0012\u0004\u0012\u00020e0aX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010f\u001a\b\u0012\u0004\u0012\u00020b0_X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010g\u001a\u0004\u0018\u00010hX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010i\u001a\u000203X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010j\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010k\u001a\u000203X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010l\u001a\u0004\u0018\u00010\u001aX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010m\u001a\u000203X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010n\u001a\u000203X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010o\u001a\u00020-X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010p\u001a\u000203X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010q\u001a\b\u0012\u0004\u0012\u00020\u00070LX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010r\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R2\u0010s\u001a&\u0012\f\u0012\n u*\u0004\u0018\u00010b0b u*\u0012\u0012\f\u0012\n u*\u0004\u0018\u00010b0b\u0018\u00010v0tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010w\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006·\u0001"}, d2 = {"Lcom/carplay/sender/AirPlaySender;", "Ljava/io/Closeable;", CastSettings.KEY_IDENTITY, "Lcom/carplay/sender/ControllerIdentity;", "pair", "Lkotlin/Function1;", "Lcom/carplay/sender/PhonePairing;", "", "connect", "", "Ljava/net/Socket;", "bindDatagram", "Ljava/net/DatagramSocket;", "", "events", "Lcom/carplay/sender/SenderEvents;", "savePair", "Lcom/carplay/sender/PairedAccessory;", "legacyAuthentication", "Lcom/carplay/sender/RtspWire;", "Lcom/carplay/sender/LegacySecurity;", "localAddress", "Lkotlin/Function0;", "Ljava/net/InetAddress;", "audioProfileOrder", "", "Lcom/carplay/sender/SenderAudioFormat;", "audioProfileWorking", "audioProfileRejected", "<init>", "(Lcom/carplay/sender/ControllerIdentity;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lcom/carplay/sender/SenderEvents;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "closed", "Ljava/util/concurrent/atomic/AtomicBoolean;", "resources", "Ljava/util/concurrent/CopyOnWriteArrayList;", "queue", "Lcom/carplay/sender/VideoQueue;", "clock", "Lcom/carplay/airplay/NtpClock;", "modes", "Lcom/carplay/sender/SessionModes;", "control", "video", "videoKey", "videoWriteLock", "", "videoFramesSinceKeepAlive", "videoBytesSinceKeepAlive", "", "videoKeepAliveCount", "videoStreamReady", "", "avcC", "resendConfig", "configPending", "videoEpochLock", "videoEpoch", "configGeneration", "requiredConfigGeneration", "videoRecoveryPending", "videoRecoveryRound", "keyFramePending", "format", "Lcom/carplay/sender/SenderDisplay;", "audioSocket", "audioSyncSocket", "audioSyncThread", "Ljava/lang/Thread;", "audioAddress", "Ljava/net/InetSocketAddress;", "audioKey", "audioCounter", "audioSequence", "audioQueueLock", "Ljava/lang/Object;", "audioQueue", "Ljava/util/ArrayDeque;", "Lcom/carplay/sender/AirPlaySender$AudioChunk;", "audioGeneration", "audioWorker", "audioQueueDrops", "pcmCarry", "audioNextSendNanos", "pcmSamples", "pcmBase", "audioSuppressed", "audioPacketsSent", "lastAudioTimestamp", "Ljava/lang/Long;", "nextAudioTimestamp", "audioSyncSequence", "aacTimestampClock", "Lcom/carplay/sender/MonotonicAudioTimestamp;", "audioSsrc", "workers", "", "touchDevices", "", "", "Lcom/carplay/sender/HidInput;", "keyDevices", "Lcom/carplay/sender/HidKeys;", "armedInputs", "iap", "Lcom/carplay/sender/PhoneIapTunnel;", "commandIapStarting", "commandIapAttempts", "commandModes", "selectedAudio", "audioProfileStable", "audioProfileSetupComplete", "iapLock", "openingIap", "earlyIap", "earlyIapBytes", "reportedCommands", "", "kotlin.jvm.PlatformType", "", "touchReports", "receiveIap", "data", "ensureCommandIap", "receiveIapResponse", "message", "Lcom/carplay/sender/ControlMessage;", "typedOnly", "requestKeyFrame", "restore", "own", "T", "resource", "(Ljava/io/Closeable;)Ljava/io/Closeable;", "describeFailure", "error", "", "run", "name", "action", "start", "port", "enableAudio", "startAirPlayKeepAlive", "remote", "startVideoKeepAlive", "withStats", "setupAudio", "wire", "shared", "address", "startAudioSync", "controlPort", "ntpBytes", "value", "Ljava/math/BigInteger;", "config", "bytes", "frame", "ptsUs", "keyFrame", CastSettings.KEY_AUDIO, "startAudioWorker", "sendAudioChunk", "chunk", "isCurrentAudioChunkLocked", "setAudioOwnership", "phone", "invalidateAudioGenerationLocked", "paceAudioPacket", "intervalNanos", "pcmToWire", "pcm", "pcmTimestamp", "sendAudioPacket", "key", "payload", "timestamp", "handleCommand", "Lcom/carplay/sender/ControlReply;", "body", "close", "AudioChunk", "Companion", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class AirPlaySender implements Closeable {
    private static final int AAC_INPUT_BUFFER_SAMPLES = 48000;
    private static final int AAC_LATENCY_MS = 1000;
    private static final int AAC_OUTPUT_BUFFER_SAMPLES = 57552;
    private static final long AUDIO_PACKET_INTERVAL_NANOS = 5000000;
    private static final long AUDIO_PROFILE_STABLE_WINDOW_MS = 20000;
    private static final int AUDIO_QUEUE_LIMIT = 24;
    private static final long AUDIO_SYNC_INTERVAL_MS = 1000;
    private static final String IAP_DATASTREAM_UUID = "E9459FD0-BCAD-4C45-820F-1E72447EF2F2";
    private static final int MAX_COMMAND_IAP_ATTEMPTS = 4;
    private static final int PCM_INPUT_BUFFER_SAMPLES = 0;
    private static final int PCM_LATENCY_MS = 1000;
    private static final int PCM_OUTPUT_BUFFER_SAMPLES = 0;
    private static final int SCREEN_LATENCY_MS = 75;
    private static final int TOUCH_REPORTS_LOGGED = 8;
    private MonotonicAudioTimestamp aacTimestampClock;
    private final List<String> armedInputs;
    private InetSocketAddress audioAddress;
    private long audioCounter;
    private long audioGeneration;
    private byte[] audioKey;
    private long audioNextSendNanos;
    private long audioPacketsSent;
    private final Function1<List<SenderAudioFormat>, List<SenderAudioFormat>> audioProfileOrder;
    private final Function1<SenderAudioFormat, Unit> audioProfileRejected;
    private volatile boolean audioProfileSetupComplete;
    private volatile boolean audioProfileStable;
    private final Function1<SenderAudioFormat, Unit> audioProfileWorking;
    private final ArrayDeque<AudioChunk> audioQueue;
    private long audioQueueDrops;
    private final Object audioQueueLock;
    private int audioSequence;
    private DatagramSocket audioSocket;
    private final int audioSsrc;
    private boolean audioSuppressed;
    private volatile int audioSyncSequence;
    private DatagramSocket audioSyncSocket;
    private Thread audioSyncThread;
    private Thread audioWorker;
    private volatile byte[] avcC;
    private final Function1<DatagramSocket, Unit> bindDatagram;
    private final NtpClock clock;
    private final AtomicBoolean closed;
    private int commandIapAttempts;
    private boolean commandIapStarting;
    private boolean commandModes;
    private long configGeneration;
    private volatile boolean configPending;
    private final Function1<Integer, Socket> connect;
    private RtspWire control;
    private final ArrayDeque<byte[]> earlyIap;
    private int earlyIapBytes;
    private final SenderEvents events;
    private SenderDisplay format;
    private volatile PhoneIapTunnel iap;
    private final Object iapLock;
    private final ControllerIdentity identity;
    private final Map<String, HidKeys> keyDevices;
    private volatile boolean keyFramePending;
    private volatile Long lastAudioTimestamp;
    private final Function1<RtspWire, LegacySecurity> legacyAuthentication;
    private final Function0<InetAddress> localAddress;
    private final SessionModes modes;
    private volatile Long nextAudioTimestamp;
    private boolean openingIap;
    private final Function1<PhonePairing, byte[]> pair;
    private final int pcmBase;
    private byte[] pcmCarry;
    private int pcmSamples;
    private final VideoQueue queue;
    private final Set<String> reportedCommands;
    private long requiredConfigGeneration;
    private volatile boolean resendConfig;
    private final CopyOnWriteArrayList<Closeable> resources;
    private final Function1<PairedAccessory, Unit> savePair;
    private SenderAudioFormat selectedAudio;
    private final Map<String, HidInput> touchDevices;
    private int touchReports;
    private Socket video;
    private long videoBytesSinceKeepAlive;
    private long videoEpoch;
    private final Object videoEpochLock;
    private int videoFramesSinceKeepAlive;
    private long videoKeepAliveCount;
    private byte[] videoKey;
    private volatile boolean videoRecoveryPending;
    private long videoRecoveryRound;
    private volatile boolean videoStreamReady;
    private final Object videoWriteLock;
    private List<Thread> workers;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final byte[] IAP2_MARKER = {-1, 85, 2, 0, -18, 16};
    private static final List<String> CONTROLLER_FEATURES = CollectionsKt.listOf((Object[]) new String[]{"uiContext", "viewAreas", "cornerMasks", "focusTransfer", "h.264Level5.1", AirPlaySessionKt.MAIN_BUFFERED_FEATURE, "altScreen", "enhancedSiri", "hevc", "sessionManagement", "logTransfer", "iAPChannel", "vehicleStateProtocol", VideoInCar.FEATURE});
    private static final List<Pair<Long, String>> AUDIO_BITS = CollectionsKt.listOf((Object[]) new Pair[]{TuplesKt.to(16L, "PCM16k/1"), TuplesKt.to(32L, "PCM16k/2"), TuplesKt.to(64L, "PCM24k/1"), TuplesKt.to(128L, "PCM24k/2"), TuplesKt.to(1024L, "PCM44.1k/1"), TuplesKt.to(2048L, "PCM44.1k/2"), TuplesKt.to(16384L, "PCM48k/1"), TuplesKt.to(32768L, "PCM48k/2"), TuplesKt.to(4194304L, "AAC-LC44.1k/2"), TuplesKt.to(8388608L, "AAC-LC48k/2"), TuplesKt.to(Long.valueOf(CarPlayMediaEngine.OPUS_24K), "OPUS24k/1"), TuplesKt.to(Long.valueOf(CarPlayMediaEngine.OPUS_48K), "OPUS48k/1")});
    private static final Map<String, String> plistHeaders = MapsKt.mapOf(TuplesKt.to("Content-Type", AirPlaySession.PLIST_CONTENT_TYPE));
    private static final Map<String, String> pairingHeaders = MapsKt.mapOf(TuplesKt.to("Content-Type", AirPlaySession.PAIRING_CONTENT_TYPE), TuplesKt.to("X-Apple-HKP", "3"));

    /* JADX INFO: compiled from: AirPlaySender.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[SenderControlKind.values().length];
            try {
                iArr[SenderControlKind.WIFI_DATASTREAM.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[SenderControlKind.WIFI_COMMAND.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[SenderControlKind.AUTHENTICATED_BLUETOOTH.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public AirPlaySender(ControllerIdentity identity, Function1<? super PhonePairing, byte[]> pair, Function1<? super Integer, ? extends Socket> connect, Function1<? super DatagramSocket, Unit> bindDatagram, SenderEvents events, Function1<? super PairedAccessory, Unit> savePair, Function1<? super RtspWire, LegacySecurity> function1, Function0<? extends InetAddress> localAddress, Function1<? super List<SenderAudioFormat>, ? extends List<SenderAudioFormat>> function2, Function1<? super SenderAudioFormat, Unit> function3, Function1<? super SenderAudioFormat, Unit> function4) {
        Intrinsics.checkNotNullParameter(identity, "identity");
        Intrinsics.checkNotNullParameter(pair, "pair");
        Intrinsics.checkNotNullParameter(connect, "connect");
        Intrinsics.checkNotNullParameter(bindDatagram, "bindDatagram");
        Intrinsics.checkNotNullParameter(events, "events");
        Intrinsics.checkNotNullParameter(savePair, "savePair");
        Intrinsics.checkNotNullParameter(localAddress, "localAddress");
        this.identity = identity;
        this.pair = pair;
        this.connect = connect;
        this.bindDatagram = bindDatagram;
        this.events = events;
        this.savePair = savePair;
        this.legacyAuthentication = function1;
        this.localAddress = localAddress;
        this.audioProfileOrder = function2;
        this.audioProfileWorking = function3;
        this.audioProfileRejected = function4;
        this.closed = new AtomicBoolean();
        this.resources = new CopyOnWriteArrayList<>();
        this.queue = new VideoQueue(0, 0, 3, null);
        this.clock = new NtpClock(this.localAddress, this.bindDatagram);
        this.modes = new SessionModes();
        this.videoWriteLock = new Object();
        this.configPending = true;
        this.videoEpochLock = new Object();
        this.audioSequence = new SecureRandom().nextInt(65536);
        this.audioQueueLock = new Object();
        this.audioQueue = new ArrayDeque<>();
        this.pcmCarry = new byte[0];
        this.pcmBase = new SecureRandom().nextInt(BasicMeasure.EXACTLY);
        this.audioSsrc = new SecureRandom().nextInt();
        this.workers = new ArrayList();
        this.touchDevices = new LinkedHashMap();
        this.keyDevices = new LinkedHashMap();
        this.armedInputs = new ArrayList();
        this.iapLock = new Object();
        this.earlyIap = new ArrayDeque<>();
        this.reportedCommands = Collections.synchronizedSet(new LinkedHashSet());
    }

    public /* synthetic */ AirPlaySender(ControllerIdentity controllerIdentity, Function1 function1, Function1 function2, Function1 function3, SenderEvents senderEvents, Function1 function4, Function1 function5, AnonymousClass1 anonymousClass1, Function1 function6, Function1 function7, Function1 function8, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(controllerIdentity, function1, function2, function3, senderEvents, function4, (i & 64) != 0 ? null : function5, (i & 128) != 0 ? new Function0() { // from class: com.carplay.sender.AirPlaySender.1
            @Override // kotlin.jvm.functions.Function0
            public final Void invoke() {
                return null;
            }
        } : anonymousClass1, (i & 256) != 0 ? null : function6, (i & 512) != 0 ? null : function7, (i & 1024) != 0 ? null : function8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: AirPlaySender.kt */
    @Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\t\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005¢\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0005HÆ\u0003J'\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\f¨\u0006\u0019"}, d2 = {"Lcom/carplay/sender/AirPlaySender$AudioChunk;", "", "bytes", "", "ptsUs", "", "generation", "<init>", "([BJJ)V", "getBytes", "()[B", "getPtsUs", "()J", "getGeneration", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    static final /* data */ class AudioChunk {
        private final byte[] bytes;
        private final long generation;
        private final long ptsUs;

        public static /* synthetic */ AudioChunk copy$default(AudioChunk audioChunk, byte[] bArr, long j, long j2, int i, Object obj) {
            if ((i & 1) != 0) {
                bArr = audioChunk.bytes;
            }
            if ((i & 2) != 0) {
                j = audioChunk.ptsUs;
            }
            if ((i & 4) != 0) {
                j2 = audioChunk.generation;
            }
            return audioChunk.copy(bArr, j, j2);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final byte[] getBytes() {
            return this.bytes;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final long getPtsUs() {
            return this.ptsUs;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final long getGeneration() {
            return this.generation;
        }

        public final AudioChunk copy(byte[] bytes, long ptsUs, long generation) {
            Intrinsics.checkNotNullParameter(bytes, "bytes");
            return new AudioChunk(bytes, ptsUs, generation);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof AudioChunk)) {
                return false;
            }
            AudioChunk audioChunk = (AudioChunk) other;
            return Intrinsics.areEqual(this.bytes, audioChunk.bytes) && this.ptsUs == audioChunk.ptsUs && this.generation == audioChunk.generation;
        }

        public int hashCode() {
            return (((Arrays.hashCode(this.bytes) * 31) + Long.hashCode(this.ptsUs)) * 31) + Long.hashCode(this.generation);
        }

        public String toString() {
            return "AudioChunk(bytes=" + Arrays.toString(this.bytes) + ", ptsUs=" + this.ptsUs + ", generation=" + this.generation + ")";
        }

        public AudioChunk(byte[] bytes, long ptsUs, long generation) {
            Intrinsics.checkNotNullParameter(bytes, "bytes");
            this.bytes = bytes;
            this.ptsUs = ptsUs;
            this.generation = generation;
        }

        public final byte[] getBytes() {
            return this.bytes;
        }

        public final long getGeneration() {
            return this.generation;
        }

        public final long getPtsUs() {
            return this.ptsUs;
        }
    }

    private final boolean receiveIap(byte[] data) {
        boolean zOffer;
        synchronized (this.iapLock) {
            zOffer = false;
            if (!this.closed.get()) {
                PhoneIapTunnel it = this.iap;
                if (it != null) {
                    zOffer = it.offer(data);
                } else if (this.openingIap) {
                    if (!(data.length <= 1048576 - this.earlyIapBytes)) {
                        throw new IllegalStateException("iAP 启动接收队列超出限制".toString());
                    }
                    ArrayDeque<byte[]> arrayDeque = this.earlyIap;
                    byte[] bArrCopyOf = Arrays.copyOf(data, data.length);
                    Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
                    arrayDeque.addLast(bArrCopyOf);
                    this.earlyIapBytes += data.length;
                    zOffer = true;
                }
            }
        }
        return zOffer;
    }

    private final PhoneIapTunnel ensureCommandIap() {
        final RtspWire wire = this.control;
        if (wire == null) {
            return null;
        }
        synchronized (this.iapLock) {
            if (this.closed.get()) {
                return null;
            }
            PhoneIapTunnel it = this.iap;
            if (it != null) {
                if (!it.isClosed()) {
                    return it;
                }
                this.iap = null;
            }
            if (this.commandIapStarting) {
                return this.iap;
            }
            if (this.commandIapAttempts >= 4) {
                return null;
            }
            this.commandIapAttempts++;
            this.commandIapStarting = true;
            final PhoneIapTunnel tunnel = new PhoneIapTunnel(null, null, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return AirPlaySender.ensureCommandIap$lambda$9$lambda$5(wire, this, (byte[]) obj);
                }
            }, 3, null);
            this.iap = tunnel;
            this.openingIap = false;
            this.earlyIapBytes = 0;
            CompletableFuture<Unit> completableFutureIapControl = this.events.iapControl(tunnel);
            this.events.diagnostic("车机通过 Wi-Fi 命令通道发起 iAP2；已建立命令链路（第 " + this.commandIapAttempts + " 次）");
            final Function2 function2 = new Function2() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda2
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return AirPlaySender.ensureCommandIap$lambda$9$lambda$7(this.f$0, tunnel, (Unit) obj, (Throwable) obj2);
                }
            };
            completableFutureIapControl.whenComplete(new BiConsumer() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda3
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    function2.invoke(obj, obj2);
                }
            });
            return tunnel;
        }
    }

    static final Unit ensureCommandIap$lambda$9$lambda$5(RtspWire $wire, final AirPlaySender this$0, byte[] data) {
        Intrinsics.checkNotNullParameter(data, "data");
        $wire.request("POST", "/command", INSTANCE.encode(MapsKt.mapOf(TuplesKt.to("type", "iAPSendMessage"), TuplesKt.to("params", MapsKt.mapOf(TuplesKt.to("data", data))))), plistHeaders, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda14
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AirPlaySender.ensureCommandIap$lambda$9$lambda$5$lambda$4(this.f$0, (ControlMessage) obj);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit ensureCommandIap$lambda$9$lambda$5$lambda$4(AirPlaySender this$0, ControlMessage it) throws ProtocolException {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.receiveIapResponse(it, false);
        return Unit.INSTANCE;
    }

    static final Unit ensureCommandIap$lambda$9$lambda$7(AirPlaySender this$0, PhoneIapTunnel $tunnel, Unit unit, Throwable error) {
        synchronized (this$0.iapLock) {
            this$0.commandIapStarting = false;
            if (error != null) {
                SenderEvents senderEvents = this$0.events;
                String message = error.getMessage();
                if (message == null) {
                    message = error.getClass().getSimpleName();
                }
                senderEvents.diagnostic("Wi-Fi 命令通道 iAP2 认证未完成：" + message);
                if (this$0.iap == $tunnel) {
                    this$0.iap = null;
                }
            }
            Unit unit2 = Unit.INSTANCE;
        }
        return Unit.INSTANCE;
    }

    private final void receiveIapResponse(ControlMessage message, boolean typedOnly) throws ProtocolException {
        if (message.getBody().length == 0) {
            return;
        }
        try {
            Object decoded = BplistCodec.INSTANCE.decode(message.getBody());
            if (typedOnly) {
                Map map = decoded instanceof Map ? (Map) decoded : null;
                if (!Intrinsics.areEqual(map != null ? map.get("type") : null, "iAPSendMessage")) {
                    return;
                }
            }
            byte[] received = IapCommandData.INSTANCE.payload(decoded, true);
            if (received != null && !receiveIap(received)) {
                throw new IllegalStateException("iAP 响应数据未被控制通道接收".toString());
            }
            SenderEvents senderEvents = this.events;
            int length = message.getBody().length;
            Map map2 = decoded instanceof Map ? (Map) decoded : null;
            senderEvents.diagnostic("iAP 命令回复：bodyBytes=" + length + "，字段数=" + (map2 != null ? map2.size() : 0) + "，控制字节=" + (received != null ? received.length : 0));
        } catch (Exception error) {
            if (typedOnly) {
                return;
            }
            ProtocolException $this$receiveIapResponse_u24lambda_u2410 = new ProtocolException("iAP 命令响应不是有效 plist");
            $this$receiveIapResponse_u24lambda_u2410.initCause(error);
            throw $this$receiveIapResponse_u24lambda_u2410;
        }
    }

    public static /* synthetic */ void requestKeyFrame$default(AirPlaySender airPlaySender, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        airPlaySender.requestKeyFrame(z);
    }

    public final void requestKeyFrame(boolean restore) {
        if (this.closed.get()) {
            return;
        }
        synchronized (this.videoEpochLock) {
            if (!this.keyFramePending && !this.videoRecoveryPending) {
                this.videoEpoch++;
                this.videoRecoveryRound++;
                long round = this.videoRecoveryRound;
                this.resendConfig = true;
                this.configPending = true;
                this.videoRecoveryPending = true;
                this.requiredConfigGeneration = this.configGeneration + 1;
                this.keyFramePending = true;
                Unit unit = Unit.INSTANCE;
                this.queue.resetForKeyFrame();
                this.events.diagnostic("视频恢复轮次 #" + round + "：" + (restore ? "屏幕恢复" : "关键帧请求") + "，epoch=" + this.videoEpoch + "，要求 CONFIG generation >= " + this.requiredConfigGeneration);
                SenderEvents senderEvents = this.events;
                if (restore) {
                    senderEvents.screenRestoreKeyFrameRequested();
                    return;
                } else {
                    senderEvents.keyFrameRequested();
                    return;
                }
            }
            this.events.diagnostic("视频恢复请求已合并：上一轮仍在等待新 IDR");
        }
    }

    private final <T extends Closeable> T own(T resource) throws IOException {
        this.resources.add(resource);
        if (this.closed.get()) {
            resource.close();
            throw new IllegalStateException("会话已停止".toString());
        }
        return resource;
    }

    private final String describeFailure(Throwable error) {
        StringBuilder $this$describeFailure_u24lambda_u2416 = new StringBuilder();
        Throwable cause = error;
        for (int depth = 0; cause != null && depth < 4; depth++) {
            if (depth > 0) {
                $this$describeFailure_u24lambda_u2416.append(" <- ");
            }
            $this$describeFailure_u24lambda_u2416.append(cause.getClass().getSimpleName());
            String it = cause.getMessage();
            if (it != null) {
                if (StringsKt.isBlank(it)) {
                    it = null;
                }
                if (it != null) {
                    $this$describeFailure_u24lambda_u2416.append(": ").append(it);
                }
            }
            cause = cause.getCause();
        }
        StackTraceElement[] stackTrace = error.getStackTrace();
        Intrinsics.checkNotNullExpressionValue(stackTrace, "getStackTrace(...)");
        Iterable $this$forEach$iv = ArraysKt.take(stackTrace, 8);
        for (Object element$iv : $this$forEach$iv) {
            $this$describeFailure_u24lambda_u2416.append("\n  at ").append((StackTraceElement) element$iv);
        }
        return $this$describeFailure_u24lambda_u2416.toString();
    }

    private final void run(final String name, final Function0<Unit> action) {
        Thread worker = new Thread(new Runnable() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                AirPlaySender.run$lambda$17(action, this, name);
            }
        }, name);
        worker.setDaemon(true);
        synchronized (this.workers) {
            this.workers.add(worker);
            Unit unit = Unit.INSTANCE;
        }
        worker.start();
    }

    static final void run$lambda$17(Function0 $action, AirPlaySender this$0, String $name) {
        try {
            $action.invoke();
        } catch (Exception error) {
            if (!this$0.closed.get()) {
                this$0.events.diagnostic($name + " 异常：" + this$0.describeFailure(error));
                this$0.events.failed(error);
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:191:0x074e  */
    /* JADX WARN: Code duplicated, block: B:194:0x0796  */
    /* JADX WARN: Code duplicated, block: B:242:0x0981  */
    /* JADX WARN: Code duplicated, block: B:335:0x0b66  */
    /* JADX WARN: Code duplicated, block: B:337:0x0b6c  */
    /* JADX WARN: Code duplicated, block: B:338:0x0bb5  */
    /* JADX WARN: Code duplicated, block: B:340:0x0bb9  */
    /* JADX WARN: Code duplicated, block: B:343:0x0c17  */
    /* JADX WARN: Code duplicated, block: B:344:0x0c89  */
    /* JADX WARN: Code duplicated, block: B:353:0x0cd5  */
    /* JADX WARN: Code duplicated, block: B:356:0x0d34 A[LOOP:10: B:354:0x0d2e->B:356:0x0d34, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:359:0x0da4  */
    /* JADX WARN: Code duplicated, block: B:362:0x0e05  */
    /* JADX WARN: Code duplicated, block: B:367:0x0ea4  */
    /* JADX WARN: Code duplicated, block: B:372:0x0ebd A[Catch: all -> 0x0ecd, TRY_LEAVE, TryCatch #8 {all -> 0x0ecd, blocks: (B:370:0x0eb3, B:372:0x0ebd, B:380:0x0ee2), top: B:579:0x0eb3 }] */
    /* JADX WARN: Code duplicated, block: B:376:0x0ed7  */
    /* JADX WARN: Code duplicated, block: B:379:0x0ee1  */
    /* JADX WARN: Code duplicated, block: B:382:0x0ee5  */
    /* JADX WARN: Code duplicated, block: B:388:0x0f1b  */
    /* JADX WARN: Code duplicated, block: B:389:0x0f1d  */
    /* JADX WARN: Code duplicated, block: B:392:0x0f2f  */
    /* JADX WARN: Code duplicated, block: B:394:0x0f35  */
    /* JADX WARN: Code duplicated, block: B:396:0x0f3d  */
    /* JADX WARN: Code duplicated, block: B:397:0x0f47  */
    /* JADX WARN: Code duplicated, block: B:399:0x0f54  */
    /* JADX WARN: Code duplicated, block: B:400:0x0f74  */
    /* JADX WARN: Code duplicated, block: B:402:0x0f7c  */
    /* JADX WARN: Code duplicated, block: B:405:0x0fc6  */
    /* JADX WARN: Code duplicated, block: B:408:0x0fcf A[Catch: all -> 0x1460, TryCatch #0 {all -> 0x1460, blocks: (B:406:0x0fc7, B:408:0x0fcf, B:409:0x0fd3, B:422:0x1012), top: B:563:0x0fc7 }] */
    /* JADX WARN: Code duplicated, block: B:411:0x0fdd A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:414:0x0ff3  */
    /* JADX WARN: Code duplicated, block: B:417:0x1001  */
    /* JADX WARN: Code duplicated, block: B:419:0x1004 A[LOOP:11: B:409:0x0fd3->B:419:0x1004, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:425:0x101a  */
    /* JADX WARN: Code duplicated, block: B:426:0x1025  */
    /* JADX WARN: Code duplicated, block: B:429:0x1029  */
    /* JADX WARN: Code duplicated, block: B:432:0x1041  */
    /* JADX WARN: Code duplicated, block: B:434:0x104a  */
    /* JADX WARN: Code duplicated, block: B:437:0x1058  */
    /* JADX WARN: Code duplicated, block: B:439:0x1065  */
    /* JADX WARN: Code duplicated, block: B:442:0x1073  */
    /* JADX WARN: Code duplicated, block: B:444:0x1083  */
    /* JADX WARN: Code duplicated, block: B:445:0x108a  */
    /* JADX WARN: Code duplicated, block: B:447:0x108d  */
    /* JADX WARN: Code duplicated, block: B:450:0x1099  */
    /* JADX WARN: Code duplicated, block: B:453:0x10a7  */
    /* JADX WARN: Code duplicated, block: B:455:0x10bc  */
    /* JADX WARN: Code duplicated, block: B:456:0x10bf  */
    /* JADX WARN: Code duplicated, block: B:459:0x10cd  */
    /* JADX WARN: Code duplicated, block: B:460:0x10d0  */
    /* JADX WARN: Code duplicated, block: B:462:0x114b  */
    /* JADX WARN: Code duplicated, block: B:464:0x1159  */
    /* JADX WARN: Code duplicated, block: B:466:0x1163  */
    /* JADX WARN: Code duplicated, block: B:469:0x1188  */
    /* JADX WARN: Code duplicated, block: B:470:0x118b  */
    /* JADX WARN: Code duplicated, block: B:473:0x1216  */
    /* JADX WARN: Code duplicated, block: B:477:0x1267  */
    /* JADX WARN: Code duplicated, block: B:479:0x1281  */
    /* JADX WARN: Code duplicated, block: B:480:0x1283  */
    /* JADX WARN: Code duplicated, block: B:483:0x1288 A[LOOP:12: B:475:0x1261->B:483:0x1288, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:487:0x1292  */
    /* JADX WARN: Code duplicated, block: B:492:0x12a8  */
    /* JADX WARN: Code duplicated, block: B:494:0x12ab  */
    /* JADX WARN: Code duplicated, block: B:496:0x135e  */
    /* JADX WARN: Code duplicated, block: B:498:0x1368  */
    /* JADX WARN: Code duplicated, block: B:499:0x136c  */
    /* JADX WARN: Code duplicated, block: B:504:0x1378  */
    /* JADX WARN: Code duplicated, block: B:507:0x137c  */
    /* JADX WARN: Code duplicated, block: B:508:0x137e  */
    /* JADX WARN: Code duplicated, block: B:511:0x138a  */
    /* JADX WARN: Code duplicated, block: B:513:0x138e  */
    /* JADX WARN: Code duplicated, block: B:515:0x13a4  */
    /* JADX WARN: Code duplicated, block: B:516:0x13c5  */
    /* JADX WARN: Code duplicated, block: B:518:0x13d1  */
    /* JADX WARN: Code duplicated, block: B:522:0x13fb  */
    /* JADX WARN: Code duplicated, block: B:524:0x1404  */
    /* JADX WARN: Code duplicated, block: B:527:0x1414  */
    /* JADX WARN: Code duplicated, block: B:529:0x1421  */
    /* JADX WARN: Code duplicated, block: B:532:0x142e  */
    /* JADX WARN: Code duplicated, block: B:534:0x143c  */
    /* JADX WARN: Code duplicated, block: B:536:0x1448  */
    /* JADX WARN: Code duplicated, block: B:545:0x146c  */
    /* JADX WARN: Code duplicated, block: B:555:0x1490  */
    /* JADX WARN: Code duplicated, block: B:557:0x149e  */
    /* JADX WARN: Code duplicated, block: B:579:0x0eb3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:584:0x0fdf A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:608:0x079a A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:626:0x1005 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:628:0x1286 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:629:0x128b A[SYNTHETIC] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v104 */
    /* JADX WARN: Type inference failed for: r0v105, types: [boolean] */
    /* JADX WARN: Type inference failed for: r0v143 */
    /* JADX WARN: Type inference failed for: r11v76 */
    /* JADX WARN: Type inference failed for: r11v77 */
    /* JADX WARN: Type inference failed for: r11v80 */
    /* JADX WARN: Type inference failed for: r12v119 */
    /* JADX WARN: Type inference failed for: r12v26 */
    /* JADX WARN: Type inference failed for: r12v27, types: [boolean, int] */
    public final SenderDisplay start(int port, boolean enableAudio) {
        ArrayList arrayListEmptyList;
        Socket socket;
        ArrayList arrayListEmptyList2;
        int i;
        ArrayList arrayListEmptyList3;
        byte[] bArr;
        String str;
        Object next;
        boolean z;
        SenderEvents senderEvents;
        int iNumber;
        int i2;
        final RtspWire rtspWire;
        List<String> list;
        long jNextLong;
        Map<?, ?> mapPlist;
        SenderControlTransport senderControlTransportControl;
        SenderControlTransport senderControlTransport;
        ?? r12;
        ?? r0;
        Function1<Integer, Socket> function1;
        Integer port2;
        PhoneIapTunnel phoneIapTunnel;
        PhoneIapTunnel phoneIapTunnel2;
        CompletableFuture<Unit> completableFutureIapControl;
        final CompletableFuture<Unit> completableFuture;
        final SenderControlTransport senderControlTransport2;
        String str2;
        long jNextLong2;
        LinkedHashMap linkedHashMapLinkedMapOf;
        String uuid;
        Iterator it;
        Object obj;
        Map map;
        int iNumber2;
        boolean z2;
        boolean z3;
        SenderAudioFormat senderAudioFormat;
        final SenderAudioFormat senderAudioFormat2;
        Object obj2;
        Number number;
        boolean z4;
        Object next2;
        LinkedHashMap linkedHashMap;
        int i3;
        boolean zHasHighQualityMediaCapability;
        List<SenderAudioFormat> listAudioCandidates;
        Function1<List<SenderAudioFormat>, List<SenderAudioFormat>> function2;
        List<SenderAudioFormat> listEmptyList;
        List<SenderAudioFormat> list2;
        List<SenderAudioFormat> list3;
        SenderAudioFormat senderAudioFormat3;
        SenderAudioFormat senderAudioFormat4;
        String str3;
        String str4;
        byte[] bArrRemoveFirst;
        ?? r11;
        boolean z5;
        Object next3;
        List list4;
        boolean z6;
        List list5;
        int i4;
        Iterator it2;
        Map<?, ?> map2;
        String str5;
        String str6;
        Socket socket2;
        PhonePairing phonePairing;
        Iterable iterable;
        int i5;
        Iterator it3;
        String str7;
        Closeable closeableOwn = own(this.connect.invoke(Integer.valueOf(port)));
        Socket socket3 = (Socket) closeableOwn;
        socket3.setSoTimeout(15000);
        socket3.setTcpNoDelay(true);
        Socket socket4 = (Socket) closeableOwn;
        final RtspWire rtspWire2 = (RtspWire) own(new RtspWire(socket4, new AirPlaySender$start$wire$1(this.events)));
        this.control = rtspWire2;
        PhonePairing phonePairing2 = new PhonePairing(this.identity, new Function2() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda16
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj3, Object obj4) {
                return AirPlaySender.start$lambda$21(rtspWire2, (String) obj3, (byte[]) obj4);
            }
        }, this.savePair);
        Function1<RtspWire, LegacySecurity> function3 = this.legacyAuthentication;
        if (function3 != null) {
            function3.invoke(rtspWire2);
        }
        byte[] bArrInvoke = this.pair.invoke(phonePairing2);
        rtspWire2.setCipher(new ControlCipher(start$key(bArrInvoke, PairVerify.CONTROL_SALT, PairVerify.CONTROL_READ_KEY_INFO), start$key(bArrInvoke, PairVerify.CONTROL_SALT, PairVerify.CONTROL_WRITE_KEY_INFO)));
        int iListen = this.clock.listen();
        this.resources.add(this.clock);
        Map mapPlist2 = INSTANCE.plist(RtspWire.request$default(rtspWire2, "SETUP", "/", INSTANCE.encode(MapsKt.mapOf(TuplesKt.to("deviceID", this.identity.getId()), TuplesKt.to("name", "CarPlay Android"), TuplesKt.to("model", "Android"), TuplesKt.to("sourceVersion", "320.17"), TuplesKt.to("sessionUUID", UUID.randomUUID().toString()), TuplesKt.to("timingPort", Integer.valueOf(iListen)), TuplesKt.to("keepAliveLowPower", true), TuplesKt.to("features", CONTROLLER_FEATURES))), plistHeaders, null, 16, null).getBody());
        Object obj3 = mapPlist2.get("enabledFeatures");
        Iterable iterable2 = obj3 instanceof List ? (List) obj3 : null;
        if (iterable2 != null) {
            Iterable iterable3 = iterable2;
            ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable3, 10));
            Iterator it4 = iterable3.iterator();
            while (it4.hasNext()) {
                arrayList.add(String.valueOf(it4.next()));
            }
            arrayListEmptyList = arrayList;
        } else {
            arrayListEmptyList = null;
        }
        if (arrayListEmptyList == null) {
            arrayListEmptyList = CollectionsKt.emptyList();
        }
        SenderEvents senderEvents2 = this.events;
        List listListOf = arrayListEmptyList;
        if (listListOf.isEmpty()) {
            listListOf = CollectionsKt.listOf("未返回");
        }
        senderEvents2.diagnostic("AirPlay 能力协商：车机启用=" + listListOf + "；本机提议=" + CollectionsKt.joinToString$default(CONTROLLER_FEATURES, ",", null, null, 0, null, null, 62, null));
        int iNumber3 = INSTANCE.number(mapPlist2.get("timingPort"));
        if (!(1 <= iNumber3 && iNumber3 < 65536)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        NtpClock ntpClock = this.clock;
        InetAddress inetAddress = socket4.getInetAddress();
        Intrinsics.checkNotNullExpressionValue(inetAddress, "getInetAddress(...)");
        ntpClock.start(inetAddress, iNumber3);
        Object obj4 = mapPlist2.get("keepAlivePort");
        Number number2 = obj4 instanceof Number ? (Number) obj4 : null;
        if (number2 != null) {
            Integer numValueOf = Integer.valueOf(number2.intValue());
            int iIntValue = numValueOf.intValue();
            if (!(1 <= iIntValue && iIntValue < 65536)) {
                numValueOf = null;
            }
            if (numValueOf != null) {
                int iIntValue2 = numValueOf.intValue();
                InetAddress inetAddress2 = socket4.getInetAddress();
                Intrinsics.checkNotNullExpressionValue(inetAddress2, "getInetAddress(...)");
                startAirPlayKeepAlive(inetAddress2, iIntValue2);
                Unit unit = Unit.INSTANCE;
                Unit unit2 = Unit.INSTANCE;
            }
        }
        Map<?, ?> mapPlist3 = INSTANCE.plist(RtspWire.request$default(rtspWire2, "GET", "/info", null, null, null, 28, null).getBody());
        Object obj5 = mapPlist3.get("hidDevices");
        Iterable iterable4 = obj5 instanceof List ? (List) obj5 : null;
        if (iterable4 != null) {
            Iterable iterable5 = iterable4;
            int i6 = 0;
            Iterator it5 = iterable5.iterator();
            while (it5.hasNext()) {
                Object next4 = it5.next();
                Map map3 = next4 instanceof Map ? (Map) next4 : null;
                if (map3 == null) {
                    socket2 = socket4;
                    phonePairing = phonePairing2;
                    iterable = iterable5;
                    i5 = i6;
                    it3 = it5;
                } else {
                    Map map4 = map3;
                    Object obj6 = map4.get("uuid");
                    socket2 = socket4;
                    String str8 = obj6 instanceof String ? (String) obj6 : null;
                    if (str8 == null) {
                        phonePairing = phonePairing2;
                        iterable = iterable5;
                        i5 = i6;
                        it3 = it5;
                    } else {
                        String str9 = str8;
                        Object obj7 = map4.get("hidDescriptor");
                        phonePairing = phonePairing2;
                        byte[] bArr2 = obj7 instanceof byte[] ? (byte[]) obj7 : null;
                        if (bArr2 == null) {
                            iterable = iterable5;
                            i5 = i6;
                            it3 = it5;
                        } else {
                            byte[] bArr3 = bArr2;
                            try {
                                HidInput hidInput = new HidInput(bArr3);
                                if (hidInput.isTouch()) {
                                    try {
                                        this.touchDevices.put(str9, hidInput);
                                        Pair<Integer, Integer> touchExtent = hidInput.getTouchExtent();
                                        SenderEvents senderEvents3 = this.events;
                                        try {
                                            try {
                                                try {
                                                    try {
                                                        if (touchExtent != null) {
                                                            try {
                                                                iterable = iterable5;
                                                                try {
                                                                    i5 = i6;
                                                                    try {
                                                                        str7 = touchExtent.getFirst() + "x" + touchExtent.getSecond();
                                                                        if (str7 == null) {
                                                                        }
                                                                        it3 = it5;
                                                                        senderEvents3.diagnostic("触摸描述符 uuid=" + str9 + " 量程=" + str7 + " 字节=" + bArr3.length + " hex=" + ArraysKt.joinToString$default(bArr3, (CharSequence) " ", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda22
                                                                            @Override // kotlin.jvm.functions.Function1
                                                                            public final Object invoke(Object obj8) {
                                                                                return AirPlaySender.start$lambda$28$lambda$27(((Byte) obj8).byteValue());
                                                                            }
                                                                        }, 30, (Object) null));
                                                                    } catch (Exception e) {
                                                                        e = e;
                                                                        it3 = it5;
                                                                        this.events.diagnostic("输入描述符不支持：" + e.getMessage());
                                                                        socket4 = socket2;
                                                                        phonePairing2 = phonePairing;
                                                                        it5 = it3;
                                                                        iterable5 = iterable;
                                                                        i6 = i5;
                                                                    }
                                                                } catch (Exception e2) {
                                                                    e = e2;
                                                                    i5 = i6;
                                                                    it3 = it5;
                                                                }
                                                            } catch (Exception e3) {
                                                                e = e3;
                                                                iterable = iterable5;
                                                                i5 = i6;
                                                                it3 = it5;
                                                            }
                                                        } else {
                                                            iterable = iterable5;
                                                            i5 = i6;
                                                        }
                                                        senderEvents3.diagnostic("触摸描述符 uuid=" + str9 + " 量程=" + str7 + " 字节=" + bArr3.length + " hex=" + ArraysKt.joinToString$default(bArr3, (CharSequence) " ", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda22
                                                            @Override // kotlin.jvm.functions.Function1
                                                            public final Object invoke(Object obj8) {
                                                                return AirPlaySender.start$lambda$28$lambda$27(((Byte) obj8).byteValue());
                                                            }
                                                        }, 30, (Object) null));
                                                    } catch (Exception e4) {
                                                        e = e4;
                                                        this.events.diagnostic("输入描述符不支持：" + e.getMessage());
                                                    }
                                                } catch (Exception e5) {
                                                    e = e5;
                                                }
                                                it3 = it5;
                                            } catch (Exception e6) {
                                                e = e6;
                                                it3 = it5;
                                            }
                                        } catch (Exception e7) {
                                            e = e7;
                                            it3 = it5;
                                        }
                                        str7 = "未声明";
                                    } catch (Exception e8) {
                                        e = e8;
                                        iterable = iterable5;
                                        i5 = i6;
                                        it3 = it5;
                                    }
                                } else {
                                    iterable = iterable5;
                                    i5 = i6;
                                    it3 = it5;
                                    this.keyDevices.put(str9, new HidKeys(bArr3));
                                    this.events.diagnostic("按键描述符 uuid=" + str9 + " 字节=" + bArr3.length);
                                }
                                this.armedInputs.add(str9);
                            } catch (Exception e9) {
                                e = e9;
                                iterable = iterable5;
                                i5 = i6;
                                it3 = it5;
                            }
                        }
                    }
                }
                socket4 = socket2;
                phonePairing2 = phonePairing;
                it5 = it3;
                iterable5 = iterable;
                i6 = i5;
            }
            socket = socket4;
            Unit unit3 = Unit.INSTANCE;
        } else {
            socket = socket4;
        }
        Object obj8 = mapPlist3.get("displays");
        Iterable iterable6 = obj8 instanceof List ? (List) obj8 : null;
        if (iterable6 != null) {
            Iterable iterable7 = iterable6;
            int i7 = 0;
            ArrayList arrayList2 = new ArrayList();
            for (Object obj9 : iterable7) {
                Iterable iterable8 = iterable7;
                int i8 = i7;
                Map map5 = obj9 instanceof Map ? (Map) obj9 : null;
                if (map5 != null) {
                    arrayList2.add(map5);
                }
                i7 = i8;
                iterable7 = iterable8;
            }
            arrayListEmptyList2 = arrayList2;
        } else {
            arrayListEmptyList2 = null;
        }
        if (arrayListEmptyList2 == null) {
            arrayListEmptyList2 = CollectionsKt.emptyList();
        }
        List list6 = arrayListEmptyList2;
        List list7 = list6;
        int i9 = 0;
        Iterator it6 = list7.iterator();
        while (it6.hasNext()) {
            Map map6 = (Map) it6.next();
            List list8 = list7;
            List list9 = list6;
            this.events.diagnostic("车机显示项 uuid=" + map6.get("uuid") + " type=" + map6.get("type") + " " + map6.get("widthPixels") + "x" + map6.get("heightPixels") + "@" + map6.get("maxFPS") + " viewAreas=" + INSTANCE.describeViewAreas(map6) + " initialViewArea=" + map6.get("initialViewArea"));
            this.events.diagnostic("  物理尺寸 " + map6.get("widthPhysical") + "x" + map6.get("heightPhysical") + "mm features=" + map6.get("features") + " primaryInputDevice=" + map6.get("primaryInputDevice"));
            this.events.diagnostic("  字段 " + CollectionsKt.joinToString$default(map6.keySet(), ",", null, null, 0, null, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda23
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj10) {
                    return String.valueOf(obj10);
                }
            }, 30, null));
            list7 = list8;
            list6 = list9;
            i9 = i9;
            it6 = it6;
            iListen = iListen;
        }
        List list10 = list6;
        Object obj10 = mapPlist3.get("features");
        Number number3 = obj10 instanceof Number ? (Number) obj10 : null;
        int i10 = 16;
        if (number3 != null) {
            SenderEvents senderEvents4 = this.events;
            String string = Long.toString(number3.longValue(), CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            senderEvents4.diagnostic("车机特性位=0x" + string);
            Unit unit4 = Unit.INSTANCE;
            Unit unit5 = Unit.INSTANCE;
        }
        Object obj11 = mapPlist3.get("audioFormats");
        Iterable iterable9 = obj11 instanceof List ? (List) obj11 : null;
        if (iterable9 != null) {
            Iterable iterable10 = iterable9;
            ArrayList arrayList3 = new ArrayList();
            for (Object obj12 : iterable10) {
                int i11 = i10;
                Iterable iterable11 = iterable10;
                Map map7 = obj12 instanceof Map ? (Map) obj12 : null;
                if (map7 != null) {
                    arrayList3.add(map7);
                }
                iterable10 = iterable11;
                i10 = i11;
            }
            i = i10;
            arrayListEmptyList3 = arrayList3;
        } else {
            i = 16;
            arrayListEmptyList3 = null;
        }
        if (arrayListEmptyList3 == null) {
            arrayListEmptyList3 = CollectionsKt.emptyList();
        }
        List list11 = arrayListEmptyList3;
        this.events.diagnostic("车机音频格式项=" + list11.size());
        List list12 = list11;
        int i12 = 0;
        Iterator it7 = list12.iterator();
        while (it7.hasNext()) {
            Map map8 = (Map) it7.next();
            Object obj13 = map8.get("audioOutputFormats");
            Number number4 = obj13 instanceof Number ? (Number) obj13 : null;
            Long lValueOf = number4 != null ? Long.valueOf(number4.longValue()) : null;
            Object obj14 = map8.get("audioInputFormats");
            Number number5 = obj14 instanceof Number ? (Number) obj14 : null;
            Long lValueOf2 = number5 != null ? Long.valueOf(number5.longValue()) : null;
            SenderEvents senderEvents5 = this.events;
            Object obj15 = map8.get("type");
            List list13 = list12;
            Object obj16 = map8.get("audioType");
            if (lValueOf != null) {
                long jLongValue = lValueOf.longValue();
                list5 = list11;
                i4 = i12;
                it2 = it7;
                String string2 = Long.toString(jLongValue, CharsKt.checkRadix(i));
                map2 = mapPlist3;
                Intrinsics.checkNotNullExpressionValue(string2, "toString(...)");
                str5 = "0x" + string2 + "(" + INSTANCE.describeAudioBits(jLongValue) + ")";
                if (str5 == null) {
                }
                if (lValueOf2 != null) {
                    long jLongValue2 = lValueOf2.longValue();
                    String string3 = Long.toString(jLongValue2, CharsKt.checkRadix(i));
                    Intrinsics.checkNotNullExpressionValue(string3, "toString(...)");
                    str6 = "0x" + string3 + "(" + INSTANCE.describeAudioBits(jLongValue2) + ")";
                    if (str6 == null) {
                    }
                    senderEvents5.diagnostic("  音频项 type=" + obj15 + " audioType=" + obj16 + " output=" + str5 + " input=" + str6);
                    this.events.diagnostic("    字段 " + CollectionsKt.joinToString$default(map8.keySet(), ",", null, null, 0, null, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda24
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj17) {
                            return String.valueOf(obj17);
                        }
                    }, 30, null));
                    list12 = list13;
                    list11 = list5;
                    i12 = i4;
                    it7 = it2;
                    mapPlist3 = map2;
                }
                str6 = "无";
                senderEvents5.diagnostic("  音频项 type=" + obj15 + " audioType=" + obj16 + " output=" + str5 + " input=" + str6);
                this.events.diagnostic("    字段 " + CollectionsKt.joinToString$default(map8.keySet(), ",", null, null, 0, null, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda24
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj17) {
                        return String.valueOf(obj17);
                    }
                }, 30, null));
                list12 = list13;
                list11 = list5;
                i12 = i4;
                it7 = it2;
                mapPlist3 = map2;
            } else {
                list5 = list11;
                i4 = i12;
                it2 = it7;
                map2 = mapPlist3;
            }
            str5 = "无";
            if (lValueOf2 != null) {
                long jLongValue3 = lValueOf2.longValue();
                String string4 = Long.toString(jLongValue3, CharsKt.checkRadix(i));
                Intrinsics.checkNotNullExpressionValue(string4, "toString(...)");
                str6 = "0x" + string4 + "(" + INSTANCE.describeAudioBits(jLongValue3) + ")";
                if (str6 == null) {
                }
                senderEvents5.diagnostic("  音频项 type=" + obj15 + " audioType=" + obj16 + " output=" + str5 + " input=" + str6);
                this.events.diagnostic("    字段 " + CollectionsKt.joinToString$default(map8.keySet(), ",", null, null, 0, null, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda24
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj17) {
                        return String.valueOf(obj17);
                    }
                }, 30, null));
                list12 = list13;
                list11 = list5;
                i12 = i4;
                it7 = it2;
                mapPlist3 = map2;
            }
            str6 = "无";
            senderEvents5.diagnostic("  音频项 type=" + obj15 + " audioType=" + obj16 + " output=" + str5 + " input=" + str6);
            this.events.diagnostic("    字段 " + CollectionsKt.joinToString$default(map8.keySet(), ",", null, null, 0, null, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda24
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj17) {
                    return String.valueOf(obj17);
                }
            }, 30, null));
            list12 = list13;
            list11 = list5;
            i12 = i4;
            it7 = it2;
            mapPlist3 = map2;
        }
        Map<?, ?> map9 = mapPlist3;
        ArrayList arrayList4 = new ArrayList();
        for (Object obj17 : list11) {
            if (Intrinsics.areEqual(((Map) obj17).get("audioType"), "speechRecognition")) {
                arrayList4.add(obj17);
            }
        }
        ArrayList arrayList5 = arrayList4;
        ArrayList arrayList6 = new ArrayList();
        Iterator it8 = arrayList5.iterator();
        while (true) {
            bArr = bArrInvoke;
            if (!it8.hasNext()) {
                break;
            }
            Object next5 = it8.next();
            Object obj18 = ((Map) next5).get("audioInputFormats");
            Number number6 = obj18 instanceof Number ? (Number) obj18 : null;
            if (((number6 != null ? number6.longValue() : 0L) & 80) != 0) {
                arrayList6.add(next5);
            }
            bArrInvoke = bArr;
        }
        ArrayList arrayList7 = arrayList6;
        SenderEvents senderEvents6 = this.events;
        if (arrayList5.isEmpty()) {
            str = "车机未声明 speechRecognition 项：麦克风上行不可能，无需再试";
        } else if (arrayList7.isEmpty()) {
            str = "车机声明了 " + arrayList5.size() + " 个 speechRecognition 项，但 audioInputFormats 不含 0x10/0x40：只能下行音频";
        } else {
            str = "车机支持麦克风上行：" + arrayList7.size() + " 个 speechRecognition 项，可接受 " + INSTANCE.describeAudioBits(80L);
        }
        senderEvents6.diagnostic(str);
        this.events.diagnostic("/info 顶层字段 " + CollectionsKt.joinToString$default(map9.keySet(), ",", null, null, 0, null, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda25
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj19) {
                return String.valueOf(obj19);
            }
        }, 30, null));
        List list14 = list10;
        ArrayList arrayList8 = new ArrayList();
        for (Object obj19 : list14) {
            Object obj20 = ((Map) obj19).get("widthPixels");
            List list15 = list14;
            Number number7 = obj20 instanceof Number ? (Number) obj20 : null;
            if (number7 == null) {
                z6 = false;
            } else if ((number7.intValue() > 0 ? 1 : 0) == 1) {
                z6 = true;
            } else {
                z6 = false;
            }
            if (z6) {
                arrayList8.add(obj19);
            }
            list14 = list15;
        }
        ArrayList arrayList9 = arrayList8;
        Iterator it9 = arrayList9.iterator();
        do {
            if (!it9.hasNext()) {
                next = null;
                break;
            }
            next = it9.next();
            Object obj21 = ((Map) next).get("viewAreas");
            list4 = obj21 instanceof List ? (List) obj21 : null;
        } while (!(list4 != null && (list4.isEmpty() ^ true)));
        Map map10 = (Map) next;
        if (map10 == null) {
            Iterator it10 = arrayList9.iterator();
            if (it10.hasNext()) {
                next3 = it10.next();
                if (it10.hasNext()) {
                    Map map11 = (Map) next3;
                    Object obj22 = map11.get("widthPixels");
                    Number number8 = obj22 instanceof Number ? (Number) obj22 : null;
                    long jIntValue = number8 != null ? number8.intValue() : 0;
                    Object obj23 = map11.get("heightPixels");
                    Number number9 = obj23 instanceof Number ? (Number) obj23 : null;
                    long jIntValue2 = jIntValue * ((long) (number9 != null ? number9.intValue() : 0));
                    do {
                        Object next6 = it10.next();
                        Map map12 = (Map) next6;
                        Object obj24 = map12.get("widthPixels");
                        Number number10 = obj24 instanceof Number ? (Number) obj24 : null;
                        long jIntValue3 = number10 != null ? number10.intValue() : 0;
                        Object obj25 = map12.get("heightPixels");
                        Number number11 = obj25 instanceof Number ? (Number) obj25 : null;
                        long jIntValue4 = jIntValue3 * ((long) (number11 != null ? number11.intValue() : 0));
                        if (jIntValue2 < jIntValue4) {
                            next3 = next6;
                            jIntValue2 = jIntValue4;
                        }
                    } while (it10.hasNext());
                }
            } else {
                next3 = null;
            }
            map10 = (Map) next3;
            if (map10 == null) {
                throw new IllegalStateException("车机未提供有效显示参数".toString());
            }
        } else {
            arrayList9 = arrayList9;
            arrayList5 = arrayList5;
        }
        Map map13 = map10;
        if (arrayList9.size() > 1) {
            this.events.diagnostic("车机声明了 " + arrayList9.size() + " 个显示项，选用 " + map13.get("widthPixels") + "x" + map13.get("heightPixels") + "（uuid=" + map13.get("uuid") + "）");
        }
        Map mapSelectedViewArea = INSTANCE.selectedViewArea(map13);
        SenderSafeArea senderSafeAreaSelectedSafeArea = INSTANCE.selectedSafeArea(map13, mapSelectedViewArea);
        int iNumber4 = INSTANCE.number(map13.get("widthPixels"));
        int iNumber5 = INSTANCE.number(map13.get("heightPixels"));
        int iNumber6 = INSTANCE.number(map13.get("maxFPS"));
        if (!(1 <= iNumber6 && iNumber6 < 61)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        Unit unit6 = Unit.INSTANCE;
        Object obj26 = map13.get("uuid");
        SenderDisplay senderDisplay = new SenderDisplay(iNumber4, iNumber5, iNumber6, obj26 instanceof String ? (String) obj26 : null, senderSafeAreaSelectedSafeArea);
        final SenderDisplay senderDisplayOutputDisplay = this.events.outputDisplay(senderDisplay);
        if (senderDisplayOutputDisplay.getWidth() > 0 && senderDisplayOutputDisplay.getHeight() > 0) {
            if (senderDisplayOutputDisplay.getWidth() % 2 == 0 && senderDisplayOutputDisplay.getHeight() % 2 == 0) {
                z = true;
            }
            if (z) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            if (!Intrinsics.areEqual(senderDisplayOutputDisplay, senderDisplay)) {
                this.events.diagnostic("输出画布由车机主显示 " + senderDisplay.getWidth() + "x" + senderDisplay.getHeight() + " 调整为 " + senderDisplayOutputDisplay.getWidth() + "x" + senderDisplayOutputDisplay.getHeight());
            }
            if (mapSelectedViewArea != null) {
                this.events.diagnostic("车机初始 viewArea：" + mapSelectedViewArea.get("widthPixels") + "x" + mapSelectedViewArea.get("heightPixels") + " origin=" + mapSelectedViewArea.get("originXPixels") + "," + mapSelectedViewArea.get("originYPixels"));
                Unit unit7 = Unit.INSTANCE;
                Unit unit8 = Unit.INSTANCE;
            }
            senderEvents = this.events;
            if (senderSafeAreaSelectedSafeArea != null) {
                senderEvents.diagnostic("车机 safeArea：" + senderSafeAreaSelectedSafeArea.getWidth() + "x" + senderSafeAreaSelectedSafeArea.getHeight() + Marker.ANY_NON_NULL_MARKER + senderSafeAreaSelectedSafeArea.getX() + "," + senderSafeAreaSelectedSafeArea.getY() + "；全画布=" + senderDisplayOutputDisplay.getWidth() + "x" + senderDisplayOutputDisplay.getHeight());
                Unit unit9 = Unit.INSTANCE;
                Unit unit10 = Unit.INSTANCE;
            } else {
                senderEvents.diagnostic("车机未提供 safeArea；全画布使用 " + senderDisplayOutputDisplay.getWidth() + "x" + senderDisplayOutputDisplay.getHeight());
            }
            this.format = senderDisplayOutputDisplay;
            iNumber = INSTANCE.number(mapPlist2.get("eventPort"));
            if (1 > iNumber && iNumber < 65536) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            Socket socket5 = (Socket) own(this.connect.invoke(Integer.valueOf(iNumber)));
            i2 = 0;
            socket5.setSoTimeout(0);
            socket5.setTcpNoDelay(true);
            Unit unit11 = Unit.INSTANCE;
            rtspWire = (RtspWire) own(new RtspWire(socket5, (Function1) null, 2, (DefaultConstructorMarker) null));
            rtspWire.setCipher(new ControlCipher(start$key(bArr, "Events-Salt", "Events-Write-Encryption-Key"), start$key(bArr, "Events-Salt", "Events-Read-Encryption-Key")));
            run("carplay-events", new Function0() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda26
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return AirPlaySender.start$lambda$49(this.f$0, rtspWire);
                }
            });
            list = this.armedInputs;
            for (String str10 : list) {
                int i13 = i2;
                Companion companion = INSTANCE;
                List<String> list16 = list;
                Pair[] pairArr = new Pair[2];
                pairArr[i13] = TuplesKt.to("type", "hidSetInputMode");
                int i14 = iNumber;
                Pair[] pairArr2 = new Pair[2];
                pairArr2[i13] = TuplesKt.to("hidInputMode", Integer.valueOf(i13));
                pairArr2[1] = TuplesKt.to("uuid", str10);
                pairArr[1] = TuplesKt.to("params", MapsKt.mapOf(pairArr2));
                rtspWire.sendCommand(companion.encode(MapsKt.mapOf(pairArr)), plistHeaders);
                i2 = i13;
                list = list16;
                iNumber = i14;
            }
            int i15 = i2;
            if (!this.armedInputs.isEmpty()) {
                this.events.diagnostic("hidSetInputMode 已排队 " + this.armedInputs.size() + "：" + CollectionsKt.joinToString$default(this.armedInputs, ",", null, null, 0, null, null, 62, null));
            }
            rtspWire2.setOnRequest(new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda27
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj27) {
                    return AirPlaySender.start$lambda$51(this.f$0, (ControlMessage) obj27);
                }
            });
            rtspWire2.setOnResponse(new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda28
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj27) {
                    return AirPlaySender.start$lambda$52(this.f$0, (ControlMessage) obj27);
                }
            });
            rtspWire2.setOnIgnoredResponse(new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda29
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj27) {
                    return AirPlaySender.start$lambda$53(this.f$0, (ControlMessage) obj27);
                }
            });
            synchronized (this.iapLock) {
                try {
                    this.openingIap = true;
                    Unit unit12 = Unit.INSTANCE;
                } catch (Throwable th) {
                    throw th;
                }
            }
            jNextLong = new SecureRandom().nextLong() & Long.MAX_VALUE;
            Companion companion2 = INSTANCE;
            Companion companion3 = INSTANCE;
            Pair[] pairArr3 = new Pair[4];
            pairArr3[i15] = TuplesKt.to("type", 130);
            pairArr3[1] = TuplesKt.to("clientTypeUUID", "E9459FD0-BCAD-4C45-820F-1E72447EF2F2");
            pairArr3[2] = TuplesKt.to("seed", Long.valueOf(jNextLong));
            pairArr3[3] = TuplesKt.to("streamConnectionID", Long.valueOf(jNextLong));
            mapPlist = companion2.plist(RtspWire.request$default(rtspWire2, "SETUP", "/", companion3.encode(MapsKt.mapOf(TuplesKt.to("streams", CollectionsKt.listOf(MapsKt.mapOf(pairArr3))))), plistHeaders, null, 16, null).getBody());
            synchronized (this.iapLock) {
                try {
                    senderControlTransportControl = SenderNegotiation.INSTANCE.control(mapPlist);
                    if (senderControlTransportControl.getKind() != SenderControlKind.AUTHENTICATED_BLUETOOTH) {
                        try {
                            if (this.earlyIap.isEmpty()) {
                                if (senderControlTransportControl.getKind() == SenderControlKind.AUTHENTICATED_BLUETOOTH) {
                                    z5 = false;
                                    this.openingIap = false;
                                } else {
                                    z5 = false;
                                }
                                senderControlTransport = senderControlTransportControl;
                                r12 = z5;
                            } else {
                                this.events.diagnostic("已收到车机实际发起的 iAP 命令；建立命令控制通道并执行独立验证");
                                senderControlTransport = new SenderControlTransport(SenderControlKind.WIFI_COMMAND, null, 2, null);
                                r12 = 0;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            throw th;
                        }
                    } else {
                        if (senderControlTransportControl.getKind() == SenderControlKind.AUTHENTICATED_BLUETOOTH) {
                            z5 = false;
                            this.openingIap = false;
                        } else {
                            z5 = false;
                        }
                        senderControlTransport = senderControlTransportControl;
                        r12 = z5;
                    }
                    this.events.diagnostic("type 130 协商结果：" + senderControlTransport.getKind() + "，回复字段数=" + mapPlist.size());
                    if (senderControlTransport.getKind() != SenderControlKind.WIFI_DATASTREAM) {
                        r0 = 1;
                    } else {
                        r0 = r12;
                    }
                    this.commandModes = r0;
                    switch (WhenMappings.$EnumSwitchMapping$0[senderControlTransport.getKind().ordinal()]) {
                        case 1:
                            function1 = this.connect;
                            port2 = senderControlTransport.getPort();
                            if (port2 != null) {
                                throw new IllegalStateException("Required value was null.".toString());
                            }
                            Socket socket6 = (Socket) own(function1.invoke(port2));
                            socket6.setTcpNoDelay(true);
                            Unit unit13 = Unit.INSTANCE;
                            phoneIapTunnel = (PhoneIapTunnel) own(new PhoneIapTunnel(socket6, start$key(bArr, "DataStream-Salt" + jNextLong, CarPlayMediaEngine.DATASTREAM_OUTPUT_KEY), null, 4, null));
                            break;
                            break;
                        case 2:
                            phoneIapTunnel = (PhoneIapTunnel) own(new PhoneIapTunnel(null, null, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda30
                                @Override // kotlin.jvm.functions.Function1
                                public final Object invoke(Object obj27) {
                                    return AirPlaySender.start$lambda$59(rtspWire2, this, (byte[]) obj27);
                                }
                            }, 3, null));
                            break;
                        case 3:
                            if (this.events.bluetoothControlAvailable()) {
                                throw new IllegalStateException("车机未接受 type 130，且已认证的蓝牙控制通道不可用".toString());
                            }
                            this.events.diagnostic("车机未接受 type 130；保留实际已认证的蓝牙控制，Wi-Fi 媒体继续使用配对验证后的加密连接");
                            phoneIapTunnel = null;
                            break;
                            break;
                        default:
                            throw new NoWhenBranchMatchedException();
                    }
                    phoneIapTunnel2 = phoneIapTunnel;
                    synchronized (this.iapLock) {
                        try {
                            if (this.closed.get()) {
                                this.iap = phoneIapTunnel2;
                                this.openingIap = r12;
                                while (!this.earlyIap.isEmpty()) {
                                    if (phoneIapTunnel2 != null) {
                                        try {
                                            bArrRemoveFirst = this.earlyIap.removeFirst();
                                            Intrinsics.checkNotNullExpressionValue(bArrRemoveFirst, "removeFirst(...)");
                                            if (phoneIapTunnel2.offer(bArrRemoveFirst)) {
                                                r11 = 1;
                                            } else {
                                                r11 = r12;
                                            }
                                        } catch (Throwable th3) {
                                            th = th3;
                                        }
                                    } else {
                                        r11 = r12;
                                    }
                                    if (r11 != 0) {
                                        throw new IllegalStateException("启动 iAP 数据未能交接".toString());
                                    }
                                }
                                this.earlyIapBytes = r12;
                                Unit unit14 = Unit.INSTANCE;
                                if (phoneIapTunnel2 != null) {
                                    completableFutureIapControl = this.events.iapControl(phoneIapTunnel2);
                                } else {
                                    completableFutureIapControl = null;
                                }
                                completableFuture = completableFutureIapControl;
                                if (completableFuture != null) {
                                    own(new Closeable() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda17
                                        @Override // java.io.Closeable, java.lang.AutoCloseable
                                        public final void close() {
                                            completableFuture.cancel(true);
                                        }
                                    });
                                }
                                if (senderControlTransport.getKind() == SenderControlKind.WIFI_DATASTREAM) {
                                    this.events.diagnostic("Wi-Fi iAP2 控制通道已建立，等待独立识别和认证后再启动媒体");
                                    if (completableFuture != null) {
                                        throw new IllegalStateException("Required value was null.".toString());
                                    }
                                    completableFuture.get(45L, TimeUnit.SECONDS);
                                    if (this.closed.get()) {
                                        throw new IllegalStateException("Wi-Fi iAP2 认证完成前连接已停止".toString());
                                    }
                                }
                                if (enableAudio) {
                                    zHasHighQualityMediaCapability = INSTANCE.hasHighQualityMediaCapability(map9);
                                    listAudioCandidates = SenderNegotiation.INSTANCE.audioCandidates(map9, zHasHighQualityMediaCapability);
                                    function2 = this.audioProfileOrder;
                                    if (function2 != null) {
                                        listEmptyList = function2.invoke(listAudioCandidates);
                                    } else {
                                        listEmptyList = null;
                                    }
                                    if (listEmptyList == null) {
                                        listEmptyList = CollectionsKt.emptyList();
                                    }
                                    list2 = listEmptyList;
                                    if (list2.isEmpty()) {
                                        list2 = listAudioCandidates;
                                    }
                                    list3 = list2;
                                    senderAudioFormat3 = (SenderAudioFormat) CollectionsKt.firstOrNull((List) list3);
                                    if (senderAudioFormat3 != null) {
                                        throw new IllegalStateException("车机未声明可发送的媒体格式（type 100/102、44.1/48 kHz）；请关闭音频重试".toString());
                                    }
                                    this.selectedAudio = senderAudioFormat3;
                                    senderAudioFormat4 = this.selectedAudio;
                                    Intrinsics.checkNotNull(senderAudioFormat4);
                                    SenderEvents senderEvents7 = this.events;
                                    int streamType = senderAudioFormat4.getStreamType();
                                    if (senderAudioFormat4.isPcm()) {
                                        str3 = "LPCM";
                                    } else {
                                        str3 = "AAC-LC";
                                    }
                                    int sampleRate = senderAudioFormat4.getSampleRate();
                                    int channels = senderAudioFormat4.getChannels();
                                    if (zHasHighQualityMediaCapability) {
                                        str4 = "（media AAC 优先）";
                                    } else {
                                        str4 = "";
                                    }
                                    senderEvents7.diagnostic("音频协商：type=" + streamType + "，" + str3 + " " + sampleRate + " Hz / " + channels + " 声道，按车机 audioOutputFormats 选择" + str4 + "；候选=" + CollectionsKt.joinToString$default(list3, "/", null, null, 0, null, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda18
                                        @Override // kotlin.jvm.functions.Function1
                                        public final Object invoke(Object obj27) {
                                            return AirPlaySender.start$lambda$69$lambda$68((SenderAudioFormat) obj27);
                                        }
                                    }, 30, null));
                                    this.events.audioFormat(senderAudioFormat4);
                                }
                                if (enableAudio) {
                                    setAudioOwnership(true);
                                }
                                senderControlTransport2 = senderControlTransport;
                                RtspWire.request$default(rtspWire2, "POST", "/command", INSTANCE.encode(this.modes.snapshot()), plistHeaders, null, 16, null);
                                SenderEvents senderEvents8 = this.events;
                                if (enableAudio) {
                                    str2 = "+音频";
                                } else {
                                    str2 = "";
                                }
                                senderEvents8.diagnostic("初始资源所有权已声明（屏幕" + str2 + "）");
                                this.events.display(senderDisplayOutputDisplay);
                                jNextLong2 = new SecureRandom().nextLong() & Long.MAX_VALUE;
                                linkedHashMapLinkedMapOf = MapsKt.linkedMapOf(TuplesKt.to("type", 110), TuplesKt.to("streamConnectionID", Long.valueOf(jNextLong2)), TuplesKt.to("latencyMs", Integer.valueOf(SCREEN_LATENCY_MS)), TuplesKt.to("displayWidth", Integer.valueOf(senderDisplayOutputDisplay.getWidth())), TuplesKt.to("displayHeight", Integer.valueOf(senderDisplayOutputDisplay.getHeight())));
                                uuid = senderDisplayOutputDisplay.getUuid();
                                if (uuid != null) {
                                    linkedHashMapLinkedMapOf.put("uuid", uuid);
                                    Unit unit15 = Unit.INSTANCE;
                                    Unit unit16 = Unit.INSTANCE;
                                }
                                it = INSTANCE.streams(RtspWire.request$default(rtspWire2, "SETUP", "/", INSTANCE.encode(MapsKt.mapOf(TuplesKt.to("streams", CollectionsKt.listOf(linkedHashMapLinkedMapOf)))), plistHeaders, null, 16, null).getBody()).iterator();
                                while (true) {
                                    if (it.hasNext()) {
                                        next2 = it.next();
                                        linkedHashMap = linkedHashMapLinkedMapOf;
                                        if (INSTANCE.number(((Map) next2).get("type")) == 110) {
                                            i3 = 1;
                                        } else {
                                            i3 = 0;
                                        }
                                        if (i3 != 0) {
                                            obj = next2;
                                        } else {
                                            linkedHashMapLinkedMapOf = linkedHashMap;
                                        }
                                    } else {
                                        obj = null;
                                    }
                                }
                                map = (Map) obj;
                                if (map != null) {
                                    throw new IllegalStateException("车机拒绝视频流".toString());
                                }
                                iNumber2 = INSTANCE.number(map.get("dataPort"));
                                if (1 <= iNumber2 || iNumber2 >= 65536) {
                                    z2 = false;
                                } else {
                                    z2 = true;
                                }
                                if (z2) {
                                    throw new IllegalArgumentException("Failed requirement.".toString());
                                }
                                this.events.diagnostic("屏幕 SETUP 已接受：type=110，画布=" + senderDisplayOutputDisplay.getWidth() + "x" + senderDisplayOutputDisplay.getHeight() + "，dataPort=" + iNumber2);
                                this.videoKey = start$key(bArr, "DataStream-Salt" + jNextLong2, CarPlayMediaEngine.DATASTREAM_OUTPUT_KEY);
                                this.video = (Socket) own(this.connect.invoke(Integer.valueOf(iNumber2)));
                                this.events.diagnostic("视频 dataPort 已连接：" + iNumber2);
                                this.videoFramesSinceKeepAlive = 0;
                                this.videoBytesSinceKeepAlive = 0L;
                                this.videoKeepAliveCount = 0L;
                                this.videoStreamReady = false;
                                RtspWire.request$default(rtspWire2, "RECORD", "/", null, null, null, 28, null);
                                this.events.diagnostic("屏幕 RECORD 已完成，等待 startDisplay 启动来源编码器");
                                if (Intrinsics.areEqual(map9.get("keepAliveSendStatsAsBody"), (Object) true)) {
                                    z3 = true;
                                } else {
                                    obj2 = map9.get("keepAliveSendStatsAsBody");
                                    if (obj2 instanceof Number) {
                                        number = (Number) obj2;
                                    } else {
                                        number = null;
                                    }
                                    if (number == null && number.intValue() == 1) {
                                        z4 = true;
                                    } else {
                                        z4 = false;
                                    }
                                    if (z4) {
                                        z3 = true;
                                    } else {
                                        z3 = false;
                                    }
                                }
                                startVideoKeepAlive(z3);
                                this.events.startDisplay(senderDisplayOutputDisplay);
                                if (enableAudio) {
                                    senderAudioFormat = this.selectedAudio;
                                    if (senderAudioFormat != null) {
                                        throw new IllegalStateException("Required value was null.".toString());
                                    }
                                    InetAddress inetAddress3 = socket.getInetAddress();
                                    Intrinsics.checkNotNullExpressionValue(inetAddress3, "getInetAddress(...)");
                                    setupAudio(rtspWire2, senderAudioFormat, bArr, inetAddress3);
                                    this.audioProfileSetupComplete = true;
                                    this.audioProfileStable = false;
                                    senderAudioFormat2 = this.selectedAudio;
                                    if (senderAudioFormat2 != null) {
                                        throw new IllegalStateException("Required value was null.".toString());
                                    }
                                    run("carplay-audio-profile-stable", new Function0() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda19
                                        @Override // kotlin.jvm.functions.Function0
                                        public final Object invoke() {
                                            return AirPlaySender.start$lambda$72(this.f$0, senderAudioFormat2);
                                        }
                                    });
                                    RtspWire.request$default(rtspWire2, "RECORD", "/", null, null, null, 28, null);
                                    startAudioWorker();
                                }
                                run("carplay-video-send", new Function0() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda20
                                    @Override // kotlin.jvm.functions.Function0
                                    public final Object invoke() {
                                        return AirPlaySender.start$lambda$75(this.f$0, senderDisplayOutputDisplay);
                                    }
                                });
                                run("carplay-feedback", new Function0() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda21
                                    @Override // kotlin.jvm.functions.Function0
                                    public final Object invoke() {
                                        return AirPlaySender.start$lambda$77(this.f$0, senderControlTransport2, rtspWire2);
                                    }
                                });
                                if (senderControlTransport2.getKind() == SenderControlKind.WIFI_COMMAND) {
                                    this.events.diagnostic("Wi-Fi 媒体已 RECORD，等待 Wi-Fi iAP2 独立识别和认证完成");
                                    if (completableFuture != null) {
                                        throw new IllegalStateException("Required value was null.".toString());
                                    }
                                    completableFuture.get(45L, TimeUnit.SECONDS);
                                    if (this.closed.get()) {
                                        throw new IllegalStateException("Wi-Fi iAP2 认证完成前连接已停止".toString());
                                    }
                                }
                                return senderDisplayOutputDisplay;
                            }
                            try {
                                throw new IllegalStateException("iAP 通道建立前会话已关闭".toString());
                            } catch (Throwable th4) {
                                th = th4;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                        }
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                }
            }
        }
        z = false;
        if (z) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (!Intrinsics.areEqual(senderDisplayOutputDisplay, senderDisplay)) {
            this.events.diagnostic("输出画布由车机主显示 " + senderDisplay.getWidth() + "x" + senderDisplay.getHeight() + " 调整为 " + senderDisplayOutputDisplay.getWidth() + "x" + senderDisplayOutputDisplay.getHeight());
        }
        if (mapSelectedViewArea != null) {
            this.events.diagnostic("车机初始 viewArea：" + mapSelectedViewArea.get("widthPixels") + "x" + mapSelectedViewArea.get("heightPixels") + " origin=" + mapSelectedViewArea.get("originXPixels") + "," + mapSelectedViewArea.get("originYPixels"));
            Unit unit17 = Unit.INSTANCE;
            Unit unit18 = Unit.INSTANCE;
        }
        senderEvents = this.events;
        if (senderSafeAreaSelectedSafeArea != null) {
            senderEvents.diagnostic("车机 safeArea：" + senderSafeAreaSelectedSafeArea.getWidth() + "x" + senderSafeAreaSelectedSafeArea.getHeight() + Marker.ANY_NON_NULL_MARKER + senderSafeAreaSelectedSafeArea.getX() + "," + senderSafeAreaSelectedSafeArea.getY() + "；全画布=" + senderDisplayOutputDisplay.getWidth() + "x" + senderDisplayOutputDisplay.getHeight());
            Unit unit19 = Unit.INSTANCE;
            Unit unit110 = Unit.INSTANCE;
        } else {
            senderEvents.diagnostic("车机未提供 safeArea；全画布使用 " + senderDisplayOutputDisplay.getWidth() + "x" + senderDisplayOutputDisplay.getHeight());
        }
        this.format = senderDisplayOutputDisplay;
        iNumber = INSTANCE.number(mapPlist2.get("eventPort"));
        if (1 > iNumber && iNumber < 65536) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        Socket socket7 = (Socket) own(this.connect.invoke(Integer.valueOf(iNumber)));
        i2 = 0;
        socket7.setSoTimeout(0);
        socket7.setTcpNoDelay(true);
        Unit unit111 = Unit.INSTANCE;
        rtspWire = (RtspWire) own(new RtspWire(socket7, (Function1) null, 2, (DefaultConstructorMarker) null));
        rtspWire.setCipher(new ControlCipher(start$key(bArr, "Events-Salt", "Events-Write-Encryption-Key"), start$key(bArr, "Events-Salt", "Events-Read-Encryption-Key")));
        run("carplay-events", new Function0() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda26
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AirPlaySender.start$lambda$49(this.f$0, rtspWire);
            }
        });
        list = this.armedInputs;
        while (r12.hasNext()) {
            int i16 = i2;
            Companion companion4 = INSTANCE;
            List<String> list17 = list;
            Pair[] pairArr4 = new Pair[2];
            pairArr4[i16] = TuplesKt.to("type", "hidSetInputMode");
            int i17 = iNumber;
            Pair[] pairArr5 = new Pair[2];
            pairArr5[i16] = TuplesKt.to("hidInputMode", Integer.valueOf(i16));
            pairArr5[1] = TuplesKt.to("uuid", str10);
            pairArr4[1] = TuplesKt.to("params", MapsKt.mapOf(pairArr5));
            rtspWire.sendCommand(companion4.encode(MapsKt.mapOf(pairArr4)), plistHeaders);
            i2 = i16;
            list = list17;
            iNumber = i17;
        }
        int i18 = i2;
        if (!this.armedInputs.isEmpty()) {
            this.events.diagnostic("hidSetInputMode 已排队 " + this.armedInputs.size() + "：" + CollectionsKt.joinToString$default(this.armedInputs, ",", null, null, 0, null, null, 62, null));
        }
        rtspWire2.setOnRequest(new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda27
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj27) {
                return AirPlaySender.start$lambda$51(this.f$0, (ControlMessage) obj27);
            }
        });
        rtspWire2.setOnResponse(new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda28
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj27) {
                return AirPlaySender.start$lambda$52(this.f$0, (ControlMessage) obj27);
            }
        });
        rtspWire2.setOnIgnoredResponse(new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda29
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj27) {
                return AirPlaySender.start$lambda$53(this.f$0, (ControlMessage) obj27);
            }
        });
        synchronized (this.iapLock) {
            this.openingIap = true;
            Unit unit112 = Unit.INSTANCE;
            jNextLong = new SecureRandom().nextLong() & Long.MAX_VALUE;
            Companion companion5 = INSTANCE;
            Companion companion6 = INSTANCE;
            Pair[] pairArr6 = new Pair[4];
            pairArr6[i18] = TuplesKt.to("type", 130);
            pairArr6[1] = TuplesKt.to("clientTypeUUID", "E9459FD0-BCAD-4C45-820F-1E72447EF2F2");
            pairArr6[2] = TuplesKt.to("seed", Long.valueOf(jNextLong));
            pairArr6[3] = TuplesKt.to("streamConnectionID", Long.valueOf(jNextLong));
            mapPlist = companion5.plist(RtspWire.request$default(rtspWire2, "SETUP", "/", companion6.encode(MapsKt.mapOf(TuplesKt.to("streams", CollectionsKt.listOf(MapsKt.mapOf(pairArr6))))), plistHeaders, null, 16, null).getBody());
            synchronized (this.iapLock) {
                senderControlTransportControl = SenderNegotiation.INSTANCE.control(mapPlist);
                if (senderControlTransportControl.getKind() != SenderControlKind.AUTHENTICATED_BLUETOOTH) {
                    if (senderControlTransportControl.getKind() == SenderControlKind.AUTHENTICATED_BLUETOOTH) {
                        z5 = false;
                        this.openingIap = false;
                    } else {
                        z5 = false;
                    }
                    senderControlTransport = senderControlTransportControl;
                    r12 = z5;
                } else if (this.earlyIap.isEmpty()) {
                    this.events.diagnostic("已收到车机实际发起的 iAP 命令；建立命令控制通道并执行独立验证");
                    senderControlTransport = new SenderControlTransport(SenderControlKind.WIFI_COMMAND, null, 2, null);
                    r12 = 0;
                } else {
                    if (senderControlTransportControl.getKind() == SenderControlKind.AUTHENTICATED_BLUETOOTH) {
                        z5 = false;
                        this.openingIap = false;
                    } else {
                        z5 = false;
                    }
                    senderControlTransport = senderControlTransportControl;
                    r12 = z5;
                }
                this.events.diagnostic("type 130 协商结果：" + senderControlTransport.getKind() + "，回复字段数=" + mapPlist.size());
                if (senderControlTransport.getKind() != SenderControlKind.WIFI_DATASTREAM) {
                    r0 = 1;
                } else {
                    r0 = r12;
                }
                this.commandModes = r0;
                switch (WhenMappings.$EnumSwitchMapping$0[senderControlTransport.getKind().ordinal()]) {
                    case 1:
                        function1 = this.connect;
                        port2 = senderControlTransport.getPort();
                        if (port2 != null) {
                            throw new IllegalStateException("Required value was null.".toString());
                        }
                        Socket socket8 = (Socket) own(function1.invoke(port2));
                        socket8.setTcpNoDelay(true);
                        Unit unit113 = Unit.INSTANCE;
                        phoneIapTunnel = (PhoneIapTunnel) own(new PhoneIapTunnel(socket8, start$key(bArr, "DataStream-Salt" + jNextLong, CarPlayMediaEngine.DATASTREAM_OUTPUT_KEY), null, 4, null));
                        break;
                        break;
                    case 2:
                        phoneIapTunnel = (PhoneIapTunnel) own(new PhoneIapTunnel(null, null, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda30
                            @Override // kotlin.jvm.functions.Function1
                            public final Object invoke(Object obj27) {
                                return AirPlaySender.start$lambda$59(rtspWire2, this, (byte[]) obj27);
                            }
                        }, 3, null));
                        break;
                    case 3:
                        if (this.events.bluetoothControlAvailable()) {
                            throw new IllegalStateException("车机未接受 type 130，且已认证的蓝牙控制通道不可用".toString());
                        }
                        this.events.diagnostic("车机未接受 type 130；保留实际已认证的蓝牙控制，Wi-Fi 媒体继续使用配对验证后的加密连接");
                        phoneIapTunnel = null;
                        break;
                        break;
                    default:
                        throw new NoWhenBranchMatchedException();
                }
                phoneIapTunnel2 = phoneIapTunnel;
                synchronized (this.iapLock) {
                    if (this.closed.get()) {
                        throw new IllegalStateException("iAP 通道建立前会话已关闭".toString());
                    }
                    this.iap = phoneIapTunnel2;
                    this.openingIap = r12;
                    while (!this.earlyIap.isEmpty()) {
                        if (phoneIapTunnel2 != null) {
                            bArrRemoveFirst = this.earlyIap.removeFirst();
                            Intrinsics.checkNotNullExpressionValue(bArrRemoveFirst, "removeFirst(...)");
                            if (phoneIapTunnel2.offer(bArrRemoveFirst)) {
                                r11 = 1;
                            } else {
                                r11 = r12;
                            }
                        } else {
                            r11 = r12;
                        }
                        if (r11 != 0) {
                            throw new IllegalStateException("启动 iAP 数据未能交接".toString());
                        }
                    }
                    this.earlyIapBytes = r12;
                    Unit unit114 = Unit.INSTANCE;
                    if (phoneIapTunnel2 != null) {
                        completableFutureIapControl = this.events.iapControl(phoneIapTunnel2);
                    } else {
                        completableFutureIapControl = null;
                    }
                    completableFuture = completableFutureIapControl;
                    if (completableFuture != null) {
                        own(new Closeable() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda17
                            @Override // java.io.Closeable, java.lang.AutoCloseable
                            public final void close() {
                                completableFuture.cancel(true);
                            }
                        });
                    }
                    if (senderControlTransport.getKind() == SenderControlKind.WIFI_DATASTREAM) {
                        this.events.diagnostic("Wi-Fi iAP2 控制通道已建立，等待独立识别和认证后再启动媒体");
                        if (completableFuture != null) {
                            throw new IllegalStateException("Required value was null.".toString());
                        }
                        completableFuture.get(45L, TimeUnit.SECONDS);
                        if (this.closed.get()) {
                            throw new IllegalStateException("Wi-Fi iAP2 认证完成前连接已停止".toString());
                        }
                    }
                    if (enableAudio) {
                        zHasHighQualityMediaCapability = INSTANCE.hasHighQualityMediaCapability(map9);
                        listAudioCandidates = SenderNegotiation.INSTANCE.audioCandidates(map9, zHasHighQualityMediaCapability);
                        function2 = this.audioProfileOrder;
                        if (function2 != null) {
                            listEmptyList = function2.invoke(listAudioCandidates);
                        } else {
                            listEmptyList = null;
                        }
                        if (listEmptyList == null) {
                            listEmptyList = CollectionsKt.emptyList();
                        }
                        list2 = listEmptyList;
                        if (list2.isEmpty()) {
                            list2 = listAudioCandidates;
                        }
                        list3 = list2;
                        senderAudioFormat3 = (SenderAudioFormat) CollectionsKt.firstOrNull((List) list3);
                        if (senderAudioFormat3 != null) {
                            throw new IllegalStateException("车机未声明可发送的媒体格式（type 100/102、44.1/48 kHz）；请关闭音频重试".toString());
                        }
                        this.selectedAudio = senderAudioFormat3;
                        senderAudioFormat4 = this.selectedAudio;
                        Intrinsics.checkNotNull(senderAudioFormat4);
                        SenderEvents senderEvents9 = this.events;
                        int streamType2 = senderAudioFormat4.getStreamType();
                        if (senderAudioFormat4.isPcm()) {
                            str3 = "LPCM";
                        } else {
                            str3 = "AAC-LC";
                        }
                        int sampleRate2 = senderAudioFormat4.getSampleRate();
                        int channels2 = senderAudioFormat4.getChannels();
                        if (zHasHighQualityMediaCapability) {
                            str4 = "（media AAC 优先）";
                        } else {
                            str4 = "";
                        }
                        senderEvents9.diagnostic("音频协商：type=" + streamType2 + "，" + str3 + " " + sampleRate2 + " Hz / " + channels2 + " 声道，按车机 audioOutputFormats 选择" + str4 + "；候选=" + CollectionsKt.joinToString$default(list3, "/", null, null, 0, null, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda18
                            @Override // kotlin.jvm.functions.Function1
                            public final Object invoke(Object obj27) {
                                return AirPlaySender.start$lambda$69$lambda$68((SenderAudioFormat) obj27);
                            }
                        }, 30, null));
                        this.events.audioFormat(senderAudioFormat4);
                    }
                    if (enableAudio) {
                        setAudioOwnership(true);
                    }
                    senderControlTransport2 = senderControlTransport;
                    RtspWire.request$default(rtspWire2, "POST", "/command", INSTANCE.encode(this.modes.snapshot()), plistHeaders, null, 16, null);
                    SenderEvents senderEvents10 = this.events;
                    if (enableAudio) {
                        str2 = "+音频";
                    } else {
                        str2 = "";
                    }
                    senderEvents10.diagnostic("初始资源所有权已声明（屏幕" + str2 + "）");
                    this.events.display(senderDisplayOutputDisplay);
                    jNextLong2 = new SecureRandom().nextLong() & Long.MAX_VALUE;
                    linkedHashMapLinkedMapOf = MapsKt.linkedMapOf(TuplesKt.to("type", 110), TuplesKt.to("streamConnectionID", Long.valueOf(jNextLong2)), TuplesKt.to("latencyMs", Integer.valueOf(SCREEN_LATENCY_MS)), TuplesKt.to("displayWidth", Integer.valueOf(senderDisplayOutputDisplay.getWidth())), TuplesKt.to("displayHeight", Integer.valueOf(senderDisplayOutputDisplay.getHeight())));
                    uuid = senderDisplayOutputDisplay.getUuid();
                    if (uuid != null) {
                        linkedHashMapLinkedMapOf.put("uuid", uuid);
                        Unit unit115 = Unit.INSTANCE;
                        Unit unit116 = Unit.INSTANCE;
                    }
                    it = INSTANCE.streams(RtspWire.request$default(rtspWire2, "SETUP", "/", INSTANCE.encode(MapsKt.mapOf(TuplesKt.to("streams", CollectionsKt.listOf(linkedHashMapLinkedMapOf)))), plistHeaders, null, 16, null).getBody()).iterator();
                    while (true) {
                        if (it.hasNext()) {
                            next2 = it.next();
                            linkedHashMap = linkedHashMapLinkedMapOf;
                            if (INSTANCE.number(((Map) next2).get("type")) == 110) {
                                i3 = 1;
                            } else {
                                i3 = 0;
                            }
                            if (i3 != 0) {
                                obj = next2;
                            } else {
                                linkedHashMapLinkedMapOf = linkedHashMap;
                            }
                        } else {
                            obj = null;
                        }
                    }
                    map = (Map) obj;
                    if (map != null) {
                        throw new IllegalStateException("车机拒绝视频流".toString());
                    }
                    iNumber2 = INSTANCE.number(map.get("dataPort"));
                    if (1 <= iNumber2) {
                        z2 = false;
                    } else {
                        z2 = false;
                    }
                    if (z2) {
                        throw new IllegalArgumentException("Failed requirement.".toString());
                    }
                    this.events.diagnostic("屏幕 SETUP 已接受：type=110，画布=" + senderDisplayOutputDisplay.getWidth() + "x" + senderDisplayOutputDisplay.getHeight() + "，dataPort=" + iNumber2);
                    this.videoKey = start$key(bArr, "DataStream-Salt" + jNextLong2, CarPlayMediaEngine.DATASTREAM_OUTPUT_KEY);
                    this.video = (Socket) own(this.connect.invoke(Integer.valueOf(iNumber2)));
                    this.events.diagnostic("视频 dataPort 已连接：" + iNumber2);
                    this.videoFramesSinceKeepAlive = 0;
                    this.videoBytesSinceKeepAlive = 0L;
                    this.videoKeepAliveCount = 0L;
                    this.videoStreamReady = false;
                    RtspWire.request$default(rtspWire2, "RECORD", "/", null, null, null, 28, null);
                    this.events.diagnostic("屏幕 RECORD 已完成，等待 startDisplay 启动来源编码器");
                    if (Intrinsics.areEqual(map9.get("keepAliveSendStatsAsBody"), (Object) true)) {
                        obj2 = map9.get("keepAliveSendStatsAsBody");
                        if (obj2 instanceof Number) {
                            number = (Number) obj2;
                        } else {
                            number = null;
                        }
                        if (number == null) {
                            z4 = false;
                        } else {
                            z4 = false;
                        }
                        if (z4) {
                            z3 = true;
                        } else {
                            z3 = false;
                        }
                    } else {
                        z3 = true;
                    }
                    startVideoKeepAlive(z3);
                    this.events.startDisplay(senderDisplayOutputDisplay);
                    if (enableAudio) {
                        senderAudioFormat = this.selectedAudio;
                        if (senderAudioFormat != null) {
                            throw new IllegalStateException("Required value was null.".toString());
                        }
                        InetAddress inetAddress4 = socket.getInetAddress();
                        Intrinsics.checkNotNullExpressionValue(inetAddress4, "getInetAddress(...)");
                        setupAudio(rtspWire2, senderAudioFormat, bArr, inetAddress4);
                        this.audioProfileSetupComplete = true;
                        this.audioProfileStable = false;
                        senderAudioFormat2 = this.selectedAudio;
                        if (senderAudioFormat2 != null) {
                            throw new IllegalStateException("Required value was null.".toString());
                        }
                        run("carplay-audio-profile-stable", new Function0() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda19
                            @Override // kotlin.jvm.functions.Function0
                            public final Object invoke() {
                                return AirPlaySender.start$lambda$72(this.f$0, senderAudioFormat2);
                            }
                        });
                        RtspWire.request$default(rtspWire2, "RECORD", "/", null, null, null, 28, null);
                        startAudioWorker();
                    }
                    run("carplay-video-send", new Function0() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda20
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return AirPlaySender.start$lambda$75(this.f$0, senderDisplayOutputDisplay);
                        }
                    });
                    run("carplay-feedback", new Function0() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda21
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return AirPlaySender.start$lambda$77(this.f$0, senderControlTransport2, rtspWire2);
                        }
                    });
                    if (senderControlTransport2.getKind() == SenderControlKind.WIFI_COMMAND) {
                        this.events.diagnostic("Wi-Fi 媒体已 RECORD，等待 Wi-Fi iAP2 独立识别和认证完成");
                        if (completableFuture != null) {
                            throw new IllegalStateException("Required value was null.".toString());
                        }
                        completableFuture.get(45L, TimeUnit.SECONDS);
                        if (this.closed.get()) {
                            throw new IllegalStateException("Wi-Fi iAP2 认证完成前连接已停止".toString());
                        }
                    }
                    return senderDisplayOutputDisplay;
                    throw th;
                }
            }
        }
    }

    static final byte[] start$lambda$21(RtspWire $wire, String path, byte[] body) {
        Intrinsics.checkNotNullParameter(path, "path");
        Intrinsics.checkNotNullParameter(body, "body");
        return RtspWire.request$default($wire, "POST", path, body, pairingHeaders, null, 16, null).getBody();
    }

    private static final byte[] start$key(byte[] shared, String salt, String label) {
        AirPlayCrypto airPlayCrypto = AirPlayCrypto.INSTANCE;
        byte[] bytes = salt.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        byte[] bytes2 = label.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes2, "getBytes(...)");
        return AirPlayCrypto.hkdfSha512$default(airPlayCrypto, shared, bytes, bytes2, 0, 8, null);
    }

    static final CharSequence start$lambda$28$lambda$27(byte it) {
        String str = String.format("%02X", Arrays.copyOf(new Object[]{Byte.valueOf(it)}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    static final Unit start$lambda$49(AirPlaySender this$0, RtspWire $eventWire) throws Throwable {
        while (!this$0.closed.get()) {
            ControlMessage command = $eventWire.next();
            if (command.getStatus() == null) {
                ControlReply reply = this$0.handleCommand(command.getBody());
                $eventWire.respond(command, reply.getBody(), reply.getStatus());
            } else {
                IntRange intRange = new IntRange(200, 299);
                Integer status = command.getStatus();
                if (status != null && intRange.contains(status.intValue())) {
                    this$0.receiveIapResponse(command, true);
                }
            }
        }
        return Unit.INSTANCE;
    }

    static final ControlReply start$lambda$51(AirPlaySender this$0, ControlMessage it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return this$0.handleCommand(it.getBody());
    }

    static final Unit start$lambda$52(AirPlaySender this$0, ControlMessage it) throws ProtocolException {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.receiveIapResponse(it, true);
        return Unit.INSTANCE;
    }

    static final Unit start$lambda$53(AirPlaySender this$0, ControlMessage it) throws ProtocolException {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.receiveIapResponse(it, true);
        return Unit.INSTANCE;
    }

    static final Unit start$lambda$59(RtspWire $wire, final AirPlaySender this$0, byte[] data) {
        Intrinsics.checkNotNullParameter(data, "data");
        $wire.request("POST", "/command", INSTANCE.encode(MapsKt.mapOf(TuplesKt.to("type", "iAPSendMessage"), TuplesKt.to("params", MapsKt.mapOf(TuplesKt.to("data", data))))), plistHeaders, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda33
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return AirPlaySender.start$lambda$59$lambda$58(this.f$0, (ControlMessage) obj);
            }
        });
        return Unit.INSTANCE;
    }

    static final Unit start$lambda$59$lambda$58(AirPlaySender this$0, ControlMessage it) throws ProtocolException {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.receiveIapResponse(it, false);
        return Unit.INSTANCE;
    }

    static final CharSequence start$lambda$69$lambda$68(SenderAudioFormat candidate) {
        Intrinsics.checkNotNullParameter(candidate, "candidate");
        return candidate.getKey();
    }

    static final Unit start$lambda$72(AirPlaySender this$0, SenderAudioFormat $stableProfile) throws InterruptedException {
        Thread.sleep(AUDIO_PROFILE_STABLE_WINDOW_MS);
        if (!this$0.closed.get()) {
            this$0.audioProfileStable = true;
            Function1<SenderAudioFormat, Unit> function1 = this$0.audioProfileWorking;
            if (function1 != null) {
                function1.invoke($stableProfile);
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit start$lambda$75(final AirPlaySender this$0, final SenderDisplay $selected) {
        final EncodedVideo frame;
        final Ref.LongRef counter = new Ref.LongRef();
        final Ref.ObjectRef sentConfig = new Ref.ObjectRef();
        final Ref.ObjectRef basePts = new Ref.ObjectRef();
        final Ref.LongRef baseNtp = new Ref.LongRef();
        while (!this$0.closed.get() && (frame = this$0.queue.take()) != null) {
            this$0 = this$0;
            $selected = $selected;
            Integer sentBytes = (Integer) this$0.queue.withCurrent(frame, new Function0() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda15
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return AirPlaySender.start$lambda$75$lambda$74(this.f$0, $selected, sentConfig, basePts, frame, baseNtp, counter);
                }
            });
            if (sentBytes == null) {
                this$0.events.diagnostic("丢弃屏幕切换期间取出的旧视频帧：epoch=" + frame.getEpoch());
            } else {
                this$0.events.sent(sentBytes.intValue(), System.nanoTime() - frame.getQueuedAtNanos());
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v20, types: [T, java.lang.Long] */
    /* JADX WARN: Type inference failed for: r7v26, types: [T, byte[], java.lang.Object] */
    /* JADX WARN: Type inference failed for: r7v7, types: [T, byte[], java.lang.Object] */
    static final Integer start$lambda$75$lambda$74(AirPlaySender this$0, SenderDisplay $selected, Ref.ObjectRef $sentConfig, Ref.ObjectRef $basePts, EncodedVideo $frame, Ref.LongRef $baseNtp, Ref.LongRef $counter) throws Throwable {
        byte[] config;
        Object obj;
        Integer num;
        Object obj2;
        OutputStream output;
        int i;
        int i2;
        if (!this$0.modes.ownsScreen() || (config = this$0.avcC) == null) {
            return null;
        }
        Object obj3 = this$0.videoWriteLock;
        synchronized (obj3) {
            try {
                Socket socket = this$0.video;
                try {
                    if (socket == null) {
                        throw new IllegalStateException("Required value was null.".toString());
                    }
                    OutputStream output2 = socket.getOutputStream();
                    if (this$0.configPending) {
                        this$0.resendConfig = false;
                        try {
                            try {
                                obj2 = obj3;
                                i = 1;
                                num = null;
                                output = output2;
                                output.write(VideoPackets.config$default(VideoPackets.INSTANCE, config, $selected.getWidth(), $selected.getHeight(), 0.0f, 0.0f, 0, 0, DNSConstants.KNOWN_ANSWER_TTL, null));
                                ?? CopyOf = Arrays.copyOf(config, config.length);
                                Intrinsics.checkNotNullExpressionValue(CopyOf, "copyOf(...)");
                                $sentConfig.element = CopyOf;
                                this$0.configPending = false;
                                this$0.videoStreamReady = true;
                                this$0.events.diagnostic("视频 CONFIG 已写出：generation=" + this$0.configGeneration + "，大小=" + config.length + "B，恢复轮次 #" + this$0.videoRecoveryRound);
                                i2 = 0;
                            } catch (Throwable th) {
                                th = th;
                                obj = obj3;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            obj = obj3;
                        }
                    } else {
                        num = null;
                        obj2 = obj3;
                        output = output2;
                        i = 1;
                        byte[] bArr = (byte[]) $sentConfig.element;
                        if (!(bArr != null && Arrays.equals(bArr, config)) || this$0.resendConfig) {
                            this$0.resendConfig = false;
                            i2 = 0;
                            output.write(VideoPackets.config$default(VideoPackets.INSTANCE, config, $selected.getWidth(), $selected.getHeight(), 0.0f, 0.0f, 0, 0, DNSConstants.KNOWN_ANSWER_TTL, null));
                            ?? CopyOf2 = Arrays.copyOf(config, config.length);
                            Intrinsics.checkNotNullExpressionValue(CopyOf2, "copyOf(...)");
                            $sentConfig.element = CopyOf2;
                            this$0.videoStreamReady = true;
                            this$0.events.diagnostic("视频 CONFIG 已重写：generation=" + this$0.configGeneration + "，大小=" + config.length + "B，恢复轮次 #" + this$0.videoRecoveryRound);
                        } else {
                            i2 = 0;
                        }
                    }
                    if (!this$0.modes.ownsScreen()) {
                        return num;
                    }
                    if ($basePts.element == 0) {
                        $basePts.element = Long.valueOf($frame.getPtsUs());
                        $baseNtp.element = this$0.clock.syncedNtp().longValue();
                    }
                    long ptsUs = $frame.getPtsUs();
                    T t = $basePts.element;
                    Intrinsics.checkNotNull(t);
                    long elapsed = ptsUs - ((Number) t).longValue();
                    if ((elapsed >= 0 ? i : i2) == 0) {
                        throw new IllegalArgumentException("Failed requirement.".toString());
                    }
                    long ntp = $baseNtp.element + ((elapsed / 1000000) << 32) + (((elapsed % 1000000) * 4294967296L) / 1000000);
                    VideoPackets videoPackets = VideoPackets.INSTANCE;
                    byte[] bytes = $frame.getBytes();
                    byte[] bArr2 = this$0.videoKey;
                    if (bArr2 == null) {
                        throw new IllegalStateException("Required value was null.".toString());
                    }
                    long j = $counter.element;
                    $counter.element = 1 + j;
                    byte[] packet = videoPackets.frame(bytes, bArr2, j, ntp);
                    output.write(packet);
                    output.flush();
                    if ($frame.getKeyFrame()) {
                        this$0.events.diagnostic("视频首个/恢复 IDR 已写出：恢复轮次 #" + this$0.videoRecoveryRound + "，epoch=" + $frame.getEpoch() + "，payload=" + $frame.getBytes().length + "B");
                    }
                    this$0.videoStreamReady = true;
                    this$0.videoFramesSinceKeepAlive++;
                    this$0.videoBytesSinceKeepAlive += (long) $frame.getBytes().length;
                    Integer numValueOf = Integer.valueOf(packet.length);
                    return numValueOf;
                } catch (Throwable th3) {
                    th = th3;
                }
            } catch (Throwable th4) {
                th = th4;
                obj = obj3;
            }
            throw th;
        }
    }

    static final Unit start$lambda$77(final AirPlaySender this$0, SenderControlTransport $transport, RtspWire $wire) throws Exception {
        while (!this$0.closed.get()) {
            try {
                if ($transport.getKind() == SenderControlKind.WIFI_COMMAND) {
                    RtspWire.request$default($wire, "POST", "/feedback", null, null, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda5
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return AirPlaySender.start$lambda$77$lambda$76(this.f$0, (ControlMessage) obj);
                        }
                    }, 12, null);
                } else {
                    RtspWire.sendAndForget$default($wire, "POST", "/feedback", null, null, 12, null);
                }
                if (!this$0.closed.get()) {
                    Thread.sleep(1000L);
                }
            } catch (Exception error) {
                if (!this$0.closed.get()) {
                    throw error;
                }
            }
        }
        return Unit.INSTANCE;
    }

    static final Unit start$lambda$77$lambda$76(AirPlaySender this$0, ControlMessage it) throws ProtocolException {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.receiveIapResponse(it, true);
        return Unit.INSTANCE;
    }

    private final void startAirPlayKeepAlive(final InetAddress remote, final int port) throws SocketException {
        final DatagramSocket udp = (DatagramSocket) own(new DatagramSocket((SocketAddress) null));
        this.bindDatagram.invoke(udp);
        InetAddress it = this.localAddress.invoke();
        if (it != null) {
            udp.bind(new InetSocketAddress(it, 0));
        }
        run("carplay-udp-keepalive", new Function0() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AirPlaySender.startAirPlayKeepAlive$lambda$81(this.f$0, udp, remote, port);
            }
        });
    }

    static final Unit startAirPlayKeepAlive$lambda$81(AirPlaySender this$0, DatagramSocket $udp, InetAddress $remote, int $port) {
        SecureRandom random = new SecureRandom();
        long count = 0;
        while (!this$0.closed.get() && !$udp.isClosed()) {
            try {
                byte[] payload = new byte[4];
                random.nextBytes(payload);
                $udp.send(new DatagramPacket(payload, payload.length, $remote, $port));
                count++;
                if (count <= 2 || count % 60 == 0) {
                    this$0.events.diagnostic("AirPlay UDP keepAlive #" + count + " → " + $remote.getHostAddress() + ":" + $port);
                }
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
            } catch (Exception error) {
                if (!this$0.closed.get()) {
                    this$0.events.diagnostic("AirPlay UDP keepAlive 结束：" + error.getMessage());
                }
            }
        }
        return Unit.INSTANCE;
    }

    private final void startVideoKeepAlive(final boolean withStats) {
        run("carplay-video-keepalive", new Function0() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda31
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return AirPlaySender.startVideoKeepAlive$lambda$83(this.f$0, withStats);
            }
        });
    }

    /* JADX WARN: Code duplicated, block: B:59:0x00e1  */
    static final Unit startVideoKeepAlive$lambda$83(AirPlaySender this$0, boolean $withStats) throws Throwable {
        Socket socket;
        Throwable th;
        boolean $withStats2;
        while (!this$0.closed.get()) {
            try {
                try {
                    if (this$0.videoStreamReady) {
                        break;
                    }
                    Thread.sleep(25L);
                } catch (InterruptedException e) {
                } catch (Exception e2) {
                    error = e2;
                    if (!this$0.closed.get()) {
                        this$0.events.diagnostic("视频 TCP keepAlive 结束：" + error.getMessage());
                    }
                    return Unit.INSTANCE;
                }
            } catch (InterruptedException e3) {
            } catch (Exception e4) {
                error = e4;
            }
        }
        while (!this$0.closed.get()) {
            Thread.sleep(1000L);
            if (this$0.closed.get() || (socket = this$0.video) == null) {
                break;
                break;
            }
            synchronized (this$0.videoWriteLock) {
                try {
                    if (this$0.closed.get() || socket.isClosed()) {
                        $withStats2 = $withStats;
                    } else {
                        int frameCount = this$0.videoFramesSinceKeepAlive;
                        long byteCount = this$0.videoBytesSinceKeepAlive;
                        this$0.videoFramesSinceKeepAlive = 0;
                        this$0.videoBytesSinceKeepAlive = 0L;
                        $withStats2 = $withStats;
                        byte[] packet = VideoPackets.INSTANCE.keepAlive($withStats2, frameCount, frameCount, byteCount);
                        socket.getOutputStream().write(packet);
                        socket.getOutputStream().flush();
                        this$0.videoKeepAliveCount++;
                        if (this$0.videoKeepAliveCount <= 3 || this$0.videoKeepAliveCount % 30 == 0) {
                            this$0.events.diagnostic("视频 TCP keepAlive #" + this$0.videoKeepAliveCount + " opcode=" + ($withStats2 ? 5 : 2) + " frames=" + frameCount);
                        }
                    }
                    try {
                        Unit unit = Unit.INSTANCE;
                        try {
                        } catch (InterruptedException e5) {
                        } catch (Exception e6) {
                            error = e6;
                            if (!this$0.closed.get()) {
                                this$0.events.diagnostic("视频 TCP keepAlive 结束：" + error.getMessage());
                            }
                            return Unit.INSTANCE;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            }
            $withStats = $withStats2;
        }
        return Unit.INSTANCE;
    }

    private final void setupAudio(RtspWire wire, SenderAudioFormat format, byte[] shared, InetAddress address) throws SocketException {
        Object objM159constructorimpl;
        Object element$iv;
        Object element$iv2;
        long id = new SecureRandom().nextLong() & Long.MAX_VALUE;
        DatagramSocket local = (DatagramSocket) own(new DatagramSocket((SocketAddress) null));
        this.bindDatagram.invoke(local);
        local.bind(new InetSocketAddress(this.localAddress.invoke(), 0));
        boolean usedLegacy = false;
        try {
            Result.Companion companion = Result.INSTANCE;
            AirPlaySender $this$setupAudio_u24lambda_u2486 = this;
            Iterator it = INSTANCE.streams(RtspWire.request$default(wire, "SETUP", "/", INSTANCE.encode(MapsKt.mapOf(TuplesKt.to("streams", setupAudio$buildMultiStreamSetup(format, id, local, false)))), plistHeaders, null, 16, null).getBody()).iterator();
            while (true) {
                if (!it.hasNext()) {
                    element$iv2 = null;
                    break;
                }
                element$iv2 = it.next();
                Map it2 = (Map) element$iv2;
                AirPlaySender $this$setupAudio_u24lambda_u2487 = $this$setupAudio_u24lambda_u2486;
                if (INSTANCE.number(it2.get("type")) == format.getStreamType()) {
                    break;
                } else {
                    $this$setupAudio_u24lambda_u2486 = $this$setupAudio_u24lambda_u2487;
                }
            }
            objM159constructorimpl = Result.m159constructorimpl((Map) element$iv2);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m165isFailureimpl(objM159constructorimpl)) {
            objM159constructorimpl = null;
        }
        Map accepted = (Map) objM159constructorimpl;
        if (accepted == null) {
            AirPlaySender $this$setupAudio_u24lambda_u2488 = this;
            usedLegacy = true;
            $this$setupAudio_u24lambda_u2488.events.diagnostic("音频 SETUP 未接受参考字段集，回退为含 controlPort 的旧字段集");
            Iterable $this$firstOrNull$iv = INSTANCE.streams(RtspWire.request$default(wire, "SETUP", "/", INSTANCE.encode(MapsKt.mapOf(TuplesKt.to("streams", setupAudio$buildMultiStreamSetup(format, id, local, true)))), plistHeaders, null, 16, null).getBody());
            Iterator it3 = $this$firstOrNull$iv.iterator();
            while (true) {
                if (!it3.hasNext()) {
                    element$iv = null;
                    break;
                }
                element$iv = it3.next();
                Map it4 = (Map) element$iv;
                AirPlaySender $this$setupAudio_u24lambda_u2489 = $this$setupAudio_u24lambda_u2488;
                Iterable $this$firstOrNull$iv2 = $this$firstOrNull$iv;
                if (INSTANCE.number(it4.get("type")) == format.getStreamType()) {
                    break;
                }
                $this$setupAudio_u24lambda_u2488 = $this$setupAudio_u24lambda_u2489;
                $this$firstOrNull$iv = $this$firstOrNull$iv2;
            }
            accepted = (Map) element$iv;
            if (accepted == null) {
                throw new IllegalStateException("车机拒绝协商后的音频流".toString());
            }
        }
        this.events.diagnostic("音频 SETUP 已接受：" + (usedLegacy ? "旧字段集（含 controlPort）" : "参考字段集（无 controlPort）") + "（已发送多流描述防 30s 超时），车机回包字段 " + CollectionsKt.joinToString$default(accepted.keySet(), ",", null, null, 0, null, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return String.valueOf(obj);
            }
        }, 30, null));
        int port = INSTANCE.number(accepted.get("dataPort"));
        if (!(1 <= port && port < 65536)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        this.audioAddress = new InetSocketAddress(address, port);
        this.audioSocket = local;
        this.aacTimestampClock = format.isPcm() ? null : new MonotonicAudioTimestamp(format);
        this.lastAudioTimestamp = null;
        this.nextAudioTimestamp = null;
        this.audioPacketsSent = 0L;
        Object obj = accepted.get("controlPort");
        Number number = obj instanceof Number ? (Number) obj : null;
        int controlPort = number != null ? number.intValue() & 65535 : 0;
        Object responseId = accepted.get("streamConnectionID");
        if (responseId == null) {
            responseId = Long.valueOf(id);
        }
        String responseSeed = CarPlayMediaEngineKt.unsignedPlistDecimal(responseId);
        if (responseSeed == null) {
            responseSeed = String.valueOf(id);
        }
        AirPlayCrypto airPlayCrypto = AirPlayCrypto.INSTANCE;
        byte[] bytes = ("DataStream-Salt" + responseSeed).getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        byte[] bytes2 = CarPlayMediaEngine.DATASTREAM_OUTPUT_KEY.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes2, "getBytes(...)");
        this.audioKey = AirPlayCrypto.hkdfSha512$default(airPlayCrypto, shared, bytes, bytes2, 0, 8, null);
        SenderEvents senderEvents = this.events;
        int streamType = format.getStreamType();
        AudioCodecKind codec = format.getCodec();
        int sampleRate = format.getSampleRate();
        int channels = format.getChannels();
        int samplesPerPacket = format.getSamplesPerPacket();
        format.isPcm();
        senderEvents.diagnostic("音频流已建立：type=" + streamType + " " + codec + " " + sampleRate + "Hz/" + channels + "声道 spf=" + samplesPerPacket + " port=" + port + " audioLatencyMs=1000 inputBufferSamples=" + (format.isPcm() ? 0 : AAC_INPUT_BUFFER_SAMPLES) + " outputBufferSamples=" + (format.isPcm() ? 0 : AAC_OUTPUT_BUFFER_SAMPLES) + " streamConnectionID=" + responseSeed);
        if (1 <= controlPort && controlPort < 65536) {
            startAudioSync(address, controlPort, format);
        } else {
            this.events.diagnostic("车机音频回包未提供 controlPort：跳过 RTP↔NTP 同步包");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Map<String, Object> setupAudio$descriptor(SenderAudioFormat $format, long id, DatagramSocket local, int streamType, long audioFormat, boolean legacy, String audioType) {
        LinkedHashMap $this$setupAudio_u24descriptor_u24lambda_u2484 = new LinkedHashMap();
        if ($format.isPcm() || legacy) {
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("streamConnectionID", Long.valueOf(id));
        }
        if (!$format.isPcm() && !legacy && SetsKt.setOf((Object[]) new Integer[]{100, 102}).contains(Integer.valueOf(streamType))) {
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("controlPort", Integer.valueOf(local.getLocalPort()));
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("spf", Integer.valueOf(streamType == 100 ? 1024 : 960));
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("audioFormat", Long.valueOf(audioFormat));
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("supportsHighAccuracyTimestamps", true);
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("audioType", audioType);
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("audioLatencyMs", 1000);
        } else {
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("type", Integer.valueOf(streamType));
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("audioType", audioType);
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("audioFormat", Long.valueOf(audioFormat));
            if (legacy) {
                $this$setupAudio_u24descriptor_u24lambda_u2484.put("controlPort", Integer.valueOf(local.getLocalPort()));
            }
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("ct", Integer.valueOf($format.isPcm() ? 1 : 4));
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("spf", Integer.valueOf(streamType == 100 ? 1024 : 960));
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("sr", Integer.valueOf($format.getSampleRate()));
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("audioLatencyMs", 1000);
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("input", false);
        }
        if (!$format.isPcm() && legacy && SetsKt.setOf((Object[]) new Integer[]{100, 102}).contains(Integer.valueOf(streamType))) {
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("encryptionType", 0);
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("channels", Integer.valueOf(streamType == 100 ? 2 : 1));
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("bytesPerFrame", Integer.valueOf((streamType == 100 ? 2 : 1) * 2));
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("framesPerPacket", Integer.valueOf(streamType == 100 ? 1024 : 960));
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("audioMode", 0);
            $this$setupAudio_u24descriptor_u24lambda_u2484.put("supportsHighAccuracyTimestamps", true);
        }
        return $this$setupAudio_u24descriptor_u24lambda_u2484;
    }

    private static final List<Map<String, Object>> setupAudio$buildMultiStreamSetup(SenderAudioFormat $format, long id, DatagramSocket local, boolean legacy) {
        List streamList = new ArrayList();
        streamList.add(setupAudio$descriptor($format, id, local, $format.getStreamType(), $format.getFormatBits(), legacy, "media"));
        if (SetsKt.setOf((Object[]) new Integer[]{100, 102}).contains(Integer.valueOf($format.getStreamType()))) {
            if ($format.getStreamType() == 100) {
                streamList.add(setupAudio$descriptor($format, id, local, AirPlaySession.STREAM_TYPE_ALT_AUDIO, CarPlayMediaEngine.OPUS_48K, legacy, "default"));
                streamList.add(setupAudio$descriptor($format, id, local, 102, CarPlayMediaEngine.OPUS_48K, legacy, "speechRecognition"));
            } else if ($format.getStreamType() == 102) {
                streamList.add(setupAudio$descriptor($format, id, local, AirPlaySession.STREAM_TYPE_ALT_AUDIO, CarPlayMediaEngine.OPUS_48K, legacy, "default"));
            }
            streamList.add(setupAudio$descriptor($format, id, local, AirPlaySession.STREAM_TYPE_AUX_AUDIO, CarPlayMediaEngine.OPUS_48K, legacy, "speechRecognition"));
        }
        return streamList;
    }

    private final void startAudioSync(InetAddress address, final int controlPort, final SenderAudioFormat format) {
        Object objM159constructorimpl;
        Thread it = this.audioSyncThread;
        if (it == null || !it.isAlive()) {
            try {
                Result.Companion companion = Result.INSTANCE;
                AirPlaySender $this$startAudioSync_u24lambda_u2492 = this;
                DatagramSocket it2 = new DatagramSocket((SocketAddress) null);
                $this$startAudioSync_u24lambda_u2492.bindDatagram.invoke(it2);
                it2.bind(new InetSocketAddress($this$startAudioSync_u24lambda_u2492.localAddress.invoke(), 0));
                it2.connect(address, controlPort);
                objM159constructorimpl = Result.m159constructorimpl(it2);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            Throwable it3 = Result.m162exceptionOrNullimpl(objM159constructorimpl);
            if (it3 != null) {
                this.events.diagnostic("音频 RTP↔NTP 同步套接字创建失败：" + it3.getMessage());
                return;
            }
            final DatagramSocket socket = (DatagramSocket) objM159constructorimpl;
            this.audioSyncSocket = socket;
            this.audioSyncSequence = 0;
            Thread worker = new Thread(new Runnable() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AirPlaySender.startAudioSync$lambda$94(this.f$0, socket, controlPort, format);
                }
            }, "carplay-audio-sync");
            worker.setDaemon(true);
            this.audioSyncThread = worker;
            worker.start();
            this.events.diagnostic("音频 RTP↔NTP 同步已启用：" + address + ":" + controlPort + "，每 1000ms");
        }
    }

    static final void startAudioSync$lambda$94(AirPlaySender this$0, DatagramSocket $socket, int $controlPort, SenderAudioFormat $format) {
        boolean first = true;
        while (!this$0.closed.get() && !$socket.isClosed()) {
            try {
                Long current = this$0.lastAudioTimestamp;
                Long next = this$0.nextAudioTimestamp;
                if (current == null || next == null) {
                    Thread.sleep(25L);
                } else {
                    byte[] packet = AudioSyncPacket.INSTANCE.build(this$0.audioSyncSequence, current.longValue(), next.longValue(), this$0.ntpBytes(this$0.clock.syncedNtp()), first);
                    $socket.send(new DatagramPacket(packet, packet.length));
                    this$0.audioSyncSequence = (this$0.audioSyncSequence + 1) & 65535;
                    if (first) {
                        try {
                            this$0.events.diagnostic("音频首个 RTP↔NTP 同步包已发送：controlPort=" + $controlPort + " ts=" + current + "→" + next + " rate=" + $format.getSampleRate() + "Hz");
                            first = false;
                        } catch (InterruptedException e) {
                            return;
                        } catch (Exception e2) {
                            error = e2;
                            if (!this$0.closed.get()) {
                                this$0.events.diagnostic("音频 RTP↔NTP 同步停止：" + error.getMessage());
                                return;
                            }
                            return;
                        }
                    }
                    Thread.sleep(1000L);
                }
            } catch (InterruptedException e3) {
                return;
            } catch (Exception e4) {
                error = e4;
            }
        }
    }

    private final byte[] ntpBytes(BigInteger value) {
        byte[] bArrCopyOfRange;
        byte[] raw = value.and(BigInteger.ONE.shiftLeft(64).subtract(BigInteger.ONE)).toByteArray();
        if (raw.length > 8) {
            Intrinsics.checkNotNull(raw);
            bArrCopyOfRange = ArraysKt.copyOfRange(raw, raw.length - 8, raw.length);
        } else {
            bArrCopyOfRange = raw;
        }
        byte[] trimmed = bArrCopyOfRange;
        byte[] it = new byte[8];
        Intrinsics.checkNotNull(trimmed);
        ArraysKt.copyInto$default(trimmed, it, it.length - trimmed.length, 0, 0, 12, (Object) null);
        return it;
    }

    public final void config(byte[] bytes) {
        String str;
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        synchronized (this.videoEpochLock) {
            byte[] bArrCopyOf = Arrays.copyOf(bytes, bytes.length);
            Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
            this.avcC = bArrCopyOf;
            this.configGeneration++;
            SenderEvents senderEvents = this.events;
            long j = this.configGeneration;
            int length = bytes.length;
            if (this.videoRecoveryPending) {
                str = "（恢复轮次要求 >= " + this.requiredConfigGeneration + "）";
            } else {
                str = "";
            }
            senderEvents.diagnostic("收到视频 CONFIG：generation=" + j + "，大小=" + length + "B" + str);
            Unit unit = Unit.INSTANCE;
        }
    }

    public final void frame(byte[] bytes, long ptsUs, boolean keyFrame) {
        boolean recovered;
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        if (this.closed.get() || !this.modes.ownsScreen() || this.avcC == null) {
            return;
        }
        if (this.videoRecoveryPending && !keyFrame) {
            return;
        }
        if (!keyFrame) {
            recovered = false;
        } else {
            synchronized (this.videoEpochLock) {
                if (this.videoRecoveryPending && this.configGeneration < this.requiredConfigGeneration) {
                    this.events.diagnostic("丢弃恢复轮次过早 IDR：CONFIG generation=" + this.configGeneration + "，需要 >= " + this.requiredConfigGeneration);
                    return;
                }
                this.keyFramePending = false;
                boolean recovered2 = this.videoRecoveryPending;
                this.videoRecoveryPending = false;
                Unit unit = Unit.INSTANCE;
                recovered = recovered2;
            }
        }
        if (recovered) {
            this.events.diagnostic("恢复轮次已接收新 IDR，等待视频线程先发 CONFIG 再发首帧");
        }
        long before = this.queue.getDropped();
        if (!this.queue.offer(new EncodedVideo(bytes, ptsUs, keyFrame, 0L, 0L, 24, null))) {
            requestKeyFrame$default(this, false, 1, null);
        }
        long dropped = this.queue.getDropped() - before;
        if (dropped > 0) {
            this.events.dropped(dropped);
        }
    }

    public final void audio(byte[] bytes, long ptsUs) throws Throwable {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        if (this.closed.get()) {
            return;
        }
        if (bytes.length == 0) {
            return;
        }
        synchronized (this.audioQueueLock) {
            try {
                if (this.closed.get() || !this.modes.ownsAudio()) {
                    return;
                }
                if (this.audioQueue.size() >= 24) {
                    try {
                        this.audioQueue.removeFirst();
                        this.audioQueueDrops++;
                        if (this.audioQueueDrops == 1 || this.audioQueueDrops % 100 == 0) {
                            this.events.diagnostic("音频实时队列丢弃过期块：" + this.audioQueueDrops);
                        }
                    } catch (Throwable th) {
                        th = th;
                    }
                }
                ArrayDeque<AudioChunk> arrayDeque = this.audioQueue;
                byte[] bArrCopyOf = Arrays.copyOf(bytes, bytes.length);
                Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
                try {
                    arrayDeque.addLast(new AudioChunk(bArrCopyOf, ptsUs, this.audioGeneration));
                    this.audioQueueLock.notifyAll();
                    Unit unit = Unit.INSTANCE;
                    return;
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Throwable th3) {
                th = th3;
            }
            throw th;
        }
    }

    private final void startAudioWorker() {
        if (this.audioWorker != null) {
            return;
        }
        Thread $this$startAudioWorker_u24lambda_u24104 = new Thread(new Runnable() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                AirPlaySender.startAudioWorker$lambda$103(this.f$0);
            }
        }, "carplay-audio-send");
        $this$startAudioWorker_u24lambda_u24104.setDaemon(true);
        $this$startAudioWorker_u24lambda_u24104.start();
        this.audioWorker = $this$startAudioWorker_u24lambda_u24104;
    }

    static final void startAudioWorker$lambda$103(final AirPlaySender this$0) {
        Object objM159constructorimpl;
        while (!this$0.closed.get()) {
            AudioQueueTake result = AudioQueueWaitKt.takeAudioQueueItem(this$0.audioQueueLock, this$0.audioQueue, new Function0() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda8
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return Boolean.valueOf(AirPlaySender.startAudioWorker$lambda$103$lambda$100(this.f$0));
                }
            });
            if (!(result instanceof AudioQueueTake.Item)) {
                if (!Intrinsics.areEqual(result, AudioQueueTake.Stopped.INSTANCE)) {
                    if (!Intrinsics.areEqual(result, AudioQueueTake.Interrupted.INSTANCE)) {
                        throw new NoWhenBranchMatchedException();
                    }
                    if (!this$0.closed.get()) {
                        InterruptedException failure = new InterruptedException("AirPlay audio sender was interrupted");
                        this$0.events.diagnostic("音频发送线程被意外中断：会话将正常收尾");
                        this$0.events.failed(failure);
                    }
                    Thread.currentThread().interrupt();
                    return;
                }
                return;
            }
            AudioChunk chunk = (AudioChunk) ((AudioQueueTake.Item) result).getValue();
            try {
                Result.Companion companion = Result.INSTANCE;
                this$0.sendAudioChunk(chunk);
                objM159constructorimpl = Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            Throwable it = Result.m162exceptionOrNullimpl(objM159constructorimpl);
            if (it != null && !this$0.closed.get()) {
                this$0.events.diagnostic("音频发送线程异常：" + it.getMessage());
            }
        }
    }

    static final boolean startAudioWorker$lambda$103$lambda$100(AirPlaySender this$0) {
        return !this$0.closed.get();
    }

    private final void sendAudioChunk(AudioChunk chunk) {
        SenderAudioFormat format;
        byte[] bArrCopyOfRange;
        byte[] key = this.audioKey;
        if (key == null || (format = this.selectedAudio) == null) {
            return;
        }
        if (format.isPcm()) {
            int frameBytes = format.getPcmPacketBytes();
            synchronized (this.audioQueueLock) {
                if (isCurrentAudioChunkLocked(chunk)) {
                    byte[] merged = this.pcmCarry.length == 0 ? chunk.getBytes() : ArraysKt.plus(this.pcmCarry, chunk.getBytes());
                    int offset = 0;
                    while (merged.length - offset >= frameBytes && !this.closed.get()) {
                        synchronized (this.audioQueueLock) {
                            if (!isCurrentAudioChunkLocked(chunk)) {
                                return;
                            }
                            byte[] payload = ArraysKt.copyOfRange(merged, offset, offset + frameBytes);
                            paceAudioPacket((((long) format.getSamplesPerPacket()) * KeyFrameRebuildGate.RETRY_MIN_INTERVAL_NANOS) / ((long) format.getSampleRate()));
                            sendAudioPacket(key, format, pcmToWire(payload), pcmTimestamp(format));
                            this.pcmSamples += format.getSamplesPerPacket();
                            offset += frameBytes;
                            if (offset >= merged.length) {
                                bArrCopyOfRange = new byte[0];
                            } else {
                                bArrCopyOfRange = ArraysKt.copyOfRange(merged, offset, merged.length);
                            }
                            this.pcmCarry = bArrCopyOfRange;
                            Unit unit = Unit.INSTANCE;
                        }
                    }
                    synchronized (this.audioQueueLock) {
                        if (isCurrentAudioChunkLocked(chunk)) {
                            this.pcmCarry = offset >= merged.length ? new byte[0] : ArraysKt.copyOfRange(merged, offset, merged.length);
                            if (this.pcmCarry.length > frameBytes * 4) {
                                this.pcmCarry = new byte[0];
                            }
                            Unit unit2 = Unit.INSTANCE;
                            return;
                        }
                        return;
                    }
                }
                return;
            }
        }
        synchronized (this.audioQueueLock) {
            if (isCurrentAudioChunkLocked(chunk)) {
                paceAudioPacket((((long) format.getSamplesPerPacket()) * KeyFrameRebuildGate.RETRY_MIN_INTERVAL_NANOS) / ((long) format.getSampleRate()));
                MonotonicAudioTimestamp monotonicAudioTimestamp = this.aacTimestampClock;
                int timestamp = monotonicAudioTimestamp != null ? monotonicAudioTimestamp.next(chunk.getPtsUs()) : format.rtpTimestamp(chunk.getPtsUs());
                sendAudioPacket(key, format, chunk.getBytes(), timestamp);
                Unit unit3 = Unit.INSTANCE;
            }
        }
    }

    private final boolean isCurrentAudioChunkLocked(AudioChunk chunk) {
        return !this.closed.get() && chunk.getGeneration() == this.audioGeneration && this.modes.ownsAudio();
    }

    private final void setAudioOwnership(boolean phone) {
        synchronized (this.audioQueueLock) {
            boolean changed = this.modes.ownsAudio() != phone;
            this.modes.audio(phone);
            if (changed) {
                invalidateAudioGenerationLocked();
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    private final void invalidateAudioGenerationLocked() {
        this.audioGeneration++;
        this.audioQueue.clear();
        this.pcmCarry = new byte[0];
        this.audioNextSendNanos = 0L;
        this.audioQueueLock.notifyAll();
    }

    static /* synthetic */ void paceAudioPacket$default(AirPlaySender airPlaySender, long j, int i, Object obj) {
        if ((i & 1) != 0) {
            j = AUDIO_PACKET_INTERVAL_NANOS;
        }
        airPlaySender.paceAudioPacket(j);
    }

    private final void paceAudioPacket(long intervalNanos) {
        long now = System.nanoTime();
        long wait = this.audioNextSendNanos - now;
        if (wait > 0) {
            if (wait > intervalNanos) {
                this.audioNextSendNanos = now + intervalNanos;
            } else {
                LockSupport.parkNanos(wait);
            }
        }
        long after = System.nanoTime();
        this.audioNextSendNanos = Math.max(after, this.audioNextSendNanos) + intervalNanos;
    }

    private final byte[] pcmToWire(byte[] pcm) {
        byte[] out = Arrays.copyOf(pcm, pcm.length);
        Intrinsics.checkNotNullExpressionValue(out, "copyOf(...)");
        for (int i = 0; i + 1 < out.length; i += 2) {
            byte first = out[i];
            out[i] = out[i + 1];
            out[i + 1] = first;
        }
        return out;
    }

    private final int pcmTimestamp(SenderAudioFormat format) {
        return this.pcmBase + this.pcmSamples;
    }

    private final void sendAudioPacket(byte[] key, SenderAudioFormat format, byte[] payload, int timestamp) throws InvalidCipherTextException, IOException {
        ByteBuffer $this$sendAudioPacket_u24lambda_u24110 = ByteBuffer.allocate(12).order(ByteOrder.BIG_ENDIAN);
        $this$sendAudioPacket_u24lambda_u24110.put(ByteCompanionObject.MIN_VALUE);
        $this$sendAudioPacket_u24lambda_u24110.put((byte) (format.getStreamType() & WorkQueueKt.MASK));
        int i = this.audioSequence;
        this.audioSequence = i + 1;
        $this$sendAudioPacket_u24lambda_u24110.putShort((short) i);
        $this$sendAudioPacket_u24lambda_u24110.putInt(timestamp);
        $this$sendAudioPacket_u24lambda_u24110.putInt(0);
        byte[] header = $this$sendAudioPacket_u24lambda_u24110.array();
        AirPlayCrypto airPlayCrypto = AirPlayCrypto.INSTANCE;
        long j = this.audioCounter;
        this.audioCounter = j + 1;
        byte[] nonce = airPlayCrypto.nonce64(j);
        AirPlayCrypto airPlayCrypto2 = AirPlayCrypto.INSTANCE;
        Intrinsics.checkNotNull(header);
        byte[] sealed = airPlayCrypto2.chachaSeal(key, nonce, payload, ArraysKt.copyOfRange(header, 4, 12));
        byte[] packet = ArraysKt.plus(ArraysKt.plus(header, sealed), ArraysKt.copyOfRange(nonce, 4, 12));
        DatagramSocket datagramSocket = this.audioSocket;
        if (datagramSocket == null) {
            throw new IllegalStateException("Required value was null.".toString());
        }
        datagramSocket.send(new DatagramPacket(packet, packet.length, this.audioAddress));
        this.lastAudioTimestamp = Long.valueOf(((long) timestamp) & MonotonicAudioTimestamp.RTP_MASK);
        Long l = this.lastAudioTimestamp;
        Intrinsics.checkNotNull(l);
        this.nextAudioTimestamp = Long.valueOf(l.longValue() + ((long) format.getSamplesPerPacket()));
        this.events.audioSent();
        this.audioPacketsSent++;
        long sent = this.audioPacketsSent;
        if (sent != 1) {
            if (sent % 200 == 0) {
                this.events.diagnostic("音频已发送 " + sent + " 包（" + format.getSampleRate() + "Hz，第 " + timestamp + " 采样）");
                return;
            }
            return;
        }
        this.events.diagnostic("音频首包：type=" + format.getStreamType() + " " + format.getCodec() + " " + payload.length + "B ts=" + timestamp + " seq=" + ((this.audioSequence - 1) & 65535) + " 目标=" + this.audioAddress);
    }

    /* JADX WARN: Code duplicated, block: B:25:0x0053  */
    private final ControlReply handleCommand(byte[] body) {
        char c;
        boolean accepted;
        String str;
        SessionModes.Change change;
        boolean forwarded;
        if (body.length == 0) {
            return new ControlReply(501, null, 2, null);
        }
        Map<?, ?> mapPlist = INSTANCE.plist(body);
        Object obj = mapPlist.get("type");
        if (Intrinsics.areEqual(obj, "iAPSendMessage")) {
            byte[] data = IapCommandData.payload$default(IapCommandData.INSTANCE, mapPlist, false, 2, null);
            if (data == null) {
                return new ControlReply(200, null, 2, null);
            }
            if (!receiveIap(data)) {
                PhoneIapTunnel phoneIapTunnelEnsureCommandIap = ensureCommandIap();
                forwarded = phoneIapTunnelEnsureCommandIap != null && phoneIapTunnelEnsureCommandIap.offer(data);
            }
            String description = INSTANCE.describeIapMessage(data);
            if (!forwarded && this.reportedCommands.add("iAP:" + description)) {
                this.events.diagnostic("iAP 控制数据未被链路接收：字节=" + data.length + "（" + description + "）");
            } else if (this.reportedCommands.add("first:" + description)) {
                this.events.diagnostic("收到车机 iAP2：字节=" + data.length + "（" + description + "），已交给链路");
            }
            return new ControlReply(200, null, 2, null);
        }
        if (Intrinsics.areEqual(obj, "forceKeyFrame")) {
            requestKeyFrame$default(this, false, 1, null);
            return new ControlReply(200, null, 2, null);
        }
        if (Intrinsics.areEqual(obj, "changeModes")) {
            synchronized (this.audioQueueLock) {
                boolean ownedBefore = this.modes.ownsAudio();
                change = this.modes.change(mapPlist);
                if (ownedBefore != this.modes.ownsAudio()) {
                    invalidateAudioGenerationLocked();
                }
            }
            Map<String, Object> reply = change.getReply();
            if (change.getAudioRestored() && this.selectedAudio != null) {
                setAudioOwnership(true);
                this.events.diagnostic("车机已归还音频资源，重新声明由手机播放");
            }
            if (change.getScreenRestored()) {
                run("carplay-change-modes-recovery", new Function0() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda9
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return AirPlaySender.handleCommand$lambda$115(this.f$0);
                    }
                });
            }
            this.events.screenOwnership(this.modes.ownsScreen());
            if (this.commandModes) {
                run("carplay-modes-update", new Function0() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda10
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return AirPlaySender.handleCommand$lambda$117(this.f$0);
                    }
                });
            }
            Companion companion = INSTANCE;
            Pair[] pairArr = new Pair[2];
            pairArr[0] = TuplesKt.to(NotificationCompat.CATEGORY_STATUS, 0);
            Object objEmptyMap = reply.get("params");
            if (objEmptyMap == null) {
                objEmptyMap = MapsKt.emptyMap();
            }
            pairArr[1] = TuplesKt.to("params", objEmptyMap);
            return new ControlReply(200, companion.encode(MapsKt.mapOf(pairArr)));
        }
        if (!Intrinsics.areEqual(obj, "hidSendReport")) {
            if (Intrinsics.areEqual(obj, "requestUI")) {
                boolean reclaimed = this.modes.claimScreen();
                if (reclaimed) {
                    this.events.screenOwnership(true);
                }
                if (reclaimed) {
                    run("carplay-request-ui-recovery", new Function0() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda13
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return AirPlaySender.handleCommand$lambda$124(this.f$0);
                        }
                    });
                    this.events.diagnostic(reclaimed ? "requestUI 屏幕归还已确认" : "requestUI 检测到上一轮恢复仍未完成，重试资源声明和关键帧");
                } else {
                    this.events.diagnostic("requestUI 已收到：屏幕仍归手机且没有待恢复关键帧");
                }
                boolean accepted2 = this.events.input(mapPlist);
                return new ControlReply(accepted2 ? 200 : 501, null, 2, null);
            }
            if (Intrinsics.areEqual(obj, "requestSiri") || Intrinsics.areEqual(obj, "setNightMode") || Intrinsics.areEqual(obj, "updateViewArea") || Intrinsics.areEqual(obj, "setLimitedUI")) {
                int i = 501;
                return new ControlReply(this.events.input(mapPlist) ? 200 : i, null, 2, null);
            }
            this.events.diagnostic("不支持的车机命令：" + mapPlist.get("type"));
            return new ControlReply(501, null, 2, null);
        }
        Object obj2 = mapPlist.get("uuid");
        String uuid = obj2 != null ? obj2.toString() : null;
        HidInput device = this.touchDevices.get(uuid);
        HidKeys keys = this.keyDevices.get(uuid);
        Object obj3 = mapPlist.get("hidReport");
        byte[] report = obj3 instanceof byte[] ? (byte[]) obj3 : null;
        if (report == null) {
            return new ControlReply(this.events.input(mapPlist) ? 200 : 501, null, 2, null);
        }
        boolean isTouch = device != null;
        if (isTouch) {
            int seen = this.touchReports;
            this.touchReports = seen + 1;
            if (seen < 8) {
                Pair<Integer, Integer> touchExtent = device != null ? device.getTouchExtent() : null;
                SenderEvents senderEvents = this.events;
                int length = report.length;
                String strJoinToString$default = ArraysKt.joinToString$default(report, (CharSequence) " ", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda11
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj4) {
                        return AirPlaySender.handleCommand$lambda$118(((Byte) obj4).byteValue());
                    }
                }, 30, (Object) null);
                if (touchExtent != null) {
                    c = 1;
                    str = touchExtent.getFirst() + "x" + touchExtent.getSecond();
                    if (str == null) {
                    }
                    senderEvents.diagnostic("触摸原始 #" + seen + " 长度=" + length + " hex=" + strJoinToString$default + " 量程=" + str);
                } else {
                    c = 1;
                }
                str = "未声明";
                senderEvents.diagnostic("触摸原始 #" + seen + " 长度=" + length + " hex=" + strJoinToString$default + " 量程=" + str);
            } else {
                c = 1;
            }
        } else {
            c = 1;
            this.events.diagnostic("车机按键报告 uuid=" + uuid + " 长度=" + report.length + " hex=" + ArraysKt.joinToString$default(report, (CharSequence) " ", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, new Function1() { // from class: com.carplay.sender.AirPlaySender$$ExternalSyntheticLambda12
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj4) {
                    return AirPlaySender.handleCommand$lambda$120(((Byte) obj4).byteValue());
                }
            }, 30, (Object) null));
        }
        if (device != null) {
            SenderEvents senderEvents2 = this.events;
            Pair[] pairArr2 = new Pair[2];
            pairArr2[0] = TuplesKt.to("type", "touchSamples");
            pairArr2[c] = TuplesKt.to("samples", device.touches(report));
            accepted = senderEvents2.input(MapsKt.mapOf(pairArr2));
        } else {
            SenderEvents senderEvents3 = this.events;
            if (keys != null) {
                Pair[] pairArr3 = new Pair[2];
                pairArr3[forwarded] = TuplesKt.to("type", "keySamples");
                pairArr3[c] = TuplesKt.to("report", keys.report(report));
                accepted = senderEvents3.input(MapsKt.mapOf(pairArr3));
            } else {
                accepted = senderEvents3.input(mapPlist);
            }
        }
        return new ControlReply(accepted ? 200 : 501, null, 2, null);
    }

    static final Unit handleCommand$lambda$115(AirPlaySender this$0) {
        Object objM159constructorimpl;
        try {
            Result.Companion companion = Result.INSTANCE;
            RtspWire rtspWire = this$0.control;
            objM159constructorimpl = Result.m159constructorimpl(rtspWire != null ? RtspWire.request$default(rtspWire, "POST", "/command", INSTANCE.encode(this$0.modes.snapshot()), plistHeaders, null, 16, null) : null);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m166isSuccessimpl(objM159constructorimpl)) {
            this$0.events.diagnostic("changeModes 屏幕归还已声明，开始请求恢复关键帧");
        }
        Throwable it = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (it != null) {
            this$0.events.diagnostic("changeModes 屏幕归还声明失败：" + it.getMessage());
        }
        if (!this$0.closed.get()) {
            this$0.requestKeyFrame(true);
        }
        return Unit.INSTANCE;
    }

    static final Unit handleCommand$lambda$117(AirPlaySender this$0) {
        try {
            Result.Companion companion = Result.INSTANCE;
            RtspWire rtspWire = this$0.control;
            Result.m159constructorimpl(rtspWire != null ? RtspWire.request$default(rtspWire, "POST", "/command", INSTANCE.encode(this$0.modes.snapshot()), plistHeaders, null, 16, null) : null);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        return Unit.INSTANCE;
    }

    static final CharSequence handleCommand$lambda$118(byte it) {
        String str = String.format("%02X", Arrays.copyOf(new Object[]{Byte.valueOf(it)}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    static final CharSequence handleCommand$lambda$120(byte it) {
        String str = String.format("%02X", Arrays.copyOf(new Object[]{Byte.valueOf(it)}, 1));
        Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    static final Unit handleCommand$lambda$124(AirPlaySender this$0) {
        Object objM159constructorimpl;
        try {
            Result.Companion companion = Result.INSTANCE;
            RtspWire rtspWire = this$0.control;
            objM159constructorimpl = Result.m159constructorimpl(rtspWire != null ? RtspWire.request$default(rtspWire, "POST", "/command", INSTANCE.encode(this$0.modes.snapshot()), plistHeaders, null, 16, null) : null);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th));
        }
        if (Result.m166isSuccessimpl(objM159constructorimpl)) {
            this$0.events.diagnostic("requestUI 资源声明已发送，开始请求 CarLife 恢复关键帧");
        }
        Throwable it = Result.m162exceptionOrNullimpl(objM159constructorimpl);
        if (it != null) {
            this$0.events.diagnostic("requestUI 资源声明失败：" + it.getMessage());
        }
        if (!this$0.closed.get()) {
            this$0.requestKeyFrame(true);
        }
        return Unit.INSTANCE;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        Unit unit;
        SenderAudioFormat it;
        Function1<SenderAudioFormat, Unit> function1;
        if (this.closed.compareAndSet(false, true)) {
            if (this.audioProfileSetupComplete && !this.audioProfileStable && this.audioPacketsSent > 0 && (it = this.selectedAudio) != null && (function1 = this.audioProfileRejected) != null) {
                function1.invoke(it);
            }
            this.audioProfileSetupComplete = false;
            this.audioProfileStable = false;
            synchronized (this.iapLock) {
                this.openingIap = false;
                this.earlyIap.clear();
                this.earlyIapBytes = 0;
                Unit unit2 = Unit.INSTANCE;
            }
            this.queue.close();
            synchronized (this.audioQueueLock) {
                this.audioQueue.clear();
                this.audioQueueLock.notifyAll();
                Unit unit3 = Unit.INSTANCE;
            }
            Thread thread = this.audioWorker;
            if (thread != null) {
                thread.interrupt();
            }
            this.audioWorker = null;
            Thread thread2 = this.audioSyncThread;
            if (thread2 != null) {
                thread2.interrupt();
            }
            this.audioSyncThread = null;
            try {
                Result.Companion companion = Result.INSTANCE;
                AirPlaySender $this$close_u24lambda_u24128 = this;
                DatagramSocket datagramSocket = $this$close_u24lambda_u24128.audioSyncSocket;
                if (datagramSocket != null) {
                    datagramSocket.close();
                    unit = Unit.INSTANCE;
                } else {
                    unit = null;
                }
                Result.m159constructorimpl(unit);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            this.audioSyncSocket = null;
            Iterable $this$forEach$iv = CollectionsKt.reversed(this.resources);
            for (Object element$iv : $this$forEach$iv) {
                Closeable it2 = (Closeable) element$iv;
                try {
                    Result.Companion companion3 = Result.INSTANCE;
                    AirPlaySender airPlaySender = this;
                    it2.close();
                    Result.m159constructorimpl(Unit.INSTANCE);
                } catch (Throwable th2) {
                    Result.Companion companion4 = Result.INSTANCE;
                    Result.m159constructorimpl(ResultKt.createFailure(th2));
                }
            }
            this.resources.clear();
            synchronized (this.workers) {
                Iterable $this$forEach$iv2 = this.workers;
                for (Object element$iv2 : $this$forEach$iv2) {
                    Thread it3 = (Thread) element$iv2;
                    it3.interrupt();
                }
                this.workers.clear();
                Unit unit4 = Unit.INSTANCE;
            }
        }
    }

    /* JADX INFO: compiled from: AirPlaySender.kt */
    @Metadata(d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u000b\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u001e\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020\u0001H\u0002J\u0018\u0010 \u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u001c2\u0006\u0010!\u001a\u00020\u0007H\u0002J\u0012\u0010\"\u001a\u00020\t2\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001H\u0002J\"\u0010#\u001a\f\u0012\u0002\b\u0003\u0012\u0002\b\u0003\u0018\u00010\u001c2\u000e\u0010$\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u001cH\u0002J\u0018\u0010%\u001a\u00020&2\u000e\u0010'\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u001cH\u0002J,\u0010(\u001a\u0004\u0018\u00010)2\u000e\u0010$\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u001c2\u0010\u0010*\u001a\f\u0012\u0002\b\u0003\u0012\u0002\b\u0003\u0018\u00010\u001cH\u0002J\u0010\u0010+\u001a\u00020\u00052\u0006\u0010,\u001a\u00020\fH\u0002J\u0018\u0010-\u001a\u00020\u00052\u000e\u0010$\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u001cH\u0002J\u0010\u0010.\u001a\u00020\u00052\u0006\u0010/\u001a\u00020\u0007H\u0002J\u001e\u00100\u001a\u0010\u0012\f\u0012\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u001c0\u00182\u0006\u0010!\u001a\u00020\u0007H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\fX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00050\u0018X\u0082\u0004¢\u0006\u0002\n\u0000R \u0010\u0019\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00050\u001a0\u0018X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u001cX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u001cX\u0082\u0004¢\u0006\u0002\n\u0000¨\u00061"}, d2 = {"Lcom/carplay/sender/AirPlaySender$Companion;", "", "<init>", "()V", "IAP_DATASTREAM_UUID", "", "IAP2_MARKER", "", "MAX_COMMAND_IAP_ATTEMPTS", "", "TOUCH_REPORTS_LOGGED", "AUDIO_PACKET_INTERVAL_NANOS", "", "AUDIO_SYNC_INTERVAL_MS", "AUDIO_QUEUE_LIMIT", "AAC_LATENCY_MS", "PCM_LATENCY_MS", "AAC_INPUT_BUFFER_SAMPLES", "AAC_OUTPUT_BUFFER_SAMPLES", "PCM_INPUT_BUFFER_SAMPLES", "PCM_OUTPUT_BUFFER_SAMPLES", "AUDIO_PROFILE_STABLE_WINDOW_MS", "SCREEN_LATENCY_MS", "CONTROLLER_FEATURES", "", "AUDIO_BITS", "Lkotlin/Pair;", "plistHeaders", "", "pairingHeaders", "encode", "value", "plist", "bytes", "number", "selectedViewArea", "display", "hasHighQualityMediaCapability", "", "info", "selectedSafeArea", "Lcom/carplay/sender/SenderSafeArea;", "area", "describeAudioBits", "mask", "describeViewAreas", "describeIapMessage", "data", "streams", "core-airplay"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final byte[] encode(Object value) {
            return BplistCodec.INSTANCE.encode(value);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final Map<?, ?> plist(byte[] bytes) {
            Object objDecode = BplistCodec.INSTANCE.decode(bytes);
            Map<?, ?> map = objDecode instanceof Map ? (Map) objDecode : null;
            if (map != null) {
                return map;
            }
            throw new IllegalStateException("车机返回无效 plist".toString());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int number(Object value) {
            Number number = value instanceof Number ? (Number) value : null;
            if (number != null) {
                return number.intValue();
            }
            throw new IllegalStateException("车机参数缺失".toString());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final Map<?, ?> selectedViewArea(Map<?, ?> display) {
            List areas;
            Object obj = display.get("viewAreas");
            Iterable iterable = obj instanceof List ? (List) obj : null;
            if (iterable != null) {
                Iterable $this$mapNotNull$iv = iterable;
                Collection destination$iv$iv = new ArrayList();
                for (Object element$iv$iv$iv : $this$mapNotNull$iv) {
                    Map map = element$iv$iv$iv instanceof Map ? (Map) element$iv$iv$iv : null;
                    if (map != null) {
                        destination$iv$iv.add(map);
                    }
                }
                areas = (List) destination$iv$iv;
            } else {
                areas = null;
            }
            if (areas == null) {
                areas = CollectionsKt.emptyList();
            }
            if (areas.isEmpty()) {
                return null;
            }
            Object obj2 = display.get("initialViewArea");
            Number number = obj2 instanceof Number ? (Number) obj2 : null;
            int index = number != null ? number.intValue() : 0;
            Map<?, ?> map2 = (Map) CollectionsKt.getOrNull(areas, index);
            return map2 == null ? (Map) CollectionsKt.first(areas) : map2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code duplicated, block: B:62:0x00e2  */
        public final boolean hasHighQualityMediaCapability(Map<?, ?> info) {
            Collection destination$iv$iv;
            boolean z;
            String string;
            String string2;
            Object obj = info.get("audioFormats");
            Iterable iterable = obj instanceof List ? (List) obj : null;
            if (iterable != null) {
                Iterable $this$mapNotNull$iv = iterable;
                Collection destination$iv$iv2 = new ArrayList();
                for (Object element$iv$iv$iv : $this$mapNotNull$iv) {
                    Map map = element$iv$iv$iv instanceof Map ? (Map) element$iv$iv$iv : null;
                    if (map != null) {
                        destination$iv$iv2.add(map);
                    }
                }
                destination$iv$iv = (List) destination$iv$iv2;
            } else {
                destination$iv$iv = null;
            }
            if (destination$iv$iv == null) {
                destination$iv$iv = CollectionsKt.emptyList();
            }
            Collection $this$any$iv = destination$iv$iv;
            if (($this$any$iv instanceof Collection) && $this$any$iv.isEmpty()) {
                return false;
            }
            for (Object element$iv : $this$any$iv) {
                Map it = (Map) element$iv;
                Object obj2 = it.get("type");
                Number number = obj2 instanceof Number ? (Number) obj2 : null;
                if (number != null && number.intValue() == 102) {
                    Object obj3 = it.get("audioType");
                    if ((obj3 == null || (string = obj3.toString()) == null || (string2 = StringsKt.trim((CharSequence) string).toString()) == null || !StringsKt.equals(string2, "media", true)) ? false : true) {
                        Object obj4 = it.get("audioOutputFormats");
                        Number number2 = obj4 instanceof Number ? (Number) obj4 : null;
                        if (((number2 != null ? number2.longValue() : 0L) & 8388608) != 0) {
                            z = true;
                        } else {
                            z = false;
                        }
                    } else {
                        z = false;
                    }
                } else {
                    z = false;
                }
                if (z) {
                    return true;
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final SenderSafeArea selectedSafeArea(Map<?, ?> display, Map<?, ?> area) {
            Object obj = area != null ? area.get("safeArea") : null;
            Map safe = obj instanceof Map ? (Map) obj : null;
            if (safe == null) {
                Object obj2 = display.get("safeArea");
                safe = obj2 instanceof Map ? (Map) obj2 : null;
                if (safe == null) {
                    return null;
                }
            }
            Object obj3 = area != null ? area.get("originXPixels") : null;
            Number number = obj3 instanceof Number ? (Number) obj3 : null;
            int areaX = number != null ? number.intValue() : 0;
            Object obj4 = area != null ? area.get("originYPixels") : null;
            Number number2 = obj4 instanceof Number ? (Number) obj4 : null;
            int areaY = number2 != null ? number2.intValue() : 0;
            Object obj5 = safe.get("originXPixels");
            Number number3 = obj5 instanceof Number ? (Number) obj5 : null;
            if (number3 == null) {
                return null;
            }
            int x = number3.intValue() + areaX;
            Object obj6 = safe.get("originYPixels");
            Number number4 = obj6 instanceof Number ? (Number) obj6 : null;
            if (number4 == null) {
                return null;
            }
            int y = number4.intValue() + areaY;
            Object obj7 = safe.get("widthPixels");
            Number number5 = obj7 instanceof Number ? (Number) obj7 : null;
            if (number5 == null) {
                return null;
            }
            int width = number5.intValue();
            Object obj8 = safe.get("heightPixels");
            Number number6 = obj8 instanceof Number ? (Number) obj8 : null;
            if (number6 == null) {
                return null;
            }
            int height = number6.intValue();
            Object obj9 = display.get("widthPixels");
            Number number7 = obj9 instanceof Number ? (Number) obj9 : null;
            if (number7 == null) {
                return null;
            }
            int displayWidth = number7.intValue();
            Object obj10 = display.get("heightPixels");
            Number number8 = obj10 instanceof Number ? (Number) obj10 : null;
            if (number8 == null) {
                return null;
            }
            int displayHeight = number8.intValue();
            if (x < 0 || y < 0 || width <= 0 || height <= 0 || ((long) x) + ((long) width) > displayWidth || ((long) y) + ((long) height) > displayHeight) {
                return null;
            }
            return new SenderSafeArea(x, y, width, height);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String describeAudioBits(long mask) {
            if (mask == 0) {
                return "空";
            }
            Iterable $this$mapNotNull$iv = AirPlaySender.AUDIO_BITS;
            Collection destination$iv$iv = new ArrayList();
            for (Object element$iv$iv$iv : $this$mapNotNull$iv) {
                Pair pair = (Pair) element$iv$iv$iv;
                long bit = ((Number) pair.component1()).longValue();
                String name = (String) pair.component2();
                if ((mask & bit) == 0) {
                    name = null;
                }
                if (name != null) {
                    destination$iv$iv.add(name);
                }
            }
            List named = (List) destination$iv$iv;
            Iterable $this$fold$iv = AirPlaySender.AUDIO_BITS;
            long accumulator$iv = 0;
            for (Object element$iv : $this$fold$iv) {
                Pair entry = (Pair) element$iv;
                long acc = accumulator$iv;
                accumulator$iv = acc | ((Number) entry.getFirst()).longValue();
            }
            long unknown = mask & (~accumulator$iv);
            List $this$describeAudioBits_u24lambda_u245 = CollectionsKt.createListBuilder();
            $this$describeAudioBits_u24lambda_u245.addAll(named);
            if (unknown != 0) {
                String string = Long.toString(unknown, CharsKt.checkRadix(16));
                Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                $this$describeAudioBits_u24lambda_u245.add("未知位 0x" + string);
            }
            return CollectionsKt.joinToString$default(CollectionsKt.build($this$describeAudioBits_u24lambda_u245), "/", null, null, 0, null, null, 62, null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String describeViewAreas(Map<?, ?> display) {
            Object obj = display.get("viewAreas");
            List areas = null;
            Iterable iterable = obj instanceof List ? (List) obj : null;
            if (iterable != null) {
                Iterable $this$mapNotNull$iv = iterable;
                Collection destination$iv$iv = new ArrayList();
                for (Object element$iv$iv$iv : $this$mapNotNull$iv) {
                    Map map = element$iv$iv$iv instanceof Map ? (Map) element$iv$iv$iv : null;
                    if (map != null) {
                        destination$iv$iv.add(map);
                    }
                }
                areas = (List) destination$iv$iv;
            }
            if (areas == null) {
                areas = CollectionsKt.emptyList();
            }
            return areas.isEmpty() ? "none" : CollectionsKt.joinToString$default(areas, ";", null, null, 0, null, new Function1() { // from class: com.carplay.sender.AirPlaySender$Companion$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj2) {
                    return AirPlaySender.Companion.describeViewAreas$lambda$8((Map) obj2);
                }
            }, 30, null);
        }

        /* JADX WARN: Code duplicated, block: B:16:0x0077  */
        /* JADX WARN: Code duplicated, block: B:18:0x007f  */
        /* JADX WARN: Code duplicated, block: B:22:0x0087  */
        /* JADX WARN: Instruction removed from duplicated block: B:16:0x0077, please report this as an issue */
        static final CharSequence describeViewAreas$lambda$8(Map area) {
            Object obj;
            String str;
            Intrinsics.checkNotNullParameter(area, "area");
            Object obj2 = area.get("safeArea");
            Map safe = obj2 instanceof Map ? (Map) obj2 : null;
            Object obj3 = area.get("originXPixels");
            if (obj3 == null) {
                obj3 = 0;
            }
            Object obj4 = area.get("originYPixels");
            if (obj4 == null) {
                obj4 = 0;
            }
            if (safe != null) {
                Map it = safe;
                str = " safe=" + it.get("widthPixels") + "x" + it.get("heightPixels") + Marker.ANY_NON_NULL_MARKER + it.get("originXPixels") + "," + it.get("originYPixels");
                if (str == null) {
                    obj = area.get("widthPixels");
                    if (obj == null) {
                        obj = "?";
                    }
                    Object obj5 = area.get("heightPixels");
                    str = " size=" + obj + "x" + (obj5 != null ? obj5 : "?");
                }
            } else {
                obj = area.get("widthPixels");
                if (obj == null) {
                    obj = "?";
                }
                Object obj6 = area.get("heightPixels");
                str = " size=" + obj + "x" + (obj6 != null ? obj6 : "?");
            }
            return "origin=" + obj3 + "," + obj4 + str;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final String describeIapMessage(byte[] data) {
            if (data.length == AirPlaySender.IAP2_MARKER.length && Arrays.equals(data, AirPlaySender.IAP2_MARKER)) {
                return "iAP2 链路标记";
            }
            if (data.length < 6 || data[0] != 64 || data[1] != 64) {
                return "非 CSM";
            }
            int length = ((data[2] & UByte.MAX_VALUE) << 8) | (data[3] & UByte.MAX_VALUE);
            int id = ((data[4] & UByte.MAX_VALUE) << 8) | (data[5] & UByte.MAX_VALUE);
            String string = Integer.toString(id, CharsKt.checkRadix(16));
            Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            return "CSM 0x" + StringsKt.padStart(string, 4, '0') + " len=" + length;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final List<Map<?, ?>> streams(byte[] bytes) {
            Object obj = plist(bytes).get("streams");
            Iterable iterable = obj instanceof List ? (List) obj : null;
            if (iterable == null) {
                throw new IllegalStateException("车机未返回流信息".toString());
            }
            Iterable $this$mapNotNull$iv = iterable;
            Collection destination$iv$iv = new ArrayList();
            for (Object element$iv$iv$iv : $this$mapNotNull$iv) {
                Map map = element$iv$iv$iv instanceof Map ? (Map) element$iv$iv$iv : null;
                if (map != null) {
                    destination$iv$iv.add(map);
                }
            }
            return (List) destination$iv$iv;
        }
    }
}
