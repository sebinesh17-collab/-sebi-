package com.sebinesh.kabaddihero.data.repository

import com.sebinesh.kabaddihero.data.model.NotificationPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AppNotification(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "INFO", // TOURNAMENT, MATCH, SYSTEM
    val targetId: String? = null,
    val isRead: Boolean = false
)

class NotificationRepository {
    private val _notifications = MutableStateFlow<List<AppNotification>>(
        listOf(
            AppNotification(
                title = "New Tournament: Madurai Championship",
                message = "New tournament announced at Simmakkal Ground with ₹50,000 prize pool.",
                type = "TOURNAMENT",
                targetId = "tourney_madurai_01"
            ),
            AppNotification(
                title = "Live Match: Kovilpatti League",
                message = "Madurai Lions vs Nellai Tigers semi-final is live now!",
                type = "MATCH",
                targetId = "match_live_01"
            )
        )
    )
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    private val _prefs = MutableStateFlow(NotificationPrefs())
    val prefs: StateFlow<NotificationPrefs> = _prefs.asStateFlow()

    fun markAsRead(id: String) {
        _notifications.value = _notifications.value.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
    }

    fun updatePrefs(prefs: NotificationPrefs) {
        _prefs.value = prefs
    }
}
