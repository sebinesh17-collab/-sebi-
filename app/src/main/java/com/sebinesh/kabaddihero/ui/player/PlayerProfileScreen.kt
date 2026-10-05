package com.sebinesh.kabaddihero.ui.player

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
import androidx.compose.runtime.*
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
fun PlayerProfileScreen(
    playerId: String,
    onBack: () -> Unit,
    onEdit: (String) -> Unit
) {
    val player = ServiceLocator.playerRepository.getPlayerById(playerId)
        ?: ServiceLocator.playerRepository.players.value.first()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(player.name, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onEdit(player.playerId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = ElectricCyan)
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
                // Avatar
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(MatClay),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                }

                Text(
                    text = player.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${player.position} • ${player.teamName.ifBlank { "Independent Player" }}",
                    color = ElectricCyan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "District: ${player.district} • Weight: ${player.weightKg} KG • Age: ${player.calculatedAge}",
                    color = SoftSteel,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PlayerStatBox(title = "Raid Points", value = "${player.raidPoints}", color = BonusYellow, modifier = Modifier.weight(1f))
                    PlayerStatBox(title = "Tackle Points", value = "${player.tacklePoints}", color = ElectricCyan, modifier = Modifier.weight(1f))
                    PlayerStatBox(title = "Total Points", value = "${player.totalPoints}", color = WhistleGreen, modifier = Modifier.weight(1f))
                }

                // Achievements Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Achievements", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        if (player.achievements.isEmpty()) {
                            Text("No official achievements recorded yet.", color = SoftSteel, fontSize = 13.sp)
                        } else {
                            player.achievements.forEach { ach ->
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = BonusYellow, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(ach, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerStatBox(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = StadiumNightAlt),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Black, color = color)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, fontSize = 10.sp, color = SoftSteel)
        }
    }
}
