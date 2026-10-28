# CarLife 运行状态监测

回答一个问题：**断连的时候，CarLife 还活着吗？**

1.3.2 的日志已经用内部事件回答了（断连前 0.5 秒 CarLife 仍在正常出帧），
但这个脚本从**系统侧**再验一遍，不依赖应用自己的日志——如果 CarLife 进程
被系统冻结、被 SELinux 拦、端口被 RST、编码器被抢占，这里看得见，应用
内部日志看不见。

## 用法

脚本是纯只读的：不写应用数据、不改权限、不装任何东西。

```bash
adb push tools/carlife-watch.sh /data/local/tmp/
adb shell su -c "sh /data/local/tmp/carlife-watch.sh 2 900" > carlife-watch.log
```

- 第一个参数：采样间隔秒数（默认 2）
- 第二个参数：采样轮数，`0` = 一直跑（默认 0）

900 轮 × 2 秒 = 30 分钟，足够覆盖好几次断连。Ctrl-C 停。

在手机上直接跑（有终端 App 时）：

```bash
su -c "sh /sdcard/carlife-watch.sh 2 900" > /sdcard/carlife-watch.log
```

## 每轮采什么

| 项 | 为什么 |
|---|---|
| 进程 pid/ppid/状态/RSS/线程数 | `Z` = 僵尸，`D` = 卡在 IO。CarLife 若是 `S` 且 CPU 时间在涨，它就是活的 |
| CPU 时间增量 | 断连前后对比：如果 CPU 归零，CarLife 停了；如果照涨，它在跑 |
| 7 个 TCP 端口状态 | 7240/8240/9240/9241/9242/9340/9440。`ESTAB` → `CLOSE_WT` 说明对端先关 |
| tx/rx 队列 | `/proc/net/tcp` 的 tx_queue:rx_queue。rx 堆积 = 我们在读但没处理 |
| 对端地址 | 断连前后对端是否还在同一个地址 |
| AVC 编码器引用数 | CarLife 出画面就要占 `c2.qti.avc.encoder`。归零 = 它停了 |
| SurfaceFlinger 图层数 | CarLife 的图层还在不在 |
| 音频焦点/模式 | 有没有别的东西抢了音源 |
| fd 数量 | 一直涨 = socket 泄漏 |

## 怎么判断

断连发生后，看该时刻前后两三轮：

- **CarLife 端口从 ESTAB 变成 CLOSE_WT** → 对端（车机/荣耀车联）先关，
  CarLife 只是被动跟随 → 是 CarPlay 侧的问题
- **CarLife 进程消失或变 Z** → CarLife 自己崩了
- **端口还在 ESTAB，但 rx_queue 持续堆积** → 数据到了没人读，CarLife 卡住
- **编码器引用归零、图层消失，但进程还在** → CarLife 活着但停了投屏
- **CPU 时间不再增长** → 进程在但没干活

如果是第一种，就和 1.3.2 应用日志的结论一致：**CarLife 是受害者，车机
主动拆的 AirPlay 连接**。
