# Third-Party Licenses

This document is a maintainer-facing summary of third-party code and build dependencies used by this project. It should be checked against the actual Gradle dependency report before release.

## Project License

MC Clock Widget source code is licensed under MIT-0. See `LICENSE`.

## Runtime Dependencies

| Component | Purpose | License |
| --- | --- | --- |
| AndroidX Core KTX (`androidx.core:core-ktx`) | Android Kotlin convenience APIs | Apache License 2.0 |
| Kotlin standard/runtime components | Kotlin language runtime support, if packaged transitively | Apache License 2.0 |

## Test Dependencies

| Component | Purpose | License |
| --- | --- | --- |
| JUnit 4 (`junit:junit`) | Unit testing | Eclipse Public License 1.0 |
| AndroidX Test JUnit (`androidx.test.ext:junit`) | Android instrumentation test integration | Apache License 2.0 |
| Espresso Core (`androidx.test.espresso:espresso-core`) | Android UI testing | Apache License 2.0 |

## Build Tooling

| Component | Purpose | License / Status |
| --- | --- | --- |
| Gradle Wrapper / Gradle | Build system | Apache License 2.0 |
| Android Gradle Plugin | Android build integration | Apache License 2.0 |
| Kotlin Gradle Plugin | Kotlin build integration | Apache License 2.0 |
| Android SDK platform APIs | Build platform, not bundled as project code | Review under Android SDK terms |

## TODO Before Submission

- Run the Gradle dependency report on Linux and verify transitive dependencies.
- Add exact versions for all third-party dependencies.
- Confirm whether any generated or bundled artwork needs separate license treatment.
