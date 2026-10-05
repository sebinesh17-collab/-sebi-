package com.sebinesh.kabaddihero.data.model

data class Player(
    val playerId: String = "",
    val userId: String = "",
    val name: String = "",
    val profilePhotoUrl: String = "",
    val dateOfBirth: String = "2000-01-01",
    val calculatedAge: Int = 24,
    val gender: String = "Men",
    val weightKg: Float = 78.0f,
    val village: String = "",
    val town: String = "",
    val district: String = "Tirunelveli",
    val state: String = "Tamil Nadu",
    val position: String = "Raider", // Raider, Defender, All-Rounder
    val defenderRole: String = "Right Corner", // Left Corner, Right Corner, Cover, None
    val teamId: String = "",
    val teamName: String = "",
    val bio: String = "",
    val achievements: List<String> = emptyList(),
    val matchesPlayed: Int = 0,
    val matchesWon: Int = 0,
    val raidPoints: Int = 0,
    val tacklePoints: Int = 0,
    val totalPoints: Int = 0,
    val verificationState: String = "VERIFIED"
)
