package com.sebinesh.kabaddihero.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.ui.components.CourtBackdrop
import com.sebinesh.kabaddihero.ui.theme.*

data class OnboardingPage(
    val titleTa: String,
    val titleEn: String,
    val descTa: String,
    val descEn: String,
    val icon: ImageVector
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            titleTa = "Discover Local Tournaments",
            titleEn = "Discover Local Tournaments",
            descTa = "Explore grassroots clay court to championship mat tournaments happening in your district and nearby.",
            descEn = "Explore grassroot clay court to championship mat tournaments happening in your district and nearby.",
            icon = Icons.Default.LocationOn
        ),
        OnboardingPage(
            titleTa = "Team Registration & Live Score",
            titleEn = "Team Registration & Live Score",
            descTa = "Register teams with roster and weight details. Follow point-by-point live match action.",
            descEn = "Register teams with roster and weight details. Follow point-by-point live match action.",
            icon = Icons.Default.Scoreboard
        ),
        OnboardingPage(
            titleTa = "Kabaddi Hero AI Assistant",
            titleEn = "Kabaddi Hero AI Assistant",
            descTa = "Get instant rules clarification, weight eligibility checks, and poster analysis powered by Gemini AI.",
            descEn = "Get instant rules clarification, weight eligibility checks, and poster analysis powered by Gemini.",
            icon = Icons.Default.SmartToy
        )
    )

    var currentPage by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StadiumNight)
    ) {
        CourtBackdrop(alpha = 0.12f)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Kabaddi Hero",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "by Sebinesh",
                        color = BonusYellow,
                        fontSize = 12.sp
                    )
                }

                TextButton(onClick = onFinish) {
                    Text(text = "Skip", color = SoftSteel, fontSize = 14.sp)
                }
            }

            // Page Content
            val page = pages[currentPage]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MatClay.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = page.icon,
                        contentDescription = null,
                        tint = ElectricCyan,
                        modifier = Modifier.size(52.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = page.titleTa,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = page.titleEn,
                    color = ElectricCyan,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = page.descTa,
                    color = ChalkWhite.copy(alpha = 0.85f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }

            // Bottom controls: indicators + button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Indicators
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    pages.indices.forEach { index ->
                        Box(
                            modifier = Modifier
                                .size(if (index == currentPage) 24.dp else 8.dp, 8.dp)
                                .clip(CircleShape)
                                .background(if (index == currentPage) ElectricCyan else Color.DarkGray)
                        )
                    }
                }

                Button(
                    onClick = {
                        if (currentPage < pages.size - 1) {
                            currentPage++
                        } else {
                            onFinish()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MatClay)
                ) {
                    Text(
                        text = if (currentPage == pages.size - 1) "Get Started" else "Next",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
