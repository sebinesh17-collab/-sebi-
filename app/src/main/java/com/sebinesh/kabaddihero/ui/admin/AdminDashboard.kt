package com.sebinesh.kabaddihero.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.sebinesh.kabaddihero.data.model.UserRole
import com.sebinesh.kabaddihero.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(
    onBack: () -> Unit
) {
    val authRepo = ServiceLocator.authRepository
    val currentUser by authRepo.currentUserState.collectAsState()
    val tournaments by ServiceLocator.tournamentRepository.tournaments.collectAsState(initial = emptyList())

    var selectedRoleToAssign by remember { mutableStateOf(currentUser?.role ?: UserRole.ADMIN) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard", fontWeight = FontWeight.Bold, color = Color.White) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Metrics Row
            item {
                Text("Platform Metrics:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AdminMetricCard(title = "Total Tournaments", value = "${tournaments.size}", color = ElectricCyan, modifier = Modifier.weight(1f))
                    AdminMetricCard(title = "Pending Review", value = "${tournaments.count { it.verificationStatus == "PENDING_REVIEW" }}", color = BonusYellow, modifier = Modifier.weight(1f))
                    AdminMetricCard(title = "Live Matches", value = "1", color = WhistleGreen, modifier = Modifier.weight(1f))
                }
            }

            // Role Test Switcher (For local testing role-based workflows)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Testing Role Switcher:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Switch roles to test Role-Based Access Control (RBAC) across the app.", fontSize = 12.sp, color = SoftSteel)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(UserRole.PLAYER, UserRole.ORGANIZER, UserRole.REFEREE, UserRole.ADMIN).forEach { r ->
                                FilterChip(
                                    selected = selectedRoleToAssign == r,
                                    onClick = {
                                        selectedRoleToAssign = r
                                        authRepo.updateCurrentUserRole(r)
                                    },
                                    label = { Text(r.name, fontSize = 11.sp) }
                                )
                            }
                        }
                    }
                }
            }

            // Verification Queue Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = StadiumNightAlt),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = WhistleGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Verification & Moderation Queue", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("All newly submitted tournaments and posters are audited before granting the 'VERIFIED' trust badge.", fontSize = 12.sp, color = SoftSteel)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminMetricCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Black, color = color)
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
