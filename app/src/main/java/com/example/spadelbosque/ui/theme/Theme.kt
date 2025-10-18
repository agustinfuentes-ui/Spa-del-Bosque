package com.example.spadelbosque.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    // Marca
    primary = VerdeHoja,
    onPrimary = Color.White,
    primaryContainer = VerdeAgua,
    onPrimaryContainer = Color.White,

    // Secundario (usamos el verde agua como acento suave)
    secondary = VerdeAgua,
    onSecondary = Color.White,
    secondaryContainer = VerdeAgua.copy(alpha = .85f),
    onSecondaryContainer = Color.White,

    // Superficies y fondos
    background = Color.White,          // fondo sutil como en la web
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = TextoPrimario,
    surfaceVariant = GrisClaro.copy(alpha = .25f),
    onSurfaceVariant = TextoSecundario,

    //Chip y textos
    outlineVariant = VerdeHoja,

    // Bordes/outline ~ gris claro
    outline = GrisClaro,

    // Estados
    error = Color(0xFFB3261E),
    onError = Color.White
)

private val DarkColors = darkColorScheme(
    //Marca
    primary = VerdeHoja,
    onPrimary = Color.White, // alto contraste no puro blanco en dark
    primaryContainer = VerdeAgua, //Color(0xFF0A5F56),
    onPrimaryContainer = Color.White,

    // Secundario
    secondary = VerdeAgua,
    onSecondary = Color.White,
    secondaryContainer = VerdeAgua.copy(alpha = .85f),
    onSecondaryContainer = Color.White,

    // Superficies y fondos
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    surfaceVariant = Color.White,//Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFDDDDDD),

    //Chip y textos
    outlineVariant = Color.White,//Color(0xFF3A3A3A),

    //Bordes/outline
    outline = Color.White/*Color(0xFF3A3A3A)*/,

    // Estados
    error = Color(0xFFB3261E),
    onError = Color.Black
)

@Composable
fun SpaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography, // ver type.kt
        content = content
    )
}

