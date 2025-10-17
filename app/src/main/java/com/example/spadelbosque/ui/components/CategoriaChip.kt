package com.example.spadelbosque.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Chip verde con texto blanco (estilo SPA del Bosque)
 * Reutilizable tanto para categorías como etiquetas.
 */
@Composable
fun CategoriaChip(
    texto: String,
    selected: Boolean = false,
    modifier: Modifier = Modifier
) {
    val background = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surface
    }

    val textColor = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        color = background,
        contentColor = textColor,
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

