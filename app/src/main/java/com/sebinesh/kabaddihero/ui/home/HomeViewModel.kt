package com.sebinesh.kabaddihero.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebinesh.kabaddihero.core.di.ServiceLocator
import com.sebinesh.kabaddihero.data.model.Match
import com.sebinesh.kabaddihero.data.model.Tournament
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val tournaments: List<Tournament> = emptyList(),
    val liveMatches: List<Match> = emptyList(),
    val savedTournamentIds: Set<String> = emptySet(),
    val selectedDistrict: String = "All",
    val searchQuery: String = "",
    val currentLanguage: String = "ta"
)

class HomeViewModel : ViewModel() {
    private val tournamentRepo = ServiceLocator.tournamentRepository
    private val matchRepo = ServiceLocator.matchRepository
    private val prefs = ServiceLocator.dataStorePrefs

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
        observeData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            tournamentRepo.loadTournaments()
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            tournamentRepo.tournaments.collect { list ->
                _uiState.value = _uiState.value.copy(tournaments = list)
            }
        }
        viewModelScope.launch {
            matchRepo.matches.collect { list ->
                _uiState.value = _uiState.value.copy(liveMatches = list.filter { it.status == "LIVE" })
            }
        }
        viewModelScope.launch {
            prefs.selectedLanguage.collect { lang ->
                _uiState.value = _uiState.value.copy(currentLanguage = lang)
            }
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            prefs.setLanguage(lang)
        }
    }

    fun toggleSave(tournamentId: String) {
        tournamentRepo.toggleSaveTournament(tournamentId)
        val saved = tournamentRepo.getSavedTournaments().map { it.tournamentId }.toSet()
        _uiState.value = _uiState.value.copy(savedTournamentIds = saved)
    }

    fun isSaved(tournamentId: String): Boolean {
        return tournamentRepo.isTournamentSaved(tournamentId)
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }
}
