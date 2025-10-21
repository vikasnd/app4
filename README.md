# Jainkosh Android App - Runnable on GitHub Actions

This project is prepared to build on GitHub Actions and produce a debug APK artifact without requiring Android Studio locally.

## Included fixes
- `android.useAndroidX=true` and `android.enableJetifier=true` set in `gradle.properties`.
- `namespace` specified in `app/build.gradle`.
- Workflow installs Gradle 8.4 on the runner via gradle/gradle-build-action.

## How to get the APK
1. Push to GitHub `main` branch.
2. Open Actions -> workflow run -> download `app-debug-apk` artifact.
