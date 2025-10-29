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
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

@ExperimentalMaterial3Api
@Composable
fun ContactoScreen(navController: NavController, viewModel: ContactoViewModel) {
    val estado by viewModel.estado.collectAsState()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = viewModel::onNombreChange,
                label = { Text("Nombre",
                    color = MaterialTheme.colorScheme.primary) },
                isError = estado.errores.nombre != null,
                supportingText = {
                    estado.errores.nombre?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth(),

            )
            OutlinedTextField(
                value = estado.correo,
                onValueChange = viewModel::onCorreoChange,
                label = { Text("Correo",
                    color = MaterialTheme.colorScheme.primary) },
                isError = estado.errores.correo != null,
                supportingText = {
                    estado.errores.correo?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth()
            )
            // --- CAMPO PARA EL ASUNTO ---
            ExposedDropdownMenuBox(
                expanded = estado.asuntoMenu,
                onExpandedChange = viewModel::onAuntoMenuChange,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    value = estado.asunto,
                    onValueChange = {},
                    label = {
                        Text(
                            "Asunto",
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = estado.asuntoMenu) },
                    isError = estado.errores.asunto != null,
                    supportingText = {
                        estado.errores.asunto?.let {
                            Text(
                                it,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                ExposedDropdownMenu(
                    expanded = estado.asuntoMenu,
                    onDismissRequest = { viewModel.onAuntoMenuChange(false) }
                ) {
                    viewModel.asuntosDisponibles.forEach { asuntoSeleccionado ->
                        DropdownMenuItem(
                            text = { Text(asuntoSeleccionado) },
                            onClick = {
                                viewModel.onAsuntoChange(asuntoSeleccionado)
                            }
                        )
                    }
                }
            }


            OutlinedTextField(
                value = estado.mensaje,
                onValueChange = viewModel::onMensajeChange,
                label = { Text("Mensaje",
                    color = MaterialTheme.colorScheme.primary) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                supportingText = {
                    Text(
                        text = "${500-estado.mensaje.length}",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            )


            Button(onClick = {
                val esValido = viewModel.validaFormulario()
                if (esValido) {
                    scope.launch {
                        snackBarHostState.showSnackbar("Mensaje enviado correctamente")
                        viewModel.limpiarFormulario()
                    }
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Enviar Mensaje")
            }
        }
    }
}
