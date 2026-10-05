package com.sebinesh.kabaddihero.data.model

data class Report(
    val reportId: String = "",
    val reporterId: String = "",
    val targetId: String = "",
    val targetType: String = "TOURNAMENT", // TOURNAMENT, USER, TEAM, MATCH
    val reason: String = "", // FAKE_TOURNAMENT, INCORRECT_INFO, SCAM, INAPPROPRIATE_IMAGE, SPAM, MISLEADING, OTHER
    val description: String = "",
    val evidenceUrl: String = "",
    val status: String = "OPEN", // OPEN, RESOLVED, DISMISSED
    val actionTaken: String = "",
    val resolvedBy: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
