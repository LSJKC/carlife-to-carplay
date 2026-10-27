package com.carlink.cast;

import android.app.ActivityManager;
import android.app.Application;
import android.app.ApplicationExitInfo;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Process;
import androidx.core.app.NotificationCompat;
import com.carplay.adapter.carlife.CarLifeBridge;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONObject;

/* JADX INFO: compiled from: CarLinkApplication.kt */
/* JADX INFO: loaded from: classes15.dex */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u0000 \u00122\u00020\u0001:\u0002\u0011\u0012B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\u0006\u001a\u00020\u0005H\u0002J\b\u0010\u0007\u001a\u00020\u0005H\u0002J\b\u0010\b\u001a\u00020\u0005H\u0002J\u0012\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0002¨\u0006\u0013"}, d2 = {"Lcom/carlink/cast/CarLinkApplication;", "Landroid/app/Application;", "<init>", "()V", "onCreate", "", "initializeCarLifeMediaKeyDispatcher", "installCrashHandler", "recordPreviousProcessExit", "readTrace", "", "exit", "Landroid/app/ApplicationExitInfo;", "reasonLabel", "", "reason", "", "CarLinkCrashHandler", "Companion", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class CarLinkApplication extends Application {
    private static final Companion Companion = new Companion(null);

    @Deprecated
    public static final int MAX_TRACE_BYTES = 2097152;

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        CastLog.INSTANCE.initialize(this);
        MediaCommandRouter.INSTANCE.initialize(this);
        initializeCarLifeMediaKeyDispatcher();
        installCrashHandler();
        recordPreviousProcessExit();
    }

    private final void initializeCarLifeMediaKeyDispatcher() {
        CarLifeBridge.INSTANCE.setMediaKeyDispatcher(new Function1() { // from class: com.carlink.cast.CarLinkApplication$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(MediaCommandRouter.INSTANCE.dispatch(((Integer) obj).intValue()));
            }
        });
    }

    private final void installCrashHandler() {
        Thread.UncaughtExceptionHandler previous = Thread.getDefaultUncaughtExceptionHandler();
        if (previous instanceof CarLinkCrashHandler) {
            return;
        }
        Thread.setDefaultUncaughtExceptionHandler(new CarLinkCrashHandler(previous));
    }

    private final void recordPreviousProcessExit() {
        Object next;
        if (Build.VERSION.SDK_INT < 30) {
            return;
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLinkApplication carLinkApplication = this;
            Object systemService = carLinkApplication.getSystemService("activity");
            Intrinsics.checkNotNull(systemService, "null cannot be cast to non-null type android.app.ActivityManager");
            Iterable historicalProcessExitReasons = ((ActivityManager) systemService).getHistoricalProcessExitReasons(carLinkApplication.getPackageName(), 0, 8);
            Intrinsics.checkNotNullExpressionValue(historicalProcessExitReasons, "getHistoricalProcessExitReasons(...)");
            Iterator it = historicalProcessExitReasons.iterator();
            if (it.hasNext()) {
                next = it.next();
                if (it.hasNext()) {
                    long timestamp = ((ApplicationExitInfo) next).getTimestamp();
                    do {
                        Object next2 = it.next();
                        long timestamp2 = ((ApplicationExitInfo) next2).getTimestamp();
                        if (timestamp < timestamp2) {
                            next = next2;
                            timestamp = timestamp2;
                        }
                    } while (it.hasNext());
                }
            } else {
                next = null;
            }
            ApplicationExitInfo applicationExitInfo = (ApplicationExitInfo) next;
            if (applicationExitInfo == null) {
                return;
            }
            SharedPreferences sharedPreferences = carLinkApplication.getSharedPreferences("process-diagnostics", 0);
            if (applicationExitInfo.getTimestamp() <= sharedPreferences.getLong("last-exit-timestamp", 0L)) {
                return;
            }
            sharedPreferences.edit().putLong("last-exit-timestamp", applicationExitInfo.getTimestamp()).commit();
            JSONObject jSONObjectPut = new JSONObject().put("timestamp", applicationExitInfo.getTimestamp()).put("processName", applicationExitInfo.getProcessName()).put("pid", applicationExitInfo.getPid()).put("reason", applicationExitInfo.getReason()).put("reasonLabel", carLinkApplication.reasonLabel(applicationExitInfo.getReason())).put(NotificationCompat.CATEGORY_STATUS, applicationExitInfo.getStatus()).put("importance", applicationExitInfo.getImportance()).put("pss", applicationExitInfo.getPss()).put("rss", applicationExitInfo.getRss());
            String description = applicationExitInfo.getDescription();
            if (description == null) {
                description = "";
            }
            JSONObject jSONObjectPut2 = jSONObjectPut.put("description", description);
            CastLog castLog = CastLog.INSTANCE;
            Intrinsics.checkNotNull(jSONObjectPut2);
            castLog.recordProcessExit(jSONObjectPut2, carLinkApplication.readTrace(applicationExitInfo));
            Result.m159constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m159constructorimpl(ResultKt.createFailure(th));
        }
    }

    private final byte[] readTrace(ApplicationExitInfo exit) {
        Object objM159constructorimpl;
        if (exit.getReason() != 6 && exit.getReason() != 5) {
            return null;
        }
        try {
            Result.Companion companion = Result.INSTANCE;
            CarLinkApplication carLinkApplication = this;
            InputStream traceInputStream = exit.getTraceInputStream();
            if (traceInputStream == null) {
                return null;
            }
            InputStream inputStream = traceInputStream;
            try {
                InputStream inputStream2 = inputStream;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bArr = new byte[8192];
                int i = 2097152;
                while (i > 0) {
                    int i2 = inputStream2.read(bArr, 0, Math.min(bArr.length, i));
                    if (i2 < 0) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, i2);
                    i -= i2;
                }
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                CloseableKt.closeFinally(inputStream, null);
                objM159constructorimpl = Result.m159constructorimpl(byteArray);
                return (byte[]) (Result.m165isFailureimpl(objM159constructorimpl) ? null : objM159constructorimpl);
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    CloseableKt.closeFinally(inputStream, th);
                    throw th2;
                }
            }
        } catch (Throwable th3) {
            Result.Companion companion2 = Result.INSTANCE;
            objM159constructorimpl = Result.m159constructorimpl(ResultKt.createFailure(th3));
        }
    }

    private final String reasonLabel(int reason) {
        switch (reason) {
            case 3:
                return "系统内存不足";
            case 4:
                return "Java/Kotlin 崩溃";
            case 5:
                return "Native 崩溃";
            case 6:
                return "ANR 无响应";
            case 7:
                return "进程初始化失败";
            case 8:
                return "权限变更后退出";
            case 9:
                return "资源占用过高";
            case 10:
                return "应用自行请求退出";
            case 11:
                return "用户从系统停止应用";
            case 12:
                return "依赖进程退出";
            case 13:
            default:
                return "其他原因 (" + reason + ")";
            case 14:
                return "系统冻结后退出";
        }
    }

    /* JADX INFO: compiled from: CarLinkApplication.kt */
    @Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0011\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0001¢\u0006\u0004\b\u0003\u0010\u0004J\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lcom/carlink/cast/CarLinkApplication$CarLinkCrashHandler;", "Ljava/lang/Thread$UncaughtExceptionHandler;", "previous", "<init>", "(Ljava/lang/Thread$UncaughtExceptionHandler;)V", "uncaughtException", "", "thread", "Ljava/lang/Thread;", "throwable", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class CarLinkCrashHandler implements Thread.UncaughtExceptionHandler {
        private final Thread.UncaughtExceptionHandler previous;

        public CarLinkCrashHandler(Thread.UncaughtExceptionHandler previous) {
            this.previous = previous;
        }

        @Override // java.lang.Thread.UncaughtExceptionHandler
        public void uncaughtException(Thread thread, Throwable throwable) {
            Intrinsics.checkNotNullParameter(thread, "thread");
            Intrinsics.checkNotNullParameter(throwable, "throwable");
            try {
                Result.Companion companion = Result.INSTANCE;
                CarLinkCrashHandler carLinkCrashHandler = this;
                CastLog.INSTANCE.recordCrash(thread, throwable);
                Result.m159constructorimpl(Unit.INSTANCE);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.INSTANCE;
                Result.m159constructorimpl(ResultKt.createFailure(th));
            }
            if (this.previous != null) {
                this.previous.uncaughtException(thread, throwable);
            } else {
                Process.killProcess(Process.myPid());
                System.exit(10);
                throw new RuntimeException("System.exit returned normally, while it was supposed to halt JVM.");
            }
        }
    }

    /* JADX INFO: compiled from: CarLinkApplication.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lcom/carlink/cast/CarLinkApplication$Companion;", "", "<init>", "()V", "MAX_TRACE_BYTES", "", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
