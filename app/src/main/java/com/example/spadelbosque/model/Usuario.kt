package com.example.spadelbosque.model

/**
 * Modelo de dominio para Usuario.
 * Este es el modelo que se usa en ViewModels y UI.
 */
data class Usuario(
    val id: Long?,
    val nombres: String = "",
    val apellidos: String = "",
    val email: String = "",
    val password: String = "",
    val telefono: String = "",
    val region: String? = null,
    val comuna: String? = null,
    val fechaNacimiento: String? = null,
    val rol: String? = null,
    val estado: String? = null,
    val fotoUri: String? = null
) {
    // Nombre completo para mostrar en UI
    fun nombreCompleto(): String = "$nombres $apellidos"

    // Validar si el usuario tiene todos los datos
    fun esValido(): Boolean {
        return nombres.isNotBlank() &&
                apellidos.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                telefono.isNotBlank()
    }
}