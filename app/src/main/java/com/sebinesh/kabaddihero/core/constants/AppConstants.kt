package com.sebinesh.kabaddihero.core.constants

object AppConstants {
    const val APP_NAME_TA = "Kabaddi Hero"
    const val APP_NAME_EN = "Kabaddi Hero"
    const val AUTHOR = "by Sebinesh"
    const val TAGLINE_TA = "Discover Kabaddi tournaments • Register • Play • Win"
    const val TAGLINE_EN = "Discover Kabaddi tournaments • Register • Play • Win"

    // Supported Languages
    const val LANG_TAMIL = "ta"
    const val LANG_ENGLISH = "en"

    // Firebase Collections
    const val COLL_USERS = "users"
    const val COLL_TOURNAMENTS = "tournaments"
    const val COLL_TEAMS = "teams"
    const val COLL_PLAYERS = "players"
    const val COLL_REGISTRATIONS = "registrations"
    const val COLL_MATCHES = "matches"
    const val COLL_MATCH_EVENTS = "matchEvents"
    const val COLL_REPORTS = "reports"
    const val COLL_RULE_PROFILES = "ruleProfiles"
    const val COLL_AUDIT_LOGS = "auditLogs"
    const val COLL_SAVED = "savedTournaments"

    // Gemini Models
    const val MODEL_GEMINI_PRO_THINKING = "gemini-3.1-pro-preview"
    const val MODEL_GEMINI_FLASH_GENERAL = "gemini-3.5-flash"
    const val MODEL_GEMINI_FLASH_FAST = "gemini-3.1-flash-lite-preview"
    const val MODEL_GEMINI_FLASH_INTELLIGENCE = "gemini-3.5-flash"
    const val MODEL_GEMINI_TTS = "gemini-3.8-flash-tts"

    // Deep link schemes
    const val DEEP_LINK_SCHEME = "kabaddihero"
    const val DEEP_LINK_HOST = "kabaddihero.app"

    // SharedPreferences / DataStore
    const val PREFS_NAME = "kabaddi_hero_preferences"
    const val KEY_FIRST_LAUNCH = "key_first_launch_done"
    const val KEY_SELECTED_LANG = "key_selected_language"
    const val KEY_OFFLINE_DISTRICT = "key_offline_district"
}
