package com.sebinesh.kabaddihero.data.model

data class Match(
    val matchId: String = "",
    val tournamentId: String = "",
    val tournamentName: String = "",
    val matchNumber: Int = 1,
    val stage: String = "Quarter Final", // League, Round of 16, Quarter Final, Semi Final, Final
    val teamAId: String = "",
    val teamAName: String = "Madurai Lions",
    val scoreTeamA: Int = 0,
    val teamBId: String = "",
    val teamBName: String = "Nellai Tigers",
    val scoreTeamB: Int = 0,
    val currentHalf: Int = 1, // 1 or 2
    val matchTimeRemainingSeconds: Int = 1200, // 20 mins per half
    val raidTimeRemainingSeconds: Int = 30,
    val activeRaider: String = "",
    val activeRaiderTeam: String = "A", // A or B
    val status: String = "UPCOMING", // UPCOMING, LIVE, HALF_TIME, COMPLETED
    val teamAPlayersOnCourt: Int = 7,
    val teamBPlayersOnCourt: Int = 7,
    val teamAAllOuts: Int = 0,
    val teamBAllOuts: Int = 0,
    val recentEventsSummary: List<String> = emptyList(),
    val venue: String = "",
    val refereeName: String = "",
    val startedAt: Long? = null,
    val completedAt: Long? = null
)
