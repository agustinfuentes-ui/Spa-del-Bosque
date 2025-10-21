package com.example.spadelbosque.repository

import com.example.spadelbosque.model.Usuario
/**
 * Interfaz que define las operaciones de autenticación.
 * Permite cambiar la implementación (Room, API, etc.) sin afectar el ViewModel.
 */
interface AuthRepository {
    // Registro
    suspend fun registrarUsuario(usuario: Usuario): Boolean

    // Login
    suspend fun validarCredenciales(correo: String, password: String): Usuario?

    // Verificaciones
    suspend fun existeUsuario(correo: String): Boolean

    // Sesión
    suspend fun guardarSesion(usuario: Usuario)
    suspend fun obtenerSesionActiva(): Usuario?
    suspend fun cerrarSesion()
    suspend fun haySesionActiva(): Boolean
}