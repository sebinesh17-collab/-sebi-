package com.sebinesh.kabaddihero.deepLink

import android.net.Uri

sealed class DeepLinkDestination {
    data class TournamentDetails(val tournamentId: String) : DeepLinkDestination()
    data class MatchScoreboard(val matchId: String) : DeepLinkDestination()
    data class PlayerProfile(val playerId: String) : DeepLinkDestination()
    data class TeamProfile(val teamId: String) : DeepLinkDestination()
    object Home : DeepLinkDestination()
    object Live : DeepLinkDestination()
    object None : DeepLinkDestination()
}

object DeepLinkHandler {
    fun parse(uri: Uri?): DeepLinkDestination {
        if (uri == null) return DeepLinkDestination.None

        val host = uri.host
        val pathSegments = uri.pathSegments

        // kabaddihero://tournament/{id} or https://kabaddihero.app/tournament/{id}
        if (host == "tournament" || pathSegments.firstOrNull() == "tournament") {
            val id = if (host == "tournament") pathSegments.firstOrNull() else pathSegments.getOrNull(1)
            if (!id.isNullOrBlank()) return DeepLinkDestination.TournamentDetails(id)
        }

        if (host == "match" || pathSegments.firstOrNull() == "match") {
            val id = if (host == "match") pathSegments.firstOrNull() else pathSegments.getOrNull(1)
            if (!id.isNullOrBlank()) return DeepLinkDestination.MatchScoreboard(id)
        }

        if (host == "player" || pathSegments.firstOrNull() == "player") {
            val id = if (host == "player") pathSegments.firstOrNull() else pathSegments.getOrNull(1)
            if (!id.isNullOrBlank()) return DeepLinkDestination.PlayerProfile(id)
        }

        if (host == "team" || pathSegments.firstOrNull() == "team") {
            val id = if (host == "team") pathSegments.firstOrNull() else pathSegments.getOrNull(1)
            if (!id.isNullOrBlank()) return DeepLinkDestination.TeamProfile(id)
        }

        if (host == "live" || pathSegments.firstOrNull() == "live") {
            return DeepLinkDestination.Live
        }

        return DeepLinkDestination.Home
    }
}
