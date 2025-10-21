package com.example.spadelbosque.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * DAO (Data Access Object) que define las operaciones de base de datos.
 * Room genera automáticamente la implementación de estas funciones.
 */
@Dao
interface UsuarioDao {

    /**
     * Inserta un nuevo usuario en la base de datos.
     * Si el correo ya existe, falla (onConflict = ABORT)
     */
    @Insert(onConflict = OnConflictStrategy.Companion.ABORT)
    suspend fun insertar(usuario: UsuarioEntity)

    /**
     * Busca un usuario por correo y contraseña (para login).
     * @return Usuario si las credenciales son correctas, null si no
     */
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND password = :password LIMIT 1")
    suspend fun validarCredenciales(correo: String, password: String): UsuarioEntity?

    /**
     * Verifica si existe un usuario con el correo dado.
     * @return true si existe, false si no
     */
    @Query("SELECT EXISTS(SELECT 1 FROM usuarios WHERE correo = :correo)")
    suspend fun existeUsuario(correo: String): Boolean

    /**
     * Obtiene un usuario por su correo.
     */
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun obtenerPorCorreo(correo: String): UsuarioEntity?

    /**
     * Obtiene todos los usuarios (útil para debug).
     */
    @Query("SELECT * FROM usuarios")
    suspend fun obtenerTodos(): List<UsuarioEntity>

    /**
     * Elimina todos los usuarios (útil para limpiar en desarrollo).
     */
    @Query("DELETE FROM usuarios")
    suspend fun eliminarTodos()
}