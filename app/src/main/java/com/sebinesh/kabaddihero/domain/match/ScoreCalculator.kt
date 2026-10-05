package com.sebinesh.kabaddihero.domain.match

import com.sebinesh.kabaddihero.data.model.Match
import com.sebinesh.kabaddihero.data.model.MatchEvent
import com.sebinesh.kabaddihero.data.model.TournamentRules

object ScoreCalculator {

    fun calculateRaidPoints(
        touches: Int,
        gotBonus: Boolean,
        defendersOnCourt: Int,
        rules: TournamentRules = TournamentRules()
    ): Int {
        var points = touches
        if (gotBonus && defendersOnCourt >= rules.bonusLineActiveDefendersMin) {
            points += 1
        }
        return points
    }

    fun calculateTacklePoints(
        defendersOnCourt: Int,
        rules: TournamentRules = TournamentRules()
    ): Pair<Int, Boolean> {
        val isSuperTackle = defendersOnCourt in 1..rules.superTackleDefendersMax
        val points = if (isSuperTackle) rules.superTacklePoints else 1
        return Pair(points, isSuperTackle)
    }
}
