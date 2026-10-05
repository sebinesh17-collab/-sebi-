package com.sebinesh.kabaddihero.data.repository

import com.sebinesh.kabaddihero.data.local.DemoDataProvider
import com.sebinesh.kabaddihero.data.model.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TeamRepository {
    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams.asStateFlow()

    fun getTeamById(teamId: String): Team? {
        return _teams.value.find { it.teamId == teamId }
    }

    fun saveTeam(team: Team) {
        val id = team.teamId.ifEmpty { "team_${System.currentTimeMillis()}" }
        val updated = team.copy(teamId = id)
        _teams.value = _teams.value.filterNot { it.teamId == id } + updated
    }
}
