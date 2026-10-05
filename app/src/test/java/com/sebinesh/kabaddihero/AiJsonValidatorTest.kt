package com.sebinesh.kabaddihero

import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

class AiJsonValidatorTest {

    @Test
    fun testValidAiPosterJsonStructure() {
        val validJson = """
            {
              "tournamentName": "கோவில்பட்டி தீரன் கபடி லீக்",
              "date": "2026-09-24",
              "startTime": "06:00 PM",
              "venue": "நகராட்சி மைதானம்",
              "district": "Thoothukudi",
              "category": "Open",
              "gender": "Men",
              "weightLimit": "80 KG",
              "entryFee": "800",
              "prize": "40000",
              "registrationDeadline": "2026-09-23",
              "contact": "9876543210",
              "rules": ["IKF Standard", "Clay Court"]
            }
        """.trimIndent()

        val json = JSONObject(validJson)
        assertEquals("கோவில்பட்டி தீரன் கபடி லீக்", json.getString("tournamentName"))
        assertEquals("2026-09-24", json.getString("date"))
        assertEquals("800", json.getString("entryFee"))
        assertEquals(2, json.getJSONArray("rules").length())
    }

    @Test
    fun testInvalidOrMalformedJsonRecovery() {
        val malformedRaw = "```json\n{\"tournamentName\": \"தஞ்சை சாம்பியன்ஷிப்\"}\n```"
        val clean = malformedRaw.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
        val json = JSONObject(clean)
        assertEquals("தஞ்சை சாம்பியன்ஷிப்", json.getString("tournamentName"))
    }
}
