# Agent Handoff

## Current State

The refactor and resource cleanup are implemented in the working tree and have been verified on a physical Android device by the user. The user reported no functional problems after the latest refresh, preview, sizing, and cache changes.

The work is not staged or committed. Preserve the existing dirty working tree and do not revert files that appear as deleted or untracked: many of those entries are intentional layout, provider-info, preview, and source-file replacements from this refactor.

## Completed Work

- Added repository hygiene and contributor documentation.
- Extracted `ClockTime`, `MoonPhase`, renderer classes, and resource mappings with unit tests.
- Added `BaseClockWidgetProvider` and `WidgetVariant`; all eight manifest provider classes are thin shells.
- Centralized each variant's render mode and drawable selection without merging their visual behavior.
- Added a process-local 4 MiB LRU bitmap resource cache for decoded and pre-scaled immutable bitmaps.
- Consolidated duplicate runtime layouts into `widget_clock.xml` and centralized dimensions and integers.
- Standardized provider-info XML names as `widget_info_<variant>.xml`.
- Standardized Classic and Modern preview families and fixed preview double-padding.
- Set all variants to request a default 2x2 launcher span with legacy size fallback.
- Added best-effort minute refresh scheduling with a non-wakeup repeating alarm and a legal 30-minute AppWidget XML fallback.
- Rewrote README in English and Chinese and credited Gemini CLI and OpenAI Codex.

## Refresh And Cache Behavior

`WidgetRefreshScheduler.kt` schedules an inexact `ELAPSED_REALTIME` repeating alarm while a provider has active widgets. It does not wake a sleeping device and may be delayed by Doze or OEM battery policies. The 30-minute `updatePeriodMillis` remains as a system fallback. Provider enable/update paths establish the alarm, disable removes it, and each alarm callback re-queries active widget IDs before rendering.

`CachedBitmapResourceLoader` stores immutable decoded resources and 256px pre-scaled resources in a synchronized 4 MiB `LruCache`. Its key includes the `Resources` identity, configuration hash, drawable ID, and target size. It reloads on a miss or recycled bitmap. Process death naturally clears the cache; the next widget refresh recreates it. Final time-dependent rendered clock images are deliberately not cached.

## Verification

Last automated verification:

```sh
./gradlew test assembleDebug
```

- 12 JVM unit tests passed.
- Debug APK assembly, Android resource compilation, and manifest merging passed.
- `git diff --check` passed.
- The user completed physical-device testing and confirmed expected behavior.

Use the Android Studio bundled JDK if the shell resolves only a JRE:

```powershell
.\gradlew.bat '-Dorg.gradle.java.home=C:\Program Files\Android\Android Studio\jbr' test assembleDebug
```

## Next Agent Task: Documentation Comments Only

Add comments to all Kotlin files that implement user-visible functionality and to the relevant UI XML files. This is a documentation-only pass: do not change behavior, APIs, resources, rendering output, refresh timing, cache policy, widget dimensions, or manifest registration.

### Kotlin Rules

- Use Javadoc-style Kotlin documentation comments (`/** ... */`, conventionally called KDoc in Kotlin).
- Document classes, objects, interfaces, and non-obvious public or internal functions.
- Explain lifecycle, rendering, resource selection, refresh, and cache responsibilities where that context helps a new contributor.
- Keep one-line provider shells concise; a short class KDoc identifying the bound variant is sufficient.
- Do not narrate obvious assignments, individual branches, or every private helper.
- Do not convert existing useful comments into longer prose merely for coverage.

Primary Kotlin scope:

- `app/src/main/java/com/victorzzt/mcclock/AnalogClockWidget*.kt`
- `app/src/main/java/com/victorzzt/mcclock/BaseClockWidgetProvider.kt`
- `app/src/main/java/com/victorzzt/mcclock/BitmapResourceLoader.kt`
- `app/src/main/java/com/victorzzt/mcclock/ClockRenderer.kt`
- `app/src/main/java/com/victorzzt/mcclock/ClockTime.kt`
- `app/src/main/java/com/victorzzt/mcclock/ClockUtils.kt`
- `app/src/main/java/com/victorzzt/mcclock/MoonPhase.kt`
- `app/src/main/java/com/victorzzt/mcclock/MoonPhaseDialResources.kt`
- `app/src/main/java/com/victorzzt/mcclock/WidgetRefreshScheduler.kt`
- `app/src/main/java/com/victorzzt/mcclock/WidgetVariant.kt`

### XML Rules

- Add XML comments only for major visual modules or a file's visual role.
- In `widget_clock.xml`, describe the root widget surface and the rendered clock image module.
- In `widget_preview_classic.xml` and `widget_preview_modern.xml`, describe the family preview image module.
- Provider-info XML may receive one short file-level comment explaining its variant metadata/preview role if useful; do not comment every attribute.
- Do not add explanatory comments to generated, backup, color, dimension, integer, string, or theme resources unless a genuinely non-obvious visual contract requires it.

## Acceptance Checklist For The Next Agent

- Only comments or documentation files change.
- All user-function Kotlin files receive useful KDoc without comment noise.
- UI XML comments describe major visual modules, not individual attributes.
- Thin providers remain thin.
- No resource identifiers or manifest component names change.
- `./gradlew test assembleDebug` passes.
- `git diff --check` passes.
