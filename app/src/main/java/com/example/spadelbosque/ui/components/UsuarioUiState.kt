package com.example.spadelbosque.ui.components

data class UsuarioUiState(
    val nombre:String = "",
    val correo:String = "",
    val asunto: String = "",
    val asuntoMenu: Boolean = false,
    val mensaje: String = "",
    val errores: UsuarioErrores = UsuarioErrores()
)
data class UsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val asunto: String? = null,
    val mensaje: String? = null
)
