package com.sebinesh.kabaddihero.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.ui.theme.AlertRed
import com.sebinesh.kabaddihero.ui.theme.BonusYellow
import com.sebinesh.kabaddihero.ui.theme.WhistleGreen

@Composable
fun VerificationBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, icon, label) = when (status.uppercase()) {
        "VERIFIED" -> Quadruple(
            WhistleGreen.copy(alpha = 0.15f),
            WhistleGreen,
            Icons.Default.CheckCircle,
            "Verified"
        )
        "PENDING_REVIEW" -> Quadruple(
            BonusYellow.copy(alpha = 0.15f),
            BonusYellow,
            Icons.Default.HourglassTop,
            "Under Review"
        )
        "NEEDS_CORRECTION" -> Quadruple(
            BonusYellow.copy(alpha = 0.2f),
            Color(0xFFE67E22),
            Icons.Default.Warning,
            "Needs Correction"
        )
        "REJECTED" -> Quadruple(
            AlertRed.copy(alpha = 0.15f),
            AlertRed,
            Icons.Default.Warning,
            "Rejected"
        )
        else -> Quadruple(
            Color.Gray.copy(alpha = 0.15f),
            Color.LightGray,
            Icons.Default.Info,
            "Unverified"
        )
    }

    Row(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = textColor,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
