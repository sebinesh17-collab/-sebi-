package com.sebinesh.kabaddihero.data.model

enum class UserRole {
    USER,
    PLAYER,
    TEAM_MANAGER,
    ORGANIZER,
    REFEREE,
    MODERATOR,
    ADMIN,
    SUPER_ADMIN;

    fun isOrganizerOrStaff(): Boolean =
        this == ORGANIZER || this == MODERATOR || this == ADMIN || this == SUPER_ADMIN

    fun isRefereeOrStaff(): Boolean =
        this == REFEREE || this == ORGANIZER || this == MODERATOR || this == ADMIN || this == SUPER_ADMIN

    fun isStaff(): Boolean =
        this == MODERATOR || this == ADMIN || this == SUPER_ADMIN
}
