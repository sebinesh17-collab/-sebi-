package com.sebinesh.kabaddihero.domain.rules

data class EligibilityResult(
    val isEligible: Boolean,
    val reasons: List<String>,
    val passedCriteria: List<String>,
    val warnings: List<String> = emptyList(),
    val missingData: List<String> = emptyList()
)
