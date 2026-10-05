package com.sebinesh.kabaddihero.ui.ai

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebinesh.kabaddihero.core.di.ServiceLocator
import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.data.remote.ai.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

data class AiChatUiState(
    val messages: List<ChatMessage> = listOf(
        ChatMessage(
            sender = "AI",
            text = "Hello! I am your Kabaddi Hero AI assistant. Ask me anything about Kabaddi rules, tournaments, weight categories, or player eligibility."
        )
    ),
    val isLoading: Boolean = false,
    val isHighThinkingEnabled: Boolean = false,
    val isFastModeEnabled: Boolean = false,
    val errorMessage: String? = null,
    val isSpeaking: Boolean = false
)

class AiChatViewModel : ViewModel(), TextToSpeech.OnInitListener {
    private val aiChatService = ServiceLocator.aiChatService
    private var tts: TextToSpeech? = null

    private val _uiState = MutableStateFlow(AiChatUiState())
    val uiState: StateFlow<AiChatUiState> = _uiState.asStateFlow()

    fun initializeTts(context: Context) {
        if (tts == null) {
            tts = TextToSpeech(context.applicationContext, this)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.ENGLISH
        }
    }

    fun toggleHighThinking() {
        _uiState.value = _uiState.value.copy(
            isHighThinkingEnabled = !_uiState.value.isHighThinkingEnabled,
            isFastModeEnabled = false
        )
    }

    fun toggleFastMode() {
        _uiState.value = _uiState.value.copy(
            isFastModeEnabled = !_uiState.value.isFastModeEnabled,
            isHighThinkingEnabled = false
        )
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        val userMessage = ChatMessage(sender = "USER", text = userText.trim())
        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + userMessage,
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            val res = aiChatService.sendMessage(
                userMessage = userText.trim(),
                isComplexTask = _uiState.value.isHighThinkingEnabled,
                isFastTask = _uiState.value.isFastModeEnabled
            )

            _uiState.value = _uiState.value.copy(isLoading = false)

            if (res is Result.Success) {
                val aiMessage = ChatMessage(sender = "AI", text = res.data)
                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages + aiMessage
                )
            } else if (res is Result.Error) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = res.userMessage
                )
            }
        }
    }

    fun speakMessage(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts_msg")
    }

    fun clearHistory() {
        aiChatService.clearHistory()
        _uiState.value = AiChatUiState()
    }

    override fun onCleared() {
        tts?.stop()
        tts?.shutdown()
        super.onCleared()
    }
}
