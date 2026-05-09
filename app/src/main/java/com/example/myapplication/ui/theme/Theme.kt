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
    surfaceVariant = DarkCard,

    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,

    onBackground = TextWhite,
    onSurface = TextWhite,
    onSurfaceVariant = TextMuted,

    primaryContainer = Color(0xFF14532D),
    secondaryContainer = Color(0xFF1E3A8A),
    tertiaryContainer = Color(0xFF4C1D95)
)

private val LightColorScheme = lightColorScheme(

    primary = PrimaryGreen,
    secondary = EnergyBlue,
    tertiary = EnergyOrange,

    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightCard,

    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,

    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,

    primaryContainer = Color(0xFFDCFCE7),
    secondaryContainer = Color(0xFFDBEAFE),
    tertiaryContainer = Color(0xFFFEF3C7)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {

    val colors =
        if (darkTheme) DarkColorScheme
        else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}