#!/bin/bash
set -e

# 代理配置  
export https_proxy=http://127.0.0.1:7890
export http_proxy=http://127.0.0.1:7890
export GRADLE_OPTS="-Dhttp.socketTimeout=120000 -Dhttp.connectionTimeout=120000"

echo "=== 编译 carlink-cast 1.2.49 (PCM 1000ms 已确认) ==="
echo "版本标记：1.2.49-169-realcar-fixes"
echo

echo "开始编译..."
timeout 900 ./gradlew assembleDebug 2>&1 | tail -100

echo
echo "=== 编译完成 ==="
ls -lh app/build/outputs/apk/debug/carlink-cast-*.apk
