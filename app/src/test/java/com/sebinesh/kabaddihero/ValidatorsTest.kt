package com.sebinesh.kabaddihero

import com.sebinesh.kabaddihero.core.validation.Validators
import org.junit.Assert.*
import org.junit.Test

class ValidatorsTest {

    @Test
    fun testValidPhoneNumbers() {
        assertTrue(Validators.isValidPhone("9876543210"))
        assertTrue(Validators.isValidPhone("+919876543210"))
        assertFalse(Validators.isValidPhone("123"))
        assertFalse(Validators.isValidPhone("abcdefghij"))
    }

    @Test
    fun testValidEmails() {
        assertTrue(Validators.isValidEmail("sebinesh15@gmail.com"))
        assertTrue(Validators.isValidEmail("admin@kabaddihero.app"))
        assertFalse(Validators.isValidEmail("invalid_email"))
        assertFalse(Validators.isValidEmail("@nodomain.com"))
    }

    @Test
    fun testValidWeights() {
        assertTrue(Validators.isValidWeight(75.0f))
        assertTrue(Validators.isValidWeight(85.0f))
        assertFalse(Validators.isValidWeight(20.0f)) // too low
        assertFalse(Validators.isValidWeight(200.0f)) // too high
    }

    @Test
    fun testValidTournamentNames() {
        assertTrue(Validators.isValidTournamentName("மதுரை மீனாட்சி கபடி சாம்பியன்ஷிப்"))
        assertFalse(Validators.isValidTournamentName("AB")) // too short
        assertFalse(Validators.isValidTournamentName(""))
    }

    @Test
    fun testValidCoordinates() {
        assertTrue(Validators.isValidCoordinates(9.9252, 78.1198)) // Madurai
        assertFalse(Validators.isValidCoordinates(100.0, 78.0)) // Invalid lat
    }
}
