package com.example.spadelbosque.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.spadelbosque.model.LoginErrores
import com.example.spadelbosque.model.Login
import com.example.spadelbosque.model.RegistroErrores
import com.example.spadelbosque.model.Registro
import com.example.spadelbosque.model.Usuario
import com.example.spadelbosque.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel que maneja la autenticación usando Room.
 * Extiende AndroidViewModel para acceder al Context.
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    // Repository para persistencia con Room
    private val repository = AuthRepositoryImpl(application)

    // ========== LOGIN ==========

    private val _loginState = MutableStateFlow(Login())
    val loginState: StateFlow<Login> = _loginState

    private val _loginLoading = MutableStateFlow(false)
    val loginLoading: StateFlow<Boolean> = _loginLoading

    fun onCorreoLoginChange(valor: String) {
        _loginState.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onPasswordLoginChange(valor: String) {
        _loginState.update { it.copy(password = valor, errores = it.errores.copy(password = null)) }
    }

    fun onRecordarmeChange(valor: Boolean) {
        _loginState.update { it.copy(recordarme = valor) }
    }

    fun toggleMostrarPassword() {
        _loginState.update { it.copy(mostrarPassword = !it.mostrarPassword) }
    }

    fun validarLogin(): Boolean {
        val estadoActual = _loginState.value
        val errores = LoginErrores(
            correo = when {
                estadoActual.correo.isBlank() -> "El correo es obligatorio"
                !estadoActual.correo.contains("@") -> "Ingresa un correo válido"
                else -> null
            },
            password = when {
                estadoActual.password.isBlank() -> "La contraseña es obligatoria"
                estadoActual.password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
                else -> null
            }
        )

        val hayErrores = listOfNotNull(
            errores.correo,
            errores.password
        ).isNotEmpty()

        _loginState.update { it.copy(errores = errores) }
        return !hayErrores
    }

    /**
     * Intenta hacer login con las credenciales ingresadas.
     * Usa coroutine porque Room requiere suspend functions.
     * @param onSuccess Callback cuando el login es exitoso
     * @param onError Callback cuando hay error
     */
    fun intentarLogin(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!validarLogin()) return

        _loginLoading.value = true

        viewModelScope.launch {
            try {
                val estadoActual = _loginState.value
                val usuario = repository.validarCredenciales(
                    estadoActual.correo,
                    estadoActual.password
                )

                if (usuario != null) {
                    // Guardar sesión
                    repository.guardarSesion(usuario)
                    _loginLoading.value = false
                    onSuccess()
                } else {
                    // Credenciales incorrectas
                    _loginState.update {
                        it.copy(
                            errores = it.errores.copy(
                                password = "Correo o contraseña incorrectos"
                            )
                        )
                    }
                    _loginLoading.value = false
                    onError("Credenciales incorrectas")
                }
            } catch (e: Exception) {
                _loginLoading.value = false
                onError("Error al iniciar sesión: ${e.message}")
            }
        }
    }

    // ========== REGISTRO ==========

    private val _registroState = MutableStateFlow(Registro())
    val registroState: StateFlow<Registro> = _registroState

    private val _registroLoading = MutableStateFlow(false)
    val registroLoading: StateFlow<Boolean> = _registroLoading

    fun onNombresChange(valor: String) {
        _registroState.update { it.copy(nombres = valor, errores = it.errores.copy(nombres = null)) }
    }

    fun onApellidosChange(valor: String) {
        _registroState.update { it.copy(apellidos = valor, errores = it.errores.copy(apellidos = null)) }
    }

    fun onCorreoRegistroChange(valor: String) {
        _registroState.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onPasswordRegistroChange(valor: String) {
        _registroState.update { it.copy(password = valor, errores = it.errores.copy(password = null)) }
    }

    fun onConfirmarPasswordChange(valor: String) {
        _registroState.update { it.copy(confirmarPassword = valor, errores = it.errores.copy(confirmarPassword = null)) }
    }

    fun onTelefonoChange(valor: String) {
        val soloNumeros = valor.filter { it.isDigit() }
        _registroState.update { it.copy(telefono = soloNumeros, errores = it.errores.copy(telefono = null)) }
    }

    fun toggleMostrarPasswordRegistro() {
        _registroState.update { it.copy(mostrarPassword = !it.mostrarPassword) }
    }

    fun toggleMostrarConfirmarPassword() {
        _registroState.update { it.copy(mostrarConfirmarPassword = !it.mostrarConfirmarPassword) }
    }

    fun validarRegistro(): Boolean {
        val estadoActual = _registroState.value
        val errores = RegistroErrores(
            nombres = when {
                estadoActual.nombres.isBlank() -> "El nombre es obligatorio"
                else -> null
            },
            apellidos = when {
                estadoActual.apellidos.isBlank() -> "El apellido es obligatorio"
                else -> null
            },
            correo = when {
                estadoActual.correo.isBlank() -> "El correo es obligatorio"
                !estadoActual.correo.contains("@") -> "Ingresa un correo válido"
                !estadoActual.correo.matches(Regex(".*(@duoc\\.cl|@profesor\\.duoc\\.cl|@gmail\\.com)$")) ->
                    "Ingresa un correo válido (@duoc.cl, @profesor.duoc.cl, @gmail.com)"
                else -> null
            },
            password = when {
                estadoActual.password.isBlank() -> "La contraseña es obligatoria"
                estadoActual.password.length < 4 || estadoActual.password.length > 10 ->
                    "La contraseña debe tener entre 4 y 10 caracteres"
                else -> null
            },
            confirmarPassword = when {
                estadoActual.confirmarPassword.isBlank() -> "Confirma tu contraseña"
                estadoActual.confirmarPassword != estadoActual.password -> "Las contraseñas deben coincidir"
                else -> null
            },
            telefono = when {
                estadoActual.telefono.isBlank() -> "El teléfono es obligatorio"
                estadoActual.telefono.length < 9 || estadoActual.telefono.length > 11 ->
                    "Ingresa un teléfono chileno válido (9-11 dígitos)"
                else -> null
            }
        )

        val hayErrores = listOfNotNull(
            errores.nombres,
            errores.apellidos,
            errores.correo,
            errores.password,
            errores.confirmarPassword,
            errores.telefono
        ).isNotEmpty()

        _registroState.update { it.copy(errores = errores) }
        return !hayErrores
    }

    /**
     * Intenta registrar un nuevo usuario.
     * @param onSuccess Callback cuando el registro es exitoso
     * @param onError Callback cuando hay error
     */
    fun intentarRegistro(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!validarRegistro()) return

        _registroLoading.value = true

        viewModelScope.launch {
            try {
                val estadoActual = _registroState.value

                // Verificar si el correo ya existe
                if (repository.existeUsuario(estadoActual.correo)) {
                    _registroState.update {
                        it.copy(
                            errores = it.errores.copy(
                                correo = "Este correo ya está registrado"
                            )
                        )
                    }
                    _registroLoading.value = false
                    onError("El correo ya está registrado")
                    return@launch
                }

                // Crear nuevo usuario
                val nuevoUsuario = Usuario(
                    nombres = estadoActual.nombres,
                    apellidos = estadoActual.apellidos,
                    correo = estadoActual.correo,
                    password = estadoActual.password,
                    telefono = estadoActual.telefono
                )

                // Guardar en Room
                val exito = repository.registrarUsuario(nuevoUsuario)

                _registroLoading.value = false

                if (exito) {
                    // Limpiar formulario
                    _registroState.value = Registro()
                    onSuccess()
                } else {
                    onError("Error al registrar usuario")
                }
            } catch (e: Exception) {
                _registroLoading.value = false
                onError("Error: ${e.message}")
            }
        }
    }
}