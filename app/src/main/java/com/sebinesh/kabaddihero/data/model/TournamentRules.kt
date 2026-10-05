package com.sebinesh.kabaddihero.data.model

data class TournamentRules(
    val rulesProfileId: String = "IKF_STANDARD",
    val profileName: String = "IKF Standard",
    val playersOnCourt: Int = 7,
    val substitutesCount: Int = 5,
    val matchDurationMinutes: Int = 40,
    val halfDurationMinutes: Int = 20,
    val halfTimeBreakMinutes: Int = 5,
    val raidDurationSeconds: Int = 30,
    val emptyRaidsBeforeDoOrDie: Int = 2,
    val bonusLineActiveDefendersMin: Int = 6,
    val allOutBonusPoints: Int = 2,
    val superTackleDefendersMax: Int = 3,
    val superTacklePoints: Int = 2,
    val superRaidPointsMin: Int = 3,
    val weightLimitKg: Float? = 85.0f,
    val maxAgeYears: Int? = null,
    val minAgeYears: Int? = null,
    val specialRestrictions: String = "",
    val customRulesText: String = "",
    val hasCustomOverrides: Boolean = false
)
