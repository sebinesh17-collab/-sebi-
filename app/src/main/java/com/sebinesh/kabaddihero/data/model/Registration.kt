package com.sebinesh.kabaddihero.data.model

data class Registration(
    val registrationId: String = "",
    val tournamentId: String = "",
    val tournamentName: String = "",
    val teamId: String = "",
    val teamName: String = "",
    val captainId: String = "",
    val captainName: String = "",
    val managerPhone: String = "",
    val playerNames: List<String> = emptyList(),
    val playerAges: List<Int> = emptyList(),
    val playerWeights: List<Float> = emptyList(),
    val category: String = "Open",
    val status: String = "PENDING", // PENDING, APPROVED, REJECTED, WAITLISTED, CANCELLED
    val paymentStatus: String = "NOT_REQUIRED", // NOT_REQUIRED, UNPAID, MARKED_PAID
    val notes: String = "",
    val submittedAt: Long = System.currentTimeMillis()
)
