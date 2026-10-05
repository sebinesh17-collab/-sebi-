package com.sebinesh.kabaddihero.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.core.di.ServiceLocator
import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.ui.components.CourtBackdrop
import com.sebinesh.kabaddihero.ui.components.ErrorBanner
import com.sebinesh.kabaddihero.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToPhoneAuth: () -> Unit,
    onGuestBrowse: () -> Unit
) {
    val authRepo = ServiceLocator.authRepository
    val coroutineScope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var guestName by remember { mutableStateOf("") }
    var isGuestLoading by remember { mutableStateOf(false) }

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
            // App Branding
            Text(
                text = "Kabaddi Hero",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "KABADDI HERO • by Sebinesh",
                color = ElectricCyan,
                fontSize = 12.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StadiumNightAlt),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Sign In",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Sign in to post tournaments or register teams.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SoftSteel
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (errorMessage != null) {
                        ErrorBanner(message = errorMessage!!)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; errorMessage = null },
                        label = { Text("Email Address") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = ElectricCyan) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_login_email"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; errorMessage = null },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ElectricCyan) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = SoftSteel
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_login_password"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sign In Button
                    Button(
                        onClick = {
                            if (email.isBlank() || password.isBlank()) {
                                errorMessage = "Please enter your email and password."
                                return@Button
                            }
                            isLoading = true
                            coroutineScope.launch {
                                val res = authRepo.signIn(email.trim(), password)
                                isLoading = false
                                if (res is Result.Success) {
                                    onLoginSuccess()
                                } else if (res is Result.Error) {
                                    errorMessage = res.userMessage
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_login_submit"),
                        colors = ButtonDefaults.buttonColors(containerColor = MatClay),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Text(text = "Sign In", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Phone Auth button
                    OutlinedButton(
                        onClick = onNavigateToPhoneAuth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Sign in with Phone OTP", fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Divider: OR QUICK GUEST LOGIN
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = SoftSteel.copy(alpha = 0.3f))
                Text(
                    text = "  OR QUICK GUEST LOGIN  ",
                    color = BonusYellow,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = SoftSteel.copy(alpha = 0.3f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Guest Login Card (Login using only name)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StadiumNightAlt),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA855F7).copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFA855F7).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = BonusYellow, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Quick Guest Login",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Login instantly using only your name",
                                style = MaterialTheme.typography.bodySmall,
                                color = SoftSteel
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = guestName,
                        onValueChange = { guestName = it },
                        label = { Text("Your Name (e.g. Sebinesh, Alex, Rahul)") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ElectricCyan) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_guest_name"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            val nameToUse = guestName.trim().ifBlank { "Guest Player" }
                            isGuestLoading = true
                            coroutineScope.launch {
                                authRepo.signInAsGuest(nameToUse)
                                isGuestLoading = false
                                onLoginSuccess()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_guest_browse"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA855F7)),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isGuestLoading
                    ) {
                        if (isGuestLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Icon(Icons.Default.FlashOn, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (guestName.isNotBlank()) "Quick Login as ${guestName.trim()}" else "Quick Login as Guest",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Register Link
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Don't have an account?", color = SoftSteel, fontSize = 14.sp)
                TextButton(onClick = onNavigateToRegister) {
                    Text(text = "Sign Up", color = ElectricCyan, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
