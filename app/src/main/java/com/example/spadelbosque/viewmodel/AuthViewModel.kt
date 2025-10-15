package com.example.spadelbosque.viewmodel

import androidx.lifecycle.ViewModel
import com.example.spadelbosque.model.LoginErrores
import com.example.spadelbosque.model.Login
import com.example.spadelbosque.model.RegistroErrores
import com.example.spadelbosque.model.Registro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel : ViewModel() {

    // login
    private val _loginState = MutableStateFlow(Login())
    val loginState: StateFlow<Login> = _loginState

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

    // registro
    private val _registroState = MutableStateFlow(Registro())
    val registroState: StateFlow<Registro> = _registroState

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
        // Solo permitir números
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
}