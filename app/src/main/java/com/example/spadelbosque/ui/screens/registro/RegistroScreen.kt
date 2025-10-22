package com.example.spadelbosque.ui.screens.registro

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spadelbosque.R
import com.example.spadelbosque.navigation.Route
import com.example.spadelbosque.ui.components.LoadingScreen
import com.example.spadelbosque.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.animation.*

// Pantalla de registro de nuevos usuarios
@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    val estado by viewModel.registroState.collectAsState()
    val isLoading by viewModel.registroLoading.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var mostrarExito by remember { mutableStateOf(false) }

    // Mostrar loading full screen si está registrando
    if (isLoading) {
        LoadingScreen(mensaje = "Creando tu cuenta...")
        return
    }

    // Ocultar mensaje de éxito después de unos segundos
    LaunchedEffect(mostrarExito) {
        if (mostrarExito) {
            delay(1500) // espera a que la animación se vea
            navController.navigate(Route.Login.path) {
                popUpTo(Route.Registro.path) { inclusive = true }
            }
            mostrarExito = false
        }
    }
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Espaciador superior
            Spacer(modifier = Modifier.height(32.dp))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo SPA del Bosque",
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Título
            Text(
                text = "SPA del Bosque",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Card del formulario
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    // Título del formulario
                    Text(
                        text = "Crea tu cuenta",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo Nombres
                    OutlinedTextField(
                        value = estado.nombres,
                        onValueChange = viewModel::onNombresChange,
                        label = { Text("Nombres") },
                        isError = estado.errores.nombres != null,
                        supportingText = {
                            estado.errores.nombres?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo Apellidos
                    OutlinedTextField(
                        value = estado.apellidos,
                        onValueChange = viewModel::onApellidosChange,
                        label = { Text("Apellidos") },
                        isError = estado.errores.apellidos != null,
                        supportingText = {
                            estado.errores.apellidos?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo Correo
                    OutlinedTextField(
                        value = estado.correo,
                        onValueChange = viewModel::onCorreoRegistroChange,
                        label = { Text("Correo") },
                        placeholder = { Text("usuario@duoc.cl") },
                        isError = estado.errores.correo != null,
                        supportingText = {
                            estado.errores.correo?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo Contraseña
                    OutlinedTextField(
                        value = estado.password,
                        onValueChange = viewModel::onPasswordRegistroChange,
                        label = { Text("Contraseña") },
                        visualTransformation = if (estado.mostrarPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(onClick = { viewModel.toggleMostrarPasswordRegistro() }) {
                                Icon(
                                    imageVector = if (estado.mostrarPassword) {
                                        Icons.Filled.VisibilityOff
                                    } else {
                                        Icons.Filled.Visibility
                                    },
                                    contentDescription = if (estado.mostrarPassword) {
                                        "Ocultar contraseña"
                                    } else {
                                        "Mostrar contraseña"
                                    }
                                )
                            }
                        },
                        isError = estado.errores.password != null,
                        supportingText = {
                            estado.errores.password?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo Confirmar Contraseña
                    OutlinedTextField(
                        value = estado.confirmarPassword,
                        onValueChange = viewModel::onConfirmarPasswordChange,
                        label = { Text("Confirmar contraseña") },
                        visualTransformation = if (estado.mostrarConfirmarPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(onClick = { viewModel.toggleMostrarConfirmarPassword() }) {
                                Icon(
                                    imageVector = if (estado.mostrarConfirmarPassword) {
                                        Icons.Filled.VisibilityOff
                                    } else {
                                        Icons.Filled.Visibility
                                    },
                                    contentDescription = if (estado.mostrarConfirmarPassword) {
                                        "Ocultar contraseña"
                                    } else {
                                        "Mostrar contraseña"
                                    }
                                )
                            }
                        },
                        isError = estado.errores.confirmarPassword != null,
                        supportingText = {
                            estado.errores.confirmarPassword?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo Teléfono
                    OutlinedTextField(
                        value = estado.telefono,
                        onValueChange = viewModel::onTelefonoChange,
                        label = { Text("Teléfono") },
                        placeholder = { Text("9 1234 5678") },
                        isError = estado.errores.telefono != null,
                        supportingText = {
                            estado.errores.telefono?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón Registrarse
                    Button(
                        onClick = {
                            viewModel.intentarRegistro(
                                onSuccess = {
                                    mostrarExito = true
                                    coroutineScope.launch {
                                        delay(1500) // tiempo para mostrar animación
                                        navController.navigate(Route.Login.path) {
                                            popUpTo(Route.Registro.path) { inclusive = true }
                                        }
                                        mostrarExito = false
                                    }
                                },
                                onError = { mensaje ->
                                    // Opcional: mostrar Snackbar o errores en campos
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading, // se desactiva mientras carga
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Registrarse", style = MaterialTheme.typography.titleMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedVisibility(
                        visible = mostrarExito,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
                        exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 2 })
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    "¡Registro exitoso!",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }

                    // Divisor
                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(16.dp))

                    // Texto de login
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "¿Ya tienes cuenta? ",
                            style = MaterialTheme.typography.bodySmall
                        )
                        TextButton(
                            onClick = { navController.navigate(Route.Login.path) },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Ingresa aquí",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

