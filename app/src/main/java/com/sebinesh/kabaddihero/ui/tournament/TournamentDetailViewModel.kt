package com.sebinesh.kabaddihero.ui.tournament

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebinesh.kabaddihero.core.di.ServiceLocator
import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.data.model.Registration
import com.sebinesh.kabaddihero.data.model.Report
import com.sebinesh.kabaddihero.data.model.Tournament
import com.sebinesh.kabaddihero.data.remote.ai.AiTournamentSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TournamentDetailUiState(
    val tournament: Tournament? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val registeredTeams: List<Registration> = emptyList(),
    val aiSummary: AiTournamentSummary? = null,
    val isAiSummaryLoading: Boolean = false,
    val reportSubmitted: Boolean = false,
    val registrationSuccessMessage: String? = null
)

class TournamentDetailViewModel(private val tournamentId: String) : ViewModel() {
    private val tournamentRepo = ServiceLocator.tournamentRepository
    private val reportRepo = ServiceLocator.reportRepository
    private val aiSummaryService = ServiceLocator.aiTournamentSummaryService

    private val _uiState = MutableStateFlow(TournamentDetailUiState(isLoading = true))
    val uiState: StateFlow<TournamentDetailUiState> = _uiState.asStateFlow()

    init {
        loadTournament()
    }

    fun loadTournament() {
        val t = tournamentRepo.getTournamentById(tournamentId)
        val isSaved = tournamentRepo.isTournamentSaved(tournamentId)
        val regs = tournamentRepo.getRegistrationsForTournament(tournamentId)

        _uiState.value = _uiState.value.copy(
            tournament = t,
            isLoading = false,
            isSaved = isSaved,
            registeredTeams = regs
        )

        // Generate AI summary if tournament loaded
        if (t != null && _uiState.value.aiSummary == null) {
            fetchAiSummary(t)
        }
    }

    fun toggleSave() {
        val saved = tournamentRepo.toggleSaveTournament(tournamentId)
        _uiState.value = _uiState.value.copy(isSaved = saved)
    }

    private fun fetchAiSummary(tournament: Tournament) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAiSummaryLoading = true)
            val res = aiSummaryService.generateSummary(tournament)
            if (res is Result.Success) {
                _uiState.value = _uiState.value.copy(aiSummary = res.data, isAiSummaryLoading = false)
            } else {
                _uiState.value = _uiState.value.copy(isAiSummaryLoading = false)
            }
        }
    }

    fun submitReport(reason: String, description: String) {
        viewModelScope.launch {
            val report = Report(
                reporterId = ServiceLocator.authRepository.currentUserState.value?.uid ?: "anon",
                targetId = tournamentId,
                targetType = "TOURNAMENT",
                reason = reason,
                description = description
            )
            reportRepo.submitReport(report)
            _uiState.value = _uiState.value.copy(reportSubmitted = true)
        }
    }

    fun registerTeam(teamName: String, captainName: String, phone: String, players: List<String>) {
        viewModelScope.launch {
            val reg = Registration(
                tournamentId = tournamentId,
                tournamentName = _uiState.value.tournament?.name ?: "",
                teamName = teamName,
                captainName = captainName,
                managerPhone = phone,
                playerNames = players
            )
            val res = tournamentRepo.registerTeam(reg)
            if (res is Result.Success) {
                _uiState.value = _uiState.value.copy(
                    registrationSuccessMessage = "Team registration submitted successfully!",
                    registeredTeams = tournamentRepo.getRegistrationsForTournament(tournamentId)
                )
            }
        }
    }
}
