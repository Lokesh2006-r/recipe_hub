package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DarkSage,
    onPrimary = ForestGreenDark,
    primaryContainer = ForestGreenContainerDark,
    onPrimaryContainer = DarkText,
    secondary = DarkSage,
    onSecondary = ForestGreenDark,
    secondaryContainer = Color(0xFF283830),
    onSecondaryContainer = Color(0xFFCCE4D6),
    tertiary = OchreAccent,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFB0BEB5),
    outline = Color(0xFF38463D),
    error = ErrorRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    onPrimary = Color.White,
    primaryContainer = SageGreenLight,
    onPrimaryContainer = ForestGreenDark,
    secondary = SageGreen,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE4EDE7),
    onSecondaryContainer = ForestGreenDark,
    tertiary = OchreAccent,
    onTertiary = Color.White,
    background = WarmCream,
    onBackground = CharcoalText,
    surface = WarmSurface,
    onSurface = CharcoalText,
    surfaceVariant = WarmSurfaceVariant,
    onSurfaceVariant = WarmGray,
    outline = WarmBorder,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun RecipeHubTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
