package com.sebinesh.kabaddihero.ui.live

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.sebinesh.kabaddihero.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScorerScreen(
    viewModel: ScoreboardViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val match = state.match

    var activeTeam by remember { mutableStateOf("A") }
    var raiderName by remember { mutableStateOf("") }
    var bonusChecked by remember { mutableStateOf(false) }

    var showCorrectionDialog by remember { mutableStateOf(false) }
    var correctionDelta by remember { mutableStateOf(1) }
    var correctionReason by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Referee Scorer Console", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showCorrectionDialog = true }) {
                        Icon(Icons.Default.Build, contentDescription = "Correct", tint = BonusYellow)
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Big Active Score Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = StadiumNightAlt)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(match.teamAName, color = Color.White, fontWeight = FontWeight.Bold)
                        Text("${match.scoreTeamA}", color = ElectricCyan, fontSize = 38.sp, fontWeight = FontWeight.Black)
                        Text("${match.teamAPlayersOnCourt} players", color = SoftSteel, fontSize = 12.sp)
                    }

                    Text("VS", color = SoftSteel, fontWeight = FontWeight.Bold)

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(match.teamBName, color = Color.White, fontWeight = FontWeight.Bold)
                        Text("${match.scoreTeamB}", color = ElectricCyan, fontSize = 38.sp, fontWeight = FontWeight.Black)
                        Text("${match.teamBPlayersOnCourt} players", color = SoftSteel, fontSize = 12.sp)
                    }
                }
            }

            // Select Active Team for Scoring
            Text("Scoring Team:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { activeTeam = "A" },
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (activeTeam == "A") MatClay else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(match.teamAName, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { activeTeam = "B" },
                    modifier = Modifier.weight(1f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (activeTeam == "B") MatClay else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(match.teamBName, fontWeight = FontWeight.Bold)
                }
            }

            // Raider name & Bonus toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = raiderName,
                    onValueChange = { raiderName = it },
                    label = { Text("Raider / Defender Name") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = bonusChecked, onCheckedChange = { bonusChecked = it })
                    Text("Bonus (+1)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Raid scoring action buttons
            Text("Raid Points:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        viewModel.recordRaid(activeTeam, 1, bonusChecked, raiderName)
                        raiderName = ""
                        bonusChecked = false
                    },
                    modifier = Modifier.weight(1f).height(52.dp).testTag("btn_score_touch_1"),
                    colors = ButtonDefaults.buttonColors(containerColor = WhistleGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("+1 Touch", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        viewModel.recordRaid(activeTeam, 2, bonusChecked, raiderName)
                        raiderName = ""
                        bonusChecked = false
                    },
                    modifier = Modifier.weight(1f).height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WhistleGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("+2 Touch", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        viewModel.recordRaid(activeTeam, 3, bonusChecked, raiderName)
                        raiderName = ""
                        bonusChecked = false
                    },
                    modifier = Modifier.weight(1.2f).height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BonusYellow),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Super Raid (+3)", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }

            // Tackle scoring action buttons
            Text("Defense / Tackle Points:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        viewModel.recordTackle(activeTeam, raiderName)
                        raiderName = ""
                    },
                    modifier = Modifier.weight(1f).height(50.dp).testTag("btn_score_tackle"),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("+1 Tackle", color = Color.Black, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        viewModel.recordTackle(activeTeam, raiderName)
                        raiderName = ""
                    },
                    modifier = Modifier.weight(1.2f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MatClay),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Super Tackle (+2)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            // Raid Timer Controls
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Raid Clock: ${match.raidTimeRemainingSeconds}s",
                        fontWeight = FontWeight.Bold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilledTonalButton(onClick = { viewModel.resetRaidTimer() }) {
                            Text("Reset (30s)")
                        }
                    }
                }
            }
        }

        // Referee Score Correction Dialog
        if (showCorrectionDialog) {
            AlertDialog(
                onDismissRequest = { showCorrectionDialog = false },
                title = { Text("Official Score Correction") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Referee correction - will be logged as an official match event.")
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = activeTeam == "A", onClick = { activeTeam = "A" })
                            Text(match.teamAName)
                            Spacer(modifier = Modifier.width(16.dp))
                            RadioButton(selected = activeTeam == "B", onClick = { activeTeam = "B" })
                            Text(match.teamBName)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(onClick = { correctionDelta = -1 }, colors = ButtonDefaults.buttonColors(containerColor = if (correctionDelta == -1) AlertRed else Color.Gray)) {
                                Text("-1 Point")
                            }
                            Button(onClick = { correctionDelta = 1 }, colors = ButtonDefaults.buttonColors(containerColor = if (correctionDelta == 1) WhistleGreen else Color.Gray)) {
                                Text("+1 Point")
                            }
                        }
                        OutlinedTextField(
                            value = correctionReason,
                            onValueChange = { correctionReason = it },
                            label = { Text("Correction Reason") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.applyCorrection(activeTeam, correctionDelta, correctionReason.ifBlank { "Referee Decision" })
                            showCorrectionDialog = false
                            correctionReason = ""
                        }
                    ) {
                        Text("Apply Correction")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCorrectionDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}
