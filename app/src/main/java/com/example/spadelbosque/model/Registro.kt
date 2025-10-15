package com.example.spadelbosque.model

data class Registro (
    val nombres: String = "",
    val apellidos: String = "",
    val correo: String = "",
    val password: String = "",
    val confirmarPassword: String = "",
    val telefono: String = "",
    val mostrarPassword: Boolean = false,
    val mostrarConfirmarPassword: Boolean = false,
    val errores: RegistroErrores = RegistroErrores()
    )

    // Errores de validación para Registro
    data class RegistroErrores(
        val nombres: String? = null,
        val apellidos: String? = null,
        val correo: String? = null,
        val password: String? = null,
        val confirmarPassword: String? = null,
        val telefono: String? = null
    )