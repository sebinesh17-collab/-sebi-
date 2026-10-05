# கபடி ஹீரோ (Kabaddi Hero) — by Sebinesh

> **கபடி போட்டிகளை கண்டுபிடி • பதிவு செய் • விளையாடு • வெற்றி பெறு**  
> *Discover Kabaddi tournaments • Register • Play • Win*

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-M3-blue.svg)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Firebase-Auth%20%7C%20Firestore%20%7C%20Storage%20%7C%20FCM%20%7C%20AppCheck-orange.svg)](https://firebase.google.com)
[![Gemini AI](https://img.shields.io/badge/Gemini_AI-High_Thinking%20%7C%20Multimodal%20%7C%20TTS-teal.svg)](https://deepmind.google/technologies/gemini/)

---

## 🏆 What is Kabaddi Hero?

**Kabaddi Hero (கபடி ஹீரோ)** is a production-grade, Tamil-first Android sports ecosystem built for players, team managers, organizers, referees, and passionate Kabaddi fans.

From local village sand court tournaments (*தரை ஆட்டம்*) to state championship mat leagues (*மேட் போட்டிகள்*), Kabaddi Hero digitizes the entire lifecycle of the sport:
- 📍 **Local Tournament Discovery**: Interactive list and Google Maps views, filter by district (Madurai, Tirunelveli, Thanjavur, Chennai, Salem, etc.), age category, weight limit, prize, and entry fee.
- 📝 **7-Step Tournament Publishing**: Complete organizer workflow with venue geotagging, rules profile customization (IKF, Junior, Sub-Junior, Local Open), duplicate event detection, and evidence submission.
- ⚡ **AI-Assisted Poster Importer**: Multimodal Gemini analyzes tournament posters and extracts all dates, venues, weight classes, entry fees, and rules automatically.
- 🤖 **Kabaddi Hero AI & Rules Assistant**: Multi-turn Gemini chatbot with High Thinking mode, structured eligibility verification, rule explainer in clear Tamil & English, and voice TTS.
- ⏱️ **Real-Time Match Scorer & Public Scoreboard**: Point-by-point live match logging (Raids, Touch, Tackle, Bonus, Super Raid, Super Tackle, All-Outs, Substitutions) with immutable audit events.
- 🛡️ **Role-Based Security & Verification Workflow**: Badged verification (`VERIFIED`, `PENDING REVIEW`, `NEEDS CORRECTION`), anti-spam rate limiting, community reporting, and moderation dashboard.

---

## 📐 Architecture & Technology Stack

- **UI Framework**: 100% Jetpack Compose with Material 3 (Dynamic Color + Clay & Stadium Mat palettes).
- **Architecture**: MVVM & Clean Architecture with Service Locator, StateFlow, Coroutines, and immutable UI state.
- **Backend & Cloud**:
  - **Firebase Authentication**: Email/Password, Google Sign-In, Phone Auth, and Guest mode.
  - **Cloud Firestore**: Real-time snapshot updates, offline persistence, composite indexing.
  - **Firebase Storage**: Secure folder segregation for posters, proof photos, team logos, and player portraits.
  - **Firebase Cloud Messaging (FCM)**: District & match topic subscriptions and deep links.
  - **Firebase App Check**: Play Integrity in production and debug provider in development.
- **AI Engine (Firebase AI Logic + Gemini REST)**:
  - `gemini-3.1-pro-preview` for complex rule conflict analysis with `thinkingLevel = HIGH`.
  - `gemini-3.5-flash` for multi-turn assistant queries and poster multimodal extraction.
  - `gemini-3.1-flash-lite` for instantaneous responses.
  - `gemini-3.8-flash-tts` for text-to-speech audio announcements.
- **Data Persistence**: Offline-first caching with DataStore Preferences and Firestore cache.

---

## 🚀 Getting Started

### 1. Open in Android Studio
1. Clone this repository:
   ```bash
   git clone https://github.com/sebinesh/kabaddi-hero.git
   ```
2. Open Android Studio Ladybug or later.
3. Select **File > Open** and choose the `kabaddi-hero` directory.
4. Allow Gradle to sync.

### 2. Configure Firebase
1. Create a project in [Firebase Console](https://console.firebase.google.com/).
2. Add an Android app with package name `com.sebinesh.kabaddihero`.
3. Download `google-services.json` and place it in the `app/` directory.
4. Enable **Authentication** (Email/Password, Google, Phone).
5. Enable **Cloud Firestore** and deploy rules:
   ```bash
   firebase deploy --only firestore:rules,firestore:indexes
   ```
6. Enable **Firebase Storage** and deploy rules:
   ```bash
   firebase deploy --only storage:rules
   ```

### 3. Configure Gemini AI & Secrets
In `.env` or the AI Studio Secrets panel, set:
```properties
GEMINI_API_KEY=YOUR_GEMINI_API_KEY
```

---

## 📱 Supported User Roles
- `USER`: Browse tournaments, follow matches, ask AI questions, save events.
- `PLAYER`: Create player profile, track match statistics (raid & tackle points).
- `TEAM_MANAGER`: Create and manage team rosters, register for open tournaments.
- `ORGANIZER`: Publish tournaments, verify team registrations, manage matches.
- `REFEREE`: Operate the live match scorer, log raid outcomes and technical points.
- `MODERATOR` & `ADMIN`: Manage verification queue, inspect reports, view audit logs.

---

## 📜 Baseline Kabaddi Rule Profiles
- **IKF Standard**: 7 players on court, 5 substitutes, 40-minute match (20-5-20), Senior Men max 85kg, Senior Women max 75kg, 30s raid duration, 3rd empty raid = Do-or-Die.
- **Local Open (*திறந்தவெளி போட்டி*)**: Custom weight limits, flexible timing, local tie-break rules.
- **Junior (< 20 yrs)**: Boys 70kg, Girls 65kg.
- **Sub-Junior (< 16 yrs)**: Boys 55kg, Girls 50kg.

---

## 🧪 Testing
Run local unit and JVM Robolectric tests:
```bash
gradle :app:testDebugUnitTest
```
Tests cover:
- Rule engine calculations & weight/age boundaries
- Eligibility verification logic
- Live match scorer point accumulation & All-Out detection
- Form field validators and anti-spam rate limiting

---

## 👨‍💻 Created by
**Sebinesh** — *Kabaddi Hero Creator*
