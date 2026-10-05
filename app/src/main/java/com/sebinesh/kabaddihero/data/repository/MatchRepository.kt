package com.sebinesh.kabaddihero.data.repository

import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.data.local.DemoDataProvider
import com.sebinesh.kabaddihero.data.model.Match
import com.sebinesh.kabaddihero.data.model.MatchEvent
import com.sebinesh.kabaddihero.data.remote.firestore.MatchRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MatchRepository(
    private val matchRemote: MatchRemote
) {
    private val _matches = MutableStateFlow<List<Match>>(emptyList())
    val matches: StateFlow<List<Match>> = _matches.asStateFlow()

    private val _events = MutableStateFlow<List<MatchEvent>>(emptyList())
    val events: StateFlow<List<MatchEvent>> = _events.asStateFlow()

    fun getMatchById(matchId: String): Match? {
        return _matches.value.find { it.matchId == matchId }
    }

    suspend fun updateMatch(match: Match): Result<Unit> {
        _matches.value = _matches.value.map {
            if (it.matchId == match.matchId) match else it
        }
        return matchRemote.saveMatch(match)
    }

    suspend fun addEvent(event: MatchEvent): Result<Unit> {
        _events.value = _events.value + event
        return matchRemote.recordEvent(event)
    }

    fun getEventsForMatch(matchId: String): List<MatchEvent> {
        return _events.value.filter { it.matchId == matchId }
    }
}
