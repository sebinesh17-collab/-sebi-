package com.sebinesh.kabaddihero.data.remote.ai

import android.graphics.Bitmap
import com.sebinesh.kabaddihero.core.constants.AppConstants
import com.sebinesh.kabaddihero.core.logging.Logger
import com.sebinesh.kabaddihero.core.result.Result
import org.json.JSONObject

data class ExtractedPosterData(
    val tournamentName: String = "",
    val date: String = "",
    val startTime: String = "",
    val venue: String = "",
    val district: String = "",
    val category: String = "Open",
    val gender: String = "Men",
    val ageLimit: String = "",
    val weightLimit: String = "",
    val entryFee: String = "",
    val prize: String = "",
    val registrationDeadline: String = "",
    val contact: String = "",
    val rules: List<String> = emptyList(),
    val rawJson: String = ""
)

class AiPosterExtractionService(private val geminiService: GeminiService) {

    suspend fun extractFromPoster(posterBitmap: Bitmap): Result<ExtractedPosterData> {
        val prompt = """
            Analyze this Kabaddi tournament poster image (often in Tamil or English).
            Extract the following fields accurately into a JSON object:
            {
              "tournamentName": "Name of the tournament",
              "date": "Date of event in YYYY-MM-DD if recognizable",
              "startTime": "Start time e.g. 08:00 AM",
              "venue": "Ground / Stadium / Sports Complex",
              "district": "District e.g. Madurai, Tirunelveli, Chennai, etc.",
              "category": "Open / Junior / Sub-Junior",
              "gender": "Men / Women / Mixed",
              "ageLimit": "Age criteria if mentioned",
              "weightLimit": "Weight limit in kg e.g. 80 KG",
              "entryFee": "Entry fee numeric e.g. 500",
              "prize": "First prize or summary of prizes e.g. 50000",
              "registrationDeadline": "Last date for entry",
              "contact": "Contact phone number or WhatsApp",
              "rules": ["Array of any specific tournament rules mentioned"]
            }
            Do not invent or hallucinate missing information; leave unknown string fields blank.
        """.trimIndent()

        val response = geminiService.generateContent(
            prompt = prompt,
            modelName = AppConstants.MODEL_GEMINI_FLASH_GENERAL,
            jsonResponse = true,
            imageBitmap = posterBitmap
        )

        return when (response) {
            is Result.Success -> {
                try {
                    val raw = response.data
                    val cleanJson = raw.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
                    val json = JSONObject(cleanJson)
                    val rulesList = mutableListOf<String>()
                    val rArr = json.optJSONArray("rules")
                    if (rArr != null) {
                        for (i in 0 until rArr.length()) {
                            rulesList.add(rArr.getString(i))
                        }
                    }

                    val extracted = ExtractedPosterData(
                        tournamentName = json.optString("tournamentName"),
                        date = json.optString("date"),
                        startTime = json.optString("startTime"),
                        venue = json.optString("venue"),
                        district = json.optString("district"),
                        category = json.optString("category", "Open"),
                        gender = json.optString("gender", "Men"),
                        ageLimit = json.optString("ageLimit"),
                        weightLimit = json.optString("weightLimit"),
                        entryFee = json.optString("entryFee"),
                        prize = json.optString("prize"),
                        registrationDeadline = json.optString("registrationDeadline"),
                        contact = json.optString("contact"),
                        rules = rulesList,
                        rawJson = cleanJson
                    )
                    Result.Success(extracted)
                } catch (e: Exception) {
                    Logger.e("AiPosterExtractionService", "Failed to parse poster JSON: ${e.message}")
                    Result.Error(e, "Could not parse poster details. Please enter manually.")
                }
            }
            is Result.Error -> Result.Error(response.exception, response.userMessage)
            is Result.Loading -> Result.Loading
        }
    }
}
