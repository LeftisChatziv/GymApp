package com.example.myapplication.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    secondary = NeonBlue,
    tertiary = NeonPurple,

    background = DarkBackground,
    surface = DarkSurface,

    onPrimary = DarkBackground,
    onSecondary = DarkBackground,
    onTertiary = TextWhite,

    onBackground = TextWhite,
    onSurface = TextWhite
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    secondary = EnergyBlue,
    tertiary = EnergyOrange,

    background = LightBackground,
    surface = LightSurface,

    onPrimary = LightSurface,
    onSecondary = LightSurface,
    onTertiary = TextPrimary,

    onBackground = TextPrimary,
    onSurface = TextPrimary
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