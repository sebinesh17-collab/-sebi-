package com.sebinesh.kabaddihero.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.core.validation.Validators
import com.sebinesh.kabaddihero.ui.components.CourtBackdrop
import com.sebinesh.kabaddihero.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneAuthScreen(
    onVerificationSuccess: () -> Unit,
    onBack: () -> Unit
) {
    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var codeSent by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Phone OTP Verification", fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = StadiumNight)
            )
        },
        containerColor = StadiumNight
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CourtBackdrop(alpha = 0.12f)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = StadiumNightAlt),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = if (!codeSent) "Mobile Number Registration" else "Enter OTP Code",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (!codeSent) "Enter your 10-digit mobile number." else "Enter the 6-digit OTP code sent to your phone.",
                            color = SoftSteel,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        if (errorText != null) {
                            Text(text = errorText!!, color = AlertRed, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        if (!codeSent) {
                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it; errorText = null },
                                label = { Text("Mobile Number (e.g. 9876543210)") },
                                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = ElectricCyan) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    if (!Validators.isValidPhone(phone)) {
                                        errorText = "Please enter a valid 10-digit mobile number."
                                        return@Button
                                    }
                                    codeSent = true
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MatClay),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(text = "Send OTP", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            OutlinedTextField(
                                value = otp,
                                onValueChange = { otp = it; errorText = null },
                                label = { Text("6-Digit OTP Code") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    if (otp.length < 6) {
                                        errorText = "Please enter the 6-digit OTP code."
                                        return@Button
                                    }
                                    onVerificationSuccess()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = WhistleGreen),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(text = "Verify & Continue", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
