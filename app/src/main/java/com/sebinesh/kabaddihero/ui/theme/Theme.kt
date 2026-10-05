package com.sebinesh.kabaddihero.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkPurpleColorScheme = darkColorScheme(
    primary = PurplePrimary,
    onPrimary = Color.White,
    primaryContainer = PurpleContainer,
    onPrimaryContainer = Color(0xFFF3E8FF),
    secondary = PurplePrimaryBright,
    onSecondary = DarkPurpleBackground,
    secondaryContainer = DarkPurpleCard,
    onSecondaryContainer = PurplePrimaryBright,
    tertiary = PurpleGold,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF452B00),
    onTertiaryContainer = Color(0xFFFFDF9E),
    background = DarkPurpleBackground,
    onBackground = TextPrimary,
    surface = DarkPurpleSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkPurpleCard,
    onSurfaceVariant = TextSecondary,
    outline = DarkPurpleBorder,
    outlineVariant = DarkPurpleBorderSubtle,
    error = AlertRed,
    onError = Color.White
)

@Composable
fun KabaddiHeroTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    // Deliver clean dark purple UI across the entire application
    MaterialTheme(
        colorScheme = DarkPurpleColorScheme,
        typography = Typography,
        content = content
    )
}
