package com.sebinesh.kabaddihero.data.local

import com.sebinesh.kabaddihero.data.model.*

object DemoDataProvider {

    val ruleProfiles = listOf(
        RuleProfile(
            ruleProfileId = "IKF_STANDARD",
            nameEn = "IKF Standard Rules",
            nameTa = "IKF International Kabaddi Federation Rules",
            source = "International Kabaddi Federation",
            sourceUrl = "https://www.kabaddiikf.com",
            version = "2026.1",
            effectiveDate = "2026-01-01",
            ageRules = "Open for all eligible players with verified age document.",
            weightRules = "Senior Men: Max 85 KG | Senior Women: Max 75 KG",
            teamRules = "7 players on court, 5 registered substitutes. Max 12 squad size.",
            matchDurationMinutes = 40,
            scoringRules = "1 point per Touch/Tackle. 1 Bonus point (if >= 6 defenders). 2 points for All-Out. 2 points for Super Tackle.",
            raidRules = "Raid duration 30 seconds. Cant must be continuous. 3rd empty raid triggers Do-or-Die raid.",
            tieBreakRules = "5 Golden Raids for each team with baulk line as bonus line."
        ),
        RuleProfile(
            ruleProfileId = "LOCAL_OPEN",
            nameEn = "Tamil Nadu Local Open",
            nameTa = "Tamil Nadu Open Tournament Rules",
            source = "Tamil Nadu Amateur Kabaddi Association",
            version = "2026.1",
            weightRules = "Max 80 KG weigh-in at 7:30 AM strictly.",
            matchDurationMinutes = 30,
            scoringRules = "15-5-15 format. Super tackle enabled. Traditional clay court rules."
        ),
        RuleProfile(
            ruleProfileId = "JUNIOR",
            nameEn = "Junior Category (Under 20)",
            nameTa = "Junior Category (Under 20)",
            version = "2026.1",
            ageRules = "Born on or after 2006-01-01.",
            weightRules = "Boys: Max 70 KG | Girls: Max 65 KG",
            matchDurationMinutes = 30
        ),
        RuleProfile(
            ruleProfileId = "SUB_JUNIOR",
            nameEn = "Sub-Junior Category (Under 16)",
            nameTa = "Sub-Junior Category (Under 16)",
            version = "2026.1",
            ageRules = "Born on or after 2010-01-01.",
            weightRules = "Boys: Max 55 KG | Girls: Max 50 KG",
            matchDurationMinutes = 20
        )
    )

    // All sample posts, tournaments, matches, players, and teams removed as requested
    val sampleTournaments: List<Tournament> = emptyList()
    val sampleMatches: List<Match> = emptyList()
    val samplePlayers: List<Player> = emptyList()
    val sampleTeams: List<Team> = emptyList()
}
