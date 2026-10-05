package com.sebinesh.kabaddihero.data.model

data class NotificationPrefs(
    val notifyNewTournamentsNearby: Boolean = true,
    val notifyDistrictTournaments: Boolean = true,
    val notifyLiveMatches: Boolean = true,
    val notifyRegistrationDeadlines: Boolean = true,
    val notifyOrganizerUpdates: Boolean = true,
    val quietHoursEnabled: Boolean = false,
    val quietHoursStartHour: Int = 22,
    val quietHoursEndHour: Int = 6
)
