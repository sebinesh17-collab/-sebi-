package com.sebinesh.kabaddihero.ui.ai

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
import com.sebinesh.kabaddihero.core.di.ServiceLocator
import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.data.remote.ai.AiEligibilityResponse
import com.sebinesh.kabaddihero.ui.components.CourtBackdrop
import com.sebinesh.kabaddihero.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EligibilityScreen(
    tournamentId: String,
    onBack: () -> Unit
) {
    val tournament = ServiceLocator.tournamentRepository.getTournamentById(tournamentId)
        ?: ServiceLocator.tournamentRepository.tournaments
    val aiEligibilityService = ServiceLocator.aiEligibilityService
    val coroutineScope = rememberCoroutineScope()

    var ageText by remember { mutableStateOf("22") }
    var weightText by remember { mutableStateOf("78") }
    var selectedGender by remember { mutableStateOf("Men") }

    var result by remember { mutableStateOf<AiEligibilityResponse?>(null) }
    var isChecking by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val actualTournament = ServiceLocator.tournamentRepository.getTournamentById(tournamentId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Player Eligibility Checker", fontWeight = FontWeight.Bold, color = Color.White) },
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
        if (actualTournament == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Tournament details not found.")
            }
            return@Scaffold
        }

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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tournament Details Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = StadiumNightAlt),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = actualTournament.name, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tournament Limits: ${actualTournament.category} • ${actualTournament.gender} • Weight Limit: ${actualTournament.weightLimitKg ?: "Open"} KG",
                            color = ElectricCyan,
                            fontSize = 12.sp
                        )
                    }
                }

                // Input Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("Enter Your Details (Player Profile):", fontWeight = FontWeight.Bold, fontSize = 15.sp)

                        OutlinedTextField(
                            value = ageText,
                            onValueChange = { ageText = it },
                            label = { Text("Age (in years)") },
                            modifier = Modifier.fillMaxWidth().testTag("input_eligibility_age")
                        )

                        OutlinedTextField(
                            value = weightText,
                            onValueChange = { weightText = it },
                            label = { Text("Weight (in KG)") },
                            modifier = Modifier.fillMaxWidth().testTag("input_eligibility_weight")
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Gender:", modifier = Modifier.width(70.dp), fontWeight = FontWeight.Medium)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = selectedGender == "Men", onClick = { selectedGender = "Men" })
                                Text("Men")
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = selectedGender == "Women", onClick = { selectedGender = "Women" })
                                Text("Women")
                            }
                        }

                        Button(
                            onClick = {
                                isChecking = true
                                errorMessage = null
                                coroutineScope.launch {
                                    val res = aiEligibilityService.checkEligibility(
                                        playerAge = ageText.toIntOrNull(),
                                        playerWeightKg = weightText.toFloatOrNull(),
                                        playerGender = selectedGender,
                                        tournament = actualTournament
                                    )
                                    isChecking = false
                                    if (res is Result.Success) {
                                        result = res.data
                                    } else if (res is Result.Error) {
                                        errorMessage = res.userMessage
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_check_eligibility"),
                            colors = ButtonDefaults.buttonColors(containerColor = MatClay),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isChecking
                        ) {
                            if (isChecking) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                            } else {
                                Text("Check Eligibility", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Result Card
                result?.let { r ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (r.eligible) WhistleGreen.copy(alpha = 0.15f) else AlertRed.copy(alpha = 0.15f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (r.eligible) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                    contentDescription = null,
                                    tint = if (r.eligible) WhistleGreen else AlertRed,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = if (r.eligible) "ELIGIBLE TO PARTICIPATE!" else "NOT ELIGIBLE",
                                    color = if (r.eligible) WhistleGreen else AlertRed,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = r.reason, fontSize = 14.sp, lineHeight = 20.sp)

                            if (r.matchedRules.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("Criteria Satisfied:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                r.matchedRules.forEach { m ->
                                    Text("• $m", fontSize = 12.sp, color = WhistleGreen)
                                }
                            }

                            if (r.warnings.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("Recommendations:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BonusYellow)
                                r.warnings.forEach { w ->
                                    Text("⚠ $w", fontSize = 12.sp, color = BonusYellow)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "* This check is calculated for informational guidance based on tournament rules. Official weigh-in and verification by the organizer is final.",
                                fontSize = 10.sp,
                                color = SoftSteel
                            )
                        }
                    }
                }
            }
        }
    }
}
