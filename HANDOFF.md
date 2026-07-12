# Maintenance Notes

## Current Architecture

- Extracted `ClockTime`, `MoonPhase`, renderer classes, and resource mappings with unit tests.
- `BaseClockWidgetProvider` and `WidgetVariant` keep manifest provider classes as thin shells.
- Centralized each variant's render mode and drawable selection without merging their visual behavior.
- `widget_clock.xml` is the shared runtime widget layout.
- Provider-info XML files use the `widget_info_<variant>.xml` naming pattern.
- Classic and Modern preview families use separate preview resources.
- All variants request a default 2x2 launcher span with legacy size fallback.

## Refresh And Cache Behavior

`WidgetRefreshScheduler.kt` schedules an inexact `ELAPSED_REALTIME` repeating alarm while a provider has active widgets. It does not wake a sleeping device and may be delayed by Doze or OEM battery policies. The 30-minute `updatePeriodMillis` remains as a system fallback. Provider enable/update paths establish the alarm, disable removes it, and each alarm callback re-queries active widget IDs before rendering.

`CachedBitmapResourceLoader` stores immutable decoded resources and 256px pre-scaled resources in a synchronized 4 MiB `LruCache`. Its key includes the `Resources` identity, configuration hash, drawable ID, and target size. It reloads on a miss or recycled bitmap. Process death naturally clears the cache; the next widget refresh recreates it. Final time-dependent rendered clock images are deliberately not cached.

## Verification Commands

```sh
./gradlew test assembleDebug
```

Use the Android Studio bundled JDK if the shell resolves only a JRE:

```powershell
.\gradlew.bat '-Dorg.gradle.java.home=C:\Program Files\Android\Android Studio\jbr' test assembleDebug
```
