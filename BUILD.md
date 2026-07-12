# Build Instructions

This document describes the command-line build environment for MC Clock Widget.

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

## Release Notes

- This project is an Android home screen widget and does not require special runtime permissions.
- The app is intended to run fully offline.
- Generated release artifacts are not committed to the repository.
- Text files in this repository should keep LF line endings.
