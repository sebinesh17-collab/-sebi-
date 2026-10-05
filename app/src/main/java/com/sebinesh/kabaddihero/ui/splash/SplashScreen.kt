package com.sebinesh.kabaddihero.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onAnimationComplete: () -> Unit
) {
    // Typewriter steps:
    // Typewriter steps: 1: K, 2: Ka, 3: Kabaddi, 4: Kabaddi..., 5: Kabaddi... Kabaddi..., 6: Chant, 7: KABADDI HERO
    var step by remember { mutableStateOf(0) }
    var showAuthor by remember { mutableStateOf(false) }

    // Court lines animation
    val courtAlpha by animateFloatAsState(
        targetValue = if (step >= 1) 0.45f else 0.1f,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "courtAlpha"
    )

    // Player raid run position
    val infiniteTransition = rememberInfiniteTransition(label = "raidTransition")
    val raiderOffset by infiniteTransition.animateFloat(
        initialValue = -40f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "raiderMovement"
    )

    LaunchedEffect(Unit) {
        delay(400)
        step = 1 // K
        delay(350)
        step = 2 // Ka
        delay(350)
        step = 3 // Kabaddi
        delay(450)
        step = 4 // Kabaddi...
        delay(450)
        step = 5 // Kabaddi... Kabaddi...
        delay(450)
        step = 6 // Kabaddi... Kabaddi... Kabaddi...
        delay(550)
        step = 7 // KABADDI HERO
        showAuthor = true
        delay(1200)
        onAnimationComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF1E2836), StadiumNight, Color(0xFF040608)),
                    center = Offset(500f, 600f),
                    radius = 1200f
                )
            )
            .clickable { onAnimationComplete() } // Allow skip
    ) {
        // Subtle illuminated Kabaddi Court canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            val padding = 40f
            val cWidth = w - (padding * 2)
            val cHeight = h * 0.55f
            val topY = h * 0.15f

            val stroke = Stroke(width = 2.5f)
            val chalk = ChalkWhite.copy(alpha = courtAlpha)
            val bonus = BonusYellow.copy(alpha = courtAlpha * 1.2f)

            // Court boundary
            drawRect(
                color = chalk,
                topLeft = Offset(padding, topY),
                size = Size(cWidth, cHeight),
                style = stroke
            )

            // Midline
            val midY = topY + (cHeight / 2)
            drawLine(
                color = chalk,
                start = Offset(padding, midY),
                end = Offset(padding + cWidth, midY),
                strokeWidth = 4f
            )

            // Baulk lines
            val baulkDist = cHeight * 0.22f
            drawLine(
                color = chalk,
                start = Offset(padding + (cWidth * 0.08f), midY - baulkDist),
                end = Offset(padding + cWidth - (cWidth * 0.08f), midY - baulkDist),
                strokeWidth = 2.5f
            )
            drawLine(
                color = chalk,
                start = Offset(padding + (cWidth * 0.08f), midY + baulkDist),
                end = Offset(padding + cWidth - (cWidth * 0.08f), midY + baulkDist),
                strokeWidth = 2.5f
            )

            // Bonus lines
            val bonusDist = cHeight * 0.31f
            drawLine(
                color = bonus,
                start = Offset(padding + (cWidth * 0.08f), midY - bonusDist),
                end = Offset(padding + cWidth - (cWidth * 0.08f), midY - bonusDist),
                strokeWidth = 2.5f
            )
            drawLine(
                color = bonus,
                start = Offset(padding + (cWidth * 0.08f), midY + bonusDist),
                end = Offset(padding + cWidth - (cWidth * 0.08f), midY + bonusDist),
                strokeWidth = 2.5f
            )

            // Raider dynamic silhouette representation crossing the line
            val raiderX = (w / 2) + raiderOffset
            val raiderY = midY - (baulkDist * 0.5f)

            // Spotlight glow under player
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(ElectricCyan.copy(alpha = 0.35f), Color.Transparent),
                    center = Offset(raiderX, raiderY),
                    radius = 90f
                ),
                radius = 90f,
                center = Offset(raiderX, raiderY)
            )

            // Athletic raider silhouette head & body
            drawCircle(
                color = ChalkWhite.copy(alpha = 0.85f),
                radius = 10f,
                center = Offset(raiderX, raiderY - 30f)
            )
            // Torso
            drawLine(
                color = ChalkWhite.copy(alpha = 0.85f),
                start = Offset(raiderX, raiderY - 20f),
                end = Offset(raiderX + 10f, raiderY + 10f),
                strokeWidth = 6f
            )
            // Forward reaching raid arm
            drawLine(
                color = ElectricCyan,
                start = Offset(raiderX + 2f, raiderY - 14f),
                end = Offset(raiderX + 28f, raiderY - 8f),
                strokeWidth = 4f
            )
            // Leaping legs
            drawLine(
                color = ChalkWhite.copy(alpha = 0.85f),
                start = Offset(raiderX + 10f, raiderY + 10f),
                end = Offset(raiderX + 24f, raiderY + 32f),
                strokeWidth = 5f
            )
            drawLine(
                color = ChalkWhite.copy(alpha = 0.85f),
                start = Offset(raiderX + 10f, raiderY + 10f),
                end = Offset(raiderX - 16f, raiderY + 28f),
                strokeWidth = 5f
            )
        }

        // Animated Broadcast Typewriter Text
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val chantText = when (step) {
                1 -> "K"
                2 -> "Ka"
                3 -> "Kabaddi"
                4 -> "Kabaddi..."
                5 -> "Kabaddi... Kabaddi..."
                6 -> "Kabaddi... Kabaddi... Kabaddi..."
                7 -> "KABADDI HERO"
                else -> ""
            }

            if (step in 1..6) {
                Text(
                    text = chantText,
                    color = ElectricCyan,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )
            } else if (step == 7) {
                Text(
                    text = "KABADDI HERO",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "KABADDI HERO",
                    color = MatClay,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp,
                    textAlign = TextAlign.Center
                )
            }

            if (showAuthor) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "by Sebinesh",
                    color = BonusYellow,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}
