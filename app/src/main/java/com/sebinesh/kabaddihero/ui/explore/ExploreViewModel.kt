package com.sebinesh.kabaddihero.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebinesh.kabaddihero.core.di.ServiceLocator
import com.sebinesh.kabaddihero.data.model.Tournament
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TournamentFilters(
    val selectedDistrict: String = "All",
    val selectedCategory: String = "All",
    val selectedGender: String = "All",
    val dateFilter: String = "All", // All, Today, Tomorrow, This Week
    val maxWeightKg: Float? = null,
    val minPrizeAmount: Double? = null,
    val maxEntryFee: Double? = null,
    val verifiedOnly: Boolean = false,
    val registrationOpenOnly: Boolean = false
)

data class ExploreUiState(
    val searchQuery: String = "",
    val isMapView: Boolean = false,
    val filters: TournamentFilters = TournamentFilters(),
    val allTournaments: List<Tournament> = emptyList(),
    val filteredTournaments: List<Tournament> = emptyList(),
    val isFilterSheetVisible: Boolean = false
)

class ExploreViewModel : ViewModel() {
    private val tournamentRepo = ServiceLocator.tournamentRepository

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            tournamentRepo.tournaments.collect { list ->
                _uiState.value = _uiState.value.copy(
                    allTournaments = list,
                    filteredTournaments = applyFilters(list, _uiState.value.searchQuery, _uiState.value.filters)
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            filteredTournaments = applyFilters(_uiState.value.allTournaments, query, _uiState.value.filters)
        )
    }

    fun setMapView(isMap: Boolean) {
        _uiState.value = _uiState.value.copy(isMapView = isMap)
    }

    fun setFilterSheetVisible(visible: Boolean) {
        _uiState.value = _uiState.value.copy(isFilterSheetVisible = visible)
    }

    fun applyNewFilters(newFilters: TournamentFilters) {
        _uiState.value = _uiState.value.copy(
            filters = newFilters,
            isFilterSheetVisible = false,
            filteredTournaments = applyFilters(_uiState.value.allTournaments, _uiState.value.searchQuery, newFilters)
        )
    }

    fun clearFilters() {
        applyNewFilters(TournamentFilters())
    }

    private fun applyFilters(
        list: List<Tournament>,
        query: String,
        filters: TournamentFilters
    ): List<Tournament> {
        return list.filter { t ->
            // Search query matching
            val matchesQuery = query.isBlank() ||
                    t.name.contains(query, ignoreCase = true) ||
                    t.district.contains(query, ignoreCase = true) ||
                    t.venueName.contains(query, ignoreCase = true) ||
                    t.village.contains(query, ignoreCase = true)

            // District filter
            val matchesDistrict = filters.selectedDistrict == "All" ||
                    t.district.equals(filters.selectedDistrict, ignoreCase = true)

            // Category filter
            val matchesCategory = filters.selectedCategory == "All" ||
                    t.category.equals(filters.selectedCategory, ignoreCase = true)

            // Gender filter
            val matchesGender = filters.selectedGender == "All" ||
                    t.gender.equals(filters.selectedGender, ignoreCase = true)

            // Verified filter
            val matchesVerified = !filters.verifiedOnly || t.verificationStatus == "VERIFIED"

            // Registration open filter
            val matchesReg = !filters.registrationOpenOnly || t.registrationStatus == "OPEN"

            // Weight filter
            val matchesWeight = filters.maxWeightKg == null ||
                    (t.weightLimitKg != null && t.weightLimitKg <= filters.maxWeightKg)

            // Prize filter
            val matchesPrize = filters.minPrizeAmount == null || t.prizeAmount >= filters.minPrizeAmount

            // Entry fee filter
            val matchesEntry = filters.maxEntryFee == null || t.entryFee <= filters.maxEntryFee

            matchesQuery && matchesDistrict && matchesCategory && matchesGender &&
                    matchesVerified && matchesReg && matchesWeight && matchesPrize && matchesEntry
        }
    }
}
