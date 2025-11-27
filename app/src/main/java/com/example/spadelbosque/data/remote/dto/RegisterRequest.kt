package com.example.spadelbosque.data.remote.dto

import java.time.LocalDate
data class RegisterRequest(
    val nombres: String,
    val apellidos: String,
    val email: String,
    val password:  String,
    val telefono: String,
    val region: String? = null,
    val comuna: String? = null,
    val fechaNacimiento: LocalDate? = null
)