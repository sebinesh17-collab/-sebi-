package com.sebinesh.kabaddihero.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.core.constants.AppConstants
import com.sebinesh.kabaddihero.core.di.ServiceLocator
import com.sebinesh.kabaddihero.ui.theme.ElectricCyan
import com.sebinesh.kabaddihero.ui.theme.StadiumNight
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    val prefs = ServiceLocator.dataStorePrefs
    val coroutineScope = rememberCoroutineScope()

    val currentLang by prefs.selectedLanguage.collectAsState(initial = AppConstants.LANG_TAMIL)
    var notifyTournaments by remember { mutableStateOf(true) }
    var notifyLiveMatches by remember { mutableStateOf(true) }
    var dataSaver by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold, color = Color.White) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Language Selection
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Language", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = currentLang == AppConstants.LANG_TAMIL,
                            onClick = { coroutineScope.launch { prefs.setLanguage(AppConstants.LANG_TAMIL) } }
                        )
                        Text("English (Default)")
                        Spacer(modifier = Modifier.width(20.dp))
                        RadioButton(
                            selected = currentLang == AppConstants.LANG_ENGLISH,
                            onClick = { coroutineScope.launch { prefs.setLanguage(AppConstants.LANG_ENGLISH) } }
                        )
                        Text("English")
                    }
                }
            }

            // Notifications
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Notifications", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("New Tournaments Announcements", fontSize = 14.sp)
                        Switch(checked = notifyTournaments, onCheckedChange = { notifyTournaments = it })
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Live Matches & Score Alerts", fontSize = 14.sp)
                        Switch(checked = notifyLiveMatches, onCheckedChange = { notifyLiveMatches = it })
                    }
                }
            }

            // Data Saver & Performance
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Data Saver Mode", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Optimizes image quality for faster loading on 2G/3G mobile networks.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(checked = dataSaver, onCheckedChange = { dataSaver = it })
                    }
                }
            }

            // App Check & Security info
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Security & Attestation", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Protected by Firebase App Check and Google Play Integrity.", fontSize = 12.sp, color = ElectricCyan)
                }
            }
        }
    }
}
