package com.sebinesh.kabaddihero.ui.team

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.core.di.ServiceLocator
import com.sebinesh.kabaddihero.ui.components.CourtBackdrop
import com.sebinesh.kabaddihero.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamProfileScreen(
    teamId: String,
    onBack: () -> Unit
) {
    val team = ServiceLocator.teamRepository.getTeamById(teamId)
        ?: ServiceLocator.teamRepository.teams.value.first()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(team.name, fontWeight = FontWeight.Bold, color = Color.White) },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CourtBackdrop(alpha = 0.08f)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(MatClayDeep),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Groups,
                        contentDescription = null,
                        tint = ElectricCyan,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Text(
                    text = team.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "District: ${team.district} • Captain: ${team.captainName}",
                    color = SoftSteel,
                    fontSize = 13.sp
                )

                // Team Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = StadiumNightAlt),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${team.matchesPlayed}", fontWeight = FontWeight.Black, fontSize = 20.sp, color = Color.White)
                            Text("Matches", fontSize = 11.sp, color = SoftSteel)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = StadiumNightAlt),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${team.wins}", fontWeight = FontWeight.Black, fontSize = 20.sp, color = WhistleGreen)
                            Text("Wins", fontSize = 11.sp, color = SoftSteel)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = StadiumNightAlt),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${team.totalPoints}", fontWeight = FontWeight.Black, fontSize = 20.sp, color = BonusYellow)
                            Text("Total Pts", fontSize = 11.sp, color = SoftSteel)
                        }
                    }
                }

                // Description Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Team Information", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = team.teamDescription, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Coach: ${team.coachName}", fontSize = 13.sp, color = SoftSteel)
                    }
                }
            }
        }
    }
}
