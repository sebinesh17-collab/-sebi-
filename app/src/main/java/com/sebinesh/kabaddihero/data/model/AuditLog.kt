package com.sebinesh.kabaddihero.data.model

data class AuditLog(
    val logId: String = "",
    val actorId: String = "",
    val action: String = "",
    val targetType: String = "",
    val targetId: String = "",
    val oldValue: String = "",
    val newValue: String = "",
    val reason: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
