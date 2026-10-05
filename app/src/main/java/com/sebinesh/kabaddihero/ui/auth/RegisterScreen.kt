package com.sebinesh.kabaddihero.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.sebinesh.kabaddihero.data.model.UserRole
import com.sebinesh.kabaddihero.ui.components.CourtBackdrop
import com.sebinesh.kabaddihero.ui.components.ErrorBanner
import com.sebinesh.kabaddihero.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val authRepo = ServiceLocator.authRepository
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.PLAYER) }
    var roleDropdownExpanded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StadiumNight)
    ) {
        CourtBackdrop(alpha = 0.15f)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create Account",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "JOIN KABADDI HERO • by Sebinesh",
                color = ElectricCyan,
                fontSize = 12.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StadiumNightAlt),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    if (errorMessage != null) {
                        ErrorBanner(message = errorMessage!!)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it; errorMessage = null },
                        label = { Text("Full Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ElectricCyan) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_reg_name"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; errorMessage = null },
                        label = { Text("Email Address") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = ElectricCyan) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_reg_email"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; errorMessage = null },
                        label = { Text("Password (min 6 chars)") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ElectricCyan) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_reg_password"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Role Selector Dropdown
                    ExposedDropdownMenuBox(
                        expanded = roleDropdownExpanded,
                        onExpandedChange = { roleDropdownExpanded = !roleDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = when (selectedRole) {
                                UserRole.PLAYER -> "Player"
                                UserRole.TEAM_MANAGER -> "Team Manager"
                                UserRole.ORGANIZER -> "Tournament Organizer"
                                else -> "Fan / Viewer"
                            },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Account Role") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleDropdownExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )

                        ExposedDropdownMenu(
                            expanded = roleDropdownExpanded,
                            onDismissRequest = { roleDropdownExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Player") },
                                onClick = { selectedRole = UserRole.PLAYER; roleDropdownExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Team Manager") },
                                onClick = { selectedRole = UserRole.TEAM_MANAGER; roleDropdownExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Tournament Organizer") },
                                onClick = { selectedRole = UserRole.ORGANIZER; roleDropdownExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Fan / Viewer") },
                                onClick = { selectedRole = UserRole.USER; roleDropdownExpanded = false }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (name.isBlank() || email.isBlank() || password.length < 6) {
                                errorMessage = "Please fill all details correctly (minimum 6 character password)."
                                return@Button
                            }
                            isLoading = true
                            coroutineScope.launch {
                                val res = authRepo.signUp(email.trim(), password, name.trim(), selectedRole)
                                isLoading = false
                                if (res is Result.Success) {
                                    onRegisterSuccess()
                                } else if (res is Result.Error) {
                                    errorMessage = res.userMessage
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_register_submit"),
                        colors = ButtonDefaults.buttonColors(containerColor = MatClay),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Text(text = "Sign Up", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Already have an account?", color = SoftSteel, fontSize = 14.sp)
                TextButton(onClick = onNavigateToLogin) {
                    Text(text = "Sign In", color = ElectricCyan, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
