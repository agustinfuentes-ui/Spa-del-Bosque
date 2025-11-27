package com.example.spadelbosque.data.remote.dto

import com.example.spadelbosque.model.Usuario

fun AuthResponse.toUsuarioLocal(): Usuario? {
    if (!exito) return null

    return Usuario(
        id = id ?: 0L,
        nombres = nombres ?: "",
        apellidos = apellidos ?: "",
        email = email ?: "",
        password = "",                // nunca se guarda desde backend
        telefono = telefono ?: "",
        region = region ?: "",
        comuna = comuna ?: "",
        fechaNacimiento = fechaNacimiento,
        rol = rol ?: "",
        estado = estado ?: "",
        fotoUri = null
    )
}
