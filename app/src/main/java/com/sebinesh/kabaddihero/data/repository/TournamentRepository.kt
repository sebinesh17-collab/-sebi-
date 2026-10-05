package com.sebinesh.kabaddihero.data.repository

import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.data.local.DemoDataProvider
import com.sebinesh.kabaddihero.data.model.Registration
import com.sebinesh.kabaddihero.data.model.Tournament
import com.sebinesh.kabaddihero.data.remote.firestore.FirestoreRefs
import com.sebinesh.kabaddihero.data.remote.firestore.TournamentRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class TournamentRepository(
    private val tournamentRemote: TournamentRemote
) {
    private val _inMemoryTournaments = MutableStateFlow<List<Tournament>>(emptyList())
    val tournaments: Flow<List<Tournament>> = _inMemoryTournaments.asStateFlow()

    private val savedTournamentIds = mutableSetOf<String>()
    private val registrations = mutableListOf<Registration>()

    suspend fun loadTournaments(): List<Tournament> {
        val remoteRes = tournamentRemote.getTournaments()
        return if (remoteRes is Result.Success) {
            _inMemoryTournaments.value = remoteRes.data
            remoteRes.data
        } else {
            _inMemoryTournaments.value = emptyList()
            emptyList()
        }
    }

    fun getTournamentById(id: String): Tournament? {
        return _inMemoryTournaments.value.find { it.tournamentId == id }
    }

    suspend fun createTournament(tournament: Tournament): Result<String> {
        val res = tournamentRemote.saveTournament(tournament)
        val id = res.getOrNull() ?: "tourney_${System.currentTimeMillis()}"
        val updatedTournament = tournament.copy(tournamentId = id)
        _inMemoryTournaments.value = listOf(updatedTournament) + _inMemoryTournaments.value
        return Result.Success(id)
    }

    suspend fun checkForDuplicates(name: String, date: String, district: String): List<Tournament> {
        val localMatches = _inMemoryTournaments.value.filter {
            it.district.equals(district, ignoreCase = true) &&
                    it.date == date &&
                    (it.name.contains(name.trim(), ignoreCase = true) || name.contains(it.name.trim(), ignoreCase = true))
        }
        val remoteMatches = tournamentRemote.findPossibleDuplicates(name, date, district)
        return (localMatches + remoteMatches).distinctBy { it.tournamentId }
    }

    fun toggleSaveTournament(tournamentId: String): Boolean {
        return if (savedTournamentIds.contains(tournamentId)) {
            savedTournamentIds.remove(tournamentId)
            false
        } else {
            savedTournamentIds.add(tournamentId)
            true
        }
    }

    fun isTournamentSaved(tournamentId: String): Boolean {
        return savedTournamentIds.contains(tournamentId)
    }

    fun getSavedTournaments(): List<Tournament> {
        return _inMemoryTournaments.value.filter { savedTournamentIds.contains(it.tournamentId) }
    }

    suspend fun registerTeam(registration: Registration): Result<String> {
        val regId = registration.registrationId.ifEmpty { "reg_${System.currentTimeMillis()}" }
        val finalReg = registration.copy(registrationId = regId)
        registrations.add(finalReg)

        try {
            FirestoreRefs.registrations()?.document(regId)?.set(finalReg)?.await()
        } catch (e: Exception) {
            // locally stored
        }

        // Increment team count locally
        val tourney = getTournamentById(registration.tournamentId)
        if (tourney != null) {
            val updated = tourney.copy(registeredTeamCount = tourney.registeredTeamCount + 1)
            _inMemoryTournaments.value = _inMemoryTournaments.value.map {
                if (it.tournamentId == tourney.tournamentId) updated else it
            }
        }

        return Result.Success(regId)
    }

    fun getRegistrationsForTournament(tournamentId: String): List<Registration> {
        return registrations.filter { it.tournamentId == tournamentId }
    }
}
