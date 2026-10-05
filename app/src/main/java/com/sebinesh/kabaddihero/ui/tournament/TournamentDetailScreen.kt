package com.sebinesh.kabaddihero.ui.tournament

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.data.model.Tournament
import com.sebinesh.kabaddihero.ui.components.CountdownText
import com.sebinesh.kabaddihero.ui.components.CourtBackdrop
import com.sebinesh.kabaddihero.ui.components.VerificationBadge
import com.sebinesh.kabaddihero.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentDetailScreen(
    viewModel: TournamentDetailViewModel,
    onBack: () -> Unit,
    onViewRules: (String) -> Unit,
    onCheckEligibility: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showRegisterDialog by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }

    val tournament = state.tournament

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tournament?.name ?: "Tournament Details", maxLines = 1, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleSave() }) {
                        Icon(
                            imageVector = if (state.isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = "Save",
                            tint = if (state.isSaved) BonusYellow else Color.White
                        )
                    }
                    IconButton(onClick = { showReportDialog = true }) {
                        Icon(Icons.Default.Report, contentDescription = "Report", tint = AlertRed)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = StadiumNight)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (tournament == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(MatClayDeep, MatClay, Color(0xFFC0392B))
                        )
                    )
                    .padding(20.dp)
            ) {
                CourtBackdrop(alpha = 0.2f)

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        VerificationBadge(status = tournament.verificationStatus)
                        CountdownText(targetDateStr = tournament.date)
                    }

                    Column {
                        Text(
                            text = tournament.name,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Place, contentDescription = null, tint = BonusYellow, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${tournament.venueName}, ${tournament.district}",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Quick Action Buttons Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Call Organizer
                Button(
                    onClick = {
                        val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${tournament.organizerPhone}"))
                        context.startActivity(phoneIntent)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = WhistleGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Call", fontSize = 12.sp)
                }

                // WhatsApp Organizer
                Button(
                    onClick = {
                        val cleanPhone = tournament.organizerWhatsapp.replace("+", "")
                        val waIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$cleanPhone"))
                        context.startActivity(waIntent)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "WhatsApp", fontSize = 12.sp)
                }

                // Directions
                Button(
                    onClick = {
                        val geoUri = Uri.parse("geo:${tournament.latitude},${tournament.longitude}?q=${Uri.encode(tournament.venueName)}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
                        context.startActivity(mapIntent)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Navigation, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Directions", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Key Info Cards (Prize, Entry, Weight, Teams)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "First Prize",
                    value = "₹${tournament.prizeAmount.toInt()}",
                    icon = Icons.Default.EmojiEvents,
                    color = BonusYellow,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Entry Fee",
                    value = "₹${tournament.entryFee.toInt()}",
                    icon = Icons.Default.Payment,
                    color = ElectricCyan,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Weight Limit",
                    value = "${tournament.weightLimitKg ?: "Open"} KG",
                    icon = Icons.Default.FitnessCenter,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // AI Eligibility Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Am I eligible for this tournament?", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "Check player eligibility instantly with Gemini AI rules checker.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Button(
                        onClick = { onCheckEligibility(tournament.tournamentId) },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Check", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // AI Generated Tournament Summary (if available)
            state.aiSummary?.let { summary ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = StadiumNightAlt),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = ElectricCyan)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "AI Tournament Summary", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = summary.shortTamilSummary, color = ChalkWhite, fontSize = 13.sp, lineHeight = 20.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = summary.rulesSummary, color = SoftSteel, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Description & Venue Details
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "About Tournament", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = tournament.description, style = MaterialTheme.typography.bodyMedium)

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Rules Standard Profile", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "Rules: ${tournament.rules.profileName}", fontSize = 14.sp)
                    Text(text = "Court Players: ${tournament.rules.playersOnCourt} | Substitutes: ${tournament.rules.substitutesCount}", fontSize = 13.sp)
                    Text(text = "Match Duration: ${tournament.rules.matchDurationMinutes} minutes", fontSize = 13.sp)

                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = { onViewRules(tournament.tournamentId) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "View Full Official Rules")
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main Registration Button
            Button(
                onClick = { showRegisterDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(52.dp)
                    .testTag("btn_detail_register"),
                colors = ButtonDefaults.buttonColors(containerColor = MatClay),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(text = "REGISTER TEAM", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // Team Registration Dialog
        if (showRegisterDialog) {
            TeamRegistrationDialog(
                tournament = tournament,
                onDismiss = { showRegisterDialog = false },
                onSubmit = { tName, cName, ph, players ->
                    viewModel.registerTeam(tName, cName, ph, players)
                    showRegisterDialog = false
                }
            )
        }

        // Report Dialog
        if (showReportDialog) {
            ReportDialog(
                onDismiss = { showReportDialog = false },
                onSubmit = { reason, desc ->
                    viewModel.submitReport(reason, desc)
                    showReportDialog = false
                }
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = title, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun TeamRegistrationDialog(
    tournament: Tournament,
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, List<String>) -> Unit
) {
    var teamName by remember { mutableStateOf("") }
    var captainName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var playerNamesText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Team Registration") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = tournament.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                OutlinedTextField(
                    value = teamName,
                    onValueChange = { teamName = it },
                    label = { Text("Team Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = captainName,
                    onValueChange = { captainName = it },
                    label = { Text("Captain Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Contact Phone") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = playerNamesText,
                    onValueChange = { playerNamesText = it },
                    label = { Text("Player Names (comma-separated)") },
                    placeholder = { Text("Ramesh, Suresh, Pandi...") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val players = playerNamesText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                    onSubmit(teamName, captainName, phone, players)
                }
            ) {
                Text("Register Team")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun ReportDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String) -> Unit
) {
    var reason by remember { mutableStateOf("Fake or fraudulent tournament") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Report Tournament") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Report suspicious, misleading, or fraudulent tournament postings.")
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Reason for Report") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Additional Details") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(reason, description) },
                colors = ButtonDefaults.buttonColors(containerColor = AlertRed)
            ) {
                Text("Submit Report")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
