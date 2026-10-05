package com.sebinesh.kabaddihero.data.remote.ai

import com.sebinesh.kabaddihero.core.constants.AppConstants
import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.data.model.Tournament
import org.json.JSONObject

data class AiTournamentSummary(
    val shortTamilSummary: String,
    val shortEnglishSummary: String,
    val rulesSummary: String,
    val eligibilitySummary: String,
    val importantDates: String
)

class AiTournamentSummaryService(private val geminiService: GeminiService) {

    suspend fun generateSummary(tournament: Tournament): Result<AiTournamentSummary> {
        val prompt = """
            Generate concise, promotional, accurate tournament summaries for Kabaddi Hero.
            Use only factual values provided:
            - Name: ${tournament.name}
            - District & Venue: ${tournament.district}, ${tournament.venueName}
            - Date & Time: ${tournament.date}, ${tournament.startTime}
            - Entry Fee: ₹${tournament.entryFee}
            - Prize: ₹${tournament.prizeAmount} (${tournament.prizeDescription})
            - Category: ${tournament.category} ${tournament.gender}
            - Weight Limit: ${tournament.weightLimitKg ?: "Open"} KG
            - Reg Deadline: ${tournament.registrationDeadline}

            Output JSON only:
            {
              "shortTamilSummary": "2-3 sentences exciting Tamil summary",
              "shortEnglishSummary": "2-3 sentences clear English summary",
              "rulesSummary": "Key rules highlight",
              "eligibilitySummary": "Who can participate",
              "importantDates": "Key date milestones"
            }
        """.trimIndent()

        val response = geminiService.generateContent(
            prompt = prompt,
            modelName = AppConstants.MODEL_GEMINI_FLASH_GENERAL,
            jsonResponse = true
        )

        return when (response) {
            is Result.Success -> {
                try {
                    val clean = response.data.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
                    val json = JSONObject(clean)
                    Result.Success(
                        AiTournamentSummary(
                            shortTamilSummary = json.optString("shortTamilSummary"),
                            shortEnglishSummary = json.optString("shortEnglishSummary"),
                            rulesSummary = json.optString("rulesSummary"),
                            eligibilitySummary = json.optString("eligibilitySummary"),
                            importantDates = json.optString("importantDates")
                        )
                    )
                } catch (e: Exception) {
                    Result.Error(e, "Could not parse AI summary.")
                }
            }
            is Result.Error -> Result.Error(response.exception, response.userMessage)
            is Result.Loading -> Result.Loading
        }
    }
}
