package com.example.spadelbosque.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.spadelbosque.model.Usuario

/**
 * Entity de Room que representa la tabla de usuarios en SQLite.
 * Cada propiedad es una columna en la base de datos.
 */
@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey
    val id: Long?,
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val password: String,
    val telefono: String,
    val fotoUri: String? = null,
    val fechaRegistro: Long = System.currentTimeMillis()
)

// Funciones de conversión entre Entity (BD) y Model (Dominio)

/**
 * Convierte un UsuarioEntity (de la BD) a Usuario (modelo de dominio)
 */
fun UsuarioEntity.toUsuario(): Usuario {
    return Usuario(
        id = id,
        nombres = nombres,
        apellidos = apellidos,
        email = correo,
        password = password,
        telefono = telefono,
        fotoUri = fotoUri
    )
}

/**
 * Convierte un Usuario (modelo) a UsuarioEntity (para guardar en BD)
 */
fun Usuario.toEntity(): UsuarioEntity {
    return UsuarioEntity(
        id = id,
        nombres = nombres,
        apellidos = apellidos,
        correo = email,
        password = password,
        telefono = telefono,
        fotoUri = fotoUri
    )
}