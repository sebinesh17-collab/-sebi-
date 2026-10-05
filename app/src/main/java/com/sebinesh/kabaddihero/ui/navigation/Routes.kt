package com.sebinesh.kabaddihero.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Explore : Screen("explore")
    object Live : Screen("live")
    object CreateTournament : Screen("create_tournament")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object RulesCenter : Screen("rules_center")
    object AiChat : Screen("ai_chat")
    object PosterAnalyzer : Screen("poster_analyzer")
    object Notifications : Screen("notifications")
    object Saved : Screen("saved")
    object AdminDashboard : Screen("admin_dashboard")

    object TournamentDetail : Screen("tournament_detail/{tournamentId}") {
        fun createRoute(tournamentId: String) = "tournament_detail/$tournamentId"
    }

    object TournamentRules : Screen("tournament_rules/{tournamentId}") {
        fun createRoute(tournamentId: String) = "tournament_rules/$tournamentId"
    }

    object EligibilityChecker : Screen("eligibility_checker/{tournamentId}") {
        fun createRoute(tournamentId: String) = "eligibility_checker/$tournamentId"
    }

    object LiveScoreboard : Screen("live_scoreboard/{matchId}") {
        fun createRoute(matchId: String) = "live_scoreboard/$matchId"
    }

    object Scorer : Screen("scorer/{matchId}") {
        fun createRoute(matchId: String) = "scorer/$matchId"
    }

    object PlayerProfile : Screen("player_profile/{playerId}") {
        fun createRoute(playerId: String) = "player_profile/$playerId"
    }

    object TeamProfile : Screen("team_profile/{teamId}") {
        fun createRoute(teamId: String) = "team_profile/$teamId"
    }
}
