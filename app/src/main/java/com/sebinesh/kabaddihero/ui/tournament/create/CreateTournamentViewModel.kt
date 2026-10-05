package com.sebinesh.kabaddihero.ui.tournament.create

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebinesh.kabaddihero.core.di.ServiceLocator
import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.data.model.Tournament
import com.sebinesh.kabaddihero.data.model.TournamentRules
import com.sebinesh.kabaddihero.data.remote.ai.ExtractedPosterData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CreateTournamentFormState(
    val currentStep: Int = 1,
    // Step 1: Basic
    val name: String = "",
    val description: String = "",
    val category: String = "Open",
    val gender: String = "Men",
    val ageCategory: String = "Open",
    val weightLimitKg: String = "85",
    val entryFee: String = "500",
    val prizeAmount: String = "25000",
    val prizeDescription: String = "1st: ₹25,000 + Trophy",
    val teamLimit: String = "32",
    val registrationDeadline: String = "2026-10-10",

    // Step 2: Date & Time
    val date: String = "2026-10-15",
    val startTime: String = "08:00 AM",
    val endTime: String = "08:00 PM",
    val checkInTime: String = "07:00 AM",
    val weighInTime: String = "07:30 AM",

    // Step 3: Venue
    val venueName: String = "",
    val village: String = "",
    val town: String = "",
    val taluk: String = "",
    val district: String = "Madurai",
    val address: String = "",
    val latitude: Double? = 9.9252,
    val longitude: Double? = 78.1198,
    val directions: String = "",

    // Step 4: Rules
    val selectedProfileId: String = "IKF_STANDARD",
    val playersOnCourt: String = "7",
    val substitutes: String = "5",
    val matchDurationMins: String = "40",
    val raidDurationSecs: String = "30",
    val specialRestrictions: String = "",
    val hasCustomOverrides: Boolean = false,

    // Step 5: Organizer
    val organizerName: String = "",
    val organizerPhone: String = "",
    val organizerWhatsapp: String = "",
    val organizerEmail: String = "",
    val organizationName: String = "",

    // Step 6: Evidence
    val evidencePhotoUrls: List<String> = emptyList(),

    // State flags
    val possibleDuplicates: List<Tournament> = emptyList(),
    val isPublishing: Boolean = false,
    val isExtractingPoster: Boolean = false,
    val publishSuccessId: String? = null,
    val errorMessage: String? = null
)

class CreateTournamentViewModel : ViewModel() {
    private val tournamentRepo = ServiceLocator.tournamentRepository
    private val authRepo = ServiceLocator.authRepository
    private val posterExtractionService = ServiceLocator.aiPosterExtractionService

    private val _formState = MutableStateFlow(CreateTournamentFormState())
    val formState: StateFlow<CreateTournamentFormState> = _formState.asStateFlow()

    init {
        // Pre-fill organizer details from logged in user if available
        authRepo.currentUserState.value?.let { user ->
            _formState.value = _formState.value.copy(
                organizerName = user.displayName,
                organizerPhone = user.phoneNumber,
                organizerEmail = user.email,
                district = if (user.district.isNotBlank()) user.district else "Madurai"
            )
        }
    }

    fun nextStep() {
        if (_formState.value.currentStep == 6) {
            // Check for duplicates before preview
            checkForDuplicates()
        }
        if (_formState.value.currentStep < 7) {
            _formState.value = _formState.value.copy(currentStep = _formState.value.currentStep + 1)
        }
    }

    fun prevStep() {
        if (_formState.value.currentStep > 1) {
            _formState.value = _formState.value.copy(currentStep = _formState.value.currentStep - 1)
        }
    }

    fun updateForm(update: (CreateTournamentFormState) -> CreateTournamentFormState) {
        _formState.value = update(_formState.value)
    }

    fun importFromPoster(bitmap: Bitmap) {
        viewModelScope.launch {
            _formState.value = _formState.value.copy(isExtractingPoster = true)
            val res = posterExtractionService.extractFromPoster(bitmap)
            _formState.value = _formState.value.copy(isExtractingPoster = false)
            if (res is Result.Success) {
                applyExtractedPosterData(res.data)
            }
        }
    }

    private fun applyExtractedPosterData(data: ExtractedPosterData) {
        _formState.value = _formState.value.copy(
            name = if (data.tournamentName.isNotBlank()) data.tournamentName else _formState.value.name,
            date = if (data.date.isNotBlank()) data.date else _formState.value.date,
            startTime = if (data.startTime.isNotBlank()) data.startTime else _formState.value.startTime,
            venueName = if (data.venue.isNotBlank()) data.venue else _formState.value.venueName,
            district = if (data.district.isNotBlank()) data.district else _formState.value.district,
            category = if (data.category.isNotBlank()) data.category else _formState.value.category,
            gender = if (data.gender.isNotBlank()) data.gender else _formState.value.gender,
            weightLimitKg = if (data.weightLimit.isNotBlank()) data.weightLimit.replace("[^0-9.]".toRegex(), "") else _formState.value.weightLimitKg,
            entryFee = if (data.entryFee.isNotBlank()) data.entryFee.replace("[^0-9.]".toRegex(), "") else _formState.value.entryFee,
            prizeAmount = if (data.prize.isNotBlank()) data.prize.replace("[^0-9.]".toRegex(), "") else _formState.value.prizeAmount,
            organizerPhone = if (data.contact.isNotBlank()) data.contact else _formState.value.organizerPhone
        )
    }

    private fun checkForDuplicates() {
        viewModelScope.launch {
            val state = _formState.value
            val duplicates = tournamentRepo.checkForDuplicates(state.name, state.date, state.district)
            _formState.value = _formState.value.copy(possibleDuplicates = duplicates)
        }
    }

    fun publishTournament() {
        val s = _formState.value
        val user = authRepo.currentUserState.value

        val tournament = Tournament(
            name = s.name.trim(),
            description = s.description.trim(),
            organizerId = user?.uid ?: "user_org",
            organizerName = s.organizerName.trim(),
            organizerPhone = s.organizerPhone.trim(),
            organizerWhatsapp = s.organizerWhatsapp.ifBlank { s.organizerPhone }.trim(),
            organizerEmail = s.organizerEmail.trim(),
            organizationName = s.organizationName.trim(),
            category = s.category,
            gender = s.gender,
            ageCategory = s.ageCategory,
            weightLimitKg = s.weightLimitKg.toFloatOrNull() ?: 85f,
            date = s.date,
            startTime = s.startTime,
            endTime = s.endTime,
            registrationDeadline = s.registrationDeadline,
            checkInTime = s.checkInTime,
            weighInTime = s.weighInTime,
            venueName = s.venueName.trim(),
            village = s.village.trim(),
            town = s.town.trim(),
            taluk = s.taluk.trim(),
            district = s.district.trim(),
            entryFee = s.entryFee.toDoubleOrNull() ?: 500.0,
            prizeAmount = s.prizeAmount.toDoubleOrNull() ?: 25000.0,
            prizeDescription = s.prizeDescription,
            teamLimit = s.teamLimit.toIntOrNull() ?: 32,
            rules = TournamentRules(
                rulesProfileId = s.selectedProfileId,
                playersOnCourt = s.playersOnCourt.toIntOrNull() ?: 7,
                substitutesCount = s.substitutes.toIntOrNull() ?: 5,
                matchDurationMinutes = s.matchDurationMins.toIntOrNull() ?: 40,
                raidDurationSeconds = s.raidDurationSecs.toIntOrNull() ?: 30,
                specialRestrictions = s.specialRestrictions
            ),
            verificationStatus = "PENDING_REVIEW"
        )

        viewModelScope.launch {
            _formState.value = _formState.value.copy(isPublishing = true)
            val res = tournamentRepo.createTournament(tournament)
            _formState.value = _formState.value.copy(isPublishing = false)
            if (res is Result.Success) {
                _formState.value = _formState.value.copy(publishSuccessId = res.data)
            } else if (res is Result.Error) {
                _formState.value = _formState.value.copy(errorMessage = res.userMessage)
            }
        }
    }
}
