# CarLink Cast 分支和提交规范

## 分支策略

### 主分支
- **main**: 生产发布版本，只接受 release 和 hotfix 合并
- **develop**: 开发主线，接受 feature/bugfix 合并

### 分支命名
```
feature/<模块>-<功能>
  feature/carlife-safe-area
  feature/airplay-pcm-sync

bugfix/<模块>-<问题>
  bugfix/iap2-reconnect-fail
  bugfix/carlife-bundle-return

hotfix/<问题>
  hotfix/connection-reset-crash

docs/<内容>
  docs/carlife-protocol
```

### 分支操作流程

#### 1. 创建新分支
```bash
# 基于 develop 创建
git checkout develop
git pull origin develop
git checkout -b feature/carlife-safe-area
```

#### 2. 提交规范
```bash
# 标题行 (50 字以内，自动加分支前缀)
git commit -m "修复 CarLife 编码器未采纳安全区问题"

# 完整格式（分支 hook 自动生成）
# [carlife] 修复 CarLife 编码器未采纳安全区问题
#
# - Bundle 返回语句缺失，编码器收不到 safe_area_* 参数
# - 修改：CarLifeHostService.kt:457 return Bundle
# - 影响：全屏编码 + 触摸崩溃 + 30s 拆连
# - 测试：验证 elapsedMs=10575 日志显示 "已采纳安全区"
```

#### 3. 推送和 PR
```bash
# 推送到远程
git push -u origin feature/carlife-safe-area

# GitHub/GitLab 上创建 Pull Request
# - 基于 develop 分支
# - 标题：清晰说明修复内容
# - 描述：
#   修复了 X 问题
#   相关日志行：elapsedMs=XXXX
#   测试方法：XXXXXX
```

#### 4. 合并策略
```bash
# develop → main (release)
git checkout main
git merge --no-ff develop -m "[release] v1.2.49 released"
git tag -a v1.2.49 -m "Release v1.2.49"
git push origin main --tags

# feature → develop (常规)
# 使用 squash merge 保持历史清晰
git checkout develop
git merge --squash feature/carlife-safe-area
git commit -m "[carlife] 修复编码器未采纳安全区"
git push origin develop
```

## 提交模板

使用本地提交模板：
```bash
git config commit.template .gitmessage
```

模板文件位置：`.gitmessage`

## 日志阅读指南

### 关键字检索
```bash
# 查看所有 carlife 修复
git log --oneline | grep carlife

# 查看某个功能从开始到完成
git log --grep="safe-area" --oneline

# 查看某个文件的改动历史
git log --oneline adapter-carlife/src/main/java/com/carplay/adapter/carlife/CarLifeHostService.kt
```

### 版本对比
```bash
# 对比两个版本的改动
git diff v1.2.47..v1.2.48

# 查看某个版本中涉及 CarLife 的所有改动
git show v1.2.48 --name-only | grep carlife
```

## CI/CD 集成

提交后自动触发：
1. 编译检查 (Kotlin linter)
2. 单元测试
3. 集成测试（可选）
4. APK 打包和版本自增

最新开发版本发布到 artifacts 目录供手机测试。
