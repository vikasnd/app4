# Jainkosh Android App - Runnable on GitHub Actions (logo auto-download)

This project is prepared to build on GitHub Actions and produce a debug APK artifact without requiring Android Studio locally.

## What changed in this update
- Workflow now attempts to download `https://www.jainkosh.org/w/images/6/65/Jainkosh.png` and save it as `app/src/main/res/drawable/jainkosh.png` during CI. This bundles the official logo into the APK automatically.
- Larger buttons and larger text on the main page for better accessibility.
- Footer line added: `Directed by Vikas Chhabra (Jain), Indore`.
- Quick navigation row appears at the top when viewing a webpage to jump to other main sections (Videos, Audio, Literature, Notes, Online).
- UI improvements for better usability.

## Notes
- If the logo download fails in CI for any reason, the app will still build and use the existing vector placeholder. You can also add the PNG manually into `app/src/main/res/drawable/jainkosh.png` in your repo if you prefer.
- Push to `main` branch to trigger the build; download `app-debug-apk` artifact from Actions to get the APK.
