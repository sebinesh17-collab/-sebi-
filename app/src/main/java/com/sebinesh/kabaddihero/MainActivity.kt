package com.sebinesh.kabaddihero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.sebinesh.kabaddihero.deepLink.DeepLinkDestination
import com.sebinesh.kabaddihero.deepLink.DeepLinkHandler
import com.sebinesh.kabaddihero.ui.navigation.AppNavGraph
import com.sebinesh.kabaddihero.ui.navigation.Screen
import com.sebinesh.kabaddihero.ui.theme.KabaddiHeroTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val deepLinkDest = DeepLinkHandler.parse(intent?.data)

        setContent {
            KabaddiHeroTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)

                // Route to deep link if present
                LaunchedEffect(deepLinkDest) {
                    when (deepLinkDest) {
                        is DeepLinkDestination.TournamentDetails -> {
                            navController.navigate(Screen.TournamentDetail.createRoute(deepLinkDest.tournamentId))
                        }
                        is DeepLinkDestination.MatchScoreboard -> {
                            navController.navigate(Screen.LiveScoreboard.createRoute(deepLinkDest.matchId))
                        }
                        is DeepLinkDestination.Live -> {
                            navController.navigate(Screen.Live.route)
                        }
                        else -> { /* regular start flow */ }
                    }
                }
            }
        }
    }
}
