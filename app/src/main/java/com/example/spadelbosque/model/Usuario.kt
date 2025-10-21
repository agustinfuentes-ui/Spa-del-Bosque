package com.example.spadelbosque.model

/**
 * Modelo de dominio para Usuario.
 * Este es el modelo que se usa en ViewModels y UI.
 */
data class Usuario(
    val id: String = "",
    val nombres: String = "",
    val apellidos: String = "",
    val correo: String = "",
    val password: String = "",
    val telefono: String = "",
    val fotoUri: String? = null
) {
    // Nombre completo para mostrar en UI
    fun nombreCompleto(): String = "$nombres $apellidos"

    // Validar si el usuario tiene todos los datos
    fun esValido(): Boolean {
        return nombres.isNotBlank() &&
                apellidos.isNotBlank() &&
                correo.isNotBlank() &&
                password.isNotBlank() &&
                telefono.isNotBlank()
    }
}