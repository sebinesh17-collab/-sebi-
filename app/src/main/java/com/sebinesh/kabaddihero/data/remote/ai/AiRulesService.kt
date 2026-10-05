package com.sebinesh.kabaddihero.data.remote.ai

import com.sebinesh.kabaddihero.core.constants.AppConstants
import com.sebinesh.kabaddihero.core.result.Result

class AiRulesService(private val geminiService: GeminiService) {

    enum class RuleExplanationType {
        EXPLAIN_SIMPLY,
        EXPLAIN_IN_TAMIL,
        EXAMPLE,
        COMMON_MISTAKE
    }

    suspend fun explainRule(
        ruleTopic: String,
        type: RuleExplanationType,
        ruleContext: String = ""
    ): Result<String> {
        val prompt = when (type) {
            RuleExplanationType.EXPLAIN_SIMPLY ->
                "Explain the Kabaddi rule concept '$ruleTopic' in simple, crystal-clear language with high clarity.\nBaseline / Context: $ruleContext"
            RuleExplanationType.EXPLAIN_IN_TAMIL ->
                "Explain the Kabaddi rule concept '$ruleTopic' in thorough detail with official scoring and referee guidelines.\nRule Context: $ruleContext"
            RuleExplanationType.EXAMPLE ->
                "Give a practical live match scenario example illustrating '$ruleTopic' in Kabaddi.\nContext: $ruleContext"
            RuleExplanationType.COMMON_MISTAKE ->
                "What are the most common referee mistakes or player fouls regarding '$ruleTopic' in Kabaddi?\nContext: $ruleContext"
        }

        return geminiService.generateContent(
            prompt = prompt,
            modelName = AppConstants.MODEL_GEMINI_FLASH_GENERAL,
            enableHighThinking = false
        )
    }
}
