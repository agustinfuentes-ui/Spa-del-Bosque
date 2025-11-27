package com.example.spadelbosque.data.remote.dto

import com.example.spadelbosque.model.Usuario

fun UsuarioResponse.toUsuarioLocal(): Usuario {
    return Usuario(
        id = id,
        nombres = nombres,
        apellidos = apellidos,
        email = email,
        telefono = telefono ?: "",
        password = "",
        region = region,
        comuna = comuna,
        fechaNacimiento = fechaNacimiento,
        rol = rol,
        estado = estado,
        fotoUri = null
    )
}
