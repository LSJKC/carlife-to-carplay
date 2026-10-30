#!/usr/bin/env bash
# 把本地 carlink-cast 同步到 GitHub。
#
# 平时改完代码跑一次就行：
#
#     ./sync.sh                 # 提交所有改动并推送 master
#     ./sync.sh "改了什么"       # 用指定信息提交
#     ./sync.sh --tag 1.3.18    # 提交、推送，并打 tag 附 APK 到 Release
#
# 网络走本机 127.0.0.1:7890 的代理（已写进本仓库的 git 配置）。

set -euo pipefail
cd "$(dirname "$0")"

REMOTE=origin
BRANCH=master
TAG=""

if [ "${1:-}" = "--tag" ]; then
    TAG="${2:?用法: ./sync.sh --tag <版本号>}"
    MESSAGE="发布 $TAG"
elif [ -n "${1:-}" ]; then
    MESSAGE="$1"
else
    MESSAGE=""
fi

# ---- 1. 提醒尚未处理的改动 ----
if [ -n "$(git status --porcelain)" ]; then
    if [ -z "$MESSAGE" ]; then
        echo "有未提交的改动："
        git status --short
        echo
        read -r -p "提交信息（留空则取消）: " MESSAGE
        [ -n "$MESSAGE" ] || { echo "已取消"; exit 1; }
    fi
    git add -A
    git commit -m "$MESSAGE"
else
    echo "工作区干净，无需提交"
fi

# ---- 2. 推送 master ----
git push "$REMOTE" "$BRANCH"
echo "已推送 $BRANCH"

# ---- 3. 可选：打 tag 并提示上传 APK ----
if [ -n "$TAG" ]; then
    git tag -f -a "v$TAG" -m "carlink-cast $TAG"
    git push -f "$REMOTE" "v$TAG"
    echo "已推送 tag v$TAG"
    cat <<EOF

下一步：把 APK 附到这个 Release。
  - 网页：https://github.com/LSJKC/carlife-to-carplay/releases/new?tag=v$TAG
  - 或用 gh：gh release upload v$TAG app/build/outputs/apk/debug/carlink-cast-$TAG.apk
EOF
fi
