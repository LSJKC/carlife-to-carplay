# carlink-cast 总清单

> **唯一事实来源。** 改代码前先看这里，改完把状态改掉。不靠记忆。
> 每条都附**依据**（真机抓包时间戳 / 我们日志行号 / 代码位置）。

最后更新：2026-11-01
文档版本：2.0（全面重构）

---

## 0. 背景与目标

**目标**：把手机上的 **CarLife** 画面/音频/数据，通过冒充 iPhone 推到只支持 **CarPlay** 的车机。

- 车机：2025 别克君越艾维亚，型号 **E2LB**，`BUICK_2874`，蓝牙 `EC:D9:09:3E:F8:0A`
- 面板：1972×520，安全区 1408×476 @ +278,+44；副屏 2194×700
- 手机：HONOR BKQ-AN90，Android 17
- 当前版本：**1.3.44**（`23e3067a`）

**参照物**：真 iPhone 在同一台车上的无线 CarPlay 会话
`sysdiagnose_2026.10.31_16-00-05+0800_iPhone-OS_iPhone_24A446`（用户实车抓取，14:58 本地时间）

---

## 1. 数据来源

| 用途 | 路径 | 说明 |
|---|---|---|
| 真机全量解包 | `D:/QQ/_work/sd1031/out/full.jsonl` | 13.5GB / 9,673,136 条 / 07:51:01–08:00:26 UTC |
| 会话窗口子集 | `D:/QQ/_work/sd1031/out/session.jsonl` | 94,693 条，10 个进程，07:58–07:59 |
| 我方日志 | `D:/QQ/_work/cc1104m` 等 | 1.3.44 实车 |
| 历史成功样本 | `D:/QQ/_work/cc1030b` | 1.3.19，**AAC 102 跑通的唯一记录** |
| 解析工具 | `D:/QQ/_work/ultool/unifiedlog_iterator.exe` | mandiant v0.7.0 |

---

## 2. 核心结论速查

| 问题 | 结论 | 依据 |
|---|---|---|
| 断连是不是音频格式导致 | **否**。AAC 与 PCM 都是 13.1s 断 | `cc1030b`(AAC) 13143ms / `cc1104m`(PCM) 13115ms |
| 断连根因 | **`controlPort` 拿到不用，RTP↔NTP 不同步 → 车机 `sampleTime` 卡死** | 见 §3 |
| 「跳回第 6 步」根因 | **Wi-Fi 命令通道另起 iAP2 会话，重跑识别/认证** | 见 §4 |
| 媒体卡一直「正在加载」 | **`0x5001` 包结构与真机不符（我们 140B 全量发，真机 15B 按需发）** | 见 §5 |
| 歌词 | **CarPlay 协议无歌词字段**，是 App 渲染进画面的 | 全量检索 12 条全为 Apple Music 缓存 |
| 车机首页媒体卡片 | 由车机自绘，数据源就是 iAP2 `0x5001` | `msgID=5001h` |
| 副屏 | `CarInstruments` 2194×700，端口 6031，**可推自定义画面** | 见 §6 |

---

## 3. 【P0】断连：13.1 秒拆链

### 3.1 现象

| 会话 | 音频 | RECORD → reset |
|---|---|---|
| `cc1104m` | PCM 开 | 13115ms |
| `cc1104l` | PCM 开 | 13107ms |
| `cc1104i` | PCM 开 | 13094ms |
| `cc1104j` | 关 | 30220ms |
| `cc1030b` | **AAC** | 13143ms |

**锚点是 `/info` → reset，跨 7 会话极差 158ms（13211–13369ms）。**

### 3.2 真机 vs 我们：音频 SETUP（决定性证据）

**真机 `07:58:22.808` AudioMain：**
```
Request: {
    "controlPort" : 57249,                 ← 我方本地控制端口
    "spf" : 1024,
    "audioFormat" : 8388608,               ← 0x800000 AAC-LC
    "supportsHighAccuracyTimestamps" : true,
    "audioType" : "media",
    "audioLatencyMs" : 1000,
}
Response: {
    "controlPort" : 6011,                  ← 车机分配
    "type" : 102,
    "dataPort" : 6000,
    "streamConnectionID" : 4079875535368112191,
}
```

**我们（`cc1030b`）：**
```
音频 SETUP 请求：type=102 audioType=media fmt=0x800000 spf=-
音频 SETUP 回包：type=102 keys=type,streamConnectionID,dataPort,controlPort
音频 SETUP 已接受：参考字段集（无 controlPort）
```

**缺 `spf`、缺 `supportsHighAccuracyTimestamps`、不发 `controlPort`。**

### 3.3 代码层：`controlPort` 拿到就丢

`core-airplay/src/main/java/com/carplay/sender/AirPlaySender.kt`

```kotlin
// :1511  取出车机回的 controlPort
val controlPort = (accepted["controlPort"] as? Number)?.toInt()?.and(0xffff) ?: 0

// :1561  拿到了，只打日志
if (controlPort in 1..65535) {
    events.diagnostic("车机回包带 controlPort=$controlPort，但本实现不发送 RTP↔NTP 同步包")
}

// :1807  startAudioSync(...) 完整实现存在 —— 全文件零调用
```

### 3.4 后果链

```
请求不带我方 controlPort
  → 车机不监听 6011（此前实测 sendto ECONNREFUSED）
  → 我们也不发 RTP↔NTP 同步包
  → 车机音频时钟无法对齐
  → sampleTime 卡在 48048（1.0s）
  → RECORD 后 13.1s 判定音频流无效 → 拆链
```

**对照**：
```
我们  12843 车机音频进度：sampleTime=48048（1.0s）
      17845 车机音频进度：sampleTime=48048（1.0s）    ← 不动

真机  07:58:24.479 → 07:58:42.618
      每 2 秒  AudioMain: Parsed feedback dictionary for stream type 102: sr = 47999.9…（持续增长）
```

### 3.5 为什么 1.2.90 摘掉了 `startAudioSync`

commit `959adea`，当时理由：
```
16029 音频同步包暂时发不出去（第 1 次）：sendto failed: ECONNREFUSED
车机在音频 SETUP 回包里给 controlPort，但从不监听。
参考实现在本车根本不发这些包（它的音频回包没有 controlPort，trace 里也没有同步流量）。
```

| 1.2.90 的判断 | 真机 `07:58` 实测 |
|---|---|
| 音频回包没有 `controlPort` | **有**：`controlPort: 6011` |
| trace 里没有同步流量 | **有**：每 2 秒一次 feedback |

**当时的依据不是这份真机抓包。** 但 `ECONNREFUSED` 是真的——因为**我们没先告知自己的端口**。

### 3.6 修复（必须成对，缺一无效）

| # | 事项 | 状态 | 优先级 |
|---|---|---|---|
| **N1** | 音频 SETUP 请求加入我方 `controlPort` | 待办 | **P0** |
| **N2** | 恢复 `startAudioSync()` 调用，向车机回的 `controlPort` 发 RTP↔NTP 同步包 | 待办 | **P0** |
| N3 | 请求补 `spf`（AAC 1024 / PCM 352） | 待办 | P0 |
| N4 | 请求补 `supportsHighAccuracyTimestamps: true` | 待办 | P0 |

**代码位置**：`AirPlaySender.kt` shape A 约 1435–1440 行，`startAudioSync` 在 1807。

---

## 4. 【P0】「跳回第 6 步」

### 4.1 现象（用户描述）

App 从「第 8 步推流完成」跳回「第 6 步验证/核对」，一直转圈，然后车机断开。

### 4.2 日志还原

`cc1104m`：
```
7667   RECORD 已完成                          ← 界面：推流成功（第 8 步）
7913   车机通过 Wi-Fi 命令通道发起 iAP2（第 1 次）
7955   Wi-Fi 控制通道已同步，独立验证车机       ← 界面跳回验证（第 6 步）
7979   0x1d00 发到 phone-command 通道
12983  命令通道 5 秒汇总：已发识别=0 已收CSM=0 identified=false
17882  命令通道 iAP2 认证未完成
18884  第 2 次 → 又跳一次
```

### 4.3 根因

**我们在 Wi-Fi 命令通道上另起了一条 iAP2 会话，重跑了整条识别/认证链。**

真机 `07:58:22.053`：
```
airplayd [0x1725], iAPChannel = 0          ← 明确声明：Wi-Fi 不承载 iAP2
```

真机识别**只做一次**：
```
07:58:22.644  Control session version = 2, starting Identification...
07:58:22.652  Identification state changed: 2      ← 8ms 完成
```

### 4.4 代码层：两套命令通道并存

| 实现 | 位置 | 状态 |
|---|---|---|
| `AirPlaySender.ensureCommandIap()` | `AirPlaySender.kt:305` | 活 |
| `AirPlayLink.iapControl()` | `AirPlayLink.kt:547` | 活 |

**两套都在建立 Wi-Fi iAP2 通道。**

另：`AirPlaySender.kt:782`
```kotlin
val iapReply: Map<*, *> = emptyMap<Any, Any>()
```
硬编码空 map → `SenderNegotiation.control(emptyMap)` 必定返回 `AUTHENTICATED_BLUETOOTH`
→ `type-130 SETUP`、`WIFI_DATASTREAM`、`WIFI_COMMAND` 几条分支**从来不可达**。
附近大段注释把它描述成「车机事实」，实际是硬编码死路。

### 4.5 修复

| # | 事项 | 状态 | 优先级 |
|---|---|---|---|
| **B1** | `iAPChannel=0` 时不建 Wi-Fi iAP2 会话；检测包只回显 | 待办 | **P0** |
| B2 | 识别/认证每会话只做一次 | 待办 | P0 |
| B3 | 合并两套命令通道实现，删除不可达分支 | 待办 | P1 |
| B4 | 修正 `AirPlaySender.kt:749-781` 误导性注释 | 待办 | P2 |

---

## 5. 【P1】媒体卡「正在加载」

### 5.1 数据源确认

**车机首页的媒体卡片由车机自己绘制**，数据源就是 iAP2 `0x5001`。

真机 `07:58:23.464`：
```
#Artwork File transfer ID sent  (type=2) msgID=5001h  msgLen=214
#PlaybackQueueList             (type=3) msgID=5001h  msgLen=15
```

### 5.2 包尺寸分布（真机会话内）

```
type=2  msgLen=15   × 28   ← 常态：只有封面 ID，无封面数据
type=3  msgLen=15   ×  4   ← 播放队列
type=2  msgLen=214  ×  2   ← 仅 2 次带真实封面
```

CSM 帧 = `0x4040(2) + msgLen(2) + msgID(4) + body`
`msgLen=15` → **body 只有 7 字节** = `group(0) { u8(26, artworkId) }`

**真机 26/28 次只发「封面 ID」一个字段。**

### 5.3 我们的包

```
NowPlaying 0x5001 frame=140B
  group(0): persistentId(8B) + title + duration + album + artist + artworkId
  group(1): state + position + queueIndex + queueLength + app + volume + 13/14 + title
```

`subscribeDefault()` 写死 `media=[1,4,6,12,26]` → **每次都发全部 5 个字段** → 140 字节。

### 5.4 对比表

| 项 | 真机 | 我们 | 差距 |
|---|---|---|---|
| 常态包 | 15 字节 CSM / 7 字节 body | **140 字节** | 大近 10 倍 |
| 发什么 | 只发封面 ID(26) | 全字段都发 | 过度发送 |
| 分包 | `type=2`/`type=3` 分开 | 一个包塞满两组 | 结构不同 |
| 封面数据 | 214B 仅 2 次，其余走 file transfer | 21062B 一次性传 | 时机不同 |
| `type=3` 队列 | 独立发包 ×4 | **无** | 缺 |
| 封面图 | **768×768 JPEG** | 128 | 偏小 |

### 5.5 代码自带的警告

`NowPlayingPublisher.kt:196`：
```kotlin
// CarPlay field 26 is ArtworkFileTransferIdentifier, not lyric text.
// Sending a string there makes the HU leave the media card in "loading"
// even though audio is alive.
```
**「车机把媒体卡留在 loading 状态」——正是当前现象。**

### 5.6 修复

| # | 事项 | 状态 | 优先级 |
|---|---|---|---|
| **Q4** | `0x5001` 按需分包，常态压到 7 字节 body | 待办 | **P1** |
| **Q5** | 补 `type=3` PlaybackQueueList 独立包 | 待办 | P1 |
| **Q6** | 封面走 file transfer，`0x5001` 只带 ID（`ARTWORK_NONE=0x80`） | 待办 | P1 |
| Q7 | 封面改 768×768 JPEG + `MIMEType` + `ArtworkIdentifier` | 待办 | P1 |
| Q8 | 补字段：Genre / Composer / Index / Disc / Liked / Banned / PlaybackRate | 待办 | P2 |

**代码位置**：`app/src/main/java/com/carlink/cast/NowPlayingPublisher.kt:192`（`encode`）、`:626`（`ARTWORK_NONE`）

### 5.7 真机 NowPlaying 完整字段（`07:58:51.526`）

```
kMRMediaRemoteNowPlayingInfoAlbum                 = "Harry Styles"
kMRMediaRemoteNowPlayingInfoArtist                = Falling
kMRMediaRemoteNowPlayingInfoTitle                 = "..."
kMRMediaRemoteNowPlayingInfoDuration              = 240
kMRMediaRemoteNowPlayingInfoElapsedTime           = 0
kMRMediaRemoteNowPlayingInfoPlaybackRate          = 0
kMRMediaRemoteNowPlayingInfoTimestamp             = "2026-10-31 07:58:51 +0000"
kMRMediaRemoteNowPlayingInfoArtworkDataWidth      = 768
kMRMediaRemoteNowPlayingInfoArtworkDataHeight     = 768
kMRMediaRemoteNowPlayingInfoArtworkIdentifier     = "e6688815504d179b/MRContentItemAnimatedArtworkFormatTall=1409093519"
kMRMediaRemoteNowPlayingInfoArtworkMIMEType       = "image/jpeg"
kMRMediaRemoteNowPlayingInfoContentItemIdentifier = "D7479E4B-EC7A-41C7-8EFF-E39C9EFCECBE"
```

---

## 6. 【P1】音频：格式与多流

### 6.1 AAC vs PCM —— 判断已反转

| 会话 | 格式 | `sampleTime` | 断连 |
|---|---|---|---|
| `cc1030b` | **AAC 102** | **0 → 435120（9.1s）持续增长** | 13143ms |
| `cc1104m` | PCM 100 | **卡在 48048（1.0s）** | 13115ms |

**AAC 能让车机真正播放。** 当前代码 PCM 优先、AAC 标记「已拒」，这个排序错了。

`cc1104i:9044`：`已拒=102|AAC...,100|PCM...` —— **两个都在「已拒」列表里，排序完全失效。**

### 6.2 真机四路音频（`07:58:22.5`）

```
07:58:22.555  AudioMain  → MainHighAudio/media, AAC, 48000Hz, Stereo, 1024spf
07:58:22.555  AudioAlt   → AltAudio/default, Opus, 48000Hz, Mono, 960spf
07:58:22.552  AudioAux   → AuxAudio/speechRecognition, Opus, 4800
07:58:22.290  OutOfBandAVVCAudio
07:58:22.674  AudioMain: Setting bitrate to 256000 (packet limit 5565 bits)
07:58:42.104  AudioAlt:  Setting bitrate to 96000  (packet limit 1920 bits)
07:58:22.254  jitter buffer 80ms, main high 1000ms
07:58:22.289  四条流全部 TimeStampSmoothing = Enabled
07:58:22.808  AudioMain: RFC2198 redundancy = Disabled
07:58:22.808  AudioMain: High-accuracy Timestamps = Disabled
```

**每路独立 SETUP、独立端口：**
```
AudioMain  → dataPort 6000, controlPort 6011
AudioAlt   → 端口 6003（07:58:42.163 建立，48.228 停、48.356 重启）
```

### 6.3 修复

| # | 事项 | 状态 | 优先级 |
|---|---|---|---|
| **A4** | 媒体音频改 AAC(102) + `audioLatencyMs=1000` | 待办 | P0 |
| A5 | 修复音频档位记忆（清空「已拒」列表） | 待办 | P0 |
| **A8** | 每路音频独立 SETUP + 独立端口（Main/Alt/Aux/OOB） | 待办 | P1 |
| A7' | 回包按 `streamType` 匹配，删掉「任意有 dataPort 的条目」兜底 | 待办 | P1 |
| C4 | 比特率 Main 256000 / Alt 96000 | 待办 | P2 |

**A7' 代码位置**：`AirPlaySender.kt:1489`
```kotlin
replyStreams.firstOrNull { number(it["type"]) == format.streamType }
    ?: replyStreams.firstOrNull { number(it["dataPort"]) in 1..65535 }   // ← 会把主音频写进辅助流端口
```

**1.2.90 摘掉 type-101 companion SETUP 的判断是对的**（真机 AudioAlt 确实是独立流、可反复停起），
要做的是按 A8 以「独立 SETUP」重新实现，而不是塞进主 SETUP。

---

## 7. 【P1】副屏（CarInstruments）

### 7.1 真机机制

```
07:58:22.344  didConnectIdentity:Car[3-5], is car display: YES, is cluster display: NO
07:58:22.361  FBSDisplayConfiguration; CarInstruments[4-5]; mode: "1097x350@2x 30Hz SDR"
07:58:22.053  clusterControlChannel = 0                  ← 控制通道关闭
07:58:22.062  Fetching Cluster Urls: [
                  "maps:/car/instrumentcluster/map",
                  "maps:/car/instrumentcluster/instructioncard",
                  "maps:/car/instrumentcluster" ]
07:58:22.319  using default showsInstruments: YES
07:58:22.320  Listening for cluster URL update notifications
```

- 副屏 = `CarInstruments`，**2194×700**，正是 `/info` 里我们忽略的第二个显示项
- 内容是 URL 驱动：`maps:/car/instrumentcluster/...`
- `clusterControlChannel = 0`：车机不支持命令切换副屏内容
- 用户看到「无导航时显示媒体」是 **iPhone Maps App 的渲染逻辑**

### 7.2 画面流

```
07:58:22.302  created screen stream 0xE123  (1972x520)  ← 主屏
07:58:22.304  created screen stream 0xC97A  (2194x700)  ← 副屏
07:58:22.304  resuming screen stream 0xC97A             ← 副屏先启动
07:58:22.406  Registering streamType AltScreen, connectionID 6968160955815185368
07:58:22.441  [0xC97A] remote screen port: 6031         ← 副屏端口
07:58:22.476  TCP → :6031 ESTABLISHED
07:58:23.542  Registering streamType MainScreen, connectionID 13164771601038066357
07:58:23.542  [0xE123] remote screen port: 6030         ← 主屏端口
07:58:23.543  TCP → :6030 ESTABLISHED
```

**`AltScreen(2194×700)` 比 `MainScreen(1972×520)` 早 1.1 秒建立。我们从没连过 6031。**

### 7.3 修复

| # | 事项 | 状态 | 优先级 |
|---|---|---|---|
| **A6** | 建立 AltScreen 流（端口 6031，2194×700） | 待办 | **P1** |
| P3 | 推 CarLife 副屏画面 or 自定义媒体卡到 AltScreen | 待办 | P1 |

---

## 8. 【P2】HID / 按键

### 8.1 真机

```
07:58:22.277  [0x16F3] Initializing restricted mode authorizations for CarPlay to 0x0
07:58:22.287  [0x16F3] Updating Restricted mode authorizations to 0x1
07:58:22.281  IOHIDUserDevice 0x1000AC8DA Start
07:58:22.285  IOHIDUserDevice 0x1000AC8DC Start
07:58:29.046  ### [0x16F3] Network HID report submitted
```

`supportedCommands`：`21(Like) / 22(Ban) / 24(SetTimeElapsed) / 25(Repeat) / 26(Shuffle)`

### 8.2 我们

```
20782  输入链路快照：iAP2-HID=未注册（0x6800 未到达）
```

**车机从没发 `0x6800 StartHID`** —— 按键全废。

代码层原因（代码审计）：`CarLifeProjectionAdapter.kt:830-837` 的硬键回调
**只打日志、无消费者**，注释却声称「经 Dispatch 路由」。

### 8.3 修复

| # | 事项 | 状态 | 优先级 |
|---|---|---|---|
| D1 | `0x6800` StartHID 注册 | 待办 | P2（依赖 §4 命令通道修复） |
| D2 | supportedCommands 21/22/24/25/26 对齐 | 待办 | P2 |
| D3 | 修 `CarLifeProjectionAdapter.kt:830` 硬键回调无消费者 | 待办 | P2 |

---

## 9. 【P2】导航

### 9.1 真机（`07:58:23`）

```
22.345  Receiving navigation ownership connection from pid 6073
23.129  Started server: com.apple.accessories.navigation
23.130  componentList:
          ComponentID=1 "Cluster"  MaxAfterManeuverRoadNameLength=40, MaxDestinationNameLength=40
          ComponentID=2 "CardView" MaxManeuverDescriptionLength=33
23.131  _attachAccessory AccName=Shim Navigation Route Guidance Accessory
23.181  accessoryNavigationStartRouteGuidance
```

### 9.2 CarPlay 导航需要的 18 个字段（真机实测）

```
ACCNav_RGUpdate_RouteGuidanceState                          (22)
ACCNav_RGUpdate_SourceSupportsRouteGuidance                 (18)
ACCNav_RGUpdate_SourceName                                  (10)
ACCNav_RGUpdate_DestinationName                             (180)
ACCNav_RGUpdate_DestinationTimeZoneOffsetMinutes            (184)
ACCNav_RGUpdate_EstimatedTimeOfArrival                      (176)
ACCNav_RGUpdate_DistanceRemaining                           (174)
ACCNav_RGUpdate_DistanceRemainingDisplayString              (174)
ACCNav_RGUpdate_DistanceRemainingDisplayUnits               (174)
ACCNav_RGUpdate_DistanceRemainingToNextManeuver             (168)
ACCNav_RGUpdate_DistanceRemainingToNextManeuverDisplayString
ACCNav_RGUpdate_DistanceRemainingToNextManeuverDisplayUnits
ACCNav_RGUpdate_RouteGuidanceManeuverCount                  (174)
ACCNav_RGUpdate_RouteGuidanceManeuverCurrentList            (170)
ACCNav_RGUpdate_LaneGuidanceShowing                         (184)
ACCNav_RGUpdate_LaneGuidanceCurrentIndex                    (176)
ACCNav_RGUpdate_RouteGuidanceBeingShownInApp                (184)
ACCNav_RGUpdate_ChargingStationInfoList                     (184)
```

**注意**：车机自身有 bug —— 264 次 `[#Navigation] ERROR: Unknown RouteGuidnaceUpdate InfoType 0`
（原文拼写 `RouteGuidnace`），车机发来的字段 0 iOS 不认识。

### 9.3 CarLife 侧

```
CarLifeProtocol.kt:81-83
  HU_DATA_SUBSCRIBE       = 0x18043
  HU_DATA_SUBSCRIBE_START = 0x18045   → moduleID 2 = media, moduleID 0 = 导航(turn-by-turn)
  HU_DATA_SUBSCRIBE_STOP  = 0x18046

dataSubscribeDone(moduleIds):
  val supported = id == 0 || id == 2    ← moduleID 0 已标 supported
```

`CarLifeSession.kt:508` 发 `listOf(0, 2)` —— **订阅了导航，但代码中没有任何解析**。

### 9.4 第三方实现也没做

反编译自述（`defpackage\O4.java`）：
```
导航数据发送尚未实现；仪表/地图未验证
车机请求导航更新，龟壳互联尚未发送导航数据，不代表仪表导航已可用
```

### 9.5 修复

| # | 事项 | 状态 | 优先级 |
|---|---|---|---|
| L1a | 抓 CarLife moduleID 0 的真实消息号与字段结构 | 待办 | P2（需实车开导航） |
| F1 | 导航 accessory + componentList（Cluster/CardView） | 待办 | P2 |
| F2 | `0x5200` StartRouteGuidanceUpdates + 18 字段 | 待办 | P2 |
| L1b | CarLife 导航数据 → CarPlay `ACCNav_*` 映射 | 待办 | P2 |

---

## 10. 【P3】Siri / 音量 / 电话

### 10.1 Siri（真机 `07:59:27.305`）

```
airplayd  Siri action: 1; saving timestamp 3236679949192, diff 1 ms
airplayd  Posting 'Endpoint_SiriRequestion' { Timestamp, ActivationSeed }
CarPlay   Received Siri prewarm action, sending accessory timestamp
```

麦克风输入设备 `Flicker`（`VAD [vhaw]`），`fixed input latency 0.62ms`。
**车机发起，手机回传 accessory timestamp 做时钟对齐。**

| # | 事项 | 状态 |
|---|---|---|
| G1 | Siri action + accessory timestamp | 待办 |

### 10.2 音量

```
07:58:22.254  No Initial Volume from receiver
07:58:22.254  initialVolume = -999.000000 dB          ← 哨兵值，车机不管音量
07:58:42.084  duck audio, DuckCommand_Duck, DuckVolume = -13.979400
```

**音量由车机硬件旋钮控制。** 导航播报时下发 `DuckCommand` 压低音乐。

| # | 事项 | 状态 |
|---|---|---|
| J1 | 处理 `DuckCommand`（导航播报压低音乐） | 待办 |

### 10.3 电话

```
07:58:22.455  addClientWithCapabilities: client=<ACCiAP2ShimServerClient>[bundId=com.apple.InCallService]
```
电话音频：`MainAudio/telephony PCM, 16000/24000 Hz, Mono`

| # | 事项 | 状态 |
|---|---|---|
| G2 | 电话（InCallService 接入） | 待办 |

---

## 11. 配对 → 连接 → 会话 全流程（真机实测）

### 11.1 蓝牙层（`07:58:13`）

```
13.096  pairing state: (STATE_IDLE)0
13.170  Received encryption refresh, status 0
13.171  classicDeviceRoleUpdated oldRole=1 newRole=0 central=1   ← 车机是 central
13.187  RecvConnectReq cid=17675 psm=1 addr=EC:D9:09:3E:F8:0A   ← 车机主动连 RFCOMM
13.187  Incoming SDP request from 7NkJPv                         ← 车机主动查 SDP
13.187  Found link key                                           ← 已配对
13.355  Security level 2: state ENCRYPTED                        ← 链路加密
```

### 11.2 协议识别

```
13.449  Created connection, connectionType: [Bluetooth]
13.618  Initialized protocol parser. Matching against 2 protocols...
13.618  Protocol 4 detected for endpoint!        ← iAP2 自动识别
13.765  Control session version = 2, starting Identification...
13.790  Identification state changed: 1 → 2 → 6  ← 172ms 走完，到状态 6
14.336  Auto-starting optional features...
14.336  No auto-start function for feature: 14 / 26
14.344  iapd accessory arrived (manager2)
```

**`state 6` 我们从来没达到过。**

### 11.3 feature 生命周期

```
17.705  Destroying feature: 0,1,11,13,14,16,20,26,8
17.705  Destroying optional features... / Destroying required features...
17.773  Creating required features...
17.802  Identified for 5 feature(s)! → 11, 13, 14, 26, 8
17.802  No checkIdentificationInfo function for feature: 27!
```

feature 编号：`0,1,6,8,11,12,13,14,16,17,20,26,27`（日志只有编号，无名称）

### 11.4 全部网络连接（`kernel` / `com.apple.xnu.net.tcp`）

```
07:58:21.154  en0 → :7000  TCP   控制
07:58:22.046  en0 → :5001  TCP   事件
07:58:22.053  en0 → :5020  UDP   keepAlive
07:58:22.476  en0 → :6031  TCP   AltScreen（副屏）★我们没连
07:58:22.807  en0 → :6000  UDP   主音频 ★我们拿不到 6000
07:58:23.543  en0 → :6030  TCP   MainScreen（主屏）
```

对照我们：7000 ✅ / 5001 ✅ / 5020 ✅ / 6030 ✅ / **6031 ❌** / **6000 端口不对 ❌**

---

## 12. 代码卫生（审计完成，HEAD `fe0a73e`）

### 12.1 完全不可达：50 个 core 主源文件 / 约 8536 行

**接收端整套（本应用只做发送端）：**
`AirPlaySession.kt` `CarPlayMediaEngine.kt` `AirPlayInfoPlist.kt` `AirPlayConfig.kt`
`AirPlayHid.kt` `PairSetup.kt` `PairVerify.kt` `PairingStore.kt` `MfiSapAuthSetup.kt`
`RtspMessage.kt` `ScreenStream.kt` `AudioStream.kt` `BufferedAudioStream.kt`
`IapTunnel.kt` `VideoSettingsChannel.kt` `VideoInCar.kt` `CarPlayClusterDisplay.kt`
`AudioPacketCapture.kt` `Bytes.kt` `Base64Compat.kt` `AirPlayIdentity.kt`
`AirPlayControlDiagnostics.kt` `AirPlayPeerDiagnostics.kt` `AirPlayTcpAccepted.kt`
`AirPlayDisplaySettings.kt` `AirPlayIapTunnelStream.kt` `SafeArea.kt`
`media/TouchLatencyProbe.kt` `network/TcpLiveness.kt`

**USB/有线整套：**
`IphoneUsbHost.kt` `Iap2UsbMuxHost.kt` `UsbMuxFrameBuffer.kt` `UsbReadQueuePolicy.kt`
`UsbRequestQueue.kt` `IphoneCarPlayConfiguration.kt` `Ch341DeviceMatcher.kt`
`I2cTransport.kt` `Iap2ControlDeadline.kt` `Iap2IdentificationClient.kt`
`Iap2WiredControlClient.kt` `Iap2WirelessControlClient.kt` `Iap2LocationClient.kt`
`Iap2VehicleStatus.kt` `mfi/Iap2MfiAuthenticationClient.kt` `mfi/MfiAuthenticationClient.kt`

**其他整文件死代码：**
`CarPlayDisplayScale.kt` `CarPlayMediaButton.kt` `CarPlayUiScale.kt` `CarPlaySize.kt`
`ClusterTurnCardOverlay.kt` `MicrophonePacketizer.kt`

### 12.2 高危险：注释与行为矛盾（13 条，3 条最要紧）

| 位置 | 问题 |
|---|---|
| `CarLifeSession.kt:54/417/446` | 注释说「永不发送 AUTHEN_RESULT/FEATURE_CONFIG」，实际两处都在发 |
| `CarLifeSession.kt:648 vs 667` | 同一函数两段互斥 KDoc：先说 PAUSE/START 是正确方式，后说「必须永不使用」 |
| `CarLifeProjectionAdapter.kt:830-837` | 注释称硬键经 Dispatch 路由，实际只打日志、无消费者 |
| `AirPlaySender.kt:782` | `iapReply = emptyMap` 硬编码死路，注释伪装成「车机事实」 |
| `AirPlaySender.kt:749-781` | 声称「车机永不发 type-130」，真实原因是 782 行写死空 map |
| `Iap2LinkChannel.kt:573-603` | KDoc 说「必须 responder」，代码实际 `allowOpenerSynchronization()` 当发起方 |

### 12.3 同一功能多套并存（17 组，最关键 2 组）

| 功能 | 两套实现 | 状态 |
|---|---|---|
| **iAP2 命令通道** | `AirPlaySender.ensureCommandIap()` vs `AirPlayLink.iapControl()` | **两套都活 —— §4 根因** |
| NowPlaying 发布 | `app/NowPlayingPublisher.kt` vs `adapter-carlife/CarLifeMediaPublisher.kt` | 两套都活，职责重叠 |
| 媒体键路由 | `Dispatch.kt` / `MediaCommandRouter.kt` / `TouchBridge.kt` | 三套都活 |
| AAC 优先策略 | `preferAacMedia` 硬编码 false | `true` 分支只有测试走 |

### 12.4 其他

- `@Deprecated` 全仓库仅 1 处（`PhoneBluetoothRecovery.kt:44 OBSERVED_FIRMWARE`）
- **连续 5 行以上被注释掉的代码块：0 个**
- 堆叠/失配 KDoc：11 处（4 处描述已删除函数）
- TODO/FIXME/XXX：0 条

### 12.5 死字段

- `AirPlaySender.kt:203` `audioSuppressed` — 全仓库零写入
- `AirPlaySender.kt:223` `audioSsrc` — RTP SSRC 写死 0，本字段未用
- `AirPlaySender.kt:211` `receiverFeedbackNanos` — 只写不读

---

## 13. 已撤回的错误判断（避免重蹈覆辙）

| 判断 | 撤回原因 |
|---|---|
| ~~「AAC 被拒」~~ | `cc1030b` 实测 AAC 的 `sampleTime` 增长到 9.1s，**AAC 是有效的** |
| ~~「音频改 UDP」~~ | 我们本来就是 `DatagramSocket`，是 UDP |
| ~~「歌词有独立协议通道」~~ | 全量检索 `lyric` 仅 12 条，全是 Apple Music 缓存 |
| ~~「13 秒从 RECORD 计时」~~ | 锚点是 `/info`，跨 7 会话极差 158ms |
| ~~「空屏也会断，与音频无关」~~ | 那份是蓝牙侧先失败，reset 是连带结果 |
| ~~「CarPlay 大区媒体卡片可以做」~~ | 那是 iPhone Dashboard widget scene，我们需要的是车机首页卡片 |
| ~~「先断蓝牙才能建命令通道」~~ | 真机 `0x1d00` 在 `+5088`，`handoff` 在 `+7170` |

---

## 14. 优先级汇总

### P0（断连 + 跳回第 6 步）
1. **N1** 音频 SETUP 请求加我方 `controlPort`
2. **N2** 恢复 `startAudioSync()`
3. N3/N4 补 `spf` + `supportsHighAccuracyTimestamps`
4. **A4** 媒体音频改 AAC 102 + latency 1000
5. A5 清空音频档位「已拒」记忆
6. **B1** `iAPChannel=0` 时不建 Wi-Fi iAP2
7. B2 识别/认证每会话只做一次

### P1（功能可用）
8. **Q4** `0x5001` 按需分包
9. **Q5** 补 `type=3` PlaybackQueueList
10. Q6/Q7 封面 file transfer + 768×768 JPEG
11. **A6** AltScreen 6031 副屏流
12. A8 四路音频独立 SETUP
13. A7' 删掉 dataPort 兜底匹配

### P2（补全功能）
14. D1/D2/D3 HID 按键
15. F1/F2/L1b 导航
16. G1 Siri / G2 电话 / J1 DuckCommand
17. B3 合并两套命令通道

### P3（代码卫生）
18. 删除 50 个不可达文件（约 8536 行）
19. 修正 13 处误导性注释
20. 清理死字段

---

## 15. 待验证 / 需实车配合

| # | 事项 | 需要 |
|---|---|---|
| V1 | CarLife moduleID 0 的真实消息号与字段 | 实车启动 CarLife 导航后抓 7240 端口 |
| V2 | `controlPort` 成对修复后断连是否消失 | 装包实车验证 |
| V3 | AltScreen 6031 推送画面车机是否接受 | 装包实车验证 |
| V4 | 音频 SETUP 改 AAC 后 `sampleTime` 是否增长 | 装包实车验证 |
