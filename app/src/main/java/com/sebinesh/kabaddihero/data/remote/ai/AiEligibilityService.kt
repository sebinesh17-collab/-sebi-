package com.sebinesh.kabaddihero.data.remote.ai

import com.sebinesh.kabaddihero.core.constants.AppConstants
import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.data.model.Tournament
import org.json.JSONObject

data class AiEligibilityResponse(
    val eligible: Boolean,
    val reason: String,
    val matchedRules: List<String>,
    val missingInformation: List<String>,
    val warnings: List<String>
)

class AiEligibilityService(private val geminiService: GeminiService) {

    suspend fun checkEligibility(
        playerAge: Int?,
        playerWeightKg: Float?,
        playerGender: String,
        tournament: Tournament
    ): Result<AiEligibilityResponse> {
        val prompt = """
            Check whether this player is eligible for the Kabaddi tournament according to the configured rules.
            Player details:
            - Age: ${playerAge ?: "Unknown"}
            - Weight: ${playerWeightKg?.let { "$it KG" } ?: "Unknown"}
            - Gender: $playerGender

            Tournament details:
            - Name: ${tournament.name}
            - Category: ${tournament.category}
            - Gender allowed: ${tournament.gender}
            - Weight limit: ${tournament.weightLimitKg?.let { "$it KG" } ?: "No limit"}
            - Min age: ${tournament.rules.minAgeYears ?: "None"}
            - Max age: ${tournament.rules.maxAgeYears ?: "None"}
            - Special restrictions: ${tournament.rules.specialRestrictions}

            Return JSON only:
            {
              "eligible": boolean,
              "reason": "Clear explanation in natural Tamil and English",
              "matchedRules": ["list of satisfied rules"],
              "missingInformation": ["any missing items like weight, dob, etc."],
              "warnings": ["any advisories e.g. close to weight boundary, weigh-in timing"]
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
                    val matched = mutableListOf<String>()
                    val mArr = json.optJSONArray("matchedRules")
                    if (mArr != null) {
                        for (i in 0 until mArr.length()) matched.add(mArr.getString(i))
                    }

                    val missing = mutableListOf<String>()
                    val misArr = json.optJSONArray("missingInformation")
                    if (misArr != null) {
                        for (i in 0 until misArr.length()) missing.add(misArr.getString(i))
                    }

                    val warnings = mutableListOf<String>()
                    val wArr = json.optJSONArray("warnings")
                    if (wArr != null) {
                        for (i in 0 until wArr.length()) warnings.add(wArr.getString(i))
                    }

                    Result.Success(
                        AiEligibilityResponse(
                            eligible = json.optBoolean("eligible", false),
                            reason = json.optString("reason", ""),
                            matchedRules = matched,
                            missingInformation = missing,
                            warnings = warnings
                        )
                    )
                } catch (e: Exception) {
                    Result.Error(e, "Error parsing eligibility evaluation.")
                }
            }
            is Result.Error -> Result.Error(response.exception, response.userMessage)
            is Result.Loading -> Result.Loading
        }
    }
}
