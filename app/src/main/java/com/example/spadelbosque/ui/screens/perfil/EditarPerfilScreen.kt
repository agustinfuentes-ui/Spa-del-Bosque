package com.example.spadelbosque.ui.screens.perfil

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.navigation.NavController
import com.example.spadelbosque.viewmodel.PerfilViewModel


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EditarPerfilScreen(
    navController: NavController,
    viewModel: PerfilViewModel
) {
    val ui by viewModel.ui.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    // Variables para manejar el estado del dropdown
    var regionExpanded by remember { mutableStateOf(false) }
    var comunaExpanded by remember { mutableStateOf(false) }

    val comunas = ui.listaDeComunas // Obtiene la lista desde el ViewModel
    val regiones = ui.listaDeRegiones // Obtiene la lista desde el ViewModel

    val datePickerState = rememberDatePickerState()
    var mostrarDatePicker by remember { mutableStateOf(false) }


    LaunchedEffect(errorMsg) {
        errorMsg?.let {
            snackbarHostState.showSnackbar(it)
            errorMsg = null
        }
    }

    Box(Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text("Editar perfil", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = ui.nombres,
                onValueChange = viewModel::onNombres,
                label = { Text("Nombres") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ui.apellidos,
                onValueChange = viewModel::onApellidos,
                label = { Text("Apellidos") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ui.telefono,
                onValueChange = viewModel::onTelefono,
                label = { Text("Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ui.email,
                onValueChange = viewModel::onEmail,
                enabled = true,
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // Altura estándar de un OutlinedTextField
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        shape = MaterialTheme.shapes.extraSmall
                    )
                    .clickable { mostrarDatePicker = true } // <-- El clic se maneja aquí
            ) {
                Text(
                    text = ui.fechaNacimiento.ifEmpty { "Fecha de Nacimiento" },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp),
                    color = if (ui.fechaNacimiento.isEmpty()) {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }

            // REEMPLAZO DEL TEXTFIELD DE REGION
            ExposedDropdownMenuBox(
                expanded = regionExpanded,
                onExpandedChange = { regionExpanded = !regionExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Este es el TextField que se muestra siempre
                OutlinedTextField(
                    value = ui.region, // El valor seleccionado actualmente
                    onValueChange = {}, // No se necesita, la selección se hace en el menú
                    readOnly = true,    // Importante: para que no se pueda escribir texto
                    label = { Text("Region") },
                    trailingIcon = {
                        // El icono de flecha que indica que es un dropdown
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor() // Ancla el menú al TextField
                        .fillMaxWidth()
                )

                // Este es el menú que se despliega
                ExposedDropdownMenu(
                    expanded = regionExpanded,
                    onDismissRequest = { regionExpanded = false }
                ) {
                    // Itera sobre tu lista de opciones
                    regiones.forEach { regionSeleccionada ->
                        DropdownMenuItem(
                            text = { Text(regionSeleccionada) },
                            onClick = {
                                viewModel.onRegionSelected(regionSeleccionada) // Llama al ViewModel
                                regionExpanded = false // Cierra el menú al seleccionar
                            }
                        )
                    }
                }
            }

            // REEMPLAZO DEL TEXTFIELD DE COMUNA
            ExposedDropdownMenuBox(
                expanded = comunaExpanded,
                onExpandedChange = { comunaExpanded = !comunaExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Este es el TextField que se muestra siempre
                OutlinedTextField(
                    value = ui.comuna, // El valor seleccionado actualmente
                    onValueChange = {}, // No se necesita, la selección se hace en el menú
                    readOnly = true,    // Importante: para que no se pueda escribir texto
                    label = { Text("Comuna") },
                    trailingIcon = {
                        // El icono de flecha que indica que es un dropdown
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = comunaExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor() // Ancla el menú al TextField
                        .fillMaxWidth()
                )

                // Este es el menú que se despliega
                ExposedDropdownMenu(
                    expanded = comunaExpanded,
                    onDismissRequest = { comunaExpanded = false }
                ) {
                    // Itera sobre tu lista de opciones
                    comunas.forEach { comunaSeleccionada ->
                        DropdownMenuItem(
                            text = { Text(comunaSeleccionada) },
                            onClick = {
                                viewModel.onComunaSelected(comunaSeleccionada) // Llama al ViewModel
                                comunaExpanded= false // Cierra el menú al seleccionar
                            }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.guardarCambios(
                        onSuccess = { navController.popBackStack() },
                        onError = { msg -> errorMsg = msg }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }


        if (mostrarDatePicker) {
            DatePickerDialog(
                onDismissRequest = { mostrarDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            mostrarDatePicker = false
                            // Es importante solo tomar la fecha si el usuario seleccionó una
                            datePickerState.selectedDateMillis?.let { millis ->
                                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                // Añadir UTC evita que la fecha cambie por la zona horaria del teléfono
                                sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
                                val fechaFormateada = sdf.format(Date(millis))
                                viewModel.onFechaNacimiento(fechaFormateada)
                            }
                        }
                    ) {
                        Text("ACEPTAR", color = MaterialTheme.colorScheme.primary)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { mostrarDatePicker = false }
                    ) {
                        Text("CANCELAR")
                    }
                }
            ) {

                DatePicker(state = datePickerState)
            }
        }

        if (ui.cargando) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
