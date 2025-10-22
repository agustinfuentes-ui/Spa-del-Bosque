package com.example.spadelbosque.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Chip con animación de color cuando se selecciona.
 * Reutilizable para categorías y etiquetas.
 */
@Composable
fun CategoriaChip(
    texto: String,
    selected: Boolean = false,
    modifier: Modifier = Modifier
) {
    // ANIMACIÓN: Color animado según selección
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(durationMillis = 300),  // Duración de 300ms
        label = "chip_background"
    )

    val textColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(durationMillis = 300),
        label = "chip_text"
    )

    Surface(
        color = backgroundColor,  // ← Color animado
        contentColor = textColor,  // ← Color animado
        shape = RoundedCornerShape(50),
        border = if (!selected)
            androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        else null,
        modifier = modifier
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}