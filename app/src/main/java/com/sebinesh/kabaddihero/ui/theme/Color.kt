package com.sebinesh.kabaddihero.ui.theme

import androidx.compose.ui.graphics.Color

// Clean Dark Purple Master Palette
val DarkPurpleBackground = Color(0xFF0D0714)       // Ultra-deep dark obsidian purple
val DarkPurpleSurface = Color(0xFF180E28)          // Deep purple surface for app bars & sheets
val DarkPurpleSurfaceElevated = Color(0xFF221438)  // Elevated surface for cards
val DarkPurpleCard = Color(0xFF281842)             // Interactive card fill
val DarkPurpleCardHighlight = Color(0xFF351F56)    // Card highlight state
val DarkPurpleBorder = Color(0xFF3F2766)           // Clean subtle purple border
val DarkPurpleBorderSubtle = Color(0xFF2F1D4C)     // Low contrast divider

// Vibrant Purple Brand Colors
val PurplePrimary = Color(0xFFA855F7)              // Luminous Amethyst / Electric Purple (Tailwind 500)
val PurplePrimaryBright = Color(0xFFC084FC)        // Bright Lilac Purple (Tailwind 400)
val PurplePrimaryDark = Color(0xFF7E22CE)          // Deep Royal Purple (Tailwind 700)
val PurplePrimaryDeep = Color(0xFF581C87)          // Deep Plum (Tailwind 900)
val PurpleContainer = Color(0xFF38155C)            // Rich container fill
val PurpleAccent = Color(0xFFD946EF)               // Fuchsia neon accent (Tailwind fuchsia-500)
val PurpleGlow = Color(0xFFE879F9)                 // Highlight glow

// Complementary Highlights for Dark Purple
val PurpleGold = Color(0xFFFBBF24)                 // Warm Golden Amber (stunning for trophies & live points)
val PurpleCyan = Color(0xFF38BDF8)                 // Sky Blue / Cyan accent
val AlertRed = Color(0xFFEF4444)                   // Clean red for errors & live tags
val WhistleGreen = Color(0xFF10B981)               // Clean emerald green for success

// Crisp Typography Colors for Dark Purple
val TextPrimary = Color(0xFFFAF5FF)                // Off-white with faint lavender tint
val TextSecondary = Color(0xFFD8B4FE)              // Soft lavender purple secondary text
val TextTertiary = Color(0xFF9E8FB2)               // Muted slate purple caption text

// Backward-compatibility aliases for seamless app-wide integration
val MatClay = PurplePrimary
val MatClayDeep = PurplePrimaryDark
val ChalkWhite = TextPrimary
val StadiumNight = DarkPurpleBackground
val StadiumNightAlt = DarkPurpleSurface
val ElectricCyan = PurplePrimaryBright
val BonusYellow = PurpleGold
val SoftSteel = TextTertiary

val ClaySurfaceLight = DarkPurpleBackground
val ClayCardLight = DarkPurpleSurfaceElevated
val TextPrimaryLight = TextPrimary
val TextSecondaryLight = TextSecondary
