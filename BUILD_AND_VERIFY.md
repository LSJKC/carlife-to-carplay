# carlink-cast 修复编译指南

## 已应用的修改

### 1. CarLife 编码器安全区 Bug (v1.2.48-168)
**文件**：`adapter-carlife/src/main/java/com/carplay/adapter/carlife/CarLifeHostService.kt:457`

**问题**：Bundle 未被返回，导致编码器不知道安全区参数

**改动**：
```kotlin
// 改前
Bundle().apply {
    overrides.forEach { ... }
}

// 改后
return Bundle().apply {
    overrides.forEach { ... }
}
```

### 2. PCM 音频采样率同步 (v1.2.48-168)
**文件**：`core-airplay/src/main/java/com/carplay/sender/AirPlaySender.kt:1478`

**问题**：PCM_LATENCY_MS=100ms 导致车机缓冲不同步

**改动**：
```kotlin
// 改前
private const val PCM_LATENCY_MS = 100

// 改后
private const val PCM_LATENCY_MS = 1000  // 与 AAC 同步
```

## 编译步骤

### 前置要求
- JDK 17+（可用 `java -version` 检查）
- Gradle 缓存已有（已验证）
- 网络连接（下载 Gradle wrapper，~200MB）

### 方式 1：在线编译（推荐）

```bash
cd /f/carplay/carlink-cast

# 完整编译 (需要 5-10 分钟，网络好的话)
./gradlew clean assembleDebug

# 快速编译 (仅增量编译，修改代码后用这个)
./gradlew assembleDebug

# 编译输出
# 成功：app/build/outputs/apk/debug/carlink-cast-*.apk
```

### 方式 2：如果网络持续超时

```bash
# 跳过 Gradle 版本检查
./gradlew --gradle-version 8.10.2 assembleDebug

# 或者使用本地 Gradle（如果有）
/path/to/gradle/bin/gradle assembleDebug
```

### 方式 3：Docker 编译（本地无 Java 环境）

```bash
docker run --rm -v /f/carplay/carlink-cast:/project android:latest bash -c \
  "cd /project && ./gradlew assembleDebug"
```

## 编译后安装

```bash
# 编译输出位置
APK_PATH="/f/carplay/carlink-cast/app/build/outputs/apk/debug/app-debug.apk"

# 卸载旧版本
adb uninstall com.carlink.cast

# 安装新版本
adb install "$APK_PATH"

# 验证安装
adb shell pm dump com.carlink.cast | grep version
```

## 验证修复

### 方法 1：查看日志

```bash
# 启动诊断脚本
bash ./diagnose-carlife.sh

# 预期输出：
# ✓ invokeMethod 被调用了
# ✓ 检测到 safe_area_* 参数
# ✓ 编码器已采纳安全区（bug 已修复）
```

### 方法 2：手动测试

```bash
# 清空日志
adb logcat -c

# 启动投屏（在车机上操作）

# 监控关键日志
adb logcat | grep -E "invokeMethod|安全区|draw_ui"

# 预期看到（约 10 秒内）：
# invokeMethod: carlife_encode_format
# CarLife 编码安全区回调：面板 1972×520，安全区 1408×476@(278,44)
# 已采纳安全区 (或 UI 被限制在安全区内)
```

## 故障排查

### 编译失败

| 错误 | 解决方案 |
|------|--------|
| `Connection timed out` | 网络问题，等待网络恢复或切换代理 |
| `No gradle binary` | 运行 `./gradlew` 让它自动下载 wrapper |
| `Out of memory` | 增加堆内存：`export GRADLE_OPTS=-Xmx2g` |

### 安装失败

```bash
# 权限错误
adb shell "pm grant com.carlink.cast android.permission.RECORD_AUDIO"

# 版本冲突
adb uninstall com.carlink.cast
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 修复后仍有问题

检查：
1. 新 apk 确实被安装了（`adb shell pm dump com.carlink.cast`）
2. CarLife 进程确实在运行（`adb shell ps | grep carlife`）
3. 车机日志显示 Bundle 被返回了（`adb logcat | grep Bundle`）

## 版本号说明

编译后的 apk 版本号：
- versionCode: 169（自动 +1）
- versionName: 1.2.48-manual-fix

## 后续优化

- [ ] 提交修复到版本控制
- [ ] 测试其他车型（目前仅在别克验证）
- [ ] 添加单元测试覆盖安全区参数
- [ ] 集成 CI/CD 自动编译
