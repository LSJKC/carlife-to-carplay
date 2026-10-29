# CarLink Cast

让一台普通 Android 手机冒充 iPhone，把手机上的 **CarLife** 画面投到只支持 **CarPlay** 的车机上。

不需要越狱，不需要 MFi 认证硬件，不需要转接盒。

> **非官方项目。** 与 Apple Inc.、百度、荣耀及任何汽车制造商均无关联，未获授权，
> 不是 MFi 产品。CarPlay / AirPlay / CarLife 均为其各自权利人的商标。
> 仅限停车时配置与调试；行驶中的导航请以原厂系统为准。

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

在所有实测中都稳定复现，跨度 76 毫秒：

| 版本 | RECORD → 断开 |
|---|---|
| 1.2.97 | 13055 ms |
| 1.3.1 | 13102 / 13131 ms |
| 1.3.2 | 13127 ms |
| 1.3.7 | 12969 ms |
| 1.3.17 | 12975 / 12960 ms |

已排除的方向：

- **不是 CarLife**：断开前 0.5 秒仍在正常出帧，7 个端口全部 ESTABLISHED
- **不是音频格式**：PCM / AAC、有无音频，断开时间一致
- **不是 iAP2 引导顺序**：事件驱动与轮询、回显与不回显，都试过
- **不是本项目的实现**：独立实现
  [a-sender](https://github.com/sender/a-sender) 在同一台车上同样是 RECORD+13.1 秒被拆

结论：这是车机固件内部的定时器，不取决于发送端发什么。本项目在断开前已能完成
配对、拿到端点、建立视频与音频流；尚未实现断开后的无感重连。

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

见 [CHANGELOG.md](CHANGELOG.md)。当前版本 **1.3.17**。

---

## 参考

本项目的协议实现参考了以下公开资料与开源项目：

- [a-sender](https://github.com/sender/a-sender) — 同为 Android→CarPlay 发送端，
  本文档中的车机行为结论多来自它的实测记录与对照实验
- [UxPlay](https://github.com/FD-/RPiPlay) — AirPlay 接收端实现，用于校验收发格式
- iPhone `sysdiagnose` 抓包 — 真机在同型车机上的 SETUP 参数与端点性能基线

---

## 许可

MIT。见 [LICENSE](LICENSE)。
