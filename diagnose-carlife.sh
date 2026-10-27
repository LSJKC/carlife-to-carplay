#!/bin/bash
# CarLife 实时诊断脚本 - 抓取编码格式协商和参数传递

set -e

echo "========== CarLife 实时诊断脚本 =========="
echo "目标：监控 CarLife 编码格式参数是否被正确发送和接收"
echo ""

# 清空旧日志
adb logcat -c

# 启动日志抓取（后台）
echo "[*] 启动 ADB logcat 监听..."
adb logcat -v threadtime "*carlife*:V" "*carplay*:V" "*encoder*:V" > /tmp/carlife-debug.log 2>&1 &
LOGCAT_PID=$!
sleep 1

echo "[*] 请立即启动车机投屏... (等待 30 秒)"
echo "[*] 监控的关键字："
echo "    - 'invokeMethod: carlife_encode_format'"
echo "    - 'Bundle()' (应该看到 save_area_* 参数)"
echo "    - 'CarLife 视频布局'"
echo ""

sleep 30

# 停止日志抓取
kill $LOGCAT_PID 2>/dev/null || true
wait $LOGCAT_PID 2>/dev/null || true

echo ""
echo "========== 诊断结果 =========="
echo ""

# 检查关键日志
if grep -q "invokeMethod.*carlife_encode_format" /tmp/carlife-debug.log; then
    echo "✓ invokeMethod 被调用了"
else
    echo "✗ invokeMethod 未被调用"
fi

if grep -q "save_area_" /tmp/carlife-debug.log; then
    echo "✓ 检测到 safe_area_* 参数"
else
    echo "✗ 未发现 safe_area_* 参数"
fi

if grep -q "未采纳安全区\|保留完整" /tmp/carlife-debug.log; then
    echo "✗ 编码器未采纳安全区（bug 存在）"
    echo "  → 需要重新编译修复的版本"
elif grep -q "已采纳安全区" /tmp/carlife-debug.log; then
    echo "✓ 编码器已采纳安全区（bug 已修复）"
else
    echo "? 无法确定编码器是否采纳了安全区"
fi

echo ""
echo "完整日志已保存：/tmp/carlife-debug.log"
tail -50 /tmp/carlife-debug.log
