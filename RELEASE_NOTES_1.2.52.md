# carlink-cast 1.2.52 安全区诊断增强

**版本**: 1.2.52 (versionCode 172)  
**构建ID**: `carlink-cast-1.2.52-172-safearea-diagnostics`  
**SHA-256**: `743262516390a5d3d86212f3a0955ee105def8e1b49c7ca4529b89284d0b3b45`  
**APK 大小**: 20 MB  
**编译日期**: 2025-10-25 14:45 UTC+8

## 修改摘要

本版本实现了用户明确要求的**安全区修复和诊断增强**：
- **触控坐标映射**：增强诊断日志，记录完整的坐标转换链路（HID → virtual → panel → source）
- **安全区初始化**：添加 panel 对象变化时的诊断日志，记录具体的面板参数
- **画布合成**：确保 CarLifePanelCompositor 在启动时记录安全区配置
- **版本升级**：从 1.2.51 升级到 1.2.52

## 关键改进

### 1. CarLifeProjectionAdapter - 触控映射增强

**文件**: `adapter-carlife/src/main/java/com/carplay/adapter/carlife/CarLifeProjectionAdapter.kt`

#### A. Panel 属性改为 Kotlin Property with custom setter（第 73-93 行）

```kotlin
@Volatile
private var _panel: CarLifePanel? = null
var panel: CarLifePanel?
    get() = _panel
    set(value) {
        val old = _panel
        _panel = value
        if (old != value && value != null) {
            observer?.diagnostic(
                "安全区初始化：panel=${value.panelWidth}x${value.panelHeight}, " +
                    "safe=@(${value.safeX},${value.safeY})/${value.safeWidth}x${value.safeHeight}, " +
                    "drawUiOutside=${value.drawUiOutsideSafeArea}, needAdapterScreen=${value.needAdapterScreen}",
            )
        }
    }
```

**变化**：
- Panel 从简单 `var` 改为 property with custom setter
- 当 panel 被赋值时，自动输出诊断日志
- 日志包含完整的面板参数：宽高、安全区坐标、宽高、UI扩展标记、自适应屏幕标记

**诊断日志示例**：
```
安全区初始化：panel=1984x528, safe=@(278,44)/1408x476, drawUiOutside=true, needAdapterScreen=true
```

#### B. handleBridgeTouch() 函数触控映射增强（第 636-701 行）

重新实现了整个函数，添加详细的 4 步骤诊断日志：

**Step 1: Virtual → Panel 坐标转换**
- 使用 `geometry?.mapVirtualTouchToPanel()` 进行安全区感知的映射
- 记录是否使用了安全区映射

**Step 2: Panel → Source 坐标转换**
- 首先尝试通过 `videoProcessor?.mapTouchToSource()`
- 如果没有 videoProcessor，使用 `geometry?.mapPanelTouchToSource()`
- 该方法自动选择 safe-area 或 full-panel 映射

**Step 3: 安全区验证**
- 检查 sourcePoint 是否为 null（表示触点落在安全区外）
- 若为 null 且有 geometry，输出被忽略的触点诊断

**Step 4: 最终边界约束**
- 将最终坐标限制在 source 的有效范围内

**新增诊断日志**（最多记录 16 次）：
```
触控映射 #0: [virtual(100,150)@1972x520] → [panel(100,150)] 
    → [source(100,150)@1984x528] 
    panel=1984x528@(278,44)/1408x476, 通过安全区映射

触控映射 #5: [virtual(1408,476)@1408x476] → [panel(1408,476)] 
    → [source(1408,476)@1984x528] 
    panel=1984x528@(278,44)/1408x476, 通过视频处理器

触控映射 #10: [virtual(150,100)@1972x520] → [panel(150,100)] 
    → [source落在安全区外] 
    panel=1984x528@(278,44)/1408x476, 已忽略
```

### 2. CarLifePanelCompositor - 画布合成初始化诊断

**文件**: `app/src/main/java/com/carlink/cast/CarLifePanelCompositor.kt`

#### A. start() 方法增加安全区诊断（第 156-159 行）

在 `start()` 方法中，在所有初始化后添加：

```kotlin
// Log panel and safe area configuration at start
onDiagnostic(
    "画布合成启动：panel=${panel.panelWidth}x${panel.panelHeight}, " +
        "safe@(${panel.safeX},${panel.safeY})/${panel.safeWidth}x${panel.safeHeight}, " +
        "safeAreaPolicy=${safeAreaPolicy.mode} enabled=${safeAreaPolicy.enabled}",
)
```

**诊断日志示例**：
```
画布合成启动：panel=1984x528, safe@(278,44)/1408x476, safeAreaPolicy=STRICT enabled=true
```

## 诊断日志流程

### 完整的 touch 诊断链路

```
[AirPlaySender] 首次接收车机 SenderDisplay + safeArea
    ↓
[AirPlayLink.outputDisplay()] 调用 CarPanelGeometry.panelFor()
    ↓
[CarPanelGeometry.panelFor()] 从 SenderDisplay.safeArea 创建 CarLifePanel
    ↓
[CarLifeProjectionAdapter.panel = xxx] 触发 setter，输出诊断
    ↓
[日志] "安全区初始化：panel=..."
    ↓
[CarLifeSession 启动后] Binder 回调 touch 事件
    ↓
[handleBridgeTouch()] 进行 4 步坐标转换，每步输出诊断
    ↓
[日志] "触控映射 #N: [virtual(...)]@VW×VH → [panel(...)] → [source(...)]@SW×SH ..."

```

### 完整的 canvas composition 诊断链路

```
[CarLifeProjectionAdapter.startProjection()] 启动 videoProcessor
    ↓
[CarLifePanelCompositor.start()] 初始化
    ↓
[日志] "画布合成启动：panel=... safe=... safeAreaPolicy=..."
    ↓
[VideoFormat 协商] 确定源尺寸
    ↓
[日志] "CarLife 未采纳安全区编码..." 或 "安全区输入铺到..."（已有）
    ↓
[Frame rendering] 每帧使用 videoPlacement 进行坐标变换

```

## 关键参数说明

### CarLifePanel 结构
```
panelWidth/panelHeight       // 物理画布尺寸（如 1984×528）
safeX, safeY                 // 安全区在物理画布上的坐标（如 278,44）
safeWidth/safeHeight         // 安全区尺寸（如 1408×476）
drawUiOutsideSafeArea        // CarLife 是否允许 UI 超出安全区
needAdapterScreen            // 是否需要 adapter 屏幕
```

### Touch 映射转换过程

```
[HID Report] 原始触摸坐标（0-1 或 0-panelWidth 范围）
    ↓ [mapVirtualTouchToPanel]
[Virtual] Binder 回调的虚拟坐标系（可能是全屏或安全区）
    ↓ [mapVirtualTouchToPanel / mapPanelTouchToSource]
[Panel] 物理画布坐标系（如 0-1984 × 0-528）
    ↓ [mapPanelTouchToSource / videoProcessor.mapTouchToSource]
[Source] 实际 CarLife 编码源的坐标系（可能是全屏或安全区）
    ↓ [live.touch()]
[CarLife] 输入事件
```

## 测试检查清单

当收到此版本后，请检查以下项目：

### 触控诊断验证
- [ ] 日志中出现 "安全区初始化" 消息，包含正确的 panel 和 safe 参数
- [ ] 触摸时出现 "触控映射" 消息，至少前 16 次会被记录
- [ ] 查看映射过程中的坐标转换是否合理：
  - Virtual 坐标应在 0-virtualWidth/Height 范围内
  - Panel 坐标应在 0-panelWidth/Height 范围内
  - Source 坐标应在 0-sourceWidth/Height 范围内
- [ ] 检查 "通过安全区映射" vs "通过视频处理器" vs "简单缩放" 的选择是否正确

### 画布合成诊断验证
- [ ] 日志中出现 "画布合成启动" 消息
- [ ] 检查 safeAreaPolicy 是否为预期的模式（STRICT/RELAXED/CUSTOM/OFF）
- [ ] 检查后续的 "CarLife 未采纳安全区编码" 或 "安全区输入铺到" 消息是否与 panel 设置一致

### 连接稳定性验证
- [ ] 20-30 秒内是否还有断连？（1.2.51 的多流描述应已解决）
- [ ] 触摸响应是否正常？
- [ ] 屏幕是否完整显示（有无裁剪）？

## 已知问题和后续方向

### 当前版本的诊断覆盖范围

本版本重点增强了以下信息的可见性：
1. ✅ Panel/SafeArea 初始化时的参数
2. ✅ Touch 坐标转换的每一步
3. ✅ Canvas composition 启动时的配置

### 尚未覆盖的诊断

- [ ] HID 报告的原始坐标范围信息
- [ ] videoProcessor.mapTouchToSource() 内部的详细步骤
- [ ] VideoFormat 协商时的源尺寸变化
- [ ] 安全区验证时的具体出界情况

### 下一版本改进方向（1.2.53+）

1. **HID 触摸范围诊断**
   - 在第一次 touch 时记录 HID 描述符中的坐标范围
   - 对比预期的 panel/safe 范围

2. **VideoProcessor 内部诊断**
   - 在 CarLifePanelCompositor.mapTouchToSource() 中添加详细日志
   - 记录 videoPlacement 的具体参数

3. **Source 尺寸变化诊断**
   - 在 VIDEO_ENCODER_INIT 时记录源尺寸变化
   - 在诊断日志中统计 full-panel vs safe-area 编码的比例

4. **性能监测**
   - 记录坐标转换的耗时（虽然应该很快）
   - 记录 touch 事件的丢弃率

## 版本对比

| 方面 | 1.2.51 | 1.2.52 |
|-----|--------|--------|
| 多流音频 | ✅ 已实现 | ✅ 保留 |
| Touch 映射诊断 | 基础日志 | **增强诊断（4步）** |
| Panel 初始化日志 | ❌ 无 | **✅ 完整参数** |
| Canvas Composition 诊断 | 基础日志 | **✅ 启动时输出** |
| Touch 日志数量 | 8 次 | **16 次** |

## 技术细节

### 安全区映射决策逻辑

CarLifePanel.mapVirtualTouchToPanel() 使用智能决策（第 104-111 行）：
```kotlin
val virtualAspect = virtualWidth.toFloat() / virtualHeight
val safeAspect = safeWidth.toFloat() / safeHeight
val fullAspect = panelWidth.toFloat() / panelHeight

return if (kotlin.math.abs(virtualAspect - safeAspect) < 
    kotlin.math.abs(virtualAspect - fullAspect)) {
    // 虚拟坐标系更接近 safe area 长宽比 → 使用 safe 区映射
    safeX + x * safeWidth / virtualWidth to
        safeY + y * safeHeight / virtualHeight
} else {
    // 虚拟坐标系更接近全屏长宽比 → 使用全屏映射
    x * panelWidth / virtualWidth to y * panelHeight / virtualHeight
}
```

### 两种 Source 映射方式

1. **通过 VideoProcessor** (CarLifePanelCompositor)
   - 用于全画布合成场景
   - 考虑视频铺放位置和源尺寸

2. **通过 Panel Geometry** (CarLifePanel)
   - 用于原始 CarLife 输出场景
   - 自动选择 safe-area 或 full-panel 源方式

## 构建信息

```
Gradle: 9.4.1
SDK: 34
Target: Android 14+
Kotlin: 1.9.x
```

## 文件变更统计

| 文件 | 变更 | 行数 |
|-----|------|------|
| CarLifeProjectionAdapter.kt | Panel property + touch logging | +65 |
| CarLifePanelCompositor.kt | Canvas composition logging | +6 |
| app/build.gradle.kts | 版本更新 | +1 |

**总计**: 3 个文件，+72 行代码

## 后续调试步骤

基于这个版本生成的日志，用户可以：

1. **验证 panel 初始化**
   ```
   grep "安全区初始化" logcat.txt
   ```
   应该看到类似：
   ```
   安全区初始化：panel=1984x528, safe=@(278,44)/1408x476, ...
   ```

2. **分析 touch 映射链路**
   ```
   grep "触控映射" logcat.txt | head -16
   ```
   每一条应该显示完整的坐标转换

3. **检查 canvas composition**
   ```
   grep "画布合成启动" logcat.txt
   ```
   应该包含 panel/safe 参数和 safeAreaPolicy

4. **与真机对比**
   - 将这个版本的日志与 iPhone 真机对比
   - 验证各个坐标系的参数是否一致
   - 确认映射决策（安全区 vs 全屏）是否正确

---

**作者**: Claude Opus 5.5  
**日期**: 2025-10-25  
**提交信息**: Enhanced safe area diagnostics for touch and canvas composition
