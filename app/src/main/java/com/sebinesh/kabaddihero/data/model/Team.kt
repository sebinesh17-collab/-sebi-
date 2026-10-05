package com.sebinesh.kabaddihero.data.model

data class Team(
    val teamId: String = "",
    val name: String = "",
    val logoUrl: String = "",
    val captainId: String = "",
    val captainName: String = "",
    val coachName: String = "",
    val managerId: String = "",
    val managerName: String = "",
    val managerPhone: String = "",
    val district: String = "Thanjavur",
    val village: String = "",
    val players: List<String> = emptyList(), // player IDs
    val substitutes: List<String> = emptyList(),
    val achievements: List<String> = emptyList(),
    val matchesPlayed: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val totalPoints: Int = 0,
    val teamDescription: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
