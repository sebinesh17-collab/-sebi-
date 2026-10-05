package com.sebinesh.kabaddihero.ui.ai

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.R
import com.sebinesh.kabaddihero.core.di.ServiceLocator
import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.data.model.Tournament
import com.sebinesh.kabaddihero.data.remote.ai.ExtractedPosterData
import com.sebinesh.kabaddihero.ui.components.CourtBackdrop
import com.sebinesh.kabaddihero.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosterAnalyzerScreen(
    onBack: () -> Unit,
    onUseExtractedData: (ExtractedPosterData) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val posterService = ServiceLocator.aiPosterExtractionService

    var isAnalyzing by remember { mutableStateOf(false) }
    var extractedData by remember { mutableStateOf<ExtractedPosterData?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Sample poster trigger or image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                if (bitmap != null) {
                    isAnalyzing = true
                    errorMessage = null
                    coroutineScope.launch {
                        val res = posterService.extractFromPoster(bitmap)
                        isAnalyzing = false
                        if (res is Result.Success) {
                            extractedData = res.data
                        } else if (res is Result.Error) {
                            errorMessage = res.userMessage
                        }
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Could not read poster image."
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Poster Import", fontWeight = FontWeight.Bold, color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkPurpleSurface)
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Automatically Extract Details from Poster",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Upload a tournament poster. Gemini AI will automatically extract date, venue, weight limits, and prize details.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                // Poster Upload / Preview Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkPurpleSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkPurpleBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.DocumentScanner,
                            contentDescription = null,
                            tint = PurplePrimaryBright,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Select Tournament Poster", fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isAnalyzing
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Select Image from Gallery", color = Color.White)
                        }

                        if (isAnalyzing) {
                            Spacer(modifier = Modifier.height(18.dp))
                            CircularProgressIndicator(color = PurplePrimaryBright)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Gemini AI is analyzing poster...", fontSize = 12.sp, color = PurplePrimaryBright)
                        }
                    }
                }

                if (errorMessage != null) {
                    Text(text = errorMessage!!, color = AlertRed, fontSize = 13.sp)
                }

                // Extracted Review Form
                extractedData?.let { data ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = WhistleGreen)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "AI EXTRACTED INFORMATION",
                                    fontWeight = FontWeight.Bold,
                                    color = WhistleGreen,
                                    fontSize = 14.sp
                                )
                            }
                            Text(
                                text = "Please verify extracted tournament details before creating your post:",
                                fontSize = 12.sp,
                                color = SoftSteel
                            )

                            OutlinedTextField(
                                value = data.tournamentName,
                                onValueChange = {},
                                label = { Text("Tournament Name") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = data.date,
                                    onValueChange = {},
                                    label = { Text("Date (YYYY-MM-DD)") },
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = data.startTime,
                                    onValueChange = {},
                                    label = { Text("Start Time") },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            OutlinedTextField(
                                value = "${data.venue}, ${data.district}",
                                onValueChange = {},
                                label = { Text("Venue / Ground") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = data.weightLimit,
                                    onValueChange = {},
                                    label = { Text("Weight Limit (KG)") },
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = "₹${data.prize}",
                                    onValueChange = {},
                                    label = { Text("First Prize Amount") },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Button(
                                onClick = { onUseExtractedData(data) },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = WhistleGreen),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Apply Extracted Details to Form", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
