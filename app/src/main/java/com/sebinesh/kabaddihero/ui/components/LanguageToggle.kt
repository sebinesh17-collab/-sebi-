package com.sebinesh.kabaddihero.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.core.constants.AppConstants
import com.sebinesh.kabaddihero.ui.theme.ElectricCyan

@Composable
fun LanguageToggle(
    currentLang: String,
    onLangChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isTamil = currentLang == AppConstants.LANG_TAMIL

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black.copy(alpha = 0.3f))
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(if (isTamil) ElectricCyan else Color.Transparent)
                .clickable { onLangChanged(AppConstants.LANG_TAMIL) }
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "English",
                color = if (isTamil) Color.Black else Color.White,
                fontSize = 12.sp,
                fontWeight = if (isTamil) FontWeight.Bold else FontWeight.Normal
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(if (!isTamil) ElectricCyan else Color.Transparent)
                .clickable { onLangChanged(AppConstants.LANG_ENGLISH) }
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "EN",
                color = if (!isTamil) Color.Black else Color.White,
                fontSize = 12.sp,
                fontWeight = if (!isTamil) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
