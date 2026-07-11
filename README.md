# MC Clock Widget

[English](#english) | [中文](#中文)

## English

MC Clock Widget is an unofficial Android home screen widget inspired by the Minecraft in-game clock. It is a widget-only app: there is no launcher activity, and all user-facing entry points are Android AppWidget variants.

The Android application ID is `com.victorzzt.mcclock`.

### Widget Variants

The app currently exposes eight widgets:

- Classic procedural clock
- Classic high-resolution procedural clock
- Classic moon-phase procedural clock
- Classic 1.15 sprite-sheet clock
- Modern 1.17 sprite-sheet clock
- Modern procedural clock
- Modern high-resolution procedural clock
- Modern moon-phase procedural clock

### Building

Use the checked-in Gradle wrapper from the repository root:

```sh
./gradlew test assembleDebug
```

On Windows, use `gradlew.bat`. For release or F-Droid review notes, see `BUILD.md` if it is present in your checkout.

### Project Layout

- `app/src/main/java/com/victorzzt/mcclock/` contains widget providers, refresh scheduling, time models, resource caching, and rendering code.
- `app/src/main/res/drawable/` contains clock masks, dials, sprite sheets, and previews.
- `app/src/main/res/layout/` contains the shared widget and preview layouts.
- `app/src/main/res/xml/` contains Android AppWidget provider declarations.
- `fastlane/metadata/android/en-US/` contains store and F-Droid-facing metadata.

### Contributing

See `CONTRIBUTING.md` for local setup, architecture notes, and the checklist for adding or changing widget variants.

### Acknowledgements

- Respect to xlythe, author of the original Minecraft Clock app: https://f-droid.org/en/packages/com.xlythe.minecraftclock/
- Development and refactoring assistance was provided by Gemini CLI and OpenAI Codex.

### License And Artwork

Source code is licensed under MIT-0. See `LICENSE`.

This project is not an official Minecraft product and is not affiliated with, endorsed by, sponsored by, or approved by Mojang AB or Microsoft. Minecraft names, brands, textures, graphics, and other assets are the property of their respective owners. Artwork and visually derived assets are not granted under MIT-0 unless a separate compatible license is documented for those files.

## 中文

MC Clock Widget 是一款受 Minecraft 游戏内时钟启发的非官方 Android 桌面小组件。本项目仅提供 Widget，没有启动页 Activity；用户可见的入口均为 Android AppWidget 变体。

Android application ID 为 `com.victorzzt.mcclock`。

### Widget 变体

应用当前提供八种 Widget：

- 经典程序渲染时钟
- 经典高分辨率程序渲染时钟
- 经典月相程序渲染时钟
- 经典 1.15 版本 Sprite Sheet 时钟
- 现代 1.17 版本 Sprite Sheet 时钟
- 现代程序渲染时钟
- 现代高分辨率程序渲染时钟
- 现代月相程序渲染时钟

### 构建

在仓库根目录使用项目自带的 Gradle Wrapper：

```sh
./gradlew test assembleDebug
```

Windows 环境请使用 `gradlew.bat`。发布或 F-Droid 审核说明见仓库中的 `BUILD.md`（如果当前检出中包含该文件）。

### 项目结构

- `app/src/main/java/com/victorzzt/mcclock/` 包含 Widget Provider、刷新调度、时间模型、资源缓存和渲染代码。
- `app/src/main/res/drawable/` 包含时钟遮罩、表盘、Sprite Sheet 和预览图。
- `app/src/main/res/layout/` 包含共享 Widget 布局和预览布局。
- `app/src/main/res/xml/` 包含 Android AppWidget Provider 声明。
- `fastlane/metadata/android/en-US/` 包含应用商店及 F-Droid 元数据。

### 参与贡献

本地环境、架构说明以及新增或修改 Widget 变体的检查清单见 `CONTRIBUTING.md`。

### 致谢

- 向原版 Minecraft Clock 应用作者 xlythe 致敬：https://f-droid.org/en/packages/com.xlythe.minecraftclock/
- Gemini CLI 与 OpenAI Codex 为本项目提供了开发和重构协助。

### 许可证与美术资源

源代码使用 MIT-0 许可证，详见 `LICENSE`。

本项目不是 Minecraft 官方产品，也不隶属于 Mojang AB 或 Microsoft，未获得其认可、赞助或批准。Minecraft 的名称、品牌、纹理、图形及其他资产归各自权利人所有。除非相关文件另有明确且兼容的许可证说明，否则美术及视觉衍生资源不在 MIT-0 授权范围内。
