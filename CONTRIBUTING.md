# Contributing

Thanks for helping make MC Clock Widget easier to maintain and fork.

## Local Setup

Use Android Studio or the checked-in Gradle wrapper. JDK 17 or newer is recommended for the current Android Gradle Plugin.

Common commands from the repository root:

```sh
./gradlew test
./gradlew assembleDebug
```

On Windows, use `gradlew.bat` instead of `./gradlew`.

## Architecture

The app is widget-only. There is no launcher activity.

- Widget receivers are declared in `app/src/main/AndroidManifest.xml`.
- AppWidget sizing and preview metadata live in `app/src/main/res/xml/`.
- Widget layouts live in `app/src/main/res/layout/`; current variants share `widget_clock.xml`.
- Clock rendering logic lives under `app/src/main/java/com/victorzzt/mcclock/`.
- `WidgetVariant.kt` is the source of truth for each layout, rendering strategy, and drawable set.
- `BitmapResourceLoader.kt` owns the bounded process-local cache for decoded resources.
- `WidgetRefreshScheduler.kt` provides best-effort minute updates without waking a sleeping device.

Keep widget provider classes thin. Shared clock math, moon-phase calculation, bitmap rendering, and resource selection should live in reusable classes so new variants can be added without copying an entire provider.

## Adding Or Changing A Widget Variant

When a variant changes, check each layer deliberately:

- Add the layout and render configuration to `WidgetVariant`, then bind it from a one-line widget provider class.
- Add or update the receiver entry in `AndroidManifest.xml`.
- Add or update the `appwidget-provider` XML in `res/xml`.
- Name provider metadata `widget_info_<variant>.xml` and use the matching preview family.
- Reuse the shared `clock_image` layout contract unless the variant truly needs a different layout.
- Add preview and drawable assets with names that match the existing asset families.
- Add unit tests for any new time, frame, moon-phase, or renderer selection logic.
- Update README and Fastlane metadata when the user-facing widget list changes.

## Style

- Prefer Kotlin standard library and Android framework APIs already used by the app.
- Keep public names stable unless there is a clear migration reason.
- Do not introduce Compose or a launcher activity for widget-only work.
- Keep text files as LF. `.gitattributes` documents the expected line endings.
- Use ASCII in source and docs unless a file already has a reason to use non-ASCII text.

## Tests

At minimum, run:

```sh
./gradlew test
```

For changes touching Android resources or widget registration, also build a debug APK:

```sh
./gradlew assembleDebug
```
