package com.example.spadelbosque.data.remote.dto

data class UsuarioResponse(
    val id: Long,
    val nombres: String,
    val apellidos: String,
    val email: String,
    val telefono: String?,
    val region: String?,
    val comuna: String?,
    val fechaNacimiento: String?,
    val rol: String,
    val estado: String,
    val createdAt: String
)
