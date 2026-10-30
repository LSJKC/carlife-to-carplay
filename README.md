# CarLink Cast

[![Latest release](https://img.shields.io/github/v/release/LSJKC/carlife-to-carplay?label=release&sort=semver)](https://github.com/LSJKC/carlife-to-carplay/releases/latest)
[![License](https://img.shields.io/github/license/LSJKC/carlife-to-carplay)](LICENSE)
[![Platform](https://img.shields.io/badge/Android-10%2B-3ddc84)](https://developer.android.com)
[![Language](https://img.shields.io/badge/Kotlin-%E2%9C%93-7f52ff)](https://kotlinlang.org)

让一台普通 Android 手机冒充 iPhone，把手机上的 **CarLife** 画面投到只支持 **CarPlay** 的车机上。

不需要越狱，不需要 MFi 认证硬件，不需要转接盒。

> **非官方项目。** 与 Apple Inc.、百度、荣耀及任何汽车制造商均无关联，未获授权，
> 不是 MFi 产品。CarPlay / AirPlay / CarLife 均为其各自权利人的商标。
> 仅限停车时配置与调试；行驶中的导航请以原厂系统为准。

---

## 下载

| 版本 | 说明 |
|---|---|
| **[最新版](https://github.com/LSJKC/carlife-to-carplay/releases/latest)** | 推荐。当前 1.3.21 |
| [全部版本](https://github.com/LSJKC/carlife-to-carplay/releases) | 1.3.0 起逐版 APK |
| [CHANGELOG](CHANGELOG.md) | 每个版本改了什么、为什么改 |

> ⚠️ **1.3.9 到 1.3.16 有一个会直接导致连不上的问题**，已在 1.3.17 修复。
> 如果装了这一段里的版本，请直接升到最新。

监测应用（独立 APK，用于排查「断连时谁先断开」）需要自行编译 `monitor` 模块。

---

## 它解决什么问题

很多车机只带 CarPlay，不带 CarLife；而安卓手机投屏到车机最成熟的方案偏偏是百度的
CarLife。两边各有一套完整实现，中间却不通。

本项目的做法是：

```
┌──────────── 手机（本应用） ────────────┐
│                                        │
│  ① CarLife 引擎（荣耀车联绘制车机桌面）   │
│         │  H.264 码流 + PCM 音频        │
│         ▼                              │
│  ② CarLife 通道（7240/8240/9240/…）     │
│         │                              │
│         ▼                              │
│  ③ 全画布合成器（可选）→ H.264 编码      │
│         │                              │
│         ▼                              │
│  ④ AirPlay 发送端 + iAP2 控制通道        │
│                                        │
└───────────────┬────────────────────────┘
                │ 无线 CarPlay
                ▼
          ⑤ 车机（CarPlay 接收端）
```

两边都用最成熟的实现，中间只做「冒充 iPhone」这一件事。

---

## 使用前提

1. **手机装有 CarLife**（荣耀机型为 `com.baidu.carlife.honor`，荣耀车联提供车机桌面）
2. **车机支持无线 CarPlay**
3. 手机与车机**已通过系统蓝牙配对**
4. Android 10 及以上

首次连接需要在车机上确认配对。之后打开应用、点连接即可。

---

## 功能状态

| 能力 | 状态 | 说明 |
|---|---|---|
| 无线 CarPlay 会话建立 | ✅ | iAP2 + AirPlay 全链路，蓝牙发起、Wi-Fi 承载 |
| CarLife 画面投屏 | ✅ | 全画布合成（安全区外填壁纸）或直通模式 |
| 音频 | ✅ | PCM type 100 直通 / AAC type 102 回退 |
| 触摸 | ✅ | 车机 HID → CarLife 9340 通道 |
| 内置壁纸 | ✅ | 9 张，按面板比例预裁 |
| 方向盘按键 | ⚠️ | 依赖车机 `buttonInfo`，尚未打通 |
| 媒体信息（歌名/歌手/封面） | ⚠️ | 0x5001 已实现，车机侧显示仍不稳定 |
| 长时间稳定会话 | ❌ | **见下方「已知问题」** |

---

## 已知问题

### 会话在 RECORD 后约 13 秒被车机断开

1.3.18 及以前的所有实测都稳定复现，跨度 76 毫秒：

| 版本 | RECORD → 断开 |
|---|---|
| 1.2.97 | 13055 ms |
| 1.3.1 | 13102 / 13131 ms |
| 1.3.2 | 13127 ms |
| 1.3.7 | 12969 ms |
| 1.3.17 | 12975 / 12960 ms |

**1.3.19 定位到根因并已修复**：Wi-Fi 命令通道握手时本程序声明了 3 个会话
（`1, 2, 12`），车机只声明 2 个。链路层照常 ACK，会话层则静默丢弃之后所有的
CSM——`0x1D00` 发出后 `0x1D01` 永不返回，10 秒后车机发 LINK-RESTART，13.1 秒
拆连接。改为与车机一致的会话表后，同一台车上的对照实现可以连续跑 975 秒无重启。

修复后的长稳表现以实车复测为准；本条在复测通过后移除。

其他已排除的方向：

- **不是 CarLife**：断开前 0.5 秒仍在正常出帧，7 个端口全部 ESTABLISHED
- **不是音频格式**：PCM / AAC、有无音频，断开时间一致
- **不是 iAP2 引导顺序**：事件驱动与轮询、回显与不回显，都试过

---

## 构建

要求：JDK 17+、Android SDK 34+。

```bash
git clone https://github.com/<you>/carlife-to-carplay.git
cd carlife-to-carplay
echo "sdk.dir=$ANDROID_SDK_ROOT" > local.properties
./gradlew :app:assembleDebug
```

产物在 `app/build/outputs/apk/debug/`。

**签名配置**：仓库不含任何 `.jks`。本地构建请自建密钥并在
`app/build.gradle.kts` 的 `signingConfigs` 指向它；直接 `assembleDebug`
会用默认调试密钥。

运行测试：

```bash
./gradlew test
```

当前 `core-iap2` / `core-airplay` / `adapter-carlife` / `monitor` 全部通过。

---

## 模块

| 模块 | 职责 |
|---|---|
| `app` | 应用外壳：服务、UI、设置、壁纸、触摸桥、媒体监听 |
| `core-airplay` | AirPlay 发送端：RTSP、SETUP/RECORD、视频与音频流、加密 |
| `core-iap2` | iAP2 协议：链路层（CSM 分帧、重传、检测包）、会话、身份认证 |
| `adapter-carlife` | CarLife 适配：协议编解码、7240 等端口、编码器协商、HID 注入 |
| `monitor` | 独立监测应用：只读采样 CarLife 进程与端口，判读「谁先断开」 |

`monitor` 是独立 APK（包名 `com.carlink.monitor`），不依赖主应用，主应用崩溃时
它仍在采样。

### 目录

```
carlink-cast/
├── app/                     应用外壳
│   └── src/main/java/com/carlink/cast/
│       ├── CastService.kt         前台服务，会话生命周期
│       ├── CastEngine.kt          一次投屏的编排
│       ├── AirPlayLink.kt         AirPlay 会话 + iAP2 控制通道
│       ├── BluetoothBootstrap.kt  蓝牙 iAP2 引导
│       ├── CarLifePanelCompositor.kt  全画布合成 + 二次编码
│       ├── MirrorSource.kt        屏幕镜像来源（另一条路径）
│       ├── NowPlayingPublisher.kt 媒体信息（0x5001）
│       ├── Iap2HidDispatcher.kt   车机 HID → CarLife
│       └── ui/                    协议内容视图、阶段环、指标
├── core-airplay/            AirPlay 发送端
│   └── sender/
│       ├── AirPlaySender.kt       主会话：SETUP、RECORD、视频与音频
│       ├── SenderNegotiation.kt   能力协商与音频候选
│       ├── VideoPackets.kt        128 字节头的封包格式
│       ├── SessionModes.kt        资源归属状态机
│       └── RtspWire.kt            RTSP 收发与加密
├── core-iap2/               iAP2 协议
│   ├── transport/                 链路层：CSM 分帧、重传、检测包
│   ├── session/                   会话与命令分发
│   └── controller/                身份认证、无线配置
├── adapter-carlife/         CarLife 适配
│   ├── CarLifeProtocol.kt         消息 id 与编解码
│   ├── CarLifeSession.kt          七个端口的会话
│   ├── CarLifeEncoderFormat.kt    98386/98311 编码器协商
│   └── CarLifeProjectionAdapter.kt 对外接口
└── monitor/                 独立监测应用
```

---

## 调试

### 应用内日志

主界面 → 导出。包含会话阶段、每条 iAP2 收发的字节、链路参数、编码器状态。

### 监测应用

```
adb install monitor/build/outputs/apk/debug/carlife-monitor-*.apk
```

打开后点「开始监测」，需要 root。它每秒采样 CarLife 的进程状态、7 个 TCP 端口、
AVC 编码器引用数与 fd 数量，并自动判读：

- 端口 `ESTAB → CLOSE_WT` = 对端先发 FIN，CarLife 是被动关的一方
- 进程变 `Z` = 已退出但父进程没回收
- 编码器引用归零 = 进程还在但不出画面

「导出到文件」写到 `下载/CarLifeMonitor/`，没有大小上限。

---

## 版本历史

见 [CHANGELOG.md](CHANGELOG.md)。当前版本 **1.3.21**。

---

## 许可

MIT。见 [LICENSE](LICENSE)。
