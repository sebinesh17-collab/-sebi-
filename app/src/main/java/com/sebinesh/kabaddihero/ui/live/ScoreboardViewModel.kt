package com.sebinesh.kabaddihero.ui.live

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebinesh.kabaddihero.core.di.ServiceLocator
import com.sebinesh.kabaddihero.data.model.Match
import com.sebinesh.kabaddihero.data.model.MatchEvent
import com.sebinesh.kabaddihero.domain.match.LiveMatchEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ScoreboardUiState(
    val match: Match? = null,
    val events: List<MatchEvent> = emptyList(),
    val isLoading: Boolean = false,
    val isTimerRunning: Boolean = false,
    val raidTimerRunning: Boolean = false
)

class ScoreboardViewModel(private val matchId: String) : ViewModel() {
    private val matchRepo = ServiceLocator.matchRepository
    private val matchEngine = LiveMatchEngine()

    private val _uiState = MutableStateFlow(ScoreboardUiState(isLoading = true))
    val uiState: StateFlow<ScoreboardUiState> = _uiState.asStateFlow()

    init {
        loadMatch()
    }

    fun loadMatch() {
        val m = matchRepo.getMatchById(matchId)
        val evts = matchRepo.getEventsForMatch(matchId)
        _uiState.value = _uiState.value.copy(
            match = m,
            events = evts,
            isLoading = false
        )
    }

    fun recordRaid(team: String, touches: Int, bonus: Boolean, raiderName: String) {
        val current = _uiState.value.match ?: return
        val (updated, event) = matchEngine.processSuccessfulRaid(
            currentMatch = current,
            raiderTeam = team,
            touches = touches,
            gotBonus = bonus,
            raiderName = raiderName.ifBlank { "Raider" },
            officialId = ServiceLocator.authRepository.currentUserState.value?.uid ?: "referee_01"
        )
        viewModelScope.launch {
            matchRepo.updateMatch(updated)
            matchRepo.addEvent(event)
            _uiState.value = _uiState.value.copy(
                match = updated,
                events = listOf(event) + _uiState.value.events
            )
        }
    }

    fun recordTackle(team: String, tacklerName: String) {
        val current = _uiState.value.match ?: return
        val (updated, event) = matchEngine.processSuccessfulTackle(
            currentMatch = current,
            defendingTeam = team,
            tacklerName = tacklerName.ifBlank { "Defender" },
            officialId = ServiceLocator.authRepository.currentUserState.value?.uid ?: "referee_01"
        )
        viewModelScope.launch {
            matchRepo.updateMatch(updated)
            matchRepo.addEvent(event)
            _uiState.value = _uiState.value.copy(
                match = updated,
                events = listOf(event) + _uiState.value.events
            )
        }
    }

    fun applyCorrection(team: String, delta: Int, reason: String) {
        val current = _uiState.value.match ?: return
        val (updated, event) = matchEngine.applyScoreCorrection(
            currentMatch = current,
            teamToAdjust = team,
            pointDelta = delta,
            reason = reason,
            officialId = ServiceLocator.authRepository.currentUserState.value?.uid ?: "referee_01"
        )
        viewModelScope.launch {
            matchRepo.updateMatch(updated)
            matchRepo.addEvent(event)
            _uiState.value = _uiState.value.copy(
                match = updated,
                events = listOf(event) + _uiState.value.events
            )
        }
    }

    fun toggleRaidTimer() {
        _uiState.value = _uiState.value.copy(
            raidTimerRunning = !_uiState.value.raidTimerRunning
        )
    }

    fun resetRaidTimer() {
        val m = _uiState.value.match?.copy(raidTimeRemainingSeconds = 30)
        _uiState.value = _uiState.value.copy(match = m, raidTimerRunning = false)
    }
}
