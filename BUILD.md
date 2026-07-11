# Build Notes

This document is a build note for maintainers and F-Droid reviewers.

## Environment

- Build platform: Linux
- JDK: 17 or newer, as required by recent Android Gradle Plugin versions
- Android SDK: install the Android SDK command line tools and Android platform matching `compileSdk`
- Gradle: use the checked-in Gradle wrapper

## Project

- Application ID: `com.victorzzt.mcclock`
- Version name: `1.0`
- Version code: `2`
- Min SDK: 26
- Target SDK: 36
- Compile SDK: 36

## Build Commands

Run from the repository root on Linux:

```sh
./gradlew clean assembleRelease
```

For a debug build:

```sh
./gradlew clean assembleDebug
```

## Notes For Review

- This project is an Android home screen widget and does not require special runtime permissions.
- The app is intended to run fully offline.
- Generated release artifacts are not committed to the repository.
- Text files in this repository should keep LF line endings.

## TODO Before Submission

- Confirm the exact Android SDK packages used by the F-Droid build environment.
- Confirm the release tag or commit used for version `1.0` / version code `2`.
- Confirm whether any artwork requires F-Droid anti-feature metadata.
