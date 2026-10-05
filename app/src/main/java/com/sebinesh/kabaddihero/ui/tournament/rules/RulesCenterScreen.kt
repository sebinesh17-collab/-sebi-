package com.sebinesh.kabaddihero.ui.tournament.rules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.core.di.ServiceLocator
import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.data.remote.ai.AiRulesService
import com.sebinesh.kabaddihero.ui.components.CourtBackdrop
import com.sebinesh.kabaddihero.ui.theme.*
import kotlinx.coroutines.launch

data class RuleTopic(
    val id: String,
    val titleTa: String,
    val titleEn: String,
    val summaryTa: String,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesCenterScreen(
    onBack: () -> Unit
) {
    val aiRulesService = ServiceLocator.aiRulesService
    val coroutineScope = rememberCoroutineScope()

    var selectedTopic by remember { mutableStateOf<RuleTopic?>(null) }
    var explanationText by remember { mutableStateOf<String?>(null) }
    var isExplaining by remember { mutableStateOf(false) }

    val topics = listOf(
        RuleTopic(
            id = "bonus_line",
            titleTa = "Bonus Line",
            titleEn = "Bonus Line Rule",
            summaryTa = "Active when 6 or 7 defenders are on court. The raider must cross the bonus line with one foot in contact and the other in the air.",
            category = "Court & Lines"
        ),
        RuleTopic(
            id = "baulk_line",
            titleTa = "Baulk Line",
            titleEn = "Baulk Line Crossing",
            summaryTa = "Raider must fully cross the baulk line with at least one foot to make the raid legitimate; otherwise raider is ruled out.",
            category = "Court & Lines"
        ),
        RuleTopic(
            id = "all_out",
            titleTa = "All-Out",
            titleEn = "All Out Rule",
            summaryTa = "Occurs when all defenders are declared out. Awards 2 extra bonus points and revives all 7 players back to the court.",
            category = "Scoring"
        ),
        RuleTopic(
            id = "super_tackle",
            titleTa = "Super Tackle",
            titleEn = "Super Tackle",
            summaryTa = "When 3 or fewer defenders successfully stop a raider, 2 points are awarded instead of 1.",
            category = "Defense"
        ),
        RuleTopic(
            id = "super_raid",
            titleTa = "Super Raid",
            titleEn = "Super Raid",
            summaryTa = "A raid where 3 or more points (touches + bonus) are scored by the raider in a single turn.",
            category = "Raid"
        ),
        RuleTopic(
            id = "do_or_die",
            titleTa = "Do-or-Die Raid",
            titleEn = "Do-or-Die Raid",
            summaryTa = "Triggered on the 3rd consecutive empty raid. The raider must score a touch or bonus point or is declared out.",
            category = "Raid"
        ),
        RuleTopic(
            id = "lobby",
            titleTa = "Lobby Strips",
            titleEn = "Lobby Access",
            summaryTa = "Lobby area becomes active only after touch contact is made between raider and defender. Entering lobby before touch results in player out.",
            category = "Court & Lines"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kabaddi Rules Center", fontWeight = FontWeight.Bold, color = Color.White) },
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

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MatClayDeep),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "International Kabaddi Federation (IKF) Standard Rules",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "7 players on court • 40 mins (20-5-20) • 30s raid clock • Men 85 KG limit.",
                                color = ChalkWhite.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                items(topics) { topic ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedTopic = topic
                                explanationText = null
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = topic.titleTa, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(text = topic.category, color = ElectricCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = topic.summaryTa, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

                            Spacer(modifier = Modifier.height(10.dp))

                            // AI Explain Quick Button
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SuggestionChip(
                                    onClick = {
                                        selectedTopic = topic
                                        isExplaining = true
                                        coroutineScope.launch {
                                            val res = aiRulesService.explainRule(topic.titleEn, AiRulesService.RuleExplanationType.EXPLAIN_IN_TAMIL, topic.summaryTa)
                                            isExplaining = false
                                            explanationText = res.getOrNull() ?: "Could not load AI explanation."
                                        }
                                    },
                                    label = { Text("AI Detailed Explanation", fontSize = 11.sp) }
                                )
                                SuggestionChip(
                                    onClick = {
                                        selectedTopic = topic
                                        isExplaining = true
                                        coroutineScope.launch {
                                            val res = aiRulesService.explainRule(topic.titleEn, AiRulesService.RuleExplanationType.EXAMPLE, topic.summaryTa)
                                            isExplaining = false
                                            explanationText = res.getOrNull() ?: "Could not load example scenario."
                                        }
                                    },
                                    label = { Text("Live Match Example", fontSize = 11.sp) }
                                )
                            }
                        }
                    }
                }
            }

            // Bottom explanation sheet/dialog if active
            if (selectedTopic != null && (explanationText != null || isExplaining)) {
                AlertDialog(
                    onDismissRequest = {
                        selectedTopic = null
                        explanationText = null
                    },
                    title = { Text(selectedTopic!!.titleTa, fontWeight = FontWeight.Bold) },
                    text = {
                        if (isExplaining) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            Text(text = explanationText.orEmpty(), fontSize = 14.sp, lineHeight = 22.sp)
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { selectedTopic = null; explanationText = null }) {
                            Text("Close")
                        }
                    }
                )
            }
        }
    }
}
