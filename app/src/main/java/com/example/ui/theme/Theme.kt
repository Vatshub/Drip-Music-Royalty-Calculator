package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CyberColorScheme = darkColorScheme(
    primary = CyberGreen,
    secondary = CyberPurple,
    tertiary = CyberGold,
    background = CyberBg,
    surface = CyberBody,
    onPrimary = Color(0xFF0A0A0A),
    onSecondary = Color.White,
    onBackground = CyberText,
    onSurface = CyberText,
    error = CyberError,
    onError = Color.White
)

@Composable
fun DripRoyaltyTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CyberColorScheme,
        typography = Typography,
        content = content
    )
}
