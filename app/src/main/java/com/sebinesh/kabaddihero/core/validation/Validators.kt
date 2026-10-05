package com.sebinesh.kabaddihero.core.validation

import java.util.regex.Pattern

object Validators {
    private val PHONE_PATTERN = Pattern.compile("^[+]?[0-9]{10,13}$")
    private val EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun isValidTournamentName(name: String?): Boolean {
        return !name.isNullOrBlank() && name.trim().length in 3..100
    }

    fun isValidPhone(phone: String?): Boolean {
        if (phone.isNullOrBlank()) return false
        val clean = phone.replace("\\s|-".toRegex(), "")
        return PHONE_PATTERN.matcher(clean).matches()
    }

    fun isValidEmail(email: String?): Boolean {
        if (email.isNullOrBlank()) return false
        return EMAIL_PATTERN.matcher(email.trim()).matches()
    }

    fun isValidWeight(weightKg: Float?): Boolean {
        return weightKg != null && weightKg in 30.0f..150.0f
    }

    fun isValidAge(age: Int?): Boolean {
        return age != null && age in 8..80
    }

    fun isValidPrize(prize: Double?): Boolean {
        return prize != null && prize >= 0.0
    }

    fun isValidEntryFee(entryFee: Double?): Boolean {
        return entryFee != null && entryFee >= 0.0
    }

    fun isValidCoordinates(lat: Double?, lng: Double?): Boolean {
        if (lat == null || lng == null) return false
        return lat in -90.0..90.0 && lng in -180.0..180.0
    }
}
