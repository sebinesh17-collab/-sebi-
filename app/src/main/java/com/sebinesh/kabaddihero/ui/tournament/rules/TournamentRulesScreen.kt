package com.sebinesh.kabaddihero.ui.tournament.rules

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.core.di.ServiceLocator
import com.sebinesh.kabaddihero.ui.theme.BonusYellow
import com.sebinesh.kabaddihero.ui.theme.ElectricCyan
import com.sebinesh.kabaddihero.ui.theme.StadiumNight
import com.sebinesh.kabaddihero.ui.theme.StadiumNightAlt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentRulesScreen(
    tournamentId: String,
    onBack: () -> Unit
) {
    val tournament = ServiceLocator.tournamentRepository.getTournamentById(tournamentId)
    val rules = tournament?.rules

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tournament Rules", fontWeight = FontWeight.Bold, color = Color.White) },
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
        if (tournament == null || rules == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Tournament details not found.")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StadiumNightAlt),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = tournament.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Rules Standard: ${rules.profileName}", color = ElectricCyan, fontWeight = FontWeight.SemiBold)
                }
            }

            RuleDetailRow(title = "Players on Court", value = "${rules.playersOnCourt} players")
            RuleDetailRow(title = "Substitutes", value = "${rules.substitutesCount} players")
            RuleDetailRow(title = "Match Duration", value = "${rules.matchDurationMinutes} mins (${rules.halfDurationMinutes} - ${rules.halfTimeBreakMinutes} - ${rules.halfDurationMinutes})")
            RuleDetailRow(title = "Raid Duration", value = "${rules.raidDurationSeconds} seconds")
            RuleDetailRow(title = "Do-or-Die Raid", value = "Active on 3rd successive empty raid")
            RuleDetailRow(title = "Bonus Point Rule", value = "Active when >= ${rules.bonusLineActiveDefendersMin} defenders on court")
            RuleDetailRow(title = "All-Out Bonus", value = "${rules.allOutBonusPoints} extra points")
            RuleDetailRow(title = "Super Tackle", value = "${rules.superTacklePoints} points (<= ${rules.superTackleDefendersMax} defenders)")
            RuleDetailRow(title = "Weight Limit", value = "${tournament.weightLimitKg ?: "Open"} KG")

            if (rules.specialRestrictions.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = BonusYellow.copy(alpha = 0.15f))
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = BonusYellow)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Organizer Specific Terms & Conditions", fontWeight = FontWeight.Bold, color = BonusYellow, fontSize = 13.sp)
                            Text(rules.specialRestrictions, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RuleDetailRow(title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
        }
    }
}
