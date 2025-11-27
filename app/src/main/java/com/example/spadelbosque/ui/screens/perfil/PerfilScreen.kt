package com.example.spadelbosque.ui.screens.perfil

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.spadelbosque.viewmodel.AuthViewModel
import com.example.spadelbosque.viewmodel.PerfilViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    authVm: AuthViewModel,
    perfilVm: PerfilViewModel,
    onIrLogin: () -> Unit,
    onEditar: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val session by authVm.sesionState.collectAsStateWithLifecycle()
    val ui by perfilVm.ui.collectAsState()
    LaunchedEffect(Unit) { perfilVm.cargar() }


    // Launchers para imagen
    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? -> perfilVm.setPhoto(uri) }

    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bmp: Bitmap? -> bmp?.let { perfilVm.setPhotoFromBitmap(it) } }

    var showEdit by remember { mutableStateOf(false) }

    Scaffold(topBar = { TopAppBar(title = { Text("Mi Perfil") }) }) { p ->
        val scroll = rememberScrollState()
        Column(Modifier
            .padding(p)
            .verticalScroll(scroll)
            .padding(16.dp))  {
            if (ui.cargando) {
                LinearProgressIndicator()
                return@Column
            }

            // Foto
            ui.photoUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

            } ?: run {
                // Si no hay foto
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Sin foto",
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.height(12.dp))

            // Botones de foto
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = {
                    pickImage.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Text("Desde galería")
                }

                OutlinedButton(onClick = { takePicture.launch(null) }) {
                    Text("Tomar foto")
                }
            }

            Spacer(Modifier.height(12.dp))


            Text(ui.nombreCompleto, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(4.dp))
            Text(text = "Fecha de Nacimiento: ${ui.fechaNacimiento.ifBlank { "-" }}")
            Text(ui.email)
            Text("Teléfono: ${ui.telefono}")
            Text(text = "Región: ${ui.region.ifBlank { "-" }}")
            Text(text = "Comuna: ${ui.comuna.ifBlank { "-" }}")


            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onEditar) { Text("Editar") }
                Button(onClick = onCerrarSesion) { Text("Cerrar sesión", color = MaterialTheme.colorScheme.onPrimary) }
            }

            Spacer(Modifier.height(24.dp))
            Text("Historial de compras", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            ui.compras.forEach {
                ListItem(
                    headlineContent   = { Text(it.titulo) },
                    supportingContent = { Text("SKU ${it.sku} · ${it.fechaStr}") },
                    trailingContent   = { Text(it.subtotalCLP()) }
                )
                HorizontalDivider()
            }

            if (ui.compras.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Text("Total: ${ui.totalComprasCLP}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}