package com.carlink.cast;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.bouncycastle.i18n.MessageBundle;

/* JADX INFO: compiled from: CastLog.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0013\b\u0086\u0081\u0002\u0018\u0000 \u00182\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0018B!\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012j\u0002\b\u0013j\u0002\b\u0014j\u0002\b\u0015j\u0002\b\u0016j\u0002\b\u0017¨\u0006\u0019"}, d2 = {"Lcom/carlink/cast/CastStage;", "", MessageBundle.TITLE_ENTRY, "", "detail", "step", "", "<init>", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;I)V", "getTitle", "()Ljava/lang/String;", "getDetail", "getStep", "()I", "BLUETOOTH", "IDENTIFY", "AUTHENTICATE", "NETWORK", "DISCOVER", "PAIR", "MEDIA", "STREAMING", "STOPPED", "FAILED", "Companion", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public enum CastStage {
    BLUETOOTH("连接车机蓝牙", "正在和车机建立蓝牙连接", 1),
    IDENTIFY("识别车机", "正在确认车机型号", 2),
    AUTHENTICATE("验证车机", "正在确认车机身份，防止连到假冒设备", 3),
    NETWORK("连接车机网络", "正在接入车机自己的 Wi-Fi", 4),
    DISCOVER("查找投屏服务", "正在车机网络上寻找投屏入口", 5),
    PAIR("建立安全连接", "正在和车机建立加密连接", 6),
    MEDIA("准备画面", "正在按车机屏幕尺寸准备画面", 7),
    STREAMING("正在投屏", "画面已经投到车机", 8),
    STOPPED("已停止", "投屏已结束", 0),
    FAILED("连接失败", "请点「开始投屏」重试，或查看导出日志", 0);

    private final String detail;
    private final int step;
    private final String title;
    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final List<CastStage> ladder = CollectionsKt.listOf((Object[]) new CastStage[]{BLUETOOTH, IDENTIFY, AUTHENTICATE, NETWORK, DISCOVER, PAIR, MEDIA, STREAMING});

    public static EnumEntries<CastStage> getEntries() {
        return $ENTRIES;
    }

    CastStage(String title, String detail, int step) {
        this.title = title;
        this.detail = detail;
        this.step = step;
    }

    public final String getDetail() {
        return this.detail;
    }

    public final int getStep() {
        return this.step;
    }

    public final String getTitle() {
        return this.title;
    }

    /* JADX INFO: compiled from: CastLog.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"Lcom/carlink/cast/CastStage$Companion;", "", "<init>", "()V", "ladder", "", "Lcom/carlink/cast/CastStage;", "getLadder", "()Ljava/util/List;", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final List<CastStage> getLadder() {
            return CastStage.ladder;
        }
    }
}
