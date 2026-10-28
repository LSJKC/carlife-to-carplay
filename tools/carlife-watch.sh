#!/system/bin/sh
# carlife-watch.sh — CarLife 运行状态监测（root，只读）
#
# 不写文件、不改权限、不动任何应用数据。每轮采一次样输出到 stdout，
# 由调用方决定存哪。
#
# 用法：
#   adb push tools/carlife-watch.sh /data/local/tmp/
#   adb shell su -c "sh /data/local/tmp/carlife-watch.sh 2 900" > watch.log
#
# 参数：$1 = 采样间隔秒（默认 2）
#       $2 = 采样轮数，0 = 一直跑（默认 0）

INTERVAL="${1:-2}"
COUNT="${2:-0}"

# 匹配 CarLife 相关进程：百度 CarLife 本体、荣耀车联宿主、本应用
PATTERN='baidu.carlife|hihonor.auto|carlink.cast'

# 读一个 pid 的状态。用 /proc/<pid>/stat 的字段位置，不依赖 ps 的列差异。
# stat 第二字段 comm 带括号，可能有空格，按最后一个 ')' 切。
one_proc() {
  pid="$1"
  [ -d "/proc/$pid" ] || return 0
  name=$(tr -d '\0' < "/proc/$pid/comm" 2>/dev/null)
  stat=$(cat "/proc/$pid/stat" 2>/dev/null)
  [ -n "$stat" ] || return 0
  rest=${stat##*') '}
  # 字段从 state 开始重排：1=state 2=ppid ... 12=utime 13=stime 18=num_threads 22=rss
  set -- $rest
  state="$1"; ppid="$2"
  utime="${12:-0}"; stime="${13:-0}"
  threads="${18:-0}"; rss="${22:-0}"
  cpu_ms=$(( (utime + stime) * 10 ))
  rss_kb=$(( rss * 4 ))
  echo "  pid=$pid ppid=$ppid name=$name state=$state cpu=${cpu_ms}ms rss=${rss_kb}KB threads=${threads}"
}

echo "# CarLife 监测开始 间隔=${INTERVAL}s 轮数=${COUNT} 时刻=$(date +%H:%M:%S)"

n=0
while :; do
  n=$((n + 1))
  now=$(date +%H:%M:%S)

  # ---- 1. 进程 ----
  pids=""
  for d in /proc/[0-9]*; do
    [ -r "$d/cmdline" ] || continue
    pid="${d#/proc/}"
    name=$(tr -d '\0' < "$d/comm" 2>/dev/null)
    cmd=$(tr '\0' ' ' < "$d/cmdline" 2>/dev/null)
    case "$name $cmd" in
      *baidu.carlife*|*hihonor.auto*|*carlink.cast*) pids="$pids $pid" ;;
    esac
  done

  echo "=== [$now] #$n 进程数=$(echo $pids | wc -w)"
  for pid in $pids; do one_proc "$pid"; done

  # ---- 2. CarLife 的 TCP 端口 ----
  # /proc/net/tcp{,6}: 第2列 local_addr:port(hex) 第3列 remote 第4列 state
  # 不用 strtonum（Android mawk 没有），手算十六进制。
  for f in /proc/net/tcp /proc/net/tcp6; do
    [ -r "$f" ] || continue
    awk -v f="$f" '
      function hexnum(s,   i, c, n) {
        n = 0; s = tolower(s)
        for (i = 1; i <= length(s); i++) {
          c = index("0123456789abcdef", substr(s, i, 1))
          if (c == 0) return -1
          n = n * 16 + (c - 1)
        }
        return n
      }
      function isport(p) {
        return p == 7240 || p == 8240 || p == 9240 || p == 9241 ||
               p == 9242 || p == 9340 || p == 9440
      }
      BEGIN {
        names["01"]="ESTAB";  names["02"]="SYN_SENT"; names["03"]="SYN_RECV"
        names["04"]="FIN_W1"; names["05"]="FIN_W2";   names["06"]="TIME_WT"
        names["07"]="CLOSE";  names["08"]="CLOSE_WT"; names["09"]="LAST_ACK"
        names["0A"]="LISTEN"; names["0B"]="CLOSING"
      }
      NR == 1 { next }
      {
        split($2, l, ":"); lp = hexnum(l[2])
        if (!isport(lp)) next
        st = toupper($4)
        # $5 形如 00000000:00000000:00:00000000 —— 只取真正的 tx_queue:rx_queue
        split($5, q, ":")
        printf "  tcp port=%s state=%s tx=%s rx=%s\n", lp, (names[st] ? names[st] : st), q[1], q[2]
      }' "$f" 2>/dev/null
  done

  # ---- 3. 编码器与图层：CarLife 是否真的在出画面 ----
  enc=$(dumpsys media.codec 2>/dev/null | grep -c "c2.qti.avc.encoder")
  layers=$(dumpsys SurfaceFlinger --list 2>/dev/null | grep -icE 'carlife|carlink|hihonor\.auto')
  echo "  画面: avc编码器引用=$enc CarLife图层=$layers"

  # ---- 4. 音频焦点 ----
  focus=$(dumpsys audio 2>/dev/null | grep -m1 "mode:" | tr -d '\r')
  echo "  音频: ${focus:-未知}"

  # ---- 5. fd 数量：持续上涨说明 socket 泄漏 ----
  for pid in $pids; do
    fd=$(ls "/proc/$pid/fd" 2>/dev/null | wc -l)
    echo "  pid=$pid fds=$fd"
  done

  echo

  if [ "$COUNT" != "0" ] && [ "$n" -ge "$COUNT" ]; then break; fi
  sleep "$INTERVAL"
done

echo "# 监测结束，共 $n 轮"
