package com.example.spadelbosque.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.spadelbosque.data.local.AppDatabase
import com.example.spadelbosque.data.local.toEntity
import com.example.spadelbosque.data.local.toUsuario
import com.example.spadelbosque.model.Usuario
import com.google.gson.Gson

/**
 * Implementación de AuthRepository usando Room (SQLite) para usuarios
 * y SharedPreferences para la sesión activa.
 */
class AuthRepositoryImpl(context: Context) : AuthRepository {

    // Base de datos Room
    private val database = AppDatabase.getDatabase(context)
    private val usuarioDao = database.usuarioDao()

    // SharedPreferences solo para sesión activa (más rápido que Room)
    private val prefs: SharedPreferences =
        context.getSharedPreferences("spa_sesion", Context.MODE_PRIVATE)
    private val gson = Gson()

    // ========== REGISTRO ==========

    /**
     * Registra un nuevo usuario en la base de datos.
     * @return true si se registró exitosamente, false si el correo ya existe
     */
    override suspend fun registrarUsuario(usuario: Usuario): Boolean {
        return try {
            // Verificar si ya existe
            if (usuarioDao.existeUsuario(usuario.correo)) {
                return false
            }

            // Convertir a Entity y guardar en Room
            val entity = usuario.toEntity()
            usuarioDao.insertar(entity)
            true
        } catch (e: Exception) {
            false
        }
    }

    // ========== LOGIN ==========

    /**
     * Valida las credenciales del usuario contra la base de datos.
     * @return Usuario si las credenciales son correctas, null si no
     */
    override suspend fun validarCredenciales(correo: String, password: String): Usuario? {
        return try {
            val entity = usuarioDao.validarCredenciales(correo, password)
            entity?.toUsuario()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Verifica si existe un usuario con el correo dado.
     */
    override suspend fun existeUsuario(correo: String): Boolean {
        return try {
            usuarioDao.existeUsuario(correo)
        } catch (e: Exception) {
            false
        }
    }

    // ========== SESIÓN ==========

    /**
     * Guarda la sesión activa en SharedPreferences.
     * (Usamos SharedPreferences aquí porque es más rápido que Room para este caso)
     */
    override suspend fun guardarSesion(usuario: Usuario) {
        val usuarioJson = gson.toJson(usuario)
        prefs.edit().putString("sesion_activa", usuarioJson).apply()
    }

    /**
     * Obtiene el usuario de la sesión activa.
     */
    override suspend fun obtenerSesionActiva(): Usuario? {
        val usuarioJson = prefs.getString("sesion_activa", null) ?: return null
        return try {
            gson.fromJson(usuarioJson, Usuario::class.java)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Cierra la sesión eliminando los datos de SharedPreferences.
     */
    override suspend fun cerrarSesion() {
        prefs.edit().remove("sesion_activa").apply()
    }

    /**
     * Verifica si hay una sesión activa.
     */
    override suspend fun haySesionActiva(): Boolean {
        return obtenerSesionActiva() != null
    }
}