# SETUP GUIDE — KABADDI HERO (கபடி ஹீரோ)

This document provides step-by-step setup instructions for development and production environments.

---

## 1. Prerequisites
- Android Studio Ladybug (2024.2.1+) or newer
- JDK 17 or JDK 21
- Android SDK API 35 with build-tools 35.0.0
- Google Play Services enabled device or emulator (API 24+)

---

## 2. Local Configuration (Development)

1. **Environment Variables**:
   Copy `.env.example` to `.env`:
   ```bash
   cp .env.example .env
   ```
   Add your development Gemini API key:
   ```properties
   GEMINI_API_KEY=AIzaSy...your_gemini_key
   ```

2. **Firebase Setup**:
   - Register Android app with package `com.sebinesh.kabaddihero`.
   - Obtain debug SHA-1 fingerprint:
     ```bash
     keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
     ```
   - Add SHA-1 to Firebase Console > Project Settings.
   - Download `google-services.json` into `app/google-services.json`.

3. **Firebase App Check (Debug Mode)**:
   In debug builds, `AppCheckManager` uses `DebugAppCheckProviderFactory`.
   Check Logcat for the debug secret token and register it under Firebase Console > App Check > Apps > Manage debug tokens.

---

## 3. Production Configuration

1. **Release Keystore**:
   Generate an upload key:
   ```bash
   keytool -genkey -v -keystore my-upload-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias upload
   ```
2. Set environment variables for CI/CD builds:
   - `KEYSTORE_PATH=/path/to/my-upload-key.jks`
   - `STORE_PASSWORD=your_store_password`
   - `KEY_PASSWORD=your_key_password`

3. **Production App Check**:
   Configure Play Integrity provider in Firebase App Check console and link your Google Play Console project.

4. **Google Maps SDK**:
   Add your Google Maps API key in `AndroidManifest.xml` metadata or via BuildConfig.
