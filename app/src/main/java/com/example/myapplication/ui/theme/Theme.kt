package com.example.myapplication.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val DarkColorScheme = darkColorScheme(

    primary = NeonGreen,
    secondary = NeonBlue,
    tertiary = NeonPurple,

    background = DarkBackground,
    surface = DarkSurface,

    onPrimary = Color(0xFF0B0F14),
    onSecondary = Color(0xFF0B0F14),
    onTertiary = Color(0xFF0B0F14),

    onBackground = TextWhite,
    onSurface = TextWhite,

    surfaceVariant = DarkCard,
    onSurfaceVariant = TextMuted
)

private val LightColorScheme = lightColorScheme(

    primary = PrimaryGreen,
    secondary = EnergyBlue,
    tertiary = EnergyOrange,

    background = LightBackground,
    surface = LightSurface,

    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,

    onBackground = TextPrimary,
    onSurface = TextPrimary,

    surfaceVariant = LightCard,
    onSurfaceVariant = TextSecondary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}