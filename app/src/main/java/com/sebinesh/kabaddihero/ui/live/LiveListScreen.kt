package com.sebinesh.kabaddihero.ui.live

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.core.di.ServiceLocator
import com.sebinesh.kabaddihero.data.model.Match
import com.sebinesh.kabaddihero.ui.components.CourtBackdrop
import com.sebinesh.kabaddihero.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveListScreen(
    onMatchClick: (String) -> Unit,
    onScorerClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val matches by ServiceLocator.matchRepository.matches.collectAsState()
    val currentUser by ServiceLocator.authRepository.currentUserState.collectAsState()

    val isRefereeOrStaff = currentUser?.role?.isRefereeOrStaff() == true

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Live Matches", fontWeight = FontWeight.Bold, color = TextPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkPurpleSurface)
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CourtBackdrop(alpha = 0.08f)

            if (matches.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(PurpleContainer, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Scoreboard,
                                contentDescription = null,
                                tint = PurplePrimaryBright,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Live Matches Currently",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "There are no live matches in progress right now. Scheduled matches will stream real-time points here.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = "Matches in Progress",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    items(matches) { match ->
                        LiveMatchCard(
                            match = match,
                            isReferee = isRefereeOrStaff,
                            onViewScoreboard = { onMatchClick(match.matchId) },
                            onOpenScorer = { onScorerClick(match.matchId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LiveMatchCard(
    match: Match,
    isReferee: Boolean,
    onViewScoreboard: () -> Unit,
    onOpenScorer: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onViewScoreboard)
            .testTag("live_match_card_${match.matchId}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkPurpleSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkPurpleBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = match.tournamentName,
                    color = TextTertiary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                if (match.status == "LIVE") {
                    Box(
                        modifier = Modifier
                            .background(AlertRed, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(text = "● LIVE", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text(text = match.status, color = PurpleGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Teams and Big Score display
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Team A
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = match.teamAName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "On Court: ${match.teamAPlayersOnCourt} players", color = TextTertiary, fontSize = 11.sp)
                }

                // Scores
                Box(
                    modifier = Modifier
                        .background(DarkPurpleCard, RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "${match.scoreTeamA} - ${match.scoreTeamB}",
                        color = PurplePrimaryBright,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                // Team B
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(text = match.teamBName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "On Court: ${match.teamBPlayersOnCourt} players", color = TextTertiary, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Half: ${match.currentHalf} • Venue: ${match.venue}",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                if (isReferee) {
                    Button(
                        onClick = onOpenScorer,
                        colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Referee Scorer", fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }
    }
}
