package com.sebinesh.kabaddihero.ui.live

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.ui.components.CourtBackdrop
import com.sebinesh.kabaddihero.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveScoreboardScreen(
    viewModel: ScoreboardViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val match = state.match

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(match?.tournamentName ?: "Live Scoreboard", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
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
        if (match == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CourtBackdrop(alpha = 0.15f)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Big Stadium Score Board Panel
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = StadiumNightAlt),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Match Stage and Live indicator
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = match.stage, color = BonusYellow, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AlertRed)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(text = "● LIVE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Large Score display
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Team A
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = match.teamAName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                AnimatedContent(
                                    targetState = match.scoreTeamA,
                                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                                    label = "scoreA"
                                ) { score ->
                                    Text(text = "$score", color = ElectricCyan, fontSize = 54.sp, fontWeight = FontWeight.Black)
                                }
                                Text(text = "On Court: ${match.teamAPlayersOnCourt}", color = SoftSteel, fontSize = 11.sp)
                            }

                            // Center VS and Raid Timer
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "VS", color = SoftSteel, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(MatClayDeep),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "${match.raidTimeRemainingSeconds}", color = BonusYellow, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                        Text(text = "sec", color = ChalkWhite, fontSize = 9.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "Raid Clock", color = SoftSteel, fontSize = 10.sp)
                            }

                            // Team B
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = match.teamBName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                AnimatedContent(
                                    targetState = match.scoreTeamB,
                                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                                    label = "scoreB"
                                ) { score ->
                                    Text(text = "$score", color = ElectricCyan, fontSize = 54.sp, fontWeight = FontWeight.Black)
                                }
                                Text(text = "On Court: ${match.teamBPlayersOnCourt}", color = SoftSteel, fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Divider(color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Match Details row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(text = "Half: ${match.currentHalf}", color = ChalkWhite, fontSize = 12.sp)
                            Text(text = "Referee: ${match.refereeName}", color = ChalkWhite, fontSize = 12.sp)
                            Text(text = "All-Outs: ${match.teamAAllOuts} - ${match.teamBAllOuts}", color = BonusYellow, fontSize = 12.sp)
                        }
                    }
                }

                // Recent Events Feed
                Text(text = "Match Events History", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (state.events.isEmpty() && match.recentEventsSummary.isNotEmpty()) {
                        items(match.recentEventsSummary) { summary ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.SportsKabaddi, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(text = summary, fontSize = 13.sp)
                                }
                            }
                        }
                    } else {
                        items(state.events) { evt ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (evt.isCorrection) Icons.Default.Edit else Icons.Default.SportsKabaddi,
                                            contentDescription = null,
                                            tint = if (evt.isCorrection) BonusYellow else ElectricCyan,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(text = evt.description, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                                            Text(text = "${evt.eventType} • Team ${evt.team}", color = SoftSteel, fontSize = 11.sp)
                                        }
                                    }
                                    Text(text = "+${evt.points}", fontWeight = FontWeight.Bold, color = ElectricCyan, fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
