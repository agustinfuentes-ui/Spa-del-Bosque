package com.example.spadelbosque.ui.screens.nosotros

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spadelbosque.R
import com.example.spadelbosque.viewmodel.NosotrosViewModel

@Composable
fun NosotrosScreen(viewModel: NosotrosViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp) // Espacio entre secciones
    ) {
        item {
            InfoSection(title = "Ubicación y Contacto") {
                InfoRow(icon = Icons.Default.LocationOn, text = uiState.direccion)
                InfoRow(icon = Icons.Default.Phone, text = uiState.telefono)
                InfoRow(icon = Icons.Default.Email, text = uiState.email)
            }
        }

        item {
            InfoSection(title = "Horarios") {
                uiState.horarios.forEach { horario ->
                    InfoRow(icon = Icons.Default.Schedule, text = horario)
                }
            }
        }

        item {
            MapaCard(direccion = uiState.direccion)
        }
    }
}

@Composable
private fun InfoSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content
        )
    }
}

@Composable
private fun InfoRow(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null, // El texto ya describe el contenido
            tint = MaterialTheme.colorScheme.primary
        )
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}

// Card que muestra el mapa y abre Google Maps al hacer clic
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapaCard(direccion: String) {
    val uriHandler = LocalUriHandler.current
    val urlMapa = "https://www.google.com/maps/search/?api=1&query=${Uri.encode(direccion)}"

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { uriHandler.openUri(urlMapa) }
    ) {
        Image(
            painter = painterResource(id = R.drawable.mapa_placeholder),
            contentDescription = "Mapa de ubicación del spa",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
    }
}
