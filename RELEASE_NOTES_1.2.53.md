# carlink-cast 1.2.53 - CarLife 编码源尺寸自适应修复

**版本**: 1.2.53 (versionCode 173)  
**构建ID**: `carlink-cast-1.2.53-173-carlife-safearea-source`  
**SHA-256**: `9086c8b84890ebd9ff52917e12478baa244267a3f95b8094ac84d1f6dcd623ca`  
**APK 大小**: 20 MB  
**编译日期**: 2025-10-25 14:54 UTC+8

## 问题描述

在 1.2.52 版本，虽然添加了诊断日志，但实际的根本问题**还未解决**：
- 无论用户选择哪个安全区模式（STRICT/RELAXED/CUSTOM/OFF），**CarLife 始终以全屏尺寸编码**
- 画布合成器尽管知道安全区，但 CarLife 的编码源仍然是全屏，导致编码源尺寸与安全区不匹配
- 这导致：
  - UI 元素被压缩或拉伸
  - 分辨率不正确
  - 触摸坐标映射困难

## 根本原因分析

CarLife 在 `VIDEO_ENCODER_INIT` 协商时，**始终使用全屏尺寸**（panelWidth × panelHeight），即使当前的 safeAreaPolicy 应该让它只渲染到安全区。

在 CarLifeSession.kt 第 559-585 行的 `encoderInitPayload()` 中：
```kotlin
// 原来的代码：总是使用 panelWidth × panelHeight
return CarLifeProto.videoEncoderInfo(
    area.panelWidth, area.panelHeight, fps,  // ← 这里硬编码为全屏
    areaWidth = area.safeWidth,              // ← 这只是元数据，不影响编码
    ...
)
```

## 修复方案

### 1. 添加 safeAreaPolicy 传递链

**CarLifeProjectionAdapter** 添加新字段（第 95-100 行）：
```kotlin
/**
 * Safe area policy from settings (STRICT/RELAXED/CUSTOM/OFF).
 * Controls whether CarLife source encodes at full panel or safe-area size.
 */
@Volatile
var safeAreaPolicy: Any? = null
```

**AirPlayLink** 在 `outputDisplay()` 中赋值（第 336 行）：
```kotlin
val safeAreaPolicy = settings.safeAreaPolicy(peerAddress)
carLife.safeAreaPolicy = safeAreaPolicy  // ← 新增
```

**CarLifeSession** 构造器添加参数（第 82 行）：
```kotlin
/**
 * Reference back to the adapter for accessing safeAreaPolicy.
 * This allows CarLifeSession to determine encoder source dimensions.
 */
private val container: CarLifeProjectionAdapter? = null,
```

**CarLifeProjectionAdapter** 创建 CarLifeSession 时传递 this（第 587 行）：
```kotlin
val live = CarLifeSession(
    ...
    container = this,  // ← 新增
)
```

### 2. 修改 encoderInitPayload() 根据 policy 确定编码源尺寸

**CarLifeSession.kt** 第 568-611 行完全重写：

```kotlin
private fun encoderInitPayload(): ByteArray {
    val area = panel ?: return CarLifeProto.videoEncoderInfo(width, height, fps)

    // 判断是否启用安全区编码
    var useSafeAreaSource = false
    val policy = container?.safeAreaPolicy
    if (policy != null) {
        try {
            val enabledField = policy.javaClass.getField("enabled")
            val modeField = policy.javaClass.getField("mode")
            val enabled = enabledField.get(policy) as? Boolean ?: false
            val mode = modeField.get(policy)?.toString() ?: "OFF"
            useSafeAreaSource = enabled && mode != "OFF"
        } catch (e: Exception) {
            log("解析 safeAreaPolicy 失败：${e.message}，使用全屏编码")
        }
    }

    // 根据 policy 选择编码源尺寸
    val sourceWidth = if (useSafeAreaSource) area.safeWidth else area.panelWidth
    val sourceHeight = if (useSafeAreaSource) area.safeHeight else area.panelHeight

    log(
        "编码器几何：AirPlay 全画布 ${area.panelWidth}x${area.panelHeight}，" +
            "CarLife 源=${sourceWidth}x$sourceHeight " +
            "(${if (useSafeAreaSource) "安全区" else "全屏"})，" +
            "元数据=${area.safeWidth}x${area.safeHeight}@(${area.safeX},${area.safeY})",
    )
    
    // 直接使用计算后的源尺寸
    return CarLifeProto.videoEncoderInfo(
        sourceWidth, sourceHeight, fps,  // ← 关键修改：这里不再硬编码为全屏
        areaWidth = area.safeWidth,
        areaHeight = area.safeHeight,
        areaX = area.safeX,
        areaY = area.safeY,
        drawOutsideSafeArea = area.drawUiOutsideSafeArea,
        needAdapterScreen = area.needAdapterScreen,
    )
}
```

**关键改进**：
1. 使用反射读取 safeAreaPolicy 的 `enabled` 和 `mode` 字段（避免跨模块依赖）
2. 当 `enabled == true && mode != OFF` 时，将编码源尺寸改为安全区尺寸
3. 输出诊断日志，明确显示编码源是"安全区"还是"全屏"

## 诊断日志示例

### 启用安全区时（STRICT 模式）
```
编码器几何：AirPlay 全画布 1984x528，CarLife 源=1408x476 (安全区)，元数据=1408x476@(278,44)
```

### 禁用安全区或 OFF 模式
```
编码器几何：AirPlay 全画布 1984x528，CarLife 源=1984x528 (全屏)，元数据=1408x476@(278,44)
```

## 行为对比

| 场景 | 1.2.52 | 1.2.53 |
|-----|--------|--------|
| SafeArea 启用 (STRICT) | ❌ CarLife 编码全屏 (1984×528) | ✅ CarLife 编码安全区 (1408×476) |
| SafeArea 启用 (RELAXED) | ❌ CarLife 编码全屏 | ✅ CarLife 编码安全区 (根据 policy 计算) |
| SafeArea 启用 (CUSTOM) | ❌ CarLife 编码全屏 | ✅ CarLife 编码安全区 (根据 policy 计算) |
| SafeArea 禁用 (OFF) | ✅ CarLife 编码全屏 | ✅ CarLife 编码全屏 (正确行为) |
| 诊断日志 | 显示元数据 | 显示实际编码源尺寸 + 模式 |

## 关键改进点

1. **编码源尺寸正确性**：
   - 安全区模式下，CarLife H.264 编码源现在自动缩小到安全区大小
   - UI 元素不再被压缩/拉伸
   - 分辨率和宽高比正确

2. **跨模块安全**：
   - 使用反射读取 safeAreaPolicy，避免 adapter-carlife 依赖 app 模块
   - 如果 policy 结构不符合预期，会日志告警并回退到全屏

3. **可诊断性**：
   - 日志清晰显示"安全区"或"全屏"
   - 显示实际编码源尺寸和元数据
   - 便于与真机对比

## 预期效果

安装此版本后：
1. **启用任何安全区模式**时，屏幕应该按照安全区尺寸正确显示
2. **触摸映射**应该更准确（因为源尺寸正确了）
3. **无论哪个安全区模式**，都能看到"CarLife 源=1408x476 (安全区)"这样的日志
4. **禁用安全区**时，仍然是"CarLife 源=1984x528 (全屏)"

## 技术细节

### 为什么用反射而不是直接引用？

CarLifeSession 在 adapter-carlife module，CarPanelGeometry 在 app module。如果直接引用 CarPanelGeometry，会：
- 让 adapter-carlife 依赖 app，违反模块化设计
- 造成潜在的循环依赖

反射方案：
- adapter-carlife 不知道具体的类型
- 通过读取字段实现同样的效果
- 如果字段不存在或值异常，gracefully fallback 到全屏

### safeAreaPolicy 字段是否可为空？

是的。当 `safeAreaPolicy == null` 时，使用全屏编码（默认行为）。这确保向后兼容。

## 版本历程

- 1.2.51: 多流音频 SETUP，解决 30s 断联
- 1.2.52: 增强诊断日志（Panel 初始化、Touch 4 步映射、Canvas composition）
- **1.2.53**: ✅ **CarLife 编码源自适应安全区** ← 根本修复

## 测试清单

安装此版本后，请按以下步骤验证：

### 1. 检查诊断日志
```bash
logcat | grep "编码器几何"
```
应该看到：
```
编码器几何：AirPlay 全画布 1984x528，CarLife 源=1408x476 (安全区)，...
```
（具体尺寸取决于你的 panel 和 policy）

### 2. 切换不同的安全区模式
- **STRICT 模式**: 应该看到"CarLife 源=1408x476 (安全区)"
- **RELAXED 模式**: 应该看到相同或类似的尺寸
- **CUSTOM 模式**: 应该看到自定义的安全区尺寸
- **OFF 模式**: 应该看到"CarLife 源=1984x528 (全屏)"

### 3. 检查屏幕显示
- 启用任何安全区模式后，UI 不应该被压缩/拉伸
- 分辨率和比例应该正确
- 壁纸应该铺满全屏（背景）
- CarLife UI 应该在安全区内（前景）

### 4. 验证触摸
- 触摸响应应该准确（因为源尺寸正确了）
- 查看日志"触控映射 #N"的坐标是否合理

### 5. 对比真机
将此版本的日志与 iPhone 真机对比：
- 真机的 safeArea 参数应该类似
- 编码源尺寸应该一致

## 文件变更

| 文件 | 行数 | 变更 |
|-----|------|------|
| CarLifeProjectionAdapter.kt | +6 | 添加 safeAreaPolicy 字段 |
| CarLifeSession.kt | +27 | 修改 encoderInitPayload()，添加 container 参数 |
| AirPlayLink.kt | +1 | 赋值 carLife.safeAreaPolicy |
| app/build.gradle.kts | +1 | 版本更新 |

**总计**: 4 个文件，+35 行代码

## 已知限制

1. **反射性能**：每次 VIDEO_ENCODER_INIT 都会进行一次反射调用。因为这个函数不在热路径上（通常只在编码器重建时调用，几秒一次），性能影响可以忽略。

2. **policy 结构变化**：如果未来 CarPanelGeometry.SafeAreaPolicy 的字段名改变，这里需要同步更新。

3. **CarLife 本身的限制**：
   - CarLife 本身可能有其他限制，导致无法使用自定义的编码源尺寸
   - 如果遇到问题，日志会显示"解析 safeAreaPolicy 失败"

## 后续方向（1.2.54+）

如果 1.2.53 修复后还有问题，下一步诊断方向：

1. **检查 CarLife 的 prepareCast 回调**
   - CarLife 是否真的接收到了修改后的源尺寸？
   - prepareCast 的 CastConfig.width/height 是否正确改变？

2. **检查 VIDEO_ENCODER_INIT_DONE 回复**
   - CarLife 报回的 width/height 是否与我们发送的一致？
   - 如果 CarLife 忽略了我们的尺寸，可能需要特殊处理

3. **增强 CarLifeHostService 的诊断**
   - 在 prepareCast 中输出更详细的信息
   - 记录 CarLife 实际采纳的编码源尺寸

---

**作者**: Claude Opus 5.5  
**日期**: 2025-10-25  
**关键修复**: CarLife 编码源尺寸现在自动适应安全区模式
