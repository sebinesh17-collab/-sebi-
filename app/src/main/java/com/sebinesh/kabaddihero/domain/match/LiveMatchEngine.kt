package com.sebinesh.kabaddihero.domain.match

import com.sebinesh.kabaddihero.data.model.Match
import com.sebinesh.kabaddihero.data.model.MatchEvent
import com.sebinesh.kabaddihero.data.model.TournamentRules

class LiveMatchEngine(private val rules: TournamentRules = TournamentRules()) {

    fun processSuccessfulRaid(
        currentMatch: Match,
        raiderTeam: String, // "A" or "B"
        touches: Int,
        gotBonus: Boolean,
        raiderName: String,
        officialId: String
    ): Pair<Match, MatchEvent> {
        val defendingTeam = if (raiderTeam == "A") "B" else "A"
        val defendersBefore = if (raiderTeam == "A") currentMatch.teamBPlayersOnCourt else currentMatch.teamAPlayersOnCourt

        var points = touches
        val bonusAwarded = gotBonus && defendersBefore >= rules.bonusLineActiveDefendersMin
        if (bonusAwarded) points += 1

        val remainingDefenders = (defendersBefore - touches).coerceAtLeast(0)
        val isAllOut = remainingDefenders == 0

        var allOutPoints = 0
        if (isAllOut) {
            allOutPoints = rules.allOutBonusPoints
            points += allOutPoints
        }

        // New scores
        val newScoreA = if (raiderTeam == "A") currentMatch.scoreTeamA + points else currentMatch.scoreTeamA
        val newScoreB = if (raiderTeam == "B") currentMatch.scoreTeamB + points else currentMatch.scoreTeamB

        val newDefendersCount = if (isAllOut) 7 else remainingDefenders

        // Raider team revivals (up to 7 players)
        val currentRaiderCourtPlayers = if (raiderTeam == "A") currentMatch.teamAPlayersOnCourt else currentMatch.teamBPlayersOnCourt
        val newRaiderCourtPlayers = (currentRaiderCourtPlayers + touches).coerceAtMost(7)

        val updatedMatch = if (raiderTeam == "A") {
            currentMatch.copy(
                scoreTeamA = newScoreA,
                teamAPlayersOnCourt = newRaiderCourtPlayers,
                teamBPlayersOnCourt = newDefendersCount,
                teamAAllOuts = if (isAllOut) currentMatch.teamAAllOuts + 1 else currentMatch.teamAAllOuts,
                raidTimeRemainingSeconds = rules.raidDurationSeconds,
                activeRaiderTeam = "B",
                recentEventsSummary = listOf(
                    "$raiderName: $touches Touch Pt" +
                            (if (bonusAwarded) " + Bonus" else "") +
                            (if (isAllOut) " + All-Out ($allOutPoints)!" else "")
                ) + currentMatch.recentEventsSummary.take(4)
            )
        } else {
            currentMatch.copy(
                scoreTeamB = newScoreB,
                teamBPlayersOnCourt = newRaiderCourtPlayers,
                teamAPlayersOnCourt = newDefendersCount,
                teamBAllOuts = if (isAllOut) currentMatch.teamBAllOuts + 1 else currentMatch.teamBAllOuts,
                raidTimeRemainingSeconds = rules.raidDurationSeconds,
                activeRaiderTeam = "A",
                recentEventsSummary = listOf(
                    "$raiderName: $touches Touch Pt" +
                            (if (bonusAwarded) " + Bonus" else "") +
                            (if (isAllOut) " + All-Out ($allOutPoints)!" else "")
                ) + currentMatch.recentEventsSummary.take(4)
            )
        }

        val event = MatchEvent(
            eventId = "evt_${System.currentTimeMillis()}",
            matchId = currentMatch.matchId,
            createdBy = officialId,
            eventType = if (isAllOut) "ALL_OUT" else if (touches >= rules.superRaidPointsMin) "SUPER_RAID" else "TOUCH_POINT",
            team = raiderTeam,
            player = raiderName,
            points = points,
            description = "$raiderName scored $points pts (Touches: $touches, Bonus: $bonusAwarded, AllOut: $isAllOut)"
        )

        return Pair(updatedMatch, event)
    }

    fun processSuccessfulTackle(
        currentMatch: Match,
        defendingTeam: String, // "A" or "B"
        tacklerName: String,
        officialId: String
    ): Pair<Match, MatchEvent> {
        val raiderTeam = if (defendingTeam == "A") "B" else "A"
        val defendersOnCourt = if (defendingTeam == "A") currentMatch.teamAPlayersOnCourt else currentMatch.teamBPlayersOnCourt

        val (points, isSuperTackle) = ScoreCalculator.calculateTacklePoints(defendersOnCourt, rules)

        val newScoreA = if (defendingTeam == "A") currentMatch.scoreTeamA + points else currentMatch.scoreTeamA
        val newScoreB = if (defendingTeam == "B") currentMatch.scoreTeamB + points else currentMatch.scoreTeamB

        // Raider is out
        val raiderCourtBefore = if (raiderTeam == "A") currentMatch.teamAPlayersOnCourt else currentMatch.teamBPlayersOnCourt
        val remainingRaiderCourt = (raiderCourtBefore - 1).coerceAtLeast(0)
        val isAllOut = remainingRaiderCourt == 0

        val extraAllOutPoints = if (isAllOut) rules.allOutBonusPoints else 0
        val finalScoreA = if (defendingTeam == "A") newScoreA + extraAllOutPoints else newScoreA
        val finalScoreB = if (defendingTeam == "B") newScoreB + extraAllOutPoints else newScoreB

        val finalRaiderCourt = if (isAllOut) 7 else remainingRaiderCourt
        // Defending team revives 1 player
        val finalDefendingCourt = (defendersOnCourt + 1).coerceAtMost(7)

        val updatedMatch = if (defendingTeam == "A") {
            currentMatch.copy(
                scoreTeamA = finalScoreA,
                teamAPlayersOnCourt = finalDefendingCourt,
                teamBPlayersOnCourt = finalRaiderCourt,
                teamAAllOuts = if (isAllOut) currentMatch.teamAAllOuts + 1 else currentMatch.teamAAllOuts,
                raidTimeRemainingSeconds = rules.raidDurationSeconds,
                activeRaiderTeam = "A",
                recentEventsSummary = listOf(
                    "$tacklerName Tackle Success!" + (if (isSuperTackle) " (Super Tackle +$points)" else " (+$points)")
                ) + currentMatch.recentEventsSummary.take(4)
            )
        } else {
            currentMatch.copy(
                scoreTeamB = finalScoreB,
                teamBPlayersOnCourt = finalDefendingCourt,
                teamAPlayersOnCourt = finalRaiderCourt,
                teamBAllOuts = if (isAllOut) currentMatch.teamBAllOuts + 1 else currentMatch.teamBAllOuts,
                raidTimeRemainingSeconds = rules.raidDurationSeconds,
                activeRaiderTeam = "B",
                recentEventsSummary = listOf(
                    "$tacklerName Tackle Success!" + (if (isSuperTackle) " (Super Tackle +$points)" else " (+$points)")
                ) + currentMatch.recentEventsSummary.take(4)
            )
        }

        val event = MatchEvent(
            eventId = "evt_${System.currentTimeMillis()}",
            matchId = currentMatch.matchId,
            createdBy = officialId,
            eventType = if (isSuperTackle) "SUPER_TACKLE" else "TACKLE_POINT",
            team = defendingTeam,
            player = tacklerName,
            points = points + extraAllOutPoints,
            description = "$tacklerName executed tackle (Super: $isSuperTackle, Pts: $points)"
        )

        return Pair(updatedMatch, event)
    }

    fun applyScoreCorrection(
        currentMatch: Match,
        teamToAdjust: String, // "A" or "B"
        pointDelta: Int, // e.g. -1 or +1
        reason: String,
        officialId: String
    ): Pair<Match, MatchEvent> {
        val updatedMatch = if (teamToAdjust == "A") {
            currentMatch.copy(
                scoreTeamA = (currentMatch.scoreTeamA + pointDelta).coerceAtLeast(0),
                recentEventsSummary = listOf("Score Correction: Team A $pointDelta pt ($reason)") + currentMatch.recentEventsSummary.take(4)
            )
        } else {
            currentMatch.copy(
                scoreTeamB = (currentMatch.scoreTeamB + pointDelta).coerceAtLeast(0),
                recentEventsSummary = listOf("Score Correction: Team B $pointDelta pt ($reason)") + currentMatch.recentEventsSummary.take(4)
            )
        }

        val event = MatchEvent(
            eventId = "evt_corr_${System.currentTimeMillis()}",
            matchId = currentMatch.matchId,
            createdBy = officialId,
            eventType = "CORRECTION",
            team = teamToAdjust,
            points = pointDelta,
            description = "Score correction for Team $teamToAdjust: $pointDelta pt. Reason: $reason",
            isCorrection = true
        )

        return Pair(updatedMatch, event)
    }
}
