package com.carlink.cast

import android.app.ActivityManager
import android.app.Application
import android.app.ApplicationExitInfo
import android.content.Context
import android.os.Build
import android.os.Process
import com.carplay.adapter.carlife.CarLifeBridge
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import kotlin.system.exitProcess

class CarLinkApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CastLog.initialize(this)
        MediaCommandRouter.initialize(this)
        initializeCarLifeMediaKeyDispatcher()
        installCrashHandler()
        recordPreviousProcessExit()
    }

    private fun initializeCarLifeMediaKeyDispatcher() {
        CarLifeBridge.mediaKeyDispatcher = { androidKeyCode ->
            MediaCommandRouter.dispatch(androidKeyCode)
        }
    }

    private fun installCrashHandler() {
        val previous = Thread.getDefaultUncaughtExceptionHandler()
        if (previous is CarLinkCrashHandler) return
        Thread.setDefaultUncaughtExceptionHandler(CarLinkCrashHandler(previous))
    }

    private fun recordPreviousProcessExit() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return
        runCatching {
            val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val exit = activityManager.getHistoricalProcessExitReasons(packageName, 0, 8)
                .maxByOrNull { it.timestamp } ?: return
            val preferences = getSharedPreferences("process-diagnostics", MODE_PRIVATE)
            if (exit.timestamp <= preferences.getLong("last-exit-timestamp", 0L)) return
            preferences.edit().putLong("last-exit-timestamp", exit.timestamp).commit()
            val details = JSONObject()
                .put("timestamp", exit.timestamp)
                .put("processName", exit.processName)
                .put("pid", exit.pid)
                .put("reason", exit.reason)
                .put("reasonLabel", reasonLabel(exit.reason))
                .put("status", exit.status)
                .put("importance", exit.importance)
                .put("pss", exit.pss)
                .put("rss", exit.rss)
                .put("description", exit.description.orEmpty())
            CastLog.recordProcessExit(details, readTrace(exit))
        }
    }

    private fun readTrace(exit: ApplicationExitInfo): ByteArray? {
        if (exit.reason != ApplicationExitInfo.REASON_ANR &&
            exit.reason != ApplicationExitInfo.REASON_CRASH_NATIVE
        ) return null
        return runCatching {
            val input = exit.traceInputStream ?: return null
            input.use {
                val output = ByteArrayOutputStream()
                val buffer = ByteArray(8192)
                var remaining = MAX_TRACE_BYTES
                while (remaining > 0) {
                    val count = it.read(buffer, 0, minOf(buffer.size, remaining))
                    if (count < 0) break
                    output.write(buffer, 0, count)
                    remaining -= count
                }
                output.toByteArray()
            }
        }.getOrNull()
    }

    private fun reasonLabel(reason: Int): String = when (reason) {
        ApplicationExitInfo.REASON_ANR -> "ANR 无响应"
        ApplicationExitInfo.REASON_CRASH -> "Java/Kotlin 崩溃"
        ApplicationExitInfo.REASON_CRASH_NATIVE -> "Native 崩溃"
        ApplicationExitInfo.REASON_LOW_MEMORY -> "系统内存不足"
        ApplicationExitInfo.REASON_EXCESSIVE_RESOURCE_USAGE -> "资源占用过高"
        ApplicationExitInfo.REASON_USER_REQUESTED -> "应用自行请求退出"
        ApplicationExitInfo.REASON_USER_STOPPED -> "用户从系统停止应用"
        ApplicationExitInfo.REASON_DEPENDENCY_DIED -> "依赖进程退出"
        ApplicationExitInfo.REASON_INITIALIZATION_FAILURE -> "进程初始化失败"
        ApplicationExitInfo.REASON_PERMISSION_CHANGE -> "权限变更后退出"
        ApplicationExitInfo.REASON_FREEZER -> "系统冻结后退出"
        else -> "其他原因 ($reason)"
    }

    private class CarLinkCrashHandler(
        private val previous: Thread.UncaughtExceptionHandler?,
    ) : Thread.UncaughtExceptionHandler {
        override fun uncaughtException(thread: Thread, throwable: Throwable) {
            runCatching { CastLog.recordCrash(thread, throwable) }
            if (previous != null) {
                previous.uncaughtException(thread, throwable)
            } else {
                Process.killProcess(Process.myPid())
                exitProcess(10)
            }
        }
    }

    private companion object {
        const val MAX_TRACE_BYTES = 2 * 1024 * 1024
    }
}
