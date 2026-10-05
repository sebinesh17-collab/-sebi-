package com.sebinesh.kabaddihero.data.model

data class MatchEvent(
    val eventId: String = "",
    val matchId: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val createdBy: String = "",
    val eventType: String = "", // RAID_START, TOUCH_POINT, TACKLE_POINT, BONUS_POINT, ALL_OUT, REVIVAL, TECHNICAL_POINT, TIMEOUT, SUBSTITUTION, HALF_START, HALF_END, MATCH_START, MATCH_END, CORRECTION
    val team: String = "A", // A or B
    val player: String = "",
    val points: Int = 1,
    val description: String = "",
    val isCorrection: Boolean = false,
    val referencedEventId: String? = null,
    val metadata: Map<String, String> = emptyMap()
)
