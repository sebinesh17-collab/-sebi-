package com.sebinesh.kabaddihero

import com.sebinesh.kabaddihero.data.model.Player
import com.sebinesh.kabaddihero.data.model.Tournament
import com.sebinesh.kabaddihero.data.model.TournamentRules
import com.sebinesh.kabaddihero.domain.rules.EligibilityChecker
import org.junit.Assert.*
import org.junit.Test

class EligibilityCheckerTest {

    @Test
    fun testEligiblePlayer() {
        val player = Player(
            name = "பாண்டி",
            gender = "Men",
            weightKg = 80.0f,
            calculatedAge = 22
        )
        val tournament = Tournament(
            category = "Open",
            gender = "Men",
            weightLimitKg = 85.0f
        )

        val result = EligibilityChecker.checkEligibility(player, tournament)
        assertTrue(result.isEligible)
        assertTrue(result.reasons.isEmpty())
    }

    @Test
    fun testOverweightPlayer() {
        val player = Player(
            name = "ராமு",
            gender = "Men",
            weightKg = 87.0f,
            calculatedAge = 24
        )
        val tournament = Tournament(
            category = "Open",
            gender = "Men",
            weightLimitKg = 85.0f
        )

        val result = EligibilityChecker.checkEligibility(player, tournament)
        assertFalse(result.isEligible)
        assertTrue(result.reasons.any { it.contains("அதிக எடை") })
    }

    @Test
    fun testJuniorAgeLimitFailure() {
        val player = Player(
            name = "சுரேஷ்",
            gender = "Men",
            weightKg = 68.0f,
            calculatedAge = 21 // Junior max age is 20
        )
        val tournament = Tournament(
            category = "Junior",
            gender = "Men",
            rules = TournamentRules(maxAgeYears = 20)
        )

        val result = EligibilityChecker.checkEligibility(player, tournament)
        assertFalse(result.isEligible)
        assertTrue(result.reasons.any { it.contains("அதிக வயது") })
    }

    @Test
    fun testGenderMismatch() {
        val femalePlayer = Player(
            name = "காயத்ரி",
            gender = "Women",
            weightKg = 60.0f,
            calculatedAge = 20
        )
        val menTournament = Tournament(
            gender = "Men"
        )

        val result = EligibilityChecker.checkEligibility(femalePlayer, menTournament)
        assertFalse(result.isEligible)
        assertTrue(result.reasons.any { it.contains("பாலினம் பொருந்தவில்லை") })
    }

    @Test
    fun testMissingWeight() {
        val player = Player(
            name = "அருண்",
            gender = "Men",
            weightKg = 0f,
            calculatedAge = 22
        )
        val tournament = Tournament(
            category = "Open",
            gender = "Men"
        )

        val result = EligibilityChecker.checkEligibility(player, tournament)
        assertFalse(result.isEligible)
        assertTrue(result.missingData.any { it.contains("எடை விவரம் இல்லை") })
    }

    @Test
    fun testMissingAge() {
        val player = Player(
            name = "விவேக்",
            gender = "Men",
            weightKg = 75f,
            calculatedAge = 0
        )
        val tournament = Tournament(
            category = "Junior",
            gender = "Men"
        )

        val result = EligibilityChecker.checkEligibility(player, tournament)
        assertFalse(result.isEligible)
        assertTrue(result.missingData.any { it.contains("பிறந்த தேதி") })
    }

    @Test
    fun testWeightCloseToBoundaryGeneratesWarning() {
        val player = Player(
            name = "சங்கர்",
            gender = "Men",
            weightKg = 84.5f,
            calculatedAge = 24
        )
        val tournament = Tournament(
            category = "Open",
            gender = "Men",
            weightLimitKg = 85.0f
        )

        val result = EligibilityChecker.checkEligibility(player, tournament)
        assertTrue(result.isEligible)
        assertTrue(result.warnings.isNotEmpty())
    }
}
