package com.sebinesh.kabaddihero.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.ui.theme.BonusYellow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CountdownText(
    targetDateStr: String,
    modifier: Modifier = Modifier,
    color: Color = BonusYellow
) {
    val countdown = calculateCountdown(targetDateStr)
    Text(
        text = countdown,
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

fun calculateCountdown(dateStr: String): String {
    if (dateStr.isBlank()) return "Date TBA"
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val target = sdf.parse(dateStr)?.time ?: return dateStr
        val now = System.currentTimeMillis()
        val diff = target - now

        if (diff <= 0) {
            "Today!"
        } else {
            val days = diff / (1000 * 60 * 60 * 24)
            if (days == 0L) {
                val hours = diff / (1000 * 60 * 60)
                "Starts in $hours hrs"
            } else {
                "$days days left"
            }
        }
    } catch (e: Exception) {
        dateStr
    }
}
