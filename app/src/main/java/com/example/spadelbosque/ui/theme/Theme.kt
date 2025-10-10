package com.example.spadelbosque.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = VerdeHoja,
    onPrimary = Color.White,
    primaryContainer = VerdeAgua,
    onPrimaryContainer = Color.White,
    secondary = VerdeAgua,
    onSecondary = Color.White,
    background = VerdeFondo,
    surface = Color.White,
    onBackground = TextoPrimario,
    onSurface = TextoPrimario
)

private val DarkColors = darkColorScheme(
    primary = VerdeHoja,
    onPrimary = Color.White,
    primaryContainer = VerdeAgua,
    onPrimaryContainer = Color.White,
    secondary = VerdeAgua,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun SpaTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography, content = content)
}
