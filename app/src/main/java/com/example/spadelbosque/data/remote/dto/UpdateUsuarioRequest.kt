package com.example.spadelbosque.data.remote.dto

data class UpdateUsuarioRequest(
    val nombres: String?,
    val apellidos: String?,
    val telefono: String?,
    val region: String?,
    val comuna: String?,
    val fechaNacimiento: String?


)