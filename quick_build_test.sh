#!/bin/bash
set -e

echo "=== Quick Build Test (不联网) ==="
echo

echo "1. 检查 gradle wrapper 是否可用"
if [ -f gradlew ]; then
    echo "   ✓ gradlew 存在"
    ls -lh gradlew
else
    echo "   ✗ gradlew 不存在，无法离线编译"
    exit 1
fi

echo
echo "2. 检查已缓存的依赖"
ls -lh ~/.gradle/wrapper/ 2>/dev/null | head -5 || echo "   (没有缓存)"

echo
echo "3. 检查 build 目录大小"
du -sh build 2>/dev/null || echo "   (首次编译)"

echo
echo "4. 尝试显示 gradle 任务列表（不需要网络）"
timeout 10 ./gradlew tasks --no-search-path 2>&1 | head -20 || echo "   (网络超时或无任务)"

echo
echo "5. 关键源文件最后修改时间"
stat adapter-carlife/src/main/java/com/carplay/adapter/carlife/CarLifeHostService.kt 2>/dev/null | grep Modify || echo "   (文件不存在)"
stat core-airplay/src/main/java/com/carplay/sender/AirPlaySender.kt 2>/dev/null | grep Modify || echo "   (文件不存在)"
