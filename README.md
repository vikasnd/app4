# Jainkosh Android App - Project Export

This ZIP contains a ready-to-commit Android Studio project scaffold for the *Jainkosh* app (Kotlin).

## What is included
- `app/` module with Kotlin sources, layouts, resources.
- Gradle files and `gradle/wrapper/gradle-wrapper.properties`.
- GitHub Actions workflow: `.github/workflows/android.yml` — builds the debug APK on push to `main`/`master`.
- Placeholder vector icons (replace with your assets as needed).

## Important notes before building
1. **Gradle wrapper binary**: `gradle-wrapper.properties` is included, but the wrapper JAR (`gradle-wrapper.jar`) is not present in this ZIP. Either:
   - Run `gradle wrapper --gradle-version 8.4` in the project root to generate the wrapper files, **or**
   - Copy `gradle/wrapper/gradle-wrapper.jar` from an existing project into `gradle/wrapper/`.

2. **Build environment**:
   - To build locally, install Android Studio and Android SDK with API 34.
   - Open the project in Android Studio and let it sync; then build `app -> Build APK(s)`.

3. **Automatic APK on GitHub**:
   - Push this repo to GitHub (to `main` or `master`) and Actions will run the workflow included in `.github/workflows/android.yml`.
   - The artifact uploaded is named `app-debug-apk` and the APK path inside the runner will be `app/build/outputs/apk/debug/app-debug.apk`.

## Why I can't produce an APK directly here
I created the entire project and zipped it for you. However, I cannot build a real Android APK in this environment because building an APK requires the Android SDK, build tools, and Gradle execution which aren't available in this sandboxed environment. The included GitHub Actions workflow will build the debug APK automatically when you push to GitHub (no manual build required).

## Quick local build steps
1. Ensure Android Studio with SDK 34 is installed.
2. Place `gradle/wrapper/gradle-wrapper.jar` if not present, or run `gradle wrapper --gradle-version 8.4`.
3. Open project in Android Studio.
4. Build -> Assemble Debug (or run `./gradlew :app:assembleDebug`).

If you'd like, I can:
- Create the `gradle-wrapper.jar` for you if you upload one or tell me to fetch it (note: I can't fetch from the internet in this environment).
- Modify the workflow to sign a release APK (needs secrets on GitHub).
- Add your logo files if you upload them here.

