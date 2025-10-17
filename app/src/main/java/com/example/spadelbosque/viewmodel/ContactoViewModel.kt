package com.example.spadelbosque.viewmodel

import androidx.lifecycle.ViewModel
import com.example.spadelbosque.ui.components.UsuarioErrores
import com.example.spadelbosque.ui.components.UsuarioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.flow.update

class ContactoViewModel : ViewModel() {
    private val _estado = MutableStateFlow(UsuarioUiState())
    val estado : StateFlow<UsuarioUiState> = _estado
    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }
    fun onCorreoChange(valor: String){
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }
    fun onAsuntoChange(asunto: String) {
        _estado.update { it.copy(asunto = asunto) }
    }

    fun onMensajeChange(mensaje: String) {
        // Aplica la validación del límite de caracteres aquí
        if (mensaje.length <= 500) {
            _estado.update { it.copy(mensaje = mensaje) }
        }
    }

    fun validaFormulario(): Boolean{
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "Campo obligatorio" else null,
            correo = if (estadoActual.correo.isBlank()) "Campo obligatorio" else null
        )
        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo
        ).isNotEmpty()

        _estado.update {it.copy(errores = errores) }

        return !hayErrores
    }
}