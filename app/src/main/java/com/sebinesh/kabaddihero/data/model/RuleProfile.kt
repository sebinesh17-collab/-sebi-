package com.sebinesh.kabaddihero.data.model

data class RuleProfile(
    val ruleProfileId: String = "",
    val nameEn: String = "",
    val nameTa: String = "",
    val source: String = "International Kabaddi Federation (IKF)",
    val sourceUrl: String = "https://www.kabaddiikf.com",
    val version: String = "2026.1",
    val effectiveDate: String = "2026-01-01",
    val lastReviewedAt: Long = System.currentTimeMillis(),
    val ageRules: String = "",
    val weightRules: String = "",
    val teamRules: String = "",
    val matchDurationMinutes: Int = 40,
    val scoringRules: String = "",
    val raidRules: String = "",
    val tieBreakRules: String = "",
    val customRules: String = ""
)
