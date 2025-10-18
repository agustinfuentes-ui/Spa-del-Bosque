package com.example.spadelbosque.ui.screens.contacto

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.spadelbosque.ui.theme.SpaTheme
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spadelbosque.viewmodel.ContactoViewModel

@Composable
fun ContactoScreen(navController: NavController, viewModel: ContactoViewModel) {
    val estado by viewModel.estado.collectAsState()
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = estado.nombre,
            onValueChange = viewModel::onNombreChange,
            label = { Text("Nombre") },
            isError = estado.errores.nombre != null,
            supportingText = {
                estado.errores.nombre?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = estado.correo,
            onValueChange = viewModel::onCorreoChange,
            label = { Text("Correo") },
            isError = estado.errores.correo != null,
            supportingText = {
                estado.errores.correo?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
            modifier = Modifier.fillMaxWidth()
        )
        // --- CAMPO PARA EL ASUNTO ---
        OutlinedTextField(
            value = estado.asunto,
            onValueChange = viewModel::onAsuntoChange,
            label = { Text("Asunto") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.mensaje,
            onValueChange = viewModel::onMensajeChange,
            label = { Text("Mensaje") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            supportingText = {
                Text(
                    text = "${estado.mensaje.length} / 500",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        )


        Button(onClick = { /* Lógica de envío */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Enviar Mensaje")
        }
    }
}
