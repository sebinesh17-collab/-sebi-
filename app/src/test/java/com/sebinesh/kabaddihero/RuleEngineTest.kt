package com.sebinesh.kabaddihero

import com.sebinesh.kabaddihero.data.model.Tournament
import com.sebinesh.kabaddihero.data.model.TournamentRules
import com.sebinesh.kabaddihero.domain.rules.RuleEngine
import org.junit.Assert.*
import org.junit.Test

class RuleEngineTest {

    @Test
    fun testSeniorMenStandardWeightLimit() {
        val tournament = Tournament(
            category = "Open",
            gender = "Men",
            weightLimitKg = null
        )
        val limit = RuleEngine.getEffectiveWeightLimit(tournament)
        assertEquals(85.0f, limit, 0.01f)
    }

    @Test
    fun testSeniorWomenStandardWeightLimit() {
        val tournament = Tournament(
            category = "Open",
            gender = "Women",
            weightLimitKg = null
        )
        val limit = RuleEngine.getEffectiveWeightLimit(tournament)
        assertEquals(75.0f, limit, 0.01f)
    }

    @Test
    fun testJuniorWeightLimit() {
        val boysTourney = Tournament(category = "Junior", gender = "Men", weightLimitKg = null)
        val girlsTourney = Tournament(category = "Junior", gender = "Women", weightLimitKg = null)

        assertEquals(70.0f, RuleEngine.getEffectiveWeightLimit(boysTourney), 0.01f)
        assertEquals(65.0f, RuleEngine.getEffectiveWeightLimit(girlsTourney), 0.01f)
    }

    @Test
    fun testSubJuniorWeightLimit() {
        val boysTourney = Tournament(category = "Sub-Junior", gender = "Men", weightLimitKg = null)
        val girlsTourney = Tournament(category = "Sub-Junior", gender = "Women", weightLimitKg = null)

        assertEquals(55.0f, RuleEngine.getEffectiveWeightLimit(boysTourney), 0.01f)
        assertEquals(50.0f, RuleEngine.getEffectiveWeightLimit(girlsTourney), 0.01f)
    }

    @Test
    fun testCustomTournamentWeightLimitOverridesDefault() {
        val customTourney = Tournament(
            category = "Open",
            gender = "Men",
            weightLimitKg = 80.0f
        )
        assertEquals(80.0f, RuleEngine.getEffectiveWeightLimit(customTourney), 0.01f)
    }

    @Test
    fun testBonusLineActivation() {
        val rules = TournamentRules(bonusLineActiveDefendersMin = 6)
        assertFalse(RuleEngine.isBonusLineActive(5, rules))
        assertTrue(RuleEngine.isBonusLineActive(6, rules))
        assertTrue(RuleEngine.isBonusLineActive(7, rules))
    }

    @Test
    fun testSuperTackleCondition() {
        val rules = TournamentRules(superTackleDefendersMax = 3)
        assertTrue(RuleEngine.isSuperTackleSituation(1, rules))
        assertTrue(RuleEngine.isSuperTackleSituation(2, rules))
        assertTrue(RuleEngine.isSuperTackleSituation(3, rules))
        assertFalse(RuleEngine.isSuperTackleSituation(4, rules))
        assertFalse(RuleEngine.isSuperTackleSituation(0, rules))
    }

    @Test
    fun testDoOrDieRaidCondition() {
        val rules = TournamentRules(emptyRaidsBeforeDoOrDie = 2)
        assertFalse(RuleEngine.isDoOrDieRaid(1, rules))
        assertTrue(RuleEngine.isDoOrDieRaid(2, rules))
        assertTrue(RuleEngine.isDoOrDieRaid(3, rules))
    }
}
