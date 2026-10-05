package com.sebinesh.kabaddihero.data.model

data class User(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val photoUrl: String = "",
    val role: UserRole = UserRole.USER,
    val preferredLanguage: String = "en",
    val district: String = "",
    val town: String = "",
    val notificationPreferences: NotificationPrefs = NotificationPrefs(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis()
)
