package com.sebinesh.kabaddihero.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.core.di.ServiceLocator
import com.sebinesh.kabaddihero.data.model.UserRole
import com.sebinesh.kabaddihero.ui.components.CourtBackdrop
import com.sebinesh.kabaddihero.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToSaved: () -> Unit,
    onNavigateToRules: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onLogout: () -> Unit
) {
    val authRepo = ServiceLocator.authRepository
    val user by authRepo.currentUserState.collectAsState()

    val isAdminOrStaff = user?.role?.isStaff() == true

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile", fontWeight = FontWeight.Bold, color = Color.White) },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
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
                // User info header
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MatClay),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(46.dp)
                    )
                }

                Text(
                    text = user?.displayName ?: "Player",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .background(ElectricCyan.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Role: ${user?.role?.name ?: "USER"}",
                        color = ElectricCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Menu items
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column {
                        ProfileMenuItem(
                            icon = Icons.Default.Bookmark,
                            title = "Saved Tournaments",
                            onClick = onNavigateToSaved
                        )
                        Divider()
                        ProfileMenuItem(
                            icon = Icons.Default.MenuBook,
                            title = "Rules Center",
                            onClick = onNavigateToRules
                        )
                        if (isAdminOrStaff) {
                            Divider()
                            ProfileMenuItem(
                                icon = Icons.Default.AdminPanelSettings,
                                title = "Admin Dashboard",
                                titleColor = BonusYellow,
                                onClick = onNavigateToAdmin
                            )
                        }
                        Divider()
                        ProfileMenuItem(
                            icon = Icons.Default.Settings,
                            title = "Settings",
                            onClick = onNavigateToSettings
                        )
                    }
                }

                // Logout Button
                Button(
                    onClick = {
                        authRepo.signOut()
                        onLogout()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_profile_logout"),
                    colors = ButtonDefaults.buttonColors(containerColor = AlertRed.copy(alpha = 0.85f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Log Out", fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "Kabaddi Hero • Version 2026.1 • by Sebinesh",
                    color = SoftSteel,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = titleColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(14.dp))
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = titleColor)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = SoftSteel, modifier = Modifier.size(14.dp))
    }
}
