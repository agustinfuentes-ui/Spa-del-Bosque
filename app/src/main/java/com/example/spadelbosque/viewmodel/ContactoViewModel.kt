package com.example.spadelbosque.viewmodel

import androidx.lifecycle.ViewModel
import com.example.spadelbosque.ui.components.UsuarioErrores
import com.example.spadelbosque.ui.components.UsuarioUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.flow.update

class ContactoViewModel : ViewModel() {
    private val _estado = MutableStateFlow(UsuarioUiState())
    val estado : StateFlow<UsuarioUiState> = _estado.asStateFlow()
    val asuntosDisponibles = listOf("Consulta","Solicitud Reserva","Cancelacion Reserva","Reclamo")

    fun onNombreChange(valor: String) {
        val errorNombre = when {
            valor.any { it.isDigit() } -> "El nombre no puede contener números."
            !valor.all { it.isLetter() || it.isWhitespace() } -> "El nombre solo puede contener letras y espacios."
            else -> null
        }

        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = errorNombre)) }
    }
    fun onCorreoChange(valor: String){
        val dominiosPermitidos = listOf("gmail.com", "hotmail.com", "outlook.com", "duoc.cl", "profesor.duoc.cl")
        val dominio = valor.substringAfter("@")
        val errorCorreo = when {
            "@" in valor && dominio.isNotBlank() -> {
                if (dominiosPermitidos.any {dominio.equals(it, ignoreCase = true) }){
                    null
                }else{
                    "Dominio no permitido"
                }
            }
            "@" in valor && dominio.isBlank() -> {
                "Dominio invalido"
            }
            else ->{
              null
            }
        }
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = errorCorreo)) }
    }
    fun onAsuntoChange(valor: String) {
        _estado.update { it.copy(asunto = valor, errores = it.errores.copy(asunto = null), asuntoMenu = false) }
    }
    fun onAuntoMenuChange(abierto: Boolean) {
        _estado.update { it.copy(asuntoMenu = abierto) }
    }

    fun onMensajeChange(mensaje: String) {
        if (mensaje.length <= 500) {
            _estado.update { it.copy(mensaje = mensaje) }
        }
    }

    fun limpiarFormulario () {
        _estado.value = UsuarioUiState()
    }
    fun validaFormulario(): Boolean{
        val estadoActual = _estado.value
        val errorNombreFinal = if (estadoActual.nombre.isBlank()) "El nombre es obligatorio." else _estado.value.errores.nombre
        val errorCorreo = if (estadoActual.correo.isBlank()) "El correo es obligatorio." else _estado.value.errores.correo
        val errorAsunto = when{
            estadoActual.asunto.isBlank() -> "Campo obligatorio"
            else -> null
        }
        val errorMensaje = when{
            estadoActual.mensaje.isBlank()  -> "Campo obligatorio"
            estadoActual.mensaje.length > 500 -> "Máximo 500 caracteres"
            else -> null
        }

        val errores = UsuarioErrores(
            nombre = errorNombreFinal,
            correo = errorCorreo,
            asunto = errorAsunto,
            mensaje = errorMensaje
        )
        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.asunto,
            errores.mensaje
        ).isNotEmpty()

        _estado.update {it.copy(errores = errores) }

        return !hayErrores
    }

}
