#!/bin/bash
set -e

echo "=== Gradle Clean Build with Proxy ==="

# 代理设置（用户开了梯子）
export https_proxy=http://127.0.0.1:7890
export http_proxy=http://127.0.0.1:7890

echo "1. Cleaning previous build..."
timeout 60 ./gradlew clean 2>&1 | tail -20 || true

echo
echo "2. Starting full build (release APK)..."
echo "   Version: 1.2.49 (PCM latency fix + Bundle return fix)"
echo

timeout 600 ./gradlew assemble 2>&1 | tee build.log | tail -50

echo
echo "3. Build completed. Checking APK..."
ls -lh app/build/outputs/apk/debug/carlink-cast-*.apk 2>/dev/null || echo "Debug APK not found"
ls -lh app/build/outputs/apk/release/carlink-cast-*.apk 2>/dev/null || echo "Release APK not found"
