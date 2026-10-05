package com.sebinesh.kabaddihero.ui.tournament.create

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
import com.sebinesh.kabaddihero.ui.components.ErrorBanner
import com.sebinesh.kabaddihero.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTournamentFlow(
    viewModel: CreateTournamentViewModel,
    onFinished: (String) -> Unit,
    onCancel: () -> Unit,
    onNavigateToPosterAnalyzer: () -> Unit
) {
    val state by viewModel.formState.collectAsState()

    LaunchedEffect(state.publishSuccessId) {
        state.publishSuccessId?.let { id ->
            onFinished(id)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Create Tournament Post", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("Step ${state.currentStep} of 7", fontSize = 12.sp, color = ElectricCyan)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (state.currentStep > 1) viewModel.prevStep() else onCancel()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = onNavigateToPosterAnalyzer) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = BonusYellow, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("AI Poster", color = BonusYellow, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = StadiumNight)
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (state.currentStep > 1) {
                        OutlinedButton(
                            onClick = { viewModel.prevStep() },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Back")
                        }
                    } else {
                        TextButton(onClick = onCancel) {
                            Text("Cancel", color = SoftSteel)
                        }
                    }

                    if (state.currentStep < 7) {
                        Button(
                            onClick = { viewModel.nextStep() },
                            colors = ButtonDefaults.buttonColors(containerColor = MatClay),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("btn_create_next")
                        ) {
                            Text("Next", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.publishTournament() },
                            enabled = !state.isPublishing,
                            colors = ButtonDefaults.buttonColors(containerColor = WhistleGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("btn_create_publish")
                        ) {
                            if (state.isPublishing) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                            } else {
                                Text("PUBLISH TOURNAMENT", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Step Progress Bar
            LinearProgressIndicator(
                progress = { state.currentStep / 7f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = ElectricCyan,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (state.errorMessage != null) {
                ErrorBanner(message = state.errorMessage!!)
                Spacer(modifier = Modifier.height(16.dp))
            }

            when (state.currentStep) {
                1 -> Step1BasicDetails(state) { viewModel.updateForm(it) }
                2 -> Step2DateTime(state) { viewModel.updateForm(it) }
                3 -> Step3Venue(state) { viewModel.updateForm(it) }
                4 -> Step4Rules(state) { viewModel.updateForm(it) }
                5 -> Step5Organizer(state) { viewModel.updateForm(it) }
                6 -> Step6Evidence(state) { viewModel.updateForm(it) }
                7 -> Step7Preview(state)
            }
        }
    }
}

@Composable
fun Step1BasicDetails(
    state: CreateTournamentFormState,
    update: ((CreateTournamentFormState) -> CreateTournamentFormState) -> Unit
) {
    Text("1. Tournament Basic Details", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = state.name,
        onValueChange = { v -> update { it.copy(name = v) } },
        label = { Text("Tournament Name*") },
        modifier = Modifier
            .fillMaxWidth()
            .testTag("create_input_name")
    )
    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = state.description,
        onValueChange = { v -> update { it.copy(description = v) } },
        label = { Text("Description & Highlights") },
        modifier = Modifier.fillMaxWidth(),
        minLines = 3
    )
    Spacer(modifier = Modifier.height(12.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = state.category,
            onValueChange = { v -> update { it.copy(category = v) } },
            label = { Text("Category (Open / Junior / Sub-Junior)") },
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = state.gender,
            onValueChange = { v -> update { it.copy(gender = v) } },
            label = { Text("Gender (Men / Women / Mixed)") },
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = state.weightLimitKg,
            onValueChange = { v -> update { it.copy(weightLimitKg = v) } },
            label = { Text("Weight Limit in KG (e.g. 80)") },
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = state.teamLimit,
            onValueChange = { v -> update { it.copy(teamLimit = v) } },
            label = { Text("Max Registered Teams Limit") },
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = state.entryFee,
            onValueChange = { v -> update { it.copy(entryFee = v) } },
            label = { Text("Entry Fee (₹)") },
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = state.prizeAmount,
            onValueChange = { v -> update { it.copy(prizeAmount = v) } },
            label = { Text("1st Prize Amount (₹)") },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun Step2DateTime(
    state: CreateTournamentFormState,
    update: ((CreateTournamentFormState) -> CreateTournamentFormState) -> Unit
) {
    Text("2. Date and Time Schedule", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = state.date,
        onValueChange = { v -> update { it.copy(date = v) } },
        label = { Text("Event Date (YYYY-MM-DD)*") },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(12.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = state.startTime,
            onValueChange = { v -> update { it.copy(startTime = v) } },
            label = { Text("Start Time (e.g. 08:00 AM)") },
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = state.endTime,
            onValueChange = { v -> update { it.copy(endTime = v) } },
            label = { Text("Expected End Time") },
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = state.registrationDeadline,
        onValueChange = { v -> update { it.copy(registrationDeadline = v) } },
        label = { Text("Registration Closing Date") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun Step3Venue(
    state: CreateTournamentFormState,
    update: ((CreateTournamentFormState) -> CreateTournamentFormState) -> Unit
) {
    Text("3. Venue and Location", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = state.venueName,
        onValueChange = { v -> update { it.copy(venueName = v) } },
        label = { Text("Ground / Stadium / Venue Name*") },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(12.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = state.village,
            onValueChange = { v -> update { it.copy(village = v) } },
            label = { Text("Town / Village") },
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = state.district,
            onValueChange = { v -> update { it.copy(district = v) } },
            label = { Text("District*") },
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = state.directions,
        onValueChange = { v -> update { it.copy(directions = v) } },
        label = { Text("Directions / Landmark") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun Step4Rules(
    state: CreateTournamentFormState,
    update: ((CreateTournamentFormState) -> CreateTournamentFormState) -> Unit
) {
    Text("4. Tournament Rules & Standards", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    Spacer(modifier = Modifier.height(16.dp))

    Text("Rules Standard Profile:", fontSize = 14.sp, fontWeight = FontWeight.Medium)
    Spacer(modifier = Modifier.height(8.dp))

    val profiles = listOf("IKF_STANDARD" to "IKF Standard (International)", "LOCAL_OPEN" to "Tamil Nadu Local Open", "JUNIOR" to "Junior Category (Under 20)", "CUSTOM" to "Custom Tournament Rules")
    profiles.forEach { (id, label) ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            RadioButton(
                selected = state.selectedProfileId == id,
                onClick = { update { it.copy(selectedProfileId = id) } }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, fontSize = 14.sp)
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = state.playersOnCourt,
            onValueChange = { v -> update { it.copy(playersOnCourt = v) } },
            label = { Text("Players on Court (7)") },
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = state.matchDurationMins,
            onValueChange = { v -> update { it.copy(matchDurationMins = v) } },
            label = { Text("Match Duration (40 min)") },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun Step5Organizer(
    state: CreateTournamentFormState,
    update: ((CreateTournamentFormState) -> CreateTournamentFormState) -> Unit
) {
    Text("5. Organizer Contact Details", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = state.organizerName,
        onValueChange = { v -> update { it.copy(organizerName = v) } },
        label = { Text("Organizer Name*") },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = state.organizationName,
        onValueChange = { v -> update { it.copy(organizationName = v) } },
        label = { Text("Club / Association Name") },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(12.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = state.organizerPhone,
            onValueChange = { v -> update { it.copy(organizerPhone = v) } },
            label = { Text("Contact Phone Number*") },
            modifier = Modifier.weight(1f)
        )
        OutlinedTextField(
            value = state.organizerWhatsapp,
            onValueChange = { v -> update { it.copy(organizerWhatsapp = v) } },
            label = { Text("WhatsApp Number") },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun Step6Evidence(
    state: CreateTournamentFormState,
    update: ((CreateTournamentFormState) -> CreateTournamentFormState) -> Unit
) {
    Text("6. Poster & Proof", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = "Attach tournament poster image, trophy photos, or official letter to receive the 'VERIFIED' trust badge.",
        color = SoftSteel,
        fontSize = 13.sp
    )

    Spacer(modifier = Modifier.height(16.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.CloudUpload, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Attach Poster or Trophy Photo", fontWeight = FontWeight.Bold)
            Text("JPG, PNG (Max 10MB)", fontSize = 12.sp, color = SoftSteel)
        }
    }
}

@Composable
fun Step7Preview(
    state: CreateTournamentFormState
) {
    Text("7. Preview & Duplicate Audit", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    Spacer(modifier = Modifier.height(14.dp))

    // Duplicate event warning if any found
    if (state.possibleDuplicates.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = AlertRed.copy(alpha = 0.15f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = AlertRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Potential Duplicate Tournament Detected!", fontWeight = FontWeight.Bold, color = AlertRed)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "A tournament with a similar name in ${state.district} on ${state.date} has already been posted. Please review before proceeding.",
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = state.name.ifBlank { "Tournament Title" }, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "${state.date} • ${state.startTime}", color = ElectricCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = "${state.venueName}, ${state.district}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Prize: ₹${state.prizeAmount} | Entry Fee: ₹${state.entryFee}", fontWeight = FontWeight.Bold)
            Text(text = "Weight Limit: ${state.weightLimitKg} KG | Category: ${state.category} (${state.gender})")
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Organizer: ${state.organizerName} (${state.organizerPhone})", fontSize = 12.sp, color = SoftSteel)
        }
    }
}
