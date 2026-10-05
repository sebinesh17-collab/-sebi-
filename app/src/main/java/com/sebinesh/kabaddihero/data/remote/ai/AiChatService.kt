package com.sebinesh.kabaddihero.data.remote.ai

import com.sebinesh.kabaddihero.core.constants.AppConstants
import com.sebinesh.kabaddihero.core.result.Result

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String, // "USER" or "AI"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

class AiChatService(private val geminiService: GeminiService) {

    private val systemInstruction = """
        You are Kabaddi Hero AI, an expert Kabaddi assistant created by Sebinesh.
        You answer Kabaddi-related questions using tournament-specific rules first, then the selected rule profile, then trusted reference information available to the application.
        Never invent rules.
        Never claim that a tournament is officially approved or certified unless verified.
        When a local tournament defines custom rules, explain those rules as tournament-specific rules.
        When information is missing, explicitly say that the information is unavailable.
        When asked whether a player is eligible, calculate only using the actual configured tournament rules.
        Always respond in clear, accurate, and professional English.
        For conflicting rules, clearly identify the conflict and recommend checking the event organizer's published rules.
        Do not provide false certainty.
    """.trimIndent()

    private val conversationHistory = mutableListOf<Pair<String, String>>()

    suspend fun sendMessage(
        userMessage: String,
        isComplexTask: Boolean = false,
        isFastTask: Boolean = false,
        contextTournamentInfo: String? = null
    ): Result<String> {
        val model = when {
            isComplexTask -> AppConstants.MODEL_GEMINI_PRO_THINKING
            isFastTask -> AppConstants.MODEL_GEMINI_FLASH_FAST
            else -> AppConstants.MODEL_GEMINI_FLASH_GENERAL
        }

        val enrichedPrompt = if (!contextTournamentInfo.isNullOrBlank()) {
            "Tournament Context:\n$contextTournamentInfo\n\nUser Question:\n$userMessage"
        } else {
            userMessage
        }

        val result = geminiService.generateContent(
            prompt = enrichedPrompt,
            modelName = model,
            systemInstruction = systemInstruction,
            enableHighThinking = isComplexTask,
            conversationHistory = conversationHistory
        )

        if (result is Result.Success) {
            conversationHistory.add("user" to userMessage)
            conversationHistory.add("model" to result.data)
        }

        return result
    }

    fun clearHistory() {
        conversationHistory.clear()
    }
}
