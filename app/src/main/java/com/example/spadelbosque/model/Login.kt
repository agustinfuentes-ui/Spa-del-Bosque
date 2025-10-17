package com.example.spadelbosque.model

data class Login (
    val correo: String = "",
    val password: String = "",
    val recordarme: Boolean = false,
    val mostrarPassword: Boolean = false,
    val errores: LoginErrores = LoginErrores()
    )

    // Errores de validación para Login
    data class LoginErrores(
        val correo: String? = null,
        val password: String? = null
    )