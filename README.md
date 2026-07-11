# MC Clock Widget

MC Clock Widget is an unofficial Android home screen widget inspired by the Minecraft in-game clock. It is a widget-only app: there is no launcher activity, and all user-facing entry points are Android AppWidget variants.

The Android application ID is `com.victorzzt.mcclock`.

## Widget Variants

The app currently exposes eight widgets:

- Classic procedural clock
- Classic high-resolution procedural clock
- Classic moon-phase procedural clock
- Classic 1.15 sprite-sheet clock
- Modern 1.17 sprite-sheet clock
- Modern procedural clock
- Modern high-resolution procedural clock
- Modern moon-phase procedural clock

## Building

Use the checked-in Gradle wrapper from the repository root:

```sh
./gradlew test assembleDebug
```

For release or F-Droid review notes, see `BUILD.md` if it is present in your checkout.

## Project Layout

- `app/src/main/java/com/victorzzt/mcclock/` contains widget providers and rendering code.
- `app/src/main/res/drawable/` contains clock masks, dials, sprite sheets, and previews.
- `app/src/main/res/xml/` contains Android AppWidget provider declarations.
- `fastlane/metadata/android/en-US/` contains store and F-Droid-facing metadata.

## Contributing

See `CONTRIBUTING.md` for local setup, architecture notes, and the checklist for adding or changing widget variants.

## Acknowledgements

- Respect to xlythe, author of the original Minecraft Clock app: https://f-droid.org/en/packages/com.xlythe.minecraftclock/
- This app has been developed with assistance from AI coding tools.

## License And Artwork

Source code is licensed under MIT-0. See `LICENSE`.

This project is not an official Minecraft product and is not affiliated with, endorsed by, sponsored by, or approved by Mojang AB or Microsoft. Minecraft names, brands, textures, graphics, and other assets are the property of their respective owners. Artwork and visually derived assets are not granted under MIT-0 unless a separate compatible license is documented for those files.
