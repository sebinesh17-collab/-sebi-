package com.sebinesh.kabaddihero

import com.sebinesh.kabaddihero.data.model.Match
import com.sebinesh.kabaddihero.data.model.TournamentRules
import com.sebinesh.kabaddihero.domain.match.LiveMatchEngine
import org.junit.Assert.*
import org.junit.Test

class LiveMatchEngineTest {

    private val engine = LiveMatchEngine(TournamentRules())

    @Test
    fun testSuccessfulSingleTouchRaid() {
        val initialMatch = Match(
            matchId = "test_match",
            scoreTeamA = 10,
            scoreTeamB = 10,
            teamAPlayersOnCourt = 6,
            teamBPlayersOnCourt = 7
        )

        val (updated, event) = engine.processSuccessfulRaid(
            currentMatch = initialMatch,
            raiderTeam = "A",
            touches = 1,
            gotBonus = false,
            raiderName = "பாண்டித்துரை",
            officialId = "referee_1"
        )

        assertEquals(11, updated.scoreTeamA)
        assertEquals(10, updated.scoreTeamB)
        assertEquals(6, updated.teamBPlayersOnCourt) // 1 out
        assertEquals("TOUCH_POINT", event.eventType)
        assertEquals(1, event.points)
    }

    @Test
    fun testRaidWithBonusPoint() {
        val initialMatch = Match(
            scoreTeamA = 5,
            scoreTeamB = 5,
            teamBPlayersOnCourt = 6
        )

        val (updated, event) = engine.processSuccessfulRaid(
            currentMatch = initialMatch,
            raiderTeam = "A",
            touches = 1,
            gotBonus = true,
            raiderName = "பாண்டி",
            officialId = "ref_1"
        )

        // 1 touch + 1 bonus = 2 points
        assertEquals(7, updated.scoreTeamA)
        assertEquals(2, event.points)
    }

    @Test
    fun testRaidTriggeringAllOut() {
        val initialMatch = Match(
            scoreTeamA = 15,
            scoreTeamB = 12,
            teamBPlayersOnCourt = 2
        )

        val (updated, event) = engine.processSuccessfulRaid(
            currentMatch = initialMatch,
            raiderTeam = "A",
            touches = 2,
            gotBonus = false,
            raiderName = "பாண்டி",
            officialId = "ref_1"
        )

        // 2 touches + 2 All Out bonus = 4 points
        assertEquals(19, updated.scoreTeamA)
        assertEquals(7, updated.teamBPlayersOnCourt) // All-Out revives all 7
        assertEquals(1, updated.teamAAllOuts)
        assertEquals("ALL_OUT", event.eventType)
    }

    @Test
    fun testRegularTackle() {
        val initialMatch = Match(
            scoreTeamA = 10,
            scoreTeamB = 10,
            teamAPlayersOnCourt = 5, // 5 defenders => Regular Tackle
            teamBPlayersOnCourt = 6
        )

        val (updated, event) = engine.processSuccessfulTackle(
            currentMatch = initialMatch,
            defendingTeam = "A",
            tacklerName = "முத்துவேல்",
            officialId = "ref_1"
        )

        assertEquals(11, updated.scoreTeamA)
        assertEquals(1, event.points)
        assertEquals("TACKLE_POINT", event.eventType)
    }

    @Test
    fun testSuperTackle() {
        val initialMatch = Match(
            scoreTeamA = 8,
            scoreTeamB = 12,
            teamAPlayersOnCourt = 3, // <= 3 defenders => Super Tackle!
            teamBPlayersOnCourt = 6
        )

        val (updated, event) = engine.processSuccessfulTackle(
            currentMatch = initialMatch,
            defendingTeam = "A",
            tacklerName = "முத்துவேல்",
            officialId = "ref_1"
        )

        // Super tackle = 2 points
        assertEquals(10, updated.scoreTeamA)
        assertEquals(2, event.points)
        assertEquals("SUPER_TACKLE", event.eventType)
    }

    @Test
    fun testScoreCorrection() {
        val initialMatch = Match(
            scoreTeamA = 20,
            scoreTeamB = 18
        )

        val (updated, event) = engine.applyScoreCorrection(
            currentMatch = initialMatch,
            teamToAdjust = "A",
            pointDelta = -1,
            reason = "Line referee error correction",
            officialId = "head_referee"
        )

        assertEquals(19, updated.scoreTeamA)
        assertTrue(event.isCorrection)
        assertEquals(-1, event.points)
    }
}
