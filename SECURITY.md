# SECURITY POLICY — KABADDI HERO (கபடி ஹீரோ)

Security and user trust are central to the Kabaddi Hero ecosystem.

---

## 🔒 Security Principles

1. **Zero Secret Leakage in APK**:
   - Private Gemini API keys are never packaged or exposed directly in public releases.
   - Production AI calls are routed through Firebase AI Logic with App Check attestation.
   - Build environment secrets use Secrets Gradle Plugin and environment variables.

2. **Role-Based Access Control (RBAC)**:
   - Client applications cannot escalate their own privileges.
   - User roles (`USER`, `PLAYER`, `TEAM_MANAGER`, `ORGANIZER`, `REFEREE`, `MODERATOR`, `ADMIN`) are enforced at the Firestore Security Rules layer.
   - Crucial operations such as verification, match correction, and moderation are audit-logged and protected.

3. **Immutable Match History**:
   - Match scoring events (`RAID_START`, `TOUCH_POINT`, `TACKLE_POINT`, `BONUS_POINT`, `ALL_OUT`) are stored as an append-only event stream.
   - Referees and scorers append correction events rather than mutating history silently.

4. **Anti-Spam & Rate Limiting**:
   - In-memory and server-side cooldown limits prevent duplicate publishing and denial-of-service spam on AI endpoints.
   - Automatic duplicate detection warns organizers if an identical tournament exists.

5. **Reporting Vulnerabilities**:
   Please report security vulnerabilities directly to `sebinesh15@gmail.com`.
