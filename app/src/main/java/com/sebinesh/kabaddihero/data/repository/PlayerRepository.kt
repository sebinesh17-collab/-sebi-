package com.sebinesh.kabaddihero.data.repository

import com.sebinesh.kabaddihero.data.local.DemoDataProvider
import com.sebinesh.kabaddihero.data.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerRepository {
    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    fun getPlayerById(playerId: String): Player? {
        return _players.value.find { it.playerId == playerId }
    }

    fun savePlayer(player: Player) {
        val id = player.playerId.ifEmpty { "player_${System.currentTimeMillis()}" }
        val updated = player.copy(playerId = id)
        _players.value = _players.value.filterNot { it.playerId == id } + updated
    }
}
