package com.sebinesh.kabaddihero.ui.saved

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sebinesh.kabaddihero.core.di.ServiceLocator
import com.sebinesh.kabaddihero.ui.components.EmptyState
import com.sebinesh.kabaddihero.ui.components.TournamentCard
import com.sebinesh.kabaddihero.ui.theme.StadiumNight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    onBack: () -> Unit,
    onTournamentClick: (String) -> Unit
) {
    val tournamentRepo = ServiceLocator.tournamentRepository
    var savedTournaments by remember { mutableStateOf(tournamentRepo.getSavedTournaments()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Tournaments", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = StadiumNight)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (savedTournaments.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                EmptyState(
                    title = "No Saved Tournaments",
                    subtitle = "Tap the bookmark icon on tournament cards to save them here for quick access."
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(savedTournaments) { t ->
                    TournamentCard(
                        tournament = t,
                        isSaved = true,
                        onCardClick = { onTournamentClick(t.tournamentId) },
                        onRegisterClick = { onTournamentClick(t.tournamentId) },
                        onViewRulesClick = { onTournamentClick(t.tournamentId) },
                        onShareClick = {},
                        onToggleSave = {
                            tournamentRepo.toggleSaveTournament(t.tournamentId)
                            savedTournaments = tournamentRepo.getSavedTournaments()
                        }
                    )
                }
            }
        }
    }
}
