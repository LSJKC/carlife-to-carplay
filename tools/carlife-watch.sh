#!/system/bin/sh
# carlife-watch.sh — CarLife 运行状态监测（root）
#
# 只读。不写文件、不改权限、不动任何应用数据。每 N 秒采一次样，
# 直接输出到 stdout，由调用方决定存哪。
#
# 用法：
#   adb shell su -c sh /data/local/tmp/carlife-watch.sh [间隔秒] [次数]
#   su -c sh /data/local/tmp/carlife-watch.sh 2 600 > /sdcard/watch.log
#
# 0 次 = 一直跑（Ctrl-C 停）。

INTERVAL="${1:-2}"
COUNT="${2:-0}"

# CarLife 相关进程的包名/关键字。荣耀车联宿主是 com.hihonor.auto，
# 百度 CarLife 本体是 com.baidu.carlife。
PATTERN='baidu.carlife|hihonor.auto|carlink'

hdr() {
  echo "# t=\$(date +%H:%M:%S) CarLife 监测开始"
  echo "# 间隔=${INTERVAL}s 次数=${COUNT:-inf}"
  echo "# 列: 时刻 pid ppid name cpu% rss_kb threads 状态"
}

# pid -> 一行进程状态。用 /proc 直接读，不依赖 ps 的列差异。
one_proc() {
  pid="$1"
  [ -d "/proc/$pid" ] || return
  name=$(tr -d '\0' < "/proc/$pid/comm" 2>/dev/null)
  stat=$(cat "/proc/$pid/stat" 2>/dev/null)
  # comm 里可能有空格和括号，从最后一个 ')' 之后切
  rest=${stat##*) }
  set -- $rest
  state="$1"; ppid="$2"
  utime=$12; stime=$13; threads=$18; rss=$22
  rss_kb=$((rss * 4))
  cpu_ms=$(( (utime + stime) * 10 ))
  echo "$pid $ppid $name ${cpu_ms}ms ${rss_kb}KB ${threads}T ${state}"
}

# 环形缓冲：保存上一轮的 CPU 时间，用来算增量
PREV_FILE=/data/local/tmp/.carlife_watch_prev
rm -f "$PREV_FILE"

hdr
n=0
while :; do
  n=$((n + 1))
  now=$(date +%H:%M:%S)

  # ---- 1. 进程是否存在、是否僵尸、CPU 是否在涨 ----
  pids=""
  for d in /proc/[0-9]*; do
    pid="${d#/proc/}"
    name=$(tr -d '\0' < "$d/comm" 2>/dev/null)
    cmd=$(tr -d '\0' < "$d/cmdline" 2>/dev/null)
    if echo "$name $cmd" | grep -qE "$PATTERN"; then
      pids="$pids $pid"
    fi
  done

  echo "=== [$now] #$n 进程数=$(echo $pids | wc -w)"
  for pid in $pids; do
    one_proc "$pid"
  done

  # ---- 2. CarLife 的 TCP 端口状态（7240/8240/9240/9241/9242/9340/9440）----
  # /proc/net/tcp{,6} 第 2 列 local_addr:port(hex) 第 4 列 state(01=ESTABLISHED)
  for f in /proc/net/tcp /proc/net/tcp6; do
    [ -r "$f" ] || continue
    awk -v f="$f" '
      NR == 1 { next }
      {
        split($2, l, ":"); split($3, r, ":")
        lp = strtonum("0x" l[2]); rp = strtonum("0x" r[2])
        if (!(lp ~ /^(7240|8240|9240|9241|9242|9340|9440)$/)) next
        st = $4
        names["01"]="ESTAB"; names["02"]="SYN_SENT"; names["03"]="SYN_RECV"
        names["04"]="FIN_W1"; names["05"]="FIN_W2"; names["06"]="TIME_WT"
        names["07"]="CLOSE"; names["08"]="CLOSE_WT"; names["09"]="LAST_ACK"
        names["0A"]="LISTEN"; names["0B"]="CLOSING"
        printf "  tcp port=%d state=%s txrx=%s:%s %s\n", lp, (names[st] ? names[st] : st), $5, $6, f
      }' "$f" 2>/dev/null
  done

  # ---- 3. CarLife 的网络连接对端 ----
  echo "  连接:"
  ss -tnp 2>/dev/null | grep -E ':(7240|8240|9240|9241|9242|9340|9440)\b' | sed 's/^/    /'

  # ---- 4. Surface / 编码器占用（CarLife 是否真的在出画面）----
  echo "  画面:"
  dumpsys media.player 2>/dev/null | grep -c . > /dev/null
  # 编码器实例数：CarLife 若挂了，c2.qti.avc.encoder 会被释放
  enc=$(dumpsys media.codec 2>/dev/null | grep -c "c2.qti.avc.encoder")
  echo "    avc编码器引用=$enc"
  # SurfaceFlinger 里 CarLife 的图层
  layers=$(dumpsys SurfaceFlinger --list 2>/dev/null | grep -icE 'carlife|carlink|hihonor.auto')
  echo "    CarLife相关图层=$layers"

  # ---- 5. 音频：CarLife 是否被系统静音/抢焦点 ----
  am=$(dumpsys audio 2>/dev/null | grep -E "mMusicActive|AudioFocus stack|mode:" | head -3 | tr '\n' ' ')
  echo "  音频: $am"

  # ---- 6. 进程的 fd 数量（socket 泄漏会让它一直涨）----
  for pid in $pids; do
    fd=$(ls "/proc/$pid/fd" 2>/dev/null | wc -l)
    echo "  pid=$pid fds=$fd"
  done

  echo

  [ "$COUNT" != "0" ] && [ "$n" -ge "$COUNT" ] && break
  sleep "$INTERVAL"
done

rm -f "$PREV_FILE"
echo "# 监测结束，共 $n 轮"
