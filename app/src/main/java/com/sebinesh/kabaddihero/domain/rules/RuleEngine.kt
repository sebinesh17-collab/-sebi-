package com.sebinesh.kabaddihero.domain.rules

import com.sebinesh.kabaddihero.data.model.Tournament
import com.sebinesh.kabaddihero.data.model.TournamentRules

object RuleEngine {

    fun getEffectiveWeightLimit(tournament: Tournament): Float {
        // Tournament custom weight takes strict precedence
        if (tournament.weightLimitKg != null && tournament.weightLimitKg > 0) {
            return tournament.weightLimitKg
        }
        if (tournament.rules.weightLimitKg != null && tournament.rules.weightLimitKg > 0) {
            return tournament.rules.weightLimitKg
        }

        // Standard IKF Baseline defaults
        return when (tournament.category.lowercase()) {
            "junior" -> if (tournament.gender.equals("women", true)) 65.0f else 70.0f
            "sub-junior", "sub_junior" -> if (tournament.gender.equals("women", true)) 50.0f else 55.0f
            else -> if (tournament.gender.equals("women", true)) 75.0f else 85.0f
        }
    }

    fun isBonusLineActive(defendersOnCourt: Int, rules: TournamentRules): Boolean {
        return defendersOnCourt >= rules.bonusLineActiveDefendersMin
    }

    fun isSuperTackleSituation(defendersOnCourt: Int, rules: TournamentRules): Boolean {
        return defendersOnCourt in 1..rules.superTackleDefendersMax
    }

    fun isDoOrDieRaid(consecutiveEmptyRaids: Int, rules: TournamentRules): Boolean {
        return consecutiveEmptyRaids >= rules.emptyRaidsBeforeDoOrDie
    }

    fun calculateTotalMatchSeconds(rules: TournamentRules): Int {
        return rules.matchDurationMinutes * 60
    }

    fun calculateHalfSeconds(rules: TournamentRules): Int {
        return rules.halfDurationMinutes * 60
    }
}
