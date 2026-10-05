package com.sebinesh.kabaddihero.domain.rules

import com.sebinesh.kabaddihero.data.model.Player
import com.sebinesh.kabaddihero.data.model.Tournament

object EligibilityChecker {

    fun checkEligibility(
        player: Player,
        tournament: Tournament
    ): EligibilityResult {
        val passed = mutableListOf<String>()
        val failures = mutableListOf<String>()
        val warnings = mutableListOf<String>()
        val missing = mutableListOf<String>()

        // 1. Gender check
        if (tournament.gender.isNotBlank() && !tournament.gender.equals("Mixed", ignoreCase = true)) {
            if (player.gender.equals(tournament.gender, ignoreCase = true)) {
                passed.add("Gender category matched (${player.gender})")
            } else {
                failures.add("Gender mismatch (Expected ${tournament.gender}, but player is ${player.gender})")
            }
        }

        // 2. Weight check
        val weightLimit = RuleEngine.getEffectiveWeightLimit(tournament)
        if (player.weightKg <= 0f) {
            missing.add("Player weight missing")
        } else if (player.weightKg > weightLimit) {
            failures.add("Overweight! Tournament limit: ${weightLimit} KG, Player weight: ${player.weightKg} KG")
        } else {
            passed.add("Weight is within limit (${player.weightKg} KG <= ${weightLimit} KG)")
            if (weightLimit - player.weightKg <= 2.0f) {
                warnings.add("Warning: Very close to weight limit. Verify at official weigh-in.")
            }
        }

        // 3. Age check
        val maxAge = tournament.rules.maxAgeYears
        val minAge = tournament.rules.minAgeYears

        if (player.calculatedAge <= 0) {
            missing.add("Date of birth / age is missing")
        } else {
            if (maxAge != null && player.calculatedAge > maxAge) {
                failures.add("Over age limit! Maximum limit: $maxAge years, Player age: ${player.calculatedAge}")
            } else if (minAge != null && player.calculatedAge < minAge) {
                failures.add("Under age limit! Minimum limit: $minAge years, Player age: ${player.calculatedAge}")
            } else {
                passed.add("Age criteria verified (${player.calculatedAge} years)")
            }
        }

        val isEligible = failures.isEmpty() && missing.isEmpty()
        return EligibilityResult(
            isEligible = isEligible,
            reasons = failures,
            passedCriteria = passed,
            warnings = warnings,
            missingData = missing
        )
    }
}
